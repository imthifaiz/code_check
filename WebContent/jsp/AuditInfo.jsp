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
function ExportReport()
{
   var flag    = "false";

   var FROM_DATE    = document.form1.FROM_DATE.value;
   var TO_DATE      = document.form1.TO_DATE.value;
   var DIRTYPE      = document.form1.DIRTYPE.value;
  
   var USER         = document.form1.USER.value;
   var ITEMNO         = document.form1.ITEM.value;


   if(FROM_DATE != null     && FROM_DATE != "") { flag = true;}
   if(TO_DATE != null    && TO_DATE != "") { flag = true;}
   if(DIRTYPE != null     && DIRTYPE != "") { flag = true;}
   
   if(USER != null    && USER != "") { flag = true;}
   if(ITEMNO != null     && ITEMNO != "") { flag = true;}

   if(flag == "false"){ alert("Please define any one search criteria"); return false;}
   
  document.form1.xlAction.value="GenerateXLSheet";
  document.form1.action="MovementExcel.jsp";
   
  document.form1.submit();
  
}

function onGo(){

   var flag    = "false";

   var FROM_DATE    = document.form1.FROM_DATE.value;
   var TO_DATE      = document.form1.TO_DATE.value;
   var DIRTYPE      = document.form1.DIRTYPE.value;
   var CMPY 		= document.form1.CMPY.value;
   var USER         = document.form1.USER.value;
  
   

   //if(FROM_DATE != null     && FROM_DATE != "") { flag = true;}
   //if(TO_DATE != null    && TO_DATE != "") { flag = true;}
   if(CMPY != null     && CMPY != "") { flag = true;}
   //if(DIRTYPE != null     && DIRTYPE != "") { flag = true;}
   //if(USER != null    && USER != "") { flag = true;}
  
   if(flag == "false"){ alert("Please Key in Company Name"); return false;}


document.form1.submit();
}



</script>

<script language="JavaScript" type="text/javascript" src="js/calendar.js"></script>
<title>Audit Info</title>
</head>
<jsp:useBean id="sl"  class="com.track.gates.selectBean" />
<link rel="stylesheet" href="css/style.css">
<%

StrUtils _strUtils     = new StrUtils();
Generator generator   = new Generator();
HTReportUtil auditUtil       = new HTReportUtil();
auditUtil.setmLogger(mLogger);
DateUtils _dateUtils = new DateUtils();
ArrayList movQryList  = new ArrayList();

session = request.getSession();
//String action         = _strUtils.fString(request.getParameter("action")).trim();
String PLANT= session.getAttribute("PLANT").toString();
String FROM_DATE ="", CMPY="", TO_DATE = "", DIRTYPE ="",BATCH ="",USER="",ITEM="",fdate="",tdate="",JOBNO="",ITEMNO="",ORDERNO="",CUSTOMER="",PGaction="";

String html = "",cntRec ="false";

PGaction         = _strUtils.fString(request.getParameter("PGaction")).trim();
FROM_DATE     = _strUtils.fString(request.getParameter("FROM_DATE"));
String curDate =_dateUtils.getDate();
TO_DATE   = _strUtils.fString(request.getParameter("TO_DATE"));

if(FROM_DATE==null) FROM_DATE=""; else FROM_DATE = FROM_DATE.trim();
if (FROM_DATE.length()>5)

fdate    = FROM_DATE.substring(6)+"-"+ FROM_DATE.substring(3,5)+"-"+FROM_DATE.substring(0,2);

if(TO_DATE==null) TO_DATE=""; else TO_DATE = TO_DATE.trim();
if (TO_DATE.length()>5)
tdate    = TO_DATE.substring(6)+"-"+ TO_DATE.substring(3,5)+"-"+TO_DATE.substring(0,2);



DIRTYPE       = _strUtils.fString(request.getParameter("DIRTYPE"));
CMPY  		  = _strUtils.fString(request.getParameter("CMPY"));

USER          = _strUtils.fString(request.getParameter("USER"));




if(PGaction.equalsIgnoreCase("View")){
 
 try{
        Hashtable ht = new Hashtable();
       
        if(_strUtils.fString(DIRTYPE).length() > 0)     ht.put("TRANSACTIONID",DIRTYPE);
        if(_strUtils.fString(USER).length() > 0)     ht.put("USERID",USER);
        if(_strUtils.fString(CMPY).length() > 0)     ht.put("COMPANY",CMPY);
         movQryList = auditUtil.getAuditList(ht,CMPY,fdate,tdate);
        
		if(movQryList.size()<=0)
			cntRec ="true";



 }catch(Exception e) {throw new Exception();  }
}
%>
<%@ include file="body.jsp"%>
<FORM name="form1" method="post" action="AuditInfo.jsp" >
<input type="hidden" name="xlAction" value="">
<input type="hidden" name="PGaction" value="View">
  <br>
  <TABLE border="0" width="100%" cellspacing="0" cellpadding="0" align="center" bgcolor="#dddddd">
    <TR>
      <TH BGCOLOR="#000066" COLSPAN="20"><font color="white">Audit Information</font></TH>
    </TR>
  </TABLE>
  <br>
  <TABLE border="0" width="80%" height = "20%" cellspacing="0" cellpadding="0" align="center" bgcolor="#dddddd">
    
     <TR>
          <TH ALIGN="left" >&nbsp;Company : </TH>
          <TD>
          &nbsp;&nbsp;
          <select name="CMPY">
          <option selected="selected" value=""></option> 
          <%=sl.getPlantNames("0")%></select>
          </TD>
          <TH ALIGN="left">&nbsp;User : </TH>
          <TD><INPUT name="USER" type = "TEXT" value="<%=USER%>" size="20"  MAXLENGTH=50 >&nbsp;&nbsp;</TD>
		 
    </TR>
    <TR>
          <TH ALIGN="left" >&nbsp;From_Date : </TH>
          <TD><INPUT name="FROM_DATE" type = "TEXT" value="<%=FROM_DATE%>" size="20"  MAXLENGTH=20 class = "inactiveGry" READONLY>&nbsp;&nbsp;<a href="javascript:show_calendar('form1.FROM_DATE');" onmouseover="window.status='Date Picker';return true;" onmouseout="window.status='';return true;"><img src="images\show-calendar.gif" width=24 height=22 border=0></a></TD>
          <TH ALIGN="left">&nbsp;To_Date : </TH>
          <TD><INPUT name="TO_DATE" type = "TEXT" value="<%=TO_DATE%>" size="20"  MAXLENGTH=20 class = "inactiveGry"  READONLY>&nbsp;&nbsp;<a href="javascript:show_calendar('form1.TO_DATE');" onmouseover="window.status='Date Picker';return true;" onmouseout="window.status='';return true;"><img src="images\show-calendar.gif" width=24 height=22 border=0></a></TD>
		 
    </TR>
    
    <TR>
           <TH ALIGN="left" >&nbsp;Transaction Type : </TH>
          <TD><INPUT name="DIRTYPE" type = "TEXT" value="<%=DIRTYPE%>" size="20"  MAXLENGTH=50>
          </TD>
            
          <TH ALIGN="left"> </TH>
          <TD></TD>

		  <TD ALIGN="left" ><TD ALIGN="left" ><TD ALIGN="left" ><input type="button" value="View" onClick="javascript:return onGo();"></TD>
		          
    </TR>
	
  </TABLE>
  <br>
  <TABLE WIDTH="90%"  border="0" cellspacing="1" cellpadding = 2 align = "center">
    <TR BGCOLOR="#000066">

         <TH width="3%"><font color="#ffffff" align="center">S/N</TH>
         <TH width="10%"><font color="#ffffff" align="left"><b>Date Time</TH>

         <TH width="10%"><font color="#ffffff" align="left"><b>Transaction Type</TH>
         <TH width="10%">
            <FONT color="#ffffff">User</FONT>
          </TH>
            <TH width="10%"><font color="#ffffff" align="left"><b>Log Message</TH>
	      
        
       </tr>
       <%
	      if(movQryList.size()<=0 && cntRec=="true" ){ %>
		  <TR><TD colspan=16 align=center>No Data For This criteria</TD></TR>
		  <%}%>

		  <%
        com.track.dao.InvMstDAO _InvMstDAO = new com.track.dao.InvMstDAO();
        com.track.dao.CustMstDAO _CustMstDAO = new com.track.dao.CustMstDAO();
        
        _InvMstDAO.setmLogger(mLogger);
        _CustMstDAO.setmLogger(mLogger);
        
        
        for (int iCnt =0; iCnt<movQryList.size(); iCnt++){
			   Map lineArr = (Map) movQryList.get(iCnt);

          String trDate= "";
          trDate=(String)lineArr.get("CRDT");
          
       
		  if (trDate.length()>8)
          trDate    = trDate.substring(8,10)+":"+ trDate.substring(10,12)+":"+trDate.substring(12,14);

     
        
          int iIndex = iCnt + 1;
          String bgcolor = ((iCnt == 0) || (iCnt % 2 == 0)) ? "#FFFFFF"  : "#dddddd";
      
       
       %>

          <TR bgcolor = "<%=bgcolor%>">
            <TD align="center" width="3%"><%=iIndex%></TD>
         
            <TD align= "left" width="10%">&nbsp;&nbsp;&nbsp;<%=(String)lineArr.get("CRDT")%></TD>

            <TD align= "left" width="1%">&nbsp;&nbsp;&nbsp;<%=(String)lineArr.get("TRANSACTIONID") %></TD>
            <TD align="left" width="10%">&nbsp;&nbsp;&nbsp;<%=(String)lineArr.get("USERID") %></TD>
             <TD align= "left" width="10%">&nbsp;&nbsp;&nbsp;<%=(String)lineArr.get("LOGMSG") %></TD>
			     
          
           </TR>
       <%}%>
    
    </TABLE>

	 
  </FORM>
<%@ include file="footer.jsp"%>