<%@ page import="com.track.util.*"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.gates.*"%>
<%@ page import="com.track.constants.*"%>
<%@ page import="java.util.*"%>
<%@ include file="header.jsp"%>
<html>
<script language="JavaScript" type="text/javascript" src="js/general.js"></script>
<title>Edit Mobile Registration  Email Message</title>
<link rel="stylesheet" href="css/style.css">
<SCRIPT LANGUAGE="JavaScript">


     
     function fnchecked(ischecked)
    {
        if(ischecked)
         document.getElementById("ccDetails").style.display = "";
        else
         document.getElementById("ccDetails").style.display = "none";
    }
     
     
	function onClear()
	{
		document.form1.subject.value="";
		document.form1.Body1.value="";
		document.form1.Body2.value="";
		document.form1.webLink.value="";
                document.form1.ccsubject.value="";
		document.form1.ccBody1.value="";
		document.form1.ccBody2.value="";
		document.form1.ccwebLink.value="";
                document.form1.emailFrom.value=""
                document.form1.ccemailTo.value=""
	
	}
	
	function onAdd(){
        if (document.form1.CC_CHECK.checked == true) 
	{
        
        var emailFrom   = document.form1.emailFrom.value;
        var emailTo   = document.form1.ccemailTo.value;
        if(emailFrom == "" || emailFrom == null) {alert("Please Enter From Email Address"); return false; }
	if(emailTo == "" || emailTo == null) {alert("Please Enter To Email Address"); return false; }
	
         var atpos=emailFrom.indexOf("@");
        var dotpos=emailFrom.lastIndexOf(".");
        if (atpos<1 || dotpos<atpos+2 || dotpos+2>=emailFrom.length)
              {
              alert("Enter a valid from Email address");
              return false;
              }
        
         atpos=emailTo.indexOf("@");
         dotpos=emailTo.lastIndexOf(".");
        if (atpos<1 || dotpos<atpos+2 || dotpos+2>=emailTo.length)
              {
              alert("Enter a valid to Email address");
              return false;
              }

	} 

         document.form1.cmd.value = "UPDATE_EMAIL_MSG"
     	 document.form1.action  =  "mRegisterMailMsg.jsp";
   	 document.form1.submit();
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
        EmailMsgUtil mailUtil = new EmailMsgUtil();
	String res = "";
	
	String action = "";
	String subject  = "",  OrderNo  = "",webLink ="",CC_CHECK="",strChecked="";
        StringBuffer Body1,Body2;
        String ccsubject  = "",  ccwebLink ="",emailFrom="",ccemailTo="";
        StringBuffer ccBody1,ccBody2;
	Body1 = new StringBuffer();
        Body2 = new StringBuffer();
        ccBody1 = new StringBuffer();
        ccBody2 = new StringBuffer();
        
	StrUtils strUtils = new StrUtils();
        res =  strUtils.fString(request.getParameter("result"));
	action = strUtils.fString(request.getParameter("cmd"));
        
          
	
        if (action.equalsIgnoreCase("Clear")) {
		action = "";
                emailFrom="";
		subject  = "";
		webLink= "";
                ccsubject  = "";
		ccwebLink= "";
                ccemailTo= "";
                Body1 = new StringBuffer();
                Body2 = new StringBuffer();
                ccBody1 = new StringBuffer();
                ccBody2 = new StringBuffer();
		
	}  
        if (action.equalsIgnoreCase("UPDATE_EMAIL_MSG")) {
		try{
                Body1 = new StringBuffer();
                Body2 = new StringBuffer();
                ccBody1 = new StringBuffer();
                ccBody2 = new StringBuffer();
                emailFrom=strUtils.fString(request.getParameter("emailFrom"));
                subject=strUtils.fString(request.getParameter("subject"));
                Body1.append(strUtils.fString(request.getParameter("Body1")));
                OrderNo  = strUtils.fString(request.getParameter("OrderNo"));
                Body2.append(strUtils.fString(request.getParameter("Body2")));
                webLink= strUtils.fString(request.getParameter("webLink"));
                CC_CHECK=strUtils.fString(request.getParameter("CC_CHECK"));
                ccsubject=strUtils.fString(request.getParameter("ccsubject"));
                ccBody1.append(strUtils.fString(request.getParameter("ccBody1")));
                ccBody2.append(strUtils.fString(request.getParameter("ccBody2")));
                ccwebLink= strUtils.fString(request.getParameter("ccwebLink"));
                ccemailTo = strUtils.fString(request.getParameter("ccemailTo"));
                
                Hashtable ht = new Hashtable();
                ht.put(IDBConstants.PLANT,plant);
                ht.put(IDBConstants.LOGIN_USER,sUserId);
                ht.put(IDBConstants.MODULE_TYPE,"MOBILE REGISTRATION");
                 ht.put(IDBConstants.EMAIL_FROM,emailFrom);
                ht.put(IDBConstants.SUBJECT,subject);
                ht.put(IDBConstants.BODY1,Body1.toString());
                ht.put(IDBConstants.ORDER_NO,OrderNo);
                ht.put(IDBConstants.BODY2,Body2.toString());
                ht.put(IDBConstants.WEB_LINK,webLink);
                if(!CC_CHECK.equalsIgnoreCase("Y"))CC_CHECK="N";
                ht.put(IDBConstants.IS_CC_CHECKED,CC_CHECK);
                ht.put(IDBConstants.CC_SUBJECT,ccsubject);
                ht.put(IDBConstants.CC_BODY1,ccBody1.toString());
                ht.put(IDBConstants.CC_BODY2,ccBody2.toString());
                ht.put(IDBConstants.CC_WEB_LINK,ccwebLink);
                ht.put(IDBConstants.CC_EMAILTO,ccemailTo);
                
                boolean isUpdated =  mailUtil.updateEmailMessageFormat(ht);
                
                if (!isUpdated) {
                        res = "<font class = " + IConstants.FAILED_COLOR+ ">Failed to edit the details</font>";
                } else {
                        res = "<font class = " + IConstants.SUCCESS_COLOR+ ">Email Message for Mobile registartion edited successfully</font>";
                }
                }catch (Exception e){res = "<font class = " + IConstants.FAILED_COLOR+ ">"+e.getMessage()+"</font>";}
	}  
       
       
	try{
         Map m= mailUtil.getEmailMsgDetails(plant,"MOBILE REGISTRATION");
         
         if(!m.isEmpty()){
              Body1 = new StringBuffer();
              Body2 = new StringBuffer();
              ccBody1 = new StringBuffer();
              ccBody2 = new StringBuffer();
               emailFrom= (String) m.get("EMAIL_FROM");
              subject= (String) m.get("SUBJECT");
              Body1.append((String)m.get("BODY1"));
              Body2.append((String)m.get("BODY2"));
              webLink = (String) m.get("WEB_LINK");
              CC_CHECK = (String) m.get("IS_CC_CHECKED");
              ccemailTo= (String) m.get("CC_EMAILTO");
              ccsubject= (String) m.get("CC_SUBJECT");
              ccBody1.append((String)m.get("CC_BODY1"));
              ccBody2.append((String)m.get("CC_BODY2"));
              ccwebLink = (String) m.get("CC_WEB_LINK");
              if(CC_CHECK.equalsIgnoreCase("Y")) strChecked="checked" ;
             
            }
         }catch(Exception e){
         res = "<font class = " + IConstants.FAILED_COLOR+ ">"+e.getMessage()+"</font>";
         }
	
        
	
        
        
%>

<%@ include file="body.jsp"%>
<FORM name="form1" method="post" action="mRegisterMailMsg.jsp"><br>
<CENTER>
<INPUT type="hidden" name="cmd" value=""/>
<TABLE border="0" width="100%" cellspacing="0" cellpadding="0"
	align="center" bgcolor="#dddddd">
	<TR>
		<TH BGCOLOR="#000066" COLSPAN="11"><font color="white">Edit Mobile Registration Email Message</font></TH>
	</TR>
</TABLE>
<B>
<CENTER><%=res%>

</B> <br>
<INPUT name="OrderType" type="hidden" value="MOBILE REGISTRATION" >
<TABLE border="0" CELLSPACING=0 WIDTH="100%" bgcolor="#dddddd">
 <TR>
		<TH WIDTH="35%" ALIGN="RIGHT">From: </TH>
		<TD><INPUT name="emailFrom" type="TEXT" value="<%=emailFrom%>" size="50" MAXLENGTH=50 STYLE= "background-color: #FFFFFF;">(Email Address)</TD>
	</TR>
   <TR>
            <TH WIDTH="35%" ALIGN="RIGHT"><b>TO :</b> </TH>
            <TD></TD>
   </TR>
   <TR>
		<TH WIDTH="35%" ALIGN="RIGHT">Subject: </TH>
		<TD><INPUT name="subject" type="TEXT" value="<%=subject%>" size="50" MAXLENGTH=50 STYLE= "background-color: #FFFFFF;"></TD>
	</TR>
	<TR>
		<TH WIDTH="35%" ALIGN="RIGHT">Body 1:</TH>
		<TD><textarea name="Body1"  id="Body1" cols="50" rows="2" wrap="hard" onKeyDown="limitText(this.form.Body1,100);"><%=Body1.toString()%></textarea></TD>
	</TR>
        <TR>
		<TH WIDTH="35%" ALIGN="RIGHT">Registration Number: </TH>
		<TD><INPUT name="OrderNo" type="TEXT" value="<<Order No>>" class="inactiveGry"  size="50" MAXLENGTH=100 readonly></TD>
	</TR>
	<TR>
		<TH WIDTH="35%" ALIGN="RIGHT">Body 2:</TH>
		<TD><textarea name="Body2"  id="Body2" cols="50" rows="2" wrap="hard" onKeyDown="limitText(this.form.Body2,100);" ><%=Body2.toString()%></textarea></TD>
	</TR>
	<TR>
		<TH WIDTH="35%" ALIGN="RIGHT">Web Link: </TH>
		<TD><INPUT name="webLink" type="TEXT" value="<%=webLink%>" size="50" MAXLENGTH=50 STYLE= "background-color: #FFFFFF;" ></TD>
	</TR>

        <TR>
        <TD COLSPAN =2><br> </TD>
        </TR>
        <TR>
        <TH  ALIGN="right"><strong>Copy To(Cc):</strong> </TH>
	<TD ALIGN="left">
             <input type="checkbox" name="CC_CHECK" VALUE="Y" <%=strChecked%> onclick="fnchecked(this.checked);" /><!-- onClick="showHide('ccDetails');" > -->

        </TD>
	</TR>
         <TR>
        <TD COLSPAN =2> <br></TD>
        </tr>
        </table>
        <div id="ccDetails" <% if(CC_CHECK.equalsIgnoreCase("N")){ System.out.println(CC_CHECK);%>style="display:none;"<%}%> > 
        <TABLE border="0" CELLSPACING=0 WIDTH="100%" bgcolor="#dddddd">
	

         <TR>
		<TH WIDTH="35%" ALIGN="RIGHT">To: </TH>
		<TD><INPUT name="ccemailTo" type="TEXT" value="<%=ccemailTo%>" size="50" MAXLENGTH=50 STYLE= "background-color: #FFFFFF;">(Email Address)</TD>
	</TR>
        <TR>
        <TH WIDTH="35%" ALIGN="RIGHT">Subject: </TH>
        <TD><INPUT name="ccsubject" type="TEXT" value="<%=subject%>" size="50" MAXLENGTH=50 STYLE= "background-color: #FFFFFF;" ></TD>
        </TR>
        <TR>
        <TH WIDTH="35%" ALIGN="RIGHT">Body 1:</TH>
                 <TD><textarea name="ccBody1"  id="ccBody1" cols="50" rows="2" wrap="hard" onKeyDown="limitText(this.form.ccBody1,100);"><%=ccBody1.toString()%></textarea></TD>
        </TR>
        <TR>
        <TH WIDTH="35%" ALIGN="RIGHT">Registration Number: </TH>
                  <TD><INPUT name="ccOrderNo" type="TEXT" value="<<Order No>>" class="inactiveGry" size="50" MAXLENGTH=100 readonly></TD>
        </TR>
        <TR>
        <TH WIDTH="35%" ALIGN="RIGHT">Body 2:</TH>
                 <TD><textarea name="ccBody2"  id="ccBody2" cols="50" rows="2" wrap="hard" onKeyDown="limitText(this.form.ccBody2,100);" ><%=ccBody2.toString()%></textarea></TD>
        </TR>
        <TR>
        <TH WIDTH="35%" ALIGN="RIGHT">Web Link </TH>
                  <TD><INPUT name="ccwebLink" type="TEXT" value="<%=ccwebLink%>" size="50" MAXLENGTH=50 STYLE= "background-color: #FFFFFF;" ></TD>
        </TR>
          <TR>
        <TD COLSPAN =2> <br></TD>
        </tr>
        </TABLE>
        

</div>
        
  <TABLE border="0" CELLSPACING=0 WIDTH="100%" bgcolor="#dddddd">      
	<TR>
		<TD COLSPAN=2>
		<center><INPUT class="Submit" type="BUTTON" value="Back"
			onClick="window.location.href='../home'">&nbsp;&nbsp; <INPUT
			class="Submit" type="BUTTON" value="Clear" onClick="onClear();"
			>&nbsp;&nbsp; <INPUT class="Submit" type="BUTTON"
			value="Save" onClick="onAdd();" >&nbsp;&nbsp;
		</TD>
	</TR>
</TABLE>

</CENTER>

</FORM>
</BODY>
</HTML>
<%@ include file="footer.jsp"%>

