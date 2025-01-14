<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.util.*"%>
<%@ page import="java.util.ArrayList.*"%>
<%@ include file="header.jsp" %>


<html>
<title>Item Location List</title>
<script language="javascript">
function onGo(){

   var flag    = "false";
   var LOC     = document.form1.LOC.value;
   var ITEM    = document.form1.ITEM.value;


   if(LOC != null     && LOC != "") { flag = true;}
   if(ITEM != null    && ITEM != "") { flag = true;}
   if(flag == "false"){ alert("Please define any one search criteria"); return false;}

   document.form1.action = "item_loc_list.jsp?action=Go";
   document.form1.submit();
}
function onAddNew(){
   document.form1.action = "item_loc.jsp";
   document.form1.submit();
}
</script>
<link rel="stylesheet" href="css/style.css">

<%
  ItemUtil itemUtils    = new ItemUtil();
  StrUtils strUtils = new StrUtils();
  ArrayList arrList = new ArrayList();
  String aItem      = strUtils.fString(request.getParameter("ITEM"));
  String aLoc       = strUtils.fString(request.getParameter("LOC"));
  String action     = strUtils.fString(request.getParameter("action"));
  if(action.equalsIgnoreCase("Go")){
       arrList = itemUtils.getItemLocList(aItem,aLoc);
  }
  String sBGColor   = "";
  String sLink      = "";
  String res        = "";
  res = strUtils.fString(request.getParameter("res"));
%>
<%@ include file="body.jsp" %>
  <center>
  <br><br>
<form name= "form1"  method = "post">
     <TABLE border="1" width="100%" cellspacing="0" cellpadding="0" align="center" bgcolor="#dddddd">
      <TR>
        <TH BGCOLOR="#000066" COLSPAN="11"><font color="white">ITEM LOCATION MASTER</font>
      </TABLE>
  <br>
  <TABLE border="0" width="80%" cellspacing="0" cellpadding="0" align="center" bgcolor="#dddddd">
    <TR>
          <TH ALIGN="left" >Item Code : </TH>
          <TD><INPUT name="ITEM" type = "TEXT" value="<%=aItem%>" size="20"  MAXLENGTH=50></TH>
          <TH ALIGN="left">Location : </TH>
          <TD><INPUT name="LOC" type = "TEXT" value="<%=aLoc%>" size="20"  MAXLENGTH=20></TD>
          <TD ALIGN="center" ><input type="button" value="Go" onClick="javascript:return onGo();"></TD>
    </TR>
  </TABLE>
  <BR>
  <BR>
    <TABLE BORDER="0" CELLSPACING="1" WIDTH="80%" align="center">
      <TR bgcolor="navy">
        <TH width="20%"><font color="#ffffff">Item No</TH>
        <TH width="40%"><font color="#ffffff">Description</TH>
        <TH width="15%"><font color="#ffffff">Location</TH>
     </TR>
     <%
      for(int iCnt = 0; iCnt<arrList.size(); iCnt++){
          ArrayList arrLine = (ArrayList)arrList.get(iCnt);
          sBGColor = ((iCnt == 0) || (iCnt % 2 == 0)) ? "#FFFFFF" : "#dddddd";
          sLink = "<a href=\"item_loc.jsp?"+
                  "ITEM="+(String)arrLine.get(0)+
                  "&ITEMDESC="+strUtils.replaceCharacters2Send((String)arrLine.get(1))+
                 "&LOC=" + (String)arrLine.get(2) + "\">";
     %>
     <TR bgcolor="<%=sBGColor%>">
          <TD><%=sLink%><%=(String)arrLine.get(0)%></a></TD>
          <TD><%=(String)arrLine.get(1)%></TD>
          <TD><%=(String)arrLine.get(2)%></TD>
     </TR>
    <%
       }
    %>
    <TR><TH colspan = 4><%=res%></TH></TR>

    </TABLE>
    </font>
    <center>
      <input type="button" value="Add New" onClick="javascript:return onAddNew();">
</form>

    </center>
  </center>

<%@ include file="footer.jsp" %>
