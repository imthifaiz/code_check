<%@page import="com.track.util.http.HttpUtils"%>
<%@page import="net.sf.json.JSONObject"%>
<%@page import="javax.transaction.UserTransaction"%>
<%@page import="org.apache.commons.fileupload.FileItem"%>
<%@page import="org.apache.commons.fileupload.servlet.ServletFileUpload"%>
<%@page import="org.apache.commons.fileupload.disk.DiskFileItemFactory"%>
<%@page import="org.apache.commons.fileupload.FileItemFactory"%>
<%@ page contentType="text/html;charset=windows-1252"%>
<%@ page import="com.track.util.*"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.gates.*"%>
<%@ page import="com.track.dao.*"%>
<%@ page import="com.track.constants.*"%>
<%@ page import="java.util.*"%>
<%@ page import ="javax.script.ScriptEngineManager"%>
<%@ page import ="javax.script.ScriptEngine"%>
<%@ page import ="javax.script.Invocable"%>

<%
String title = "Company Status Edit";
String rootURI = HttpUtils.getRootURI(request);
%>
<%@include file="sessionCheck.jsp" %>
<jsp:include page="header2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
	<jsp:param name="mainmenu" value="<%=IConstants.SETTINGS %>" />
	<jsp:param name="submenu" value="<%=IConstants.USER_ADMIN %>" />
</jsp:include>
<script src="<%=rootURI%>/jsp/js/typeahead.jquery.js"></script>
<link rel="stylesheet" href="<%=rootURI%>/jsp/css/typeahead.css">
<link rel="stylesheet" href="<%=rootURI%>/jsp/css/accounting.css">
<link rel="stylesheet" href="<%=rootURI%>/jsp/css/fileshowcasedesign.css">
<link rel="stylesheet" href="<%=rootURI%>/jsp/css/bootstrap-datepicker.css">
<script type="text/javascript" src="<%=rootURI%>/jsp/js/json2.js"></script>
<script type="text/javascript" src="<%=rootURI%>/jsp/js/general.js"></script>
<script type="text/javascript">
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
	 document.form1.status.value= ""; 
	 document.form1.remarks.value= ""; 
	 document.form1.USER.value= ""; 
	 document.form1.PARTNER.value= ""; 
}

function onUpdate(){
	 
	var Authstat   = document.form1.authenticatestatus.value;

	if(Authstat == "" || Authstat == null) {
	alert("Please Choose Authenticate Status");
	document.form1.authenticatestatus.focus(); 
	return false; 
	}
	  
	var chk = confirm("Are you sure you would like to save?");
	if(chk){   
   	document.form1.action  = "../track/internalcrmedit?action=UPDATE";
   	document.form1.submit();}
	else
	{
		return false;
	}
}
 </script>
 
 <script type="text/javascript" src="<%=rootURI%>/jsp/js/general.js"></script>
 <script language="JavaScript" type="text/javascript" src="<%=rootURI%>/jsp/js/calendar.js"></script>
 
 <%@ include file="header.jsp"%>
<%
	String USER="",COMPANYCODE="",COMPANYNAME="",CONTACTPERSON="",SYSTEMTYPE="",CONTACTDETAIL="",PARTNER="",REGION="",STATUS="",REMARKS="",SDATE="",EDATE="",AEDATE="",AUTHENTICATESTATUS="",ENABLEINVENTORY = "0", ENABLEACCOUNTING = "0",AUTHSTAT="",
			PLANT="",STARTDATE="",EXPIRYDATE="",ACTUALEXPIRYDATE="",NAME="",TELNO="",EMAIL="",ENABLEPAYROLL="0",ENABLEPOS="0",sSAVE_RED;

	String sUserId = (String) session.getAttribute("LOGIN_USER");
	String Plant = (String) session.getAttribute("PLANT");
	String sPlantDesc="",sPlant="" ,action="";
	StrUtils strUtils = new StrUtils();
	sPlantDesc  = strUtils.fString(request.getParameter("PLANTDESC"));
	action = strUtils.fString(request.getParameter("action"));
	sPlant  = strUtils.fString(request.getParameter("PLANT"));
	String result  = strUtils.fString(request.getParameter("result"));
	sSAVE_RED  = strUtils.fString(request.getParameter("SAVE_RED"));

	String sNewEnb    = "enabled";
	String sDeleteEnb = "enabled";
	String sAddEnb    = "disabled";
	String sUpdateEnb = "enabled";
	String res="";
	String logores="";
	String existingplnt="";
	boolean flag=false;
	MasterUtil _MasterUtil=new  MasterUtil();
	PlantMstUtil plantmstutil = new PlantMstUtil();
	PlantMstDAO _PlantMstDAO = new PlantMstDAO();
	DateUtils dateutils = new DateUtils();
	StrUtils _strUtils     = new StrUtils();
          PLANT     = StrUtils.fString(request.getParameter("PLANT"));

        USER = StrUtils.fString(request.getParameter("USER"));
        COMPANYCODE = StrUtils.fString(request.getParameter("COMPANYCODE"));
        COMPANYNAME =  StrUtils.fString(request.getParameter("COMPANYNAME"));
        CONTACTPERSON = StrUtils.fString(request.getParameter("PERSON"));
        CONTACTDETAIL = StrUtils.fString(request.getParameter("CONTACTDETAIL"));
        SYSTEMTYPE = StrUtils.fString(request.getParameter("SYSTEMTYPE"));
        PARTNER = StrUtils.fString(request.getParameter("PARTNER"));
        REGION = StrUtils.fString(request.getParameter("REGION"));
        STATUS = StrUtils.fString(request.getParameter("status"));
        SDATE = StrUtils.fString(request.getParameter("SDATE"));
        EXPIRYDATE = StrUtils.fString(request.getParameter("EXPIRYDATE"));
        AEDATE = StrUtils.fString(request.getParameter("ACTUALEXPIRYDATE"));
        ACTUALEXPIRYDATE= StrUtils.fString(request.getParameter("ACTUALEXPIRYDATE"));
        REMARKS = StrUtils.fString(request.getParameter("remarks"));
        AUTHENTICATESTATUS = StrUtils.fString(request.getParameter("authenticatestatus"));
    
	if(!STARTDATE.equals("") || STARTDATE.equals(null))
	{
	    SDATE    = STARTDATE.substring(6)+"-"+ STARTDATE.substring(3,5)+"-"+STARTDATE.substring(0,2);
	}
	if(!EXPIRYDATE.equals("") || EXPIRYDATE.equals(null))
	{
	   EDATE    = EXPIRYDATE.substring(6)+"-"+ EXPIRYDATE.substring(3,5)+"-"+EXPIRYDATE.substring(0,2);
	}
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
 
    if(action.equalsIgnoreCase("UPDATE")){
    	 sAddEnb  = "disabled";
         result="";
      	 Hashtable htCond=new Hashtable();
      	 htCond.put("PLANT",PLANT);
      	 sPlant=request.getParameter("PLANT");

      	 List listQry = plantmstutil.getPlantMstDetailForCrm(PLANT);


      	 for (int i =0; i<listQry.size(); i++){
      	     Map map = (Map) listQry.get(i);
                    existingplnt  = (String) map.get("PLANT");
      	      String plntcode = request.getParameter("PLANT_CODE");
      		  System.out.println("Existing company"+plntcode);
      	     if(existingplnt.equalsIgnoreCase(PLANT))
      	     {

      	       StringBuffer updateQyery=new StringBuffer("set ");
      	       updateQyery.append("STATUS= '"+ (String)STATUS+ "'");
      	       updateQyery.append(",REMARKS= '"+ (String)REMARKS+ "'");
      	       updateQyery.append(",USERNAME= '"+ (String)USER+ "'");
      	       updateQyery.append(",PARTNER= '"+ (String)PARTNER+ "'");
    	       updateQyery.append("," +IDBConstants.EXPIRY_DATE +" = '"+ (String)EDATE  + "'");
//     	       updateQyery.append("," +IDBConstants.ACT_EXPIRY_DATE +" = '"+ (String)ACTUALEXPIRYDATE + "'");
      	       updateQyery.append(",AUTHSTAT= '"+ (String)AUTHENTICATESTATUS+ "'");

      	       flag=  _PlantMstDAO.update(updateQyery.toString(),htCond,"");

               if(true) {
              	  sSAVE_RED = "Update";
                } else {
              	  sSAVE_RED="Failed to Update";
                }
          }
       	    	else{
       	         	sSAVE_RED="Company  doesn't not Exists. Try again ";

       	         }

       	  }

      	 }
	

	    List viewlistQry = plantmstutil.getPlantMstDetailForCrm(PLANT);
            for (int i = 0; i < viewlistQry.size(); i++) {
                Map map = (Map) viewlistQry.get(i);
						System.out.println("map : " + map);
						PLANT     = StrUtils.fString((String)map.get("PLANT"));
                        COMPANYNAME     = StrUtils.fString((String)map.get("PLNTDESC"));
                        CONTACTPERSON=StrUtils.fString((String)map.get("NAME"));
                        TELNO     = StrUtils.fString((String)map.get("TELNO"));
                        EMAIL    = StrUtils.fString((String)map.get("EMAIL"));
                        CONTACTDETAIL= TELNO+","+EMAIL;
                        ENABLEINVENTORY = StrUtils.fString((String)map.get("ENABLE_INVENTORY"));
                        ENABLEACCOUNTING = StrUtils.fString((String)map.get("ENABLE_ACCOUNTING"));
                        ENABLEPAYROLL     = StrUtils.fString((String)map.get("ENABLE_PAYROLL"));
                        ENABLEPOS     = StrUtils.fString((String)map.get("ENABLE_POS"));
						if(ENABLEINVENTORY.equals("1")) {
							ENABLEINVENTORY = "Order Management";
						}else {
							ENABLEINVENTORY = "";
						}
						if(ENABLEACCOUNTING.equals("1")) {
							ENABLEACCOUNTING = ",Accounting";
						}else {
							ENABLEACCOUNTING = "";
						}
						if(ENABLEPAYROLL.equals("1")) {
							ENABLEPAYROLL = ",Payroll";
						}else {
							ENABLEPAYROLL = "";
						}
						if(ENABLEPOS.equals("1")) {
							ENABLEPOS = ",POS";
						}else {
							ENABLEPOS = "";
						}
						SYSTEMTYPE = ENABLEINVENTORY+""+ENABLEACCOUNTING+""+ENABLEPAYROLL+""+ENABLEPOS;
                        REGION = StrUtils.fString((String)map.get("REGION"));
                        STATUS = StrUtils.fString((String)map.get("STATUS"));
                        SDATE     = StrUtils.fString((String)map.get("STARTDATE"));
                        EDATE   = StrUtils.fString((String)map.get("EXPIRYDATE"));
                        AEDATE=StrUtils.fString((String)map.get("ACTEXPIRYDATE"));
                        REMARKS   = StrUtils.fString((String)map.get("REMARK"));
                        USER   = StrUtils.fString((String)map.get("USERNAME"));
                        PARTNER   = StrUtils.fString((String)map.get("PARTNER"));
                        AUTHENTICATESTATUS   = StrUtils.fString((String)map.get("AUTHSTAT"));
					}
	  
	  List AttachList =_PlantMstDAO.getCompAttachByHrdId(PLANT,"");
	  
	  if(!result.equalsIgnoreCase("")) {
			sSAVE_RED = "";
			res = "<font class = " + IDBConstants.FAILED_COLOR
			+ ">"+result+"</font>";
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
				onclick="window.location.href='<%=rootURI%>/internalcrmsummary'">
				<i class="glyphicon glyphicon-remove"></i>
			</h1>
		</div>

 <div class="box-body">

  <CENTER><strong><%=res%></font></strong></CENTER>

 <form class="form-horizontal" name="form1" id="form1" method="post" action="../track/internalcrmedit">
 <div class="col-sm-6">
<div class="row">
   <div class="form-group">
      <label class="control-label col-form-label col-sm-6" for="User">User</label>
      <div class="col-sm-6">
      <div class="input-group">
      	  	 <input class="form-control" name="USER" type = "TEXT" value="<%=USER%>" size="30"  MAXLENGTH=50 placeholder="Max 50 characters">
 			<INPUT type="hidden" name="SAVE_RED" value="<%=sSAVE_RED%>">
  		</div>
      </div>
    </div>
   <div class="form-group">
      <label class="control-label col-form-label col-sm-6" for="Company ID">Company Code</label>
      <div class="col-sm-6">
      <div class="input-group">
      	  	 <input class="form-control" name="PLANT" type = "TEXT" value="<%=PLANT%>" size="30"  MAXLENGTH=50 readonly>
  		</div>
      </div>
    </div>

   <div class="form-group">
      <label class="control-label col-form-label col-sm-6" for="Company Name">Company Name</label>
      <div class="col-sm-6">
      <div class="input-group">
      	  	 <input class="form-control" name="companyname" type = "TEXT" value="<%=COMPANYNAME%>" size="30"  MAXLENGTH=100 readonly>
  		</div>
      </div>
    </div>

     <div class="form-group">
	      <label class="control-label col-form-label col-sm-6 " for="Contact Person">Contact Person</label>
	      <div class="col-sm-6">
	      <div class="input-group">
	      	  	 <input class="form-control" name="contactperson" type = "TEXT" value="<%=CONTACTPERSON%>" size="30"  MAXLENGTH=100 readonly>
	  		</div>
	      </div>
	    </div>

     <div class="form-group">
	      <label class="control-label col-form-label col-sm-6 " for="Contact Detail">Contact Detail</label>
	      <div class="col-sm-6">
	      <div class="input-group">
	      	  	 <input class="form-control" name="contactdetail" type = "TEXT" value="<%=CONTACTDETAIL%>" size="30"  MAXLENGTH=100 readonly>
	  		</div>
	      </div>
	    </div>

     <div class="form-group">
	      <label class="control-label col-form-label col-sm-6 required" for="Authenticate Status">Authenticate Status</label>
	      <div class="col-sm-6">
	      <div class="input-group">
   	  	 	<input type="text" name="authenticatestatus" id="authenticatestatus" value="<%=AUTHENTICATESTATUS%>" class="ac-selected form-control" placeholder="AUTHENTICATE STATUS" >
	  		<span class="select-icon" onclick="$(this).parent().find('input[name=\'authenticatestatus\']').focus()">
	  			<i class="glyphicon glyphicon-menu-down"></i>
  			</span>
	  		</div>
	      </div>
	    </div>

    <div class="form-group">
      	<label class="control-label col-form-label col-sm-6" for="Purchase System">System Type</label>
      	<div class="col-sm-6" style="padding-top: 7px;"><%=SYSTEMTYPE%>      						 
      	</div>
    	</div>
    	
    	     <div class="form-group">
	      <label class="control-label col-form-label col-sm-6 " for="Partner">Partner</label>
	      <div class="col-sm-6">
	      <div class="input-group">
	      	  	 <input class="form-control" name="PARTNER" type = "TEXT" value="<%=PARTNER%>" size="30"  MAXLENGTH=100 placeholder="Max 100 characters">
	  		</div>
	      </div>
	    </div>
    	
    	     <div class="form-group" >
	      <label class="control-label col-form-label col-sm-6 " for="Region">Region</label>
	      <div class="col-sm-6">
	      <div class="input-group">
	      	  	 <input class="form-control" name="region" type = "TEXT" value="<%=REGION%>" size="30"  MAXLENGTH=100 readonly>
	  		</div>
	      </div>
	    </div>
	    
    	     <div class="form-group" >
	      <label class="control-label col-form-label col-sm-6 " for="Status">Status</label>
	      <div class="col-sm-6">
	      <div class="input-group">
			<input type="text" name="status" id="status" value="<%=STATUS%>" class="ac-selected form-control" placeholder="STATUS" >
					  		<span class="select-icon" onclick="$(this).parent().find('input[name=\'status\']').focus()">
					  			<i class="glyphicon glyphicon-menu-down"></i>
				  			</span>
	  		</div>
	      </div>
	    </div>

    <div class="form-group">
      <label class="control-label col-form-label col-sm-6" for="Start Date">Start Date</label>
      <div class="col-sm-6">
      <div class="input-group">
      	  	 <input class="form-control" name="STARTDATE" type = "TEXT" value="<%=SDATE%>" size="50"  MAXLENGTH=20 readonly>
  		</div>
      </div>
    </div>

    <div class="form-group">
      <label class="control-label col-form-label col-sm-6" for="Expiry Date">Expiry Date</label>
      <div class="col-sm-6">
      <div class="input-group">
      	  	 <input class="form-control datepicker" name="EXPIRYDATE" class="inactiveGry" type = "TEXT" value="<%=EDATE%>"  size="50"  MAXLENGTH=20 readonly>
  		</div>
      </div>
    </div>

    <div class="form-group">
      <label class="control-label col-form-label col-sm-6" for="Actual Expiry Date">Actual Expiry Date</label>
      <div class="col-sm-6">
      <div class="input-group">
      	  	 <input class="form-control" name="ACTUALEXPIRYDATE" type = "TEXT"  value="<%=AEDATE%>" onfocus="javascript:checkDate();" size="30" MAXLENGTH=20 readonly>
  		</div>
      </div>
    </div>

    <div class="form-group">
      <label class="control-label col-form-label col-sm-6" for="Remarks">Remarks</label>
      <div class="col-sm-6">
      <div class="input-group">
<%--       	  	 <input class="form-control" name="remarks" type = "TEXT" value="<%=REMARKS%>" size="30"  MAXLENGTH=30> --%>
								<textarea rows="4" name="remarks" class="ember-text-area form-control ember-view"
								placeholder="Max 500 characters"  maxlength="500"><%=REMARKS%></textarea>
  		</div>
      </div>
    </div>
	</div>
    </div>

    <div class="form-group">
     <div class="col-sm-offset-4 col-sm-8">
     <button type="button" class="Submit btn btn-default" onClick="return onNew()">Clear</button>&nbsp;&nbsp;
	 <button type="button" class="btn btn-success" data-toggle="modal" data-target="#myModal" onClick="onUpdate();">Save</button>&nbsp;&nbsp;
 	</div>
 	</div>
   </form>


 	    </div>
</div>
</div>

<script>
$(document).ready(function(){
    $('[data-toggle="tooltip"]').tooltip();
    var plant = document.form1.PLANT.value;
    if(document.form1.SAVE_RED.value!=""){
    if(document.form1.SAVE_RED.value!="") {
	document.form1.action  = "../track/internalcrmsummary?PGaction=View&result=Company '"+plant+"' Updated Successfully";	   
    document.form1.submit();
	}
    else{
    	document.form1.action  = "../track/internalcrmsummary?PGaction=View&result=Company '"+plant+"' Updated Successfully";	   
        document.form1.submit();
}
    }
});
        $(document).ready(function(){
            $("#status").typeahead({
            	  hint: true,
            	  minLength:0,  
            	  searchOnFocus: true
            	},
            	{
            	  name: 'postatus',
            	  display: 'value',  
            	  source: substringMatcher(postatus),
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
        	
            $("#authenticatestatus").typeahead({
            	  hint: true,
            	  minLength:0,  
            	  searchOnFocus: true
            	},
            	{
            	  name: 'authstatus',
            	  display: 'value',  
            	  source: substringMatcher(authstatus),
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

          });
        		
        var postatus =   [{
            "year": "Trial",
            "value": "Trial",
            "tokens": [
              "Trial"
            ]
          },
  		  	{
		    "year": "Purchased",
		    "value": "Purchased",
		    "tokens": [
		      "Purchased"
		    ]
		  },
	  		{
		    "year": "Expired",
		    "value": "Expired",
		    "tokens": [
		      "Expired"
		    ]
		  },
		  {
			    "year": "Not In Use",
			    "value": "Not In Use",
			    "tokens": [
			      "Not In Use"
			    ]
			  }];
        		
        var authstatus =   [{
            "year": "0",
            "value": "0",
            "tokens": [
              "0"
            ]
          },
		  {
			    "year": "1",
			    "value": "1",
			    "tokens": [
			      "1"
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
      
</script>

<jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
</jsp:include>
