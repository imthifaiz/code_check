<%@ page import="com.track.util.*"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.constants.IConstants"%>
<!-- Not in Use -->
<%@ page import="java.util.*"%>

<%@ include file="header.jsp" %>

<script language="JavaScript" type="text/javascript" src="js/calendar.js"></script>
<script language = "JavaScript">
function onAdd(){
     var ITEM = document.form1.ITEM.value;
     var LOC = document.form1.LOC.value;
     if(ITEM == "" || ITEM == null) {alert("Please Enter the Item Code"); document.form1.ITEM.focus(); return false; }
     if(LOC == "" || LOC == null) {alert("Please Enter Location"); document.form1.LOC.focus(); return false; }
     if (isNaN( parseInt(LOC))) {alert("Please Enter Location As a Number."); document.form1.LOC.focus();return false;}
     document.form1.action  = "item_loc.jsp?action=ADD";
     document.form1.submit();
}
function onDelete(){
     var ITEM = document.form1.ITEM.value;
     if(ITEM == "" || ITEM == null) {alert("Please Enter the Item Code"); document.form1.ITEM.focus(); return false; }

        document.form1.action  = "item_loc.jsp?action=DELETE";
        document.form1.submit();
}
</script>
<title>Item Location</title>

<SCRIPT LANGUAGE="JavaScript">
<!-- Begin
 var subWin = null;
 function popUpWin(URL) {
   subWin = window.open(URL, 'Items', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=700,height=400,left = 200,top = 184');
 }
//-->
</script>
<%
  ItemUtil itemUtils = new ItemUtil();
  StrUtils strUtils = new StrUtils();
  String res = "";
  String sItemCode    = strUtils.fString(request.getParameter("ITEM"));
  sItemCode           = strUtils.replaceCharacters2Recv(sItemCode);
  String sLocation    = strUtils.fString(request.getParameter("LOC"));
  String sItemDesc    = strUtils.fString(request.getParameter("ITEMDESC"));
  sItemDesc           = strUtils.replaceCharacters2Recv(sItemDesc);
  String action       = strUtils.fString(request.getParameter("action"));
  if(action.equalsIgnoreCase("ADD")){
       if(itemUtils.isExistsLoc4Item(sItemCode,sLocation)){
            res = "<font class = "+IConstants.FAILED_COLOR+">Item Location Exists Already. Try again</font>";
       } else {
          Hashtable ht = new Hashtable();

          ht.put(IConstants.PLANT,IConstants.PLANT_VAL);
          ht.put(IConstants.ITEM,sItemCode);
          ht.put(IConstants.LOC,sLocation);
          boolean inserted = itemUtils.insertItemLoc(ht);
          if(inserted == true) res = "<font class = "+IConstants.SUCCESS_COLOR+">Item Location Added Successfully</font>";
          else res = "<font class = "+IConstants.FAILED_COLOR+">Failed to Add Item Location</font>";
          response.sendRedirect("item_loc_list.jsp?action=Go&ITEM="+sItemCode+"&res="+res);
       }
  }

  else if(action.equalsIgnoreCase("DELETE")){
    Hashtable ht = new Hashtable();
    ht.put(IConstants.ITEM,sItemCode);
    ht.put(IConstants.LOC,sLocation);
    boolean deleted = itemUtils.deleteItemLoc(ht);
    if(deleted == true) res = "<font class = "+IConstants.SUCCESS_COLOR+">Item Location Deleted Successfully</font>";
    else res = "<font class = "+IConstants.FAILED_COLOR+">Failed to Delete Item Location</font>";
    response.sendRedirect("item_loc_list.jsp?action=Go&ITEM="+sItemCode+"&res="+res);
  }
%>

<link rel="stylesheet" href="css/style.css">
<%@ include file="body.jsp" %>

<FORM name="form1" method="post" action="ccSbmt.jsp">
  <br>


  <table border="0" width="100%" cellspacing="0" cellpadding="0" align="center" bgcolor="#dddddd">
      <TR>
        <TH BGCOLOR="#000066" COLSPAN="11"><font color="white">ITEM LOCATION</font>
      </table>
  <BR><BR>
  <table border="0" width="100%" cellspacing="0" cellpadding="0" align="center" bgcolor="#dddddd">
    <tr>
    <td width="100%">
    <CENTER>
    <TABLE border="0" CELLSPACING=0 WIDTH="100%">
    <TR>
        <TH WIDTH="35%" ALIGN="RIGHT" >Item No :
        <TD>
            <INPUT name="ITEM" type = "TEXT" size="50"  MAXLENGTH=50 value = "<%=sItemCode%>">
            <a href="#" onClick="javascript:popUpWin('item_list_2choose.jsp?ITEM='+form1.ITEM.value);"><img src="images/populate.gif" border="0"></a>
            <INPUT name="ITEM1" type = "HIDDEN" value = "<%=sItemCode%>">
            <INPUT name="UOM" type = "HIDDEN">
       </TD>
     </TR>
     <TR>
            <TH WIDTH="35%" ALIGN="RIGHT" >Description :
            <TH WIDTH="65%" ALIGN = "LEFT"><INPUT type = "TEXT" class="inactive" size="50"  MAXLENGTH=80 name="DESC" value = "<%=sItemDesc%>" readonly>
      </TR>
      <TR>
             <TH WIDTH="35%" ALIGN="RIGHT" >Location :
             <TD><INPUT name="LOC" size="50"  MAXLENGTH=80 value = "<%=sLocation%>">
       </TR>
       <TR>
              <TH colspan = 2><br><%=res%></TD>
        </TR>
       <TR>
              <TH colspan = 2><br>&nbsp;</TD>
        </TR>
       <TR>
              <TH colspan = 2>
                  <INPUT type="Button" value="Cancel" onClick="window.location.href='item_loc_list.jsp'">&nbsp;&nbsp;
                  <INPUT class="Submit" type="BUTTON" value="Add" onClick="onAdd();">&nbsp;&nbsp;
                  <INPUT class="Submit" type="BUTTON" value="Delete" onClick="onDelete();">
              </TH>
        </TR>
    </TABLE>
</CENTER>
</td>
</tr>
</table>
</FORM>
<%@ include file="footer.jsp" %>
