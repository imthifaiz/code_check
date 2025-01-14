<%@ page contentType="text/html;charset=windows-1252"%>
<%@ page import="com.track.util.*"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.gates.*"%>
<%@ page import="com.track.dao.*"%>
<%@ page import="com.track.constants.*"%>
<%@ page import="java.util.*"%>
<jsp:useBean id="countryNCurrencyDAO"  class="com.track.dao.CountryNCurrencyDAO" />
<%
String title = "Maintenance Expiry Date";
%>
<%@include file="sessionCheck.jsp" %>
<jsp:include page="header2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
</jsp:include>

<script language="JavaScript" type="text/javascript" src="js/general.js"></script>
<meta http-equiv="Content-Type" content="text/html; charset=windows-1252">

    <SCRIPT LANGUAGE="JavaScript">

function popWin(URL) {
 subWin = window.open(URL, 'PRODUCT', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=700,height=400,left = 200,top = 184');
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
    document.form1.action = "MaintenanceExpiry.jsp?action=View";
    document.form1.submit();
}
function onNew(){
 
   document.form1.action  = "MaintenanceExpiry.jsp?action=NEW";
   document.form1.submit();
}
function onAdd(){
   
   document.form1.action  = "MaintenanceExpiry.jsp?action=ADD";
   document.form1.submit();
   
}
</script>
  
<script language="JavaScript" type="text/javascript" src="js/calendar.js"></script>
  
<%@ include file="header.jsp"%>
<%
	String MNTEXPIRYDATE="" ,action="",PLANT="",sPlant="",EDATE="",res="",existingplnt="";
	String sUserId = (String) session.getAttribute("LOGIN_USER");
	PLANT = (String) session.getAttribute("PLANT");
	
	boolean flag=false;
	PlantMstUtil plantmstutil = new PlantMstUtil();
	PlantMstDAO _PlantMstDAO = new PlantMstDAO();
	DateUtils dateutils = new DateUtils();
	StrUtils _strUtils     = new StrUtils();
	action = _strUtils.fString(request.getParameter("action"));
	String maintexpdate=(session.getAttribute("MAINTPAGEEXPDATE").toString());
	if(!maintexpdate.equals(""))
	{
		MNTEXPIRYDATE =maintexpdate;
	}
	else
	{
		MNTEXPIRYDATE     = _strUtils.fString(request.getParameter("MNTEXPIRYDATE"));
	}
	session.setAttribute("MAINTPAGEEXPDATE","");
	if(!MNTEXPIRYDATE.equals("") )
	{
	   EDATE    = MNTEXPIRYDATE.substring(6)+"-"+ MNTEXPIRYDATE.substring(3,5)+"-"+MNTEXPIRYDATE.substring(0,2);
	}
	
	if(action.equalsIgnoreCase("NEW")){
	      PLANT="";
	      MNTEXPIRYDATE="";
            
	          
	}
	else if(action.equalsIgnoreCase("View")){
	
	    PLANT     = _strUtils.fString(request.getParameter("PLANT"));
	    MNTEXPIRYDATE     = _strUtils.fString(request.getParameter("MNTEXPIRYDATE"));
	    
	       
	}
	
	else if(action.equalsIgnoreCase("ADD")){
		
		Hashtable htCond=new Hashtable();
		 htCond.put("PLANT",PLANT);  
		 
		 List listQry = plantmstutil.getPlantMstDetailsForMaintenanceExpDate(PLANT);
		 for (int i =0; i<listQry.size(); i++){
		     Map map = (Map) listQry.get(i);
		   	 existingplnt  = (String) map.get("PLANT");
		     if(existingplnt.equalsIgnoreCase(PLANT))
		     { 
		       StringBuffer updateQyery=new StringBuffer("set ");
		       updateQyery.append("MNTEXPIRYDATE" +" = '"+ (String)EDATE + "'");
		       
		       flag=  _PlantMstDAO.update(updateQyery.toString(),htCond,""); 
		       
		       if(flag==false)
		       {
		           res="<font class = "+"mainred"+">Company"+ " "+ PLANT +" "+" Maintenance Expiry Date Not Updated  Successfully</font>";
		       }
		       else
		       {
		          res="<font class = "+"maingreen"+">Company "+ " "+ PLANT +" "+"Maintenance Expiry Date  Updated Successfully</font>";
		          
		       }
		     }
		     else
		     {
		    	 res="<font class = "+"mainred"+">Company "+ " "+ PLANT +" "+"doesn't Exist</font>"; 
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
  <label class="control-label col-sm-4" for="Maintenance Expiry Date">Maintenance Expiry Date:</label>
    <div class="col-sm-4">
    <div class="input-group">    
    		<INPUT name="MNTEXPIRYDATE" class="form-control datepicker" type = "TEXT" value="<%=MNTEXPIRYDATE%>"  size="40"  MAXLENGTH=20 readonly>
   		 	</div>
  		      </div>
  		        </div>
  
  <div class="form-group">        
     <div class="col-sm-offset-4 col-sm-8">
      <button type="button" class="Submit btn btn-default"  onClick="window.location.href='../home'"><b>Back</b></button>&nbsp;&nbsp;
      	<button type="button" class="Submit btn btn-default" onClick="onNew();"><b>Clear</b></button>&nbsp;&nbsp;
      	<button type="button" class="Submit btn btn-default"  onClick="onAdd();"><b>Save</b></button>&nbsp;&nbsp;
     </div>
       </div>
       
       
    </form>
    </div>
    </div>
    </div>

<jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
</jsp:include>
 