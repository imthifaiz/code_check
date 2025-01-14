<%@page import="com.track.dao.ProductionBomDAO"%>
<%@ include file="header.jsp"%>
<%@ page import="com.track.dao.TblControlDAO"%>
<%@ page import="com.track.constants.*"%>
<%@ page import="com.track.dao.MovHisDAO"%>
<%
String title = "Kitting Dekitting with Bill Of Materials ";
%>
<%@include file="sessionCheck.jsp" %>
<jsp:include page="header2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
	<jsp:param name="mainmenu" value="<%=IConstants.IN_HOUSE%>"/>
    <jsp:param name="submenu" value="<%=IConstants.IN_HOUSE_SUB_MENU%>"/>
</jsp:include>

<script src="../jsp/js/typeahead.jquery.js"></script>
<link rel="stylesheet" href="../jsp/css/typeahead.css">
<link rel="stylesheet" href="../jsp/css/accounting.css">
<script language="JavaScript" type="text/javascript" src="../jsp/js/general.js"></script>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<script language="JavaScript" type="text/javascript" src="../jsp/js/json2.js"></script>
<script src="../jsp/js/Kitting_DekittingwithBOM.js"></script>
<SCRIPT LANGUAGE="JavaScript">

var subWin = null;
function popUpWin(URL) {
	
 		subWin = window.open(URL, 'ProductionBOM', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=800,height=400,left = 200,top = 184');
		
}
</SCRIPT>


<%
session = request.getSession();
String sUserId = (String) session.getAttribute("LOGIN_USER");
String res        = "",action     =   "",pitem="",pbatch="",citem="",cbatch="",qty="",EMP_ID="",EMP_NAME="",EMP_LNAME="",LOC_ID="",pqty="1",KONO="",DESC="",ISEDIT="",CHILDUOM="",
LOC_DESC="",LOC_TYPE_ID="",LOC_TYPE_DESC="",LOC_TYPE_ID2="",REMARKS="",REASONCODE="",fieldDesc="",allChecked="",PUOM="",SerParent="checked",ORDDATE="",RFLAG="2",errcls="error-msg";

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
CHILDUOM  = strUtils.fString(request.getParameter("CHILDUOM"));
PUOM  = strUtils.fString(request.getParameter("PUOM"));
LOC_TYPE_ID = strUtils.fString(request.getParameter("LOC_TYPE_ID"));
LOC_TYPE_ID2 = strUtils.fString(request.getParameter("LOC_TYPE_ID2"));
//fieldDesc=(String)session.getAttribute("MESSAGE");
if(action.equalsIgnoreCase(""))
	action = (String)(request.getAttribute("action"));
if(pitem.equalsIgnoreCase(""))
	pitem = (String)(request.getAttribute("ITEM"));
if(pbatch.equalsIgnoreCase(""))
	pbatch = (String)(request.getAttribute("BATCH_0"));
	KONO = (String)(request.getAttribute("KONO"));
	DESC = (String)(request.getAttribute("DESC"));
	ISEDIT = (String)(request.getAttribute("ISEDIT"));
if(pqty.equalsIgnoreCase(""))
{
	pqty="1";
}
if(pbatch.equalsIgnoreCase(""))
{
	pbatch="NOBATCH";
}
if(cbatch.equalsIgnoreCase(""))
{
	cbatch="NOBATCH";
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
if(action.equalsIgnoreCase("Kitting Added Successfully"))
{
  fieldDesc="Kitting Added Successfully";
  fieldDesc = "<font class='maingreen'>" + fieldDesc + "</font>";
  errcls="success-msg";
}
ProductionBomDAO _ProductionBomDAO = new ProductionBomDAO();
ArrayList  movQryList = new ArrayList();
if(!ISEDIT.equalsIgnoreCase("")) 
{
 movQryList = _ProductionBomDAO.getkittingDetails(pitem,"","",plant, " AND KONO='"+KONO+"'");

	Map lineArr = (Map) movQryList.get(0);
	PUOM = StrUtils.fString((String)lineArr.get("PARENTUOM"));
	pbatch = StrUtils.fString((String)lineArr.get("PARENT_PRODUCT_BATCH"));
	LOC_ID = StrUtils.fString((String)lineArr.get("PARENT_PRODUCT_LOC"));
	pqty = StrUtils.fString((String)lineArr.get("PARENT_PRODUCT_QTY"));
	if(pqty.equalsIgnoreCase("1.000"))
			SerParent="checked";
		else
			SerParent="";
	EMP_ID = StrUtils.fString((String)lineArr.get("EMPNO"));
	EMP_NAME = StrUtils.fString((String)lineArr.get("EMP_NAME"));
	LOC_TYPE_ID = StrUtils.fString((String)lineArr.get("LOC_TYPE_ID"));
	LOC_TYPE_ID2 = StrUtils.fString((String)lineArr.get("LOC_TYPE_ID2"));
	REASONCODE = StrUtils.fString((String)lineArr.get("RSNCODE"));
	REMARKS = StrUtils.fString((String)lineArr.get("REMARKS"));
	ORDDATE = StrUtils.fString((String)lineArr.get("ORDDATE"));
	RFLAG="3";
}

%>
<div class="container-fluid m-t-20">
	 <div class="box"> 
	 <!-- Thanzith Modified on 28.02.2022 -->
	 <ul class="breadcrumb backpageul">    
                <li class="underline-on-hover"><a href="../home">Dashboard</a></li>	
                 <li><a href="../kittingdekitting/summary"><span class="underline-on-hover">Kitting Dekitting with Bill Of Materials Summary</span></a></li>	
                <li><label>Kitting Dekitting with Bill Of Materials</label></li>                                   
            </ul>
        <!-- Thanzith Modified on 28.02.2022 --> 
            <div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title"><%=title %></h1>
              <h1 style="font-size: 18px; cursor: pointer;"
				class="box-title pull-right"
				onclick="window.location.href='../kittingdekitting/summary'">
				<i class="glyphicon glyphicon-remove"></i>
			</h1>
		</div>
		<center>
	<h2>
		<small class="<%=errcls%>"><%=fieldDesc%></small>
	</h2>
</center>
 <div class="container-fluid">

<form class="form-horizontal" name="form" method="post" action="">
<INPUT type="hidden" name="RFLAG" value="<%=RFLAG%>">
<INPUT type="hidden" name="plant" value=<%=plant%>>
<input type="hidden" name="EMP_ID" id="EMP_ID" value="<%=EMP_ID%>">
<input type="hidden" name="LOC_DESC" id="LOC_DESC" value="<%=LOC_DESC%>">
<input type="hidden" name="EMP_LNAME"  id="EMP_LNAME" value="<%=EMP_LNAME%>">
<input type="hidden" name="LOC_TYPE_DESC" id="LOC_TYPE_DESC" value="<%=LOC_TYPE_DESC%>">
<input type="hidden" name="ISEDIT" id="ISEDIT" value="<%=ISEDIT%>">
<input type="hidden" name="ORDDATE" id="ORDDATE" value="<%=ORDDATE%>">
<input type="hidden" name="EDIT_REMARKS" id="EDIT_REMARKS" value="<%=REMARKS%>">
<input type="hidden" name="DESC" id="DESC">
<input type="hidden" name="DETDESC" id="DETDESC">
<input type="hidden" name="CDETDESC" id="CDETDESC">
<input type="hidden" name="CDESC" id="CDESC">
<input type="hidden" name="ISAUTOGENERATE" value="false">
 <div id = "ERROR_MSG"></div>
   <div id = "COMPLETED_MSG"></div>
   
 
            <!-- <div class="box-header menu-drop">
              <h2><small>Employee/Location/Remarks/Reason Code</small></h2>
		</div> -->

<div class="form-group">
					<label class="control-label col-form-label col-sm-2 required">Kitting Dekitting Number:</label>
					<div class="col-sm-4">
						<div class="input-group">
							<input type="text" class="form-control" id="KONO" name="KONO"  value="<%=KONO%>" >
							<span class="input-group-addon" id="autoGen">
					   		 	<a href="#" data-toggle="tooltip" data-placement="top" title="Auto-Generate">
					   		 		<i class="glyphicon glyphicon-edit" style="font-size: 15px;"></i>
					   		 	</a>
				   		 	</span>
			   		 	</div>
					</div>
				</div>

<div class="form-group">
					<label class="control-label col-form-label col-sm-2 required">Employee:</label>
					<div class="col-sm-4">
						<input type="text" class="form-control" id="EMP_NAME" name="EMP_NAME" value="<%=EMP_NAME%>" onkeypress="if((event.keyCode=='13') && ( this.value.length > 0)){validateEmployee();}">
						<span class="select-icon"
							onclick="$(this).parent().find('input[name=\'EMP_NAME\']').focus()">
								<i class="glyphicon glyphicon-menu-down"></i>
						</span>
					</div>
				</div>
				
				<div class="form-group">
					<label class="control-label col-form-label col-sm-2 required">Location:</label>
					<div class="col-sm-4">
						<input type="text" class="form-control" id="LOC_ID" name="LOC_ID" value="<%=LOC_ID%>" onkeypress="if((event.keyCode=='13') && ( this.value.length > 0)){validateLocation();}">
						<span class="select-icon"
							onclick="$(this).parent().find('input[name=\'LOC_ID\']').focus()">
								<i class="glyphicon glyphicon-menu-down"></i>
						</span>
					</div>
				</div>
				
				<div class="form-group">
					<label class="control-label col-form-label col-sm-2">Location Type One:</label>
					<div class="col-sm-4">
						<input type="text" class="form-control" id="LOC_TYPE_ID" name="LOC_TYPE_ID" value="<%=LOC_TYPE_ID%>" readonly onkeypress="if((event.keyCode=='13') && ( this.value.length > 0)){validateLocationtype();}">
						<!-- <span class="select-icon"
							onclick="$(this).parent().find('input[name=\'LOC_TYPE_ID\']').focus()">
								<i class="glyphicon glyphicon-menu-down"></i>
						</span> -->
					</div>
				</div>
				
				<div class="form-group">
					<label class="control-label col-form-label col-sm-2">Location Type Two:</label>
					<div class="col-sm-4">
						<input type="text" class="form-control" id="LOC_TYPE_ID2" name="LOC_TYPE_ID2" value="<%=LOC_TYPE_ID2%>" readonly >
						
					</div>
				</div>
				
				<div class="form-group">
					<label class="control-label col-form-label col-sm-2">Reason Code:</label>
					<div class="col-sm-4">
						<input class="form-control" name="REASONCODE" id="REASONCODE" type="TEXT" value="<%=REASONCODE%>" size="20" MAXLENGTH=20 onkeypress="if((event.keyCode=='13') && ( this.value.length > 0)){validateRsncode();}">
						<span class="select-icon"
							onclick="$(this).parent().find('input[name=\'REASONCODE\']').focus()">
								<i class="glyphicon glyphicon-menu-down"></i>
						</span>
					</div>
				</div>
 
 <div class="form-group">
					<label class="control-label col-form-label col-sm-2">Remarks:</label>
					<div class="col-sm-6">
						<textarea rows="2" name="REMARKS" id="REMARKS" class="ember-text-area form-control ember-view"
								placeholder="Max 100 characters"  maxlength="100"></textarea>						
					</div>
				</div>
       
<!-- <div class="box-header menu-drop">
              <h2><small>Parent Product Details</small></h2>
		</div> -->


       
<div class="form-group">
     <div class="col-sm-2">
          <label class="checkbox-inline">      
            <input type = "checkbox" class="form-check-input" style="border:0;" name = "pserialqty"  id = "pserialqty" value="pserialqty" <%=SerParent%> onclick="ParentSerialqty();"><b>Serialized Parent</b></label>
            </div>
                 </div>
                 
<div class="form-group">
     <label class="control-label col-form-label col-sm-2 required">Parent Product</label>
    <div class="col-sm-4 ac-box">
				<div class="input-group">   
    		<input type="TEXT"  name="ITEM" id="ITEM" value="<%=pitem%>" onkeypress="if((event.keyCode=='13') && ( document.form.ITEM.value.length > 0)){validateProduct();}"
			size="20" MAXLENGTH=100  class="ac-selected  form-control typeahead">
			<span class="select-icon" style="right:10px;" onclick="$(this).parent().find('input[name=\'ITEM\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>   		 		
  		</div>
  		</div>
  		<div class="form-inline">
<div class="col-sm-1">
    <button type="button" class="Submit btn btn-default" value="View" name="Submit" onclick="onGo(1)">View</button>
       </div>
          </div>
  		</div>                 

<div class="form-group">
					<label class="control-label col-form-label col-sm-2 required">Batch/Serial Number:</label>
					<div class="col-sm-4">
					<div class="input-group">
						<input class="ac-selected  form-control typeahead" name="BATCH_0" id="BATCH_0" type="TEXT" value="<%=pbatch%>" size="20" MAXLENGTH=40>
   		 	<span class="input-group-addon"  onClick="javascript:generateBatch(0);return false;"  id="autoGenBatch">
   		 	<a href="#" data-toggle="tooltip" data-placement="top" title="Generate Batch">
   		 	<i class="glyphicon glyphicon-edit" style="font-size: 20px;"></i></a></span>
					</div>
					</div>
				</div>

<div class="form-group">
      <label class="control-label col-form-label col-sm-2 required">Parent Quantity</label>
     <div class="col-sm-2">
				<div class="input-group">     
          <INPUT  class="form-control" name="PARENTQTY" type="TEXT" id="PARENTQTY" value="<%=pqty%>" size="4" MAXLENGTH=50 onchange="pqtychange();" readonly>
			</div>
            </div>
            <div class="col-sm-2">
    	<input type="text" name="PARENTUOM" id="PARENTUOM" class="form-control" placeholder="Parent UOM" readonly value="<%=PUOM%>">
    	<span class="select-icon" style="right:25px;" onclick="$(this).parent().find('input[name=\'PARENTUOM\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
	 	</div>
            
            </div>


<!-- <div class="box-header menu-drop">
              <h2><small>Child Product Details</small></h2>
		</div> -->
   
 
 
<div class="form-group">
     <div class="col-sm-2">
         <!-- <label class="checkbox-inline">      
             <input type = "checkbox" class="form-check-input"  style="border:0;" name = "serialqty"  id = "serialqty" value="serialqty" onclick="Serialqty();"><b>Serialized Child</b></label> -->
             <label class="checkbox-inline"><b>Child Product</b></label>
            </div>
                 </div>
                     
<div class="form-group">
      <label class="control-label col-form-label col-sm-2 required">Child Product</label>
      <div class="col-sm-4 ac-box">
				<div class="input-group">  
    		<input type="TEXT"  name="CITEM" id="CITEM" value="<%=citem%>" onkeypress="if((event.keyCode=='13') && ( document.form.CITEM.value.length > 0)){ValidateChildProduct();}"
			size="20" MAXLENGTH=100 class="form-control">
			<span class="select-icon" style="right:10px;" onclick="$(this).parent().find('input[name=\'CITEM\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
   		</div>
  		</div>
  	</div>
  	
  	<div class="form-group">
					<label class="control-label col-form-label col-sm-2 required">Batch/Serial Number:</label>
					<div class="col-sm-4">
					<div class="input-group">
						<input class="form-control" name="BATCH_1" id="BATCH_1" type="TEXT" value="<%=cbatch%>" size="20" MAXLENGTH=40
					onkeypress="if((event.keyCode=='13') && ( document.form.BATCH_1.value.length > 0)){validateBatch(1);}">   		 	
					</div>
					</div>
				</div>
  		
  		  <div class="form-group">
      <label class="control-label col-form-label col-sm-2 required">Child Quantity</label>
      <div class="col-sm-2">				        
       <INPUT  class="form-control"  name="QTY" type="TEXT" id="QTY" value="<%=qty%>" size="4" MAXLENGTH=50
						onkeypress="if((event.keyCode=='13') && ( document.form.QTY.value.length>0)){validateQuantity();}" >      
  		
    </div>
    <div class="col-sm-2">
    	<input type="text" name="CHILDUOM" id="CHILDUOM" class="form-control" placeholder="Child UOM" value="<%=CHILDUOM%>" readonly>
    	<span class="select-icon" style="right:25px;" onclick="$(this).parent().find('input[name=\'CHILDUOM\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
	 	</div>
  </div>
      
  
  <div class="form-group">        
     <div class="col-sm-12" align="center">
     <!--<button type="button" class="Submit btn btn-default" onClick="window.location.href='../home'"><b>Back</b></button>&nbsp;&nbsp;
      <button type="button" class="Submit btn btn-default" onClick="window.location.href='inPremises.jsp'"><b>Back</b></button>&nbsp;&nbsp; -->
      <button type="button" class="btn btn-success" value="Kit" onClick="onAdd()">Kit</button>&nbsp;&nbsp;
      	<button type="button" class="Submit btn btn-default" value="Clear" id="Clear" onClick="onClear();">Clear</button>&nbsp;&nbsp;
      	
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
      <button type="button" class="Submit btn btn-default" value="De-Kit" id="DeKit" onClick="onDelete()">De-Kit</button>&nbsp;&nbsp;
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
	document.getElementById("LOC_TYPE_ID2").value = ""
	document.getElementById("LOC_TYPE_DESC").value = ""
	document.getElementById("REMARKS").value = ""
	document.getElementById("REASONCODE").value = ""
	document.getElementById("ITEM").value = ""
	document.getElementById("DESC").value = ""
	document.getElementById("DETDESC").value = ""
	document.getElementById("BATCH_0").value = "NOBATCH"
	document.getElementById("CITEM").value = ""
	document.getElementById("CDESC").value = ""
	document.getElementById("CDETDESC").value = ""
	document.getElementById("BATCH_1").value = "NOBATCH"
	document.getElementById("QTY").value = ""
	document.getElementById("CHILDUOM").value = ""
	document.getElementById("PARENTUOM").value = ""
	document.getElementById("ORDDATE").value = ""
	document.getElementById("EDIT_REMARKS").value = ""
	document.getElementById("ISEDIT").value = ""
	document.getElementById('VIEW_RESULT_HERE').innerHTML =  getTable()+'</TABLE>';
	autoGen();
	
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
	var puom = document.form.PARENTUOM.value;
	var cuom = document.form.CHILDUOM.value;
	var kono = document.form.KONO.value;
	var ORDDATE = document.form.ORDDATE.value;
	
	if(kono=="" || kono.length==0 ) {
		alert("Enter Kitting Dekitting Number");
		document.getElementById("KONO").focus();
		return false;
	}
	if(emp=="" || emp.length==0 ) {
		alert("Enter Employee");
		document.getElementById("EMP_NAME").focus();
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
	if(puom=="" || puom.length==0 ) {
		alert("Enter Parent UOM");
		document.getElementById("PARENTUOM").focus();
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
	if(cuom=="" || cuom.length==0 ) {
		alert("Enter Child UOM");
		document.getElementById("CHILDUOM").focus();
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
	
	var tabled=document.getElementById("tabledata");
	var len = tabled.rows.length;
	var scanitem = trim((document.getElementById("CITEM").value).toUpperCase());
	var scanqty = parseInt(document.getElementById("QTY").value);
	for ( var i = 1; i < len; i++) {
		
		var item = (document.getElementById("citem_" + i).innerText).toUpperCase();
		//var eitem = (document.getElementById("eitem_" + i).innerText).toUpperCase();
		var item = trim(item);
		//var eitem = trim(eitem);
		var eitem = "";
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
    //document.getElementById('spinnerImg').innerHTML ='<br><br><p align=center><img src="images/spinner.gif"  > </p> ';
    var urlStr = "/track/ProductionBomServlet";
    
    // Call the method of JQuery Ajax provided
    $.ajax({type: "POST",url: urlStr, data: {EMP_ID:emp,LOC_ID:loc,LOC_TYPE_ID:loctype,REMARKS:remarks,REASONCODE:rsncode,ITEM:pitem,BATCH_0:pbatch,CITEM:citem,BATCH_1:cbatch,QTY:qty,PARENTQTY:pqty,PUOM:puom,CUOM:cuom,KONO:kono,ORDDATE:ORDDATE,KITTYPE:"KITDEKITWITHBOM",action: "ADD_KITTING_WITHBOM",PLANT:"<%=plant%>"},dataType: "json", success: savecallback });
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
	var kono = document.form.KONO.value;
	
	if(index == '1'){
	if(kono=="" || kono.length==0 ) {
			alert("Enter Order Number");
			document.getElementById("KONO").focus();
			return false;
	}
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
    //document.getElementById('spinnerImg').innerHTML ='<br><br><p align=center><img src="images/spinner.gif"  > </p> ';
    var urlStr = "/track/ProductionBomServlet";
    
    // Call the method of JQuery Ajax provided
    $.ajax({type: "POST",url: urlStr, data: {ITEM:product,BATCH_0:pbatch,PARENTQTY:pqty,KONO:kono,KITTYPE:"KITDEKITWITHBOM",action: "VIEW_KITBOM_DETAILS_FOR_KITTING",PLANT:"<%=plant%>"},dataType: "json", success: callback });

}

function onDelete()
{
	var emp = document.form.EMP_ID.value;
	var kono = document.form.KONO.value;
	if(kono=="" || kono.length==0 ) {
		alert("Enter Order Number");
		document.getElementById("KONO").focus();
		return false;
}
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
function savecallback(data){
	
	var outPutdata = getTable();
	var ii = 0;
	var errorBoo = false;
	$.each(data.errors, function(i,error){
		if(error.ERROR_CODE=="99"){
			errorBoo = true;
			
		}
	});
	
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
    var KONO = document.form.KONO.value;
    var parentitem = document.form.ITEM.value;
    if(errorMsg.indexOf('Kitting Added Successfully') >= 0){
    	document.form.action  = "../kittingdekitting/edit?KONO="+KONO+"&ITEM="+parentitem+"&action=Kitting Added Successfully";
        document.form.submit();
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
        return '<TABLE class="table table-bordred table-striped" id="tabledata" WIDTH="100%" align = "center">'+
               '<thead>'+
        	   '<tr >'+
        	   '<th width="3%">Select</th>'+
        		'<th width="2%">No</th>'+
        		'<th width="10%">Child Product</th>'+
        		'<th width="11%">Child Product Desc</th>'+
        		'<th width="11%">Child Product Detail Desc</th>'+
        		'<th width="10%">Child UOM</th>'+
        		/* '<th width="10%">Equivalent Product</th>'+
        		'<th width="11%">Equivalent Product Desc</th>'+
        		'<th width="11%">Equivalent Product Detail Desc</th>'+ */
        		'<th width="4%">BOM Qty</th>'+
        		'<th width="4%">TRAN Qty</th>'+
        		'<th width="10%">Child Product Price</th>'+
        		'<th width="10%">BOM Qty Price</th>'+
        		'<th width="10%">TRAN Qty Price</th>'+
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