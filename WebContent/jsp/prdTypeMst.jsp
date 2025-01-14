<%@ page import="com.track.util.*"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.gates.*"%>
<%@ page import="com.track.constants.*"%>
<%@ page import="com.track.dao.MovHisDAO"%>
<%@ page import="com.track.dao.TblControlDAO"%>
<%@ include file="header.jsp"%>

<%
String title = "Create Sub Category Type";
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
 subWin = window.open(URL, 'PRODUCT', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=700,height=400,left = 200,top = 184');
}
function onNew(){
	document.form1.ITEM_ID.value = ""; 
	document.form1.ITEM_DESC.value = "";
   /* document.form1.action  = "../jsp/prdTypeMst.jsp?action=Clear";
   document.form1.submit(); */
}
function onAdd(){
   var ITEM_ID   = document.form1.ITEM_ID.value;
   var ITEM_DESC = document.form1.ITEM_DESC.value;
    if(ITEM_ID == "" || ITEM_ID == null) {alert("Please Enter Product Type ID");document.form1.ITEM_ID.focus(); return false; }
    if(ITEM_DESC == "" || ITEM_DESC == null) {alert("Please Enter Product Type Description");document.form1.ITEM_DESC.focus(); return false; }
   document.form1.action  = "../jsp/prdTypeMst.jsp?action=ADD";
   document.form1.submit();
}


function onGenID(){

	$.ajax({
		type: "GET",
		url: "../producttype/Auto_ID",
		dataType: "json",
		beforeSend: function(){
			showLoader();
		}, 
		success: function(data) {
			$("#ITEM_ID").val(data.PRDTYPE);
		},
		error: function(data) {
			alert('Unable to generate Product Sub CategoryID. Please try again later.');
		},
		complete: function(){
			hideLoader();
		}
	});
     
   /* document.form1.action  = "../jsp/prdTypeMst.jsp?action=Auto_ID";
   document.form1.submit(); */
}

</script>
<%
session = request.getSession();
String sUserId = (String) session.getAttribute("LOGIN_USER");
String sPlant = (String) session.getAttribute("PLANT");
String res        = "";

String sNewEnb    = "enabled";
String sDeleteEnb = "enabled";
String sAddEnb    = "disabled";
String sUpdateEnb = "enabled";
sAddEnb    = "enabled";
TblControlDAO _TblControlDAO =new TblControlDAO();
String action     =   "";
String sItemId ="",sPrdClsId  =   "",
       sItemDesc  = "",sSAVE_RED;

session= request.getSession();
StrUtils strUtils = new StrUtils();
PrdTypeUtil prdtypeutil = new PrdTypeUtil();
DateUtils dateutils = new DateUtils();

prdtypeutil.setmLogger(mLogger);

action            = strUtils.fString(request.getParameter("action"));
String plant = (String)session.getAttribute("PLANT");
sItemId  = strUtils.fString(request.getParameter("ITEM_ID"));
sItemDesc  = strUtils.fString(request.getParameter("ITEM_DESC"));
String result = StrUtils.fString(request.getParameter("result"));
sSAVE_RED  = strUtils.fString(request.getParameter("SAVE_RED"));
if(sItemId.length() <= 0) sItemId  = strUtils.fString(request.getParameter("ITEM_ID1"));

//1. >> New
if(action.equalsIgnoreCase("Clear")){
      sItemId  = "";
      sItemDesc  = "";
      sPrdClsId ="";
}
/* else if(action.equalsIgnoreCase("Auto_ID"))
{
 
  String minseq="";  String sBatchSeq=""; boolean insertFlag=false;String sZero="";
 // TblControlDAO _TblControlDAO =new TblControlDAO();
  _TblControlDAO.setmLogger(mLogger);
      Hashtable  ht=new Hashtable();
     
      String query=" isnull(NXTSEQ,'') as NXTSEQ";
      ht.put(IDBConstants.PLANT,plant);
      ht.put(IDBConstants.TBL_FUNCTION,"PRDTYPE");
        try{
       boolean exitFlag=false; boolean resultflag=false;
      exitFlag=_TblControlDAO.isExisit(ht,"",plant);
     
     //--if exitflag is false than we insert batch number on first time based on plant,currentmonth
     if (exitFlag==false)
      { 
                    
            Map htInsert=null;
            Hashtable htTblCntInsert  = new Hashtable();
           
            htTblCntInsert.put(IDBConstants.PLANT,plant);
          
            htTblCntInsert.put(IDBConstants.TBL_FUNCTION,"PRDTYPE");
            htTblCntInsert.put(IDBConstants.TBL_PREFIX1,"PT");
             htTblCntInsert.put(IDBConstants.TBL_MINSEQ,"000");
             htTblCntInsert.put(IDBConstants.TBL_MAXSEQ,"999");
            htTblCntInsert.put(IDBConstants.TBL_NEXT_SEQ, (String)IDBConstants.TBL_FIRST_NEX_SEQ);
            htTblCntInsert.put(IDBConstants.CREATED_BY, sUserId);
            htTblCntInsert.put(IDBConstants.CREATED_AT, (String)new DateUtils().getDateTime());
            insertFlag= _TblControlDAO.insertTblControl(htTblCntInsert,plant);
            
        sItemId="PT"+"001";
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
           htTblCntUpdate.put(IDBConstants.TBL_FUNCTION,"PRDTYPE");
           htTblCntUpdate.put(IDBConstants.TBL_PREFIX1,"PT");
           StringBuffer updateQyery=new StringBuffer("set ");
           updateQyery.append(IDBConstants.TBL_NEXT_SEQ +" = '"+ (String)updatedSeq.toString()+ "'");
         
         
      //  boolean updateFlag=_TblControlDAO.update(updateQyery.toString(),htTblCntUpdate,"",plant);
        
        
              sItemId="PT"+sZero+updatedSeq;
           }
           } catch(Exception e)
            {
        	   mLogger.exception(true,
   					"ERROR IN JSP PAGE - prdTypeMst.jsp ", e);
            }
} */
//2. >> Add
else if(action.equalsIgnoreCase("ADD")){
	  result="";

 Hashtable ht = new Hashtable();
 ht.put(IDBConstants.PLANT,sPlant);
 ht.put(IDBConstants.PRDTYPEID,sItemId);
  if(!(prdtypeutil.isExistsItemType(ht))) // if the Item  exists already
  {
        ht.put(IDBConstants.PLANT,sPlant);
        ht.put(IDBConstants.PRDTYPEID,sItemId);
        ht.put(IDBConstants.PRDTYPEDESC,sItemDesc); 
        ht.put(IConstants.ISACTIVE,"Y");
        ht.put(IDBConstants.CREATED_AT,new DateUtils().getDateTime());
        ht.put(IDBConstants.LOGIN_USER,sUserId);
       
        MovHisDAO mdao = new MovHisDAO(sPlant);
        mdao.setmLogger(mLogger);
     Hashtable htm = new Hashtable();
        htm.put("PLANT",sPlant);
        htm.put("DIRTYPE",TransactionConstants.ADD_PRDTYPE);
        htm.put("RECID","");
        htm.put("ITEM",sItemId);
        htm.put("REMARKS",sItemDesc);
        htm.put("UPBY",sUserId);   htm.put("CRBY",sUserId);
         htm.put("CRAT",dateutils.getDateTime());
         htm.put("UPAT",dateutils.getDateTime());
         htm.put(IDBConstants.TRAN_DATE,dateutils.getDateinyyyy_mm_dd(dateutils.getDate()));  
         
         htm.put(IDBConstants.CREATED_AT,dateutils.getDateTime());
         htm.put(IDBConstants.TRAN_DATE,dateutils.getDateinyyyy_mm_dd(dateutils.getDate()));
         
		  boolean updateFlag;
		if(sItemId!="PT001")
 		  {	
			boolean exitFlag = false;
			Hashtable htv = new Hashtable();				
			htv.put(IDBConstants.PLANT, plant);
			htv.put(IDBConstants.TBL_FUNCTION, "PRDTYPE");
			exitFlag = _TblControlDAO.isExisit(htv, "", plant);
			if (exitFlag) 
 		    updateFlag=_TblControlDAO.updateSeqNo("PRDTYPE",plant);
			else
			{
				boolean insertFlag = false;
				Map htInsert=null;
           	Hashtable htTblCntInsert  = new Hashtable();           
           	htTblCntInsert.put(IDBConstants.PLANT,plant);          
           	htTblCntInsert.put(IDBConstants.TBL_FUNCTION,"PRDTYPE");
           	htTblCntInsert.put(IDBConstants.TBL_PREFIX1,"PT");
            	htTblCntInsert.put("MINSEQ","0000");
            	htTblCntInsert.put("MAXSEQ","9999");
           	htTblCntInsert.put(IDBConstants.TBL_NEXT_SEQ, (String)IDBConstants.TBL_FIRST_NEX_SEQ);
           	htTblCntInsert.put(IDBConstants.CREATED_BY, sUserId);
           	htTblCntInsert.put(IDBConstants.CREATED_AT, (String)new DateUtils().getDateTime());
           	insertFlag= _TblControlDAO.insertTblControl(htTblCntInsert,plant);
			}
		}
               
       
        boolean itemInserted = prdtypeutil.insertPrdTypeMst(ht);
           boolean  inserted = mdao.insertIntoMovHis(htm);
        if(itemInserted&&inserted) {
      	  sSAVE_RED="Update";
                  /* res = "<font class = "+IDBConstants.SUCCESS_COLOR +">Product Type Added Successfully</font>"; */

               
        } else {
        	sSAVE_RED="Failed to add New Product Sub Category";
                 /*  res = "<font class = "+IDBConstants.FAILED_COLOR +">Failed to add New Product Type </font>"; */
                
        }
  }else{
	  sSAVE_RED="Product Sub Category  Exists already. Try again";
        /*  res = "<font class = "+IDBConstants.FAILED_COLOR +">Product Type  Exists already. Try again</font>"; */
        
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
                <li><a href="../producttype/summary"><span class="underline-on-hover">Product Sub Category Summary</span></a></li>                        
                <li><label>Create Sub Category Type</label></li>                                   
            </ul>
        <!-- Thanzith Modified on 23.02.2022 -->     
            <div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title"><%=title %></h1>
              <h1 style="font-size: 18px; cursor: pointer;"
				class="box-title pull-right"
						onclick="window.location.href='../producttype/summary'">
				<i class="glyphicon glyphicon-remove"></i>
			</h1>
		</div>
		
 <div class="box-body">
 
 
 
   <CENTER><strong><%=res%></strong></CENTER>


  <form class="form-horizontal" name="form1" method="post">
    <div class="form-group">
      <label class="control-label col-form-label col-sm-4 required" for="Product Type ID">Product Sub Category ID</label>
<!--       <i class="glyphicon glyphicon-star" style="font-size: 10px; top:-6px; color: #e50000"></i>&nbsp;Product Type ID:</label> -->
      <div class="col-sm-4">
      	<div class="input-group">    
    		<input name="ITEM_ID" id="ITEM_ID" type="TEXT" value="<%=sItemId%>" onkeypress="return blockSpecialChar(event)"
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
      <label class="control-label col-form-label col-sm-4 required" for="Product Type Description">Product Sub Category Description</label>
<!--       <i class="glyphicon glyphicon-star" style="font-size: 10px; top:-6px; color: #e50000"></i>&nbsp;Product Type Description:</label> -->
      <div class="col-sm-4">          
        <INPUT  class="form-control" name="ITEM_DESC" id="ITEM_DESC" type="TEXT" value="<%=sItemDesc%>"
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
    if(document.form1.SAVE_RED.value!="") {
    if(document.form1.SAVE_RED.value!="")
	{
	document.form1.action  = "../producttype/summary?PGaction=View&result=Product Sub Category Created Successfully";
	document.form1.submit();
	}
    else{
    	document.form1.action  = "../producttype/summary?PGaction=View&result=Product Sub Category Created Successfully";
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