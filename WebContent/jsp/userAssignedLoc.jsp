<%@ page import="com.track.util.*"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.gates.*"%>
<%@ page import="com.track.constants.*"%>
<%@ page import="com.track.dao.TblControlDAO"%>
<%@ include file="header.jsp"%>
<%
String title = "User Location Relationship";
%>
<%@include file="sessionCheck.jsp" %>
<jsp:include page="header2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
</jsp:include>

<script language="JavaScript" type="text/javascript" src="js/general.js"></script>


<SCRIPT LANGUAGE="JavaScript">
	var subWin = null;
	

	
	function popUpWin(URL) {
 		subWin = window.open(URL, 'USERLOC', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=800,height=400,left = 200,top = 184');
	}
	
	function onAssign(){
   		var USER   = document.form1.USER.value;
 	        var LOC   = document.form1.LOC.value;
   		
   		if(USER == "" || USER == null) {alert("Please Enter/Select User"); return false; }
   		if(LOC == "" || LOC == null) {alert("Please Enter/Select Loc"); return false; }
   			
   		document.form1.action  = "/track/userlocservlet?action=Assign";
   		document.form1.submit();
	}
	

	function onUnAssign(){
   		var USER   = document.form1.USER.value;
 	        var LOC   = document.form1.LOC.value;
   		
   		if(USER == "" || USER == null) {alert("Please Enter/Select User"); return false; }
   		if(LOC == "" || LOC == null) {alert("Please Enter/Select Loc"); return false; }
   		document.form1.action  = "/track/userlocservlet?action=UnAssign";
   		document.form1.submit();
   
	}
	function onView(){
   		var USER   = document.form1.USER.value;
   		if(USER == "" || USER == null) {alert("Please Enter/Select User"); return false; }

                 document.form1.action  = "userAssignedLoc.jsp?action=VIEW";
                 document.form1.submit();
               
                    }
</script>
<%
	session = request.getSession();
	String plant = (String) session.getAttribute("PLANT");
	String sUserId = (String) session.getAttribute("LOGIN_USER");
     	String res = "";
	String sNewEnb = "enabled";
	String sDeleteEnb = "enabled";
	String sAddEnb = "enabled";
	String sUpdateEnb = "enabled";
	String sCustEnb = "enabled";
	String action = "";
	String sAssignUser = "", sLoc = "";
	StrUtils strUtils = new StrUtils();
	UserLocUtil userLocUtil = new UserLocUtil();
        ArrayList QryList = new ArrayList ();;

	try {
		 action = strUtils.fString(request.getParameter("action"));
                 sAssignUser = strUtils.fString(request.getParameter("USER"));
                 sLoc = strUtils.fString(request.getParameter("LOC"));
                 
	} catch (Exception e) {
	}
	
        
        if(action.equalsIgnoreCase("View")){
   try{
    
   
      QryList= userLocUtil.getUserLocDetails(sAssignUser,plant);
      if(QryList.size()== 0)
      {
        res="";
      }
                 
     
 }catch(Exception e) { }
}
	if (action.equalsIgnoreCase("Clear")) {
		action = "";
		sAssignUser = "";
		sLoc = "";
		
	} else if (action.equalsIgnoreCase("SHOW_RESULT")) {
		res = request.getParameter("result");
		Hashtable ht = (Hashtable) request.getSession()
				.getAttribute("userAssignedLoc");
                                
		sAssignUser = (String) ht.get("USERID");
		sLoc = (String) ht.get("LOC");
		  QryList= userLocUtil.getUserLocDetails(sAssignUser,plant);
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
      <label class="control-label col-sm-4" for="User Location">
      <i class="glyphicon glyphicon-star" style="font-size: 10px; top:-6px; color: #e50000"></i>&nbsp;User:</label>
      <div class="col-sm-4">
      	<div class="input-group">    
    		<input name="USER" type="TEXT" value="<%=sAssignUser%>"
			size="50" MAXLENGTH=100 class="form-control">
   		 	<span class="input-group-addon" 
   		          onClick="javascript:popUpWin('list/userLoc_list.jsp?TYPE=USER_LIST&USER='+form1.USER.value);"> 	
   		 	<a href="#" data-toggle="tooltip" data-placement="top" title="User Details">
   		 	<i class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span>
  		</div></div>
  		<div class="col-sm-4">
      	<div class="form-inline">
  	     	<button type="button" class="Submit btn btn-default" onClick="onView();"><b>View</b></button>&nbsp;&nbsp;
      </div>
      </div>
      </div>
    
    <div class="form-group">
      <label class="control-label col-sm-4" for="User Location">Location:</label>
      <div class="col-sm-4">
      	<div class="input-group">    
    		<input name="LOC" type="TEXT" value="<%=sLoc%>"
			size="50" MAXLENGTH=100 class="form-control">
   		 	<span class="input-group-addon" 
   		  onClick="javascript:popUpWin('list/userLoc_list.jsp?TYPE=LOC_LIST&LOC='+form1.LOC.value);"> 	
   		 	<a href="#" data-toggle="tooltip" data-placement="top" title="Location Details">
   		 	<i class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span>
  		</div>
  	     	
      </div>
    </div>

    <div class="form-group">        
      <div class="col-sm-offset-4 col-sm-8">
      <button type="button" class="Submit btn btn-default" onClick="{backNavigation('settings.jsp','SM');}"><b>Back</b></button>&nbsp;&nbsp;
        <button type="button" class="Submit btn btn-default" data-toggle="modal" data-target="#myModal" onClick="onAssign();"><b>Assign</b></button>&nbsp;&nbsp;
      	<button type="button" class="Submit btn btn-default" data-toggle="modal" data-target="#myModal" onClick="onUnAssign();"><b>UnAssign</b></button>&nbsp;&nbsp;

      </div>
    </div>
  </form>
</div>
</div>
</div>

<%
if(QryList.size()>0){%>
<div class="container">
<TABLE WIDTH="60%"  border="0" cellspacing="1" cellpadding = 2 align = "center" class="table table-bordered">
        <TR style="background:#eaeafa">
          
           <td><font  align="right"><center><b>Location</b></center></font></td>
         
       </TR>
         <%

          for (int iCnt =0; iCnt<QryList.size(); iCnt++){
          Map lineArr = (Map) QryList.get(iCnt);
          int iIndex = iCnt + 1;
          String bgcolor = ((iCnt == 0) || (iCnt % 2 == 0)) ? "#FFFFFF"  : "#dddddd";
          %>
          
           <TR bgcolor = "<%=bgcolor%>">
              <TD align="center"><%=(String)lineArr.get("LOC")%></TD>
           </TR>
        
      <% }%>  
          
       
<%}%>
    </TABLE>
    </div>

<script>
$(document).ready(function(){
    $('[data-toggle="tooltip"]').tooltip();   
});
</script>

<jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
</jsp:include>

