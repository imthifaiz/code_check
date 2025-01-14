<%@ include file="header.jsp" %>
<%@ page import="com.track.dao.*"%>
<%
String title = "Purchase Order";
%>
<%@include file="sessionCheck.jsp" %>
<jsp:include page="header2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
</jsp:include>

<script type="text/javascript" src="js/receiving.js"></script>
<script type="text/javascript" src="js/general.js"></script>
<script type="text/javascript" src="js/calendar.js"></script>
<script type="text/javascript" src="js/json2.js"></script>

<script type="text/javascript">
<!--
function validateForm(){
 if(document.form.ITEM.value == ""){
    alert("Please select an Product");
    document.form.ITEM.focus();
    return false;
 }
 if(document.form.QTYOR.value ==""){
   alert("Please enter quantity");
   document.form.QTYOR.focus();
   return false;
 }

}

function onDelete(form)
{
	 var frmRoot=document.form;
	 var table = document.getElementById("MULTI_IB_REMARKS");
	 var rowCount = table.rows.length;
	 var checkcnt = rowCount-1;
	 document.form.DYNAMIC_REMARKS_SIZE.value = rowCount;
	 for(var i=0;i<rowCount;i++){
		   var prdremarks= document.getElementById("PRDREMARKS"+"_"+i);
		   
	 }
    if (form.PONO.value.length < 1)
    {
    alert("Please Enter Purchase Order No !");
    form.PONO.focus();
    return false;
    }
    else{
     var mes=confirm("Do you want to Delete the Product ID !");
      if(mes==true)
      {
    
       document.form.action="/track/purchaseorderservlet?Submit=DeleteProduct";
                    document.form.submit();
      }
      else
      {  
      return  false;
      }
    }
    
}
//start code by deen to add price editable on edit sales order on 26/02/2013
function onUpdate()
{
	
	 var frmRoot=document.form;
	 var table = document.getElementById("MULTI_IB_REMARKS");
	 var rowCount = table.rows.length;
	 
	 var checkcnt = rowCount-1;
	 document.form.DYNAMIC_REMARKS_SIZE.value = rowCount;
	for(var i=0;i<rowCount;i++){
		   var prdremarks= document.getElementById("PRDREMARKS"+"_"+i);
     }
	  if(document.form.QTY.value == ""){
	    alert("Please enter Quantity");
	    document.form.QTY.focus();
	    return false;
	  }
	  if(!IsNumeric(removeCommas(document.form.QTY.value)))
	   {
	     alert(" Please Enter valid  Qty !");
	     form.QTY.focus();  form.QTY.select(); return false;
	  }
	  if(document.form.UNITCOST.value == ""){
	    alert("Please enter Unit Cost");
	    document.form.UNITCOST.focus();
	    return false;
	  }
	  var costamt = document.form.UNITCOST.value;
		if (costamt.indexOf('.') == -1) costamt += ".";
		var cdecNum = costamt.substring(costamt.indexOf('.')+1, costamt.length);
		var declength =	parseInt(document.getElementById("numberOfDecimal").value);
		if (cdecNum.length > declength)
		{
			alert("Invalid more than "+declength+" digits after decimal in Unit Cost");
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
	  if(isNaN(document.form.UNITCOST.value))
	  {
		  alert("Please Enter valid Unit Cost");
		    document.form.UNITCOST.focus();
		    return false;
	    
	  	}
	 else{
		  		document.form.action="/track/purchaseorderservlet?Submit=Updatepodet";
                document.form.submit();
			
		   }
}
function validDecimal(str){
	if (str.indexOf('.') == -1) str += ".";
	var decNum = str.substring(str.indexOf('.')+1, str.length);
	var declength =	parseInt(document.getElementById("numberOfDecimal").value);
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
//start code by radhika to add cost editable on edit outbound order on 26/02/2013
//-->
</script>

<jsp:useBean id="gn"  class="com.track.gates.Generator" />
<jsp:useBean id="sl"  class="com.track.gates.selectBean" />
<jsp:useBean id="db"  class="com.track.gates.defaultsBean" />
<jsp:useBean id="su"  class="com.track.util.StrUtils" />
<jsp:useBean id="ddb"  class="com.track.tables.PODET" />
<jsp:useBean id="dhb"  class="com.track.tables.POHDR" />

<%
	POUtil _POUtil=new POUtil();
	PlantMstDAO plantMstDAO = new PlantMstDAO();
	db.setmLogger(mLogger);
	ddb.setmLogger(mLogger);
	dhb.setmLogger(mLogger);
	  String plant=StrUtils.fString((String)session.getAttribute("PLANT"));
    String pono    = StrUtils.fString(request.getParameter("PONO"));
    String polnno  = StrUtils.fString(request.getParameter("POLNNO"));
    String item  = StrUtils.fString(request.getParameter("ITEM"));
  //start code by deen to add price editable on edit sales order on 26/02/2013
    String desc  = StrUtils.fString(request.getParameter("DESC"));
     String PRDREMARKS  = StrUtils.fString(request.getParameter("PRDREMARKS"));
    String recvstatus = StrUtils.fString(request.getParameter("RECVSTATUS"));
    String qty1  = su.removeFormat(StrUtils.fString(request.getParameter("QTY")));
    Float qty = new Float(qty1);
    String uom  = StrUtils.fString(request.getParameter("UOM"));
    String nonStkFlg  = StrUtils.fString(request.getParameter("NONSTOCKFLG"));
    String cost1  = su.removeFormat(su.replaceCharacters2Recv(StrUtils.fString(request.getParameter("COST"))));
    double cost = Double.parseDouble(cost1);
    String result ="", sql="";       int n=1;
    String enrolledBy = (String)session.getAttribute("LOGIN_USER");
    String RFLAG=     StrUtils.fString(request.getParameter("RFLAG"));
    String qtyReceived1  = su.removeFormat(StrUtils.fString(request.getParameter("QTYRECEIVED")));
    String MINSTKQTY = StrUtils.fString(request.getParameter("MINSTKQTY"));
    String MAXSTKQTY = StrUtils.fString(request.getParameter("MAXSTKQTY"));
    String STOCKONHAND = StrUtils.fString(request.getParameter("STOCKONHAND"));
    String INCOMINGQTY = StrUtils.fString(request.getParameter("INCOMINGQTY"));
    String LISTCOST= StrUtils.fString(request.getParameter("LISTCOST"));
    String SUPPLIERDISCOUNT= StrUtils.fString(request.getParameter("SUPDIS"));
    String DISTYPE= StrUtils.fString(request.getParameter("DISTYPE"));
    String PRODGST=StrUtils.fString(request.getParameter("PRODGST"));
    String unitCostrd =StrUtils.fString(request.getParameter("COSTRD"));
    String PRODUCTDELIVERYDATE =StrUtils.fString(request.getParameter("PRODUCTDELIVERYDATE"));
    String unitCost = String.valueOf(cost); 
    String numberOfDecimal = plantMstDAO.getNumberOfDecimal(plant);
    ddb.selectPODET(plant);
	LISTCOST=LISTCOST.replace(",", "");
    float gstVatValue ="".equals(PRODGST) ? 0.0f :  Float.parseFloat(PRODGST);
	/* float listCostValue ="".equals(LISTCOST) ? 0.0f :  Float.parseFloat(LISTCOST); */
	float maxStockQtyValue ="".equals(MAXSTKQTY) ? 0.0f :  Float.parseFloat(MAXSTKQTY);
	float minStockQtyValue ="".equals(MINSTKQTY) ? 0.0f :  Float.parseFloat(MINSTKQTY);
	float incomingQtyValue ="".equals(INCOMINGQTY) ? 0.0f :  Float.parseFloat(INCOMINGQTY);
	float stockOnHandValue ="".equals(STOCKONHAND) ? 0.0f :  Float.parseFloat(STOCKONHAND);
	/* float supplierDiscountValue ="".equals(SUPPLIERDISCOUNT) ? 0.0f :  Float.parseFloat(SUPPLIERDISCOUNT);
	float unitCostValue ="".equals(unitCost) ? 0.0f :  Float.parseFloat(unitCost); */
	float unitCostrdValue ="".equals(unitCostrd) ? 0.0f :  Float.parseFloat(unitCostrd);
	
	double listCostValue ="".equals(LISTCOST) ? 0.0d :  Double.parseDouble(LISTCOST);
	double unitCostValue ="".equals(unitCost) ? 0.0d :  Double.parseDouble(unitCost);
	double supplierDiscountValue ="".equals(SUPPLIERDISCOUNT) ? 0.0d :  Double.parseDouble(SUPPLIERDISCOUNT);
	
	if(gstVatValue==0f){
		PRODGST="0.000";
	}else{
		PRODGST=PRODGST.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
	}/* if(listCostValue==0f){
		LISTCOST="0.00000";
	}else{
		LISTCOST=LISTCOST.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
	} */if(minStockQtyValue==0f){
		MINSTKQTY="0.000";
	}else{
		MINSTKQTY=MINSTKQTY.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
	}if(maxStockQtyValue==0f){
		MAXSTKQTY="0.000";
	}else{
		MAXSTKQTY=MAXSTKQTY.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
	}if(incomingQtyValue==0f){
		INCOMINGQTY="0.000";
	}else{
		INCOMINGQTY=INCOMINGQTY.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
	}if(stockOnHandValue==0f){
		STOCKONHAND="0.000";
	}else{
		STOCKONHAND=STOCKONHAND.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
	}
	
	LISTCOST = StrUtils.addZeroes(listCostValue, numberOfDecimal);
    
    
	 if(DISTYPE.equalsIgnoreCase("BYPERCENTAGE") && supplierDiscountValue==0d){
		SUPPLIERDISCOUNT="0.000"+"%";
	}/* else if(!DISTYPE.equalsIgnoreCase("BYPERCENTAGE") && supplierDiscountValue==0f){
		SUPPLIERDISCOUNT="0.00000";
	} */else if(DISTYPE.equalsIgnoreCase("BYPERCENTAGE") && supplierDiscountValue!=0d){
		SUPPLIERDISCOUNT=SUPPLIERDISCOUNT.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "")+"%";
	}else{
		/* SUPPLIERDISCOUNT=SUPPLIERDISCOUNT.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", ""); */
		SUPPLIERDISCOUNT = StrUtils.addZeroes(supplierDiscountValue, numberOfDecimal);
	}
	/* if(unitCostValue==0f){
		unitCost="0.00000";
	}else{
		unitCost=unitCost.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
	} */
	unitCost = StrUtils.addZeroes(unitCostValue, numberOfDecimal);
	
	if(unitCostrdValue==0f){
		unitCostrd="0.00000";
	}else{
		unitCostrd=unitCostrd.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
	}
    
    /* if(DISTYPE.equalsIgnoreCase("BYPERCENTAGE")){
    	SUPPLIERDISCOUNT=SUPPLIERDISCOUNT+"%";
    } */
     Float qtyReceived = new Float(qtyReceived1);
     if ((pono.length()>0)||(polnno.length()>0))
    {
      ddb.setPONO(pono);
      ddb.setPOLNNO(polnno);
      ddb.setITEM(item);
      ddb.setITEMDESC(desc);
       ddb.setQTYOR(qty);
      ddb.setUNITMO(uom);
      ddb.setCOST(cost);
      ddb.selectPODET(plant);
	 
    }
  //End code by deen to add price editable on edit sales order on 26/02/2013
%>
<div class="container-fluid m-t-20">
	 <div class="box"> 
            <div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title"><%=title %></h1>
		</div>
		
 <div class="box-body">
 <input type="number" id="numberOfDecimal" style="display:none;" value=<%=numberOfDecimal%>>
<form class="form-horizontal" name="form" method="post">
     
     <div class="col-sm-6">  <!-- column is used for grid function -->
     
      <div class="form-group">
      <label class="control-label col-sm-4" for="Transfer Order No">Order Number:</label>
      <div class="col-sm-6">
      <INPUT type = "TEXT" class="form-control" size="20"  MAXLENGTH=50 name="PONO" value="<%=ddb.getPONO()%>" READONLY>
	  </div>
      </div>
            
      <div class="form-group">
      <label class="control-label col-sm-4" for="Order Line No">Order Line No:</label>
      <div class="col-sm-6">          
      <INPUT type = "TEXT"  class="form-control"  size="5"  MAXLENGTH=6 name="POLNNO" value="<%=ddb.getPOLNNO()%>" READONLY>
      </div>
      </div>
      
      <div class="form-group">
      <label class="control-label col-sm-4" for="Product VAT/GST">Product VAT:</label>
      <div class="col-sm-6">
      <div class="input-group">          
      <INPUT class="form-control"  type = "TEXT" size="20"  MAXLENGTH=20 name="PRODGST" id="PRODGST" value="<%=PRODGST%>"class="inactivegry" READONLY>
      <span class="input-group-addon" style="font-size: 20px; color: #0059b3"><b>%</b></span>
      </div>
      </div>
      </div>
     
      <div class="form-group">
      <label class="control-label col-sm-4" for="Unit Price">List Cost:</label>
      <div class="col-sm-6">          
      <INPUT  type = "TEXT" style="width: 100%"  value="<%=LISTCOST%>" MAXLENGTH=20 name="LISTCOST"  class="form-control" READONLY>
      </div>
      </div>
      
      <div class="form-group">
      <label class="control-label col-sm-4" for="List price">Supplier Discount:</label>
      <div class="col-sm-6">
      <div class="input-group">  
      <INPUT  type = "TEXT" size="20"  value="<%=SUPPLIERDISCOUNT%>" MAXLENGTH=20 name="SUPPLIERDISCOUNT"  class="form-control" READONLY>
      <span class="input-group-addon" style="font-size: 12px;"><b>(By Cost or Percentage)</b></span>      
      </div>
      </div>
      </div>
      
      <div class="form-group">
      <label class="control-label col-sm-4" for="Min Stock Quantity">Min Stock Quantity:</label>
      <div class="col-sm-6">
       <INPUT  type = "TEXT" size="20"  value="<%=MINSTKQTY%>" MAXLENGTH=20 name="MINSTKQTY"  class="form-control" READONLY>
      </div>
       </div>
       
       <div class="form-group">
      <label class="control-label col-sm-4" for="Max Stock Quantity">Max Stock Quantity:</label>
      <div class="col-sm-6">          
      <INPUT  type = "TEXT" size="20"  MAXLENGTH=20 name="MAXSTKQTY"  value="<%=MAXSTKQTY%>" class="form-control" READONLY>
      </div>
      </div>
      
      <div class="form-group">
      <label class="control-label col-sm-4" for="Out going Quantity">Incoming Quantity:</label>
      <div class="col-sm-6">          
      <INPUT  type = "TEXT" size="20"  MAXLENGTH=20 name="INCOMINGQTY"  value="<%=INCOMINGQTY%>" class="form-control" READONLY>
      </div>
      </div>
       
      <div class="form-group">
      <label class="control-label col-sm-4" for="Stock On Hand">Stock On Hand:</label>
      <div class="col-sm-6">          
      <INPUT  type = "TEXT" size="20"  MAXLENGTH=20 name="STOCKONHAND"  value="<%=STOCKONHAND%>" class="form-control" READONLY>
      </div>
      </div>
      
      </div>
      
      <div class="col-sm-6">  <!-- column is used for grid function -->
      
      <div class="form-group">
      <label class="control-label col-sm-4" for="Product ID">Product ID:</label>
      <div class="col-sm-6">
      <INPUT type = "TEXT" style="width: 100%"  class="form-control"  MAXLENGTH=20 name="ITEM" value="<%=ddb.getITEM()%>" READONLY>
      </div>
  	  </div>
  	  
  	  <div class="form-group">
      <label class="control-label col-sm-4" for="Discription">Description:</label>
      <div class="col-sm-6">
      <INPUT  type = "TEXT" style="width: 100%" class="form-control" MAXLENGTH=100 name="DESC" value="<%=su.formatHTML(su.replaceCharacters2Recv(ddb.getITEMDESC()))%>" READONLY>
      </div>
      </div>
      
      <div class="form-group">
      <label class="control-label col-sm-4" for="Picked Quantity">Received Quantity:</label>
      <div class="col-sm-6">          
      <INPUT  type = "TEXT"  name="RECEIVEDQTY" style="width: 100%"  value="<%=su.formatNum(String.valueOf(qtyReceived))%>"  MAXLENGTH=20   class="form-control" READONLY>
      </div>
      </div>
      
      <div class="form-group">
      <label class="control-label col-sm-4" for="Quantity">
      <a href="#" data-placement="left">
   	  <i class="glyphicon glyphicon-star" style="font-size: 10px; top:-6px; color: #e50000"></i></a>&nbsp;Quantity:</label>
      <div class="col-sm-6"> 
      <div class="input-group">         
      <INPUT class="form-control"  type = "TEXT" style="width: 100%"  MAXLENGTH=20 name="QTY" id="QTY" onchange="validToThreeDecimal(this.value)" value="<%=su.formatNum(String.valueOf(ddb.getQTYOR()))%>" >
      <span class="input-group-btn"></span>    
      <INPUT  type = "TEXT" style="width: 100%" class="form-control" MAXLENGTH=20 name="UOM" value="<%=ddb.getUNITMO()%>" READONLY>
      </div>
      </div>
       </div>
       
      <div class="form-group">
      <label class="control-label col-sm-4" for="Unit Price">
      <a href="#" data-placement="left">
   	  <i class="glyphicon glyphicon-star" style="font-size: 10px; top:-6px; color: #e50000"></i></a>&nbsp;Unit Cost:</label>
      <div class="col-sm-6">          
      <INPUT class="form-control" type = "TEXT" style="width: 100%"  MAXLENGTH=20 name="UNITCOST" id="UNITCOST" value="<%=new java.math.BigDecimal(unitCost).toPlainString()%>"  onchange="validDecimal(this.value)">	
      </div>
       </div>
       
             <div class="form-group">
      <label class="control-label col-sm-4" for="Delivery Date">Delivery Date:</label>
      <div class="col-sm-6">          
      <INPUT  type = "TEXT"  name="PRODUCTDELIVERYDATE" style="width: 100%"  value="<%=PRODUCTDELIVERYDATE%>"  MAXLENGTH=20   class="form-control datepicker" READONLY>
      </div>
      </div>
      
      <div class="form-group">
	  <div class="col-sm-10">
      <table id="MULTI_IB_REMARKS">
        <% 
                      ArrayList al= _POUtil.listPoMultiRemarks(plant,pono,polnno);
                      String lblRemarks="";PRDREMARKS="";
                     // String strId="";
                    if(al.size()> 0)
                    	
                    {
	                     
	                     for(int i=0 ; i<al.size();i++)
	                      {
	                    	  Map m=(Map)al.get(i);
	                    	  lblRemarks="Remarks";
	                    	 // strId="PRDREMARKS"+i;
	                    	  PRDREMARKS = (String)m.get("remarks");
	                    %>
	                    <TR>
	                       	<%if(i==0){ %>
	                        		<TH WIDTH="19%" ALIGN="RIGHT" Style="font-size: 15px;">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<%=lblRemarks%>:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</TH>
	                        	<%}else{ %>
	                        		<TH>
	                       		 <%}%> 
	                       		 
	                      	 <TH  WIDTH="74%"><INPUT class="form-control" type = "TEXT" size="100"  MAXLENGTH="100" name="PRDREMARKS_<%=i%>" id="PRDREMARKS_<%=i%>" value="<%=PRDREMARKS%>" ></TH>
	                     </TR>
                     <%   } 
	                   }else {lblRemarks="Remarks1"; %>
	                     <TR>
	                        <TH WIDTH="19%" ALIGN="RIGHT" Style="font-size: 15px;">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<%=lblRemarks%>:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</TH>
	                      	 <TH  WIDTH="74%"><INPUT class="form-control"  type = "TEXT" size="100"  MAXLENGTH="100" name="PRDREMARKS_0" id="PRDREMARKS_0" value="<%=PRDREMARKS%>" ></TH>
	                     </TR>
                       <% } %>
                       
                   
               </TABLE>
                         
	 <INPUT type="hidden"	name="DYNAMIC_REMARKS_SIZE">
      </div>
      </div>
      
       </div>
           
      	<INPUT type="Hidden" name="ENCRYPT_FLAG" value="1">
        <INPUT type="Hidden" name="VENDNO" value="<%=dhb.getVENDNO()%>">
        <INPUT type="Hidden" name="RFLAG" value="<%=RFLAG%>">
      <INPUT type="Hidden" name="UNITCOSTRD" id="UNITCOSTRD" value="<%=new java.math.BigDecimal(unitCostrd).toPlainString()%>">
      <div class="form-group">        
      <div class="col-sm-12" align="center">
      <button type="button" class="Submit btn btn-default" onClick="window.location.href='/track/purchaseorderservlet?PONO=<%=pono%>&RFLAG=<%=RFLAG%>&Submit=View';"><b>Back</b></button>&nbsp;&nbsp;
      
                  <%if(recvstatus.equalsIgnoreCase("N")){%>
      <button type="button" class="Submit btn btn-default" id="Delete" name="Submit" onclick="return onDelete(document.form)"><b>Delete</b></button>&nbsp;&nbsp;
      <%} %>
      <button type="button" class="Submit btn btn-default" onClick="return onUpdate()"><b>Update</b></button>&nbsp;&nbsp;
      
      </div>
      
      </div>
     
      </form>
      </div>
      </div>
      </div>
      
<script type="text/javascript">
	$(document).ready(function(){
    $('[data-toggle="tooltip"]').tooltip();   
	});
	//start code by radhika to add price editable on edit outbound order on 26/02/2013
	//onpageload(); //commanded by deen on Oct 28 2014 to allow quantity to ammend
	function onpageload() {
		
		var status= "<%=recvstatus%>";
		if(status!="N"){
	    		document.getElementById("QTY").readOnly = true;
			document.getElementById("QTY").style.backgroundColor = "#C0C0C0";
			
		}
			
	
	}
	//End code by radhika to add price editable on edit outbound order on 26/02/2013
</script>

<jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
	<jsp:param name="nobackblock" value="1" />
</jsp:include>
