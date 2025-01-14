<%@ include file="header.jsp"%>
<%@ page import="com.track.dao.TblControlDAO"%>
<%@ page import="com.track.constants.*"%>
<%@ page import="com.track.dao.MovHisDAO"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Production BOM Summary</title>
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
String res        = "",action     =   "",pitem="",citem="",fieldDesc="";

session= request.getSession();
StrUtils strUtils = new StrUtils();
DateUtils dateutils = new DateUtils();
LocTypeUtil loctypeutil = new LocTypeUtil();

action            = strUtils.fString(request.getParameter("action"));
String plant = (String)session.getAttribute("PLANT");
pitem  = strUtils.fString(request.getParameter("ITEM"));
citem  = strUtils.fString(request.getParameter("CITEM"));


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
  
  <table border="1" width="100%" cellspacing="0" cellpadding="0" align="center" bgcolor="#dddddd">
      <TR><TH BGCOLOR="#000066" COLSPAN="11"><font color="white">Production BOM Summary</font>
  </table>
 
   <div id = "ERROR_MSG"></div>
  <br> 
   <table border="1" width="70%" cellspacing="0" cellpadding="0" align="center" bgcolor="#dddddd">
    <tr>
    <td>
  <table border="0" width="60%" cellspacing="0" cellpadding="0" align="center" >
   <br>
    <TR>
    	  <th  ALIGN="left">Parent Product:</th>
								<td ><INPUT type="TEXT" size="20" MAXLENGTH=50 name="ITEM" id="ITEM"
									value="<%=pitem%>"
									onkeypress="if((event.keyCode=='13') && ( document.form.ITEM.value.length > 0)){validateProduct();}">
								<a href="#"
									onClick="javascript:popUpWin('ProductionBOMPitemList.jsp?ITEM='+form.ITEM.value+'&TYPE=PRODBOM');">
								<img src="images/populate.gif" border="0" /> </a>
								<INPUT name="DESC" id="DESC" type="hidden" value="" >
								<INPUT name="UOM" id="UOM" type="hidden" value="" >
								</td>
			
		    <th  ALIGN="left">Child Product:</th>
								<td ><INPUT type="TEXT" size="20" MAXLENGTH=50 name="CITEM" id="CITEM"
									value="<%=citem%>"
									onkeypress="if((event.keyCode=='13') && ( document.form.CITEM.value.length > 0)){ValidateChildProduct();}">
								<a href="#"
									onClick="javascript:popUpWin('ProductionBOMchilditemList.jsp?PITEM='+form.ITEM.value+'&CITEM='+form.CITEM.value+'&TYPE=BOMCHILDITEM');">
								<img src="images/populate.gif" border="0" /> </a>
								<input type="button" value="View" name="Submit" onclick="onGo()"/>
								</td>
							
    </TR>
     
</TABLE>
<br>
</td>
</tr>
</table>
</CENTER>
 <br> 
 <table border="0" cellspacing="0" cellpadding="0"  WIDTH="60%"  align = "center">
	<tr><td align="center"><%=fieldDesc%></td></tr>
	
</table>
<br>
  <div id="VIEW_RESULT_HERE"></div>
  <div id="spinnerImg" ></div>
  <div align="center">
				<center><br>
				<table border="0" width="30%" cellspacing="0" cellpadding="0">
				<tr>
					<td width="15%" align="center">
						<input type="button" value="Back" onClick="window.location.href='../home'">
					</td>
					</tr>
				</table>
				</center>
				</div>
	

</FORM>
<script>
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
							//document.getElementById("DESC").value = resultVal.discription;
							//document.getElementById("UOM").value = resultVal.uom;
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
		CITEM : childproduct,
		PLANT : "<%=plant%>",
		action : "VALIDATE_CHILD_PRODUCT"
					},
					dataType : "json",
					success : function(data) {
						if (data.status == "100") {
							var resultVal = data.result;
							//document.getElementById("CUOM").value = resultVal.uom;
							//document.form.action = "/track/OrderIssuingServlet?action=ViewProductOrders";
							//document.form.submit();

						} else {
							alert("Not a valid child product!");
							document.form.CITEM.value = "";
							document.form.CITEM.focus();
						}
					}
				});
		}
	}

function onGo() {

	
	var product = document.form.ITEM.value;
	var childproduct = document.form.CITEM.value;
	
	
	/*if(product=="" || product.length==0 ) {
		alert("Enter Product");
		document.getElementById("ITEM").focus();
		return false;
		}*/
     
	
    document.getElementById('VIEW_RESULT_HERE').innerHTML = '';
    document.getElementById('spinnerImg').innerHTML ='<br><br><p align=center><img src="images/spinner.gif"  > </p> ';
    var urlStr = "/track/ProductionBomServlet";
    
    // Call the method of JQuery Ajax provided
    $.ajax({type: "POST",url: urlStr, data: {ITEM:product,CITEM:childproduct,action: "VIEW_PRODBOM_SUMMARY",PLANT:"<%=plant%>"},dataType: "json", success: callback });
  
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
        	   	'<th width="10%"><font color="#ffffff">Serial No</font></th>'+
         		'<th width="10%"><font color="#ffffff">Parent Product</font></th>'+
         		'<th width="10%"><font color="#ffffff">Description</font></th>'+
         		'<th width="10%"><font color="#ffffff">UOM</font></th>'+
         		'<th width="10%"><font color="#ffffff">Child Product</font></th>'+
         		'<th width="10%"><font color="#ffffff">Description</font></th>'+
         		'<th width="10%"><font color="#ffffff">UOM</font></th>'+
         		'<th width="10%"><font color="#ffffff">Qty Per</font></th>'+
         		'<th width="10%"><font color="#ffffff">Operation Seq</font></th>'+
         		'<th width="10%"><font color="#ffffff">Remark1</font></th>'+
         		'<th width="10%"><font color="#ffffff">Remark2</font></th>'+
         		'</tr>';
       
}


document.getElementById('VIEW_RESULT_HERE').innerHTML =  getTable()+'</TABLE>';

	
</script>
<%@ include file="footer.jsp"%>