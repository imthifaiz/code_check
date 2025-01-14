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

function popUpfullWin(URL) {
   subWin = window.open(URL, 'maintain', 'toolbar=1,scrollbars=yes,location=1,statusbar=1,menubar=1,dependant=1,resizable=1,width=800,height=800,left = 200,top = 184');
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
    var HPNO          = document.form1.HPNO.value;
  
   
   if(FROM_DATE != null     && FROM_DATE != "") { flag = true;}
   if(TO_DATE != null    && TO_DATE != "") { flag = true;}
   if(DIRTYPE != null     && DIRTYPE != "") { flag = true;}

   if(USER != null    && USER != "") { flag = true;}
   if(ITEMNO != null     && ITEMNO != "") { flag = true;}
   
    if(ORDERNO != null     && ORDERNO != "") { flag = true;}
    if(JOBNO != null     && JOBNO != "") { flag = true;}
     if(HPNO != null     && HPNO != "") { flag = true;}

   //if(flag == "false"){ alert("Please define any one search criteria"); return false;}

  document.form1.action="mobileShoppingSummary.jsp";
  document.form1.submit();
}

</script>

<script language="JavaScript" type="text/javascript" src="js/calendar.js"></script>
<title>Mobile Shopping Order Details</title>
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


String FROM_DATE ="",  TO_DATE = "", PICKSTATUS="",ORDERTYPE="",HPNO="",ISSUESTATUS="",MEMBER_STATUS="",DIRTYPE ="",BATCH ="",USER="",ITEM="",DESC="",fdate="",tdate="",JOBNO="",ITEMNO="",ORDERNO="",CUSTOMER="",PGaction="";
String CUSTOMERID="";
PGaction         = _strUtils.fString(request.getParameter("PGaction")).trim();
String html = "",cntRec ="false";
String COMPANY="";
float Total=0,subtotal=0,unitprice=0;

      double totalAmountToBill=0;
DecimalFormat decformat = new DecimalFormat("#,##0.00");

FROM_DATE     = _strUtils.fString(request.getParameter("FROM_DATE"));
TO_DATE   = _strUtils.fString(request.getParameter("TO_DATE"));
ORDERTYPE   = _strUtils.fString(request.getParameter("ORDERTYPE"));
if(FROM_DATE==null) FROM_DATE=""; else FROM_DATE = FROM_DATE.trim();
String curDate =_dateUtils.getDate();
String deliverydateandtime="";
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
MEMBER_STATUS = _strUtils.fString(request.getParameter("MEMBER_STATUS"));
HPNO =  _strUtils.fString(request.getParameter("HPNO"));



COMPANY=plant;


session.setAttribute("RFLAG","6");
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
                  if(_strUtils.fString(MEMBER_STATUS).length() > 0)   ht.put("B.ISMEMBER",MEMBER_STATUS);
                    if(_strUtils.fString(HPNO).length() > 0)   ht.put("A.CONTACTNUM",HPNO);
                  
            
                  movQryList = movHisUtil.getMobileShoppingSummary(ht,fdate,tdate,DIRTYPE,COMPANY, DESC,"");
                 
	
	 }catch(Exception e) { }
	}
%>

<%@ include file="body.jsp"%>
<FORM name="form1" method="post" action="mobileShoppingSummary.jsp" >
<input type="hidden" name="xlAction" value="">
<input type="hidden" name="PGaction" value="View">
  <br>
  <TABLE border="0" width="100%" cellspacing="0" cellpadding="0" align="center" bgcolor="#dddddd">
    <TR>
      <TH BGCOLOR="#000066" COLSPAN="20"><font color="white">Mobile Shopping Order Details</font></TH>
    </TR>
  </TABLE>
  <br>
  <TABLE border="0" width="80%" height = "25%" cellspacing="0" cellpadding="0" align="center" bgcolor="#dddddd">
  
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
            <TH ALIGN="left" >Handphone : </TH>
            <TD><INPUT name="HPNO" type = "TEXT" value="<%=HPNO%>" size="20"  MAXLENGTH=20 > </TD>   
      
         
    </TR>
    
    <TR>
      <TH ALIGN="left" >Order Type : </TH>
            <TD><INPUT name="ORDERTYPE" type = "TEXT" value="MOBILE ORDER" size="20"  MAXLENGTH=20 readonly> </TD>   
           <TH ALIGN="left" >&nbsp;Membership Status : </TH>
          <TD><SELECT NAME ="MEMBER_STATUS" size="1">
            <OPTION selected  value=""> </OPTION>
     		<OPTION   value='Y' <%if(MEMBER_STATUS.equalsIgnoreCase("Y")){ %>selected<%} %>>Member </OPTION>
     		<OPTION   value='N' <%if(MEMBER_STATUS.equalsIgnoreCase("N")){ %>selected<%} %>>Non Memeber </OPTION>
          </SELECT></TD>
                    
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
         <TH><font color="#ffffff" align="left"><b>Is Member</TH>
         <TH><font color="#ffffff" align="left"><b>Product ID</TH>
         
         <TH><font color="#ffffff" align="left"><b>Order Date & time</TH>
         <TH><font color="#ffffff" align="left"><b>Deliver Date & time</TH>
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
	          deliverydateandtime=(String)lineArr.get("deliverydate")+ " " +(String)lineArr.get("deliverytime");
	         // subtotal = StrUtils.Round(subtotal,2);
	          
	        
	       		
	          totalPrice=totalPrice+Float.parseFloat(((String)lineArr.get("total").toString()));
	        //  totalPrice = StrUtils.Round(totalPrice,2);
                    
                  
       	        
                String custCode =(String)lineArr.get("custcode");
                
          String bgcolor = ((irow == 0) || (irow % 2 == 0)) ? "#FFFFFF"  : "#dddddd";
        
          if((lastCust.equalsIgnoreCase("") || lastCust.equalsIgnoreCase(custCode))){
          
       %>
       
        <TR bgcolor = "<%=bgcolor%>">
        <TD align="center"><%=k%></TD>
            <TD><a href="javascript:popUpfullWin('/track/deleveryorderservlet?DONO=<%=(String)lineArr.get("dono")%>&Submit=View&RFLAG=6');" ><%=(String)lineArr.get("dono")%></a></TD>
            <TD align= "left">&nbsp;&nbsp;&nbsp;<%=(String)lineArr.get("ordertype")%></TD>
            <TD align= "left">&nbsp;&nbsp;&nbsp;<%=(String)lineArr.get("jobnum")%></TD>
            <TD align= "left">&nbsp;&nbsp;&nbsp;<%=(String)lineArr.get("custname")%></TD>
            <TD align= "left">&nbsp;&nbsp;&nbsp;<%=(String)lineArr.get("ISMEMBER")%></TD>
            <TD align= "left">&nbsp;&nbsp;&nbsp;<%=(String)lineArr.get("item") %></TD>
            <TD align= "left">&nbsp;&nbsp;&nbsp;<%=(String)lineArr.get("orddate") %></TD>
            <TD align= "left">&nbsp;&nbsp;&nbsp;<%=deliverydateandtime%></TD>
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
         <TD></TD>
         <TD></TD>
         <TD></TD>
         <TD></TD>
         <TD></TD>
         <TD></TD>
         <TD></TD>
         <TD></TD>
         <TD></TD>
         <TD></TD>
         <TD></TD> 
         <TD></TD>
         <TD align= "Center"><b>Total:</b></td> 
         <TD align="center" class="textbold">&nbsp; <%=DbBean.CURRENCYSYMBOL%><%=decformat.format(totalPrice)%></TD>
        </TR>
       <%} }else{
            System.out.println(" Total - previous Total" + totalPrice + "total to deduct:::"+(String)lineArr.get("total"));
             totalPrice = totalPrice - Float.parseFloat((String)lineArr.get("total"));
            // totalPrice = StrUtils.Round(totalPrice,2);
       %>
       <TR bgcolor ="<%=bgcolor%>" >
      
              <TD></TD> 
              <TD></TD> 
              <TD></TD>
              <TD></TD> 
              <TD></TD>
               <TD></TD>
               <TD></TD> 
               <TD></TD> 
               <TD></TD>
               <TD></TD> 
               <TD></TD>
               <TD></TD>
               <TD align= "Center"><b>Total:</b></td> 
               <TD align="center" class="textbold">&nbsp; <%=DbBean.CURRENCYSYMBOL%><%=decformat.format(totalPrice)%></TD>
            </TR>
            <%
                irow=irow+1;
                bgcolor = ((irow == 0) || (irow % 2 == 0)) ? "#FFFFFF"  : "#dddddd"; 
           
            %>
                <TR bgcolor = "<%=bgcolor%>">
                  <TD align="center"><%=k%></TD>
               <TD><a href="javascript:popUpfullWin('/track/deleveryorderservlet?DONO=<%=(String)lineArr.get("dono")%>&Submit=View&RFLAG=6');" ><%=(String)lineArr.get("dono")%></a></TD>
            <TD align= "left">&nbsp;&nbsp;&nbsp;<%=(String)lineArr.get("ordertype")%></TD>
            <TD align= "left">&nbsp;&nbsp;&nbsp;<%=(String)lineArr.get("jobnum")%></TD>
            <TD align= "left">&nbsp;&nbsp;&nbsp;<%=(String)lineArr.get("custname")%></TD>
            <TD align= "left">&nbsp;&nbsp;&nbsp;<%=(String)lineArr.get("ISMEMBER")%></TD>
            <TD align= "left">&nbsp;&nbsp;&nbsp;<%=(String)lineArr.get("item") %></TD>
            <TD align= "left">&nbsp;&nbsp;&nbsp;<%=(String)lineArr.get("orddate") %></TD>
            <TD align= "left">&nbsp;&nbsp;&nbsp;<%=deliverydateandtime%></TD>
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
              <TD></TD> 
              <TD></TD> 
              <TD></TD>
              <TD></TD> 
              <TD></TD> 
              <TD></TD>
              <TD></TD> 
              <TD></TD> 
              <TD></TD>
              <TD></TD>
              <TD></TD>
              <TD></TD>
              <TD align= "Center"><b>Total:</b></td> 
               <TD align="center" class="textbold">&nbsp; <%=DbBean.CURRENCYSYMBOL%><%=decformat.format(totalPrice)%></TD>
            </TR>
            <%}
        }
             irow=irow+1;
             iIndex=iIndex+1;
             lastCust = custCode;
             
          }
       	 
		}%>
       
      
     
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
   
<%@ include file="footer.jsp"%>
