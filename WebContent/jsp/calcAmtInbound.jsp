<jsp:useBean id="sb" class="com.track.gates.selectBean" />
<%@ page import="java.util.*" session="true"%>
<%@ page import="java.text.DecimalFormat"%>
<%@page import="com.track.tables.ITEMMST"%>
<%@page import="com.track.dao.ItemSesBeanDAO"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.util.*"%>
<%@ page import="com.track.gates.*"%>
<%@ page import="com.track.constants.*"%>


<html>
<head>
<title>Calculate Discount</title>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
<link rel="stylesheet" href="css/style.css">
<script type="text/javascript" src="js/jquery.min.js"></script>
<script type="text/javascript" src="js/bootstrap.min.js"></script>
<script type="text/javascript" src="js/general.js"></script>
<script src="dist/js/lib/numberutil.js"></script> 
</head>
<body bgcolor="#ffffff" onload="enabletb();">
<%
	String AMOUNTDISCOUNT="",PERCENTAGEDISCOUNT="",discountAmount="",discountPercentage="", result = "",sBGColor="";
	float fTotal=0,fCalcTotal=0,fSubtotal=0,fTax=0,fCalcTax=0,fDiscount=0,fCalcDiscount=0,fPercentage=0,fPricewithtax=0;
	int iCnt = 0;String appenddisc="";
	
	String plant=(String)session.getAttribute("PLANT");
	String action = StrUtils.fString(request.getParameter("action")).trim();	
	String CURRENTAMOUNT=StrUtils.fString(request.getParameter("CURRENTAMOUNT"));
	String MINSPRICE=StrUtils.fString(request.getParameter("MINSPRICE"));
	if (CURRENTAMOUNT.length()==0)CURRENTAMOUNT="0";
	if (MINSPRICE.length()==0)MINSPRICE="0";
	String GST = sb.getGST("GST",plant);
	String Tax =  StrUtils.fString(request.getParameter("Tax"));

   
  %>
  
<div class="panel panel-default">
<div class="panel-heading" style="color: #ffffff; background-color: #102C54 " align="center">
<h3 class="panel-title">Calculate Discount</h3> 
</div>
</div>


<div class="container">
<FORM class="form-horizontal" name="form" method="Post">


<div class="form-group">
      <label class="control-label col-sm-5" for="Current Price">Current Price:</label>
       <div class="col-sm-3">  
		 <INPUT class="form-control" name=CURRENTAMOUNT type = "TEXT" value="<%= CURRENTAMOUNT%>"   size="10"  MAXLENGTH=20 readonly >
		 </div>
		 </div>
		 
		 
<div class="form-group">
       <label class="control-label col-sm-5" for="Discount By Cost">Discount By Cost:</label>
       <label class="radio-inline" >
       <INPUT name="rdoDiscount" type = "radio"    value ="Y" <%if(PERCENTAGEDISCOUNT.length()==0) {%>checked <%}%> onClick="enabletb();">
	   <INPUT class="form-control" name="AMOUNTDISCOUNT" id="AMOUNTDISCOUNT" type = "TEXT" value="<%=AMOUNTDISCOUNT%>"   size="14"  MAXLENGTH=20 >
	   </label>
	   </div>
	   
	   
	   
<div class="form-group">
      <label class="control-label col-sm-5" for="Discount By Percentage">Discount By Percentage:</label>
	  <label class="radio-inline">
      <INPUT name="rdoDiscount" type = "radio"    value ="N"  <%if(PERCENTAGEDISCOUNT.length()>0) {%>checked <%}%>  onClick="enabletb();" >
	  <INPUT class="form-control" name="PERCENTAGEDISCOUNT" id="PERCENTAGEDISCOUNT" type = "TEXT"  value="<%=PERCENTAGEDISCOUNT%>"  size="14"  MAXLENGTH="5" >
	  </label>
	  </div>
	  
		
		<INPUT type="hidden" name="MINSPRICE" value="<%=MINSPRICE%>"/>	
		<INPUT type="hidden" name="GST" value="<%=GST%>"/>	
		
<div class="form-group" style="display: none;">
      <label class="control-label col-sm-5" for="Tax">Tax:</label>
	  <label class="radio-inline">
	  <INPUT name="Tax" type = "radio" value="Y"  id="incl" checked="checked" >Exclusive</label>
	  <label class="radio-inline">
	  <INPUT name="Tax" type = "radio"  value="N"  id="incl"  >Inclusive  </label>
	  </div>
      
         <br>
      
<div class="form-group">
         <div class="text-center">
      	<button type="button" class="Submit btn btn-default" onClick="Cancel();"><b>Close</b></button>&nbsp;&nbsp;
      	<button type="button" class="Submit btn btn-default" onClick="refreshParent();"><b>Ok</b></button>&nbsp;&nbsp;
      	</div>
      </div>
    
</form>
</div>
 <script type="text/javascript">
  
 	 
 		function refreshParent() {
 			
 		    var  currentamount=parseFloat(removeCommas(document.form.CURRENTAMOUNT.value));
 		 	var  amountdiscount =  parseFloat(removeCommas(document.form.AMOUNTDISCOUNT.value));
 			var  amountpercentage =  parseFloat(document.form.PERCENTAGEDISCOUNT.value);
 			var minsprice = parseFloat(document.form.MINSPRICE.value);
 			var fTax = parseFloat(document.form.GST.value);
 			fTax=fTax/100;
 			var discount,price;
 		
 			if(document.form.AMOUNTDISCOUNT.value.length > 0)
 			{
 				if(amountdiscount > currentamount)
 				{
 					alert("Entered Discount By Amount cannot greater than Price");
 					return false;
 				}
 			
 				if(isNaN(document.form.AMOUNTDISCOUNT.value))
 				{
 					 alert("Please enter valid Discount By Amount.");
 					 return false;
 				}
 			}
 			if(document.form.PERCENTAGEDISCOUNT.value.length > 0)
 	 		{
 				
 	 				if(isNaN(document.form.PERCENTAGEDISCOUNT.value)) 
 	 				{
 	 					alert("Please enter valid Discount By Percentage.");
 	 					return false;
 	 				}
 	 		}
 	 		if(document.form.rdoDiscount[0].checked == true)
 	 		{	
 	 			if(document.form.AMOUNTDISCOUNT.value.length == 0)
 	 	 		{
 	 				amountdiscount=currentamount;
 	 	 		}
 	 	 		//price = parseFloat(currentamount);

 	 	 		/*if(amountdiscount < minsprice)
 	 	 		{
 	 	 	 		alert("Calualted Price after discount is less than Minimum selling price.");
 	 	 	 		document.form.AMOUNTDISCOUNT.focus();
 	 	 	 		return false;
 	 	 		}*/
 	 	 		if(document.form.Tax[1].checked == true)// If price is incluisve of GST/Tax
 	 	 		{
 	 	 			amountdiscount =  amountdiscount/(1+fTax) ;
 	 			}
 	 	 		
 	 	 		var calAmount = (amountdiscount).toFixedSpecial(<%=com.track.gates.DbBean.NOOFDECIMALPTSFORCURRENCY%>);
 	 	 		window.opener.form.UNITCOST.value =calAmount;
				window.opener.form.UNITCOSTRD.value =calAmount;
 	 			 				
 	 		}
 	 		
 	 		if(document.form.rdoDiscount[1].checked == true)
 	 		{
 	 			
 	 			if(document.form.Tax[1].checked == true)// If price is incluisve of GST/Tax
 	 	 		{
 	 				currentamount =  currentamount/(1+fTax) ;
 	 			}
 	 			if(document.form.PERCENTAGEDISCOUNT.value.length > 0)
 	 	 		{
 	 				discount = parseFloat((currentamount*amountpercentage)/100);
 	 	 	 		price = parseFloat(currentamount-((currentamount*amountpercentage)/100));
 	 	 		}else{
 	 	 			price=currentamount;
 	 	 		}
 	 		

 	 	 	/*	if(price < minsprice)
 	 	 		{
 	 	 	 		alert("Price is Less than minimum selling price after calculating discount.Give valid Discount.");
 	 	 	 	document.form.PERCENTAGEDISCOUNT.focus();
 	 	 	 		return false;
 	 	 		}*/
 	 	 		var calAmount = parseFloat(price).toFixedSpecial(<%=com.track.gates.DbBean.NOOFDECIMALPTSFORCURRENCY%>);
 	 	 		window.opener.form.UNITCOST.value =calAmount;
				window.opener.form.UNITCOSTRD.value =calAmount;

 	 		}	
 	 	
 	 		 window.close();   	
 		}
 		
 	
	function Cancel()
	{
		window.close();
	}
	function enabletb()
	  {
		  var amount=document.getElementById("AMOUNTDISCOUNT");	  
		  var percentage=document.getElementById("PERCENTAGEDISCOUNT");	 
		  
		  if( document.form.rdoDiscount[0].checked == true )
		  {
			  document.form.AMOUNTDISCOUNT.disabled=false;
		      amount.style.backgroundColor="#ffffff";
		      
		      document.form.PERCENTAGEDISCOUNT.disabled=true;
		      document.form.PERCENTAGEDISCOUNT.value="";
		      percentage.style.backgroundColor="#C0C0C0";

		  }
		  else if( document.form.rdoDiscount[1].checked == true )
		  {
			  document.form.AMOUNTDISCOUNT.disabled=true;
			  document.form.AMOUNTDISCOUNT.value="";
			  amount.style.backgroundColor="#C0C0C0";
			  
			  document.form.PERCENTAGEDISCOUNT.disabled=false;
			  percentage.style.backgroundColor="#ffffff";
		
			  
		  }
	
	  }
		
</script>
</body>
</html>

