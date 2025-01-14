<%@ page import="com.track.util.*"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.constants.IConstants"%>
<!-- Not in Use -->
<%@ page import="java.util.*"%>

<%@ include file="header.jsp" %>

<script language="JavaScript" type="text/javascript" src="js/calendar.js"></script>
<script language = "JavaScript">
function onAdd(){
     var ITEM = document.form.ITEM.value;
     var CYCLESDATE = document.form.CYCLESDATE.value;
     var CYCLEEDATE = document.form.CYCLEEDATE.value;

     if(ITEM == "" || ITEM == null) {alert("Please Enter the Item Code"); document.form.ITEM.focus(); return false; }
     if(CYCLESDATE == "" || CYCLESDATE == null) {alert("Please Enter Cycle Count Start Date"); document.form.CYCLESDATE.focus(); return false; }
     if(CYCLEEDATE == "" || CYCLEEDATE == null) {alert("Please Enter Cycle Count End Date"); document.form.CYCLEEDATE.focus(); return false; }

     document.form.action  = "CC_itemAdd.jsp?action=ADD";
     document.form.submit();
}
function onUpdate(){
     var ITEM = document.form.ITEM.value;
     var CYCLESDATE = document.form.CYCLESDATE.value;
     var CYCLEEDATE = document.form.CYCLEEDATE.value;

     if(ITEM == "" || ITEM == null) {alert("Please Enter the Item Code"); document.form.ITEM.focus(); return false; }
     if(CYCLESDATE == "" || CYCLESDATE == null) {alert("Please Enter Cycle Count Start Date"); document.form.CYCLESDATE.focus(); return false; }
     if(CYCLEEDATE == "" || CYCLEEDATE == null) {alert("Please Enter Cycle Count End Date"); document.form.CYCLEEDATE.focus(); return false; }
     //var confirm = confirm ("Are you sure want to Update the Cycle Count?");
     //if(confirm) {
        document.form.action  = "CC_itemAdd.jsp?action=UPDATE";
        document.form.submit();
     //}else{
       //return false;
     //}
}
function onDelete(){
     var aItem = document.form.ITEM.value;
     if(aItem == "" || aItem == null) {alert("Please Enter the Item Code"); document.form.ITEM.focus(); return false; }
     //var confirm = confirm ("Are you sure want to Delete the Cycle Count?");
    // if(confirm) {
        document.form.action  = "CC_itemAdd.jsp?action=DELETE";
        document.form.submit();
     //}else{
     //  return false;
     //}
}
</script>
<title>Cycle Count Item Details</title>

<SCRIPT LANGUAGE="JavaScript">
<!-- Begin
 var subWin = null;
 function popUpWin(URL) {
   subWin = window.open(URL, 'Items', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=700,height=400,left = 200,top = 184');
 }
//-->
</script>
<%
  CCUtil ccUtils = new CCUtil();
  StrUtils strUtils = new StrUtils();
  String res = "";
  String sItemCode    = strUtils.fString(request.getParameter("ITEM"));
  sItemCode = strUtils.replaceCharacters2Recv(sItemCode);
  String sCycleStDate = strUtils.fString(request.getParameter("CYCLESDATE"));
  String sCycleEdDate = strUtils.fString(request.getParameter("CYCLEEDATE"));
  String sItemDesc    = strUtils.fString(request.getParameter("ITEMDESC"));
  sItemDesc           = strUtils.replaceCharacters2Recv(sItemDesc);
  String action       = strUtils.fString(request.getParameter("action"));
  //System.out.println("sItemCode : " + sItemCode + ",sCycleStDate : " + sCycleStDate +",sCycleEdDate : " + sCycleEdDate);
  if(action.equalsIgnoreCase("ADD")){
       if(ccUtils.isExistInCycleCntItem(sItemCode)){
            res = "<font class = "+IConstants.FAILED_COLOR+">Item Exists Already. Try again</font>";
       } else {
          Hashtable ht = new Hashtable();
          ht.put(IConstants.PLANT,IConstants.PLANT_VAL);
          ht.put(IConstants.ITEM,sItemCode);
          ht.put(IConstants.CYCLECOUNT_ST_DATE,sCycleStDate);
          ht.put(IConstants.CYCLECOUNT_END_DATE,sCycleEdDate);
          boolean inserted = ccUtils.insertIntoCCItem(ht);
          if(inserted == true) res = "<font class = "+IConstants.SUCCESS_COLOR+">Item Added Successfully</font>";
          else res = "<font class = "+IConstants.FAILED_COLOR+">Failed to Add Item</font>";
          response.sendRedirect("CC_itemSumm.jsp?res="+res);
       }
  }

  else if(action.equalsIgnoreCase("UPDATE")){
     Hashtable htUpdate    = new Hashtable();

     htUpdate.put(IConstants.CYCLECOUNT_ST_DATE,sCycleStDate);
     htUpdate.put(IConstants.CYCLECOUNT_END_DATE,sCycleEdDate);

     Hashtable htCondition = new Hashtable();
     htCondition.put(IConstants.ITEM,sItemCode);

     boolean updated = ccUtils.updateCCItem(htUpdate,htCondition);
     
     if(updated == true) res = "<font class = "+IConstants.SUCCESS_COLOR+">Item Updated Successfully</font>";
     else res = "<font class = "+IConstants.FAILED_COLOR+">Failed to Update Item</font>";
     
     response.sendRedirect("CC_itemSumm.jsp?res="+res);
  }

  else if(action.equalsIgnoreCase("DELETE")){
    boolean deleted = ccUtils.deleteCycleCountItem(sItemCode);
    if(deleted == true) res = "<font class = "+IConstants.SUCCESS_COLOR+">Item Deleted Successfully</font>";
    else res = "<font class = "+IConstants.FAILED_COLOR+">Failed to Delete Item</font>";
    response.sendRedirect("CC_itemSumm.jsp?res="+res);
  }
%>

<link rel="stylesheet" href="css/style.css">
<%@ include file="body.jsp" %>

<FORM name="form" method="post" action="ccSbmt.jsp">
  <br>


  <table border="0" width="100%" cellspacing="0" cellpadding="0" align="center" bgcolor="#dddddd">
      <TR>
        <TH BGCOLOR="#000066" COLSPAN="11"><font color="white">CYCLE COUNT </font>
      </table>
  <br>
  <table border="0" width="100%" cellspacing="0" cellpadding="0" align="center" bgcolor="#dddddd">
    <tr>
    <td width="100%">
    <CENTER>
    <TABLE border="0" CELLSPACING=0 WIDTH="100%">
    <TR>
        <TH WIDTH="35%" ALIGN="RIGHT" >Item No : 
        <TD>
            <INPUT name="ITEM" type = "TEXT" size="50"  MAXLENGTH=80 value = "<%=sItemCode%>">
            <a href="#" onClick="javascript:popUpWin('item_list_cc.jsp?ITEM='+form.ITEM.value+'&CUST_CODE='+form.CUST_CODE.value);"><img src="images/populate.gif" border="0"></a>
            <INPUT name="ITEM1" type = "HIDDEN" value = "<%=sItemCode%>">
            <INPUT name="UOM" type = "HIDDEN">
           
       </TD>
     </TR>
     <TR>
            <TH WIDTH="35%" ALIGN="RIGHT" >Description :
            <TH WIDTH="65%" ALIGN = "LEFT"><INPUT type = "TEXT" class="inactive" size="50"  MAXLENGTH=80 name="DESC" value = "<%=sItemDesc%>" readonly>
      </TR>
       <TR>
            <TH WIDTH="35%" ALIGN="RIGHT" >Customer :
           <TD>
             <INPUT name="CUST_NAME" type = "TEXT" value="" size="50"  MAXLENGTH=80>
             <a href="#" onClick="javascript:popUpWin('customer_list_cc.jsp?CUST_NAME='+form.CUST_NAME.value);"><img src="images/populate.gif" border="0"></a>
           </TH>
            </TH>
            </tr>
            <tr>
        <TH WIDTH="35%" ALIGN="RIGHT" >Customer Code :
        <td>
            <INPUT name="CUST_CODE" type = "TEXT"  size="50" >
      </td>
      </TR>
      <TR>
             <TH WIDTH="35%" ALIGN="RIGHT" >Start Date :
             <TD><INPUT name="CYCLESDATE" size="50"  MAXLENGTH=80 value = "<%=sCycleStDate%>"><a href="javascript:show_calendar('form.CYCLESDATE');" onmouseover="window.status='Date Picker';return true;" onmouseout="window.status='';return true;"><img src="images\show-calendar.gif" width=24 height=22 border=0></a>
       </TR>
       <TR>
              <TH WIDTH="35%" ALIGN="RIGHT" >End Date :
              <TD><INPUT name="CYCLEEDATE" size="50"  MAXLENGTH=80 value = "<%=sCycleEdDate%>"><a href="javascript:show_calendar('form.CYCLEEDATE');" onmouseover="window.status='Date Picker';return true;" onmouseout="window.status='';return true;"><img src="images\show-calendar.gif" width=24 height=22 border=0></a>
        </TR>
       <TR>
              <TH colspan = 2><br><%=res%></TD>
        </TR>
       <TR>
              <TH colspan = 2><br>&nbsp;</TD>
        </TR>
       <TR>
              <TH colspan = 2>
                  <INPUT type="Button" value="Cancel" onClick="window.location.href='CC_itemSumm.jsp'">&nbsp;&nbsp;
                  <INPUT class="Submit" type="BUTTON" value="Add" onClick="onAdd();">&nbsp;&nbsp;
                  <INPUT class="Submit" type="BUTTON" value="Update" onClick="onUpdate();">&nbsp;&nbsp;
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
