<%@ include file="header.jsp" %>
<%@ page import="com.track.constants.*"%>
<%@page import="com.track.constants.IDBConstants,com.track.util.StrUtils,com.track.dao.PlantMstDAO"%>
<%@ page import="com.track.dao.PurchaseAttachDAO"%>
<%
String title = "Edit Purchase Order";
%>
<%@include file="sessionCheck.jsp" %>
<jsp:include page="header2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
	<jsp:param name="mainmenu" value="<%=IConstants.PURCHASE%>"/>
	<jsp:param name="submenu" value="<%=IConstants.PURCHASE_ORDER%>"/>
</jsp:include>
<link rel="stylesheet" href="css/typeahead.css">
<link rel="stylesheet" href="css/accounting.css">
<script type="text/javascript" src="js/general.js"></script>
<script type="text/javascript" src="js/calendar.js"></script>
<script type="text/javascript">

function isNumberKey(evt, element, id) {
	  var charCode = (evt.which) ? evt.which : event.keyCode;
	  if (charCode > 31 && (charCode < 48 || charCode > 57) && !(charCode == 46 || charCode == 8 || charCode == 45))
		  {
	    	return false;
		  }
	  
/* 	  		else {
	    var len = $(element).val().length;
	    var index = $(element).val().indexOf('.');
	    if (index > 0 && charCode == 46) {
	      return false;
	    }
	    if (index > 0) {
	      var CharAfterdot = (len + 1) - index;
	      if (CharAfterdot > id) {
	        return false;
	      }
	    }

	  } */
	  return true;
	}
function validDecimal(str,precision) {
	if (str.indexOf('.') == -1)
	str += ".";
	var declength = parseInt(precision);
	var decNum = str.substring(str.indexOf('.') + 1, str.length);
	if(decNum.length>declength){
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

var subWin = null;

function popUpWin(URL) {
 subWin = window.open(encodeURI(URL), 'CUSTOMERS', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=800,height=400,left = 200,top = 184');
}

	function validatePO(form)
	{
		if (form.PONO.value.length < 1)
	    {
		    alert("Please Enter Order Number !");
		    form.PONO.focus();
		    return false;
	    }
	    
	  /*    if (form.DELDATE.value.length > 1){
	    if (isDate(form.DELDATE.value)==false){
			form.DELDATE.focus();
			return false;
	    }
	   } */
	     submitForm('View');
	        
	}

	function popWin(vname){
	    window.open('vendSumm.jsp?VENDNAME='+vname+'&P=Y');
	}

	function onDelete(form)
	{
	   if (form.PONO.value.length < 1)
	    {
		    alert("Please Enter PO Number !");
		    form.PONO.focus();
		    return false;
	    }
	    else{
	     	var mes=confirm("Do you want to delete the purchase order ?");
	      if(mes==true)
	      {
	      return true;
	      }
	      else
	      {  
	      return  false;
	      }
	    }
	    
	}
	
	function onDelete(form)
	{
		  if (form.PONO.value.length < 1)
		    {
			    alert("Please Enter Order Number !");
			    form.PONO.focus();
			    return false;
		    }
	        
		  else{
			   var mes=confirm("Do you want to Delete the Purchase order ?");
			   if(mes==true)
			    {
			      
			      document.form.action="/track/purchaseorderservlet?Submit=DELETE";
			      document.form.submit();
			    }
			    else
			    {  
			      return  false;
			    }
			}
	}
	
	function onUpdate(form)
	{
	   
		if (form.PONO.value.length < 1)
	    {
		    alert("Please Enter Order Number !");
		    form.PONO.focus();
		    return false;
	    }
		
		if (form.CUST_NAME.value.length < 1)
		    {
			    alert("Please Select Supplier !");
			    form.CUST_NAME.focus();
			    return false;
		    }
	            
	             if (form.CUST_CODE1.value.length < 1)
	      {
	        alert("Please select valid Supplier Name !");
	        form.CUST_NAME.focus();
	        form.CUST_NAME.select();
	        return false;
	      }
	             if (form.DISPLAY.value.length < 1)
	             {
	               alert("Please select Currency ID!");
	               form.DISPLAY.focus();
	               form.DISPLAY.select();
	               return false;
	             }
	                    
	        if(!IsNumeric(form.GST.value))
	           {
	             alert(" Please Enter valid  VAT !");
	             form.GST.focus();  form.GST.select(); return false;
	           }
	        if(!IsNumeric(form.LOCALEXPENSES.value))
	        {
	          alert(" Please Enter valid  Local Expenses !");
	          form.LOCALEXPENSES.focus();  form.LOCALEXPENSES.select(); return false;
	        }
	        
	       /*  if (form.DELDATE.value.length > 1){
	            if (isDate(form.DELDATE.value)==false){
	                        form.DELDATE.focus();
	                        return false;
	            }
	        }  */
	       
	       if(!IsNumeric(form.ORDERDISCOUNT.value))
           {
             alert(" Please Enter valid Order Discount!");
             form.ORDERDISCOUNT.focus(); 
             form.ORDERDISCOUNT.select(); 
             return false;
           }
           if(!IsNumeric(form.SHIPPINGCOST.value))
           {
             alert(" Please Enter valid Shipping Cost!");
             form.SHIPPINGCOST.focus(); 
             form.SHIPPINGCOST.select(); 
             return false;
           }
           if(!IsNumeric(form.LOCALEXPENSES.value))
           {
             alert(" Please Enter valid Local Expenses!");
             form.LOCALEXPENSES.focus(); 
             form.LOCALEXPENSES.select(); 
             return false;
           }
       	if (!validToThreeDecimal(document.getElementById("GST").value)) {
			alert("Not more than 3 digits are allowed after decimal value in Vat");
			form.GST.focus();
			return false;
		} 
		else if (!validDecimal(document.getElementById("SHIPPINGCOST").value,document.getElementById("numberOfDecimal").value)) {
		alert("Not more than "+document.getElementById("numberOfDecimal").value+" digits are allowed after decimal value in Shipping Cost");
		document.form.SHIPPINGCOST.focus();
		return false;
	}
		else if (!validToThreeDecimal(document.getElementById("ORDERDISCOUNT").value)) {
			alert("Not more than 3 digits are allowed after decimal value in Order Discount");
			document.form.ORDERDISCOUNT.focus();
			return false;
		}
		else if (!validDecimal(document.getElementById("LOCALEXPENSES").value,document.getElementById("numberOfDecimal").value)) {
			alert("Not more than "+document.getElementById("numberOfDecimal").value+" digits are allowed after decimal value in Local Expenses");
			document.form.LOCALEXPENSES.focus();
			return false;
		}
	          if (form.ORDDATE.value.length > 1){
	            if (isDate(form.ORDDATE.value)==false){
	                        form.ORDDATE.focus();
	                        return false;
	            }	  
	              else{
	     var mes=confirm("Do you want to update the Purchase order ?");
	      if(mes==true)
	      {
	      
	    	  add_attachments();
	    /*   document.form.action="/track/purchaseorderservlet?Submit=Update";
	      document.form.submit(); */
	      }
	      else
	      {  
	      return  false;
	      }
	    }
	  } 
	}


function onClear(){
	   document.form.PONO.value   		=""
	  document.form.CUST_NAME.value     =""
	  form.JOB_NUM.value         		=""
	  form.PERSON_INCHARGE.value 		=""
	  form.CONTACT_NUM.value     		=""
	  form.JOB_NUM.value         		=""
	  form.COLLECTION_TIME.value  		=""
	  form.DELDATE.value         		=""
	  //form.COLLECTION_TIME.value 		=""
	  form.REMARK1.value         =""
	  form.REMARK2.value         =""
	  form.REMARK3.value         ="";
	  form.CUST_NAME.value         		=""
	  form.PERSON_INCHARGE.value   		=""
	  form.TELNO.value         			=""
	  form.EMAIL.value         			=""
	  form.ADD1.value         			=""
	  form.ADD2.value         			=""
	  form.ADD3.value         			=""
	  form.ADD4.value         			=""
	  form.COUNTRY.value         		=""
	  form.ZIP.value         			=""
	  form.ORDERTYPE.value="";
	  form.STATUS_ID.value=""; //orderstaus
	  form.SHIPPINGID.value="";
	  form.SHIPPINGCUSTOMER.value="";
	  form.ORDERDISCOUNT.value="";
	  form.SHIPPINGCOST.value="";
	  form.INCOTERMS.value="";
	  form.PAYMENTTYPE.value="";
	  form.LOCALEXPENSES.value="";
	  form.EDIT_PURCHASE_LOC.value="";
	  form.PURCHASE_LOC.value="";
	  form.TAXTREATMENT.value="";
		document.form.PURCHASE_LOC.selectedIndex=0;
		document.getElementById('CHK1').style.display = 'none';
		$('.check').not(this).prop('checked', false);
		document.getElementById('TAXTREATMENT').innerHTML="";
	  return true;
	}

function headerReadable(){
	if(document.form.DATEFORMAT.checked)
	{
		
		document.form.DELDATE.value="";
		$('#DELDATE').attr('readonly',true).datepicker({ dateFormat: 'dd/mm/yy'});
		
	}
	else{
		document.form.DELDATE.value="";
		 $('#DELDATE').attr('readonly',false).datepicker("destroy");

	}
} 

	function submitForm(actionvalue)
	{
		form.removeAttribute("target", "_blank");
		document.form.action = "/track/purchaseorderservlet?Submit="+actionvalue;
	    document.form.submit();
	}
	
	function submitFormPrint(actionvalue)
	{
		form.setAttribute("target", "_blank");
		document.form.action = "/track/purchaseorderservlet?Submit="+actionvalue;
	    document.form.submit();
	}

	function submitToView(){
		
		if(event.keyCode=='13'){
			submitForm('View');
		}
				
	}
	function CopyIBtoOBsubmitForm(actionvalue)
	{
		document.form.action = "/track/deleveryorderservlet?Submit="+actionvalue;
	    document.form.submit();
	}
	
	function validateform(form)
	{

	  if (form.PONO.value.length < 1)
	  {
	    alert("Please Enter Order Number!");
	    form.PONO.focus();
	    return false;
	  }
	 
	  return true;
	 	  
	}
</script>
<jsp:useBean id="gn"  class="com.track.gates.Generator" />
<jsp:useBean id="sl"  class="com.track.gates.selectBean" />
<jsp:useBean id="db"  class="com.track.gates.defaultsBean" />
<jsp:useBean id="su"  class="com.track.util.StrUtils" />
<jsp:useBean id="vmb" class="com.track.tables.VENDMST" />
<jsp:useBean id="phb" class="com.track.tables.POHDR" />
<jsp:useBean id="pdb" class="com.track.tables.PODET" />
<jsp:useBean id="logger" class="com.track.util.MLogger" />
<jsp:useBean id="du" class="com.track.util.DateUtils" />
<jsp:useBean id="ub"  class="com.track.gates.userBean" />
<%
	PlantMstDAO plantMstDAO = new PlantMstDAO();
    PurchaseAttachDAO purchaseAttachDAO = new PurchaseAttachDAO();
	session = request.getSession();
	String DIRTYPE ="";
    String pono     = StrUtils.fString(request.getParameter("PONO"));
    String action   = StrUtils.fString(request.getParameter("action")).trim();
    String plant = StrUtils.fString((String)session.getAttribute("PLANT"));
    String sUserId = (String) session.getAttribute("LOGIN_USER");
    String RFLAG=    (String) session.getAttribute("RFLAG");
    DIRTYPE       = StrUtils.fString(request.getParameter("DIRTYPE"));
    List supplierattachlist= new ArrayList();
   	String vend = "",deldate="",jobNum = "",custName = "",custCode="",personIncharge = "",contactNum = "",
    remark1 = "",remark2 = "",address="",address2="",address3="",collectionDate="",collectionTime="",
    contactname="",telno="",email="",add1="",currencyid="",add2="",add3="",add4="",country="",zip="",
    remarks="",gst="",ordertype="",shippingid="",shippingcustomer="",orderdiscount="",shippingcost="",
    incoterms="",statusID="",paymenttype="",localexpenses="",dateformat="",sTAXTREATMENT="",sPURCHASE_LOC="",sREVERSECHARGE="",sGOODSIMPORT="";
    String sSaveEnb    = "disabled";
    String taxbylabelordermanagement = ub.getTaxByLableOrderManagement(plant);
    String numberOfDecimal = plantMstDAO.getNumberOfDecimal(plant);   
    String fieldDesc="<tr><td colspan=\"10\" align=\"center\"> Please enter any search criteria</td></tr>";
    if(DIRTYPE.length()<=0){
   	 	DIRTYPE = "INBOUND";
   	}
    System.out.println("before view.."+gst);
    float gstVatValue ="".equals(gst) ? 0.0f :  Float.parseFloat(gst);
	float orderdiscountValue ="".equals(orderdiscount) ? 0.0f :  Float.parseFloat(orderdiscount);
	/* float shippingcostValue ="".equals(shippingcost) ? 0.0f :  Float.parseFloat(shippingcost);
	float localexpensesValue ="".equals(localexpenses) ? 0.0f :  Float.parseFloat(localexpenses); */
	double shippingcostValue ="".equals(shippingcost) ? 0.0d :  Double.parseDouble(shippingcost);
	double localexpensesValue ="".equals(localexpenses) ? 0.0d :  Double.parseDouble(localexpenses);
	if(gstVatValue==0f){
		gst="0.000";
	}else{
		gst=gst.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
	}if(orderdiscountValue==0f){
		orderdiscount="0.000";
	}else{
		orderdiscount=orderdiscount.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
	}/* if(shippingcostValue==0f){
		shippingcost="0.00000";
	}else{
		shippingcost=shippingcost.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
	}if(localexpensesValue==0f){
		localexpenses="0.00000";
	}else{
		localexpenses=localexpenses.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
	} */
	
	shippingcost = StrUtils.addZeroes(shippingcostValue, numberOfDecimal);
	localexpenses = StrUtils.addZeroes(localexpensesValue, numberOfDecimal);
    
    if(action.equalsIgnoreCase("View")){
     try{
      
      		Map m=(Map)request.getSession().getAttribute("podetVal");
      		fieldDesc=(String)request.getSession().getAttribute("RESULT1");
      		if(m.size()>0)
      		{
	      		ordertype=(String)m.get("ordertype");
	      		currencyid = (String)m.get("currencyid");
	      		gst=(String)m.get("inbound_Gst");
	            jobNum=(String)m.get("jobNum");
	            custName=(String)m.get("custName");
	            custCode = (String) m.get("custCode");
	            personIncharge=(String)m.get("contactname");
	            contactNum=(String)m.get("contactNum");
	            telno=(String)m.get("telno");
	            email=(String)m.get("email");
	            add1=(String)m.get("add1");
	            add2=(String)m.get("add2");
	            add3=(String)m.get("add3");
	            add4=(String)m.get("add4");
	            country=(String)m.get("country"); zip=(String)m.get("zip");
	            remarks=(String)m.get("remarks");
	            address=(String)m.get("address");
	            address2=(String)m.get("address2");
	            address3=(String)m.get("address3");
	            collectionDate=(String)m.get("collectionDate");
	            collectionTime=(String)m.get("collectionTime");
	            remark1=(String)m.get("remark1");
	            remark2=(String)m.get("remark2");
	            remark2=(String)m.get("remark3");

	            deldate=(String)m.get("deldate");
	            statusID  = (String)m.get("statusid");
	            shippingid= (String) m.get("shippingid");
	            shippingcustomer = (String) m.get("shippingcustomer");
	    		orderdiscount = (String) m.get("orderdiscount");
	    		shippingcost= (String) m.get("shippingcost");
	    		incoterms = (String) m.get("incoterms");
	    		localexpenses= (String) m.get("localexpenses");
	    		dateformat= (String) m.get("DELIVERYDATEFORMAT");
	    		sPURCHASE_LOC= (String) m.get("PURCHASE_LOCATION");
	    		sTAXTREATMENT= (String) m.get("TAXTREATMENT");
	    		sREVERSECHARGE= (String) m.get("REVERSECHARGE");
	    		sGOODSIMPORT= (String) m.get("GOODSIMPORT");
	    		supplierattachlist = purchaseAttachDAO.getpurchaseAttachByPONO(plant, pono);	
	    		System.out.println("After view.."+gst);
	    		System.out.println("After view.."+orderdiscount);
	    		System.out.println("After view.."+shippingcost);
	    		float gstVatVal ="".equals(gst) ? 0.0f :  Float.parseFloat(gst);
	    		float orderdiscountVal ="".equals(orderdiscount) ? 0.0f :  Float.parseFloat(orderdiscount);
	    		/* float shippingcostVal ="".equals(shippingcost) ? 0.0f :  Float.parseFloat(shippingcost);
	    		float localexpensesVal ="".equals(localexpenses) ? 0.0f :  Float.parseFloat(localexpenses); */
	    		double shippingcostVal ="".equals(shippingcost) ? 0.0d :  Double.parseDouble(shippingcost);
	    		double localexpensesVal ="".equals(localexpenses) ? 0.0d :  Double.parseDouble(localexpenses);
	    		
	    		if(gstVatVal==0f){
	    			gst="0.000";
	    		}else{
	    			gst=gst.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
	    		}if(orderdiscountVal==0f){
	    			orderdiscount="0.000";
	    		}else{
	    			orderdiscount=orderdiscount.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
	    		}/* if(shippingcostVal==0f){
	    			shippingcost="0.00000";
	    		}else{
	    			shippingcost=shippingcost.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
	    		}if(localexpensesVal==0f){
	    			localexpenses="0.00000";
	    		}else{
	    			localexpenses=localexpenses.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
	    		} */
	    		shippingcost = StrUtils.addZeroes(shippingcostVal, numberOfDecimal);
	    		localexpenses = StrUtils.addZeroes(localexpensesVal, numberOfDecimal);
	    		paymenttype=(String) m.get("payment_terms");
	    		
	       }
	   		//---Commanded by Deen on April 14 2014, Description:To fix data's refreshing bug when click Print Out and Export Excel button
				//request.getSession().setAttribute("RESULT","");
				// request.getSession().setAttribute("podetVal","");
		    //---End Commanded by Deen on April 14 2014 
    }
    catch(Exception e){
     	logger.log(0,"Exception ::" + e.getMessage());
    }
  }
  else if(action.equalsIgnoreCase("NEW")){
     sSaveEnb  = "enabled";
     com.track.dao.PoHdrDAO _PoHdrDAO=new   com.track.dao.PoHdrDAO();
     _PoHdrDAO.setmLogger(mLogger);
     pono=_PoHdrDAO.getNextOrder(plant);
     collectionDate=du.getDate();
     collectionTime=du.getTimeHHmm();
  
  }
  
%>
<div class="container-fluid m-t-20">
	 <div class="box"> 
            <div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title"><%=title %></h1>
              
              <button class="btn btn-success pull-right" onclick="advPayment()">Advance Payment</button>
		</div>
		
 <div class="box-body">

<input type="number" id="numberOfDecimal" style="display:none;" value=<%=numberOfDecimal%>>
<form class="form-horizontal" id="productform" name="form" method="post" action="/track/purchaseorderservlet?">
   
        
      	 <%
  String statusValue = StrUtils.fString(request.getParameter("statusValue"));
  if(statusValue.equals("100")) {
	  out.println("<div><b><font color='green'><center>Purchase order "+pono+" has sucessfully deleted!</center></font></b></div>");
  }
  if(statusValue.equals("99")) {
	  out.println("<div><b><font color='red'><center>Purchase order "+pono+"  does not exist</center></font></b></div>");
  }
  if(statusValue.equals("98")) {
	  out.println("<div'><b><font color='red'><center>Purchase order  "+pono+" is in use. Cannot Delete!</center></font></b></div>");
  }
  if(statusValue.equals("97")) {
	  out.println("<div'><b><font color='red'><center>Unable to remove the Purchase order "+pono+". Some Error Occured!</center></font></b></div>");
  }
  if(statusValue.equals("96")) {
	  out.println("<div'><b><font color='red'><center>Payment is processed for Purchase order "+pono+". Please void before Deleting the Order!</center></font></b></div>");
  }
  %>
   
   
   		<INPUT type="Hidden" name="DIRTYPE" value="INBOUND">
    	<input type="hidden" name="xlAction" value="">
      	<INPUT type = "hidden" name="SHIPPINGID" value ="<%=shippingid%>">
      	<INPUT type = "hidden" name="TAXTREATMENT" value ="<%=sTAXTREATMENT%>">
		<INPUT type="hidden" name="EDIT_PURCHASE_LOC" value="<%=sPURCHASE_LOC%>">
   
   
<div class="form-group">
       <label class="control-label col-sm-2" for="inbound Order">
   		 	<i class="glyphicon glyphicon-star" style="font-size: 10px; top:-6px; color: #e50000"></i>&nbsp;Order Number:</label>
       <div class="col-sm-3">
      	    <div class="input-group">
    		<INPUT class="form-control" type = "TEXT" size="20"  MAXLENGTH=20 name="PONO" value="<%=pono%>" onKeyPress ="submitToView()" >
   		 	<span class="input-group-addon"  onClick="javascript:popUpWin('po_list_po.jsp?PONO='+form.PONO.value);">
   		 	<a href="#" data-toggle="tooltip" data-placement="top" title="Purchase Details">
   		 	<i  class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span>
  		</div>
  		</div>
  		<div class="form-inline">
  		<div class="col-sm-1 ">   
  		<button type="button" class="Submit btn btn-default" value="View"  onClick="return validatePO(document.form)"><b>View</b></button>
  		</div>
  		</div>
  		<div class="form-inline">
  		<label class="control-label col-sm-2" for="supplier name">
   		 	<i class="glyphicon glyphicon-star" style="font-size: 10px; top:-6px; color: #e50000"></i>&nbsp;Supplier Name/ID:</label>
        <div class="col-sm-3">
      	    <div class="input-group">
    		 <INPUT class="form-control" name="CUST_NAME" type = "TEXT" value="<%=StrUtils.forHTMLTag(custName)%>" size="30"  MAXLENGTH=100>
    		<span class="input-group-addon"  onClick="javascript:popUpWin('vendor_hdrlist.jsp?CUST_NAME='+form.CUST_NAME.value);$('.check').not(this).prop('checked', false);">
   		 	<a href="#" data-toggle="tooltip" data-placement="top" title="Supplier Details">
   		 	<i class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span>
        </div>
        <span><a href="#" title="Tax Treatment" id="TAXTREATMENT"><%=sTAXTREATMENT%></a></span>
        </div>
 		</div>  
 		</div>
 		
 		
 					<INPUT type = "hidden" name="CUST_CODE"  value="<%=custCode%>">
                    <INPUT type = "hidden" name="CUST_CODE1" value="<%=custCode%>">
                    <INPUT type = "hidden" name="LOGIN_USER" value = "<%=sUserId%>">
 			
<div class="form-group">
        <label class="control-label col-sm-2" for="Reference No">Reference No:</label>
        <div class="col-sm-3">
        <INPUT class="form-control" type = "TEXT" size="20"  MAXLENGTH="20" name="JOB_NUM" value="<%=jobNum%>">
       </div>
    	<div class="form-inline">
    	<label class="control-label col-sm-3" for="Person Incharge">Contact Name:</label>
        <div class="col-sm-3">
        <INPUT type = "TEXT" style="width: 100%"  MAXLENGTH="100"  class = "form-control" readonly name="PERSON_INCHARGE" value="<%=personIncharge%>">
        </div>
 		</div>
 		</div>
 		 
<div class="form-group">
        <label class="control-label col-sm-2" for="Order Date">Order Date:</label>
        <div class="col-sm-3">
        <div class="input-group">          
        <INPUT class="form-control datepicker" type="TEXT" size="30" MAXLENGTH="20" name="ORDDATE" value="<%=collectionDate%>"/>
      	</div>
      	 <INPUT type="Hidden" size="10" MAXLENGTH="20" name="COLLECTION_TIME" value="<%=collectionTime%>"/>
    	</div>
    	<!-- <div class="form-inline">
    	<label class="control-label col-sm-3" for="Telephone">Telephone:</label>
        <div class="col-sm-3"> -->
        <INPUT  type="hidden"class="form-control" name="TELNO"  value="<%=telno%>" size="30" MAXLENGTH=100 readonly>
        <!-- </div>
        </div> -->
        <div class="form-inline">
        <label class="control-label col-sm-3" for="Payment Type">Payment Type:</label>
        <div class="col-sm-3">
		<div class="input-group">
        <INPUT name="PAYMENTTYPE" value="<%=paymenttype%>" type="TEXT" value="" class="form-control" style="width: 100%" MAXLENGTH=100>
 		<span class="input-group-addon"  onClick="javascript:popUpWin('list/paymenttypelist_save.jsp?paymenttype='+form.PAYMENTTYPE.value+'&EDIT=EDIT');">
		   		<a href="#" data-toggle="tooltip" data-placement="top" title="Payment Type">
		   		<i class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span>
	  	</div>
		</div>
 		</div>
 		</div>
 
 <div class="form-group">
        <label class="control-label col-sm-2" for="Delivery Date">Delivery Period/Date:</label>
        <div class="col-sm-3">
        <div class="input-group">          
       <INPUT type = "TEXT" size="30"  MAXLENGTH="20" id="DELDATE" name="DELDATE" <%if(dateformat.equals("1")) {%>readonly class="form-control datepicker"<%} else {%> class=form-control <%}%> value="<%=deldate%>">
      	</div>
    	</div>
    	<div class="form-inline">
       	<label class="control-label col-sm-1">
       	<input type = "checkbox" id = "DATEFORMAT" name = "DATEFORMAT" <%if(dateformat.equals("1")) {%>checked <%}%> onClick = "headerReadable();"/><font size="2.9"><b>&nbsp;By Date</b></font></label> 	
    	</div> 
    	<!-- <div class="form-inline">
    	<label class="control-label col-sm-3" for="Email">Email:</label>
        <div class="col-sm-3"> -->
        <INPUT	type="hidden" size="30" class="form-control" readonly MAXLENGTH="20"name="EMAIL" value="<%=email%>" />
        <!-- </div>
        </div> -->
        <div class="form-inine">
 		<label class="control-label col-sm-2" for="Shipping Customer">Shipping Customer:</label>
        <div class="col-sm-3">
        <div class="input-group">
       <INPUT name="SHIPPINGCUSTOMER" type="TEXT" value="<%=StrUtils.forHTMLTag(shippingcustomer)%>" class="form-control" size="30" MAXLENGTH=40>
        <span class="input-group-addon"  onClick="javascript:popUpWin('shippingdetails_list.jsp?SHIPPINGCUSTOMER='+form.SHIPPINGCUSTOMER.value);">
   		 	<a href="#" data-toggle="tooltip" data-placement="top" title="Shipping Customer Details">
   		 	<i class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span>
        </div>
 		</div>
 		</div>
    	</div>
 		
 		
 <div class="form-group">
        <label class="control-label col-sm-2" for="Order Type">Order Type:</label>
        <div class="col-sm-3">
        <div class="input-group">
        <INPUT type="TEXT" size="20" MAXLENGTH="20" class="form-control"	name="ORDERTYPE" value="<%=ordertype%>" />
        <span class="input-group-addon"  onClick="javascript:popUpWin('OrderType_list.jsp?ORDERTYPE='+form.ORDERTYPE.value+'&FORMTYPE=inbound');">
   		 	<a href="#" data-toggle="tooltip" data-placement="top" title="Order Type Details">
   		 	<i class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span>
        </div>
        </div>
        <!-- <div class="form-inline">
    	<label class="control-label col-sm-3" for="Street">Street:</label>
        <div class="col-sm-3"> -->
        <INPUT type="hidden" size="30" MAXLENGTH="20"	class="form-control" readonly name="ADD3" value="<%=add3%>" />
        <!-- </div>
        </div> -->
        <div class="form-inline">
 		<label class="control-label col-sm-3" for="incoterm">INCOTERM:</label>
        <div class="col-sm-3">
        <div class="input-group">
        <INPUT name="INCOTERMS" type="TEXT" value="<%=incoterms%>" class="form-control" size="30" MAXLENGTH=200> 
        <span class="input-group-addon"  onClick="javascript:popUpWin('incoterms_list.jsp?INCOTERMS='+form.INCOTERMS.value);">
   		 	<a href="#" data-toggle="tooltip" data-placement="top" title="INCOTERM Details">
   		 	<i class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span>
        </div>
 		</div>
 		</div>
    	<!-- <div class="form-inline">
    	<label class="control-label col-sm-3" for="Unit No.">Unit No.:</label>
        <div class="col-sm-3"> -->
        <INPUT type="hidden" size="30" class="form-control" readonly MAXLENGTH="20" name="ADD1" value="<%=add1%>" />
        <!-- </div>
        </div> -->
 		</div>
 		
 <div class="form-group">
 		<label class="control-label col-sm-2" for="Currency">Currency:</label>
        <div class="col-sm-3">
        <INPUT class="form-control" type = "TEXT" size="20"   MAXLENGTH=20 name="DISPLAY" value="<%=currencyid%>" readonly>
        </div>
        <!-- <div class="form-inline">
    	<label class="control-label col-sm-3" for="Postal Code">Postal Code:</label>
        <div class="col-sm-3"> -->
        <INPUT type="hidden" size="30" class="form-control" MAXLENGTH="20" name="ZIP"	readonly value="<%=zip%>" />
        <!-- </div>
        </div> -->
        <div class="form-inline">
        <label class="control-label col-sm-3" for="Remarks">Remark1:</label>
        <div class="col-sm-3">
        <div class="input-group">
        <INPUT class="form-control" type="TEXT" size="30" MAXLENGTH="100" name="REMARK1" value="<%=remark1%>"/>
        <span class="input-group-addon"  onClick="javascript:popUpWin('remarks_list.jsp?REMARKS='+form.REMARK1.value+'&TYPE=REMARKS1');">
   		 	<a href="#" data-toggle="tooltip" data-placement="top" title="Remarks Details">
   		 	<i class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span>
        </div>
        </div>
 		</div>
 		<!-- <div class="form-inline">
    	<label class="control-label col-sm-3" for="Building">Building:</label>
        <div class="col-sm-3"> -->
        <INPUT type="hidden" size="30" MAXLENGTH="20"	class="form-control" readonly name="ADD2" value="<%=add2%>" />
        <!-- </div>
        </div> -->
 		</div>
 <INPUT type="hidden" id="TaxByLabelOrderManagement" name="taxbylabelordermanagement" value=<%=taxbylabelordermanagement%>>	
<div class="form-group">
        <label class="control-label col-sm-2" for="rcbno" id="TaxLabelOrderManagement"></label>
        <div class="col-sm-3">
        <div class="input-group">
        <INPUT class="form-control" type = "TEXT" size="20" onkeypress="return isNumberKey(event,this,4)" id="GST" MAXLENGTH=20 name="GST" value="<%=gst%>">
        <span class="input-group-addon" style="font-size: 20px; color: #0059b3"><b>%</b></span>
        </div>
        </div>
        <!-- <div class="form-inline">
    	<label class="control-label col-sm-3" for="City">City:</label>
        <div class="col-sm-3"> -->
        <INPUT type="hidden" size="30" MAXLENGTH="20"	class="form-control" readonly name="ADD4" value="<%=add4%>" />
        <!-- </div>
        </div> -->
        <div class="form-inline">
        <label class="control-label col-sm-3" for="Remarks">Remark2:</label>
        <div class="col-sm-3">
        <div class="input-group">
        <INPUT class="form-control" type="TEXT" size="30" MAXLENGTH="100" name="REMARK3" value="<%=remark2%>"/>
        <span class="input-group-addon"  onClick="javascript:popUpWin('remarks_list.jsp?REMARKS='+form.REMARK3.value+'&TYPE=REMARKS2');">
   		 	<a href="#" data-toggle="tooltip" data-placement="top" title="Remarks Details">
   		 	<i class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span>
        </div>
        </div>
 		</div>
      	
      	<!-- 	<div class="form-inline">
    	<label class="control-label col-sm-3" for="State">State:</label>
        <div class="col-sm-3">
        <INPUT type="TEXT" size="30" MAXLENGTH="20"	class="form-control" readonly name="" value="" />
        </div>
 		</div> -->
 		</div>
 		
<div class="form-group">
    <label class="control-label col-sm-2" for="Shipping Cost">Shipping Cost:</label>
        <div class="col-sm-3">
        <INPUT class="form-control" type = "TEXT" size="20" onkeypress="return isNumberKey(event,this,6)" id="SHIPPINGCOST" MAXLENGTH=20 name="SHIPPINGCOST" value="<%=new java.math.BigDecimal(shippingcost).toPlainString()%>">
        </div>
 		
 		<!-- <label class="control-label col-sm-2" for="Order Status">Order Status:</label>
        <div class="col-sm-3">
        <div class="input-group"> -->
        <INPUT name="STATUS_ID" type="hidden" value="<%=statusID%>" class="form-control" size="20" MAXLENGTH=50>
        <!-- <span class="input-group-addon"  onClick="javascript:popUpWin('OrderStatusList.jsp?ORDERSTATUS='+form.STATUS_ID.value+'&TYPE=IB');">
   		 	<a href="#" data-toggle="tooltip" data-placement="top" title="Order Status Details">
   		 	<i class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span>
        </div>
 		</div> -->
 		<!-- <div class="form-inline">
    	<label class="control-label col-sm-3" for="Supplier Remarks">Supplier Remarks:</label>
        <div class="col-sm-3"> -->
        <INPUT type ="hidden" size="30" class = "form-control" readonly MAXLENGTH=20 name="REMARK2" value="<%=remarks%>">
        <!-- </div>
        </div> -->
        <div class="form-inline">
 		<label class="control-label col-sm-3" for="Order Discount">Order Discount:</label>
        <div class="col-sm-3">
       <div class="input-group">
       <INPUT class="form-control" type = "TEXT" size="30"  onkeypress="return isNumberKey(event,this,4)" id="ORDERDISCOUNT" MAXLENGTH=20 name="ORDERDISCOUNT" value="<%=orderdiscount%>">
       <span class="input-group-addon" style="font-size: 20px; color: #0059b3"><b>%</b></span>
        </div>
      	</div>
      	</div>
 	 	 		 <input type="hidden" name="CURRENCY_ID">
        		 <input type="hidden" name="DESC">
        		 
        <!-- <div class="form-inline">
    	<label class="control-label col-sm-3" for="Country">Country:</label>
        <div class="col-sm-3"> -->
        <INPUT type="hidden" size="30" class="form-control" MAXLENGTH="20" name="COUNTRY" readonly value="<%=country%>" /> 
        <!-- </div>
        </div> -->
 		</div>
 		
 		
 <div class="form-group">
 <label class="control-label col-sm-2" for="Local Expenses">Local Expenses:</label>
        <div class="col-sm-3">
        <INPUT class="form-control" type = "TEXT" size="20"  onkeypress="return isNumberKey(event,this,6)" MAXLENGTH=20 id="LOCALEXPENSES" name="LOCALEXPENSES" value="<%=new java.math.BigDecimal(localexpenses).toPlainString()%>">
        </div>
        <div class="form-inline">
        <div class="col-sm-1">
        </div>
        <div class="col-sm-5" style="text-align: right;">
        <div id="CHK1">        
      	<input type = "checkbox" class="check" id = "REVERSECHARGE" name = "REVERSECHARGE" <%if(sREVERSECHARGE.equals("1")) {%>checked <%}%>/><b>&nbsp;This transaction is applicable for reverse charge </b>
      	</br>
      	<input type = "checkbox" class="check" id = "GOODSIMPORT" name = "GOODSIMPORT" <%if(sGOODSIMPORT.equals("1")) {%>checked <%}%>/><b>&nbsp;This transaction is applicable for goods import &nbsp; &nbsp; </b>
      	</div>
      	</div>
      	</div>      	
      	</div>
      	
      	<div class="form-group">
 		<label class="control-label col-sm-2" for="Purchase Location">Purchase Location:</label>
        <div class="col-sm-3 ac-box">
				<SELECT class="form-control" data-toggle="dropdown" data-placement="right" id="PURCHASE_LOC" name="PURCHASE_LOC" value="<%=sPURCHASE_LOC%>" style="width: 100%">
				<OPTION style="display:none;"></OPTION>
				<%
		   MasterUtil _MasterUtil=new  MasterUtil();
				ArrayList ccList =  _MasterUtil.getSalesLocationList("",plant,"");
				for(int i=0 ; i<ccList.size();i++)
	      		 {
					Map m=(Map)ccList.get(i);
					String STATE = (String)m.get("STATE"); %>
			        <option  value= '<%=STATE%>' ><%=STATE %> </option>		          
			        <%
	       			}
			 %></SELECT>
        </div>
        <div class="form-inline">        
      	<div class="col-sm-offset-2 col-sm-5">
      	<button type="button" class="Submit btn btn-default" onClick="onClear();"><b>Clear</b></button>&nbsp;&nbsp;
      	<button type="button" class="Submit btn btn-default" onClick="return onUpdate(document.form)"><b>Save</b></button>&nbsp;&nbsp;
      	<button type="button" class="Submit btn btn-default" onClick="return onDelete(document.form);"><b>Delete</b></button>&nbsp;&nbsp;
      	</div>
      	      
      	</div>
 		</div>
 			  		
  		 <TABLE style="font-size:15px" BORDER="0" CELLSPACING="0" WIDTH="100%" class="table" >
         <thead style="background: #eaeafa">
         <tr>
          <th width="5%">Order Line No</th>
          <th width="15%">Product ID </th>
          <th width="15%">Description </th>
          <th width="15%">Detail Description</th>
          <th width="7%">Unit Cost </th>
          <th width="10%">Manufacturer </th>
          <th width="7%">Order Quantity </th>
          <th width="7%">Receive Quantity</th>
          <th width="6%"><%=IDBConstants.UOM_LABEL%> </th>
          <th width="5%">Status </th>
        </tr>
       	 </thead>
       	 <tbody>
       	<%=fieldDesc%>
         </tbody>
       </TABLE>
       
       
         <INPUT type = "hidden" size="20"  MAXLENGTH=20 name="CONTACT_NUM" value="<%=contactNum%>">
         <INPUT type="Hidden" name="ENCRYPT_FLAG" value="1">
         <INPUT type="Hidden" name="RFLAG" value="4">
  		 
          		
  		<div class="form-group">        
      	<div class="col-sm-12" align="center">
      	<button type="button" class="Submit btn btn-default" onClick="window.location.href='../home'"><b>Back</b></button>&nbsp;&nbsp;
        <!-- <button type="button" class="Submit btn btn-default" onClick="{backNavigation('orderManagement.jsp','IBO');}"><b>Back</b></button>&nbsp;&nbsp; -->
      	<button type="button" class="Submit btn btn-default" onClick="submitForm('Add Products');"><b>Add Products</b></button>&nbsp;
      	<button type="button" class="Submit btn btn-default" onClick= "submitFormPrint('Print Inbound Order');"><b>Print Purchase Order</b></button>&nbsp;
        <button type="button" class="Submit btn btn-default" onClick="submitFormPrint('Print Inbound Order With Cost');"><b>Print Purchase Order With Cost</b></button>&nbsp;
        <button type="button" class="Submit btn btn-default" onClick="submitForm('Export To Excel');"><b>Export To Excel</b></button>&nbsp;	        
        <button type="button" class="Submit btn btn-default" onClick="if(validateform(document.form)) {submitForm('Copy IB');}"><b>Convert To Purchase</b></button>&nbsp;
      	<button type="button" class="Submit btn btn-default" onClick="if(validateform(document.form)) {CopyIBtoOBsubmitForm('Convert OB');}"><b>Convert To Sales</b></button>
        </div>
        </div>
                        <div class="form-group">  
        				<div class="form-inline">
					<label for="email">Attach Files(s)</label>
					<div class="attch-section">
						<input type="file" class="form-control input-attch" id="supplierAttch" name="file" multiple="true">
						<div class="input-group">
							<svg version="1.1" id="Layer_1" xmlns="http://www.w3.org/2000/svg" x="0" y="0" viewBox="0 0 512 512" xml:space="preserve" class="icon icon-xs align-text-top action-icons input-group-addon" style="height:30px;display:inline-block;color: #c63616;"><path d="M262.4 512c-35.7 0-64.2-10.5-84.9-31.4-24.7-24.9-37.1-63.8-36.8-115.6.2-32.6-.3-248.7-1.4-268.1-.9-15.9 4.8-41.6 23.6-61.5 11.4-12 32-26.3 66-26.3 30.3 0 55.1 15.7 69.8 44.2 10.1 19.7 12.2 38.8 12.4 40.9l.1.9v.9l.5 236.9v.5c-1 19.2-15.9 47.6-53 47.6h-.7c-39.1-.4-53.7-30.4-56-46.2l-.2-1.3V179.6c0-10.2 8.3-18.5 18.5-18.5s18.5 8.3 18.5 18.5v150.5c.4 1.5 1.4 4.4 3.6 7.2s6.2 6.5 16 6.6c9.2.1 12.4-3.2 14.1-6 1.6-2.6 2.2-5.6 2.3-6.3l-.7-234.5c-.4-3-2.4-15.6-8.8-27.6-8.3-15.7-20.2-23.3-36.4-23.3-16.7 0-29.8 5-39.1 14.8-10.7 11.3-14 26.6-13.6 34 1.2 21.6 1.6 244.3 1.4 270.3-.2 41.6 8.5 71.7 26 89.3 13.5 13.6 33.2 20.4 58.7 20.4 17.2 0 31.8-5.9 43.5-17.7 18.9-18.9 30.1-53.4 30-92.2 0-19.6-.1-193.2-.1-250.9 0-10.2 8.3-18.5 18.5-18.5s18.5 8.3 18.5 18.5c0 57.6.1 231.2.1 250.8.1 49.1-14.8 92.3-40.8 118.4-18.6 18.7-42.7 28.6-69.6 28.6z"></path></svg>
							<button type="button" class="btn btn-sm btn-attch">Upload File</button>
						</div>
						
					</div>
				</div>
					<%if(supplierattachlist.size()>0){ %>
						<div id="supplierAttchNote">
							<small class="text-muted"><div class="attachclass"><a><%=supplierattachlist.size()%> files Attached</a>
									<div class="tooltiptext" style="width: 30%">
										
										<%for(int i =0; i<supplierattachlist.size(); i++) {   
									  		Map attach=(Map)supplierattachlist.get(i); %>
												<div class="row" style="padding-left:10px;padding-top:10px">
													<span class="text-danger col-sm-3">
														<%if(attach.get("FileType").toString().equalsIgnoreCase("application/pdf")) {%>
														<svg version="1.1" id="Layer_1" xmlns="http://www.w3.org/2000/svg" x="0" y="0" viewBox="0 0 512 512" xml:space="preserve" class="icon doc-icon icon-xxxlg"><path d="M314.2 318.9c-6.4-3.7-13-7.7-18.2-12.5-13.9-13-25.5-31.1-32.7-50.8.5-1.9.9-3.5 1.3-5.2 0 0 7.8-44.5 5.8-59.6-.3-2.1-.5-2.6-1-4.3l-.7-1.8c-2.1-4.9-6.3-10.6-12.9-10.4l-3.8-.6h-.1c-7.3 0-13.3 4.2-14.8 9.9-4.8 17.5.2 43.9 9 77.9l-2.2 5.7c-6.3 15.5-14.4 31.2-21.4 44.9l-.9 1.9c-7.4 14.5-14.2 26.8-20.2 37.2l-6.2 3.3c-.5.2-11.2 6-13.8 7.4-21.4 12.8-35.6 27.3-38 38.9-.8 3.7-.2 8.4 3.6 10.5l6.1 3c2.6 1.4 5.4 2 8.3 2 15.2 0 33-19 57.4-61.5 28.2-9.2 60.3-16.8 88.4-21 21.4 12 47.8 20.4 64.5 20.4 2.9 0 5.5-.3 7.6-.9 3.2-.8 5.9-2.6 7.5-5.1 3.2-4.9 3.9-11.5 3-18.5-.3-2.1-1.9-4.6-3.6-6.2-4.9-4.9-15.9-7.4-32.5-7.6-11.6 0-25.2 1-39.5 3zM158 405c2.8-7.6 13.8-22.7 30.1-36 1.1-.8 3.5-3.2 5.9-5.4-17.1 27.1-28.5 38-36 41.4zm96.5-222.2c4.9 0 7.7 12.4 7.9 23.9.2 11.6-2.4 19.7-5.9 25.8-2.8-8.9-4.1-22.9-4.1-32.1 0 0-.2-17.6 2.1-17.6zm-28.8 158.3c3.4-6.2 6.9-12.6 10.6-19.4 8.9-16.7 14.5-29.9 18.7-40.6 8.3 15 18.6 27.8 30.8 38.2 1.5 1.3 3.1 2.5 4.8 3.8-24.9 4.8-46.2 10.8-64.9 18zm148.1-9.1c8.8 2.2 8.9 6.7 7.4 7.7s-5.8 1.5-8.6 1.5c-8.9 0-20-4.1-35.4-10.7 6-.5 11.4-.7 16.3-.7 8.9 0 11.5 0 20.3 2.2z"></path><path d="M441.6 116.6L329 4.7c-3-3-7.1-4.7-11.3-4.7H94.1C76.5 0 62.4 14.2 62.4 31.7v448.5c0 17.5 14.2 31.7 31.7 31.7h320.6c17.3 0 31.3-14 31.4-31.3l.3-352.7c-.1-4.1-1.8-8.2-4.8-11.3zm-14.9 358c0 9.4-7.8 17.1-17.3 17.1H99.2c-9.5 0-17.3-7.7-17.3-17.1V36.3c0-9.4 7.8-17.1 17.3-17.1h172.4c9.5 0 17.3 7.7 17.3 17.1v83.5c0 18.7 14.7 33.8 34.1 33.8h86.5c9.5 0 17.3 7.7 17.3 17.1l-.1 303.9zM326.8 136c-10.8 0-19.6-8.8-19.6-19.6V24.6c0-4.4 5.3-6.5 8.3-3.4l106.6 106.5c3.1 3.1.9 8.3-3.4 8.3h-91.9z"></path></svg>
														<%}else if(attach.get("FileType").toString().equalsIgnoreCase("image/jpeg") || attach.get("FileType").toString().equalsIgnoreCase("image/png") || attach.get("FileType").toString().equalsIgnoreCase("image/gif") || attach.get("FileType").toString().equalsIgnoreCase("image/tiff")){ %>
														<svg version="1.1" id="Layer_1" xmlns="http://www.w3.org/2000/svg" x="0" y="0" viewBox="0 0 512 512" xml:space="preserve" class="icon doc-icon icon-xxxlg"><path d="M417.2 378.8H95.3c-7 0-12.8-5.7-12.8-12.8v-34.9c0-2.7.8-5.2 2.3-7.4l44.6-63c4-5.6 11.6-7 17.4-3.3l60.8 39.7c4.9 3.2 11.1 2.7 15.5-1.1l116.8-103.2c5.5-4.9 14.1-4.1 18.5 1.8l66.3 86c1.7 2.2 2.7 5 2.7 7.8v80.2c0 5.6-4.6 10.2-10.2 10.2z" fill="#40bab5"></path><path d="M212.2 157.7c23.2 0 42 19 42 42.4s-18.8 42.4-42 42.4-42-19-42-42.4c.1-23.4 18.9-42.4 42-42.4z" fill="#fbbe01"></path><path d="M462 60.8c16.5 0 30 13.5 30 30V422c0 16.5-13.5 30-30 30H50.4c-16.5 0-30-13.5-30-30V90.8c0-16.5 13.5-30 30-30H462m0-20H50.4c-27.6 0-50 22.4-50 50V422c0 27.6 22.4 50 50 50H462c27.6 0 50-22.4 50-50V90.8c0-27.6-22.4-50-50-50z" fill="#888"></path></svg>
														<%} else{%>
														<svg version="1.1" id="Layer_1" xmlns="http://www.w3.org/2000/svg" x="0" y="0" viewBox="0 0 512 512" xml:space="preserve" class="icon doc-icon icon-xxxlg"><path d="M270.825,70.55L212.17,3.66C210.13,1.334,207.187,0,204.093,0H55.941C49.076,0,43.51,5.566,43.51,12.431V304.57  c0,6.866,5.566,12.431,12.431,12.431h205.118c6.866,0,12.432-5.566,12.432-12.432V77.633  C273.491,75.027,272.544,72.51,270.825,70.55z M55.941,305.073V12.432H199.94v63.601c0,3.431,2.78,6.216,6.216,6.216h54.903  l0.006,222.824H55.941z"></path></svg>	
														<%} %>
													</span>
													<div class="col-sm-9" style="padding-left:16px"><span class="fileNameFont"><a><%=attach.get("FileName").toString() %></a></span><br><span class="fileTypeFont">File Size: <%=Integer.parseInt(attach.get("FileSize").toString())/1024 %>KB</span></div>
												</div>
												<div class="row bottomline">
														<span class="col-sm-6" Style="font-size:14px;"><i class="fa fa-download" aria-hidden="true" onclick="downloadFile(<%=attach.get("ID") %>,'<%=(String) attach.get("FileName") %>')"> Download</i></span>
														<span class="col-sm-6" Style="font-size:14px;float:right"><i class="fa fa-trash" aria-hidden="true" onclick="removeFile(<%=attach.get("ID") %>)"> Remove</i></span>
												</div>	
										<%} %>
										
									</div>
								</div>
								
							</small>
						</div>
						<%}else{ %>
						<div id="supplierAttchNote">
							<small class="text-muted">  You can upload a maximum of 5 files, 2MB each  </small>
						</div>
						<%} %>
        </div>
        
  		</form>
		</div>
		</div>
		</div>
		
<% 

 //logger.log(-1,"CreateIncomingOredr.jsp  ");
%>
<script>
$(document).ready(function(){
	$("select[name ='PURCHASE_LOC']").val(document.form.EDIT_PURCHASE_LOC.value);
	document.getElementById('CHK1').style.display = 'none';	
	if(document.form.TAXTREATMENT.value =="GCC VAT Registered"||document.form.TAXTREATMENT.value=="GCC NON VAT Registered"||document.form.TAXTREATMENT.value=="NON GCC")
	{
		document.getElementById('CHK1').style.display = 'block';
	}
	$('.check').click(function() {
        $('.check').not(this).prop('checked', false);
    });
    $('[data-toggle="tooltip"]').tooltip();

    var  d = document.getElementById("TaxByLabelOrderManagement").value;
    document.getElementById('TaxLabelOrderManagement').innerHTML = "Purchase " + d +" :";
});

/* function convertToBill(){
	var pono = $("input[name ='PONO']").val();
	var vendno = $("input[name ='CUST_CODE']").val();
	var vendname = $("input[name ='CUST_NAME']").val();
	window.location.href = "createBill.jsp?PONO="+pono+"&VENDNO="+vendno+"&VEND_NAME="+vendname;
	
} */

function advPayment(){
	var pono = $("input[name ='PONO']").val();
	if(pono == ""){
		alert("Please Enter Order Number!");
		form.PONO.focus();
	}else{
		window.location.href = "quickBillPayment.jsp?type=ADVANCE&pono="+pono;		
	}
}
function downloadFile(id,fileName)
{
	 var urlStrAttach = "/track/PurchaseAttachmentServlet?Submit=downloadAttachmentById&attachid="+id;
	 var xhr=new XMLHttpRequest();
	 xhr.open("POST", urlStrAttach, true);
	 //Now set response type
	 xhr.responseType = 'arraybuffer';
	 xhr.addEventListener('load',function(){
	   if (xhr.status === 200){
	     console.log(xhr.response) // ArrayBuffer
	     console.log(new Blob([xhr.response])) // Blob
	     var datablob=new Blob([xhr.response]);
	     var a = document.createElement('a');
         var url = window.URL.createObjectURL(datablob);
         a.href = url;
         a.download = fileName;
         document.body.append(a);
         a.click();
         a.remove();
         //window.URL.revokeObjectURL(url); 
	   }
	 })
	 xhr.send();
}
function removeFile(id)
{
	var urlStrAttach = "/track/PurchaseAttachmentServlet?Submit=removeAttachmentById&removeid="+id;	
	$.ajax( {
		type : "POST",
		url : urlStrAttach,
		success : function(data) {
					window.location.reload();
				}
			});
}

$("#supplierAttch").change(function(){
	var files = $(this)[0].files.length;
	var sizeFlag = false;
		if(files > 5){
			$(this)[0].value="";
			alert("You can upload only a maximum of 5 files");
			$("#billAttchNote").html("<small class='text-muted'>  You can upload a maximum of 5 files, 2MB each  </small>");
		}else{
			for (var i = 0; i < $(this)[0].files.length; i++) {
			    var imageSize = $(this)[0].files[i].size;
			    if(imageSize > 2097152 ){
			    	sizeFlag = true;
			    }
			}	
			if(sizeFlag){
				$(this)[0].value="";
				alert("Maximum file size allowed is 2MB, please try with different file.");
				$("#supplierAttchNote").html("<small class='text-muted'>  You can upload a maximum of 5 files, 2MB each  </small>");
			}else{
				$("#supplierAttchNote").html(files +" files attached");
				/* $("#supplierAttchNote").append('<br><br><button onclick="add_attachments()">Upload Supplier Attachments</button>'); */
			}
			
		}
	});
function add_attachments(){
    var formData = new FormData($('#productform')[0]);
    var userId= form.PONO.value;
	if(userId){
    $.ajax({
        type: 'post',
        url: "/track/PurchaseAttachmentServlet?Submit=add_attachments",
	    data:  formData,//{key:val}
	    contentType: false,
	    processData: false,
        success: function (data) {
        	console.log(data);
        //	window.location.reload();
        	document.form.action  = "/track/purchaseorderservlet?Submit=Update";
         document.form.submit();
        },
        error: function (data) {
            alert(data.responseText);
        }
    });
	}else{
		alert("Please enter Order NO");
	}
        return false; 
  }
  
</script>
<jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
</jsp:include>
 
