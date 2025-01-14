<%@ page import="com.track.util.*"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.gates.*"%>
<%@ page import="com.track.constants.*"%>
<%@ page import="com.track.dao.CustMstDAO"%>
<%@ page import="com.track.dao.MovHisDAO"%>
<%@ page import="com.track.dao.TblControlDAO"%>
<%@ include file="header.jsp"%>

<html>
<script language="JavaScript" type="text/javascript" src="js/general.js"></script>

<title>Register Details</title>
<link rel="stylesheet" href="css/style.css">

<SCRIPT LANGUAGE="JavaScript">

var subWin = null;
function popUpWin(URL) {
 subWin = window.open(URL, 'CUSTOMERS', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=700,height=400,left = 200,top = 184');
}
function onNew(){

   document.form.action  = "maint_customer.jsp?action=NEW";
   document.form.submit();
}
function onAdd(){
   var CUST_CODE   = document.form.CUST_CODE.value;
   var CUST_NAME   = document.form.CUST_NAME.value;
   if(CUST_CODE == "" || CUST_CODE == null) {alert("Please Choose Customer Code"); return false; }
   
   if(CUST_NAME == "" || CUST_NAME == null) {
   alert("Please enter Customer Name"); 
   document.form.CUST_NAME.focus();
   return false; 
   }
   
   document.form.action  = "maint_customer.jsp?action=ADD";
   document.form.submit();
}
function onUpdate(){
   var CUST_CODE   = document.form.CUST_CODE.value;
   if(CUST_CODE == "" || CUST_CODE == null) {alert("Please Enter Customer Code"); return false; }

   document.form.action  = "maint_customer.jsp?action=UPDATE";
   document.form.submit();
}
function onDelete(){

   var CUST_CODE   = document.form.CUST_CODE.value;
   if(CUST_CODE == "" || CUST_CODE == null) {alert("Please Enter Customer Code");  return false; }
   confirm("Do you want to delete Customer permanently ");
   document.form.action  = "maint_customer.jsp?action=DELETE";
   document.form.submit();
}
function onView(){
   var CUST_CODE   = document.form.CUST_CODE.value;
   if(CUST_CODE == "" || CUST_CODE == null) {alert("Please Enter Customer Code"); return false; }

   document.form.action  = "maint_customer.jsp?action=VIEW";
   document.form.submit();
}
function onIDGen()
{
 	document.form.action  = "maint_customer.jsp?action=Auto-ID";
    document.form.submit(); 

}

</script>
<%
	String sUserId = (String) session.getAttribute("LOGIN_USER");
	String res        = "";
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
    sCountry   = "",
    sZip       = "",
    sCons      = "Y";
 	String sContactName ="", sDesgination="",sTelNo="",sHpNo="",sFax="",sEmail="",sRemarks="",isActive="";
	StrUtils strUtils = new StrUtils();
	CustUtil custUtil = new CustUtil();
	action            = strUtils.fString(request.getParameter("action"));
	String plant = strUtils.fString((String)session.getAttribute("PLANT"));

	sCustCode  = strUtils.fString(request.getParameter("CUST_CODE"));
	DateUtils dateutils = new DateUtils();
	if(sCustCode.length() <= 0) sCustCode  = strUtils.fString(request.getParameter("CUST_CODE1"));
	sCustName  = strUtils.replaceCharacters2Recv(strUtils.fString(request.getParameter("CUST_NAME")));
	sCustNameL  = strUtils.replaceCharacters2Recv(strUtils.fString(request.getParameter("L_CUST_NAME")));
	sAddr1     = strUtils.replaceCharacters2Recv(strUtils.fString(request.getParameter("ADDR1")));
	sAddr2     =strUtils.replaceCharacters2Recv(strUtils.fString(request.getParameter("ADDR2")));
	sAddr3     =strUtils.replaceCharacters2Recv( strUtils.fString(request.getParameter("ADDR3")));
	sAddr4     = strUtils.replaceCharacters2Recv(strUtils.fString(request.getParameter("ADDR4")));
	sCountry   = strUtils.replaceCharacters2Recv(strUtils.fString(request.getParameter("COUNTRY")));
	sZip       = strUtils.fString(request.getParameter("ZIP"));
	sCons      = strUtils.fString(request.getParameter("CONSIGNMENT"));
	sContactName      = strUtils.replaceCharacters2Recv(strUtils.fString(request.getParameter("CONTACTNAME")));
	sDesgination  = strUtils.replaceCharacters2Recv(strUtils.fString(request.getParameter("DESGINATION")));
	sTelNo  = strUtils.fString(request.getParameter("TELNO"));
	sHpNo  = strUtils.fString(request.getParameter("HPNO"));
	sFax  = strUtils.fString(request.getParameter("FAX"));
	sEmail= strUtils.replaceCharacters2Recv(strUtils.fString(request.getParameter("EMAIL")));
	sRemarks= strUtils.replaceCharacters2Recv(strUtils.fString(request.getParameter("REMARKS")));
        isActive = strUtils.fString(request.getParameter("ISACTIVE"));


%>

<%@ include file="body.jsp"%>
<FORM name="form" method="post">
  <br>
  <CENTER>
  <TABLE border="0" width="100%" cellspacing="0" cellpadding="0" align="center" bgcolor="#dddddd">
    <TR>
      <TH BGCOLOR="#000066" COLSPAN="11"><font color="white">Register Details</font></TH>
    </TR>
  </TABLE>
  <br>
  <TABLE border="0" CELLSPACING=0 WIDTH="100%" bgcolor="#dddddd">
    <TR>
         <TH WIDTH="35%" ALIGN="RIGHT" >* Customer Code : </TH>
         <TD>
                <INPUT name="CUST_CODE" type = "TEXT" value="<%=sCustCode%>" size="50" readonly="readonly"  MAXLENGTH=20 <%=sCustEnb%> >
                <INPUT type = "hidden" name="CUST_CODE1" value = <%=sCustCode%>>
                  
         </TD>
    </TR>
    <TR>
         <TH WIDTH="35%" ALIGN="RIGHT" > Customer Name : </TH>
         <TD>
             <INPUT name="CUST_NAME" type = "TEXT" value="<%=sCustName%>" size="50"  MAXLENGTH=80 readonly="readonly">
           
         </TD>

    </TR>
    
     <TR>
      <TH WIDTH="35%" ALIGN="RIGHT">Designation:</TH>
      <TD>
        <INPUT name="DESGINATION" type="TEXT" value="<%=sDesgination%>" size="50" MAXLENGTH="80" readonly="readonly"/>
      </TD>
    </TR>
    
         
        <INPUT name="L_CUST_NAME" type = "hidden" value="<%=sCustNameL%>" size="50"  MAXLENGTH=80 readonly="readonly">
  
    <TR>
      <TH WIDTH="35%" ALIGN="RIGHT">Contact Details:</TH>
      <TD>&nbsp;</TD>
    </TR>
    <TR>
      <TH WIDTH="35%" ALIGN="RIGHT">Company Name:</TH>
      <TD>
        <INPUT name="CONTACTNAME" type="TEXT" value="<%=sContactName%>" size="50" MAXLENGTH="80" readonly="readonly"/>
      </TD>
    </TR>
   
    <TR>
      <TH WIDTH="35%" ALIGN="RIGHT">Telephone No:</TH>
      <TD>
        <INPUT name="TELNO" type="TEXT" value="<%=sTelNo%>" size="50" MAXLENGTH="10" readonly="readonly"/>
      </TD>
    </TR>
    <TR>
      <TH WIDTH="35%" ALIGN="RIGHT">Hand Phone No:</TH>
      <TD>
        <INPUT name="HPNO" type="TEXT" value="<%=sHpNo%>" size="50" MAXLENGTH="10" readonly="readonly"/>
      </TD>
    </TR>
    <TR>
      <TH WIDTH="35%" ALIGN="RIGHT">Fax:</TH>
      <TD>
        <INPUT name="FAX" type="TEXT" value="<%=sFax%>" size="50" MAXLENGTH="10" readonly="readonly"/>
      </TD>
    </TR>
    <TR>
      <TH WIDTH="35%" ALIGN="RIGHT">Email:</TH>
      <TD>
        <INPUT name="EMAIL" type="TEXT" value="<%=sEmail%>" size="50" MAXLENGTH="30" readonly="readonly"/>
      </TD>
    </TR>
    <TR>
         <TH WIDTH="35%" ALIGN="RIGHT">Unit No  : </TH>
         <TD><INPUT name="ADDR1" type = "TEXT" value="<%=sAddr1%>" size="40"  MAXLENGTH=40  readonly="readonly"></TD>
    </TR>
    <TR>
         <TH WIDTH="35%" ALIGN="RIGHT" >Building : </TH>
         <TD><INPUT name="ADDR2" type = "TEXT" value="<%=sAddr2%>" size="40"  MAXLENGTH=40 readonly="readonly"></TD>
    </TR>
    <TR>
         <TH WIDTH="35%" ALIGN="RIGHT" >Street : </TH>
         <TD><INPUT name="ADDR3" type = "TEXT" value="<%=sAddr3%>" size="40"  MAXLENGTH=40 readonly="readonly"></TD>
    </TR>
    <TR>
         <TH WIDTH="35%" ALIGN="RIGHT" >City : </TH>
         <TD><INPUT name="ADDR4" type = "TEXT" value="<%=sAddr4%>" size="40"  MAXLENGTH=40 readonly="readonly"></TD>
    </TR>
    <TR>
         <TH WIDTH="35%" ALIGN="RIGHT" >Country : </TH>
         <TD><INPUT name="COUNTRY" type = "TEXT" value="<%=sCountry%>" size="40"  MAXLENGTH=40 readonly="readonly"></TD>
    </TR>
    <TR>
         <TH WIDTH="35%" ALIGN="RIGHT" >Postal&nbsp;Code : </TH>
         <TD><INPUT name="ZIP" type = "TEXT" value="<%=sZip%>" size="10"  MAXLENGTH=9 readonly="readonly"></TD>
    </TR>
     <TR>
         <TH WIDTH="35%" ALIGN="RIGHT" >Remarks : </TH>
         <TD><INPUT name="REMARKS" type = "TEXT" value="<%=sRemarks%>" size="50"  MAXLENGTH=30 readonly="readonly"></TD>
    </TR>
    
    <tr>
           <td>&nbsp;&nbsp;</td>
            <TD align=" left"><INPUT name="ACTIVE" type = "radio" value="Y"   disabled="disabled"  <%if(isActive.equalsIgnoreCase("Y")) {%>checked <%}%>  >Active 
            <INPUT name="ACTIVE" type = "radio" value="N"    disabled="disabled" <%if(isActive.equalsIgnoreCase("N")) {%>checked <%}%>  >Non Active </TD>
    </tr>
 
</TABLE>
</CENTER>

</FORM>
</BODY>
</HTML>
<%@ include file="footer.jsp"%>

