<%@ page contentType="text/html;charset=windows-1252"%>
<%@ page import="com.track.util.*"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.gates.*"%>
<%@ page import="com.track.dao.*"%>
<%@ page import="com.track.constants.*"%>
<%@ page import="java.util.*"%>

<%
String title = "Company Details";
%>
<%@include file="sessionCheck.jsp" %>
<jsp:include page="header2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
</jsp:include>

<script language="JavaScript" type="text/javascript" src="js/general.js"></script>
<jsp:useBean id="countryNCurrencyDAO"  class="com.track.dao.CountryNCurrencyDAO" />
<meta http-equiv="Content-Type" content="text/html; charset=windows-1252">


 <SCRIPT LANGUAGE="JavaScript">

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
      var expyear=new Date(document.form1.EXPIRYDATE.value);
      var yearplus=expyear.getFullYear();
      var yearminus=yearplus-1;
      document.form1.ACTUALEXPIRYDATE.focus();
      document.form1.ACTUALEXPIRYDATE.value=expirydate.substring(0,2)+"/"+"01"+"/"+yearminus;
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
   if(ITEM_ID == "" || ITEM_ID == null) {alert("Please Enter Plant "); document.form1.ACTUALEXPIRYDATE.value="";return false; }

   document.form1.action  = "maintPlant.jsp?action=ADD";
      document.form1.submit();
  function OnClear()
  {
  document.form1.PLANT.value="";
  document.form1.PLNTDESC.value="";document.form1.STARTDATE.value="";
  document.form1.EXPIRYDATE.value="";
  return true;
  }
   
}
</script>
  <script language="JavaScript" type="text/javascript" src="js/calendar.js"></script>
  
<%@ include file="header.jsp"%>
<%
	String sUserId = (String) session.getAttribute("LOGIN_USER");
	String Plant = (String) session.getAttribute("PLANT");
	//String currencyCode = (String) session.getAttribute("BASE_CURRENCY");
	String sPlantDesc="",sPlant="" ,action="";
	StrUtils strUtils = new StrUtils();
	sPlantDesc  = strUtils.fString(request.getParameter("PLANTDESC"));
	action = strUtils.fString(request.getParameter("action"));
	sPlant  = strUtils.fString(request.getParameter("PLANT"));
	
	String PLANT="",COMPANY="",PLNTDESC="",STARTDATE="",EXPIRYDATE="",ACTUALEXPIRYDATE="",SDATE="",EDATE="",
	       NAME="",DESGINATION="",TELNO="",HPNO="",EMAIL="",ADD1="",ADD2="",ADD3="",ADD4="",FAX="",REMARKS="",COUNTY="",ZIP="",
	       SALESPERCENT="",SDOLLARFRATE="",SCENTSFRATE="",SALES="",EDOLLARFRATE="",ECENTSFRATE="",STATE="",
	       currencyCode ="",NOOFSIGNATURES="",TAXBY="",TAXBYLABEL="",PERCENTAGE="",FLATRATE="";
	String sNewEnb    = "enabled";
	String sDeleteEnb = "enabled";
	String sAddEnb    = "disabled";
	String sUpdateEnb = "enabled";
	String res="";
	String existingplnt="";
	String NOOFCATALOGS="";
	boolean flag=false;
	PlantMstUtil plantmstutil = new PlantMstUtil();
	PlantMstDAO _PlantMstDAO = new PlantMstDAO();
	
	DateUtils dateutils = new DateUtils();
	StrUtils _strUtils     = new StrUtils();
	
	PLANT     = _strUtils.fString(request.getParameter("PLANT"));
	PLNTDESC     = _strUtils.fString(request.getParameter("PLNTDESC"));
	STARTDATE     = _strUtils.fString(request.getParameter("STARTDATE"));
	EXPIRYDATE   = _strUtils.fString(request.getParameter("EXPIRYDATE"));
	ACTUALEXPIRYDATE= _strUtils.fString(request.getParameter("ACTUDALEXPIRYDATE"));
	NAME=_strUtils.fString(request.getParameter("NAME"));
	DESGINATION     = _strUtils.fString(request.getParameter("DESGINATION"));
	TELNO     = _strUtils.fString(request.getParameter("TELNO"));
	HPNO    = _strUtils.fString(request.getParameter("HPNO"));
	EMAIL    = _strUtils.fString(request.getParameter("EMAIL"));
	ADD1   = _strUtils.fString(request.getParameter("ADD1"));
	ADD2   = _strUtils.fString(request.getParameter("ADD2"));
	ADD3   = _strUtils.fString(request.getParameter("ADD3"));
	ADD4  =  _strUtils.fString(request.getParameter("ADD4"));
	COUNTY = _strUtils.fString(request.getParameter("COUNTY"));
	ZIP = _strUtils.fString(request.getParameter("ZIP"));
	REMARKS   = _strUtils.fString(request.getParameter("REMARKS"));
	FAX  = _strUtils.fString(request.getParameter("FAX"));
    SALES = StrUtils.fString(request.getParameter("SALES"));
    NOOFCATALOGS = StrUtils.fString(request.getParameter("NOOFCATALOGS"));    
    NOOFSIGNATURES = StrUtils.fString(request.getParameter("NOOFSIGNATURES"));    
    SALESPERCENT=_strUtils.fString(request.getParameter("SALESPERCENT"));
    SDOLLARFRATE=_strUtils.fString(request.getParameter("SDOLLARFRATE"));
    SCENTSFRATE=_strUtils.fString(request.getParameter("SCENTSFRATE"));
    EDOLLARFRATE=_strUtils.fString(request.getParameter("EDOLLARFRATE"));
    ECENTSFRATE=_strUtils.fString(request.getParameter("ECENTSFRATE"));  
    STATE=_strUtils.fString(request.getParameter("STATE"));  
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
	currencyCode = _strUtils.fString(request.getParameter("BASECURRENCY"));
	TAXBY   = _strUtils.fString(request.getParameter("TAXBY"));
	TAXBYLABEL   = _strUtils.fString(request.getParameter("TAXBYLABEL"));
	if(action.equalsIgnoreCase("NEW")){
	      PLANT="";
	      PLNTDESC="";
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
	      SALESPERCENT="0";
          SDOLLARFRATE="0";
          SCENTSFRATE="0";
          EDOLLARFRATE="0";
          ECENTSFRATE="0";    
          STATE="";
	}
	else if(action.equalsIgnoreCase("View")){
	    PLANT     = _strUtils.fString(request.getParameter("PLANT"));
	    PLNTDESC     = _strUtils.fString(request.getParameter("PLNTDESC"));
	    STARTDATE     = _strUtils.fString(request.getParameter("STARTDATE"));
	    EXPIRYDATE   = _strUtils.fString(request.getParameter("EXPIRYDATE"));
	    ACTUALEXPIRYDATE=_strUtils.fString(request.getParameter("ACTUDALEXPIRYDATE"));
	    NAME=_strUtils.fString(request.getParameter("NAME"));
	    DESGINATION     = _strUtils.fString(request.getParameter("DESGINATION"));
	    TELNO     = _strUtils.fString(request.getParameter("TELNO"));
	    HPNO    = _strUtils.fString(request.getParameter("HPNO"));
	    EMAIL    = _strUtils.fString(request.getParameter("EMAIL"));
	    ADD1   = _strUtils.replaceCharacters2Recv(_strUtils.fString(request.getParameter("ADD1")));
	    ADD2   = _strUtils.replaceCharacters2Recv(_strUtils.fString(request.getParameter("ADD2")));
	    ADD3   = _strUtils.replaceCharacters2Recv(_strUtils.fString(request.getParameter("ADD3")));
	    ADD4   = _strUtils.replaceCharacters2Recv(_strUtils.fString(request.getParameter("ADD4")));
	    COUNTY   = _strUtils.fString(request.getParameter("COUNTY"));
	    ZIP   = _strUtils.fString(request.getParameter("ZIP"));
	    REMARKS   = _strUtils.fString(request.getParameter("REMARKS"));
	    FAX   = _strUtils.fString(request.getParameter("FAX"));
        SALES   = _strUtils.fString(request.getParameter("SALES"));
	    SALESPERCENT   = _strUtils.fString(request.getParameter("SALESPERCENT"));
	    SDOLLARFRATE   = _strUtils.fString(request.getParameter("SDOLLARFRATE"));
	    SCENTSFRATE   = _strUtils.fString(request.getParameter("SCENTSFRATE"));
        EDOLLARFRATE   = _strUtils.fString(request.getParameter("EDOLLARFRATE"));
        ECENTSFRATE   = _strUtils.fString(request.getParameter("ECENTSFRATE"));
        TAXBY   = _strUtils.fString(request.getParameter("TAXBY"));
        TAXBYLABEL   = _strUtils.fString(request.getParameter("TAXBYLABEL"));
        STATE  = _strUtils.fString(request.getParameter("STATE"));
            
	         
	}
%>

<div class="container-fluid m-t-20">
	 <div class="box"> 
            <div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title"><%=title %></h1>
		</div>
		
 <div class="box-body">

<FORM class="form-horizontal" name="form1" method="post" >
  
<div class="form-group">
 <label class="control-label col-sm-4" for="Company Code">Company Code:</label>
 <div class="col-sm-4">
  <input class="form-control" name="PLANT" type = "TEXT" value="<%=PLANT%>" size="30"  MAXLENGTH=10 readonly>
  </div>
     </div>
     
<div class="form-group">
 <label class="control-label col-sm-4" for="Company Description">Company Description:</label>
 <div class="col-sm-4">
  <input class="form-control" type = "TEXT" value="<%=PLNTDESC%>" size="30"  MAXLENGTH=50  readonly>
  </div>
     </div>
     
 <div class="form-group">
 <label class="control-label col-sm-4" for="Tax By">Tax By:</label>
 <div class="col-sm-4">
  <input class="form-control" name="TAXBY" type = "TEXT" value="<%=TAXBY%>" size="30"  MAXLENGTH=30 readonly>
  </div>
</div>

<div class="form-group">
 <label class="control-label col-sm-4" for="Tax By Label">Tax By Label:</label>
 <div class="col-sm-4">
  <input class="form-control" name="TAXBYLAEBL" type = "TEXT" value="<%=TAXBYLABEL%>" size="30"  MAXLENGTH=30 readonly>
  </div>
</div>
     
     
 <div class="form-group">
 <label class="control-label col-sm-4" for="Start Date">Start Date:</label>
 <div class="col-sm-4">
  <input class="form-control" name="STARTDATE" type = "TEXT" value="<%=STARTDATE%>" size="30"  MAXLENGTH=20 readonly>
  </div>
     </div>    

 <div class="form-group">
 <label class="control-label col-sm-4" for="Expiry Date">Expiry Date:</label>
 <div class="col-sm-4">
  <input class="form-control" name="EXPIRYDATE" type = "TEXT" value="<%=EXPIRYDATE%>"  size="30"  MAXLENGTH=20 readonly>
  </div>
     </div>
     
 <div class="form-group">
 <label class="control-label col-sm-4" for="Actual Expiry Date">Actual Expiry Date:</label>
 <div class="col-sm-4">
  <input class="form-control" name="ACTUALEXPIRYDATE" type = "TEXT"  value="<%=ACTUALEXPIRYDATE%>" onfocus="javascript:checkDate();" size="30" MAXLENGTH=20  readonly>
  </div>
     </div> 
     
      <!-- <div class="box-header menu-drop">
      
      <h2><label class="control-label col-sm-4"><small>Contact Details</small></label></h2> 
      
    </div> -->
         

<div class="form-group">
 <label class="control-label col-sm-4" for="Name">Contact Name:</label>
 <div class="col-sm-4">
  <input class="form-control" name="NAME" type = "TEXT" value="<%=NAME%>" size="30" onchange="javascript:checkDate();" MAXLENGTH=20 readonly>
  </div>
     </div>
      
<div class="form-group">
 <label class="control-label col-sm-4" for="Designation">Designation:</label>
 <div class="col-sm-4">
  <input class="form-control" name="DESGINATION" type = "TEXT" value="<%=DESGINATION%>" size="30"  MAXLENGTH=30 readonly>
  </div>
     </div> 
   
   <div class="form-group">
 <label class="control-label col-sm-4" for="Telephone No.">Telephone No.:</label>
 <div class="col-sm-4">
  <input class="form-control" name="TELNO" type = "TEXT" value="<%=TELNO%>" size="30"  MAXLENGTH=15 readonly>
  </div>
     </div> 
      
      
<div class="form-group">
 <label class="control-label col-sm-4" for="Hand Phone No.">Hand Phone No.:</label>
 <div class="col-sm-4">
  <input class="form-control" name="HPNO" type = "TEXT" value="<%=HPNO%>" size="30"  MAXLENGTH=15 readonly>
  </div>
     </div>
     
      
<div class="form-group">
 <label class="control-label col-sm-4" for="Fax">Fax:</label>
 <div class="col-sm-4">
  <input class="form-control" name="FAX" type = "TEXT" value="<%=FAX%>" size="30"  MAXLENGTH=15 readonly>
  </div>
     </div>  
     
     
<div class="form-group">
 <label class="control-label col-sm-4" for="Email">Email:</label>
 <div class="col-sm-4">
  <input class="form-control" name="EMAIL" type = "TEXT" value="<%=EMAIL%>" size="30"  MAXLENGTH=50 readonly>
  </div>
     </div>  
      
<div class="form-group">
 <label class="control-label col-sm-4" for="Unit No">Unit No:</label>
 <div class="col-sm-4">
  <input class="form-control" name="ADD1" type = "TEXT" value="<%=ADD1%>" size="30"  MAXLENGTH=30 readonly>
  </div>
     </div>   
      
<div class="form-group">
 <label class="control-label col-sm-4" for="Street">Street:</label>
 <div class="col-sm-4">
  <input class="form-control" name="ADD2" type = "TEXT" value="<%=ADD2%>" size="30"  MAXLENGTH=30 readonly>
  </div>
     </div>
     
<div class="form-group">
 <label class="control-label col-sm-4" for="City">City:</label>
 <div class="col-sm-4">
  <input class="form-control" name="ADD3" type = "TEXT" value="<%=ADD3%>" size="30"  MAXLENGTH=30 readonly>
  </div>
     </div>
     
     <div class="form-group">
 <label class="control-label col-sm-4" for="State">State:</label>
 <div class="col-sm-4">
  <input class="form-control" name="STATE" type = "TEXT" value="<%=STATE%>" size="30"  MAXLENGTH=30 readonly>
  </div>
     </div>
   
   <div class="form-group">
 <label class="control-label col-sm-4" for="Address4">Address4:</label>
 <div class="col-sm-4">
  <input class="form-control" name="ADD4" type = "TEXT" value="<%=ADD4%>" size="30"  MAXLENGTH=30 readonly>
  </div>
     </div>   
     
<div class="form-group">
 <label class="control-label col-sm-4" for="Country">Country:</label>
 <div class="col-sm-4">
  <input class="form-control" name="COUNTY" type = "TEXT" value="<%=COUNTY%>" size="30"  MAXLENGTH=15  readonly>
  </div>
     </div> 
     
<div class="form-group">
 <label class="control-label col-sm-4" for="Postal Code">Postal Code:</label>
 <div class="col-sm-4">
  <input class="form-control" name="ZIP" type = "TEXT" value="<%=ZIP%>" size="30"  MAXLENGTH=10 readonly>
  </div>
     </div>
     
<div class="form-group">
 <label class="control-label col-sm-4" for="Remarks">Remarks:</label>
 <div class="col-sm-4">
  <input class="form-control" name="REMARKS" type = "TEXT" value="<%=REMARKS%>" size="30"  MAXLENGTH=40 readonly>
  </div>
     </div>
      
   <div class="form-group">
 <label class="control-label col-sm-4" for="Number Of Catalogs">Number Of Catalogs:</label>
 <div class="col-sm-4">
  <input class="form-control" name="NOOFCATALOGS" type = "TEXT" value="<%=NOOFCATALOGS%>" size="30"  MAXLENGTH=30 readonly>
  </div>
     </div>    


<div class="form-group">
 <label class="control-label col-sm-4" for="Number Of Signatures">Number Of Signatures:</label>
 <div class="col-sm-4">
  <input class="form-control" name="NOOFSIGNATURES" type = "TEXT" value="<%=NOOFSIGNATURES%>" size="30"  MAXLENGTH=30 readonly>
  </div>
     </div>

  
 

	
	<div class="form-group">
      <label class="control-label col-sm-4" for="Base Currency">Base Currency:</label>
       <div class="col-sm-4">           	 			
            <SELECT class="form-control" data-toggle="dropdown" data-placement="right" name="BaseCurrency">
			<option value= "0" >Select One : </option>
		<%
		Hashtable ht1 = new Hashtable();
		   ArrayList ccList = countryNCurrencyDAO.getCurrencyList(ht1);
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
     <div class="col-sm-12" align="center">
	 <button type="button" class="Submit btn btn-default" onClick="window.location.href='PlantSummary.jsp'"><b>Back</b></button>&nbsp;&nbsp;
	 	</div>
	 	</div>
	 	
</FORM>
	 	
</div>
</div> 
</div>	

<jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
</jsp:include>
