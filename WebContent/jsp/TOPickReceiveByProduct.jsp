<%@ page import="com.track.dao.*"%>
<%@ page import="com.track.db.util.*"%>
<%@ include file="header.jsp"%>
<%@page import="com.track.constants.IDBConstants"%>
<%@page import="com.track.constants.IConstants"%>

<%--New page design begin --%>
<%
String title = "Transfer Order Pick/Issue (By ProductID) ";
%>
<%@include file="sessionCheck.jsp" %>
<jsp:include page="header2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
</jsp:include>
<%--New page design end --%>

<!-- <script src="js/jquery-1.4.2.js"></script> -->
<script src="js/calendar.js"></script>
<script src="js/json2.js"></script>
<script src="js/general.js"></script>
<!-- <title>Transfer Order Pick/Issue (By ProductID) </title>
<link rel="stylesheet" href="css/style.css"> -->
<script>
var subWin = null;
var chkdvalue = new Array(); 
var chkstring="";
function popUpWin(URL) {
 subWin = window.open(URL, 'Transferpickissuebyprodid', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=800,height=400,left = 200,top = 184');
}
function popUpWinForShipNo(URL) {
	subWinForshipno = window.open(URL, 'Shipno', 'toolbar=0,scrollbars=no,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=300,height=300,left = 300,top = 250');
}
function onIssue(form){

	
 	var Traveler ;
 	var concatTraveler="";
 	
 	if(document.form.AFLAG.value==null||document.form.AFLAG.value=="")
    {
   	   	alert("No Data's Found For Issue");
   	 	return false;
    }
 	
 	
    var checkFound = false;  
	 var orderLNo;
	 var len = document.form.chkdToNo.length; 
	 if(len == undefined) len = 1;
    for (var i = 0; i < len ; i++)
    {
    	if(len ==1 && document.form.chkdToNo.checked)
    	{
    		chkstring = document.form.chkdToNo.value;
    	}
    	else
    	{
    		chkstring = document.form.chkdToNo[i].value;
    	}
    	chkdvalue = chkstring.split(',');
		if(len == 1 && (!document.form.chkdToNo.checked))
		{
			checkFound = false;
		}
		
		else if(len ==1 && document.form.chkdToNo.checked)
	     {
	    	 checkFound = true;
	    	 document.form.TRAVELER.value=document.form.chkdToNo.value+"=";
	    	 orderLNo = chkdvalue[0]+"_"+chkdvalue[1];
	    	if(!verifyIssuingQty(orderLNo))	
		    	  return false;

	    	//if(!verifyshipmentno(orderLNo))	
		    	 // return false;
	     }
	
	     else {
		     if(document.form.chkdToNo[i].checked){
		    	 checkFound = true;
		    	 Traveler=document.form.chkdToNo[i].value;
	             concatTraveler=concatTraveler+Traveler+"=";
	             orderLNo = chkdvalue[0]+"_"+chkdvalue[1];
		    	 if(!verifyIssuingQty(orderLNo))	
			    	  return false;
		    	// if(!verifyshipmentno(orderLNo))	
			    	//  return false;
		     }
		     document.form.TRAVELER.value=concatTraveler; 
	     }
          		
        	     
    }
	 if (checkFound != true) {
		    alert ("Please check at least one checkbox.");
		    return false;
		    }
	   
	   document.form.action ="/track/TransferOrderServlet?Submit=BulkPickReceivebyProd";
	   document.form.submit();
  }

function verifyshipmentno(orderLNo)
{
	
	
		var shipmentno = document.getElementById("SHIPNO_"+(orderLNo)).value;
		if (shipmentno == "" || shipmentno.length == 0 || shipmentno==null ) {
			alert("Select Shipment Number");
			document.getElementById("SHIPNO_"+(orderLNo)).focus();
			document.getElementById("SHIPNO_"+(orderLNo)).select();
	        return false;
		}
		return true;
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
		
	if(issueQty > balanceQty)
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
	var len = document.form.chkdToNo.length;
	 var orderLNo; 
	 if(len == undefined) len = 1;  
    if (document.form.chkdToNo)
    {
        for (var i = 0; i < len ; i++)
        {      
              	if(len == 1){
              		document.form.chkdToNo.checked = isChk;
              		chkstring = document.form.chkdToNo.value;
              		 chkdvalue = chkstring.split(',');
              		 orderLNo = chkdvalue[0]+"_"+chkdvalue[1];
              	}
              	else{
              		document.form.chkdToNo[i].checked = isChk;
              		chkstring = document.form.chkdToNo[i].value;
              		chkdvalue = chkstring.split(',');
              		 orderLNo = chkdvalue[0]+"_"+chkdvalue[1];
              	}
            	setIssuingQty(orderLNo,i);       	
                
        }
    }
}
 
function fullIssuing(isChk)
{
	
	var len = document.form.chkdToNo.length;
	 var orderLNo; 
	 if(len == undefined) len = 1;  
    if (document.form.chkdToNo)
    {
        for (var i = 0; i < len ; i++)
        {      
              	if(len == 1 && document.form.chkdToNo.checked){
              		chkstring = document.form.chkdToNo.value;
             		 chkdvalue = chkstring.split(',');
             		 orderLNo = chkdvalue[0]+"_"+chkdvalue[1];
              		//orderLNo = document.form.chkdToNo.value;
              		setIssuingQty(orderLNo,i); 
              	}
              	else if(len == 1 && !document.form.chkdToNo.checked){
              		return;
              	}
              	else{
                  	if(document.form.chkdToNo[i].checked){
                  		chkstring = document.form.chkdToNo[i].value;
                  		chkdvalue = chkstring.split(',');
                  		 orderLNo = chkdvalue[0]+"_"+chkdvalue[1];
              		 //orderLNo = document.form.chkdToNo[i].value;
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
		if(document.form.fullIssue.checked)
		{
			setQty(orderLNo);
		}
	  
	}
		
	else if(len !=1 && document.form.chkdToNo[i].checked){
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

</script>
<%--New page design begin --%>

<div class="container-fluid m-t-20">
	 <div class="box"> 
            <div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title"><%=title %></h1>
		</div>
		
 <div class="box-body">
<%--New page design end --%>

<%-- <link rel="stylesheet" href="css/style.css">
<%@ include file="body.jsp"%> --%>
<jsp:useBean id="gn" class="com.track.gates.Generator" />

<jsp:useBean id="su" class="com.track.util.StrUtils" />
<jsp:useBean id="vmb" class="com.track.tables.VENDMST" />
<jsp:useBean id="dhb" class="com.track.tables.DOHDR" />
<jsp:useBean id="ddb" class="com.track.tables.DODET" />
<jsp:useBean id="du" class="com.track.util.DateUtils" />

<%
    TOUtil _toUtil=new TOUtil();
    ShipHisDAO shipDao = new ShipHisDAO(); 
    _toUtil.setmLogger(mLogger);
    ItemMstDAO itemdao = new ItemMstDAO();
    String itemno="",  itemdesc="",shipno="",TRANSACTIONDATE="";
    String action   = su.fString(request.getParameter("action")).trim();
    Map checkedDOS = (Map) request.getSession().getAttribute("checkedDOS");
    String result   = su.fString(request.getParameter("result")).trim();
    String plant = su.fString((String)session.getAttribute("PLANT"));
    String sUserId = (String) session.getAttribute("LOGIN_USER");
    String RFLAG=    (String) session.getAttribute("RFLAG");
    String AFLAG=    (String) session.getAttribute("AFLAG");
    itemno=su.fString(request.getParameter("ITEM"));
    itemdesc=su.replaceCharacters2Recv(su.fString(request.getParameter("DESC")));
    String uom=su.fString(request.getParameter("UOM"));
	TRANSACTIONDATE = su.fString(request.getParameter("TRANSACTIONDATE"));
    DateUtils _dateUtils = new DateUtils();
    String curDate =_dateUtils.getDate();
    if(TRANSACTIONDATE.length()<0|TRANSACTIONDATE==null||TRANSACTIONDATE.equalsIgnoreCase(""))TRANSACTIONDATE=curDate;
    String fullIssue = "",allChecked = "",loc = "",REF = "",tno="",chkString ="";
    String fieldDesc="<tr><td> Please enter any search criteria</td></tr>";
    ArrayList al=new ArrayList();
     if(action.equalsIgnoreCase("View")){
      itemdesc=itemdao.getItemDesc(plant,itemno);
     itemdesc = su.replaceCharacters2Send(itemdesc);
     itemdesc = su.replaceCharacters2Recv(itemdesc);

        al= _toUtil.listTransferSummaryByProd(itemno,plant);
        
      
    }
    if(result.equalsIgnoreCase("sucess"))
    {
     fieldDesc=su.fString((String)request.getSession().getAttribute("RESULT"));
    } 
    else if(result.equalsIgnoreCase("catchrerror"))
    {
      fieldDesc=su.fString((String)request.getSession().getAttribute("CATCHERROR"));
      fieldDesc = "<font class='mainred'>" + fieldDesc + "</font>";
      allChecked = su.fString(request.getParameter("allChecked"));
      fullIssue = su.fString(request.getParameter("fullReceive"));
   } 
  
    else if(result.equalsIgnoreCase("error"))
    {
      fieldDesc=su.fString((String)request.getSession().getAttribute("RESULTERROR"));
      fieldDesc = "<font class='mainred'>" + fieldDesc + "</font>";
      allChecked = su.fString(request.getParameter("allChecked"));
      fullIssue = su.fString(request.getParameter("fullReceive"));
   } 
    
    
          
  //}
 
%>

<form class="form-horizontal" name="form" method="post" action="/track/TransferOrderServlet?" >
 <INPUT type="Hidden" name="TYPE" value="pickrecvbyprd">
 <center>
   <h2><small><%=fieldDesc%></small></h2>
  </center>
 
   		<br>
       <div class="form-group">
       <label class="control-label col-sm-2" for="Product ID">Product ID:</label>
       <div class="col-sm-3">
       <div class="input-group">
       <INPUT class="form-control" type="TEXT" size="30" MAXLENGTH=50 name="ITEM" id="ITEM"	value="<%=itemno%>"
	   onkeypress="if((event.keyCode=='13') && ( document.form.ITEM.value.length > 0)){loadOutboundOrderDetails();}">
   		 	<span class="input-group-addon"  onClick="javascript:popUpWin('list/item_list_po.jsp?ITEM='+form.ITEM.value+'&DESC='+form.DESC.value);">
   		 	<a href="#" data-toggle="tooltip" data-placement="top" title="Product Details">
   		 	<i  class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span>
  		</div>
  		</div>
  		<div class="form-inline">
  		<label class="control-label col-sm-2" for="Description">Description:</label>
        <div class="col-sm-3">
        <div class="input-group">
        <INPUT class="form-control" name="DESC" type="TEXT" value="<%=StrUtils.forHTMLTag(itemdesc)%>" size="30" MAXLENGTH=80>
         	<span class="input-group-addon"  onClick="javascript:popUpWin('list/item_list_po.jsp?ITEM='+form.ITEM.value+'&DESC='+form.DESC.value);">
   		 	<a href="#" data-toggle="tooltip" data-placement="top" title="Product Details">
   		 	<i  class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span>
    	</div>
 		</div>
 		</div>
 		<div class="form-inline">
  		<div class="col-sm-1 ">   
  		<button type="button" class="Submit btn btn-default" onClick="viewTransferOrders();"><b>View</b></button>
  		</div>
  		</div>  
 		</div>
 		
 		  <INPUT  type="hidden"  name="UOM" value="" >
 		  <INPUT type="hidden" name="LOGIN_USER" value="<%=sUserId%>">
 		
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
         <thead style="background: #eaeafa; font-size: 15px">
         <tr>
          <th width="5%">Chk </th>
         <th width="12%">Order No </th>
         <th width="9%">Order Line No </th>
         <th width="12%">Customer Name</th>
         <th width="9%">From Location</th>
         <th width="9%">TO Location</th>
         <th width="8%">Order Qty</th>
         <th width="8%">Picked/Received Qty</th>
         <th width="8%">Picking/Receiving Qty</th>
         <th width="15%">Batch </th>
         <th width="5%">Status</th>
         </tr>
       	 </thead>
       	 <tbody style="font-size: 15px;">

				

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
          String tono = (String)m.get("tono");
          String tolnno = (String)m.get("tolnno");
          String cname= (String)m.get("cname");
          String item= (String)m.get("item");
          
          
       /*    shipno = shipDao.selectLatestShipNo(plant,dono);
          
        if(shipno.equalsIgnoreCase("NULL") || shipno.equalsIgnoreCase(""))
          {
        	  shipno=" ";
        	  
          }*/
        
          
       
          //getting item loc
            
          String qtyor= (String)m.get("qtyor");
          String qtypick = (String)m.get("qtypick");
          String lnstat= (String)m.get("pickstatus");
          String fromloc = (String)m.get("fromloc");
          String toloc = (String)m.get("toloc");
          chkString  =tono+","+tolnno+","+qtyor+","+qtypick+","+su.replaceCharacters2Send(cname)+","+fromloc+","+toloc;
          tno=tono+"_"+tolnno;
          if(checkedDOS!=null && result.equalsIgnoreCase("catchrerror")){
              value = (String)checkedDOS.get(tno);   
              
              if(value!=null)
              {
            	  issuingQty  = value.split(":")[0];
            	  batch  = value.split(":")[1];
            	 
              }
              }
      %>
					<TR bgcolor = "<%=bgcolor%>">
           
         
							
              <TD width="5%"  align="CENTER"><font color="black"><INPUT Type=Checkbox  style="border:0;background=#dddddd" 
              name="chkdToNo"  value="<%=chkString%>" <%if(value!=null){%>checked <%}%> onclick = "setIssuingQty('<%=tono%>_<%=tolnno%>',<%=i%>);"></font></TD>
              <TD align="center" width="12%"><%=(String)m.get("tono")%></TD>
              <TD align="center" width="9%"><%=(String)m.get("tolnno")%></TD>
              <TD align="left" width="12%"><%=(String)m.get("cname")%></TD>
              <TD align="left" width="9%"><%=(String)m.get("fromloc")%></TD>
              <TD align="left" width="9%"><%=(String)m.get("toloc")%></TD>
              <TD align="center" id = "QtyOrdered_<%=tono%>_<%=tolnno%>"width="8%"><%=StrUtils.formatNum((String)m.get("qtyor"))%></TD>              
              <TD align="center" id = "QtyIssued_<%=tono%>_<%=tolnno%>" width="8%"><%=StrUtils.formatNum((String)m.get("qtypick"))%></TD>
              <TD align="center" width="8%">
						<input class="form-control" type="text" name = "issuingQty_<%=tono%>_<%=tolnno%>" id = "issuingQty_<%=tono%>_<%=tolnno%>" 
						 size = "6" maxlength = "10"  value = <%=issuingQty%>>
						</TD>
             
             
             	<td width="15%" align="center"><INPUT class="form-control" name="BATCH_<%=tono%>_<%=tolnno%>" id="BATCH_<%=tono%>_<%=tolnno%>" value = <%=batch%> type="TEXT" size="10"  >
             	
            
              <TD align="center" width="5%"><%=(String)m.get("pickstatus")%></TD>
           </TR>
					 <%}} else {%>
       
             <TR> <TD align="center" colspan="11"> Data's Not Found For Issuing</TD></TR>
       <%}%>
       </tbody>
				</table>
								
		<INPUT type="Hidden" name="ENCRYPT_FLAG" value="1"> 
		<INPUT type="Hidden" name="RFLAG" value="5"> 
		<INPUT type="Hidden" name="AFLAG" value="<%=AFLAG%>">
       <INPUT type="hidden" size="20" MAXLENGTH=20	name="RECFLAG" value="ByProduct">
        
	  	<div class="form-group">
        <label class="control-label col-sm-2" for="Transaction Date">Transaction Date:</label>
        <div class="col-sm-3">
        <div class="input-group">          
        <INPUT class="form-control datepicker" name="TRANSACTIONDATE" id="TRANSACTIONDATE" value="<%=TRANSACTIONDATE%>" type="TEXT" size="30" MAXLENGTH="80" readonly="readonly">
        </div>
    	</div>
    	<div class="form-inline">
    	<label class="control-label col-sm-2" for="Remarks">Remarks:</label>
        <div class="col-sm-3">
        <INPUT class="form-control" name="REF" type="TEXT"  value="<%=REF%>" style="width: 100%" MAXLENGTH=100>
        </div>
 		</div>
 		</div>
 		
  		<div class="form-group">        
      	<div class="col-sm-12" align="center">
       	<button type="button" class="Submit btn btn-default" onClick="window.location.href='outboundTransaction.jsp'"><b>Back</b></button>&nbsp;&nbsp;
      	<button type="button" class="Submit btn btn-default" onClick="onIssue(document.form)"><b>Goods Issue/Receive</b></button>&nbsp;&nbsp;
      	<INPUT type="hidden" name="TRAVELER" value="">
      	</div>
        </div>
         		
  		</form>
		</div>



<script>

function generateShipno(orderno,lineno){
	
	var index = orderno+"_"+lineno;
	var currentShipLable = index;
	var urlStr = "/track/OutboundOrderHandlerServlet";
	$.ajax( {
	type : "POST",
	url : urlStr,
	data : {
		PLANT : "<%=plant%>",
		ACTION : "GENERATE_SHIPNO"
		},
		dataType : "json",
		success : function(data) {
				if (data.status == "100") {
						var resultVal = data.result;
						document.getElementById("SHIPNO_"+currentShipLable).value = resultVal.shipno;
			
					} else {
						alert("Unable to genarate SHipment No");
						document.getElementById("SHIPNO_"+currentShipLable).value = "";
					}
				}
			});
}

function callpopupshipNo(orderno,lineno)
{
	debugger;
	var dono = orderno;
	var dolnno = lineno;
	popUpWinForShipNo('do_list_shipmentno.jsp?DONO='+dono+'&DOLNNO='+dolnno+'&TYPE=OBBYPROD');
	
}

function viewTransferOrders(){
			document.form.action="/track/TransferOrderServlet?Submit=ViewProductOrders";
            document.form.submit();
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

function loadOutboundOrderDetails() {
	var productId = document.form.ITEM.value;
	if(productId=="" || productId.length==0 ) {
		alert("Enter Product ID!");
		document.getElementById("ITEM").focus();
	}else{
	var urlStr = "/track/MiscOrderHandlingServlet";
	$.ajax( {
		type : "POST",
		url : urlStr,
		data : {
		ITEM : productId,
		PLANT : "<%=plant%>",
		ACTION : "VALIDATE_PRODUCT"
					},
					dataType : "json",
					success : function(data) {
						if (data.status == "100") {
							var resultVal = data.result;
							
							document.form.DESC.value = resultVal.discription;
							document.form.action = "/track/TransferOrderServlet?Submit=ViewProductOrders";
							document.form.submit();

						} else {
							alert("Not a valid product!");
							document.form.ITEM.value = "";
							document.form.DESC.value = "";
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