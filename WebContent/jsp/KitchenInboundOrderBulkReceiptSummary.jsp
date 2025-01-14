<%@ page import="com.track.dao.*"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.constants.*"%>
<%@ include file="header.jsp"%>
<%--New page design begin --%>
<%
String title = "Goods Receipt By Purchase Order ";
%>
<%! @SuppressWarnings({"rawtypes"}) %>
<%@include file="sessionCheck.jsp" %>
<jsp:include page="header2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
	<jsp:param name="mainmenu" value="<%=IConstants.PURCHASE%>"/>
	<jsp:param name="submenu" value="<%=IConstants.PURCHASE_TRANSACTION%>"/>
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
	}
</style>
<%--New page design end --%>

<%

POUtil _poUtil=new POUtil();
_poUtil.setmLogger(mLogger);
String action   = StrUtils.fString(request.getParameter("action")).trim();
String pono     = StrUtils.fString(request.getParameter("PONO"));
String sUserId = (String) session.getAttribute("LOGIN_USER");
String userId = (String) session.getAttribute("LOGIN_USER");
String RFLAG   =    (String) session.getAttribute("RFLAG");
String plant = StrUtils.fString((String)session.getAttribute("PLANT"));
PlantMstDAO plantMstDAO = new PlantMstDAO();
String SETCURRENTDATE_GOODSRECEIPT =  plantMstDAO.getSETCURRENTDATE_GOODSRECEIPT(plant);//Thanzith
ArrayList plntList = plantMstDAO.getPlantMstDetails(plant);
Map plntMap = (Map) plntList.get(0);
String PLNTDESC = (String) plntMap.get("PLNTDESC");
Map checkedPOS = (Map) request.getSession().getAttribute("checkedPOS");
String fieldDesc="<tr><td> Please enter any search criteria</td></tr>";
String jobNum = "",custName = "",chkString ="",item_loc= "",EXPIREDATE = "",REF = "",fullReceive = "",allChecked = "",allCheckedloc = "",checkNoBatch = "",ClearLoc = "",RECVDATE="",custcode="",email="";
com.track.dao.TblControlDAO _TblControlDAO=new   com.track.dao.TblControlDAO();
String grno = _TblControlDAO.getNextOrder(plant,userId,"GRN");

RECVDATE     = StrUtils.fString(request.getParameter("RECEVDATE"));
String curDate =DateUtils.getDate();
if(RECVDATE.length()<0|RECVDATE==null||RECVDATE.equalsIgnoreCase(""))RECVDATE=curDate;

String remark = "",collectionDate="",collectionTime="",remarks = "" ,isData = "";
Map mp= new HashMap();
if(action.equalsIgnoreCase("View")){
  
	Map m=(Map)request.getSession().getAttribute("isummaryvalue");
	mp =(Map)request.getSession().getAttribute("isummaryvalue");  //not by Draft - azees
      fieldDesc=StrUtils.fString((String)request.getSession().getAttribute("RESULTINBRECEIVE"));
      fieldDesc="<font class='maingreen'>"+fieldDesc+"</font>";

      if(m.size()>0){
    
      jobNum=StrUtils.fString((String)m.get("jobNum"));
      custName=(String)m.get("custName");
      remarks=(String)m.get("remarks");
      collectionDate=(String)m.get("collectionDate");
      collectionTime=(String)m.get("collectionTime");
      remark=StrUtils.fString((String)m.get("remark1"));
      custcode=(String)m.get("CustCode");
      email=(String)m.get("email");
      }
     
     
   }
 
   else if(action.equalsIgnoreCase("catcerror"))
     {

      fieldDesc=(String)request.getSession().getAttribute("CATCHERROR");
      fieldDesc="<font class='mainred'>"+fieldDesc+"</font>";
      jobNum=StrUtils.fString(request.getParameter("JOB_NUM"));
      custName=StrUtils.fString(request.getParameter("CUST_NAME"));
      remarks=StrUtils.fString(request.getParameter("REMARK2"));
      collectionDate=StrUtils.fString(request.getParameter("COLLECTION_DATE"));
      collectionTime=StrUtils.fString(request.getParameter("COLLECTION_TIME"));
      remark=StrUtils.fString(request.getParameter("REMARK"));
      item_loc = StrUtils.fString(request.getParameter("ITEM_LOC"));
      EXPIREDATE = StrUtils.fString(request.getParameter("EXPIREDATE"));
      RECVDATE = StrUtils.fString(request.getParameter("RECVDATE"));
      REF = StrUtils.fString(request.getParameter("REF"));
      allChecked = StrUtils.fString(request.getParameter("allChecked"));
      allCheckedloc = StrUtils.fString(request.getParameter("allCheckedloc"));
      fullReceive = StrUtils.fString(request.getParameter("fullReceive"));
      checkNoBatch = StrUtils.fString(request.getParameter("checkNoBatch"));
      ClearLoc = StrUtils.fString(request.getParameter("ClearLoc"));
     }
   
   request.getSession().setAttribute("RESULTINBRECEIVE","");
   request.getSession().setAttribute("podetVal","");
   request.getSession().setAttribute("CATCHERROR","");
   checkNoBatch="true";
 
%>
<!-- <html>
<head>
<title>Goods Receipt by Inbound Order</title>
<link rel="stylesheet" href="css/style.css">
<script language="JavaScript" type="text/javascript"
	src="js/jquery-1.4.2.js"></script> -->
<script type="text/javascript" src="../jsp/js/json2.js"></script>
<script type="text/javascript" src="../jsp/js/calendar.js"></script>
	<script type="text/javascript" src="../jsp/js/general.js"></script>
	<link rel="stylesheet" href="../jsp/css/typeahead.css">
<link rel="stylesheet" href="../jsp/css/accounting.css">
<script type="text/javascript">
var subWin = null;

function popUpWin(URL) {
 subWin = window.open(URL, 'InboundOrderSummary', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=800,height=400,left = 200,top = 184');
}
function onNew()
{
	
	var urlStr = "/track/InboundOrderHandlerServlet";
	$.ajax( {
		type : "POST",
		url : urlStr,
		data : {
		PLANT : "<%=plant%>",
		ACTION : "GRN"
		},
		dataType : "json",
		success : function(data) {
			if (data.status == "100") {
				var resultVal = data.result;				
				var resultV = resultVal.grno;
				document.form.GRNO.value= resultV;
	
			} else {
				alert("Unable to genarate GRN NO");
				document.form.GRNO.value = "";
			}
		}
	});	
	}
function viewInboundOrders(){
	document.form.action="/track/OrderReceivingServlet?action=ViewMultiple&type=BulkRecieve";
    document.form.submit();
}
function loadInbundOrderDetails(inboundOrderNo) {
// 	var inboundOrderNo = document.form.PONO.value;
	var urlStr = "/track/InboundOrderHandlerServlet";
	$.ajax( {
		type : "POST",
		url : urlStr,
		data : {
			ORDER_NO : inboundOrderNo,
			PLANT : "<%=plant%>",
			ACTION : "LOAD_INBOUND_ORDER_DETAILS"
			},
					dataType : "json",
					success : function(data) {
						if (data.status == "100") {
							var resultVal = data.result;
							document.form.JOB_NUM.value = resultVal.JOBNUM;
							document.form.CUST_NAME.value = resultVal.CUSTNAME;
							document.form.action = "/track/OrderReceivingServlet?action=ViewMultiple&type=BulkRecieve";
							document.form.submit();

						} else {
							alert("Not a valid Order Number!");
							document.form.PONO.value = "";
							document.form.JOB_NUM.value = "";
							document.form.CUST_NAME.value = "";
							document.form.PONO.focus();
						}
					}
				});
	}
function checkAll(isChk)
{
	var len = document.form.chkdPoNo.length;
	 var orderLNo; 
	 if(len == undefined) len = 1;  
    if (document.form.chkdPoNo)
    {
        for (var i = 0; i < len ; i++)
        {      
              	if(len == 1){
              		document.form.chkdPoNo.checked = isChk;
              		 orderLNo = document.form.chkdPoNo.value;
              	}
              	else{
              		document.form.chkdPoNo[i].checked = isChk;
              		 orderLNo = document.form.chkdPoNo[i].value;
              	}
            	setReceivingQty(orderLNo,i);       	
                
        }
    }
}
function checkNOBATCH(isChk)
{
	var len = document.form.chkdPoNo.length;
	 var orderLNo; 
	 if(len == undefined) len = 1;  
   if (document.form.chkdPoNo)
   {
       for (var i = 0; i < len ; i++)
       {      
             	if(len == 1 && document.form.chkdPoNo.checked){
             		chkstring = document.form.chkdPoNo.value;
             		chkdvalue = chkstring.split(',');
					orderLNo = chkdvalue[0];
					if(document.form.checkNoBatch.checked)
					{
						document.getElementById("BATCH_"+orderLNo).value = "NOBATCH";
					}
					else
	              		document.getElementById("BATCH_"+orderLNo).value = "";
             	}
             	else if(len == 1 && !document.form.chkdPoNo.checked){
             		return;
             	}
             	else{
                 	if(document.form.chkdPoNo[i].checked){
                 		chkstring = document.form.chkdPoNo[i].value;
                 		chkdvalue = chkstring.split(',');
	 					orderLNo = chkdvalue[0];
             		if(document.form.checkNoBatch.checked)
					{
             			document.getElementById("BATCH_"+orderLNo).value = "NOBATCH";
					}
             		else
	              		document.getElementById("BATCH_"+orderLNo).value = "";
                 	}
             	}
           	 
       }
   }
} 

function ClearAllLoc(isChk)
{
	var len = document.form.chkdPoNo.length;
	 var orderLNo; 
	 if(len == undefined) len = 1;  
  if (document.form.chkdPoNo)
  {
      for (var i = 0; i < len ; i++)
      {      
            	if(len == 1 && document.form.chkdPoNo.checked){
            		chkstring = document.form.chkdPoNo.value;
            		chkdvalue = chkstring.split(',');
					orderLNo = chkdvalue[0];
					if(document.form.ClearLoc.checked)
					{
					document.getElementById("LOC_"+orderLNo).value = "";
					}
					else
	              		document.getElementById("LOC_"+orderLNo).value = "";
            	}
            	else if(len == 1 && !document.form.chkdPoNo.checked){
            		return;
            	}
            	else{
                	if(document.form.chkdPoNo[i].checked){
                		chkstring = document.form.chkdPoNo[i].value;
                		chkdvalue = chkstring.split(',');
	 					orderLNo = chkdvalue[0];
            		if(document.form.ClearLoc.checked)
					{
            			document.getElementById("LOC_"+orderLNo).value = "";
					}
            		else
	              		document.getElementById("LOC_"+orderLNo).value = "";
                	}
            	}
          	 
      }
  }
} 

function fullReceiving(isChk)
{
	var len = document.form.chkdPoNo.length;
	 var orderLNo; 
	 if(len == undefined) len = 1;  
    if (document.form.chkdPoNo)
    {
        for (var i = 0; i < len ; i++)
        {      
              	if(len == 1 && document.form.chkdPoNo.checked){
              		orderLNo = document.form.chkdPoNo.value;
              		setReceivingQty(orderLNo,i); 
              	}
              	else if(len == 1 && !document.form.chkdPoNo.checked){
              		return;
              	}
              	else{
                  	if(document.form.chkdPoNo[i].checked){
              		 orderLNo = document.form.chkdPoNo[i].value;
              		 setReceivingQty(orderLNo,i); 
                  	}
              	}
            	      	
                
        }
    }
}
function generateBatch(index){
	var currentBatchLable = index;
	var urlStr = "/track/InboundOrderHandlerServlet";
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
				document.getElementById("BATCH_"+currentBatchLable).value = resultVal.batchCode;
	
			} else {
				alert("Unable to genarate Batch");
				document.getElementById("BATCH_"+currentBatchLable).value = "NOBATCH";
			}
		}
	});
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
function setReceivingQty(orderLNo,i){
	var len = document.form.chkdPoNo.length;
	if(len == undefined) len = 1; 
	if(len ==1 && document.form.chkdPoNo.checked){	  
		if(document.form.fullReceive.checked)
		{
			setQty(orderLNo);
		}
	  
	}
		
	else if(len !=1 && document.form.chkdPoNo[i].checked){
		if(document.form.fullReceive.checked)
		{
			setQty(orderLNo);
		}
		else{
			document.getElementById("receivingQty_"+orderLNo).value = 0;
		}
	}
	else{
		document.getElementById("receivingQty_"+orderLNo).value = 0;	
	}
	
	
	
}
function setQty(orderLNo){
	var QtyOrdered = document.getElementById("QtyOrdered_"+orderLNo).innerHTML;
	QtyOrdered = removeCommas(QtyOrdered);
	var QtyReceived = document.getElementById("QtyReceived_"+orderLNo).innerHTML;
	QtyReceived = removeCommas(QtyReceived);
	var QtyReceiving = QtyOrdered - QtyReceived;
	QtyReceiving = parseFloat(QtyReceiving).toFixed(3);
	document.getElementById("receivingQty_"+orderLNo).value = QtyReceiving;	
}


function checkAllLoc(isChk)
{
	var len = document.form.chkdPoNo.length;
	 var orderLNo; 
	 if(len == undefined) len = 1;  
if (document.form.chkdPoNo)
{
    for (var i = 0; i < len ; i++)
    {      
          	if(len == 1 && document.form.chkdPoNo.checked){
          		chkstring = document.form.chkdPoNo.value;
          		chkdvalue = chkstring.split(',');
					orderLNo = chkdvalue[0];
					document.getElementById("LOC_"+orderLNo).value = document.form.LOC.value;
          	}
          	else if(len == 1 && !document.form.chkdPoNo.checked){
          		return;
          	}
          	else{
              	if(document.form.chkdPoNo[i].checked){
              		chkstring = document.form.chkdPoNo[i].value;
              		chkdvalue = chkstring.split(',');
	 					orderLNo = chkdvalue[0];
              		document.getElementById("LOC_"+orderLNo).value = document.form.LOC.value;
              	}
          	}
        	 
    }
}
}

function onReceive(form){
	var pono=document.form.PONO.value;

	if(pono==null||pono=="")
	{
	alert("Please select Order Number");	
	return false;
	}    
    if(document.form.isData.value==null||document.form.isData.value=="" ||document.form.isData.value!="DATA")
    {
	   	alert("No Data's Found For Receiving");
	 	return false;
    }	
// 	var locId = document.form.LOC_0.value;
// 	 if(!validateLocation(locId,0)){
// 		 return false;
// 	 }
	 var checkFound = false;  
	 var orderLNo;
	 var len = document.form.chkdPoNo.length; 
	 if(len == undefined) len = 1;  
	 for (var i = 0; i < len ; i++)
     {
		if(len == 1 && (!document.form.chkdPoNo.checked))
		{
			checkFound = false;
		}
		
		else if(len ==1 && document.form.chkdPoNo.checked)
	     {
	    	 checkFound = true;
	    	 orderLNo = document.form.chkdPoNo.value;
	    	if(!verifyRecvQty(orderLNo))	
		    	  return false;
	    	if(!verifyLocQty(orderLNo))	
		    	  return false;
	     }
	
	     else {
		     if(document.form.chkdPoNo[i].checked){
		    	 checkFound = true;
		    	 orderLNo = document.form.chkdPoNo[i].value;
		    	 if(!verifyRecvQty(orderLNo))	
			    	  return false;
		    	 if(!verifyLocQty(orderLNo))	
			    	  return false;
		     }
	     }
           		
         	     
     }
	 if (checkFound != true) {
		    alert ("Please check at least one checkbox.");
		    return false;
		    }
	 var grnval = document.form.GRNO.value.length;
	 if(grnval>0)
	 {
		 /* var grncheck = VERIFYGRNO(document.form.GRNO.value);
		 if (checkFound != false) {
			 alert("ok");
		 }else{
			 alert("not ok")
			 
			 
		 } */
		 
		 document.form.action ="/track/OrderReceivingByPOServlet?action=BulkReceive";
		 document.form.submit(); 
	 }
	 else
		 {
		 if (confirm('Are you sure to submit this transaction without GRNO?')) {
	   document.form.action ="/track/OrderReceivingByPOServlet?action=BulkReceive";
	   document.form.submit();
		 } else {
			 document.getElementById("GRNO").focus();
		 }		 
		 }
}


function VERIFYGRNO(grno)
{
	var result = false;
	var urlStr = "/track/InboundOrderHandlerServlet";
	$.ajax( {
		type : "POST",
		url : urlStr,
		data : {
		PLANT : "<%=plant%>",
		ACTION : "VGRN",
		GRNO : grno
		},
		dataType : "json",
		success : function(data) {
			if (data.status == "100") {
				var resultVal = data.result;				
				var resultV = resultVal.grno;
				document.form.GRNO.value= resultV;
				result = true;
				return result;
			} else {
				alert("GRNO "+document.form.GRNO.value+" already converted as Bill. Please process with the new GRNO.");
				document.form.GRNO.value = "";
				result = false;
				return result;
			}
		}
	});	

	}

function onReceiveAndBill(form){
	var pono=document.form.PONO.value;

	if(pono==null||pono=="")
	{
	alert("Please select Order Number");	
	return false;
	}    
	var date=document.form.RECVDATE_0.value;

	if(date==null||date=="")
	{
	alert("Please select Transaction Date");	
	return false;
	}    
	
    if(document.form.isData.value==null||document.form.isData.value=="" ||document.form.isData.value!="DATA")
    {
	   	alert("No Data's Found For Receiving");
	 	return false;
    }	
// 	var locId = document.form.LOC_0.value;
// 	 if(!validateLocation(locId,0)){
// 		 return false;
// 	 }
	 var checkFound = false;  
	 var orderLNo;
	 var len = document.form.chkdPoNo.length; 
	 if(len == undefined) len = 1;  
	 for (var i = 0; i < len ; i++)
     {
		if(len == 1 && (!document.form.chkdPoNo.checked))
		{
			checkFound = false;
		}
		
		else if(len ==1 && document.form.chkdPoNo.checked)
	     {
	    	 checkFound = true;
	    	 orderLNo = document.form.chkdPoNo.value;
	    	if(!verifyRecvQty(orderLNo))	
		    	  return false;
	    	if(!verifyLocQty(orderLNo))	
		    	  return false;
	     }
	
	     else {
		     if(document.form.chkdPoNo[i].checked){
		    	 checkFound = true;
		    	 orderLNo = document.form.chkdPoNo[i].value;
		    	 if(!verifyRecvQty(orderLNo))	
			    	  return false;
		    	 if(!verifyLocQty(orderLNo))	
			    	  return false;
		     }
	     }
           		
         	     
     }
	 if (checkFound != true) {
		    alert ("Please check at least one checkbox.");
		    return false;
		    }
	 var grnval = document.form.GRNO.value.length;
	 if(grnval>0)
	 {
		/*  var grncheck = VERIFYGRNO(document.form.GRNO.value);
		 if (checkFound != false) {
			 alert("ok");
		 }else{
			 alert("not ok")
		 } */
		// document.form.action ="/track/OrderReceivingByPOServlet?action=BulkReceive&createBill=Y";
		// document.form.submit(); 
		 var urlStr = "/track/InboundOrderHandlerServlet";
			$.ajax( {
				type : "POST",
				url : urlStr,
				data : {
				PLANT : "<%=plant%>",
				ACTION : "VGRN",
				GRNO : document.form.GRNO.value
				},
				dataType : "json",
				success : function(data) {
					if (data.status == "100") {
						var resultVal = data.result;				
						var resultV = resultVal.grno;
						document.form.GRNO.value= resultV;
						//alert("ok");
						document.form.action ="/track/OrderReceivingByPOServlet?action=BulkReceive&createBill=Y";
						document.form.submit();
					} else {
						alert("GRNO "+document.form.GRNO.value+" already converted as Bill. Please process with the new GRNO.");
						document.form.GRNO.value = "";
					}
				}
			});	
	 }
	 else
		 {
		 	 alert ("Please Enter GRNO");
			 document.getElementById("GRNO").focus();		 		 
		 }
}

function onReceiveAndBillAndEmail(form){
	var pono=document.form.PONO.value;

	if(pono==null||pono=="")
	{
	alert("Please select Order Number");	
	return false;
	}    

	var date=document.form.RECVDATE_0.value;

	if(date==null||date=="")
	{
	alert("Please select Transaction Date");	
	return false;
	}
	
    if(document.form.isData.value==null||document.form.isData.value=="" ||document.form.isData.value!="DATA")
    {
	   	alert("No Data's Found For Receiving");
	 	return false;
    }	
// 	var locId = document.form.LOC_0.value;
// 	 if(!validateLocation(locId,0)){
// 		 return false;
// 	 }
	 var checkFound = false;  
	 var orderLNo;
	 var len = document.form.chkdPoNo.length; 
	 if(len == undefined) len = 1;  
	 for (var i = 0; i < len ; i++)
     {
		if(len == 1 && (!document.form.chkdPoNo.checked))
		{
			checkFound = false;
		}
		
		else if(len ==1 && document.form.chkdPoNo.checked)
	     {
	    	 checkFound = true;
	    	 orderLNo = document.form.chkdPoNo.value;
	    	if(!verifyRecvQty(orderLNo))	
		    	  return false;
	    	if(!verifyLocQty(orderLNo))	
		    	  return false;
	     }
	
	     else {
		     if(document.form.chkdPoNo[i].checked){
		    	 checkFound = true;
		    	 orderLNo = document.form.chkdPoNo[i].value;
		    	 if(!verifyRecvQty(orderLNo))	
			    	  return false;
		    	 if(!verifyLocQty(orderLNo))	
			    	  return false;
		     }
	     }
           		
         	     
     }
	 if (checkFound != true) {
		    alert ("Please check at least one checkbox.");
		    return false;
		    }
	 var grnval = document.form.GRNO.value.length;
	 if(grnval>0)
	 {
		/*  var grncheck = VERIFYGRNO(document.form.GRNO.value);
		 if (checkFound != false) {
			 alert("ok");
		 }else{
			 alert("not ok")
		 } */
		// document.form.action ="/track/OrderReceivingByPOServlet?action=BulkReceive&createBill=Y";
		// document.form.submit(); 
		 var urlStr = "/track/InboundOrderHandlerServlet";
			$.ajax( {
				type : "POST",
				url : urlStr,
				data : {
					PLANT : "<%=plant%>",
					ACTION : "VGRN",
					GRNO : document.form.GRNO.value
				},
				beforeSend: function(){
					showLoader();
				},
				error: function(){
					hideLoader();
				},
				dataType : "json",
				success : function(data) {
					if (data.status == "100") {
						var resultVal = data.result;				
						var resultV = resultVal.grno;
						document.form.GRNO.value= resultV;
						urlStr = "/track/OrderReceivingByPOServlet?action=BulkReceive&createBill=Y";
						var formData = $('#frmInboundOrderBulkReceipt').serialize();
						$.ajax( {
							type : "POST",
							url : urlStr,
							data : formData,
							complete: function(){
								hideLoader();
							},
							dataType : "json",
							success : function(data) {
								hideLoader();
								if (data.ERROR_CODE == "100") {
									$('.success-msg').html(data.MESSAGE).css('display', 'inline');
									$('#common_email_modal').modal('toggle');
									$('#send_to').val($('#supplier_email').val()).multiEmail();
									$('#send_subject').val($('#template_subject').val()
															.replace(/\{COMPANY_NAME\}/, $('#plant_desc').val())
															.replace(/\{ORDER_NO\}/, $('#PONO').val())
															.replace(/\{ORDER_NO_2\}/, $('#GRNO').val())
															);
									$('.wysihtml5-sandbox').contents().find('.wysihtml5-editor').html(
												$('#template_body').val()
												.replace(/\{ORDER_NO\}/, $('#PONO').val())
												.replace(/\{SUPPLIER_NAME\}/, $('#CUST_NAME').val())
												);
									$('#send_attachment').val('Goods Receipt');
								} else {
									$('.success-msg').html(data.MESSAGE).addClass('error-msg').removeClass('success-msg').css('display', 'inline');
								}
							}
						});	
					} else {
						hideLoader();
						alert("GRNO "+document.form.GRNO.value+" already converted as Bill. Please process with the new GRNO.");
						document.form.GRNO.value = "";
					}
				}
			});	
	 }
	 else
		 {
		 	 alert ("Please Enter GRNO");
			 document.getElementById("GRNO").focus();		 		 
		 }
}

function verifyLocQty(orderLNo)
{

		var recvQty = document.getElementById("LOC_"+(orderLNo)).value;
		if (recvQty == "" || recvQty.length == 0 || recvQty == '0') {
			alert("Enter a Location!");
			document.getElementById("LOC_"+(orderLNo)).focus();
			document.getElementById("LOC_"+(orderLNo)).select();
	        return false;
		}
	return true;
}

function verifyRecvQty(orderLNo)
{

		var recvQty = document.getElementById("receivingQty_"+(orderLNo)).value;
		if (recvQty == "" || recvQty.length == 0 || recvQty == '0') {
			alert("Enter a valid Quantity!");
			document.getElementById("receivingQty_"+(orderLNo)).focus();
			document.getElementById("receivingQty_"+(orderLNo)).select();
	        return false;
		}
		if(!isNumericInput(recvQty)){
			alert("Entered Quantity is not a valid number!");
			document.getElementById("receivingQty_"+(orderLNo)).focus();
			document.getElementById("receivingQty_"+(orderLNo)).select();
	         return false;
		}
		recvQty = parseFloat(recvQty).toFixed(3);
		var orderedQty = document.getElementById("QtyOrdered_"+(orderLNo)).innerHTML;
		orderedQty = removeCommas(orderedQty);
		orderedQty = parseFloat(orderedQty).toFixed(3);
		var recvdQty = document.getElementById("QtyReceived_"+(orderLNo)).innerHTML;
		recvdQty = removeCommas(recvdQty);
		recvdQty = parseFloat(recvdQty).toFixed(3);
		var balanceQty =  orderedQty - recvdQty;
		balanceQty = parseFloat(balanceQty).toFixed(3);
		if ((Math.round(parseFloat(recvQty)*100)/100) > (Math.round(parseFloat(balanceQty)*100)/100))
		//if (parseFloat(recvQty.toFixed(3)) > parseFloat(balanceQty.toFixed(3)))	
		//if(recvQty > balanceQty)
	{
		alert("Exceeded the Ordered Qty of LineOrderNO:: "+orderLNo);
		document.getElementById("receivingQty_"+(orderLNo)).focus();
		document.getElementById("receivingQty_"+(orderLNo)).select();
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
	for (var i = 0; i < strString.length && blnResult == true; i++) {
		strChar = strString.charAt(i);
		if (strValidChars.indexOf(strChar) == -1) {
			blnResult = false;
		}
	}
	return blnResult;
}
function generateEmail(attachName)
{
	var img = toDataURL($(".dash-logo").attr("src"),
			function(dataUrl) {
				generatePdfMail(dataUrl,attachName);
		  	},'image/jpeg');
	}
function generatePdfMail(dataUrl,attachName){	
	formData = new FormData();
	formData.append("GRNO_PONO", $("#PONO").val());
	progressBar();
	sendMailTemplate(formData);
	  //return formData;
	  
}
function toDataURL(src, callback, outputFormat) {
	  var img = new Image();
	  img.crossOrigin = 'Anonymous';
	  img.onload = function() {
	    var canvas = document.createElement('CANVAS');
	    var ctx = canvas.getContext('2d');
	    var dataURL;
	    canvas.height = this.naturalHeight;
	    canvas.width = this.naturalWidth;
	    ctx.drawImage(this, 0, 0);
	    dataURL = canvas.toDataURL(outputFormat);
	    callback(dataURL);
	  };
	  img.src = src;
	  if (img.complete || img.complete === undefined) {
	    img.src = "data:image/gif;base64,R0lGODlhAQABAIAAAAAAAP///ywAAAAAAQABAAACAUwAOw==";
	    img.src = src;
	  }
}
</script>

<%--New page design begin --%>
<center>
	<h2><small class="success-msg"> <%=fieldDesc%></small></h2>
</center>
<div class="container-fluid m-t-20">
	 <div class="box"> 
	 <!-- Muruganantham Modified on 16.02.2022 -->
             <ol class="breadcrumb backpageul" >      	
                  <li><a href="../home"><span class="underline-on-hover">Dashboard</span> </a></li>    
                  <li><a href="../purchaseTransactionDashboard"><span class="underline-on-hover">Purchase Transaction Dashboard</span></a></li>                   
                  <li>Goods Receipt By Purchase Order</l>                                    
             </ol>   
     <!-- Muruganantham Modified on 16.02.2022 --> 
            <div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title"><%=title %></h1>
                      <h1 style="font-size: 18px; cursor: pointer;" class="box-title pull-right" onclick="window.location.href='../purchaseTransactionDashboard'">
			  <i class="glyphicon glyphicon-remove"></i>
		 	  </h1>
		</div>
 	<div class="box-body">
	<%--New page design end --%>
  <form class="form-horizontal" name="form" id="frmInboundOrderBulkReceipt" method="post"  action="/track/OrderReceivingByPOServlet?action=BulkReceive">
      
      
      <div class="form-group">
      <label class="control-label col-sm-4" for="Inbound Order">Order Number:</label>
      <div class="col-sm-4">          
      <div class="input-group">
      <INPUT class="form-control" type="TEXT" size="30" MAXLENGTH=20 name="PONO" id="PONO"	value="<%=pono%>"> 
      							 <span class="select-icon" onclick="$(this).parent().find('input[name=\'PONO\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
<!-- 	  onkeypress="if((event.keyCode=='13') && ( document.form.PONO.value.length > 0)){loadInbundOrderDetails();}"> -->
	  
	  
	  
	  <!-- <span class="input-group-addon" onClick="javascript:popUpWin('../jsp/po_list_po.jsp?PONO='+form.PONO.value+'&OpenForReceipt=yes');return false;"> 	
   	  <a href="#" data-toggle="tooltip" data-placement="top" title="Purchase Details">
   	  <i class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span> -->
   	  
   	  
      </div>
      </div>
      <div class="form-inline">
      <div class="col-sm-1">          
      <button type="button" class="Submit btn btn-default" name="actionButton" onClick="viewInboundOrders();"><b>View</b></button>
      </div>
      </div>
      </div>
      
      <div class="form-group">
      <label class="control-label col-sm-4" for="Supplier Name">Supplier Name:</label>
      <div class="col-sm-4">          
      <INPUT class="form-control" name="CUST_NAME" id="CUST_NAME" type="TEXT" value="<%=StrUtils.forHTMLTag(custName)%>" size="30" MAXLENGTH=80>
      </div>
      </div>
      
      <INPUT type="hidden" name="CUST_CODE" value="<%=custcode%>"> 
      <INPUT type="hidden" name="CUST_CODE1" value=""> 
      <INPUT type="hidden" name="LOGIN_USER" value="<%=sUserId%>">
      <INPUT type="hidden" name="supplier_email" id="supplier_email" value="<%=email%>">
      
      <div class="form-group">
      <label class="control-label col-sm-4" for="Reference No">Reference No:</label>
      <div class="col-sm-4">          
      <INPUT type="TEXT" size="30" MAXLENGTH=20	name="JOB_NUM" class="form-control" readonly value="<%=jobNum%>">
	  </div>
      <!-- <div class="form-inline">
      <label class="control-label col-sm-3" for="Supplier Remarks">Supplier Remarks:</label>
      <div class="col-sm-3"> -->          
      <INPUT class="form-control" name="CUST_NAME" type="hidden" value="<%=StrUtils.forHTMLTag(custName)%>" size="30" MAXLENGTH=80>
      <!-- </div>
      </div> -->
      </div>
      
      <div class="form-group">
      <label class="control-label col-sm-4" for="Order Date">Order Date:</label>
      <div class="col-sm-4">          
      <INPUT type="TEXT" size="30" MAXLENGTH="10" name="COLLECTION_DATE" class="form-control" readonly value="<%=collectionDate%>" />
	  </div>
      </div>
      
      <div class="form-group">
      <label class="control-label col-sm-4" for="Time">Order Time:</label>
      <div class="col-sm-4">          
      <INPUT type="TEXT" size="30" class="form-control" readonly MAXLENGTH="20" name="COLLECTION_TIME" value="<%=collectionTime%>" />
	  </div>
      </div>
      
      <div class="form-group">
      <label class="control-label col-sm-4" for="Remarks">Remarks:</label>
      <div class="col-sm-4">          
      <INPUT type="TEXT" size="25" MAXLENGTH="20" name="REMARK" class="form-control" readonly value="<%=remark%>" />
	  </div>
      </div>
      
      <div class="form-group">
      <div class="col-sm-12">    
      <label class="checkbox-inline">      
      <INPUT Type=Checkbox  style="border:0;" name = "select" value="select" <%if(allChecked.equalsIgnoreCase("true")){%>checked <%}%>onclick="return checkAll(this.checked);">
	  <b>Select/Unselect All</b></label>
	  <label class="checkbox-inline"> 
	  <INPUT Type=Checkbox  style="border:0;" name="fullReceive"  <%if(fullReceive.equalsIgnoreCase("true")){%>checked <%}%> value="fullReceive" onclick="return fullReceiving(this.checked)">
      <b>Full Receiving</b> </label>
       <label class="checkbox-inline">      
      <INPUT Type=Checkbox  style="border:0;" name = "checkNoBatch" value="checkNoBatch" <%if(checkNoBatch.equalsIgnoreCase("true")){%>checked <%}%>onclick="return checkNOBATCH(this.checked);">
	  <b>Default NOBATCH</b></label>
	   <label class="checkbox-inline">      
      <INPUT Type=Checkbox  style="border:0;" name = "ClearLoc" value="ClearLoc" <%if(ClearLoc.equalsIgnoreCase("true")){%>checked <%}%>onclick="return ClearAllLoc(this.checked);">
	  <b>Clear Location</b></label>
		<label class="text-inline">
      <div class="col-sm-7">
		<input type="text" class="ac-selected  form-control typeahead locationSearch" id="LOC" placeholder="Select a Location" name="LOC" value="">
			<span class="select-icon" onclick="$(this).parent().find('input[name=\'LOC\']').focus()">
				<i class="glyphicon glyphicon-menu-down"></i>
	  		</div>
     	 <label class="checkbox-inline">      
      <INPUT Type=Checkbox  style="border:0;" name = "selectloc" value="selectloc" <%if(allCheckedloc.equalsIgnoreCase("true")){%>checked <%}%>onclick="return checkAllLoc(this.checked);">
	  <b>Apply to all Select</b></label>
	 </div>
     </div>
      
      	
	  
	  <TABLE class="table table-bordred table-striped"  align="center">
	  <thead>
	  <tr>
      <th align = "center" width="5%">SELECT </th>
	  <th align = "center" width="5%">ORDERLNO </th>
	  <th align = "center" 	width="8%">PRODUCT ID </th>
	  <th align = "center" width="15%">DESCRIPTION </th>
	  <th align = "center" width="5%">UOM </th>
	  <th align = "center" width="6%">ORDER QUANTITY</th>
	  <th align = "center" width="6%">RECEIVED QUANTITY</th>
	  <th align = "center" width="6%">RECEIVING QUANTITY</th>
	  <th align = "center" width="6%">LOCATION</th>
	  <th align = "center" width="10%">BATCH NO</th>
	  <th align = "center" width="10%">EXPIRY DATE</th>
	  <th align = "center" width="5%">STATUS </th>
	  
	  </tr>
      </thead>
	  <tbody style="font-size: 15px;">
	  					<% 
	  					if(mp.size()>0){
       ArrayList al= _poUtil.listInboundSummaryPODET(pono,plant);
       
       if(al.size()==0)
       {
    	   isData="";
       }
      
       if(al.size()>0)
       {
        ItemMstDAO _ItemMstDAO= new ItemMstDAO();
        InvMstDAO _InvMstDAO= new InvMstDAO();
        _ItemMstDAO.setmLogger(mLogger);
        _InvMstDAO.setmLogger(mLogger);
        isData="DATA";
        String recvingQty = "",value = null,batchNo = "NOBATCH";
       for(int i=0 ; i<al.size();i++)
       {
    	   recvingQty = "";
    	   batchNo = "NOBATCH";
    	  Map m=(Map)al.get(i);
          int iIndex = i + 1;
          String bgcolor = ((i == 0) || (i % 2 == 0)) ? "#FFFFFF"  : "#FFFFFF";
          String strpono = (String)m.get("pono");
          String polnno = (String)m.get("polnno");
          String item= (String)m.get("item");
          String desc= (String)m.get("ItemDesc");//_ItemMstDAO.getItemDesc(plant ,item);
          String loc= (String)m.get("location");
         
          String qtyor= StrUtils.formatNum((String)m.get("qtyor"));
          String qtyrc= StrUtils.formatNum((String)m.get("qtyrc"));
          String lnstat= (String)m.get("lnstat");
          String Uom= (String)m.get("uom");
          if(checkedPOS!=null && action.equalsIgnoreCase("catcerror")){
          value = (String)checkedPOS.get(polnno);   
          
          if(value!=null)
          {
        	  recvingQty  = value.split(":")[0];
        	  batchNo  = value.split(":")[1];
        	 
          }
          }
       
                   
      %>
					<TR bgcolor="<%=bgcolor%>">

						<TD align = "left" width="5%" align="CENTER"><font color="black"><INPUT
							Type=checkbox style="border: 0;" name="chkdPoNo" <%if(value!=null){%>checked <%}%>
							value="<%=polnno%>" onclick="setReceivingQty(<%=polnno%>,<%=i%>);"></font></TD>
			            <TD align = "center" width="5%" align="center"><font color="black"><%=polnno%></font></TD>

						<TD align = "left" align="center" width="8%"><%=item%></TD>
						<TD align="left" width="15%"><%=desc%></TD>
						<TD align="left" width="5%"><%=Uom%></TD>
						<TD id = "QtyOrdered_<%=polnno%>" align="center" width="6%"><%=qtyor%></TD>
						<TD id = "QtyReceived_<%=polnno%>" align="center" width="6%"><%=qtyrc%></TD>
						<TD align="center" width="6%">
						<input class="form-control" type="text" name = "receivingQty_<%=polnno%>" id = "receivingQty_<%=polnno%>"  size = "6" maxlength = "10" value = <%=recvingQty%>>
						</TD>
	<!-- location -->	<TD align="center" width="15%"> 
						<div class="input-group">
						<input class="form-control" type="text" name = "LOC_<%=polnno%>" id = "LOC_<%=polnno%>"  size = "6" maxlength = "50" value = "<%=loc%>"
					  	onkeypress="if((event.keyCode=='13') && ( this.value.length > 0)){validateLocation(this.value,0);}" MAXLENGTH=80>
					  	<span class="input-group-addon" onClick="javascript:popUpWin('../jsp/loc_list_MultiReceivewms.jsp?INDEX=<%=polnno%>&LOC_<%=polnno%>='+form.LOC_<%=polnno%>.value);return false;"> 	
				   	  	<a href="#" data-toggle="tooltip" data-placement="top" title="Location Details">
				   	  	<i class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span></div>
						</TD>
						<td width="15%"><div class="input-group"><INPUT class="form-control" name="BATCH_<%=polnno%>" id="BATCH_<%=polnno%>" value = <%=batchNo%> type="TEXT" size="10"   MAXLENGTH=40>
						<span class="input-group-addon" 	onclick="javascript:generateBatch(<%=polnno%>);return false;" 	name="actionBatch">
						<a href="#" data-toggle="tooltip" data-placement="top" title="Generate">
						<i class="glyphicon glyphicon-edit"></i></a></span></div></td>
<!-- expiry date -->	<TD align="center" width="6%">
 						<INPUT class="form-control datepicker" name="EXPIREDATE_<%=polnno%>" id="EXPIREDATE_<%=polnno%>" value="<%= EXPIREDATE%>" type="TEXT" size="30" MAXLENGTH="80" readonly="readonly">
<%-- 						<input class="form-control" type="text" name = "receivingQtya_<%=polnno%>" id = "receivingQtya_<%=polnno%>"  size = "6" maxlength = "10" value = <%=recvingQty%>> --%>
						</TD>
						<TD align="left" width="5%"><%=lnstat%></TD>
						
					</TR>
					<%}} else 
    	   {%>
					<TR>
						<td colspan="12" align="center">Data's Not Found</TD>
					</TR>
					<%} }%>
		</tbody>
		</table>
		
		<INPUT type="Hidden" name="ENCRYPT_FLAG" value="1"> 
		<INPUT type="Hidden" name="RFLAG" value="1"> 
		<INPUT type="Hidden" name="isData" value=<%=isData%>>

	  <div class="form-group">
	  <label class="control-label col-sm-2 required" for="Transaction Date">Transaction Date:</label>
      <div class="col-sm-3">
      <div class="input-group"> 
      <%if(SETCURRENTDATE_GOODSRECEIPT.equals("1")){%>         
    <INPUT class="form-control datepicker" name="RECVDATE_0" id="RECVDATE_0"	value="<%= RECVDATE%>" type="TEXT" size="30" MAXLENGTH="80" readonly="readonly"> 
    <%}else {%>
      <INPUT class="form-control datepicker" name="RECVDATE_0" id="RECVDATE_0"	value="" type="TEXT" size="30" MAXLENGTH="80" readonly="readonly">
    <%}%>
      </div>
      </div>
<!--       <label class="control-label col-sm-2" for="Location">Location:</label> -->
<!--       <div class="col-sm-3">           -->
<!--       <div class="input-group"> -->
<%--       <INPUT class="form-control" name="LOC_0" id="LOC_0" type="TEXT" size="30" value = '<%=item_loc%>' --%>
<!-- 	  onkeypress="if((event.keyCode=='13') && ( this.value.length > 0)){validateLocation(this.value,0);}" MAXLENGTH=80> -->
<!-- 	  <span class="input-group-addon" onClick="javascript:popUpWin('../jsp/loc_list_MultiReceivewms.jsp?INDEX=0&LOC='+form.LOC_0.value);return false;"> 	 -->
<!--    	  <a href="#" data-toggle="tooltip" data-placement="top" title="Location Details"> -->
<!--    	  <i class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span> -->
<!--       </div> -->
<!--       </div> -->
      <div class="form-inline">
      <label class="control-label col-sm-2" for="Remarks">Remarks:</label>
      <div class="col-sm-3">          
      <INPUT class="form-control" name="REF" type="TEXT" value="<%=REF%>" style="width: 100%" MAXLENGTH=100>
      </div>
      </div>
      </div>
      
<!--       <div class="form-group"> -->
<!--       <label class="control-label col-sm-2" for="Transaction Date">Transaction Date:</label> -->
<!--       <div class="col-sm-3"> -->
<!--       <div class="input-group">           -->
<%--       <INPUT class="form-control datepicker" name="RECVDATE_0" id="RECVDATE_0"	value="<%= RECVDATE%>" type="TEXT" size="30" MAXLENGTH="80" readonly="readonly"> --%>
<!--       </div> -->
<!--       </div> -->
<!--       <div class="form-inline"> -->
<!--       <label class="control-label col-sm-2" for="Expiry Date">Expiry Date:</label> -->
<!--       <div class="col-sm-3"> -->
<!--       <div class="input-group">           -->
<%--       <INPUT class="form-control datepicker" name="EXPIREDATE_0" id="EXPIREDATE_0" value="<%= EXPIREDATE%>" type="TEXT" size="30" MAXLENGTH="80" readonly="readonly"> --%>
<!--       </div> -->
<!--       </div>	 -->
<!--       </div> -->
<!--       </div> -->

	<div class="form-group">
	<label class="control-label col-sm-2" for="Transport">Transport:</label>
        <div class="col-sm-3 ac-box">
        <input type="hidden" name="TRANSPORTID" value="">
			<input type="text" class="ac-selected  form-control typeahead" id="transport" placeholder="Select a transport" name="transport" onchange="checktransport(this.value)" value="">
			<span class="select-icon" onclick="$(this).parent().find('input[name=\'transport\']').focus()">
				<i class="glyphicon glyphicon-menu-down"></i>
			</span>
   		 </div> 
<!--    		       <div class="form-group"> -->
	<label class="control-label col-sm-2" for="Clearing Agent">Clearing Agent:</label>
      <!-- <label class="control-label col-sm-2" for="Clearance type">Type of Clearance:</label> -->
      <div class="col-sm-3 ac-box"> 
      <input type="hidden" id="typeofclearance" name="typeofclearance" value="" />         
	  <input type="text" class="ac-selected  form-control typeahead clearingagent" id="clearingagent" placeholder="Select a clearing agent" name="clearingagent" onchange="checkclearingagent(this.value)" value="">
      <!-- <input type="text" class="ac-selected  form-control typeahead typeofclearance" id="typeofclearance" placeholder="Select a type of clearance" name="typeofclearance" onchange="checktypeofclearance(this.value)" value=""> -->
		<span class="select-icon" onclick="$(this).parent().find('input[name=\'clearingagent\']').focus()">
			<i class="glyphicon glyphicon-menu-down"  style="font-size: 8px;"></i>
		</span>
<!--       </div> -->
      </div>       
      </div>
      
      
             <div class="form-group">
        <label class="control-label col-sm-2" for="Contact Name">Contact Name:</label>
      <div class="col-sm-3">
      <div class="input-group">          
      <INPUT class="form-control" name="CONTACT_NAME" id="CONTACT_NAME" value="" type="TEXT" readonly="readonly">
      </div>
      </div>
      <div class="form-inline">
      <label class="control-label col-sm-2" for="GRNO">GRNO:</label>
        <div class="col-sm-3">
		<div class="input-group">
        <input name="GRNO" id="GRNO" value="<%= grno %>" class="form-control" type="TEXT" style="width: 100%" MAXLENGTH=80 />
        <span class="input-group-addon" value="<%= grno %>" onClick="javascript:onNew();return false;">
   		 	<a href="#" data-toggle="tooltip" data-placement="top" title="Auto-Generate">
   		 	<i class="glyphicon glyphicon-edit" style="font-size: 20px;"></i></a></span>
   		 	</div>        
        </div>
        </div>
      </div>
      
<!--       <div class="form-group"> -->
<!--       <label class="control-label col-sm-2" for="GRNO">GRNO:</label> -->
<!--         <div class="col-sm-3"> -->
<!-- 		<div class="input-group"> -->
<%--         <input name="GRNO" id="GRNO" value="<%= grno %>" class="form-control" type="TEXT" style="width: 100%" MAXLENGTH=80 /> --%>
<%--         <span class="input-group-addon" value="<%= grno %>" onClick="javascript:onNew();return false;"> --%>
<!--    		 	<a href="#" data-toggle="tooltip" data-placement="top" title="Auto-Generate"> -->
<!--    		 	<i class="glyphicon glyphicon-edit" style="font-size: 20px;"></i></a></span> -->
<!--    		 	</div>         -->
<!--         </div> -->
<!--       </div> -->
      
     

      <div class="form-group">        
      	<div class="col-sm-12" align="left">
      <!-- <button type="button" class="Submit btn btn-default" onClick="window.location.href='inboundTransaction.jsp'"><b>Back</b></button>&nbsp;&nbsp; -->
      <!-- <button type="button" class="Submit btn btn-default" onClick="onReceive(document.form);"><b>Receive</b></button>&nbsp;&nbsp; -->
      <div class="dropup">
		<%--https://www.w3schools.com/bootstrap/tryit.asp?filename=trybs_dropdown-menu-dropup&stacked=h --%>
	    <button class="btn btn-success dropdown-toggle" type="button" data-toggle="dropdown">Receive
	    <span class="caret"></span></button>
	    <ul class="dropdown-menu">
	      <li><a id="btnRedeive" href="#" onclick="onReceiveAndBill(document.form);">Receive</a></li>
	      <li><a id="btnReceiveEmail" href="#" onclick="onReceiveAndBillAndEmail(document.form);">Receive and Send Email</a></li>
	    </ul>
	    <button type="button" class="Submit btn btn-default" onClick="window.location.href='../home'"><b>Back</b></button>
	  </div>
      </div>
      </div>
      </form>
      </div>
      </div>
      </div>
<script>

$(document).ready(function(){
	$('[data-toggle="tooltip"]').tooltip(); 


	 /* TRANID Auto Suggestion */
	$('#PONO').typeahead({
		  hint: true,
		  minLength:0,  
		  searchOnFocus: true
		},
		{
		  display: 'PONO',  
		  source: function (query, process,asyncProcess) {
			var pono = document.form.PONO.value;
			var urlStr = "/track/ItemMstServlet";
			$.ajax( {
			type : "POST",
			url : urlStr,
			async : true,
			data : {
				PLANT : "<%=plant%>",
				ACTION : "GET_PORECEIPT_LIST_FOR_SUGGESTION",
				PONO : pono,
				OpenForReceipt : "YES",
				QUERY : query
			},
			dataType : "json",
			success : function(data) {
				return asyncProcess(data.PORECEIPT);
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
			return '<div onclick="loadInbundOrderDetails(\''+data.PONO+'\')"><p class="item-suggestion">'+data.PONO+'</p><p class="item-suggestion pull-right">'+data.DATE+'</p><br/><p class="item-suggestion">Supplier :'+data.CUST+'</p></div>';
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
		}).on('typeahead:change',function(event,selection){
 			if($(this).val() == ""){
 				document.form.PONO.value = "";
 				document.getElementById("PONO").value = "";
 			}
	});
	
	/* clearagent Auto Suggestion by Thansith*/
	$('.clearingagent').typeahead({
		  hint: true,
		  minLength:0,  
		  searchOnFocus: true
		},
		{
		  display: 'clearing_agent_id',  
// 		  display: 'clearing_agent_name',  
		  source: function (query, process,asyncProcess) {
			var urlStr = "/track/MasterServlet";
			$.ajax( {
			type : "POST",
			url : urlStr,
			async : true,
			data : {
				PLANT : '<%=plant%>',
				ACTION : "GET_TRANSPORT_CLEARING_AGENT",
				TRANSPORTID : $("input[name=TRANSPORTID]").val(),
				CLEARAGENTID : query
			},
			dataType : "json",
			success : function(data) {
				return asyncProcess(data.DESIGNATIONLIST);
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
// 			return '<div onclick="document.form.CLEARAGENTID.value = \''+data.clearing_agent_id+'\'" ><p class="item-suggestion">'+ data.clearing_agent_id +'</p><p class="item-suggestion">'+data.clearing_agent_name+'</p></div>';
			return '<div onclick="document.form.CONTACT_NAME.value = \''+data.CONTACTNAME+'\'"><p class="item-suggestion">'+ data.clearing_agent_id + '</p><p class="item-suggestion pull-right">' + data.TRANSPORT + '</p><br/><p class="item-suggestion">' + data.clearing_agent_name + '</p><p class="item-suggestion pull-right">' + data.CONTACTNAME + '</p></div>';
	}
	}
		}).on('typeahead:render',function(event,selection){
			var menuElement = $(this).parent().find(".tt-menu");
			var top = menuElement.height()+35;
			top+="px";
			if(menuElement.next().hasClass("footer")){
				menuElement.next().remove();
			}
// 			if(newclearagent == 'true'){
			menuElement.after( '<div class="clearingagentAddBtn footer"  data-toggle="modal" data-target="#clearingagentModal"><a href="#"> + New Clearaing Agent </a></div>');
// 			}
			menuElement.next().width(menuElement.width());
			menuElement.next().css({ "top": top,"padding":"3px 20px" });
			if($(this).parent().find(".tt-menu").css('display') != "block")
				menuElement.next().hide(); 
		}).on('typeahead:open',function(event,selection){
			$('.clearingagentAddBtn').show();
			var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
			element.toggleClass("glyphicon-menu-up",true);
			element.toggleClass("glyphicon-menu-down",false);    
		}).on('typeahead:close',function(){
			setTimeout(function(){ $('.clearingagentAddBtn').hide();}, 180);	
			var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
			element.toggleClass("glyphicon-menu-up",false);
			element.toggleClass("glyphicon-menu-down",true);
 		}).on('typeahead:change',function(event,selection){
 			if($(this).val() == ""){
// 				document.form.CLEARAGENTID.value = "";
 				document.form.CONTACT_NAME.value = "";
 			}
	});

	/* clearance type Auto Suggestion by vicky*/
	$('.typeofclearance').typeahead({
		  hint: true,
		  minLength:0,  
		  searchOnFocus: true
		},
		{
		  display: 'CLEARANCETYPE',  
		  source: function (query, process,asyncProcess) {
			var urlStr = "/track/MasterServlet";
			$.ajax( {
			type : "POST",
			url : urlStr,
			async : true,
			data : {
				PLANT : '<%=plant%>',
				ACTION : "GET_CLEARANCETYPE_FOR_SUMMARY",
				CLEARANCETYPE : query
			},
			dataType : "json",
			success : function(data) {
				return asyncProcess(data.CLEARANCETYPELIST);
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
	return '<div><p class="item-suggestion">'+ data.CLEARANCETYPE +'</p></div>';
	}
	}
	}).on('typeahead:render',function(event,selection){
	var menuElement = $(this).parent().find(".tt-menu");
	var top = menuElement.height()+35;
	top+="px";
	if(menuElement.next().hasClass("footer")){
		menuElement.next().remove();
	}
// 	if(newclearancetype == 'true'){
	menuElement.after( '<div class="clearanceTypeAddBtn footer"  data-toggle="modal" data-target="#clearanceTypeModal"><a href="#"> + New CLEARANCE TYPE</a></div>');
// 	}
	menuElement.next().width(menuElement.width());
	menuElement.next().css({ "top": top,"padding":"3px 20px" });
	if($(this).parent().find(".tt-menu").css('display') != "block")
		menuElement.next().hide(); 
}).on('typeahead:open',function(event,selection){
	$('.clearanceTypeAddBtn').show();
	var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
	element.toggleClass("glyphicon-menu-up",true);
	element.toggleClass("glyphicon-menu-down",false);    
}).on('typeahead:close',function(){
	setTimeout(function(){ $('.clearanceTypeAddBtn').hide();}, 180);	
	var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
	element.toggleClass("glyphicon-menu-up",false);
	element.toggleClass("glyphicon-menu-down",true);
	});

	//transport
	$('#transport').typeahead({
		  hint: true,
		  minLength:0,  
		  searchOnFocus: true
		},
		{
		  display: 'TRANSPORT_MODE',  
		  source: function (query, process,asyncProcess) {
			  	var urlStr = "../MasterServlet";
				$.ajax({
					type : "POST",
					url : urlStr,
					async : true,
					data : {				
						ACTION : "GET_TRANSPORT_LIST",
						QUERY : query
					},
					dataType : "json",
					success : function(data) {
						return asyncProcess(data.TRANSPORTMODE);
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
		    return '<p>' + data.TRANSPORT_MODE + '</p>';
			}
		  }
		}).on('typeahead:render',function(event,selection){
			var menuElement = $(this).parent().find(".tt-menu");
			var top = menuElement.height()+35;
			top+="px";	
			if(menuElement.next().hasClass("footer")){
				menuElement.next().remove();  
			}
			menuElement.after( '<div class="transportAddBtn footer"  data-toggle="modal" data-target="#transportModal"><a href="#"> + New Transport </a></div>');
			menuElement.next().width(menuElement.width());
			menuElement.next().css({ "top": top,"padding":"3px 20px" });
			if($(this).parent().find(".tt-menu").css('display') != "block")
				menuElement.next().hide();
		  
		}).on('typeahead:open',function(event,selection){
			$('.transportAddBtn').show();
			var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
			element.toggleClass("glyphicon-menu-up",true);
			element.toggleClass("glyphicon-menu-down",false);    
		}).on('typeahead:close',function(){
			setTimeout(function(){ $('.transportAddBtn').hide();}, 180);	
			var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
			element.toggleClass("glyphicon-menu-up",false);
			element.toggleClass("glyphicon-menu-down",true);
		}).on('typeahead:select',function(event,selection){
			$("input[name=TRANSPORTID]").val(selection.ID);
			$("#clearingagent").typeahead('val', '"');
			$("#clearingagent").typeahead('val', '');
			document.form.CONTACT_NAME.value = "";
		}).on('typeahead:change',function(event,selection){
 			if($(this).val() == ""){
			$("input[name=TRANSPORTID]").val('');
			$("#clearingagent").typeahead('val', '"');
			$("#clearingagent").typeahead('val', '');
			document.form.CONTACT_NAME.value = "";
 			}	
		});

	/* location Auto Suggestion */
	$('.locationSearch').typeahead({
		  hint: true,
		  minLength:0,  
		  searchOnFocus: true
		},
		{
		  display: 'LOC',  
		  source: function (query, process,asyncProcess) {
			var urlStr = "/track/ItemMstServlet";
			$.ajax( {
			type : "POST",
			url : urlStr,
			async : true,
			data : {
				PLANT : '<%=plant%>',
				ACTION : "GET_LOC_LIST_FOR_SUGGESTION",
				QUERY : query
			},
			dataType : "json",
			success : function(data) {
				return asyncProcess(data.LOC_MST);
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
			return '<div><p class="item-suggestion">'+data.LOC+'</p></div>';
			//return '<div onclick="document.form.FROMWAREHOUSE.value = \''+data.LOCDESC+'\'"><p class="item-suggestion">'+data.LOC+'</p><br/><p class="item-suggestion">DESC:'+data.LOCDESC+'</p></div>';
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


function checkclearingagent(clearingagent){
// 	var agentid = document.form.CLEARAGENTID.value;
	var urlStr = "/track/MasterServlet";
	$.ajax( {
		type : "POST",
		url : urlStr,
		async : true,
		data : {
			CLEARINGAGENT : clearingagent,
			ACTION : "CLEARING_AGENT_CHECK"
			},
			dataType : "json",
			success : function(data) {
				if (data.status == "99") {
					alert("Clearing Agent Does't Exists");
					document.getElementById("clearingagent").focus();
					$("#clearingagent").typeahead('val', '');
// 					document.form.CLEARAGENTID.value="";
					return false;	
				} 
				else 
					return true;
			}
		});
	 return true;
}	
	
function checktypeofclearance(typeofclearance){
	var urlStr = "/track/MasterServlet";
	$.ajax( {
		type : "POST",
		url : urlStr,
		async : true,
		data : {
			TYPEOFCLEARANCE : typeofclearance,
			ACTION : "TYPE_OF_CLEARANCE_CHECK"
			},
			dataType : "json",
			success : function(data) {
				if (data.status == "99") {
					alert("Type Of Clearance Does't Exists");
					document.getElementById("typeofclearance").focus();
					$("#typeofclearance").typeahead('val', '');
					return false;	
				} 
				else 
					return true;
			}
		});
	 return true;
}

function checktransport(transport){	
	var urlStr = "/track/MasterServlet";
	$.ajax( {
		type : "POST",
		url : urlStr,
		async : true,
		data : {
			TRANSPORT_MODE : transport,
			ACTION : "TRANSPORT_CHECK"
			},
			dataType : "json",
			success : function(data) {
				if (data.status == "99") {
					alert("Transport Does't Exists");
					document.getElementById("transport").focus();
					$("#transport").typeahead('val', '');
					return false;	
				} 
				else 
					return true;
			}
		});
	 return true;
}

function transportCallback(data){
	if(data.STATUS="SUCCESS"){
		$("#transport").typeahead('val', data.transport);
		$("input[name=TRANSPORTID]").val(data.TRANSPORTID);
	}
}


function clearanceTypeCallback(data){
	if(data.STATUS="SUCCESS"){
		$("#typeofclearance").typeahead('val', data.CLEARANCE_TYPE_ID);
	}
}

function clearingAgentCallback(data){
	if(data.STATUS="SUCCESS"){
		//$("#clearingagent").typeahead('val', data.CLEARING_AGENT_ID);
		//$("input[name=CLEARINGAGENTID]").val(data.CLEARINGAGENTID);
		$("#clearingagent").typeahead('val', '"');
		$("#clearingagent").typeahead('val', '');
		document.form.CONTACT_NAME.value = "";
	}	
}

</script>     
<%
	EmailMsgUtil emailMsgUtil = new EmailMsgUtil();
	Map<String, String> emailMsg = emailMsgUtil.getEmailMsgDetails(plant, IConstants.PURCHASE_ORDER_AR);
	String template_subject = (String)emailMsg.get("SUBJECT");
	String template_body = (String)emailMsg.get("BODY1");
%>
<input type="hidden" id="plant_desc" value="<%=PLNTDESC %>" />
<input type="hidden" id="template_subject" value="<%=template_subject %>" />
<input type="hidden" id="template_body" value="<%=template_body %>" />
<jsp:include page="CommonEmailTemplate.jsp">
	<jsp:param value="<%=title%>" name="title"/>
	<jsp:param value="<%=PLNTDESC %>" name="PLANTDESC"/>
</jsp:include>
<jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
</jsp:include>
