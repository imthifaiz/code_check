<%@ page import="com.track.dao.*"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="java.util.*"%>
<%@ page import="com.track.util.*"%>
<%@ page import="com.track.gates.*"%>
<%@ page import="com.track.gates.DbBean"%>
<%@ page import="com.track.gates.Generator"%>
<%@ include file="header.jsp"%>
<%@ page import="com.track.constants.*"%>
<%! @SuppressWarnings({"rawtypes"}) %>
<%--New page design begin --%>
<%
String title = "Goods Pick/Issue By Sales Order ";
String plant = StrUtils.fString((String)session.getAttribute("PLANT"));
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
<script src="js/jquery-1.4.2.js"></script> -->
<script src="../jsp/js/calendar.js"></script>
<script src="../jsp/js/json2.js"></script>
<script src="../jsp/js/general.js"></script>
<!-- <link rel="stylesheet" href="css/style.css"> -->
<script type="text/javascript">
var subWin = null;
var chkdvalue = new Array(); 
var chkstring="";
function popUpWin(URL) {
	
 		subWin = window.open(URL, 'Outboundpickissuebyprodid', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=800,height=400,left = 200,top = 184');
		
}
function popWinBatch(URL) {

	var locId = document.getElementById("LOC_0").value;
	if(locId=="" || locId.length==0 ) {
		alert("Enter Location!");
		document.getElementById("LOC_0").focus();
		return false;
	}
	else{
 		subWin = window.open(URL, 'Outboundpickissuebyprodid', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=800,height=400,left = 200,top = 184');
		}
}
function onIssue(form){
 	
 	var Traveler ;
 	var concatTraveler="";
 	var type = document.form.PICKISSUE.value;
 	
 	if(document.form.AFLAG.value==null||document.form.AFLAG.value=="")
    {
   	   	alert("No Data's Found For Issue");
   	 	return false;
    }
 	
 	var locId = document.form.LOC_0.value;
	 if(!validateLocation(locId,0)){
		return false;
		 
	 }

		var date=document.form.TRANSACTIONDATE.value;

		if(date==null||date=="")
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
	    	 document.form.TRAVELER.value=document.form.chkdDoNo.value+"=";
	    	 orderLNo = chkdvalue[0]+"_"+chkdvalue[1];
	    	 if(type!="Issue"){
	    		if(!verifyIssuingQty(orderLNo))	
		    	  return false;
	    	 }
	     }
	
	     else {
		     if(document.form.chkdDoNo[i].checked){
		    	 checkFound = true;
		    	 Traveler=document.form.chkdDoNo[i].value;
	             concatTraveler=concatTraveler+Traveler+"=";
	             orderLNo = chkdvalue[0]+"_"+chkdvalue[1];
	             if(type!="Issue"){
	 	    		if(!verifyIssuingQty(orderLNo))	
	 		    	  return false;
	 	    	 }
		     }
		     document.form.TRAVELER.value=concatTraveler; 
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
						document.form.action ="/track/OrderIssuingServlet?action=BulkPickIssuebyOrders";
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
		 if (confirm('Are you sure to submit this transaction without GINo?')) {
			 document.form.action ="/track/OrderIssuingServlet?action=BulkPickIssuebyOrders";
			   document.form.submit();
		 } else {
			 document.getElementById("INVOICENO").focus();
		 }		 
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
		
	if(parseFloat(issueQty) > parseFloat(balanceQty))
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
              		chkstring = document.form.chkdDoNo.value;
              		 chkdvalue = chkstring.split(',');
              		 orderLNo = chkdvalue[0]+"_"+chkdvalue[1];
              	}
              	else{
              		document.form.chkdDoNo[i].checked = isChk;
              		chkstring = document.form.chkdDoNo[i].value;
              		chkdvalue = chkstring.split(',');
              		 orderLNo = chkdvalue[0]+"_"+chkdvalue[1];
              	}
            	setIssuingQty(orderLNo,i);       	
                
        }
    }
}
 
function fullIssuing(isChk)
{
	if(document.getElementById('Issue').checked) {
	}
	else
		{
	var len = document.form.chkdDoNo.length;
	 var orderLNo; 
	 if(len == undefined) len = 1;  
    if (document.form.chkdDoNo)
    {
        for (var i = 0; i < len ; i++)
        {      
              	if(len == 1 && document.form.chkdDoNo.checked){
              		chkstring = document.form.chkdDoNo.value;
             		 chkdvalue = chkstring.split(',');
             		 orderLNo = chkdvalue[0]+"_"+chkdvalue[1];
              		//orderLNo = document.form.chkdDoNo.value;
              		setIssuingQty(orderLNo,i); 
              	}
              	else if(len == 1 && !document.form.chkdDoNo.checked){
              		return;
              	}
              	else{
                  	if(document.form.chkdDoNo[i].checked){
                  		chkstring = document.form.chkdDoNo[i].value;
                  		chkdvalue = chkstring.split(',');
                  		 orderLNo = chkdvalue[0]+"_"+chkdvalue[1];
              		 //orderLNo = document.form.chkdDoNo[i].value;
              			setIssuingQty(orderLNo,i); 
                  	}
              	}
        	}   	      	
                
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
function Disabletextbox()
{
	debugger;
	 var orderLNo;
	 var len = document.form.chkdDoNo.length; 
	 if(len == undefined) len = 1;
   for (var i = 0; i < len ; i++)
   {
   		chkstring = document.form.chkdDoNo[i].value;
      	chkdvalue = chkstring.split(',');
		orderLNo = chkdvalue[0]+"_"+chkdvalue[1];
		if(document.getElementById("Issue").checked == true)
		  {
			  document.getElementById("issuingQty_"+orderLNo).value = "";
			  document.getElementById("issuingQty_"+orderLNo).disabled = true;
			  
	      }
		else
		{
			document.getElementById("issuingQty_"+orderLNo).disabled = false;
		}
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
<%--New page design begin --%>

<div class="container-fluid m-t-20">
	 <div class="box"> 
	 
<!-- Muruganantham Modified on 16.02.2022 -->
            <ol class="breadcrumb backpageul">      	
                <li><a href="../home"><span class="underline-on-hover">Dashboard</span> </a></li>                
                <li><a href="../salesTransactionDashboard"><span class="underline-on-hover">Sales Transaction Dashboard</span> </a></li>                
                <li>Goods Pick/Issue By Sales Order</li>                                   
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
<%-- <title>Goods Pick/Issue By Outbound Order</title>
<link rel="stylesheet" href="css/style.css">
<%@ include file="body.jsp"%> --%>
<jsp:useBean id="gn" class="com.track.gates.Generator" />
<jsp:useBean id="dhb" class="com.track.tables.DOHDR" />
<jsp:useBean id="ddb" class="com.track.tables.DODET" />
<jsp:useBean id="du" class="com.track.util.DateUtils" />

<%
    DOUtil _doUtil=new DOUtil();
    _doUtil.setmLogger(mLogger);
    ItemMstDAO itemdao = new ItemMstDAO();
    String itemno="",  itemdesc="",TRANSACTIONDATE="",PICKISSUE="",FROM_DATE="",TO_DATE="",fdate="",tdate="",CUSTOMER="",orderno="",loc="";

    String action   = StrUtils.fString(request.getParameter("action")).trim();
    Map checkedDOS = (Map) request.getSession().getAttribute("checkedDOS");
    String result   = StrUtils.fString(request.getParameter("result")).trim();
    //String dono     = StrUtils.fString(request.getParameter("DONO"));
    
    String sUserId = (String) session.getAttribute("LOGIN_USER");
    String userId = (String) session.getAttribute("LOGIN_USER");
    String RFLAG=    (String) session.getAttribute("RFLAG");
    String AFLAG=    (String) session.getAttribute("AFLAG");
    
    com.track.dao.TblControlDAO _TblControlDAO=new   com.track.dao.TblControlDAO();
    String gino = _TblControlDAO.getNextOrder(plant,userId,"GINO");
    
    PICKISSUE = StrUtils.fString(request.getParameter("PICKISSUE"));
    FROM_DATE     = StrUtils.fString(request.getParameter("FROM_DATE"));
    TO_DATE   = StrUtils.fString(request.getParameter("TO_DATE"));
    CUSTOMER      = StrUtils.fString(request.getParameter("CUSTOMER"));
    orderno     = StrUtils.fString(request.getParameter("DONO"));
    itemno=StrUtils.fString(request.getParameter("ITEM"));
    itemdesc=StrUtils.replaceCharacters2Recv(StrUtils.fString(request.getParameter("DESC")));
    loc      = StrUtils.fString(request.getParameter("LOC"));
	String SETCURRENTDATE_PICKANDISSUE = new PlantMstDAO().getSETCURRENTDATE_PICKANDISSUE(plant);//Thanzith
    
    if(FROM_DATE==null) FROM_DATE=""; else FROM_DATE = FROM_DATE.trim();
    String curDate =DateUtils.getDate();
    if(FROM_DATE.length()<0||FROM_DATE==null||FROM_DATE.equalsIgnoreCase(""))FROM_DATE=curDate;

    if (FROM_DATE.length()>5)

    fdate    = FROM_DATE.substring(6)+"-"+ FROM_DATE.substring(3,5)+"-"+FROM_DATE.substring(0,2);



    if(TO_DATE==null) TO_DATE=""; else TO_DATE = TO_DATE.trim();
    if (TO_DATE.length()>5)
    tdate    = TO_DATE.substring(6)+"-"+ TO_DATE.substring(3,5)+"-"+TO_DATE.substring(0,2);

    
    String fullIssue = "",allChecked = "",REF = "",dno="",chkString ="";
    String fieldDesc="";
    //String fieldDesc="<tr><td> Please enter any search criteria</td></tr>";
 	TRANSACTIONDATE = StrUtils.fString(request.getParameter("TRANSACTIONDATE"));
 	if(PICKISSUE.length()<=0){
 		PICKISSUE = "Pick&Issue";
 		}
 	    
    if(TRANSACTIONDATE.length()<0|TRANSACTIONDATE==null||TRANSACTIONDATE.equalsIgnoreCase(""))TRANSACTIONDATE=curDate;
    ArrayList al=new ArrayList();
     if(action.equalsIgnoreCase("View")){
    
        al= _doUtil.listOutboundSummaryBymultiple(plant,fdate,tdate,CUSTOMER,orderno,itemno);
     
    }
    if(result.equalsIgnoreCase("sucess"))
    {
     fieldDesc=StrUtils.fString((String)request.getSession().getAttribute("RESULT"));
     fieldDesc="<font class='maingreen'>"+fieldDesc+"</font>";
    } 
    else if(result.equalsIgnoreCase("catchrerror"))
    {
      fieldDesc=StrUtils.fString((String)request.getSession().getAttribute("CATCHERROR"));
      fieldDesc = "<font class='mainred'>" + fieldDesc + "</font>";
      allChecked = StrUtils.fString(request.getParameter("allChecked"));
      fullIssue = StrUtils.fString(request.getParameter("fullReceive"));
   } 
  
    else if(result.equalsIgnoreCase("error"))
    {
      fieldDesc=StrUtils.fString((String)request.getSession().getAttribute("RESULTERROR"));
      fieldDesc = "<font class='mainred'>" + fieldDesc + "</font>";
      allChecked = StrUtils.fString(request.getParameter("allChecked"));
      fullIssue = StrUtils.fString(request.getParameter("fullReceive"));
   } 
  
          
  //}
 
%>
<form class="form-horizontal" name="form" method="post" action="/track/OrderIssuingServlet?">
   
        
      	<span style="text-align: center;">
	<h2><small class="success-msg"> <%=fieldDesc%></small></h2>
</span>
      
      <br>
   		<div class="form-group">
        <label class="control-label col-sm-2" for="From Date">From Date:</label>
        <div class="col-sm-3">
        <div class="input-group">          
        <INPUT name="FROM_DATE" type = "TEXT" value="<%=FROM_DATE%>" size="30"  MAXLENGTH=20 class ="form-control datepicker" READONLY>
        </div>
      	</div>
    	<div class="form-inline">
    	<label class="control-label col-sm-2" for="To Date">To Date:</label>
        <div class="col-sm-3">
        <div class="input-group">          
        <INPUT name="TO_DATE" type = "TEXT" value="<%=TO_DATE%>" size="30"  MAXLENGTH=20 class = "form-control datepicker" READONLY>
        </div>
      	</div>
 		</div>
 		</div>
   
       <div class="form-group">
       <label class="control-label col-sm-2" for="Customer Name">Customer Name/ID:</label>
       <div class="col-sm-3">
      	    <div class="input-group">
    		<INPUT class="form-control" name="CUSTOMER" type = "TEXT" value="<%=CUSTOMER%>" size="30"  MAXLENGTH=100>
   		 	<span class="input-group-addon"  onClick="javascript:popUpWin('../jsp/customer_list_issue_summary.jsp?CUSTOMER='+form.CUSTOMER.value+'&TYPE=MULTIPLEOB');">
   		 	<a href="#" data-toggle="tooltip" data-placement="top" title="Supplier Details">
   		 	<i  class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span>
  		</div>
  		</div>
  		<div class="form-inline">
  		<label class="control-label col-sm-2" for="Outbound Order">Order Number:</label>
        <div class="col-sm-3">
      	    <div class="input-group">
    		<INPUT class="form-control" type="TEXT" size="30" MAXLENGTH="20" name="DONO" value="<%=orderno%>"  >
    		<span class="input-group-addon"  onClick="javascript:popUpWin('../jsp/do_list_do.jsp?DONO='+form.DONO.value+'&OpenForPick=yes');">
   		 	<a href="#" data-toggle="tooltip" data-placement="top" title="Sales Details">
   		 	<i class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span>
        </div>
        </div>
 		</div>  
 		</div>
 		
 		<INPUT type = "hidden" name="JOB_NUM" value = "">
        <INPUT type = "hidden" name="CUST_NAME" value = "">
		<INPUT type = "hidden" name="LOGIN_USER" value = "<%=sUserId%>">
 		
        <div class="form-group">
       <label class="control-label col-sm-2" for="Product Id">Product ID:</label>
       <div class="col-sm-3">
       <div class="input-group">
       <INPUT class="form-control" type="TEXT" size="30" MAXLENGTH=50 name="ITEM" id="ITEM" 	value="<%=itemno%>"
	   onkeypress="if((event.keyCode=='13') && ( document.form.ITEM.value.length > 0)){validateProduct();}">
   		 	<span class="input-group-addon"  onClick="javascript:popUpWin('../jsp/list/item_list_po.jsp?ITEM='+form.ITEM.value);">
   		 	<a href="#" data-toggle="tooltip" data-placement="top" title="Product Details">
   		 	<i  class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span>
  		</div>
  		</div>
  		<div class="form-inline">
  		<label class="control-label col-sm-2" for="Description">Description:</label>
        <div class="col-sm-3">
      	    <div class="input-group">
    		 <INPUT class="form-control" name="DESC" type="TEXT"	value="<%=StrUtils.forHTMLTag(itemdesc)%>" size="30" MAXLENGTH=80>
    		<span class="input-group-addon"  onClick="javascript:popUpWin('../jsp/list/item_list_po.jsp?ITEM='+form.ITEM.value+'&DESC='+form.DESC.value);">
   		 	<a href="#" data-toggle="tooltip" data-placement="top" title="Product Details">
   		 	<i class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span>
        </div>
        </div>
 		</div> 
 		</div>
 		
  		<div class="form-group">
 		<div class="col-sm-offset-4 col-sm-3">
        <label class="radio-inline"> 
		<INPUT name="PICKISSUE" type = "radio"  value="Pick"  id="Pick" <%if(PICKISSUE.equalsIgnoreCase("Pick")) {%>checked <%}%>  onClick="Disabletextbox();" ><B>Pick</B></label> 
        <label class="radio-inline">      
		<INPUT name="PICKISSUE" type = "radio"  value="Issue"  id="Issue"  <%if(PICKISSUE.equalsIgnoreCase("Issue")) {%>checked <%}%>  onClick="Disabletextbox();" ><b>Issue</b></label>
        <label class="radio-inline">      
		<INPUT name="PICKISSUE" type = "radio" value="Pick&Issue"  id = "Pick&Issue" <%if(PICKISSUE.equalsIgnoreCase("Pick&Issue")) {%>checked <%}%> onClick="Disabletextbox();" ><b>Pick&Issue</b></label>
        </div>
        <div class="form-inline">
    	<div class="col-sm-1">
        <button type="button" class="Submit btn btn-default" onClick="viewOutboundOrders();"><b>View</b></button>
        </div>
 		</div> 
 		</div>
 		
 		<div class="form-group">
 		<div class="col-sm-8">
        <label class="checkbox-inline"> 
		<INPUT Type=Checkbox  style="border:0;" name = "select" value="select" <%if(allChecked.equalsIgnoreCase("true")){%>checked <%}%>onclick="return checkAll(this.checked);">
        <b>Select/Unselect All</b></label>        
        <label class="checkbox-inline">      
		<INPUT Type=Checkbox  style="border:0;" name="fullIssue"  <%if(fullIssue.equalsIgnoreCase("true")){%>checked <%}%> value="fullIssue" onclick="return fullIssuing(this.checked)">
        <b>Full Pick/Issue/Pick&Issue</b></label>        
        </div>
        </div>
 		

 		<TABLE BORDER="0" CELLSPACING="0" WIDTH="100%" class="table">
        <thead style="background: #eaeafa; font-size: 15px">
        <tr>
         <th width="2%">Chk</th>
         <th width="9%">Order No</th>
         <th width="4%">Line No</th>
         <th width="11%">Custmor Name</th>
         <th width="10%">Product ID</th>
         <th width="12%">Description</th>
         <th width="7%">UOM</th>
         <th width="5%">Order Qty</th>
         <th width="5%">Picked Qty</th>
         <th width="5%">Issued Qty</th>
         <th width="8%">Issuing Qty</th>
         <th width="15%">Batch</th>
         <th width="3%">Status</th>
        </tr>
        </thead>
       <tbody style="font-size:  15px;">
       	<% 
                                    
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
          String cname= (String)m.get("custname");
          String item= (String)m.get("item");
          String desc= (String)m.get("itemdesc");
          String qtyor= (String)m.get("qtyor");
          String qtypick = (String)m.get("qtypick");
          String qtyis= (String)m.get("qtyis");
         // String batch=(String)m.get("batch");
          String lnstat= (String)m.get("lnstat");
          String uom= (String)m.get("UNITMO");
          String uomqty= (String)m.get("UOMQTY");
          chkString  =dono+","+dolnno+","+qtyor+","+qtypick+","+qtyis+","+StrUtils.replaceCharacters2Send(cname)+","+uomqty+","+uom;
          dno=dono+"_"+dolnno;
          if(checkedDOS!=null && result.equalsIgnoreCase("catchrerror")){
              value = (String)checkedDOS.get(dno);   
              
              if(value!=null)
              {
            	  issuingQty  = value.split(":")[0];
            	  batch  = value.split(":")[1];
            	 
              }
              }
      %>
					<TR bgcolor = "<%=bgcolor%>">
           
         
							
              <TD width="2%"  align="left"><font color="black"><INPUT Type=Checkbox  style="border:0;background=#dddddd" 
              name="chkdDoNo"  value="<%=chkString%>" <%if(value!=null){%>checked <%}%> onclick = "setIssuingQty('<%=dono%>_<%=dolnno%>',<%=i%>);"></font></TD>
              <TD align="center" width="9%"><%=dono%></TD>
              <TD align="center" width="4%"><%=dolnno%></TD>
              <TD align="left" width="11%"><%=cname%></TD>
              <TD align="left" width="10%"><%=item%></TD>
              <TD align="left" width="12%"><%=desc%></TD>
              <TD align="left" width="7%"><%=uom%></TD>
              <TD align="center" id = "QtyOrdered_<%=dono%>_<%=dolnno%>"width="5%"><%=StrUtils.formatNum((String)m.get("qtyor"))%></TD>
              <TD align="center" id = "QtyPicked_<%=dono%>_<%=dolnno%>" width="5%"><%=StrUtils.formatNum((String)m.get("qtypick"))%></TD>              
              <TD align="center" id = "QtyIssued_<%=dono%>_<%=dolnno%>" width="5%"><%=StrUtils.formatNum((String)m.get("qtyis"))%></TD>
              <TD align="center" width="8%">
						<input class="form-control" type="text" onkeypress="return isNumberKey(event,this,4)" name = "issuingQty_<%=dono%>_<%=dolnno%>" id = "issuingQty_<%=dono%>_<%=dolnno%>" 
						 size = "6" maxlength = "10"  value = <%=issuingQty%>>
			  </TD>
              <td width="15%" align="center"><div class="input-group"><input class="form-control" name="BATCH_<%=dono%>_<%=dolnno%>" id="BATCH_<%=dono%>_<%=dolnno%>" value = <%=batch%> type="TEXT" size="15"  >
              <!--<input class="form-control" name="BATCH_ID_<%=dono%>_<%=dolnno%>" id="BATCH_ID_<%=dono%>_<%=dolnno%>" value = <%=batch%> type="hidden" value="-1">-->
              <input type="hidden" name="BATCH_ID_<%=dono%>_<%=dolnno%>" value="-1" />
             	<a href="#" class="input-group-addon" data-toggle="tooltip" data-placement="top" title="Batch Details" onClick="javascript:popWinBatch('../jsp/OutBoundMultiPickingBatch.jsp?ITEMNO=<%=item%>&DONO=<%=dono%>&DOLNNO=<%=dolnno%>&LOC_0='+form.LOC_0.value+'&INDEX=0&TYPE=OBBYPROD&UOM=<%=(String)uom%>');">
				<i class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></div>
             </TD>
              <TD align="center" width="3%"><%=(String)m.get("lnstat")%></TD>
           </TR>
					 <%}} else {%>
       
             <TR> <TD align="center" colspan="12"> Data's Not Found For Issuing</TD></TR>
       <%}%>
       </tbody>
       </TABLE>
 	
 		<INPUT type="Hidden" name="ENCRYPT_FLAG" value="1">  
 		<INPUT type="Hidden" name="AFLAG" value="<%=AFLAG%>">
       <INPUT type="hidden" size="20" MAXLENGTH=20		name="RECFLAG" value="ByProduct">
 		
 		
 		<div class="form-group">
        <label class="control-label col-sm-1" for="Location">Location:</label>
        <div class="col-sm-3">
        <div class="input-group">
        <INPUT class="form-control" name="LOC_0" id="LOC_0" type="TEXT" size="30" value = '<%=loc%>' 
		onkeypress="if((event.keyCode=='13') && ( this.value.length > 0)){validateLocation(this.value,0);}"	MAXLENGTH=80>
        <span class="input-group-addon"  onClick="javascript:popUpWin('../jsp/loc_list_MultiReceivewms.jsp?INDEX=0&LOC='+form.LOC_0.value);">
   		 	<a href="#" data-toggle="tooltip" data-placement="top" title="Location Details">
   		 	<i class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span>
        </div>
        </div>
        <div class="form-inline">
        <label class="control-label col-sm-2 required" for="Trasaction Date">Transaction Date:</label>
        <div class="col-sm-3">
        <div class="input-group">      
        <%if(SETCURRENTDATE_PICKANDISSUE.equals("1")){%>    
       <INPUT class="form-control datepicker" name="TRANSACTIONDATE" id="TRANSACTIONDATE"	value="<%=TRANSACTIONDATE%>" type="TEXT" size="30" MAXLENGTH="80" readonly="readonly"> 
        <%}else {%>
        <INPUT class="form-control datepicker" name="TRANSACTIONDATE" id="TRANSACTIONDATE"	value="" type="TEXT" size="30" MAXLENGTH="80" readonly="readonly">
        <%}%>
        </div>
      	</div>
    	</div>	 	
 		</div>
 		<div class="form-group">
    	<label class="control-label col-sm-1" for="Remarks">Remarks:</label>
        <div class="col-sm-3">
        <INPUT class="form-control" name="REF" type="TEXT"  value="<%=REF%>" size="30" MAXLENGTH=100>
        </div>
        
 		<div class="form-inline">
    	<label class="control-label col-sm-2" for="GINO">GINO:</label>
        <div class="col-sm-3">
                <div class="input-group">
        <INPUT class="form-control" name="INVOICENO" id="INVOICENO" value="<%= gino %>" type="TEXT" onkeypress="return blockSpecialChar(event)" value="" style="width: 100%" MAXLENGTH=100>
        <span class="input-group-addon"  onClick="onNew()">
   		 	<a href="#" data-toggle="tooltip" data-placement="top" title="Auto-Generate">
   		 	<i class="glyphicon glyphicon-edit" style="font-size: 20px;"></i></a></span>
        </div>
        </div>
 		</div>			 			
 		</div>
 			
 		
    	<div class="form-group">        
      	<div class="col-sm-12" align="center">
<!--       	<button type="button" class="Submit btn btn-default" onClick="window.location.href='../home'"><b>Back</b></button>&nbsp;&nbsp; -->
      	<!-- <button type="button" class="Submit btn btn-default" onClick="window.location.href='outboundTransaction.jsp'"><b>Back</b></button>&nbsp;&nbsp; -->
      	<button type="button" class="Submit btn btn-default" onClick="onIssue(document.form)"><b>Issue Goods</b></button>&nbsp;&nbsp;
      	</div>
      	</div> 
      	
      	<INPUT type="hidden" name="TRAVELER" value="">
  		       
  		</form>
		</div>
		
		
		
<script type="text/javascript">
$(document).ready(function(){
	if(document.getElementById('Issue').checked) {		
		Disabletextbox();
        }
	});

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
			document.form.action="../salestransaction/orderpickissuebymultiple";
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
function validateProduct() {
	var productId = document.form.ITEM.value;
      
	if(document.form.ITEM.value=="" || document.form.ITEM.value.length==0 ) {
		alert("Enter Product ID!");
		document.form.ITEM.focus();
	}else{
		var urlStr = "/track/MiscOrderHandlingServlet";
		$.ajax( {
			type : "POST",
			url : urlStr,
			data : {
				ITEM : productId,
				PLANT : "<%=plant%>",
				ACTION : "VALIDATE_PRODUCT_GETDETAIL"
				},
				dataType : "json",
				success : function(data) {
					
					if (data.status == "100") {
						var resultVal = data.result;
						document.form.DESC.value = resultVal.discription;
                                                document.form.DETAILDESC.value = resultVal.detaildesc;
                                               
					} else {
						alert("Not a valid product or It is a parent item");
						document.form.ITEM.value = "";
						document.form.ITEM.focus();
					}
				}
			});
		}
	}
	</script>
	</div>
	</div>
	
<jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
</jsp:include>      
 