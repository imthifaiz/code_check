<%@ include file="header.jsp" %>
<%@page import="com.track.constants.IDBConstants"%>
<%@page import="com.track.constants.IConstants"%>
<%@ page import="com.track.dao.*"%>
 
<%
String title = "Create Consignment Order";
%>
<%@include file="sessionCheck.jsp" %>
<jsp:include page="header2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
	<jsp:param name="mainmenu" value="<%=IConstants.RENTAL_CONSIGNMENT%>"/>
    <jsp:param name="submenu" value="<%=IConstants.RENTAL_CONSIGNMENT_ORDER%>"/>
</jsp:include>

<script language="JavaScript" type="text/javascript" src="js/json2.js"></script>
<script language="JavaScript" type="text/javascript" src="js/general.js"></script>
<script language="JavaScript" type="text/javascript" src="js/calendar.js"></script>

<SCRIPT LANGUAGE="JavaScript">

var subWin = null;

function popUpWin(URL) {
 subWin = window.open(URL, 'CreateTransferOrder', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=800,height=400,left = 200,top = 184');
}

function sf()
{
document.form.TONO.focus();
}


function validateOrderNo(form)
{


  if (form.DONO.value.length < 1)
  {
    alert("Please Enter Consignment Order No !");
    form.DONO.focus();
    return false;
  }
 
  
}


function validateMandatoryFeilds()
{ 
	
 if (document.form.TONO.value.length < 1)
  {
    alert("Please Enter Consignment Order No !");
    document.form.TONO.focus();
    return false;
  }
 
  if (document.form.CUST_NAME.value.length < 1)
  {
    alert("Please Select Customer !");
    document.form.CUST_NAME.focus();
    return false;
  }
  
   if (document.form.FROM_WAREHOUSE.value.length < 1)
  {
    alert("Please Enter/Select From Loc !");
    document.form.FROM_WAREHOUSE.focus();
    return false;
  }
  
   if (document.form.TO_WAREHOUSE.value.length < 1)
  {
    alert("Please Enter/Select To Loc !");
    document.form.TO_WAREHOUSE.focus();
    return false;
  }
   if(document.form.FROM_WAREHOUSE.value==document.form.TO_WAREHOUSE.value)
   {
	   alert("Please Choose Different From/To Location!");
	   return false;
   }
   
    
    if (document.form.DELDATE.value.length > 1){
    if (isDate(document.form.DELDATE.value)==false){
    	document.form.DELDATE.focus();
		return false;
    }
   }
   if (document.form.CUST_CODE.value.length < 1)
      {
        alert("Please select valid Consignment Customer Name !");
        document.form.CUST_NAME.focus();
        document.form.CUST_NAME.select();
        return false;
      }

   document.form.action = "/track/TransferOrderServlet?Submit=Save";
   document.form.submit();
  
}

function popWin(vname){
    window.open('vendSumm.jsp?VENDNAME='+vname+'&P=Y');
}

function onDelete(form)
{
   
	  if (form.TONO.value.length < 1)
    {
    alert("Please Enter TO Number !");
    form.TONO.focus();
    return false;
    }
    else{
     var mes=confirm("Do you want to delete the Consignment order !");
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

function onUpdate(form)
{
   
	  if (form.TONO.value.length < 1)
    {
    alert("Please Enter TO Number !");
    form.TONO.focus();
    return false;
    }
    else{
     var mes=confirm("Do you want to update the Consignment order !");
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
function onNew(form)
{
   
	document.form.TONO.value   =""
    document.form.CUST_NAME.value       =""
    form.FROM_WAREHOUSE.value         =""
    form.TO_WAREHOUSE.value         =""
    form.PERSON_INCHARGE.value =""
    form.CONTACT_NUM.value     =""
    form.DELDATE.value         =""
    form.COLLECTION_TIME.value =""
    form.REMARK1.value         =""
    form.REMARK2.value         =""
    form.JOB_NUM.value         =""
    
    //Ship Details Hidden feilds
     form.SCUST_NAME.value         =""
    form.SCONTACT_NAME.value =""
    form.SADDR1.value     =""
    form.SADDR2.value         =""
    form.SCITY.value =""
    	
    form.SCOUNTRY.value         =""
    form.SZIP.value         =""
     form.STELNO.value         =""
     
    document.form.action = "/track/TransferOrderServlet?Submit=Auto-Generate";
    document.form.submit();
   
}

function onClear(){
	 
	  document.form.TONO.value   =""
	  document.form.CUST_NAME.value       =""
	  form.PERSON_INCHARGE.value =""
      form.FROM_WAREHOUSE.value         =""
      form.TO_WAREHOUSE.value         =""
	  form.CONTACT_NUM.value     =""
	  form.COLLECTION_TIME.value  =""
	  form.DELDATE.value         =""
	  form.COLLECTION_TIME.value =""
	  form.REMARK1.value         =""
	  form.REMARK2.value         =""
	  form.JOB_NUM.value         =""
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
          
            //Ship Details Hidden feilds
            form.SCUST_NAME.value         =""
            form.SCONTACT_NAME.value =""
            form.SADDR1.value     =""
            form.SADDR2.value         =""
            form.SCITY.value =""
            	
            form.SCOUNTRY.value         =""
            form.SZIP.value         =""
            form.STELNO.value         =""
	  		  
	  
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
//-->
</script>


<jsp:useBean id="gn"  class="com.track.gates.Generator" />
<jsp:useBean id="su"  class="com.track.util.StrUtils" />
<jsp:useBean id="vmb" class="com.track.tables.VENDMST" />
<jsp:useBean id="phb" class="com.track.tables.POHDR" />
<jsp:useBean id="pdb" class="com.track.tables.PODET" />
<jsp:useBean id="du" class="com.track.util.DateUtils" />


<%
String DIRTYPE ="";
    String plant=(String)session.getAttribute("PLANT");
    String tono     = su.fString(request.getParameter("TONO"));
    String action   = su.fString(request.getParameter("action")).trim();
    String sUserId = (String) session.getAttribute("LOGIN_USER");
    DIRTYPE       = su.fString(request.getParameter("DIRTYPE"));
    session.setAttribute("RFLAG","1");
   
    String vend = "",deldate="",jobNum = "",custName = "",custCode="",personIncharge = "",contactNum = "";
    String remark1 = "",remark2 = "",address="",address2="",address3="",collectionDate="",collectionTime="";
    String contactname="",telno="",email="",add1="",add2="",add3="",add4="",country="",zip="",remarks="";
    String sCustName="",sContactName= "",sAddr1="",sAddr2="",sCity="",sCountry="",sZip="",sTelno="";
    String sSaveEnb    = "disabled";
    String fromWareHouse="",toWareHouse="";
	String isAutoGenerate = "false";
    String fieldDesc="<tr><td colspan=\"8\" align=\"center\">Please enter any search criteria</td></tr>";
    String UNITPRICE=su.fString(request.getParameter("UNITPRICE"));
    deldate=du.getDate();
    if(DIRTYPE.length()<=0){
      	 DIRTYPE = "TRANSFER";
      	 }

    if(action.equalsIgnoreCase("View")){
      	Map m=(Map)request.getSession().getAttribute("todetVal");
      	fieldDesc=(String)request.getSession().getAttribute("RESULT1");
        if(m.size()>0){
          	jobNum=(String)m.get("jobNum");
         	fromWareHouse=(String)m.get("fromwarehouse");
       	 	toWareHouse=(String)m.get("towarehouse");
         	custName=(String)m.get("custName");
         	custCode=(String)m.get("custCode");
         	
         	personIncharge=(String)m.get("contactname");
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
        
         	contactNum=(String)m.get("contactNum");
         	address=(String)m.get("address");
         	address2=(String)m.get("address2");
         	address3=(String)m.get("address3");
         	deldate=(String)m.get("collectionDate");
         	collectionTime=(String)m.get("collectionTime");
         	remark1=(String)m.get("remark1");
         	remark2=(String)m.get("remarks");
                
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
    }
     else if(action.equalsIgnoreCase("Auto-Generate")){
 
      	
      		 com.track.dao.TblControlDAO _TblControlDAO=new   com.track.dao.TblControlDAO();
      		_TblControlDAO.setmLogger(mLogger); 
      		tono = _TblControlDAO.getNextOrder(plant,sUserId,IConstants.TRANSFER);
      		isAutoGenerate = "true";
      		//tono=_ToHdrDAO.getNextOrder(plant);
      		deldate=du.getDate();
      		collectionTime=du.getTimeHHmm();
     
     		
    
  } 
  
%>
<div class="container-fluid m-t-20">
	 <div class="box"> 
            <div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title"><%=title %></h1>
		</div>
		
 <div class="box-body">

  <form class="form-horizontal" name="form" method="post" action="/track/TransferOrderServlet?">
    
    <INPUT type="Hidden" name="DIRTYPE" value="TRANSFER">
    <input type="hidden" name="xlAction" value="">
     <INPUT type = "hidden" name="SCUST_NAME" value ="<%=sCustName%>">
    <INPUT type = "hidden" name="SCONTACT_NAME" value ="<%=sContactName%>">
    <INPUT type = "hidden" name="SADDR1" value ="<%=sAddr1%>">
    <INPUT type = "hidden" name="SADDR2" value ="<%=sAddr2%>">
    <INPUT type = "hidden" name="SCITY" value ="<%=sCity%>">
    <INPUT type = "hidden" name="SCOUNTRY" value ="<%=sCountry%>">
    
    <INPUT type = "hidden" name="SZIP" value ="<%=sZip%>">
    <INPUT type = "hidden" name="STELNO" value ="<%=sTelno%>">
    
<div class="form-group">
       <label class="control-label col-sm-2" for="create transfer Order">
   		 	<i class="glyphicon glyphicon-star" style="font-size: 10px; top:-6px; color: #e50000"></i>&nbsp;Order Number:</label>
       <div class="col-sm-3">
      	    <div class="input-group">
    		<input name="TONO" type="TEXT" value="<%=tono%>" size="50" MAXLENGTH=20 class="form-control">
   		 	<span class="input-group-addon"  onClick="return onNew(document.form)">
   		 	<a href="#" data-toggle="tooltip" data-placement="top" title="Auto-Generate">
   		 	<i class="glyphicon glyphicon-edit" style="font-size: 20px;"></i></a></span>
  		</div>
  		</div>
  		<div class="form-inline">
  		<label class="control-label col-sm-3" for="Assignee name"><a href="#" data-placement="left">
   		 	<i class="glyphicon glyphicon-star" style="font-size: 10px; top:-6px; color: #e50000"></i></a>&nbsp;Customer Name/ID:</label>
        <div class="col-sm-3">
      	    <div class="input-group">
    		<input name="CUST_NAME" type="TEXT" value="<%=su.forHTMLTag(custName)%>" size="30" MAXLENGTH=100 class="form-control">
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
            <INPUT type = "Hidden" size="20"  MAXLENGTH=20 name="CONTACT_NUM" value="<%=contactNum%>">
 		
        <div class="form-group">
        <label class="control-label col-sm-2" for="From Location">
      		<a href="#" data-placement="left">
   			<i class="glyphicon glyphicon-star" style="font-size: 10px; top:-6px; color: #e50000"></i></a>&nbsp;From Location:</label>
        <div class="col-sm-3">
        <div class="input-group">          
        <INPUT  class="form-control" name="FROM_WAREHOUSE" type="TEXT" value="<%=fromWareHouse%>" size="50" MAXLENGTH=50>
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
        <INPUT  class="form-control" name="TO_WAREHOUSE" type="TEXT" value="<%=toWareHouse%>" size="50" MAXLENGTH=50>
        <span class="input-group-addon"  onClick="javascript:popUpWin('loc_list_transferto.jsp?TOLOC='+form.TO_WAREHOUSE.value);">
   		 	<a href="#" data-toggle="tooltip" data-placement="top" title="Location Details">
   		 	<i class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span>
      	</div>
    	</div>
    	<!-- <div class="form-inline">
    	<label class="control-label col-sm-2" for="From Location">Tel No/E-mail:</label>
        <div class="col-sm-2"> -->
        <INPUT  class="form-control" name="TELNO" type="hidden" value="<%=telno%>" size="30" MAXLENGTH=100 readonly>
        <!-- </div>
 		</div> -->
 		<!-- <div class="form-inline">
 		<div class="col-sm-offset-10"> -->
 		<INPUT  class="form-control" name="EMAIL" type="hidden" value="<%=email%>" size="25" MAXLENGTH=100 readonly>
 		<!-- </div>
 		</div> -->
 		<div class="form-inline">
        <label class="control-label col-sm-3" for="Remarks">Remarks:</label>
        <div class="col-sm-3">
        <INPUT  class="form-control" name="REMARK1" type="TEXT" value="<%=remark1%>" style="width: 100%" MAXLENGTH=100>
        </div>
    	<!-- <div class="form-inline">
    	<label class="control-label col-sm-2" for="Assigee Remarks">Assignee Remarks:</label>
        <div class="col-sm-2"> -->
        <INPUT  class="form-control" name="REMARK2" type="hidden" value="<%=remarks%>" size="30" MAXLENGTH=100 readonly>
        <!-- </div>
 		</div> -->
 		<!-- <div class="form-inline">
 		<div class="col-sm-offset-10">
 		<button type="button" class="Submit btn btn-default" name=addShippingAddr
 		onClick="javascript:popUpWin('shiptoAddr.jsp?ORDTYPE=Transfer&SCUST_NAME='+form.SCUST_NAME.value+'&SCONTACT_NAME='+form.SCONTACT_NAME.value+'&SADDR1='+form.SADDR1.value+'&SADDR2='+form.SADDR2.value+'&SCITY='+form.SCITY.value+'&SCOUNTRY='+form.SCOUNTRY.value+'&SZIP='+form.SZIP.value+'&STELNO='+form.STELNO.value);">
 		<b>Add Shipping Address</b></button>
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
    	<label class="control-label col-sm-2" for="Location">Unit no/Building:</label>
        <div class="col-sm-2"> -->
        <INPUT  class="form-control" name="ADD1" type="hidden" value="<%=add1%>" size="30" MAXLENGTH=100 readonly>
        <!-- </div>
 		</div> -->
 		<!-- <div class="form-inline">
 		<div class="col-sm-offset-10"> -->
 		<INPUT  class="form-control" name="ADD2" type="hidden" value="<%=add2%>" size="25" MAXLENGTH=100 readonly>
 		<!-- </div>
 		</div> -->
 		</div>
 		
<div class="form-group">
        <label class="control-label col-sm-2" for="Order Date">Order Date:</label>
        <div class="col-sm-3">
        <!-- <div class="input-group"> -->          
        <INPUT  class="form-control datepicker" name="DELDATE" type="TEXT" value="<%=deldate%>" size="30" MAXLENGTH=20>
      	<!-- </div> -->
    	</div>
    	<!-- <div class="form-inline">
    	<label class="control-label col-sm-2" for="Location">Street/City:</label>
        <div class="col-sm-2"> -->
        <INPUT  class="form-control" name="ADD3" type="hidden" value="<%=add3%>" size="30" MAXLENGTH=100 readonly>
        <!-- </div>
 		</div> -->
 		<!-- <div class="form-inline">
 		<div class="col-sm-offset-10"> -->
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
    	<label class="control-label col-sm-2" for="Location">Country/Postal code:</label>
        <div class="col-sm-2"> -->
        <INPUT  class="form-control" name="COUNTRY" type="hidden" value="<%=country%>" size="30" MAXLENGTH=100 readonly>
        <!-- </div>
 		</div> -->
 		<!-- <div class="form-inline">
 		<div class="col-sm-offset-10"> -->
 		<INPUT  class="form-control" name="ZIP" type="hidden" value="<%=zip%>" size="25" MAXLENGTH=100 readonly>
 		<!-- </div>
 		</div> -->
 		</div>
 		 		
 		
 		    
      	<div class="form-group">        
      	<div class="col-sm-offset-5 col-sm-7">
        <button type="button" class="Submit btn btn-default" onClick="onClear();"><b>Clear</b></button>&nbsp;&nbsp;
      	<button type="button" class="Submit btn btn-default" data-toggle="modal" data-target="#myModal" onClick="return validateMandatoryFeilds(document.form)"><b>Save</b></button>&nbsp;&nbsp;
      	<INPUT type = "hidden" name="ISAUTOGENERATE"  value="<%=isAutoGenerate%>">
        </div>
    	</div>
  		
  		 <TABLE  style="font-size:15px" BORDER="0" CELLSPACING="0" WIDTH="100%" class="table">
         <thead style="background: #eaeafa">
         <tr >
         <th width="10%">Order Line No </th>
         <th width="17%">Product ID </th>
         <th width="27%">Description </th>
         <th width="12%">Order Qty </th>
         <th width="12%">Pick Qty </th>
         <th width="12%">Receive Qty </th>
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
         <INPUT type = "Hidden"  name="UNITPRICE"  value="<%=UNITPRICE%>">
  		
  		<div class="form-group">        
      	<div class="col-sm-12" align="center">
      	<button type="button" class="Submit btn btn-default" onClick="window.location.href='../home'"><b>Back</b></button>&nbsp;&nbsp;
        <!-- <button type="button" class="Submit btn btn-default" onClick="{backNavigation('orderManagement.jsp','TBO');}"><b>Back</b></button>&nbsp;&nbsp; -->
      	<button type="button" class="Submit btn btn-default" onClick="submitForm('Add Products');"><b>Add Product</b></button>&nbsp;&nbsp;
      	<button type="button" class="Submit btn btn-default" onClick="submitFormPrint('Print Transfer Order'); "><b>Print Consignment Order</b></button>&nbsp;&nbsp;
      	<button type="button" class="Submit btn btn-default" onClick="submitForm('Export To Excel');"><b>Export To Excel</b></button>&nbsp;&nbsp;
        
        </div>
    	</div>
  		
  		</form>
		</div>
		</div>
		</div>
		


<jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
</jsp:include>

