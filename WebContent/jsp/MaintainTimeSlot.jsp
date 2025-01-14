<%@ page import="com.track.util.*"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.gates.*"%>
<%@ page import="com.track.constants.*"%>
<%@ page import="com.track.dao.TblControlDAO"%>
<%@ include file="header.jsp"%>
<html>
<script language="JavaScript" type="text/javascript" src="js/general.js"></script>
<title>Maintain	Time Slot</title>
<link rel="stylesheet" href="css/style.css">
<SCRIPT LANGUAGE="JavaScript">

	var subWin = null;
	function popUpWin(URL) {
		subWin = window.open(
						URL,
						'EditTimeSlot',
						'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=700,height=400,left = 200,top = 184');
	}
	
			
	function onUpdate()
	{
	
	    if(document.form.FROMTIME.selectedIndex==0)
		{
			alert("Please choose TIMESLOTS");
			document.form.FROMTIME.focus();
			return false;
		}
	    
	   
		
		
		if(isNaN(document.form.QTY.value))
		{
			alert("Please enter valid  Qty.");
		    document.form.QTY.focus();
		    return false;
		}
		
		document.form.action  =  "/track/TimeSlotServlet?Submit=Update";
   		document.form.submit();
	
	}
	
	function onDelete()
	{
	
	    if(document.form.FROMTIME.selectedIndex==0)
		{
			alert("Please choose TIMESLOTS");
			document.form.FROMTIME.focus();
			return false;
		}
	
	
		if(isNaN(document.form.QTY.value))
		{
			alert("Please enter valid  Qty.");
		    document.form.QTY.focus();
		    return false;
		}
		
		
		document.form.action  =  "/track/TimeSlotServlet?Submit=Delete";
   		document.form.submit();
	
	}
	
	function onClear()
	{
		document.form.FROMTIME.selectedIndex=0;
		document.form.QTY.value="";
		document.form.REMARKS.value="";
			
	}

</script>
<%
	session = request.getSession();
	String plant = (String) session.getAttribute("PLANT");
	String sUserId = (String) session.getAttribute("LOGIN_USER");
	String result = "";
    String action = "";
	String TIMESLOTS = "",FROMTIME = "", TOTIME = "", MERIDIEN="",QTY = "",REMARKS="" ;
	StrUtils strUtils = new StrUtils();
	TimeSlotUtil _TimeSlotUtil = new TimeSlotUtil();
	
    List timeslotlist =_TimeSlotUtil.getTimeSlots(plant," ");
	result =  strUtils.fString(request.getParameter("result"));
			
	
	FROMTIME   = strUtils.fString(request.getParameter("FROMTIME"));
	QTY        = strUtils.fString(request.getParameter("QTY"));
	REMARKS    = strUtils.fString(request.getParameter("REMARKS"));
	
	try {
		action = strUtils.fString(request.getParameter("action"));
		
		if (action.equalsIgnoreCase("SHOW_RESULT")) {
			Hashtable arrCust = (Hashtable) request.getSession().getAttribute("TimeSlotSession");
			FROMTIME 	= (String) arrCust.get("FROMTIME");
			QTY  		= (String) arrCust.get("QTY");
			REMARKS  	= (String) arrCust.get("REMARKS");
		 }
		
		if (action.equalsIgnoreCase("Delete")) {
			FROMTIME 	= "";
			QTY  		= "";
			REMARKS  	= "";
		 }
		
	} catch (Exception e) {
	}

	if (action.equalsIgnoreCase("Clear")) {
		action = "";
		
	}  
	
%>

<%@ include file="body.jsp"%>
<FORM name="form" method="post"><br>
<CENTER>
<TABLE border="0" width="100%" cellspacing="0" cellpadding="0"
	align="center" bgcolor="#dddddd">
	<TR>
		<TH BGCOLOR="#000066" COLSPAN="11"><font color="white">Maintain
		Time Slot</font></TH>
	</TR>
</TABLE>
<B>
<CENTER><%=result%>
</B> <br>
<TABLE border="0" CELLSPACING=0 WIDTH="100%" bgcolor="#dddddd" >

 <TR>
        <TH  width="10%" ALIGN="right">* TimeSlot :</TH>
		<TD  valign='left'  >
			<SELECT NAME="FROMTIME" size="1">
				<OPTION selected value="NOCLASSIFICATION">Choose</OPTION>
				<% for (int i =0; i<timeslotlist.size(); i++){
                    Map map = (Map) timeslotlist.get(i);
                    String frmtime     = (String) map.get("timeslots");
				  %>
					<OPTION value="<%=frmtime%>"
							<%if(FROMTIME.equalsIgnoreCase(frmtime)){%>selected  <%} %>><%=frmtime%>
							
					</OPTION>
			    <%}%>
			</SELECT>
			
			
			<a href="#"
				onClick="javascript: popUpWin('TimeSlotList.jsp?TIMESLOTS=' + form.FROMTIME.value);"><img
				src="images/populate.gif" border="0"></a> 
			</TD>
			
		</TD>
	
 </TR>
	
	<TR>
		<TH WIDTH="35%" ALIGN="RIGHT">Maximum Quantity For TimeSlot :</TH>
		<TD><INPUT name="QTY" type="TEXT" value="<%=QTY%>"
			size="50" MAXLENGTH=80></TD>
	</TR>
	<TR>
		<TH WIDTH="35%" ALIGN="RIGHT">Remarks :</TH>
		<TD><INPUT name="REMARKS" type="TEXT" value="<%=REMARKS%>"
			size="50" MAXLENGTH=80></TD>
	</TR>

	<TR>
		<TD COLSPAN=2><BR>
		</TD>
	</TR>
	<TR>
	  
		<TD COLSPAN=2>
		<center><INPUT class="Submit" type="BUTTON" value="Back"
			onClick="window.location.href='../home'">&nbsp;&nbsp; <INPUT
			class="Submit" type="BUTTON" value="Clear" onClick="onClear();"	>&nbsp;&nbsp; 
			<INPUT class="Submit" type="BUTTON"
			value="Save" onClick="onUpdate();">&nbsp;&nbsp;
			<INPUT class="Submit" type="BUTTON" value="Delete" onClick="onDelete();" >&nbsp;&nbsp; 
		</TD>
	</TR>
	</TABLE>
<INPUT type="Hidden" name="TIMESLOTS" value="<%=TIMESLOTS%>">
</CENTER>

</FORM>
</BODY>
</HTML>
<%@ include file="footer.jsp"%>

