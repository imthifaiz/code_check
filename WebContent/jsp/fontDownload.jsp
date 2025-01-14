
<%@ page import="com.track.db.util.ItemUtil"%>
<%@ page import="com.track.db.util.InvUtil"%>
<%@ page import="com.track.util.*"%>
<%@ page import="com.track.dao.MovHisDAO"%>
<%@ page import="com.track.dao.TblControlDAO"%>
<%@ page import="com.track.gates.*"%>
<%@ page import="com.track.util.*"%>
<%@ include file="header.jsp"%>

<%

String microsoftActioveSync="http://www.microsoft.com/downloads/details.aspx?familyid=9e641c34-6f7f-404d-a04b-dc09f8141141&displaylang=en&Hash=1WcB59kccM27%2fsFS5KvFi%2fKBcFmnu1rTgT1fa34q7GPyZNZJ%2fPVayFjjNSnJy5hQao4ZV3ymUWuadM7Z7QnsfQ%3d%3d#filelist";

%>
<html>
<style type="text/css">

ul
{
padding: 5;
margin: 20;

}


</style>

  <script language="JavaScript" type="text/javascript" src="js/general.js"></script>
  <title>Download Font</title>
  <link rel="stylesheet" href="css/style.css"></link>
  <script language="JavaScript">
var subWin = null;
function popUpWin(URL) {
 subWin = window.open(URL, 'Items', 'toolbar=0,scrollbars=yes,location=0,statusbar=0,menubar=0,dependant=1,resizable=1,width=700,height=400,left = 200,top = 184');
}

function download()
{
	document.form.action="/track/FileDownloadServlet?action=Upload&fname=FR6800";
	document.form.submit();
}

</script>
  <%@ include file="body.jsp"%>
  <form name="form" method="post" action="/track/FileDownloadServlet?">
    <br></br>
    <center>
      <table border="0" width="100%" cellspacing="0" cellpadding="0" align="center" bgcolor="#dddddd">
        <tr>
          <th bgcolor="#000066" colspan="11">
            <font color="white">Downloads </font>
          </th>
        </tr>
      </table>
      <br></br>
      <table border="0" cellspacing="0" width="30%" bgcolor="#dddddd" align="center">
        <tr>
          <td>
            Download Manual
            <ul type="circle">
              <li><a href="/track/FileDownloadServlet?action=download_track_user_manual">SUNPRO user manual</a></li>
              <li><a href="/track/FileDownloadServlet?action=download_fr6000">FR6000 user manual</a></li>
              <li><a href="/track/FileDownloadServlet?action=download_ls2280">LS2208 user manual</a></li>
              
            </ul>
          </td>
        </tr>
        <tr>
          <td>
           Download PDA Module
            <ul type="circle">
              <li><a href="/track/FileDownloadServlet?action=download_pda_application">PDA file</a></li>
              <li><a href='<%=microsoftActioveSync%>' target='_blank'>Microsoft Active Synch</a></li>
            </ul>
          </td>
        </tr>
        <!--  <tr>
          <td>
           Download Printing Module
            <ul type="circle">
             <li><a href="/track/FileDownloadServlet?action=download_printing_application">SUNPRO Printing Module</a></li>
             <li><a href="/track/FileDownloadServlet?action=download_barcode_font">Bar Code Font</a></li>
            </ul>
          </td>
        </tr> -->
      </table>
    </center>
  </form>
</html>
<%@ include file="footer.jsp"%>