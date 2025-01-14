<%@ include file="header.jsp" %>
<jsp:useBean id="eb"  class="com.track.gates.encryptBean" />
<%@ page import="com.track.gates.*"%>
 <SCRIPT LANGUAGE="JavaScript">
function onView(){
    document.form.action = "addNewUser.jsp?action=View";
    document.form.submit();
}
function onClear(){
 
  // document.form.action  = "MiscOrderReceiving.jsp?action=CLEAR";
  // document.form.submit();
  document.form.USER_ID.value="";
  document.form.PASSWORD.value="";
  document.form.CPASSWORD.value="";
  document.form.USER_NAME.value="";
  document.form.DESGINATION.value="";
  document.form.TELNO.value="";
  document.form.HPNO.value="";
  document.form.FAX.value="";
  document.form.EMAIL.value="";
   
  document.form.RANK.value="";
  document.form.REMARKS.value="";
  document.form.EFFECTIVE_DATE.value="";
  document.form.DEPT.selectedIndex=0
  document.form.USER_LEVEL.selectedIndex=0
 
  
  return true;
}

</SCRIPT>

<%


String plant = (String) session.getAttribute("PLANT");
String userName = StrUtils.fString(
		(String) session.getAttribute("LOGIN_USER")).trim();

StrUtils strUtils = new StrUtils();
ArrayList invQryList  = new ArrayList();
userBean _userBean      = new userBean();
_userBean.setmLogger(mLogger);

String action="",USER_ID="",PASSWORD="",CPASSWORD="",USER_NAME="",DESGINATION="",TELNO="",HPNO="",FAX="",EMAIL="",
                RANK="",REMARKS="",EFFECTIVE_DATE="",DEPT="",USER_LEVEL="",COMPANY="";


String selplant = plant;
action = strUtils.fString(request.getParameter("action"));

if(plant.equalsIgnoreCase("track"))
{
plant="";
}
else
{
plant=plant+"_";
}

 if(action.equalsIgnoreCase("View")){

    Hashtable ht = new Hashtable();
  
    
    ht.put("DEPT",request.getParameter("COMPANY"));
    ht.put("USER_ID",request.getParameter("USER_ID1"));
    COMPANY=request.getParameter("COMPANY");
    USER_ID=request.getParameter("USER_ID1");
    session.setAttribute("SELECTEDUSRID",request.getParameter("USER_ID1"));
    invQryList =_userBean.getUserListSummary(ht,selplant,COMPANY,USER_ID);
     for (int iCnt =0; iCnt<invQryList.size(); iCnt++){
			 
       Map lineArr = (Map) invQryList.get(iCnt);
       
       USER_ID    = (String)lineArr.get("USER_ID");
       COMPANY    = request.getParameter("COMPANY");
       PASSWORD = eb.decrypt((String)lineArr.get("PASSWORD"));
//       PASSWORD   = (String)lineArr.get("PASSWORD");
       CPASSWORD  = eb.decrypt((String)lineArr.get("PASSWORD"));
       USER_NAME  = (String)lineArr.get("USER_NAME");
       DESGINATION=(String)lineArr.get("DESGINATION");
       TELNO=(String)lineArr.get("TELNO");
       HPNO    = (String)lineArr.get("HPNO");
       FAX     = (String)lineArr.get("FAX");
       EMAIL    = (String)lineArr.get("EMAIL");
       RANK    = (String)lineArr.get("RANK");
       REMARKS   = (String)lineArr.get("REMARKS");
       EFFECTIVE_DATE   = (String)lineArr.get("EFFECTIV_DATE");
       DEPT   = (String)lineArr.get("DEPT");
       USER_LEVEL   = (String)lineArr.get("USER_LEVEL");
           
      } 
       
     // sAddEnb    = "enabled"; 
     
}
%>
<script language="JavaScript" type="text/javascript" src="js/newUser.js"></script>

<title>Add New User</title>
<link rel="stylesheet" href="css/style.css">

<%@ include file="body.jsp" %>
<jsp:useBean id="gn"  class="com.track.gates.Generator" />
<jsp:useBean id="sl"  class="com.track.gates.selectBean" />
<FORM name="form" method="post" action="newUserSubmit.jsp" onSubmit="return validateUser(document.form)">
<!--<FORM name="form" method="post" onSubmit="return validateUser(document.form);">-->
  <br>

  <table border="1" width="100%" cellspacing="0" cellpadding="0" align="center" bgcolor="#dddddd">
      <TR>
        <TH BGCOLOR="#000066" COLSPAN="11"><font color="white"> MAINTAIN USER</font>

  </table>
  <br>
  <table border="0" width="100%" cellspacing="0" cellpadding="0" align="center" bgcolor="#dddddd">

    <tr>
      <td width="100%">&nbsp;
        <font face="Times New Roman" size="2">
        <center>
          <table border="0" width="90%">
            <tr>
              <td width="100%">
                <CENTER>
                  <TABLE BORDER="0" CELLSPACING=0 WIDTH="100%">
                    <TR>
                      <TH WIDTH="35%" ALIGN="RIGHT" >User ID :&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                      <TD>
                        <INPUT size="50"    MAXLENGTH=10 name="USER_ID" value="<%=USER_ID%>" onFocus="nextfield ='PASSWORD';">
                    <TR>
                      <TH WIDTH="35%"    ALIGN="RIGHT" >Password :&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                      <TD>
                        <INPUT type="password" size="50"   MAXLENGTH=10 value="<%=PASSWORD%>" name="PASSWORD" onFocus="nextfield ='CPASSWORD';">
                       
                    <TR>
                      <TH WIDTH="35%" ALIGN="RIGHT" >Confirm Password :&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                      <TD>
                        <INPUT type="password" size="50"    MAXLENGTH=10 value="<%=CPASSWORD%>"  name="CPASSWORD" onFocus="nextfield ='USER_NAME';">
                    <TR>
                      <TH WIDTH="35%" ALIGN="RIGHT" >User Name :&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                      <TD>
                        <INPUT size="50" MAXLENGTH=25 name="USER_NAME" value="<%=USER_NAME%>"  onFocus="nextfield ='RANK';">
                        
                      <TR>
                      <TH WIDTH="35%" ALIGN="RIGHT" >Desgination :&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                      <TD>
                        <INPUT size="50" MAXLENGTH=40 name="DESGINATION" value="<%=DESGINATION%>"  onFocus="nextfield ='DESGINATION';">
                     <TR>
                      <TH WIDTH="35%" ALIGN="RIGHT" >Telephone :&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                      <TD>
                        <INPUT size="50" MAXLENGTH=40 name="TELNO" value="<%=TELNO%>"  onFocus="nextfield ='TELNO';">
                        <TR>
                      <TH WIDTH="35%" ALIGN="RIGHT" >HandPhone :&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                      <TD>
                        <INPUT size="50" MAXLENGTH=40 name="HPNO"  value="<%=HPNO%>"  onFocus="nextfield ='HPNO';">
                          <TR>
                      <TH WIDTH="35%" ALIGN="RIGHT" >Fax :&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                      <TD>
                        <INPUT size="50" MAXLENGTH=40 name="FAX" value="<%=FAX%>"  onFocus="nextfield ='FAX';">
                       <TR>
                      <TH WIDTH="35%" ALIGN="RIGHT" >Email :&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                      <TD>
                        <INPUT size="50" MAXLENGTH=40 name="EMAIL" value="<%=EMAIL%>"  onFocus="nextfield ='Email';">
                        
                    <TR>
                      <TH WIDTH="35%" ALIGN="RIGHT" >Job Title :&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                      <TD>
                        <INPUT size="50" MAXLENGTH=40 name="RANK" value="<%=RANK%>"  onFocus="nextfield ='REMARKS';">
                    <TR>
                      <TH WIDTH="35%" ALIGN="RIGHT" >Remarks :&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                      <TD>
                        <INPUT size="50" MAXLENGTH=60 name="REMARKS" value="<%=REMARKS%>"  onFocus="nextfield ='EFFECTIVE_DATE';">
                    <TR>
                      <TH WIDTH="35%" ALIGN="RIGHT" >Effective Date :&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                      <TD>
                        <INPUT size="50" MAXLENGTH=10 name="EFFECTIVE_DATE" value="<%=gn.getDate()%>" onFocus="nextfield ='COMPANY';">
                    <TR>
                      <TH WIDTH="35%" ALIGN="RIGHT" >Company :&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                      <TD>
                      <%if(selplant.equalsIgnoreCase("track")){%>
                        <SELECT NAME ="DEPT" size="1">
                          <OPTION selected value="<%=DEPT%>">< -- Choose  Company-- > </OPTION>
                         <%=sl.getPlantNames("0")%>  
                         <%if(!COMPANY.equals("")) { %>
                     <OPTION selected value="<%=COMPANY%>"><%=COMPANY%></OPTION>
                     <%}%>
                     </SELECT><%}else{%>
                      <SELECT NAME ="DEPT" size="1">
                           <OPTION selected value="<%=selplant%>"><%=selplant%> </OPTION> </SELECT>
                     <%}%>
                      </TD>
      
                    <TR>
                      <TH WIDTH="35%" ALIGN="RIGHT" >User Level :&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                      <TD>
                        <SELECT NAME ="USER_LEVEL" size="1">
                          <OPTION selected value=''>< -- Choose -- > </OPTION>
                      <%if(plant.equalsIgnoreCase("track")){%>    <%=sl.getUserLevels("0")%> <%}else {%>
                      <%=sl.getUserLevels("0",plant)%>
                      <%if(!USER_LEVEL.equals("")) { %>
                     <OPTION selected value="<%=USER_LEVEL%>"><%=USER_LEVEL%></OPTION>
                         
                      
                      
                      <%}}%>
                        </SELECT>
                      </TD>
                  </TABLE>
                  <INPUT     name="COMPANY"  type ="hidden" value="<%=COMPANY%>" size="1"   MAXLENGTH=80 >
                </CENTER>
              </td>
            </tr>
          </table>
          <br>
        </center>
        <INPUT type="Hidden" name="ENCRYPT_FLAG" value="1">
        <div align="center"><center>
            <table border="0" width="100%" cellspacing="0" cellpadding="0">
              <tr>
                <td align="center">
                 <!-- <input type="Submit" value=" Save " name="Submit"> -->
                    <input type="Submit" value=" Update " name="Submit" onclick="return confirm('Do you want to update user');">
                    <input type="Submit" value=" Delete " name="Submit" onclick="return confirm('Do you want to delete user');">
                  &nbsp;
                   <INPUT class="Submit" type="BUTTON" value="Clear" onClick="return onClear();">&nbsp;&nbsp;   
                  <input type="Button" value="Cancel" onClick="window.location.href='../home';">
                </td>
              </tr>
            </table>
          </center>
        </div>
        <div align="center">
          <center>
            <p>&nbsp;</p>
          </center>
        </div>
        </font></td>
    </tr>
  </table>
</FORM>
<%@ include file="footer.jsp" %>
