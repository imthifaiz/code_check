
<%@ page import="com.track.gates.DbBean"%>
<%@ page import="java.util.*"%>
<%@ page import="javax.naming.*"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.util.*"%>
<%@ page import="com.track.util.*"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="java.util.*"%>

<%@ include file="header.jsp"%>
<html>
<title>Stock Adjustment Details</title>
<link rel="stylesheet" href="css/style.css">
<script language="javascript">
<!--
function popUpWin(URL) {
    subWin = window.open(URL, 'rep', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=700,height=400,left = 200,top = 184');
}



function onReportGenerate(){
     var sRid   = document.form1.REFNO.value;
     if(sRid == "" || sRid == null) {alert("Please Enter Ref No"); document.form1.REFNO.focus(); return false; }
     document.form1.action = "reportProcess.jsp?action=Report Generate";
     document.form1.submit();
}

-->
</script>
<!--script language ="javascript" src="js/vendor.js"></script-->

<%
    String PLANT = (String)session.getAttribute("PLANT");
    if(PLANT == null) PLANT = "COM";
    StrUtils strUtils    = new StrUtils();
    ItemUtil itemUtil    = new ItemUtil();

    List listStock       = new ArrayList();

    String REFNO         =  strUtils.fString(request.getParameter("REFNO"));
    String action        =  strUtils.fString(request.getParameter("action")); // Add / Approve


    boolean isReport   = true;

    boolean isReadyApprove   = true;


    String btnReadyApprove   = "";


    String btnReport   = "";

    String checkBox    = "";
    String stkStatus   = "";



// if there ia value of Reference no. get the details from Stockadj table
String itemList = "";


//control button displays here ...
btnReport  = (isReport) ? "<input type=\"button\" value=\"Report Generate\" onclick=\"onReportGenerate(); \">" : "";

%>
<%@ include file="body.jsp"%>
<FORM name="form1" method="post">

  <br>
  <table border="0" width="100%" cellspacing="0" cellpadding="0" align="center" bgcolor="#dddddd">
    <TR>
      <TH BGCOLOR="#000066" COLSPAN="11"><font color="white">REPORTS</font></TH>
    </TR>
  </table>
  <br>
  <table border="0" width="100%" cellspacing="0" cellpadding="0" align="center" bgcolor="#dddddd">
    <tr>
      <td width="100%">&nbsp;
        <center>
          <table border="0" width="90%">
            <tr>
              <td width="100%"> <CENTER>
                  <TABLE border="0" CELLSPACING=0 WIDTH="100%" bgcolor="#dddddd">
                    <TR>
                      <TH WIDTH="35%" ALIGN="RIGHT" >*  Ref No : </TH>
                      <TD><INPUT name="REFNO" type = "TEXT" value="<%=REFNO%>" size="50"  MAXLENGTH=20>


                      </TD>
                    </TR>
                  </TABLE>
                </CENTER></td>
            </tr>
          </table>
         <table border="0" width="100%" cellspacing="0" cellpadding="0" align="center" bgcolor="#dddddd">
         <TBODY>
         <TR>
               <TD BGCOLOR="#000066" align="left"><B><font color="white">&nbsp;</font></TD>
               <TD BGCOLOR="#000066" align="left"><B><font color="white">ITEM NO</font></TD>
               <TD BGCOLOR="#000066" align="left"><B><font color="white">DESC</font></TD>
               <TD BGCOLOR="#000066" align="left"><B><font color="white">RANK</font></TD>
               <TD BGCOLOR="#000066" align="left"><B><font color="white">WH</font></TD>
               <TD BGCOLOR="#000066" align="left"><B><font color="white">LOC</font></TD>
               <TD BGCOLOR="#000066" align="left"><B><font color="white">BAT</font></TD>
               <TD BGCOLOR="#000066" align="left"><B><font color="white">BIN</font></TD>
               <TD BGCOLOR="#000066" align="left"><B><font color="white">PALL</font></TD>
               <TD BGCOLOR="#000066" align="left"><B><font color="white">QTY <br>ISSUE</font></TD>
               <TD BGCOLOR="#000066" align="left"><B><font color="white">QTY <br>RECIEVE</font></TD>

         </TR>
             <%=itemList%>
          </TBODY>
         </table>
          <br>
        </center>
        <INPUT type="Hidden" name="ENCRYPT_FLAG" value="1">
        <div align="center">
          <center>
            <table border="0" width="100%" cellspacing="0" cellpadding="0">
              <tr>
                <td align="center">
                <input type="hidden" name="type"  value="A">
                <input type="button" value="Back" onClick="window.location.href='indexPage.jsp'">&nbsp;
                <%=btnReport%>
               </td>
              </tr>
            </table>
          </center>
        </div>
   </td>
    </tr>
  </table>
</FORM>

<%@ include file="footer.jsp"%>


