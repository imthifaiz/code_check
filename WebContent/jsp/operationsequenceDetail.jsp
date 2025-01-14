<%@ page import="com.track.util.*"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.gates.*"%>
<%@ page import="com.track.constants.*"%>
<%@ page import="com.track.dao.MovHisDAO"%>
<%@ page import="com.track.dao.TblControlDAO"%>
<%@ include file="header.jsp"%>

<html>
<script language="JavaScript" type="text/javascript" src="js/general.js"></script>

<title>Operation Sequence Detail</title>
<link rel="stylesheet" href="css/style.css">

<SCRIPT LANGUAGE="JavaScript">

function popWin(URL) {
 subWin = window.open(URL, 'OPRSEQ', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=700,height=400,left = 200,top = 184');
}

</SCRIPT>
<%
String sUserId = (String) session.getAttribute("LOGIN_USER");
String sPlant = (String) session.getAttribute("PLANT");
String res        = "";
String sOprseq ="",sOprseqDesc  =   "",action =   "",fromloc="",toloc="",remarks="";

session= request.getSession();
StrUtils strUtils = new StrUtils();
OrderStatusUtil ordstatusutil = new OrderStatusUtil();
ordstatusutil.setmLogger(mLogger);
DateUtils dateutils = new DateUtils();

action            = strUtils.fString(request.getParameter("action"));
String plant = (String)session.getAttribute("PLANT");
sOprseq  = strUtils.fString(request.getParameter("OPERATIONSEQ"));
sOprseqDesc  = strUtils.replaceCharacters2Recv(strUtils.fString(request.getParameter("OPRSEQ_DESC")));
fromloc  = strUtils.fString(request.getParameter("FROMLOC"));
toloc  = strUtils.fString(request.getParameter("TOLOC"));
remarks  = strUtils.fString(request.getParameter("REMARKS"));

%>

<%@ include file="body.jsp"%>
<FORM name="form1" method="post">
  <br>
  <CENTER>
  <TABLE border="0" width="100%" cellspacing="0" cellpadding="0" align="center" bgcolor="#dddddd">
    <TR>
      <TH BGCOLOR="#000066" COLSPAN="11"><font color="white">Operation Sequence Details</font></TH>
    </TR>
  </TABLE><B><CENTER><%=res%></B>
  <br>
  <TABLE border="0" CELLSPACING=0 WIDTH="100%" bgcolor="#dddddd">
    <TR>
         <TH WIDTH="35%" ALIGN="RIGHT" > Operation SeqNum: </TH>
         <TD>
                <INPUT name="OPR_SEQNUM" type = "TEXT" value="<%=sOprseq%>" size="50"  MAXLENGTH=20 readonly>
               
				 
                   </TD>
    </TR>
    <TR>
         <TH WIDTH="35%" ALIGN="RIGHT" >Description : </TH>
         <TD>
             <INPUT name="OPR_SEQ_DESC" type = "TEXT" value="<%=sOprseqDesc%>" size="50"  MAXLENGTH=50 readonly>

         </TD>

    </TR>
    <TR>
         <TH WIDTH="35%" ALIGN="RIGHT" >From Location  : </TH>
         <TD>
             <INPUT name="LOC_0" type = "TEXT" value="<%=fromloc%>" size="50"  MAXLENGTH=50 readonly>

         </TD>

    </TR>
    <TR>
         <TH WIDTH="35%" ALIGN="RIGHT" >To Location : </TH>
         <TD>
             <INPUT name="LOC_1" type = "TEXT" value="<%=toloc%>" size="50"  MAXLENGTH=50 readonly>

         </TD>

    </TR>
     <TR>
         <TH WIDTH="35%" ALIGN="RIGHT" >Remarks : </TH>
         <TD>
             <INPUT name="REMARKS" type = "TEXT" value="<%=remarks%>" size="50"  MAXLENGTH=50 readonly>

         </TD>

    </TR>
</TABLE>
</CENTER>

</FORM>
</BODY>
</HTML>
<%@ include file="footer.jsp"%>


