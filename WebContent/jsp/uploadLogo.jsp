<%@ page import="com.track.util.*"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.gates.*"%>
<%@ page import="com.track.constants.*"%>
<%@ page import="com.track.dao.TblControlDAO"%>
<%@ include file="header.jsp"%>

<%
String title = "Upload Company Logo";
%>
<%@include file="sessionCheck.jsp" %>
<jsp:include page="header2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
</jsp:include>
<script language="JavaScript" type="text/javascript" src="js/general.js"></script>


<SCRIPT LANGUAGE="JavaScript">
var subWin = null;
function popUpWin(URL) {
 	subWin = window.open(URL, 'Items', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=500,height=200,left = 200,top = 184');
}

	function onAddLogo1(){
		
		
		if(document.form.IMAGE_UPLOAD1.value.length<0){
			alert('Please browse image');
   	   	    return false;
	   		
		}
		else
			
		{
   			document.form.action  = "/track/CatalogServlet?Submit=UPLOAD_LOGO&Type=Logo";
   		    document.form.submit();
   	        return true;
   	    }
   	
	}
function onAddLogo2(){
		
		
		if(document.form.IMAGE_UPLOAD2.value.length<0){
			alert('Please browse image');
   	   	    return false;
	   		
		}
		else
			
		{
   			document.form.action  = "/track/CatalogServlet?Submit=UPLOAD_LOGO&Type=Logo1";
   		    document.form.submit();
   	        return true;
   	    }
   	
	}
	function onTestPreview()
	{
		var plant = document.form.PLANT.value;
		document.form.action  = "/track/DynamicFileServlet?action=PREVIEW_LOGO_ON_PAGE"+"&plant="+plant;
		document.form.submit();
			
	}     
	
        
        
</script>

<%
	session = request.getSession();
	String plant = (String) session.getAttribute("PLANT");
	String sUserId = (String) session.getAttribute("LOGIN_USER");
        
	String res = "";
	
	String action = "";
	
	String logopath1="",logopath2="";
	
	StrUtils strUtils = new StrUtils();
	
	res =  strUtils.fString(request.getParameter("result"));
	
%>

<div class="container-fluid m-t-20">
	 <div class="box"> 
            <div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title"><%=title %></h1>
		</div>
		
 <div class="box-body">
 
   <CENTER><strong><%=res%></strong></CENTER>

 <form class="form-horizontal" name="form" method="post" enctype="multipart/form-data" action="">
 <input type="hidden" name="PLANT" value="<%=plant%>">
 
 <div class="form-group">
      <label class="control-label col-sm-4" for="Upload Logo1">Upload Logo1:</label>
      <div class="col-sm-4">          
        <INPUT  class="form-control" name="IMAGE_UPLOAD1" type="File" size="20" MAXLENGTH=100>
        <!--  <INPUT class="Submit" type="BUTTON"
			value="Preview Catalog Image" onClick="onAddLogo1();" >  -->
      </div>
      <div class="form-inline">
      <button type="button" class="Submit btn btn-default" value="Upload Logo1" onClick="onAddLogo1();"><b>Upload Logo1</b></button>&nbsp;&nbsp;
    </div>
    </div>
    
    <div class="form-group">
      <label class="control-label col-sm-4" for="Upload Logo2">Upload Logo2:</label>
      <div class="col-sm-4">          
        <INPUT  class="form-control" name="IMAGE_UPLOAD2" type="File" size="20" MAXLENGTH=100>
      </div>
      <div class="form-inline">
      <button type="button" class="Submit btn btn-default" value="Upload Logo2" onClick="onAddLogo2();"><b>Upload Logo2</b></button>&nbsp;&nbsp;
    </div>
    </div>
    
    <TR>
		<TH WIDTH="35%" ALIGN="RIGHT"></TH>
		<TD><font color=blue><center>*Note :Upload only Images,Dimensions should 400(w) X 200(h) in range</center></font></TD>

	</TR>
	
	<br>
	
	<div class="form-group">        
      <div class="col-sm-offset-4 col-sm-8">
      
      	<button type="button" class="Submit btn btn-default" value="Back" onClick="window.location.href='importExcelPage.jsp'"><b>Back</b></button>&nbsp;&nbsp;
      	<button type="button" class="Submit btn btn-default" value="Test Preview" onClick="onTestPreview();" ><b>Test Preview</b></button>&nbsp;&nbsp;
	</div>
	</div>
	
	
	</form>
	</div>
	</div>
	</div>
	

<script type="text/javascript">

</script>

<jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
</jsp:include>
