<%@ page import="com.track.gates.DbBean"%>
<%@ page import="com.track.gates.Generator"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.util.*"%>
<%@ page import="com.track.gates.*"%>
<%@ page import="java.util.*"%>
<%@ include file="header.jsp"%>
<html>
<head>
<% String plant = (String)session.getAttribute("PLANT");%>
<script language="javascript">
var subWin = null;
function popUpWin(URL) {
  subWin = window.open(URL, 'ProductList', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=500,height=200,left = 200,top = 184');
}

function popUpfullWin(URL) {
 // subWin = window.open(URL, 'maintain', 'toolbar=1,scrollbars=yes,location=1,statusbar=1,menubar=1,dependant=1,resizable=1,width='+screen.width+',height='+screen.height+',left = 0,top = 0');
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
   
   if(FROM_DATE != null     && FROM_DATE != "") { flag = true;}
   if(TO_DATE != null    && TO_DATE != "") { flag = true;}
   if(DIRTYPE != null     && DIRTYPE != "") { flag = true;}

   if(USER != null    && USER != "") { flag = true;}
   if(ITEMNO != null     && ITEMNO != "") { flag = true;}
   
    if(ORDERNO != null     && ORDERNO != "") { flag = true;}
    if(JOBNO != null     && JOBNO != "") { flag = true;}

   //if(flag == "false"){ alert("Please define any one search criteria"); return false;}

  document.form1.action="mobileEnquirySummary.jsp";
  document.form1.submit();
}

</script>

<script language="JavaScript" type="text/javascript" src="js/calendar.js"></script>
<title>Mobile Enquiry Order Details</title>
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


String FROM_DATE ="",  TO_DATE = "", PICKSTATUS="",ORDERTYPE="",HPNO="",ISSUESTATUS="",DIRTYPE ="",BATCH ="",USER="",ITEM="",DESC="",fdate="",tdate="",JOBNO="",ITEMNO="",ORDERNO="",CUSTOMER="",PGaction="";
String CUSTOMERID="";

PGaction         = _strUtils.fString(request.getParameter("PGaction")).trim();
String COMPANY="",html = "",cntRec ="false";

FROM_DATE     = _strUtils.fString(request.getParameter("FROM_DATE"));
TO_DATE   = _strUtils.fString(request.getParameter("TO_DATE"));
ORDERTYPE   = _strUtils.fString(request.getParameter("ORDERTYPE"));


COMPANY=plant;



session.setAttribute("RFLAG","8");
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
HPNO          = _strUtils.fString(request.getParameter("HPNO"));
CUSTOMERID    = _strUtils.fString(request.getParameter("CUSTOMERID"));
PICKSTATUS    = _strUtils.fString(request.getParameter("PICKSTATUS"));
ISSUESTATUS   = _strUtils.fString(request.getParameter("ISSUESTATUS"));
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
         if(_strUtils.fString(HPNO).length() > 0)            ht.put("A.CONTACTNUM",HPNO);
        
       movQryList = movHisUtil.getMobileEnquirySummaryList(ht,fdate,tdate,DIRTYPE,COMPANY,DESC);
        
		if(movQryList.size()<=0)
			cntRec ="true";



 }catch(Exception e) { }
}
%>
<%@ include file="body.jsp"%>
<FORM name="form1" method="post" action="mobileEnquirySummary.jsp" >
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
          <TH ALIGN="left"> Customer Name : </TH>
          <TD><INPUT name="CUSTOMER" type = "TEXT" value="<%=CUSTOMER%>" size="20"  MAXLENGTH=20></TD>
          
          <TH ALIGN="left"> Handphone : </TH>
          <TD><INPUT name="HPNO" type = "TEXT" value="<%=HPNO%>" size="20"  MAXLENGTH=20></TD>      
           
    </TR>
    
     <TR>
        
            <TH ALIGN="left" >&nbsp;Status : </TH>
          <TD><SELECT NAME ="ISSUESTATUS" size="1">
            <OPTION selected  value=""> </OPTION>
     		<OPTION   value='O' <%if(ISSUESTATUS.equalsIgnoreCase("O")){ %>selected<%} %>>O </OPTION>
     		<OPTION   value='C' <%if(ISSUESTATUS.equalsIgnoreCase("C")){ %>selected<%} %>>C </OPTION>
     		<OPTION   value='N' <%if(ISSUESTATUS.equalsIgnoreCase("N")){ %>selected<%} %>>N </OPTION>
          </SELECT></TD>
      
           <TH ALIGN="left" >Order Type : </TH>
           <TD><INPUT name="ORDERTYPE" type = "TEXT" value="MOBILE ENQUIRY" size="20"  MAXLENGTH=20></TD>  
           
           <TD ALIGN="left" ><input type="button" value="View"  onClick="javascript:return onGo();"></TD>
    </TR>
   
    <TR>&nbsp;
  
  
        

		
		          
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
                <!--<TH><font color="#ffffff" align="left"><b>Unit Price</TH>-->
                <TH><font color="#ffffff" align="left"><b>Order Date & Time</TH>
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
           
          
               
       %>

           <TR bgcolor = "<%=bgcolor%>">
            <TD align="center"><%=iIndex%></TD>
           
         
      <!--  <TD><a href="/track/deleveryorderservlet?DONO=<%=(String)lineArr.get("dono")%>&Submit=View&RFLAG=8&COMPANY=<%=COMPANY%>"><%=(String)lineArr.get("dono")%></a></TD>-->
      
      <TD><a href="javascript:popUpfullWin('/track/deleveryorderservlet?DONO=<%=(String)lineArr.get("dono")%>&Submit=View&RFLAG=8&COMPANY=<%=COMPANY%>');" ><%=(String)lineArr.get("dono")%></a></TD>
      
        
     
           <TD align= "left">&nbsp;&nbsp;&nbsp;<%=(String)lineArr.get("ordertype")%></TD>
            <TD align= "left">&nbsp;&nbsp;&nbsp;<%=(String)lineArr.get("jobNum")%></TD>
            
            <TD align= "left">&nbsp;&nbsp;&nbsp;<%=(String)lineArr.get("custname")%></TD>
            <TD align= "left">&nbsp;&nbsp;&nbsp;<%=(String)lineArr.get("item") %></TD>
               <TD align= "left">&nbsp;&nbsp;&nbsp;<%=(String)lineArr.get("itemdesc") %></TD>
            <!-- <TD align= "right">&nbsp;&nbsp;&nbsp;<%=StrUtils.currencyWtoutSymbol((String)lineArr.get("unitprice")) %></TD>-->
            <TD align= "left">&nbsp;&nbsp;&nbsp;<%=(String)lineArr.get("TRANDATE") %>&nbsp;<%=(String)lineArr.get("TIME") %></TD>
          <!--  <TD align= "right">&nbsp;&nbsp;&nbsp;<%=StrUtils.formatNum((String)lineArr.get("qtyor")) %></TD>
            <TD align= "right">&nbsp;&nbsp;&nbsp;<%=StrUtils.formatNum((String)lineArr.get("qtyPick")) %></TD>
            <TD align= "right">&nbsp;&nbsp;&nbsp;<%=StrUtils.formatNum((String)lineArr.get("qty")) %></TD>
            <TD align= "left">&nbsp;&nbsp;&nbsp;<%=(String)lineArr.get("pickstatus") %></TD> -->
	    <TD align= "left">&nbsp;&nbsp;&nbsp;<%=(String)lineArr.get("lnstat") %></TD> 
		       
           </TR>
           
       <%}%>

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
  
<%@ include file="footer.jsp"%>
