<%@ include file="header.jsp"%>
<%@ page import="com.track.dao.TblControlDAO"%>
<%@ page import="com.track.constants.*"%>
<%@ page import="com.track.dao.MovHisDAO"%>

<%
String title = "Create Order Status";
%>
<%@include file="sessionCheck.jsp" %>
<jsp:include page="header2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
</jsp:include>

<script language="JavaScript" type="text/javascript" src="js/general.js"></script>

<SCRIPT LANGUAGE="JavaScript">


function onAdd(){
	   var STATUS_ID   = document.form1.STATUS_ID.value;
	   var STATUS_DESC = document.form1.STATUS_DESC.value;
	    if(STATUS_ID == "" || STATUS_ID == null) {alert("Please Enter Order Status ID");document.form1.STATUS_ID.focus(); return false; }
	    if(STATUS_DESC == "" || STATUS_DESC == null) {alert("Please Enter Order Status Description");document.form1.STATUS_DESC.focus(); return false; }
	   document.form1.action  = "create_orderStatus.jsp?action=ADD";
	   document.form1.submit();
	}
function onNew(){
	   document.form1.action  = "create_orderStatus.jsp?action=Clear";
	   document.form1.submit();
	}
</SCRIPT>


<%
session = request.getSession();
String sUserId = (String) session.getAttribute("LOGIN_USER");
String sPlant = (String) session.getAttribute("PLANT");
String res        = "",action     =   "",statusID="",statusDesc="";

String sNewEnb    = "enabled";
String sAddEnb    = "enabled";

session= request.getSession();
StrUtils strUtils = new StrUtils();
DateUtils dateutils = new DateUtils();
OrderStatusUtil ordstatusutil = new OrderStatusUtil();

action            = strUtils.fString(request.getParameter("action"));
String plant = (String)session.getAttribute("PLANT");
statusID  = strUtils.fString(request.getParameter("STATUS_ID"));
statusDesc  = strUtils.fString(request.getParameter("STATUS_DESC"));

 if(action.equalsIgnoreCase("Clear")){
	 statusID  = "";
	 statusDesc  = "";
     
}
 
 //2. >> Add
 else if(action.equalsIgnoreCase("ADD")){
 	

    Hashtable ht = new Hashtable();
    ht.put(IDBConstants.PLANT,sPlant);
    ht.put(IDBConstants.ORDSTATUSID,statusID);
     if(!(ordstatusutil.isExistsOrdStatus(ht))) // if the Item  exists already
     {
           ht.put(IDBConstants.PLANT,sPlant);
           ht.put(IDBConstants.ORDSTATUSID,statusID);
           ht.put(IDBConstants.ORDSTATUSDESC,statusDesc);
           ht.put(IConstants.ISACTIVE,"Y");
           ht.put(IDBConstants.CREATED_AT,new DateUtils().getDateTime());
           ht.put(IDBConstants.LOGIN_USER,sUserId);
          
           MovHisDAO mdao = new MovHisDAO(sPlant);
           mdao.setmLogger(mLogger);
       	   Hashtable htm = new Hashtable();
           htm.put("PLANT",sPlant);
         //  htm.put("DIRTYPE",TransactionConstants.ADD_ORDSTATUS);
           htm.put("RECID","");
           htm.put("ITEM",statusID);
           htm.put("REMARKS",statusDesc);
           htm.put("UPBY",sUserId);   
           htm.put("CRBY",sUserId);
           htm.put("CRAT",dateutils.getDateTime());
           htm.put("UPAT",dateutils.getDateTime());
           htm.put(IDBConstants.TRAN_DATE,dateutils.getDateinyyyy_mm_dd(dateutils.getDate()));      
                  
          
           boolean itemInserted = ordstatusutil.insertOrdStatusMst(ht);
              boolean  inserted = mdao.insertIntoMovHis(htm);
           if(itemInserted&&inserted) {
                     res = "<font class = "+IDBConstants.SUCCESS_COLOR +">Order Status Added Successfully</font>";

                  
           } else {
                     res = "<font class = "+IDBConstants.FAILED_COLOR +">Failed to add New Order Status </font>";
                   
           }
     }else{
            res = "<font class = "+IDBConstants.FAILED_COLOR +">Order Status  Exists already. Try again</font>";
           
     }
  
 }
%>
<div class="container-fluid m-t-20">
	 <div class="box"> 
            <div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title"><%=title %></h1>
		</div>
		
 <div class="box-body">

   <CENTER><strong><%=res%></strong></CENTER>

  <form class="form-horizontal" name="form1" method="post">
    <div class="form-group">
      <label class="control-label col-sm-4" for="Order Status">
      <i class="glyphicon glyphicon-star" style="font-size: 10px; top:-6px; color: #e50000"></i>&nbsp;Order Status:</label>
      <div class="col-sm-4">
    		<input name="STATUS_ID" type="TEXT" value="<%=statusID%>"
			size="50" MAXLENGTH=50 class="form-control">	  		
      </div>
    </div>
    
    <div class="form-group">
      <label class="control-label col-sm-4" for="Description">
      <i class="glyphicon glyphicon-star" style="font-size: 10px; top:-6px; color: #e50000"></i>&nbsp;Description:</label>
      <div class="col-sm-4">          
        <INPUT  class="form-control" name="STATUS_DESC" type="TEXT" value="<%=statusDesc%>"
			size="50" MAXLENGTH=100>
      </div>
    </div>
    <div class="form-group">        
      <div class="col-sm-offset-4 col-sm-8">
      <button type="button" class="Submit btn btn-default" onClick="window.location.href='../home'"><b>Back</b></button>&nbsp;&nbsp;
       <!-- <button type="button" class="Submit btn btn-default" onClick="{backNavigation('settings.jsp','OA');}"><b>Back</b></button>&nbsp;&nbsp; -->
      	<button type="button" class="Submit btn btn-default" onClick="onNew();" <%=sNewEnb%>><b>Clear</b></button>&nbsp;&nbsp;
      	<button type="button" class="Submit btn btn-default" data-toggle="modal" data-target="#myModal" onClick="onAdd();" <%=sAddEnb%>><b>Save</b></button>&nbsp;&nbsp;
      	
      </div>
    </div>
  </form>
</div>
</div>
</div>

<jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
</jsp:include>


