<%@ page import="com.track.gates.DbBean"%>
<%@ page import="com.track.gates.Generator"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.util.*"%>
<%@ page import="com.track.gates.*"%>
<%@ page import="java.util.*"%>
<%@ include file="header.jsp"%>
<%@page import="java.text.DecimalFormat"%>
<html>
<head>
<% String plant = (String)session.getAttribute("PLANT");%>
<script language="javascript">
var subWin = null;
function popUpWin(URL) {
  subWin = window.open(URL, 'ProductList', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=500,height=200,left = 200,top = 184');
}
function ExportReport()
{
  var flag    = "false";
  document.form1.FROM_DATE.value;
  document.form1.TO_DATE.value;
  
  document.form1.ITEM.value;
  document.form1.ORDERNO.value;
  document.form1.JOBNO.value;
  document.form1.DIRTYPE.value;
  document.form1.CUSTOMER.value;
  document.form1.xlAction.value="GenerateXLSheet";
  document.form1.action="mobileOrderExcel.jsp";
 
  var  DIRTYPE= document.form1.DIRTYPE.value;
      
  document.form1.submit();
  
}
function onGo(){

   var flag    = "false";
 var companySel ='<%=plant%>';
 if (companySel=="track"){
   var COMPANY      = document.form1.COMPANY.value;
    if(COMPANY != null     && COMPANY != "") { flag = true;}
     
   if(flag == "false"){ alert("Please Enter/Select Company "); return false;}
 }
   var FROM_DATE      = document.form1.FROM_DATE.value;
   var TO_DATE        = document.form1.TO_DATE.value;
   var DIRTYPE        = document.form1.DIRTYPE.value;
   var USER           = document.form1.CUSTOMER.value;
   var ITEMNO         = document.form1.ITEM.value;
   var ORDERNO        = document.form1.ORDERNO.value;
   var JOBNO          = document.form1.JOBNO.value;
  
   
   if(FROM_DATE != null     && FROM_DATE != "") { flag = true;}
   if(TO_DATE != null    && TO_DATE != "") { flag = true;}
   if(DIRTYPE != null     && DIRTYPE != "") { flag = true;}

   if(USER != null    && USER != "") { flag = true;}
   if(ITEMNO != null     && ITEMNO != "") { flag = true;}
   
    if(ORDERNO != null     && ORDERNO != "") { flag = true;}
    if(JOBNO != null     && JOBNO != "") { flag = true;}

   //if(flag == "false"){ alert("Please define any one search criteria"); return false;}

  document.form1.action="billingMobileShopping.jsp";
  document.form1.submit();
}

</script>

<script language="JavaScript" type="text/javascript" src="js/calendar.js"></script>
<title>Mobile Shopping Summary</title>
</head>
<link rel="stylesheet" href="css/style.css">
<%

StrUtils _strUtils     = new StrUtils();
Generator generator   = new Generator();
HTReportUtil movHisUtil       = new HTReportUtil();
movHisUtil.setmLogger(mLogger);
DateUtils _dateUtils = new DateUtils();
ArrayList movQryList  = new ArrayList();
ArrayList movCustomerList  = new ArrayList();

int k=0;
String rowColor="";		


session= request.getSession();


String FROM_DATE ="",  TO_DATE = "", PICKSTATUS="",ORDERTYPE="",ISSUESTATUS="",DIRTYPE ="",BATCH ="",USER="",ITEM="",DESC="",fdate="",tdate="",JOBNO="",ITEMNO="",ORDERNO="",CUSTOMER="",PGaction="";
String CUSTOMERID="";
PGaction         = _strUtils.fString(request.getParameter("PGaction")).trim();
String html = "",cntRec ="false";
String COMPANY="",SALESPERCNT="",SALESRATE="",AMOUNT_TO_BILL="";
float Total=0,subtotal=0,unitprice=0;
 String chargeBy="",salesRate="",noOfOrders="0";
      double totalAmountToBill=0;
DecimalFormat decformat = new DecimalFormat("#,##0.00");

FROM_DATE     = _strUtils.fString(request.getParameter("FROM_DATE"));
TO_DATE   = _strUtils.fString(request.getParameter("TO_DATE"));
ORDERTYPE   = _strUtils.fString(request.getParameter("ORDERTYPE"));
if(FROM_DATE==null) FROM_DATE=""; else FROM_DATE = FROM_DATE.trim();
String curDate =_dateUtils.getDate();
if(FROM_DATE.length()<0||FROM_DATE==null||FROM_DATE.equalsIgnoreCase(""))FROM_DATE=curDate;

if (FROM_DATE.length()>5)

fdate    = FROM_DATE.substring(6)+"-"+ FROM_DATE.substring(3,5)+"-"+FROM_DATE.substring(0,2);



if(TO_DATE==null) TO_DATE=""; else TO_DATE = TO_DATE.trim();
if (TO_DATE.length()>5)
tdate    = TO_DATE.substring(6)+"-"+ TO_DATE.substring(3,5)+"-"+TO_DATE.substring(0,2);



DIRTYPE       = _strUtils.fString(request.getParameter("DIRTYPE"));
JOBNO         = _strUtils.fString(request.getParameter("JOBNO"));
USER          = _strUtils.fString(request.getParameter("USER"));
ITEMNO        = _strUtils.fString(request.getParameter("ITEM"));
DESC          = _strUtils.fString(request.getParameter("DESC"));
ORDERNO       = _strUtils.fString(request.getParameter("ORDERNO"));
CUSTOMER      = _strUtils.fString(request.getParameter("CUSTOMER"));
CUSTOMERID      = _strUtils.fString(request.getParameter("CUSTOMERID"));
PICKSTATUS    = _strUtils.fString(request.getParameter("PICKSTATUS"));
ISSUESTATUS  = _strUtils.fString(request.getParameter("ISSUESTATUS"));
  chargeBy=_strUtils.fString(request.getParameter("SALESPERCNT"));
   salesRate=_strUtils.fString(request.getParameter("ENQUIRYRATE"));
   AMOUNT_TO_BILL = _strUtils.fString(request.getParameter("AMOUNT_TO_BILL"));
   
   System.out.println("chargeBy"+chargeBy+"salesRate ::: "+salesRate);
    if(plant.equalsIgnoreCase("track")){
       COMPANY   = _strUtils.fString(request.getParameter("COMPANY"));
    }else{
    COMPANY=plant;
    ArrayList al=movHisUtil.getsalesChargableRate( COMPANY);
                       
                       if(al.size()>0){
                           Map lineArr = (Map) al.get(0);
                           chargeBy = StrUtils.fString((String) lineArr.get("SALES_CHARGE_BY"));
                           salesRate = StrUtils.fString((String) lineArr.get("SALESRATE"));
                          
                       }else{
                           chargeBy="";
                           salesRate="0";
                           noOfOrders="0";
                       }
                       
      
}


if(DIRTYPE.length()<=0){
DIRTYPE = "MOBILE ORDER";
}

	if(PGaction.equalsIgnoreCase("View")){
	 
	 try{
         
              Hashtable ht = new Hashtable();
              if(_strUtils.fString(JOBNO).length() > 0)        ht.put("A.JOBNUM",JOBNO);
              if(_strUtils.fString(ITEMNO).length() > 0)       ht.put("B.ITEM",ITEMNO);
              if(_strUtils.fString(ORDERNO).length() > 0)      ht.put("B.DONO",ORDERNO);
              if(_strUtils.fString(CUSTOMER).length() > 0)     ht.put("B.CUSTNAME",CUSTOMER);
              if(_strUtils.fString(CUSTOMERID).length() > 0)   ht.put("A.CUSTCODE",CUSTOMERID);
              if(_strUtils.fString(ORDERTYPE).length() > 0)    ht.put("ORDERTYPE",ORDERTYPE);
              if(_strUtils.fString(ISSUESTATUS).length() > 0)  ht.put("B.LNSTAT",ISSUESTATUS);
              if(_strUtils.fString(PICKSTATUS).length() > 0)   ht.put("B.PICKSTATUS",PICKSTATUS);
                     ArrayList al=movHisUtil.getsalesChargableRate( COMPANY, DESC, fdate, tdate, ht);
                       
                       if(al.size()>0){
                           Map lineArr = (Map) al.get(0);
                           chargeBy = StrUtils.fString((String) lineArr.get("SALES_CHARGE_BY"));
                           salesRate = StrUtils.fString((String) lineArr.get("SALESRATE"));
                           noOfOrders = StrUtils.fString((String) lineArr.get("NO_OF_ORDERS"));
                           
                       }else{
                           chargeBy="";
                           salesRate="0";
                           noOfOrders="0";
                       }
                       
                 System.out.println("Inside View chargeBy"+chargeBy+"salesRate ::: "+salesRate+"noOfOrders ::: "+noOfOrders);
                  movQryList = movHisUtil.getMobileShoppingSummary(ht,fdate,tdate,DIRTYPE,COMPANY, DESC,"");

	
	 }catch(Exception e) { }
	}
%>

<%@ include file="body.jsp"%>
<FORM name="form1" method="post" action="billingMobileShopping.jsp" >
<input type="hidden" name="xlAction" value="">
<input type="hidden" name="PGaction" value="View">
<input type="hidden" name="SALESPERCNT" value=<%=chargeBy%>>

  <br>
  <TABLE border="0" width="100%" cellspacing="0" cellpadding="0" align="center" bgcolor="#dddddd">
    <TR>
      <TH BGCOLOR="#000066" COLSPAN="20"><font color="white">Mobile Shopping Summary</font></TH>
    </TR>
  </TABLE>
  <br>
  <TABLE border="0" width="80%" height = "25%" cellspacing="0" cellpadding="0" align="center" bgcolor="#dddddd">
 
  <TR>
      <TH ALIGN="left" >Company</TH>
        <TD >
        
            <%if(plant.equalsIgnoreCase("track")){%>
              <INPUT name="COMPANY" type="TEXT" value="<%=COMPANY%>" size="20" MAXLENGTH="20" />
            <a href="#" onClick="javascript:popUpWin('plant_list_for_billing.jsp?TYPE=MOBILE ORDER&COMPANY='+form1.COMPANY.value);">
            <img src="images/populate.gif" border="0"/>
            </a>
            <%}else{%>
              <INPUT name="COMPANY" type="TEXT" value="<%=COMPANY%>" size="20" MAXLENGTH="20" class = inactivegry readonly/>
              <%}%>
        </TD>
    <TH ALIGN="left">Sales Rate:  </TH>
          <TD ><INPUT name="SALESRATE" type = "TEXT" value="<%=salesRate%>" size="15"  MAXLENGTH=20 class = inactivegry readonly>
          &nbsp;&nbsp;<b>Billable Amount :&nbsp;&nbsp;&nbsp;</b><INPUT name="AMOUNT_TO_BILL" type = "TEXT" value="<%=AMOUNT_TO_BILL%>" size="15"  MAXLENGTH=20 class = inactivegry readonly >
          </TD>
  </tr>

  
    <TR>
          <TH ALIGN="left" >&nbsp;From_Date : </TH>
          <TD><INPUT name="FROM_DATE" type = "TEXT" value="<%=FROM_DATE%>" size="20"  MAXLENGTH=20 class = inactivegry READONLY>&nbsp;&nbsp;<a href="javascript:show_calendar('form1.FROM_DATE');" onmouseover="window.status='Date Picker';return true;" onmouseout="window.status='';return true;"><img src="images\show-calendar.gif" width=24 height=22 border=0></a></TD>
          <TH ALIGN="left">To_Date : </TH>
          <TD><INPUT name="TO_DATE" type = "TEXT" value="<%=TO_DATE%>" size="20"  MAXLENGTH=20 class = inactivegry READONLY>&nbsp;&nbsp;<a href="javascript:show_calendar('form1.TO_DATE');" onmouseover="window.status='Date Picker';return true;" onmouseout="window.status='';return true;"><img src="images\show-calendar.gif" width=24 height=22 border=0></a></TD>
		 
    </TR>
    
       <TR>
          <TH ALIGN="left" >&nbsp;Ref No : </TH>
          <TD><INPUT name="JOBNO" type = "TEXT" value="<%=JOBNO%>" size="20"  MAXLENGTH=20></TD>
          <TH ALIGN="left">Product ID : </TH>
          <TD><INPUT name="ITEM" type = "TEXT" value="<%=ITEMNO%>" size="20"  MAXLENGTH=50>
         <!-- <a href="#" onClick="javascript:popUpWin('item_list_inventory.jsp?ITEM='+form1.ITEM.value);">
            <img src="images/populate.gif" border="0"/>
          </a>   -->      
           </TD>
    </TR>
	
    <TR>
        <TH ALIGN="left" >&nbsp; Order No : </TH>
          <TD><INPUT name="ORDERNO" type = "TEXT" value="<%=ORDERNO%>" size="20"  MAXLENGTH=20></TD>
          <TH ALIGN="left" >&nbsp;Product Description : </TH>
          <TD><INPUT name="DESC" type = "TEXT" value="<%=_strUtils.forHTMLTag(DESC)%>" size="20"  MAXLENGTH=100></TD>
    </TR>
    
     <TR>
           <TH ALIGN="left" >&nbsp;Pick Status : </TH>
          <TD><SELECT NAME ="PICKSTATUS" size="1">
            <OPTION selected  value=""> </OPTION>
     		<OPTION   value='O' <%if(PICKSTATUS.equalsIgnoreCase("O")){ %>selected<%} %>>O </OPTION>
     		<OPTION   value='C' <%if(PICKSTATUS.equalsIgnoreCase("C")){ %>selected<%} %>>C </OPTION>
     		<OPTION   value='N' <%if(PICKSTATUS.equalsIgnoreCase("N")){ %>selected<%} %>>N </OPTION>
                           </SELECT></TD>
                            <TH ALIGN="left"> Customer Name : </TH>
          <TD><INPUT name="CUSTOMER" type = "TEXT" value="<%=CUSTOMER%>" size="20"  MAXLENGTH=20></TD>
                
             </TR>
	<TR>
           <TH ALIGN="left" >&nbsp;Issue Status : </TH>
          <TD><SELECT NAME ="ISSUESTATUS" size="1">
            <OPTION selected  value=""> </OPTION>
     		<OPTION   value='O' <%if(ISSUESTATUS.equalsIgnoreCase("O")){ %>selected<%} %>>O </OPTION>
     		<OPTION   value='C' <%if(ISSUESTATUS.equalsIgnoreCase("C")){ %>selected<%} %>>C </OPTION>
     		<OPTION   value='N' <%if(ISSUESTATUS.equalsIgnoreCase("N")){ %>selected<%} %>>N </OPTION>
          </SELECT></TD>
           <TH ALIGN="left" >Order Type : </TH>
            <TD><INPUT name="ORDERTYPE" type = "TEXT" value="MOBILE ORDER" size="20"  MAXLENGTH=20 readonly> <!--<a href="#" onClick="javascript:popUpWin('displayOrderType.jsp?OTYPE=OUTBOUND&ORDERTYPE='+form1.ORDERTYPE.value+'&COMPANY='+form1.COMPANY.value);">
           <img src="images/populate.gif" border="0"/>
          </a>--></TD>          
           <TD ALIGN="left" ><input type="button" value="View"  onClick="javascript:return onGo();"></TD>
    </TR>
   
    <TR>&nbsp;
       
    </TR>
	
  </TABLE>
  <br>
 
    
     <TABLE WIDTH="90%"  border="0" cellspacing="1" cellpadding = 2 align = "center">
    <TR BGCOLOR="#000066">
<TH><font color="#ffffff" align="center">S/N</TH>
         <TH><font color="#ffffff" align="left"><b>Order No</TH>
         <TH><font color="#ffffff" align="left"><b>Order Type</TH>
         <TH><font color="#ffffff" align="left"><b>Ref No</TH>
         <TH><font color="#ffffff" align="left"><b>Customer Name</TH>
         <TH><font color="#ffffff" align="left"><b>Product ID</TH>
         <TH><font color="#ffffff" align="left"><b>Order Date & time</TH>
         <TH><font color="#ffffff" align="left"><b>Order Qty</TH>
         <TH><font color="#ffffff" align="left"><b>Pick Qty</TH>
         <TH><font color="#ffffff" align="left"><b>Issue Qty</TH>
          <TH><font color="#ffffff" align="left"><b>Unit Price</TH>
          <TH><font color="#ffffff" align="left"><b>Total</TH>
       </tr>
        <%
	       if(movQryList.size()<=0 || cntRec=="true" ){ %>
		  <TR><TD colspan="8" align=center>No Data For This criteria</TD></TR>
		  <%
		  } else { %>

		  <%
		  int iIndex = 0;
                   int irow = 0;
		  double totalPrice = 0;String lastCust="";
                   for (int iCnt =0; iCnt<movQryList.size(); iCnt++){
		      Map lineArr = (Map) movQryList.get(iCnt);
                      k=k+1;
	         
	          
	          
	          unitprice = Float.parseFloat((String)lineArr.get("unitprice"));
	          unitprice = StrUtils.Round(unitprice,2);
	          
	          subtotal = Float.parseFloat((String)lineArr.get("total"));
	        // subtotal = StrUtils.Round(subtotal,2);
	          totalAmountToBill =totalAmountToBill+Float.parseFloat(((String)lineArr.get("total").toString()));
	        
	       		
	          totalPrice=totalPrice+Float.parseFloat(((String)lineArr.get("total").toString()));
	         // totalPrice = StrUtils.Round(totalPrice,2);
                    
                  
       	        
                String custCode =(String)lineArr.get("custcode");
                
          String bgcolor = ((irow == 0) || (irow % 2 == 0)) ? "#FFFFFF"  : "#dddddd";
        
          if((lastCust.equalsIgnoreCase("") || lastCust.equalsIgnoreCase(custCode))){
          
       %>
       
        <TR bgcolor = "<%=bgcolor%>">
        <TD align="center"><%=k%></TD>
           <TD><%=(String)lineArr.get("dono")%></TD>
            <TD align= "left">&nbsp;&nbsp;&nbsp;<%=(String)lineArr.get("ordertype")%></TD>
            <TD align= "left">&nbsp;&nbsp;&nbsp;<%=(String)lineArr.get("jobnum")%></TD>
            <TD align= "left">&nbsp;&nbsp;&nbsp;<%=(String)lineArr.get("custcode")%></TD>
            <TD align= "left">&nbsp;&nbsp;&nbsp;<%=(String)lineArr.get("item") %></TD>
            <TD align= "left">&nbsp;&nbsp;&nbsp;<%=(String)lineArr.get("orddate") %></TD>
            <TD align= "right">&nbsp;&nbsp;&nbsp;<%=StrUtils.formatNum((String)lineArr.get("qtyor")) %></TD>
            <TD align= "right">&nbsp;&nbsp;&nbsp;<%=StrUtils.formatNum((String)lineArr.get("qtypick")) %></TD>
            <TD align= "right">&nbsp;&nbsp;&nbsp;<%=StrUtils.formatNum((String)lineArr.get("qty")) %></TD>
            <TD align="center" class="textbold">&nbsp; <%=DbBean.CURRENCYSYMBOL%><%=decformat.format(unitprice)%></TD>
            <TD align="center" class="textbold">&nbsp; <%=DbBean.CURRENCYSYMBOL%><%=decformat.format(subtotal)%></TD>
         </TR>
         
        <%    
        if(iIndex+1 == movQryList.size()){
        irow=irow+1;
        bgcolor = ((irow == 0) || (irow % 2 == 0)) ? "#FFFFFF"  : "#dddddd";  
     
        %>
       
        <TR bgcolor ="<%=bgcolor%>" >
         <TD></TD> <TD></TD> <TD></TD><TD></TD>  <TD></TD><TD></TD> <TD></TD> <TD></TD><TD></TD><TD></TD> <TD align= "Center"><b>Total:</b></td> <TD align="center" class="textbold">&nbsp; <%=DbBean.CURRENCYSYMBOL%><%=decformat.format(totalPrice)%></TD>
        </TR>
       <%} }else{
            System.out.println(" Total - previous Total" + totalPrice + "total to deduct:::"+(String)lineArr.get("total"));
             totalPrice = totalPrice - Float.parseFloat((String)lineArr.get("total"));
            // totalPrice = StrUtils.Round(totalPrice,2);
       %>
       <TR bgcolor ="<%=bgcolor%>" >
      
              <TD></TD> <TD></TD> <TD></TD><TD></TD>  <TD></TD><TD></TD> <TD></TD> <TD></TD><TD></TD> <TD></TD><TD align= "Center"><b>Total:</b></td> <TD align="center" class="textbold">&nbsp; <%=DbBean.CURRENCYSYMBOL%><%=decformat.format(totalPrice)%></TD>
            </TR>
            <%
                irow=irow+1;
                bgcolor = ((irow == 0) || (irow % 2 == 0)) ? "#FFFFFF"  : "#dddddd"; 
           
            %>
                <TR bgcolor = "<%=bgcolor%>">
                  <TD align="center"><%=k%></TD>
             <TD><%=(String)lineArr.get("dono")%></TD>
            <TD align= "left">&nbsp;&nbsp;&nbsp;<%=(String)lineArr.get("ordertype")%></TD>
            <TD align= "left">&nbsp;&nbsp;&nbsp;<%=(String)lineArr.get("jobnum")%></TD>
            <TD align= "left">&nbsp;&nbsp;&nbsp;<%=(String)lineArr.get("custcode")%></TD>
            <TD align= "left">&nbsp;&nbsp;&nbsp;<%=(String)lineArr.get("item") %></TD>
            <TD align= "left">&nbsp;&nbsp;&nbsp;<%=(String)lineArr.get("orddate") %></TD>
            <TD align= "right">&nbsp;&nbsp;&nbsp;<%=StrUtils.formatNum((String)lineArr.get("qtyor")) %></TD>
            <TD align= "right">&nbsp;&nbsp;&nbsp;<%=StrUtils.formatNum((String)lineArr.get("qtypick")) %></TD>
            <TD align= "right">&nbsp;&nbsp;&nbsp;<%=StrUtils.formatNum((String)lineArr.get("qty")) %></TD>
            <TD align="center" class="textbold">&nbsp; <%=DbBean.CURRENCYSYMBOL%><%=decformat.format(unitprice)%></TD>
            <TD align="center" class="textbold">&nbsp; <%=DbBean.CURRENCYSYMBOL%><%=decformat.format(subtotal)%></TD>
                </TR>
         
           <% 
             
               totalPrice =  Float.parseFloat((String)lineArr.get("total"));
               //totalPrice = StrUtils.Round(totalPrice,2);
            if(iIndex+1 == movQryList.size()){ 
                irow=irow+1;
                bgcolor = ((irow == 0) || (irow % 2 == 0)) ? "#FFFFFF"  : "#dddddd"; 
        
            %>
            <TR bgcolor ="<%=bgcolor%>" >
              <TD></TD> <TD></TD> <TD></TD><TD></TD> <TD></TD><TD></TD> <TD></TD> <TD></TD><TD></TD><TD></TD> <TD align= "Center"><b>Total:</b></td> <TD align="center" class="textbold">&nbsp; <%=DbBean.CURRENCYSYMBOL%><%=decformat.format(totalPrice)%></TD>
            </TR>
            <%}
        }
             irow=irow+1;
             iIndex=iIndex+1;
             lastCust = custCode;
               
          }
       	 
		}%>
       
       <%if(chargeBy.equalsIgnoreCase("PERCENTAGE")){%>
  <TR> <TD></TD><TD></TD><TD></TD><TD ></TD><TD ></TD><TD></TD><TD><b>Total Sales : </b></TD><TD><%=decformat.format(totalAmountToBill)%></TD><TD></TD><TD></TD><TD></TD><TD></TD> <TD></TD> </TR>
  <%}else if ( chargeBy.equalsIgnoreCase("FLATRATE")){%>  
  <TR> <TD></TD><TD></TD><TD></TD><TD ></TD><TD ></TD><TD></TD><TD><b>Total Orders : </b></TD><TD><%=noOfOrders%></TD><TD></TD><TD></TD><TD></TD><TD></TD> <TD></TD> </TR>
  <%}%>
     <%
       if (chargeBy.equalsIgnoreCase("PERCENTAGE")){
                  totalAmountToBill = (totalAmountToBill/100)*Double.parseDouble(salesRate);
                  salesRate=salesRate+"%";
                  }else if (chargeBy.equalsIgnoreCase("FLATRATE")){
                  totalAmountToBill = Integer.parseInt(noOfOrders)*Double.parseDouble(salesRate);
                  salesRate="$"+salesRate;
                  }else{
                  totalAmountToBill=0;
                  }
     
     %>
     
    </TABLE>
      <br>
    <table align="center" >
     <TR>
   <td>  <input type="button" value=" Back " onClick="window.location.href='../home'">&nbsp; </td>
   <td>   <input type="button" value="ExportReport" onClick="javascript:ExportReport();" > </td>
   <INPUT type="Hidden" name="DIRTYPE" value="MOBILE ORDER">
     </TR>
    </table>
  </FORM>
     <Script>
  var rate = '<%=salesRate%>';
    var totalbill = '<%=decformat.format(totalAmountToBill)%>';
    var plnt = '<%=plant%>'

    document.form1.SALESRATE.value='<%=salesRate%>';
    document.form1.AMOUNT_TO_BILL.value=totalbill;
  
  </Script>
<%@ include file="footer.jsp"%>
