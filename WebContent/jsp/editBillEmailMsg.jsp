<html>
<head>
<%@ page import="com.track.util.StrUtils.*"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.gates.*"%>
<%@ page import="com.track.constants.*"%>
<%@ page import="java.util.*"%>
<%@ include file="header.jsp"%>
<%String rootURI = com.track.util.http.HttpUtils.getRootURI(request);%>
<%
	String title = "Edit Bill Email Message";
%>
<%@include file="sessionCheck.jsp" %>
<jsp:include page="header2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
	<jsp:param name="mainmenu" value="<%=IConstants.SETTINGS%>"/>
	<jsp:param name="submenu" value="<%=IConstants.PRINTOUT_CONFIGURATION%>"/>
</jsp:include>
<link rel="stylesheet" type="text/css" href="<%=rootURI%>/jsp/css/typeahead.css">
<link rel="stylesheet" type="text/css" href="<%=rootURI%>/jsp/css/accounting.css">
<link rel="stylesheet" type="text/css" href="<%=rootURI%>/jsp/dist/css/bootstrap3-wysihtml5.min.css">
<script src="<%=rootURI%>/jsp/js/general.js"></script>

<script>



     
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
		//document.form1.ccBody2.value="";
		document.form1.ccwebLink.value="";
        document.form1.emailFrom.value="";
        document.form1.ccemailTo.value="";
        document.form1.emailTo.value = "";
        var sa_options = document.form1.send_attachment; 
        for(var sa_option_index = 0; sa_option_index < sa_options.length; sa_option_index ++){
            sa_options[sa_option_index].checked=false;
        }
        document.getElementById("turnON").checked = true;
	
	}
	
	function onAdd(){

		var emailFrom = document.form1.emailFrom.value;
		if (emailFrom == "" || emailFrom == null) {
			alert("Please Enter From Email Address");
			document.form1.emailFrom.focus();
			return false;
		}

		var atpos = emailFrom.indexOf("@");
		var dotpos = emailFrom.lastIndexOf(".");
		if ((atpos < 1) || (dotpos < atpos + 2) || (dotpos + 2 >= emailFrom.length)) {
			alert("Enter a valid from Email address");
			document.form1.emailFrom.focus();
			return false;
		}
		if (document.form1.subject.value == ""){
			alert("Please Enter Email Subject");
			document.form1.subject.focus();
			return false;
		}
		if (document.form1.Body1.value == ""){
			alert("Please Enter Email Body");
			document.form1.Body1.focus();
			return false;
		}
		if (document.form1.send_attachment.value == ""){
			alert("Select Send Attachment option");
			return false;
		}
/*                
		 var emailTo = document.form1.emailTo.value;
                 if( document.form1.ISAUTOEMAIL[1].checked == true ){ //turned on
                
                    if (emailTo == "" || emailTo == null) {
                     alert("Please Enter To Email Address");
                     document.form1.emailTo.focus();
                     return false;
                    }
                    
                    atpos = emailTo.indexOf("@");
		    dotpos = emailTo.lastIndexOf(".");
		if (atpos < 1 || dotpos < atpos + 2 || dotpos + 2 >= emailTo.length) {
			alert("Enter a valid to Email address");
			document.form1.emailTo.focus();
			return false;
		}
               }
                 
                
	
      
		if (document.form1.CC_CHECK.checked == true) {
			
			var ccemailTo = document.form1.ccemailTo.value;
			if (ccemailTo == "" || ccemailTo == null) {
				alert("Please Enter To CCEmail Address");
				document.form1.ccemailTo.focus();
				return false;
			}			

			atpos = ccemailTo.indexOf("@");
			dotpos = ccemailTo.lastIndexOf(".");
			if (atpos < 1 || dotpos < atpos + 2
					|| dotpos + 2 >= ccemailTo.length) {
				alert("Enter a valid to CCEmail address");
				document.form1.ccemailTo.focus();
				return false;
			}

		}
		*/

		

		document.form1.cmd.value = "UPDATE_EMAIL_MSG";
		document.form1.Body1.value = $('.wysihtml5-sandbox').contents().find('.wysihtml5-editor').html();
		document.form1.action = "<%=rootURI%>/email_config/bill";
		document.form1.submit();
	}

	function limitText(limitField, limitNum) {
		if (limitField.value.length > limitNum) {
			limitField.value = limitField.value.substring(0, limitNum);
		}
	}
</script>
</head>
<body>
<%
		session = request.getSession();
		String plant = (String) session.getAttribute("PLANT");
		String sUserId = (String) session.getAttribute("LOGIN_USER");
	    EmailMsgUtil mailUtil = new EmailMsgUtil();
		String res = "";
		
		String action = "";
		String subject  = "",  OrderNo  = "",webLink ="",CC_CHECK="",strChecked="",isAutoEmail = "", sendAttachment = "";
	    StringBuffer Body1,Body2;
	    String ccsubject  = "",  ccwebLink ="",emailTo = "",emailFrom="",ccemailTo="";
	    StringBuffer ccBody1,ccBody2;
		Body1 = new StringBuffer();
	    Body2 = new StringBuffer();
	    ccBody1 = new StringBuffer();
	    ccBody2 = new StringBuffer();
        
	
	
        res =  StrUtils.fString(request.getParameter("result"));
        if ("".equals(res)){
        	res = StrUtils.fString((String)session.getAttribute("result"));
        	session.setAttribute("result", "");
        	if (!"".equals(res)){
        		if (res.contains("success")){
        			res = "<font class = " + IConstants.SUCCESS_COLOR
							+ ">" + res + "</font>";
        		}else{
        			res = "<font class = " + IConstants.FAILED_COLOR
							+ ">" + res + "</font>";
        		}
        	}
        }
		action = StrUtils.fString(request.getParameter("cmd"));
       
          
	
        if (action.equalsIgnoreCase("Clear")) {
			action = "";
	        emailFrom="";
	        emailTo = "";
			subject  = "";
			webLink= "";
	        ccsubject  = "";
			ccwebLink= "";
	        ccemailTo= "";
            Body1 = new StringBuffer();
            Body2 = new StringBuffer();
            ccBody1 = new StringBuffer();
            ccBody2 = new StringBuffer();
            sendAttachment = "";
		
		}  
        
	try{
         Map<String, String> m= mailUtil.getEmailMsgDetails(plant,IConstants.BILL);
         
         if(!m.isEmpty()){
              Body1 = new StringBuffer();
              Body2 = new StringBuffer();
              ccBody1 = new StringBuffer();
              ccBody2 = new StringBuffer();
               emailFrom= (String) m.get("EMAIL_FROM");
               emailTo= (String) m.get("EMAIL_TO");
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
              isAutoEmail	 = (String) m.get("ISAUTOEMAIL");
              sendAttachment = (String) m.get(IDBConstants.SEND_ATTACHMENT);
             /*  if(CC_CHECK.equalsIgnoreCase("Y")) strChecked="checked" ; */
             
            }
         }catch(Exception e){
         res = "<font class = " + IConstants.FAILED_COLOR+ ">"+e.getMessage()+"</font>";
         }
	
        
	
        
        
%>

<div class="text-center"><strong><%=res%></strong></div>

<div class="container-fluid m-t-20">
	 <div class="box"> 
	  <!-- Thanzith Modified on 23.02.2022 -->
	 <ul class="breadcrumb backpageul">    
                <li class="underline-on-hover"><a href="../home">Dashboard</a></li>	
                <li><label>Edit Bill Email Message</label></li>                                   
            </ul>
        <!-- Thanzith Modified on 23.02.2022 -->  
            <div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title"><%=title %></h1>
               <h1 style="font-size: 18px; cursor: pointer;"
				class="box-title pull-right"
				onclick="window.location.href='../home'">
				<i class="glyphicon glyphicon-remove"></i>
			</h1>
		</div>
		
 <div class="box-body">

   

  <form class="form-horizontal" name="form1" method="post" action="">
        <INPUT type="hidden" name="cmd" value="" />
        <INPUT name="OrderType" type="hidden" value="MOBILE ORDER">
    <div style="display:none;">
                 
    <div class="form-group">
         <label class="control-label col-form-label col-sm-2" for="To Email">To</label>
      <div class="col-sm-4">          
       <INPUT class="form-control" name="emailTo" type="TEXT" value="<%=emailTo%>" size="50" MAXLENGTH=50>
      </div>
    </div>
    <%-- <div class="form-group">
    <div class="form-inline">
  <div class="col-sm-4">
    <label class="radio-inline">
      <input type="radio" name="ISAUTOEMAIL" id="turnOff" value="N"
			<%if(isAutoEmail.equalsIgnoreCase("N")) {%> checked <%}%>>Turn Off Auto Email 
    </label>
    <label class="radio-inline">
      <input type="radio" name="ISAUTOEMAIL" id="turnOn"
			value="Y" <%if(isAutoEmail.equalsIgnoreCase("Y")) {%> checked <%}%>>Turn On Auto Email
    </label>
     </div>
</div>
</div> --%>
     <div class="form-group">
      <label class="control-label  col-form-label col-sm-2" for="Order Number">Order Number</label>
      <div class="col-sm-4">          
        <INPUT class="form-control" name="OrderNo" type="TEXT" value=""	size="50" MAXLENGTH=30 readonly>
      </div>
    </div>
    <div class="form-group">
      <label class="control-label  col-form-label col-sm-2" for="Body 2">Body 2</label>
      <div class="col-sm-4">          
        <textarea class="form-control" name="Body2" id="Body2" cols="50" rows="2" onKeyDown="limitText(this.form.Body2,100);"><%=Body2.toString()%></textarea>
      </div>
    </div>
    <div class="form-group">
      <label class="control-label  col-form-label col-sm-2" for="Web Link">Web Link</label>
      <div class="col-sm-4">          
        <INPUT class="form-control" name="webLink" type="TEXT" value="<%=webLink%>"	size="50" MAXLENGTH=50>
      </div>
    </div>
    
      <div class=form-group style="Display:none">
      <div class="col-sm-offset-4 col-sm-8">   
      <label class="checkbox-inline">      
        <INPUT type="checkbox" name="CC_CHECK" VALUE="N"
			<%=strChecked%> onclick="fnchecked(this.checked);" >Copy To(cc)</label>
			</div>
    </div> 
     <div class="form-group">
      <div class="col-form-label col-sm-2"> 
      <a href="#" class="Show" style="font-size: 15px">Show CC Details</a>
      <a href="#" class="Hide" style="font-size: 15px; display:none">Hide CC Details</a>
      </div>
      </div>
    <%-- <div id="ccDetails"
	<% if(CC_CHECK.equalsIgnoreCase("N")){ System.out.println(CC_CHECK);%>
	style="display: none;" <%}%>> --%>
	
    <div id="target" style="display:none">
    	

            <div class="form-group">
      <label class="control-label  col-form-label col-sm-2" for="To">To</label>
      <div class="col-sm-4">          
        <INPUT class="form-control" name="ccemailTo" type="TEXT" value="<%=ccemailTo%>"	size="50" MAXLENGTH=30>
      </div>
    </div>
    <div class="form-group">
      <label class="control-label col-form-label col-sm-2" for="Subject">Subject</label>
      <div class="col-sm-4">          
        <INPUT class="form-control"  name="ccsubject" type="TEXT" value="<%=ccsubject%>" size="50" MAXLENGTH=100>
      </div>
    </div>
    <div class="form-group">
      <label class="control-label  col-form-label col-sm-2" for="Body">Body 1</label>
      <div class="col-sm-4">          
        <textarea class="form-control" name="ccBody1" id="ccBody1" cols="50" rows="10" onKeyDown="limitText(this.form.ccBody1,100);"><%=ccBody1.toString()%></textarea>
      </div>
    </div>
    <div class="form-group">
      <label class="control-label  col-form-label col-sm-2" for="Order Number">Order Number</label>
      <div class="col-sm-4">          
        <INPUT class="form-control" name="ccOrderNo" type="TEXT" value="" size="50" MAXLENGTH=30 readonly>
      </div>
    </div>
    <div class="form-group">
      <label class="control-label col-form-label col-sm-2" for="Body 2">Body 2</label>
      <div class="col-sm-4">          
         <textarea class="form-control" name="ccBody2" id="ccBody2" cols="50" rows="2" onKeyDown="limitText(this.form.Body2,100);"><%=Body2.toString()%></textarea>
      </div>
    </div>
    <div class="form-group">
      <label class="control-label  col-form-label col-sm-2" for="Web Link">Web Link</label>
      <div class="col-sm-4">          
       <INPUT class="form-control" name="ccwebLink" type="TEXT" value="<%=ccwebLink%>" size="50" MAXLENGTH=50>
      </div>
    </div>
        
        </div>
    </div>

	<div class="form-group">
      <label class="control-label col-form-label col-sm-2" for="From">From</label>
      <div class="col-sm-4">          
        <INPUT class="form-control" name="emailFrom" type="TEXT" value="<%=emailFrom%>" size="50" MAXLENGTH=50>
      </div>
      <div class="form-inline">
      <div class="col-sm-6">
      <label class="control-label col-form-label col-sm-2" for="AUTO MAIL">Auto Mail</label>
      
			<label class="radio-inline">
      	<INPUT name="ISAUTOEMAIL" type = "radio"  value="Y" id="turnON" <%if(isAutoEmail.equalsIgnoreCase("Y")) {%> checked <%}%>>Enable
    	</label>
    	<label class="radio-inline">
      	<INPUT  name="ISAUTOEMAIL" type = "radio" value="N" id="turnOff"<%if(isAutoEmail.equalsIgnoreCase("N")) {%> checked <%}%>>Disable
    	</label>				
      </div>
    </div>
    </div>
    <div class="form-group">
      <label class="control-label  col-form-label col-sm-2" for="Subject">Subject</label>
      <div class="col-sm-4">          
        <INPUT class="form-control" name="subject" type="TEXT" value="<%=subject%>" size="50" MAXLENGTH=100>
      </div>
    </div>
    <div class="form-group">
      <label class="control-label  col-form-label col-sm-2" for="Body">Body</label>
      <div class="col-sm-4">          
      <textarea class="form-control" name="Body1" id="Body1" cols="50" rows="15" onKeyDown="limitText(this.form.Body1,200);"><%=Body1.toString()%></textarea>
      </div>
      <div class="col-sm-12 col-md-4">
      	Allowed Parameters in subject : 
      	<ul>
      		<li>{BILL_NO}</li>
      		<li>{COMPANY_NAME}</li>
      	</ul>
      	Allowed Parameters in body : 
      	<ul>
      		<li>{BILL_NO}</li>
      		<li>{SUPPLIER_NAME}</li>
      	</ul>
      </div>
    </div>

   <div class="form-group">
      <label class="control-label  col-form-label col-sm-2" for="Web Link">Send Attachment</label>
      <div class="col-sm-2">         
       <input type="radio" name="send_attachment" value="bill" /> Bill<br/>
       <input type="radio" name="send_attachment" value="none" /> None<br/>
      </div>
    </div>
        
    <div class="form-group">        
     <div class="col-sm-offset-4 col-sm-8">
     <!-- <button type="button" class="Submit btn btn-default" onClick="window.location.href='../home'"><b>Back</b></button>&nbsp;&nbsp; -->
       <!-- <button type="button" class="Submit btn btn-default" onClick="{backNavigation('settings.jsp','OC');}"><b>Back</b></button>&nbsp;&nbsp; -->
      	<button type="button" class="Submit btn btn-default" onClick="onClear();">Clear</button>&nbsp;&nbsp;
      	<button type="button" class="btn btn-success" onClick="onAdd();">Save</button>&nbsp;&nbsp;
      </div>
    </div>
  </form>
</div>
</div>
</div>
<script type="text/javascript" src="<%=rootURI%>/jsp/dist/js/bootstrap3-wysihtml5.all.min.js"></script>

<script>
$(document).ready(function(){
	$('#Body1').wysihtml5();
    $('[data-toggle="tooltip"]').tooltip();   
    document.form1.send_attachment.value = '<%=sendAttachment%>';
    $('.Show').click(function() {
	    $('#target').show(500);
	    $('.ShowSingle').hide(0);
	    $('.Show').hide(0);
	    $('.Hide').show(0);
	  
	});
    $('.Hide').click(function() {
	    $('#target').hide(500);
	    $('.ShowSingle').show(0);
	    $('.Show').show(0);
	    $('.Hide').hide(0);
	   
	});
  
});
</script>



<jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
</jsp:include>

</body>
</html>