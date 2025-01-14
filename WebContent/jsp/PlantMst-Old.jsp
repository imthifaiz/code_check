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
   var zip = document.form1.ZIP.value;
   var noofcatlogs = document.form1.NOOFCATALOGS.value;
   var noofsignatures = document.form1.NOOFSIGNATURES.value;
   var base_currency = document.form1.BaseCurrency.value;
   var taxby = document.form1.TAXBY.value;
   var taxbylabel = document.form1.TAXBYLABEL.value;
   var state = document.form1.STATE.value;
  
   	
   var frdt = new Date(frarray[2],frarray[1],frarray[0]);
   var todt = new Date(toarray[2],toarray[1],toarray[0]);
   //if(ITEM_ID == "" || ITEM_ID == null) {alert("Please Enter Company ");document.form1.PLANT.focus(); return false; }
   if(desc == "" || desc == null) {alert("Please Enter Company Name");document.form1.PLNTDESC.focus(); return false; }
   //else if(rcbno == "" || rcbno == null) {alert("Please Enter RCB No");document.form1.RCBNO.focus(); return false; }
   else if(STRATDT == "" || STRATDT == null) {alert("Please Select Start Date");document.form1.STARTDATE.focus(); return false; }   
   else if(ENDDT == "" || ENDDT == null) {alert("Please Select Expiry Date");document.form1.EXPIRYDATE.focus(); return false; } 
   else if(todt <frdt) {alert("Please Select Expiry Date greater than Start Date");return false; } 
   else if(cntperson == "" || cntperson == null) {alert("Please Enter Contact Person");document.form1.NAME.focus(); return false; }
   else if(telno == "" || telno == null) {alert("Please Enter Telephone No");document.form1.TELNO.focus(); return false; }
   else if(add1 == "" || add1 == null) {alert("Please Enter Unit No");document.form1.ADD1.focus(); return false; }
   else if(add3 == "" || add3 == null) {alert("Please Enter Street");document.form1.ADD3.focus(); return false; }
   else if(cnty == "" || cnty == null) {alert("Please Enter Country");document.form1.COUNTY.focus(); return false; }
   else if(zip == "" || zip == null) {alert("Please Enter Postal Code");document.form1.ZIP.focus(); return false; }
   else if(noofcatlogs == "" || noofcatlogs == null) {alert("Please Enter Number of Catalogs");document.form1.NOOFCATALOGS.focus(); return false; }
   else if(noofcatlogs ==0) {alert("Enter Valid Number Of Catalogs");document.form1.NOOFCATALOGS.focus(); return false; }
   else if(noofsignatures == "" || noofsignatures == null) {alert("Please Enter Number of Signatures");document.form1.NOOFSIGNATURES.focus(); return false; }
   else if(noofsignatures ==0) {alert("Enter Valid Number Of Signatures");document.form1.NOOFSIGNATURES.focus(); return false; }
   else if(base_currency ==0){alert("Please select the base currency");document.form1.BaseCurrency.focus();return false;}
   else if(taxby ==0){alert("Please select the tax by");document.form1.TAXBY.focus();return false;}
   else if(taxbylabel ==0){alert("Please select the tax by label");document.form1.TAXBYLABEL.focus();return false;}
  

   
  else{
   document.form1.action  = "PlantMst.jsp?action=ADD";
   document.form1.submit();}
   
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
	
	String PLANT="",COMPANY="",PLNTDESC="",STARTDATE="",EXPIRYDATE="",ACTUALEXPIRYDATE="",SDATE="",EDATE="",RCBNO="",
	NAME="",DESGINATION="",TELNO="",HPNO="",EMAIL="",ADD1="",ADD2="",ADD3="",ADD4="",FAX="",REMARKS="",COUNTY="",ZIP="",
    SALESPERCENT="",SDOLLARFRATE="",SCENTSFRATE="",SALES="",NOOFCATALOGS="",EDOLLARFRATE="",STATE="",
    ECENTSFRATE="",currencyCode = "",NOOFSIGNATURES="",TAXBY="", FLATRATE="",PERCENTAGE="", ENABLEINVENTORY = "0", ENABLEACCOUNTING = "0",TAXBYLABEL="",DECIMALPRECISION="0",ISTAXREG="0",REPROTSBASIS="";
	String sNewEnb    = "enabled";
	String sDeleteEnb = "enabled";
	String sAddEnb    = "disabled";
	String sUpdateEnb = "enabled";
	String res="";
	String existingplnt="";
	boolean flag=false;
	PlantMstUtil plantmstutil = new PlantMstUtil();
	
	PlantMstDAO _PlantMstDAO = new PlantMstDAO();
	plantmstutil.setmLogger(mLogger);
	_PlantMstDAO.setmLogger(mLogger);
	DateUtils dateutils = new DateUtils();
	PLANT     = StrUtils.fString(request.getParameter("PLANT"));
	PLNTDESC     = StrUtils.fString(request.getParameter("PLNTDESC"));
	ISTAXREG	= StrUtils.fString(request.getParameter("ISTAXREGISTRED"));
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
    DECIMALPRECISION=StrUtils.fString(request.getParameter("decimal_precision"));
    
    if (!"".equals(StrUtils.fString(request.getParameter("ENABLE_INVENTORY")))){
    	ENABLEINVENTORY = "1";
    }
    if (!"".equals(StrUtils.fString(request.getParameter("ENABLE_ACCOUNTING")))){
    	ENABLEACCOUNTING = "1";
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

	
	long randomCompany=0;
	DateUtils dateUtils = null;
	if(action.equalsIgnoreCase("NEW")){
	      PLANT="";
	      PLNTDESC="";
	      ISTAXREG="0";
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
          DECIMALPRECISION = "2";
            
	          
	}
	else if(action.equalsIgnoreCase("View")){
	
	    PLANT     = StrUtils.fString(request.getParameter("PLANT"));
	    PLNTDESC     = StrUtils.fString(request.getParameter("PLNTDESC"));
	    ISTAXREG	= StrUtils.fString(request.getParameter("ISTAXREGISTRED"));
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
		DECIMALPRECISION = StrUtils.fString(request.getParameter("decimal_precision"));  
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
	    PLANT=stringCompany;
	    ht.put("PLANT",PLANT);
	    ht.put("PLNTDESC",PLNTDESC);
	    ht.put("REPROTSBASIS",REPROTSBASIS);
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
        ht.put("NUMBEROFDECIMAL", DECIMALPRECISION);
	    boolean itemInserted = plantmstutil.insertPlantMst(ht,sUserId);
	    
	    if(itemInserted) 
	    {
	        res = "<font class = "+"maingreen"+">Company  "+ " "+ PLANT +" "+"Created Successfully</font>";
	        PLANT="";
	        PLNTDESC="";
	        ISTAXREG="0";
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
	    
	     } 
	     else 
	     {
	         res = "<font class = "+"black"+">Failed to add Company </font>";
	                        
	     }
	
	 }
	}

%>
  <div class="container-fluid m-t-20">
	 <div class="box"> 
            <div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title"><%=title %></h1>
		</div>
		
 <div class="box-body">
 
 <CENTER><strong><%=res%></strong></CENTER>

<form class="form-horizontal" name="form1" method="post" >


<div class="form-group">
 <label class="control-label col-sm-4" for="Company ID">Company ID:</label>
 <div class="col-sm-4">
  <input class="form-control" name="PLANT" type = "TEXT" value="<%=PLANT%>" size="30"  MAXLENGTH=50 readonly>
  </div>
     </div>

  <div class="form-group">
 <label class="control-label col-sm-4" for="Company Name">
 <i class="glyphicon glyphicon-star" style="font-size: 10px; top:-6px; color: #e50000"></i>&nbsp;Company Name:</label>
 <div class="col-sm-4">
  <input class="form-control" name="PLNTDESC" type = "TEXT" value="<%=PLNTDESC%>" size="30"  MAXLENGTH=100>
  </div>
     </div>
     <div class="form-group">
 <label class="control-label col-sm-4">
 <i class="glyphicon glyphicon-star" style="font-size: 10px; top:-6px; color: #e50000"></i>&nbsp;IS Tax Registered:</label>
 <div class="col-sm-4">
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
  <div class="form-group">
  	<label class="control-label col-sm-4">Report Basis</label> 
  		<div class="col-lg-8">
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
  <div class="form-group">
 <label class="control-label col-sm-4" for="TRN/RCB/Tax No" id="TaxLabel"></label>
 <div class="col-sm-4">
  <input class="form-control" name="RCBNO" type = "TEXT" value="<%=RCBNO%>" size="30"  MAXLENGTH=30>
  </div>
     </div>
     
 	<div class="form-group">
      <label class="control-label col-sm-4" for="Tax By">
      <i class="glyphicon glyphicon-star" style="font-size: 10px; top:-6px; color: #e50000"></i>&nbsp;Tax By:</label>
       <div class="col-sm-4">           	 			
            <SELECT class="form-control" data-toggle="dropdown" data-placement="right" name="TAXBY">
			<option value= "0" disabled selected>Choose : </option>
	        <option value="BYORDER">BYORDER</option>
		    <option value="BYPRODUCT">BYPRODUCT</option>
		</select>
		</div>
		</div> 
		
		<div class="form-group">
      <label class="control-label col-sm-4" for="Tax By Label">
      <i class="glyphicon glyphicon-star" style="font-size: 10px; top:-6px; color: #e50000"></i>&nbsp;Tax By Label:</label>
       <div class="col-sm-4">           	 			
            <SELECT onchange="getval()" id="select_id" class="form-control" data-toggle="dropdown" data-placement="right" name="TAXBYLABEL">
			<option value= "0" disabled selected>Choose : </option>
			 <option selected value="VAT">VAT</option>
             <option selected value="GST">GST</option>
	         <option selected value="TRN">TRN</option>
	         <option selected value="RCB">RCB</option>
	         <option selected value="TAX">TAX</option>
	         <option selected value="UEN">UEN</option>
		</select>
		</div>
		</div> 
     
      <div class="form-group">
 <label class="control-label col-sm-4" for="Start Date">
 <i class="glyphicon glyphicon-star" style="font-size: 10px; top:-6px; color: #e50000"></i>&nbsp;Start Date:</label>
 <div class="col-sm-4">
 <div class="input-group">  
  <input class="form-control datepicker" name="STARTDATE" type = "TEXT" value="<%=STARTDATE%>" size="50"  MAXLENGTH=20 readonly>
   </div>
     </div>
     </div>
     
     <div class="form-group">
 <label class="control-label col-sm-4" for="Expiry Date">
 <i class="glyphicon glyphicon-star" style="font-size: 10px; top:-6px; color: #e50000"></i>&nbsp;Expiry Date:</label>
 <div class="col-sm-4">
 <div class="input-group">  
  <input class="form-control datepicker" name="EXPIRYDATE" class="inactiveGry" type = "TEXT" value="<%=EXPIRYDATE%>"  size="50"  MAXLENGTH=20 readonly>
  </div>
     </div>
     </div>
     
      <div class="form-group">
 <label class="control-label col-sm-4" for="Actual Expiry Date">Actual Expiry Date:</label>
 <div class="col-sm-4">
  <input class="form-control" name="ACTUALEXPIRYDATE" type = "TEXT"  value="<%=ACTUALEXPIRYDATE%>" onfocus="javascript:checkDate();" size="30"    MAXLENGTH=20  readonly>
  </div>
     </div>
      
       
      <!-- <div class="box-header menu-drop">
      <h2><label class="control-label col-sm-4"><small>Contact Details</small></label></h2> 
    </div> -->
    
    
    <div class="form-group">
 <label class="control-label col-sm-4" for="Contact Person">
 <i class="glyphicon glyphicon-star" style="font-size: 10px; top:-6px; color: #e50000"></i>&nbsp;Contact Name:</label>
 <div class="col-sm-4">
  <input class="form-control" name="NAME" type = "TEXT" value="<%=NAME%>" size="30" onchange="javascript:checkDate();" MAXLENGTH=25>
  </div>
     </div>
      
      <div class="form-group">
 <label class="control-label col-sm-4" for="Designation">Designation:</label>
 <div class="col-sm-4">
  <input class="form-control" name="DESGINATION" type = "TEXT" value="<%=DESGINATION%>" size="30"  MAXLENGTH=30>
  </div>
     </div>
      
      <div class="form-group">
 <label class="control-label col-sm-4" for="Telephone No.">
 <i class="glyphicon glyphicon-star" style="font-size: 10px; top:-6px; color: #e50000"></i>&nbsp;Telephone No.:</label>
 <div class="col-sm-4">
  <input class="form-control" name="TELNO" type = "TEXT" value="<%=TELNO%>" size="30"  MAXLENGTH=20>
  </div>
     </div>
     
     <div class="form-group">
 <label class="control-label col-sm-4" for="Hand Phone No.">Hand Phone No.:</label>
 <div class="col-sm-4">
  <input class="form-control" name="HPNO" type = "TEXT" value="<%=HPNO%>" size="30"  MAXLENGTH=20>
  </div>
     </div> 
       
       
 <div class="form-group">
 <label class="control-label col-sm-4" for="Fax">Fax:</label>
 <div class="col-sm-4">
  <input class="form-control" name="FAX" type = "TEXT" value="<%=FAX%>" size="30"  MAXLENGTH=20>
  </div>
     </div>
       
      <div class="form-group">
 <label class="control-label col-sm-4" for="Email">Email:</label>
 <div class="col-sm-4">
  <input class="form-control" name="EMAIL" type = "TEXT" value="<%=EMAIL%>" size="30"  MAXLENGTH=50>
  </div>
     </div> 
     
     
     <div class="form-group">
 <label class="control-label col-sm-4" for="Unit No">
 <i class="glyphicon glyphicon-star" style="font-size: 10px; top:-6px; color: #e50000"></i>&nbsp;Unit No:</label>
 <div class="col-sm-4">
  <input class="form-control" name="ADD1" type = "TEXT" value="<%=ADD1%>" size="30"  MAXLENGTH=40 >
  </div>
     </div>
     
     
     <div class="form-group">
 <label class="control-label col-sm-4" for="Building">Building:</label>
 <div class="col-sm-4">
  <input class="form-control" name="ADD2" type = "TEXT" value="<%=ADD2%>" size="30"  MAXLENGTH=40>
  </div>
     </div> 
     
     
     <div class="form-group">
 <label class="control-label col-sm-4" for="Street">
 <i class="glyphicon glyphicon-star" style="font-size: 10px; top:-6px; color: #e50000"></i>&nbsp;Street:</label>
 <div class="col-sm-4">
  <input class="form-control" name="ADD3" type = "TEXT" value="<%=ADD3%>" size="30"  MAXLENGTH=40 >
  </div>
     </div>
      
     
      <div class="form-group">
 <label class="control-label col-sm-4" for="City">City:</label>
 <div class="col-sm-4">
  <input class="form-control" name="ADD4" type = "TEXT" value="<%=ADD4%>" size="30"  MAXLENGTH=40>
  </div>
     </div> 
     
         <div class="form-group">
 <label class="control-label col-sm-4" for="City">State:</label>
 <div class="col-sm-4">
  <input class="form-control" name="STATE" type = "TEXT" value="<%=STATE%>" size="30"  MAXLENGTH=40>
  </div>
     </div> 
     
     
     <div class="form-group">
 <label class="control-label col-sm-4" for="Country">
 <i class="glyphicon glyphicon-star" style="font-size: 10px; top:-6px; color: #e50000"></i>&nbsp;Country:</label>
 <div class="col-sm-4">
  <input class="form-control" name="COUNTY" type = "TEXT" value="<%=COUNTY%>" size="30"  MAXLENGTH=40 >
  </div>
     </div>
     
     
     <div class="form-group">
 <label class="control-label col-sm-4" for="Postal Code">
 <i class="glyphicon glyphicon-star" style="font-size: 10px; top:-6px; color: #e50000"></i>&nbsp;Postal Code:</label>
 <div class="col-sm-4">
  <input class="form-control" name="ZIP" type = "TEXT" value="<%=ZIP%>" size="30"  MAXLENGTH=10  >
  </div>
     </div>
      
      
      <div class="form-group">
 <label class="control-label col-sm-4" for="Remarks">Remarks:</label>
 <div class="col-sm-4">
  <input class="form-control" name="REMARKS" type = "TEXT" value="<%=REMARKS%>" size="30"  MAXLENGTH=100 >
  </div>
     </div>
     
     
     
    <div class="form-group">
 <label class="control-label col-sm-4" for="Number Of Catalogs">Number Of Catalogs:</label>
 <div class="col-sm-4">
  <input class="form-control" name="NOOFCATALOGS" type = "TEXT" value="<%=NOOFCATALOGS%>" size="30"  MAXLENGTH=30 >
  </div>
     </div> 
     
     <div class="form-group">
 <label class="control-label col-sm-4" for="Number Of Signatures">Number Of Signatures:</label>
 <div class="col-sm-4">
  <input class="form-control" name="NOOFSIGNATURES" type = "TEXT" value="<%=NOOFSIGNATURES%>" size="30"  MAXLENGTH=30 >
  </div>
     </div>
     
      
    <div class="form-group">
      <label class="control-label col-sm-4" for="Base Currency">
      <i class="glyphicon glyphicon-star" style="font-size: 10px; top:-6px; color: #e50000"></i>&nbsp;Base Currency:</label>
       <div class="col-sm-4">           	 			
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

		<div class="form-group">
					<label class="control-label col-sm-4" for="decimal_precision">Number
						of Decimals </label>
					<div class="col-sm-4">
						<SELECT class="form-control" data-toggle="dropdown" data-placement="right"
							name="decimal_precision">
							<option value="2" selected>2</option>
							<option value="3">3</option>
							<option value="4">4</option>
							<option value="5">5</option>
						</SELECT>
					</div>
				</div>
	
	<div class="form-group">
      <label class="control-label col-sm-4" for="Enable Modules">
      <i class="glyphicon glyphicon-star" style="font-size: 10px; top:-6px; color: #e50000"></i>&nbsp;Enable Modules</label>
       <div class="col-sm-4">           	 			
            <input type="checkbox" value="i" name="ENABLE_INVENTORY" id="ENABLE_INVENTORY" /> Inventory <input type="checkbox" value="a" name="ENABLE_ACCOUNTING" id="ENABLE_ACCOUNTING" /> Accounting 
		</div>
		</div>
	
	<div class="form-group">        
     <div class="col-sm-offset-4 col-sm-8">
	 <button type="button" class="Submit btn btn-default" onClick="window.location.href='../home'"><b>Back</b></button>&nbsp;&nbsp;
	 <button type="button" class="Submit btn btn-default" onClick="onNew();"><b>Clear</b></button>&nbsp;&nbsp;
	 <button type="button" class="Submit btn btn-default" onClick="onAdd();"><b>Save</b></button>&nbsp;&nbsp;
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
        }
        );
        </script>

<jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
</jsp:include>