<%@ page import="com.track.util.*"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.gates.*"%>
<%@ page import="com.track.constants.*"%>
<%@ page import="com.track.dao.MovHisDAO"%>
<%@ page import="com.track.dao.TblControlDAO"%>
<%@ include file="header.jsp"%>

<%
String title = "Product Sub Category Details";
%>
<%@include file="sessionCheck.jsp" %>
<jsp:include page="header2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
	<jsp:param name="mainmenu" value="<%=IConstants.SETTINGS%>"/>
	<jsp:param name="submenu" value="<%=IConstants.SYSTEM_MASTER%>"/>
</jsp:include>
<link rel="stylesheet" href="../jsp/css/typeahead.css">
<link rel="stylesheet" href="../jsp/css/accounting.css">
<script language="JavaScript" type="text/javascript" src="../jsp/js/general.js"></script>


<SCRIPT LANGUAGE="JavaScript">

function popWin(URL) {
 subWin = window.open(URL, 'PRODUCT', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=700,height=400,left = 200,top = 184');
}
function onNew(){
   document.form1.action  = "../jsp/maint_prdtype.jsp?action=Clear";
   document.form1.submit();
}
function onAdd(){
   var ITEM_ID   = document.form1.ITEM_ID.value;
    if(ITEM_ID == "" || ITEM_ID == null) {alert("Please Enter Product Sub CategoryID "); return false; }
   document.form1.action  = "../jsp/maint_prdtype.jsp?action=ADD";
   document.form1.submit();
}
function onUpdate(){
 
    var ITEM_ID   = document.form1.ITEM_ID.value;
    if(ITEM_ID == "" || ITEM_ID == null) {alert("Please Enter Product Sub CategoryId "); return false; }
    var chkmsg=confirm("Are you sure you would like to update?");
    if(chkmsg){
   document.form1.action  = "../jsp/maint_prdtype.jsp?action=UPDATE";
   document.form1.submit();}
    else
    { return false;
    }
}
function onDelete(){
      var ITEM_ID   = document.form1.ITEM_ID.value;
      if(ITEM_ID == "" || ITEM_ID == null) {alert("Please Enter Product Sub CategoryId "); return false; }
    var chkmsg=confirm("Are you sure you would like to delete?");    
    if(chkmsg){
   document.form1.action  = "../jsp/maint_prdtype.jsp?action=DELETE";
   document.form1.submit();}
   else
   {
   return false;
   }
}
function onView(){
    var ITEM_ID   = document.form1.ITEM_ID.value;
   if(ITEM_ID == "" || ITEM_ID == null) {alert("Please Enter Product Sub CategoryId "); return false; }

   document.form1.action  = "../jsp/maint_prdtype.jsp?action=VIEW";
   document.form1.submit();
}
function onGenID(){
     
   document.form1.action  = "../jsp/maint_prdtype.jsp?action=Auto_ID";
   document.form1.submit();
}
</SCRIPT>
<%
String sUserId = (String) session.getAttribute("LOGIN_USER");
String sPlant = (String) session.getAttribute("PLANT");
String res        = "";

String sNewEnb    = "enabled";
String sDeleteEnb = "enabled";
String sAddEnb    = "disabled";
String sUpdateEnb = "enabled";
sAddEnb    = "enabled";
String action     =   "";
String sItemId ="",sPrdClsId  =   "",
       sItemDesc  = "",isActive="";

session= request.getSession();
StrUtils strUtils = new StrUtils();
PrdTypeUtil prdtypeutil = new PrdTypeUtil();
prdtypeutil.setmLogger(mLogger);
DateUtils dateutils = new DateUtils();
action            = strUtils.fString(request.getParameter("action"));
String plant = (String)session.getAttribute("PLANT");
sItemId  = strUtils.fString(request.getParameter("ITEM_ID"));
sItemDesc  = strUtils.replaceCharacters2Recv(strUtils.fString(request.getParameter("ITEM_DESC")));
isActive  = strUtils.fString(request.getParameter("ISACTIVE"));
if(sItemId.length() <= 0) sItemId  = strUtils.fString(request.getParameter("ITEM_ID1"));

//1. >> New
if(action.equalsIgnoreCase("Clear")){
      sItemId  = "";
      sItemDesc  = "";
      sPrdClsId ="";
}

//3. >> Update
else if(action.equalsIgnoreCase("UPDATE"))  {
   
    sAddEnb  = "disabled";
    Hashtable ht = new Hashtable();
    ht.put(IDBConstants.PRDTYPEID,sItemId);
    ht.put(IDBConstants.PLANT,sPlant);
    if((prdtypeutil.isExistsItemType(ht)))
    {
          Hashtable htUpdate = new Hashtable();
          htUpdate.put(IDBConstants.PRDTYPEID,sItemId);
          htUpdate.put(IDBConstants.PRDTYPEDESC,sItemDesc);   
          htUpdate.put(IDBConstants.UPDATED_AT,new DateUtils().getDateTime());
          htUpdate.put(IDBConstants.UPDATED_BY,sUserId);
          
          Hashtable htCondition = new Hashtable();
          htCondition.put(IDBConstants.PRDTYPEID,sItemId);
          htCondition.put(IDBConstants.PLANT,sPlant);
          
          MovHisDAO mdao = new MovHisDAO(sPlant);
          mdao.setmLogger(mLogger);
       	  Hashtable htm = new Hashtable();
          htm.put("PLANT",sPlant);
          htm.put("DIRTYPE","UPD_PRDTYPE");
          htm.put("RECID","");
          htm.put("UPBY",sUserId);   htm.put("CRBY",sUserId);
           htm.put("CRAT",dateutils.getDateTime());htm.put("REMARKS",sItemDesc);
           htm.put("UPAT",dateutils.getDateTime());
           htm.put(IDBConstants.TRAN_DATE,dateutils.getDateinyyyy_mm_dd(dateutils.getDate()));      
        
                    
        boolean Updated = prdtypeutil.updatePrdtypeId(htUpdate,htCondition);
        boolean  inserted = mdao.insertIntoMovHis(htm);
          if(true) {
                    res = "<font class = "+IDBConstants.SUCCESS_COLOR +" >Product Sub Category  Updated Successfully</font>";
          } else {
                    res = "<font class = "+IDBConstants.FAILED_COLOR +" >Failed to Update Product Sub Category </font>";
          }
    }else{
           res = "<font class = "+IDBConstants.FAILED_COLOR +">Product Sub Category doesn't not Exists. Try again</font>";

    }

}

//4. >> Delete
else if(action.equalsIgnoreCase("DELETE")){
    Hashtable htDelete = new Hashtable();
   htDelete.put(IDBConstants.PRDTYPEID,sItemId);
   htDelete.put(IDBConstants.PLANT,sPlant);
   if((prdtypeutil.isExistsItemType(htDelete)))
    {
          boolean itemDeleted = prdtypeutil.deletePrdTypeId(htDelete);
          if(true) {
                    res = "<font class = "+IDBConstants.SUCCESS_COLOR +">Product Sub Category Deleted Successfully</font>";
                    sAddEnb    = "disabled";
                    sItemId  = "";
                    sItemDesc  = "";
                    sPrdClsId ="";

          } else {
                    res = "<font class = "+IDBConstants.FAILED_COLOR +">Failed to Delete Product Sub Category </font>";
                    sAddEnb = "enabled";
          }
    }else{
           res = "<font class = "+IDBConstants.FAILED_COLOR +">Product Sub Category doesn't not Exists. Try again</font>";
    }
}
%>

<<div class="container-fluid m-t-20">
	 <div class="box"> 
	 <!-- Thanzith Modified on 23.02.2022 -->
	 <ul class="breadcrumb backpageul">    
                <li class="underline-on-hover"><a href="../home">Dashboard</a></li>	
                <li><a href="../producttype/summary"><span class="underline-on-hover">Product Sub Category Summary</span></a></li>                    
                <li><label>Product Sub Category Details</label></li>                                   
            </ul>
        <!-- Thanzith Modified on 23.02.2022 -->     
            <div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title"><%=title %></h1>
               <h1 style="font-size: 18px; cursor: pointer;"
				class="box-title pull-right"
			onclick="window.location.href='../producttype/summary'">
				<i class="glyphicon glyphicon-remove"></i>
			</h1>
		</div>
		
 <div class="box-body">
 
 
 
   <CENTER><strong><%=res%></strong></CENTER>


<form class="form-horizontal" name="form1" method="post">
    <div class="form-group">
      <label class="control-label col-sm-4" for="Product Type ID">Product Sub Category ID:</label>
      <div class="col-sm-4">
      		<input name="ITEM_ID" type="TEXT" value="<%=sItemId%>"
			size="50" MAXLENGTH=100 class="form-control" readonly>
		        
  		<INPUT type="hidden" name="ITEM_ID1" value="">			
</div>
   </div>
<div class="form-group">
      <label class="control-label col-sm-4" for="Product Type Description">Product Sub Category Description:</label>
      <div class="col-sm-4">          
        <INPUT  class="form-control" name="ITEM_DESC" type="TEXT" value="<%=sItemDesc%>"
			size="50" MAXLENGTH=100 readonly>
      </div>
      </div>
      <div class="form-group">
  <div class="col-sm-offset-4 col-sm-8">
    <label class="radio-inline">
      <input type="radio" name="ACTIVE" value="Y" disabled="disabled" <%if (isActive.equalsIgnoreCase("Y")) {%> checked <%}%> >Active
    </label>
    <label class="radio-inline">
      <input type="radio" name="ACTIVE" value="N" disabled="disabled" <%if (isActive.equalsIgnoreCase("N")) {%> checked <%}%> >Non Active
    </label>
     </div> 
</div>

<div class="form-group">        
     <div class="col-sm-offset-5 col-sm-7">
     <!-- <button type="button" class="Submit btn btn-default" onClick="window.location.href='prdtypeSummary.jsp'"><b>Back</b></button>&nbsp;&nbsp; -->
	 <!-- <button type="button" class="Submit btn btn-default" onClick="{backNavigation('settings.jsp','SM');}"><b>Back</b></button>&nbsp;&nbsp; -->
	 	</div>
	 	</div>

  </form>
</div>					
</div>
</div>

<jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
</jsp:include>



