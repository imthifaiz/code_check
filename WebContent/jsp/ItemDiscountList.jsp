<%@ page import="java.util.*" session="true"%>
<%@ page import="java.text.DecimalFormat"%>
<%@page import="com.track.tables.ITEMMST"%>
<%@page import="com.track.dao.ItemSesBeanDAO"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.util.*"%>
<%@ page import="com.track.gates.*"%>
<%@ page import="com.track.constants.*"%>
<script language="JavaScript" type="text/javascript" src="js/general.js">

</script>
<html>
 
<head>
<title>Discount</title>
<link rel="stylesheet" href="css/style.css">
</head>
<body bgcolor="#ffffff" onload="enabletb();">
<%
	
    ITEMMST items = new ITEMMST();
	HashMap<String, String> loggerDetailsHasMap = new HashMap<String, String>();
	loggerDetailsHasMap.put(MLogger.COMPANY_CODE, (String) session.getAttribute("PLANT"));
	loggerDetailsHasMap.put(MLogger.USER_CODE, StrUtils.fString(
	(String) session.getAttribute("LOGIN_USER")).trim());
	MLogger mLogger = new MLogger();
	mLogger.setLoggerConstans(loggerDetailsHasMap);
	Hashtable ht=new Hashtable();
	
	
	StrUtils strUtils = new StrUtils();
	
	String AMOUNTDISCOUNT="",PERCENTAGEDISCOUNT="",discountAmount="",discountPercentage="", result = "",sBGColor="";
	float fTotal=0,fCalcTotal=0,fSubtotal=0,fTax=0,fCalcTax=0,fDiscount=0,fCalcDiscount=0,fPercentage=0,fPricewithtax=0;
	int iCnt = 0;String appenddisc="";
	
	String plant=(String)session.getAttribute("PLANT");
	String action = StrUtils.fString(request.getParameter("action")).trim();
	String disabledivdiscount = StrUtils.fString(request.getParameter("disabledivdiscount")).trim();
	String checkvalues = request.getParameter("checkvalues");
	String ITEM=request.getParameter("ITEM");
	String BATCH=request.getParameter("BATCH");
	String MINSPRICE=request.getParameter("MINSPRICE");
	String STOCKQTY=request.getParameter("STOCKQTY");
	String TYPE=request.getParameter("TYPE");
	
	String CURRENTAMOUNT=request.getParameter("CURRENTAMOUNT");
	AMOUNTDISCOUNT= strUtils.fString(request.getParameter("AMOUNTDISCOUNT"));
	PERCENTAGEDISCOUNT= strUtils.fString(request.getParameter("PERCENTAGEDISCOUNT"));
	
	session = request.getSession();
	 
	DecimalFormat decformat = new DecimalFormat("#,##0.00");
    DecimalFormat fltformat = new DecimalFormat("#,###");
   
    Vector buylist=null;
    if(TYPE.equalsIgnoreCase("POSTRAN")){
    buylist = (Vector)session.getValue("poslist");}
    else{
    	buylist = (Vector)session.getValue("posreflist");
    }
  
   
	if(action.equalsIgnoreCase("View"))
	{  
        
	 session.setAttribute("RESULTTOTALDISCOUNT","");
         session.setAttribute("TOTALDISCOUNT", "");
         session.setAttribute("TOTALSUBTOTAL", ""); 
         session.setAttribute("TOTALTAX", ""); 
         session.setAttribute("CALCDISCOUNT","");
         
		 for (int i = 0; i < buylist.size(); i++) {
		 
		 ITEMMST itembean  = (ITEMMST)buylist.elementAt(i);
		      
         if (itembean.getITEM().equalsIgnoreCase(request.getParameter("ITEM")) && itembean.getBATCH().equalsIgnoreCase(request.getParameter("BATCH"))) {
        		float discount =itembean.getDISCOUNT();
 				//float totalprice= itembean.getTotalPrice();
 				float totalprice= itembean.getUNITPRICE();
 				fTax =itembean.getGSTTAX();
 				fTax=fTax/100;
 				
 				
 				float totalpricewithtax=itembean.getPRICEWITHTAX();
 				
                 if(request.getParameter("AMOUNTDISCOUNT")==null||request.getParameter("AMOUNTDISCOUNT")=="")
    			 {
    				 discountAmount="";
    			 }
    			 else
    			 {
    				 discountAmount=request.getParameter("AMOUNTDISCOUNT");
    				 fDiscount=Float.parseFloat(AMOUNTDISCOUNT);
    				//fDiscount=Float.parseFloat(discountAmount);
    				 fCalcDiscount=totalprice-fDiscount;
    				 
    				 //Set Discount Percentage
    				 fCalcDiscount=(fCalcDiscount/totalprice)*100 ; 
    				 //fCalcDiscount=(fCalcDiscount)/totalprice)*100 ;
    				 itembean.setDISCOUNT(fCalcDiscount);
    				 
    				 //Set Subtotal
    				 System.out.println(STOCKQTY);
    				 float stockqty=Float.parseFloat(STOCKQTY);
    				 fSubtotal= stockqty*fDiscount;
    				 
    				 //fSubtotal= fDiscount;
    				 itembean.setTotalPrice(fSubtotal);
    				 //Set Tax
    				 fCalcTax=fSubtotal*fTax;
    				 //Set Total Amount
    				 fCalcTotal=fSubtotal+fCalcTax;
    				 
    				 
    				 itembean.setPRICEWITHTAX(fCalcTotal);
    				 buylist.setElementAt(itembean, i);
    				// System.out.println("Elemetn 1..."+itembean.getDISCOUNT());
    		   		
    			 }
    			 
    			 if(request.getParameter("PERCENTAGEDISCOUNT")==null||request.getParameter("PERCENTAGEDISCOUNT")=="")
    			 {
    				
    				 discountPercentage="";
    				 
    			 }
    			 else
    			 {
    				 //Set Discount
         			 discountPercentage=request.getParameter("PERCENTAGEDISCOUNT");
    				 fDiscount=Float.parseFloat(discountPercentage);
    				 //Set discount
    				 itembean.setDISCOUNT(fDiscount);
    				 fCalcDiscount=(totalprice*fDiscount/100);
                                 //Set Subtotal
                  	 float stockqty=Float.parseFloat(STOCKQTY);
    				 fSubtotal=(totalprice-fCalcDiscount)*stockqty;
    				 itembean.setTotalPrice(fSubtotal);
    				 //Set Tax
    				 fCalcTax=fSubtotal*fTax;
    				 //Set Total Amount
    				 fCalcTotal=fSubtotal+fCalcTax;
    				  itembean.setPRICEWITHTAX(fCalcTotal);
    				 buylist.setElementAt(itembean, i);
    	 
    			 }
		    }
         }
	}
	  	
		
%>

<FORM name="form" method="Post"  >
<div id="divdiscount">
  <table border="0" width="100%" cellspacing="1" cellpadding="0" align="center" bgcolor="#dddddd">
     <TR>
      <TH BGCOLOR="#000066" COLSPAN="11"><font color="white">Discount</font></TH>
    </TR>
    <TR></TR>
    <TR></TR>
    <TR></TR>
    <TR></TR>
    <TR></TR>
    <TR>
         <TH ALIGN="Right" width="45%">Current Price : &nbsp;&nbsp; </TH>
         <TD></TD>
		 <TD><INPUT name=CURRENTAMOUNT type = "TEXT" value="<%= CURRENTAMOUNT%>"   size="15"  MAXLENGTH=20 readonly ><TD>
	</TR>
    <TR>
     
       <TH ALIGN="Right" width="45%">Discounted Price : &nbsp;&nbsp; </TH>
		<TD><INPUT name="rdoDiscount" type = "radio"    value ="Y" <%if(PERCENTAGEDISCOUNT.length()==0) {%>checked <%}%> onClick="enabletb();"></TD>
		<TD><INPUT name="AMOUNTDISCOUNT" id="AMOUNTDISCOUNT" type = "TEXT" value="<%=AMOUNTDISCOUNT%>"   size="15"  MAXLENGTH=20 onchange="validDecimal(this.value)"><TD>
	</TR>
	 <TR>
	   <TH ALIGN="Right" width="45%">Discount By Percentage : &nbsp;&nbsp; </TH>
		<TD><INPUT name="rdoDiscount" type = "radio"    value ="N"  <%if(PERCENTAGEDISCOUNT.length()>0) {%>checked <%}%>  onClick="enabletb();" ></TD>
		<TD><INPUT name="PERCENTAGEDISCOUNT" id="PERCENTAGEDISCOUNT" type = "TEXT"  value="<%=PERCENTAGEDISCOUNT%>"  size="15"  MAXLENGTH=20 ><TD>
		 <INPUT type="hidden" name="ITEM" value="<%=ITEM%>"/>
		 <INPUT type="hidden" name="BATCH" value="<%=BATCH%>"/>
		 <INPUT type="hidden" name="MINSPRICE" value="<%=MINSPRICE%>"/>	
		 <INPUT type="hidden" name="STOCKQTY" value="<%=STOCKQTY%>"/>
		 <INPUT type="hidden" name="TYPE" value="<%=TYPE%>"/>
		<INPUT type="hidden" name="disabledivdiscount" value="<%=disabledivdiscount%>"/>
		 
	</TR>
	  <TR></TR>
	  <TR></TR>
	  <TR></TR>
	  <TR></TR>
	  <TR></TR>
	  <TR></TR>
	  <TR></TR>
	  <TR></TR>
	
   <TR>
        <TH COLSPAN="8">&nbsp;</TH>
   </TR>
    <TR>
      
        
      <TH COLSPAN="8" align="center"> 
            
             <input type="button" value="Apply" name="SubmitBtn" onclick="refreshParent();">
	  </TH>
    </TR>
  </table>
 </div>
 <div id="divclose" style="display: none;">
 <table border="0" width="50%" cellspacing="1" cellpadding="0" align="center" bgcolor="#dddddd">
 <TR>
      <TH></TH>
    </TR>
    <TR></TR>
    <TR></TR>
    <TR></TR>
    <TR></TR>
    <TR></TR>
    <TR></TR>
     <TR>
     	<TH></TH>
      	<td align="center"><input type="button" value="Close" name="close" onclick="Close();"></td>
    </TR>
 </table>	
 </div>
</form>

 <script language="javascript">
 disablediv();
 	 
 		function refreshParent() {
 			
 		    var  currentamount=parseFloat(removeCommas(document.form.CURRENTAMOUNT.value));
 		 	var  amountdiscount =  parseFloat(removeCommas(document.form.AMOUNTDISCOUNT.value));
 			var  amountpercentage =  parseFloat(document.form.PERCENTAGEDISCOUNT.value);
 			var minsprice = parseFloat(document.form.MINSPRICE.value);
 			var price;
 			
 			
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
 	 	 		//price = parseFloat(currentamount);

 	 	 		if(amountdiscount < minsprice)
 	 	 		{
 	 	 	 		alert("Calualted Price after discount is less than Minimum selling price.");
 	 	 	 		document.form.AMOUNTDISCOUNT.focus();
 	 	 	 		return false;
 	 	 		}
 	 			 				
 	 		}
 	 		//if(document.form.rdoDiscount.value=="N")
 	 		if(document.form.rdoDiscount[1].checked == true)
 	 		{
 	 	 		price = parseFloat(currentamount-(currentamount*amountpercentage)/100);

 	 	 		if(price < minsprice)
 	 	 		{
 	 	 	 		alert("Price is Less than minimum selling price after calculating discount.Give valid Discount.");
 	 	 	 	document.form.PERCENTAGEDISCOUNT.focus();
 	 	 	 		return false;
 	 	 		}

 	 		}
 	 		var dispriceamt = document.form.AMOUNTDISCOUNT.value;
 	 	     if (dispriceamt.indexOf('.') == -1) dispriceamt += ".";
 	 		 var decNum = dispriceamt.substring(dispriceamt.indexOf('.')+1, dispriceamt.length);
 	 		 if (decNum.length > 2)
 	 		 {
 	 			alert("Invalid more than 2 digits after decimal in discounted price");
 	 			document.form.AMOUNTDISCOUNT.focus();
 	 			return false;
 	 			
 	 		 }
 	 		
            document.form.action  = "ItemDiscountList.jsp?action=View&disabledivdiscount=true";
 			document.form.submit();
 			
 			document.form.disabledivdiscount.value="true";
 			document.getElementById("divdiscount").style.display = "none";
 			document.getElementById("divclose").style.display = "inline";
                        
                	
 		}
  
 function disablediv()
 {
 		if(document.form.disabledivdiscount.value=="true")
		  {
		  	document.getElementById("divdiscount").style.display = "none";
		  	document.getElementById("divclose").style.display = "inline";
		  }
 }
 		
function Close()
{
   //finish doing things after the pause
   document.form.disabledivdiscount.value="false";
   window.opener.parent.location.reload();
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
	
	function validDecimal(str){
		if (str.indexOf('.') == -1) str += ".";
		var decNum = str.substring(str.indexOf('.')+1, str.length);
		if (decNum.length > 2)
		{
			alert("Invalid more than 2 digits after decimal in discounted price");
			document.form.AMOUNTDISCOUNT.focus();
			return false;
			
		}
	}
		
</script>
</body>
</html>

