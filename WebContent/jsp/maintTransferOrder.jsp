<%@ include file="header.jsp"%>
<%@ page import="com.track.constants.*"%>
  <%
String title = "Edit Consignment Order";
%>
<%@include file="sessionCheck.jsp" %>
<jsp:include page="header2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
	<jsp:param name="mainmenu" value="<%=IConstants.RENTAL_CONSIGNMENT%>"/>
    <jsp:param name="submenu" value="<%=IConstants.RENTAL_CONSIGNMENT_ORDER%>"/>
</jsp:include>

<script language="JavaScript" type="text/javascript" src="js/calendar.js"></script>
<script language="JavaScript" type="text/javascript" src="js/general.js"></script>
<SCRIPT LANGUAGE="JavaScript">

var subWin = null;


function popUpWin(URL) {
 subWin = window.open(URL, 'MaintTransferOrder', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=800,height=400,left = 200,top = 184');
}

function sf()
{
document.form.TONO.focus();
}

function validatePO(form)
{
	if (form.TONO.value.length < 1)
  {
    alert("Please Enter TO Number !");
    form.TONO.focus();
    return false;
  }
 
  /* if (form.CUST_NAME.value.length < 1)
  {
    alert("Please Select Assignee !");
    form.CUST_NAME.focus();
    return false;
  } */
  
   
   /*  if (form.DELDATE.value.length > 1){
    if (isDate(form.DELDATE.value)==false){
		form.DELDATE.focus()
		return false
    }
   } */

    submitForm('View');
  
}

function submitForm(actionvalue)
{
    form.removeAttribute("target", "_blank");
	document.form.action = "/track/TransferOrderServlet?Submit="+actionvalue;
    document.form.submit();
}

function submitFormPrint(actionvalue)
{
	form.setAttribute("target", "_blank");
	document.form.action = "/track/TransferOrderServlet?Submit="+actionvalue;
    document.form.submit();
}

function popWin(vname){
    window.open('vendSumm.jsp?VENDNAME='+vname+'&P=Y');
}

function onDelete(form)
{
	  if (form.TONO.value.length < 1)
	    {
	    alert("Please Enter Order Number !");
	    form.TONO.focus();
	    return false;
	    }
	  else{
		     var mes=confirm("Do you want to Delete the Consignment order ?");
		      if(mes==true)
		      {
		      
		      document.form.action="/track/TransferOrderServlet?Submit=REMOVE_TO";
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
   
	if (form.TONO.value.length < 1)
    {
    alert("Please Enter TO Number !");
    form.TONO.focus();
    return false;
    }
	if (form.CUST_NAME.value.length < 1)
	  {
	    alert("Please Select Customer !");
	    form.CUST_NAME.focus();
	    return false;
	  }
	 if (form.FROM_WAREHOUSE.value.length < 1)
	  {
	    alert("Please Enter/Select From Loc !");
	    form.FROM_WAREHOUSE.focus();
	    return false;
	  }
	 
	   if (form.TO_WAREHOUSE.value.length < 1)
	  {
	    alert("Please Enter/Select To Loc !");
	    form.TO_WAREHOUSE.focus();
	    return false;
	  }
	   if(form.FROM_WAREHOUSE.value==form.TO_WAREHOUSE.value)
	   {
		   alert("Please Choose Different From/To Location!");
		   return false;
	   }
      if (form.CUST_CODE.value.length < 1)
      {
        alert("Please select valid Customer Name !");
        form.CUST_NAME.focus();
        form.CUST_NAME.select();
        return false;
      }
           
    if (form.DELDATE.value.length > 1){
    if (isDate(form.DELDATE.value)==false){
		form.DELDATE.focus()
		return false
    }else{
     var mes=confirm("Do you want to update the Consignment order ?");
      if(mes==true)
      {
      document.form.action="/track/TransferOrderServlet?Submit=Update";
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
   
    document.form.TONO.value   ="DUMMY"
    document.form.CUST_NAME.value       ="DUMMY"
    form.JOB_NUM.value         =""
    form.PERSON_INCHARGE.value =""
    form.CONTACT_NUM.value     =""
    form.ADDRESS.value         =""
    form.ADDRESS2.value         =""
    form.ADDRESS3.value         =""
    form.DELDATE.value         =""
    form.COLLECTION_TIME.value =""
    form.REMARK1.value         =""
    form.REMARK2.value         =""
    
    form.SCUST_NAME.value         =""
    form.SCONTACT_NAME.value =""
    form.SADDR1.value     =""
    form.SADDR2.value         =""
    form.SCITY.value =""
    form.SCOUNTRY.value         =""
    form.SZIP.value         =""
    form.STELNO.value         =""
     
    return true;
  
    
}

function onClear(){
	 
	  document.form.TONO.value   =""
	  document.form.CUST_NAME.value       =""
	  form.JOB_NUM.value         =""
	  form.PERSON_INCHARGE.value =""
	  form.CONTACT_NUM.value     =""
	  form.JOB_NUM.value         =""
	  form.COLLECTION_TIME.value  =""
	  form.DELDATE.value         =""
	  form.COLLECTION_TIME.value =""
	  form.REMARK1.value         =""
	  form.REMARK2.value         =""

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
          
            form.SCUST_NAME.value         =""
            form.SCONTACT_NAME.value =""
            form.SADDR1.value     =""
            form.SADDR2.value         =""
            form.SCITY.value =""
            form.SCOUNTRY.value         =""
            form.SZIP.value         =""
            form.STELNO.value         =""	
	  		  
	  return true;
	}


function submitToView(){
	
	if(event.keyCode=='13'){
		submitForm('View');
	}
			
}

//-->
</script>


<jsp:useBean id="gn" class="com.track.gates.Generator" />
<jsp:useBean id="sl" class="com.track.gates.selectBean" />
<jsp:useBean id="db" class="com.track.gates.defaultsBean" />
<jsp:useBean id="su" class="com.track.util.StrUtils" />
<jsp:useBean id="vmb" class="com.track.tables.VENDMST" />
<jsp:useBean id="phb" class="com.track.tables.POHDR" />
<jsp:useBean id="pdb" class="com.track.tables.PODET" />
<jsp:useBean id="du" class="com.track.util.DateUtils" />


<%
	com.track.db.util.POUtil _poUtil = new com.track.db.util.POUtil();
_poUtil.setmLogger(mLogger);
String DIRTYPE ="";
	String plant = (String) session.getAttribute("PLANT");
	String tono = su.fString(request.getParameter("TONO"));
	String action = su.fString(request.getParameter("action")).trim();
	String sUserId = (String) session.getAttribute("LOGIN_USER");
	DIRTYPE       = su.fString(request.getParameter("DIRTYPE"));
	session.setAttribute("RFLAG", "4");

	String vend = "", deldate = "", jobNum = "", custName = "", custCode = "", personIncharge = "", contactNum = "";
	String remark1 = "", remark2 = "", address = "", address2 = "", address3 = "", collectionDate = "", collectionTime = "";
	String contactname = "", telno = "", email = "", add1 = "", add2 = "", add3 = "", add4 = "", country = "", zip = "", remarks = "";
        String sCustName="",sContactName= "",sAddr1="",sAddr2="",sCity="",sCountry="",sZip="",sTelno="";
	String fromWareHouse = "", toWareHouse = "";
	String sSaveEnb = "disabled";
	String fieldDesc = "<tr><td colspan=\"8\" align=\"center\">Please enter any search criteria</td></tr>";

	if(DIRTYPE.length()<=0){
     	 DIRTYPE = "TRANSFER";
     	 }
	if (action.equalsIgnoreCase("View")) {

		Map m = (Map) request.getSession().getAttribute("todetVal");
		fieldDesc = (String) request.getSession().getAttribute(
				"RESULT1");

		

		if (m.size() > 0) {
			jobNum = (String) m.get("jobNum");
			fromWareHouse = (String) m.get("fromwarehouse");
			toWareHouse = (String) m.get("towarehouse");
			custName = (String) m.get("custName");
			custCode = (String) m.get("custCode");
			
			personIncharge = (String) m.get("personInCharge");
			contactNum = (String) m.get("contactNum");

			telno = (String) m.get("telno");
			email = (String) m.get("email");
			add1 = (String) m.get("add1");
			add2 = (String) m.get("add2");
			add3 = (String) m.get("add3");
			add4 = (String) m.get("add4");
			country = (String) m.get("country");
			zip = (String) m.get("zip");
			remarks = (String) m.get("remarks");

			contactNum = (String) m.get("contactNum");
			address = (String) m.get("address");
			address2 = (String) m.get("address2");
			address3 = (String) m.get("address3");
			deldate = (String) m.get("collectionDate");
			collectionTime = (String) m.get("collectionTime");
			remark1 = (String) m.get("remark1");
			remark2 = (String) m.get("remark2");
                        
                        //Shipping Address Details
                        sCustName= (String) m.get("sCust_Name");
                        sContactName = (String) m.get("sContact_Name");
                        sAddr1 = (String) m.get("sAddr1");
                        sAddr2 = (String) m.get("sAddr2");
                        sCity = (String) m.get("sCity");
                        sCountry = (String) m.get("sCountry");
                        sZip = (String) m.get("sZip");
                        sTelno = (String) m.get("sTelno");
		}
	} else if (action.equalsIgnoreCase("NEW")) {

		sSaveEnb = "enabled";
		com.track.dao.ToHdrDAO _ToHdrDAO = new com.track.dao.ToHdrDAO();
		_ToHdrDAO.setmLogger(mLogger);
		tono = _ToHdrDAO.getNextOrder(plant);
		deldate = du.getDate();
		collectionTime = du.getTimeHHmm();

	}
%>
	<div class="container-fluid m-t-20">
	 <div class="box"> 
	 <!-- Muruganantham Modified on 16.02.2022 -->
            <ul class="breadcrumb backpageul">      	
                <li><a href="../home"><span class="underline-on-hover">Dashboard</span> </a></li>     
                <li><a href="../onsignment/summary"><span class="underline-on-hover">Consignment Summary</span> </a></li> 
                <li><a href="../consignment/detail?tono="<%=tono%>><span class="underline-on-hover">Consignment Order Detail</span> </a></li>                       
                <li><label>Edit Consignment Order</label></li>                                   
            </l>             
    <!-- Muruganantham Modified on 16.02.2022 -->
            <div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title"><%=title %></h1>
		</div>
		
 <div class="box-body">


  	<form class="form-horizontal" name="form" method="post" action="/track/TransferOrderServlet?">
  	
  	<INPUT type="Hidden" name="DIRTYPE" value="TRANSFER">
    <input type="hidden" name="xlAction" value="">
    
   <%
	String statusValue = su
			.fString(request.getParameter("statusValue"));
	if (statusValue.equals("100")) {
		out
				.println("<div><b><font color='green'><center>Consignment Order "
						+ tono
						+ " has sucessfully Deleted!</center></font></b></div>");
	}
	if (statusValue.equals("99")) {
		out.println("<div><b><font color='red'><center>Consignment Order " + tono
				+ "  does not exist</center></font></b></div>");
	}
	if (statusValue.equals("98")) {
		out
				.println("<div><b><font color='red'><center>Consignment Order "
						+ tono
						+ " is in use. Cannot Delete!</center></font></b></div>");
	}
	if (statusValue.equals("97")) {
		out
				.println("<div'><b><font color='red'><center>Unable to remove Consignment Order "
						+ tono
						+ ". Some Error Occured!</center></font></b></div>");
	}
%>
  
     
<div class="form-group">
       <label class="control-label col-sm-2" for="edit Consignment Order">
   		 	<i class="glyphicon glyphicon-star" style="font-size: 10px; top:-6px; color: #e50000"></i>&nbsp;Order Number:</label>
       <div class="col-sm-3">
       <div class="input-group">
       <INPUT class="form-control" type="TEXT" size="5" MAXLENGTH="20" name="TONO" onKeyPress ="submitToView()" value="<%=tono%>" />
   		 	<span class="input-group-addon"  onClick="javascript:popUpWin('to_list_do.jsp?TONO='+form.TONO.value);">
   		 	<a href="#" data-toggle="tooltip" data-placement="top" title="Consignment Order Details">
   		 	<i class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span>
  		</div>
  		</div>
  		<div class="form-inline">
  		<div class="col-sm-1 ">   
  		<button type="button" class="Submit btn btn-default" value="View"  onClick="return validatePO(document.form)"><b>View</b></button>
  		</div>
  		</div>
  		<div class="form-inline">
  		<label class="control-label col-sm-2" for="Assignee name">
   		 	<i class="glyphicon glyphicon-star" style="font-size: 10px; top:-6px; color: #e50000"></i>&nbsp;Customer Name/ID:</label>
        <div class="col-sm-3">
      	<div class="input-group">
    	<INPUT class="form-control" name="CUST_NAME" type="TEXT" value="<%=su.forHTMLTag(custName)%>" size="40" MAXLENGTH=100>
    		<span class="input-group-addon"  onClick="javascript:popUpWin('toassignee_list_order.jsp?CUST_NAME='+form.CUST_NAME.value);">
   		 	<a href="#" data-toggle="tooltip" data-placement="top" title="Customer Details">
   		 	<i class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span>
   		</div>
        </div>
 		</div>  
 		</div>
 		 		 	
   		 	<INPUT type = "hidden" name="CUST_CODE" value = "<%=custCode%>">
            <INPUT type = "hidden" name="LOGIN_USER" value = "<%=sUserId%>">
            <INPUT type = "hidden" name="CUST_CODE1" value = "<%=custCode%>">
            
            <INPUT type = "hidden" name="SCUST_NAME" value ="<%=sCustName%>">
            <INPUT type = "hidden" name="SCONTACT_NAME" value ="<%=sContactName%>">
            <INPUT type = "hidden" name="SADDR1" value ="<%=StrUtils.replaceCharacters2Send(sAddr1) %>">
            <INPUT type = "hidden" name="SADDR2" value ="<%=sAddr2%>">
            <INPUT type = "hidden" name="SCITY" value ="<%=sCity%>">
            <INPUT type = "hidden" name="SCOUNTRY" value ="<%=sCountry%>">
            <INPUT type = "hidden" name="SZIP" value ="<%=sZip%>">
            <INPUT type = "hidden" name="STELNO" value ="<%=sTelno%>">
 		
<div class="form-group">
        <label class="control-label col-sm-2" for="From Location">
      		<a href="#" data-placement="left">
   			<i class="glyphicon glyphicon-star" style="font-size: 10px; top:-6px; color: #e50000"></i></a>&nbsp;From Location:</label>
        <div class="col-sm-3">
        <div class="input-group">          
        <INPUT class="form-control" type="TEXT" size="25" MAXLENGTH=50 name="FROM_WAREHOUSE" value="<%=fromWareHouse%>">
        <span class="input-group-addon"  onClick="javascript:popUpWin('loc_list_transferfrom.jsp?FROMLOC='+form.FROM_WAREHOUSE.value);">
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
        <div class="input-group">          
        <INPUT  class="form-control" name="TO_WAREHOUSE" type="TEXT" value="<%=toWareHouse%>" size="50" MAXLENGTH=100>
        <INPUT type="Hidden" size="20" MAXLENGTH=50 name="CONTACT_NUM" value="<%=contactNum%>">
        <span class="input-group-addon"  onClick="javascript:popUpWin('loc_list_transferto.jsp?TOLOC='+form.TO_WAREHOUSE.value);">
   		 	<a href="#" data-toggle="tooltip" data-placement="top" title="Location Details">
   		 	<i class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span>
      	</div>
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
        <label class="control-label col-sm-3" for="Remarks">Remarks:</label>
        <div class="col-sm-3">
        <INPUT  class="form-control" name="REMARK1" type="TEXT" value="<%=remark1%>" style="width: 100%" MAXLENGTH=100>
        </div>
    	<!-- <div class="form-inline">
    	<label class="control-label col-sm-3" for="Assigee Remarks">Assignee Remarks:</label>
        <div class="col-sm-2"> -->
        <INPUT  class="form-control" name="REMARK2" type="hidden" value="<%=remarks%>" size="30" MAXLENGTH=100 readonly>
        <!-- </div>
 		</div> -->
 		<!-- <div class="form-inline">
 		<div class="col-sm-offset-11">
 		<button type="button" class="Submit btn btn-default" name=addShippingAddr
 		onClick="javascript:popUpWin('shiptoAddr.jsp?ORDTYPE=Transfer&SCUST_NAME='+form.SCUST_NAME.value+'&SCONTACT_NAME='+form.SCONTACT_NAME.value+'&SADDR1='+form.SADDR1.value+'&SADDR2='+form.SADDR2.value+'&SCITY='+form.SCITY.value+'&SCOUNTRY='+form.SCOUNTRY.value+'&SZIP='+form.SZIP.value+'&STELNO='+form.STELNO.value);">
 		<b>Edit Shipping Address</b></button>
 		</div>
 		</div> -->
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
 		</div>
 		
<div class="form-group">
        <label class="control-label col-sm-2" for="Order Date">Order Date:</label>
        <div class="col-sm-3">
        <div class="input-group">
        <INPUT  class="form-control datepicker" name="DELDATE" type="TEXT" value="<%=deldate%>" size="30" MAXLENGTH=20>
       	</div>
       	</div>
    	<!-- <div class="form-inline">
    	<label class="control-label col-sm-3" for="Location">Street/City:</label>
        <div class="col-sm-2"> -->
        <INPUT  class="form-control" name="ADD3" type="hidden" value="<%=add3%>" size="30" MAXLENGTH=100 readonly>
        <!-- </div>
 		</div> -->
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
 		<!-- <div class="form-inline">
 		<div class="col-sm-offset-11"> -->
 		<INPUT  class="form-control" name="ZIP" type="hidden" value="<%=zip%>" size="25" MAXLENGTH=100 readonly>
 		<!-- </div>
 		</div> -->
 		</div>
 		 		
 		
 		    
      	<div class="form-group">        
      	<div class="col-sm-offset-5 col-sm-7">
        <button type="button" class="Submit btn btn-default" onClick="onClear();"><b>Clear</b></button>&nbsp;&nbsp;
      	<button type="button" class="Submit btn btn-default" data-toggle="modal" data-target="#myModal" onClick="return onUpdate(document.form)"><b>Save</b></button>&nbsp;&nbsp;
      	<button type="button" class="Submit btn btn-default" onClick="return onDelete(document.form);"><b>Delete</b></button>&nbsp;&nbsp;
      	</div>
    	</div>
  		
  		 <TABLE style="font-size:15px" BORDER="0" CELLSPACING="0" WIDTH="100%" class="table">
         <thead style="background: #eaeafa">
         <tr >
         <th width="10%">Order Line No </th>
         <th width="17%">Product ID </th>
         <th width="27%">Product Description </th>
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
       	<!-- <button type="button" class="Submit btn btn-default" onClick="{backNavigation('orderManagement.jsp','TBO');}"><b>Back</b></button>&nbsp;&nbsp; -->
      	<button type="button" class="Submit btn btn-default" name="Submit" onClick="submitForm('Add Products');"><b>Add Product</b></button>&nbsp;&nbsp;
      	<button type="button" class="Submit btn btn-default" name="Submit" onClick="submitFormPrint('Print Transfer Order'); "><b>Print Consignment Order</b></button>&nbsp;&nbsp;
      	<button type="button" class="Submit btn btn-default" name="Submit" onClick="submitForm('Export To Excel');"><b>Export To Excel</b></button>&nbsp;&nbsp;
        
        </div>
    	</div>
  		
  		</form>
		</div>
		</div>
		</div>





<jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
</jsp:include>