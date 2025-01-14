<%@ page import="com.track.util.*"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.gates.*"%>
<%@ page import="com.track.constants.*"%>
<%@ include file="header.jsp"%>
<html>
<script language="JavaScript" type="text/javascript" src="js/general.js"></script>
<title>Table Control Master</title>
<link rel="stylesheet" href="css/style.css">
<SCRIPT LANGUAGE="JavaScript">
var subWin = null;
function popUpWin(URL) {
 subWin = window.open(URL, 'TABLECONTROL', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=700,height=400,left = 200,top = 184');
}
function onNew(){
   document.form1.action  = "tblControl_view.jsp?action=NEW";
   document.form1.submit();
}
function onAdd(){
     var sFunctionName = document.form1.FUNCTION_NAME.value;
     var sMinSeqNo = document.form1.MIN_SEQ_NO.value;
     var sMaxSeqNo = document.form1.MAX_SEQ_NO.value;
     var sPreFix   = document.form1.PREFIX.value;
     if(sFunctionName == "" || sFunctionName == null) {alert("Please Enter the Function Name"); document.form1.FUNCTION_NAME.focus(); return false; }
     if(sPreFix == "" || sFunctionName == null) {alert("Please Enter Prefix"); document.form1.PREFIX.focus(); return false; }
     if(sMinSeqNo == "" || sFunctionName == null) {alert("Please Enter the Minimum Sequence number"); document.form1.MIN_SEQ_NO.focus(); return false; }
     if(sMaxSeqNo == "" || sFunctionName == null) {alert("Please Enter the Maximum Sequence number"); document.form1.MAX_SEQ_NO.focus(); return false; }

     document.form1.action  = "tblControl_view.jsp?action=ADD";
     document.form1.submit();
}
function onUpdate(){
     var sFunctionName = document.form1.FUNCTION_NAME.value;
     if(sFunctionName == "" || sFunctionName == null) {alert("Please Enter the Function Name"); document.form1.FUNCTION_NAME.focus(); return false; }
     document.form1.action  = "tblControl_view.jsp?action=UPDATE";
     document.form1.submit();
}
function onDelete(){
     var sFunctionName = document.form1.FUNCTION_NAME.value;
     if(sFunctionName == "" || sFunctionName == null) {alert("Please Enter the Function Name"); document.form1.FUNCTION_NAME.focus(); return false; }
     return confirm ("Are you sure want to delete the Table Control Master?");
     document.form1.action  = "tblControl_view.jsp?action=DELETE";
     document.form1.submit();
}
function onView(){
     var sFunctionName = document.form1.FUNCTION_NAME.value;
     if(sFunctionName == "" || sFunctionName == null) {alert("Please Enter the Function Name"); document.form1.FUNCTION_NAME.focus(); return false; }
     document.form1.action  = "customer_view.jsp?action=VIEW";
     document.form1.submit();
}
</script>

<%
	String sUserId = (String) session.getAttribute("LOGIN_USER");
	String res        = "";
	String sNewEnb    = "enabled";
	String sDeleteEnb = "enabled";
	String sAddEnb    = "disabled";
	String sUpdateEnb = "enabled";
	String sTblEnb   = "enabled";
	String action     = "";
	String sFunc  = "",
    sDesc  = "",
    sPrefix     = "",
    sSuffix     = "",
    sMinSeq     = "",
    sMaxSeq     = "",
    sNextSeq    = "";
	StrUtils strUtils = new StrUtils();
	TblControlUtil tblUtil = new TblControlUtil();
	action            = strUtils.fString(request.getParameter("action"));
	sFunc  = strUtils.fString(request.getParameter("FUNCTION_NAME"));
	if(sFunc.length() <= 0) sFunc  = strUtils.fString(request.getParameter("FUNCTION_NAME1"));
	sDesc     = strUtils.fString(request.getParameter("FN_DESC"));
	sPrefix   = strUtils.fString(request.getParameter("PREFIX"));
	sSuffix   = strUtils.fString(request.getParameter("POSTFIX"));
	sMinSeq   = strUtils.fString(request.getParameter("MIN_SEQ_NO"));
	sMaxSeq   = strUtils.fString(request.getParameter("MAX_SEQ_NO"));
	sNextSeq  = strUtils.fString(request.getParameter("NXT_SEQ_NO"));

//1. >> New
	if(action.equalsIgnoreCase("NEW")){
       	sFunc   = "";
       	sDesc    = "";
       	sPrefix  = "";
       	sSuffix  = "";
       	sMinSeq  = "";
       	sMaxSeq  = "";
       	sNextSeq = "";
       	sAddEnb   = "enabled";
       	sTblEnb   = "enabled";
	}

	//2. >> Add
	else if(action.equalsIgnoreCase("ADD")){
    	if(!(tblUtil.isExistInTblControl(sFunc)))
    	{
          Hashtable ht = new Hashtable();
          ht.put(IConstants.PLANT,IConstants.PLANT_VAL);
          ht.put(IConstants.TBL_FUNCTION,sFunc);
          ht.put(IConstants.DESCRIPTION,sDesc);
          ht.put(IConstants.TBL_PREFIX,sPrefix);
          ht.put(IConstants.TBL_POSTFIX,sSuffix);
          ht.put(IConstants.TBL_MIN_SEQ,sMinSeq);
          ht.put(IConstants.TBL_MAX_SEQ,sMaxSeq);
          ht.put(IConstants.TBL_NEXT_SEQ,sMinSeq);
          ht.put(IConstants.CREATED_AT,new DateUtils().getDateTime());
          ht.put(IConstants.CREATED_BY,sUserId);
          boolean tblInserted = tblUtil.insertTblControl(ht);
          if(tblInserted) {
                    res = "<font class = "+IConstants.SUCCESS_COLOR+">Function Added Successfully</font>";
                    sAddEnb  = "disabled";
                    sTblEnb = "disabled";
          } else {
                    res = "<font class = "+IConstants.FAILED_COLOR+">Failed to add New Function</font>";
                    sAddEnb  = "enabled";
                    sTblEnb = "enabled";
          }
    }else{
           res = "<font class = "+IConstants.FAILED_COLOR+">Function Exists already. Try again</font>";
           sAddEnb = "enabled";
           sTblEnb = "enabled";
    }
}

//3. >> Update
	else if(action.equalsIgnoreCase("UPDATE"))  {
    	sTblEnb = "disabled";
    	sAddEnb  = "disabled";
    	if((tblUtil.isExistInTblControl(sFunc)))
    	{
          	Hashtable htUpdate = new Hashtable();
          	htUpdate.put(IConstants.PLANT,IConstants.PLANT_VAL);
          	htUpdate.put(IConstants.TBL_FUNCTION,sFunc);
          	htUpdate.put(IConstants.DESCRIPTION,sDesc);
          	htUpdate.put(IConstants.TBL_PREFIX,sPrefix);
          	htUpdate.put(IConstants.TBL_POSTFIX,sSuffix);
          	htUpdate.put(IConstants.TBL_MIN_SEQ,sMinSeq);
          	htUpdate.put(IConstants.TBL_MAX_SEQ,sMaxSeq);
          	htUpdate.put(IConstants.UPDATED_AT,new DateUtils().getDateTime());
          	htUpdate.put(IConstants.UPDATED_BY,sUserId);
          	Hashtable htCondition = new Hashtable();
          	htCondition.put(IConstants.TBL_FUNCTION,sFunc);
          	boolean tblUpdated = tblUtil.updateTblControl(htUpdate,htCondition);
          	if(tblUpdated) {
                    res = "<font class = "+IConstants.SUCCESS_COLOR+">Function Updated Successfully</font>";
          	} else {
                    res = "<font class = "+IConstants.FAILED_COLOR+">Failed to the </font>";
          }
    	}else{
           res = "<font class = "+IConstants.FAILED_COLOR+">Customer doesn't not Exists. Try again</font>";

   	 	}
	}

//4. >> Delete
	else if(action.equalsIgnoreCase("DELETE")){
    	sTblEnb = "disabled";
    	if(tblUtil.isExistInTblControl(sFunc))
    	{
          boolean tblDeleted = tblUtil.deleteTblControl(sFunc);
          if(tblDeleted) {
                    res = "<font class = "+IConstants.SUCCESS_COLOR+">Function Deleted Successfully</font>";
                    sAddEnb    = "disabled";
                    sFunc   = "";
                    sDesc    = "";
                    sPrefix  = "";
                    sSuffix  = "";
                    sMinSeq  = "";
                    sMaxSeq  = "";
                    sNextSeq = "";

          } else {
                    res = "<font class = "+IConstants.FAILED_COLOR+">Failed to Delete Function</font>";
                    sAddEnb = "enabled";
          }
    }else{
           res = "<font class = "+IConstants.FAILED_COLOR+">Function doesn't not Exists. Try again</font>";
   	 }
	}

//4. >> View
	else if(action.equalsIgnoreCase("VIEW")){
    	ArrayList arrCust = tblUtil.getTblControlDetails(sFunc);
    	sFunc    = (String)arrCust.get(0);
    	sDesc    = (String)arrCust.get(1);
    	sPrefix  =  (String)arrCust.get(2);
    	sSuffix  = (String)arrCust.get(3);
    	sMinSeq  = (String)arrCust.get(4);
    	sMaxSeq  = (String)arrCust.get(5);
    	sNextSeq = (String)arrCust.get(6);
	}

%>
<%@ include file="body.jsp"%>
<FORM name="form1" method="post">
  <br>
  <CENTER>
  <TABLE border="0" width="100%" cellspacing="0" cellpadding="0" align="center" bgcolor="#dddddd">
    <TR>
      <TH BGCOLOR="#000066" COLSPAN="11"><font color="white">TABLE CONTROL MASTER</font></TH>
    </TR>
  </TABLE>
  <br>
  <TABLE border="0" CELLSPACING=0 WIDTH="100%" bgcolor="#dddddd">
                    <TR>
                      <TH WIDTH="35%" ALIGN="RIGHT" >* Function Name : </TH>
                      <TD><INPUT name="FUNCTION_NAME" type = "TEXT" value="<%=sFunc%>" size="50"  MAXLENGTH=20>
                      <a href="#" onClick="javascript:popUpWin('tblControl_list.jsp?FUNCTION_NAME='+form1.FUNCTION_NAME.value);"><img src="images/populate.gif" border="0"></a>
                      <input name="action" type="submit" id="action" value="View" onclick="javascript:return onView();">
                     <INPUT name="FUNCTION_NAME1" type = "HIDDEN"  VALUE="<%=sFunc%>">
                    </TD>
                    </TR>
                    <TR>
                      <TH WIDTH="35%" ALIGN="RIGHT" >Description : </TH>
                      <TD><INPUT name="FN_DESC" type = "TEXT" value="" size="50"  MAXLENGTH=80 > </TD>
                    </TR>
                    <TR>
                      <TH WIDTH="35%" ALIGN="RIGHT" >* Prefix : </TH>
                      <TD><INPUT name="PREFIX" type = "TEXT" value="<%=sPrefix%>" size="50"  MAXLENGTH=20 > </TD>
                    </TR>
                    <TR>
                      <TH WIDTH="35%" ALIGN="RIGHT" >Suffix : </TH>
                      <TD><INPUT name="POSTFIX" type = "TEXT" value="<%=sSuffix%>" size="50"  MAXLENGTH=20 > </TD>
                    </TR>
                    <TR>
                      <TH WIDTH="35%" ALIGN="RIGHT" >* Minimum Sequence Number : </TH>
                      <TD><INPUT name="MIN_SEQ_NO" type = "TEXT" value="<%=sMinSeq%>" size="50"  MAXLENGTH=20 > </TD>
                    </TR>
                    <TR>
                      <TH WIDTH="35%" ALIGN="RIGHT" >* Maximum Sequence Number : </TH>
                      <TD><INPUT name="MAX_SEQ_NO" type = "TEXT" value="<%=sMaxSeq%>" size="50"  MAXLENGTH=20 > </TD>
                    </TR>
                    <TR>
                      <TH WIDTH="35%" ALIGN="RIGHT" >* Current Sequence Number : </TH>
                      <TD><INPUT name="MAX_SEQ_NO" type = "TEXT" value="<%=sNextSeq%>" size="50"  MAXLENGTH=20 > </TD>
                    </TR>
                    <TR>
                      <TH WIDTH="35%" ALIGN="RIGHT" >&nbsp;</TH>
                      <TD>* Mandatory Fields</TD>
                    </TR>
    <TR>
         <TD COLSPAN = 2><BR><B><CENTER><%=res%></B></TD>
    </TR>
    <TR>
         <TD COLSPAN = 2><center>
                <INPUT class="Submit" type="BUTTON" value="Cancel" onClick="window.location.href='../home'">&nbsp;&nbsp;
                <INPUT class="Submit" type="BUTTON" value="New" onClick="onNew();" <%=sNewEnb%>>&nbsp;&nbsp;
                <INPUT class="Submit" type="BUTTON" value="Add" onClick="onAdd();" <%=sAddEnb%>>&nbsp;&nbsp;
                <INPUT class="Submit" type="BUTTON" value="Update" onClick="onUpdate();" <%=sUpdateEnb%>>&nbsp;&nbsp;
                <INPUT class="Submit" type="BUTTON" value="Delete" onClick="onDelete();" <%=sDeleteEnb%>>
         </TD>
    </TR>
</TABLE>
</CENTER>
</FORM>
</BODY>
</HTML>
<%@ include file="footer.jsp"%>

