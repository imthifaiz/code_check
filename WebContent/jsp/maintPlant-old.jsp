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

<script src="js/general.js"></script>
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
    document.form1.action = "maintPlant.jsp?action=View";
    document.form1.submit();
}
function onAdd(){
   var ITEM_ID   = document.form1.PLANT.value;
 
   if(ITEM_ID == "" || ITEM_ID == null) {alert("Please Enter Company "); return false; }
   
   var chkmsg=confirm("Are you sure you would like to save?");
   if(chkmsg){
   document.form1.action  = "maintPlant.jsp?action=ADD";
   document.form1.submit();}
}
  function OnClear()
  {
  document.form1.PLANT.value="";
  document.form1.PLNTDESC.value="";document.form1.STARTDATE.value="";
  document.form1.EXPIRYDATE.value="";
  return true;
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
	
	String PLANT="",COMPANY="",PLNTDESC="",STARTDATE="",EXPIRYDATE="",NOOFCATALOGS="",ACTUALEXPIRYDATE="",SDATE="",EDATE="",RCBNO="",
	       NAME="",DESGINATION="",TELNO="",HPNO="",EMAIL="",ADD1="",ADD2="",ADD3="",ADD4="",FAX="",REMARKS="",COUNTY="",ZIP="",
           SALESPERCENT="",SDOLLARFRATE="",SCENTSFRATE="",SALES="",EDOLLARFRATE="",ECENTSFRATE="",currencyCode="",NOOFSIGNATURES="",STATE="",PERCENTAGE="",
	       FLATRATE="", ENABLEINVENTORY = "0", ENABLEACCOUNTING = "0",ISTAXREG="",REPROTSBASIS="";
	
               
	String sNewEnb    = "enabled";
	String sDeleteEnb = "enabled";
	String sAddEnb    = "disabled";
	String sUpdateEnb = "enabled";
	String res="";
	String existingplnt="";
	boolean flag=false;
	PlantMstUtil plantmstutil = new PlantMstUtil();
	PlantMstDAO _PlantMstDAO = new PlantMstDAO();
	DateUtils dateutils = new DateUtils();
	StrUtils _strUtils     = new StrUtils();
	
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
    SALES = StrUtils.fString(request.getParameter("SALES"));
    SALESPERCENT=StrUtils.fString(request.getParameter("SALESPERCENT"));
    SDOLLARFRATE=StrUtils.fString(request.getParameter("SDOLLARFRATE"));
    SCENTSFRATE=StrUtils.fString(request.getParameter("SCENTSFRATE"));
    EDOLLARFRATE=StrUtils.fString(request.getParameter("EDOLLARFRATE"));
    ECENTSFRATE=StrUtils.fString(request.getParameter("ECENTSFRATE"));
    NOOFCATALOGS = StrUtils.fString(request.getParameter("NOOFCATALOGS")); 
    NOOFSIGNATURES = StrUtils.fString(request.getParameter("NOOFSIGNATURES")); 
    STATE = StrUtils.fString(request.getParameter("STATE")); 
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
	if(action.equalsIgnoreCase("NEW")){
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
	      ADD3   = "";  ADD4   = "";
	      COUNTY="";ZIP="";
	      REMARKS="";
	      FAX="";
	      RCBNO="";
          SALESPERCENT="0";
          SDOLLARFRATE="0";
          SCENTSFRATE="0";
          EDOLLARFRATE="0";
          ECENTSFRATE="0";
          STATE="";
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
	    RCBNO = StrUtils.fString(request.getParameter("RCBNO"));
	    currencyCode = StrUtils.fString(request.getParameter("BaseCurrency"));
	    STATE = StrUtils.fString(request.getParameter("STATE"));
	 
	    
	        
	}
	else if(action.equalsIgnoreCase("ADD")){
	
	 Hashtable htCond=new Hashtable();
	 htCond.put("PLANT",request.getParameter("PLANT_CODE"));  
	 sPlant=request.getParameter("PLANT_CODE");
	 List listQry = plantmstutil.getPlantMstDetails(sPlant);
	 for (int i =0; i<listQry.size(); i++){
	     Map map = (Map) listQry.get(i);
	   	 existingplnt  = (String) map.get("PLANT");
	     String plntcode = request.getParameter("PLANT_CODE");
	     if(existingplnt.equalsIgnoreCase(plntcode))
	     { 
	       StringBuffer updateQyery=new StringBuffer("set ");
	       updateQyery.append(IDBConstants.PLNTDESC +" = '"+ (String)PLNTDESC + "'");
	       updateQyery.append(","+IDBConstants.ISTAXREG +" = '"+ (String)ISTAXREG + "'");
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
	       updateQyery.append("," +IDBConstants.COUNTY+" = '"+ (String)COUNTY+ "'");
	       updateQyery.append("," +IDBConstants.ZIP+" = '"+ (String)ZIP+ "'");
	       updateQyery.append("," +IDBConstants.USERFLD1+" = '"+ (String)REMARKS+ "'");
	       updateQyery.append("," +IDBConstants.USERFLD2+" = '"+ (String)FAX+ "'");
           updateQyery.append("," +IDBConstants.SALES_CHARGE_BY+" = '"+ (String)SALES+ "'");
	       updateQyery.append("," +IDBConstants.SALES_PERCENT+" = '"+ (String)SALESPERCENT+ "'");
	       updateQyery.append("," +IDBConstants.SALES_FR_DOLLARS+" = '"+ (String)SDOLLARFRATE+ "'");
	       updateQyery.append("," +IDBConstants.SALES_FR_CENTS+" = '"+ (String)SCENTSFRATE+ "'");
	       updateQyery.append("," +IDBConstants.ENQUIRY_FR_DOLLARS+" = '"+ (String)EDOLLARFRATE+ "'");
	       updateQyery.append("," +IDBConstants.ENQUIRY_FR_CENTS+" = '"+ (String)ECENTSFRATE+ "'");
	       updateQyery.append("," +IDBConstants.BASE_CURRENCY+" = '"+ (String)currencyCode+ "'");
	       updateQyery.append("," +"NUMBER_OF_SIGNATURES"+" = '"+ NOOFSIGNATURES+ "'");
	       updateQyery.append("," +IDBConstants.STATE+" = '"+ (String)STATE+ "'");
	       updateQyery.append(",ENABLE_INVENTORY=" + ENABLEINVENTORY);
	       updateQyery.append(",ENABLE_ACCOUNTING=" + ENABLEACCOUNTING);
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
%>
<div class="container-fluid m-t-20">
	 <div class="box"> 
            <div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title"><%=title %></h1>
		</div>
		
 <div class="box-body">
 
 <CENTER><strong><%=res%></strong></CENTER>
 
<FORM class="form-horizontal" name="form1" method="post" >
  
 <div class="form-group">
 <label class="control-label col-sm-4" for="Company ID">
 <i class="glyphicon glyphicon-star" style="font-size: 10px; top:-6px; color: #e50000"></i>&nbsp;Company ID:</label>
 <div class="col-sm-4">
 <div class="input-group">
 <INPUT name="PLANT_CODE" type = "hidden" value="" size="30"  MAXLENGTH=10 >
  <input class="form-control" name="PLANT" type = "TEXT" value="<%=PLANT%>" size="30"  MAXLENGTH=10>
  <span class="input-group-addon" onClick="javascript:popWin('PlantMstList.jsp?PLANT='+form1.PLANT.value);">
			<a href="#" data-toggle="tooltip" data-placement="top" title="Company ID Details">
   		 	<i class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span>
  </div>
     </div>
     </div>
     
  <div class="form-group">
 <label class="control-label col-sm-4" for="Company Name">
 <i class="glyphicon glyphicon-star" style="font-size: 10px; top:-6px; color: #e50000"></i>&nbsp;Company Name:</label>
 <div class="col-sm-4">
  <input class="form-control" name="PLNTDESC" type = "TEXT" value="<%=PLNTDESC%>" size="30"  MAXLENGTH=50>
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
 <label class="control-label col-sm-4" for="TRN/RCB No">
 <i class="glyphicon glyphicon-star" style="font-size: 10px; top:-6px; color: #e50000"></i>&nbsp;TRN/RCB No:</label>
 <div class="col-sm-4">
  <input class="form-control" name="RCBNO" type = "TEXT" value="<%=RCBNO%>" size="30"  MAXLENGTH=30>
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
  <input class="form-control datepicker" name="EXPIRYDATE" type = "TEXT" value="<%=EXPIRYDATE%>"  size="50"  MAXLENGTH=20 readonly>
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
 <label class="control-label col-sm-4" for="Contact Person">Contact Name:</label>
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
 <label class="control-label col-sm-4" for="Telephone No.">Telephone No.:</label>
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
			<option value= "0" >Select One : </option>
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
      <label class="control-label col-sm-4" for="Tax By">
      <i class="glyphicon glyphicon-star" style="font-size: 10px; color: #e50000"></i>&nbsp;Enabled Modules</label>
       <div class="col-sm-4">  						 
       						 <input type="checkbox" value="i" name="ENABLE_INVENTORY" id="ENABLE_INVENTORY" />Inventory 
       						 <input type="checkbox" value="a" name="ENABLE_ACCOUNTING" id="ENABLE_ACCOUNTING" /> Accounting
		</div>
		</div>          
                   
        <div class="form-group">        
     <div class="col-sm-offset-4 col-sm-8">
	 <button type="button" class="Submit btn btn-default" onClick="window.location.href='../home'"><b>Back</b></button>&nbsp;&nbsp;
	 <button type="button" class="Submit btn btn-default" onClick="return onNew();"><b>Clear</b></button>&nbsp;&nbsp;
	 <button type="button" class="Submit btn btn-default" onClick="onAdd();"><b>Save</b></button>&nbsp;&nbsp;
	 	</div>
	 	</div>           
                   
   
</FORM>

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
</Script>

</div>
</div> 
</div>	

<jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
</jsp:include>
