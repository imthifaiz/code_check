<%@ page import="com.track.util.*"%>
<%@ page import="com.track.dao.*"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.constants.*"%>
<%@ include file="header.jsp"%>
<jsp:useBean id="gn" class="com.track.gates.Generator" />
<jsp:useBean id="sl" class="com.track.gates.selectBean" />
<jsp:useBean id="db" class="com.track.gates.defaultsBean" />
<jsp:useBean id="su" class="com.track.util.StrUtils" />
<jsp:useBean id="vmb" class="com.track.tables.VENDMST" />
<jsp:useBean id="phb" class="com.track.tables.POHDR" />
<jsp:useBean id="pdb" class="com.track.tables.PODET" />
<jsp:useBean id="du" class="com.track.util.DateUtils" />
<%
       
     
       StrUtils strUtils=new StrUtils();
       POUtil itemUtil = new POUtil();
       session = request.getSession();
       String plant = session.getAttribute("PLANT").toString();
       String action   = StrUtils.fString(request.getParameter("action")).trim();
       String sUserId = (String) session.getAttribute("LOGIN_USER");
       String pono     = StrUtils.fString(request.getParameter("PONO"));
      
       String  fieldDesc="";
       String   ORDERNO    = "",ORDERLNO="",CUSTNAME = "", ITEMNO   = "", ITEMDESC  = "",
       LOC   = "" , BATCH  = "", REF   = "",
       ORDERQTY = "", RECEIVEDQTY="",RECEIVEQTY="",EXPIREDATE="", BALANCEQTY="",CONTACTNAME="",TELNO="",EMAIL="",ADD1=""
	   ,ADD2="",ADD3="",TRANSACTIONDATE="";
       ORDERNO = StrUtils.fString(request.getParameter("ORDERNO"));
       ORDERLNO=StrUtils.fString(request.getParameter("ORDERLNO"));
       CUSTNAME = strUtils.replaceCharacters2Recv(StrUtils.fString(request.getParameter("CUSTNAME")));
       ITEMNO = StrUtils.fString(request.getParameter("ITEMNO"));
       ITEMDESC = strUtils.replaceCharacters2Recv(StrUtils.fString(request.getParameter("ITEMDESC")));
       LOC = StrUtils.fString(request.getParameter("LOC"));
       BATCH = StrUtils.fString(request.getParameter("BATCH"));
       EXPIREDATE = StrUtils.fString(request.getParameter("EXPIREDATE"));
	   TRANSACTIONDATE = StrUtils.fString(request.getParameter("TRANSACTIONDATE"));
       DateUtils _dateUtils = new DateUtils();
       String curDate =_dateUtils.getDate();
       if(TRANSACTIONDATE.length()<0|TRANSACTIONDATE==null||TRANSACTIONDATE.equalsIgnoreCase(""))TRANSACTIONDATE=curDate;
        //Get podet
       PoDetDAO  _PoDetDAO  = new PoDetDAO();  
       _PoDetDAO.setmLogger(mLogger);
     List listQry =  _PoDetDAO.getInboundItemListByWMS(plant,ORDERNO,ITEMNO,ORDERLNO);
       for(int i =0; i<listQry.size(); i++) {
          Map m=(Map)listQry.get(i);
          
          ORDERQTY =StrUtils.formatNum((String)m.get("qtyor"));
          RECEIVEDQTY =StrUtils.formatNum((String)m.get("qtyrc"));
          BALANCEQTY=(String)m.get("balanceqty");
          REF= (String)m.get("ref");
       }
        //Get end
       
       //Get Supplier Details
          Hashtable ht=new Hashtable();
         String extCond="";
         ht.put("PLANT",plant);
         ht.put("PONO",ORDERNO);
    
  
        if(ORDERNO.length()>0) extCond="a.plant='"+plant+"' and a.pono = '"+ORDERNO+"' and a.custname=b.vname";
         extCond=extCond+" order by a.pono desc";
         itemUtil.setmLogger(mLogger);
         ArrayList listQry1 = itemUtil.getPoHdrDetailsReceiving("isnull(b.name,'') as contactname,isnull(b.telno,'') as telno,isnull(b.email,'') as email,isnull(b.addr1,'') as add1,isnull(b.addr2,'') as add2,isnull(b.addr3,'') as add3",ht,extCond);
         for(int i =0; i<listQry1.size(); i++) {
            Map m1=(Map)listQry1.get(i);
            CONTACTNAME  =  (String)m1.get("contactname");
            TELNO  =  (String)m1.get("telno");
            EMAIL  =  (String)m1.get("email");
            ADD1  =  (String)m1.get("add1");
            ADD2  =  (String)m1.get("add2");
            ADD3 =  (String)m1.get("add3");
       }
     
   
      if(action.equalsIgnoreCase("result"))
      {
 
       fieldDesc=(String)request.getSession().getAttribute("RESULT");
       fieldDesc="<font class='maingreen'>"+fieldDesc+"</font>";
      }
      else if(action.equalsIgnoreCase("resulterror"))
      {
 
       fieldDesc=(String)request.getSession().getAttribute("RESULTERROR");
       fieldDesc="<font class='mainred'>"+fieldDesc+"</font>";
      }
       else if(action.equalsIgnoreCase("qtyerror"))
      {
 
       fieldDesc=(String)request.getSession().getAttribute("QTYERROR");
       fieldDesc="<font class='mainred'>"+fieldDesc+"</font>";
      }
     
      else if(action.equalsIgnoreCase("batcherror"))
      {
 
       fieldDesc=(String)request.getSession().getAttribute("BATCHERROR");
       fieldDesc="<font class='mainred'>"+fieldDesc+"</font>";
      }
      else if(action.equalsIgnoreCase("catcherro"))
      {
 
       fieldDesc=(String)request.getSession().getAttribute("CATCHBATCHERROR");
       fieldDesc="<font class='mainred'>"+fieldDesc+"</font>";
      }
       


%>
<!-- <html> -->

<%--New page design begin --%>
<%
String title = "Goods Receipt by Purchase Order ";
%>
<%@include file="sessionCheck.jsp" %>
<jsp:include page="header2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
</jsp:include>
<%--New page design end --%>

<script src="js/calendar.js"></script>
<!-- <script src="js/jquery-1.4.2.js"></script> -->
<script src="js/json2.js"></script>
<script src="js/general.js"></script>

<!-- <title>Goods Receipt by Inbound Order</title>
<link rel="stylesheet" href="css/style.css"> -->
<script>

var subWin = null;
function popUpWin(URL) {
    subWin = window.open(URL, 'InboundOrderList', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=800,height=400,left = 200,top = 184');
}
function validatePO(form) {
   var frmRoot=document.form;
   var table = document.getElementById("MULTIPLE_RECEIVING");
   var rowCount = table.rows.length;
   document.form.DYNAMIC_RECEIVING_SIZE.value = rowCount;
   if(document.form.SERIAL_SELECTION.checked){
	   processSerialSelection();
   }
   if(document.form.ALWAYS_SAME_LOCATION.checked){
		processSameLocation();
   }
   if(document.form.ALWAYS_SAME_EXPIRY.checked){
		processSameExpiry();
   }
   var totalQtyReceive=0;
   if(frmRoot.ORDERNO.value=="" || frmRoot.ORDERNO.value.length==0 ) {
		alert("Please Enter ORDERNO!");
		frmRoot.ORDERNO.focus();
		return false;
   }
   for(var index = 0; index<rowCount; index++) {
	    var locationText = document.getElementById("LOC_"+index);
		var receiveQty = document.getElementById("RECEIVEQTY_"+index);
		var batchText = document.getElementById("BATCH_"+index);

                if(document.form.SERIAL_SELECTION.checked){
                     for(var j=0;j<rowCount;j++){
                if(index!=j){

                        var chkbatch = document.getElementById("BATCH"+"_"+j);
                         if(batchText.value==chkbatch.value){
                          alert("Duplicate batch Scanned !");
                 chkbatch.select();
                   return false;
               }
               }
                }
}
		if(locationText.value== "" || locationText.value.length==0 ) {
			alert("Please Enter LOC!");
			locationText.focus();
			locationText.style.backgroundColor = "#FFE5EC";
			return false;
		}else{
			locationText.style.backgroundColor = "#FFFFFF";
		}
		if(batchText.value== "" || batchText.value.length==0 ) {
			alert("Please Enter valid batch!");
			batchText.style.backgroundColor = "#FFE5EC";
			batchText.focus();
               if(document.form.SERIAL_SELECTION.checked){
		batchText.value="";
                }else{
                batchText.value="NOBATCH";
                }
			return false;
		}else{
			batchText.style.backgroundColor = "#FFFFFF";
		}
		if(receiveQty.value== "" || receiveQty.value.length==0 || receiveQty.value<=0) {
			alert("Please Enter Valid Qty!");
			receiveQty.style.backgroundColor = "#FFE5EC";
			receiveQty.focus();
			return false;
		}else{
			totalQtyReceive = (totalQtyReceive*1)+ (receiveQty.value*1);
			totalQtyReceive = totalQtyReceive * 1;
			receiveQty.style.backgroundColor = "#FFFFFF";
		}
	
   }
    
   if(frmRoot.ITEMNO.value=="" || frmRoot.ITEMNO.value.length==0 ) {
		alert("Please Enter Product ID!");
		frmRoot.ITEMNO.focus();
		return false;
   }
   var orderedQty = <%=StrUtils.removeFormat(ORDERQTY)%>;
   orderedQty = parseFloat(orderedQty).toFixed(3);
   var receivedQty = <%=StrUtils.removeFormat(RECEIVEDQTY)%>;
   receivedQty = parseFloat(receivedQty).toFixed(3);
   totalQtyReceive = parseFloat(totalQtyReceive).toFixed(3);
   
   var totalqty = (totalQtyReceive*1)+(receivedQty*1);
   totalqty = parseFloat(totalqty).toFixed(3);

      if(totalqty>(orderedQty*1)){
	   for(var index = 0; index<rowCount; index++) {
		   var receiveQty = document.getElementById("RECEIVEQTY_"+index);
		   receiveQty.style.backgroundColor = "#FFE5EC";
	   }
	   alert("Exceeded the Orderd Qty. Please check all the Qtys.!");
	   return false;
   }else{
	   document.form.RECEIVEQTY.value = totalQtyReceive; 
	   
	   document.form.action ="/track/OrderReceivingByPOServlet?action=MultipleReceive";
	   document.form.submit();
	   return true;
   }
  
}
function onClear(){
 

  document.form.ORDERNO.value="";
  document.form.CUSTNAME.value="";
  document.form.ITEMNO.value="";
  document.form.ITEMDESC.value="";
  document.form.LOC.value="";
  document.form.BATCH.value="";
  document.form.REF.value="";
  document.form.ORDERQTY.value="";
  document.form.RECEIVEDQTY.value="";
  document.form.RECEIVEQTY.value="";
  document.form.CONTACTNAME.value="";
  document.form.TELNO.value="";
  document.form.EMAIL.value="";
  document.form.ADD1.value="";
  document.form.ADD2.value="";
  document.form.ADD3.value="";
 
  
  return true;
}
</script>
<%--New page design begin --%>

<div class="container-fluid m-t-20">
	 <div class="box"> 
	 <!-- Muruganantham Modified on 16.02.2022 -->
             <ul class="breadcrumb backpageul" >      	
                  <li><a href="../home"><span class="underline-on-hover">Dashboard</span> </a></li>       
                  <li><a href="../purchaseorder/summary"><span class="underline-on-hover">Purchase Order Summary</span></a></li> 
                  <li><a href="../purchaseorder/detail?pono=<%=pono%>"><span class="underline-on-hover"> Purchase Order Detail</span></a></li>  
                  <li><label>Purchase Receipt</label></li>                                    
             </ul>   
     <!-- Muruganantham Modified on 16.02.2022 --> 
            <div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title"><%=title %></h1>
		</div>
		
 <div class="box-body">
<%--New page design end --%>

<form class="form-horizontal" name="form" method="post" action="../OrderReceivingByPOServlet?">
   
        
<center>
      <h2><small><%=fieldDesc%></small></h2>
</center>
   
       <div class="form-group">
       <label class="control-label col-sm-2" for="inbound Order">Order Number:</label>
       <div class="col-sm-3">
       <INPUT name="ORDERNO" type="TEXT" value="<%=ORDERNO%>" size="30" MAXLENGTH=20 class="form-control" readonly="readonly">
   	   </div>
   	   <div class="form-inline">
        <label class="control-label col-sm-2" for="Order Qty">Order Quantity:</label>
        <div class="col-sm-3">
        <INPUT name="ORDERQTY" class="form-control"	type="TEXT" value="<%=ORDERQTY%>" style="width: 100%" MAXLENGTH=80 readonly>
        </div>
    	<!-- <div class="form-inline">
    	<label class="control-label col-sm-2" for="Email Address">Email:</label>
        <div class="col-sm-3"> -->
        <INPUT name="EMAIL" value="<%=EMAIL%>" class="form-control" type="hidden" size="30" MAXLENGTH=80 readonly>
        <!-- </div>
 		</div> -->
 		</div> 
 		</div>
 		
 		<div class="form-group">
  	   <label class="control-label col-sm-2" for="supplier name">Supplier Name:</label>
       <div class="col-sm-3">
       <INPUT name="CUSTNAME" class="form-control" type="TEXT" value="<%=StrUtils.forHTMLTag(CUSTNAME)%>" size="30" MAXLENGTH=80 readonly>
    	</div>
    	<div class="form-inline">
        <label class="control-label col-sm-2" for="Received Qty">Received Quantity:</label>
        <div class="col-sm-3">
        <INPUT name="RECEIVEDQTY" class="form-control" type="TEXT" value="<%=RECEIVEDQTY%>" style="width: 100%" MAXLENGTH=80 readonly>
        </div>
    	<!-- <div class="form-inline">
    	<label class="control-label col-sm-2" for="Unit Number">Unit No:</label>
        <div class="col-sm-3"> -->
        <INPUT name="ADD1" class="form-control"	value="<%=ADD1%>" type="hidden" size="30" MAXLENGTH=80 readonly>
        <!-- </div>
 		</div> -->
 		</div>
 		</div> 
 		
 		
 		<div class="form-group">
    	<label class="control-label col-sm-2" for="Contact Name">Contact Name:</label>
        <div class="col-sm-3">
        <INPUT name="CONTACTNAME" class="form-control" value="<%=CONTACTNAME%>" type="TEXT" size="30" MAXLENGTH=80 readonly>
        </div>
        <div class="form-inline">
        <label class="control-label col-sm-2" for="Receiving Qty">Receiving Quantity:</label>
        <div class="col-sm-3">
        <INPUT name="RECEIVEQTY" value="<%= RECEIVEQTY%>" type="TEXT" style="width: 100%" MAXLENGTH=80 readOnly  class="form-control">
        </div>
    	<!-- <div class="form-inline">
    	<label class="control-label col-sm-2" for="Buiding Name">Building:</label>
        <div class="col-sm-3"> -->
        <INPUT name="ADD2" value="<%=ADD2%>" class="form-control" type="hidden" value="" size="30" MAXLENGTH=80	readonly>
        <!-- </div>
 		</div> -->
 		</div>
 		</div>
 		
        <div class="form-group">
        <label class="control-label col-sm-2" for="Product Id">Product ID:</label>
        <div class="col-sm-3">
        <INPUT name="ITEMNO" type="TEXT" value="<%=ITEMNO%>" size="30" MAXLENGTH=80 class="form-control" readonly="readonly">
       </div>
       <div class="form-inline">
        <label class="control-label col-sm-2" for="GRNO">GRNO:</label>
        <div class="col-sm-3">
        <input name="GRNO" class="form-control" type="TEXT" value="" style="width: 100%" MAXLENGTH=80 />
        </div>
        </div>
 		</div>
 		 
        <div class="form-group">
        <label class="control-label col-sm-2" for="Description">Description:</label>
        <div class="col-sm-3">
        <INPUT name="ITEMDESC" class="form-control" type="TEXT" value="<%=StrUtils.forHTMLTag(ITEMDESC)%>" size="30" MAXLENGTH=80 readonly>
        </div>
    	<!-- <div class="form-inline">
    	<label class="control-label col-sm-2" for="Telephone">Telephone:</label>
        <div class="col-sm-3"> -->
        <INPUT name="TELNO" class="form-control"	value="<%=TELNO%>" type="hidden" size="30" MAXLENGTH=80 readonly>
        <!-- </div>
 		</div> -->
 		<div class="form-inline">
        <label class="control-label col-sm-2" for="Remarks">Remarks:</label>
        <div class="col-sm-3">
        <INPUT class="form-control" name="REF" type="TEXT" value="<%=REF%>"	style="width: 100%" MAXLENGTH=100>
        </div>
    	<!-- <div class="form-inline">
    	<label class="control-label col-sm-2" for="street">Street:</label>
        <div class="col-sm-3"> -->
        <INPUT name="ADD3" value="<%=ADD3%>" class="form-control" type="hidden" size="30" MAXLENGTH=80 readonly>
        <!-- </div>
 		</div> -->
 		</div>
 		</div>

 		
 		<div class="form-group">
 		<label class="control-label col-sm-4" for="Remarks">Always use the same for the following:</label>
        <div class="col-sm-8">
        <label class="checkbox-inline"> 
		<input type="Checkbox" name="SERIAL_SELECTION" id="SERIAL_SELECTION" value="SERIAL_SELECTION" onClick="processSerialSelection();"></input> <b>Serial Receiving</b>
		</label> 
        <label class="checkbox-inline">      
        <input type="Checkbox" name="ALWAYS_SAME_LOCATION" id="ALWAYS_SAME_LOCATION" value="ALWAYS_SAME_LOCATION" onClick="processSameLocation();"></input><b>Location</b></label>
		<label class="checkbox-inline"> 
		<input type="Checkbox" name="ALWAYS_SAME_EXPIRY" id="ALWAYS_SAME_EXPIRY" value="ALWAYS_SAME_EXPIRY" onClick="processSameExpiry();"></input> <b>Expiry Date</b></label>
		</div>
        </div>
 		
 		

 		<div class="form-group">
 		<div class="col-sm-12">
 		<table align="center" width="85%" border="0" id="MULTIPLE_RECEIVING">
			<tr>
				<td width="18%"><b>Location : </b><div class="input-group"><INPUT class=form-control name="LOC_0" id="LOC_0" type="TEXT"
					value="<%=LOC%>" size="20"
					onkeypress="if((event.keyCode=='13') && ( this.value.length > 0)){validateLocation(this.value,0);}"
					MAXLENGTH=80> <a href="#" class="input-group-addon" data-toggle="tooltip" data-placement="top" Title="Location Details"
					onClick="javascript:popUpWin('loc_list_MultiReceivewms.jsp?INDEX=0&LOC='+form.LOC_0.value);">
					<i class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></div></td>
				<td width="2%">&nbsp;&nbsp;</td>
				<td width="26%"><b>Batch : </b><div class="input-group"><INPUT class="form-control" name="BATCH_0" id="BATCH_0" type="TEXT"
					value="<%=BATCH%>" size="20" onkeypress="if((event.keyCode=='13') && ( this.value.length > 0)&&(document.form.SERIAL_SELECTION.checked)){addRow(); }"
					MAXLENGTH=40><a href="#" class="input-group-addon" data-toggle="tooltip" data-placement="top" Title="Auto-Generate" onClick="generateBatch(0);">
					<i class="glyphicon glyphicon-edit" style="font-size: 20px;"></i></a> </div></td>
				<td width="2%">&nbsp;&nbsp;</td>
				<td width="16%"><b>Receiving Qty : </b><INPUT class="form-control" name="RECEIVEQTY_0" id="RECEIVEQTY_0"
					value="<%= RECEIVEQTY%>" type="TEXT" size="10" MAXLENGTH="80" onkeypress="if((event.keyCode=='13') && ( this.value.length > 0)){document.getElementById('EXPIREDATE_0').focus();}" \></td>
				<td width="1%">&nbsp;&nbsp;</td>
				<td width="20%"><b>Expiry Date : </b><div class="input-group"><INPUT class="form-control datepicker" name="EXPIREDATE_0" id="EXPIREDATE_0"
					value="<%= EXPIREDATE%>" type="TEXT" size="30" MAXLENGTH="80">
				</div></td>
			</tr>

		</table> 		 
		</div>	
 		 </div>
 		
 			 		
 		<div class="form-group">        
      	<div class="col-sm-1">&nbsp;</div>
      	<INPUT type="hidden" name="DYNAMIC_RECEIVING_SIZE">
      	<div class="col-sm-5">
      	<button type="button" class="Submit btn btn-default" onClick="addRow();"><b>Add New Receiving</b></button>&nbsp;&nbsp;
      	<button type="button" class="Submit btn btn-default" onClick="deleteRow('MULTIPLE_RECEIVING');"><b>Remove Last Added Receiving </b></button>
      	</div>     	
      	 <div class="form-inline">
        <label class="control-label col-sm-2" for="Transaction Date">Transaction Date:</label>
        <div class="col-sm-3">
        <div class="input-group">          
        <INPUT class="form-control datepicker" name="TRANSACTIONDATE" id="TRANSACTIONDATE"	value="<%=TRANSACTIONDATE%>" type="TEXT" size="30" MAXLENGTH="80" readonly="readonly">
      	</div>
    	</div>
    	</div>
    	</div>
    	
    	<div class="form-group">        
      	<div class="col-sm-12" align="center">
      	<button type="button" class="Submit btn btn-default" onClick="window.location.href='InboundOrderMultipleReceiptSummary.jsp?PONO=<%=ORDERNO%>&action=View'"><b>Back</b></button>&nbsp;&nbsp;
      	<button type="button" class="Submit btn btn-default" onClick="validatePO(document.form)"><b>Receive</b></button>&nbsp;&nbsp;
      	</div>
      	</div> 
      	
      	<INPUT name="ORDERLNO" type="hidden" value="<%=ORDERLNO%>" size="1"	MAXLENGTH=80> 
 		<INPUT name="BALANCEQTY" type="hidden" value="<%=BALANCEQTY%>"	size="1" MAXLENGTH=80>	  		
  		<INPUT name="LOGIN_USER" type="hidden" value="<%=sUserId%>" size="1" MAXLENGTH=80> 
  		       
  		</form>
		</div>
		




<script>


function addRow() {
	var table = document.getElementById("MULTIPLE_RECEIVING");
	var rowCount = table.rows.length;

	var serialselection = false;
	var sameLocaionUse = false;
        var allchecked =false;
        var sameExpiryUse =false;
	if(document.form.SERIAL_SELECTION.checked){
		serialselection = true;
	}
	if(document.form.ALWAYS_SAME_LOCATION.checked){
		sameLocaionUse = true;
	}
      if(document.form.ALWAYS_SAME_EXPIRY.checked){
		sameExpiryUse = true;
	}
 if(serialselection && sameLocaionUse && sameExpiryUse  ){
   allchecked=true;
  }
	var row = table.insertRow(rowCount);
	var firstElementLocationValue = document.getElementById("LOC_0").value;
var firstElementExpiryValue = document.getElementById("EXPIREDATE_0").value;
	
	var locationCell = row.insertCell(0);
		var locationCellText =  "<b>Location:</b><div class=\"input-group\"><INPUT class=\"form-control\" name=\"LOC_"+rowCount+"\" ";
		if(sameLocaionUse){
			locationCellText = locationCellText+ "value=\""+firstElementLocationValue+"\" readonly ";
		}
		locationCellText = locationCellText+ " id=\"LOC_"+rowCount+"\" type = \"TEXT\" size=\"20\"  onkeypress=\"if((event.keyCode=='13') && ( this.value.length > 0)){validateLocation(this.value, "+rowCount+");}\" MAXLENGTH=\"80\">";
		//if(!sameLocaionUse) {
			locationCellText = locationCellText+ "<a class=\"input-group-addon\" data-toggle=\"tooltip\" data-placement=\"right\" Title=\"Location Details\" href=\"#\" onClick=\"javascript:popUpWin('loc_list_MultiReceivewms.jsp?LOC='+form.LOC"+'_'+rowCount+".value+'&INDEX="+rowCount+"');\"><i class=\"glyphicon glyphicon-log-in\" style=\"font-size: 20px;\"></i></a></div>";
			
		//}
	locationCell.innerHTML = locationCellText;
	
	var firstEmptyCell = row.insertCell(1);   
	firstEmptyCell.innerHTML = "&nbsp;&nbsp;";
	
var batchCell = row.insertCell(2);
var Batchtext = "<b>Batch :</b><div class=\"input-group\"><INPUT class=\"form-control\" name=\"BATCH_"+rowCount+"\" id=\"BATCH_"+rowCount+"\"  type = \"TEXT\" size=\"20\"";
if(allchecked){
Batchtext= Batchtext+ " onkeypress=\"if((event.keyCode=='13') && ( this.value.length > 0)  ){addRow();}\"  ";
}
	 Batchtext= Batchtext+  " MAXLENGTH=\"40\" > <a class=\"input-group-addon\" data-toggle=\"tooltip\" data-placement=\"right\" Title=\"Auto Generate\" href=\"#\" onclick=\"generateBatch("+rowCount+");\" value=\"Generate Batch\" name=\"actionBatch\" ><i class=\"glyphicon glyphicon-edit\"  style=\"font-size: 20px; \"></i></a></div>";
	batchCell.innerHTML =Batchtext
	var secondEmptyCell = row.insertCell(3);
	secondEmptyCell.innerHTML = "&nbsp;&nbsp;";
	var receivingQtyCell = row.insertCell(4);
	var receiveQtyText = "<b>Receiving Qty :<b><INPUT class=\"form-control\" name=\"RECEIVEQTY_"+rowCount+"\" ";
		if(serialselection){
			receiveQtyText = receiveQtyText + " value=\"1\" readonly ";
		}
		receiveQtyText = receiveQtyText + " id=\"RECEIVEQTY_"+rowCount+"\" type = \"TEXT\"  size=\"10\"  MAXLENGTH=\"80\" onkeypress=\"if((event.keyCode=='13') && ( this.value.length > 0)){document.getElementById(\'EXPIREDATE_"+rowCount+"').focus();}\"  > ";
	receivingQtyCell.innerHTML = receiveQtyText;
	var thirdEmptyCell = row.insertCell(5);
	thirdEmptyCell.innerHTML = "&nbsp;&nbsp;";
	    var expiredateCell = row.insertCell(6);
        var expiryDtCellText =  "<b>Expire Date :</b></label><div class=\"input-group\"><INPUT class=\"form-control\" name=\"EXPIREDATE_"+rowCount+"\" ";
        if(sameExpiryUse){
			expiryDtCellText = expiryDtCellText+ "value=\""+firstElementExpiryValue+"\" ";
	}
        expiryDtCellText = expiryDtCellText+ " id=\"EXPIREDATE_"+rowCount+"\" type = \"date\" size=\"10\"  MAXLENGTH=\"80\" \></div>";
	
	expiredateCell.innerHTML = expiryDtCellText
        if(allchecked ){
         document.getElementById("BATCH_"+rowCount).select();
        }
}

function deleteRow(tableID) {
	try {
		var table = document.getElementById(tableID);
		var rowCount = table.rows.length;
		rowCount = rowCount * 1 - 1;
		if (rowCount == 0) {
			alert("Can not remove the default Receiving");
		} else {
			table.deleteRow(rowCount);
		}
	} catch (e) {
		alert(e);
	}
}
function validateLocation(locId, index) {
	if(locId=="" || locId.length==0 ) {
		alert("Enter Location!");
		document.getElementById("LOC_"+index).focus();
	}else{
		var urlStr = "/track/InboundOrderHandlerServlet";
		$.ajax( {
			type : "POST",
			url : urlStr,
			data : {
				LOC : locId,
                USERID : "<%=sUserId%>",
				PLANT : "<%=plant%>",
				ACTION : "VALIDATE_LOCATION"
				},
				dataType : "json",
				success : function(data) {
					if (data.status == "100") {
                                var receiveQty = document.getElementById("RECEIVEQTY_"+index);
                                if(document.form.SERIAL_SELECTION.checked){
                                   receiveQty.value="1";
                                }else{
                                    receiveQty.value="";
                                }
					
                                                document.getElementById("BATCH_"+index).value = "NOBATCH";
						document.getElementById("BATCH_"+index).select();
                                	
						
						
					} else {
						alert("Not a valid Location");
						var location = document.getElementById("LOC_"+index);
						location.value="";
						location.focus();
					}
				}
			});
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
				document.getElementById("RECEIVEQTY_"+currentBatchLable).focus();
	
			} else {
				alert("Unable to genarate Batch");
				document.getElementById("BATCH_"+currentBatchLable).value = "NOBATCH";
				document.getElementById("BATCH_"+currentBatchLable).focus();
			}
		}
	});
}
function processSerialSelection() {
	var table = document.getElementById("MULTIPLE_RECEIVING");
	var rowCount = table.rows.length;
	for(var index = 0; index<rowCount; index++) {
		var receiveQty = document.getElementById("RECEIVEQTY_"+index);
		if(document.form.SERIAL_SELECTION.checked)
		{
		receiveQty.value = 1;
		receiveQty.readOnly=true;}
		else
		{
			receiveQty.value = "";
			receiveQty.readOnly=false;
		}
	}
}
function processSameLocation(){
	var table = document.getElementById("MULTIPLE_RECEIVING");
	var rowCount = table.rows.length;
	var defaultValue = document.getElementById("LOC_0").value;
	for(var index = 1; index<rowCount; index++) {
		var receiveQty = document.getElementById("LOC_"+index);
		if(document.form.ALWAYS_SAME_LOCATION.checked)
		{
		receiveQty.value = defaultValue;
		receiveQty.readOnly=true;
		}
		else
		{
			receiveQty.value = "";
			receiveQty.readOnly = false;
		}
	}
}
function processSameExpiry(){
	var table = document.getElementById("MULTIPLE_RECEIVING");
	var rowCount = table.rows.length;
	var defaultValue = document.getElementById("EXPIREDATE_0").value;
	for(var index = 1; index<rowCount; index++) {
		var expiryDt = document.getElementById("EXPIREDATE_"+index);
		if(document.form.ALWAYS_SAME_EXPIRY.checked)
		{
		expiryDt.value = defaultValue;
		expiryDt.readOnly=true;
		}
		else
		{
			expiryDt.value = "";
			expiryDt.readOnly = false;
		}
	}
}
function removeCommas(number)
{
	var numval=number.value;
	var splitArr = new Array();
	var appendStr = new String();
	for(var i=0; i<splitArr.length; i++){
		appendStr = appendStr + splitArr[i];}
alert(appendStr);
     return appendStr;

}
</script>
</div>
</div>

<jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
</jsp:include>
