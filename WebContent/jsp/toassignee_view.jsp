<%@ page import="com.track.util.*"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.gates.*"%>
<%@ page import="com.track.constants.*"%>
<%@ page import="com.track.dao.CustMstDAO"%>
<%@ page import="com.track.dao.MovHisDAO"%>
<%@ page import="com.track.dao.TblControlDAO"%>
<%@ include file="header.jsp"%>
<!-- Not in Use - Menus status 0 -->
<%
String title = "Create Consignment Order Customer";
%>
<%@include file="sessionCheck.jsp" %>
<jsp:include page="header2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
	<jsp:param name="mainmenu" value="<%=IConstants.SETTINGS%>"/>
	<jsp:param name="submenu" value="<%=IConstants.SYSTEM_ADMIN%>"/>
</jsp:include>

<script language="JavaScript" type="text/javascript" src="js/general.js"></script>

<SCRIPT LANGUAGE="JavaScript">

	var subWin = null;
	function popUpWin(URL) {
 		subWin = window.open(URL, 'Transfer Order To_Assignee  Master', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=800,height=400,left = 200,top = 184');
	}
	function onNew(){
   		document.form.action  = "toassignee_view.jsp?action=NEW";
   		document.form.submit();
	}
	function isNumber(evt) {	
    evt = (evt) ? evt : window.event;
    var charCode = (evt.which) ? evt.which : evt.keyCode;
    
    if ((charCode > 31 && (charCode < 48 || charCode > 57))) {
    	if( (charCode!=43 && charCode!=32 && charCode!=45))
    		{
    		
        alert("  Please enter only Numbers.  ");
        return false;
    		}
    	}
    return true;
}
	function onAdd(){
  	 	var CUST_CODE   = document.form.CUST_CODE.value;
   		var CUST_NAME   = document.form.CUST_NAME.value;
   		if(CUST_CODE == "" || CUST_CODE == null) {alert("Please Enter Consignment Order Customer ID");document.form.CUST_CODE.focus(); return false; }
  		if(CUST_NAME == "" || CUST_NAME == null) 
  	  		{
  				alert("Please Enter Consignment Order Customer Name"); 
   				document.form.CUST_NAME.focus();
   				return false; 
   			}
   		document.form.action  = "toassignee_view.jsp?action=ADD";
   		document.form.submit();
	}
	function onUpdate(){
   		var CUST_CODE   = document.form.CUST_CODE.value;
   		if(CUST_CODE == "" || CUST_CODE == null) {alert("Please Enter Customer ID"); return false; }
   		document.form.action  = "toassignee_view.jsp?action=UPDATE";
  		document.form.submit();
	}
	function onDelete(){

   		var CUST_CODE   = document.form.CUST_CODE.value;
   		if(CUST_CODE == "" || CUST_CODE == null) {alert("Please Enter Customer ID");  return false; }
  		confirm("Are you sure to delete Assignee permanently ");
   		document.form.action  = "toassignee_view.jsp?action=DELETE";
   		document.form.submit();
	}
	function onView(){
   		var CUST_CODE   = document.form.CUST_CODE.value;
   		if(CUST_CODE == "" || CUST_CODE == null) {alert("Please Enter Customer ID"); return false; }
   		document.form.action  = "toassignee_view.jsp?action=VIEW";
   		document.form.submit();
	}
	function onIDGen()
	{
   		document.form.action  = "toassignee_view.jsp?action=Auto-ID";
   		document.form.submit();
	}

</script>
<%
	String sUserId = (String) session.getAttribute("LOGIN_USER");
	String res        = "";
	TblControlDAO _TblControlDAO =new TblControlDAO();
	String sNewEnb    = "enabled";
	String sDeleteEnb = "enabled";
	String sAddEnb    = "enabled";
	String sUpdateEnb = "enabled";
	String sCustEnb   = "enabled";
	String action     = "";
	String sCustCode  = "",
    sCustName  = "",
    sCustNameL  = "",
    sAddr1     = "",
    sAddr2     = "",
    sAddr3     = "", sAddr4     = "",
    		sState   = "",
    sCountry   = "",
    sZip       = "",
    sCons      = "Y";
 	String sContactName ="", sDesgination="",sTelNo="",sHpNo="",sFax="",sEmail="",sRemarks="";
	StrUtils strUtils = new StrUtils();
	CustUtil custUtil = new CustUtil();
	custUtil.setmLogger(mLogger);
	action            = strUtils.fString(request.getParameter("action"));
	String plant = strUtils.fString((String)session.getAttribute("PLANT"));
    sCustCode  = strUtils.fString(request.getParameter("CUST_CODE"));
	DateUtils dateutils = new DateUtils();
	if(sCustCode.length() <= 0) sCustCode  = strUtils.fString(request.getParameter("CUST_CODE1"));
	sCustName  = strUtils.InsertQuotes(strUtils.fString(request.getParameter("CUST_NAME")));
	sCustNameL  = strUtils.InsertQuotes(strUtils.fString(request.getParameter("L_CUST_NAME")));
	sAddr1     = strUtils.fString(request.getParameter("ADDR1"));
	sAddr2     = strUtils.fString(request.getParameter("ADDR2"));
	sAddr3     = strUtils.fString(request.getParameter("ADDR3"));
	sAddr4     = strUtils.fString(request.getParameter("ADDR4"));
	sState   = strUtils.fString(request.getParameter("STATE"));
	sCountry   = strUtils.fString(request.getParameter("COUNTRY"));
	sZip       = strUtils.fString(request.getParameter("ZIP"));
	sCons      = strUtils.fString(request.getParameter("CONSIGNMENT"));
	sContactName      = strUtils.fString(request.getParameter("CONTACTNAME"));
	sDesgination  = strUtils.fString(request.getParameter("DESGINATION"));
	sTelNo  = strUtils.fString(request.getParameter("TELNO"));
	sHpNo  = strUtils.fString(request.getParameter("HPNO"));
	sFax  = strUtils.fString(request.getParameter("FAX"));
	sEmail= strUtils.fString(request.getParameter("EMAIL"));
	sRemarks= strUtils.InsertQuotes(strUtils.fString(request.getParameter("REMARKS")));
//1. >> New
	if(action.equalsIgnoreCase("NEW")){
       	sCustCode  = "";
      	sCustName  = "";
      	sCustNameL="";
      	sAddr1     = "";
      	sAddr2     = "";
      	sAddr3     = ""; sAddr4     = "";
      	sState   = "";
      	sCountry   = "";
      	sZip       = "";
     	sCons      = "Y";
      	sContactName =""; sDesgination="";sTelNo="";sHpNo="";sFax="";sEmail="";sRemarks="";
	}
	else if(action.equalsIgnoreCase("Auto-ID"))
	{
		String minseq="";  String sBatchSeq=""; boolean insertFlag=false;String sZero="";
		//TblControlDAO _TblControlDAO =new TblControlDAO();
		_TblControlDAO.setmLogger(mLogger);
	    Hashtable  ht=new Hashtable();
        String query=" isnull(NXTSEQ,'') as NXTSEQ";
        ht.put(IDBConstants.PLANT,plant);
        ht.put(IDBConstants.TBL_FUNCTION,"TOASSIGNEE");
        try{
       		boolean exitFlag=false; boolean resultflag=false;
      		exitFlag=_TblControlDAO.isExisit(ht,"",plant);
     
     		//--if exitflag is false than we insert batch number on first time based on plant,currentmonth
    		 if (exitFlag==false)
      		{                     
            	Map htInsert=null;
            	Hashtable htTblCntInsert  = new Hashtable();
               	htTblCntInsert.put(IDBConstants.PLANT,plant);
               	htTblCntInsert.put(IDBConstants.TBL_FUNCTION,"TOASSIGNEE");
            	htTblCntInsert.put(IDBConstants.TBL_PREFIX1,"TA");
             	htTblCntInsert.put("MINSEQ","000");
             	htTblCntInsert.put("MAXSEQ","999");
            	htTblCntInsert.put(IDBConstants.TBL_NEXT_SEQ, (String)IDBConstants.TBL_FIRST_NEX_SEQ);
            	htTblCntInsert.put(IDBConstants.CREATED_BY, sUserId);
            	htTblCntInsert.put(IDBConstants.CREATED_AT, (String)new DateUtils().getDateTime());
            	insertFlag= _TblControlDAO.insertTblControl(htTblCntInsert,plant);
            
        		sCustCode="TA"+"001";
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
           		htTblCntUpdate.put(IDBConstants.TBL_FUNCTION,"TOASSIGNEE");
           		htTblCntUpdate.put(IDBConstants.TBL_PREFIX1,"TA");
           		StringBuffer updateQyery=new StringBuffer("set ");
           		updateQyery.append(IDBConstants.TBL_NEXT_SEQ +" = '"+ (String)updatedSeq.toString()+ "'");
          		//boolean updateFlag=_TblControlDAO.update(updateQyery.toString(),htTblCntUpdate,"",plant);
              	sCustCode="TA"+sZero+updatedSeq;
             }
           } catch(Exception e)
            {
        	   mLogger.exception(true,
   					"ERROR IN JSP PAGE - toassignee_view.jsp ", e);
            }

}

//2. >> Addc
else if(action.equalsIgnoreCase("ADD")){
  if(!custUtil.isExistToAssignee(sCustCode,plant)) // if the Customer exists already
    {
		//Inserting into Assignee 
	  	Hashtable ht = new Hashtable();
      	ht.put(IConstants.PLANT,plant);
      	ht.put("ASSIGNENO",sCustCode);
      	ht.put("ASSIGNENAME",sCustName);
     	ht.put(IConstants.NAME,sContactName);
      	ht.put(IConstants.DESGINATION,sDesgination);
      	ht.put(IConstants.TELNO,sTelNo);
      	ht.put(IConstants.HPNO,sHpNo);
      	ht.put(IConstants.FAX,sFax);
      	ht.put(IConstants.EMAIL,sEmail);
      	ht.put(IConstants.ADDRESS1,sAddr1);
      	ht.put(IConstants.ADDRESS2,sAddr2);
      	ht.put(IConstants.ADDRESS3,sAddr3);
       	ht.put(IConstants.ADDRESS4,sAddr4);
       	ht.put(IConstants.STATE,sState);
      	ht.put(IConstants.COUNTRY,sCountry);
      	ht.put(IConstants.ZIP,sZip);
      	ht.put(IConstants.USERFLG1,sCons);
      	ht.put(IConstants.REMARKS,sRemarks);
      	
      	ht.put(IConstants.CREATED_AT,new DateUtils().getDateTime());
      	ht.put(IConstants.CREATED_BY,sUserId);
      	ht.put("Comment1"," 0 ");
         ht.put(IConstants.ISACTIVE,"Y");
        
      	//Inserting into MovHis
        MovHisDAO mdao = new MovHisDAO(plant);
        mdao.setmLogger(mLogger);
        Hashtable htm = new Hashtable();
        htm.put("PLANT",plant);
        htm.put("DIRTYPE",TransactionConstants.ADD_TO_ASSIGNEE_MST);
        htm.put("RECID","");
        htm.put("ITEM",sCustCode);
        htm.put("CRBY",sUserId); 
        if(!sRemarks.equals(""))
		{
			htm.put(IDBConstants.REMARKS, sCustName+","+sRemarks);
		}
		else
		{
			htm.put(IDBConstants.REMARKS, sCustName);
		}
        htm.put("CRAT",dateutils.getDateTime());
        htm.put(IDBConstants.TRAN_DATE,dateutils.getDateinyyyy_mm_dd(dateutils.getDate()));
          boolean updateFlag;
		if(sCustCode!="TA001")
  		  {
			boolean exitFlag = false;
			Hashtable htv = new Hashtable();				
			htv.put(IDBConstants.PLANT, plant);
			htv.put(IDBConstants.TBL_FUNCTION, "TOASSIGNEE");
			exitFlag = _TblControlDAO.isExisit(htv, "", plant);
			if (exitFlag) 
				updateFlag=_TblControlDAO.updateSeqNo("TOASSIGNEE",plant);
			else
			{
				boolean insertFlag = false;
				Map htInsert=null;
            	Hashtable htTblCntInsert  = new Hashtable();
               	htTblCntInsert.put(IDBConstants.PLANT,plant);
               	htTblCntInsert.put(IDBConstants.TBL_FUNCTION,"TOASSIGNEE");
            	htTblCntInsert.put(IDBConstants.TBL_PREFIX1,"TA");
             	htTblCntInsert.put("MINSEQ","000");
             	htTblCntInsert.put("MAXSEQ","999");
            	htTblCntInsert.put(IDBConstants.TBL_NEXT_SEQ, (String)IDBConstants.TBL_FIRST_NEX_SEQ);
            	htTblCntInsert.put(IDBConstants.CREATED_BY, sUserId);
            	htTblCntInsert.put(IDBConstants.CREATED_AT, (String)new DateUtils().getDateTime());
            	insertFlag= _TblControlDAO.insertTblControl(htTblCntInsert,plant);
			}
		}
        boolean custInserted = custUtil.insertToAssignee(ht);
       
        boolean  inserted = mdao.insertIntoMovHis(htm);
        if(custInserted&&inserted) {
               res = "<font class = "+IConstants.SUCCESS_COLOR+">Consignment Order Customer Added Successfully</font>";
          } else {
               res = "<font class = "+IConstants.FAILED_COLOR+">Failed to add New Consignment Order Customer</font>";

          }
    }else{
           res = "<font class = "+IConstants.FAILED_COLOR+">Consignment Order Customer Exists already. Try again</font>";

    }
}

//3. >> Update
else if(action.equalsIgnoreCase("UPDATE"))  {
   if(custUtil.isExistToAssignee(sCustCode,plant))
    {
          Hashtable htUpdate = new Hashtable();
          htUpdate.put(IConstants.PLANT,plant);
          htUpdate.put("ASSIGNENO",sCustCode);
          htUpdate.put("ASSIGNENAME",sCustName);
          htUpdate.put(IConstants.CUSTOMER_LAST_NAME,sCustNameL);
          htUpdate.put(IConstants.ADDRESS1,sAddr1);
          htUpdate.put(IConstants.ADDRESS2,sAddr2);
          htUpdate.put(IConstants.ADDRESS3,sAddr3);
          htUpdate.put(IConstants.ADDRESS4,sAddr4);
          htUpdate.put(IConstants.STATE,sState);
          htUpdate.put(IConstants.COUNTRY,sCountry);
          htUpdate.put(IConstants.ZIP,sZip);
          htUpdate.put(IConstants.USERFLG1,sCons);
          htUpdate.put(IConstants.NAME,sContactName);
          htUpdate.put(IConstants.DESGINATION,sDesgination);
          htUpdate.put(IConstants.TELNO,sTelNo);
          htUpdate.put(IConstants.HPNO,sHpNo);
          htUpdate.put(IConstants.FAX,sFax);
          htUpdate.put(IConstants.EMAIL,sEmail);
          htUpdate.put(IConstants.REMARKS,sRemarks);
          htUpdate.put(IConstants.UPDATED_AT,new DateUtils().getDateTime());
          htUpdate.put(IConstants.UPDATED_BY,sUserId);

          Hashtable htCondition = new Hashtable();
          htCondition.put("ASSIGNENO",sCustCode);
          htCondition.put(IConstants.PLANT,plant);
            
          MovHisDAO mdao = new MovHisDAO(plant);
          mdao.setmLogger(mLogger);
          Hashtable htm = new Hashtable();
          htm.put("PLANT",plant);
          htm.put("DIRTYPE","UPD_TO_ASSIGNENO");
          htm.put("RECID","");
          htm.put("UPBY",sUserId);   htm.put("CRBY",sUserId);
          htm.put("CRAT",dateutils.getDateTime());
          htm.put("UPAT",dateutils.getDateTime());
          htm.put(IDBConstants.TRAN_DATE,dateutils.getDateinyyyy_mm_dd(dateutils.getDate()));  
          boolean custUpdated =custUtil.updateToAssignee(htUpdate,htCondition);
          boolean  inserted = mdao.insertIntoMovHis(htm);
          if(custUpdated&&inserted) {
                    res = "<font class = "+IConstants.SUCCESS_COLOR+">Consignment Order Customer Updated Successfully</font>";
          } else {
                    res = "<font class = "+IConstants.FAILED_COLOR+">Failed to Update Consignment Order Customer</font>";
          }
    }else{
           res = "<font class = "+IConstants.FAILED_COLOR+">Consignment Order Customer doesn't not Exists. Try again</font>";

    }
}
//4. >> Delete
else if(action.equalsIgnoreCase("DELETE")){
    if(custUtil.isExistToAssignee(sCustCode,plant))
    {
       boolean custDeleted = custUtil.deleteToAssignee(sCustCode,plant);
       if(custDeleted) 
       {
        	res = "<font class = "+IConstants.SUCCESS_COLOR+">Consignment Order Customer Deleted Successfully</font>";
            sCustCode  = "";
            sCustName  = "";
            sCustNameL = "";
            sAddr1     = "";
            sAddr2     = "";
            sAddr3     = ""; sAddr4     = "";
            sState   = "";
            sCountry   = "";
            sZip       = "";
            sCons      = "Y";sDesgination="";sTelNo="";sHpNo="";sFax="";sEmail="";sRemarks="";
            sContactName="";
          } else {
                    res = "<font class = "+IConstants.FAILED_COLOR+">Failed to Delete Consignment Order Customer</font>";
                    sAddEnb = "enabled";
          }
    }else{
           res = "<font class = "+IConstants.FAILED_COLOR+">Consignment Order Customer doesn't not Exists. Try again</font>";
    }
}

%>
<div class="container-fluid m-t-20">
	 <div class="box"> 
            <div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title"><%=title %></h1>
		</div>
		
 <div class="box-body">

   <CENTER><strong><%=res%></strong></CENTER>

  <form class="form-horizontal" name="form" method="post">
    <div class="form-group">
      <label class="control-label col-sm-4" for="Transfer Assignee ID">
      <i class="glyphicon glyphicon-star" style="font-size: 10px; top:-6px; color: #e50000"></i>&nbsp;Customer ID:</label>
      <div class="col-sm-4">
      	<div class="input-group">    
    		<input name="CUST_CODE" type="TEXT" value="<%=sCustCode%>"
			size="50" MAXLENGTH=50 class="form-control">
   		 	<span class="input-group-addon"  onClick="onIDGen();">
   		 	<a href="#" data-toggle="tooltip" data-placement="top" title="Auto-Generate">
   		 	<i class="glyphicon glyphicon-edit" style="font-size: 20px;"></i></a></span>
  		</div>
  		<INPUT type="hidden" name="CUST_CODE1" value="<%=sCustCode%>">
      	
      </div>
    </div>
    
    <div class="form-group">  
      <label class="control-label col-sm-4" for="Transfer Assignee Name">
      <i class="glyphicon glyphicon-star" style="font-size: 10px; top:-6px; color: #e50000"></i>&nbsp;Customer Name:</label>
      <div class="col-sm-4">          
        <INPUT  class="form-control" name="CUST_NAME" type="TEXT" value="<%=sCustName%>"
			size="50" MAXLENGTH=100>
      </div>
    </div>
    <INPUT name="L_CUST_NAME" type = "hidden" value="lastname" size="50"  MAXLENGTH=100>
    
    <div class="form-group">
         <label class="control-label col-sm-4" for="Contact Name">Contact Name:</label>
      <div class="col-sm-4">          
       <INPUT name="CONTACTNAME" type="TEXT" class="form-control"
			value="<%=sContactName%>" size="50" MAXLENGTH="100" >
      </div>
    </div>
    <div class="form-group">
      <label class="control-label col-sm-4" for="Designation">Designation:</label>
      <div class="col-sm-4">          
        <INPUT name="DESGINATION" type="TEXT" class="form-control"
			value="<%=sDesgination%>" size="50" MAXLENGTH="50">
      </div>
    </div>
    <div class="form-group">
      <label class="control-label col-sm-4" for="Telephone No">Telephone:</label>
      <div class="col-sm-4">          
        <INPUT name="TELNO" type="TEXT" value="<%=sTelNo%>" size="50" class="form-control" onkeypress="return isNumber(event)"
			MAXLENGTH="30">
      </div>
    </div>
    <div class="form-group">
      <label class="control-label col-sm-4" for="Hand Phone">Mobile:</label>
      <div class="col-sm-4">          
        <INPUT name="HPNO" type="TEXT" value="<%=sHpNo%>" size="50" class="form-control" onkeypress="return isNumber(event)"
			MAXLENGTH="30">
      </div>
    </div>
    <div class="form-group">
      <label class="control-label col-sm-4" for="Fax">Fax:</label>
      <div class="col-sm-4">          
        <INPUT name="FAX" type="TEXT" value="<%=sFax%>" size="50" onkeypress="return isNumber(event)"
			MAXLENGTH="30" class="form-control">
      </div>
    </div>
    <div class="form-group">
      <label class="control-label col-sm-4" for="Email">Email:</label>
      <div class="col-sm-4">          
        <INPUT name="EMAIL" type="TEXT" value="<%=sEmail%>" size="50"
			MAXLENGTH="50" class="form-control">
      </div>
    </div>
    <div class="form-group">
      <label class="control-label col-sm-4" for="Unit No">Unit No:</label>
      <div class="col-sm-4">          
        <INPUT name="ADDR1" type="TEXT" value="<%=sAddr1%>" size="50"
			MAXLENGTH=50 class="form-control">
      </div>
    </div>
    <div class="form-group">
      <label class="control-label col-sm-4" for="Building">Building:</label>
      <div class="col-sm-4">          
        <INPUT name="ADDR2" type="TEXT" value="<%=sAddr2%>" size="50"
			MAXLENGTH=50 class="form-control">
      </div>
    </div>
    <div class="form-group">
      <label class="control-label col-sm-4" for="Street">Street:</label>
      <div class="col-sm-4">          
        <INPUT name="ADDR3" type="TEXT" value="<%=sAddr3%>" size="50"
			MAXLENGTH=50 class="form-control">
      </div>
    </div>
    <div class="form-group">
      <label class="control-label col-sm-4" for="City">City:</label>
      <div class="col-sm-4">          
        <INPUT name="ADDR4" type="TEXT" value="<%=sAddr4%>" size="50"
			MAXLENGTH=50  class="form-control">
      </div>
    </div>
    <div class="form-group">
      <label class="control-label col-sm-4" for="State">State:</label>
      <div class="col-sm-4">          
        <INPUT name="STATE" type="TEXT" value="<%=sState%>" size="50"
			MAXLENGTH=50 class="form-control">
      </div>
    </div>
    <div class="form-group">
      <label class="control-label col-sm-4" for="Country">Country:</label>
      <div class="col-sm-4">          
       <INPUT name="COUNTRY" type="TEXT" value="<%=sCountry%>"
			size="50" MAXLENGTH=50 class="form-control">
      </div>
    </div>
    <div class="form-group">
      <label class="control-label col-sm-4" for="Postal Code">Postal Code:</label>
      <div class="col-sm-4">          
        <INPUT name="ZIP" type="TEXT" value="<%=sZip%>" size="50"
			MAXLENGTH=10 class="form-control">
      </div>
    </div>
    <div class="form-group">
      <label class="control-label col-sm-4" for="Remarks">Remarks:</label>
      <div class="col-sm-4">          
        <INPUT name="REMARKS" type="TEXT" value="<%=sRemarks%>"
			size="50" MAXLENGTH=100 class="form-control">
      </div>
    </div>
    
    <div class="form-group">        
     <div class="col-sm-offset-4 col-sm-8">
     <button type="button" class="Submit btn btn-default" onClick="window.location.href='../home'"><b>Back</b></button>&nbsp;&nbsp;
      <!-- <button type="button" class="Submit btn btn-default" onClick="{backNavigation('settings.jsp','SA');}"><b>Back</b></button>&nbsp;&nbsp; -->
      	<button type="button" class="Submit btn btn-default" onClick="onNew();"<%=sNewEnb%>><b>Clear</b></button>&nbsp;&nbsp;
      	<button type="button" class="Submit btn btn-default" data-toggle="modal" data-target="#myModal" onClick="onAdd();"<%=sAddEnb%>><b>Save</b></button>&nbsp;&nbsp;
      	
      </div>
    </div>
  </form>
</div>
</div>
</div>

<script>
$(document).ready(function(){
    $('[data-toggle="tooltip"]').tooltip();   
});
</script>


<jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
</jsp:include>
