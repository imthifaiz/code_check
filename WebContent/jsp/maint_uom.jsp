<%@ page import="com.track.util.*"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.gates.*"%>
<%@ page import="com.track.constants.*"%>
<%@ page import="com.track.dao.MovHisDAO"%>
<%@ page import="com.track.dao.TblControlDAO"%>
<%@ page import="com.track.dao.ItemMstDAO"%>
<jsp:useBean id="UomDAO"  class="com.track.dao.UomDAO" />
<%@ include file="header.jsp"%>

<%
String title = "Edit Unit of Measure";
String PLANT = (String)session.getAttribute("PLANT");
%>
<%@include file="sessionCheck.jsp" %>
<jsp:include page="header2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
	<jsp:param name="mainmenu" value="<%=IConstants.SETTINGS%>"/>
	<jsp:param name="submenu" value="<%=IConstants.SYSTEM_MASTER%>"/>
</jsp:include>
<link rel="stylesheet" href="../jsp/css/typeahead.css">
<link rel="stylesheet" href="../jsp/css/accounting.css">
<script language="JavaScript" type="text/javascript" src="../jsp/js/general.js"></script>

<SCRIPT LANGUAGE="JavaScript">

function popWin(URL) {
 subWin = window.open(URL, 'PRODUCT', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=800,height=400,left = 200,top = 184');
}
function onNew(){
	document.form1.ITEM_ID.value = ""; 
	document.form1.ITEM_DESC.value = "";
	document.form1.Display.value = ""; 
	document.form1.QPUOM.value = "";
   /* document.form1.action  = "maint_uom.jsp?action=Clear";
   document.form1.submit(); */
}
function onAdd(){
   var ITEM_ID   = document.form1.ITEM_ID.value;
    if(ITEM_ID == "" || ITEM_ID == null) {alert("Please Enter UOM  "); return false; }
   document.form1.action  = "UomMaster.jsp?action=ADD";
   document.form1.submit();
}
function onUpdate(){
 
    var ITEM_ID   = document.form1.ITEM_ID.value;
	var Display   = document.form1.Display.value;
    var QPUOM   = document.form1.QPUOM.value;
	
    if(ITEM_ID == "" || ITEM_ID == null) {alert("Please Enter UOM  ");document.form1.ITEM_ID.focus(); return false; }
    if(ITEM_ID=='PCS') { if(QPUOM!=1){ alert("Update Not Possible for PCS UOM"); return false; }}
	else if(Display == "" || Display == null) 
    {
    	alert("Please Enter Display for UOM");
    	document.form1.Display.focus();
    	return false; 
    	}
    else if(!IsNumeric(document.form1.QPUOM.value))
    {
      alert("Please Enter valid Quantity Per UOM");
      form1.QPUOM.focus(); form1.QPUOM.select();
      return false;
    }
    else if(QPUOM == "" || QPUOM <= 0)
    {
        alert("Please Enter Quantity Per UOM");
        document.form1.QPUOM.focus();
        return false;
      }else{
    	 if (QPUOM != 1){
    		 if(CUOM == "" || CUOM <= 0)
    		    {
    			 	alert("Please select Convertible UOM");
    		        document.form1.CUOM.focus();
    		        return false;
    		    }
    	 }
    }
      
     var radio_choice = false;

// Loop from zero to the one minus the number of radio button selections
for (i = 0; i < document.form1.ACTIVE.length; i++)
{
if (document.form1.ACTIVE[i].checked)
radio_choice = true; 
}

if (!radio_choice)
{
// If there were no selections made display an alert box 
alert("Please select Active or non Active mode.")
return (false);
}
	var chk = confirm("Are you sure you would like to save?");
	if(chk){   
   document.form1.action  = "../jsp/maint_uom.jsp?action=UPDATE";
   document.form1.submit();}
	else
	{
		return false;
	}
}
function onDelete(){
      var ITEM_ID   = document.form1.ITEM_ID.value;
      if(ITEM_ID == "" || ITEM_ID == null) {alert("Please Enter UOM "); return false; }
      if(ITEM_ID=='PCS') {alert("Delete Not Possible for PCS "); return false; }    	  
    var chkmsg=confirm("Are you sure you would like to delete?");    
    if(chkmsg){
   document.form1.action  = "../jsp/maint_uom.jsp?action=DELETE";
   document.form1.submit();}
   else
   {
   return false;
   }
}
function onView(){
    var ITEM_ID   = document.form1.ITEM_ID.value;
   if(ITEM_ID == "" || ITEM_ID == null) {alert("Please Enter UOM "); return false; }

   document.form1.action  = "../jsp/maint_uom.jsp?action=VIEW";
   document.form1.submit();
}
function onGenID(){
     
   document.form1.action  = "../jsp/maint_uom.jsp?action=Auto_ID";
   document.form1.submit();
}

</script>
<%
String sUserId = (String) session.getAttribute("LOGIN_USER");
String sPlant = (String) session.getAttribute("PLANT");
String res        = "";

String sNewEnb    = "enabled";
String sDeleteEnb = "enabled";
String sAddEnb    = "disabled";
String sUpdateEnb = "enabled";
sAddEnb    = "enabled";
String action     =   "";
String sItemId ="",sPrdClsId  =   "",isActive="",sCUomFlag="",sCUom="",
       sItemDesc  = "",sDisplay = "",sQtyPerUom = "",sSAVE_RED,sSAVE_REDELETE;

session= request.getSession();
StrUtils strUtils = new StrUtils();
UomUtil uomUtil = new UomUtil();
DateUtils dateutils = new DateUtils();

uomUtil.setmLogger(mLogger);

action            = strUtils.fString(request.getParameter("action"));
String plant = (String)session.getAttribute("PLANT");
sItemId  = strUtils.fString(request.getParameter("ITEM_ID"));
sItemDesc  = strUtils.fString(request.getParameter("ITEM_DESC"));
String result = StrUtils.fString(request.getParameter("result"));
isActive= strUtils.fString(request.getParameter("ACTIVE"));
sDisplay = strUtils.fString(request.getParameter("Display"));
sQtyPerUom = strUtils.fString(request.getParameter("QPUOM"));
sCUomFlag = strUtils.fString(request.getParameter("CUOMFLAG"));
sCUom = strUtils.fString(request.getParameter("CUOM"));
if(sItemId.length() <= 0) sItemId  = strUtils.fString(request.getParameter("ITEM_ID1"));
sSAVE_RED  = strUtils.fString(request.getParameter("SAVE_RED"));
sSAVE_REDELETE = strUtils.fString(request.getParameter("SAVE_REDELETE"));

//1. >> New
if(action.equalsIgnoreCase("Clear")){
      sItemId  = "";
      sItemDesc  = "";
      sPrdClsId ="";
	  isActive="";
	  sDisplay ="";
	  sQtyPerUom ="";
}

//3. >> Update
else if(action.equalsIgnoreCase("UPDATE"))  {
   
    sAddEnb  = "disabled";
     result="";
    Hashtable ht = new Hashtable();
    ht.put(IDBConstants.UOMCODE,sItemId);
    ht.put(IDBConstants.PLANT,sPlant);
    if((uomUtil.isExistsUom(ht)))
    {
          Hashtable htUpdate = new Hashtable();
          htUpdate.put(IDBConstants.UOMCODE,sItemId);
          htUpdate.put(IDBConstants.UOMDESC,sItemDesc);   
       
          htUpdate.put(IConstants.ISACTIVE,isActive);
          htUpdate.put(IDBConstants.UPDATED_AT,new DateUtils().getDateTime());
          htUpdate.put(IDBConstants.UPDATED_BY,sUserId);
          
          Hashtable htCondition = new Hashtable();
          htCondition.put(IDBConstants.UOMCODE,sItemId);
          htCondition.put(IDBConstants.PLANT,sPlant);
          
           MovHisDAO mdao = new MovHisDAO(sPlant);
           mdao.setmLogger(mLogger);
           Hashtable htm = new Hashtable();
           htm.put(IDBConstants.PLANT, sPlant);
		
           htm.put(IDBConstants.DIRTYPE,TransactionConstants.UPD_UOM);
           htm.put("RECID","");
           htm.put("ITEM",sItemId);
           htm.put(IDBConstants.UPBY, sUserId);
           htm.put(IDBConstants.REMARKS, sItemDesc);
		   htm.put(IDBConstants.CREATED_BY, sUserId);
		   htm.put(IDBConstants.CREATED_AT, dateutils.getDateTime());
		   htm.put(IDBConstants.UPDATED_AT, dateutils.getDateTime());
		   htm.put(IDBConstants.TRAN_DATE, dateutils
					.getDateinyyyy_mm_dd(dateutils.getDate()));
		   htUpdate.put("Display",sDisplay);
		   /*if(sQtyPerUom == "")
			{
				sQtyPerUom = "0";
			}*/
		   htUpdate.put("QPUOM",sQtyPerUom);
			if(Double.valueOf(sQtyPerUom) != 1)
			{
				htUpdate.put("ISCONVERTIBLE", "1");
				htUpdate.put("CUOM", sCUom);
			}else{
				htUpdate.put("ISCONVERTIBLE", "0");
				htUpdate.put("CUOM", sItemId);
			}
                   
        boolean Updated = uomUtil.updateUom(htUpdate,htCondition);
        boolean  inserted = mdao.insertIntoMovHis(htm);
          if(true) {
        	  sSAVE_RED = "Update";
                    /* res = "<font class = "+IDBConstants.SUCCESS_COLOR +" >UOM  Updated Successfully</font>"; */
          } else {
        	  sSAVE_RED="Failed to Update UOM ";
                   /*  res = "<font class = "+IDBConstants.FAILED_COLOR +" >Failed to Update UOM </font>"; */
          }
    }else{
    	sSAVE_RED="UOM  doesn't not Exists. Try again ";
//            res = "<font class = "+IDBConstants.FAILED_COLOR +">UOM  doesn't not Exists. Try again</font>";

    }
      
}

else if(action.equalsIgnoreCase("DELETE")){
	ItemMstDAO itemmstdao = new ItemMstDAO();
	Hashtable ht = new Hashtable();
	ht.put("STKUOM",sItemId);
	ht.put(IDBConstants.PLANT,sPlant);
	boolean itemtypeflag  = itemmstdao.isExisit(ht,"");
	if (itemtypeflag) {
		sSAVE_REDELETE="UOM Exists In Products";
		/* res = "<font class = " + IDBConstants.FAILED_COLOR
				+ " >UOM Exists In Products</font>"; */
	}
	else{
	
	Hashtable htcondition = new Hashtable();
	htcondition.put(IDBConstants.UOMCODE,sItemId);
	htcondition.put(IDBConstants.PLANT,sPlant);
	
	
	if(uomUtil.isExistsUom(htcondition))
	 {
		
		
		boolean flag = uomUtil.deleteUom(htcondition);
		
		 MovHisDAO mdao = new MovHisDAO(sPlant);
         mdao.setmLogger(mLogger);
         Hashtable htm = new Hashtable();
         htm.put(IDBConstants.PLANT, sPlant);
		
         htm.put(IDBConstants.DIRTYPE,TransactionConstants.DEL_UOM);
         htm.put("RECID","");
         htm.put("ITEM",sItemId);
         htm.put(IDBConstants.UPBY, sUserId);
         htm.put(IDBConstants.REMARKS, sItemDesc);
		 htm.put(IDBConstants.CREATED_BY, sUserId);
		 htm.put(IDBConstants.CREATED_AT, dateutils.getDateTime());
		 htm.put(IDBConstants.UPDATED_AT, dateutils.getDateTime());
		 htm.put(IDBConstants.TRAN_DATE, dateutils
					.getDateinyyyy_mm_dd(dateutils.getDate()));
          
			flag = mdao.insertIntoMovHis(htm);
      
		if(flag)
			{
			sSAVE_REDELETE = "Delete";
			/* res = "<font class = " + IDBConstants.SUCCESS_COLOR
				+ " >UOM Deleted Successfully </font>"; */
				}
		else {
			sSAVE_REDELETE="Failed to Delete UOM";
//             res = "<font class = "+IConstants.FAILED_COLOR+">Failed to Delete UOM</font>";
            
  			}
	}else{
		sSAVE_REDELETE="UOM doesn't  Exists. Try again";
         /*   res = "<font class = "+IConstants.FAILED_COLOR+">UOM doesn't  Exists. Try again</font>"; */
    }
	}
	
}

//4. >> View
else if(action.equalsIgnoreCase("VIEW")){
    Map map  = uomUtil.getUomDetails(sItemId);
   if(map.size()>0){
    sItemId   = strUtils.fString((String)map.get("UOM"));
    sItemDesc   = strUtils.fString((String)map.get("UOMDESC"));
   isActive   = strUtils.fString((String)map.get("ISACTIVE"));
   sDisplay = strUtils.fString((String) map.get("Display"));
   sQtyPerUom = strUtils.fString((String) map.get("QPUOM"));
    }else{
     res = "<font class = "+IDBConstants.FAILED_COLOR +">UOM doesn't not Exists. Try again</font>";
    }
}


if(!result.equalsIgnoreCase("")) {
	sSAVE_RED = "";
	sSAVE_REDELETE = "";
	res = "<font class = " + IDBConstants.FAILED_COLOR
	+ ">"+result+"</font>";
	}	

%>


<div class="container-fluid m-t-20">
	 <div class="box"> 
	 <!-- Thanzith Modified on 23.02.2022 -->
	 <ul class="breadcrumb backpageul">    
                <li class="underline-on-hover"><a href="../home">Dashboard</a></li>	
              <li><a href="../uom/summary"><span class="underline-on-hover">UOM Summary</span></a></li>                    
                <li><label>Edit Unit Of Measure</label></li>                                   
            </ul>
        <!-- Thanzith Modified on 23.02.2022 -->     
            <div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title"><%=title %></h1>
              <h1 style="font-size: 18px; cursor: pointer;"
				class="box-title pull-right"
				onclick="window.location.href='../uom/summary'">
				<i class="glyphicon glyphicon-remove"></i>
			</h1>
		</div>
		
 <div class="box-body">
 
   <CENTER><strong><%=res%></strong></CENTER>

<!-- <B><CENTER></CENTER></B> -->
  <form class="form-horizontal" name="form1" method="post" >
    <div class="form-group">
      <label class="control-label col-form-label col-sm-2 required" for="Product UOM ID">Unit Of Measure</label>
      <!-- <i class="glyphicon glyphicon-star" style="font-size: 10px; top:-6px; color: #e50000"></i>&nbsp;Unit Of Measure</label> -->
      <div class="col-sm-4">
      	<div class="input-group">    
    		<input name="ITEM_ID" type="TEXT" value="<%=sItemId%>"
			size="50" MAXLENGTH=14 class="form-control" readonly>
   		 	<!-- <span class="input-group-addon"  
   		 	onClick="javascript:popWin('../jsp/list/UomMstList.jsp?ITEM_ID='+form1.ITEM_ID.value);">
   		 	<a href="#"  data-toggle="tooltip" data-placement="top" title="UOM Details">
   		 	<i class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span> -->
  		</div>
  	   <INPUT type="hidden" name="ITEM_ID1" value=""> 
  	   <INPUT type="hidden" name="SAVE_RED" value="<%=sSAVE_RED%>">
      <INPUT type="hidden" name="SAVE_REDELETE" value="<%=sSAVE_REDELETE%>">
      </div>
    </div>
    
    <div class="form-group">
      <label class="control-label col-form-label col-sm-2" for="UOM Description">Unit Of Measure Description</label>
      <div class="col-sm-4">          
        <INPUT  class="form-control" name="ITEM_DESC" type="TEXT" value="<%=sItemDesc%>"
			size="50" MAXLENGTH=50>
      </div>
    </div>
	
	<div class="form-group">
      <label class="control-label col-form-label col-sm-2 required" for="Display">Display</label>
      <div class="col-sm-4">          
        <INPUT  class="form-control" name="Display" type="TEXT" value="<%=sDisplay%>"
			size="50" MAXLENGTH=14>
      </div>
      <font><i class="glyphicon glyphicon-star" style="font-size: 10px; top:-6px; color: #e50000"></i>&nbsp;<b>Note : To Display in Purchase,Sales,Direct Sales,Sales Estimate,Rental 
       and Transfer Order Invoice.</b></font>
    </div>
    
    <div class="form-group">
      <label class="control-label col-form-label col-sm-2 required" for="Qty Per UOM">Quantity Per UOM</label>
<!-- 	  <i class="glyphicon glyphicon-star" style="font-size: 10px; top:-6px; color: #e50000"></i>&nbsp;Quantity Per UOM:</label> -->
      <div class="col-sm-2">          
        <INPUT  class="form-control" name="QPUOM" type="TEXT" value="<%=sQtyPerUom%>"
			size="50" MAXLENGTH=50  onchange="checkno()" style="width: 120%;">
      </div>
      <div class="col-sm-2 squom" <%if(!sCUomFlag.equalsIgnoreCase("1")){ %>hidden<%} %>>
         <SELECT class="form-control " data-toggle="dropdown" data-placement="left" name="CUOM" id="CUOM" >
			
					<%
					
				  ArrayList ccList = UomDAO.getUOMList(PLANT);
					for(int i=0 ; i < ccList.size();i++)
		      		 {
						Map m=(Map)ccList.get(i);
						String uom = (String)m.get("UOM"); %>
						<%if(sCUomFlag.equalsIgnoreCase("1")){
							if(uom.equalsIgnoreCase(sCUom)){ %>
				        		<option selected value=<%=uom%> ><%=uom%>  </option>
				        	<%}else{%>
				        		<option value=<%=uom%>><%=uom%>  </option>
							<%}%>	          
		        		<%}else{%>
		        			<option value=<%=uom%>><%=uom%>  </option> 
						<%}%>
						
					<%}%>
	 </SELECT>
	 </div>
    </div>
    
    <div class="form-group">
  <div class="col-sm-offset-4 col-sm-8">
    <label class="radio-inline">
      <input type="radio" name="ACTIVE" value="Y"<%if (isActive.equalsIgnoreCase("Y")) {%> checked <%}%>>Active
    </label>
    <label class="radio-inline">
      <input type="radio" name="ACTIVE" value="N"<%if (isActive.equalsIgnoreCase("N")) {%> checked <%}%>>Non Active
    </label>
     </div>
</div>
    
    
    <div class="form-group">        
      <div class="col-sm-offset-4 col-sm-8">
      <!-- <button type="button" class="Submit btn btn-default" onClick="window.location.href='../home'"><b>Back</b></button>&nbsp;&nbsp; -->
      <!-- <button type="button" class="Submit btn btn-default" onClick="{backNavigation('settings.jsp','SM');}"><b>Back</b></button>&nbsp;&nbsp; -->
      	<button type="button" class="Submit btn btn-default" onClick="onNew();" <%=sNewEnb%>>Clear</button>&nbsp;&nbsp;
      	<button type="button" class="btn btn-success" data-toggle="modal" data-target="#myModal" onClick="onUpdate();" <%=sUpdateEnb%>>Save</button>&nbsp;&nbsp;
      	<button type="button" class="Submit btn btn-default" data-toggle="modal" data-target="#myModal" onClick="onDelete();">Delete</button>&nbsp;&nbsp;
      </div>
    </div>
  </form>
</div>
</div>
</div>



<script>
$(document).ready(function(){
    $('[data-toggle="tooltip"]').tooltip();
    if(document.form1.SAVE_RED.value!=""){
    if(document.form1.SAVE_RED.value!="") {
	document.form1.action  = "../uom/summary?PGaction=View&result=UOM Updated Successfully";	   
    document.form1.submit();
	}
    else{
    	document.form1.action  = "../uom/summary?PGaction=View&result=UOM Updated Successfully";	   
        document.form1.submit();
}
    }
});
$(document).ready(function(){
    $('[data-toggle="tooltip"]').tooltip();
    if(document.form1.SAVE_REDELETE.value!=""){
    if(document.form1.SAVE_REDELETE.value!=""){
    	document.form1.action  = "../uom/summary?PGaction=View&result=UOM Deleted Successfully";
    	 document.form1.submit();
	}
    else{
    	document.form1.action  = "../uom/summary?PGaction=View&result=UOM Deleted Successfully";
   	 document.form1.submit();
}
    }
});
$(document).ready(function(){
    $('[data-toggle="tooltip"]').tooltip();   
});

function checkno(){
	var baseamount = $("input[name=QPUOM]").val();
	var zeroval = "0";
	if(baseamount != ""){
		var decimal =  /^[-+]?[0-9]+\.[0-9]+$/; 
		var numbers = /^[0-9]+$/;
		if(baseamount.match(decimal) || baseamount.match(numbers)) 
		{ 
			$("input[name=QPUOM]").val(baseamount);
			if(baseamount == '1'){
				$(".squom").hide();
			}else{
				$(".squom").show();
			}
			
		}else{
			$("input[name=QPUOM]").val(zeroval);
			$(".squom").hide();
		}
	}else{
		$(".squom").hide();
		$("input[name=QPUOM]").val(zeroval);
	}
	
}
</script>

<jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
</jsp:include>
