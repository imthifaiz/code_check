<%@ page import="com.track.gates.DbBean"%>
<%@ page import="com.track.gates.Generator"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.util.*"%>
<%@ page import="com.track.gates.*"%>
<%@ page import="com.track.dao.ItemMstDAO"%>
<%@ page import="com.track.dao.InvMstDAO"%>
<%@ page import="java.util.*"%>
<%@ include file="header.jsp"%>

<%@page import="com.track.constants.IDBConstants"%>
<%@page import="com.track.constants.IConstants"%>
<%@page import="com.track.constants.IDBConstants"%>
<jsp:useBean id="sb" class="com.track.gates.selectBean" />
<jsp:useBean id="su" class="com.track.util.StrUtils" />
<html>
<head>
<script language="javascript">
var subWin = null;
function popUpWin(URL) {
  subWin = window.open(URL, 'Label Setting', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=700,height=400,left = 200,top = 184');
}

function checkAll(isChk)
{
	var len = document.form1.chkdLnNo.length;
	 var orderLNo; 
	 if(len == undefined) len = 1;  
    if (document.form1.chkdLnNo)
    {
        for (var i = 0; i < len ; i++)
        {      
              	if(len == 1){
              		document.form1.chkdLnNo.checked = isChk;
               	}
              	else{
              		document.form1.chkdLnNo[i].checked = isChk;
              	}
            	
        }
    }
}

function onPrint()
{
	 var checkFound = false;  
	 var orderLNo;
	 var len = document.form1.chkdLnNo.length; 
	 if(len == undefined) len = 1;
	for (var i = 0; i < len ; i++)
    {
    	if(len ==1 && document.form1.chkdLnNo.checked)  
    	{
    		chkstring = document.form1.chkdLnNo.value;
     	}
    	else
    	{
    		chkstring = document.form1.chkdLnNo[i].value;
     	}
    	chkdvalue = chkstring.split(',');
		if(len == 1 && (!document.form1.chkdLnNo.checked))
		{
			checkFound = false;
		}
		
		else if(len ==1 && document.form1.chkdLnNo.checked)
	     {
	    	 checkFound = true;
	    	 orderLNo = chkdvalue[0]+"_"+chkdvalue[1]+"_"+chkdvalue[2];
	    	 
	    }
	
	     else {
		     if(document.form1.chkdLnNo[i].checked){
		    	 checkFound = true;
		    	 orderLNo = chkdvalue[0]+"_"+chkdvalue[1]+"_"+chkdvalue[2];
		    	 
		    }
		   
	     }
              	     
    }
	
    if (checkFound!=true) {
    	alert("Please check at least one checkbox.");
		return false;
	}
    
   document.form1.action ="/track/LabelPrintServlet?action=Print";
   document.form1.submit();
 
 }


 
 
function onViewReport()
{
  document.form1.action ="/track/LabelPrintServlet?action=ViewReport";
  document.form1.submit();

}
 
function onGo(){
   var flag    = "false";
   var FROM_DATE      = document.form1.FROM_DATE.value;
   var TO_DATE        = document.form1.TO_DATE.value;
   var DIRTYPE        = document.form1.DIRTYPE.value;
   var USER           = document.form1.CUSTOMER.value;
   var ITEMNO         = document.form1.ITEM.value;
   var ORDERNO        = document.form1.ORDERNO.value;
   var JOBNO          = document.form1.JOBNO.value;
   var PRD_BRAND_ID      = document.form1.PRD_BRAND_ID.value;
   var PRD_TYPE_ID      = document.form1.PRD_TYPE_ID.value;
   var PRD_CLS_ID      = document.form1.PRD_CLS_ID.value;
   var REASONCODE      = document.form1.REASONCODE.value;
  
   if(DIRTYPE != null     && DIRTYPE != "") { flag = true;}
   if(flag == "false"){ alert("Please select the Dirtype"); return false;}
   
   if(FROM_DATE != null     && FROM_DATE != "") { flag = true;}
   if(TO_DATE != null    && TO_DATE != "") { flag = true;}
   if(DIRTYPE != null     && DIRTYPE != "") { flag = true;}

   if(USER != null    && USER != "") { flag = true;}
   if(ITEMNO != null     && ITEMNO != "") { flag = true;}
   
   if(ORDERNO != null     && ORDERNO != "") { flag = true;}
   if(JOBNO != null     && JOBNO != "") { flag = true;}

   if(PRD_BRAND_ID != null     && PRD_BRAND_ID != "") { flag = true;}
   if(PRD_TYPE_ID != null     && PRD_TYPE_ID != "") 
   { flag = true;}
   if(PRD_CLS_ID != null     && PRD_CLS_ID != "") { flag = true;}
   if(REASONCODE != null     && REASONCODE != "") { flag = true;}
 
  document.form1.action="LabelPrintGoodsIssue.jsp";
  document.form1.submit();
}


</script>
<script language="JavaScript" type="text/javascript" src="js/calendar.js"></script>
<title>Label Print (Goods Issue)</title>
</head>
<link rel="stylesheet" href="css/style.css">
<%
	StrUtils _strUtils     = new StrUtils();
	Generator generator   = new Generator();
	HTReportUtil movHisUtil       = new HTReportUtil();
	movHisUtil.setmLogger(mLogger);
	DateUtils _dateUtils = new DateUtils();
	ArrayList movQryList  = new ArrayList();
	ArrayList movItemQryList  = new ArrayList();
	InvMstDAO invmstdao = new InvMstDAO();
	invmstdao.setmLogger(mLogger);
	String  fieldDesc="",errorDesc="",loc="",expiredate="",item="",batch="",REASONCODE="",chkString ="",MODULETYPE ="";
	session= request.getSession();
	String plant 	= (String)session.getAttribute("PLANT");
    String userID 	= (String)session.getAttribute("LOGIN_USER");
	String FROM_DATE ="",  TO_DATE = "", DIRTYPE ="",BATCH ="",USER="",ITEM="",ISPOPUP="",
	fdate="",tdate="",JOBNO="",ITEMNO="",ORDERNO="",ORDERTYPE="",CUSTOMER="",CUSTOMER_TO="",CUSTOMER_LO="",ITEMDESC="",TRANSACTIONDATE,strTransactionDate,PGaction="";
	PGaction        = _strUtils.fString(request.getParameter("PGaction")).trim();
	String html 	= "",cntRec ="false",PRD_BRAND_ID = "",PRD_TYPE_ID="",PRD_CLS_ID="",LOC="",LOC_TYPE_ID="";
	String PLANT 	= "", OrderNo = "", Cname="",Loc = "",Batch="",Item= "",ItemDesc= "",Qty = "",Uom = "",Status="",REFNO="",PrintStatus="";
	String action   = su.fString(request.getParameter("action")).trim();
	String sFrom_Date="",PRINTTYPE="",PRD_CLS_DESC="",PRD_CLS_ID1,PRD_TYPE_DESC="",PRD_TYPE_ID1="",PRD_BRAND_DESC="",ACTIVE="",PRINTSTATUS="";
	FROM_DATE     = _strUtils.fString(request.getParameter("FROM_DATE"));
	TO_DATE   	  = _strUtils.fString(request.getParameter("TO_DATE"));
	
	String value=null,allChecked = "";
	String bgcolor="";
	int iColor=0;
	
	double iordertotal=0;
	double ipicktotal=0;
	double iissuetotal=0;
	double ireversetotal=0;
	int k=0;

	if(FROM_DATE==null) FROM_DATE=""; else FROM_DATE = FROM_DATE.trim();
	String curDate =_dateUtils.getDate();
	if(FROM_DATE.length()<0||FROM_DATE==null||FROM_DATE.equalsIgnoreCase(""))FROM_DATE=curDate;

	if (FROM_DATE.length()>5)
	fdate    = FROM_DATE.substring(6)+ FROM_DATE.substring(3,5)+FROM_DATE.substring(0,2);
	
	if(TO_DATE==null) TO_DATE=""; else TO_DATE = TO_DATE.trim();
	if (TO_DATE.length()>5)
	tdate    = TO_DATE.substring(6)+ TO_DATE.substring(3,5)+TO_DATE.substring(0,2);
	
	DIRTYPE       = _strUtils.fString(request.getParameter("DIRTYPE"));
	JOBNO         = _strUtils.fString(request.getParameter("JOBNO"));
	USER          = _strUtils.fString(request.getParameter("USER"));
	ITEMNO        = _strUtils.fString(request.getParameter("ITEM"));
	BATCH         = _strUtils.fString(request.getParameter("BATCH"));
	ORDERNO       = _strUtils.fString(request.getParameter("ORDERNO"));
	CUSTOMER      = _strUtils.fString(request.getParameter("CUSTOMER"));
	CUSTOMER_TO   = _strUtils.fString(request.getParameter("CUSTOMER_TO"));
	CUSTOMER_LO   = _strUtils.fString(request.getParameter("CUSTOMER_LO"));
	ITEMDESC      = _strUtils.fString(request.getParameter("DESC"));
	ORDERTYPE= _strUtils.fString(request.getParameter("ORDERTYPE"));
	PRD_TYPE_ID = _strUtils.fString(request.getParameter("PRD_TYPE_ID"));
	PRD_BRAND_ID = _strUtils.fString(request.getParameter("PRD_BRAND_ID"));
	PRD_CLS_ID = _strUtils.fString(request.getParameter("PRD_CLS_ID"));
	REASONCODE = _strUtils.fString(request.getParameter("REASONCODE"));
	LOC = _strUtils.fString(request.getParameter("LOC"));
	LOC_TYPE_ID = _strUtils.fString(request.getParameter("LOC_TYPE_ID"));
	MODULETYPE            = _strUtils.fString(request.getParameter("MODULENAME"));
	PRD_CLS_DESC=_strUtils.fString(request.getParameter("PRD_CLS_DESC"));
	PRD_CLS_ID1=_strUtils.fString(request.getParameter("PRD_CLS_ID1"));
	PRD_TYPE_DESC=_strUtils.fString(request.getParameter("PRD_TYPE_DESC"));
	PRD_TYPE_ID1=_strUtils.fString(request.getParameter("PRD_TYPE_ID1"));
	PRD_BRAND_DESC=_strUtils.fString(request.getParameter("PRD_BRAND_DESC"));
	ACTIVE=_strUtils.fString(request.getParameter("ACTIVE"));
	PRINTSTATUS=_strUtils.fString(request.getParameter("PRINTSTATUS"));
	if(PRINTSTATUS.equals(""))
    {
		PRINTSTATUS="N";
	}
	//ISPOPUP=_strUtils.fString(request.getParameter("ISPOPUP"));
	
	if(DIRTYPE.length()<=0){
		DIRTYPE = "LABEL PRINT GOODS ISSUE";
	}
	ItemMstDAO itemdao = new ItemMstDAO();
	itemdao.setmLogger(mLogger);
	if(PGaction.equalsIgnoreCase("View")){
  	try{
  		 Hashtable htItem = new Hashtable();
        if(_strUtils.fString(ITEMNO).length() > 0)             htItem.put("ITEM",ITEMNO);
        if(_strUtils.fString(BATCH).length() > 0)              htItem.put("BATCH",BATCH);
        if(_strUtils.fString(ORDERNO).length() > 0)            htItem.put("DONO",ORDERNO);
        if(_strUtils.fString(CUSTOMER).length() > 0)           htItem.put("CNAME",CUSTOMER);
        if(_strUtils.fString(CUSTOMER_LO).length() > 0)        htItem.put("CNAME_LON",CUSTOMER_LO);
        if(_strUtils.fString(CUSTOMER_TO).length() > 0)        htItem.put("CNAME_TO",CUSTOMER_TO);
        if (_strUtils.fString(ORDERTYPE).length() > 0)   	   htItem.put("ORDERTYPE", ORDERTYPE);
        if(_strUtils.fString(PRD_TYPE_ID).length() > 0)        htItem.put("ITEMTYPE",PRD_TYPE_ID);
        if(_strUtils.fString(PRD_BRAND_ID).length() > 0)       htItem.put("PRD_BRAND_ID",PRD_BRAND_ID);
        if(_strUtils.fString(PRD_CLS_ID).length() > 0)         htItem.put("PRD_CLS_ID",PRD_CLS_ID);
        if(_strUtils.fString(REASONCODE).length() > 0)     	   htItem.put("REMARK",REASONCODE);
        if(_strUtils.fString(LOC).length() > 0)         	   htItem.put("LOC",LOC);
        if(_strUtils.fString(LOC_TYPE_ID).length() > 0)        htItem.put("LOC_TYPE_ID",LOC_TYPE_ID);
        movItemQryList = movHisUtil.getGoodsIssuePrintItemList(htItem,fdate,tdate,"GOODS ISSUE PRINT",plant,StrUtils.replaceCharacters2Send(ITEMDESC));
        REFNO=(String)request.getSession().getAttribute("refNo");
        ISPOPUP=(String)request.getSession().getAttribute("ISPOPUP");
            
     	errorDesc=(String)request.getSession().getAttribute("RESULTERROR");
    	if(errorDesc.length() != 0 && !errorDesc.equals(null))
    	{
        	fieldDesc = "<font class='mainred'>" + errorDesc + "</font>";
      		allChecked = su.fString(request.getParameter("allChecked"));
    	}
    	else
    	{    
    		System.out.println("ISPOPUP"+ISPOPUP);
    		if(ISPOPUP.equals("OPENED")){
    		if(REFNO.length()>0  )
    		{
    			
    			PrintWriter outWriter = response.getWriter();
    			outWriter.println("<script type=\"text/javascript\">");
    			outWriter.println("window.open('LabelSettings.jsp?PRINTTYPE=GOODS ISSUE','LabelSetting','toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=750,height=400,left=200,top=184')");
    		    outWriter.println("</script>");
    		  }
    		}
    	
    	}
  request.getSession().setAttribute("RESULT","");
  request.getSession().setAttribute("RESULTERROR","");
     
 }catch(Exception e) { 
	  fieldDesc="<font class='mainred'>"+e.getMessage()+"</font>";
	  if(e.getMessage()==null)
	  {
		  fieldDesc="";
	  }
 }
 	
}
	
	

%>
<%@ include file="body.jsp"%>
<FORM name="form1" method="post"  action="/track/LabelPrintServlet?">
<input type="hidden" name="xlAction" value="">
<input type="hidden" name="PGaction" value="View">
 <br>
 <TABLE border="0" width="100%" cellspacing="0" cellpadding="0" align="center" bgcolor="#dddddd">
    <TR>
      <TH BGCOLOR="#000066" COLSPAN="20"><font color="white">Label Print (Goods Issue)</font></TH>
    </TR>
  </TABLE>
  <br>
   <Center>
  <table  border="0" cellspacing="1" cellpadding="2"  bgcolor="">
   <%=fieldDesc%>
 </table>
  </Center>
  <TABLE border="0" width="80%" height = "20%" cellspacing="0" cellpadding="0" align="center" bgcolor="#dddddd">
    <TR>
          <TH ALIGN="left" >&nbsp;From_Date : </TH>
          <TD><INPUT name="FROM_DATE" type = "TEXT" value="<%=FROM_DATE%>" size="20"  MAXLENGTH=20 class = "inactivegry" READONLY>&nbsp;&nbsp;<a href="javascript:show_calendar('form1.FROM_DATE');" onmouseover="window.status='Date Picker';return true;" onmouseout="window.status='';return true;"><img src="images\show-calendar.gif" width=24 height=22 border=0></a></TD>
          <TH ALIGN="left">&nbsp;To_Date : </TH>
          <TD><INPUT name="TO_DATE" type = "TEXT" value="<%=TO_DATE%>" size="20"  MAXLENGTH=20 class = "inactivegry" READONLY>&nbsp;&nbsp;<a href="javascript:show_calendar('form1.TO_DATE');" onmouseover="window.status='Date Picker';return true;" onmouseout="window.status='';return true;"><img src="images\show-calendar.gif" width=24 height=22 border=0></a></TD>
	 </TR>
     <TR>
          <TH ALIGN="left">&nbsp;Product ID : </TH>
          <TD><INPUT name="ITEM" type = "TEXT" value="<%=ITEMNO%>" size="20"  MAXLENGTH=50></TD>
          
          
		<TH ALIGN="left">&nbsp;Location :</TH>
		<TD><INPUT name="LOC" type="TEXT" value="<%=LOC%>"
			size="20" MAXLENGTH=50>
		<a href="#" onClick="javascript:popUpWin('loc_list_inventory.jsp?LOC='+form1.LOC.value);">
           <img src="images/populate.gif" border="0"/>
          </a>
	   </TD>
	 </TR>
      <TR>
          <TH ALIGN="left">&nbsp;Product Description : </TH>
          <TD><INPUT name="DESC" type = "TEXT" value="<%=_strUtils.forHTMLTag(ITEMDESC)%>" size="20"  MAXLENGTH=100></TD>
          
          <TH ALIGN="left">&nbsp;Location Type: </TH>
		<TD><INPUT name="LOC_TYPE_ID"  type="TEXT" value="<%=LOC_TYPE_ID%>"
			size="20" MAXLENGTH=20>
			<a href="#"	onClick="javascript:popUpWin('LocTypeList.jsp?LOC_TYPE_ID='+form1.LOC_TYPE_ID.value);"><img
			src="images/populate.gif" border="0"></a></TD>
		</TR>
	
    <TR>
          <TH ALIGN="left" >&nbsp;Batch : </TH>
          <TD><INPUT name="BATCH" type = "TEXT" value="<%=BATCH%>" size="20"  MAXLENGTH=40></TD>
           <TH ALIGN="left">&nbsp;Customer Name : </TH>
          <TD><INPUT name="CUSTOMER" type = "TEXT" value="<%=CUSTOMER%>" size="20"  MAXLENGTH=20></TD>
          
         
    </TR>
      <TR>
           <TH ALIGN="left" >&nbsp;Order No :</TH>
          <TD><INPUT name="ORDERNO" type = "TEXT" value="<%=ORDERNO%>" size="20"  MAXLENGTH=20></TD>
          
           <TH ALIGN="left">&nbsp;TO-Assignee Name : </TH>
          <TD><INPUT name="CUSTOMER_TO" type = "TEXT" value="<%=CUSTOMER_TO%>" size="20"  MAXLENGTH=20></TD>
       </TR>
    <TR>
	    <TH ALIGN="left">&nbsp;Order Type :</TH>
		<TD><INPUT name="ORDERTYPE" type="TEXT" value="<%=ORDERTYPE%>"
			size="20" MAXLENGTH=50></TD>
			
			 <TH ALIGN="left">&nbsp;Loan Assignee Name : </TH>
          <TD><INPUT name="CUSTOMER_LO" type = "TEXT" value="<%=CUSTOMER_LO%>" size="20"  MAXLENGTH=20></TD>
       </TR>
          <TR>
               
            <TH ALIGN="left" width="15%"> &nbsp;&nbsp;Product Class ID:  </TH>
          <TD width="13%"><INPUT name="PRD_CLS_ID" type = "TEXT" value="<%=PRD_CLS_ID%>" size="20"  MAXLENGTH=20>
           <a href="#" onClick="javascript:popUpWin('activePrdClsList.jsp?PRD_CLS_ID='+form1.PRD_CLS_ID.value);">
           <img src="images/populate.gif" border="0"/>
          </a>
          </TD>
          <TH ALIGN="left" width="15%"> &nbsp;&nbsp;Product Type ID:  </TH>
          <TD width="13%"><INPUT name="PRD_TYPE_ID" type = "TEXT" value="<%=PRD_TYPE_ID%>" size="20"  MAXLENGTH=20>
           <a href="#" onClick="javascript:popUpWin('activeproductTypeList.jsp?ITEM_ID='+form1.PRD_TYPE_ID.value);"><img src="images/populate.gif" border="0"/>
          </TD>	
        
        </TR>
        <TR>
        <TH ALIGN="left" width="15%"> &nbsp;&nbsp;Product Brand ID:  </TH>
          <TD width="13%"><INPUT name="PRD_BRAND_ID" type = "TEXT" value="<%=PRD_BRAND_ID%>" size="20"  MAXLENGTH=20>
           <a href="#" onClick="javascript:popUpWin('PrdBrandList.jsp?ITEM_ID='+form1.PRD_BRAND_ID.value+'&Cond=OnlyActive&formName=form1');">           
           <img src="images/populate.gif" border="0"/>
          </a>
          </TD>
          
           <TH ALIGN="left" width="15%"> &nbsp;&nbsp;Reason Code:  </TH>
          <TD><INPUT name="REASONCODE" type = "TEXT" value="<%=REASONCODE%>" size="20"  MAXLENGTH=20>
          <a href="#" onClick="javascript:popUpWin('miscreasoncode.jsp?ITEMNO='+form1.ITEM.value+'&FORM=form1');">
					<img src="images/populate.gif" border="0" /> </a>
					</TD>
        
        </TR>
        <TR>
        <TH ALIGN="left" >&nbsp;&nbsp;Print Type:  </TH>
             <TD><select name="MODULENAME" size='1'>
            <option selected value="">Choose</option>
            <option value='ISSUE' <%if(MODULETYPE.equalsIgnoreCase("ISSUE")){%>selected <%}%>>ISSUE</option>
            <option value='MISC_ISSUE' <%if(MODULETYPE.equalsIgnoreCase("MISC_ISSUE")){ %>selected <%}%>>MISC_ISSUE</option>
            <option value='LOAN' <%if(MODULETYPE.equalsIgnoreCase("LOAN")){ %>selected <%}%>>LOAN</option>
            <option value='TRANSFER' <%if(MODULETYPE.equalsIgnoreCase("TRANSFER")){ %>selected <%}%>>TRANSFER</option>
         </select></TD>
         <TH ALIGN="left" width="15%">&nbsp;&nbsp;Print Status:  </TH>
	
			<TD >
				<INPUT name="PRINTSTATUS" type = "radio"  value="N"  id="NotPrinted" <%if(PRINTSTATUS.equalsIgnoreCase("N")) {%>checked <%}%>>N
		 		<INPUT  name="PRINTSTATUS" type = "radio" value="C"  id = "Printed" <%if(PRINTSTATUS.equalsIgnoreCase("C")) {%>checked <%}%>>Y
		 		<INPUT  name="PRINTSTATUS" type = "radio" value="ALL"  id = "ALL" <%if(PRINTSTATUS.equalsIgnoreCase("ALL")) {%>checked <%}%>>All
		 	</td> 
		 	</TR>
		<TR>
          <TD></TD>
          <TD></TD>
          <TD></TD>
          
          <TD ALIGN="left"><input type="button" value="View"  onClick="javascript:return onGo();"></TD>
        </TR>

 <input type="hidden" name="LOC_DESC" value="">
	 
  </TABLE>
	<br>
<table BORDER = "1" WIDTH = "92%" align="center" bgcolor="#dddddd" >
		<tr>
		<td width = "15%">  
			<INPUT Type=Checkbox  style="border:0;" name = "select" value="select" <%if(allChecked.equalsIgnoreCase("true")){%>checked <%}%>onclick="return checkAll(this.checked);">
        	&nbsp; Select/Unselect All 
         </td>
		<tr>
</table>
  <TABLE WIDTH="95%"  border="0" cellspacing="1" cellpadding = 2 align = "center">
 
    <TR BGCOLOR="#000066">
        <!--  <TH><font color="#ffffff" align="center">S/N</TH> -->
         <TH><font color="#ffffff" align="left"><b>Chk</TH>
         <TH><font color="#ffffff" align="left"><b>Order No</TH>
          <TH><font color="#ffffff" align="left"><b>Customer Name</TH>
         <TH><font color="#ffffff" align="left"><b>Product ID</TH>
         <TH><font color="#ffffff" align="left"><b>Description</TH>
         <TH><font color="#ffffff" align="left"><b>Loc</TH>
         <TH><font color="#ffffff" align="left"><b>Batch</TH>
         <TH><font color="#ffffff" align="left"><b>Qty</TH>
         <TH><font color="#ffffff" align="left"><b><%=IDBConstants.UOM_LABEL%></TH>
	     <TH><font color="#ffffff" align="left"><b>Status</TH>
	     <TH><font color="#ffffff" align="left"><b>Print Status</TH>
         </tr>
       <%
	      if(movItemQryList.size()<=0 ){ %>
		    <TR><TD colspan=15 align=center>No Data For This criteria</TD></TR>
		  <%}%>

		  <%
		   for(int iItemCnt = 0; iItemCnt < movItemQryList.size(); iItemCnt++) {
			  		 					
			    Map lineItemArr = (Map) movItemQryList.get(iItemCnt);
				int iItemIndex = iItemCnt + 1;
		    	try{
			 		
				 		 ItemMstUtil itemMstUtil = new ItemMstUtil();
						 itemMstUtil.setmLogger(mLogger);
						 String temItem = itemMstUtil.isValidAlternateItemInItemmst( plant, (String)lineItemArr.get("item"));
						 if (temItem != "") {
						 	ITEMNO = temItem;
						 } else {
						 	throw new Exception("Product not found!");
						 }
			      
			       Hashtable ht = new Hashtable();
			       if(DIRTYPE.equalsIgnoreCase("GOODS ISSUE PRINT"))
			       {
			         if(_strUtils.fString(ITEMNO).length() > 0)            ht.put("ITEM",ITEMNO);
			        if(_strUtils.fString(BATCH).length() > 0)              ht.put("BATCH",BATCH);
			        if(_strUtils.fString(ORDERNO).length() > 0)            ht.put("DONO",ORDERNO);
			        if(_strUtils.fString(CUSTOMER).length() > 0)           ht.put("CNAME",CUSTOMER);
			        if(_strUtils.fString(CUSTOMER_LO).length() > 0)        ht.put("CNAME_LON",CUSTOMER_LO);
			        if(_strUtils.fString(CUSTOMER_TO).length() > 0)        ht.put("CNAME_TO",CUSTOMER_TO);
			        if (_strUtils.fString(ORDERTYPE).length() > 0)   	   ht.put("ORDERTYPE", ORDERTYPE);
			        if(_strUtils.fString(PRD_TYPE_ID).length() > 0) 	   ht.put("ITEMTYPE",PRD_TYPE_ID);
			        if(_strUtils.fString(PRD_BRAND_ID).length() > 0)       ht.put("PRD_BRAND_ID",PRD_BRAND_ID);
			        if(_strUtils.fString(PRD_CLS_ID).length() > 0)         ht.put("PRD_CLS_ID",PRD_CLS_ID);
			        if(_strUtils.fString(REASONCODE).length() > 0)         ht.put("REMARK",REASONCODE);
			        if(_strUtils.fString(LOC).length() > 0)         	   ht.put("LOC",LOC);
			        if(_strUtils.fString(LOC_TYPE_ID).length() > 0)        ht.put("LOC_TYPE_ID",LOC_TYPE_ID);
			        if(PRINTSTATUS.equals("N") || PRINTSTATUS.equals("C"))  ht.put("PRINTSTATUS",PRINTSTATUS); 
			       
			       }
			       if (MODULETYPE.equalsIgnoreCase("ISSUE")){
			    	   movQryList = movHisUtil.getGoodsIssuePrintingListWithType(ht,fdate,tdate,"ISSUE",plant,ITEMDESC);
			       }
			       else if(MODULETYPE.equalsIgnoreCase("MISC_ISSUE")){
			    	   movQryList = movHisUtil.getGoodsIssuePrintingListWithType(ht,fdate,tdate,"MISC_ISSUE",plant,ITEMDESC);
			       }
			       else if(MODULETYPE.equalsIgnoreCase("LOAN")){
			    	   movQryList = movHisUtil.getGoodsIssuePrintingListWithType(ht,fdate,tdate,"LOAN",plant,ITEMDESC);
			       }
			       else if(MODULETYPE.equalsIgnoreCase("TRANSFER")){
			    	   movQryList = movHisUtil.getGoodsIssuePrintingListWithType(ht,fdate,tdate,"TRANSFER",plant,ITEMDESC);
			       }
			       else
			       {
			       	   movQryList = movHisUtil.getGoodsIssuePrintingList(ht,fdate,tdate,"GOODS ISSUE PRINT",plant,ITEMDESC);
			       }
			       
			      
			      
			 }catch(Exception e) { 
				  fieldDesc="<font class='mainred'>"+e.getMessage()+"</font>";
			 }
				
			 
		   strTransactionDate="";
		    for (int iCnt =0; iCnt< movQryList.size(); iCnt++){
			    Map lineArr = (Map) movQryList.get(iCnt);
         		int iIndex = iCnt + 1;
         		
        	    bgcolor = ((iColor == 0) || (iColor % 2 == 0)) ? "#FFFFFF"  : "#dddddd";
        		String uom = itemdao.getItemUOM(plant,(String)lineArr.get("item"));
        		loc = (String)lineArr.get("loc");
        		String locarry[] = loc.split("_");
        		loc = locarry[locarry.length-1];
        		item = (String)lineArr.get("item");
          		 OrderNo = (String)lineArr.get("dono");
		         Cname = (String)lineArr.get("cname");
				 Item= (String)lineArr.get("item");
				 ItemDesc= (String)lineArr.get("itemdesc");
				 Loc = (String)lineArr.get("loc");
		         Batch= (String)lineArr.get("batch");
		         Qty= (String)lineArr.get("issueqty");
		         Uom=  itemdao.getItemUOM(plant,item);
		         Status= (String)lineArr.get("status");
		         if(Status.equals(""))
		         {
		        	 Status="Empty";  // to overcome array null point exception in LabelPrintServlet LabelPrint Method
		         }
		         PrintStatus= (String)lineArr.get("printstatus");
		         chkString  =OrderNo+",,,"+StrUtils.replaceCharacters2Send(Cname)+",,,"+Item+",,,"+StrUtils.replaceCharacters2Send(ItemDesc)+",,,"+Loc+",,,"+Batch+",,,"+Qty+",,,"+StrUtils.replaceCharacters2Send(Uom)+",,,"+Status;
		         String lineno=String.valueOf(iIndex)+"_"+OrderNo+"_"+Loc+"_"+Batch;
		         if(Status.equals("Empty"))
		         {
		        	 Status="";
		         }
		       
        	%>

            <TR bgcolor = "<%=bgcolor%>">
            
            <TD width="5%" align="CENTER"><font color="black"><INPUT Type=checkbox style="border: 0;" name="chkdLnNo" 	value="<%=chkString%>" ></font></TD>
            
            <TD width="5%" align="center"><%=OrderNo%></TD>
             <input type="hidden" name = "OrderNo_<%=lineno%>" id = "OrderNo_<%=lineno%>"  	 value = <%=OrderNo%> ></input>
					
			
			<TD align="center" width="9%"><%=Cname%></TD>
			<input type="hidden" name = "Cname_<%=lineno%>" id = "Cname_<%=lineno%>"   value = <%=Cname%> ></input>
			
			<TD align="center" width="13%"><%=Item%></TD>
			<input type="hidden" name = "Item_<%=lineno%>" id = "Item_<%=lineno%>"   value = <%=Item%> ></input>
			 
			 
			<TD align="center" width="15%"><%=ItemDesc%></TD>
            <input type="hidden" name = "ItemDesc_<%=lineno%>" id = "ItemDesc_<%=lineno%>"  value = <%=ItemDesc%> ></input>
			
			<TD align="center" width="9%"><%=Loc%></TD>
			<input type="hidden" name = "Loc_<%=lineno%>" id = "Loc_<%=lineno%>" value = <%=Loc%> ></input>
			
            <TD align="center" width="8%"><%=Batch%></TD>
            <input type="hidden" name = "Batch_<%=lineno%>" id = "Batch_<%=lineno%>"  value = <%=Batch%> ></input>
			
			<TD align="center" width="9%"><%=Qty%></TD>
			<input type="hidden" name = "Qty_<%=lineno%>" id = "Qty_<%=lineno%>" value = <%=Qty%> ></input>
			
			<TD align="center" width="10%"><%=Uom%></TD>
			<input type="hidden" name = "Uom_<%=lineno%>" id = "Uom_<%=lineno%>"  value = <%=Uom%> ></input>
			
			<TD align="center" width="8%"><%=Status%></TD>
			<input type="hidden" name = "Status_<%=lineno%>" id = "Status_<%=lineno%>"  value = <%=Status%> ></input>
			
			<TD align="center" width="9%"><%=PrintStatus%></TD>
			<input type="hidden" name = "PrintStatus_<%=lineno%>" id = "PrintStatus_<%=lineno%>"  value = <%=PrintStatus%> ></input>
			             
          </TR>
                    
       <%
       iColor=iColor+1;
     }
		   
     	if(k==0)
			k=1;
	  }
		  if( movQryList.size()<=0){ cntRec ="true";%>
			
	 <% }%>  
	
    </TABLE>
      <br>
      <br><br>
    <table align="center" >
     <TR>
   <td>  <input type="button" value=" Back "  onClick="window.location.href='LabelPrintMenu.jsp'">&nbsp; </td>
   <td>   <input type="button" value="Submit" onClick="onPrint();" > </td>
   <INPUT type="Hidden" name="DIRTYPE" value="GOODS ISSUE PRINT">
   <INPUT type="Hidden" name="REFNO" value=<%=REFNO%>>
    <INPUT type="Hidden" name="PRD_CLS_DESC" value=<%=PRD_CLS_DESC%>>
   <INPUT type="Hidden" name="PRD_CLS_ID1" value=<%=PRD_CLS_ID1%>>
   <INPUT type="Hidden" name="PRD_TYPE_DESC" value=<%=PRD_TYPE_DESC%>>
   <INPUT type="Hidden" name="PRD_TYPE_ID1" value=<%=PRD_TYPE_ID1%>>
   <INPUT type="Hidden" name="PRD_BRAND_DESC" value=<%=PRD_BRAND_DESC%>>
    <INPUT type="Hidden" name="ACTIVE" value=<%=ACTIVE%>>
    <input type="hidden" name="visited" value="" />
   
  
  </TR>
    </table>
       <INPUT name="JOBNO" type = "Hidden" value="<%=JOBNO%>" size="20"  MAXLENGTH=20>
  </FORM>

<%@ include file="footer.jsp"%>
 
