<%@ page import="com.track.db.util.ItemUtil"%>
<%@ page import="com.track.db.util.InvUtil"%>
<%@ page import="com.track.util.*"%>
<%@ page import="com.track.dao.*"%>
<%@ page import="com.track.constants.*"%>
<%@ page import="javax.transaction.UserTransaction"%>
<%@ page import="java.util.Set"%>
<%@ include file="header.jsp"%>
<%@page import="com.track.db.util.PrdTypeUtil"%>
<%@page import="com.track.db.util.PrdClassUtil"%>


<%
String title = "Create Product Details";
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
<SCRIPT LANGUAGE="JavaScript">
     window.onload = function(){
     //document.getElementById("STKQTY").value = "0.000";
   	//  document.getElementById("MAXSTKQTY").value = "0.000";
	  document.getElementById("PRICE").value = parseFloat("0.00000").toFixed(document.getElementById("numberOfDecimal").value);
	  document.getElementById("COST").value = parseFloat("0.00000").toFixed(document.getElementById("numberOfDecimal").value);
	  document.getElementById("MINSELLINGPRICE").value = parseFloat("0.00000").toFixed(document.getElementById("numberOfDecimal").value);
	  $("input[name ='INCPRICE']").val(parseFloat("0.00000").toFixed(document.getElementById("numberOfDecimal").value));
	  document.getElementById("DISCOUNT").value = "0.0";
	  document.getElementById("PRODGST").value = "0.000";
	  document.getElementById("NETWEIGHT").value = "0.000";
	  document.getElementById("GROSSWEIGHT").value = "0.000";
	  document.getElementById("RENTALPRICE").value = parseFloat("0.00000").toFixed(document.getElementById("numberOfDecimal").value);
	  document.getElementById("SERVICEPRICE").value = parseFloat("0.00000").toFixed(document.getElementById("numberOfDecimal").value);
	  if (document.getElementById("NONE").checked == true) {
			document.getElementById("NONE").value = "NONE";
		/* 	$(".comproprice").hide(); */
		}

	}
	
	
	var subWin = null;
	function popUpWin(URL) {
	 	subWin = window.open(URL, 'Items', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=800,height=400,left = 200,top = 184');
	}
	
	function onNew(){
		$.ajax({
			type: "GET",
			url: "../product/auto-id",
			dataType: "json",
			beforeSend: function(){
				showLoader();
			}, 
			success: function(data) {
				$("#ITEM").val(data.PRODUCT);
			},
			error: function(data) {
				alert('Unable to generate product Category ID. Please try again later.');
			},
			complete: function(){
				hideLoader();
			}
		});
		/* var prdcls = document.form.PRD_CLS_ID.value;
	   document.form.action  = "../jsp/item_view.jsp?action=NEW&PRD_CLS_ID="+prdcls;
	   document.form.submit(); */
	}
	function onClear()
	{
		document.form.ITEM.value="";
		document.form.DESC.value="";
		document.form.TITLE.value="";
		//document.form.ARTIST.selectedIndex=0;
		//document.form.UOM.selectedIndex=0;
		document.form.REMARKS.value="";
		//document.form.PRD_CLS_ID.selectedIndex=0;
		document.form.MANUFACT.value="";
		document.form.STKQTY.value="";
		/* document.form.ASSET.value=""; */
		document.form.ITEM_CONDITION.value="";
		document.form.COST.value="";
		document.form.MINSELLINGPRICE.value="";
		document.form.DISCOUNT.value="";
		document.form.PRODGST.value="";
		document.form.LOC_0.value="";
		//document.form.PRD_BRAND.selectedIndex=0;
		document.form.MAXSTKQTY.value="";
		document.form.NETWEIGHT.value="0.000";
		document.form.GROSSWEIGHT.value="0.000";
		document.form.HSCODE.value="";
		document.form.COO.value="";
		document.form.PRICE.value="0.00000";
		document.form.COST.value="0.00000";
		document.form.MINSELLINGPRICE.value="0.00000";
		document.form.PRODGST.value="0.000";
		document.form.DYNAMIC_CUSTOMER_DISCOUNT_0.value="";
		document.form.DYNAMIC_SUPPLIER_DISCOUNT_0.value="";
		document.form.UOM.value="";
		document.form.PRD_CLS_DESC.value="";
		document.form.vendno.value="";
		document.form.vendname.value="";
		document.form.LOC_ID.value="";
		document.form.PRD_DEPT_DESC.value="";
// 		document.form.DEPT_DISPLAY_ID.value="";
		document.form.PRD_TYPE_DESC.value="";
		document.form.PRD_BRAND_DESC.value="";
		document.form.VINNO.value="";
		document.form.DIMENSION.value="";
		document.form.MODEL.value="";
		document.form.RENTALPRICE.value="0.00000";
		document.form.SERVICEPRICE.value="0.00000";
		document.form.PURCHASEUOM.value="";
		document.form.SALESUOM.value="";
		document.form.RENTALUOM.value="";
		document.form.SERVICEUOM.value="";
		document.form.INVENTORYUOM.value="";
		document.form.DESCRIPTION1.value="";
		document.form.DESCRIPTION2.value="";
        document.form.DESCRIPTION3.value="";
		document.form.DESCRIPTION4.value="";
		document.form.DESCRIPTION5.value="";
		document.form.DESCRIPTION6.value="";
		document.form.DESCRIPTION7.value="";
		document.form.DESCRIPTION8.value="";
		document.form.DESCRIPTION9.value="";
		document.form.DESCRIPTION10.value="";
		document.form.PRODUCT1.value="";
		document.form.PRODUCT2.value="";
        document.form.PRODUCT3.value="";
		document.form.PRODUCT4.value="";
		document.form.PRODUCT5.value="";
		document.form.PRODUCT6.value="";
		document.form.PRODUCT7.value="";
		document.form.PRODUCT8.value="";
		document.form.PRODUCT9.value="";
		document.form.PRODUCT10.value="";
		 document.form1.ISPOSDISCOUNT.checked = false;
		 document.form1.ISNEWARRIVAL.checked = false;
		 document.form1.ISTOPSELLING.checked = false;
		//document.form.ISBASICUOM.checked = false;
		document.form.ISCHILDCAL.checked = true;
	}
	function onCheck()
	{
	 var ITEM   = document.form.ITEM.value;
	 var DESC = document.form.DESC.value;
	 var uom = document.form.UOM.value;
	 var aRTIST = document.form.ARTIST.value;
	if(ITEM == "" || ITEM == null)
	{
		alert("Please Enter Product Id");
		document.form.ITEM.focus(); 
		return false;
	}
	else if(DESC=="" || DESC == null)
	{
		alert("Please Enter Description");
	}
	else if(aRTIST=="" || uom == aRTIST)
	//else if(document.form.ARTIST.selectedIndex==0)
	{
		alert("Please Enter Product Type");
	}
	else if(uom=="" || uom == null)
	//else if(document.form.UOM.selectedIndex==0)
	{
		alert("Please Enter UOM");
	}
	
		
	else
	{
		return true;
	}

	
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
			     
		    }  else{ (document.getElementById("ISPOSDISCOUNT").checked == false)
		    	 document.getElementById("ISPOSDISCOUNT").value = "0";
		    }
	} 
	function istopsellingproduct(){

		  if (document.getElementById("ISTOPSELLING").checked == true) {
			  document.getElementById("ISTOPSELLING").value = "1";
			     
		    }  else{ (document.getElementById("ISTOPSELLING").checked == false)
		    	 document.getElementById("ISTOPSELLING").value = "0";
		    }
	} 
	function isnewarrival(){

		  if (document.getElementById("ISNEWARRIVAL").checked == true) {
			  document.getElementById("ISNEWARRIVAL").value = "1";
			     
		    }  else{ (document.getElementById("ISNEWARRIVAL").checked == false)
		    	 document.getElementById("ISNEWARRIVAL").value = "0";
		    }
	} 
	
	function UOMpopUpwin(UOMTYPE){
		  var uom = document.form.UOM.value;	
			
			 popUpWin('../jsp/list/uomlist_save.jsp?UOM='+uom+'&UOMTYPE='+UOMTYPE);
		     
			
		}
	
	
	function onAdd(){
	 var ITEM   = document.form.ITEM.value;
	 var DESC = document.form.DESC.value;
	 var uom = document.form.UOM.value;
	 var purchaseuom = document.form.PURCHASEUOM.value;
	 var salesuom = document.form.SALESUOM.value;
	 var rentaluom = document.form.RENTALUOM.value;
	 var serviceuom = document.form.SERVICEUOM.value;
	 var inventoryuom = document.form.INVENTORYUOM.value;
	  var netweight   = document.form.NETWEIGHT.value;
	 var grossweight = document.form.GROSSWEIGHT.value;
	 var hscode   = document.form.HSCODE.value;
	 var coo = document.form.COO.value; 
	 var price = parseFloat(document.getElementById('PRICE').value);
	 var mprice = parseFloat(document.getElementById('MINSELLINGPRICE').value);
	 var discount=parseFloat(document.getElementById('DISCOUNT').value);
	 var productgst=parseFloat(document.getElementById('PRODGST').value);
	 var PRODGST=parseFloat(document.getElementById('PRODGST').value);
	 var hh = DESC.charCodeAt(0);
	 var minqty = parseFloat(document.form.STKQTY.value);
	 var maxqty = parseFloat(document.form.MAXSTKQTY.value);
	 var rentalprice = parseFloat(document.getElementById('RENTALPRICE').value);
	 var serviceprice = parseFloat(document.getElementById('SERVICEPRICE').value);
	 var declength =	parseInt(document.getElementById("numberOfDecimal").value);
	 var vendname = document.form.vendname.value;
	 var ISSUPPLIERMANDATORY = document.form.ISSUPPLIERMANDATORY.value;
	 
		
	   var iscompro   = document.form.ISCOMPRO.value;
	 //  alert("iscompro "+iscompro);
	   var CPPI   = document.form.CPPI.value;
	 //  alert("CPPI "+CPPI);
	   var INCPRICE   = document.form.INCPRICE.value;
	 //  alert("INCPRICE "+INCPRICE);
	   var INCPRICEUNIT   = document.form.INCPRICEUNIT.value;
	 //  alert("INCPRICEUNIT "+INCPRICEUNIT);
	  /// return false;
	 
	 var ValidNumber   = document.form.ValidNumber.value;
	   //if(ValidNumber != "") {alert("You have reached the limit of "+ValidNumber+" products you can create"); return false; }

	   if(ValidNumber != "") {alert("You can no longer add products as the limit of "+ValidNumber+" has been reached. Please contact Alphabit Administrator."); return false; }
	   
	  	   
	 
	 if(ITEM == "" || ITEM == null)
	{
		alert("Please Enter Product Id");
		document.form.ITEM.focus(); return false;
	}
	else if(DESC == "" || DESC == null)
	 {
		alert("Please Enter Description");
		document.form.DESC.focus(); return false;
	 }
	else if(uom=="" || uom == null)
	{
		alert("Please Enter Base UOM");
		document.form.UOM.focus();
		return false;
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
/* 	else if(rentaluom=="" || rentaluom == null)
	{
		alert("Please key in RENTALUOM");
		return false;
	} */
	/* else if(serviceuom=="" || serviceuom == null)
	{
		alert("Please key in SERVICEUOM");
		return false;
	} */
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
	
	/*else if(document.form.PRICE.value < document.form.MINSELLINGPRICE.value)*/
	
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
/* 	if (checkitem(ITEM)) {		 
			form.ITEM.focus();
			return false;
		} */
		
	
	else if(document.form.RENTALPRICE.value == ""){
	    alert("Please Enter Rental Price");
	    document.form.RENTALPRICE.focus();
	    return false;
	  }
	
	else if(!IsNumeric(document.form.RENTALPRICE.value))
	{
		alert("Please Enter Valid Rental Price");
		form.RENTALPRICE.focus();
		return false;
	}
	 
	/* else if(document.form.SERVICEPRICE.value == ""){
	    alert("Please enter Service Price");
	    document.form.SERVICEPRICE.focus();
	    return false;
	  }
	
	else if(!IsNumeric(document.form.SERVICEPRICE.value))
	{
		alert("Please Enter Valid Service Price");
		form.SERVICEPRICE.focus();
		return false;
	} */
		
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
		} else if (!validDecimal(document.getElementById("COST").value)) {
			alert("Not more than "+declength+" digits are allowed after decimal value in Cost");
			document.form.COST.focus();
			return false;
		} else if (!validDecimal(document.getElementById("MINSELLINGPRICE").value)) {
			alert("Not more than "+declength+" digits are allowed after decimal value in Minimum Selling Price");
			form.MINSELLINGPRICE.focus();
			return false;
		} else if (!validToThreeDecimal(document.getElementById("PRODGST").value)) {
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
						// alert(c);

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

			// to validate supplier discount sales 
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
							alert("Supplier Discount Purchase Order not more than Cost");
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
						// alert(c);

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
			
			// to validate supplier discount sales  end  
			var priceamt = document.getElementById('PRICE').value;
			var mpriceamt = document.getElementById('MINSELLINGPRICE').value;
			var costamt = document.getElementById('COST').value;
			if (priceamt.indexOf('.') == -1)
				priceamt += ".";
			var decNum = priceamt.substring(priceamt.indexOf('.') + 1,
					priceamt.length);

			document.form.DYNAMIC_CUSTOMERDISCOUNT_SIZE.value = document
					.getElementById("customerDiscount").rows.length;
			document.form.DYNAMIC_SUPPLIERDISCOUNT_SIZE.value = document
					.getElementById("supplierDiscount").rows.length;
			document.form.DYNAMIC_ITEMSUPPLIER_SIZE.value = document
					.getElementById("multiitemsup").rows.length;
			
			document.form.action = "/track/ItemMstServlet?ACTION=ADD";
			document.form.submit();
		}

	}
	function onUpdate() {
		var ITEM = document.form.ITEM.value;
		if (ITEM == "" || ITEM == null) {
			alert("Please Choose Item Code");
			document.form.ITEM.focus();
			return false;
		}

		document.form.action = "item_view.jsp?action=UPDATE";
		document.form.submit();
	}
	function onDelete() {
		var ITEM = document.form.ITEM.value;
		if (ITEM == "" || ITEM == null) {
			alert("Please Choose Item Code");
			document.form.ITEM.focus();
			return false;
		}

		document.form.action = "item_view.jsp?action=DELETE";
		document.form.submit();
	}
	function onView() {
		var ITEM = document.form.ITEM.value;
		if (ITEM == "" || ITEM == null) {
			alert("Please Choose Item Code");
			document.form.ITEM.focus();
			return false;
		}

		document.form.action = "item_view.jsp?action=VIEW";
		document.form.submit();
	}
	function onViewMapItem() {
		var ITEM = document.form.ITEM.value;
		if (ITEM == "" || ITEM == null) {
			alert("Please Choose Item Code");
			document.form.ITEM.focus();
			return false;
		}
		document.form.action = "item_view.jsp?action=VIEWMAPITEM";
		document.form.submit();
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
	}
	function validToThreeDecimal(str) {
		if (str.indexOf('.') == -1)
			str += ".";
		var decNum = str.substring(str.indexOf('.') + 1, str.length);
		if (decNum.length > 3) {
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
	String sViewMapItemEnb = "enabled";
	TblControlDAO _TblControlDAO =new TblControlDAO();
	String sItem      = "",sItemDesc  = "", nonstk="",desc="", sItemUom   = "", sReOrdQty  = "",prdclassid="" ,DEPT_DISPLAY_ID="",prd_dept_id="",prdBrand = "",vendno="",vendname="";
	String action     = "",sRemark1   = "" , sRemark2  = "", sRemark3   = "",prd_cls_id="",uom="";
	String Description1="",Description2="",Description3="",Description4="",Description5="",Description6="",Description7="",Description8="",Description9="",Description10="",DESCRIPTION_SIZE="";
	String Product1="",Product2="",Product3="",Product4="",Product5="",Product6="",Product7="",Product8="",Product9="",Product10="",PRD_SIZE="";
	String sItemCondition = "",sArtist="", prdclsid ="",prddepid="",minsprice="",cost="",discount="",sTitle="",sMedium="",price="",sRemark="",sUOM="",stkqty="",maxstkqty="";
	String DYNAMIC_ALTERNATE_SIZE = "",ISPARENTCHILD="",nonstkid="",nonstkflag="",nonstocktype="",loc="",CUSTOMER_TYPE_ID="", DYNAMIC_CUSTOMERDISCOUNT_SIZE="",OBDISCOUNT="",
			SUPPLIER="",DYNAMIC_SUPPLIERDISCOUNT_SIZE="",IBDISCOUNT="",PRODGST="",NETWEIGHT="",GROSSWEIGHT="",ISNEWARRIVAL="",ISTOPSELLING="",LOC_ID="",LOCID="",
			HSCODE="",COO="",VINNO="",MODEL="",RENTALPRICE="",SERVICEPRICE="",PURCHASEUOM="",SALESUOM="",RENTALUOM="",SERVICEUOM="",INVENTORYUOM="",ISPOSDISCOUNT="",ISBASICUOM="",ISCHILDCAL="",DIMENSION="";
	String DYNAMIC_ITEMSUPPLIER_SIZE="";
	StrUtils strUtils = new StrUtils();
	ItemUtil itemUtil = new ItemUtil();
	DateUtils dateutils = new DateUtils();
	PrdTypeUtil prdutil = new PrdTypeUtil();
	PrdBrandUtil prdbrandutil = new PrdBrandUtil();
	PrdClassUtil prdcls = new PrdClassUtil();
	PrdDeptUtil prddep = new PrdDeptUtil();
	PlantMstDAO plantMstDAO = new PlantMstDAO();
	ItemMstDAO itemMstDAO= new ItemMstDAO();
	CustomerBeanDAO custdao = new CustomerBeanDAO();
	res =  strUtils.fString(request.getParameter("result"));
	action = strUtils.fString(request.getParameter("action"));
	String region = StrUtils.fString((String) session.getAttribute("REGION"));
	itemUtil.setmLogger(mLogger);
	prdutil.setmLogger(mLogger);
	prdcls.setmLogger(mLogger);
	String plant=(String)session.getAttribute("PLANT");
	String transports = StrUtils.fString(request.getParameter("TRANSPORTID"));
	String taxbylabel = ub.getTaxByLable(plant);
	String taxbylabelordermanagement = ub.getTaxByLableOrderManagement(plant);
	String COMP_INDUSTRY = plantMstDAO.getCOMP_INDUSTRY(plant);//Check Company Industry
	String username=(String)session.getAttribute("LOGIN_USER");
	//Validate no.of Product -- Azees 15.11.2020
	String NOOFINVENTORY=strUtils.fString((String) session.getAttribute("NOOFINVENTORY"));
	String NOOFSUPPLIER=strUtils.fString((String) session.getAttribute("NOOFSUPPLIER"));
	String ValidNumber="",SupValidNumber="";
	int novalid =itemMstDAO.Itemcount(plant);
	if(!NOOFINVENTORY.equalsIgnoreCase("Unlimited"))
	{
		int convl = Integer.valueOf(NOOFINVENTORY);
		if(novalid>=convl)
		{
			ValidNumber=NOOFINVENTORY;
		}
	}
	int nosupvalid =custdao.Vendorcount(plant);
	if(!NOOFSUPPLIER.equalsIgnoreCase("Unlimited"))
	{
		int convl = Integer.valueOf(NOOFSUPPLIER);
		if(nosupvalid>=convl)
		{
			SupValidNumber=NOOFSUPPLIER;
		}
	}
	String numberOfDecimal = plantMstDAO.getNumberOfDecimal(plant);
	sItem     = StrUtils.fString(request.getParameter("ITEM"));
	if(sItem.length() <= 0) sItem     = StrUtils.fString(request.getParameter("ITEM1"));
	sItemDesc      = StrUtils.fString(request.getParameter("DESC"));
	sItemDesc      = StrUtils.replaceCharacters2Recv(sItemDesc);
	prd_cls_id= StrUtils.fString(request.getParameter("PRD_CLS_ID")); 
	vendno=StrUtils.fString(request.getParameter("vendno"));
	vendname=StrUtils.fString(request.getParameter("vendname"));
	LOC_ID  = strUtils.fString(request.getParameter("LOC_ID"));
	prd_dept_id= StrUtils.fString(request.getParameter("PRD_DEPT_ID")); 
// 	DEPT_DISPLAY_ID= StrUtils.fString(request.getParameter("DEPT_DISPLAY_ID")); 
	sArtist = StrUtils.fString(request.getParameter("ARTIST"));
	// Start code added by Deen for product brand on 11/9/12
	prdBrand = StrUtils.fString(request.getParameter("PRD_BRAND"));
	// End code added by Deen for product brand on 11/9/12
	sTitle = StrUtils.fString(request.getParameter("TITLE"));
	//sMedium = StrUtils.fString(request.getParameter("MANUFACT"));
	sRemark      = StrUtils.fString(request.getParameter("REMARKS"));
	sItemCondition = StrUtils.fString(request.getParameter("ITEM_CONDITION"));
	sUOM = StrUtils.fString(request.getParameter("UOM"));
	stkqty = StrUtils.fString(request.getParameter("STKQTY"));
	price  =  StrUtils.fString(request.getParameter("PRICE"));
	minsprice = StrUtils.fString(request.getParameter("MINSELLINGPRICE"));
	cost = StrUtils.fString(request.getParameter("COST"));
	discount = StrUtils.fString(request.getParameter("DISCOUNT"));
	PRODGST = StrUtils.fString(request.getParameter("PRODGST"));
	nonstkflag = StrUtils.fString(request.getParameter("NONSTOCKFLAG"));
	nonstkid = StrUtils.fString(request.getParameter("NONSTKTYPE"));
	DYNAMIC_ALTERNATE_SIZE = StrUtils.fString(request.getParameter("DYNAMIC_ALTERNATE_SIZE"));
	DYNAMIC_CUSTOMERDISCOUNT_SIZE = StrUtils.fString(request.getParameter("DYNAMIC_CUSTOMERDISCOUNT_SIZE"));
	DYNAMIC_SUPPLIERDISCOUNT_SIZE = StrUtils.fString(request.getParameter("DYNAMIC_SUPPLIERDISCOUNT_SIZE"));
	DYNAMIC_ITEMSUPPLIER_SIZE = StrUtils.fString(request.getParameter("DYNAMIC_ITEMSUPPLIER_SIZE"));
	PRD_SIZE = StrUtils.fString(request.getParameter("PRD_SIZE"));
	DESCRIPTION_SIZE = StrUtils.fString(request.getParameter("DESCRIPTION_SIZE"));
	loc = "";
	NETWEIGHT     = StrUtils.fString(request.getParameter("NETWEIGHT"));
	GROSSWEIGHT     = StrUtils.fString(request.getParameter("GROSSWEIGHT"));
	HSCODE     = StrUtils.fString(request.getParameter("HSCODE"));
	COO     = StrUtils.fString(request.getParameter("COO"));
	OBDISCOUNT = StrUtils.fString(request.getParameter("OBDISCOUNT"));
	IBDISCOUNT = StrUtils.fString(request.getParameter("IBDISCOUNT"));
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
	ISBASICUOM = (request.getParameter("ISBASICUOM") != null) ? "1": "0";
	ISCHILDCAL = (request.getParameter("ISCHILDCAL") != null) ? "1": "0";
	ISPOSDISCOUNT = (request.getParameter("ISPOSDISCOUNT") != null) ? "1": "0";
	ISNEWARRIVAL = (request.getParameter("ISNEWARRIVAL") != null) ? "1": "0";
	ISTOPSELLING = (request.getParameter("ISTOPSELLING") != null) ? "1": "0";
	String SUPPLIER_TYPE_ID = StrUtils.fString(request.getParameter("SUPPLIER_TYPE_IDS"));
	
	CustUtil custUtil = new CustUtil();
	List customertypelist=custUtil.getCustTypeList("",plant," AND ISACTIVE ='Y'");
	
	maxstkqty = StrUtils.fString(request.getParameter("MAXSTKQTY"));
	float pricef=0;float costf=0;float minspricef=0; float prodgstf=0; float Rentalpricef=0; float ServicePricef=0;
	
    if(price.length()<0&&price==null){
		price="0";
	}
	
if(minsprice.length()<0&&minsprice==null){
			minsprice="0";
		}
	
	
	if(cost.length()<0&&cost==null){
		cost="0";
	}
	
	if(PRODGST.length()<0&&PRODGST==null){
		PRODGST="0";
	}
	
	if(RENTALPRICE.length()<0&&RENTALPRICE==null){
		RENTALPRICE="0";
	}
	
	if(SERVICEPRICE.length()<0&&SERVICEPRICE==null){
		SERVICEPRICE="0";
	}
	
	  if(!Description1.isEmpty())
        	 DESCRIPTION_SIZE=DESCRIPTION_SIZE+1;
         if(!Description2.isEmpty())
        	 DESCRIPTION_SIZE=DESCRIPTION_SIZE+1;
         if(!Description3.isEmpty())
        	 DESCRIPTION_SIZE=DESCRIPTION_SIZE+1;
         if(!Description4.isEmpty())
        	 DESCRIPTION_SIZE=DESCRIPTION_SIZE+1;
         if(!Description5.isEmpty())
        	 DESCRIPTION_SIZE=DESCRIPTION_SIZE+1;
         if(!Description6.isEmpty())
        	 DESCRIPTION_SIZE=DESCRIPTION_SIZE+1;
         if(!Description7.isEmpty())
        	 DESCRIPTION_SIZE=DESCRIPTION_SIZE+1;
         if(!Description8.isEmpty())
        	 DESCRIPTION_SIZE=DESCRIPTION_SIZE+1;
         if(!Description9.isEmpty())
        	 DESCRIPTION_SIZE=DESCRIPTION_SIZE+1;
         if(!Description10.isEmpty())
        	 DESCRIPTION_SIZE=DESCRIPTION_SIZE+1;
         
	  if(!Product1.isEmpty())
        	 PRD_SIZE=PRD_SIZE+1;
         if(!Product2.isEmpty())
        	 PRD_SIZE=PRD_SIZE+1;
         if(!Product3.isEmpty())
        	 PRD_SIZE=PRD_SIZE+1;
         if(!Product4.isEmpty())
        	 PRD_SIZE=PRD_SIZE+1;
         if(!Product5.isEmpty())
        	 PRD_SIZE=PRD_SIZE+1;
         if(!Product6.isEmpty())
        	 PRD_SIZE=PRD_SIZE+1;
         if(!Product7.isEmpty())
        	 PRD_SIZE=PRD_SIZE+1;
         if(!Product8.isEmpty())
        	 PRD_SIZE=PRD_SIZE+1;
         if(!Product9.isEmpty())
        	 PRD_SIZE=PRD_SIZE+1;
         if(!Product10.isEmpty())
        	 PRD_SIZE=PRD_SIZE+1;
	
	
	if(((String)session.getAttribute("PRDCLSID"))!=null)
	{
		prdclsid = (String)session.getAttribute("PRDCLSID");
		
	}
	sAddEnb    = "enabled";
	sItemEnb   = "enabled";
	List prdlist=prdutil.getPrdTypeList("",plant," AND ISACTIVE ='Y'");
	List prdBrandList=prdbrandutil.getPrdBrandList("",plant," AND ISACTIVE ='Y'");
	List prdclslst = prdcls.getPrdTypeList("",plant," AND ISACTIVE ='Y'");
	List prddeplst = prddep.getPrdTypeList("",plant," AND ISACTIVE ='Y'");//Resvi
        List uomlist = itemUtil.getUomList(plant," AND ISACTIVE ='Y' ");
     List nonstklst =   itemUtil.getNonStockList(plant,""); 
	UserTransaction ut = null;
	String companyregnumber=StrUtils.fString(request.getParameter("companyregnumber"));
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
/* 	if(action.equalsIgnoreCase("NEW")){
		
	    sItemDesc  = "";
	    sArtist   = "";
	    prd_cls_id="";
	    prdBrand = "";
	    sTitle  = "";
	    //sMedium="";
	    sRemark="";
	    sItemCondition=""; 
	    stkqty=""; 
	    price="0.00";
	    cost="0.00";
	    maxstkqty="";
	    minsprice="";
	    discount="";
	    sUOM="";
	    NETWEIGHT="0.00";
	    GROSSWEIGHT="0.00";
	    HSCODE="";
	    COO="";
	    VINNO="";
	    MODEL="";
		RENTALPRICE="0.00";
	    SERVICEPRICE="0.00";
	    PURCHASEUOM="";
	    SALESUOM="";
	    RENTALUOM="";
	    SERVICEUOM="";
	    INVENTORYUOM="";
	    ISBASICUOM="";
		
	    sAddEnb    = "enabled";
	    sItemEnb   = "enabled";
	    String minseq="", sBatchSeq="",sZero="";
	   
	    boolean insertFlag=false;
	 // 	TblControlDAO _TblControlDAO =new TblControlDAO();
	  	_TblControlDAO.setmLogger(mLogger);
	  	prdclassid = request.getParameter("PRD_CLS_ID");
	    Hashtable  ht=new Hashtable();
	    String query=" isnull(NXTSEQ,'') as NXTSEQ";
	    ht.put(IDBConstants.PLANT,plant);
	    ht.put(IDBConstants.TBL_FUNCTION,"PRODUCT");
	    try{
	       boolean exitFlag=false; boolean resultflag=false;
	       exitFlag=_TblControlDAO.isExisit(ht,"",plant);
	   
	     //--if exitflag is false than we insert batch number on first time based on plant,currentmonth
	      if (exitFlag==false)
	      {         
	            Map htInsert=null;
	            Hashtable htTblCntInsert  = new Hashtable();
	            htTblCntInsert.put(IDBConstants.PLANT,plant);
	            htTblCntInsert.put(IDBConstants.TBL_FUNCTION,"PRODUCT");
	            htTblCntInsert.put(IDBConstants.TBL_PREFIX1,"P");
	            htTblCntInsert.put(IDBConstants.TBL_MINSEQ,"0000");
	            htTblCntInsert.put(IDBConstants.TBL_MAXSEQ,"9999");
	            htTblCntInsert.put(IDBConstants.TBL_NEXT_SEQ, (String)IDBConstants.TBL_FIRST_NEX_SEQ);
	            htTblCntInsert.put(IDBConstants.CREATED_BY, username);
	            htTblCntInsert.put(IDBConstants.CREATED_AT, (String)new DateUtils().getDateTime());
	            insertFlag= _TblControlDAO.insertTblControl(htTblCntInsert,plant);
	            sItem="P"+"0001";
	      }
	      else
	      {
	           //--if exitflag is not false than we updated nxtseq batch number based on plant,currentmonth
	           Map m= _TblControlDAO.selectRow(query, ht,"");
	           sBatchSeq=(String)m.get("NXTSEQ");
	           int inxtSeq=Integer.parseInt(((String)sBatchSeq.trim().toString()))+1;
	           String updatedSeq=Integer.toString(inxtSeq);
	            if(updatedSeq.length()==1)
	           {
	             sZero="000";
	           }
	           else if(updatedSeq.length()==2)
	           {
	             sZero="00";
	           }
	           else if(updatedSeq.length()==3)
	           {
	             sZero="0";
	           }
	            Map htUpdate = null;
	           Hashtable htTblCntUpdate = new Hashtable();
	           htTblCntUpdate.put(IDBConstants.PLANT,plant);
	           htTblCntUpdate.put(IDBConstants.TBL_FUNCTION,"PRODUCT");
	           htTblCntUpdate.put(IDBConstants.TBL_PREFIX1,"P");
	           StringBuffer updateQyery=new StringBuffer("set ");
	           updateQyery.append(IDBConstants.TBL_NEXT_SEQ +" = '"+ (String)updatedSeq.toString()+ "'");
	        //   boolean updateFlag=_TblControlDAO.update(updateQyery.toString(),htTblCntUpdate,"",plant);
	           sItem="P"+sZero+updatedSeq;
	        }
	      } catch(Exception e)
	         {
	    	  mLogger.exception(true,
						"ERROR IN JSP PAGE - item_view.jsp ", e);
	         }
	           
	
	} */
       if(action.equalsIgnoreCase("ADD")){
		try{
			
			ut = com.track.gates.DbBean.getUserTranaction();
			ut.begin();
		    if(!(itemUtil.isExistsItemMst(sItem,plant))) // if the item exists already
		    {
                        String specialcharsnotAllowed = StrUtils.isValidAlphaNumeric(sItem);
                        if(sItem.equals("")){
                            throw new Exception("");
                    }
                        if(specialcharsnotAllowed.length()>0){
                                throw new Exception("Product ID  value : '" + sItem + "' has special characters "+specialcharsnotAllowed+" that are  not allowed ");
                        }
                        
                        if(loc.length()>0){
                        boolean isExistLoc = false;
    					LocUtil locUtil = new LocUtil();
    					isExistLoc = locUtil.isValidLocInLocmst(plant, loc);
    					if(!isExistLoc){
    						throw new Exception("Location: " + loc + " is not valid location");
    						}
                        }
		          sItemDesc=strUtils.InsertQuotes(sItemDesc);
		          Hashtable ht = new Hashtable();
		          ht.put(IConstants.PLANT,plant);
		          ht.put(IConstants.ITEM,sItem);
		          ht.put(IConstants.ITEM_DESC,sItemDesc);
		          ht.put(IConstants.ITEMMST_REMARK1,strUtils.InsertQuotes(sRemark));
		          ht.put(IConstants.ITEMMST_ITEM_TYPE,sArtist);//itemtype
		       // Start code added by Deen for product brand on 11/9/12
		          ht.put(IConstants.PRDBRANDID ,prdBrand);
		       // End code added by Deen for product brand on 11/9/12
		          ht.put("STKUOM",sUOM);
		          ht.put(IConstants.ITEMMST_REMARK4,strUtils.InsertQuotes(sTitle));//remark4
		          //ht.put(IConstants.ITEMMST_REMARK2,strUtils.InsertQuotes(sMedium)); // remark2
		          ht.put(IConstants.ITEMMST_REMARK3,strUtils.InsertQuotes(sItemCondition));//remark3
		          ht.put(IConstants.PRDCLSID,prd_cls_id);//prd_cls_id
		          ht.put(IConstants.PRDDEPTID,prd_dept_id);//prddept
		          ht.put("LOC_ID",LOC_ID);
// 		          ht.put(IConstants.DEPTDISPLAY,DEPT_DISPLAY_ID);//DEPT_DISPLAY_ID
		          ht.put(IConstants.PRICE,price);
		          ht.put(IConstants.ISACTIVE,"Y");
		          ht.put(IConstants.COST,cost);
		          ht.put(IConstants.MIN_S_PRICE,minsprice); 
		          ht.put(IConstants.DISCOUNT, discount);
		          ht.put(IConstants.PRODGST, PRODGST);
		          ht.put(IConstants.NONSTKFLAG, nonstkflag);
		          ht.put(IConstants.NONSTKTYPEID, nonstkid);
		          ht.put(IConstants.DISCOUNT, discount);
		          ht.put("ITEM_LOC", loc);
		          ht.put("NETWEIGHT",NETWEIGHT);
			      ht.put("GROSSWEIGHT",GROSSWEIGHT);
			      ht.put("DIMENSION",DIMENSION);
			      ht.put("HSCODE",HSCODE);
			      ht.put("COO",COO);
		          // Start code added by deen for createddate,createdby on 15/july/2013
		          ht.put("CRBY",username);
		          ht.put("CRAT",dateutils.getDateTime());
		          ht.put("VINNO",VINNO);
			      ht.put("MODEL",MODEL);
				  ht.put("RENTALPRICE",RENTALPRICE);
			      ht.put("SERVICEPRICE",SERVICEPRICE);
			      ht.put("PURCHASEUOM",PURCHASEUOM);
			      ht.put("SALESUOM",SALESUOM);
			      ht.put("RENTALUOM",RENTALUOM);
			      ht.put("SERVICEUOM",SERVICEUOM);
			      ht.put("INVENTORYUOM",INVENTORYUOM);
			      ht.put("ISBASICUOM",ISBASICUOM);
			      ht.put("ISPOSDISCOUNT",ISPOSDISCOUNT);
			      ht.put("ISNEWARRIVAL",ISNEWARRIVAL);
			      ht.put("ISTOPSELLING",ISTOPSELLING);
			      ht.put(IDBConstants.VENDOR_CODE,vendno);
		         
		          ht.put("USERFLD1", "N");
		          if(stkqty=="")
		          stkqty ="0";
		          ht.put(IDBConstants.STKQTY, stkqty);//stkqty
		          if(maxstkqty=="")
			          maxstkqty ="0";
		          ht.put(IDBConstants.MAXSTKQTY, maxstkqty);
		          String remark = sRemark+" "+sItemCondition+" "+sTitle;
		          MovHisDAO mdao = new MovHisDAO(plant);
		          mdao.setmLogger(mLogger);
		          Hashtable htm = new Hashtable();
		          htm.put("PLANT",plant);
		          htm.put("DIRTYPE",TransactionConstants.ADD_ITEM);
		          htm.put("RECID","");
		          htm.put("ITEM",sItem);
		          htm.put("LOC",loc);
		          htm.put(IDBConstants.REMARKS,strUtils.InsertQuotes(remark));  
		          htm.put("CRBY",username);
		          htm.put("CRAT",dateutils.getDateTime());
		          htm.put(IDBConstants.TRAN_DATE,dateutils.getDateinyyyy_mm_dd(dateutils.getDate()));  
		          boolean  inserted=false; 
		          boolean  insertAlternateItem = false; 
		          boolean itemInserted = itemUtil.insertItem(ht);
		          { 
		        	  if(itemInserted) {
			        	 String alternateItemName = sItem; 
			           	 List<String> alternateItemNameLists = new ArrayList<String>();
			        	 alternateItemNameLists.add(alternateItemName);
			        	 insertAlternateItem = itemUtil.insertAlternateItemLists(plant, sItem, alternateItemNameLists);
			          }
			          if(itemInserted && insertAlternateItem) {
			        	  boolean OBCustomerDiscount=false;  
			           //insert customer discount outbound
			        	  int DYNAMIC_CUSTOMERDISCOUNT_SIZE_INT = (new Integer(DYNAMIC_CUSTOMERDISCOUNT_SIZE)).intValue();
			        	  for(int nameCount = 0; nameCount<=DYNAMIC_CUSTOMERDISCOUNT_SIZE_INT;nameCount++){
				        		 System.out.println("nameCount"+nameCount);
				        		 System.out.println("nameCount"+strUtils.fString(request.getParameter("CUSTOMER_TYPE_ID_"+nameCount)));
				        		if(StrUtils.fString(request.getParameter("DYNAMIC_CUSTOMER_DISCOUNT_"+nameCount))==""){
				        			break;
				        		}else{
				        			Hashtable HM = new Hashtable();
				        		   	HM.put(IConstants.PLANT, plant);
				    				HM.put(IConstants.ITEM, sItem);
				    				HM.put(IConstants.CUSTOMERTYPEID, strUtils.fString(request.getParameter("CUSTOMER_TYPE_ID_"+nameCount)));
				    				if(OBDISCOUNT.equalsIgnoreCase("BYPERCENTAGE")){
				    					HM.put("OBDISCOUNT", request.getParameter("DYNAMIC_CUSTOMER_DISCOUNT_"+nameCount)+"%");
				    				}
				    				else{
				    					HM.put("OBDISCOUNT", request.getParameter("DYNAMIC_CUSTOMER_DISCOUNT_"+nameCount));
				    				}
				    				HM.put("CRAT",dateutils.getDateTime());
				    			    HM.put("CRBY",username);
				    			    OBCustomerDiscount = itemUtil.insertOBCustomerDiscount(HM);
				    								        		
				        		}
				        	 }
 			           //end insert customer discount sales
 			           
			        	     //insert supplier discount Purchase
			        	  boolean IBSupplierDiscount=false;
			        	  int DYNAMIC_SUPPLIERDISCOUNT_SIZE_INT = (new Integer(DYNAMIC_SUPPLIERDISCOUNT_SIZE)).intValue();
			        	  for(int nameCount = 0; nameCount<=DYNAMIC_SUPPLIERDISCOUNT_SIZE_INT;nameCount++){
				        		 System.out.println("nameCount"+nameCount);
				        		 System.out.println("nameCount"+strUtils.fString(request.getParameter("SUPPLIER_"+nameCount)));
				        		if(StrUtils.fString(request.getParameter("DYNAMIC_SUPPLIER_DISCOUNT_"+nameCount))==""){
				        			break;
				        		}else{
				        			Hashtable HM = new Hashtable();
				        		   	HM.put(IConstants.PLANT, plant);
				    				HM.put(IConstants.ITEM, sItem);
				    				HM.put(IConstants.VENDNO, strUtils.fString(request.getParameter("SUPPLIER_"+nameCount)));
				    				System.out.println("IBDISCOUNT"+IBDISCOUNT);
				    				if(IBDISCOUNT.equalsIgnoreCase("BYPERCENTAGE")){
				    					HM.put("IBDISCOUNT", request.getParameter("DYNAMIC_SUPPLIER_DISCOUNT_"+nameCount)+"%");
				    									    				}
				    				else{
				    					HM.put("IBDISCOUNT", request.getParameter("DYNAMIC_SUPPLIER_DISCOUNT_"+nameCount));
				    									    				}
				    				HM.put("CRAT",dateutils.getDateTime());
				    			    HM.put("CRBY",username);
				    			    IBSupplierDiscount = itemUtil.insertIBSupplierDiscount(HM);
				    								        		
				        		}
				        	 }
			        		
			        	  
			        	  
			    		
 			           //end insert supplier discount Purchase
			            //if(OBCustomerDiscount){
			          	 inserted = mdao.insertIntoMovHis(htm);
			          // }
			          }
			          
			          htm.put(IDBConstants.CREATED_AT,dateutils.getDateTime());
		              htm.put(IDBConstants.TRAN_DATE,dateutils.getDateinyyyy_mm_dd(dateutils.getDate()));
		              
		    		  boolean updateFlag;
		    		if(sItem!="P0001")
		      		  {	
		    			boolean exitFlag = false;
		    			Hashtable htv = new Hashtable();				
		    			htv.put(IDBConstants.PLANT, plant);
		    			htv.put(IDBConstants.TBL_FUNCTION, "PRODUCT");
		    			exitFlag = _TblControlDAO.isExisit(htv, "", plant);
		    			if (exitFlag) 
		      		    updateFlag=_TblControlDAO.updateSeqNo("PRODUCT",plant);
		    			else
		    			{
		    				boolean insertFlag = false;
		    				Map htInsert=null;
		                	Hashtable htTblCntInsert  = new Hashtable();           
		                	htTblCntInsert.put(IDBConstants.PLANT,plant);          
		                	htTblCntInsert.put(IDBConstants.TBL_FUNCTION,"PRODUCT");
		                	htTblCntInsert.put(IDBConstants.TBL_PREFIX1,"P");
		                 	htTblCntInsert.put("MINSEQ","0000");
		                 	htTblCntInsert.put("MAXSEQ","9999");
		                	htTblCntInsert.put(IDBConstants.TBL_NEXT_SEQ, (String)IDBConstants.TBL_FIRST_NEX_SEQ);
		                	htTblCntInsert.put(IDBConstants.CREATED_BY, username);
		                	htTblCntInsert.put(IDBConstants.CREATED_AT, (String)new DateUtils().getDateTime());
		                	insertFlag= _TblControlDAO.insertTblControl(htTblCntInsert,plant);
		    			}
		    		}
			          
		          	sItemDesc=strUtils.RemoveDoubleQuotesToSingle(sItemDesc);
		          }
		          if(itemInserted&&inserted) {
		              res = "<font class = "+IConstants.SUCCESS_COLOR+">Product Added Successfully</font>";
                              DbBean.CommitTran(ut);
		             
						
		          } else {
                              DbBean.RollbackTran(ut);
		              res = "<font class = "+IConstants.FAILED_COLOR+">Failed to add New Product</font>";
		
		          }
		   
		    }else{
                            DbBean.RollbackTran(ut);
                            res = "<font class = "+IConstants.FAILED_COLOR+">Product Exists already. Try again</font>";
		         
		    }}
             catch(Exception e)
                {
                     DbBean.RollbackTran(ut);
                          res = "<font class = "+IConstants.FAILED_COLOR+">"+e.getMessage()+"</font>";
                }
		} 
	else if(action.equalsIgnoreCase("PRINT")){
	    	com.track.dao.LblDet lblDet=new com.track.dao.LblDet();
	    	lblDet.setmLogger(mLogger);
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
	         }else{
	           Hashtable htUpdate = new Hashtable();
	           htUpdate.put("LBSTATUS","N");
	           lblDet.updateLblDet(htUpdate,htCond);
	        
	         }
	
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
                <li><label>Create Product Details</label></li>                                   
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
  <CENTER><strong><font style="font-size:18px;"><%=res%></font></strong></CENTER>
 <input type="number" id="numberOfDecimal" style="display:none;" value=<%=numberOfDecimal%>> 
<form class="form-horizontal" name="form" method="post"onSubmit="return onCheck();" enctype="multipart/form-data" >

		<div class="form-group">      	
      	<label class="control-label col-form-label col-sm-2 required" for="Product ID">Product Id</label>
      	<div class="col-sm-4">
      	<div class="input-group">
    	<INPUT class="form-control" name="ITEM" id="ITEM" type="TEXT" value="<%=sItem%>" onchange="checkitem(this.value)" size="20" MAXLENGTH=50 <%=sItemEnb%> onkeypress="return blockSpecialChar(event)"> 
   		<span class="input-group-addon"  onClick="onNew();" sNewEnb >
   		<a href="#" data-toggle="tooltip" data-placement="top" title="Auto-Generate">
   		<i class="glyphicon glyphicon-edit" style="font-size: 20px;"></i></a></span>
  		</div>
  		</div>
  		<div class="form-inline">
 		<div class="col-sm-2">
  		<label class="radio-inline">
      	<input type="radio" name="NONSTOCKFLAG" type = "radio"  value="N"  id="NotNonStock" checked="checked" onclick="DisplayNonStkType();">Stock
    	</label>
    	<label class="radio-inline">
      	<input type="radio" name="NONSTOCKFLAG" type = "radio" value="Y"  id = "NonStock" onclick="DisplayNonStkType();">Non Stock
    	</label>
     	</div>
		</div>
        </div>
        <INPUT type="hidden" name="plant" value=<%=plant%>> 
        <input type="hidden" name="CURRENCYID" value="<%=DISPLAYID%>">
 		<INPUT type="hidden" name="ITEM1" value=<%=sItem%>>
 		<INPUT type="hidden" name="CURRENCY" value=<%=currency%>>
 		<INPUT type="hidden" name="ValidNumber" value="<%=ValidNumber%>">
 		<input type="hidden" name="PRD_CLS_ID" value="<%=prdclsid%>">
 		<input type="hidden" name="PRD_DEPT_ID" value="<%=prddepid%>">
 		<input type="hidden" name="LOC_DESC" value="<%=LOCID%>">
 		<input type="hidden" name="PRD_BRAND" value="<%=prdBrand%>">
 		<input type="hidden" name="ARTIST" value="<%=sArtist%>">
 		<input type = "hidden" name="uomModal">
 		<INPUT type="hidden" name="F1" id="F1" value="<%=Description1%>">
        <INPUT type="hidden" name="F2" id="F2" value="<%=Description2%>">
        <INPUT type="hidden" name="F3" id="F3" value="<%=Description3%>">
        <INPUT type="hidden" name="F4" id="F4" value="<%=Description4%>">
        <INPUT type="hidden" name="F5" id="F5" value="<%=Description5%>">
        <INPUT type="hidden" name="F6" id="F6" value="<%=Description6%>">
        <INPUT type="hidden" name="F7" id="F7" value="<%=Description7%>">
        <INPUT type="hidden" name="F8" id="F8" value="<%=Description8%>">
        <INPUT type="hidden" name="F9" id="F9" value="<%=Description9%>">
        <INPUT type="hidden" name="F10" id="F10" value="<%=Description10%>">
 		<INPUT type="hidden" name="P1" id="P1" value="<%=Product1%>">
        <INPUT type="hidden" name="P2" id="P2" value="<%=Product2%>">
        <INPUT type="hidden" name="P3" id="P3" value="<%=Product3%>">
        <INPUT type="hidden" name="P4" id="P4" value="<%=Product4%>">
        <INPUT type="hidden" name="P5" id="P5" value="<%=Product5%>">
        <INPUT type="hidden" name="P6" id="P6" value="<%=Product6%>">
        <INPUT type="hidden" name="P7" id="P7" value="<%=Product7%>">
        <INPUT type="hidden" name="P8" id="P8" value="<%=Product8%>">
        <INPUT type="hidden" name="P9" id="P9" value="<%=Product9%>">
        <INPUT type="hidden" name="P10" id="P10" value="<%=Product10%>">
        <INPUT type="hidden" name="ISSUPPLIERMANDATORY" id="ISSUPPLIERMANDATORY" value="<%=ISSUPPLIERMANDATORY%>">
 		
 	<div class="form-group">
      <label class="control-label col-form-label col-sm-2 required" for="Product Description">Description</label>
      <div class="col-sm-4">                
        <INPUT class="form-control" name="DESC" type="TEXT" value="<%=sItemDesc%>" size="20" MAXLENGTH=100>
      </div>
      <div class="form-inline" style="display: none;">>
	     <div class="col-sm-2">
	     <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name ="ISCHILDCAL" value="ISCHILDCAL" id="ISCHILDCAL" >
                     Is Pack Product</label>
         </div>
	     </div>      
    </div>
    
    <% if(!COMP_INDUSTRY.equals("Retail")) {%>
    <div class="form-group">
      <label class="control-label col-form-label col-sm-2" for="Detailed Description">Detailed Description</label>
      <div class="col-sm-4">                
        <INPUT class="form-control" name="REMARKS" type="TEXT" value="<%=sRemark%>"	size="20" MAXLENGTH=100>
      	</div>
      	<div id="divNonStk" style="display:none;" >
		<div class="form-inline">
      	<!-- <label class="control-label col-sm-3" for="Non Stock Type">Non Stock Type:</label> -->
      	<div class="col-sm-3" id="nonStktd">          
       	<SELECT class="form-control" NAME="NONSTKTYPE" size="1" maxlength="9">
		<OPTION selected value="0">Choose</OPTION>
		<%       for (int i =0; i<nonstklst.size(); i++){
                 Map map = (Map) nonstklst.get(i);
                 nonstk     = (String) map.get("NONSTOCKDESC");
                 nonstkid = (String) map.get("NONSTKCODE");
        %>
		<OPTION value="<%=nonstkid%>"><%=nonstk%>
		</OPTION>
		<%}%>
		</SELECT>
      	</div>
    	</div>
   		</div>
      </div>
      <%} %>
      
      <div class="form-group">
      <label class="control-label col-form-label col-sm-2 required" for="Basic UOM">Base UOM</label>
      <div class="col-sm-4">                
        <INPUT class="form-control" name="UOM" type="TEXT" value="<%=uom%>" size="20" MAXLENGTH=50 id="Basicuom" placeholder="Select a UOM">
        <span class="select-icon" style="right: 45px;" onclick="$(this).parent().find('input[name=\'UOM\']').focus()">
        <i class="glyphicon glyphicon-menu-down" id="uombtnpop"></i></span>
      </div>
      <div class="form-inline">
	     <div class="col-sm-2">
	     <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name ="ISBASICUOM" value="ISBASICUOM" id="ISBASICUOM" onclick="isbasicuom();">
                     Apply to all UOM</label>
         </div>
	     </div>
    </div>
    
    <div class="form-group">
      <label class="control-label col-form-label col-sm-2" for="Net Weight">Net Weight (KG)</label>
      <div class="col-sm-4">
			<INPUT class="form-control" name="NETWEIGHT" id="NETWEIGHT" type="TEXT" value="<%=NETWEIGHT%>" style="width:100%" MAXLENGTH=50 onkeypress="return isNumberKey(event,this,4)">				
      </div>
      <% if(COMP_INDUSTRY.equals("Retail")) {%>
		<div class="form-inline">
	     	<div class="col-sm-3">
	     		<label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name ="ISNEWARRIVAL" value="ISNEWARRIVAL" id="ISNEWARRIVAL" onclick="isnewarrival();"
	     		<%if(ISNEWARRIVAL.equals("1")) {%>checked <%}%>/> New Arrival</label>
         	</div>
	    </div>
	    <%}else{%>
		<div class="form-inline" style="display:none;">
	     	<div class="col-sm-3">
	     		<label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name ="ISNEWARRIVAL" value="ISNEWARRIVAL" id="ISNEWARRIVAL" onclick="isnewarrival();"
	     		<%if(ISNEWARRIVAL.equals("1")) {%>checked <%}%>/> New Arrival</label>
         	</div>
	    </div>
	    <%}%>
    </div>
    
    <div class="form-group">
      <label class="control-label col-form-label col-sm-2" for="Gross Weight">Gross Weight (KG)</label>
      <div class="col-sm-4">
			<INPUT class="form-control" name="GROSSWEIGHT" id="GROSSWEIGHT" type="TEXT" value="<%=GROSSWEIGHT%>" style="width:100%" MAXLENGTH=50 onkeypress="return isNumberKey(event,this,4)">				
      </div>
      <% if(COMP_INDUSTRY.equals("Retail")) {%>
		<div class="form-inline">
	     <div class="col-sm-3">
	     	<label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name ="ISTOPSELLING" value="ISTOPSELLING" id="ISTOPSELLING" onclick="istopsellingproduct();"
	     	<%if(ISTOPSELLING.equals("1")) {%>checked <%}%>/> Top Selling Product</label>
         </div>
	    </div>
	    <%}else{%>
	    <div class="form-inline" style="display:none;">
	     <div class="col-sm-3">
	     	<label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name ="ISTOPSELLING" value="ISTOPSELLING" id="ISTOPSELLING" onclick="istopsellingproduct();"
	     	<%if(ISTOPSELLING.equals("1")) {%>checked <%}%>/> Top Selling Product</label>
         </div>
	    </div>
	    <%}%>
    </div>
   <%  if(!COMP_INDUSTRY.equalsIgnoreCase("Centralised Kitchen")) { %>
<div class="form-group">
      <label class="control-label col-form-label col-sm-2" for="Location">Location</label>
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
      <label class="control-label col-form-label col-sm-2" for="Dimension">Dimension</label>
      <div class="col-sm-4">
			<INPUT class="form-control" name="DIMENSION" type="TEXT" value="<%=DIMENSION%>" style="width:100%" MAXLENGTH=50>				
      </div>
	<div class="form-inline">
	     <div class="col-sm-3">
	     <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name ="ISPOSDISCOUNT" value="ISPOSDISCOUNT" id="ISPOSDISCOUNT" onclick="isposdiscount();"
	     <%if(ISPOSDISCOUNT.equals("1")) {%>checked <%}%>/> Allow POS Terminal Discount</label>
         </div>
	</div>
    </div>
    
    
    <div class="form-group">
      <label class="control-label col-form-label col-sm-2" for="Product Department">Product  Department</label>
      <div class="col-sm-4" >                
			<INPUT class="form-control" name="PRD_DEPT_DESC" type="TEXT" value="<%=prddepid%>" size="20" MAXLENGTH=100 id="PRD_DEPT_DESC" placeholder="Select Product Department">
			<span class="select-icon" style="right: 45px;" onclick="$(this).parent().find('input[name=\'PRD_DEPT_DESC\']').focus()">
        <i class="glyphicon glyphicon-menu-down" id="prddepbtnpop"></i></span>				
      </div>
    </div>
    
    <div class="form-group">
      <label class="control-label col-form-label col-sm-2" for="Product Class">Product Category</label>
      <div class="col-sm-4">                
			<INPUT class="form-control" name="PRD_CLS_DESC" type="TEXT" value="<%=prdclsid%>" size="20" MAXLENGTH=100 id="PRD_CLS_DESC" placeholder="Select Product Category">
			<span class="select-icon" style="right: 45px;" onclick="$(this).parent().find('input[name=\'PRD_CLS_DESC\']').focus()">
        <i class="glyphicon glyphicon-menu-down" id="prclassbtnpop"></i></span>				
      </div>
    </div>
    
    <div class="form-group">
      <label class="control-label col-form-label col-sm-2" for="Product Type">Product Sub Category</label>
      <div class="col-sm-4">                
			<INPUT class="form-control" name="PRD_TYPE_DESC" type="TEXT" value="<%=sArtist%>" size="20" MAXLENGTH=100 id="PRD_TYPE_DESC" placeholder="Select Product Sub Category">
			<span class="select-icon" style="right: 45px;" onclick="$(this).parent().find('input[name=\'PRD_TYPE_DESC\']').focus()">
        <i class="glyphicon glyphicon-menu-down" id="prtypbtnpop"></i></span>				
      </div>
    </div>
    
    <div class="form-group">
      <label class="control-label col-form-label col-sm-2" for="Product Brand">Product Brand</label>
      <div class="col-sm-4">                
			<INPUT class="form-control" name="PRD_BRAND_DESC" type="TEXT" value="<%=prdBrand%>" size="20" MAXLENGTH=100 id="PRD_BRAND_DESC" placeholder="Select Product Brand">
			<span class="select-icon" style="right: 45px;" onclick="$(this).parent().find('input[name=\'PRD_BRAND_DESC\']').focus()">
        <i class="glyphicon glyphicon-menu-down" id="prbadbtnpop"></i></span>				
      </div>
    </div>
    
    <div class="form-group" style="display: none;">>
      <label class="control-label col-form-label col-sm-2" for="Department Display">Department Display</label>
      <div class="col-sm-4" >                
			<INPUT class="form-control" name="DEPT_DISPLAY_ID"  type="hidden" value="<%=DEPT_DISPLAY_ID%>" size="20" MAXLENGTH=100 id="DEPT_DISPLAY_ID" placeholder="Select Department Display">
			<span class="select-icon" style="right: 45px;" onclick="$(this).parent().find('input[name=\'DEPT_DISPLAY_ID\']').focus()">
        <i class="glyphicon glyphicon-menu-down" id="prbdeptdispbtnpop"></i></span>				
      </div>
    </div>
    
    <INPUT type="hidden" id="TaxByLabelOrderManagement" name="taxbylabelordermanagement" value=<%=taxbylabelordermanagement%>>
    
    <div class="form-group" style="display:none">
      <label class="control-label col-form-label col-sm-2" for="Product VAT" id="TaxLabelOrderManagement"></label>
      <div class="col-sm-4">                
        <INPUT class="form-control" name="PRODGST" id="PRODGST" type="TEXT" value="<%=PRODGST%>" size="20" MAXLENGTH=50 onkeypress="return isNumberKey(event,this,4)">
      </div>
    </div>
    
    <% if(!COMP_INDUSTRY.equals("Retail")) {%>
    <div class="form-group">
      <label class="control-label col-form-label col-sm-2" for="Upload Image">Upload Product Image</label>
      <div class="col-sm-4">                
        <INPUT class="form-control" name="IMAGE_UPLOAD" type="File" size="20" MAXLENGTH=100>
      </div>
    </div> 
    <%} %>
    
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
      <!--   <li class="nav-item">
            <a href="#rental" class="nav-link" data-toggle="tab">Rental</a>
        </li> -->
        <li class="nav-item">
            <a href="#inventory" class="nav-link" data-toggle="tab">Inventory</a>
        </li>
        <% if(COMP_INDUSTRY.equals("Retail")) {%>
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
			<INPUT class="form-control" name="HSCODE" type="TEXT" value="<%=HSCODE%>" size="20" MAXLENGTH=20 id="HSCODE" placeholder="Select HS Code">
			<span class="select-icon" style="right: 45px;" onclick="$(this).parent().find('input[name=\'HSCODE\']').focus()">
        <i class="glyphicon glyphicon-menu-down" id="hsbtnpop"></i></span>				
      </div>
    </div>
    
    <div class="form-group">
      <label class="control-label col-form-label col-sm-2" for="COO">COO</label>
      <div class="col-sm-4">                
			<INPUT class="form-control" name="COO" type="TEXT" value="<%=COO%>" size="20" MAXLENGTH=50 id="COO" placeholder="Select COO">
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
			<INPUT class="form-control" name="VINNO" type="TEXT" value="<%=VINNO%>" style="width:100%" MAXLENGTH=50>				
      </div>
    </div>
    
    <div class="form-group">
      <label class="control-label col-form-label col-sm-2" for="MODEL">MODEL</label>
      <div class="col-sm-4">
			<INPUT class="form-control" name="MODEL" type="TEXT" value="<%=MODEL%>" style="width:100%" MAXLENGTH=50>				
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
    
    <%-- <div class="form-group">
      <label class="control-label col-form-label col-sm-2" for="Cost">Cost</label>
      <div class="col-sm-4">
			<INPUT class="form-control" name="COST" id="COST" type="TEXT" value="<%=StrUtils.currencyWtoutCommSymbol(cost)%>" size="20"
		MAXLENGTH=50 onchange="validDecimal(this.value)" onkeypress="return isNumberKey(event,this,4)">				
      </div>
    </div> --%>
    
    
    <div class="form-group row">
    <label class="control-label col-form-label col-sm-2" for="Cost">Cost</label>
    <div class="col-sm-4">
        <input class="form-control" name="COST" id="COST" type="text" 
               value="<%=StrUtils.currencyWtoutCommSymbol(cost)%>" size="20" maxlength="50" 
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
<%-- 
       <label class="control-label col-sm-1" for="PriceFC">Price.FC/LC</label>
    <div class="col-sm-2">
        <input class="form-control ac-selected" name="CURPRICE" id="CURPRICE" onchange="currencyconv(this.value)" value="">
        <input class="form-control" name="SELCUR" id="SELCUR" type="text" value="" readonly="" style="position: relative;bottom: 34px;width: 51px;left: 126px;">
    </div>

    <div class="col-sm-1">
        <input class="form-control" name="CONPRICE" id="CONPRICE" type="text" value="" readonly >
        <input class="form-control" name="BASECUR" id="BASECUR" type="text" value="<%=currency%>" readonly="" style="position: relative;bottom: 34px;width: 45px;left: 74px;">
    </div>
    
    <div class="col-sm-2">
        <label class="checkbox-inline" style="position: relative;left: 30px;">
            <input type="checkbox" style="border:0;" name="APPLYCONPRICE" value="APPLYCONPRICE" id="APPLYCONPRICE" onclick="Isapplyconprice(this);"> Apply To Cost
        </label>
    </div> 
--%>
    
</div>
    
    
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
<!--         			 <div class="form-inline"> -->
<!-- 	     <div class="col-sm-3"> -->
<!-- 	     <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name ="ISPOSDISCOUNT" value="ISPOSDISCOUNT" id="ISPOSDISCOUNT" onclick="isposdiscount();" -->
<%-- 	     <%if(ISPOSDISCOUNT.equals("1")) {%>checked <%}%>/> Allow POS Terminal Discount</label> --%>
<!--          </div> -->
<!-- 	     </div> -->
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
								onclick="addItemSupplier('multiitemsup');return false;">+ Add another Supplier</a>
						</div>
					</div>
				</div>
    
    <INPUT type="hidden" name="DYNAMIC_SUPPLIERDISCOUNT_SIZE">
    
    <div class="form-group">
      <label class="control-label col-form-label col-sm-2" for="Supplier Discount">Supplier Discount</label>
      <div class="col-sm-4">
			<label class="radio-inline">
			<INPUT type ="hidden" name="SUPDISCOUNTTYPE" id="SUPDISCOUNTTYPE" value="">
      	<INPUT name="IBDISCOUNT" type = "radio"  value="BYCOST"  id="BYCOST" checked="checked" onclick="document.form.SUPDISCOUNTTYPE.value = 'BYCOST'" onchange="SuppDiscountType('BYCOST')">By Cost
    	</label>
    	<label class="radio-inline">
      	<INPUT  name="IBDISCOUNT" type = "radio" name="IBDISCOUNT" type = "radio" value="BYPERCENTAGE"  id = "BYPERCENTAGE" onclick="document.form.SUPDISCOUNTTYPE.value = 'BYPERCENTAGE'" onchange="SuppDiscountType('BYPERCENTAGE')">By Percentage
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
		<TD align="center"><div class="input-group"><span class="glyphicon glyphicon-remove-circle emptype-action" aria-hidden="true" onclick="deleteRowCost('supplierDiscount');return false;"></span><INPUT class="form-control supplierSearch" name="SUPPLIER_0" id="SUPPLIER_0"  type = "TEXT" value="<%=SUPPLIER%>" size="20" placeholder="Select Supplier" MAXLENGTH=50
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
								onclick="addRowCost('supplierDiscount');return false;">+ Add another discount</a>
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
			<INPUT class="form-control" name="PRICE" id="PRICE" type="TEXT" value="<%=StrUtils.currencyWtoutCommSymbol(price)%>" size="20"
		MAXLENGTH=50 onchange="javascript:enableSellingPrice(this.value);" onkeypress="return isNumberKey(event,this,4)">				
      </div>
    </div>
    
    
    <div class="form-group">
      <!-- <label class="control-label col-form-label col-sm-2" for="List Price">Is Combination Product</label>
      <div class="form-inline">
	     <div class="col-sm-2">
	     	<label class="checkbox-inline" style="margin-bottom: 16px;"><INPUT Type=Checkbox  name ="ISCOMPRO" style="border:0;" value="0" id="ISCOMPRO" onclick="iscombpro();"></label>
         </div>
	   </div> -->
      
      <!-- <div class="col-sm-4">
      		<INPUT Type=Checkbox  name ="ISCOMPRO" value="0" id="ISCOMPRO" onclick="iscombpro();">
      </div> -->
    </div>
    
     <div class="form-group">
     <label class="control-label col-form-label col-sm-2 required">Product Type</label>
      <div class="col-sm-6">
      	<label class="radio-inline"><INPUT Type=radio  name ="ISCOMPRO" style="border:0;" value="NONE" id="NONE" checked="checked" onclick="iscombpro();">None</label>
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
    	<!-- <label class="radio-inline"><INPUT Type=Checkbox  name ="ISCOMPRO" style="border:0;" value="0" id="ISCOMPRO" onclick="iscombpro();">Is Finished Product</label> -->
<!--     	<label class="radio-inline"><INPUT Type=radio  name ="ISCOMPRO" style="border:0;" value="NONE" id="NONE" checked="checked">None</label> -->
<!--     	<label class="radio-inline"><INPUT Type=radio  name ="ISCOMPRO" style="border:0;" value="ISCOMPRO" id="ISCOMPRO">Is Finished Product</label>				 -->
<!--     	<label class="radio-inline"><INPUT Type=radio  name ="ISCOMPRO" style="border:0;" value="ISCOMPRO_SEMI" id="ISCOMPRO_SEMI">Is Semi Finished Product</label>					 -->
      </div>
    </div>
     <div class="form-group comproprice">
      <label class="control-label col-form-label col-sm-2" for="Customer Discount">Product Price Increased Value</label>
      <div class="col-sm-3">
      		<INPUT  class="form-control"  Type="text"  name ="INCPRICE" id="INCPRICE" style="width: 115%;" onchange="isNumCheck();" onkeypress="return isNumberKey(event,this,4)">
      </div>
      <div class="col-sm-1">
      		<INPUT  class="form-control"  Type="text"  name ="INCPRICEUNIT" id="INCPRICEUNIT" value="<%=currency %>" readonly>
      </div>
    </div>
    
    
    <div class="form-group">
      <label class="control-label col-form-label col-sm-2" for="Minimum Selling Price">Minimum Selling Price</label>
      <div class="col-sm-4">
			<INPUT  class="form-control" name="MINSELLINGPRICE" id="MINSELLINGPRICE" type="TEXT" value="<%=StrUtils.currencyWtoutCommSymbol(minsprice)%>" size="20"
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
			<INPUT type ="hidden" name="TYPE" id="TYPE" value="CREATE">
      	<INPUT name="OBDISCOUNT" type = "radio"  name="OBDISCOUNT" type = "radio"  value="BYPRICE"  id="BYPRICE" checked="checked" onclick="document.form.CUSDISCOUNTTYPE.value = 'BYPRICE'" onchange="CusDiscountType('BYPRICE')">By Price
    	</label>
    	<label class="radio-inline">
      	<INPUT  name="OBDISCOUNT" type = "radio" name="OBDISCOUNT" type = "radio" value="BYPERCENTAGE"  id = "BYPERCENTAGE" onclick="document.form.CUSDISCOUNTTYPE.value = 'BYPERCENTAGE'" onchange="CusDiscountType('BYPERCENTAGE')">By Percentage
    	</label>				
      </div>
    </div>
    
    <div class="form-group">
      <label class="control-label col-form-label col-sm-2" for="Customer Discount"></label>
      <div class="col-sm-4">
			<TABLE id="customerDiscount">
		<TR>
		<TD><INPUT class="form-control" name="DYNAMIC_CUSTOMER_DISCOUNT_0" id="DYNAMIC_CUSTOMER_DISCOUNT_0" type="TEXT" value="" onchange="calculateCustDiscount(this)" onkeypress="return isNumberKey(event,this,4)"
		size="20" MAXLENGTH="50"  />&nbsp;</TD>
		<TD align="center"><div class="input-group"><span class="glyphicon glyphicon-remove-circle emptype-action" aria-hidden="true" onclick="deleteRow('customerDiscount');return false;"></span><INPUT class="form-control customerSearch" name="CUSTOMER_TYPE_ID_0" id="CUSTOMER_TYPE_ID_0" placeholder="Select Customer Type" type = "TEXT" value="<%=CUSTOMER_TYPE_ID%>" size="20"  MAXLENGTH=50
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
								onclick="addRow('customerDiscount');return false;">+ Add another discount</a>
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
			<INPUT class="form-control" name="RENTALPRICE" id="RENTALPRICE" type="TEXT" value="<%=StrUtils.currencyWtoutCommSymbol(RENTALPRICE)%>" size="20" onkeypress="return isNumberKey(event,this,4)"
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
		<span class="glyphicon glyphicon-remove-circle emptype-action" style="left: 800px;" aria-hidden="true" onclick="deleteRows('descriptiontbl');return false;"></span>
		<INPUT class="form-control" name="DESCRIPTION" id="DESCRIPTION0"  placeholder="Max 1000 Characters" type = "TEXT" value="<%=Description1%>" size="100"  MAXLENGTH=1000>		        
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
								onclick="addRowDesc('descriptiontbl','');return false;">+ Add another Description</a>
						</div>
					</div>
				</div>
        </div>
        
        <div class="tab-pane fade" id="catalogues">
        <br>
         <div class="form-group">
      	<label class="control-label col-form-label col-sm-2" for="Upload Image">Upload Product Main Image</label>
      		<div class="col-sm-4">                
        		<INPUT class="form-control" name="IMAGE_UPLOAD" id="IMAGE_UPLOAD" type="File" size="20" MAXLENGTH=100>
      		</div>
    	</div>
    	
         <div class="form-group">
      	<label class="control-label col-form-label col-sm-2" for="Upload Image">Upload Product Image 2</label>
      		<div class="col-sm-4">                
        		<INPUT class="form-control" name="IMAGE_UPLOAD2" id="IMAGE_UPLOAD2" type="File" size="20" MAXLENGTH=100>
<!--         		<INPUT class="form-control" name="IMAGE_UPLOAD2" id="IMAGE_UPLOAD2" type="File" size="20" onchange="checkImageSize('IMAGE_UPLOAD2',this.files[0])" MAXLENGTH=100> -->
      		</div>
    	</div>
    	
         <div class="form-group">
      	<label class="control-label col-form-label col-sm-2" for="Upload Image">Upload Product Image 3</label>
      		<div class="col-sm-4">                
        		<INPUT class="form-control" name="IMAGE_UPLOAD3" id="IMAGE_UPLOAD3" type="File" size="20" MAXLENGTH=100>
      		</div>
    	</div>
    	
         <div class="form-group">
      	<label class="control-label col-form-label col-sm-2" for="Upload Image">Upload Product Image 4</label>
      		<div class="col-sm-4">                
        		<INPUT class="form-control" name="IMAGE_UPLOAD4" id="IMAGE_UPLOAD4" type="File" size="20" MAXLENGTH=100>
      		</div>
    	</div>
    	
         <div class="form-group">
      	<label class="control-label col-form-label col-sm-2" for="Upload Image">Upload Product Image 5</label>
      		<div class="col-sm-4">                
        		<INPUT class="form-control" name="IMAGE_UPLOAD5" id="IMAGE_UPLOAD5" type="File" size="20" MAXLENGTH=100>
      		</div>
    	</div>
    	
         <div class="form-group">
      	<label class="control-label col-form-label col-sm-2" for="Upload Image">Upload Product Image 6</label>
      		<div class="col-sm-4">                
        		<INPUT class="form-control" name="IMAGE_UPLOAD6" id="IMAGE_UPLOAD6" type="File" size="20" MAXLENGTH=100>
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
		<TD align="center"  style="width: 93%;"><div class="col-sm-6"><div class="input-group">
		<span class="glyphicon glyphicon-remove-circle emptype-action" style="left: 500px;" aria-hidden="true" onclick="deleteRowPrd('prdtbl');return false;"></span>
		<INPUT class="form-control itemSearch" name="PRODUCT" id="PRODUCT0"  placeholder="Select Product" type = "TEXT" value="<%=Product1%>" size="100"  MAXLENGTH=200>		        
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
								onclick="addRowPrd('prdtbl','');return false;">+ Add another Product</a>
						</div>
					</div>
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
				<textarea  class="form-control" name="TITLE" placeholder="Max 100 Characters"  MAXLENGTH=100><%=sTitle%></textarea>
			</div>
		</div>

		</div>
		
		<!--</div>
    <div class="col-sm-6">
   
    <div class="panel panel-default">
    <div class="panel-heading" style="background: #eaeafa; height: 36px"><strong>Service</strong></div>
    <div class="panel-body">
    <div class="form-group">
      	<label class="control-label col-sm-6" for="Service UOM">
      	<i class="glyphicon glyphicon-star" style="font-size: 10px; top:-6px; color: #e50000"></i>&nbsp;Service UOM:</label>     	
        <div class="col-sm-6">
      	<div class="input-group"> -->
    	<INPUT class="form-control" name="SERVICEUOM" type="hidden" value="<%=SERVICEUOM%>" size="20" MAXLENGTH=100 readonly id="serviceuom">
   		<!-- <span class="input-group-addon"  onClick="UOMpopUpwin('SERVICEUOM');return false;">
   		<a href="#" data-toggle="tooltip" data-placement="top" title="Service UOM Details">
   		<i class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span>
  		</div>
  		</div>     
  	  	</div> -->
  	  	<!-- <div class="form-group">
      	<label class="control-label col-sm-6" for="Service Price">Service Price:</label>
      	<div class="col-sm-6">   -->        
        <INPUT class="form-control" name="SERVICEPRICE" id="SERVICEPRICE" type="hidden" value="<%=StrUtils.currencyWtoutCommSymbol(SERVICEPRICE)%>" size="20"
		MAXLENGTH=50 onchange="validDecimal(this.value)">
      	<!-- </div>
    	</div>
    </div>
    </div>
  	  </div>
  	 </div> -->	
		
        </div>
    
    <div class="form-group">        
      	<div class="col-sm-12" align="center">
      	<!--<button type="button" class="Submit btn btn-default" onClick="window.location.href='../home'"><b>Back</b></button>&nbsp;&nbsp;
      	 <button type="button" class="Submit btn btn-default" onClick="{backNavigation('settings.jsp','SM');}"><b>Back</b></button>&nbsp;&nbsp; -->
      	<button type="button" class="Submit btn btn-default" onClick="onClear();" sNewEnb >Clear</button>&nbsp;&nbsp;
      	<button type="button" class="Submit btn btn-success" data-toggle="modal" data-target="#myModal" onClick="onAdd();" sAddEnb >Save</button>&nbsp;&nbsp;
   <!--   <INPUT class="hidden" type="BUTTON" value="Print Barcode" onClick="onPrint();" sDeleteEnb/>  -->
      	</div>
    	</div>
    <!--  <INPUT     name="ISPARENTCHILD"  type ="hidden" value="<%=ISPARENTCHILD%>" size="1"   MAXLENGTH=80 ></TD>-->
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
	
	
	
<script>
$(document).ready(function(){
    $('[data-toggle="tooltip"]').tooltip();   
	
	var  d = document.getElementById("TaxByLabelOrderManagement").value;
    document.getElementById('TaxLabelOrderManagement').innerHTML = "Product " + d +" (%):";
    
    var DESCRIPTION_SIZE1 = document.form.DESCRIPTION_SIZE.value;
    if(DESCRIPTION_SIZE1!=0)
	{
	
	for ( var i = 0; i < DESCRIPTION_SIZE1; i++) {
		if(i!=0)
			{
				var footerval= document.getElementById("F"+(i+1)).value; 
				addRowDesc('descriptiontbl',footerval);
			}
	}
	}
    
    var PRD_SIZE1 = document.form.PRD_SIZE.value;
    if(PRD_SIZE1!=0)
	{
	
	for ( var i = 0; i < PRD_SIZE1; i++) {
		if(i!=0)
			{
				var footerval= document.getElementById("P"+(i+1)).value; 
				addRowPrd('prdtbl',footerval);
			}
	}
	}
    $(".itemSearch").typeahead('destroy');
	addSuggestionprd();
});
</script>
<script>
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
   if(ITEM == "" || ITEM == null) {alert("Please Choose Product ID"); document.form.ITEM.focus(); return false; }
   document.form.action  = "PrintItemLabel.jsp?action=PRINT&Item="+ITEM+"&ITEMDESC="+ITEMDESC;
   alert("Do you want to Print?");
   document.form.submit();
}


function addItemSupplier(tableID) {
	var table = document.getElementById(tableID);
	var rowCount = table.rows.length;
	var row = table.insertRow(rowCount);
	var form = document.forms['form'];
	var itemCell = row.insertCell(0);
	var itemCellText =  "<div class=\"input-group\"> <span class=\"glyphicon glyphicon-remove-circle emptype-action\" aria-hidden=\"true\" onclick=\"deleteItemSupplier('multiitemsup');return false;\"></span> <INPUT class=\"form-control vendorSearch\" name=\"ITEMSUPPLIER_"+rowCount+"\" ";
	itemCellText = itemCellText+ " id=\"ITEMSUPPLIER_"+rowCount+"\" type = \"TEXT\" size=\"55\" placeholder=\"Select Supplier\"  onkeypress=\"if((event.keyCode=='13') && ( this.value.length > 0)){validateItemSupplier("+rowCount+");}\" MAXLENGTH=\"50\"></div>&nbsp;";
	itemCell.innerHTML = itemCellText;
	removeSuggestionSearch();
	addSuggestionSearch();
}

function deleteItemSupplier(tableID) {
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

function addRow(tableID) {
	
	  	
	var table = document.getElementById(tableID);
	var rowCount = table.rows.length;
	var row = table.insertRow(rowCount);
	 var form = document.forms['form'];


	var itemCell = row.insertCell(0);
	var itemCellText =  "<INPUT class=\"form-control\" name=\"DYNAMIC_CUSTOMER_DISCOUNT_"+rowCount+"\"  onkeypress=\"return isNumberKey(event,this,4)\" ";
	itemCellText = itemCellText+ " id=\"DYNAMIC_CUSTOMER_DISCOUNT_"+rowCount+"\" type = \"TEXT\" onchange=\"calculateCustDiscount(this)\" size=\"20\" MAXLENGTH=\"50\">&nbsp;";
	itemCell.innerHTML = itemCellText;
	
	
	var itemCell = row.insertCell(1);
	var itemCellText =  "<div class=\"input-group\"><span class=\"glyphicon glyphicon-remove-circle emptype-action\" aria-hidden=\"true\" onclick=\"deleteRow('customerDiscount');return false;\"></span><INPUT class=\"form-control customerSearch\" name=\"CUSTOMER_TYPE_ID_"+rowCount+"\" ";
	itemCellText = itemCellText+ " id=\"CUSTOMER_TYPE_ID_"+rowCount+"\" type = \"TEXT\" size=\"20\"  placeholder=\"Select Customer Type\"  onkeypress=\"if((event.keyCode=='13') && ( this.value.length > 0)){validateCustomerType("+rowCount+");}\" MAXLENGTH=\"50\"></div>&nbsp;";
	/* itemCellText = itemCellText+ "<span class=\"input-group-addon\"><a href=\"#\" onClick=\"javascript:popUpWin('list/multiCustomerTypeList.jsp?CUSTOMERTYPE='+form.CUSTOMER_TYPE_ID"+'_'+rowCount+".value+'&INDEX="+rowCount+"');return false;\"><i class=\"glyphicon glyphicon-log-in\" style=\"font-size: 20px;\"></i></a></span></div>&nbsp;"; */
	itemCell.innerHTML = itemCellText;
	
	removeSuggestionSearch();
	addSuggestionSearch();
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

function addRowCost(tableID) {
	
  	
	var table = document.getElementById(tableID);
	var rowCount = table.rows.length;
	var row = table.insertRow(rowCount);
	 var form = document.forms['form'];


	var itemCell = row.insertCell(0);
	var itemCellText =  "<INPUT name=\"DYNAMIC_SUPPLIER_DISCOUNT_"+rowCount+"\"  onkeypress=\"return isNumberKey(event,this,4)\" ";
	itemCellText = itemCellText+ " id=\"DYNAMIC_SUPPLIER_DISCOUNT_"+rowCount+"\" onchange=\"calculateSuppDiscount(this)\" type = \"TEXT\" size=\"20\" MAXLENGTH=\"50\" class=\"form-control\">&nbsp;";
	itemCell.innerHTML = itemCellText;
	
	
	var itemCell = row.insertCell(1);
	var itemCellText =  "<div class=\"input-group\"> <span class=\"glyphicon glyphicon-remove-circle emptype-action\" aria-hidden=\"true\" onclick=\"deleteRowCost('supplierDiscount');return false;\"></span> <INPUT class=\"form-control supplierSearch\" name=\"SUPPLIER_"+rowCount+"\" ";
	itemCellText = itemCellText+ " id=\"SUPPLIER_"+rowCount+"\" type = \"TEXT\" size=\"20\" placeholder=\"Select Supplier\"  onkeypress=\"if((event.keyCode=='13') && ( this.value.length > 0)){validateSupplier("+rowCount+");}\" MAXLENGTH=\"50\"></div>&nbsp;";
	/* itemCellText = itemCellText+ "<span class=\"input-group-addon\"><a href=\"#\" onClick=\"javascript:popUpWin('list/multiSupplierList.jsp?SUPPLIER='+form.SUPPLIER"+'_'+rowCount+".value+'&INDEX="+rowCount+"');return false;\"><i class=\"glyphicon glyphicon-log-in\" style=\"font-size: 20px;\"></i></a></span></div>&nbsp;"; */
	itemCell.innerHTML = itemCellText;
	
	removeSuggestionSearch();
	addSuggestionSearch();
	
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
		//}
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

	function validateItemSupplier(index) {
	 	 var table = document.getElementById("multiitemsup");
		 var rowCount = table.rows.length;
		var scansupplier = document.getElementById("ITEMSUPPLIER_"+index).value;
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
					                  if(scancustomertype==document.getElementById("ITEMSUPPLIER_"+r).value)
					                	{
					                		alert("Supplier already exists for the product");
					                		document.getElementById("ITEMSUPPLIER_"+index).focus();
											document.getElementById("ITEMSUPPLIER_"+index).value="";
											return false;
					                	}
					            }
					        }
					        
					     } else {
							alert("Not a valid Supplier");
							document.getElementById("ITEMSUPPLIER_"+index).value = "";
							document.getElementById("ITEMSUPPLIER_"+index).focus();
						}
					}
				});
		}
		//onLoad();
</script>

</form>
</div>
</div>
</div>



<!-- ----------------Modal-------------------------- -->
<%@include file="../jsp/newProductDepTypeModal.jsp"%> <!-- Resvi for add product department -->
<%@include file="../jsp/newProductCategoryModal.jsp"%> <!-- Thanzith for add product category -->
<%@include file="../jsp/newProductLocationModal.jsp"%> <!-- Thanzith for add product location -->
<%@include file="/jsp/newProductSubCatgoryModal.jsp"%> <!-- Thanzith for add product sub Category -->
<%@include file="/jsp/newProductBrandModal.jsp"%> <!-- Thanzith for add product Brand -->
<%@include file="../jsp/newProductHsCodeModal.jsp"%> <!-- Thanzith for add product Hscode -->
<%@include file="../jsp/newProductCOOModal.jsp"%> <!-- Thanzith for add product COO -->
<%@include file="../jsp/newProductUOMmodal.jsp"%> <!-- Thanzith for add product COO -->

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
									type="TEXT" value="" onkeypress="return blockSpecialChar(event)"
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
						 <input name="SUPPLIER_TYPE_ID" type="hidden" value="<%=SUPPLIER_TYPE_ID%>">
  							
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

<!-- 								<div class="form-group"> -->
<!-- 									<label class="control-label col-form-label col-sm-2" -->
<!-- 										for="Payment Terms">Payment Type</label> -->
<!-- 									<div class="col-sm-4"> -->
<!-- 										<div class="input-group"> -->
<!-- 											<INPUT class="form-control" name="PAYTERMS" type="TEXT" -->
<!-- 												value="" size="20" MAXLENGTH=100 readonly> -->
<!-- 											<span class="input-group-addon" -->
<!-- 												onClick="javascript:popUpWin('../jsp/list/paymenttypelist_save.jsp?paymenttype='+form1.PAYTERMS.value+'&PAYTYPE=POPUP1');"> -->
<!-- 												<a href="#" data-toggle="tooltip" data-placement="top" -->
<!-- 												title="Payment Type"> <i -->
<!-- 													class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a> -->
<!-- 											</span> -->
<!-- 										</div> -->
<!-- 									</div> -->

<!-- 									<div class="form-inline"> -->
<!-- 										<label class="control-label col-form-label col-sm-1" -->
<!-- 											for="Payment Terms">Days</label> <input name="PMENT_DAYS" -->
<!-- 											type="TEXT" value="" size="5" MAXLENGTH=10 -->
<!-- 											class="form-control"> -->
<!-- 									</div> -->
<!-- 								</div> -->

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
<%-- 				<SELECT class="form-control" data-toggle="dropdown" data-placement="right" onchange="OnBank(this.value)" id="BANKNAME" name="BANKNAME" value="<%=sBANKNAME%>" style="width: 100%"> --%>
<!-- 				<OPTION style="display:none;">Select Bank</OPTION> -->
	<INPUT name="BANKNAME" type = "TEXT" value="" id="BANKNAME" class="form-control"   placeholder="BANKNAME">
	 <span class="select-icon"  onclick="$(this).parent().find('input[name=\'BANKNAME\']').focus()">
	<i class="glyphicon glyphicon-menu-down"></i></span>
	
  	</div>
				<%-- <%
		    _MasterUtil=new  MasterUtil();
		    ccList =  _MasterUtil.getBankList("",plant);
			for(int i=0 ; i<ccList.size();i++)
      		 {
				Map m=(Map)ccList.get(i);
				String vNAME = (String)m.get("NAME"); %>
		        <option  value='<%=vNAME%>' ><%=vNAME %> </option>		          
		        <%
       			}
			 %> --%>
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
							<!-- <button type="button" class="Submit btn btn-default"
								onClick="window.location.href='../home'">Back</button>
							&nbsp;&nbsp;
							<button type="button" class="Submit btn btn-default" onClick="{backNavigation('settings.jsp','SA');}"><b>Back</b></button>&nbsp;&nbsp;
							<button type="button" class="Submit btn btn-default"
								onClick="onNew();" >Clear</button>
							&nbsp;&nbsp; -->
							<button type="button" class="btn btn-success" onClick="onAddSupplier();" >
								Save
							</button>
							&nbsp;&nbsp;
							<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>

						</div>
					</div>




				</div>
				<!-- <div class="modal-footer">
	      	<button id="btnBillOpen" type="button" class="btn btn-success" onClick="onAdd();">Save</button>
			<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
	      </div> -->
			</div>
		</form>
	</div>
</div>

<%@include file="../jsp/newBankModal.jsp"%>
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
	   

	   /* if(region == "GCC"){
		   document.form1.companyregnumber.value="";
		}else if(region == "ASIA PACIFIC"){
			if (companyregnumber == "" || companyregnumber == null) {
			alert("Please Enter Unique Entity Number (UEN)");
			document.form1.companyregnumber.focus();
			return false; 
			}
		} */

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
	
// 	function iscombpro(){
// 		if (document.getElementById("ISCOMPRO").checked == true) {
// 			document.getElementById("ISCOMPRO").value = "1";
// 			$(".comproprice").show();
			
// 		}  else {
// 			document.getElementById("ISCOMPRO").value = "0";
// 			$(".comproprice").hide();
// 		}
		
// 	}

	function iscombpro(){
		if (document.getElementById("NONE").checked == true) {
			document.getElementById("NONE").value = "NONE";
		/* 	$(".comproprice").hide(); */
		}  else {
			document.getElementById("NONE").value = "NONE";
			$(".comproprice").show();
		}
	}
	 
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

	function suppliertypeCallback(data){
		if(data.STATUS="SUCCESS"){
			$("#SUPPLIER_TYPE_S").typeahead('val', data.SUPPLIER_TYPE_DESC);
			$("input[name=SUPPLIER_TYPE_ID]").val(data.SUPPLIER_TYPE_ID);
		}
	}
	
	$('select[name="COUNTRY_CODE_S"]').on('change', function(){		
		var text = $("#COUNTRY_CODE_S option:selected").text();
		// $("input[name ='COUNTRY']").val(text.trim());
		document.form1.COUNTRY.value = text.trim();
		});	
	function readURL(input) {
	    if (input.files && input.files[0]) {
	        var reader = new FileReader();
	        const allowedExtension = ['image/jpeg', 'image/jpg', 'image/png','image/gif','image/bmp','image/webp'];
	        //alert(input.files[0].type);
	        if(allowedExtension.indexOf(input.files[0].type)>-1)
	        {
	        } else {
	       		//Validate Image -Azees 02.02.23
	             alert('Upload valid image file');
	             $('#IMAGE_UPLOAD').val('');
	             return false;
	    	}
	        reader.onload = function (e) {
	        	var image = new Image();
	        	image.src = e.target.result;
	        	//Validate the File Height and Width.250px -Azees 02.02.23
	        	  image.onload = function () {
	        	    var height = this.height;
	        	    var width = this.width;

// 	        	    	if (input.files[0].size > 6296932) {}
	        	    /*var plant = $("input[name=PLANT]").val();
	        	    if(!(plant=='C2716640758S2T' || plant=='C1255800687S2T' || plant=='C4376460171S2T' || plant=='C5500747293S2T' || plant=='C697464484S2T' || plant=='C7743535839S2T' || plant=='C947002346S2T')){
		        	    
	        	    if (height > 250 || width > 250) {
	        	    $('#IMAGE_UPLOAD').val('');
	        	    alert("Image size should be less than 250 Pixel");
	        	      return false;
	        	    }
	        	    }*/ 
	        	    //alert("Uploaded image has valid Height and Width.");
	        	    return true;
	        	  };
	        }

	        reader.readAsDataURL(input.files[0]);
	    }
	}
	$(document).on('change', '#IMAGE_UPLOAD', function (e) {
	    readURL(this);
	});
	function readURL1(input) {
	    if (input.files && input.files[0]) {
	        var reader = new FileReader();
	        const allowedExtension = ['image/jpeg', 'image/jpg', 'image/png','image/gif','image/bmp','image/webp'];
	        if(allowedExtension.indexOf(input.files[0].type)>-1)
	        {
	        } else {
	       		//Validate Image -Azees 02.02.23
	             alert('Upload valid image file');
	             $('#IMAGE_UPLOAD2').val('');
	             return false;
	    	}
	        reader.onload = function (e) {
	        	
	        	var image = new Image();
	        	image.src = e.target.result;
	        	//Validate the File Height and Width.250px -Azees 02.02.23
	        	  image.onload = function () {
	        	    var height = this.height;
	        	    var width = this.width;
	        	    var plant = $("input[name=PLANT]").val();
	        	    if(!(plant=='C2716640758S2T' || plant=='C1255800687S2T' || plant=='C4376460171S2T' || plant=='C5500747293S2T' || plant=='C697464484S2T' || plant=='C7743535839S2T' || plant=='C947002346S2T')){
	        	    if (height > 250 || width > 250) {
	        	    $('#IMAGE_UPLOAD2').val('');
	        	      alert("Height and Width must not exceed 250px.");
	        	      return false;
	        	    }
	        	    }
	        	    return true;
	        	  };
	        }

	        reader.readAsDataURL(input.files[0]);
	    }
	}
	$(document).on('change', '#IMAGE_UPLOAD2', function (e) {
	    readURL1(this);
	});
	function readURL2(input) {
	    if (input.files && input.files[0]) {
	        var reader = new FileReader();
	        const allowedExtension = ['image/jpeg', 'image/jpg', 'image/png','image/gif','image/bmp','image/webp'];
	        if(allowedExtension.indexOf(input.files[0].type)>-1)
	        {
	        } else {
	       		//Validate Image -Azees 02.02.23
	             alert('Upload valid image file');
	             $('#IMAGE_UPLOAD3').val('');
	             return false;
	    	}
	        reader.onload = function (e) {
	        	
	        	var image = new Image();
	        	image.src = e.target.result;
	        	//Validate the File Height and Width.250px -Azees 02.02.23
	        	  image.onload = function () {
	        	    var height = this.height;
	        	    var width = this.width;
	        	    var plant = $("input[name=PLANT]").val();
	        	    if(!(plant=='C2716640758S2T' || plant=='C1255800687S2T' || plant=='C4376460171S2T' || plant=='C5500747293S2T' || plant=='C697464484S2T' || plant=='C7743535839S2T' || plant=='C947002346S2T')){
	        	    if (height > 250 || width > 250) {
	        	    $('#IMAGE_UPLOAD3').val('');
	        	      alert("Height and Width must not exceed 250px.");
	        	      return false;
	        	    }
	        	    }
	        	    return true;
	        	  };
	        }

	        reader.readAsDataURL(input.files[0]);
	    }
	}
	$(document).on('change', '#IMAGE_UPLOAD3', function (e) {
	    readURL2(this);
	});
	function readURL3(input) {
	    if (input.files && input.files[0]) {
	        var reader = new FileReader();
	        const allowedExtension = ['image/jpeg', 'image/jpg', 'image/png','image/gif','image/bmp','image/webp'];
	        if(allowedExtension.indexOf(input.files[0].type)>-1)
	        {
	        } else {
	       		//Validate Image -Azees 02.02.23
	             alert('Upload valid image file');
	             $('#IMAGE_UPLOAD4').val('');
	             return false;
	    	}
	        reader.onload = function (e) {
	        	
	        	var image = new Image();
	        	image.src = e.target.result;
	        	//Validate the File Height and Width.250px -Azees 02.02.23
	        	  image.onload = function () {
	        	    var height = this.height;
	        	    var width = this.width;
	        	    var plant = $("input[name=PLANT]").val();
	        	    if(!(plant=='C2716640758S2T' || plant=='C1255800687S2T' || plant=='C4376460171S2T' || plant=='C5500747293S2T' || plant=='C697464484S2T' || plant=='C7743535839S2T' || plant=='C947002346S2T')){
	        	    if (height > 250 || width > 250) {
	        	    $('#IMAGE_UPLOAD4').val('');
	        	      alert("Height and Width must not exceed 250px.");
	        	      return false;
	        	    }
	        	    }
	        	    return true;
	        	  };
	        }

	        reader.readAsDataURL(input.files[0]);
	    }
	}
	$(document).on('change', '#IMAGE_UPLOAD4', function (e) {
	    readURL3(this);
	});
	function readURL4(input) {
	    if (input.files && input.files[0]) {
	        var reader = new FileReader();
	        const allowedExtension = ['image/jpeg', 'image/jpg', 'image/png','image/gif','image/bmp','image/webp'];
	        if(allowedExtension.indexOf(input.files[0].type)>-1)
	        {
	        } else {
	       		//Validate Image -Azees 02.02.23
	             alert('Upload valid image file');
	             $('#IMAGE_UPLOAD5').val('');
	             return false;
	    	}
	        reader.onload = function (e) {
	        	
	        	var image = new Image();
	        	image.src = e.target.result;
	        	//Validate the File Height and Width.250px -Azees 02.02.23
	        	  image.onload = function () {
	        	    var height = this.height;
	        	    var width = this.width;
	        	    var plant = $("input[name=PLANT]").val();
	        	    if(!(plant=='C2716640758S2T' || plant=='C1255800687S2T' || plant=='C4376460171S2T' || plant=='C5500747293S2T' || plant=='C697464484S2T' || plant=='C7743535839S2T' || plant=='C947002346S2T')){
	        	    if (height > 250 || width > 250) {
	        	    $('#IMAGE_UPLOAD5').val('');
	        	      alert("Height and Width must not exceed 250px.");
	        	      return false;
	        	    }
	        	    }
	        	    return true;
	        	  };
	        }

	        reader.readAsDataURL(input.files[0]);
	    }
	}
	$(document).on('change', '#IMAGE_UPLOAD5', function (e) {
	    readURL4(this);
	});
	function readURL5(input) {
	    if (input.files && input.files[0]) {
	        var reader = new FileReader();
	        const allowedExtension = ['image/jpeg', 'image/jpg', 'image/png','image/gif','image/bmp','image/webp'];
	        if(allowedExtension.indexOf(input.files[0].type)>-1)
	        {
	        } else {
	       		//Validate Image -Azees 02.02.23
	             alert('Upload valid image file');
	             $('#IMAGE_UPLOAD6').val('');
	             return false;
	    	}
	        reader.onload = function (e) {
	        	
	        	var image = new Image();
	        	image.src = e.target.result;
	        	//Validate the File Height and Width.250px -Azees 02.02.23
	        	  image.onload = function () {
	        	    var height = this.height;
	        	    var width = this.width;
	        	    var plant = $("input[name=PLANT]").val();
	        	    if(!(plant=='C2716640758S2T' || plant=='C1255800687S2T' || plant=='C4376460171S2T' || plant=='C5500747293S2T' || plant=='C697464484S2T' || plant=='C7743535839S2T' || plant=='C947002346S2T')){
	        	    if (height > 250 || width > 250) {
	        	    $('#IMAGE_UPLOAD6').val('');
	        	      alert("Height and Width must not exceed 250px.");
	        	      return false;
	        	    }
	        	    }
	        	    return true;
	        	  };
	        }

	        reader.readAsDataURL(input.files[0]);
	    }
	}
	$(document).on('change', '#IMAGE_UPLOAD6', function (e) {
	    readURL5(this);
	});
</script>


 <jsp:include page="newPaymentTermsModal.jsp"flush="true"></jsp:include><!-- imti for add paymentterms -->
 <jsp:include page="newPaymentModeModal.jsp" flush="true"></jsp:include><!-- added imthi for payment mode -->
 <jsp:include page="newTransportModeModal.jsp" flush="true"></jsp:include> <!-- imti for add transport -->
 <jsp:include page="NewSupplierTypeModal.jsp" flush="true"></jsp:include> <!-- thanzith Add For SupplierType -->
<jsp:include page="footer2.jsp" flush="true">
		<jsp:param name="title" value="<%=title %>" />
	</jsp:include>

