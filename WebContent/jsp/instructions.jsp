<%@ page import="com.track.util.*"%>
<%@ page import="com.track.db.util.*"%>
<%@ page import="com.track.gates.*"%>
<%@ page import="com.track.constants.*"%>
<%@ include file="header.jsp"%>

<%
String title = "Instructions Display";
%>
<%@include file="sessionCheck.jsp" %>
<jsp:include page="header2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
</jsp:include>

<script language="JavaScript" type="text/javascript" src="js/general.js"></script>

<SCRIPT LANGUAGE="JavaScript">
	
	function onClear()
	{
		document.form.Step1.value="";
		document.form.Step2.value="";
		document.form.Step3.value="";
		document.form.Step4.value="";
		document.form.Step5.value="";
	}

	function onAdd(){
		
		
		
		if (form.Step1.value.length > 100)
	     {
	    	alert("Step1 should not exceed 100 characters !");
	    	form.Step1.focus();
	    	return false;
	     }
		if (form.Step2.value.length > 100)
	     {
	    	alert("Step2 should not exceed 100 characters !");
	    	form.Step2.focus();
	    	return false;
	     }
		if (form.Step3.value.length > 100)
	     {
	    	alert("Step3 should not exceed 100 characters !");
	    	form.Step3.focus();
	    	return false;
	     }
		if (form.Step4.value.length > 100)
	     {
	    	alert("Step4 should not exceed 100 characters !");
	    	form.Step4.focus();
	    	return false;
	     }
		
		if (form.Step5.value.length > 100)
	     {
	    	alert("Step5 should not exceed 100 characters !");
	    	form.Step5.focus();
	    	return false;
	     }
		else
	    {
   			document.form.action  =  "/track/instructions?Submit=CREATE_INSTRN";
   		   	document.form.submit();
   		   	return true;
	    }
   	
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
        
	String res = "",sNewEnb="enabled";
	
	String action = "";
	
	String RefNo   = "", Terms  = "", TermsDetails   = "",SoNo ="",Item ="";
	String Description = "", OrderQty  = "",
      	   TotalTax="",Total="";
	StringBuffer step1,step2,step3,step4,step5; 
	StrUtils strUtils = new StrUtils();
	step1 = new StringBuffer();
	step2 = new StringBuffer();
	step3 = new StringBuffer();
	step4 = new StringBuffer();
	step5 = new StringBuffer();
    res =  strUtils.fString(request.getParameter("result"));
	InstructionUtil insUtil = new InstructionUtil();
	Hashtable ht = new Hashtable();
	ht.put(IConstants.PLANT,plant);
         List m= insUtil.listInstrns("isnull(step1,'') step1,isnull(step2,'') step2,isnull(step3,'') step3,isnull(step4,'') step4,isnull(step5,'') step5",ht,"");
         
         for(int i=0;i<m.size();i++)
         {
        	Map insmp= (Map)m.get(i);
         step1.append(insmp.get("step1"));
         step2.append(insmp.get("step2"));
         step3.append(insmp.get("step3"));
         step4.append(insmp.get("step4"));
         step5.append(insmp.get("step5"));
         
         }    
      

	try {
		action = strUtils.fString(request.getParameter("action"));
	} catch (Exception e) {
	}

	if (action.equalsIgnoreCase("Clear")) {
		action = "";
	}  
%>

<div class="container-fluid m-t-20">
	 <div class="box"> 
            <div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title"><%=title %></h1>
		</div>
		
 <div class="box-body">
 
   <CENTER><strong><%=res%></strong></CENTER>

  <form class="form-horizontal" name="form" method="post">
  
  
    <div class="form-group">
      <label class="control-label col-sm-4" for="Discription">Step1:</label>
      <div class="col-sm-4">
      	<div class="input-group">
      	   
    <textarea class="form-control" name="Step1"  id="Step1" cols="60" rows="2" wrap="hard" onKeyDown="limitText(this.form.Step1,100);"><%=step1.toString()%></textarea>
		
		  		</div>
  		
      </div>
    </div>
    <div class="form-group">
      <label class="control-label col-sm-4" for="Description">Step2:</label>
      <div class="col-sm-4">  
              <div class="input-group"> 
      <textarea class="form-control" name="Step2"  cols="60" rows="2" wrap="hard" onKeyDown="limitText(this.form.Step2,100);" ><%=step2.toString()%></textarea>
			</div>
      </div>
      </div>
      <div class="form-group">
      <label class="control-label col-sm-4" for="Description">Step3:</label>
      <div class="col-sm-4">  
              <div class="input-group"> 
      <textarea class="form-control" name="Step3"  cols="60" rows="2" wrap="hard" onKeyDown="limitText(this.form.Step3,100);"><%=step3.toString()%></textarea>
			</div>
      </div>
      </div>
      <div class="form-group">
      <label class="control-label col-sm-4" for="Description">Step4:</label>
      <div class="col-sm-4">  
              <div class="input-group"> 
      <textarea  class="form-control" name="Step4"  cols="60" rows="2" wrap="hard" onKeyDown="limitText(this.form.Step4,100);"><%=step4.toString()%></textarea>
			</div>
      </div>
      </div>
 <div class="form-group">
      <label class="control-label col-sm-4" for="Description">Step5:</label>
      <div class="col-sm-4">  
              <div class="input-group"> 
      <textarea  class="form-control" name="Step5"  cols="60" rows="2" wrap="hard" onKeyDown="limitText(this.form.Step5,100);"><%=step5.toString()%></textarea>
			</div>
      </div>
      </div>
    <div class="form-group">        
      <div class="col-sm-offset-4 col-sm-8">
        <button type="button" class="Submit btn btn-default" onClick="window.location.href='../home'"><b>Back</b></button>&nbsp;&nbsp;
       	<!-- <button type="button" class="Submit btn btn-default" onClick="{backNavigation('settings.jsp','SM');}"><b>Back</b></button>&nbsp;&nbsp; -->
      	<button type="button" class="Submit btn btn-default" onClick="onClear();" <%=sNewEnb%>><b>Clear</b></button>&nbsp;&nbsp;
      	<button type="button" class="Submit btn btn-default" onClick="onAdd();" ><b>Save</b></button>&nbsp;&nbsp;
      	      </div>
    </div>
  </form>
</div>
</div>
</div>


<script>
$(document).ready(function(){
    $('[data-toggle="tooltip"]').tooltip();   
});
</script>

<jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
</jsp:include>

