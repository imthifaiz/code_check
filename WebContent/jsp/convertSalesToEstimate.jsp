<%@ include file="header.jsp"%>
<%@ page import="com.track.constants.*"%>
<%@ page import="com.track.dao.*"%>
<%@ page import="com.track.db.util.*"%>

  <%
String title = "Create Sales Estimate Order";
%>
<%@include file="sessionCheck.jsp" %>
<jsp:include page="header2.jsp" flush="true">
<jsp:param name="title" value="<%=title %>" />
<jsp:param name="mainmenu" value="<%=IConstants.ESTIMATE_ORDER%>"/>
 <jsp:param name="submenu" value="<%=IConstants.ESTIMATE_REPORTS%>"/>
</jsp:include>

<script type="text/javascript" src="js/calendar.js"></script>
<script type="text/javascript" src="js/general.js"></script>
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
var isSaveClicked=false;
var subWin = null;

function popUpWin(URL) {
 subWin = window.open(encodeURI(URL), 'Items', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=800,height=400,left = 200,top = 184');
}

function sf()
{
document.form.ESTNO.focus();
}

function validatePO(form)
{



  if (form.ESTNO.value.length < 1)
  {
    alert("Please Enter Order Number !");
    form.ESTNO.focus();
    return false;
  }
 
  if (form.CUST_NAME.value.length < 1)
  {
    alert("Please Enter Customer !");
    form.CUST_NAME.focus();
    return false;
  }

  if(!IsNumeric(form.GST.value))
  {
    alert(" Please Enter valid  VAT !");
    form.GST.focus();  form.GST.select(); return false;
  }
   
  
    if (form.DELDATE.value.length > 1){
    if (isDate(form.DELDATE.value)==false){
		form.DELDATE.focus();
		return false;
    }
   }
    if (!validToThreeDecimal(document.getElementById("GST").value)) {
		alert("Not more than 3 digits are allowed after decimal value in Vat");
		form.GST.focus();
		return false;
	} 
	else if (!validDecimal(document.getElementById("SHIPPINGCOST").value)) {
	alert("Not more than 5 digits are allowed after decimal value in Shipping Cost");
	document.form.SHIPPINGCOST.focus();
	return false;
}
	else if (!validDecimal(document.getElementById("ORDERDISCOUNT").value)) {
		alert("Not more than 5 digits are allowed after decimal value in Order Discount");
		document.form.ORDERDISCOUNT.focus();
		return false;
	} 
   if(isSaveClicked) {
        if (form.CUST_CODE.value.length < 1)
      {
        alert("Please select valid Customer Name !");
        form.CUST_NAME.focus();
        form.CUST_NAME.select();
        return false;
      }
        if (form.DISPLAY.value.length < 1)
	    {
	    	alert("Please Select Currency ID");
	    	form.DISPLAY.focus();
                form.DISPLAY.select();
	    	return false;
			    
	    }
    }
  
}

function onSavedClicked(){

isSaveClicked = true;

  if (form.ESTNO.value.length < 1)
  {
    alert("Please Enter Order Number !");
    form.ESTNO.focus();
    return false;
  }
 
  if (form.CUST_NAME.value.length < 1)
  {
    alert("Please Enter Customer !");
    form.CUST_NAME.focus();
    return false;
  }

  if(!IsNumeric(form.GST.value))
  {
    alert(" Please Enter valid  VAT !");
    form.GST.focus();  form.GST.select(); return false;
  }
   
  
    if (form.DELDATE.value.length > 1){
    if (isDate(form.DELDATE.value)==false){
		form.DELDATE.focus();
		return false;
    }
   }
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
   if(isSaveClicked) {
        if (form.CUST_CODE.value.length < 1)
      {
        alert("Please select valid Customer Name !");
        form.CUST_NAME.focus();
        form.CUST_NAME.select();
        return false;
      }
        if (form.DISPLAY.value.length < 1)
	    {
	    	alert("Please Select Currency ID");
	    	form.DISPLAY.focus();
                form.DISPLAY.select();
	    	return false;
			    
	    }
    }
document.form.action = "/track/EstimateServlet?Submit=SaveEstData";
document.form.submit();
}
function onNew(form)
{
   
    document.form.ESTNO.value   =""
    document.form.CUST_NAME.value       =""
    form.JOB_NUM.value         =""
    form.PERSON_INCHARGE.value =""
    form.CONTACT_NUM.value     =""
    form.DELDATE.value         =""
    form.COLLECTION_TIME.value =""
    form.REMARK1.value         =""
    form.REMARK2.value         =""
    form.ORDERTYPE.value="";
    form.STATUS_ID.value=""; //orderstaus
    form.DELIVERYDATE.value="";
	form.SHIPPINGID.value="";
	form.SHIPPINGCUSTOMER.value="";
	form.ORDERDISCOUNT.value="";
	form.SHIPPINGCOST.value="";
	form.INCOTERMS.value="";  
	 form.PAYMENTTYPE.value="";
	 form.TAXTREATMENT.value="";
	 document.getElementById('TAXTREATMENT').innerHTML="";
    document.form.action = "/track/EstimateServlet?Submit=Auto-Generate";
    document.form.submit();
  
    
}

function onClear(){
	 
	  document.form.ESTNO.value   =""
	  document.form.CUST_NAME.value       =""
	  form.JOB_NUM.value         =""
	  form.PERSON_INCHARGE.value =""
	  form.CONTACT_NUM.value     =""
	  form.JOB_NUM.value         =""
	 // form.DELDATE.value         =""
	 // form.COLLECTION_TIME.value =""
	  form.REMARK1.value         ="";
	  form.REMARK2.value         ="";
	  form.REMARK3.value         ="";
	  form.EMP_NAME.value = ""
	  form.EXPIREDATE.value = ""
	  form.CUST_NAME.value         =""
	  form.PERSON_INCHARGE.value   =""
	  form.TELNO.value         =""
	  form.EMAIL.value         =""
	  form.ADD1.value         =""
	  form.ADD2.value         =""
	  form.ADD3.value         =""
	  form.ADD4.value         =""
	  form.COUNTRY.value         =""
	  form.ZIP.value         =""
	  form.ORDERTYPE.value="";
      form.STATUS_ID.value=""; //orderstaus
      form.DELIVERYDATE.value="";
	  form.SHIPPINGID.value="";
	  form.SHIPPINGCUSTOMER.value="";
	  form.ORDERDISCOUNT.value="";
	  form.SHIPPINGCOST.value="";
	  form.INCOTERMS.value="";  
	  form.PAYMENTTYPE.value="";
	  form.TAXTREATMENT.value="";
	  document.getElementById('TAXTREATMENT').innerHTML="";
}

function submitForm(actionvalue)
{
    form.removeAttribute("target", "_blank");
	document.form.action = "/track/EstimateServlet?Submit="+actionvalue;
    document.form.submit();
}

function submitFormPrint(actionvalue)
{
	form.setAttribute("target", "_blank");
	document.form.action = "/track/EstimateServlet?Submit="+actionvalue;
    document.form.submit();
}

function headerReadable(){
	if(document.form.DATEFORMAT.checked)
	{
		
		document.form.DELIVERYDATE.value="";
		$('#DELIVERYDATE').attr('readonly',true).datepicker({ dateFormat: 'dd/mm/yy'});
		
	}
	else{
		document.form.DELIVERYDATE.value="";
		 $('#DELIVERYDATE').attr('readonly',false).datepicker("destroy");

	}
} 

</script>

<jsp:useBean id="sb" class="com.track.gates.selectBean" />
<jsp:useBean id="gn" class="com.track.gates.Generator" />
<jsp:useBean id="sl" class="com.track.gates.selectBean" />
<jsp:useBean id="db" class="com.track.gates.defaultsBean" />
<jsp:useBean id="su" class="com.track.util.StrUtils" />
<jsp:useBean id="vmb" class="com.track.tables.VENDMST" />
<jsp:useBean id="phb" class="com.track.tables.POHDR" />
<jsp:useBean id="pdb" class="com.track.tables.PODET" />
<jsp:useBean id="logger" class="com.track.util.MLogger" />
<jsp:useBean id="du" class="com.track.util.DateUtils" />
<jsp:useBean id="ub"  class="com.track.gates.userBean" />

<%
	String DIRTYPE ="";
	
	session.setAttribute("RFLAG", "1");

	String vend = "", deldate = "", jobNum = "", custName = "",ordertype="", custCode = "", personIncharge = "", contactNum = "";
	String  address = "", address2 = "", address3 = "", collectionDate = "", collectionTime = "";
	String contactname = "", telno = "", email = "",currencyid="", add1 = "", add2 = "", add3 = "", add4 = "", country = "", zip = "",
		  remark1 = "", remark2 = "", remarks="",gst="", sSaveEnb = "disabled",statusID="",EMP_NAME = "",EXPIREDATE="" ;
	String deliverydate="",customerstatusid="",customerstatusdesc="",
	       customertypeid="",customertypedesc="",paymenttype="",sTAXTREATMENT="";
    String shippingid="",shippingcustomer="",orderdiscount="",shippingcost="",incoterms="",dateformat="";
    String fitem="",floc="",floc_type_id="",floc_type_id2="",fmodel="",fuom="";
	String fieldDesc = "<tr><td colspan=\"7\" align=\"center\">Please enter any search criteria</td></tr>";
	String detList="";
	String isAutoGenerate = "false";
	String plant = (String) session.getAttribute("PLANT");
	String estno = su.fString(request.getParameter("ESTNO"));
	String action = su.fString(request.getParameter("action")).trim();
	String sUserId = (String) session.getAttribute("LOGIN_USER");
	DIRTYPE = su.fString(request.getParameter("DIRTYPE"));
	customerstatusdesc=su.fString(request.getParameter("CUSTOMERSTATUSDESC"));
	customertypedesc=su.fString(request.getParameter("CUSTOMERTYPEDESC"));	
	custName = su.fString(request.getParameter("CUSTNAME"));
	custCode = su.fString(request.getParameter("CUSTCODE"));
	sTAXTREATMENT = su.fString(request.getParameter("TAXTREATMENT"));
	personIncharge = su.fString(request.getParameter("NAME"));
	customertypedesc = su.fString(request.getParameter("CTYPE"));
	fitem = su.fString(request.getParameter("FITEM"));
	floc = su.fString(request.getParameter("FLOC"));
	floc_type_id = su.fString(request.getParameter("FLOC_TYPE_ID"));
	floc_type_id2 = su.fString(request.getParameter("FLOC_TYPE_ID2"));
	fmodel = su.fString(request.getParameter("FMODEL"));
	fuom = su.fString(request.getParameter("FUOM"));
	
	String taxbylabelordermanagement = ub.getTaxByLableOrderManagement(plant);
	if(DIRTYPE.length()<=0){
   	 DIRTYPE = "ESTIMATE";
   	 }
	sb.setmLogger(mLogger);
	 gst = sb.getGST("SALES ESTIMATE",plant);
	 
	 deldate=du.getDate();

	CustomerBeanDAO customerBeanDAO = new CustomerBeanDAO();
	
	PlantMstDAO _PlantMstDAO = new PlantMstDAO();
	String basecurrency=_PlantMstDAO.getBaseCurrency(plant);
	if(currencyid.length()<0||currencyid==null||currencyid.equalsIgnoreCase(""))currencyid=basecurrency;
	
	String numberOfDecimal = _PlantMstDAO.getNumberOfDecimal(plant);
	float gstVatValue ="".equals(gst) ? 0.0f :  Float.parseFloat(gst);
 	/* float shipingCostValue ="".equals(shippingcost) ? 0.0f :  Float.parseFloat(shippingcost); */
 	double shipingCostValue ="".equals(shippingcost) ? 0.0d :  Double.parseDouble(shippingcost);
 	float orderDiscountValue ="".equals(orderdiscount) ? 0.0f :  Float.parseFloat(orderdiscount);
	
	if(gstVatValue==0f){
		gst="0.000";
	}else{
		gst=gst.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
	}if(orderDiscountValue==0f){
		orderdiscount="0.000";
	}else{
		orderdiscount=orderdiscount.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
	}/* if(shipingCostValue==0f){
		shippingcost="0.00000";
	}else{
		shippingcost=shippingcost.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
	} */
	
	if (action.equalsIgnoreCase("View")) {
		//Map m = (Map) request.getSession().getAttribute("podetVal");
		fieldDesc = (String) request.getSession().getAttribute("RESULT");
		detList = (String) request.getSession().getAttribute("DETLIST");
		logger.log(0, "fieldDesc : " + fieldDesc.length());
		
		if(custCode.length() > 0){
			CustUtil custUtils = new CustUtil();
			ArrayList arrCust = custUtils.getCustomerListStartsWithName(custCode,plant);	
			if (arrCust.size() > 0) {
               for(int i =0; i<arrCust.size(); i++) {
                   Map arrCustLine = (Map)arrCust.get(i);
                   personIncharge = (String)arrCustLine.get("NAME");
                   customertypedesc = (String)arrCustLine.get("CUSTOMER_TYPE_ID");
                   sTAXTREATMENT = (String)arrCustLine.get("TAXTREATMENT");
               }
			}
		}
		
		//logger.log(0, "m : " + m.size());

		/*if (m.size() > 0) {
			
			currencyid = (String) m.get("currencyid");
			jobNum = (String) m.get("jobNum");
			custName = (String) m.get("custName");
			custCode = (String) m.get("custCode");
			personIncharge = (String) m.get("contactname");
			contactNum = (String) m.get("contactNum");
			gst=(String)m.get("outbound_Gst");
			telno = (String) m.get("telno");
			email = (String) m.get("email");
			add1 = (String) m.get("add1");
			add2 = (String) m.get("add2");
			add3 = (String) m.get("add3");
			add4 = (String) m.get("add4");
			country = (String) m.get("country");
			zip = (String) m.get("zip");
			customerstatusid  = (String) m.get("customerstatusid");
			if(customerstatusid == null || customerstatusid.equals("")||customerstatusid.equals("NOCUSTOMERSTATUS"))
			{
			   customerstatusdesc="";
			}
			else
			{
			  customerstatusdesc = customerBeanDAO.getCustomerStatusDesc(plant,customerstatusid);
			}
			customertypeid  = (String) m.get("customertypeid");
		    if(customertypeid == null || customertypeid.equals("")|| customertypeid.equals("NOCUSTOMERTYPE"))
			{
				customertypedesc="";
			}
			else
			{
				customertypedesc = customerBeanDAO.getCustomerTypeDesc(plant,customertypeid);
			}
			ordertype = (String) m.get("ordertype");
			contactNum = (String) m.get("contactNum");
			address = (String) m.get("address");
			address2 = (String) m.get("address2");
			address3 = (String) m.get("address3");
			deldate = (String) m.get("collectionDate");
			collectionTime = (String) m.get("collectionTime");
			deldate=(String)m.get("deldate");
			remark1 = (String) m.get("remark1");
			remark2 = (String) m.get("remark3");
			remarks = (String) m.get("remarks");
			deldate=(String) m.get("collectionDate");
			deliverydate=(String) m.get("deliverydate");
			EMP_NAME = (String) m.get("empno");
			statusID  = (String)m.get("statusid");
            shippingid= (String) m.get("shippingid");
			shippingcustomer = (String) m.get("shippingcustomer");
			orderdiscount = (String) m.get("orderdiscount");
			shippingcost= (String) m.get("shippingcost");
			incoterms = (String) m.get("incoterms");
			EXPIREDATE = (String) m.get("expiredate");
			paymenttype=(String) m.get("payment_terms");
			dateformat= (String) m.get("DELIVERYDATEFORMAT");
			sTAXTREATMENT= (String) m.get("TAXTREATMENT");
			
			gstVatValue ="".equals(gst) ? 0.0f :  Float.parseFloat(gst);
		 	
		 	shipingCostValue ="".equals(shippingcost) ? 0.0d :  Double.parseDouble(shippingcost);
		 	orderDiscountValue ="".equals(orderdiscount) ? 0.0f :  Float.parseFloat(orderdiscount);
		 	
		 	if(gstVatValue==0f){
		 		gst="0.000";
		 	}else{
		 		gst=gst.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
		 	}if(orderDiscountValue==0f){
		 		orderdiscount="0.00000";
		 	}else{
		 		orderdiscount=orderdiscount.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
		 	}

			
		}*/
	}  else if (action.equalsIgnoreCase("Auto-Generate")) {
		
		com.track.dao.TblControlDAO _TblControlDAO=new   com.track.dao.TblControlDAO();
		_TblControlDAO.setmLogger(mLogger); 
		estno = _TblControlDAO.getNextOrder(plant,sUserId,IConstants.ESTIMATE);
		isAutoGenerate = "true";
		deldate = du.getDate();
		collectionTime = du.getTimeHHmm();
		
		
	}
	
	shippingcost = StrUtils.addZeroes(shipingCostValue, numberOfDecimal);
  

%>
<div class="container-fluid m-t-20">
	 <div class="box"> 
            <div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title"><%=title %></h1>
		</div>
		
 <div class="box-body">

<input type="number" id="numberOfDecimal" style="display:none;" value=<%=numberOfDecimal%>>
<form class="form-horizontal" name="form" method="post"  action="/track/EstimateServlet?" onSubmit="return validatePO(document.form)">
   
       <INPUT type="Hidden" name="DIRTYPE" value="ESTIMATE">
       <input type="hidden" name="xlAction" value="">
       <input type="hidden" name="formtype" value="estimate">
	   <INPUT type = "hidden" name="SHIPPINGID" value ="<%=shippingid%>">
	   <INPUT type = "hidden" name="TAXTREATMENT" value ="<%=sTAXTREATMENT%>">
	   <%=detList%>
   
<div class="form-group">
       <label class="control-label col-sm-2" for="create estimate Order"><a href="#" data-placement="left">
   		 	<i class="glyphicon glyphicon-star" style="font-size: 10px; top:-6px; color: #e50000"></i></a>&nbsp;Order Number:</label>
       <div class="col-sm-3">
    	<div class="input-group">
    		<INPUT class="form-control" type="TEXT" size="14" MAXLENGTH="20" name="ESTNO" value="<%=estno%>" />
  		</div>
  		</div>
  		<div class="form-inline">
  		<label class="control-label col-sm-3" for="Customer name"><a href="#" data-placement="left">
   		 	<i class="glyphicon glyphicon-star" style="font-size: 10px; top:-6px; color: #e50000"></i></a>&nbsp;Customer Name/ID:</label>
        <div class="col-sm-3">
      	    <div class="input-group">
    		 <INPUT class="form-control" name="CUST_NAME" type="TEXT" value="<%=su.forHTMLTag(custName)%>" size="30" MAXLENGTH=100>
    		<span class="input-group-addon"  onClick="javascript:popUpWin('customer_list_order.jsp?CUST_NAME='+form.CUST_NAME.value);">
   		 	<a href="#" data-toggle="tooltip" data-placement="top" title="Customer Details">
   		 	<i class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span>
        </div>
        <span><a href="#" title="Tax Treatment" id="TAXTREATMENT"><%=sTAXTREATMENT%></a></span>
        	<INPUT type = "hidden" name="CUST_CODE" value = "<%=custCode%>">
            <INPUT type = "hidden" name="LOGIN_USER" value = "<%=sUserId%>">
            <INPUT type = "hidden" name="CUST_CODE1" value = "<%=custCode%>">
 		
        </div>
 		</div>  
 		</div>
 		
<div class="form-group">
        <label class="control-label col-sm-2" for="Reference No">Reference No:</label>
        <div class="col-sm-3">
        <INPUT class="form-control" type="TEXT" size="20" MAXLENGTH="20" name="JOB_NUM" value="<%=jobNum%>">
       </div>
    	<div class="form-inline">
    	<label class="control-label col-sm-3" for="Person Incharge">Contact Name:</label>
        <div class="col-sm-3">
        <INPUT type = "TEXT" style="width: 100%" class = "form-control" MAXLENGTH=100  name="PERSON_INCHARGE" value="<%=personIncharge%>" readonly>
        </div>
 		</div>
 		</div>
 		 
<div class="form-group">
        <label class="control-label col-sm-2" for="Order Date">Order Date:</label>
        <div class="col-sm-3">
        <div class="input-group">          
        <INPUT class="form-control datepicker" type="TEXT" size="30" MAXLENGTH="20" name="DELDATE" value="<%=deldate%>"/>
      	</div>
    	<INPUT type = "Hidden" size="30"  MAXLENGTH=20 name="CONTACT_NUM" value="<%=contactNum%>">
    	</div>
    	<!-- <div class="form-inline">
    	<label class="control-label col-sm-3" for="Telephone">Telephone:</label>
        <div class="col-sm-3"> -->
        <INPUT  class="form-control" name="TELNO" type="hidden" value="<%=telno%>" size="30" MAXLENGTH=100 readonly>
        <!-- </div>
 		</div> -->
 		<!-- <div class="form-inline">
    	<label class="control-label col-sm-3" for="Customer Status">Customer Status:</label>
        <div class="col-sm-3"> -->
        <INPUT readonly class="form-control" type="hidden" style="width: 100%" MAXLENGTH="100" name="CUSTOMERSTATUSDESC" value="<%=customerstatusdesc%>">
        <!-- </div>
 		</div> -->
 		<div class="form-inline">
    	<label class="control-label col-sm-3" for="Location">Customer Type:</label>
        <div class="col-sm-3">
        <INPUT readonly class="form-control" type="TEXT" style="width: 100%" MAXLENGTH="100" name="CUSTOMERTYPEDESC" value="<%=customertypedesc%>">
        </div>
 		</div>
 		</div>
 
<div class="form-group">
        <label class="control-label col-sm-2" for="Order Time">Order Time:</label>
        <div class="col-sm-3">
        <INPUT class="form-control" type="TEXT" size="20" MAXLENGTH="20"	name="COLLECTION_TIME" value="<%=collectionTime%>" />
        </div>
    	<!-- <div class="form-inline">
    	<label class="control-label col-sm-3" for="Email Address">Email:</label>
        <div class="col-sm-3"> -->
        <INPUT	type="hidden" size="30" class="form-control" readonly MAXLENGTH="20"name="EMAIL" value="<%=email%>" />
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
        <INPUT type = "TEXT" size="30"  MAXLENGTH="20" id="DELIVERYDATE" name="DELIVERYDATE" <%if(dateformat.equals("1")) {%>readonly class="form-control datepicker"<%} else {%> class=form-control <%}%> value="<%=deliverydate%>">
      	</div>
    	</div>
    	<div class="form-inline">
       	<label class="control-label col-sm-1">
       	<input type = "checkbox" id = "DATEFORMAT" name = "DATEFORMAT" <%if(dateformat.equals("1")) {%>checked <%}%> onClick = "headerReadable();"/><font size="2.9"><b>&nbsp;By Date</b></font></label> 	
    	</div> 
    	<!-- <div class="form-inline">
    	<label class="control-label col-sm-3" for="Unit Number">Unit No:</label>
        <div class="col-sm-3"> -->
        <INPUT type="hidden" size="30" class="form-control" readonly MAXLENGTH="20" name="ADD1" value="<%=add1%>" />
        <!-- </div>
 		</div> -->
 		<div class="form-inine">
 		<label class="control-label col-sm-2" for="Shipping Customer">Shipping Customer:</label>
        <div class="col-sm-3">
        <div class="input-group">
        <INPUT class="form-control" name="SHIPPINGCUSTOMER" type="TEXT" value="<%=su.forHTMLTag(shippingcustomer)%>"size="30" MAXLENGTH=40>
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
        <INPUT class="form-control" type="TEXT" size="20" MAXLENGTH="20"	name="ORDERTYPE" value="<%=ordertype%>" />
        <span class="input-group-addon"  onClick="javascript:popUpWin('OrderType_list.jsp?ORDERTYPE='+form.ORDERTYPE.value+'&FORMTYPE='+form.formtype.value);">
   		 	<a href="#" data-toggle="tooltip" data-placement="top" title="Order Type Details">
   		 	<i class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span>
        </div>
        </div>
    <!-- 	<div class="form-inline">
    	<label class="control-label col-sm-3" for="State">State:</label>
        <div class="col-sm-3">
        <INPUT type="TEXT" size="30" MAXLENGTH="20"	class="form-control" readonly name="" value="" />
        </div>
 		</div> -->
 		<div class="form-inline">
 		<label class="control-label col-sm-3" for="incoterm">INCOTERM:</label>
        <div class="col-sm-3">
        <div class="input-group">
        <INPUT class="form-control" name="INCOTERMS" type="TEXT" value="<%=incoterms%>" size="30" MAXLENGTH=200>
        <span class="input-group-addon"  onClick="javascript:popUpWin('incoterms_list.jsp?INCOTERMS='+form.INCOTERMS.value);">
   		 	<a href="#" data-toggle="tooltip" data-placement="top" title="INCOTERM Details">
   		 	<i class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span>
        </div>
 		</div>
 		</div>
 		</div>
 		
 		
 		
<div class="form-group">
 		<label class="control-label col-sm-2" for="Remarks">Currency:</label>
        <div class="col-sm-3">
        <div class="input-group">
        <INPUT class="form-control" type = "TEXT" size="20"   MAXLENGTH=20 name="DISPLAY" value="<%=currencyid%>">
        <span class="input-group-addon"  onClick="javascript:popUpWin('list/ordcurencyLst.jsp?DISPLAY='+form.DISPLAY.value);">
   		 	<a href="#" data-toggle="tooltip" data-placement="top" title="Currency Details">
   		 	<i class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span>
        </div>
        <input type="hidden" name="CURRENCY_ID">
        <input type="hidden" name="DESC">
 		</div>
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
    	<!-- <div class="form-inline">
    	<label class="control-label col-sm-3" for="Buiding Name">Building:</label>
        <div class="col-sm-3"> -->
        <INPUT type="hidden" size="30" MAXLENGTH="20"	class="form-control" readonly name="ADD2" value="<%=add2%>" />
        <!-- </div>
 		</div> -->
 		</div>
 		</div>
        
 <INPUT type="hidden" id="TaxByLabelOrderManagement" name="taxbylabelordermanagement" value=<%=taxbylabelordermanagement%>>	
<div class="form-group">
        <label class="control-label col-sm-2" for="rcbno" id="TaxLabelOrderManagement"></label>
        <div class="col-sm-3">
        <div class="input-group">
        <INPUT class="form-control" type = "TEXT" size="20"  onkeypress="return isNumberKey(event,this,4)" id="GST"  MAXLENGTH=20 name="GST"  value="<%=gst%>">
        <span class="input-group-addon" style="font-size: 20px; color: #0059b3"><b>%</b></span>
        </div>
        </div>
        <!-- <div class="form-inline">
    	<label class="control-label col-sm-3" for="country">Country:</label>
        <div class="col-sm-3"> -->
        <INPUT type="hidden" size="30" class="form-control" MAXLENGTH="20" name="COUNTRY" readonly value="<%=country%>" /> 
        <!-- </div>
 		</div> -->
 		    	<!-- <div class="form-inline">
    	<label class="control-label col-sm-3" for="street">Street:</label>
        <div class="col-sm-3"> -->
        <INPUT type="hidden" size="30" MAXLENGTH="20"	class="form-control" readonly name="ADD3" value="<%=add3%>" />
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
 		</div>
 		
 		
 		
<div class="form-group">
        <label class="control-label col-sm-2" for="Employee Name">Employee ID:</label>
        <div class="col-sm-3">
        <div class="input-group">
        <INPUT class="form-control" name="EMP_NAME" type = "TEXT" value="<%=EMP_NAME%>" size="20"  MAXLENGTH=50>
        <span class="input-group-addon"  onClick="javascript:popUpWin('employee_list.jsp?EMP_NAME='+form.EMP_NAME.value+'&TYPE=ESTIMATE&FORM=form');">
   		 	<a href="#" data-toggle="tooltip" data-placement="top" title="Employee Details">
   		 	<i class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span>
        </div>
        </div>
    	<!-- <div class="form-inline">
    	<label class="control-label col-sm-3" for="City">City:</label>
        <div class="col-sm-3"> -->
        <INPUT type="hidden" size="30" MAXLENGTH="20"	class="form-control" readonly name="ADD4" value="<%=add4%>" />
        <!-- </div>
 		</div> -->
 		
        <!-- <div class="form-inline">
    	<label class="control-label col-sm-3" for="postal code">Postal Code:</label>
        <div class="col-sm-3"> -->
        <INPUT type="hidden" size="30" class="form-control" MAXLENGTH="20" name="ZIP"	readonly value="<%=zip%>" />
        <!-- </div>
 		</div> -->
 		<div class="form-inline">
 		<label class="control-label col-sm-3" for="Remarks">Order Discount:</label>
        <div class="col-sm-3">
        <div class="input-group">
        <INPUT class="form-control" type = "TEXT" size="30"  onkeypress="return isNumberKey(event,this,4)" id="ORDERDISCOUNT"  MAXLENGTH=20 name="ORDERDISCOUNT" value="<%=orderdiscount == null || "".equals(orderdiscount.trim()) ? "0.0" : orderdiscount%>">
        <span class="input-group-addon" style="font-size: 20px; color: #0059b3"><b>%</b></span>
        </div>
        </div>
        </div>
 		</div>
 		
 		
 		
<!-- <div class="form-group">
 		<label class="control-label col-sm-2" for="Remarks">Order Status:</label>
        <div class="col-sm-3">
        <div class="input-group"> -->
        <INPUT class="form-control" name="STATUS_ID" type="hidden" value="<%=statusID%>" size="20" MAXLENGTH=50>
        <!-- <span class="input-group-addon"  onClick="javascript:popUpWin('OrderStatusList.jsp?ORDERSTATUS='+form.STATUS_ID.value+'&TYPE=OB');">
   		 	<a href="#" data-toggle="tooltip" data-placement="top" title="Order Status Details">
   		 	<i class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span>
        </div>
 		</div> -->
 		
        <!-- <div class="form-inline">
    	<label class="control-label col-sm-3" for="customer remarks">Customer Remarks:</label>
        <div class="col-sm-3"> -->
        <INPUT readonly class="form-control" type="hidden" size="30"MAXLENGTH="100" name="REMARK2" value="<%=remarks%>">
        <!-- </div>
 		</div> -->
 		<!-- </div> -->	
 	 	
 
 <div class="form-group">
        <label class="control-label col-sm-2" for="Expiry Date">Expiry Date:</label>
        <div class="col-sm-3">
        <div class="input-group">          
       <INPUT class="form-control datepicker" name="EXPIREDATE" type="TEXT" readonly="readonly" value="<%=EXPIREDATE%>" size="30" MAXLENGTH=50>
      	</div>
    	</div>
    	<div class="form-inline">
 		<label class="control-label col-sm-3" for="Remarks">Shipping Cost:</label>
        <div class="col-sm-3">
        <INPUT class="form-control" type = "TEXT" style="width: 100%"  onkeypress="return isNumberKey(event,this,6)" id="SHIPPINGCOST"  MAXLENGTH=20 name="SHIPPINGCOST" value="<%=new java.math.BigDecimal(shippingcost).toPlainString()%>">
       <!-- <INPUT class="form-control" type = "TEXT" style="width: 100%"   MAXLENGTH=20 name="SHIPPINGCOST" value="<%=shippingcost == null || "".equals(shippingcost.trim()) ? "0.0" : shippingcost%>">-->
        </div>
        </div>
    	</div>	 	
 	 	 		
      	   	
      	<div class="form-group">        
      	<div class="col-sm-offset-5 col-sm-7">
      	<INPUT type = "hidden" name="ISAUTOGENERATE"  value="<%=isAutoGenerate%>">
        </div>
    	</div>
  		
  		 <TABLE style="font-size:15px" BORDER="0" CELLSPACING="0" WIDTH="100%" class="table"  align="center">
         <thead style="background: #eaeafa">
         <tr>
    	<th width="10%">Order Line No </th>
		<th width="20%">Product ID</th>
		<th width="30%">Description </th>
        <th width="10%">Unit Price</th>
		<th width="10%">Order Quantity</th>
		<th width="10%">Issue Quantity</th>
		<th width="10%"><%=IDBConstants.UOM_LABEL%></th>
         </tr>
         </thead>
       	 <tbody>
       	 
       	
       <%=fieldDesc%>
       </tbody>
       </TABLE>
       
       	 
  		 <INPUT type="Hidden" name="ENCRYPT_FLAG" value="1">
		 <INPUT type="Hidden" name="RFLAG" value="1">
		 <!--<INPUT type="Hidden" name="CUSTOMERSTATUSDESC" value="">-->
        
  		<div class="form-group">        
      	<div class="col-sm-12" align="center">
      	<button type="button" class="Submit btn btn-default" onClick="onback()"><b>Cancel</b></button>&nbsp;&nbsp;
		<button type="button" class="Submit btn btn-default" data-toggle="modal" data-target="#myModal" onClick="onSavedClicked();"><b>Save</b></button>&nbsp;&nbsp;
        </div>
    	</div>
  		
  		</form>
		</div>
        </div>
        </div>
<script>
$(document).ready(function(){
    $('[data-toggle="tooltip"]').tooltip();

    var  d = document.getElementById("TaxByLabelOrderManagement").value;
    document.getElementById('TaxLabelOrderManagement').innerHTML = "Sales " + d +" :";
});
function onback(){
	window.location.href="alternatebrandproductsummary.jsp?PGaction=view&ITEM=<%=fitem%>&LOC=<%=floc%>&LOC_TYPE_ID=<%=floc_type_id%>&LOC_TYPE_ID2=<%=floc_type_id2%>&CUSTOMER=<%=custCode%>&MODEL=<%=fmodel%>&UOM=<%=fuom%>&CNAME=<%=custName%>&TAXTREATMENT=<%=sTAXTREATMENT%>&NAME=<%=personIncharge%>&CUSTOMER_TYPE_ID=<%=customertypedesc%>";
}
</script>
<jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
</jsp:include>
