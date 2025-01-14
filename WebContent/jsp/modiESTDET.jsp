<%@ include file="header.jsp" %>
<%@ page import="com.track.dao.*"%>
<%
String title = "Sales Estimate Order";
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

function validateForm(){
 if(document.form.ITEM.value == ""){
    alert("Please select an item");
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
   
    if (form.ESTNO.value.length < 1)
    {
    alert("Please Enter Sales Estimate Order No !");
    form.ESTNO.focus();
    return false;
    }
    else{
     var mes=confirm("Do you want to Delete the Product ID !");
      if(mes==true)
      {
    	  document.form.action="/track/EstimateServlet?Submit=Delete";
	      document.form.submit();
     // document.form.Submit.value="Delete";
      }
      else
      {  
      return  false;
      }
    }
    
}

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
	  else if(!IsNumeric(removeCommas(document.form.QTY.value)))
	   {
	     alert(" Please Enter valid  Qty !");
	     form.QTY.focus();  form.QTY.select(); return false;
	  }
	  
	 
	  else if(document.form.UNITPRICE.value == ""){
	    alert("Please enter Unit Price");
	    document.form.UNITPRICE.focus();
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
		 var costamt = document.form.QTY.value;
		  if (costamt.indexOf('.') == -1) costamt += ".";
			var cdecNum = costamt.substring(costamt.indexOf('.')+1, costamt.length);
			if (cdecNum.length > 3)
			{
				alert("Invalid more than 3 digits after decimal in QTY");
				document.form.QTY.focus();
				return false;
				
			}
	  if(isNaN(document.form.UNITPRICE.value))
	  {
		  alert("Please Enter valid Unit Price");
		    document.form.UNITPRICE.focus();
		    return false;
	    
	  }
	  else{
		  document.form.action="/track/EstimateServlet?Submit=Updateestdet";
	      document.form.submit();
			 // document.form.Submit.value="Update";
			   // return true;
		   }
}
function validDecimal(str){
	if (str.indexOf('.') == -1) str += ".";
	var decNum = str.substring(str.indexOf('.')+1, str.length);
	var declength =	parseInt(document.getElementById("numberOfDecimal").value);
	if (decNum.length > declength)
	{
		alert("Invalid more than "+declength+" digits after decimal in Unit price");
		document.form.UNITPRICE.focus();
		return false;
		
	}
	else
	{
	var price = document.form.UNITPRICE.value;
	   
	  document.getElementById("UNITPRICERD").value =price;
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
<jsp:useBean id="ddb"  class="com.track.tables.DODET" />
<jsp:useBean id="dhb"  class="com.track.tables.DOHDR" />

<%

	db.setmLogger(mLogger);
	ddb.setmLogger(mLogger);
	dhb.setmLogger(mLogger);
	
	PlantMstDAO plantMstDAO = new PlantMstDAO();
    ESTUtil _ESTUtil=new ESTUtil();
    String plant=su.fString((String)session.getAttribute("PLANT"));
    String estno    = su.fString(request.getParameter("ESTNO"));
    String estlnno  = su.fString(request.getParameter("ESTLNNO"));
    String item  = su.fString(request.getParameter("ITEM"));
  //start code by radhika to add price editable on edit outbound order on 26/02/2013
    String desc  = su.fString(request.getParameter("DESC"));
     String prdRemarks  = su.fString(request.getParameter("PRDREMARKS"));
    String status = su.fString(request.getParameter("STATUS"));
    String qty1  = su.removeFormat(su.fString(request.getParameter("QTY")));
    Float qty = new Float(qty1);
    String uom  = su.fString(request.getParameter("UOM"));
    String nonStkFlg  = su.fString(request.getParameter("NONSTOCKFLG"));
    String price1  = su.removeFormat(su.replaceCharacters2Recv(su.fString(request.getParameter("PRICE"))));
    double price = Double.parseDouble(price1);
    String result ="", sql="";       int n=1;
    String enrolledBy = (String)session.getAttribute("LOGIN_USER");
    String qtyIssue1  = su.removeFormat(su.fString(request.getParameter("QTYISSUE")));
    Float qtyIssue = new Float(qtyIssue1);
    String listprice1  = su.removeFormat(su.replaceCharacters2Recv(su.fString(request.getParameter("LISTPRICE"))));
    String MINSTKQTY = su.fString(request.getParameter("MINSTKQTY"));
    String MAXSTKQTY = su.fString(request.getParameter("MAXSTKQTY"));
    String STOCKONHAND = su.fString(request.getParameter("STOCKONHAND"));
    String OUTGOINGQTY = su.fString(request.getParameter("OUTGOINGQTY"));
    String CUSTOMERDISCOUNT= su.fString(request.getParameter("CUSDIS"));
    String DISTYPE= su.fString(request.getParameter("DISTYPE"));
    String PRODGST=su.fString(request.getParameter("PRODGST"));
    String RFLAG=     su.fString(request.getParameter("RFLAG"));
    String unitPrice = String.valueOf(request.getParameter("PRICE"));
    String unitPricerd =StrUtils.fString(request.getParameter("COSTRD"));
    String PRODUCTDELIVERYDATE =StrUtils.fString(request.getParameter("PRODUCTDELIVERYDATE"));
    String numberOfDecimal = plantMstDAO.getNumberOfDecimal(plant);
    
    float gstVatValue ="".equals(PRODGST) ? 0.0f :  Float.parseFloat(PRODGST);
	float listCostValue ="".equals(listprice1) ? 0.0f :  Float.parseFloat(listprice1);
	float maxStockQtyValue ="".equals(MAXSTKQTY) ? 0.0f :  Float.parseFloat(MAXSTKQTY);
	float minStockQtyValue ="".equals(MINSTKQTY) ? 0.0f :  Float.parseFloat(MINSTKQTY);
	float outgoingQtyValue ="".equals(OUTGOINGQTY) ? 0.0f :  Float.parseFloat(OUTGOINGQTY);
	float stockOnHandValue ="".equals(STOCKONHAND) ? 0.0f :  Float.parseFloat(STOCKONHAND);
	/* float customerDiscountValue ="".equals(CUSTOMERDISCOUNT) ? 0.0f :  Float.parseFloat(CUSTOMERDISCOUNT); */
	float unitPriceValue ="".equals(unitPrice) ? 0.0f :  Float.parseFloat(unitPrice);
	float unitPricerdValue ="".equals(unitPricerd) ? 0.0f :  Float.parseFloat(unitPricerd);
	
	double customerDiscountValue ="".equals(CUSTOMERDISCOUNT) ? 0.0d :  Double.parseDouble(CUSTOMERDISCOUNT);

	if(gstVatValue==0f){
		PRODGST="0.000";
	}else{
		PRODGST=PRODGST.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
	}if(listCostValue==0f){
		listprice1="0.00000";
	}else{
		listprice1=listprice1.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
	}if(minStockQtyValue==0f){
		MINSTKQTY="0.000";
	}else{
		MINSTKQTY=MINSTKQTY.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
	}if(maxStockQtyValue==0f){
		MAXSTKQTY="0.000";
	}else{
		MAXSTKQTY=MAXSTKQTY.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
	}if(outgoingQtyValue==0f){
		OUTGOINGQTY="0.000";
	}else{
		OUTGOINGQTY=OUTGOINGQTY.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
	}if(stockOnHandValue==0f){
		STOCKONHAND="0.000";
	}else{
		STOCKONHAND=STOCKONHAND.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
	}if(unitPriceValue==0f){
		unitPrice="0.00000";
	}else{
		unitPrice=unitPrice.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
	}if(unitPricerdValue==0f){
		unitPricerd="0.00000";
	}else{
		unitPricerd=unitPricerd.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
	}
    
	 if(DISTYPE.equalsIgnoreCase("BYPERCENTAGE") && customerDiscountValue==0f){
		CUSTOMERDISCOUNT="0.000";
	}/* else if(!DISTYPE.equalsIgnoreCase("BYPERCENTAGE") && customerDiscountValue==0f){
		CUSTOMERDISCOUNT="0.00000";
	} */else if(DISTYPE.equalsIgnoreCase("BYPERCENTAGE") && customerDiscountValue!=0f){
		CUSTOMERDISCOUNT=CUSTOMERDISCOUNT.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "")+"%";
	}else{
		/* CUSTOMERDISCOUNT=CUSTOMERDISCOUNT.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", ""); */
		CUSTOMERDISCOUNT = StrUtils.addZeroes(customerDiscountValue, numberOfDecimal);
	} 
	
    
    	
    if ((estno.length()>0)||(estlnno.length()>0))
    {
      ddb.setsESTNO(estno);
      ddb.setsESTLNNO(estlnno);
      ddb.setsESTITEM(item);
      ddb.setsESTITEMDESC(desc);
      ddb.setsESTCOMMENT1(prdRemarks);
      ddb.setfESTQTYOR(qty);
      ddb.setsESTUNITMO(uom);
      ddb.setdESTPRICE(price);    
	 
    }
    double dListPrice1 = Double.parseDouble(StrUtils.fString(listprice1));
    listprice1 = StrUtils.addZeroes(dListPrice1, numberOfDecimal);
    
    double dUnitPrice = Double.parseDouble(StrUtils.fString(unitPrice));
    unitPrice = StrUtils.addZeroes(dUnitPrice, numberOfDecimal);
%>
<div class="container-fluid m-t-20">
	 <div class="box"> 
            <div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title"><%=title %></h1>
		</div>
		
 <div class="box-body">
 
<input type="number" id="numberOfDecimal" style="display:none;" value=<%=numberOfDecimal%>>
<form class="form-horizontal" name="form" method="post" action="/track/EstimateServlet?">

<div class="col-sm-6">  <!-- column is used for grid function -->
     
      <div class="form-group">
      <label class="control-label col-sm-4" for="Outbound Order No">Order Number:</label>
      <div class="col-sm-6">
      <INPUT type = "TEXT" class="form-control" size="20"  MAXLENGTH=50 name="ESTNO" value="<%=ddb.getsESTNO()%>" READONLY>
	  </div>
      </div>
            
      <div class="form-group">
      <label class="control-label col-sm-4" for="Order Line No">Order Line No:</label>
      <div class="col-sm-6">          
      <INPUT type = "TEXT"  class="form-control"  size="20"  MAXLENGTH=6 name="ESTLNNO" value="<%=ddb.getsESTLNNO()%>" READONLY>
      </div>
      </div>
      
      <div class="form-group">
      <label class="control-label col-sm-4" for="Product GST">Product VAT:</label>
      <div class="col-sm-6">
      <div class="input-group">          
     <INPUT  type = "TEXT" size="20"  MAXLENGTH=20 name="PRODGST" id="PRODGST" value="<%=PRODGST%>"class="form-control" READONLY>
      <span class="input-group-addon" style="font-size: 20px; color: #0059b3"><b>%</b></span>
      </div>
      </div>
      </div>
      
      
      <div class="form-group">
      <label class="control-label col-sm-4" for="List Price">List Price:</label>
      <div class="col-sm-6">          
      <INPUT  type = "TEXT" size="20"  MAXLENGTH=20 name="LISTPRICE"   id="LISTPRICE" value="<%=listprice1%>" class="form-control" READONLY>
      </div>
      </div>
            	
	  <div class="form-group">
      <label class="control-label col-sm-4" for="List price">Customer Discount:</label>
      <div class="col-sm-6">
      <div class="input-group">  
      <INPUT  type = "TEXT" size="20"  MAXLENGTH=20 name="CUSTOMERDISCOUNT"  value="<%=CUSTOMERDISCOUNT%>" class="form-control" READONLY>
      <span class="input-group-addon" style="font-size: 12px;"><b>(By Price or Percentage)</b></span>       
      </div>
      </div>
      </div>
       
                
      <div class="form-group">
      <label class="control-label col-sm-4" for="Min Stock Quantity">Min Stock Quantity:</label>
      <div class="col-sm-6">          
      <INPUT  type = "TEXT" size="20"  MAXLENGTH=20 name="MINSTKQTY"  value="<%=MINSTKQTY%>" class="form-control" READONLY>
      </div>
       </div>
       
       <div class="form-group">
      <label class="control-label col-sm-4" for="Max Stock Quantity">Max Stock Quantity:</label>
      <div class="col-sm-6">          
      <INPUT  type = "TEXT" size="20"  MAXLENGTH=20 name="MAXSTKQTY"  value="<%=MAXSTKQTY%>" class="form-control" READONLY>
      </div>
      </div>
       
      <div class="form-group">
      <label class="control-label col-sm-4" for="Stock On Hand">Stock On Hand:</label>
      <div class="col-sm-6">          
      <INPUT  type = "TEXT" size="20"  MAXLENGTH=20 name="STOCKONHAND"  value="<%=STOCKONHAND%>" class="form-control" READONLY>
      </div>
       </div>
       
      <div class="form-group">
      <label class="control-label col-sm-4" for="Out going Quantity">Outgoing Quantity:</label>
      <div class="col-sm-6">          
      <INPUT  type = "TEXT" size="20"  MAXLENGTH=20 name="OUTGOINGQTY"  value="<%=OUTGOINGQTY%>" class="form-control" READONLY>
      </div>
      </div>
      
      </div>
      
      <div class="col-sm-6">  <!-- column is used for grid function -->
      
      <div class="form-group">
      <label class="control-label col-sm-4" for="Product ID">Product ID:</label>
      <div class="col-sm-6">
      <INPUT type = "TEXT" style="width: 100%"  class="form-control"  MAXLENGTH=20 name="ITEM" value="<%=ddb.getsESTITEM()%>" READONLY>
      </div>
  	  </div>
  	  
 	  
  	  <div class="form-group">
      <label class="control-label col-sm-4" for="Discription">Description:</label>
      <div class="col-sm-6">
      <INPUT  type = "TEXT" style="width: 100%" class="form-control" MAXLENGTH=100 name="DESC" value="<%=su.formatHTML(su.replaceCharacters2Recv(ddb.getsESTITEMDESC()))%>" READONLY>
      </div>
      </div>
      
      <div class="form-group">
      <label class="control-label col-sm-4" for="Picked Quantity">Issued Quantity:</label>
      <div class="col-sm-6">          
      <INPUT  type = "TEXT" style="width: 100%"  value="<%=su.formatNum(String.valueOf(qtyIssue))%>"  MAXLENGTH=20 name="PICKEDQTY"  class="form-control" READONLY>
      </div>
       </div>
       
      <div class="form-group">
      <label class="control-label col-sm-4" for="Quantity">
      <a href="#" data-placement="left">
   	  <i class="glyphicon glyphicon-star" style="font-size: 10px; top:-6px; color: #e50000"></i></a>&nbsp;Quantity:</label>
      <div class="col-sm-6"> 
         <div class="input-group">  
      <INPUT class="form-control"  type = "TEXT" style="width: 100%"  MAXLENGTH=20 name="QTY" id="QTY" onchange="validToThreeDecimal(this.value)" value="<%=su.formatNum(String.valueOf(ddb.getfESTQTYOR()))%>" >
      <span class="input-group-btn"></span>    
      <INPUT  type = "TEXT" style="width: 100%" class="form-control" MAXLENGTH=20 name="UOM" value="<%=ddb.getsESTUNITMO()%>" READONLY>
      </div>
      </div>
       </div>
       
      <div class="form-group">
      <label class="control-label col-sm-4" for="Unit Price">
      <a href="#" data-placement="left">
   	  <i class="glyphicon glyphicon-star" style="font-size: 10px; top:-6px; color: #e50000"></i></a>&nbsp;Unit Price:</label>
      <div class="col-sm-6">          
      <INPUT class="form-control" type = "TEXT" style="width: 100%"  MAXLENGTH=20 name="UNITPRICE" id="UNITPRICE" value="<%=new java.math.BigDecimal(unitPrice).toPlainString()%>" onchange="validDecimal(this.value)">	
      </div>
       </div>
       
      <div class="form-group">
      <label class="control-label col-sm-4" for="Delivery Date">Delivery Date:</label>
      <div class="col-sm-6">          
      <INPUT  type = "TEXT"  name="PRODUCTDELIVERYDATE" style="width: 100%" value="<%=PRODUCTDELIVERYDATE%>"  MAXLENGTH=20   class="form-control datepicker" READONLY>
      </div>
      </div>
              
      <div class="form-group">
	    
	 <div class="col-sm-10">
      <table id="MULTI_IB_REMARKS">
      
       <% 
       ArrayList al= _ESTUtil.listEstimateMultiRemarks(plant,estno,estlnno);
       String lblRemarks="";
       String PRDREMARKS="";
      if(al.size()> 0)
        {
          for(int i=0 ; i<al.size();i++)
           {
         	  Map m=(Map)al.get(i);
         	  lblRemarks="Remarks";
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
      <INPUT type="Hidden" name="CUSTNO" value="<%=dhb.getCustNo()%>">
      <INPUT type="Hidden" name="UNITPRICERD" id="UNITPRICERD" value="<%=new java.math.BigDecimal(unitPricerd).toPlainString()%>">
      <div class="form-group">        
      <div class="col-sm-12" align="center">
      <button type="button" class="Submit btn btn-default" onClick="window.location.href='/track/EstimateServlet?ESTNO=<%=estno%>&RFLAG=<%=RFLAG%>&Submit=View';"><b>Back</b></button>&nbsp;&nbsp;
      
                  <%if(status.equalsIgnoreCase("N")|| nonStkFlg.equalsIgnoreCase("Y")){%>
      <button type="button" class="Submit btn btn-default" id="Delete" name="Submit" onclick="return onDelete(document.form)"><b>Delete</b></button>&nbsp;&nbsp;
      <%} %>
      <button type="button" class="Submit btn btn-default" onClick="return onUpdate()"><b>Update</b></button>&nbsp;&nbsp;
      
      </div>
      
      </div>
     
      </form>
      </div>
      </div>
      </div>
      
      <script>
	$(document).ready(function(){
    $('[data-toggle="tooltip"]').tooltip();   
	});
	</script>					



<jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
	<jsp:param name="nobackblock" value="1" />
</jsp:include>
