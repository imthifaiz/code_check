<%@page import="com.track.dao.PlantMstDAO"%>
<%@ page import="com.track.constants.*"%>
<%@ include file="header.jsp"%>
<%
	DateUtils _dateUtils = new DateUtils();
	String title = "Sales Order Return";
	String plant = StrUtils.fString((String) session.getAttribute("PLANT"));
	String curency = StrUtils.fString((String) session.getAttribute("BASE_CURRENCY"));
	String username = StrUtils.fString((String) session.getAttribute("LOGIN_USER"));
	String fieldDesc = StrUtils.fString(request.getParameter("result"));
	String orderNo= StrUtils.fString(request.getParameter("ORDERNO"));
	String invoiceNo= StrUtils.fString(request.getParameter("INVOICENO"));
	String action= StrUtils.fString(request.getParameter("ACTION"));
	String curDate = _dateUtils.getDate();
	
	PlantMstDAO _PlantMstDAO = new PlantMstDAO();
	String numberOfDecimal = _PlantMstDAO.getNumberOfDecimal(plant);
%>
<%@include file="sessionCheck.jsp"%>
<jsp:include page="header2.jsp" flush="true">
	<jsp:param name="title" value="<%=title%>" />
	<jsp:param name="mainmenu" value="<%=IConstants.SALES%>"/>
	<jsp:param name="submenu" value="<%=IConstants.SALES_RETURN%>"/>
</jsp:include>
<script src="../jsp/js/typeahead.jquery.js"></script>
<link rel="stylesheet" href="../jsp/css/typeahead.css">
<link rel="stylesheet" href="../jsp/css/accounting.css">
<script type="text/javascript" src="../jsp/js/general.js"></script>
<script type="text/javascript" src="../jsp/js/poReversal.js"></script>
<style>

.custom-table {
  width: 100%;
  table-layout: fixed;
  border-collapse: collapse;
}

.custom-table th,
.custom-table td {
  width: 150px; 
  padding: 8px;
  text-align: center;
  white-space: nowrap; 
   min-width: 150px;
}
</style>
<center>
	<h2>
		<small><%=fieldDesc%></small>
	</h2>
</center>

<div class="container-fluid m-t-20">
	<div class="box">
	
<!-- Muruganantham Modified on 16.02.2022 -->
            <ul class="breadcrumb backpageul">      	
                <li><a href="../home"><span class="underline-on-hover">Dashboard</span> </a></li>                
                <li><a href="../salesreturn/summary"><span class="underline-on-hover">Sales Order Return Summary</span> </a></li>                
                <li><label>Sales Order Return</label></li>                                   
            </ul>             
    <!-- Muruganantham Modified on 16.02.2022 -->
		<div class="box-header menu-drop">
			<h1 style="font-size: 20px;" class="box-title"><%=title%></h1>
			<h1 style="font-size: 18px; cursor: pointer;"
				class="box-title pull-right"
				onclick="window.location.href='../salesreturn/summary'">
				<i class="glyphicon glyphicon-remove"></i>
			</h1>
		</div>
		
		<div class="container-fluid">
			<form class="form-horizontal" name="form" method="post" action="">
				<input type="text" name="plant" value="<%=plant%>" hidden>
				<input type="number" id="numberOfDecimal" style="display: none;"
				value=<%=numberOfDecimal%>>
				<input type="text" name="LOGIN_USER" value="<%=username%>" hidden>
				<input type="hidden" name="isData" value="">
				<div style="padding: 18px;">
					<div class="form-group">
						<div class="row">
							<div class="col-sm-2.5">
								<label class="control-label col-sm-2" for="search">Search</label>
							</div>
							<div class="col-sm-4 ac-box">  		
					  			<input type="text" class="ac-selected form-control" id="ORDERNO" 
					  			name="ORDERNO" value="<%=orderNo%>" placeholder="ORDER NO">
								<span class="select-icon" 
								onclick="$(this).parent().find('input[name=\'ORDERNO\']').focus()">
									<i class="glyphicon glyphicon-menu-down"></i>
								</span>
					  		</div>
					  		<div class="col-sm-4 ac-box">
								<div class="input-group"> 
								 <input type="text" class="ac-selected form-control typeahead" id="auto_giNo" 
								 placeholder="GINO" name="gino" value="<%=invoiceNo%>">				
								<span class="select-icon"  
								onclick="$(this).parent().find('input[name=\'gino\']').focus()">
									<i class="glyphicon glyphicon-menu-down"></i>
								</span>							
								</div>
							</div>	
						</div>
						<div class="row" style="padding:6px">
					  		<div class="col-sm-2">
					  		</div>
							<div class="col-sm-4 ac-box">
								<div class="input-group"> 
								 <input type="text" class="ac-selected form-control typeahead" id="auto_invoiceNo" 
								 placeholder="INVOICE NO" name="invoiceno" value="<%=invoiceNo%>">				
								<span class="select-icon"  
								onclick="$(this).parent().find('input[name=\'invoiceno\']').focus()">
									<i class="glyphicon glyphicon-menu-down"></i>
								</span>							
								</div>
							</div>
						</div>
						<div class="row" style="padding:3px">
					  		<div class="col-sm-2">
					  		</div>
							<div class="col-sm-10 txn-buttons">								
								<button type="button" class="btn btn-success"onClick="javascript:return onGo();">Search</button>							
							</div>
						</div>
					</div>
				</div>
				<div class="form-group">
					<div class="col-sm-6">    
				 		<label class="checkbox-inline">      
			  			<input type=Checkbox name = "select" value="select" onclick="return checkAll(this.checked);">
					 	Select/Unselect All
						</label>
						<label class="checkbox-inline"> 
						 <input type=Checkbox name = "fullReturn" value="fullReturn" onclick="fullReturning()">
						    Full Return
					    </label>
					</div>
					<div class="col-sm-2">
			       			 <label class="control-label col-form-label required">SO RETURN NUMBER</label>
			        </div>
					<div class="col-sm-4">
					
               			 <div class="input-group">
			       			 <INPUT class="form-control" name="sorno" id="sorno" type="TEXT" onchange="checkorderno(this.value)" onkeypress="return blockSpecialCharOrderNo(event)" value="" style="width: 100%" MAXLENGTH=100>
			       			 <span class="input-group-addon"  onClick="onNew()" >
			   		 			<a href="#" data-toggle="tooltip" data-placement="top" title="Auto-Generate" >
			   		 			<i class="glyphicon glyphicon-edit" style="font-size: 20px;"></i></a></span>
			        		</div>
			        </div>
		      	</div>
				<div class="row" style="margin: 0px;">
				<div class ="table-responsive">
					<table class="table table-bordered line-item-table return-table soreturntable custom-table">
						<thead>
							<tr>
								<th style="width: 200px;">SELECT</th>
								<th>ORDER NO</th>
								<th>LINE NO</th>
								<th>GINO</th>
								<th>INVOICE</th>
								<th>PRODUCT ID</th>
								<th>DESCRIPTION</th>
								<th>LOC</th>
								<th>BATCH</th>
								<th>ORDER QTY</th>
								<th>ISSUED QTY</th>
								<th>RETURNED QTY</th>
								<th>RETURN QTY</th>
							</tr>
						</thead>
						<tbody>
							<tr>
								<td colspan="13"></td>
							</tr>
						</tbody>
					</table>
				</div>
				</div>
				<div class="row">
					<div class="col-sm-6">
						<div class="form-group">
							<label class="control-label col-form-label col-sm-4">Return Date :</label>
							<div class="col-sm-6">
								<input type="text" class="form-control datepicker" id="return_date" value="<%=curDate%>"
							name="return_date" READONLY>
							</div>
						</div>
					</div>
					<div class="col-sm-6">
						<div class="form-group">
							<label class="control-label col-form-label col-sm-3">Notes :</label>
							<div class="col-sm-8">
								<textarea rows="2" name="note"   MAXLENGTH="950" class="ember-text-area form-control ember-view"></textarea>
							</div>
						</div>
					</div>
<!-- 					<div class="col-sm-12 text-center"> -->
<!-- 						<button type="button" onclick="if(onReverse(document.form)) {submitForm();}" class="btn btn-success">Return</button> -->
<!-- 					</div> -->
				</div>
				
				<div class="row">
					<div class="col-sm-6">
						<div class="form-group">
							<label class="control-label col-form-label col-sm-4">Customer :</label>
							<div class="col-sm-6">
								<input type="text" class="form-control datepicker" id="CUSTOMERNAME" value=""
							name="CUSTOMERNAME" READONLY disabled>
							</div>
						</div>
					</div>
					<div class="col-sm-12 text-center">
						<button type="button" onclick="if(onReverse(document.form)) {submitForm();}" class="btn btn-success">Return</button>
					</div>
				</div>
			</form>
		</div>
	</div>
</div>
<script>
$(document).ready(function(){
	onNew();
	if("<%=action%>" == "view"){
		onGo();
	}
});
function onGo(){
	var DONO = document.form.ORDERNO.value;
	var INVOICE = document.form.invoiceno.value;
	var GINO = document.form.gino.value;
	if(DONO == "" && INVOICE == "" && GINO == ""){
		alert("Please choose any one of the search filter");
		return flase;
	}
	
	$.ajax( {
		type : "GET",
		url : "/track/ReturnOrderServlet",
		async : true,
		data : { 
			"DONO":DONO,"INVOICE":INVOICE,"Submit":"GET_SALES_ORDER_RETURN_DETAILS",GINO:GINO
		},
		dataType : "json",
		success : function(data) {
			var result = "";
			if(data.ReturnDetails.length > 0){
				document.form.isData.value="DATA";
			}else{
				document.form.isData.value="";
			}
			for(var i = 0; i < data.ReturnDetails.length; i ++){
				if(data.ReturnDetails[i].cnstatus == "1"){
					
				}else{
				var lineno = data.ReturnDetails[i].dolno+"_"+data.ReturnDetails[i].loc+"_"+data.ReturnDetails[i].batch;
// 				var CustName = data.ReturnDetails[i].cname;
				var CustName = data.ReturnDetails[i].CustName;
				$("input[name=CUSTOMERNAME]").val(CustName);
				result += "<tr>";
				result += "<td><input type='checkbox' style='border: 0;' name='chkdDoNo' value='"+data.ReturnDetails[i].dono+"," +data.ReturnDetails[i].dolno+ "," +data.ReturnDetails[i].invoice+ "," +data.ReturnDetails[i].item+ "," +data.ReturnDetails[i].loc+ "," +data.ReturnDetails[i].batch+ "," +data.ReturnDetails[i].qtyor+ "," +data.ReturnDetails[i].qtyissue+"," +data.ReturnDetails[i].UOMQTY+"," +data.ReturnDetails[i].cname+"," +data.ReturnDetails[i].gino+"," +data.ReturnDetails[i].CUSTCODE+"," +data.ReturnDetails[i].uom+"," +data.ReturnDetails[i].qtyrd+"'></td>";
				result += "<td>" +data.ReturnDetails[i].dono+ "</td>";
				result += "<td>" +data.ReturnDetails[i].dolno+ "</td>";
				result += "<td>" +data.ReturnDetails[i].gino+ "</td>";
				result += "<td>" +data.ReturnDetails[i].invoice+ "</td>";
				result += "<td>" +data.ReturnDetails[i].item+ "</td>";
				result += "<td>" +data.ReturnDetails[i].itemdesc+ "</td>";
				result += "<td>" +data.ReturnDetails[i].loc+ "</td>";
				result += "<td>" +data.ReturnDetails[i].batch+ "</td>";
				result += "<td>" +data.ReturnDetails[i].qtyor+ "</td>";
				result += "<td>" +data.ReturnDetails[i].qtyissue+ "</td>";
				result += "<td>" +data.ReturnDetails[i].qtyrd+ "</td>";
				result += "<td><input name='QtyReverse_"+lineno+"' id='QtyReverse_"+lineno+"' type='text' class='form-control text-right' value='0.000' onkeypress='return isNumberKey(event,this,4)'></td>";
				result += "<tr>";
				}
			}
			$(".return-table tbody").html(result);
		}
	});
}

function checkAll(isChk)
{
	var len = document.form.chkdDoNo.length;
	 var orderLNo; 
	 if(len == undefined) len = 1;  
    if (document.form.chkdDoNo)
    {
        for (var i = 0; i < len ; i++)
        {      
              	if(len == 1){
              		document.form.chkdDoNo.checked = isChk;
               	}
              	else{
              		document.form.chkdDoNo[i].checked = isChk;
              	}
        }
    }
}

function onReverse(form){
	if (document.form.isData.value == null|| document.form.isData.value == ""|| document.form.isData.value != "DATA") {
		alert("No Data's Found For Reversal");
		return false;
	}
	
	var DONO = document.form.ORDERNO.value;
	var INVOICE = document.form.invoiceno.value;
	
	var sorno = document.form.sorno.value;
	
	if(sorno == ""){
		alert("Please Generate Sales Return Number");
		return flase;
	}
	
	
	/* if(DONO == "" && INVOICE == ""){
		alert("Please choose any one of the search filter");
		return flase;
	} */
	
	var checkFound = false;  
	var orderLNo;
	var len = document.form.chkdDoNo.length; 
	if(len == undefined) len = 1;
	
	for (var i = 0; i < len ; i++){
		if(len ==1 && document.form.chkdDoNo.checked)
    	{
    		chkstring = document.form.chkdDoNo.value;
    	}
    	else
    	{
    		chkstring = document.form.chkdDoNo[i].value;
    	}
		
		chkdvalue = chkstring.split(',');
		if(len == 1 && (!document.form.chkdDoNo.checked))
		{
			checkFound = false;
		}
		else if(len ==1 && document.form.chkdDoNo.checked)
	     {
	    	 checkFound = true;
	    	 orderLNo = chkdvalue[1]+"_"+chkdvalue[4]+"_"+chkdvalue[5];
	    	 var receivedqty = parseFloat(removeCommas(chkdvalue[7]))-parseFloat(removeCommas(chkdvalue[13]));
	    	 //if(!verifyReverseQty(chkdvalue[7],orderLNo))	
	    	 if(!verifyReverseQty(receivedqty,orderLNo))	
		    	  return false;
	    }
		else {
		     if(document.form.chkdDoNo[i].checked){
		    	 checkFound = true;
		    	 orderLNo = chkdvalue[1]+"_"+chkdvalue[4]+"_"+chkdvalue[5];
		    	 var receivedqty = parseFloat(removeCommas(chkdvalue[7]))-parseFloat(removeCommas(chkdvalue[13]));
		    	 //if(!verifyReverseQty(chkdvalue[7],orderLNo))	
		    	 if(!verifyReverseQty(receivedqty,orderLNo))	
			    	  return false;
		    }
		   
	     }
    }
	if (checkFound != true) {
	    alert ("Please check at least one checkbox.");
	    return false;
    }
	
	var checkFoundzero = false;  
	
	$(".soreturntable tbody tr").each(function() {
		var chk	= $('td:eq(0)', this).find('input').prop("checked");
		if(chk){
			var amt = $('td:eq(12)', this).find('input').val();
			if(amt <= 0){
				checkFoundzero = true;
			}
		}
	});
	
	if (checkFoundzero != false) {
		alert ("Return Quantity Should not be Zero.");
	    return false;
    }
   
   document.form.action ="/track/ReturnOrderServlet?Submit=PROCESS_SALESORDERRETURN";
   document.form.submit();
}

function verifyReverseQty(receivedqty,orderLNo)
{
	var reverseqty = document.getElementById("QtyReverse_"+(orderLNo)).value;
	if (reverseqty == "" || reverseqty.length == 0 || reverseqty == '0') {
		alert("Enter a valid Quantity!");
		document.getElementById("QtyReverse_"+(orderLNo)).focus();
		document.getElementById("QtyReverse_"+(orderLNo)).select();
        return false;
	}
	if(!isNumericInput(reverseqty)){
		alert("Entered Quantity is not a valid number!");
		document.getElementById("QtyReverse_"+(orderLNo)).focus();
		document.getElementById("QtyReverse_"+(orderLNo)).select();
         return false;
	}
	//receivedqty = removeCommas(receivedqty);
	receivedqty = parseFloat(receivedqty).toFixed(3);
	reverseqty = removeCommas(reverseqty);
	reverseqty = parseFloat(reverseqty).toFixed(3);
	if ((Math.round(parseFloat(reverseqty)*100)/100) > (Math.round(parseFloat(receivedqty)*100)/100))
		
	{
		alert("Cannot reverse more than received Qty of LineOrderNO:: "+orderLNo);
		document.getElementById("QtyReverse_"+(orderLNo)).focus();
		document.getElementById("QtyReverse_"+(orderLNo)).select();
		return false;
	}
	
	return true;
}

function isNumericInput(strString) {
	var strValidChars = "0123456789.";
	var strChar;
	var blnResult = true;
	if (strString.length == 0)
		return false;
	//  test strString consists of valid characters listed above
	for (var i = 0; i < strString.length && blnResult == true; i++) {
		strChar = strString.charAt(i);
		if (strValidChars.indexOf(strChar) == -1) {
			blnResult = false;
		}
	}
	return blnResult;
}

function fullReturning()
{
	var len = document.form.chkdDoNo.length;
	 var orderLNo, chkstring, chkdvalue; 
	 if(len == undefined) len = 1;  
    if (document.form.chkdDoNo)
    {
        for (var i = 0; i < len ; i++)
        {      
           	if(len == 1 && document.form.chkdDoNo.checked){
           		chkstring = document.form.chkdDoNo.value;
           		chkdvalue = chkstring.split(',');
               	orderLNo = chkdvalue[1]+"_"+chkdvalue[4]+"_"+chkdvalue[5];
               	var receivedqty = parseFloat(removeCommas(chkdvalue[7]))-parseFloat(removeCommas(chkdvalue[13]));
               	setReceivingQty(orderLNo,i,receivedqty); 
               	//setReceivingQty(orderLNo,i,chkdvalue[7]); 
           	}
           	else if(len == 1 && !document.form.chkdDoNo.checked){
           		return;
           	}
           	else{
               	if(document.form.chkdDoNo[i].checked){
               		chkstring = document.form.chkdDoNo[i].value;
               		chkdvalue = chkstring.split(',');
                   	orderLNo = chkdvalue[1]+"_"+chkdvalue[4]+"_"+chkdvalue[5];
	               	var receivedqty = parseFloat(removeCommas(chkdvalue[7]))-parseFloat(removeCommas(chkdvalue[13]));
                   	setReceivingQty(orderLNo,i,receivedqty); 
                   	//setReceivingQty(orderLNo,i,chkdvalue[7]); 
               	}
           	}
        }
    }
}

function setReceivingQty(orderLNo,i,recvQty){
	var len = document.form.chkdDoNo.length;
	if(len == undefined) len = 1; 
	if(len ==1 && document.form.chkdDoNo.checked){	  
		if(document.form.fullReturn.checked)
		{
			setQty(orderLNo,recvQty);
		}
	}
		
	else if(len !=1 && document.form.chkdDoNo[i].checked){
		if(document.form.fullReturn.checked)
		{
			setQty(orderLNo,recvQty);
		}
		else{
			document.getElementById("QtyReverse_"+orderLNo).value = 0.000;
		}
	}
	else{
		document.getElementById("QtyReverse_"+orderLNo).value = 0.000;	
	}	
}

function setQty(orderLNo,recvQty){
	var QtyReceived = recvQty;
	//QtyReceived = removeCommas(QtyReceived);
	QtyReceiving = parseFloat(QtyReceived).toFixed(3);
	document.getElementById("QtyReverse_"+orderLNo).value = QtyReceiving;	
}

function submitForm(){
	document.form.action ="/track/ReturnOrderServlet?Submit=PROCESS_SALESORDERRETURN";
	document.form.submit();
}

function onNew()
{
	
	var urlStr = "/track/ReturnOrderServlet";
	$.ajax( {
		type : "POST",
		url : urlStr,
		data : {
		PLANT : "<%=plant%>",
		Submit : "SALES_RETURN_NUMBER"
		},
		dataType : "json",
		success : function(data) {
			if (data.status == "100") {
				var resultVal = data.result;				
				var resultV = resultVal.sorno;
				document.form.sorno.value= resultV;
	
			} else {
				alert("Unable to genarate PURCHASE RETURN NO");
				document.form.sorno.value = "";
			}
		}
	});	
	}

function isNumberKey(evt, element, id) {
	  var charCode = (evt.which) ? evt.which : event.keyCode;
	  if (charCode > 31 && (charCode < 48 || charCode > 57) && !(charCode == 46 || charCode == 8 || charCode == 45))
		  {
	    	return false;
		  }
	  return true;
	}


</script>

<jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title%>" />
</jsp:include>