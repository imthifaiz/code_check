<%@ page import="com.track.util.*"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.gates.*"%>
<%@ page import="com.track.constants.*"%>
<%@ page import="com.track.dao.TblControlDAO"%>
<%@ include file="header.jsp"%>
<html>
<script language="JavaScript" type="text/javascript" src="js/general.js"></script>
<title>TimeSlot</title>
<link rel="stylesheet" href="css/style.css">
<SCRIPT LANGUAGE="JavaScript">
	var subWin = null;
			
	function onAdd()
	{

		if(document.form.FROMTIME.selectedIndex==0 )
		{
		   alert("Not a valid TimeSlot");
			document.form.FROMTIME.focus();
			return false;
		   
		}
		
		if(document.form.TOTIME.selectedIndex==0 )
		{
		   alert("Not a valid TimeSlot");
			document.form.TOTIME.focus();
			return false;
		   
		}
		
		if(document.form.MIN1.selectedIndex==0 )
		{
		   alert("Not a valid TimeSlot");
			document.form.MIN1.focus();
			return false;
		   
		}
		
		if(document.form.MIN2.selectedIndex==0 )
		{
		   alert("Not a valid TimeSlot");
			document.form.MIN2.focus();
			return false;
		   
		}
		
		
		
				
		
	  if(document.form.FROMTIME.value==document.form.TOTIME.value)
		{
			if(document.form.MIN1.value==document.form.MIN2.value)
			{
				if(document.form.MERIDIEN1.value==document.form.MERIDIEN2.value)
				{
		   			alert("Not a valid TimeSlot");
					document.form.FROMTIME.focus();
					return false;
				}
			}
		   
		}
		
	
	  
		if(isNaN(document.form.QTY.value))
		{
			alert("Please enter valid  Qty.");
		    document.form.QTY.focus();
		    return false;
		}
		
		document.form.action  =  "/track/TimeSlotServlet?Submit=Save";
   		document.form.submit();
	
	}
	
	function onClear()
	{
		document.form.FROMTIME.selectedIndex=0;
		document.form.TOTIME.selectedIndex=0;
		document.form.MIN1.selectedIndex=0;
		document.form.MIN2.selectedIndex=0;
		document.form.MERIDIEN1.selectedIndex=0;
		document.form.MERIDIEN2.selectedIndex=0;
		document.form.QTY.value="";
		document.form.REMARKS.value="";
		
	}

</script>
<%
	session = request.getSession();
	String plant = (String) session.getAttribute("PLANT");
	String sUserId = (String) session.getAttribute("LOGIN_USER");
	String result = "";
	String sNewEnb = "enabled";
	String sDeleteEnb = "enabled";
	String sAddEnb = "enabled";
	String sUpdateEnb = "enabled";
	String sCustEnb = "enabled";
	String action = "";
	String TIMESLOTS = "",FROMTIME = "", TOTIME = "", MERIDIEN1 = "", MERIDIEN2 = "",MIN1="",MIN2="",QTY = "",REMARKS="" ;
	StrUtils strUtils = new StrUtils();
	CustUtil custUtil = new CustUtil();
	result =  strUtils.fString(request.getParameter("result"));
			
	String am="AM";
    String pm="PM";
    
	FROMTIME   = strUtils.fString(request.getParameter("FROMTIME"));
	MIN1   = strUtils.fString(request.getParameter("MIN1"));
	MERIDIEN1   = strUtils.fString(request.getParameter("MERIDIEN1"));
	TOTIME     = strUtils.fString(request.getParameter("TOTIME"));
	MIN2   = strUtils.fString(request.getParameter("MIN2"));
	MERIDIEN2   = strUtils.fString(request.getParameter("MERIDIEN2"));
	
	QTY        = strUtils.fString(request.getParameter("QTY"));
	REMARKS    = strUtils.fString(request.getParameter("REMARKS"));
	
	try {
		action = strUtils.fString(request.getParameter("action"));
		
		if (action.equalsIgnoreCase("SHOW_RESULT")) {
		
			Hashtable arrCust = (Hashtable) request.getSession().getAttribute("TimeSlotSession");
			FROMTIME 	= (String) arrCust.get("FROMTIME");
			MIN1 	= (String) arrCust.get("MIN1");
			MERIDIEN1 	= (String) arrCust.get("MERIDIEN1");
			TOTIME 		= (String) arrCust.get("TOTIME");
			MIN2 	= (String) arrCust.get("MIN2");
			MERIDIEN2 	= (String) arrCust.get("MERIDIEN2");
			QTY  		= (String) arrCust.get("QTY");
			REMARKS  	= (String) arrCust.get("REMARKS");
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
		<TH BGCOLOR="#000066" COLSPAN="11"><font color="white">Create
		TimeSlot</font></TH>
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
				<OPTION selected value="NOCLASSIFICATION">HR</OPTION>
				<% for (int i =0; i<13; i++){
				   String frmtime= Integer.toString(i);%>
					<OPTION value="<%=frmtime%>"
							<%if(FROMTIME.equalsIgnoreCase(frmtime)){%>selected  <%} %>><%=frmtime%>
							
					</OPTION>
			    <%}%>
			</SELECT>
			
			<SELECT NAME="MIN1" size="1">
				<OPTION selected value="NOCLASSIFICATION">MIN</OPTION>
				<OPTION   value='00' <%if(MIN1.equalsIgnoreCase("00")){ %>selected<%} %>>00  </OPTION>
     		<OPTION   value='30' <%if(MIN1.equalsIgnoreCase("30")){ %>selected<%} %>>30 </OPTION>
     		</SELECT>
			
			 <select name="MERIDIEN1">
		        <option value="<%=am%>"<%if(MERIDIEN1.equalsIgnoreCase(am)){%>selected  <%} %>><%=am%></option>
				<option value="<%=pm%>"<%if(MERIDIEN1.equalsIgnoreCase(pm)){%>selected  <%} %>><%=pm%></option>
			</select>
			
		   <b>(TO)</b>
			
			<SELECT NAME="TOTIME" size="1">
				<OPTION selected value="NOCLASSIFICATION">HR</OPTION>
				<% for (int i =0; i<13; i++){
					 String totime= Integer.toString(i);%>
					<OPTION value="<%=totime%>"
						<%if(TOTIME.equalsIgnoreCase(totime)){%>selected  <%} %>><%=totime%>
					</OPTION>
			    <%}%>
			</SELECT>
			<SELECT NAME="MIN2" size="1">
				<OPTION selected value="NOCLASSIFICATION">MIN</OPTION>
				<OPTION   value='00' <%if(MIN2.equalsIgnoreCase("00")){ %>selected<%} %>>00  </OPTION>
     			<OPTION   value='30' <%if(MIN2.equalsIgnoreCase("30")){ %>selected<%} %>>30 </OPTION>
     		</SELECT>
			
		 <select name="MERIDIEN2">
		        <option value="<%=am%>"<%if(MERIDIEN2.equalsIgnoreCase(am)){%>selected  <%} %>><%=am%></option>
				<option value="<%=pm%>"<%if(MERIDIEN2.equalsIgnoreCase(pm)){%>selected  <%} %>><%=pm%></option>
		</select>
			
					
		</TD>
	
 </TR>
	
	<TR>
		<TH WIDTH="35%" ALIGN="RIGHT">Maximum Quantity For TimeSlot :</TH>
		<TD><INPUT name="QTY" type="TEXT" value="<%=QTY%>"
			size="54" MAXLENGTH=80></TD>
	</TR>
	<TR>
		<TH WIDTH="35%" ALIGN="RIGHT">Remarks :</TH>
		<TD><INPUT name="REMARKS" type="TEXT" value="<%=REMARKS%>"
			size="54" MAXLENGTH=80></TD>
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
			value="Save" onClick="onAdd();">&nbsp;&nbsp;
		</TD>
	</TR>
	</TABLE>
<INPUT type="Hidden" name="TIMESLOTS" value="<%=TIMESLOTS%>">
</CENTER>

</FORM>
</BODY>
</HTML>
<%@ include file="footer.jsp"%>

