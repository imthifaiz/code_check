<%@ page import="com.track.gates.DbBean"%>
<%@ page import="com.track.gates.Generator"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.util.*"%>
<%@ page import="com.track.gates.*"%>
<%@ page import="java.util.*"%>
<%@page import="java.text.DecimalFormat"%>
<%@ include file="header.jsp"%>
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

  document.form1.action="billingEnquiry.jsp";
  document.form1.submit();
}

</script>

<script language="JavaScript" type="text/javascript" src="js/calendar.js"></script>
<title>Mobile Enquiry Summary</title>
</head>
<link rel="stylesheet" href="css/style.css">
<%

StrUtils _strUtils     = new StrUtils();
Generator generator   = new Generator();
HTReportUtil movHisUtil       = new HTReportUtil();
movHisUtil.setmLogger(mLogger);
DateUtils _dateUtils = new DateUtils();
ArrayList movQryList  = new ArrayList();


session= request.getSession();

DecimalFormat decformat = new DecimalFormat("#,##0.00");
String FROM_DATE ="",  TO_DATE = "", PICKSTATUS="",ORDERTYPE="",ISSUESTATUS="",DIRTYPE ="",BATCH ="",USER="",ITEM="",DESC="",fdate="",tdate="",JOBNO="",ITEMNO="",ORDERNO="",CUSTOMER="",PGaction="";
String CUSTOMERID="";
 String NoofEnquires="",EnquiryRate=""; double totalBill=0;
PGaction         = _strUtils.fString(request.getParameter("PGaction")).trim();
String COMPANY="",SALESPERCNT="",ENQUIRYRATE="",AMOUNT_TO_BILL="",html = "",cntRec ="false";

FROM_DATE     = _strUtils.fString(request.getParameter("FROM_DATE"));
TO_DATE   = _strUtils.fString(request.getParameter("TO_DATE"));
ORDERTYPE   = _strUtils.fString(request.getParameter("ORDERTYPE"));

if(plant.equalsIgnoreCase("track")){
  COMPANY   = _strUtils.fString(request.getParameter("COMPANY"));
  SALESPERCNT=_strUtils.fString(request.getParameter("SALESPERCNT"));
  ENQUIRYRATE=_strUtils.fString(request.getParameter("ENQUIRYRATE"));
  AMOUNT_TO_BILL = _strUtils.fString(request.getParameter("AMOUNT_TO_BILL"));
}else{
COMPANY=plant;
ENQUIRYRATE = new PlantMstUtil().getEnquiryFlatrate(COMPANY);

}



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
if(DIRTYPE.length()<=0){
DIRTYPE = "MOBILE_ENQUIRY";
}

if(PGaction.equalsIgnoreCase("View")){
 
 try{
        Hashtable ht = new Hashtable();
         
        if(_strUtils.fString(JOBNO).length() > 0)            ht.put("A.JOBNUM",JOBNO);
        if(_strUtils.fString(ITEMNO).length() > 0)           ht.put("B.ITEM",ITEMNO);
        if(_strUtils.fString(ORDERNO).length() > 0)          ht.put("B.DONO",ORDERNO);
        if(_strUtils.fString(CUSTOMER).length() > 0)         ht.put("A.CUSTNAME",CUSTOMER);
        if(_strUtils.fString(CUSTOMERID).length() > 0)       ht.put("A.CUSTCODE",CUSTOMERID);
        if(_strUtils.fString(ORDERTYPE).length() > 0)        ht.put("A.ORDERTYPE",ORDERTYPE);
        if(_strUtils.fString(ISSUESTATUS).length() > 0)      ht.put("B.LNSTAT",ISSUESTATUS);
        if(_strUtils.fString(PICKSTATUS).length() > 0)       ht.put("B.PICKSTATUS",PICKSTATUS);
        
       movQryList = movHisUtil.getMobileEnquirySummaryList(ht,fdate,tdate,DIRTYPE,COMPANY,DESC);
        
		if(movQryList.size()<=0)
			cntRec ="true";



 }catch(Exception e) { }
}
%>
<%@ include file="body.jsp"%>
<FORM name="form1" method="post" action="billingEnquiry.jsp" >
<input type="hidden" name="xlAction" value="">
<input type="hidden" name="PGaction" value="View">
  <br>
  <TABLE border="0" width="100%" cellspacing="0" cellpadding="0" align="center" bgcolor="#dddddd">
    <TR>
      <TH BGCOLOR="#000066" COLSPAN="20"><font color="white">Mobile Enquiry Summary </font></TH>
    </TR>
  </TABLE>
  <br>
  <TABLE border="0" width="80%" height = "25%" cellspacing="0" cellpadding="0" align="center" bgcolor="#dddddd">
 
  <TR>
      <TH ALIGN="left" >Company</TH>
        <TD >
          <INPUT name="COMPANY" type="TEXT" value="<%=COMPANY%>" size="20" MAXLENGTH="20" class = inactivegry readonly/>
           <%if(plant.equalsIgnoreCase("track")){%>
          <a href="#" onClick="javascript:popUpWin('plant_list_for_billing.jsp?TYPE=MOBILE ENQUIRY&COMPANY='+form1.COMPANY.value);">
            <img src="images/populate.gif" border="0"/>
          </a>
          <%}%>
        </TD>
    <TH ALIGN="left">Enquires (Flat rate):  </TH>
          <TD ><INPUT name="ENQUIRYRATE" type = "TEXT" value="<%=ENQUIRYRATE%>" size="15"  MAXLENGTH=20 class = inactivegry readonly>
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
        <TH ALIGN="left" >&nbsp;Order No : </TH>
          <TD><INPUT name="ORDERNO" type = "TEXT" value="<%=ORDERNO%>" size="20"  MAXLENGTH=20></TD>
          <TH ALIGN="left" >&nbsp;Product Description : </TH>
          <TD><INPUT name="DESC" type = "TEXT" value="<%=_strUtils.forHTMLTag(DESC)%>" size="20"  MAXLENGTH=100></TD>
    </TR>
    
   <TR>
      <!--       <TH ALIGN="left" >&nbsp;Pick Status : </TH>
          <TD><SELECT NAME ="PICKSTATUS" size="1">
            <OPTION selected  value=""> </OPTION>
     		<OPTION   value='O' <%if(PICKSTATUS.equalsIgnoreCase("O")){ %>selected<%} %>>O </OPTION>
     		<OPTION   value='C' <%if(PICKSTATUS.equalsIgnoreCase("C")){ %>selected<%} %>>C </OPTION>
     		<OPTION   value='N' <%if(PICKSTATUS.equalsIgnoreCase("N")){ %>selected<%} %>>N </OPTION>
                           </SELECT></TD>-->
                            <TH ALIGN="left"> Customer Name : </TH>
          <TD><INPUT name="CUSTOMER" type = "TEXT" value="<%=CUSTOMER%>" size="20"  MAXLENGTH=20></TD>
                
       <!--      </TR>
	<TR>
           <TH ALIGN="left" >&nbsp;Issue Status : </TH>
          <TD><SELECT NAME ="ISSUESTATUS" size="1">
            <OPTION selected  value=""> </OPTION>
     		<OPTION   value='O' <%if(ISSUESTATUS.equalsIgnoreCase("O")){ %>selected<%} %>>O </OPTION>
     		<OPTION   value='C' <%if(ISSUESTATUS.equalsIgnoreCase("C")){ %>selected<%} %>>C </OPTION>
     		<OPTION   value='N' <%if(ISSUESTATUS.equalsIgnoreCase("N")){ %>selected<%} %>>N </OPTION>
                           </SELECT></TD>-->
           <TH ALIGN="left" >Order Type : </TH>
            <TD><INPUT name="ORDERTYPE" type = "TEXT" value="MOBILE ENQUIRY" size="20"  MAXLENGTH=20> <!--<a href="#" onClick="javascript:popUpWin('displayOrderType.jsp?OTYPE=OUTBOUND&ORDERTYPE='+form1.ORDERTYPE.value+'&COMPANY='+form1.COMPANY.value);">
           <img src="images/populate.gif" border="0"/>
          </a>--></TD>          
           <TD ALIGN="left" ><input type="button" value="View"  onClick="javascript:return onGo();"></TD>
    </TR>
   
    <TR>&nbsp;
  
  
 <!-- <TD><INPUT name="DIRTYPE" type = "TEXT" value="<%=DIRTYPE%>" size="20"  MAXLENGTH=20></TD> -->
   <!-- <TD><select name="DIRTYPE" size=1 >
   <OPTION selected value="<%=DIRTYPE%>"><%=DIRTYPE%> </OPTION>
        <option value="INBOUND"  <%=("INBOUND".equals("")?"selected":"")%>  >INBOUND</option>
        <option value="OUTBOUND"  <%=("OUTBOUND".equals("")?"selected":"")%>  >OUTBOUND</option>
    </select>
    </TD>-->
        

		
		          
    </TR>
	
  </TABLE>
  <br>
  <TABLE WIDTH="80%"  border="0" cellspacing="1" cellpadding = 2 align = "center">
    <TR BGCOLOR="#000066">

                <TH><font color="#ffffff" align="center">S/N</TH>
                <TH><font color="#ffffff" align="left"><b>Order No</TH>
                <TH><font color="#ffffff" align="left"><b>Order Type</TH>
                <TH><font color="#ffffff" align="left"><b>Ref No</TH>
                <TH><font color="#ffffff" align="left"><b>Customer Name</TH>
                <TH><font color="#ffffff" align="left"><b>Product ID</TH>
                <TH><font color="#ffffff" align="left"><b>Description</TH>
              <!--  <TH><font color="#ffffff" align="left"><b>Unit Price</TH>-->
                <TH><font color="#ffffff" align="left"><b>Order Date</TH>
              <!--  <TH><font color="#ffffff" align="left"><b>Order Qty</TH>
                <TH><font color="#ffffff" align="left"><b>Pick Qty</TH>
                <TH><font color="#ffffff" align="left"><b>Issue Qty</TH>
                <TH><font color="#ffffff" align="left"><b>Pick Status</TH>
                <TH><font color="#ffffff" align="left"><b>Issue Status</TH>-->
                <TH><font color="#ffffff" align="left"><b>Status</TH>
	     
       </tr>
       <%
	       if(movQryList.size()<=0 && cntRec=="true" ){ %>
		    <TR><TD colspan=15 align=center>No Data For This criteria</TD></TR>
		  <%}%>

		  <%
                 
          for (int iCnt =0; iCnt<movQryList.size(); iCnt++){
               Map lineArr = (Map) movQryList.get(iCnt);
               int iIndex = iCnt + 1;
               String bgcolor = ((iCnt == 0) || (iCnt % 2 == 0)) ? "#FFFFFF"  : "#dddddd";
                NoofEnquires=(String)lineArr.get("No_Enquires");
                EnquiryRate=(String)lineArr.get("EnquiryRate");
                totalBill =Double.parseDouble(NoofEnquires)* Double.parseDouble(EnquiryRate);
          
       %>

           <TR bgcolor = "<%=bgcolor%>">
            <TD align="center"><%=iIndex%></TD>
           
  
          <TD><%=(String)lineArr.get("dono")%></TD>
   
           <TD align= "left">&nbsp;&nbsp;&nbsp;<%=(String)lineArr.get("ordertype")%></TD>
            <TD align= "left">&nbsp;&nbsp;&nbsp;<%=(String)lineArr.get("jobNum")%></TD>
            
            <TD align= "left">&nbsp;&nbsp;&nbsp;<%=(String)lineArr.get("custname")%></TD>
            <TD align= "left">&nbsp;&nbsp;&nbsp;<%=(String)lineArr.get("item") %></TD>
               <TD align= "left">&nbsp;&nbsp;&nbsp;<%=(String)lineArr.get("itemdesc") %></TD>
           <!--  <TD align= "right">&nbsp;&nbsp;&nbsp;<%=StrUtils.currencyWtoutSymbol((String)lineArr.get("unitprice")) %></TD>-->
            <TD align= "left">&nbsp;&nbsp;&nbsp;<%=(String)lineArr.get("TRANDATE") %></TD>
          <!--  <TD align= "right">&nbsp;&nbsp;&nbsp;<%=StrUtils.formatNum((String)lineArr.get("qtyor")) %></TD>
            <TD align= "right">&nbsp;&nbsp;&nbsp;<%=StrUtils.formatNum((String)lineArr.get("qtyPick")) %></TD>
            <TD align= "right">&nbsp;&nbsp;&nbsp;<%=StrUtils.formatNum((String)lineArr.get("qty")) %></TD>
            <TD align= "left">&nbsp;&nbsp;&nbsp;<%=(String)lineArr.get("pickstatus") %></TD> -->
	    <TD align= "left">&nbsp;&nbsp;&nbsp;<%=(String)lineArr.get("lnstat") %></TD> 
		       
           </TR>
           
       <%}%>

  <TR> <TD></TD><TD></TD><TD></TD><TD ></TD><TD ></TD><TD></TD><TD><b>Total Enquires : </b></TD><TD><%=NoofEnquires%></TD><TD></TD><TD></TD><TD></TD><TD></TD> <TD></TD> </TR>

  </TABLE>
      <br>
    <table align="center" >
     <TR>
   <td>  <input type="button" value=" Back " onClick="window.location.href='../home'">&nbsp; </td>
   <td>   <input type="button" value="ExportReport" onClick="javascript:ExportReport();" > </td>
   <INPUT type="Hidden" name="DIRTYPE" value="MOBILE_ENQUIRY">
   </TR>
    </table>
  </FORM>
  <Script>
     var rate = '<%=EnquiryRate%>';
     var noofOrders = '<%=NoofEnquires%>';
     var plnt = '<%=plant%>'
     document.form1.AMOUNT_TO_BILL.value='<%=decformat.format(totalBill)%>';

  </Script>
<%@ include file="footer.jsp"%>
