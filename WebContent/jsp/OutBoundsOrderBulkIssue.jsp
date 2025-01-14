<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.dao.*"%>
<%@ include file="header.jsp" %>
<%@page import="com.track.constants.IConstants"%>
<%@page import="com.track.constants.IDBConstants"%>
<%! @SuppressWarnings({"rawtypes"}) %>
<%--New page design begin --%>
<%@include file="sessionCheck.jsp" %>
<%
String title = "Goods Issue By Sales Order ";
String plant=(String)session.getAttribute("PLANT");
PlantMstDAO plantMstDAO = new PlantMstDAO();
ArrayList plntList = plantMstDAO.getPlantMstDetails(plant);
Map plntMap = (Map) plntList.get(0);
String PLNTDESC = (String) plntMap.get("PLNTDESC");
%>
<%@include file="sessionCheck.jsp" %>
<jsp:include page="header2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
	<jsp:param name="mainmenu" value="<%=IConstants.SALES%>"/>
    <jsp:param name="submenu" value="<%=IConstants.SALES_TRANSACTION%>"/>
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

<!-- <html>
<script type="text/javascript" src="js/jquery-1.4.2.js"></script> -->
<script type="text/javascript" src="../jsp/js/json2.js"></script>
<link rel="stylesheet" href="../jsp/css/typeahead.css">
<link rel="stylesheet" href="../jsp/css/accounting.css">
<script type="text/javascript" src="../jsp/js/general.js"></script>
<script type="text/javascript" src="../jsp/js/calendar.js"></script>

<!-- <title>Goods Issue By Outbound Order </title>
<link rel="stylesheet" href="css/style.css">
 -->
 
 
 <script>

	var subWin = null;

	function popUpWin(URL) {
 		subWin = window.open(URL, 'OutBoundsOrderBulkIssue', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=800,height=400,left = 200,top = 184');
	}

	function popWinBatch(URL) {
		var locId = document.getElementById("LOC_0").value;
		if(locId=="" || locId.length==0 ) {
			alert("Enter Location!");
			document.getElementById("LOC_0").focus();
			return false;
		}
		else{
	 		subWin = window.open(URL, 'Outboundpickissuebyprodid', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=700,height=400,left = 200,top = 184');
			}
	}
	
function onIssue(form){
 	
 	if(document.form.AFLAG.value==null||document.form.AFLAG.value=="")
    {
   	   	alert("No Data's Found For Issue");
   	 	return false;
    }
 	
 	var locId = document.form.LOC_0.value;
	 if(!validateLocation(locId,0)){
		return false;
		 
	 }
    
    var checkFound = false;  
	 var orderLNo;
	 var len = document.form.chkdDoNo.length; 
	 if(len == undefined) len = 1;  
	 for (var i = 0; i < len ; i++)
    {
		if(len == 1 && (!document.form.chkdDoNo.checked))
		{
			checkFound = false;
		}
		
		else if(len ==1 && document.form.chkdDoNo.checked)
	     {
	    	 checkFound = true;
	    	 orderLNo = document.form.chkdDoNo.value;
	    	if(!verifyIssuingQty(orderLNo))	
		    	  return false;
	     }
	
	     else {
		     if(document.form.chkdDoNo[i].checked){
		    	 checkFound = true;
		    	 orderLNo = document.form.chkdDoNo[i].value;
		    	 if(!verifyIssuingQty(orderLNo))	
			    	  return false;
		     }
	     }
          		
        	     
    }
	 if (checkFound != true) {
		    alert ("Please check at least one checkbox.");
		    return false;
		    }
	 var invval = document.form.INVOICENO.value.length;
	 if(invval>0)
	 {
	   document.form.action ="/track/OrderIssuingServlet?action=BulkIssue";
	   document.form.submit();
	 }
	 else
		 {
		 if (confirm('Are you sure to submit this transaction without InvoiceNo?')) {
			 document.form.action ="/track/OrderIssuingServlet?action=BulkIssue";
			   document.form.submit();
		 } else {
			 document.getElementById("INVOICENO").focus();
		 }		 
		 }
  }
  
  
function onIssueinvoce(form){
 	
 	if(document.form.AFLAG.value==null||document.form.AFLAG.value=="")
    {
   	   	alert("No Data's Found For Issue");
   	 	return false;
    }
 	
 	
 	
 	if(document.form.INVOICENO.value==null||document.form.INVOICENO.value=="")
    {
   	   	alert("You are not able to submit this transaction without GINO");
   	 	return false;
    }
 	
 	if(document.form.ISSUEDATE_0.value==null||document.form.ISSUEDATE_0.value=="")
    {
 		alert("Please select Transaction Date");
   	 	return false;
    }
 	
 	var locId = document.form.LOC_0.value;
	 if(!validateLocation(locId,0)){
		return false;
		 
	 }
    
    var checkFound = false;  
	 var orderLNo;
	 var len = document.form.chkdDoNo.length; 
	 if(len == undefined) len = 1;  
	 for (var i = 0; i < len ; i++)
    {
		if(len == 1 && (!document.form.chkdDoNo.checked))
		{
			checkFound = false;
		}
		
		else if(len ==1 && document.form.chkdDoNo.checked)
	     {
	    	 checkFound = true;
	    	 orderLNo = document.form.chkdDoNo.value;
	    	if(!verifyIssuingQty(orderLNo))	
		    	  return false;
	     }
	
	     else {
		     if(document.form.chkdDoNo[i].checked){
		    	 checkFound = true;
		    	 orderLNo = document.form.chkdDoNo[i].value;
		    	 if(!verifyIssuingQty(orderLNo))	
			    	  return false;
		     }
	     }
          		
        	     
    }
	 if (checkFound != true) {
		    alert ("Please check at least one checkbox.");
		    return false;
		    }
	 var invval = document.form.INVOICENO.value.length;
	 if(invval>0)
	 {
		 
		 var urlStr = "/track/OutboundOrderHandlerServlet";
			$.ajax( {
				type : "POST",
				url : urlStr,
				data : {
				PLANT : "<%=plant%>",
				ACTION : "VGINO",
				GINO : document.form.INVOICENO.value
				},
				dataType : "json",
				success : function(data) {
					if (data.status == "100") {
						var resultVal = data.result;				
						var resultV = resultVal.invno;
						document.form.INVOICENO.value= resultV;
						document.form.action ="/track/OrderIssuingServlet?action=BulkIssueInvoice";
						document.form.submit();
					} else {
						alert("GINO "+document.form.INVOICENO.value+" already converted as Invoice. Please process with the new GINO.");
						document.form.INVOICENO.value = "";
					}
				}
			});	
	  
	 }
	 else
		 {
		 /* if (confirm('Are you sure to submit this transaction without InvoiceNo?')) {
			 document.form.action ="/track/OrderIssuingServlet?action=BulkIssueInvoice";
			   document.form.submit();
		 } else { */
			 alert("You are not able to submit this transaction without GINO");
			 document.getElementById("INVOICENO").focus();
		 //}		 
		 }
  }

function onIssueinvoceAndSendEmail(form){
 	
 	if(document.form.AFLAG.value==null||document.form.AFLAG.value=="")
    {
   	   	alert("No Data's Found For Issue");
   	 	return false;
    }
 	
 	
 	
 	if(document.form.INVOICENO.value==null||document.form.INVOICENO.value=="")
    {
   	   	alert("You are not able to submit this transaction without GINO");
   	 	return false;
    }
 	
 	var locId = document.form.LOC_0.value;
	 if(!validateLocation(locId,0)){
		return false;
		 
	 }

	 	if(document.form.ISSUEDATE_0.value==null||document.form.ISSUEDATE_0.value=="")
	    {
	 		alert("Please select Transaction Date");
	   	 	return false;
	    }
    
    var checkFound = false;  
	 var orderLNo;
	 var len = document.form.chkdDoNo.length; 
	 if(len == undefined) len = 1;  
	 for (var i = 0; i < len ; i++)
    {
		if(len == 1 && (!document.form.chkdDoNo.checked))
		{
			checkFound = false;
		}
		
		else if(len ==1 && document.form.chkdDoNo.checked)
	     {
	    	 checkFound = true;
	    	 orderLNo = document.form.chkdDoNo.value;
	    	if(!verifyIssuingQty(orderLNo))	
		    	  return false;
	     }
	
	     else {
		     if(document.form.chkdDoNo[i].checked){
		    	 checkFound = true;
		    	 orderLNo = document.form.chkdDoNo[i].value;
		    	 if(!verifyIssuingQty(orderLNo))	
			    	  return false;
		     }
	     }
          		
        	     
    }
	 if (checkFound != true) {
		    alert ("Please check at least one checkbox.");
		    return false;
		    }
	 var invval = document.form.INVOICENO.value.length;
	 if(invval>0)
	 {
		 
		 var urlStr = "/track/OutboundOrderHandlerServlet";
			$.ajax( {
				type : "POST",
				url : urlStr,
				data : {
				PLANT : "<%=plant%>",
				ACTION : "VGINO",
				GINO : document.form.INVOICENO.value
				},
				dataType : "json",
				beforeSend: function(){
					showLoader();
				},
				error: function(){
					hideLoader();
				},
				success : function(data) {
					if (data.status == "100") {
						var resultVal = data.result;				
						var resultV = resultVal.invno;
						document.form.INVOICENO.value= resultV;
						urlStr = "/track/OrderIssuingServlet?action=BulkIssueInvoice";
						var formData = $('#frmOutBoundOrderBulkIssue').serialize();
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
									$('#send_to').val($('#EMAIL').val()).multiEmail();
									$('#send_subject').val($('#template_subject').val()
															.replace(/\{COMPANY_NAME\}/, $('#plant_desc').val())
															.replace(/\{ORDER_NO\}/, $('#DONO').val())
															.replace(/\{ORDER_NO_2\}/, $('#INVOICENO').val())
															);
									$('.wysihtml5-sandbox').contents().find('.wysihtml5-editor').html(
												$('#template_body').val()
												.replace(/\{ORDER_NO\}/, $('#DONO').val())
												.replace(/\{CUSTOMER_NAME\}/, $('#CUST_NAME').val())
												);
									$('#send_attachment').val('Goods Issue');
								} else {
									$('.success-msg').html(data.MESSAGE).addClass('error-msg').removeClass('success-msg').css('display', 'inline');
								}
							}
						});	
					} else {
						hideLoader();
						alert("GINO "+document.form.INVOICENO.value+" already converted as Invoice. Please process with the new GINO.");
						document.form.INVOICENO.value = "";
					}
				}
			});	
	  
	 }
	 else
		 {
		 /* if (confirm('Are you sure to submit this transaction without InvoiceNo?')) {
			 document.form.action ="/track/OrderIssuingServlet?action=BulkIssueInvoice";
			   document.form.submit();
		 } else { */
			 alert("You are not able to submit this transaction without GINO");
			 document.getElementById("INVOICENO").focus();
		 //}		 
		 }
  }
 
function verifyIssuingQty(orderLNo)
{

		var issueQty = document.getElementById("issuingQty_"+(orderLNo)).value;
		if (issueQty == "" || issueQty.length == 0 || issueQty == '0') {
			alert("Enter a valid Quantity!");
			document.getElementById("issuingQty_"+(orderLNo)).focus();
			document.getElementById("issuingQty_"+(orderLNo)).select();
	        return false;
		}
		if(!isNumericInput(issueQty)){
			alert("Entered Quantity is not a valid number!");
			document.getElementById("issuingQty_"+(orderLNo)).focus();
			document.getElementById("issuingQty_"+(orderLNo)).select();
	         return false;
		}
		issueQty = parseFloat(issueQty).toFixed(3);
		var orderedQty = document.getElementById("QtyOrdered_"+(orderLNo)).innerHTML;
		orderedQty = removeCommas(orderedQty);
		orderedQty = parseFloat(orderedQty).toFixed(3);
		var issuedQty = document.getElementById("QtyIssued_"+(orderLNo)).innerHTML;
		issuedQty = removeCommas(issuedQty);
		issuedQty = parseFloat(issuedQty).toFixed(3);
		var balanceQty =  orderedQty - issuedQty;
		balanceQty = parseFloat(balanceQty).toFixed(3); 
	if ((Math.round(parseFloat(issueQty)*100)/100) > (Math.round(parseFloat(balanceQty)*100)/100))
		//if(issueQty > balanceQty)
	{
		alert("Exceeded the Ordered Qty of LineOrderNO:: "+orderLNo);
		document.getElementById("issuingQty_"+(orderLNo)).focus();
		document.getElementById("issuingQty_"+(orderLNo)).select();
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
function fullIssuing(isChk)
{
	var len = document.form.chkdDoNo.length;
	 var orderLNo; 
	 if(len == undefined) len = 1;  
    if (document.form.chkdDoNo)
    {
        for (var i = 0; i < len ; i++)
        {      
              	if(len == 1 && document.form.chkdDoNo.checked){
              		orderLNo = document.form.chkdDoNo.value;
              		setIssuingQty(orderLNo,i); 
              	}
              	else if(len == 1 && !document.form.chkdDoNo.checked){
              		return;
              	}
              	else{
                  	if(document.form.chkdDoNo[i].checked){
              		 orderLNo = document.form.chkdDoNo[i].value;
              		setIssuingQty(orderLNo,i); 
                  	}
              	}
            	      	
                
        }
    }
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
              		 orderLNo = document.form.chkdDoNo.value;
              	}
              	else{
              		document.form.chkdDoNo[i].checked = isChk;
              		 orderLNo = document.form.chkdDoNo[i].value;
              	}
            	setIssuingQty(orderLNo,i);       	
                
        }
    }
}

function setIssuingQty(orderLNo,i){
	var len = document.form.chkdDoNo.length;
	if(len == undefined) len = 1; 
	if(len ==1 && document.form.chkdDoNo.checked){	  
		if(document.form.fullIssue.checked)
		{
			setQty(orderLNo);
		}
	  
	}
		
	else if(len !=1 && document.form.chkdDoNo[i].checked){
		if(document.form.fullIssue.checked)
		{
			setQty(orderLNo);
		}
		else{
			document.getElementById("issuingQty_"+orderLNo).value = 0;
		}
	}
	else{
		document.getElementById("issuingQty_"+orderLNo).value = 0;	
	}
		
}

function setQty(orderLNo){
	var QtyOrdered = document.getElementById("QtyOrdered_"+orderLNo).innerHTML;
	QtyOrdered = removeCommas(QtyOrdered);
	var QtyIssued = document.getElementById("QtyIssued_"+orderLNo).innerHTML;
	QtyIssued = removeCommas(QtyIssued);
	var QtyIssuing = QtyOrdered - QtyIssued;
	QtyIssuing = parseFloat(QtyIssuing).toFixed(3);
	document.getElementById("issuingQty_"+orderLNo).value = QtyIssuing;	
}

/* function onRePrintWithOutBatch(){

 
    var DONO    = document.form.DONO.value;
    if(DONO == null    || DONO == "") {
    	alert("Please Enter Order No"); 
    	return false;
    }
    
    document.form.action="/track/DynamicFileServlet?action=printDOWITHOUTBATCH&DONO="+DONO;
    document.form.submit();
} */
function generateEmail(attachName)
{
	var img = toDataURL($(".dash-logo").attr("src"),
			function(dataUrl) {
				generatePdfMail(dataUrl,attachName);
		  	},'image/jpeg');
	}
function generatePdfMail(dataUrl,attachName){	
	formData = new FormData();
	formData.append("GINO_DONO", $("#DONO").val());
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
function isNumberKey(evt, element, id) {
	  var charCode = (evt.which) ? evt.which : event.keyCode;
	  if (charCode > 31 && (charCode < 48 || charCode > 57) && !(charCode == 46 || charCode == 8 || charCode == 45))
		  {
	    	return false;
		  }
	  return true;
	}

</script>


<jsp:useBean id="su"  class="com.track.util.StrUtils" />

<%
   Map checkedDOS = (Map) request.getSession().getAttribute("checkedDOS");
   String DONO     = StrUtils.fString(request.getParameter("DONO"));
   String action   = StrUtils.fString(request.getParameter("action")).trim();
   String result   = StrUtils.fString(request.getParameter("result")).trim();
   String sUserId = (String) session.getAttribute("LOGIN_USER");
   String userId = (String) session.getAttribute("LOGIN_USER");
   String RFLAG=    (String) session.getAttribute("RFLAG");
   String AFLAG=    (String) session.getAttribute("AFLAG");
   String INVOICENO=StrUtils.fString(request.getParameter("INVOICENO"));
   
//    com.track.dao.TblControlDAO _TblControlDAO=new   com.track.dao.TblControlDAO();
//    String gino = _TblControlDAO.getNextOrder(plant,userId,"GINO");
   
   boolean confirm = false;
   DOUtil _DOUtil=new DOUtil();
   ItemMstDAO _ItemMstDAO=new ItemMstDAO();
   
   _DOUtil.setmLogger(mLogger);
   _ItemMstDAO.setmLogger(mLogger);
   
   
   String vend = "",deldate="",jobNum = "",custName = "",custCode="",personIncharge = "",contactNum = "";
   String remark1 = "",remark2 = "",address="",address2="",address3="",collectionDate="",collectionTime="";
   String contactname="",telno="",email="",add1="",add2="",add3="",add4="",country="",zip="",remarks="",loc = "",REF = "",ISSUEDATE="";
   String sSaveEnb    = "disabled",fullIssue = "",allChecked = "";
   	 String SETCURRENTDATE_PICKANDISSUE = new PlantMstDAO().getSETCURRENTDATE_PICKANDISSUE(plant);//Thanzith

   loc=StrUtils.fString(request.getParameter("LOC"));
   ISSUEDATE=StrUtils.fString(request.getParameter("ISSUEDATE"));
   String issuedate=ISSUEDATE;
   ISSUEDATE=StrUtils.fString(request.getParameter("ISSUEDATE"));
   String curDate =DateUtils.getDate();
   if(ISSUEDATE.length()<0|ISSUEDATE==null||ISSUEDATE.equalsIgnoreCase(""))ISSUEDATE=curDate;
   
   String fieldDesc="<tr><td>Please enter any search criteria</td></tr>";
  
   if(action.equalsIgnoreCase("View")){
      Map m=(Map)request.getSession().getAttribute("podetVal");
      fieldDesc=(String)request.getSession().getAttribute("RESULT");
      fieldDesc="<font class='maingreen'>"+fieldDesc+"</font>";
      
      if(m.size()>0){
       jobNum=(String)m.get("jobNum");
       custName=(String)m.get("custName");
       custCode=(String)m.get("custCode");
      
       personIncharge=(String)m.get("contactname");
       contactNum=(String)m.get("contactNum");
       telno=(String)m.get("telno");
       email=(String)m.get("email");
       add1=(String)m.get("add1");
       add2=(String)m.get("add2");
       add3=(String)m.get("add3");
       add4=(String)m.get("add4");
       country=(String)m.get("country"); 
       zip=(String)m.get("zip");
       remarks=(String)m.get("remarks");
       contactNum=(String)m.get("contactNum");
       address=(String)m.get("address");
       address2=(String)m.get("address2");
       address3=(String)m.get("address3");
       deldate=(String)m.get("collectionDate");
       collectionTime=(String)m.get("collectionTime");
       remark1=(String)m.get("remark1");
       remark2=(String)m.get("remarks");
       loc = StrUtils.fString(request.getParameter("LOC"));
       REF = StrUtils.fString(request.getParameter("REF"));
     
      }
      else 
      {
        fieldDesc="Details not found for Order:"+ DONO;
        fieldDesc="<font class='mainred'>"+fieldDesc+"</font>";
      }
    }
     
     if(result.equalsIgnoreCase("catchrerror"))
      {
        fieldDesc=(String)request.getSession().getAttribute("CATCHERROR");
        fieldDesc = "<font class='mainred'>" + fieldDesc + "</font>";
        allChecked = StrUtils.fString(request.getParameter("allChecked"));
        fullIssue = StrUtils.fString(request.getParameter("fullReceive"));
     }
 %>
 <%--New page design begin --%>
<center>
	<%if(fieldDesc!=null){ %>
	<h2><small class="success-msg"> <%=fieldDesc%></small></h2>
	<%} %>
</center>
   		
<div class="container-fluid m-t-20">
	 <div class="box"> 
	  <!-- Muruganantham Modified on 16.02.2022 -->
            <ol class="breadcrumb backpageul">      	
                <li><a href="../home"><span class="underline-on-hover">Dashboard</span></a></li>                
                <li><a href="../salesTransactionDashboard"><span class="underline-on-hover">Sales Transaction Dashboard</span> </a></li>                
                <li>Goods Issue By Sales Order</li>                                   
            </ol>             
    <!-- Muruganantham Modified on 16.02.2022 -->
            <div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title"><%=title %></h1>
                <h1 style="font-size: 18px; cursor: pointer;" class="box-title pull-right" onclick="window.location.href='../salesTransactionDashboard'">
			  <i class="glyphicon glyphicon-remove"></i>
		 	  </h1>
		</div>
		
 <div class="box-body">
<%--New page design end --%>

<form class="form-horizontal" id="frmOutBoundOrderBulkIssue" name="form" method="post" action="/track/OrderIssuingServlet?">

       <div class="form-group">
       <label class="control-label col-sm-4" for="outbound Order">
       <i class="glyphicon glyphicon-star" style="font-size: 10px; top:-6px; color: #e50000"></i>&nbsp;Order Number:</label>
       <div class="col-sm-4">
      	    <div class="input-group">
    		<INPUT class="form-control" type="TEXT" size="30" MAXLENGTH="20" name="DONO" id="DONO" value="<%=DONO%>" 
    		onkeypress="if((event.keyCode=='13') && ( document.form.DONO.value.length > 0)){loadOutboundOrderDetails();}"/>
   		 	<span class="input-group-addon"  onClick="javascript:popUpWin('../jsp/do_list_do.jsp?DONO='+form.DONO.value+'&OpenForPick=yes');return false;">
   		 	<a href="#" data-toggle="tooltip" data-placement="top" title="Sales Details">
   		 	<i  class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span>
  		</div>
  		</div>
  		<div class="form-inline">
  		<div class="col-sm-1 ">   
  		<button type="button" class="Submit btn btn-default" onClick="viewOutboundOrders();"><b>View</b></button>
  		</div>
  		</div> 
 		</div>
 		
 		<div class="form-group">
  		<label class="control-label col-sm-4" for="customer name">Customer Name:</label>
        <div class="col-sm-4">
        <INPUT name="CUST_NAME"  id="CUST_NAME"  class="form-control" MAXLENGTH="20" readonly type = "TEXT" value="<%=StrUtils.forHTMLTag(custName)%>" size="30"  MAXLENGTH=80>
    	</div>
 		</div> 
 		
 					<INPUT type = "hidden" name="CUST_CODE" value = "<%=custCode%>">
                    <INPUT type = "hidden" name="LOGIN_USER" value = "<%=sUserId%>">
                    <INPUT type = "hidden" name="CUST_CODE1" value = "<%=custCode%>">
                    
        <div class="form-group">
    	<label class="control-label col-sm-4" for="Person Incharge">Contact Name:</label>
        <div class="col-sm-4">
        <INPUT type = "TEXT" size="30"  MAXLENGTH="20"  class = "form-control" readonly name="PERSON_INCHARGE" value="<%=StrUtils.forHTMLTag(personIncharge)%>">
        </div>
 		</div>
        
        <div class="form-group">
        <label class="control-label col-sm-4" for="Reference No">Reference No:</label>
        <div class="col-sm-4">
        <INPUT type="TEXT" size="30" MAXLENGTH=20 name="JOB_NUM" class="form-control" readonly value="<%=jobNum%>">
       </div>
 		</div>
 		 
        <div class="form-group">
        <label class="control-label col-sm-4" for="Order Date">Order Date:</label>
        <div class="col-sm-4">
        <INPUT type="TEXT" size="30" MAXLENGTH="10"	name="DELDATE" class="form-control" readonly	value="<%=deldate%>" />
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
        <label class="control-label col-sm-4" for="Time">Order Time:</label>
        <div class="col-sm-4">
        <INPUT type="TEXT" size="30" class="form-control" readonly MAXLENGTH="20" name="COLLECTION_TIME"	value="<%=collectionTime%>" />
        </div>
    	<!-- <div class="form-inline">
    	<label class="control-label col-sm-3" for="Email Address">Email:</label>
        <div class="col-sm-3"> -->
        <INPUT	type="hidden" size="30" class="form-control" readonly MAXLENGTH="20"name="EMAIL" id="EMAIL" value="<%=email%>" />
        <!-- </div>
 		</div> -->
 		</div>
 		
 		<div class="form-group">
        <label class="control-label col-sm-4" for="Remarks">Remarks:</label>
        <div class="col-sm-4">
        <INPUT type="TEXT" size="30" MAXLENGTH="20"	name="REMARK1" class="form-control" readonly	value="<%=remark1%>" />
        </div>
    	<!-- <div class="form-inline">
    	<label class="control-label col-sm-3" for="Unit Number">Unit No:</label>
        <div class="col-sm-3"> -->
        <INPUT type="hidden" size="30" class="form-control" readonly MAXLENGTH="20" name="ADD1" value="<%=add1%>" />
        <!-- </div>
 		</div> -->
 		</div>
 		 		
    	<!-- <div class="form-group">
    	<div class="form-inline">
    	<label class="control-label col-sm-8" for="Buiding Name">Building:</label>
        <div class="col-sm-3"> -->
        <INPUT type="hidden" size="30" MAXLENGTH="20"	class="form-control" readonly name="ADD2" value="<%=add2%>" />
        <!-- </div>
 		</div>
 		</div> -->
 		
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
       <!--  </div>
 		</div>
 		</div> -->

        <!-- <div class="form-group">
        <div class="form-inline">
    	<label class="control-label col-sm-8" for="customer remarks">Customer Remarks:</label>
        <div class="col-sm-3"> -->
        <INPUT type="hidden" size="30" class="form-control" readonly MAXLENGTH=20 name="REMARK2" value="<%=remarks%>">
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
        <b>Full Issuing</b></label>
		</div>
    	</div>
 			  		
  		 <TABLE BORDER="0" CELLSPACING="0" WIDTH="100%" class="table" >
         <thead style="background: #eaeafa; font-size: 14px">
         <tr>
         <th width="5%">CHK</th>
         <th width="5%">ORDER LINE NO</th>
         <th width="15%">PRODUCT ID</th>
         <th width="20%">DESCRIPTION</th>
         <th width="8%">UOM</th>
         <th width="10%">ORDER QUANTITY</th>
         <th width="10%">ISSUED QUANTITY</th>
         <th width="10%">ISSUING QUANTITY</th>
         <th width="15%" style="min-width:140px">BATCH</th>
         <th width="5%">STATUS</th>
         </tr>
       	 </thead>
       	 <tbody style="font-size: 15px;">
       	  <% 
      ArrayList al= _DOUtil.listOutGoingIssueBulkDODET(plant,DONO);
      String issuingQty = "",value = null,batch = "NOBATCH";
      if(al.size()==0)
      {
  	    AFLAG="";
      }
       if(al.size()>0)
       {
    	 AFLAG="DATA";
       for(int i=0 ; i<al.size();i++)
       {
    	   issuingQty = "";
    	   batch = "NOBATCH";
          Map m=(Map)al.get(i);
          int iIndex = i + 1;
          String bgcolor = ((i == 0) || (i % 2 == 0)) ? "#FFFFFF"  : "#FFFFFF";
          String dono = (String)m.get("dono");
          String dolnno = (String)m.get("dolnno");
          String custname= (String)m.get("custname");
          String item= (String)m.get("item");
          //String loc= (String)m.get("loc");
          //String batch= "NOBATCH";
          //String batch= (String)m.get("batch");
          String qtyor= (String)m.get("qtyor");
          String qtyis= (String)m.get("qtyis");
          String desc= (String)m.get("Itemdesc");//_ItemMstDAO.getItemDesc(plant ,item);
          String uom = (String)m.get("uom");//_ItemMstDAO.getItemUOM(plant ,item);
          if(checkedDOS!=null && result.equalsIgnoreCase("catchrerror")){
              value = (String)checkedDOS.get(dolnno);   
              
              if(value!=null)
              {
            	  issuingQty  = value.split(":")[0];
            	  batch  = value.split(":")[1];
            	 
              }
              }
       
       %>
       
      		
        <TR bgcolor = "<%=bgcolor%>">
           
         
							
              <TD width="5%"  align="left"><font color="black"><INPUT Type=Checkbox  style="border:0;background=#dddddd" 
              name="chkdDoNo"  value="<%=dolnno%>" <%if(value!=null){%>checked <%}%> onclick = "setIssuingQty(<%=dolnno%>,<%=i%>);"></font></TD>
              <TD align="center" width="5%"><%=(String)m.get("dolnno")%></TD>
              <TD align="left" width="15%"><%=(String)m.get("item")%></TD>
              <TD align="left" width="20%"><%=(String)desc%></TD>
              <TD align="left" width="8%"><%=(String)uom%></TD>
              <TD align="center" id = "QtyOrdered_<%=dolnno%>"width="10%"><%=StrUtils.formatNum((String)m.get("qtyor"))%></TD>              
              <TD align="center" id = "QtyIssued_<%=dolnno%>" width="10%"><%=StrUtils.formatNum((String)m.get("qtyis"))%></TD>
              <TD align="center" width="10%">
						<input class="form-control" type="text" onkeypress="return isNumberKey(event,this,4)" name = "issuingQty_<%=dolnno%>" id = "issuingQty_<%=dolnno%>" 
						 size = "6" maxlength = "10"  value = <%=issuingQty%>>
						</TD>
             <%--  <TD align="center" width="14%"><%=StrUtils.formatNum((String)m.get("qtyPick"))%></TD> --%>
             
             	<td width="15%" align="center"><div class="input-group"><INPUT class="form-control" name="BATCH_<%=dolnno%>" id="BATCH_<%=dolnno%>" value = <%=batch%> type="TEXT" size="15"  /><input type="hidden" name="BATCH_ID_<%=dolnno%>" value="-1" />
             	<a href="#" class="input-group-addon" data-toggle="tooltip" data-placement="top" title="Batch Details" onClick="javascript:popWinBatch('../jsp/OutBoundMultiPickingBatch.jsp?ITEMNO=<%=item%>&DOLNNO=<%=dolnno%>&LOC_0='+form.LOC_0.value+'&BATCH_0='+form.BATCH_<%=dolnno%>.value+'&BATCH_ID_0='+form.BATCH_ID_<%=dolnno%>.value+'&INDEX=0&TYPE=OBBULK&UOM=<%=(String)uom%>');return false;">
				<i  class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></div>
             </td>
              <TD align="center" width="5%"><%=(String)m.get("lnstat")%></TD>
           </TR>
           
       <%}} else {%>
       
             <TR> <TD align="center" colspan="9"> Data's Not Found For Issuing</TD></TR>
       <%}%>
         </tbody>
       </TABLE>
       
        <INPUT type="Hidden" name="ENCRYPT_FLAG" value="1">
        <INPUT type="Hidden" name="RFLAG" value="5">
        <INPUT type="Hidden" name="AFLAG" value="<%=AFLAG%>">
        
        <div class="form-group">
    	<label class="control-label col-sm-2" for="Location">Location:</label>
        <div class="col-sm-3">
        <div class="input-group">
        <INPUT class="form-control" name="LOC_0" id="LOC_0" type="TEXT" size="30" value = '<%=loc%>' 
		onkeypress="if((event.keyCode=='13') && ( this.value.length > 0)){validateLocation(this.value,0);}"	MAXLENGTH=80>
        <span class="input-group-addon"  onClick="javascript:popUpWin('../jsp/loc_list_MultiReceivewms.jsp?INDEX=0&LOC='+form.LOC_0.value);return false;">
   		 	<a href="#" data-toggle="tooltip" data-placement="top" title="Location Details">
   		 	<i  class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span>
        </div>		
        </div>
 		<div class="form-inline">
        <label class="control-label col-sm-2 required" for="Transaction Date">Transaction Date:</label>
        <div class="col-sm-3">
        <div class="input-group">  
        <%if(SETCURRENTDATE_PICKANDISSUE.equals("1")){%>        
        <INPUT class="form-control datepicker" name="ISSUEDATE_0" id="ISSUEDATE_0" value="<%=ISSUEDATE%>" type="TEXT" size="30" MAXLENGTH="80" readonly="readonly"> 
        <%}else {%>
        <INPUT class="form-control datepicker" name="ISSUEDATE_0" id="ISSUEDATE_0" value="<%=issuedate%>" type="TEXT" size="30" MAXLENGTH="80" readonly="readonly">
      <%}%>
       </div>
    	</div>
    	</div>
 		</div>
        
	<div class="form-group" style="display:none;">
	<label class="control-label col-sm-2" for="Transport">Transport:</label>
        <div class="col-sm-3 ac-box">
         <input type="hidden" name="TRANSPORTID" value="">
			<input type="text" class="ac-selected  form-control typeahead" id="transport" placeholder="Select a transport" name="transport" onchange="checktransport(this.value)" value="">
			<span class="select-icon" onclick="$(this).parent().find('input[name=\'transport\']').focus()">
				<i class="glyphicon glyphicon-menu-down"></i>
			</span>
   		 </div> 
<!--    		       <div class="form-group"> -->
      <!-- <label class="control-label col-sm-2" for="Clearance type">Type of Clearance:</label> -->
	<label class="control-label col-sm-2" for="Clearing Agent">Clearing Agent:</label>
      <div class="col-sm-3 ac-box">
      <input type="hidden" id="typeofclearance" name="typeofclearance" value="" />          
            <!-- <input type="text" class="ac-selected  form-control typeahead typeofclearance" id="typeofclearance" placeholder="Select a type of clearance" name="typeofclearance" onchange="checktypeofclearance(this.value)" value=""> -->
			<input type="text" class="ac-selected  form-control typeahead clearingagent" id="clearingagent" placeholder="Select a clearing agent" name="clearingagent" onchange="checkclearingagent(this.value)" value="">
			<span class="select-icon" onclick="$(this).parent().find('input[name=\'clearingagent\']').focus()">
			<i class="glyphicon glyphicon-menu-down"  style="font-size: 8px;"></i>
		</span>
<!--       </div> -->
      </div>       
      </div>
 		
 		<div class="form-group" style="display:none;">	
	    <label class="control-label col-sm-2" for="Contact Name">Contact Name:</label>
	    <div class="col-sm-3">
	    <INPUT class="form-control datepicker" name="CONTACT_NAME" id="CONTACT_NAME" value="" type="TEXT" readonly="readonly">
	      </div>	
    	<div class="form-inline">
    	<label class="control-label col-sm-2" for="TRACKINGNO">Tracking No:</label>
        <div class="col-sm-3">
                <div class="input-group">
        <INPUT class="form-control" name="TRACKINGNO" id="TRACKINGNO" type="TEXT"  value="" style="width: 100%" MAXLENGTH=20 placeholder="Max 20 characters">
        </div>
        </div>
 		</div>			 			
 		</div>

 		<div class="form-group">	
	    <label class="control-label col-sm-2" for="Remarks">Remarks:</label>
	    <div class="col-sm-3">
	    <INPUT class="form-control" name="REF" type="TEXT"  value="<%=REF%>" size="30" MAXLENGTH=100>
	      </div>	
    	<div class="form-inline">
    	<label class="control-label col-sm-2" for="GINO">GINO:</label>
        <div class="col-sm-3">
                <div class="input-group">
        <INPUT class="form-control" name="INVOICENO" id="INVOICENO" type="TEXT"  value="<%=INVOICENO%>" style="width: 100%" MAXLENGTH=100>
        <span class="input-group-addon"  onClick="javascript:onNew();return false;">
   		 	<a href="#" data-toggle="tooltip" data-placement="top" title="Auto-Generate">
   		 	<i class="glyphicon glyphicon-edit" style="font-size: 20px;"></i></a></span>
        </div>
        </div>
 		</div>			 			
 		</div>
        
  		<div class="form-group">        
      	<div class="col-sm-12" align="left">
      	<div class="dropup">
		<%--https://www.w3schools.com/bootstrap/tryit.asp?filename=trybs_dropdown-menu-dropup&stacked=h --%>
	    <button class="btn btn-success dropdown-toggle" type="button" data-toggle="dropdown">Issue Goods
	    <span class="caret"></span></button>
	    <ul class="dropdown-menu">
	      <li><a id="btnRedeive" href="#" onclick="onIssueinvoce(document.form)">Issue Goods</a></li>
	      <li><a id="btnReceiveEmail" href="#" onclick="onIssueinvoceAndSendEmail(document.form)">Issue Goods and Send Email</a></li>
	    </ul>
<!-- 	    <button type="button" class="Submit btn btn-default" onClick="window.location.href='../home'"><b>Back</b></button> -->
	  </div>
      	
       	<!-- <button type="button" class="Submit btn btn-default" onClick="window.location.href='outboundTransaction.jsp'"><b>Back</b></button>&nbsp;&nbsp; -->
      	<!-- <button type="button" class="Submit btn btn-default" onClick="onIssue(document.form)"><b>Issue Goods</b></button>&nbsp;&nbsp; -->
      	</div>
        </div>
         		
  		</form>
		</div>



 
 
<script>
$(document).ready(function(){
	onNew();
	
	$('[data-toggle="tooltip"]').tooltip();
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
				document.form.INVOICENO.value= resultV;
	
			} else {
				alert("Unable to genarate GINO");
				document.form.INVOICENO.value = "";
			}
		}
	});	
	}
function viewOutboundOrders(){
	document.form.action="/track/OrderIssuingServlet?action=View";
    document.form.submit();
}



function loadOutboundOrderDetails() {
	var outboundOrderNo = document.form.DONO.value;
	var urlStr = "/track/OutboundOrderHandlerServlet";
	$.ajax( {
		type : "POST",
		url : urlStr,
		data : {
			ORDER_NO : outboundOrderNo,
			PLANT : "<%=plant%>",
			ACTION : "LOAD_OUTBOUND_ORDER_DETAILS"
					},
					dataType : "json",
					success : function(data) {
						if (data.status == "100") {
							var resultVal = data.result;
							
							document.form.CUST_NAME.value = resultVal.CUSTNAME;
							document.form.JOB_NUM.value=resultVal.JOBNUM;
							document.form.action = "/track/OrderIssuingServlet?action=View";
							document.form.submit();

						} else {
							alert("Not a valid Order Number!");
							document.form.DONO.value = "";
							document.form.CUST_NAME.value = "";
							document.form.DONO.focus();
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
</script>

</div>
</div>
<%
	EmailMsgUtil emailMsgUtil = new EmailMsgUtil();
	Map<String, String> emailMsg = emailMsgUtil.getEmailMsgDetails(plant, IConstants.SALES_ORDER_API);
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
<%@include file="../jsp/newClearanceTypeModal.jsp"%>
<%@include file="../jsp/newClearingAgentModal.jsp"%>
<jsp:include page="newTransportModeModal.jsp" flush="true"></jsp:include>
<jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
</jsp:include>
