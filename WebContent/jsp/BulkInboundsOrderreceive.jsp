<%@ page import="com.track.dao.*"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="java.util.*"%>
<%@ page import="com.track.util.*"%>
<%@ page import="com.track.gates.*"%>
<%@ page import="com.track.gates.DbBean"%>
<%@ page import="com.track.gates.Generator"%>
<%@ page import="com.track.constants.*"%>
<%@ include file="header.jsp"%>
<%! @SuppressWarnings({"rawtypes"}) %>
<jsp:useBean id="gn" class="com.track.gates.Generator" />
<jsp:useBean id="dhb" class="com.track.tables.DOHDR" />
<jsp:useBean id="ddb" class="com.track.tables.DODET" />
<%--New page design begin --%>
<%
String title = "Goods Receipt(Multiple) by Purchase Order ";
String plant = StrUtils.fString((String)session.getAttribute("PLANT"));

%>
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
	}</style>
<%--New page design end --%>

<!-- <html>
<script type="text/javascript" src="js/jquery-1.4.2.js"></script> -->
<script type="text/javascript" src="../jsp/js/calendar.js"></script>
<script type="text/javascript" src="../jsp/js/json2.js"></script>
<script type="text/javascript" src="../jsp/js/general.js"></script>
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
 		subWin = window.open(URL, 'Outboundpickissuebyprodid', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=700,height=400,left = 200,top = 184');
		}
}
function onReceive(form){
 	
 	var Traveler ;
 	var concatTraveler="";
 	
 	if(document.form.AFLAG.value==null||document.form.AFLAG.value=="")
    {
   	   	alert("No Data's Found For Issue");
   	 	return false;
    }
 	
 	if(document.form.TRANSACTIONDATE.value==null||document.form.TRANSACTIONDATE.value=="")
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
	 var len = document.form.chkdPoNo.length; 
	 if(len == undefined) len = 1;
    for (var i = 0; i < len ; i++)
    {
    	if(len ==1 && document.form.chkdPoNo.checked)
    	{
    		chkstring = document.form.chkdPoNo.value;
    	}
    	else
    	{
    		chkstring = document.form.chkdPoNo[i].value;
    	}
    	chkdvalue = chkstring.split(',');
		if(len == 1 && (!document.form.chkdPoNo.checked))
		{
			checkFound = false;
		}
		
		else if(len ==1 && document.form.chkdPoNo.checked)
	     {
	    	 checkFound = true;
	    	 document.form.TRAVELER.value=document.form.chkdPoNo.value+"=";
	    	 orderLNo = chkdvalue[0]+"_"+chkdvalue[1];
	    	if(!verifyRecvQty(orderLNo))	
		    	  return false;
	     }
	
	     else {
		     if(document.form.chkdPoNo[i].checked){
		    	 checkFound = true;
		    	 Traveler=document.form.chkdPoNo[i].value;
	             concatTraveler=concatTraveler+Traveler+"=";
	             orderLNo = chkdvalue[0]+"_"+chkdvalue[1];
		    	 if(!verifyRecvQty(orderLNo))	
			    	  return false;
		     }
		     document.form.TRAVELER.value=concatTraveler; 
	     }
          		
        	     
    }
	 if (checkFound != true) {
		    alert ("Please check at least one checkbox.");
		    return false;
		    }
	 var grnval = document.form.GRNO.value.length;
	 if(grnval>0)
	 {
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
						document.form.action ="/track/OrderReceivingByPOServlet?action=BulkReceivebyOrders";
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
		 if (confirm('Are you sure to submit this transaction without GRNO?')) {
			 document.form.action ="/track/OrderReceivingByPOServlet?action=BulkReceivebyOrders";
			   document.form.submit();
		 } else {
			 document.getElementById("GRNO").focus();
		 }		 
		 }
  }

function verifyRecvQty(orderLNo)
{

		var recvQty = document.getElementById("receivingQTY_"+(orderLNo)).value;
		if (recvQty == "" || recvQty.length == 0 || recvQty == '0') {
			alert("Enter a valid Quantity!");
			document.getElementById("receivingQTY_"+(orderLNo)).focus();
			document.getElementById("receivingQTY_"+(orderLNo)).select();
	        return false;
		}
		if(!isNumericInput(recvQty)){
			alert("Entered Quantity is not a valid number!");
			document.getElementById("receivingQTY_"+(orderLNo)).focus();
			document.getElementById("receivingQTY_"+(orderLNo)).select();
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
		{
			alert("Exceeded the Ordered Qty of LineOrderNO:: "+orderLNo);
			document.getElementById("receivingQTY_"+(orderLNo)).focus();
			document.getElementById("receivingQTY_"+(orderLNo)).select();
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
	
	var len = document.form.chkdPoNo.length;
	 var orderLNo; 
	 if(len == undefined) len = 1;  
    if (document.form.chkdPoNo)
    {
        for (var i = 0; i < len ; i++)
        {      
              	if(len == 1){
              		document.form.chkdPoNo.checked = isChk;
              		chkstring = document.form.chkdPoNo.value;
              		 chkdvalue = chkstring.split(',');
              		 orderLNo = chkdvalue[0]+"_"+chkdvalue[1];
              	}
              	else{
              		document.form.chkdPoNo[i].checked = isChk;
              		chkstring = document.form.chkdPoNo[i].value;
              		chkdvalue = chkstring.split(',');
              		 orderLNo = chkdvalue[0]+"_"+chkdvalue[1];
              	}
            	setReceivingQty(orderLNo,i);       	
                
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
              		chkstring = document.form.chkdPoNo.value;
             		 chkdvalue = chkstring.split(',');
             		 orderLNo = chkdvalue[0]+"_"+chkdvalue[1];
              		//orderLNo = document.form.chkdPoNo.value;
              		setReceivingQty(orderLNo,i); 
              	}
              	else if(len == 1 && !document.form.chkdPoNo.checked){
              		return;
              	}
              	else{
                  	if(document.form.chkdPoNo[i].checked){
                  		chkstring = document.form.chkdPoNo[i].value;
                  		chkdvalue = chkstring.split(',');
                  		 orderLNo = chkdvalue[0]+"_"+chkdvalue[1];
              		 //orderLNo = document.form.chkdPoNo[i].value;
              			setReceivingQty(orderLNo,i); 
                  	}
              	}
            	      	
                
        }
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
			document.getElementById("receivingQTY_"+orderLNo).value = 0;
		}
	}
	else{
		document.getElementById("receivingQTY_"+orderLNo).value = 0;	
	}
	
}

function setQty(orderLNo){
	
	var QtyOrdered = document.getElementById("QtyOrdered_"+orderLNo).innerHTML;
	QtyOrdered = removeCommas(QtyOrdered);
	var QtyReceived = document.getElementById("QtyReceived_"+orderLNo).innerHTML;
	QtyReceived = removeCommas(QtyReceived);
	var QtyReceiving = QtyOrdered - QtyReceived;
	QtyReceiving = parseFloat(QtyReceiving).toFixed(3);
	document.getElementById("receivingQTY_"+orderLNo).value = QtyReceiving;	
}


</script>

<%--New page design begin --%>

<div class="container-fluid m-t-20">
	 <div class="box"> 
	 <!-- Muruganantham Modified on 16.02.2022 -->
            <ul class="breadcrumb backpageul" >      	
                <li><a href="../home"><span class="underline-on-hover">Dashboard</span> </a></li>
                <li><a href="../purchaseTransactionDashboard"><span class="underline-on-hover">Purchase Transaction Dashboard</span> </a></li>
                <li>Goods Receipt(Multiple) by Purchase Order</li>                                   
            </ul>             
     <!-- Muruganantham Modified on 16.02.2022 -->
            <div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title"><%=title %></h1>
                <h1 style="font-size: 18px; cursor: pointer;" class="box-title pull-right" onclick="window.location.href='../purchaseTransactionDashboard'">
			  <i class="glyphicon glyphicon-remove"></i>
		 	  </h1>
		</div>
		
 <div class="box-body">
<%--New page design end --%>

<%-- <title>Goods Receipt by Inbound Order</title>
<link rel="stylesheet" href="css/style.css">
<%@ include file="body.jsp"%> --%>


<%
    POUtil _poUtil=new POUtil();
    _poUtil.setmLogger(mLogger);
    ItemMstDAO itemdao = new ItemMstDAO();
    String itemno="",  itemdesc="",TRANSACTIONDATE="",FROM_DATE="",TO_DATE="",fdate="",tdate="",CUSTOMER="",orderno="",loc="",EXPIREDATE="";

    String action   = StrUtils.fString(request.getParameter("action")).trim();
    Map checkedPOS = (Map) request.getSession().getAttribute("checkedPOS");
    String result   = StrUtils.fString(request.getParameter("result")).trim();
    
    String sUserId = (String) session.getAttribute("LOGIN_USER");
    
    String userId = (String) session.getAttribute("LOGIN_USER");
    com.track.dao.TblControlDAO _TblControlDAO=new   com.track.dao.TblControlDAO();
    String grno = _TblControlDAO.getNextOrder(plant,userId,"GRN");
    
    String RFLAG=    (String) session.getAttribute("RFLAG");
    String AFLAG=    (String) session.getAttribute("AFLAG");
    FROM_DATE     = StrUtils.fString(request.getParameter("FROM_DATE"));
    TO_DATE   = StrUtils.fString(request.getParameter("TO_DATE"));
    CUSTOMER      = StrUtils.fString(request.getParameter("CUSTOMER"));
    orderno     = StrUtils.fString(request.getParameter("PONO"));
    itemno=StrUtils.fString(request.getParameter("ITEM"));
    itemdesc=StrUtils.replaceCharacters2Recv(StrUtils.fString(request.getParameter("DESC")));
    loc      = StrUtils.fString(request.getParameter("LOC"));
    EXPIREDATE = StrUtils.fString(request.getParameter("EXPIREDATE"));
    String SETCURRENTDATE_GOODSRECEIPT = new PlantMstDAO().getSETCURRENTDATE_GOODSRECEIPT(plant);//Thanzith
    
    if(FROM_DATE==null) FROM_DATE=""; else FROM_DATE = FROM_DATE.trim();
    String curDate =DateUtils.getDate();
    if(FROM_DATE.length()<0||FROM_DATE==null||FROM_DATE.equalsIgnoreCase(""))FROM_DATE=curDate;

    if (FROM_DATE.length()>5)

    fdate    = FROM_DATE.substring(6)+"-"+ FROM_DATE.substring(3,5)+"-"+FROM_DATE.substring(0,2);



    if(TO_DATE==null) TO_DATE=""; else TO_DATE = TO_DATE.trim();
    if (TO_DATE.length()>5)
    tdate    = TO_DATE.substring(6)+"-"+ TO_DATE.substring(3,5)+"-"+TO_DATE.substring(0,2);

    
    String fullReceive = "",allChecked = "",REF = "",pno="",chkString ="";
    String fieldDesc="";
    //String fieldDesc="<tr><td> Please enter any search criteria</td></tr>";
 	TRANSACTIONDATE = StrUtils.fString(request.getParameter("TRANSACTIONDATE"));
 	 	    
    if(TRANSACTIONDATE.length()<0|TRANSACTIONDATE==null||TRANSACTIONDATE.equalsIgnoreCase(""))TRANSACTIONDATE=curDate;
    ArrayList al=new ArrayList();
     if(action.equalsIgnoreCase("View")){
    
        //al= _poUtil.listInboundSummaryBymultiple(plant,fdate,tdate,CUSTOMER,orderno,itemno);
        al= _poUtil.listInboundSummaryBymultipleWithext(plant,fdate,tdate,CUSTOMER,orderno,itemno);  //not by Draft - azees
     
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
      fullReceive = StrUtils.fString(request.getParameter("fullReceive"));
   } 
  
    else if(result.equalsIgnoreCase("error"))
    {
      fieldDesc=StrUtils.fString((String)request.getSession().getAttribute("RESULTERROR"));
      fieldDesc = "<font class='mainred'>" + fieldDesc + "</font>";
      allChecked = StrUtils.fString(request.getParameter("allChecked"));
      fullReceive = StrUtils.fString(request.getParameter("fullReceive"));
   } 
  
          
  //}
 
%>

<form class="form-horizontal" name="form" method="post" action="/track/OrderIssuingServlet?">
      <span style="text-align: center;">
	<h2><small class="success-msg"> <%=fieldDesc%></small></h2>
</span>
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
       <label class="control-label col-sm-2" for="Supplier Name">Supplier Name/ID:</label>
       <div class="col-sm-3">
      	    <div class="input-group">
    		<INPUT class="form-control" name="CUSTOMER" type = "TEXT" value="<%=CUSTOMER%>" size="30"  MAXLENGTH=100>
   		 	<span class="input-group-addon"  onClick="javascript:popUpWin('../jsp/supplierlist.jsp?CUSTOMER='+form.CUSTOMER.value+'&TYPE=MULTIPLEIB');">
   		 	<a href="#" data-toggle="tooltip" data-placement="top" title="Supplier Details">
   		 	<i  class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span>
  		</div>
  		</div>
  		<div class="form-inline">
  		<label class="control-label col-sm-2" for="Inbound Order">Order Number:</label>
        <div class="col-sm-3">
      	    <div class="input-group">
    		 <INPUT class="form-control" type="TEXT" size="30" MAXLENGTH="20" name="PONO" value="<%=orderno%>"  > 
    		<span class="input-group-addon"  onClick="javascript:popUpWin('../jsp/po_list_po.jsp?PONO='+form.PONO.value+'&OpenForReceipt=yes');">
   		 	<a href="#" data-toggle="tooltip" data-placement="top" title="Purchase Details">
   		 	<i class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span>
        </div>
        </div>
 		</div>  
 		</div>
 		
 		<INPUT type = "hidden" name="JOB_NUM" value = "">
        <INPUT type = "hidden" name="CUST_NAME" value = "">
        <INPUT type = "hidden" name="CUST_CODE" value = "">
        <INPUT type = "hidden" name="CUST_CODE1" value = "">
 		
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
 		<div class="form-inline">
    	<div class="col-sm-1">
        <button type="button" class="Submit btn btn-default" onClick="viewOutboundOrders();"><b>View</b></button>
        </div>
 		</div> 
 		</div>
 		
  		
 		<div class="col-sm-12">
        <label class="checkbox-inline"> 
		<INPUT Type=Checkbox  style="border:0;" name = "select" value="select" <%if(allChecked.equalsIgnoreCase("true")){%>checked <%}%>onclick="return checkAll(this.checked);">
		<b>Select/Unselect All</b></label> 
        <label class="checkbox-inline">      
		<INPUT Type=Checkbox  style="border:0;" name="fullReceive"  <%if(fullReceive.equalsIgnoreCase("true")){%>checked <%}%> value="fullReceive" onclick="return fullReceiving(this.checked)">
        <b>Full Receive</b></label>
        </div>
 		
 		

 		<TABLE BORDER="0" CELLSPACING="0" WIDTH="100%" class="table">
        <thead style="background: #eaeafa; font-size: 15px">
        <tr>
         <th width="3%">CHK </th>
         <th width="9%">ORDER NO </th>
         <th width="5%">LINE NO </th>
         <th width="12%">SUPPLIER NAME</th>
         <th width="11%">PRODUCT ID</th>
         <th width="12%">DESCRIPTION</th>
         <th width="8%">UOM</th>
         <th width="7%">ORDER QTY </th>
         <th width="8%">RECEIVED QTY </th>
         <th width="8%">RECEIVING QTY </th>
         <th width="15%">BATCH </th>
         <th width="5%">STATUS </th>
        </tr>
        </thead>
       <tbody style="font-size:  15px;">
       <% 
                                    
		String receivingQty = "",value = null,batch = "NOBATCH";
       if(al.size()==0)
       {
    	   AFLAG="";
       }
      
       if(al.size()>0)
       {
        AFLAG="DATA";
        
       for(int i=0 ; i<al.size();i++)
       {
    	   receivingQty = "";
    	   batch = "NOBATCH";
          Map m=(Map)al.get(i);
          int iIndex = i + 1;
          String bgcolor = ((i == 0) || (i % 2 == 0)) ? "#FFFFFF"  : "#FFFFFF";
          String pono = (String)m.get("pono");
          String polnno = (String)m.get("polnno");
          String cname= (String)m.get("custname");
          String item= (String)m.get("item");
          String desc= (String)m.get("itemdesc");
          String qtyor= (String)m.get("qtyor");
          String qtyrc = (String)m.get("qtyrc");
          String lnstat= (String)m.get("lnstat");
          String uom= (String)m.get("uom");
          String uomqty = (String)m.get("UOMQTY");
          chkString  =pono+","+polnno+","+qtyor+","+qtyrc+","+StrUtils.replaceCharacters2Send(cname)+","+uom+","+uomqty;
          pno=pono+"_"+polnno;
          if(checkedPOS!=null && result.equalsIgnoreCase("catchrerror")){
              value = (String)checkedPOS.get(pno);   
              
              if(value!=null)
              {
            	  receivingQty  = value.split(":")[0];
            	  batch  = value.split(":")[1];
            	 
              }
              }
      %>
					<TR bgcolor = "<%=bgcolor%>">
           
         
							
              <TD width="3%"  align="left"><font color="black"><INPUT Type=Checkbox  style="border:0;background=#dddddd" 
              name="chkdPoNo" value="<%=chkString%>" <%if(value!=null){%>checked <%}%> onclick = "setReceivingQty('<%=pono%>_<%=polnno%>',<%=i%>);"></font></TD>
              <TD align="center" width="9%"><%=pono%></TD>
              <TD align="center" width="5%"><%=polnno%></TD>
              <TD align="left" width="12%"><%=cname%></TD>
              <TD align="left" width="11%"><%=item%></TD>
              <TD align="left" width="12%"><%=desc%></TD>
              <TD align="left" width="8%"><%=uom%></TD>
              <TD align="center" id = "QtyOrdered_<%=pono%>_<%=polnno%>" width="7%"><%=StrUtils.formatNum((String)m.get("qtyor"))%></TD>
              <TD align="center" id = "QtyReceived_<%=pono%>_<%=polnno%>"  width="8%"><%=StrUtils.formatNum((String)m.get("qtyrc"))%></TD>              
              <TD align="center" width="8%">
						<input class="form-control" type="text" name = "receivingQTY_<%=pono%>_<%=polnno%>" id = "receivingQTY_<%=pono%>_<%=polnno%>" 
						 size = "6" maxlength = "10"  value = <%=receivingQty%>>
			  </TD>
			  <td width="15%" align="center"><div class="input-group"><INPUT class="form-control" name="BATCH_<%=pono%>_<%=polnno%>" id="BATCH_<%=pono%>_<%=polnno%>" value = <%=batch%> type="TEXT" size="10" MAXLENGTH=40>
						<a href="#" class="input-group-addon" data-toggle="tooltip"  onclick="generateBatch('<%=pono%>_<%=polnno%>');" data-placement="top" title="Generate">
   		 	<i class="glyphicon glyphicon-edit"></i></a></div>
						</td>
						
              <TD align="center" width="5%"><%=(String)m.get("lnstat")%></TD>
           </TR>
					 <%}} else {%>
       
             <TR> <td colspan="11" align="center"> Data's Not Found For Issuing</TD></TR>
       <%}%>
 		</tbody>
 		</TABLE>
 		
 		<INPUT type="Hidden" name="ENCRYPT_FLAG" value="1">  
 		<INPUT type="Hidden" name="AFLAG" value="<%=AFLAG%>">
        <INPUT type="hidden" size="20" MAXLENGTH=20	name="RECFLAG" value="ByProduct">
 		
 		<div class="form-group">
        <label class="control-label col-sm-2" for="Location">Location:</label>
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
    	<label class="control-label col-sm-2" for="Remarks">Remarks:</label>
        <div class="col-sm-3">
        <INPUT class="form-control" name="REF" type="TEXT"  value="<%=REF%>"	style="width: 100%" MAXLENGTH=100>
        </div>
 		</div>
 		</div>
 		
 		<div class="form-group">
 		<label class="control-label col-sm-2" for="Transaction Date">
   		 	<i class="glyphicon glyphicon-star" style="font-size: 10px; top:-6px; color: #e50000"></i>&nbsp;Transaction Date:</label>
<!--         <label class="control-label col-sm-2 required" for="Trasaction Date">Transaction Date:</label> -->
        <div class="col-sm-3">
        <div class="input-group">  
        <%if(SETCURRENTDATE_GOODSRECEIPT.equals("1")){%>        
         <INPUT class="form-control datepicker" name="TRANSACTIONDATE" id="TRANSACTIONDATE"	value="<%=TRANSACTIONDATE%>" type="TEXT" size="30" MAXLENGTH="80" readonly="readonly"> 
         <%}else {%>
        <INPUT class="form-control datepicker" name="TRANSACTIONDATE" id="TRANSACTIONDATE"	value="" type="TEXT" size="30" MAXLENGTH="80" readonly="readonly">
        <%}%>
        </div>
      	</div>
    	<div class="form-inline">
    	<label class="control-label col-sm-2" for="Expiry Date">Expiry Date:</label>
        <div class="col-sm-3">
        <div class="input-group">          
        <INPUT class="form-control datepicker" name="EXPIREDATE_0" id="EXPIREDATE_0" value="<%= EXPIREDATE%>" type="TEXT" size="30" MAXLENGTH="80" readonly="readonly">
        </div>
      	</div>
 		</div>
 		</div>	 		
 		<div class="form-group">
      <label class="control-label col-sm-2" for="GRNO">GRNO:</label>
        <div class="col-sm-3">
                <div class="input-group">
        <input name="GRNO" id="GRNO" value="<%= grno %>" class="form-control" type="TEXT" value="" style="width: 100%" MAXLENGTH=80 />
        <span class="input-group-addon"  onClick="onNew()">
   		 	<a href="#" data-toggle="tooltip" data-placement="top" title="Auto-Generate">
   		 	<i class="glyphicon glyphicon-edit" style="font-size: 20px;"></i></a></span>
   		 	</div>
        </div>
      </div>
    	<div class="form-group">        
      	<div class="col-sm-12" align="center">
    <!--  	<button type="button" class="Submit btn btn-default" onClick="window.location.href='../home'"><b>Back</b></button>&nbsp;&nbsp; -->
      	<!-- <button type="button" class="Submit btn btn-default" onClick="window.location.href='inboundTransaction.jsp'"><b>Back</b></button>&nbsp;&nbsp; -->
      	<button type="button" class="Submit btn btn-default" onClick="onReceive(document.form)"><b>Goods Receive</b></button>&nbsp;&nbsp;
      	</div>
      	</div> 
      	
      	<INPUT type="hidden" name="TRAVELER" value="">
		<INPUT type = "hidden" name="LOGIN_USER" value = "<%=sUserId%>">
  		       
  		</form>
		</div> 
		
		
		

<script>
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
function viewOutboundOrders(){
			document.form.action="../purchasetransaction/receiptsummarybymultiple";
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
function generateBatch(index){
	debugger;
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