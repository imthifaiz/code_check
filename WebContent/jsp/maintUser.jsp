<%@ include file="header.jsp" %>

<script language="JavaScript" type="text/javascript" src="js/maintUser.js"></script>
<title>Maintain User*</title>
<link rel="stylesheet" href="css/style.css">
<%@ include file="body.jsp" %>
<jsp:useBean id="gn"  class="com.track.gates.Generator" />
<jsp:useBean id="sl"  class="com.track.gates.selectBean" />
<jsp:useBean id="ub"  class="com.track.gates.userBean" />
<jsp:useBean id="eb"  class="com.track.gates.encryptBean" />

<%!ArrayList al; %>
<%
	
    String caption  = "Maintain";
    String delParam = "";
    String disabled = "";
    String disabledInView="";
    String plant= ((String)session.getAttribute("PLANT")).trim();
    Enumeration e = request.getParameterNames();
        while(e.hasMoreElements())
        {
            String s = e.nextElement().toString();
            if(s.equalsIgnoreCase("view"))  // If the option is to view disable partly
                {
                     caption = "View";
                     disabledInView = "disabled";
                }
            else if(s.equalsIgnoreCase("del")) // If the option is to delete disable all
                {
                     caption = "Delete";
                     delParam = "<input type=\"Hidden\" name=\"del\" value=\"\">"; //  To Indicate the delete function
                     disabled = "disabled";
                }

        }
%>

<FORM name="chooseForm" METHOD="post" action="maintUser.jsp">
  <br>
  <table border="0" width="100%" cellspacing="0" cellpadding="0" align="center" bgcolor="#dddddd">
      <TR><TH BGCOLOR="#000066" COLSPAN="11"><font color="white">MAINTAIN USER</font>
  </table>
  <br>
 <%=delParam%>
  <table border="0" width="100%" cellspacing="0" cellpadding="0" align="center" bgcolor="#dddddd">
  <tr>
  <td width="100%">&nbsp;
<font face="Times New Roman" size="2">
  <center>
    <table border="0" width="90%">
    <tr>
    <td width="100%">
    <CENTER>

     <!-- <TABLE BORDER="0" CELLSPACING="0" WIDTH="100%">
        <TR>
        <TH WIDTH="18%" >User ID
        <TD>
            <SELECT NAME ="USER_ID" size="1" <%=disabledInView%>>
            <OPTION selected value=''>< -- Choose -- > </OPTION>
          		<%if(plant.equalsIgnoreCase("track")){%>  <%=sl.getUserIDs()%><%}else{%>
         		<%=sl.getUserIDs(plant)%> <%}%>
            </SELECT>&nbsp;
        </TD>
        <TH WIDTH="18%" >User Name&nbsp;&nbsp;&nbsp;&nbsp;
        <TD><INPUT type="text" name="USER_NAME" <%=disabledInView%>>&nbsp;
            <INPUT type="submit" value="Go" <%=disabledInView%>>&nbsp;
            <input type="button" value="Cancel" <%=disabledInView%> onClick ="window.location.href='../home'">
        </TD>
        </TR>
        </TABLE> -->
<%
    session= request.getSession();
    String user_id   =(String) session.getAttribute("LOGIN_USER");
    String user_name = request.getParameter("USER_NAME").toUpperCase();
    if (user_id.length()<1 && user_name.length()<1)
    {
        out.write("<br><table width=\"100%\"><tr><td align=\"center\"><b>Please Select the User ID ( or ) Key in the User Name</b></td></tr></table></FORM>");
    }
    else
    {
        if(plant.equalsIgnoreCase("track"))
        {
        if(user_id.length()>0)
        al  = ub.getUserDetails(user_id,0);
     
      }else
      {
         if(user_id.length()>0)
              al  = ub.getUserDetails(user_id,0,plant);
             
      
      }
        if(al.isEmpty())
        out.write("<br><table width=\"100%\"><tr><td align=\"center\"><font color=\"red\"<b>Invalid USER ID or USER NAME Specified</b></font><br>"+
                    "Please ensure the User ID or USER NAME is available in the database</td></tr></table>");
        else
        {
            user_id      = al.get(0).toString();
            String authorise_by = al.get(7).toString();
            if((authorise_by==null) || (authorise_by.length()<=1)) authorise_by="Not Authorised";

%>
</FORM>
<FORM name="userForm" method="post" action="newUserSubmit.jsp" onSubmit="return validateUser(document.userForm)">
    <CENTER>
      <TABLE border="0" CELLSPACING=0 WIDTH="100%">
        <TR>
        <TH WIDTH="35%" ALIGN="RIGHT" >User ID :&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
        <TD><INPUT size="50"  MAXLENGTH=10 name="USER_ID" <%=disabled%> value="<%=al.get(0).toString()%>" >
       <TR>
        <!-- <TH WIDTH="35%" ALIGN="RIGHT" >Password :&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; -->
        <TD><INPUT type="hidden" size="50" MAXLENGTH=10 name="PASSWORD" <%=disabled%> value="<%=al.get(1).toString()%>" onFocus="clearText(document.userForm);">
         <TR>
     <!--   <TH WIDTH="35%" ALIGN="RIGHT" >Confirm Password :&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
        <TD><INPUT type="password" size="50" MAXLENGTH=10 name="CPASSWORD" <%=disabled%> value="<%=al.get(1).toString()%>">
        <TR> -->
        <TH WIDTH="35%" ALIGN="RIGHT" >User Name :&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
        <TD><INPUT size="50" MAXLENGTH=25 name="USER_NAME" <%=disabled%>  value="<%=al.get(2).toString()%>">
        <TR>
        <TH WIDTH="35%" ALIGN="RIGHT" >Job Title :&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
        <TD><INPUT size="50" MAXLENGTH=40 name="RANK" <%=disabled%> value="<%=al.get(3).toString()%>">
        <TR>
        <TH WIDTH="35%" ALIGN="RIGHT" >Remarks :&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
        <TD><INPUT size="50" MAXLENGTH=60 name="REMARKS" <%=disabled%> value="<%=al.get(4).toString()%>">
        <TR>
        <TH WIDTH="35%" ALIGN="RIGHT" >Effective Date :&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
        <TD><INPUT size="50" MAXLENGTH=10 name="EFFECTIVE_DATE" <%=disabled%>  value="<%=gn.getDB2UserDate(al.get(5).toString())%>">
         <TR>
            <TH WIDTH="35%" ALIGN="RIGHT" >Company :&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
            <TD>
             <SELECT NAME ="DEPT" size="1">
                  <OPTION selected value=''>< -- Choose  Company-- > </OPTION>
                      <%=sl.getPlantNames("0")%>  </SELECT>
             </TD>
          <TR>
        <TR>
        <TH WIDTH="35%" ALIGN="RIGHT" >User Level :&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
        <TD>
            <SELECT NAME ="USER_LEVEL" size="1" <%=disabled%>>
            <OPTION selected value="<%=al.get(6).toString()%>"><%=al.get(6).toString()%></OPTION>
         	<%if(plant.equalsIgnoreCase("track")){%>    <%=sl.getUserLevels("0")%> <%}else {%>
                      <%=sl.getUserLevels("1",plant)%>
              <%}%>
            </SELECT>
        </TD>
        <TR>
        <TD><INPUT type="hidden" size="50" disabled name="AUTHORISE_BY" value="<%=authorise_by%>">
        <TR>
          <TD><INPUT type="hidden" size="50" disabled name="AUTHORISE_ON" value="<%=gn.getDB2UserDateTime(al.get(8).toString())%>">
      </TABLE>
    </CENTER>
    </td>
    </tr>
    </table>
    <br>
    </center>
    <INPUT type="Hidden" name="USER_ID" value="<%=user_id%>">
    <INPUT type="Hidden" name="ENCRYPT_FLAG" value="0">
    <div align="center"><center>
      <TABLE BORDER="0" CELLSPACING="0" WIDTH="100%">
        <TR> <TD  ALIGN = "CENTER">
<%
        if(caption.equalsIgnoreCase("delete"))
        out.write("<INPUT type=\"Submit\" name=\"Submit\" Value=\"Delete\" onClick=\"return confirm('Are you sure to delete "+user_id+" user ID permanently ?');\">&nbsp;");
        else if(caption.equalsIgnoreCase("view"))
        out.write("<INPUT type=\"Button\" Value=\"Back to List\" onClick=\"window.location.href='javascript:history.back()'\"> ");
        else
        out.write("<INPUT type=\"Submit\" name=\"Submit\" Value=\"Update\">&nbsp;");
%>
        <INPUT Type=Button name="C" Value="Cancel" Size = 10 onClick="window.location.href='../home'"></TD>
      </TABLE>
 </center></div>
 <div align="center">
 <center><p>&nbsp;</p>
 </center>
 </div></td></tr>
  </table>
<%
        }   //  Closing inner else
    }       //  Closing else
 %>
</FORM>

<%@ include file="footer.jsp" %>
