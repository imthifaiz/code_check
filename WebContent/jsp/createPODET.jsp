<%@ include file="header.jsp" %> 
<%@ page import="com.track.dao.*"%>



<%
String title = "Purchase Order - Add Product";
%>
<%@include file="sessionCheck.jsp" %>
<jsp:include page="header2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
</jsp:include>

<script type="text/javascript" src="js/json2.js"></script>
<script type="text/javascript" src="js/general.js"></script>
<script type="text/javascript">
var subWin = null;
function popUpWin(URL) {
 subWin = window.open(URL, 'Items', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=800,height=400,left = 200,top = 184');
}
function validateForm(){
 var frmRoot=document.form;
 var table = document.getElementById("MULTI_IB_REMARKS");
 var rowCount = table.rows.length;
 var checkcnt = rowCount-1;
 document.form.DYNAMIC_REMARKS_SIZE.value = rowCount;
 for(var i=0;i<rowCount;i++){
	   var prdremarks= document.getElementById("PRDREMARKS"+"_"+i);
 }
 if(document.form.ITEM.value == ""){
    alert("Please select an item ");
    document.form.ITEM.focus();
    return false;
 }
else if(document.form.QTY.value == ""){
    alert("Please enter Quantity");
    document.form.QTY.focus();
    return false;
  }
  if(!IsNumeric(form.QTY.value))
   {
     alert(" Please Enter valid  Qty !");
     form.QTY.focus();  form.QTY.select(); return false;
   }
   
/*  else if(document.form.QTY.value <= 0){
	    alert("Please enter > 0 quantity");
	    document.form.QTY.focus();
	    return false;
	  }*/
  if(document.form.UNITCOST.value == ""){
	    alert("Please enter Unit Cost");
	    document.form.UNITCOST.focus();
	    return false;
	  }
	  else if (document.form.MULTIUOM.value == ""){
	    alert("Please enter a UOM value");
	    document.form.MULTIUOM.focus();
	    document.form.MULTIUOM.select();
	    return false;
  }
  var costamt = document.form.UNITCOST.value;
  if (costamt.indexOf('.') == -1) costamt += ".";
	var cdecNum = costamt.substring(costamt.indexOf('.')+1, costamt.length);
	if (cdecNum.length > <%=DbBean.NOOFDECIMALPTSFORCURRENCY%>)
	{
		alert("Invalid more than <%=DbBean.NOOFDECIMALPTSFORCURRENCY%> digits after decimal in Unit Cost");
		document.form.UNITCOST.focus();
		return false;
		
	}
	
	 var costamt = document.form.QTY.value;
	  if (costamt.indexOf('.') == -1) costamt += ".";
		var cdecNum = costamt.substring(costamt.indexOf('.')+1, costamt.length);
		if (cdecNum.length > 3)
		{
			alert("Invalid more than 3 digits after decimal in QTY");
			document.form.QTY.focus();
			return false;
			
		}

  if(isNaN(removeCommas(document.form.UNITCOST.value)))
  {
	    alert("Please Enter valid Unit Cost");
	    document.form.UNITCOST.focus();
	    return false;
  }
  
 else{
    
    //document.form.Submit.value="Add Product"; 
    document.form.submit();
    return true;
    }
 
}

function ref()
{
  document.form.ITEM.focus();
  document.form.ITEM.value="";
}
function validDecimal(str){
	if (str.indexOf('.') == -1) str += ".";
	var decNum = str.substring(str.indexOf('.')+1, str.length);
	var declength =	parseInt(document.getElementById("numberOfDecimal").value);
	var disprice = document.form.UNITCOSTDISCOUNT.value;
		if(disprice==0)
		{
			var price = document.form.UNITCOST.value;
			document.form.UNITCOSTDISCOUNT.value=price;
		}
	if (decNum.length > declength)
	{
		alert("Invalid more than "+declength+" digits after decimal in Unit Cost");
		document.form.UNITCOST.focus();
		return false;
		
	}
	else
		{
		var price = document.form.UNITCOST.value;
		   
		  document.getElementById("UNITCOSTRD").value =price;
		}
}	
function validToThreeDecimal(str) {
	if (str.indexOf('.') == -1)
		str += ".";
	var decNum = str.substring(str.indexOf('.') + 1, str.length);
	 if(decNum.length>3){
		 alert("Not more than 3 digits are allowed after decimal value in Qty");
		 document.form.QTY.focus();
		return false;
		
	}
	
}

</script>




<jsp:useBean id="gn"  class="com.track.gates.Generator" />
<jsp:useBean id="sl"  class="com.track.gates.selectBean" />
<jsp:useBean id="db"  class="com.track.gates.defaultsBean" />
<jsp:useBean id="su"  class="com.track.util.StrUtils" />
<jsp:useBean id="tpdb"  class="com.track.tables.PODET" />
<jsp:useBean id="tphb"  class="com.track.tables.POHDR" />
<jsp:useBean id="imb"  class="com.track.tables.ITEMMST" />
<jsp:useBean id="UomDAO"  class="com.track.dao.UomDAO" />
<%
    session= request.getSession();
	PlantMstDAO plantMstDAO = new PlantMstDAO();
    String plant=su.fString((String)session.getAttribute("PLANT"));
    sl.setmLogger(mLogger);
    tpdb.setmLogger(mLogger);
    tphb.setmLogger(mLogger);
    imb.setmLogger(mLogger);
    String action  = su.fString(request.getParameter("Submit")).trim();
    String pono    = su.fString(request.getParameter("PONO"));
    String jobNum  = su.replaceCharacters2Recv(su.fString(request.getParameter("JOB_NUM")));
    String custName  = su.replaceCharacters2Recv(su.fString(request.getParameter("CUST_NAME")));
    String custCode  = su.fString(request.getParameter("CUST_CODE"));
    String deldate = su.fString(request.getParameter("DELDATE"));
   // String origin  = su.fString(request.getParameter("USERFLD1"));
    String result ="", sql="";       int n=1;
    String enrolledBy = (String)session.getAttribute("LOGIN_USER");
    String RFLAG=     su.fString(request.getParameter("RFLAG"));
    String PRDREMARKS = su.fString(request.getParameter("PRDREMARKS"));
    String LBLREMARKS = su.fString(request.getParameter("LBLREMARKS"));
    String UNITCOSTDISCOUNT= su.fString(request.getParameter("UNITCOSTDISCOUNT"));
	String uom = su.fString(request.getParameter("MULTIUOM"));
	String numberOfDecimal = plantMstDAO.getNumberOfDecimal(plant);
    LBLREMARKS="Remarks1:";
  
    if (action.equalsIgnoreCase("View"))
    {
      response.sendRedirect("createPO.jsp?PONO="+pono);
    }
    if (action.equalsIgnoreCase("Delete"))
    {
      pono    = request.getParameter("PONO");
      n = tpdb.deletePODETML(pono,plant);
      n = tphb.deletePOHDRML(pono,plant);
      if(n==1) result = "<font color=\"green\">Purchase Order Deleted Successfully</font><br><br><center>"+
                        "<input type=\"button\" value=\" OK \" onClick=\"window.location.href='../home'\">";
      else     result = "<font color=\"red\"> Error in Delete Purchase Order  -  <br><br><center>"+
                        "<input type=\"button\" value=\"Back\" onClick=\"window.location.href='javascript:history.back()'\"> "+
                        "<input type=\"button\" value=\"Cancel\" onClick=\"window.location.href='../home'\">";

      result = "Purchase Order<br><h3>"+result;
      session.setAttribute("RESULT",result);
      response.sendRedirect("displayResult2User.jsp");
    }
    POUtil posUtil = new POUtil();
    Map hdrDetail= posUtil.getPOReceiptInvoiceHdrDetails(plant);
    String showPreviousPurchaseCost = (String) hdrDetail.get("SHOWPREVIOUSPURCHASECOST");
%>
<body onload="ref()">
<div class="container-fluid m-t-20">
	 <div class="box"> 
            <div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title"><%=title %></h1>
		</div>
		
 <div class="box-body">
 <input type="number" id="numberOfDecimal" style="display:none;" value=<%=numberOfDecimal%>>
<form class="form-horizontal" name="form" method="post" action="/track/purchaseorderservlet?Submit=Add Product" onSubmit="return validateForm()">
      
     <div class="col-sm-6">  <!-- column is used for grid function -->
     
      <div class="form-group">
      <label class="control-label col-sm-4" for="Transfer Order No">Order Number:</label>
      <div class="col-sm-6">
      <INPUT type = "TEXT" class="form-control" size="20"  MAXLENGTH=20 name="PONO"   value="<%=pono%>" READONLY>
	  </div>
      </div>
      
	  <div class="form-group">
      <label class="control-label col-sm-4" for="Assignee Name">Supplier Name:</label>
      <div class="col-sm-6">          
      <INPUT type = "TEXT"  class="form-control" size="20"  MAXLENGTH=100 name="CUST_NAME"  value="<%=su.forHTMLTag(custName)%>" READONLY>
      </div>
      <INPUT type = "hidden"  size="30"  MAXLENGTH=20 name="CUST_CODE"  value="<%=custCode%>" READONLY>
      </div>
      
      <div class="form-group">
      <label class="control-label col-sm-4" for="Order Line No">Order Line No:</label>
      <div class="col-sm-6">          
     <INPUT type = "TEXT"  class="form-control" size="20"  MAXLENGTH=4 name="POLNNO" value="<%=tpdb.getNextLineNo(pono,plant)%>" READONLY>
      </div>
      </div>
                       <!--(Do not delete this below Coding)-->
                             
      <!-- <div class="form-group">
      <label class="control-label col-sm-2" for="Manufacturer">Manufacturer:</label>
      <div class="col-sm-3">
      <div class="input-group">   -->  
      <INPUT class="form-control"  type = "hidden" size="40"  MAXLENGTH=100 name="MANUFACT">
      <!-- <span class="input-group-addon" 
   	  onClick="javascript:popUpWin('list/vendor_listforItem.jsp?MANUFACT='+form.MANUFACT.value);"> 	
      <a href="#" data-toggle="tooltip" data-placement="top" title="Manufacturer Details">
  	  <i class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span>
      </div>
  	  </div>
      </div> -->
      
      <div class="form-group">
      <label class="control-label col-sm-4" for="Product VAT/GST">Product VAT:</label>
      <div class="col-sm-6"> 
      <div class="input-group">         
      <INPUT type="TEXT"  size="20"  MAXLENGTH=20 name="PRODGST" id="PRODGST" class="form-control" readonly >
      <span class="input-group-addon" style="font-size: 20px; color: #0059b3"><b>%</b></span>
      </div>
      </div>
      </div>
            	
	  <div class="form-group">
      <label class="control-label col-sm-4" for="Unit Price">List Cost:</label>
      <div class="col-sm-6">          
      <INPUT  type = "TEXT" size="20"  MAXLENGTH=20 name="LISTCOST" id="LISTPRICE" class="form-control" READONLY>
      </div>
       </div>
      
      
      <div class="form-group">
      <label class="control-label col-sm-4" for="List price">Supplier Discount:</label>
      <div class="col-sm-6">
      <div class="input-group">  
      <INPUT  type = "TEXT" size="20"  MAXLENGTH=20 name="SUPPLIERDISCOUNT" id="SUPPLIERDISCOUNT" class="form-control" READONLY>
      <span class="input-group-addon" style="font-size: 12px;"><b>(By Cost or Percentage)</b></span>   
      </div>
      </div>
      </div>
       
      <div class="form-group">
      <label class="control-label col-sm-4" for="Min Stock Quantity">Min Stock Quantity:</label>
      <div class="col-sm-6">          
      <INPUT  type = "TEXT" size="20"  MAXLENGTH=20 name="MINSTKQTY" id="MINSTKQTY" class="form-control" READONLY>
      </div>
       </div>
       
       <div class="form-group">
      <label class="control-label col-sm-4" for="Max Stock Quantity">Max Stock Quantity:</label>
      <div class="col-sm-6">          
      <INPUT  type = "TEXT" size="20"  MAXLENGTH=20 name="MAXSTKQTY" id="MAXSTKQTY" class="form-control" READONLY>
      </div>
      </div>
       
       <div class="form-group">
      <label class="control-label col-sm-4" for="Out going Quantity">Incoming Quantity:</label>
      <div class="col-sm-6">          
      <INPUT  type = "TEXT" size="20"  MAXLENGTH=20 name="INCOMINGQTY" id="INCOMINGQTY" class="form-control" READONLY>
      </div>
      </div>
      
       <div class="form-group">
      <label class="control-label col-sm-4" for="Stock On Hand">Stock On Hand:</label>
      <div class="col-sm-6">          
      <INPUT  type = "TEXT" size="20"  MAXLENGTH=20 name="STOCKONHAND" id="STOCKONHAND" class="form-control" READONLY>
      </div>
       </div>
       </div>
       
      
     <div class="col-sm-6"> <!-- column is used for grid function -->

      <div class="form-group">
      <label class="control-label col-sm-3" for="Product ID"> 
      <a href="#" data-placement="left">
   	  <i class="glyphicon glyphicon-star" style="font-size: 10px; top:-6px; color: #e50000"></i></a>&nbsp;Product ID:</label>
      <div class="col-sm-6">
      <div class="input-group">    
     <INPUT class="form-control" type = "TEXT" size="30"  MAXLENGTH=50 onkeypress="if((event.keyCode=='13') && ( document.form.ITEM.value.length > 0)) {validateProduct();}"  name="ITEM" >
      <span class="input-group-addon" 
   	  onClick="javascript:popUpWin('list/item_list_po.jsp?ITEM='+form.ITEM.value+'&PONO='+form.PONO.value+'&FLAG=A');"> 	
      <a href="#" data-toggle="tooltip" data-placement="top" title="Product Details">
  	  <i class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span>
      </div>
  	  </div>
  	  </div>
  	  
  	  <div class="form-group">
      <label class="control-label col-sm-3" for="Discription">Description:</label>
      <div class="col-sm-6">
      <div class="input-group">    
      <input name="DESC" type="TEXT" size="30" MAXLENGTH=100 class="form-control">
      <span class="input-group-addon" 
   	  onClick="javascript:popUpWin('list/item_list_po.jsp?ITEM='+form.ITEM.value+'&DESC='+form.DESC.value+'&PONO='+form.PONO.value+'&FLAG=A');"> 	
      <a href="#" data-toggle="tooltip" data-placement="top" title="Product Details">
  	  <i class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span>
      </div>
  	  </div>
      </div>
       
       <div class="form-group">
      <label class="control-label col-sm-3" for="Detailed Description">Detailed Description:</label>
      <div class="col-sm-6">          
      <INPUT class="form-control" type = "TEXT" style="width: 100%"  MAXLENGTH=100 name="DETAILDESC" readonly>
      </div>
      </div>
       
       <div class="form-group">
      <label class="control-label col-sm-3" for="Quantity">
      <a href="#" data-placement="left">
   	  <i class="glyphicon glyphicon-star" style="font-size: 10px; top:-6px; color: #e50000"></i></a>&nbsp;Quantity:</label>
      <div class="col-sm-6">
      <div class="input-group"> 
      <INPUT class="form-control"  type = "TEXT" size="30"  MAXLENGTH=10 name="QTY" id="QTY" onchange="validToThreeDecimal(this.value)" onkeypress="if((event.keyCode=='13') && ( document.form.QTY.value.length > 0)) {validateForm();}">
       <span class="input-group-btn"></span>
       
	   <SELECT class="form-control" data-toggle="dropdown" data-placement="left" name="MULTIUOM" style="width: 150%" onchange="CheckPriceVal(this.value)">
			
					<%
				  ArrayList ccList = UomDAO.getUOMList(plant);
					for(int i=0 ; i < ccList.size();i++)
		      		 {
						Map m=(Map)ccList.get(i);
						uom = (String)m.get("UOM"); %>
				        <option value="<%=uom%>"><%=uom%>  </option>	          
		        <%
       		}
			%> 
	 </SELECT>
      </div>
      </div>
      </div>
       
       <div class="form-group">
      <label class="control-label col-sm-3" for="Unit Price">
      <a href="#" data-placement="left">
   	  <i class="glyphicon glyphicon-star" style="font-size: 10px; top:-6px; color: #e50000"></i></a>&nbsp;Unit Cost:</label>
      <div class="col-sm-6">
      <div class="input-group">          
      <INPUT class="form-control" type = "TEXT" size="30"  MAXLENGTH=20 name="UNITCOST" id="UNITCOST" autocomplete="off" onchange="validDecimal(this.value)">
      <span class="input-group-btn">
      <button class="Submit btn btn-default" type="button" name="Calculate"  
      onClick="if((document.form.UNITCOSTDISCOUNT.value !=0.000) && (document.form.UNITCOST.value <= document.form.UNITCOSTDISCOUNT.value)) {javascript:popUpWin('calcAmtInbound.jsp?CURRENTAMOUNT='+form.UNITCOST.value);}">
      <b>Discount</b></button></span>
      </div>
      </div>
       </div>
       
       <div class="form-group">
        <label class="control-label col-sm-3" for="Delivery Date">Delivery Date:</label>
        <div class="col-sm-6">
        <div class="input-group">          
       <INPUT class="form-control datepicker" type = "TEXT" size="30"  MAXLENGTH="20" id="PRODUCTDELIVERYDATE" name="PRODUCTDELIVERYDATE" readonly>
      	</div>
    	</div>
    	</div>
    	
       <div class="form-group">
	  <label class="control-label col-sm-3" for="Remarks">Remarks:</label>    
    
	 <div class="col-sm-6">
      <table id="MULTI_IB_REMARKS">
      <TR>
      <TH  id="TH1REMARKS_0"></TH>
      <TH id="TH2REMARKS_0"><div class="input-group"><INPUT class="form-control" name="PRDREMARKS_0" type="TEXT" id="PRDREMARKS_0" value="<%=PRDREMARKS%>"	size="88" MAXLENGTH="100"> 
			         <a href="#" class="input-group-addon" data-toggle="tooltip" data-placement="top" Title="Remarks Details" onClick="javascript:popUpWin('remarks_list.jsp?REMARKS='+form.PRDREMARKS_0.value+'&TYPE=MULTI'+'&INDEX=0');">
						<i class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a> </div></TH>
                    </TR>
                    
                    </TABLE>
      </div>
     
       
      <div class="form-inline">        
      <div class="col-sm-3">
      <a href="#" onclick="addRow();" data-toggle="tooltip" data-placement="top" Title="Add New Remarks">
        <i class="glyphicon glyphicon-plus" style="font-size: 20px; top:6px;"></i></a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
      <!-- <button type="button" class="Submit btn btn-default" onClick="addRow();"><b>Add New Remarks</b></button>&nbsp;&nbsp; -->
      <a href="#" onclick="deleteRow('MULTI_IB_REMARKS');" data-toggle="tooltip" data-placement="top" Title="Remove Last Remarks">
        <i class="glyphicon glyphicon-trash" style="font-size: 20px; top:6px;"></i></a>
      <!-- <button type="button" class="Submit btn btn-default" name="Submit" onclick="deleteRow('MULTI_IB_REMARKS');"><b>Delete Last Remarks</b></button>&nbsp;&nbsp; -->
      </div>
      <INPUT type="hidden" name="DYNAMIC_REMARKS_SIZE">
      </div>
       </div>
      
      <div class="form-group">        
      <div class="col-sm-offset-4 col-sm-8">
      <button type="button" class="Submit btn btn-default"name="SubmitBtn" onclick="return validateForm();">
      <b>Add Product & Save</b></button>&nbsp;&nbsp;
      
      <button type="button" class="Submit btn btn-default" name="Submit" 
       onclick="window.location.href='/track/purchaseorderservlet?PONO=<%=pono%>&RFLAG=<%=RFLAG%>&Submit=View';">
      <b>Back</b></button>&nbsp;&nbsp;
      </div>
       <input type="hidden" value="" name="Submit" >
       </div>
       
       </div>
       
        <INPUT type = "hidden"  name="JOB_NUM"  value="<%=jobNum%>" >
        <INPUT type="Hidden" name="ENCRYPT_FLAG" value="1">
        <INPUT type="Hidden" name="VENDNO" value="<%=tphb.getVENDNO()%>">
        <INPUT type = "Hidden"  name="VENDNAME"  value="<%=su.forHTMLTag(custName)%>">
        <INPUT type = "Hidden"  name="LOGIN_USER"  value="<%=enrolledBy%>">
        <INPUT type="Hidden" name="RFLAG" value="<%=RFLAG%>">
        <INPUT type="Hidden" name="UNITCOSTDISCOUNT" value="<%=UNITCOSTDISCOUNT%>">
      <INPUT type="Hidden" name="UNITCOSTRD" id="UNITCOSTRD">
      <INPUT type="Hidden" name="DISCOUNTTYPE" id="DISCOUNTTYPE">
      </form>
      <div class="col-sm-12" id="prevOrderDetail">
      </div>
      </div>
      </div>
      </div>
      
      
      <script>
	$(document).ready(function(){
// 		$("#UNITCOST").bind("change paste keyup", function() {
// 			validDecimal($(this).val()); 
// 			});
    $('[data-toggle="tooltip"]').tooltip();   
	});
	</script>					

</body>

<%--TODO : After giving discount, system is changing into two decimal places --%>
<SCRIPT>
function validateProduct() {
	   //var ucost,mcost;
	   debugger;
	   var cost;
		var productId = document.form.ITEM.value;
        var pono = document.form.PONO.value;
        var desc = document.form.DESC.value;
	if((productId=="" || productId.length==0) && (desc == "" ||desc.length == 0)) {
		alert("Enter Product ID/Description !");
		document.form.ITEM.focus();
	}else{
		var urlStr = "/track/MiscOrderHandlingServlet";
		$.ajax( {
			type : "POST",
			url : urlStr,
			data : {
				ITEM : productId,
				DESC : desc,
				PLANT : "<%=plant%>",
                                PONO:pono,
				ACTION : "VALIDATE_PRODUCT_GETDETAIL"
				},
				dataType : "json",
				success : function(data) {
					
					if (data.status == "100") {
						var resultVal = data.result;
						var regex = /^[^\.]+?(?=\.0*$)|^[^\.]+?\..*?(?=0*$)|^[^\.]*$/g;
						
						if(resultVal.ConvertedUnitCost == null || resultVal.ConvertedUnitCost == undefined || resultVal.ConvertedUnitCost == 0 ){
							document.form.LISTCOST.value = parseFloat("0.00000").toFixed(<%=numberOfDecimal%>);
						}else{
							document.form.LISTCOST.value = parseFloat(resultVal.ConvertedUnitCost).toFixed(<%=numberOfDecimal%>);
						}if(resultVal.PRODGST == null || resultVal.PRODGST == undefined || resultVal.PRODGST == 0 ){
							document.form.PRODGST.value = "0.000";
						}else{
							document.form.PRODGST.value = resultVal.PRODGST.match(regex)[0];
						}if(resultVal.ConvertedUnitCost == null || resultVal.ConvertedUnitCost == undefined || resultVal.ConvertedUnitCost == 0){
							document.form.UNITCOST.value = parseFloat("0.00000").toFixed(<%=numberOfDecimal%>);
							document.form.UNITCOSTRD.value = "0.00000";
						}else{
							document.form.UNITCOST.value = parseFloat(resultVal.ConvertedUnitCost).toFixed(<%=numberOfDecimal%>);
							document.form.UNITCOSTRD.value = resultVal.ConvertedUnitCostWTC.match(regex)[0];
						}if(resultVal.MINSTKQTY == null || resultVal.MINSTKQTY == undefined || resultVal.MINSTKQTY == 0  ){
							document.form.MINSTKQTY.value = "0.000";
						}else{
							document.form.MINSTKQTY.value = resultVal.MINSTKQTY.match(regex)[0];
						}if(resultVal.MAXSTKQTY == null || resultVal.MAXSTKQTY == undefined || resultVal.MAXSTKQTY == 0){
							document.form.MAXSTKQTY.value = "0.000";
						}else{
							document.form.MAXSTKQTY.value = resultVal.MAXSTKQTY.match(regex)[0];
						}if(resultVal.STOCKONHAND == null || resultVal.STOCKONHAND == undefined || resultVal.STOCKONHAND == 0){
							document.form.STOCKONHAND.value = "0.000";
						}else{
							document.form.STOCKONHAND.value = resultVal.STOCKONHAND.match(regex)[0];
						}if(resultVal.INCOMINGQTY == null || resultVal.INCOMINGQTY == undefined || resultVal.INCOMINGQTY == 0){
							document.form.INCOMINGQTY.value = "0.000";
						}else{
							document.form.INCOMINGQTY.value = resultVal.INCOMINGQTY.match(regex)[0];
						}
						document.form.ITEM.value = resultVal.item;
						document.form.DESC.value = resultVal.discription;
                        document.form.DETAILDESC.value = resultVal.detaildesc;
                       
                        
                        document.form.UNITCOSTDISCOUNT.value = resultVal.ConvertedUnitCost;
                        document.form.MANUFACT.value = resultVal.manufacturer;
                        document.form.MULTIUOM.value = resultVal.uom;
                        
                      
                        
                        
                        
                        if(resultVal.incomingIBDiscount=='' || resultVal.incomingIBDiscount=='0' ||resultVal.incomingIBDiscount=='0.00'||resultVal.incomingIBDiscount==undefined)
						{
                        	 document.form.SUPPLIERDISCOUNT.value=parseFloat("0.00000").toFixed(<%=numberOfDecimal%>);
                        	
                        	 document.form.UNITCOSTDISCOUNT.value = resultVal.ConvertedUnitCost;
                        }
						else
						{
							if(resultVal.IBDiscountType=="BYPERCENTAGE")
							{
								document.form.DISCOUNTTYPE.value ="BYPERCENTAGE";
								document.form.SUPPLIERDISCOUNT.value = resultVal.incomingIBDiscount.match(regex)[0]+'%';
								discount = parseFloat((resultVal.ConvertedUnitCost*resultVal.incomingIBDiscount)/100);
								cost = parseFloat(resultVal.ConvertedUnitCost-((resultVal.ConvertedUnitCost*resultVal.incomingIBDiscount)/100));
								
								
							}
							else
							{
								document.form.SUPPLIERDISCOUNT.value = parseFloat(resultVal.incomingIBDiscount).toFixed(<%=numberOfDecimal%>);
								cost = parseFloat(resultVal.incomingIBDiscount);
     						}
							var calAmount = parseFloat(cost).toFixed(<%=numberOfDecimal%>);
							document.form.UNITCOST.value = calAmount;
							document.form.UNITCOSTRD.value =calAmount.match(regex)[0];
							document.form.UNITCOSTDISCOUNT.value =  calAmount;
						}
                        //CheckPrice(resultVal.uom);   
					 	document.form.QTY.focus();
					} else {
						document.form.ITEM.value = "";
						document.form.ITEM.focus();
					}
					<%if(showPreviousPurchaseCost.equals("1")) {%>
						getPreviousOrderDetails();
					<%}%>
				}				
			});
		}
	}
        function validateQuantity() {
		var qty = document.form.QTY.value;
		if (qty == "" || qty.length == 0) {
			alert("Enter Quantity!");
			document.form.QTY.focus();
                        return false;
		} else {
			if (isNumericInput(qty) == false) {
				alert("Entered Quantity is not a valid Qty!");
                                return false;
			} else {
				var keyinQty = document.form.QTY.value;
				
				//alert(qty);
				if(keyinQty<=0){
					alert("Please Enter  > 0 quantity");
					document.form.QTY.value = "";
					document.form.QTY.focus();
				}else{
					  
                   document.form.submit(); 
                   return true; 
				}
			}

		}
	}
   function isNumericInput(strString) {
		var strValidChars = "0123456789.-";
		var strChar;
		var blnResult = true;
		if (strString.length == 0)
			return false;
		//  test strString consists of valid characters listed above
		for (i = 0; i < strString.length && blnResult == true; i++) {
			strChar = strString.charAt(i);
			if (strValidChars.indexOf(strChar) == -1) {
				blnResult = false;
			}
		}
		return blnResult;
	}     

   function addRow() {
		var table = document.getElementById("MULTI_IB_REMARKS");
		var rowCount = table.rows.length;
		var rowCountRemarks = table.rows.length+1;
		var row = table.insertRow(rowCount);
		
    	var remarkslblCell = row.insertCell(0); 
		remarkslblCell.innerHTML='&nbsp;';

		var remarksCell = row.insertCell(1);
		var remarksCellText =  "&nbsp;<div class=\"input-group\"><INPUT class=\"form-control\" name=\"PRDREMARKS_"+rowCount+"\" ";
		remarksCellText = remarksCellText+ " id=\"PRDREMARKS_"+rowCount+"\" type = \"TEXT\" size=\"100\"   MAXLENGTH=\"100\">";
		remarksCellText = remarksCellText + "<a class=\"input-group-addon\" href=\"#\" onClick=\"javascript:popUpWin('remarks_list.jsp?REMARKS='+form.PRDREMARKS"+'_'+rowCount+".value+'&TYPE=MULTI'+'&INDEX="+rowCount+"');\"><i class=\"glyphicon glyphicon-log-in\" style=\"font-size: 20px;\"></i></a></div>";
		remarksCell.innerHTML = remarksCellText;
    	document.getElementById("PRDREMARKS_"+rowCount).focus();
	}

	function deleteRow(tableID) {
		try {
			var table = document.getElementById(tableID);
			var rowCount = table.rows.length;
			rowCount = rowCount * 1 - 1;
			if (rowCount == 0) {
				alert("Can not remove the default Remarks");
			} else {
				table.deleteRow(rowCount);
			}
		} catch (e) {
			alert(e);
		}
	}
	function CheckPriceVal(uom) {
		var productId = document.form.ITEM.value;
		var pono = document.form.PONO.value;
	    var desc = document.form.DESC.value;
	    var disc = document.form.SUPPLIERDISCOUNT.value;
	if((productId=="" || productId.length==0) && (desc == "" ||desc.length == 0)) {
		alert("Enter Product ID/Description !");
		document.form.ITEM.focus();
	}else{
		var urlStr = "/track/MiscOrderHandlingServlet";
		$.ajax( {
			type : "POST",
			url : urlStr,
			data : {
				ITEM : productId,
				DESC : desc,
				PLANT : "<%=plant%>",
				PONO:pono,
	                            UOM:uom,
	                            TYPE:"Purchase",
	                            DISC:disc.replace("%",""),
	                            MINPRICE:1,
				ACTION : "VALIDATE_PRODUCT_UOM"
				},
				dataType : "json",
				success : function(data) {
					
					if (data.status == "100") {
						var resultVal = data.result;
						var regex = /^[^\.]+?(?=\.0*$)|^[^\.]+?\..*?(?=0*$)|^[^\.]*$/g;						
						if(resultVal.ConvertedUnitCost == null || resultVal.ConvertedUnitCost == undefined || resultVal.ConvertedUnitCost == 0){
							document.form.UNITCOST.value = "0.00000";
							document.form.UNITCOSTRD.value = "0.00000";
						}else{
							document.form.UNITCOST.value = parseFloat(resultVal.ConvertedUnitCost).toFixed(<%=numberOfDecimal%>);
							document.form.UNITCOSTRD.value = resultVal.ConvertedUnitCostWTC.match(regex)[0];
						}
						//alert(disc);
	                   if(parseFloat(disc)>0)
	                    {
	                    	if(document.form.DISCOUNTTYPE.value=="BYPERCENTAGE")
							{								 
								var getdist = disc.replace("%","");								
								discount = parseFloat((resultVal.ConvertedUnitCost*getdist)/100);
								cost = parseFloat(resultVal.ConvertedUnitCost-((resultVal.ConvertedUnitCost*getdist)/100));
							}
							else
							{								
								cost = parseFloat(resultVal.ConvertedDiscWTC);
     						}
							var calAmount = parseFloat(cost).toFixed(<%=numberOfDecimal%>);							
							document.form.UNITCOST.value = calAmount;
							document.form.UNITCOSTRD.value =calAmount.match(regex)[0];
							document.form.UNITCOSTDISCOUNT.value =  calAmount;
							
	                    }
					 	document.form.MULTIUOM.focus();
					} else {
						document.form.ITEM.value = "";
						document.form.ITEM.focus();
					}
				}
			});
	}
	}
	
	function getPreviousOrderDetails() {
		$.ajax({
			type : "POST",
			url : "/track/purchaseorderservlet",
			data : {
				ITEM : document.form.ITEM.value,
				PLANT : "<%=plant%>",
				CUSTCODE:document.form.CUST_CODE.value,
				ROWS:"1",
				Submit : "GET_PREVIOUS_ORDER_DETAILS"
			},
			dataType : "json",
			success : function(data) {
				if(data.orders[0].PONO != undefined){
				var result = "<h4>Previours Purchase Cost Detail</h4>";
				result += "<table  style='font-size:15px' border='0' cellspacing='0' width='100%' class='table'>";
				result += "<tr>";
				result += "<thead style='background: #eaeafa'>";
				result += "<th>ORDER NO</th>";
				result += "<th>SUPPLIER</th>";
				result += "<th>DATE</th>";
				result += "<th>COST</th>";
				result += "</tr>";
				result += "</thead>";
				result += "<tbody>";
				$.each(data.orders, function( key, value ) {
					result += "<tr>";
					result += "<td>"+value.PONO+"</td>";
					result += "<td>"+value.CUSTNAME+"</td>";
					result += "<td>"+value.COLLECTIONDATE+"</td>";
					result += "<td>"+parseFloat(value.UNITCOST).toFixed(<%=numberOfDecimal%>)+"</td>";
					result += "</tr>";
				});
				result += "</tbody>";
				result += "</table>";
				if(data.orders.length > 0){
					$("#prevOrderDetail").html(result);
				}
				}
			}
		});
	}
</SCRIPT>
<jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
	<jsp:param name="nobackblock" value="1" />
</jsp:include>
