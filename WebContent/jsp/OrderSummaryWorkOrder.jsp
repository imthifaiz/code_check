<%@ page import="com.track.gates.DbBean"%>
<%@ page import="com.track.gates.Generator"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.util.*"%>
<%@ page import="com.track.gates.*"%>
<%@ page import="java.util.*"%>
<%@ include file="header.jsp"%>
<html>
<head>

<script language="javascript">
var subWin = null;
function popUpWin(URL) {
  subWin = window.open(URL, 'ProductList', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=700,height=400,left = 200,top = 184');
}
//---Modified by Deen on June 12 2014, Description:To open work order summary  in excel powershell format
function ExportReport()
{
  var flag    = "false";
  var  DIRTYPE= document.form1.DIRTYPE.value;
  document.form1.FROM_DATE.value;
  document.form1.TO_DATE.value;
  document.form1.ITEM.value;
  document.form1.ORDERNO.value;
  document.form1.JOBNO.value;
  document.form1.DIRTYPE.value;
  document.form1.CUSTOMER.value;
  document.form1.action="/track/ReportServlet?action=ExportExcelWorkOrderSummary";
  document.form1.submit();
 }

//---End Modified by Deen on June 12 2014, Description:To open work order summary  in excel powershell format

function onGo(){

   var flag    = "false";
   var FROM_DATE      = document.form1.FROM_DATE.value;
   var TO_DATE        = document.form1.TO_DATE.value;
   var DIRTYPE        = document.form1.DIRTYPE.value;
   var USER           = document.form1.CUSTOMER.value;
   var ITEMNO         = document.form1.ITEM.value;
   var ORDERNO        = document.form1.ORDERNO.value;
   var JOBNO          = document.form1.JOBNO.value;
   var PRD_BRAND_ID      = document.form1.PRD_BRAND_ID.value;
   var PRD_TYPE_ID    = document.form1.PRD_TYPE_ID.value;
   var PRD_CLS_ID     = document.form1.PRD_CLS_ID.value;
     
   if(FROM_DATE != null     && FROM_DATE != "") { flag = true;}
   if(TO_DATE != null    && TO_DATE != "") { flag = true;}
   if(DIRTYPE != null     && DIRTYPE != "") { flag = true;}

   if(USER != null    && USER != "") { flag = true;}
   if(ITEMNO != null     && ITEMNO != "") { flag = true;}
   
    if(ORDERNO != null     && ORDERNO != "") { flag = true;}
    if(JOBNO != null     && JOBNO != "") { flag = true;}

    if(PRD_BRAND_ID != null     && PRD_BRAND_ID != "") { flag = true;}
    if(PRD_TYPE_ID != null     && PRD_TYPE_ID != "") { flag = true;}
    if(PRD_CLS_ID != null     && PRD_CLS_ID != "") { flag = true;}

  document.form1.action="OrderSummaryWorkOrder.jsp";
  document.form1.submit();
}

</script>

<script language="JavaScript" type="text/javascript" src="js/calendar.js"></script>
<title>Work Order Summary</title>
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


String FROM_DATE ="",  TO_DATE = "",WSTATUS="",LNSTATUS="",DIRTYPE ="",BATCH ="",
USER="",ITEM="",DESC="",fdate="",tdate="",JOBNO="",ITEMNO="",ORDERNO="",CUSTOMER="",PGaction="";
String CUSTOMERID="",PRD_BRAND_ID = "",PRD_TYPE_ID="",PRD_CLS_ID="";

PGaction         = _strUtils.fString(request.getParameter("PGaction")).trim();
String html      = "",cntRec ="false";
String plant     = (String)session.getAttribute("PLANT");
FROM_DATE        = _strUtils.fString(request.getParameter("FROM_DATE"));
TO_DATE          = _strUtils.fString(request.getParameter("TO_DATE"));

session.setAttribute("RFLAG","4");



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
CUSTOMERID    = _strUtils.fString(request.getParameter("CUSTOMERID"));
WSTATUS       = _strUtils.fString(request.getParameter("WSTATUS"));
LNSTATUS      = _strUtils.fString(request.getParameter("LNSTATUS"));
PRD_TYPE_ID   = _strUtils.fString(request.getParameter("PRD_TYPE_ID"));
PRD_BRAND_ID  = _strUtils.fString(request.getParameter("PRD_BRAND_ID"));
PRD_CLS_ID    = _strUtils.fString(request.getParameter("PRD_CLS_ID"));

if(DIRTYPE.length()<=0){
DIRTYPE = "WORKORDER";
}

if(PGaction.equalsIgnoreCase("View")){
 
 try{
        Hashtable ht = new Hashtable();
        if(_strUtils.fString(JOBNO).length() > 0)           ht.put("A.JOBNUM",JOBNO);
        if(_strUtils.fString(ITEMNO).length() > 0)          ht.put("B.ITEM",ITEMNO);
        if(_strUtils.fString(ORDERNO).length() > 0)         ht.put("B.WONO",ORDERNO);
        if(_strUtils.fString(CUSTOMERID).length() > 0)      ht.put("A.CUSTCODE",CUSTOMERID);
        if(_strUtils.fString(LNSTATUS).length() > 0)        ht.put("B.LNSTAT",LNSTATUS);
        if(_strUtils.fString(PRD_TYPE_ID).length() > 0)     ht.put("C.ITEMTYPE",PRD_TYPE_ID);
        if(_strUtils.fString(PRD_BRAND_ID).length() > 0)    ht.put("C.PRD_BRAND_ID",PRD_BRAND_ID);
        if(_strUtils.fString(PRD_CLS_ID).length() > 0)      ht.put("C.PRD_CLS_ID",PRD_CLS_ID);
        movQryList = movHisUtil.getWorkOrderSummaryList(ht,fdate,tdate,DIRTYPE,plant,DESC,CUSTOMER,"","","");
        
		if(movQryList.size()<=0)
			cntRec ="true";

 }catch(Exception e) { }
}
%>
<%@ include file="body.jsp"%>
<FORM name="form1" method="post" action="OrderSummaryOutBound.jsp" >
<input type="hidden" name="xlAction" value="">
<input type="hidden" name="PGaction" value="View">
<input type="hidden" name="PRD_CLS_ID1" value="">
<input type="hidden" name="PRD_CLS_DESC" value="">
<input type="hidden" name="PRD_TYPE_DESC" value="">
<input type="hidden" name="PRD_TYPE_ID1" value="">
<input type="hidden" name="PRD_BRAND_DESC" value="">

  <br>
  <TABLE border="0" width="100%" cellspacing="0" cellpadding="0" align="center" bgcolor="#dddddd">
    <TR>
      <TH BGCOLOR="#000066" COLSPAN="20"><font color="white">Work Order Summary </font></TH>
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
          <TD><INPUT name="ITEM" type = "TEXT" value="<%=ITEMNO%>" size="20"  MAXLENGTH=50></TD>
    </TR>
	 <TR>
        <TH ALIGN="left" >&nbsp;Work Order No : </TH>
          <TD><INPUT name="ORDERNO" type = "TEXT" value="<%=ORDERNO%>" size="20"  MAXLENGTH=20></TD>
          <TH ALIGN="left" >Product Description : </TH>
          <TD><INPUT name="DESC" type = "TEXT" value="<%=_strUtils.forHTMLTag(DESC)%>" size="20"  MAXLENGTH=100></TD>
    </TR>
    
     <TR>
           <TH ALIGN="left" >&nbsp;Status : </TH>
          <TD><SELECT NAME ="LNSTATUS" size="1">
            <OPTION selected  value=""> </OPTION>
     		<OPTION   value='O' <%if(LNSTATUS.equalsIgnoreCase("O")){ %>selected<%} %>>O </OPTION>
     		<OPTION   value='C' <%if(LNSTATUS.equalsIgnoreCase("C")){ %>selected<%} %>>C </OPTION>
     		<OPTION   value='N' <%if(LNSTATUS.equalsIgnoreCase("N")){ %>selected<%} %>>N </OPTION>
           </SELECT></TD>
          <TH ALIGN="left">Work Order Customer Name : </TH>
          <TD><INPUT name="CUSTOMER" type = "TEXT" value="<%=CUSTOMER%>" size="20"  MAXLENGTH=20></TD>
                
      </TR>
	<TR>
          <TH ALIGN="left" width="15%">&nbsp;Product Class ID:  </TH>
          <TD width="13%"><INPUT name="PRD_CLS_ID" type = "TEXT" value="<%=PRD_CLS_ID%>" size="20"  MAXLENGTH=20>
           <a href="#" onClick="javascript:popUpWin('activePrdClsList.jsp?PRD_CLS_ID='+form1.PRD_CLS_ID.value);">
           <img src="images/populate.gif" border="0"/>
          </a>
          </TD>
          
            <TH ALIGN="left" width="15%">Product Type ID:  </TH>
          <TD width="13%"><INPUT name="PRD_TYPE_ID" type = "TEXT" value="<%=PRD_TYPE_ID%>" size="20"  MAXLENGTH=20>
           <a href="#" onClick="javascript:popUpWin('activeproductTypeList.jsp?ITEM_ID='+form1.PRD_TYPE_ID.value);"><img src="images/populate.gif" border="0"/>
          </TD>
         </TR>
    	<TR>
         
           <TH ALIGN="left" width="15%">&nbsp;Product Brand ID:  </TH>
          <TD width="13%"><INPUT name="PRD_BRAND_ID" type = "TEXT" value="<%=PRD_BRAND_ID%>" size="20"  MAXLENGTH=20>
           <a href="#" onClick="javascript:popUpWin('PrdBrandList.jsp?ITEM_ID='+form1.PRD_BRAND_ID.value+'&Cond=OnlyActive&formName=form1');">           
           <img src="images/populate.gif" border="0"/>
          </a>
          
            <TH WIDTH="20%" ALIGN="left"></TH>
			
         <TD ALIGN="left" ><input type="button" value="View"  onClick="javascript:return onGo();"></TD>
          
     </TR>
   </TABLE>
  <br>
  <TABLE WIDTH="100%"  border="0" cellspacing="1" cellpadding = 2 align = "center">
    <TR BGCOLOR="#000066">
                <TH width><font color="#ffffff" align="center">S/N</TH>
                <TH><font color="#ffffff" align="left"><b>Order No</TH>
                <TH><font color="#ffffff" align="left"><b>Ref No</TH>
                <TH><font color="#ffffff" align="left"><b>Customer Name</TH>
                <TH><font color="#ffffff" align="left"><b>Remarks</TH>
                <TH><font color="#ffffff" align="left"><b>Product ID</TH>
                <TH><font color="#ffffff" align="left"><b>Description</TH>
                <TH><font color="#ffffff" align="left"><b>Order Date</TH>
                <TH><font color="#ffffff" align="left"><b>Order Qty</TH>
                <TH><font color="#ffffff" align="left"><b>Finished Qty</TH>
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
           
         
        <TD><a href="/track/WorkOrderServlet?WONO=<%=(String)lineArr.get("wono")%>&Submit=View&RFLAG=4"><%=(String)lineArr.get("wono")%></a></TD>
     
    
            <TD align= "left">&nbsp;&nbsp;&nbsp;<%=(String)lineArr.get("jobNum")%></TD>
            <TD align= "left">&nbsp;&nbsp;&nbsp;<%=(String)lineArr.get("custname")%></TD>
            <TD align= "left">&nbsp;&nbsp;&nbsp;<%=(String)lineArr.get("remarks")%></TD>
            <TD align= "left">&nbsp;&nbsp;&nbsp;<%=(String)lineArr.get("item") %></TD>
            <TD align= "left">&nbsp;&nbsp;&nbsp;<%=(String)lineArr.get("itemdesc") %></TD>
            <TD align= "left">&nbsp;&nbsp;&nbsp;<%=(String)lineArr.get("TRANDATE") %></TD>
            <TD align= "right">&nbsp;&nbsp;&nbsp;<%=StrUtils.formatNum((String)lineArr.get("orderqty")) %></TD>
            <TD align= "right">&nbsp;&nbsp;&nbsp;<%=StrUtils.formatNum((String)lineArr.get("finishedqty")) %></TD>
            <TD align= "left">&nbsp;&nbsp;&nbsp;<%=(String)lineArr.get("lnstat") %></TD> 
		  
           </TR>
           
       <%}%>

  </TABLE>
      <br>
    <table align="center" >
     <TR>
   <td>  <input type="button" value=" Back " onClick="window.location.href='../home'">&nbsp; </td>
   <td>   <input type="button" value="ExportReport" onClick="javascript:ExportReport();" > </td>
   <INPUT type="Hidden" name="DIRTYPE" value="WORKORDER">
   </TR>
    </table>
  </FORM>
  
<%@ include file="footer.jsp"%>
