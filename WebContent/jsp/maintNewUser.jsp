<%@page import="net.sf.json.JSONObject"%>
<%@page import="org.apache.commons.fileupload.FileItem"%>
<%@page import="org.apache.commons.fileupload.disk.DiskFileItemFactory"%>
<%@page import="org.apache.commons.fileupload.FileItemFactory"%>
<%@page import="org.apache.commons.fileupload.servlet.ServletFileUpload"%>
<%@page import="javax.transaction.UserTransaction"%>
<%@ page contentType="text/html;charset=windows-1252"%>
<%@ page import="com.track.util.*"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.gates.*"%>
<%@ page import="com.track.dao.*"%>
<%@ page import="com.track.constants.*"%>
<%@ page import="java.util.*"%>
<%@ include file="header.jsp"%>

<%
String title = "Edit User";
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
  
  <meta http-equiv="Content-Type" content="text/html; charset=windows-1252">
  <jsp:useBean id="eb"  class="com.track.gates.encryptBean" /> 
  
<SCRIPT LANGUAGE="JavaScript">

function popWin(URL) {
 subWin = window.open(URL, 'PRODUCT', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=800,height=400,left = 200,top = 184');
}

function onView(){
    document.form.action = "../jsp/maintNewUser.jsp?action=View";
    document.form.submit();
}


function onUpdate()
{
	   var DEPT1   = document.form.DEPT1.value;
	   var USER_ID   = document.form.USER_ID.value;
	   var PASSWORD   = document.form.PASSWORD.value;
	   var CPASSWORD   = document.form.CPASSWORD.value;
	   var USER_NAME   = document.form.USER_NAME.value;
	   var USER_LEVEL   = document.form.USER_LEVEL.value;
	   var USER_LEVEL_ACCOUNTING   = document.form.USER_LEVEL_ACCOUNTING.value;
	   var USER_LEVEL_PAYROLL   = document.form.USER_LEVEL_PAYROLL.value;
  	   var Access   = document.form.ACCESS.value;
	   //var chkPurchaseSystem="0";
	   if(document.getElementById("WEBACCESS").checked == true)
		   Access = "1";
	   else if(document.getElementById("OWNERAPP").checked == true)
		   Access = "1";
	   else if(document.getElementById("MANAGERAPP").checked == true)
		   Access = "1";
	   else if(document.getElementById("STOREAPP").checked == true)
		   Access = "1";
	   else(document.getElementById("RIDEAPP").checked == true)
	   	   Access = "1";
	   if(DEPT1 == "0" || DEPT1 == null) {alert("Please Select Company ");document.form.DEPT1.focus(); return false; }
	   if(USER_ID == "" || USER_ID == null) {alert("Please Enter User ID ");document.form.USER_ID.focus(); return false; }
	   if(PASSWORD == "" || PASSWORD == null) {alert("Please Enter Password ");document.form.PASSWORD.focus(); return false; }
	   if(CPASSWORD == "" || CPASSWORD == null) {alert("Please Enter Confirm Password ");document.form.CPASSWORD.focus(); return false; }
	   if(USER_NAME == "" || USER_NAME == null) {alert("Please Enter User Name ");document.form.USER_NAME.focus(); return false; }
	   if(USER_LEVEL == "0" || USER_LEVEL == null) {alert("Please Select User Level ");document.form.USER_LEVEL.focus(); return false; }
	   if(USER_LEVEL_ACCOUNTING == "0" || USER_LEVEL_ACCOUNTING == null) {alert("Please Select User Level ");document.form.USER_LEVEL_ACCOUNTING.focus(); return false; }
	   if(USER_LEVEL_PAYROLL == "0" || USER_LEVEL_PAYROLL == null) {alert("Please Select User Level ");document.form.USER_LEVEL_PAYROLL.focus(); return false; }
	   if(Access=="0"){alert("Please select any Access System");document.form.WEBACCESS.focus();return false;}

	   var checkadmin = "0";

	   if ($('#isApprovalAdmin').is(':checked')) {
	   	if ($('#isPurchaseApproval').is(':checked')) {
	   		checkadmin = "1";
	   	}
	   	if ($('#isPurchaseRetApproval').is(':checked')) {
	   		checkadmin = "1";
	   	}
	   	if ($('#isSalesApproval').is(':checked')) {
	   		checkadmin = "1";
	   	}
	   	if ($('#isSalesRetApproval').is(':checked')) {
	   		checkadmin = "1";
	   	}
	   	
	   	if(checkadmin == "0"){
	   		alert("Please Select Minimun One Approval As Admin");
	   		return false;
	   	}
	   }
	var chk= confirm("Are you sure you would like to save?");
	if(chk){
document.form.action = "../jsp/newUserSubmit.jsp?Submit=update&USER_ID="+form.USER_ID.value;
document.form.submit();}else
{
	return false;
}
}
function onDelete()
{
var chkmsg = confirm("Are you sure you would like to delete?");
if(chkmsg){
document.form.action = "../jsp/newUserSubmit.jsp?Submit=delete&USER_ID="+form.USER_ID.value;
 document.form.submit();
 }
 else
 {
 return false;
 }
}
function onClear(){
 
  // document.form.action  = "MiscOrderReceiving.jsp?action=CLEAR";
  // document.form.submit();
  document.form.USER_ID.value="";
  document.form.PASSWORD.value="";
  document.form.CPASSWORD.value="";
  document.form.USER_NAME.value="";
  document.form.DESGINATION.value="";
  document.form.TELNO.value="";
  document.form.HPNO.value="";
  document.form.FAX.value="";
  document.form.EMAIL.value="";
   
  document.form.RANK.value="";
  document.form.REMARKS.value="";
  document.form.EFFECTIVE_DATE.value="";
  document.form.DEPT.selectedIndex=0;
  //document.form.USER_LEVEL.value="";
  document.form.USER_LEVEL_1.value="";
  document.form.USER_LEVEL_ACCOUNTING_1.value="";
  document.form.USER_LEVEL_PAYROLL_1.value="";
  document.form.USER_LEVEL_PAYROLL.selectedIndex=0
  document.form.USER_LEVEL_ACCOUNTING.selectedIndex=0
  document.form.USER_LEVEL.selectedIndex=0
  var len = form.USER_LEVEL.options.length;
  for(var i=1;i<len-1;i++)
  {
	  form.USER_LEVEL.options[i]=null;
  }
  var len = form.USER_LEVEL_ACCOUNTING.options.length;
  for(var i=1;i<len-1;i++)
  {
	  form.USER_LEVEL_ACCOUNTING.options[i]=null;
  }
  var len = form.USER_LEVEL_PAYROLL.options.length;
  for(var i=1;i<len-1;i++)
  {
	  form.USER_LEVEL_PAYROLL.options[i]=null;
  }
  return true;
}
function clearlstbox()
{
	
}

</script>

 <script language="JavaScript" type="text/javascript" src="../jsp/js/calendar.js"></script>
 <script language="JavaScript" type="text/javascript" src="../jsp/js/newUser.js"></script>
  <%
  
  session = request.getSession();
  PlantMstDAO plantMstDAO = new PlantMstDAO();
String sUserId = (String) session.getAttribute("LOGIN_USER");
String plant = (String) session.getAttribute("PLANT");
String systatus = session.getAttribute("SYSTEMNOW").toString();
 
String selplant = plant;
String sPlantDesc="",sPlant="" ,action="",OWNERAPP = "0", MANAGERAPP = "0", STOREAPP = "0",RIDEAPP = "0", WEBACCESS = "0",isAccess="",ACCESS="";
StrUtils strUtils = new StrUtils();
String  ISACCESSOWNERAPP = plantMstDAO.getACCESSOWNERAPP(plant);
String  ISACCESSMANGERAPP = plantMstDAO.getACCESSMANGERAPP(plant);
String  ISACCESS_STOREAPP = plantMstDAO.getACCESSSTOREAPP(plant);
String  ISRIDERRAPP = plantMstDAO.getRIDERRAPP(plant);
sPlantDesc  = strUtils.fString(request.getParameter("PLANTDESC"));
action = strUtils.fString(request.getParameter("action"));
sPlant  = strUtils.fString(request.getParameter("PLANT"));
String syname="",user_inv="style=\"display: none;\"",user_pay="style=\"display: none;\"",user_acct="style=\"display: none;\"";
if(systatus.equalsIgnoreCase("PAYROLL")) {
	user_pay="";
	syname="Payroll";
} else if(systatus.equalsIgnoreCase("ACCOUNTING")) {
	user_acct="";
	syname="Accounting";
} else {
	user_inv="";
	syname="Inventory";
}

	if (!"".equals(StrUtils.fString(request.getParameter("ACCESS")))) {
		WEBACCESS = "1";
	}
	if (!"".equals(StrUtils.fString(request.getParameter("ACCESS")))) {
		OWNERAPP = "1";
	}
	if (!"".equals(StrUtils.fString(request.getParameter("ACCESS")))) {
		MANAGERAPP = "1";
	}
	if (!"".equals(StrUtils.fString(request.getParameter("ACCESS")))) {
		STOREAPP = "1";
	}
	if (!"".equals(StrUtils.fString(request.getParameter("ACCESS")))) {
		RIDEAPP = "1";
	}
ArrayList invQryList  = new ArrayList();
userBean _userBean      = new userBean();
_userBean.setmLogger(mLogger);

  String USER_ID="",PASSWORD="",CPASSWORD="",USER_NAME="",DESGINATION="",TELNO="",HPNO="",FAX="",EMAIL="",ISADMIN="",ISPURCHASEAPPROVAL="",ISSALESAPPROVAL="",ISPURCHASERETAPPROVAL="",ISSALESRETAPPROVAL="",
                RANK="",REMARKS="",EFFECTIVE_DATE="",DEPT="",USER_LEVEL="",COMPANY="",IMAGEPATH="",USER_LEVEL_ACCOUNTING="",ACCESS_COUNTER="",USER_STATUS="",ENROLLED_BY="",ENROLLED_ON="",USER_LEVEL_PAYROLL="";     
 if(action.equalsIgnoreCase("View")){
    USER_ID= request.getParameter("USER_ID");
    Hashtable ht = new Hashtable();
    String dept1="";
    if(selplant.equalsIgnoreCase("track"))
     dept1 = request.getParameter("DEPT");
    else
     dept1 = (String)session.getAttribute("PLANT");

    invQryList =_userBean.getUserListSummary(ht,plant,dept1,USER_ID);
  
     for (int iCnt =0; iCnt<invQryList.size(); iCnt++){
			 
       Map lineArr = (Map) invQryList.get(iCnt);
       
       USER_ID    = (String)lineArr.get("USER_ID");
       COMPANY    = request.getParameter("COMPANY");
       PASSWORD = eb.decrypt((String)lineArr.get("PASSWORD"));
       CPASSWORD  = eb.decrypt((String)lineArr.get("PASSWORD"));
       USER_NAME  = (String)lineArr.get("USER_NAME");
       DESGINATION=(String)lineArr.get("DESGINATION");
       TELNO=(String)lineArr.get("TELNO");
       HPNO    = (String)lineArr.get("HPNO");
       FAX     = (String)lineArr.get("FAX");
       EMAIL    = (String)lineArr.get("EMAIL");
       ACCESS_COUNTER    = (String)lineArr.get("ACCESS_COUNTER");
       USER_STATUS    = (String)lineArr.get("USER_STATUS");
       ENROLLED_BY    = (String)lineArr.get("ENROLLED_BY");
       ENROLLED_ON    = (String)lineArr.get("ENROLLED_ON");
       RANK    = (String)lineArr.get("RANK");
       REMARKS   = (String)lineArr.get("REMARKS");
       EFFECTIVE_DATE   = (String)lineArr.get("EFFECTIV_DATE");
       DEPT   = (String)lineArr.get("DEPT");
       USER_LEVEL   = (String)lineArr.get("USER_LEVEL");
       USER_LEVEL_ACCOUNTING   = (String)lineArr.get("USER_LEVEL_ACCOUNTING");
       USER_LEVEL_PAYROLL=(String)lineArr.get("USER_LEVEL_PAYROLL");
       ISADMIN =(String)lineArr.get("ISADMIN");
       ISPURCHASEAPPROVAL =(String)lineArr.get("ISPURCHASEAPPROVAL");
       ISSALESAPPROVAL =(String)lineArr.get("ISSALESAPPROVAL");
       ISPURCHASERETAPPROVAL =(String)lineArr.get("ISPURCHASERETAPPROVAL");
       ISSALESRETAPPROVAL =(String)lineArr.get("ISSALESRETAPPROVAL");
       IMAGEPATH =(String)lineArr.get("IMAGEPATH");
				
				if ("1".equals(StrUtils.fString((String) lineArr.get("WEB_ACCESS")))) {
					isAccess = "W";
				}
				else if ("1".equals(StrUtils.fString((String) lineArr.get("ISACCESSOWNERAPP")))) {
					isAccess = "O";
				}
				else if ("1".equals(StrUtils.fString((String) lineArr.get("MANAGER_APP_ACCESS")))) {
					isAccess = "M";
				}
				else if ("1".equals(StrUtils.fString((String) lineArr.get("ISACCESS_STOREAPP")))) {
					isAccess = "S";
				}
				else  {
					isAccess = "R";
				}
     
      } 
      }
session.setAttribute("SELECTEDUSRID",request.getParameter("USER_ID1"));

%>
 
<jsp:useBean id="gn"  class="com.track.gates.Generator" />
<jsp:useBean id="sl"  class="com.track.gates.selectBean" />
<% 
sl.setmLogger(mLogger);
%>

<div class="container-fluid m-t-20">
	 <div class="box"> 
	 <!-- Thanzith Modified on 23.02.2022 -->
            <ul class="breadcrumb backpageul">    
                <li class="underline-on-hover"><a href="../home">Dashboard</a></li>	
                <li><a href="../user/summary"><span class="underline-on-hover">User Summary</span></a></li>                       
                <li><label>Edit User</label></li>                                   
            </ul>             
    <!-- Thanzith Modified on 23.02.2022 -->
            <div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title"><%=title %></h1>
               <h1 style="font-size: 18px; cursor: pointer;"
				class="box-title pull-right"
				onclick="window.location.href='../user/summary'">
				<i class="glyphicon glyphicon-remove"></i>
			</h1>
		</div>
		
 <div class="box-body">
<h4 id="msg" class="text-center"></h4>
  <form class="form-horizontal" name="form" method="post" id="editUserForm" action="newUserSubmit.jsp" autocomplete="off" onSubmit="return validateUser(this);">
  <div class="col-sm-8">
  <div class="form-group">
   <label class="control-label col-form-label col-sm-4 required" for="Company">Company</label>
   
      <INPUT type="hidden" name="USER_ID1" value="<%=USER_ID%>" size="50"  MAXLENGTH=10>
      <INPUT type="hidden" name="USER_LEVEL_PAYROLL_1" id="USER_LEVEL_PAYROLL_1" value="<%=USER_LEVEL_PAYROLL%>">
      <INPUT type="hidden" name="USER_LEVEL_ACCOUNTING_1" id="USER_LEVEL_ACCOUNTING_1" value="<%=USER_LEVEL_ACCOUNTING%>">
      <INPUT type="hidden" name="USER_LEVEL_1" id="USER_LEVEL_1" value="<%=USER_LEVEL%>">
      <INPUT type="hidden" name="ACCESS_COUNTER" id="ACCESS_COUNTER" value="<%=ACCESS_COUNTER%>">
      <INPUT type="hidden" name="USER_STATUS" id="USER_STATUS" value="<%=USER_STATUS%>">
      <INPUT type="hidden" name="ENROLLED_BY" id="ENROLLED_BY" value="<%=ENROLLED_BY%>">
      <INPUT type="hidden" name="ENROLLED_ON" id="ENROLLED_ON" value="<%=ENROLLED_ON%>">
      <div class="col-sm-5">
      <div class="input-group">
       <SELECT class="btn btn-default dropdown-toggle" data-toggle="dropdown" data-placement="right" NAME="DEPT1" size="1">
		 <%if(selplant.equalsIgnoreCase("track")){ %>  <OPTION selected value="0">< -- Choose  Company-- > </OPTION>  <%=sl.getPlantNames("0")%> <%} else{%>
          <OPTION selected value='<%=selplant%>'><%=selplant%></OPTION>
            <% }%>
                   </SELECT>
        </div>
        </div>
        </div>
        
      <div class="form-group">
      <label class="control-label col-form-label col-sm-4 required" for="User ID">User ID</label>
     
      <div class="col-sm-6">
      <div class="input-group">
      <input name="USER_ID" type="TEXT" value="<%=USER_ID%>"
			size="50" MAXLENGTH=50 class="form-control" onFocus="nextfield ='PASSWORD';">
	<!--   <span class="input-group-addon" >onClick="javascript:popWin('../jsp/userList.jsp?USER_ID='+form.USER_ID.value+'&DEPT1='+form.DEPT1.value);"
			<a href="#" data-toggle="tooltip" data-placement="top" title="User Details">
   		 	<i class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span> -->
		  </div>
	 <INPUT type="hidden" name="DEPT1" value="">
		  </div>
          </div>
                      
      <div class="form-group">
      <label class="control-label col-form-label col-sm-4 required" for="Password">Password</label>
     
      <div class="col-sm-6">
      <input name="PASSWORD" type="password" value="<%=PASSWORD%>"
			size="50" MAXLENGTH=50 class="form-control" onFocus="nextfield ='PASSWORD';">
			</div>
			</div>
			
           
  	  <div class="form-group">
      <label class="control-label col-form-label col-sm-4 required" for="Confirm Password">Confirm Password</label>
      
      <div class="col-sm-6">
      <input name="CPASSWORD" type="password" value="<%=CPASSWORD%>"
			size="50" MAXLENGTH=50 class="form-control" onFocus="nextfield ='USER_NAME';">
		</div>
		 </div>
		   
			
	 <div class="form-group">
     <label class="control-label col-form-label col-sm-4 required" for="User Name">User Name</label>
     
     <div class="col-sm-6">
     <input name="USER_NAME" type="TEXT" value="<%=USER_NAME%>"
			size="50" MAXLENGTH=25 class="form-control" onFocus="nextfield =''RANK';">
			</div>
			</div>
			
			
			
	 <div class="form-group">
      <label class="control-label col-form-label col-sm-4" for="Designation">Designation</label>
      <div class="col-sm-6">
      <input name="DESGINATION" value="<%=DESGINATION%>"
			size="50" MAXLENGTH=40 class="form-control" onFocus="nextfield ='DESGINATION';">
			</div>
			</div>
			
			
	  <div class="form-group">
      <label class="control-label col-form-label col-sm-4" for="Telephone">Telephone </label>
      <div class="col-sm-6">
      	<input id="TELNO" name="TELNO" value="<%=TELNO%>" onkeypress="return validateInput(event)"
			size="50" MAXLENGTH=40 class="form-control" onFocus="nextfield ='TELNO';">
			</div>
			</div>
			 
	  <div class="form-group">
      <label class="control-label col-form-label col-sm-4" for="Handphone">Handphone</label>
      <div class="col-sm-6">
      	<input id="HPNO" name="HPNO" value="<%=HPNO%>" onkeypress="return validateInput(event)"
			size="50" MAXLENGTH=40 class="form-control" onFocus="nextfield ='HPNO';">
			</div>
			</div>
  		 
  	  <div class="form-group">
      <label class="control-label col-form-label col-sm-4" for="Fax">Fax</label>
      <div class="col-sm-6">
      <input id="FAX" name="FAX" value="<%=FAX%>" onkeypress="return validateInput(event)"
			size="50" MAXLENGTH=40 class="form-control" onFocus="nextfield ='FAX';">
			</div>
			</div>
			
	  <div class="form-group">
      <label class="control-label col-form-label col-sm-4" for="Email">Email</label>
      <div class="col-sm-6">
      <input name="EMAIL" value="<%=EMAIL%>"
			size="50" MAXLENGTH=40 class="form-control" onFocus="nextfield ='Email';">
			</div>
			</div>
			
			
	 <div class="form-group">
      <label class="control-label col-form-label col-sm-4" for="Company">Company</label>
      <div class="col-sm-6">
      <%if(selplant.equalsIgnoreCase("track")){%>
            <INPUT size="50" MAXLENGTH=40 name="DEPT" value="<%=DEPT%>" readonly class="form-control" ><%}else{%>
            <INPUT size="50" MAXLENGTH=40 name="DEPT" value="<%=DEPT%>" readonly class="form-control" onFocus="nextfield ='USER_LEVEL';">
                     <%}%>
       </div>
		</div>
			
	  <div class="form-group" <%=user_inv%>>
      <label class="control-label col-form-label col-sm-4 required" for="User level">User level <%=syname%></label>
    
      <div class="col-sm-6">
      <div class="input-group">
       <SELECT class="btn btn-default dropdown-toggle" data-toggle="dropdown" data-placement="right" NAME="USER_LEVEL" size="1">
      <%if(selplant.equalsIgnoreCase("track")){ %>
              <%=sl.getMutiUserLevels("0","INVENTORY")%> <%}else{%><%=sl.getMutiUserLevels("0",selplant,"INVENTORY")%><%} %>
                           
        </SELECT>
         </div>
         </div>
         </div>
         
         <div class="form-group" <%=user_pay%>>
      <label class="control-label col-form-label col-sm-4 required" for="User level">User level <%=syname%></label>
    
      <div class="col-sm-6">
      <div class="input-group">
      
       <SELECT class="btn btn-default dropdown-toggle" data-toggle="dropdown" data-placement="right" NAME="USER_LEVEL_PAYROLL" size="1">
      <%if(selplant.equalsIgnoreCase("track")){ %>
              <%=sl.getMutiUserLevels("0","PAYROLL")%> <%}else{%><%=sl.getMutiUserLevels("0",selplant,"PAYROLL")%><%} %>
                           
        </SELECT>
         </div>
         </div>
         </div>
         
         <div class="form-group" <%=user_acct%>>
      <label class="control-label col-form-label col-sm-4 required" for="User level">User level <%=syname%></label>
    
      <div class="col-sm-6">
      <div class="input-group">        
        <SELECT class="btn btn-default dropdown-toggle" data-toggle="dropdown" data-placement="right" NAME="USER_LEVEL_ACCOUNTING" size="1">
      <%if(selplant.equalsIgnoreCase("track")){ %>
              <%=sl.getMutiUserLevels("0","ACCOUNTING")%> <%}else{%><%=sl.getMutiUserLevels("0",selplant,"ACCOUNTING")%><%} %>
                           
        </SELECT>
         </div>
         </div>
         </div>
      
		<div class="form-group">
			<label class="control-label col-form-label col-sm-4" for="Access System">Access System</label>
			<div class="col-sm-6">
						<label class="control-label col-form-label" class="col-sm-4">
							<input type="radio" value="W" name="ACCESS" id="WEBACCESS" <%if (isAccess.equalsIgnoreCase("W")) {%> checked <%}%> />Web&nbsp;&nbsp;&nbsp;&nbsp;
							</label>
							<label class="control-label col-form-label" class="col-sm-4" <% if(ISACCESSOWNERAPP.equals("0")) {%> style="display:none" <%}%>>
							<input type="radio" value="O" name="ACCESS" id="OWNERAPP" <%if (isAccess.equalsIgnoreCase("O")) {%> checked <%}%> />Owner App&nbsp;&nbsp;&nbsp;&nbsp;
							</label>
							<label class="control-label col-form-label" class="col-sm-4" <% if(ISACCESSMANGERAPP.equals("0")) {%> style="display:none" <%}%>>
							<input type="radio" value="M" name="ACCESS" id="MANAGERAPP" <%if (isAccess.equalsIgnoreCase("M")) {%> checked <%}%> /> Manager App&nbsp;&nbsp;&nbsp;&nbsp;
							</label>
						</div>
						
						<label class="control-label col-form-label col-sm-4" for="Effective Date"></label>
						<div class="col-sm-6">
							<label class="control-label col-form-label" class="col-sm-4" <% if(ISACCESS_STOREAPP.equals("0")) {%> style="display:none" <%}%>>
							<input type="radio" value="S" name="ACCESS" id="STOREAPP" <%if (isAccess.equalsIgnoreCase("S")) {%> checked <%}%> /> Store App&nbsp;&nbsp;&nbsp;&nbsp;
							</label>
							<label class="control-label col-form-label" class="col-sm-4" <% if(ISRIDERRAPP.equals("0")) {%> style="display:none" <%}%>>
							<input type="radio" value="R" name="ACCESS" id="RIDEAPP" <%if (isAccess.equalsIgnoreCase("R")) {%> checked <%}%> /> Delivery App&nbsp;&nbsp;&nbsp;&nbsp;
							</label>
			</div>
		</div>

					<div class="form-group">
						<label class="control-label col-form-label col-sm-4" for="Effective Date">Is Approval Admin</label>
						<div class="col-sm-6">
							<input type="checkbox" name="isApprovalAdmin"  id="isApprovalAdmin" <%if (ISADMIN.equalsIgnoreCase("1")) {%> checked <%}%>>
						</div>
					</div>
					<div class="form-group" id="isadd" <%if (!ISADMIN.equalsIgnoreCase("1")) {%> hidden  <%}%>>
						<label class="control-label col-form-label col-sm-4" for="Effective Date"></label>
						<div class="col-sm-6">
							<p style="font-weight: bold;">Select for Approval</p>
							<div class="col-sm-1">
								<input type="checkbox" name="isPurchaseApproval" id="isPurchaseApproval" <%if (ISPURCHASEAPPROVAL.equalsIgnoreCase("1")) {%> checked <%}%>>
							</div>
							<div class="col-sm-3">
								Purchase
							</div>
							<div class="col-sm-1">
								<input type="checkbox" name="isPurchaseRetApproval" id="isPurchaseRetApproval" <%if (ISPURCHASERETAPPROVAL.equalsIgnoreCase("1")) {%> checked <%}%>>
							</div>
							<div class="col-sm-7">
								Purchase Return
							</div>
						</div>
						<label class="control-label col-form-label col-sm-4" for="Effective Date"></label>
						<div class="col-sm-6">
							<div class="col-sm-1">
								<input type="checkbox" name="isSalesApproval" id="isSalesApproval" <%if (ISSALESAPPROVAL.equalsIgnoreCase("1")) {%> checked <%}%>>
							</div>
							<div class="col-sm-3">
								Sales
							</div>
							<div class="col-sm-1">
								<input type="checkbox" name="isSalesRetApproval" id="isSalesRetApproval" <%if (ISSALESRETAPPROVAL.equalsIgnoreCase("1")) {%> checked <%}%>>
							</div>
							<div class="col-sm-7">
								Sales Return
							</div>
						</div>
					</div>

					<div class="form-group">
      <label class="control-label col-form-label col-sm-4" for="Effective Date">Effective Date</label>
      <div class="col-sm-6">
      <input name="EFFECTIVE_DATE" value="<%=gn.getDate()%>"
			size="50" MAXLENGTH=10 class="form-control" >
			</div>
			</div>
			
			
			
	 <div class="form-group">
     <label class="control-label col-form-label col-sm-4" for="Remarks">Remarks</label>
     <div class="col-sm-6">
     <input name="REMARKS" value="<%=REMARKS%>"
			size="50" MAXLENGTH=60 class="form-control" onFocus="nextfield ='EFFECTIVE_DATE';" >
			</div>
			
			</div>
			<INPUT type="hidden" size="50" MAXLENGTH=40 name="RANK" value="" onFocus="nextfield ='REMARKS';">
		    <INPUT name="COMPANY"  type ="hidden" value="<%=COMPANY%>"  size="1"   MAXLENGTH=80 class="form-control"  >
		    <INPUT type="Hidden" name="ENCRYPT_FLAG" value="1" class="form-control">
	 	
		
			
     <div class="form-group">        
     <div class="col-sm-offset-6 col-sm-8">
     <!-- <button type="button" class="Submit btn btn-default" onClick="window.location.href='../home'"><b>Back</b></button>&nbsp;&nbsp; -->
	 <!-- <button type="button" class="Submit btn btn-default" onClick="{backNavigation('settings.jsp','UA');}"><b>Back</b></button>&nbsp;&nbsp; -->
     <button type="button" class="Submit btn btn-default" onClick="return onClear();">Clear</button>&nbsp;&nbsp;
     <button type="button" class="btn btn-success" onClick="return onUpdate();">Save</button>&nbsp;&nbsp;
	<%if(plant.equalsIgnoreCase("track")){%> <button type="button" class="Submit btn btn-default" value=" Delete " name="button" onClick="return onDelete();"></button> <%}%>
                  &nbsp;
	</div>
	 </div>
	 </div>
	 <div class="col-sm-4 text-center">
		<div class="row">
			<div class="col-sm-12">
				<INPUT size="50" type="hidden" id="imagetmppath" MAXLENGTH=200
					name="imagetmppath" value="<%=IMAGEPATH%>"> <img
					class="img-thumbnail" id="imagePreview" name="IMAGE_UPLOAD" style="width:35%"
					src="<%=(IMAGEPATH.equalsIgnoreCase("")) ? "../jsp/images/trackNscan/nouser.png" : "/track/ReadFileServlet/?fileLocation="+IMAGEPATH%>">

			</div>
		</div>
		<div class="row">
			<div class="col-sm-12">
				<div class="form-group">
					<label>Upload Image</label> <INPUT style="width: 100%;" class="form-control" id="userImage" name="IMAGE_UPLOAD1" type="File" size="20" MAXLENGTH=100> 
					<br> 
					<INPUT class=" btn btn-default" type="BUTTON" value="Remove Image" onClick="removeImage();"> 
					<INPUT class=" btn btn-default" type="BUTTON" value="Upload & Save Image" onClick="onAddImage();">
				</div>
			</div>
		</div>
	</div>
	</form>
	</div>
	</div>
	</div>
			 
			

  <script>
$(document).ready(function(){
	 $("select[name ='USER_LEVEL_PAYROLL']").val(document.form.USER_LEVEL_PAYROLL_1.value);
	 $("select[name ='USER_LEVEL_ACCOUNTING']").val(document.form.USER_LEVEL_ACCOUNTING_1.value);
	 $("select[name ='USER_LEVEL']").val(document.form.USER_LEVEL_1.value);
	 $('[data-toggle="tooltip"]').tooltip();

	 $('#isApprovalAdmin').change(function() {
	     if ($(this).is(':checked')) {
	 		$('#isadd').show();
	     } else {
	         $('#isPurchaseApproval').prop('checked',false);
	         $('#isSalesApproval').prop('checked',false);
	 		$('#isPurchaseRetApproval').prop('checked',false);
	 		$('#isSalesRetApproval').prop('checked',false);
	 		$('#isadd').hide();
	     }
	 }); 
});
function readURL(input) {
    if (input.files && input.files[0]) {
        var reader = new FileReader();

        reader.onload = function (e) {
        	
        	
            $('#imagePreview').attr('src', e.target.result);
        }

        reader.readAsDataURL(input.files[0]);
    }
}
$(document).on('change', '#userImage', function (e) {
    readURL(this);
});

function onAddImage(){
	if(document.form.IMAGE_UPLOAD1.value.length<=0){
		alert('Please browse image');
	   	    return false;   		
	}
	else		
	{
	    var formData = new FormData($('form#editUserForm')[0]);
		var userId= form.USER_ID.value;
		if(userId){
	    	$.ajax({
		        type: 'post',
		        url: "/track/CatalogServlet?Submit=UPLOAD_USER_IMAGE",
		       	dataType:'json',
		        data:  formData,
		        contentType: false,
		        processData: false,	      
		        success: function (data) {
		        	$("#msg").html(data.Message);
		        },
		        error: function (data) {	        	
		        	$("#msg").html(data.Message);
		        }
	    	});
		}
		else{
			alert("Please enter UserId");
		}
    }
	return false;	
}

function removeImage(){
	var loginUserId = '<%=sUserId%>';
	 var selectUserId = document.form.USER_ID.value;
	if(selectUserId == "" || selectUserId == null) {alert("Please choose User ID");  return false; }
	var chkmsg=confirm("Are you sure you would like to delete? ");
	if(chkmsg){
		var formData = $('form#editUserForm').serialize();
		$.ajax({
		      type: 'post',
		      url: '/track/CatalogServlet?Submit=DELETE_USER_IMAGE', 
		      dataType:'json',
		      data:  formData,		     
		      success: function (data) {
		    	  $('#imagePreview').attr('src',"../jsp/images/trackNscan/nouser.png");
		    	  $("#msg").html(data.Message); 
		    	  $('#userImage').val('');
		      },
		      error: function (data) {	        	
		    	  $("#msg").html(data.Message);
		      }
		});
	}
	else
	{ 
		return false;
	}
}
</script>
  
<jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
</jsp:include>