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
    subWin = window.open(URL, 'GroupSummary', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=700,height=400,left = 200,top = 184');
  }
 function onGo(){
 
     document.form.submit();
}
 function onView(){
	   document.form.action  = "PaymentIDSummary.jsp?action=VIEW";
	   document.form.submit();
	}
</script>
<title>Payment ID Summary</title>
</head>
<link rel="stylesheet" href="css/style.css">
<%
StrUtils strUtils     = new StrUtils();
Generator generator   = new Generator();
userBean _userBean      = new userBean();
ArrayList locQryList  = new ArrayList();


String fieldDesc="";
String PLANT="",spaymentId ="";
String html = "";
int Total=0;
String SumColor="";
boolean flag=false;
session=request.getSession();
String PGaction         = strUtils.fString(request.getParameter("PGaction")).trim();
PLANT= session.getAttribute("PLANT").toString();
spaymentId     = strUtils.fString(request.getParameter("PAYMENT_ID"));

OrderPaymentUtil paymentutil = new OrderPaymentUtil();
paymentutil.setmLogger(mLogger);

if(PGaction.equalsIgnoreCase("View")){
 try{
        locQryList= paymentutil.getPaymentIdList(spaymentId,PLANT,"");
      
      if(locQryList.size()== 0)
      {
        fieldDesc="Data Not Found";
      }
                 
     
 }catch(Exception e) { }
}
%>
<%@ include file="body.jsp"%>
<FORM name="form" method="post" action="PaymentIDSummary.jsp">
<input type="hidden" name="xlAction" value="">
<input type="hidden" name="PGaction" value="View">
<INPUT name="ACTIVE" type = "hidden" >
<INPUT name="PAYMENT_DESC" type = "hidden" >
  <br>
  <TABLE border="0" width="100%" cellspacing="0" cellpadding="0" align="center" bgcolor="#dddddd">
    <TR>
      <TH BGCOLOR="#000066" COLSPAN="11"><font color="white">Payment ID Summary</font></TH>
    </TR>
  </TABLE>
    <center>
  <font face="Times New Roman" size="4">
     <table  border="0" cellspacing="1" cellpadding="2"  bgcolor="" >
      <font class="mainred"> <%=fieldDesc%></font>
    </table>
    </font>
  </center><br>
  <TABLE border="0" width="40%" cellspacing="0" cellpadding="0" align="center" bgcolor="#dddddd">
  
   <TR align="Center">
    
          <TH WIDTH="35%" ALIGN="RIGHT"> Payment ID:</TH>
		<TD><INPUT name="PAYMENT_ID" type="TEXT" value="<%=spaymentId%>" size="25" MAXLENGTH=50> 
			 <a href="#" onClick="javascript:popUpWin('PaymentIdList.jsp?PAYMENT_ID='+form.PAYMENT_ID.value);">
			 <img src="images/populate.gif" border="0"></a>
			 </TD>
                    
          <TD ALIGN="center" width="10%"> 
          <input type="button" value="View"   align="left" onClick="javascript:return onGo();"></TD>
        </TR>
  </TABLE>
  <INPUT name="ACTIVE" type = "hidden" value="Y"   >
            <INPUT name="ACTIVE" type = "hidden" value="N"   >
  <br>
 <TABLE WIDTH="40%"  border="0" cellspacing="1" cellpadding = 2 align = "center">
    <TR BGCOLOR="#000066">
         <TH><font color="#ffffff" align="center">S/N</TH>
         <TH><font color="#ffffff" align="left"><b>Payment ID</TH>
          <TH><font color="#ffffff" align="left"><b>Payment ID Description</TH>
           <TH><font color="#ffffff" align="left"><b>IsActive</TH>
      </TR>
   	  <%        
         
          for (int iCnt =0; iCnt<locQryList.size(); iCnt++){
			     int iIndex = iCnt + 1;
          String bgcolor = ((iCnt == 0) || (iCnt % 2 == 0)) ? "#FFFFFF"  : "#dddddd";
          
            Map lineArr = (Map) locQryList.get(iCnt);
       %>

          <TR bgcolor = "<%=bgcolor%>">
            <TD align="center"><%=iIndex%></TD>
            
           <TD align="left"> <a href ="paymentIDDetail.jsp?PAYMENT_ID=<%=(String)lineArr.get("payment_id")%>&PAYMENT_DESC=<%=strUtils.replaceCharacters2Send((String)lineArr.get("payment_desc"))%>&ISACTIVE=<%=(String)lineArr.get("ISACTIVE")%>"%><%=(String)lineArr.get("payment_id")%></a></TD>
    	     <TD align="left">&nbsp;&nbsp; &nbsp;&nbsp;&nbsp; <%=strUtils.fString((String)lineArr.get("payment_desc"))%></TD>
              <TD align="left">&nbsp;&nbsp; &nbsp;&nbsp;&nbsp; <%=strUtils.fString((String)lineArr.get("ISACTIVE"))%></TD>
          </TR>
       <%}%>
       </TABLE>
  </FORM>
<%@ include file="footer.jsp"%>