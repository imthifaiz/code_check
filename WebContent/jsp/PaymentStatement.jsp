<%@ page import="com.track.gates.DbBean"%>
<%@ page import="com.track.gates.Generator"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.util.*"%>
<%@ page import="com.track.dao.*"%>
<%@ page import="com.track.gates.*"%>
<%@ page import="java.util.*"%>
<%@page import ="java.text.DecimalFormat" %>
<%@ include file="header.jsp"%>
<jsp:useBean id="su" class="com.track.util.StrUtils" />
<html>
<head>

<script language="javascript">
var subWin = null;
function popUpWin(URL) {
  subWin = window.open(URL, 'ProductList', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=700,height=400,left = 200,top = 184');
}


function onGo(){

   var flag    = "false";
   
   var STATEMENT_DATE   = document.form.STATEMENT_DATE.value;
   var FROM_DATE   = document.form.FROM_DATE.value;
   var TO_DATE     = document.form.TO_DATE.value;
   var ORDER       = document.form.Order.value;
   var CUSTNAME    = document.form.custName.value;
   var orderNo    = document.form.orderNo.value  
  /* if(CUSTNAME==null || CUSTNAME=="")
	{
		alert("Please choose Customer/Supplier Name");
		return false;	
	}*/
   
   if(FROM_DATE != null     && FROM_DATE != "") { flag = true;}
   if(TO_DATE != null    && TO_DATE != "") { flag = true;}
   if(ORDER != null     && ORDER != "") { flag = true;}

   if(CUSTNAME != null    && CUSTNAME != "") { flag = true;}
      

  if(flag == "false"){ alert("Please define any one search criteria"); return false;}

  if(orderNo != null    && orderNo != "")
	  {
	       document.form.action="PaymentOrderStatement.jsp?action=View&Order="+ORDER+"&OrderNo="+orderNo+"&CustName="+CUSTNAME+"&StatementDate="+STATEMENT_DATE+"&fromDate="+FROM_DATE+"&toDate="+TO_DATE;
		   document.form.submit();
	  }
  else
	  {
	  	    document.form.action="PaymentStatement.jsp";
	  		document.form.submit();
	  }
  
}

function onPrint(){
	
    document.form.action="/track/OrderPaymentServlet?action=printAgeingReport";
    document.form.submit();
}

</script>

<script language="JavaScript" type="text/javascript" src="js/calendar.js"></script>
<title>Payment Summary(Ageing) </title>
</head>
<link rel="stylesheet" href="css/style.css">
<%

StrUtils _strUtils     = new StrUtils();
Generator generator   = new Generator();
OrderPaymentUtil pmtutil      = new OrderPaymentUtil();
OrderPaymentDAO _pmtdao=new OrderPaymentDAO();
pmtutil.setmLogger(mLogger);
DateUtils _dateUtils = new DateUtils();
ArrayList movQryList  = new ArrayList();
ArrayList movQrycustList  = new ArrayList();

session= request.getSession();

String FROM_DATE ="",  TO_DATE = "", fdate="",tdate="",PGaction="",STATEMENT_DATE="";
String Order="",custName="",ordRefNo="",custcode="",orderNo="";

PGaction         = _strUtils.fString(request.getParameter("PGaction")).trim();
String html = "",cntRec ="false";
String plant = (String)session.getAttribute("PLANT");
STATEMENT_DATE     = _strUtils.fString(request.getParameter("STATEMENT_DATE"));
FROM_DATE     = _strUtils.fString(request.getParameter("FROM_DATE"));
TO_DATE   = _strUtils.fString(request.getParameter("TO_DATE"));

if(FROM_DATE==null) FROM_DATE=""; else FROM_DATE = FROM_DATE.trim();
String curDate =_dateUtils.getDate();
if(STATEMENT_DATE.length()<0||STATEMENT_DATE==null||STATEMENT_DATE.equalsIgnoreCase(""))STATEMENT_DATE=curDate;
if(FROM_DATE.length()<0||FROM_DATE==null||FROM_DATE.equalsIgnoreCase(""))FROM_DATE=curDate;

if (FROM_DATE.length()>5)

fdate    = FROM_DATE.substring(6)+"-"+ FROM_DATE.substring(3,5)+"-"+FROM_DATE.substring(0,2);

if(TO_DATE==null) TO_DATE=""; else TO_DATE = TO_DATE.trim();
if (TO_DATE.length()>5)
tdate    = TO_DATE.substring(6)+"-"+ TO_DATE.substring(3,5)+"-"+TO_DATE.substring(0,2);


Order = _strUtils.fString(request.getParameter("Order"));
custName     = _strUtils.fString(StrUtils.replaceCharacters2Recv(request.getParameter("custName")));
ordRefNo     = StrUtils.fString(request.getParameter("ordRefNo"));
orderNo     = StrUtils.fString(request.getParameter("orderNo"));

if(Order.length()==0)Order ="OUTBOUND"; 

if(PGaction.equalsIgnoreCase("View")){
 
 try{
	 	movQrycustList = pmtutil.getcustomerorsuppliername(plant,fdate,tdate,Order,custName);       
        // movQryList = pmtutil.getPaymentStatementDetails(plant,fdate,tdate,Order,custName,ordRefNo);
        boolean flag =  pmtutil.InsertTempOrderPayment(plant, fdate,tdate,Order,custName);

		if(movQryList.size()<=0) 
			cntRec ="true";



 }catch(Exception e) { }
}
%>
<%@ include file="body.jsp"%>
<FORM name="form" method="post" action="PaymentStatement.jsp" >
<input type="hidden" name="xlAction" value="">
<input type="hidden" name="PGaction" value="View">
<INPUT name="ACTIVE" type = "hidden" >
<INPUT name="PAYMENT_DESC" type = "hidden" >
  <br>
  <TABLE border="0" width="100%" cellspacing="0" cellpadding="0" align="center" bgcolor="#dddddd">
    <TR>
      <TH BGCOLOR="#000066" COLSPAN="20"><font color="white">Payment Summary(Ageing) </font></TH>
    </TR>
  </TABLE>
  <br>
  <TABLE border="0" width="70%"  cellspacing="0" cellpadding="0" align="center" bgcolor="#dddddd">
 
  <TR>
          <TH ALIGN="left" >&nbsp;Statement Date : </TH>
          <TD><INPUT name="STATEMENT_DATE" type = "TEXT" value="<%=STATEMENT_DATE%>" size="20"  MAXLENGTH=20 class = inactivegry READONLY>&nbsp;&nbsp;<a href="javascript:show_calendar('form.STATEMENT_DATE');" onmouseover="window.status='Date Picker';return true;" onmouseout="window.status='';return true;"><img src="images\show-calendar.gif" width=24 height=22 border=0></a></TD>
          <TH ALIGN="left"></TH>
          <TD></TD>
		 
    </TR>
    
    <TR>
          <TH ALIGN="left" >&nbsp;From_Date : </TH>
          <TD><INPUT name="FROM_DATE" type = "TEXT" value="<%=FROM_DATE%>" size="20"  MAXLENGTH=20 class = inactivegry READONLY>&nbsp;&nbsp;<a href="javascript:show_calendar('form.FROM_DATE');" onmouseover="window.status='Date Picker';return true;" onmouseout="window.status='';return true;"><img src="images\show-calendar.gif" width=24 height=22 border=0></a></TD>
          <TH ALIGN="left">To_Date : </TH>
          <TD><INPUT name="TO_DATE" type = "TEXT" value="<%=TO_DATE%>" size="20"  MAXLENGTH=20 class = inactivegry READONLY>&nbsp;&nbsp;<a href="javascript:show_calendar('form.TO_DATE');" onmouseover="window.status='Date Picker';return true;" onmouseout="window.status='';return true;"><img src="images\show-calendar.gif" width=24 height=22 border=0></a></TD>
		 
    </TR>
    
       <tr>
       		<th WIDTH="20%" ALIGN="left">&nbsp;Order :</th>
			<TD><select name="Order" >
					<option value="INBOUND" <%if(Order.equalsIgnoreCase("INBOUND")){ %>selected<%} %>>INBOUND</option>
					<option value="OUTBOUND" <%if(Order.equalsIgnoreCase("OUTBOUND")){ %>selected<%} %> >OUTBOUND</option>
				</select>
			</TD>
			<th WIDTH="20%" ALIGN="left">Customer/Supplier :</th>
			<TD><INPUT name="custName" type="TEXT" value="<%=su.forHTMLTag(custName)%>" size="20" MAXLENGTH=80>
				 <a href="#" onClick="javascript:popUpWin('list/orderContactPersonList.jsp?custName='+form.custName.value+'&orderType='+form.Order.value);"> <img src="images/populate.gif" border="0" /> </a>
				 </TD>
		</tr>
		<tr>
			<!--  <th WIDTH="20%" ALIGN="left">Ref No :</th>
			<TD><INPUT name="ordRefNo" type="TEXT" value="<%=su.forHTMLTag(ordRefNo)%>" size="20" MAXLENGTH=80></TD>
			-->
			<th WIDTH="20%" ALIGN="left">Order No :</th>
			<TD><INPUT name="orderNo" type="TEXT" value="<%=su.forHTMLTag(orderNo)%>" size="20" MAXLENGTH=80></TD>
			 <TD><input type="button" value="View"  onClick="javascript:return onGo();"></TD>
		</tr>
							
  </TABLE>
  <br>
  <TABLE WIDTH="70%"  border="0" cellspacing="1" cellpadding = 2 align = "center">
    <TR BGCOLOR="#000066">

                <TH><font color="#ffffff" align="center">Date</TH>
                <TH><font color="#ffffff" align="left"><b>Order No</TH>
                <TH><font color="#ffffff" align="left"><b>Customer Name</TH>
                <TH><font color="#ffffff" align="left"><b>Amount</TH>
               <!--   <TH><font color="#ffffff" align="left"><b>Paid Amount</TH>-->
                <TH><font color="#ffffff" align="left"><b>Balance</TH>
                
       </tr>
       <%
	       if(movQrycustList.size()<=0 && cntRec=="true" ){ %>
		    <TR><TD colspan=15 align=center>No Data For This criteria</TD></TR>
		  <%}%>

		  <%
		  DecimalFormat decformat = new DecimalFormat("#,##0.00");
		  double totaldue = 0;
		  double amount=0;
		  double balance=0;
		  int k=0;
		  String total="",stmtdate="";
		  
		  //stmtdate   = _strUtils.fString(request.getParameter("STATEMENT_DATE"));

		  for (int i = 0; i < movQrycustList.size(); i++)
			{
			  			  
				Map lineArrcust = (Map) movQrycustList.get(i);
	                      
	             custName = (String)lineArrcust.get("custname");
	             custcode = (String)lineArrcust.get("custcode");
	             String pmtdays = (String)lineArrcust.get("pmtdays");
	             
	    	        
	           movQryList = pmtutil.getPaymentStatementDetails(plant,fdate,tdate,Order,custcode);
	           
	           for (int iCnt =0; iCnt<movQryList.size(); iCnt++){
               		Map lineArr = (Map) movQryList.get(iCnt);
               		int iIndex = iCnt + 1;
              		String bgcolor = ((iCnt == 0) || (iCnt % 2 == 0)) ? "#FFFFFF"  : "#dddddd";
               		String ordNo = (String)lineArr.get("ORDNO");
               		if (ordNo.indexOf("Pmtrefno") != -1) {
               			ordNo = ordNo.replaceAll("Pmtrefno", "-");

        			}
               		String ordDate = (String)lineArr.get("TRANDATE");
               		//String custname = (String)lineArr.get("custname");
                
               		
               		amount = Double.parseDouble((String)lineArr.get("AMOUNT"));
               		amount = StrUtils.RoundDB(amount,2);
               		balance = Double.parseDouble((String)lineArr.get("BALANCE"));
               		balance = StrUtils.RoundDB(balance,2);
               		
               		String amtReceived= decformat.format(amount);
               		String balToPay= decformat.format(balance);
     		   		String orderno = "";
     		   		orderno = (String)lineArr.get("ORDNO");
               		int plusIndex = orderno.indexOf("Pmtrefno");
               		if (plusIndex != -1) {
            	   		orderno = orderno.substring(0, plusIndex);
                   
               		}   
               		totaldue = Double.parseDouble((String)lineArr.get("BALANCE"));
               		totaldue = StrUtils.RoundDB(totaldue,2);
               		total = decformat.format(totaldue);
               		
               		
               		
       %>

           <TR bgcolor = "<%=bgcolor%>">
            <TD align="center"><%=ordDate%></TD>
            <TD align= "left"><a href ="PaymentOrderStatement.jsp?action=View&Order=<%=Order%>&OrderNo=<%=orderno%>&CustName=<%=custName%>&StatementDate=<%=STATEMENT_DATE%>&fromDate=<%=FROM_DATE%>&toDate=<%=TO_DATE%>"><%=ordNo%></a></TD>
            <TD align= "center"><%=custName%></TD>
            <TD align= "right"><%=amtReceived%></TD>
            <TD align= "right"><%=balToPay%></TD>
            </TR>           
       <% }
	           if (movQryList.size() >0)
	     		{ 
	     			k=k+1;
	     			String bgItemcolor = ((k == 0) || (k % 2 == 0)) ? "#FFFFFF"
	     					: "#dddddd";
	     			
	     		%>
	     		
	     		<TR bgcolor = "<%=bgItemcolor%>">
	              
	               
	               <TD align="center"></TD>
	               <TD align="center"></TD>
	               <TD align="center"></TD>
	               <TD align= "right" ><b>Total:</b></TD>
	               <TD align= "right" ><b><%=total%></b></TD>
	               
	             </TR>
	       <% 
	     		}
	           totaldue = 0;
	           
	           
	   		if(k==0)
	   			k=1;
		}
	%>
  </TABLE>
      <br>
    <table align="center" >
     <TR>
   		<td>  
   			<input type="button" value=" Back " onClick="window.location.href='../home'">&nbsp; 
   			<input type="button" value="Print Ageing Report"  name="action" onclick="javascript:return onPrint();" />
       	</td>
     </TR>
    </table>
  </FORM>
  
<%@ include file="footer.jsp"%>
