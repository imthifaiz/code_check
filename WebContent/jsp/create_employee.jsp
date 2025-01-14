<%@page import="org.hamcrest.core.IsNull"%>
<%@page import="com.track.db.object.LeaveTypePojo"%>
<%@page import="org.apache.commons.fileupload.FileItem"%>
<%@page import="org.apache.commons.fileupload.disk.DiskFileItemFactory"%>
<%@page import="org.apache.commons.fileupload.FileItemFactory"%>
<%@page import="org.apache.commons.fileupload.servlet.ServletFileUpload"%>
<%@ page import="com.track.util.*"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.db.object.EmployeeLeaveDET"%>
<%@ page import="com.track.db.object.HrEmpSalaryDET"%>
<%@ page import="com.track.gates.*"%>
<%@ page import="com.track.constants.*"%>
<%@ page import="com.track.dao.CustMstDAO"%>
<%@ page import="com.track.dao.CoaDAO"%>
<%@ page import="com.track.dao.EmployeeLeaveDetDAO"%>
<%@ page import="com.track.dao.HrEmpSalaryDetDAO"%>
<%@ page import="com.track.dao.EmpAttachDAO"%>
<%@ page import="com.track.dao.MovHisDAO"%>
<%@ page import="com.track.dao.TblControlDAO"%>
<%@ page import="com.track.dao.PlantMstDAO"%>
<%@ page import="com.track.dao.EmployeeDAO"%>
<%@ include file="header.jsp"%>

<%
String title = "Create Employee";
%>
<%@include file="sessionCheck.jsp" %>
<jsp:include page="header2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
	<jsp:param name="mainmenu" value="<%=IConstants.SETTINGS%>"/>
	<jsp:param name="submenu" value="<%=IConstants.EMPLOYEE%>"/>
</jsp:include>
<jsp:useBean id="eb"  class="com.track.gates.encryptBean" />
<jsp:useBean id="gn"  class="com.track.gates.Generator" />
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

label {
   cursor: pointer;
   /* Style as you please, it will become the visible UI component. */
}

#empfile {
   opacity: 0;
   position: absolute;
   z-index: -1;
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

   document.form.action  = "../payroll/createemployee?action=NEW";
   document.form.submit();
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
function onAdd(){
	
   var CUST_CODE   = document.form.CUST_CODE.value;
   var CUST_NAME   = document.form.CUST_NAME.value;
   var EMP_LOGIN_ID   = document.form.EMP_LOGIN_ID.value;
   var PASSWORD   = document.form.PASSWORD.value;
   var EMP_REPORTING   = document.form.EMP_REPORTING.value;
   var EMAIL = document.form.EMAIL.value;
   var ispay   = document.form.ispay.value;
  /*  var EMP_TYPE   = document.form.employeetype.value; */
   var GENDER = document.form.GENDER.value;
   var ValidNumber   = document.form.ValidNumber.value;
   if(ValidNumber != "") {alert("You have reached the limit of "+ValidNumber+" employees you can create"); return false; }
   if(CUST_CODE == "" || CUST_CODE == null) {alert("Please Enter Employee ID");document.form.CUST_CODE.focus(); return false; }
   
   if(CUST_NAME == "" || CUST_NAME == null) {
   alert("Please Enter Employee Name"); 
   document.form.CUST_NAME.focus();
   return false; 
   }  
   if(ispay == "1"){
	   if(EMP_LOGIN_ID == "" || EMP_LOGIN_ID == null) {
		   alert("Please Enter Employee Login ID"); 
		   document.form.EMP_LOGIN_ID.focus();
		   return false; 
		   }  
	   
	   if(PASSWORD == "" || PASSWORD == null) {
		   alert("Please Enter Password"); 
		   document.form.PASSWORD.focus();
		   return false; 
		   } 
	   
	   /* if(EMAIL == "" || EMAIL == null) {
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
   
  /*  if(EMP_TYPE == "" || EMP_TYPE == null) {
   alert("Please Enter Employee Type"); 
   document.form.employeetype.focus();
   return false; 
   } */
   
   var radio_choice = false;
   for (i = 0; i < document.form.GENDER.length; i++)
   {
       if (document.form.GENDER[i].checked)
       radio_choice = true; 
   }
   if (!radio_choice)
   {
   alert("Please Select Gender.");
   return (false);
   }
   /* if(form.COUNTRY_CODE.selectedIndex==0)
	{
	   alert("Please Select Country from Address");
	 form.COUNTRY_CODE.focus();
	 return false;
	} */
   
   document.form.action  = "../payroll/createemployee?action=ADD";
   document.form.submit();
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

function onUpdate(){
   var CUST_CODE   = document.form.CUST_CODE.value;
   if(CUST_CODE == "" || CUST_CODE == null) {alert("Please Enter Employee ID"); return false; }

   document.form.action  = "../payroll/createemployee?action=UPDATE";
   document.form.submit();
}
function onDelete(){

   var CUST_CODE   = document.form.CUST_CODE.value;
   if(CUST_CODE == "" || CUST_CODE == null) {alert("Please Enter Employee ID");  return false; }
   confirm("Are you sure to delete Employee permanently ");
   document.form.action  = "../payroll/createemployee?action=DELETE";
   document.form.submit();
}
function onView(){
   var CUST_CODE   = document.form.CUST_CODE.value;
   if(CUST_CODE == "" || CUST_CODE == null) {alert("Please Enter Employee ID"); return false; }

   document.form.action  = "../payroll/createemployee?action=VIEW";
   document.form.submit();
}
function onIDGen()
{
 document.form.action  = "../payroll/createemployee?action=Auto-ID";
 document.form.submit(); 
}

</script>
<%
String sUserId = (String) session.getAttribute("LOGIN_USER");
String res        = "";
String Defaultgr="DefaultGroup";
TblControlDAO _TblControlDAO =new TblControlDAO();
String ispay ="0",ispos="0";
String sNewEnb    = "enabled";
String sDeleteEnb = "enabled";
String sAddEnb    = "enabled";
String sUpdateEnb = "enabled";
String sCustEnb   = "enabled";
String action     = "";
String sCustCode  = "",
       sCustName  = "",
       sEmptypeid  = "",
       sEmploginid  = "",
       sEmppassword  = "",
       sEmpreportid  = "",
       sManworkhour  = "",
       sCustNameL  = "",
       sAddr1     = "",
       sAddr2     = "",
       sAddr3     = "", sAddr4     = "",
    		   sState   = "",
       sCountry   = "",
       sZip       = "",
       sDept      = "";
String sDesgination="",sTelNo="",sHpNo="",sFax="",sEmail="",sRemarks="",sNationality="",sDOB="",sGender="",DOB="",
sPASSPORTNUMBER="",sCOUNTRYOFISSUE="",sPASSPORTEXPIRYDATE="",FACEBOOK="",TWITTER="",LINKEDIN="",SKYPE="",sEMIRATESID="",sEMIRATESIDEXPIRY="",sVISANUMBER="",sVISAEXPIRYDATE="",
sDATEOFJOINING="",sDATEOFLEAVING="",sLABOURCARDNUMBER="",sWORKPERMITNUMBER="",sCONTRACTSTARTDATE="",sCONTRACTENDDATE="",sIBAN="",sBANKNAME="",sBANKROUTINGCODE="",sBRANCH="",
BASICSALARY="",HOUSERENTALLOWANCE="",TRANSPORTALLOWANCE="",COMMUNICATIONALLOWANCE="",OTHERALLOWANCE="",BONUS="",COMMISSION="",sSAVE_RED="",sSalary="0",
GRATUITY="",AIRTICKET="",LEAVESALARY="",ISAUTOEMAIL="",
ISPOSCUSTOMER="",ISEDITPOSPRODUCTPRICE="",ISCASHIER="",ISSALESMAN="",sSAVE_REDELETE="",sOutlet="",sOutCode="";
String effDt=gn.getDate();
StrUtils strUtils = new StrUtils();
PlantMstDAO _PlantMstDAO = new PlantMstDAO();
EmployeeUtil custUtil = new EmployeeUtil();
EmployeeDAO employeeDao = new EmployeeDAO();
custUtil.setmLogger(mLogger);


action            = strUtils.fString(request.getParameter("action"));
String plant = strUtils.fString((String)session.getAttribute("PLANT"));
String region = strUtils.fString((String) session.getAttribute("REGION"));
String pcountry = strUtils.fString((String) session.getAttribute("COUNTRY"));
ispay = _PlantMstDAO.getispayroll(plant);
ispos = _PlantMstDAO.getispos(plant);
if(pcountry.equalsIgnoreCase("United Arab Emirates"))
	pcountry="UAE";
    //Validate no.of Employee -- Azees 15.11.2020
	String NOOFEMPLOYEE=strUtils.fString((String) session.getAttribute("NOOFEMPLOYEE"));
	String ValidNumber="";
	int novalid =employeeDao.Employeecount(plant);
	if(!NOOFEMPLOYEE.equalsIgnoreCase("Unlimited"))
	{
		int convl = Integer.valueOf(NOOFEMPLOYEE);
		if(novalid>=convl)
		{
			ValidNumber=NOOFEMPLOYEE;
		}
	}
String numberOfDecimal = _PlantMstDAO.getNumberOfDecimal(plant);
sCustCode  = strUtils.fString(request.getParameter("CUST_CODE"));
DateUtils dateutils = new DateUtils();
//if(sCustCode.length() <= 0) sCustCode  = strUtils.fString(request.getParameter("CUST_CODE1"));
sCustName  = strUtils.InsertQuotes(strUtils.fString(request.getParameter("CUST_NAME")));
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
String ISCREATEONUSERINFO = (request.getParameter("ISCREATEONUSERINFO") != null) ? "1": "0";


if(sDOB.length() > 5){
	DOB = sDOB.substring(6) +"-"+ sDOB.substring(3, 5) +"-"+  sDOB.substring(0, 2);
}
sSAVE_RED  = strUtils.fString(request.getParameter("SAVE_RED"));
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




//1. >> New
if(action.equalsIgnoreCase("NEW")){
     
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
else if(action.equalsIgnoreCase("Auto-ID"))
{

String minseq="";  String sBatchSeq=""; boolean insertFlag=false;String sZero="";
//TblControlDAO _TblControlDAO =new TblControlDAO();
_TblControlDAO.setmLogger(mLogger);
      Hashtable  ht=new Hashtable();
     
      String query=" isnull(NXTSEQ,'') as NXTSEQ";
      ht.put(IDBConstants.PLANT,plant);
      ht.put(IDBConstants.TBL_FUNCTION,"EMPLOYEE");
      try{
      		boolean exitFlag=false; boolean resultflag=false;
      		exitFlag=_TblControlDAO.isExisit(ht,"",plant);
     
     
    	 	if (exitFlag==false)
      		{ 
                    
            	Map htInsert=null;
            	Hashtable htTblCntInsert  = new Hashtable();
           
            	htTblCntInsert.put(IDBConstants.PLANT,plant);
          
            	htTblCntInsert.put(IDBConstants.TBL_FUNCTION,"EMPLOYEE");
            	htTblCntInsert.put(IDBConstants.TBL_PREFIX1,"E");
             	htTblCntInsert.put("MINSEQ","0000");
             	htTblCntInsert.put("MAXSEQ","9999");
            	htTblCntInsert.put(IDBConstants.TBL_NEXT_SEQ, (String)IDBConstants.TBL_FIRST_NEX_SEQ);
            	htTblCntInsert.put(IDBConstants.CREATED_BY, sUserId);
            	htTblCntInsert.put(IDBConstants.CREATED_AT, (String)new DateUtils().getDateTime());
            	insertFlag= _TblControlDAO.insertTblControl(htTblCntInsert,plant);
            
        		sCustCode="E"+"0001";
      		}
      		else
      		{
           //--if exitflag is not false than we updated nxtseq batch number based on plant,currentmonth
         
           	   Map m= _TblControlDAO.selectRow(query, ht,"");
         	   sBatchSeq=(String)m.get("NXTSEQ");
          
           	   int inxtSeq=Integer.parseInt(((String)sBatchSeq.trim().toString()))+1;
          
           	   String updatedSeq=Integer.toString(inxtSeq);
               if(updatedSeq.length()==1)
               {
               	  sZero="000";
               }
           	   else if(updatedSeq.length()==2)
          	   {
             	  sZero="00";
           	   }
           	   else if(updatedSeq.length()==3)
           	   {
                  sZero="0";
               }
           
          
           		Map htUpdate = null;
          
           		Hashtable htTblCntUpdate = new Hashtable();
           		htTblCntUpdate.put(IDBConstants.PLANT,plant);
           		htTblCntUpdate.put(IDBConstants.TBL_FUNCTION,"EMPLOYEE");
           		htTblCntUpdate.put(IDBConstants.TBL_PREFIX1,"E");
           		StringBuffer updateQyery=new StringBuffer("set ");
           		updateQyery.append(IDBConstants.TBL_NEXT_SEQ +" = '"+ (String)updatedSeq.toString()+ "'");
         

        		//boolean updateFlag=_TblControlDAO.update(updateQyery.toString(),htTblCntUpdate,"",plant);
              	sCustCode="E"+sZero+updatedSeq;
           		}
           	} catch(Exception e)
            {
           		mLogger.exception(true,
    					"ERROR IN JSP PAGE - create_employee.jsp ", e);
            }

}
//2. >> Add
else if(action.equalsIgnoreCase("ADD")){

		boolean isMultipart = ServletFileUpload.isMultipartContent(request);
		EmployeeLeaveDetDAO employeeLeaveDetDAO = new EmployeeLeaveDetDAO();
		HrEmpSalaryDetDAO hrEmpSalaryDetDAO = new HrEmpSalaryDetDAO();
		List leavetype = new ArrayList(), totalentitlement = new ArrayList(), leaveyear = new ArrayList(),notes = new ArrayList();
		int leavetypeCount  = 0, totalentitlementCount  = 0, leaveyearCount  = 0, notesCount  = 0;
		List empSalary = new ArrayList(), empSalaryAmt = new ArrayList();
		int empSalaryCount  = 0, empSalaryAmtCount  = 0;
		List<Hashtable<String,String>> empAttachmentList = null;
		List<Hashtable<String,String>> empAttachmentInfoList = null;
		EmpAttachDAO empAttachDAO = new EmpAttachDAO();
		empAttachmentList = new ArrayList<Hashtable<String,String>>();
		empAttachmentInfoList = new ArrayList<Hashtable<String,String>>();
		if(isMultipart)
		{
	   		  boolean imageSizeflg = false;
	 			String fileLocation = DbBean.COMPANY_EMPLOYEE_PATH + "/"+ plant;
	 			String filetempLocation = DbBean.COMPANY_EMPLOYEE_PATH + "/temp" + "/" + plant;
	 			String result = "",strpath = "" , catlogpath = "";
	 			List<String> imageFormatsList = Arrays.asList(DbBean.imageFormatsArray);
			FileItemFactory factory = new DiskFileItemFactory();
			ServletFileUpload upload = new ServletFileUpload(factory);
			
			
			

			
			List items = upload.parseRequest(request);
			Iterator iterator = items.iterator();
			
			
			while (iterator.hasNext()) {
				FileItem item = (FileItem) iterator.next();
				if (item.isFormField()) {

					if (item.getFieldName()
							.equalsIgnoreCase("CUST_CODE")) {
						sCustCode = strUtils.fString(item.getString());
					}
					else if (item.getFieldName()
							.equalsIgnoreCase("CUST_NAME")) {
						sCustName = strUtils.fString(item.getString());
					}
					else if (item.getFieldName()
							.equalsIgnoreCase("employeetypeid")) {
						sEmptypeid = strUtils.fString(item.getString());
					}
					else if (item.getFieldName()
							.equalsIgnoreCase("EMP_LOGIN_ID")) {
						sEmploginid = strUtils.fString(item.getString());
					}
					else if (item.getFieldName()
							.equalsIgnoreCase("PASSWORD")) {
						sEmppassword = strUtils.fString(item.getString());
					}
					else if (item.getFieldName()
							.equalsIgnoreCase("repid")) {
						sEmpreportid = strUtils.fString(item.getString());
					}
					 else if (item.getFieldName()
							.equalsIgnoreCase("isapy")) {
						ispay = strUtils.fString(item.getString());
					}
					else if (item.getFieldName()
							.equalsIgnoreCase("L_CUST_NAME")) {
						sCustNameL = strUtils.fString(item.getString());
					}
					else if (item.getFieldName()
							.equalsIgnoreCase("ADDR1")) {
						sAddr1 = strUtils.fString(item.getString());
					}
					else if (item.getFieldName()
							.equalsIgnoreCase("ADDR2")) {
						sAddr2 = strUtils.fString(item.getString());
					}
					else if (item.getFieldName()
							.equalsIgnoreCase("ADDR3")) {
						sAddr3 = strUtils.fString(item.getString());
					}
					else if (item.getFieldName()
							.equalsIgnoreCase("ADDR4")) {
						sAddr4 = strUtils.fString(item.getString());
					}
					else if (item.getFieldName()
							.equalsIgnoreCase("STATE")) {
						sState = strUtils.InsertQuotes(strUtils.fString(item.getString()));
					}
					else if (item.getFieldName()
							.equalsIgnoreCase("COUNTRY")) {
						sCountry = strUtils.InsertQuotes(strUtils.fString(item.getString()));
					}
					else if (item.getFieldName()
							.equalsIgnoreCase("ZIP")) {
						sZip = strUtils.fString(item.getString());
					}
					else if (item.getFieldName()
							.equalsIgnoreCase("DEPT")) {
						sDept = strUtils.fString(item.getString());
					}
					else if (item.getFieldName()
							.equalsIgnoreCase("NATIONALITY")) {
						sNationality = strUtils.fString(item.getString());
					}
					else if (item.getFieldName()
							.equalsIgnoreCase("DESGINATION")) {
						sDesgination = strUtils.fString(item.getString());
					}
					else if (item.getFieldName()
							.equalsIgnoreCase("DOB")) {
						sDOB = strUtils.fString(item.getString());
					}
					else if (item.getFieldName()
							.equalsIgnoreCase("GENDER")) {
						sGender = strUtils.fString(item.getString());
					}
					else if (item.getFieldName()
							.equalsIgnoreCase("TELNO")) {
						sTelNo = strUtils.fString(item.getString());
					}
					else if (item.getFieldName()
							.equalsIgnoreCase("HPNO")) {
						sHpNo = strUtils.fString(item.getString());
					}
					else if (item.getFieldName()
							.equalsIgnoreCase("FAX")) {
						sFax = strUtils.fString(item.getString());
					}	
					else if (item.getFieldName()
							.equalsIgnoreCase("EMAIL")) {
						sEmail = strUtils.fString(item.getString());
					}
					else if (item.getFieldName()
							.equalsIgnoreCase("OUTLET_NAME")) {
						sOutlet = strUtils.fString(item.getString());
					}
					else if (item.getFieldName()
							.equalsIgnoreCase("OUTCODE")) {
						sOutCode = strUtils.fString(item.getString());
					}
					else if (item.getFieldName()
							.equalsIgnoreCase("REMARKS")) {
						sRemarks = strUtils.InsertQuotes(strUtils.fString(item.getString()));
					}
					else if (item.getFieldName()
							.equalsIgnoreCase("PASSPORTNUMBER")) {
						sPASSPORTNUMBER = strUtils.InsertQuotes(strUtils.fString(item.getString()));
					}
					else if (item.getFieldName()
							.equalsIgnoreCase("COUNTRYOFISSUE")) {
						sCOUNTRYOFISSUE = strUtils.InsertQuotes(strUtils.fString(item.getString()));
					}
					else if (item.getFieldName()
							.equalsIgnoreCase("PASSPORTEXPIRYDATE")) {
						sPASSPORTEXPIRYDATE = strUtils.InsertQuotes(strUtils.fString(item.getString()));
					}
					else if (item.getFieldName()
							.equalsIgnoreCase("FACEBOOK")) {
						FACEBOOK = strUtils.InsertQuotes(strUtils.fString(item.getString()));
					}
					else if (item.getFieldName()
							.equalsIgnoreCase("TWITTER")) {
						TWITTER = strUtils.InsertQuotes(strUtils.fString(item.getString()));
					}
					else if (item.getFieldName()
							.equalsIgnoreCase("LINKEDIN")) {
						LINKEDIN = strUtils.InsertQuotes(strUtils.fString(item.getString()));
					}
					else if (item.getFieldName()
							.equalsIgnoreCase("SKYPE")) {
						SKYPE = strUtils.InsertQuotes(strUtils.fString(item.getString()));
					}
					else if (item.getFieldName()
							.equalsIgnoreCase("EMIRATESIDEXPIRY")) {
						sEMIRATESIDEXPIRY = strUtils.InsertQuotes(strUtils.fString(item.getString()));
					}
					else if (item.getFieldName()
							.equalsIgnoreCase("VISANUMBER")) {
						sVISANUMBER = strUtils.InsertQuotes(strUtils.fString(item.getString()));
					}
					else if (item.getFieldName()
							.equalsIgnoreCase("EMIRATESID")) {
						sEMIRATESID = strUtils.InsertQuotes(strUtils.fString(item.getString()));
					}
					else if (item.getFieldName()
							.equalsIgnoreCase("VISAEXPIRYDATE")) {
						sVISAEXPIRYDATE = strUtils.InsertQuotes(strUtils.fString(item.getString()));
					}
					else if (item.getFieldName()
							.equalsIgnoreCase("DATEOFJOINING")) {
						sDATEOFJOINING = strUtils.InsertQuotes(strUtils.fString(item.getString()));
					}
					else if (item.getFieldName()
							.equalsIgnoreCase("DATEOFLEAVING")) {
						sDATEOFLEAVING = strUtils.InsertQuotes(strUtils.fString(item.getString()));
					}
					else if (item.getFieldName()
							.equalsIgnoreCase("LABOURCARDNUMBER")) {
						sLABOURCARDNUMBER = strUtils.InsertQuotes(strUtils.fString(item.getString()));
					}
					else if (item.getFieldName()
							.equalsIgnoreCase("WORKPERMITNUMBER")) {
						sWORKPERMITNUMBER = strUtils.InsertQuotes(strUtils.fString(item.getString()));
					}
					else if (item.getFieldName()
							.equalsIgnoreCase("CONTRACTSTARTDATE")) {
						sCONTRACTSTARTDATE = strUtils.InsertQuotes(strUtils.fString(item.getString()));
					}
					else if (item.getFieldName()
							.equalsIgnoreCase("CONTRACTENDDATE")) {
						sCONTRACTENDDATE = strUtils.InsertQuotes(strUtils.fString(item.getString()));
					}
					else if (item.getFieldName()
							.equalsIgnoreCase("IBAN")) {
						sIBAN = strUtils.InsertQuotes(strUtils.fString(item.getString()));
					}
					else if (item.getFieldName()
							.equalsIgnoreCase("BANKNAME")) {
						sBANKNAME = strUtils.InsertQuotes(strUtils.fString(item.getString()));
					}
					else if (item.getFieldName()
							.equalsIgnoreCase("BANKROUTINGCODE")) {
						sBANKROUTINGCODE = strUtils.InsertQuotes(strUtils.fString(item.getString()));
					}
					else if (item.getFieldName()
							.equalsIgnoreCase("BRANCH")) {
						sBRANCH = strUtils.InsertQuotes(strUtils.fString(item.getString()));
					}
					else if (item.getFieldName()
							.equalsIgnoreCase("BASICSALARY")) {
						BASICSALARY = strUtils.InsertQuotes(strUtils.fString(item.getString()));
					}
					else if (item.getFieldName()
							.equalsIgnoreCase("HOUSERENTALLOWANCE")) {
						HOUSERENTALLOWANCE = strUtils.InsertQuotes(strUtils.fString(item.getString()));
					}
					else if (item.getFieldName()
							.equalsIgnoreCase("TRANSPORTALLOWANCE")) {
						TRANSPORTALLOWANCE = strUtils.InsertQuotes(strUtils.fString(item.getString()));
					}
					else if (item.getFieldName()
							.equalsIgnoreCase("COMMUNICATIONALLOWANCE")) {
						COMMUNICATIONALLOWANCE = strUtils.InsertQuotes(strUtils.fString(item.getString()));
					}
					else if (item.getFieldName()
							.equalsIgnoreCase("OTHERALLOWANCE")) {
						OTHERALLOWANCE = strUtils.InsertQuotes(strUtils.fString(item.getString()));
					}
					else if (item.getFieldName()
							.equalsIgnoreCase("BONUS")) {
						BONUS = strUtils.InsertQuotes(strUtils.fString(item.getString()));
					}
					else if (item.getFieldName()
							.equalsIgnoreCase("COMMISSION")) {
						COMMISSION = strUtils.InsertQuotes(strUtils.fString(item.getString()));
					}
					else if (item.getFieldName()
							.equalsIgnoreCase("GRATUITY")) {
						GRATUITY = strUtils.InsertQuotes(strUtils.fString(item.getString()));
					}
					else if (item.getFieldName()
							.equalsIgnoreCase("AIRTICKET")) {
						AIRTICKET = strUtils.InsertQuotes(strUtils.fString(item.getString()));
					}
					else if (item.getFieldName()
							.equalsIgnoreCase("LEAVESALARY")) {
						LEAVESALARY = strUtils.InsertQuotes(strUtils.fString(item.getString()));
					}
					else if (item.getFieldName()
							.equalsIgnoreCase("isautoemail")) {
						ISAUTOEMAIL = strUtils.InsertQuotes(strUtils.fString(item.getString()));
					}
					
					else if (item.getFieldName()
							.equalsIgnoreCase("ISCREATEONUSERINFO")) {
						ISCREATEONUSERINFO = strUtils.InsertQuotes(StrUtils.fString((item.getString() != null) ? "1": "0").trim());
					}
					
					
					
					else if (item.getFieldName()
							.equalsIgnoreCase("ISPOSCUSTOMER")) {
						ISPOSCUSTOMER = strUtils.InsertQuotes(strUtils.fString(item.getString()));
					}
					else if (item.getFieldName()
							.equalsIgnoreCase("ISEDITPOSPRODUCTPRICE")) {
						ISEDITPOSPRODUCTPRICE = strUtils.InsertQuotes(strUtils.fString(item.getString()));
					}
					else if (item.getFieldName()
							.equalsIgnoreCase("ISCASHIER")) {
						ISCASHIER = strUtils.InsertQuotes(strUtils.fString(item.getString()));
					}
					else if (item.getFieldName()
							.equalsIgnoreCase("ISSALESMAN")) {
						ISSALESMAN = strUtils.InsertQuotes(strUtils.fString(item.getString()));
					}
					
				}
				else if (!item.isFormField()
						&& (item.getName().length() > 0)) {
					if(item.getFieldName().equalsIgnoreCase("IMAGE_UPLOAD")){
						String fileName = item.getName();
						long size = item.getSize();
	
						size = size / 1024;
						// size = size / 1000;
						System.out.println("size of the Image imported :::"
								+ size);
						
						String extension = fileName.substring(fileName.lastIndexOf(".")).toLowerCase();
						System.out.println("Extensions:::::::" + extension);
						if (!imageFormatsList.contains(extension)) {
							result = "<font color=\"red\"> Image extension not valid </font>";
							imageSizeflg = true;
						}
						
						//checking image size for 2MB
						if (size > 2040) // condtn checking Image size
						{
							result = "<font color=\"red\">  Catalog Image size greater than 1 MB </font>";
	
							imageSizeflg = true;
	
						}
						File path = new File(fileLocation);
						if (!path.exists()) {
							boolean status = path.mkdirs();
						}
						fileName = fileName.substring(fileName
								.lastIndexOf("\\") + 1);
						File uploadedFile = new File(path + "/" + strUtils.RemoveSlash(sCustCode)
								+ ".JPEG");
	
						// System.out.println("uploaded file"+uploadedFile.getAbsolutePath());
						// if (uploadedFile.exists()) {
						// uploadedFile.delete();
						// }
						strpath = path + "/" + fileName;
						catlogpath = uploadedFile.getAbsolutePath();
						if (!imageSizeflg && !uploadedFile.exists())
							item.write(uploadedFile);
	
						// delete temp uploaded file
						File tempPath = new File(filetempLocation);
						if (tempPath.exists()) {
							File tempUploadedfile = new File(tempPath + "/"
									+ strUtils.RemoveSlash(sCustCode) + ".JPEG");
							if (tempUploadedfile.exists()) {
								tempUploadedfile.delete();
							}
						}
					}
					
				}
				
				if (item.isFormField()) {
					if (item.getFieldName().equalsIgnoreCase("leavetypeid")) {
						leavetype.add(leavetypeCount, StrUtils.fString(item.getString()).trim());
						leavetypeCount++;
					}
				}
				if (item.isFormField()) {
					if (item.getFieldName().equalsIgnoreCase("totalentitlement")) {
						totalentitlement.add(totalentitlementCount,StrUtils.fString(item.getString()).trim());
						totalentitlementCount++;
					}
				}
				if (item.isFormField()) {
					if (item.getFieldName().equalsIgnoreCase("leaveyear")) {
						leaveyear.add(leaveyearCount, StrUtils.fString(item.getString()).trim());
						leaveyearCount++;
					}
				}
				if (item.isFormField()) {
					if (item.getFieldName().equalsIgnoreCase("notes")) {
						notes.add(notesCount, StrUtils.fString(item.getString()).trim());
						notesCount++;
					}
				}
				if (item.isFormField()) {
					if (item.getFieldName().equalsIgnoreCase("empSalary")) {
						empSalary.add(empSalaryCount, StrUtils.fString(item.getString()).trim());
						empSalaryCount++;
					}
				}
				if (item.isFormField()) {
					if (item.getFieldName().equalsIgnoreCase("empSalaryAmt")) {
						empSalaryAmt.add(empSalaryAmtCount,StrUtils.fString(item.getString()).trim());
						empSalaryAmtCount++;
					}
				}
				
				
				
				if (!item.isFormField() && (item.getName().length() > 0)) {
					if(item.getFieldName().equalsIgnoreCase("file")){
						String fileLocationATT = "C:/ATTACHMENTS/EMPLOYEE" + "/"+ sCustCode;
						String filetempLocationATT = "C:/ATTACHMENTS/EMPLOYEE" + "/temp" + "/"+ sCustCode;
						String fileName = StrUtils.fString(item.getName()).trim();
						fileName = fileName.substring(fileName.lastIndexOf("\\") + 1);
						
						File path = new File(fileLocationATT);
						if (!path.exists()) {
							path.mkdirs();
						}
						
						//fileName = fileName.substring(fileName.lastIndexOf("\\") + 1);
						File uploadedFile = new File(path + "/" +fileName);
						if (uploadedFile.exists()) {
							uploadedFile.delete();
						}
						
						item.write(uploadedFile);
						
						// delete temp uploaded file
						File tempPath = new File(filetempLocationATT);
						if (tempPath.exists()) {
							File tempUploadedfile = new File(tempPath + "/"+ fileName);
							if (tempUploadedfile.exists()) {
								tempUploadedfile.delete();
							}
						}
						Hashtable empAttachment = new Hashtable<String, String>();
						empAttachment.put("PLANT", plant);
						empAttachment.put("FILETYPE", item.getContentType());
						empAttachment.put("FILENAME", fileName);
						empAttachment.put("FILESIZE", String.valueOf(item.getSize()));
						empAttachment.put("FILEPATH", fileLocationATT);
						empAttachment.put("CRAT",dateutils.getDateTime());
						empAttachment.put("CRBY",sUserId);
						empAttachment.put("UPAT",dateutils.getDateTime());
						empAttachmentList.add(empAttachment);
					}
				}
				
				
			}
			if (imageSizeflg) {
				res  = result;
			}
			else{
	 	Hashtable htcond = new Hashtable();
	 	htcond.put(IDBConstants.PLANT,plant);
	 	htcond.put(IDBConstants.EMPNO,sCustCode);
  if(!custUtil.isExistsEmployee(htcond)) // if the Customer exists already
    {
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
	  
	  BASICSALARY=StrUtils.addZeroes(BASICSALARYVALUE, numberOfDecimal);
	  HOUSERENTALLOWANCE=StrUtils.addZeroes(HOUSERENTALLOWANCEVALUE, numberOfDecimal);
	  TRANSPORTALLOWANCE=StrUtils.addZeroes(TRANSPORTALLOWANCEVALUE, numberOfDecimal);
	  COMMUNICATIONALLOWANCE=StrUtils.addZeroes(COMMUNICATIONALLOWANCEVALUE, numberOfDecimal);
	  OTHERALLOWANCE=StrUtils.addZeroes(OTHERALLOWANCEVALUE, numberOfDecimal);
	  BONUS=StrUtils.addZeroes(BONUSVALUE, numberOfDecimal);
	  COMMISSION=StrUtils.addZeroes(COMMISSIONVALUE, numberOfDecimal);
	  GRATUITY=StrUtils.addZeroes(GRATUITYVALUE, numberOfDecimal);
	  AIRTICKET=StrUtils.addZeroes(AIRTICKETVALUE, numberOfDecimal);
	  LEAVESALARY=StrUtils.addZeroes(LEAVESALARYVALUE, numberOfDecimal);
	  String empPwd   = eb.encrypt(sEmppassword);
	  
          Hashtable ht = new Hashtable();
          ht.put(IConstants.PLANT,plant);
          ht.put(IDBConstants.EMPNO,sCustCode);
          ht.put(IDBConstants.FNAME,sCustName);
          ht.put(IDBConstants.EMPLOYEETYPEID,sEmptypeid);
          ht.put(IDBConstants.EMPUSERID,sEmploginid);
          ht.put(IDBConstants.PASSWORD_EMP,empPwd);
          ht.put(IDBConstants.REPORTING_INCHARGE,sEmpreportid);
         /*  ht.put(IDBConstants.NUMBEROFMANDAYS,sManworkhour); */
          ht.put(IConstants.CUSTOMER_LAST_NAME,sCustNameL);
          ht.put(IDBConstants.GENDER,sGender);
          ht.put(IDBConstants.DOB,sDOB);
          ht.put(IDBConstants.DEPTARTMENT,sDept);
          ht.put(IConstants.DESGINATION,sDesgination);
          ht.put(IDBConstants.DATEOFJOINING,sDATEOFJOINING);
          ht.put(IDBConstants.DATEOFLEAVING,sDATEOFLEAVING);
          ht.put(IDBConstants.NATIONALITY,sNationality);
          ht.put(IConstants.TELNO,sTelNo);
          ht.put(IConstants.HPNO,sHpNo);
          //ht.put(IConstants.FAX,sFax);
          ht.put(IConstants.EMAIL,sEmail);
	      ht.put(IConstants.FACEBOOK,FACEBOOK);
	      ht.put(IConstants.TWITTER,TWITTER);
	      ht.put(IConstants.LINKEDIN,LINKEDIN);
	      ht.put(IConstants.SKYPE,SKYPE);
	      ht.put(IDBConstants.PASSPORTNUMBER,sPASSPORTNUMBER);
	      if(sCOUNTRYOFISSUE.equalsIgnoreCase("Select Country"))
	    	  sCOUNTRYOFISSUE="";
	      ht.put(IDBConstants.COUNTRYOFISSUE,sCOUNTRYOFISSUE);
	      ht.put(IDBConstants.PASSPORTEXPIRYDATE,sPASSPORTEXPIRYDATE);
          ht.put(IConstants.UNITNO,sAddr1);
          ht.put(IConstants.BUILDING,sAddr2);
          ht.put(IConstants.STREET,sAddr3);
          ht.put(IConstants.CITY,sAddr4);
          ht.put(IConstants.OUTLETS_CODE,sOutCode);
          if(sState.equalsIgnoreCase("Select State"))
				sState="";
          ht.put(IConstants.STATE,sState);
          if(sCountry.equalsIgnoreCase("Select Country"))
        	  sCountry="";
          ht.put(IConstants.COUNTRY,sCountry);
          ht.put(IConstants.ZIP,sZip);
          ht.put(IDBConstants.EMIRATESID,sEMIRATESID);
          ht.put(IDBConstants.EMIRATESIDEXPIRY,sEMIRATESIDEXPIRY);
          ht.put(IDBConstants.VISANUMBER,sVISANUMBER);
          ht.put(IDBConstants.VISAEXPIRYDATE,sVISAEXPIRYDATE);
          ht.put(IDBConstants.LABOURCARDNUMBER,sLABOURCARDNUMBER);
          ht.put(IDBConstants.WORKPERMITNUMBER,sWORKPERMITNUMBER);
          ht.put(IDBConstants.CONTRACTSTARTDATE,sCONTRACTSTARTDATE);
          ht.put(IDBConstants.CONTRACTENDDATE,sCONTRACTENDDATE);
          if(sBANKNAME.equalsIgnoreCase("Select Bank"))
        	  sBANKNAME="";
          ht.put(IDBConstants.BANKNAME,sBANKNAME);
          ht.put(IDBConstants.IBAN,sIBAN);
          ht.put(IDBConstants.BANKROUTINGCODE,sBANKROUTINGCODE);
          ht.put(IDBConstants.BASICSALARY,BASICSALARY);
          ht.put(IDBConstants.HOUSERENTALLOWANCE,HOUSERENTALLOWANCE);
          ht.put(IDBConstants.TRANSPORTALLOWANCE,TRANSPORTALLOWANCE);
          ht.put(IDBConstants.COMMUNICATIONALLOWANCE,COMMUNICATIONALLOWANCE);
          ht.put(IDBConstants.OTHERALLOWANCE,OTHERALLOWANCE);
          ht.put(IDBConstants.BONUS,BONUS);
          ht.put(IDBConstants.COMMISSION,COMMISSION);
          ht.put(IConstants.REMARKS,sRemarks);
          ht.put(IConstants.CREATED_AT,new DateUtils().getDateTime());
          ht.put(IConstants.CREATED_BY,sUserId);
          ht.put(IConstants.ISACTIVE,"Y");
          ht.put(IDBConstants.GRATUITY,GRATUITY);
          ht.put(IDBConstants.AIRTICKET,AIRTICKET);
          ht.put(IDBConstants.LEAVESALARY,LEAVESALARY);
          ht.put(IDBConstants.ISAUTOEMAILPAY,ISAUTOEMAIL);
          ht.put("ISCREATEONUSERINFO",ISCREATEONUSERINFO);
          ht.put("ISPOSCUSTOMER",ISPOSCUSTOMER);
          ht.put("ISEDITPOSPRODUCTPRICE",ISEDITPOSPRODUCTPRICE);
          ht.put("ISCASHIER",ISCASHIER);
          ht.put("ISSALESMAN",ISSALESMAN);
          
          MovHisDAO mdao = new MovHisDAO(plant);
          mdao.setmLogger(mLogger);
          Hashtable htm = new Hashtable();
          htm.put(IDBConstants.PLANT,plant);
          htm.put(IDBConstants.DIRTYPE,TransactionConstants.ADD_EMPLOYEE);
          htm.put("RECID","");
          htm.put("ITEM",sCustCode);
          htm.put(IDBConstants.CREATED_BY,sUserId);
         
          if(!sRemarks.equals(""))
		  {
			htm.put(IDBConstants.REMARKS, sCustName+","+sRemarks);
		  }
		  else
		  {
			htm.put(IDBConstants.REMARKS, sCustName);
		  }
          
          htm.put(IDBConstants.CREATED_AT,dateutils.getDateTime());
          htm.put(IDBConstants.TRAN_DATE,dateutils.getDateinyyyy_mm_dd(dateutils.getDate()));
          boolean updateFlag;
  		if(sCustCode!="E0001")
		{
    		  boolean exitFlag = false;
			Hashtable htv = new Hashtable();				
			htv.put(IDBConstants.PLANT, plant);
			htv.put(IDBConstants.TBL_FUNCTION, "EMPLOYEE");
			exitFlag = _TblControlDAO.isExisit(htv, "", plant);
			if (exitFlag) 
    		  updateFlag=_TblControlDAO.updateSeqNo("EMPLOYEE",plant);
			else
			{
				boolean insertFlag = false;
				Map htInsert=null;
            	Hashtable htTblCntInsert  = new Hashtable();           
            	htTblCntInsert.put(IDBConstants.PLANT,plant);          
            	htTblCntInsert.put(IDBConstants.TBL_FUNCTION,"EMPLOYEE");
            	htTblCntInsert.put(IDBConstants.TBL_PREFIX1,"E");
             	htTblCntInsert.put("MINSEQ","0000");
             	htTblCntInsert.put("MAXSEQ","9999");
            	htTblCntInsert.put(IDBConstants.TBL_NEXT_SEQ, (String)IDBConstants.TBL_FIRST_NEX_SEQ);
            	htTblCntInsert.put(IDBConstants.CREATED_BY, sUserId);
            	htTblCntInsert.put(IDBConstants.CREATED_AT, (String)new DateUtils().getDateTime());
            	insertFlag= _TblControlDAO.insertTblControl(htTblCntInsert,plant);
			}
	}
  		  catlogpath = catlogpath.replace('\\', '/');
		  ht.put(IDBConstants.CATLOGPATH, catlogpath);
         // boolean custInserted = custUtil.insertEmployeeMst(ht);
         int custInserted = custUtil.insertEmployeeMstretid(ht);
         
          if(custInserted > 0){
        	  for(int i =0 ; i < leavetype.size() ; i++){
        		  String lvtypeid = (String)leavetype.get(i);
        		 if(!lvtypeid.equalsIgnoreCase("0")){
	        		 EmployeeLeaveDET employeeLeavedet = new EmployeeLeaveDET();
	        		 employeeLeavedet.setPLANT(plant);
	        		 employeeLeavedet.setEMPNOID(custInserted);
	        		 employeeLeavedet.setLEAVETYPEID(Integer.valueOf((String)leavetype.get(i)));
	        		 employeeLeavedet.setTOTALENTITLEMENT(Double.valueOf((String)totalentitlement.get(i)));
	        		 employeeLeavedet.setLEAVEBALANCE(Double.valueOf((String)totalentitlement.get(i)));
	        		 employeeLeavedet.setLEAVEYEAR((String)leaveyear.get(i));
	        		 employeeLeavedet.setNOTE((String)notes.get(i));
	        		 employeeLeavedet.setCRAT(dateutils.getDate());
	        		 employeeLeavedet.setCRBY(sUserId);
	        	  	 employeeLeaveDetDAO.addEmployeeLeavedet(employeeLeavedet);
        	  	 }
        	  }
        	  
        	  //Add Salary - (Azees 6.8.20)
        	  for(int i =0 ; i < empSalary.size() ; i++){
        		  String empSalarytype = (String)empSalary.get(i);
        		 if(!empSalarytype.equalsIgnoreCase("")){
        			 HrEmpSalaryDET hrEmpSalaryDET = new HrEmpSalaryDET();
        			 hrEmpSalaryDET.setPLANT(plant);
        			 hrEmpSalaryDET.setEMPNOID(custInserted);
        			 hrEmpSalaryDET.setSALARYTYPE((String)empSalary.get(i));
        			 hrEmpSalaryDET.setSALARY(Double.valueOf((String)empSalaryAmt.get(i)));
        			 hrEmpSalaryDET.setCRAT(dateutils.getDate());
        			 hrEmpSalaryDET.setCRBY(sUserId);
        			 hrEmpSalaryDetDAO.addSalarydet(hrEmpSalaryDET);
        	  	 }
        	  }
        	  
        	  int attchSize = empAttachmentList.size();
			  for(int i =0 ; i < attchSize ; i++){
					Hashtable empAttachmentat = new Hashtable<String, String>();
					empAttachmentat = empAttachmentList.get(i);
					empAttachmentat.put("EMPID", String.valueOf(custInserted));
					empAttachmentInfoList.add(empAttachmentat);
			  }
			  empAttachDAO.addempAttachments(empAttachmentInfoList, plant);
			  if(ispay.equalsIgnoreCase("1") || ispos.equalsIgnoreCase("1")){
			    Hashtable htTblloginInsert  = new Hashtable();           
			    htTblloginInsert.put(IDBConstants.PLANT,plant);          
			    htTblloginInsert.put("EMPNOID",String.valueOf(custInserted));
			    htTblloginInsert.put("EMPUSERID",sEmploginid);
			    htTblloginInsert.put("PASSWORD",empPwd);
			    htTblloginInsert.put("ISCASHIER",ISCASHIER);
			    htTblloginInsert.put("ISSALESMAN",ISSALESMAN);
	           	htTblloginInsert.put(IDBConstants.CREATED_BY, sUserId);
	           	htTblloginInsert.put(IDBConstants.CREATED_AT, (String)new DateUtils().getDateTime());
	           	custUtil.insertIntoEmployeeuseidMst(htTblloginInsert);
			  }
			  if (ISCREATEONUSERINFO.equalsIgnoreCase("1")) {
				    Hashtable<String, String> htTblLoginInsert = new Hashtable<>();
				    userBean _userBean = new userBean();
				        if(_userBean.checkuserpassword(sEmploginid)){
				        	result="User Not Created, Employee ALready Exist On User Details";
				        }else{
				        	 
					            htTblLoginInsert.put("DEPT", plant);
					            htTblLoginInsert.put("USER_ID", sEmploginid);
					            htTblLoginInsert.put("PASSWORD", empPwd);
					            htTblLoginInsert.put("USER_NAME", sCustName);
					            htTblLoginInsert.put("ACCESS_COUNTER", ISCASHIER);
					            htTblLoginInsert.put("RANK", Defaultgr);
					            htTblLoginInsert.put("REMARKS",  "user created on employee");
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
			  
          }
          
          boolean  inserted = mdao.insertIntoMovHis(htm);
          if((custInserted > 0) && inserted) {
                    //res = "<font class = "+IConstants.SUCCESS_COLOR+">Employee Added Successfully</font>";
        	  //response.sendRedirect("jsp/employeeSummary.jsp?PGaction=View&result=Employee Added Successfully");
        	    CoaUtil coaUtil = new CoaUtil();
        	    CoaDAO coaDAO = new CoaDAO();
				String accname = sCustCode+"-"+sCustName;
								
				Hashtable<String, String> accountHt = new Hashtable<>();
				accountHt.put("PLANT", plant);
				accountHt.put("ACCOUNTTYPE", "6");
				accountHt.put("ACCOUNTDETAILTYPE", "20");
				accountHt.put("ACCOUNT_NAME", accname);
				accountHt.put("DESCRIPTION", "");
				accountHt.put("ISSUBACCOUNT", "1");
				accountHt.put("SUBACCOUNTNAME", "38");
				accountHt.put("OPENINGBALANCE", "");
				accountHt.put("OPENINGBALANCEDATE", "");
				accountHt.put("CRAT", dateutils.getDateTime());
				accountHt.put("CRBY", sUserId);
				accountHt.put("UPAT", dateutils.getDateTime());
				
				String Acode = coaDAO.GetAccountCodeByID("38", plant);
				List<Map<String, String>> subaccount = coaDAO.getMaxSubCode(plant, "38");
				String scode ="";
				String atcode ="";
				if(subaccount.size() > 0) {
					for (int i = 0; i < subaccount.size(); i++) {
						Map<String, String> m = subaccount.get(i);
						scode = m.get("CODE");
					}
					if(scode == null){
						atcode = "01";
					}else{
						int count = Integer.valueOf(scode);
						atcode = String.valueOf(count+1);
						if(atcode.length() == 1) {
							atcode = "0"+atcode;
						}
					}
				}else {
					atcode = "01";
				}
				String accountcode = Acode +"-"+atcode;
				accountHt.put("ACCOUNT_CODE", accountcode);
				accountHt.put("SUB_CODE", atcode);
				
				coaUtil.addAccount(accountHt, plant);
        	  
        	  sSAVE_RED="Employee Added Successfully"+result;

          } else {
        	  sSAVE_RED="Failed to add New Employee";
//                     res = "<font class = "+IConstants.FAILED_COLOR+">Failed to add New Employee</font>";

          }
    }else{
    	sSAVE_RED="Employee Exists already. Try again";
//            res = "<font class = "+IConstants.FAILED_COLOR+">Employee Exists already. Try again</font>";

    }
		}
}
}
//3. >> Update
else if(action.equalsIgnoreCase("UPDATE"))  {
	Hashtable htcond = new Hashtable();
 	htcond.put(IDBConstants.PLANT,plant);
 	htcond.put(IDBConstants.EMPNO,sCustCode);
   if(custUtil.isExistsEmployee(htcond))
    {
          Hashtable htUpdate = new Hashtable();
          htUpdate.put(IConstants.PLANT,plant);
          htUpdate.put(IDBConstants.EMPNO,sCustCode);
          htUpdate.put(IDBConstants.FNAME,sCustName);
          htUpdate.put(IConstants.CUSTOMER_LAST_NAME,sCustNameL);
          htUpdate.put(IDBConstants.GENDER,sGender);
          htUpdate.put(IDBConstants.DATEOFBIRTH,DOB);
          htUpdate.put(IDBConstants.DEPT,sDept);
          htUpdate.put(IDBConstants.NATIONALITY,sNationality);
          htUpdate.put(IConstants.ADDRESS1,sAddr1);
          htUpdate.put(IConstants.ADDRESS2,sAddr2);
          htUpdate.put(IConstants.ADDRESS3,sAddr3);
          htUpdate.put(IConstants.ADDRESS4,sAddr4);
          htUpdate.put(IConstants.STATE,sState);
          htUpdate.put(IConstants.COUNTRY,sCountry);
          htUpdate.put(IConstants.ZIP,sZip);
          htUpdate.put(IConstants.DESGINATION,sDesgination);
          htUpdate.put(IConstants.TELNO,sTelNo);
          htUpdate.put(IConstants.HPNO,sHpNo);
          htUpdate.put(IConstants.FAX,sFax);
          htUpdate.put(IConstants.EMAIL,sEmail);
          htUpdate.put(IConstants.OUTLETS_CODE,sOutCode);
          htUpdate.put(IConstants.REMARKS,sRemarks);
          htUpdate.put(IConstants.UPDATED_AT,new DateUtils().getDateTime());
          htUpdate.put(IConstants.UPDATED_BY,sUserId);
          htUpdate.put(IConstants.ISACTIVE,"Y");

          
          MovHisDAO mdao = new MovHisDAO(plant);
          mdao.setmLogger(mLogger);
          Hashtable htm = new Hashtable();
          htm.put("PLANT",plant);
          htm.put(IDBConstants.DIRTYPE,TransactionConstants.UPDATE_EMPLOYEE);
          htm.put("RECID","");
          htm.put("UPBY",sUserId);   htm.put("CRBY",sUserId);
          htm.put("CRAT",dateutils.getDateTime());
          htm.put("UPAT",dateutils.getDateTime());
          htm.put(IDBConstants.TRAN_DATE,dateutils.getDateinyyyy_mm_dd(dateutils.getDate()));  
          boolean custUpdated = custUtil.updateEmployeeMst(htUpdate, htcond);
          boolean  inserted = mdao.insertIntoMovHis(htm);
          if(custUpdated&&inserted) {
        	  sSAVE_RED="Employee Updated Successfully";
//                     res = "<font class = "+IConstants.SUCCESS_COLOR+">Employee Updated Successfully</font>";
          } else {
        	  sSAVE_RED="Failed to Update Employee";
//                     res = "<font class = "+IConstants.FAILED_COLOR+">Failed to Update Employee</font>";
          }
    }else{
    	sSAVE_RED="Employee doesn't not Exists. Try again";
//            res = "<font class = "+IConstants.FAILED_COLOR+">Employee doesn't not Exists. Try again</font>";

    }
}

//4. >> Delete
else if(action.equalsIgnoreCase("DELETE")){
	Hashtable htcond = new Hashtable();
 	htcond.put(IDBConstants.PLANT,plant);
 	htcond.put(IDBConstants.EMPNO,sCustCode);
    if(custUtil.isExistsEmployee(htcond))
    {
          boolean custDeleted = custUtil.deleteEmployeeid(htcond);
          if(custDeleted) {
                    res = "<font class = "+IConstants.SUCCESS_COLOR+">Employee Deleted Successfully</font>";
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
                    sNationality =""; sDesgination="";sTelNo="";sHpNo="";sFax="";sEmail="";sDOB="";sGender="";sRemarks="";sOutCode="";
 

          } else {
        	  sSAVE_REDELETE="Failed to Delete Employee";
//                     res = "<font class = "+IConstants.FAILED_COLOR+">Failed to Delete Employee</font>";
                    sAddEnb = "enabled";
          }
    }else{
    	sSAVE_REDELETE="Employee doesn't not Exists. Try again";
//            res = "<font class = "+IConstants.FAILED_COLOR+">Employee doesn't not Exists. Try again</font>";
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

<div class="container-fluid m-t-20">
	 <div class="box"> 
	 <!-- Muruganantham Modified on 16.02.2022 -->
            <ul class="breadcrumb backpageul">      	
                <li><a href="../home"><span class="underline-on-hover">Dashboard</span> </a></li>   
                <li><a href="../payroll/employee"><span class="underline-on-hover">Employee Summary</span> </a></li>                                           
                <li><label>Create Employee</label></li>                                   
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

   <CENTER><strong><%=res%></strong></CENTER>
<form class="form-horizontal" name="form" method="post" autocomplete="off" enctype="multipart/form-data">
<div class="col-sm-6">    
<div class="form-group">
      <label class="control-label col-form-label col-sm-4 required" for="Create Employee ID">Employee Id</label>
      <div class="col-sm-8">
      <div class="input-group">   
      	  	 <input name="CUST_CODE" type="TEXT" value="<%=sCustCode%>"	size="50" MAXLENGTH=50  autocomplete="off" class="form-control"> 
   		 	<span class="input-group-addon"  onClick="onIDGen();">
   		 	<a href="#" data-toggle="tooltip" data-placement="top" title="Auto-Generate">
   		 	<i class="glyphicon glyphicon-edit" style="font-size: 20px;"></i></a></span>
  		</div>
  		<INPUT type="hidden" name="CUST_CODE1" value="<%=sCustCode%>">
  		<INPUT type="hidden" name="COUNTRY" value="<%=sCountry%>">
  		<INPUT type="hidden" name="SAVE_RED" value="<%=sSAVE_RED%>">
  		<INPUT type="hidden" name="repid" value="">
  		<INPUT type="hidden" name="ispay" value="<%=ispay%>">
  		<INPUT type="hidden" name="ValidNumber" value="<%=ValidNumber%>">
  		<% if(pcountry.equals("Singapore")) { %> 
  		<INPUT type="hidden" id="BASIC_SALARY" name="BASIC_SALARY" value=" Deduct CPF Contribution">
  		<%} else{%>
  		<INPUT type="hidden" id="BASIC_SALARY" name="BASIC_SALARY" value=" Deduct PF Contribution">
  		<%}%> 
      </div>
    
    </div>

    <div class="form-group">
      <label class="control-label col-form-label col-sm-4 required" for="Employee Name">Employee Name</label>
      <div class="col-sm-8">                
        <INPUT  class="form-control" name="CUST_NAME" type="TEXT" autocomplete="off" value="<%=sCustName%>"
			size="50" MAXLENGTH=100>
      </div>
    </div>
    <%if(ispay.equalsIgnoreCase("0")){ %>
    	<div class="form-group">
	      <label class="control-label col-form-label col-sm-4" for="Employee Login ID">Employee Login ID</label>
	      <div class="col-sm-8"> 
	      	               
	        <INPUT  class="form-control loginuserid" name="EMP_LOGIN_ID" autocomplete="off" type="TEXT" size="50" MAXLENGTH=100 >
	      </div>
	    </div>
	    
	    <div class="form-group">
	      <label class="control-label col-form-label col-sm-4" for="PASSWORD">Password</label>
	      <div class="col-sm-8">                
	        <INPUT  class="form-control" id="password-field" name="PASSWORD" type="password" autocomplete="off" size="50" MAXLENGTH=100>
	        <span class="input-group-btn phideshow">
            <button class="btn btn-default reveal" type="button" style="background: white;"><i class="fa fa-fw fa-eye" id="peye" style="color: #23527c;"></i></button>
          </span>  
	      </div>
	    </div>
    <%}else{ %>
	    <div class="form-group">
	      <label class="control-label col-form-label col-sm-4 required" for="Employee Login ID">Employee Login ID</label>
	      <div class="col-sm-8">                
	        <INPUT  class="form-control loginuserid" id="EMP_LOGIN_ID" name="EMP_LOGIN_ID" autocomplete="off" type="TEXT" value="" size="50" MAXLENGTH=100 >
	        
	      </div>
	    </div>
	    
	    <div class="form-group">
	      <label class="control-label col-form-label col-sm-4 required" for="PASSWORD">Password</label>
	      <div class="col-sm-8">                
	        <INPUT  class="form-control" id="password-field" name="PASSWORD" type="password" autocomplete="off" value="" size="50" MAXLENGTH=100>
	         <span class="input-group-btn phideshow">
            <button class="btn btn-default reveal" type="button" style="background: white;"><i class="fa fa-fw fa-eye" id="peye" style="color: #23527c;"></i></button>
          </span>  
	      </div>
	    </div>
    <%} %>
    <div class="form-group">
      <label class="control-label col-form-label col-sm-4" for="Employee Type">Employee Type</label>
      <div class="col-sm-8">                
			<input type="hidden" name="employeetypeid" value="">
			<input type="text" class="form-control emptype" name="employeetype" placeholder="Select Employee Type">				
      </div>
    </div>
    
     <!-- <div class="form-group">
      <label class="control-label col-form-label col-sm-2" for="Working man hours">Working man hours</label>
      <div class="col-sm-4">                
        <INPUT  class="form-control WORK_MAN_HOURS" name="WORK_MAN_HOURS" type="TEXT" value="0.0" size="50" MAXLENGTH=100>
      </div>
    </div> -->
    
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
        <input name="DOB" type="TEXT" value="<%=sDOB%>"	size="50" MAXLENGTH=10 class="form-control" autocomplete="off" readonly id="DOP">
      </div>
    </div>
    
    <div class="form-group">
      <label class="control-label col-form-label col-sm-4" for="Employee Phone">Employee Phone</label>
      <div class="col-sm-8">                
        <INPUT name="TELNO" type="TEXT" value="<%=sTelNo%>" size="50" class="form-control" autocomplete="off" onkeypress="return validateInput(event)"
			MAXLENGTH="30">
      </div>
    </div>
   <%--   <%if(ispay.equalsIgnoreCase("0")){ %> --%>
    <div class="form-group">
      <label class="control-label col-form-label col-sm-4" for="Employee Email">Employee Email</label>
      <div class="col-sm-8">                
        <INPUT name="EMAIL" type="TEXT" value="<%=sEmail%>" size="50" MAXLENGTH="50" autocomplete="off" class="form-control">
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
        <input name="PASSPORTEXPIRYDATE" type="TEXT" value="<%=sPASSPORTEXPIRYDATE%>"	size="50" MAXLENGTH=10 class="form-control datepicker" readonly>
      </div>
    </div>
    <%if(ispay.equalsIgnoreCase("0")){ %>
    <div class="form-group employee-section">
					<label class="control-label col-form-label col-sm-4">Employee Reporting</label>
					<div class="col-sm-8 ac-box">
						<input type="text" class="ac-selected form-control typeahead"
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
						<input type="text" class="ac-selected form-control typeahead"
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
    
    <!-- <div class="form-group">
      <label class="control-label col-form-label col-sm-2" for="Upload Employee Image">Upload Employee Image</label>
      <div class="col-sm-4">                
        <INPUT class="form-control" name="IMAGE_UPLOAD" type="File" size="20" MAXLENGTH=100>
      </div>
    </div> -->
</div>
<div class="col-sm-6">
	    	<div class="col-sm-12" style="padding-bottom: 5%;">
				<div class="col-sm-12"  style="text-align: left;">
						<label>Employee Image:</label>
				</div>
				<div class="col-sm-5" style="border: 1px dashed #ddd;border-radius: 4px;padding: 10px;text-align: center;height:75px">
						<div id="item_logo" hidden>
							<img id="item_img_logo1" src="" style="width: 100px; height: 50px"/>
						</div>
						<div  id="item_btn_logo">
							<label for="empfile" style="color: #337ab7;padding-top: 15px;">Upload Employee Image</label>
							<input accept="image/gif,image/jpeg,image/png,image/bmp" name="IMAGE_UPLOAD"  type="File" size="20" id="empfile" onchange="readURLLogo(this);">
					  	</div>
				</div>
				<div class="col-sm-7">
					<!-- <p style="font-size: 10px;text-align: left;">This logo will shows on Purchase, Sales Estimate, Sales order, Invoice, Bill, Payslip etc.</p> -->
					<div id="logoremove" style="text-align: left;" hidden>
						<a href="#" onClick="image_delete_new();">Remove Employee Image</a>	
					</div>
				</div>
			</div>
			<div class="col-sm-12" style="padding-bottom: 5%;">
      			
      			<label class ="checkbox-inline">
	            <input type = "checkbox" class="form-check-input" id = "ISCREATEONUSERINFO" name = "ISCREATEONUSERINFO" value = "ISCREATEONUSERINFO" Onclick="setautoemp(this)"
	            <%if(ISCREATEONUSERINFO.equals("1")) {%>checked <%}%> >Create Employee Login On User Details</label>
			</div>
			<div class="col-sm-12" style="padding-bottom: 5%;">
      			<INPUT type="hidden" name="isautoemail" value="1">
		    	<lable class="checkbox-inline"><input type="checkbox" class="form-check-input" id="autoemail" name="autoemail" checked Onclick="setautoemail(this)">Send Auto Email (General Payroll)</lable>
			</div>
			<div class="col-sm-12" style="padding-bottom: 5%;">
      			<INPUT type="hidden" name="ISPOSCUSTOMER" value="0">
		    	<lable class="checkbox-inline"><input type="checkbox" class="form-check-input" id="POSCUSTOMER" name="POSCUSTOMER"  Onclick="setposcustomer(this)">Allow to Create POS Customer</lable>
			</div>
			<div class="col-sm-12" style="padding-bottom: 5%;">
      			<INPUT type="hidden" name="ISEDITPOSPRODUCTPRICE" value="0">
		    	<lable class="checkbox-inline"><input type="checkbox" class="form-check-input" id="EDITPOSPRODUCTPRICE" name="EDITPOSPRODUCTPRICE"  Onclick="setproductprice(this)">Allow to Edit POS Product Prices</lable>
			</div>
</div>
 <div class="col-sm-12">   
    <div class="bs-example">
     <ul class="nav nav-tabs" id="myTab" style="font-size: 94%;"> 
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
    	
        </div>
        
        <div class="tab-pane fade" id="identity">
        <br>
        
       
        <div class="form-group">
         <% if(pcountry.equals("Singapore")) { %>
      	<label class="control-label col-form-label col-sm-2" for="EmiratesId NO"> NRIC/FIN</label>
      	<div class="col-sm-4">  
        <INPUT name="EMIRATESID" type="TEXT" value="<%=sEMIRATESID%>"	size="50" MAXLENGTH=50 class="form-control">
      	</div>
         <%} else{%> 
      	<label class="control-label col-form-label col-sm-2" for="EmiratesId NO"><%=pcountry%> ID Number</label>
      	<div class="col-sm-4">  
        <INPUT name="EMIRATESID" type="TEXT" value="<%=sEMIRATESID%>"	size="50" MAXLENGTH=50 class="form-control">
      	</div>
      	<%}%> 
    	</div>
        
    	
    	 
    	<div class="form-group">
    	<% if(pcountry.equals("Singapore")) { %>
      <label class="control-label col-form-label col-sm-2" for="Emirates ExpiryDate">NRIC/FIN Expiry Date</label>
       <div class="col-sm-4">                
        <input name="EMIRATESIDEXPIRY" type="TEXT" value="<%=sEMIRATESIDEXPIRY%>"	size="50" MAXLENGTH=10 class="form-control datepicker" readonly>
      </div>
       <%} else{%> 
      <label class="control-label col-form-label col-sm-2" for="Emirates ExpiryDate"><%=pcountry%> ExpiryDate</label>
      <div class="col-sm-4">                
        <input name="EMIRATESIDEXPIRY" type="TEXT" value="<%=sEMIRATESIDEXPIRY%>"	size="50" MAXLENGTH=10 class="form-control datepicker" readonly>
      </div>
      <%}%> 
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
        <input name="VISAEXPIRYDATE" type="TEXT" value="<%=sVISAEXPIRYDATE%>"	size="50" MAXLENGTH=10 class="form-control datepicker" readonly>
      </div>
       </div>
        
        </div>        
        
        <div class="tab-pane fade" id="employment">
        <br>
        
        <div class="form-group">
        <input type="hidden" name="departmentid" value="0">
      	<label class="control-label col-form-label col-sm-2" for="Department">Department</label>
      	<%-- <div class="col-sm-4">  
        <INPUT name="DEPT" type="TEXT" value="<%=sDept%>"	size="50" MAXLENGTH=100 class="form-control"> --%>
             <div class="col-sm-4"> 
      		<input type="text" class="ac-selected form-control typeahead"
							id="DEPT" name="DEPT" placeholder="Department">
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
      	<INPUT type="hidden" name="ISCASHIER" value="0">
		<lable class="checkbox-inline"><input type="checkbox" class="form-check-input" id="CASHIER" name="CASHIER"  Onclick="setcashier(this)">Cashier</lable>
		</div>
      	<INPUT type="hidden" name="ISSALESMAN" value="0">
		<lable class="checkbox-inline"><input type="checkbox" class="form-check-input" id="SALESMAN" name="SALESMAN"  Onclick="setsalesman(this)">Sales Person</lable>
    	</div>
    	
    	<div class="form-group">
      <label class="control-label col-form-label col-sm-2" for="Date of Joining">Date of Joining</label>
      <div class="col-sm-4">                
        <input name="DATEOFJOINING" type="TEXT" value="<%=sDATEOFJOINING%>"	size="50" MAXLENGTH=10 class="form-control datepicker" readonly>
      </div>
       </div>
       
       <div class="form-group">
      <label class="control-label col-form-label col-sm-2" for="Date of Leaving">Date of Leaving</label>
      <div class="col-sm-4">                
        <input name="DATEOFLEAVING" type="TEXT" value="<%=sDATEOFLEAVING%>"	size="50" MAXLENGTH=10 class="form-control datepicker" readonly>
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
        <input name="CONTRACTSTARTDATE" type="TEXT" value="<%=sCONTRACTSTARTDATE%>"	size="50" MAXLENGTH=10 class="form-control datepicker" readonly>
      </div>
       </div>
       
       <div class="form-group">
      <label class="control-label col-form-label col-sm-2" for="Contract ExpiryDate">Contract ExpiryDate</label>
      <div class="col-sm-4">                
        <input name="CONTRACTENDDATE" type="TEXT" value="<%=sCONTRACTENDDATE%>"	size="50" MAXLENGTH=10 class="form-control datepicker" readonly>
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
			<div class="col-sm-4 ac-box" >				
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
							id="BANKNAME" name="BANKNAME" placeholder="Bank">
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
					</table>
			</div>
			<div class="form-group">
					<div class="row">
						<div class="col-sm-6">
							<a class="add-line"
								style="text-decoration: none; cursor: pointer;margin-left: 5%;"
								onclick="addRow()">+ Add another Leave Detail</a>
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
						<tbody>
							<tr>
								<td class="text-center">
									<input type="hidden" name="empSalaryid" value="0">									
									<input class="form-control text-left empSalary" name="empSalary" type="text" placeholder="Enter Salary Type" maxlength="100"></td>
									<td class="text-center">
									  <input type="hidden" name="PAYROLL_BY_BASIC_SALARY" value = "0">
									  <input type="checkbox" name="ISPAYROLL_BY_BASIC_SALARY" id="ISPAYROLL_BY_BASIC_SALARY" value="1" onclick="payrollbybasicsalary(this)" disabled />&nbsp;&nbsp;
								</td>
								<td class="text-center">
									<input class="form-control text-left" type="text" name="empSalaryAmt" placeholder="Enter Amount" value="<%=new java.math.BigDecimal(sSalary).toPlainString()%>" onkeypress="return isNumberKey(event,this,4)">
								</td>
							</tr>
						</tbody>
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
        
        
        <!-- <div class="form-group">
      	<label class="control-label col-form-label col-sm-2" for="Working Man Days">Working Man Days</label>
      	<div class="col-sm-4">  
        <INPUT  class="form-control WORK_MAN_HOURS" name="WORK_MAN_HOURS" type="TEXT" value="0.0" size="50" MAXLENGTH=100>
      	</div>
    	</div> -->
        
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
								<input type="file" class="form-control input-attch"
									id="empAttch" name="file" multiple="true">
								<div class="input-group">
									<svg version="1.1" id="Layer_1"
										xmlns="http://www.w3.org/2000/svg" x="0" y="0"
										viewBox="0 0 512 512" xml:space="preserve"
										class="icon icon-xs align-text-top action-icons input-group-addon"
										style="height: 30px; display: inline-block; color: #c63616;">
										<path
											d="M262.4 512c-35.7 0-64.2-10.5-84.9-31.4-24.7-24.9-37.1-63.8-36.8-115.6.2-32.6-.3-248.7-1.4-268.1-.9-15.9 4.8-41.6 23.6-61.5 11.4-12 32-26.3 66-26.3 30.3 0 55.1 15.7 69.8 44.2 10.1 19.7 12.2 38.8 12.4 40.9l.1.9v.9l.5 236.9v.5c-1 19.2-15.9 47.6-53 47.6h-.7c-39.1-.4-53.7-30.4-56-46.2l-.2-1.3V179.6c0-10.2 8.3-18.5 18.5-18.5s18.5 8.3 18.5 18.5v150.5c.4 1.5 1.4 4.4 3.6 7.2s6.2 6.5 16 6.6c9.2.1 12.4-3.2 14.1-6 1.6-2.6 2.2-5.6 2.3-6.3l-.7-234.5c-.4-3-2.4-15.6-8.8-27.6-8.3-15.7-20.2-23.3-36.4-23.3-16.7 0-29.8 5-39.1 14.8-10.7 11.3-14 26.6-13.6 34 1.2 21.6 1.6 244.3 1.4 270.3-.2 41.6 8.5 71.7 26 89.3 13.5 13.6 33.2 20.4 58.7 20.4 17.2 0 31.8-5.9 43.5-17.7 18.9-18.9 30.1-53.4 30-92.2 0-19.6-.1-193.2-.1-250.9 0-10.2 8.3-18.5 18.5-18.5s18.5 8.3 18.5 18.5c0 57.6.1 231.2.1 250.8.1 49.1-14.8 92.3-40.8 118.4-18.6 18.7-42.7 28.6-69.6 28.6z"></path></svg>
									<button type="button" name="IMAGE_UPLOAD" class="btn btn-sm btn-attch">Upload File</button>
								</div>

							</div>
						</div>
						<div id="empAttchNote">
							<small class="text-muted"> You can upload a maximum of 5
								files, 2MB each </small>
						</div>
        </div>
        
        </div>
        
        </div>
    </div>
    <div class="form-group">        
     <div class="col-sm-offset-4 col-sm-8">
     <!--<button type="button" class="Submit btn btn-default" onClick="window.location.href='../home'"><b>Back</b></button>&nbsp;&nbsp;
       <button type="button" class="Submit btn btn-default" onClick="{backNavigation('settings.jsp','SA');}"><b>Back</b></button>&nbsp;&nbsp; -->
      	<button type="button" class="Submit btn btn-default" onClick="onNew();"<%=sNewEnb%>>Clear</button>&nbsp;&nbsp;
      	<button type="button" class="btn btn-success" data-toggle="modal" data-target="#myModal" onClick="onAdd();"<%=sAddEnb%>>Save</button>&nbsp;&nbsp;
      	
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
    if(document.form.SAVE_RED.value!="")
	{
	document.form.action  = "../payroll/employee?PGaction=View&result="+document.form.SAVE_RED.value;
	//document.form.action  = "../payroll/employee?PGaction=View&result=Employee Added Successfully";
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
						ACTION : "GET_EMPLOYEE_DATA",
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
	    			}
	    			
	    		}
	    	});
	    
	    $(document).on("focusout",".loginuserid", function(){
	    	var userid = $(this).val();
	    	$.ajax({
	    		type : "GET",
	    		url: '/track/HrLeaveTypeServlet',
	    		async : true,
	    		dataType: 'json',
	    		data : {
	    			CMD : "CHECK_USERID",
	    			username : userid,
	    		},
	    		success : function(data) {
	    			if(data.STATUS == "NOT OK"){
	    				$('input[name = "EMP_LOGIN_ID"]').val("");
	    				alert("Employee Login ID already exist");
	    			}
	    		}
	    	});	
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
    			/* return '<div><p class="item-suggestion">'+data.DEPARTMENT+'</p></div>'; */
    			return '<div onclick="setdepartmentid(this,\''+data.ID+'\')"><p>' + data.DEPARTMENT + '</p>';
    			}
    		  }
    		})		.on('typeahead:render',function(event,selection){
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


function addSalaryRow(){

	var body="";
	body += '<tr>';
	body += '<td class="text-center">';
	body += '<input type="hidden" name="empSalaryid" value="0">';	
	body += '<input class="form-control text-left empSalary" name="empSalary" type="text" placeholder="Enter Salary Type" maxlength="50">';
	body += '</td>';
	body += '<td class="text-center grey-bg" style="position:relative;">'; 
	body += '<input type="hidden" name="PAYROLL_BY_BASIC_SALARY" value="0">';
	body += '<input type="Checkbox" style="border:0;background=#dddddd"	name="ISPAYROLL_BY_BASIC_SALARY" value="1" onclick="payrollbybasicsalary(this)" disabled>';
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

function addRow() {

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
					   $('#STATE').append('<option value="' + value.text + '">' + value.text + '</option>');
					});
		}
	});	
	
}
$('select[name="COUNTRY_CODE"]').on('change', function(){
    var text = $("#COUNTRY_CODE option:selected").text();
    $("input[name ='COUNTRY']").val(text.trim());
});

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
        $("#DOP").datepicker({dateFormat: 'dd/mm/yy',changeMonth: true, changeYear: true, yearRange: '-100:+0'});

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
//       		return '<div onclick="document.form.OUTLET_NAME.value = \''+data.OUTLET_NAME+'\'"><p class="item-suggestion">Name: ' + data.OUTLET_NAME + '</p><br/><p class="item-suggestion">Code: ' + data.OUTLET + '</p></div>';
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
    
    function payrollbybasicsalary(obj){
    	var manageapp = $(obj).closest('tr').find('input[name=PAYROLL_BY_BASIC_SALARY]').val();
    	if(manageapp == 0)
    	$(obj).closest('tr').find('input[name=PAYROLL_BY_BASIC_SALARY]').val(1);
    	else
    	$(obj).closest('tr').find('input[name=PAYROLL_BY_BASIC_SALARY]').val(0);
    	
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
        }
    }

function image_delete_new(){
    	$('#empfile').val("");
    	$('#item_logo').hide();
        $('#item_btn_logo').show();
        $('#logoremove').hide();
      }	
      
$(".toggle-password").click(function() {

	  $(this).toggleClass("fa-eye fa-eye-slash");
	  var input = $($(this).attr("toggle"));
	  if (input.attr("type") == "password") {
	    input.attr("type", "text");
	  } else {
	    input.attr("type", "password");
	  }
	});
	
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
   