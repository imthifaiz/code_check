<%@ include file="header.jsp" %>
<script language="JavaScript" type="text/javascript" src="js/receiving.js"></script>
<script language="JavaScript" type="text/javascript" src="js/calendar.js"></script>
<script language="javascript">

function validateForm(){
 if(document.form.ITEM.value == ""){
    alert("Please select an item");
    document.form.ITEM.focus();
    return false;
 }
 if(document.form.QTYOR.value == ""){
   alert("Please enter quantity");
   document.form.QTYOR.focus();
   return false;
 }
}
</script>
<title>Order Picking Reverse</title>
<link rel="stylesheet" href="css/style.css">

<%@ include file="body.jsp" %>
<jsp:useBean id="gn"  class="com.track.gates.Generator" />
<jsp:useBean id="sl"  class="com.track.gates.selectBean" />
<jsp:useBean id="db"  class="com.track.gates.defaultsBean" />
<jsp:useBean id="su"  class="com.track.util.StrUtils" />
<jsp:useBean id="ddb"  class="com.track.tables.DODET" />
<jsp:useBean id="dhb"  class="com.track.tables.DOHDR" />
<jsp:useBean id="imb"  class="com.track.tables.ITEMMST" />
<jsp:useBean id="cm"  class="com.track.tables.CLASSMST" />
<jsp:useBean id="am"  class="com.track.tables.ATTMST" />
<%

db.setmLogger(mLogger);
ddb.setmLogger(mLogger);
dhb.setmLogger(mLogger);
cm.setmLogger(mLogger);
am.setmLogger(mLogger);

    String action   = su.fString(request.getParameter("action")).trim();
    String  fieldDesc="";
     
    String dono    = su.fString(request.getParameter("DONO"));
    String dolnno  = su.fString(request.getParameter("DOLNNO"));
    String custname = su.fString(request.getParameter("CUSTNAME"));
    String item  = su.fString(request.getParameter("ITEM"));
    String itemdesc  = su.fString(request.getParameter("ITEMDESC"));
    String orderqty  = su.fString(request.getParameter("ORDERQTY"));
    String pickedqty  = su.fString(request.getParameter("PICKEDQTY"));
    String issuedqty = su.fString(request.getParameter("ISSUEDQTY"));
    String reverseqty = su.fString(request.getParameter("REVERSEQTY"));
  
    
    String result ="", sql="";       int n=1;
    String enrolledBy = (String)session.getAttribute("LOGIN_USER");
    if ((dono.length()>0)||(dolnno.length()>0))
    {
      ddb.setDONO(dono);
      ddb.setDOLNNO(dolnno);
      ddb.setITEM(item);
      ddb.setUSERFLD1(itemdesc);
      ddb.setUSERFLD2(orderqty);
      ddb.setUSERFLD3(pickedqty);
      ddb.setUSERFLD4(issuedqty );
      ddb.setUSERFLD5(reverseqty );
      ddb.setUSERFLD6(custname);
      ddb.selectDODET();
	  }
    
   if(action.equalsIgnoreCase("resulterror"))
   {
      fieldDesc=(String)request.getSession().getAttribute("RESULTERROR");
   }
   if(action.equalsIgnoreCase("catchrerror"))
   {
      fieldDesc=(String)request.getSession().getAttribute("CATCHERROR");
   }
%>
<FORM name="form" method="post"  action="/track/OrderIssuingServlet?" >
  <br>
   <CENTER>
  <table border="0" width="100%" cellspacing="0" cellpadding="0" align="center" bgcolor="#dddddd">
      <TR><TH BGCOLOR="#000066" COLSPAN="11"><font color="white"> Order&nbsp;Picking Reverse</font>
  </table>
  <br>
  
  <font face="Times New Roman" size="4">
     <table  border="0" cellspacing="1" cellpadding="2"  bgcolor="">
  <font class="maingreen"><%=fieldDesc%></font>
  </font>
  </table>
  
  <table border="0" width="100%" cellspacing="0" cellpadding="0" align="center" bgcolor="#dddddd">
    <tr>
      <td width="100%">&nbsp;
        <font face="Times New Roman" size="2">
        <center>
          <table border="0" width="90%">
            <tr>
              <td >     <CENTER>
                  <TABLE BORDER="0" CELLSPACING=0 WIDTH="100%">
                    <TR>
                      <TH WIDTH="20%" ALIGN = "RIGHT">Outbound Order No:&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                      <TH WIDTH="30%" ALIGN = "LEFT"><INPUT type = "TEXT" class="inactivegry" size="20"  MAXLENGTH=20 name="DONO" value="<%=ddb.getDONO()%>" READONLY>
                    </TR>
                    <TR>
                      <TH WIDTH="35%" ALIGN="RIGHT" > Line No :&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                      <TH WIDTH="65%" ALIGN = "LEFT"><INPUT type = "TEXT"  class="inactivegry"  size="5"  MAXLENGTH=6 name="DOLNNO" value="<%=ddb.getDOLNNO()%>" READONLY>
                    </TR>
                    <TR>
                      <TH WIDTH="35%" ALIGN="RIGHT" >Product ID :&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                      <TH WIDTH="65%" ALIGN = "LEFT"><INPUT type = "TEXT" size="20"  class="inactivegry"  MAXLENGTH=50 name="ITEM" value="<%=ddb.getITEM()%>" READONLY>
                     </TR>
                     <TR>
                      <TH WIDTH="35%" ALIGN="RIGHT" >Product Desc :&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                      <TH WIDTH="65%" ALIGN = "LEFT"><INPUT type = "TEXT" size="20"  class="inactivegry"  MAXLENGTH=20 name="ITEMDESC" value="<%=ddb.getUSERFLD1()%>" READONLY>
                     </TR>
                       <TR>
                      <TH WIDTH="35%" ALIGN="RIGHT" >Order Qty :&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                      <TH WIDTH="65%" ALIGN = "LEFT"><INPUT type = "TEXT" size="20"  class="inactivegry"  MAXLENGTH=20 name="ORDERQTY" value="<%=ddb.getUSERFLD2()%>" READONLY>
                     </TR>
                      <TR>
                      <TH WIDTH="35%" ALIGN="RIGHT" >Picked Qty :&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                      <TH WIDTH="65%" ALIGN = "LEFT"><INPUT type = "TEXT" size="20"  class="inactivegry"  MAXLENGTH=20 name="PICKEDQTY" value="<%=ddb.getUSERFLD3()%>" READONLY>
                     </TR>
                     <!--  <TR>
                      <TH WIDTH="35%" ALIGN="RIGHT" >Issued Qty :&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                      <TH WIDTH="65%" ALIGN = "LEFT"><INPUT type = "TEXT" size="20"  class="inactive"  MAXLENGTH=20 name="ISSUEDQTY" value="<%=ddb.getUSERFLD4()%>" READONLY>
                     </TR>-->
                      <TR>
                      <TH WIDTH="35%" ALIGN="RIGHT" >Reverse Qty :&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                      <TH WIDTH="65%" ALIGN = "LEFT"><INPUT type = "TEXT" size="20"  class="inactivegry"  MAXLENGTH=20 name="REVERSEQTY" value="<%=ddb.getUSERFLD5()%>" >
                     </TR>
                     </td>
                     
               </TABLE>
                </CENTER>
              </td>
            </tr>
          </table>
          <br>
        </center>
        <INPUT type="Hidden" name="ENCRYPT_FLAG" value="1">
        <INPUT type="Hidden" name="CUSTNO" value="<%=dhb.getCustNo()%>">
         <INPUT type="Hidden" name="CUSTNAME" value="<%=ddb.getUSERFLD6()%>">
         

        <div align="center"><center>
            <table border="0" width="100%" cellspacing="0" cellpadding="0">
              <tr>
                <td align="center">
                <input type="Button" value="Cancel" onClick="window.location.href='/track/deleveryorderservlet?DONO=<%=dono%>&Submit=Go';">
                  &nbsp;
               <!--   <input type="Submit" value="Modify" name="Submit" onClick="return validateForm();"> -->
                  &nbsp;
                 <!--<input type="Submit" value="Delete" name="Submit">-->
                 <input type="Submit" value="Reverse" name="action">
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
   </CENTER>
</FORM>
<%@ include file="footer.jsp" %>
