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
String title = "Edit Contacts";
String rootURI = HttpUtils.getRootURI(request);
%>
<%@include file="sessionCheck.jsp" %>
<jsp:include page="header2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
	<jsp:param name="mainmenu" value="<%=IConstants.SETTINGS %>" />
	<jsp:param name="submenu" value="<%=IConstants.USER_ADMIN %>" />
</jsp:include>
<script src="../jsp/js/typeahead.jquery.js"></script>
<link rel="stylesheet" href="../jsp/css/typeahead.css">
<link rel="stylesheet" href="../jsp/css/accounting.css">
<!-- <link rel="stylesheet" href="../jsp/css/fileshowcasedesign.css"> -->
<link rel="stylesheet" href="../jsp/css/bootstrap-datepicker.css">
<script type="text/javascript" src="../jsp/js/json2.js"></script>
<script type="text/javascript" src="../jsp/js/general.js"></script>
<%-- <script src="<%=rootURI%>/jsp/js/typeahead.jquery.js"></script> --%>
<%-- <link rel="stylesheet" href="<%=rootURI%>/jsp/css/typeahead.css"> --%>
<%-- <link rel="stylesheet" href="<%=rootURI%>/jsp/css/accounting.css"> --%>
<%-- <link rel="stylesheet" href="<%=rootURI%>/jsp/css/fileshowcasedesign.css"> --%>
<%-- <link rel="stylesheet" href="<%=rootURI%>/jsp/css/bootstrap-datepicker.css"> --%>
<%-- <script type="text/javascript" src="<%=rootURI%>/jsp/js/json2.js"></script> --%>
<%-- <script type="text/javascript" src="<%=rootURI%>/jsp/js/general.js"></script> --%>
<script type="text/javascript">
function popWin(URL) {
 subWin = window.open(URL, 'PRODUCT', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=700,height=400,left = 200,top = 184');
}

function onClear(){
	document.form1.NAME.value = "";
	document.form1.EMAIL.value = "";
	document.form1.COMPANYCONTACT.value = "";
	document.form1.JOB.value = "";
	document.form1.PHNO.value = "";
	document.form1.LIFECYCLE.value = "";
	document.form1.LEADSTATUS.value = "";
	document.form1.NOTE.value = "";
}

function onUpdate(){
	 
   var name   = document.form1.NAME.value;
   var email = document.form1.EMAIL.value;
   var companycontact = document.form1.COMPANYCONTACT.value;
   var job = document.form1.JOB.value;
   var phonenumber = document.form1.PHNO.value;
   var lifestage = document.form1.LIFECYCLE.value;
   var leadsts = document.form1.LEADSTATUS.value;
   var cID = document.form1.ID.value;
   
	if(name == "" || name == null) {alert("Please Enter Name");document.form1.NAME.focus(); return false; }
    if(email == "" || email == null) {alert("Please Enter Email");document.form1.EMAIL.focus(); return false; }
//     if(companycontact == "" || companycontact == null) {alert("Please Enter Company Contact");document.form1.COMPANYCONTACT.focus(); return false; }
//     if(job == "" || job == null) {alert("Please Enter Job Title");document.form1.JOB.focus(); return false; }
//     if(phonenumber == "" || phonenumber == null) {alert("Please Enter Phone Number");document.form1.PHNO.focus(); return false; }
    if(lifestage == "" || lifestage == null) {alert("Please Choose Lifecycle Stage");document.form1.LIFECYCLE.focus(); return false; }
    if(leadsts == "" || leadsts == null) {alert("Please Choose Lead Status");document.form1.LEADSTATUS.focus(); return false; }
    if(form1.COUNTRY_CODE.selectedIndex==0)
	{
	   alert("Please Select Country from Address");
	 form1.COUNTRY_CODE.focus();
	 return false;
	}
	  
	var chk = confirm("Are you sure you would like to save?");
	if(chk){   
	add_attachments();
   	document.form1.action  = "../contact/edit?action=UPDATE";
   	document.form1.submit();}
	else
	{
		return false;
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

function downloadFile(id,fileName)
{
	 var urlStrAttach = "/track/ContactAttachmentServlet?Submit=downloadAttachmentById&attachid="+id;
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
	var urlStrAttach = "/track/ContactAttachmentServlet?Submit=removeAttachmentById&removeid="+id;	
	$.ajax( {
		type : "POST",
		url : urlStrAttach,
		success : function(data) {
					window.location.reload();
				}
			});
}

function add_attachments(){
    var formData = new FormData($('#form1')[0]);
    var userId= form1.ID.value;
	if(userId){
    $.ajax({
        type: 'post',
        url: "/track/ContactAttachmentServlet?Submit=add_attachments",
	    data:  formData,//{key:val}
	    contentType: false,
	    processData: false,
        success: function (data) {
        	console.log(data);
        //	window.location.reload();
        	document.form.action  = "../jsp/ContactEdit.jsp?action=UPDATE";
        	document.form.submit();
        },
       /*  error: function (data) {
            alert(data.responseText);
        } */
        error: function (data) {
        	document.form.action  = "../jsp/ContactEdit.jsp?action=UPDATE";
       		document.form.submit();
        }
    });
	}else{
		alert("Please enter Company Name");
	}
        return false; 
  } 
 </script>
 
<%--  <script type="text/javascript" src="<%=rootURI%>/jsp/js/general.js"></script> --%>
<%--  <script language="JavaScript" type="text/javascript" src="<%=rootURI%>/jsp/js/calendar.js"></script> --%>
 <link rel="stylesheet" href="../jsp/css/typeahead.css">
<link rel="stylesheet" href="../jsp/css/accounting.css">
<script language="JavaScript" type="text/javascript" src="../jsp/js/general.js"></script>
 
 <%@ include file="header.jsp"%>
<%
	String name="",email="",companycontact="",job="",phno="",lifecycle="",leadstatus="",note="",hdrid="",cHdrId="",USER_NAME="",region="",sCountry="",
			plant="",sSAVE_RED;
	String MOBILENO="",HOWWEKNOW="",INDUSTRY="",WEBSITE="",FACEBOOK="",LINKED="",SALESPROBABILITY="";
	String sUserId = (String) session.getAttribute("LOGIN_USER");
	String Plant = (String) session.getAttribute("PLANT");
	String sPlantDesc="",sPlant="" ,action="";
	StrUtils strUtils = new StrUtils();
	userBean userBean = new userBean();
	sPlantDesc  = strUtils.fString(request.getParameter("PLANTDESC"));
	action = strUtils.fString(request.getParameter("action"));
	sPlant  = strUtils.fString(request.getParameter("PLANT"));
	String result  = strUtils.fString(request.getParameter("result"));
	sSAVE_RED  = strUtils.fString(request.getParameter("SAVE_RED"));
	cHdrId = strUtils.fString(request.getParameter("ID"));
	System.out.println(cHdrId);
	List contactattachlist= new ArrayList();
	

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
	ContactAttachDAO contactAttachDAO = new ContactAttachDAO();
	
          plant     = StrUtils.fString(request.getParameter("PLANT"));
          name     = StrUtils.fString(request.getParameter("NAME"));
          email     = StrUtils.fString(request.getParameter("EMAIL"));
          companycontact     = StrUtils.fString(request.getParameter("COMPANYCONTACT"));
          job     = StrUtils.fString(request.getParameter("JOB"));
          phno     = StrUtils.fString(request.getParameter("PHNO"));
          lifecycle     = StrUtils.fString(request.getParameter("LIFECYCLE"));
          USER_NAME     = StrUtils.fString(request.getParameter("USER_NAME"));
          leadstatus     = StrUtils.fString(request.getParameter("LEADSTATUS"));
          note     = StrUtils.fString(request.getParameter("NOTE"));
          cHdrId     = StrUtils.fString(request.getParameter("ID"));
          USER_NAME = strUtils.fString(request.getParameter("USER_NAME"));
       	  MOBILENO = strUtils.fString(request.getParameter("MOBILENO"));
      	  HOWWEKNOW = strUtils.fString(request.getParameter("HOWWEKNOW"));
      	  INDUSTRY = strUtils.fString(request.getParameter("INDUSTRY"));
      	  WEBSITE = strUtils.fString(request.getParameter("WEBSITE"));
      	  FACEBOOK = strUtils.fString(request.getParameter("FACEBOOK"));
      	  LINKED = strUtils.fString(request.getParameter("LINKED"));
      	  SALESPROBABILITY = strUtils.fString(request.getParameter("SALESPROBABILITY"));
      	  region = strUtils.fString((String) session.getAttribute("REGION"));
      	  sCountry = strUtils.InsertQuotes(strUtils.fString(request.getParameter("COUNTRY")));

    
 
    if(action.equalsIgnoreCase("UPDATE")){
    	 sAddEnb  = "disabled";
         result="";
      	 Hashtable htCond=new Hashtable();
      	 Hashtable det = new Hashtable();
      	 
      	 htCond.put("PLANT",sPlant);
//       	 htCond.put("NAME",name);
      	 htCond.put("ID",cHdrId);
      	 sPlant=request.getParameter("PLANT");
      	 cHdrId=request.getParameter("ID");

      	 List listQryHdr = plantmstutil.getContactHdr(plant,cHdrId);
      	 List listQryDet = plantmstutil.getContactDet(plant,cHdrId);


      	 for (int i =0; i<listQryHdr.size(); i++){
      	     Map map = (Map) listQryHdr.get(i);
                    existingplnt  = (String) map.get("PLANT");
                   String names  = (String) map.get("NAME");
                   String hdrID  = (String) map.get("ID");
      	     if(existingplnt.equalsIgnoreCase(plant)&& cHdrId.equalsIgnoreCase(hdrID))
      	     {
      	       StringBuffer updateQyery=new StringBuffer("set ");
      	       updateQyery.append("NAME= '"+ (String)name+ "'");
      	       updateQyery.append(",EMAIL= '"+ (String)email+ "'");
      	       updateQyery.append(",COMPANYCONTACT= '"+ (String)companycontact+ "'");
      	       updateQyery.append(",JOB= '"+ (String)job+ "'");
      	       updateQyery.append(",PHONENUMBER= '"+ (String)phno+ "'");
      	       updateQyery.append(",LEADSTATUS= '"+ (String)leadstatus+ "'");
      	       updateQyery.append(",MOBILENO= '"+ (String)MOBILENO+ "'");
      	       updateQyery.append(",HOWWEKNOW= '"+ (String)HOWWEKNOW+ "'");
      	       updateQyery.append(",USER_NAME= '"+ (String)USER_NAME+ "'");
      	       updateQyery.append(",COUNTRY= '"+ (String)sCountry+ "'");
      	       updateQyery.append(",INDUSTRY= '"+ (String)INDUSTRY+ "'");
      	       updateQyery.append(",WEBSITE= '"+ (String)WEBSITE+ "'");
      	       updateQyery.append(",FACEBOOK= '"+ (String)FACEBOOK+ "'");
      	       updateQyery.append(",LINKED= '"+ (String)LINKED+ "'");
      	       updateQyery.append(",SALESPROBABILITY= '"+ (String)SALESPROBABILITY+ "'");
      	       updateQyery.append(",UPAT= '"+dateutils.getDateTime()+"'");
      	       updateQyery.append(",UPBY= '"+(String)sUserId+"'");
      	       flag=  _PlantMstDAO.updateContactHdr(updateQyery.toString(),htCond,"");
   			
      	       MovHisDAO mdao = new MovHisDAO(plant);
   			mdao.setmLogger(mLogger);
   			Hashtable htm = new Hashtable();
   			htm.put("PLANT", plant);
   			htm.put("DIRTYPE", TransactionConstants.UPDATE_CONTACT);
   			htm.put("RECID", "");
   			htm.put("ITEM",cHdrId);
   			htm.put(IDBConstants.UPDATED_BY, sUserId);
   			htm.put(IDBConstants.REMARKS, note+","+strUtils.InsertQuotes(note));
   			htm.put(IDBConstants.CREATED_BY, sUserId);
   			htm.put(IDBConstants.CREATED_AT, dateutils.getDateTime());
   			htm.put(IDBConstants.UPDATED_AT, dateutils.getDateTime());
   			htm.put(IDBConstants.TRAN_DATE, dateutils.getDateinyyyy_mm_dd(dateutils.getDate()));
   			boolean inserted = mdao.insertIntoMovHis(htm);
	            
   			det.put(IDBConstants.PLANT,sPlant); 
	             det.put("LEADDATE",DateUtils.getDate()); 
	             det.put("LIFECYCLESTAGE",lifecycle); 
	             det.put("LEADSTATUS",leadstatus); 
	             det.put("NOTE",note); 
	             det.put("CONTACTHDRID",cHdrId); 
	             det.put("UPBY",sUserId);   det.put("CRBY",sUserId);
	             det.put("CRAT",dateutils.getDateTime());
	             det.put("UPAT",dateutils.getDateTime());
	             boolean ContactDetInserted = plantmstutil.insertContactDet(det); 

               if(true) {
              	  sSAVE_RED = "Update";
                } else {
              	  sSAVE_RED="Failed to Update";
                }
          }
       	    	else{
       	         	sSAVE_RED="Contact doesn't not Exists. Try again ";
       	         }
       	  }

      	 }

	    List contactHDR = plantmstutil.getContactHdr(plant,cHdrId);
            for (int i = 0; i < contactHDR.size(); i++) {
                Map map = (Map) contactHDR.get(i);
						System.out.println("map : " + map);
				          plant     = StrUtils.fString((String)map.get("PLANT"));
				          name     = StrUtils.fString((String)map.get("NAME"));
				          email=StrUtils.fString((String)map.get("EMAIL"));
				          MOBILENO=StrUtils.fString((String)map.get("MOBILENO"));
				          HOWWEKNOW=StrUtils.fString((String)map.get("HOWWEKNOW"));
				          USER_NAME=StrUtils.fString((String)map.get("USER_NAME"));
				          sCountry=StrUtils.fString((String)map.get("COUNTRY"));
				          INDUSTRY=StrUtils.fString((String)map.get("INDUSTRY"));
				          WEBSITE=StrUtils.fString((String)map.get("WEBSITE"));
				          FACEBOOK=StrUtils.fString((String)map.get("FACEBOOK"));
				          LINKED=StrUtils.fString((String)map.get("LINKED"));
				          SALESPROBABILITY=StrUtils.fString((String)map.get("SALESPROBABILITY"));
				          companycontact     = StrUtils.fString((String)map.get("COMPANYCONTACT"));
				          job    = StrUtils.fString((String)map.get("JOB"));
				          phno    = StrUtils.fString((String)map.get("PHONENUMBER"));
				          hdrid    = StrUtils.fString((String)map.get("ID"));
				          contactattachlist = contactAttachDAO.getcontactAttachByCId(plant, cHdrId);
					}

	    List contactDET = plantmstutil.getContactDet(plant,cHdrId);
            for (int i = 0; i < contactDET.size(); i++) {
                Map map = (Map) contactDET.get(i);
						System.out.println("map : " + map);
				          lifecycle = StrUtils.fString((String)map.get("LIFECYCLESTAGE"));
				          leadstatus = StrUtils.fString((String)map.get("LEADSTATUS"));
				          note     = StrUtils.fString((String)map.get("NOTE"));
					}
	  
	  
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
	  <!-- Thanzith Modified on 28.02.2022 -->
	 <ul class="breadcrumb backpageul">    
                <li class="underline-on-hover"><a href="../home">Dashboard</a></li>	
                 <li><a href="../contact/summary"><span class="underline-on-hover">Contacts Summary</span></a></li>	
                <li><label>Edit Contacts</label></li>                                   
            </ul>
        <!-- Thanzith Modified on 28.02.2022 --> 
            <div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title"><%=title %></h1>
              <h1 style="font-size: 18px; cursor: pointer;"
				class="box-title pull-right"
				onclick="window.location.href='../contact/summary'">
				<i class="glyphicon glyphicon-remove"></i>
			</h1>
		</div>

 <div class="box-body">

  <CENTER><strong><%=res%></font></strong></CENTER>

 <form class="form-horizontal" name="form1" id="form1" autocomplete="off" method="post" >
<div class="row">

   <div class="form-group">
      <label class="control-label col-form-label col-sm-2 required" for="Name">Company Name</label>
      	<div class="col-sm-4">
      		<div class="input-group">
      	  	 <input class="form-control" name="NAME" id="NAME" type = "TEXT" value="<%=name%>" size="30"  MAXLENGTH=100 placeholder="Max 100 characters">
      	  	 <input type="hidden" name="PLANT" value="<%=sPlant%>">
      	  	 <input type="hidden" name="ID" value="<%=cHdrId%>">
      	  	 <INPUT type="hidden" name="SAVE_RED" value="<%=sSAVE_RED%>">
      	  	 <INPUT type="hidden" name="COUNTRY" value="<%=sCountry%>">
  		<INPUT type="hidden" name="COUNTRY_REG" value="<%=region%>">
  			</div>
      	</div>
   </div>
   
   <div class="form-group">
      <label class="control-label col-form-label col-sm-2 required" for="Country">Country</label>
      <div class="col-sm-4">  
      <SELECT class="form-control" data-toggle="dropdown" data-placement="right" id="COUNTRY_CODE" name="COUNTRY_CODE" value="<%=sCountry%>" style="width: 100%">
				<OPTION style="display:none;">Select Country</OPTION>
				<%
		   //MasterUtil _MasterUtil=new  MasterUtil();
		   ArrayList ccList =  _MasterUtil.getCountryList("",plant,region);
			for(int i=0 ; i<ccList.size();i++)
      		 {
				Map m=(Map)ccList.get(i);
				String vCOUNTRYNAME = (String)m.get("COUNTRYNAME");
				String vCOUNTRY_CODE = (String)m.get("COUNTRY_CODE"); %>
		        <option  value='<%=vCOUNTRYNAME%>' ><%=vCOUNTRYNAME%> </option>		          
		        <%
       			}
			 %></SELECT>       
       <%-- <INPUT name="COUNTRY" type="TEXT" value="<%=sCountry%>"
			size="50" MAXLENGTH=50 class="form-control"> --%>
      </div>
      
    </div>
         <div class="form-group">
      <label class="control-label col-form-label col-sm-2" for="Industry">Industry</label>
      <div class="col-sm-4">
      <INPUT class="form-control" name="INDUSTRY" id="INDUSTRY" type="TEXT" value="<%=INDUSTRY%>" placeholder="Max 50 Characters" size="25" MAXLENGTH=50>
  		        </div>
    </div>
   
   <div class="form-group">
      <label class="control-label col-form-label col-sm-2" for="Company Contact">Contact Person</label>
      	<div class="col-sm-4">
      		<div class="input-group">
      	  	 <input class="form-control" name="COMPANYCONTACT" id="COMPANYCONTACT" type = "TEXT" value="<%=companycontact%>" size="30"  MAXLENGTH=100 placeholder="Max 100 characters">
  			</div>
      	</div>
   </div>
     
   <div class="form-group">
      <label class="control-label col-form-label col-sm-2" for="Designation">Designation</label>
      	<div class="col-sm-4">
      		<div class="input-group">
      	  	 <input class="form-control" name="JOB" id="JOB" type = "TEXT" value="<%=job%>" size="30"  MAXLENGTH=50 placeholder="Max 50 characters">
  			</div>
      	</div>
   </div>
   
     <div class="form-group">
      <label class="control-label col-form-label col-sm-2" for="Telephone Number">Telephone Number</label>
      	<div class="col-sm-4">
      		<div class="input-group">
      	  	 <input class="form-control" name="PHNO" id="PHNO" type = "TEXT" value="<%=phno%>" size="30" onkeypress="return validateInput(event)"  MAXLENGTH=20 placeholder="Max 20 characters">
  			</div>
      	</div>
   </div>
    <div class="form-group">
      <label class="control-label col-form-label col-sm-2"for="Mobile Number">Contact Number</label>
      	<div class="col-sm-4">
      		<div class="input-group">
      	  	 <input class="form-control" name="MOBILENO" id="MOBILENO" type = "TEXT" value="<%=MOBILENO%>" onkeypress="return validateInput(event)" size="30"  MAXLENGTH=20 placeholder="Max 20 characters">
  			</div>
      	</div>
   </div>
   <div class="form-group">
      <label class="control-label col-form-label col-sm-2"for="How We Know">How We Know</label>
      	<div class="col-sm-4">
      		<div class="input-group">
      	  	 <input class="form-control" name="HOWWEKNOW" id="HOWWEKNOW" type = "TEXT" value="<%=HOWWEKNOW%>" size="30"  MAXLENGTH=50 placeholder="Max 100 characters">
  			</div>
      	</div>
   </div>
   
  <div class="form-group">
      <label class="control-label col-form-label col-sm-2 required" for="Email">Email</label>
      	<div class="col-sm-4">
      		<div class="input-group">
      	  	 <input class="form-control" name="EMAIL" id="EMAIL" type = "TEXT" value="<%=email%>" size="30"  MAXLENGTH=50 placeholder="Max 50 characters">
  			</div>
      	</div>
   </div>
  
   <div class="form-group">
      <label class="control-label col-form-label col-sm-2" for="Sales Consaltant">Sales Consultant</label>
      	<div class="col-sm-4">
      		<div class="input-group">
      	  	 <input type="text" name="USER_NAME" id="USER_NAME" value="<%=USER_NAME%>" class="ac-selected form-control" onchange="checkUser(this.value)"  placeholder="Sales Constaltent" >
				<span class="select-icon" onclick="$(this).parent().find('input[name=\'USER_NAME\']').focus()">
				<i class="glyphicon glyphicon-menu-down"></i>
				</span>
  			</div>
      	</div>
   </div>
   
   <div class="form-group">
      <label class="control-label col-form-label col-sm-2 required" for="LifeCycle Stage">LifeCycle Stage</label>
      	<div class="col-sm-4">
      		<div class="input-group">
      	  	 <input type="text" name="LIFECYCLE" id="LIFECYCLE" value="<%=lifecycle%>" class="ac-selected form-control" placeholder="LifeCycle Stage" >
				<span class="select-icon" onclick="$(this).parent().find('input[name=\'LIFECYCLE\']').focus()">
				<i class="glyphicon glyphicon-menu-down"></i>
				</span>
  			</div>
      	</div>
   </div>
  
   <div class="form-group">
      <label class="control-label col-form-label col-sm-2 required" for="Lead Status">Lead Status</label>
      	<div class="col-sm-4">
      		<div class="input-group">
      	  	 <input type="text" name="LEADSTATUS" id="LEADSTATUS" value="<%=leadstatus%>" class="ac-selected form-control" placeholder="Lead Status" >
				<span class="select-icon" onclick="$(this).parent().find('input[name=\'LEADSTATUS\']').focus()">
				<i class="glyphicon glyphicon-menu-down"></i>
				</span>
  			</div>
      	</div>
   </div>
   
   <div class="form-group">
      <label class="control-label col-form-label col-sm-2" for="Sales Probability">Sales Probability</label>
      <div class="col-sm-4">
      <INPUT class="form-control" name="SALESPROBABILITY" id="SALESPROBABILITY"type="TEXT" value="<%=SALESPROBABILITY%>" placeholder="Max 50 Characters" size="25" MAXLENGTH=50>
  	 </div>
    </div>
    
   </div>
   
      <div class="bs-example">
     	<ul class="nav nav-tabs" id="myTab"> 
     	<li class="nav-item active">
            <a href="#Other" class="nav-link" data-toggle="tab" aria-expanded="true">Other Details</a>
        </li>
        <li class="nav-item">
            <a href="#note" class="nav-link" data-toggle="tab">Notes</a>
        </li>
        <li class="nav-item">
            <a href="#attachfiles" class="nav-link" data-toggle="tab">Attachments</a>
        </li>
        </ul>
        <div class="tab-content clearfix">
       <div class="tab-pane active" id="Other">
        <br>
        
         <div class="form-group">
      <label class="control-label col-form-label col-sm-2" for="Website">Website</label>
      <div class="col-sm-4">
      <INPUT class="form-control" name="WEBSITE" id="WEBSITE" type="TEXT" value="<%=WEBSITE%>" placeholder="Max 50 Characters" size="25" MAXLENGTH=50>
  		        </div>
    </div>
         <div class="form-group">
      <label class="control-label col-form-label col-sm-2" for="Facebook">Facebook</label>
      <div class="col-sm-4">
      <INPUT class="form-control" name="FACEBOOK" id="FACEBOOK"type="TEXT" value="<%=FACEBOOK%>" placeholder="Max 50 Characters" size="25" MAXLENGTH=50>
  		        </div>
    </div>
         <div class="form-group">
      <label class="control-label col-form-label col-sm-2" for="LinkedIn">LinkedIn</label>
      <div class="col-sm-4">
      <INPUT class="form-control" name="LINKED" id="LINKED"type="TEXT" value="<%=LINKED%>" placeholder="Max 50 Characters" size="25" MAXLENGTH=50>
  		        </div>
    </div>
         
    </div>
			
			<div class="tab-pane fade" id="note">
        <br>
         <div class="form-group">
      <label class="control-label col-form-label col-sm-2" for="Note">Note</label>
      	<div class="col-sm-4">
      		<div class="input-group">
<%--       	  	 <input class="form-control" name="NOTE" id="NOTE" type = "TEXT" value="<%=note%>" size="30"  MAXLENGTH=500> --%>
      	  	 <textarea rows="4" name="NOTE" class="ember-text-area form-control ember-view" placeholder="Max 500 characters"  maxlength="500"><%=note%></textarea>
  			</div>
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
				<%if(contactattachlist.size()>0){ %>
						<div id="supplierAttchNote">
							<small class="text-muted"><div class="attachclass"><a><%=contactattachlist.size()%> files Attached</a>
									<div class="tooltiptext" style="width: 30%">
										
										<%for(int i =0; i<contactattachlist.size(); i++) {   
									  		Map attach=(Map)contactattachlist.get(i); %>
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
     <button type="button" class="Submit btn btn-default" onClick="return onClear()">Clear</button>&nbsp;&nbsp;
	 <button type="button" class="btn btn-success" data-toggle="modal" data-target="#myModal" onClick="onUpdate();">Save</button>&nbsp;&nbsp;
 	</div>
 	</div>
 	<label class="control-label col-form-label col-sm-6" for="Note"><b style="font-size: 20px;">Activity History</b> </label>
		<div id="VIEW_RESULT_HERE"></div>

   </form>

</div>
</div>
</div>

<script>
	onGo(0);
	  function onGo(index) {
		  var index = index;
		  document.getElementById('VIEW_RESULT_HERE').innerHTML = '';
		  var urlStr = "/track/MasterServlet";
		  $.ajax({type: "POST",url: urlStr, data: {PLANT:"<%=plant%>",action: "VIEW_CONTACT_DETAILS_EDIT",PLANT:"<%=plant%>",ID:"<%=cHdrId%>"},dataType: "json", success: callback });
		  }

	  function callback(data){
			
			var outPutdata = getTable();
			var ii = 0;
			var errorBoo = false;
			$.each(data.errors, function(i,error){
				if(error.ERROR_CODE=="99"){
					errorBoo = true;
					
				}
			});
			
			if(!errorBoo){
				
		        $.each(data.contactmaster, function(i,item){
		                   
		        	outPutdata = outPutdata+item.CONTACTMASTERDATA;
		                    	ii++;
		            
		          });
		        
			}
		    outPutdata = outPutdata +'</TABLE>';
		    document.getElementById('VIEW_RESULT_HERE').innerHTML = outPutdata;
		}

	  function getTable(){
	        return '<TABLE class="table" id="tabledata" BORDER="0" cellspacing="0" WIDTH="85%"  align = "center">'+
	 	           '<thead style="background:#eaeafa">'+
		           '<tr>'+
		           '<th width="5%"></th>'+
		           '<th width="10%">S/No</th>'+
	 		       '<th width="20%">Lead Date</th>'+
	 		       '<th width="20%">Lifecycle Stage</th>'+
	 		       '<th width="20%">Lead Status</th>'+
	 		       '<th width="20%">Note</th>'+
	 	           '</tr>'+
	 	           '</thead>';

	}

	  $('select[name="COUNTRY_CODE"]').on('change', function(){
	      var text = $("#COUNTRY_CODE option:selected").text();
	      $("input[name ='COUNTRY']").val(text.trim());
	  });

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

	  

	  document.getElementById('VIEW_RESULT_HERE').innerHTML =  getTable()+'</TABLE>';
	  
$(document).ready(function(){
    $('[data-toggle="tooltip"]').tooltip();

    if(document.form1.COUNTRY.value!="")
	{
	$("select[name ='COUNTRY_CODE']").val(document.form1.COUNTRY.value);
	}    
    var plant = document.form1.PLANT.value;
    var name = document.form1.NAME.value;
    if(document.form1.SAVE_RED.value!=""){
    if(document.form1.SAVE_RED.value!="") {
// 	document.form1.action  = "../contact/summary?PGaction=View&result=Contacts '"+name+"' Updated Successfully";	   
	document.form1.action  = "../contact/summary?PGaction=View&result=Contacts Updated Successfully";	   
    document.form1.submit();
	}
    else{
//     	document.form1.action  = "../contact/summary?PGaction=View&result=Contacts '"+came+"' Updated Successfully";	   
    	document.form1.action  = "../contact/summary?PGaction=View&result=Contacts Updated Successfully";	   
        document.form1.submit();
}
    }
});
$(document).ready(function(){
    $("#LIFECYCLE").typeahead({
    	  hint: true,
    	  minLength:0,  
    	  searchOnFocus: true
    	},
    	{
    	  name: 'cyclestatus',
    	  display: 'value',  
    	  source: substringMatcher(cyclestatus),
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
	
    $("#LEADSTATUS").typeahead({
    	  hint: true,
    	  minLength:0,  
    	  searchOnFocus: true
    	},
    	{
    	  name: 'leadstatus',
    	  display: 'value',  
    	  source: substringMatcher(leadstatus),
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

    $("#SALESPROBABILITY").typeahead({
  	  hint: true,
  	  minLength:0,  
  	  searchOnFocus: true
  	},
  	{
  	  name: 'salesprobability',
  	  display: 'value',  
  	  source: substringMatcher(salesprobability),
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


	/* USERNAME Auto Suggestion */
	$('#USER_NAME').typeahead({
		  hint: true,
		  minLength:0,  
		  searchOnFocus: true
		},
		{
		  display: 'USER_ID',  
		  async: true,   
		  source: function (query, process,asyncProcess) {
			var urlStr = "/track/MasterServlet";
			$.ajax( {
			type : "POST",
			url : urlStr,
			async : true,
			data : {
				PLANT : "<%=Plant%>",
				ACTION : "GET_USERADMIN_FOR_USERDETAILS",
				USER : query
			},
			dataType : "json",
			success : function(data) {
				return asyncProcess(data.USERLEVEL);
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
		    return '<p class="item-suggestion">'+ data.USER_ID +'</p>';
			}
		  }
		  
		}).on('typeahead:open',function(event,selection){
			var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
			element.toggleClass("glyphicon-menu-up",true);
			element.toggleClass("glyphicon-menu-down",false);
		}).on('typeahead:close',function(){
			var element = $(this).parent().parent().find('.select-icon').find('.glyphicon');
			element.toggleClass("glyphicon-menu-up",false);
			element.toggleClass("glyphicon-menu-down",true);
		});


  });
		
     var cyclestatus =   [{
		    "year": "Contact",
		    "value": "Contact",
		    "tokens": [
		      "Contact"
		    ]
		  },
		  {
			    "year": "Customer",
			    "value": "Customer",
			    "tokens": [
			      "Customer"
			    ]
			  },
			  {
				    "year": "Evangelist",
				    "value": "Evangelist",
				    "tokens": [
				      "Evangelist"
				    ]
				  },
  		  	{
		    "year": "Lead",
		    "value": "Lead",
		    "tokens": [
		      "Lead"
		    ]
		  },
  		  	{
		    "year": "Marketing Qualified Lead",
		    "value": "Marketing Qualified Lead",
		    "tokens": [
		      "Marketing Qualified Lead"
		    ]
		  },
		  {
			    "year": "Other",
			    "value": "Other",
			    "tokens": [
			      "Other"
				    ]
				  },
		  {
			    "year": "Opportunity",
			    "value": "Opportunity",
			    "tokens": [
			      "Opportunity"
			    ]
			  },
			  {
				    "year": "Pilot offer",
				    "value": "Pilot offer",
				    "tokens": [
				      "Pilot offer"
				    ]
				  },
		  {
			    "year": "Prospect",
			    "value": "Prospect",
			    "tokens": [
			      "Prospect"
			    ]
			  },
			  {
				    "year": "Probability",
				    "value": "Probability",
				    "tokens": [
				      "Probability"
				    ]
				  },
				  {
					    "year": "Proposal",
					    "value": "Proposal",
					    "tokens": [
					      "Proposal"
					    ]
					  },
			  {
				"year": "Qualified Lead",
				 "value": "Qualified Lead",
				 "tokens": [
				    "Qualified Lead"
				    ]
				  },
		 	 {
	            "year": "Subscriber",
	            "value": "Subscriber",
	            "tokens": [
	              "Subscriber"
	            ]
	          },
	          {
				    "year": "Sales Qualified Lead",
				    "value": "Sales Qualified Lead",
				    "tokens": [
				      "Sales Qualified Lead"
				    ]
				  }];
		
var leadstatus =   [{
    "year": "New",
    "value": "New",
    "tokens": [
      "New"
    ]
  },
	  	{
    "year": "Open",
    "value": "Open",
    "tokens": [
      "Open"
    ]
  },
	  	{
    "year": "In Progress",
    "value": "In Progress",
    "tokens": [
      "In Progress"
    ]
  },
  {
    "year": "Open Deal",
    "value": "Open Deal",
    "tokens": [
      "Open Deal"
	    ]
	  },
	    {
		    "year": "Product Closure",
		    "value": "Product Closure",
		    "tokens": [
		      "Product Closure"
			    ]
			  },
		  {
		    "year": "Declined",
		    "value": "Declined",
		    "tokens": [
		      "Declined"
			    ]
			  },
		  {
		    "year": "Refused",
		    "value": "Refused",
		    "tokens": [
		      "Refused"
			    ]
			  }];

var salesprobability =   [{
    "year": "25%",
    "value": "25%",
    "tokens": [
      "25%"
    ]
  },
  {
	    "year": "50%",
	    "value": "50%",
	    "tokens": [
	      "50%"
		    ]
	  },
 	  {
	    "year": "75%",
	    "value": "75%",
	    "tokens": [
	      "75%"
		    ]
	  },
  	  {
	    "year": "100%",
	    "value": "100%",
	    "tokens": [
	      "100%"
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


function checkUser(user){	
	var urlStr = "/track/MasterServlet";
	$.ajax( {
		type : "POST",
		url : urlStr,
		async : true,
		data : {
			USER : user,
			ACTION : "USER_CHECK"
			},
			dataType : "json",
			success : function(data) {
				if (data.status == "99") {
					alert("Sales Consultant Does't Exists");
					document.getElementById("USER_NAME").focus();
					$("#USER_NAME").typeahead('val', '');
					return false;	
				} 
				else 
					return true;
			}
		});
	 return true;
}

      
</script>

<jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
</jsp:include>
