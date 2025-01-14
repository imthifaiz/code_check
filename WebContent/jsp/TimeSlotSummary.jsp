<%@ page import="com.track.gates.DbBean"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.util.*"%>
<%@ page import="com.track.gates.*"%>
<%@ page import="java.util.*"%>
<%@ include file="header.jsp"%>
<html>
<head>
<script language="JavaScript" type="text/javascript" src="js/calendar.js"></script>
<script language="javascript">
	var subWin = null;
  	function popUpWin(URL) {
    	subWin = window.open(URL, 'TimeSlotSummary', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=700,height=400,left = 200,top = 184');
  	}
 	function onGo()
	{ 
     document.form.submit();
	}
</script>
<title>TimeSlot Summary</title>
</head>
<link rel="stylesheet" href="css/style.css">
<%
	StrUtils strUtils     = new StrUtils();
	Generator generator   = new Generator();
	userBean _userBean      = new userBean();
	ArrayList timeslotQryList  = new ArrayList();
	
	_userBean.setmLogger(mLogger);
	String fieldDesc="";
	String PLANT="",TIMESLOTS ="",FROMTIME = "";
	String html = "";
	int Total=0;
	String SumColor="";
	boolean flag=false;
	session=request.getSession();
	String PGaction         = strUtils.fString(request.getParameter("PGaction")).trim();
	PLANT= session.getAttribute("PLANT").toString();
	TIMESLOTS     = strUtils.fString(request.getParameter("TIMESLOTS"));
 	TimeSlotUtil _TimeSlotUtil = new TimeSlotUtil();
 	_TimeSlotUtil.setmLogger(mLogger);
 	 List timeslotlist =_TimeSlotUtil.getTimeSlots(PLANT," ");
	if(PGaction.equalsIgnoreCase("View")){
 	try{
      Hashtable ht = new Hashtable();
      if(strUtils.fString(PLANT).length() > 0)       ht.put("PLANT",PLANT);
      
      if (TIMESLOTS.equals("NOCLASSIFICATION"))
  	  {		
  		TIMESLOTS="";
  				
      }
  	  else
  	  {
  		TIMESLOTS=TIMESLOTS;
  		
  	   }
      
      timeslotQryList=      _TimeSlotUtil.getTimeSlotDetails(PLANT,TIMESLOTS);
      if(timeslotQryList.size()== 0)
      {
        fieldDesc="Data Not Found";
      }
  
 	}catch(Exception e) {mLogger.exception(true,
			"ERROR IN JSP PAGE - TimeSlotSummary.jsp ", e); }
	}
%>
<%@ include file="body.jsp"%>
<FORM name="form" method="post" action="TimeSlotSummary.jsp">
<input type="hidden" name="xlAction" value="">
<input type="hidden" name="PGaction" value="View">
 
  <br>
  <TABLE border="0" width="100%" cellspacing="0" cellpadding="0" align="center" bgcolor="#dddddd">
    <TR>
      <TH BGCOLOR="#000066" COLSPAN="11"><font color="white">TimeSlot Summary</font></TH>
    </TR>
  </TABLE>
    <br>
  <center>
  <font face="Times New Roman" size="4">
     <table  border="0" cellspacing="1" cellpadding="2"  bgcolor="" >
      <font class="mainred"> <%=fieldDesc%></font>
    </table>
    </font>
  </center>
  <TABLE border="0" width="40%" cellspacing="0" cellpadding="0" align="center" bgcolor="#dddddd">
   <TR align="Center">
    
          <TH ALIGN="right" width="15%">TimeSlot </TH>
         <TD width="25%" align="center">
         <SELECT NAME="TIMESLOTS" size="1">
				<OPTION selected value="NOCLASSIFICATION">Choose</OPTION>
				<% for (int i =0; i<timeslotlist.size(); i++){
                    Map map = (Map) timeslotlist.get(i);
                    String frmtime     = (String) map.get("timeslots");
				  %>
					<OPTION value="<%=frmtime%>"
							<%if(TIMESLOTS.equalsIgnoreCase(frmtime)){%>selected  <%} %>><%=frmtime%>
							
					</OPTION>
			    <%}%>
			</SELECT>
			
          <a href="#" onClick="javascript:popUpWin('TimeSlotSummaryList.jsp?TIMESLOTS='+form.TIMESLOTS.value);"><img src="images/populate.gif" border="0"></a>
          </TD>
          <TD ALIGN="center" width="10%"> 
          <input type="button" value="View"   align="left" onClick="javascript:return onGo();"></TD>
        </TR>
  </TABLE>
  <br>
 <TABLE WIDTH="40%"  border="0" cellspacing="1" cellpadding = 2 align = "center">
    <TR BGCOLOR="#000066">
         <TH><font color="#ffffff" align="center">S/N</TH>
         <TH><font color="#ffffff" align="left"><b>TimeSlot</TH>
          <TH><font color="#ffffff" align="left"><b>Maximum Quantity For TimeSlot </TH>
            <TH><font color="#ffffff" align="left"><b>Remarks</TH>
      </TR>
   	  <%
         for (int iCnt =0; iCnt<timeslotQryList.size(); iCnt++){
			    int iIndex = iCnt + 1;
         		String bgcolor = ((iCnt == 0) || (iCnt % 2 == 0)) ? "#FFFFFF"  : "#dddddd";
           		Map lineArr = (Map) timeslotQryList.get(iCnt);
       %>

          <TR bgcolor = "<%=bgcolor%>">
	          <TD align="center"><%=iIndex%></TD>
	           <TD align="left" class="textbold"> &nbsp;
	              <a href ="TimeSlotDetail.jsp?action=View&TIMESLOTS=<%=(String)lineArr.get("TIMESLOTS")%>&REMARKS=<%=strUtils.replaceCharacters2Send((String)lineArr.get("REMARKS"))%>&QTY=<%=(String)lineArr.get("QTY")%>")%><%=(String)lineArr.get("TIMESLOTS")%></a></TD>
	    	   <TD align="left" class="textbold">&nbsp; <%=strUtils.fString((String)lineArr.get("QTY"))%></TD>
	           <TD align="left" class="textbold">&nbsp; <%=strUtils.fString((String)lineArr.get("REMARKS"))%></TD>
	       </TR>
       <%}%>
       </TABLE>
  </FORM>
<%@ include file="footer.jsp"%>