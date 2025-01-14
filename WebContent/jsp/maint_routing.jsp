<%@ include file="header.jsp"%>
<%@ page import="com.track.dao.TblControlDAO"%>
<%@ page import="com.track.constants.*"%>
<%@ page import="com.track.dao.MovHisDAO"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Maintain Routing Master</title>
<script language="JavaScript" type="text/javascript"
	src="js/jquery-1.4.2.js"></script>
<script language="JavaScript" type="text/javascript" src="js/json2.js"></script>
<script language="JavaScript" type="text/javascript" src="js/general.js"></script>
<link rel="stylesheet" href="css/style.css">

<SCRIPT LANGUAGE="JavaScript">

var subWin = null;
function popUpWin(URL) {
	
 		subWin = window.open(URL, 'Routing', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=700,height=400,left = 200,top = 184');
		
}
</SCRIPT>
</head>

<%
session = request.getSession();
String sUserId = (String) session.getAttribute("LOGIN_USER");
String sPlant = (String) session.getAttribute("PLANT");
String res        = "",action     =   "",pitem="",fieldDesc="";

session= request.getSession();
StrUtils strUtils = new StrUtils();
DateUtils dateutils = new DateUtils();

action            = strUtils.fString(request.getParameter("action"));
String plant = (String)session.getAttribute("PLANT");
pitem  = strUtils.fString(request.getParameter("ITEM"));


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
<FORM name="form1" method="post" action="/track/RoutingServlet?" >
  <br>
  <INPUT type="hidden" name="RFLAG" value="2">
  <table border="1" width="100%" cellspacing="0" cellpadding="0" align="center" bgcolor="#dddddd">
      <TR><TH BGCOLOR="#000066" COLSPAN="11"><font color="white">Maintain Routing Master</font>
  </table>
 
   <div id = "ERROR_MSG"></div>
  <br> 
   <table border="1" width="70%" cellspacing="0" cellpadding="0" align="center" bgcolor="#dddddd">
    <tr>
    <td>
  <table border="0" width="60%" cellspacing="0" cellpadding="0" align="center" >
   
    <TR>
    	<td></td>
        <th  ALIGN="left">Parent Product:</th>
								<td><INPUT type="TEXT" size="20" MAXLENGTH=50 name="ITEM" id="ITEM"
									value="<%=pitem%>"
									onkeypress="if((event.keyCode=='13') && ( document.form1.ITEM.value.length > 0)){validateProduct();}">
								<a href="#"
									onClick="javascript:popUpWin('ProductionBOMPitemList.jsp?ITEM='+form1.ITEM.value);">
								<img src="images/populate.gif" border="0" /> </a>
								<INPUT name="DESC" id="DESC" type="TEXT" value="" size="20" >
								<a href="#"
									onClick="javascript:popUpWin('ProductionBOMPitemList.jsp?ITEM='+form1.ITEM.value);">
								<img src="images/populate.gif" border="0" /> </a>
								<INPUT name="UOM" id="UOM" type="TEXT" value="" size="6" >
								<input type="button" value="View" name="Submit" onclick="onGo(1)"/>
								<INPUT type="hidden" name="OPR_SEQNUM" value="">
								</td>
			
						
    </TR>
    </TABLE>
    <br>
</td>
</tr>
</table>
</CENTER>
 <br> 
 <div id="RESULT_MESSAGE">
 <table border="0" cellspacing="0" cellpadding="0"  WIDTH="60%"  align = "center">
	<tr><td align="center"><%=fieldDesc%></td></tr>
	
</table></div>
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
	var productId = document.form1.ITEM.value;
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
							document.getElementById("UOM").value = resultVal.uom;

						} else {
							alert("Not a valid product!");
							document.form1.ITEM.value = "";
							document.form1.ITEM.focus();
						}
					}
				});
		}
	}

function onGo(index) {

	var index = index;
	var product = document.form1.ITEM.value;
	
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
    var urlStr = "/track/RoutingServlet";
    
    // Call the method of JQuery Ajax provided
    $.ajax({type: "POST",url: urlStr, data: {ITEM:product,action: "VIEW_ROUTING_DETAILS",PLANT:"<%=plant%>"},dataType: "json", success: callback });
  
}

function onDelete()
{
	debugger;
	var checkFound = false;
	var chkitems = document.form1.chkitem.value;
	 var len = document.form1.chkitem.length; 
	 if(len == undefined) len = 1;  
	 for (var i = 0; i < len ; i++)
   {
		if(len == 1 && (!document.form1.chkitem.checked))
		{
			checkFound = false;
		}
		
		else if(len ==1 && document.form1.chkitem.checked)
	     {
	    	 checkFound = true;
	    	 
	     }
	
	     else {
		     if(document.form1.chkitem[i].checked){
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
	  
	  document.form1.action="/track/RoutingServlet?action=DELETE_ROUTING";
	  document.form1.submit();
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
		
        $.each(data.routing, function(i,item){
                   
        	outPutdata = outPutdata+item.ROUTINGDATA;
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
    
}

function getTable(){
        return '<TABLE BORDER="0" id="tabledata" cellspacing="0" WIDTH="70%"  align = "center" bgcolor="navy">'+
        	   '<tr BGCOLOR="#000066">'+
         		'<th width="10%"><font color="#ffffff">Select</font></th>'+
         		'<th width="10%"><font color="#ffffff">Serial No</font></th>'+
         		'<th width="10%"><font color="#ffffff">Operation Seq</font></th>'+
         		'<th width="10%"><font color="#ffffff">Description</font></th>'+
         		//'<th width="10%"><font color="#ffffff">Location</font></th>'+
         		'<th width="10%"><font color="#ffffff">Remarks</font></th>'+
         		'</tr>';
       
}

document.getElementById('VIEW_RESULT_HERE').innerHTML =  getTable()+'</TABLE>';

	
</script>
<%@ include file="footer.jsp"%>