<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.dao.*"%>
<%@page import="com.track.constants.IDBConstants"%>
<%@ include file="header.jsp"%>
<%--New page design begin --%>
<%
String title = "Pick and Issue Reversal By Sales Order";
%>
<%@include file="sessionCheck.jsp" %>
<jsp:include page="header2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
</jsp:include>
<%--New page design end --%>

<jsp:useBean id="su" class="com.track.util.StrUtils" />
<%

String REMARKS = "",REASONCODE="",TRANSACTIONDATE="";
String fieldDesc="<tr><td>Please enter any search criteria</td></tr>";
session = request.getSession();
String plant = (String) session.getAttribute("PLANT");
String dono = su.fString(request.getParameter("DONO"));
REASONCODE = su.fString(request.getParameter("REASONCODE"));
String action = su.fString(request.getParameter("action")).trim();
String result   = su.fString(request.getParameter("result")).trim();
String sUserId = (String) session.getAttribute("LOGIN_USER");
session.setAttribute("RFLAG", "6");
String isData =    (String) session.getAttribute("AFLAG");
Map checkedDOS = (Map) request.getSession().getAttribute("checkedDOS");
boolean confirm = false;
DOUtil _DOUtil = new DOUtil();
DoHdrDAO _DoHdrDAO = new DoHdrDAO();
ItemMstDAO _ItemMstDAO = new ItemMstDAO();
DoDetDAO _DoDetDAO = new DoDetDAO();

_DOUtil.setmLogger(mLogger);
_DoHdrDAO.setmLogger(mLogger);
_ItemMstDAO.setmLogger(mLogger);

String chkString ="";
//String loc="";
String sSaveEnb    = "disabled";
String allChecked = "";
TRANSACTIONDATE =su.fString(request.getParameter("TRANSACTIONDATE"));
DateUtils _dateUtils = new DateUtils();
String curDate =_dateUtils.getDate();
if(TRANSACTIONDATE.length()<0|TRANSACTIONDATE==null||TRANSACTIONDATE.equalsIgnoreCase(""))TRANSACTIONDATE=curDate;

if (action.equalsIgnoreCase("View")) {
	Map m=(Map)request.getSession().getAttribute("podetVal");
    fieldDesc=(String)request.getSession().getAttribute("RESULT");
    fieldDesc="<font class='maingreen'>"+fieldDesc+"</font>";
    	if (m.size() > 0) {			
			//fromWareHouse = (String) m.get("fromwarehouse");
			//toWareHouse = (String) m.get("towarehouse");
			 

		} else {
			fieldDesc = "Details not found for Outbound order:" + dono;
			fieldDesc="<font class='mainred'>"+fieldDesc+"</font>";
		}
	}
if(result.equalsIgnoreCase("catcherror"))
{
	fieldDesc=(String)request.getSession().getAttribute("CATCHERROR");
    fieldDesc = "<font class='mainred'>" + fieldDesc + "</font>";
    allChecked = su.fString(request.getParameter("allChecked"));
 
 
}
%>
<!-- <html>
<head>
<title>Pick & Issue Reversal By Outbound Order</title>
<link rel="stylesheet" href="css/style.css">
<script src="js/jquery-1.4.2.js"></script> -->
<script src="js/json2.js"></script>
<script src="js/calendar.js"></script>
<script src="js/general.js"></script>
<script>
	var subWin = null;

	function popUpWin(URL) {
 		subWin = window.open(URL, 'OutboundOrderReversal', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=800,height=400,left = 200,top = 184');
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
		

	function submitForm(){
		 document.form.action ="/track/OrderIssuingServlet?action=OBISSUEReverseConfirm";
		   document.form.submit();
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
	 
	function onIssue(form){

		
	 	var Traveler ;
	 	var concatTraveler="";
	 	
	 	if (document.form.isData.value == null|| document.form.isData.value == ""|| document.form.isData.value != "DATA") {
			alert("No Data's Found For Reversal");
			return false;
		}

	 	var dono = document.form.DONO.value;

		if (dono == null || dono == "") {
			alert("Please select Order Number");
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
		    	 orderLNo = chkdvalue[0]+"_"+chkdvalue[1]+"_"+chkdvalue[2];
		    	 if(!verifyReverseQty(orderLNo))	
			    	  return false;

		    	
		     }
		
		     else {
			     if(document.form.chkdDoNo[i].checked){
			    	 checkFound = true;
			    	 orderLNo = chkdvalue[0]+"_"+chkdvalue[1]+"_"+chkdvalue[2];
			    	 if(!verifyReverseQty(orderLNo))	
				    	  return false;
			     }
			    
		     }
	          		
	        	     
	    }
		 if (checkFound != true) {
			    alert ("Please check at least one checkbox.");
			    return false;
			    }
		   
		   document.form.action ="/track/OrderIssuingServlet?action=OBISSUEReverseConfirm";
		   document.form.submit();
	  }
	
	function verifyReverseQty(orderLNo)
	{
		
		var issuedqty = document.getElementById("Qtyissued_"+(orderLNo)).value;
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
		//  test strString consists of valid characters listed above
		for (var i = 0; i < strString.length && blnResult == true; i++) {
			strChar = strString.charAt(i);
			if (strValidChars.indexOf(strChar) == -1) {
				blnResult = false;
			}
		}
		return blnResult;
	}			
	
</script>
<!-- </head>
 -->
	
<%--New page design begin --%>

<div class="container-fluid m-t-20">
	 <div class="box"> 
            <div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title"><%=title %></h1>
		</div>
		
 <div class="box-body">
<%--New page design end --%>

<center>
<h2><small> <%=fieldDesc%> </small></h2>
</center>

  <form class="form-horizontal" name="form" method="post" action="/track/OrderReceivingServlet?">
      <div class="form-group">
      <label class="control-label col-sm-4" for="Outbound Order no">Order Number:</label>
      <div class="col-sm-4">          
      <div class="input-group">
      <INPUT class="form-control" type="TEXT" size="30" MAXLENGTH="20" name="DONO" value="<%=dono%>" 
      onkeypress="if((event.keyCode=='13') && ( document.form.DONO.value.length > 0)){loadOutboundOrderDetails();}"/>
	  <span class="input-group-addon"  onClick="javascript:popUpWin('do_list_doreverse.jsp?DONO='+form.DONO.value+'&TYPE=OBISSUEREVERSE');"> 	
   	  <a href="#" data-toggle="tooltip" data-placement="top" title="Sales Details">
   	  <i class="glyphicon glyphicon-log-in" style="font-size: 20px"></i></a></span>
      </div>
      </div>
      <div class="form-inline">
      <div class="col-sm-1">
      <button type="button" class="Submit btn btn-default" onClick="viewOutboundOrders();"><b>View</b></button>
      </div>
      </div>
     <INPUT type = "hidden" name="CUST_NAME" value = "">
     <INPUT type = "hidden" name="JOB_NUM" value = "">
    <INPUT type = "hidden" name="LOGIN_USER" value = "<%=sUserId%>">
      </div>
      
        <div class="form-group">
        <div class="col-sm-12">    
        <label class="checkbox-inline">      
        <INPUT Type=Checkbox  style="border:0;" name = "select" value="select" <%if(allChecked.equalsIgnoreCase("true")){%>checked <%}%>onclick="return checkAll(this.checked);">
        <b>Select/Unselect All</b></label>
		</div>
        </div>
      <TABLE BORDER="0" class="table">
	  <thead style="background: #eaeafa; font-size: 15px;">				
					<tr>
						<th width="5%">Select</th>
						<th width="5%">Line No </th>
						<th width="10%">Product ID </th>
						<th width="20%">Description </th>
						<th width="10%">Uom </th>
						<th width="10%">Location</th>
						<th width="10%">Batch</th>
						<th width="10%">Order Qty </th>
						<th width="10%">Issued Qty </th>
						<th width="10%">Reverse Qty</th>
						
					</tr>
					</thead>
				<tbody style="font-size: 15px;">
					<%
						String returnQty = "",value = null,ITEM_BATCH = "NOBATCH";
						ArrayList al = _DOUtil.listOutboundSummaryReverseDODET(plant,dono);
						if(al.size()==0)
					    {
							isData="";
					    }
					      
						if (al.size() > 0) {
							isData="DATA";
							for (int i = 0; i < al.size(); i++) {
								Map m = (Map) al.get(i);
								int iIndex = i + 1;
								String bgcolor = ((i == 0) || (i % 2 == 0)) ? "#FFFFFF"	: "#dddddd";
								  dono = (String)m.get("dono");
						          String dolnno = (String)m.get("dolno");
								  String item= (String)m.get("item");
								  String desc= _ItemMstDAO.getItemDesc(plant ,item);
								  String loc = (String)m.get("loc");
						          String qtyor= (String)m.get("qtyor");
						          String qtyissue= (String)m.get("qtyissue");
						          String batch = (String)m.get("batch");
						          String UOMQTY = (String)m.get("UOMQTY");
						          String uom = (String)m.get("UNITMO");
						          chkString  =dolnno+","+loc+","+batch+","+item+","+qtyor+","+qtyissue+","+dono+","+UOMQTY;
						          String lineno=dolnno+"_"+loc+"_"+batch;
						        			        
						      %>
					<TR>

						<TD width="5%" align="left"><font color="black"><INPUT
							Type=checkbox style="border: 0;" name="chkdDoNo" 
							value="<%=chkString%>" ></font></TD>
						<TD width="5%" align="center"><%=dolnno%></TD>
						<TD align="left" width="10%"><%=item%></TD>
						<TD align="left" width="20%"><%=(String) desc%></TD>
						<TD align="left" width="10%"><%=(String) uom%></TD>
						<TD align="left" width="10%"><%=loc%></TD>
					
						 <TD align="center" width="10%"><input class="form-control" type="text" name = "batch_<%=lineno%>" id = "batch_<%=lineno%>" 
						 size = "15" style="border: 0px;background-color: '<%=bgcolor%>';" readonly  value = "<%=batch.trim()%>" ></input>
						</TD> 
						<TD align="center" width="10%"><input class="form-control" type="text" name = "Qtyordered_<%=lineno%>" id = "Qtyordered_<%=lineno%>" 
						 size = "6" maxlength = "10"   style="border: 0px; background-color: '<%=bgcolor%>';" readonly  value = <%=qtyor%>></input>
						</TD>
						<TD align="center" width="10%"><input class="form-control" type="text" name = "Qtyissued_<%=lineno%>" id = "Qtyissued_<%=lineno%>" 
						 size = "6" maxlength = "10" style="border: 0px;background-color: '<%=bgcolor%>';" readonly  value = <%=qtyissue%> ></input>
						</TD>
						<TD align="center" width="10%"><input class="form-control" type="text" name = "QtyReverse_<%=lineno%>" id = "QtyReverse_<%=lineno%>" 
						 size = "6" maxlength = "10" ></input>
						</TD>										
					</TR>

					<%
						}
						} else {
					%>

					<TR>
						<TD align="center" colspan="10">Data's Not Found To Reverse</TD>
					</TR>
					<%
						}
					%>
				</tbody>	
				</table>
			
				<INPUT	type="Hidden" name="RFLAG" value="6">
				<INPUT type="Hidden" name="isData" value="<%=isData%>">
				<INPUT type="hidden" name="TRAVELER" value="">	
				
		<div class="form-group">
        <label class="control-label col-sm-2" for="Transaction Date">Transaction Date:</label>
        <div class="col-sm-2">
        <div class="input-group">          
        <INPUT class="form-control datepicker" name="TRANSACTIONDATE" id="TRANSACTIONDATE" value="<%=TRANSACTIONDATE%>" type="TEXT" size="20" MAXLENGTH="80" readonly="readonly">
        </div>
    	</div>
    	<div class="form-inline">
    	<label class="control-label col-sm-1" for="Remarks">Remarks:</label>
        <div class="col-sm-2">
        <INPUT class="form-control" name="REMARKS" type="TEXT"  value="<%=REMARKS%>" size="20" MAXLENGTH=100>
        </div>
 		</div>
 		<div class="form-inline">
      	<label class="control-label col-sm-2" for="Reason code">Reason Code:</label>
      	<div class="col-sm-2">
      	<div class="input-group">    
    	<INPUT name="REASONCODE" type="TEXT" value="<% if(REASONCODE.length() > 0) { out.print(REASONCODE); }else{out.print("NOREASONCODE");}%>"
		class="form-control" size="30" readonly MAXLENGTH=80 > 
   		<span class="input-group-addon"	  onClick="javascript:popUpWin('miscreasoncode.jsp?ITEMNO=');"> 	
   		 	<a href="#" data-toggle="tooltip" data-placement="top" title="Reason Code Details">
   		 	<i class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span>
  		</div>
  		</div>
    	</div>
 		</div>
      <div class="form-group">        
      <div class="col-sm-12" align="center">
      <button type="button" class="Submit btn btn-default" onClick="window.location.href='../home'"><b>Back</b></button>&nbsp;&nbsp;
      <!-- <button type="button" class="Submit btn btn-default" onClick="window.location.href='outboundTransaction.jsp'"><b>Back</b></button>&nbsp;&nbsp; -->
      <button type="button" class="Submit btn btn-default" onClick="if(onIssue(document.form)) {submitForm();}"><b>Reverse</b></button>&nbsp;&nbsp;
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