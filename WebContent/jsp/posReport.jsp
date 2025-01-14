<%@ page import="com.track.gates.DbBean"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.util.*"%>
<%@ page import="com.track.gates.*"%>
<%@ page import="java.util.*"%>
<%@ include file="header.jsp"%>

<%@page import="com.track.dao.SalesDetailDAO"%>
<html>
<head>
<script language="JavaScript" type="text/javascript" src="js/calendar.js"></script>
<script language="javascript">

var subWin = null;
  function popUpWin(URL) {
    subWin = window.open(URL, 'GroupSummary', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=700,height=400,left = 200,top = 184');
  }
 
function onGo(){

	document.form.action="posReport.jsp?action=View";
  document.form.submit();

}
</script>
<title>POS Report</title>
</head>
<link rel="stylesheet" href="css/style.css">
<%
	StrUtils strUtils     = new StrUtils();
	SalesDetailDAO salesdao = new SalesDetailDAO();
	List salesList  = new ArrayList();
	String fieldDesc="";
	String PLANT="",ITEM ="",ITEM_DESC="",STARTDATE="",TODATE="",action="",pGAction="";
	String html = "";
	PLANT = (String)session.getAttribute("PLANT");
	
	STARTDATE=strUtils.fString(request.getParameter("STARTDATE"));
	TODATE=strUtils.fString(request.getParameter("TODATE"));
	ITEM = strUtils.fString(request.getParameter("ITEM")).trim();
	action = request.getParameter("action");
	pGAction = request.getParameter("PGaction");
	System.out.print("Action PG"+pGAction);
	if (STARTDATE.length()>5)
		STARTDATE    = STARTDATE.substring(6)+"-"+ STARTDATE.substring(3,5)+"-"+STARTDATE.substring(0,2);
	
	if (TODATE.length()>5)
		TODATE    = TODATE.substring(6)+"-"+ TODATE.substring(3,5)+"-"+TODATE.substring(0,2);
	if(pGAction!=null){
	if(pGAction.equalsIgnoreCase("View")){
		salesList = salesdao.getSalesDetails(PLANT,STARTDATE,TODATE,ITEM);
	//else
		//salesList=null;
		 if(salesList.size()<=0)
	     {
	       //cntRec ="true";
	       fieldDesc="Data Not Found";
	     }
	}}
	
%>
<%@ include file="body.jsp"%>
<FORM name="form" method="post" action="posReport.jsp">
<input type="hidden" name="xlAction" value="">
<input type="hidden" name="PGaction" value="View">
  <br>
  <TABLE border="0" width="100%" cellspacing="0" cellpadding="0" align="center" bgcolor="#dddddd">
    <TR>
      <TH BGCOLOR="#000066" COLSPAN="11"><font color="white">POS Report</font></TH>
    </TR>
  </TABLE>
    <br>
  <center>
   <table  border="0" cellspacing="1" cellpadding="2"  bgcolor="" >
      <font class="mainred"> <%=fieldDesc%></font>
   </table>
   </center>
  <TABLE border="0" width="70%" cellspacing="0" cellpadding="0" align="center" bgcolor="#dddddd">
  
   <TR><TH ALIGN="right">Start Date :&nbsp;&nbsp; </TH>
                    <TD align="left"><INPUT name="STARTDATE"  type = "TEXT" value="<%=STARTDATE%>" size="20"  MAXLENGTH=20 >&nbsp;&nbsp;<a href="javascript:show_calendar('form.STARTDATE');" onmouseover="window.status='Date Picker';return true;" onmouseout="window.status='';return true;"><img src="images\show-calendar.gif" width=24 height=22 border=0></a></TD>
           <TH ALIGN="right" width="15%">To Date :&nbsp;&nbsp;</TH>
          <TD><INPUT name="TODATE"  type = "TEXT" value="<%=TODATE%>" size="20"  MAXLENGTH=20 >&nbsp;&nbsp;<a href="javascript:show_calendar('form.TODATE');" onmouseover="window.status='Date Picker';return true;" onmouseout="window.status='';return true;"><img src="images\show-calendar.gif" width=24 height=22 border=0></a></TD>
        
                  </TR>
                  <TR><TH ALIGN="right">Product ID :&nbsp;&nbsp; </TH>
                    <TD align="left"><INPUT name="ITEM"  type = "TEXT" value="<%=ITEM%>" size="20"  MAXLENGTH=50 >&nbsp;&nbsp;</TD>
          
        <TD ALIGN="left" width="5%"><input type="button" value="View"  onClick="javascript:return onGo();"></TD>
                  </TR>
   
  </TABLE>
  <br>
 <TABLE WIDTH="50%"  border="0" cellspacing="1" cellpadding ="2" align = "center">
     <TR BGCOLOR="#000066">
         <TH><font  align="center" color="#ffffff">Tran ID</TH>
         <TH><font color="#ffffff" align="left"><b>Product ID</TH>
          <TH><font color="#ffffff" align="left"><b>Product Description</TH>
          <TH><font color="#ffffff" align="left"><b>Qty</TH>
          <TH><font color="#ffffff" align="left"><b>Price</TH>
          
      </TR>
   	  <% int iIndex=0; String bgcolor="";Map salemap=null;
   	  if(salesList!=null&&salesList.size()>0)
       		for(int iCnt=0;iCnt<salesList.size();iCnt++){
       			 salemap=(Map)salesList.get(iCnt);
       			iIndex = iCnt+1;
       	 bgcolor = ((iCnt == 0) || (iCnt % 2 == 0)) ? "#FFFFFF"  : "#dddddd";
       		  	   
       %>
          <TR bgcolor = "<%=bgcolor%>">
	            <TD align="center"><%=salemap.get("tranid")%></TD>
	            <TD align="left" > &nbsp;<%=salemap.get("item")%></TD>
	    	    <TD align="left" class="textbold">&nbsp; <%=salemap.get("itemdesc")%></TD>
	    	    <TD align="center" class="textbold">&nbsp; <%=salemap.get("qty")%></TD> 
	            <TD align="center" class="textbold">&nbsp; <%=Float.parseFloat((String)salemap.get("unitprice"))%></TD>
	                     </TR>
       <%}%>
       </TABLE>
  </FORM>
<%@ include file="footer.jsp"%>