
<%@ page import="com.track.db.util.ItemUtil"%>
<%@ page import="com.track.db.util.InvUtil"%>
<%@ page import="com.track.util.*"%>
<%@ page import="com.track.dao.MovHisDAO"%>
<%@ page import="com.track.dao.TblControlDAO"%>
<%@ page import="com.track.gates.*"%>
<%@ page import="com.track.util.*"%>

<%@ include file="header.jsp"%>

<%
String title = "Broadcast Message";
%>
<%@include file="sessionCheck.jsp" %>
<jsp:include page="header2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
</jsp:include>

<script language="JavaScript" type="text/javascript" src="js/general.js"></script>

<SCRIPT LANGUAGE="JavaScript">


var subWin = null;
function popUpWin(URL) {
 subWin = window.open(URL, 'Items', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=800,height=400,left = 200,top = 184');
}


function Onsave(){

   var msg   = document.form.MESSAGE.value;
  
 alert(msg);
   
   document.form.submit();
}
function onClear()
{
document.form.MESSAGE.value="";

document.form.action="broadcast.jsp?action=#";
document.form.submit();
}

</script>
<%
session= request.getSession();
String sMsg  = "",sAction="";
String res="";
if(request.getParameter("MESSAGE")==null||request.getParameter("MESSAGE")=="")
sMsg="";
else
sMsg = request.getParameter("MESSAGE");
if(request.getParameter("action")!=null)
if((request.getParameter("action")).equalsIgnoreCase("result"))
{
res="Message Broadcasted Successfully";
}

%>

<div class="container-fluid m-t-20">
	 <div class="box"> 
            <div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title"><%=title %></h1>
		</div>
		
 <div class="box-body">
 
 <div class="maingreen">
	  	<CENTER><strong><%=res%></strong></CENTER>
	</div> 
 

<FORM class="form-horizontal" name="form" method="get" action="/track/BroadCastServlet?" >
  
  <div class="form-group">
      <label class="control-label col-sm-4" for="Enter Message">Enter Message:</label>
      <div class="col-sm-4">          
        <INPUT  class="form-control" name="MESSAGE" type = "text" value="" size="30"  MAXLENGTH=100>
      </div>
    </div>
  
  
  <div class="form-group">        
     <div class="col-sm-offset-4 col-sm-8">
      <button type="button" class="Submit btn btn-default" onClick="window.location.href='../home'"><b>Back</b></button>&nbsp;&nbsp;
      	<button type="button" class="Submit btn btn-default" name="clr"  onclick="return onClear();"><b>Clear</b></button>&nbsp;&nbsp;
      	<button type="Submit" class="Submit btn btn-default" name="action"><b>Save</b></button>&nbsp;&nbsp;
  </div>
  </div>
  
  
</FORM>
</div>
</div>
</div>

<jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
</jsp:include>
