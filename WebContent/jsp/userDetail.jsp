<%@ page contentType="text/html;charset=windows-1252"%>
<%@ page import="com.track.util.*"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.gates.*"%>
<%@ page import="com.track.dao.*"%>
<%@ page import="com.track.constants.*"%>
<jsp:useBean id="eb"  class="com.track.gates.encryptBean" />
<%@ page import="java.util.*"%>

<%
String title = "User Details";
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
   
 <SCRIPT LANGUAGE="JavaScript">

function popWin(URL) {
 subWin = window.open(URL, 'PRODUCT', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=700,height=400,left = 200,top = 184');
}

function onView(){
    document.form.action = "maintNewUser.jsp?action=View";
    document.form.submit();
}


function onUpdate()
{
document.form.action = "newUserSubmit.jsp?Submit=update&USER_ID="+form.USER_ID.value;
document.form.submit();
}
function onDelete()
{

document.form.action = "newUserSubmit.jsp?Submit=delete&USER_ID="+form.USER_ID.value;
 document.form.submit();
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
  document.form.DEPT.value=0
  document.form.USER_LEVEL.selected=0;
 
  return true;
}

</script>
 <script language="JavaScript" type="text/javascript" src="../jsp/js/calendar.js"></script>
  <script language="JavaScript" type="text/javascript" src="../jsp/js/newUser.js"></script>
  
  <%@ include file="header.jsp"%>
  <%
session = request.getSession();
  PlantMstDAO plantMstDAO = new PlantMstDAO();
String sUserId = (String) session.getAttribute("LOGIN_USER");
String plant = (String) session.getAttribute("PLANT");
String selplant = plant;
String sPlantDesc="",sPlant="" ,action="";
StrUtils strUtils = new StrUtils();
sPlantDesc  = strUtils.fString(request.getParameter("PLANTDESC"));
action = strUtils.fString(request.getParameter("action"));
sPlant  = strUtils.fString(request.getParameter("PLANT"));
String systatus = session.getAttribute("SYSTEMNOW").toString();
String  ISACCESSOWNERAPP = plantMstDAO.getACCESSOWNERAPP(plant);
String  ISACCESSMANGERAPP = plantMstDAO.getACCESSMANGERAPP(plant);
String  ISACCESS_STOREAPP = plantMstDAO.getACCESSSTOREAPP(plant);
String  ISRIDERRAPP = plantMstDAO.getRIDERRAPP(plant);

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
ArrayList invQryList  = new ArrayList();
userBean _userBean      = new userBean();
_userBean.setmLogger(mLogger);
  String USER_ID="",PASSWORD="",CPASSWORD="",USER_NAME="",DESGINATION="",TELNO="",HPNO="",FAX="",EMAIL="",
                RANK="",REMARKS="",EFFECTIVE_DATE="",DEPT="",USER_LEVEL="",COMPANY="",USER_LEVEL_ACCOUNTING="",USER_LEVEL_PAYROLL=""; 
  String OWNERAPP = "0", MANAGERAPP = "0", STOREAPP = "0",RIDEAPP = "0", WEBACCESS = "0";
  if (!"".equals(StrUtils.fString(request.getParameter("WEB_ACCESS")))) {
		WEBACCESS = "1";
	}
	if (!"".equals(StrUtils.fString(request.getParameter("OWNER_APP")))) {
		OWNERAPP = "1";
	}
	if (!"".equals(StrUtils.fString(request.getParameter("MANAGER_APP")))) {
		MANAGERAPP = "1";
	}
	if (!"".equals(StrUtils.fString(request.getParameter("STORE_APP")))) {
		STOREAPP = "1";
	}
	if (!"".equals(StrUtils.fString(request.getParameter("RIDE_APP")))) {
		RIDEAPP = "1";
	}
 if(action.equalsIgnoreCase("View")){
    USER_ID= request.getParameter("USER_ID");
    Hashtable ht = new Hashtable();
    String dept1="";
    if(selplant.equalsIgnoreCase("track"))
     dept1 = request.getParameter("DEPT");
    else
     dept1 = (String)session.getAttribute("PLANT");

    invQryList =_userBean.getUserListSummary(ht,plant,dept1,USER_ID);
    System.out.println("");
     for (int iCnt =0; iCnt<invQryList.size(); iCnt++){
			 
       Map lineArr = (Map) invQryList.get(iCnt);
       
       USER_ID    = (String)lineArr.get("USER_ID");
       COMPANY    = request.getParameter("COMPANY");
       PASSWORD = eb.decrypt((String)lineArr.get("PASSWORD"));
//       PASSWORD   = (String)lineArr.get("PASSWORD");
       CPASSWORD  = eb.decrypt((String)lineArr.get("PASSWORD"));
       USER_NAME  = (String)lineArr.get("USER_NAME");
       DESGINATION=(String)lineArr.get("DESGINATION");
       TELNO=(String)lineArr.get("TELNO");
       HPNO    = (String)lineArr.get("HPNO");
       FAX     = (String)lineArr.get("FAX");
       EMAIL    = (String)lineArr.get("EMAIL");
       RANK    = (String)lineArr.get("RANK");
       REMARKS   = (String)lineArr.get("REMARKS");
       EFFECTIVE_DATE   = (String)lineArr.get("EFFECTIV_DATE");
       DEPT   = (String)lineArr.get("DEPT");
       USER_LEVEL   = (String)lineArr.get("USER_LEVEL");
//       DEPT   = (String)session.getAttribute("DEPT");
//       USER_LEVEL   = (String)session.getAttribute("USER_LEVEL");
       USER_LEVEL_ACCOUNTING   = (String)lineArr.get("USER_LEVEL_ACCOUNTING");
       USER_LEVEL_PAYROLL=(String)lineArr.get("USER_LEVEL_PAYROLL");
       WEBACCESS = (String) lineArr.get("WEB_ACCESS");
	   OWNERAPP = (String) lineArr.get("ISACCESSOWNERAPP");
	   MANAGERAPP = (String) lineArr.get("MANAGER_APP_ACCESS");
	   STOREAPP = (String) lineArr.get("ISACCESS_STOREAPP");
	   RIDEAPP = (String) lineArr.get("RIDER_APP_ACCESS");
     
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
           		<li class="underline-on-hover"><a href="../home">Dashboard </a></li>   	
 			<li><a href="../user/summary"><span class="underline-on-hover">User Summary</span></a></li>                
 			<li><label>User Details</label></li>                                   
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
 
 <form class="form-horizontal" name="form" method="post" action="newUserSubmit.jsp" onSubmit="return validateUser(this);">
 <div class="form-group">
 <label class="control-label col-form-label col-sm-4" for="User ID">User ID</label>
 <div class="col-sm-4">
  <input name="USER_ID"  value="<%=USER_ID%>" 
          size="50" MAXLENGTH=10 onFocus="nextfield ='PASSWORD';" class="form-control" readonly>
  </div>
  <INPUT size="50" type="hidden"  MAXLENGTH=10 name="USER_ID1" value="" > 
     </div>
     
     
 <div class="form-group">
 <label class="control-label col-form-label col-sm-4" for="Password">Password</label>
 <div class="col-sm-4">
  <input name="PASSWORD" type="password" value="<%=PASSWORD%>" 
          size="50" MAXLENGTH=10 onFocus="nextfield ='CPASSWORD';" class="form-control" readonly>
  </div>
     </div>
     
<div class="form-group">
<label class="control-label col-form-label col-sm-4" for="Confirm Password">Confirm Password</label>
<div class="col-sm-4">
<input name="CPASSWORD" type="password" value="<%=CPASSWORD%>" 
          size="50" MAXLENGTH=10 onFocus="nextfield ='USER_NAME';" class="form-control" readonly>
 </div>
     </div>
     
<div class="form-group">
<label class="control-label col-form-label col-sm-4" for="User Name">User Name</label>
<div class="col-sm-4">
<input name="USER_NAME"  value="<%=USER_NAME%>" 
          size="50" MAXLENGTH=25 onFocus="nextfield ='RANK';" class="form-control" readonly>
 </div>
     </div>
     
<div class="form-group">
<label class="control-label col-form-label col-sm-4" for="Designation">Designation</label>
<div class="col-sm-4">
<input name="DESGINATION"  value="<%=DESGINATION%>" 
          size="50" MAXLENGTH=40 onFocus="nextfield ='DESGINATION';" class="form-control" readonly>
 </div>
     </div>
     
<div class="form-group">
<label class="control-label col-form-label col-sm-4" for="Telephone">Telephone</label>
<div class="col-sm-4">
<input name="TELNO"  value="<%=TELNO%>" 
          size="50" MAXLENGTH=40 onFocus="nextfield ='TELNO';" class="form-control" readonly>
 </div>
     </div>
     
<div class="form-group">
<label class="control-label col-form-label col-sm-4" for="HandPhone">HandPhone</label>
<div class="col-sm-4">
<input name="HPNO"  value="<%=HPNO%>" 
          size="50" MAXLENGTH=40 onFocus="nextfield ='HPNO';" class="form-control" readonly>
 </div>
     </div>
     
<div class="form-group">
<label class="control-label col-form-label col-sm-4" for="Fax">Fax</label>
<div class="col-sm-4">
<input name="FAX"  value="<%=FAX%>" 
          size="50" MAXLENGTH=40 onFocus="nextfield ='FAX';" class="form-control" readonly>
 </div>
     </div> 
     
 <div class="form-group">
<label class="control-label col-form-label col-sm-4" for="Email">Email</label>
<div class="col-sm-4">
<input name="EMAIL"  value="<%=EMAIL%>" 
          size="50" MAXLENGTH=40 onFocus="nextfield ='Email';" class="form-control" readonly>
 </div>
     </div>  
     
     
<div class="form-group">
<label class="control-label col-form-label col-sm-4" for="Company">Company</label>
<div class="col-sm-4">
<%if(selplant.equalsIgnoreCase("track")){%>
   <INPUT size="50" MAXLENGTH=40 name="DEPT" value="<%=DEPT%>" class="form-control" readonly ><%}else{%>
    <INPUT size="50" MAXLENGTH=40 name="DEPT" value="<%=DEPT%>" class="form-control" readonly  onFocus="nextfield ='USER_LEVEL';">
                     <%}%>
         </div>
     </div> 
     
     
     <div class="form-group">
      <label class="control-label col-form-label col-sm-4" for="User level">User level <%=syname%></label>
      <div class="col-sm-4">
      <div class="input-group" <%=user_inv%>>
       <SELECT class="btn btn-default dropdown-toggle" data-toggle="dropdown" data-placement="right" NAME ="USER_LEVEL" size="1"  disabled="disabled">
         <OPTION selected value='<%=USER_LEVEL%>'><%=USER_LEVEL%> </OPTION>
         </SELECT>  
         </div>
         
         <div class="input-group" <%=user_acct%>>
       <SELECT class="btn btn-default dropdown-toggle" data-toggle="dropdown" data-placement="right" NAME ="USER_LEVEL_ACCOUNTING" size="1"  disabled="disabled">
         <OPTION selected value='<%=USER_LEVEL_ACCOUNTING%>'><%=USER_LEVEL_ACCOUNTING%> </OPTION>
         </SELECT>  
         </div>
         
         <div class="input-group" <%=user_pay%>>
       <SELECT class="btn btn-default dropdown-toggle" data-toggle="dropdown" data-placement="right" NAME ="USER_LEVEL_PAYROLL" size="1"  disabled="disabled">
         <OPTION selected value='<%=USER_LEVEL_PAYROLL%>'><%=USER_LEVEL_PAYROLL%> </OPTION>
         </SELECT>  
         </div>
         
         </div>
         </div>
         
             	<div class="form-group">
      	<label class="control-label col-form-label col-sm-4" for="Access System">Access System</label>
       	<div class="col-sm-6" style="padding-top: 7px;">
       						 <%if (Integer.parseInt(WEBACCESS) == 1){%>Web<input type="hidden" value="o" name="WEB_ACCESS" id="WEB_ACCESS" /><%}%>
       					<% if(ISACCESSOWNERAPP.equals("1")) {%><%if (Integer.parseInt(OWNERAPP) == 1){%>,Owner App<input type="hidden" value="o" name="OWNER_APP" id="OWNER_APP"/><%}%> <%}%> 
       					<% if(ISACCESSMANGERAPP.equals("1")) {%><%if (Integer.parseInt(MANAGERAPP) == 1){%>,Manager App<input type="hidden" value="m" name="MANAGER_APP" id="MANAGER_APP" /><%}%><%}%>
       					<% if(ISACCESS_STOREAPP.equals("1")) {%><%if (Integer.parseInt(STOREAPP) == 1){%>,Store App<input type="hidden" value="m" name="STORE_APP" id="STORE_APP"   /><%}%><%}%>
       					<% if(ISRIDERRAPP.equals("1")) {%> <%if (Integer.parseInt(RIDEAPP) == 1){%>,Ride App<input type="hidden" value="r" name="RIDE_APP" id="RIDE_APP"  /><%}%> <%}%>
      	</div>
    	</div>
         
 <div class="form-group">
<label class="control-label col-form-label col-sm-4" for="Effective Date">Effective Date</label>
<div class="col-sm-4">
<input name="EFFECTIVE_DATE"  value="<%=gn.getDate()%>" 
          size="50" MAXLENGTH=10 onFocus="nextfield ='COMPANY';" class="form-control" readonly>
 </div>
     </div>
  
     
<div class="form-group">
<label class="control-label col-form-label col-sm-4" for="Remarks">Remarks</label>
<div class="col-sm-4">
<input name="REMARKS"  value="<%=REMARKS%>" 
          size="50" MAXLENGTH=60 onFocus="nextfield ='EFFECTIVE_DATE';" class="form-control" readonly>
 </div>
     </div>
     
     
 <div class="form-group">
  <div class="col-sm-4">
  <INPUT type="hidden" size="50" MAXLENGTH=40 name="RANK" value=""  onFocus="nextfield ='REMARKS';"> 
  <INPUT name="COMPANY"  type ="hidden" value="<%=COMPANY%>" size="1"   MAXLENGTH=80>
  <INPUT type="Hidden" name="ENCRYPT_FLAG" value="1">
		 </div>
	 	</div>
	 	
<div class="form-group">        
     <div class="col-sm-offset-5 col-sm-7">
    <!--  <button type="button" class="Submit btn btn-default" onClick="window.location.href='UserSummary.jsp'"><b>Back</b></button>&nbsp;&nbsp; -->
	 <!-- <button type="button" class="Submit btn btn-default" onClick="{backNavigation('settings.jsp','UA');}"><b>Back</b></button>&nbsp;&nbsp; -->
	 	</div>
	 	</div>
	 	
	 	</form>
	</div>
	</div>
	</div>
	

<jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
</jsp:include>