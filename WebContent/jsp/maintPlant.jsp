<%@ page contentType="text/html;charset=windows-1252"%>
<%@ page import="com.track.util.*"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.gates.*"%>
<%@ page import="com.track.dao.*"%>
<%@ page import="com.track.constants.*"%>
<%@ page import="java.util.*"%>

<%
String title = "Maintain Company";
%>
<%@include file="sessionCheck.jsp" %>
<jsp:include page="header2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
</jsp:include>
<script src="js/typeahead.jquery.js"></script>
<link rel="stylesheet" href="css/typeahead.css">
<link rel="stylesheet" href="css/accounting.css">
<link rel="stylesheet" href="css/fileshowcasedesign.css">
<link rel="stylesheet" href="css/bootstrap-datepicker.css">
<script type="text/javascript" src="js/json2.js"></script>
<script type="text/javascript" src="js/general.js"></script>
<jsp:useBean id="countryNCurrencyDAO"  class="com.track.dao.CountryNCurrencyDAO" />
<meta http-equiv="Content-Type" content="text/html; charset=windows-1252">

<script>

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
      /*var expyear=new Date(document.form1.EXPIRYDATE.value);
      var yearplus=expyear.getFullYear();
      var yearminus=yearplus-1;*/
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
function onNew(){
 
   document.form1.action  = "maintPlant.jsp?action=NEW";
   document.form1.submit();
}
function onView(){
	var ITEM_ID   = document.form1.PLANT.value;
	if(ITEM_ID == "" || ITEM_ID == null) {alert("Please Enter Company ID");document.form1.PLANT.focus(); return false; }
	else{
    document.form1.action = "maintPlant.jsp?action=View";
    document.form1.submit();
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
	   var ITEM_ISTAXREG=document.form1.ISTAXREGISTRED.value;
	   //var rcbno = document.form1.RCBNO.value;
	   var cntperson = document.form1.NAME.value;
	   var telno = document.form1.TELNO.value;
	   var add1 = document.form1.ADD1.value;
	   var add3 = document.form1.ADD3.value;
	   var cnty = document.form1.COUNTY.value;
	   //var companyregnumber = document.form1.companyregnumber.value;
	   var region = document.form1.REGION.value;
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
	   
	   if(ITEM_ID == "" || ITEM_ID == null) {alert("Please Enter Company ID");document.form1.PLANT.focus(); return false; }
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

	  /*  IMTHI&NAVAS */
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
	   else if(ENDDT == "" || ENDDT == null) {alert("Please Select Expiry Date");document.form1.EXPIRYDATE.focus(); return false; } 
	   else if(todt <frdt) {alert("Please Select Expiry Date greater than Start Date");return false; }
	   else if(noofcatlogs == "" || noofcatlogs == null) {alert("Please Enter Number of Catalogs");document.form1.NOOFCATALOGS.focus(); return false; }
	   else if(noofcatlogs ==0) {alert("Enter Valid Number Of Catalogs");document.form1.NOOFCATALOGS.focus(); return false; }
	   else if(noofsignatures == "" || noofsignatures == null) {alert("Please Enter Number of Signatures");document.form1.NOOFSIGNATURES.focus(); return false; }
	   else if(noofsignatures ==0) {alert("Enter Valid Number Of Signatures");document.form1.NOOFSIGNATURES.focus(); return false; }
	   else if(base_currency ==0){alert("Please select the base currency");document.form1.BaseCurrency.focus();return false;}
	   else if(chkPurchaseSystem=="0"){alert("Please select any Purchase System");document.form1.ENABLE_INVENTORY.focus();return false;}
	   else if(COUNTRY_CODE != "SG")
	   {
		   if(rcbn>0) {
		   if("15"!=rcbn)
		   {
			   alert(" Please Enter your 15 digit numeric "+taxbylabel+" No.");
			   document.form1.RCBNO.focus();
			   return false;
		   }
		   }
		}
	   
	   if(taxby =="0")
	   {
		   alert("Please select the tax by");
		   document.form1.TAXBY.focus();return false;
	   }
	   if(taxbylabel ==0){alert("Please select the tax by label order configuration");document.form1.TAXBYLABEL.focus();return false;}
	   if(taxbylabelmagt ==0){alert("Please select the tax by label order management");document.form1.TAXBYLABELORDERMANAGEMENT.focus();return false;}
	   /* else if(fiscalyear == "" || fiscalyear == null) {alert("Please Select Fiscal Year from Other Details");document.form1.FISCALYEAR.focus(); return false; }
	   else if(payear == "" || payear == null) {alert("Please Select PayRoll Year from Other Details");document.form1.PAYROLLYEAR.focus(); return false; } */
	   else if(cntperson == "" || cntperson == null) {alert("Please Enter Contact Name from Contact Details");document.form1.NAME.focus(); return false; }
	   else if(telno == "" || telno == null) {alert("Please Enter Telephone No from Contact Details");document.form1.TELNO.focus(); return false; }
	   else if(document.form1.REGION.selectedIndex==0) {alert("Please Select Region from Address");document.form1.REGION.focus();return false; }
	   else if(document.form1.COUNTRY_CODE.selectedIndex==0) {alert("Please Select Country from Address"); document.form1.COUNTRY_CODE.focus();return false; }
	   /* else if(add1 == "" || add1 == null) {alert("Please Enter Unit No from Address");document.form1.ADD1.focus(); return false; } */
	   //else if(add3 == "" || add3 == null) {alert("Please Enter Street from Address");document.form1.ADD3.focus(); return false; }   
	   else if(zip == "" || zip == null) {alert("Please Enter Postal Code from Address");document.form1.ZIP.focus(); return false; }
	   else{
	   var chkmsg=confirm("Are you sure you would like to save?");
	   if(chkmsg){
	   attach_edit();}
	   }

}
  function OnClear()
  {
  document.form1.PLANT.value="";
  document.form1.PLNTDESC.value="";document.form1.STARTDATE.value="";
  document.form1.EXPIRYDATE.value="";
  document.form1.PRODUCT_SHOWBY_CATAGERY.checked = false;
  document.form1.ISASSIGN_USERLOC.checked = false;
  document.form1.SHOW_STOCKQTY_ONAPP.checked = false;
  document.form1.ISSHOWPOSPRICEBYOUTLET.checked = false;
  document.form1.ALLOWPOSTRAN_LESSTHAN_AVBQTY.checked = false;
  document.form1.ISPOSRETURN_TRAN.checked = false;
  document.form1.ISPOSVOID_TRAN.checked = false;
  document.form1.ISAUTO_CONVERT_ESTPO.checked = false;
  document.form1.ISPRODUCTMAXQTY.checked = false;
  document.form1.ISAUTO_CONVERT_RECEIPTBILL.checked = false;
  document.form1.SETCURRENTDATE_GOODSRECEIPT.checked = false;
  document.form1.ISEMPLOYEEVALIDATEPO.checked = false;
  document.form1.ISEMPLOYEEVALIDATESO.checked = false;
  document.form1.ISAUTO_CONVERT_ISSUETOINVOICE.checked = false;
  
  document.form1.SETCURRENTDATE_PICKANDISSUE.checked = false;
  document.form1.SHOWITEM_AVGCOST.checked = false;
  document.form1.ISAUTO_MINMAX_MULTIESTPO.checked = false;
  document.form1.ISAUTO_CONVERT_SOINVOICE.checked = false;
  document.form1.ENABLE_RESERVEQTY.checked = false;
  document.form1.ISSALESTOPURCHASE.checked = false;
  document.form1.ISREFERENCEINVOICE.checked = false;
  return true;
  }

  function getval() {
	  var  d = document.getElementById("select_id").value;
	     document.getElementById('TaxLabel').innerHTML = d + " No.:";

	}

  function image_edit(){
  	$('#userImage').val('');
  	var myForm = document.getElementById('form1');
  	var formData = new FormData(myForm);
      //var formData = new FormData($('form')[0]);
      var userId= form1.PLANT.value;
  	if(userId){
      $.ajax({
          type: 'post',
          url: "/track/CatalogServlet?Submit=logo_img_edit&Type=Logo",
         	dataType:'html',
      data:  formData,//{key:val}
      contentType: false,
      processData: false,
        
          success: function (data) {
          	console.log(data)
          	var result =JSON.parse(data).result;
             	$('#msg').html(result.message); 
             	$('#productImg').val('');
          },
          error: function (data) {
          	
              alert(data.responseText);
              $('#productImg').val('');
          }
      });
  	}else{
  		alert("Please enter Company Id");
  	}
          return false; 
    }
    
  function image_delete(){
  	
      //var formData = new FormData($('form')[0]);
      var formData = $('form').serialize();
      var userId= form1.PLANT.value;
  	if(userId){
      $.ajax({
          type: 'post',
          url: "/track/CatalogServlet?Submit=logo_img_delete&Type=Logo",
         	dataType:'html',
      data:  formData,//{key:val}
        
          success: function (data) {
          	console.log(data)
          	var result =JSON.parse(data).result;
          	$('#msg').html(result.message); 
          	  $('#item_img').attr('src',"../jsp/dist/img/NO_IMG.png");
          	  $('#productImg').val('');
           
          },
          error: function (data) {
          	
              alert(data.responseText);
          }
      });
  	}else{
  		alert("Please enter Company Id");
  	}
          return false; 
    }	


  function attach_edit(){		
 	    
 	 	var myForm = document.getElementById('form1');
 		var formData = new FormData(myForm);
 	    var userId= form1.PLANT.value;
 		if(userId){
 	    $.ajax({
 	        type: 'post',
 	        url: "/track/MasterServlet?action=comp_attach_edit&Type=Attachment",
 	       	dataType:'html',
 	    data:  formData,//{key:val}
 	    contentType: false,
 	    processData: false,
 	      
 	        success: function (data) {
 	        	console.log(data)
 	        	var result =JSON.parse(data).result;
 	           	$('#msg').html(result.message);
 	           	RetrunUpdate();
 	        },
 	        error: function (data) {
 	        	
 	        	RetrunUpdate();
 	            //alert(data.responseText);
 	        }
 	    });
 		}else{
 			//alert("Please enter Product Id");
 		}
 	        return false; 
 	  }

  
  function RetrunUpdate(){
	  document.form1.action  = "maintPlant.jsp?action=ADD";
	  document.form1.submit();
	  }
  
 </script>
 
 <script src="js/calendar.js"></script>
 
 <%@ include file="header.jsp"%>
<%
	String sUserId = (String) session.getAttribute("LOGIN_USER");
	String Plant = (String) session.getAttribute("PLANT");
	String sPlantDesc="",sPlant="" ,action="";
	StrUtils strUtils = new StrUtils();
	sPlantDesc  = StrUtils.fString(request.getParameter("PLANTDESC"));
	action = StrUtils.fString(request.getParameter("action"));
	sPlant  = StrUtils.fString(request.getParameter("PLANT"));
	String PARENT_PLANT = new ParentChildCmpDetDAO().getPARENT_CHILD(Plant);//Check Parent Plant 
	
	String PLANT="",COMPANY="",PLNTDESC="",companyregnumber="",STARTDATE="",EXPIRYDATE="",NOOFCATALOGS="",ACTUALEXPIRYDATE="",SDATE="",EDATE="",RCBNO="",
	       NAME="",DESGINATION="",TELNO="",HPNO="",EMAIL="",ADD1="",ADD2="",ADD3="",ADD4="",FAX="",REMARKS="",COUNTY="",ZIP="",
           SALESPERCENT="",SDOLLARFRATE="",SCENTSFRATE="",SALES="",EDOLLARFRATE="",ECENTSFRATE="",currencyCode="",COMP_INDUSTRY="",NOOFSIGNATURES="",STATE="",PERCENTAGE="",
	       FLATRATE="", ENABLEINVENTORY = "0", ENABLEACCOUNTING = "0",OWNERAPP= "0",CUSTOMERAPP="0",MANAGERAPP = "0",STOREAPP = "0",RIDEAPP = "0",ISTAXREG="",REPROTSBASIS="",
	       DECIMALPRECISION="",IMAGEPATH="",APPPATH="",TAXBY="",TAXBYLABEL="",TAXBYLABELORDERMANAGEMENT="",strtaxby="", strtaxbylabe1="",strtaxbylabe1order="",
			FISCALYEAR="",PAYROLLYEAR="",REGION="",WEBSITE="",FACEBOOK="",TWITTER="",LINKEDIN="",SKYPE="",ENABLEPAYROLL="0",ENABLEPOS="0",FYEAR="",PYEAR="",sCountryCode="",PLANTID="",EMPLOYEEWORKINGMANDAYSBY="",IsDefautDrawerAmount="",IsExpJournalEdit="",
					NOOFSUPPLIER="",NOOFCUSTOMER="",NOOFEMPLOYEE="",NOOFUSER="",NOOFINVENTORY="",NOOFLOCATION="",NOOFORDER="",NOOFEXPBILLINV="",SIGNNAME="",SEALNAME="",appPath="",sealPath="",signPath="",PRODUCT_SHOWBY_CATAGERY="",ALLOWPRDTOCOMPANY="",ISASSIGN_USERLOC="",ISSHOWPOSPRICEBYOUTLET="",ALLOWPOSTRAN_LESSTHAN_AVBQTY="",SHOW_STOCKQTY_ONAPP="",ISPOSRETURN_TRAN="",ISPOSVOID_TRAN="",SHOW_POS_SUMMARY="",ISMANAGEWORKFLOW="",ALLOWCATCH_ADVANCE_SEARCH="",SETCURRENTDATE_ADVANCE_SEARCH="",ISPRODUCTMAXQTY="",ISSALESAPP_TAXINCLUSIVE="",ISPOSTAXINCLUSIVE="",ISPOSSALESMAN="",ISAUTO_CONVERT_ESTPO="",ISAUTO_CONVERT_RECEIPTBILL="",SHOWITEM_AVGCOST="",ISAUTO_MINMAX_MULTIESTPO="",SETCURRENTDATE_GOODSRECEIPT="",ISEMPLOYEEVALIDATEPO="",ISEMPLOYEEVALIDATESO="",ISAUTO_CONVERT_ISSUETOINVOICE="",SETCURRENTDATE_PICKANDISSUE="",ISAUTO_CONVERT_SOINVOICE="",ENABLE_RESERVEQTY="",ISSALESTOPURCHASE="",NOOFPAYMENT="",NOOFJOURNAL="", NOOFCONTRA="", shopify="",ISPEPPOL="",PEPPOL_ID="",PEPPOL_UKEY="", lazada="", shopee="", amazon="",ISREFERENCEINVOICE="",ISSUPPLIERMANDATORY="",ISPRICE_UPDATEONLY_IN_OWNOUTLET="",ISSTOCKTAKE_BYAVGCOST="";
	String sNewEnb    = "enabled";
	String sDeleteEnb = "enabled";
	String sAddEnb    = "disabled";
	String sUpdateEnb = "enabled";
	String logores="";
	String res="";
	String existingplnt="";
	boolean flag=false;
	MasterUtil _MasterUtil=new  MasterUtil();
	PlantMstUtil plantmstutil = new PlantMstUtil();
	PlantMstDAO _PlantMstDAO = new PlantMstDAO();
	DateUtils dateutils = new DateUtils();
	StrUtils _strUtils     = new StrUtils();
	
	PLANT     = StrUtils.fString(request.getParameter("PLANT"));
	PLNTDESC     = StrUtils.fString(request.getParameter("PLNTDESC"));
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
	companyregnumber = StrUtils.fString(request.getParameter("companyregnumber"));
	ZIP = StrUtils.fString(request.getParameter("ZIP"));
	REMARKS   = StrUtils.fString(request.getParameter("REMARKS"));
	FAX  = StrUtils.fString(request.getParameter("FAX"));
	RCBNO = StrUtils.fString(request.getParameter("RCBNO"));
	TAXBY= StrUtils.fString(request.getParameter("TAXBY"));
	TAXBYLABEL= StrUtils.fString(request.getParameter("TAXBYLABEL"));
	TAXBYLABELORDERMANAGEMENT= StrUtils.fString(request.getParameter("TAXBYLABELORDERMANAGEMENT"));
	DECIMALPRECISION = StrUtils.fString(request.getParameter("decimal_precision"));
    NOOFCATALOGS = StrUtils.fString(request.getParameter("NOOFCATALOGS")); 
    NOOFSIGNATURES = StrUtils.fString(request.getParameter("NOOFSIGNATURES")); 
    STATE = StrUtils.fString(request.getParameter("STATE"));
    
    REGION=strUtils.fString(request.getParameter("REGION"));
	FISCALYEAR=strUtils.fString(request.getParameter("FISCALYEAR"));
	PAYROLLYEAR=strUtils.fString(request.getParameter("PAYROLLYEAR"));
	WEBSITE=strUtils.fString(request.getParameter("WEBSITE"));
	FACEBOOK=strUtils.fString(request.getParameter("FACEBOOK"));
	TWITTER=strUtils.fString(request.getParameter("TWITTER"));
	LINKEDIN=strUtils.fString(request.getParameter("LINKEDIN"));
	SKYPE=strUtils.fString(request.getParameter("SKYPE"));
	EMPLOYEEWORKINGMANDAYSBY=strUtils.fString(request.getParameter("EMPLOYEEWORKINGMANDAYSBY"));
	
	NOOFSUPPLIER=StrUtils.fString(request.getParameter("No_of_Supplier"));
    NOOFCUSTOMER=StrUtils.fString(request.getParameter("No_of_Customer"));
    NOOFEMPLOYEE=StrUtils.fString(request.getParameter("No_of_Employee"));
    NOOFUSER=StrUtils.fString(request.getParameter("No_of_User"));
    NOOFINVENTORY=StrUtils.fString(request.getParameter("No_of_Inventory"));
    NOOFLOCATION=StrUtils.fString(request.getParameter("No_of_Location"));
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
    PEPPOL_ID = StrUtils.fString(request.getParameter("PEPPOL_ID"));
    PEPPOL_UKEY = StrUtils.fString(request.getParameter("PEPPOL_UKEY"));
    ISPOSTAXINCLUSIVE = StrUtils.fString(request.getParameter("ISPOSTAXINCLUSIVE"));
    ISSALESAPP_TAXINCLUSIVE = StrUtils.fString(request.getParameter("ISSALESAPP_TAXINCLUSIVE"));
    ISPOSSALESMAN = StrUtils.fString(request.getParameter("ISPOSSALESMAN"));
    ALLOWPRDTOCOMPANY=StrUtils.fString(request.getParameter("ALLOWPRDTOCOMPANY"));
	if(ALLOWPRDTOCOMPANY.equalsIgnoreCase("NONE"))
		ALLOWPRDTOCOMPANY="0";
	else if(ALLOWPRDTOCOMPANY.equalsIgnoreCase("PARENT_COMPANY"))
		ALLOWPRDTOCOMPANY="1";
	else if(ALLOWPRDTOCOMPANY.equalsIgnoreCase("CHILD_COMPANY"))
		ALLOWPRDTOCOMPANY="2";
	else
		ALLOWPRDTOCOMPANY="3";

    PRODUCT_SHOWBY_CATAGERY = (request.getParameter("PRODUCT_SHOWBY_CATAGERY") != null) ? "1": "0";
    ISASSIGN_USERLOC = (request.getParameter("ISASSIGN_USERLOC") != null) ? "1": "0";
    SHOW_STOCKQTY_ONAPP = (request.getParameter("SHOW_STOCKQTY_ONAPP") != null) ? "1": "0";
    ISSHOWPOSPRICEBYOUTLET = (request.getParameter("ISSHOWPOSPRICEBYOUTLET") != null) ? "1": "0";
    ALLOWPOSTRAN_LESSTHAN_AVBQTY = (request.getParameter("ALLOWPOSTRAN_LESSTHAN_AVBQTY") != null) ? "1": "0";
    ISPOSRETURN_TRAN = (request.getParameter("ISPOSRETURN_TRAN") != null) ? "1": "0";
    ISPOSVOID_TRAN = (request.getParameter("ISPOSVOID_TRAN") != null) ? "1": "0";
    SHOW_POS_SUMMARY = (request.getParameter("SHOW_POS_SUMMARY") != null) ? "1": "0";
    ISMANAGEWORKFLOW = (request.getParameter("ISMANAGEWORKFLOW") != null) ? "1": "0";
    ALLOWCATCH_ADVANCE_SEARCH = (request.getParameter("ALLOWCATCH_ADVANCE_SEARCH") != null) ? "1": "0";
    SETCURRENTDATE_ADVANCE_SEARCH = (request.getParameter("SETCURRENTDATE_ADVANCE_SEARCH") != null) ? "1": "0";
    ISAUTO_CONVERT_ESTPO = (request.getParameter("ISAUTO_CONVERT_ESTPO") != null) ? "1": "0";
    ISPRODUCTMAXQTY = (request.getParameter("ISPRODUCTMAXQTY") != null) ? "1": "0";
    ISAUTO_CONVERT_RECEIPTBILL = (request.getParameter("ISAUTO_CONVERT_RECEIPTBILL") != null) ? "1": "0";
    SETCURRENTDATE_GOODSRECEIPT = (request.getParameter("SETCURRENTDATE_GOOODSRECEIPT") != null) ? "1": "0";
    ISEMPLOYEEVALIDATEPO  = (request.getParameter("ISEMPLOYEEVALIDATEPO") != null) ? "1": "0";
    ISEMPLOYEEVALIDATESO   = (request.getParameter("ISEMPLOYEEVALIDATESO") != null) ? "1": "0";
    ISAUTO_CONVERT_ISSUETOINVOICE   = (request.getParameter("ISAUTO_CONVERT_ISSUETOINVOICE") != null) ? "1": "0";
    
    SETCURRENTDATE_PICKANDISSUE = (request.getParameter("SETCURRENTDATE_PICKANDISSUE") != null) ? "1": "0";
    SHOWITEM_AVGCOST = (request.getParameter("SHOWITEM_AVGCOST") != null) ? "1": "0";
    ISAUTO_MINMAX_MULTIESTPO = (request.getParameter("ISAUTO_MINMAX_MULTIESTPO") != null) ? "1": "0";
    ISAUTO_CONVERT_SOINVOICE = (request.getParameter("ISAUTO_CONVERT_SOINVOICE") != null) ? "1": "0";
    ENABLE_RESERVEQTY = (request.getParameter("ENABLE_RESERVEQTY") != null) ? "1": "0";
    ISSUPPLIERMANDATORY = (request.getParameter("ISSUPPLIERMANDATORY") != null) ? "1": "0";
	ISPRICE_UPDATEONLY_IN_OWNOUTLET = (request.getParameter("ISPRICE_UPDATEONLY_IN_OWNOUTLET") != null) ? "1": "0";
	ISSTOCKTAKE_BYAVGCOST = (request.getParameter("ISSTOCKTAKE_BYAVGCOST") != null) ? "1": "0";
    ISSALESTOPURCHASE = (request.getParameter("ISSALESTOPURCHASE") != null) ? "1": "0";
    IsDefautDrawerAmount = (request.getParameter("IsDefautDrawerAmount") != null) ? "1": "0";
    ISREFERENCEINVOICE = (request.getParameter("ISREFERENCEINVOICE") != null) ? "1": "0";
    IsExpJournalEdit = (request.getParameter("IsExpJournalEdit") != null) ? "1": "0";
   
    if (!"".equals(StrUtils.fString(request.getParameter("ENABLE_INVENTORY")))){
    	ENABLEINVENTORY = "1";
    }
    if (!"".equals(StrUtils.fString(request.getParameter("ENABLE_ACCOUNTING")))){
    	ENABLEACCOUNTING = "1";
    }
    
    if (!"".equals(StrUtils.fString(request.getParameter("ENABLE_PAYROLL")))){
    	ENABLEPAYROLL = "1";
    }
    if (!"".equals(StrUtils.fString(request.getParameter("ENABLE_POS")))){
    	ENABLEPOS = "1";
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
    if(!FISCALYEAR.equals("") || FISCALYEAR.equals(null))
	{
	    FYEAR    = FISCALYEAR.substring(6)+"-"+ FISCALYEAR.substring(3,5)+"-"+FISCALYEAR.substring(0,2);
	}
	if(!PAYROLLYEAR.equals("") || PAYROLLYEAR.equals(null))
	{
	   PYEAR    = PAYROLLYEAR.substring(6)+"-"+ PAYROLLYEAR.substring(3,5)+"-"+PAYROLLYEAR.substring(0,2);
	}

        
    if(SALESPERCENT.length()==0)SALESPERCENT="0";
    if(SDOLLARFRATE.length()==0)SDOLLARFRATE="0";
    if(SCENTSFRATE.length()==0)SCENTSFRATE="0";
        
        if(EDOLLARFRATE.length()==0)EDOLLARFRATE="0";
        if(ECENTSFRATE.length()==0)ECENTSFRATE="0";
	if(!STARTDATE.equals("") || STARTDATE.equals(null))
	{
	    SDATE    = STARTDATE.substring(6)+"-"+ STARTDATE.substring(3,5)+"-"+STARTDATE.substring(0,2);
	}
	if(!EXPIRYDATE.equals("") || EXPIRYDATE.equals(null))
	{
	   EDATE    = EXPIRYDATE.substring(6)+"-"+ EXPIRYDATE.substring(3,5)+"-"+EXPIRYDATE.substring(0,2);
	}
	//Start code added by Deen for base Currency inclusion on Aug 15 2012 
	currencyCode = StrUtils.fString(request.getParameter("BaseCurrency"));
	//End code added by Deen for base Currency inclusion on Aug 15 2012 
	
	String imagePath = DbBean.COMPANY_LOGO_PATH + "/" + PLANT.toLowerCase() + DbBean.LOGO_FILE;
	
	if(action.equalsIgnoreCase("NEW")){
	      PLANT="";
	      PLNTDESC="";
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
	      ADD3   = "";  ADD4   = "";
	      COUNTY="";ZIP="";
	      companyregnumber="";
	      REMARKS="";
	      FAX="";
	      RCBNO="";
          SALESPERCENT="0";
          SDOLLARFRATE="0";
          SCENTSFRATE="0";
          EDOLLARFRATE="0";
          ECENTSFRATE="0";
          STATE="";
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
          PEPPOL_UKEY="";
          PRODUCT_SHOWBY_CATAGERY="";
          ISASSIGN_USERLOC="";
          SHOW_STOCKQTY_ONAPP="";
          ISSHOWPOSPRICEBYOUTLET="";
          ALLOWPOSTRAN_LESSTHAN_AVBQTY="";
          ISPOSRETURN_TRAN="";
          ISPOSVOID_TRAN="";
          SHOW_POS_SUMMARY="";
          ISMANAGEWORKFLOW="";
          ALLOWCATCH_ADVANCE_SEARCH="";
          SETCURRENTDATE_ADVANCE_SEARCH="";
          ISAUTO_CONVERT_ESTPO="";
          ISPRODUCTMAXQTY="";
          ISAUTO_CONVERT_RECEIPTBILL="";
          SETCURRENTDATE_PICKANDISSUE="";
          SETCURRENTDATE_GOODSRECEIPT="";
          ISEMPLOYEEVALIDATEPO="";
          ISEMPLOYEEVALIDATESO="";
          ISAUTO_CONVERT_ISSUETOINVOICE="";
          SHOWITEM_AVGCOST="";
          ISAUTO_MINMAX_MULTIESTPO="";
          ISAUTO_CONVERT_SOINVOICE="";
          ENABLE_RESERVEQTY="";
          ISSUPPLIERMANDATORY="";
		  ISPRICE_UPDATEONLY_IN_OWNOUTLET="";
		  ISSTOCKTAKE_BYAVGCOST="";
          ISSALESTOPURCHASE="";
          ISPOSTAXINCLUSIVE="";
          ISSALESAPP_TAXINCLUSIVE="";
          ISPOSSALESMAN="";
          ALLOWPRDTOCOMPANY="";
          IsDefautDrawerAmount="";
          IsExpJournalEdit="";
          ISREFERENCEINVOICE="";
    }
	else if(action.equalsIgnoreCase("View")){
	    List viewlistQry = plantmstutil.getPlantMstDetails(PLANT);
        for (int i = 0; i < viewlistQry.size(); i++) {
            Map map = (Map) viewlistQry.get(i);
					System.out.println("map : " + map);
                    PLANT     = StrUtils.fString((String)map.get("PLANT"));
                    PLNTDESC     = StrUtils.fString((String)map.get("PLNTDESC"));
                    ISTAXREG     = StrUtils.fString((String)map.get("ISTAXREGISTRED"));
                    COMP_INDUSTRY	= StrUtils.fString((String)map.get("COMP_INDUSTRY"));
                    REPROTSBASIS = StrUtils.fString((String)map.get("REPROTSBASIS"));
                    STARTDATE     = StrUtils.fString((String)map.get("STARTDATE"));
                    EXPIRYDATE   = StrUtils.fString((String)map.get("EXPIRYDATE"));
                    ACTUALEXPIRYDATE=StrUtils.fString((String)map.get("ACTEXPIRYDATE"));
                    NAME=StrUtils.fString((String)map.get("NAME"));
                    DESGINATION     = StrUtils.fString((String)map.get("DESGINATION"));
                    TELNO     = StrUtils.fString((String)map.get("TELNO"));
                    HPNO    = StrUtils.fString((String)map.get("HPNO"));
                    EMAIL    = StrUtils.fString((String)map.get("EMAIL"));
                    ADD1   = StrUtils.fString((String)map.get("ADD1"));
                    ADD2   = StrUtils.fString((String)map.get("ADD2"));
                    ADD3   = StrUtils.fString((String)map.get("ADD3"));
                    ADD4   = StrUtils.fString((String)map.get("ADD4"));
                    STATE   = StrUtils.fString((String)map.get("STATE"));
                    COUNTY   = StrUtils.fString((String)map.get("COUNTY"));
                    companyregnumber   = StrUtils.fString((String)map.get("companyregnumber"));
                    ZIP   = StrUtils.fString((String)map.get("ZIP"));
                    REMARKS   = StrUtils.fString((String)map.get("REMARKS"));
                    FAX   = StrUtils.fString((String)map.get("FAX"));
                    RCBNO = StrUtils.fString((String)map.get("RCBNO"));
                    TAXBY = StrUtils.fString((String)map.get("TAXBY"));
                    TAXBYLABEL = StrUtils.fString((String)map.get("TAXBYLABEL"));
					TAXBYLABELORDERMANAGEMENT = StrUtils.fString((String)map.get("TAXBYLABELORDERMANAGEMENT"));
                    ENABLEINVENTORY = StrUtils.fString((String)map.get("ENABLE_INVENTORY"));
                    ENABLEACCOUNTING = StrUtils.fString((String)map.get("ENABLE_ACCOUNTING"));
                    OWNERAPP = StrUtils.fString((String)map.get("ISACCESSOWNERAPP"));
                    CUSTOMERAPP = StrUtils.fString((String)map.get("ISACCESS_CUSTOMERAPP"));
                    MANAGERAPP = StrUtils.fString((String)map.get("ISACCESSMANGERAPP"));
                    STOREAPP = StrUtils.fString((String)map.get("ISACCESS_STOREAPP"));
                    RIDEAPP = StrUtils.fString((String)map.get("ISRIDERRAPP"));
                    DECIMALPRECISION = StrUtils.fString((String)map.get("NUMBEROFDECIMAL"));
                    IMAGEPATH = StrUtils.fString((String)map.get("IMAGEPATH"));
                    APPPATH = StrUtils.fString((String)map.get("APPPATH"));
                    NOOFCATALOGS= StrUtils.fString((String)map.get("NUMBER_OF_SIGNATURES"));
                    NOOFSIGNATURES= StrUtils.fString((String)map.get("NUMBER_OF_CATALOGS"));
                    currencyCode = StrUtils.fString((String)map.get("BASE_CURRENCY"));
                    WEBSITE   = StrUtils.fString((String)map.get("WEBSITE"));
                    SKYPE = StrUtils.fString((String)map.get("SKYPEID"));
                    FACEBOOK = StrUtils.fString((String)map.get("FACEBOOKID"));
                    TWITTER = StrUtils.fString((String)map.get("TWITTERID"));
                    LINKEDIN = StrUtils.fString((String)map.get("LINKEDINID"));
                    REGION = StrUtils.fString((String)map.get("REGION"));
                    SEALNAME=StrUtils.fString((String)map.get("SEALNAME"));
                    SIGNNAME=StrUtils.fString((String)map.get("SIGNATURENAME"));
                    sCountryCode = StrUtils.fString((String)map.get("COUNTRY_CODE"));
                    FISCALYEAR     = StrUtils.fString((String)map.get("FISCALYEAR"));
                    PAYROLLYEAR   = StrUtils.fString((String)map.get("PAYROLLYEAR"));
                    ENABLEPAYROLL     = StrUtils.fString((String)map.get("ENABLE_PAYROLL"));
                    ENABLEPOS     = StrUtils.fString((String)map.get("ENABLE_POS"));
                    PLANTID     = StrUtils.fString((String)map.get("ID"));
                    EMPLOYEEWORKINGMANDAYSBY= StrUtils.fString((String)map.get("EMPLOYEEWORKINGMANDAYSBY"));
                    NOOFSUPPLIER=StrUtils.fString((String)map.get("NUMBER_OF_SUPPLIER"));
                    NOOFCUSTOMER=StrUtils.fString((String)map.get("NUMBER_OF_CUSTOMER"));
                    NOOFEMPLOYEE=StrUtils.fString((String)map.get("NUMBER_OF_EMPLOYEE"));
                    NOOFUSER=StrUtils.fString((String)map.get("NUMBER_OF_USER"));
                    NOOFINVENTORY=StrUtils.fString((String)map.get("NUMBER_OF_INVENTORY"));
                    NOOFLOCATION=StrUtils.fString((String)map.get("NUMBER_OF_LOCATION"));
                    NOOFORDER=StrUtils.fString((String)map.get("NUMBER_OF_ORDER"));
                    NOOFEXPBILLINV=StrUtils.fString((String)map.get("NUMBER_OF_EBIQI"));
                    NOOFPAYMENT=StrUtils.fString((String)map.get("NUMBER_OF_PAYMENT"));
                    NOOFJOURNAL=StrUtils.fString((String)map.get("NUMBER_OF_JOURNAL"));
                    NOOFCONTRA=StrUtils.fString((String)map.get("NUMBER_OF_CONTRA"));

                    PRODUCT_SHOWBY_CATAGERY=StrUtils.fString((String)map.get("PRODUCT_SHOWBY_CATAGERY"));
                    ISASSIGN_USERLOC=StrUtils.fString((String)map.get("ISASSIGN_USERLOC"));
                    SHOW_STOCKQTY_ONAPP=StrUtils.fString((String)map.get("SHOW_STOCKQTY_ONAPP"));
                    ISSHOWPOSPRICEBYOUTLET=StrUtils.fString((String)map.get("SHOW_STOCKQTY_ONAPP"));
                    ALLOWPOSTRAN_LESSTHAN_AVBQTY=StrUtils.fString((String)map.get("ALLOWPOSTRAN_LESSTHAN_AVBQTY"));
                    ISPOSRETURN_TRAN=StrUtils.fString((String)map.get("ISPOSRETURN_TRAN"));
                    ISPOSVOID_TRAN=StrUtils.fString((String)map.get("ISPOSVOID_TRAN"));
                    SHOW_POS_SUMMARY=StrUtils.fString((String)map.get("SHOW_POS_SUMMARY"));
                    ISMANAGEWORKFLOW=StrUtils.fString((String)map.get("ISMANAGEWORKFLOW"));
                    ALLOWCATCH_ADVANCE_SEARCH=StrUtils.fString((String)map.get("ALLOWCATCH_ADVANCE_SEARCH"));
                    SETCURRENTDATE_ADVANCE_SEARCH=StrUtils.fString((String)map.get("SETCURRENTDATE_ADVANCE_SEARCH"));
                    ISAUTO_CONVERT_ESTPO=StrUtils.fString((String)map.get("ISAUTO_CONVERT_ESTPO"));
                    ISPRODUCTMAXQTY=StrUtils.fString((String)map.get("ISPRODUCTMAXQTY"));
                    ISPOSTAXINCLUSIVE=StrUtils.fString((String)map.get("ISPOSTAXINCLUSIVE"));
                    ISSALESAPP_TAXINCLUSIVE=StrUtils.fString((String)map.get("ISSALESAPP_TAXINCLUSIVE"));
                    ISPOSSALESMAN=StrUtils.fString((String)map.get("ISPOSSALESMAN_BYBILLPRODUCT"));
                    ALLOWPRDTOCOMPANY=StrUtils.fString((String)map.get("ALLOWPRODUCT_TO_OTHERCOMPANY"));
                    ISAUTO_CONVERT_RECEIPTBILL=StrUtils.fString((String)map.get("ISAUTO_CONVERT_RECEIPTBILL"));
                    SETCURRENTDATE_GOODSRECEIPT=StrUtils.fString((String)map.get("SETCURRENTDATE_GOODSRECEIPT"));
                    

                    SETCURRENTDATE_PICKANDISSUE=StrUtils.fString((String)map.get("SETCURRENTDATE_PICKANDISSUE"));
                    SHOWITEM_AVGCOST=StrUtils.fString((String)map.get("SHOWITEM_AVGCOST"));
                    ISAUTO_MINMAX_MULTIESTPO=StrUtils.fString((String)map.get("ISAUTO_MINMAX_MULTIESTPO"));
                    ISAUTO_CONVERT_SOINVOICE=StrUtils.fString((String)map.get("ISAUTO_CONVERT_SOINVOICE"));
                    ENABLE_RESERVEQTY=StrUtils.fString((String)map.get("ENABLE_RESERVEQTY"));
                    ISSUPPLIERMANDATORY=StrUtils.fString((String)map.get("ISSUPPLIERMANDATORY"));
					ISSALESTOPURCHASE=StrUtils.fString((String)map.get("ISSALESTOPURCHASE"));
                    PEPPOL_ID=StrUtils.fString((String)map.get("PEPPOL_ID"));
                    PEPPOL_UKEY=StrUtils.fString((String)map.get("PEPPOL_UKEY"));
                   	shopify=StrUtils.fString((String)map.get("shopify"));
                   	lazada=StrUtils.fString((String)map.get("lazada"));
                   	shopee=StrUtils.fString((String)map.get("shopee"));
                   	amazon=StrUtils.fString((String)map.get("amazon"));
                   	ISPEPPOL=StrUtils.fString((String)map.get("ISPEPPOL"));
                   	IsDefautDrawerAmount=StrUtils.fString((String)map.get("IsDefautDrawerAmount"));
                   	IsExpJournalEdit=StrUtils.fString((String)map.get("IsExpJournalEdit"));
                   	ISREFERENCEINVOICE=StrUtils.fString((String)map.get("ISREFERENCEINVOICE"));
                   	ISEMPLOYEEVALIDATEPO=StrUtils.fString((String)map.get("ISEMPLOYEEVALIDATEPO"));
                   	ISEMPLOYEEVALIDATESO=StrUtils.fString((String)map.get("ISEMPLOYEEVALIDATESO"));
                   	ISAUTO_CONVERT_ISSUETOINVOICE=StrUtils.fString((String)map.get("ISAUTO_CONVERT_ISSUETOINVOICE"));
                   	ISPRICE_UPDATEONLY_IN_OWNOUTLET=StrUtils.fString((String)map.get("ISPRICE_UPDATEONLY_IN_OWNOUTLET"));
                   	ISSTOCKTAKE_BYAVGCOST=StrUtils.fString((String)map.get("ISSTOCKTAKE_BYAVGCOST"));
		}
         sealPath = DbBean.COMPANY_SEAL_PATH + "/" + SEALNAME;
         appPath = DbBean.COMPANY_ORDER_APP_BACKGROUND_PATH + "/" + APPPATH;
         signPath = DbBean.COMPANY_SIGNATURE_PATH + "/" + SIGNNAME;
	}
	else if(action.equalsIgnoreCase("ADD")){
	
		String sPLANT_CODE =StrUtils.fString(request.getParameter("PLANT_CODE"));
		String sPLANT =StrUtils.fString(request.getParameter("PLANT"));
		if(sPLANT_CODE.equalsIgnoreCase(""))
			sPLANT_CODE=sPLANT;
	 Hashtable htCond=new Hashtable();
	 htCond.put("PLANT",sPLANT_CODE);  
	 sPlant=sPLANT_CODE;
	 List listQry = plantmstutil.getPlantMstDetails(sPlant);
	 for (int i =0; i<listQry.size(); i++){
	     Map map = (Map) listQry.get(i);
	   	 existingplnt  = (String) map.get("PLANT");
	     String plntcode = sPLANT_CODE;
	     if(existingplnt.equalsIgnoreCase(plntcode))
	     { 
	       StringBuffer updateQyery=new StringBuffer("set ");
	       updateQyery.append(IDBConstants.PLNTDESC +" = '"+ (String)PLNTDESC + "'");
	       updateQyery.append(","+IDBConstants.ISTAXREG +" = '"+ (String)ISTAXREG + "'");
	       updateQyery.append(","+IDBConstants.COMP_INDUSTRY +" = '"+ (String)COMP_INDUSTRY + "'");
	       updateQyery.append(",REPROTSBASIS = '"+REPROTSBASIS + "'");
	       updateQyery.append(","+ IDBConstants.START_DATE+" = '"+ (String)SDATE  + "'");
	       updateQyery.append("," +IDBConstants.EXPIRY_DATE +" = '"+ (String)EDATE  + "'");
	       updateQyery.append("," +IDBConstants.ACT_EXPIRY_DATE +" = '"+ (String)ACTUALEXPIRYDATE + "'");
	       updateQyery.append("," +IDBConstants.NAME +" = '"+ (String)NAME + "'");
	       updateQyery.append("," +IDBConstants.DESGINATION +" = '"+ (String)DESGINATION+ "'");
	       updateQyery.append("," +IDBConstants.TELNO +" = '"+ (String)TELNO+ "'");
	      
	       updateQyery.append("," +IDBConstants.HPNO +" = '"+ (String)TELNO+ "'");
	       updateQyery.append("," +IDBConstants.EMAIL +" = '"+ (String)EMAIL+ "'");
	       updateQyery.append("," +IDBConstants.ADD1 +" = '"+ (String)ADD1+ "'");
	       updateQyery.append("," +IDBConstants.ADD2 +" = '"+ (String)ADD2+ "'");
	       updateQyery.append("," +IDBConstants.ADD3+" = '"+ (String)ADD3+ "'");
	       updateQyery.append("," +"NUMBER_OF_CATALOGS"+" = '"+ NOOFCATALOGS+ "'");
	       updateQyery.append("," +IDBConstants.RCBNO+" = '"+ (String)RCBNO+ "'");
	       updateQyery.append("," +IDBConstants.ADD4+" = '"+ (String)ADD4+ "'");
	       if(COUNTY.equalsIgnoreCase("Select Country"))
		    	COUNTY="";
	       updateQyery.append("," +IDBConstants.COUNTY+" = '"+ (String)COUNTY+ "'");
	       updateQyery.append("," +IDBConstants.ZIP+" = '"+ (String)ZIP+ "'");
	       updateQyery.append("," +IDBConstants.USERFLD1+" = '"+ (String)REMARKS+ "'");
	       updateQyery.append("," +IDBConstants.USERFLD2+" = '"+ (String)FAX+ "'");           
	       updateQyery.append("," +IDBConstants.TAXBY+" = '"+ (String)TAXBY+ "'");
	       updateQyery.append("," +IDBConstants.TAXBYLABEL+" = '"+ (String)TAXBYLABEL+ "'");
		   updateQyery.append("," +IDBConstants.TAXBYLABELORDERMANAGEMENT+" = '"+ (String)TAXBYLABELORDERMANAGEMENT+ "'");
	       updateQyery.append("," +IDBConstants.BASE_CURRENCY+" = '"+ (String)currencyCode+ "'");
	       updateQyery.append("," +"NUMBER_OF_SIGNATURES"+" = '"+ NOOFSIGNATURES+ "'");
	       if(STATE.equalsIgnoreCase("Select State"))
	        	STATE="";
	       updateQyery.append("," +IDBConstants.STATE+" = '"+ (String)STATE+ "'");
	       updateQyery.append(",ENABLE_INVENTORY=" + ENABLEINVENTORY);
	       updateQyery.append(",ENABLE_ACCOUNTING=" + ENABLEACCOUNTING);
	       updateQyery.append("," +IDBConstants.OWNER_APP+" = "+ OWNERAPP);
	       updateQyery.append("," +IDBConstants.CUSTOMER_APP+" = "+ CUSTOMERAPP);
	       updateQyery.append("," +IDBConstants.MANAGER_APP+" = "+ MANAGERAPP);
	       updateQyery.append("," +IDBConstants.STORE_APP+" = "+ STOREAPP);
	       updateQyery.append("," +IDBConstants.RIDE_APP+" = "+ RIDEAPP);
	       updateQyery.append(",NUMBEROFDECIMAL=" + DECIMALPRECISION);
	       updateQyery.append(",ENABLE_PAYROLL=" + ENABLEPAYROLL);
	       updateQyery.append(",ENABLE_POS=" + ENABLEPOS);
	       updateQyery.append(",FISCAL_YEAR='" + FYEAR+ "'");
	       updateQyery.append(",PAYROLL_YEAR='" + PYEAR+ "'");
	       if(REGION.equalsIgnoreCase("Select Region"))
	        	REGION="";
	       updateQyery.append(",REGION='" +REGION+ "'");	        
	       updateQyery.append("," +IConstants.WEBSITE+" = '"+ (String)WEBSITE+ "'");
	       updateQyery.append("," +IConstants.FACEBOOK+" = '"+ (String)FACEBOOK+ "'");
	       updateQyery.append("," +IConstants.TWITTER+" = '"+ (String)TWITTER+ "'");
	       updateQyery.append("," +IConstants.LINKEDIN+" = '"+ (String)LINKEDIN+ "'");
	       updateQyery.append("," +IConstants.SKYPE+" = '"+ (String)SKYPE+ "'");
	       updateQyery.append(",EMPLOYEEWORKINGMANDAYSBY= '"+ (String)EMPLOYEEWORKINGMANDAYSBY+ "'");
	       
	       updateQyery.append(",companyregnumber= '"+ (String)companyregnumber+ "'");
	       updateQyery.append(",PRODUCT_SHOWBY_CATAGERY= '"+ (String)PRODUCT_SHOWBY_CATAGERY+ "'");
	       updateQyery.append(",ISASSIGN_USERLOC= '"+ (String)ISASSIGN_USERLOC+ "'");
	       updateQyery.append(",SHOW_STOCKQTY_ONAPP= '"+ (String)SHOW_STOCKQTY_ONAPP+ "'");
	       updateQyery.append(",ISSHOWPOSPRICEBYOUTLET= '"+ (String)ISSHOWPOSPRICEBYOUTLET+ "'");
	       updateQyery.append(",ALLOWPOSTRAN_LESSTHAN_AVBQTY= '"+ (String)ALLOWPOSTRAN_LESSTHAN_AVBQTY+ "'");
	       updateQyery.append(",ISPOSRETURN_TRAN= '"+ (String)ISPOSRETURN_TRAN+ "'");
	       updateQyery.append(",ISPOSVOID_TRAN= '"+ (String)ISPOSVOID_TRAN+ "'");
	       updateQyery.append(",SHOW_POS_SUMMARY= '"+ (String)SHOW_POS_SUMMARY+ "'");
	       updateQyery.append(",ISMANAGEWORKFLOW= '"+ (String)ISMANAGEWORKFLOW+ "'");
	       updateQyery.append(",ALLOWCATCH_ADVANCE_SEARCH= '"+ (String)ALLOWCATCH_ADVANCE_SEARCH+ "'");
	       updateQyery.append(",SETCURRENTDATE_ADVANCE_SEARCH= '"+ (String)SETCURRENTDATE_ADVANCE_SEARCH+ "'");
	       updateQyery.append(",ISAUTO_CONVERT_ESTPO= '"+ (String)ISAUTO_CONVERT_ESTPO+ "'");
	       updateQyery.append(",ISPRODUCTMAXQTY= '"+ (String)ISPRODUCTMAXQTY+ "'");
	       updateQyery.append(",ISPOSTAXINCLUSIVE= '"+ (String)ISPOSTAXINCLUSIVE+ "'");
	       updateQyery.append(",ISSALESAPP_TAXINCLUSIVE= '"+ (String)ISSALESAPP_TAXINCLUSIVE+ "'");
	       updateQyery.append(",ISPOSSALESMAN_BYBILLPRODUCT= '"+ (String)ISPOSSALESMAN+ "'");
	       updateQyery.append(",ISAUTO_CONVERT_RECEIPTBILL= '"+ (String)ISAUTO_CONVERT_RECEIPTBILL+ "'");
	       updateQyery.append(",SETCURRENTDATE_GOODSRECEIPT= '"+ (String)SETCURRENTDATE_GOODSRECEIPT+ "'");
	       updateQyery.append(",ISEMPLOYEEVALIDATEPO= '"+ (String)ISEMPLOYEEVALIDATEPO+ "'");
	       updateQyery.append(",ISEMPLOYEEVALIDATESO= '"+ (String)ISEMPLOYEEVALIDATESO+ "'");
	       updateQyery.append(",ISAUTO_CONVERT_ISSUETOINVOICE= '"+ (String)ISAUTO_CONVERT_ISSUETOINVOICE+ "'");
	       
	       updateQyery.append(",SETCURRENTDATE_PICKANDISSUE= '"+ (String)SETCURRENTDATE_PICKANDISSUE+ "'");
	       updateQyery.append(",SHOWITEM_AVGCOST= '"+ (String)SHOWITEM_AVGCOST+ "'");
	       updateQyery.append(",ISAUTO_MINMAX_MULTIESTPO= '"+ (String)ISAUTO_MINMAX_MULTIESTPO+ "'");
	       updateQyery.append(",ISAUTO_CONVERT_SOINVOICE= '"+ (String)ISAUTO_CONVERT_SOINVOICE+ "'");
	       updateQyery.append(",ENABLE_RESERVEQTY= '"+ (String)ENABLE_RESERVEQTY+ "'");
	       updateQyery.append(",ISSUPPLIERMANDATORY= '"+ (String)ISSUPPLIERMANDATORY+ "'");
	       updateQyery.append(",ISPRICE_UPDATEONLY_IN_OWNOUTLET= '"+ (String)ISPRICE_UPDATEONLY_IN_OWNOUTLET+ "'");
	       updateQyery.append(",ISSTOCKTAKE_BYAVGCOST= '"+ (String)ISSTOCKTAKE_BYAVGCOST+ "'");
		   updateQyery.append(",ISSALESTOPURCHASE= '"+ (String)ISSALESTOPURCHASE+ "'");
	       updateQyery.append(",NUMBER_OF_SUPPLIER= '"+ (String)NOOFSUPPLIER+ "'");
	       updateQyery.append(",NUMBER_OF_CUSTOMER= '"+ (String)NOOFCUSTOMER+ "'");
	       updateQyery.append(",NUMBER_OF_EMPLOYEE= '"+ (String)NOOFEMPLOYEE+ "'");
	       updateQyery.append(",NUMBER_OF_USER= '"+ (String)NOOFUSER+ "'");
	       updateQyery.append(",NUMBER_OF_INVENTORY= '"+ (String)NOOFINVENTORY+ "'");
	       updateQyery.append(",NUMBER_OF_LOCATION= '"+ (String)NOOFLOCATION+ "'");
	       updateQyery.append(",NUMBER_OF_ORDER= '"+ (String)NOOFORDER+ "'");
	       updateQyery.append(",NUMBER_OF_EBIQI= '"+ (String)NOOFEXPBILLINV+ "'");
	       updateQyery.append(",NUMBER_OF_PAYMENT= '"+ (String)NOOFPAYMENT+ "'");
	       updateQyery.append(",NUMBER_OF_JOURNAL= '"+ (String)NOOFJOURNAL+ "'");
	       updateQyery.append(",NUMBER_OF_CONTRA= '"+ (String)NOOFCONTRA+ "'");
	       updateQyery.append(",PEPPOL_ID= '"+ (String)PEPPOL_ID+ "'");
	       updateQyery.append(",PEPPOL_UKEY= '"+ (String)PEPPOL_UKEY+ "'");
	       updateQyery.append(",shopify= '"+ ("on".equals(shopify) ? 1 : 0) + "'");
		   updateQyery.append(",lazada= '"+ ("on".equals(lazada) ? 1 : 0) + "'");
		   updateQyery.append(",shopee= '"+ ("on".equals(shopee) ? 1 : 0) + "'");
		   updateQyery.append(",amazon= '"+ ("on".equals(amazon) ? 1 : 0) + "'");
		   updateQyery.append(",ISPEPPOL= '"+ ("on".equals(ISPEPPOL) ? 1 : 0) + "'");
		   updateQyery.append(",ALLOWPRODUCT_TO_OTHERCOMPANY= '"+ (String)ALLOWPRDTOCOMPANY+ "'");
		   updateQyery.append(",IsDefautDrawerAmount= '"+ (String)IsDefautDrawerAmount+ "'");
		   updateQyery.append(",IsExpJournalEdit= '"+ (String)IsExpJournalEdit+ "'");
		   updateQyery.append(",ISREFERENCEINVOICE= '"+ (String)ISREFERENCEINVOICE+ "'");
	       
	       flag=  _PlantMstDAO.update(updateQyery.toString(),htCond,""); 
	       
	       MovHisDAO mdao = new MovHisDAO();
	       Hashtable htm = new Hashtable();
	       htm.put("PLANT","track");
	       htm.put("DIRTYPE","UPD_COMPANY");
	       htm.put("RECID","");
	       htm.put("REMARKS",plntcode+","+REMARKS);
	       htm.put("CRBY",(String)sUserId);
	       htm.put("CRAT",dateutils.getDateTime());
	       htm.put("UPAT",dateutils.getDateTime());
	       htm.put(IDBConstants.TRAN_DATE,dateutils.getDateinyyyy_mm_dd(dateutils.getDate()));      
	
	       boolean   inserted = mdao.insertIntodefaultMovHis(htm);
	
	       if(flag==false)
	       {
	           res="<font class = "+"mainred"+">Company"+ " "+ plntcode +" "+"Details Not Updated  Successfully</font>";
	       }
	       else
	       {
	          res="<font class = "+"maingreen"+">Company "+ " "+ plntcode +" "+"Details  Updated Successfully</font>";
	          PLANT=plntcode;
	       }
	       
	     }
	     else
	     {
	    	 res="<font class = "+"mainred"+">Company "+ " "+ plntcode +" "+"doesn't Exist</font>"; 
	     }
	 
	  }
	
	 }
	
	List AttachList =_PlantMstDAO.getCompAttachByHrdId(PLANT,"");
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
		</div>
		
 <div class="box-body">
 
 <CENTER><strong><%=res%></strong></CENTER>
 
<form class="form-horizontal" name="form1" id="form1" autocomplete="off" method="post">
 <div class="col-sm-6">
<div class="row">
   <div class="form-group">
      <label class="control-label col-form-label col-sm-4 required" for="Company ID">Company ID</label>
      <div class="col-sm-6">
      <div class="input-group">   
      	  	 <INPUT name="PLANT_CODE" type = "hidden" value="" size="30"  MAXLENGTH=10 >
  <input class="form-control" name="PLANT" type = "TEXT" value="<%=PLANT%>" size="30"  MAXLENGTH=10>
  <span class="input-group-addon" onClick="javascript:popWin('PlantMstList.jsp?PLANT='+form1.PLANT.value);">
			<a href="#" data-toggle="tooltip" data-placement="top" title="Company ID Details">
   		 	<i class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span> 
  		</div>
  		
  		<INPUT type="hidden" name="EDIT_COUNTRY" value="<%=sCountryCode%>">
  		<INPUT type="hidden" name="EDIT_STATE" value="<%=STATE%>">
  		<INPUT type="hidden" name="EDIT_REGION" value="<%=REGION%>">
  		<INPUT type="hidden" name="ID" value="<%=PLANTID%>">
      </div>
      <div class=form-inline>
    <div class="col-sm-2">
      	<button type="button" class="Submit btn btn-default" onClick="onView();">View</button>&nbsp;&nbsp;
    </div></div>
    </div>
    
   <div class="form-group">
      <label class="control-label col-form-label col-sm-4 required" for="Company Name">Company Name</label>
      <div class="col-sm-6">
      <div class="input-group">   
      	  	 <input class="form-control" name="PLNTDESC" type = "TEXT" value="<%=PLNTDESC%>" size="30"  MAXLENGTH=100>      	  	  
  		</div>
      </div>
    </div>
    
      <div class="form-group" id="UEN">
     
	      <label class="control-label col-form-label col-sm-4" for="Unique Entity Number (UEN)">Unique Entity Number (UEN)</label>
	      <div class="col-sm-6">
	      <div class="input-group">   
	      	  	 <input class="form-control" name="companyregnumber" type = "TEXT" value="<%=companyregnumber%>" size="30"  MAXLENGTH=100> 
	  		</div>
	  		</div>
	  		</div>
	   
    
    <div class="form-group">
      <label class="control-label col-form-label col-sm-4" for="Start Date">Start Date</label>
      <div class="col-sm-6">
      <div class="input-group">   
      	  	 <input class="form-control datepicker" name="STARTDATE" type = "TEXT" value="<%=STARTDATE%>" size="50"  MAXLENGTH=20 readonly>
  		</div>
      </div>
    </div>
    
    <div class="form-group">
      <label class="control-label col-form-label col-sm-4" for="Expiry Date">Expiry Date</label>
      <div class="col-sm-6">
      <div class="input-group">   
      	  	 <input class="form-control datepicker" name="EXPIRYDATE" type = "TEXT" value="<%=EXPIRYDATE%>"  size="50"  MAXLENGTH=20 readonly> 
  		</div>
      </div>
    </div>
    
    <div class="form-group">
      <label class="control-label col-form-label col-sm-4" for="Actual Expiry Date">Actual Expiry Date</label>
      <div class="col-sm-6">
      <div class="input-group">   
      	  	 <input class="form-control" name="ACTUALEXPIRYDATE" type = "TEXT"  value="<%=ACTUALEXPIRYDATE%>" onfocus="javascript:checkDate();" size="30"    MAXLENGTH=20  readonly> 
  		</div>
      </div>
    </div>
    
    <div class="form-group" style="display:none;">
      <label class="control-label col-form-label col-sm-4" for="Number Of Catalogs">Number Of Catalogs</label>
      <div class="col-sm-6">
      <div class="input-group">   
      	  	 <input class="form-control" name="NOOFCATALOGS" type = "TEXT" value="<%=NOOFCATALOGS%>" size="30"  MAXLENGTH=30 > 
  		</div>
      </div>
    </div>
    
	    <div class="form-group">
	  <label class="control-label col-form-label col-sm-4 required" for="User">Number of User</label>
	  <div class="col-sm-6">
	  <div class="input-group">   
	  	  	 <SELECT class="form-control" data-toggle="dropdown" data-placement="right" name="No_of_User" id="No_of_User"></SELECT> 
	</div>
	  </div>
	</div>
    
    <div class="form-group">
      <label class="control-label col-form-label col-sm-4" for="Number Of Signatures">Number Of Signatures</label>
      <div class="col-sm-6">
      <div class="input-group">   
      	  	 <input class="form-control" name="NOOFSIGNATURES" type = "TEXT" value="<%=NOOFSIGNATURES%>" size="30"  MAXLENGTH=30 > 
  		</div>
      </div>
    </div>
    
    <div class="form-group">
      <label class="control-label col-form-label col-sm-4" for="Base Currency">Base Currency</label>
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
      <label class="control-label col-form-label col-sm-4 required" for="decimal_precision">Number
						of Decimals</label>
      <div class="col-sm-6">
      <div class="input-group">   
      	  	 <SELECT class="form-control" data-toggle="dropdown" data-placement="right"
							name="decimal_precision">
							<option value="2" <%if(DECIMALPRECISION.equals("2")){%> selected <%}%>>2</option>
							<option value="3" <%if(DECIMALPRECISION.equals("3")){%> selected <%}%>>3</option>
							<option value="4" <%if(DECIMALPRECISION.equals("4")){%> selected <%}%>>4</option>
							<option value="5" <%if(DECIMALPRECISION.equals("5")){%> selected <%}%>>5</option>
						</SELECT> 
  		</div>
      </div>
    </div>
    
    <div class="form-group">
	      <label class="control-label col-form-label col-sm-4 required" for="Supplier">Number
							of Supplier</label>
	      <div class="col-sm-8">
	      <div class="input-group">   
	      	  	 <SELECT class="form-control" data-toggle="dropdown" data-placement="right"
								name="No_of_Supplier" id="No_of_Supplier">
							</SELECT> 
	  		</div>
	      </div>
	    </div>
	    
	    <div class="form-group">
	      <label class="control-label col-form-label col-sm-4 required" for="Customer">Number of Customer</label>
	      <div class="col-sm-8">
	      <div class="input-group">   
	      	  	 <SELECT class="form-control" data-toggle="dropdown" data-placement="right"
								name="No_of_Customer" id="No_of_Customer">
								
							</SELECT> 
	  		</div>
	      </div>
	    </div>
	    
	    <div class="form-group">
	      <label class="control-label col-form-label col-sm-4 required" for="Employee">Number
							of Employee</label>
	      <div class="col-sm-8">
	      <div class="input-group">   
	      	  	 <SELECT class="form-control" data-toggle="dropdown" data-placement="right"
								name="No_of_Employee" id="No_of_Employee">
								
							</SELECT> 
	  		</div>
	      </div>
	    </div>
	    
	    <div class="form-group">
	      <label class="control-label col-form-label col-sm-4 required" for="Inventory">Number
							of Inventory</label>
	      <div class="col-sm-8">
	      <div class="input-group">   
	      	  	 <SELECT class="form-control" data-toggle="dropdown" data-placement="right"
								name="No_of_Inventory" id="No_of_Inventory">
							</SELECT> 
	  		</div>
	      </div>
	    </div>
	    
	    <!-- imti start -->	    
	     <div class="form-group">
	      <label class="control-label col-form-label col-sm-4 required" for="Inventory">Number
							of Location</label>
	      <div class="col-sm-8">
	      <div class="input-group">   
	      	  	 <SELECT class="form-control" data-toggle="dropdown" data-placement="right"
								name="No_of_Location" id="No_of_Location">
							</SELECT> 
	  		</div>
	      </div>
	      
    </div>
	   
	    
	     <div class="form-group">
	      <label class="control-label col-form-label col-sm-4 required" for="Inventory">Number
							of Order</label>
	      <div class="col-sm-8">
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
	         <label class="control-label col-form-label col-sm-4 required" for="Inventory">Number of Expenses/Bill/Invoice Quot./Invoices</label>
	      <div class="col-sm-8">
	         <div class="input-group">   
	      	  	 <SELECT class="form-control" data-toggle="dropdown" data-placement="right"
								name="No_of_Exp_Bill_Inv" id="No_of_Exp_Bill_Inv">
							</SELECT> 
	  		  </div>
	      </div>
	      </div>
	      
<!-- 	      Resvi End -->

<!-- Resvi start  -->


      <div class="form-group">
	            <label class="control-label col-form-label col-sm-4 required" for="Inventory">Number
							of Payment</label>
	           <div class="col-sm-8">
	           <div class="input-group">   
	      	      <SELECT class="form-control" data-toggle="dropdown" data-placement="right"
								name="No_of_Payment" id="No_of_Payment">
			      </SELECT> 
	  		  </div>
	          </div>
	    </div>
    


         <div class="form-group">
	            <label class="control-label col-form-label col-sm-4 required" for="Inventory">Number
							of Journal</label>
	           <div class="col-sm-8">
	           <div class="input-group">   
	      	      <SELECT class="form-control" data-toggle="dropdown" data-placement="right"
								name="No_of_Journal" id="No_of_Journal">
			      </SELECT> 
	  		  </div>
	          </div>
	    </div>
    
    
       <div class="form-group">
	      <label class="control-label col-form-label col-sm-4 required" for="Inventory">Number
							of Contra</label>
	      <div class="col-sm-8">
	      <div class="input-group">   
	      	  	 <SELECT class="form-control" data-toggle="dropdown" data-placement="right"
								name="No_of_Contra" id="No_of_Contra">
				 </SELECT> 
	  	  </div>
	      </div>
	  </div>
	    
<!-- 	    Resvi ends -->

    <div class="form-group">
      	<label class="control-label col-form-label col-sm-4" for="Purchase System">Purchase System</label>
      	<div class="col-sm-7">  						 
       						 <input type="checkbox" value="i" name="ENABLE_INVENTORY" id="ENABLE_INVENTORY" <%if(ENABLEINVENTORY.equals("1")){%> checked <%}%>/>Order Management&nbsp;&nbsp; 
       						 <input type="checkbox" value="a" name="ENABLE_ACCOUNTING" id="ENABLE_ACCOUNTING" <%if(ENABLEACCOUNTING.equals("1")){%> checked <%}%>/> Accounting&nbsp;&nbsp;
       						 <input type="checkbox" value="p" name="ENABLE_PAYROLL" id="ENABLE_PAYROLL" <%if(ENABLEPAYROLL.equals("1")){%> checked <%}%>/> Payroll&nbsp;&nbsp;    
       						 <input type="checkbox" value="o" name="ENABLE_POS" id="ENABLE_POS" <%if(ENABLEPOS.equals("1")){%> checked <%}%>/> POS&nbsp;&nbsp;    						 
		</div>
    	</div>
    	
<%--     	<div class="form-group">
      	<label class="control-label col-form-label col-sm-4" for="Access System">Access System</label>
      	<div class="col-sm-7">
       						 <input type="checkbox" value="o" name="OWNER_APP" id="OWNER_APP" <%if(OWNERAPP.equals("1")){%> checked <%}%>/>Owner App&nbsp;&nbsp; 
       						 <input type="checkbox" value="m" name="MANAGER_APP" id="MANAGER_APP" <%if(MANAGERAPP.equals("1")){%> checked <%}%>/> Manager App&nbsp;&nbsp;
       						 <input type="checkbox" value="s" name="STORE_APP" id="STORE_APP" <%if(STOREAPP.equals("1")){%> checked <%}%>/> Store App&nbsp;&nbsp;
       						 <input type="checkbox" value="r" name="RIDE_APP" id="RIDE_APP" <%if(RIDEAPP.equals("1")){%> checked <%}%>/> Rider App
		</div>
    	</div> --%>
    	
    	</div>
    	</div>
    
   <%--  <div class="col-sm-6 text-center">
		<div class="row" style="height:644px">
			<div class="row">
			<div class="col-sm-8">
			<font style="font-size:20px;" id="msg"><%=logores%></font>
				 <INPUT size="50" type="hidden" id="imagetmppath" MAXLENGTH=200 name="imagetmppath" value="<%=IMAGEPATH%>">  
				<img id="item_img" name="CATALOGPATH" 
				class="img-thumbnail img-responsive col-sm-3" 
				src="<%=(new File(imagePath).exists()) ?  "/track/ReadFileServlet/?fileLocation="+imagePath:"images/trackNscan/nouser.png"%>"
				style="width: 70%; padding: 3px;">

			</div>
		</div>
		<div class="row">
			<div class="col-sm-8">
				<div class="form-group">				
					<label>Upload Your Company Logo:</label> <INPUT class="form-control btn-sm"  name="IMAGE_UPLOAD"  type="File" size="20" id ="productImg" MAXLENGTH=100> 
					</div>
					 <div class="form-group">
					<b><INPUT class=" btn btn-sm btn-default" type="BUTTON" value="Remove Image" onClick="image_delete();"></b> 
					<b><INPUT class=" btn btn-sm btn-default" type="BUTTON" value="Upload & Save Image" onClick="image_edit();"></b>
				</div>
			</div>
		</div>
		</div> --%>
		   <div class="col-sm-6 text-center">
			<div class="row" style="height:588px">
			<div class="row">
			<div class="col-sm-12" style="padding-bottom: 5%;">
				<div class="col-sm-12"  style="text-align: left;">
					<%-- <%if(!(new File(imagePath).exists())){%>
						<label>Upload Your Company Logo:</label>
					<%}else{ %> --%>
						<label>Company Logo:</label>
					<%-- <%} %> --%>
				</div>
				<div class="col-sm-5" style="border: 1px dashed #ddd;border-radius: 4px;padding: 10px;">
						<div id="item_logo" <%if(!(new File(imagePath).exists())){%> hidden <%} %>>
							<img id="item_img_logo1" src="/track/ReadFileServlet/?fileLocation=<%=imagePath%>" style="width: 100px; height: 50px"/>
						</div>
						<div  id="item_btn_logo" <%if((new File(imagePath).exists())){%> hidden <%} %>>
							<label for="logofile" style="color: #337ab7;">Upload your logo</label>
							<input accept="image/gif,image/jpeg,image/png,image/bmp" name="IMAGE_UPLOAD"  type="File" size="20" id="logofile" onchange="readURLLogo(this);">
					  	</div>
				</div>
				<div class="col-sm-7">
					<p style="font-size: 10px;text-align: left;">This logo will shows on Purchase, Sales Estimate, Sales order, Invoice, Bill, Payslip etc.</p>
					<div id="logoremove" <%if(!(new File(imagePath).exists())){%> hidden <%} %>   style="text-align: left;">
						<a href="#" onClick="image_delete_new();">Remove Logo</a>
					</div>
				</div>
			</div>
			
			<!-- imthi -->
			<div class="col-sm-12" style="padding-bottom: 5%;">
				<div class="col-sm-12"  style="text-align: left;">
					<%-- <%if(!(new File(imagePath).exists())){%>
						<label>Upload Your Company Logo:</label>
					<%}else{ %> --%>
						<label>Order App Background Image:</label>
					<%-- <%} %> --%>
				</div>
				<div class="col-sm-5" style="border: 1px dashed #ddd;border-radius: 4px;padding: 10px;">
						<div id="app_logo" <%if(APPPATH.equalsIgnoreCase("")){%> hidden <%} %>> 
							<img id="app_img_logo1" src="/track/ReadFileServlet/?fileLocation=<%=appPath%>" style="width: 100px; height: 50px"/>
						</div>
						<div  id="app_btn_logo" <%if(!APPPATH.equalsIgnoreCase("")){%> hidden <%} %>>
							<label for="applogofile" style="color: #337ab7;">Upload Order App Image</label>
							<input accept="image/gif,image/jpeg,image/png,image/bmp" name="APP_IMAGE_UPLOAD"  type="File" size="20" id="applogofile" onchange="readURLAPPBACKGROUND(this);">
					  	</div>
				</div>
				<div class="col-sm-7">
					<p style="font-size: 10px;text-align: left;">This will shows on Order App Background.</p>
					<div id="applogoremove" <%if(APPPATH.equalsIgnoreCase("")){%> hidden <%} %>  style="text-align: left;">
						<a href="#" onClick="app_image_delete_new();">Remove Order App Image</a>
					</div>
				</div>
			</div>
			
			<div class="col-sm-12" style="padding-bottom: 5%;">
				<div class="col-sm-12"  style="text-align: left;">
					<%-- <%if(SEALNAME.equalsIgnoreCase("")){%>
						<label>Upload Your Company Seal:</label>
					<%}else{ %> --%>
						<label>Company Seal:</label>
					<%-- <%} %> --%>
				</div>
				<div class="col-sm-5" style="border: 1px dashed #ddd;border-radius: 4px;padding: 10px;">

						<div id="item_seal" <%if(SEALNAME.equalsIgnoreCase("")){%> hidden <%} %>>
							<img id="item_img_seal1" src="/track/ReadFileServlet/?fileLocation=<%=sealPath%>" style="width: 100px; height: 50px"/>
						</div>
						<div  id="item_btn_seal" <%if(!SEALNAME.equalsIgnoreCase("")){%> hidden <%} %>>
							<label for="sealfile" style="color: #337ab7;">Upload your seal</label>
 							<input accept="image/gif,image/jpeg,image/png,image/bmp" name="seal"  type="file" size="20"  id="sealfile" onchange="readURLSeal(this);"> 
					  	</div>
				</div>
				<div class="col-sm-7">
					<p style="font-size: 10px;text-align: left;">This seal will shows on Payslip</p>
					<div id="sealremove" <%if(SEALNAME.equalsIgnoreCase("")){%> hidden <%} %> style="text-align: left;">
						<a href="#" onClick="seal_delete_new();">Remove Seal</a>
					</div>
				</div>
			</div>
			<div class="col-sm-12" style="padding-bottom: 5%;">
				<div class="col-sm-12"  style="text-align: left;">
					<%-- <%if(SIGNNAME.equalsIgnoreCase("")){%>
						<label>Upload Your Company Signature:</label>
					<%}else{ %> --%>
						<label>Company Signature:</label>
					<%-- <%} %> --%>
				</div>
				<div class="col-sm-5" style="border: 1px dashed #ddd;border-radius: 4px;padding: 10px;">

						<div id="item_sign" <%if(SIGNNAME.equalsIgnoreCase("")){%> hidden <%} %>>
							<img id="item_img_sign1" src="/track/ReadFileServlet/?fileLocation=<%=signPath%>" style="width: 100px; height: 50px"/>
						</div>
						<div  id="item_btn_sign" <%if(!SIGNNAME.equalsIgnoreCase("")){%> hidden <%} %>>
							<label for="signfile" style="color: #337ab7;">Upload your signature</label>
							<input accept="image/gif,image/jpeg,image/png,image/bmp" name="dsignature"  type="File" size="20" id="signfile" onchange="readURLSign(this);">
					  	</div>
				</div>
				<div class="col-sm-7">
					<p style="font-size: 10px;text-align: left;">This signature will shows on Payslip</p>
					<div id="signremove" <%if(SIGNNAME.equalsIgnoreCase("")){%> hidden <%} %> style="text-align: left;">
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
            <a href="#integration" class="nav-link" data-toggle="tab">Integration</a>
        </li>
        <li class="nav-item">
            <a href="#appaccess" class="nav-link" data-toggle="tab">App Access</a>
        </li>
        <li class="nav-item">
            <a href="#CompIndustry" class="nav-link" data-toggle="tab">Company Industry</a>
        </li>
       <!--  <li class="nav-item">
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
       <OPTION selected value="0">Choose : </OPTION>
       <option selected value="BYORDER">BYORDER</option>
	   <option selected value="BYPRODUCT">BYPRODUCT</option>
         <%
			
		   ArrayList taxbyList = _PlantMstDAO.getTaxByList(Plant);
			for(int i=0 ; i<taxbyList.size();i++)
      		 {
				Map m=(Map)taxbyList.get(i);
				strtaxby = (String)m.get("TAXBY");
		       %>
		      <OPTION value="<%=strtaxby%>"  
		      <%if(TAXBY.equalsIgnoreCase(strtaxby)){%> selected <%}%>><%=strtaxby%>
		      </OPTION>
		     <%} %>
	  </select>
  		</div>
      </div>
    </div>
    
    <div class="form-group">
      <label class="control-label col-form-label col-sm-2 required" for="Tax By Label">Tax By Label Order Configuration</label>
      <div class="col-sm-4">
      <div class="input-group">
      <SELECT onchange="getval()" id="select_id" class="form-control" data-toggle="dropdown" data-placement="right" name="TAXBYLABEL">
       <OPTION selected value="0">Choose : </OPTION>
       <option selected value="VAT">VAT</option>
       <option selected value="GST">GST</option>
	   <option selected value="TRN">TRN</option>
	   <option selected value="RCB">RCB</option>
	   <option selected value="TAX">TAX</option>
	   <option selected value="UEN">UEN</option>
	   
	   
	   
	    <%
			
		   ArrayList taxbylabelList = _PlantMstDAO.getTaxByLabelList(Plant);
			for(int i=0 ; i<taxbylabelList.size();i++)
      		 {
				Map m=(Map)taxbylabelList.get(i);
				strtaxbylabe1 = (String)m.get("TAXBYLABEL");
			 %>
		      <OPTION value="<%=strtaxbylabe1%>"  
		      <%if(TAXBYLABEL.equalsIgnoreCase(strtaxbylabe1)){%> selected <%}%>><%=strtaxbylabe1%>
		      </OPTION>
		     <%} %>
		     
      
	  </select> 
  		</div>
      </div>
    </div>
    
    <div class="form-group">
      <label class="control-label col-form-label col-sm-2 required" for="Tax By Label Order Management">Tax By Label Order Management</label>
      <div class="col-sm-4">
      <div class="input-group">
      		 <SELECT class="form-control" data-toggle="dropdown" data-placement="right" name="TAXBYLABELORDERMANAGEMENT" size="1">
       <OPTION selected value="0">Choose : </OPTION>
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
      	  	 <input name="NAME" type="TEXT" value="<%=NAME%>" size="30" MAXLENGTH=100 class="form-control"> 
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
              <input type="checkbox" value="o" name="OWNER_APP" id="OWNER_APP" <%if(OWNERAPP.equals("1")){%> checked <%}%>/>Owner App&nbsp;&nbsp; 
              </label>
              
        	  <TH WIDTH="20%" ALIGN = "LEFT">
              <label class="checkbox-inline"> 
       		  <input type="checkbox" value="m" name="CUSTOMER_APP" id="CUSTOMER_APP" <%if(CUSTOMERAPP.equals("1")){%> checked <%}%>/> Customer App&nbsp;&nbsp;
              </label>
              
        	  <TH WIDTH="20%" ALIGN = "LEFT">
              <label class="checkbox-inline"> 
       		  <input type="checkbox" value="m" name="MANAGER_APP" id="MANAGER_APP" <%if(MANAGERAPP.equals("1")){%> checked <%}%>/> Manager App&nbsp;&nbsp;
              </label>
              
        	  <TH WIDTH="20%" ALIGN = "LEFT">
              <label class="checkbox-inline"> 
    		  <input type="checkbox" value="s" name="STORE_APP" id="STORE_APP" <%if(STOREAPP.equals("1")){%> checked <%}%>/> Store App&nbsp;&nbsp;
              </label>
              
        	  <TH WIDTH="20%" ALIGN = "LEFT">
              <label class="checkbox-inline"> 
       		  <input type="checkbox" value="r" name="RIDE_APP" id="RIDE_APP" <%if(RIDEAPP.equals("1")){%> checked <%}%>/> Delivery App
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
     
	  <option value="General"<%if(COMP_INDUSTRY.equals("General")){%> selected <%}%>>General</option>
  	  <option value="Construction"<%if(COMP_INDUSTRY.equals("Construction")){%> selected <%}%>>Construction</option>
  	  <option value="Retail"<%if(COMP_INDUSTRY.equals("Retail")){%> selected <%}%>>Retail</option>
  	  <option value="Warehouse" <%if(COMP_INDUSTRY.equals("Warehouse")){%> selected <%}%>>Warehouse</option>
  	  <option value="Service" <%if(COMP_INDUSTRY.equals("Service")){%> selected <%}%>>Service</option>
    <option value="Centralised Kitchen" <%if(COMP_INDUSTRY.equals("Centralised Kitchen")){%> selected <%}%>>Centralised Kitchen</option>
    <option value="Education" <%if(COMP_INDUSTRY.equals("Education")){%> selected <%}%>>Education</option>
    <option value="Customs" <%if(COMP_INDUSTRY.equals("Customs")){%> selected <%}%>>Customs</option>
      </select> 
      
      
       
  		</div>
      </div>
      </div>
      
      <div class="form-group">
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
  		</div>
  		
  	  <div class="form-group">
  	 <div class="panel panel-default">
    <div class="panel-heading" style="background: #eaeafa"><strong>Sales Order</strong></div>
    <div class="panel-body">
    <TABLE class="table1" style="font-size:14px;width: 100%;"> 
    <TR>
    
   	 <TH WIDTH="10%" ALIGN = "LEFT">
  		<div class="form-inline">
  		<label class ="checkbox-inline"  >
		<input type = "checkbox" id = "ISAUTO_CONVERT_SOINVOICE" name = "ISAUTO_CONVERT_SOINVOICE" value = "ISAUTO_CONVERT_SOINVOICE"
		<%if(ISAUTO_CONVERT_SOINVOICE.equals("1")) {%>checked <%}%> />Sales Order To Invoice Automatic Conversion</label>
 	 	</div>
 		 <TH WIDTH="20%" ALIGN = "LEFT">
  		<div class="form-inline">
  		<label class ="checkbox-inline"  >
		<input type = "checkbox" id = "ISSALESTOPURCHASE" name = "ISSALESTOPURCHASE" value = "ISSALESTOPURCHASE"
		<%if(ISSALESTOPURCHASE.equals("1")) {%>checked <%}%> />Sales Order To Purchase Order Automatic Conversion(By Pick & Issue)</label>
 	 	</div>
 	 	
 		 <TH WIDTH="20%" ALIGN = "LEFT">
  		<div class="form-inline">
  		<label class ="checkbox-inline"  >
		<input type = "checkbox" id = "ISREFERENCEINVOICE" name = "ISREFERENCEINVOICE" value = "ISREFERENCEINVOICE"
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
    </TABLE>
    </div>
    </div>
    </div>
    
    <div class="form-group">
  	 <div class="panel panel-default">
    <div class="panel-heading" style="background: #eaeafa"><strong>POS</strong></div>
    <div class="panel-body">
    <TABLE class="table1" style="font-size:14px;width: 100%;"> 
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
		<div class="col-sm-12" style="padding-bottom: 0%;text-align: left;">
			<label class ="checkbox-inline">
			<input type = "checkbox" id = "ISSHOWPOSPRICEBYOUTLET" name = "ISSHOWPOSPRICEBYOUTLET" value = "ISSHOWPOSPRICEBYOUTLET"
			<%if(ISSHOWPOSPRICEBYOUTLET.equals("1")) {%>checked <%}%> />Show POS Price By Outlet</label>
		</div>
	</div>
	</TH>
	
	<TH WIDTH="20%" ALIGN = "LEFT">
	<div class="row">
		<div class="col-sm-12" style="padding-bottom: 0%;text-align: left;">
			<label class ="checkbox-inline">
			<input type = "checkbox" id = "ALLOWPOSTRAN_LESSTHAN_AVBQTY" name = "ALLOWPOSTRAN_LESSTHAN_AVBQTY" value = "ALLOWPOSTRAN_LESSTHAN_AVBQTY"
			<%if(ALLOWPOSTRAN_LESSTHAN_AVBQTY.equals("1")) {%>checked <%}%> />Allow POS Transaction Less Than Available Quantity</label>
		</div>
	</div>
	</TH>
     <TH WIDTH="20%" ALIGN = "LEFT">
	<div class="row">
		<div class="col-sm-12" style="padding-bottom: 0%;text-align: left;">
			<label class ="checkbox-inline">
			<input type = "checkbox" id = "IsDefautDrawerAmount" name = "IsDefautDrawerAmount"
			<%if(IsDefautDrawerAmount.equals("1")) {%>checked <%}%> />Set Default Drawer Amount From ERP</label>
		</div>
	</div>
	</TH>
	</TR>
	<TR>
	<TH WIDTH="20%" ALIGN = "LEFT">
	<div class="row">
		<div class="col-sm-12" style="padding-bottom: 0%;text-align: left;">
			<label class ="checkbox-inline">
			<input type = "checkbox" id = "IsExpJournalEdit " name = "IsExpJournalEdit "
			<%if(IsExpJournalEdit .equals("1")) {%>checked <%}%> />Allow Add Journal For Expense</label>
		</div>
	</div>
	</TH>
    </TR>
    
    </TABLE>
    </div>
    </div>
    </div>
    
  		 <% if(PARENT_PLANT != null){%>
  	 	  <div class="form-group">
  	 	  <%}else{ %>
  	 	  <div class="form-group" style="display:none;">
  	 	  <%} %>
  	 <div class="panel panel-default">
    <div class="panel-heading" style="background: #eaeafa"><strong>Product Automatic Creation</strong></div>
    <div class="panel-body">
    <TABLE class="table1" style="font-size:14px;width: 100%;"> 
    <tbody><tr>
 	<th align="LEFT" width="20%">
 <%-- 	<% if(PARENT_PLANT != null){%> --%>
		<div class="form-group comproprice">
  <%-- <%}else{ %>
		<div class="form-group comproprice" style="display:none;">
  <%} %> --%>
      		<!-- <label class="control-label col-form-label col-sm-12" for="POS">Add Product To Other Company</label> -->
      		<div class="col-sm-12" style="padding-bottom: 0%;text-align: left;">
				<label class="radio-inline"><input type="radio" name="ALLOWPRDTOCOMPANY" style="border:0;" value="NONE" id="ALLOWPRDTOCOMPANY" <%if (ALLOWPRDTOCOMPANY.equalsIgnoreCase("0")) {%> checked <%}%>>None</label>
    			<label class="radio-inline"><input type="radio" name="ALLOWPRDTOCOMPANY" style="border:0;" value="PARENT_COMPANY" id="ALLOWPRDTOCOMPANY" <%if (ALLOWPRDTOCOMPANY.equalsIgnoreCase("1")) {%> checked <%}%>>Allow Create And Edit Parent Product In Child Company</label>				
    			<label class="radio-inline"><input type="radio" name="ALLOWPRDTOCOMPANY" style="border:0;" value="CHILD_COMPANY" id="ALLOWPRDTOCOMPANY" <%if (ALLOWPRDTOCOMPANY.equalsIgnoreCase("2")) {%> checked <%}%>>Allow Create And Edit Product In Child Company</label>			
    			<label class="radio-inline"><input type="radio" name="ALLOWPRDTOCOMPANY" style="border:0;" value="BOTH" id="ALLOWPRDTOCOMPANY" <%if (ALLOWPRDTOCOMPANY.equalsIgnoreCase("3")) {%> checked <%}%>>Both</label>			
      		</div>
    	</div>
	</th>
	
	</tbody>
    </TABLE>
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
			<input type = "checkbox" id = "ISMANAGEWORKFLOW" name = "ISMANAGEWORKFLOW" value = "ISMANAGEWORKFLOW"
			<%if(ISMANAGEWORKFLOW.equals("1")) {%>checked <%}%> />Manage Workflow</label>
		</div>
	</div>
	</TH>    
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
			<input type = "checkbox" id = "ALLOWCATCH_ADVANCE_SEARCH" name = "ALLOWCATCH_ADVANCE_SEARCH" value = "ALLOWCATCH_ADVANCE_SEARCH"
			<%if(ALLOWCATCH_ADVANCE_SEARCH.equals("1")) {%>checked <%}%> />Enable/Disable Advance Search Cache</label>
		</div>
	</div>
	</TH>    
			<TH WIDTH="20%" ALIGN = "LEFT">
			<div class="row">
			<div class="col-sm-12" style="padding-bottom: 0%;text-align: left;">
			<label class ="checkbox-inline">
			<input type = "checkbox" id = "SETCURRENTDATE_ADVANCE_SEARCH" name = "SETCURRENTDATE_ADVANCE_SEARCH" value = "SETCURRENTDATE_ADVANCE_SEARCH"
			<%if(SETCURRENTDATE_ADVANCE_SEARCH.equals("1")) {%>checked <%}%> />Show Current Date as Default In Advance Search</label>
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
			<%if(ISPRICE_UPDATEONLY_IN_OWNOUTLET.equals("1")) {%>checked <%}%> />Update List Price only on own Outlet </label>
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
    
    	  
  		
 	 	
  
  <%--  <TH WIDTH="20%" ALIGN = "LEFT">	
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
	</div> 
<tr>


  	 </TABLE> --%>
  	 
  
  	 
    
    </div>
    
    
    
       <div class="tab-pane fade" id="remark">
        <br>
        
        <div class="form-group">
        <div class="col-sm-2">
				<label class="control-label col-form-label">Remarks</label>
			</div>
        <div class="col-sm-6">
							<textarea rows="2" name="REMARKS"
								class="ember-text-area form-control ember-view"  maxlength="1000" placeholder="Max 1000 characters"><%=REMARKS%></textarea>
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
						<input type="file" class="form-control input-attch" id="billAttch" name="file" multiple="true">
						<div class="input-group">
							<svg version="1.1" id="Layer_1" xmlns="http://www.w3.org/2000/svg" x="0" y="0" viewBox="0 0 512 512" xml:space="preserve" class="icon icon-xs align-text-top action-icons input-group-addon" style="height:30px;display:inline-block;color: #c63616;"><path d="M262.4 512c-35.7 0-64.2-10.5-84.9-31.4-24.7-24.9-37.1-63.8-36.8-115.6.2-32.6-.3-248.7-1.4-268.1-.9-15.9 4.8-41.6 23.6-61.5 11.4-12 32-26.3 66-26.3 30.3 0 55.1 15.7 69.8 44.2 10.1 19.7 12.2 38.8 12.4 40.9l.1.9v.9l.5 236.9v.5c-1 19.2-15.9 47.6-53 47.6h-.7c-39.1-.4-53.7-30.4-56-46.2l-.2-1.3V179.6c0-10.2 8.3-18.5 18.5-18.5s18.5 8.3 18.5 18.5v150.5c.4 1.5 1.4 4.4 3.6 7.2s6.2 6.5 16 6.6c9.2.1 12.4-3.2 14.1-6 1.6-2.6 2.2-5.6 2.3-6.3l-.7-234.5c-.4-3-2.4-15.6-8.8-27.6-8.3-15.7-20.2-23.3-36.4-23.3-16.7 0-29.8 5-39.1 14.8-10.7 11.3-14 26.6-13.6 34 1.2 21.6 1.6 244.3 1.4 270.3-.2 41.6 8.5 71.7 26 89.3 13.5 13.6 33.2 20.4 58.7 20.4 17.2 0 31.8-5.9 43.5-17.7 18.9-18.9 30.1-53.4 30-92.2 0-19.6-.1-193.2-.1-250.9 0-10.2 8.3-18.5 18.5-18.5s18.5 8.3 18.5 18.5c0 57.6.1 231.2.1 250.8.1 49.1-14.8 92.3-40.8 118.4-18.6 18.7-42.7 28.6-69.6 28.6z"></path></svg>
							<button type="button" class="btn btn-sm btn-attch">Upload File</button>
						</div>
						
					</div>
				</div>
				<%if(AttachList.size()>0){ %>
						<div id="billAttchNote">
							<small class="text-muted"><div class="attachclass"><a><%=AttachList.size()%> files Attached</a>
									<div class="tooltiptext">
										
										<%for(int i =0; i<AttachList.size(); i++) {   
									  		Map attach=(Map)AttachList.get(i); %>
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
						<div id="billAttchNote">
							<small class="text-muted">  You can upload a maximum of 5 files, 2MB each  </small>
						</div>
						<%} %>
			</div>
		</div>
        
        </div>
       </div> 
       </div>
    </div>
        
    <div class="form-group">        
     <div class="col-sm-offset-4 col-sm-8">
	 <!-- <button type="button" class="Submit btn btn-default" onClick="window.location.href='../home'"><b>Back</b></button>&nbsp;&nbsp;
	 <button type="button" class="Submit btn btn-default" onClick="{backNavigation('settings.jsp','UA');}"><b>Back</b></button>&nbsp;&nbsp; -->
     <button type="button" class="Submit btn btn-default" onClick="return onNew()">Clear</button>&nbsp;&nbsp;
	 <button type="button" class="btn btn-success" data-toggle="modal" data-target="#myModal" onClick="onAdd();">Save</button>&nbsp;&nbsp;
 	</div>
 	</div>
   </form>

<script>
  function defaultSales()
{
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
 
  $(document).ready(function(){
  	var  d = document.getElementById("select_id").value;
      document.getElementById('TaxLabel').innerHTML = d + " No.:";
      

      $('#No_of_Supplier').empty();
      $('#No_of_Supplier').append('<option value="Unlimited"  <%if(NOOFSUPPLIER.equals("Unlimited")){%> selected <%}%> >Unlimited</option>');
      $('#No_of_Supplier').append('<option value="5"  <%if(NOOFSUPPLIER.equals("5")){%> selected <%}%> >5</option>');
      $('#No_of_Supplier').append('<option value="10"  <%if(NOOFSUPPLIER.equals("10")){%> selected <%}%> >10</option>');
      for (var i = 30; i < 1000; i++) {
    	  var nof = "<%=NOOFSUPPLIER%>";
    	  if(i==nof)
  			  $('#No_of_Supplier').append('<option value="' + i + '" selected>' + i + '</option>');
    	  else
    		  $('#No_of_Supplier').append('<option value="' + i + '" >' + i + '</option>');
    	  
    	  if(i==480)
        	  $('#No_of_Supplier').append('<option value="500" <%if(NOOFSUPPLIER.equals("500")){%> selected <%}%> >500</option>');
    	  
  	i += 29;
      }
      $('#No_of_Supplier').append('<option value="1000"  <%if(NOOFSUPPLIER.equals("1000")){%> selected <%}%> >1000</option>');
      
      $('#No_of_Customer').empty();
      $('#No_of_Customer').append('<option value="Unlimited" <%if(NOOFCUSTOMER.equals("Unlimited")){%> selected <%}%> >Unlimited</option>');
      $('#No_of_Customer').append('<option value="5" <%if(NOOFCUSTOMER.equals("5")){%> selected <%}%> >5</option>');
      $('#No_of_Customer').append('<option value="10" <%if(NOOFCUSTOMER.equals("10")){%> selected <%}%> >10</option>');
      for (var i = 30; i < 1000; i++) {
    	  var nof = "<%=NOOFCUSTOMER%>";
    	  if(i==nof)
    		$('#No_of_Customer').append('<option value="' + i + '" selected>' + i + '</option>');
    	  else
  			$('#No_of_Customer').append('<option value="' + i + '">' + i + '</option>');
    	  
    	  if(i==480)
    	  $('#No_of_Customer').append('<option value="500" <%if(NOOFCUSTOMER.equals("500")){%> selected <%}%> >500</option>');
    	  
  	i += 29;
      }
      $('#No_of_Customer').append('<option value="1000" <%if(NOOFCUSTOMER.equals("1000")){%> selected <%}%>>1000</option>');
      
      $('#No_of_Employee').empty();
      $('#No_of_Employee').append('<option value="Unlimited" <%if(NOOFEMPLOYEE.equals("Unlimited")){%> selected <%}%> >Unlimited</option>');
      $('#No_of_Employee').append('<option value="5" <%if(NOOFEMPLOYEE.equals("5")){%> selected <%}%> >5</option>');
      $('#No_of_Employee').append('<option value="10" <%if(NOOFEMPLOYEE.equals("10")){%> selected <%}%> >10</option>');
      for (var i = 25; i < 1000; i++) {
    	  var nof = "<%=NOOFEMPLOYEE%>";
    	  if(i==nof)
    		  $('#No_of_Employee').append('<option value="' + i + '" selected>' + i + '</option>');		  
    	 else
  			  $('#No_of_Employee').append('<option value="' + i + '">' + i + '</option>');
  	i += 24;
      }
      $('#No_of_Employee').append('<option value="1000" <%if(NOOFEMPLOYEE.equals("1000")){%> selected <%}%>>1000</option>');
      
      $('#No_of_User').empty();
      $('#No_of_User').append('<option value="Unlimited" <%if(NOOFUSER.equals("Unlimited")){%> selected <%}%> >Unlimited</option>');
      $('#No_of_User').append('<option value="5" <%if(NOOFUSER.equals("5")){%> selected <%}%> >5</option>');
      $('#No_of_User').append('<option value="10" <%if(NOOFUSER.equals("10")){%> selected <%}%> >10</option>');
      $('#No_of_User').append('<option value="15" <%if(NOOFUSER.equals("15")){%> selected <%}%> >15</option>');
      $('#No_of_User').append('<option value="20" <%if(NOOFUSER.equals("20")){%> selected <%}%> >20</option>');
      $('#No_of_User').append('<option value="25" <%if(NOOFUSER.equals("25")){%> selected <%}%> >25</option>');
      $('#No_of_User').append('<option value="30" <%if(NOOFUSER.equals("30")){%> selected <%}%> >30</option>');
      $('#No_of_User').append('<option value="35" <%if(NOOFUSER.equals("35")){%> selected <%}%> >35</option>');
      $('#No_of_User').append('<option value="40" <%if(NOOFUSER.equals("40")){%> selected <%}%> >40</option>');
      $('#No_of_User').append('<option value="45" <%if(NOOFUSER.equals("45")){%> selected <%}%> >45</option>');
      $('#No_of_User').append('<option value="50" <%if(NOOFUSER.equals("50")){%> selected <%}%> >50</option>');
//imti start
 $('#No_of_Location').empty();
      $('#No_of_Location').append('<option value="Unlimited" <%if(NOOFLOCATION.equals("Unlimited")){%> selected <%}%> >Unlimited</option>');
      for (var i = 1; i < 100; i++) {
    	  var nof = "<%=NOOFLOCATION%>";
    	  if(i==nof)
    		  $('#No_of_Location').append('<option value="' + i + '" selected>' + i + '</option>');		  
    	 else
  			  $('#No_of_Location').append('<option value="' + i + '">' + i + '</option>');
  	//i += 9;
      }
      for (var i = 100; i < 550; i++) {
    	  var nof = "<%=NOOFLOCATION%>";
    	  if(i==nof)
    		  $('#No_of_Location').append('<option value="' + i + '" selected>' + i + '</option>');		  
    	 else
  			  $('#No_of_Location').append('<option value="' + i + '">' + i + '</option>');
  	i += 49;
      }

      $('#No_of_Order').empty();
      $('#No_of_Order').append('<option value="Unlimited" <%if(NOOFORDER.equals("Unlimited")){%> selected <%}%> >Unlimited</option>');
      $('#No_of_Order').append('<option value="5" <%if(NOOFORDER.equals("5")){%> selected <%}%> >5</option>');
      $('#No_of_Order').append('<option value="10" <%if(NOOFORDER.equals("10")){%> selected <%}%> >10</option>');
      $('#No_of_Order').append('<option value="20" <%if(NOOFORDER.equals("20")){%> selected <%}%> >20</option>');
      $('#No_of_Order').append('<option value="50" <%if(NOOFORDER.equals("50")){%> selected <%}%> >50</option>');
      $('#No_of_Order').append('<option value="100" <%if(NOOFORDER.equals("100")){%> selected <%}%> >100</option>');
      $('#No_of_Order').append('<option value="200" <%if(NOOFORDER.equals("200")){%> selected <%}%> >200</option>');
      $('#No_of_Order').append('<option value="500" <%if(NOOFORDER.equals("500")){%> selected <%}%> >500</option>');
      $('#No_of_Order').append('<option value="1000" <%if(NOOFORDER.equals("1000")){%> selected <%}%> >1000</option>');
      $('#No_of_Order').append('<option value="2000" <%if(NOOFORDER.equals("2000")){%> selected <%}%> >2000</option>');
      for (var i = 5000; i < 105000; i++) {
    	  var nof = "<%=NOOFORDER%>";
    	  if(i==nof)
    		  $('#No_of_Order').append('<option value="' + i + '" selected>' + i + '</option>');		  
    	 else
  			  $('#No_of_Order').append('<option value="' + i + '">' + i + '</option>');
  	i += 4999;
      }
//imti end

// RESVI STARTS


      $('#No_of_Exp_Bill_Inv').empty();
      $('#No_of_Exp_Bill_Inv').append('<option value="Unlimited" <%if(NOOFEXPBILLINV.equals("Unlimited")){%> selected <%}%> >Unlimited</option>');
      $('#No_of_Exp_Bill_Inv').append('<option value="10" <%if(NOOFEXPBILLINV.equals("10")){%> selected <%}%> >10</option>');
      $('#No_of_Exp_Bill_Inv').append('<option value="20" <%if(NOOFEXPBILLINV.equals("20")){%> selected <%}%> >20</option>');
      $('#No_of_Exp_Bill_Inv').append('<option value="30" <%if(NOOFEXPBILLINV.equals("30")){%> selected <%}%> >30</option>');
      $('#No_of_Exp_Bill_Inv').append('<option value="40" <%if(NOOFEXPBILLINV.equals("40")){%> selected <%}%> >40</option>');
      $('#No_of_Exp_Bill_Inv').append('<option value="50" <%if(NOOFEXPBILLINV.equals("50")){%> selected <%}%> >50</option>');
      $('#No_of_Exp_Bill_Inv').append('<option value="60" <%if(NOOFEXPBILLINV.equals("60")){%> selected <%}%> >60</option>');
      $('#No_of_Exp_Bill_Inv').append('<option value="70" <%if(NOOFEXPBILLINV.equals("70")){%> selected <%}%> >70</option>');
      $('#No_of_Exp_Bill_Inv').append('<option value="80" <%if(NOOFEXPBILLINV.equals("80")){%> selected <%}%> >80</option>');
      $('#No_of_Exp_Bill_Inv').append('<option value="90" <%if(NOOFEXPBILLINV.equals("90")){%> selected <%}%> >90</option>');
      $('#No_of_Exp_Bill_Inv').append('<option value="100" <%if(NOOFEXPBILLINV.equals("100")){%> selected <%}%> >100</option>');
      $('#No_of_Exp_Bill_Inv').append('<option value="200" <%if(NOOFEXPBILLINV.equals("200")){%> selected <%}%> >200</option>');
      $('#No_of_Exp_Bill_Inv').append('<option value="300" <%if(NOOFEXPBILLINV.equals("300")){%> selected <%}%> >300</option>');
      $('#No_of_Exp_Bill_Inv').append('<option value="400" <%if(NOOFEXPBILLINV.equals("400")){%> selected <%}%> >400</option>');
      $('#No_of_Exp_Bill_Inv').append('<option value="500" <%if(NOOFEXPBILLINV.equals("500")){%> selected <%}%> >500</option>');
      $('#No_of_Exp_Bill_Inv').append('<option value="600" <%if(NOOFEXPBILLINV.equals("600")){%> selected <%}%> >600</option>');
      $('#No_of_Exp_Bill_Inv').append('<option value="700" <%if(NOOFEXPBILLINV.equals("700")){%> selected <%}%> >700</option>');
      $('#No_of_Exp_Bill_Inv').append('<option value="800" <%if(NOOFEXPBILLINV.equals("800")){%> selected <%}%> >800</option>');
      $('#No_of_Exp_Bill_Inv').append('<option value="900" <%if(NOOFEXPBILLINV.equals("900")){%> selected <%}%> >900</option>');
      $('#No_of_Exp_Bill_Inv').append('<option value="1000" <%if(NOOFEXPBILLINV.equals("1000")){%> selected <%}%> >1000</option>');
      $('#No_of_Exp_Bill_Inv').append('<option value="2000" <%if(NOOFEXPBILLINV.equals("2000")){%> selected <%}%> >2000</option>');
      $('#No_of_Exp_Bill_Inv').append('<option value="3000" <%if(NOOFEXPBILLINV.equals("3000")){%> selected <%}%> >3000</option>');
      $('#No_of_Exp_Bill_Inv').append('<option value="4000" <%if(NOOFEXPBILLINV.equals("4000")){%> selected <%}%> >4000</option>');
      $('#No_of_Exp_Bill_Inv').append('<option value="5000" <%if(NOOFEXPBILLINV.equals("5000")){%> selected <%}%> >5000</option>');
      $('#No_of_Exp_Bill_Inv').append('<option value="6000" <%if(NOOFEXPBILLINV.equals("6000")){%> selected <%}%> >6000</option>');
      $('#No_of_Exp_Bill_Inv').append('<option value="7000" <%if(NOOFEXPBILLINV.equals("7000")){%> selected <%}%> >7000</option>');
      $('#No_of_Exp_Bill_Inv').append('<option value="8000" <%if(NOOFEXPBILLINV.equals("8000")){%> selected <%}%> >8000</option>');
      $('#No_of_Exp_Bill_Inv').append('<option value="9000" <%if(NOOFEXPBILLINV.equals("9000")){%> selected <%}%> >9000</option>');

      for (var i = 10000; i < 35000; i++) {
          
    	  var nof = "<%=NOOFEXPBILLINV%>";
    	  if(i==nof)
    		  $('#No_of_Exp_Bill_Inv').append('<option value="' + i + '" selected>' + i + '</option>');		  
    	 else
  			  $('#No_of_Exp_Bill_Inv').append('<option value="' + i + '">' + i + '</option>');
			  
  	i += 4999;
      }
      $('#No_of_Exp_Bill_Inv').append('<option value="40000" <%if(NOOFEXPBILLINV.equals("40000")){%> selected <%}%> >40000</option>');
      $('#No_of_Exp_Bill_Inv').append('<option value="50000" <%if(NOOFEXPBILLINV.equals("50000")){%> selected <%}%> >50000</option>');
         
      $('#No_of_Payment').empty();
          $('#No_of_Payment').append('<option value="Unlimited" <%if(NOOFPAYMENT.equals("Unlimited")){%> selected <%}%> >Unlimited</option>');
          $('#No_of_Payment').append('<option value="5" <%if(NOOFPAYMENT.equals("5")){%> selected <%}%> >5</option>');
          $('#No_of_Payment').append('<option value="10" <%if(NOOFPAYMENT.equals("10")){%> selected <%}%> >10</option>');
          $('#No_of_Payment').append('<option value="50" <%if(NOOFPAYMENT.equals("50")){%> selected <%}%> >50</option>');
          $('#No_of_Payment').append('<option value="100" <%if(NOOFPAYMENT.equals("100")){%> selected <%}%> >100</option>');
          $('#No_of_Payment').append('<option value="200" <%if(NOOFPAYMENT.equals("200")){%> selected <%}%> >200</option>');
          $('#No_of_Payment').append('<option value="250" <%if(NOOFPAYMENT.equals("250")){%> selected <%}%> >250</option>');
          $('#No_of_Payment').append('<option value="300" <%if(NOOFPAYMENT.equals("300")){%> selected <%}%> >300</option>');
          $('#No_of_Payment').append('<option value="400" <%if(NOOFPAYMENT.equals("400")){%> selected <%}%> >400</option>');
          $('#No_of_Payment').append('<option value="500" <%if(NOOFPAYMENT.equals("500")){%> selected <%}%> >500</option>');
          $('#No_of_Payment').append('<option value="600" <%if(NOOFPAYMENT.equals("600")){%> selected <%}%> >600</option>');
          $('#No_of_Payment').append('<option value="700" <%if(NOOFPAYMENT.equals("700")){%> selected <%}%> >700</option>');
          $('#No_of_Payment').append('<option value="750" <%if(NOOFPAYMENT.equals("750")){%> selected <%}%> >750</option>');
          $('#No_of_Payment').append('<option value="800" <%if(NOOFPAYMENT.equals("800")){%> selected <%}%> >800</option>');
          $('#No_of_Payment').append('<option value="900" <%if(NOOFPAYMENT.equals("900")){%> selected <%}%> >900</option>');
          for (var i = 1000; i < 11000; i++) {
    	  var nof = "<%=NOOFPAYMENT%>";
    	  if(i==nof)
    		  $('#No_of_Payment').append('<option value="' + i + '" selected>' + i + '</option>');		  
    	  else
  			  $('#No_of_Payment').append('<option value="' + i + '">' + i + '</option>');
  	          i += 999;
              }

      
         $('#No_of_Journal').empty();
            $('#No_of_Journal').append('<option value="Unlimited"  <%if(NOOFJOURNAL.equals("Unlimited")){%> selected <%}%> >Unlimited</option>');
            $('#No_of_Journal').append('<option value="5" <%if(NOOFJOURNAL.equals("5")){%> selected <%}%> >5</option>');
            $('#No_of_Journal').append('<option value="10" <%if(NOOFJOURNAL.equals("10")){%> selected <%}%> >10</option>');
            $('#No_of_Journal').append('<option value="100" <%if(NOOFJOURNAL.equals("100")){%> selected <%}%> >100</option>');
            $('#No_of_Journal').append('<option value="1000" <%if(NOOFJOURNAL.equals("1000")){%> selected <%}%> >1000</option>');
            for (var i = 10000; i < 110000; i++) {
    	  var nof = "<%=NOOFJOURNAL%>";
    	  if(i==nof)
    		  $('#No_of_Journal').append('<option value="' + i + '" selected>' + i + '</option>');		  
    	 else
  			  $('#No_of_Journal').append('<option value="' + i + '">' + i + '</option>');
  	i += 9999;
      }


            $('#No_of_Contra').empty();
               $('#No_of_Contra').append('<option value="Unlimited"  <%if(NOOFCONTRA.equals("Unlimited")){%> selected <%}%> >Unlimited</option>');
               $('#No_of_Contra').append('<option value="5" <%if(NOOFCONTRA.equals("5")){%> selected <%}%> >5</option>');
               $('#No_of_Contra').append('<option value="10" <%if(NOOFCONTRA.equals("10")){%> selected <%}%> >10</option>');
               $('#No_of_Contra').append('<option value="100" <%if(NOOFCONTRA.equals("100")){%> selected <%}%> >100</option>');
               $('#No_of_Contra').append('<option value="1000" <%if(NOOFCONTRA.equals("1000")){%> selected <%}%> >1000</option>');
               for (var i = 10000; i < 110000; i++) {

    	  var nof = "<%=NOOFCONTRA%>";
    	  if(i==nof)
    		  $('#No_of_Contra').append('<option value="' + i + '" selected>' + i + '</option>');		  
    	 else
  			  $('#No_of_Contra').append('<option value="' + i + '">' + i + '</option>');
  	i += 9999;
      }

//             Resvi ends
     
      

      $('#No_of_Inventory').empty(); 
      $('#No_of_Inventory').append('<option value="50000" <%if(NOOFINVENTORY.equals("50000")){%> selected <%}%> >50000</option>');  	
      $('#No_of_Inventory').append('<option value="5" <%if(NOOFINVENTORY.equals("5")){%> selected <%}%> >5</option>');
      $('#No_of_Inventory').append('<option value="10" <%if(NOOFINVENTORY.equals("10")){%> selected <%}%> >10</option>');
   
      for (var i = 500; i < 15500; i++) {
    	  {
    	  var nof = "<%=NOOFINVENTORY%>";
    	  if(i==nof)
    		  $('#No_of_Inventory').append('<option value="' + i + '" selected>' + i + '</option>');  
    	 else
  			  $('#No_of_Inventory').append('<option value="' + i + '">' + i + '</option>');
    	  }
  	i += 499;
      }
      
      $('#No_of_Inventory').append('<option value="20000" <%if(NOOFINVENTORY.equals("20000")){%> selected <%}%>>20000</option>');
      $('#No_of_Inventory').append('<option value="25000" <%if(NOOFINVENTORY.equals("25000")){%> selected <%}%>>25000</option>');
      $('#No_of_Inventory').append('<option value="30000" <%if(NOOFINVENTORY.equals("30000")){%> selected <%}%>>30000</option>');
      $('#No_of_Inventory').append('<option value="35000" <%if(NOOFINVENTORY.equals("35000")){%> selected <%}%>>35000</option>');
      $('#No_of_Inventory').append('<option value="40000" <%if(NOOFINVENTORY.equals("40000")){%> selected <%}%>>40000</option>');
      $('#No_of_Inventory').append('<option value="45000" <%if(NOOFINVENTORY.equals("45000")){%> selected <%}%>>45000</option>');
      
      
      if(document.form1.EDIT_REGION.value!="")
  	{
      	$("select[name ='REGION']").val(document.form1.EDIT_REGION.value);
      	OnRegion(document.form1.EDIT_REGION.value);
      if(document.form1.EDIT_COUNTRY.value!="")
  	{	
      	$("select[name ='COUNTRY_CODE']").val(document.form1.EDIT_COUNTRY.value);
      	OnCountry(document.form1.EDIT_COUNTRY.value);		
  	if(document.form1.EDIT_STATE.value!="")
  		{
  		   $("select[name ='STATE']").val(document.form1.EDIT_STATE.value);
  		   document.getElementById("STATE").value = document.form1.EDIT_STATE.value;
  		}
  	}
  	}
      
      $("#billAttch").change(function(){
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
      				$("#billAttchNote").html("<small class='text-muted'>  You can upload a maximum of 5 files, 2MB each  </small>");
      			}else{
      				$("#billAttchNote").html(files +" files attached");
      			}
      			
      		}
      	});
  }
  );
  
  
	function readURL(input) {
	    if (input.files && input.files[0]) {
	        var reader = new FileReader();

	        reader.onload = function (e) {
	        	
	        	
	            $('#item_img').attr('src', e.target.result);
	        }

	        reader.readAsDataURL(input.files[0]);
	    }
	}
	$(document).on('change', '#productImg', function (e) {
	    readURL(this);
	});
	
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
  			PLANT : "<%=Plant%>",
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
  					 if(document.form1.EDIT_COUNTRY.value==value.COUNTRY_CODE)
						 	$('#COUNTRY_CODE').append('<option selected value="' + value.COUNTRY_CODE + '">' + value.COUNTRYNAME + '</option>');
						 else
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
  			PLANT : "<%=Plant%>",
  			COUNTRY : Country,
  		},
  		success : function(dataitem) {
  			var StateList=dataitem.STATEMST;
  			var myJSON = JSON.stringify(StateList);
  			//alert(myJSON);
  			$('#STATE').empty();
  			$('#STATE').append('<OPTION style="display:none;">Select State</OPTION>');
  				 $.each(StateList, function (key, value) {
  					 if(document.form1.EDIT_STATE.value==value.text)
						 	$('#STATE').append('<option selected value="' + value.text + '">' + value.text + '</option>');
						 else					 
					   		$('#STATE').append('<option value="' + value.text + '">' + value.text + '</option>');
  					});
  		}
  	});	
  	
  }
  $('select[name="REGION"]').on('change', function(){
      $("#REGION option").removeAttr('selected');
      document.form1.EDIT_REGION.value="";
      });
  $('select[name="COUNTRY_CODE"]').on('change', function(){
      var text = $("#COUNTRY_CODE option:selected").text();
      $("input[name ='COUNTY']").val(text.trim());
      document.form1.EDIT_STATE.value="";
  });
  $('select[name="STATE"]').on('change', function(){
  $("#STATE option").removeAttr('selected');
  document.form1.EDIT_STATE.value="";
  });
  
  function downloadFile(id,fileName)
  {
  	 var urlStrAttach = "/track/MasterServlet?action=downloadCompAttachmentById&attachid="+id;
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
  	var urlStrAttach = "/track/MasterServlet?action=removeCompAttachmentById&removeid="+id;	
  	$.ajax( {
  		type : "POST",
  		url : urlStrAttach,
  		success : function(data) {
  					//window.location.reload();
  				document.form1.action  = "customer_maintPlant.jsp";
  			   	document.form1.submit();
  				}
  			});
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
          app_edit_new();
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
          seal_edit_new();
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
          sign_edit_new();
      }
  }

  function image_delete_new(){

      //var formData = new FormData($('form')[0]);
      var formData = $('form').serialize();
      var userId= form1.PLANT.value;
  	if(userId){
      $.ajax({
          type: 'post',
          url: "/track/CatalogServlet?Submit=logo_img_delete&Type=Logo",
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
  		alert("Please enter Company Id");
  	}
          return false;
    }

  function app_image_delete_new(){

      //var formData = new FormData($('form')[0]);
      var formData = $('form').serialize();
      var userId= form1.PLANT.value;
  	if(userId){
      $.ajax({
          type: 'post',
          url: "/track/CatalogServlet?Submit=app_img_delete&Type=App",
         	dataType:'html',
      data:  formData,//{key:val}

          success: function (data) {
          	console.log(data)
          	$('#applogofile').val("");
          	$('#app_logo').hide();
              $('#app_btn_logo').show();
              $('#applogoremove').hide();
          },
          error: function (data) {

              alert(data.responseText);
          }
      });
  	}else{
  		alert("Please enter Product Id");
  	}
          return false;
    }

  function seal_delete_new(){

      //var formData = new FormData($('form')[0]);
      var formData = $('form').serialize();
      var userId= form1.PLANT.value;
  	if(userId){
      $.ajax({
          type: 'post',
          url: "/track/CatalogServlet?Submit=seal_img_delete&Type=Seal",
         	dataType:'html',
      data:  formData,//{key:val}

          success: function (data) {
          	console.log(data)
          	$('#sealfile').val("");
          	$('#item_seal').hide();
              $('#item_btn_seal').show();
              $('#sealremove').hide();

          },
          error: function (data) {

              alert(data.responseText);
          }
      });
  	}else{
  		alert("Please enter Company Id");
  	}
          return false;
    }

  function sign_delete_new(){

      var formData = $('form').serialize();
      var userId= form1.PLANT.value;
  	if(userId){
      $.ajax({
          type: 'post',
          url: "/track/CatalogServlet?Submit=sign_img_delete&Type=Sign",
         	dataType:'html',
      data:  formData,//{key:val}

          success: function (data) {
          	console.log(data)
          	$('#signfile').val("");
          	$('#item_sign').hide();
              $('#item_btn_sign').show();
              $('#signremove').hide();
          },
          error: function (data) {

              alert(data.responseText);
          }
      });
  	}else{
  		alert("Please enter Company Id");
  	}
          return false;
    }


  function image_edit_new(){
  	$('#userImage').val('');
  	var myForm = document.getElementById('form1');
  	var formData = new FormData(myForm);
      //var formData = new FormData($('form')[0]);
      var userId= form1.PLANT.value;
  	if(userId){
      $.ajax({
          type: 'post',
          url: "/track/CatalogServlet?Submit=logo_img_edit&Type=Logo",
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
  		alert("Please enter Company Id");
  		return false;
  	}

    }

  function app_edit_new(){
  	$('#userImage').val('');
  	var splant   = document.form1.PLANT.value;
  	var myForm = document.getElementById('form1');
  	var formData = new FormData(myForm);
      //var formData = new FormData($('form')[0]);
      var userId= form1.PLANT.value;
  	if(userId){
      $.ajax({
          type: 'post',
          url: "/track/CatalogServlet?Submit=app_img_edit&Type=appImg&SPlant="+splant,
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
		alert("Please enter Product Id");
		return false;
	}

}

  function seal_edit_new(){
  	$('#userImage').val('');
  	var splant   = document.form1.PLANT.value;
  	var myForm = document.getElementById('form1');
  	var formData = new FormData(myForm);
      //var formData = new FormData($('form')[0]);
      var userId= form1.PLANT.value;
  	if(userId){
      $.ajax({
          type: 'post',
          url: "/track/CatalogServlet?Submit=seal_img_edit&Type=Seal&SPlant="+splant,
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
		alert("Please enter Company Id");
		return false;
	}

}


  function sign_edit_new(){
  		$('#userImage').val('');
  		var splant   = document.form1.PLANT.value;
  		var myForm = document.getElementById('form1');
  		var formData = new FormData(myForm);
  	    //var formData = new FormData($('form')[0]);
  	    var userId= form1.PLANT.value;
  		if(userId){
  	    $.ajax({
  	        type: 'post',
  	        url: "/track/CatalogServlet?Submit=sign_img_edit&Type=Sign",
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
  		alert("Please enter Company Id");
  		return false;
  	}
    }
  </script>

</div>
</div> 
</div>	

<jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
</jsp:include>
