
<%@ page import="com.track.gates.DbBean"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.util.*"%>
<%@ page import="com.track.gates.*"%>
<%@ page import="java.util.*"%>
<%@ include file="header.jsp"%>
<!-- Not in Use - Menus status 0 -->
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
</script>
<title>Mobile Register Summary</title>
</head>
<link rel="stylesheet" href="css/style.css">
<%


StrUtils strUtils     = new StrUtils();
Generator generator   = new Generator();
userBean _userBean      = new userBean();

List locQryList  = new ArrayList();
_userBean.setmLogger(mLogger);
String fieldDesc="";
String PLANT="",CUST_CODE ="";
String html = "";
int Total=0;
String SumColor="";
boolean flag=false;
session=request.getSession();
String PGaction         = strUtils.fString(request.getParameter("PGaction")).trim();
PLANT= session.getAttribute("PLANT").toString();
CUST_CODE     = strUtils.fString(request.getParameter("CUST_CODE"));
CustUtil custUtils = new CustUtil(); 
custUtils.setmLogger(mLogger);
if(PGaction.equalsIgnoreCase("View")){
 try{
     
      Hashtable ht = new Hashtable();
      if(strUtils.fString(PLANT).length() > 0)       ht.put("PLANT",PLANT);
      locQryList= custUtils.getCustomerListStartsWithName(CUST_CODE,PLANT);
      if(locQryList.size()== 0)
      {
        fieldDesc="Data Not Found";
      }
                 
     
 }catch(Exception e) { }
}
%>
<%@ include file="body.jsp"%>
<FORM name="form" method="post" action="mobileregistercustmerSummary.jsp">
<input type="hidden" name="xlAction" value="">
<input type="hidden" name="PGaction" value="View">
<input type="hidden" name="CUST_CODE1" value="">
<input type="hidden" name="CUST_NAME" value="">
<input type="hidden" name="L_CUST_NAME" value="">
  <br>
  <TABLE border="0" width="100%" cellspacing="0" cellpadding="0" align="center" bgcolor="#dddddd">
    <TR>
      <TH BGCOLOR="#000066" COLSPAN="11"><font color="white">Mobile Register Summary</font></TH>
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
  <TABLE border="0" width="60%" cellspacing="0" cellpadding="0" align="center" bgcolor="#dddddd">
  
   <TR align="Center">
    
          <TH ALIGN="right" width="15%">Customer Name </TH>
         <TD width="25%" align="center"><INPUT name="CUST_CODE" type = "TEXT" value="<%=CUST_CODE%>" size="20"  MAXLENGTH=20>
          <a href="#" onClick="javascript:popUpWin('customer_list.jsp?CUST_NAME='+form.CUST_CODE.value);"><img src="images/populate.gif" border="0"></a>
          </TD>
                    
          <TD ALIGN="center" width="20%"> 
          <input type="button" value="View"   align="left" onClick="javascript:return onGo();"></TD>
        </TR>
  </TABLE>
  <br>
 <TABLE WIDTH="60%"  border="0" cellspacing="1" cellpadding = 2 align = "center">
    <TR BGCOLOR="#000066">
         <TH><font color="#ffffff" align="center">S/N</TH>
         <TH><font color="#ffffff" align="left"><b>Customer Code</TH>
          <TH><font color="#ffffff" align="left"><b>Customer Name</TH>
          <TH><font color="#ffffff" align="left"><b>Company Name</TH>
            <TH><font color="#ffffff" align="left"><b>Telephone</TH>
            <TH><font color="#ffffff" align="left"><b>IsActive</TH>
      </TR>
   	  <%
          
          for (int iCnt =0; iCnt<locQryList.size(); iCnt++)
          {
			int iIndex = iCnt + 1;
          	String bgcolor = ((iCnt == 0) || (iCnt % 2 == 0)) ? "#FFFFFF"  : "#dddddd";
           	Map lineArr = (Map) locQryList.get(iCnt);

       %>
  
          <TR bgcolor = "<%=bgcolor%>">
            <TD align="left"  class="textbold">&nbsp;<%=iIndex%></TD>
            <TD align="left" class="textbold">&nbsp; <a href ="mobileregistercustomerDetail.jsp?action=View&CUST_CODE=<%=(String)lineArr.get("CUSTNO")%>
           &CUST_NAME=<%=(String)lineArr.get("CNAME")%>&L_CUST_NAME=<%=strUtils.replaceCharacters2Send((String)lineArr.get("LNAME"))%>&CONTACTNAME=<%=strUtils.replaceCharacters2Send((String)lineArr.get("NAME"))%>
           &DESGINATION=<%=strUtils.replaceCharacters2Send((String)lineArr.get("DESGINATION"))%>&TELNO=<%=(String)lineArr.get("TELNO")%>
           &HPNO=<%=(String)lineArr.get("HPNO")%>&FAX=<%=(String)lineArr.get("FAX")%>
           &EMAIL=<%=strUtils.replaceCharacters2Send((String)lineArr.get("EMAIL"))%>&ADDR1=<%=strUtils.replaceCharacters2Send((String)lineArr.get("ADDR1"))%>
           &ADDR2=<%=strUtils.replaceCharacters2Send((String)lineArr.get("ADDR2"))%>&ADDR3=<%=strUtils.replaceCharacters2Send((String)lineArr.get("ADDR3"))%>&ADDR4=<%=strUtils.replaceCharacters2Send((String)lineArr.get("ADDR4"))%>
           &COUNTRY=<%=strUtils.replaceCharacters2Send((String)lineArr.get("COUNTRY"))%>&ZIP=<%=(String)lineArr.get("ZIP")%>&REMARKS=<%=strUtils.replaceCharacters2Send((String)lineArr.get("REMARKS"))%>&ISACTIVE=<%=(String)lineArr.get("ISACTIVE")%>"%>
           <%=(String)lineArr.get("CUSTNO")%></a></TD>
    	     <TD align="left" class="textbold">&nbsp; <%=strUtils.removeQuotes((String)lineArr.get("CNAME"))%></TD>
           <TD align="left" class="textbold">&nbsp; <%=strUtils.fString((String)lineArr.get("NAME"))%></TD>
            <TD align="left" class="textbold">&nbsp; <%=strUtils.fString((String)lineArr.get("TELNO"))%></TD>
             <TD align="left" class="textbold">&nbsp; <%=strUtils.fString((String)lineArr.get("ISACTIVE"))%></TD>
          </TR>
       <%}%>
       </TABLE>
  </FORM>
<%@ include file="footer.jsp"%>