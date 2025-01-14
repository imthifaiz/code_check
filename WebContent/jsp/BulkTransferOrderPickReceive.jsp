<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.dao.*"%>
<%@ page import="com.track.constants.*"%>
<%@ include file="header.jsp"%>


<jsp:useBean id="su" class="com.track.util.StrUtils" />
<%

//String fieldDesc = "Please enter any search criteria";
String fieldDesc="<tr><td>Please enter any search criteria</td></tr>";
session = request.getSession();
String plant = (String) session.getAttribute("PLANT");
String tono = su.fString(request.getParameter("TONO"));
String action = su.fString(request.getParameter("action")).trim();
String result   = su.fString(request.getParameter("result")).trim();
String TRANSACTIONDATE = su.fString(request.getParameter("TRANSACTIONDATE"));
DateUtils _dateUtils = new DateUtils();
String curDate =_dateUtils.getDate();
if(TRANSACTIONDATE.length()<0|TRANSACTIONDATE==null||TRANSACTIONDATE.equalsIgnoreCase(""))TRANSACTIONDATE=curDate;
String sUserId = (String) session.getAttribute("LOGIN_USER");
session.setAttribute("RFLAG", "10");
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


String vend = "",deldate="",jobNum = "",custName = "",custCode="",personIncharge = "",contactNum = "";
String remark1 = "",remark2 = "",address="",address2="",address3="",collectionDate="",collectionTime="";
String contactname="",telno="",email="",add1="",add2="",add3="",add4="",country="",zip="",remarks="",chkString ="";
String fromWareHouse="",toWareHouse="",item_loc= "";
String sSaveEnb    = "disabled";
String fullIssue = "",allChecked = "",REMARKS  = "",GINO="";

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
			custName = (String) m.get("custName");
			personIncharge = (String) m.get("contactname");
			collectionDate = (String) m.get("collectionDate");
			collectionTime = (String) m.get("collectionTime");
			
			 contactNum=(String)m.get("contactNum");
	         telno=(String)m.get("telno");
	         email=(String)m.get("email");
	         add1=(String)m.get("add1");
	         add2=(String)m.get("add2");
	         add3=(String)m.get("add3");
	         add4=(String)m.get("add4");
	         country=(String)m.get("country"); 
	         zip=(String)m.get("zip");
	         //contactNum=(String)m.get("contactNum");
	         //address=(String)m.get("address");
	         //address2=(String)m.get("address2");
	         //address3=(String)m.get("address3");
	         deldate=(String)m.get("collectionDate");
	         remark1=(String)m.get("remark1");
	         remark2=(String)m.get("remark2");
			

		} else {
			fieldDesc = "Details not found for Consignment order:" + tono;
			fieldDesc="<font class='mainred'>"+fieldDesc+"</font>";
		}
	}
if(result.equalsIgnoreCase("catcherror"))
{
 fieldDesc=(String)request.getSession().getAttribute("CATCHERROR");
 fieldDesc="<font class='mainred'>"+fieldDesc+"</font>";
  item_loc = su.fString(request.getParameter("LOC"));
 allChecked = su.fString(request.getParameter("allChecked"));
 fullIssue = su.fString(request.getParameter("fullIssue"));
 }
%>

<%--New page design begin --%>
<%
String title = " Bulk Pick/Receive By Consignment Order";
%>
<%@include file="sessionCheck.jsp" %>
<jsp:include page="header2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
	<jsp:param name="mainmenu" value="<%=IConstants.CONSIGNMENT%>"/>
    <jsp:param name="submenu" value="<%=IConstants.CONSIGNMENT_TRANSACTION%>"/>
</jsp:include>
<style type="text/css">
.backpageul
{
	background-color: rgb(255, 255, 255);
    padding: 0px 10px;
    margin-bottom: 0px;
    margin-top: 15px;
}
.underline-on-hover:hover {
  		text-decoration: underline;
	}</style>
<%--New page design end --%>

<!--  <html>
<head>
<title>Goods Pick/Receive(Bulk) By Transfer Order</title> 
<link rel="stylesheet" href="css/style.css">
<script src="js/jquery-1.4.2.js"></script> -->
<script src="../jsp/js/json2.js"></script>
<script src="../jsp/js/calendar.js"></script>
<script src="../jsp/js/general.js"></script>
<script>

$(document).ready(function(){
	onNew();
});

	var subWin = null;

	function popUpWin(URL) {
 		subWin = window.open(URL, 'TransferOrderIssuing', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=800,height=400,left = 200,top = 184');
	}
	function popWinBatch(URL) {
		
		subWin = window.open(URL, 'TransferOrderPicking', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=800,height=400,left = 200,top = 184');			
	}
	function viewTransferOrders(){
		document.form.action="/track/TransferOrderServlet?Submit=MultipleView";
        document.form.submit();
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
		document.form.action="/track/TransferOrderServlet?Submit=Bulk_PickReceiveConfirm";
	    document.form.submit();
	}
	function validateLocation(locId, index) {
		var isValid;
		if(locId=="" || locId.length==0 ) {
			alert("Enter Location!");
			document.getElementById("LOC_"+index).focus();
			return false;
		}else{
			var urlStr = "/track/InboundOrderHandlerServlet";
			$.ajax( {
				type : "POST",
				url : urlStr,
				async : false,
				data : {
					LOC : locId,
	                USERID : "<%=sUserId%>",
					PLANT : "<%=plant%>",
					ACTION : "VALIDATE_LOCATION"
					},
					dataType : "json",
					success : function(data) {
						if (data.status != "100") {
	                               
							alert("Not a valid Location");
							document.getElementById("LOC_"+index).focus();
							document.getElementById("LOC_"+index).value="";
							isValid = false;	
														
						} 
						else 
							isValid =  true;
					}
				});
			 return isValid;
			}
		}

	
	function fullIssuing (isChk) {
		var len = document.form.chkdToNo.length;
		var orderLNo;
		if (len == undefined)
			len = 1;
		if (document.form.chkdToNo) {
			for ( var i = 0; i < len; i++) {
				if (len == 1 && document.form.chkdToNo.checked) {
					orderLNo = document.form.chkdToNo.value;
					//batch = document.form.chkdToNo.value.split(":")[1];
					setIssuingQty(orderLNo,i);
				} else if (len == 1 && !document.form.chkdToNo.checked) {
					return;
				} else {
					if (document.form.chkdToNo[i].checked) {
						orderLNo = document.form.chkdToNo[i].value;
						//batch = document.form.chkdToNo[i].value.split(":")[1];
						setIssuingQty(orderLNo,i);
					}
				}

			}
		}
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
					orderLNo = document.form.chkdToNo.value;
					//batch = document.form.chkdToNo.value.split(":")[1];
				} else {
					document.form.chkdToNo[i].checked = isChk;
					orderLNo = document.form.chkdToNo[i].value;
					//batch = document.form.chkdToNo[i].value.split(":")[1];
				}
				setIssuingQty(orderLNo,i);

			}
		}
	}

	function setIssuingQty(orderLNo,i) {
		var len = document.form.chkdToNo.length;
		if (len == undefined)
			len = 1;
		if (len == 1 && document.form.chkdToNo.checked) {
			if (document.form.fullIssue.checked) {
				setQty(orderLNo);
			}

		}

		else if (len != 1 && document.form.chkdToNo[i].checked) {
			if (document.form.fullIssue.checked) {
				setQty(orderLNo);
			} else {
				document.getElementById("pickingQty_" + orderLNo).value = 0;
						
			}
		} else {
			document.getElementById("pickingQty_" + orderLNo).value = 0;
		}

	}

	function setQty(orderLNo) {
		var QtyPicked = document.getElementById("Qtyordered_" + orderLNo).value;
				
		QtyPicked = removeCommas(QtyPicked);
		var QtyReceived = document.getElementById("Qtypicked_" + orderLNo).value;
				
		QtyReceived = removeCommas(QtyReceived);
		var QtyReceiving = QtyPicked - QtyReceived;
		QtyReceiving = parseFloat(QtyReceiving).toFixed(3);
		document.getElementById("pickingQty_" + orderLNo).value = QtyReceiving;
	}

	function onIssue(form) {
		var tono = document.form.TONO.value;

		if (tono == null || tono == "") {
			alert("Please select Transfer Order");
			return false;
		}
		if (document.form.isData.value == null
				|| document.form.isData.value == ""
				|| document.form.isData.value != "DATA") {
			alert("No Data's Found For Issuing");
			return false;
		}
		var locId = document.form.LOC_0.value;
		if (!validateLocation(locId, 0)) {
			return false;

		}
		if(document.form.GINO.value==null||document.form.GINO.value=="")
	    {
	   	   	alert("You are not able to submit this transaction without GINO");
	   	 	return false;
	    }
		var checkFound = false;
		var orderLNo;
		var len = document.form.chkdToNo.length;
		if (len == undefined)
			len = 1;
		for ( var i = 0; i < len; i++) {
			if (len == 1 && (!document.form.chkdToNo.checked)) {
				checkFound = false;
			}

			else if (len == 1 && document.form.chkdToNo.checked) {
				checkFound = true;
				orderLNo = document.form.chkdToNo.value;
				//batch = document.form.chkdToNo.value.split(":")[1];
				if (!verifyRecvQty(orderLNo))
					return false;
			}

			else {
				if (document.form.chkdToNo[i].checked) {
					checkFound = true;
					orderLNo = document.form.chkdToNo[i].value;
					//batch = document.form.chkdToNo[i].value.split(":")[1];
					if (!verifyRecvQty(orderLNo))
						return false;
				}
			}

		}
		if (checkFound != true) {
			alert("Please check at least one checkbox.");
			return false;
		}
		document.form.action = "/track/TransferOrderServlet?Submit=Bulk_PickReceiveConfirm";
		document.form.submit();
	}

	function verifyRecvQty(orderLNo) {

		var recvQty = document.getElementById("pickingQty_" + orderLNo).value;
				
		if (recvQty == "" || recvQty.length == 0 || recvQty <= 0) {
			alert("Enter a valid Quantity!");
			document.getElementById("pickingQty_" + orderLNo).focus();
					
			document.getElementById("pickingQty_" + orderLNo).select();
					
			return false;
		}
		if(!isNumericInput(recvQty)){
			alert("Entered Quantity is not a valid number!");
			document.getElementById("pickingQty_"+orderLNo).focus();
			document.getElementById("pickingQty_"+orderLNo).select();
		        return false;
			}
		recvQty = parseFloat(recvQty).toFixed(3);
		var recvdQty = document.getElementById("Qtypicked_" + orderLNo).value;
				
		recvdQty = removeCommas(recvdQty);
		recvdQty = parseFloat(recvdQty).toFixed(3);
		var Qtypicked = document.getElementById("Qtyordered_" + orderLNo).value;
				
		Qtypicked = removeCommas(Qtypicked);
		Qtypicked = parseFloat(Qtypicked).toFixed(3);
		
		var balanceQty =  Qtypicked - recvdQty;
		balanceQty = parseFloat(balanceQty).toFixed(3); 
		if ((Math.round(parseFloat(recvQty)*100)/100) > (Math.round(parseFloat(balanceQty)*100)/100)){
		//if (recvQty > balanceQty) {
			alert("Exceeded the Available Qty of LineOrderNO:: " + orderLNo);
			document.getElementById("pickingQty_" + orderLNo).focus();
			document.getElementById("pickingQty_" + orderLNo).select();
					
			return false;
		}
		return true;
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
	
	function onNew()
	{
		
		var urlStr = "/track/OutboundOrderHandlerServlet";
		$.ajax( {
			type : "POST",
			url : urlStr,
			data : {
			PLANT : "<%=plant%>",
			ACTION : "GINO"
			},
			dataType : "json",
			success : function(data) {
				if (data.status == "100") {
					var resultVal = data.result;				
					var resultV = resultVal.invno;
					document.form.GINO.value= resultV;
		
				} else {
					alert("Unable to genarate GINO");
					document.form.GINO.value = "";
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
<!-- </head>
 -->
	

<%--New page design begin --%>

<div class="container-fluid m-t-20">
	 <div class="box"> 
	 <!-- Muruganantham Modified on 16.02.2022 -->
            <ul class="breadcrumb backpageul">      	
                <li><a href="../home"><span class="underline-on-hover">Dashboard</span> </a></li>                       
                <li>Bulk Pick/Receive By Consignment Order</li>                                   
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
       <label class="control-label col-sm-4" for="outbound Order">
       <i class="glyphicon glyphicon-star" style="font-size: 10px; top:-6px; color: #e50000"></i>&nbsp;Order Number:</label>
       <div class="col-sm-4">
       <div class="input-group">
       <INPUT class="form-control" type="TEXT" size="30" MAXLENGTH="20" name="TONO" value="<%=tono%>" 
       onkeypress="if((event.keyCode=='13') && ( document.form.TONO.value.length > 0)){loadTransferOrderDetails();}"/>
   		 	<span class="input-group-addon"  onClick="javascript:popUpWin('../jsp/to_list_do.jsp?TONO='+form.TONO.value+'&TYPE=issue');">
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
 		
 		<div class="form-group">
  		<label class="control-label col-sm-4" for="Assignee name">Customer Name:</label>
        <div class="col-sm-4">
        <INPUT name="CUST_NAME"   class="form-control" MAXLENGTH="20" readonly type = "TEXT" value="<%=su.forHTMLTag(custName)%>" size="30"  MAXLENGTH=80>
    	</div>
 		</div>
 		
 		<INPUT type = "hidden" name="CUST_CODE" value = "<%=custCode%>">
        <INPUT type = "hidden" name="LOGIN_USER" value = "<%=sUserId%>">
        <INPUT type = "hidden" name="CUST_CODE1" value = "<%=custCode%>">
        
        <div class="form-group">
    	<label class="control-label col-sm-4" for="Person Incharge">Contact Name:</label>
        <div class="col-sm-4">
        <INPUT type = "TEXT" size="30"  MAXLENGTH="20"  class = "form-control" readonly name="PERSON_INCHARGE" value="<%=personIncharge%>">
        </div>
 		</div>
        
        <div class="form-group">
        <label class="control-label col-sm-4" for="From Location">From Location:</label>
        <div class="col-sm-4">
        <INPUT type = "TEXT" size="30"   class="form-control" MAXLENGTH="20" readonly  MAXLENGTH=20 name="FROM_WAREHOUSE" value="<%=fromWareHouse%>">
       </div>
 		</div>
 		 
        <div class="form-group">
        <label class="control-label col-sm-4" for="To Location">To Location:</label>
        <div class="col-sm-4">
        <INPUT type="TEXT" size="30" class="form-control" MAXLENGTH="20" readonly name="TO_WAREHOUSE" value="<%=toWareHouse%>"/>
        </div>
      	<INPUT type = "Hidden" size="20"  MAXLENGTH=20 name="CONTACT_NUM" value="<%=contactNum%>">
    	<!-- <div class="form-inline">
    	<label class="control-label col-sm-3" for="Telephone">Telephone:</label>
        <div class="col-sm-3"> -->
        <INPUT  class="form-control" name="TELNO" type="hidden" value="<%=telno%>" size="30" MAXLENGTH=100 readonly>
        <!-- </div>
 		</div> -->
 		</div>
 
 		<div class="form-group">
        <label class="control-label col-sm-4" for="Order Date">Order Date:</label>
        <div class="col-sm-4">
        <INPUT type="TEXT" class="form-control" MAXLENGTH="20" readonly size="30" name="DELDATE" value="<%=deldate%>"/>
        </div>
    	<!-- <div class="form-inline">
    	<label class="control-label col-sm-3" for="Email Address">Email:</label>
        <div class="col-sm-3"> -->
        <INPUT	type="hidden" size="30" class="form-control" readonly MAXLENGTH="20"name="EMAIL" value="<%=email%>" />
        <!-- </div>
 		</div> -->
 		</div>
 		
 		<div class="form-group">
        <label class="control-label col-sm-4" for="Time">Order Time:</label>
        <div class="col-sm-4">
        <INPUT type="TEXT" size="30" class="form-control" MAXLENGTH="20" readonly name="COLLECTION_TIME" value="<%=collectionTime%>"/>
        </div>
    	<!-- <div class="form-inline">
    	<label class="control-label col-sm-3" for="Unit Number">Unit No:</label>
        <div class="col-sm-3"> -->
        <INPUT type="hidden" size="30" class="form-control" readonly MAXLENGTH="20" name="ADD1" value="<%=add1%>" />
        <!-- </div>
 		</div> -->
 		</div>
 		 		
    	<div class="form-group">
    	 <label class="control-label col-sm-4" for="Remarks">Remarks:</label>
        <div class="col-sm-4">
        <INPUT type="TEXT" size="30" MAXLENGTH="20" class="form-control" readonly name="REMARK1" value="<%=remark1%>"/>
        </div>
    	<!-- <div class="form-inline">
    	<label class="control-label col-sm-3" for="Buiding Name">Building:</label>
        <div class="col-sm-3"> -->
        <INPUT type="hidden" size="30" MAXLENGTH="20"	class="form-control" readonly name="ADD2" value="<%=add2%>" />
       <!--  </div>
 		</div> -->
 		</div>
 		
    	<!-- <div class="form-group">
    	<div class="form-inline">
    	<label class="control-label col-sm-8" for="street">Street:</label>
        <div class="col-sm-3"> -->
        <INPUT type="hidden" size="30" MAXLENGTH="20"	class="form-control" readonly name="ADD3" value="<%=add3%>" />
        <!-- </div>
 		</div>
 		 </div> -->
 		 
      	<!-- <div class="form-group">
    	<div class="form-inline">
    	<label class="control-label col-sm-8" for="City">City:</label>
        <div class="col-sm-3"> -->
        <INPUT type="hidden" size="30" MAXLENGTH="20"	class="form-control" readonly name="ADD4" value="<%=add4%>" />
        <!-- </div>
 		</div>
 		</div> -->
 		
 		<!-- <div class="form-group">
 		<div class="form-inline">
    	<label class="control-label col-sm-8" for="State">State:</label>
        <div class="col-sm-3"> -->
        <INPUT type="hidden" size="30" MAXLENGTH="20"	class="form-control" readonly name="" value="" />
        <!-- </div>
 		</div> 
 	 	</div> -->
 	 	
    	<!-- <div class="form-group">
    	<div class="form-inline">
    	<label class="control-label col-sm-8" for="country">Country:</label>
        <div class="col-sm-3"> -->
        <INPUT type="hidden" size="30" class="form-control" MAXLENGTH="20" name="COUNTRY" readonly value="<%=country%>" /> 
        <!-- </div>
 		</div>
 		</div> -->
 	 	 		
        <!-- <div class="form-group">
        <div class="form-inline">
    	<label class="control-label col-sm-8" for="postal code">Postal Code:</label>
        <div class="col-sm-3"> -->
        <INPUT type="hidden" size="30" class="form-control" MAXLENGTH="20" name="ZIP"	readonly value="<%=zip%>" />
        <!-- </div>
 		</div>
 		</div> -->

        <!-- <div class="form-group">
        <div class="form-inline">
    	<label class="control-label col-sm-8" for="Assignee remarks">Assignee Remarks:</label>
        <div class="col-sm-3"> -->
        <INPUT type="hidden" size="30" class="form-control" readonly MAXLENGTH=20 name="REMARK2" value="<%=remark2%>">
        <!-- </div>
 		</div>
 		</div> -->
 		
 		<div class="form-group">
       	<div class="col-sm-12">    
      	<label class="checkbox-inline">      
        <INPUT Type=Checkbox  style="border:0;" name = "select" value="select" <%if(allChecked.equalsIgnoreCase("true")){%>checked <%}%>onclick="return checkAll(this.checked);">
       <b>Select/Unselect All</b></label>
		<label class="checkbox-inline">      
        <INPUT Type=Checkbox  style="border:0;" name="fullIssue"  <%if(fullIssue.equalsIgnoreCase("true")){%>checked <%}%> value="fullIssue" onclick="return fullIssuing(this.checked)">
         <b>Full Issuing</b> </label>
		</div>
    	</div> 		
 			  		
  	<table id="tableSalesOrderSummary" class="table table-bordred table-striped">                   
         <thead>
         <tr>
        <th width="5%">Select </th>
		<th width="8%">Order Line No </th>
		<th width="15%">Product ID </th>
		<th width="20%">Description </th>
		<th width="8%">UOM </th> 
		<th width="10%">Order Qty </th>
		<th width="10%">Picked/Received Qty </th>
		<th width="10%">Picking/Receiving Qty</th>						
		<th width="18%">Batch </th>
		<th width="5%">Status</th>
       	 </thead>
       	 <tbody style="font-size: 15px;">
       	<%
						String pickingQty = "",value = null,batch = "NOBATCH";
						ArrayList al = _TOUtil.listTODETForPicking(plant,tono);
						if(al.size()==0)
					    {
							isData="";
					    }
					      
						if (al.size() > 0) {
							isData="DATA";
							for (int i = 0; i < al.size(); i++) {
								pickingQty = "";
								batch = "NOBATCH";
								Map m = (Map) al.get(i);
								int iIndex = i + 1;
								String bgcolor = ((i == 0) || (i % 2 == 0)) ? "#FFFFFF"
										: "#FFFFFF";
								  tono = (String)m.get("tono");
						          String tolnno = (String)m.get("tolnno");
								  //String custname= (String)m.get("custname");
						          String item= (String)m.get("item");
						         // String loc= (String)m.get("loc");
						         // String batch= (String)m.get("BATCH");
						          String status = (String)m.get("pickstatus");
						          String qtyor= (String)m.get("qtyor");
						          String qtypick= (String)m.get("qtyPick");
						          String desc= (String)m.get("itemdesc");//_ItemMstDAO.getItemDesc(plant ,item);
						          String uom= (String)m.get("stkuom");//_ItemMstDAO.getItemUOM(plant ,item);
						        
								 if(checkedTOS!=null && result.equalsIgnoreCase("CATCHERROR")){
									 value = (String)checkedTOS.get(tolnno);   
						              
						              if(value!=null)
						              {
						            	  pickingQty  = value.split(":")[0];
						            	  batch  = value.split(":")[1];
						            	 
						              }
									// pickingQty = (String)checkedTOS.get(tolnno+":"+batch); 
							     }
								
					%>
					<TR bgcolor="<%=bgcolor%>">

						<TD width="5%" align="CENTER"><font color="black"><INPUT
							Type=checkbox style="border: 0;" name="chkdToNo" <%if(value!=null){%>checked <%}%>
							value="<%=tolnno%>" onclick = "setIssuingQty('<%=tolnno%>','<%=i%>');"></font></TD>
						<TD width="8%" align="center"><%=tolnno%></TD>
						<TD align="left" width="15%"><%=item%></TD>
						<TD align="left" width="20%"><%=(String) desc%></TD>
						<TD align="left" width="8%"><%=(String) uom%></TD> 
						<TD align="center" width="10%"><input class="form-control" type="text" name = "Qtyordered_<%=tolnno%>" id = "Qtyordered_<%=tolnno%>" 
						 size = "6" maxlength = "10"   style="border: 0px; background-color: '<%=bgcolor%>';" readonly  value = <%=qtyor%>></input>
						</TD>
						<TD align="center" width="10%"><input class="form-control" type="text" name = "Qtypicked_<%=tolnno%>" id = "Qtypicked_<%=tolnno%>" 
						 size = "6" maxlength = "10" style="border: 0px;background-color: '<%=bgcolor%>';" readonly  value = <%=qtypick%> ></input>
						</TD>
						<TD align="center" width="10%"><input class="form-control" type="text" onkeypress="return isNumberKey(event,this,4)" name = "pickingQty_<%=tolnno%>" id = "pickingQty_<%=tolnno%>" 
						 size = "6" maxlength = "10" value = <%=pickingQty%> >
						</TD>
						<!-- <TD align="center" width="15%"><input class="form-control" type="text" name = "batch_<%=tolnno%>" id = "batch_<%=tolnno%>" 
						 size = "10"   value = <%=batch%> >
						</TD>-->
			<td width="18%" align="center"><div class="input-group"><INPUT class="form-control" name="BATCH_<%=tolnno%>" id="BATCH_<%=tolnno%>" value = <%=batch%> type="TEXT" size="15"  /><input type="hidden" name="BATCH_ID_<%=tolnno%>" value="-1" />
             	<a href="#" class="input-group-addon" data-toggle="tooltip" data-placement="top" title="Batch Details"	
             	onclick="javascript:popWinBatch('../jsp/OutBoundMultiPickingBatch.jsp?ITEMNO=<%=item%>&INDEX=<%=tolnno%>&LOC_<%=tolnno%>='+form.FROM_WAREHOUSE.value+'&BATCH_<%=tolnno%>='+form.BATCH_<%=tolnno%>.value+'&BATCH_ID_<%=tolnno%>='+form.BATCH_ID_<%=tolnno%>.value+'&TYPE=TRBULK&UOM=<%=(String)uom%>')" >             	
				<i  class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></div>
             </td>
						<TD align="center" width="5%"><%=status%></TD>
											
					</TR>

					<%
						}
						} else {
					%>

					<TR>
						<TD align="center" colspan="9">Data's Not Found For Picking</TD>
					</TR>
					<%
						}
					%>
				
         </tbody>
       </TABLE>
       
        <INPUT	type="Hidden" name="RFLAG" value="8">
		<INPUT type="Hidden" name="isData" value="<%=isData%>">
		
		<div class="form-group">
    	<label class="control-label col-sm-2" for="Transaction Date">Transaction Date:</label>
        <div class="col-sm-2">
        <div class="input-group">          
        <INPUT class="form-control datepicker" name="TRANSACTIONDATE" id="TRANSACTIONDATE"	value="<%=TRANSACTIONDATE%>" type="TEXT" size="20" MAXLENGTH="80" readonly="readonly">
       </div>
    	</div>
 		<div class="form-inline">        
    	<label class="control-label col-sm-1" for="GINO">GINO:</label>
	    <div class="col-sm-3">
	    <div class="input-group">
        <INPUT class="form-control" name="GINO" id="GINO" type="TEXT" onkeypress="return blockSpecialChar(event)" value="<%=GINO%>" style="width: 100%" MAXLENGTH=100>
        <span class="input-group-addon"  onClick="javascript:onNew();return false;">
   		 	<a href="#" data-toggle="tooltip" data-placement="top" title="Auto-Generate">
   		 	<i class="glyphicon glyphicon-edit" style="font-size: 20px;"></i></a></span>
        </div>
	      </div>
    	</div>
    	<div class="form-inline">
    	<label class="control-label col-sm-1" for="Remarks">Remarks:</label>
        <div class="col-sm-2">
        <INPUT class="form-control" name="REF" type="TEXT" value="<%=REMARKS%>"	size="25" MAXLENGTH=100>
        </div>
 		</div>
 		</div>
        <div class="form-group">
        <label class="control-label col-sm-1" for="Location" hidden>Location:</label>
        <div class="col-sm-2" style="display: none;">
        <div class="input-group">
       <INPUT class="form-control" name="LOC_0" id="LOC_0" type="TEXT" size="30"  <% if(action.equalsIgnoreCase("CATCHERROR")){ %> value = '<%=item_loc%>' <%}else{ %>  value = '<%=toWareHouse%>'<%}%>
	   onkeypress="if((event.keyCode=='13') && ( this.value.length > 0)){validateLocation(this.value,0);}" MAXLENGTH=80>
        <span class="input-group-addon"  onClick="javascript:popUpWin('../jsp/loc_list_MultiReceivewms.jsp?INDEX=0&LOC='+form.LOC_0.value);">
   		 	<a href="#" data-toggle="tooltip" data-placement="top" title="Location Details">
   		 	<i  class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span>
        </div>		
        </div>	
	    	
    	<div class="form-inline">
    	
        <div class="col-sm-3">
                
        </div>
 		</div>			 			
 		</div>
        
  		<div class="form-group">        
      	<div class="col-sm-12" align="center">
      	<button type="button" class="Submit btn btn-default" onClick="window.location.href='../home'"><b>Back</b></button>&nbsp;&nbsp;
       	<!-- <button type="button" class="Submit btn btn-default" onClick="window.location.href='outboundTransaction.jsp'"><b>Back</b></button>&nbsp;&nbsp; -->
      	<button type="button" class="Submit btn btn-default" onClick="if(onIssue(document.form)) {submitForm();}"><b>Goods Issue/Receive</b></button>&nbsp;&nbsp;
      	<INPUT name="LOGIN_USER"	type="hidden" value="<%=sUserId%>">
      	</div>
        </div>
         		
  		</form>
		</div>
		</div>
		</div>



<jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
</jsp:include>