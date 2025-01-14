<%@ page import="com.track.gates.DbBean"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.util.*"%>
<%@ page import="com.track.tables.*"%>
<%@ page import="com.track.gates.*"%>
<%@ page import="java.util.*"%>
<%@ include file="header.jsp"%>
<html>
<head>
<script language="javascript">
function onGo(){

   var flag    = "false";
   var LOC     = document.form1.LOC.value;
   var ITEM    = document.form1.ITEM.value;


   if(LOC != null     && LOC != "") { flag = true;}
   if(ITEM != null    && ITEM != "") { flag = true;}
   if(flag == "false"){ alert("Please define any one search criteria"); return false;}


document.form1.submit();
}
</script>
<title>Packing List</title>
</head>
<link rel="stylesheet" href="css/style.css">
<%
StrUtils strUtils     = new StrUtils();
Generator generator   = new Generator();
PCKALLOC pckalloc       = new PCKALLOC();
ArrayList pckQryList  = new ArrayList();
String action         = strUtils.fString(request.getParameter("action")).trim();
String DONO ="",  CUSTID = "", MODEL ="",MANNO ="",SERIALNO ="";
String html = "";
DONO     = strUtils.fString(request.getParameter("DONO"));
CUSTID    = strUtils.fString(request.getParameter("CUSTID"));
MODEL     = strUtils.fString(request.getParameter("MODEL"));
MANNO    = strUtils.fString(request.getParameter("MANNO"));
SERIALNO     = strUtils.fString(request.getParameter("SERIALNO"));



if(action.equalsIgnoreCase("Go")){
 try{
      pckQryList = pckalloc.getPCKList(DONO,CUSTID,MODEL,MANNO,SERIALNO);
	  System.out.println("pckQryList: "+pckQryList.size());
 }catch(Exception e) { }
}
%>
<%@ include file="body.jsp"%>
<FORM name="form1" method="post">
  <br>
  <TABLE border="0" width="100%" cellspacing="0" cellpadding="0" align="center" bgcolor="#dddddd">
    <TR>
      <TH BGCOLOR="#000066" COLSPAN="11"><font color="white">PACKING LIST</font></TH>
    </TR>
  </TABLE>
  <br>
  <TABLE border="0" width="80%" cellspacing="0" cellpadding="0" align="center" bgcolor="#dddddd">
    <TR>
          <TH ALIGN="left" >DONO : </TH>
          <TD><INPUT name="DONO" type = "TEXT" value="<%=DONO%>" size="20"  MAXLENGTH=20></TH>
          <TH ALIGN="left">CUSTID : </TH>
          <TD><INPUT name="CUSTID" type = "TEXT" value="<%=CUSTID%>" size="20"  MAXLENGTH=20></TD>

    </TR>
	<TR>
          <TH ALIGN="left" >MODEL : </TH>
          <TD><INPUT name="MODEL" type = "TEXT" value="<%=MODEL%>" size="20"  MAXLENGTH=20></TH>
          <TH ALIGN="left">MAN.NO : </TH>
          <TD><INPUT name="MANNO" type = "TEXT" value="<%=MANNO%>" size="20"  MAXLENGTH=20></TD>

    </TR>
	<TR>
          <TH ALIGN="left" >SERIAL NO: </TH>
          <TD><INPUT name="SERIALNO" type = "TEXT" value="<%=SERIALNO%>" size="20"  MAXLENGTH=20></TH>
          <td></td><td></td>
          <TD ALIGN="center" ><input type="submit" value="Go" name="action" onClick="javascript:return onGo();"></TD>
    </TR>
  </TABLE>
  <br>
  <TABLE WIDTH="80%"  border="0" cellspacing="1" cellpadding = 2 align = "center">
    <TR BGCOLOR="#000066">
         <TH><font color="#ffffff" align="center">SNO</TH>
         <TH><font color="#ffffff" align="left"><b>DONO</TH>
         <TH><font color="#ffffff" align="left"><b>CUSTID</TH>
         <TH><font color="#ffffff" align="left"><b>MODEL</TH>
         <TH><font color="#ffffff" align="left"><b>MAN.NO</TH>
         <TH><font color="#ffffff" align="left"><b>SERIAL NO</TH>

       </tr>
       <%
          for (int iCnt =0; iCnt<pckQryList.size(); iCnt++){
          ArrayList lineArr = new ArrayList();
          lineArr = (ArrayList)pckQryList.get(iCnt);
          int iIndex = iCnt + 1;
          String bgcolor = ((iCnt == 0) || (iCnt % 2 == 0)) ? "#FFFFFF"  : "#dddddd";
       %>
          <TR bgcolor = "<%=bgcolor%>">
            <TD align="center"><%=iIndex%></TD>
            <TD><%=(String)lineArr.get(0)%></TD>
            <TD><%=(String)lineArr.get(1)%></TD>
            <TD align= "left"><%=(String)lineArr.get(2)%></TD>
            <TD align= "left"><%=(String)lineArr.get(3)%></TD>
            <TD align= "left"><%=(String)lineArr.get(4) %></TD>
			</TR>
       <%}%>

    </TABLE>
  </FORM>
<%@ include file="footer.jsp"%>