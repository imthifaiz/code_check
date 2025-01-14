<%@ include file="header.jsp"%>
<%@ page import="com.track.dao.TblControlDAO"%>
<%@ page import="com.track.constants.*"%>
<%@ page import="com.track.dao.MovHisDAO"%>
<%@ page import="com.track.util.*"%>

<%
String title = "Create Location Type One";
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

function onGenID(){

	$.ajax({
		type: "GET",
		url: "../loctypeone/Auto_ID",
		dataType: "json",
		beforeSend: function(){
			showLoader();
		}, 
		success: function(data) {
			$("#LOC_TYPE_ID").val(data.LOCTYPE);
		},
		error: function(data) {
			alert('Unable to generate Location Type ID. Please try again later.');
		},
		complete: function(){
			hideLoader();
		}
	});
    
	   /* document.form1.action  = "create_locType.jsp?action=Auto_ID";
	   document.form1.submit(); */
	}
function onAdd(){
	   var LOC_TYPE_ID   = document.form1.LOC_TYPE_ID.value;
	   var LOC_DESC = document.form1.LOC_DESC.value;
	    if(LOC_TYPE_ID == "" || LOC_TYPE_ID == null) {alert("Please Enter Location Type ID");document.form1.LOC_TYPE_ID.focus(); return false; }
	    if(LOC_DESC == "" || LOC_DESC == null) {alert("Please Enter Location Type Description");document.form1.LOC_DESC.focus(); return false; }
	   document.form1.action  = "../jsp/create_locType.jsp?action=ADD";
	   document.form1.submit();
	}
function onNew(){

	document.form1.LOC_TYPE_ID.value = ""; 
	document.form1.LOC_DESC.value = "";
	  /*  document.form1.action  = "../jsp/create_locType.jsp?action=Clear";
	   document.form1.submit(); */
	}
</SCRIPT>


<%
session = request.getSession();
String sUserId = (String) session.getAttribute("LOGIN_USER");
String sPlant = (String) session.getAttribute("PLANT");
String res        = "",action     =   "",locationTypeID="",locationTypeDesc="",sSAVE_RED;

String sNewEnb    = "enabled";
String sAddEnb    = "enabled";
TblControlDAO _TblControlDAO =new TblControlDAO();
session= request.getSession();
StrUtils strUtils = new StrUtils();
DateUtils dateutils = new DateUtils();
LocTypeUtil loctypeutil = new LocTypeUtil();

action            = strUtils.fString(request.getParameter("action"));
String plant = (String)session.getAttribute("PLANT");
locationTypeID  = strUtils.fString(request.getParameter("LOC_TYPE_ID"));
locationTypeDesc  = strUtils.fString(request.getParameter("LOC_DESC"));
sSAVE_RED  = strUtils.fString(request.getParameter("SAVE_RED"));

 if(action.equalsIgnoreCase("Clear")){
	 locationTypeID  = "";
	 locationTypeDesc  = "";
     
}
 /* else if(action.equalsIgnoreCase("Auto_ID"))
 {
  
   String minseq="";  String sBatchSeq=""; boolean insertFlag=false;String sZero="";
  
   _TblControlDAO.setmLogger(mLogger);
       Hashtable  ht=new Hashtable();
      
       String query=" isnull(NXTSEQ,'') as NXTSEQ";
       ht.put(IDBConstants.PLANT,plant);
       ht.put(IDBConstants.TBL_FUNCTION,"LOCTYPE");
         try{
        boolean exitFlag=false; boolean resultflag=false;
       exitFlag=_TblControlDAO.isExisit(ht,"",plant);
      
      //--if exitflag is false than we insert batch number on first time based on plant,currentmonth
      if (exitFlag==false)
       { 
                     
             Map htInsert=null;
             Hashtable htTblCntInsert  = new Hashtable();
            
             htTblCntInsert.put(IDBConstants.PLANT,plant);
           
             htTblCntInsert.put(IDBConstants.TBL_FUNCTION,"LOCTYPE");
             htTblCntInsert.put(IDBConstants.TBL_PREFIX1,"LT");
              htTblCntInsert.put(IDBConstants.TBL_MINSEQ,"000");
              htTblCntInsert.put(IDBConstants.TBL_MAXSEQ,"999");
             htTblCntInsert.put(IDBConstants.TBL_NEXT_SEQ, (String)IDBConstants.TBL_FIRST_NEX_SEQ);
             htTblCntInsert.put(IDBConstants.CREATED_BY, sUserId);
             htTblCntInsert.put(IDBConstants.CREATED_AT, (String)new DateUtils().getDateTime());
             insertFlag= _TblControlDAO.insertTblControl(htTblCntInsert,plant);
             
             locationTypeID="LT"+"001";
       }
       else
       {
            //--if exitflag is not false than we updated nxtseq batch number based on plant,currentmonth
          
            Map m= _TblControlDAO.selectRow(query, ht,"");
           sBatchSeq=(String)m.get("NXTSEQ");
                     
            int inxtSeq=Integer.parseInt(((String)sBatchSeq.trim().toString()))+1;
           
            String updatedSeq=Integer.toString(inxtSeq);
             if(updatedSeq.length()==1)
            {
              sZero="00";
            }
            else if(updatedSeq.length()==2)
            {
              sZero="0";
            }
           
          
            Map htUpdate = null;
           
            Hashtable htTblCntUpdate = new Hashtable();
            htTblCntUpdate.put(IDBConstants.PLANT,plant);
            htTblCntUpdate.put(IDBConstants.TBL_FUNCTION,"LOCTYPE");
            htTblCntUpdate.put(IDBConstants.TBL_PREFIX1,"LT");
            StringBuffer updateQyery=new StringBuffer("set ");
            updateQyery.append(IDBConstants.TBL_NEXT_SEQ +" = '"+ (String)updatedSeq.toString()+ "'");
          
          
       //  boolean updateFlag=_TblControlDAO.update(updateQyery.toString(),htTblCntUpdate,"",plant);
         
         
         locationTypeID="LT"+sZero+updatedSeq;
            }
            } catch(Exception e)
             {
         	   mLogger.exception(true,
    					"ERROR IN JSP PAGE - create_locType.jsp ", e);
             }
 } */
 //2. >> Add
 else if(action.equalsIgnoreCase("ADD")){
 	

    Hashtable ht = new Hashtable();
    ht.put(IDBConstants.PLANT,sPlant);
    ht.put(IDBConstants.LOCTYPEID,locationTypeID);
     if(!(loctypeutil.isExistsLocType(ht))) // if the Item  exists already
     {
           ht.put(IDBConstants.PLANT,sPlant);
           ht.put(IDBConstants.LOCTYPEID,locationTypeID);
           ht.put(IDBConstants.LOCTYPEDESC,locationTypeDesc); 
           ht.put(IConstants.ISACTIVE,"Y");
           ht.put(IDBConstants.CREATED_AT,new DateUtils().getDateTime());
           ht.put(IDBConstants.LOGIN_USER,sUserId);
          
           MovHisDAO mdao = new MovHisDAO(sPlant);
           mdao.setmLogger(mLogger);
        Hashtable htm = new Hashtable();
           htm.put("PLANT",sPlant);
           htm.put("DIRTYPE",TransactionConstants.ADD_LOCTYPE);
           htm.put("RECID","");
           htm.put("ITEM",locationTypeID);
           htm.put("REMARKS",locationTypeDesc);
           htm.put("UPBY",sUserId);   htm.put("CRBY",sUserId);
            htm.put("CRAT",dateutils.getDateTime());
            htm.put("UPAT",dateutils.getDateTime());
            htm.put(IDBConstants.TRAN_DATE,dateutils.getDateinyyyy_mm_dd(dateutils.getDate()));      
                  
            htm.put(IDBConstants.CREATED_AT,dateutils.getDateTime());
            htm.put(IDBConstants.TRAN_DATE,dateutils.getDateinyyyy_mm_dd(dateutils.getDate()));
            
  		  boolean updateFlag;
  		if(locationTypeID!="LT001")
    		  {	
  			boolean exitFlag = false;
  			Hashtable htv = new Hashtable();				
  			htv.put(IDBConstants.PLANT, plant);
  			htv.put(IDBConstants.TBL_FUNCTION, "LOCTYPE");
  			exitFlag = _TblControlDAO.isExisit(htv, "", plant);
  			if (exitFlag) 
    		    updateFlag=_TblControlDAO.updateSeqNo("LOCTYPE",plant);
  			else
  			{
  				boolean insertFlag = false;
  				Map htInsert=null;
              	Hashtable htTblCntInsert  = new Hashtable();           
              	htTblCntInsert.put(IDBConstants.PLANT,plant);          
              	htTblCntInsert.put(IDBConstants.TBL_FUNCTION,"LOCTYPE");
              	htTblCntInsert.put(IDBConstants.TBL_PREFIX1,"LT");
               	htTblCntInsert.put("MINSEQ","0000");
               	htTblCntInsert.put("MAXSEQ","9999");
              	htTblCntInsert.put(IDBConstants.TBL_NEXT_SEQ, (String)IDBConstants.TBL_FIRST_NEX_SEQ);
              	htTblCntInsert.put(IDBConstants.CREATED_BY, sUserId);
              	htTblCntInsert.put(IDBConstants.CREATED_AT, (String)new DateUtils().getDateTime());
              	insertFlag= _TblControlDAO.insertTblControl(htTblCntInsert,plant);
  			}
  		}
           boolean itemInserted = loctypeutil.insertLocTypeMst(ht);
              boolean  inserted = mdao.insertIntoMovHis(htm);
           if(itemInserted&&inserted) {
        	   sSAVE_RED = "Updated";
                     /* res = "<font class = "+IDBConstants.SUCCESS_COLOR +">Location Type Added Successfully</font>"; */

                  
           } else {
                     res = "<font class = "+IDBConstants.FAILED_COLOR +">Failed to add New Location Type One </font>";
                   
           }
     }else{
            res = "<font class = "+IDBConstants.FAILED_COLOR +">Location Type One Exists already. Try again</font>";
           
     }
  
 }
%>
<div class="container-fluid m-t-20">
	 <div class="box"> 
	 <!-- Thanzith Modified on 23.02.2022 -->
	 <ul class="breadcrumb backpageul">    
                <li class="underline-on-hover"><a href="../home">Dashboard</a></li>	
                <li><a href="../loctypeone/summary"><span class="underline-on-hover">Location Type One Summary</span></a></li>                      
                <li><label>Create Location Type One</label></li>                                   
            </ul>
        <!-- Thanzith Modified on 23.02.2022 -->     
            <div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title"><%=title %></h1>
                <h1 style="font-size: 18px; cursor: pointer;"
				class="box-title pull-right"
				onclick="window.location.href='../loctypeone/summary'">
				<i class="glyphicon glyphicon-remove"></i>
			</h1>
		</div>
		
 <div class="box-body">
 
   <CENTER><strong><%=res%></strong></CENTER>

<!-- <B><CENTER></CENTER></B> -->
  <form class="form-horizontal" name="form1" method="post">
    <div class="form-group">
      <label class="control-label col-form-label col-sm-4 required" for="Location Type ID">Location Type ID</label>
<!--       <i class="glyphicon glyphicon-star" style="font-size: 10px; top:-6px; color: #e50000"></i>&nbsp;Location Type ID:</label> -->
      <div class="col-sm-4">
      	<div class="input-group">    
    		<input name="LOC_TYPE_ID" id="LOC_TYPE_ID"type="TEXT" value="<%=locationTypeID%>" onkeypress="return blockSpecialChar(event)"
			size="50" MAXLENGTH=50 class="form-control">
   		 	<span class="input-group-addon"  onClick="onGenID();" <%=sAddEnb%>>  
   		 	<a href="#" data-toggle="tooltip" data-placement="top" title="Auto-Generate">
   		 	<i class="glyphicon glyphicon-edit" style="font-size: 20px;"></i></a></span>
  		</div>
  		<INPUT type="hidden" name="ITEM_ID1" value="">
  		<INPUT type="hidden" name="SAVE_RED" value="<%=sSAVE_RED%>">
      </div>
    </div>
    
 <div class="form-group">
      <label class="control-label col-form-label col-sm-4 required" for="Location Type Description">Location Type Description</label>
<!--       <i class="glyphicon glyphicon-star" style="font-size: 10px; top:-6px; color: #e50000"></i>&nbsp;Location Type Description:</label> -->
      <div class="col-sm-4">          
        <INPUT  class="form-control" name="LOC_DESC" id="LOC_DESC"  type="TEXT" value="<%=locationTypeDesc%>"
			size="50" MAXLENGTH=100>
      </div>
    </div>
    
    <div class="form-group">        
      <div class="col-sm-offset-4 col-sm-8">
<!--       <button type="button" class="Submit btn btn-default" onClick="window.location.href='../home'"><b>Back</b></button>&nbsp;&nbsp; -->
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
	    if(document.form1.SAVE_RED.value!="")
		{
		document.form1.action  = "../loctypeone/summary?PGaction=View&result=Location Type One Created Successfully";
		document.form1.submit();
		}
	});
$(document).ready(function(){
    $('[data-toggle="tooltip"]').tooltip();   
});
</script>

<jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
</jsp:include>
