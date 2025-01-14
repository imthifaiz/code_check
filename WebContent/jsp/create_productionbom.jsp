<%@ include file="header.jsp"%>
<%@ page import="com.track.dao.TblControlDAO"%>
<%@ page import="com.track.constants.*"%>
<%@ page import="com.track.dao.MovHisDAO"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Create Production BOM Master</title>
<script language="JavaScript" type="text/javascript"
	src="js/jquery-1.4.2.js"></script>
<script language="JavaScript" type="text/javascript" src="js/json2.js"></script>
<script language="JavaScript" type="text/javascript" src="js/general.js"></script>
<link rel="stylesheet" href="css/style.css">

<SCRIPT LANGUAGE="JavaScript">

var subWin = null;
function popUpWin(URL) {
	
 		subWin = window.open(URL, 'ProductionBOM', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=700,height=400,left = 200,top = 184');
		
}
</SCRIPT>
</head>

<%
session = request.getSession();
String sUserId = (String) session.getAttribute("LOGIN_USER");
String sPlant = (String) session.getAttribute("PLANT");
String res        = "",action     =   "",pitem="",citem="",qty="",operationseq="",remark1="",remark2="",fieldDesc="";

session= request.getSession();
StrUtils strUtils = new StrUtils();
DateUtils dateutils = new DateUtils();
LocTypeUtil loctypeutil = new LocTypeUtil();

action            = strUtils.fString(request.getParameter("action"));
String plant = (String)session.getAttribute("PLANT");
pitem  = strUtils.fString(request.getParameter("ITEM"));
citem  = strUtils.fString(request.getParameter("CITEM"));
qty  = strUtils.fString(request.getParameter("QTY"));
operationseq  = strUtils.fString(request.getParameter("OPR_SEQNUM"));
remark1  = strUtils.fString(request.getParameter("REMARK1"));
remark2  = strUtils.fString(request.getParameter("REMARK2"));


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
  
  
}
%>
<%@ include file="body.jsp"%>
<FORM name="form" method="post" action="/track/ProductionBomServlet?" >
  <br>
   <INPUT type="hidden" name="RFLAG" value="1">
  <table border="1" width="100%" cellspacing="0" cellpadding="0" align="center" bgcolor="#dddddd">
      <TR><TH BGCOLOR="#000066" COLSPAN="11"><font color="white">Create Production BOM Master</font>
  </table>
 
   <div id = "ERROR_MSG"></div>
  <br> 
   <table border="1" width="70%" cellspacing="0" cellpadding="0" align="center" bgcolor="#dddddd">
    <tr>
    <td>
  <table border="0" width="60%" cellspacing="0" cellpadding="0" align="center" >
    <TR>
    	<td></td>
         <th WIDTH="25%" ALIGN="left">Parent Product:</th>
								<td><INPUT type="TEXT" size="20" MAXLENGTH=50 name="ITEM" id="ITEM"
									value="<%=pitem%>"
									onkeypress="if((event.keyCode=='13') && ( document.form.ITEM.value.length > 0)){validateProduct();}">
								<a href="#"
									onClick="javascript:popUpWin('list/item_list_po.jsp?ITEM='+form.ITEM.value+'&TYPE=PARENTITEM');">
								<img src="images/populate.gif" border="0" /> </a>
								<INPUT name="DESC" id="DESC" type="TEXT" value="" size="20" >
								<INPUT name="UOM" id="UOM" type="TEXT" value="" size="6" >
								<input type="button" value="View" name="Submit" onclick="onGo(1)"/>
								</td>
			
							
    </TR>
    <TR>
  		 <td></td>
         <th WIDTH="25%" ALIGN="left">Parent Qty:</th>
				<td><INPUT name="PARENTQTY" type="TEXT" id="PARENTQTY" value="1" size="4" MAXLENGTH=50 readonly>
						
			 
			
		</td>				
    </TR>
    
    <TR>
   		 <td></td>
         <th WIDTH="25%" ALIGN="left">Child Product:</th>
								<td><INPUT type="TEXT" size="20" MAXLENGTH=50 name="CITEM" id="CITEM"
									value="<%=citem%>"
									onkeypress="if((event.keyCode=='13') && ( document.form.CITEM.value.length > 0)){ValidateChildProduct();}">
								<a href="#"
									onClick="javascript:popUpWin('list/item_list_po.jsp?ITEM='+form.CITEM.value+'&TYPE=CHILDITEM');">
								<img src="images/populate.gif" border="0" /> </a>
								<INPUT name="CDESC" id="CDESC" type="TEXT" value="" size="20" >
								<INPUT name="CUOM" id="CUOM" type="TEXT" value="" size="6" >
								</td>
							
    </TR>
   <TR>
  		 <td></td>
         <th WIDTH="25%" ALIGN="left">Qty Per:</th>
				<td><INPUT name="QTY" type="TEXT" id="QTY" value="<%=qty%>" size="4" MAXLENGTH=50
						onkeypress="if((event.keyCode=='13') && ( document.form.QTY.value.length > 0)){validateQuantity();}">
			 
			
		</td>				
    </TR>
     <TR>
   		 <td></td>
         <th  ALIGN="left">Operation SeqNum:</th>
								<td><INPUT type="TEXT" size="20" MAXLENGTH=50 name="OPR_SEQNUM" id="OPR_SEQNUM"
									value="<%=operationseq%>"
									onkeypress="if((event.keyCode=='13') && ( document.form.OPR_SEQNUM.value.length > 0)){ValidateSeqnum();}">
								 <a href="#" onClick="javascript:popUpWin('OperationSequenceList.jsp?OPERATIONSEQ='+form.OPR_SEQNUM.value+'&TYPE=PBOM');">
								 	<img src="images/populate.gif" border="0"></a> 
								<INPUT name="OPR_SEQ_DESC" id="OPR_SEQ_DESC" type="TEXT" value="" size="20" >
								</td>
							
    </TR>
    <TR>
    	<td></td>
         <th WIDTH="25%" ALIGN="left">Remark1:</th>
				<td><INPUT name="REMARK1" type="TEXT" id="REMARK1" value="<%=remark1%>" size="20" MAXLENGTH=50
						onkeypress="if((event.keyCode=='13') && ( document.form.REMARK1.value.length > 0)){focusremark2();}">
			 
			
		</td>				
    </TR>
    
    <TR>
    	<td></td>
         <th WIDTH="25%" ALIGN="left">Remark2:</th>
				<td><INPUT name="REMARK2" type="TEXT" id="REMARK2" value="<%=remark2%>" size="20" MAXLENGTH=50
						onkeypress="if((event.keyCode=='13') && ( document.form.REMARK2.value.length > 0)){onAdd();}">
			 
			
		</td>				
    </TR>
    <br>
    <tr>
    	<td></td>
     	<th WIDTH="25%" ALIGN="center"></th>
		<td align="center">
		<input type="button" value="ADD" onClick="onAdd()"/>
		</td>
	</tr>
    
</TABLE>
</td>
</tr>
</table>
</CENTER>
 <br> 
 <div id="RESULT_MESSAGE">
 <table border="0" cellspacing="0" cellpadding="0"  WIDTH="60%"  align = "center">
	<tr><td align="center"><%=fieldDesc%></td></tr>
	
</table>
</div>
<br>
  <div id="VIEW_RESULT_HERE"></div>
  <div id="spinnerImg" ></div>
  <div align="center">
				<center><br>
				<table border="0" width="30%" cellspacing="0" cellpadding="0">
				<tr>
					<td width="15%" align="center">
						<input type="button" value="DELETE" onClick="onDelete()"/>
					</td>
					</tr>
				</table>
				</center>
				</div>
	

</FORM>
<script>
onGo(0);
function validateProduct() {
	var productId = document.form.ITEM.value;
	if(productId=="" || productId.length==0 ) {
		alert("Enter Product");
		document.getElementById("ITEM").focus();
	}else{
	var urlStr = "/track/MiscOrderHandlingServlet";
	$.ajax( {
		type : "POST",
		url : urlStr,
		data : {
		ITEM : productId,
		PLANT : "<%=plant%>",
		ACTION : "VALIDATE_PRODUCT"
					},
					dataType : "json",
					success : function(data) {
						if (data.status == "100") {
							var resultVal = data.result;
							document.getElementById("UOM").value = resultVal.uom;
							document.getElementById("DESC").value = resultVal.discription;
							document.form.CITEM.focus();

						} else {
							alert("Not a valid product!");
							document.form.ITEM.value = "";
							document.form.ITEM.focus();
						}
					}
				});
		}
	}
function ValidateChildProduct() {
	debugger;
	var childproduct = document.form.CITEM.value;
	if(childproduct=="" || childproduct.length==0 ) {
		alert("Enter Child Product");
		document.getElementById("CITEM").focus();
	}else{
	var urlStr = "/track/MiscOrderHandlingServlet";
	$.ajax( {
		type : "POST",
		url : urlStr,
		data : {
		ITEM : childproduct,
		PLANT : "<%=plant%>",
		ACTION : "VALIDATE_PRODUCT"
					},
					dataType : "json",
					success : function(data) {
						if (data.status == "100") {
							var resultVal = data.result;
							document.getElementById("CUOM").value = resultVal.uom;
							document.getElementById("CDESC").value = resultVal.discription;
							document.form.QTY.focus();

						} else {
							alert("Not a valid child product!");
							document.form.CITEM.value = "";
							document.form.CITEM.focus();
						}
					}
				});
		}
	}
function validateQuantity() {
	debugger;
	var qty = document.getElementById("QTY").value;
	if (qty == "" || qty.length == 0) {
		alert("Enter Quantity!");
		document.getElementById("QTY").focus();
	} else {
		if (isNumericInput(qty) == false) {
			alert("Entered Quantity is not a valid Qty!");
		} 

	}
	document.form.OPR_SEQNUM.focus();
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
function focusremark2(){
	document.form.REMARK2.focus();
}

function ValidateSeqnum() {
	var oprseqnum = document.form.OPR_SEQNUM.value;
	if(oprseqnum=="" || oprseqnum.length==0 ) {
		alert("Enter Operation Sequence Number");
		document.getElementById("OPR_SEQNUM").focus();
	}else{
	var urlStr = "/track/RoutingServlet";
	$.ajax( {
		type : "POST",
		url : urlStr,
		data : {
		OPRSEQNUM : oprseqnum,
		PLANT : "<%=plant%>",
		action : "VALIDATE_OPRSEQNUM"
					},
					dataType : "json",
					success : function(data) {
						if (data.status == "100") {
							var resultVal = data.result;
							document.getElementById("OPR_SEQ_DESC").value = resultVal.discription;
							document.form.REMARK1.focus();

						} else {
							alert("Not a valid operation sequence number!");
							document.form.OPR_SEQNUM.value = "";
							document.form.OPR_SEQNUM.focus();
						}
					}
				});
		}
	}


function onAdd() {
debugger;
	var product = document.form.ITEM.value;
	var childproduct = document.form.CITEM.value;
	var qty = document.form.QTY.value;
	var oprseqnum = document.form.OPR_SEQNUM.value;
	var remark1 = document.form.REMARK1.value;
	var remark2 = document.form.REMARK2.value;
	
	if(product=="" || product.length==0 ) {
		alert("Enter Parent Product");
		document.getElementById("ITEM").focus();
		return false;
	}
	
	if(childproduct=="" || childproduct.length==0 ) {
		alert("Enter Child Product");
		document.getElementById("CITEM").focus();
		return false;
	}
	if(qty=="" || qty.length==0 ) {
		alert("Enter Quantity Per");
		document.getElementById("QTY").focus();
		return false;
	}
	
	if(oprseqnum=="" || oprseqnum.length==0 ) {
		alert("Enter Operation Sequence Number");
		document.getElementById("OPR_SEQNUM").focus();
		return false;
	}
		
	if(product==childproduct ) {
		alert("Child Product cannot be same as Parent Product.Choose diff child product.");
		document.form.CITEM.value = "";
		document.getElementById("CITEM").focus();
		return false;
	}
	
	
	document.getElementById('RESULT_MESSAGE').innerHTML = '';   
    document.getElementById('VIEW_RESULT_HERE').innerHTML = '';
    document.getElementById('spinnerImg').innerHTML ='<br><br><p align=center><img src="images/spinner.gif"  > </p> ';
    var urlStr = "/track/ProductionBomServlet";
    
    // Call the method of JQuery Ajax provided
    $.ajax({type: "POST",url: urlStr, data: {ITEM:product,CITEM:childproduct,QTY:qty,OPRSEQNUM:oprseqnum,REMARK1:remark1,REMARK2:remark2,action: "ADD_PRODBOM",PLANT:"<%=plant%>"},dataType: "json", success: callback });
    document.form.CITEM.value = "";
    document.form.CDESC.value = "";
    document.form.CUOM.value = "";
    document.form.QTY.value = "";
    document.form.OPR_SEQNUM.value = "";
    document.form.OPR_SEQ_DESC.value = "";
    document.form.REMARK1.value = "";
    document.form.REMARK2.value = "";
    
    
}

function onGo(index) {

	var index = index;
	var product = document.form.ITEM.value;
	//var childproduct = document.form.CITEM.value;
	
	if(index == '1'){
	if(product=="" || product.length==0 ) {
		alert("Enter Product");
		document.getElementById("ITEM").focus();
		return false;
		}
     
	}
	document.getElementById('RESULT_MESSAGE').innerHTML = ''; 
    document.getElementById('VIEW_RESULT_HERE').innerHTML = '';
    document.getElementById('spinnerImg').innerHTML ='<br><br><p align=center><img src="images/spinner.gif"  > </p> ';
    var urlStr = "/track/ProductionBomServlet";
    
    // Call the method of JQuery Ajax provided
    $.ajax({type: "POST",url: urlStr, data: {ITEM:product,action: "VIEW_PRODBOM_DETAILS",PLANT:"<%=plant%>"},dataType: "json", success: callback });
  
}

function onDelete()
{
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
	  	  document.form.action="/track/ProductionBomServlet?action=DELETE_PRODBOM";
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
		
        $.each(data.productionbom, function(i,item){
                   
        	outPutdata = outPutdata+item.PRODBOMDATA;
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
     document.form.TRANSACTIONNO.select();
	 document.form.TRANSACTIONNO.focus();
      	     
}

function getTable(){
        return '<TABLE BORDER="0" id="tabledata" cellspacing="0" WIDTH="70%"  align = "center" bgcolor="navy">'+
        	   '<tr BGCOLOR="#000066">'+
         		'<th width="10%"><font color="#ffffff">Select</font></th>'+
         		'<th width="10%"><font color="#ffffff">Serial No</font></th>'+
         		'<th width="10%"><font color="#ffffff">Child Product</font></th>'+
         		'<th width="10%"><font color="#ffffff">Description</font></th>'+
         		'<th width="10%"><font color="#ffffff">Qty Per</font></th>'+
         		'<th width="10%"><font color="#ffffff">Operation Seq</font></th>'+
         		'<th width="10%"><font color="#ffffff">UOM</font></th>'+
         		'</tr>';
       
}


document.getElementById('VIEW_RESULT_HERE').innerHTML =  getTable()+'</TABLE>';

	
</script>
<%@ include file="footer.jsp"%>