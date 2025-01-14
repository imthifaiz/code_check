<%@ page import="java.util.*"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.util.*"%>
<html>
<head>
<title>TimeSlot List</title>

<link rel="stylesheet" href="css/style.css">
</head>
<body bgcolor="#ffffff">
<form method="post" name="form">
<table border="0" width="100%" cellspacing="1" cellpadding="0"
	align="center" bgcolor="#dddddd">
	<TR BGCOLOR="#000066">
		<TH align="left"><font color="white">Time Slots</font></TH>
		<TH align="left"><font color="white">Maximum Quantity For TimeSlot</font></TH>
		<TH align="left"><font color="white">Remarks</font></TH>
		
	</TR>
	<%
	String PLANT = "", TIMESLOTS="",FROMTIME = "", TOTIME = "", MERIDIEN = "", QTY = "", REMARKS="";
	HashMap<String, String> loggerDetailsHasMap = new HashMap<String, String>();
	
    loggerDetailsHasMap.put(MLogger.COMPANY_CODE, (String) session
				.getAttribute("PLANT"));
	loggerDetailsHasMap.put(MLogger.USER_CODE, StrUtils.fString(
		(String) session.getAttribute("LOGIN_USER")).trim());
	MLogger mLogger = new MLogger();
	mLogger.setLoggerConstans(loggerDetailsHasMap);
    String plant= (String)session.getAttribute("PLANT");
    StrUtils strUtils = new StrUtils();
    TIMESLOTS = StrUtils.fString((String)request.getParameter("TIMESLOTS"));
   
	if (TIMESLOTS.equals("NOCLASSIFICATION"))
	{		
		TIMESLOTS="";
				
	}
	else
	{
		TIMESLOTS=TIMESLOTS;
		
	}
    String sBGColor = "";
    
    
   
    try
    {
    	 TimeSlotUtil _TimeSlotUtil = new TimeSlotUtil();
    	_TimeSlotUtil.setmLogger(mLogger);
    	
    	List listQry= _TimeSlotUtil.qryEditTimeSlot(TIMESLOTS ,plant," ");
    	for(int i =0; i<listQry.size(); i++) {
        	sBGColor = ((i == 0) || (i % 2 == 0)) ? "#FFFFFF" : "#dddddd";
         	Vector vecItem   = (Vector)listQry.get(i);
           	String sTimeslots   = (String)vecItem .get(0);
        	String sQty   = (String)vecItem .get(1);
        	String sRemarks      =(String)vecItem .get(2);
   
%>
	<TR bgcolor="<%=sBGColor%>">
		<td class="main2"><a href="#"
			onClick="
		     window.opener.form.TIMESLOTS.value='<%=sTimeslots %>';
		   
        		window.close();"><%=sTimeslots%>
		</a></td>
		<td class="main2"><%=sQty%></td>
		<td class="main2"><%=sRemarks%></td>
		
	</TR>
	<%
		}
	}catch(Exception he){he.printStackTrace(); 
		System.out.println("Error in reterieving data");
	}
%>
	<TR>
		<TH COLSPAN="8">&nbsp;</TH>
	</TR>
	<TR>
		<TH COLSPAN="8" align="center"><a href="#"
			onclick="window.close();"
><input type="submit" value="Close"></a></TH>
	</TR>
</table>
</body>
</html>
