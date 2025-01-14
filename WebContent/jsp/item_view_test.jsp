<%@ page import="com.track.db.util.ItemUtil"%>
<%@ page import="com.track.db.util.InvUtil"%>
<%@ page import="com.track.util.*"%>
<%@ page import="com.track.dao.MovHisDAO"%>
<%@ page import="com.track.constants.*"%>
<%@ include file="header.jsp"%>

<html>
<script language="JavaScript" type="text/javascript" src="js/general.js"></script>

<title>Item Master</title>
<link rel="stylesheet" href="css/style.css">

<SCRIPT LANGUAGE="JavaScript">

var subWin = null;
function popUpWin(URL) {
 subWin = window.open(URL, 'Items', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=700,height=400,left = 200,top = 184');
}
function onNew(){
   document.form.action  = "item_view.jsp?action=NEW";
   document.form.submit();
}

function onAdd(){
   var ITEM   = document.form.ITEM.value;
   if(ITEM == "" || ITEM == null) {alert("Please Choose Item Code"); document.form.ITEM.focus(); return false; }

   document.form.action  = "item_view_test.jsp?action=ADD";
   document.form.submit();
}
function onUpdate(){
   var ITEM   = document.form.ITEM.value;
   if(ITEM == "" || ITEM == null) {alert("Please Choose Item Code"); document.form.ITEM.focus(); return false; }

   document.form.action  = "item_view.jsp?action=UPDATE";
   document.form.submit();
}
function onDelete(){
   var ITEM   = document.form.ITEM.value;
   if(ITEM == "" || ITEM == null) {alert("Please Choose Item Code"); document.form.ITEM.focus(); return false; }

   document.form.action  = "item_view.jsp?action=DELETE";
   document.form.submit();
}


function onView(){
    var ITEM   = document.form.ITEM.value;
   if(ITEM == "" || ITEM == null) {alert("Please Choose Item Code"); document.form.ITEM.focus(); return false; }

   document.form.action  = "item_view.jsp?action=VIEW";
   document.form.submit();
}
function onViewMapItem(){
   var ITEM   = document.form.ITEM.value;
   if(ITEM == "" || ITEM == null) {alert("Please Choose Item Code"); document.form.ITEM.focus(); return false; }
   document.form.action  = "item_view.jsp?action=VIEWMAPITEM";
   document.form.submit();
}
function onViewItemLoc(){
   var ITEM   = document.form.ITEM.value;
   if(ITEM == "" || ITEM == null) {alert("Please Choose Item Code"); document.form.ITEM.focus(); return false; }
   document.form.action  = "item_view.jsp?action=VIEWITEMLOC";
   document.form.submit();
}


</script>
<%
String res        = "";

String sNewEnb    = "enabled";
String sDeleteEnb = "enabled";
String sAddEnb    = "disabled";
String sUpdateEnb = "enabled";
String sItemEnb   = "enabled";
String sViewMapItemEnb = "enabled";
String sItem      = "",sItemDesc  = "", sItemUom   = "", sReOrdQty  = "",
       action     = "",sRemark1   = "" , sRemark2  = "", sRemark3   = "",
       sItemCondition = "",sArtist="",sTitle="",sMedium="",sRemark="";

StrUtils strUtils = new StrUtils();
ItemUtil itemUtil = new ItemUtil();
DateUtils dateutils = new DateUtils();

action            = strUtils.fString(request.getParameter("action"));

String plant=(String)session.getAttribute("PLANT");
String username=(String)session.getAttribute("LOGIN_USER");
sItem     = strUtils.fString(request.getParameter("ITEM"));
if(sItem.length() <= 0) sItem     = strUtils.fString(request.getParameter("ITEM1"));
sItemDesc      = strUtils.fString(request.getParameter("DESC"));
sItemDesc      = strUtils.replaceCharacters2Recv(sItemDesc);


sArtist = strUtils.fString(request.getParameter("ARTIST"));
sTitle = strUtils.fString(request.getParameter("TITLE"));
sMedium = strUtils.fString(request.getParameter("MEDIUM"));
sRemark      = strUtils.fString(request.getParameter("REMARKS"));
sItemCondition = strUtils.fString(request.getParameter("ITEM_CONDITION"));

System.out.println("action : " + action);
sAddEnb    = "enabled";
      sItemEnb   = "enabled";

if(action.equalsIgnoreCase("NEW")){
      sItem      = "";
      sItemDesc  = "";
      sArtist   = "";
      sTitle  = "";
      sMedium="";
      sRemark="";
      sItemCondition="";
      sAddEnb    = "enabled";
      sItemEnb   = "enabled";


} else if(action.equalsIgnoreCase("ADD")){
    if(!(itemUtil.isExistsItemMst(sItem,plant))) // if the item exists already
    {
          sItemDesc=strUtils.InsertQuotes(sItemDesc);
          Hashtable ht = new Hashtable();
          ht.put(IConstants.PLANT,plant);
          ht.put(IConstants.ITEM,sItem);
          ht.put(IConstants.ITEM_DESC,sItemDesc);
          ht.put(IConstants.ITEMMST_REMARK1,sRemark);
          ht.put(IConstants.ITEMMST_ITEM_ARTIST,sArtist);
          ht.put(IConstants.ITEMMST_ITEM_TITLE,sTitle);
          ht.put(IConstants.ITEMMST_REMARK2,sMedium);
          ht.put(IConstants.ITEMMST_REMARK3,sItemCondition);
          
           MovHisDAO mdao = new MovHisDAO(plant);
       Hashtable htm = new Hashtable();
          htm.put("PLANT",plant);
          htm.put("DIRTYPE","ADD_ITEM");
          htm.put("RECID","");
          htm.put("ITEM",sItem);
             htm.put("CRBY",username);
           htm.put("CRAT",dateutils.getDateTime());
           
           htm.put(IDBConstants.TRAN_DATE,dateutils.getDateinyyyy_mm_dd(dateutils.getDate()));      
         
            
          boolean itemInserted = itemUtil.insertItem(ht);
          boolean  inserted = mdao.insertIntoMovHis(htm);
          sItemDesc=strUtils.RemoveDoubleQuotesToSingle(sItemDesc);
          if(itemInserted&&inserted) {
                    res = "<font class = "+IConstants.SUCCESS_COLOR+">Item Added Successfully</font>";
                    sAddEnb  = "disabled";
                    sItemEnb = "disabled";
          } else {
                    res = "<font class = "+IConstants.FAILED_COLOR+">Failed to add New Item</font>";
                    sAddEnb  = "enabled";
                    sItemEnb = "enabled";
          }
    }else{
           res = "<font class = "+IConstants.FAILED_COLOR+">Item Exists already. Try again</font>";
           sAddEnb = "enabled";
           sItemEnb = "enabled";
    }
} else if(action.equalsIgnoreCase("UPDATE"))  {
    sItemEnb = "disabled";
    sAddEnb  = "disabled";
    if((itemUtil.isExistsItemMst(sItem,plant)))
    {
          Hashtable htCondition = new Hashtable();
          htCondition.put(IConstants.ITEM,sItem);
          htCondition.put(IConstants.PLANT,plant);
          sItemDesc=strUtils.InsertQuotes(sItemDesc);
          
          Hashtable htUpdate = new Hashtable();
          htUpdate.put(IConstants.ITEM_DESC,sItemDesc);
          htUpdate.put(IConstants.ITEMMST_REMARK1,sRemark);
          htUpdate.put(IConstants.ITEMMST_ITEM_ARTIST,sArtist);
          htUpdate.put(IConstants.ITEMMST_ITEM_TITLE,sTitle);
          htUpdate.put(IConstants.ITEMMST_ITEM_MEDIUM,sMedium);
          htUpdate.put(IConstants.ITEMMST_ITEM_CONDITION,sItemCondition);
         
      //    htUpdate.put(IConstants.STKUOM,sItemUom);
      //   htUpdate.put(IConstants.USERFLD1,sReOrdQty);
            MovHisDAO mdao = new MovHisDAO(plant);
       Hashtable htm = new Hashtable();
          htm.put("PLANT",plant);
          htm.put("DIRTYPE","UPD_ITEM");
          htm.put("RECID","");
          htm.put("ITEM",sItem);
          htm.put("CRBY",username);  
          htm.put("UPBY",username);  
           htm.put("CRAT",dateutils.getDateTime());
           htm.put("UPAT",dateutils.getDateTime());
           htm.put(IDBConstants.TRAN_DATE,dateutils.getDateinyyyy_mm_dd(dateutils.getDate()));      
                                 
         
          
          boolean itemUpdated = itemUtil.updateItem(htUpdate,htCondition);
          boolean  inserted = mdao.insertIntoMovHis(htm);
          sItemDesc=strUtils.RemoveDoubleQuotesToSingle(sItemDesc);
          if(itemUpdated&&inserted) {
                    res = "<font class = "+IConstants.SUCCESS_COLOR+">Item Updated Successfully</font>";
          } else {
                    res = "<font class = "+IConstants.FAILED_COLOR+">Failed to Update Item</font>";
          }
    }else{
           res = "<font class = "+IConstants.FAILED_COLOR+">Item doesn't not Exists. Try again</font>";

    }
} else if(action.equalsIgnoreCase("DELETE")){
    sItemEnb = "disabled";
    if(itemUtil.isExistsItemMst(sItem,plant))
    {

          Hashtable htCondition = new Hashtable();
          htCondition.put(IConstants.ITEM,sItem);
          htCondition.put(IConstants.PLANT,plant);
          boolean itemDeleted = itemUtil.deleteItem(htCondition);
          if(itemDeleted) {
                    res = "<font class = "+IConstants.SUCCESS_COLOR+">Item Deleted Successfully</font>";
                    sAddEnb    = "disabled";
                    sItem      = "";
                    sItemDesc  = "";
                    sArtist   = "";
                    sTitle  = "";
                    sMedium="";
                    sRemark="";   
                    sItemCondition="";
                    } else {
                    res = "<font class = "+IConstants.FAILED_COLOR+">Failed to Delete Item</font>";
                    sAddEnb = "enabled";
          }
    }else{
           res = "<font class = "+IConstants.FAILED_COLOR+">Item doesn't not Exists. Try again</font>";
    }
}else if(action.equalsIgnoreCase("ITEMMAPDELETE")){
    sItemEnb = "disabled";
    String keyItem = request.getParameter("keyitem");
    String mapItem = request.getParameter("mapitem");
    Hashtable htCondition = new Hashtable();
    htCondition.put(IConstants.KEY_ITEM,keyItem);
    htCondition.put(IConstants.MAP_ITEM,mapItem);
    boolean itemDeleted = itemUtil.deleteItemMap(htCondition);
    if(itemDeleted) {
              res = "<font class = "+IConstants.SUCCESS_COLOR+">Item Map Deleted Successfully</font>";
              sAddEnb    = "disabled";
              //sItem      = "";
              //sItemDesc  = "";
              //sItemUom   = "";
              //sReOrdQty  = "";
              sItem      = keyItem;
              action     = "VIEWMAPITEM";
    } else {
              res = "<font class = "+IConstants.FAILED_COLOR+">Failed to Delete Item Map</font>";
              sAddEnb = "enabled";
    }
}else if(action.equalsIgnoreCase("VIEW")){
try{
    ArrayList arrItem = itemUtil.getItemDetails(sItem);
    sItem   = (String)arrItem.get(0);
   sItemDesc   = (String)arrItem.get(1); 
   sArtist   = (String)arrItem.get(2);
   sTitle   = (String)arrItem.get(3);
   sMedium      = (String)arrItem.get(4);
   sRemark      = (String)arrItem.get(5);
   sItemCondition      = (String)arrItem.get(6);
  

   }catch(Exception e)
   {
       res="no details found for Item id : "+  sItem;
   }
 
}
else if(action.equalsIgnoreCase("PRINT")){

System.out.println("Printing  >>>>>> Starts");

com.track.dao.LblDet lblDet=new com.track.dao.LblDet();

         sItemDesc=strUtils.InsertQuotes(sItemDesc);
          Hashtable ht = new Hashtable();
          ht.put(IConstants.PLANT,IConstants.PLANT_VAL);
          ht.put(IConstants.ITEM,sItem);
          ht.put(IConstants.ITEM_DESC,sItemDesc);
          
          ht.put("SEQNO","");
          ht.put("LBLCOUNT","1");
          ht.put("LBSTATUS","N");
          
           Hashtable htCond = new Hashtable();
           
           htCond.put("PLANT","SIS");
            htCond.put("ITEM",sItem);
            // htCond.put("","");
             
           if(!(lblDet.isExists(htCond))) // if the item exists already
           {
            System.out.println("Inserting item");
            boolean itemInserted = lblDet.insertIntoLblDet(ht);
           }else{
           System.out.println("Updating  item");
            Hashtable htUpdate = new Hashtable();
           
            htUpdate.put("LBSTATUS","N");
          lblDet.updateLblDet(htUpdate,htCond);
            
            // htCond.put("","");
           }
         
          

System.out.println("Printing  >>>>>> Ends");

}

%>

<%@ include file="body.jsp"%>
<FORM name="form" method="post">
  <br>
  <CENTER>
  <TABLE border="0" width="100%" cellspacing="0" cellpadding="0" align="center" bgcolor="#dddddd">
    <TR>
      <TH BGCOLOR="#000066" COLSPAN="11"><font color="white">ITEM MASTER </font></TH>
    </TR>
  </TABLE>
  <br>
  <TABLE border="0" CELLSPACING=0 WIDTH="100%" bgcolor="#dddddd">
    <TR>
         <TH WIDTH="35%" ALIGN="RIGHT" >* Item Code : </TH>
         <TD>
                <INPUT name="ITEM" type = "TEXT" value="<%=sItem%>" size="50"  MAXLENGTH=20 <%=sItemEnb%>>
                <a href="#" onClick="javascript:popUpWin('item_list.jsp?ITEM='+form.ITEM.value);"><img src="images/populate.gif" border="0"></a>
                <INPUT type = "hidden" name="ITEM1" value = <%=sItem%>>
                <!--  <INPUT class="Submit" type="BUTTON" value="View" onClick="onView();">-->
         </TD>
    </TR>
    <TR>
         <TH WIDTH="35%" ALIGN="RIGHT" >* Item Description : </TH>
         <TD><INPUT name="DESC" type = "TEXT" value="<%=sItemDesc%>" size="50"  MAXLENGTH=80></TD>
    </TR>
   
    <TR>
         <TH WIDTH="35%" ALIGN="RIGHT" >Item Type: </TH>
         <TD><INPUT name="ARTIST" type = "TEXT" value="<%=sArtist%>" size="50"  MAXLENGTH=80></TD>
    </TR>
     <TR>
         <TH WIDTH="35%" ALIGN="RIGHT" > &nbsp;Remarks 1&nbsp;: </TH>
         <TD><INPUT name="REMARKS" type = "TEXT" value="<%=sRemark%>" size="50"  MAXLENGTH=80></TD>
    </TR>
     <TR>
         <TH WIDTH="35%" ALIGN="RIGHT" >Remarks&nbsp;2 : </TH>
         <TD><INPUT name="MEDIUM" type = "TEXT" value="<%=sMedium%>" size="50"  MAXLENGTH=80></TD>
    </TR>
     <TR>
         <TH WIDTH="35%" ALIGN="RIGHT" >Remarks&nbsp;3 : </TH>
         <TD><INPUT name="ITEM_CONDITION" type = "TEXT" value="<%=sItemCondition%>" size="50"  MAXLENGTH=80></TD>
    </TR>
     <TR>
         <TH WIDTH="35%" ALIGN="RIGHT" >Remarks&nbsp;4&nbsp;: </TH>
         <TD><INPUT name="TITLE" type = "TEXT" value="<%=sTitle%>" size="50"  MAXLENGTH=80></TD>
    </TR>
    <TR>
         <TD COLSPAN = 2><BR><B><CENTER><%=res%></B></TD>
    </TR>
    <TR>
         <TD COLSPAN = 2>
           <center>
             <INPUT class="Submit" type="BUTTON" value="Cancel" onClick="window.location.href='../home'"/>&nbsp;&nbsp;
            <!-- <INPUT class="Submit" type="BUTTON" value="New" onClick="onNew();" sNewEnb/>&nbsp;&nbsp; -->
             <INPUT class="Submit" type="BUTTON" value="Add" onClick="onAdd();" sAddEnb/>&nbsp;&nbsp;
             <INPUT class="Submit" type="BUTTON" value="Update" onClick="onUpdate();" sUpdateEnb/>&nbsp;&nbsp;
             <INPUT class="Submit" type="BUTTON" value="Delete" onClick="onDelete();" sDeleteEnb/>
             <INPUT class="Submit" type="BUTTON" value="Print Barcode" onClick="onPrint();" sDeleteEnb/>
             <!--   &lt;INPUT class=&quot;Submit&quot; type=&quot;BUTTON&quot; value=&quot;View Map Item&quot; onClick=&quot;onViewMapItem();&quot; &lt;%=sViewMapItemEnb%&gt;&gt; -->
             <!--  &lt;INPUT class=&quot;Submit&quot; type=&quot;BUTTON&quot; value=&quot;View Item Loc&quot; onClick=&quot;onViewItemLoc();&quot; &lt;%=sViewMapItemEnb%&gt;&gt; -->
           </center>
         </TD>
    </TR>
</TABLE>
<br><br>
<%if (action.equalsIgnoreCase("VIEWMAPITEM")) {%>
     <TABLE border="1" width="50%" cellspacing="0" cellpadding="0" align="center" bgcolor="#dddddd">
      <TR>
        <TH BGCOLOR="#000066" COLSPAN="11"><font color="white">View Mapping Item</font>
      </TABLE>
    <TABLE BORDER="0" CELLSPACING="1" WIDTH="50%" align="center">
      <TR bgcolor="navy">
        <TH width="20%"><font color="#ffffff">S. NO</TH>
        <TH width="40%"><font color="#ffffff">Mapping Item</TH>
        <TH width="40%"><font color="#ffffff">DELETE</TH>
     </TR>
     <%
       List listMapItem = itemUtil.getMapItems4KeyItem(sItem);
       if(listMapItem.size()>0) {
       for(int i = 0; i<listMapItem.size(); i++){
       int iIndex = i+1;
       String sBGColor = ((i == 0) || (i % 2 == 0)) ? "#FFFFFF" : "#dddddd";

       %>
      <TR bgcolor="<%=sBGColor%>">
          <TD width="20%"><%=iIndex%></TD>
          <TD width="40%"><%=(String)listMapItem.get(i)%></TD>
          <TD width="40%" align = "center"><a href="item_view.jsp?action=ITEMMAPDELETE&keyitem=<%=sItem%>&mapitem=<%=(String)listMapItem.get(i)%>&DESC=<%=strUtils.replaceCharacters2Send(sItemDesc)%>&UOM=<%=sItemUom%>&REORDQTY=<%=sReOrdQty%>">Delete</a></TD>
     </TR>
     <%} }else {%>
          <Th colspan = 2><font class = "<%=IConstants.FAILED_COLOR%>">No Mapping Item available</font></Th>
     <%} %>
     </TABLE>
<%} else if(action.equalsIgnoreCase("VIEWITEMLOC")) {%>
     <TABLE border="1" width="50%" cellspacing="0" cellpadding="0" align="center" bgcolor="#dddddd">
      <TR>
        <TH BGCOLOR="#000066" COLSPAN="11"><font color="white">View Item Location</font>
      </TABLE>
    <TABLE BORDER="0" CELLSPACING="1" WIDTH="50%" align="center">
      <TR bgcolor="navy">
        <TH width="20%"><font color="#ffffff">S. NO</TH>
        <TH width="40%"><font color="#ffffff">Location</TH>
     </TR>
     <%
       List listItemLoc = itemUtil.getLocationList4Item(sItem);
       if(listItemLoc.size()>0) {
       for(int i = 0; i<listItemLoc.size(); i++){
       int iIndex = i+1;
       String sBGColor = ((i == 0) || (i % 2 == 0)) ? "#FFFFFF" : "#dddddd";

       %>
      <TR bgcolor="<%=sBGColor%>">
          <TD width="20%"><%=iIndex%></TD>
          <TD width="40%"><%=(String)listItemLoc.get(i)%></TD>
     </TR>
     <%} }else {%>
          <Th colspan = 2><font class = "<%=IConstants.FAILED_COLOR%>">No Item Location available</font></Th>
     <%} %>
     </TABLE>

<%}%>
</TABLE>
</CENTER>

</FORM>
</BODY>
</HTML>
<script>
function onPrint(){
   var ITEM   = document.form.ITEM.value;
   var ITEMDESC = document.form.DESC.value;
   if(ITEM == "" || ITEM == null) {alert("Please Choose Item Code"); document.form.ITEM.focus(); return false; }

//   document.form.action  = "item_view.jsp?action=PRINT";
  document.form.action  = "PrintItemLabel.jsp?action=PRINT&Item="+ITEM+"&ITEMDESC="+ITEMDESC;
   alert("Do you want to Print?")
   document.form.submit();
}
</script>
<%@ include file="footer.jsp"%>

