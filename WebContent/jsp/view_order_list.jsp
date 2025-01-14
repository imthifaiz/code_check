<%@ page import="com.track.gates.DbBean"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.util.*"%>
<%@ page import="com.track.constants.*"%>
<%@ page import="com.track.gates.*"%>
<%@ page import="java.util.*"%>
<%@ include file="header.jsp"%>
<html>
<head>
<script language="javascript">
function onGo(){

   var flag    = "false";

   var FROM_DATE    = document.form1.FROM_DATE.value;
   var TO_DATE      = document.form1.TO_DATE.value;
   var DIRTYPE      = document.form1.DIRTYPE.value;
   var MOVTID       = document.form1.MOVTID.value;
   var PONO         = document.form1.PONO.value;
   var USER         = document.form1.USER.value;
   var ITEM         = document.form1.ITEM.value;


   if(FROM_DATE != null     && FROM_DATE != "") { flag = true;}
   if(TO_DATE != null    && TO_DATE != "") { flag = true;}
   if(DIRTYPE != null     && DIRTYPE != "") { flag = true;}
   if(MOVTID != null    && MOVTID != "") { flag = true;}
   if(PONO != null     && PONO != "") { flag = true;}
   if(USER != null    && USER != "") { flag = true;}
   if(ITEM != null     && ITEM != "") { flag = true;}

   if(flag == "false"){ alert("Please define any one search criteria"); return false;}


document.form1.submit();
}
</script>
</script>
<script language="JavaScript" type="text/javascript" src="js/calendar.js"></script>
<title>Inventory List</title>
</head>
<link rel="stylesheet" href="css/style.css">
<%
StrUtils _strUtils     = new StrUtils();
Generator generator   = new Generator();
MovHisUtil movHisUtil       = new MovHisUtil();
DateUtils _dateUtils = new DateUtils();
ArrayList movQryList  = new ArrayList();
String action         = _strUtils.fString(request.getParameter("action")).trim();
String FROM_DATE ="",  TO_DATE = "", DIRTYPE ="",MOVTID ="",PONO ="",USER="",ITEM="",fdate="",tdate="";
String html = "",cntRec ="false";
FROM_DATE     = _strUtils.fString(request.getParameter("FROM_DATE"));
if(_strUtils.fString(FROM_DATE).length() > 0){ fdate     = generator.getSQLDate(FROM_DATE);}
 TO_DATE       = _strUtils.fString(request.getParameter("TO_DATE"));
if(_strUtils.fString(TO_DATE).length() > 0)  {
tdate       = generator.getSQLDate(TO_DATE);
 }

DIRTYPE       = _strUtils.fString(request.getParameter("DIRTYPE"));
MOVTID        = _strUtils.fString(request.getParameter("MOVTID"));
PONO          = _strUtils.fString(request.getParameter("PONO"));
USER          = _strUtils.fString(request.getParameter("USER"));
ITEM          = _strUtils.fString(request.getParameter("ITEM"));


if(action.equalsIgnoreCase("Go")){
 try{
        Hashtable ht = new Hashtable();
       // if(_strUtils.fString(FROM_DATE).length() > 0)   ht.put(IConstants.CREATED_AT,FROM_DATE);
       // if(_strUtils.fString(TO_DATE).length() > 0)     ht.put(IConstants.CREATED_AT,TO_DATE);
        if(_strUtils.fString(DIRTYPE).length() > 0)     ht.put(IConstants.DIRTYPE,DIRTYPE);
        if(_strUtils.fString(MOVTID).length() > 0)      ht.put(IConstants.MOVTID,MOVTID);
        if(_strUtils.fString(PONO).length() > 0)        ht.put(IConstants.PONO,PONO);
        if(_strUtils.fString(USER).length() > 0)        ht.put(IConstants.CREATED_BY,USER);
        if(_strUtils.fString(ITEM).length() > 0)        ht.put(IConstants.ITEM,ITEM);

        movQryList = movHisUtil.getMovHisList(ht,fdate,tdate);
		if(movQryList.size()<=0)
			cntRec ="true";



 }catch(Exception e) { }
}
%>
<%@ include file="body.jsp"%>
<FORM name="form1" method="post">
  <br>
  <TABLE border="0" width="100%" cellspacing="0" cellpadding="0" align="center" bgcolor="#dddddd">
    <TR>
      <TH BGCOLOR="#000066" COLSPAN="20"><font color="white">MovHistory List</font></TH>
    </TR>
  </TABLE>
  <br>
  <TABLE border="0" width="80%" height = "20%" cellspacing="0" cellpadding="0" align="center" bgcolor="#dddddd">
    <TR>
          <TH ALIGN="left" >FROM_DATE : </TH>
          <TD><INPUT name="FROM_DATE" type = "TEXT" value="<%=FROM_DATE%>" size="20"  MAXLENGTH=20 class = inactive READONLY>&nbsp;&nbsp;<a href="javascript:show_calendar('form1.FROM_DATE');" onmouseover="window.status='Date Picker';return true;" onmouseout="window.status='';return true;"><img src="images\show-calendar.gif" width=24 height=22 border=0></a></TD>
          <TH ALIGN="left">TO_DATE : </TH>
          <TD><INPUT name="TO_DATE" type = "TEXT" value="<%=TO_DATE%>" size="20"  MAXLENGTH=20 class = inactive READONLY>&nbsp;&nbsp;<a href="javascript:show_calendar('form1.TO_DATE');" onmouseover="window.status='Date Picker';return true;" onmouseout="window.status='';return true;"><img src="images\show-calendar.gif" width=24 height=22 border=0></a></TD>
		 
    </TR>
	<TR><td></td><td></td></TR>
    <TR>
          <TH ALIGN="left" >OrderNO : </TH>
          <TD><INPUT name="DIRTYPE" type = "TEXT" value="<%=DIRTYPE%>" size="20"  MAXLENGTH=20></TD>
          <TH ALIGN="left">Cust&nbsp;Name : </TH>
          <TD><INPUT name="MOVTID" type = "TEXT" value="<%=MOVTID%>" size="20"  MAXLENGTH=20></TD>
		 
    </TR>
	<TR><td></td><td></td></TR>
    <TR>
          <TH ALIGN="left" >Item&nbsp;Code : </TH>
          <TD><INPUT name="PONO" type = "TEXT" value="<%=PONO%>" size="20"  MAXLENGTH=20></TD>
          <TH ALIGN="left">User : </TH>
          <TD><INPUT name="USER" type = "TEXT" value="<%=USER%>" size="20"  MAXLENGTH=20></TD>
		          
    </TR>
	<TR><td></td><td></td></TR>

  </TABLE>
  <br>
  <TABLE WIDTH="80%"  border="0" cellspacing="1" cellpadding = 2 align = "center">
    <TR BGCOLOR="#000066">

         <TH><font color="#ffffff" align="center">SNO</TH>
         <TH><font color="#ffffff" align="left"><b>Job No</TH>
         <TH><font color="#ffffff" align="left"><b>Date</TH>
         <TH><font color="#ffffff" align="left"><b>Time</TH>
         <TH><font color="#ffffff" align="left"><b>Dir Type</TH>
         <TH><font color="#ffffff" align="left"><b>Item</TH>
         <TH><font color="#ffffff" align="left"><b>Customer</TH>
         <TH><font color="#ffffff" align="left"><b>User</TH>
        </tr>
       <%
	      if(movQryList.size()<=0 && cntRec=="true" ){ %>
		  <TR><TD colspan=15 align=center>No Data For This criteria</TD></TR>
		  <%}%>

		  <%
          for (int iCnt =0; iCnt<movQryList.size(); iCnt++){
          ArrayList lineArr = new ArrayList();
          lineArr = (ArrayList)movQryList.get(iCnt);
          int iIndex = iCnt + 1;
          String bgcolor = ((iCnt == 0) || (iCnt % 2 == 0)) ? "#FFFFFF"  : "#dddddd";
       %>

          <TR bgcolor = "<%=bgcolor%>">
            <TD align="center"><%=iIndex%></TD>
            <TD><%=_dateUtils.getDB2UserDate_New((String)lineArr.get(0))%></TD>
            <TD><%=(String)lineArr.get(1)%></TD>
            <TD align= "right"><%=(String)lineArr.get(2)%></TD>
            <TD align= "right"><%=(String)lineArr.get(3) %></TD>
            <TD align= "center"><%=(String)lineArr.get(4) %></TD>
            <TD><%=(String)lineArr.get(5)%></TD>
            <TD align= "center"><%=(String)lineArr.get(6) %></TD>
            <TD align= "center"><%=(String)lineArr.get(7) %></TD>
           </TR>
       <%}%>


    </TABLE>
  </FORM>
<%@ include file="footer.jsp"%>