<%@ include file="header.jsp" %>

<script language="JavaScript" type="text/javascript" src="js/calendar.js"></script>
<script language="JavaScript" type="text/javascript" src="js/newSO.js"></script>
<SCRIPT LANGUAGE="JavaScript">
<!-- Begin

var subWin = null;
function popUpWin(URL) {
 subWin = window.open(URL, 'Items', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=700,height=400,left = 200,top = 184');
}
//-->
</script>
<title>Sales Order</title>
<link rel="stylesheet" href="css/style.css">
<%@ include file="body.jsp" %>
<jsp:useBean id="gn"  class="com.track.gates.Generator" />
<jsp:useBean id="su"  class="com.track.util.StrUtils" />
<jsp:useBean id="db"  class="com.track.gates.defaultsBean" />
<jsp:useBean id="cmb" class="com.track.tables.CUSTMST" />
<jsp:useBean id="dhb" class="com.track.tables.DOHDR" />
<jsp:useBean id="ddb" class="com.track.tables.DODET" />

<%
db.setmLogger(mLogger);
dhb.setmLogger(mLogger);
dhb.setmLogger(mLogger);
ddb.setmLogger(mLogger);
    String dono   = su.fString(request.getParameter("DONO"));
    String cust = "";
    String deldate="";
    String origin = "";
    String fieldDesc="<tr><td> Please Choose options from the list box shown above</td></tr>";
   if (dono.length()>0)
    {
        dhb.setDoNo(dono);
        dhb.selectDOHDR();
        cust = dhb.getCustNo();
        cmb.setCUSTNO(cust);
        cmb.selectCUSTMST();
        deldate = dhb.getDelDate();
        origin = dhb.getUserFiled1();
        fieldDesc = ddb.listDODET(dono);
        if(fieldDesc.length()<1) fieldDesc = "<tr><td>No Records Available</td></tr>";
    }
    else{
      fieldDesc = "<tr><td>No Records Available</td></tr>";
    }

%>
<FORM name="form1" method="post" action="createSODET.jsp" onSubmit="return validateSO(document.form1)">
  <br>
  <table border="0" width="100%" cellspacing="0" cellpadding="0" align="center" bgcolor="#dddddd">
      <TR><TH BGCOLOR="#000066" COLSPAN="11"><font color="white">CREATE SALES ORDER</font>
  </table>
  <br>
  <table border="0" width="100%" cellspacing="0" cellpadding="0" align="center" bgcolor="#dddddd">
    <tr>
      <td width="100%">&nbsp;
        <font face="Times New Roman" size="2">
        <center>
          <table border="0" width="95%">
            <tr>
              <td width="100%">
        <center>
          <table border="0" width="95%">
            <tr>
              <td width="100%">
                <CENTER>
                  <TABLE BORDER="0" CELLSPACING=0 WIDTH="100%">
                    <TR>
                      <TH WIDTH="20%" ALIGN = "RIGHT">Sales Order No:
                      <TH WIDTH="30%" ALIGN = "LEFT"><INPUT type = "TEXT" size="15"  MAXLENGTH=20 name="DONO" value="<%=dono%>"> <input type="Submit" value="Go" name="Submit">
                    <TR>
                      <TH WIDTH="20%" ALIGN = "RIGHT">Customer No:
                      <TH WIDTH="30%" ALIGN = "LEFT"><INPUT type = "TEXT" size="20"  MAXLENGTH=20 name="CUST_CODE" value="<%=dhb.getCustNo()%>">
                    <TR>
                      <TH WIDTH="20%" ALIGN = "RIGHT">Customer Name:
                      <TH WIDTH="30%" ALIGN = "LEFT"><INPUT type = "TEXT" size="40"  MAXLENGTH=20 name="CUST_NAME" value="<%=cmb.getCustDesc(cust)%>"><a href="#" onClick="javascript:popUpWin('customer_list.jsp?CUST_NAME='+form1.CUST_NAME.value);"><img src="images/populate.gif" border="0"></a>

                     <TR>
                      <TH WIDTH="20%" ALIGN = "RIGHT">Ordered Date:
                      <TH WIDTH="30%" ALIGN = "LEFT"><INPUT type = "TEXT" size="20"  MAXLENGTH=10 name="ORDDATE" value="<%=gn.getDB2UserDate(dhb.getOrdDate())%>">&nbsp;<a href="javascript:show_calendar('form1.ORDDATE');" onmouseover="window.status='Date Picker';return true;" onmouseout="window.status='';return true;"><img src="images\show-calendar.gif" width=24 height=22 border=0></a>
                    <TR>
                      <TH WIDTH="20%" ALIGN = "RIGHT">Origin:
                      <TH WIDTH="30%" ALIGN = "LEFT"><INPUT type = "TEXT" size="20"  MAXLENGTH=25 name="USERFLD1" value="<%=dhb.getUserFiled1()%>">
                  </TABLE>
                </CENTER>
              </td>
            </tr>
          </table>
          <br>
       <TABLE BORDER="0" CELLSPACING="1" WIDTH="100%" cellpadding="0">
       <tr bgcolor="navy">
         <th width="20%"><font color="#ffffff">SO Line No
         <th width="20%"><font color="#ffffff">Item Code
         <th width="30%"><font color="#ffffff">Description
         <th width="10%"><font color="#ffffff">Qty Ordered
         <th width="10%"><font color="#ffffff">UOM
         <th width="10%"><font color="#ffffff">Delivery Date
       </TABLE>
        </center>
    <table width="100%" border="0" cellspacing="1" cellpadding="0" bgcolor="#eeeeee">
      <%=fieldDesc%>
    </table>
        <INPUT type="Hidden" name="ENCRYPT_FLAG" value="1">
        <INPUT type="Hidden" name="CUST_CODE1" value=<%=dhb.getCustNo()%>>
        <div align="center"><center>
            <BR>
            <table border="0" width="100%" cellspacing="0" cellpadding="0">
              <tr>
                <td align="center">
                  <input type="Button" value="Cancel" onClick="window.location.href='../home'">
                  &nbsp;
                  <input type="Submit" value="Add" name="Submit">
                  &nbsp;
                  <input type="Submit" value="Delete" name="Submit">
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
