<%@ include file="header.jsp" %>

<script language="JavaScript" type="text/javascript" src="js/calendar.js"></script>
<script language="JavaScript" type="text/javascript" src="js/newPO.js"></script>
<SCRIPT LANGUAGE="JavaScript">
<!-- Begin

var subWin = null;
function popUpWin(URL) {
 subWin = window.open(URL, 'Items', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=700,height=400,left = 200,top = 184');
}
//-->
</script>

<title>Incoming Order</title>
<link rel="stylesheet" href="css/style.css">
<%@ include file="body.jsp" %>
<jsp:useBean id="gn"  class="com.track.gates.Generator" />
<jsp:useBean id="sl"  class="com.track.gates.selectBean" />
<jsp:useBean id="db"  class="com.track.gates.defaultsBean" />
<jsp:useBean id="su"  class="com.track.util.StrUtils" />
<jsp:useBean id="vmb" class="com.track.tables.VENDMST" />
<jsp:useBean id="phb" class="com.track.tables.POHDR" />
<jsp:useBean id="pdb" class="com.track.tables.PODET" />

<%
    String pono   = su.fString(request.getParameter("PONO"));
    String vend = "";
    String deldate="";
    String origin = "";
    String fieldDesc="<tr><td> Please Choose options from the list box shown above</td></tr>";
  
   if (pono.length()>0)
    {
        phb.setPONO(pono);
        phb.selectPOHDR();
        vend = phb.getVENDNO();
        vmb.setVENDNO(vend);
        vmb.selectVENDMST();
        deldate = phb.getDELDATE();
        origin = phb.getUSERFLD1();
		
        fieldDesc = pdb.listPODET(pono);
        if(fieldDesc.length()<1) fieldDesc = "<tr><td>No Records Available</td></tr>";
    }
    else{
      fieldDesc = "<tr><td>No Records Available</td></tr>";
    }

%>
<FORM name="form" method="post" action="createPODET.jsp" onSubmit="return validatePO(document.form)">
  <br>
  <table border="1" width="100%" cellspacing="0" cellpadding="0" align="center" bgcolor="#dddddd">
      <TR><TH BGCOLOR="#000066" COLSPAN="11"><font color="white">CREATE INCOMING ORDER</font>
  </table>
  <br>
  <table border="1" width="100%" cellspacing="0" cellpadding="0" align="center" bgcolor="#dddddd">
    <tr>
      <td width="100%">&nbsp;
        <font face="Times New Roman" size="2">
        <center>
          <table border="0" width="90%">
            <tr>
              <td width="100%">
        <center>
          <table border="0" width="90%">
            <tr>
              <td width="100%">
                <CENTER>
                  <TABLE BORDER="0" CELLSPACING=1 WIDTH="100%">
                    <TR>
                      <TH WIDTH="20%" ALIGN = "RIGHT">Incoming Order No:
                      <TH WIDTH="30%" ALIGN = "LEFT"><INPUT type = "TEXT" size="20"  MAXLENGTH=20 name="PONO" value="<%=pono%>"> <input type="Submit" value="Go" name="Submit">
                    <TR>
                      <TH WIDTH="20%" ALIGN = "RIGHT">Vendor No:
                      <TH WIDTH="30%" ALIGN = "LEFT"><INPUT type = "TEXT" size="20"  MAXLENGTH=20 name="VENDNO" value="<%=vend%>">
                    <TR>
                      <TH WIDTH="20%" ALIGN = "RIGHT">Vendor Name:
                      <TH WIDTH="30%" ALIGN = "LEFT"><INPUT type = "TEXT" size="50"  MAXLENGTH=20 name="VNAME" value="<%=vmb.getVendDesc(vend)%>"><a href="#" onClick="javascript:popUpWin('vendSumm1.jsp?P=Y&VENDNAME='+form.VNAME.value);"><img src="images/populate.gif" border="0"></a>
                    <TR>
                      <TH WIDTH="20%" ALIGN = "RIGHT">Date Required:
                      <TH WIDTH="30%" ALIGN = "LEFT"><INPUT type = "TEXT" size="20"  MAXLENGTH=10 name="DELDATE" value="<%=deldate%>">&nbsp;<a href="javascript:show_calendar('form.DELDATE');" onmouseover="window.status='Date Picker';return true;" onmouseout="window.status='';return true;"><img src="images\show-calendar.gif" width=24 height=22 border=0></a>
                    <TR>
                      <TH WIDTH="20%" ALIGN = "RIGHT">Origin:
                      <TH WIDTH="30%" ALIGN = "LEFT"><INPUT type = "TEXT" size="20"  MAXLENGTH=10 name="USERFLD1" value="<%=origin%>">
                  </TABLE>
                </CENTER>
              </td>
            </tr>
          </table>
          <br>
       <TABLE BORDER="1" CELLSPACING="0" WIDTH="100%" bgcolor="navy">
       <tr>
         <th width="20%"><font color="#ffffff">PO Line No
         <th width="20%"><font color="#ffffff">Item Code
         <th width="40%"><font color="#ffffff">Description
         <th width="10%"><font color="#ffffff">Qty Ordered
         <th width="10%"><font color="#ffffff">UOM
       </TABLE>
        </center>
    <table width="100%" border="0" cellspacing="0" cellpadding="5" bgcolor="#eeeeee">
      <%=fieldDesc%>
    </table>

        <INPUT type="Hidden" name="ENCRYPT_FLAG" value="1">
        <div align="center"><center>
          <br>
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
