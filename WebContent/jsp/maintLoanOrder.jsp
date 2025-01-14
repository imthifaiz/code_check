<%@ include file="header.jsp" %>
<%@ page import="com.track.constants.*"%>
<%@ page import="com.track.dao.*"%>

<%
String title = "Edit Rental Order";
%>
<%@include file="sessionCheck.jsp" %>
<jsp:include page="header2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
	<jsp:param name="mainmenu" value="<%=IConstants.RENTAL_CONSIGNMENT%>"/>
    <jsp:param name="submenu" value="<%=IConstants.RENTAL_CONSIGNMENT_ORDER%>"/>
</jsp:include>

<script language="JavaScript" type="text/javascript" src="js/general.js"></script>
<script language="JavaScript" type="text/javascript" src="js/calendar.js"></script>
<script type="text/javascript">

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
 
 function expiryReadable(){
		if(document.form.EDATEFORMAT.checked)
		{
			document.form.EXPIREDATE.value="";
			document.form.DAYS.value="";
			$('#EXPIREDATE').attr('readonly',true).datepicker({ dateFormat: 'dd/mm/yy'});
			
		}
		else{
			document.form.EXPIREDATE.value="";
			document.form.DAYS.value="";
			 $('#EXPIREDATE').attr('readonly',false).datepicker("destroy");

		}
	} 
   
 function parseDate(str) {
	    var mdy = str.split('/');
	    return new Date(mdy[2], mdy[1], mdy[0]-1);
	}

	function datediff(first, EXPIREDATE) {
	    return Math.round((EXPIREDATE-first)/(1000*60*60*24));
	}

	function getdays(){
		 
			  document.form.DAYS.value=datediff(parseDate(first.value), parseDate(EXPIREDATE.value))+1;  
		  
				
			}

 
function isNumberKey(evt, element, id) {
	  var charCode = (evt.which) ? evt.which : event.keyCode;
	  if (charCode > 31 && (charCode < 48 || charCode > 57) && !(charCode == 46 || charCode == 8 || charCode == 45))
		  {
	    	return false;
		  }
	  

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
 subWin = window.open(URL, 'LoanOrder', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=800,height=400,left = 200,top = 184');
}

function sf()
{
document.form.DONO.focus();
}

function validatePO(form)
{

  if (form.DONO.value.length < 1)
  {
    alert("Please Enter Rental Order No !");
    form.DONO.focus();
    return false;
  }

  if (form.DONO.value.length < 1)
  {
    alert("Please Enter Rental Order No !");
    form.DONO.focus();
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
   
    if (form.DELDATE.value.length > 1){
    if (isDate(form.DELDATE.value)==false){
		form.DELDATE.focus()
		return false
    }
   }
  
    submitForm('View');
}
function onDelete(form)
{
	  if (form.DONO.value.length < 1)
	    {
	    alert("Please Enter Order Number !");
	    form.DONO.focus();
	    return false;
	    }
	  else{
		     var mes=confirm("Do you want to Delete the Rental order ?");
		      if(mes==true)
		      {
		      
		      document.form.action="/track/loanorderservlet?Submit=REMOVE_LO";
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
   
	if (form.DONO.value.length < 1)
    {
    alert("Please Enter Rental Order No !");
    form.DONO.focus();
    return false;
    }
    
    if (form.CUST_CODE.value.length < 1)
      {
        alert("Please select valid Rental Customer Name !");
        form.CUST_NAME.focus();
        form.CUST_NAME.select();
        return false;
      }
     if (form.DELDATE.value.length > 1){
    if (isDate(form.DELDATE.value)==false){
		form.DELDATE.focus()
		return false
    }
     else{
     var mes=confirm("Are you sure you would like to save ?");
      if(mes==true)
      {
      document.form.action="/track/loanorderservlet?Submit=Update";
      document.form.submit();
      }
      else
      {  
      return  false;
      }
    }
   }
   
    
}
function onNew(form)
{
   
        document.form.DONO.value   ="";
        document.form.CUST_NAME.value       ="";
        document.form.JOB_NUM.value         ="";
        document.form.PERSON_INCHARGE.value ="";
        document.form.CONTACT_NUM.value     ="";
        document.form.ADD1.value         ="";
        document.form.ADD2.value         ="";
        document.form.ADD3.value         ="";
        document.form.ADD4.value         ="";
        document.form.DELDATE.value         ="";
        document.form.TELNO.value         ="";
        document.form.EMAIL.value         ="";
        document.form.COUNTRY.value         ="";
        document.form.ZIP.value         ="";
        document.form.COLLECTION_TIME.value ="";
        document.form.REMARK1.value         ="";
        document.form.REMARK2.value         ="";
        document.form.FRLOC.value         ="";
        document.form.TOLOC.value         ="";
        document.form.EXPIREDATE.value         ="";
		document.form.DAYS.value         ="";
     
    return true;
   
}

function submitForm(actionvalue)
{
	form.removeAttribute("target", "_blank");
	document.form.action = "/track/loanorderservlet?Submit="+actionvalue;
    document.form.submit();
}

function submitFormPrint(actionvalue)
{
	form.setAttribute("target", "_blank");
	document.form.action = "/track/loanorderservlet?Submit="+actionvalue;
    document.form.submit();
}

function submitToView(){
	
	if(event.keyCode=='13'){
		submitForm('View');
	}
			
}

//-->
</script>


<jsp:useBean id="gn"  class="com.track.gates.Generator" />
<jsp:useBean id="su"  class="com.track.util.StrUtils" />
<jsp:useBean id="phb" class="com.track.tables.POHDR" />
<jsp:useBean id="pdb" class="com.track.tables.PODET" />
<jsp:useBean id="logger" class="com.track.util.MLogger" />
<jsp:useBean id="du" class="com.track.util.DateUtils" />
<jsp:useBean id="sb" class="com.track.gates.selectBean" />

<%
    session.setAttribute("RFLAG","4");
	String DIRTYPE="";
    String plant=(String)session.getAttribute("PLANT");
    String ORDTYPE     = su.fString(request.getParameter("ORDERTYPE"));
    String ordno     = su.fString(request.getParameter("DONO"));
    String action   = su.fString(request.getParameter("action")).trim();
    String sUserId = (String) session.getAttribute("LOGIN_USER");
    DIRTYPE       = StrUtils.fString(request.getParameter("DIRTYPE"));
    PlantMstDAO _PlantMstDAO  = new  PlantMstDAO();
    String numberOfDecimal = _PlantMstDAO.getNumberOfDecimal(plant);  
    String vend = "",deldate="",jobNum = "",custName = "",custCode="",personIncharge = "",contactNum = "";
    String remark1 = "",remark2 = "",address="",address2="",address3="",collectionDate="",collectionTime="",days="";
    String contactname="",telno="",email="",add1="",add2="",add3="",add4="",country="",zip="",remarks="",frLoc="",toLoc="",shippingid="",edateformat="";
    String sSaveEnb    = "disabled",EXPIREDATE="",ordertype="",currencyid="",gst="",EMP_NAME="",deliverydate="",paymenttype="",shippingcustomer="",shippingcost="",orderdiscount="",dateformat="";
    String fieldDesc="<tr><td colspan=\"8\" align=\"center\">Please Enter Any Search Criteria</td></tr>";
    if(DIRTYPE.length()<=0){
   	 	DIRTYPE = "RENTAL";
   	}
    logger.log(0,"Stage 1 ");
    sb.setmLogger(mLogger);
    gst = sb.getGST("RENTAL",plant);
    String basecurrency=_PlantMstDAO.getBaseCurrency(plant);
    if(currencyid.length()<0||currencyid==null||currencyid.equalsIgnoreCase(""))currencyid=basecurrency;
    float gstVatValue ="".equals(gst) ? 0.0f :  Float.parseFloat(gst);
 	/* float shipingCostValue ="".equals(shippingcost) ? 0.0f :  Float.parseFloat(shippingcost); */
 	double shipingCostValue ="".equals(shippingcost) ? 0.0d :  Double.parseDouble(shippingcost);
 	float orderDiscountValue ="".equals(orderdiscount) ? 0.0f :  Float.parseFloat(orderdiscount);
 	
 	if(gstVatValue==0f){
 		gst="0.000";
 	}else{
 		gst=gst.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
 	}/* if(shipingCostValue==0f){
 		shippingcost="0.00000";
 	}else{
 		shippingcost=shippingcost.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
 	} */if(orderDiscountValue==0f){
 		orderdiscount="0.000";
 	}else{
 		orderdiscount=orderdiscount.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
 	}    
     if(action.equalsIgnoreCase("GO")){
    
      Map m=(Map)request.getSession().getAttribute("podetVal");
      fieldDesc=(String)request.getSession().getAttribute("RESULT");
      
       if(m.size()>0){
    	   jobNum=(String)m.get("jobNum");
       	   custName=(String)m.get("custName");
           custCode=(String)m.get("custCode");
           logger.log(0,"custCode : " + custCode);
      	   contactNum=(String)m.get("contactNum");
           telno=(String)m.get("telno");
           email=(String)m.get("email");
           add1=(String)m.get("add1");
           add2=(String)m.get("add2");
           add3=(String)m.get("add3");
           add4=(String)m.get("add4");
           country=(String)m.get("country"); 
           zip=(String)m.get("zip");
           remarks=(String)m.get("remarks");
       
       personIncharge=(String)m.get("contactname");
           contactNum=(String)m.get("contactNum");
           address=(String)m.get("address");
           address2=(String)m.get("address2");
           address3=(String)m.get("address3");
           deldate=(String)m.get("collectionDate");
           collectionTime=(String)m.get("collectionTime");
           remark1=(String)m.get("remark1");
           remark2=(String)m.get("remark2");
             frLoc=(String)m.get("frLoc");
           toLoc=(String)m.get("toLoc");
           EXPIREDATE=(String)m.get("expiredate");
           ordertype = (String) m.get("ordertype");
           currencyid = (String) m.get("currencyid");
           gst = (String) m.get("rentalgst");
           EMP_NAME = (String) m.get("employeeid");
           deliverydate = (String) m.get("deliverydate");
           paymenttype = (String) m.get("paymenttype");
           shippingcustomer = (String) m.get("shippingcustomer");
           shippingcost = (String) m.get("shippingcost");
           orderdiscount = (String) m.get("orderdiscount");
           days = (String) m.get("DAYS");
           dateformat= (String) m.get("DELIVERYDATEFORMAT");
           edateformat= (String) m.get("EXPIRYDATEFORMAT");
           gstVatValue ="".equals(gst) ? 0.0f :  Float.parseFloat(gst);
		 	shipingCostValue ="".equals(shippingcost) ? 0.0f :  Float.parseFloat(shippingcost);
		 	orderDiscountValue ="".equals(orderdiscount) ? 0.0f :  Float.parseFloat(orderdiscount);
		 	
		 	if(gstVatValue==0f){
		 		gst="0.000";
		 	}else{
		 		gst=gst.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
		 	}/* if(shipingCostValue==0f){
		 		shippingcost="0.00000";
		 	}else{
		 		shippingcost=shippingcost.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
		 	} */if(orderDiscountValue==0f){
		 		orderdiscount="0.000";
		 	}else{
		 		orderdiscount=orderdiscount.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
		 	}

       
      }
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
  	<form class="form-horizontal" name="form" method="post" action="/track/loanorderservlet?" >
  	 	
    
   <%
  String statusValue = su.fString(request.getParameter("statusValue"));
  if(statusValue.equals("100")) {
	  out.println("<div><b><font color='green'><center>Rental Order "+ordno+" has sucessfully Deleted!</center></font></b></div>");
  }
  if(statusValue.equals("99")) {
	  out.println("<div><b><font color='red'><center>Rental Order "+ordno+"  does not exist</center></font></b></div>");
  }
  if(statusValue.equals("98")) {
	  out.println("<div'><b><font color='red'><center>Rental Order "+ordno+" is in use. Cannot remove!</center></font></b></div>");
  }
  if(statusValue.equals("97")) {
	  out.println("<div'><b><font color='red'><center>Unable to remove Rental Order "+ordno+". Some Error Occured!</center></font></b></div>");
  }
  %>
 
     
<div class="form-group">
       <label class="control-label col-sm-2" for="create loan Order">
   		 	<i class="glyphicon glyphicon-star" style="font-size: 10px; top:-6px; color: #e50000"></i>&nbsp;Order Number:</label>
       <div class="col-sm-3">
       <div class="input-group">
       <INPUT class="form-control" type="TEXT" size="20" MAXLENGTH="20" name="DONO" value="<%=ordno%>" onKeyPress ="submitToView()"/>
   		 	<span class="input-group-addon"  onClick="javascript:popUpWin('list/loan_hdrlist.jsp?DONO='+form.DONO.value);">
   		 	<a href="#" data-toggle="tooltip" data-placement="top" title="Rental Details">
   		 	<i class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span>
  		</div>
  		</div>
  		<div class="form-inline">
  		<div class="col-sm-1">   
  		<button type="button" class="Submit btn btn-default" value="View"  onClick="return validatePO(document.form)"><b>View</b></button>
  		</div>
  		</div>
  		<div class="form-inline">
  		<label class="control-label col-sm-2" for="Assignee name"><a href="#" data-placement="left">
   		 	<i class="glyphicon glyphicon-star" style="font-size: 10px; top:-6px; color: #e50000"></i></a>&nbsp;Customer Name/ID:</label>
        <div class="col-sm-3">
      	<div class="input-group">
    	<INPUT class="form-control" name="CUST_NAME" type = "TEXT" value="<%=su.forHTMLTag(custName)%>" size="40"  MAXLENGTH=100>
    		<span class="input-group-addon"  onClick="javascript:popUpWin('list/loanAssigneehdrlist.jsp?CUST_NAME='+form.CUST_NAME.value);">
   		 	<a href="#" data-toggle="tooltip" data-placement="top" title="Customer Details">
   		 	<i class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span>
   		</div>
        </div>
 		</div>  
 		</div>
 		 	<INPUT type="Hidden" name="DIRTYPE" value="RENTAL">
   		 	<INPUT type = "hidden" name="CUST_CODE" value = "<%=custCode%>">
            <INPUT type = "hidden" name="LOGIN_USER" value = "<%=sUserId%>">
            <INPUT type = "hidden" name="CUST_CODE1" value = "<%=custCode%>">
            
<div class="form-group">
        <label class="control-label col-sm-2" for="From Location">
      		<a href="#" data-placement="left">
   			<i class="glyphicon glyphicon-star" style="font-size: 10px; top:-6px; color: #e50000"></i></a>&nbsp;From Location:</label>
        <div class="col-sm-3">
        <div class="input-group">          
        <INPUT class="form-control" type="TEXT" size="20" MAXLENGTH="50" name="FRLOC" value="<%=frLoc%>"/>
        <span class="input-group-addon"  onClick="javascript:popUpWin('list/loanLocList.jsp?FEILD_NAME=FRLOC&LOC='+form.FRLOC.value);">
   		 	<a href="#" data-toggle="tooltip" data-placement="top" title="Location Details">
   		 	<i class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span>
      	</div>
    	</div>
    	<div class="form-inline">
    	<label class="control-label col-sm-3" for="From Location">Contact Name:</label>
        <div class="col-sm-3">
        <INPUT  class="form-control" name="PERSON_INCHARGE" type="TEXT" value="<%=personIncharge%>" style="width: 100%" MAXLENGTH=100 readonly>
        </div>
 		</div>
 		</div>
 		 		 
<div class="form-group">
        <label class="control-label col-sm-2" for="To Location">
      		<a href="#" data-placement="left">
   			<i class="glyphicon glyphicon-star" style="font-size: 10px; top:-6px; color: #e50000"></i></a>&nbsp;To Location:</label>
        <div class="col-sm-3">
                 
        <INPUT type="TEXT" size="20" MAXLENGTH="50" class = "form-control" name="TOLOC" readonly value="<%=toLoc%>"  />
        
    	</div>
    	<!-- <div class="form-inline">
    	<label class="control-label col-sm-3" for="From Location">Tel No/E-mail:</label>
        <div class="col-sm-2"> -->
        <INPUT  class="form-control" name="TELNO" type="hidden" value="<%=telno%>" size="30" MAXLENGTH=100 readonly>
        <!-- </div>
 		</div> -->
 		<!-- <div class="form-inline">
 		<div class="col-sm-offset-11"> -->
 		<INPUT  class="form-control" name="EMAIL" type="hidden" value="<%=email%>" size="25" MAXLENGTH=100 readonly>
 		<!-- </div>
 		</div> -->
 		<div class="form-inline">
        <label class="control-label col-sm-3" for="Payment Type">Payment Type:</label>
        <div class="col-sm-3">
         <div class="input-group">
        <INPUT name="PAYMENTTYPE" value="<%=paymenttype%>"  type="TEXT" value="" class="form-control" style="width: 100%" MAXLENGTH=100>
 		<span class="input-group-addon"  onClick="javascript:popUpWin('list/paymenttypelist_save.jsp?paymenttype='+form.PAYMENTTYPE.value+'&EDIT=EDIT');">
		   		<a href="#" data-toggle="tooltip" data-placement="top" title="Payment Type">
		   		<i class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span>
	  	</div>
 		</div>
 		</div>
 		</div>
 
<div class="form-group">
        <label class="control-label col-sm-2" for="Refernce no">Reference No:</label>
        <div class="col-sm-3">
        <INPUT  class="form-control" name="JOB_NUM" type="TEXT" value="<%=jobNum%>" size="50" MAXLENGTH=20>
        </div>
    	<!-- <div class="form-inline">
    	<label class="control-label col-sm-3" for="Location">Unit no/Building:</label>
        <div class="col-sm-2"> -->
        <INPUT  class="form-control" name="ADD1" type="hidden" value="<%=add1%>" size="30" MAXLENGTH=100 readonly>
        <!-- </div>
 		</div> -->
 		<!-- <div class="form-inline">
 		<div class="col-sm-offset-11"> -->
 		<INPUT  class="form-control" name="ADD2" type="hidden" value="<%=add2%>" size="25" MAXLENGTH=100 readonly>
 		<!-- </div>
 		</div> -->
 		<div class="form-inine">
 		<label class="control-label col-sm-3" for="Shipping Customer">Shipping Customer:</label>
        <div class="col-sm-3">
        <div class="input-group">
        <INPUT class="form-control" name="SHIPPINGCUSTOMER" type="TEXT" value="<%=su.forHTMLTag(shippingcustomer)%>"style="width: 100%" MAXLENGTH=40>
        <span class="input-group-addon"  onClick="javascript:popUpWin('shippingdetails_list.jsp?SHIPPINGCUSTOMER='+form.SHIPPINGCUSTOMER.value);">
   		 	<a href="#" data-toggle="tooltip" data-placement="top" title="Shipping Customer Details">
   		 	<i class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span>
        </div>
 		</div>
 		</div>
 		<!-- <div class="form-inline">
    	<label class="control-label col-sm-3" for="Assigee Remarks">Assignee Remarks:</label>
        <div class="col-sm-2"> -->
        <INPUT  class="form-control" name="REMARK2" type="hidden" value="<%=remarks%>" size="30" MAXLENGTH=100 readonly>
        <!-- </div>
 		</div> -->
 		</div>
 	<INPUT type = "hidden" name="SHIPPINGID" value ="<%=shippingid%>">

			<div class="form-group">
        <label class="control-label col-sm-2" for="Order Date">Order Date:</label>
        <div class="col-sm-3">
        <div class="input-group">
        <INPUT class="form-control datepicker" id="first" type="TEXT" size="30" MAXLENGTH="20" name="DELDATE" value="<%=deldate%>" onchange="getdays()"/>
        </div>
       	<INPUT type = "Hidden" size="20"  MAXLENGTH=20 name="CONTACT_NUM" value="<%=contactNum%>">
       	</div>
    	<!-- <div class="form-inline">
    	<label class="control-label col-sm-3" for="Location">Street/City:</label>
        <div class="col-sm-2"> -->
        <INPUT  class="form-control" name="ADD3" type="hidden" value="<%=add3%>" size="30" MAXLENGTH=100 readonly>
        <!-- </div>
 		</div> -->
 		<div class="form-inline">
 		<label class="control-label col-sm-3" for="Remarks">Order Discount:</label>
        <div class="col-sm-3">
        <div class="input-group">
        <INPUT class="form-control" type = "TEXT" style="width: 100%"  onkeypress="return isNumberKey(event,this,4)" id="ORDERDISCOUNT"  MAXLENGTH=20 name="ORDERDISCOUNT" value="<%=orderdiscount%>">
        <span class="input-group-addon" style="font-size: 20px; color: #0059b3"><b>%</b></span>
        </div>
        </div>
        </div>
 		<!-- <div class="form-inline">
 		<div class="col-sm-offset-11"> -->
 		<INPUT  class="form-control" name="ADD4" type="hidden" value="<%=add4%>" size="25" MAXLENGTH=100 readonly>
 		<!-- </div>
 		</div> -->
 		</div>
 		
 		
<div class="form-group">
        <label class="control-label col-sm-2" for="Order Time">Order Time:</label>
        <div class="col-sm-3">
        <INPUT  class="form-control" name="COLLECTION_TIME" type="TEXT" value="<%=collectionTime%>" size="50" MAXLENGTH=20>
        </div>
    	<!-- <div class="form-inline">
    	<label class="control-label col-sm-3" for="Location">Country/Postal code:</label>
        <div class="col-sm-2"> -->
        <INPUT  class="form-control" name="COUNTRY" type="hidden" value="<%=country%>" size="30" MAXLENGTH=100 readonly>
        <!-- </div>
 		</div> -->
 		<div class="form-inline">
 		<label class="control-label col-sm-3" for="Remarks">Shipping Cost:</label>
        <div class="col-sm-3">
        <INPUT class="form-control" type = "TEXT" style="width: 100%"  onkeypress="return isNumberKey(event,this,6)" id="SHIPPINGCOST"  MAXLENGTH=20 name="SHIPPINGCOST" value="<%=shippingcost%>">
        </div>
        </div>
 		<!-- <div class="form-inline">
 		<div class="col-sm-offset-11"> -->
 		<INPUT  class="form-control" name="ZIP" type="hidden" value="<%=zip%>" size="25" MAXLENGTH=100 readonly>
 		<!-- </div>
 		</div> -->
 		</div>
  
 		<div class="form-group">
        <label class="control-label col-sm-2" for="Delivery Date">Delivery Period/Date:</label>
        <div class="col-sm-3">
        <div class="input-group"> 
        <INPUT type = "TEXT" id="DELIVERYDATE" style="width: 100%"  MAXLENGTH=20  name="DELIVERYDATE" <%if(dateformat.equals("1")) {%>readonly class="form-control datepicker"<%} else {%> class=form-control <%}%> value="<%=deliverydate%>" >
        </div>
        </div>
      	<div class="form-inline">
       	<label class="control-label col-sm-1">
       	<input type = "checkbox" id = "DATEFORMAT" name = "DATEFORMAT" <%if(dateformat.equals("1")) {%>checked <%}%> onClick = "headerReadable();"/><font size="2.9"><b>&nbsp;By Date</b></font></label> 	
    	</div> 
    	<div class="form-inline">
        <label class="control-label col-sm-2"for="Remarks">Remarks:</label>
        <div class="col-sm-3">
        <INPUT  class="form-control" name="REMARK1" type="TEXT" value="<%=remark1%>" style="width: 100%" MAXLENGTH=1000>
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
        <input type="hidden" name="formtype" value="rental">
        </div>
        <div class="form-inline">
        <label class="control-label col-sm-3" for="Expiry Date">Expiry Date:</label>
        <div class="col-sm-2">
        <INPUT  id="EXPIREDATE" name="EXPIREDATE" type = "TEXT" <%if(edateformat.equals("1")) {%>readonly class="form-control datepicker"<%} else {%> class=form-control <%}%> value="<%=EXPIREDATE%>" size="20"  MAXLENGTH=25 onchange="getdays()">   
        </div>
        </div>
       <div class="form-inline"> 
         <div class="col-sm-1">   
         <INPUT class="form-control" id ="DAYS" name="DAYS" value="<%=days%>" type = "TEXT"  size="5"  MAXLENGTH=25 READONLY>
         </div>
         </div>
         <div class="form-inline">
        <label class="control-label col-sm-1">
       	<input type = "checkbox" id = "EDATEFORMAT" name = "EDATEFORMAT" <%if(edateformat.equals("1")) {%>checked <%}%> onClick = "expiryReadable();"/><font size="2"><b>&nbsp;By Date</b></font></label> 
        </div>
        </div>
        
        <div class="form-group">
 		<label class="control-label col-sm-2" for="Currency">Currency:</label>
        <div class="col-sm-3">
        <div class="input-group">
        <INPUT class="form-control" type = "TEXT" size="20"   MAXLENGTH=20 name="DISPLAY" value="<%=currencyid%>">
        <span class="input-group-addon"  onClick="javascript:popUpWin('list/ordcurencyLst.jsp?DISPLAY='+form.DISPLAY.value);">
   		 	<a href="#" data-toggle="tooltip" data-placement="top" title="Currency Details">
   		 	<i class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span>
        </div>
 		</div>
 		<input type="hidden" name="CURRENCY_ID">
 		<input type="hidden" name="DESC">
 		</div>
 		
 		<div class="form-group">
        <label class="control-label col-sm-2" for="Rental VAT/GST">Rental VAT:</label>
        <div class="col-sm-3">
        <div class="input-group">
        <INPUT class="form-control" type = "TEXT" size="20"  onkeypress="return isNumberKey(event,this,4)" id="GST"  MAXLENGTH=20 name="GST"  value="<%=gst%>">
        <span class="input-group-addon" style="font-size: 20px; color: #0059b3"><b>%</b></span>
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
        </div>		 		
 	
 		    
      	<div class="form-group">        
      	<div class="col-sm-offset-5 col-sm-7">
        <button type="button" class="Submit btn btn-default" onClick="onNew();"><b>Clear</b></button>&nbsp;&nbsp;
      	<button type="button" class="Submit btn btn-default" data-toggle="modal" data-target="#myModal" onClick="return onUpdate(document.form)"><b>Save</b></button>&nbsp;&nbsp;
      	<button type="button" class="Submit btn btn-default" onClick="return onDelete(document.form);"><b>Delete</b></button>&nbsp;&nbsp;
      	</div>
    	</div>
  		
  		 <TABLE style="font-size:15px" BORDER="0" CELLSPACING="0" WIDTH="100%" class="table">
         <thead style="background: #eaeafa">
         <tr >
         <th width="10%">Order Line No </th>
         <th width="17%">Product ID </th>
         <th width="27%">Description </th>
         <th width="12%">Order Quantity </th>
         <th width="12%">Pick Quantity </th>
         <th width="12%">Receive Quantity </th>
         <th width="5%"><%=IDBConstants.UOM_LABEL%></th>
         <th width="5%">Status </th>
         </tr>
       	</thead>
        <tbody>
       	 
      	 <%=fieldDesc%>
      	 </tbody>
    	 </table>
  	
  		 <INPUT type="Hidden" name="ENCRYPT_FLAG" value="1">
         <INPUT type="Hidden" name="RFLAG" value="1">
        
  		
  		<div class="form-group">        
      	<div class="col-sm-12" align="center">
      	<button type="button" class="Submit btn btn-default" onClick="window.location.href='../home'"><b>Back</b></button>&nbsp;&nbsp;
       	<!-- <button type="button" class="Submit btn btn-default" onClick="{backNavigation('orderManagement.jsp','LBO');}"><b>Back</b></button>&nbsp;&nbsp; -->
      	<button type="button" class="Submit btn btn-default" name="Submit" onClick="submitForm('Add Products');"><b>Add Product</b></button>&nbsp;&nbsp;
      	<button type="button" class="Submit btn btn-default" name="Submit" onClick="submitFormPrint('Print Loan Order');"><b>Print Rental Order</b></button>&nbsp;&nbsp;
      	<button type="button" class="Submit btn btn-default" name="Submit" onClick="submitFormPrint('Print Loan Order With Price');"><b>Print Rental Order With Price</b></button>&nbsp;&nbsp;
      	<button type="button" class="Submit btn btn-default" onClick="submitForm('Export To Excel');"><b>Export To Excel</b></button>&nbsp;
      	</div>
    	</div>
  		
  		</form>
		</div>
		</div>
		</div>
		
<% 

 logger.log(-1,"createloanOrder.jsp  ");
%>


<jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
</jsp:include>
