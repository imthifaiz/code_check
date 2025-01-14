<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.dao.*"%>
<%@ page import="com.track.constants.*"%>
<%@ include file="header.jsp"%>


<jsp:useBean id="su" class="com.track.util.StrUtils" />
<%

//String fieldDesc = "Please enter any search criteria";
String TRANSACTIONDATE="";
String fieldDesc="<tr><td>Please enter any search criteria</td></tr>";
session = request.getSession();
String plant = (String) session.getAttribute("PLANT");
Map checkedDOS = (Map) request.getSession().getAttribute("checkedDOS");
String gino = StrUtils.fString(request.getParameter("GINO"));
String tono = su.fString(request.getParameter("TONO"));
String action = su.fString(request.getParameter("action")).trim();
String result   = su.fString(request.getParameter("result")).trim();
String sUserId = (String) session.getAttribute("LOGIN_USER");
session.setAttribute("RFLAG", "9");
String isData =    (String) session.getAttribute("AFLAG");
Map checkedTOS = (Map) request.getSession().getAttribute("checkedTOS");
boolean confirm = false;
TOUtil _TOUtil = new TOUtil();
ToHdrDAO _ToHdrDAO = new ToHdrDAO();
ItemMstDAO _ItemMstDAO = new ItemMstDAO();
ToDetDAO _ToDetDAO = new ToDetDAO();

_TOUtil.setmLogger(mLogger);
_ToHdrDAO.setmLogger(mLogger);
_ItemMstDAO.setmLogger(mLogger);

String chkString ="";
String fromWareHouse="",toWareHouse="";
String sSaveEnb    = "disabled";
String allChecked = "",fullReverse = "";
TRANSACTIONDATE =su.fString(request.getParameter("TRANSACTIONDATE"));
DateUtils _dateUtils = new DateUtils();
String curDate =_dateUtils.getDate();
if(TRANSACTIONDATE.length()<0|TRANSACTIONDATE==null||TRANSACTIONDATE.equalsIgnoreCase(""))TRANSACTIONDATE=curDate;

if (action.equalsIgnoreCase("MultipleView")) {
	Map<String,String> m = (Map<String,String>) (request.getSession().getAttribute("todetVal"));
 	
 	if(request.getSession().getAttribute("RESULTPICK")!=null)
    {
 		fieldDesc = (String) request.getSession().getAttribute("RESULTPICK");
 		fieldDesc="<font class='maingreen'>"+fieldDesc+"</font>";
    }
 	else 
 		 fieldDesc="";
 	
 	request.getSession().setAttribute("RESULTPICK","");

		if (m.size() > 0) {			
			fromWareHouse = (String) m.get("fromwarehouse");
			toWareHouse = (String) m.get("towarehouse");
			
			 

		} else {
			fieldDesc = "Details not found for Consignment order:" + tono;
			fieldDesc="<font class='mainred'>"+fieldDesc+"</font>";
		}
	}
if(result.equalsIgnoreCase("catcherror"))
{

 fieldDesc=(String)request.getSession().getAttribute("CATCHERROR");
 fieldDesc="<font class='mainred'>"+fieldDesc+"</font>";
  
 allChecked = su.fString(request.getParameter("allChecked"));
 fullReverse = su.fString(request.getParameter("fullReceive"));
 
 
}
%>

<%--New page design begin --%>
<%
String title = "Consignment Order Reversal";
%>
<%@include file="sessionCheck.jsp" %>
<jsp:include page="header2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
	<jsp:param name="mainmenu" value="<%=IConstants.CONSIGNMENT%>"/>
    <jsp:param name="submenu" value="<%=IConstants.CONSIGNMENT_TRANSACTION%>"/>
</jsp:include>
<%--New page design end --%>

<!-- <html>
<head>
<title>Transfer Order Reversal</title>
<link rel="stylesheet" href="css/style.css">
<script language="JavaScript" type="text/javascript"src="js/jquery-1.4.2.js"></script> -->
<link rel="stylesheet" href="../jsp/css/typeahead.css">
<link rel="stylesheet" href="../jsp/css/accounting.css">
<script language="JavaScript" type="text/javascript" src="../jsp/js/json2.js"></script>
<script language="JavaScript" type="text/javascript" src="../jsp/js/calendar.js"></script>
<script language="JavaScript" type="text/javascript" src="../jsp/js/general.js"></script>
<script language="JavaScript">
	var subWin = null;

	function popUpWin(URL) {
 		subWin = window.open(URL, 'TransferOrderIssuing', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=800,height=400,left = 200,top = 184');
	}
	function viewTransferOrders(){
		var TONO = document.form.TONO.value;
		var GINO = document.form.gino.value;
		if(TONO == "" && GINO == ""){
			alert("Please choose any one of the search filter");
			return flase;
		}
		document.form.action="/track/TransferOrderServlet?Submit=MultipleView";
        document.form.submit();
    	$("#auto_giNo").typeahead('val', '"');
		$("#auto_giNo").typeahead('val', '');
	}
	function loadTransferOrderDetails() {
		var transferOrderNo = document.form.TONO.value;
		var urlStr = "/track/TransferOrderHandlerServlet";
		$.ajax( {
			type : "POST",
			url : urlStr,
			data : {
				ORDER_NO : transferOrderNo,
				PLANT : "<%=plant%>",
				ACTION : "LOAD_TRANSER_ORDER_DETAILS"
						},
						dataType : "json",
						success : function(data) {
							if (data.status == "100") {
								var resultVal = data.result;
								
								document.form.CUST_NAME.value = resultVal.CUSTNAME;
								document.form.action = "/track/TransferOrderServlet?Submit=MultipleView";
								document.form.submit();

							} else {
								alert("Not a valid Order Number!");
								document.form.TONO.value = "";
								document.form.CUST_NAME.value = "";
								document.form.TONO.focus();
							}
						}
					});
		}

	function submitForm(){
		document.form.action="/track/TransferOrderServlet?Submit=TOReverseConfirm";
	    document.form.submit();
	}
			
	function checkAll(isChk) {
		var len = document.form.chkdToNo.length;
		var orderLNo;
		if (len == undefined)
			len = 1;
		if (document.form.chkdToNo) {
			for ( var i = 0; i < len; i++) {
				if (len == 1) {
					document.form.chkdToNo.checked = isChk;
					orderLNo = document.form.chkline.value;
					//batch = document.form.chkdToNo.value.split(":")[1];
				} else {
					document.form.chkdToNo[i].checked = isChk;
					orderLNo = document.form.chkline[i].value;
					//batch = document.form.chkdToNo[i].value.split(":")[1];
				}
				setIssuingQty(orderLNo,i);    
			}
		}
	}
	function fullReversing(isChk)
	{
		var len = document.form.chkdToNo.length;
		 var orderLNo; 
		 if(len == undefined) len = 1;  
	    if (document.form.chkdToNo)
	    {
	        for (var i = 0; i < len ; i++)
	        {      
	              	if(len == 1 && document.form.chkdToNo.checked){
	              		orderLNo = document.form.chkline.value;
	              		setIssuingQty(orderLNo,i); 
	              	}
	              	else if(len == 1 && !document.form.chkdToNo.checked){
	              		return;
	              	}
	              	else{
	                  	if(document.form.chkdToNo[i].checked){
	              		 orderLNo = document.form.chkline[i].value;
	              		setIssuingQty(orderLNo,i); 
	                  	}
	              	}
	            	      	
	                
	        }
	    }
	}



	function setIssuingQty(orderLNo,i){
		var len = document.form.chkdToNo.length;
		if(len == undefined) len = 1; 
		if(len ==1 && document.form.chkdToNo.checked){	  
			if(document.form.fullReverse.checked)
			{
				setQty(orderLNo);
			}
		  
		}
			
		else if(len !=1 && document.form.chkdToNo[i].checked){
			if(document.form.fullReverse.checked)
			{
				setQty(orderLNo);
			}
			else{
				document.getElementById("QtyReverse_"+orderLNo).value = 0;
			}
		}
		else{
			document.getElementById("QtyReverse_"+orderLNo).value = 0;	
		}
			
	}

	function setQty(orderLNo){
		var QtyIssuing =document.getElementById("Qtypicked_"+orderLNo).value;
		document.getElementById("QtyReverse_"+orderLNo).value = QtyIssuing;	
	}

	function onIssue(form) {
		var tono = document.form.TONO.value;

		if (tono == null || tono == "") {
			alert("Please select Consignment Order");
			return false;
		}
		if (document.form.isData.value == null
				|| document.form.isData.value == ""
				|| document.form.isData.value != "DATA") {
			alert("No Data's Found For Issuing");
			return false;
		}
		//var locId = document.form.LOC_0.value;
		//if (!validateLocation(locId, 0)) {
			//return false;

		//}
		var checkFound = false;
		var orderLNo;
		var len = document.form.chkdToNo.length;
		if (len == undefined)
			len = 1;
		for ( var i = 0; i < len; i++) {
			if(len ==1 && document.form.chkdToNo.checked)
	    	{
	    		chkstring = document.form.chkdToNo.value;
	    	}
	    	else
	    	{
	    		chkstring = document.form.chkdToNo[i].value;
	    	}
			chkdvalue = chkstring.split(',');
			if (len == 1 && (!document.form.chkdToNo.checked)) {
				
				checkFound = false;
			}

			else if (len == 1 && document.form.chkdToNo.checked) {
				checkFound = true;
				orderLNo = document.form.chkline.value;
// 				orderLNo = chkdvalue[0]+"_"+chkdvalue[1];
				 if(!verifyReverseQty(orderLNo))	
			    	  return false;
			}

			else {
				if (document.form.chkdToNo[i].checked) {
					checkFound = true;
					orderLNo = document.form.chkline[i].value;
// 					orderLNo = chkdvalue[0]+"_"+chkdvalue[1];
					 if(!verifyReverseQty(orderLNo))	
				    	  return false;

					//batch = document.form.chkdToNo[i].value.split(":")[1];
					
				}
			}

		}
		if (checkFound != true) {
			alert("Please check at least one checkbox.");
			return false;
		}
		document.form.action = "/track/TransferOrderServlet?Submit=TOReverseConfirm";
		document.form.submit();
	}
	function verifyReverseQty(orderLNo)
	{
		var issuedqty = document.getElementById("Qtypicked_"+(orderLNo)).value;
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
		
		
		issuedqty = removeCommas(issuedqty);
		issuedqty = parseFloat(issuedqty).toFixed(3);
		reverseqty = removeCommas(reverseqty);
		reverseqty = parseFloat(reverseqty).toFixed(3);
		if ((Math.round(parseFloat(reverseqty)*100)/100) > (Math.round(parseFloat(issuedqty)*100)/100))
			
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
		for (var i = 0; i < strString.length && blnResult == true; i++) {
			strChar = strString.charAt(i);
			if (strValidChars.indexOf(strChar) == -1) {
				blnResult = false;
			}
		}
		return blnResult;
	}			
	
	
</script>
<!-- </head> -->

	<%--New page design begin --%>

<div class="container-fluid m-t-20">
	 <div class="box"> 
	 <!-- Muruganantham Modified on 16.02.2022 -->
            <ul class="breadcrumb backpageul">      	
                <li><a href="../home"><span class="underline-on-hover">Dashboard</span> </a></li>                       
                <li><label>Consignment Order Reversal</label></li>                                   
            </ul>             
    <!-- Muruganantham Modified on 16.02.2022 -->
            <div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title"><%=title %></h1>
		</div>
		
 <div class="box-body">
<%--New page design end --%>

<form class="form-horizontal" name="form" method="post" action="/track/TransferOrderServlet?">
 
 <center>
   <h2><small><%=fieldDesc%></small></h2>
  </center>

   		
       <div class="form-group">
       <label class="control-label col-sm-4" for="Consignment Order">
       <i class="glyphicon glyphicon-star" style="font-size: 10px; top:-6px; color: #e50000"></i>&nbsp;Order Number:</label>
       <div class="col-sm-4">
       <div class="input-group">
       <INPUT class="form-control" type="TEXT" size="30" MAXLENGTH="20" name="TONO" value="<%=tono%>" 
       onkeypress="if((event.keyCode=='13') && ( document.form.TONO.value.length > 0)){loadTransferOrderDetails();}"/>
   		 	<!-- <span class="input-group-addon"  onClick="javascript:popUpWin('list/to_order_list.jsp?TONO='+form.TONO.value+'&TYPE=Reverse');"> -->
   		 	<span class="input-group-addon"  onClick="javascript:popUpWin('../jsp/to_list_do.jsp?TONO='+form.TONO.value+'&TYPE=view');">
   		 	<a href="#" data-toggle="tooltip" data-placement="top" title="Consignment Order Details">
   		 	<i  class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span>
  		</div>
  		</div>
  		<div class="form-inline">
  		<div class="col-sm-1 ">   
  		<button type="button" class="Submit btn btn-default" onClick="viewTransferOrders();"><b>View</b></button>
  		</div>
  		</div>
  		</div>
 		 <INPUT type = "hidden" name="CUST_NAME" value = "">
         <INPUT type = "hidden" name="LOGIN_USER" value = "<%=sUserId%>">
        
        <div class="form-group">
        <label class="control-label col-sm-4" for="From Location">From Location:</label>
        <div class="col-sm-4">
        <INPUT class="form-control" type = "TEXT" size="30"  MAXLENGTH="20" readonly  MAXLENGTH=20 name="FROM_WAREHOUSE" value="<%=fromWareHouse%>">
       </div>
       </div>
       
       <div class="form-group">
        <label class="control-label col-sm-4" for="To Location">To Location:</label>
        <div class="col-sm-4">
        <INPUT type="TEXT" size="30" class="form-control" MAXLENGTH="20" readonly name="TO_WAREHOUSE" value="<%=toWareHouse%>"/>
       </div>
       </div>
       
       <div class="form-group"> 
       <label class="control-label col-sm-4" for="GINO">GINO:</label>
        <div class="col-sm-4">
		<input type="text" class="form-control" id="auto_giNo"  name="gino" value="<%=gino%>">				
		<span class="select-icon" onclick="$(this).parent().find('input[name=\'gino\']').focus()">
		<i class="glyphicon glyphicon-menu-down"></i>
		</span>							
  		</div>
  		</div>
       
<!--        					<div class="form-group">  -->
<!-- 								<label class="control-label col-sm-4" for="GINO">GINO:</label> -->
<%-- 								<input type="text" class="form-control" id="auto_giNo"  name="gino" value="<%=tono%>">				 --%>
<!-- 								<span class="select-icon" onclick="$(this).parent().find('input[name=\'gino\']').focus()"> -->
<!-- 								<i class="glyphicon glyphicon-menu-down"></i> -->
<!-- 								</span>							 -->
<!-- 								</div> -->
       
<!--   		<div class="form-group"> -->
<!-- 		<label class="control-label col-sm-4" for="GINO">GINO:</label> -->
<!-- 		<div class="col-sm-4"> -->
<%-- 		<input type="text" class="form-control" id="auto_giNo" name="gino" value="<%=gino%>">				 --%>
<!-- 		<span class="select-icon" onclick="$(this).parent().find('input[name=\'gino\']').focus()"> -->
<!-- 		<i class="glyphicon glyphicon-menu-down"></i> -->
<!-- 		</span>							 -->
<!-- 		</div> -->
<!-- 		</div> -->
 		
 		<div class="form-group">
       	<div class="col-sm-12">    
      	<label class="checkbox-inline">      
        <INPUT Type=Checkbox  style="border:0;" name = "select" value="select" <%if(allChecked.equalsIgnoreCase("true")){%>checked <%}%>onclick="return checkAll(this.checked);">
        <b>Select/Unselect All</b></label>
        <label class="checkbox-inline">      
        <INPUT Type=Checkbox  style="border:0;" name="fullReverse"  <%if(fullReverse.equalsIgnoreCase("true")){%>checked <%}%> value="fullReverse" onclick="return fullReversing(this.checked)">
        <b>Full Reverse</b></label>
		</div>
		</div> 		
 			  		
  		 <TABLE BORDER="0" CELLSPACING="0" WIDTH="100%" class="table" >
         <thead style="background: #eaeafa; font-size: 15px">
         <tr>
        <th width="5%">Select </th>
        <th width="6%">Order No </th>
		<th width="6%">Line No </th>
		<th width="6%">GINO </th>
		<th width="10%">Product ID</th>
		<th width="20%">Description </th>
		<th width="10%">UOM </th>
		<th width="10%">Order Qty </th>
		<th width="10%">Picked Qty </th>
		<th width="10%">Batch</th>
		<th width="10%">Reverse Qty </th>
		<th width="5%">Status </th>
       	 </thead>
       	 <tbody style="font-size: 15px;">
       <%
						//String pickingQty = "",value = null,batch = "NOBATCH";
						//ArrayList al = _TOUtil.listTransferSummaryReverseTODET(plant,tono);
// 						ArrayList al = _TOUtil.listConsignmentReverseTODET(plant,tono); //Azees feb_21
				if(!tono.equalsIgnoreCase("") || (!gino.equalsIgnoreCase(""))){
					List<Map<String, String>> al = _TOUtil.listConsignmentReverseGINO(plant,tono,gino); //imti mar 4
						String value = null,reverseQty=""/* batch = "NOBATCH" */;
						if(al.size()==0)
					    {
							isData="";
					    }
					      
						if (al.size() > 0) {
							isData="DATA";
							for (int i = 0; i < al.size(); i++) {
								reverseQty = "";
// 						    	   batch = "NOBATCH";
								Map m = (Map) al.get(i);
								int iIndex = i + 1;
								String bgcolor = ((i == 0) || (i % 2 == 0)) ? "#FFFFFF"
										: "#FFFFFF";
								  tono = (String)m.get("tono");
						          String tolno = (String)m.get("tolno");
						          gino = (String)m.get("gino");
								  String item= (String)m.get("item");
								  String desc= (String)m.get("itemdesc");
						          String status = (String)m.get("status");
						          String qtyor= (String)m.get("qtyor");
						          String qtypick= (String)m.get("qtypick");
						          String batch = (String)m.get("batch");
						          String uom = (String)m.get("UNITMO");
								  tolno= tolno.trim(); 
								  chkString  =tolno+","+batch+","+item+","+qtyor+","+qtypick+","+tono+","+gino+","+iIndex;
								  String lineindex = String.valueOf(iIndex);
						          String lineno=lineindex+"_"+batch;
						          
						          
						          if(checkedDOS!=null && result.equalsIgnoreCase("catchrerror")){
						              value = (String)checkedDOS.get(tolno);   
						              
						              if(value!=null)
						              {
						            	  reverseQty  = value.split(":")[0];
						            	  batch  = value.split(":")[1];
						            	 
						              }
						              }
						        							
								
					%>
					<TR bgcolor="<%=bgcolor%>">

						<TD width="5%" align="CENTER">
						<input type="hidden" value="<%=lineno%>" name="chkline">
						<font color="black"><INPUT
							Type=checkbox style="border: 0;" name="chkdToNo" 
							value="<%=chkString%>" <%if(value!=null){%>checked <%}%> onclick = "setIssuingQty('<%=lineno%>',<%=i%>);" ></font></TD>
						<TD width="6%" align="center"><%=tono%></TD>	
						<TD width="6%" align="center"><%=tolno%></TD>
						<TD width="6%" align="center"><%=gino%></TD>
						<TD align="left" width="15%"><%=item%></TD>
						<TD align="left" width="20%"><%=(String) desc%></TD>
						<TD align="left" width="10%"><%=(String) uom%></TD>
						<TD align="center" width="11%"><input class="form-control" type="text" name = "Qtyordered_<%=lineno%>" id = "Qtyordered_<%=lineno%>" 
						 size = "6" maxlength = "10"   style="border: 0px; background-color: '<%=bgcolor%>';" readonly  value = <%=qtyor%>></input>
						</TD>
						<TD align="center" width="11%"><input class="form-control" type="text" name = "Qtypicked_<%=lineno%>" id = "Qtypicked_<%=lineno%>" 
						 size = "6" maxlength = "10" style="border: 0px;background-color: '<%=bgcolor%>';" readonly  value = <%=qtypick%> ></input>
						</TD>
						<TD align="center" width="13%"><input class="form-control" type="text" name = "batch_<%=lineno%>" id = "batch_<%=lineno%>" 
						 size = "10" style="border: 0px;background-color: '<%=bgcolor%>';" readonly  value = "<%=batch.trim()%>" ></input>
						</TD>
						<TD align="center" width="11%"><input class="form-control" type="text" name = "QtyReverse_<%=lineno%>" id = "QtyReverse_<%=lineno%>" 
						 size = "6" maxlength = "10"   value = <%=reverseQty%> ></input>
						</TD>
						
						<TD align="center" width="5%"><%=status%></TD>
											
					</TR>

					<%
						}
						} 
				}else {
					%>

					<TR>
						<TD align="center" colspan="9">Data's Not Found For Picking</TD>
					</TR>
					<%
						}
					%>
       
         </tbody>
       </TABLE>
       
        <INPUT	type="Hidden" name="RFLAG" value="9">
		<INPUT type="Hidden" name="isData" value="<%=isData%>">
				
		
 		<div class="form-group">
        <label class="control-label col-sm-2" for="Transaction Date">Transaction Date:</label>
        <div class="col-sm-2">
        <div class="input-group">          
        <INPUT class="form-control datepicker" name="TRANSACTIONDATE" id="TRANSACTIONDATE"	value="<%=TRANSACTIONDATE%>" type="TEXT" size="20" MAXLENGTH="80" readonly="readonly">
        </div>
    	</div>
    	<div class="form-inline">
    	<div class="col-sm-offset-5">
    	<button type="button" class="Submit btn btn-default" onClick="window.location.href='../home'"><b>Back</b></button>&nbsp;&nbsp;
       	<!-- <button type="button" class="Submit btn btn-default" onClick="window.location.href='outboundTransaction.jsp'"><b>Back</b></button>&nbsp;&nbsp; -->
      	<button type="button" class="Submit btn btn-default" onClick="if(onIssue(document.form)) {submitForm();}"><b>Reverse</b></button>&nbsp;&nbsp;
      	<INPUT name="LOGIN_USER"	type="hidden" value="<%=sUserId%>">
    	</div>
    	</div>
    	</div>
    	
    	
         		
  		</form>
		</div>
		</div>
		</div>
<script>
$(document).ready(function(){
	var numberOfDecimal = $("#numberOfDecimal").val();

	$('#auto_giNo').typeahead({
		  hint: true,
		  minLength:0,  
		  searchOnFocus: true
		},
		{
		  display: 'GINO',  
		  source: function (query, process,asyncProcess) {
			var urlStr = "/track/TransferOrderServlet";
			$.ajax( {
			type : "POST",
			url : urlStr,
			async : true,
			data : {
				PLANT : "<%=plant%>",
				Submit : "GET_GINO_NO_FOR_AUTO_SUGGESTION",
				TONO : document.form.TONO.value, 
			},
			dataType : "json",
			success : function(data) {
				return asyncProcess(data.gino);
			}
		   });
		  },
		  limit: 9999,
		  templates: {
		  empty: [
		      '<div style="padding:3px 20px">',
		        'No results found',
		      '</div>',
		    ].join('\n'),
		    suggestion: function(data) {
		    return '<p>' + data.GINO + '</p>';
			}
		  }
		}).on('typeahead:open',function(event,selection){
			var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
			element.toggleClass("glyphicon-menu-up",true);
			element.toggleClass("glyphicon-menu-down",false);
		}).on('typeahead:close',function(){
			var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
			element.toggleClass("glyphicon-menu-up",false);
			element.toggleClass("glyphicon-menu-down",true);
		});
	
});
</script>


<jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
</jsp:include>