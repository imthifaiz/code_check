<%@ page import="com.track.dao.ItemMstDAO"%>
<%@ page import="com.track.gates.DbBean"%>
<%@ page import="com.track.gates.Generator"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.util.*"%>
<%@ page import="com.track.gates.*"%>
<%@ page import="com.track.constants.IDBConstants" %>
<%@page import="java.text.DecimalFormat" %>
<%@ page import="java.util.*"%>
<%@ include file="header.jsp"%>
<html>
<head>
<script language="javascript">

</script>

<script language="JavaScript" type="text/javascript"
	src="js/calendar.js"></script>
<title>Order Details</title>
</head>
<link rel="stylesheet" href="css/style.css">
<%@ include file="body.jsp"%>
<FORM name="form1" method="post">
<br>
                  
<TABLE border="0" width="100%" cellspacing="0" cellpadding="0"
	align="center" bgcolor="#dddddd">
	<TR>
		<TH BGCOLOR="#000066" COLSPAN="20"><font color="white">Order Details </font></TH>
	</TR>
</TABLE>
<br>


<TABLE WIDTH="80%" border="0" cellspacing="1" cellpadding=2
	align="center">
	
		
	
	
<%
StrUtils _strUtils = new StrUtils();
HTReportUtil rptUtil = new HTReportUtil();
rptUtil.setmLogger(mLogger);
ArrayList QryList =  null;
DecimalFormat decformat = new DecimalFormat("#,##0.00");
session = request.getSession();
	String plant = (String) session.getAttribute("PLANT");
    String userID = (String) session.getAttribute("LOGIN_USER");
	String orderType =   _strUtils.fString(request.getParameter("orderType"));
	String orderNo = _strUtils.fString(request.getParameter("orderNo"));
	
	
		
	%>
	
		<TR>
		<TD width="10%"><%=orderType%></TD>
		<TD colspan=2><%=orderNo%></TD>
		<TD align="center" width="10%"></TD>
		<TD align="center" width="10%"></TD>
		<TD align="center" width="10%"></TD>
		<TD align="center" width="10%"></TD>
		<TD align="center" width="10%"></TD>
		<TD align="center" width="10%"></TD>
		<TD align="center" width="10%"></TD>		
	</TR>
	
	<TR></TR>
	<TR BGCOLOR="#000066">

		<TH><font color="#ffffff" align="left"><b> Product ID</TH>
		<TH><font color="#ffffff" align="left"><b>Description</TH>
		<TH><font color="#ffffff" align="left"><b>Order Qty</TH>
		<TH><font color="#ffffff" align="left"><b>Unit Cost</TH>
		<TH><font color="#ffffff" align="left"><b>Tax(%)</TH>
		<TH><font color="#ffffff" align="left"><b>Order Price</TH>
		<TH><font color="#ffffff" align="left"><b>Tax</TH>
		<TH><font color="#ffffff" align="left"><b>Total</TH>
		
	</TR>
	<%
	if(orderType.equalsIgnoreCase("OUTBOUND")){
		  Hashtable ht = new Hashtable();
          if(_strUtils.fString(orderNo).length() > 0)      ht.put("A.DONO",orderNo);
          QryList = rptUtil.getCustomerDOInvoiceSummary(ht,"","","",plant, "","","","");
      
		
	}else if(orderType.equalsIgnoreCase("INBOUND")){
		Hashtable ht = new Hashtable();
        if(_strUtils.fString(orderNo).length() > 0)      ht.put("A.PONO",orderNo);
        QryList = rptUtil.getSupplierPOInvoiceSummary(ht,"","","",plant, "","","");
	}
		
		
		if (QryList.size() > 0) {
            int iIndex = 0,Index = 0;
             int irow = 0;
             
            double sumprdQty = 0;String lastProduct="";
            
            double totalOrdPrice=0,totaltax=0,totOrdPriceWTax=0;//issPriceSubTot=0,OrdPriceGrandTot=0,taxGrandTotal=0,priceWTaxGrandTotal=0;
            double unitprice=0;double orderAmt =0,	 tax =0, ordAmtwTax=0;
            String orderAmount="";
                for (int iCnt =0; iCnt<QryList.size(); iCnt++){
                      
                        String result="";
                        Map lineArr = (Map) QryList.get(iCnt);
                        String custcode =(String)lineArr.get("custcode");
                       
                        Float gstpercentage =  Float.parseFloat(((String) lineArr.get("Tax").toString())) ;
             	       if(orderType.equalsIgnoreCase("OUTBOUND")){
             	    	
          	        	 unitprice = Double.parseDouble((String)lineArr.get("unitprice"));
          	             unitprice = StrUtils.RoundDB(unitprice,2);
          	          
          	             //indiviual subtotal price details
          	             orderAmount = (String)lineArr.get("ordPrice");
          	             orderAmt = Double.parseDouble((String)lineArr.get("ordPrice"));
          	            orderAmt = StrUtils.RoundDB(orderAmt,2);
                         tax = (orderAmt*gstpercentage)/100;
                         ordAmtwTax = orderAmt+tax;
                       //total price details
                         totalOrdPrice=totalOrdPrice+Double.parseDouble(((String)lineArr.get("ordPrice").toString()));
                         totaltax  =totaltax + tax;
                         totOrdPriceWTax = totOrdPriceWTax+ordAmtwTax;
          	           }else if(orderType.equalsIgnoreCase("INBOUND")){
          	        	 unitprice = Double.parseDouble((String)lineArr.get("unitcost"));
          	             unitprice = StrUtils.RoundDB(unitprice,2);
          	          
          	             //indiviual subtotal price details
          	             orderAmount = (String)lineArr.get("OrderCost");
          	             orderAmt = Double.parseDouble((String)lineArr.get("OrderCost"));
          	            orderAmt = StrUtils.RoundDB(orderAmt,2);
                         tax = (orderAmt*gstpercentage)/100;
                         ordAmtwTax = orderAmt+tax;
                       //total price details
                         totalOrdPrice=totalOrdPrice+Double.parseDouble(((String)lineArr.get("OrderCost").toString()));
                         totaltax  =totaltax + tax;
                         totOrdPriceWTax = totOrdPriceWTax+ordAmtwTax;  
          	           }
                        
                        
                        //Grand total details
                      //  priceGrandTot = priceGrandTot+Double.parseDouble((String)lineArr.get("ordPrice"));;
                      //  taxGrandTot = taxGrandTot+tax;
                      //  priceWTaxGrandTot = priceWTaxGrandTot+ordPricewTax;
                     
             
                        String bgcolor = ((irow == 0) || (irow % 2 == 0)) ? "#FFFFFF"  : "#dddddd";
                       
                       if((lastProduct.equalsIgnoreCase("") || lastProduct.equalsIgnoreCase(custcode))){
                       	Index = Index + 1; 
                       	%>
                        <TR bgcolor = "<%=bgcolor%>">
				      	<TD width="10%"><%=(String) lineArr.get("item")%></TD>
				      	<TD align= "center" width="5%"><%=(String)lineArr.get("itemdesc")%></TD>
						<TD align= "center" width="5%"><%=(String)lineArr.get("qtyor")%></TD>
						<TD align= "center" width="12%"><%=(DbBean.CURRENCYSYMBOL)+ decformat.format(unitprice)%></TD>
						<TD align="center" width="15%"><%=gstpercentage%></TD>
						<TD align="center" width="10%"><%= (DbBean.CURRENCYSYMBOL)+ decformat.format(orderAmt)%></TD>
						<TD align="center" width="10%"><%=(DbBean.CURRENCYSYMBOL)+ decformat.format(tax)%></TD>
						<TD align="center" width="10%"><%=(DbBean.CURRENCYSYMBOL)+ decformat.format(ordAmtwTax)%></TD>
	
				      </TR> 
                     <%   if(iIndex+1 == QryList.size()){
                                irow=irow+1;
                                bgcolor = ((irow == 0) || (irow % 2 == 0)) ? "#FFFFFF"  : "#dddddd";  
					%>
                             <TR bgcolor ="<%=bgcolor%>" >
                             <TD colspan=4></TD> <TD align= "right"><b>Grand Total:</b></td><TD align= "right"><b><%=(DbBean.CURRENCYSYMBOL)+ decformat.format(StrUtils.RoundDB(totalOrdPrice,2)) %></b></TD><TD align= "right"><b><%=(DbBean.CURRENCYSYMBOL)+ decformat.format(StrUtils.RoundDB(totaltax,2)) %></b></TD><TD align= "right"><b><%=(DbBean.CURRENCYSYMBOL)+ decformat.format(StrUtils.RoundDB(totOrdPriceWTax,2)) %></b></TD> </TR>
                                   
                      <%
                        } }
                       else{
                          
                       	  totalOrdPrice=totalOrdPrice-Double.parseDouble(orderAmount);
                       	  totalOrdPrice = StrUtils.RoundDB(totalOrdPrice,2);
                       	  
                       	  totaltax=totaltax-tax;
                       	  totaltax = StrUtils.RoundDB(totaltax,2);
                       	  
                       	  totOrdPriceWTax=totOrdPriceWTax-ordAmtwTax;
                       	  totOrdPriceWTax = StrUtils.RoundDB(totOrdPriceWTax,2);
					%>
					
					<TR bgcolor ="<%=bgcolor%>" >
                             <TD colspan=4></TD> <TD align= "right"><b>Grand Total:</b></td><TD align= "right"><b><%=(DbBean.CURRENCYSYMBOL)+ decformat.format(StrUtils.RoundDB(totalOrdPrice,2)) %></b></TD><TD align= "right"><b><%=(DbBean.CURRENCYSYMBOL)+ decformat.format(StrUtils.RoundDB(totaltax,2)) %></b></TD><TD align= "right"><b><%=(DbBean.CURRENCYSYMBOL)+ decformat.format(StrUtils.RoundDB(totOrdPriceWTax,2)) %></b></TD> </TR>
                            
                   <%    irow=irow+1;
                            Index = 0;
                            Index=Index+1;
                            bgcolor = ((irow == 0) || (irow % 2 == 0)) ? "#FFFFFF"  : "#dddddd"; 
                    %>
                        
                         <TR bgcolor = "<%=bgcolor%>">
				      	<TD width="10%"><%=(String) lineArr.get("item")%></TD>
				      	<TD align= "center" width="5%"><%=(String)lineArr.get("itemdesc")%></TD>
						<TD align= "center" width="5%"><%=(String)lineArr.get("qtyor")%></TD>
						<TD align= "center" width="12%"><%=(DbBean.CURRENCYSYMBOL)+ decformat.format(unitprice)%></TD>
						<TD align="center" width="15%"><%=gstpercentage%></TD>
						<TD align="center" width="10%"><%= (DbBean.CURRENCYSYMBOL)+ decformat.format(orderAmt)%></TD>
						<TD align="center" width="10%"><%=(DbBean.CURRENCYSYMBOL)+ decformat.format(tax)%></TD>
						<TD align="center" width="10%"><%=(DbBean.CURRENCYSYMBOL)+ decformat.format(ordAmtwTax)%></TD>
					
				      </TR> 
                       
                      
                        
                       <%    totalOrdPrice=Double.parseDouble(orderAmount);
                      	     totalOrdPrice = StrUtils.RoundDB(totalOrdPrice,2);
                      	  
	                       	  totaltax=tax;
	                       	  totaltax = StrUtils.RoundDB(totaltax,2);
                      	  
	                       	  totOrdPriceWTax=ordAmtwTax;
	                       	  totOrdPriceWTax = StrUtils.RoundDB(totOrdPriceWTax,2);

                         	
                                 if(iIndex+1 == QryList.size()){ 
                                     irow=irow+1;
                    
                                     bgcolor = ((irow == 0) || (irow % 2 == 0)) ? "#FFFFFF"  : "#dddddd"; 
                  %> 
                  
                  	<TR bgcolor ="<%=bgcolor%>" >
                     <TD colspan=4></TD> <TD align= "right"><b>Grand Total:</b></td><TD align= "right"><b><%=(DbBean.CURRENCYSYMBOL)+ decformat.format(StrUtils.RoundDB(totalOrdPrice,2)) %></b></TD><TD align= "right"><b><%=(DbBean.CURRENCYSYMBOL)+ decformat.format(StrUtils.RoundDB(totaltax,2)) %></b></TD><TD align= "right"><b><%=(DbBean.CURRENCYSYMBOL)+ decformat.format(StrUtils.RoundDB(totOrdPriceWTax,2)) %></b></TD> </TR>
                
                  <%               
                        }
                        }
                             irow=irow+1;
                             iIndex=iIndex+1;
                             lastProduct = custcode;
                             if(QryList.size()==iCnt+1){
                            	 irow=irow+1;
                                 bgcolor = ((irow == 0) || (irow % 2 == 0)) ? "#FFFFFF"  : "#dddddd"; 
                     %>    
                     
                                 <%}
                             }
                             }%>
             
	
	
</TABLE>
<br>
<table align="center">
	<TR>
		<td><input type="button" value="Close" onclick="window.close();">&nbsp;</td>
	
	</TR>

	
</table>


</FORM>
</html>
<%@ include file="footer.jsp"%>