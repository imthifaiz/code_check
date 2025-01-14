<%@page import="com.track.db.object.HrEmpSalaryDET"%>
<%@page import="com.track.db.object.HrEmpSalaryMst"%>
<%@page import="com.track.service.HrEmpSalaryService"%>
<%@page import="com.track.serviceImplementation.HrEmpSalaryServiceImpl"%>
<%@page import="org.apache.taglibs.standard.tag.el.core.ForEachTag"%>
<%@ page import="com.track.util.*"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.gates.*"%>
<%@ page import="com.track.constants.*"%>
<%@ page import="com.track.dao.*"%>
<%@ page import="com.track.db.object.EmployeeLeaveDET"%>
<%@ page import="com.track.db.object.EmployeeLeaveDETpojo"%>
<%@ include file="header.jsp"%>
<%
String title = "Employee Details";
%>
<%@include file="sessionCheck.jsp" %>
<jsp:useBean id="eb"  class="com.track.gates.encryptBean" />
<jsp:useBean id="gn"  class="com.track.gates.Generator" />
<jsp:include page="header2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
	<jsp:param name="mainmenu" value="<%=IConstants.SETTINGS%>"/>
	<jsp:param name="submenu" value="<%=IConstants.EMPLOYEE%>"/>
</jsp:include>
  
<script src="../jsp/js/typeahead.jquery.js"></script>
<link rel="stylesheet" href="../jsp/css/typeahead.css">
<link rel="stylesheet" href="../jsp/css/accounting.css">
<script type="text/javascript" src="../jsp/js/general.js"></script>
<style>
.leavetype-action {
	cursor: pointer;
	font-size: 15px;
	opacity: 0.4;
	position: absolute;
	right: -25%;
	top: 15px;
}
.empSalary-action {
	cursor: pointer;
	font-size: 15px;
	opacity: 0.4;
	position: absolute;
	right: -10%;
	top: 15px;
}

.phideshow{
    float: right;
    margin-top: -34px;
    position: relative;
    z-index: 2;
    margin-right: 40px;
}
</style>

<SCRIPT LANGUAGE="JavaScript">


var subWin = null;
function popUpWin(URL) {
 subWin = window.open(URL, 'Employee', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=800,height=400,left = 200,top = 184');
}
function onNew(){

	document.form.CUST_NAME.value = "";
	document.form.EMP_LOGIN_ID.value = ""; 
	document.form.CPASSWORD.value = "";
	document.form.employeetypeid.value = ""; 
	document.form.GENDER.value = "";
	document.form.DOB.value = ""; 
	document.form.TELNO.value = "";
	document.form.EMAIL.value = ""; 
	document.form.PASSPORTNUMBER.value = "";
	document.form.COUNTRYOFISSUE.value = ""; 
	document.form.PASSPORTEXPIRYDATE.value = "";
	document.form.EMP_REPORTING.value = ""; 
	document.form.COUNTRY_CODE.value = "";
	document.form.ADDR1.value = ""; 
	document.form.ADDR2.value = "";
	document.form.ADDR3.value = ""; 
	document.form.ADDR4.value = "";
	document.form.STATE.value = ""; 
	document.form.ZIP.value = "";
	document.form.FACEBOOK.value = ""; 
	document.form.TWITTER.value = "";
	document.form.LINKEDIN.value = ""; 
	document.form.SKYPE.value = "";
	document.form.ACTIVE.value = ""; 
	document.form.EMIRATESID.value = "";
	document.form.EMIRATESIDEXPIRY.value = ""; 
	document.form.VISANUMBER.value = "";
	document.form.VISAEXPIRYDATE.value = ""; 
	document.form.DEPT.value = "";
	document.form.DESGINATION.value = ""; 
	document.form.DATEOFJOINING.value = "";
	document.form.DATEOFLEAVING.value = ""; 
	document.form.LABOURCARDNUMBER.value = "";
	document.form.WORKPERMITNUMBER.value = ""; 
	document.form.CONTRACTSTARTDATE.value = "";
	document.form.CONTRACTENDDATE.value = ""; 
	document.form.OUTLET_NAME.value = ""; 

	document.form.leavetype.value = "";
	document.form.totalentitlement.value = "";
	document.form.leaveyear.value = "";
	document.form.notes.value = "";
	document.form.empSalaryid.value = "";
	document.form.empSalaryAmt.value = "";
	document.form.GRATUITY.value = "";
	document.form.AIRTICKET.value = "";
	document.form.LEAVESALARY.value = "";
	document.form.REMARKS.value = "";
	
//    document.form.action  = "../payroll/editemployee";
//    document.form.action  = "../payroll/editemployee?action=NEW";
//    document.form.submit();
}
function isNumberKey(evt, element, id) {
	  var charCode = (evt.which) ? evt.which : event.keyCode;
	  if (charCode > 31 && (charCode < 48 || charCode > 57) && !(charCode == 46 || charCode == 8 || charCode == 45))
		  {
	    	return false;
		  }
	  return true;
	}
function isNumber(evt) {	
    evt = (evt) ? evt : window.event;
    var charCode = (evt.which) ? evt.which : evt.keyCode;
    
    if ((charCode > 31 && (charCode < 48 || charCode > 57))) {
    	if( (charCode!=43 && charCode!=32 && charCode!=45))
    		{
    		
        alert("  Please enter only Numbers.  ");
        return false;
    		}
    	}
    return true;
}
function validateInput(event) {
    const key = String.fromCharCode(event.which || event.keyCode);
    
    // Allow digits, spaces, commas, semicolons, and plus signs
    const regex = /[\d\s,+;]/;

    if (regex.test(key)) {
        return true;
    } else {
        event.preventDefault();  // Prevent the keypress if it's not valid
        alert("Only (0-9)+;, and spaces special characters are allowed.");
        return false;
    }
}
function image_edit(){
    var formData = new FormData($('#empform')[0]);
    var userId= form.CUST_CODE.value;
	if(userId){
    $.ajax({
        type: 'post',
        url: "/track/CatalogServlet?Submit=emp_img_edit",
       	dataType:'html',
    data:  formData,//{key:val}
    contentType: false,
    processData: false,
      
        success: function (data) {
        	console.log(data)
        	console.log(JSON.parse(data).result)
        	var result =JSON.parse(data).result;
        	$('#msg').html(result.message); 
         
        },
        error: function (data) {
        	
            alert(data.responseText);
        }
    });
	}else{
		alert("Please enter Employee Id");
	}
        return false; 
  }
function image_delete(){
	 var formData = $('form').serialize();
    var userId= form.CUST_CODE.value;
	if(userId){
    $.ajax({
        type: 'post',
        url: "/track/CatalogServlet?Submit=emp_img_delete",
       	dataType:'html',
    data:  formData,//{key:val}
        success: function (data) {
        	console.log(data)
        	var result =JSON.parse(data).result;
        	$('#msg').html(result.message); 
        	  $('#emp_img').attr('src',"../jsp/images/trackNscan/nouser.png");      	 
        	  $('#userImage').val('');
        },
        error: function (data) {
        	
            alert(data.responseText);
        }
    });
	}else{
		alert("Please enter Employee Id");
	}
        return false; 
  }	
function onUpdate(){
	
   var CUST_CODE   = document.form.CUST_CODE.value;
   var CUST_NAME   = document.form.CUST_NAME.value;
   /* var EMP_TYPE   = document.form.employeetype.value; */
   if(CUST_CODE == "" || CUST_CODE == null) {alert("Please Enter Employee ID");document.form.CUST_CODE.focus(); return false; }
   
   if(CUST_NAME == "" || CUST_NAME == null) {
	   alert("Please Enter Employee Name");
	   document.form.CUST_NAME.focus();
	   return false; 
	   }
   
   var EMP_LOGIN_ID   = document.form.EMP_LOGIN_ID.value;
   var PASSWORD   = document.form.CPASSWORD.value;
   var EMP_REPORTING   = document.form.EMP_REPORTING.value;
   var ispay   = document.form.ispay.value;
   var EMAIL = document.form.EMAIL.value;
   if(ispay == "1"){
	   if(EMP_LOGIN_ID == "" || EMP_LOGIN_ID == null) {
		   alert("Please Enter Employee Login ID"); 
		   document.form.EMP_LOGIN_ID.focus();
		   return false; 
		   }  
	   
	   if(PASSWORD == "" || PASSWORD == null) {
		   alert("Please Enter Password"); 
		   document.form.CPASSWORD.focus();
		   return false; 
		   }  
	   
	  /*  if(EMAIL == "" || EMAIL == null) {
		   alert("Please Enter Employee Email"); 
		   document.form.EMAIL.focus();
		   return false; 
		   }  */
	   
	   if(EMP_REPORTING == "" || EMP_REPORTING == null) {
		   alert("Please Enter Employee Reporting"); 
		   document.form.EMP_REPORTING.focus();
		   return false; 
		   }  
   }
   
   	var userid = document.form.EMP_LOGIN_ID.value;
   	var uid = document.form.empid.value;
   	if(EMP_LOGIN_ID == "" || EMP_LOGIN_ID == null) {
   	
   	}else{
	   	$.ajax({
	   		type : "GET",
	   		url: '/track/HrLeaveTypeServlet',
	   		async : true,
	   		dataType: 'json',
	   		data : {
	   			CMD : "CHECK_USERID_EDIT",
	   			username : userid,
	   			uid : uid
	   		},
	   		success : function(data) {
	   			if(data.STATUS == "NOT OK"){
	   				$('input[name = "EMP_LOGIN_ID"]').val(logid);
	   				alert("Employee Login ID already exist");
	   				document.form.EMP_LOGIN_ID.focus();
	   				return false;
	   			}
	   		}
	   	});
   	}

   
   var radio_choiceemp = false;
   for (i = 0; i < document.form.GENDER.length; i++)
   {
       if (document.form.GENDER[i].checked)
    	   radio_choiceemp = true; 
   }
   if (!radio_choiceemp)
   {
   alert("Please Select Gender.");
   return (false);
   }

    var radio_choice = false;
    for (i = 0; i < document.form.ACTIVE.length; i++)
    {
        if (document.form.ACTIVE[i].checked)
        radio_choice = true; 
    }
    if (!radio_choice)
    {
    alert("Please select Active or non Active mode.")
    return (false);
    }
    /* if(form.COUNTRY_CODE.selectedIndex==0)
	{
	   alert("Please Select Country from Address");
	 form.COUNTRY_CODE.focus();
	 return false;
	} */
   var chk = confirm("Are you sure you would like to save?");
	if(chk){
		add_attachments();
   		/* document.form.action  = "maint_employee.jsp?action=UPDATE";
   		document.form.submit(); */
	}
	else{
		return false;}	   
}
function onDelete(){
	   var CUST_CODE   = document.form.CUST_CODE.value;
	   if(CUST_CODE == "" || CUST_CODE == null) {alert("Please Enter Employee ID");  return false; }
	var chkmsg=confirm("Are you sure you would like to delete? ");
	    if(chkmsg){
	   document.form.action  = "../payroll/editemployee?action=DELETE";
	   document.form.submit();}
	   else
	   { return false;
	   }
	}

function onView(){
   var CUST_CODE   = document.form.CUST_CODE.value;
   
   if(CUST_CODE == "" || CUST_CODE == null) {alert("Please Enter Employee ID"); return false; }

   document.form.action  = "../payroll/editemployee?action=VIEW";
   document.form.submit();
}

</SCRIPT>
<%
String sUserId = (String) session.getAttribute("LOGIN_USER");
String Defaultgr="DefaultGroup";
String res        = "";
String sNewEnb    = "enabled";
String sDeleteEnb = "enabled";
String sAddEnb    = "enabled";
String sUpdateEnb = "enabled";
String sCustEnb   = "enabled";
String action     = "";
String ispay="0",ispos="0";
String sCustCode  = "",
empid="",
uid="",
euid="",
sEmptypeid  = "",
sEmptype  = "",
sRepid="",
sRepname="",
sUserloginid="",
sPassword="",
sManworkhour  = "",
sCustName  = "",
sOldCustName  = "",
sCustNameL  = "",
sAddr1     = "",
sAddr2     = "",
sAddr3     = "", sAddr4     = "",
sState   = "",
sCountry   = "",
isautoemail   = "",
ISCREATEONUSERINFO = "",
ISPOSCUSTOMER ="",
ISEDITPOSPRODUCTPRICE="",
ISCASHIER="",
ISSALESMAN="",
sZip       = "",
sDept      = "",isActive="";
String sDesgination="",sTelNo="",sHpNo="",sFax="",sEmail="",sRemarks="",sNationality="",sDOB="",sGender="",DOB="",DYNAMIC_ALTERNATE_SIZE="",IMAGEPATH="",
sPASSPORTNUMBER="",sCOUNTRYOFISSUE="",sPASSPORTEXPIRYDATE="",FACEBOOK="",TWITTER="",LINKEDIN="",SKYPE="",sEMIRATESID="",sEMIRATESIDEXPIRY="",sVISANUMBER="",sVISAEXPIRYDATE="",
sDATEOFJOINING="",sDATEOFLEAVING="",sLABOURCARDNUMBER="",sWORKPERMITNUMBER="",sCONTRACTSTARTDATE="",sCONTRACTENDDATE="",sIBAN="",sBANKNAME="",sBANKROUTINGCODE="",sBRANCH="",
BASICSALARY="",HOUSERENTALLOWANCE="",TRANSPORTALLOWANCE="",COMMUNICATIONALLOWANCE="",OTHERALLOWANCE="",BONUS="",COMMISSION="",sSAVE_RED="",sCountryCode="",sSAVE_REDELETE,sSalary="0",
GRATUITY="",AIRTICKET="",result="",LEAVESALARY="",sOutlet="",sOutCode="",ISPAYROLL_BY_BASIC_SALARY="";
StrUtils strUtils = new StrUtils();
PlantMstDAO _PlantMstDAO = new PlantMstDAO();
EmployeeUtil custUtil = new EmployeeUtil();
userBean _userBean = new userBean();
HrEmpTypeDAO hrEmpTypeDAO = new HrEmpTypeDAO();
EmployeeLeaveDetDAO employeeLeaveDetDAO = new EmployeeLeaveDetDAO();
List<EmployeeLeaveDETpojo>  empleavedet = new ArrayList<EmployeeLeaveDETpojo>();
HrEmpSalaryDetDAO hrEmpSalaryDetDAO = new HrEmpSalaryDetDAO();
List<HrEmpSalaryDET> empSalarydet = new ArrayList<HrEmpSalaryDET>();
EmpAttachDAO empAttachDAO = new EmpAttachDAO();
custUtil.setmLogger(mLogger);

action            = strUtils.fString(request.getParameter("action"));
if(action.equalsIgnoreCase("VIEWDATA"))
	title = "Employee Details";
else
	title = "Edit Employee";

HrEmpSalaryService hrEmpSalaryService = new HrEmpSalaryServiceImpl();
String plant = strUtils.fString((String)session.getAttribute("PLANT"));
String region = strUtils.fString((String) session.getAttribute("REGION"));
String pcountry = strUtils.fString((String) session.getAttribute("COUNTRY"));
ispay = _PlantMstDAO.getispayroll(plant);
ispos = _PlantMstDAO.getispos(plant);
if(pcountry.equalsIgnoreCase("United Arab Emirates"))
	pcountry="UAE";
String numberOfDecimal = _PlantMstDAO.getNumberOfDecimal(plant);
sCustCode  = strUtils.fString(request.getParameter("CUST_CODE"));
DateUtils dateutils = new DateUtils();
//if(sCustCode.length() <= 0) sCustCode  = strUtils.fString(request.getParameter("CUST_CODE1"));
sCustName  = strUtils.InsertQuotes(strUtils.fString(request.getParameter("CUST_NAME")));
sOldCustName  = strUtils.InsertQuotes(strUtils.fString(request.getParameter("OLD_CUST_NAME")));
sEmptypeid  = strUtils.fString(request.getParameter("employeetypeid"));
sUserloginid=strUtils.fString(request.getParameter("EMP_LOGIN_ID"));
sPassword=strUtils.fString(request.getParameter("CPASSWORD"));
sRepid = strUtils.fString(request.getParameter("repid"));
uid = strUtils.fString(request.getParameter("uid"));
euid= strUtils.fString(request.getParameter("euid"));
/* sManworkhour  = strUtils.fString(request.getParameter("WORK_MAN_HOURS")); */
sCustNameL  = strUtils.InsertQuotes(strUtils.fString(request.getParameter("L_CUST_NAME")));
sAddr1     = strUtils.fString(request.getParameter("ADDR1"));
sAddr2     = strUtils.fString(request.getParameter("ADDR2"));
sAddr3     = strUtils.fString(request.getParameter("ADDR3"));
sAddr4     = strUtils.fString(request.getParameter("ADDR4"));
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
sEmail= strUtils.fString(request.getParameter("EMAIL"));
sOutlet = strUtils.InsertQuotes(strUtils.fString(request.getParameter("OUTLET_NAME")));
sOutCode = strUtils.InsertQuotes(strUtils.fString(request.getParameter("OUTCODE")));
sRemarks= strUtils.InsertQuotes(strUtils.fString(request.getParameter("REMARKS")));
DYNAMIC_ALTERNATE_SIZE = StrUtils.fString(request.getParameter("DYNAMIC_ALTERNATE_SIZE"));
if(sDOB.length() > 5){
	DOB = sDOB.substring(6) +"-"+ sDOB.substring(3, 5) +"-"+  sDOB.substring(0, 2);
}
isActive= strUtils.fString(request.getParameter("ACTIVE"));

sDATEOFJOINING     = strUtils.fString(request.getParameter("DATEOFJOINING"));
sDATEOFLEAVING   = strUtils.InsertQuotes(strUtils.fString(request.getParameter("DATEOFLEAVING")));
SKYPE   = strUtils.InsertQuotes(strUtils.fString(request.getParameter("SKYPE")));
FACEBOOK   = strUtils.InsertQuotes(strUtils.fString(request.getParameter("FACEBOOK")));
TWITTER   = strUtils.InsertQuotes(strUtils.fString(request.getParameter("TWITTER")));
LINKEDIN   = strUtils.InsertQuotes(strUtils.fString(request.getParameter("LINKEDIN")));
sPASSPORTNUMBER       = strUtils.fString(request.getParameter("PASSPORTNUMBER"));
sCOUNTRYOFISSUE      = strUtils.fString(request.getParameter("COUNTRYOFISSUE"));
sPASSPORTEXPIRYDATE      = strUtils.fString(request.getParameter("PASSPORTEXPIRYDATE"));
sEMIRATESID  = strUtils.fString(request.getParameter("EMIRATESID"));
sEMIRATESIDEXPIRY = strUtils.fString(request.getParameter("EMIRATESIDEXPIRY"));
sVISANUMBER = strUtils.fString(request.getParameter("VISANUMBER"));
sVISAEXPIRYDATE  = strUtils.fString(request.getParameter("VISAEXPIRYDATE"));
sLABOURCARDNUMBER  = strUtils.fString(request.getParameter("LABOURCARDNUMBER"));
sWORKPERMITNUMBER  = strUtils.fString(request.getParameter("WORKPERMITNUMBER"));
sCONTRACTSTARTDATE= strUtils.fString(request.getParameter("CONTRACTSTARTDATE"));
sCONTRACTENDDATE     = strUtils.fString(request.getParameter("CONTRACTENDDATE"));
sBANKNAME   = strUtils.InsertQuotes(strUtils.fString(request.getParameter("BANKNAME")));
sIBAN   = strUtils.InsertQuotes(strUtils.fString(request.getParameter("IBAN")));
sBANKROUTINGCODE       = strUtils.fString(request.getParameter("BANKROUTINGCODE"));
BASICSALARY      = strUtils.fString(request.getParameter("BASICSALARY"));
HOUSERENTALLOWANCE      = strUtils.fString(request.getParameter("HOUSERENTALLOWANCE"));
TRANSPORTALLOWANCE  = strUtils.fString(request.getParameter("TRANSPORTALLOWANCE"));
COMMUNICATIONALLOWANCE = strUtils.fString(request.getParameter("COMMUNICATIONALLOWANCE"));
OTHERALLOWANCE = strUtils.fString(request.getParameter("OTHERALLOWANCE"));
BONUS  = strUtils.fString(request.getParameter("BONUS"));
COMMISSION  = strUtils.fString(request.getParameter("COMMISSION"));
sSAVE_RED  = strUtils.fString(request.getParameter("SAVE_RED"));
sSAVE_REDELETE = strUtils.fString(request.getParameter("SAVE_REDELETE"));
GRATUITY      = strUtils.fString(request.getParameter("GRATUITY"));
AIRTICKET      = strUtils.fString(request.getParameter("AIRTICKET"));
LEAVESALARY  = strUtils.fString(request.getParameter("LEAVESALARY"));
isautoemail = strUtils.fString(request.getParameter("isautoemail"));
ISCREATEONUSERINFO = strUtils.fString(request.getParameter("ISCREATEONUSERINFO"));
ISPOSCUSTOMER = strUtils.fString(request.getParameter("ISPOSCUSTOMER")); 
ISEDITPOSPRODUCTPRICE = strUtils.fString(request.getParameter("ISEDITPOSPRODUCTPRICE")); 
ISCASHIER = strUtils.fString(request.getParameter("ISCASHIER")); 
ISSALESMAN = strUtils.fString(request.getParameter("ISSALESMAN")); 

float BASICSALARYVALUE="".equals(BASICSALARY) ? 0.0f :  Float.parseFloat(BASICSALARY);
float HOUSERENTALLOWANCEVALUE="".equals(HOUSERENTALLOWANCE) ? 0.0f :  Float.parseFloat(HOUSERENTALLOWANCE);
float TRANSPORTALLOWANCEVALUE="".equals(TRANSPORTALLOWANCE) ? 0.0f :  Float.parseFloat(TRANSPORTALLOWANCE);
float COMMUNICATIONALLOWANCEVALUE="".equals(COMMUNICATIONALLOWANCE) ? 0.0f :  Float.parseFloat(COMMUNICATIONALLOWANCE);
float OTHERALLOWANCEVALUE="".equals(OTHERALLOWANCE) ? 0.0f :  Float.parseFloat(OTHERALLOWANCE);
float BONUSVALUE="".equals(BONUS) ? 0.0f :  Float.parseFloat(BONUS);
float COMMISSIONVALUE="".equals(COMMISSION) ? 0.0f :  Float.parseFloat(COMMISSION);
float sSalaryVALUE="".equals(sSalary) ? 0.0f :  Float.parseFloat(sSalary);
float GRATUITYVALUE="".equals(GRATUITY) ? 0.0f :  Float.parseFloat(GRATUITY);
float AIRTICKETVALUE="".equals(AIRTICKET) ? 0.0f :  Float.parseFloat(AIRTICKET);
float LEAVESALARYVALUE="".equals(LEAVESALARY) ? 0.0f :  Float.parseFloat(LEAVESALARY);
List Empattachlist= new ArrayList();
//1. >> New
if(action.equalsIgnoreCase("NEW")){
     
// 	 sCustCode  = "";
     sCustName  = "";
     sCustNameL="";
     sAddr1     = "";
     sAddr2     = "";
     sAddr3     = ""; sAddr4     = "";
     sState   = "";
     sCountry   = "";
     sZip       = "";
     sDept ="";
     sPassword =  "";
     sNationality =""; sDesgination="";sTelNo="";sHpNo="";sFax="";sEmail="";sDOB="";sGender="";sRemarks="";
     sPASSPORTNUMBER="";sCOUNTRYOFISSUE="";sPASSPORTEXPIRYDATE="";FACEBOOK="";TWITTER="";LINKEDIN="";SKYPE="";sEMIRATESID="";sEMIRATESIDEXPIRY="";sVISANUMBER="";sVISAEXPIRYDATE="";
	  sDATEOFJOINING="";sDATEOFLEAVING="";sLABOURCARDNUMBER="";sWORKPERMITNUMBER="";sCONTRACTSTARTDATE="";sCONTRACTENDDATE="";sIBAN="";sBANKNAME="";sBANKROUTINGCODE="";sBRANCH="";
	  BASICSALARY="";HOUSERENTALLOWANCE="";TRANSPORTALLOWANCE="";COMMUNICATIONALLOWANCE="";OTHERALLOWANCE="";BONUS="";COMMISSION="";
	  GRATUITY="";AIRTICKET="";LEAVESALARY="";sOutlet="";sOutCode=""; 
	 
	  
	  BASICSALARYVALUE="".equals(BASICSALARY) ? 0.0f :  Float.parseFloat(BASICSALARY);
	  HOUSERENTALLOWANCEVALUE="".equals(HOUSERENTALLOWANCE) ? 0.0f :  Float.parseFloat(HOUSERENTALLOWANCE);
	  TRANSPORTALLOWANCEVALUE="".equals(TRANSPORTALLOWANCE) ? 0.0f :  Float.parseFloat(TRANSPORTALLOWANCE);
	  COMMUNICATIONALLOWANCEVALUE="".equals(COMMUNICATIONALLOWANCE) ? 0.0f :  Float.parseFloat(COMMUNICATIONALLOWANCE);
	  OTHERALLOWANCEVALUE="".equals(OTHERALLOWANCE) ? 0.0f :  Float.parseFloat(OTHERALLOWANCE);
	  BONUSVALUE="".equals(BONUS) ? 0.0f :  Float.parseFloat(BONUS);
	  COMMISSIONVALUE="".equals(COMMISSION) ? 0.0f :  Float.parseFloat(COMMISSION);
	  GRATUITYVALUE="".equals(GRATUITY) ? 0.0f :  Float.parseFloat(GRATUITY);
	  AIRTICKETVALUE="".equals(AIRTICKET) ? 0.0f :  Float.parseFloat(AIRTICKET);
	  LEAVESALARYVALUE="".equals(LEAVESALARY) ? 0.0f :  Float.parseFloat(LEAVESALARY);

}
//3. >> Update
else if(action.equalsIgnoreCase("UPDATE"))  {
	result="";
	EmployeeDAO employeeDAO =new EmployeeDAO();
	Hashtable htcond = new Hashtable();
	String[] leavetype = request.getParameterValues("leavetypeid");
	String[] totalentitlement = request.getParameterValues("totalentitlement");
	String[] leaveyear = request.getParameterValues("leaveyear");
	String[] notes = request.getParameterValues("notes");
	
	String[] empSalary = request.getParameterValues("empSalary");
	String[] empSalaryAmt = request.getParameterValues("empSalaryAmt");
	
	empid = request.getParameter("empid");
	String empPwd = "";
	 String effDt=gn.getDate();
	
 	htcond.put(IDBConstants.PLANT,plant);
 	htcond.put(IDBConstants.EMPNO,sCustCode);
   if(custUtil.isExistsEmployee(htcond))
    {
          Hashtable htUpdate = new Hashtable();
          htUpdate.put(IConstants.PLANT,plant);
          htUpdate.put(IDBConstants.EMPNO,sCustCode);
          htUpdate.put(IDBConstants.FNAME,sCustName);
          htUpdate.put(IConstants.CUSTOMER_LAST_NAME,sCustNameL);
          htUpdate.put(IDBConstants.EMPLOYEETYPEID,sEmptypeid);
          htUpdate.put(IDBConstants.EMPUSERID,sUserloginid);
          if(sPassword != ""){
        	  empPwd   = eb.encrypt(sPassword);
        	  htUpdate.put(IDBConstants.PASSWORD_EMP,empPwd);
        	  sPassword = empPwd;
          }
          htUpdate.put(IDBConstants.REPORTING_INCHARGE,sRepid);
          /* htUpdate.put(IDBConstants.NUMBEROFMANDAYS,sManworkhour); */
          htUpdate.put(IDBConstants.GENDER,sGender);
          htUpdate.put(IDBConstants.DOB,sDOB);
          htUpdate.put(IDBConstants.DEPTARTMENT,sDept);
          htUpdate.put(IConstants.DESGINATION,sDesgination);
          htUpdate.put(IDBConstants.DATEOFJOINING,sDATEOFJOINING);
          htUpdate.put(IDBConstants.DATEOFLEAVING,sDATEOFLEAVING);
          htUpdate.put(IDBConstants.NATIONALITY,sNationality);
          htUpdate.put(IConstants.TELNO,sTelNo);
          htUpdate.put(IConstants.HPNO,sHpNo);
          htUpdate.put(IConstants.EMAIL,sEmail);
          htUpdate.put(IConstants.FACEBOOK,FACEBOOK);
          htUpdate.put(IConstants.TWITTER,TWITTER);
          htUpdate.put(IConstants.LINKEDIN,LINKEDIN);
          htUpdate.put(IConstants.SKYPE,SKYPE);
          htUpdate.put(IDBConstants.PASSPORTNUMBER,sPASSPORTNUMBER);
	      if(sCOUNTRYOFISSUE.equalsIgnoreCase("Select Country"))
	    	  sCOUNTRYOFISSUE="";
	      htUpdate.put(IDBConstants.COUNTRYOFISSUE,sCOUNTRYOFISSUE);
	      htUpdate.put(IDBConstants.PASSPORTEXPIRYDATE,sPASSPORTEXPIRYDATE);
	      htUpdate.put(IConstants.UNITNO,sAddr1);
	      htUpdate.put(IConstants.BUILDING,sAddr2);
	      htUpdate.put(IConstants.STREET,sAddr3);
	      htUpdate.put(IConstants.CITY,sAddr4);
          if(sState.equalsIgnoreCase("Select State"))
				sState="";
          htUpdate.put(IConstants.STATE,sState);
          if(sCountry.equalsIgnoreCase("Select Country"))
        	  sCountry="";
          htUpdate.put(IConstants.COUNTRY,sCountry);
          htUpdate.put(IConstants.ZIP,sZip);
          htUpdate.put(IDBConstants.EMIRATESID,sEMIRATESID);
          htUpdate.put(IDBConstants.EMIRATESIDEXPIRY,sEMIRATESIDEXPIRY);
          htUpdate.put(IDBConstants.VISANUMBER,sVISANUMBER);
          htUpdate.put(IDBConstants.VISAEXPIRYDATE,sVISAEXPIRYDATE);
          htUpdate.put(IDBConstants.LABOURCARDNUMBER,sLABOURCARDNUMBER);
          htUpdate.put(IDBConstants.WORKPERMITNUMBER,sWORKPERMITNUMBER);
          htUpdate.put(IDBConstants.CONTRACTSTARTDATE,sCONTRACTSTARTDATE);
          htUpdate.put(IDBConstants.CONTRACTENDDATE,sCONTRACTENDDATE);
          if(sBANKNAME.equalsIgnoreCase("Select Bank"))
        	  sBANKNAME="";
          htUpdate.put(IDBConstants.BANKNAME,sBANKNAME);
          htUpdate.put(IDBConstants.IBAN,sIBAN);
          htUpdate.put(IDBConstants.BANKROUTINGCODE,sBANKROUTINGCODE);
          htUpdate.put(IDBConstants.BASICSALARY,BASICSALARY);
          htUpdate.put(IDBConstants.HOUSERENTALLOWANCE,HOUSERENTALLOWANCE);
          htUpdate.put(IDBConstants.TRANSPORTALLOWANCE,TRANSPORTALLOWANCE);
          htUpdate.put(IDBConstants.COMMUNICATIONALLOWANCE,COMMUNICATIONALLOWANCE);
          htUpdate.put(IDBConstants.OTHERALLOWANCE,OTHERALLOWANCE);
          htUpdate.put(IDBConstants.BONUS,BONUS);
          htUpdate.put(IDBConstants.COMMISSION,COMMISSION);          
          htUpdate.put(IConstants.REMARKS,sRemarks);
          htUpdate.put(IConstants.UPDATED_AT,new DateUtils().getDateTime());
          htUpdate.put(IConstants.UPDATED_BY,sUserId);
          htUpdate.put(IConstants.ISACTIVE,isActive);
          htUpdate.put(IConstants.ISACTIVE,isActive);
          htUpdate.put(IConstants.OUTLETS_CODE, sOutCode);
          htUpdate.put(IDBConstants.GRATUITY,GRATUITY);
          htUpdate.put(IDBConstants.AIRTICKET,AIRTICKET);
          htUpdate.put(IDBConstants.LEAVESALARY,LEAVESALARY);
          htUpdate.put(IDBConstants.ISAUTOEMAILPAY,isautoemail);
          htUpdate.put("ISPOSCUSTOMER",ISPOSCUSTOMER);
          htUpdate.put("ISEDITPOSPRODUCTPRICE",ISEDITPOSPRODUCTPRICE);
          htUpdate.put("ISSALESMAN",ISSALESMAN);
          htUpdate.put("ISCREATEONUSERINFO",ISCREATEONUSERINFO);
          htUpdate.put("ISCASHIER",ISCASHIER);

          MovHisDAO mdao = new MovHisDAO(plant);
          mdao.setmLogger(mLogger);
          Hashtable htm = new Hashtable();
          htm.put("PLANT",plant);
          htm.put(IDBConstants.DIRTYPE,TransactionConstants.UPDATE_EMPLOYEE);
          htm.put("RECID","");
          htm.put("ITEM",sCustCode);
          htm.put("UPBY",sUserId);   htm.put("CRBY",sUserId);
          htm.put("CRAT",dateutils.getDateTime());
          htm.put("UPAT",dateutils.getDateTime());
          htm.put(IDBConstants.TRAN_DATE,dateutils.getDateinyyyy_mm_dd(dateutils.getDate()));  
          boolean custUpdated = custUtil.updateEmployeeMst(htUpdate, htcond);
          boolean  inserted = mdao.insertIntoMovHis(htm);
          
          if(custUpdated){
        	  if(leaveyear != null){
	        	  for(int i =0 ; i < leavetype.length ; i++){
	         		 EmployeeLeaveDET employeeLeavedet = new EmployeeLeaveDET();
	         		 employeeLeavedet.setPLANT(plant);
	         		 employeeLeavedet.setEMPNOID(Integer.valueOf(empid));
	         		 employeeLeavedet.setLEAVETYPEID(Integer.valueOf((leavetype[i])));
	         		 employeeLeavedet.setTOTALENTITLEMENT(Double.valueOf(totalentitlement[i]));
	         		 employeeLeavedet.setLEAVEBALANCE(Double.valueOf(totalentitlement[i]));
	         		 employeeLeavedet.setLEAVEYEAR(leaveyear[i]);
	         		 employeeLeavedet.setNOTE(notes[i]);
	         		 employeeLeavedet.setCRAT(dateutils.getDate());
	         		 employeeLeavedet.setCRBY(sUserId);
	         	  	 employeeLeaveDetDAO.addEmployeeLeavedet(employeeLeavedet);
	        	  }
        	  }
        	  
        	//Add Salary - (Azees 7.8.20)
        	  if(empSalary != null){        		  
        		  Boolean chksalary=hrEmpSalaryDetDAO.DeleteEmpSalarydet(plant,Integer.valueOf(empid));
	        	  for(int i =0 ; i < empSalary.length ; i++){
	        		  String empSalarytype = (String)empSalary[i];
	         		 if(!empSalarytype.equalsIgnoreCase("")){	         		 
	        		  HrEmpSalaryDET hrEmpSalaryDET = new HrEmpSalaryDET();
	        			 hrEmpSalaryDET.setPLANT(plant);
	        			 hrEmpSalaryDET.setEMPNOID(Integer.valueOf(empid));
	        			 hrEmpSalaryDET.setSALARYTYPE(empSalarytype);
	        			 hrEmpSalaryDET.setSALARY(Double.valueOf((String)empSalaryAmt[i]));
	        			 hrEmpSalaryDET.setCRAT(dateutils.getDate());
	        			 hrEmpSalaryDET.setCRBY(sUserId);
	        			 hrEmpSalaryDET.setUPAT(dateutils.getDate());
	        			 hrEmpSalaryDET.setUPBY(sUserId);
	        			 hrEmpSalaryDetDAO.addSalarydet(hrEmpSalaryDET);
	         		 }
	        	  }
        	  }
        	
        	  
        	  if(ispay.equalsIgnoreCase("1") || ispos.equalsIgnoreCase("1")){
        		if(uid.equalsIgnoreCase("")) {
        		if(empPwd != "" && sUserloginid != "" ){	
  			    Hashtable htTblloginInsert  = new Hashtable();           
  			    htTblloginInsert.put(IDBConstants.PLANT,plant);          
  			    htTblloginInsert.put("EMPNOID",String.valueOf(Integer.valueOf(empid)));
  			    htTblloginInsert.put("EMPUSERID",sUserloginid);
  			    htTblloginInsert.put("PASSWORD",empPwd);
  			    htTblloginInsert.put("ISCASHIER",ISCASHIER);
  			    htTblloginInsert.put("ISSALESMAN",ISSALESMAN);
  	           	htTblloginInsert.put(IDBConstants.CREATED_BY, sUserId);
  	           	htTblloginInsert.put(IDBConstants.CREATED_AT, (String)new DateUtils().getDateTime());
  	           	custUtil.insertIntoEmployeeuseidMst(htTblloginInsert);
  			  }
        	}
        		
        	  if(empPwd != ""){
        		  employeeDAO.updatepassword(uid, empPwd);
        	  }
        	  employeeDAO.updateusername(uid, sUserloginid);
        	  employeeDAO.updatesalesman(uid, ISSALESMAN);
        	  employeeDAO.updatecashier(uid, ISCASHIER);
          }
        	  
        	  
        	  if (ISCREATEONUSERINFO.equalsIgnoreCase("1")) {
        		  
        				  //uid = employeeDAO.getidbyusername((String)m.get("EMPUSERID"), " PLANT='"+plant+"' AND EMPNOID='"+empid+"' "); 
     			 if(!euid.equalsIgnoreCase("")) {
     				 if(empPwd != "" && sUserloginid != "" ){
     					
     						_userBean.updateuserpassword(euid, empPwd);
     					
     				 }
     			 }else{
     				Hashtable<String, String> htTblLoginInsert = new Hashtable<>();
     				htTblLoginInsert.put("DEPT", plant);
		            htTblLoginInsert.put("USER_ID", sUserloginid);
		            htTblLoginInsert.put("PASSWORD", empPwd);
		            htTblLoginInsert.put("USER_NAME", sCustName);
		            htTblLoginInsert.put("ACCESS_COUNTER", ISCASHIER);
		            htTblLoginInsert.put("RANK", Defaultgr);
		            htTblLoginInsert.put("REMARKS", "user created on employee");
		    
		            htTblLoginInsert.put("USER_LEVEL", Defaultgr);
		            htTblLoginInsert.put("USER_STATUS", "1");
		            htTblLoginInsert.put("EFFECTIVE_DATE", gn.getDBDateShort(effDt));
		            htTblLoginInsert.put("ENROLLED_BY", sUserId);
		            htTblLoginInsert.put("ENROLLED_ON", gn.getDBDateShort(effDt));
		            htTblLoginInsert.put("UPDATED_BY", sUserId);
		            htTblLoginInsert.put("UPDATED_ON", new DateUtils().getDateTime());
		            htTblLoginInsert.put("AUTHORISE_BY",sUserId );
		            htTblLoginInsert.put("AUTHORISE_ON", new DateUtils().getDateTime());
		            htTblLoginInsert.put("USER_LEVEL_ACCOUNTING", Defaultgr);
		            htTblLoginInsert.put("USER_LEVEL_PAYROLL",Defaultgr );
		            htTblLoginInsert.put("WEB_ACCESS", "1");
		            htTblLoginInsert.put("ISACCESSOWNERAPP", "0");
		            htTblLoginInsert.put("MANAGER_APP_ACCESS", "0");
		            htTblLoginInsert.put("ISACCESS_STOREAPP", "0");
		            htTblLoginInsert.put("RIDER_APP_ACCESS", "0");
		            htTblLoginInsert.put("ISADMIN", "0");
		            htTblLoginInsert.put("ISPURCHASEAPPROVAL", "0");
		            htTblLoginInsert.put("ISSALESAPPROVAL", "0");
		            htTblLoginInsert.put("ISPURCHASERETAPPROVAL", "0");
		            htTblLoginInsert.put("ISSALESRETAPPROVAL", "0");
		            htTblLoginInsert.put("ISACCESSSUPERVISORAPP", "0");
		            htTblLoginInsert.put("ISACCESSPROJECTMANAGERAPP", "0");
		        	custUtil.insertIntouserMst(htTblLoginInsert);
     			 }
     		 }
        	  if(!sCustName.equalsIgnoreCase(sOldCustName))
				 {
        	  Hashtable htJournalDet = new Hashtable();
              htJournalDet.put("ACCOUNT_NAME", sCustCode+"-"+sOldCustName);
              htJournalDet.put(IConstants.PLANT, plant);
  			
              String updateJournalDet ="set ACCOUNT_NAME='"+sCustCode+"-"+sCustName+"'"; 
              
        	//FINCHARTOFACCOUNTS & FINJOURNALDET UPDATE
				boolean flag = new CoaDAO().isExisitcoaAccount(htJournalDet);
				 if(flag){
					 	flag = new CoaDAO().updatecoaAccount(updateJournalDet.toString(), htJournalDet, " ");
					 }
				 flag = new JournalDAO().isExisitJournalDet(htJournalDet);
				 if(flag){
					 	flag = new JournalDAO().updateJournalDet(updateJournalDet.toString(), htJournalDet, " ");
					 }
					 
				 }
          }
          
/* 	        boolean alternateEmpRemoved = false;
	       if(custUtil.isAlternateEmployeeAvailable(plant, sCustCode)){
	    	   alternateEmpRemoved= custUtil.removeAlternateEmp(plant,sCustCode," and EMPNO<>ALTERNATE_EMPLOYEE_NO ");
	        	alternateEmpRemoved=true;
	        }else{
	        	alternateEmpRemoved = true;
	        } 
	        boolean insertAlternateEmp = false;
               List<String> alternateEmpNameLists = new ArrayList<String>();
               
             
	          if(alternateEmpRemoved) {
	        	  String alternateEmpName = StrUtils.fString(request.getParameter("DYNAMIC_ALTERNATE_NAME_1"));
		        
		        	 System.out.println("DYNAMIC_ALTERNATE_SIZE"+DYNAMIC_ALTERNATE_SIZE);
		        	 int DYNAMIC_ALTERNATE_SIZE_INT = (new Integer(DYNAMIC_ALTERNATE_SIZE)).intValue();
		        	 for(int nameCount = 1; nameCount<=DYNAMIC_ALTERNATE_SIZE_INT;nameCount++){
		        		if(strUtils.fString(request.getParameter("DYNAMIC_ALTERNATE_NAME_"+nameCount))==""){
		        			break;
		        		}else{
		        			alternateEmpNameLists.add(StrUtils.fString(request.getParameter("DYNAMIC_ALTERNATE_NAME_"+nameCount)));
		        		
		        		}
		        	 }

		int index=alternateEmpNameLists.indexOf(sCustCode);
	if(index>=0)
	{
		alternateEmpNameLists.remove(index);
	}
			insertAlternateEmp = custUtil.insertAlternateEmpLists(plant, sCustCode, alternateEmpNameLists);
	          } */
          
          
            if(custUpdated&&inserted) {
                    //res = "<font class = "+IConstants.SUCCESS_COLOR+">Employee Updated Successfully</font>";
            	//response.sendRedirect("jsp/employeeSummary.jsp?result=Employee Updated Successfully");
            	//request.getRequestDispatcher("jsp/employeeSummary.jsp?result=Employee Updated Successfully").forward(request,response);
            	//RequestDispatcher requestDispatcher = request.getRequestDispatcher("jsp/employeeSummary.jsp?result=Employee Updated Successfully");
                //requestDispatcher.forward(request, response);
                //return;
            	//getServletConfig().getServletContext().getRequestDispatcher("jsp/employeeSummary.jsp").forward(request, response);
            	sSAVE_RED="Employee Updated Successfully";
          } else {
        	  sSAVE_RED="Failed to Update Employee";

//                     res = "<font class = "+IConstants.FAILED_COLOR+">Failed to Update Employee</font>";
          }
    }else{
    	sSAVE_RED="Employee doesn't not Exists. Try again";

//            res = "<font class = "+IConstants.FAILED_COLOR+">Employee doesn't not Exists. Try again</font>";

    }
}

else if(action.equalsIgnoreCase("DELETE")){
	result="";
	 DoHdrDAO dao = new DoHdrDAO();
	 EstHdrDAO estdao = new EstHdrDAO();
	 //TimeTrackingDAO timetrackingdao = new TimeTrackingDAO();//Removed by Azees 14.09.22
	    
	 boolean movementhistoryExist=false;
	 boolean isExistEstimate=false;
	 boolean isExistTimeTracking=false;
	 
	 Hashtable htmh = new Hashtable();
	 htmh.put("EMPNO",sCustCode);
	 htmh.put(IConstants.PLANT,plant);
	   	
	 movementhistoryExist = dao.isExisit(htmh,"");
	 isExistEstimate= estdao.isExisit(htmh,"");
	 //isExistTimeTracking = timetrackingdao.isExisit(htmh,"");
	 if(movementhistoryExist || isExistEstimate||isExistTimeTracking)
	 {	
	 		res = "<font class = " + IDBConstants.FAILED_COLOR
	 	+ " >Employee Exists In Transactions</font>";
	   		
	 }else{
	
	Hashtable htcond = new Hashtable();
 	htcond.put(IDBConstants.PLANT,plant);
 	htcond.put(IDBConstants.EMPNO,sCustCode);
    if(custUtil.isExistsEmployee(htcond))
    {
    	 boolean custDeleted = custUtil.deleteEmployeeid(htcond);
         
    	 MovHisDAO mdao = new MovHisDAO(plant);
          mdao.setmLogger(mLogger);
 			Hashtable htm = new Hashtable();
   			htm.put(IDBConstants.PLANT,plant);
   		 	htm.put(IDBConstants.DIRTYPE,TransactionConstants.DEL_EMPLOYEE);
    		htm.put("RECID","");
    		htm.put("ITEM",sCustCode);
   		 	htm.put(IDBConstants.CREATED_BY,sUserId);   htm.put("CRBY",sUserId);
    	 	htm.put(IDBConstants.CREATED_AT,dateutils.getDateTime());
     		htm.put(IDBConstants.REMARKS,sCustName+","+strUtils.InsertQuotes(sRemarks));
     		htm.put(IDBConstants.UPDATED_AT,dateutils.getDateTime());
     		htm.put(IDBConstants.TRAN_DATE,dateutils.getDateinyyyy_mm_dd(dateutils.getDate()));  
    
     		custDeleted = mdao.insertIntoMovHis(htm);
     		boolean alternateemp=custUtil.removeAlternateEmp(plant,sCustCode, "");
          
     		 if(custDeleted) {
     			
                 /* res = "<font class = "+IConstants.SUCCESS_COLOR+">Employee Deleted Successfully</font>"; */
                 sSAVE_REDELETE ="Delete";
                 sCustCode  = "";
                 sCustName  = "";
                 sCustNameL="";
                 sAddr1     = "";
                 sAddr2     = "";
                 sAddr3     = ""; sAddr4     = "";
                 sState   = "";
                 sCountry   = "";
                 sZip       = "";
                 sDept ="";
                 sPassword = "";
                 sNationality =""; sDesgination="";sTelNo="";sHpNo="";sFax="";sEmail="";sDOB="";sGender="";sRemarks="";
                 sPASSPORTNUMBER="";sCOUNTRYOFISSUE="";sPASSPORTEXPIRYDATE="";FACEBOOK="";TWITTER="";LINKEDIN="";SKYPE="";sEMIRATESID="";sEMIRATESIDEXPIRY="";sVISANUMBER="";sVISAEXPIRYDATE="";
           	  sDATEOFJOINING="";sDATEOFLEAVING="";sLABOURCARDNUMBER="";sWORKPERMITNUMBER="";sCONTRACTSTARTDATE="";sCONTRACTENDDATE="";sIBAN="";sBANKNAME="";sBANKROUTINGCODE="";sBRANCH="";
           	  BASICSALARY="";HOUSERENTALLOWANCE="";TRANSPORTALLOWANCE="";COMMUNICATIONALLOWANCE="";OTHERALLOWANCE="";BONUS="";COMMISSION="";
           	  GRATUITY="";AIRTICKET="";LEAVESALARY="";sOutlet="";sOutCode="";ISPAYROLL_BY_BASIC_SALARY="";

       } else {
    	   sSAVE_REDELETE="Failed to Delete Employee";
//                  res = "<font class = "+IConstants.FAILED_COLOR+">Failed to Delete Employee</font>";
                 sAddEnb = "enabled";
       }
 }else{
	 sSAVE_REDELETE="Employee doesn't not Exists. Try again";
//         res = "<font class = "+IConstants.FAILED_COLOR+">Employee doesn't not Exists. Try again</font>";
 }
	 }
}


//4. >> View
else if(action.equalsIgnoreCase("VIEW")||action.equalsIgnoreCase("VIEWDATA")){
try{

	EmployeeDAO employeeDAO = new EmployeeDAO();
    ArrayList arrCust = custUtil.getEmployeeList(sCustCode,plant,"");
    
    Map m=(Map)arrCust.get(0);
    
   sCustCode   = (String)m.get("EMPNO");
   empid = (String)m.get("ID");
   isautoemail = (String)m.get("ISAUTOEMAIL");
   ISCREATEONUSERINFO = (String)m.get("ISCREATEONUSERINFO");
   sCustName   = (String)m.get("FNAME");
   sCustNameL   = (String)m.get("LNAME");
   sUserloginid = (String)m.get("EMPUSERID");
   sPassword = (String)m.get("PASSWORD");
	sRepid = (String)m.get("REPORTING_INCHARGE");
	sRepname = employeeDAO.getEmpnamebyid(plant, sRepid, "");
   sEmptypeid  = (String)m.get("EMPLOYEETYPEID");
   if(!sEmptypeid.equalsIgnoreCase("0")){
	   sEmptype = hrEmpTypeDAO.getEmployeetypeusingId(plant, Integer.valueOf(sEmptypeid));
   }
   //uid = employeeDAO.getidbyusername((String)m.get("EMPUSERID"), "");
   
        		  
   uid = employeeDAO.getidbyusername((String)m.get("EMPUSERID"), " PLANT='"+plant+"' AND EMPNOID='"+empid+"' ");//FIX username password update if empty -azees 14.12.22
 /*   ISSALESMAN = employeeDAO.getidbyusername((String)m.get("ISSALESMAN"), "");
   ISCASHIER = employeeDAO.getidbyusername((String)m.get("ISCASHIER"), ""); */
   Empattachlist = empAttachDAO.getempAttachByempId(plant, empid);
   /* sManworkhour   = (String)m.get("NUMBEROFMANDAYS"); */
   sGender =(String)m.get("GENDER");
   sDOB =(String)m.get("DOB");
   /* if(DOB.length() > 0){
	   sDOB = DOB.substring(8,10)+"/"+DOB.substring(5, 7)+"/"+DOB.substring(0,4) ;
	} */
   sDept =(String)m.get("DEPT");
   sDesgination =(String)m.get("DESGINATION");
   sDATEOFJOINING =(String)m.get("DATEOFJOINING");
   sDATEOFLEAVING =(String)m.get("DATEOFLEAVING");
   sNationality =(String)m.get("NATIONALITY");
   sTelNo =(String)m.get("TELNO");
   sHpNo =(String)m.get("HPNO");
   //sFax =(String)m.get("FAX");
   sEmail = (String)m.get("EMAIL");
   SKYPE = (String)m.get("SKYPEID");
   FACEBOOK = (String)m.get("FACEBOOKID");
   TWITTER = (String)m.get("TWITTERID");
   LINKEDIN = (String)m.get("LINKEDINID");
   sPASSPORTNUMBER = (String)m.get("PASSPORTNUMBER");
   sCOUNTRYOFISSUE = (String)m.get("COUNTRYOFISSUE");
   sPASSPORTEXPIRYDATE = (String)m.get("PASSPORTEXPIRYDATE");   
   sAddr1      = (String)m.get("ADDR1");
   sAddr2      = (String)m.get("ADDR2");
   sAddr3      = (String)m.get("ADDR3");
   sAddr4 =(String)m.get("ADDR4");
   sState    = (String)m.get("STATE");
   sCountry    = (String)m.get("COUNTRY");
   sZip        = (String)m.get("ZIP");
   
   sEMIRATESID = (String)m.get("EMIRATESID");
   sEMIRATESIDEXPIRY = (String)m.get("EMIRATESIDEXPIRY");
   sVISANUMBER = (String)m.get("VISANUMBER");
   sVISAEXPIRYDATE = (String)m.get("VISAEXPIRYDATE");   
   sLABOURCARDNUMBER      = (String)m.get("LABOURCARDNUMBER");
   sWORKPERMITNUMBER      = (String)m.get("WORKPERMITNUMBER");
   sCONTRACTSTARTDATE      = (String)m.get("CONTRACTSTARTDATE");
   sCONTRACTENDDATE =(String)m.get("CONTRACTENDDATE");
   sBANKNAME    = (String)m.get("BANKNAME");
   sBRANCH    = (String)m.get("BRANCH");
   sIBAN        = (String)m.get("IBAN");
   sBANKROUTINGCODE        = (String)m.get("BANKROUTINGCODE");
   
   BASICSALARY      = (String)m.get("BASICSALARY");
   HOUSERENTALLOWANCE      = (String)m.get("HOUSERENTALLOWANCE");
   TRANSPORTALLOWANCE =(String)m.get("TRANSPORTALLOWANCE");
   COMMUNICATIONALLOWANCE    = (String)m.get("COMMUNICATIONALLOWANCE");
   OTHERALLOWANCE    = (String)m.get("OTHERALLOWANCE");
   BONUS        = (String)m.get("BONUS");
   COMMISSION        = (String)m.get("COMMISSION");
   IMAGEPATH = (String)m.get("CATLOGPATH");
   sRemarks =(String)m.get("REMARKS");
   isActive =(String)m.get("IsActive");
   sCountryCode = (String)m.get("COUNTRY_CODE");
   GRATUITY      = (String)m.get("GRATUITY");
   AIRTICKET      = (String)m.get("AIRTICKET");
   LEAVESALARY =(String)m.get("LEAVESALARY");
   ISPOSCUSTOMER = (String)m.get("ISPOSCUSTOMER");
   ISEDITPOSPRODUCTPRICE = (String)m.get("ISEDITPOSPRODUCTPRICE");
   ISCASHIER = (String)m.get("ISCASHIER");
   ISSALESMAN = (String)m.get("ISSALESMAN");
   sOutlet = (String)m.get("OUTNAME");
   sOutCode = (String)m.get("OUTLET");
   
   BASICSALARYVALUE="".equals(BASICSALARY) ? 0.0f :  Float.parseFloat(BASICSALARY);
   HOUSERENTALLOWANCEVALUE="".equals(HOUSERENTALLOWANCE) ? 0.0f :  Float.parseFloat(HOUSERENTALLOWANCE);
   TRANSPORTALLOWANCEVALUE="".equals(TRANSPORTALLOWANCE) ? 0.0f :  Float.parseFloat(TRANSPORTALLOWANCE);
   COMMUNICATIONALLOWANCEVALUE="".equals(COMMUNICATIONALLOWANCE) ? 0.0f :  Float.parseFloat(COMMUNICATIONALLOWANCE);
   OTHERALLOWANCEVALUE="".equals(OTHERALLOWANCE) ? 0.0f :  Float.parseFloat(OTHERALLOWANCE);
   BONUSVALUE="".equals(BONUS) ? 0.0f :  Float.parseFloat(BONUS);
   COMMISSIONVALUE="".equals(COMMISSION) ? 0.0f :  Float.parseFloat(COMMISSION);
   GRATUITYVALUE="".equals(GRATUITY) ? 0.0f :  Float.parseFloat(GRATUITY);
   AIRTICKETVALUE="".equals(AIRTICKET) ? 0.0f :  Float.parseFloat(AIRTICKET);
   LEAVESALARYVALUE="".equals(LEAVESALARY) ? 0.0f :  Float.parseFloat(LEAVESALARY);
   int eid = Integer.valueOf((String)m.get("ID"));
   String curtyear = dateutils.getYear();
   empleavedet = employeeLeaveDetDAO.EmployeeLeavedetlistpojo(plant, eid , curtyear);
   empSalarydet= hrEmpSalaryDetDAO.IsEmpSalarydetlist(plant,  "", eid);

   }catch(Exception e)
   {

       res="no details found for Employee id : "+  sCustCode;
   }
try{
	   euid=_userBean.getidbyuserid(sUserloginid,"DEPT='"+plant+"'");

}catch(Exception ex)
{
	
	}

 
}



BASICSALARY=StrUtils.addZeroes(BASICSALARYVALUE, numberOfDecimal);
HOUSERENTALLOWANCE=StrUtils.addZeroes(HOUSERENTALLOWANCEVALUE, numberOfDecimal);
TRANSPORTALLOWANCE=StrUtils.addZeroes(TRANSPORTALLOWANCEVALUE, numberOfDecimal);
COMMUNICATIONALLOWANCE=StrUtils.addZeroes(COMMUNICATIONALLOWANCEVALUE, numberOfDecimal);
OTHERALLOWANCE=StrUtils.addZeroes(OTHERALLOWANCEVALUE, numberOfDecimal);
BONUS=StrUtils.addZeroes(BONUSVALUE, numberOfDecimal);
COMMISSION=StrUtils.addZeroes(COMMISSIONVALUE, numberOfDecimal);
sSalary=StrUtils.addZeroes(sSalaryVALUE, numberOfDecimal);
GRATUITY=StrUtils.addZeroes(GRATUITYVALUE, numberOfDecimal);
AIRTICKET=StrUtils.addZeroes(AIRTICKETVALUE, numberOfDecimal);
LEAVESALARY=StrUtils.addZeroes(LEAVESALARYVALUE, numberOfDecimal);

%>
<style>
label {
   cursor: pointer;
   /* Style as you please, it will become the visible UI component. */
}
#logofile {
   opacity: 0;
   position: absolute;
   z-index: -1;
}
</style>
<div class="container-fluid m-t-20">
	 <div class="box"> 
	 <!-- Muruganantham Modified on 16.02.2022 -->
            <ul class="breadcrumb backpageul">      	
                <li><a href="../home"><span class="underline-on-hover">Dashboard</span> </a></li>   
                <li><a href="../payroll/employee"><span class="underline-on-hover">Employee Summary</span> </a></li>                                           
                <li><label>Edit Employee</label></li>                                   
            </ul>             
    <!-- Muruganantham Modified on 16.02.2022 --> 
            <div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title"><%=title %></h1>
              <h1 style="font-size: 18px; cursor: pointer;"
				class="box-title pull-right"
				onclick="window.location.href='../payroll/employee'">
				<i class="glyphicon glyphicon-remove"></i>
			</h1>
		</div>
		
 <div class="box-body">

   <CENTER><strong id="msg"><%=res%></strong></CENTER>
<form class="form-horizontal" id="empform" autocomplete="off" name="form" method="post">
<div class="col-sm-6">
<div class="row">
<div class="form-group">
      <label class="control-label col-form-label col-sm-4 required" for="Create Employee ID">Employee Id</label>
      <div class="col-sm-8">
     <!--  <div class="input-group">    -->
      	  	 <input name="CUST_CODE" type="TEXT" value="<%=sCustCode%>"	size="50" readonly MAXLENGTH=50 class="form-control" onkeypress="if((event.keyCode=='13') && ( document.form.CUST_NAME.value.length > 0)) {getEmployeeDetails();}"> 

   		 <!-- 	<span class="btn-danger input-group-addon" id="btnpop"
								onclick="javascript:popUpWin('employee_list.jsp?EMP_NAME='+form.CUST_NAME.value);"><span
								class="glyphicon glyphicon-search" aria-hidden="true"></span></span> -->
  		</div>
  		<INPUT type="hidden" name="CUST_CODE1" value="<%=sCustCode%>">
  		<INPUT type="hidden" name="COUNTRY" value="<%=sCountry%>">
  		<INPUT type="hidden" name="EDIT_STATE" value="<%=sState%>">
  		<INPUT type="hidden" name="SAVE_RED" value="<%=sSAVE_RED%>">
  		<INPUT type="hidden" name="EDIT_COUNTRY" value="<%=sCountryCode%>">
  		<INPUT type="hidden" name="EDIT_COUNTRYOFISSUE" value="<%=sCOUNTRYOFISSUE%>">
  		<INPUT type="hidden" name="EDIT_BANKNAME" value="<%=sBANKNAME%>">
  		<INPUT type="hidden" name="ISEDIT" value="<%=action%>">
  		<INPUT type="hidden" name="empid" value="<%=empid%>">
  		<INPUT type="hidden" name="SAVE_REDELETE" value="<%=sSAVE_REDELETE%>">
  		<INPUT type="hidden" name="repid" value="<%=sRepid%>">
  		<INPUT type="hidden" name="uid" value="<%=uid%>">
  		<INPUT type="hidden" name="euid" value="<%=euid%>">
  		<INPUT type="hidden" name="ispay" value="<%=ispay%>">
  		<input type = "hidden" name="OLD_CUST_NAME" value="<%=sCustName%>">
   <!--    </div> -->
     <!--  <div class=form-inline>
    <div class="col-sm-1" id="btnView">
      	<button type="button" class="Submit btn btn-default" onClick="getEmployeeDetails();"><b>View</b></button>&nbsp;&nbsp;
    </div></div> -->
    </div>
    
  <%--   <div class="form-group">
      <label class="control-label col-form-label col-sm-4">Send Auto Email (General Payroll)</label>
      <div class="col-sm-4">
      	<INPUT type="hidden" name="isautoemail" value="<%=isautoemail%>">                
       	<input type="checkbox" class="form-check-input" id="autoemail" name="autoemail" Onclick="setautoemail(this)" <%if(isautoemail.equalsIgnoreCase("1")){%>checked<%}%>>
      </div>
    </div> --%>
    
    <div class="form-group">
      <label class="control-label col-form-label col-sm-4 required" for="Employee Name">Employee Name</label>
      <div class="col-sm-8">                
        <INPUT  class="form-control" name="CUST_NAME" type="TEXT" value="<%=sCustName%>"
			size="50" MAXLENGTH=100>
      </div>
    </div>
    

    <%if(ispay.equalsIgnoreCase("0")){ %>
     <div class="form-group">
      <label class="control-label col-form-label col-sm-4" for="Employee Login ID">Employee Login ID</label>
      <div class="col-sm-8">                
        <INPUT  class="form-control loginuserid" name="EMP_LOGIN_ID" type="TEXT" size="50" <%if(ISCREATEONUSERINFO.equals("1")) {%>readonly <%}%> MAXLENGTH=100  value="<%=sUserloginid%>">
        <INPUT  class="form-control" name="PASSWORD" type="hidden" size="50" MAXLENGTH=100  value="<%=sPassword%>" readonly>
      </div>
    </div>
    
   <%--  <div class="form-group" hidden>
      <label class="control-label col-form-label col-sm-4" for="PASSWORD">Password</label>
      <div class="col-sm-8">                
        <INPUT  class="form-control" name="PASSWORD" type="password" size="50" MAXLENGTH=100  value="<%=sPassword%>" readonly>
      </div>
    </div> --%>
    <%}else{ %>
     <div class="form-group">
      <label class="control-label col-form-label col-sm-4 required" for="Employee Login ID">Employee Login ID</label>
      <div class="col-sm-8">                
        <INPUT  class="form-control loginuserid" name="EMP_LOGIN_ID" type="TEXT" size="50" MAXLENGTH=100 <%if(ISCREATEONUSERINFO.equals("1")) {%>readonly <%}%>  value="<%=sUserloginid%>">
        <INPUT  class="form-control" name="PASSWORD" type="hidden" size="50" MAXLENGTH=100  value="<%=sPassword%>" readonly>
      </div>
    </div>
    
    <%-- <div class="form-group" hidden>
      <label class="control-label col-form-label col-sm-4 required" for="PASSWORD">Password</label>
      <div class="col-sm-8">                
        <INPUT  class="form-control" name="PASSWORD" type="hidden" size="50" MAXLENGTH=100  value="<%=sPassword%>" readonly>
      </div>
    </div> --%>
    <%} %>
    
    <div class="form-group">
      <label class="control-label col-form-label col-sm-4 required" for="PASSWORD">Password</label>
      <div class="col-sm-8">
      <%
      String decryptpassword = "";
      if(sPassword.length() > 0){
      	decryptpassword = eb.decrypt(sPassword);
      }
      %>                
        <INPUT  class="form-control" id="password-field" name="CPASSWORD" type="password" size="50" MAXLENGTH=100  value="<%=decryptpassword%>">
        <span class="input-group-btn phideshow">
            <button class="btn btn-default reveal" type="button" style="background: white;"><i class="fa fa-fw fa-eye" id="peye" style="color: #23527c;"></i></button>
          </span>  
      </div>
    </div>
    <%if(!empleavedet.isEmpty()){ %>
	    <div class="form-group">
	      <label class="control-label col-form-label col-sm-4" for="Employee Type">Employee Type</label>
	      <div class="col-sm-8">                
				<input type="hidden" name="employeetypeid" value="<%=sEmptypeid%>">
				<input type="text" class="form-control emptype" name="employeetype" value="<%=sEmptype%>" disabled>				
	      </div>
	    </div>
    <%}else{ %>
    <div class="form-group">
      <label class="control-label col-form-label col-sm-4" for="Employee Type">Employee Type</label>
      <div class="col-sm-8">                
			<input type="hidden" name="employeetypeid" value="<%=sEmptypeid%>">
			<input type="text" class="form-control emptype" name="employeetype" value="<%=sEmptype%>">				
      </div>
    </div>
    <%} %>
    
     <%-- <div class="form-group">
      <label class="control-label col-form-label col-sm-4" for="Working man hours">Working man hours</label>
      <div class="col-sm-4">                
        <INPUT  class="form-control WORK_MAN_HOURS" name="WORK_MAN_HOURS" type="TEXT" value="<%=sManworkhour%>" size="50" MAXLENGTH=100>
      </div>
    </div> --%>
    
    <div class="form-group">
      <label class="control-label col-form-label col-sm-4 required" for="Gender">Gender</label>
      <div class="col-sm-8">                
        <label class="radio-inline">
      <INPUT name="GENDER"  type = "radio" value="M"    <%if(sGender.equalsIgnoreCase("M")) {%>checked <%}%> >Male 
    </label>
    <label class="radio-inline">
      <INPUT name="GENDER" type = "radio" value="F"    <%if(sGender.equalsIgnoreCase("F")) {%>checked <%}%>  >Female
    </label>
      </div>
    </div>
    
    <div class="form-group">
      <label class="control-label col-form-label col-sm-4" for="Date of Birth">Date of Birth</label>
      <div class="col-sm-8">                
        <input name="DOB" type="TEXT" value="<%=sDOB%>"	size="50" MAXLENGTH=10 class="form-control" readonly id="DOP">
      </div>
    </div>
    
    <div class="form-group">
      <label class="control-label col-form-label col-sm-4" for="Employee Phone">Employee Phone</label>
      <div class="col-sm-8">                
        <INPUT name="TELNO" type="TEXT" value="<%=sTelNo%>" size="50" class="form-control" onkeypress="return validateInput(event)"
			MAXLENGTH="30">
      </div>
    </div>
    <%-- <%if(ispay.equalsIgnoreCase("0")){ %> --%>
	    <div class="form-group">
	      <label class="control-label col-form-label col-sm-4" for="Employee Email">Employee Email</label>
	      <div class="col-sm-8">                
	        <INPUT name="EMAIL" type="TEXT" value="<%=sEmail%>" size="50" MAXLENGTH="50" class="form-control">
	      </div>
	    </div>
    <%-- <%}else{ %>
	    <div class="form-group">
	      <label class="control-label col-form-label col-sm-4 required" for="Employee Email">Employee Email</label>
	      <div class="col-sm-8">                
	        <INPUT name="EMAIL" type="TEXT" value="<%=sEmail%>" size="50" MAXLENGTH="50" class="form-control">
	      </div>
	    </div>
    <%} %> --%>
    
    <div class="form-group">
      <label class="control-label col-form-label col-sm-4" for="Passport Number">Passport Number</label>
      <div class="col-sm-8">                
        <INPUT name="PASSPORTNUMBER" type="TEXT" value="<%=sPASSPORTNUMBER%>" size="50" MAXLENGTH="50" class="form-control">
      </div>
    </div>
    
    <div class="form-group">
      <label class="control-label col-form-label col-sm-4" for="Country of Issue">Country of Issue</label>
      <div class="col-sm-8">
        <SELECT class="form-control" data-toggle="dropdown" data-placement="right" id="COUNTRYOFISSUE" name="COUNTRYOFISSUE" value="<%=sCOUNTRYOFISSUE%>" style="width: 100%">
				<OPTION style="display:none;">Select Country</OPTION>
				<%
		   MasterUtil _MasterUtil=new  MasterUtil();
		   ArrayList ccList =  _MasterUtil.getCountryList("",plant,region);
			for(int i=0 ; i<ccList.size();i++)
      		 {
				Map m=(Map)ccList.get(i);
				String vCOUNTRYNAME = (String)m.get("COUNTRYNAME");
				 %>
		        <option  value='<%=vCOUNTRYNAME%>' ><%=vCOUNTRYNAME%> </option>		          
		        <%
       			}
			 %></SELECT>
      </div>
    </div>
    
    <div class="form-group">
      <label class="control-label col-form-label col-sm-4" for="Passport ExpiryDate">Passport ExpiryDate</label>
      <div class="col-sm-8">                
        <input name="PASSPORTEXPIRYDATE" type="TEXT" value="<%=sPASSPORTEXPIRYDATE%>"	size="50" MAXLENGTH=10 class="form-control" readonly id="PASSPORTEXPIRYDATE">
      </div>
    </div>
    <%if(ispay.equalsIgnoreCase("0")){ %>
        <div class="form-group employee-section">
					<label class="control-label col-form-label col-sm-4">Employee Reporting</label>
					<div class="col-sm-8 ac-box">
						<input type="text" class="ac-selected form-control typeahead"  value="<%=sRepname%>"
							id="EMP_REPORTING" name="EMP_REPORTING" placeholder="Select a reporting incharge">
						<span class="select-icon"
							onclick="$(this).parent().find('input[name=\'EMP_REPORTING\']').focus()"><i
							class="glyphicon glyphicon-menu-down"></i></span>
					</div>
				</div>
				
	<%}else{ %>
	<div class="form-group employee-section">
					<label class="control-label col-form-label col-sm-4 required">Employee Reporting</label>
					<div class="col-sm-8 ac-box">
						<input type="text" class="ac-selected form-control typeahead"  value="<%=sRepname%>"
							id="EMP_REPORTING" name="EMP_REPORTING" placeholder="Select a reporting incharge">
						<span class="select-icon"
							onclick="$(this).parent().find('input[name=\'EMP_REPORTING\']').focus()"><i
							class="glyphicon glyphicon-menu-down"></i></span>
					</div>
				</div>
	<%} %>
	<div class="form-group">
      		<label class="control-label col-form-label col-sm-4" for="Outlet Name">POS Outlets</label>
      			<div class="col-sm-8">
                	<INPUT class=" form-control" id="OUTLET_NAME" value="<%=sOutlet%>" name="OUTLET_NAME" type="TEXT" size="30" MAXLENGTH=100 placeholder="Select Outlet">
    		 		<span class="select-icon" style="right: 45px;" onclick="$(this).parent().find('input[name=\'OUTLET_NAME\']').focus()">
					<i class="glyphicon glyphicon-menu-down"></i></span>    						
					<INPUT type="hidden" name="OUTCODE" value="<%=sOutCode%>">
     			</div>
    	</div>
    
    </div>
    </div>
    
    <div class="col-sm-6 text-center" style="padding-left: 5%;">
    	<div class="col-sm-12" style="padding-bottom: 5%;">
				<div class="col-sm-12"  style="text-align: left;">
					<%-- <%if(!(new File(imagePath).exists())){%>
						<label>Upload Your Company Logo:</label>
					<%}else{ %> --%>
						<label>Employee Image:</label>
					<%-- <%} %> --%>
				</div>
				<div class="col-sm-5" style="border: 1px dashed #ddd;border-radius: 4px;padding: 10px;height:75px">
						<div id="item_logo" <%if(IMAGEPATH.equalsIgnoreCase("")){%> hidden <%} %>>
							<img id="item_img_logo1" src="<%=(IMAGEPATH.equalsIgnoreCase("")) ? "../jsp/dist/img/NO_IMG.png" : "/track/ReadFileServlet/?fileLocation="+IMAGEPATH%>" style="width: 100px; height: 50px"/>
						</div>
						<div  id="item_btn_logo" <%if(!IMAGEPATH.equalsIgnoreCase("")){%> hidden <%} %>>
							<label for="logofile" style="color: #337ab7;padding-top: 15px;">Upload Employee Image</label>
							<input accept="image/gif,image/jpeg,image/png,image/bmp" name="IMAGE_UPLOAD1"  type="File" size="20" id="logofile" onchange="readURLLogo(this);">
					  	</div>
				</div>
				<div class="col-sm-7">
					<div id="logoremove" <%if(IMAGEPATH.equalsIgnoreCase("")){%> hidden <%} %>   style="text-align: left;">
						<a href="#" onClick="image_delete_new();">Remove Employee Image</a>	
					</div>
				</div>
			</div>
			
			<div class="col-sm-12" style="padding-bottom: 5%;text-align: left;">
      			<label class ="checkbox-inline">
	            <INPUT type="hidden" name="ISCREATEONUSERINFO" value="<%=ISCREATEONUSERINFO%>">
	            <input type = "checkbox" class="form-check-input" id = "ISCREATEONUSERINFO" name = "ISCREATEONUSERINFO" value = "ISCREATEONUSERINFO"  Onclick="setautoemp(this)"
	            <%if(ISCREATEONUSERINFO.equals("1")) {%>checked <%}%> >Create Employee Login On User Details</label>
			</div>
        
    		<div class="col-sm-12" style="padding-bottom: 5%;text-align: left;">
    			<INPUT type="hidden" name="isautoemail" value="<%=isautoemail%>">                
       			<lable class="checkbox-inline"><input type="checkbox" class="form-check-input" id="autoemail" name="autoemail" Onclick="setautoemail(this)" <%if(isautoemail.equalsIgnoreCase("1")){%>checked<%}%>>Send Auto Email (General Payroll)</lable>
			</div>
			<div class="col-sm-12" style="padding-bottom: 5%; text-align: left;">
      			<INPUT type="hidden" name="ISPOSCUSTOMER" value="<%=ISPOSCUSTOMER%>">
		    	<lable class="checkbox-inline"><input type="checkbox" class="form-check-input" id="POSCUSTOMER" name="POSCUSTOMER" <%if(ISPOSCUSTOMER.equalsIgnoreCase("1")){%>checked<%}%> Onclick="setposcustomer(this)">Allow to Create POS Customer</lable>
			</div>
			<div class="col-sm-12" style="padding-bottom: 5%; text-align: left;">
      			<INPUT type="hidden" name="ISEDITPOSPRODUCTPRICE" value="<%=ISEDITPOSPRODUCTPRICE%>">
		    	<lable class="checkbox-inline"><input type="checkbox" class="form-check-input" id="EDITPOSPRODUCTPRICE" name="EDITPOSPRODUCTPRICE" <%if(ISEDITPOSPRODUCTPRICE.equalsIgnoreCase("1")){%>checked<%}%> Onclick="setproductprice(this)">Allow to Edit POS Product Prices</lable>
			</div>
    
		<%-- <div class="row">
			<div class="col-sm-12">
				<INPUT size="50" type="hidden" id="imagetmppath" MAXLENGTH=200 name="imagetmppath" value="<%=IMAGEPATH%>"> 
				<img class="img-thumbnail" id="emp_img" name="CATALOGPATH" style="width:35%"
					src="<%=(IMAGEPATH.equalsIgnoreCase("")) ? "../jsp/dist/img/NO_IMG.png" : "/track/ReadFileServlet/?fileLocation="+IMAGEPATH%>">

			</div>
		</div>
		<div class="row">
			<div class="col-sm-12">
				<div class="form-group" id="btnUpload">
					<label>Upload Employee Image:</label> <INPUT style="width: 100%;" class="form-control" id="userImage" name="IMAGE_UPLOAD1" type="File" size="20" MAXLENGTH=100> 
					<br> 
					<INPUT class=" btn btn-default" style="font-size: 85%;" type="BUTTON" value="Remove Employee Image" onClick="image_delete();"> 
					<INPUT class=" btn btn-default" style="font-size: 85%;" type="BUTTON" value="Upload & Save Employee Image" onClick="image_edit();">
				</div>
			</div>
		</div> --%>
	</div>
    <div class="col-sm-12">
    <div class="bs-example">
     <ul class="nav nav-tabs" id="myTab" style="font-size: 90%;">
     	<li class="nav-item active">
            <a href="#home" class="nav-link" data-toggle="tab" aria-expanded="true">Address</a>
        </li>
        <li class="nav-item">
            <a href="#identity" class="nav-link" data-toggle="tab"><%=pcountry%> Identity</a>
        </li>
        <li class="nav-item">
            <a href="#employment" class="nav-link" data-toggle="tab">Employment Details</a>
        </li>
        <li class="nav-item">
            <a href="#contract" class="nav-link" data-toggle="tab">Contract Details</a>
        </li>
        <li class="nav-item">
            <a href="#bank" class="nav-link" data-toggle="tab">Bank Account</a>
        </li>
         <li class="nav-item">
            <a href="#leavedet" class="nav-link" data-toggle="tab">Leave Details</a>
        </li>
        <li class="nav-item">
            <a href="#salary" class="nav-link" data-toggle="tab">Salary</a>
        </li>
        <li class="nav-item">
            <a href="#benefit" class="nav-link" data-toggle="tab">Benefit</a>
        </li>        
        <li class="nav-item">
            <a href="#remark" class="nav-link" data-toggle="tab">Remarks</a>
        </li>
         <li class="nav-item">
            <a href="#attachfiles" class="nav-link" data-toggle="tab">Attachments</a>
        </li>
        </ul>
        
        <div class="tab-content clearfix">
        <div class="tab-pane active" id="home">
        <br>
        <div class="form-group">
      <label class="control-label col-form-label col-sm-2" for="Country">Country</label>
      <div class="col-sm-4">  
       <SELECT class="form-control" data-toggle="dropdown" data-placement="right" onchange="OnCountry(this.value)" id="COUNTRY_CODE" name="COUNTRY_CODE" value="<%=sCountry%>" style="width: 100%">
				<OPTION style="display:none;">Select Country</OPTION>
				<%
		   _MasterUtil=new  MasterUtil();
		   ccList =  _MasterUtil.getCountryList("",plant,region);
			for(int i=0 ; i<ccList.size();i++)
      		 {
				Map m=(Map)ccList.get(i);
				String vCOUNTRYNAME = (String)m.get("COUNTRYNAME");
				String vCOUNTRY_CODE = (String)m.get("COUNTRY_CODE"); %>
		        <option  value='<%=vCOUNTRY_CODE%>' ><%=vCOUNTRYNAME%> </option>		          
		        <%
       			}
			 %></SELECT>
      </div>
    </div>
       <div class="form-group">
      <label class="control-label col-form-label col-sm-2" for="Unit No">Unit No.</label>
      <div class="col-sm-4">  
               
        <INPUT name="ADDR1" type="TEXT" value="<%=sAddr1%>" size="50"
			MAXLENGTH=50 class="form-control">
      </div>
    </div>
    <div class="form-group">
      <label class="control-label col-form-label col-sm-2" for="Building">Building</label>
      <div class="col-sm-4">
              
        <INPUT name="ADDR2" type="TEXT" value="<%=sAddr2%>" size="50"
			MAXLENGTH=50 class="form-control">
      </div>
    </div>
    <div class="form-group">
      <label class="control-label col-form-label col-sm-2" for="Street">Street</label>
      <div class="col-sm-4">
                
        <INPUT name="ADDR3" type="TEXT" value="<%=sAddr3%>" size="50"
			MAXLENGTH=50 class="form-control">
      </div>
    </div>
    <div class="form-group">
      <label class="control-label col-form-label col-sm-2" for="City">City</label>
      <div class="col-sm-4"> 
                
        <INPUT name="ADDR4" type="TEXT" value="<%=sAddr4%>" size="50"
			MAXLENGTH=80  class="form-control">
      </div>
    </div>
    
    <div class="form-group">
      <label class="control-label col-form-label col-sm-2" for="State">State</label>
      <div class="col-sm-4">
      <SELECT class="form-control" data-toggle="dropdown" data-placement="right" id="STATE" name="STATE" value="<%=sState%>" style="width: 100%">
				<OPTION style="display:none;">Select State</OPTION>
				</SELECT>
      </div>
    </div>
    
    <div class="form-group">
      <label class="control-label col-form-label col-sm-2" for="Postal Code">Postal Code</label>
      <div class="col-sm-4">
                
        <INPUT name="ZIP" type="TEXT" value="<%=sZip%>" size="50"
			MAXLENGTH=10 class="form-control">
      </div>
    </div>
    
        <div class="form-group">
      	<label class="control-label col-form-label col-sm-2" for="Facebook">Facebook Id</label>
      	<div class="col-sm-4">  
        <INPUT name="FACEBOOK" type="TEXT" value="<%=FACEBOOK%>"	size="50" MAXLENGTH=50 class="form-control">
      	</div>
    	</div>
    
        <div class="form-group">
      	<label class="control-label col-form-label col-sm-2" for="Twitter">Twitter Id</label>
      	<div class="col-sm-4">  
        <INPUT name="TWITTER" type="TEXT" value="<%=TWITTER%>"	size="50" MAXLENGTH=50 class="form-control">
      	</div>
    	</div>
    
        <div class="form-group">
      	<label class="control-label col-form-label col-sm-2" for="Linkedin">LinkedIn Id</label>
      	<div class="col-sm-4">  
        <INPUT name="LINKEDIN" type="TEXT" value="<%=LINKEDIN%>"	size="50" MAXLENGTH=50 class="form-control">
      	</div>
    	</div>
    
        <div class="form-group">
      	<label class="control-label col-form-label col-sm-2" for="SkypeId">Skype Id</label>
      	<div class="col-sm-4">  
        <INPUT name="SKYPE" type="TEXT" value="<%=SKYPE%>"	size="50" MAXLENGTH=50 class="form-control">
      	</div>
    	</div>
    	
    	<div class="form-group">
  <div class="col-sm-offset-2 col-sm-8">
    <label class="radio-inline">
      <input type="radio" name="ACTIVE" value="Y"<%if (isActive.equalsIgnoreCase("Y")) {%> checked <%}%>>Active
    </label>
    <label class="radio-inline">
      <input type="radio" name="ACTIVE" value="N"<%if (isActive.equalsIgnoreCase("N")) {%> checked <%}%>>Non Active
    </label>
     </div>
</div>
    	
        </div>
        
        <div class="tab-pane fade" id="identity">
        <br>
        
        <div class="form-group">
      	<label class="control-label col-form-label col-sm-2" for="EmiratesId NO"><%=pcountry%> ID Number</label>
      	<div class="col-sm-4">  
        <INPUT name="EMIRATESID" type="TEXT" value="<%=sEMIRATESID%>"	size="50" MAXLENGTH=50 class="form-control">
      	</div>
    	</div>
    	
    	<div class="form-group">
      <label class="control-label col-form-label col-sm-2" for="Emirates ExpiryDate"><%=pcountry%> ExpiryDate</label>
      <div class="col-sm-4">                
        <input name="EMIRATESIDEXPIRY" type="TEXT" value="<%=sEMIRATESIDEXPIRY%>"	size="50" MAXLENGTH=10 class="form-control" readonly id="EMIRATESIDEXPIRY">
      </div>
       </div>
       
       <div class="form-group">
      	<label class="control-label col-form-label col-sm-2" for="Visa NO">Visa Number</label>
      	<div class="col-sm-4">  
        <INPUT name="VISANUMBER" type="TEXT" value="<%=sVISANUMBER%>"	size="50" MAXLENGTH=50 class="form-control">
      	</div>
    	</div>
    	
    	<div class="form-group">
      <label class="control-label col-form-label col-sm-2" for="Visa ExpiryDate">Visa ExpiryDate</label>
      <div class="col-sm-4">                
        <input name="VISAEXPIRYDATE" type="TEXT" value="<%=sVISAEXPIRYDATE%>"	size="50" MAXLENGTH=10 class="form-control" readonly id="VISAEXPIRYDATE">
      </div>
       </div>
        
        </div>        
        
        <div class="tab-pane fade" id="employment">
        <br>
        
        <div class="form-group">
        <input type="hidden" name="departmentid" value="0">
      	<label class="control-label col-form-label col-sm-2" for="Department">Department</label>
      	<%-- <div class="col-sm-4">  
        <INPUT name="DEPT" type="TEXT" value="<%=sDept%>"	size="50" MAXLENGTH=100 class="form-control">
      	</div> --%>
      <div class="col-sm-4"> 
      		<input type="text" class="ac-selected form-control typeahead"
							id="DEPT" name="DEPT" placeholder="Department" value="<%=sDept%>">
						<span class="select-icon"
							onclick="$(this).parent().find('input[name=\'DEPT\']').focus()"><i
							class="glyphicon glyphicon-menu-down"></i></span> 
      </div>
    	</div>
    	
    	<div class="form-group">
      	<label class="control-label col-form-label col-sm-2" for="Designation">Designation</label>
      	<div class="col-sm-4">  
        <INPUT name="DESGINATION" type="TEXT" value="<%=sDesgination%>"	size="50" MAXLENGTH=100 class="form-control">
      	</div>
      	<div class="col-sm-2" style="padding-bottom: 0%;">
      	<INPUT type="hidden" name="ISCASHIER" value="<%=ISCASHIER%>">
		<lable class="checkbox-inline"><input type="checkbox" class="form-check-input" id="CASHIER" name="CASHIER"  <%if(ISCASHIER.equalsIgnoreCase("1")){%>checked<%}%>  Onclick="setcashier(this)">Cashier</lable>
		</div>
      	<INPUT type="hidden" name="ISSALESMAN" value="<%=ISSALESMAN%>">
		<lable class="checkbox-inline"><input type="checkbox" class="form-check-input" id="SALESMAN" name="SALESMAN"  <%if(ISSALESMAN.equalsIgnoreCase("1")){%>checked<%}%>  Onclick="setsalesman(this)">Sales Person</lable>
    	</div>
    	
    	<div class="form-group">
      <label class="control-label col-form-label col-sm-2" for="Date of Joining">Date of Joining</label>
      <div class="col-sm-4">                
        <input name="DATEOFJOINING" type="TEXT" value="<%=sDATEOFJOINING%>"	size="50" MAXLENGTH=10 class="form-control" readonly id="DATEOFJOINING">
      </div>
       </div>
       
       <div class="form-group">
      <label class="control-label col-form-label col-sm-2" for="Date of Leaving">Date of Leaving</label>
      <div class="col-sm-4">                
        <input name="DATEOFLEAVING" type="TEXT" value="<%=sDATEOFLEAVING%>"	size="50" MAXLENGTH=10 class="form-control" readonly id="DATEOFLEAVING">
      </div>
       </div>
        
        </div>
        
        <div class="tab-pane fade" id="contract">
        <br>
        
        <div class="form-group">
      	<label class="control-label col-form-label col-sm-2" for="Labour Card Number">Labour Card Number</label>
      	<div class="col-sm-4">  
        <INPUT name="LABOURCARDNUMBER" type="TEXT" value="<%=sLABOURCARDNUMBER%>"	size="50" MAXLENGTH=100 class="form-control">
      	</div>
    	</div>
    	
    	<div class="form-group">
      	<label class="control-label col-form-label col-sm-2" for="Work Permit Number">Work Permit Number</label>
      	<div class="col-sm-4">  
        <INPUT name="WORKPERMITNUMBER" type="TEXT" value="<%=sWORKPERMITNUMBER%>"	size="50" MAXLENGTH=100 class="form-control">
      	</div>
    	</div>
    	
    	<div class="form-group">
      <label class="control-label col-form-label col-sm-2" for="Contract StartDate">Contract StartDate</label>
      <div class="col-sm-4">                
        <input name="CONTRACTSTARTDATE" type="TEXT" value="<%=sCONTRACTSTARTDATE%>"	size="50" MAXLENGTH=10 class="form-control" readonly id="CONTRACTSTARTDATE">
      </div>
       </div>
       
       <div class="form-group">
      <label class="control-label col-form-label col-sm-2" for="Contract ExpiryDate">Contract ExpiryDate</label>
      <div class="col-sm-4">                
        <input name="CONTRACTENDDATE" type="TEXT" value="<%=sCONTRACTENDDATE%>"	size="50" MAXLENGTH=10 class="form-control" readonly id="CONTRACTENDDATE">
      </div>
       </div>
        
        </div>
        
        <div class="tab-pane fade" id="bank">
        <br>
        
        <div class="form-group">
      	<label class="control-label col-form-label col-sm-2" for="IBAN">IBAN</label>
      	<div class="col-sm-4">  
        <INPUT name="IBAN" type="TEXT" value="<%=sIBAN%>"	size="50" MAXLENGTH=100 class="form-control">
      	</div>
    	</div>
       
       <div class="form-group">
			<label class="control-label col-form-label col-sm-2">Bank</label>
			<div class="col-sm-4 ac-box">				
<%-- 				<SELECT class="form-control" data-toggle="dropdown" data-placement="right" onchange="OnBank(this.value)" id="BANKNAME" name="BANKNAME" value="<%=sBANKNAME%>" style="width: 100%">
				<OPTION style="display:none;">Select Bank</OPTION>
				<%
		    _MasterUtil=new  MasterUtil();
		    ccList =  _MasterUtil.getBankList("",plant);
			for(int i=0 ; i<ccList.size();i++)
      		 {
				Map m=(Map)ccList.get(i);
				String vNAME = (String)m.get("NAME"); %>
		        <option  value='<%=vNAME%>' ><%=vNAME %> </option>		          
		        <%
       			}
			 %></SELECT> --%>
			       		<input type="text" class="ac-selected form-control typeahead"
							id="BANKNAME" name="BANKNAME" placeholder="Bank" value="<%=sBANKNAME%>" >
						<span class="select-icon"
							onclick="$(this).parent().find('input[name=\'BANKNAME\']').focus()"><i
							class="glyphicon glyphicon-menu-down"></i></span> 
			</div>
		</div>
		
		<div class="form-group">
      	<label class="control-label col-form-label col-sm-2" for="Branch">Branch</label>
      	<div class="col-sm-4">  
        <INPUT name="BRANCH" type="TEXT" value="<%=sBRANCH%>"	size="50" MAXLENGTH=100 class="form-control" readonly>
      	</div>
    	</div>
        
        <div class="form-group">
      	<label class="control-label col-form-label col-sm-2" for="Routing Code">Routing Code</label>
      	<div class="col-sm-4">  
        <INPUT name="BANKROUTINGCODE" type="TEXT" value="<%=sBANKROUTINGCODE%>"	size="50" MAXLENGTH=100 class="form-control">
      	</div>
    	</div>
        
        </div>
        
        
        
        <div class="tab-pane fade" id="leavedet">
        <br>
        	<div class="row" style="margin: 0px; width: 95%; margin-left: 15px;">
					<table class="table table-bordered line-item-table leavetype-table">
						<thead>
							<tr>
								<th>Leave Type</th>
								<th>Total Entitlement</th>
								<th>Year</th>
								<th>Notes</th>
							</tr>
						</thead>
						<%if(!empleavedet.isEmpty()){ %>
						<tbody>
						<%for(EmployeeLeaveDETpojo employeeLeaveDETpojo:empleavedet){ %>
							<tr>
								<td class="text-center">
									<input type="hidden" name="leavetypeid" value="<%=employeeLeaveDETpojo.getLEAVETYPEID()%>" disabled>
									<input class="form-control text-left leavetype" name="leavetype" type="text" value="<%=employeeLeaveDETpojo.getLEAVETYPE() %>" placeholder="Enter Leave Type" maxlength="100" disabled></td>
								<td class="text-center">
									<input class="form-control text-left totalentitlement" type="text" name="totalentitlement" placeholder="Enter Totalentitlement" value="<%=employeeLeaveDETpojo.getTOTALENTITLEMENT() %>" disabled>
								</td>
								<td class="text-center">
									<input class="form-control text-left leaveyear" type="text" name="leaveyear" value="<%=employeeLeaveDETpojo.getLEAVEYEAR() %>" disabled>
									<%-- <select class="form-control text-left leaveyear" name="leaveyear" value="<%=employeeLeaveDETpojo %>"></select> --%>
								<td class="text-center">
									<textarea  name="notes" class="form-control text-left" maxlength="1000" placeholder="Max 1000 characters" disabled><%=employeeLeaveDETpojo.getNOTE()%></textarea>
								</td>
							
							</tr>
						<%} %>
						</tbody>
					
						<%}else{%>
						<tbody>
							<tr>
								<td class="text-center">
									<input type="hidden" name="leavetypeid" value="0">
									<input class="form-control text-left leavetype" name="leavetype" type="text" placeholder="Enter Leave Type" maxlength="100"></td>
								<td class="text-center">
									<input class="form-control text-left totalentitlement" type="text" name="totalentitlement" placeholder="Enter Totalentitlement" value="0.0">
								</td>
								<td class="text-center">
									<select class="form-control text-left leaveyear" name="leaveyear"></select>
								<td class="text-center">
									<textarea  name="notes" class="form-control text-left" maxlength="1000" placeholder="Max 1000 characters"></textarea>
								</td>
							
							</tr>
						</tbody>	
						<%}%>
					</table>
			</div>
			<div class="form-group">
					<div class="row">
						<div class="col-sm-6">
							<a class="add-line"
								style="text-decoration: none; cursor: pointer;margin-left: 5%;"
								onclick="addRowleave()">+ Add another Leave Detail</a>
						</div>
					</div>
			</div>
        
        </div>
        
        
        
        
        
        <div class="tab-pane fade" id="salary">
        <br>
        
        <div class="row" style="margin: 0px; width: 95%; margin-left: 15px;">
					<table class="table table-bordered line-item-table empSalary-table">
						<thead>
							<tr>
								<th>Salary Type</th>
								<% if(pcountry.equals("Singapore")) { %> 
								<th>Deduct CPF Contribution</th>
								<%} else{%> 
								<th>Deduct PF Contribution</th>
								<%}%>
								<th>Amount</th>
							</tr>
						</thead>
						<%if(!empSalarydet.isEmpty()){ int loopid=0; %>
						<tbody>
						<%for(HrEmpSalaryDET hrEmpSalaryDet:empSalarydet){ loopid=loopid+1;
						List<HrEmpSalaryMst> SalaryList = hrEmpSalaryService.IsSalarylistdropdown(plant, hrEmpSalaryDet.getSALARYTYPE());
						for (HrEmpSalaryMst hrEmpType : SalaryList) {
							ISPAYROLL_BY_BASIC_SALARY =String.valueOf(hrEmpType.getISPAYROLL_BY_BASIC_SALARY());
						}
						
						String hrsSalary=StrUtils.addZeroes(hrEmpSalaryDet.getSALARY(), numberOfDecimal);
						%>
							<tr>
								<td class="text-center">
									<input type="hidden" name="empSalaryid" value="<%=hrEmpSalaryDet.getID()%>">									
									<input class="form-control text-left empSalary" name="empSalary" type="text" placeholder="Enter Salary Type" maxlength="100"value="<%=hrEmpSalaryDet.getSALARYTYPE()%>"></td>
									<td class="text-center">
									  <input type="hidden" name="PAYROLL_BY_BASIC_SALARY" value = "0">
									  <input type="checkbox" name="ISPAYROLL_BY_BASIC_SALARY" id="ISPAYROLL_BY_BASIC_SALARY" value="1" onclick="payrollbybasicsalary(this)" <%if(ISPAYROLL_BY_BASIC_SALARY.equalsIgnoreCase("1")) {%>checked <%}%> disabled />&nbsp;&nbsp;
								</td>
								<td class="text-center grey-bg" style="position:relative;">
								<%if(loopid>1){ %>
								<span class="glyphicon glyphicon-remove-circle empSalary-action" aria-hidden="true"></span>
								<%} %>
									<input class="form-control text-left" type="text" name="empSalaryAmt" placeholder="Enter Amount" value="<%=new java.math.BigDecimal(hrsSalary).toPlainString()%>" onkeypress="return isNumberKey(event,this,4)">
								</td>
							</tr>
						<%} %>
						</tbody>
					
						<%}else{%>
						<tbody>
						<tr>
								<td class="text-center">
									<input type="hidden" name="empSalaryid" value="0">									
									<input class="form-control text-left empSalary" name="empSalary" type="text" placeholder="Enter Salary Type" maxlength="100"></td>
								<td class="text-center">
									  <input type="hidden" name="PAYROLL_BY_BASIC_SALARY" value = "0">
									  <input type="checkbox" name="ISPAYROLL_BY_BASIC_SALARY" id="ISPAYROLL_BY_BASIC_SALARY" value="1" onclick="payrollbybasicsalary(this)" <%if(ISPAYROLL_BY_BASIC_SALARY.equalsIgnoreCase("1")) {%>checked <%}%>  disabled />&nbsp;&nbsp;
								</td>
								<td class="text-center">
									<input class="form-control text-left" type="text" name="empSalaryAmt" placeholder="Enter Amount" value="<%=new java.math.BigDecimal(sSalary).toPlainString()%>" onkeypress="return isNumberKey(event,this,4)">
								</td>
							</tr>
							</tbody>
							<%}%>
					</table>
			</div>
        <div class="form-group">
					<div class="row">
						<div class="col-sm-6">
							<a class="add-line"
								style="text-decoration: none; cursor: pointer;margin-left: 5%;"
								onclick="addSalaryRow()">+ Add another Salary Detail</a>
						</div>
					</div>
			</div>
        <%-- <div class="form-group">
      	<label class="control-label col-form-label col-sm-2" for="Working Man Days">Working Man Days</label>
      	<div class="col-sm-4">  
        <INPUT  class="form-control WORK_MAN_HOURS" name="WORK_MAN_HOURS" type="TEXT" value="<%=sManworkhour%>" size="50" MAXLENGTH=100>
      	</div>
    	</div> --%>
        
        <%-- <div class="form-group">
      	<label class="control-label col-form-label col-sm-2" for="Basic Salary">Basic Salary</label>
      	<div class="col-sm-4">  
        <INPUT name="BASICSALARY" type="TEXT" value="<%=new java.math.BigDecimal(BASICSALARY).toPlainString()%>" onkeypress="return isNumberKey(event,this,4)"	size="50" MAXLENGTH=50 class="form-control">
      	</div>
    	</div>
    	
    	<div class="form-group">
      	<label class="control-label col-form-label col-sm-2" for="House Rent Allowance">House Rent Allowance</label>
      	<div class="col-sm-4">  
        <INPUT name="HOUSERENTALLOWANCE" type="TEXT" value="<%=new java.math.BigDecimal(HOUSERENTALLOWANCE).toPlainString()%>" onkeypress="return isNumberKey(event,this,4)"	size="50" MAXLENGTH=50 class="form-control">
      	</div>
    	</div>
    	
    	<div class="form-group">
      	<label class="control-label col-form-label col-sm-2" for="Transport Allowance">Transport Allowance</label>
      	<div class="col-sm-4">  
        <INPUT name="TRANSPORTALLOWANCE" type="TEXT" value="<%=new java.math.BigDecimal(TRANSPORTALLOWANCE).toPlainString()%>" onkeypress="return isNumberKey(event,this,4)"	size="50" MAXLENGTH=50 class="form-control">
      	</div>
    	</div>
    	
    	<div class="form-group">
      	<label class="control-label col-form-label col-sm-2" for="Communication Allowance">Communication Allowance</label>
      	<div class="col-sm-4">  
        <INPUT name="COMMUNICATIONALLOWANCE" type="TEXT" value="<%=new java.math.BigDecimal(COMMUNICATIONALLOWANCE).toPlainString()%>" onkeypress="return isNumberKey(event,this,4)"	size="50" MAXLENGTH=50 class="form-control">
      	</div>
    	</div>
    	
    	<div class="form-group">
      	<label class="control-label col-form-label col-sm-2" for="Other Allowance">Other Allowance</label>
      	<div class="col-sm-4">  
        <INPUT name="OTHERALLOWANCE" type="TEXT" value="<%=new java.math.BigDecimal(OTHERALLOWANCE).toPlainString()%>" onkeypress="return isNumberKey(event,this,4)"	size="50" MAXLENGTH=50 class="form-control">
      	</div>
    	</div>
    	
    	<div class="form-group">
      	<label class="control-label col-form-label col-sm-2" for="Bonus">Bonus</label>
      	<div class="col-sm-4">  
        <INPUT name="BONUS" type="TEXT" value="<%=new java.math.BigDecimal(BONUS).toPlainString()%>" onkeypress="return isNumberKey(event,this,4)"	size="50" MAXLENGTH=50 class="form-control">
      	</div>
    	</div>
    	
    	<div class="form-group">
      	<label class="control-label col-form-label col-sm-2" for="Commission">Commission</label>
      	<div class="col-sm-4">  
        <INPUT name="COMMISSION" type="TEXT" value="<%=new java.math.BigDecimal(COMMISSION).toPlainString()%>" onkeypress="return isNumberKey(event,this,4)"	size="50" MAXLENGTH=50 class="form-control">
      	</div>
    	</div> --%>
        
        </div>        
        
        <div class="tab-pane fade" id="benefit">
        <br>
        <div class="form-group">
      	<label class="control-label col-form-label col-sm-2" for="Gratuity">Gratuity</label>
      	<div class="col-sm-4">  
        <INPUT name="GRATUITY" type="TEXT" value="<%=new java.math.BigDecimal(GRATUITY).toPlainString()%>" onkeypress="return isNumberKey(event,this,4)"	size="50" MAXLENGTH=50 class="form-control">
      	</div>
    	</div>
    	
    	<div class="form-group">
      	<label class="control-label col-form-label col-sm-2" for="Air Ticket">Air Ticket</label>
      	<div class="col-sm-4">  
        <INPUT name="AIRTICKET" type="TEXT" value="<%=new java.math.BigDecimal(AIRTICKET).toPlainString()%>" onkeypress="return isNumberKey(event,this,4)"	size="50" MAXLENGTH=50 class="form-control">
      	</div>
    	</div>
    	
    	<div class="form-group">
      	<label class="control-label col-form-label col-sm-2" for="Leave Salary">Leave Salary</label>
      	<div class="col-sm-4">  
        <INPUT name="LEAVESALARY" type="TEXT" value="<%=new java.math.BigDecimal(LEAVESALARY).toPlainString()%>" onkeypress="return isNumberKey(event,this,4)"	size="50" MAXLENGTH=50 class="form-control">
      	</div>
    	</div>
        </div>
        
        <div class="tab-pane fade" id="remark">
        <br>
       <div class="form-group">
      <label class="control-label col-form-label col-sm-2" for="Remarks">Remarks</label>
      <div class="col-sm-4">
        <textarea  class="form-control" name="REMARKS"   MAXLENGTH=1000><%=sRemarks%></textarea>
      </div>
    </div>
		     
        </div>
        
        <div class="tab-pane fade" id="attachfiles">
        <br>
				<div class="form-inline">
					<label for="email">Attach Files(s)</label>
					<div class="attch-section">
						<input type="file" class="form-control input-attch" id="empAttch" name="file" multiple="true">
						<div class="input-group">
							<svg version="1.1" id="Layer_1" xmlns="http://www.w3.org/2000/svg" x="0" y="0" viewBox="0 0 512 512" xml:space="preserve" class="icon icon-xs align-text-top action-icons input-group-addon" style="height:30px;display:inline-block;color: #c63616;"><path d="M262.4 512c-35.7 0-64.2-10.5-84.9-31.4-24.7-24.9-37.1-63.8-36.8-115.6.2-32.6-.3-248.7-1.4-268.1-.9-15.9 4.8-41.6 23.6-61.5 11.4-12 32-26.3 66-26.3 30.3 0 55.1 15.7 69.8 44.2 10.1 19.7 12.2 38.8 12.4 40.9l.1.9v.9l.5 236.9v.5c-1 19.2-15.9 47.6-53 47.6h-.7c-39.1-.4-53.7-30.4-56-46.2l-.2-1.3V179.6c0-10.2 8.3-18.5 18.5-18.5s18.5 8.3 18.5 18.5v150.5c.4 1.5 1.4 4.4 3.6 7.2s6.2 6.5 16 6.6c9.2.1 12.4-3.2 14.1-6 1.6-2.6 2.2-5.6 2.3-6.3l-.7-234.5c-.4-3-2.4-15.6-8.8-27.6-8.3-15.7-20.2-23.3-36.4-23.3-16.7 0-29.8 5-39.1 14.8-10.7 11.3-14 26.6-13.6 34 1.2 21.6 1.6 244.3 1.4 270.3-.2 41.6 8.5 71.7 26 89.3 13.5 13.6 33.2 20.4 58.7 20.4 17.2 0 31.8-5.9 43.5-17.7 18.9-18.9 30.1-53.4 30-92.2 0-19.6-.1-193.2-.1-250.9 0-10.2 8.3-18.5 18.5-18.5s18.5 8.3 18.5 18.5c0 57.6.1 231.2.1 250.8.1 49.1-14.8 92.3-40.8 118.4-18.6 18.7-42.7 28.6-69.6 28.6z"></path></svg>
							<button type="button" class="btn btn-sm btn-attch">Upload File</button>
						</div>
						
					</div>
				</div>
				<%if(Empattachlist.size()>0){ %>
						<div id="empAttchNote">
							<small class="text-muted"><div class="attachclass"><a><%=Empattachlist.size()%> files Attached</a>
									<div class="tooltiptext" style="width: 30%">
										
										<%for(int i =0; i<Empattachlist.size(); i++) {   
									  		Map attach=(Map)Empattachlist.get(i); %>
												<div class="row" style="padding-left:10px;padding-top:10px">
													<span class="text-danger col-sm-3">
														<%if(attach.get("FileType").toString().equalsIgnoreCase("application/pdf")) {%>
														<svg version="1.1" id="Layer_1" xmlns="http://www.w3.org/2000/svg" x="0" y="0" viewBox="0 0 512 512" xml:space="preserve" class="icon doc-icon icon-xxxlg"><path d="M314.2 318.9c-6.4-3.7-13-7.7-18.2-12.5-13.9-13-25.5-31.1-32.7-50.8.5-1.9.9-3.5 1.3-5.2 0 0 7.8-44.5 5.8-59.6-.3-2.1-.5-2.6-1-4.3l-.7-1.8c-2.1-4.9-6.3-10.6-12.9-10.4l-3.8-.6h-.1c-7.3 0-13.3 4.2-14.8 9.9-4.8 17.5.2 43.9 9 77.9l-2.2 5.7c-6.3 15.5-14.4 31.2-21.4 44.9l-.9 1.9c-7.4 14.5-14.2 26.8-20.2 37.2l-6.2 3.3c-.5.2-11.2 6-13.8 7.4-21.4 12.8-35.6 27.3-38 38.9-.8 3.7-.2 8.4 3.6 10.5l6.1 3c2.6 1.4 5.4 2 8.3 2 15.2 0 33-19 57.4-61.5 28.2-9.2 60.3-16.8 88.4-21 21.4 12 47.8 20.4 64.5 20.4 2.9 0 5.5-.3 7.6-.9 3.2-.8 5.9-2.6 7.5-5.1 3.2-4.9 3.9-11.5 3-18.5-.3-2.1-1.9-4.6-3.6-6.2-4.9-4.9-15.9-7.4-32.5-7.6-11.6 0-25.2 1-39.5 3zM158 405c2.8-7.6 13.8-22.7 30.1-36 1.1-.8 3.5-3.2 5.9-5.4-17.1 27.1-28.5 38-36 41.4zm96.5-222.2c4.9 0 7.7 12.4 7.9 23.9.2 11.6-2.4 19.7-5.9 25.8-2.8-8.9-4.1-22.9-4.1-32.1 0 0-.2-17.6 2.1-17.6zm-28.8 158.3c3.4-6.2 6.9-12.6 10.6-19.4 8.9-16.7 14.5-29.9 18.7-40.6 8.3 15 18.6 27.8 30.8 38.2 1.5 1.3 3.1 2.5 4.8 3.8-24.9 4.8-46.2 10.8-64.9 18zm148.1-9.1c8.8 2.2 8.9 6.7 7.4 7.7s-5.8 1.5-8.6 1.5c-8.9 0-20-4.1-35.4-10.7 6-.5 11.4-.7 16.3-.7 8.9 0 11.5 0 20.3 2.2z"></path><path d="M441.6 116.6L329 4.7c-3-3-7.1-4.7-11.3-4.7H94.1C76.5 0 62.4 14.2 62.4 31.7v448.5c0 17.5 14.2 31.7 31.7 31.7h320.6c17.3 0 31.3-14 31.4-31.3l.3-352.7c-.1-4.1-1.8-8.2-4.8-11.3zm-14.9 358c0 9.4-7.8 17.1-17.3 17.1H99.2c-9.5 0-17.3-7.7-17.3-17.1V36.3c0-9.4 7.8-17.1 17.3-17.1h172.4c9.5 0 17.3 7.7 17.3 17.1v83.5c0 18.7 14.7 33.8 34.1 33.8h86.5c9.5 0 17.3 7.7 17.3 17.1l-.1 303.9zM326.8 136c-10.8 0-19.6-8.8-19.6-19.6V24.6c0-4.4 5.3-6.5 8.3-3.4l106.6 106.5c3.1 3.1.9 8.3-3.4 8.3h-91.9z"></path></svg>
														<%}else if(attach.get("FileType").toString().equalsIgnoreCase("image/jpeg") || attach.get("FileType").toString().equalsIgnoreCase("image/png") || attach.get("FileType").toString().equalsIgnoreCase("image/gif") || attach.get("FileType").toString().equalsIgnoreCase("image/tiff")){ %>
														<svg version="1.1" id="Layer_1" xmlns="http://www.w3.org/2000/svg" x="0" y="0" viewBox="0 0 512 512" xml:space="preserve" class="icon doc-icon icon-xxxlg"><path d="M417.2 378.8H95.3c-7 0-12.8-5.7-12.8-12.8v-34.9c0-2.7.8-5.2 2.3-7.4l44.6-63c4-5.6 11.6-7 17.4-3.3l60.8 39.7c4.9 3.2 11.1 2.7 15.5-1.1l116.8-103.2c5.5-4.9 14.1-4.1 18.5 1.8l66.3 86c1.7 2.2 2.7 5 2.7 7.8v80.2c0 5.6-4.6 10.2-10.2 10.2z" fill="#40bab5"></path><path d="M212.2 157.7c23.2 0 42 19 42 42.4s-18.8 42.4-42 42.4-42-19-42-42.4c.1-23.4 18.9-42.4 42-42.4z" fill="#fbbe01"></path><path d="M462 60.8c16.5 0 30 13.5 30 30V422c0 16.5-13.5 30-30 30H50.4c-16.5 0-30-13.5-30-30V90.8c0-16.5 13.5-30 30-30H462m0-20H50.4c-27.6 0-50 22.4-50 50V422c0 27.6 22.4 50 50 50H462c27.6 0 50-22.4 50-50V90.8c0-27.6-22.4-50-50-50z" fill="#888"></path></svg>
														<%} else{%>
														<svg version="1.1" id="Layer_1" xmlns="http://www.w3.org/2000/svg" x="0" y="0" viewBox="0 0 512 512" xml:space="preserve" class="icon doc-icon icon-xxxlg"><path d="M270.825,70.55L212.17,3.66C210.13,1.334,207.187,0,204.093,0H55.941C49.076,0,43.51,5.566,43.51,12.431V304.57  c0,6.866,5.566,12.431,12.431,12.431h205.118c6.866,0,12.432-5.566,12.432-12.432V77.633  C273.491,75.027,272.544,72.51,270.825,70.55z M55.941,305.073V12.432H199.94v63.601c0,3.431,2.78,6.216,6.216,6.216h54.903  l0.006,222.824H55.941z"></path></svg>	
														<%} %>
													</span>
													<div class="col-sm-9" style="padding-left:16px"><span class="fileNameFont"><a><%=attach.get("FileName").toString() %></a></span><br><span class="fileTypeFont">File Size: <%=Integer.parseInt(attach.get("FileSize").toString())/1024 %>KB</span></div>
												</div>
												<div class="row bottomline">
														<span class="col-sm-6" Style="font-size:14px;"><i class="fa fa-download" aria-hidden="true" onclick="downloadFile(<%=attach.get("ID") %>,'<%=(String) attach.get("FileName") %>')"> Download</i></span>
														<span class="col-sm-6" Style="font-size:14px;float:right"><i class="fa fa-trash" aria-hidden="true" onclick="removeFile(<%=attach.get("ID") %>)"> Remove</i></span>
												</div>	
										<%} %>
										
									</div>
								</div>
								
							</small>
						</div>
						<%}else{ %>
						<div id="empAttchNote">
							<small class="text-muted">  You can upload a maximum of 5 files, 2MB each  </small>
						</div>
						<%} %>
        </div>
        
        </div>
        
        </div>
        </div>
    
    
    <!-- <div class="form-group" id="btnBack">        
     <div class="col-sm-offset-4 col-sm-8">
     	<button type="button" class="Submit btn btn-default" onClick="window.location.href='employeeSummary.jsp'"><b>Back</b></button>&nbsp;&nbsp;
      </div>
    </div> -->
    <div class="form-group" id="btnSave">        
     <div class="col-sm-offset-4 col-sm-8">
     	<!--<button type="button" class="Submit btn btn-default" onClick="window.location.href='employeeSummary.jsp'"><b>Back</b></button>&nbsp;&nbsp;
        <button type="button" class="Submit btn btn-default" onClick="{backNavigation('settings.jsp','SA');}"><b>Back</b></button>&nbsp;&nbsp; -->
      	<button type="button" class="Submit btn btn-default" onClick="onNew();" <%=sNewEnb%>>Clear</button>&nbsp;&nbsp;
      	<button type="button" class="btn btn-success" data-toggle="modal" data-target="#myModal" onClick="onUpdate();" <%=sUpdateEnb%>>Save</button>&nbsp;&nbsp;
      	<button type="button" class="Submit btn btn-default" onClick="return onDelete();" <%=sDeleteEnb%>>Delete</button>&nbsp;&nbsp;
      </div>
    </div>
    
</form>
  
</div>
</div>
</div>
<%@include file="Employeetypepopup.jsp"%>
<%@include file="Leavetypepopup.jsp"%>
<%@include file="Salarytypepopup.jsp"%>
<%@include file="Departmentpopup.jsp"%>
<%@include file="newBankModal.jsp"%>
<script>
$(document).ready(function(){
    $('[data-toggle="tooltip"]').tooltip();
    $("#DOP").datepicker({dateFormat: 'dd/mm/yy',changeMonth: true, changeYear: true, yearRange: '-100:+0'});
    $('#PASSPORTEXPIRYDATE').datepicker({dateFormat: 'dd/mm/yy',changeMonth: true, changeYear: true, yearRange: '-10:+20'});
	$('#EMIRATESIDEXPIRY').datepicker({dateFormat: 'dd/mm/yy',changeMonth: true, changeYear: true, yearRange: '-10:+20'});
	$('#VISAEXPIRYDATE').datepicker({dateFormat: 'dd/mm/yy',changeMonth: true, changeYear: true, yearRange: '-10:+20'});
	$('#DATEOFJOINING').datepicker({dateFormat: 'dd/mm/yy',changeMonth: true, changeYear: true, yearRange: '-10:+10'});
	$('#DATEOFLEAVING').datepicker({dateFormat: 'dd/mm/yy',changeMonth: true, changeYear: true, yearRange: '-10:+20'});
	$('#CONTRACTSTARTDATE').datepicker({dateFormat: 'dd/mm/yy',changeMonth: true, changeYear: true, yearRange: '-10:+10'});
	$('#CONTRACTENDDATE').datepicker({dateFormat: 'dd/mm/yy',changeMonth: true, changeYear: true, yearRange: '-10:+20'});
    if(document.form.SAVE_RED.value!="")
    	{
    	document.form.action  = "../payroll/employee?PGaction=View&result=Employee Updated Successfully";
    	document.form.submit();
    	}
    if(document.form.SAVE_REDELETE.value!=""){
    	document.form.action  = "../payroll/employee?PGaction=View&result=Employee Deleted Successfully";
    	 document.form.submit();
	}
    
    var start = new Date().getFullYear();
    var end = parseFloat(start)+parseFloat("100");
    var options = "";
    for(var year = start ; year <=end; year++){
    	  $('.leaveyear')
          .append($("<option></option>")
                     .attr("value", year)
                     .text(year)); 
    }
    
    if(document.form.EDIT_COUNTRY.value!="")
	{
	$("select[name ='COUNTRY_CODE']").val(document.form.EDIT_COUNTRY.value);
	OnCountry(document.form.EDIT_COUNTRY.value);		
	if(document.form.EDIT_STATE.value!="")
		{
		$("select[name ='STATE']").val(document.form.EDIT_STATE.value);
		   document.getElementById("STATE").value = document.form.EDIT_STATE.value;

		}
	}
    if(document.form.EDIT_COUNTRYOFISSUE.value!="")
		$("select[name ='COUNTRYOFISSUE']").val(document.form.EDIT_COUNTRYOFISSUE.value);
	if(document.form.EDIT_BANKNAME.value!="")
		$("select[name ='BANKNAME']").val(document.form.EDIT_BANKNAME.value);
	if(document.form.ISEDIT.value!="")
	{
		if(document.form.ISEDIT.value=="VIEWDATA")
			{
			$(':input').attr('readonly','readonly');
			$(':radio').attr('disabled','disabled');
			$('select').attr('disabled','disabled');
			$('#DOP').datepicker("disable");
			$('#PASSPORTEXPIRYDATE').datepicker("disable");
			$('#EMIRATESIDEXPIRY').datepicker("disable");
			$('#VISAEXPIRYDATE').datepicker("disable");
			$('#DATEOFJOINING').datepicker("disable");
			$('#DATEOFLEAVING').datepicker("disable");
			$('#CONTRACTSTARTDATE').datepicker("disable");
			$('#CONTRACTENDDATE').datepicker("disable");
			document.getElementById("btnSave").style.display = "none";
			document.getElementById("btnUpload").style.display = "none";
			document.getElementById("btnView").style.display = "none";
			document.getElementById("btnpop").style.display = "none";
			}
		//else
			//document.getElementById("btnBack").style.display = "none";
	}
	//else
		//document.getElementById("btnBack").style.display = "none";
	
	
	  $('.emptype').typeahead({
		  hint: true,
		  minLength:0,  
		  searchOnFocus: true
		},
		{		  
		  display: 'EMPLOYEETYPE',  
		  source: function (query, process,asyncProcess) {
			var urlStr = "/track/EmployeeTypeServlet";
			$.ajax( {
				type : "GET",
				url : urlStr,
				async : true,
				data : {
					CMD : "GET_EMPLOYEE_TYPE_DROPDOWN",
					QUERY : query
				},
				dataType : "json",
				success : function(data) {
					return asyncProcess(data.EMPTYPELIST);
				}
			});
		  },
		  limit: 9999,
		  templates: {
		  empty: [
		      '<div style="padding:3px 20px">',
		        'No results found',
		      '</div>',
		    ].join('\n'),
		    suggestion: function(data) {
		    		return '<div onclick="setemployetprid(this,\''+data.ID+'\')"><p>' + data.EMPLOYEETYPE + '</p>';
			}
		  }
		}).on('typeahead:render',function(event,selection){
			var menuElement = $(this).parent().find(".tt-menu");
			var top = menuElement.height()+35;
			top+="px";	
			if(menuElement.next().hasClass("footer")){
				menuElement.next().remove();  
			}
			/*menuElement.after( '<div class="shipmentAddBtn footer"  data-toggle="modal" data-target="#shipmentModal"><a href="#"> + Add Shipment</a></div>');*/
			menuElement.after( '<div class="accountAddBtn footer emptypepopup"  data-toggle="modal" data-target="#create_employee_type"><a href="#"> + New Employee Type</a></div>');
			menuElement.next().width(menuElement.width());
			menuElement.next().css({ "top": top,"padding":"3px 20px" });
			if($(this).parent().find(".tt-menu").css('display') != "block")
				menuElement.next().hide();
		  
		}).on('typeahead:open',function(event,selection){
			var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
			element.toggleClass("glyphicon-menu-up",true);
			element.toggleClass("glyphicon-menu-down",false);
			var menuElement = $(this).parent().find(".tt-menu");
			menuElement.next().show();
		}).on('typeahead:close',function(){
			var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
			element.toggleClass("glyphicon-menu-up",false);
			element.toggleClass("glyphicon-menu-down",true);
			var menuElement = $(this).parent().find(".tt-menu");
			setTimeout(function(){ menuElement.next().hide();}, 180);
		});
    
	    $(document).on("focusout",".WORK_MAN_HOURS", function(){
			var value = $(this).val();
			var decimal=  /^[-+]?[0-9]+\.[0,5]+$/; 
			var numbers = /^[0-9]+$/;
			if(value.match(decimal) || value.match(numbers)) 
			{ 
				var ldays=parseFloat(value).toFixed(1);
				$(this).val(ldays);
			}else{
				alert("Please enter valid working man hours");
				var ldays=parseFloat("0").toFixed(1);
				$(this).val(ldays);
			}
		});
	    
	    $('.leavetype').typeahead({
			  hint: true,
			  minLength:0,  
			  searchOnFocus: true
			},
			{		  
			  display: 'LEAVETYPE',  
			  source: function (query, process,asyncProcess) {
				var urlStr = "/track/HrLeaveTypeServlet";
				$.ajax( {
					type : "GET",
					url : urlStr,
					async : true,
					data : {
						CMD : "GET_LEAVE_TYPE_DROPDOWN",
						EMPTYPEID : document.form.employeetypeid.value,
						QUERY : query
					},
					dataType : "json",
					success : function(data) {
						return asyncProcess(data.LEAVETYPELIST);
					}
				});
			  },
			  limit: 9999,
			  templates: {
			  empty: [
			      '<div style="padding:3px 20px">',
			        'No results found',
			      '</div>',
			    ].join('\n'),
			    suggestion: function(data) {
			    		return '<div onclick="setleavetypeiddays(this,\''+data.ID+'\',\''+data.TOTALENTITLEMENT+'\')"><p>' + data.LEAVETYPE + '</p>';
				}
			  }
			}).on('typeahead:render',function(event,selection){
				var menuElement = $(this).parent().find(".tt-menu");
				var top = menuElement.height()+35;
				top+="px";	
				if(menuElement.next().hasClass("footer")){
					menuElement.next().remove();  
				}
				/*menuElement.after( '<div class="shipmentAddBtn footer"  data-toggle="modal" data-target="#shipmentModal"><a href="#"> + Add Shipment</a></div>');*/
				menuElement.after( '<div class="accountAddBtn footer lvtypepopup"  data-toggle="modal" data-target="#create_leave_type"><a href="#"> + New Leave Type</a></div>');
				menuElement.next().width(menuElement.width());
				menuElement.next().css({ "top": top,"padding":"3px 20px" });
				if($(this).parent().find(".tt-menu").css('display') != "block")
					menuElement.next().hide();
			  
			}).on('typeahead:open',function(event,selection){
				var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
				element.toggleClass("glyphicon-menu-up",true);
				element.toggleClass("glyphicon-menu-down",false);
				var menuElement = $(this).parent().find(".tt-menu");
				menuElement.next().show();
			}).on('typeahead:close',function(){
				var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
				element.toggleClass("glyphicon-menu-up",false);
				element.toggleClass("glyphicon-menu-down",true);
				var menuElement = $(this).parent().find(".tt-menu");
				setTimeout(function(){ menuElement.next().hide();}, 180);
			});
	    
	    $("#empAttch").change(function(){
	    	var files = $(this)[0].files.length;
	    	var sizeFlag = false;
	    		if(files > 5){
	    			$(this)[0].value="";
	    			alert("You can upload only a maximum of 5 files");
	    			$("#billAttchNote").html("<small class='text-muted'>  You can upload a maximum of 5 files, 2MB each  </small>");
	    		}else{
	    			for (var i = 0; i < $(this)[0].files.length; i++) {
	    			    var imageSize = $(this)[0].files[i].size;
	    			    if(imageSize > 2097152 ){
	    			    	sizeFlag = true;
	    			    }
	    			}	
	    			if(sizeFlag){
	    				$(this)[0].value="";
	    				alert("Maximum file size allowed is 2MB, please try with different file.");
	    				$("#empAttchNote").html("<small class='text-muted'>  You can upload a maximum of 5 files, 2MB each  </small>");
	    			}else{
	    				$("#empAttchNote").html(files +" files attached");
	    				/* $("#empAttchNote").append('<br><br><button onclick="add_attachments()">Upload Employee Attachments</button>'); */
	    			}
	    			
	    		}
	    	});
	    
	    /* Employee Auto Suggestion */
		$('#EMP_REPORTING').typeahead({
			  hint: true,
			  minLength:0,  
			  searchOnFocus: true
			},
			{
			  //name: 'states',
			  display: 'FNAME',  
			  async: true,   
			  //source: substringMatcher(states),
			  source: function (query, process,asyncProcess) {
				  var urlStr = "/track/MasterServlet";
						$.ajax( {
						type : "POST",
						url : urlStr,
						async : true,
						data : {
							PLANT : "<%=plant%>",
							ACTION : "GET_EMPLOYEE_DATA_EMPID",
							empid : "<%=empid%>",
							QUERY : query
						},
						dataType : "json",
						success : function(data) {
							return asyncProcess(data.EMPMST);
						}
			   });
			  },
			  limit: 9999,
			  templates: {
			  empty: [
			      '<div style="padding:3px 20px">',
			        'No results found',
			      '</div>',
			    ].join('\n'),
			    suggestion: function(data) {
			    	return '<p onclick="document.form.repid.value = \''+data.ID+'\'">' + data.FNAME + '</p>';		    
				}
			  }
			}).on('typeahead:render',function(event,selection){
				var menuElement = $(this).parent().find(".tt-menu");
				var top = menuElement.height()+35;
				top+="px";	
				if(menuElement.next().hasClass("footer")){
					menuElement.next().remove();  
				}
				/* menuElement.after( '<div class="employeeAddBtn footer"  data-toggle="modal" data-target="#employeeModal"><a href="#"> + New Employee</a></div>'); */
				menuElement.next().width(menuElement.width());
				menuElement.next().css({ "top": top,"padding":"3px 20px" });
				if($(this).parent().find(".tt-menu").css('display') != "block")
					menuElement.next().hide();
			  
			}).on('typeahead:open',function(event,selection){
				var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
				element.toggleClass("glyphicon-menu-up",true);
				element.toggleClass("glyphicon-menu-down",false);
				var menuElement = $(this).parent().find(".tt-menu");
				menuElement.next().show();
			}).on('typeahead:close',function(){
				var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
				element.toggleClass("glyphicon-menu-up",false);
				element.toggleClass("glyphicon-menu-down",true);
				var menuElement = $(this).parent().find(".tt-menu");
				setTimeout(function(){ menuElement.next().hide();}, 150);
			});
	    
		 $(document).on("focusout",".loginuserid", function(){
		    	var userid = $(this).val();
		    	if(userid != "") {
		    	$.ajax({
		    		type : "GET",
		    		url: '/track/HrLeaveTypeServlet',
		    		async : true,
		    		dataType: 'json',
		    		data : {
		    			CMD : "CHECK_USERID_EDIT",
		    			username : userid,
		    			uid : "<%=empid%>"
		    		},
		    		success : function(data) {
		    			if(data.STATUS == "NOT OK"){
		    				var logid="<%=sUserloginid%>";
		    				$('input[name = "EMP_LOGIN_ID"]').val(logid);
		    				alert("Employee Login ID already exist");
		    			}
		    		}
		    	});	
		    	}
		    });
		 
		 $('.empSalary').typeahead({
			  hint: true,
			  minLength:0,  
			  searchOnFocus: true
			},
			{		  
			  display: 'SALARYTYPE',  
			  source: function (query, process,asyncProcess) {
				var urlStr = "/track/HrSalaryServlet";
				$.ajax( {
					type : "GET",
					url : urlStr,
					async : true,
					data : {
						CMD : "GET_SALARY_TYPE_DROPDOWN",
						QUERY : query
					},
					dataType : "json",
					success : function(data) {
						//alert(JSON.stringify(data));
						return asyncProcess(data.SALARYTYPELIST);
					}
				});
			  },
			  limit: 9999,
			  templates: {
			  empty: [
			      '<div style="padding:3px 20px">',
			        'No results found',
			      '</div>',
			    ].join('\n'),
			    suggestion: function(data) {
			    		return '<div onclick="setsalarytypeid(this,\''+data.ID+'\',\''+data.ISPAYROLL_BY_BASIC_SALARY+'\')"><p>' + data.SALARYTYPE + '</p>';
				}
			  }
			}).on('typeahead:render',function(event,selection){
				var menuElement = $(this).parent().find(".tt-menu");
				var top = menuElement.height()+35;
				top+="px";	
				if(menuElement.next().hasClass("footer")){
					menuElement.next().remove();  
				}
				menuElement.after( '<div class="accountAddBtn footer salarytypepopup"  data-toggle="modal" data-target="#create_salary_type"><a href="#"> + New Salary Type</a></div>');
				menuElement.next().width(menuElement.width());
				menuElement.next().css({ "top": top,"padding":"3px 20px" });
				if($(this).parent().find(".tt-menu").css('display') != "block")
					menuElement.next().hide();
			  
			}).on('typeahead:open',function(event,selection){
				var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
				element.toggleClass("glyphicon-menu-up",true);
				element.toggleClass("glyphicon-menu-down",false);
				var menuElement = $(this).parent().find(".tt-menu");
				menuElement.next().show();
			}).on('typeahead:close',function(){
				var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
				element.toggleClass("glyphicon-menu-up",false);
				element.toggleClass("glyphicon-menu-down",true);
				var menuElement = $(this).parent().find(".tt-menu");
				setTimeout(function(){ menuElement.next().hide();}, 180);
			});
});

function setsalarytypeid(obj,id,ISPAYROLL_BY_BASIC_SALARY){
	var count = "0";
	$("input[name ='empSalaryid']").each(function() {
		if($(this).val() == id){
			count = "1";
	    }
	});
	if(count == "0"){
		$(obj).closest('tr').find("input[name ='empSalaryid']").val(id);
	}else{
		alert("Salary type alredy selected");
		$(obj).closest('tr').remove();
	}
	 if(ISPAYROLL_BY_BASIC_SALARY == "1") {
		 $(obj).closest('tr').find("input[name ='ISPAYROLL_BY_BASIC_SALARY']").prop('checked', true);
	} 
	
}

function addSalaryRow(){

	var body="";
	body += '<tr>';
	body += '<td class="text-center">';
	body += '<input type="hidden" name="empSalaryid" value="0">';	
	body += '<input class="form-control text-left empSalary" name="empSalary" type="text" placeholder="Enter Salary Type" maxlength="50">';
	body += '</td>';
	body += '<td class="text-center grey-bg" style="position:relative;">';
//	body += '<span class="glyphicon glyphicon-remove-circle empSalary-action" aria-hidden="true"></span>'; 
	body += '<input type="hidden" name="PAYROLL_BY_BASIC_SALARY" value="0">';
	body += '<input type="Checkbox" style="border:0;background=#dddddd"	name="ISPAYROLL_BY_BASIC_SALARY" value="1" onclick="payrollbybasicsalary(this)" disabled> ';
	body += '</td>';
	body += '<td class="text-center grey-bg" style="position:relative;">';
	body += '<span class="glyphicon glyphicon-remove-circle empSalary-action" aria-hidden="true"></span>';
	body += '<input class="form-control text-left" type="text" name="empSalaryAmt" placeholder="Enter Amount" value="<%=new java.math.BigDecimal(sSalary).toPlainString()%>" onkeypress="return isNumberKey(event,this,4)">';
	body += '</td>';
	body += '</tr>';
	$(".empSalary-table tbody").append(body);
	removesalaryrowclasses();
	addsalaryrowclasses();
}

$(".empSalary-table tbody").on('click','.empSalary-action',function(){
    $(this).parent().parent().remove();
});

function removesalaryrowclasses(){
	$(".empSalary").typeahead('destroy');
}

function addsalaryrowclasses(){

$('.empSalary').typeahead({
	  hint: true,
	  minLength:0,  
	  searchOnFocus: true
	},
	{		  
	  display: 'SALARYTYPE',  
	  source: function (query, process,asyncProcess) {
		var urlStr = "/track/HrSalaryServlet";
		$.ajax( {
			type : "GET",
			url : urlStr,
			async : true,
			data : {
				CMD : "GET_SALARY_TYPE_DROPDOWN",
				QUERY : query
			},
			dataType : "json",
			success : function(data) {
				//alert(JSON.stringify(data));
				return asyncProcess(data.SALARYTYPELIST);
			}
		});
	  },
	  limit: 9999,
	  templates: {
	  empty: [
	      '<div style="padding:3px 20px">',
	        'No results found',
	      '</div>',
	    ].join('\n'),
	    suggestion: function(data) {
	    		return '<div onclick="setsalarytypeid(this,\''+data.ID+'\',\''+data.ISPAYROLL_BY_BASIC_SALARY+'\')"><p>' + data.SALARYTYPE + '</p>';
		}
	  }
	}).on('typeahead:render',function(event,selection){
		var menuElement = $(this).parent().find(".tt-menu");
		var top = menuElement.height()+35;
		top+="px";	
		if(menuElement.next().hasClass("footer")){
			menuElement.next().remove();  
		}
		menuElement.after( '<div class="accountAddBtn footer salarytypepopup"  data-toggle="modal" data-target="#create_salary_type"><a href="#"> + New Salary Type</a></div>');
		menuElement.next().width(menuElement.width());
		menuElement.next().css({ "top": top,"padding":"3px 20px" });
		if($(this).parent().find(".tt-menu").css('display') != "block")
			menuElement.next().hide();
	  
	}).on('typeahead:open',function(event,selection){
		var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
		element.toggleClass("glyphicon-menu-up",true);
		element.toggleClass("glyphicon-menu-down",false);
		var menuElement = $(this).parent().find(".tt-menu");
		menuElement.next().show();
	}).on('typeahead:close',function(){
		var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
		element.toggleClass("glyphicon-menu-up",false);
		element.toggleClass("glyphicon-menu-down",true);
		var menuElement = $(this).parent().find(".tt-menu");
		setTimeout(function(){ menuElement.next().hide();}, 180);
	});
}


function setemployetprid(obj, id){
	$('input[name = "employeetypeid"]').val(id);
	NewLleaveRow();
}

function addRowleave() {

	var body = "";
	body += '<tr>';
	body += '<td class="text-center">';
	body += '<input type="hidden" name="leavetypeid" value="0">';
	body += '<input class="form-control text-left leavetype" name="leavetype" type="text" placeholder="Enter Leave Type" maxlength="100"></td>';
	body += '<td class="text-center">';
	body += '<input class="form-control text-left totalentitlement" type="text" name="totalentitlement" placeholder="Enter Totalentitlement" value="0.0">';
	body += '</td>';
	body += '<td class="text-center">';
	body += '<select class="form-control text-left leaveyear" name="leaveyear"></select>';
	body += '</td>';
	body += '<td class="text-center grey-bg" style="position:relative;">';
	body += '<span class="glyphicon glyphicon-remove-circle leavetype-action" aria-hidden="true"></span>';
	body += '<textarea  name="notes" class="form-control text-left" maxlength="1000" placeholder="Max 1000 characters"></textarea>';
	body += '</td>';
	body += '</tr>';
	$(".leavetype-table tbody").append(body);
	removerowclasses();
	addrowclasses();
}

function NewLleaveRow() {
	$(".leavetype-table tbody").html("");
	var body = "";
	body += '<tr>';
	body += '<td class="text-center">';
	body += '<input type="hidden" name="leavetypeid" value="0">';
	body += '<input class="form-control text-left leavetype" name="leavetype" type="text" placeholder="Enter Leave Type" maxlength="100"></td>';
	body += '<td class="text-center">';
	body += '<input class="form-control text-left totalentitlement" type="text" name="totalentitlement" placeholder="Enter Totalentitlement" value="0.0">';
	body += '</td>';
	body += '<td class="text-center">';
	body += '<select class="form-control text-left leaveyear" name="leaveyear"></select>';
	body += '</td>';
	body += '<td class="text-center">';
	body += '<textarea  name="notes" class="form-control text-left" maxlength="1000" placeholder="Max 1000 characters"></textarea>';
	body += '</td>';
	body += '</tr>';
	$(".leavetype-table tbody").append(body);
	removerowclasses();
	addrowclasses();
}

$(".leavetype-table tbody").on('click', '.leavetype-action', function() {
	$(this).parent().parent().remove();
});

function removerowclasses(){
	$(".leavetype").typeahead('destroy');
}

function addrowclasses(){
	  $('.leavetype').typeahead({
		  hint: true,
		  minLength:0,  
		  searchOnFocus: true
		},
		{		  
		  display: 'LEAVETYPE',  
		  source: function (query, process,asyncProcess) {
			var urlStr = "/track/HrLeaveTypeServlet";
			$.ajax( {
				type : "GET",
				url : urlStr,
				async : true,
				data : {
					CMD : "GET_LEAVE_TYPE_DROPDOWN",
					EMPTYPEID : document.form.employeetypeid.value,
					QUERY : query
				},
				dataType : "json",
				success : function(data) {
					return asyncProcess(data.LEAVETYPELIST);
				}
			});
		  },
		  limit: 9999,
		  templates: {
		  empty: [
		      '<div style="padding:3px 20px">',
		        'No results found',
		      '</div>',
		    ].join('\n'),
		    suggestion: function(data) {
		    		return '<div onclick="setleavetypeiddays(this,\''+data.ID+'\',\''+data.TOTALENTITLEMENT+'\')"><p>' + data.LEAVETYPE + '</p>';
			}
		  }
		}).on('typeahead:render',function(event,selection){
			var menuElement = $(this).parent().find(".tt-menu");
			var top = menuElement.height()+35;
			top+="px";	
			if(menuElement.next().hasClass("footer")){
				menuElement.next().remove();  
			}
			/*menuElement.after( '<div class="shipmentAddBtn footer"  data-toggle="modal" data-target="#shipmentModal"><a href="#"> + Add Shipment</a></div>');*/
			menuElement.after( '<div class="accountAddBtn footer lvtypepopup"  data-toggle="modal" data-target="#create_leave_type"><a href="#"> + New Leave Type</a></div>');
			menuElement.next().width(menuElement.width());
			menuElement.next().css({ "top": top,"padding":"3px 20px" });
			if($(this).parent().find(".tt-menu").css('display') != "block")
				menuElement.next().hide();
		  
		}).on('typeahead:open',function(event,selection){
			var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
			element.toggleClass("glyphicon-menu-up",true);
			element.toggleClass("glyphicon-menu-down",false);
			var menuElement = $(this).parent().find(".tt-menu");
			menuElement.next().show();
		}).on('typeahead:close',function(){
			var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
			element.toggleClass("glyphicon-menu-up",false);
			element.toggleClass("glyphicon-menu-down",true);
			var menuElement = $(this).parent().find(".tt-menu");
			setTimeout(function(){ menuElement.next().hide();}, 180);
		});
	  
	    var start = new Date().getFullYear();
	    var end = parseFloat(start)+parseFloat("100");
	    var options = "";
	    for(var year = start ; year <=end; year++){
	    	  $('.leaveyear')
	          .append($("<option></option>")
	                     .attr("value", year)
	                     .text(year)); 
	    }
}

function setleavetypeiddays(obj,id,tdays){
	var count = "0";
	$("input[name ='leavetypeid']").each(function() {
		if($(this).val() == id){
			count = "1";
	    }
	});
	if(count == "0"){
		$(obj).closest('tr').find("input[name ='leavetypeid']").val(id);
		$(obj).closest('tr').find("input[name ='totalentitlement']").val(tdays);
	}else{
		alert("Leave type alredy selected");
		$(obj).closest('tr').remove();
	}
}

</script>






<script>
function getEmployeeDetails() {
	debugger;
	var empid = document.form.CUST_CODE.value;
	
	if(document.form.CUST_CODE.value=="" || document.form.CUST_CODE.value.length==0 ) {
		alert("Enter Employee ID!");
		document.form.CUST_CODE.focus();
	}else{
		var urlStr = "/track/ItemMstServlet";
		$.ajax( {
			type : "POST",
			url : urlStr,
                        async:false ,
			data : {
				EMPNO : empid,
				PLANT : "<%=plant%>",
				ACTION : "GET_EMPLOYEE_DETAILS"
				},
				dataType : "json",
				success : function(data) {
					
					if (data.status == "100") {
                                        var resultVal = data.result;
                                                   document.form.CUST_NAME.value = resultVal.FNAME;
                                                   //document.form.L_CUST_NAME.value=resultVal.LNAME;
                                                   document.form.GENDER.value=resultVal.GENDER;
                                                   document.form.DOB.value=resultVal.DOB;
                                                   document.form.DEPT.value=resultVal.DEPT;
                                                   document.form.DESGINATION.value=resultVal.DESGINATION;
                                                   document.form.DATEOFJOINING.value=resultVal.DATEOFJOINING;
                                                   document.form.DATEOFLEAVING.value=resultVal.DATEOFLEAVING;
                                                   //document.form.NATIONALITY.value=resultVal.NATIONALITY;                                                   
                                                   document.form.TELNO.value=resultVal.TELNO;
                                                   //document.form.HPNO.value=resultVal.HPNO;
                                                   //document.form.FAX.value=resultVal.FAX;
                                                   document.form.EMAIL.value=resultVal.EMAIL;
                                                   document.form.SKYPE.value=resultVal.SKYPE;
                                                   document.form.FACEBOOK.value=resultVal.FACEBOOK;
                                                   document.form.TWITTER.value=resultVal.TWITTER;
                                                   document.form.LINKEDIN.value=resultVal.LINKEDIN;
                                                   document.form.PASSPORTNUMBER.value=resultVal.PASSPORTNUMBER;
                                                   document.form.COUNTRYOFISSUE.value=resultVal.COUNTRYOFISSUE;
                                                   document.form.PASSPORTEXPIRYDATE.value=resultVal.PASSPORTEXPIRYDATE;
                                                   document.form.ADDR1.value=resultVal.ADDR1;
                                                   document.form.ADDR2.value=resultVal.ADDR2;                                                   
                                                   document.form.ADDR3.value=resultVal.ADDR3;
                                                   document.form.ADDR4.value=resultVal.ADDR4;
                                                   document.form.COUNTRY.value=resultVal.COUNTRY;
                                                   document.form.ZIP.value=resultVal.ZIP;
                                                   document.form.EMIRATESID.value=resultVal.EMIRATESID;
                                                   document.form.EMIRATESIDEXPIRY.value=resultVal.EMIRATESIDEXPIRY;
                                                   document.form.VISANUMBER.value=resultVal.VISANUMBER;
                                                   document.form.VISAEXPIRYDATE.value=resultVal.VISAEXPIRYDATE;
                                                   document.form.LABOURCARDNUMBER.value=resultVal.LABOURCARDNUMBER;
                                                   document.form.WORKPERMITNUMBER.value=resultVal.WORKPERMITNUMBER;
                                                   document.form.CONTRACTSTARTDATE.value=resultVal.CONTRACTSTARTDATE;
                                                   document.form.CONTRACTENDDATE.value=resultVal.CONTRACTENDDATE;
                                                   document.form.BANKNAME.value=resultVal.BANKNAME;
                                                   document.form.BRANCH.value=resultVal.BRANCH;                                                   
                                                   document.form.IBAN.value=resultVal.IBAN;
                                                   document.form.BANKROUTINGCODE.value=resultVal.BANKROUTINGCODE;
                                                   document.form.BASICSALARY.value=resultVal.BASICSALARY;
                                                   document.form.HOUSERENTALLOWANCE.value=resultVal.HOUSERENTALLOWANCE;
                                                   document.form.TRANSPORTALLOWANCE.value=resultVal.TRANSPORTALLOWANCE;                                                   
                                                   document.form.COMMUNICATIONALLOWANCE.value=resultVal.COMMUNICATIONALLOWANCE;
                                                   document.form.OTHERALLOWANCE.value=resultVal.OTHERALLOWANCE;
                                                   document.form.BONUS.value=resultVal.BONUS;
                                                   document.form.COMMISSION.value=resultVal.COMMISSION;
                                                   document.form.GRATUITY.value=resultVal.GRATUITY;
                                                   document.form.AIRTICKET.value=resultVal.AIRTICKET;
                                                   document.form.LEAVESALARY.value=resultVal.LEAVESALARY;
                                                   document.form.REMARKS.value=resultVal.REMARKS;
                                                   setCheckedValue( document.form.ACTIVE,resultVal.IsActive);
                                                   loadAlternateEmployee(empid);
                                                   document.form.EDIT_STATE.value=resultVal.STATE;
												if(resultVal.COUNTRY_CODE!="")
                                                   $("select[name ='COUNTRY_CODE']").val(resultVal.COUNTRY_CODE);
                                                   
                                           		OnCountry(resultVal.COUNTRY_CODE);		
                                           		if(document.form.EDIT_STATE.value!="")
                                    			{
                                    				$("select[name ='STATE']").val(document.form.EDIT_STATE.value);
                                    			    document.getElementById("STATE").value = document.form.EDIT_STATE.value;

                                    			}
                                                   
                                                   if(resultVal.IMAGEPATH=="")
                                                  	 $("#emp_img").attr("src","../jsp/dist/img/NO_IMG.png");
                                                   else	 
                                             			 $("#emp_img").attr("src","/track/ReadFileServlet/?fileLocation="+resultVal.IMAGEPATH);
                                                  
                                           		
                         
					}
				}
			});
		}
	}
function setCheckedValue(radioObj, newValue) {
	if (!radioObj)
		return;
	var radioLength = radioObj.length;
	if (radioLength == undefined) {
		radioObj.checked = (radioObj.value == newValue.toString());
		return;
	}
	for ( var i = 0; i < radioLength; i++) {
		radioObj[i].checked = false;
		if (radioObj[i].value == newValue.toString()) {
			radioObj[i].checked = true;
		}
	}
}
function loadAlternateEmployee(empid) {
	var urlStr = "/track/ItemMstServlet";
	$.ajax( {
		type : "POST",
		url : urlStr,
		data : {
			EMPID : empid,
			PLANT : "<%=plant%>",
			ACTION : "LOAD_ALTERNATE_EMPLOYEE_DETAILS"
			},
			dataType : "json",
			success : formatData
		});
	}

	function formatData(data) {
		debugger;
		var ii = 0;
		var errorBoo = false;
		$.each(data.status, function(i, statusN) {
			if (statusN == "99") {
				errorBoo = true;
			}
		});
		
		if (!errorBoo) {
			var table = document.getElementById('alternateEmployeeDynamicTable');
			var rowCount = table.rows.length;
			rowCount = rowCount * 1 - 1;
			for(; rowCount>=0; rowCount--) {
 				table.deleteRow(rowCount);
			}
			var resultVal = data.result;
			$.each(resultVal, function(i, resultIntVal) {
				addRow('alternateEmployeeDynamicTable', resultIntVal);
			});
		} else {
			alert("No Data found....!!!!");
		}

	}
function addRow(tableID, textValue) {
	
	
	var table = document.getElementById(tableID);
	var rowCount = table.rows.length;
	var row = table.insertRow(rowCount);

	var secondCell = row.insertCell(0);
	var inputTextArea = document.createElement("input");
	var newRowCount = rowCount + 1;
	
	inputTextArea.name = "DYNAMIC_ALTERNATE_NAME_" + newRowCount;
	inputTextArea.className = "form-control";
	
	inputTextArea.size = "50";
	inputTextArea.type = "text";
	
	inputTextArea.value = textValue;
	// secondCell.appendChild(document.createElement("br"));
	secondCell.appendChild(inputTextArea);

	
}

function deleteRow(tableID) {
	try {
		var table = document.getElementById(tableID);
		var rowCount = table.rows.length;
		rowCount = rowCount * 1 - 1;
		if (rowCount == 0) {
			alert("Can not remove the default Item");
		} else {
			table.deleteRow(rowCount);
		}
	} catch (e) {
		alert(e);
	}
}
function readURL(input) {
    if (input.files && input.files[0]) {
        var reader = new FileReader();

        reader.onload = function (e) {
        	
        	
            $('#emp_img').attr('src', e.target.result);
        }

        reader.readAsDataURL(input.files[0]);
    }
}
$(document).on('change', '#userImage', function (e) {
    readURL(this);
});
function setBranch(obj,id){
	 $("input[name ='BRANCH']").val(id);
}
function OnBank(BankName)
{
	$.ajax({
		type : "POST",
		url: '/track/MasterServlet',
		async : true,
		dataType: 'json',
		data : {
			action : "GET_BANK_DATA",
			PLANT : "<%=plant%>",
			NAME : BankName,
		},
		success : function(dataitem) {
			var BankList=dataitem.BANKMST;
			var myJSON = JSON.stringify(BankList);						
			var dt = JSON.stringify(BankList).replace('[', '').replace(']', '');
			if (dt) {
			  var result = jQuery.parseJSON(dt);			  
			  var val = result.BRANCH;			  
			  $("input[name ='BRANCH']").val(val);
			}
		}
	});		
}

function OnCountry(Country)
{
	$.ajax({
		type : "POST",
		url: '/track/MasterServlet',
		async : true,
		dataType: 'json',
		data : {
			action : "GET_STATE_DATA",
			PLANT : "<%=plant%>",
			COUNTRY : Country,
		},
		success : function(dataitem) {
			var StateList=dataitem.STATEMST;
			var myJSON = JSON.stringify(StateList);
			$('#STATE').empty();
		//	$('#STATE').append('<OPTION style="display:none;">Select State</OPTION>');
			$('#STATE').append('<OPTION>Select State</OPTION>');
			 $.each(StateList, function (key, value) {
				 
					 if(document.form.EDIT_STATE.value==value.text)
					 	$('#STATE').append('<option selected value="' + value.text + '">' + value.text + '</option>');
					 else					 
				   		$('#STATE').append('<option value="' + value.text + '">' + value.text + '</option>');
				});
		}
	});	
	
}
$('select[name="COUNTRY_CODE"]').on('change', function(){
    var text = $("#COUNTRY_CODE option:selected").text();
    $("input[name ='COUNTRY']").val(text.trim());
    document.form.EDIT_STATE.value="";
});
$('select[name="STATE"]').on('change', function(){
$("#STATE option").removeAttr('selected');
document.form.EDIT_STATE.value="";
});

function downloadFile(id,fileName)
{
	 var urlStrAttach = "/track/EmpMstAttachmentServlet?Submit=downloadAttachmentById&attachid="+id;
	 var xhr=new XMLHttpRequest();
	 xhr.open("POST", urlStrAttach, true);
	 //Now set response type
	 xhr.responseType = 'arraybuffer';
	 xhr.addEventListener('load',function(){
	   if (xhr.status === 200){
	     console.log(xhr.response) // ArrayBuffer
	     console.log(new Blob([xhr.response])) // Blob
	     var datablob=new Blob([xhr.response]);
	     var a = document.createElement('a');
         var url = window.URL.createObjectURL(datablob);
         a.href = url;
         a.download = fileName;
         document.body.append(a);
         a.click();
         a.remove();
         //window.URL.revokeObjectURL(url); 
	   }
	 })
	 xhr.send();
}

function removeFile(id)
{
	var urlStrAttach = "/track/EmpMstAttachmentServlet?Submit=removeAttachmentById&removeid="+id;	
	$.ajax( {
		type : "POST",
		url : urlStrAttach,
		success : function(data) {
					window.location.reload();
				}
			});
}

function add_attachments(){
    var formData = new FormData($('#empform')[0]);
    var userId= form.CUST_CODE.value;
	if(userId){
    $.ajax({
        type: 'post',
        url: "/track/EmpMstAttachmentServlet?Submit=add_attachments",
	    data:  formData,//{key:val}
	    contentType: false,
	    processData: false,
        success: function (data) {
        	document.form.action  = "../payroll/editemployee?action=UPDATE";
       		document.form.submit();
        },
        error: function (data) {
        	document.form.action  = "../payroll/editemployee?action=UPDATE";
       		document.form.submit();
        }
    });
	}else{
		alert("Please enter Employee Id");
	}
        return false; 
  }

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
        var plant= '<%=plant%>';
    	/* Department Auto Suggestion */
    	$('#DEPT').typeahead({
    		  hint: true,
    		  minLength:0,  
    		  searchOnFocus: true
    		},
    		{
    		  display: 'DEPARTMENT',  
    		  source: function (query, process,asyncProcess) {
    			var urlStr = "/track/MasterServlet";
    			$.ajax( {
    			type : "POST",
    			url : urlStr,
    			async : true,
    			data : {
    				PLANT : plant,
    				ACTION : "GET_DEPARTMENT_DATA",
    				QUERY : query
    			},
    			dataType : "json",
    			success : function(data) {
    				return asyncProcess(data.DEPARTMENT_MST);
    			}
    				});
    		},
    		  limit: 9999,
    		  templates: {
    		  empty: [
    			  '<div style="padding:3px 20px">',
    				'No results found',
    			  '</div>',
    			].join('\n'),
    			suggestion: function(data) {
    			//return '<div><p class="item-suggestion">'+data.DEPARTMENT+'</p></div>';
    			return '<div onclick="setdepartmentid(this,\''+data.ID+'\')"><p>' + data.DEPARTMENT + '</p>';
    			}
    		  }
    		}).on('typeahead:render',function(event,selection){
    			var menuElement = $(this).parent().find(".tt-menu");
    			var top = menuElement.height()+35;
    			top+="px";	
    			if(menuElement.next().hasClass("footer")){
    				menuElement.next().remove();  
    			}
    			menuElement.after( '<div class="accountAddBtn footer departmentpopup"  data-toggle="modal" data-target="#create_department"><a href="#"> + New Department</a></div>');
    			menuElement.next().width(menuElement.width());
    			menuElement.next().css({ "top": top,"padding":"3px 20px" });
    			if($(this).parent().find(".tt-menu").css('display') != "block")
    				menuElement.next().hide();
    		  
    		}).on('typeahead:open',function(event,selection){
    			var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
    			element.toggleClass("glyphicon-menu-up",true);
    			element.toggleClass("glyphicon-menu-down",false);
    			var menuElement = $(this).parent().find(".tt-menu");
    			menuElement.next().show();
    		}).on('typeahead:close',function(){
    			var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
    			element.toggleClass("glyphicon-menu-up",false);
    			element.toggleClass("glyphicon-menu-down",true);
    			var menuElement = $(this).parent().find(".tt-menu");
    			setTimeout(function(){ menuElement.next().hide();}, 180);
    		});

    	 $('#OUTLET_NAME').typeahead({
    	  	  hint: true,
    	  	  minLength:0,  
    	  	  searchOnFocus: true
    	  	},
    	  	{
    	  	  display: 'OUTLET_NAME',  
    	  	  async: true,   
    	  	  source: function (query, process,asyncProcess) {
    	  		var urlStr = "/track/MasterServlet";
    	  		$.ajax( {
    	  		type : "POST",
    	  		url : urlStr,
    	  		async : true,
    	  		data : {
    	  			PLANT : "<%=plant%>",
    	  			ACTION : "GET_OUTLET_DATA",
    	  			QUERY : query
    	  		},
    	  		dataType : "json",
    	  		success : function(data) {
    	  			return asyncProcess(data.POSOUTLETS);
    	  		}
    	  		});
    	  	  },
    	  	  limit: 9999,
    	  	  templates: {
    	  	  empty: [
    	  	      '<div style="padding:3px 20px">',
    	  	        'No results found',
    	  	      '</div>',
    	  	    ].join('\n'),
    	  	    suggestion: function(data) {
//    	   		return '<div onclick="document.form.OUTLET_NAME.value = \''+data.OUTLET_NAME+'\'"><p class="item-suggestion">Name: ' + data.OUTLET_NAME + '</p><br/><p class="item-suggestion">Code: ' + data.OUTLET + '</p></div>';
    	  	    	return '<div onclick="setOutletData(\''+data.OUTLET+'\',\''+data.OUTLET_NAME+'\')"><p class="item-suggestion">Name: ' + data.OUTLET_NAME + '</p><br/><p class="item-suggestion">Code: ' + data.OUTLET + '</p></div>';
    	  		}
    	  	  }
    	  	}).on('typeahead:open',function(event,selection){
    	  		var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
    	  		element.toggleClass("glyphicon-menu-up",true);
    	  		element.toggleClass("glyphicon-menu-down",false);
    	  	}).on('typeahead:close',function(){
    	  		var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
    	  		element.toggleClass("glyphicon-menu-up",false);
    	  		element.toggleClass("glyphicon-menu-down",true);
    	  	}).on('typeahead:change',function(event,selection){
    			if($(this).val() == ""){
    				document.form.OUTCODE.value = "";
    			}
    	  	});
 	  	
    	/* BANK Auto Suggestion */
    	$('#BANKNAME').typeahead({
    		  hint: true,
    		  minLength:0,  
    		  searchOnFocus: true
    		},
    		{
    		  display: 'NAME',  
    		  source: function (query, process,asyncProcess) {
    			var urlStr = "/track/MasterServlet";
    			$.ajax( {
    			type : "POST",
    			url : urlStr,
    			async : true,
    			data : {
    				PLANT : plant,
    				ACTION : "GET_BANKNAME_DATA",
    				QUERY : query
    			},
    			dataType : "json",
    			success : function(data) {
    				return asyncProcess(data.BANKMST);
    			}
    				});
    		},
    		  limit: 9999,
    		  templates: {
    		  empty: [
    			  '<div style="padding:3px 20px">',
    				'No results found',
    			  '</div>',
    			].join('\n'),
    			suggestion: function(data) {
    			
    			return '<div onclick="setBranch(this,\''+data.BRANCH_NAME+'\')"><p>' + data.NAME + '</p></div>';
    			}
    		  }
    		})		.on('typeahead:render',function(event,selection){
    			var menuElement = $(this).parent().find(".tt-menu");
    			var top = menuElement.height()+35;
    			top+="px";	
    			if(menuElement.next().hasClass("footer")){
    				menuElement.next().remove();  
    			}
    			menuElement.after( '<div class="accountAddBtn footer departmentpopup"  data-toggle="modal" data-target="#bankMstModal"><a href="#"> + New Bank</a></div>');
    			menuElement.next().width(menuElement.width());
    			menuElement.next().css({ "top": top,"padding":"3px 20px" });
    			if($(this).parent().find(".tt-menu").css('display') != "block")
    				menuElement.next().hide();
    		  
    		}).on('typeahead:open',function(event,selection){
    			var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
    			element.toggleClass("glyphicon-menu-up",true);
    			element.toggleClass("glyphicon-menu-down",false);
    			var menuElement = $(this).parent().find(".tt-menu");
    			menuElement.next().show();
    		}).on('typeahead:close',function(){
    			var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
    			element.toggleClass("glyphicon-menu-up",false);
    			element.toggleClass("glyphicon-menu-down",true);
    			var menuElement = $(this).parent().find(".tt-menu");
    			setTimeout(function(){ menuElement.next().hide();}, 180);
    		});
    });
    function setautoemail(obj) {
		if ($(obj).is(":checked")) {
			$("input[name ='isautoemail']").val("1");
		} else {
			$("input[name ='isautoemail']").val("0");
		}
	}

    function setautoemp(obj) {
        if ($(obj).is(":checked")) {
            
            $("input[name='ISCREATEONUSERINFO']").val("1");
            validateAndFocusCustCode();
           
        } else {
            $("input[name='ISCREATEONUSERINFO']").val("0");
            $("input[name='EMP_LOGIN_ID']").prop('readonly', false);  
           
        }
    }


    function validateAndFocusCustCode() {
        var custCode = $("input[name='CUST_CODE']").val();
        var password = $("input[name='PASSWORD']").val();
        if (!custCode) {
            alert("Please Enter Employee Id");
            $("input[name='ISCREATEONUSERINFO']").prop('checked', false).val("0");
            $("input[name='EMP_LOGIN_ID']").val('').prop('readonly', false);
        } else {
            if ($("input[name='ISCREATEONUSERINFO']").is(":checked")) {
                $("input[name='EMP_LOGIN_ID']").val(custCode).prop('readonly', true);
                if(!password){
                	alert("Please Enter Password");
                	$("input[name='PASSWORD']").focus();
                    }
            }
            
        }
    }
	
    function setposcustomer(obj) {
		if ($(obj).is(":checked")) {
			$("input[name ='ISPOSCUSTOMER']").val("1");
		} else {
			$("input[name ='ISPOSCUSTOMER']").val("0");
		}
	}
    function setproductprice(obj) {
		if ($(obj).is(":checked")) {
			$("input[name ='ISEDITPOSPRODUCTPRICE']").val("1");
		} else {
			$("input[name ='ISEDITPOSPRODUCTPRICE']").val("0");
		}
	}

    function setcashier(obj) {
		if ($(obj).is(":checked")) {
			$("input[name ='ISCASHIER']").val("1");
		} else {
			$("input[name ='ISCASHIER']").val("0");
		}
	}
    function setsalesman(obj) {
		if ($(obj).is(":checked")) {
			$("input[name ='ISSALESMAN']").val("1");
		} else {
			$("input[name ='ISSALESMAN']").val("0");
		}
	}
    
    function readURLLogo(input) {
        if (input.files && input.files[0]) {
            var reader = new FileReader();
            reader.onload = function (e) {
                $('#item_img_logo1')
                    .attr('src', e.target.result)
                    .width(100)
                    .height(50);
            };
            reader.readAsDataURL(input.files[0]);
            $('#item_logo').show();
            $('#item_btn_logo').hide();
            $('#logoremove').show();
            image_edit_new();
        }
    }
    
    function image_edit_new(){
        var formData = new FormData($('#empform')[0]);
        var userId= form.CUST_CODE.value;
    	if(userId){
        $.ajax({
            type: 'post',
            url: "/track/CatalogServlet?Submit=emp_img_edit",
           	dataType:'html',
        data:  formData,//{key:val}
        contentType: false,
        processData: false,
          
            success: function (data) {
            	console.log(data)
            	return true;
             
            },
            error: function (data) {
                alert(data.responseText);
                return false; 
            }
        });
    	}else{
    		alert("Please enter Employee Id");
    		return false; 
    	}
            
      }
    function setdepartmentid(obj,id){
    	var count = "0";
    	$("input[name ='departmentid']").each(function() {
    		if($(this).val() == id){
    			count = "1";
    	    }
    	});
    	if(count == "0"){
    		$(obj).closest('tr').find("input[name ='departmentid']").val(id);
    	}else{
    		alert("Department alredy selected");
    		$(obj).closest('tr').remove();
    	}
    }

    function image_delete_new(){
    	 var formData = $('form').serialize();
        var userId= form.CUST_CODE.value;
    	if(userId){
        $.ajax({
            type: 'post',
            url: "/track/CatalogServlet?Submit=emp_img_delete",
           	dataType:'html',
        data:  formData,//{key:val}
            success: function (data) {
            	console.log(data)
            	$('#logofile').val("");
            	$('#item_logo').hide();
                $('#item_btn_logo').show();
                $('#logoremove').hide();
            },
            error: function (data) {
            	
                alert(data.responseText);
            }
        });
    	}else{
    		alert("Please enter Employee Id");
    	}
            return false; 
      }	
    
    $(".reveal").on('click',function() {
        var $pwd = $("#password-field");
        $('#peye').toggleClass("fa-eye fa-eye-slash");
        if ($pwd.attr('type') === 'password') {
            $pwd.attr('type', 'text');
        } else {
            $pwd.attr('type', 'password');
        }
    });
    function departmentCallback(departmentData){
    	if(departmentData.STATUS="SUCCESS"){				
    		$("input[name ='DEPT']").typeahead('val', departmentData.DEPARTMENT);
    		$("input[name ='departmentid']").val(departmentData.DEPARTMENTID);
    		
    	}
    }
    function bankCallback(bankData){
    	if(bankData.STATUS="SUCCESS"){		
    		$("input[name ='BANKNAME']").typeahead('val', bankData.BANK_NAME);
    		
    		
    	}
    }
    function setOutletData(OUTLET,OUTLET_NAME){
    	$("input[name=OUTCODE]").val(OUTLET);
    	$("input[name=OUTLET_NAME]").val(OUTLET_NAME);
    }
</script>
<jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
</jsp:include>

