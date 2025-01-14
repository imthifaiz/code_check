<%@page import="com.track.db.util.DOUtil"%>
<%@page import="com.track.db.util.POUtil"%>
<%@page import="com.track.dao.PlantMstDAO"%>
<%
String title = "Sales Order- Add Products";
%>
<%@include file="sessionCheck.jsp" %>
<jsp:include page="header2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
</jsp:include>

<script type="text/javascript" src="js/general.js"></script>
<script type="text/javascript" src="js/json2.js"></script>
<script type="text/javascript">

var subWin = null;
function popUpWin(URL) {
 subWin = window.open(URL, 'Items', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=800,height=400,left = 200,top = 184');
}
   var subWinForDiscount = null;
  function popUpWinForDiscount(URL) {
    subWinForDiscount = window.open(URL, 'Calculate', 'toolbar=0,scrollbars=no,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=800,height=400,left = 400,top = 184');
  }


function checkprice()
{
	
	uprice= parseFloat(document.getElementById('UNITPRICE').value);
	var msprice = parseFloat(mprice);
	if(uprice != '0' && uprice < msprice)
	{
		alert("Price should not be less than minimum selling price");
		return false;
	}
	else{
		return true;
                }
}
function validDecimal(str){
	if (str.indexOf('.') == -1) str += ".";
	var decNum = str.substring(str.indexOf('.')+1, str.length);
	var declength =	parseInt(document.getElementById("numberOfDecimal").value);
	var disprice = document.form.UNITPRICEDISCOUNT.value;
	if(disprice==0)
	{
		var price = document.form.UNITPRICE.value;
		document.form.UNITPRICEDISCOUNT.value=price;
	}
	if (decNum.length > declength)
	{
		alert("Invalid more than "+declength+" digits after decimal in Unit Price");
		document.form.UNITPRICE.focus();
		return false;
		
	}
	else
	{
	var price = document.form.UNITPRICE.value;		   
	document.getElementById("UNITPRICERD").value =price;
	}
}function validToThreeDecimal(str) {
	if (str.indexOf('.') == -1)
		str += ".";
	var decNum = str.substring(str.indexOf('.') + 1, str.length);
	 if(decNum.length>3){
		 alert("Not more than 3 digits are allowed after decimal value in Qty");
		 document.form.Qty.focus();
		return false;
		
	}
	
}	

</script>

<script language="javascript">

function ref()
{

  //document.form.ITEM.focus();
  //document.form.ITEM.value="";
}

</script>



<%@ page language="java" import="java.util.*,java.sql.*,java.io.*,java.net.*"%>
<jsp:useBean id="gn"  class="com.track.gates.Generator" />
<jsp:useBean id="sl"  class="com.track.gates.selectBean" />
<jsp:useBean id="db"  class="com.track.gates.defaultsBean" />
<jsp:useBean id="su"  class="com.track.util.StrUtils" />
<jsp:useBean id="tpdb"  class="com.track.tables.DODET" />
<jsp:useBean id="tphb"  class="com.track.tables.DOHDR" />
<jsp:useBean id="imb"  class="com.track.tables.ITEMMST" />
<jsp:useBean id="UomDAO"  class="com.track.dao.UomDAO" />
<%@ page import="com.track.util.*"%>
<%@ page import="com.track.gates.*"%>
<%@ page import="java.util.*"%>

<%
	session= request.getSession();
	PlantMstDAO plantMstDAO = new PlantMstDAO();
	String plant=su.fString((String)session.getAttribute("PLANT"));
    String action  = su.fString(request.getParameter("Submit")).trim();
    String dono    = su.fString(request.getParameter("DONO"));
    String jobNum  = su.replaceCharacters2Recv(su.fString(request.getParameter("JOB_NUM")));;
    String custName  =  su.replaceCharacters2Recv(su.fString(request.getParameter("CUST_NAME")));
    String custCode  =su.fString(request.getParameter("CUST_CODE"));
    String deldate = su.fString(request.getParameter("DELDATE"));    
    String UNITPRICEDISCOUNT= su.fString(request.getParameter("UNITPRICEDISCOUNT"));
	String PRDREMARKS = su.fString(request.getParameter("PRDREMARKS"));
    String result ="", sql="";       int n=1;
    String enrolledBy = (String)session.getAttribute("LOGIN_USER");
    String RFLAG=     su.fString(request.getParameter("RFLAG"));
	String uom = su.fString(request.getParameter("UOM"));
	String numberOfDecimal = plantMstDAO.getNumberOfDecimal(plant);
    if (action.equalsIgnoreCase("Go"))
    {
      response.sendRedirect("createDO.jsp?DONO="+dono);
    }
    if (action.equalsIgnoreCase("Delete"))
    {
    
      dono    = request.getParameter("DONO");
      n = tpdb.deleteDODET(dono);
      n = tphb.deleteDOHDR(dono);
      if(n==1) result = "<font color=\"green\">OutBound Order Deleted Successfully</font><br><br><center>"+
                        "<input type=\"button\" value=\" OK \" onClick=\"window.location.href='../home'\">";
      else     result = "<font color=\"red\"> Error in Delete Outgoing Order  -  <br><br><center>"+
                        "<input type=\"button\" value=\"Back\" onClick=\"window.location.href='javascript:history.back()'\"> "+
                        "<input type=\"button\" value=\"Cancel\" onClick=\"window.location.href='../home'\">";

      result = "Outbound Order<br><h3>"+result;
      session.setAttribute("RESULT",result);
      response.sendRedirect("displayResult2User.jsp");
    }
    
    DOUtil doUtil = new DOUtil();
	Map doHdrDetail= doUtil.getDOReceiptInvoiceHdrDetails(plant,"Outbound Order");
	String showPreviousPurchaseCost = (String) doHdrDetail.get("SHOWPREVIOUSPURCHASECOST");
	String showPreviousSalesCost = (String) doHdrDetail.get("SHOWPREVIOUSSALESCOST");
%>
<body onload="ref()">
<div class="container-fluid m-t-20">
	 <div class="box"> 
            <div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title"><%=title %></h1>
		</div>
		
 <div class="box-body">
 <input type="number" id="numberOfDecimal" style="display:none;" value=<%=numberOfDecimal%>>
 
<form class="form-horizontal" name="form" method="post" action="/track/deleveryorderservlet?Submit=Add Product" onSubmit="return validateForm()">
     <div class="row">
     <div class="col-sm-6">  <!-- column is used for grid function -->
     
      <div class="form-group">
      <label class="control-label col-sm-4" for="Transfer Order No">Order Number:</label>
      <div class="col-sm-6">
      <input name="DONO" type="TEXT" value="<%=dono%>" size="20" MAXLENGTH=20 class="form-control" readonly>
	  </div>
      </div>
      
	  <div class="form-group">
      <label class="control-label col-sm-4" for="Assignee Name">Customer Name:</label>
      <div class="col-sm-6">          
      <INPUT  class="form-control" name="CUST_NAME" type="TEXT" value="<%=su.forHTMLTag(custName)%>" size="50" MAXLENGTH=100 readonly>
      <INPUT type = "hidden"  class="inactive" size="20"  MAXLENGTH=20 name="CUST_CODE"  value="<%=custCode%>" READONLY>
      </div>
      </div>
      
      <div class="form-group">
      <label class="control-label col-sm-4" for="Order Line No">Order Line No:</label>
      <div class="col-sm-6">          
      <INPUT type = "TEXT"  class="form-control" size="20"  MAXLENGTH=4 name="DOLNNO" value="<%=tpdb.getNextLineNo(dono,plant)%>" READONLY>
      </div>
      </div>
      
      
      <INPUT class="inactive" type = "hidden" size="40"  MAXLENGTH=20 name="REMARK1">
      <INPUT class="inactive" type = "hidden" size="40"  MAXLENGTH=20 name="REMARK2">
      <INPUT class="inactive" type = "hidden" size="40"  MAXLENGTH=20 name="REMARK3">
      <INPUT class="inactive" type = "hidden" size="40"  MAXLENGTH=20 name="ITEM_CONDITION">
	
	  <div class="form-group">
      <label class="control-label col-sm-4" for="Product VAT">Product VAT:</label>
      <div class="col-sm-6">
      <div class="input-group">          
      <INPUT type="TEXT"  size="30"  MAXLENGTH=20 name="PRODGST" id="PRODGST" class="form-control" readonly >
      <span class="input-group-addon" style="font-size: 20px; color: #0059b3"><b>%</b></span>
      </div>
      </div>
      </div>
      
      <div class="form-group">
      <label class="control-label col-sm-4" for="Unit Price">List Price:</label>
      <div class="col-sm-6">          
      <INPUT  type = "TEXT" size="20"  MAXLENGTH=20 name="LISTPRICE" id="LISTPRICE" class="form-control" READONLY>
      </div>
       </div>
       
      <div class="form-group">
      <label class="control-label col-sm-4" for="List price">Customer Discount:</label>
      <div class="col-sm-6">
      <div class="input-group">  
      <INPUT  type = "TEXT" size="20"  MAXLENGTH=20 name="CUSTOMERDISCOUNT" id="CUSTOMERDISCOUNT" class="form-control" READONLY>
      <span class="input-group-addon" style="font-size: 12px;"><b>(By Price or Percentage)</b></span>        
      </div>
      </div>
      </div>
       
      <div class="form-group">
      <label class="control-label col-sm-4" for="Min Stock Quantity">Min Stock Quantity:</label>
      <div class="col-sm-6">          
      <INPUT  type = "TEXT" size="10"  MAXLENGTH=20 name="MINSTKQTY" id="MINSTKQTY" class="form-control" READONLY>
      </div>
       </div>
       
      <div class="form-group">
      <label class="control-label col-sm-4" for="Max Stock Quantity">Max Stock Quantity:</label>
      <div class="col-sm-6">          
      <INPUT  type = "TEXT" size="20"  MAXLENGTH=20 name="MAXSTKQTY" id="MAXSTKQTY" class="form-control" READONLY>
      </div>
      </div>
       
       <div class="form-group">
      <label class="control-label col-sm-4" for="Stock On Hand">Stock On Hand:</label>
      <div class="col-sm-6">          
      <INPUT  type = "TEXT" size="20"  MAXLENGTH=20 name="STOCKONHAND" id="STOCKONHAND" class="form-control" READONLY>
      </div>
       </div>
       
       <div class="form-group">
      <label class="control-label col-sm-4" for="Out going Quantity">Outgoing Quantity:</label>
      <div class="col-sm-6">          
      <INPUT  type = "TEXT" size="20"  MAXLENGTH=20 name="OUTGOINGQTY" id="OUTGOINGQTY" class="form-control" READONLY>
      </div>
      </div>
      
      </div>
       
      <INPUT type = "Hidden"   name="JOB_NUM"  value="<%=jobNum%>" >
      <INPUT type = "Hidden"   name="ACTUALUPRICE"   >
      <INPUT type="Hidden" name="ENCRYPT_FLAG" value="1">
      <INPUT type = "Hidden"  name="VENDNAME"  value="<%=su.forHTMLTag(custName)%>">
      <INPUT type = "Hidden"  name="LOGIN_USER"  value="<%=enrolledBy%>">
      <INPUT type="Hidden" name="RFLAG" value="<%=RFLAG%>">
      <INPUT type="Hidden" name="UNITPRICEDISCOUNT" value="<%=UNITPRICEDISCOUNT%>">
      <INPUT type="Hidden" name="UNITPRICERD" id="UNITPRICERD">
      <INPUT type="Hidden" name="DISCOUNTTYPE" id="DISCOUNTTYPE">
      <div class="col-sm-6">  <!-- column is used for grid function -->
      
      <div class="form-group">
      <label class="control-label col-sm-3" for="Product ID">
      <a href="#" data-placement="left">
   	  <i class="glyphicon glyphicon-star" style="font-size: 10px; top:-6px; color: #e50000"></i></a>&nbsp;Product ID:</label>
      <div class="col-sm-6">
      <div class="input-group">    
      <INPUT class="form-control" type = "TEXT" size="30"  MAXLENGTH=50 onkeypress="if((event.keyCode=='13') && ( document.form.ITEM.value.length > 0)) {validateProduct();}" name="ITEM" >
      <span class="input-group-addon" 
   	  onClick="javascript:popUpWin('item_list_do.jsp?ITEM='+form.ITEM.value);"> 	
      <a href="#" data-toggle="tooltip" data-placement="top" title="Product Details">
  	  <i class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span>
      </div>
  	  </div>
  	  </div>
      
      <div class="form-group">
      <label class="control-label col-sm-3" for="Description">Description:</label>
      <div class="col-sm-6">
      <div class="input-group">    
      <input name="DESC" type="TEXT" size="30" MAXLENGTH=100 class="form-control">
      <span class="input-group-addon" 
   	  onClick="javascript:popUpWin('item_list_do.jsp?ITEM='+form.ITEM.value+'&DESC='+form.DESC.value);"> 	
      <a href="#" data-toggle="tooltip" data-placement="top" title="Product Details">
  	  <i class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span>
      </div>
  	  </div>
      </div>
      
      <div class="form-group">
      <label class="control-label col-sm-3" for="Quantity">
      <a href="#" data-placement="left">
   	  <i class="glyphicon glyphicon-star" style="font-size: 10px; top:-6px; color: #e50000"></i></a>&nbsp;Quantity:</label>
      <div class="col-sm-6"> 
      <div class="input-group">
      <INPUT class="form-control"  type = "TEXT" size="30"  MAXLENGTH="20" name="QTY" onchange="validToThreeDecimal(this.value)" onkeypress="if((event.keyCode=='13') && ( this.value.length > 0)){validateForm();}">
      <span class="input-group-btn"></span>
      <SELECT class="form-control" data-toggle="dropdown" data-placement="left" name="UOM" style="width: 150%" onchange="CheckPriceVal(this.value)">
			
					<%
				  ArrayList ccList = UomDAO.getUOMList(plant);
					for(int i=0 ; i < ccList.size();i++)
		      		 {
						Map m=(Map)ccList.get(i);
						uom = (String)m.get("UOM"); %>
				        <option value=<%=uom%>><%=uom%>  </option>	          
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
   	  <i class="glyphicon glyphicon-star" style="font-size: 10px; top:-6px; color: #e50000"></i></a>&nbsp;Unit Price:</label>
      <div class="col-sm-6">
      <div class="input-group">          
      <INPUT class="form-control"  type = "TEXT" size="30"  MAXLENGTH=20 name="UNITPRICE" id="UNITPRICE" autocomplete="off" onchange="validDecimal(this.value)">
      <span class="input-group-btn">
      <button class="Submit btn btn-default" type="button" name="Calculate"  
      onClick="if((document.form.UNITPRICEDISCOUNT.value !=0.000 )&& (document.form.UNITPRICE.value <= document.form.UNITPRICEDISCOUNT.value)) {javascript:popUpWinForDiscount('calcAmt.jsp?CURRENTAMOUNT='+form.UNITPRICE.value);}">
      <b>Discount</b></button></span>
      </div>
      </div>
      </div>
      
      <div class="form-group">
        <label class="control-label col-sm-3" for="Delivery Date">Delivery Date:</label>
        <div class="col-sm-6">
        <div class="input-group">          
       <INPUT class="form-control datepicker" type = "TEXT" size="30"   MAXLENGTH="20" id="PRODUCTDELIVERYDATE" name="PRODUCTDELIVERYDATE" readonly>
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
      <div class="col-3">
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
      
      <button type="button" class="Submit btn btn-default" name="Submit" onclick="return validateForm();">
      <b>Add Product & Save</b></button>&nbsp;&nbsp;
      
       <button type="button" class="Submit btn btn-default" name="Submit" 
       onclick="window.location.href='/track/deleveryorderservlet?DONO=<%=dono%>&RFLAG=<%=RFLAG%>&Submit=View';">
      <b>Back</b></button>&nbsp;&nbsp;
      
      </div>
      </div>
      
      </div>
      </div>
      </form>
      <div class="row">
	      <div class="col-sm-6" id="prevPurOrderDetail">
	      </div>
	      <div class="col-sm-6" id="prevSalOrderDetail">
	      </div>
      </div>
      </div>
      </div>
      </div>
    
<div id="costAlertModal" class="modal fade" role="dialog">
  <div class="modal-dialog">

    <!-- Modal content-->
    <div class="modal-content">
		<div class="modal-header">
          <button type="button" class="close" data-dismiss="modal">&times;</button>
		</div>
      <div class="modal-body text-center">
        <h4 id="costAlert">Some text in the modal.</h4>
      </div>
      <div class="modal-footer">
      	<button type="button" class="btn btn-success" data-dismiss="modal" onclick="document.form.submit();">Continue</button>
        <button type="button" class="btn btn-default" data-dismiss="modal">Cancel</button>
      </div>
    </div>

  </div>
</div>
  
      <script>
	$(document).ready(function(){
// 		$("#UNITPRICE").bind("change paste keyup", function() {
// 			validDecimal($(this).val()); 
// 			});
    $('[data-toggle="tooltip"]').tooltip();   
	});
	</script>					

</body>

<script type="text/javascript">
//added by radhika
onpageload();
function onpageload(){

	document.form.ITEM.focus();
	
}
var uprice,mprice;
function validateProduct() {
	var productId = document.form.ITEM.value;
	 var dono = document.form.DONO.value;
	 var urlStr = "/track/ItemMstServlet";
	 var discount;
		$.ajax( {
			type : "POST",
			url : urlStr,
			data : {
				ITEM : productId,
				PLANT : "<%=plant%>",
				DONO:dono,          
				ACTION : "GET_PRODUCT_DETAILS"
				},
				dataType : "json",
				success : function(data) {
					//outgoingOBDiscount
					if (data.status == "100") {
						var resultVal = data.result;
						var regex = /^[^\.]+?(?=\.0*$)|^[^\.]+?\..*?(?=0*$)|^[^\.]*$/g;
						
						if(resultVal.ConvertedUnitCost == null || resultVal.ConvertedUnitCost == undefined || resultVal.ConvertedUnitCost == 0 ){
							document.form.LISTPRICE.value = "0.00000";
						}else{
							document.form.LISTPRICE.value = resultVal.ConvertedUnitCost.match(regex)[0];
						}if(resultVal.PRODGST == null || resultVal.PRODGST == undefined || resultVal.PRODGST == 0 ){
							document.form.PRODGST.value = "0.000";
						}else{
							document.form.PRODGST.value = resultVal.PRODGST.match(regex)[0];
						}if(resultVal.ConvertedUnitCost == null || resultVal.ConvertedUnitCost == undefined || resultVal.ConvertedUnitCost == 0){
							document.form.UNITPRICE.value = parseFloat("0.00000").toFixed(<%=numberOfDecimal%>);
							document.form.UNITPRICERD.value = "0.00000";
						}else{
							 document.form.UNITPRICE.value = parseFloat(resultVal.ConvertedUnitCost).toFixed(<%=numberOfDecimal%>);
							 document.form.UNITPRICERD.value = resultVal.ConvertedUnitCostWTC.match(regex)[0];
						}if(resultVal.stkqty == null || resultVal.stkqty == undefined || resultVal.stkqty == 0  ){
							document.form.MINSTKQTY.value = "0.000";
						}else{
							document.form.MINSTKQTY.value = resultVal.stkqty.match(regex)[0];
						}if(resultVal.maxstkqty == null || resultVal.maxstkqty == undefined || resultVal.maxstkqty == 0){
							document.form.MAXSTKQTY.value = "0.000";
						}else{
							 document.form.MAXSTKQTY.value = resultVal.maxstkqty.match(regex)[0];
						}if(resultVal.stockonhand == null || resultVal.stockonhand == undefined || resultVal.stockonhand == 0){
							document.form.STOCKONHAND.value = "0.000";
						}else{
							document.form.STOCKONHAND.value = resultVal.stockonhand.match(regex)[0];
						}if(resultVal.outgoingqty == null || resultVal.outgoingqty == undefined || resultVal.outgoingqty == 0){
							document.form.OUTGOINGQTY.value = "0.000";
						}else{
							document.form.OUTGOINGQTY.value = resultVal.outgoingqty.match(regex)[0];
						}
						
						document.form.ITEM.value = resultVal.sItem;
						document.form.DESC.value = resultVal.sItemDesc;
                        document.form.UOM.value = resultVal.SalesUOM;
                        
                        
                        
                        
                        
                     	document.form.QTY.focus();
                     	
						//mprice=resultVal.minsprice;
						Convertedmprice=resultVal.minSellingConvertedUnitCost;
						
						if(resultVal.outgoingOBDiscount=='' || resultVal.outgoingOBDiscount=='0' ||resultVal.outgoingOBDiscount=='0.00'||resultVal.outgoingOBDiscount==undefined)
						{
							 document.form.CUSTOMERDISCOUNT.value=parseFloat("0.00000").toFixed(<%=numberOfDecimal%>);
							
							document.form.UNITPRICEDISCOUNT.value = resultVal.ConvertedUnitCost;
						}
						else
						{
							if(resultVal.OBDiscountType=="BYPERCENTAGE")
							{
								document.form.DISCOUNTTYPE.value ="BYPERCENTAGE";
								document.form.CUSTOMERDISCOUNT.value = resultVal.outgoingOBDiscount.match(regex)[0]+'%';
								discount = parseFloat((resultVal.ConvertedUnitCost*resultVal.outgoingOBDiscount)/100);
								price = parseFloat(resultVal.ConvertedUnitCost-((resultVal.ConvertedUnitCost*resultVal.outgoingOBDiscount)/100));
							}
							else
							{
								document.form.CUSTOMERDISCOUNT.value = parseFloat(resultVal.outgoingOBDiscount).toFixed(<%=numberOfDecimal%>);
								price = parseFloat(resultVal.outgoingOBDiscount);
							}
							var calAmount = parseFloat(price).toFixed(<%=numberOfDecimal%>);
							document.form.UNITPRICE.value = calAmount;
							document.form.UNITPRICERD.value =calAmount.match(regex)[0];
							document.form.UNITPRICEDISCOUNT.value =  calAmount;
						}
						<%if(showPreviousPurchaseCost.equals("1")) {%>
							getPreviousPurchaseOrderDetails();
						<%}%>
						<%if(showPreviousSalesCost.equals("1")) {%>
							getPreviousSalesOrderDetails();
						<%}%>
					} 
				}
			});
		
	}

function addRow() {
	var table = document.getElementById("MULTI_IB_REMARKS");
	var rowCount = table.rows.length;
	var rowCountRemarks = table.rows.length+1;
	var row = table.insertRow(rowCount);
	
	var remarkslblCell = row.insertCell(0); 
	remarkslblCell.innerHTML='&nbsp;';

	var remarksCell = row.insertCell(1);
	var remarksCellText =  "&nbsp;<div class=\"input-group\"> <INPUT class=\"form-control\" name=\"PRDREMARKS_"+rowCount+"\" ";
	remarksCellText = remarksCellText+ " id=\"PRDREMARKS_"+rowCount+"\" type = \"TEXT\" size=\"100\"   MAXLENGTH=\"100\">";
	remarksCellText = remarksCellText + "<a class=\"input-group-addon\"  href=\"#\" onClick=\"javascript:popUpWin('remarks_list.jsp?REMARKS='+form.PRDREMARKS"+'_'+rowCount+".value+'&TYPE=MULTI'+'&INDEX="+rowCount+"');\"><i class=\"glyphicon glyphicon-log-in\" style=\"font-size: 20px;\"></i></a></div>";
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
	var dono = document.form.DONO.value;
    var desc = document.form.DESC.value;
    var disc = document.form.CUSTOMERDISCOUNT.value;
    var Convertedmsprice = parseFloat(Convertedmprice);
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
			PONO:dono,
                            UOM:uom,
                            TYPE:"Sales",
                            DISC:disc.replace("%",""),
                            MINPRICE:Convertedmsprice,
			ACTION : "VALIDATE_PRODUCT_UOM"
			},
			dataType : "json",
			success : function(data) {
				
				if (data.status == "100") {
					var resultVal = data.result;
					var regex = /^[^\.]+?(?=\.0*$)|^[^\.]+?\..*?(?=0*$)|^[^\.]*$/g;
					Convertedmprice=resultVal.MinPriceWTC;
					if(resultVal.ConvertedUnitCost == null || resultVal.ConvertedUnitCost == undefined || resultVal.ConvertedUnitCost == 0){
						document.form.UNITPRICE.value = parseFloat("0.00000").toFixed(<%=numberOfDecimal%>);
						document.form.UNITPRICERD.value = "0.00000";
					}else{
						document.form.UNITPRICE.value = parseFloat(resultVal.ConvertedUnitCost).toFixed(<%=numberOfDecimal%>);
						document.form.UNITPRICERD.value = resultVal.ConvertedUnitCostWTC.match(regex)[0];
					}
					
					if(parseFloat(disc)>0)
                    {   
                    	if(document.form.DISCOUNTTYPE.value=="BYPERCENTAGE")
						{								 
							var getdist = disc.replace("%","");								
							discount = parseFloat((resultVal.ConvertedUnitCost*getdist)/100);
							price = parseFloat(resultVal.ConvertedUnitCost-((resultVal.ConvertedUnitCost*getdist)/100));
						}
						else
						{								
							price = parseFloat(resultVal.ConvertedDiscWTC);
 						}
                    	var calAmount = parseFloat(price).toFixed(<%=numberOfDecimal%>);
						document.form.UNITPRICE.value = parseFloat(calAmount).toFixed(<%=numberOfDecimal%>);
						document.form.UNITPRICERD.value =calAmount.match(regex)[0];
						document.form.UNITPRICEDISCOUNT.value =  calAmount;
						
                    }
				 	document.form.UOM.focus();
				} else {
					document.form.ITEM.value = "";
					document.form.ITEM.focus();
				}
			}
		});
}
getPreviousPurchaseOrderDetails();
}
  
function getPreviousPurchaseOrderDetails() {
	var uom = document.form.UOM.value;
	$.ajax({
		type : "POST",
		url : "/track/purchaseorderservlet",
		data : {
			ITEM : document.form.ITEM.value,
			PLANT : "<%=plant%>",
			CUSTCODE:"",
			UOM:uom,
			ROWS:"2",
			Submit : "GET_PREVIOUS_ORDER_DETAILS"
		},
		dataType : "json",
		success : function(data) {
			if(data.orders[0].PONO != undefined){
			var result = "<h4>Previours Purchase Cost Detail</h4>";
			result += "<table  style='font-size:15px' border='0' cellspacing='0' width='100%' class='table'>";

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
				$("#prevPurOrderDetail").html(result);
			}
			}
		}
	});
}

function getPreviousSalesOrderDetails() {
	$.ajax({
		type : "POST",
		url : "/track/deleveryorderservlet",
		data : {
			ITEM : document.form.ITEM.value,
			PLANT : "<%=plant%>",
			CUSTCODE:document.form.CUST_CODE.value,
			Submit : "GET_PREVIOUS_ORDER_DETAILS"
		},
		dataType : "json",
		success : function(data) {
			if(data.orders[0].DONO != undefined){
			var result = "<h4>Previours Sales Price Detail</h4>";
			result += "<table  style='font-size:15px' border='0' cellspacing='0' width='100%' class='table'>";
			result += "<tr>";
			result += "<thead style='background: #eaeafa'>";
			result += "<th>ORDER NO</th>";
			result += "<th>CUSTOMER</th>";
			result += "<th>DATE</th>";
			result += "<th>PRICE</th>";
			result += "</tr>";
			result += "</thead>";
			result += "<tbody>";
			
			$.each(data.orders, function( key, value ) {
				result += "<tr>";
				result += "<td>"+value.DONO+"</td>";
				result += "<td>"+value.CNAME+"</td>";
				result += "<td>"+value.COLLECTIONDATE+"</td>";
				result += "<td>"+parseFloat(value.UNITPRICE).toFixed(<%=numberOfDecimal%>)+"</td>";
				result += "</tr>";
			});
			result += "</tbody>";
			result += "</table>";
				$("#prevSalOrderDetail").html(result);
			}else{
				$("#prevSalOrderDetail").html("");
			}
		}
	});
}

function checkPreviousCost() {
	var previousCost = parseFloat($("#prevPurOrderDetail table tbody tr:first td:last").text());
	var price = parseFloat(document.form.UNITPRICE.value);
	if(price < previousCost){
		$("#costAlert").text("Entered Price is less than previous cost : "+previousCost);
		$("#costAlertModal").modal();
		return false;
	}else{
		document.form.submit();
    	return true;
	}
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
		 var uprice= parseFloat(document.getElementById('UNITPRICE').value);
			//var msprice = parseFloat(mprice);
			var Convertedmsprice = parseFloat(Convertedmprice);
	  if(document.form.ITEM.value == "")
	  {
	    alert("Please select an Product");
	    document.form.ITEM.focus();
	    return false;
	  }
	   else if(isNaN(document.form.QTY.value)) {alert("Please enter valid Quantity.");document.form.QTY.focus(); return false;}
	 
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
	   
	 
	  else if(document.form.UNITPRICE.value == ""){
	    alert("Please enter Unit Price");
	    document.form.UNITPRICE.focus();
	    return false;
	  }
	  if(document.form.UOM.value == ""){
		    alert("Please enter a UOM value");
		    document.form.UOM.focus();
		    document.form.UOM.select();
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
	  var priceamt = document.form.UNITPRICE.value;
	  if (priceamt.indexOf('.') == -1) priceamt += ".";
		var decNum = priceamt.substring(priceamt.indexOf('.')+1, priceamt.length);
		if (decNum.length > <%=DbBean.NOOFDECIMALPTSFORCURRENCY%>)
		{
			alert("Invalid more than <%=DbBean.NOOFDECIMALPTSFORCURRENCY%> digits after decimal in Unit Price");
			document.form.UNITPRICE.focus();
			return false;
			
		}
	 
	  if(isNaN(removeCommas(document.form.UNITPRICE.value)))
	  {
		  alert("Please Enter valid Unit Price");
		    document.form.UNITPRICE.focus();
		    return false;
	    
	  }
	 
	  /*else if(uprice != '0' && uprice < msprice){      
		
			alert("Price should not be less than minimum selling price");
	                 document.form.UNITPRICE.focus();
			return false;
		}*/
		else if(uprice != '0' && uprice < Convertedmsprice){      
			
			alert("Price should not be less than minimum selling price");
	             document.form.UNITPRICE.focus();
			return false;
		}
	  else{
	     // document.form.Submit.value="Add Product";
	     <%if(showPreviousPurchaseCost.equals("1")) {%>
	  		checkPreviousCost();
		<%}else{%>
		  	document.form.submit();
	    	return true;
	    <%}%>
	   }
	}
        </script>

<jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
	<jsp:param name="nobackblock" value="1" />
</jsp:include>
 

