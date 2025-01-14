<%@ page import="java.util.*"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.util.*"%>
<%@ page import="com.track.db.util.ItemUtil"%>
<%@ page import="com.track.db.util.InvUtil"%>
<%@ page import="com.track.dao.MovHisDAO"%>
<%@ page import="com.track.dao.TblControlDAO"%>
<%@ page import="com.track.constants.*"%>
<%@ page import="javax.transaction.UserTransaction"%>
<%@ page import="java.util.Set"%>
<%@page import="com.track.db.util.PrdTypeUtil"%>
<%@page import="com.track.db.util.PrdClassUtil"%>
<%@page import="com.track.gates.DbBean"%>
<html>
<head>
<title>Product List</title>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
<link rel="stylesheet" href="css/style.css">

<link href="https://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/css/bootstrap.min.css" rel="stylesheet">   
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.7.1/jquery.min.js"></script>
<link rel="stylesheet" href="https://cdn.datatables.net/1.10.2/css/jquery.dataTables.min.css">
<script type="text/javascript" src="https://cdn.datatables.net/1.10.2/js/jquery.dataTables.min.js"></script>
<script type="text/javascript" src="https://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/js/bootstrap.min.js"></script>

</head>

<body>

<script language="JavaScript" type="text/javascript" src="js/general.js"></script>
<script language="JavaScript" type="text/javascript" src="js/json2.js"></script>

<SCRIPT LANGUAGE="JavaScript">
     window.onload = function(){
     //document.getElementById("STKQTY").value = "0.000";
   	//document.getElementById("MAXSTKQTY").value = "0.000";
	  document.getElementById("PRICE").value = "0.00000";
	  document.getElementById("COST").value = "0.00000";
	  document.getElementById("MINSELLINGPRICE").value = "0.00000";
	  document.getElementById("DISCOUNT").value = "0.0";
	  document.getElementById("PRODGST").value = "0.000";
	  document.getElementById("NETWEIGHT").value = "0.000";
	  document.getElementById("GROSSWEIGHT").value = "0.000";
	 
	}
	
	
	var subWin = null;
	function popUpWin(URL) {
		
		subWin = window.open(URL, '_blank', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=800,height=400,left = 200,top = 184');
	}
	function onNew(){
	   var prdcls = document.form.PRD_CLS_ID.value;
	   document.form.action  = "item_list_do.jsp?action=NEW&PRD_CLS_ID="+prdcls;
	   document.form.submit();
	}
	function onClear()
	{
		document.form.ITEM2.value="";
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
		document.form.PRD_CLS_ID.value="";
		document.form.ARTIST.value="";
		document.form.PRD_BRAND.value="";
	}
	function onCheck()
	{
	 var ITEM   = document.form.ITEM2.value;/*  */
	 var DESC = document.form.DESC.value;
	 var uom = document.form.UOM.value;
	 var aRTIST = document.form.ARTIST.value;
	if(ITEM == "" || ITEM == null)
	{
		alert("Please key in Product");
		document.form.ITEM2.focus();/*  */ 
		return false;
	}
	else if(DESC=="" || DESC == null)
	{
		alert("Please key in Description");
	}
	else if(aRTIST=="" || uom == aRTIST)
	//else if(document.form.ARTIST.selectedIndex==0)
	{
		alert("Please key in Product Type");
	}
	else if(uom=="" || uom == null)
	//else if(document.form.UOM.selectedIndex==0)
	{
		alert("Please key in UOM");
	}
	
		
	else
	{
		return true;
	}

	
	}
	
	function onAdd(){
	 var ITEM   = document.form.ITEM2.value;/*  */
	 var DESC = document.form.DESC.value;
	 var uom = document.form.UOM.value;
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
	 var minstkqty   = document.form.STKQTY.value;
	 var maxstkqty   = document.form.MAXSTKQTY.value;
	 
	 if(minstkqty=="")
		 {
		 minstkqty=0;
		 }
	 if(maxstkqty=="")
		 {
		 maxstkqty=0;
		 }
	 
	 if(ITEM == "" || ITEM == null)
	{
		alert("Please key in Product");
		document.form.ITEM2.focus(); return false; /*  */
	}
	else if(DESC == "" || DESC == null)
	 {
		alert("Please key in Description");
		document.form.DESC.focus(); return false;
	 }
	else if(uom=="" || uom == null)
	{
		alert("Please key in UOM");
		return false;
	}
	else if(!jQuery.isNumeric(document.form.NETWEIGHT.value))
	  	{
	  		alert("Please Enter Valid NetWeight");
	  		form.NETWEIGHT.focus();
	  		return false;
	  	}
		else if(!jQuery.isNumeric(document.form.GROSSWEIGHT.value))
	  	{
	  		alert("Please Enter Valid Grossweight");
	  		form.GROSSWEIGHT.focus();
	  		return false;
	  	}
		else if(!jQuery.isNumeric(minstkqty))
	  	{
	  		alert("Please Enter Valid Min Stock Quantity");
	  		form.STKQTY.focus();
	  		return false;
	  	}
		else if(!jQuery.isNumeric(maxstkqty))
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
	
	else if(!jQuery.isNumeric(document.form.PRICE.value))
	{
		alert("Please Enter Valid Price");
		form.PRICE.focus();
		return false;
	}
	
	/*else if(document.form.PRICE.value < document.form.MINSELLINGPRICE.value)*/
	
	else if(document.form.COST.value == ""){
	    alert("Please enter Cost");
	    document.form.COST.focus();
	    return false;
	  }
	else if(!jQuery.isNumeric(document.form.COST.value))
	{
		alert("Please Enter Valid Cost");
		form.COST.focus();
		return false;
	}
	else if(!jQuery.isNumeric(document.form.MINSELLINGPRICE.value))
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
	else if(!jQuery.isNumeric(document.form.PRODGST.value))
  	{
  		alert("Please Enter Valid Product VAT");
  		form.PRODGST.focus();
  		return false;
  	}if (!validToThreeDecimal(document.getElementById("NETWEIGHT").value)) {
		alert("Not more than 3 digits are allowed after decimal value in Net Weight");
		form.NETWEIGHT.focus();
		return false;
	} else if (!validToThreeDecimal(document.getElementById("GROSSWEIGHT").value)) {
		alert("Not more than 3 digits are allowed after decimal value in Gross Weight");
		form.GROSSWEIGHT.focus();
		return false;
	} 
	else if (!validDecimal(document.getElementById("PRICE").value)) {
			alert("Not more than 5 digits are allowed after decimal value in List Price");
			document.form.PRICE.focus();
			return false;
		} else if (!validDecimal(document.getElementById("COST").value)) {
			alert("Not more than 5 digits are allowed after decimal value in Cost");
			document.form.COST.focus();
			return false;
		} else if (!validDecimal(document.getElementById("MINSELLINGPRICE").value)) {
			alert("Not more than 5 digits are allowed after decimal value in Minimum Selling Price");
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

		else {
			var val = 0;
			var table = document.getElementById("customerDiscount");
			if(table)
			{
			var rowCount = table.rows.length;
			var discounttype = document.form.OBDISCOUNT.value;
			for (var r = 0, n = table.rows.length; r < n; r++) {
				for (var c = 0, m = table.rows[r].cells.length; c < m; c++) {
					var discount = parseFloat(document
							.getElementById("DYNAMIC_CUSTOMER_DISCOUNT_" + r).value);

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
					if (!jQuery.isNumeric(document
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
					if (!jQuery.isNumeric(document
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
			document.form.DYNAMIC_CUSTOMERDISCOUNT_SIZE.value = document
			.getElementById("customerDiscount").rows.length;
	document.form.DYNAMIC_SUPPLIERDISCOUNT_SIZE.value = document
			.getElementById("supplierDiscount").rows.length;
			}
			// to validate supplier discount sales  end  
			var priceamt = document.getElementById('PRICE').value;
			var mpriceamt = document.getElementById('MINSELLINGPRICE').value;
			var costamt = document.getElementById('COST').value;
			if (priceamt.indexOf('.') == -1)
				priceamt += ".";
			var decNum = priceamt.substring(priceamt.indexOf('.') + 1,
					priceamt.length);
			
			document.form.action = "item_list_do.jsp?action=ADD";
			document.form.submit();
		}
	
	}
	function onViewMapItem() {
		var ITEM = document.form.ITEM.value;
		if (ITEM == "" || ITEM == null) {
			alert("Please Choose Item Code");
			document.form.ITEM.focus();
			return false;
		}
		document.form.action = "item_list_do.jsp?action=VIEWMAPITEM";
		document.form.submit();
	}
	
	function validDecimal(str) {
		if (str.indexOf('.') == -1)
			str += ".";
		var decNum = str.substring(str.indexOf('.') + 1, str.length);
		if (decNum.length > 5) {
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
	String sItem      = "",sItemDesc  = "", nonstk="",desc="", sItemUom   = "", sReOrdQty  = "",prdclassid="" ,prdBrand = "";
	String action     = "",sRemark1   = "" , sRemark2  = "", sRemark3   = "",prd_cls_id="",uom="";
	String sItemCondition = "",sArtist="", prdclsid ="",minsprice="",cost="",discount="",sTitle="",sMedium="",price="",sRemark="",sUOM="",stkqty="",maxstkqty="";
	String DYNAMIC_ALTERNATE_SIZE = "",ISPARENTCHILD="",nonstkid="",nonstkflag="",nonstocktype="",loc="",CUSTOMER_TYPE_ID="", DYNAMIC_CUSTOMERDISCOUNT_SIZE="",OBDISCOUNT="",
			SUPPLIER="",DYNAMIC_SUPPLIERDISCOUNT_SIZE="",IBDISCOUNT="",PRODGST="",NETWEIGHT="",GROSSWEIGHT="",HSCODE="",COO="";
	StrUtils strUtils = new StrUtils();
	ItemUtil itemUtil = new ItemUtil();
	DateUtils dateutils = new DateUtils();
	/*  */
	MLogger mLogger = new MLogger();
	/*  */
	PrdTypeUtil prdutil = new PrdTypeUtil();
	PrdBrandUtil prdbrandutil = new PrdBrandUtil();
	PrdClassUtil prdcls = new PrdClassUtil();
	action = strUtils.fString(request.getParameter("action"));
	itemUtil.setmLogger(mLogger);
	prdutil.setmLogger(mLogger);
	prdcls.setmLogger(mLogger);
	String plant=(String)session.getAttribute("PLANT");
	String taxbylabelordermanagement = ub.getTaxByLableOrderManagement(plant);
	String username=(String)session.getAttribute("LOGIN_USER");
	sItem     = StrUtils.fString(request.getParameter("ITEM2"));/*  */
	if(sItem.length() <= 0) sItem     = StrUtils.fString(request.getParameter("ITEM1"));
	sItemDesc      = StrUtils.fString(request.getParameter("DESC"));
	sItemDesc      = StrUtils.replaceCharacters2Recv(sItemDesc);
	prd_cls_id= StrUtils.fString(request.getParameter("PRD_CLS_ID"));
	sArtist = StrUtils.fString(request.getParameter("ARTIST"));
	// Start code added by Deen for product brand on 11/9/12
	prdBrand = StrUtils.fString(request.getParameter("PRD_BRAND"));
	// End code added by Deen for product brand on 11/9/12
	sTitle = StrUtils.fString(request.getParameter("TITLE"));
	sMedium = StrUtils.fString(request.getParameter("MANUFACT"));
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
	loc = "";
	NETWEIGHT     = StrUtils.fString(request.getParameter("NETWEIGHT"));
	GROSSWEIGHT     = StrUtils.fString(request.getParameter("GROSSWEIGHT"));
	HSCODE     = StrUtils.fString(request.getParameter("HSCODE"));
	COO     = StrUtils.fString(request.getParameter("COO"));
	OBDISCOUNT = StrUtils.fString(request.getParameter("OBDISCOUNT"));
	IBDISCOUNT = StrUtils.fString(request.getParameter("IBDISCOUNT"));
	
	//User Check 	
		boolean al=false;
		al = ub.isCheckVal("popproduct", plant,username);
		if(al==true)
		{		
			System.out.println("popproduct");
		}
	
	CustUtil custUtil = new CustUtil();
	List customertypelist=custUtil.getCustTypeList("",plant," AND ISACTIVE ='Y'");
	
	maxstkqty = StrUtils.fString(request.getParameter("MAXSTKQTY"));
	float pricef=0;float costf=0;float minspricef=0; float prodgstf=0;
	
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
	
	
	if(((String)session.getAttribute("PRDCLSID"))!=null)
	{
		prdclsid = (String)session.getAttribute("PRDCLSID");
		
	}
	sAddEnb    = "enabled";
	sItemEnb   = "enabled";
	List prdlist=prdutil.getPrdTypeList("",plant," AND ISACTIVE ='Y'");
	List prdBrandList=prdbrandutil.getPrdBrandList("",plant," AND ISACTIVE ='Y'");
	List prdclslst = prdcls.getPrdTypeList("",plant," AND ISACTIVE ='Y'");
    List uomlist = itemUtil.getUomList(plant," AND ISACTIVE ='Y' ");
    List nonstklst =   itemUtil.getNonStockList(plant,""); 
	UserTransaction ut = null;
	if(action.equalsIgnoreCase("NEW")){
	    sItemDesc  = "";
	    sArtist   = "";
	    prd_cls_id="";
	    prdBrand = "";
	    sTitle  = "";
	    sMedium="";
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
	    sAddEnb    = "enabled";
	    sItemEnb   = "enabled";String minseq="";  String sBatchSeq=""; boolean insertFlag=false;String sZero="";
	  	TblControlDAO _TblControlDAO =new TblControlDAO();
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
	           boolean updateFlag=_TblControlDAO.update(updateQyery.toString(),htTblCntUpdate,"",plant);
	           sItem="P"+sZero+updatedSeq;
	        }
	      } catch(Exception e)
	         {
	    	  mLogger.exception(true,
						"ERROR IN JSP PAGE - item_list_do.jsp ", e);
	         }
	           
	
	} else if(action.equalsIgnoreCase("ADD")){
		try{
			
			ut = com.track.gates.DbBean.getUserTranaction();
			ut.begin();
		    if(!(itemUtil.isExistsItemMst(sItem,plant))) // if the item exists already
		    {
                        String specialcharsnotAllowed = StrUtils.isValidAlphaNumeric(sItem);
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
		          ht.put(IConstants.ITEMMST_REMARK2,strUtils.InsertQuotes(sMedium)); // remark2
		          ht.put(IConstants.ITEMMST_REMARK3,strUtils.InsertQuotes(sItemCondition));//remark3
		          ht.put(IConstants.PRDCLSID,prd_cls_id);//prd_cls_id
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
			      ht.put("HSCODE",HSCODE);
			      ht.put("COO",COO);
		          // Start code added by deen for createddate,createdby on 15/july/2013
		          ht.put("CRBY",username);
		          ht.put("CRAT",dateutils.getDateTime());
		          // End code added by deen for createddate,createdby on 15/july/2013
		         /*  if(ISPARENTCHILD=="")
		          {
		        	  ISPARENTCHILD="N";
		          } */
		         
		          ht.put("USERFLD1", "N");
		          if(stkqty=="")
		          stkqty ="0";
		          ht.put(IDBConstants.STKQTY, stkqty);//stkqty
		          if(maxstkqty=="")
			          maxstkqty ="0";
		          ht.put(IDBConstants.MAXSTKQTY, maxstkqty);
		          String remark = sRemark+" "+sMedium+" "+sItemCondition+" "+sTitle;
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
			        	  /* int DYNAMIC_CUSTOMERDISCOUNT_SIZE_INT = (new Integer(DYNAMIC_CUSTOMERDISCOUNT_SIZE)).intValue();
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
				        	 } */
 			           //end insert supplier discount Purchase
			            //if(OBCustomerDiscount){
			          	 inserted = mdao.insertIntoMovHis(htm);
			          // }
			          }
		          	sItemDesc=strUtils.RemoveDoubleQuotesToSingle(sItemDesc);
		          }
		          if(itemInserted&&inserted) {
		              res = "<font class = "+IConstants.SUCCESS_COLOR+">Product Added Successfully</font>";
		              DbBean.CommitTran(ut);
		             
		            //clear data automatically when click save button
		            sItem   = "";   /*  */
		            sItemDesc  = "";
		      	    sArtist   = "";
		      	    prd_cls_id="";
		      	    prdBrand = "";
		      	    sTitle  = "";
		      	    sMedium="";
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
		      	    sAddEnb    = "";
		      	    sItemEnb   = "";
		      	//automatic clear end	
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
%>


<div class="panel panel-default">
<div class="panel-heading" style="color: #ffffff; background-color: #102C54 " align="center">
<h3 class="panel-title">Product List</h3> 
</div>
</div>


<form class="form-horizontal" name="form" method="post"onSubmit="return onCheck();">

<!-- <form method="post" name="form1"> -->

<div class="box-body">
  <CENTER><strong><font style="font-size:18px;"><%=res%></font></strong></CENTER>
  <div id="target" style="display:none">
  <div class="form-group">
      	<label class="control-label col-sm-3" for="Product ID">
      	<i class="glyphicon glyphicon-star" style="font-size: 10px; top:-6px; color: #e50000"></i>&nbsp;Product ID:</label>
      	<div class="col-sm-3">
      	<div class="input-group">
		<input type="hidden" value="hide" name="search_criteria_status" id="search_criteria_status" />
    	<INPUT class="form-control" name="ITEM2" id="ITEM2" type="TEXT" value="<%=sItem%>" onchange="checkitem(this.value)" size="20" MAXLENGTH=50 <%=sItemEnb%>> <!--  -->
   		<span class="input-group-addon"  onClick="onNew();" sNewEnb >
   		<a href="#" data-toggle="tooltip" data-placement="top" title="Auto-Generate">
   		<i class="glyphicon glyphicon-edit" style="font-size: 20px;"></i></a></span>
  		</div>
  		</div>
    	<div class="form-inline">
      	<label class="control-label col-sm-2" for="Product Description">
      	<i class="glyphicon glyphicon-star" style="font-size: 10px; top:-6px; color: #e50000"></i>&nbsp;Description:</label>
      	<div class="col-sm-3">
    	<INPUT class="form-control" name="DESC" type="TEXT" value="<%=sItemDesc%>" style="width:100%" MAXLENGTH=100>
    	</div>
    	</div>
        </div>
         
 		<INPUT type="hidden" name="ITEM1" value=<%=sItem%>>
      
  		<div class="form-group">
      	<label class="control-label col-sm-3" for="Detailed Description">Detailed Description:</label>
      	<div class="col-sm-3">          
        <INPUT class="form-control" name="REMARKS" type="TEXT" value="<%=sRemark%>"	size="20" MAXLENGTH=100>
      	</div>
      	<div class="form-inline">
      	<label class="control-label col-sm-2" for="Unit Of Measure">
      	<i class="glyphicon glyphicon-star" style="font-size: 10px; top:-6px; color: #e50000"></i>&nbsp;Unit Of Measure:</label>      	
        <div class="col-sm-3">
      	<div class="input-group">
    	<INPUT class="form-control" name="UOM" type="TEXT" value="<%=uom%>" size="31" MAXLENGTH=100 readonly>
   		<span class="input-group-addon"  onClick="javascript:popUpWin('list/uomlist_save.jsp?UOM='+form.UOM.value);">
   		<a href="#" data-toggle="tooltip" data-placement="top" title="Unit Of Measure">
   		<i class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span>
  		</div>
  		</div>     
  	  	</div>
    	</div>
    
    	
  	  	
  	  	<div class="form-group">
      	<label class="control-label col-sm-3" for="Min Stock Quantity">Net Weight:</label>
      	<div class="col-sm-3">          
        <INPUT class="form-control" name="NETWEIGHT" id="NETWEIGHT" type="TEXT" value="<%=NETWEIGHT%>"size="20" MAXLENGTH=50>
      	</div>
      	<div class="form-inline">
      	<label class="control-label col-sm-2" for="Gross Weight">Gross Weight:</label>
      	<div class="col-sm-3">          
        <INPUT class="form-control" name="GROSSWEIGHT" id="GROSSWEIGHT" type="TEXT" value="<%=GROSSWEIGHT%>" style="width:100%" MAXLENGTH=50>
      	</div>
    	</div>
    	</div>
    	
    	 
  		      
  		<div class="form-group">
      	<label class="control-label col-sm-3" for="Product Class">Product Class:</label>
      	 <div class="col-sm-3">
      	<div class="input-group">
    	<INPUT class="form-control" name="PRD_CLS_ID" type="TEXT" value="<%=prdclsid%>" size="20" MAXLENGTH=100 readonly>
   		<span class="input-group-addon"  onClick="javascript:popUpWin('list/classlist_save.jsp?prdclsid='+form.PRD_CLS_ID.value);">
   		<a href="#" data-toggle="tooltip" data-placement="top" title="Product Class">
   		<i class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span>
  		</div>
  		</div> 
  		<div class="form-inline">
      	<label class="control-label col-sm-2" for="Product Type">Product Type:</label>
      	<div class="col-sm-3">           	
  		<div class="input-group">
    	<INPUT class="form-control" name="ARTIST" type="TEXT" value="<%=sArtist%>" size="31" MAXLENGTH=100 readonly>
   		<span class="input-group-addon"  onClick="javascript:popUpWin('list/typelist_save.jsp?sArtist='+form.ARTIST.value);">
   		<a href="#" data-toggle="tooltip" data-placement="top" title="Product Type">
   		<i class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span>
  		</div>
  	    </div>
  	    </div>  
  	  	</div>

		<div class="form-group">
      	<label class="control-label col-sm-3" for="Product Brand">Product Brand:</label>
      	<div class="col-sm-3">           	
  		<div class="input-group">
    	<INPUT class="form-control" name="PRD_BRAND" type="TEXT" value="<%=prdBrand%>" size="20" MAXLENGTH=100 readonly>
   		<span class="input-group-addon"  onClick="javascript:popUpWin('list/brandlist_save.jsp?prdBrand='+form.PRD_BRAND.value);">
   		<a href="#" data-toggle="tooltip" data-placement="top" title="Product Brand">
   		<i class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span>
  		</div>
  		</div>
  		<div class="form-inline">
      	<label class="control-label col-sm-2" for="HS Code">HS Code:</label>
      	<div class="col-sm-3"> 
        <div class="input-group">		
        <INPUT class="form-control" name="HSCODE" type="TEXT" value="<%=HSCODE%>" style="width:100%" MAXLENGTH=50>
		<span class="input-group-addon" onClick="javascript:popUpWin('list/hscodelist_save.jsp?hscode='+form.HSCODE.value);">
   		<a href="#" data-toggle="tooltip" data-placement="top" title="HS Code">
   		<i class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span>
   		</div>
      	</div>
    	</div>
  	  	</div>
  	  	
    	<div class="form-group">
      	<label class="control-label col-sm-3" for="COO">COO:</label>
      	<div class="col-sm-3">          
        <div class="input-group">		
        <INPUT class="form-control" name="COO" type="TEXT" value="<%=COO%>"size="20" MAXLENGTH=50>
		<span class="input-group-addon"  onClick="javascript:popUpWin('list/coolist_save.jsp?coo='+form.COO.value);">
        <a href="#" data-toggle="tooltip" data-placement="top" title="COO">
   		<i class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span>
      	</div>
      	</div>
      	<div class="form-inline">
      	<label class="control-label col-sm-2" for="Min Stock Quantity">Min Stock Quantity:</label>
      	<div class="col-sm-3">          
        <INPUT class="form-control" name="STKQTY" type="TEXT" value="<%=stkqty%>" style="width:100%" MAXLENGTH=50>
      	</div>
    	</div>
    	</div> 
	
		<div class="form-group">
      	<label class="control-label col-sm-3" for="Max Stock Quantity">Max Stock Quantity:</label>
      	<div class="col-sm-3">          
       	<INPUT class="form-control" name="MAXSTKQTY" type="TEXT" value="<%=maxstkqty%>"	size="20" MAXLENGTH=50>
      	</div>
      	<div class="form-inline">
      	<label class="control-label col-sm-2" for="List Price">List Price:</label>
      	<div class="col-sm-3">          
        <INPUT class="form-control" name="PRICE" id="PRICE" type="TEXT" value="<%=StrUtils.currencyWtoutCommSymbol(price)%>" style="width:100%"
		MAXLENGTH=50 onchange="javascript:enableSellingPrice(this.value);">
      	</div>
    	</div>
    	</div>
		
		<INPUT type="hidden" name="LOC_0" id="LOC_0" >  	
	  	
   		
   		<div class="form-group">
      	<label class="control-label col-sm-3" for="Cost">Cost:</label>
      	<div class="col-sm-3">          
        <INPUT class="form-control" name="COST" id="COST" type="TEXT" value="<%=StrUtils.currencyWtoutCommSymbol(cost)%>" size="20"
		MAXLENGTH=50 onchange="validDecimal(this.value)">
      	</div>
      	<div class="form-inline">
      	<label class="control-label col-sm-2" for="Min Selling Price ">Minimum Selling Price:</label>
      	<div class="col-sm-3">          
        <INPUT  class="form-control" name="MINSELLINGPRICE" id="MINSELLINGPRICE" type="TEXT" value="<%=StrUtils.currencyWtoutCommSymbol(minsprice)%>" style="width:100%"
		MAXLENGTH=50 onchange="validDecimal(this.value)">
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
		<div class="form-inline">
		<label class="control-label col-sm-2" style="padding-top: 5px;">Used as Non Stock Product:</label>
 		<div class="col-sm-2">
  		<label class="radio-inline">
      	<input type="radio" name="NONSTOCKFLAG" type = "radio"  value="N"  id="NotNonStock" checked="checked" onclick="DisplayNonStkType();">NO
    	</label>
    	<label class="radio-inline">
      	<input type="radio" name="NONSTOCKFLAG" type = "radio" value="Y"  id = "NonStock" onclick="DisplayNonStkType();">YES
    	</label>
     	</div>
		</div>
      	</div>
      	<div class="form-group">
      	<label class="control-label col-sm-3" for="Non Stock Type"></label>
      	<div class="col-sm-3">
		</div>
      	<div id="divNonStk" style="display:none;" >
		<div class="form-inline">
      	<label class="control-label col-sm-2" for="Non Stock Type">Non Stock Type:</label>
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
   		<!-- <div class="form-group">
      	<label class="control-label col-sm-3" for="Manufacturer">Manufacturer:</label>
      	<div class="col-sm-3">
      	<div class="input-group"> -->
    	<INPUT class="form-control" name="MANUFACT" type="hidden" value="<%=sMedium%>" size="20" MAXLENGTH=100>
   		<!-- <span class="input-group-addon"  onClick="javascript:popUpWin('list/vendor_listforItem.jsp?MANUFACT='+form.MANUFACT.value);">
   		<a href="#" data-toggle="tooltip" data-placement="top" title="Manufacturer Details">
   		<i class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span>
  		</div>
  		</div>
        </div> -->
      	
<!-- <div class="form-group">
      	<label class="control-label col-sm-3" for="Discount POS(%)">Discount POS(%):</label>
      	<div class="col-sm-3">       -->    
       	<INPUT class="form-control" name="DISCOUNT" id="DISCOUNT" type="hidden" value="<%=discount%>"
		size="20" MAXLENGTH=50>
      	<!-- </div>
    	</div> -->
		
		<INPUT type="hidden" name="DYNAMIC_CUSTOMERDISCOUNT_SIZE">
				
		<!-- <div class="form-group">
      	<label class="control-label col-sm-3" for="Remarks">Customer Discount Sales Order:</label>
    	<div class="col-sm-4">
    	<label class="radio-inline"> -->
     	<INPUT name="OBDISCOUNT" name="OBDISCOUNT" type = "hidden"  value="BYPRICE"  id="BYPRICE" checked="checked"><!-- By Price -->
    	<!-- </label>
    	<label class="radio-inline"> -->
      	<INPUT  name="OBDISCOUNT" name="OBDISCOUNT" type = "hidden" value="BYPERCENTAGE"  id = "BYPERCENTAGE" ><!-- By Percentage -->
    	<!-- </label>
     	</div>
		</div> -->


 		<!-- <div class="form-group">
	 	<div class="col-sm-3">&nbsp;</div>
	 	<div class="col-sm-3">
		<TABLE id="customerDiscount">
		<TR>
		<TD> --><INPUT class="form-control" name="DYNAMIC_CUSTOMER_DISCOUNT_0" id="DYNAMIC_CUSTOMER_DISCOUNT_0" type="hidden" value=""
		size="20" MAXLENGTH="50"  /><!-- </TD>
		<TD align="center"><div class="input-group">--><INPUT class="form-control" name="CUSTOMER_TYPE_ID_0" id="CUSTOMER_TYPE_ID_0"  type = "hidden" value="<%=CUSTOMER_TYPE_ID%>" size="20"  MAXLENGTH=50
		onkeypress="if((event.keyCode=='13') && ( document.form.CUSTOMER_TYPE_ID_0.value.length > 0)){validateCustomerType(0);}">
       <!--  <a href="#" class="input-group-addon" data-toggle="tooltip" data-placement="top" Title="Customer Details" 
        onClick="javascript:popUpWin('list/multiCustomerTypeList.jsp?CUSTOMERTYPE='+form.CUSTOMER_TYPE_ID_0.value+'&INDEX=0');">
        <i class="glyphicon glyphicon-log-in"  style="font-size: 20px;"></i></a> 
        </div>
        </TD>
        </TR>
		</TABLE>
    	</div>
    	</div> -->
    
    	<!-- <div class="form-group">        
        <div class="col-sm-offset-3 col-sm-8">
       	<button type="button" class="Submit btn btn-default" value="Add New Customer Discount(Outbound Order)"
		onclick="addRow('customerDiscount');"><b>Add New Customer Discount</b></button>&nbsp;&nbsp;
      	<button type="button" class="Submit btn btn-default" value="Remove Last Customer Discount(Outbound Order)"
		onclick="deleteRow('customerDiscount');"><b>Remove Last Customer Discount</b></button>&nbsp;&nbsp; -->
      	<INPUT type="hidden" name="DYNAMIC_CUSTOMER_DISCOUNT_SIZE">
        <!-- </div>
    	</div> -->
	
		<!-- <div class="form-group">
      	<label class="control-label col-sm-3" for="Remarks">Supplier Discount Purchase Order:</label>
    	<div class="col-sm-3">
    	<label class="radio-inline"> -->
      	<INPUT name="IBDISCOUNT" type = "hidden"  value="BYCOST"  id="BYCOST" checked="checked"><!-- By Cost -->
    	<!-- </label>
    	<label class="radio-inline"> -->
      	<INPUT  name="IBDISCOUNT" type = "hidden" name="IBDISCOUNT" type = "radio" value="BYPERCENTAGE"  id = "BYPERCENTAGE"><!-- By Percentage -->
    	<!-- </label>
     	</div>
		</div> -->	
	 
		<!-- <div class="form-group">
	 	<div class="col-sm-3">&nbsp;</div>
	 	<div class="col-sm-3">
    	<TABLE id="supplierDiscount">
		<TR>
		<TD> --><INPUT class="form-control" name="DYNAMIC_SUPPLIER_DISCOUNT_0" id="DYNAMIC_SUPPLIER_DISCOUNT_0" type="hidden" value=""
		size="20" MAXLENGTH="50"  /><!-- </TD>
		<TD align="center"><div class="input-group"> --><INPUT class="form-control" name="SUPPLIER_0" id="SUPPLIER_0"  type = "hidden" value="<%=SUPPLIER%>" size="20"  MAXLENGTH=50
		onkeypress="if((event.keyCode=='13') && ( document.form.SUPPLIER_0.value.length > 0)){validateSupplier(0);}">
        <!-- <a href="#" class="input-group-addon" data-toggle="tooltip" data-placement="top" Title="Supplier Details" 
        onClick="javascript:popUpWin('list/multiSupplierList.jsp?SUPPLIER='+form.SUPPLIER_0.value+'&INDEX=0');">
       	<i class="glyphicon glyphicon-log-in"  style="font-size: 20px;"></i></a>
       	</div>
        </TD>
       	</TR>
		</TABLE>
    	</div>
    	</div> -->
	
		<!-- <div class="form-group">        
        <div class="col-sm-offset-3 col-sm-8">
       	<button type="button" class="Submit btn btn-default"value="Add New Supplier Discount(Inbound Order)"
		onclick="addRowCost('supplierDiscount');"><b>Add New Supplier Discount</b></button>&nbsp;&nbsp;
      	<button type="button" class="Submit btn btn-default" value="Remove Last Supplier Discount(Inbound Order)"
		onclick="deleteRowCost('supplierDiscount');"><b>Remove Last Supplier Discount</b></button>&nbsp;&nbsp; -->
      	<INPUT type="hidden" name="DYNAMIC_SUPPLIER_DISCOUNT_SIZE">
       <!--  </div>
    	</div> -->
      
    
    	<!-- <div class="form-group">
      	<label class="control-label col-sm-3" for="Remarks">Remarks1:</label>
      	<div class="col-sm-3">  -->         
       	<INPUT class="form-control" name="ITEM_CONDITION" type="hidden" value="<%=sItemCondition%>" size="20" MAXLENGTH=100>
      	<!-- </div>
    	</div> -->
     	
     	<!-- <div class="form-group">
      	<label class="control-label col-sm-3" for="Remarks">Remarks2:</label>
      	<div class="col-sm-3">   -->        
        <INPUT class="form-control" name="TITLE" type="hidden" value="<%=sTitle%>" size="20"	MAXLENGTH=200>
      <!-- 	</div>
    	</div> -->
	  
	 	<div class="form-group">        
      	<div class="text-center">
      	<!-- <button type="button" class="Submit btn btn-default" onClick="window.location.href='../home'"><b>Back</b></button>&nbsp;&nbsp; -->
      	<button type="button" class="Submit btn btn-default" onClick="onClear();" sNewEnb ><b>Clear</b></button>&nbsp;&nbsp;
      	<% if (al) { %>
      	<button type="button" class="Submit btn btn-default" data-toggle="modal" data-target="#myModal" onClick="onAdd();" sAddEnb ><b>Save</b></button>&nbsp;&nbsp;
      	<% } else { %>
      	<button disabled="disabled" type="button" class="Submit btn btn-default" data-toggle="modal" data-target="#myModal" onClick="onAdd();" sAddEnb ><b>Save</b></button>&nbsp;&nbsp;
      	<% } %>
      	<button type="button" class="Submit btn btn-default" onClick="window.close();"><b>Close</b></button>&nbsp;&nbsp;
   <!--   <INPUT class="hidden" type="BUTTON" value="Print Barcode" onClick="onPrint();" sDeleteEnb/>  -->
      	</div>
    	</div>
		</div>
   <!--  <INPUT     name="ISPARENTCHILD"  type ="hidden" value="<%=ISPARENTCHILD%>" size="1"   MAXLENGTH=80 ></TD>-->
   <!--  <br>
<br> -->
 
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
			href="item_list_po.jsp?action=ITEMMAPDELETE&keyitem=<%=sItem%>&mapitem=<%=(String)listMapItem.get(i)%>&DESC=<%=strUtils.replaceCharacters2Send(sItemDesc)%>&UOM=<%=sItemUom%>&REORDQTY=<%=sReOrdQty%>">Delete</a></TD>
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

<div class="form-group">
      <div class="col-sm-3">
      <a href="#" id="Show" style="font-size: 15px; color: #0059b3; text-decoration:none;">Show Create Product</a>
      <a href="#" id="Hide" style="font-size: 15px; color: #0059b3; text-decoration:none; display:none;">Hide Create Product</a>
      </div>
       	  </div>

</div>

<script>

function onPrint(){
   var ITEM   = document.form.ITEM.value;
   var ITEMDESC = document.form.DESC.value;
   if(ITEM == "" || ITEM == null) {alert("Please Choose Product ID"); document.form.ITEM.focus(); return false; }
   document.form.action  = "PrintItemLabel.jsp?action=PRINT&Item="+ITEM+"&ITEMDESC="+ITEMDESC;
   alert("Do you want to Print?");
   document.form.submit();
}
function addRow(tableID) {
	
	  	
	var table = document.getElementById(tableID);
	var rowCount = table.rows.length;
	var row = table.insertRow(rowCount);
	 var form = document.forms['form'];


	var itemCell = row.insertCell(0);
	var itemCellText =  "&nbsp;<INPUT class=\"form-control\" name=\"DYNAMIC_CUSTOMER_DISCOUNT_"+rowCount+"\" ";
	itemCellText = itemCellText+ " id=\"DYNAMIC_CUSTOMER_DISCOUNT_"+rowCount+"\" type = \"TEXT\" size=\"20\" MAXLENGTH=\"50\">";
	itemCell.innerHTML = itemCellText;
	
	
	var itemCell = row.insertCell(1);
	var itemCellText =  "&nbsp;<div class=\"input-group\"><INPUT class=\"form-control\" name=\"CUSTOMER_TYPE_ID_"+rowCount+"\" ";
	itemCellText = itemCellText+ " id=\"CUSTOMER_TYPE_ID_"+rowCount+"\" type = \"TEXT\" size=\"20\"    onkeypress=\"if((event.keyCode=='13') && ( this.value.length > 0)){validateCustomerType("+rowCount+");}\" MAXLENGTH=\"50\">";
	itemCellText = itemCellText+ "<a class=\"input-group-addon\" href=\"#\" onClick=\"javascript:popUpWin('list/multiCustomerTypeList.jsp?CUSTOMERTYPE='+form.CUSTOMER_TYPE_ID"+'_'+rowCount+".value+'&INDEX="+rowCount+"');\"><i class=\"glyphicon glyphicon-log-in\" style=\"font-size: 20px;\"><i></a><div>";
	itemCell.innerHTML = itemCellText;
	
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
	var itemCellText =  "&nbsp;<INPUT name=\"DYNAMIC_SUPPLIER_DISCOUNT_"+rowCount+"\" ";
	itemCellText = itemCellText+ " id=\"DYNAMIC_SUPPLIER_DISCOUNT_"+rowCount+"\" type = \"TEXT\" size=\"20\" MAXLENGTH=\"50\" class=\"form-control\">";
	itemCell.innerHTML = itemCellText;
	
	
	var itemCell = row.insertCell(1);
	var itemCellText =  "&nbsp;<div class=\"input-group\"> <INPUT class=\"form-control\" name=\"SUPPLIER_"+rowCount+"\" ";
	itemCellText = itemCellText+ " id=\"SUPPLIER_"+rowCount+"\" type = \"TEXT\" size=\"20\"    onkeypress=\"if((event.keyCode=='13') && ( this.value.length > 0)){validateSupplier("+rowCount+");}\" MAXLENGTH=\"50\">";
	itemCellText = itemCellText+ "<a  class=\"input-group-addon\" href=\"#\" onClick=\"javascript:popUpWin('list/multiSupplierList.jsp?SUPPLIER='+form.SUPPLIER"+'_'+rowCount+".value+'&INDEX="+rowCount+"');\"><i class=\"glyphicon glyphicon-log-in\" style=\"font-size: 20px;\"><i></a></div>";
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
			   document.getElementById("divNonStk").style.display = "inline";  
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
		
</script>

<div>
  <table id="myTable" class="table">
    <thead style="background: #eaeafa">
    <TR>
      <TH >Product</TH>
       <TH >Description</TH>
        <TH >UOM</TH>
    </TR>
    </thead>
    <tbody>
<%
HashMap<String, String> loggerDetailsHasMap = new HashMap<String, String>();
loggerDetailsHasMap.put(MLogger.COMPANY_CODE, (String) session
		.getAttribute("PLANT"));
loggerDetailsHasMap.put(MLogger.USER_CODE, StrUtils.fString(
		(String) session.getAttribute("LOGIN_USER")).trim());
/* MLogger mLogger = new MLogger(); */
mLogger.setLoggerConstans(loggerDetailsHasMap);
/* StrUtils strUtils = new StrUtils(); */
ItemMstUtil itemUtil1 = new ItemMstUtil();
plant =(String)session.getAttribute("PLANT");
sItem = strUtils.fString(request.getParameter("ITEM")).trim();
sItemDesc = strUtils.fString(request.getParameter("ITEM_DESC")).trim();
String sCond = "";
ArrayList listQry = itemUtil1.getItemList(plant,sItem,sItemDesc,sCond);
try
    {
       for(int i =0; i<listQry.size(); i++) {
       	Map m=(Map)listQry.get(i);
     	String item   =  strUtils.fString((String)m.get("item"));
        String itemdesc    =  strUtils.fString((String)m.get("itemdesc"));
        uom   =  strUtils.fString((String)m.get("UOM"));
%>
<TR>
		<td class="main2" style="font-weight: bold" align="left">&nbsp;<a href="#"
			onClick="window.opener.form.ITEM.value='<%=item%>';window.opener.form.DESC.value='<%=itemdesc%>';
      	             ;window.opener.form.UOM.value='<%=uom%>';window.opener.validateProduct();
       		          window.close();"><%=item%></a></td>
		<td align="left" class="main2">&nbsp;<%=itemdesc%></td>
		<td align="left" class="main2">&nbsp;<%=uom%></td>
	</TR>

    
<%
		}
	}catch(Exception he){he.printStackTrace(); System.out.println("Error in reterieving data");
	}
%>
       
   </tbody>
 </table>
 <!-- <br>
  <div class="text-center">       
        <button type="button" class="Submit btn btn-default" onClick="window.close();"><b>Close</b></button>&nbsp;&nbsp;
	     </div>  -->   
</div>
</form>
</body>
</html>

<script>
$(document).ready(function(){
	$('#myTable').dataTable();
    /* $('#myModal1').click(function(){
    	if(document.getElementById("alertValue").value!="")
    	{
    		//$("#myModal").modal();
    		document.getElementById('myModal').style.display = "block";
    	}
    }); */
    $('[data-toggle="tooltip"]').tooltip(); 
	
	var  d = document.getElementById("TaxByLabelOrderManagement").value;
    document.getElementById('TaxLabelOrderManagement').innerHTML = "Product " + d +" (%):";
	
	//Below Jquery Script used for Show/Hide Function
    
    $('#Show').click(function() {
	    $('#target').show(500);
	    $('#Show').hide(0);
	    $('#Hide').show(0);
	    $('#search_criteria_status').val('show');
	});
 
    $('#Hide').click(function() {
	    $('#target').hide(500);
	    $('#Show').show(0);
	    $('#Hide').hide(0);
	    $('#search_criteria_status').val('hide');
	});
    if ('<%=request.getParameter("search_criteria_status")%>' == 'show'){
    	$('#Show').click();
    }else{
    	$('#Hide').click();
    }
});
function checkitem(itemvalue)
{	
	 if(itemvalue=="" || itemvalue.length==0 ) {
		alert("Enter Item!");
		document.getElementById("ITEM2").focus();
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
						document.getElementById("ITEM2").focus();
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
</script>
