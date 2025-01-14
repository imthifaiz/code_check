<%@ page import="java.util.*"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.util.*"%>
<%@ page import="com.track.gates.*"%>
<%@ page import="com.track.constants.*"%>
<%@ page import="com.track.dao.CustMstDAO"%>
<%@ page import="com.track.dao.VendMstDAO"%>
<%@ page import="com.track.dao.MovHisDAO"%>
<%@ page import="com.track.dao.TblControlDAO"%>
<%@ page import="com.track.dao.PlantMstDAO"%>

<html>
<head>
<title>Supplier List</title>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.1/jquery.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
<link rel="stylesheet" href="css/style.css">

<link href="https://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/css/bootstrap.min.css" rel="stylesheet">   
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.7.1/jquery.min.js"></script>
<link rel="stylesheet" href="https://cdn.datatables.net/1.10.2/css/jquery.dataTables.min.css">
<script type="text/javascript" src="https://cdn.datatables.net/1.10.2/js/jquery.dataTables.min.js"></script>
<script type="text/javascript" src="https://maxcdn.bootstrapcdn.com/bootstrap/3.2.0/js/bootstrap.min.js"></script>
</head>

<body>
<script src="js/typeahead.jquery.js"></script>
<link rel="stylesheet" href="css/typeahead.css">
<link rel="stylesheet" href="css/accounting.css">
<script language="JavaScript" type="text/javascript" src="js/general.js"></script>

<SCRIPT LANGUAGE="JavaScript">
var subWin = null;
function popUpWin(URL) {
 subWin = window.open(encodeURI(URL), '_blank', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=800,height=400,left = 200,top = 184');
}
function onNew(){
	document.form.TAXTREATMENT.selectedIndex=0;
   document.form.action  = "vendor_hdrlist.jsp?action=Clear";
   document.form.submit();
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
   var CUST_NAME   = document.form.CUST_NAME1.value; /*  */
   var TAXTREATMENT   = document.form.TAXTREATMENT.value;
   var RCBNO   = document.form.RCBNO.value;
   var rcbn = RCBNO.length;
   if(CUST_CODE == "" || CUST_CODE == null) {alert("Please Enter Supplier ID");document.form.CUST_CODE.focus(); return false; }
   
   if(CUST_NAME == "" || CUST_NAME == null) {
   alert("Please Enter Supplier Name"); 
   document.form.CUST_NAME1.focus();  /*  */
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
   document.form.action  = "vendor_hdrlist.jsp?action=ADD";
   document.form.submit();
}

function onIDGen()
{
 document.form.action  = "vendor_hdrlist.jsp?action=Auto-ID";
   document.form.submit(); 

}

</SCRIPT>
<jsp:useBean id="ub" class="com.track.gates.userBean" />
<%
	String sUserId = (String) session.getAttribute("LOGIN_USER");
	String res = "";
    TblControlDAO _TblControlDAO = new TblControlDAO();
   
	String sNewEnb = "enabled";
	String sDeleteEnb = "enabled";
	String sAddEnb = "enabled";
	String sUpdateEnb = "enabled";
	String sCustEnb = "enabled";
	String action = "";
	String sCustCode = "", sCustName = "", sCustNameL = "", sAddr1 = "", sAddr2 = "", sAddr3 = "", sAddr4 = "", sCountry = "", sZip = "", sCons = "Y";
	String sContactName = "", sDesgination = "", sTelNo = "", sHpNo = "", sFax = "", sEmail = "",sRcbno="",paymenttype="",suppliertypeid="",desc="", 
			sRemarks = "",sPayTerms="",sPayInDays="",sState="",sTAXTREATMENT="";
	String CUSTOMEREMAIL="",WEBSITE="",FACEBOOK="",TWITTER="",LINKEDIN="",SKYPE="",OPENINGBALANCE="",WORKPHONE="";
	String sIBAN="",sBANKNAME="",sBANKROUTINGCODE="",sBRANCH="";
	DateUtils dateutils = new DateUtils();
	/*  */
	MLogger mLogger = new MLogger();
	/*  */
	StrUtils strUtils = new StrUtils();
	CustUtil custUtil = new CustUtil();
	PlantMstDAO _PlantMstDAO = new PlantMstDAO();
	custUtil.setmLogger(mLogger);
	action = strUtils.fString(request.getParameter("action"));
	String plant = strUtils.fString((String) session.getAttribute("PLANT"));
	String region = strUtils.fString((String) session.getAttribute("REGION"));
	String username = strUtils.fString((String) session.getAttribute("LOGIN_USER"));
	sCustCode = strUtils.fString(request.getParameter("CUST_CODE"));
	String taxbylabel= ub.getTaxByLable(plant);
	String numberOfDecimal = _PlantMstDAO.getNumberOfDecimal(plant);
	if (sCustCode.length() <= 0)
	sCustCode = strUtils.fString(request.getParameter("CUST_CODE1"));
	sCustName = strUtils.InsertQuotes(strUtils.fString(request.getParameter("CUST_NAME1")));  /*  */
	sCustNameL = strUtils.InsertQuotes(strUtils.fString(request.getParameter("L_CUST_NAME")));
	sAddr1 = strUtils.fString(request.getParameter("ADDR1"));
	sAddr2 = strUtils.fString(request.getParameter("ADDR2"));
	sAddr3 = strUtils.fString(request.getParameter("ADDR3"));
	sAddr4 = strUtils.fString(request.getParameter("ADDR4"));

	sState = strUtils.InsertQuotes(strUtils.fString(request.getParameter("STATE")));
	sCountry = strUtils.InsertQuotes(strUtils.fString(request.getParameter("COUNTRY")));
	sZip = strUtils.fString(request.getParameter("ZIP"));
	sCons = strUtils.fString(request.getParameter("CONSIGNMENT"));
	sContactName = strUtils.InsertQuotes(strUtils.fString(request.getParameter("CONTACTNAME")));
	sDesgination = strUtils.fString(request.getParameter("DESGINATION"));
	sTelNo = strUtils.fString(request.getParameter("TELNO"));
	sHpNo = strUtils.fString(request.getParameter("HPNO"));
	sFax = strUtils.fString(request.getParameter("FAX"));
	sEmail = strUtils.fString(request.getParameter("EMAIL"));
	sRcbno = strUtils.fString(request.getParameter("RCBNO"));
	sRemarks = strUtils.InsertQuotes(strUtils.fString(request.getParameter("REMARKS")));
	sPayTerms=strUtils.InsertQuotes(strUtils.fString(request.getParameter("PAYTERMS")));
	sPayInDays=strUtils.InsertQuotes(strUtils.fString(request.getParameter("PMENT_DAYS")));
	suppliertypeid=strUtils.fString(request.getParameter("SUPPLIER_TYPE_ID"));
	
	CUSTOMEREMAIL=strUtils.fString(request.getParameter("CUSTOMEREMAIL"));
	WEBSITE=strUtils.fString(request.getParameter("WEBSITE"));
	FACEBOOK=strUtils.fString(request.getParameter("FACEBOOK"));
	TWITTER=strUtils.fString(request.getParameter("TWITTER"));
	LINKEDIN=strUtils.fString(request.getParameter("LINKEDIN"));
	SKYPE=strUtils.fString(request.getParameter("SKYPE"));
	OPENINGBALANCE=strUtils.fString(request.getParameter("OPENINGBALANCE"));
	WORKPHONE=strUtils.fString(request.getParameter("WORKPHONE"));
	sTAXTREATMENT=strUtils.fString(request.getParameter("TAXTREATMENT"));
	sBANKNAME   = strUtils.InsertQuotes(strUtils.fString(request.getParameter("BANKNAME")));
	sIBAN   = strUtils.InsertQuotes(strUtils.fString(request.getParameter("IBAN")));
	sBANKROUTINGCODE       = strUtils.fString(request.getParameter("BANKROUTINGCODE"));
	float OPENINGBALANCEVALUE ="".equals(OPENINGBALANCE) ? 0.0f :  Float.parseFloat(OPENINGBALANCE);
	MovHisDAO mdao = new MovHisDAO(plant);
	
	//User Check 	
	boolean al=false;
	al = ub.isCheckVal("popsupplier", plant,username);
	if(al==true)
	{		
		System.out.println("popsupplier");
	}
	
	//1. >> New
	if (action.equalsIgnoreCase("Clear")) {

		sCustCode = "";
		sCustName = "";
		sCustNameL = "";
		sAddr1 = "";
		sAddr2 = "";
		sAddr3 = "";
		sAddr4 = "";
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
		sPayInDays="";
		sAddEnb = "";
		sCustEnb = "";
		sState="";
		sRcbno="";
		suppliertypeid="";
		CUSTOMEREMAIL="";WEBSITE="";FACEBOOK="";TWITTER="";LINKEDIN="";SKYPE="";OPENINGBALANCE="";WORKPHONE="";
		sIBAN="";sBANKNAME="";sBANKROUTINGCODE="";sBRANCH="";OPENINGBALANCEVALUE=0;
		
	} else if (action.equalsIgnoreCase("Auto-ID")) {
		String minseq = "";
		String sBatchSeq = "";
		boolean insertFlag = false;
		String sZero = "";
		//TblControlDAO _TblControlDAO = new TblControlDAO();
		_TblControlDAO.setmLogger(mLogger);
		Hashtable ht = new Hashtable();

		String query = " isnull(NXTSEQ,'') as NXTSEQ";
		ht.put(IDBConstants.PLANT, plant);
		ht.put(IDBConstants.TBL_FUNCTION, "SUPPLIER");
		try {
			boolean exitFlag = false;
			boolean resultflag = false;
			exitFlag = _TblControlDAO.isExisit(ht, "", plant);

			//--if exitflag is false than we insert batch number on first time based on plant,currentmonth
			if (exitFlag == false) {

				Map htInsert = null;
				Hashtable htTblCntInsert = new Hashtable();
				htTblCntInsert.put(IDBConstants.PLANT, plant);
				htTblCntInsert.put(IDBConstants.TBL_FUNCTION,"SUPPLIER");
				htTblCntInsert.put(IDBConstants.TBL_PREFIX1, "S");
				htTblCntInsert.put("MINSEQ", "0000");
				htTblCntInsert.put("MAXSEQ", "9999");
				htTblCntInsert.put(IDBConstants.TBL_NEXT_SEQ,(String) IDBConstants.TBL_FIRST_NEX_SEQ);
				htTblCntInsert.put(IDBConstants.CREATED_BY, username);
				htTblCntInsert.put(IDBConstants.CREATED_AT,(String) new DateUtils().getDateTime());
				insertFlag = _TblControlDAO.insertTblControl(htTblCntInsert, plant);

				sCustCode = "S" + "0001";
			} else {
				//--if exitflag is not false than we updated nxtseq batch number based on plant,currentmonth

				Map m = _TblControlDAO.selectRow(query, ht, "");
				sBatchSeq = (String) m.get("NXTSEQ");
				System.out.println("length" + sBatchSeq.length());

				int inxtSeq = Integer.parseInt(((String) sBatchSeq.trim().toString())) + 1;

				String updatedSeq = Integer.toString(inxtSeq);
				if (updatedSeq.length() == 1) {
					sZero = "000";
				} else if (updatedSeq.length() == 2) {
					sZero = "00";
				} else if (updatedSeq.length() == 3) {
					sZero = "0";
				}

				Map htUpdate = null;

				Hashtable htTblCntUpdate = new Hashtable();
				htTblCntUpdate.put(IDBConstants.PLANT, plant);
				htTblCntUpdate.put(IDBConstants.TBL_FUNCTION,"SUPPLIER");
				htTblCntUpdate.put(IDBConstants.TBL_PREFIX1, "S");
				StringBuffer updateQyery = new StringBuffer("set ");
				updateQyery.append(IDBConstants.TBL_NEXT_SEQ + " = '"+ (String) updatedSeq.toString() + "'");

				//boolean updateFlag = _TblControlDAO.update(updateQyery.toString(), htTblCntUpdate, "", plant);
				sCustCode = "S" + sZero + updatedSeq;
			}
		} catch (Exception e) {
			mLogger.exception(true,
					"ERROR IN JSP PAGE - vendor_hdrlist.jsp ", e);
		}
	}
	//2. >> Add
	else if (action.equalsIgnoreCase("ADD")) {
		if (!custUtil.isExistVendor(sCustCode, plant) && !custUtil.isExistVendorName(sCustName, plant)) // if the Customer exists already
		{
			Hashtable ht = new Hashtable();
			ht.put(IConstants.PLANT, plant);
			ht.put(IConstants.VENDOR_CODE, sCustCode);
			ht.put(IConstants.VENDOR_NAME, sCustName);
			ht.put(IConstants.NAME, sContactName);
			ht.put(IConstants.DESGINATION, sDesgination);
			ht.put(IConstants.TELNO, sTelNo);
			ht.put(IConstants.HPNO, sHpNo);
			ht.put(IConstants.FAX, sFax);
			ht.put(IConstants.EMAIL, sEmail);
			ht.put(IConstants.ADDRESS1, sAddr1);
			ht.put(IConstants.ADDRESS2, sAddr2);
			ht.put(IConstants.ADDRESS3, sAddr3);
			ht.put(IConstants.ADDRESS4, sAddr4);
			ht.put(IConstants.COUNTRY, sCountry);
			ht.put(IConstants.ZIP, sZip);
			ht.put(IConstants.USERFLG1, sCons);
			ht.put(IConstants.REMARKS, strUtils.InsertQuotes(sRemarks));
			ht.put(IConstants.PAYTERMS, strUtils.InsertQuotes(sPayTerms));
			ht.put(IConstants.PAYINDAYS, sPayInDays);
			ht.put(IConstants.CREATED_AT, new DateUtils().getDateTime());
			ht.put(IConstants.CREATED_BY, sUserId);
			 ht.put(IConstants.SUPPLIERTYPEID,suppliertypeid);
			ht.put(IConstants.ISACTIVE, "Y");
			
			ht.put(IConstants.CUSTOMEREMAIL,CUSTOMEREMAIL);
	        ht.put(IConstants.WEBSITE,WEBSITE);
	        ht.put(IConstants.FACEBOOK,FACEBOOK);
	        ht.put(IConstants.TWITTER,TWITTER);
	        ht.put(IConstants.LINKEDIN,LINKEDIN);
	        ht.put(IConstants.SKYPE,SKYPE);
	        ht.put(IConstants.OPENINGBALANCE,OPENINGBALANCE);
	        ht.put(IConstants.WORKPHONE,WORKPHONE);
			
			ht.put("Comment1", " 0 ");
			if(sState.equalsIgnoreCase("Select State"))
				sState="";
			ht.put(IConstants.STATE, sState);
			ht.put(IConstants.RCBNO, sRcbno);
			ht.put(IConstants.TAXTREATMENT, sTAXTREATMENT);
			if(sBANKNAME.equalsIgnoreCase("Select Bank"))
	        	  sBANKNAME="";
	          ht.put(IDBConstants.BANKNAME,sBANKNAME);
	          ht.put(IDBConstants.IBAN,sIBAN);
	          ht.put(IDBConstants.BANKROUTINGCODE,sBANKROUTINGCODE);
			String sysTime = DateUtils.Time();
			String sysDate = DateUtils.getDate();
			sysDate = DateUtils.getDateinyyyy_mm_dd(sysDate);

			mdao.setmLogger(mLogger);
			Hashtable htm = new Hashtable();
			htm.put("PLANT", plant);
			htm.put("DIRTYPE", TransactionConstants.ADD_SUP);
			htm.put("RECID", "");
			htm.put("ITEM",sCustCode);
			if(!sRemarks.equals(""))
			{
				htm.put(IDBConstants.REMARKS, sCustName+","+sRemarks);
			}
			else
			{
				htm.put(IDBConstants.REMARKS, sCustName);
			}
			
			htm.put(IDBConstants.CREATED_BY, username);
			htm.put(IDBConstants.CREATED_AT, dateutils.getDateTime());
			htm.put(IDBConstants.TRAN_DATE, dateutils.getDateinyyyy_mm_dd(dateutils.getDate()));
		
		boolean updateFlag;
			if(sCustCode!="S0001")
	  		  updateFlag=_TblControlDAO.updateSeqNo("SUPPLIER",plant);
		
			boolean custInserted = custUtil.insertVendor(ht);
			boolean inserted = mdao.insertIntoMovHis(htm);
			if (custInserted) {

				res = "<font class = " + IConstants.SUCCESS_COLOR+ ">Supplier Added Successfully</font>";
				//                    sAddEnb  = "disabled";
				sCustEnb = "disabled";
				
				//clear data automatically when click save button 
				sCustCode = "";
				sCustName = "";
				sCustNameL = "";
				sAddr1 = "";
				sAddr2 = "";
				sAddr3 = "";
				sAddr4 = "";
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
				sPayInDays="";
				sAddEnb = "";
				sCustEnb = "";
				sState="";
				sRcbno="";
				suppliertypeid="";
				CUSTOMEREMAIL="";WEBSITE="";FACEBOOK="";TWITTER="";LINKEDIN="";SKYPE="";OPENINGBALANCE="";WORKPHONE="";
				sIBAN="";sBANKNAME="";sBANKROUTINGCODE="";sBRANCH="";;OPENINGBALANCEVALUE=0;
                //automatic clear end
			} else {
				res = "<font class = " + IConstants.FAILED_COLOR+ ">Failed to add New Supplier</font>";
				//                    sAddEnb  = "enabled";
				sCustEnb = "enabled";
			}
		} else {
			res = "<font class = " + IConstants.FAILED_COLOR
					+ ">Supplier ID Or Name Exists already. Try again with diffrent Supplier ID Or Name.</font>";
			//           sAddEnb = "enabled";
			sCustEnb = "enabled";
		}
	}
	OPENINGBALANCE = StrUtils.addZeroes(OPENINGBALANCEVALUE, numberOfDecimal);
	%>
	
	
<div class="panel panel-default">
<div class="panel-heading" style="color: #ffffff; background-color: #102C54 " align="center">
<h3 class="panel-title">Supplier List</h3> 
</div>
</div>

<form class="form-horizontal" method="post" name="form">
 <input name="SUPPLIER_TYPE_DESC" type="hidden" value="<%=desc%>">
<div class="box-body">

   <CENTER><strong><%=res%></strong></CENTER>
<div id="target" style="display:none">
<div class="form-group">
      <label class="control-label col-sm-3 required" for="Create Supplier ID">Supplier ID:</label>
      <div class="col-sm-3">
      
      	<div class="input-group">
            <input type="hidden" value="hide" name="search_criteria_status" id="search_criteria_status" />   		
      	  	<INPUT class="form-control" name="CUST_CODE" id="CUST_CODE" type="TEXT" value="<%=sCustCode%>" onchange="checkitem(this.value)" size="50" MAXLENGTH=50 width="50"<%=sCustEnb%>>
   		 	<span class="input-group-addon"  onClick="onIDGen();">
   		 	<a href="#" data-toggle="tooltip" data-placement="top" title="Auto-Generate">
   		 	<i class="glyphicon glyphicon-edit" style="font-size: 20px;"></i></a></span>
  		</div>
  		<INPUT type="hidden" name="CUST_CODE1" value="<%=sCustCode%>">
      	<INPUT type="hidden" name="COUNTRY" value="<%=sCountry%>">
      	<INPUT type="hidden" name="COUNTRY_REG" value="<%=region%>">
      </div>
      <div class="form-inline">
      <label class="control-label col-sm-2 required" for="Supplier Name">Supplier Name:</label>
      <div class="col-sm-3">
                
        <INPUT  class="form-control" name="CUST_NAME1" type="TEXT" value="<%=sCustName%>" style="width:100%" MAXLENGTH=100> <!--  -->
      </div>
    </div>
    </div>
    
	<INPUT type="hidden" id="TaxByLabel" name="taxbylabel" value=<%=taxbylabel%>>
    
        <div class="form-group">
       <label class="control-label col-sm-3" for="Supplier Type">Supplier Type:</label>
      <div class="col-sm-3">
                
        <div class="input-group">    
    		<input name="SUPPLIER_TYPE_ID" type="TEXT" value="<%=suppliertypeid%>" size="50"
			style="width:100%" MAXLENGTH="50" class="form-control">
   		 	<span class="input-group-addon" 
   		  onClick="javascript:popUpWin('suppliertypelistsave.jsp?SUPPLIER_TYPE_ID='+form.SUPPLIER_TYPE_ID.value);"> 	
   		 	<a href="#" data-toggle="tooltip" data-placement="top" title="Customer Type Details">
   		 	<i class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span>
  	</div>
      </div>
        <div class="form-inline">
         <label class="control-label col-sm-2" for="Telephone No">Supplier Phone:</label>
      <div class="col-sm-3">           	
  	 <INPUT name="TELNO" type="TEXT" value="<%=sTelNo%>" size="50" style="width:100%" class="form-control" onkeypress="return isNumber(event)"
			MAXLENGTH="30">
  	</div>
  	</div>
  	 </div>
  	 
  	 
    
    <div class="form-group">
     <label class="control-label col-sm-3" for="Fax">Supplier Fax:</label>
      <div class="col-sm-3">
               
        <INPUT name="FAX" type="TEXT" value="<%=sFax%>"  onkeypress="return isNumber(event)"
			MAXLENGTH="30" class="form-control">
      </div>
     
      <div class="form-inline">
       <label class="control-label col-sm-2" for="Customer Email">Supplier Email:</label>
      <div class="col-sm-3"> 
              
        <INPUT name="CUSTOMEREMAIL" type="TEXT" value="<%=CUSTOMEREMAIL%>" size="50" style="width:100%"
			MAXLENGTH="50" class="form-control">
      </div>
     
    </div>
    </div>
    
    <div class="form-group">
      	<label class="control-label col-sm-3" for="Website">Website</label>
      	<div class="col-sm-3">  
    	<INPUT name="WEBSITE" type="TEXT" value="<%=WEBSITE%>"  MAXLENGTH=50 class="form-control">
      	</div>
    	</div>
    
<div class="bs-example">
     <ul class="nav nav-tabs" id="myTab"> 
     	<li class="nav-item active">
            <a href="#other" class="nav-link" data-toggle="tab" aria-expanded="true" style="text-decoration:none;">Other Details</a>
        </li>
        <li class="nav-item">
            <a href="#profile" class="nav-link" data-toggle="tab" style="text-decoration:none;">Contact Details</a>
        </li>
        <li class="nav-item">
            <a href="#home" class="nav-link" data-toggle="tab" style="text-decoration:none;">Address</a>
        </li>
        <li class="nav-item">
            <a href="#bank_cus" class="nav-link" data-toggle="tab" style="text-decoration:none;">Bank Account Details</a>
        </li>
        <li class="nav-item">
            <a href="#remark" class="nav-link" data-toggle="tab" style="text-decoration:none;">Remarks</a>
        </li>
        </ul>
        <div class="tab-content clearfix">
        <div class="tab-pane active" id="other">
        <br>
        
        <div class="form-group">
        <label class="control-label col-sm-3 required" for="Tax Treatment">Tax Treatment:</label>			
			<div class="col-sm-3 ac-box">				
				<SELECT class="form-control" data-toggle="dropdown" data-placement="right" onchange="OnTaxChange(this.value)" id="TAXTREATMENT" name="TAXTREATMENT" value="<%=sTAXTREATMENT%>" style="width: 100%">
				<OPTION style="display:none;">Select Tax Treatment</OPTION>
				<%
		   MasterUtil _MasterUtil=new  MasterUtil();
		   ArrayList ccList =  _MasterUtil.getTaxTreatmentList("",plant,"");
			for(int i=0 ; i<ccList.size();i++)
      		 {
				Map m=(Map)ccList.get(i);
				String vTAXTREATMENT = (String)m.get("TAXTREATMENT"); %>
		        <option  value='<%=vTAXTREATMENT%>' ><%=vTAXTREATMENT %> </option>		          
		        <%
       			}
			 %></SELECT>
			</div>
			
			<div class="form-inline">
       <label class="control-label col-sm-2" for="TRN/RCB NO" id="TaxLabel"></label>
      <div class="col-sm-3">                
        <INPUT name="RCBNO" type="TEXT" class="form-control" value="<%=sRcbno%>" style="width:100%" MAXLENGTH="30">
      </div>			
		</div>		
		</div>
		
		<div class="form-group">
      	<label class="control-label col-sm-3" for="Opening Balance">Opening Balance</label>
      	<div class="col-sm-3">  
        <INPUT name="OPENINGBALANCE" type="TEXT" value="<%=new java.math.BigDecimal(OPENINGBALANCE).toPlainString()%>" onkeypress="return isNumberKey(event,this,4)"	 MAXLENGTH=50 class="form-control">
      	</div>
      	<div class="form-inline">
      	<label class="control-label col-sm-2" for="Payment Type">Payment Type:</label>
      <div class="col-sm-3">  
              <div class="input-group">
       <input name="PAYTERMS" type="text" value="<%=sPayTerms%>" style="width:100%"
			size="50" MAXLENGTH=50 class="form-control" readonly>
			<span class="input-group-addon"  onClick="javascript:popUpWin('list/paymenttypelist_save.jsp?paymenttype='+form.PAYTERMS.value);">
		   		<a href="#" data-toggle="tooltip" data-placement="top" title="Payment Type">
		   		<i class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span></div>
      </div>
      	</div>
      	</div>
    
     <div class="form-group">
      <label class="control-label col-sm-3" for="Days">Payment Days:</label>
      <div class="col-sm-3">
       <input name="PMENT_DAYS" type="text" value="<%=sPayInDays%>"
			 MAXLENGTH=10  class="form-control">
      </div>
      <div class="form-inline">
      <label class="control-label col-sm-2" for="Facebook">Facebook Id</label>
      	<div class="col-sm-3">  
        <INPUT name="FACEBOOK" type="TEXT" value="<%=FACEBOOK%>" style="width:100%" size="50" MAXLENGTH=50 class="form-control">
      	</div>
      </div>
      </div>
      
      <div class="form-group">
       <label class="control-label col-sm-3" for="Twitter Id">Twitter Id:</label>
      <div class="col-sm-3">        
       <INPUT name="TWITTER" type="TEXT" class="form-control" value="<%=TWITTER%>" size="50" MAXLENGTH="100" >
      </div>
   <div class="form-inline">
    <label class="control-label col-sm-2" for="LinkedIn Id">LinkedIn Id:</label>
	<div class="col-sm-3">
        <INPUT name="LINKEDIN" type="TEXT" class="form-control"	value="<%=LINKEDIN%>" size="50" style="width:100%" MAXLENGTH="30">
      </div>
   </div>
   </div>
   
   <div class="form-group">
       <label class="control-label col-sm-3" for="Skype Id">Skype Id:</label>
      <div class="col-sm-3">        
       <INPUT name="SKYPE" type="TEXT" class="form-control" value="<%=SKYPE%>" size="50" MAXLENGTH="100" >
      </div>
      </div>
		
        </div>
        
        <div class="tab-pane fade" id="profile">
        <br>
     <div class="form-group">
       <label class="control-label col-sm-3" for="Contact Name">Contact Name:</label>
      <div class="col-sm-3">        
       <INPUT name="CONTACTNAME" type="TEXT" class="form-control"
			value="<%=sContactName%>" size="50" MAXLENGTH="100" >
      </div>
   <div class="form-inline">
    <label class="control-label col-sm-2" for="Designation">Designation:</label>
	<div class="col-sm-3">
        <INPUT name="DESGINATION" type="TEXT" class="form-control"
			value="<%=sDesgination%>" size="50" style="width:100%" MAXLENGTH="30">
      </div>
   </div>
   </div>
    
    <div class="form-group">
     <label class="control-label col-sm-3" for="Work Phone">Work Phone:</label>
      <div class="col-sm-3">
                 
        <INPUT name="WORKPHONE" type="TEXT" value="<%=WORKPHONE%>"  class="form-control" onkeypress="return isNumber(event)"
			MAXLENGTH="30">
      </div>
      
      <div class="form-inline">
      <label class="control-label col-sm-2" for="Hand Phone">Mobile:</label>
      <div class="col-sm-3">
              
        <INPUT name="HPNO" type="TEXT" value="<%=sHpNo%>" size="50" class="form-control" style="width:100%" onkeypress="return isNumber(event)"
			MAXLENGTH="30">
      </div>
     
    </div>
    </div>
    
    <div class="form-group">
       <label class="control-label col-sm-3" for="Email">Email:</label>
      <div class="col-sm-3"> 
              
        <INPUT name="EMAIL" type="TEXT" value="<%=sEmail%>" size="50" MAXLENGTH="50" class="form-control">
      </div>
     
    </div>
       
        </div>
        
        <div class="tab-pane fade" id="home">
        <br>
        
        <div class="form-group">
        <label class="control-label col-sm-3 required" for="Country">Country:</label>     
      <div class="col-sm-3">  
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
     
      <div class="form-inline">
      <label class="control-label col-sm-2" for="Unit No">Unit No:</label>       
      <div class="col-sm-3">
        <INPUT name="ADDR1" type="TEXT" value="<%=sAddr1%>" size="50" style="width:100%" 
			MAXLENGTH=50 class="form-control">
      </div>
     
    </div>
    </div>
    
    <div class="form-group">     
     <label class="control-label col-sm-3" for="Building">Building:</label>
      <div class="col-sm-3">
        <INPUT name="ADDR2" type="TEXT" value="<%=sAddr2%>"
			MAXLENGTH=50 class="form-control">
      </div>
     
      <div class="form-inline">
      <label class="control-label col-sm-2" for="Street">Street:</label>       
      <div class="col-sm-3"> 
        <INPUT name="ADDR3" type="TEXT" value="<%=sAddr3%>" size="50" style="width:100%" 
			MAXLENGTH=50 class="form-control">
      </div>
     
    </div>
    </div>
    
    <div class="form-group">
    <label class="control-label col-sm-3" for="City">City:</label>     
      <div class="col-sm-3">
      <INPUT name="ADDR4" type="TEXT" value="<%=sAddr4%>"
			MAXLENGTH=50  class="form-control">
      </div>
      
      <div class="form-inline">
      <label class="control-label col-sm-2" for="State">State:</label>      
      <div class="col-sm-3">  
             
      <SELECT class="form-control" data-toggle="dropdown" data-placement="right" id="STATE" name="STATE" value="<%=sState%>" style="width: 100%">
				<OPTION style="display:none;">Select State</OPTION>
				</SELECT>
      </div>
     
    </div>
    </div>
    
    <div class="form-group">
    <label class="control-label col-sm-3" for="Postal Code">Postal Code:</label>
      <div class="col-sm-3">
                
        <INPUT name="ZIP" type="TEXT" value="<%=sZip%>" MAXLENGTH=10 class="form-control">
      </div>
      </div>
    
        </div>
        
        <div class="tab-pane fade" id="bank_cus">
        <br>
        
        <div class="form-group">
      	<label class="control-label col-sm-3" for="IBAN">IBAN</label>
      	<div class="col-sm-3">  
        <INPUT name="IBAN" type="TEXT" value="<%=sIBAN%>"	size="50" MAXLENGTH=100 class="form-control">
      	</div>
      	<div class="form-inline">
      	<label class="control-label col-sm-2">Bank</label>
			<div class="col-sm-3 ac-box">				
				<SELECT class="form-control" data-toggle="dropdown" data-placement="right" onchange="OnBank(this.value)" id="BANKNAME" name="BANKNAME" value="<%=sBANKNAME%>" style="width: 100%">
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
			 %></SELECT>
			</div>
      	</div>
    	</div>
		
		<div class="form-group">
      	<label class="control-label col-sm-3" for="Branch">Branch</label>
      	<div class="col-sm-3">  
        <INPUT name="BRANCH" type="TEXT" value="<%=sBRANCH%>"	size="50" MAXLENGTH=100 class="form-control" readonly>
      	</div>
        <div class="form-inline">
      	<label class="control-label col-sm-2" for="Routing Code">Routing Code</label>
      	<div class="col-sm-3">  
        <INPUT name="BANKROUTINGCODE" type="TEXT" value="<%=sBANKROUTINGCODE%>"	size="50" MAXLENGTH=100 class="form-control">
      	</div>
    	</div>
    	</div>
        
        </div>
        
        <div class="tab-pane fade" id="remark">
        <br>
        <div class="form-group">
      	<label class="control-label col-sm-3" for="Remarks">Remarks</label>
      	<div class="col-sm-4">
        <textarea  class="form-control" name="REMARKS"   MAXLENGTH=1000><%=sRemarks%></textarea>
      	</div>
    	</div>
		</div>
		</div>
		</div>
		
		<div class="form-group">
      <div class="col-sm-offset-4 col-sm-8">                        
                        
       <button type="button" class="Submit btn btn-default" onClick="onNew();"<%=sNewEnb%>><b>Clear</b></button>&nbsp;&nbsp;
      	<% if (al) { %>
      	<button  type="button" class="btn btn-success" data-toggle="modal" data-target="#myModal" onClick="onAdd();"<%=sAddEnb%>><b>Save</b></button>&nbsp;&nbsp;
      	<% } else { %>
   		<button disabled="disabled" type="button" class="btn btn-success" data-toggle="modal" data-target="#myModal" onClick="onAdd();"<%=sAddEnb%>><b>Save</b></button>&nbsp;&nbsp;
		<% } %>
      	<button type="button" class="Submit btn btn-default" onClick="window.close();"><b>Close</b></button>&nbsp;&nbsp;
      	
      </div>
       </div>
               
  </div>
    
    <div class="form-group">
      <div class="col-sm-3">
      <a href="#" id="Show" style="font-size: 15px; color: #0059b3; text-decoration:none;">Show Create Supplier</a>
      <a href="#" id="Hide" style="font-size: 15px; color: #0059b3; text-decoration:none; display:none;">Hide Create Supplier</a>
      </div>
       	  </div>
               
  </div>

  <div>
  <table id="myTable" class="table">
    <thead style="background: #eaeafa">
    <TR>
      <TH>Supplier ID</TH>
      <TH>Supplier Name</TH>
    <!--  <TH align="left"><font color="white">Last Name</font></TH> -->
    </TR>
    </thead>
    <tbody>
<%
HashMap<String, String> loggerDetailsHasMap = new HashMap<String, String>();
loggerDetailsHasMap.put(MLogger.COMPANY_CODE, (String) session
		.getAttribute("PLANT"));
loggerDetailsHasMap.put(MLogger.USER_CODE, StrUtils.fString(
		(String) session.getAttribute("LOGIN_USER")).trim());
/* MLogger mLogger = new MLogger(); */
mLogger.setLoggerConstans(loggerDetailsHasMap);

     CustUtil custUtils = new CustUtil();
     custUtils.setmLogger(mLogger);
     strUtils = new StrUtils();
     plant= (String)session.getAttribute("PLANT");
     sCustName = strUtils.fString(request.getParameter("CUST_NAME")); 
    
  //  String sCustCode=  strUtils.fString(request.getParameter("CUST_CODE"));
  //  com.track.util.MLogger.log(0," List view for customer CUST_CODE : " + sCustCode);
    String sBGColor = "";
    
   try{
	   ArrayList arrCust = custUtil.getVendorListStartsWithName(sCustName,plant," AND ISACTIVE='Y' ORDER BY VNAME asc");
   
      for(int i =0; i<arrCust.size(); i++) {
        sBGColor = ((i == 0) || (i % 2 == 0)) ? "#FFFFFF" : "#dddddd";
        Map arrCustLine = (Map)arrCust.get(i);
        sCustCode     = (String)arrCustLine.get("VENDNO");
        String sCustName1     = strUtils.replaceCharacters2Send((String)arrCustLine.get("VNAME"));
        sContactName     = strUtils.replaceCharacters2Send((String)arrCustLine.get("NAME"));
        sTelNo     = strUtils.removeQuotes((String)arrCustLine.get("TELNO"));
        sEmail     = strUtils.removeQuotes((String)arrCustLine.get("EMAIL"));
        String sAdd1     = strUtils.removeQuotes((String)arrCustLine.get("ADDR1"));
        String sAdd2     = strUtils.removeQuotes((String)arrCustLine.get("ADDR2"));
        String sAdd3     = strUtils.removeQuotes((String)arrCustLine.get("ADDR3"));
        sRemarks     = strUtils.replaceCharacters2Send((String)arrCustLine.get("REMARKS"));
        String sAdd4     = strUtils.removeQuotes((String)arrCustLine.get("ADDR4"));
        sCountry    = strUtils.replaceCharacters2Send((String)arrCustLine.get("COUNTRY"));
        sZip     = strUtils.removeQuotes((String)arrCustLine.get("ZIP"));
        paymenttype=strUtils.removeQuotes((String)arrCustLine.get("PAYMENT_TERMS"));
        sTAXTREATMENT=strUtils.removeQuotes((String)arrCustLine.get("TAXTREATMENT"));        


%>

<TR>
		<td class="main2" style="font-weight: bold" align="left">&nbsp;<a href="#"
			onClick="window.opener.form.CUST_CODE.disabled = true;
                     window.opener.form.CUST_CODE.value='<%=sCustCode%>';
                     window.opener.form.CUST_CODE1.value='<%=sCustCode%>';
                     window.opener.form.CUST_NAME.value='<%=strUtils.insertEscp(sCustName1)%>';
                     window.opener.form.PERSON_INCHARGE.value='<%=strUtils.insertEscp(sContactName)%>';
                     window.opener.form.TELNO.value='<%=sTelNo%>';
                     window.opener.form.EMAIL.value='<%=sEmail%>';
                     window.opener.form.ADD1.value='<%=sAdd1%>';
                     window.opener.form.ADD2.value='<%=sAdd2%>';
                     window.opener.form.ADD3.value='<%=sAdd3%>';
                     window.opener.form.REMARK2.value='<%=strUtils.insertEscp(sRemarks)%>';
                     window.opener.form.ADD4.value='<%=sAdd4%>';
                     window.opener.form.COUNTRY.value='<%=strUtils.insertEscp(sCountry)%>';
                     window.opener.form.ZIP.value='<%=sZip%>';
                      window.opener.form.PAYMENTTYPE.value='<%=paymenttype%>';
                      window.opener.form.TAXTREATMENT.value='<%=sTAXTREATMENT%>';
                      window.opener.document.getElementById('TAXTREATMENT').innerHTML='<%=sTAXTREATMENT%>';
                      if('<%=sTAXTREATMENT%>'=='GCC VAT Registered'||'<%=sTAXTREATMENT%>'=='GCC NON VAT Registered'||'<%=sTAXTREATMENT%>'=='NON GCC'){window.opener.document.getElementById('CHK1').style.display = 'block'}else{window.opener.document.getElementById('CHK1').style.display = 'none'};
                     window.close();"><%=sCustCode%></a></td>
		<td align="left" class="main2">&nbsp;<%=strUtils.replaceCharacters2Recv(sCustName1)%></td>
	</TR>
    
<%
}
}catch(Exception he){he.printStackTrace(); System.out.println("Error in reterieving data");}
%>
</tbody>
 </table>
 <!-- <br>
  <div class="text-center">       
        <button type="button" class="Submit btn btn-default" onClick="window.close();"><b>Close</b></button>&nbsp;&nbsp;
	     </div> -->    
</div>
</form>
</body>
</html>

<script>
$(document).ready(function(){
	$('#myTable').dataTable();
	
    $('[data-toggle="tooltip"]').tooltip();
	
//Below Jquery Script used for Auto Change Tax By Label Function

    var  d = document.getElementById("TaxByLabel").value;
    document.getElementById('TaxLabel').innerHTML = d +" No.:";
    
//Below Jquery Script used for Show/Hide Function
    
    $('#Show').click(function() {
	    $('#target').show(500);
	    $('#Show').hide(0);
	    $('#Hide').show(0);
	    $('#search_criteria_status').val('show');
	});
 
    $('#Hide').click(function() {
	    $('#target').hide(500);
	    $('#Show').show(0);
	    $('#Hide').hide(0);
	    $('#search_criteria_status').val('hide');
	});
    if ('<%=request.getParameter("search_criteria_status")%>' == 'show'){
    	$('#Show').click();
    }else{
    	$('#Hide').click();
    }
});
function checkitem(aCustCode)
{	
	 if(aCustCode=="" || aCustCode.length==0 ) {
		alert("Enter Supplier ID!");
		document.getElementById("CUST_CODE").focus();
		return false;
	 }else{ 
			var urlStr = "/track/MasterServlet";
			$.ajax( {
				type : "POST",
				url : urlStr,
				async : true,
				data : {
					CUST_CODE : aCustCode,
	                USERID : "<%=username%>",
					PLANT : "<%=plant%>",
					ACTION : "SUPPLIER_CHECK"
					},
					dataType : "json",
					success : function(data) {
						if (data.status == "100") {
	                               
							alert("Supplier Already Exists");
							document.getElementById("CUST_CODE").focus();
							//document.getElementById("ITEM").value="";
							return false;
						}
						else
							return true;
					}
	});	
			return true;
	}
}
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
			$('#STATE').append('<OPTION style="display:none;">Select State</OPTION>');
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
.nav-tabs li a:hover {
     background-color: #00c0ef;
}
</style>