<%@ page import="com.track.util.*"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.gates.*"%>
<%@ page import="com.track.constants.*"%>
<%@ page import="com.track.dao.CustMstDAO"%>
<%@ page import="com.track.dao.CustomerBeanDAO"%>
<%@ page import="com.track.dao.MovHisDAO"%>
<%@ page import="com.track.dao.TblControlDAO"%>
<%@ page import="com.track.dao.DoHdrDAO"%>
<%@ page import="com.track.dao.DoDetDAO"%>
<%@ page import="com.track.dao.ShipHisDAO"%>
<%@ page import="com.track.dao.ShipHisDAO"%>
<%@ page import="com.track.dao.EstHdrDAO"%>
<%@ page import="com.track.dao.PlantMstDAO"%>
<%@ page import="com.track.dao.*"%>
<%@ include file="header.jsp"%>

<%
String title = "Edit Customer";
%>
<%@include file="sessionCheck.jsp" %>
<jsp:useBean id="eb"  class="com.track.gates.encryptBean" />
<jsp:include page="header2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
	<jsp:param name="mainmenu" value="<%=IConstants.SETTINGS%>"/>
	<jsp:param name="submenu" value="<%=IConstants.SYSTEM_ADMIN%>"/>
</jsp:include>
<script src="../jsp/js/typeahead.jquery.js"></script>
<link rel="stylesheet" href="../jsp/css/typeahead.css">
<link rel="stylesheet" href="../jsp/css/accounting.css">
<!-- <link rel="stylesheet" href="../jsp/css/bootstrap-datepicker.css"> -->
<!-- <script src="../jsp/js/bootstrap-datepicker.min.js"></script> -->
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<script type="text/javascript" src="../jsp/js/calendar.js"></script>
<script language="JavaScript" type="text/javascript" src="../jsp/js/general.js"></script>
<style>
.user-action {
	cursor: pointer;
	font-size: 15px;
	opacity: 0.4;
	position: absolute;
	right: -25%;
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
 subWin = window.open(encodeURI(URL), 'CUSTOMERS', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=800,height=400,left = 200,top = 184');
}
function onNew(){
// 	document.form.CUST_CODE.value = ""; 
	document.form.CUST_NAME.value = "";
	document.form.companyregnumber.value = ""; 
	document.form.CURRENCY.value = ""; 
	document.form.CUSTOMER_TYPE_ID.value = "";
	document.form.CUSTOMER_TYPE_DESC.value = "";
	document.form.transport.value = "";
	document.form.TELNO.value = ""; 
	document.form.FAX.value = "";
	document.form.CUSTOMEREMAIL.value = ""; 
	document.form.WEBSITE.value = "";
	document.form.ADDR1.value = "";
	document.form.ADDR2.value = "";
	document.form.ADDR3.value = "";
	document.form.ADDR4.value = "";
	document.form.ZIP.value = "";
	document.form.DESGINATION.value = "";
	document.form.CONTACTNAME.value = "";
	document.form.WORKPHONE.value = "";
	document.form.HPNO.value = "";
	document.form.EMAIL.value = "";
	document.form.FACEBOOK.value = "";
	document.form.TWITTER.value = "";
	document.form.LINKEDIN.value = "";
	document.form.SKYPE.value = "";
	document.form.IBAN.value = "";
	document.form.BANKROUTINGCODE.value = "";
	document.form.REMARKS.value = "";
	document.form.CREDITLIMIT.value = "";
	document.form.RCBNO.value = "";
	document.form.PEPPOL_ID.value = "";
	document.form.CUST_ADDONCOST.value = "";
	document.form.PEPPOL.checked = false;
	document.form.ISSHOWBYPRODUCT.checked = false;
	document.form.ISSAMEASCONTACTADD.checked = false;
  /*  document.form.TAXTREATMENT.selectedIndex=0;
   document.form.action  = "maint_customer.jsp?action=NEW";
   document.form.submit(); */
}

function isNumberKey(evt, element, id) {
	  var charCode = (evt.which) ? evt.which : event.keyCode;
	  if (charCode > 31 && (charCode < 48 || charCode > 57) && !(charCode == 46 || charCode == 8 || charCode == 45))
		  {
	    	return false;
		  }
	  
	  		/* else {
	    var len = $(element).val().length;
	    var index = $(element).val().indexOf('.');
	    if (index > 0 && charCode == 46) {
	      return false;
	    }
	    if (index > 0) {
	      var CharAfterdot = (len + 1) - index;
	      if (CharAfterdot > id) {
	        return false;
	      }
	    }

	  } */
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
function onUpdate(){
   var CUST_CODE   = document.form.CUST_CODE.value;
   var CUST_NAME   = document.form.CUST_NAME.value;
   var PEPPOL   = document.form.PEPPOL.value;
   var PEPPOL_ID   = document.form.PEPPOL_ID.value;
   var CURRENCY   = document.form.CURRENCY.value;
//   var companyregnumber   = document.form.companyregnumber.value;
   var region = document.form.COUNTRY_REG.value;
   var CL   = document.form.CREDITLIMIT.value;
   var TAXTREATMENT   = document.form.TAXTREATMENT.value;
   var RCBNO   = document.form.RCBNO.value;
   var rcbn = RCBNO.length;
   
   var companyindus   = document.form.EDUCATION.value;
   if(companyindus == "Education") {
   var dob   = document.form.DOB.value;
   var nationality   = document.form.NATIONALITY.value;
   }
   
   if(CUST_CODE == "" || CUST_CODE == null) {alert("Please Enter Customer ID"); return false; }
   if(CUST_NAME == "" || CUST_NAME == null) {
	   alert("Please Enter Customer Name"); 
	   document.form.CUST_NAME.focus();
	   return false; 
	   }

 

//thanzi
if(document.getElementById("PEPPOL").checked == true)
	PEPPOL="1";
else 
	PEPPOL="0";
   if(PEPPOL_ID == "" &&  PEPPOL == "1") {
 	  alert("Please Enter Peppol Id"); 
 	return false; 
}
   if(companyindus == "Education") {
		   if(dob == "" || dob == null) {
			   alert("Please Enter Date of Birth");document.form.DOB.focus(); 
			   return false; 
			   }
		   if(nationality == "" || nationality == null) {
			   alert("Please Select Nationality");document.form.NATIONALITY.focus(); 
			   return false; 
			   }
	    }

 /*   if(region == "GCC"){
	   document.form.companyregnumber.value="";
	}else if(region == "ASIA PACIFIC"){
		if (companyregnumber == "" || companyregnumber == null) {
		alert("Please Enter Unique Entity Number (UEN)");
		document.form.companyregnumber.focus();
		return false; 
		}
	} */


   if(CURRENCY == "" || CURRENCY == null) {
	   alert("Please Enter Currency ID"); 
	   document.form.CURRENCY.focus();
	   return false; 
	   }

   
   if(form.TAXTREATMENT.selectedIndex==0)
	{
alert("Please Select TAXTREATMENT");
form.TAXTREATMENT.focus();
return false;
	}
   if(TAXTREATMENT=="VAT Registered"||TAXTREATMENT=="VAT Registered - Desginated Zone")
   {
	   var  d = document.getElementById("TaxByLabel").value;
   	if(RCBNO == "" || RCBNO == null) {
   		
	   alert("Please Enter "+d+" No."); 
	   document.form.RCBNO.focus();
	   return false; 
	   }
   	//if(document.form.COUNTRY_REG.value=="GCC")// region based validtion
		//{
		if(!IsNumeric(RCBNO))
		{
	    alert(" Please Enter "+d+" No. Input In Number"); 
	   	document.form.RCBNO.focus();
	   	return false; 
	  	}
	
		if("15"!=rcbn)
		{
    	alert(" Please Enter your 15 digit numeric "+d+" No."); 
   		document.form.RCBNO.focus();
   		return false; 
  		}
		//}
   }
   else if(50<rcbn)
   {
	   var  d = document.getElementById("TaxByLabel").value;
       alert(" "+d+" No. length has exceeded the maximum value"); 
	   document.form.RCBNO.focus();
	   return false; 
     }
   if(CL < 0) 
   {
	   alert("Credit limit cannot be less than zero");
	   document.form.CREDITLIMIT.focus(); 
	   return false; 
	   }
   /*if(this.form.ISCREDITLIMIT.checked == true && CL == ""){
	   alert("Please Enter Credit Limit");
	   document.form.CREDITLIMIT.focus();
	   return false; 
   }*/
   
   if(!IsNumeric(CL))
   {
     alert(" Please Enter Credit Limit Input In Number");
     form.CREDITLIMIT.focus();  
     form.CREDITLIMIT.select(); 
     return false;
   }
   if(!IsNumeric(form.PMENT_DAYS.value))
   {
     alert(" Please Enter Days In Number");
     form.PMENT_DAYS.focus();  form.PMENT_DAYS.select(); return false;
   }
   
   if(form.COUNTRY_CODE.selectedIndex==0)
	{
	 alert("Please Select Country from Address");
	 form.COUNTRY_CODE.focus();
	 return false;
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
    
   var chk = confirm("Are you sure you would like to save?");
	if(chk){
	add_attachments();
   
	}
	else{
		return false;}	   
}
function onDelete(){
	   var CUST_CODE   = document.form.CUST_CODE.value;
	   if(CUST_CODE == "" || CUST_CODE == null) {alert("Please Enter Customer ID");document.form.CUST_CODE.focus(); return false; }
	var chkmsg=confirm("Are you sure you would like to delete? ");
	    if(chkmsg){
	   document.form.action  = "../jsp/maint_customer.jsp?action=DELETE";
	   document.form.submit();}
	   else
	   { return false;
	   }
	}

function onView(){
   var CUST_CODE   = document.form.CUST_CODE.value;
   if(CUST_CODE == "" || CUST_CODE == null) {alert("Please Enter Customer ID"); return false; }

   document.form.action  = "../jsp/maint_customer.jsp?action=VIEW";
   document.form.submit();
}

</SCRIPT>
<jsp:useBean id="ub"  class="com.track.gates.userBean" />
<%
String sUserId = (String) session.getAttribute("LOGIN_USER");
String curency = StrUtils.fString((String) session.getAttribute("BASE_CURRENCY"));
String res        = "";

String sNewEnb    = "enabled";
String sDeleteEnb = "enabled";
String sAddEnb    = "enabled";
String sUpdateEnb = "enabled";
String sCustEnb   = "enabled";
String action     = "";
String sCustCode  = "",
       sCustName  = "",
       sOldCustName  = "",
       APP_SHOWBY_PRODUCT="",
       APP_SHOWBY_CATEGORY="",
       SHOWSALESBYPURCHASECOST="",
    		   ADDONCOSTTYPE="",
    		   PEPPOL="",
    		   PEPPOL_ID="",
       companyregnumber="",
       sCustNameL  = "",
       sAddr1     = "",
       sAddr2     = "",
       sAddr3     = "", sAddr4     = "",
       sState   = "",
       sCountry   = "",
       sZip       = "",
       sCons      = "Y";
String sContactName ="", sDesgination="",sTelNo="",sHpNo="",sFax="",sDob="",sNational="",sEmail="",sRemarks="",sPayTerms="",sPayMentTerms="",sPayInDays="",isActive="",
		customertypeid="",customertype_id="",desc="",customer_status_id="",customerstatusid="",transport="",statusdesc="",sRcbno="",CREDITLIMIT="",ISCREDITLIMIT="",CUST_ADDONCOST="";
String CUSTOMEREMAIL="",WEBSITE="",FACEBOOK="",TWITTER="",LINKEDIN="",SKYPE="",OPENINGBALANCE="",WORKPHONE="",sTAXTREATMENT="",CREDIT_LIMIT_BY="",sCountryCode="",shipcountrycode="";
String sIBAN="",sBANKNAME="",sBANKROUTINGCODE="",sBRANCH="",CURRENCY="",sSAVE_RED="",transportmode="",sSAVE_REDELETE;
String shipContactName="",shipDesgination="",shipWORKPHONE="",shipHpNo="",shipEmail="",shipCountry="",shipAddr1="",shipAddr2="",shipAddr3="",shipAddr4="",shipState="",shipZip="",SameAsContactAddress="";
StrUtils strUtils = new StrUtils();
CustUtil custUtil = new CustUtil();
CustomerBeanDAO customerBeanDAO = new CustomerBeanDAO();
custUtil.setmLogger(mLogger);
PlantMstDAO _PlantMstDAO = new PlantMstDAO();
DoHdrDAO DoHdrDAO = new  DoHdrDAO();
DoDetDAO DoDetDAO = new  DoDetDAO();
ShipHisDAO ShipHisDAO = new ShipHisDAO();
EstHdrDAO EstHdrDAO = new EstHdrDAO();
CustomerAttachDAO customerAttachDAO = new CustomerAttachDAO();
PlantMstDAO plantMstDAO = new PlantMstDAO();
CurrencyDAO currencyDAO = new CurrencyDAO();

List customerattachlist= new ArrayList();
action            = strUtils.fString(request.getParameter("action"));
String plant = strUtils.fString((String)session.getAttribute("PLANT"));
String region = strUtils.fString((String) session.getAttribute("REGION"));
String COMP_INDUSTRY = plantMstDAO.getCOMP_INDUSTRY(plant);//Check Company Industry
String ISPEPPOL = plantMstDAO.getisPeppol(plant);//Check Company Industry
String taxbylabel= ub.getTaxByLable(plant);
String numberOfDecimal = _PlantMstDAO.getNumberOfDecimal(plant);
sCustCode  = strUtils.fString(request.getParameter("CUST_CODE"));
companyregnumber= strUtils.fString(request.getParameter("companyregnumber"));
DateUtils dateutils = new DateUtils();
if(sCustCode.length() <= 0) sCustCode  = strUtils.fString(request.getParameter("CUST_CODE1"));
sCustName  = strUtils.InsertQuotes(strUtils.fString(request.getParameter("CUST_NAME")));
sOldCustName  = strUtils.InsertQuotes(strUtils.fString(request.getParameter("OLD_CUST_NAME")));
CURRENCY = strUtils.fString(request.getParameter("CURRENCYID"));
sCustNameL  = strUtils.InsertQuotes(strUtils.fString(request.getParameter("L_CUST_NAME")));
sAddr1     = strUtils.fString(request.getParameter("ADDR1"));
sAddr2     = strUtils.fString(request.getParameter("ADDR2"));
sAddr3     = strUtils.fString(request.getParameter("ADDR3"));
sAddr4     = strUtils.fString(request.getParameter("ADDR4"));
sState   = strUtils.fString(request.getParameter("STATE"));
sCountry   = strUtils.fString(request.getParameter("COUNTRY"));
transport=strUtils.fString(request.getParameter("TRANSPORTID"));

shipZip = strUtils.fString(request.getParameter("SHIP_ZIP"));
shipState = strUtils.fString(request.getParameter("SHIP_STATE"));
shipAddr4 = strUtils.fString(request.getParameter("SHIP_ADDR4"));
shipAddr3 = strUtils.fString(request.getParameter("SHIP_ADDR3"));
shipAddr2 = strUtils.fString(request.getParameter("SHIP_ADDR2"));
shipAddr1 = strUtils.fString(request.getParameter("SHIP_ADDR1"));
shipCountry = strUtils.fString(request.getParameter("SHIP_COUNTRY"));
shipEmail = strUtils.fString(request.getParameter("SHIP_EMAIL"));
shipHpNo = strUtils.fString(request.getParameter("SHIP_HPNO"));
shipWORKPHONE = strUtils.fString(request.getParameter("SHIP_WORKPHONE"));
shipDesgination = strUtils.fString(request.getParameter("SHIP_DESGINATION"));
shipContactName  = strUtils.fString(request.getParameter("SHIP_CONTACTNAME"));
SameAsContactAddress=StrUtils.fString((request.getParameter("SameAsContactAddress") != null) ? "1": "0").trim();

sZip       = strUtils.fString(request.getParameter("ZIP"));
sCons      = strUtils.fString(request.getParameter("CONSIGNMENT"));
sContactName      = strUtils.InsertQuotes(strUtils.fString(request.getParameter("CONTACTNAME")));
sDesgination  = strUtils.fString(request.getParameter("DESGINATION"));
sTelNo  = strUtils.fString(request.getParameter("TELNO"));
sHpNo  = strUtils.fString(request.getParameter("HPNO"));
sFax  = strUtils.fString(request.getParameter("FAX"));
sDob  = strUtils.fString(request.getParameter("DOB"));
sNational  = strUtils.fString(request.getParameter("NATIONALITY"));
sEmail= strUtils.fString(request.getParameter("EMAIL"));
sRemarks= strUtils.fString(request.getParameter("REMARKS"));
sRcbno = strUtils.fString(request.getParameter("RCBNO"));
sPayTerms=strUtils.InsertQuotes(strUtils.fString(request.getParameter("PAYTERMS")));
sPayMentTerms=strUtils.InsertQuotes(strUtils.fString(request.getParameter("payment_terms")));
sPayInDays=strUtils.InsertQuotes(strUtils.fString(request.getParameter("PMENT_DAYS")));
isActive= strUtils.fString(request.getParameter("ACTIVE"));
customertypeid=strUtils.fString(request.getParameter("CUSTOMER_TYPE_ID"));
CREDITLIMIT=strUtils.fString(request.getParameter("CREDITLIMIT"));
ISCREDITLIMIT=StrUtils.fString((request.getParameter("ISCREDITLIMIT") != null) ? "1": "0").trim();
CUST_ADDONCOST=strUtils.fString(request.getParameter("CUST_ADDONCOST"));
APP_SHOWBY_PRODUCT=StrUtils.fString((request.getParameter("APP_SHOWBY_PRODUCT") != null) ? "1": "0").trim();
APP_SHOWBY_CATEGORY=StrUtils.fString((request.getParameter("APP_SHOWBY_CATEGORY") != null) ? "1": "0").trim();
// SHOWSALESBYPURCHASECOST=StrUtils.fString((request.getParameter("SHOWSALESBYPURCHASECOST") != null) ? "1": "0").trim();
SHOWSALESBYPURCHASECOST=strUtils.fString(request.getParameter("SHOWSALESBYPURCHASECOST"));
ADDONCOSTTYPE=strUtils.fString(request.getParameter("ADDONCOSTTYPE"));
PEPPOL=StrUtils.fString((request.getParameter("PEPPOL") != null) ? "1": "0").trim();
customer_status_id=strUtils.fString(request.getParameter("CUSTOMER_STATUS_ID"));
PEPPOL_ID=strUtils.fString(request.getParameter("PEPPOL_ID"));

CUSTOMEREMAIL=strUtils.fString(request.getParameter("CUSTOMEREMAIL"));
WEBSITE=strUtils.fString(request.getParameter("WEBSITE"));
FACEBOOK=strUtils.fString(request.getParameter("FACEBOOK"));
TWITTER=strUtils.fString(request.getParameter("TWITTER"));
LINKEDIN=strUtils.fString(request.getParameter("LINKEDIN"));
SKYPE=strUtils.fString(request.getParameter("SKYPE"));
OPENINGBALANCE=strUtils.fString(request.getParameter("OPENINGBALANCE"));
WORKPHONE=strUtils.fString(request.getParameter("WORKPHONE"));
sTAXTREATMENT=strUtils.fString(request.getParameter("TAXTREATMENT"));
//IMTHIYAS Modified on 02.03.2022
if(COMP_INDUSTRY.equals("Education")){
	CREDIT_LIMIT_BY = "NOLIMIT";
}else{
CREDIT_LIMIT_BY=strUtils.fString(request.getParameter("CREDIT_LIMIT_BY"));
}
sCountryCode=strUtils.fString(request.getParameter("EDIT_COUNTRY"));
shipcountrycode=strUtils.fString(request.getParameter("EDIT_SHIP_COUNTRY"));
sBANKNAME   = strUtils.InsertQuotes(strUtils.fString(request.getParameter("BANKNAME")));
sBRANCH   = strUtils.InsertQuotes(strUtils.fString(request.getParameter("BRANCH")));
sIBAN   = strUtils.InsertQuotes(strUtils.fString(request.getParameter("IBAN")));
sBANKROUTINGCODE       = strUtils.fString(request.getParameter("BANKROUTINGCODE"));
sSAVE_RED  = strUtils.fString(request.getParameter("SAVE_RED"));
sSAVE_REDELETE = strUtils.fString(request.getParameter("SAVE_REDELETE"));
float CREDITLIMITVALUE ="".equals(CREDITLIMIT) ? 0.0f :  Float.parseFloat(CREDITLIMIT);
float OPENINGBALANCEVALUE ="".equals(OPENINGBALANCE) ? 0.0f :  Float.parseFloat(OPENINGBALANCE);
float CUST_ADDONCOSTVALUE ="".equals(CUST_ADDONCOST) ? 0.0f :  Float.parseFloat(CUST_ADDONCOST);
List customerstatuslist=custUtil.getCustStatusList("",plant," AND ISACTIVE ='Y'");
MovHisDAO mdao = new MovHisDAO(plant);
  String currency=plantMstDAO.getBaseCurrency(plant);
   String DISPLAY="",DISPLAYID="";
   List curQryList=new ArrayList();
   curQryList = currencyDAO.getCurrencyDetails(currency,plant);
   for(int i =0; i<curQryList.size(); i++) {
		ArrayList arrCurrLine = (ArrayList)curQryList.get(i);
		DISPLAYID	= StrUtils.fString(StrUtils.removeQuotes((String)arrCurrLine.get(0)));
		DISPLAY	= StrUtils.fString(StrUtils.removeQuotes((String)arrCurrLine.get(2)));
   }

   List customeruserlist= new CustMstDAO().getCustomerUserDetails(sCustCode, plant, "");
   
if(action.equalsIgnoreCase("NEW")){
     
      sCustCode  = "";
      sCustName  = "";
      companyregnumber="";
      sCustNameL="";
      sAddr1     = "";
      sAddr2     = "";
      sAddr3     = ""; sAddr4     = "";
      sState   = "";
      sCountry   = "";
      transport="";
      sZip       = "";
      sCons      = "Y";
      CURRENCY="";
      sContactName =""; sDesgination="";sTelNo="";sHpNo="";sFax="";sDob="";sNational="";sEmail="";sRemarks="";sPayTerms="";sPayMentTerms="";sPayInDays="";sRcbno="";customertypeid="";CREDITLIMIT="";CUST_ADDONCOST="";
      CUSTOMEREMAIL="";WEBSITE="";FACEBOOK="";TWITTER="";LINKEDIN="";SKYPE="";PEPPOL="";PEPPOL_ID="";OPENINGBALANCE="";WORKPHONE="";
      shipContactName="";shipDesgination="";shipWORKPHONE="";shipHpNo="";shipEmail="";shipCountry="";shipAddr1="";shipAddr2="";shipAddr3="";shipAddr4="";shipState="";shipZip="";
      sIBAN="";sBANKNAME="";sBANKROUTINGCODE="";sBRANCH="";CREDITLIMITVALUE=0;OPENINGBALANCEVALUE=0;sCountryCode="";shipcountrycode="";
}
//3. >> Update
else if(action.equalsIgnoreCase("UPDATE"))  {

   if(custUtil.isExistCustomer(sCustCode,plant))
    {
	   String[] USER_NAME = request.getParameterValues("USER_NAME");
	   String[] USER_HPNO = request.getParameterValues("USER_HPNO");
	   String[] USER_EMAIL = request.getParameterValues("USER_EMAIL");
	   String[] MANAGER_APP = request.getParameterValues("MANAGER_APP_VAL");
		String[] customeruserid = request.getParameterValues("USER_ID");
		String[] PASSWORD= request.getParameterValues("PASSWORD");
	   
		boolean chkuserid=false;
		boolean removeuserid= new CustMstDAO().Deletecustomeruser(sCustCode,plant);
		String chkcustomeruserid="";
		if(customeruserid !=null) {
		for(int i =0 ; i < customeruserid.length ; i++){
		chkcustomeruserid =(String)customeruserid[i];
		if(!chkcustomeruserid.equalsIgnoreCase("")) {
		if(new CustMstDAO().isExistsCustomerUser(chkcustomeruserid,plant))
		{
			chkuserid=true;
			break;
		}
		}
		}					
		}
		if(!chkuserid) {
		
	   Hashtable ht = new Hashtable();
	   ht.put(IDBConstants.PLANT, plant);
	  if(!customerBeanDAO.isExisitCustomer(ht,
				" CUSTNO <> '"+ sCustCode + "'  AND CNAME = '" + sCustName + "'"))
	  {
		  
		  if(ISCREDITLIMIT.equals("1"))
		  {
			  ISCREDITLIMIT="Y";
		  }else if(ISCREDITLIMIT.equals("0"))
		  {
			  ISCREDITLIMIT="N";
		  }
		  CREDITLIMITVALUE ="".equals(CREDITLIMIT) ? 0.0f :  Float.parseFloat(CREDITLIMIT);
		  CREDITLIMIT = StrUtils.addZeroes(CREDITLIMITVALUE, numberOfDecimal);
		  OPENINGBALANCEVALUE ="".equals(OPENINGBALANCE) ? 0.0f :  Float.parseFloat(OPENINGBALANCE);
		  OPENINGBALANCE = StrUtils.addZeroes(OPENINGBALANCEVALUE, numberOfDecimal);
		  CUST_ADDONCOSTVALUE ="".equals(CUST_ADDONCOST) ? 0.0f :  Float.parseFloat(CUST_ADDONCOST);
		  Hashtable htUpdate = new Hashtable();
		  htUpdate.put(IConstants.PLANT,plant);
		  htUpdate.put(IConstants.CUSTOMER_CODE,sCustCode);
		  htUpdate.put(IConstants.CUSTOMER_NAME,sCustName);
		  htUpdate.put("CURRENCY_ID",CURRENCY);
		  htUpdate.put(IConstants.companyregnumber,companyregnumber);
		  htUpdate.put(IConstants.CUSTOMER_LAST_NAME,sCustNameL);
		  htUpdate.put(IConstants.ADDRESS1,sAddr1);
		  htUpdate.put(IConstants.ADDRESS2,sAddr2);
		  htUpdate.put(IConstants.ADDRESS3,sAddr3);
		  htUpdate.put(IConstants.ADDRESS4,sAddr4);
		  if(sState.equalsIgnoreCase("Select State"))
				sState="";
		  htUpdate.put(IConstants.STATE,strUtils.InsertQuotes(sState));
		  htUpdate.put(IConstants.COUNTRY,strUtils.InsertQuotes(sCountry));
		  htUpdate.put(IConstants.ZIP,sZip);
		  htUpdate.put(IConstants.USERFLG1,sCons);
		  htUpdate.put(IConstants.NAME,sContactName);
		  htUpdate.put(IConstants.DESGINATION,sDesgination);
		  htUpdate.put(IConstants.TELNO,sTelNo);
		  htUpdate.put(IConstants.HPNO,sHpNo);
		  htUpdate.put(IConstants.FAX,sFax);
          htUpdate.put("DATEOFBIRTH",sDob);
          htUpdate.put("NATIONALITY",sNational);
		  htUpdate.put(IConstants.EMAIL,sEmail);
		  htUpdate.put(IConstants.TRANSPORTID,transport);
		  
		  htUpdate.put(IConstants.SHIP_CONTACTNAME,shipContactName);
          htUpdate.put(IConstants.SHIP_DESGINATION,shipDesgination);
          htUpdate.put(IConstants.SHIP_WORKPHONE,shipWORKPHONE);
          htUpdate.put(IConstants.SHIP_HPNO,shipHpNo);
          htUpdate.put(IConstants.SHIP_EMAIL,shipEmail);
          htUpdate.put(IConstants.SHIP_COUNTRY_CODE,shipCountry);
          htUpdate.put(IConstants.SHIP_ADDR1,shipAddr1);
          htUpdate.put(IConstants.SHIP_ADDR2,shipAddr2);
          htUpdate.put(IConstants.SHIP_ADDR3,shipAddr3);
          htUpdate.put(IConstants.SHIP_ADDR4,shipAddr4);
          if(shipState.equalsIgnoreCase("Select State"))
        	  shipState="";
          htUpdate.put(IConstants.SHIP_STATE,shipState);
          htUpdate.put(IConstants.SHIP_ZIP,shipZip);
		  
		  htUpdate.put(IConstants.REMARKS,strUtils.InsertQuotes(sRemarks));
		  htUpdate.put(IConstants.PAYTERMS, strUtils.InsertQuotes(sPayTerms));
		  htUpdate.put(IConstants.payment_terms, strUtils.InsertQuotes(sPayMentTerms));
          htUpdate.put(IConstants.PAYINDAYS, sPayInDays);
		  htUpdate.put(IConstants.UPDATED_AT,new DateUtils().getDateTime());
		  htUpdate.put(IConstants.UPDATED_BY,sUserId);
		  htUpdate.put(IConstants.ISACTIVE,isActive);
		  htUpdate.put(IConstants.CUSTOMERTYPEID,customertypeid);
          htUpdate.put(IConstants.CUSTOMERSTATUSID,customer_status_id);
          htUpdate.put(IConstants.RCBNO,sRcbno);
          htUpdate.put("CREDITLIMIT",CREDITLIMIT);
          htUpdate.put("ISCREDITLIMIT",ISCREDITLIMIT);
          htUpdate.put("CUST_ADDONCOST",CUST_ADDONCOST);
          htUpdate.put("ISSHOWBYPRODUCT",APP_SHOWBY_PRODUCT);
          htUpdate.put("ISPEPPOL",PEPPOL);
          htUpdate.put("PEPPOL_ID",PEPPOL_ID);
          htUpdate.put("ISSAMEASCONTACTADD",SameAsContactAddress);
          htUpdate.put("ISSHOWAPPCATEGORYIMAGE",APP_SHOWBY_CATEGORY);
          htUpdate.put("SHOWSALESBYPURCHASECOST",SHOWSALESBYPURCHASECOST); 
          htUpdate.put("ADDONCOSTTYPE",ADDONCOSTTYPE); 
          htUpdate.put(IConstants.CUSTOMEREMAIL,CUSTOMEREMAIL);
          htUpdate.put(IConstants.WEBSITE,WEBSITE);
          htUpdate.put(IConstants.FACEBOOK,FACEBOOK);
          htUpdate.put(IConstants.TWITTER,TWITTER);
          htUpdate.put(IConstants.LINKEDIN,LINKEDIN);
          htUpdate.put(IConstants.SKYPE,SKYPE);
          htUpdate.put(IConstants.OPENINGBALANCE,OPENINGBALANCE);
          htUpdate.put(IConstants.WORKPHONE,WORKPHONE);
          htUpdate.put(IConstants.TAXTREATMENT, sTAXTREATMENT);
          htUpdate.put("CREDIT_LIMIT_BY", CREDIT_LIMIT_BY);
          if(sBANKNAME.equalsIgnoreCase("Select Bank"))
        	  sBANKNAME="";
          htUpdate.put(IDBConstants.BANKNAME,sBANKNAME);
          htUpdate.put(IDBConstants.IBAN,sIBAN);
          htUpdate.put(IDBConstants.BANKROUTINGCODE,sBANKROUTINGCODE);
          Hashtable htCondition = new Hashtable();
          htCondition.put("CUSTNO",sCustCode);
          htCondition.put(IConstants.PLANT,plant);
          
            Hashtable htdohdr = new Hashtable();
            htdohdr.put("CustCode", sCustCode);
            htdohdr.put(IConstants.PLANT, plant);

            Hashtable htJournalDet = new Hashtable();
            htJournalDet.put("ACCOUNT_NAME", sCustCode+"-"+sOldCustName);
            htJournalDet.put(IConstants.PLANT, plant);
			
            String updateJournalDet ="set ACCOUNT_NAME='"+sCustCode+"-"+sCustName+"'"; 
            
			StringBuffer updatedohdr = new StringBuffer("set ");
			updatedohdr.append(IConstants.IN_CUST_CODE + " = '" + sCustCode + "'");
			updatedohdr.append("," +IConstants.IN_CUST_NAME + " = '" + sCustName + "'");
			updatedohdr.append("," + IConstants.IN_PERSON_IN_CHARGE + " = '"+ sContactName + "'");
			updatedohdr.append(",Address = '"+ sAddr1 + "'");
			updatedohdr.append(",Address2 = '"+ sAddr2 + "'");
			updatedohdr.append(",Address3 = '"+ sAddr3 + "'");
          
			Hashtable htdodet = new Hashtable();
			htdodet.put(IConstants.PLANT, plant); 
			
			String dodetquery = "select Count(*) from "+plant+"_DODET where DONO in(select DONO from "+plant+"_DOHDR where plant='"+plant+"' and  CustCode='"+sCustCode+"' )";
			String dodetupdate = "set userfld3='"+sCustName+"' ";

			String shiphisquery = "select Count(*) from "+plant+"_SHIPHIS where DONO in(select DONO from "+plant+"_DOHDR where  plant='"+plant+"' and CustCode='"+sCustCode+"' )";
			String shiphisupdate = "set cname='"+sCustName+"' ";
			
			String wodetquery = "select Count(*) from "+plant+"_WODET where WONO in(select WONO from "+plant+"_WOHDR where plant='"+plant+"' and  CustCode='"+sCustCode+"' )";
			String wodetupdate = "set userfld3='"+sCustName+"' ";

            

         mdao.setmLogger(mLogger);
		 Hashtable htm = new Hashtable();
         htm.put(IDBConstants.PLANT,plant);
         htm.put(IDBConstants.DIRTYPE,TransactionConstants.UPD_CUST);
	     htm.put("RECID","");
	     htm.put("ITEM",sCustCode);
	     htm.put(IDBConstants.CREATED_BY,sUserId);   htm.put("CRBY",sUserId);
	     htm.put(IDBConstants.CREATED_AT,dateutils.getDateTime());
	     htm.put(IDBConstants.REMARKS,sCustName+","+strUtils.InsertQuotes(sRemarks));
	     htm.put(IDBConstants.UPDATED_AT,dateutils.getDateTime());
	     htm.put(IDBConstants.TRAN_DATE,dateutils.getDateinyyyy_mm_dd(dateutils.getDate()));  
	     boolean custUpdated = custUtil.updateCustomer(htUpdate,htCondition);
         boolean  inserted = mdao.insertIntoMovHis(htm);
         boolean flag = false;
			if(custUpdated){
				if(!sCustName.equalsIgnoreCase(sOldCustName))
				 {
				 flag = DoHdrDAO.isExisit(htdohdr,"");
				 if(flag){
				 	flag = DoHdrDAO.update(updatedohdr.toString(), htdohdr, " ");
				 }
				 if(flag){
					 flag = DoDetDAO.isExisit(dodetquery);
					 if(flag)
					 {
						 flag = DoDetDAO.update(dodetupdate, htdodet, " and DONO in(select DONO from "+plant+"_DOHDR where plant='"+plant+"' and CustCode='"+sCustCode+"' )");
					 }
				 }
				 if(flag){
					 flag = ShipHisDAO.isExisit(shiphisquery);
					 if(flag)
					 {
						 flag = ShipHisDAO.update(shiphisupdate, htdodet, " and DONO in(select DONO from "+plant+"_DOHDR where plant='"+plant+"' and CustCode='"+sCustCode+"' )");
					 }
				 }
				 flag = EstHdrDAO.isExisit(htdohdr);
				 if(flag){
					 	flag = EstHdrDAO.update(updatedohdr.toString(), htdohdr, " ");
					 }
				//FINCHARTOFACCOUNTS & FINJOURNALDET UPDATE
				 flag = new CoaDAO().isExisitcoaAccount(htJournalDet);
				 if(flag){
					 	flag = new CoaDAO().updatecoaAccount(updateJournalDet.toString(), htJournalDet, " ");
					 }
				 flag = new JournalDAO().isExisitJournalDet(htJournalDet);
				 if(flag){
					 	flag = new JournalDAO().updateJournalDet(updateJournalDet.toString(), htJournalDet, " ");
					 }
					 
				 }
				/* flag = WorkOrderHdrDAO.isExisit(htdohdr,"");
				 if(flag){
					 flag =  WorkOrderHdrDAO.update(updatedohdr.toString(), htdohdr, " ");
				 }
				 if(flag){
					 flag = WorkOrderDetDAO.isExisit(wodetquery);
					 if(flag)
				 	 {
						 flag = WorkOrderDetDAO.update(wodetupdate, htdodet, " and WONO in(select WONO from "+plant+"_WOHDR where plant='"+plant+"' and CustCode='"+sCustCode+"' )");
					 }
				 }*/
				 
				 if(customeruserid !=null) {
						for(int i =0 ; i < customeruserid.length ; i++){
							Hashtable customeruser = new Hashtable<String, String>();
							String sPassword =(String)PASSWORD[i];
							String smanager =(String)MANAGER_APP[i];
							String Tmanager = "0";
							if(sPassword != ""){
					        	String  empPwd   = eb.encrypt(sPassword);
					        	  sPassword = empPwd;
					          }
							customeruser.put("PLANT", plant);
							customeruser.put("CUSTNO", sCustCode);
							customeruser.put("CUSTDESC", sCustName);
							customeruser.put("USER_ID", (String)customeruserid[i]);
							customeruser.put("PASSWORD", sPassword);
							customeruser.put("USER_NAME", (String)USER_NAME[i]);
							customeruser.put("USER_HPNO", (String)USER_HPNO[i]);
							customeruser.put("USER_EMAIL", (String)USER_EMAIL[i]);
							customeruser.put("ISMANAGERAPPACCESS",smanager);
							customeruser.put("CRAT",dateutils.getDateTime());
							customeruser.put("CRBY",sUserId);
							customeruser.put("UPAT",dateutils.getDateTime());
							boolean itemInserted =new CustMstDAO().addcustomerUser(customeruser, plant);
						}
				 }
				 
			}
          if(custUpdated&&inserted) {
        	  sSAVE_RED = "Customer Updated Successfully";
                    /* res = "<font class = "+IConstants.SUCCESS_COLOR+">Customer Updated Successfully</font>"; */
          } else {
        	  sSAVE_RED = "Failed to Update Customer";
//                     res = "<font class = "+IConstants.FAILED_COLOR+">Failed to Update Customer</font>";
          }
	  }else{
		  sSAVE_RED = "Customer Name Exists already. Try again with diffrent Customer Name";
//           res = "<font class = "+IConstants.FAILED_COLOR+">Customer Name Exists already. Try again with diffrent Customer Name.</font>";

      }
		}else{
	    	  sSAVE_RED = "User ID "+chkcustomeruserid+" Exists already. Try again with diffrent User ID.";
//	           res = "<font class = "+IConstants.FAILED_COLOR+">Customer doesn't not Exists. Try again</font>";
	
	    }		
		}else{
	    	  sSAVE_RED = "Customer doesn't not Exists. Try again";
// 	           res = "<font class = "+IConstants.FAILED_COLOR+">Customer doesn't not Exists. Try again</font>";
	
	    }
}

else if(action.equalsIgnoreCase("DELETE")){
    sCustEnb = "enabled";
    DoHdrDAO dao = new DoHdrDAO();
    EstHdrDAO estdao = new EstHdrDAO();
    
    boolean movementhistoryExist=false;
    boolean isExistEstimate=false;
   	Hashtable htmh = new Hashtable();
   	htmh.put("CUSTCODE",strUtils.InsertQuotes(sCustCode));
   	htmh.put(IConstants.PLANT,plant);
   	
   	movementhistoryExist = dao.isExisit(htmh,"");
   	isExistEstimate= estdao.isExisit(htmh,"");
   	if(movementhistoryExist || isExistEstimate)
   	{	
   	  sSAVE_REDELETE="Customer Exists In Transactions";
   			/* res = "<font class = " + IDBConstants.FAILED_COLOR
   					+ " >Customer Exists In Transactions</font>"; */
   		
   	}
   	else{
   	
    if(custUtil.isExistCustomer(strUtils.InsertQuotes(sCustCode),plant))
    {
          boolean custDeleted = custUtil.deleteCustomer(strUtils.InsertQuotes(sCustCode),plant);
          mdao.setmLogger(mLogger);
 			Hashtable htm = new Hashtable();
   			htm.put(IDBConstants.PLANT,plant);
   		 	htm.put(IDBConstants.DIRTYPE,TransactionConstants.DEL_CUST);
    		htm.put("RECID","");
    		htm.put("ITEM",sCustCode);
   		 	htm.put(IDBConstants.CREATED_BY,sUserId);   htm.put("CRBY",sUserId);
    	 	htm.put(IDBConstants.CREATED_AT,dateutils.getDateTime());
     		htm.put(IDBConstants.REMARKS,sCustName+","+strUtils.InsertQuotes(sRemarks));
     		htm.put(IDBConstants.UPDATED_AT,dateutils.getDateTime());
     		htm.put(IDBConstants.TRAN_DATE,dateutils.getDateinyyyy_mm_dd(dateutils.getDate()));  
    
     		custDeleted = mdao.insertIntoMovHis(htm);
     		customerAttachDAO.deletecustomerAttachByNo(plant, sCustCode);	
     		boolean removeuserid= new CustMstDAO().Deletecustomeruser(sCustCode,plant);
          if(custDeleted) {
        	  sSAVE_REDELETE="Customer Deleted Successfully";
                    /* res = "<font class = "+IConstants.SUCCESS_COLOR+">Customer Deleted Successfully</font>"; */
                    sSAVE_REDELETE ="Delete";
//                    sAddEnb    = "disabled";
                    sCustCode  = "";
                    sCustName  = "";
                    companyregnumber = "";
                    sCustNameL = "";
                    sAddr1     = "";
                    sAddr2     = "";
                    transport="";
                    sAddr3     = "";sAddr4="";
                    
                    sState   = "";
                    sCountry   = "";
                    sZip       = "";
                    sContactName = "";
                    sDesgination = "";
                    sTelNo       = "";
                    sHpNo        = "";
                    sFax         = "";
                    sDob         ="";
                    sNational	 ="";
                    sEmail       = "";
                    sRemarks     = "";
                    sPayTerms    ="";
                    sPayMentTerms="";
                    sPayInDays   ="";
                    sCons        = "Y";
                    sState="";
                    sRcbno="";
					customertypeid="";
					CURRENCY="";
					CREDITLIMIT="";					
					CUST_ADDONCOST="";					
				      CUSTOMEREMAIL="";WEBSITE="";FACEBOOK="";TWITTER="";LINKEDIN="";SKYPE="";OPENINGBALANCE="";WORKPHONE="";
				      sIBAN="";sBANKNAME="";sBANKROUTINGCODE="";sBRANCH="";CREDITLIMITVALUE=0;OPENINGBALANCEVALUE=0;sCountryCode="";shipcountrycode="";
          } else {
        	  sSAVE_REDELETE="Failed to Delete Customer";
                  /*   res = "<font class = "+IConstants.FAILED_COLOR+">Failed to Delete Customer</font>"; */
                    sAddEnb = "enabled";
          }
    }else{
    	  sSAVE_REDELETE="Customer doesn't not Exists. Try againy";
//            res = "<font class = "+IConstants.FAILED_COLOR+">Customer doesn't not Exists. Try again</font>";
     }
   	}
}

//4. >> View
else if(action.equalsIgnoreCase("VIEW")){
try{


    ArrayList arrCust = custUtil.getCustomerDetails(sCustCode,plant);
   sCustCode   = (String)arrCust.get(0);
   sCustName   = (String)arrCust.get(1);
   sAddr1      = (String)arrCust.get(2);
   sAddr2      = (String)arrCust.get(3);
   sAddr3      = (String)arrCust.get(4);
   sCountry    = (String)arrCust.get(5);
   sZip        = (String)arrCust.get(6);
   sCons       = (String)arrCust.get(7);
   sCustNameL  = (String)arrCust.get(8);
   sContactName=(String)arrCust.get(9);
   sDesgination=(String)arrCust.get(10);
   sTelNo=(String)arrCust.get(11);
   sHpNo=(String)arrCust.get(12);
   sFax=(String)arrCust.get(13);
   sDob=(String)arrCust.get(61);
   sNational=(String)arrCust.get(62);
   sEmail= (String)arrCust.get(14);
   sRemarks=(String)arrCust.get(15);
   sAddr4=(String)arrCust.get(16);
   isActive=(String)arrCust.get(17);
   sPayTerms = (String) arrCust.get(18);
   sPayMentTerms = (String) arrCust.get(59);
   sPayInDays = (String) arrCust.get(19);
 //  customertypeid = (String) arrCust.get(20);
   
   shipZip = (String) arrCust.get(55);
   shipState = (String) arrCust.get(54);
   shipAddr4 = (String) arrCust.get(53);
   shipAddr3 = (String) arrCust.get(52);
   shipAddr2 = (String) arrCust.get(51);
   shipAddr1 = (String) arrCust.get(50);
   shipCountry = (String) arrCust.get(49);
   shipEmail = (String) arrCust.get(48);
   shipHpNo = (String) arrCust.get(47);
   shipWORKPHONE = (String) arrCust.get(46);
   shipDesgination = (String) arrCust.get(45);
   shipContactName = (String) arrCust.get(44);
   shipcountrycode = (String) arrCust.get(58);
   
   
customer_status_id = (String) arrCust.get(21);
sState = (String) arrCust.get(22);
sRcbno = (String) arrCust.get(23);
CREDITLIMIT = (String) arrCust.get(24);
ISCREDITLIMIT = (String) arrCust.get(25);
CUSTOMEREMAIL = (String) arrCust.get(26);
WEBSITE = (String) arrCust.get(27);
FACEBOOK = (String) arrCust.get(28);
TWITTER = (String) arrCust.get(29);
LINKEDIN = (String) arrCust.get(30);
SKYPE = (String) arrCust.get(31);
OPENINGBALANCE = (String) arrCust.get(32);
WORKPHONE = (String) arrCust.get(33);
sTAXTREATMENT= (String) arrCust.get(34);
CREDIT_LIMIT_BY = (String) arrCust.get(35);
sCountryCode = (String) arrCust.get(36);
sBANKNAME = (String) arrCust.get(37);
sBRANCH= (String) arrCust.get(38);
sIBAN = (String) arrCust.get(39);
sBANKROUTINGCODE = (String) arrCust.get(40);
companyregnumber = (String) arrCust.get(41);
CURRENCY = (String) arrCust.get(42);
APP_SHOWBY_PRODUCT = (String) arrCust.get(43);
PEPPOL_ID = (String) arrCust.get(64);
PEPPOL = (String) arrCust.get(63);
SameAsContactAddress = (String) arrCust.get(60);
APP_SHOWBY_CATEGORY = (String) arrCust.get(56);
SHOWSALESBYPURCHASECOST = (String) arrCust.get(65);
CUST_ADDONCOST = (String) arrCust.get(66);
ADDONCOSTTYPE = (String) arrCust.get(67);
if(!CURRENCY.equalsIgnoreCase("")){
if(!currency.equalsIgnoreCase(CURRENCY)){
	 curQryList = currencyDAO.getCurrencyDetails(CURRENCY,plant);
	 for(int i =0; i<curQryList.size(); i++) {
			ArrayList arrCurrLine = (ArrayList)curQryList.get(i);
			DISPLAYID	= StrUtils.fString(StrUtils.removeQuotes((String)arrCurrLine.get(0)));
			DISPLAY	= StrUtils.fString(StrUtils.removeQuotes((String)arrCurrLine.get(2)));
	 }
	}
}

TransportModeDAO transportmodedao = new TransportModeDAO();
transport = (String) arrCust.get(57);
int trans = Integer.valueOf(transport);
if(trans > 0){
	transportmode = transportmodedao.getTransportModeById(plant,trans);
}
else{
transportmode = "";
}

CustomerBeanDAO custTypedao = new CustomerBeanDAO();
customertypeid = (String) arrCust.get(20);
String Cust = String.valueOf(customertypeid);
if(Cust.length() > 0){
	customertype_id = custTypedao.getCustomerTypeById(plant,Cust);
}
else{
	customertype_id = "";
}

customerattachlist = customerAttachDAO.getcusAttachBycusId(plant, sCustCode);
CREDITLIMITVALUE ="".equals(CREDITLIMIT) ? 0.0f :  Float.parseFloat(CREDITLIMIT);
OPENINGBALANCEVALUE ="".equals(OPENINGBALANCE) ? 0.0f :  Float.parseFloat(OPENINGBALANCE);
CUST_ADDONCOSTVALUE ="".equals(CUST_ADDONCOST) ? 0.0f :  Float.parseFloat(CUST_ADDONCOST);

     
   }catch(Exception e)
   {

       res="no details found for customer id : "+  sCustCode;
   }
 
}
CREDITLIMIT = StrUtils.addZeroes(CREDITLIMITVALUE, numberOfDecimal);
OPENINGBALANCE = StrUtils.addZeroes(OPENINGBALANCEVALUE, numberOfDecimal);
CUST_ADDONCOST = StrUtils.addZeroes(CUST_ADDONCOSTVALUE, numberOfDecimal);
%>

<div class="container-fluid m-t-20">
	 <div class="box"> 
	 <!-- Thanzith Modified on 23.02.2022 -->
	 <ul class="breadcrumb backpageul">    
                <li class="underline-on-hover"><a href="../home">Dashboard</a></li>	
                 <li><a href="../customer/summary"><span class="underline-on-hover">Customer Summary</span></a></li>                        
                <li><label>Edit Customer</label></li>                                   
            </ul>
        <!-- Thanzith Modified on 23.02.2022 -->     
            <div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title"><%=title %></h1>
              <h1 style="font-size: 18px; cursor: pointer;"
				class="box-title pull-right"
				onclick="window.location.href='../customer/summary'" >
				<i class="glyphicon glyphicon-remove"></i>
			</h1>
		</div>
		
 <div class="box-body">

   <CENTER><strong><%=res%></strong></CENTER>

  <form class="form-horizontal" id="customerform" name="form" autocomplete="off" method="post">
<%--    <input name="CUSTOMER_TYPE_DESC" type="hidden" value="<%=desc%>"> --%>
   <input type="number" id="numberOfDecimal" style="display:none;" value=<%=numberOfDecimal%>>
    <div class="form-group">
     <label class="control-label col-form-label col-sm-2 required">Customer Id</label>
      <div class="col-sm-4">
      
      	<div class="input-group">   
      	  	<input name="CUST_CODE" type="TEXT" value="<%=sCustCode%>"
			size="50" MAXLENGTH=50 class="form-control" width="50" readonly>
   		 	<!-- <span class="input-group-addon"  onClick="javascript:popUpWin('../jsp/customer_list.jsp?CUST_NAME='+form.CUST_NAME.value);return false;" >
   		 	<a href="#" data-toggle="tooltip" data-placement="top" title="Customer Details">
   		 	<i class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span> -->
  		</div>
  		<INPUT type="hidden" name="CUST_CODE1" value="">
  		<INPUT type="hidden" name="EDIT_TAXTREATMENT" value="<%=sTAXTREATMENT%>">
  		<INPUT type="hidden" name="EDIT_STATE" value="<%=sState%>">
  		<INPUT type="hidden" name="EDIT_SHIP_STATE" value="<%=shipState%>">
  		<INPUT type="hidden" name="COUNTRY" value="<%=sCountry%>">
  		<INPUT type="hidden" name="SHIP_COUNTRY" value="<%=shipCountry%>">
  		<INPUT type="hidden" name="EDIT_COUNTRY" value="<%=sCountryCode%>">
  		<INPUT type="hidden" name="EDIT_SHIP_COUNTRY" value="<%=shipcountrycode%>">
  		<INPUT type="hidden" name="COUNTRY_REG" value="<%=region%>">
  		<INPUT type="hidden" name="EDIT_BANKNAME" value="<%=sBANKNAME%>">
  		<INPUT type="hidden" name="SAVE_RED" value="<%=sSAVE_RED%>">
  		<INPUT type="hidden" name="SAVE_REDELETE" value="<%=sSAVE_REDELETE%>">
  		<input type="hidden" name="CURRENCYID" value="<%=DISPLAYID%>">
  	    <input type = "hidden" name="currency" value="<%=currency%>">
  	    	    <input type="hidden" name="TRANSPORTID" value="<%=transport%>">
  	    	    <input type = "hidden" name="EDUCATION" value="<%=COMP_INDUSTRY%>">
  	    	    <input type = "hidden" name="CUSTOMER_TYPE_ID" value="<%=customertypeid%>">
  	    	    <input type = "hidden" name="OLD_CUST_NAME" value="<%=sCustName%>">
      	      </div>
<%--     <% if(COMP_INDUSTRY.equals("Education")){%> --%>
<!--       <div class="form-inline"> -->
<!--       <label class ="checkbox-inline"> -->
<!-- <input type = "checkbox" id = "APP_SHOWBY_PRODUCT" name = "APP_SHOWBY_PRODUCT" value = "APP_SHOWBY_PRODUCT" -->
<%-- <%if(APP_SHOWBY_PRODUCT.equals("1")) {%>checked <%}%> />Show APP Products Image</label> --%>
<!--     </div> -->
<%--     <%}else{ %> --%>
<!--    <div class=form-inline> -->
<!--    <div class="col-sm-2"> -->
<!--       	<button type="button" class="Submit btn btn-default" onClick="onView();">View</button>&nbsp;&nbsp; -->
<!--     </div></div> -->
<%--     <%} %> --%>
    </div>   
    <div class="form-group">
      <label class="control-label col-form-label col-sm-2 required">Customer Name</label>
      <div class="col-sm-4">
                
        <INPUT  class="form-control" name="CUST_NAME" type="TEXT" value="<%=sCustName%>"
			size="50" MAXLENGTH=100>
      </div>
      <% if(COMP_INDUSTRY.equals("Education")){%>
<!--           <div class="form-inline"> -->
<!--       <label class ="checkbox-inline"> -->
<!-- <input type = "checkbox" id = "APP_SHOWBY_CATEGORY" name = "APP_SHOWBY_CATEGORY" value = "APP_SHOWBY_CATEGORY" -->
<%-- <%if(APP_SHOWBY_CATEGORY.equals("1")) {%>checked <%}%> />Show APP Category Image</label> --%>
<!--     </div> -->
    <%}else{ %>
      <div class="form-inline">
      <label class ="checkbox-inline">
<input type = "checkbox" id = "APP_SHOWBY_PRODUCT" name = "APP_SHOWBY_PRODUCT" value = "APP_SHOWBY_PRODUCT"
<%if(APP_SHOWBY_PRODUCT.equals("1")) {%>checked <%}%> />Show APP Products Image</label>
    </div>
    <%} %>
      <INPUT name="L_CUST_NAME" type = "hidden" value="<%=sCustNameL%>" size="50"  MAXLENGTH=100>
      </div>
      
          <div class="form-group" id="UEN">
           <% if(COMP_INDUSTRY.equals("Education")){%>
	  <label class="control-label col-form-label col-sm-2">IC/FIN Number</label>
	      <%}else{ %>
      <label class="control-label col-form-label col-sm-2">Unique Entity Number (UEN)</label>
	      <%}%>
      <div class="col-sm-4">
                
        <INPUT  class="form-control" name="companyregnumber" type="TEXT" value="<%=companyregnumber%>"
			size="50" MAXLENGTH=50>
      </div>
      <% if(!COMP_INDUSTRY.equals("Education")){%>
      <div class="form-inline">
      <label class ="checkbox-inline">
<input type = "checkbox" id = "APP_SHOWBY_CATEGORY" name = "APP_SHOWBY_CATEGORY" value = "APP_SHOWBY_CATEGORY"
<%if(APP_SHOWBY_CATEGORY.equals("1")) {%>checked <%}%> />Show APP Category Image</label>
    </div>
    <%} %>
    </div>
    
    <% if(COMP_INDUSTRY.equals("Education")){%>
 				<div class="form-group">
				<label class="control-label col-form-label col-sm-2 required">Date of Birth</label>
					<div class="col-sm-4">
						<div class="input-group">
							<input type="text" class="form-control" autocomplete="off" id="DOB" name="DOB" value="<%=sDob%>" readonly>
						</div>
					</div>
				</div>
				
				<div class="form-group">
      			<label class="control-label col-form-label col-sm-2 required"for="Nationality">Nationality</label>
      				<div class="col-sm-4">
      					<div class="input-group">
      	  	 				<input type="text" name="NATIONALITY" id="NATIONALITY" value="<%=sNational%>" class="ac-selected form-control" placeholder="SELECT NATIONALITY" >
							<span class="select-icon" onclick="$(this).parent().find('input[name=\'NATIONALITY\']').focus()">
							<i class="glyphicon glyphicon-menu-down"></i></span>
  						</div>
      				</div>
   				</div>
		<%} %>
    <% if(COMP_INDUSTRY.equals("Education")){%>
     <div class="form-group" style="display:none;">
					<label class="control-label col-form-label col-sm-2 required " for="CURRENCY">Customer Currency</label>
					<div class="col-sm-4">
						<input type="text" class="ac-selected form-control typeahead" id="CURRENCY" name="CURRENCY" placeholder="Select a Currency" value="<%=DISPLAY%>">
						<span class="select-icon" onclick="$(this).parent().find('input[name=\'CURRENCY\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
					</div>
					
      <%-- <div class="form-inline">
      <label class ="checkbox-inline">
<input type = "checkbox" id = "SHOWSALESBYPURCHASECOST" name = "SHOWSALESBYPURCHASECOST" value = "SHOWSALESBYPURCHASECOST"
<%if(SHOWSALESBYPURCHASECOST.equals("1")) {%>checked <%}%> />Show Sales Price by Avg.Cost</label>
    </div> --%>
    
    <div class="form-inline">
<div class="col-sm-6" style="right: 15px;">  
      <input type="radio" name="SHOWSALESBYPURCHASECOST" <%if(SHOWSALESBYPURCHASECOST.equalsIgnoreCase("0")) {%>checked <%}%> value="0">None &nbsp;&nbsp;
      <input type="radio" name="SHOWSALESBYPURCHASECOST" <%if(SHOWSALESBYPURCHASECOST.equalsIgnoreCase("1")) {%>checked <%}%> value="1">Show Sales Price by Avg.Cost &nbsp;&nbsp;
      <input type="radio" name="SHOWSALESBYPURCHASECOST" <%if(SHOWSALESBYPURCHASECOST.equalsIgnoreCase("2")) {%>checked <%}%> value="2">Show Sales Price by Purchase Cost
      </div>
</div>
    
				</div>
				<%}else{ %>
     <div class="form-group">
					<label class="control-label col-form-label col-sm-2 required " for="CURRENCY">Customer Currency</label>
					<div class="col-sm-4">
						<input type="text" class="ac-selected form-control typeahead" id="CURRENCY" name="CURRENCY" placeholder="Select a Currency" value="<%=DISPLAY%>">
						<span class="select-icon" onclick="$(this).parent().find('input[name=\'CURRENCY\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
					</div>
					
<%--       <div class="form-inline">
      <label class ="checkbox-inline">
       <input type = "checkbox" id = "SHOWSALESBYPURCHASECOST" name = "SHOWSALESBYPURCHASECOST" value = "SHOWSALESBYPURCHASECOST"
<%if(SHOWSALESBYPURCHASECOST.equals("1")) {%>checked <%}%> />Show Sales Price by Avg.Cost</label>
    </div> --%>
    
        <div class="form-inline">
<div class="col-sm-6" style="right: 15px;">  
      <input type="radio" name="SHOWSALESBYPURCHASECOST" <%if(SHOWSALESBYPURCHASECOST.equalsIgnoreCase("0")) {%>checked <%}%> value="0">None &nbsp;&nbsp;
      <input type="radio" name="SHOWSALESBYPURCHASECOST" <%if(SHOWSALESBYPURCHASECOST.equalsIgnoreCase("1")) {%>checked <%}%> value="1">Show Sales Price by Avg.Cost &nbsp;&nbsp;
      <input type="radio" name="SHOWSALESBYPURCHASECOST" <%if(SHOWSALESBYPURCHASECOST.equalsIgnoreCase("2")) {%>checked <%}%> value="2">Show Sales Price by Purchase Cost
      </div>
</div>
    
				</div>
				<%} %>
    
    <% if(!COMP_INDUSTRY.equals("Education")){%>
    <div class="form-group">
      <label class="control-label col-form-label col-sm-2" for="customer Group">Customer Group</label>
               	
      	 <div class="col-sm-4">     
      	 	<div class="input-group">         	
    		<input name="CUSTOMER_TYPE_DESC" id="CUSTOMER_TYPE_DESC"  type="TEXT" placeholder="Select a Customer Group" value="<%=customertype_id%>"
			size="50" MAXLENGTH=50 class="form-control">
   		 	<span class="select-icon"  onclick="$(this).parent().find('input[name=\'CUSTOMER_TYPE_ID\']').focus()" > 	
   		 	<!-- <a href="#" data-toggle="tooltip" data-placement="top" title="Customer Type Details"> -->
   		 	<i class="glyphicon glyphicon-menu-down"></i></a></span>
  	</div>
  	</div>
  	</div>
  	
  	 <div class="form-group">
      <label class="control-label col-form-label col-sm-2" for="transmode">Transport Mode</label>
      <div class="col-sm-4">           	
    		<input name="transport"  id="transport"  type="TEXT" value="<%=transportmode%>" placeholder="Select a transport"
			size="50" MAXLENGTH=50 class="form-control">
   		 	<span class="select-icon" onclick="$(this).parent().find('input[name=\'transport\']').focus()">
									<i class="glyphicon glyphicon-menu-down"></i>
								</span>
  	</div>
  	</div>
  	<%} %>
    
	<INPUT type="hidden" id="TaxByLabel" name="taxbylabel" value=<%=taxbylabel%>>
	
    <% if(!COMP_INDUSTRY.equals("Education")){%>
    <div class="form-group">
      <label class="control-label col-form-label col-sm-2" for="Telephone No">Customer Phone</label>
      <div class="col-sm-4">
                 
        <INPUT name="TELNO" type="TEXT" value="<%=sTelNo%>" size="50" class="form-control" onkeypress="return validateInput(event)"
			MAXLENGTH="30">
      </div>
    </div>
    
    <div class="form-group">
      <label class="control-label col-form-label col-sm-2" for="Fax">Customer Fax</label>
      <div class="col-sm-4">
               
        <INPUT name="FAX" type="TEXT" value="<%=sFax%>" size="50" onkeypress="return validateInput(event)"
			MAXLENGTH="30" class="form-control">
      </div>
    </div>
    
    	<div class="form-group">
      	<label class="control-label col-form-label col-sm-2" for="Customer Email">Customer Email</label>
      	<div class="col-sm-4">  
      	<INPUT name="CUSTOMEREMAIL" type="TEXT" value="<%=CUSTOMEREMAIL%>" size="50" MAXLENGTH=50 class="form-control">
      	</div>
      	</div>
    
        <div class="form-group">
      	<label class="control-label col-form-label col-sm-2" for="Website">Website</label>
      	<div class="col-sm-4">  
    	<INPUT name="WEBSITE" type="TEXT" value="<%=WEBSITE%>" size="50" MAXLENGTH=50 class="form-control">
      	</div>
    	</div>
      	<%} %>
      	
    	  <%if(ISPEPPOL.equalsIgnoreCase("1")){ %>
      	 <div class="form-group">
      <label class="control-label col-form-label col-sm-2">Peppol</label>
      <div class="col-sm-4">                
        <label class="checkbox-inline">
      <INPUT name="PEPPOL"  type = "checkbox" value="PEPPOL" id="PEPPOL"<%if(PEPPOL.equals("1")) {%>checked <%}%> >
    </label>
      </div>
    </div>
     <div class="form-group">
      	<label class="control-label col-form-label col-sm-2" for="Peppol id">Peppol Id</label>
      	<div class="col-sm-4">  
    	<INPUT name="PEPPOL_ID" id="PEPPOL_ID" type="TEXT" value="<%=PEPPOL_ID%>" size="50" MAXLENGTH=50 class="form-control">
      	</div>
    	</div>
    	<%}else{%>
    	<input type="hidden" name="PEPPOL" id="PEPPOL" value="">
					<input type="hidden" name="PEPPOL_ID" id="PEPPOL_ID" value="">
    <%}%>
    
    <div class="bs-example">
     <ul class="nav nav-tabs" id="myTab"> 
        <li class="nav-item active">
            <a href="#other" class="nav-link" data-toggle="tab" aria-expanded="true">Other Details</a>
        </li>
        <li class="nav-item">
            <a href="#profile" class="nav-link" data-toggle="tab">Contact And Address Details</a>
        </li>
<!--           <li class="nav-item">
            <a href="#home" class="nav-link" data-toggle="tab">Address</a>
        </li> -->
        <li class="nav-item">
            <a href="#bank_cus" class="nav-link" data-toggle="tab">Bank Account Details</a>
        </li>
        <% if(!COMP_INDUSTRY.equals("Education")){%>
        <li class="nav-item">
            <a href="#user_cus" class="nav-link" data-toggle="tab">User</a>
        </li>
        <%} %>
        <li class="nav-item">
            <a href="#remark" class="nav-link" data-toggle="tab">Remarks</a>
        </li>
        <li class="nav-item">
            <a href="#attachfiles" class="nav-link" data-toggle="tab">Attachments</a>
        </li>
        </ul>
    <div class="tab-content clearfix">
        <%-- <div class="tab-pane fade" id="home">
        <br>
        <div class="form-group">
      <label class="control-label col-form-label col-sm-2 required" for="Country">Country</label>
      <div class="col-sm-4">  
      <SELECT class="form-control" data-toggle="dropdown" data-placement="right" onchange="OnCountry(this.value)" id="COUNTRY_CODE" name="COUNTRY_CODE" value="<%=sCountry%>" style="width: 100%">
				<OPTION style="display:none;">Select Country</OPTION>
				<%
		   MasterUtil _MasterUtil=new  MasterUtil();
		   ArrayList ccList =  _MasterUtil.getCountryList("",plant,region);
			for(int i=0 ; i<ccList.size();i++)
      		 {
				Map m=(Map)ccList.get(i);
				String vCOUNTRYNAME = (String)m.get("COUNTRYNAME");
				String vCOUNTRY_CODE = (String)m.get("COUNTRY_CODE"); %>
		        <option  value='<%=vCOUNTRY_CODE%>' ><%=vCOUNTRYNAME%> </option>		          
		        <%
       			}
			 %></SELECT>       
       <INPUT name="COUNTRY" type="TEXT" value="<%=sCountry%>"
			size="50" MAXLENGTH=50 class="form-control">
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
        <INPUT name="STATE" type="TEXT" value="<%=sState%>" size="50"
			MAXLENGTH=50 class="form-control">
      </div>
    </div>
    
    <div class="form-group">
      <label class="control-label col-form-label col-sm-2" for="Postal Code">Postal Code</label>
      <div class="col-sm-4">
                
        <INPUT name="ZIP" type="TEXT" value="<%=sZip%>" size="50"
			MAXLENGTH=10 class="form-control">
      </div>
    </div>
		     
        </div> --%>
        
        <div class="tab-pane fade" id="profile">
        <br>
       <%--  <h1 class="col-sm-3" style="font-weight: bold;font-size: 16px;right: 16px;">  Contact Address </h1>
        <h1 style="font-weight: bold;font-size: 16px;margin-left: 600px;">  Shipping Address <label class="checkbox-inline" style="margin-left: 150px;margin-top: 1px;">      
       <input type="checkbox" id="SameAsContactAddress" name="SameAsContactAddress" value="SameAsContactAddress" onclick="shippingAddress();" <%if(SameAsContactAddress.equals("1")) {%>checked <%}%>>Same As Contact Address</label></h1> --%>
       
       <div class="form-group">
         <h1 class="col-sm-3" style="font-weight: bold;font-size: 16px;">  Contact Address </h1>
      <div class="col-sm-3">
        
      </div>
       <% if(!COMP_INDUSTRY.equals("Education")){%>
        <div class="col-sm-6">
        <h1 class="col-sm-6" style="font-weight: bold;font-size: 16px;">  Shipping Address </h1>
        <div class="form-inline">
        <label class="checkbox-inline" style="margin-top: 10px;">         
       <input type="checkbox" id="SameAsContactAddress" name="SameAsContactAddress" value="SameAsContactAddress" onclick="shippingAddress();" <%if(SameAsContactAddress.equals("1")) {%>checked <%}%>>Same As Contact Address</label>
      </div>
    </div>
    <%} %>
    </div>
        <% if(!COMP_INDUSTRY.equals("Education")){%>
        <div class="form-group">
         <label class="control-label col-form-label col-sm-2" for="Contact Name">Contact Name</label>
      <div class="col-sm-4">
       <INPUT name="CONTACTNAME" id="CONTACTNAME" type="TEXT" class="form-control"
			value="<%=sContactName%>" size="50" MAXLENGTH="100" >
      </div>
        <div class="col-sm-4">
       <INPUT name="SHIP_CONTACTNAME" id="SHIP_CONTACTNAME" type="TEXT" class="form-control"
			value="<%=shipContactName%>" size="50" MAXLENGTH="100" >
      </div>
    </div>
    <div class="form-group">
      <label class="control-label col-form-label col-sm-2" for="Designation">Designation</label>
      <div class="col-sm-4">
        <INPUT name="DESGINATION" id="DESGINATION" type="TEXT" class="form-control"
			value="<%=sDesgination%>" size="50" MAXLENGTH="30">
      </div>
       <div class="col-sm-4">
        <INPUT name="SHIP_DESGINATION" id="SHIP_DESGINATION" type="TEXT" class="form-control"
			value="<%=shipDesgination%>" size="50" MAXLENGTH="30">
      </div>
    </div>
    	<div class="form-group">
      	<label class="control-label col-form-label col-sm-2" for="Work phone">Work Phone</label>
      	<div class="col-sm-4">  
        <INPUT name="WORKPHONE" id="WORKPHONE" type="TEXT" value="<%=WORKPHONE%>" onkeypress="return validateInput(event)"	size="50" MAXLENGTH=30 class="form-control">
      	</div>
      		<div class="col-sm-4">  
        <INPUT name="SHIP_WORKPHONE" id="SHIP_WORKPHONE" type="TEXT" value="<%=shipWORKPHONE%>" onkeypress="return validateInput(event)"	size="50" MAXLENGTH=50 class="form-control">
      	</div>
    	</div>
    <%} %>
    
    <div class="form-group">
      <label class="control-label col-form-label col-sm-2" for="Hand Phone">Mobile</label>
      <div class="col-sm-4">
        <INPUT name="HPNO" id="HPNO" type="TEXT" value="<%=sHpNo%>" size="50" class="form-control" onkeypress="return validateInput(event)"
			MAXLENGTH="30">
      </div>
       <% if(!COMP_INDUSTRY.equals("Education")){%>
      <div class="col-sm-4">
        <INPUT name="SHIP_HPNO" id="SHIP_HPNO" type="TEXT" value="<%=shipHpNo%>" size="50" class="form-control" onkeypress="return validateInput(event)"
			MAXLENGTH="30">
      </div>
      <%} %>
    </div>  
    
    <div class="form-group">
      <label class="control-label col-form-label col-sm-2" for="Email">Email</label>
      <div class="col-sm-4"> 
        <INPUT name="EMAIL" id="EMAIL" type="TEXT" value="<%=sEmail%>" size="50"
			MAXLENGTH="50" class="form-control">
      </div>
       <% if(!COMP_INDUSTRY.equals("Education")){%>
      <div class="col-sm-4"> 
        <INPUT name="SHIP_EMAIL" id="SHIP_EMAIL" type="TEXT" value="<%=shipEmail%>" size="50"
			MAXLENGTH="50" class="form-control">
      </div>
      <%} %>
    </div>
    
    <div class="form-group">
      <label class="control-label col-form-label col-sm-2 required" for="Country">Country</label>
      <div class="col-sm-4">  
      <SELECT class="form-control" data-toggle="dropdown" data-placement="right" onchange="OnCountry(this.value)" id="COUNTRY_CODE" name="COUNTRY_CODE" value="<%=sCountry%>" style="width: 100%">
				<OPTION style="display:none;">Select Country</OPTION>
				<%
		   MasterUtil _MasterUtil=new  MasterUtil();
		   ArrayList ccList =  _MasterUtil.getCountryList("",plant,region);
			for(int i=0 ; i<ccList.size();i++)
      		 {
				Map m=(Map)ccList.get(i);
				String vCOUNTRYNAME = (String)m.get("COUNTRYNAME");
				String vCOUNTRY_CODE = (String)m.get("COUNTRY_CODE"); %>
		        <option  value='<%=vCOUNTRY_CODE%>' ><%=vCOUNTRYNAME%> </option>		          
		        <%
       			}
			 %></SELECT>       
       <%-- <INPUT name="COUNTRY" type="TEXT" value="<%=sCountry%>"
			size="50" MAXLENGTH=50 class="form-control"> --%>
      </div>
       <% if(!COMP_INDUSTRY.equals("Education")){%>
        <div class="col-sm-4">
       <SELECT class="form-control" data-toggle="dropdown" data-placement="right" onchange="SHIP_OnCountry(this.value)" id="SHIP_COUNTRY_CODE" name="SHIP_COUNTRY_CODE" value="<%=shipCountry%>" style="width: 100%">
				<OPTION style="display:none;">Select Country</OPTION>
				<%
		   MasterUtil _MasterUtil1=new  MasterUtil();
		   ArrayList ccList1 =  _MasterUtil1.getCountryList("",plant,region);
			for(int i=0 ; i<ccList1.size();i++)
      		 {
				Map m=(Map)ccList1.get(i);
				String vCOUNTRYNAME = (String)m.get("COUNTRYNAME");
				String vCOUNTRY_CODE = (String)m.get("COUNTRY_CODE"); %>
		        <option  value='<%=vCOUNTRY_CODE%>' ><%=vCOUNTRYNAME%> </option>		          
		        <%
       			}
			 %></SELECT>
			 <%-- <INPUT name="COUNTRY" type="TEXT" value="<%=sCountry%>"
			size="50" MAXLENGTH=50 class="form-control"> --%>
      </div>
      <%} %>
    </div>
       <div class="form-group">
      <label class="control-label col-form-label col-sm-2" for="Unit No">Unit No.</label>
      <div class="col-sm-4">  
        <INPUT name="ADDR1" id="ADDR1" type="TEXT" value="<%=sAddr1%>" size="50"
			MAXLENGTH=50 class="form-control">
      </div>
       <% if(!COMP_INDUSTRY.equals("Education")){%>
      <div class="col-sm-4">  
        <INPUT name="SHIP_ADDR1" id="SHIP_ADDR1" type="TEXT" value="<%=shipAddr1%>" size="50"
			MAXLENGTH=50 class="form-control">
      </div>
      <%} %>
    </div>
    <div class="form-group">
      <label class="control-label col-form-label col-sm-2" for="Building">Building</label>
      <div class="col-sm-4">
        <INPUT name="ADDR2" id="ADDR2" type="TEXT" value="<%=sAddr2%>" size="50"
			MAXLENGTH=50 class="form-control">
      </div>
       <% if(!COMP_INDUSTRY.equals("Education")){%>
       <div class="col-sm-4">
        <INPUT name="SHIP_ADDR2" id="SHIP_ADDR2"type="TEXT" value="<%=shipAddr2%>" size="50"
			MAXLENGTH=50 class="form-control">
      </div>
      <%} %>
    </div>
    <div class="form-group">
      <label class="control-label col-form-label col-sm-2" for="Street">Street</label>
      <div class="col-sm-4">
        <INPUT name="ADDR3" id="ADDR3" type="TEXT" value="<%=sAddr3%>" size="50"
			MAXLENGTH=50 class="form-control">
      </div>
       <% if(!COMP_INDUSTRY.equals("Education")){%>
      <div class="col-sm-4">
        <INPUT name="SHIP_ADDR3" id="SHIP_ADDR3" type="TEXT" value="<%=shipAddr3%>" size="50"
			MAXLENGTH=50 class="form-control">
      </div>
      <%} %>
    </div>
    <div class="form-group">
      <label class="control-label col-form-label col-sm-2" for="City">City</label>
      <div class="col-sm-4"> 
                
        <INPUT name="ADDR4" id="ADDR4" type="TEXT" value="<%=sAddr4%>" size="50"
			MAXLENGTH=80  class="form-control">
      </div>
       <% if(!COMP_INDUSTRY.equals("Education")){%>
      <div class="col-sm-4"> 
        <INPUT name="SHIP_ADDR4" id="SHIP_ADDR4" type="TEXT" value="<%=shipAddr4%>" size="50"
			MAXLENGTH=80  class="form-control">
      </div>
      <%} %>
    </div>
    <div class="form-group">
      <label class="control-label col-form-label col-sm-2" for="State">State</label>
      <div class="col-sm-4">
      <SELECT class="form-control" data-toggle="dropdown" data-placement="right" id="STATE" name="STATE" value="<%=sState%>" style="width: 100%">
				<OPTION style="display:none;">Select State</OPTION>
				</SELECT>           
        <%-- <INPUT name="STATE" type="TEXT" value="<%=sState%>" size="50"
			MAXLENGTH=50 class="form-control"> --%>
      </div>
     <% if(!COMP_INDUSTRY.equals("Education")){%>
      <div class="col-sm-4">           
        <SELECT class="form-control" data-toggle="dropdown" data-placement="right" id="SHIP_STATE" name="SHIP_STATE" value="<%=shipState%>" style="width: 100%">
				<OPTION style="display:none;">Select State</OPTION>
				</SELECT>
				<%-- <INPUT name="STATE" type="TEXT" value="<%=sState%>" size="50"
			MAXLENGTH=50 class="form-control"> --%>
      </div>
    <%} %>
    </div>
    
    <div class="form-group">
      <label class="control-label col-form-label col-sm-2" for="Postal Code">Postal Code</label>
      <div class="col-sm-4">
        <INPUT name="ZIP" id="ZIP" type="TEXT" value="<%=sZip%>" size="50"
			MAXLENGTH=10 class="form-control">
      </div>
       <% if(!COMP_INDUSTRY.equals("Education")){%>
      <div class="col-sm-4">
        <INPUT name="SHIP_ZIP" id="SHIP_ZIP" type="TEXT" value="<%=shipZip%>" size="50"
			MAXLENGTH=10 class="form-control">
      </div>
      <%} %>
    </div>
		     
        </div>
        
        <div class="tab-pane active" id="other">
        <br>
        <% if(COMP_INDUSTRY.equals("Education")){%>
        <div class="form-group" style="display:none;">
			<label class="control-label col-form-label col-sm-2 required">Tax Treatment</label>
			<div class="col-sm-4 ac-box">				
				<SELECT class="form-control" data-toggle="dropdown" data-placement="right" onchange="OnTaxChange(this.value)" id="TAXTREATMENT" name="TAXTREATMENT" value="<%=sTAXTREATMENT%>" style="width: 100%">
				<OPTION style="display:none;">Select Tax Treatment</OPTION>
				<%
		   _MasterUtil=new  MasterUtil();
		   ccList =  _MasterUtil.getTaxTreatmentList("",plant,"");
			for(int i=0 ; i<ccList.size();i++)
      		 {
				Map m=(Map)ccList.get(i);
				String vTAXTREATMENT = (String)m.get("TAXTREATMENT"); %>
		        <option  value='<%=vTAXTREATMENT%>' ><%=vTAXTREATMENT %> </option>		          
		        <%
       			}
			 %></SELECT>
			</div>
		</div>
		<%}else{ %>
        <div class="form-group">
			<label class="control-label col-form-label col-sm-2 required">Tax Treatment</label>
			<div class="col-sm-4 ac-box">				
				<SELECT class="form-control" data-toggle="dropdown" data-placement="right" onchange="OnTaxChange(this.value)" id="TAXTREATMENT" name="TAXTREATMENT" value="<%=sTAXTREATMENT%>" style="width: 100%">
				<OPTION style="display:none;">Select Tax Treatment</OPTION>
				<%
		   _MasterUtil=new  MasterUtil();
		   ccList =  _MasterUtil.getTaxTreatmentList("",plant,"");
			for(int i=0 ; i<ccList.size();i++)
      		 {
				Map m=(Map)ccList.get(i);
				String vTAXTREATMENT = (String)m.get("TAXTREATMENT"); %>
		        <option  value='<%=vTAXTREATMENT%>' ><%=vTAXTREATMENT %> </option>		          
		        <%
       			}
			 %></SELECT>
			</div>
		</div>
		<%} %>
         <% if(COMP_INDUSTRY.equals("Education")){%>
        <div class="form-group" style="display:none;">
         <label class="control-label col-form-label col-sm-2" for="TRN No" id="TaxLabel"></label>
      <div class="col-sm-4">
       <INPUT name="RCBNO" type="TEXT" class="form-control"
			value="<%=sRcbno%>" size="50" MAXLENGTH="50" >
      </div>
    </div>
    <%}else{ %>
        <div class="form-group">
         <label class="control-label col-form-label col-sm-2" for="TRN No" id="TaxLabel"></label>
      <div class="col-sm-4">
       <INPUT name="RCBNO" type="TEXT" class="form-control"
			value="<%=sRcbno%>" size="50" MAXLENGTH="50" >
      </div>
    </div>
    <%} %>
        
        <div class="form-group" style="display: none;">
      	<label class="control-label col-form-label col-sm-2" for="Opening Balance">Opening Balance</label>
      	<div class="col-sm-4">  
        <INPUT name="OPENINGBALANCE" type="hidden" value="<%=new java.math.BigDecimal(OPENINGBALANCE).toPlainString()%>" onkeypress="return isNumberKey(event,this,4)"	size="50" MAXLENGTH=50 class="form-control">
      	</div>
    	</div>
        
   <div class="form-group">  
        <label class="control-label col-form-label col-sm-2" for="Payment Terms">Payment Mode</label>
      <div class="col-sm-4">
	       <input id="PAYTERMS" name="PAYTERMS" class="form-control" type="text" value="<%=sPayTerms%>"  MAXLENGTH=100 placeholder="Select Payment Mode">
		    	<span class="select-icon" onclick="$(this).parent().find('input[name=\'PAYTERMS\']').focus()">
						 	<i class="glyphicon glyphicon-menu-down"></i></span>
  		</div>
  	</div> 
  	 <% if(COMP_INDUSTRY.equals("Education")){%>
  	<div class="form-group" style="display:none;">
  	<label class="control-label col-form-label col-sm-2">Payment Terms</label>
       			<div class="col-sm-4">
       				<input type="text" class="form-control" id="payment_terms"	name="payment_terms" value="<%=sPayMentTerms%>" placeholder="Select Payment Terms">
					<span class="select-icon" onclick="$(this).parent().find('input[name=\'payment_terms\']').focus()">
					<i class="glyphicon glyphicon-menu-down"></i></span>
				</div>
				<div class="form-inline">
    		<label class="control-label col-form-label col-sm-1">Days</label>
    	    <input name="PMENT_DAYS" type="TEXT" value="<%=sPayInDays%>" class="form-control" size="5" MAXLENGTH=10 readonly >
  			</div>
    	</div>
  <%}else{ %>
  	<div class="form-group">
  	<label class="control-label col-form-label col-sm-2">Payment Terms</label>
       			<div class="col-sm-4">
       				<input type="text" class="form-control" id="payment_terms"	name="payment_terms" value="<%=sPayMentTerms%>" placeholder="Select Payment Terms">
					<span class="select-icon" onclick="$(this).parent().find('input[name=\'payment_terms\']').focus()">
					<i class="glyphicon glyphicon-menu-down"></i></span>
				</div>
				<div class="form-inline">
    		<label class="control-label col-form-label col-sm-1">Days</label>
    	    <input name="PMENT_DAYS" type="TEXT" value="<%=sPayInDays%>" class="form-control" size="5" MAXLENGTH=10 readonly >
  			</div>
    	</div>
  <%} %>
   <% if(COMP_INDUSTRY.equals("Education")){%>
  <div class="form-group" style="display:none;">
      <label class="control-label col-form-label col-sm-2" for="Credit Limit">Credit Limit</label>
      <div class="col-sm-4">  
      <INPUT name="CREDITLIMIT" type="TEXT" value="<%=new java.math.BigDecimal(CREDITLIMIT).toPlainString()%>" size="50" MAXLENGTH=50  class="form-control" onkeypress="return isNumberKey(event,this,4)">
      </div>
      <div class="form-inline">
      <div class="col-sm-2"></div>
      </div>
    </div>   
    <%}else{ %>
  <div class="form-group">
      <label class="control-label col-form-label col-sm-2" for="Credit Limit">Credit Limit</label>
      <div class="col-sm-4">  
      <INPUT name="CREDITLIMIT" type="TEXT" value="<%=new java.math.BigDecimal(CREDITLIMIT).toPlainString()%>" size="50" MAXLENGTH=50  class="form-control" onkeypress="return isNumberKey(event,this,4)">
      </div>
      <div class="form-inline">
      <div class="col-sm-2"></div>
      </div>
    </div>   
   
   <div class="form-group">
      <label class="control-label col-form-label col-sm-2"></label>
      <div class="col-sm-4">  
      <input type = "radio" name="CREDIT_LIMIT_BY" value="DAILY" <%if(CREDIT_LIMIT_BY.equalsIgnoreCase("DAILY")) {%>checked <%}%>/>By Daily
      <input type = "radio" name="CREDIT_LIMIT_BY" value="MONTHLY"<%if(CREDIT_LIMIT_BY.equalsIgnoreCase("MONTHLY")) {%>checked <%}%>/>By Monthly
      <input type = "radio" name="CREDIT_LIMIT_BY" value="NOLIMIT" <%if(CREDIT_LIMIT_BY.equalsIgnoreCase("NOLIMIT")) {%>checked <%}%>/>No Credit Limit
      </div>
  </div> 
    <%} %>
  
  <div class="form-group">
      <label class="control-label col-form-label col-sm-2" for="Credit Limit">Add on Cost</label>
      <div class="col-sm-4">  
      <INPUT name="CUST_ADDONCOST" id="CUST_ADDONCOST" type="TEXT" value="<%=new java.math.BigDecimal(CUST_ADDONCOST).toPlainString()%>" size="50" MAXLENGTH=50  class="form-control"   onkeypress="return isNumberKey(event,this,4)">
      </div>
      <div class="form-inline">
		<div class="col-sm-6" style="right: 15px;">  
      	<input type="radio" name="ADDONCOSTTYPE" <%if(ADDONCOSTTYPE.equalsIgnoreCase("0")) {%>checked <%}%> onchange="addoncosttype('BYCOST')" value="0" id="byCost">By Cost &nbsp;&nbsp;
      	<input type="radio" name="ADDONCOSTTYPE" <%if(ADDONCOSTTYPE.equalsIgnoreCase("1")) {%>checked <%}%> onchange="addoncosttype('BYPERCENTAGE')" value="1" id="byPercentage">By Percentage &nbsp;&nbsp;
        </div>
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
      	<label class="control-label col-form-label col-sm-2" for="Linkedin">Skype Id</label>
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
        
        <div class="tab-pane fade" id="bank_cus">
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
<%-- 				<SELECT class="form-control" data-toggle="dropdown" data-placement="right" onchange="OnBank(this.value)" id="BANKNAME" name="BANKNAME" value="<%=sBANKNAME%>" style="width: 100%"> --%>
<!-- 				<OPTION style="display:none;">Select Bank</OPTION> -->
	<INPUT name="BANKNAME" type = "TEXT" value="<%=sBANKNAME%>" id="BANKNAME" class="form-control"   placeholder="BANKNAME">
	 <span class="select-icon"  onclick="$(this).parent().find('input[name=\'BANKNAME\']').focus()">
	<i class="glyphicon glyphicon-menu-down"></i></span>
	
  	</div>
				<%-- <%
		    _MasterUtil=new  MasterUtil();
		    ccList =  _MasterUtil.getBankList("",plant);
			for(int i=0 ; i<ccList.size();i++)
      		 {
				Map m=(Map)ccList.get(i);
				String vNAME = (String)m.get("NAME"); %>
		        <option  value='<%=vNAME%>' ><%=vNAME %> </option>		          
		        <%
       			}
			 %> --%>
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
        
        <div class="tab-pane fade" id="remark">
        <br>
       <div class="form-group">
      <label class="control-label col-form-label col-sm-2" for="Remarks">Remarks</label>
      <div class="col-sm-4"> 
               
        <%-- <INPUT name="REMARKS" type="TEXT" value="<%=sRemarks%>"	size="50" MAXLENGTH=100 class="form-control "> --%>
        <textarea  class="form-control" name="REMARKS"   MAXLENGTH=100><%=sRemarks%></textarea>
      </div>
    </div>		     
    </div>
    
    <div class="tab-pane fade" id="user_cus">
        <br>
        
        <div class="row" style="margin: 0px; width: 95%; margin-left: 15px;">
					<table class="table table-bordered line-item-table user-table">
						<thead>
							<tr>
								<th>User Name</th>
								<th>User Phone No</th>
								<th>User Email</th>
								<th>User Id</th>
								<th>Password</th>
								<th>Customer App Manager Access</th>
							</tr>
						</thead>
						<%if(!customeruserlist.isEmpty()){ %>
						<tbody>
						<%for(int i =0; i<customeruserlist.size(); i++) {
							Map arrCurrLine = (Map)customeruserlist.get(i);
							 String decryptpassword = "";
							 String sPassword=(String)arrCurrLine.get("PASSWORD"); 
							 String MANAGERAPP = (String)arrCurrLine.get("ISMANAGERAPPACCESS");
						      if(sPassword.length() > 0){
						      	decryptpassword = eb.decrypt(sPassword);
						      }
						%>
						<tr>
								<td class="text-center">
									<input type="hidden" name="customeruserid" value="<%= (String)arrCurrLine.get("ID") %>">
									<input class="form-control text-left" name="USER_NAME" type="text" placeholder="Enter User Name" maxlength="50" value="<%= (String)arrCurrLine.get("USER_NAME") %>" autocomplete="off" ></td>
									<td class="text-center">
									<input class="form-control text-left" name="USER_HPNO" type="text" placeholder="Enter User Phone No" maxlength="30" onkeypress="return isNumber(event)" value="<%= (String)arrCurrLine.get("USER_HPNO") %>" autocomplete="off" ></td>
									<td class="text-center">
									<input class="form-control text-left" name="USER_EMAIL" type="text" placeholder="Enter User Email" maxlength="50" value="<%= (String)arrCurrLine.get("USER_EMAIL") %>" autocomplete="off" ></td>
								<td class="text-center">
									<input class="form-control text-left" type="text" name="USER_ID" placeholder="Enter User id" maxlength="100" value="<%= (String)arrCurrLine.get("USER_ID") %>" autocomplete="off"  onchange="checkuser(this.value)">
								</td>
								<td class="text-center grey-bg" style="position:relative;">
								<!-- <span class="glyphicon glyphicon-remove-circle user-action" aria-hidden="true"></span>  -->
								<input  name="PASSWORD" class="form-control text-left password-field" maxlength="60" placeholder="Enter Password" value="<%= decryptpassword%>" type="password" autocomplete="off">
								<span class="input-group-btn phideshow" onclick="javascript:viewpassword(this);return false;">
					            <button class="btn btn-default reveal" type="button" style="background: white;"><i class="fa fa-fw fa-eye" id="peye" style="color: #23527c;"></i></button>
					          	</span>	
								</td>
								<%if(i>0){ %>
							<td class="text-center">
									<span class="glyphicon glyphicon-remove-circle user-action" aria-hidden="true"></span>
									<input type="hidden" name="MANAGER_APP_VAL" value="<%= MANAGERAPP %>"/>
									<input type="checkbox" name="MANAGER_APP" <%if(MANAGERAPP.equals("1")){%> checked <%}%> onclick="checkManagerApp(this)"/>
								</td>
										<%} else {%>
									<td class="text-center">
									<input  name="MANAGER_APP_VAL" type="hidden"  value="<%= MANAGERAPP %>" >
									<input type="checkbox"  name="MANAGER_APP" <%if(MANAGERAPP.equals("1")){%> checked <%}%> onclick="checkManagerApp(this)"/>
								</td>			
										<%} %>
							</tr>
						<%} %>
						</tbody>
						<%}else{%>
						<tbody>
							<tr>
								<td class="text-center">
									<input type="hidden" name="customeruserid" value="0">
									<input class="form-control text-left" name="USER_NAME" type="text" placeholder="Enter User Name" maxlength="50" autocomplete="off" ></td>
									<td class="text-center">
									<input class="form-control text-left" name="USER_HPNO" type="text" placeholder="Enter User Phone No" maxlength="30" onkeypress="return isNumber(event)" autocomplete="off" ></td>
									<td class="text-center">
									<input class="form-control text-left" name="USER_EMAIL" type="text" placeholder="Enter User Email" maxlength="50" autocomplete="off" ></td>
								<td class="text-center">
									<input class="form-control text-left" type="text" name="USER_ID" placeholder="Enter User id" maxlength="100" autocomplete="off" onchange="checkuser(this.value)">
								</td>								
								<td class="text-center">
									<input  name="PASSWORD" class="form-control text-left password-field" maxlength="60" placeholder="Enter Password" type="password" autocomplete="off">
									<span class="input-group-btn phideshow" onclick="javascript:viewpassword(this);return false;">
						            <button class="btn btn-default reveal" type="button" style="background: white;"><i class="fa fa-fw fa-eye" id="peye" style="color: #23527c;"></i></button>
						          	</span>
								</td>
								<td class="text-center">
									  <input type="hidden" name="MANAGER_APP_VAL" value="0" />
									  <input type="checkbox" value="1" name="MANAGER_APP" id="MANAGER_APP" onclick="checkManagerApp(this)"/>
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
								onclick="addUserRow()">+ Add another User Detail</a>
						</div>
					</div>
			</div>
        
     </div>
        		             <div class="tab-pane fade" id="attachfiles">
        <br>
				<div class="form-inline">
					<label for="email">Attach Files(s)</label>
					<div class="attch-section">
						<input type="file" class="form-control input-attch" id="customerAttch" name="file" multiple="true">
						<div class="input-group">
							<svg version="1.1" id="Layer_1" xmlns="http://www.w3.org/2000/svg" x="0" y="0" viewBox="0 0 512 512" xml:space="preserve" class="icon icon-xs align-text-top action-icons input-group-addon" style="height:30px;display:inline-block;color: #c63616;"><path d="M262.4 512c-35.7 0-64.2-10.5-84.9-31.4-24.7-24.9-37.1-63.8-36.8-115.6.2-32.6-.3-248.7-1.4-268.1-.9-15.9 4.8-41.6 23.6-61.5 11.4-12 32-26.3 66-26.3 30.3 0 55.1 15.7 69.8 44.2 10.1 19.7 12.2 38.8 12.4 40.9l.1.9v.9l.5 236.9v.5c-1 19.2-15.9 47.6-53 47.6h-.7c-39.1-.4-53.7-30.4-56-46.2l-.2-1.3V179.6c0-10.2 8.3-18.5 18.5-18.5s18.5 8.3 18.5 18.5v150.5c.4 1.5 1.4 4.4 3.6 7.2s6.2 6.5 16 6.6c9.2.1 12.4-3.2 14.1-6 1.6-2.6 2.2-5.6 2.3-6.3l-.7-234.5c-.4-3-2.4-15.6-8.8-27.6-8.3-15.7-20.2-23.3-36.4-23.3-16.7 0-29.8 5-39.1 14.8-10.7 11.3-14 26.6-13.6 34 1.2 21.6 1.6 244.3 1.4 270.3-.2 41.6 8.5 71.7 26 89.3 13.5 13.6 33.2 20.4 58.7 20.4 17.2 0 31.8-5.9 43.5-17.7 18.9-18.9 30.1-53.4 30-92.2 0-19.6-.1-193.2-.1-250.9 0-10.2 8.3-18.5 18.5-18.5s18.5 8.3 18.5 18.5c0 57.6.1 231.2.1 250.8.1 49.1-14.8 92.3-40.8 118.4-18.6 18.7-42.7 28.6-69.6 28.6z"></path></svg>
							<button type="button" class="btn btn-sm btn-attch">Upload File</button>
						</div>
						
					</div>
				</div>
				<%if(customerattachlist.size()>0){ %>
						<div id="customerAttchNote">
							<small class="text-muted"><div class="attachclass"><a><%=customerattachlist.size()%> files Attached</a>
									<div class="tooltiptext" style="width: 30%">
										
										<%for(int i =0; i<customerattachlist.size(); i++) {   
									  		Map attach=(Map)customerattachlist.get(i); %>
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
						<div id="customerAttchNote">
							<small class="text-muted">  You can upload a maximum of 5 files, 2MB each  </small>
						</div>
						<%} %>
        </div>
        </div>
        </div>

    <div class="form-group">        
     <div class="col-sm-offset-4 col-sm-8">
     <!-- <button type="button" class="Submit btn btn-default" onClick="window.location.href='../home'">Back</button>&nbsp;&nbsp; -->
      <!-- <button type="button" class="Submit btn btn-default" onClick="{backNavigation('settings.jsp','SA');}"><b>Back</b></button>&nbsp;&nbsp; -->
      	<button type="button" class="Submit btn btn-default" onClick="onNew();"<%=sNewEnb%>>Clear</button>&nbsp;&nbsp;
      	<button type="button" class="Submit btn btn-default" onClick="return onDelete();" <%=sDeleteEnb%>>Delete</button>&nbsp;&nbsp;
      	<button type="button" class="btn btn-success" data-toggle="modal" data-target="#myModal" onClick="onUpdate();"<%=sUpdateEnb%>>Save</button>&nbsp;&nbsp;
      	
      </div>
    </div>
  </form>
</div>
</div>
</div>

 <!-- modal page-->
<%@include file="../jsp/newBankModal.jsp"%>
<%@include file="../jsp/newTransportModeModal.jsp"%>
<%@include file="../jsp/newCurrencyModal.jsp"%>
<%@include file="../jsp/newCustomerTypeModal.jsp"%> <!-- thansith Add for Customertype -->
<%@include file="../jsp/newPaymentTermsModal.jsp"%> <!-- thansith for add paymentterms -->
<%@include file="newPaymentModeModal.jsp"%><!-- thansith for add paymentmode -->
<input type="hidden" name="Basecurrency" id="Basecurrency" style="display: none;" value=<%=curency%>>
 <script>
 $(document).ready(function(){

	 /* Bank Name Auto Suggestion */
		$('#BANKNAME').typeahead({
			  hint: true,
			  minLength:0,  
			  searchOnFocus: true
			},
			{
			  display: 'NAME',  
			  source: function (query, process,asyncProcess) {
				  var urlStr = "/track/BankServlet";
						$.ajax( {
						type : "POST",
						url : urlStr,
						async : true,
						data : {
							PLANT :"<%=plant%>",
							action : "GET_ORDER_NO_FOR_AUTO_SUGGESTION",
							NAME : query
						},
						dataType : "json",
						success : function(data) {
							return asyncProcess(data.orders);
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
			    	  return '<p onclick="document.form.BRANCH.value = \''+data.BRANCH_NAME+'\'">'+data.NAME+'</p>';
				}
			  }

			}).on('typeahead:render',function(event,selection){
				var menuElement = $(this).parent().find(".tt-menu");
				var top = menuElement.height()+35;
				top+="px";	
				if(menuElement.next().hasClass("footer")){
					menuElement.next().remove();  
				}
				menuElement.after( '<div class="employeeAddBtn footer"  data-toggle="modal" data-target="#bankMstModal"><a href="#"> + New Bank</a></div>');
				menuElement.next().width(menuElement.width());
				menuElement.next().css({ "top": top,"padding":"3px 20px" });
				if($(this).parent().find(".tt-menu").css('display') != "block")
					menuElement.next().hide();
				
			}).on('typeahead:open',function(event,selection){
				var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
				element.toggleClass("glyphicon-menu-up",true);
				element.toggleClass("glyphicon-menu-down",false);
				$('.employeeAddBtn').show();
			}).on('typeahead:close',function(){
				setTimeout(function(){ $('.employeeAddBtn').hide();}, 150);	
				var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
				element.toggleClass("glyphicon-menu-up",false);
				element.toggleClass("glyphicon-menu-down",true);
			});


		/* Customer Type Auto Suggestion */
		$('#CUSTOMER_TYPE_DESC').typeahead({
			  hint: true,
			  minLength:0,  
			  searchOnFocus: true
			},
			{
			  display: 'CUSTOMER_TYPE_DESC',  
			  source: function (query, process,asyncProcess) {
				var urlStr = "/track/MasterServlet";
				$.ajax( {
				type : "POST",
				url : urlStr,
				async : true,
				data : {
					PLANT :  "<%=plant%>",
					ACTION : "GET_CUSTOMER_TYPE_DATA",
					QUERY : query
				},
				dataType : "json",
				success : function(data) {
					return asyncProcess(data.CUSTOMER_TYPE_MST);
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
					return '<div onclick="document.form.CUSTOMER_TYPE_ID.value = \''+data.CUSTOMER_TYPE_ID+'\'"><p class="item-suggestion">' + data.CUSTOMER_TYPE_ID + '</p><br/><p class="item-suggestion">DESC: '+data.CUSTOMER_TYPE_DESC+'</p></div>';
				}
			  }
			}).on('typeahead:render',function(event,selection){
				var menuElement = $(this).parent().find(".tt-menu");
				var top = menuElement.height()+35;
				top+="px";	
				if(menuElement.next().hasClass("footer")){
					menuElement.next().remove();  
				}
				menuElement.after( '<div class="CUSTOMER_TYPE_IDAddBtn footer"  data-toggle="modal" data-target="#CustTypeModal"> <a href="#"> + Add Customer Group</a></div>');
				menuElement.next().width(menuElement.width());
				menuElement.next().css({ "top": top,"padding":"3px 20px" });
				if($(this).parent().find(".tt-menu").css('display') != "block")
					menuElement.next().hide();
			  
			}).on('typeahead:open',function(event,selection){
				$('.CUSTOMER_TYPE_IDAddBtn').show();
				var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
				element.toggleClass("glyphicon-menu-up",true);
				element.toggleClass("glyphicon-menu-down",false);
				var menuElement = $(this).parent().find(".tt-menu");
				menuElement.next().show();
			}).on('typeahead:close',function(){
				setTimeout(function(){ $('.CUSTOMER_TYPE_IDAddBtn').hide();}, 180);
				var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
				element.toggleClass("glyphicon-menu-up",false);
				element.toggleClass("glyphicon-menu-down",true);
				
			}).on('typeahead:change',function(event,selection){
				if($(this).val() == "")
					document.form.CUSTOMER_TYPE_ID.value = "";
			});

		//transport
		$('#transport').typeahead({
			  hint: true,
			  minLength:0,  
			  searchOnFocus: true
			},
			{
			  display: 'TRANSPORT_MODE',  
			  source: function (query, process,asyncProcess) {
				  	var urlStr = "../MasterServlet";
					$.ajax({
						type : "POST",
						url : urlStr,
						async : true,
						data : {				
							ACTION : "GET_TRANSPORT_LIST",
							QUERY : query
						},
						dataType : "json",
						success : function(data) {
							return asyncProcess(data.TRANSPORTMODE);
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
			    return '<p>' + data.TRANSPORT_MODE + '</p>';
				}
			  }
			}).on('typeahead:render',function(event,selection){
				var menuElement = $(this).parent().find(".tt-menu");
				var top = menuElement.height()+35;
				top+="px";	
				if(menuElement.next().hasClass("footer")){
					menuElement.next().remove();  
				}
				menuElement.after( '<div class="transportAddBtn footer"  data-toggle="modal" data-target="#transportModal"><a href="#"> + New Transport </a></div>');
				menuElement.next().width(menuElement.width());
				menuElement.next().css({ "top": top,"padding":"3px 20px" });
				if($(this).parent().find(".tt-menu").css('display') != "block")
					menuElement.next().hide();
			  
			}).on('typeahead:open',function(event,selection){
				$('.transportAddBtn').show();
				var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
				element.toggleClass("glyphicon-menu-up",true);
				element.toggleClass("glyphicon-menu-down",false);    
			}).on('typeahead:close',function(){
				setTimeout(function(){ $('.transportAddBtn').hide();}, 180);	
				var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
				element.toggleClass("glyphicon-menu-up",false);
				element.toggleClass("glyphicon-menu-down",true);
			}).on('typeahead:select',function(event,selection){
				$("input[name=TRANSPORTID]").val(selection.ID);
			});
		/* Payment Mode Auto Suggestion */
		$("#PAYTERMS").typeahead({
			  hint: true,
			  minLength:0,  
			  searchOnFocus: true
			},
			{	  
			  display: 'PAYMENTMODE',  
			  source: function (query, process,asyncProcess) {
					var urlStr = "/track/PaymentModeMst";
					$.ajax( {
					type : "POST",
					url : urlStr,
					async : true,
					data : {
						PLANT : "<%=plant%>",
						action : "GET_PAYMENT_MODE_LIST",
						QUERY : query
					},
					dataType : "json",
					success : function(data) {
						return asyncProcess(data.PAYMENTMODE);
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
					return '<p>' + data.PAYMENTMODE + '</p>';
				}
			  }
			}).on('typeahead:render',function(event,selection){
				var menuElement = $(this).parent().find(".tt-menu");
				var top = menuElement.height()+35;
				top+="px";	
				if(menuElement.next().hasClass("footer")){
					menuElement.next().remove();  
				}
				menuElement.after( '<div class="accountAddBtn footer"  data-toggle="modal" data-target="#paymentModeModal"><a href="#"> + New Payment Mode</a></div>');
				menuElement.next().width(menuElement.width());
				menuElement.next().css({ "top": top,"padding":"3px 20px" });
				if($(this).parent().find(".tt-menu").css('display') != "block")
					menuElement.next().hide();	  
			}).on('typeahead:open',function(event,selection){
				var element = $(this).parent().parent().find('.pay-select-icon').find('.glyphicon');
				element.toggleClass("glyphicon-menu-up",true);
				element.toggleClass("glyphicon-menu-down",false);
				var menuElement = $(this).parent().find(".tt-menu");
				menuElement.next().show();
			}).on('typeahead:close',function(){
				var element = $(this).parent().parent().find('.pay-select-icon').find('.glyphicon');
				element.toggleClass("glyphicon-menu-up",false);
				element.toggleClass("glyphicon-menu-down",true);
				var menuElement = $(this).parent().find(".tt-menu");
				setTimeout(function(){ menuElement.next().hide();}, 180);
			});
		

		/* Payment Terms Auto Suggestion */
		$('#payment_terms').typeahead({
			  hint: true,
			  minLength:0,  
			  searchOnFocus: true
			},
			{
			  name: 'states',
			  display: 'PAYMENT_TERMS',  
			  source: function (query, process,asyncProcess) {
				  	var urlStr = "/track/PaymentTermsServlet";
					$.ajax( {
					type : "POST",
					url : urlStr,
					async : true,
					data : {
						PLANT : "<%=plant%>",
						ACTION : "GET_PAYMENT_TERMS_DETAILS",
						TERMS : query
					},
					dataType : "json",
					success : function(data) {
						return asyncProcess(data.terms);
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
			    return '<p>' + data.PAYMENT_TERMS + '</p>';
				}
			  }
			}).on('typeahead:render',function(event,selection){
				var menuElement = $(this).parent().find(".tt-menu");
				var top = menuElement.height()+35;
				top+="px";	
				if(menuElement.next().hasClass("footer")){
					menuElement.next().remove();  
				}
				menuElement.after( '<div class="payTermsAddBtn footer"  data-toggle="modal" data-target="#payTermsModal"><a href="#"> + Add Payment Terms</a></div>');
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
			}).on('typeahead:select',function(event,selection){
				$("input[name=PMENT_DAYS]").val(selection.NO_DAYS);
			});
				
		 

		/* To get the suggestion data for Currency */
		$("#CURRENCY").typeahead({
			hint: true,
			  minLength:0,  
			  searchOnFocus: true
			},
			{
			  display: 'CURRENCY',  
			  source: function (query, process,asyncProcess) {
				var urlStr = "../MasterServlet";
				$.ajax( {
					type : "POST",
					url : urlStr,
					async : true,
					data : {
						ACTION : "GET_CURRENCY_DATA",
						CURRENCY : query
					},
					dataType : "json",
					success : function(data) {
						return asyncProcess(data.CURRENCYMST);
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
				return '<div><p onclick="setCurrencyid(\''+data.CURRENCYID+'\',\''+data.CURRENCYUSEQT+'\')">'+data.CURRENCY+'</p></div>';
				}
			  }
		}).on('typeahead:render',function(event,selection){
			var menuElement = $(this).parent().find(".tt-menu");
			var top = menuElement.height()+35;
			top+="px";	
			if(menuElement.next().hasClass("footer")){
				menuElement.next().remove();  
			}
			menuElement.after( '<div class="CurrencyidAddBtn footer"  data-toggle="modal" data-target="#CurrencyModal"> <a href="#"> + Add Currency</a></div>');
			menuElement.next().width(menuElement.width());
			menuElement.next().css({ "top": top,"padding":"3px 20px" });
			if($(this).parent().find(".tt-menu").css('display') != "block")
				menuElement.next().hide();
		}).on('typeahead:open',function(event,selection){
			$('.CurrencyidAddBtn').show();
			var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
			element.toggleClass("glyphicon-menu-up",true);
			element.toggleClass("glyphicon-menu-down",false);
			var menuElement = $(this).parent().find(".tt-menu");
			menuElement.next().show();
		}).on('typeahead:close',function(){
			setTimeout(function(){ $('.CurrencyidAddBtn').hide();}, 180);
			var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
			element.toggleClass("glyphicon-menu-up",false);
			element.toggleClass("glyphicon-menu-down",true);
		}).on('typeahead:change',function(event,selection){
			if($(this).val() == "")
				$("input[name ='CURRENCYID']").val("");	
		});
		

		$("#NATIONALITY").typeahead({
	      	  hint: true,
	      	  minLength:0,  
	      	  searchOnFocus: true
	      	},
	      	{
	      	  name: 'nationality',
	      	  display: 'value',  
	      	  source: substringMatcher(nationality),
	      	  limit: 9999,
	      	  templates: {
	      	  empty: [
	      		  '<div style="padding:3px 20px">',
	      			'No results found',
	      		  '</div>',
	      		].join('\n'),
	      		suggestion: function(data) {
	      		return '<p>' + data.value + '</p>';
	      		}
	      	  }
	      	}).on('typeahead:render',function(event,selection){
	      		  
	      	}).on('typeahead:open',function(event,selection){
	      		
	      	}).on('typeahead:close',function(){
	      		
	      	});
      	
		 
	    $('[data-toggle="tooltip"]').tooltip();
	    if(document.form.SAVE_RED.value!="")
		{
		document.form.action  = "../customer/summary?PGaction=View&result=Customer Updated Successfully";
		document.form.submit();
		}
	});

 var nationality =   [{
     "year": "Singapore Citizen",
     "value": "Singapore Citizen",
     "tokens": [
       "Singapore Citizen"
     ]
   },
	  	{
	    "year": "Permanent Residence",
	    "value": "Permanent Residence",
	    "tokens": [
	      "Permanent Residence"
	    ]
	  },
	  {
	    "year": "Foreigner",
	    "value": "Foreigner",
	    "tokens": [
	      "Foreigner"
		    ]
		  }];
 		
 var substringMatcher = function(strs) {
	  return function findMatches(q, cb) {
	    var matches, substringRegex;
	    matches = [];
	    substrRegex = new RegExp(q, 'i');
	    $.each(strs, function(i, str) {
	      if (substrRegex.test(str.value)) {
	        matches.push(str);
	      }
	    });
	    cb(matches);
	  };
};
 $(document).ready(function(){
	    $('[data-toggle="tooltip"]').tooltip();

	    var region = '<%=region%>'; 
	    if(region == "GCC"){
			var x = document.getElementById("UEN");
			x.style.display = "none";
		}else if(region == "ASIA PACIFIC"){
			var x = document.getElementById("UEN");
			x.style.display = "block";
		}
	    
	    if(document.form.SAVE_REDELETE.value!=""){
	    	document.form.action  = "../customer/summary?PGaction=View&result=Customer Deleted Successfully";
	    	 document.form.submit();
		}
	});
$(document).ready(function(){
	if(document.form.EDIT_TAXTREATMENT.value!="")
	$("select[name ='TAXTREATMENT']").val(document.form.EDIT_TAXTREATMENT.value);
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
	if(document.form.EDIT_SHIP_COUNTRY.value!="")
	{
	$("select[name ='SHIP_COUNTRY_CODE']").val(document.form.EDIT_SHIP_COUNTRY.value);
	SHIP_OnCountry(document.form.EDIT_SHIP_COUNTRY.value);		
	if(document.form.EDIT_SHIP_STATE.value!="")
		{
		$("select[name ='SHIP_STATE']").val(document.form.EDIT_SHIP_STATE.value);
		   document.getElementById("SHIP_STATE").value = document.form.EDIT_SHIP_STATE.value;

		}
	}
	if(document.form.EDIT_TAXTREATMENT.value=="VAT Registered" ||document.form.EDIT_TAXTREATMENT.value=="VAT Registered - Desginated Zone")
	{
		$("#TaxLabel").addClass("required");
	}
	else
		$("#TaxLabel").removeClass("required");
	
	if(document.form.EDIT_BANKNAME.value!="")
		$("select[name ='BANKNAME']").val(document.form.EDIT_BANKNAME.value);
	
    $('[data-toggle="tooltip"]').tooltip();

    var  d = document.getElementById("TaxByLabel").value;
    document.getElementById('TaxLabel').innerHTML = d +" No.";	
});
function OnTaxChange(TAXTREATMENT)
{
	
	if(TAXTREATMENT=="VAT Registered"||TAXTREATMENT=="VAT Registered - Desginated Zone")
	{
		$("#TaxLabel").addClass("required");
	}
	else
		$("#TaxLabel").removeClass("required");
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


function setCurrencyid(CURRENCYID,CURRENCYUSEQT){	
	$('input[name ="CURRENCYID"]').val(CURRENCYID);
	
}

function bankCallback(data){
	if(data.STATUS="SUCCESS"){
		$("#BANKNAME").typeahead('val', data.BANK_NAME);
		  $("input[name ='BRANCH']").val(data.BANK_BRANCH);
		
	}
}

function transportCallback(data){
	if(data.STATUS="SUCCESS"){
		$("#transport").typeahead('val', data.transport);
		$("input[name=TRANSPORTID]").val(data.TRANSPORTID);
	}
}
function customertypeCallback(data){
	if(data.STATUS="SUCCESS"){
		$("input[name=CUSTOMER_TYPE_DESC]").typeahead ('val',data.CUSTOMER_TYPE_DESC);
		$("input[name ='CUSTOMER_TYPE_ID']").val(data.CUSTOMER_TYPE_ID);
	}
}

function paymentTypeCallback(payTermsData)
{
	if(payTermsData.STATUS="100"){
		$("input[name ='PAYTERMS']").typeahead('val', payTermsData.PAYMENTMODE);
		
	}
}
function payTermsCallback(payTermsData){
	if(payTermsData.STATUS="SUCCESS"){
		$("input[name ='payment_terms']").typeahead('val', payTermsData.PAYMENT_TERMS);
		$("input[name=PMENT_DAYS]").val(payTermsData.NO_DAYS);
	}
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
			//alert(myJSON);
			$('#STATE').empty();
			//$('#STATE').append('<OPTION style="display:none;">Select State</OPTION>');
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
function SHIP_OnCountry(Country)
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
			//alert(myJSON);
			$('#SHIP_STATE').empty();
			//$('#SHIP_STATE').append('<OPTION style="display:none;">Select State</OPTION>');
				$('#SHIP_STATE').append('<OPTION>Select State</OPTION>');
				 $.each(StateList, function (key, value) {
					 if(document.form.EDIT_SHIP_STATE.value==""){
						 
					 if(document.form.STATE.value==value.text)
						 	$('#SHIP_STATE').append('<option selected value="' + value.text + '">' + value.text + '</option>');
					 else
					   $('#SHIP_STATE').append('<option value="' + value.text + '">' + value.text + '</option>');
					 }
					 else{
						 if(document.form.EDIT_SHIP_STATE.value==value.text)
							$('#SHIP_STATE').append('<option selected value="' + value.text + '">' + value.text + '</option>');
						 else
						   $('#SHIP_STATE').append('<option value="' + value.text + '">' + value.text + '</option>');
						 }
					});
		}
	});	
	
}
$('select[name="COUNTRY_CODE"]').on('change', function(){
    var text = $("#COUNTRY_CODE option:selected").text();
    $("input[name ='COUNTRY']").val(text.trim());
    document.form.EDIT_STATE.value="";
});
$('select[name="SHIP_COUNTRY_CODE"]').on('change', function(){
    var text = $("#SHIP_COUNTRY_CODE option:selected").text();
    $("input[name ='SHIP_COUNTRY']").val(text.trim());
    document.form.EDIT_SHIP_STATE.value="";
});
$('select[name="STATE"]').on('change', function(){
$("#STATE option").removeAttr('selected');
document.form.EDIT_STATE.value="";
});
$('select[name="SHIP_STATE"]').on('change', function(){
	$("#SHIP_STATE option").removeAttr('selected');
	document.form.EDIT_SHIP_STATE.value="";
	});

function shippingAddress(){
	
	document.form.EDIT_SHIP_STATE.value = "";
	document.form.EDIT_SHIP_COUNTRY.value = "";
	
	if (document.getElementById("SameAsContactAddress").checked == true) {
		document.getElementById("SameAsContactAddress").value = "1";
		var contactname = document.getElementById("CONTACTNAME").value;
		var desgination = document.getElementById("DESGINATION").value;
		var workphone = document.getElementById("WORKPHONE").value;
		var country_code = document.getElementById("COUNTRY_CODE").value;
		SHIP_OnCountry(country_code);
		var scountry = document.form.COUNTRY.value;
		var hpno= document.getElementById("HPNO").value;
		var email = document.getElementById("EMAIL").value;
		var addr1 = document.getElementById("ADDR1").value;
		var addr2 = document.getElementById("ADDR2").value;
		var addr3 = document.getElementById("ADDR3").value;
		var addr4 = document.getElementById("ADDR4").value;
		var state = document.getElementById("STATE").value;
		var zip = document.getElementById("ZIP").value;
			     
		document.getElementById("SHIP_CONTACTNAME").value = contactname;
		document.getElementById("SHIP_DESGINATION").value = desgination;
		document.getElementById("SHIP_WORKPHONE").value = workphone;
		document.getElementById("SHIP_HPNO").value = hpno;
		document.getElementById("SHIP_EMAIL").value = email;
		document.getElementById("SHIP_COUNTRY_CODE").value = country_code;
		document.form.SHIP_COUNTRY.value = scountry;
		document.getElementById("SHIP_ADDR1").value = addr1;
		document.getElementById("SHIP_ADDR2").value = addr2;
		document.getElementById("SHIP_ADDR3").value = addr3;
		document.getElementById("SHIP_ADDR4").value = addr4;
		//document.getElementById("SHIP_STATE").value = state;
			$("select[name ='SHIP_STATE']").val(state);
		document.getElementById("SHIP_STATE").value = state;
		document.getElementById("SHIP_ZIP").value = zip;
				 
		}  
	else {
		document.getElementById("SameAsContactAddress").value = "0";
		document.getElementById("SHIP_CONTACTNAME").value = "";
		document.getElementById("SHIP_DESGINATION").value = "";
		document.getElementById("SHIP_WORKPHONE").value = "";
		document.getElementById("SHIP_HPNO").value = "`";
		document.getElementById("SHIP_EMAIL").value = "";
		document.getElementById("SHIP_COUNTRY_CODE").value = "Select Country";
		document.form.SHIP_COUNTRY.value ="";
		document.getElementById("SHIP_ADDR1").value = "";
		document.getElementById("SHIP_ADDR2").value = "";
		document.getElementById("SHIP_ADDR3").value = "";
		document.getElementById("SHIP_ADDR4").value = "";
		document.getElementById("SHIP_STATE").value = "Select State";
		document.getElementById("SHIP_ZIP").value = "";
	
		
		}
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
        $("#DOB").datepicker({dateFormat: 'dd/mm/yy',changeMonth: true, changeYear: true, yearRange: '-100:+0'});
    });
    $("#customerAttch").change(function(){
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
    				$("#customerAttchNote").html("<small class='text-muted'>  You can upload a maximum of 5 files, 2MB each  </small>");
    			}else{
    				$("#customerAttchNote").html(files +" files attached");
/*     				$("#customerAttchNote").append('<br><br><button onclick="add_attachments()">Upload Customer Attachments</button>'); */
    			}
    			
    		}
    	});
    function downloadFile(id,fileName)
    {
    	 var urlStrAttach = "/track/CustomerAttachmentServlet?Submit=downloadAttachmentById&attachid="+id;
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
    	var urlStrAttach = "/track/CustomerAttachmentServlet?Submit=removeAttachmentById&removeid="+id;	
    	$.ajax( {
    		type : "POST",
    		url : urlStrAttach,
    		success : function(data) {
    					window.location.reload();
    				}
    			});
    }

    function add_attachments(){
        var formData = new FormData($('#customerform')[0]);
        var userId= form.CUST_CODE.value;
    	if(userId){
        $.ajax({
            type: 'post',
            url: "/track/CustomerAttachmentServlet?Submit=add_attachments",
    	    data:  formData,//{key:val}
    	    contentType: false,
    	    processData: false,
            success: function (data) {
            	console.log(data);
            //	window.location.reload();
            	document.form.action  = "../jsp/maint_customer.jsp?action=UPDATE";
            	 document.form.submit();
            },
            error: function (data) {
                alert(data.responseText);
            }
        });
    	}else{
    		alert("Please enter Customer Id");
    	}
            return false; 
      }
    function addUserRow() {

    	var body = "";
    	body += '<tr>';
    	body += '<td class="text-center">';
    	body += '<input type="hidden"name="customeruserid" value="0">';
    	body += '<input class="form-control text-left" name="USER_NAME" type="text" placeholder="Enter User Name" maxlength="50" autocomplete="off" ></td>';
    	body += '<td class="text-center">';
    	body += '<input class="form-control text-left" name="USER_HPNO" type="text" placeholder="Enter User Phone No" maxlength="30" onkeypress="return isNumber(event)" autocomplete="off" ></td>';
    	body += '<td class="text-center">';
    	body += '<input class="form-control text-left" name="USER_EMAIL" type="text" placeholder="Enter User Email" maxlength="50" autocomplete="off" ></td>';
    	body += '<td class="text-center">';
    	body += '<input class="form-control text-left" type="text" name="USER_ID" placeholder="Enter User id" maxlength="100" autocomplete="off" onchange="checkuser(this.value)" >';
    	body += '</td>';
    	body += '<td class="text-center grey-bg" style="position:relative;">';  	
    	body += '<input  name="PASSWORD" class="form-control text-left password-field" maxlength="60" placeholder="Enter Password" type="password" autocomplete="off">';
    	body += '<span class="input-group-btn phideshow" onclick="javascript:viewpassword(this);return false;" >';
    	body += '<button class="btn btn-default reveal" type="button" style="background: white;"><i class="fa fa-fw fa-eye" id="peye" style="color: #23527c;"></i></button>';
    	body += '</span>';
    	body += '</td>';
    	body += '<td class="text-center grey-bg" style="position:relative;">';
    	body += '<input type="hidden" name="MANAGER_APP_VAL" value="0">';
    	body += '<input type="Checkbox" style="border:0;background=#dddddd"	name="MANAGER_APP" value="1" onclick="checkManagerApp(this)">';
    	body += '<span class="glyphicon glyphicon-remove-circle user-action" aria-hidden="true"></span>';    	
    	body += '</td>';
    	body += '</tr>';
    	$(".user-table tbody").append(body);
    }

    $(".user-table tbody").on('click', '.user-action', function() {
    	$(this).parent().parent().remove();
    });

    function checkuser(aCustCode)
    {	
    	 if(aCustCode=="" || aCustCode.length==0 ) {
    		alert("Enter User ID!");
    		return false;
    	 }else{
        	 
    		 /* var count = "0";
    	    	$("input[name ='USER_ID']").each(function() {
    	    		if($(this).val() == aCustCode){
    	    			count = "1";
    	    	    }
    	    	});
    	    	if(count == "0"){
    	    		$(obj).closest('tr').find("input[name ='USER_ID']").val(id);
    	    	}else{
    	    		alert("User Id alredy entered");
    	    		$(obj).closest('tr').remove();
    	    	} */
    	    	 
    		var urlStr = "/track/MasterServlet";
    		$.ajax( {
    			type : "POST",
    			url : urlStr,
    			async : true,
    			data : {
    				CUST_CODE : aCustCode,
                    USERID : "<%=sUserId%>",
    				PLANT : "<%=plant%>",
    				ACTION : "CUSTOMER_CHECKUSER"
    				},
    				dataType : "json",
    				success : function(data) {
    					if (data.status == "100") {
                                
    						alert("User ID Already Exists");
    						return false;
    					}
    					else
    						return true;
    				}
    });	
    		return true;
    }
    }

    function checkManagerApp(obj){
    	var manageapp = $(obj).closest('tr').find('input[name=MANAGER_APP_VAL]').val();
    	if(manageapp == 0){
    	$(obj).closest('tr').find('input[name=MANAGER_APP_VAL]').val(1);
    	}
    	else{
    	$(obj).closest('tr').find('input[name=MANAGER_APP_VAL]').val(0);
    	}
    }
  function viewpassword(obj){
	  var trid = $(obj).closest('tr').find("td:nth-child(5)").find('input[name=PASSWORD]').attr('type');
	  if (trid == 'password') {
		  $(obj).closest('tr').find("td:nth-child(5)").find('input[name=PASSWORD]').attr('type', 'text');
		  $(obj).closest('tr').find("td:nth-child(5)").find('i').attr('class', 'fa fa-fw fa-eye-slash');
      } else {
    	  $(obj).closest('tr').find("td:nth-child(5)").find('input[name=PASSWORD]').attr('type', 'password');
    	  $(obj).closest('tr').find("td:nth-child(5)").find('i').attr('class', 'fa fa-fw fa-eye');
      }
  }
  function currencyCallback(Data)
  {
  	if(Data.STATUS="100"){
  		$("input[name ='CURRENCY']").typeahead('val',Data.CURRENCYID);
  		$('input[name ="CURRENCYID"]').val(Data.CURRENCYID);
  		
  	}
  }

  document.getElementById('CUST_ADDONCOST').addEventListener('input', function() {
	    var addonCostValue = parseFloat(this.value);
	    var isPercentage = document.getElementById('byPercentage').checked;

	    if (isPercentage && addonCostValue >= 100) {
	        alert('Add on Cost should be less than 100 when selected by percentage.');
	        this.value = ""; 
	    }
	});


  function addoncosttype(obj){
  	var disPerValue =100;
  	var ADDONCOST = $('input[name ="CUST_ADDONCOST"]').val();
  	ADDONCOST =  parseFloat(ADDONCOST);
  				if(obj=='BYCOST'){
      				
  				}else{
  					if(disPerValue <= ADDONCOST){ 
  						alert('Add on Cost should be less than 100');
  						$("input[name=CUST_ADDONCOST]").val("");
  					}
  				}
  	}
</script>
<jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
</jsp:include>