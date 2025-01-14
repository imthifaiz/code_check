<%@page import="org.apache.commons.fileupload.FileItem"%>
<%@page import="org.apache.commons.fileupload.disk.DiskFileItemFactory"%>
<%@page import="org.apache.commons.fileupload.FileItemFactory"%>
<%@page import="org.apache.commons.fileupload.servlet.ServletFileUpload"%>
<%@ page contentType="text/html;charset=windows-1252"%>
<%@ page import="com.track.util.*"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.gates.*"%>
<%@ page import="com.track.dao.*"%>
<%@ page import="com.track.constants.*"%>
<%@ page import="java.util.*"%>

<%
String title = "Create Company";
%>
<%@include file="sessionCheck.jsp" %>
<jsp:include page="header2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
</jsp:include>

<script src="js/typeahead.jquery.js"></script>
<link rel="stylesheet" href="css/typeahead.css">
<link rel="stylesheet" href="css/accounting.css">
<link rel="stylesheet" href="css/bootstrap-datepicker.css">
<script type="text/javascript" src="js/general.js"></script>
<jsp:useBean id="countryNCurrencyDAO"  class="com.track.dao.CountryNCurrencyDAO" />
<meta http-equiv="Content-Type" content="text/html; charset=windows-1252">

<script type="text/javascript">

function popWin(URL) {
 subWin = window.open(URL, 'PRODUCT', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=800,height=400,left = 200,top = 184');
}

function checkDate()
{
	
   var startdate=document.form1.STARTDATE.value;
   var expirydate=document.form1.EXPIRYDATE.value;
   
   var substartdate=startdate.substring(0,2)+startdate.substring(3,5)+startdate.substring(6,11);
   var subexpirydate=expirydate.substring(0,2)+expirydate.substring(3,5)+expirydate.substring(6,11);
   var subexpirymonth=expirydate.substring(3,5);
   var subexpiryyear=expirydate.substring(6,11);
   
   if( expirydate.substring(3,5)=="12")
    {
      //var expyear=new Date(document.form1.EXPIRYDATE.value);
      //var yearplus=expyear.getFullYear();
     // var yearminus=yearplus-1;
     var actualexpiryyear = parseInt(subexpiryyear) + 1;
      document.form1.ACTUALEXPIRYDATE.focus();
      document.form1.ACTUALEXPIRYDATE.value=expirydate.substring(0,2)+"/"+"01"+"/"+actualexpiryyear;
    }
    else
    {
       var monthplus;
       var dateplus;
       if(expirydate.substring(3,5)=="01")
       {
        
        if(expirydate.substring(0,2)=="31")
         {
           dateplus="28";
           monthplus="02";
         }
        else if(expirydate.substring(0,2)=="30")
         {
           dateplus="28";
            monthplus="02";
         }
         else if(expirydate.substring(0,2)=="29")
         {
           dateplus="28";
            monthplus="02";
         }
         else
         {
            monthplus="02";
            dateplus=expirydate.substring(0,2);
          }
         
        }
      else if(expirydate.substring(3,5)=="02")
       {
        
         monthplus="03";
         dateplus=expirydate.substring(0,2);
      
       }
        else if(expirydate.substring(3,5)=="03")
       {
        
         
         if(expirydate.substring(0,2)=="31")
         {
           dateplus="30";
           monthplus="04";
         }
          else
         {
           dateplus=expirydate.substring(0,2);
           monthplus="04";
         }
      
       }
       else if(expirydate.substring(3,5)=="04")
       {
        
         monthplus="05";
         dateplus=expirydate.substring(0,2);
       }
       else if(expirydate.substring(3,5)=="05")
       {
        
         
         if(expirydate.substring(0,2)=="31")
         {
           dateplus="30";
           monthplus="06";
         }
         else
         {
          dateplus=expirydate.substring(0,2);
          monthplus="06";
         }
      
       }
       else if(expirydate.substring(3,5)=="06")
       {
        
         monthplus="07";
         dateplus=expirydate.substring(0,2);
       }
        else if(expirydate.substring(3,5)=="07")
       {
        
         monthplus="08";
          dateplus=expirydate.substring(0,2);
        }
        else if(expirydate.substring(3,5)=="08")
       {
        
         if(expirydate.substring(0,2)=="31")
         {
           dateplus="30";
           monthplus="09";
         }
          else
         {
          dateplus=expirydate.substring(0,2);
           monthplus="09";
         }
      
       }
       else if(expirydate.substring(3,5)=="09")
       {
        
         monthplus="10";
         dateplus=expirydate.substring(0,2);
       }
       else if(expirydate.substring(3,5)=="10")
       {
        
         if(expirydate.substring(0,2)=="31")
         {
           monthplus="11";
           dateplus="30";
         }
          else
         {
          dateplus=expirydate.substring(0,2);
           monthplus="11";
         }
      
       }
       else if(expirydate.substring(3,5)=="11")
       {
        
         monthplus="12";
         dateplus=expirydate.substring(0,2);
       }
        else if(expirydate.substring(3,5)=="12")
       {
        
          monthplus="01";
          dateplus=expirydate.substring(0,2);
       }
      document.form1.ACTUALEXPIRYDATE.value=dateplus+"/"+monthplus+"/"+expirydate.substring(6,11);
    }
 
   return true;
}
function onView(){
    document.form1.action = "PlantMst.jsp?action=View";
    document.form1.submit();
}
function onNew(){
 
   document.form1.action  = "PlantMst.jsp?action=NEW";
   document.form1.submit();
}
function onAdd(){
   var ITEM_ID   = document.form1.PLANT.value;
   var STRATDT = document.form1.STARTDATE.value;
   var ENDDT = document.form1.EXPIRYDATE.value;
   var ISPEPPOL = document.form1.ISPEPPOL.value;
   var PEPPOL_ID = document.form1.PEPPOL_ID.value;
   var PEPPOL_UKEY = document.form1.PEPPOL_UKEY.value;
   var frarray = new Array();
   frarray = STRATDT.split('/');
   var toarray = new Array();
   toarray = ENDDT.split('/');
   var desc = document.form1.PLNTDESC.value;
   //var companyregnumber = document.form1.companyregnumber.value;
   var ITEM_ISTAXREG=document.form1.ISTAXREGISTRED.value;
   var ISASSIGN_USERLOC=document.form1.ISASSIGN_USERLOC.value;
   var SHOW_STOCKQTY_ONAPP=document.form1.SHOW_STOCKQTY_ONAPP.value;//thanzith
   var ISPOSRETURN_TRAN=document.form1.ISPOSRETURN_TRAN.value;//thanzith
   var ISPOSVOID_TRAN=document.form1.ISPOSVOID_TRAN.value;//thanzith
   var SHOW_POS_SUMMARY=document.form1.SHOW_POS_SUMMARY.value;//thanzith
   var ISMANAGEWORKFLOW=document.form1.ISMANAGEWORKFLOW.value;//thanzith
   var ALLOWCATCH_ADVANCE_SEARCH=document.form1.ALLOWCATCH_ADVANCE_SEARCH.value;//thanzith
   var SETCURRENTDATE_ADVANCE_SEARCH=document.form1.SETCURRENTDATE_ADVANCE_SEARCH.value;//thanzith
   var ISSHOWPOSPRICEBYOUTLET=document.form1.ISSHOWPOSPRICEBYOUTLET.value;//imthi
   var ALLOWPOSTRAN_LESSTHAN_AVBQTY=document.form1.ALLOWPOSTRAN_LESSTHAN_AVBQTY.value;//thanzith
   var ISPRODUCTMAXQTY=document.form1.ISPRODUCTMAXQTY.value;
   var ISAUTO_CONVERT_ESTPO=document.form1.ISAUTO_CONVERT_ESTPO.value;
   var ISAUTO_CONVERT_RECEIPTBILL=document.form1.ISAUTO_CONVERT_RECEIPTBILL.value;//thanzith
   var SETCURRENTDATE_GOODSRECEIPT=document.form1.SETCURRENTDATE_GOODSRECEIPT.value;//thanzith
   var ISEMPLOYEEVALIDATEPO=document.form1.ISEMPLOYEEVALIDATEPO.value;//alafath
   var ISEMPLOYEEVALIDATESO=document.form1.ISEMPLOYEEVALIDATESO.value;//alafath
   var ISAUTO_CONVERT_ISSUETOINVOICE=document.form1.ISAUTO_CONVERT_ISSUETOINVOICE.value;//thanzith
   
   var SETCURRENTDATE_PICKANDISSUE=document.form1.SETCURRENTDATE_PICKANDISSUE.value;//thanzith
   var SHOWITEM_AVGCOST=document.form1.SHOWITEM_AVGCOST.value;//thanzith
   var ISAUTO_MINMAX_MULTIESTPO=document.form1.ISAUTO_MINMAX_MULTIESTPO.value;//thanzith
   var ISAUTO_CONVERT_SOINVOICE=document.form1.ISAUTO_CONVERT_SOINVOICE.value;//Resvi
   var ENABLE_RESERVEQTY=document.form1.ENABLE_RESERVEQTY.value;//Resvi
   var ISSALESTOPURCHASE=document.form1.ISSALESTOPURCHASE.value;//Thanzith
   var PRODUCT_SHOWBY_CATAGERY=document.form1.PRODUCT_SHOWBY_CATAGERY.value;
   var ISSUPPLIERMANDATORY=document.form1.ISSUPPLIERMANDATORY.value;
   var ISPRICE_UPDATEONLY_IN_OWNOUTLET=document.form1.ISPRICE_UPDATEONLY_IN_OWNOUTLET.value;
   var ISSTOCKTAKE_BYAVGCOST=document.form1.ISSTOCKTAKE_BYAVGCOST.value;
      var region = document.form1.REGION.value;
   //var rcbno = document.form1.RCBNO.value;
   var cntperson = document.form1.NAME.value;
   var telno = document.form1.TELNO.value;
   var add1 = document.form1.ADD1.value;
   var add3 = document.form1.ADD3.value;
   var cnty = document.form1.COUNTY.value;
   var zip = document.form1.ZIP.value;
   var noofcatlogs = document.form1.NOOFCATALOGS.value;
   var noofsignatures = document.form1.NOOFSIGNATURES.value;
   var base_currency = document.form1.BaseCurrency.value;
   var taxby = document.form1.TAXBY.value;
   var taxbylabel = document.form1.TAXBYLABEL.value;
   var state = document.form1.STATE.value;
   var taxbylabelmagt = document.form1.TAXBYLABELORDERMANAGEMENT.value;
   var fiscalyear = document.form1.FISCALYEAR.value;
   var payear = document.form1.PAYROLLYEAR.value;
   var RCBNO   = document.form1.RCBNO.value;
   var rcbn = RCBNO.length;
   var COUNTRY_CODE   = document.form1.COUNTRY_CODE.value;
   var ISREFERENCEINVOICE=document.form1.ISREFERENCEINVOICE.value;
   
   var frdt = new Date(frarray[2],frarray[1],frarray[0]);
   var todt = new Date(toarray[2],toarray[1],toarray[0]);
   var chkPurchaseSystem="0";
   if(document.getElementById("ENABLE_INVENTORY").checked == true)
	   chkPurchaseSystem="1";
   else if(document.getElementById("ENABLE_ACCOUNTING").checked == true)
	   chkPurchaseSystem="1";
   else if(document.getElementById("ENABLE_PAYROLL").checked == true)
   	   chkPurchaseSystem="1";
   else if(document.getElementById("ENABLE_POS").checked == true)
   	   chkPurchaseSystem="1";
	   
   else if(document.getElementById("OWNER_APP").checked == true)
	   chkPurchaseSystem="1";
   else if(document.getElementById("CUSTOMER_APP").checked == true)
	   chkPurchaseSystem="1";
   else if(document.getElementById("MANAGER_APP").checked == true)
	   chkPurchaseSystem="1";
   else if(document.getElementById("STORE_APP").checked == true)
	   chkPurchaseSystem="1";
   else(document.getElementById("RIDE_APP").checked == true)
   	   chkPurchaseSystem="1";
   
   //if(ITEM_ID == "" || ITEM_ID == null) {alert("Please Enter Company ");document.form1.PLANT.focus(); return false; }
   if(desc == "" || desc == null) {alert("Please Enter Company Name");document.form1.PLNTDESC.focus(); return false; }

 //thanzi
	if(document.getElementById("ISPEPPOL").checked == true)
		ISPEPPOL="1";
	else 
		ISPEPPOL="0";
	   if(PEPPOL_ID == "" &&  ISPEPPOL == "1") {
	 	  alert("Please Enter Peppol Id"); 
	 	return false; 
	}
	   if(PEPPOL_UKEY == "" &&  ISPEPPOL == "1") {
		 	  alert("Please Enter Peppol Ukey"); 
		 	return false; 
		}
 
   /* if(region == "GCC"){
	   document.form1.companyregnumber.value="";
		
	   }else if(region == "ASIA PACIFIC"){
	if (companyregnumber == "" || companyregnumber == null) {
		 alert("Please Enter Unique Entity Number (UEN)");
		 document.form1.companyregnumber.focus();
		  return false; 
			}
		  } */
	   
   //else if(rcbno == "" || rcbno == null) {alert("Please Enter RCB No");document.form1.RCBNO.focus(); return false; }
   if(STRATDT == "" || STRATDT == null) {alert("Please Select Start Date");document.form1.STARTDATE.focus(); return false; }   
   if(ENDDT == "" || ENDDT == null) {alert("Please Select Expiry Date");document.form1.EXPIRYDATE.focus(); return false; } 
   if(todt <frdt) {alert("Please Select Expiry Date greater than Start Date");return false; }
   if(noofcatlogs == "" || noofcatlogs == null) {alert("Please Enter Number of Catalogs");document.form1.NOOFCATALOGS.focus(); return false; }
   if(noofcatlogs ==0) {alert("Enter Valid Number Of Catalogs");document.form1.NOOFCATALOGS.focus(); return false; }
   if(noofsignatures == "" || noofsignatures == null) {alert("Please Enter Number of Signatures");document.form1.NOOFSIGNATURES.focus(); return false; }
   if(noofsignatures ==0) {alert("Enter Valid Number Of Signatures");document.form1.NOOFSIGNATURES.focus(); return false; }
   if(base_currency ==0){alert("Please select the base currency");document.form1.BaseCurrency.focus();return false;}
   if(chkPurchaseSystem=="0"){alert("Please select any Purchase System");document.form1.ENABLE_INVENTORY.focus();return false;}
   if(COUNTRY_CODE != "SG") {
	   if(rcbn>0){
	   if("15"!=rcbn){alert(" Please Enter your 15 digit numeric "+taxbylabel+" No.");document.form1.RCBNO.focus();return false;}
	   }
   }
   if(taxby =="0"){alert("Please select the tax by");document.form1.TAXBY.focus();return false;}
   if(taxbylabel ==0){alert("Please select the tax by label order configuration");document.form1.TAXBYLABEL.focus();return false;}
   if(taxbylabelmagt ==0){alert("Please select the tax by label order management");document.form1.TAXBYLABELORDERMANAGEMENT.focus();return false;}
   /* else if(fiscalyear == "" || fiscalyear == null) {alert("Please Select Fiscal Year from Other Details");document.form1.FISCALYEAR.focus(); return false; }
   else if(payear == "" || payear == null) {alert("Please Select PayRoll Year from Other Details");document.form1.PAYROLLYEAR.focus(); return false; } */
   if(cntperson == "" || cntperson == null) {alert("Please Enter Contact Name from Contact Details");document.form1.NAME.focus(); return false; }
   if(telno == "" || telno == null) {alert("Please Enter Telephone No from Contact Details");document.form1.TELNO.focus(); return false; }
   if(document.form1.REGION.selectedIndex==0) {alert("Please Select Region from Address");document.form1.REGION.focus();return false; }
   if(document.form1.COUNTRY_CODE.selectedIndex==0) {alert("Please Select Country from Address"); document.form1.COUNTRY_CODE.focus();return false; }
   /* else if(add1 == "" || add1 == null) {alert("Please Enter Unit No from Address");document.form1.ADD1.focus(); return false; } */
   //else if(add3 == "" || add3 == null) {alert("Please Enter Street from Address");document.form1.ADD3.focus(); return false; }   
   if(zip == "" || zip == null) {alert("Please Enter Postal Code from Address");document.form1.ZIP.focus(); return false; }
   
 
   
  else{
   /* document.form1.action  = "PlantMst.jsp?action=ADD";
   document.form1.submit(); */
   }
   
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
function getval() {
	  var  d = document.getElementById("select_id").value;
	     document.getElementById('TaxLabel').innerHTML = d + " No.:";

	}
</script>
  
  <script type="text/javascript" src="js/calendar.js"></script>
  
 
<%@ include file="header.jsp"%>
<%
   
	String sUserId = (String) session.getAttribute("LOGIN_USER");
	String Plant = (String) session.getAttribute("PLANT");
	String sPlantDesc="",sPlant="" ,action="";
	sPlantDesc  = StrUtils.fString(request.getParameter("PLANTDESC"));
	action = StrUtils.fString(request.getParameter("action"));
	sPlant  = StrUtils.fString(request.getParameter("PLANT"));
	String sesplant = StrUtils.fString((String) session.getAttribute("PLANT"));
	String msgPlant  = StrUtils.fString(request.getParameter("SPLANT"));
	String msgPlantDesc  = StrUtils.fString(request.getParameter("SPLNTDESC"));
	String fieldDesc = StrUtils.fString(request.getParameter("result"));
	String msg="error-msg";
	if(fieldDesc.equalsIgnoreCase("Created Successfully"))
	{
		msg="success-msg";
		fieldDesc="New Company '"+msgPlant+"' ("+msgPlantDesc+") Created Successfully";
	}
	String PLANT="",COMPANY="",PLNTDESC="",PEPPOL_UKEY="",companyregnumber="",STARTDATE="",EXPIRYDATE="",ACTUALEXPIRYDATE="",SDATE="",EDATE="",RCBNO="",
	NAME="",DESGINATION="",TELNO="",HPNO="",EMAIL="",ADD1="",ADD2="",ADD3="",ADD4="",FAX="",REMARKS="",COUNTY="",ZIP="",
    SALESPERCENT="",SDOLLARFRATE="",SCENTSFRATE="",SALES="",NOOFCATALOGS="",EDOLLARFRATE="",STATE="",
    ECENTSFRATE="",currencyCode = "",NOOFSIGNATURES="",TAXBY="", FLATRATE="",PERCENTAGE="", ENABLEINVENTORY = "0", ENABLEACCOUNTING = "0",TAXBYLABEL="",DECIMALPRECISION="0",ISTAXREG="0",COMP_INDUSTRY="",REPROTSBASIS="",
    FISCALYEAR="",PAYROLLYEAR="",REGION="",TAXBYLABELORDERMANAGEMENT="",strtaxbylabe1order="",WEBSITE="",FACEBOOK="",TWITTER="",LINKEDIN="",PEPPOL_ID="",SKYPE="",ENABLEPAYROLL="",ENABLEPOS="",OWNERAPP="0",CUSTOMERAPP="0",MANAGERAPP="0",STOREAPP = "0",RIDEAPP="0",FYEAR="",PYEAR="",EMPLOYEEWORKINGMANDAYSBY="",
    		NOOFSUPPLIER="",NOOFCUSTOMER="",NOOFEMPLOYEE="",NOOFUSER="",NOOFINVENTORY="",NOOFLOCATION="",NOOFORDER="",NOOFEXPBILLINV="",ISASSIGN_USERLOC="",SHOW_STOCKQTY_ONAPP="",ISPOSRETURN_TRAN="",ISPOSVOID_TRAN="",SHOW_POS_SUMMARY="",ISMANAGEWORKFLOW="",ALLOWCATCH_ADVANCE_SEARCH="",SETCURRENTDATE_ADVANCE_SEARCH="",ISSHOWPOSPRICEBYOUTLET="",ALLOWPOSTRAN_LESSTHAN_AVBQTY="",ISPRODUCTMAXQTY="",ISSALESAPP_TAXINCLUSIVE="",ISPOSTAXINCLUSIVE="",ISPOSSALESMAN="",ALLOWPRDTOCOMPANY="",ISAUTO_CONVERT_ESTPO="",ISAUTO_CONVERT_RECEIPTBILL="",SHOWITEM_AVGCOST="",ISAUTO_MINMAX_MULTIESTPO="",SETCURRENTDATE_GOODSRECEIPT="",ISEMPLOYEEVALIDATEPO="",ISEMPLOYEEVALIDATESO="",ISAUTO_CONVERT_ISSUETOINVOICE="",SETCURRENTDATE_PICKANDISSUE="", ISAUTO_CONVERT_SOINVOICE="",ENABLE_RESERVEQTY="",ISSALESTOPURCHASE="",PRODUCT_SHOWBY_CATAGERY="", NOOFPAYMENT="", NOOFJOURNAL="", NOOFCONTRA="", ISPEPPOL="",shopify="", lazada="", shopee="", amazon="",ISREFERENCEINVOICE="",ISPRICE_UPDATEONLY_IN_OWNOUTLET="",ISSUPPLIERMANDATORY="",ISSTOCKTAKE_BYAVGCOST="";
	String sNewEnb    = "enabled";
	String sDeleteEnb = "enabled";
	String sAddEnb    = "disabled";
	String sUpdateEnb = "enabled";
	String res="";
	String existingplnt="";
	boolean flag=false;
	PlantMstUtil plantmstutil = new PlantMstUtil();
	MasterUtil _MasterUtil=new  MasterUtil();
	PlantMstDAO _PlantMstDAO = new PlantMstDAO();
	plantmstutil.setmLogger(mLogger);
	_PlantMstDAO.setmLogger(mLogger);
	DateUtils dateutils = new DateUtils();
	PLANT     = StrUtils.fString(request.getParameter("PLANT"));
	PLNTDESC     = StrUtils.fString(request.getParameter("PLNTDESC"));
	PEPPOL_UKEY     = StrUtils.fString(request.getParameter("PEPPOL_UKEY"));
	ISTAXREG	= StrUtils.fString(request.getParameter("ISTAXREGISTRED"));
	COMP_INDUSTRY	= StrUtils.fString(request.getParameter("CompanyIndustry"));
	REPROTSBASIS = StrUtils.fString(request.getParameter("REPROTSBASIS"));
	STARTDATE     = StrUtils.fString(request.getParameter("STARTDATE"));
	EXPIRYDATE   = StrUtils.fString(request.getParameter("EXPIRYDATE"));
	ACTUALEXPIRYDATE= StrUtils.fString(request.getParameter("ACTUDALEXPIRYDATE"));
	NAME=StrUtils.fString(request.getParameter("NAME"));
	DESGINATION     = StrUtils.fString(request.getParameter("DESGINATION"));
	TELNO     = StrUtils.fString(request.getParameter("TELNO"));
	HPNO    = StrUtils.fString(request.getParameter("HPNO"));
	EMAIL    = StrUtils.fString(request.getParameter("EMAIL"));
	ADD1   = StrUtils.fString(request.getParameter("ADD1"));
	ADD2   = StrUtils.fString(request.getParameter("ADD2"));
	ADD3   = StrUtils.fString(request.getParameter("ADD3"));
	ADD4  =  StrUtils.fString(request.getParameter("ADD4"));
	COUNTY = StrUtils.fString(request.getParameter("COUNTY"));
	ZIP = StrUtils.fString(request.getParameter("ZIP"));
	REMARKS   = StrUtils.fString(request.getParameter("REMARKS"));
	FAX  = StrUtils.fString(request.getParameter("FAX"));
	RCBNO = StrUtils.fString(request.getParameter("RCBNO"));
    NOOFCATALOGS = "500";
    NOOFSIGNATURES="500";
    SALES = StrUtils.fString(request.getParameter("SALES"));
    SALESPERCENT=StrUtils.fString(request.getParameter("SALESPERCENT"));
    SDOLLARFRATE=StrUtils.fString(request.getParameter("SDOLLARFRATE"));
    SCENTSFRATE=StrUtils.fString(request.getParameter("SCENTSFRATE"));
    EDOLLARFRATE=StrUtils.fString(request.getParameter("EDOLLARFRATE"));
    ECENTSFRATE=StrUtils.fString(request.getParameter("ECENTSFRATE"));
    TAXBY=StrUtils.fString(request.getParameter("TAXBY"));
    TAXBYLABEL=StrUtils.fString(request.getParameter("TAXBYLABEL"));
    STATE=StrUtils.fString(request.getParameter("STATE"));
    PEPPOL_ID= StrUtils.fString(request.getParameter("PEPPOL_ID"));
    DECIMALPRECISION=StrUtils.fString(request.getParameter("decimal_precision"));
    
    if (!"".equals(StrUtils.fString(request.getParameter("ENABLE_INVENTORY")))){
    	ENABLEINVENTORY = "1";
    }
    if (!"".equals(StrUtils.fString(request.getParameter("ENABLE_ACCOUNTING")))){
    	ENABLEACCOUNTING = "1";
    }
    if (!"".equals(StrUtils.fString(request.getParameter("OWNER_APP")))){
    	OWNERAPP = "1";
    }
    if (!"".equals(StrUtils.fString(request.getParameter("CUSTOMER_APP")))){
    	CUSTOMERAPP = "1";
    }
    if (!"".equals(StrUtils.fString(request.getParameter("MANAGER_APP")))){
    	MANAGERAPP = "1";
    }
    if (!"".equals(StrUtils.fString(request.getParameter("STORE_APP")))){
    	STOREAPP = "1";
    }
    if (!"".equals(StrUtils.fString(request.getParameter("RIDE_APP")))){
    	RIDEAPP = "1";
    }
    if(SALESPERCENT.length()==0)SALESPERCENT="0";
    if(SDOLLARFRATE.length()==0)SDOLLARFRATE="0";
    if(SCENTSFRATE.length()==0)SCENTSFRATE="0";
    
    if(EDOLLARFRATE.length()==0)EDOLLARFRATE="0";
    if(ECENTSFRATE.length()==0)ECENTSFRATE="0";
    
           
    System.out.println(SALES+"###########"+SALESPERCENT+"#########"+SDOLLARFRATE+"#########"+SCENTSFRATE);
	if(!STARTDATE.equals("") || STARTDATE.equals(null))
	{
	    SDATE    = STARTDATE.substring(6)+"-"+ STARTDATE.substring(3,5)+"-"+STARTDATE.substring(0,2);
	}
	if(!EXPIRYDATE.equals("") || EXPIRYDATE.equals(null))
	{
	   EDATE    = EXPIRYDATE.substring(6)+"-"+ EXPIRYDATE.substring(3,5)+"-"+EXPIRYDATE.substring(0,2);
	}

	currencyCode = StrUtils.fString(request.getParameter("BaseCurrency"));
	
	FISCALYEAR= StrUtils.fString(request.getParameter("FISCALYEAR"));
    PAYROLLYEAR= StrUtils.fString(request.getParameter("PAYROLLYEAR"));
    REGION= StrUtils.fString(request.getParameter("REGION"));
    TAXBYLABELORDERMANAGEMENT= StrUtils.fString(request.getParameter("TAXBYLABELORDERMANAGEMENT"));
    strtaxbylabe1order= StrUtils.fString(request.getParameter("strtaxbylabe1order"));
    WEBSITE= StrUtils.fString(request.getParameter("WEBSITE"));
    FACEBOOK= StrUtils.fString(request.getParameter("FACEBOOK"));
    TWITTER= StrUtils.fString(request.getParameter("TWITTER"));
    LINKEDIN= StrUtils.fString(request.getParameter("LINKEDIN"));
    SKYPE= StrUtils.fString(request.getParameter("SKYPE"));
    
    
    if (!"".equals(StrUtils.fString(request.getParameter("ENABLE_PAYROLL")))){
    	ENABLEPAYROLL = "1";
    }
    if (!"".equals(StrUtils.fString(request.getParameter("ENABLE_POS")))){
    	ENABLEPOS = "1";
    }
    
    NOOFSUPPLIER=StrUtils.fString(request.getParameter("No_of_Supplier"));
    NOOFCUSTOMER=StrUtils.fString(request.getParameter("No_of_Customer"));
    NOOFEMPLOYEE=StrUtils.fString(request.getParameter("No_of_Employee"));
    NOOFUSER=StrUtils.fString(request.getParameter("No_of_User"));
    NOOFINVENTORY=StrUtils.fString(request.getParameter("No_of_Inventory"));
    NOOFLOCATION=StrUtils.fString(request.getParameter("No_of_Location"));
    ISPOSTAXINCLUSIVE=StrUtils.fString(request.getParameter("ISPOSTAXINCLUSIVE"));
    ISSALESAPP_TAXINCLUSIVE =StrUtils.fString(request.getParameter("ISSALESAPP_TAXINCLUSIVE"));
    ISPOSSALESMAN=StrUtils.fString(request.getParameter("ISPOSSALESMAN"));
    ALLOWPRDTOCOMPANY=StrUtils.fString(request.getParameter("ALLOWPRDTOCOMPANY"));//imthi

   ISASSIGN_USERLOC = (request.getParameter("ISASSIGN_USERLOC") != null) ? "1": "0";
   SHOW_STOCKQTY_ONAPP = (request.getParameter("SHOW_STOCKQTY_ONAPP") != null) ? "1": "0";
   ISPOSRETURN_TRAN = (request.getParameter("ISPOSRETURN_TRAN") != null) ? "1": "0";
   ISPOSVOID_TRAN = (request.getParameter("ISPOSVOID_TRAN") != null) ? "1": "0";
   SHOW_POS_SUMMARY = (request.getParameter("SHOW_POS_SUMMARY") != null) ? "1": "0";
   ISMANAGEWORKFLOW = (request.getParameter("ISMANAGEWORKFLOW") != null) ? "1": "0";
   ALLOWCATCH_ADVANCE_SEARCH = (request.getParameter("ALLOWCATCH_ADVANCE_SEARCH") != null) ? "1": "0";
   SETCURRENTDATE_ADVANCE_SEARCH = (request.getParameter("SETCURRENTDATE_ADVANCE_SEARCH") != null) ? "1": "0";
   ISSHOWPOSPRICEBYOUTLET = (request.getParameter("ISSHOWPOSPRICEBYOUTLET") != null) ? "1": "0";
   ALLOWPOSTRAN_LESSTHAN_AVBQTY = (request.getParameter("ALLOWPOSTRAN_LESSTHAN_AVBQTY") != null) ? "1": "0";
   ISAUTO_CONVERT_ESTPO = (request.getParameter("ISAUTO_CONVERT_ESTPO") != null) ? "1": "0";
   ISPRODUCTMAXQTY = (request.getParameter("ISPRODUCTMAXQTY") != null) ? "1": "0";
   ISAUTO_CONVERT_RECEIPTBILL = (request.getParameter("ISAUTO_CONVERT_RECEIPTBILL") != null) ? "1": "0";
   SETCURRENTDATE_GOODSRECEIPT = (request.getParameter("SETCURRENTDATE_GOODSRECEIPT") != null) ? "1": "0";
   ISEMPLOYEEVALIDATEPO = (request.getParameter("ISEMPLOYEEVALIDATEPO") != null) ? "1": "0";
   ISEMPLOYEEVALIDATESO = (request.getParameter("ISEMPLOYEEVALIDATESO") != null) ? "1": "0";
   ISAUTO_CONVERT_ISSUETOINVOICE = (request.getParameter("ISAUTO_CONVERT_ISSUETOINVOICE") != null) ? "1": "0";
   SETCURRENTDATE_PICKANDISSUE = (request.getParameter("SETCURRENTDATE_PICKANDISSUE") != null) ? "1": "0";
   SHOWITEM_AVGCOST = (request.getParameter("SHOWITEM_AVGCOST") != null) ? "1": "0";
   ISAUTO_CONVERT_SOINVOICE = (request.getParameter("ISAUTO_CONVERT_SOINVOICE") != null) ? "1": "0";
   ENABLE_RESERVEQTY = (request.getParameter("ENABLE_RESERVEQTY") != null) ? "1": "0";
   ISSUPPLIERMANDATORY = (request.getParameter("ISSUPPLIERMANDATORY") != null) ? "1": "0";
   ISPRICE_UPDATEONLY_IN_OWNOUTLET = (request.getParameter("ISPRICE_UPDATEONLY_IN_OWNOUTLET") != null) ? "1": "0";
   ISSTOCKTAKE_BYAVGCOST = (request.getParameter("ISSTOCKTAKE_BYAVGCOST") != null) ? "1": "0";
   ISSALESTOPURCHASE = (request.getParameter("ISSALESTOPURCHASE") != null) ? "1": "0";
   PRODUCT_SHOWBY_CATAGERY = (request.getParameter("PRODUCT_SHOWBY_CATAGERY") != null) ? "1": "0";
    companyregnumber=StrUtils.fString(request.getParameter("companyregnumber"));
    NOOFORDER=StrUtils.fString(request.getParameter("No_of_Order"));
    NOOFEXPBILLINV=StrUtils.fString(request.getParameter("No_of_Exp_Bill_Inv"));
    NOOFPAYMENT=StrUtils.fString(request.getParameter("No_of_Payment"));
    NOOFJOURNAL=StrUtils.fString(request.getParameter("No_of_Journal")); 
    NOOFCONTRA=StrUtils.fString(request.getParameter("No_of_Contra"));
    shopify = StrUtils.fString(request.getParameter("shopify"));
    lazada = StrUtils.fString(request.getParameter("lazada"));
    shopee = StrUtils.fString(request.getParameter("shopee"));
    amazon = StrUtils.fString(request.getParameter("amazon"));
    ISPEPPOL = StrUtils.fString(request.getParameter("ISPEPPOL"));
    ISREFERENCEINVOICE = (request.getParameter("ISREFERENCEINVOICE") != null) ? "1": "0";
    
	long randomCompany=0;
	DateUtils dateUtils = null;
	if(action.equalsIgnoreCase("NEW")){
	      PLANT="";
	      PLNTDESC="";
	      PEPPOL_UKEY="";
	      companyregnumber="";
	      ISTAXREG="0";
	      COMP_INDUSTRY="";
	      REPROTSBASIS = "";
	      STARTDATE="";
	      EXPIRYDATE="";
	      ACTUALEXPIRYDATE="";
	      NAME="";
	      DESGINATION     = "";
	      TELNO     = "";
	      HPNO    = "";
	      EMAIL    = "";
	      ADD1   = "";
	      ADD2   ="";
	      ADD3   = "";  ADD4   = "";
	      COUNTY="";ZIP="";
	      REMARKS="";
	      FAX="";RCBNO="";
          SALESPERCENT="0";
          SDOLLARFRATE="0";
          SCENTSFRATE="0";
          EDOLLARFRATE="0";
          ECENTSFRATE="0";
          TAXBY="0";
          TAXBYLABEL="0";
          STATE="";
          ENABLEINVENTORY = "0";
          ENABLEACCOUNTING = "0";
          OWNERAPP = "0";
          CUSTOMERAPP = "0";
          MANAGERAPP = "0";
          STOREAPP = "0";
          RIDEAPP = "0";
          DECIMALPRECISION = "2";
          FISCALYEAR="";
          PAYROLLYEAR="";
          REGION="";
          TAXBYLABELORDERMANAGEMENT="";
          strtaxbylabe1order="";
          WEBSITE="";
          FACEBOOK="";
          TWITTER="";
          LINKEDIN="";
          SKYPE="";
          ENABLEPAYROLL = "0";
          ENABLEPOS = "0";
          EMPLOYEEWORKINGMANDAYSBY="";
          NOOFSUPPLIER="Unlimited";
          NOOFCUSTOMER="Unlimited";
          NOOFEMPLOYEE="Unlimited";
          NOOFUSER="Unlimited";
          NOOFINVENTORY="500";
          NOOFLOCATION="Unlimited";
          NOOFORDER="Unlimited";
          NOOFEXPBILLINV="Unlimited";
          NOOFPAYMENT="Unlimited";
          NOOFJOURNAL="Unlimited";
          NOOFCONTRA="Unlimited";
          shopify="";
          lazada="";
          shopee="";
          amazon="";
          ISPEPPOL="";
          PEPPOL_ID="";
          ISASSIGN_USERLOC="";
          SHOW_STOCKQTY_ONAPP="";
          ISPOSRETURN_TRAN="";
          ISPOSVOID_TRAN="";
          SHOW_POS_SUMMARY="";
          ISMANAGEWORKFLOW="";
          ALLOWCATCH_ADVANCE_SEARCH="";
          SETCURRENTDATE_ADVANCE_SEARCH="";
          ISSHOWPOSPRICEBYOUTLET="";
          ALLOWPOSTRAN_LESSTHAN_AVBQTY="";
          ISAUTO_CONVERT_ESTPO="";
          ISPRODUCTMAXQTY="";
          ISAUTO_CONVERT_RECEIPTBILL="";
          SETCURRENTDATE_GOODSRECEIPT="";
          ISEMPLOYEEVALIDATEPO="";
          ISEMPLOYEEVALIDATESO="";
          ISAUTO_CONVERT_ISSUETOINVOICE="";
          SETCURRENTDATE_PICKANDISSUE="";
          SHOWITEM_AVGCOST="";
          ISAUTO_MINMAX_MULTIESTPO="";
          ISAUTO_CONVERT_SOINVOICE="";
          ENABLE_RESERVEQTY="";
          ISSUPPLIERMANDATORY="";
		  ISPRICE_UPDATEONLY_IN_OWNOUTLET="";
		  ISSTOCKTAKE_BYAVGCOST="";
          ISSALESTOPURCHASE="";
          PRODUCT_SHOWBY_CATAGERY="";
          ISPOSTAXINCLUSIVE="";
          ISSALESAPP_TAXINCLUSIVE="";
          ISPOSSALESMAN="";
          ALLOWPRDTOCOMPANY="";
          ISREFERENCEINVOICE="";
	}
	else if(action.equalsIgnoreCase("View")){
	
	    PLANT     = StrUtils.fString(request.getParameter("PLANT"));
	    PLNTDESC     = StrUtils.fString(request.getParameter("PLNTDESC"));
	    PEPPOL_UKEY     = StrUtils.fString(request.getParameter("PEPPOL_UKEY"));
	    ISTAXREG	= StrUtils.fString(request.getParameter("ISTAXREGISTRED"));
	    COMP_INDUSTRY	= StrUtils.fString(request.getParameter("CompanyIndustry"));
	    REPROTSBASIS = StrUtils.fString(request.getParameter("REPROTSBASIS"));
	    STARTDATE     = StrUtils.fString(request.getParameter("STARTDATE"));
	    EXPIRYDATE   = StrUtils.fString(request.getParameter("EXPIRYDATE"));
	    ACTUALEXPIRYDATE=StrUtils.fString(request.getParameter("ACTUDALEXPIRYDATE"));
	    NAME=StrUtils.fString(request.getParameter("NAME"));
	    DESGINATION     = StrUtils.fString(request.getParameter("DESGINATION"));
	    TELNO     = StrUtils.fString(request.getParameter("TELNO"));
	    HPNO    = StrUtils.fString(request.getParameter("HPNO"));
	    EMAIL    = StrUtils.fString(request.getParameter("EMAIL"));
	    ADD1   = StrUtils.fString(request.getParameter("ADD1"));
	    ADD2   = StrUtils.fString(request.getParameter("ADD2"));
	    ADD3   = StrUtils.fString(request.getParameter("ADD3"));
	    ADD4   = StrUtils.fString(request.getParameter("ADD4"));
	    COUNTY   = StrUtils.fString(request.getParameter("COUNTY"));
	    ZIP   = StrUtils.fString(request.getParameter("ZIP"));
	    REMARKS   = StrUtils.fString(request.getParameter("REMARKS"));
	    FAX   = StrUtils.fString(request.getParameter("FAX"));
	    currencyCode = StrUtils.fString(request.getParameter("BaseCurrency"));
	    TAXBY=StrUtils.fString(request.getParameter("TAXBY"));
	    TAXBYLABEL=StrUtils.fString(request.getParameter("TAXBYLABEL"));
	    STATE=StrUtils.fString(request.getParameter("STATE"));
		ENABLEINVENTORY = StrUtils.fString(request.getParameter("ENABLE_INVENTORY"));
		ENABLEACCOUNTING = StrUtils.fString(request.getParameter("ENABLE_ACCOUNTING"));
		OWNERAPP = StrUtils.fString(request.getParameter("OWNER_APP"));
		CUSTOMERAPP = StrUtils.fString(request.getParameter("CUSTOMER_APP"));
		MANAGERAPP = StrUtils.fString(request.getParameter("MANAGER_APP"));
		STOREAPP = StrUtils.fString(request.getParameter("STORE_APP"));
		RIDEAPP = StrUtils.fString(request.getParameter("RIDE_APP"));
		DECIMALPRECISION = StrUtils.fString(request.getParameter("decimal_precision"));  
	    NOOFSUPPLIER = StrUtils.fString(request.getParameter("No_of_Supplier"));
	    NOOFCUSTOMER = StrUtils.fString(request.getParameter("No_of_Customer"));
	    NOOFEMPLOYEE = StrUtils.fString(request.getParameter("No_of_Employee"));
	    NOOFUSER = StrUtils.fString(request.getParameter("No_of_User"));
	    NOOFINVENTORY = StrUtils.fString(request.getParameter("No_of_Inventory"));
	    NOOFLOCATION = StrUtils.fString(request.getParameter("No_of_Location"));
	    companyregnumber = StrUtils.fString(request.getParameter("companyregnumber"));
	    NOOFORDER = StrUtils.fString(request.getParameter("No_of_Order"));
	    NOOFEXPBILLINV = StrUtils.fString(request.getParameter("No_of_Exp_Bill_Inv"));
	    NOOFPAYMENT= StrUtils.fString(request.getParameter("No_of_Payment"));
	    NOOFJOURNAL= StrUtils.fString(request.getParameter("No_of_Journal"));
	    NOOFCONTRA= StrUtils.fString(request.getParameter("No_of_Contra"));
	    ISPOSTAXINCLUSIVE= StrUtils.fString(request.getParameter("ISPOSTAXINCLUSIVE"));
	    ISSALESAPP_TAXINCLUSIVE= StrUtils.fString(request.getParameter("ISSALESAPP_TAXINCLUSIVE"));
	    ISPOSSALESMAN= StrUtils.fString(request.getParameter("ISPOSSALESMAN"));
	    ALLOWPRDTOCOMPANY= StrUtils.fString(request.getParameter("ALLOWPRDTOCOMPANY"));
	    PEPPOL_ID= StrUtils.fString(request.getParameter("PEPPOL_ID"));

    ISASSIGN_USERLOC = (request.getParameter("ISASSIGN_USERLOC") != null) ? "1": "0";
    SHOW_STOCKQTY_ONAPP = (request.getParameter("SHOW_STOCKQTY_ONAPP") != null) ? "1": "0";
    ISPOSRETURN_TRAN = (request.getParameter("ISPOSRETURN_TRAN") != null) ? "1": "0";
    ISPOSVOID_TRAN = (request.getParameter("ISPOSVOID_TRAN") != null) ? "1": "0";
    SHOW_POS_SUMMARY = (request.getParameter("SHOW_POS_SUMMARY") != null) ? "1": "0";
    ISMANAGEWORKFLOW = (request.getParameter("ISMANAGEWORKFLOW") != null) ? "1": "0";
    ALLOWCATCH_ADVANCE_SEARCH = (request.getParameter("ALLOWCATCH_ADVANCE_SEARCH") != null) ? "1": "0";
    SETCURRENTDATE_ADVANCE_SEARCH = (request.getParameter("SETCURRENTDATE_ADVANCE_SEARCH") != null) ? "1": "0";
    ISSHOWPOSPRICEBYOUTLET = (request.getParameter("ISSHOWPOSPRICEBYOUTLET") != null) ? "1": "0";
    ALLOWPOSTRAN_LESSTHAN_AVBQTY = (request.getParameter("ALLOWPOSTRAN_LESSTHAN_AVBQTY") != null) ? "1": "0";
    ISAUTO_CONVERT_ESTPO = (request.getParameter("ISAUTO_CONVERT_ESTPO") != null) ? "1": "0";
    ISPRODUCTMAXQTY = (request.getParameter("ISPRODUCTMAXQTY") != null) ? "1": "0";
    ISAUTO_CONVERT_RECEIPTBILL = (request.getParameter("ISAUTO_CONVERT_RECEIPTBILL") != null) ? "1": "0";
    SETCURRENTDATE_GOODSRECEIPT = (request.getParameter("SETCURRENTDATE_GOODSRECEIPT") != null) ? "1": "0";
    ISEMPLOYEEVALIDATEPO = (request.getParameter("ISEMPLOYEEVALIDATEPO") != null) ? "1": "0";
    ISEMPLOYEEVALIDATESO = (request.getParameter("ISEMPLOYEEVALIDATESO") != null) ? "1": "0";
    ISAUTO_CONVERT_ISSUETOINVOICE = (request.getParameter("ISAUTO_CONVERT_ISSUETOINVOICE") != null) ? "1": "0";
    SETCURRENTDATE_PICKANDISSUE = (request.getParameter("SETCURRENTDATE_PICKANDISSUE") != null) ? "1": "0";
    SHOWITEM_AVGCOST = (request.getParameter("SHOWITEM_AVGCOST") != null) ? "1": "0";
    ISAUTO_MINMAX_MULTIESTPO = (request.getParameter("ISAUTO_MINMAX_MULTIESTPO") != null) ? "1": "0";
    ISAUTO_CONVERT_SOINVOICE = (request.getParameter("ISAUTO_CONVERT_SOINVOICE") != null) ? "1": "0";
    ENABLE_RESERVEQTY = (request.getParameter("ENABLE_RESERVEQTY") != null) ? "1": "0";
    ISSUPPLIERMANDATORY = (request.getParameter("ISSUPPLIERMANDATORY") != null) ? "1": "0";
	ISPRICE_UPDATEONLY_IN_OWNOUTLET = (request.getParameter("ISPRICE_UPDATEONLY_IN_OWNOUTLET") != null) ? "1": "0";
	ISSTOCKTAKE_BYAVGCOST = (request.getParameter("ISSTOCKTAKE_BYAVGCOST") != null) ? "1": "0";
    ISSALESTOPURCHASE = (request.getParameter("ISSALESTOPURCHASE") != null) ? "1": "0";
    PRODUCT_SHOWBY_CATAGERY = (request.getParameter("PRODUCT_SHOWBY_CATAGERY") != null) ? "1": "0";
    ISREFERENCEINVOICE = (request.getParameter("ISREFERENCEINVOICE") != null) ? "1": "0";
	    shopify=StrUtils.fString(request.getParameter("shopify"));
       	lazada=StrUtils.fString(request.getParameter("lazada"));
       	shopee=StrUtils.fString(request.getParameter("shopee"));
       	amazon=StrUtils.fString(request.getParameter("amazon"));
       	ISPEPPOL=StrUtils.fString(request.getParameter("ISPEPPOL"));
	}
	
	else if(action.equalsIgnoreCase("ADD")){
	
		Hashtable htCond=new Hashtable();
		 htCond.put("PLANT",(String)PLANT);
		 List listQry = plantmstutil.getPlantMstDetails(PLANT);
		 for (int i =0; i<listQry.size(); i++){
		    Map map = (Map) listQry.get(i);
		    existingplnt  = (String) map.get("PLANT");
		    if(existingplnt.equalsIgnoreCase(sPlant))
		    {
		      flag=true;
		      res="<font class = "+"mainred"+">Company"+ " "+ PLANT +" "+"Exists Already</font>";
		    }  
		    
		}
		 Hashtable ht = new Hashtable();
		 if(flag==false)
		  {
			 //random+(Math.floor(Math.random() * (max - min + 1)) + min);
			 randomCompany=(long)(Math.random()*10000000000L);
			 //String stringCompany="C"+dateUtils.getDateTime()+Long.toString(randomCompany)+"S2T" ;
			String stringCompany="C"+Long.toString(randomCompany)+"S2T" ;
			NOOFCATALOGS = StrUtils.fString(request.getParameter("NOOFCATALOGS"));
			NOOFSIGNATURES = StrUtils.fString(request.getParameter("NOOFSIGNATURES"));
			
		    ISASSIGN_USERLOC = (request.getParameter("ISASSIGN_USERLOC") != null) ? "1": "0";
		    SHOW_STOCKQTY_ONAPP = (request.getParameter("SHOW_STOCKQTY_ONAPP") != null) ? "1": "0";
		    ISPOSRETURN_TRAN = (request.getParameter("ISPOSRETURN_TRAN") != null) ? "1": "0";
		    ISPOSVOID_TRAN = (request.getParameter("ISPOSVOID_TRAN") != null) ? "1": "0";
		    ISMANAGEWORKFLOW = (request.getParameter("ISMANAGEWORKFLOW") != null) ? "1": "0";
		    ALLOWCATCH_ADVANCE_SEARCH = (request.getParameter("ALLOWCATCH_ADVANCE_SEARCH") != null) ? "1": "0";
		    SETCURRENTDATE_ADVANCE_SEARCH = (request.getParameter("SETCURRENTDATE_ADVANCE_SEARCH") != null) ? "1": "0";
		    ISSHOWPOSPRICEBYOUTLET = (request.getParameter("ISSHOWPOSPRICEBYOUTLET") != null) ? "1": "0";
		    ALLOWPOSTRAN_LESSTHAN_AVBQTY = (request.getParameter("ALLOWPOSTRAN_LESSTHAN_AVBQTY") != null) ? "1": "0";
		    ISAUTO_CONVERT_ESTPO = (request.getParameter("ISAUTO_CONVERT_ESTPO") != null) ? "1": "0";
		    ISPRODUCTMAXQTY = (request.getParameter("ISPRODUCTMAXQTY") != null) ? "1": "0";
		    ISAUTO_CONVERT_RECEIPTBILL = (request.getParameter("ISAUTO_CONVERT_RECEIPTBILL") != null) ? "1": "0";
		    SETCURRENTDATE_GOODSRECEIPT = (request.getParameter("SETCURRENTDATE_GOODSRECEIPT") != null) ? "1": "0";
		    ISEMPLOYEEVALIDATEPO = (request.getParameter("ISEMPLOYEEVALIDATEPO") != null) ? "1": "0";
		    ISEMPLOYEEVALIDATESO = (request.getParameter("ISEMPLOYEEVALIDATESO") != null) ? "1": "0";
		    ISAUTO_CONVERT_ISSUETOINVOICE = (request.getParameter("ISAUTO_CONVERT_ISSUETOINVOICE") != null) ? "1": "0";
		    SETCURRENTDATE_PICKANDISSUE = (request.getParameter("SETCURRENTDATE_PICKANDISSUE") != null) ? "1": "0";
		    SHOWITEM_AVGCOST = (request.getParameter("SHOWITEM_AVGCOST") != null) ? "1": "0";
		    ISAUTO_CONVERT_SOINVOICE = (request.getParameter("ISAUTO_CONVERT_SOINVOICE") != null) ? "1": "0";
		    ENABLE_RESERVEQTY = (request.getParameter("ENABLE_RESERVEQTY") != null) ? "1": "0";
		    ISSUPPLIERMANDATORY = (request.getParameter("ISSUPPLIERMANDATORY") != null) ? "1": "0";
			ISPRICE_UPDATEONLY_IN_OWNOUTLET = (request.getParameter("ISPRICE_UPDATEONLY_IN_OWNOUTLET") != null) ? "1": "0";
			ISSTOCKTAKE_BYAVGCOST = (request.getParameter("ISSTOCKTAKE_BYAVGCOST") != null) ? "1": "0";
		    ISSALESTOPURCHASE = (request.getParameter("ISSALESTOPURCHASE") != null) ? "1": "0";
		    PRODUCT_SHOWBY_CATAGERY = (request.getParameter("PRODUCT_SHOWBY_CATAGERY") != null) ? "1": "0";
		    PLANT=stringCompany;
		    ht.put("PLANT",PLANT);
		    ht.put("PLNTDESC",PLNTDESC);
		    ht.put("PEPPOL_UKEY",PEPPOL_UKEY);
		    ht.put("companyregnumber",companyregnumber);
		    ht.put("REPROTSBASIS",REPROTSBASIS);
		    ht.put(IDBConstants.COMP_INDUSTRY,COMP_INDUSTRY);
		    ht.put(IDBConstants.ISTAXREG,ISTAXREG);
		    ht.put("STARTDATE",SDATE );
		    ht.put("EXPIRYDATE",EDATE );
		    ht.put("ACTEXPIRYDATE",ACTUALEXPIRYDATE);
		    ht.put("NAME",NAME);
		    ht.put("DESGINATION",DESGINATION);
		    ht.put("TELNO",TELNO);
		    ht.put("HPNO",HPNO);
		    ht.put("EMAIL",EMAIL);
		    ht.put("ADD1",ADD1);
		    ht.put("ADD2",ADD2);
		    ht.put("ADD3",ADD3);
		    ht.put("ADD4",ADD4);
		    ht.put(IDBConstants.RCBNO,RCBNO);
		    ht.put(IDBConstants.COUNTY,COUNTY);
		    ht.put(IDBConstants.ZIP,ZIP);  
		    ht.put("USERFLD1",REMARKS);
		    ht.put("USERFLD2",FAX);ht.put("AUTHSTAT","0");
		    ht.put("CRBY",sUserId);
		    ht.put("CRAT", new DateUtils().getDateTime());
		    ht.put("SALES_CHARGE_BY",SALES);
	        ht.put("SALES_PERCENT",SALESPERCENT);
	        ht.put("SALES_FR_DOLLARS",SDOLLARFRATE);
	        ht.put("SALES_FR_CENTS",SCENTSFRATE);
	        ht.put("ENQUIRY_FR_DOLLARS",EDOLLARFRATE);
	        ht.put("ENQUIRY_FR_CENTS",ECENTSFRATE);
	        ht.put("NUMBER_OF_CATALOGS",NOOFCATALOGS);
	        ht.put("NUMBER_OF_SIGNATURES",NOOFSIGNATURES);   
	        ht.put(IDBConstants.BASE_CURRENCY,currencyCode);
	        ht.put(IDBConstants.TAXBY,TAXBY);
	        ht.put(IDBConstants.TAXBYLABEL,TAXBYLABEL);
	        ht.put(IDBConstants.STATE,STATE);
	        ht.put("ENABLE_INVENTORY", ENABLEINVENTORY);
	        ht.put("ENABLE_ACCOUNTING", ENABLEACCOUNTING);
	        ht.put(IDBConstants.OWNER_APP, OWNERAPP);
	        ht.put(IDBConstants.OWNER_APP, CUSTOMERAPP);
	        ht.put(IDBConstants.MANAGER_APP, MANAGERAPP);
	        ht.put(IDBConstants.STORE_APP, STOREAPP);
	        ht.put(IDBConstants.RIDE_APP, RIDEAPP);
	        ht.put("NUMBEROFDECIMAL", DECIMALPRECISION);
	        ht.put("NUMBER_OF_SUPPLIER", NOOFSUPPLIER);
	        ht.put("NUMBER_OF_CUSTOMER", NOOFCUSTOMER);
	        ht.put("NUMBER_OF_EMPLOYEE", NOOFEMPLOYEE);
	        ht.put("NUMBER_OF_USER", NOOFUSER);
	        ht.put("NUMBER_OF_INVENTORY", NOOFINVENTORY);
	        ht.put("NUMBER_OF_LOCATION", NOOFLOCATION);
	        ht.put("NUMBER_OF_ORDER", NOOFORDER);
	        ht.put("NUMBER_OF_EBIQI", NOOFEXPBILLINV);
	        ht.put("PRODUCT_SHOWBY_CATAGERY", PRODUCT_SHOWBY_CATAGERY);
	        ht.put("ISASSIGN_USERLOC", ISASSIGN_USERLOC);
	        ht.put("SHOW_STOCKQTY_ONAPP", SHOW_STOCKQTY_ONAPP);
	        ht.put("ISPOSRETURN_TRAN", ISPOSRETURN_TRAN);
	        ht.put("ISPOSVOID_TRAN", ISPOSVOID_TRAN);
	        ht.put("SHOW_POS_SUMMARY", SHOW_POS_SUMMARY);
	        ht.put("ISMANAGEWORKFLOW", ISMANAGEWORKFLOW);
	        ht.put("ALLOWCATCH_ADVANCE_SEARCH", ALLOWCATCH_ADVANCE_SEARCH);
	        ht.put("SETCURRENTDATE_ADVANCE_SEARCH", SETCURRENTDATE_ADVANCE_SEARCH);
	        ht.put("ISSHOWPOSPRICEBYOUTLET", ISSHOWPOSPRICEBYOUTLET);
	        ht.put("ALLOWPOSTRAN_LESSTHAN_AVBQTY", ALLOWPOSTRAN_LESSTHAN_AVBQTY);
	        ht.put("ISAUTO_CONVERT_ESTPO", ISAUTO_CONVERT_ESTPO);
	        ht.put("ISPRODUCTMAXQTY", ISPRODUCTMAXQTY);
	        ht.put("ISPOSTAXINCLUSIVE", ISPOSTAXINCLUSIVE);
	        ht.put("ISSALESAPP_TAXINCLUSIVE", ISSALESAPP_TAXINCLUSIVE);
	        ht.put("ISPOSSALESMAN_BYBILLPRODUCT", ISPOSSALESMAN);
	        ht.put("ALLOWPRODUCT_TO_OTHERCOMPANY", ALLOWPRDTOCOMPANY);
	        ht.put("ISAUTO_CONVERT_RECEIPTBILL", ISAUTO_CONVERT_RECEIPTBILL);
	        ht.put("SETCURRENTDATE_GOODSRECEIPT", SETCURRENTDATE_GOODSRECEIPT);
	        
	        ht.put("SETCURRENTDATE_PICKANDISSUE", SETCURRENTDATE_PICKANDISSUE);
	        ht.put("SHOWITEM_AVGCOST", SHOWITEM_AVGCOST);
	        ht.put("ISAUTO_CONVERT_SOINVOICE", ISAUTO_CONVERT_SOINVOICE);
	        ht.put("ENABLE_RESERVEQTY", ENABLE_RESERVEQTY);
	        ht.put("ISSUPPLIERMANDATORY", ISSUPPLIERMANDATORY);
			ht.put("ISPRICE_UPDATEONLY_IN_OWNOUTLET", ISPRICE_UPDATEONLY_IN_OWNOUTLET);
			ht.put("ISSTOCKTAKE_BYAVGCOST", ISSTOCKTAKE_BYAVGCOST);
	        ht.put("ISSALESTOPURCHASE", ISSALESTOPURCHASE);
	        ht.put("NUMBER_OF_PAYMENT", NOOFPAYMENT);
	        ht.put("NUMBER_OF_JOURNAL", NOOFJOURNAL);
	        ht.put("NUMBER_OF_CONTRA", NOOFCONTRA);
	        ht.put("PEPPOL_ID", PEPPOL_ID);
	        ht.put("shopify", ("on".equals(shopify) ? 1 : 0));
	        ht.put("lazada", ("on".equals(lazada) ? 1 : 0));
	        ht.put("shopee", ("on".equals(shopee) ? 1 : 0));
	        ht.put("amazon", ("on".equals(amazon) ? 1 : 0));
	        ht.put("ISPEPPOL", ("on".equals(ISPEPPOL) ? 1 : 0));
	        ht.put("ISREFERENCEINVOICE", ISREFERENCEINVOICE);
	        ht.put("ISEMPLOYEEVALIDATEPO", ISEMPLOYEEVALIDATEPO);
	        ht.put("ISEMPLOYEEVALIDATESO", ISEMPLOYEEVALIDATESO);
	        ht.put("ISAUTO_CONVERT_ISSUETOINVOICE", ISAUTO_CONVERT_ISSUETOINVOICE);
	        

	       
	    
	      
		    boolean itemInserted = plantmstutil.insertPlantMst(ht,sUserId);
		    
		    if(itemInserted) 
		    {
		        res = "<font class = "+"maingreen"+">Company  "+ " "+ PLANT +" "+"Created Successfully</font>";
		        PLANT="";
		        PLNTDESC="";
		        PEPPOL_UKEY="";
		        companyregnumber="";
		        ISTAXREG="0";
		        COMP_INDUSTRY="";
		        REPROTSBASIS="";
		        STARTDATE="";
		        EXPIRYDATE="";
		        ACTUALEXPIRYDATE="";
		        NAME="";
		        DESGINATION     = "";
		        TELNO     = "";
		        HPNO    = "";
		        EMAIL    = "";
		        ADD1   = "";
		        ADD2   ="";
		        ADD3   = "";
		        ADD4   = "";
		        ZIP="";
		        COUNTY="";
		        REMARKS="";
		        FAX="";RCBNO="";
		        SALESPERCENT="0";
	            SDOLLARFRATE="0";
	            SCENTSFRATE="0";
	            EDOLLARFRATE="0";
	            ECENTSFRATE="0";    
	            TAXBY="0";     
	            TAXBYLABEL="0";    
	            STATE="";
	            ENABLEINVENTORY = "0";
	            ENABLEACCOUNTING = "0";
	            OWNERAPP = "0";
	            CUSTOMERAPP = "0";
	            MANAGERAPP = "0";
	            STOREAPP = "0";
	            RIDEAPP = "0";
	            NOOFSUPPLIER="Unlimited";
	            NOOFCUSTOMER="Unlimited";
	            NOOFUSER="Unlimited";
	            NOOFEMPLOYEE="Unlimited";
	            NOOFINVENTORY="500";
	            NOOFLOCATION="Unlimited";
	            NOOFORDER="Unlimited";
	            NOOFEXPBILLINV="Unlimited";
	            NOOFPAYMENT="Unlimited";
	            NOOFJOURNAL="Unlimited";
	            NOOFCONTRA="Unlimited";
	            shopify="";
	            lazada="";
	            shopee="";
	            amazon="";
	            ISPEPPOL="";
	            PEPPOL_ID="";
	            PRODUCT_SHOWBY_CATAGERY="";
	            ISASSIGN_USERLOC="";
	            SHOW_STOCKQTY_ONAPP="";
	            ISPOSVOID_TRAN="";
	            SHOW_POS_SUMMARY="";
	            ISPOSRETURN_TRAN="";
	            ISMANAGEWORKFLOW="";
	            ALLOWCATCH_ADVANCE_SEARCH="";
	            SETCURRENTDATE_ADVANCE_SEARCH="";
	            ISSHOWPOSPRICEBYOUTLET="";
	            ALLOWPOSTRAN_LESSTHAN_AVBQTY="";
	            ISAUTO_CONVERT_ESTPO="";
	            ISPRODUCTMAXQTY="";
	            ISPOSTAXINCLUSIVE="";
	            ISSALESAPP_TAXINCLUSIVE="";
	            ISPOSSALESMAN="";
	            ALLOWPRDTOCOMPANY="";
	            ISAUTO_CONVERT_RECEIPTBILL="";
	            SETCURRENTDATE_GOODSRECEIPT="";
	            ISEMPLOYEEVALIDATEPO="";
	            ISEMPLOYEEVALIDATESO="";
	            ISAUTO_CONVERT_ISSUETOINVOICE="";
	            SETCURRENTDATE_PICKANDISSUE="";
	            SHOWITEM_AVGCOST="";
	            ISAUTO_MINMAX_MULTIESTPO="";
	            ISAUTO_CONVERT_SOINVOICE="";
	            ENABLE_RESERVEQTY="";
	            ISSUPPLIERMANDATORY="";
				ISPRICE_UPDATEONLY_IN_OWNOUTLET="";
				ISSTOCKTAKE_BYAVGCOST="";
	            ISSALESTOPURCHASE="";
	            ISREFERENCEINVOICE="";
		     } 
		     else 
		     {
		         res = "<font class = "+"black"+">Failed to add Company </font>";
		                        
		     }
		
		 }
		}
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

#applogofile {
   opacity: 0;
   position: absolute;
   z-index: -1;
}


#sealfile {
   opacity: 0;
   position: absolute;
   z-index: -1;
}

#signfile {
   opacity: 0;
   position: absolute;
   z-index: -1;
}
</style>
<div class="container-fluid m-t-20">
	 <div class="box"> 
            <div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title"><%=title %></h1>
              <h1 style="font-size: 18px; cursor: pointer;"
				class="box-title pull-right"
				onclick="window.location.href='../home'">
				<i class="glyphicon glyphicon-remove"></i>
			</h1>
		</div>
		
 <div class="box-body">
<center>
	<h2>
		<small class="<%=msg%>"><%=fieldDesc%></small>
	</h2>
</center>
   <CENTER><strong><%=res%></strong></CENTER>
   <form class="form-horizontal" name="form1" method="post" autocomplete="off" action="/track/MasterServlet?action=ADD_PLANT" enctype="multipart/form-data" onsubmit="return onAdd()">
   <div class="col-sm-6">
	   <div class="form-group">
	      <label class="control-label col-form-label col-sm-6" for="Company ID">Company ID</label>
	      <div class="col-sm-6">
	      <div class="input-group">   
	      	  	 <input class="form-control" name="PLANT" type = "TEXT" value="<%=PLANT%>" size="30"  MAXLENGTH=50 readonly> 
	  		</div>
	      </div>
	    </div>
	    
	   <div class="form-group">
	      <label class="control-label col-form-label col-sm-6 required" for="Company Name">Company Name</label>
	      <div class="col-sm-6">
	      <div class="input-group">   
	      	  	 <input class="form-control" name="PLNTDESC" type = "TEXT" value="<%=PLNTDESC%>" size="30"  MAXLENGTH=100> 
	  		</div>
	      </div>
	    </div>
	    
	     <div class="form-group" id="UEN">
	      <label class="control-label col-form-label col-sm-6" for="Unique Entity Number (UEN)">Unique Entity Number (UEN)</label>
	      <div class="col-sm-6">
	      <div class="input-group">   
	      	  	 <input class="form-control" name="companyregnumber" type = "TEXT" value="<%=companyregnumber%>" size="30"  MAXLENGTH=100> 
	  		</div>
	      </div>
	    </div>
	    
	    <div class="form-group">
	      <label class="control-label col-form-label col-sm-6 required" for="Start Date">Start Date</label>
	      <div class="col-sm-6">
	      <div class="input-group">   
	      	  	 <input class="form-control datepicker" name="STARTDATE" type = "TEXT" value="<%=STARTDATE%>" size="50"  MAXLENGTH=20 readonly>
	  		</div>
	      </div>
	    </div>
	    
	    <div class="form-group">
	      <label class="control-label col-form-label col-sm-6 required" for="Expiry Date">Expiry Date</label>
	      <div class="col-sm-6">
	      <div class="input-group">   
	      	  	 <input class="form-control datepicker" name="EXPIRYDATE" class="inactiveGry" type = "TEXT" value="<%=EXPIRYDATE%>"  size="50"  MAXLENGTH=20 readonly> 
	  		</div>
	      </div>
	    </div>
	    
	    <div class="form-group">
	      <label class="control-label col-form-label col-sm-6" for="Actual Expiry Date">Actual Expiry Date</label>
	      <div class="col-sm-6">
	      <div class="input-group">   
	      	  	 <input class="form-control" name="ACTUALEXPIRYDATE" type = "TEXT"  value="<%=ACTUALEXPIRYDATE%>" onfocus="javascript:checkDate();" size="30"    MAXLENGTH=20  readonly> 
	  		</div>
	      </div>
	    </div>
	    
	    <div class="form-group" style="display:none;">
	      <label class="control-label col-form-label col-sm-6" for="Number Of Catalogs">Number Of Catalogs</label>
	      <div class="col-sm-6">
	      <div class="input-group">   
	      	  	 <input class="form-control" name="NOOFCATALOGS" type = "TEXT" value="<%=NOOFCATALOGS%>" size="30"  MAXLENGTH=30 > 
	  		</div>
	      </div>
	    </div>

	    <div class="form-group">
	      <label class="control-label col-form-label col-sm-6 required" for="User">Number of User</label>
	      <div class="col-sm-6">
	      <div class="input-group">   
	      	  	 <SELECT class="form-control" data-toggle="dropdown" data-placement="right" name="No_of_User" id="No_of_User"></SELECT> 
	  		</div>
	      </div>
	    </div>
	    	    
	    <div class="form-group">
	      <label class="control-label col-form-label col-sm-6" for="Number Of Signatures">Number Of Signatures</label>
	      <div class="col-sm-6">
	      <div class="input-group">   
	      	  	 <input class="form-control" name="NOOFSIGNATURES" type = "TEXT" value="<%=NOOFSIGNATURES%>" size="30"  MAXLENGTH=30 > 
	  		</div>
	      </div>
	    </div>
	    
	    <div class="form-group">
	      <label class="control-label col-form-label col-sm-6 required" for="Base Currency">Base Currency</label>
	      <div class="col-sm-6">
	      <div class="input-group">   
	      	  	 <SELECT class="form-control" data-toggle="dropdown" data-placement="right" name="BaseCurrency">
				<option value= "0" >Choose : </option>
			<%
			Hashtable ht = new Hashtable();
			   ArrayList ccList = countryNCurrencyDAO.getCurrencyList(ht);
				for(int i=0 ; i<ccList.size();i++)
	      		 {
					Map m=(Map)ccList.get(i);
					String country = (String)m.get("COUNTRY_NAME");
			        String currency = (String)m.get("CURRENCY_CODE"); %>
			        <option <%if(currencyCode.equalsIgnoreCase(currency)){%>  selected <%} %> value= <%=currency%> ><%=country %>(<%=currency%>) </option>		          
			        <%
	       			}
				 %>
			</select> 
	  		</div>
	      </div>
	    </div>
	    
	    <div class="form-group">
	      <label class="control-label col-form-label col-sm-6 required" for="decimal_precision">Number
							of Decimals</label>
	      <div class="col-sm-6">
	      <div class="input-group">   
	      	  	 <SELECT class="form-control" data-toggle="dropdown" data-placement="right"
								name="decimal_precision">
								<option value="2" selected>2</option>
								<option value="3">3</option>
								<option value="4">4</option>
								<option value="5">5</option>
							</SELECT> 
	  		</div>
	      </div>
	    </div>
	    
	    <div class="form-group">
	      <label class="control-label col-form-label col-sm-6 required" for="Supplier">Number
							of Supplier</label>
	      <div class="col-sm-6">
	      <div class="input-group">   
	      	  	 <SELECT class="form-control" data-toggle="dropdown" data-placement="right"
								name="No_of_Supplier" id="No_of_Supplier">
							</SELECT> 
	  		</div>
	      </div>
	    </div>
	    
	    <div class="form-group">
	      <label class="control-label col-form-label col-sm-6 required" for="Customer">Number
							of Customer</label>
	      <div class="col-sm-6">
	      <div class="input-group">   
	      	  	 <SELECT class="form-control" data-toggle="dropdown" data-placement="right"
								name="No_of_Customer" id="No_of_Customer">
								
							</SELECT> 
	  		</div>
	      </div>
	    </div>
	    
	    <div class="form-group">
	      <label class="control-label col-form-label col-sm-6 required" for="Employee">Number
							of Employee</label>
	      <div class="col-sm-6">
	      <div class="input-group">   
	      	  	 <SELECT class="form-control" data-toggle="dropdown" data-placement="right"
								name="No_of_Employee" id="No_of_Employee">
								
							</SELECT> 
	  		</div>
	      </div>
	    </div>
	    
	    <div class="form-group">
	      <label class="control-label col-form-label col-sm-6 required" for="Inventory">Number
							of Inventory</label>
	      <div class="col-sm-6">
	      <div class="input-group">   
	      	  	 <SELECT class="form-control" data-toggle="dropdown" data-placement="right"
								name="No_of_Inventory" id="No_of_Inventory">
							</SELECT> 
	  		</div>
	      </div>
	    </div>
	    
<!-- imti start -->	    
	     <div class="form-group">
	      <label class="control-label col-form-label col-sm-6 required" for="Inventory">Number
							of Location</label>
	      <div class="col-sm-6">
	      <div class="input-group">   
	      	  	 <SELECT class="form-control" data-toggle="dropdown" data-placement="right"
								name="No_of_Location" id="No_of_Location">
							</SELECT> 
	  		</div>
	      </div>
	    </div>
	    
	     <div class="form-group">
	      <label class="control-label col-form-label col-sm-6 required" for="Inventory">Number
							of Order</label>
	      <div class="col-sm-6">
	      <div class="input-group">   
	      	  	 <SELECT class="form-control" data-toggle="dropdown" data-placement="right"
								name="No_of_Order" id="No_of_Order">
							</SELECT> 
	  		</div>
	      </div>
	    </div>
<!-- imti end -->	
<!-- Resvi start Number of Expenses/Bill/Invoice Quot./Invoices 24/6/21 -->

  <div class="form-group">
	      <label class="control-label col-form-label col-sm-6 required" for="Inventory">Number of Expenses/Bill/Invoice Quot./Invoices</label>
	      <div class="col-sm-6">
	         <div class="input-group">   
	      	  	 <SELECT class="form-control" data-toggle="dropdown" data-placement="right"
								name="No_of_Exp_Bill_Inv" id="No_of_Exp_Bill_Inv">
							</SELECT> 
	  		  </div>
	      </div>
	      </div>
	      
<!-- 	      Resvi End -->


<!-- Resvi start     -->

         <div class="form-group">
	      <label class="control-label col-form-label col-sm-6 required" for="Inventory">Number
							of Payment</label>
	      <div class="col-sm-6">
	         <div class="input-group">   
	      	  	 <SELECT class="form-control" data-toggle="dropdown" data-placement="right"
								name="No_of_Payment" id="No_of_Payment">
							</SELECT> 
	  		  </div>
	      </div>
	      </div>
	      
	      
           <div class="form-group">
	      <label class="control-label col-form-label col-sm-6 required" for="Inventory">Number
							of Journal</label>
	      <div class="col-sm-6">
	      <div class="input-group">   
	      	  	 <SELECT class="form-control" data-toggle="dropdown" data-placement="right"
								name="No_of_Journal" id="No_of_Journal">
							</SELECT> 
	  		</div>
	      </div>
	    </div>
	    
	    
	    <div class="form-group">
	      <label class="control-label col-form-label col-sm-6 required" for="Inventory">Number
							of Contra</label>
	      <div class="col-sm-6">
	      <div class="input-group">   
	      	  	 <SELECT class="form-control" data-toggle="dropdown" data-placement="right"
								name="No_of_Contra" id="No_of_Contra">
							</SELECT> 
	  		</div>
	      </div>
	    </div>
	    
	    
<!-- 	    End -->
	    <div class="form-group">
	      	<label class="control-label col-form-label col-sm-6 required" for="Purchase System">Purchase System</label>
	      	<div class="col-sm-6">  
	        <input type="checkbox" value="i" name="ENABLE_INVENTORY" id="ENABLE_INVENTORY" /> Order Management&nbsp;&nbsp;
	        <input type="checkbox" value="a" name="ENABLE_ACCOUNTING" id="ENABLE_ACCOUNTING" /> Accounting&nbsp;&nbsp;
	        <input type="checkbox" value="p" name="ENABLE_PAYROLL" id="ENABLE_PAYROLL" /> Payroll&nbsp;&nbsp;
	        <input type="checkbox" value="o" name="ENABLE_POS" id="ENABLE_POS" /> POS&nbsp;&nbsp;
	       </div>
	    	</div>
<!-- 	    <div class="form-group">
	    <label class="control-label col-form-label col-sm-6" for="Access System">Access System</label>
	    <div class="col-sm-6"> 
	     <input type="checkbox" value="o" name="OWNER_APP" id="OWNER_APP" /> Owner App&nbsp;&nbsp;
	        <input type="checkbox" value="m" name="MANAGER_APP" id="MANAGER_APP" /> Manager App&nbsp;&nbsp;
	        <input type="checkbox" value="s" name="STORE_APP" id="STORE_APP" /> Store App&nbsp;&nbsp;
	        <input type="checkbox" value="r" name="RIDE_APP" id="RIDE_APP" /> Rider App
	    </div>
	    </div>	 -->
	    	<!-- <div class="form-group">
	      <label class="control-label col-form-label col-sm-6" for="Upload your company logo">Upload Your Company Logo</label>
	      <div class="col-sm-6">                
	        <INPUT class="form-control" name="IMAGE_UPLOAD" type="File" size="20" MAXLENGTH=100>
	      </div>
	    </div>
	    
	    <div class="form-group">
	      <label class="control-label col-form-label col-sm-6" for="Upload your company seal">Upload Your Company Seal</label>
	      <div class="col-sm-6">                
	        <INPUT class="form-control" name="seal" type="File" size="20" MAXLENGTH=100>
	      </div>
	    </div>
	    
	    <div class="form-group">
	      <label class="control-label col-form-label col-sm-6" for="Upload your company signature">Upload Your Company Signature</label>
	      <div class="col-sm-6">                
	        <INPUT class="form-control" name="dsignature" type="File" size="20" MAXLENGTH=100>
	      </div>
	    </div> -->
    </div>
    <div class="col-sm-6">
    		<div class="row" style="height:588px">
    		<div class="row">
    		<div class="col-sm-12" style="padding-bottom: 5%;">
				<div class="col-sm-12"  style="text-align: left;">
						<label>Company Logo:</label>
				</div>
				<div class="col-sm-5" style="border: 1px dashed #ddd;border-radius: 4px;padding: 10px;text-align: center;">
						<div id="item_logo" hidden>
							<img id="item_img_logo1" src="" style="width: 100px; height: 50px"/>
						</div>
						<div  id="item_btn_logo">
							<label for="logofile" style="color: #337ab7;">Upload your logo</label>
							<input accept="image/gif,image/jpeg,image/png,image/bmp" name="IMAGE_UPLOAD"  type="File" size="20" id="logofile" onchange="readURLLogo(this);">
					  	</div>
				</div>
				<div class="col-sm-7">
					<p style="font-size: 10px;text-align: left;">This logo will shows on Purchase, Sales Estimate, Sales order, Invoice, Bill, Payslip etc.</p>
					<div id="logoremove" style="text-align: left;" hidden>
						<a href="#" onClick="image_delete_new();">Remove Logo</a>	
					</div>
				</div>
			</div>
			
			<!-- imthi -->
			<div class="col-sm-12" style="padding-bottom: 5%;">
				<div class="col-sm-12"  style="text-align: left;">
						<label>Order App Background Image:</label>
				</div>
				<div class="col-sm-5" style="border: 1px dashed #ddd;border-radius: 4px;padding: 10px;text-align: center;">
						<div id="app_logo" hidden>
							<img id="app_img_logo1" src="" style="width: 100px; height: 50px"/>
						</div>
						<div  id="app_btn_logo">
							<label for="applogofile" style="color: #337ab7;">Upload Order App Image</label>
							<input accept="image/gif,image/jpeg,image/png,image/bmp" name="APP_IMAGE_UPLOAD"  type="File" size="20" id="applogofile" onchange="readURLAPPBACKGROUND(this);">
					  	</div>
				</div>
				<div class="col-sm-7">
					<p style="font-size: 10px;text-align: left;">This will shows on Order App Background.</p>
					<div id="applogoremove" style="text-align: left;" hidden>
						<a href="#" onClick="app_image_delete_new();">Remove Order App Image</a>	
					</div>
				</div>
			</div>
			
			
			<div class="col-sm-12" style="padding-bottom: 5%;">
				<div class="col-sm-12"  style="text-align: left;">
					<label>Company Seal:</label>
				</div>
				<div class="col-sm-5" style="border: 1px dashed #ddd;border-radius: 4px;padding: 10px;text-align: center;">
				
						<div id="item_seal"  hidden>
							<img id="item_img_seal1" src="" style="width: 100px; height: 50px"/>
						</div>
						<div  id="item_btn_seal">
							<label for="sealfile" style="color: #337ab7;">Upload your seal</label>
							<input accept="image/gif,image/jpeg,image/png,image/bmp" name="seal"  type="file" size="20"  id="sealfile" onchange="readURLSeal(this);">
					  	</div>
				</div>
				<div class="col-sm-7" style="text-align: left;">
					<p style="font-size: 10px;text-align: left;">This seal will shows on Payslip.</p>
					<div style="text-align: left;" id="sealremove" hidden>
						<a href="#" onClick="seal_delete_new();">Remove Seal</a>
					</div>
				</div>
			</div>
			<div class="col-sm-12" style="padding-bottom: 5%;">
				<div class="col-sm-12"  style="text-align: left;">
					<label>Company Signature:</label>
				</div>
				<div class="col-sm-5" style="border: 1px dashed #ddd;border-radius: 4px;padding: 10px;text-align: center;">
						<div id="item_sign" hidden>
							<img id="item_img_sign1" src="" style="width: 100px; height: 50px"/>
						</div>
						<div  id="item_btn_sign">
							<label for="signfile" style="color: #337ab7;">Upload your signature</label>
							<input accept="image/gif,image/jpeg,image/png,image/bmp" name="dsignature"  type="File" size="20" id="signfile" onchange="readURLSign(this);">
					  	</div>
				</div>
				<div class="col-sm-7" style="text-align: left;">
					<p style="font-size: 10px;text-align: left;">This signature will shows on Payslip.</p>
					<div style="text-align: left;" id="signremove" hidden>
						<a href="#" onClick="sign_delete_new();">Remove Signature</a>
					</div>	
				</div>
			</div>
			</div>
</div>
    </div>
    <div class="col-sm-12">
    <div class="bs-example">
     <ul class="nav nav-tabs" id="myTab"> 
     	<li class="nav-item active">
            <a href="#tax" class="nav-link" data-toggle="tab" aria-expanded="true">Tax/VAT Details</a>
        </li>
        <li class="nav-item">
            <a href="#other" class="nav-link" data-toggle="tab">Other Details</a>
        </li>
        <li class="nav-item">
            <a href="#contact" class="nav-link" data-toggle="tab">Contact Details</a>
        </li>
        <li class="nav-item">
            <a href="#home" class="nav-link" data-toggle="tab">Address</a>
        </li>
        <li class="nav-item">
            <a href="#integration" class="nav-link" data-toggle="tab">Integrations</a>
        </li>
        <li class="nav-item">
            <a href="#appaccess" class="nav-link" data-toggle="tab">App Access</a>
        </li>
         <li class="nav-item">
            <a href="#CompIndustry" class="nav-link" data-toggle="tab">Company Industry</a>
        </li>
         <!-- <li class="nav-item">
            <a href="#POS" class="nav-link" data-toggle="tab">POS</a>
        </li> -->
        <li class="nav-item">
            <a href="#remark" class="nav-link" data-toggle="tab">Remarks</a>
        </li>
        <li class="nav-item">
            <a href="#attachment" class="nav-link" data-toggle="tab">Attachment</a>
        </li>
        </ul>
        <div class="tab-content clearfix">
        <div class="tab-pane active" id="tax">
        <br>
        <div class="form-group">
      <label class="control-label col-form-label col-sm-2 required" for="IS Tax Registered">Is Tax Registered</label>
      <div class="col-sm-4">
      <div class="input-group">   
      	  	 <select class="form-control" name="ISTAXREGISTRED">
  <%if(ISTAXREG.equalsIgnoreCase("1")) {%>
	  <option value="0">NO</option>
  	  <option value="1" selected>YES</option>
  <%} else {%>
  	  <option value="0" selected>NO</option>
  	  <option value="1">YES</option>
  	<%} %>
  </select> 
  		</div>
      </div>
    </div>
    
    <div class="form-group">
      <label class="control-label col-form-label col-sm-2" for="TRN/RCB/Tax No" id="TaxLabel"></label>
      <div class="col-sm-4">
      <div class="input-group">   
      	  	 <input class="form-control" name="RCBNO" type = "TEXT" value="<%=RCBNO%>" size="30"  MAXLENGTH=30> 
  		</div>
      </div>
    </div>
    
    <div class="form-group">
      <label class="control-label col-form-label col-sm-2 required" for="Tax By">Tax By</label>
      <div class="col-sm-4">
      <div class="input-group">   
      	  	 <SELECT class="form-control" data-toggle="dropdown" data-placement="right" name="TAXBY">
			<option value= "0" disabled selected>Choose : </option>
	        <option value="BYORDER">BYORDER</option>
		    <option value="BYPRODUCT">BYPRODUCT</option>
		</select> 
  		</div>
      </div>
    </div>
    
    <div class="form-group">
      <label class="control-label col-form-label col-sm-2 required" for="Tax By Label">Tax By Label Order Configuration</label>
      <div class="col-sm-4">
      <div class="input-group">   
      	  	 <SELECT onchange="getval()" id="select_id" class="form-control" data-toggle="dropdown" data-placement="right" name="TAXBYLABEL">
			<option value= "0" style="display:none;">Choose : </option>
			 <option selected value="VAT">VAT</option>
             <option selected value="GST">GST</option>
	         <option selected value="TRN">TRN</option>
	         <option selected value="RCB">RCB</option>
	         <option selected value="TAX">TAX</option>
	         <option selected value="UEN">UEN</option>
		</select> 
  		</div>
      </div>
    </div>
    
    <div class="form-group">
      <label class="control-label col-form-label col-sm-2 required" for="Tax By Label Order Management">Tax By Label Order Management</label>
      <div class="col-sm-4">
      <div class="input-group">   
      	  	 <SELECT class="form-control" data-toggle="dropdown" data-placement="right" name="TAXBYLABELORDERMANAGEMENT">
			 <option selected value="0">Choose : </option>
			 <option selected value="VAT">VAT</option>
             <option selected value="GST">GST</option>
	         <option selected value="TAX">TAX</option>
	         <%
			
		   ArrayList taxbylabeorderlist = _PlantMstDAO.getTaxByLabeOrderList(Plant);
			for(int i=0 ; i<taxbylabeorderlist.size();i++)
      		 {
				Map m=(Map)taxbylabeorderlist.get(i);
				strtaxbylabe1order = (String)m.get("TAXBYLABELORDERMANAGEMENT");
			 %>
		      <OPTION value="<%=strtaxbylabe1order%>"  
		      <%if(TAXBYLABELORDERMANAGEMENT.equalsIgnoreCase(strtaxbylabe1order)){%> selected <%}%>><%=strtaxbylabe1order%>
		      </OPTION>
		     <%} %>
		</select> 
  		</div>
  		
      </div>
    </div>
       </div>
       <div class="tab-pane fade" id="other">
        <br>
        <div class="form-group">
      <label class="control-label col-form-label col-sm-2" for="Report Basis">Report Basis</label>
      <div class="col-sm-4">
      <div class="input-group">   
      	  	 <%if(REPROTSBASIS.equalsIgnoreCase("Cash")) {%> 
  				<div>
  				 <input name="REPROTSBASIS" id="reportbasisaccru"  type="radio" value="Accrual">
  			 	 <label class="control-label">Accrual <span class="muted">(you owe tax as of invoice date)</span></label> 
  				  
  				</div>
  				<div>
  				<input name="REPROTSBASIS" id="reportbasiscash"  type="radio" value="Cash" checked>
  				<label class="control-label">Cash <span class="muted">(you owe tax upon payment receipt)</span></label> 
  				 
  				</div>
  			  <%} else {%>
  			  	<div>
  				 <input name="REPROTSBASIS" id="reportbasisaccru"  type="radio" value="Accrual" checked>
  			 	 <label class="control-label">Accrual <span class="muted">(you owe tax as of invoice date)</span></label> 
  				  
  				</div>
  				<div>
  				<input name="REPROTSBASIS" id="reportbasiscash"  type="radio" value="Cash">
  				<label class="control-label">Cash <span class="muted">(you owe tax upon payment receipt)</span></label> 
  				 
  				</div>
  			  <%} %> 
  		</div>
      </div>
    </div>
        
        <div class="form-group">
      <label class="control-label col-form-label col-sm-2" for="Fiscal Year">Fiscal Year</label>
      <div class="col-sm-4">
      <div class="input-group">   
      	  	 <input class="form-control datepicker" name="FISCALYEAR" type = "TEXT" value="<%=FISCALYEAR%>" size="50"  MAXLENGTH=20 readonly>
  		</div>
      </div>
    </div>
    
    <div class="form-group">
      <label class="control-label col-form-label col-sm-2" for="PayRoll Year">PayRoll Year</label>
      <div class="col-sm-4">
      <div class="input-group">   
      	  	 <input class="form-control datepicker" name="PAYROLLYEAR" type = "TEXT" value="<%=PAYROLLYEAR%>"  size="50"  MAXLENGTH=20 readonly> 
  		</div>
      </div>
    </div>
    
    <div class="form-group">
      <label class="control-label col-form-label col-sm-2" for="Employee Working Man Days">Employee Working Man Days</label>
      <div class="col-sm-4">                
        <label class="radio-inline">
      <INPUT name="EMPLOYEEWORKINGMANDAYSBY"  type = "radio" value="30DAYS"    <%if(EMPLOYEEWORKINGMANDAYSBY.equalsIgnoreCase("30DAYS")) {%>checked <%}%> >By 30 Days 
    </label>
    <label class="radio-inline">
      <INPUT name="EMPLOYEEWORKINGMANDAYSBY" type = "radio" value="MONTHLY"    <%if(EMPLOYEEWORKINGMANDAYSBY.equalsIgnoreCase("MONTHLY")) {%>checked <%}%>  >By Monthly
    </label>
      </div>
    </div>
        
       </div>
       
        <div class="tab-pane fade" id="home">
        <br>
        <div class="form-group">
      <label class="control-label col-form-label col-sm-2 required" for="Region">Region</label>
      <div class="col-sm-4">  
       <SELECT class="form-control" data-toggle="dropdown" data-placement="right" onchange="OnRegion(this.value)" id="REGION" name="REGION" value="<%=REGION%>" style="width: 100%">
				<OPTION style="display:none;">Select Region</OPTION>
				<%
		   
		   ccList =  _MasterUtil.getRegionList("");
			for(int i=0 ; i<ccList.size();i++)
      		 {
				Map m=(Map)ccList.get(i);
				String vREGION = (String)m.get("REGION"); %>
		        <option  value='<%=vREGION%>' ><%=vREGION%> </option>		          
		        <%
       			}
			 %></SELECT>
    </div>
    </div>
    
        <div class="form-group">
      <label class="control-label col-form-label col-sm-2 required" for="Country">Country</label>
      <div class="col-sm-4">
			 <SELECT class="form-control" data-toggle="dropdown" data-placement="right" onchange="OnCountry(this.value)" id="COUNTRY_CODE" name="COUNTRY_CODE" value="<%=COUNTY%>" style="width: 100%">
				<OPTION style="display:none;">Select Country</OPTION>
				</SELECT>
			 <INPUT type="hidden" name="COUNTY" value="<%=COUNTY%>">      
       <%-- <input class="form-control" name="COUNTY" type = "TEXT" value="<%=COUNTY%>" size="30"  MAXLENGTH=40 > --%>
      </div>
    </div>
    
       <div class="form-group">
      <label class="control-label col-form-label col-sm-2" for="Unit No">Unit No.</label>
      <div class="col-sm-4">
        <input class="form-control" name="ADD1" type = "TEXT" value="<%=ADD1%>" size="30"  MAXLENGTH=40 >
      </div>
    </div>
    
    <div class="form-group">
      <label class="control-label col-form-label col-sm-2" for="Building">Building</label>
      <div class="col-sm-4">
        <input class="form-control" name="ADD2" type = "TEXT" value="<%=ADD2%>" size="30"  MAXLENGTH=40>
      </div>
    </div>
    
    <div class="form-group">
      <label class="control-label col-form-label col-sm-2" for="Street">Street</label>
      <div class="col-sm-4">
        <input class="form-control" name="ADD3" type = "TEXT" value="<%=ADD3%>" size="30"  MAXLENGTH=40 >
      </div>
    </div>
    
    <div class="form-group">
      <label class="control-label col-form-label col-sm-2" for="City">City</label>
      <div class="col-sm-4">
	<input class="form-control" name="ADD4" type = "TEXT" value="<%=ADD4%>" size="30"  MAXLENGTH=40>
      </div>
    </div>
    
    <div class="form-group">
      <label class="control-label col-form-label col-sm-2" for="State">State</label>
      <div class="col-sm-4">
      <SELECT class="form-control" data-toggle="dropdown" data-placement="right" id="STATE" name="STATE" value="<%=STATE%>" style="width: 100%">
				<OPTION style="display:none;">Select State</OPTION>
				</SELECT>
      </div>
      <%-- <input class="form-control" name="STATE" type = "TEXT" value="<%=STATE%>" size="30"  MAXLENGTH=40> --%>
    </div>
    
    <div class="form-group">
      <label class="control-label col-form-label col-sm-2 required" for="Postal Code">Postal Code</label>
      <div class="col-sm-4">
        <input class="form-control" name="ZIP" type = "TEXT" value="<%=ZIP%>" size="30"  MAXLENGTH=10  >
      </div>
    </div>
    
       </div>
       
       <div class="tab-pane fade" id="contact">
        <br>
       <div class="form-group">
      <label class="control-label col-form-label col-sm-2 required" for="Contact Name">Contact Name</label>
      <div class="col-sm-4">
      <div class="input-group">   
      	  	 <input name="NAME" type="TEXT" value="<%=NAME%>" size="30" onchange="javascript:checkDate();" MAXLENGTH=100 class="form-control"> 
  		</div>
      </div>
    </div>
    
    <div class="form-group">
      <label class="control-label col-form-label col-sm-2" for="Designation">Designation</label>
      <div class="col-sm-4">
      <div class="input-group">   
      	  	 <input class="form-control" name="DESGINATION" type = "TEXT" value="<%=DESGINATION%>" size="30"  MAXLENGTH=30> 
  		</div>
      </div>
    </div>
    
    <div class="form-group">
      <label class="control-label col-form-label col-sm-2 required" for="Telephone No.">Telephone No.</label>
      <div class="col-sm-4">
      <div class="input-group">   
      	  	 <input class="form-control" name="TELNO" type = "TEXT" value="<%=TELNO%>" size="30"  MAXLENGTH=20 onkeypress="return validateInput(event)"> 
  		</div>
      </div>
    </div>
    
    <div class="form-group">
      <label class="control-label col-form-label col-sm-2" for="Hand Phone No.">Hand Phone No.</label>
      <div class="col-sm-4">
      <div class="input-group">   
      	  	 <input class="form-control" name="HPNO" type = "TEXT" value="<%=HPNO%>" size="30"  MAXLENGTH=20 onkeypress="return validateInput(event)"> 
  		</div>
      </div>
    </div>
    
    <div class="form-group">
      <label class="control-label col-form-label col-sm-2" for="Fax">Fax</label>
      <div class="col-sm-4">
      <div class="input-group">   
      	  	 <input class="form-control" name="FAX" type = "TEXT" value="<%=FAX%>" size="30"  MAXLENGTH=20 onkeypress="return validateInput(event)"> 
  		</div>
      </div>
    </div>
    
    <div class="form-group">
      <label class="control-label col-form-label col-sm-2" for="Email">Email</label>
      <div class="col-sm-4">
      <div class="input-group">   
      	  	 <input class="form-control" name="EMAIL" type = "TEXT" value="<%=EMAIL%>" size="30"  MAXLENGTH=50> 
  		</div>
      </div>
    </div>
    
    <div class="form-group">
      	<label class="control-label col-form-label col-sm-2" for="Website">Website</label>
      	<div class="col-sm-4">  
    	<INPUT name="WEBSITE" type="TEXT" value="<%=WEBSITE%>" size="50" MAXLENGTH=50 class="form-control">
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
    
       </div>
       <div class="tab-pane fade" id="integration">
       <div class="col-sm-12 col=md-4 col-lg-4">
       	<fieldset>
       		<legend>Shopping Cart</legend>
       		<input type="checkbox" name="shopify" <%= "1".equals(shopify) ? "checked='checked'" : "" %>/> Shopify
       	</fieldset>
       	</div>
       	<div class="col-sm-12 col=md-4 col-lg-4">
       	<fieldset>
       		<legend>eCommerce</legend>
       		<input type="checkbox" name="lazada" <%= "1".equals(lazada) ? "checked='checked'" : "" %>/> Lazada
       	<br><input type="checkbox" name="shopee" <%= "1".equals(shopee) ? "checked='checked'" : "" %>/> Shopee
       	<br><input type="checkbox" name="amazon" <%= "1".equals(amazon) ? "checked='checked'" : "" %>/> Amazon
       	</fieldset>
       	</div>
       	<div class="col-sm-12 col=md-4 col-lg-4">
       	<fieldset>
       		<legend>Peppol</legend>
       		<input type="checkbox" name="ISPEPPOL" id="ISPEPPOL" <%= "1".equals(ISPEPPOL) ? "checked='checked'" : "" %>/> Peppol
       		<br><INPUT name="PEPPOL_ID" id="PEPPOL_ID" type="TEXT" value="<%=PEPPOL_ID%>"	size="50" MAXLENGTH=50 class="form-control" placeholder="Peppol ID">
			<br><input name="PEPPOL_UKEY" id="PEPPOL_UKEY" type="TEXT" value="<%=PEPPOL_UKEY%>" size="200" maxlength="200" class="form-control" placeholder="Peppol Ukey">
       	</fieldset>
       	</div>
       </div>
       
        <div class="tab-pane fade" id="appaccess"><br>
        <div class="panel panel-default">
        <div class="panel-heading" style="background: #eaeafa"><strong>Access System</strong></div>
        <div class="panel-body">
        	 <TABLE class="table1" style="font-size:14px;width: 100%;">
        	 <TR>
        	 
        	  <TH WIDTH="20%" ALIGN = "LEFT">
              <label class="checkbox-inline"> 
              <input type="checkbox" value="o" name="OWNER_APP" id="OWNER_APP" /> Owner App&nbsp;&nbsp;
              </label>
              
        	  <TH WIDTH="20%" ALIGN = "LEFT">
              <label class="checkbox-inline"> 
              <input type="checkbox" value="c" name="CUSTOMER_APP" id="CUSTOMER_APP" /> Customer App&nbsp;&nbsp;
              </label>
              
        	  <TH WIDTH="20%" ALIGN = "LEFT">
              <label class="checkbox-inline"> 
	          <input type="checkbox" value="m" name="MANAGER_APP" id="MANAGER_APP" /> Manager App&nbsp;&nbsp;
              </label>
              
        	  <TH WIDTH="20%" ALIGN = "LEFT">
              <label class="checkbox-inline"> 
	          <input type="checkbox" value="s" name="STORE_APP" id="STORE_APP" /> Store App&nbsp;&nbsp;
              </label>
              
        	  <TH WIDTH="20%" ALIGN = "LEFT">
              <label class="checkbox-inline"> 
	          <input type="checkbox" value="r" name="RIDE_APP" id="RIDE_APP" /> Delivery App	
              </label>
  		      </TABLE>
        </div>
        </div>
        </div>

       
        <div class="tab-pane fade" id="CompIndustry">
        <br>
         <div class="form-group">
      <label class="control-label col-form-label col-sm-2 required" for="Company Industry">Company Industry</label>
      <div class="col-sm-4">
      <div class="input-group">   
      	  	 <select class="form-control" data-toggle="dropdown" data-placement="right" name="CompanyIndustry">
     
	  <option value="General" selected>General</option>
  	  <option value="Construction">Construction</option>
  	  <option value="Retail">Retail</option>
  	  <option value="Warehouse" >Warehouse</option>
  	  <option value="Service" >Service</option>
  	  <option value="Centralised Kitchen" >Centralised Kitchen</option>
  	  <option value="Education" >Education</option>
  	  <option value="Customs" >Customs</option>
    
      </select> 
      
      
  		</div>
      </div>
      </div>
      <br>
  <div class="panel panel-default">  
  <div class="panel-heading" style="background: #eaeafa"><strong>Purchase Order</strong></div>  
 <div class="panel-body">
 <TABLE class="table1" style="font-size:14px;width: 100%;"> 
  <TR>
  <TH WIDTH="20%" ALIGN = "LEFT">	
  	<div class="form-inline">
  	<label class ="checkbox-inline">
	<input type = "checkbox" id = "ISPRODUCTMAXQTY" name = "ISPRODUCTMAXQTY" value = "ISPRODUCTMAXQTY"
	<%if(ISPRODUCTMAXQTY.equals("1")) {%>checked <%}%> />Check Product Max Stock Qty (Multiple Purchase Estimate Order, Purchase Estimate Order, Purchase Order)</label>
  	</div>
  	
  <TH WIDTH="20%" ALIGN = "LEFT">	
     <div class="form-inline">
  	<label class ="checkbox-inline">
	<input type = "checkbox" id = "ISAUTO_CONVERT_ESTPO" name = "ISAUTO_CONVERT_ESTPO" value = "ISAUTO_CONVERT_ESTPO"
	<%if(ISAUTO_CONVERT_ESTPO.equals("1")) {%>checked <%}%> />Purchase Estimate To Purchase Order Automatic Conversion</label>
  	</div>
  	
 <TH WIDTH="20%" ALIGN = "LEFT">	
    <div class="form-inline">
  	<label class ="checkbox-inline">
	<input type = "checkbox" id = "ISAUTO_CONVERT_RECEIPTBILL" name = "ISAUTO_CONVERT_RECEIPTBILL" value = "ISAUTO_CONVERT_RECEIPTBILL"
	<%if(ISAUTO_CONVERT_RECEIPTBILL.equals("1")) {%>checked <%}%> />Goods Receipt To Bill Automatic Conversion</label>
  	</div>
  	
  	<tr>
  	
 <TH WIDTH="20%" ALIGN = "LEFT">	
    <div class="form-inline">
  	<label class ="checkbox-inline">
	<input type = "checkbox" id = "SHOWITEM_AVGCOST" name = "SHOWITEM_AVGCOST" value = "SHOWITEM_AVGCOST"
	<%if(SHOWITEM_AVGCOST.equals("1")) {%>checked <%}%> />Show Product Master Purchase Cost Based On Average Cost</label>
  	</div>
    <TH WIDTH="20%" ALIGN = "LEFT">	
    <div class="form-inline">
  	<label class ="checkbox-inline">
	<input type = "checkbox" id = "ISAUTO_MINMAX_MULTIESTPO" name = "ISAUTO_MINMAX_MULTIESTPO" value = "ISAUTO_MINMAX_MULTIESTPO"
	<%if(ISAUTO_MINMAX_MULTIESTPO.equals("1")) {%>checked <%}%> />Auto Multiple Purchase Estimate Order By Min/Max Quantity Reorder Level</label>
  	</div>
	
	<TH WIDTH="20%" ALIGN = "LEFT">	
    <div class="form-inline">
  	<label class ="checkbox-inline">
	<input type = "checkbox" id = "SETCURRENTDATE_GOODSRECEIPT" name = "SETCURRENTDATE_GOODSRECEIPT" value = "SETCURRENTDATE_GOODSRECEIPT"
	<%if(SETCURRENTDATE_GOODSRECEIPT.equals("1")) {%>checked <%}%> />Purchase Order Goods Receipt - Show Default Transaction Date As Current Date</label>
  	</div>
  	
  	<TR>
  <TH WIDTH="20%" ALIGN = "LEFT">	
  	<div class="form-inline">
  	<label class ="checkbox-inline">
	<input type = "checkbox" id = "ISEMPLOYEEVALIDATEPO" name = "ISEMPLOYEEVALIDATEPO" value = "ISEMPLOYEEVALIDATEPO"
	<%if(ISEMPLOYEEVALIDATEPO.equals("1")) {%>checked <%}%> />Set Employee as mandatory for (Multiple Purchase Estimate Order, Purchase Estimate Order, Purchase Order, Bill)</label>
  	</div>
	
  	</Table>
  	</div>
  	</div>
  	
  	 <div class="panel panel-default">  
  <div class="panel-heading" style="background: #eaeafa"><strong>Sales Order</strong></div>  
 <div class="panel-body">
 <TABLE class="table1" style="font-size:14px;width: 100%;"> 
  <TR>
 <TH WIDTH="10%" ALIGN = "LEFT">	
 	<div class="form-inline">
  	<label class ="checkbox-inline" >
	<input type = "checkbox" id = "ISAUTO_CONVERT_SOINVOICE" name = "ISAUTO_CONVERT_SOINVOICE"  value = "ISAUTO_CONVERT_SOINVOICE"
	<%if(ISAUTO_CONVERT_SOINVOICE.equals("1")) {%>checked <%}%> />Sales Order To Invoice Automatic Conversion</label>
  	</div>
 <TH WIDTH="20%" ALIGN = "LEFT">	
 	<div class="form-inline">
  	<label class ="checkbox-inline"  >
	<input type = "checkbox" id = "ISSALESTOPURCHASE" name = "ISSALESTOPURCHASE"  value = "ISSALESTOPURCHASE"
	<%if(ISSALESTOPURCHASE.equals("1")) {%>checked <%}%> />Sales Order To Purchase Order To Automatic Conversion(By Pick & Issue)</label>
  	</div>
  	
 <TH WIDTH="20%" ALIGN = "LEFT">	
 	<div class="form-inline">
  	<label class ="checkbox-inline"  >
	<input type = "checkbox" id = "ISREFERENCEINVOICE" name = "ISREFERENCEINVOICE"  value = "ISREFERENCEINVOICE"
	<%if(ISREFERENCEINVOICE.equals("1")) {%>checked <%}%> />Is Reference Mandatory For Invoice</label>
  	</div>
  	
    <TR>
  	   <TH WIDTH="20%" ALIGN = "LEFT">
	  <div class="form-group comproprice">
      <label class="control-label col-form-label col-sm-12" for="Sales">Sales App Product Rates Are</label>
      <div class="col-sm-12" style="padding-bottom: 0%;text-align: left;">
			<label class="radio-inline">
      	<INPUT name="ISSALESAPP_TAXINCLUSIVE"  type = "radio"  value="0"  id="ISSALESAPP_TAXINCLUSIVE" <%if (ISSALESAPP_TAXINCLUSIVE.equalsIgnoreCase("0")) {%> checked <%}%>>Tax Exclusive
    	</label>
    	<label class="radio-inline">
      	<INPUT  name="ISSALESAPP_TAXINCLUSIVE" type = "radio" value="1"  id = "ISSALESAPP_TAXINCLUSIVE" <%if (ISSALESAPP_TAXINCLUSIVE.equalsIgnoreCase("1")) {%> checked <%}%> >Tax Inclusive
    	</label>				
      </div>
    </div>
    <TH WIDTH="10%" ALIGN = "LEFT">	
 	<div class="form-inline">
  	<label class ="checkbox-inline" >
	<input type = "checkbox" id = "ENABLE_RESERVEQTY" name = "ENABLE_RESERVEQTY"  value = "ENABLE_RESERVEQTY"
	<%if(ENABLE_RESERVEQTY.equals("1")) {%>checked <%}%> />Enable Reserve Qty </label>
	</div>
     <TH WIDTH="20%" ALIGN = "LEFT">	
    <div class="form-inline">
  	<label class ="checkbox-inline">
	<input type = "checkbox" id = "SETCURRENTDATE_PICKANDISSUE" name = "SETCURRENTDATE_PICKANDISSUE" value = "SETCURRENTDATE_PICKANDISSUE"
	<%if(SETCURRENTDATE_PICKANDISSUE.equals("1")) {%>checked <%}%> />Sales Order Pick & Issue - Show Default Transaction Date As Current Date</label>
  	</div>
  	<TR>
  	<TH WIDTH="20%" ALIGN = "LEFT">	
    <div class="form-inline">
  	<label class ="checkbox-inline">
	<input type = "checkbox" id = "ISEMPLOYEEVALIDATESO" name = "ISEMPLOYEEVALIDATESO" value = "ISEMPLOYEEVALIDATESO"
	<%if(ISEMPLOYEEVALIDATESO.equals("1")) {%>checked <%}%> />Set Employee as mandatory for (Sales Estimate Order, Sales Order, Invoice)</label>
  	</div>
  	
  	<TH WIDTH="20%" ALIGN = "LEFT">	
    <div class="form-inline">
  	<label class ="checkbox-inline">
	<input type = "checkbox" id = "ISAUTO_CONVERT_ISSUETOINVOICE" name = "ISAUTO_CONVERT_ISSUETOINVOICE" value = "ISAUTO_CONVERT_ISSUETOINVOICE"
	<%if(ISAUTO_CONVERT_ISSUETOINVOICE.equals("1")) {%>checked <%}%> />Sales Order Pick & Issue To Invoice Automatic Conversion</label>
  	</div>
  	
 <%-- <TH WIDTH="20%" ALIGN = "LEFT">	
  	<div class="row">
	<div class="col-sm-12" style="padding-bottom: 4%;text-align: left; display:none">
	<label class ="checkbox-inline">
	<input type = "checkbox" id = "PRODUCT_SHOWBY_CATAGERY" name = "PRODUCT_SHOWBY_CATAGERY" value = "PRODUCT_SHOWBY_CATAGERY"
	<%if(PRODUCT_SHOWBY_CATAGERY.equals("1")) {%>checked <%}%> />POS Product Show By Category</label>
	</div>
  	</div>
  
 <TH WIDTH="20%" ALIGN = "LEFT">	
  	<div class="row">  
	<div class="col-sm-12" style="padding-bottom: 4%;text-align: left; display:none">
	<label class ="checkbox-inline">
	<input type = "checkbox" id = "ISASSIGN_USERLOC" name = "ISASSIGN_USERLOC" value = "ISASSIGN_USERLOC"
	<%if(ISASSIGN_USERLOC.equals("1")) {%>checked <%}%> />POS Assign User Location</label>
	</div>
    </div> --%>
 
    
    
   
    
<%--     <TR>
 <TH WIDTH="20%" ALIGN = "LEFT">	
   <div class="row">  
	<div class="col-sm-12" style="padding-bottom: 5%;text-align: left;">
	<label class ="checkbox-inline">
	<input type = "checkbox" id = "ISMANAGEWORKFLOW" name = "ISMANAGEWORKFLOW" value = "ISMANAGEWORKFLOW"
	<%if(ISMANAGEWORKFLOW.equals("1")) {%>checked <%}%> />Manage Workflow</label>
	</div>
    </div> --%>
 	
  	
  	</TABLE>
  	</div>
  	</div>
  	
  	 <div class="form-group">
     <div class="panel panel-default">  
  <div class="panel-heading" style="background: #eaeafa"><strong>POS</strong></div>  
 <div class="panel-body">
  <table class="table1" style="font-size:14px;width: 100%;"> 
 <TR>
 
   <TH WIDTH="20%" ALIGN = "LEFT">
<div class="form-group comproprice">
      <label class="control-label col-form-label col-sm-12" for="POS">POS Product Rates Are</label>
      <div class="col-sm-12" style="padding-bottom: 0%;text-align: left;">
			<label class="radio-inline">
      	<INPUT name="ISPOSTAXINCLUSIVE"  type = "radio"  value="0"  id="ISPOSTAXINCLUSIVE" <%if (ISPOSTAXINCLUSIVE.equalsIgnoreCase("0")) {%> checked <%}%>>Tax Exclusive
    	</label>
    	<label class="radio-inline">
      	<INPUT  name="ISPOSTAXINCLUSIVE" type = "radio" value="1"  id = "ISPOSTAXINCLUSIVE" <%if (ISPOSTAXINCLUSIVE.equalsIgnoreCase("1")) {%> checked <%}%> >Tax Inclusive
    	</label>				
      </div>
    </div>
  
 <TH WIDTH="20%" ALIGN = "LEFT">	
 	<div class="row">  
	<div class="col-sm-12" style="padding-bottom: 0%;text-align: left;">
	<label class ="checkbox-inline">
	<input type = "checkbox" id = "ISPOSRETURN_TRAN" name = "ISPOSRETURN_TRAN" value = "ISPOSRETURN_TRAN"
	<%if(ISPOSRETURN_TRAN.equals("1")) {%>checked <%}%> />Allow POS Return Transaction</label>
	</div>
    </div>
    
    <TH WIDTH="20%" ALIGN = "LEFT">	
 	<div class="row">  
	<div class="col-sm-12" style="padding-bottom: 0%;text-align: left;">
	<label class ="checkbox-inline">
	<input type = "checkbox" id = "ISPOSVOID_TRAN" name = "ISPOSVOID_TRAN" value = "ISPOSVOID_TRAN"
	<%if(ISPOSVOID_TRAN.equals("1")) {%>checked <%}%> />Allow POS Void Transaction</label>
	</div>
    </div>
    
    <TR>
    	<TH WIDTH="20%" ALIGN = "LEFT">
<div class="form-group comproprice">
      <label class="control-label col-form-label col-sm-12" for="POS">POS Sales Man</label>
      <div class="col-sm-12" style="padding-bottom: 0%;text-align: left;">
			<label class="radio-inline">
      	<INPUT name="ISPOSSALESMAN"  type = "radio"  value="0"  id="ISPOSSALESMAN" <%if (ISPOSSALESMAN.equalsIgnoreCase("0")) {%> checked <%}%>>By Bill
    	</label>
    	<label class="radio-inline">
      	<INPUT  name="ISPOSSALESMAN" type = "radio" value="1"  id = "ISPOSSALESMAN" <%if (ISPOSSALESMAN.equalsIgnoreCase("1")) {%> checked <%}%> >By Product
    	</label>				
      </div>
    </div>
    
    <TH WIDTH="20%" ALIGN = "LEFT">	
 	<div class="row">  
	<div class="col-sm-12" style="padding-bottom: 0%;text-align: left;">
	<label class ="checkbox-inline">
	<input type = "checkbox" id = "SHOW_POS_SUMMARY" name = "SHOW_POS_SUMMARY" value = "SHOW_POS_SUMMARY"
	<%if(SHOW_POS_SUMMARY.equals("1")) {%>checked <%}%> />Show POS Sales Summary</label>
	</div>
    </div>
    
     <TH WIDTH="20%" ALIGN = "LEFT">	
 	<div class="row">  
	<div class="col-sm-12" style="padding-bottom: 0%;text-align: left;">
	<label class ="checkbox-inline">
	<input type = "checkbox" id = "SHOW_STOCKQTY_ONAPP" name = "SHOW_STOCKQTY_ONAPP" value = "SHOW_STOCKQTY_ONAPP"
	<%if(SHOW_STOCKQTY_ONAPP.equals("1")) {%>checked <%}%> />Show Available Qty On App</label>
	</div>
    </div>
    
    	<TR>
	
	<TH WIDTH="20%" ALIGN = "LEFT">
	<div class="row">
		<div class="col-sm-12" style="padding-bottom: 5%;text-align: left;">
			<label class ="checkbox-inline">
			<input type = "checkbox" id = "ISSHOWPOSPRICEBYOUTLET" name = "ISSHOWPOSPRICEBYOUTLET" value = "ISSHOWPOSPRICEBYOUTLET"
			<%if(ISSHOWPOSPRICEBYOUTLET.equals("1")) {%>checked <%}%> />Show POS Price By Outlet</label>
		</div>
	</div>
	</TH>
	
	<TH WIDTH="20%" ALIGN = "LEFT">
	<div class="row">
		<div class="col-sm-12" style="padding-bottom: 5%;text-align: left;">
			<label class ="checkbox-inline">
			<input type = "checkbox" id = "ALLOWPOSTRAN_LESSTHAN_AVBQTY" name = "ALLOWPOSTRAN_LESSTHAN_AVBQTY" value = "ALLOWPOSTRAN_LESSTHAN_AVBQTY"
			<%if(ALLOWPOSTRAN_LESSTHAN_AVBQTY.equals("1")) {%>checked <%}%> />Allow POS Transaction Less Than Available Quantity</label>
		</div>
	</div>
	</TH>
 
 
 	</table>
 	</div>
 	</div>
    </div>
  	
  	<div class="form-group">	
  	  <div class="panel panel-default">  
  <div class="panel-heading" style="background: #eaeafa"><strong>Product Automatic Creation</strong></div>  
 <div class="panel-body">
  <table class="table1" style="font-size:14px;width: 100%;"> 
  	<tr>
 	<th align="LEFT" width="20%">
		<div class="form-group comproprice">
      		<!-- <label class="control-label col-form-label col-sm-12" for="POS">Add Product To Other Company</label> -->
      		<div class="col-sm-12" style="padding-bottom: 0%;text-align: left;">
				<label class="radio-inline"><input type="radio" name="ALLOWPRDTOCOMPANY" style="border:0;" value="0" id="ALLOWPRDTOCOMPANY" <%if (ALLOWPRDTOCOMPANY.equalsIgnoreCase("0")) {%> checked <%}%>>None</label>
    			<label class="radio-inline"><input type="radio" name="ALLOWPRDTOCOMPANY" style="border:0;" value="1" id="ALLOWPRDTOCOMPANY" <%if (ALLOWPRDTOCOMPANY.equalsIgnoreCase("1")) {%> checked <%}%>>Allow Create And Edit Parent Product In Child Company</label>			
    			<label class="radio-inline"><input type="radio" name="ALLOWPRDTOCOMPANY" style="border:0;" value="2" id="ALLOWPRDTOCOMPANY" <%if (ALLOWPRDTOCOMPANY.equalsIgnoreCase("2")) {%> checked <%}%>>Allow Create And Edit Product In Child Company</label>
    			<label class="radio-inline"><input type="radio" name="ALLOWPRDTOCOMPANY" style="border:0;" value="3" id="ALLOWPRDTOCOMPANY" <%if (ALLOWPRDTOCOMPANY.equalsIgnoreCase("3")) {%> checked <%}%>>Both</label>			
      		</div>
    	</div>
	</th>
	
  </table>
  </div>
  </div>
    </div>
    
    
    <div class="form-group">	
  	  <div class="panel panel-default">  
  <div class="panel-heading" style="background: #eaeafa"><strong>Manage Workflow</strong></div>  
 <div class="panel-body">
  <table class="table1" style="font-size:14px;width: 100%;">
    <TR>
    	<TH WIDTH="20%" ALIGN = "LEFT">
		<div class="row">
		<div class="col-sm-12" style="padding-bottom: 0%;text-align: left;">
			<label class ="checkbox-inline">
			<input type = "checkbox" id = "ISMANAGEWORKFLOW" name = "ISMANAGEWORKFLOW"   style="border:0;" value = "ISMANAGEWORKFLOW"
			<%if(ISMANAGEWORKFLOW.equals("1")) {%>checked <%}%> />Manage Workflow</label>
		</div>
	</div>
    
   </table>
   </div>
   </div>
   </div>
    <div class="form-group">	
  	  <div class="panel panel-default">  
  <div class="panel-heading" style="background: #eaeafa"><strong>Advance Search</strong></div>  
 <div class="panel-body">
  <table class="table1" style="font-size:14px;width: 100%;">
    <TR>
    	<TH WIDTH="20%" ALIGN = "LEFT">
		<div class="row">
		<div class="col-sm-12" style="padding-bottom: 0%;text-align: left;">
			<label class ="checkbox-inline">
			<input type = "checkbox" id = "ALLOWCATCH_ADVANCE_SEARCH" name = "ALLOWCATCH_ADVANCE_SEARCH"   style="border:0;" value = "ALLOWCATCH_ADVANCE_SEARCH"
			<%if(ALLOWCATCH_ADVANCE_SEARCH.equals("1")) {%>checked <%}%> />Enable/Disable Advance Search Cache</label>
		</div>
	</div>
	</TH>
    	<TH WIDTH="20%" ALIGN = "LEFT">
		<div class="row">
		<div class="col-sm-12" style="padding-bottom: 0%;text-align: left;">
			<label class ="checkbox-inline">
			<input type = "checkbox" id = "SETCURRENTDATE_ADVANCE_SEARCH" name = "SETCURRENTDATE_ADVANCE_SEARCH"   style="border:0;" value = "SETCURRENTDATE_ADVANCE_SEARCH"
			<%if(SETCURRENTDATE_ADVANCE_SEARCH.equals("1")) {%>checked <%}%> />Show Current Date as Default  In Advance Search</label>
		</div>
	</div>
	</TH>
    
   </table>
   </div>
   </div>
   </div>
   
        <div class="form-group">	
  	  <div class="panel panel-default">  
  <div class="panel-heading" style="background: #eaeafa"><strong>Settings</strong></div>  
 <div class="panel-body">
  <table class="table1" style="font-size:14px;width: 100%;">
    <TR>
			<TH WIDTH="20%" ALIGN = "LEFT">
			<div class="row">
			<div class="col-sm-12" style="padding-bottom: 0%;text-align: left;">
			<label class ="checkbox-inline">
			<input type = "checkbox" id = "ISSUPPLIERMANDATORY" name = "ISSUPPLIERMANDATORY" value = "ISSUPPLIERMANDATORY"
			<%if(ISSUPPLIERMANDATORY.equals("1")) {%>checked <%}%> />Set Mandatory For Supplier In Product Master </label>
		</div>
	</div>
	</TH>  	
			<TH WIDTH="20%" ALIGN = "LEFT">
			<div class="row">
			<div class="col-sm-12" style="padding-bottom: 0%;text-align: left;">
			<label class ="checkbox-inline">
			<input type = "checkbox" id = "ISPRICE_UPDATEONLY_IN_OWNOUTLET" name = "ISPRICE_UPDATEONLY_IN_OWNOUTLET" value = "ISPRICE_UPDATEONLY_IN_OWNOUTLET"
			<%if(ISPRICE_UPDATEONLY_IN_OWNOUTLET.equals("1")) {%>checked <%}%> />Update List Price only on own Outlet</label>
		</div>
	</div>
	</TH>  
			<TH WIDTH="20%" ALIGN = "LEFT">
			<div class="row">
			<div class="col-sm-12" style="padding-bottom: 0%;text-align: left;">
			<label class ="checkbox-inline">
			<input type = "checkbox" id = "ISSTOCKTAKE_BYAVGCOST" name = "ISSTOCKTAKE_BYAVGCOST" value = "ISSTOCKTAKE_BYAVGCOST"
			<%if(ISSTOCKTAKE_BYAVGCOST.equals("1")) {%>checked <%}%> />Stock Take by Avg. Cost</label>
		</div>
	</div>
	</TH>  
	
   </table>
   </div>
   </div>
   </div>
    
    
    </div>
    
   <!--  <div class="form-group">
      <label class="control-label col-form-label col-sm-2 required" for="Company Industry"></label>
      <div class="col-sm-4">
      <div class="input-group">
      </div>
  		</div>
   
    </div> -->
    
  <!--   <div class="tab-pane fade" id="POS">
        <br>
         <div class="form-group">
    <div class="panel-body">
 <TABLE class="table1" style="font-size:14px;width: 100%;"> 
  <TR>
   

  
  
   </table>
  
    </div>
    </div>
    </div> -->
    
       <div class="tab-pane fade" id="remark">
        <br>
        
        <div class="form-group">
        <div class="col-sm-2">
				<label class="control-label col-form-label">Remarks</label>
			</div>
        <div class="col-sm-6">
							<textarea rows="2" name="REMARKS"
								class="ember-text-area form-control ember-view"  maxlength="1000" placeholder="Max 1000 characters" <%=REMARKS%>></textarea>
								<%-- <input class="form-control" name="REMARKS" type = "TEXT" value="<%=REMARKS%>" size="30"  MAXLENGTH=100 > --%>						
					</div>
                </div>
       </div>
       <div class="tab-pane fade" id="attachment">
        <br>
        
        <div class="row grey-bg">
					<div class="col-sm-4">
						<div class="form-inline">
							<label for="email">Attach Files(s)</label>
							<div class="attch-section">
								<input type="file" class="form-control input-attch"
									id="compyAttch" name="file" multiple="true">
								<div class="input-group">
									<svg version="1.1" id="Layer_1"
										xmlns="http://www.w3.org/2000/svg" x="0" y="0"
										viewBox="0 0 512 512" xml:space="preserve"
										class="icon icon-xs align-text-top action-icons input-group-addon"
										style="height: 30px; display: inline-block; color: #c63616;">
										<path
											d="M262.4 512c-35.7 0-64.2-10.5-84.9-31.4-24.7-24.9-37.1-63.8-36.8-115.6.2-32.6-.3-248.7-1.4-268.1-.9-15.9 4.8-41.6 23.6-61.5 11.4-12 32-26.3 66-26.3 30.3 0 55.1 15.7 69.8 44.2 10.1 19.7 12.2 38.8 12.4 40.9l.1.9v.9l.5 236.9v.5c-1 19.2-15.9 47.6-53 47.6h-.7c-39.1-.4-53.7-30.4-56-46.2l-.2-1.3V179.6c0-10.2 8.3-18.5 18.5-18.5s18.5 8.3 18.5 18.5v150.5c.4 1.5 1.4 4.4 3.6 7.2s6.2 6.5 16 6.6c9.2.1 12.4-3.2 14.1-6 1.6-2.6 2.2-5.6 2.3-6.3l-.7-234.5c-.4-3-2.4-15.6-8.8-27.6-8.3-15.7-20.2-23.3-36.4-23.3-16.7 0-29.8 5-39.1 14.8-10.7 11.3-14 26.6-13.6 34 1.2 21.6 1.6 244.3 1.4 270.3-.2 41.6 8.5 71.7 26 89.3 13.5 13.6 33.2 20.4 58.7 20.4 17.2 0 31.8-5.9 43.5-17.7 18.9-18.9 30.1-53.4 30-92.2 0-19.6-.1-193.2-.1-250.9 0-10.2 8.3-18.5 18.5-18.5s18.5 8.3 18.5 18.5c0 57.6.1 231.2.1 250.8.1 49.1-14.8 92.3-40.8 118.4-18.6 18.7-42.7 28.6-69.6 28.6z"></path></svg>
									<button type="button" class="btn btn-sm btn-attch">Upload
										File</button>
								</div>

							</div>
						</div>
						<div id="compyAttchNote">
							<small class="text-muted"> You can upload a maximum of 5
								files, 2MB each </small>
						</div>
					</div>
					
				</div>
        
        </div>
       </div> 
       </div>
     </div>
    <div class="form-group">        
     <div class="col-sm-offset-4 col-sm-8">
	 <!-- <button type="button" class="Submit btn btn-default" onClick="window.location.href='../home'"><b>Back</b></button>&nbsp;&nbsp; -->
	 <button type="button" class="Submit btn btn-default" onClick="onNew();">Clear</button>&nbsp;&nbsp;
	 <button type="Submit" class="btn btn-success" data-toggle="modal" data-target="#myModal">Save</button>&nbsp;&nbsp;
 	</div>
 	</div>
   </form>
   <script type="text/javascript">
function defaultSales(){
	  var val = 0;
	  for( i = 0; i < document.form1.SALES.length; i++ )
	  {
		  if( document.form1.SALES[i].checked == true )
		  {
			  val = document.form1.SALES[i].value;
			  if(val=='PERCENT')
			  {
	              document.form1.SDOLLARFRATE.value="0";
	              document.form1.SCENTSFRATE.value="0";
			  }else{
				  document.form1.SALESPERCENT.value="0";
              }
	  	  }
	  }
}
</script>
   </div>
   </div>
   </div>  

<script>
        $(document).ready(function(){
        	var  d = document.getElementById("select_id").value;
            document.getElementById('TaxLabel').innerHTML = d + " No.:";

            $('#No_of_Supplier').empty();
            $('#No_of_Supplier').append('<option value="Unlimited" selected>Unlimited</option>');
            $('#No_of_Supplier').append('<option value="5">5</option>');
            $('#No_of_Supplier').append('<option value="10">10</option>');
            for (var i = 30; i < 1000; i++) {
        	$('#No_of_Supplier').append('<option value="' + i + '">' + i + '</option>');
        	if(i==480)
        		$('#No_of_Supplier').append('<option value="500">500</option>');
        	i += 29;
            }
            $('#No_of_Supplier').append('<option value="1000">1000</option>');
            
            $('#No_of_Customer').empty();
            $('#No_of_Customer').append('<option value="Unlimited" selected>Unlimited</option>');
            $('#No_of_Customer').append('<option value="5">5</option>');
            $('#No_of_Customer').append('<option value="10">10</option>');
            for (var i = 30; i < 1000; i++) {
        	$('#No_of_Customer').append('<option value="' + i + '">' + i + '</option>');
        	if(i==480)
        		$('#No_of_Customer').append('<option value="500">500</option>');
        	i += 29;
            }
            $('#No_of_Customer').append('<option value="1000">1000</option>');
            
            $('#No_of_Employee').empty();
            $('#No_of_Employee').append('<option value="Unlimited" selected>Unlimited</option>');
            $('#No_of_Employee').append('<option value="5">5</option>');
            $('#No_of_Employee').append('<option value="10">10</option>');
            for (var i = 25; i < 1000; i++) {
        	$('#No_of_Employee').append('<option value="' + i + '">' + i + '</option>');
        	i += 24;
            }
            $('#No_of_Employee').append('<option value="1000">1000</option>');
            
            $('#No_of_User').empty();
            $('#No_of_User').append('<option value="Unlimited" selected>Unlimited</option>');
            $('#No_of_User').append('<option value="5">5</option>');
            $('#No_of_User').append('<option value="10">10</option>');
            $('#No_of_User').append('<option value="15">15</option>');
            $('#No_of_User').append('<option value="20">20</option>');
            $('#No_of_User').append('<option value="25">25</option>');
            $('#No_of_User').append('<option value="30">30</option>');
            $('#No_of_User').append('<option value="35">35</option>');
            $('#No_of_User').append('<option value="40">40</option>');
            $('#No_of_User').append('<option value="45">45</option>');
            $('#No_of_User').append('<option value="50">50</option>');

            
//imti start
            $('#No_of_Location').empty();
            $('#No_of_Location').append('<option value="Unlimited" selected>Unlimited</option>');
            for (var i = 1; i < 100; i++) {
        	$('#No_of_Location').append('<option value="' + i + '">' + i + '</option>');
        	//i += 9;
            }
            for (var i = 100; i < 550; i++) {
            	$('#No_of_Location').append('<option value="' + i + '">' + i + '</option>');
            	i += 49;
                }
            
            $('#No_of_Order').empty();
            $('#No_of_Order').append('<option value="Unlimited" selected>Unlimited</option>');
            $('#No_of_Order').append('<option value="5">5</option>');
            $('#No_of_Order').append('<option value="10">10</option>');
            $('#No_of_Order').append('<option value="20">20</option>');
            $('#No_of_Order').append('<option value="50">50</option>');
            $('#No_of_Order').append('<option value="100">100</option>');
            $('#No_of_Order').append('<option value="200">200</option>');
            $('#No_of_Order').append('<option value="500">500</option>');
            $('#No_of_Order').append('<option value="1000">1000</option>');
            $('#No_of_Order').append('<option value="2000">2000</option>');
            for (var i = 5000; i < 105000; i++) {
        	$('#No_of_Order').append('<option value="' + i + '">' + i + '</option>');
        	i += 4999;
            }
//imti end            

//  Resvi Start   


            $('#No_of_Exp_Bill_Inv').empty();
            $('#No_of_Exp_Bill_Inv').append('<option value="Unlimited" selected>Unlimited</option>');
            $('#No_of_Exp_Bill_Inv').append('<option value="10">10</option>');
            $('#No_of_Exp_Bill_Inv').append('<option value="20">20</option>');
            $('#No_of_Exp_Bill_Inv').append('<option value="30">30</option>');
            $('#No_of_Exp_Bill_Inv').append('<option value="40">40</option>');
            $('#No_of_Exp_Bill_Inv').append('<option value="50">50</option>');
            $('#No_of_Exp_Bill_Inv').append('<option value="60">60</option>');
            $('#No_of_Exp_Bill_Inv').append('<option value="70">70</option>');
            $('#No_of_Exp_Bill_Inv').append('<option value="80">80</option>');
            $('#No_of_Exp_Bill_Inv').append('<option value="90">90</option>');
            $('#No_of_Exp_Bill_Inv').append('<option value="100">100</option>');
            $('#No_of_Exp_Bill_Inv').append('<option value="200">200</option>');
            $('#No_of_Exp_Bill_Inv').append('<option value="300">300</option>');
            $('#No_of_Exp_Bill_Inv').append('<option value="400">400</option>');
            $('#No_of_Exp_Bill_Inv').append('<option value="500">500</option>');
            $('#No_of_Exp_Bill_Inv').append('<option value="600">600</option>');
            $('#No_of_Exp_Bill_Inv').append('<option value="700">700</option>');
            $('#No_of_Exp_Bill_Inv').append('<option value="800">800</option>');
            $('#No_of_Exp_Bill_Inv').append('<option value="900">900</option>');
            $('#No_of_Exp_Bill_Inv').append('<option value="1000">1000</option>');
            $('#No_of_Exp_Bill_Inv').append('<option value="2000">2000</option>');
            $('#No_of_Exp_Bill_Inv').append('<option value="3000">3000</option>');
            $('#No_of_Exp_Bill_Inv').append('<option value="4000">4000</option>');
            $('#No_of_Exp_Bill_Inv').append('<option value="5000">5000</option>');
            $('#No_of_Exp_Bill_Inv').append('<option value="6000">6000</option>');
            $('#No_of_Exp_Bill_Inv').append('<option value="7000">7000</option>');
            $('#No_of_Exp_Bill_Inv').append('<option value="8000">8000</option>');
            $('#No_of_Exp_Bill_Inv').append('<option value="9000">9000</option>');
            for (var i = 10000; i < 35000; i++) {
        	$('#No_of_Exp_Bill_Inv').append('<option value="' + i + '">' + i + '</option>');
        	i += 4999;
            }
            $('#No_of_Exp_Bill_Inv').append('<option value="40000" <%if(NOOFEXPBILLINV.equals("40000")){%> selected <%}%> >40000</option>');
            $('#No_of_Exp_Bill_Inv').append('<option value="50000" <%if(NOOFEXPBILLINV.equals("50000")){%> selected <%}%> >50000</option>');

            $('#No_of_Payment').empty();
            $('#No_of_Payment').append('<option value="Unlimited" selected>Unlimited</option>');
            $('#No_of_Payment').append('<option value="5">5</option>');
            $('#No_of_Payment').append('<option value="10">10</option>');
            $('#No_of_Payment').append('<option value="50">50</option>');
            $('#No_of_Payment').append('<option value="100">100</option>');
            $('#No_of_Payment').append('<option value="200">200</option>');
            $('#No_of_Payment').append('<option value="250">250</option>');
            $('#No_of_Payment').append('<option value="300">300</option>');
            $('#No_of_Payment').append('<option value="400">400</option>');
            $('#No_of_Payment').append('<option value="500">500</option>');
            $('#No_of_Payment').append('<option value="600">600</option>');
            $('#No_of_Payment').append('<option value="700">700</option>');
            $('#No_of_Payment').append('<option value="750">750</option>');
            $('#No_of_Payment').append('<option value="800">800</option>');
            $('#No_of_Payment').append('<option value="900">900</option>');
            for (var i = 1000; i < 11000; i++) {
            $('#No_of_Payment').append('<option value="' + i + '">' + i + '</option>');
            i += 999;
            }

            
            
            $('#No_of_Journal').empty();
            $('#No_of_Journal').append('<option value="Unlimited" selected>Unlimited</option>');
            $('#No_of_Journal').append('<option value="5">5</option>');
            $('#No_of_Journal').append('<option value="10">10</option>');
            $('#No_of_Journal').append('<option value="100">100</option>');
            $('#No_of_Journal').append('<option value="1000">1000</option>');
            for (var i = 10000; i < 110000; i++) {
        	$('#No_of_Journal').append('<option value="' + i + '">' + i + '</option>');
        	i += 9999;
            }

            $('#No_of_Contra').empty();
            $('#No_of_Contra').append('<option value="Unlimited" selected>Unlimited</option>');
            $('#No_of_Contra').append('<option value="5">5</option>');
            $('#No_of_Contra').append('<option value="10">10</option>');
            $('#No_of_Contra').append('<option value="100">100</option>');
            $('#No_of_Contra').append('<option value="1000">1000</option>');
            for (var i = 10000; i < 110000; i++) {
        	$('#No_of_Contra').append('<option value="' + i + '">' + i + '</option>');
        	i += 9999;
            }
            
// Resvi  End

            $('#No_of_Inventory').empty();
            $('#No_of_Inventory').append('<option value="50000" selected>50000</option>');
            $('#No_of_Inventory').append('<option value="5" >5</option>');
            $('#No_of_Inventory').append('<option value="10" >10</option>');
            for (var i = 500; i < 15500; i++) {
            if(i==500)
            $('#No_of_Inventory').append('<option value="500" >500</option>');
            else
        		$('#No_of_Inventory').append('<option value="' + i + '">' + i + '</option>');
        	i += 499;
            }
            $('#No_of_Inventory').append('<option value="20000">20000</option>');
            $('#No_of_Inventory').append('<option value="25000">25000</option>');
            $('#No_of_Inventory').append('<option value="30000">30000</option>');
            $('#No_of_Inventory').append('<option value="35000">35000</option>');
            $('#No_of_Inventory').append('<option value="40000">40000</option>');
            $('#No_of_Inventory').append('<option value="45000">45000</option>');
            
            
            $("#myTab a").click(function(e){
                e.preventDefault();
                $(this).tab('show');
            });
            
            $("#compyAttch").change(function(){
            	var files = $(this)[0].files.length;
            	var sizeFlag = false;
            		if(files > 5){
            			$(this)[0].value="";
            			alert("You can upload only a maximum of 5 files");
            			$("#compyAttchNote").html("<small class='text-muted'>  You can upload a maximum of 5 files, 2MB each  </small>");
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
            				$("#compyAttchNote").html("<small class='text-muted'>  You can upload a maximum of 5 files, 2MB each  </small>");
            			}else{
            				$("#compyAttchNote").html(files +" files attached");
            			}
            			
            		}
            	});
        }
        );
        
        function OnRegion(region)
        {
        	  if(region == "GCC"){
    			  var x = document.getElementById("UEN");
    			  x.style.display = "none";
    			}else if(region =="ASIA PACIFIC"){
    				var x = document.getElementById("UEN");
    				  x.style.display = "block";
    			}
            
        	$.ajax({
        		type : "POST",
        		url: '/track/MasterServlet',
        		async : true,
        		dataType: 'json',
        		data : {
        			action : "GET_COUNTRY_DATA",
        			PLANT : "<%=sesplant%>",
        			QUERY : "CREATEPLANT",
        			REGION : region,
        		},
        		success : function(dataitem) {
        			var CountryList=dataitem.COUNTRYMST;
        			var myJSON = JSON.stringify(CountryList);
        			//alert(myJSON);
        			$('#STATE').empty();
        		//	$('#STATE').append('<OPTION style="display:none;">Select State</OPTION>');
        			$('#STATE').append('<OPTION>Select State</OPTION>');
        			$('#COUNTRY_CODE').empty();
        			$('#COUNTRY_CODE').append('<OPTION style="display:none;">Select Country</OPTION>');
        				 $.each(CountryList, function (key, value) {
        					   $('#COUNTRY_CODE').append('<option value="' + value.COUNTRY_CODE + '">' + value.COUNTRYNAME + '</option>');
        					});
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
        			PLANT : "<%=sesplant%>",
        			COUNTRY : Country,
        		},
        		success : function(dataitem) {
        			var StateList=dataitem.STATEMST;
        			var myJSON = JSON.stringify(StateList);
        			//alert(myJSON);
        			$('#STATE').empty();
        			$('#STATE').append('<OPTION style="display:none;">Select State</OPTION>');
        				 $.each(StateList, function (key, value) {
        					   $('#STATE').append('<option value="' + value.text + '">' + value.text + '</option>');
        					});
        		}
        	});	
        	
        }
        $('select[name="COUNTRY_CODE"]').on('change', function(){
            var text = $("#COUNTRY_CODE option:selected").text();
            $("input[name ='COUNTY']").val(text.trim());
        });
        
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

        function readURLAPPBACKGROUND(input) {
            if (input.files && input.files[0]) {
                var reader = new FileReader();
                reader.onload = function (e) {
                    $('#app_img_logo1')
                        .attr('src', e.target.result)
                        .width(100)
                        .height(50);
                };
                reader.readAsDataURL(input.files[0]);
                $('#app_logo').show();
                $('#app_btn_logo').hide();
                $('#applogoremove').show();
            }
        }
        
        function readURLSeal(input) {
            if (input.files && input.files[0]) {
                var reader = new FileReader();
                reader.onload = function (e) {
                    $('#item_img_seal1')
                        .attr('src', e.target.result)
                        .width(100)
                        .height(50);
                };
                reader.readAsDataURL(input.files[0]);
                $('#item_seal').show();
                $('#item_btn_seal').hide();
                $('#sealremove').show();
            }
        }
        
        function readURLSign(input) {
            if (input.files && input.files[0]) {
                var reader = new FileReader();
                reader.onload = function (e) {
                    $('#item_img_sign1')
                        .attr('src', e.target.result)
                        .width(100)
                        .height(50);
                };
                reader.readAsDataURL(input.files[0]);
                $('#item_sign').show();
                $('#item_btn_sign').hide();
                $('#signremove').show();
            }
        }
        
        function image_delete_new(){
        	$('#logofile').val("");
        	$('#item_logo').hide();
            $('#item_btn_logo').show();
            $('#logoremove').hide();
          }	

        function app_image_delete_new(){
        	$('#applogofile').val("");
        	$('#app_logo').hide();
            $('#app_btn_logo').show();
            $('#applogoremove').hide();
          }	
          
        function seal_delete_new(){
        	$('#sealfile').val("");
        	$('#item_seal').hide();
            $('#item_btn_seal').show();
            $('#sealremove').hide();
          }
        
        function sign_delete_new(){
        	$('#signfile').val("");
        	$('#item_sign').hide();
            $('#item_btn_sign').show();
            $('#signremove').hide();
          }
		

        </script>
<style>
	.bs-example{
    	margin: 20px;
    }
</style>
<jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
</jsp:include>