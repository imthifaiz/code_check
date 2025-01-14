<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.gates.*"%>
<%@ page import="com.track.constants.*"%>
<%@ page import="com.track.dao.CustMstDAO"%>
<%@ page import="com.track.dao.VendMstDAO"%>
<%@ page import="com.track.dao.MovHisDAO"%>
<%@ page import="com.track.dao.PoHdrDAO"%>
<%@ page import="com.track.dao.PoDetDAO"%>
<%@ page import="com.track.dao.RecvDetDAO"%>
<%@ page import="com.track.dao.TblControlDAO"%>
<%@ page import="com.track.dao.CustomerBeanDAO"%>
<%@ page import="com.track.dao.PlantMstDAO"%>
<%@ page import="com.track.dao.*"%>
<%@ include file="header.jsp"%>
<!-- Not in Use - Menus status 0 -->
<%
String title = "Edit Supplier";
%>
<%@include file="sessionCheck.jsp" %>
<jsp:include page="header2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
	<jsp:param name="mainmenu" value="<%=IConstants.SETTINGS%>"/>
	<jsp:param name="submenu" value="<%=IConstants.SYSTEM_ADMIN%>"/>
</jsp:include>
<script src="js/typeahead.jquery.js"></script>
<link rel="stylesheet" href="../jsp/css/typeahead.css">
<link rel="stylesheet" href="../jsp/css/accounting.css">
<link rel="stylesheet" href="../jsp/css/bootstrap-datepicker.css">
<script src="../jsp/js/bootstrap-datepicker.min.js"></script>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<script language="JavaScript" type="text/javascript" src="../jsp/js/general.js"></script>


<SCRIPT LANGUAGE="JavaScript">


var subWin = null;
function popUpWin(URL) {
 subWin = window.open(encodeURI(URL), 'CUSTOMERS', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=800,height=400,left = 200,top = 184');
}
function onNew(){

	document.form.CUST_CODE.value = ""; 
	document.form.CUST_NAME.value = "";
	document.form.companyregnumber.value = ""; 
	document.form.CURRENCY.value = ""; 
	document.form.SUPPLIER_TYPE_ID.value = "";
	document.form.SUPPLIER_TYPE_DESC.value = "";
	document.form.transport.value = "";
	document.form.TELNO.value = ""; 
	document.form.FAX.value = "";
	document.form.CUSTOMEREMAIL.value = "";
	document.form.ADDR1.value = "";
	document.form.ADDR2.value = "";
	document.form.ADDR3.value = "";
	document.form.ADDR4.value = "";
	document.form.ZIP.value = "";
	document.form.CONTACTNAME.value = "";
	document.form.DESGINATION.value = "";
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
	document.form.WEBSITE.value = "";
	document.form.RCBNO.value = "";
	document.form.PEPPOL_ID.value = "";
	document.form.PEPPOL.checked = false;
	
	
	
	/* document.form.TAXTREATMENT.selectedIndex=0;
   document.form.action  = "maint_vendor.jsp?action=Clear";
   document.form.submit(); */
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
function onUpdate(){
   var CUST_CODE   = document.form.CUST_CODE.value;
   var CUST_NAME   = document.form.CUST_NAME.value;
   var PEPPOL   = document.form.PEPPOL.value;
   var PEPPOL_ID   = document.form.PEPPOL_ID.value;
//   var companyregnumber   = document.form.companyregnumber.value;
   var CURRENCY = document.form.CURRENCY.value;
   var region = document.form.COUNTRY_REG.value;
   var TAXTREATMENT   = document.form.TAXTREATMENT.value;
   var RCBNO   = document.form.RCBNO.value;
   var rcbn = RCBNO.length;
   if(CUST_CODE == "" || CUST_CODE == null) {alert("Please Enter Supplier ID");document.form.CUST_CODE.focus(); return false; }
   
   if(CUST_NAME == "" || CUST_NAME == null) {
	   alert("Please Enter Supplier Name"); 
	   document.form.CUST_NAME.focus();
	   return false; 
	   }

  /*  if(region == "GCC"){
	   document.form.companyregnumber.value="";
	}else if(region == "ASIA PACIFIC"){
		if (companyregnumber == "" || companyregnumber == null) {
		alert("Please Enter Unique Entity Number (UEN)");
		document.form.companyregnumber.focus();
		return false; 
		}
	} */

 //thanzi
	if(document.getElementById("PEPPOL").checked == true)
		PEPPOL="1";
	else 
		PEPPOL="0";
	   if(PEPPOL_ID == "" &&  PEPPOL == "1") {
	 	  alert("Please Enter Peppol Id"); 
	 	return false; 
	}


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


function onDelete(){
   var CUST_CODE   = document.form.CUST_CODE.value;
   if(CUST_CODE == "" || CUST_CODE == null) {alert("Please Enter Supplier ID");  return false; }
var chkmsg=confirm("Are you sure you would like to delete? ");
    if(chkmsg){
   document.form.action  = "../jsp/maint_vendor.jsp?action=DELETE";
   document.form.submit();}
   else
   { return false;
   }
}
function onView(){
   var CUST_CODE   = document.form.CUST_CODE.value;
   if(CUST_CODE == "" || CUST_CODE == null) {alert("Please Enter Supplier ID"); return false; }

   document.form.action  = "../jsp/maint_vendor.jsp?action=VIEW";
   document.form.submit();
}


</script>
<jsp:useBean id="ub"  class="com.track.gates.userBean" />
<%
	String sUserId = (String) session.getAttribute("LOGIN_USER");
	String curency = StrUtils.fString((String) session.getAttribute("BASE_CURRENCY"));
	String res = "";

	String sNewEnb = "enabled";
	String sDeleteEnb = "enabled";
	String sAddEnb = "enabled";
	String sUpdateEnb = "enabled";
	String sCustEnb = "enabled";
	String action = "";
	String sCustCode = "", sCustName = "",sOldCustName  = "", companyregnumber="", sCustNameL = "", sAddr1 = "", sAddr2 = "", sAddr3 = "", sAddr4 = "", sState = "", sCountry = "", sZip = "", sCons = "Y";
	String sContactName = "", sDesgination = "", sTelNo = "", sHpNo = "",transport="", sFax = "", sEmail = "", sRemarks = "",sPayTerms="",sPayMentTerms="",sPayInDays="",sRcbno="", isActive = "";
	String desc = "",suppliertypeid="",supplier_type_id="",sTAXTREATMENT="",sCountryCode="";
	String CUSTOMEREMAIL="",WEBSITE="",FACEBOOK="",TWITTER="",LINKEDIN="",SKYPE="",CURRENCY="",OPENINGBALANCE="",WORKPHONE="",PEPPOL="",PEPPOL_ID="";
	String sIBAN="",sBANKNAME="",sBANKROUTINGCODE="",sBRANCH="",sSAVE_RED="",transportmode="",sSAVE_REDELETE;
	DateUtils dateutils = new DateUtils();
	StrUtils strUtils = new StrUtils();
	CustUtil custUtil = new CustUtil();
	PoHdrDAO PoHdrDAO = new  PoHdrDAO();
	PoDetDAO PoDetDAO = new  PoDetDAO();
	RecvDetDAO RecvDetDAO = new RecvDetDAO();
	PlantMstDAO _PlantMstDAO = new PlantMstDAO();
	CustomerBeanDAO customerBeanDAO = new CustomerBeanDAO();
	SupplierAttachDAO supplierAttachDAO = new SupplierAttachDAO();
	PlantMstDAO plantMstDAO = new PlantMstDAO();
	CurrencyDAO currencyDAO = new CurrencyDAO();
	custUtil.setmLogger(mLogger);
	
	
	List supplierattachlist= new ArrayList();
	action = strUtils.fString(request.getParameter("action"));
	String plant = strUtils.fString((String) session.getAttribute("PLANT"));
	String ISPEPPOL = plantMstDAO.getisPeppol(plant);
	String region = strUtils.fString((String) session.getAttribute("REGION"));
	String username = strUtils.fString((String) session.getAttribute("LOGIN_USER"));
	sCustCode = strUtils.fString(request.getParameter("CUST_CODE"));
	String taxbylabel= ub.getTaxByLable(plant);
	String numberOfDecimal = _PlantMstDAO.getNumberOfDecimal(plant);
	if (sCustCode.length() <= 0)
	sCustCode = strUtils.fString(request.getParameter("CUST_CODE1"));
	sCustName = strUtils.InsertQuotes(strUtils.fString(request.getParameter("CUST_NAME")));
	sOldCustName  = strUtils.InsertQuotes(strUtils.fString(request.getParameter("OLD_CUST_NAME")));
	sCustNameL = strUtils.InsertQuotes(strUtils.fString(request.getParameter("L_CUST_NAME")));
	companyregnumber= strUtils.fString(request.getParameter("companyregnumber"));
	sAddr1 = strUtils.fString(request.getParameter("ADDR1"));
	sAddr2 = strUtils.fString(request.getParameter("ADDR2"));
	sAddr3 = strUtils.fString(request.getParameter("ADDR3"));
	sAddr4 = strUtils.fString(request.getParameter("ADDR4"));

	sState = strUtils.fString(request.getParameter("STATE"));
	sCountry = strUtils.fString(request.getParameter("COUNTRY"));
	sZip = strUtils.fString(request.getParameter("ZIP"));
	sCons = strUtils.fString(request.getParameter("CONSIGNMENT"));
	sContactName = strUtils.InsertQuotes(strUtils.fString(request.getParameter("CONTACTNAME")));
	sDesgination = strUtils.fString(request.getParameter("DESGINATION"));
	sTelNo = strUtils.fString(request.getParameter("TELNO"));
	sHpNo = strUtils.fString(request.getParameter("HPNO"));
	sFax = strUtils.fString(request.getParameter("FAX"));
	sEmail = strUtils.fString(request.getParameter("EMAIL"));
	sRemarks = strUtils.fString(request.getParameter("REMARKS"));
	sRcbno = strUtils.fString(request.getParameter("RCBNO"));
	
	isActive = strUtils.fString(request.getParameter("ACTIVE"));
	suppliertypeid=strUtils.fString(request.getParameter("SUPPLIER_TYPE_ID"));
	transport=strUtils.fString(request.getParameter("TRANSPORTID"));
	
	sPayTerms=strUtils.fString(request.getParameter("PAYTERMS"));
	System.out.println("sPayTerms"+sPayTerms);
	sPayMentTerms=strUtils.fString(request.getParameter("payment_terms"));
	sPayInDays=strUtils.fString(request.getParameter("PMENT_DAYS"));
	
	CUSTOMEREMAIL=strUtils.fString(request.getParameter("CUSTOMEREMAIL"));
	WEBSITE=strUtils.fString(request.getParameter("WEBSITE"));
	FACEBOOK=strUtils.fString(request.getParameter("FACEBOOK"));
	TWITTER=strUtils.fString(request.getParameter("TWITTER"));
	LINKEDIN=strUtils.fString(request.getParameter("LINKEDIN"));
	SKYPE=strUtils.fString(request.getParameter("SKYPE"));
	OPENINGBALANCE=strUtils.fString(request.getParameter("OPENINGBALANCE"));
	WORKPHONE=strUtils.fString(request.getParameter("WORKPHONE"));
	sTAXTREATMENT=strUtils.fString(request.getParameter("TAXTREATMENT"));
	sCountryCode=strUtils.fString(request.getParameter("EDIT_COUNTRY"));
	sBANKNAME   = strUtils.InsertQuotes(strUtils.fString(request.getParameter("BANKNAME")));
	sBRANCH   = strUtils.InsertQuotes(strUtils.fString(request.getParameter("BRANCH")));
	sIBAN   = strUtils.InsertQuotes(strUtils.fString(request.getParameter("IBAN")));
	sBANKROUTINGCODE       = strUtils.fString(request.getParameter("BANKROUTINGCODE"));
	sSAVE_RED  = strUtils.fString(request.getParameter("SAVE_RED"));
	sSAVE_REDELETE = strUtils.fString(request.getParameter("SAVE_REDELETE"));
	CURRENCY = strUtils.fString(request.getParameter("CURRENCYID"));
	PEPPOL=StrUtils.fString((request.getParameter("PEPPOL") != null) ? "1": "0").trim();
	PEPPOL_ID = strUtils.fString(request.getParameter("PEPPOL_ID"));
	
	String currency=plantMstDAO.getBaseCurrency(plant);
	 String DISPLAY="",DISPLAYID="";
	 List curQryList=new ArrayList();
	 curQryList = currencyDAO.getCurrencyDetails(currency,plant);
	 for(int i =0; i<curQryList.size(); i++) {
			ArrayList arrCurrLine = (ArrayList)curQryList.get(i);
			DISPLAYID	= StrUtils.fString(StrUtils.removeQuotes((String)arrCurrLine.get(0)));
			DISPLAY	= StrUtils.fString(StrUtils.removeQuotes((String)arrCurrLine.get(2)));
	 }

	float OPENINGBALANCEVALUE ="".equals(OPENINGBALANCE) ? 0.0f :  Float.parseFloat(OPENINGBALANCE);
	//1. >> New
	if (action.equalsIgnoreCase("Clear")) {

		sCustCode = "";
		sCustName = "";
		sCustNameL = "";
		 companyregnumber="";
		 CURRENCY="";
		sAddr1 = "";
		sAddr2 = "";
		sAddr3 = "";
		sAddr4 = "";
		sState = "";
		sCountry = "";
		sZip = "";
		sCons = "Y";
		sContactName = "";
		sDesgination = "";
		sTelNo = "";
		sHpNo = "";
		sFax = "";
		sEmail = "";
		sRemarks = "";
		sPayTerms="";
		sPayMentTerms="";
		sPayInDays="";
		sAddEnb = "";
		sCustEnb = "";
		sRcbno="";
		suppliertypeid="";
		transport="";
		PEPPOL="";
		PEPPOL_ID="";
		CUSTOMEREMAIL="";WEBSITE="";FACEBOOK="";TWITTER="";LINKEDIN="";SKYPE="";OPENINGBALANCE="";WORKPHONE="";
		sIBAN="";sBANKNAME="";sBANKROUTINGCODE="";sBRANCH="";OPENINGBALANCEVALUE=0;sCountryCode="";
	}

	//3. >> Update
	else if (action.equalsIgnoreCase("UPDATE")) {
		if (custUtil.isExistVendor(sCustCode, plant)) {
			Hashtable ht = new Hashtable();
			ht.put(IDBConstants.PLANT, plant);
			if(!customerBeanDAO.isExistsSupplier(ht,
					" VENDNO <> '"+ sCustCode + "'  AND VNAME = '" + sCustName + "'"))
			{
				OPENINGBALANCEVALUE ="".equals(OPENINGBALANCE) ? 0.0f :  Float.parseFloat(OPENINGBALANCE);
				OPENINGBALANCE = StrUtils.addZeroes(OPENINGBALANCEVALUE, numberOfDecimal);
			Hashtable htUpdate = new Hashtable();
			htUpdate.put(IConstants.PLANT, plant);
			htUpdate.put(IConstants.VENDOR_CODE, sCustCode);
			htUpdate.put(IConstants.VENDOR_NAME, sCustName);
			htUpdate.put(IConstants.companyregnumber,companyregnumber);
			htUpdate.put("ISPEPPOL", PEPPOL);
			htUpdate.put("PEPPOL_ID", PEPPOL_ID);
			htUpdate.put("CURRENCY_ID", CURRENCY);
			htUpdate.put(IConstants.NAME, sContactName);
			htUpdate.put(IConstants.DESGINATION, sDesgination);
			htUpdate.put(IConstants.TELNO, sTelNo);
			htUpdate.put(IConstants.HPNO, sHpNo);
			htUpdate.put(IConstants.FAX, sFax);
			htUpdate.put(IConstants.EMAIL, sEmail);
			htUpdate.put(IConstants.ADDRESS1, sAddr1);
			htUpdate.put(IConstants.ADDRESS2, sAddr2);
			htUpdate.put(IConstants.ADDRESS3, sAddr3);
			htUpdate.put(IConstants.ADDRESS4, sAddr4);
			if(sState.equalsIgnoreCase("Select State"))
				sState="";
			htUpdate.put(IConstants.STATE, strUtils.InsertQuotes(sState));
			htUpdate.put(IConstants.COUNTRY, strUtils.InsertQuotes(sCountry));
			htUpdate.put(IConstants.ZIP, sZip);
			htUpdate.put(IConstants.USERFLG1, sCons);
			htUpdate.put(IConstants.REMARKS, strUtils.InsertQuotes(sRemarks));
			htUpdate.put(IConstants.PAYTERMS, strUtils.InsertQuotes(sPayTerms));
			htUpdate.put(IConstants.payment_terms, strUtils.InsertQuotes(sPayMentTerms));
			htUpdate.put(IConstants.PAYINDAYS, sPayInDays);
			htUpdate.put(IConstants.UPDATED_AT, new DateUtils().getDateTime());
			htUpdate.put(IConstants.UPDATED_BY, sUserId);
			htUpdate.put(IConstants.ISACTIVE, isActive);
			htUpdate.put(IConstants.SUPPLIERTYPEID,suppliertypeid);
			htUpdate.put(IConstants.TRANSPORTID,transport);
			htUpdate.put(IConstants.RCBNO, sRcbno);
			
			htUpdate.put(IConstants.CUSTOMEREMAIL,CUSTOMEREMAIL);
			htUpdate.put(IConstants.WEBSITE,WEBSITE);
			htUpdate.put(IConstants.FACEBOOK,FACEBOOK);
			htUpdate.put(IConstants.TWITTER,TWITTER);
			htUpdate.put(IConstants.LINKEDIN,LINKEDIN);
			htUpdate.put(IConstants.SKYPE,SKYPE);
			htUpdate.put(IConstants.OPENINGBALANCE,OPENINGBALANCE);
			htUpdate.put(IConstants.WORKPHONE,WORKPHONE);
			htUpdate.put(IConstants.TAXTREATMENT, sTAXTREATMENT);
			if(sBANKNAME.equalsIgnoreCase("Select Bank"))
	        	  sBANKNAME="";
	          htUpdate.put(IDBConstants.BANKNAME,sBANKNAME);
	          htUpdate.put(IDBConstants.IBAN,sIBAN);
	          htUpdate.put(IDBConstants.BANKROUTINGCODE,sBANKROUTINGCODE);
			Hashtable htCondition = new Hashtable();
			htCondition.put("VENDNO", sCustCode);
			htCondition.put(IConstants.PLANT, plant);
			
			Hashtable htpohdr = new Hashtable();
			htpohdr.put("CustCode", sCustCode);
			htpohdr.put(IConstants.PLANT, plant); 
			
			Hashtable htJournalDet = new Hashtable();
            htJournalDet.put("ACCOUNT_NAME", sCustCode+"-"+sOldCustName);
            htJournalDet.put(IConstants.PLANT, plant);
			
            String updateJournalDet ="set ACCOUNT_NAME='"+sCustCode+"-"+sCustName+"'"; 
			
			StringBuffer updatepohdr = new StringBuffer("set ");
			updatepohdr.append(IConstants.IN_CUST_CODE + " = '" + sCustCode + "'");
			updatepohdr.append("," +IConstants.IN_CUST_NAME + " = '" + sCustName + "'");
			updatepohdr.append("," + IConstants.IN_PERSON_IN_CHARGE + " = '"+ sContactName + "'");
			updatepohdr.append(",Address = '"+ sAddr1 + "'");
			updatepohdr.append(",Address2 = '"+ sAddr2 + "'");
			updatepohdr.append(",Address3 = '"+ sAddr3 + "'");
            
			Hashtable htpodet = new Hashtable();
			htpodet.put(IConstants.PLANT, plant); 
			
			String podetquery = "select Count(*) from "+plant+"_PODET where PONO in(select PONO from "+plant+"_POHDR where plant='"+plant+"' and  CustCode='"+sCustCode+"' )";
			String podetupdate = "set userfld3='"+sCustName+"' ";

			String recvdetquery = "select Count(*) from "+plant+"_RECVDET where PONO in(select PONO from "+plant+"_POHDR where  plant='"+plant+"' and CustCode='"+sCustCode+"' )";
			String recvdetupdate = "set cname='"+sCustName+"' ";

			String poestdetquery = "select Count(*) from "+plant+"_POESTDET where POESTNO in(select POESTNO from "+plant+"_POESTHDR where plant='"+plant+"' and  CustCode='"+sCustCode+"' )";
			String poestdetupdate = "set userfld3='"+sCustName+"' ";
			
			String pomultiestdetupdate = "set CustName='"+sCustName+"' ";
			
			String sysTime = DateUtils.Time();
			String sysDate = DateUtils.getDate();
			sysDate = DateUtils.getDateinyyyy_mm_dd(sysDate);

			MovHisDAO mdao = new MovHisDAO(plant);
			mdao.setmLogger(mLogger);
			Hashtable htm = new Hashtable();
			htm.put("PLANT", plant);
			htm.put("DIRTYPE", TransactionConstants.UPD_SUP);
			htm.put("RECID", "");
			htm.put("ITEM",sCustCode);
			htm.put(IDBConstants.UPDATED_BY, username);
			htm.put(IDBConstants.REMARKS, sCustName+","+strUtils.InsertQuotes(sRemarks));
			htm.put(IDBConstants.CREATED_BY, username);
			htm.put(IDBConstants.CREATED_AT, dateutils.getDateTime());
			htm.put(IDBConstants.UPDATED_AT, dateutils.getDateTime());
			htm.put(IDBConstants.TRAN_DATE, dateutils.getDateinyyyy_mm_dd(dateutils.getDate()));
			boolean custUpdated = custUtil.updateVendor(htUpdate,htCondition);
			boolean inserted = mdao.insertIntoMovHis(htm);
			boolean flag = false;
			if(custUpdated){
				if(!sCustName.equalsIgnoreCase(sOldCustName))
				 {
				 flag = PoHdrDAO.isExisit(htpohdr);
				 if(flag){
				 	flag = PoHdrDAO.updatePO(updatepohdr.toString(), htpohdr, " ");
				 }
				 if(flag){
					 flag = PoDetDAO.isExisit(podetquery);
					 if(flag)
					 {
						 flag = PoDetDAO.update(podetupdate, htpodet, " and PONO in(select PONO from "+plant+"_POHDR where plant='"+plant+"' and CustCode='"+sCustCode+"' )");
					 }
				 }
				 if(flag){
					 flag = RecvDetDAO.isExisit(recvdetquery);
					 if(flag)
					 {
						 flag = RecvDetDAO.update(recvdetupdate, htpodet, " and PONO in(select PONO from "+plant+"_POHDR where plant='"+plant+"' and CustCode='"+sCustCode+"' )", plant);
					 }
				 }
				 flag =new PoEstHdrDAO().isExisit(htpohdr);//POEST
				 if(flag){
				 	flag =new PoEstHdrDAO().updatePO(updatepohdr.toString(), htpohdr, " ");
				 }
				 if(flag){
					 flag =new PoEstDetDAO().isExisit(poestdetquery);
					 if(flag)
					 {
						 flag =new PoEstDetDAO().update(poestdetupdate, htpodet, " and POESTNO in(select POESTNO from "+plant+"_POESTHDR where plant='"+plant+"' and CustCode='"+sCustCode+"' )");
					 }
				 }
				 flag =new multiPoEstDetDAO().isExisit(htpohdr);//POMULTIESTDET
				 if(flag)
				 {
					 flag =new multiPoEstDetDAO().update(pomultiestdetupdate, htpodet, " ");
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
				 
			}
			if (custUpdated) {
				sSAVE_RED = "Update";
				/* res = "<font class = " + IConstants.SUCCESS_COLOR
						+ ">Supplier Updated Successfully</font>"; */
			} else {
				/* res = "<font class = " + IConstants.FAILED_COLOR
						+ ">Failed to Update Supplier</font>"; */
				sSAVE_RED = "Failed to Update Supplier";
			}
		} else {
				/* res = "<font class = " + IConstants.FAILED_COLOR
						+ ">Supplier Name Exists already. Try again with diffrent Supplier Name</font>"; */
			sSAVE_RED = "Supplier Name Exists already. Try again with diffrent Supplier Name";

			}
			
		} else {
			sSAVE_RED = "Supplier doesn't not Exists. Try again";
			/* res = "<font class = " + IConstants.FAILED_COLOR
					+ ">Supplier doesn't not Exists. Try again</font>"; */

		}
	
			
	}
else if(action.equalsIgnoreCase("DELETE")){
	
	MovHisDAO mdao=new MovHisDAO(plant);
	sCustEnb = "enabled";
    PoHdrDAO dao = new PoHdrDAO();
    boolean movementhistoryExist=false;
	Hashtable htmh = new Hashtable();
	htmh.put("CUSTCODE",strUtils.InsertQuotes(sCustCode));
	htmh.put(IConstants.PLANT,plant);
	movementhistoryExist = dao.isExisit(htmh,"");
	if(movementhistoryExist)
	{	
			res = "<font class = " + IDBConstants.FAILED_COLOR
					+ " >Supplier Exists In Transactions</font>";
		
	}
	else{
    if(custUtil.isExistVendor(strUtils.InsertQuotes(sCustCode),plant))
    {
          boolean custDeleted = custUtil.deleteVendor(strUtils.InsertQuotes(sCustCode),plant);
          
           mdao.setmLogger(mLogger);
			Hashtable htm = new Hashtable();
			htm.put("PLANT", plant);
			htm.put("DIRTYPE", TransactionConstants.DEL_SUP);
			htm.put("RECID", "");
			htm.put("ITEM",sCustCode);
			htm.put(IDBConstants.UPDATED_BY, username);
			htm.put(IDBConstants.REMARKS, sCustName+","+strUtils.InsertQuotes(sRemarks));
			htm.put(IDBConstants.CREATED_BY, username);
			htm.put(IDBConstants.CREATED_AT, dateutils.getDateTime());
			htm.put(IDBConstants.UPDATED_AT, dateutils.getDateTime());
			htm.put(IDBConstants.TRAN_DATE, dateutils.getDateinyyyy_mm_dd(dateutils.getDate()));
			
			custDeleted = mdao.insertIntoMovHis(htm);
			supplierAttachDAO.deletesupplierAttachByNo(plant, sCustCode);	
          if(custDeleted) {
                    /* res = "<font class = "+IConstants.SUCCESS_COLOR+">Supplier Deleted Successfully</font>"; */
                    sSAVE_REDELETE = "Delete";
                    sCustCode  = "";
                    sCustName  = "";
                    companyregnumber = "";
                    CURRENCY="";
                    sCustNameL = "";
                    sAddr1     = "";
                    sAddr2     = "";
                    sAddr3     = "";sAddr4="";
                    sState   = "";
                    sCountry   = "";
                    sZip       = "";
                    sContactName = "";
                    sDesgination = "";
                    sTelNo       = "";
                    sHpNo        = "";
                    sFax         = "";
                    sEmail       = "";
                    sRemarks     = "";
                    sPayTerms="";
                    sPayMentTerms="";
            		sPayInDays="";
                    sCons      = "Y";
                    sRcbno      = "";
					suppliertypeid="";
					transport="";
					PEPPOL="";
					PEPPOL_ID="";
					CUSTOMEREMAIL="";WEBSITE="";FACEBOOK="";TWITTER="";LINKEDIN="";SKYPE="";OPENINGBALANCE="";WORKPHONE="";
					sIBAN="";sBANKNAME="";sBANKROUTINGCODE="";sBRANCH="";OPENINGBALANCEVALUE=0;sCountryCode="";
          } else {
                    res = "<font class = "+IConstants.FAILED_COLOR+">Failed to Delete Supplier</font>";
                    sAddEnb = "enabled";
          }
    }else{
           res = "<font class = "+IConstants.FAILED_COLOR+">Supplier doesn't not Exists. Try again</font>";
    }
	}
}
	//4. >> View
	else if (action.equalsIgnoreCase("VIEW")) {
		try {
			ArrayList arrCust = custUtil.getVendorDetails(sCustCode,plant);
			sCustCode = (String) arrCust.get(0);
			sCustName = (String) arrCust.get(1);
			sAddr1 = (String) arrCust.get(2);
			sAddr2 = (String) arrCust.get(3);
			sAddr3 = (String) arrCust.get(4);
			sAddr4 = (String) arrCust.get(15);
			sCountry = (String) arrCust.get(5);
			sZip = (String) arrCust.get(6);
			sCons = (String) arrCust.get(7);
			sContactName = (String) arrCust.get(8);
			sDesgination = (String) arrCust.get(9);
			sTelNo = (String) arrCust.get(10);
			sHpNo = (String) arrCust.get(11);
			sEmail = (String) arrCust.get(12);
			sFax = (String) arrCust.get(13);
			sRemarks = (String) arrCust.get(14);
			isActive = (String) arrCust.get(16);
			sPayTerms = (String) arrCust.get(17);
			sPayMentTerms = (String) arrCust.get(39);
			sPayInDays = (String) arrCust.get(18);
			sState = (String) arrCust.get(19);
			sRcbno = (String) arrCust.get(20);
		//	suppliertypeid = (String) arrCust.get(21);
			CUSTOMEREMAIL = (String) arrCust.get(22);
			WEBSITE = (String) arrCust.get(23);
			FACEBOOK = (String) arrCust.get(24);
			TWITTER = (String) arrCust.get(25);
			LINKEDIN = (String) arrCust.get(26);
			SKYPE = (String) arrCust.get(27);
			OPENINGBALANCE = (String) arrCust.get(28);
			WORKPHONE = (String) arrCust.get(29);
			sTAXTREATMENT = (String) arrCust.get(30);
			sCountryCode = (String) arrCust.get(31);
			sBANKNAME = (String) arrCust.get(32);
			sBRANCH= (String) arrCust.get(33);
			sIBAN = (String) arrCust.get(34);
			sBANKROUTINGCODE = (String) arrCust.get(35);
			companyregnumber = (String) arrCust.get(36);
			PEPPOL = (String) arrCust.get(40);
			PEPPOL_ID = (String) arrCust.get(41);
			CURRENCY = (String) arrCust.get(37);
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
			transport = (String) arrCust.get(38);
			int trans = Integer.valueOf(transport);
			if(trans > 0){
				transportmode = transportmodedao.getTransportModeById(plant,trans);
			}
		else{
			transportmode = "";
		}
			
			CustomerBeanDAO Customerdao = new CustomerBeanDAO();
			suppliertypeid = (String) arrCust.get(21);
			if(suppliertypeid.length() > 0){
				supplier_type_id = customerBeanDAO.getSupplierTypeById(plant,suppliertypeid);
			}
		else{
			supplier_type_id = "";
		}
			
			
			
			
			
			
			
			supplierattachlist = supplierAttachDAO.getsupAttachByvenId(plant, sCustCode);
			OPENINGBALANCEVALUE ="".equals(OPENINGBALANCE) ? 0.0f :  Float.parseFloat(OPENINGBALANCE);
		} catch (Exception e) {
			res = "no details found for Vendor id : " + sCustCode;
		}

	}
	OPENINGBALANCE = StrUtils.addZeroes(OPENINGBALANCEVALUE, numberOfDecimal);
%>

<div class="container-fluid m-t-20">
	 <div class="box"> 
	 <!-- Thanzith Modified on 23.02.2022 -->
	 <ul class="breadcrumb backpageul">    
                <li class="underline-on-hover"><a href="../home">Dashboard</a></li>	
                <li><a href="../supplier/summary"><span class="underline-on-hover">Supplier Summary</span></a></li>                       
                <li><label>Edit Supplier</label></li>                                   
            </ul>
        <!-- Thanzith Modified on 23.02.2022 -->     
            <div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title"><%=title %></h1>
               <h1 style="font-size: 18px; cursor: pointer;"
				class="box-title pull-right"
			onclick="window.location.href='../supplier/summary'">
				<i class="glyphicon glyphicon-remove"></i>
			</h1>
		</div>
		
 <div class="box-body">

   <CENTER><strong><%=res%></strong></CENTER>

  <form class="form-horizontal" id="supplierform" autocomplete="off" name="form" method="post">
<%-- <input name="SUPPLIER_TYPE_DESC" type="hidden" value="<%=desc%>"> --%>
<input type="number" id="numberOfDecimal" style="display:none;" value=<%=numberOfDecimal%>>
    <div class="form-group">
      <label class="control-label col-form-label col-sm-2 required" for="Maintain Supplier ID">Supplier Id</label>
      <div class="col-sm-4">
      	<div class="input-group">    
    		<input name="CUST_CODE" type="TEXT" value="<%=sCustCode%>"
			size="50" MAXLENGTH=50 class="form-control" readonly>
   		 	<!-- <span class="input-group-addon"  onClick="javascript:popUpWin('../jsp/vendor_list.jsp?CUST_NAME='+form.CUST_NAME.value);return false;" >
   		 	<a href="#" data-toggle="tooltip" data-placement="top" title="Supplier Details" >
   		 	<i class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span> -->
  		</div>
  		<INPUT type="hidden" name="CUST_CODE1" value="">
  		<INPUT type="hidden" name="EDIT_TAXTREATMENT" value="<%=sTAXTREATMENT%>">
  		<INPUT type="hidden" name="EDIT_STATE" value="<%=sState%>">
  		<INPUT type="hidden" name="COUNTRY" value="<%=sCountry%>">
  		<INPUT type="hidden" name="EDIT_COUNTRY" value="<%=sCountryCode%>">
  		<INPUT type="hidden" name="COUNTRY_REG" value="<%=region%>">
  		<INPUT type="hidden" name="EDIT_BANKNAME" value="<%=sBANKNAME%>">
  		<INPUT type="hidden" name="SAVE_RED" value="<%=sSAVE_RED%>">
  		<INPUT type="hidden" name="SAVE_REDELETE" value="<%=sSAVE_REDELETE%>">
  		 <input type="hidden" name="CURRENCYID" value="<%=DISPLAYID%>">
  	    <input type = "hidden" name="currency" value="<%=currency%>">
  	    <input type="hidden" name="TRANSPORTID" value="<%=transport%>">
  	     <input type="hidden" name="SUPPLIER_TYPE_ID" value="<%=suppliertypeid%>">
  	     <input type = "hidden" name="OLD_CUST_NAME" value="<%=sCustName%>">
       	</div>
       
    <div class=form-inline>
    <div class="col-sm-2">
      	<button type="button" class="Submit btn btn-default" onClick="onView();">View</button>&nbsp;&nbsp;
    </div></div>
    
    </div>
    <div class="form-group">
      <label class="control-label col-form-label col-sm-2 required" for="Supplier Name">Supplier Name</label>
      <div class="col-sm-4">          
        <INPUT  class="form-control" name="CUST_NAME" id="CUST_NAME" type="TEXT" value="<%=sCustName%>"
			size="50" MAXLENGTH=100>
      </div>
    </div>
    
            <div class="form-group" id="UEN">
      <label class="control-label col-form-label col-sm-2">Unique Entity Number (UEN)</label>
      <div class="col-sm-4">
                
        <INPUT  class="form-control" name="companyregnumber" id="companyregnumber" type="TEXT" value="<%=companyregnumber%>"
			size="50" MAXLENGTH=50>
      </div>
    </div>
    
     <div class="form-group">
					<label class="control-label col-form-label col-sm-2 required" for="CURRENCY">Supplier Currency</label>
					<div class="col-sm-4">
						<input type="text" class="ac-selected form-control typeahead" id="CURRENCY" name="CURRENCY" placeholder="Select a Currency" value="<%=DISPLAY%>">
						<span class="select-icon" onclick="$(this).parent().find('input[name=\'CURRENCY\']').focus()"><i class="glyphicon glyphicon-menu-down"></i></span>
					</div>
				</div>
    
    <div class="form-group">
      <label class="control-label col-form-label col-sm-2" for="supplier Group">Supplier Group</label>
      <div class="col-sm-4">           	
  	      	<div class="input-group">    
    		<input name="SUPPLIER_TYPE_DESC" id="SUPPLIER_TYPE_DESC" type="TEXT" placeholder="Select Supplier Group" value="<%=supplier_type_id%>"
			size="50" MAXLENGTH=50 class="form-control">
   		 	<span class="select-icon"  onclick="$(this).parent().find('input[name=\'SUPPLIER_TYPE_ID\']').focus()"> 	
   		 	<!-- <a href="#" data-toggle="tooltip" data-placement="top" title="Supplier Type Details"> -->
   		 	<i class="glyphicon glyphicon-menu-down"></i></span>
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
	
	<INPUT type="hidden" id="TaxByLabel" name="taxbylabel" value=<%=taxbylabel%>>
       
       <div class="form-group">
      <label class="control-label col-form-label col-sm-2" for="Telephone No">Supplier Phone</label>
      <div class="col-sm-4">          
        <INPUT name="TELNO"  id="TELNO"  type="TEXT" value="<%=sTelNo%>" size="50" class="form-control" onkeypress="return validateInput(event)"
			MAXLENGTH="30">
      </div>
    </div>  	
    
    <div class="form-group">
      <label class="control-label col-form-label col-sm-2" for="Fax">Supplier Fax</label>
      <div class="col-sm-4">          
        <INPUT name="FAX" id="FAX" type="TEXT" value="<%=sFax%>" size="50" onkeypress="return validateInput(event)"
			MAXLENGTH="30" class="form-control">
      </div>
    </div>
    
    <div class="form-group">
      	<label class="control-label col-form-label col-sm-2" for="Customer Email">Supplier Email</label>
      	<div class="col-sm-4">  
      	<INPUT name="CUSTOMEREMAIL" id="CUSTOMEREMAIL"  type="TEXT" value="<%=CUSTOMEREMAIL%>" size="50" MAXLENGTH=50 class="form-control">
      	</div>
      	</div>
    
        <div class="form-group">
      	<label class="control-label col-form-label col-sm-2" for="Website">Website</label>
      	<div class="col-sm-4">  
    	<INPUT name="WEBSITE" id="WEBSITE"  type="TEXT" value="<%=WEBSITE%>" size="50" MAXLENGTH=50 class="form-control">
      	</div>
    	</div>
    	
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
      	<label class="control-label col-form-label col-sm-2">Peppol Id</label>
      	<div class="col-sm-4">  
    	<INPUT name="PEPPOL_ID" id="PEPPOL_ID"  type="TEXT" value="<%=PEPPOL_ID%>" size="50" MAXLENGTH=50 class="form-control">
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
            <a href="#profile" class="nav-link" data-toggle="tab">Contact Details</a>
        </li>
        <li class="nav-item">
            <a href="#home" class="nav-link" data-toggle="tab">Address</a>
        </li>
        <li class="nav-item">
            <a href="#bank_cus" class="nav-link" data-toggle="tab">Bank Account Details</a>
        </li>
        <li class="nav-item">
            <a href="#remark" class="nav-link" data-toggle="tab">Remarks</a>
        </li>
         <li class="nav-item">
            <a href="#attachfiles" class="nav-link" data-toggle="tab">Attachments</a>
        </li>
        </ul>
    <div class="tab-content clearfix">
        <div class="tab-pane fade" id="home">
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
       <%-- <INPUT name="COUNTRY" type="TEXT" value="<%=sCountry%>"
			size="50" MAXLENGTH=50 class="form-control"> --%>
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
        <%-- <INPUT name="STATE" type="TEXT" value="<%=sState%>" size="50"
			MAXLENGTH=50 class="form-control"> --%>
      </div>
    </div>    
    <div class="form-group">
      <label class="control-label col-form-label col-sm-2" for="Postal Code">Postal Code</label>
      <div class="col-sm-4">
                
        <INPUT name="ZIP" type="TEXT" value="<%=sZip%>" size="50"
			MAXLENGTH=10 class="form-control">
      </div>
    </div>
		     
        </div>
        
        <div class="tab-pane fade" id="profile">
        <br>
        
        <div class="form-group">
         <label class="control-label col-form-label col-sm-2" for="Contact Name">Contact Name</label>
      <div class="col-sm-4">
                 
       <INPUT name="CONTACTNAME" type="TEXT" class="form-control"
			value="<%=sContactName%>" size="50" MAXLENGTH="100" >
      </div>
    </div>
    
    <div class="form-group">
      <label class="control-label col-form-label col-sm-2" for="Designation">Designation</label>
      <div class="col-sm-4">
                
        <INPUT name="DESGINATION" type="TEXT" class="form-control"
			value="<%=sDesgination%>" size="50" MAXLENGTH="30">
      </div>
    </div>
    
    	<div class="form-group">
      	<label class="control-label col-form-label col-sm-2" for="Work phone">Work Phone</label>
      	<div class="col-sm-4">  
        <INPUT name="WORKPHONE" type="TEXT" value="<%=WORKPHONE%>" onkeypress="return validateInput(event)"	size="50" MAXLENGTH=50 class="form-control">
      	</div>
    	</div>
    
    <div class="form-group">
      <label class="control-label col-form-label col-sm-2" for="Hand Phone">Mobile</label>
      <div class="col-sm-4">
              
        <INPUT id="HPNO" name="HPNO" type="TEXT" value="<%=sHpNo%>" size="50" class="form-control" onkeypress="return validateInput(event)"
			MAXLENGTH="30">
      </div>
    </div>  
    
    <div class="form-group">
      <label class="control-label col-form-label col-sm-2" for="Email">Email</label>
      <div class="col-sm-4"> 
              
        <INPUT name="EMAIL" type="TEXT" value="<%=sEmail%>" size="50"
			MAXLENGTH="50" class="form-control">
      </div>
    </div>
     
		     
        </div>
        
        <div class="tab-pane active" id="other">
        <br>
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
		
		<div class="form-group">
      <label class="control-label col-form-label col-sm-2" for="TRN/RCB NO" id="TaxLabel"></label>
      <div class="col-sm-4">          
        <INPUT  class="form-control" name="RCBNO" type="TEXT" value="<%=sRcbno%>"
			size="50" MAXLENGTH=100>
      </div>
    </div>
		
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
	      <%--  <div class="input-group">
		    	<INPUT class="form-control" name="PAYTERMS" type="TEXT" value="<%=sPayTerms%>" size="20" MAXLENGTH=100 readonly>
		   		<span class="input-group-addon"  onClick="javascript:popUpWin('../jsp/list/paymenttypelist_save.jsp?paymenttype='+form.PAYTERMS.value);return false;">
		   		<a href="#" data-toggle="tooltip" data-placement="top" title="Payment Type">
		   		<i class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span>
	  		</div> --%>
  		</div>
  </div> 
  <div class="form-group">
       				<label class="control-label col-form-label col-sm-2">Payment Terms</label>
       			<div class="col-sm-4">
       				<input type="text" class="form-control" id="payment_terms"	name="payment_terms" value="<%=sPayMentTerms%>" placeholder="Select Payment Terms">
					<span class="select-icon" onclick="$(this).parent().find('input[name=\'payment_terms\']').focus()">
					<i class="glyphicon glyphicon-menu-down"></i></span>
				</div>
					
			<div class="form-inline">
    		<label class="control-label col-form-label col-sm-1">Days</label>
    	    <input name="PMENT_DAYS" type="TEXT" value="<%=sPayInDays%>" class="form-control" size="5" MAXLENGTH=10 readonly>
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
      	<label class="control-label col-form-label col-sm-2" for="Skype">Skype Id</label>
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
			<%-- 	<SELECT class="form-control" data-toggle="dropdown" data-placement="right" onchange="OnBank(this.value)" id="BANKNAME" name="BANKNAME" value="<%=sBANKNAME%>" style="width: 100%">
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
			 
			 <INPUT name="BANKNAME" type = "TEXT" value="<%=sBANKNAME%>" id="BANKNAME" class="form-control" placeholder="BANKNAME">
	         <span class="select-icon"  onclick="$(this).parent().find('input[name=\'BANKNAME\']').focus()">
	        <i class="glyphicon glyphicon-menu-down"></i></span>
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
		             <div class="tab-pane fade" id="attachfiles">
        <br>
				<div class="form-inline">
					<label for="email">Attach Files(s)</label>
					<div class="attch-section">
						<input type="file" class="form-control input-attch" id="supplierAttch" name="file" multiple="true">
						<div class="input-group">
							<svg version="1.1" id="Layer_1" xmlns="http://www.w3.org/2000/svg" x="0" y="0" viewBox="0 0 512 512" xml:space="preserve" class="icon icon-xs align-text-top action-icons input-group-addon" style="height:30px;display:inline-block;color: #c63616;"><path d="M262.4 512c-35.7 0-64.2-10.5-84.9-31.4-24.7-24.9-37.1-63.8-36.8-115.6.2-32.6-.3-248.7-1.4-268.1-.9-15.9 4.8-41.6 23.6-61.5 11.4-12 32-26.3 66-26.3 30.3 0 55.1 15.7 69.8 44.2 10.1 19.7 12.2 38.8 12.4 40.9l.1.9v.9l.5 236.9v.5c-1 19.2-15.9 47.6-53 47.6h-.7c-39.1-.4-53.7-30.4-56-46.2l-.2-1.3V179.6c0-10.2 8.3-18.5 18.5-18.5s18.5 8.3 18.5 18.5v150.5c.4 1.5 1.4 4.4 3.6 7.2s6.2 6.5 16 6.6c9.2.1 12.4-3.2 14.1-6 1.6-2.6 2.2-5.6 2.3-6.3l-.7-234.5c-.4-3-2.4-15.6-8.8-27.6-8.3-15.7-20.2-23.3-36.4-23.3-16.7 0-29.8 5-39.1 14.8-10.7 11.3-14 26.6-13.6 34 1.2 21.6 1.6 244.3 1.4 270.3-.2 41.6 8.5 71.7 26 89.3 13.5 13.6 33.2 20.4 58.7 20.4 17.2 0 31.8-5.9 43.5-17.7 18.9-18.9 30.1-53.4 30-92.2 0-19.6-.1-193.2-.1-250.9 0-10.2 8.3-18.5 18.5-18.5s18.5 8.3 18.5 18.5c0 57.6.1 231.2.1 250.8.1 49.1-14.8 92.3-40.8 118.4-18.6 18.7-42.7 28.6-69.6 28.6z"></path></svg>
							<button type="button" class="btn btn-sm btn-attch">Upload File</button>
						</div>
						
					</div>
				</div>
				<%if(supplierattachlist.size()>0){ %>
						<div id="supplierAttchNote">
							<small class="text-muted"><div class="attachclass"><a><%=supplierattachlist.size()%> files Attached</a>
									<div class="tooltiptext" style="width: 30%">
										
										<%for(int i =0; i<supplierattachlist.size(); i++) {   
									  		Map attach=(Map)supplierattachlist.get(i); %>
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
						<div id="supplierAttchNote">
							<small class="text-muted">  You can upload a maximum of 5 files, 2MB each  </small>
						</div>
						<%} %>
        </div>
        </div>
        </div>
        
    
       
    
    <div class="form-group">        
     <div class="col-sm-offset-4 col-sm-8">
    <!--  <button type="button" class="Submit btn btn-default" onClick="window.location.href='../home'">Back</button>&nbsp;&nbsp; -->
      <!-- <button type="button" class="Submit btn btn-default" onClick="{backNavigation('settings.jsp','SA');}"><b>Back</b></button>&nbsp;&nbsp; -->
      	<button type="button" class="Submit btn btn-default" onClick="onNew();"<%=sNewEnb%>>Clear</button>&nbsp;&nbsp;
      	<button class="Submit btn btn-default" type="BUTTON"  onClick="return onDelete();" <%=sDeleteEnb%>>Delete</button>&nbsp;&nbsp;
      	<button type="button" class="btn btn-success" data-toggle="modal" data-target="#myModal" onClick="onUpdate();" <%=sUpdateEnb%>>Save</button>&nbsp;&nbsp;
      	
      </div>
    </div>
  </form>
</div>
</div>
</div>

    <!-- modal page-->
<%@include file="../jsp/newBankModal.jsp"%>
<%@include file="../jsp/newTransportModeModal.jsp"%> <!-- Resvi for add transport -->
<%@include file="../jsp/newCurrencyModal.jsp"%>
<%@include file="../jsp/NewSupplierTypeModal.jsp"%> <!-- thansith Add For SupplierType -->
<%@include file="../jsp/newPaymentTermsModal.jsp"%> <!-- thansith Add For paymentterms -->
<%@include file="newPaymentModeModal.jsp"%><!-- thansith Add for paymentmode -->
<input type="hidden" name="Basecurrency" id="Basecurrency" style="display: none;" value=<%=curency%>> 
<script>
$(document).ready(function(){

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

	/* Supplier Type Auto Suggestion */
	$('#SUPPLIER_TYPE_DESC').typeahead({
		  hint: true,
		  minLength:0,  
		  searchOnFocus: true
		},
		{
		  display: 'SUPPLIER_TYPE_DESC',  
		  source: function (query, process,asyncProcess) {
			var urlStr = "/track/MasterServlet";
			$.ajax( {
			type : "POST",
			url : urlStr,
			async : true,
			data : {
				PLANT :  "<%=plant%>",
				ACTION : "GET_SUPPLIER_TYPE_DATA",
				QUERY : query
			},
			dataType : "json",
			success : function(data) {
				return asyncProcess(data.SUPPLIER_TYPE_MST);
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
				return '<div onclick="document.form.SUPPLIER_TYPE_ID.value = \''+data.SUPPLIER_TYPE_ID+'\'"><p class="item-suggestion">' + data.SUPPLIER_TYPE_ID + '</p><br/><p class="item-suggestion">DESC: '+data.SUPPLIER_TYPE_DESC+'</p></div>';
			}
		  }
		}).on('typeahead:render',function(event,selection){
			var menuElement = $(this).parent().find(".tt-menu");
			var top = menuElement.height()+35;
			top+="px";	
			if(menuElement.next().hasClass("footer")){
				menuElement.next().remove();  
			}
			menuElement.after( '<div class="SUPPLIER_TYPE_IDAddBtn footer"  data-toggle="modal" data-target="#suppliertypeModal"> <a href="#"> + Add Supplier Group</a></div>');
			menuElement.next().width(menuElement.width());
			menuElement.next().css({ "top": top,"padding":"3px 20px" });
			if($(this).parent().find(".tt-menu").css('display') != "block")
				menuElement.next().hide();
		  
		}).on('typeahead:open',function(event,selection){
			$('.SUPPLIER_TYPE_IDAddBtn').show();
			var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
			element.toggleClass("glyphicon-menu-up",true);
			element.toggleClass("glyphicon-menu-down",false);
			var menuElement = $(this).parent().find(".tt-menu");
			menuElement.next().show();
		}).on('typeahead:close',function(){
			setTimeout(function(){ $('.SUPPLIER_TYPE_IDAddBtn').hide();}, 180);
			var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
			element.toggleClass("glyphicon-menu-up",false);
			element.toggleClass("glyphicon-menu-down",true);
			
		}).on('typeahead:change',function(event,selection){
			if($(this).val() == "")
				document.form.SUPPLIER_TYPE_ID.value = "";
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

	
    $('[data-toggle="tooltip"]').tooltip();
    if(document.form.SAVE_RED.value!="")
	{    
    	document.form.action  = "../supplier/summary?PGaction=View&result=Supplier Updated Successfully";
    	document.form.submit(); 
	}
});
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
    	document.form.action  = "../supplier/summary?PGaction=View&result=Supplier Deleted Successfully";
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
	if(document.form.EDIT_TAXTREATMENT.value=="VAT Registered"||document.form.EDIT_TAXTREATMENT.value=="VAT Registered - Desginated Zone")
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
	
	if(TAXTREATMENT=="VAT Registered" ||TAXTREATMENT=="VAT Registered - Desginated Zone")
	{
		$("#TaxLabel").addClass("required");
	}
	else
		$("#TaxLabel").removeClass("required");
	}


function setCurrencyid(CURRENCYID,CURRENCYUSEQT){	
	$('input[name ="CURRENCYID"]').val(CURRENCYID);
	
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
function payTermsCallback(payTermsData){
	if(payTermsData.STATUS="SUCCESS"){
		$("input[name ='payment_terms']").typeahead('val', payTermsData.PAYMENT_TERMS);
		$("input[name=PMENT_DAYS]").val(payTermsData.NO_DAYS);
	}
}

function paymentTypeCallback(payTermsData)
{
	if(payTermsData.STATUS="100"){
		$("input[name ='PAYTERMS']").typeahead('val', payTermsData.PAYMENTMODE);
	}
}

function suppliertypeCallback(data){
	if(data.STATUS="SUCCESS"){
		$("input[name=SUPPLIER_TYPE_DESC]").typeahead ('val',data.SUPPLIER_TYPE_DESC);
		$("input[name ='SUPPLIER_TYPE_ID']").val(data.SUPPLIER_TYPE_ID);
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
$("#supplierAttch").change(function(){
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
				$("#supplierAttchNote").html("<small class='text-muted'>  You can upload a maximum of 5 files, 2MB each  </small>");
			}else{
				$("#supplierAttchNote").html(files +" files attached");
				/* $("#supplierAttchNote").append('<br><br><button onclick="add_attachments()">Upload Supplier Attachments</button>'); */
			}
			
		}
	});
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
	 var urlStrAttach = "/track/SupplierAttachmentServlet?Submit=downloadAttachmentById&attachid="+id;
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
	var urlStrAttach = "/track/SupplierAttachmentServlet?Submit=removeAttachmentById&removeid="+id;	
	$.ajax( {
		type : "POST",
		url : urlStrAttach,
		success : function(data) {
					window.location.reload();
				}
			});
}

function add_attachments(){
    var formData = new FormData($('#supplierform')[0]);
    var userId= form.CUST_CODE.value;
	if(userId){
    $.ajax({
        type: 'post',
        url: "/track/SupplierAttachmentServlet?Submit=add_attachments",
	    data:  formData,//{key:val}
	    contentType: false,
	    processData: false,
        success: function (data) {
        	console.log(data);
        //	window.location.reload();
        	document.form.action  = "../jsp/maint_vendor.jsp?action=UPDATE";
        	document.form.submit();
        },
        error: function (data) {
            alert(data.responseText);
        }
    });
	}else{
		alert("Please enter Supplier Id");
	}
        return false; 
  }
function currencyCallback(Data)
{
	if(Data.STATUS="100"){
		$("input[name ='CURRENCY']").typeahead('val',Data.CURRENCYID);
		$('input[name ="CURRENCYID"]').val(Data.CURRENCYID);
		
	}
}
</script>

<jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
</jsp:include>