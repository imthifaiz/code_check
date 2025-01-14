<%@ page import="com.track.util.*"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.gates.*"%>
<%@ page import="com.track.constants.*"%>
<%@ page import="com.track.dao.CustMstDAO"%>
<%@ page import="com.track.dao.MovHisDAO"%>
<%@ page import="com.track.dao.TblControlDAO"%>
<%@ include file="header.jsp"%>

<%
String title = "Employee Details";
%>
<%@include file="sessionCheck.jsp" %>
<jsp:include page="header2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
</jsp:include>

<script language="JavaScript" type="text/javascript" src="js/general.js"></script>

<SCRIPT LANGUAGE="JavaScript">

var subWin = null;
function popUpWin(URL) {
 subWin = window.open(URL, 'Employee', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=700,height=400,left = 200,top = 184');
}
function onNew(){

   document.form.action  = "maint_customer.jsp?action=NEW";
   document.form.submit();
}
function onAdd(){
   var CUST_CODE   = document.form.CUST_CODE.value;
   var CUST_NAME   = document.form.CUST_NAME.value;
   if(CUST_CODE == "" || CUST_CODE == null) {alert("Please Choose Customer Code"); return false; }
   
   if(CUST_NAME == "" || CUST_NAME == null) {
   alert("Please enter Customer Name"); 
   document.form.CUST_NAME.focus();
   return false; 
   }
   
   document.form.action  = "maint_customer.jsp?action=ADD";
   document.form.submit();
}
function onUpdate(){
   var CUST_CODE   = document.form.CUST_CODE.value;
   if(CUST_CODE == "" || CUST_CODE == null) {alert("Please Enter Customer Code"); return false; }

   document.form.action  = "maint_customer.jsp?action=UPDATE";
   document.form.submit();
}
function onDelete(){

   var CUST_CODE   = document.form.CUST_CODE.value;
   if(CUST_CODE == "" || CUST_CODE == null) {alert("Please Enter Customer Code");  return false; }
   confirm("Do you want to delete Customer permanently ");
   document.form.action  = "maint_customer.jsp?action=DELETE";
   document.form.submit();
}
function onView(){
   var CUST_CODE   = document.form.CUST_CODE.value;
   if(CUST_CODE == "" || CUST_CODE == null) {alert("Please Enter Customer Code"); return false; }

   document.form.action  = "maint_customer.jsp?action=VIEW";
   document.form.submit();
}
function onIDGen()
{
 	document.form.action  = "maint_customer.jsp?action=Auto-ID";
    document.form.submit(); 

}

</script>
<%
String sUserId = (String) session.getAttribute("LOGIN_USER");
String res        = "";

String sNewEnb    = "enabled";
String sDeleteEnb = "enabled";
String sAddEnb    = "enabled";
String sUpdateEnb = "enabled";
String sCustEnb   = "enabled";
String action     = "";
String sCustCode  = "",
sCustName  = "",
sCustNameL  = "",
sAddr1     = "",
sAddr2     = "",
sAddr3     = "", sAddr4     = "",
sState   = "",
sCountry   = "",
sZip       = "",
sDept      = "",isActive="";
String sDesgination="",sTelNo="",sHpNo="",sFax="",sEmail="",sRemarks="",sNationality="",sDOB="",sGender="",DOB="";
StrUtils strUtils = new StrUtils();
	CustUtil custUtil = new CustUtil();
	DateUtils dateutils = new DateUtils();
	
	action            = strUtils.fString(request.getParameter("action"));
	String plant = strUtils.fString((String)session.getAttribute("PLANT"));


		
	sCustCode  = strUtils.fString(request.getParameter("CUST_CODE"));
	sCustName  = strUtils.InsertQuotes(strUtils.fString(request.getParameter("CUST_NAME")));
	sCustNameL  = strUtils.InsertQuotes(strUtils.fString(request.getParameter("L_CUST_NAME")));
	sAddr1     = strUtils.replaceCharacters2Recv(strUtils.fString(request.getParameter("ADDR1")));
	sAddr2     = strUtils.replaceCharacters2Recv(strUtils.fString(request.getParameter("ADDR2")));
	sAddr3     = strUtils.replaceCharacters2Recv(strUtils.fString(request.getParameter("ADDR3")));
	sAddr4     = strUtils.replaceCharacters2Recv(strUtils.fString(request.getParameter("ADDR4")));
	sState   = strUtils.InsertQuotes(strUtils.fString(request.getParameter("STATE")));
	sCountry   = strUtils.InsertQuotes(strUtils.fString(request.getParameter("COUNTRY")));
	sZip       = strUtils.fString(request.getParameter("ZIP"));
	sDept      = strUtils.fString(request.getParameter("DEPT"));
	sNationality      = strUtils.fString(request.getParameter("NATIONALITY"));
	sDesgination  = strUtils.fString(request.getParameter("DESGINATION"));
	sDOB = strUtils.fString(request.getParameter("DOB"));
	sGender = strUtils.fString(request.getParameter("GENDER"));
	sTelNo  = strUtils.fString(request.getParameter("TELNO"));
	sHpNo  = strUtils.fString(request.getParameter("HPNO"));
	sFax  = strUtils.fString(request.getParameter("FAX"));
	sEmail= strUtils.replaceCharacters2Recv(strUtils.fString(request.getParameter("EMAIL")));
	sRemarks= strUtils.InsertQuotes(strUtils.fString(request.getParameter("REMARKS")));
	if(sDOB.length() > 5){
		sDOB = sDOB.substring(8,10) +"/"+ sDOB.substring(5, 7) +"/"+  sDOB.substring(0, 4);
	}
	isActive= strUtils.fString(request.getParameter("ISACTIVE"));
	

%>

<div class="container-fluid m-t-20">
	 <div class="box"> 
            <div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title"><%=title %></h1>
		</div>
		
 <div class="box-body">

   <CENTER><strong><%=res%></strong></CENTER>

  <form class="form-horizontal" name="form" method="post">
    <div class="form-group">
      <label class="control-label col-sm-4"  for="Create Employee ID">Employee ID:</label>
      <div class="col-sm-4">
      	    
    		<input name="CUST_CODE" type="TEXT" value="<%=sCustCode%>"
			size="50" MAXLENGTH=100 class="form-control" <%=sCustEnb%> readonly>
   	      </div>
        </div>
    
    <div class="form-group">
      <label class="control-label col-sm-4" for="Emp First Name">First Name:</label>
      <div class="col-sm-4">          
        <INPUT  class="form-control" name="CUST_NAME" type="TEXT" value="<%=sCustName%>"
			size="50" MAXLENGTH=100 readonly>
      </div>
    </div>
    <div class="form-group">
      <label class="control-label col-sm-4" for="Emp Last Name">Last Name:</label>
      <div class="col-sm-4">          
        <INPUT  class="form-control" name="L_CUST_NAME" type="TEXT" value="<%=sCustNameL%>"
			size="50" MAXLENGTH=100 readonly>
      </div>
    </div>
       <div class="form-group">
    <label class="control-label col-sm-4" for="Type">Gender:</label>
  <div class="col-sm-4">
    <label class="radio-inline">
      <INPUT name="GENDER" type = "radio" value="M"    disabled="disabled" <%if(sGender.equalsIgnoreCase("M")) {%>checked <%}%> >Male  
    </label>
    <label class="radio-inline">
      <INPUT name="GENDER" type = "radio" value="F" disabled="disabled"  <%if(sGender.equalsIgnoreCase("F")) {%>checked <%}%>  >Female
    </label>
     </div>
</div>
    <div class="form-group">
      <label class="control-label col-sm-4"  for="Date of Birth">Date of Birth:</label>
      <div class="col-sm-4">
      	    
    		<input name="DOB" type="TEXT" value="<%=sDOB%>"
			size="50" MAXLENGTH=100 class="form-control" readonly>
   		 	
  		      </div>
    </div>  
   <div class="form-group">
      <label class="control-label col-sm-4" for="Nationality">Nationality:</label>
      <div class="col-sm-4">          
        <INPUT  class="form-control"  name="NATIONALITY" type="TEXT" value="<%=sNationality%>"
			size="50" MAXLENGTH=100 readonly>
      </div>
    </div>
    <div class="form-group">
      <label class="control-label col-sm-4" for="Departmant">Department:</label>
      <div class="col-sm-4">          
        <INPUT  class="form-control" name="DEPT" type="TEXT" value="<%=sDept%>"
			size="50" MAXLENGTH=100 readonly>
      </div>
    </div>
    
    <div class="form-group">
      <label class="control-label col-sm-4" for="Designation">Designation:</label>
      <div class="col-sm-4">          
        <INPUT name="DESGINATION" type="TEXT" class="form-control"
			value="<%=sDesgination%>" size="50" MAXLENGTH="30" readonly>
      </div>
    </div>
    <div class="form-group">
      <label class="control-label col-sm-4" for="Telephone No">Telephone:</label>
      <div class="col-sm-4">          
        <INPUT name="TELNO" type="TEXT" value="<%=sTelNo%>" size="50" class="form-control"
			MAXLENGTH="30" readonly>
      </div>
    </div>
    <div class="form-group">
      <label class="control-label col-sm-4" for="Hand Phone">Mobile:</label>
      <div class="col-sm-4">          
        <INPUT name="HPNO" type="TEXT" value="<%=sHpNo%>" size="50" class="form-control"
			MAXLENGTH="30" readonly>
      </div>
    </div>
    <div class="form-group">
      <label class="control-label col-sm-4" for="Fax">Fax:</label>
      <div class="col-sm-4">          
        <INPUT name="FAX" type="TEXT" value="<%=sFax%>" size="50"
			MAXLENGTH="30" class="form-control" readonly>
      </div>
    </div>
    <div class="form-group">
      <label class="control-label col-sm-4" for="Email">Email:</label>
      <div class="col-sm-4">          
        <INPUT name="EMAIL" type="TEXT" value="<%=sEmail%>" size="50"
			MAXLENGTH="50" class="form-control" readonly>
      </div>
    </div>
    <div class="form-group">
      <label class="control-label col-sm-4" for="Unit No">Unit No:</label>
      <div class="col-sm-4">          
        <INPUT name="ADDR1" type="TEXT" value="<%=sAddr1%>" size="50"
			MAXLENGTH=50 class="form-control" readonly>
      </div>
    </div>
    <div class="form-group">
      <label class="control-label col-sm-4" for="Building">Building:</label>
      <div class="col-sm-4">          
        <INPUT name="ADDR2" type="TEXT" value="<%=sAddr2%>" size="50"
			MAXLENGTH=50 class="form-control" readonly>
      </div>
    </div>
    <div class="form-group">
      <label class="control-label col-sm-4" for="Street">Street:</label>
      <div class="col-sm-4">          
        <INPUT name="ADDR3" type="TEXT" value="<%=sAddr3%>" size="50"
			MAXLENGTH=50 class="form-control" readonly>
      </div>
    </div>
    <div class="form-group">
      <label class="control-label col-sm-4" for="City">City:</label>
      <div class="col-sm-4">          
        <INPUT name="ADDR4" type="TEXT" value="<%=sAddr4%>" size="50"
			MAXLENGTH=50  class="form-control" readonly>
      </div>
    </div>
    <div class="form-group">
      <label class="control-label col-sm-4" for="State">State:</label>
      <div class="col-sm-4">          
        <INPUT name="STATE" type="TEXT" value="<%=sState%>" size="50"
			MAXLENGTH=50 class="form-control" readonly>
      </div>
    </div>
    <div class="form-group">
      <label class="control-label col-sm-4" for="Country">Country:</label>
      <div class="col-sm-4">          
       <INPUT name="COUNTRY" type="TEXT" value="<%=sCountry%>"
			size="50" MAXLENGTH=30 class="form-control" readonly>
      </div>
    </div>
    <div class="form-group">
      <label class="control-label col-sm-4" for="Postal Code">Postal Code:</label>
      <div class="col-sm-4">          
        <INPUT name="ZIP" type="TEXT" value="<%=sZip%>" size="50"
			MAXLENGTH=10 class="form-control" readonly>
      </div>
    </div>
    <div class="form-group">
      <label class="control-label col-sm-4" for="Remarks">Remarks:</label>
      <div class="col-sm-4">          
        <INPUT name="REMARKS" type="TEXT" value="<%=sRemarks%>"
			size="50" MAXLENGTH=100 class="form-control" readonly>
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
      <button type="button" class="Submit btn btn-default" onClick="window.location.href='employeeSummary.jsp'"><b>Back</b></button>&nbsp;&nbsp;
      <!-- <button type="button" class="Submit btn btn-default" onClick="{backNavigation('settings.jsp','SA');}"><b>Back</b></button>&nbsp;&nbsp; -->
    </div>
    </div>

   </form>
</div>
</div>
</div>


<jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
</jsp:include>



