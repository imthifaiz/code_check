<%@ page import="com.track.util.*"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.db.util.ClearanceUtil"%>
<%@ include file="header.jsp"%>
<%@ page import="com.track.dao.TblControlDAO"%>
<%@ page import="com.track.constants.*"%>
<%@ page import="com.track.dao.MovHisDAO"%>
<%
String title = "Create Clearance Type";
%>
<%@include file="sessionCheck.jsp" %>
<jsp:include page="header2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
	<jsp:param name="mainmenu" value="<%=IConstants.SETTINGS%>"/>
	<jsp:param name="submenu" value="<%=IConstants.SYSTEM_ADMIN%>"/>
</jsp:include>
<link rel="stylesheet" href="../jsp/css/typeahead.css">
<link rel="stylesheet" href="../jsp/css/accounting.css">
<script language="JavaScript" type="text/javascript" src="../jsp/js/general.js"></script>


<SCRIPT LANGUAGE="JavaScript">


function onAdd(){
	   var CLEARANCE_TYPE_ID   = document.form1.CLEARANCE_TYPE_ID.value;
	   var TYPE = document.form1.TYPE.value;
	    if(CLEARANCE_TYPE_ID == "" || CLEARANCE_TYPE_ID == null) {alert("Please Enter Clearance Type ID");document.form1.CLEARANCE_TYPE_ID.focus(); return false; }
	   document.form1.action  = "../jsp/createClearanceType.jsp?action=ADD";
	   document.form1.submit();
	}
function onNew(){
	document.form1.CLEARANCE_TYPE_ID.value = ""; 
	document.form1.TYPE.value = "";
	}
</SCRIPT>


<%
session = request.getSession();
String sUserId = (String) session.getAttribute("LOGIN_USER");
String sPlant = (String) session.getAttribute("PLANT");
String res        = "",action     =   "",clearanceTypeID="",type="",sSAVE_RED;
String sNewEnb    = "enabled";
String sAddEnb    = "enabled";
TblControlDAO _TblControlDAO =new TblControlDAO();
session= request.getSession();
StrUtils strUtils = new StrUtils();
DateUtils dateutils = new DateUtils();
ClearanceUtil clearanceutil = new ClearanceUtil();
action            = strUtils.fString(request.getParameter("action"));
String plant = (String)session.getAttribute("PLANT");
clearanceTypeID  = strUtils.fString(request.getParameter("CLEARANCE_TYPE_ID"));
type= strUtils.fString(request.getParameter("TYPE"));
sSAVE_RED  = strUtils.fString(request.getParameter("SAVE_RED"));
 if(action.equalsIgnoreCase("Clear")){
	 clearanceTypeID  = "";
	 type  = "";
  }
 
 //1. >> Add
  if(action.equalsIgnoreCase("ADD")){
 	 Hashtable ht = new Hashtable();
    ht.put(IDBConstants.PLANT,sPlant);
    ht.put(IDBConstants.CLEARANCETYPEID,clearanceTypeID);
     if(!(clearanceutil.isExistsClearanceType(ht))) // if the Item  exists already
     {
           ht.put(IDBConstants.PLANT,sPlant);
           ht.put(IDBConstants.CLEARANCETYPEID,clearanceTypeID);
           ht.put(IDBConstants.CLEARANCETYPE,type); 
           ht.put(IDBConstants.CREATED_AT,new DateUtils().getDateTime());
           ht.put(IDBConstants.LOGIN_USER,sUserId);
           MovHisDAO mdao = new MovHisDAO(sPlant);
           mdao.setmLogger(mLogger);
           Hashtable htm = new Hashtable();
           htm.put("PLANT",sPlant);
           htm.put("DIRTYPE",TransactionConstants.ADD_CLEARANCE_TYPE);
           htm.put("RECID","");
           htm.put("ITEM",clearanceTypeID);
           if(type.equalsIgnoreCase("0"))
        	   htm.put("REMARKS","Import");
           else 
           htm.put("REMARKS","Export");
           htm.put("UPBY",sUserId);   htm.put("CRBY",sUserId);
           htm.put("CRAT",dateutils.getDateTime());
           htm.put("UPAT",dateutils.getDateTime());
           htm.put(IDBConstants.TRAN_DATE,dateutils.getDateinyyyy_mm_dd(dateutils.getDate()));      
           htm.put(IDBConstants.CREATED_AT,dateutils.getDateTime());
           htm.put(IDBConstants.TRAN_DATE,dateutils.getDateinyyyy_mm_dd(dateutils.getDate()));
           
 		  boolean updateFlag;
           
           boolean itemInserted = clearanceutil.insertClearanceTypeMst(ht);
           boolean  inserted = mdao.insertIntoMovHis(htm);
           if(itemInserted&&inserted) {
        	   sSAVE_RED="Clearance Type Added Successfully";
                  
           } else {
                     res = "<font class = "+IDBConstants.FAILED_COLOR +">Failed to add New Clearance Type </font>";
           }
     }
     else{
         res = "<font class = "+IDBConstants.FAILED_COLOR +">Clearance Type  Exists already. Try again</font>";
        
  }
  
 }
%>
<div class="container-fluid m-t-20">
	 <div class="box"> 
	 <ul class="breadcrumb backpageul">    
                <li class="underline-on-hover"><a href="../home">Dashboard</a></li>	
                 <li><a href="../clearanceType/summary"><span class="underline-on-hover">Clearance Type Summary</span></a></li>                        
                <li><label>Create Clearance Type</label></li>                                   
            </ul>   
            <div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title"><%=title %></h1>
              <h1 style="font-size: 18px; cursor: pointer;"
				class="box-title pull-right"
				onclick="window.location.href='../clearanceType/summary'">
				<i class="glyphicon glyphicon-remove"></i>
			</h1>
		</div>
		
 <div class="box-body">

   <CENTER><strong><%=res%></strong></CENTER>

  <form class="form-horizontal" name="form1" method="post">
    <div class="form-group">
      
      <label class="control-label col-form-label col-sm-4 required">Clearance Type</label>
      <div class="col-sm-4">
      	<div class="input-group">    
    		<input name="CLEARANCE_TYPE_ID" id="CLEARANCE_TYPE_ID" type="TEXT" placeholder="Max Character 200" value="<%=clearanceTypeID%>"
			size="50" MAXLENGTH=200 class="form-control">
  		</div>
  		<INPUT type="hidden" name="SAVE_RED" value="<%=sSAVE_RED%>">
      </div>
    </div>
    <div class="form-group">
      
      <label class="control-label col-form-label col-sm-4 required">Type</label>
      <div class="col-sm-8">
                      <label class="radio-inline">          
			      		<input type="radio" checked="checked" name="TYPE" value="0">Import</label>
			    	<label class="radio-inline">
			      		<input type="radio" name="TYPE" value="1">Export</label>
      </div>
    </div>
    <div class="form-group">        
      <div class="col-sm-offset-4 col-sm-8">
      	<button type="button" class="Submit btn btn-default" onClick="onNew();" <%=sNewEnb%>>Clear</button>&nbsp;&nbsp;
      	<button type="button" class="btn btn-success" data-toggle="modal" data-target="#myModal" onClick="onAdd();" <%=sAddEnb%>>Save</button>&nbsp;&nbsp;
      	      </div>
    </div>
  </form>
</div>
</div>
</div>

<script>
$(document).ready(function(){
    $('[data-toggle="tooltip"]').tooltip();
    if(document.form1.SAVE_RED.value!="")
	{
	document.form1.action  = "../clearanceType/summary?PGaction=View&result=Clearance Type Added Successfully";
	document.form1.submit();
	}
});
$(document).ready(function(){
    $('[data-toggle="tooltip"]').tooltip();   
});
</script>

<jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
</jsp:include>