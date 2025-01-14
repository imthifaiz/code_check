<%@ include file="header.jsp" %>
<%@page import="com.track.constants.IConstants"%>
<%
String title = "Payroll User Access Rights Details";
%>
<%@include file="sessionCheck.jsp" %>
<jsp:include page="header2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
	<jsp:param name="mainmenu" value="<%=IConstants.SETTINGS %>" />
	<jsp:param name="submenu" value="<%=IConstants.USER_ADMIN %>" />
</jsp:include>
<link rel="stylesheet" href="../jsp/css/typeahead.css">
<link rel="stylesheet" href="../jsp/css/accounting.css">
<script language="JavaScript" type="text/javascript" src="../jsp/js/userLevel.js"></script>
<script language="JavaScript" type="text/javascript" src="../jsp/js/general.js"></script>

<%--<style>
.table1c tr td{padding: 30px}
</style> --%>


<jsp:useBean id="sl"  class="com.track.gates.selectBean" />
<jsp:useBean id="ub"  class="com.track.gates.userBean" />


<div class="container-fluid m-t-20">
	 <div class="box"> 
	 <!-- Thanzith Modified on 28.02.2022 -->
	 <ul class="breadcrumb backpageul">    
                <li class="underline-on-hover"><a href="../home">Dashboard</a></li>	
                 <li><a href="../useraccess/paysummary"><span class="underline-on-hover">Payroll User Access Rights Summary</span></a></li>	
                <li><label>Payroll User Access Rights Details</label></li>                                   
            </ul>
        <!-- Thanzith Modified on 28.02.2022 --> 
            <div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title"><%=title %></h1>
              <h1 style="font-size: 18px; cursor: pointer;"
				class="box-title pull-right"
				onclick="window.location.href='../useraccess/paysummary'">
				<i class="glyphicon glyphicon-remove"></i>
			</h1>
		</div>
		
 <div class="box-body">
<%! ArrayList al;
//          Method for selecting the CheckBox as 'Checked'
       public String checkOption(String str)
                {
                    for(int i=0; i< al.size();i++)
                    {
                    if(str.equalsIgnoreCase(al.get(i).toString())) return "checked";
                    }
                    return "";
                }
%>
<%
ub.setmLogger(mLogger);
sl.setmLogger(mLogger);
        String caption = "Maintain";
        String delParam="";
        String disabled="";
        String disabledInView="";
     String plant = ((String)session.getAttribute("PLANT")).trim();
     String plant1 = plant;
     if(plant.equalsIgnoreCase("track"))
    {
   plant="";
    }
    else
    {
     plant=plant+"_";
    }
        Enumeration e = request.getParameterNames();
        while(e.hasMoreElements())
        {
            String s = e.nextElement().toString();

            if(s.equalsIgnoreCase("view"))
                {
                     caption = "View";
                     disabledInView = "disabled";
                }

            else if(s.equalsIgnoreCase("del"))
                {
                     caption = "Delete";
                     delParam = "<input type=\"Hidden\" name=\"del\" value=\"\">"; //  To Indicate the delete function
                     disabled = "disabled";
                }
        }
%>
<script>
function onUpdate()
{
document.form.action="../jsp/maintUserLevel.jsp?action=update";
document.form.submit();
}
</script>


<form name="form" class="form-horizontal" method="POST" action="../jsp/maintUserLevel.jsp">
<br>
<%=delParam%>
          
         <%
		String level_name = request.getParameter("LEVEL_NAME");

		/*if (level_name.length() < 1) {
		out
		.write("<br><table width=\"100%\"><tr><td align=\"center\"><b>Please Select a GROUP</b></td></tr></table></FORM>");
		} else {*/
		if (level_name.length() > 1) {
		al = ub.getUserLevelLinkspayroll(level_name, plant);
		String authorise_by = al.get(al.size() - 2).toString();
		if ((authorise_by == null) || (authorise_by.length() <= 1))
		authorise_by = "Not Authorised";
	%>
        

</FORM>

<form class="form-horizontal" method="POST" action="../jsp/userLevelSubmit.jsp">
<INPUT type="Hidden" name="LEVEL_NAME" value="<%=level_name%>">
<P align="center"><font face="Verdana" color="black" size="2" ><b>GROUPS</b></font><font face="Verdana" color="black" size="2"><b>
                    - <%=new String(level_name).toUpperCase()%></b></font>

<div class="panel panel-default">
    <div class="panel-heading" style="background: #eaeafa"><strong>Dashboard</strong></div>
    <div class="panel-body">
    <TABLE class="table1">
                <TR>
                 <TH WIDTH="20%" ALIGN = "LEFT">
                        <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="UserDasnboard" value="homePayroll" <%=disabled%> <%=checkOption("homePayroll")%>>
                        &nbsp; Payroll</label>   
                  
      
                          
                      </TABLE>
         </div>
  </div>
  
 
 <div class="panel panel-default">
    <div class="panel-heading" style="background: #eaeafa"><strong>User Admin</strong></div>
    <div class="panel-body">
    <TABLE class="table1" style="font-size:14px;width: 100%;">
                    <TR>
             			<TH WIDTH="20%" ALIGN = "LEFT">
                        <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="UserAdmin" value="customerMaintCpny" <%=disabled%> <%=checkOption("customerMaintCpny")%>>
                        &nbsp;  Edit Your Company Profile</label>

                     <TH WIDTH="20%" ALIGN = "LEFT">
                        <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="UserAdmin" value="uaMaintLevel" <%=disabled%> <%=checkOption("uaMaintLevel")%>>
                        &nbsp; User Details</label>
                        
                      <TH WIDTH="20%" ALIGN = "LEFT">
                     <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="UserAdmin" value="summarylnkuaMaintLevel" <%=disabled%> <%=checkOption("summarylnkuaMaintLevel")%>>
                       &nbsp; Summary &#45; User Details Link</label>
                       
                       <TH WIDTH="20%" ALIGN = "LEFT">
                     <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="UserAdmin" value="edituaMaintLevel" <%=disabled%> <%=checkOption("edituaMaintLevel")%>>
                       &nbsp; Edit User Details</label>
                     
                     <TR>
                        
                      <TH WIDTH="20%" ALIGN = "LEFT">
                        <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="UserAdmin" value="uaChngPwd" <%=disabled%> <%=checkOption("uaChngPwd")%>>
                        &nbsp; Edit Password Details</label>  
                        
                       <TH WIDTH="20%" ALIGN = "LEFT">
                        <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="UserAdmin" value="uaMaintAcct" <%=disabled%> <%=checkOption("uaMaintAcct")%>>
                        &nbsp;  User Access Rights</label> 
                        
                    	<TH WIDTH="20%" ALIGN = "LEFT">
                          <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="UserAdmin" value="newuaMaintAcct"  <%=disabled%> <%=checkOption("newuaMaintAcct")%>>
                        &nbsp; New User Access Rights</label>
                        
                        <TH WIDTH="20%" ALIGN = "LEFT">
                          <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="UserAdmin" value="summarylnkuaMaintAcct"  <%=disabled%> <%=checkOption("summarylnkuaMaintAcct")%>>
                        &nbsp; Summary &#45; User Access Rights Link</label>
                        
                        <TR>
                        
                        <TH WIDTH="20%" ALIGN = "LEFT">
                          <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="UserAdmin" value="edituaMaintAcct"  <%=disabled%> <%=checkOption("edituaMaintAcct")%>>
                        &nbsp; Edit User Access Rights</label>
                       
                      </TABLE>
         </div>
  </div>

 <div class="panel panel-default">
    <div class="panel-heading" style="background: #eaeafa"><strong>Employee</strong></div>
    <div class="panel-body">
    <TABLE class="table1" style="font-size:14px;width: 100%;">
                  
                     <TR>
                       <TH WIDTH="20%" ALIGN = "LEFT">
                        <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="UserEmployee" value="employeesummry" <%=disabled%> <%=checkOption("employeesummry")%>>
                        &nbsp; Employee</label>
                                              
                    <TH WIDTH="20%" ALIGN = "LEFT">
                        <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="UserEmployee" value="pnewemployee" <%=disabled%> <%=checkOption("pnewemployee")%>>
                        &nbsp; New</label>
                        
                    <TH WIDTH="20%" ALIGN = "LEFT">
	                 <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="UserEmployee" value="psummarylnkemployee" <%=disabled%> <%=checkOption("psummarylnkemployee")%> >
	                 &nbsp; Summary &#45; Employee Link</label>
	                 
                    <TH WIDTH="20%" ALIGN = "LEFT">
                        <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="UserEmployee" value="peditemoloyee" <%=disabled%> <%=checkOption("peditemoloyee")%>>
                        &nbsp; Edit</label>
                        
             <TR>
             
                     <TH WIDTH="20%" ALIGN = "LEFT">
                        <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="UserEmployee" value="pimportemployee" <%=disabled%> <%=checkOption("pimportemployee")%>>
                        &nbsp; Import Employee</label>
                                       
                    <TH WIDTH="20%" ALIGN = "LEFT">
                    <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="UserEmployee" value="pimportemployeeleavetype"  <%=disabled%> <%=checkOption("pimportemployeeleavetype")%>>
                    &nbsp; Import Employee Leave Type</label>
                        
                     <TH WIDTH="20%" ALIGN = "LEFT">
	                 <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="UserEmployee" value="pimportemployeesalarytype"  <%=disabled%> <%=checkOption("pimportemployeesalarytype")%>>
	                 &nbsp; Import Employee Salary Type</label> 
	                 </TH>
	                        
	                 <TH WIDTH="20%" ALIGN = "LEFT">
	                 <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="UserEmployee" value="pexportmasterdata" <%=disabled%> <%=checkOption("pexportmasterdata")%>>
	                 &nbsp;  Export Master Data</label>
	                 </TH>
	                 
	                 <TR>
					 
					 <TH WIDTH="20%" ALIGN = "LEFT">
	                 <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="UserEmployee" value="pexportleavedetails" <%=disabled%> <%=checkOption("pexportleavedetails")%>>
	                 &nbsp; Export Leave Details</label>
                     </TH>
                                       
                    <TH WIDTH="20%" ALIGN = "LEFT">
                    <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="UserEmployee" value="pexportsalarydetails" <%=disabled%> <%=checkOption("pexportsalarydetails")%>>
                    &nbsp; Export Salary Details</label>
                     

                      </TABLE>
         </div>
  </div>
  

   <div class="panel panel-default">
    <div class="panel-heading" style="background: #eaeafa"><strong>Employee Type</strong></div>
    <div class="panel-body">
    	<TABLE class="table1" style="font-size:14px;width: 100%;">
             <TR>
                  <TH WIDTH="20%" ALIGN = "LEFT">
                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="userEmployeeType" value="EmployeeTypeSummary"  <%=disabled%> <%=checkOption("EmployeeTypeSummary")%>>
                  &nbsp; Employee Type</label>
                  
                     <TH WIDTH="20%" ALIGN = "LEFT">
                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="userEmployeeType" value="Pnewemployeetype"  <%=disabled%> <%=checkOption("Pnewemployeetype")%>>
                  &nbsp; New</label>
                  
                  <TH WIDTH="20%" ALIGN = "LEFT">
                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="userEmployeeType" value="peditemployeetype"  <%=disabled%> <%=checkOption("peditemployeetype")%>>
                  &nbsp;  Edit</label> 
                  
                  <TH WIDTH="20%" ALIGN = "LEFT">
                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="userEmployeeType" value="pdeleteemployeetype" <%=disabled%> <%=checkOption("pdeleteemployeetype")%>>
                  &nbsp; Delete</label>
                  
                  <TR>
                  
                  <TH WIDTH="20%" ALIGN = "LEFT">
                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="userEmployeeType" value="pexportemployeetype" <%=disabled%> <%=checkOption("pexportemployeetype")%>>
                  &nbsp; Export</label>
               
	         </TABLE>
    </div>
  </div>

<div class="panel panel-default">
    <div class="panel-heading" style="background: #eaeafa"><strong>Department</strong></div>
    <div class="panel-body">
    <TABLE class="table1" style="font-size:14px;width: 100%;">
             <TR>                 
                  <TH WIDTH="20%" ALIGN = "LEFT">
                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="pdepartment" value="DepartmentSummary"  <%=disabled%> <%=checkOption("DepartmentSummary")%>>
                  &nbsp;  Department</label>
                  
                   <TH WIDTH="20%" ALIGN = "LEFT">
                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="pdepartment" value="newDepartment" <%=disabled%> <%=checkOption("newDepartment")%> >
                  &nbsp; New</label>
                  
                  <TH WIDTH="20%" ALIGN = "LEFT">
                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="pdepartment" value="editDepartment" <%=disabled%> <%=checkOption("editDepartment")%> >
                  &nbsp; Edit</label>
                  
                  <TH WIDTH="20%" ALIGN = "LEFT">
                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="pdepartment" value="exportDepartment" <%=disabled%> <%=checkOption("exportDepartment")%> >
                  &nbsp; Export</label>
                  
                                
        	 </TABLE>
    </div>
    </div>

   <div class="panel panel-default">
    <div class="panel-heading" style="background: #eaeafa"><strong>Salary Type</strong></div>
    <div class="panel-body">
    <TABLE class="table1" style="font-size:14px;width: 100%;">
             <TR>                 
                  <TH WIDTH="20%" ALIGN = "LEFT">
                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="psalarytype" value="EmployeeSalarySummary"  <%=disabled%> <%=checkOption("EmployeeSalarySummary")%>>
                  &nbsp;  Salary Type</label>
                  
                   <TH WIDTH="20%" ALIGN = "LEFT">
                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="psalarytype" value="pnewsalarytype " <%=disabled%> <%=checkOption("pnewsalarytype")%> >
                  &nbsp; New</label>
                  
                  <TH WIDTH="20%" ALIGN = "LEFT">
                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="psalarytype" value="peditsalarytype" <%=disabled%> <%=checkOption("peditsalarytype")%> >
                  &nbsp; Edit</label>
                  
                  <TH WIDTH="20%" ALIGN = "LEFT">
                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="psalarytype" value="pexportsalarytype" <%=disabled%> <%=checkOption("pexportsalarytype")%> >
                  &nbsp; Export</label>
                                
        	 </TABLE>
    </div>
    </div>
    
<div class="panel panel-default">
    <div class="panel-heading" style="background: #eaeafa"><strong>Leave Type</strong></div>
    <div class="panel-body"> 
		<TABLE class="table1" style="font-size:14px;width: 100%;">
	             <TR>                 
       	                      
                   <TH WIDTH="20%" ALIGN = "LEFT">
                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="pLeavetype" value="LeaveTypeSummary" <%=disabled%> <%=checkOption("LeaveTypeSummary")%> >
                  &nbsp; Leave Type</label>  
                  
                         
                  <TH WIDTH="20%" ALIGN = "LEFT">
                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="pLeavetype" value="pnewleavetype" <%=disabled%> <%=checkOption("pnewleavetype")%> >
                  &nbsp; New</label>
  
                  <TH WIDTH="20%" ALIGN = "LEFT">
                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="pLeavetype" value="peditlevaetype" <%=disabled%> <%=checkOption("peditlevaetype")%> >
                  &nbsp; Edit</label>  
                  
                  
                  <TH WIDTH="20%" ALIGN = "LEFT">
                  <label class="checkbox-inline">    <INPUT Type=Checkbox  style="border:0;" name="pLeavetype" value="pdeleteleavetype" <%=disabled%> <%=checkOption("pdeleteleavetype")%> >
                      &nbsp; Delete</label>
                      
                  <TR>    
                  
                  <TH WIDTH="20%" ALIGN = "LEFT">
                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="pLeavetype" value="pexportleavetype" <%=disabled%> <%=checkOption("pexportleavetype")%> >
                  &nbsp; Export</label>
               
                
              </TABLE>
	</div>
</div>    

  

<div class="panel panel-default">
    <div class="panel-heading" style="background: #eaeafa"><strong>Holiday</strong></div>
    <div class="panel-body"> 
    	<TABLE class="table1" style="font-size:14px;width: 100%;">
	             <TR>     
	              <TH WIDTH="20%" ALIGN = "LEFT">
                    <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="pholidaytype" value="HolidayMstSummary" <%=disabled%> <%=checkOption("HolidayMstSummary")%>  >
                    &nbsp; Holiday Type</label>            
                 
       				 <TH WIDTH="23%" ALIGN = "LEFT">
                    <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="pholidaytype" value="pnewholiday" <%=disabled%> <%=checkOption("pnewholiday")%> >
                    &nbsp; New</label>
                    
                    <TH WIDTH="23%" ALIGN = "LEFT">
                     <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="pholidaytype" value="peditholiday" <%=disabled%> <%=checkOption("peditholiday")%>  >
                     &nbsp; Edit</label>
                     
                     <TH WIDTH="23%" ALIGN = "LEFT">
                    <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="pholidaytype" value="pdeleteholiday" <%=disabled%> <%=checkOption("pdeleteholiday")%> >
                    &nbsp; Delete</label>
                  <TR>   
                    <TH WIDTH="23%" ALIGN = "LEFT">
                    <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="pholidaytype" value="pimportholiday"  <%=disabled%> <%=checkOption("pimportholiday")%> >
                    &nbsp;  Import</label>
                    
                    <TH WIDTH="20%" ALIGN = "LEFT">
                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="pholidaytype" value="pexportholiday" <%=disabled%> <%=checkOption("pexportholiday")%> >
                  &nbsp; Export</label>
                   
                  
                               
                </TABLE>
    </div>
</div>

<div class="panel panel-default">
<div class="panel-heading" style="background: #eaeafa"><strong>Banking</strong></div>
<div class="panel-body"> 
		<TABLE class="table1" style="font-size:14px;width: 100%;">
        	 <TR>
        	 
        	 <TH WIDTH="20%" ALIGN = "LEFT">
              <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;" name="Banking" value="BankSummary" <%=disabled%> <%=checkOption("BankSummary")%>>
              &nbsp; Banking</label>
              
             <TH WIDTH="20%" ALIGN = "LEFT">
	          <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="Banking" value="newbank"  <%=disabled%> <%=checkOption("newbank")%> >     
	          &nbsp; New</label>
	          
	         <TH WIDTH="20%" ALIGN = "LEFT">
	          <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="Banking" value="editbank"  <%=disabled%> <%=checkOption("editbank")%> >     
	          &nbsp; Edit</label>
	          
	         <TH WIDTH="20%" ALIGN = "LEFT">
	          <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="Banking" value="exportbank"  <%=disabled%> <%=checkOption("exportbank")%> >     
	          &nbsp; Export</label>
	          
	          <TR>
	          
	         <TH WIDTH="20%" ALIGN = "LEFT">
	          <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="Banking" value="deletebank"  <%=disabled%> <%=checkOption("deletebank")%> >     
	          &nbsp; Delete</label>   
                      
        </TABLE>
  </div>
</div>

<!-- Resvi  adds -->

<div class="panel panel-default">
<div class="panel-heading" style="background: #eaeafa"><strong>Shift</strong></div>
<div class="panel-body"> 
		<TABLE class="table1" style="font-size:14px;width: 100%;">
        	 <TR>
        	 
        	 <TH WIDTH="20%" ALIGN = "LEFT">
              <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;" name="Shift" value="ShiftSummary" <%=disabled%> <%=checkOption("ShiftSummary")%>>
              &nbsp; Shift</label>
              
             <TH WIDTH="20%" ALIGN = "LEFT">
	          <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="Shift" value="pnewshift"  <%=disabled%> <%=checkOption("pnewshift")%> >     
	          &nbsp; New</label>
	          
	         <TH WIDTH="20%" ALIGN = "LEFT">
	          <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="Shift" value="peditshift"  <%=disabled%> <%=checkOption("peditshift")%> >     
	          &nbsp; Edit</label>
	          
	         <TH WIDTH="20%" ALIGN = "LEFT">
	          <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="Shift" value="pexportshift"  <%=disabled%> <%=checkOption("pexportshift")%> >     
	          &nbsp; Export</label>
	          
	          <TR>
	          
	         <TH WIDTH="20%" ALIGN = "LEFT">
	          <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="Shift" value="pdeleteshift"  <%=disabled%> <%=checkOption("pdeleteshift")%> >     
	          &nbsp; Delete</label>   
                      
        </TABLE>
  </div>
</div>
<!-- ends -->
 
  <div class="panel panel-default">
    <div class="panel-heading" style="background: #eaeafa"><strong>Printout Configuration</strong></div>
    <div class="panel-body">
    <TABLE class="table1" style="font-size:14px;width: 100%;">
                    <TR>
                    <TH WIDTH="20%" ALIGN = "LEFT">
                        <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="pPrint_Config" value="email_configpayslip" <%=disabled%> <%=checkOption("email_configpayslip")%>>
                        &nbsp; Edit PaySlip Email Configuration</label>
  </TABLE>
   </div>
  </div>
  
<div class="panel panel-default">
    <div class="panel-heading" style="background: #eaeafa"><strong>Addition</strong></div>
    <div class="panel-body"> 
    	<TABLE class="table1" style="font-size:14px;width: 100%;">
    	   	
                    <TR>
                   
                    <TH WIDTH="20%" ALIGN = "LEFT">
                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="payaddition" value="payrolladdmstsummary" <%=disabled%> <%=checkOption("payrolladdmstsummary")%> >
                  &nbsp; Addition</label>
                   
                    <TH WIDTH="20%" ALIGN = "LEFT">
                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="payaddition" value="pnewaddition" <%=disabled%> <%=checkOption("pnewaddition")%> >
                  &nbsp; New</label>
                  
                   <TH WIDTH="20%" ALIGN = "LEFT">
                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="payaddition" value="peditaddition" <%=disabled%> <%=checkOption("peditaddition")%>>
                  &nbsp; Edit</label>
                    
                    <TH WIDTH="20%" ALIGN = "LEFT">
                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="payaddition" value="pexportaddition" <%=disabled%> <%=checkOption("pexportaddition")%> >
                  &nbsp; Export</label>
                  
              
                    
             </TABLE>
    </div>
</div> 

<div class="panel panel-default">
    <div class="panel-heading" style="background: #eaeafa"><strong>Deduction</strong></div>
    <div class="panel-body"> 
    	<TABLE class="table1" style="font-size:14px;width: 100%;">
                    <TR>
                                    
                    <TH WIDTH="20%" ALIGN = "LEFT">
                    <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="paydeduction" value="PayrollDeductionSummary" <%=disabled%> <%=checkOption("PayrollDeductionSummary")%> >
                    &nbsp; Deduction</label>                    
                   
                    
                    <TH WIDTH="20%" ALIGN = "LEFT">
                    <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="paydeduction" value="pnewdeduction" <%=disabled%> <%=checkOption("pnewdeduction")%> >
                    &nbsp; New</label>
                                
                    <TH WIDTH="20%" ALIGN = "LEFT">
                    <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="paydeduction" value="peditdeduction" <%=disabled%> <%=checkOption("peditdeduction")%> >
                    &nbsp; Edit</label>
                    
                    <TH WIDTH="20%" ALIGN = "LEFT">
                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="paydeduction" value="pexporteduction" <%=disabled%> <%=checkOption("pexporteduction")%> >
                  &nbsp; Export</label>
                
              
		                      
             </TABLE>
    </div>
</div>   
 
<div class="panel panel-default">
    <div class="panel-heading" style="background: #eaeafa"><strong>Payroll</strong></div>
    <div class="panel-body">
    <TABLE class="table1" style="font-size:14px;width: 100%;">
                    <TR>
                                                                                       
                    <%-- <TH WIDTH="20%" ALIGN = "LEFT">
                    <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="UserPayroll" value="pgraduity" <%=disabled%> <%=checkOption("pgraduity")%>>
                    &nbsp;  Graduity</label> --%>
                    
                     <TH WIDTH="20%" ALIGN = "LEFT">
                    <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="UserPayroll" value="PayrollSummary" <%=disabled%> <%=checkOption("PayrollSummary")%>>
                    &nbsp;  Payroll</label>
                       <TH WIDTH="20%" ALIGN = "LEFT">
                    <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="UserPayroll" value="pnewpayroll" <%=disabled%> <%=checkOption("pnewpayroll")%> >
                    &nbsp; New</label>
                                       	                        
	                 <TH WIDTH="20%" ALIGN = "LEFT">
	                 <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="UserPayroll" value="pdetailpayroll" <%=disabled%> <%=checkOption("pdetailpayroll")%> >
	                 &nbsp; Summary - Payroll Link</label>
	                 </TH>
                        
                     <TH WIDTH="20%" ALIGN = "LEFT">
	                 <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="UserPayroll" value="pexportpayroll" <%=disabled%> <%=checkOption("pexportpayroll")%> >
	                 &nbsp;  Export</label> 
	                 </TH>
                    
                    <TR>
                        
                     <TH WIDTH="20%" ALIGN = "LEFT">
	                 <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="UserPayroll" value="peditpayroll" <%=disabled%> <%=checkOption("peditpayroll")%> >
	                 &nbsp;  Edit</label> 
	                 </TH>
					 
					 <TH WIDTH="20%" ALIGN = "LEFT">
	                 <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="UserPayroll" value="payrollprint"  <%=disabled%> <%=checkOption("payrollprint")%> >
	                 &nbsp;  PDF/Print</label>
                     </TH>
                     <!--  <TH WIDTH="20%" ALIGN = "LEFT">
	                 <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="UserPayroll" value="summaryalternatbrandproduct"  >
	                 &nbsp;  Generate Payroll</label>
                     </TH> -->
                     
                  
                   </TABLE>
    </div>
</div>

<div class="panel panel-default">
    <div class="panel-heading" style="background: #eaeafa"><strong>Payroll Payment</strong></div>
    <div class="panel-body">
    <TABLE class="table1" style="font-size:14px;width: 100%;">
                    <TR>
                     <TH WIDTH="20%" ALIGN = "LEFT">
                    <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="PayrollPayment" value="PayrollPaymentSummary" <%=disabled%> <%=checkOption("PayrollPaymentSummary")%>>
                    &nbsp;  Payroll Payment</label>
                    
                       <TH WIDTH="20%" ALIGN = "LEFT">
                    <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="PayrollPayment" value="pnewPayrollPayment" <%=disabled%> <%=checkOption("pnewPayrollPayment")%> >
                    &nbsp; New</label>
                                       	                        
	                 <TH WIDTH="20%" ALIGN = "LEFT">
	                 <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="PayrollPayment" value="pdetailPayrollPayment" <%=disabled%> <%=checkOption("pdetailPayrollPayment")%> >
	                 &nbsp; Summary - Payroll Payment Link</label>
	                 </TH>
                        
                     <TH WIDTH="20%" ALIGN = "LEFT">
	                 <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="PayrollPayment" value="pexportPayrollPayment" <%=disabled%> <%=checkOption("pexportPayrollPayment")%> >
	                 &nbsp;  Export</label> 
	                 </TH>
                    
                    <TR>
                        
                     <TH WIDTH="20%" ALIGN = "LEFT">
	                 <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="PayrollPayment" value="peditPayrollPayment" <%=disabled%> <%=checkOption("peditPayrollPayment")%> >
	                 &nbsp;  Edit</label> 
	                 </TH>
                        
                     <TH WIDTH="20%" ALIGN = "LEFT">
	                 <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="PayrollPayment" value="pprintPayrollPayment"  <%=disabled%> <%=checkOption("pprintPayrollPayment")%> >
	                 &nbsp;  PDF/Print</label>
                     </TH>
     </TABLE>
    </div>
</div> 

<div class="panel panel-default">
    <div class="panel-heading" style="background: #eaeafa"><strong>Claim</strong></div>
    <div class="panel-body">
    <TABLE class="table1" style="font-size:14px;width: 100%;">
                    <TR>
                     <TH WIDTH="20%" ALIGN = "LEFT">
                    <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="pClaim" value="HrClaimSummary" <%=disabled%> <%=checkOption("HrClaimSummary")%>>
                    &nbsp;  Claim</label>
                    
                       <TH WIDTH="20%" ALIGN = "LEFT">
                    <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="pClaim" value="pnewClaim" <%=disabled%> <%=checkOption("pnewClaim")%> >
                    &nbsp; Process Claim</label>
                        
                     <TH WIDTH="20%" ALIGN = "LEFT">
	                 <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="pClaim" value="pexportClaim" <%=disabled%> <%=checkOption("pexportClaim")%> >
	                 &nbsp;  Export</label> 
	                 </TH>
     </TABLE>
    </div>
</div>  

<div class="panel panel-default">
    <div class="panel-heading" style="background: #eaeafa"><strong>Claim Payment</strong></div>
    <div class="panel-body">
    <TABLE class="table1" style="font-size:14px;width: 100%;">
                    <TR>
                    <TH WIDTH="20%" ALIGN = "LEFT">
                    <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="pClaimPayment" value="ClaimPaymentSummary" <%=disabled%> <%=checkOption("ClaimPaymentSummary")%>>
                    &nbsp;  Claim Payment</label>
                    
                    <TH WIDTH="20%" ALIGN = "LEFT">
	                 <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="pClaimPayment" value="pdetailClaimPayment" <%=disabled%> <%=checkOption("pdetailClaimPayment")%> >
	                 &nbsp; Summary - Claim Payment Link</label>
	                 </TH>
                        
                     <TH WIDTH="20%" ALIGN = "LEFT">
	                 <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="pClaimPayment" value="pexportClaimPayment" <%=disabled%> <%=checkOption("pexportClaimPayment")%> >
	                 &nbsp;  Export</label> 
	                 </TH>
                        
                     <TH WIDTH="20%" ALIGN = "LEFT">
	                 <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="pClaimPayment" value="peditClaimPayment" <%=disabled%> <%=checkOption("peditClaimPayment")%> >
	                 &nbsp;  Edit</label> 
	                 </TH>
                    
                    <TR>
                        
                     <TH WIDTH="20%" ALIGN = "LEFT">
	                 <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="pClaimPayment" value="pprintClaimPayment"  <%=disabled%> <%=checkOption("pprintClaimPayment")%> >
	                 &nbsp;  PDF/Print</label>
                     </TH>
                     
                     <TH WIDTH="20%" ALIGN = "LEFT">
	                 <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="pClaimPayment" value="pemailClaimPayment" <%=disabled%> <%=checkOption("pemailClaimPayment")%> >
	                 &nbsp; Email</label> 
	                 </TH>
     </TABLE>
    </div>
</div>    

 <div class="panel panel-default">
    <div class="panel-heading" style="background: #eaeafa"><strong>Bulk Payslip</strong></div>
    <div class="panel-body">
    <TABLE class="table1" style="font-size:14px;width: 100%;">
                    <TR>
                    <TH WIDTH="20%" ALIGN = "LEFT">
                        <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="pbulkpayslip" value="payslipgenerate" <%=disabled%> <%=checkOption("payslipgenerate")%>>
                        &nbsp; Bulk Payslip Processing</label>
  </TABLE>
   </div>
  </div>            

<div class="panel panel-default">
    <div class="panel-heading" style="background: #eaeafa"><strong>Payroll Reports</strong></div>
    <div class="panel-body">
    <TABLE class="table1" style="font-size:14px;width: 100%;">
    				<TR>
                     
                    <TH WIDTH="20%" ALIGN = "LEFT">
                    <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="payrollreport" value="PayrollDetailedReport" <%=disabled%> <%=checkOption("PayrollDetailedReport")%>>
                    &nbsp; Payroll Report</label>   
                    </TH>                
                    
                   <TH WIDTH="20%" ALIGN = "LEFT">
                    <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="payrollreport" value="paysummaryexport" <%=disabled%> <%=checkOption("paysummaryexport")%>>
                    &nbsp; Payroll Report Export</label>
                    </TH>
                 
                    <TH WIDTH="20%" ALIGN = "LEFT">
                    <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="payrollreport" value="paysummaryemail" <%=disabled%> <%=checkOption("paysummaryemail")%> >
                    &nbsp; Payroll Report Email</label>
                    </TH>
                    
                    <TH WIDTH="20%" ALIGN = "LEFT">
                    <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="payrollreport" value="DownloadPayslip" <%=disabled%> <%=checkOption("DownloadPayslip")%>>
                    &nbsp; Payslip</label>
                    </TH>
                    
                    <TR>
                    
                    <TH WIDTH="20%" ALIGN = "LEFT">
                    <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="payrollreport" value="rpprintPayslip" <%=disabled%> <%=checkOption("rpprintPayslip")%>>
                    &nbsp; Payslip Print</label>
                    </TH>
                    
                    <TH WIDTH="20%" ALIGN = "LEFT">
                    <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="payrollreport" value="rpemailPayslip" <%=disabled%> <%=checkOption("rpemailPayslip")%>>
                    &nbsp; Payslip Email</label>
                    </TH>
                                      
              
                  
              </TABLE>
    </div>
</div>


  <div class="panel panel-default">
    <div class="panel-heading" style="background: #eaeafa"><strong>Reports</strong></div>
    <div class="panel-body">
    <TABLE class="table1">
                <TR>
                 <TH WIDTH="20%" ALIGN = "LEFT">
                        <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="payreports" value="msPOList" <%=disabled%> <%=checkOption("msPOList")%>>
                        &nbsp; Activity Logs</label>   
                  
      
                          
                      </TABLE>
         </div>
  </div>
                       
                       
                       
                       
                       
<INPUT Type=Hidden name="URL" value="logout">
<div class="form-group">
<label class="control-label col-sm-4">Remarks</label>
 <div class="col-sm-4">
      <input name="REMARKS" <%=disabled%>  value="<%=al.get(al.size()-3).toString()%>"
	   size="50" MAXLENGTH=60 class="form-control">
	   </div>
		  </div>
                 
<div class="form-group">
<label class="control-label col-sm-4">Updated By</label>
<div class="col-sm-4">
<input  size="50" MAXLENGTH=60 disabled value="<%=authorise_by%>" class="form-control">
</div>
</div>
		  
<div class="form-group">
 <label class="control-label col-sm-4">Updated On</label>
 <div class="col-sm-4">
  <input  size="50" MAXLENGTH=60 disabled value="<%=al.get(al.size()-1).toString()%>" class="form-control">
	   </div>
		  </div>
		  
<div class="form-group">        
     <div class="col-sm-offset-5 col-sm-7">
     <!-- <button type="button" class="Submit btn btn-default" onClick="window.location.href='GroupSummary.jsp'"><b>Back</b></button>&nbsp;&nbsp; -->
	<!-- <button type="button" class="Submit btn btn-default" onClick="{backNavigation('settings.jsp','UA');}"><b>Back</b></button>&nbsp;&nbsp; -->
	 	</div>
	 	</div>
<%
    }   //  Closing else
 %>
 </form>
 </div>
 </div>
 </div>
 
<jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
</jsp:include>
