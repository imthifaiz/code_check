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
String title = "Edit Company";
%>
<%@include file="sessionCheck.jsp" %>
<jsp:include page="header2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
	<jsp:param name="mainmenu" value="<%=IConstants.SETTINGS %>" />
	<jsp:param name="submenu" value="<%=IConstants.USER_ADMIN %>" />
</jsp:include>
<script type="text/javascript" src="js/json2.js"></script>
<script type="text/javascript" src="js/general.js"></script>
<script type="text/javascript">
  
function popWin(URL) {
 subWin = window.open(URL, 'PRODUCT', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=700,height=400,left = 200,top = 184');
}

function onNew(){
 
   document.form1.action  = "customer_maintPlant.jsp?action=NEW";
   document.form1.submit();
}

function image_edit(){
	$('#userImage').val('');
    var formData = new FormData($('form')[0]);
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
		alert("Please enter Product Id");
	}
        return false; 
  }	

function onAdd(){
   var ITEM_ID   = document.form1.PLANT.value;
   var ITEM_NAME   = document.form1.PLNTDESC.value;
   var ITEM_ISTAXREG=document.form1.ISTAXREGISTRED.value;
   //var ITEM_RCBNO   = document.form1.RCBNO.value;
   var ITEM_ADD1   = document.form1.ADD1.value;
   var ITEM_ADD3   = document.form1.ADD3.value;
   var ITEM_COUNTY   = document.form1.COUNTY.value;
   var ITEM_ZIP   = document.form1.ZIP.value;
   var TAXBY   = document.form1.TAXBY.value;
   var TAXBYLABEL   = document.form1.TAXBYLABEL.value;
   var TAXBYLABELORDERMANAGEMENT   = document.form1.TAXBYLABELORDERMANAGEMENT.value;
   if(ITEM_ID == "" || ITEM_ID == null) {alert("Please Enter Company ID ");document.form1.PLANT.focus(); return false; }
   if(ITEM_NAME == "" || ITEM_NAME == null) {alert("Please Enter Company Name ");document.form1.PLNTDESC.focus(); return false; }
  // if(ITEM_RCBNO == "" || ITEM_RCBNO == null) {alert("Please Enter TRN No. ");document.form1.RCBNO.focus(); return false; }
   if(ITEM_ADD1 == "" || ITEM_ADD1 == null) {alert("Please Enter Unit No. ");document.form1.ADD1.focus(); return false; }
   if(ITEM_ADD3 == "" || ITEM_ADD3 == null) {alert("Please Enter Street ");document.form1.ADD3.focus(); return false; }
   if(ITEM_COUNTY == "" || ITEM_COUNTY == null) {alert("Please Enter Country ");document.form1.COUNTY.focus(); return false; }
   if(ITEM_ZIP == "" || ITEM_ZIP == null) {alert("Please Enter Postal Code ");document.form1.ZIP.focus(); return false; }
   if(TAXBY == "0" || TAXBY == null) {alert("Please Select Tax By ");document.form1.TAXBY.focus(); return false; }
   if(TAXBYLABEL == "0" || TAXBYLABEL == null) {alert("Please Select Tax By Label");document.form1.TAXBYLABEL.focus(); return false; }
   if(TAXBYLABELORDERMANAGEMENT == "0" || TAXBYLABELORDERMANAGEMENT == null) {alert("Please Select Tax By Label");document.form1.TAXBYLABELORDERMANAGEMENT.focus(); return false; }
   var chkmsg=confirm("Are you sure you would like to save?");
   if(chkmsg){
   document.form1.action  = "customer_maintPlant.jsp?action=ADD";
   document.form1.submit();}
}
 
 function getval() {
	  var  d = document.getElementById("select_id").value;
	     document.getElementById('TaxLabel').innerHTML = d + " No.:";

	}
 
 </script>
 
 <script type="text/javascript" src="js/general.js"></script>
 <script language="JavaScript" type="text/javascript" src="js/calendar.js"></script>
 
 <%@ include file="header.jsp"%>
<%
	String sUserId = (String) session.getAttribute("LOGIN_USER");
	String Plant = (String) session.getAttribute("PLANT");
	String sPlantDesc="",sPlant="" ,action="";
	StrUtils strUtils = new StrUtils();
	sPlantDesc  = strUtils.fString(request.getParameter("PLANTDESC"));
	action = strUtils.fString(request.getParameter("action"));
	sPlant  = strUtils.fString(request.getParameter("PLANT"));
	String result  = strUtils.fString(request.getParameter("result"));
	
	String PLANT="",COMPANY="",PLNTDESC="",STARTDATE="",EXPIRYDATE="",ACTUALEXPIRYDATE="",SDATE="",EDATE="",RCBNO="",
	       NAME="",DESGINATION="",TELNO="",HPNO="",EMAIL="",ADD1="",ADD2="",ADD3="",ADD4="",FAX="",
	       REMARKS="",COUNTY="",ZIP="",TAXBY="",TAXBYLABEL="",TAXBYLABELORDERMANAGEMENT="",strtaxby="", strtaxbylabe1="",strtaxbylabe1order="",
	       ENABLEINVENTORY = "0", ENABLEACCOUNTING = "0",STATE="",DECIMALPRECISION="",IMAGEPATH="",ISTAXREG="",REPROTSBASIS="";
	String sNewEnb    = "enabled";
	String sDeleteEnb = "enabled";
	String sAddEnb    = "disabled";
	String sUpdateEnb = "enabled";
	String res="";
	String logores="";
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
	STATE  =  StrUtils.fString(request.getParameter("STATE"));
	COUNTY = StrUtils.fString(request.getParameter("COUNTY"));
	ZIP = StrUtils.fString(request.getParameter("ZIP"));
	REMARKS   = StrUtils.fString(request.getParameter("REMARKS"));
	FAX  = StrUtils.fString(request.getParameter("FAX"));
	RCBNO = StrUtils.fString(request.getParameter("RCBNO"));
	TAXBY= StrUtils.fString(request.getParameter("TAXBY"));
	TAXBYLABEL= StrUtils.fString(request.getParameter("TAXBYLABEL"));
	TAXBYLABELORDERMANAGEMENT= StrUtils.fString(request.getParameter("TAXBYLABELORDERMANAGEMENT"));
	DECIMALPRECISION = StrUtils.fString(request.getParameter("decimal_precision"));
	
    String imagePath = DbBean.COMPANY_LOGO_PATH + "/" + Plant.toLowerCase() + DbBean.LOGO_FILE; 

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
        
  
	 if(action.equalsIgnoreCase("ADD")){
	 Hashtable htCond=new Hashtable();
	 htCond.put("PLANT",Plant);  
	 sPlant=request.getParameter("PLANT");
    
	 List listQry = plantmstutil.getPlantMstDetails(Plant);


	 for (int i =0; i<listQry.size(); i++){
	     Map map = (Map) listQry.get(i);
              existingplnt  = (String) map.get("PLANT");
	      String plntcode = request.getParameter("PLANT_CODE");
System.out.println("Existing company"+plntcode);              
	     if(existingplnt.equalsIgnoreCase(Plant))
	     { 
	       StringBuffer updateQyery=new StringBuffer("set ");
	       updateQyery.append(IDBConstants.PLNTDESC +" = '"+ (String)PLNTDESC + "'");
	       updateQyery.append(",REPROTSBASIS = '"+REPROTSBASIS + "'");
	       updateQyery.append(","+IDBConstants.ISTAXREG +" = '"+ (String)ISTAXREG + "'");
	       updateQyery.append(","+ IDBConstants.START_DATE+" = '"+ (String)SDATE  + "'");
	       updateQyery.append("," +IDBConstants.EXPIRY_DATE +" = '"+ (String)EDATE  + "'");
	       updateQyery.append("," +IDBConstants.ACT_EXPIRY_DATE +" = '"+ (String)ACTUALEXPIRYDATE + "'");
	       updateQyery.append("," +IDBConstants.NAME +" = '"+ (String)NAME + "'");
	       updateQyery.append("," +IDBConstants.DESGINATION +" = '"+ (String)DESGINATION+ "'");
	       updateQyery.append("," +IDBConstants.TELNO +" = '"+ (String)TELNO+ "'");
	       updateQyery.append("," +IDBConstants.HPNO +" = '"+ (String)HPNO+ "'");
	       updateQyery.append("," +IDBConstants.EMAIL +" = '"+ (String)EMAIL+ "'");
	       updateQyery.append("," +IDBConstants.ADD1 +" = '"+ (String)ADD1+ "'");
	       updateQyery.append("," +IDBConstants.ADD2 +" = '"+ (String)ADD2+ "'");
	       updateQyery.append("," +IDBConstants.ADD3+" = '"+ (String)ADD3+ "'");
	       updateQyery.append("," +IDBConstants.RCBNO+" = '"+ (String)RCBNO+ "'");
	       updateQyery.append("," +IDBConstants.ADD4+" = '"+ (String)ADD4+ "'");
	       updateQyery.append("," +IDBConstants.STATE+" = '"+ (String)STATE+ "'");
	       updateQyery.append("," +IDBConstants.COUNTY+" = '"+ (String)COUNTY+ "'");
	       updateQyery.append("," +IDBConstants.ZIP+" = '"+ (String)ZIP+ "'");
	       updateQyery.append("," +IDBConstants.USERFLD1+" = '"+ (String)REMARKS+ "'");
	       updateQyery.append("," +IDBConstants.USERFLD2+" = '"+ (String)FAX+ "'");
	       updateQyery.append("," +IDBConstants.TAXBY+" = '"+ (String)TAXBY+ "'");
	       updateQyery.append("," +IDBConstants.TAXBYLABEL+" = '"+ (String)TAXBYLABEL+ "'");
		   updateQyery.append("," +IDBConstants.TAXBYLABELORDERMANAGEMENT+" = '"+ (String)TAXBYLABELORDERMANAGEMENT+ "'");
	       updateQyery.append(",ENABLE_INVENTORY=" + ENABLEINVENTORY);
	       updateQyery.append(",ENABLE_ACCOUNTING=" + ENABLEACCOUNTING);
	       updateQyery.append(",NUMBEROFDECIMAL=" + DECIMALPRECISION);
	       flag=  _PlantMstDAO.update(updateQyery.toString(),htCond,""); 
	       
	       MovHisDAO mdao = new MovHisDAO();
	       Hashtable htm = new Hashtable();
	       htm.put("PLANT",sPlant);
	       htm.put("DIRTYPE",TransactionConstants.MAINT_CUST_COMPANY);
	       htm.put("RECID","");
	       htm.put("REMARKS",REMARKS);
	       htm.put("CRBY",(String)sUserId);
	       htm.put("CRAT",dateutils.getDateTime());
	       htm.put("UPAT",dateutils.getDateTime());
	       htm.put(IDBConstants.TRAN_DATE,dateutils.getDateinyyyy_mm_dd(dateutils.getDate()));      
	
	       boolean   inserted = mdao.insertIntoMovHis(htm);
             /*  if(inserted){
                        //htm.remove("PLANT");
                        //htm.put("PLANT",Plant);
                        inserted = mdao.insertIntoMovHis(htm);
                } */
	       if(flag==false)
	       {
	           res="<font class = "+"mainred"+">Company"+ " "+ sPlant +" "+"Details Not Updated  Successfully</font>";
	       }
	       else
	       {
	          res="<font class = "+"maingreen"+">Company "+ " "+ sPlant +" "+"Details  Updated Successfully</font>";
	         
	       }
	       
	     }
	     else
	     {
	    	 res="<font class = "+"mainred"+">Company "+ " "+ sPlant +" "+"doesn't Exist</font>"; 
	     }
	 
	  }
	
	 }

	    List viewlistQry = plantmstutil.getPlantMstDetails(Plant);
            for (int i = 0; i < viewlistQry.size(); i++) {
                Map map = (Map) viewlistQry.get(i);
System.out.println("map : " + map);
                        PLANT     = StrUtils.fString((String)map.get("PLANT"));
                        PLNTDESC     = StrUtils.fString((String)map.get("PLNTDESC"));
                        ISTAXREG     = StrUtils.fString((String)map.get("ISTAXREGISTRED"));
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
                        STATE   = StrUtils.fString((String)map.get("STATE"));
                        COUNTY   = StrUtils.fString((String)map.get("COUNTY"));
                        ZIP   = StrUtils.fString((String)map.get("ZIP"));
                        REMARKS   = StrUtils.fString((String)map.get("REMARKS"));
                        FAX   = StrUtils.fString((String)map.get("FAX"));
                        RCBNO = StrUtils.fString((String)map.get("RCBNO"));
                        TAXBY = StrUtils.fString((String)map.get("TAXBY"));
                        TAXBYLABEL = StrUtils.fString((String)map.get("TAXBYLABEL"));
						TAXBYLABELORDERMANAGEMENT = StrUtils.fString((String)map.get("TAXBYLABELORDERMANAGEMENT"));
                        ENABLEINVENTORY = StrUtils.fString((String)map.get("ENABLE_INVENTORY"));
                        ENABLEACCOUNTING = StrUtils.fString((String)map.get("ENABLE_ACCOUNTING"));
                        DECIMALPRECISION = StrUtils.fString((String)map.get("NUMBEROFDECIMAL"));
                        IMAGEPATH = StrUtils.fString((String)map.get("IMAGEPATH"));
	}
	  
	  if(action.equalsIgnoreCase("NEW")){
	      
	      PLNTDESC="";
	      REPROTSBASIS="";
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
	      ENABLEINVENTORY = "0";
	      ENABLEACCOUNTING = "0";
	      DECIMALPRECISION="2";
    }       
	
%>

<div class="container-fluid m-t-20">
	 <div class="box"> 
            <div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title"><%=title %></h1>
		</div>
		
 <div class="box-body">
 
  <CENTER><strong><%=res%></font></strong></CENTER>
 
 

 <form class="form-horizontal" name="form1" method="post">
   <div class="col-sm-8">
   <div class="form-group">
      <label class="control-label col-sm-4" for="Company ID">Company ID:</label>
      <div class="col-sm-4">
      <input name="PLANT_CODE" type="hidden" value=""
			size="30" MAXLENGTH=10 class="form-control">
       <input name="PLANT" type="TEXT" value="<%=Plant%>"
			size="30" MAXLENGTH=10 class="form-control" readonly>
			</div>
			</div>
					
<div class="form-group">
      <label class="control-label col-sm-4" for="Company Name">
      <i class="glyphicon glyphicon-star" style="font-size: 10px; top:-6px; color: #e50000"></i>&nbsp;Company Name:</label>
      <div class="col-sm-4">
	  <input name="PLNTDESC" type="TEXT" value="<%=PLNTDESC%>"
			size="30" MAXLENGTH=50 class="form-control">
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
  				<%if(REPROTSBASIS.equalsIgnoreCase("Accrual")) {%> 
  				<div>
  				 <input name="REPROTSBASIS" id="reportbasisaccru"  type="radio" value="Accrual" checked>
  			 	 <label class="control-label">Accrual <span class="muted">(you owe tax as of invoice date)</span></label> 
  				  
  				</div>
  				<div>
  				<input name="REPROTSBASIS" id="reportbasiscash"  type="radio" value="Cash">
  				<label class="control-label">Cash <span class="muted">(you owe tax upon payment receipt)</span></label> 
  				 
  				</div>
  			  <%} else {%>
  			  	<div>
  				 <input name="REPROTSBASIS" id="reportbasisaccru"  type="radio" value="Accrual">
  			 	 <label class="control-label">Accrual <span class="muted">(you owe tax as of invoice date)</span></label> 
  				  
  				</div>
  				<div>
  				<input name="REPROTSBASIS" id="reportbasiscash"  type="radio" value="Cash" checked>
  				<label class="control-label">Cash <span class="muted">(you owe tax upon payment receipt)</span></label> 
  				 
  				</div>
  			  <%} %>
  			
  		</div>
  </div>			
<div class="form-group">
        <label class="control-label col-sm-4" for="TRN/RCB/Tax No." id="TaxLabel"></label>
      <div class="col-sm-4">
	  <input name="RCBNO" type="TEXT" value="<%=RCBNO%>"
			size="30" MAXLENGTH=30 class="form-control">
			</div>
			</div>

			
<div class="form-group">
      <label class="control-label col-sm-4" for="Tax By">
      <i class="glyphicon glyphicon-star" style="font-size: 10px; top:-6px; color: #e50000"></i>&nbsp;Tax By:</label>
      <div class="col-sm-4">
      <div class="input-group">
      <SELECT class="btn btn-default dropdown-toggle" data-toggle="dropdown" data-placement="right" NAME="TAXBY" size="1">
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
      <label class="control-label col-sm-4" for="Tax By Label">
      <i class="glyphicon glyphicon-star" style="font-size: 10px; top:-6px; color: #e50000"></i>&nbsp;Tax By Label Order Configuration:</label>
      <div class="col-sm-4">
      <div class="input-group">
      <SELECT onchange="getval()" id="select_id" class="btn btn-default dropdown-toggle" data-toggle="dropdown" data-placement="right" NAME="TAXBYLABEL" size="1">
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
      <label class="control-label col-sm-4" for="Tax By Label Order Management">
      <i class="glyphicon glyphicon-star" style="font-size: 10px; top:-6px; color: #e50000"></i>&nbsp;Tax By Label Order Management:</label>
      <div class="col-sm-4">
      <div class="input-group">
      <SELECT onchange="getval()" id="select_id" class="btn btn-default dropdown-toggle" data-toggle="dropdown" data-placement="right" NAME="TAXBYLABELORDERMANAGEMENT" size="1">
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
<div class="form-group">
      <label class="control-label col-sm-4" for="Start Date">Start Date:</label>
      <div class="col-sm-4">
	  <input name="STARTDATE" type="TEXT" value="<%=STARTDATE%>"
			size="30" MAXLENGTH=20 class="form-control" readonly>
			</div>
			</div>
			
<div class="form-group">
      <label class="control-label col-sm-4" for="Expiry Date">Expiry Date:</label>
      <div class="col-sm-4">
		<input name="EXPIRYDATE" type="TEXT" value="<%=EXPIRYDATE%>"
			size="30" MAXLENGTH=20 class="form-control" readonly>
			</div>
			</div>
<div class="form-group">
      <label class="control-label col-sm-4" for="Actual Expiry Date">Actual Expiry Date:</label>
      <div class="col-sm-4">
	  <input name="ACTUALEXPIRYDATE" type="TEXT" value="<%=ACTUALEXPIRYDATE%>"
			size="30" MAXLENGTH=20 class="form-control" readonly>
			</div>
			</div>
			
<div class="form-group">
      <label class="control-label col-sm-4" for="Contact Person">Contact Person:</label>
      <div class="col-sm-4">
	  <input name="NAME" type="TEXT" value="<%=NAME%>"
			size="30" onchange="javascript:checkDate();"MAXLENGTH=100 class="form-control">
			</div>
			</div>
			
<div class="form-group">
      <label class="control-label col-sm-4" for="Designation">Designation:</label>
      <div class="col-sm-4">
	  <input name="DESGINATION" type="TEXT" value="<%=DESGINATION%>"
			size="30" MAXLENGTH=30 class="form-control">
			</div>
			</div>
			
<div class="form-group">
      <label class="control-label col-sm-4" for="Telephone No.">Telephone No.:</label>
      <div class="col-sm-4">
	  <input name="TELNO" type="TEXT" value="<%=TELNO%>"
			size="30" MAXLENGTH=20 class="form-control">
			</div>
			</div>
			
<div class="form-group">
      <label class="control-label col-sm-4" for="Hand Phone No.">Hand Phone No.:</label>
      <div class="col-sm-4">
	  <input name="HPNO" type="TEXT" value="<%=HPNO%>"
			size="30" MAXLENGTH=20 class="form-control">
			</div>
			</div>
			
<div class="form-group">
      <label class="control-label col-sm-4" for="Fax">Fax:</label>
      <div class="col-sm-4">
	  <input name="FAX" type="TEXT" value="<%=FAX%>"
			size="30" MAXLENGTH=20 class="form-control">
			</div>
			</div>
			
<div class="form-group">
      <label class="control-label col-sm-4" for="Email">Email:</label>
      <div class="col-sm-4">
	  <input name="EMAIL" type="TEXT" value="<%=EMAIL%>"
			size="30" MAXLENGTH=50 class="form-control">
			</div>
			</div>
			
<div class="form-group">
      <label class="control-label col-sm-4" for="Unit No">
      <i class="glyphicon glyphicon-star" style="font-size: 10px; top:-6px; color: #e50000"></i>&nbsp;Unit No:</label>
      <div class="col-sm-4">
	  <input name="ADD1" type="TEXT" value="<%=ADD1%>"
			size="30" MAXLENGTH=40 class="form-control">
			</div>
			</div>
			
			
<div class="form-group">
      <label class="control-label col-sm-4" for="Building">Building:</label>
      <div class="col-sm-4">
	  <input name="ADD2" type="TEXT" value="<%=ADD2%>"
			size="30" MAXLENGTH=40 class="form-control">
			</div>
			</div>
			
				
<div class="form-group">
      <label class="control-label col-sm-4" for="Street">
      <i class="glyphicon glyphicon-star" style="font-size: 10px; top:-6px; color: #e50000"></i>&nbsp;Street:</label>
      <div class="col-sm-4">
	  <input name="ADD3" type="TEXT" value="<%=ADD3%>"
			size="30" MAXLENGTH=40 class="form-control">
			</div>
			</div>
			
<div class="form-group">
      <label class="control-label col-sm-4" for="City">City:</label>
      <div class="col-sm-4">
	  <input name="ADD4" type="TEXT" value="<%=ADD4%>"
			size="30" MAXLENGTH=40 class="form-control">
			</div>
			</div>
			
			<div class="form-group">
      <label class="control-label col-sm-4" for="State">State:</label>
      <div class="col-sm-4">
	  <input name="STATE" type="TEXT" value="<%=STATE%>"
			size="30" MAXLENGTH=40 class="form-control">
			</div>
			</div>
			
<div class="form-group">
      <label class="control-label col-sm-4" for="Country">
      <i class="glyphicon glyphicon-star" style="font-size: 10px; top:-6px; color: #e50000"></i>&nbsp;Country:</label>
      <div class="col-sm-4">
	  <input name="COUNTY" type="TEXT" value="<%=COUNTY%>"
			size="30" MAXLENGTH=40 class="form-control">
			</div>
			</div>
			
<div class="form-group">
      <label class="control-label col-sm-4" for="Postal Code">
      <i class="glyphicon glyphicon-star" style="font-size: 10px; top:-6px; color: #e50000"></i>&nbsp;Postal Code:</label>
      <div class="col-sm-4">
	  <input name="ZIP" type="TEXT" value="<%=ZIP%>"
			size="30" MAXLENGTH=10 class="form-control">
			</div>
			</div>
				
<div class="form-group">
      <label class="control-label col-sm-4" for="Remarks">Remarks:</label>
      <div class="col-sm-4">
	  <input name="REMARKS" type="TEXT" value="<%=REMARKS%>"
			size="30" MAXLENGTH=100 class="form-control">
			</div>
			</div>

	<div class="form-group">
      <label class="control-label col-sm-4" for="Enabled Modules">Enabled Modules</label>
       <div class="col-sm-4" style="padding-top: 7px;"><%if (Integer.parseInt(ENABLEINVENTORY) == 1){%>Inventory<input type="hidden" value="i" name="ENABLE_INVENTORY" id="ENABLE_INVENTORY" />,<%}%>
       						 <%if (Integer.parseInt(ENABLEACCOUNTING) == 1){%>Accounting<input type="hidden" value="a" name="ENABLE_ACCOUNTING" id="ENABLE_ACCOUNTING" /><%}%>           	 			
		</div>
		</div>
		<div class="form-group">
					<label class="control-label col-sm-4" for="decimal_precision">Number
						of Decimals </label>
					<div class="col-sm-4">
						<SELECT class="form-control" data-toggle="dropdown" data-placement="right"
							name="decimal_precision">
							<option value="2" <%if(DECIMALPRECISION.equals("2")){%> selected <%}%>>2</option>
							<option value="3" <%if(DECIMALPRECISION.equals("3")){%> selected <%}%>>3</option>
							<option value="4" <%if(DECIMALPRECISION.equals("4")){%> selected <%}%>>4</option>
							<option value="5" <%if(DECIMALPRECISION.equals("5")){%> selected <%}%>>5</option>
						</SELECT>
					</div>
				</div>
<div class="form-group">        
     <div class="col-sm-offset-4 col-sm-8">
     <button type="button" class="Submit btn btn-default" onClick="window.location.href='../home'"><b>Back</b></button>&nbsp;&nbsp;
	 <!-- <button type="button" class="Submit btn btn-default" onClick="{backNavigation('settings.jsp','UA');}"><b>Back</b></button>&nbsp;&nbsp; -->
     <button type="button" class="Submit btn btn-default" onClick="return onNew()"><b>Clear</b></button>&nbsp;&nbsp;
     <button type="button" class="Submit btn btn-default" onClick="onAdd();"><b>Save</b></button>&nbsp;&nbsp;
	 </div>
	 </div>
	 </div>
	 <div class="col-sm-4 text-center">
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
					<label>Upload Image:</label> <INPUT class="form-control btn-sm"  name="IMAGE_UPLOAD"  type="File" size="20" id ="productImg" MAXLENGTH=100> 
					</div>
					 <div class="form-group">
					<b><INPUT class=" btn btn-sm btn-default" type="BUTTON" value="Remove Image" onClick="image_delete();"></b> 
					<b><INPUT class=" btn btn-sm btn-default" type="BUTTON" value="Upload & Save Image" onClick="image_edit();"></b>
				</div>
			</div>
		</div>
	</div>
	
	  </form>
	    </div>
</div>
</div>
    
<script>
        $(document).ready(function(){
        	var  d = document.getElementById("select_id").value;
            document.getElementById('TaxLabel').innerHTML = d + " No.:";
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
        </script>
	
<jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
</jsp:include>