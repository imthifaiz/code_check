<%@ page import="com.track.util.*"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.constants.*"%>
<%@ include file="header.jsp"%>


<%
String title = "New Bank";
String systatus = session.getAttribute("SYSTEMNOW").toString();
String mainmenu="";
if(systatus.equalsIgnoreCase("PAYROLL")) { 
	mainmenu = IConstants.SETTINGS; 
	} else { 
		mainmenu = IConstants.ACCOUNTING; 
	}
%>
<%@include file="sessionCheck.jsp" %>
<jsp:include page="header2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
	<jsp:param name="mainmenu" value="<%=mainmenu%>"/>
    <jsp:param name="submenu" value="<%=IConstants.BANKING%>"/>
</jsp:include>
<script src="../jsp/js/typeahead.jquery.js"></script>
<link rel="stylesheet" href="../jsp/css/typeahead.css">
<link rel="stylesheet" href="../jsp/css/accounting.css">
<link rel="stylesheet" href="../jsp/css/bootstrap-datepicker.css">
<script src="../jsp/js/bootstrap-datepicker.min.js"></script>
<script language="JavaScript" type="text/javascript" src="../jsp/js/general.js"></script>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

<SCRIPT LANGUAGE="JavaScript">


function onGenID(){
    
	   document.form1.action  = "/track/BankServlet?action=Auto_ID";
	   document.form1.submit();
	}
function onAdd(){
	   var BANK_NAME   = document.form1.BANK_NAME.value;
	//   var BANK_BRANCH = document.form1.BANK_BRANCH.value;
	   var BANK_BRANCH_CODE = document.form1.BANK_BRANCH_CODE.value;
	    if(BANK_NAME == "" || BANK_NAME == null) {alert("Please Enter Bank Name");document.form1.BANK_NAME.focus(); return false; }
	//    if(BANK_BRANCH == "" || BANK_BRANCH == null) {alert("Please Enter Bank Branch");document.form1.BANK_BRANCH.focus(); return false; }
	    if(BANK_BRANCH_CODE == "" || BANK_BRANCH_CODE == null) {alert("Please Enter Bank Branch Code");document.form1.BANK_BRANCH_CODE.focus(); return false; }
	   document.form1.action  = "/track/BankServlet?action=ADD";
	   document.form1.submit();
	}

function onClear(){
	document.form1.BANK_NAME.value = "";
	document.form1.BANK_BRANCH.value = "";
	document.form1.BANK_BRANCH_CODE.value = "";
}
</SCRIPT>


<%
String responseMsg = request.getParameter("response");
String BANK_NAME = request.getParameter("BANK_NAME");
String BANK_BRANCH = request.getParameter("BANK_BRANCH");
String BANK_BRANCH_CODE = request.getParameter("BANK_BRANCH_CODE");
String IFSC = request.getParameter("IFSC");
String SWIFT_CODE = request.getParameter("SWIFT_CODE");
String TELNO = request.getParameter("TELNO");
String FAX = request.getParameter("FAX");
String EMAIL = request.getParameter("EMAIL");
String WEBSITE = request.getParameter("WEBSITE");
String UNITNO = request.getParameter("UNITNO");
String BUILDING = request.getParameter("BUILDING");
String STREET = request.getParameter("STREET");
String CITY = request.getParameter("CITY");
String STATE = request.getParameter("STATE");
String COUNTRY = request.getParameter("COUNTRY");
String ZIP = request.getParameter("ZIP");
String FACEBOOKID = request.getParameter("FACEBOOKID");
String TWITTERID = request.getParameter("TWITTERID");
String LINKEDINID = request.getParameter("LINKEDINID");
String CONTACT_PERSON = request.getParameter("CONTACT_PERSON");
String HPNO = request.getParameter("HPNO");
String NOTE = request.getParameter("NOTE");
if(responseMsg == null)
	responseMsg = "";

if(BANK_NAME == null)
	BANK_NAME = "";

if(BANK_BRANCH == null)
	BANK_BRANCH = "";
if(BANK_BRANCH_CODE == null)
	BANK_BRANCH_CODE = "";
if(IFSC == null)
	IFSC = "";

if(SWIFT_CODE == null)
	SWIFT_CODE = "";
if(TELNO == null)
	TELNO = "";
if(FAX == null)
		FAX = "";

	if(EMAIL == null)
		EMAIL = "";
	if(WEBSITE == null)
		WEBSITE = "";
	if(UNITNO == null)
		UNITNO = "";
	if(BUILDING == null)
		BUILDING = "";
	if(STREET == null)
		STREET = "";
	if(CITY == null)
		CITY = "";
	if(STATE == null)
		STATE = "";
	if(COUNTRY == null)
		COUNTRY = "";
	if(ZIP == null)
		ZIP = "";
	if(FACEBOOKID == null)
		FACEBOOKID = "";
	if(TWITTERID == null)
		TWITTERID = "";
	if(LINKEDINID == null)
		LINKEDINID = "";
	if(CONTACT_PERSON == null)
		CONTACT_PERSON = "";
	if(HPNO == null)
		HPNO = "";
	if(NOTE == null)
		NOTE = "";
      
%>
<div class="container-fluid m-t-20">
	 <div class="box"> 
	 <!-- Muruganantham Modified on 16.02.2022 -->
            <ul class="breadcrumb backpageul">      	
                <li><a href="../home"><span class="underline-on-hover">Dashboard</span> </a></li>   
                <li><a href="../banking/summary"><span class="underline-on-hover">Bank Summary</span> </a></li>                      
                <li><label>New Bank</label></li>                                   
            </ul>             
    <!-- Muruganantham Modified on 16.02.2022 -->
            <div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title"><%=title %></h1>
              <h1 style="font-size: 18px; cursor: pointer;"	class="box-title pull-right" onclick="window.location.href='../banking/summary'">
				<i class="glyphicon glyphicon-remove"></i>
			</h1>
		</div>
		
 <div class="box-body">
 
 
   <CENTER><strong><%=responseMsg%></strong></CENTER>

  <form class="form-horizontal" name="form1" method="post" action = "/track/BankServlet">
    
    <div class="form-group">
			<label class="control-label col-form-label col-sm-2 required">Bank Name</label>
			<div class="col-sm-4">
				<input name="BANK_NAME" type="TEXT" value="<%=BANK_NAME%>"	MAXLENGTH=100 class="form-control">
			</div>
		</div>
		
		<div class="form-group">
			<label class="control-label col-form-label col-sm-2">Branch</label>
			<div class="col-sm-4">
				<INPUT  class="form-control" name="BANK_BRANCH" type="TEXT" value="<%=BANK_BRANCH%>" MAXLENGTH=100>
			</div>
		</div>
		
			<div class="form-group">
			<label class="control-label col-form-label col-sm-2 required">Branch Code</label>
			<div class="col-sm-4">
				<INPUT  class="form-control" name="BANK_BRANCH_CODE" type="TEXT" value="<%=BANK_BRANCH_CODE%>" MAXLENGTH=100>
			</div>
		</div>
    
    <div class="form-group">
			<label class="control-label col-form-label col-sm-2">IFSC Code</label>
			<div class="col-sm-4">				
				<INPUT  class="form-control" name="IFSC" type="TEXT" value="<%=IFSC%>" MAXLENGTH=50>
			</div>
		</div>
		
				    <div class="form-group">
			<label class="control-label col-form-label col-sm-2">Swift Code</label>
			<div class="col-sm-4">				
				<INPUT  class="form-control" name="SWIFT_CODE" type="TEXT" value="<%=SWIFT_CODE%>" MAXLENGTH=50>
			</div>
		</div>
		
		    <div class="form-group">
			<label class="control-label col-form-label col-sm-2">Tel No</label>
			<div class="col-sm-4">				
				<INPUT  class="form-control" name="TELNO" type="TEXT" value="<%=TELNO%>" MAXLENGTH=30>
			</div>
		</div>
		    <div class="form-group">
			<label class="control-label col-form-label col-sm-2">Fax</label>
			<div class="col-sm-4">				
				<INPUT  class="form-control" name="FAX" type="TEXT" value="<%=FAX%>" MAXLENGTH=30>
			</div>
		</div>

		    <div class="form-group">
			<label class="control-label col-form-label col-sm-2">Email</label>
			<div class="col-sm-4">				
				<INPUT  class="form-control" name="EMAIL" type="TEXT" value="<%=EMAIL%>" MAXLENGTH=50>
			</div>
			</div>
			
			<div class="form-group">
			<label class="control-label col-form-label col-sm-2">Web Site</label>
			<div class="col-sm-4">				
				<INPUT  class="form-control" name="WEBSITE" type="TEXT" value="<%=WEBSITE%>" MAXLENGTH=50>
			</div>
		</div>	    	
		
		<div class="bs-example">
     <ul class="nav nav-tabs" id="myTab"> 
          <li class="nav-item active">
            <a href="#home" class="nav-link" data-toggle="tab" aria-expanded="true">Address</a>
        </li>
        <li class="nav-item">
            <a href="#profile" class="nav-link" data-toggle="tab">Contact Details</a>
        </li>
        <li class="nav-item">
            <a href="#other" class="nav-link" data-toggle="tab">Other Details</a>
        </li>
        <li class="nav-item">
            <a href="#remark" class="nav-link" data-toggle="tab">Remarks</a>
        </li>
        </ul>
    <div class="tab-content clearfix">
        <div class="tab-pane active" id="home">
        <br>
       <div class="form-group">
			<label class="control-label col-form-label col-sm-2">Unit No</label>
			<div class="col-sm-4">				
			<INPUT  class="form-control" name="UNITNO" type="TEXT" value="<%=UNITNO%>" MAXLENGTH=50>
			</div>
		</div>
		       <div class="form-group">
			<label class="control-label col-form-label col-sm-2">Building</label>
			<div class="col-sm-4">				
			<INPUT  class="form-control" name="BUILDING" type="TEXT" value="<%=BUILDING%>" MAXLENGTH=50>
			</div>
		</div>
		
		       <div class="form-group">
			<label class="control-label col-form-label col-sm-2">Street</label>
			<div class="col-sm-4">				
			<INPUT  class="form-control" name="STREET" type="TEXT" value="<%=STREET%>" MAXLENGTH=100>
			</div>
		</div>
		
		       <div class="form-group">
			<label class="control-label col-form-label col-sm-2">City</label>
			<div class="col-sm-4">				
			<INPUT  class="form-control" name="CITY" type="TEXT" value="<%=CITY%>" MAXLENGTH=50>
			</div>
		</div>
		
		       <div class="form-group">
			<label class="control-label col-form-label col-sm-2">State</label>
			<div class="col-sm-4">				
			<INPUT  class="form-control" name="STATE" type="TEXT" value="<%=STATE%>" MAXLENGTH=50>
			</div>
		</div>
		
		<div class="form-group">
			<label class="control-label col-form-label col-sm-2">Country</label>
			<div class="col-sm-4">				
			<INPUT  class="form-control" name="COUNTRY" type="TEXT" value="<%=COUNTRY%>" MAXLENGTH=50>
			</div>
		</div>
		
		       <div class="form-group">
			<label class="control-label col-form-label col-sm-2">Postal Code</label>
			<div class="col-sm-4">				
			<INPUT  class="form-control" name="ZIP" type="TEXT" value="<%=ZIP%>" MAXLENGTH=10>
			</div>
		</div>
		     
        </div>
        
        <div class="tab-pane fade" id="profile">
        <br>
            <div class="form-group">
			<label class="control-label col-form-label col-sm-2">Contact Person</label>
			<div class="col-sm-4">				
			<INPUT  class="form-control" name="CONTACT_PERSON" type="TEXT" value="<%=CONTACT_PERSON%>" MAXLENGTH=50>
			</div>
		</div>
		
		<div class="form-group">
			<label class="control-label col-form-label col-sm-2">Hand Phone</label>
			<div class="col-sm-4">				
			<INPUT  class="form-control" name="HPNO" type="TEXT" value="<%=HPNO%>" MAXLENGTH=30>
			</div>
		</div>
        </div>
         <div class="tab-pane fade" id="other">
         <br>
            <div class="form-group">
			<label class="control-label col-form-label col-sm-2">Facebook</label>
			<div class="col-sm-4">				
			<INPUT  class="form-control" name="FACEBOOKID" type="TEXT" value="<%=FACEBOOKID%>" MAXLENGTH=50>
			</div>
		</div>
		
		            <div class="form-group">
			<label class="control-label col-form-label col-sm-2">Twitter Id</label>
			<div class="col-sm-4">				
			<INPUT  class="form-control" name="TWITTERID" type="TEXT" value="<%=TWITTERID%>" MAXLENGTH=50>
			</div>
		</div>
		
		            <div class="form-group">
			<label class="control-label col-form-label col-sm-2">LinkedIn Id </label>
			<div class="col-sm-4">				
			<INPUT  class="form-control" name="LINKEDINID" type="TEXT" value="<%=LINKEDINID%>" MAXLENGTH=50>
			</div>
		</div>
        </div>
         <div class="tab-pane fade" id="remark">
         <br>
            <div class="form-group">
			<label class="control-label col-form-label col-sm-2">Remarks</label>
			<div class="col-sm-4">				
			 <%-- <INPUT  class="form-control" name="NOTE" type="TEXT" value="<%=NOTE%>" MAXLENGTH=1000>  --%>
			 <textarea  class="form-control" name="NOTE"  MAXLENGTH=1000><%=NOTE%></textarea> 
			</div>
		</div>
        </div>
        </div>
        </div>
<br>
    <div class="row">
			<div class="col-sm-offset-4 col-sm-12 txn-buttons">
			<button type="button" class="Submit btn btn-default" onClick="window.location.href='../home'">Back</button>&nbsp;&nbsp;
			<button type="button" class="btn btn-success" onClick="onAdd();">Save</button>
		</div>
		</div>
  </form>
</div>
</div>
</div>


<script>
$(document).ready(function(){
    $('[data-toggle="tooltip"]').tooltip();   
});

/* $(".nav li").on("click", function(){
    $(".nav li").removeClass("active");
    $(this).addClass("active");
}); */
</script>

<style>
	.bs-example{
    	margin: 20px;
    }
</style>
<script>
    $(document).ready(function(){ 
        $("#myTab a").click(function(e){
            e.preventDefault();
            $(this).tab('show');
        });
    });
</script>
 
<jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
</jsp:include>
