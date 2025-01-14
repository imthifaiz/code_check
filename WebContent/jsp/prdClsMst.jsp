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
String title = "Create Product Category";
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
	document.form1.PRD_CLS_ID.value = ""; 
	document.form1.PRD_CLS_DESC.value = "";
//    document.form1.action  = "../productclass/new?action=Clear";
//    document.form1.submit();
}
function onAdd(){
   var ITEM_ID   = document.form1.PRD_CLS_ID.value;
   var PRD_CLS_DESC = document.form1.PRD_CLS_DESC.value;
    if(ITEM_ID == "" || ITEM_ID == null) {alert("Please Enter Product Category ID ");document.form1.PRD_CLS_ID.focus(); return false; }
    if(PRD_CLS_DESC == "" || PRD_CLS_DESC == null) {alert("Please Enter Product Category Description");document.form1.PRD_CLS_DESC.focus(); return false; }
   document.form1.action  = "../jsp/prdClsMst.jsp?action=ADD";
   document.form1.submit();
}


function onGenID(){

	$.ajax({
		type: "GET",
		url: "../productclass/Auto_ID",
		dataType: "json",
		beforeSend: function(){
			showLoader();
		}, 
		success: function(data) {
			$("#PRD_CLS_ID").val(data.PRDCLASS);
		},
		error: function(data) {
			alert('Unable to generate Product Category ID. Please try again later.');
		},
		complete: function(){
			hideLoader();
		}
	});
 
//    document.form1.action  = "../jsp/prdClsMst.jsp?action=Auto_ID";
//    document.form1.submit();
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
	String sItemId = "", sPrdClsId = "", sItemDesc = "",sSAVE_RED;

	session = request.getSession();
	StrUtils strUtils = new StrUtils();
	PrdClassUtil prdclsutil = new PrdClassUtil();
	prdclsutil.setmLogger(mLogger);
	DateUtils dateutils = new DateUtils();
	action = strUtils.fString(request.getParameter("action"));
	String plant = (String) session.getAttribute("PLANT");
	String result = (String) request.getAttribute("result");
	sItemId = strUtils.fString(request.getParameter("PRD_CLS_ID"));
	sItemDesc = strUtils.fString(request.getParameter("PRD_CLS_DESC"));
	sSAVE_RED  = strUtils.fString(request.getParameter("SAVE_RED"));
	if (sItemId.length() <= 0)
		sItemId = strUtils.fString(request.getParameter("ITEM_ID1"));

	//1. >> New
	if (action.equalsIgnoreCase("Clear")) {
		sItemId = "";
		sItemDesc = "";
		sPrdClsId = "";
	} 
// 	else if (action.equalsIgnoreCase("Auto_ID")) {

// 		String minseq = "";
// 		String sBatchSeq = "";
// 		boolean insertFlag = false;
// 		String sZero = "";
// 		//TblControlDAO _TblControlDAO = new TblControlDAO();
// 		_TblControlDAO.setmLogger(mLogger);
// 		Hashtable ht = new Hashtable();

// 		String query = " isnull(NXTSEQ,'') as NXTSEQ";
// 		ht.put(IDBConstants.PLANT, plant);
// 		ht.put(IDBConstants.TBL_FUNCTION, "PRDCLASS");
// 		try {
// 			boolean exitFlag = false;
// 			boolean resultflag = false;
// 			exitFlag = _TblControlDAO.isExisit(ht, "", plant);

// 			//--if exitflag is false than we insert batch number on first time based on plant,currentmonth
// 			if (exitFlag == false) {
// 				Map htInsert = null;
// 				Hashtable htTblCntInsert = new Hashtable();

// 				htTblCntInsert.put(IDBConstants.PLANT, plant);

// 				htTblCntInsert.put(IDBConstants.TBL_FUNCTION,
// 						"PRDCLASS");
// 				htTblCntInsert.put(IDBConstants.TBL_PREFIX1, "PC");
// 				htTblCntInsert.put(IDBConstants.TBL_MINSEQ, "000");
// 				htTblCntInsert.put(IDBConstants.TBL_MAXSEQ, "999");
// 				htTblCntInsert.put(IDBConstants.TBL_NEXT_SEQ,
// 						(String) IDBConstants.TBL_FIRST_NEX_SEQ);
// 				htTblCntInsert.put(IDBConstants.CREATED_BY, sUserId);
// 				htTblCntInsert.put(IDBConstants.CREATED_AT,
// 						(String) new DateUtils().getDateTime());
// 				insertFlag = _TblControlDAO.insertTblControl(
// 						htTblCntInsert, plant);

// 				sItemId = "PC" + "001";
// 			} else {
// 				//--if exitflag is not false than we updated nxtseq batch number based on plant,currentmonth

// 				Map m = _TblControlDAO.selectRow(query, ht, "");
// 				sBatchSeq = (String) m.get("NXTSEQ");

// 				int inxtSeq = Integer.parseInt(((String) sBatchSeq
// 						.trim().toString())) + 1;

// 				String updatedSeq = Integer.toString(inxtSeq);
// 				if (updatedSeq.length() == 1) {
// 					sZero = "00";
// 				} else if (updatedSeq.length() == 2) {
// 					sZero = "0";
// 				}

// 				//System.out.print("..................................."+rtnBatch);
// 				Map htUpdate = null;

// 				Hashtable htTblCntUpdate = new Hashtable();
// 				htTblCntUpdate.put(IDBConstants.PLANT, plant);
// 				htTblCntUpdate.put(IDBConstants.TBL_FUNCTION,
// 						"PRDCLASS");
// 				htTblCntUpdate.put(IDBConstants.TBL_PREFIX1, "PC");
// 				StringBuffer updateQyery = new StringBuffer("set ");
// 				updateQyery.append(IDBConstants.TBL_NEXT_SEQ + " = '"
// 						+ (String) updatedSeq.toString() + "'");

// 				//boolean updateFlag = _TblControlDAO.update(updateQyery.toString(), htTblCntUpdate, "", plant);

// 				sItemId = "PC" + sZero + updatedSeq;
// 			}
// 		} catch (Exception e) {
// 			mLogger.exception(true,
// 					"ERROR IN JSP PAGE - prdClsMst.jsp ", e);
// 		}
// 	}
	//2. >> Add
	else if (action.equalsIgnoreCase("ADD")) {
		String strpath = "", catlogpath = "",fileLocation="",filetempLocation = "";
		boolean isMultipart = ServletFileUpload.isMultipartContent(request);
		if (isMultipart) {
			try {
				
				boolean imageSizeflg = false;

				FileItemFactory factory = new DiskFileItemFactory();
				ServletFileUpload upload = new ServletFileUpload(factory);

				List items = upload.parseRequest(request);
				Iterator iterator = items.iterator();
				
				String ITEM = "",sLogo="";;
				while (iterator.hasNext()) {
					FileItem item = (FileItem) iterator.next();
					if (item.isFormField()) {

						if (item.getFieldName()
								.equalsIgnoreCase("PRD_CLS_ID")) {
							sItemId = StrUtils.fString(item.getString());
						}
						if (item.getFieldName()
								.equalsIgnoreCase("PRD_CLS_DESC")) {
							sItemDesc = StrUtils.fString(item.getString());
						}
					}
					if (!item.isFormField()) {
						if(item.getFieldName().equalsIgnoreCase("IMAGE_UPLOAD")) 
    					{
    					String fileName = StrUtils.fString(item.getName()).trim();
    					sLogo=fileName;
						if(fileName.length()>0) {
						long size = item.getSize();

						size = size / 1024;
						System.out.println("size of the Image imported :::"
								+ size);
						//checking image size for 2MB
						if (size > 2040) // condtn checking Image size
						{
							result = "<font color=\"red\">  Logo Image size greater than 1 MB </font>";

							imageSizeflg = true;

						}
						
    					fileLocation = DbBean.COMPANY_CATAGERY_PATH + "/"+ plant.toLowerCase();
	         			filetempLocation = DbBean.COMPANY_CATAGERY_PATH + "/temp" + "/" + plant.toLowerCase();
	         			
    				 java.io.File path = new File(fileLocation);
    					if (!path.exists()) {
    						boolean status = path.mkdirs();
    					}
    					fileName = fileName.substring(fileName
    							.lastIndexOf("\\") + 1);
    					
						File uploadedFile = new File(path + "/" + strUtils.RemoveSlash(sItemId)
						+ ".JPEG");
						if (uploadedFile.exists()) {
							 uploadedFile.delete();
							 }
    					strpath = path + "/" + fileName;
    					catlogpath = uploadedFile.getAbsolutePath();
    					if (!imageSizeflg && !uploadedFile.exists())
    						item.write(uploadedFile);

    					// delete temp uploaded file
    					File tempPath = new File(filetempLocation);
    					if (tempPath.exists()) {
    						File tempUploadedfile = new File(filetempLocation);
    						
    						if (tempUploadedfile.exists()) {
    							tempUploadedfile.delete();
    						}
    					}
    					
    					}
					}
					}
				}


					} catch (Exception e) {

					}
		}
		
		  result="";
		Hashtable ht = new Hashtable();
		ht.put(IDBConstants.PLANT, sPlant);
		ht.put(IDBConstants.PRDCLSID, sItemId);
		if (!(prdclsutil.isExistsItemType(ht))) // if the Item  exists already
		{
			ht.put(IDBConstants.PLANT, sPlant);
			ht.put(IDBConstants.PRDCLSID, sItemId);
			ht.put(IDBConstants.PRDCLSDESC, strUtils.InsertQuotes(sItemDesc));
			ht.put(IConstants.ISACTIVE, "Y");
			ht.put(IDBConstants.CREATED_AT, new DateUtils()
					.getDateTime());
			ht.put(IDBConstants.LOGIN_USER, sUserId);
			
            catlogpath = catlogpath.replace('\\', '/');
				ht.put(IDBConstants.CATLOGPATH, catlogpath);

			MovHisDAO mdao = new MovHisDAO(sPlant);
			mdao.setmLogger(mLogger);
			Hashtable htm = new Hashtable();
			htm.put("PLANT", sPlant);
			htm.put("DIRTYPE", TransactionConstants.ADD_PRDCLS);
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
   		if(sItemId!="PC001")
     		  {	
   			boolean exitFlag = false;
   			Hashtable htv = new Hashtable();				
   			htv.put(IDBConstants.PLANT, plant);
   			htv.put(IDBConstants.TBL_FUNCTION, "PRDCLASS");
   			exitFlag = _TblControlDAO.isExisit(htv, "", plant);
   			if (exitFlag) 
     		    updateFlag=_TblControlDAO.updateSeqNo("PRDCLASS",plant);
   			else
   			{
   				boolean insertFlag = false;
   				Map htInsert=null;
               	Hashtable htTblCntInsert  = new Hashtable();           
               	htTblCntInsert.put(IDBConstants.PLANT,plant);          
               	htTblCntInsert.put(IDBConstants.TBL_FUNCTION,"PRDCLASS");
               	htTblCntInsert.put(IDBConstants.TBL_PREFIX1,"PC");
                	htTblCntInsert.put("MINSEQ","0000");
                	htTblCntInsert.put("MAXSEQ","9999");
               	htTblCntInsert.put(IDBConstants.TBL_NEXT_SEQ, (String)IDBConstants.TBL_FIRST_NEX_SEQ);
               	htTblCntInsert.put(IDBConstants.CREATED_BY, sUserId);
               	htTblCntInsert.put(IDBConstants.CREATED_AT, (String)new DateUtils().getDateTime());
               	insertFlag= _TblControlDAO.insertTblControl(htTblCntInsert,plant);
   			}
   		}					
			
			boolean itemInserted = prdclsutil.insertPrdClsMst(ht);
			boolean inserted = mdao.insertIntoMovHis(htm);

			if (itemInserted && inserted) {
				sSAVE_RED = "Updated"; 
				/*res = "<font class = " + IDBConstants.SUCCESS_COLOR
						+ ">Product Class Added Successfully</font>"; */

						
			} else {
				sSAVE_RED="Failed to add New Product Category";
				/* res = "<font class = " + IDBConstants.FAILED_COLOR
						+ ">Failed to add New Product Class </font>"; */

			}
		} else {
			sSAVE_RED="Product Category Exists already.Try again";
			/* res = "<font class = " + IDBConstants.FAILED_COLOR
					+ ">Product Class Exists already.Try again</font>"; */

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
                <li><a href="../productclass/summary"><span class="underline-on-hover">Product Category Summary</span></a></li>                      
                <li><label>Create Product Category</label></li>                                   
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


<!-- <B><CENTER></CENTER></B> -->
  <form class="form-horizontal" name="form1" method="post" enctype="multipart/form-data">
    <div class="form-group">
      <label class="control-label col-form-label col-sm-4 required" for="Product Class ID">Product Category ID</label>
<!--       <i class="glyphicon glyphicon-star" style="font-size: 10px; top:-6px; color: #e50000"></i>&nbsp;Product Class ID:</label> -->
      <div class="col-sm-4">
      	<div class="input-group">    
    		<input name="PRD_CLS_ID" id="PRD_CLS_ID" type="TEXT" value="<%=sItemId%>" onkeypress="return blockSpecialChar(event)"
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
      <label class="control-label col-form-label col-sm-4 required" for="Product Class Description">Product Category Description</label>
<!--       <i class="glyphicon glyphicon-star" style="font-size: 10px; top:-6px; color: #e50000"></i>&nbsp;Product Class Description:</label> -->
      <div class="col-sm-4">          
        <INPUT  class="form-control" name="PRD_CLS_DESC" type="TEXT" value="<%=sItemDesc%>"
			size="50" MAXLENGTH=100>
      </div>
    </div>
    
    <div class="form-group">
      <label class="control-label col-form-label col-sm-4" for="Upload Image">Upload Image</label>
      <div class="col-sm-4">                
        <INPUT class="form-control" name="IMAGE_UPLOAD" type="File" size="20" MAXLENGTH=100>
      </div>
    </div>
    
    <div class="form-group">        
      <div class="col-sm-offset-4 col-sm-8">
      <!-- <button type="button" class="Submit btn btn-default" onClick="window.location.href='../home'"><b>Back</b></button>&nbsp;&nbsp; -->
      <!-- <button type="button" class="Submit btn btn-default" onClick="{backNavigation('settings.jsp','SM');}"><b>Back</b></button>&nbsp;&nbsp; -->
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
	document.form1.action  = "../productclass/summary?PGaction=View&result=Product Category Created Successfully";
	document.form1.submit();
	}
    else{
		document.form1.action =  "../productclass/summary?PGaction=View&result=Product Category Created Successfully";
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



