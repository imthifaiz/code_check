<%@ page import="com.track.util.*"%>
<%@ page import="com.track.db.util.*"%>
<%@ include file="header.jsp"%>
<%@ page import="com.track.dao.TblControlDAO"%>
<%@ page import="com.track.constants.*"%>
<%@ page import="com.track.dao.MovHisDAO"%>
<%
String title = "Create Customer Group";
%>
<%@include file="sessionCheck.jsp" %>
<jsp:include page="header2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
	<jsp:param name="mainmenu" value="<%=IConstants.SETTINGS%>"/>
	<jsp:param name="submenu" value="<%=IConstants.SYSTEM_ADMIN%>"/>
</jsp:include>
<link rel="stylesheet" href="../jsp/css/typeahead.css">
<link rel="stylesheet" href="../jsp/css/accounting.css">
<script language="JavaScript" type="text/javascript" src="../jsp/js/general.js"></script>


<SCRIPT LANGUAGE="JavaScript">

function onGenID(){
	$.ajax({
		type: "GET",
		url: "../customertype/Auto_ID",
		dataType: "json",
		beforeSend: function(){
			showLoader();
		}, 
		success: function(data) {
			$("#CUSTOMER_TYPE_ID").val(data.CUSTOMERTYPE);
		},
		error: function(data) {
			alert('Unable to generate Customer Group ID. Please try again later.');
		},
		complete: function(){
			hideLoader();
		}
	});
      /* document.form1.action  = "createCustomerType.jsp?action=Auto_ID";
	   document.form1.submit(); */
	}
function onAdd(){
	   var CUSTOMER_TYPE_ID   = document.form1.CUSTOMER_TYPE_ID.value;
	   var CUSTOMER_TYPE_DESC = document.form1.CUSTOMER_TYPE_DESC.value;
	    if(CUSTOMER_TYPE_ID == "" || CUSTOMER_TYPE_ID == null) {alert("Please Enter Customer Group ID");document.form1.CUSTOMER_TYPE_ID.focus(); return false; }
	    if(CUSTOMER_TYPE_DESC == "" || CUSTOMER_TYPE_DESC == null) {alert("Please Enter Customer Group Description");document.form1.CUSTOMER_TYPE_DESC.focus(); return false; }
	   document.form1.action  = "../jsp/createCustomerType.jsp?action=ADD";
	   document.form1.submit();
	}
function onNew(){
	document.form1.CUSTOMER_TYPE_ID.value = ""; 
	document.form1.CUSTOMER_TYPE_DESC.value = "";
	   /* document.form1.action  = "createCustomerType.jsp?action=Clear";
	   document.form1.submit(); */
	}
</SCRIPT>


<%
session = request.getSession();
String sUserId = (String) session.getAttribute("LOGIN_USER");
String sPlant = (String) session.getAttribute("PLANT");
String res        = "",action     =   "",customerTypeID="",customerTypeDesc="",sSAVE_RED;
String sNewEnb    = "enabled";
String sAddEnb    = "enabled";
TblControlDAO _TblControlDAO =new TblControlDAO();
session= request.getSession();
StrUtils strUtils = new StrUtils();
DateUtils dateutils = new DateUtils();
CustUtil custutil = new CustUtil();
action            = strUtils.fString(request.getParameter("action"));
String plant = (String)session.getAttribute("PLANT");
customerTypeID  = strUtils.fString(request.getParameter("CUSTOMER_TYPE_ID"));
customerTypeDesc  = strUtils.fString(request.getParameter("CUSTOMER_TYPE_DESC"));
sSAVE_RED  = strUtils.fString(request.getParameter("SAVE_RED"));
 if(action.equalsIgnoreCase("Clear")){
	 customerTypeID  = "";
	 customerTypeDesc  = "";
  }
 /* else if(action.equalsIgnoreCase("Auto_ID"))
 {
   String minseq="";  String sBatchSeq=""; boolean insertFlag=false;String sZero="";
 
   _TblControlDAO.setmLogger(mLogger);
       Hashtable  ht=new Hashtable();
       String query=" isnull(NXTSEQ,'') as NXTSEQ";
       ht.put(IDBConstants.PLANT,plant);
       ht.put(IDBConstants.TBL_FUNCTION,"CUSTOMERTYPE");
       try{
        boolean exitFlag=false; boolean resultflag=false;
       exitFlag=_TblControlDAO.isExisit(ht,"",plant);
       //--if exitflag is false than we insert batch number on first time based on plant,currentmonth
      if (exitFlag==false)
       { 
             Map htInsert=null;
             Hashtable htTblCntInsert  = new Hashtable();
             htTblCntInsert.put(IDBConstants.PLANT,plant);
             htTblCntInsert.put(IDBConstants.TBL_FUNCTION,"CUSTOMERTYPE");
             htTblCntInsert.put(IDBConstants.TBL_PREFIX1,"CT");
             htTblCntInsert.put(IDBConstants.TBL_MINSEQ,"000");
             htTblCntInsert.put(IDBConstants.TBL_MAXSEQ,"999");
             htTblCntInsert.put(IDBConstants.TBL_NEXT_SEQ, (String)IDBConstants.TBL_FIRST_NEX_SEQ);
             htTblCntInsert.put(IDBConstants.CREATED_BY, sUserId);
             htTblCntInsert.put(IDBConstants.CREATED_AT, (String)new DateUtils().getDateTime());
             insertFlag= _TblControlDAO.insertTblControl(htTblCntInsert,plant);
             customerTypeID="CT"+"001";
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
            htTblCntUpdate.put(IDBConstants.TBL_FUNCTION,"CUSTOMERTYPE");
            htTblCntUpdate.put(IDBConstants.TBL_PREFIX1,"CT");
            StringBuffer updateQyery=new StringBuffer("set ");
            updateQyery.append(IDBConstants.TBL_NEXT_SEQ +" = '"+ (String)updatedSeq.toString()+ "'");
           // boolean updateFlag=_TblControlDAO.update(updateQyery.toString(),htTblCntUpdate,"",plant);
            customerTypeID="CT"+sZero+updatedSeq;
            }
            } catch(Exception e)
             {
         	   mLogger.exception(true,
    					"ERROR IN JSP PAGE -createCustomerType.jsp ", e);
             }
 } */
 //2. >> Add
 else if(action.equalsIgnoreCase("ADD")){
 	 Hashtable ht = new Hashtable();
    ht.put(IDBConstants.PLANT,sPlant);
    ht.put(IDBConstants.CUSTOMERTYPEID,customerTypeID);
     if(!(custutil.isExistsCustomerType(ht))) // if the Item  exists already
     {
           ht.put(IDBConstants.PLANT,sPlant);
           ht.put(IDBConstants.CUSTOMERTYPEID,customerTypeID);
           ht.put(IDBConstants.CUSTOMERTYPEDESC,customerTypeDesc); 
           ht.put(IConstants.ISACTIVE,"Y");
           ht.put(IDBConstants.CREATED_AT,new DateUtils().getDateTime());
           ht.put(IDBConstants.LOGIN_USER,sUserId);
           MovHisDAO mdao = new MovHisDAO(sPlant);
           mdao.setmLogger(mLogger);
           Hashtable htm = new Hashtable();
           htm.put("PLANT",sPlant);
           htm.put("DIRTYPE",TransactionConstants.ADD_CUSTOMER_TYPE);
           htm.put("RECID","");
           htm.put("ITEM",customerTypeID);
           htm.put("REMARKS",customerTypeDesc);
           htm.put("UPBY",sUserId);   htm.put("CRBY",sUserId);
           htm.put("CRAT",dateutils.getDateTime());
           htm.put("UPAT",dateutils.getDateTime());
           htm.put(IDBConstants.TRAN_DATE,dateutils.getDateinyyyy_mm_dd(dateutils.getDate()));      
          
           htm.put(IDBConstants.CREATED_AT,dateutils.getDateTime());
           htm.put(IDBConstants.TRAN_DATE,dateutils.getDateinyyyy_mm_dd(dateutils.getDate()));
           
 		  boolean updateFlag;
 		if(customerTypeID!="CT001")
   		  {	
 			boolean exitFlag = false;
 			Hashtable htv = new Hashtable();				
 			htv.put(IDBConstants.PLANT, plant);
 			htv.put(IDBConstants.TBL_FUNCTION, "CUSTOMERTYPE");
 			exitFlag = _TblControlDAO.isExisit(htv, "", plant);
 			if (exitFlag) 
   		    updateFlag=_TblControlDAO.updateSeqNo("CUSTOMERTYPE",plant);
 			else
 			{
 				boolean insertFlag = false;
 				Map htInsert=null;
             	Hashtable htTblCntInsert  = new Hashtable();           
             	htTblCntInsert.put(IDBConstants.PLANT,plant);          
             	htTblCntInsert.put(IDBConstants.TBL_FUNCTION,"CUSTOMERTYPE");
             	htTblCntInsert.put(IDBConstants.TBL_PREFIX1,"CT");
              	htTblCntInsert.put("MINSEQ","0000");
              	htTblCntInsert.put("MAXSEQ","9999");
             	htTblCntInsert.put(IDBConstants.TBL_NEXT_SEQ, (String)IDBConstants.TBL_FIRST_NEX_SEQ);
             	htTblCntInsert.put(IDBConstants.CREATED_BY, sUserId);
             	htTblCntInsert.put(IDBConstants.CREATED_AT, (String)new DateUtils().getDateTime());
             	insertFlag= _TblControlDAO.insertTblControl(htTblCntInsert,plant);
 			}
 		}
           
           boolean itemInserted = custutil.insertCustomerTypeMst(ht);
           boolean  inserted = mdao.insertIntoMovHis(htm);
           if(itemInserted&&inserted) {
        	   sSAVE_RED="Customer Group Added Successfully";
                     /* res = "<font class = "+IDBConstants.SUCCESS_COLOR +">Customer Type Added Successfully</font>"; */
                  
           } else {
                     res = "<font class = "+IDBConstants.FAILED_COLOR +">Failed to add New Customer Group </font>";
           }
     }else{
            res = "<font class = "+IDBConstants.FAILED_COLOR +">Customer Group  Exists already. Try again</font>";
           
     }
  
 }
%>
<div class="container-fluid m-t-20">
	 <div class="box"> 
	 <!-- Thanzith Modified on 23.02.2022 -->
	 <ul class="breadcrumb backpageul">    
                <li class="underline-on-hover"><a href="../home">Dashboard</a></li>	
                 <li><a href="../customertype/summary"><span class="underline-on-hover">Customer Group Summary</span></a></li>                        
                <li><label>Create Customer Group</label></li>                                   
            </ul>
        <!-- Thanzith Modified on 23.02.2022 -->     
            <div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title"><%=title %></h1>
              <h1 style="font-size: 18px; cursor: pointer;"
				class="box-title pull-right"
				onclick="window.location.href='../customertype/summary'">
				<i class="glyphicon glyphicon-remove"></i>
			</h1>
		</div>
		
 <div class="box-body">

   <CENTER><strong><%=res%></strong></CENTER>

  <form class="form-horizontal" name="form1" method="post">
    <div class="form-group">
      
      <label class="control-label col-form-label col-sm-4 required">Customer Group Id</label>
      <div class="col-sm-4">
      	<div class="input-group">    
    		<input name="CUSTOMER_TYPE_ID" id="CUSTOMER_TYPE_ID" type="TEXT" value="<%=customerTypeID%>" onkeypress="return blockSpecialChar(event)"
			size="50" MAXLENGTH=50 class="form-control">
   		 	<span class="input-group-addon"  onClick="onGenID();" <%=sAddEnb%>>
   		 	<a href="#" data-toggle="tooltip" data-placement="top" title=Auto-Generate>
   		 	<i class="glyphicon glyphicon-edit" style="font-size: 20px;"></i></a></span>
  		</div>
  		<INPUT type="hidden" name="SAVE_RED" value="<%=sSAVE_RED%>">
      </div>
    </div>
    <div class="form-group">
      
      <label class="control-label col-form-label col-sm-4 required">Customer Group Description</label>
      <div class="col-sm-4">          
        <INPUT  class="form-control" name="CUSTOMER_TYPE_DESC" id="CUSTOMER_TYPE_DESC" type="TEXT" value="<%=customerTypeDesc%>"
			size="50" MAXLENGTH=100>
      </div>
    </div>
    <div class="form-group">        
      <div class="col-sm-offset-4 col-sm-8">
      <!-- <button type="button" class="Submit btn btn-default" onClick="window.location.href='../home'"><b>Back</b></button>&nbsp;&nbsp; -->
      <!-- <button type="button" class="Submit btn btn-default" onClick="{backNavigation('settings.jsp','SA');}"><b>Back</b></button>&nbsp;&nbsp; -->
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
	document.form1.action  = "../customertype/summary?PGaction=View&result=Customer Group Added Successfully";
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