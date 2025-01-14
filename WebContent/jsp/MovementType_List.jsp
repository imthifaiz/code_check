<%@ page import="java.util.*"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.db.util.POUtil"%>
<%@ page import="com.track.dao.RecvDetDAO"%>
<%@ page import="com.track.util.*"%>
<%@page import="com.track.dao.LocMstDAO"%>
<%@page import="com.track.constants.TransactionConstants"%>

<html>
<head>
<title>Activity Type List</title>
<link rel="stylesheet" href="css/style.css">


</head>
<body bgcolor="#ffffff">
<form method="post" name="form">
  <table border="0" width="90%" cellspacing="1" cellpadding="0" align="center">
    <TR>
     <td colspan = "8" align = "center" ><b>Select the Activity Logs Type</b></td>
    </TR>
    <TR>
        <TH COLSPAN="8">&nbsp;</TH>
   </TR>
     
<%
HashMap<String, String> loggerDetailsHasMap = new HashMap<String, String>();
loggerDetailsHasMap.put(MLogger.COMPANY_CODE, (String) session
		.getAttribute("PLANT"));
loggerDetailsHasMap.put(MLogger.USER_CODE, StrUtils.fString(
		(String) session.getAttribute("LOGIN_USER")).trim());
MLogger mLogger = new MLogger();
mLogger.setLoggerConstans(loggerDetailsHasMap);
Map m = TransactionConstants.getTransactionList();
Set s = m.entrySet();
String mkey="",mvalue="",formName="";

try{
    StrUtils strUtils = new StrUtils();
    String PLANT= session.getAttribute("PLANT").toString();
     String USERID= session.getAttribute("LOGIN_USER").toString();
     formName =   StrUtils.fString(request.getParameter("formName"));
     String movementgroup =   StrUtils.fString(request.getParameter("MOVEMENTGROUP"));
 

    if(movementgroup.equalsIgnoreCase("Purchase Estimate Order")){
   	    
   		String[] inbounddirtype = TransactionConstants.purchaseestimatedirtype;
   		for(int j=0;j<inbounddirtype.length;j++){
   			String cnsval= inbounddirtype[j];
   			
   	     	if((j) % 3 ==0)	{
   	%>
   	    <TR>
   	    <%} %>
   	      <td>
   	      	<input type = "checkbox" name = "movementtype" value = '<%=cnsval%>'/><%=cnsval%>      	
   	      </td>  <%
   	      if((j+1) % 3 ==0)	{
   	%>
   	    </TR>
   	    <%} 
   	 	
   		}
   	} else if(movementgroup.equalsIgnoreCase("Purchase Order")){
    
	String[] inbounddirtype = TransactionConstants.inbounddirtype;
	for(int j=0;j<inbounddirtype.length;j++){
		String cnsval= inbounddirtype[j];
		
     	if((j) % 3 ==0)	{
%>
    <TR>
    <%} %>
      <td>
      	<input type = "checkbox" name = "movementtype" value = '<%=cnsval%>'/><%=cnsval%>      	
      </td>  <%
      if((j+1) % 3 ==0)	{
%>
    </TR>
    <%} 
 	
	}
} else if(movementgroup.equalsIgnoreCase("Expenses")){
    
	String[] expensesdirtype = TransactionConstants.expenses;
	for(int j=0;j<expensesdirtype.length;j++){
		String cnsval= expensesdirtype[j];
		
     	if((j) % 3 ==0)	{
%>
    <TR>
    <%} %>
      <td>
      	<input type = "checkbox" name = "movementtype" value = '<%=cnsval%>'/><%=cnsval%>      	
      </td>  <%
      if((j+1) % 3 ==0)	{
%>
    </TR>
    <%} 
 	
	}
	
} else if(movementgroup.equalsIgnoreCase("Bill")){
    
	String[] billdirtype = TransactionConstants.bill;
	for(int j=0;j<billdirtype.length;j++){
		String cnsval= billdirtype[j];
		
     	if((j) % 3 ==0)	{
%>
    <TR>
    <%} %>
      <td>
      	<input type = "checkbox" name = "movementtype" value = '<%=cnsval%>'/><%=cnsval%>      	
      </td>  <%
      if((j+1) % 3 ==0)	{
%>
    </TR>
    <%} 
 	
	}
} else if(movementgroup.equalsIgnoreCase("Payment")){
    
	String[] paymentdirtype = TransactionConstants.payment;
	for(int j=0;j<paymentdirtype.length;j++){
		String cnsval= paymentdirtype[j];
		
     	if((j) % 3 ==0)	{
%>
    <TR>
    <%} %>
      <td>
      	<input type = "checkbox" name = "movementtype" value = '<%=cnsval%>'/><%=cnsval%>      	
      </td>  <%
      if((j+1) % 3 ==0)	{
%>
    </TR>
    <%} 
 	
	}
} else if(movementgroup.equalsIgnoreCase("Debit Note")){
    
	String[] purchasecreditdirtype = TransactionConstants.purchase_creditnote;
	for(int j=0;j<purchasecreditdirtype.length;j++){
		String cnsval= purchasecreditdirtype[j];
		
     	if((j) % 3 ==0)	{
%>
    <TR>
    <%} %>
      <td>
      	<input type = "checkbox" name = "movementtype" value = '<%=cnsval%>'/><%=cnsval%>      	
      </td>  <%
      if((j+1) % 3 ==0)	{
%>
    </TR>
    <%} 
 	
	}
} else if(movementgroup.equalsIgnoreCase("Sales Estimate Order")){
	String[] estimatedirtype = TransactionConstants.estimatedirtype;
	for(int j=0;j<estimatedirtype.length;j++){
		String cnsval= estimatedirtype[j];
		
     	if((j) % 3 ==0)	{
%>
    <TR>
    <%} %>
      <td>
      	<input type = "checkbox" name = "movementtype" value = '<%=cnsval%>'/><%=cnsval%>      	
      </td>  <%
      if((j+1) % 3 ==0)	{
%>
    
    <%} 
 	
	}
} else if(movementgroup.equalsIgnoreCase("Sales Order")){
	String[] outbounddirtype = TransactionConstants.outbounddirtype;
	for(int j=0;j<outbounddirtype.length;j++){
		String cnsval= outbounddirtype[j];
		
     	if((j) % 3 ==0)	{
%>
    <TR>
    <%} %>
      <td>
      	<input type = "checkbox" name = "movementtype" value = '<%=cnsval%>'/><%=cnsval%>      	
      </td>  <%
      if((j+1) % 3 ==0)	{
%>
    </TR>
    <%} 
 	
	}

} else if(movementgroup.equalsIgnoreCase("Invoice")){
    
	String[] invoicedirtype = TransactionConstants.invoice;
	for(int j=0;j<invoicedirtype.length;j++){
		String cnsval= invoicedirtype[j];
		
     	if((j) % 3 ==0)	{
%>
    <TR>
    <%} %>
      <td>
      	<input type = "checkbox" name = "movementtype" value = '<%=cnsval%>'/><%=cnsval%>      	
      </td>  <%
      if((j+1) % 3 ==0)	{
%>
    </TR>
    <%} 
 	
	}
} else if(movementgroup.equalsIgnoreCase("Payment Received")){
    
	String[] paymentdirtype = TransactionConstants.payment_received;
	for(int j=0;j<paymentdirtype.length;j++){
		String cnsval= paymentdirtype[j];
		
     	if((j) % 3 ==0)	{
%>
    <TR>
    <%} %>
      <td>
      	<input type = "checkbox" name = "movementtype" value = '<%=cnsval%>'/><%=cnsval%>      	
      </td>  <%
      if((j+1) % 3 ==0)	{
%>
    </TR>
    <%} 
 	
	}
} else if(movementgroup.equalsIgnoreCase("Credit Note")){
    
	String[] creditdirtype = TransactionConstants.sales_creditnote;
	for(int j=0;j<creditdirtype.length;j++){
		String cnsval= creditdirtype[j];
		
     	if((j) % 3 ==0)	{
%>
    <TR>
    <%} %>
      <td>
      	<input type = "checkbox" name = "movementtype" value = '<%=cnsval%>'/><%=cnsval%>      	
      </td>  <%
      if((j+1) % 3 ==0)	{
%>
    </TR>
    <%} 
 	
	}
} else if(movementgroup.equalsIgnoreCase("Consignment Order")){
	String[] transferdirtype = TransactionConstants.transferdirtype;
	for(int j=0;j<transferdirtype.length;j++){
		String cnsval= transferdirtype[j];
		
     	if((j) % 3 ==0)	{
%>
    <TR>
    <%} %>
      <td>
      	<input type = "checkbox" name = "movementtype" value = '<%=cnsval%>'/><%=cnsval%>      	
      </td>  <%
      if((j+1) % 3 ==0)	{
%>
    </TR>
    <%} 
 	
	}
} else if(movementgroup.equalsIgnoreCase("Rental And Service Order")){
	String[] loandirtype = TransactionConstants.loandirtype;
	for(int j=0;j<loandirtype.length;j++){
		String cnsval= loandirtype[j];
		
     	if((j) % 3 ==0)	{
%>
    <TR>
    <%} %>
      <td>
      	<input type = "checkbox" name = "movementtype" value = '<%=cnsval%>'/><%=cnsval%>      	
      </td>  <%
      if((j+1) % 3 ==0)	{
%>
    </TR>
    <%} 
 	
	}

	
}else if(movementgroup.equalsIgnoreCase("Goods_Receipt_Issue")){
		String[] miscdirtype = TransactionConstants.goods_receipt_Issue;
		for(int j=0;j<miscdirtype.length;j++){
			String cnsval= miscdirtype[j];
			
	     	if((j) % 3 ==0)	{
	%>
	    <TR>
	    <%} %>
	      <td>
	      	<input type = "checkbox" name = "movementtype" value = '<%=cnsval%>'/><%=cnsval%>      	
	      </td>  <%
	      if((j+1) % 3 ==0)	{
	%>
	    </TR>
	    <%} 
	 	
		}
}else if(movementgroup.equalsIgnoreCase("Stock Move")){
	String[] loctransferdirtype = TransactionConstants.stockmove;
	for(int j=0;j<loctransferdirtype.length;j++){
		String cnsval= loctransferdirtype[j];
		
     	if((j) % 3 ==0)	{
%>
    <TR>
    <%} %>
      <td>
      	<input type = "checkbox" name = "movementtype" value = '<%=cnsval%>'/><%=cnsval%>      	
      </td>  <%
      if((j+1) % 3 ==0)	{
%>
    	    </TR>
	    <%} 
	 	
		}
}else if(movementgroup.equalsIgnoreCase("De-Kitting")){
	String[] loctransferdirtype = TransactionConstants.ProcessingReceive;
	for(int j=0;j<loctransferdirtype.length;j++){
		String cnsval= loctransferdirtype[j];
		
     	if((j) % 3 ==0)	{
%>
    <TR>
    <%} %>
      <td>
      	<input type = "checkbox" name = "movementtype" value = '<%=cnsval%>'/><%=cnsval%>      	
      </td>  <%
      if((j+1) % 3 ==0)	{
    	  %>
  	    </TR>
	    <%} 
	 	
		}
}else if(movementgroup.equalsIgnoreCase("Kitting")){
	String[] loctransferdirtype = TransactionConstants.SemiProcessedProduct;
	for(int j=0;j<loctransferdirtype.length;j++){
		String cnsval= loctransferdirtype[j];
		
   	if((j) % 3 ==0)	{
%>
  <TR>
  <%} %>
    <td>
    	<input type = "checkbox" name = "movementtype" value = '<%=cnsval%>'/><%=cnsval%>      	
    </td>  <%
    if((j+1) % 3 ==0)	{
%>
    </TR>
    <%} 
 	
	}
}else if(movementgroup.equalsIgnoreCase("Stock Take")){
	String[] stocktakedirtype = TransactionConstants.stocktake;
	for(int j=0;j<stocktakedirtype.length;j++){
		String cnsval= stocktakedirtype[j];
		
     	if((j) % 3 ==0)	{
%>
    <TR>
    <%} %>
      <td>
      	<input type = "checkbox" name = "movementtype" value = '<%=cnsval%>'/><%=cnsval%>      	
      </td>  <%
      if((j+1) % 3 ==0)	{
%>
    </TR>
    <%} 
 	
	}
}else if(movementgroup.equalsIgnoreCase("Bulk Payslip")){
	String[] stocktakedirtype = TransactionConstants.bulkpayslip;
	for(int j=0;j<stocktakedirtype.length;j++){
		String cnsval= stocktakedirtype[j];
		
     	if((j) % 3 ==0)	{
%>
    <TR>
    <%} %>
      <td>
      	<input type = "checkbox" name = "movementtype" value = '<%=cnsval%>'/><%=cnsval%>      	
      </td>  <%
      if((j+1) % 3 ==0)	{
%>
    </TR>
    <%} 
 	
	}
}else if(movementgroup.equalsIgnoreCase("Kitting And DeKitting")){
	String[] paymentdirtype = TransactionConstants.kitting;
	for(int j=0;j<paymentdirtype.length;j++){
		String cnsval= paymentdirtype[j];
		
     	if((j) % 3 ==0)	{
%>
    <TR>
    <%} %>
      <td>
      	<input type = "checkbox" name = "movementtype" value = '<%=cnsval%>'/><%=cnsval%>      	
      </td>  <%
      if((j+1) % 3 ==0)	{
%>
    </TR>
    <%} 
 	
	}
}else if(movementgroup.equalsIgnoreCase("TAX_FILE")){
	String[] paymentdirtype = TransactionConstants.tax_file;
	for(int j=0;j<paymentdirtype.length;j++){
		String cnsval= paymentdirtype[j];
		
     	if((j) % 3 ==0)	{
%>
    <TR>
    <%} %>
      <td>
      	<input type = "checkbox" name = "movementtype" value = '<%=cnsval%>'/><%=cnsval%>      	
      </td>  <%
      if((j+1) % 3 ==0)	{
%>
    </TR>
    <%} 
 	
	}	
}else if(movementgroup.equalsIgnoreCase("Holiday")){
	String[] paymentdirtype = TransactionConstants.holiday;
	for(int j=0;j<paymentdirtype.length;j++){
		String cnsval= paymentdirtype[j];
		
     	if((j) % 3 ==0)	{
%>
    <TR>
    <%} %>
      <td>
      	<input type = "checkbox" name = "movementtype" value = '<%=cnsval%>'/><%=cnsval%>      	
      </td>  <%
      if((j+1) % 3 ==0)	{
%>
    </TR>
    <%} 
 	
	}
}else if(movementgroup.equalsIgnoreCase("Project")){
	String[] projecttype = TransactionConstants.project;
	for(int j=0;j<projecttype.length;j++){
		String cnsval= projecttype[j];
		
     	if((j) % 3 ==0)	{
%>
    <TR>
    <%} %>
      <td>
      	<input type = "checkbox" name = "movementtype" value = '<%=cnsval%>'/><%=cnsval%>      	
      </td>  <%
      if((j+1) % 3 ==0)	{
%>
    </TR>
    <%} 
 	
	}
}else if(movementgroup.equalsIgnoreCase("Leave Type")){
	String[] paymentdirtype = TransactionConstants.leavetype;
	for(int j=0;j<paymentdirtype.length;j++){
		String cnsval= paymentdirtype[j];
		
     	if((j) % 3 ==0)	{
%>
    <TR>
    <%} %>
      <td>
      	<input type = "checkbox" name = "movementtype" value = '<%=cnsval%>'/><%=cnsval%>      	
      </td>  <%
      if((j+1) % 3 ==0)	{
%>
    </TR>
    <%} 
 	
	}
}else if(movementgroup.equalsIgnoreCase("Salary Type")){
	String[] paymentdirtype = TransactionConstants.salarytype;
	for(int j=0;j<paymentdirtype.length;j++){
		String cnsval= paymentdirtype[j];
		
     	if((j) % 3 ==0)	{
%>
    <TR>
    <%} %>
      <td>
      	<input type = "checkbox" name = "movementtype" value = '<%=cnsval%>'/><%=cnsval%>      	
      </td>  <%
      if((j+1) % 3 ==0)	{
%>
    </TR>
    <%} 
 	
	}
}

else if(movementgroup.equalsIgnoreCase("Employee Type")){
	String[] paymentdirtype = TransactionConstants.employeetype;
	for(int j=0;j<paymentdirtype.length;j++){
		String cnsval= paymentdirtype[j];
		
     	if((j) % 3 ==0)	{
%>
    <TR>
    <%} %>
      <td>
      	<input type = "checkbox" name = "movementtype" value = '<%=cnsval%>'/><%=cnsval%>      	
      </td>  <%
      if((j+1) % 3 ==0)	{
%>
    </TR>
    <%} 
 	
	}
}

else if(movementgroup.equalsIgnoreCase("Apply Leave")){
	String[] paymentdirtype = TransactionConstants.applyleave;
	for(int j=0;j<paymentdirtype.length;j++){
		String cnsval= paymentdirtype[j];
		
     	if((j) % 3 ==0)	{
%>
    <TR>
    <%} %>
      <td>
      	<input type = "checkbox" name = "movementtype" value = '<%=cnsval%>'/><%=cnsval%>      	
      </td>  <%
      if((j+1) % 3 ==0)	{
%>
    </TR>
    <%} 
 	
	}
}

else if(movementgroup.equalsIgnoreCase("Payroll")){
	String[] paymentdirtype = TransactionConstants.payroll;
	for(int j=0;j<paymentdirtype.length;j++){
		String cnsval= paymentdirtype[j];
		
     	if((j) % 3 ==0)	{
%>
    <TR>
    <%} %>
      <td>
      	<input type = "checkbox" name = "movementtype" value = '<%=cnsval%>'/><%=cnsval%>      	
      </td>  <%
      if((j+1) % 3 ==0)	{
%>
    </TR>
    <%} 
 	
	}
}
else if(movementgroup.equalsIgnoreCase("Journal")){
	String[] paymentdirtype = TransactionConstants.journal;
	for(int j=0;j<paymentdirtype.length;j++){
		String cnsval= paymentdirtype[j];
		
     	if((j) % 3 ==0)	{
%>
    <TR>
    <%} %>
      <td>
      	<input type = "checkbox" name = "movementtype" value = '<%=cnsval%>'/><%=cnsval%>      	
      </td>  <%
      if((j+1) % 3 ==0)	{
%>
    </TR>
    <%} 
 	
	}
}
else{
	
	Iterator it = s.iterator();int i=0; String[] constarray=new String[s.size()]; 
	while(it.hasNext())
	{
		Map.Entry itermap = (Map.Entry)it.next();
		 mkey = (String)itermap.getKey();
		 mvalue = (String)itermap.getValue();
		constarray[i]=mvalue;
		i++;	
	}
	Arrays.sort(constarray);
	for(int j=0;j<constarray.length;j++){
		String cnsval= constarray[j];
		if((j) % 3 ==0)	{
			%>
			    <TR>
			    <%} %>
			      <td>
			      	<input type = "checkbox" name = "movementtype" value = '<%=cnsval%>'/><%=cnsval%>      	
			      </td>  <%
			      if((j+1) % 3 ==0)	{
			%>
			    </TR>
			    <%} 
	
	}
}

}catch(Exception he){he.printStackTrace(); System.out.println("Error in reterieving data");}
%>
   <TR>
        <TH COLSPAN="8">&nbsp;</TH>
   </TR>
    <TR>
      <TH COLSPAN="8" align="center"><a href="#" onclick="window.close();"><input type="submit" value="Close" onClick = Getmovementtypes();></a></TH>
    </TR>
  </table>
  </form>
</body>


</html>

<script language="JavaScript">

function Getmovementtypes()
{
	var len = document.form.movementtype.length;
	var types = "";
	var count  = 0;
	var checkFound =  false;
	if (len == undefined)
		len = 1;
	for ( var i = 0; i < len; i++) {
		if (len == 1 && (!document.form.movementtype.checked)) {
			checkFound = false;
		}

		else if (len == 1 && document.form.movementtype.checked) {
			checkFound = true;
			count++;
			types = "'"+document.form.movementtype.value+"'";
		}

		else {
			if (document.form.movementtype[i].checked) {
				checkFound = true;
				count++;
				if(types == "")
					types = "'"+document.form.movementtype[i].value+"'";
				else
					types = types + ",'" + document.form.movementtype[i].value+"'";
		}
		}

	}
	 
	window.opener.<%=formName%>.TYPE.value = types;
	window.close();
	
}

</script>
