<%@ page import="com.track.util.*"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.gates.*"%>
<%@ page import="com.track.constants.*"%>
<%@ page import="com.track.dao.TblControlDAO"%>
<%@ include file="header.jsp"%>
<%
String title = "Order Type Details";
%>
<%@include file="sessionCheck.jsp" %>
<jsp:include page="header2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
	<jsp:param name="mainmenu" value="<%=IConstants.SETTINGS%>"/>
	<jsp:param name="submenu" value="<%=IConstants.ORDER_ADMIN%>"/>
</jsp:include>
<link rel="stylesheet" href="../jsp/css/typeahead.css">
<link rel="stylesheet" href="../jsp/css/accounting.css">
<script language="JavaScript" type="text/javascript" src="../jsp/js/general.js"></script>

<SCRIPT LANGUAGE="JavaScript">
	var subWin = null;
	function popUpWin(URL) {
	 subWin = window.open(URL, 'ORDERTYPE', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=800,height=400,left = 200,top = 184');
	}
	function onNew(){
   	 document.form1.action  = "/Wms/OrderTypeServlet?action=NEW";
   	 document.form1.submit();
	}
	function onGenID(){
	 document.form1.action  = "../jsp/OrderType_View.jsp?action=Auto_ID";
 	 document.form1.submit();
   }
   function onAdd(){
     var ORDERTYPE   = document.form1.ORDERTYPE.value;
     if(ORDERTYPE == "" || ORDERTYPE == null) {alert("Please Enter OrderType"); return false; }
     document.form1.action  = "/Wms/OrderTypeServlet?action=ADD";
     document.form1.submit();
	}
	function onUpdate(){
   	 var ORDERTYPE   = document.form1.ORDERTYPE.value;
     if(ORDERTYPE == "" || ORDERTYPE == null) {alert("Please Enter OrderType"); return false; }
     document.form1.action  = "/Wms/OrderTypeServlet?action=UPDATE&ORDERTYPE=" + ORDERTYPE;
     document.form1.submit();
	}

	function onDelete(){
   	 var ORDERTYPE   = document.form1.ORDERTYPE.value;
     if(ORDERTYPE == "" || ORDERTYPE == null) {alert("Please Enter OrderType");  return false; }
     confirm("Do you want to delete OrderType?");
     document.form1.action  = "/Wms/OrderTypeServlet?action=DELETE&ORDERTYPE=" + ORDERTYPE;
     document.form1.submit();
    }

	function onView(){

   	 var ORDERTYPE   = document.form1.ORDERTYPE.value;
     if(ORDERTYPE == "" || ORDERTYPE == null) 
     {
      alert("Please Enter OrderType"); 
      return false; 
     }

     document.form1.action  = "/Wms/OrderTypeServlet?action=VIEW";
     document.form1.submit();
    }
</script>
<%
	session = request.getSession();
	String plant = (String) session.getAttribute("PLANT");
	String sUserId = (String) session.getAttribute("LOGIN_USER");
	String res        = "";

	String sNewEnb    = "enabled";
	String sDeleteEnb = "enabled";
	String   sAddEnb    = "enabled";
	String sUpdateEnb = "enabled";
	String sCustEnb   = "enabled";

	String action     = "";
	String sOrderType  = "",
    isActive  = "",
    sOrderDesc  = "",
    sType  = "",
    sRemarks     = "";
	StrUtils strUtils = new StrUtils();
	CustUtil custUtil = new CustUtil();

	try	{action= strUtils.fString(request.getParameter("action"));}
	catch(Exception e){	}


	//1. >> New
	if(action.equalsIgnoreCase("Clear")){
  	   System.out.println("Action : " + action) ;
       action     = "";
       sOrderType  = "";
       sOrderDesc = "";
       sType = "";
       sRemarks     = "";
       
     }
    else if(action.equalsIgnoreCase("view")){
   	 	sOrderType  = request.getParameter("ORDERTYPE");
     	sOrderDesc   = strUtils.replaceCharacters2Recv(request.getParameter("ORDERDESC"));
     	sType   = request.getParameter("TYPE");
     	sRemarks      = strUtils.replaceCharacters2Recv(request.getParameter("USERFLD1"));
        isActive      = request.getParameter("ISACTIVE");
        sRemarks = strUtils.replaceCharacters2Recv(request.getParameter("REMARKS"));
        
}

	else if(action.equalsIgnoreCase("SHOW_RESULT")){

    	res =request.getParameter("result");
    	Hashtable arrCust=(Hashtable)request.getSession().getAttribute("orderTypeData");
    	sOrderType  = (String)arrCust.get("ORDERTYPE");
    	sOrderDesc   = strUtils.replaceCharacters2Recv((String)arrCust.get("ORDERDESC"));
    	sType   = (String)arrCust.get("TYPE");
      	sRemarks      = strUtils.replaceCharacters2Recv((String)arrCust.get("USERFLD1"));
       	isActive      = request.getParameter("ISACTIVE");
  
	}
	else if(action.equalsIgnoreCase("UPDATE")){
  		sCustEnb    = "disabled";
  
	}


%>
<div class="container-fluid m-t-20">
	 <div class="box"> 
	 <!-- Thanzith Modified on 23.02.2022 -->
	 <ul class="breadcrumb backpageul">    
                <li class="underline-on-hover"><a href="../home">Dashboard</a></li>	
                <li><a href="../ordertype/summary"><span class="underline-on-hover">Order Type Summary</span></a></li>                       
                <li><label>Order Type Details</label></li>                                   
            </ul>
        <!-- Thanzith Modified on 23.02.2022 -->     
            <div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title"><%=title %></h1>
              <h1 style="font-size: 18px; cursor: pointer;"
				class="box-title pull-right"
			onclick="window.location.href='../ordertype/summary'">
				<i class="glyphicon glyphicon-remove"></i>
			</h1>
		</div>
		
 <div class="box-body">

   <CENTER><strong><%=res%></strong></CENTER>

  <form class="form-horizontal" name="form1" method="post">
    <div class="form-group">
      <label class="control-label col-form-label col-sm-4" for="Order Type Master">Order Type</label>
      <div class="col-sm-4">
      	    
    		<input name="ORDERTYPE" type="TEXT" value="<%=sOrderType%>"
			size="50" MAXLENGTH=100 class="form-control"<%=sCustEnb%> readonly>
   		 	
  		<INPUT type="hidden" name="ORDERTYPE1" value="<%=sOrderType%>"> 
       </div>
    </div>
 
    <div class="form-group">
      <label class="control-label col-form-label col-sm-4" for="Order Type Description">Description</label>
      <div class="col-sm-4">          
        <INPUT  class="form-control" name="ORDERDESC" type="TEXT" value="<%=sOrderDesc%>"
			size="50" MAXLENGTH=100 readonly>
      </div>
    </div>
 
     <div class="form-group">
      <label class="control-label col-form-label col-sm-4" for="Order Type">Type</label>
      <div class="col-sm-4">           	
  	<INPUT name="TYPE" type = "TEXT" value="<%=sType%>" size="50"  MAXLENGTH=80 class="form-control" readonly>
  	</div>
  	  </div>
 
      <div class="form-group">
      <label class="control-label col-form-label col-sm-4" for="Remarks">Remarks</label>
      <div class="col-sm-4">          
        <INPUT  class="form-control" name="REMARKS" type="TEXT" value="<%=sRemarks%>"
			size="50" MAXLENGTH=100 readonly>
      </div>
    </div>
 
    <div class="form-group">
    <div class="col-sm-offset-4 col-sm-8">
    <label class="radio-inline">
      <input type="radio" name="ACTIVE" value="Y" disabled="disabled" <%if (isActive.equalsIgnoreCase("Y")) {%> checked <%}%>>Active
    </label>
    <label class="radio-inline">
      <input type="radio" name="ACTIVE" value="N" disabled="disabled" <%if (isActive.equalsIgnoreCase("N")) {%> checked <%}%>>Non Active
    </label>
     </div>
</div>
<div class="form-group">        
     <div class="col-sm-offset-5 col-sm-7">
     <!-- <button type="button" class="Submit btn btn-default" onClick="window.location.href='OrderTypeSummary.jsp'"><b>Back</b></button>&nbsp;&nbsp; -->
      <!-- <button type="button" class="Submit btn btn-default" onClick="{backNavigation('settings.jsp','OA');}"><b>Back</b></button>&nbsp;&nbsp; -->
</div>
</div>
  </form>
</div>
</div>
</div>


<jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
</jsp:include>