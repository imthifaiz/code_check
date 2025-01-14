<%@ include file="header.jsp"%>
<%@ page import="com.track.dao.TblControlDAO"%>
<%@ page import="com.track.constants.*"%>
<%@ page import="com.track.dao.MovHisDAO"%>
<%
String title = "Kitting Dekitting with BOM";
%>
<%@include file="sessionCheck.jsp" %>
<jsp:include page="header2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
	<jsp:param name="mainmenu" value="<%=IConstants.IN_HOUSE%>"/>
    <jsp:param name="submenu" value="<%=IConstants.IN_HOUSE_SUB_MENU%>"/>
</jsp:include>


<script language="JavaScript" type="text/javascript" src="js/general.js"></script>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<script language="JavaScript" type="text/javascript" src="js/json2.js"></script>

<SCRIPT LANGUAGE="JavaScript">

var subWin = null;
function popUpWin(URL) {
	
 		subWin = window.open(URL, 'ProductionBOM', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=800,height=400,left = 200,top = 184');
		
}
</SCRIPT>


<%
session = request.getSession();
String sUserId = (String) session.getAttribute("LOGIN_USER");
String res        = "",action     =   "",pitem="",pbatch="",citem="",cbatch="",qty="",EMP_ID="",EMP_NAME="",EMP_LNAME="",LOC_ID="",pqty="",
LOC_DESC="",LOC_TYPE_ID="",LOC_TYPE_DESC="",REMARKS="",REASONCODE="",fieldDesc="",allChecked="";

session= request.getSession();
StrUtils strUtils = new StrUtils();
DateUtils dateutils = new DateUtils();
LocTypeUtil loctypeutil = new LocTypeUtil();

action            = strUtils.fString(request.getParameter("action"));
String plant = (String)session.getAttribute("PLANT");
EMP_ID  = strUtils.fString(request.getParameter("EMP_ID"));
EMP_NAME  = strUtils.fString(request.getParameter("EMP_NAME"));
EMP_LNAME  = strUtils.fString(request.getParameter("EMP_LNAME"));
LOC_ID  = strUtils.fString(request.getParameter("LOC_ID"));
LOC_DESC  = strUtils.fString(request.getParameter("LOC_DESC"));
LOC_TYPE_ID  = strUtils.fString(request.getParameter("LOC_TYPE_ID"));
LOC_TYPE_DESC  = strUtils.fString(request.getParameter("LOC_TYPE_DESC"));
REMARKS  = strUtils.fString(request.getParameter("REMARKS"));
REASONCODE  = strUtils.fString(request.getParameter("REASONCODE"));
pitem  = strUtils.fString(request.getParameter("ITEM"));
pbatch  = strUtils.fString(request.getParameter("BATCH_0"));
pqty  = strUtils.fString(request.getParameter("PARENTQTY"));
citem  = strUtils.fString(request.getParameter("CITEM"));
cbatch  = strUtils.fString(request.getParameter("BATCH_1"));
qty  = strUtils.fString(request.getParameter("QTY"));
fieldDesc=(String)session.getAttribute("MESSAGE");
if(pqty.equalsIgnoreCase(""))
{
	pqty="1";
}

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

%>
<div class="container-fluid m-t-20">
	 <div class="box"> 
            <div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title"><%=title %></h1>
		</div>
		
 <div class="box-body">

<form class="form-horizontal" name="form" method="post" action="/track/ProductionBomServlet?">
<INPUT type="hidden" name="RFLAG" value="2">

<!--  <table  border="0" width="100%" cellspacing="1" cellpadding="2"  bgcolor="" align="center">
 	<tr>
 		<td align="center">
 			<font class="maingreen"> <%=fieldDesc%></font>
 		</td>
 	</tr>
 </table>-->
 <div id = "ERROR_MSG"></div>
   <div id = "COMPLETED_MSG"></div>
   
 
            <div class="box-header menu-drop">
              <h2><small>Employee/Location/Remarks/Reason Code</small></h2>
		</div>


 
 <div class="form-group">
  <label class="control-label col-sm-3" for="Employee ID">
  <i class="glyphicon glyphicon-star" style="font-size: 10px; top:-6px; color: #e50000"></i>&nbsp;Employee ID:</label>
      <div class="col-sm-3">
         <div class="input-group">    
    		<input class="form-control"  name="EMP_ID" id="EMP_ID" type = "TEXT" value="<%=EMP_ID%>" size="20"  MAXLENGTH=20 onkeypress="if((event.keyCode=='13') && ( this.value.length > 0)){validateEmployee();}">
   		 	<span class="input-group-addon"  onClick="javascript:popUpWin('employee_list.jsp?EMP_NAME='+form.EMP_ID.value+'&TYPE=KITDEKIT&FORM=form');">
   		 	<a href="#" data-toggle="tooltip" data-placement="top" title="Employee Details">
   		 	<i class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span>
  		</div>        
       </div>
 <div class="form-inline">
  <label class="control-label col-sm-3" for="Location ID">Location ID:</label>
      <div class="col-sm-3">
         <div class="input-group">    
    		<input class="form-control" name="LOC_ID" id="LOC_ID"  type = "TEXT" value="<%=LOC_ID%>" size="20"  MAXLENGTH=20 onkeypress="if((event.keyCode=='13') && ( this.value.length > 0)){validateLocation();}">
   		 	<span class="input-group-addon"  onClick="javascript:popUpWin('loc_list_MultiReceivewms.jsp?LOC='+form.LOC_ID.value+'&TYPE=KITDEKIT');">
   		 	<a href="#" data-toggle="tooltip" data-placement="top" title="Location Details">
   		 	<i class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span>
  		</div>        
       </div>
       </div>
        </div>
        
 <div class="form-group">
   <label class="control-label col-sm-3" for="Employee First Name">Employee First Name:</label>
      <div class="col-sm-3">
         <div class="input-group">    
    		<input class="form-control" name="EMP_NAME" id="EMP_NAME" type = "TEXT" value="<%=EMP_NAME%>" size="20"  MAXLENGTH=20>
   		 	<span class="input-group-addon"  onClick="javascript:popUpWin('employee_list.jsp?EMP_NAME='+form.EMP_NAME.value+'&TYPE=KITDEKIT&FORM=form');">
   		 	<a href="#" data-toggle="tooltip" data-placement="top" title="Employee Details">
   		 	<i class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span>
  		</div>        
       </div>
<div class="form-inline">
   <label class="control-label col-sm-3" for="Location Description">Location Description:</label>
      <div class="col-sm-3">
         <div class="input-group">    
    		<input class="form-control" name="LOC_DESC" id="LOC_DESC" type = "TEXT" value="<%=LOC_DESC%>" size="20"  MAXLENGTH=20>
   		 	<span class="input-group-addon" onClick="javascript:popUpWin('loc_list_MultiReceivewms.jsp?LOC='+form.LOC_DESC.value+'&TYPE=KITDEKIT');" >
   		 	<a href="#" data-toggle="tooltip" data-placement="top" title="Location Details">
   		 	<i class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span>
  		</div>        
       </div>
       </div>
       </div>
 <div class="form-group">
   <label class="control-label col-sm-3" for="Employee Last Name">Employee Last Name:</label>
      <div class="col-sm-3">
         <div class="input-group">    
    		<input class="form-control" name="EMP_LNAME"  id="EMP_LNAME" type = "TEXT" value="<%=EMP_LNAME%>" size="20"  MAXLENGTH=20>
   		 	<span class="input-group-addon" onClick="javascript:popUpWin('employee_list.jsp?EMP_NAME='+form.EMP_LNAME.value+'&TYPE=KITDEKIT&FORM=form');">
   		 	<a href="#" data-toggle="tooltip" data-placement="top" title="Employee Details">
   		 	<i class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span>
  		</div>        
       </div>
<div class="form-inline">
   <label class="control-label col-sm-3" for="Location Type">Location Type:</label>
      <div class="col-sm-3">
         <div class="input-group">    
    		<input class="form-control" name="LOC_TYPE_ID" id="LOC_TYPE_ID" type = "TEXT" value="<%=LOC_TYPE_ID%>" size="20"  MAXLENGTH=20 onkeypress="if((event.keyCode=='13') && ( this.value.length > 0)){validateLocationtype();}">
   		 	<span class="input-group-addon"  onClick="javascript:popUpWin('LocTypeList.jsp?LOC_TYPE_ID='+form.LOC_TYPE_ID.value+'&TYPE=KITDEKIT');">
   		 	<a href="#" data-toggle="tooltip" data-placement="top" title="Location Details">
   		 	<i class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span>
  		</div>        
       </div>
       </div>
       </div>
       
       
<div class="form-group">
  <label class="control-label col-sm-3" for="Remarks">Remarks:</label>
      <div class="col-sm-3">    
    		<input class="form-control" name="REMARKS" id="REMARKS"  type = "TEXT" value="<%=REMARKS%>" size="20"  MAXLENGTH=100>
  		</div>
 <div class="form-inline">
   <label class="control-label col-sm-3" for="Location Type Description">Location Type Description:</label>
      <div class="col-sm-3">
         <div class="input-group">    
    		<input class="form-control" name="LOC_TYPE_DESC" id="LOC_TYPE_DESC" type = "TEXT" value="<%=LOC_TYPE_DESC%>" size="20"  MAXLENGTH=20>
   		 	<span class="input-group-addon"  onClick="javascript:popUpWin('LocTypeList.jsp?LOC_TYPE_ID='+form.LOC_TYPE_DESC.value+'&TYPE=KITDEKIT');">
   		 	<a href="#" data-toggle="tooltip" data-placement="top" title="Location Details">
   		 	<i class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span>
  		</div>        
       </div>
       </div>
  		</div>
  		            
 <div class="form-group">
   <label class="control-label col-sm-3" for="Reason Code">Reason Code:</label>
      <div class="col-sm-3">
         <div class="input-group">    
    		<input class="form-control" name="REASONCODE" id="REASONCODE" type="TEXT" value="<%=REASONCODE%>" size="20" MAXLENGTH=20 onkeypress="if((event.keyCode=='13') && ( this.value.length > 0)){validateRsncode();}">
   		 	<span class="input-group-addon"  onClick="javascript:popUpWin('list/ReasonMstList.jsp?ITEM_ID='+form.REASONCODE.value+'&TYPE=KITDEKIT');">
   		 	<a href="#" data-toggle="tooltip" data-placement="top" title="Reason Code Details">
   		 	<i class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span>
  		</div>        
       </div>
       </div>
 
       

<div class="box-header menu-drop">
              <h2><small>Parent Product Details</small></h2>
		</div>


       
<div class="form-group">
     <div class="col-sm-offset-1 col-sm-11">
          <label class="checkbox-inline">      
            <input type = "checkbox" class="form-check-input" style="border:0;" name = "pserialqty"  id = "pserialqty" value="pserialqty" checked="checked" onclick="ParentSerialqty();"><b>Serialized</b></label>
            </div>
                 </div>

<div class="form-group">
  <label class="control-label col-sm-3" for="Parent Product ID">
  <i class="glyphicon glyphicon-star" style="font-size: 10px; top:-6px; color: #e50000"></i>&nbsp;Parent Product ID:</label>
      <div class="col-sm-2">
         <div class="input-group">    
    		<input class="form-control" type="TEXT" size="20" MAXLENGTH=50 name="ITEM" id="ITEM"	value="<%=pitem%>"	onkeypress="if((event.keyCode=='13') && ( document.form.ITEM.value.length > 0)){validateProduct();}">
   		 	<span class="input-group-addon"  onClick="javascript:popUpWin('ProductionBOMPitemList.jsp?ITEM='+form.ITEM.value+'&TYPE=KITBOM');">
   		 	<a href="#" data-toggle="tooltip" data-placement="top" title="Parent Product Details">
   		 	<i class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span>
  		</div>        
       </div>
<div class="form-inline">
<div class="col-sm-1">
    <button type="button" class="Submit btn btn-default" value="View" name="Submit" onclick="onGo(1)"><b>View</b></button>&nbsp;&nbsp;
       </div>
          </div>
<div class="form-inline">
  <label class="control-label col-sm-3" for="Batch/Serial Number">Batch/Serial Number:</label>
      <div class="col-sm-3">
         <div class="input-group">    
    		<input class="form-control" name="BATCH_0" id="BATCH_0" type="TEXT" value="<%=pbatch%>" size="20" MAXLENGTH=40>
   		 	<span class="input-group-addon"  onClick="javascript:popUpWin('parentbatch_list_kitting.jsp?ITEM='+form.ITEM.value+'&PARENTBATCH='+form.BATCH_0.value+'&TYPE=KITCHECK');">
   		 	<a href="#" data-toggle="tooltip" data-placement="top" title="Batch/Serial Details">
   		 	<i class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span>
  		</div>        
       </div>
          </div>
          </div>
 <div class="form-group">
   <label class="control-label col-sm-3" for="Product Description">Product Description:</label>
      <div class="col-sm-3">
         <div class="input-group">    
    		<input class="form-control" name="DESC" id="DESC" type="TEXT" value="" size="20">
   		 	<span class="input-group-addon"  onClick="javascript:popUpWin('ProductionBOMPitemList.jsp?ITEM='+form.ITEM.value+'&TYPE=KITBOM');">
   		 	<a href="#" data-toggle="tooltip" data-placement="top" title="Product Details">
   		 	<i class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span>
  		</div>        
       </div>
<div class=form-inline>
   <div class="col-sm-offset-3 col-sm-3">
    <button type="button" class="Submit btn btn-default" onclick="generateBatch(0);" size="6" value="Generate Batch" name="actionBatch"><b>Generate Batch</b></button>&nbsp;&nbsp;
       </div>
          </div>
       </div>
       
 <div class="form-group">
   <label class="control-label col-sm-3" for="Product Detail Description">Product Detail Description:</label>
      <div class="col-sm-3">    
    		<input class="form-control" name="DETDESC" id="DETDESC" type="TEXT" value="" size="25" readonly>
  		</div>
<div class=form-inline>
   <label class="control-label col-sm-3" for="Qty">Qty:</label>
      <div class="col-sm-3">    
    		<input class="form-control" name="PARENTQTY" type="TEXT" id="PARENTQTY" value="<%=pqty%>" size="4" MAXLENGTH=50 readonly>       
       </div>
           </div>        
       </div>
       

<div class="box-header menu-drop">
              <h2><small>Child Product Details</small></h2>
		</div>
   
 
 
<div class="form-group">
     <div class="col-sm-offset-1 col-sm-11">
          <label class="checkbox-inline">      
            <input type = "checkbox" class="form-check-input"  style="border:0;" name = "serialqty"  id = "serialqty" value="serialqty" onclick="Serialqty();"><b>Serialized</b></label>
            </div>
                 </div>
                     
       
<div class="form-group">
  <label class="control-label col-sm-3" for="Product ID">
  <i class="glyphicon glyphicon-star" style="font-size: 10px; top:-6px; color: #e50000"></i>&nbsp;Product ID:</label>
      <div class="col-sm-3">
         <div class="input-group">    
    		<input class="form-control" type="TEXT" size="20" MAXLENGTH=50 name="CITEM" id="CITEM" value="<%=citem%>" onkeypress="if((event.keyCode=='13') && ( document.form.CITEM.value.length > 0)){ValidateChildProduct();}">
   		 	<span class="input-group-addon" onClick="javascript:popUpWin('ProductionBOMchilditemList.jsp?PITEM='+form.ITEM.value+'&CITEM='+form.CITEM.value+'&TYPE=KITWITHBOM');">
   		 	<a href="#" data-toggle="tooltip" data-placement="top" title="Child Product Details">
   		 	<i class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span>
  		</div>        
       </div>
       
 <div class="form-inline">
   <label class="control-label col-sm-3" for="Batch/Serial Number">Batch/Serial Number:</label>
      <div class="col-sm-3">
         <div class="input-group">    
    		<input class="form-control" name="BATCH_1" id="BATCH_1" type="TEXT" value="<%=cbatch%>" size="20" MAXLENGTH=40
					onkeypress="if((event.keyCode=='13') && ( document.form.BATCH_1.value.length > 0)){validateBatch(1);}">
   		 	<span class="input-group-addon" onClick="javascript:popUpWin('batch_list_locationtransfer.jsp?FROM_LOC='+form.LOC_ID.value+'&ITEMNO='+form.CITEM.value+'&BATCH='+form.BATCH_1.value+'&TYPE=KITDEKIT');">
   		 	<a href="#" data-toggle="tooltip" data-placement="top" title="Batch/Serial Details">
   		 	<i class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span>
  		</div>        
       </div>
        </div>
        </div>
 <div class="form-group">
   <label class="control-label col-sm-3" for="Product Description">Product Description:</label>
      <div class="col-sm-3">
         <div class="input-group">    
    		<input class="form-control" name="CDESC" id="CDESC" type="TEXT" value="" size="20">
   		 	<span class="input-group-addon"  onClick="javascript:popUpWin('ProductionBOMchilditemList.jsp?PITEM='+form.ITEM.value+'&CITEM='+form.CITEM.value+'&TYPE=KITWITHBOM');">
   		 	<a href="#" data-toggle="tooltip" data-placement="top" title="Product Details">
   		 	<i class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span>
  		</div>        
       </div>
<div class=form-inline>
   <label class="control-label col-sm-3" for="Qty">Qty:</label>
      <div class="col-sm-3">   
    		<input class="form-control" name="QTY" type="TEXT" id="QTY" value="<%=qty%>" size="4" MAXLENGTH=50
						onkeypress="if((event.keyCode=='13') && ( document.form.QTY.value.length > 0)){validateQuantity();}">
  	</div>        
        </div>
       </div>
       
 <div class="form-group">
   <label class="control-label col-sm-3" for="Product Detail Description">Product Detail Description:</label>
      <div class="col-sm-3">   
    		<input class="form-control" name="CDETDESC" id="CDETDESC" type="TEXT" value="" size="25" readonly>
   		</div>        
       </div>
      
  
  <div class="form-group">        
     <div class="col-sm-12" align="center">
     <button type="button" class="Submit btn btn-default" onClick="window.location.href='../home'"><b>Back</b></button>&nbsp;&nbsp;
     <!-- <button type="button" class="Submit btn btn-default" onClick="window.location.href='inPremises.jsp'"><b>Back</b></button>&nbsp;&nbsp; -->
      <button type="button" class="Submit btn btn-default" value="Kit" onClick="onAdd()"><b>Kit</b></button>&nbsp;&nbsp;
      	<button type="button" class="Submit btn btn-default" value="Clear" onClick="onClear();"><b>Clear</b></button>&nbsp;&nbsp;
      	
     </div>
       </div>        
      
<div id="RESULT_MESSAGE">
 <table border="0" cellspacing="0" cellpadding="0"  WIDTH="60%"  align = "center">
	<tr><td align="center"><%=(String)session.getAttribute("MESSAGE")%></td></tr>
	
</table>
</div>
  
  <div class="row">
  	<div class="col-12 col-sm-12"><INPUT Type=Checkbox  class="form-check-input" style="border:0;" name = "select" value="select" <%if(allChecked.equalsIgnoreCase("true")){%>checked <%}%>onclick="return checkAll(this.checked);">
                     <strong>  &nbsp; Select/Unselect </strong> </div>
  </div>

  <div id="VIEW_RESULT_HERE"></div>
  <div id="spinnerImg" ></div>
  
  <div class="form-group">       
      <div class="col-sm-12" align="center">
      <button type="button" class="Submit btn btn-default" value="De-Kit" onClick="onDelete()"><b>De-Kit</b></button>&nbsp;&nbsp;
       </div>
          </div> 
<input type="hidden" name="PTYPE" id="PTYPE" value="CREATEKITBOM">
</form>
</div>
  </div>
  </div>
       

<script> 
onGo(0);
function validateEmployee() {
	
	var empId = document.getElementById("EMP_ID").value;
	if(empId=="" || empId.length==0 ) {
		alert("Enter Employee!");
		document.getElementById("EMP_ID").focus();
	}else{
		var urlStr = "/track/ClockInOutServlet";
		$.ajax( {
			type : "POST",
			url : urlStr,
			data : {
				CUST_CODE : empId,
				PLANT : "<%=plant%>",
				Submit : "VALIDATE_EMPLOYEE"
				},
				dataType : "json",
				success : function(data) {
					if (data.status == "100") {
						var resultVal = data.result;
						document.getElementById("EMP_NAME").value = resultVal.empname;
						document.getElementById("EMP_LNAME").value = resultVal.emplastname;
						document.getElementById("LOC_ID").focus();
					} else {
						alert("Not a valid Employee");
						document.getElementById("EMP_ID").value = "";
						document.getElementById("EMP_NAME").value = "";
						document.getElementById("EMP_LNAME").value = "";
						document.getElementById("EMP_ID").focus();
					}
				}
			});
		}
	}
function validateLocation() {
	var locId = document.getElementById("LOC_ID").value;
	if(locId=="" || locId.length==0 ) {
		alert("Enter Location!");
		document.getElementById("LOC_ID").focus();
	}else{
		var urlStr = "/track/MiscOrderHandlingServlet";
		$.ajax( {
			type : "POST",
			url : urlStr,
			data : {
				LOC : locId,
				PLANT : "<%=plant%>",
					ACTION : "VALIDATE_LOCATION"
				},
				dataType : "json",
				success : function(data) {
					if (data.status == "100") {
						var resultVal = data.result;
						document.getElementById("LOC_DESC").value = resultVal.locdesc;
						document.getElementById("LOC_TYPE_ID").focus();
					} else {
						alert("Not a valid Location");
						document.getElementById("LOC_ID").value = "";
						document.getElementById("LOC_ID").focus();
					}
				}
			});
		}
	}
function validateLocationtype() {
	var loctypeId = document.getElementById("LOC_TYPE_ID").value;
	if(loctypeId=="" || loctypeId.length==0 ) {
		alert("Enter Location Type!");
		document.getElementById("LOC_TYPE_ID").focus();
	}else{
		var urlStr = "/track/MiscOrderHandlingServlet";
		$.ajax( {
			type : "POST",
			url : urlStr,
			data : {
				LOCTYPE : loctypeId,
				PLANT : "<%=plant%>",
					ACTION : "VALIDATE_LOCTYPE"
				},
				dataType : "json",
				success : function(data) {
					if (data.status == "100") {
						var resultVal = data.result;
						document.getElementById("LOC_TYPE_DESC").value = resultVal.loctypedesc;
						document.getElementById("REMARKS").focus();
					} else {
						alert("Not a valid Location");
						document.getElementById("LOC_TYPE_ID").value = "";
						document.getElementById("LOC_TYPE_ID").focus();
					}
				}
			});
		}
	}
function validateRsncode() {
	var rsncode = document.getElementById("REASONCODE").value;
	if(rsncode=="" || rsncode.length==0 ) {
		alert("Enter Reason code!");
		document.getElementById("REASONCODE").focus();
	}else{
		var urlStr = "/track/MiscOrderHandlingServlet";
		$.ajax( {
			type : "POST",
			url : urlStr,
			data : {
				RSNCODE : rsncode,
				PLANT : "<%=plant%>",
					ACTION : "VALIDATE_RSNCODE"
				},
				dataType : "json",
				success : function(data) {
					if (data.status == "100") {
						document.getElementById("ITEM").focus();
					} else {
						alert("Not a valid Reason Code");
						document.getElementById("REASONCODE").value = "";
						document.getElementById("REASONCODE").focus();
					}
				}
			});
		}
	}
function validateProduct() {
	var productId = document.form.ITEM.value;
	if(productId=="" || productId.length==0 ) {
		alert("Enter Product");
		document.getElementById("ITEM").focus();
	}else{
	var urlStr = "/track/ProductionBomServlet";
	$.ajax( {
		type : "POST",
		url : urlStr,
		data : {
		ITEM : productId,
		PLANT : "<%=plant%>",
		action : "VALIDATE_PARENT_PRODUCT"
					},
					dataType : "json",
					success : function(data) {
						if (data.status == "100") {
							var resultVal = data.result;
							document.getElementById("DESC").value = resultVal.discription;
							document.getElementById("DETDESC").value = resultVal.detaildesc;
							document.form.BATCH_0.value = "NOBATCH";
							document.form.BATCH_0.focus();
							document.form.BATCH_0.select();
							
						} else {
							alert("Not a valid Parent product!");
							document.form.ITEM.value = "";
							document.form.ITEM.focus();
						}
					}
				});
		}
	}	

function generateBatch(index){
	var currentbatch=index;
		var urlStr = "/track/MiscOrderHandlingServlet";
		$.ajax( {
			type : "POST",
			url : urlStr,
			data : {
			PLANT : "<%=plant%>",
			ACTION : "GENERATE_BATCH"
		},
		dataType : "json",
		success : function(data) {
			if (data.status == "100") {
				var resultVal = data.result;
				document.getElementById("BATCH_"+currentbatch).value = resultVal.batchCode;
				//document.getElementById("QTY_"+currentbatch).focus();

			} else {
				alert("Unable to genarate Batch");
				document.getElementById("BATCH_"+currentbatch).value = "";
				document.getElementById("BATCH_"+currentbatch).focus();
			}
		}
	});
}
function ParentSerialqty()
{   if(document.getElementById("pserialqty").checked == true)
	{
		document.form.PARENTQTY.value=1;
		document.form.PARENTQTY.readOnly=true; 
	}
	else
	{
		document.form.PARENTQTY.value="";
		document.form.PARENTQTY.readOnly=false; 
	}
}
function Serialqty()
{   if(document.getElementById("serialqty").checked == true)
	{
		document.form.QTY.value=1;
		document.form.QTY.readOnly=true; 
	}
	else
	{
		document.form.QTY.value="";
		document.form.QTY.readOnly=false; 
	}
}
function ValidateChildProduct() {
	var parentproduct = document.form.ITEM.value;
	var childproduct = document.form.CITEM.value;
	if(childproduct=="" || childproduct.length==0 ) {
		alert("Enter Child Product");
		document.getElementById("CITEM").focus();
	}else{
	var urlStr = "/track/ProductionBomServlet";
	$.ajax( {
		type : "POST",
		url : urlStr,
		data : {
		PITEM : parentproduct,	
		CITEM : childproduct,
		PLANT : "<%=plant%>",
		action : "VALIDATE_CHILDEQUI_PRODUCT"
					},
					dataType : "json",
					success : function(data) {
						if (data.status == "100") {
							var resultVal = data.result;
							document.getElementById("CDESC").value = resultVal.discription;
							document.getElementById("CDETDESC").value = resultVal.detaildesc;
							document.form.BATCH_1.value = "NOBATCH";
							document.form.BATCH_1.focus();
							document.form.BATCH_1.select();
						} else {
							alert("Not a valid child  or equivalent product!");
							document.form.CITEM.value = "";
							document.form.CITEM.focus();
						}
					}
				});
		}
	}

function validateBatch(index) {
	var batch = document.getElementById("BATCH_1").value;
	var productId = document.getElementById("CITEM").value;
	var locId = document.getElementById("LOC_ID").value;
	//var nonstkflg = document.getElementById("NONSTKFLG"+"_"+index).value;
	
	if(locId=="" || locId.length==0 ) {
		alert("Enter Location!");
		document.form.LOC_ID.focus();
	}else{
		if(document.getElementById("CITEM").value=="" || document.getElementById("CITEM").value.length==0 ) {
			alert("Enter Product ID!");
			document.getElementById("CITEM").focus();
		}
		else{
			if (batch == "" || batch.length == 0) {
				alert("Enter Batch!");
				document.getElementById("BATCH_1").focus();
			} 
				var urlStr = "/track/MiscOrderHandlingServlet";
				$.ajax( {
					type : "POST",
					url : urlStr,
					data : {
						ITEM : productId,
						LOC : locId,
						BATCH : batch,
						PLANT : "<%=plant%>",
						ACTION : "VALIDATE_BATCH"
						},
						dataType : "json",
						success : function(data) {
							
							if (data.status == "100") {
								var resultVal = data.result;
								document.getElementById("BATCH"+"_"+index).value = resultVal.batchCode;
								if(document.getElementById("serialqty").checked == true)
								{
									validateQuantity();	
								}
								else
								{
									document.getElementById("QTY").focus();
								}
								
                                 
							} else {
								alert("Not a valid Batch");
								document.getElementById("BATCH"+"_"+index).value = "";
								document.getElementById("BATCH"+"_"+index).focus();
							}
						}
					});
			}
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
function onClear()
{
	document.getElementById("EMP_ID").value = ""
	document.getElementById("EMP_NAME").value = ""
	document.getElementById("EMP_LNAME").value = ""
	document.getElementById("LOC_ID").value = ""
	document.getElementById("LOC_DESC").value = ""
	document.getElementById("LOC_TYPE_ID").value = ""
	document.getElementById("LOC_TYPE_DESC").value = ""
	document.getElementById("REMARKS").value = ""
	document.getElementById("REASONCODE").value = ""
	document.getElementById("ITEM").value = ""
	document.getElementById("DESC").value = ""
	document.getElementById("DETDESC").value = ""
	document.getElementById("BATCH_0").value = ""
	document.getElementById("CITEM").value = ""
	document.getElementById("CDESC").value = ""
	document.getElementById("CDETDESC").value = ""
	document.getElementById("BATCH_1").value = ""
	document.getElementById("QTY").value = ""
	
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

function onAdd() {
	var emp = document.form.EMP_ID.value;
	var loc = document.form.LOC_ID.value;
	var loctype = document.form.LOC_TYPE_ID.value;
	var remarks = document.form.REMARKS.value;
	var rsncode = document.form.REASONCODE.value;
	var pitem = document.form.ITEM.value;
	var pbatch = document.form.BATCH_0.value;
	var citem = document.form.CITEM.value;
	var cbatch = document.form.BATCH_1.value;
	var qty = document.form.QTY.value;
	var pqty = document.form.PARENTQTY.value;
	
	if(emp=="" || emp.length==0 ) {
		alert("Enter Employee");
		document.getElementById("EMP_ID").focus();
		return false;
	}
	if(loc=="" || loc.length==0 ) {
		alert("Enter Location");
		document.getElementById("LOC_ID").focus();
		return false;
	}
	if(pitem=="" || pitem.length==0 ) {
		alert("Enter Parent Product");
		document.getElementById("ITEM").focus();
		return false;
	}
	if(pbatch=="" || pbatch.length==0 ) {
		alert("Enter Parent Product Batch");
		document.getElementById("BATCH_0").focus();
		return false;
	}
	if(citem=="" || citem.length==0 ) {
		alert("Enter Child Product");
		document.getElementById("CITEM").focus();
		return false;
	}
	if(cbatch=="" || cbatch.length==0 ) {
		alert("Enter Child Product Batch");
		document.getElementById("BATCH_1").focus();
		return false;
	}
	if (qty == "" || qty.length == 0) {
		alert("Enter Child Quantity");
		document.getElementById("QTY").focus();
		return false;
	} else {
		if (isNumericInput(qty) == false) {
			alert("Entered Quantity is not a valid Qty!");
			return false;
		} 

	}
	
	if(pitem==citem ) {
		alert("Child Product cannot be same as Parent Product.Choose diff child product.");
		document.form.CITEM.value = "";
		document.getElementById("CITEM").focus();
		return false;
	}
	
	var table=document.getElementById("tabledata");
	var len = table.rows.length;
	var scanitem = trim((document.getElementById("CITEM").value).toUpperCase());
	var scanqty = parseInt(document.getElementById("QTY").value);
	for ( var i = 1; i < len; i++) {
		
		var item = (document.getElementById("citem_" + i).innerText).toUpperCase();
		var eitem = (document.getElementById("eitem_" + i).innerText).toUpperCase();
		var item = trim(item);
		var eitem = trim(eitem);
		//var bomqty = parseInt(document.getElementById("bomqty_" + i).innerText) ;
		var kitqty = parseInt(document.getElementById("kitqty_" + i).innerText) ;
		var tranqty = parseInt(document.getElementById("tranqty_" + i).innerText) ;
		
		var balqty = parseInt(scanqty)+parseInt(kitqty);	
			
		if(scanitem == item || scanitem == eitem){
			if(balqty>tranqty)
			{
				alert("Scan Qty Exceeds Bom Qty for the Product Scanned.");
				document.form.CITEM.select();
    			document.form.CITEM.focus();
				return false;
			}
	
		}
	
	}	
	
	document.getElementById('RESULT_MESSAGE').innerHTML = '';   
    document.getElementById('VIEW_RESULT_HERE').innerHTML = '';
    document.getElementById('spinnerImg').innerHTML ='<br><br><p align=center><img src="images/spinner.gif"  > </p> ';
    var urlStr = "/track/ProductionBomServlet";
    
    // Call the method of JQuery Ajax provided
    $.ajax({type: "POST",url: urlStr, data: {EMP_ID:emp,LOC_ID:loc,LOC_TYPE_ID:loctype,REMARKS:remarks,REASONCODE:rsncode,ITEM:pitem,BATCH_0:pbatch,CITEM:citem,BATCH_1:cbatch,QTY:qty,PARENTQTY:pqty,KITTYPE:"KITDEKITWITHBOM",action: "ADD_KITTING_WITHBOM",PLANT:"<%=plant%>"},dataType: "json", success: callback });
    document.form.CITEM.value = "";
    document.form.CDESC.value = "";
    document.form.CDETDESC.value = "";
    document.form.BATCH_1.value = "";
    if(document.getElementById("serialqty").checked == true)
    {
   		document.form.QTY.value = "1";
    }
    else
    {
    	document.form.QTY.value = "";	
    }
    document.getElementById("CITEM").focus();
    
 }

function onGo(index) {

	var index = index;
	var product = document.form.ITEM.value;
	var loc = document.form.LOC_ID.value;
	var pbatch = document.form.BATCH_0.value;
	var pqty = document.form.PARENTQTY.value;
	
	if(index == '1'){
	if(product=="" || product.length==0 ) {
		alert("Enter Product");
		document.getElementById("ITEM").focus();
		return false;
		}
	if(pqty=="" || pqty.length==0 ) {
		alert("Please Enter Qty of Parent Product to be kitted.Please ensure sufficient qty of the Child Product before proceeding with the kit");
		document.getElementById("PARENTQTY").focus();
		return false;
		}
     
	}
	document.getElementById('RESULT_MESSAGE').innerHTML = ''; 
    document.getElementById('VIEW_RESULT_HERE').innerHTML = '';
    document.getElementById('spinnerImg').innerHTML ='<br><br><p align=center><img src="images/spinner.gif"  > </p> ';
    var urlStr = "/track/ProductionBomServlet";
    
    // Call the method of JQuery Ajax provided
    $.ajax({type: "POST",url: urlStr, data: {ITEM:product,BATCH_0:pbatch,PARENTQTY:pqty,KITTYPE:"KITDEKITWITHBOM",action: "VIEW_KITBOM_DETAILS",PLANT:"<%=plant%>"},dataType: "json", success: callback });

}

function onDelete()
{
	var emp = document.form.EMP_ID.value;
	if(emp=="" || emp.length==0 ) {
		alert("Enter Employee");
		document.getElementById("EMP_ID").focus();
		return false;
	}
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
	  	  document.form.action="/track/ProductionBomServlet?action=DEKITTING";
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
     errorHTML = "<table width= '100%' align = 'center' border='0' cellspacing='0' cellpadding='0' ><tr><td align='center'>"+errorMsg+"</td></tr></table>";
     document.getElementById('ERROR_MSG').innerHTML = errorHTML;
     var comMsg = data.completedMes;
     if(typeof(comMsg) == "undefined"){
    	 comMsg = "";
     }
     mesHTML = "<table width= '100%' align = 'center' border='0' cellspacing='0' cellpadding='0' ><tr><td align='center'>"+comMsg+"</td></tr></table>";
     document.getElementById('COMPLETED_MSG').innerHTML = mesHTML;
    
      	     
}

function getTable(){
        return '<TABLE class="table" id="tabledata" WIDTH="100%" align = "center">'+
               '<thead style="background:#eaeafa">'+
        	   '<tr >'+
        	   '<th width="3%">Select</th>'+
        		'<th width="2%">No</th>'+
        		'<th width="10%">Child Prod ID</th>'+
        		'<th width="11%">Child Prod Desc</th>'+
        		'<th width="11%">Child Prod Detail Desc</th>'+
        		'<th width="10%">Equivalent Prod ID</th>'+
        		'<th width="11%">Equivalent Prod Desc</th>'+
        		'<th width="11%">Equivalent Prod Detail Desc</th>'+
        		'<th width="4%">BOM Qty</th>'+
        		'<th width="4%">TRAN Qty</th>'+
        		'<th width="7%">Batch/Serial No</th>'+
        		'<th width="3%">Qty</th>'+
        		'<th width="3%">Status</th>'+
        		'</tr>'+
         		'</thead>';
       
}


document.getElementById('VIEW_RESULT_HERE').innerHTML =  getTable()+'</TABLE>';

</script>

<script>
$(document).ready(function(){
    $('[data-toggle="tooltip"]').tooltip();   
});
</script>


<jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
</jsp:include>