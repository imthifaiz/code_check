<!-- Created By Resvi - 04.10.21  -->
<%@ page import="com.track.util.*"%>
<%@ page import="com.track.db.util.PrdClassUtil"%>
<%@ page import="com.track.gates.*"%>
<%@ page import="com.track.constants.*"%>
<%@ page import="com.track.dao.MovHisDAO"%>
<%@ page import="com.track.dao.TblControlDAO"%>
<%@ page import="org.apache.commons.fileupload.FileItem"%>
<%@ page import="org.apache.commons.fileupload.FileItemFactory"%>
<%@ page import="org.apache.commons.fileupload.disk.DiskFileItemFactory"%>
<%@ page import="org.apache.commons.fileupload.servlet.ServletFileUpload"%>
<%@ include file="header.jsp"%>

<%
String title = "Create Product Department";
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


function onNew(){
	document.form1.PRD_DEP_ID.value = ""; 
	document.form1.PRD_DEP_DESC.value = "";
//    document.form1.action  = "../productclass/new?action=Clear";
//    document.form1.submit();
}
function onAdd(){
   var ITEM_ID   = document.form1.PRD_DEP_ID.value;
   var PRD_DEP_DESC = document.form1.PRD_DEP_DESC.value;
    if(ITEM_ID == "" || ITEM_ID == null) {alert("Please Enter Product Department ID ");document.form1.PRD_DEP_ID.focus(); return false; }
    if(PRD_DEP_DESC == "" || PRD_DEP_DESC == null) {alert("Please Enter Product Department Description");document.form1.PRD_DEP_DESC.focus(); return false; }
   document.form1.action  = "../jsp/PrdDepMst.jsp?action=ADD";
   document.form1.submit();
}


function onGenID(){

	$.ajax({
		type: "GET",
		url: "../productdept/Auto_ID",
		dataType: "json",
		beforeSend: function(){
			showLoader();
		}, 
		success: function(data) {
			$("#PRD_DEP_ID").val(data.PRDDEPT);
		},
		error: function(data) {
			alert('Unable to generate Product Department ID. Please try again later.');
		},
		complete: function(){
			hideLoader();
		}
	});
 

}

</script>
<%
	session = request.getSession();
	String sUserId = (String) session.getAttribute("LOGIN_USER");
	String sPlant = (String) session.getAttribute("PLANT");
	String res = "";

	String sNewEnb = "enabled";
	String sDeleteEnb = "enabled";
	String sAddEnb = "disabled";
	String sUpdateEnb = "enabled";
	sAddEnb = "enabled";
	String action = "";
	TblControlDAO _TblControlDAO =new TblControlDAO();
	String sItemId = "", sPrdDepId = "", sItemDesc = "",sSAVE_RED;

	session = request.getSession();
	StrUtils strUtils = new StrUtils();
	PrdDeptUtil prddeputil = new PrdDeptUtil();
	prddeputil.setmLogger(mLogger);
	DateUtils dateutils = new DateUtils();
	action = strUtils.fString(request.getParameter("action"));
	String plant = (String) session.getAttribute("PLANT");
	String result = (String) request.getAttribute("result");
	sItemId = strUtils.fString(request.getParameter("PRD_DEP_ID"));
	sItemDesc = strUtils.fString(request.getParameter("PRD_DEP_DESC"));
	sSAVE_RED  = strUtils.fString(request.getParameter("SAVE_RED"));
	if (sItemId.length() <= 0)
		sItemId = strUtils.fString(request.getParameter("ITEM_ID1"));

	//1. >> New
	if (action.equalsIgnoreCase("Clear")) {
		sItemId = "";
		sItemDesc = "";
		sPrdDepId = "";
	} 

	//2. >> Add
	else if (action.equalsIgnoreCase("ADD")) {
		String strpath = "", fileLocation="",filetempLocation = "";
		boolean isMultipart = ServletFileUpload.isMultipartContent(request);
		if (isMultipart) {
			try {
				

				FileItemFactory factory = new DiskFileItemFactory();
				ServletFileUpload upload = new ServletFileUpload(factory);

				List items = upload.parseRequest(request);
				Iterator iterator = items.iterator();
				
				String ITEM = "",sLogo="";;
				while (iterator.hasNext()) {
					FileItem item = (FileItem) iterator.next();
					if (item.isFormField()) {

						if (item.getFieldName()
								.equalsIgnoreCase("PRD_DEP_ID")) {
							sItemId = StrUtils.fString(item.getString());
						}
						if (item.getFieldName()
								.equalsIgnoreCase("PRD_DEP_DESC")) {
							sItemDesc = StrUtils.fString(item.getString());
						}
					}

				}


					} catch (Exception e) {

					}
		}
		
		  result="";
		Hashtable ht = new Hashtable();
		ht.put(IDBConstants.PLANT, sPlant);
		ht.put(IDBConstants.PRDDEPTID, sItemId);
		if (!(prddeputil.isExistsItemType(ht))) // if the Item  exists already
		{
			ht.put(IDBConstants.PLANT, sPlant);
			ht.put(IDBConstants.PRDDEPTID, sItemId);
			ht.put(IDBConstants.PRDDEPTDESC, strUtils.InsertQuotes(sItemDesc));
			ht.put(IConstants.ISACTIVE, "Y");
			ht.put(IDBConstants.CREATED_AT, new DateUtils()
					.getDateTime());
			ht.put(IDBConstants.LOGIN_USER, sUserId);
			
            

			MovHisDAO mdao = new MovHisDAO(sPlant);
			mdao.setmLogger(mLogger);
			Hashtable htm = new Hashtable();
			htm.put("PLANT", sPlant);
			htm.put("DIRTYPE", TransactionConstants.ADD_PRDDEP);
			htm.put("RECID", "");
			htm.put("ITEM",sItemId);
			htm.put("REMARKS",strUtils.InsertQuotes( sItemDesc));
			htm.put("UPBY", sUserId);
			htm.put("CRBY", sUserId);
			htm.put("CRAT", dateutils.getDateTime());
			htm.put("UPAT", dateutils.getDateTime());
			htm.put(IDBConstants.TRAN_DATE, dateutils.getDateinyyyy_mm_dd(dateutils.getDate()));

			 htm.put(IDBConstants.CREATED_AT,dateutils.getDateTime());
             htm.put(IDBConstants.TRAN_DATE,dateutils.getDateinyyyy_mm_dd(dateutils.getDate()));
             
   		  boolean updateFlag;
   		if(sItemId!="PD001")
     		  {	
   			boolean exitFlag = false;
   			Hashtable htv = new Hashtable();				
   			htv.put(IDBConstants.PLANT, plant);
   			htv.put(IDBConstants.TBL_FUNCTION, "PRDDEPT");
   			exitFlag = _TblControlDAO.isExisit(htv, "", plant);
   			if (exitFlag) 
     		    updateFlag=_TblControlDAO.updateSeqNo("PRDDEPT",plant);
   			else
   			{
   				boolean insertFlag = false;
   				Map htInsert=null;
               	Hashtable htTblCntInsert  = new Hashtable();           
               	htTblCntInsert.put(IDBConstants.PLANT,plant);          
               	htTblCntInsert.put(IDBConstants.TBL_FUNCTION,"PRDDEPT");
               	htTblCntInsert.put(IDBConstants.TBL_PREFIX1,"PD");
                	htTblCntInsert.put("MINSEQ","0000");
                	htTblCntInsert.put("MAXSEQ","9999");
               	htTblCntInsert.put(IDBConstants.TBL_NEXT_SEQ, (String)IDBConstants.TBL_FIRST_NEX_SEQ);
               	htTblCntInsert.put(IDBConstants.CREATED_BY, sUserId);
               	htTblCntInsert.put(IDBConstants.CREATED_AT, (String)new DateUtils().getDateTime());
               	insertFlag= _TblControlDAO.insertTblControl(htTblCntInsert,plant);
   			}
   		}					
			
			boolean itemInserted = prddeputil.insertPrdDepMst(ht);
			boolean inserted = mdao.insertIntoMovHis(htm);

			if (itemInserted && inserted) {
				sSAVE_RED = "Updated"; 
				

						
			} else {
				sSAVE_RED="Failed to add New Product Department";
				

			}
		} else {
			sSAVE_RED="Product Department Exists already.Try again";
	

		}

	}
	
	if(!result.equalsIgnoreCase("")) {
		sSAVE_RED = "";
		res = "<font class = " + IDBConstants.FAILED_COLOR
		+ ">"+result+"</font>";
		}	
%>

<div class="container-fluid m-t-20">
	 <div class="box"> 
	  <!-- Thanzith Modified on 23.02.2022 -->
	 <ul class="breadcrumb backpageul">    
                <li class="underline-on-hover"><a href="../home">Dashboard</a></li>	
                 <li><a href="../productdept/summary"><span class="underline-on-hover">Product Department Summary</span></a></li>                        
                <li><label>Create Product Department</label></li>                                   
            </ul>   
        <!-- Thanzith Modified on 23.02.2022 -->      
            <div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title"><%=title %></h1>
               <h1 style="font-size: 18px; cursor: pointer;"
				class="box-title pull-right"
					onclick="window.location.href='../productdept/summary'">
				<i class="glyphicon glyphicon-remove"></i>
			</h1>
		</div>
		
 <div class="box-body">
 
 

   <CENTER><strong><%=res%></strong></CENTER>


<!-- <B><CENTER></CENTER></B> -->
  <form class="form-horizontal" name="form1" method="post" enctype="multipart/form-data">
    <div class="form-group">
      <label class="control-label col-form-label col-sm-4 required" for="Product Class ID">Product Department ID</label>
<!--       <i class="glyphicon glyphicon-star" style="font-size: 10px; top:-6px; color: #e50000"></i>&nbsp;Product Class ID:</label> -->
      <div class="col-sm-4">
      	<div class="input-group">    
    		<input name="PRD_DEP_ID" id="PRD_DEP_ID" type="TEXT" value="<%=sItemId%>" onkeypress="return blockSpecialChar(event)"
			size="50" MAXLENGTH=100 class="form-control">
   		 	<span class="input-group-addon"  onClick="onGenID();" <%=sAddEnb%>>
   		 	<a href="#" data-toggle="tooltip" data-placement="top" title="Auto-Generate">
   		 	<i class="glyphicon glyphicon-edit" style="font-size: 20px;"></i></a></span>
  		</div>
  		<INPUT type="hidden" name="ITEM_ID1" value="">
  		<INPUT type="hidden" name="SAVE_RED" value="<%=sSAVE_RED%>">
      </div>
    </div>
     
    <div class="form-group">
      <label class="control-label col-form-label col-sm-4 required" for="Product Class Description">Product Department Description</label>
      <div class="col-sm-4">          
        <INPUT  class="form-control" name="PRD_DEP_DESC" type="TEXT" value="<%=sItemDesc%>"
			size="50" MAXLENGTH=100>
      </div>
    </div>
    
    
    
    <div class="form-group">        
      <div class="col-sm-offset-4 col-sm-8">
      
      	<button type="button" class="Submit btn btn-default" onClick="onNew();" <%=sNewEnb%>>Clear</button>&nbsp;&nbsp;
      	<button type="button" class="btn btn-success" data-toggle="modal" data-target="#myModal" onClick="onAdd();" <%=sAddEnb%>>Save</button>&nbsp;&nbsp;
      	

      </div>
    </div>
  </form>
</div>
</div>
</div>



<script>
$(document).ready(function(){
    $('[data-toggle="tooltip"]').tooltip();
    if(document.form1.SAVE_RED.value!="") {
    if(document.form1.SAVE_RED.value!="")
	{
	document.form1.action  = "../productdept/summary?PGaction=View&result=Product Department Created Successfully";
	document.form1.submit();
	}
    else{
		document.form1.action =  "../productdept/summary?PGaction=View&result=Product Department Created Successfully";
		document.form1.submit();
}
    }
});
$(document).ready(function(){
    $('[data-toggle="tooltip"]').tooltip();   
});
</script>

<jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
</jsp:include>



