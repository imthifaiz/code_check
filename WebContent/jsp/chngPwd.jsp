<%@page import="com.track.constants.IConstants"%>
<%@ include file="header.jsp" %>

<%
String title = "Edit Password";
%>
<%@include file="sessionCheck.jsp" %>
<jsp:include page="header2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
	<jsp:param name="mainmenu" value="<%=IConstants.SETTINGS %>" />
	<jsp:param name="submenu" value="<%=IConstants.USER_ADMIN %>" />
</jsp:include>
<link rel="stylesheet" href="../jsp/css/typeahead.css">
<link rel="stylesheet" href="../jsp/css/accounting.css">
<script language="JavaScript" type="text/javascript" src="../jsp/js/general.js"></script>
<script language="JavaScript" type="text/javascript" src="../jsp/js/pwd.js"></script>


<%
	String user_id = session.getAttribute("LOGIN_USER").toString();
    String PASSWORDTYPE=request.getParameter("PASSWORDTYPE");
    PASSWORDTYPE="CHANGEPASSWORD";
%>


<div class="container-fluid m-t-20">
	 <div class="box"> 
	 <!-- Thanzith Modified on 23.02.2022 -->
            <ul class="breadcrumb backpageul">      	
                <li class="underline-on-hover"><a href="../home">Dashboard </a></li>                       
                <li><label>Edit Password</label></li>                                   
            </ul>             
    <!-- Thanzith Modified on 23.02.2022 -->
            <div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title"><%=title %></h1>
              <h1 style="font-size: 18px; cursor: pointer;"
				class="box-title pull-right"
				onclick="window.location.href='../home'">
				<i class="glyphicon glyphicon-remove"></i>
			</h1>
		</div>
		
 <div class="box-body">
 
 <form class="form-horizontal" name="form" method="post"action="../jsp/pwdSubmit.jsp" onSubmit="return validatePwd(this)">
 <INPUT type="hidden" name="USER_ID" value="<%=user_id%>">
 <div class="form-group">
  <label class="control-label col-form-label col-sm-4 required" for="Enter Old Password">Enter Old Password</label>

   <div class="col-sm-4">
      <INPUT class="form-control" name="PASSWORD" type="password" size="50" MAXLENGTH=10 >
      	</div>
      	   </div> 
      	        

      <div class="form-group">
      <label class="control-label col-form-label col-sm-4 required" for="Enter New Password">Enter New Password</label>
      
      <div class="col-sm-4">
      <INPUT class="form-control" name="NPASSWORD" type="password" size="50" MAXLENGTH=10 >
      	</div>
      	   </div>
      	         

      <div class="form-group">
      <label class="control-label col-form-label col-sm-4 required" for="Confirm Password">Confirm Password</label>
  
      <div class="col-sm-4">
      <INPUT class="form-control" name="CPASSWORD" type="password" size="50" MAXLENGTH=10 >
      	</div>
      	  </div>
      	      
      	

      <div class="form-group">        
      <div class="col-sm-offset-4 col-sm-8">
      <!-- <button type="button" class="Submit btn btn-default" onClick="window.location.href='../home'"><b>Back</b></button>&nbsp;&nbsp; -->
      <!-- <button type="button" class="Submit btn btn-default" onClick="{backNavigation('settings.jsp','UA');}"><b>Back</b></button>&nbsp;&nbsp; -->
      <button type="Submit" class="btn btn-success" onClick="onUpdate();">Save</button>&nbsp;&nbsp;
     
      	</div>
           </div>
    
        </form>
        </div>
        </div>
        </div>
    

<jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
</jsp:include>