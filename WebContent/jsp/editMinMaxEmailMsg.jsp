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
	String title = "Edit Inventory Min/Max Qty Email Alert";
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
<script type="text/javascript" src="../jsp/js/clockpicker.js"></script>
<link rel="stylesheet" href="../jsp/css/clockpicker.css">
<script src="<%=rootURI%>/jsp/js/general.js"></script>

<script>
	function onClear()
	{
        document.form1.emailFrom.value="";
        document.form1.emailTo.value = "";
		document.form1.subject.value="";
		document.form1.Body1.value="";
//         var sa_options = document.form1.send_attachment; 
//         for(var sa_option_index = 0; sa_option_index < sa_options.length; sa_option_index ++){
//             sa_options[sa_option_index].checked=false;
//         }
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


		if (document.getElementById("turnON").checked == true) {
			if (document.form1.emailTo.value == ""){
				alert("Please Enter To Email Address");
				return false;
			}
		}

		document.form1.cmd.value = "UPDATE_EMAIL_MSG";
		document.form1.Body1.value = $('.wysihtml5-sandbox').contents().find('.wysihtml5-editor').html();
		document.form1.action = "<%=rootURI%>/email_config/minmax";
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
		String subject  = "",isAutoEmail = "",sendAttachment="",emailTo = "",emailFrom="",time="";
	    StringBuffer Body1;
		Body1 = new StringBuffer();
		
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
			time="";
            Body1 = new StringBuffer();
		}  
        
	try{
         Map<String, String> m= mailUtil.getEmailMsgDetails(plant,IConstants.MINMAX);
         
         if(!m.isEmpty()){
              Body1 = new StringBuffer();
              emailFrom= (String) m.get("EMAIL_FROM");
              emailTo= (String) m.get("EMAIL_TO");
              subject= (String) m.get("SUBJECT");
              Body1.append((String)m.get("BODY1"));
              isAutoEmail	 = (String) m.get("ISAUTOEMAIL");
              time= (String) m.get("TIME");
              sendAttachment = (String) m.get(IDBConstants.SEND_ATTACHMENT);
            }
         }catch(Exception e){
         res = "<font class = " + IConstants.FAILED_COLOR+ ">"+e.getMessage()+"</font>";
         }
%>

<div class="container-fluid m-t-20">
	 <div class="box"> 
	  <!-- Thanzith Modified on 23.02.2022 -->
	 <ul class="breadcrumb backpageul">    
                <li class="underline-on-hover"><a href="../home">Dashboard</a></li>	
                <li><label>Edit Inventory Min/Max Qty Email Alert</label></li>                                   
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

   <div class="text-center"><strong><%=res%></strong></div>

  <form class="form-horizontal" name="form1" method="post" action="">
        <INPUT type="hidden" name="cmd" value="" />
        <INPUT name="OrderType" type="hidden" value="MOBILE ORDER">
    

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
         <label class="control-label col-form-label col-sm-2" for="To Email">To</label>
      <div class="col-sm-4">          
       <INPUT class="form-control" name="emailTo" type="TEXT" value="<%=emailTo%>" size="50" MAXLENGTH=500>
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
    </div>
    
     <div class="form-group" style="display:none">
      		<label class="control-label col-form-label col-sm-2" for=" Time">Mail Send Time</label>
      		<div class="col-sm-4">
      		<div class="input-group">   
      	  	 	<input class="form-control" name="TIME" id="TIME" type = "TEXT" value="<%=time%>" size="50"  MAXLENGTH=50 placeholder="HH:MM" readonly> 
  			</div>
      		</div>
    </div>
    
       <div class="form-group" style="display:none">
      <label class="control-label  col-form-label col-sm-2" for="Web Link">Send Attachment</label>
      <div class="col-sm-2">         
       <input type="radio" name="send_attachment" value="bill" /> Bill<br/>
       <input type="radio" name="send_attachment" value="none" /> None<br/>
      </div>
    </div>
        
    <div class="form-group">        
     <div class="col-sm-offset-4 col-sm-8">
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
	    $('.Show').hide(0);
	    $('.Hide').show(0);
	});
    $('.Hide').click(function() {
	    $('.Show').show(0);
	    $('.Hide').hide(0);
	});


    $('#TIME').clockpicker({
    	placement: 'top',
    	autoclose: true,
    	twelvehour: false,
    	donetext: 'Done'
    });
  
});
</script>

<jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
</jsp:include>

</body>
</html>