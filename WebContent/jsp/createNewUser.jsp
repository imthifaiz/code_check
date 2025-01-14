<%@ include file="header.jsp" %>
<%@ page import="com.track.gates.*"%>

<%
String title = "Create User";
%>
<%@include file="sessionCheck.jsp" %>
<jsp:include page="header2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
</jsp:include>

<script language="JavaScript" type="text/javascript" src="js/general.js"></script>

<jsp:useBean id="eb"  class="com.track.gates.encryptBean" />
<jsp:useBean id="sl" scope="session"  class="com.track.gates.selectBean" />

 <SCRIPT >
function onView(){
    document.form.action = "addNewUser.jsp?action=View";
    document.form.submit();
}

function onClear(){
 
  document.form.USER_ID.value="";
  document.form.PASSWORD.value="";
  document.form.CPASSWORD.value="";
  document.form.USER_NAME.value="";
  document.form.DESGINATION.value="";
  document.form.TELNO.value="";
  document.form.HPNO.value="";
  document.form.FAX.value="";
  document.form.EMAIL.value="";
   
  document.form.RANK.value="";
  document.form.REMARKS.value="";
  document.form.EFFECTIVE_DATE.value="";
  document.form.DEPT.selectedIndex=0
  document.form.USER_LEVEL_PAYROLL.selectedIndex=0
  document.form.USER_LEVEL_ACCOUNTING.selectedIndex=0
  document.form.USER_LEVEL.selectedIndex=0
  return true;
}
function getSelCompany(company)
{
	 var cmp = company.value;
	 form.USER_LEVEL.options.length=0;
	 form.USER_LEVEL.options[0]= new Option('DefaultGroup','DefaultGroup');
	 
	 form.USER_LEVEL_ACCOUNTING.options.length=0;
	 form.USER_LEVEL_ACCOUNTING.options[0]= new Option('DefaultGroup','DefaultGroup');
	 
	 form.USER_LEVEL_PAYROLL.options.length=0;
	 form.USER_LEVEL_PAYROLL.options[0]= new Option('DefaultGroup','DefaultGroup');
 }
</SCRIPT>

<%
	StrUtils strUtils = new StrUtils();
	ArrayList invQryList  = new ArrayList();
	userBean _userBean      = new userBean();
	String action="",USER_ID="",PASSWORD="",CPASSWORD="",USER_NAME="",DESGINATION="",TELNO="",HPNO="",FAX="",EMAIL="";
	String RANK="",REMARKS="",EFFECTIVE_DATE="",DEPT="",USER_LEVEL="",COMPANY="",selcmpy="",USER_LEVEL_ACCOUNTING="",USER_LEVEL_PAYROLL="";
	String OWNERAPP = "0", MANAGERAPP = "0", STOREAPP = "0",RIDEAPP = "0", WEBACCESS = "0",isAccess="",ISADMIN="0",ISPURCHASEAPPROVAL="",ISSALESAPPROVAL="",ISPURCHASERETAPPROVAL="",ISSALESRETAPPROVAL="";
	String plant= ((String)session.getAttribute("PLANT")).trim();
	String NOOFUSER=StrUtils.fString((String) session.getAttribute("NOOFUSER"));
	//System.out.println("NOOFUSER"+NOOFUSER);
	String ValidNumber="";
	int novalid=_userBean.getUserCount(plant);
	if(!NOOFUSER.equalsIgnoreCase("Unlimited"))
	{
		int convl = Integer.valueOf(NOOFUSER);
		if(novalid>=convl)
		{
			ValidNumber=NOOFUSER;
		}
	}
	selcmpy=plant;
	System.out.println("Plant"+plant);
	action = strUtils.fString(request.getParameter("action"));
	if (!"".equals(StrUtils.fString(request.getParameter("ACCESS")))) {
		
		if ("W".equals(StrUtils.fString(request.getParameter("ACCESS")))) {
			WEBACCESS = "1";
			OWNERAPP = "0";
			MANAGERAPP = "0";
			STOREAPP = "0";
			RIDEAPP = "0";
		}
		else if ("O".equals(StrUtils.fString(request.getParameter("ACCESS")))) {
			WEBACCESS = "0";
			OWNERAPP = "1";
			MANAGERAPP = "0";
			STOREAPP = "0";
			RIDEAPP = "0";
		}
		else if ("M".equals(StrUtils.fString(request.getParameter("ACCESS")))) {
			WEBACCESS = "0";
			OWNERAPP = "0";
			MANAGERAPP = "1";
			STOREAPP = "0";
			RIDEAPP = "0";
		}
		else if ("S".equals(StrUtils.fString(request.getParameter("ACCESS")))) {
			WEBACCESS = "0";
			OWNERAPP = "0";
			MANAGERAPP = "0";
			STOREAPP = "1";
			RIDEAPP = "0";
		}
		else  {
			WEBACCESS = "0";
			OWNERAPP = "0";
			MANAGERAPP = "0";
			STOREAPP = "0";
			RIDEAPP = "1";
		}
	}
	if(plant.equalsIgnoreCase("track"))
	{
		plant="";
	}
	else
	{
		plant=plant;
	}
	
    if(action.equalsIgnoreCase("View")){
	
	    Hashtable ht = new Hashtable();
	    ht.put("DEPT",request.getParameter("COMPANY"));
	    ht.put("USER_ID",request.getParameter("USER_ID1"));
	    COMPANY=request.getParameter("COMPANY");
	    USER_ID=request.getParameter("USER_ID1");
	    session.setAttribute("SELECTEDUSRID",request.getParameter("USER_ID1"));
	    invQryList =_userBean.getUserListSummary(ht,plant,COMPANY,USER_ID);
	     for (int iCnt =0; iCnt<invQryList.size(); iCnt++){
		   Map lineArr = (Map) invQryList.get(iCnt);
	       USER_ID    = (String)lineArr.get("USER_ID");
	       COMPANY    = request.getParameter("COMPANY");
	       PASSWORD = eb.decrypt((String)lineArr.get("PASSWORD"));
	       CPASSWORD  = eb.decrypt((String)lineArr.get("PASSWORD"));
	       USER_NAME  = (String)lineArr.get("USER_NAME");
	       DESGINATION=(String)lineArr.get("DESGINATION");
	       TELNO=(String)lineArr.get("TELNO");
	       HPNO    = (String)lineArr.get("HPNO");
	       FAX     = (String)lineArr.get("FAX");
	       EMAIL    = (String)lineArr.get("EMAIL");
	       RANK    = (String)lineArr.get("RANK");
	       REMARKS   = (String)lineArr.get("REMARKS");
	       EFFECTIVE_DATE   = (String)lineArr.get("EFFECTIV_DATE");
	       DEPT   = (String)lineArr.get("DEPT");
	       USER_LEVEL   = (String)lineArr.get("USER_LEVEL");
	       USER_LEVEL_ACCOUNTING   = (String)lineArr.get("USER_LEVEL_ACCOUNTING");
	       USER_LEVEL_PAYROLL=(String)lineArr.get("USER_LEVEL_PAYROLL");
	       WEBACCESS = (String) lineArr.get("WEB_ACCESS");
		   OWNERAPP = (String) lineArr.get("ISACCESSOWNERAPP");
		   MANAGERAPP = (String) lineArr.get("MANAGER_APP_ACCESS");
		   STOREAPP = (String) lineArr.get("ISACCESS_STOREAPP");
		   RIDEAPP = (String) lineArr.get("RIDER_APP_ACCESS");
		   ISADMIN =(String)lineArr.get("ISADMIN");
	       ISPURCHASEAPPROVAL =(String)lineArr.get("ISPURCHASEAPPROVAL");
	       ISSALESAPPROVAL =(String)lineArr.get("ISSALESAPPROVAL");
	       ISPURCHASERETAPPROVAL =(String)lineArr.get("ISPURCHASERETAPPROVAL");
	       ISSALESRETAPPROVAL =(String)lineArr.get("ISSALESRETAPPROVAL");
	      } 
	  }
%>
<script language="JavaScript" type="text/javascript" src="js/newUser.js"></script>
<jsp:useBean id="gn"  class="com.track.gates.Generator" />

<div class="container-fluid m-t-20">
	 <div class="box"> 
            <div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title"><%=title %></h1>
		</div>
		
 <div class="box-body">
 
 <form class="form-horizontal" name="form" method="post" action="newUserSubmit.jsp" autocomplete="off"  onSubmit="return validateUser(document.form);">
 
 <div class="form-group">
      <label class="control-label col-sm-4" for="User ID">
   		 	<i class="glyphicon glyphicon-star" style="font-size: 10px; top:-6px; color: #e50000"></i>&nbsp;User ID:</label>
      <div class="col-sm-4">  
      <INPUT type="hidden" name="ValidNumber" value="<%=ValidNumber%>">        
        <INPUT  class="form-control" size="50"  MAXLENGTH=50 name="USER_ID" value="<%=USER_ID%>" onFocus="nextfield ='PASSWORD';">
      </div>
    </div>
    
    
   <div class="form-group">
      <label class="control-label col-sm-4" for="Password">
   		 	<i class="glyphicon glyphicon-star" style="font-size: 10px; top:-6px; color: #e50000"></i>&nbsp;Password:</label>
      <div class="col-sm-4">          
        <INPUT  class="form-control" type="password" size="50"    MAXLENGTH=50 value="<%=PASSWORD%>" name="PASSWORD" onFocus="nextfield ='CPASSWORD';">
      </div>
    </div> 
    
    
    <div class="form-group">
      <label class="control-label col-sm-4" for="Confirm Password">
   		 	<i class="glyphicon glyphicon-star" style="font-size: 10px; top:-6px; color: #e50000"></i>&nbsp;Confirm Password:</label>
      <div class="col-sm-4">          
        <INPUT  class="form-control" type="password" size="50"    MAXLENGTH=50 value="<%=CPASSWORD%>"  name="CPASSWORD" onFocus="nextfield ='USER_NAME';">
      </div>
    </div> 
    
  
  <div class="form-group">
      <label class="control-label col-sm-4" for="User Name">
   		 	<i class="glyphicon glyphicon-star" style="font-size: 10px; top:-6px; color: #e50000"></i>&nbsp;User Name:</label>
      <div class="col-sm-4">          
        <INPUT  class="form-control" size="50" MAXLENGTH=25 name="USER_NAME" value="<%=USER_NAME%>"  onFocus="nextfield ='RANK';">
      </div>
    </div> 
  
  
  <div class="form-group">
      <label class="control-label col-sm-4" for="Designation">Designation:</label>
      <div class="col-sm-4">          
        <INPUT  class="form-control" size="50" MAXLENGTH=40 name="DESGINATION" value="<%=DESGINATION%>"  onFocus="nextfield ='DESGINATION';">
      </div>
    </div> 
    
    <div class="form-group">
      <label class="control-label col-sm-4" for="Telephone">Telephone:</label>
      <div class="col-sm-4">          
        <INPUT  class="form-control" size="50" MAXLENGTH=40 id="TELNO" name="TELNO" value="<%=TELNO%>" onkeypress="return validateInput(event)"  onFocus="nextfield ='TELNO';">
      </div>
    </div> 
    
    
    <div class="form-group">
      <label class="control-label col-sm-4" for="HandPhone">HandPhone:</label>
      <div class="col-sm-4">          
        <INPUT  class="form-control" size="50" MAXLENGTH=40 id="HPNO" name="HPNO"  value="<%=HPNO%>" onkeypress="return validateInput(event)"  onFocus="nextfield ='HPNO';">
      </div>
    </div> 
  
    
    
    <div class="form-group">
      <label class="control-label col-sm-4" for="Fax">Fax:</label>
      <div class="col-sm-4">          
        <INPUT  class="form-control" size="50" MAXLENGTH=40 id="FAX" name="FAX" value="<%=FAX%>" onkeypress="return validateInput(event)"  onFocus="nextfield ='FAX';">
      </div>
    </div> 
    
    
     <div class="form-group">
      <label class="control-label col-sm-4" for="Email">Email:</label>
      <div class="col-sm-4">          
        <INPUT  class="form-control" size="50" MAXLENGTH=40 name="EMAIL" value="<%=EMAIL%>"  onFocus="nextfield ='Email';">
      </div>
    </div> 
    
    <div class="form-group">
      <label class="control-label col-sm-4" for="Company">Company:</label>
      <div class="col-sm-4">          
        <INPUT  class="form-control" size="50" MAXLENGTH=40 readonly name="DEPT" value="<%=plant%>">
      </div>
    </div> 
    
   <div class="form-group">
      <label class="control-label col-sm-4" for="User Level">
      <i class="glyphicon glyphicon-star" style="font-size: 10px; top:-6px; color: #e50000"></i>&nbsp;User Level Inventory:</label>
      <div class="col-sm-4">
      <div class="input-group">          
        <SELECT class="btn btn-default dropdown-toggle" data-toggle="dropdown" data-placement="right"  NAME ="USER_LEVEL" size="1" >
                         <OPTION selected value='0'>< -- Choose -- > </OPTION> 
                      <%
                      if(selcmpy.equalsIgnoreCase("track")){%>    <%=sl.getMutiUserLevels("0","INVENTORY")%> <%}else {%>
                      <%=sl.getMutiUserLevels("1",plant,"INVENTORY")%>
                      <%if(!USER_LEVEL.equals("")) { %>
                     <OPTION selected value="<%=USER_LEVEL%>"><%=USER_LEVEL%></OPTION>
                     <%}}%>
                        </SELECT>
                        
      </div>
    </div> 
    </div>
    
    <div class="form-group">
      <label class="control-label col-sm-4" for="User Level">
      <i class="glyphicon glyphicon-star" style="font-size: 10px; top:-6px; color: #e50000"></i>&nbsp;User Level Accounting:</label>
      <div class="col-sm-4">
      <div class="input-group">          
        <SELECT class="btn btn-default dropdown-toggle" data-toggle="dropdown" data-placement="right"  NAME ="USER_LEVEL_ACCOUNTING" size="1" >
                         <OPTION selected value='0'>< -- Choose -- > </OPTION> 
                      <%
                      if(selcmpy.equalsIgnoreCase("track")){%>    <%=sl.getMutiUserLevels("0","ACCOUNTING")%> <%}else {%>
                      <%=sl.getMutiUserLevels("1",plant,"ACCOUNTING")%>
                      <%if(!USER_LEVEL_ACCOUNTING.equals("")) { %>
                     <OPTION selected value="<%=USER_LEVEL_ACCOUNTING%>"><%=USER_LEVEL_ACCOUNTING%></OPTION>
                     <%}}%>
                        </SELECT>
                        
      </div>
    </div> 
    </div>
    
    <div class="form-group">
      <label class="control-label col-sm-4" for="User Level">
      <i class="glyphicon glyphicon-star" style="font-size: 10px; top:-6px; color: #e50000"></i>&nbsp;User Level Payroll:</label>
      <div class="col-sm-4">
      <div class="input-group">          
        <SELECT class="btn btn-default dropdown-toggle" data-toggle="dropdown" data-placement="right"  NAME ="USER_LEVEL_PAYROLL" size="1" >
                         <OPTION selected value='0'>< -- Choose -- > </OPTION> 
                      <%
                      if(selcmpy.equalsIgnoreCase("track")){%>    <%=sl.getMutiUserLevels("0","PAYROLL")%> <%}else {%>
                      <%=sl.getMutiUserLevels("1",plant,"PAYROLL")%>
                      <%if(!USER_LEVEL_PAYROLL.equals("")) { %>
                     <OPTION selected value="<%=USER_LEVEL_PAYROLL%>"><%=USER_LEVEL_PAYROLL%></OPTION>
                     <%}}%>
                        </SELECT>
                        
      </div>
    </div> 
    </div>
    
    <!--   <TR>
              <TH WIDTH="35%" ALIGN="RIGHT" >*Company :&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
               <TD>
                   <SELECT NAME ="DEPT" size="1" onChange="return getSelCompany(this);">
                   <OPTION selected value="0">< -- Choose  Company-- > </OPTION>
                         <%=sl.getPlantNames(plant)%>  
                         <%if(!COMPANY.equals("")) { %>
                   <OPTION selected value="<%=COMPANY%>"><%=COMPANY%></OPTION>
                     <%}%>
                     </SELECT>
                 </TD>-->
                 
                 	<%-- <div class="form-group">
						<label class="control-label col-sm-4" for="Access System">Access System</label>
						<div class="col-sm-7">
							<label class="radio-inline">
							<input type="radio" value="W" name="ACCESS" id="WEBACCESS" <%if (isAccess.equalsIgnoreCase("W")) {%> checked <%}%> />Web&nbsp;&nbsp;
							</label>
							<label class="radio-inline">
							<input type="radio" value="O" name="ACCESS" id="OWNERAPP" <%if (isAccess.equalsIgnoreCase("O")) {%> checked <%}%> />Owner App&nbsp;&nbsp; 
							</label>
							<label class="radio-inline">
							<input type="radio" value="M" name="ACCESS" id="MANAGERAPP" <%if (isAccess.equalsIgnoreCase("M")) {%> checked <%}%> /> Manager App&nbsp;&nbsp; 
							</label>
							<label class="radio-inline">
							<input type="radio" value="S" name="ACCESS" id="STOREAPP" <%if (isAccess.equalsIgnoreCase("S")) {%> checked <%}%> /> Store App&nbsp;&nbsp; 
							</label>
							<label class="radio-inline">
							<input type="radio" value="R" name="ACCESS" id="RIDEAPP" <%if (isAccess.equalsIgnoreCase("R")) {%> checked <%}%> /> Rider App
							</label>
						</div>
					</div> --%>
					
					<div class="form-group">
			<label class="control-label col-form-label col-sm-4" for="Access System">Access System</label>
			<div class="col-sm-6">
						<label class="control-label col-form-label" class="col-sm-4">
							<input type="radio" value="W" name="ACCESS" id="WEBACCESS" <%if (isAccess.equalsIgnoreCase("W")) {%> checked <%}%> />Web&nbsp;&nbsp;&nbsp;&nbsp;
							</label>
							<label class="control-label col-form-label" class="col-sm-4">
							<input type="radio" value="O" name="ACCESS" id="OWNERAPP" <%if (isAccess.equalsIgnoreCase("O")) {%> checked <%}%> />Owner App&nbsp;&nbsp;&nbsp;&nbsp;
							</label>
							<label class="control-label col-form-label" class="col-sm-4">
							<input type="radio" value="M" name="ACCESS" id="MANAGERAPP" <%if (isAccess.equalsIgnoreCase("M")) {%> checked <%}%> /> Manager App&nbsp;&nbsp;&nbsp;&nbsp;
							</label>
						</div>
						<label class="control-label col-form-label col-sm-4" for="Effective Date"></label>
						<div class="col-sm-6">
							<label class="control-label col-form-label" class="col-sm-4">
							<input type="radio" value="S" name="ACCESS" id="STOREAPP" <%if (isAccess.equalsIgnoreCase("S")) {%> checked <%}%> /> Store App&nbsp;&nbsp;&nbsp;&nbsp;
							</label>
							<label class="control-label col-form-label" class="col-sm-4">
							<input type="radio" value="R" name="ACCESS" id="RIDEAPP" <%if (isAccess.equalsIgnoreCase("R")) {%> checked <%}%> /> Delivery App&nbsp;&nbsp;&nbsp;&nbsp;
							</label>
			</div>
		</div>

					<div class="form-group">
						<label class="control-label col-form-label col-sm-4" for="Effective Date">Is Approval Admin</label>
						<div class="col-sm-6">
							<input type="checkbox" name="isApprovalAdmin"  id="isApprovalAdmin" <%if (ISADMIN.equalsIgnoreCase("1")) {%> checked <%}%>>
						</div>
					</div>
					<div class="form-group" id="isadd" <%if (!ISADMIN.equalsIgnoreCase("1")) {%> hidden  <%}%>>
						<label class="control-label col-form-label col-sm-4" for="Effective Date"></label>
						<div class="col-sm-6">
							<p style="font-weight: bold;">Select for Approval</p>
							<div class="col-sm-1">
								<input type="checkbox" name="isPurchaseApproval" id="isPurchaseApproval" <%if (ISPURCHASEAPPROVAL.equalsIgnoreCase("1")) {%> checked <%}%>>
							</div>
							<div class="col-sm-3">
								Purchase
							</div>
							<div class="col-sm-1">
								<input type="checkbox" name="isPurchaseRetApproval" id="isPurchaseRetApproval" <%if (ISPURCHASERETAPPROVAL.equalsIgnoreCase("1")) {%> checked <%}%>>
							</div>
							<div class="col-sm-7">
								Purchase Return
							</div>
						</div>
						<label class="control-label col-form-label col-sm-4" for="Effective Date"></label>
						<div class="col-sm-6">
							<div class="col-sm-1">
								<input type="checkbox" name="isSalesApproval" id="isSalesApproval" <%if (ISSALESAPPROVAL.equalsIgnoreCase("1")) {%> checked <%}%>>
							</div>
							<div class="col-sm-3">
								Sales
							</div>
							<div class="col-sm-1">
								<input type="checkbox" name="isSalesRetApproval" id="isSalesRetApproval" <%if (ISSALESRETAPPROVAL.equalsIgnoreCase("1")) {%> checked <%}%>>
							</div>
							<div class="col-sm-7">
								Sales Return
							</div>
						</div>
					</div>
                 
    
    <div class="form-group">
      <label class="control-label col-sm-4" for="Effective Date">
   		 	<i class="glyphicon glyphicon-star" style="font-size: 10px; top:-6px; color: #e50000"></i>&nbsp;Effective Date:</label>
      <div class="col-sm-4">          
        <INPUT  class="form-control" size="50" MAXLENGTH=10 name="EFFECTIVE_DATE" value="<%=gn.getDate()%>">
      </div>
    </div> 
    
    
    <div class="form-group">
      <label class="control-label col-sm-4" for="Remarks">Remarks:</label>
      <div class="col-sm-4">          
        <INPUT  class="form-control" size="50" MAXLENGTH=60 name="REMARKS" value="<%=REMARKS%>"  onFocus="nextfield ='EFFECTIVE_DATE';">
        <!-- <TH WIDTH="35%" ALIGN="RIGHT" >Job Title :&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; -->
      </div>
    </div>
    <INPUT type="hidden" size="50" MAXLENGTH=40 name="RANK" value=""  onFocus="nextfield ='REMARKS';">
    <INPUT name="COMPANY"  type ="hidden" value="<%=COMPANY%>" size="1"   MAXLENGTH=80 >
    <INPUT type="Hidden" name="ENCRYPT_FLAG" value="1">
     <INPUT type="hidden" name="ACCESS_COUNTER" id="ACCESS_COUNTER" value="0">
      <INPUT type="hidden" name="USER_STATUS" id="USER_STATUS" value="1">
      <INPUT type="hidden" name="ENROLLED_BY" id="ENROLLED_BY" value="">
      <INPUT type="hidden" name="ENROLLED_ON" id="ENROLLED_ON" value="">
    
     <!--  <input type="Submit" value=" Update " name="Submit">
                    <input type="Submit" value=" Delete " name="Submit"> -->
                    
 <div class="form-group">        
      <div class="col-sm-offset-4 col-sm-8">
      
      	<button type="button" class="Submit btn btn-default" value="Back" onClick="window.location.href='settings.jsp';"><b>Back</b></button>&nbsp;&nbsp;
      	<button type="button" class="Submit btn btn-default" value="Clear" onClick="return onClear();"><b>Clear</b></button>&nbsp;&nbsp;
      	<button type="Submit" class="Submit btn btn-default" value=" Save " name="Submit"><b>Save</b></button>&nbsp;&nbsp;
      	

      </div>
    </div>            
       </form>
       </div>
       </div>
       </div>
                    

<script>
$(document).ready(function(){
	 $('[data-toggle="tooltip"]').tooltip();  

	 $('#isApprovalAdmin').change(function() {
	     if ($(this).is(':checked')) {
	 		$('#isadd').show();
	     } else {
	         $('#isPurchaseApproval').prop('checked',false);
	         $('#isSalesApproval').prop('checked',false);
	 		$('#isPurchaseRetApproval').prop('checked',false);
	 		$('#isSalesRetApproval').prop('checked',false);
	 		$('#isadd').hide();
	     }
	 });  
});
</script>


 <jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
</jsp:include>