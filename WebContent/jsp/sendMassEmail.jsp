<%@ page import="com.track.util.*"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.gates.*"%>
<%@ page import="com.track.constants.*"%>
<%@ page import="java.util.*"%>
<%@ include file="header.jsp"%>
<html>
<script language="JavaScript" type="text/javascript" src="js/general.js"></script>
<title>Send Mass Email Message</title>
<link rel="stylesheet" href="css/style.css">
<SCRIPT LANGUAGE="JavaScript">

function onSend(){
     
   		document.form1.action  = "/track/EmailServlet?action=Send Mass Email to all Customers";
   	    document.form1.submit();
   	   
	}
     
     
	function onClear()
	{
		document.form1.subject.value="";
		document.form1.Body1.value="";
		document.form1.Body2.value="";
		document.form1.webLink.value="";

	}
	

        
        function limitText(limitField, limitNum) {
	if (limitField.value.length > limitNum) {
		limitField.value = limitField.value.substring(0, limitNum);
	} 
        }
	
</script>
<%
	session = request.getSession();
	String plant = (String) session.getAttribute("PLANT");
	String sUserId = (String) session.getAttribute("LOGIN_USER");
      
	String res = "";
	
	String action = "";
	String subject  = "",  webLink ="";
        StringBuffer Body1,Body2;
       
	Body1 = new StringBuffer();
        Body2 = new StringBuffer();
      
        
	StrUtils strUtils = new StrUtils();
        res =  strUtils.fString(request.getParameter("result"));
	action = strUtils.fString(request.getParameter("cmd"));
        
          
        if (action.equalsIgnoreCase("Clear")) {
		action = "";
		subject  = "";
		webLink= "";
                Body1 = new StringBuffer();
                Body2 = new StringBuffer();
	}  
      
        
	
        
        
%>

<%@ include file="body.jsp"%>
<FORM name="form1" method="post" enctype="multipart/form-data" action="/track/EmailServlet?" ><br>
<CENTER>
<INPUT type="hidden" name="cmd" value=""/>
<TABLE border="0" width="100%" cellspacing="0" cellpadding="0"
	align="center" bgcolor="#dddddd">
	<TR>
		<TH BGCOLOR="#000066" COLSPAN="11"><font color="white">Send Mass Email Message</font></TH>
	</TR>
</TABLE>
<B>
<CENTER><%=res%>

</B> <br>

<TABLE border="0" CELLSPACING=0 WIDTH="100%" bgcolor="#dddddd">
        <TR>
		<TH WIDTH="35%" ALIGN="RIGHT">From: </TH>
		<TD><INPUT name="from" type="TEXT" value="" size="50" MAXLENGTH=50 STYLE= "background-color: #FFFFFF;">(Email Address)</TD>
	</TR>
   
   <TR>
		<TH WIDTH="35%" ALIGN="RIGHT">Subject: </TH>
		<TD><INPUT name="subject" type="TEXT" value="" size="50" MAXLENGTH=50 STYLE= "background-color: #FFFFFF;"></TD>
	</TR>
	<TR>
		<TH WIDTH="35%" ALIGN="RIGHT">Body 1:</TH>
		<TD><textarea name="Body1"  id="Body1" cols="50" rows="2" wrap="hard" onKeyDown="limitText(this.form.Body1,100);"></textarea></TD>
	</TR>
      
	<TR>
		<TH WIDTH="35%" ALIGN="RIGHT">Body 2:</TH>
		<TD><textarea name="Body2"  id="Body2" cols="50" rows="2" wrap="hard" onKeyDown="limitText(this.form.Body2,100);" ></textarea></TD>
	</TR>
	<TR>
		<TH WIDTH="35%" ALIGN="RIGHT">Web Link: </TH>
		<TD><INPUT name="webLink" type="TEXT" value="<%=webLink%>" size="50" MAXLENGTH=50 STYLE= "background-color: #FFFFFF;" ></TD>
	</TR>
        <TR>
		<TH WIDTH="35%" ALIGN="RIGHT">Embed Inline Image:</TH>
		<TD><INPUT name="IMAGE_UPLOAD" type="File" size="20" MAXLENGTH=100>&nbsp;(Upload JPEG,GIF original Image format)
        </TR>

       
         <TR>
        <TD COLSPAN =2> <br></TD>
        </tr>
        </table>
        
        
  <TABLE border="0" CELLSPACING=0 WIDTH="100%" bgcolor="#dddddd">      
	<TR>
		<TD COLSPAN=2>
		<center><INPUT class="Submit" type="BUTTON" value="Back"
			onClick="window.location.href='../home'">&nbsp;&nbsp; <INPUT
			class="Submit" type="BUTTON" value="Clear" onClick="onClear();"
			>&nbsp;&nbsp;   <INPUT class="Submit" type="BUTTON" value="Send Mass Email to all Customers" onClick="onSend();" >
                       
		</TD>
	</TR>
</TABLE>

</CENTER>

</FORM>
</BODY>
</HTML>
<%@ include file="footer.jsp"%>

