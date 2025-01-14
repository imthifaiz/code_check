<%@ include file="header.jsp"%>
<%@ page import="com.track.dao.TblControlDAO"%>
<%@ page import="com.track.constants.*"%>
<%@ page import="com.track.dao.MovHisDAO"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Routing Summary</title>
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
String res        = "",action     =   "",pitem="",operationseq="",loc="",fieldDesc="";

session= request.getSession();
StrUtils strUtils = new StrUtils();
DateUtils dateutils = new DateUtils();
LocTypeUtil loctypeutil = new LocTypeUtil();

action            = strUtils.fString(request.getParameter("action"));
String plant = (String)session.getAttribute("PLANT");
pitem  = strUtils.fString(request.getParameter("ITEM"));
operationseq  = strUtils.fString(request.getParameter("OPR_SEQNUM"));
loc  = strUtils.fString(request.getParameter("LOC_0"));

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
  
  <table border="1" width="100%" cellspacing="0" cellpadding="0" align="center" bgcolor="#dddddd">
      <TR><TH BGCOLOR="#000066" COLSPAN="11"><font color="white">Routing Summary</font>
  </table>
 
   <div id = "ERROR_MSG"></div>
  <br> 
   <table border="1" width="80%" cellspacing="0" cellpadding="0" align="center" bgcolor="#dddddd">
    <tr>
    <td>
  <table border="0" width="75%" cellspacing="0" cellpadding="0" align="center" >
   <br>
   <TR>
    	
         <th ALIGN="left">Parent Product:</th>
								<td><INPUT type="TEXT" size="20" MAXLENGTH=50 name="ITEM" id="ITEM"
									value="<%=pitem%>"
									onkeypress="if((event.keyCode=='13') && ( document.form1.ITEM.value.length > 0)){validateProduct();}">
								<a href="#"
									onClick="javascript:popUpWin('ProductionBOMPitemList.jsp?ITEM='+form1.ITEM.value);">
								<img src="images/populate.gif" border="0" /> </a>
								<INPUT name="DESC" id="DESC" type="hidden" value="" >
								<INPUT name="UOM" id="UOM" type="hidden" value="" >
								</td>
			
		 
         <th ALIGN="left">Operation SeqNum:</th>
								<td><INPUT type="TEXT" size="20" MAXLENGTH=50 name="OPR_SEQNUM" id="OPR_SEQNUM"
									value="<%=operationseq%>"
									onkeypress="if((event.keyCode=='13') && ( document.form1.OPR_SEQNUM.value.length > 0)){ValidateSeqnum();}">
								 <a href="#" onClick="javascript:popUpWin('OperationSequenceList.jsp?OPERATIONSEQ='+form1.OPR_SEQNUM.value+'&TYPE=routing');">
								 	<img src="images/populate.gif" border="0"></a> 
								<INPUT name="OPR_SEQ_DESC" id="OPR_SEQ_DESC" type="hidden" value="">
								<input type="hidden" name="REMARK" value="">
								<input type="button" value="View" name="Submit" onclick="onGo()"/>
								</td>
      <!--   <th ALIGN="left">Location:</th>
         <td ><INPUT name="LOC_0" id="LOC_0" type="TEXT" value="<%=loc%>" size="20"
					onkeypress="if((event.keyCode=='13') && ( this.value.length > 0)){validateLocation(0);}" MAXLENGTH=50> 
					<a href="#" onClick="javascript:popUpWin('loc_list_MultiReceivewms.jsp?LOC='+form1.LOC_0.value+'&INDEX=0&TYPE=routing');">
					<img src="images/populate.gif" border="0"></a>
			   		
								 </td>-->
							
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
	debugger;
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
							//document.form.action = "/track/OrderIssuingServlet?action=ViewProductOrders";
							//document.form.submit();

						} else {
							alert("Not a valid product!");
							document.form1.ITEM.value = "";
							document.form1.ITEM.focus();
						}
					}
				});
		}
	}
function ValidateSeqnum() {
	var oprseqnum = document.form1.OPR_SEQNUM.value;
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
							

						} else {
							alert("Not a valid operation sequence number!");
							document.form1.OPR_SEQNUM.value = "";
							document.form1.OPR_SEQNUM.focus();
						}
					}
				});
		}
	}
	
function validateLocation(index) {
	var locId = document.getElementById("LOC_0").value;
	if(locId=="" || locId.length==0 ) {
		alert("Enter Location!");
		document.getElementById("LOC_0").focus();
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
						document.getElementById("REMARK").value = "";
						document.getElementById("REMARK").focus();
					} else {
						alert("Not a valid Location");
						document.getElementById("LOC_0").value = "";
						document.getElementById("LOC_0").focus();
					}
				}
			});
		}
	}

function onGo() {

	
	var product = document.form1.ITEM.value;
	var seqnum = document.form1.OPR_SEQNUM.value;
	//var loc = document.getElementById("LOC_0").value;
	
	
    document.getElementById('VIEW_RESULT_HERE').innerHTML = '';
    document.getElementById('spinnerImg').innerHTML ='<br><br><p align=center><img src="images/spinner.gif"  > </p> ';
    var urlStr = "/track/RoutingServlet";
    
    // Call the method of JQuery Ajax provided
    $.ajax({type: "POST",url: urlStr, data: {ITEM:product,OPRSEQNUM:seqnum,action: "VIEW_ROUTING_SUMMARY",PLANT:"<%=plant%>"},dataType: "json", success: callback });
  
  
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
        return '<TABLE BORDER="0" id="tabledata" cellspacing="0" WIDTH="80%"  align = "center" bgcolor="navy">'+
        	   '<tr BGCOLOR="#000066">'+
        	   	'<th width="10%"><font color="#ffffff">Serial No</font></th>'+
         		'<th width="10%"><font color="#ffffff">Parent Product</font></th>'+
         		'<th width="10%"><font color="#ffffff">Description</font></th>'+
         		'<th width="10%"><font color="#ffffff">Operation Seqnum</font></th>'+
         		'<th width="10%"><font color="#ffffff">Description</font></th>'+
         		//'<th width="10%"><font color="#ffffff">Location</font></th>'+
         		'<th width="10%"><font color="#ffffff">Remarks</font></th>'+
         		'</tr>';
       
}


document.getElementById('VIEW_RESULT_HERE').innerHTML =  getTable()+'</TABLE>';

	
</script>
<%@ include file="footer.jsp"%>