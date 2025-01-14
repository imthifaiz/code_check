<%@ page import="com.track.util.*"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.gates.*"%>
<%@ page import="com.track.constants.*"%>
<%@ page import="com.track.dao.MovHisDAO"%>
<%@ page import="com.track.dao.ItemMstDAO"%>
<%@ page import="com.track.dao.TblControlDAO"%>
<%@ include file="header.jsp"%>

<%
String title = "Edit Product Category";
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
 subWin = window.open(URL, 'PRODUCT', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=800,height=420,left = 200,top = 184');
}
function onNew(){
	document.form1.PRD_CLS_ID.value = ""; 
	document.form1.PRD_CLS_DESC.value = "";
   /* document.form1.action  = "../jsp/maint_prdcls.jsp?action=Clear";
   document.form1.submit(); */
   
}

function onUpdate(){
 
    var ITEM_ID   = document.form1.PRD_CLS_ID.value;
    var PRD_CLS_DESC = document.form1.PRD_CLS_DESC.value;
    if(ITEM_ID == "" || ITEM_ID == null) {alert("Please Enter Product Category ID ");document.form1.PRD_CLS_ID.focus(); return false; }
    if(PRD_CLS_DESC == "" || PRD_CLS_DESC == null) {alert("Please Enter Product Category Description");document.form1.PRD_CLS_DESC.focus(); return false; }  
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
    
    var chkmsg=confirm("Are you sure you would like to update?");
    if(chkmsg){
   document.form1.action  = "../jsp/maint_prdcls.jsp?action=UPDATE";
   document.form1.submit();}
    else
    { return false;
    }
}
function onDelete() {
	var ITEM_ID   = document.form1.PRD_CLS_ID.value;
	if (ITEM_ID == "" || ITEM_ID == null) {
		alert("Please Enter Product CategoryID");
		return false;
	}
	var chkmsg = confirm("Are you sure you would like to delete?");
	if (chkmsg) {
		document.form1.action = "../jsp/maint_prdcls.jsp?action=DELETE";
		document.form1.submit();
	} else {
		return false;
	}
}


function onView(){
    var ITEM_ID   = document.form1.PRD_CLS_ID.value;
   if(ITEM_ID == "" || ITEM_ID == null) {alert("Please Enter Product CategoryID "); return false; }

   document.form1.action  = "../jsp/maint_prdcls.jsp?action=VIEW";
   document.form1.submit();
}


</script>
<%
	String sUserId = (String) session.getAttribute("LOGIN_USER");
	String sPlant = (String) session.getAttribute("PLANT");
	String res = "";

	String sNewEnb = "enabled";
	String sDeleteEnb = "enabled";
	String sAddEnb = "disabled";
	String sUpdateEnb = "enabled";
	sAddEnb = "enabled";
	String action = "";
	String sItemId = "", sPrdClsId = "", sItemDesc = "", isActive = "",catalogpath="",sSAVE_RED,sSAVE_REDELETE;

	session = request.getSession();
	StrUtils strUtils = new StrUtils();
	PrdClassUtil prdclsutil = new PrdClassUtil();
	ItemMstDAO itemmstdao = new ItemMstDAO();

	prdclsutil.setmLogger(mLogger);
	
	DateUtils dateutils = new DateUtils();
	action = strUtils.fString(request.getParameter("action"));
	String plant = (String) session.getAttribute("PLANT");
	String result = (String) request.getAttribute("result");
	sItemId = strUtils.fString(request.getParameter("PRD_CLS_ID"));
	sItemDesc = strUtils.fString(request.getParameter("PRD_CLS_DESC"));
	isActive = strUtils.fString(request.getParameter("ACTIVE"));
	sSAVE_RED  = strUtils.fString(request.getParameter("SAVE_RED"));
	sSAVE_REDELETE = strUtils.fString(request.getParameter("SAVE_REDELETE"));
	if (sItemId.length() <= 0)
		sItemId = strUtils.fString(request.getParameter("PRD_CLS_ID1"));
if(sItemId.length()>0) {
	ArrayList locQryList =  prdclsutil.getPrdTypeList(sItemId,plant,"");
	if (locQryList.size() > 0) {
		for(int i =0; i<locQryList.size(); i++) {
		Map arrCustLine = (Map)locQryList.get(i);
		catalogpath=(String)arrCustLine.get("CATALOG");
		}
	}
}
	//1. >> New
	if (action.equalsIgnoreCase("Clear")) {
		sItemId = "";
		sItemDesc = "";
		sPrdClsId = "";

	}

	//3. >> Update
	else if (action.equalsIgnoreCase("UPDATE")) {
     result="";
		sAddEnb = "disabled";
		Hashtable ht = new Hashtable();
		ht.put(IDBConstants.PRDCLSID, sItemId);
		ht.put(IDBConstants.PLANT, sPlant);
		if ((prdclsutil.isExistsItemType(ht))) {
			Hashtable htUpdate = new Hashtable();
			htUpdate.put(IDBConstants.PRDCLSID, sItemId);
			htUpdate.put(IDBConstants.PRDCLSDESC,  strUtils.InsertQuotes(sItemDesc));
			htUpdate.put(IConstants.ISACTIVE, isActive);
			htUpdate.put(IDBConstants.UPDATED_AT, new DateUtils().getDateTime());
			htUpdate.put(IDBConstants.UPDATED_BY, sUserId);

			Hashtable htCondition = new Hashtable();
			htCondition.put(IDBConstants.PRDCLSID, sItemId);
			htCondition.put(IDBConstants.PLANT, sPlant);

			MovHisDAO mdao = new MovHisDAO(sPlant);
			mdao.setmLogger(mLogger);
			Hashtable htm = new Hashtable();
			htm.put("PLANT", sPlant);
			htm.put("DIRTYPE", TransactionConstants.UPDATE_PRDCLS);
			htm.put("RECID", "");
			htm.put("ITEM",sItemId);
			htm.put("UPBY", sUserId);
			htm.put("CRBY", sUserId);
			htm.put("CRAT", dateutils.getDateTime());
			htm.put("REMARKS",  strUtils.InsertQuotes(sItemDesc));
			htm.put("UPAT", dateutils.getDateTime());
			htm.put(IDBConstants.TRAN_DATE, dateutils
					.getDateinyyyy_mm_dd(dateutils.getDate()));

			boolean Updated = prdclsutil.updatePrdtypeId(htUpdate,
					htCondition);
			boolean inserted = mdao.insertIntoMovHis(htm);
			if (true) {
				 sSAVE_RED = "Update";
				/* res = "<font class = "
						+ IDBConstants.SUCCESS_COLOR
						+ " >Product Class  Updated Successfully</font>"; */
			} else {
				sSAVE_RED="Failed to Update Product Category";
				/* res = "<font class = " + IDBConstants.FAILED_COLOR
						+ " >Failed to Update Product Class </font>"; */
			}
		} else {
			sSAVE_RED="Product Category  doesn't not Exists. Try again";
			/* res = "<font class = "
					+ IDBConstants.FAILED_COLOR
					+ ">Product Class  doesn't not Exists. Try again</font>"; */

		}

	}
	
	else if(action.equalsIgnoreCase("DELETE")){
		result="";
		Hashtable htCondition = new Hashtable();
		htCondition.put(IDBConstants.PRDCLSID, sItemId);
		htCondition.put(IDBConstants.PLANT, sPlant);
		
		boolean itemclassflag  = itemmstdao.isExisit(htCondition,"");
		if (itemclassflag) {
			/* res = "<font class = " + IDBConstants.FAILED_COLOR
					+ " >Product Class Exists In Products</font>"; */
			sSAVE_REDELETE="Product Category Exists In Products";
		}
		
		else{
			if(prdclsutil.isExistsItemType(htCondition))
		 	{
				boolean flag = prdclsutil.deletePrdTypeId(htCondition);
				
				MovHisDAO mdao = new MovHisDAO(sPlant);
				mdao.setmLogger(mLogger);
				Hashtable htm = new Hashtable();
				htm.put("PLANT", sPlant);
				htm.put("DIRTYPE", TransactionConstants.DEL_PRDCLS);
				htm.put("RECID", "");
				htm.put("ITEM",sItemId);
				htm.put("UPBY", sUserId);
				htm.put("CRBY", sUserId);
				htm.put("CRAT", dateutils.getDateTime());
				htm.put("REMARKS",  strUtils.InsertQuotes(sItemDesc));
				htm.put("UPAT", dateutils.getDateTime());
				htm.put(IDBConstants.TRAN_DATE, dateutils
						.getDateinyyyy_mm_dd(dateutils.getDate()));

				 flag = mdao.insertIntoMovHis(htm);
				
				if(flag)
					{
					sSAVE_REDELETE = "Delete";
					/* res = "<font class = " + IDBConstants.SUCCESS_COLOR
						+ " >Product Class Deleted Successfully </font>"; */
						}
				else {
					sSAVE_REDELETE="Failed to Delete Product Category";
//                 	res = "<font class = "+IConstants.FAILED_COLOR+">Failed to Delete Product Class</font>";
                
      				}
			}else{
				sSAVE_REDELETE="Product Category doesn't  Exists. Try again";
// 	           res = "<font class = "+IConstants.FAILED_COLOR+">Product Class doesn't  Exists. Try again</font>";
	        }
		

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
                <li><a href="../productclass/summary"><span class="underline-on-hover">Product Category Summary</span></a></li>                      
                <li><label>Edit Product Category</label></li>                                   
            </ul>
        <!-- Thanzith Modified on 23.02.2022 -->     
            <div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title"><%=title %></h1>
              <h1 style="font-size: 18px; cursor: pointer;"
				class="box-title pull-right"
				onclick="window.location.href='../productclass/summary'">
				<i class="glyphicon glyphicon-remove"></i>
			</h1>
		</div>
		
 <div class="box-body">
 
 
 
   <CENTER><strong><%=res%></strong></CENTER>


<form class="form-horizontal" name="form1" method="post" id="form">
    <div class="form-group">
      <label class="control-label col-form-label col-sm-4 required" for="Product Class ID">Product Category ID</label>
<!--       <i class="glyphicon glyphicon-star" style="font-size: 10px; top:-6px; color: #e50000"></i>&nbsp;Product Class ID:</label> -->
      <div class="col-sm-4">
      	<div class="input-group">    
    		<input name="PRD_CLS_ID" type="TEXT" value="<%=sItemId%>"
			size="50" MAXLENGTH=100 class="form-control" readonly>
			<!-- <span class="input-group-addon" 
   		  onClick="javascript:popWin('../jsp/PrdClsList.jsp?ITEM_ID='+form1.PRD_CLS_ID.value);"> 	
   		 	<a href="#" data-toggle="tooltip" data-placement="top" title="Product Category Details">
   		 	<i class="glyphicon glyphicon-log-in" style="font-size: 20px;"></i></a></span> -->
        </div>
  		<INPUT type="hidden" name="PRD_CLS_ID1" value="">
  		 <INPUT type="hidden" name="SAVE_RED" value="<%=sSAVE_RED%>">
      <INPUT type="hidden" name="SAVE_REDELETE" value="<%=sSAVE_REDELETE%>">
  	</div>
   </div>
   
<div class="form-group">
      <label class="control-label col-form-label col-sm-4 required" for="Product Category Description">Product Category Description</label>
<!--       <i class="glyphicon glyphicon-star" style="font-size: 10px; top:-6px; color: #e50000"></i>&nbsp;Product Class Description:</label> -->
      <div class="col-sm-4">          
        <INPUT  class="form-control" name="PRD_CLS_DESC" type="TEXT" value="<%=sItemDesc%>"
			size="50" MAXLENGTH=100>
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

		<div class="row">
			<div class="col-sm-offset-4 col-sm-4">				
				<img src="../jsp/dist/img/NO_IMG.png" id="item_img" name="CATALOGPATH" alt="new image"
				class="img-thumbnail img-responsive col-sm-3" style="width: 70%;float:revert; padding: 3px;">
			</div>
		</div>
	<div class="row">
			<div class="col-sm-offset-4 col-sm-4">
				<div class="form-group" id="btnUpload">
					<label>Upload Product Category Image:</label> <INPUT style="width: 100%;" class="form-control btn-sm"  name="IMAGE_UPLOAD"  type="File" size="20" id ="productImg" MAXLENGTH=100>  
					<br> 
					<INPUT class=" btn btn-default" style="font-size: 85%;" type="BUTTON" value="Remove Product Category Image" onClick="image_delete();"> 
					<INPUT class=" btn btn-default" style="font-size: 85%;" type="BUTTON" value="Upload & Save Product Category Image" onClick="image_edit();">
				</div>
			</div>
		</div>
    <div class="form-group">        
      <div class="col-sm-offset-4 col-sm-8">
      
<!--       <button type="button" class="Submit btn btn-default" onClick="window.location.href='home.jsp'"><b>Back</b></button>&nbsp;&nbsp; -->
      <!-- <button type="button" class="Submit btn btn-default" onClick="{backNavigation('settings.jsp','SM');}"><b>Back</b></button>&nbsp;&nbsp; -->
      	<button type="button" class="Submit btn btn-default" onClick="onNew();" <%=sNewEnb%>>Clear</button>&nbsp;&nbsp;
      	<button type="button" class="btn btn-success" onClick="onUpdate();" <%=sUpdateEnb%>>Save</button>&nbsp;&nbsp;
      	<button type="button" class="Submit btn btn-default" onClick="onDelete();" <%=sDeleteEnb%>>Delete</button>&nbsp;&nbsp;
      	
     </div>
    </div>
  </form>
</div>
</div>
</div>					

 <script>
var catalogpath='<%=catalogpath%>';
 /* if(catalogpath=="")
	 $("#item_img").attr("src","../jsp/dist/img/NO_IMG.png");
 else */	 
	$("#item_img").attr("src",catalogpath);
 
 $(document).ready(function(){
	    $('[data-toggle="tooltip"]').tooltip();
	    if(document.form1.SAVE_RED.value!="") {
	    	if(document.form1.SAVE_RED.value=="Updated") {  
		document.form1.action  = "../productclass/summary?PGaction=View&result=Product Category Updated Successfully";	   
	    document.form1.submit();
		}
	    else{
	    		document.form1.action = "../productclass/summary?PGaction=View&result=Product Category Updated Successfully";
	    		document.form1.submit();
	    }
	    }
	});
	$(document).ready(function(){
	    $('[data-toggle="tooltip"]').tooltip();
	    if(document.form1.SAVE_REDELETE.value!=""){
	    	if(document.form1.SAVE_REDELETE.value=="Delete") {
	    	document.form1.action  = "../productclass/summary?PGaction=View&result=Product Category Deleted Successfully";
	    	 document.form1.submit();
		}
	    	else{
	    		document.form1.action = "../productclass/summary?PGaction=View&result=Product Category Deleted Successfully";
	    		document.form1.submit();
	    		}
	    		}
	});

	function image_edit(){
		var myForm = document.getElementById('form');
		var formData = new FormData(myForm);
	    var userId= form1.PRD_CLS_ID.value;
		if(userId){
	    $.ajax({
	        type: 'post',
	        url: "/track/CatalogServlet?Submit=catry_img_edit",
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
			alert("Please enter Product Category Id");
		}
	        return false; 
	  }
function image_delete(){

    var formData = $('form').serialize();
    var userId= form1.PRD_CLS_ID.value;
	if(userId){
    $.ajax({
        type: 'post',
        url: "/track/CatalogServlet?Submit=catry_img_delete",
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
		alert("Please enter Product Category Id");
	}
        return false; 
  }

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
		
$(document).ready(function(){
    $('[data-toggle="tooltip"]').tooltip();   
});
</script>

<jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
</jsp:include>



