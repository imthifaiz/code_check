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
<%@ page import="com.track.constants.*"%>
<%@ include file="header.jsp"%>
<%@ page import="com.track.dao.*"%>
<%@ page import ="javax.script.ScriptEngineManager"%>
<%@ page import ="javax.script.ScriptEngine"%>
<%@ page import ="javax.script.Invocable"%>

<%
String title = "Edit Product Details";
%>
<%@include file="sessionCheck.jsp" %>
<jsp:include page="header2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
	<jsp:param name="mainmenu" value="<%=IConstants.SETTINGS%>"/>
	<jsp:param name="submenu" value="<%=IConstants.SYSTEM_MASTER%>"/>
</jsp:include>

<script type="text/javascript" src="js/json2.js"></script>
<script type="text/javascript" src="js/general.js"></script>

<script type="text/javascript">
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
function image_edit(){
		$('#userImage').val('');
	    var formData = new FormData($('form')[0]);
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

function UOMpopUpwin(UOMTYPE){
	  var uom = document.form.UOM.value;	
		
		 popUpWin('list/uomlist_save.jsp?UOM='+uom+'&UOMTYPE='+UOMTYPE);
	     
		
	}

function onUpdate(){
	
    var ITEM   = document.form.ITEM.value;
    var DESC = document.form.DESC.value;
    var uom = document.form.UOM.value;
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
	 
      if(ITEM == "" || ITEM == null)
      {
      alert("Please key in Product");
      document.form.ITEM.focus(); return false;
      }
  
 
      else if(DESC=="" || DESC == null)
      {
      alert("Please key in Description");return false;
      }
  
      else if(uom=="" || uom == null)
      {
      alert("Please key in UOM");return false;
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
                alert("Please enter Price");
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
        alert("Please enter Cost");
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
						/*if (disdecNum.length > 5)
						{
						alert("Invalid more than 2 digits after decimal in Supplier Discount");
						document.getElementById("DYNAMIC_SUPPLIER_DISCOUNT_"+(r)).focus();
						return false;
						
						}*/

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
			// to validate customer discount sales  end 
			var priceamt = document.getElementById('PRICE').value;
			var mpriceamt = document.getElementById('MINSELLINGPRICE').value;
			var costamt = document.getElementById('COST').value;
			if (priceamt.indexOf('.') == -1)
				priceamt += ".";
			var decNum = priceamt.substring(priceamt.indexOf('.') + 1,
					priceamt.length);

			var chk = confirm("Are you sure you would like to save?");
			if (chk) {
				document.form.DYNAMIC_ALTERNATE_SIZE.value = document
						.getElementById("alternateItemDynamicTable").rows.length;
				document.form.DYNAMIC_CUSTOMERDISCOUNT_SIZE.value = document
						.getElementById("customerDiscount").rows.length;
				document.form.DYNAMIC_SUPPLIERDISCOUNT_SIZE.value = document
						.getElementById("supplierDiscount").rows.length;
				document.form.action = "maint_item.jsp?action=UPDATE";
				document.form.submit();
			} else {

				return false;
			}

		}

	}
	function onDelete() {

		var ITEM = document.form.ITEM.value;
		if (ITEM == "" || ITEM == null) {
			alert("Please Key in Prdocuct ID");
			document.form.ITEM.focus();
			return false;
		}
		var delmsg = confirm("Are you sure you would like to delete?");
		if (delmsg) {
			document.form.action = "maint_item.jsp?action=DELETE";
			document.form.submit();
		} else {
			return false;
		}
	}
	function onClear() {
		document.form.ITEM.value = "";
		document.form.DESC.value = "";
		document.form.LOC_0.value = "";
		//document.form.PRD_CLS_ID.selectedIndex = 0;
		//document.form.ARTIST.selectedIndex = 0;
		// Start code added by Deen for product brand on 11/9/12
		//document.form.PRD_BRAND.selectedIndex = 0;
		// End code added by Deen for product brand on 11/9/12
		//document.form.UOM.selectedIndex = 0;
		document.form.REMARKS.value = "";
		document.form.MANUFACT.value = "";
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
		document.form.PRD_CLS_ID.value="";
		document.form.ARTIST.value="";
		document.form.PRD_BRAND.value="";
		document.form.VINNO.value = "";
		document.form.MODEL.value = "";
		document.form.RENTALPRICE.value="0.00000";
		document.form.SERVICEPRICE.value="0.00000";
		document.form.PURCHASEUOM.value="";
		document.form.SALESUOM.value="";
		document.form.RENTALUOM.value="";
		document.form.SERVICEUOM.value="";
		document.form.INVENTORYUOM.value="";
		//document.form.ISBASICUOM.checked = false;
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
	String action = "",sRemark1   = "" , nonstkflag="" ,sRemark2  = "", sRemark3   = "",prd_cls_id="" ,prdBrand = "";
	String sItemCondition = "",sArtist="",sTitle="",sMedium="",sRemark="",sUOM="",stkqty="",isActive="";
	String DYNAMIC_ALTERNATE_SIZE = "",ISPARENTCHILD="",nonstk="",nonstkid="",loc="",maxstkqty="",CUSTOMER_TYPE_ID="", DYNAMIC_CUSTOMERDISCOUNT_SIZE="",FLAGSUCCESS="",
		   SUPPLIER="",DYNAMIC_SUPPLIERDISCOUNT_SIZE="",IBDISCOUNT="",NETWEIGHT="",
		   GROSSWEIGHT="",HSCODE="",COO="",VINNO="",MODEL="",RENTALPRICE="",SERVICEPRICE="",PURCHASEUOM="",SALESUOM="",RENTALUOM="",SERVICEUOM="",INVENTORYUOM="",ISBASICUOM="";
	
	String discount="",PRODGST="",OBDiscounttype="",IBDiscounttype="";
	String plant=(String)session.getAttribute("PLANT");
	String taxbylabelordermanagement = ub.getTaxByLableOrderManagement(plant);
	MovHisDAO mdao = new MovHisDAO(plant);
	StrUtils strUtils = new StrUtils();
	ItemUtil itemUtil = new ItemUtil();
	DateUtils dateutils = new DateUtils();
	PrdTypeUtil prdutil = new PrdTypeUtil();
	PrdBrandUtil prdbrandutil = new PrdBrandUtil();
	PrdClassUtil prdcls = new PrdClassUtil();
	 ItemMstDAO itemdao = new ItemMstDAO();
	sqlBean sqlbn= new sqlBean();
	PlantMstDAO plantMstDAO = new PlantMstDAO();
	itemUtil.setmLogger(mLogger);
	prdutil.setmLogger(mLogger);
	prdcls.setmLogger(mLogger);
	action = StrUtils.fString(request.getParameter("action"));
	String numberOfDecimal = plantMstDAO.getNumberOfDecimal(plant);
	String username=(String)session.getAttribute("LOGIN_USER");
	sItem     = StrUtils.fString(request.getParameter("ITEM"));
	sItemDesc      = StrUtils.fString(request.getParameter("DESC"));
	sItemDesc      = strUtils.replaceCharacters2Recv(sItemDesc);
	prd_cls_id=StrUtils.fString(request.getParameter("PRD_CLS_ID"));
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
	OBDiscounttype = StrUtils.fString(request.getParameter("OBDISCOUNT"));
	FLAGSUCCESS=StrUtils.fString(request.getParameter("FLAGSUCCESS"));
	loc ="";
	maxstkqty = StrUtils.fString(request.getParameter("MAXSTKQTY"));
	DYNAMIC_SUPPLIERDISCOUNT_SIZE = StrUtils.fString(request.getParameter("DYNAMIC_SUPPLIERDISCOUNT_SIZE"));
	IBDiscounttype = StrUtils.fString(request.getParameter("IBDISCOUNT"));
	NETWEIGHT     = StrUtils.fString(request.getParameter("NETWEIGHT"));
	GROSSWEIGHT     = StrUtils.fString(request.getParameter("GROSSWEIGHT"));
	HSCODE     = StrUtils.fString(request.getParameter("HSCODE"));
	COO     = StrUtils.fString(request.getParameter("COO"));
	VINNO     = StrUtils.fString(request.getParameter("VINNO"));
	MODEL     = StrUtils.fString(request.getParameter("MODEL"));
	RENTALPRICE = StrUtils.fString(request.getParameter("RENTALPRICE"));
	SERVICEPRICE = StrUtils.fString(request.getParameter("SERVICEPRICE"));
	PURCHASEUOM = StrUtils.fString(request.getParameter("PURCHASEUOM"));
	SALESUOM = StrUtils.fString(request.getParameter("SALESUOM"));
	RENTALUOM = StrUtils.fString(request.getParameter("RENTALUOM"));
	SERVICEUOM = StrUtils.fString(request.getParameter("SERVICEUOM"));
	INVENTORYUOM = StrUtils.fString(request.getParameter("INVENTORYUOM"));
	ISBASICUOM = (request.getParameter("ISBASICUOM") != null) ? "1": "0";
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
        List uomlist = itemUtil.getUomList(plant," AND ISACTIVE='Y' ");
    List nonstklst =   itemUtil.getNonStockList(plant,"");      

	sAddEnb    = "enabled";
	sItemEnb   = "enabled";
        
	
	  if(action.equalsIgnoreCase("UPDATE"))  {
		  try{
	    //sItemEnb = "disabled";
	    sAddEnb  = "disabled";
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
	        htUpdate.put("ISBASICUOM",ISBASICUOM);
	                
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
	          }
	          boolean  inserted = false;
	          if(insertAlternateItem) {
	        	  
	        	  //delete customer discount sales
	        	    boolean delete=itemdao.removeCustomerDiscountOB(plant,sItem, "");
	        	  //end delete customer discount sales
	        	  //insert customer discount sales
	        	   boolean OBCustomerDiscount=false;  
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
		    				}
		    				else{
		    					HM.put("OBDISCOUNT", request.getParameter("DYNAMIC_CUSTOMER_DISCOUNT_"+nameCount));
		    				}
		    				HM.put(IConstants.CUSTOMERTYPEID, StrUtils.fString(request.getParameter("CUSTOMER_TYPE_ID_"+nameCount)));
		    			    HM.put("CRAT",dateutils.getDateTime());
		    			    HM.put("CRBY",username);
		    			    OBCustomerDiscount = itemUtil.insertOBCustomerDiscount(HM);
		    	     		}
		        	 }
	        	   
                 //end insert customer discount sales
                    	 //delete supplier discount purchase
		        	    boolean deleteCost=itemdao.removeSupplierDiscountIB(plant,sItem, "");
		        	  //end delete customer discount sales
		        	  //insert customer discount sales
		        	   boolean IBSupplierDiscount=false;  
		        	   int DYNAMIC_SUPPLIERDISCOUNT_SIZE_INT = (new Integer(DYNAMIC_SUPPLIERDISCOUNT_SIZE)).intValue();
		        	   	for(int nameCount = 0; nameCount<=DYNAMIC_SUPPLIERDISCOUNT_SIZE_INT;nameCount++){
			        	if(StrUtils.fString(request.getParameter("DYNAMIC_SUPPLIER_DISCOUNT_"+nameCount))==""){
			        	 			break;
			           		}else{
			        			Hashtable HM = new Hashtable();
			        		   	HM.put(IConstants.PLANT, plant);
			    				HM.put(IConstants.ITEM, sItem);
			    				HM.put(IConstants.VENDNO, StrUtils.fString(request.getParameter("SUPPLIER_"+nameCount)));
			    				System.out.println("IBDISCOUNT"+IBDISCOUNT);
			    				if(IBDiscounttype.equalsIgnoreCase("BYPERCENTAGE")){
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
		        	   
	                 //end insert supplier discount purchase
	            //if(OBCustomerDiscount){
	        	  inserted = mdao.insertIntoMovHis(htm);
	            //}
	          } 
	        
	        sItemDesc=strUtils.RemoveDoubleQuotesToSingle(sItemDesc);
	        if(itemUpdated&&inserted) {
	                    res = "<font class = "+IConstants.SUCCESS_COLOR+">Product Updated Successfully</font> ";
	                   	            
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
		    sArtist   = (String)arrItem.get(2);
		    sTitle   = (String)arrItem.get(3);
		    //sMedium      = (String)arrItem.get(4);
		    sRemark      = (String)arrItem.get(5);
		    sItemCondition      = (String)arrItem.get(6);
		       //ISPARENTCHILD = (String)arrItem.get(3);
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
        movementhistoryExist = mdao.isExisit(htmh," DIRTYPE NOT IN('ADD_ITEM','UPD_ITEM','CNT_ITEM_UPLOAD')");
        
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
	  
%>

<div class="container-fluid m-t-20">
	 <div class="box"> 
            <div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title"><%=title %></h1>
		</div>
		
 <div class="box-body">


   <CENTER><strong><font style="font-size:20px;" id="msg"><%=res%></font></strong></CENTER>
<input type="number" id="numberOfDecimal" style="display:none;" value=<%=numberOfDecimal%>>
  <form class="form-horizontal" name="form" method="post">
    
    	<div class="panel panel-default">
    <div class="panel-heading" style="background: #eaeafa; height: 36px"><strong>General</strong></div>
    <div class="panel-body">
    <div class="form-group">
							
								<label class="control-label col-sm-3" for="Product ID">
									<i class="glyphicon glyphicon-star"
									style="font-size: 10px; top: -6px; color: #e50000"></i>&nbsp;Product
									ID:
								</label>
								<div class="col-sm-3">
									<div class="input-group">
										<INPUT class="form-control" name="ITEM" type="TEXT"
											value="<%=sItem%>" size="20"
											onkeypress="if((event.keyCode=='13') && ( document.form.ITEM.value.length > 0)) {getProductDetails();}"
											MAXLENGTH=50 <%=sItemEnb%>> <span
											class="input-group-addon"
											onClick="javascript:popUpWin('list/itemlistwithnonstk.jsp?ITEM='+form.ITEM.value);return false;">
											<a href="#" data-toggle="tooltip" data-placement="top"
											title="Product Details"> <i
												class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a>
										</span>
									</div>
								</div>
								<div class="col-sm-3">
									<div class="form-inline">
										<!-- <label class="control-label col-sm-3" style="padding-top: 5px; for="Falg">Flag:</label> -->

										<label class="radio-inline"> <input type="radio"
											name="ACTIVE" value="Y"
											<%if (isActive.equalsIgnoreCase("Y")) {%> checked <%}%>>Active
										</label> <label class="radio-inline"> <input type="radio"
											name="ACTIVE" value="N"
											<%if (isActive.equalsIgnoreCase("N")) {%> checked <%}%>>Non
											Active
										</label>
									</div>
								
								
			
								</div>
						
							<div class="col-sm-3">
									<div class="form-inline">
										<div class="row">
											<div class="col-sm-offset-3 col-sm-9">
												<img src="../jsp/dist/img/NO_IMG.png" id="item_img"
													name="CATALOGPATH" alt="new image"
													class="img-thumbnail img-responsive col-sm-3"
													style="width: 70%; padding: 3px;">
											</div>
										</div>
										
									</div>
								</div>
    	</div>
    	
								<div class="form-group" style="margin-bottom:5px">
										<label class="control-label col-sm-3"
											for="Product Description"> <i
											class="glyphicon glyphicon-star"
											style="font-size: 10px; top: -6px; color: #e50000"></i>&nbsp;Description:
										</label>
										<div class="col-sm-3">
											<div class="input-group">
												<input class="form-control" name="DESC" type="TEXT" value=""
													size="20" maxlength="100"> <span
													class="input-group-addon"
													onclick="javascript:popUpWin('list/itemlistwithnonstk.jsp?ITEM_DESC='+form.DESC.value);return false;">
													<a href="#" data-toggle="tooltip" data-placement="top"
													title="" data-original-title="Product Details"> <i
														class="glyphicon glyphicon-log-in"
														style="font-size: 20px;"></i></a>
												</span>
											</div>
										</div>
										<div class="col-sm-3">
										<div class="form-inline">
											<!-- <label class="control-label col-sm-3" style="padding-top: 5px; for="Stock Type">Stock Type:</label> -->
											
												<label class="radio-inline"> <input
													name="NONSTOCKFLAG" type="radio"
													onclick="DisplayNonStkType();" value="N">Stock
												</label> <label class="radio-inline"> <input
													name="NONSTOCKFLAG" type="radio"
													onclick="DisplayNonStkType();" value="Y">Non Stock
												</label>
											</div>
										</div>
										    	
							<label class="col-sm-3 text-center" for="Upload Image">Upload
								Image:</label>
							<div class="col-sm-3">
								<INPUT class="form-control btn-sm"  name="IMAGE_UPLOAD"  type="File"
									size="20" id ="productImg" MAXLENGTH=100>
						</div>
									</div>
    	<div class="form-group">
      	<label class="control-label col-sm-3" for="description">Detailed Description:</label>
      	<div class="col-sm-3">          
        <INPUT class="form-control" name="REMARKS" type="TEXT" value="<%=strUtils.forHTMLTag(sRemark)%>" size="20" MAXLENGTH=100>
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
    	<div class="form-group">
											<div class="col-sm-offset-3 col-sm-3">
												<INPUT class=" btn btn-sm btn-default" type="BUTTON" value="Remove Image" onClick="image_delete();"> 
												<INPUT class=" btn btn-sm btn-default" type="BUTTON" value="Upload & Save Image" onClick="image_edit();">
											</div>
				</div>						
    	</div>
		
    	<div class="form-group">
      	<label class="control-label col-sm-3" for="Unit Of Measure">
      	<i class="glyphicon glyphicon-star" style="font-size: 10px; top:-6px; color: #e50000"></i>&nbsp;Basic UOM:</label>      	
        <div class="col-sm-3">
      	<div class="input-group">
    	<INPUT class="form-control" name="UOM" type="TEXT" value="<%=uom%>" size="20" MAXLENGTH=100 readonly id="Basicuom">
   		<span class="input-group-addon"  onClick="javascript:popUpWin('list/uomlist_save.jsp?UOM='+form.UOM.value);return false;">
   		<a href="#" data-toggle="tooltip" data-placement="top" title="Basic UOM">
   		<i class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span>
  		</div>
  		</div>
  		<div class="form-inline">
	     <div class="col-sm-2">
	     <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name ="ISBASICUOM" id="ISBASICUOM" value="ISBASICUOM" onclick="isbasicuom();">
                     <b>Apply to all UOM</b></label>
         </div>
	     </div>      
  	  	</div>
  	  	
  	  	<div class="form-group">
      	<label class="control-label col-sm-3" for="net weight">Net Weight (KG):</label>
      	<div class="col-sm-3">         
        <INPUT class="form-control" name="NETWEIGHT" id="NETWEIGHT" type="TEXT" value="<%=NETWEIGHT%>" style="width:100%" MAXLENGTH=100>
      	</div>
    	</div>
    	
    	<div class="form-group">
      	<label class="control-label col-sm-3" for="Gross Weight">Gross Weight (KG):</label>
      	<div class="col-sm-3">          
        <INPUT class="form-control" name="GROSSWEIGHT" id="GROSSWEIGHT" type="TEXT" value="<%=GROSSWEIGHT%>" style="width:100%" MAXLENGTH=100>
      	</div>
    	</div>
    	
  	  	<div class="form-group">
      	<label class="control-label col-sm-3" for="Product Class">Product Class:</label>
      	 <div class="col-sm-3">
      	<div class="input-group">
    	<INPUT class="form-control" name="PRD_CLS_ID" type="TEXT" value="<%=prdclsid%>" size="20" MAXLENGTH=100 readonly>
   		<span class="input-group-addon"  onClick="javascript:popUpWin('list/classlist_save.jsp?prdclsid='+form.PRD_CLS_ID.value);return false;">
   		<a href="#" data-toggle="tooltip" data-placement="top" title="Product Class">
   		<i class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span>
  		</div>
  		</div> 
  		  
  	  	</div>
  	  	
  	  		<div class="form-group">
      	<label class="control-label col-sm-3" for="Product Type">Product Type:</label>
      	<div class="col-sm-3">           	
  		<div class="input-group">
    	<INPUT class="form-control" name="ARTIST" type="TEXT" value="<%=sArtist%>" size="20" MAXLENGTH=100 readonly>
   		<span class="input-group-addon"  onClick="javascript:popUpWin('list/typelist_save.jsp?sArtist='+form.ARTIST.value);return false;">
   		<a href="#" data-toggle="tooltip" data-placement="top" title="Product Type">
   		<i class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span>
  		</div>
  	    </div>
  	    
  	    </div>
 
 		<div class="form-group">
      	<label class="control-label col-sm-3" for="Product Brand">Product Brand:</label>
      	<div class="col-sm-3">           	
  		<div class="input-group">
    	<INPUT class="form-control" name="PRD_BRAND" type="TEXT" value="<%=prdBrand%>" size="20" MAXLENGTH=100 readonly>
   		<span class="input-group-addon"  onClick="javascript:popUpWin('list/brandlist_save.jsp?prdBrand='+form.PRD_BRAND.value);return false;">
   		<a href="#" data-toggle="tooltip" data-placement="top" title="Product Brand">
   		<i class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span>
  		</div>
  		</div>
  		
  	  	</div>
  	  	
		<INPUT type="hidden" id="TaxByLabelOrderManagement" name="taxbylabelordermanagement" value=<%=taxbylabelordermanagement%>>
		
  	  	<div class="form-group">
      	<label class="control-label col-sm-3" for="Product VAT" id="TaxLabelOrderManagement"></label>
      	<div class="col-sm-3">          
        <INPUT class="form-control" name="PRODGST" id="PRODGST" type="TEXT" value="<%=PRODGST%>"
		size="20" MAXLENGTH=50>
		</div>
		
      	</div>  	
      	
      	<div class="form-group">
      	<label class="control-label col-sm-3" for="HS Code">HS Code:</label>
      	<div class="col-sm-3">           	
  		<div class="input-group">
    	<INPUT class="form-control" name="HSCODE" type="TEXT" value="<%=strUtils.forHTMLTag(HSCODE)%>" size="20" MAXLENGTH=100 readonly>
   		<span class="input-group-addon" onClick="javascript:popUpWin('list/hscodelist_save.jsp?hscode='+form.HSCODE.value);return false;">
   		<a href="#" data-toggle="tooltip" data-placement="top" title="HS Code">
   		<i class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span>
  		</div>
  		</div>
  	  	</div> 
  	  	
  	  	<div class="form-group">
      	<label class="control-label col-sm-3" for="COO">COO:</label>
      	<div class="col-sm-3">           	
  		<div class="input-group">
    	<INPUT class="form-control" name="COO" type="TEXT" value="<%=strUtils.forHTMLTag(COO)%>" size="20" MAXLENGTH=100 readonly>
   		<span class="input-group-addon" onClick="javascript:popUpWin('list/coolist_save.jsp?coo='+form.COO.value);return false;">
   		<a href="#" data-toggle="tooltip" data-placement="top" title="COO">
   		<i class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span>
  		</div>
  		</div>
  	  	</div>
  	  	
  	  	<div class="form-group">
      	<label class="control-label col-sm-3" for="VINNO">VIN No.:</label>
      	<div class="col-sm-3">          
        <INPUT class="form-control" name="VINNO" type="TEXT" value="<%=strUtils.forHTMLTag(VINNO)%>" style="width:100%" MAXLENGTH=50>
      	</div>
    	</div> 
    	
    	<div class="form-group">
      	<label class="control-label col-sm-3" for="MODEL">Model:</label>
      	<div class="col-sm-3">          
        <INPUT class="form-control" name="MODEL" type="TEXT" value="<%=strUtils.forHTMLTag(MODEL)%>" style="width:100%" MAXLENGTH=50>
      	</div>
    	</div>  
    	
    	<div class="form-group">
      	<label class="control-label col-sm-3" for="Remarks">Remarks1:</label>
      	<div class="col-sm-3">          
       	<INPUT class="form-control" name="ITEM_CONDITION" type="TEXT" value="<%=sItemCondition%>" size="20" MAXLENGTH=100>
      	</div>
    	</div>
     
     	<div class="form-group">
      	<label class="control-label col-sm-3" for="Remarks">Remarks2:</label>
      	<div class="col-sm-3">          
        <INPUT class="form-control" name="TITLE" type="TEXT" value="<%=sTitle%>" size="20" MAXLENGTH=200>
      	</div>
    	</div>
    	
    	 <div class="form-group">
      	<label class="control-label col-sm-3" for="Remarks">Alternate Product:</label>
        <div class="col-sm-3"> 
        <TABLE id="alternateItemDynamicTable">
		<TR>
		<TD> <INPUT class="form-control" name="DYNAMIC_ALTERNATE_NAME_1" type="TEXT" value="" size="50" MAXLENGTH="50"  /></TD>
		</TR>
		</TABLE>
      	</div>
      	<div class="form-inline">
    	<div class="col-sm-2">
    	<a href="#" onclick="addRow('alternateItemDynamicTable','');" data-toggle="tooltip" data-placement="top" Title="Add New Alternate Product">
        <i class="glyphicon glyphicon-plus" style="font-size: 20px; top:6px;"></i></a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
        <a href="#" onclick="deleteRow('alternateItemDynamicTable');" data-toggle="tooltip" data-placement="top" Title="Remove Last Alternate Product">
        <i class="glyphicon glyphicon-trash" style="font-size: 20px; top:6px;"></i></a>
        <INPUT	type="hidden" name="DYNAMIC_ALTERNATE_SIZE">
		<INPUT	type="hidden" name="FLAGSUCCESS" value="NOTSUBMIT">
    	</div>
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
      	<div class="input-group">
    	<INPUT class="form-control" name="PURCHASEUOM" type="TEXT" value="<%=PURCHASEUOM%>" size="20" MAXLENGTH=100 readonly id="purchaseuom">
   		<span class="input-group-addon" onClick="UOMpopUpwin('PURCHASEUOM');return false;">
   		<a href="#" data-toggle="tooltip" data-placement="top" title="Purchase UOM Details">
   		<i class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span>
  		</div>
  		</div>     
  	  	</div>
  	  	
  	  	<div class="form-group">
      	<label class="control-label col-sm-3" for="Cost">Cost:</label>
      	<div class="col-sm-3">          
        <INPUT class="form-control" name="COST" id="COST" type="TEXT" value="<%=StrUtils.addZeroes(Double.parseDouble(cost), numberOfDecimal)%>" size="20"
		MAXLENGTH=50 onchange="validDecimal(this.value)">
      	</div>
    	</div>
    	
    	<INPUT type="hidden" name="DYNAMIC_SUPPLIERDISCOUNT_SIZE">

 		<div class="form-group">
      	<label class="control-label col-sm-3" for="Remarks">Supplier Discount Purchase Order:</label>
    	<div class="col-sm-3">
    	<label class="radio-inline">
      	<INPUT name="IBDISCOUNT" type = "radio"  value="BYCOST"  id="BYCOST" <%if(IBDiscounttype.equalsIgnoreCase("BYCOST")) {%>  checked="checked" <%}%>  >By Cost
    	</label>
    	<label class="radio-inline">
      	<INPUT  name="IBDISCOUNT" type = "radio" value="BYPERCENTAGE"  id = "BYPERCENTAGE" <%if(IBDiscounttype.equalsIgnoreCase("BYPERCENTAGE")) {%>  checked="checked" <%}%> >By Percentage
    	</label>
     	</div>
		</div>

		<div class="form-group">
		<label class="control-label col-sm-3" for="Supplier Discount Purchase Order"></label>
	 	<!-- <div class="col-sm-3">&nbsp;</div> -->
	 	<div class="col-sm-3">
    	<TABLE id="supplierDiscount">
		<TR>
		<TD><INPUT class="form-control" name="DYNAMIC_SUPPLIER_DISCOUNT_0" id="DYNAMIC_SUPPLIER_DISCOUNT_0" type="TEXT" value=""
		size="20" MAXLENGTH="50"  />&nbsp;</TD>
		<TD align="center"><div class="input-group"><INPUT class="form-control" name="SUPPLIER_0" id="SUPPLIER_0"  type = "TEXT" value="<%=SUPPLIER%>" size="20"  MAXLENGTH=50
		onkeypress="if((event.keyCode=='13') && ( document.form.SUPPLIER_0.value.length > 0)){validateSupplier(0);}">
        <span class="input-group-addon"><a href="#" data-toggle="tooltip" data-placement="top" Title="Supplier Details" 
        onClick="javascript:popUpWin('list/multiSupplierList.jsp?SUPPLIER='+form.SUPPLIER_0.value+'&INDEX=0');return false;">
        <i class="glyphicon glyphicon-log-in"  style="font-size: 20px;"></i></a></span>
        </div>&nbsp;
        </TD>
        </TR>
		</TABLE>
    	</div>
    	<div class="form-inline">
    	<div class="col-sm-2">
    	<a href="#" onclick="addRowFormatCost('supplierDiscount','','');return false;" data-toggle="tooltip" data-placement="top" Title="Add New Supplier Discount">
        <i class="glyphicon glyphicon-plus" style="font-size: 20px; top:6px;"></i></a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
        <a href="#" onclick="deleteRowCost('supplierDiscount');return false;" data-toggle="tooltip" data-placement="top" Title="Remove Last Supplier Discount">
        <i class="glyphicon glyphicon-trash" style="font-size: 20px; top:6px;"></i></a>
        <INPUT type="hidden" name="DYNAMIC_SUPPLIER_DISCOUNT_SIZE">
    	</div>
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
      	<div class="input-group">
    	<INPUT class="form-control" name="SALESUOM" type="TEXT" value="<%=SALESUOM%>" size="20" MAXLENGTH=100 readonly id="salesuom">
   		<span class="input-group-addon"  onClick="UOMpopUpwin('SALESUOM');return false;">
   		<a href="#" data-toggle="tooltip" data-placement="top" title="Sales UOM Details">
   		<i class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span>
  		</div>
  		</div>
  	  	</div>
  	  	
  	  	<div class="form-group">
      	<label class="control-label col-sm-3" for="List Price">List Price:</label>
      	<div class="col-sm-3">          
      	<INPUT class="form-control" name="PRICE" id="PRICE" type="TEXT" value="<%=StrUtils.addZeroes(Double.parseDouble(price), numberOfDecimal)%>" size="20"
	  	MAXLENGTH=50 onchange="javascript:enableSellingPrice(this.value);">
      	</div>
      	</div>
    
    	<div class="form-group">
      	<label class="control-label col-sm-3" for="Min Selling Price ">Minimum Selling Price:</label>
      	<div class="col-sm-3">          
        <INPUT  class="form-control" name="MINSELLINGPRICE" id="MINSELLINGPRICE" type="TEXT" value="<%=StrUtils.addZeroes(Double.parseDouble(minsprice), numberOfDecimal)%>" size="20"
		MAXLENGTH=50 onchange="validDecimal(this.value)">
      	</div>
    	</div>
    	
    	<!-- <div class="form-group">
      	<label class="control-label col-sm-3" for="Discount POS(%)">Discount POS(%):</label>
      	<div class="col-sm-3"> -->          
       	<INPUT class="form-control" name="DISCOUNT" id="DISCOUNT" type="hidden" value="<%=discount%>"
		size="20" MAXLENGTH=50>
      	<!-- </div>
    	</div> -->
    	
    	<INPUT type="hidden" name="DYNAMIC_CUSTOMERDISCOUNT_SIZE">
     
     	<div class="form-group">
      	<label class="control-label col-sm-3" for="Customer Discount OB">Customer Discount Sales Order:</label>
    	<div class="col-sm-3">
    	<label class="radio-inline">
     	<INPUT name="OBDISCOUNT" type = "radio"  value="BYPRICE"  id="BYPRICE" <%if(OBDiscounttype.equalsIgnoreCase("BYPRICE")) {%>  checked="checked" <%}%>  >By Price
    	</label>
    	<label class="radio-inline">
      	<INPUT  name="OBDISCOUNT" type = "radio" value="BYPERCENTAGE"  id = "BYPERCENTAGE" <%if(OBDiscounttype.equalsIgnoreCase("BYPERCENTAGE")) {%>  checked="checked" <%}%> >By Percentage
    	</label>
     	</div>
		</div>

		<div class="form-group">
		<label class="control-label col-sm-3" for="Customer Discount Purchase Order"></label>
	 	<!-- <div class="col-sm-3">&nbsp;</div> -->
	 	<div class="col-sm-3">
		<TABLE id="customerDiscount">
		<TR>
		<TD><INPUT class="form-control" name="DYNAMIC_CUSTOMER_DISCOUNT_0" id="DYNAMIC_CUSTOMER_DISCOUNT_0" type="TEXT" value=""
		size="20" MAXLENGTH="50"  />&nbsp;</TD>
		<TD align="center"><div class="input-group"><INPUT class="form-control" name="CUSTOMER_TYPE_ID_0" id="CUSTOMER_TYPE_ID_0"  type = "TEXT" value="<%=CUSTOMER_TYPE_ID%>" size="20"  MAXLENGTH=50
		onkeypress="if((event.keyCode=='13') && ( document.form.CUSTOMER_TYPE_ID_0.value.length > 0)){validateCustomerType(0);}">
        <span class="input-group-addon"><a href="#" data-toggle="tooltip" data-placement="top" Title="Customer Details" 
        onClick="javascript:popUpWin('list/multiCustomerTypeList.jsp?CUSTOMERTYPE='+form.CUSTOMER_TYPE_ID_0.value+'&INDEX=0');return false;">
        <i class="glyphicon glyphicon-log-in"  style="font-size: 20px;"></i></a></span> 
        </div>&nbsp;
        </TD>
        </TR>
		</TABLE>
    	</div>
    	<div class="form-inline">
    	<div class="col-sm-2">
    	<a href="#" onclick="addRowFormatCustomerType('customerDiscount','','');return false;" data-toggle="tooltip" data-placement="top" Title="Add New Customer Discount">
        <i class="glyphicon glyphicon-plus" style="font-size: 20px; top:6px;"></i></a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
        <a href="#" onclick="deleteRow('customerDiscount');return false;" data-toggle="tooltip" data-placement="top" Title="Remove Last Customer Discount">
        <i class="glyphicon glyphicon-trash" style="font-size: 20px; top:6px;"></i></a>
        <INPUT type="hidden" name="DYNAMIC_CUSTOMER_DISCOUNT_SIZE">
    	</div>
    	</div>
    	</div>
    </div>
    </div>
    
    
    <!-- <div class="row"> --><!-- This div is used to seperate panel side by side 
    <div class="col-sm-6">  -->
    
    <div class="panel panel-default">
    <div class="panel-heading" style="background: #eaeafa; height: 36px"><strong>Rental</strong></div>
    <div class="panel-body">
    <div class="form-group">
      	<label class="control-label col-sm-3" for="Rental UOM">Rental UOM:</label>      	
        <div class="col-sm-3">
      	<div class="input-group">
    	<INPUT class="form-control" name="RENTALUOM" type="TEXT" value="<%=RENTALUOM%>" size="20" MAXLENGTH=100 readonly id="rentaluom">
   		<span class="input-group-addon"  onClick="UOMpopUpwin('RENTALUOM');return false;">
   		<a href="#" data-toggle="tooltip" data-placement="top" title="Rental UOM Details">
   		<i class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span>
  		</div>
  		</div>     
  	  	</div>
  	  	
  	  	<div class="form-group">
      	<label class="control-label col-sm-3" for="Rental Price">Rental Price:</label>
      	<div class="col-sm-3">          
        <INPUT class="form-control" name="RENTALPRICE" id="RENTALPRICE" type="TEXT" value="<%=StrUtils.addZeroes(Double.parseDouble(RENTALPRICE), numberOfDecimal)%>" size="20"
		MAXLENGTH=50 onchange="validDecimal(this.value)">
      	</div>
    	</div>
    </div>
    </div>
    <!-- </div> -->
   <!--  <div class="col-sm-6">
   
    <div class="panel panel-default">
    <div class="panel-heading" style="background: #eaeafa; height: 36px"><strong>Service</strong></div>
    <div class="panel-body">
    <div class="form-group">
      	<label class="control-label col-sm-6" for="Service UOM">Service UOM:</label>      	
        <div class="col-sm-6">
      	<div class="input-group">  -->
    	<INPUT class="form-control" name="SERVICEUOM" type="hidden" value="<%=SERVICEUOM%>" size="20" MAXLENGTH=100 readonly id="serviceuom">
   		<!-- <span class="input-group-addon"  onClick="UOMpopUpwin('SERVICEUOM');return false;">
   		<a href="#" data-toggle="tooltip" data-placement="top" title="Service UOM Details">
   		<i class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span>
  		</div>
  		</div>     
  	  	</div> -->
  	  	
  	  	<!-- <div class="form-group">
      	<label class="control-label col-sm-6" for="Service Price">Service Price:</label>
      	<div class="col-sm-6"> -->          
        <INPUT class="form-control" name="SERVICEPRICE" id="SERVICEPRICE" type="hidden" value="<%=StrUtils.addZeroes(Double.parseDouble(SERVICEPRICE), numberOfDecimal)%>" size="20"
		MAXLENGTH=50 onchange="validDecimal(this.value)">
      	<!-- </div>
    	</div>
    </div> 
    </div> 
  	  </div>
  	 </div>	-->
   
  	  <div class="panel panel-default">
    <div class="panel-heading" style="background: #eaeafa; height: 36px"><strong>Inventory</strong></div>
    <div class="panel-body">
    <div class="form-group">
      	<label class="control-label col-sm-3" for="Inventory UOM">Inventory UOM:</label>      	
        <div class="col-sm-3">
      	<div class="input-group">
    	<INPUT class="form-control" name="INVENTORYUOM" type="TEXT" value="<%=INVENTORYUOM%>" size="20" MAXLENGTH=100 readonly id="inventoryuom">
   		<span class="input-group-addon"  onClick="UOMpopUpwin('INVENTORYUOM');return false;">
   		<a href="#" data-toggle="tooltip" data-placement="top" title="Inventory UOM Details">
   		<i class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span>
  		</div>
  		</div>     
  	  	</div>
  	  	
  	  	
    <div class="form-group">
      	<label class="control-label col-sm-3" for="Min Stock Qty">Min Stock Quantity:</label>
      	<div class="col-sm-3">          
        <INPUT class="form-control" name="STKQTY" type="TEXT" value="<%=stkqty%>" size="20" MAXLENGTH=50>
      	</div>
    	</div>
    
    	<div class="form-group">
      	<label class="control-label col-sm-3" for="Max Stock Qty">Max Stock Quantity:</label>
      	<div class="col-sm-3">          
       	<INPUT class="form-control" name="MAXSTKQTY" type="TEXT" value="<%=maxstkqty%>"	size="20" MAXLENGTH=50>
      	</div>
    	</div>
       
    	<INPUT type="hidden" 	name="LOC_0" id="LOC_0" >
    </div>
    </div>
    
    	<div class="form-group">        
      	<div class="col-sm-12" align="center">
      	<button type="button" class="Submit btn btn-default" onClick="window.location.href='../home'"><b>Back</b></button>&nbsp;&nbsp;
      	<!-- <button type="button" class="Submit btn btn-default" onClick="{backNavigation('settings.jsp','SM');}"><b>Back</b></button>&nbsp;&nbsp; -->
      	<button type="button" class="Submit btn btn-default" onClick="onClear();" sNewEnb><b>Clear</b></button>&nbsp;&nbsp;
      	<button type="button" class="Submit btn btn-default" data-toggle="modal" data-target="#myModal" onClick="onUpdate();sNewEnb"><b>Save</b></button>&nbsp;&nbsp;
      	<button type="reset" class="Submit btn btn-default" onClick="return onDelete();" sNewEnb><b>Delete</b></button>&nbsp;&nbsp;
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
		<TH width="20%"><font color="#ffffff">SNO</TH>
		<TH width="40%"><font color="#ffffff">Mapping Item</TH>
		<TH width="40%"><font color="#ffffff">DELETE</TH>
	</TR>
	<%
       List listMapItem = itemUtil.getMapItems4KeyItem(sItem);
       if(listMapItem.size()>0) {
       for(int i = 0; i<listMapItem.size(); i++){
       int iIndex = i+1;
       String sBGColor = ((i == 0) || (i % 2 == 0)) ? "#FFFFFF" : "#FFFFFF";

       %>
	<TR bgcolor="<%=sBGColor%>">
		<TD width="20%"><%=iIndex%></TD>
		<TD width="40%"><%=(String)listMapItem.get(i)%></TD>
		<TD width="40%" align="center"><a
			href="item_view.jsp?action=ITEMMAPDELETE&keyitem=<%=sItem%>&mapitem=<%=(String)listMapItem.get(i)%>&DESC=<%=strUtils.replaceCharacters2Send(sItemDesc)%>&UOM=<%=sItemUom%>&REORDQTY=<%=sReOrdQty%>">Delete</a></TD>
	</TR>
	<%} }else {%>
	<Th colspan=2><font class="<%=IConstants.FAILED_COLOR%>">No
	Mapping Item available</font></Th>
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
	Item Location available</font></Th>
	<%} %>
</TABLE>

<%}%>

  
<script>
$(document).ready(function(){
    $('[data-toggle="tooltip"]').tooltip();   
	
	var  d = document.getElementById("TaxByLabelOrderManagement").value;
    document.getElementById('TaxLabelOrderManagement').innerHTML = "Product " + d +" (%):";
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
                                                   document.form.DISCOUNT.value=resultVal.discount;
                                                   document.form.LOC_0.value=resultVal.loc;
                                                   document.form.NONSTKTYPE.value=resultVal.nonstktypeid;
                                                   document.form.MAXSTKQTY.value=resultVal.maxstkqty;
                                                   document.form.HSCODE.value=resultVal.hscode;
                                                   document.form.COO.value=resultVal.coo;
                                                   document.form.VINNO.value=resultVal.vinno;
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
                                                   if(resultVal.ISBASICUOM == 1){
                                                	   document.form.ISBASICUOM.checked = true;
                                                   }else if(resultVal.ISBASICUOM == 0){
                                                	   document.form.ISBASICUOM.checked = false;
                                                   }
                                                   setCheckedValue( document.form.ACTIVE,resultVal.isActive);
                                                   //setCheckedValue( document.form.rdoparentchild,resultVal.ISPARENTCHILD);
                                                   setCheckedValue( document.form.NONSTOCKFLAG,resultVal.nonstkflg);
												   DisplayNonStkType();
                                                   loadAlternateItemNames(productId);
                                                   loadCustomerDiscount(resultVal.sItem);
                                                   loadSupplierDiscount(resultVal.sItem);
                                                   
                                                 if(resultVal.catalogpath=="")
                                                	 $("#item_img").attr("src","../jsp/dist/img/NO_IMG.png");
                                                 else	 
                                           			 $("#item_img").attr("src","/track/ReadFileServlet/?fileLocation="+resultVal.catalogpath);
                         
					}
				}
			});
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

		var secondCell = row.insertCell(0);
		var inputTextArea = document.createElement("input");
		var newRowCount = rowCount + 1;
		inputTextArea.name = "DYNAMIC_ALTERNATE_NAME_" + newRowCount;
		inputTextArea.className = "form-control";
		inputTextArea.size = "50";
		inputTextArea.type = "text";
		
		inputTextArea.value = textValue;
		// secondCell.appendChild(document.createElement("br"));
		secondCell.appendChild(inputTextArea);
	}

	function deleteRow(tableID) {
		try {
			var table = document.getElementById(tableID);
			var rowCount = table.rows.length;
			rowCount = rowCount * 1 - 1;
			if (rowCount == 0) {
				alert("Can not remove the default Item");
			} else {
				table.deleteRow(rowCount);
			}
		} catch (e) {
			alert(e);
		}
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
		
		var itemCell = row.insertCell(0);
		var itemCellText =  "<INPUT name=\"DYNAMIC_CUSTOMER_DISCOUNT_"+rowCount+"\" ";
		itemCellText = itemCellText+ " id=\"DYNAMIC_CUSTOMER_DISCOUNT_"+rowCount+"\" value=\""+obDiscount+"\" type = \"TEXT\" size=\"20\" MAXLENGTH=\"50\" class=\"form-control\">&nbsp;";
		itemCell.innerHTML = itemCellText;
				
		var itemCell = row.insertCell(1);
		var itemCellText =  "<div class=\"input-group\">  <INPUT class=\"form-control\" name=\"CUSTOMER_TYPE_ID_"+rowCount+"\" ";
		itemCellText = itemCellText+ " id=\"CUSTOMER_TYPE_ID_"+rowCount+"\" type = \"TEXT\" size=\"20\"   value=\""+customerType+"\"  onkeypress=\"if((event.keyCode=='13') && ( this.value.length > 0)){validateCustomerType("+rowCount+");}\" MAXLENGTH=\"50\">";
		itemCellText = itemCellText+ "<span class=\"input-group-addon\"><a href=\"#\" onClick=\"javascript:popUpWin('list/multiCustomerTypeList.jsp?CUSTOMERTYPE='+form.CUSTOMER_TYPE_ID"+'_'+rowCount+".value+'&INDEX="+rowCount+"');return false;\"><i class=\"glyphicon glyphicon-log-in\" style=\"font-size: 20px;\"></i></a></span></div>&nbsp;";
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

  
	/*function addRowFormatCost(tableID) {
		
		var table = document.getElementById(tableID);
		var rowCount = table.rows.length;
		var row = table.insertRow(rowCount);
		 var form = document.forms['form'];

		var itemCell = row.insertCell(0);
		var itemCellText =  "<INPUT name=\"DYNAMIC_SUPPLIER_DISCOUNT_"+rowCount+"\" ";
		itemCellText = itemCellText+ " id=\"DYNAMIC_SUPPLIER_DISCOUNT_"+rowCount+"\" type = \"TEXT\" size=\"20\" MAXLENGTH=\"50\">";
		itemCell.innerHTML = itemCellText;
		
		var itemCell = row.insertCell(1);
		var itemCellText =  "<INPUT name=\"SUPPLIER_"+rowCount+"\" ";
		itemCellText = itemCellText+ " id=\"SUPPLIER_"+rowCount+"\" type = \"TEXT\" size=\"20\"    onkeypress=\"if((event.keyCode=='13') && ( this.value.length > 0)){validateSupplier("+rowCount+");}\" MAXLENGTH=\"50\">";
		itemCellText = itemCellText+ "<a href=\"#\" onClick=\"javascript:popUpWin('list/multiSupplierList.jsp?SUPPLIER='+form.SUPPLIER"+'_'+rowCount+".value+'&INDEX="+rowCount+"');\"><img src=\"images/populate.gif\" border=\"0\"></a>";
		itemCell.innerHTML = itemCellText;
		
	}*/

	function addRowFormatCost(tableID,ibDiscount,vendNo) {
		var table = document.getElementById(tableID);
		var rowCount = table.rows.length;
		var row = table.insertRow(rowCount);
		
		var itemCell = row.insertCell(0);
		var itemCellText =  "<INPUT name=\"DYNAMIC_SUPPLIER_DISCOUNT_"+rowCount+"\" ";
		itemCellText = itemCellText+"  id=\"DYNAMIC_SUPPLIER_DISCOUNT_"+rowCount+"\" value=\""+ibDiscount+"\" type = \"TEXT\" size=\"20\" MAXLENGTH=\"50\" class=\"form-control\">&nbsp;";
		itemCell.innerHTML = itemCellText;
				
		var itemCell = row.insertCell(1);
		var itemCellText =  "<div class=\"input-group\">  <INPUT class=\"form-control\" name=\"SUPPLIER_"+rowCount+"\" ";
		itemCellText = itemCellText+ " id=\"SUPPLIER_"+rowCount+"\" type = \"TEXT\" size=\"20\"   value=\""+vendNo+"\"  onkeypress=\"if((event.keyCode=='13') && ( this.value.length > 0)){validateSupplier("+rowCount+");}\" MAXLENGTH=\"50\">";
		itemCellText = itemCellText+ "<span class=\"input-group-addon\"><a href=\"#\" onClick=\"javascript:popUpWin('list/multiSupplierList.jsp?SUPPLIER='+form.SUPPLIER"+'_'+rowCount+".value+'&INDEX="+rowCount+"');return false;\"><i class=\"glyphicon glyphicon-log-in\" style=\"font-size: 20px;\"></i></a></span></div>&nbsp;";
		itemCell.innerHTML = itemCellText;
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
			function readURL(input) {
			    if (input.files && input.files[0]) {
			        var reader = new FileReader();

			        reader.onload = function (e) {
			        	
			        	
			            $('#item_img').attr('src', e.target.result);
			        }

			        reader.readAsDataURL(input.files[0]);
			    }
			}
			$(document).on('change', '#productImg', function (e) {
			    readURL(this);
			});
</script>
 </form>
  </div>
 </div>
  </div>
  
  
<jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
</jsp:include>