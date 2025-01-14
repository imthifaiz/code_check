<%@page import="com.track.dao.PlantMstDAO"%>
<%@ include file="header.jsp" %>
<%
String title = "Rental Order-Add Product";
%>
<%@include file="sessionCheck.jsp" %>
<jsp:include page="header2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
</jsp:include>


<script language="JavaScript" type="text/javascript" src="js/general.js"></script>

<script language="JavaScript" type="text/javascript" src="js/json2.js"></script>
<SCRIPT LANGUAGE="JavaScript">
var subWin = null;
function popUpWin(URL) {
 subWin = window.open(URL, 'Items', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=800,height=400,left = 200,top = 184');
}

var subWinForDiscount = null;
function popUpWinForDiscount(URL) {
  subWinForDiscount = window.open(URL, 'Calculate', 'toolbar=0,scrollbars=no,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=800,height=400,left = 400,top = 184');
}
</script>
<script language="javascript">

function validateForm(){
	var frmRoot=document.form;
	 var table = document.getElementById("MULTI_IB_REMARKS");
	 var rowCount = table.rows.length;
	 var checkcnt = rowCount-1;
	 document.form.DYNAMIC_REMARKS_SIZE.value = rowCount;
	 for(var i=0;i<rowCount;i++){
		   var prdremarks= document.getElementById("PRDREMARKS"+"_"+i);
	 }
  if(document.form.ITEM.value == "")
  {
    alert("Please select an Product ID");
    document.form.ITEM.focus();
    return false;
  }
 //else if(isNaN(document.form.QTY.value)) {alert("Please enter valid Quantity.");document.form.QTY.focus(); return false;}
 
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
   
  /*else if(document.form.QTY.value <= 0){
	    alert("Please enter > 0 quantity");
	    document.form.QTY.focus();
	    return false;
	  }*/
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
	  var priceamt = document.form.RENTALPRICE.value;
	  if (priceamt.indexOf('.') == -1) priceamt += ".";
		var decNum = priceamt.substring(priceamt.indexOf('.')+1, priceamt.length);
		if (decNum.length > <%=DbBean.NOOFDECIMALPTSFORCURRENCY%>)
		{
			alert("Invalid more than <%=DbBean.NOOFDECIMALPTSFORCURRENCY%> digits after decimal in Rental Price");
			document.form.RENTALPRICE.focus();
			return false;
			
		}
  else{
	// document.form.Submit.value="Add Product";
	  document.form.submit();
	    return true; 
   
   }
}




</script>

<script language="javascript">

function ref()
{
	document.form.ITEM.focus();
	document.form.ITEM.value="";
}

</script>



<jsp:useBean id="gn"  class="com.track.gates.Generator" />
<jsp:useBean id="sl"  class="com.track.gates.selectBean" />
<jsp:useBean id="db"  class="com.track.gates.defaultsBean" />
<jsp:useBean id="su"  class="com.track.util.StrUtils" />
<jsp:useBean id="tpdb"  class="com.track.tables.DODET" />
<jsp:useBean id="tphb"  class="com.track.tables.DOHDR" />
<jsp:useBean id="imb"  class="com.track.tables.ITEMMST" />
<jsp:useBean id="UomDAO"  class="com.track.dao.UomDAO" />
<%

tpdb.setmLogger(mLogger);
db.setmLogger(mLogger);
imb.setmLogger(mLogger);
tphb.setmLogger(mLogger);
    session= request.getSession();
    PlantMstDAO plantMstDAO = new PlantMstDAO();
    String plant=su.fString((String)session.getAttribute("PLANT"));
    String action  = su.fString(request.getParameter("Submit")).trim();
    String dono    = su.fString(request.getParameter("DONO"));
    String jobNum  = su.fString(request.getParameter("JOB_NUM"));
    String custName  = su.replaceCharacters2Recv(su.fString(request.getParameter("CUST_NAME")));
    String custCode  = su.fString(request.getParameter("CUST_CODE"));
    String deldate = su.fString(request.getParameter("DELDATE"));
    String RFLAG=    (String) session.getAttribute("RFLAG");
    String UNITPRICE=su.fString(request.getParameter("UNITPRICE"));
    String uom =su.fString(request.getParameter("UOM"));
    String PRDREMARKS = su.fString(request.getParameter("PRDREMARKS"));
    String RENTALPRICEDISCOUNT= su.fString(request.getParameter("RENTALPRICEDISCOUNT"));
    String result ="", sql="";       int n=1;
    String enrolledBy = (String)session.getAttribute("LOGIN_USER");
    String numberOfDecimal = plantMstDAO.getNumberOfDecimal(plant);
    if (action.equalsIgnoreCase("Go"))
    {
      response.sendRedirect("createLOANDET.jsp?DONO="+dono);
    }
   

%>
<body onload="ref()">
<div class="container-fluid m-t-20">
	 <div class="box"> 
            <div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title"><%=title %></h1>
		</div>
		
 <div class="box-body">
<input type="number" id="numberOfDecimal" style="display:none;" value=<%=numberOfDecimal%>>
<form class="form-horizontal" name="form" method="post" action="/track/loanorderservlet?Submit=Add Product" onSubmit="return validateForm()">
      <div class="col-sm-6"> 
      <div class="form-group">
      <label class="control-label col-sm-4" for="Loan Order No">Order Number:</label>
      <div class="col-sm-6">
      <INPUT type = "TEXT" class="form-control" size="50"  MAXLENGTH=20 name="DONO"   value="<%=dono%>" READONLY>
	  </div>
      </div>
      
	  <div class="form-group">
      <label class="control-label col-sm-4" for="Assignee Name">Customer Name:</label>
      <div class="col-sm-6">          
      <INPUT type = "TEXT"  class="form-control" size="50"  MAXLENGTH=20 name="CUST_NAME"  value="<%=su.forHTMLTag(custName)%>" READONLY>
      <INPUT type = "hidden" size="50"  MAXLENGTH=20 name="CUST_CODE"  value="<%=custCode%>" READONLY>
      </div>
      </div>
      
      <div class="form-group">
      <label class="control-label col-sm-4" for="Order Line No">Order Line No:</label>
      <div class="col-sm-6">          
      <INPUT type = "TEXT"  class="form-control" size="50"  MAXLENGTH=6 name="DOLNNO" value="<%=tpdb.getNextLoanOrderLineNo(dono,plant)%>" READONLY>
      </div>
      </div>
      
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
      <label class="control-label col-sm-4" for="Unit Price">Rental Price:</label>
      <div class="col-sm-6">           
      <INPUT  type = "text" size="20"  MAXLENGTH=20 name="LISTPRICE" id="LISTPRICE" class="form-control" READONLY>
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
      
      <div class="col-sm-6">
      <div class="form-group">
      <label class="control-label col-sm-3" for="Product ID">
      <a href="#" data-placement="left">
   	  <i class="glyphicon glyphicon-star" style="font-size: 10px; top:-6px; color: #e50000"></i></a>&nbsp;Product ID:</label>
      <div class="col-sm-6">
      <div class="input-group">    
      <INPUT class="form-control" type = "TEXT" size="30"  MAXLENGTH=50 
      onkeypress="if((event.keyCode=='13') && ( document.form.ITEM.value.length > 0)) {validateProduct();}" name="ITEM" >
      <span class="input-group-addon" 
   	  onClick="javascript:popUpWin('item_list_order.jsp?ITEM='+form.ITEM.value);"> 	
      <a href="#" data-toggle="tooltip" data-placement="top" title="Product Details">
  	  <i class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span>
      </div>
  	  </div>
      </div>
      
      <div class="form-group">
      <label class="control-label col-sm-3" for="Discription">
      <a href="#" data-placement="left">
   	  <i class="glyphicon glyphicon-star" style="font-size: 10px; top:-6px; color: #e50000"></i></a>&nbsp;Description:</label>
      <div class="col-sm-6">
      <div class="input-group">    
      <INPUT class="form-control"  type = "TEXT" size="30"  MAXLENGTH=100 name="DESC" >
      <span class="input-group-addon" 
   	  onClick="javascript:popUpWin('item_list_order.jsp?ITEM='+form.ITEM.value+'&DESC='+form.DESC.value);"> 	
      <a href="#" data-toggle="tooltip" data-placement="top" title="Product Details">
  	  <i class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span>
      </div>
  	  </div>
      </div>
      
      <INPUT class="inactive" type = "hidden" size="40"  MAXLENGTH=20 name="REMARK1">
      <INPUT class="inactive" type = "hidden" size="40"  MAXLENGTH=20 name="REMARK2">
      <INPUT class="inactive" type = "hidden" size="40"  MAXLENGTH=20 name="REMARK3">
      <INPUT class="inactive" type = "hidden" size="40"  MAXLENGTH=20 name="ITEM_CONDITION">
	<INPUT type="Hidden" name="RENTALPRICEDISCOUNT" value="<%=RENTALPRICEDISCOUNT%>">
	
	
	  <div class="form-group">
      <label class="control-label col-sm-3" for="Quantity">
      <a href="#" data-placement="left">
   	  <i class="glyphicon glyphicon-star" style="font-size: 10px; top:-6px; color: #e50000"></i></a>&nbsp;Quantity:</label>
      <div class="col-sm-6">  
      <div class="input-group">        
      <INPUT class="form-control"  type = "TEXT" size="30"  MAXLENGTH=20 name="QTY" onchange="validToThreeDecimal(this.value)"  onkeypress="if((event.keyCode=='13') && ( this.value.length > 0)){validateForm();}">
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
      <label class="control-label col-sm-3" for="Rental Price">
      <a href="#" data-placement="left">
   	  <i class="glyphicon glyphicon-star" style="font-size: 10px; top:-6px; color: #e50000"></i></a>&nbsp;Rental Price:</label>
      <div class="col-sm-6">
      <div class="input-group">          
      <INPUT class="form-control"  type = "TEXT" size="30"  MAXLENGTH=20 name="RENTALPRICE" id="RENTALPRICE"  autocomplete="off" onchange="validDecimal(this.value)">
       <span class="input-group-btn">
       <button class="Submit btn btn-default" type="button" name="Calculate"  
      onClick="javascript:popUpWinForDiscount('calcAmt_Rental.jsp?CURRENTAMOUNT='+form.RENTALPRICE.value);">
      <b>Discount</b></button></span>  
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
     <!--  <button type="button" class="Submit btn btn-default" name="Submit" onclick="deleteRow('MULTI_IB_REMARKS');"><b>Delete Last Remarks</b></button>&nbsp;&nbsp; -->
      </div>
      <INPUT type="hidden" name="DYNAMIC_REMARKS_SIZE">
      </div>
      </div>
      </div>
                     
      <INPUT type = "Hidden"   name="JOB_NUM"  value="<%=jobNum%>">
      <INPUT type="Hidden" name="ENCRYPT_FLAG" value="1">
      <INPUT type = "Hidden"  name="VENDNAME"  value="<%=su.forHTMLTag(custName)%>">
      <INPUT type = "Hidden"  name="LOGIN_USER"  value="<%=enrolledBy%>">
      <INPUT type="Hidden" name="RFLAG" value="3">
      <INPUT type = "Hidden"  name="UNITPRICE"  value="<%=UNITPRICE%>">
      <INPUT type="Hidden" name="RENTALPRICERD" id="RENTALPRICERD">
      <div class="form-group">        
      <div class="col-sm-offset-4 col-sm-8">
      <button type="button" class="Submit btn btn-default" 
      onClick="window.location.href='/track/loanorderservlet?DONO=<%=dono%>&Submit=View';">
      <b>Back</b></button>&nbsp;&nbsp;
      
      <button type="button" class="Submit btn btn-default" name="Submit" onclick="return validateForm();">
      <b>Add Product & Save</b></button>&nbsp;&nbsp;
      
      </div>
      </div>
      </form>
      </div>
      </div>
      </div>
      
      
      <script>
	$(document).ready(function(){
// 		$("#RENTALPRICE").bind("change paste keyup", function() {
// 			validDecimal($(this).val()); 
// 			});
    $('[data-toggle="tooltip"]').tooltip();   
	});
	</script>					
      </body>
	  

<Script>
function validateProduct() {
	var productId = document.form.ITEM.value;
	 var dono = document.form.DONO.value;
        var urlStr = "/track/ItemMstServlet";
		$.ajax( {
			type : "POST",
			url : urlStr,
			data : {
				ITEM : productId,
				PLANT : "<%=plant%>",
				DONO:dono,
				TYPE:"RENTAL",
				ACTION : "GET_PRODUCT_DETAILS"
				},
				dataType : "json",
				success : function(data) {
					
					if (data.status == "100") {
						var resultVal = data.result;
						var regex = /^[^\.]+?(?=\.0*$)|^[^\.]+?\..*?(?=0*$)|^[^\.]*$/g;
					/* 	if(resultVal.RentalPrice == null || resultVal.RentalPrice == undefined || resultVal.RentalPrice == 0 ){
							document.form.LISTPRICE.value = "0.00000";
						}else{
							document.form.LISTPRICE.value = resultVal.RentalPrice.match(regex)[0];
						} */
						if(resultVal.ConvertedUnitCost == null || resultVal.ConvertedUnitCost == undefined || resultVal.ConvertedUnitCost == 0 ){
							document.form.LISTPRICE.value = "0.00000";
						}else{
							document.form.LISTPRICE.value = resultVal.ConvertedUnitCost.match(regex)[0];
							}												
						if(resultVal.PRODGST == null || resultVal.PRODGST == undefined || resultVal.PRODGST == 0 ){
							document.form.PRODGST.value = "0.000";
						}else{
							document.form.PRODGST.value = resultVal.PRODGST.match(regex)[0];
						}
						if(resultVal.stockonhand == null || resultVal.stockonhand == undefined || resultVal.stockonhand == 0){
							document.form.STOCKONHAND.value = "0.000";
						}else{
							document.form.STOCKONHAND.value = resultVal.stockonhand.match(regex)[0];
						}if(resultVal.outgoingqtyloan == null || resultVal.outgoingqtyloan == undefined || resultVal.outgoingqtyloan == 0){
							document.form.OUTGOINGQTY.value = "0.000";
						}else{
							document.form.OUTGOINGQTY.value = resultVal.outgoingqtyloan.match(regex)[0];
						}
						 if(resultVal.ConvertedUnitCost == null || resultVal.ConvertedUnitCost == undefined || resultVal.ConvertedUnitCost == 0){
								document.form.RENTALPRICE.value = parseFloat("0.00000").toFixed(<%=numberOfDecimal%>);
								document.form.RENTALPRICERD.value = "0.00000";
							}else{
								 document.form.RENTALPRICE.value = parseFloat(resultVal.ConvertedUnitCost).toFixed(<%=numberOfDecimal%>);
								 document.form.RENTALPRICERD.value = resultVal.ConvertedUnitCostWTC.match(regex)[0];
							} 
						document.form.ITEM.value = resultVal.sItem;
						document.form.DESC.value = resultVal.sItemDesc;
                        document.form.UOM.value = resultVal.RentalUOM;
						document.form.QTY.focus();
						
						
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
	    var disc = 1;	    
	   
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
	                            TYPE:"Rental",
				ACTION : "VALIDATE_PRODUCT_UOM"
				},
				dataType : "json",
				success : function(data) {
					
					if (data.status == "100") {
						var resultVal = data.result;
						var regex = /^[^\.]+?(?=\.0*$)|^[^\.]+?\..*?(?=0*$)|^[^\.]*$/g;
						
						if(resultVal.ConvertedUnitCost == null || resultVal.ConvertedUnitCost == undefined || resultVal.ConvertedUnitCost == 0){
							document.form.RENTALPRICE.value = parseFloat("0.00000").toFixed(<%=numberOfDecimal%>);
							document.form.RENTALPRICERD.value = "0.00000";
						}else{
							document.form.RENTALPRICE.value = parseFloat(resultVal.ConvertedUnitCost).toFixed(<%=numberOfDecimal%>);
							document.form.RENTALPRICERD.value = resultVal.ConvertedUnitCostWTC.match(regex)[0];
						}
						//alert(disc);
	                    if(disc!="0.00000")
	                    {
// 	                    	if(document.form.DISCOUNTTYPE.value=="BYPERCENTAGE")
// 							{								 
// 								var getdist = disc.replace("%","");								
// 								discount = parseFloat((resultVal.ConvertedUnitCost*getdist)/100);
// 								price = parseFloat(resultVal.ConvertedUnitCost-((resultVal.ConvertedUnitCost*getdist)/100));
// 							}
// 							else
// 							{								
// 								price = parseFloat(disc);
// 	 						}
	                    	var calAmount = parseFloat(price).toFixed(5);
							document.form.RENTALPRICE.value = calAmount.match(regex)[0];
							document.form.RENTALPRICERD.value =calAmount.match(regex)[0];
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
	}
        
        </Script>
        <script>
        function validDecimal(str){
        	if (str.indexOf('.') == -1) str += ".";
        	var decNum = str.substring(str.indexOf('.')+1, str.length);
        	var declength =	parseInt(document.getElementById("numberOfDecimal").value);
        	if (decNum.length > declength)
        	{
        		alert("Invalid more than "+declength+" digits after decimal in Rental Price");
        		document.form.RENTALPRICE.focus();
        		return false;
        		
        	}

        	else
        	{
        	var rentalprice = document.form.RENTALPRICE.value;		   
        	document.getElementById("RENTALPRICERD").value =rentalprice;
        	}
        }
        
        function validToThreeDecimal(str) {
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
        
        <jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
</jsp:include>
