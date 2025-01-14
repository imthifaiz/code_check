<%@ page import="com.track.util.*"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.gates.*"%>
<%@ page import="com.track.constants.*"%>
<%@ page import="com.track.dao.TblControlDAO"%>
<%@ include file="header.jsp"%>
<html>
<script language="JavaScript" type="text/javascript" src="js/general.js"></script>
<title>TimeSlot Details</title>
<link rel="stylesheet" href="css/style.css">
<SCRIPT LANGUAGE="JavaScript">
	var subWin = null;
	function popUpWin(URL) {
	 subWin = window.open(URL, 'TIMESLOT', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=500,height=200,left = 200,top = 184');
	}

</script>
<%
	session = request.getSession();
	String plant = (String) session.getAttribute("PLANT");
	String sUserId = (String) session.getAttribute("LOGIN_USER");
	String res        = "";
	

	String action     = "";
	String TIMESLOTS = "",QTY="",REMARKS="";
        
	StrUtils strUtils = new StrUtils();
	

	try	{action= strUtils.fString(request.getParameter("action"));}
	catch(Exception e){	}

	 if(action.equalsIgnoreCase("View")){
   	 	TIMESLOTS   = request.getParameter("TIMESLOTS");
     	REMARKS    = strUtils.replaceCharacters2Recv(request.getParameter("REMARKS"));
     	QTY        = request.getParameter("QTY");
  
    }

%>
<%@ include file="body.jsp"%>
<FORM name="form" method="post">
  <br>
  <CENTER>
  <TABLE border="0" width="100%" cellspacing="0" cellpadding="0" align="center" bgcolor="#dddddd">
    <TR>
      <TH BGCOLOR="#000066" COLSPAN="11"><font color="white"> TIMESLOT DETAILS  </font></TH>
    </TR>
  </TABLE>
  <br>
  <TABLE border="0" CELLSPACING=0 WIDTH="100%" bgcolor="#dddddd">
    <TR>
         <TH WIDTH="35%" ALIGN="RIGHT" > TimeSlot : </TH>
         <TD>
                <INPUT name="TIMESLOTS" type = "TEXT" value="<%=TIMESLOTS%>" readonly size="50"  MAXLENGTH=20  >
               
         </TD>
    </TR>
    <TR>
         <TH WIDTH="35%" ALIGN="RIGHT">Maximum Quantity For TimeSlot : </TH>
         <TD><INPUT name="QTY" type = "TEXT" value="<%=QTY%>" size="50" readonly MAXLENGTH=80></TD>
    </TR>
     <TR>
         <TH WIDTH="35%" ALIGN="RIGHT" >Remarks : </TH>
         <TD><INPUT name="REMARKS" type = "TEXT" value="<%=REMARKS%>" size="50" readonly MAXLENGTH=80></TD>
    </TR>

</TABLE>
</CENTER>

</FORM>
</BODY>
</HTML>
<%@ include file="footer.jsp"%>

