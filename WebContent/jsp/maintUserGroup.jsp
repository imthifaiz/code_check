<%@ include file="header.jsp" %>
<%@page import="com.track.constants.IConstants"%>
<%@ page import="com.track.dao.*"%>
<%
String title = "User Access Rights Group Details";
%>
<%@include file="sessionCheck.jsp" %>
<jsp:include page="header2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
	<jsp:param name="mainmenu" value="<%=IConstants.SETTINGS %>" />
	<jsp:param name="submenu" value="<%=IConstants.USER_ADMIN %>" />
</jsp:include>
<link rel="stylesheet" href="../jsp/css/typeahead.css">
<link rel="stylesheet" href="../jsp/css/accounting.css">
<style>
th {
vertical-align: top;
}
</style>
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
                 <li><a href="../useraccess/invsummary"><span class="underline-on-hover">User Access Rights Group Summary</span></a></li>	
                <li><label>User Access Rights Group Details</label></li>                                   
            </ul>
        <!-- Thanzith Modified on 28.02.2022 --> 
            <div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title"><%=title %></h1>
              <h1 style="font-size: 18px; cursor: pointer;"
				class="box-title pull-right"
				onclick="window.location.href='../useraccess/invsummary'">
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
     PlantMstDAO plantMstDAO = new PlantMstDAO();
     String ISPEPPOL =new PlantMstDAO().getisPeppol(plant);
     String  MANAGEWORKFLOW1 = plantMstDAO.getMANAGEWORKFLOW1(plant);
     String COMP_INDUSTRY = plantMstDAO.getCOMP_INDUSTRY(plant);//Check Company Industry
     String  ENABLE_POS =new PlantMstDAO().getispos(plant);
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
        String taxbylabelordermanagement = ub.getTaxByLableOrderManagement(plant1);
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
		al = ub.getUserLevelLinks(level_name, plant);
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
                
                 <%-- <TH WIDTH="20%" ALIGN = "LEFT">
                  <INPUT Type=Checkbox  style="border:0;" name="URL" value="homepurchasetotal" <%=disabled%> <%=checkOption("homepurchasetotal")%>>
                  &nbsp; Total Purchase
                  
                   <TH WIDTH="20%" ALIGN = "LEFT">
                  <INPUT Type=Checkbox  style="border:0;" name="URL" value="homesaletotal" <%=disabled%> <%=checkOption("homesaletotal")%>>
                  &nbsp; Total Sales
                                  
                  <TH WIDTH="20%" ALIGN = "LEFT">
                  <INPUT Type=Checkbox  style="border:0;" name="URL" value="homeallactivity" <%=disabled%> <%=checkOption("homeallactivity")%>>
                  &nbsp; See All Activity
                  
                   <TH WIDTH="20%" ALIGN = "LEFT">
                  <INPUT Type=Checkbox  style="border:0;" name="URL" value="homereceivedproductgrid" <%=disabled%> <%=checkOption("homereceivedproductgrid")%> >
                  &nbsp; Top Received Products 
                  <TR>
                   <TH WIDTH="20%" ALIGN = "LEFT">
                  <INPUT Type=Checkbox  style="border:0;" name="URL" value="homeissuedproductgrid" <%=disabled%> <%=checkOption("homeissuedproductgrid")%> >
                  &nbsp; Top Issued Products 
                  
                  <TH WIDTH="20%" ALIGN = "LEFT">
                  <INPUT Type=Checkbox  style="border:0;" name="URL" value="homeexpiringproductgrid" <%=disabled%> <%=checkOption("homeexpiringproductgrid")%> >
                  &nbsp; Expiring Products 
                  
                     <TH WIDTH="20%" ALIGN = "LEFT">
                  <INPUT Type=Checkbox  style="border:0;" name="URL" value="homestockproductgrid" <%=disabled%> <%=checkOption("homestockproductgrid")%> >
                  &nbsp; Stock Replenishment Products --%>
                  
                  <%-- <TH WIDTH="20%" ALIGN = "LEFT">
                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="URL" value="homeaccountingmanaget" <%=disabled%> <%=checkOption("homeaccountingmanaget")%>>
                  &nbsp; Accounting Management</label>
                  
                   <TH WIDTH="20%" ALIGN = "LEFT">
                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="URL" value="homeaccounting" <%=disabled%> <%=checkOption("homeaccounting")%>>
                  &nbsp; Accounting</label> --%>
                                  
                  <TH WIDTH="20%" ALIGN = "LEFT">
                 <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;" name="URL" value="homepurchase" <%=disabled%> <%=checkOption("homepurchase")%>>
                  &nbsp; Purchase</label>
                  
                   <TH WIDTH="20%" ALIGN = "LEFT">
                 <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;" name="URL" value="homesales" <%=disabled%> <%=checkOption("homesales")%> >
                  &nbsp; Sales </label>
                  
                   <TH WIDTH="20%" ALIGN = "LEFT">
                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="URL" value="homewarehouse" <%=disabled%> <%=checkOption("homewarehouse")%> >
                  &nbsp; Warehouse </label>
                  
                  <% if(COMP_INDUSTRY.equals("Retail")) { %> 
                   <th WIDTH="20%" ALIGN = "LEFT">
                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="URL" value="homepos" <%=disabled%> <%=checkOption("homepos")%>>
                  &nbsp;POS</label>
                  <%} %>
           
               </TABLE>
         </div>
  </div>
					
<div class="panel panel-default">
    <div class="panel-heading" style="background: #eaeafa"><strong>User Admin</strong></div>
    <div class="panel-body">
    <TABLE class="table1" style="font-size:14px;width: 100%;">
                 <TR>
                    <%if(plant1.equalsIgnoreCase("track")){%>
                     <TH WIDTH="20%" ALIGN = "LEFT">
                     <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;background=#dddddd" name="URL" value="companySummry" <%=disabled%> <%=checkOption("companySummry")%>>
                      &nbsp; Summary &#45; Company Details</label>
                      
                      <TH WIDTH="20%" ALIGN = "LEFT">
                     <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;background=#dddddd" name="URL" value="uaMaintCpny" <%=disabled%> <%=checkOption("uaMaintCpny")%>>
                       &nbsp; Edit Your Company Profile</label>
                      
                      <TH WIDTH="20%" ALIGN = "LEFT">
                      <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;background=#dddddd" name="URL" value="uaCreateCpny" <%=disabled%> <%=checkOption("uaCreateCpny")%>>
                       &nbsp; Create Company Details</label>
                      
                       <TH WIDTH="20%" ALIGN = "LEFT">
                       <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;background=#dddddd" name="URL" value="uaMaintAcct" <%=disabled%> <%=checkOption("uaMaintAcct")%>>
                        &nbsp;  Summary &#45; User Access Rights Group Details</label>
                        <%}%>
                
                    <TR>
                    <TH WIDTH="20%" ALIGN = "LEFT">
                    <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="UserAdmin" value="customerMaintCpny" <%=disabled%> <%=checkOption("customerMaintCpny")%>>
                    &nbsp; Edit Your Company Profile</label>
                    
                    <% if(COMP_INDUSTRY.equals("Retail")) { %> 
                    <TH WIDTH="20%" ALIGN = "LEFT">
                    <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="UserAdmin" value="outletsummary" <%=disabled%> <%=checkOption("outletsummary")%>>
                    &nbsp; Outlet Summary</label>
                    
                    <TH WIDTH="20%" ALIGN = "LEFT">
                    <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="UserAdmin" value="outletnew" <%=disabled%> <%=checkOption("outletnew")%>>
                    &nbsp; New Outlet</label>
                    
                    <TH WIDTH="20%" ALIGN = "LEFT">
                    <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="UserAdmin" value="outletedit" <%=disabled%> <%=checkOption("outletedit")%>>
                    &nbsp; Edit Outlet</label>
                    
                    <TR>
                    
                    <TH WIDTH="20%" ALIGN = "LEFT">
                    <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="UserAdmin" value="outletterminalummary" <%=disabled%> <%=checkOption("outletterminalummary")%>>
                    &nbsp; Outlet Terminal Summary</label>
                    
                    <TH WIDTH="20%" ALIGN = "LEFT">
                    <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="UserAdmin" value="outletterminalnew" <%=disabled%> <%=checkOption("outletterminalnew")%>>
                    &nbsp; New Outlet Terminal</label>
                    
                    <TH WIDTH="20%" ALIGN = "LEFT">
                    <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="UserAdmin" value="outletterminaledit" <%=disabled%> <%=checkOption("outletterminaledit")%>>
                    &nbsp; Edit Outlet Terminal</label>
                    
                     <TH WIDTH="20%" ALIGN = "LEFT">
                        <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="UserAdmin" value="companybanner" <%=disabled%> <%=checkOption("companybanner")%>>
                        &nbsp;  Banner</label>
                        <TR>
                     <%} %>
                        
                     <TH WIDTH="20%" ALIGN = "LEFT">
                    <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="UserAdmin" value="contactsmry" <%=disabled%> <%=checkOption("contactsmry")%>>
                    &nbsp; Contacts</label>
                    
                   <%-- <% if(COMP_INDUSTRY.equals("Retail")) { %> 
                        <TR>
                        <%} %> --%>
                       
                    <TH WIDTH="20%" ALIGN = "LEFT">
                    <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="UserAdmin" value="contactnew" <%=disabled%> <%=checkOption("contactnew")%>>
                    &nbsp; New Contacts</label>
                       
                    <TH WIDTH="20%" ALIGN = "LEFT">
                    <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="UserAdmin" value="contactedit" <%=disabled%> <%=checkOption("contactedit")%>>
                    &nbsp; Edit Contacts</label>
                     
                     <% if(!COMP_INDUSTRY.equals("Retail")) { %> 
                        <TR>
                        <%} %>
                       
                     <TH WIDTH="20%" ALIGN = "LEFT">
                     <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;background=#dddddd" name="UserAdmin" value="uaMaintLevel" <%=disabled%> <%=checkOption("uaMaintLevel")%>>
                       &nbsp; User Details</label>
                       
                       <% if(COMP_INDUSTRY.equals("Retail")) { %> 
                        <TR>
                        <%} %>
                       
                       <TH WIDTH="20%" ALIGN = "LEFT">
                     <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;background=#dddddd" name="UserAdmin" value="summarylnkuaMaintLevel" <%=disabled%> <%=checkOption("summarylnkuaMaintLevel")%>>
                       &nbsp; Summary &#45; User Details Link</label>
                    
                       <TH WIDTH="20%" ALIGN = "LEFT">
                     <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;background=#dddddd" name="UserAdmin" value="edituaMaintLevel" <%=disabled%> <%=checkOption("edituaMaintLevel")%>>
                       &nbsp; Edit User Details</label>
                        
                     <TH WIDTH="20%" ALIGN = "LEFT">
                        <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;background=#dddddd" name="UserAdmin" value="uaChngPwd" <%=disabled%> <%=checkOption("uaChngPwd")%>>
                        &nbsp; Edit Password Details</label>
                        
                        <% if(!COMP_INDUSTRY.equals("Retail")) { %> 
                        <TR>
                        <%} %>
                        
                      <% if(MANAGEWORKFLOW1.equals("1")) {%>
                        <TH WIDTH="20%" ALIGN = "LEFT">
                        <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;background=#dddddd" name="UserAdmin" value="manageworkflow" <%=disabled%> <%=checkOption("manageworkflow")%>>
                        &nbsp; Manage Workflow</label>
                     	<%}%>
                     	
                     	  <% if(MANAGEWORKFLOW1.equals("1")) {%>
                        <% if(COMP_INDUSTRY.equals("Retail")) { %> 
                        <TR>
                        <%} }%>
                     
                        <TH WIDTH="20%" ALIGN = "LEFT">
                          <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;background=#dddddd" name="UserAdmin" value="uaMaintAcct"  <%=disabled%> <%=checkOption("uaMaintAcct")%>>
                        &nbsp;  User Access Rights</label>
                        
                          <% if(MANAGEWORKFLOW1.equals("0")) {%>
                        <% if(COMP_INDUSTRY.equals("Retail")) { %> 
                        <TR>
                        <%} }%>
                        
                        <TH WIDTH="20%" ALIGN = "LEFT">
                          <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;background=#dddddd" name="UserAdmin" value="newuaMaintAcct"  <%=disabled%> <%=checkOption("newuaMaintAcct")%>>
                        &nbsp; New User Access Rights</label>
                       
                        <TH WIDTH="20%" ALIGN = "LEFT">
                          <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;background=#dddddd" name="UserAdmin" value="summarylnkuaMaintAcct"  <%=disabled%> <%=checkOption("summarylnkuaMaintAcct")%>>
                        &nbsp; Summary &#45; User Access Rights Link</label>
                    
                        <% if(MANAGEWORKFLOW1.equals("1")) {%>
                        <% if(!COMP_INDUSTRY.equals("Retail")) { %> 
                        <TR>
                        <%} }%>
                        
                        <TH WIDTH="20%" ALIGN = "LEFT">
                          <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;background=#dddddd" name="UserAdmin" value="edituaMaintAcct"  <%=disabled%> <%=checkOption("edituaMaintAcct")%>>
                        &nbsp; Edit User Access Rights</label>
                        
                     <% if(MANAGEWORKFLOW1.equals("1")) {%>
                        <% if(COMP_INDUSTRY.equals("Retail")) { %> 
                        <TR>
                        <%} }%>
                     
                       <% if(MANAGEWORKFLOW1.equals("0")) {%>
                        <% if(!COMP_INDUSTRY.equals("Retail")) { %> 
                        <TR>
                        <%} }%>
                        
                     <TH WIDTH="20%" ALIGN = "LEFT">
                     <%if(plant1.equalsIgnoreCase("track")){%>
                   <label class="checkbox-inline">  <INPUT Type=Checkbox  style="border:0;background=#dddddd" name="URL" value="uaNewLevel" <%=disabled%> <%=checkOption("uaNewLevel")%>>
                      &nbsp; Create User Details</label>
                      
                      <% if(MANAGEWORKFLOW1.equals("0")) {%>
                       <% if(COMP_INDUSTRY.equals("Retail")) { %> 
                        <TR>
                        <%} }%>
                      
                     <TH WIDTH="20%" ALIGN = "LEFT">
                   <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;background=#dddddd" name="URL" value="uaauthcmpy" <%=disabled%> <%=checkOption("uaauthcmpy")%>>&nbsp;Authorise Company Details<%}%> </label>     
                    </TABLE>
                 </div>
        </div>     
<div class="panel panel-default">
<div class="panel-heading" style="background: #eaeafa"><strong>Product</strong></div>
<div class="panel-body">
  <TABLE class="table1" style="font-size:14px;width: 100%;">
             <TR>
                  <TH WIDTH="20%" ALIGN = "LEFT">
                 <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;" name="SystemMaster" value="itemsummry"  <%=disabled%> <%=checkOption("itemsummry")%>>
                  &nbsp; Product</label>
                  
                  <TH WIDTH="20%" ALIGN = "LEFT">
                 <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;" name="SystemMaster" value="newuitem"  <%=disabled%> <%=checkOption("newuitem")%>>
                  &nbsp; New Product</label>
                  
                  <TH WIDTH="20%" ALIGN = "LEFT">
                 <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;" name="SystemMaster" value="summarylnkuitem"  <%=disabled%> <%=checkOption("summarylnkuitem")%>>
                  &nbsp; Summary &#45; Product Link</label>
                  
                  <TH WIDTH="20%" ALIGN = "LEFT">
                 <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;" name="SystemMaster" value="edituitem"  <%=disabled%> <%=checkOption("edituitem")%>>
                  &nbsp; Edit Product</label>
                  
                  <TR>
                  
                  <TH WIDTH="20%" ALIGN = "LEFT">
                 <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;" name="SystemMaster" value="importuitem"  <%=disabled%> <%=checkOption("importuitem")%>>
                  &nbsp; Import Product</label>
                  
                  <TH WIDTH="20%" ALIGN = "LEFT">
                 <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;" name="SystemMaster" value="exportuitem"  <%=disabled%> <%=checkOption("exportuitem")%>>
                  &nbsp; Export Product</label>
                  
                     <TH WIDTH="20%" ALIGN = "LEFT">
                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SystemMaster" value="alternateItemSummary"  <%=disabled%> <%=checkOption("alternateItemSummary")%>>   
                  &nbsp; Alternate Products</label>
                  
                  <TH WIDTH="20%" ALIGN = "LEFT">
                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SystemMaster" value="importalternateItem"  <%=disabled%> <%=checkOption("importalternateItem")%>>   
                  &nbsp; Import Alternate Products</label>
                  
                  <TR>
                  
                  <TH WIDTH="20%" ALIGN = "LEFT">
                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SystemMaster" value="exportalternateItem"  <%=disabled%> <%=checkOption("exportalternateItem")%>>   
                  &nbsp; Export Alternate Products</label>
                  
                  <TH WIDTH="20%" ALIGN = "LEFT">
                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SystemMaster" value="alternatebrandsummary" <%=disabled%> <%=checkOption("alternatebrandsummary")%>> 
                  &nbsp; Alternate Brand Product</label>
                  
                  <TH WIDTH="20%" ALIGN = "LEFT">
                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SystemMaster" value="newalternatebrand" <%=disabled%> <%=checkOption("newalternatebrand")%>> 
                  &nbsp; New Alternate Brand Product</label>
                  
                  <TH WIDTH="20%" ALIGN = "LEFT">
                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SystemMaster" value="summarylnkalternatebrand" <%=disabled%> <%=checkOption("summarylnkalternatebrand")%>> 
                  &nbsp; Summary &#45; Alternate Brand Product Link</label>
                  
                  <TR>
                  
                  <TH WIDTH="20%" ALIGN = "LEFT">
                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SystemMaster" value="editalternatebrand" <%=disabled%> <%=checkOption("editalternatebrand")%>> 
                  &nbsp; Edit Alternate Brand Product</label>
                  
                  <TH WIDTH="20%" ALIGN = "LEFT">
                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SystemMaster" value="exportalternatebrand" <%=disabled%> <%=checkOption("exportalternatebrand")%>> 
                  &nbsp; Export Alternate Brand Product</label>
                  
                   <% if(COMP_INDUSTRY.equals("Retail")) { %> 
                   <TH WIDTH="20%" ALIGN = "LEFT">
                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SystemMaster" value="productpromotionsummary" <%=disabled%> <%=checkOption("productpromotionsummary")%>>
                  &nbsp; Product Promotion</label>
                  
                   <TH WIDTH="20%" ALIGN = "LEFT">
                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SystemMaster" value="productpromotionnew" <%=disabled%> <%=checkOption("productpromotionnew")%>>
                  &nbsp; New Product Promotion</label>
                  
                  <% if(COMP_INDUSTRY.equals("Retail")) { %> 
                        <TR>
                        <%} %>
                  
                   <TH WIDTH="20%" ALIGN = "LEFT">
                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SystemMaster" value="productpromotionedit" <%=disabled%> <%=checkOption("productpromotionedit")%>>
                  &nbsp; Edit Product Promotion</label>
                  
                   <TH WIDTH="20%" ALIGN = "LEFT">
                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SystemMaster" value="categorypromotionsummary" <%=disabled%> <%=checkOption("categorypromotionsummary")%>>
                  &nbsp; Category Promotion</label>
                  
                    <% if(!COMP_INDUSTRY.equals("Retail")) { %> 
                        <TR>
                        <%} %>
                  
                   <TH WIDTH="20%" ALIGN = "LEFT">
                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SystemMaster" value="categorypromotionnew" <%=disabled%> <%=checkOption("categorypromotionnew")%> >
                  &nbsp; New Category Promotion</label>
                  
                   <TH WIDTH="20%" ALIGN = "LEFT">
                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SystemMaster" value="categorypromotionedit" <%=disabled%> <%=checkOption("categorypromotionedit")%>>
                  &nbsp; Edit Category Promotion</label>
                  
                  <% if(COMP_INDUSTRY.equals("Retail")) { %> 
                        <TR>
                        <%} %>
                  
                  <TH WIDTH="20%" ALIGN = "LEFT">
                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SystemMaster" value="brandpromotionsummary" <%=disabled%> <%=checkOption("brandpromotionsummary")%> >
                  &nbsp; Brand Promotion</label>
                  
                  <TH WIDTH="20%" ALIGN = "LEFT">
                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SystemMaster" value="brandpromotionnew" <%=disabled%> <%=checkOption("brandpromotionnew")%>>
                  &nbsp; New Brand Promotion</label>
                  
                    <% if(!COMP_INDUSTRY.equals("Retail")) { %> 
                        <TR>
                        <%} %>
                  
                  <TH WIDTH="20%" ALIGN = "LEFT">
                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SystemMaster" value="brandpromotionedit" <%=disabled%> <%=checkOption("brandpromotionedit")%>>
                  &nbsp; Edit Brand Promotion</label>
                  <%} %>
                  
                  <TH WIDTH="20%" ALIGN = "LEFT">
                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SystemMaster" value="postouchscreenconfig" <%=disabled%> <%=checkOption("postouchscreenconfig")%>>
                  &nbsp; POS Touch Screen Config</label>
                  
                 <% if(COMP_INDUSTRY.equals("Retail")) { %> 
                        <TR>
                        <%} %>
                  
                  <TH WIDTH="20%" ALIGN = "LEFT">
                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SystemMaster" value="postouchscreenconfigdelete" <%=disabled%> <%=checkOption("postouchscreenconfigdelete")%>>
                  &nbsp; Delete-POS Touch Screen Config</label>
                  
                    <% if(!COMP_INDUSTRY.equals("Retail")) { %> 
                        <TR>
                        <%} %>
                  
                  <TH WIDTH="20%" ALIGN = "LEFT">
                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SystemMaster" value="productdeptsummary" <%=disabled%> <%=checkOption("productdeptsummary")%>>
                  &nbsp; Product Department</label>
                  
                  <TH WIDTH="20%" ALIGN = "LEFT">
                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SystemMaster" value="newproductdept" <%=disabled%> <%=checkOption("newproductdept")%>>
                  &nbsp; New Product Department</label>
                  
                  <TH WIDTH="20%" ALIGN = "LEFT">
                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SystemMaster" value="summarylnkproductdept" <%=disabled%> <%=checkOption("summarylnkproductdept")%>>
                  &nbsp; Summary &#45; Product Department Link</label>
                  
                   <% if(COMP_INDUSTRY.equals("Retail")) { %> 
                        <TR>
                        <%} %>
                  
                  <TH WIDTH="20%" ALIGN = "LEFT">
                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SystemMaster" value="editproductdept" <%=disabled%> <%=checkOption("editproductdept")%>>
                  &nbsp; Edit Product Department</label>
                  
                    <% if(!COMP_INDUSTRY.equals("Retail")) { %> 
                        <TR>
                        <%} %>
                  
                  <TH WIDTH="20%" ALIGN = "LEFT">
                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SystemMaster" value="prdClsSummary" <%=disabled%> <%=checkOption("prdClsSummary")%>>
                  &nbsp; Product Category</label>
                  
                  <TH WIDTH="20%" ALIGN = "LEFT">
                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SystemMaster" value="newprdCls" <%=disabled%> <%=checkOption("newprdCls")%>>
                  &nbsp; New Product Category</label>
                  
                  <TH WIDTH="20%" ALIGN = "LEFT">
                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SystemMaster" value="summarylnkprdCls" <%=disabled%> <%=checkOption("summarylnkprdCls")%>>
                  &nbsp; Summary &#45; Product Category Link</label>
                  
                  <% if(COMP_INDUSTRY.equals("Retail")) { %> 
                        <TR>
                        <%} %>
                  
                  <TH WIDTH="20%" ALIGN = "LEFT">
                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SystemMaster" value="editprdCls" <%=disabled%> <%=checkOption("editprdCls")%>>
                  &nbsp; Edit Product Category</label>
                  
                    <% if(!COMP_INDUSTRY.equals("Retail")) { %> 
                        <TR>
                        <%} %>
                  
                  <TH WIDTH="20%" ALIGN = "LEFT">
                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SystemMaster" value="prdTypeSummary" <%=disabled%> <%=checkOption("prdTypeSummary")%>>
                  &nbsp; Product Sub Category</label>
                  
                  <TH WIDTH="20%" ALIGN = "LEFT">
                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SystemMaster" value="newprdType" <%=disabled%> <%=checkOption("newprdType")%>>
                  &nbsp; New Product Sub Category</label>
                  
                  <TH WIDTH="20%" ALIGN = "LEFT">
                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SystemMaster" value="summarylnkprdType" <%=disabled%> <%=checkOption("summarylnkprdType")%>>
                  &nbsp; Summary &#45; Product Sub Category Link</label>
                  
                 <% if(COMP_INDUSTRY.equals("Retail")) { %> 
                        <TR>
                        <%} %>
                  
                  <TH WIDTH="20%" ALIGN = "LEFT">
                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SystemMaster" value="editprdType" <%=disabled%> <%=checkOption("editprdType")%>>
                  &nbsp; Edit Product Sub Category</label>
                  
                  <% if(!COMP_INDUSTRY.equals("Retail")) { %> 
                        <TR>
                        <%} %>
                  
                  <TH WIDTH="20%" ALIGN = "LEFT">
	                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SystemMaster" value="prdBrandSummary"  <%=disabled%> <%=checkOption("prdBrandSummary")%>>
	                  &nbsp; Product Brand</label>
                  
                  <TH WIDTH="20%" ALIGN = "LEFT">
	                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SystemMaster" value="newprdBrand"  <%=disabled%> <%=checkOption("newprdBrand")%>>
	                  &nbsp; New Product Brand</label>
                  
                  <TH WIDTH="20%" ALIGN = "LEFT">
	                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SystemMaster" value="summarylnkprdBrand"  <%=disabled%> <%=checkOption("summarylnkprdBrand")%>>
	                  &nbsp; Summary &#45; Product Brand Link</label>
                              
                 <% if(COMP_INDUSTRY.equals("Retail")) { %> 
                        <TR>
                        <%} %>
                                          
                  <TH WIDTH="20%" ALIGN = "LEFT">
	                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SystemMaster" value="editprdBrand"  <%=disabled%> <%=checkOption("editprdBrand")%>>
	                  &nbsp; Edit Product Brand</label>  
	                  
                  	         </TABLE>
                  </div>
        </div>
        
        <div class="panel panel-default">
<div class="panel-heading" style="background: #eaeafa"><strong>UOM / Location / Reason Code</strong></div>
<div class="panel-body">
    <TABLE class="table1" style="font-size:14px;width: 100%;">
             <TR>
        			  <TH WIDTH="20%" ALIGN = "LEFT">
                      <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="UomLoc" value="uomsummry" <%=disabled%> <%=checkOption("uomsummry")%>>    
                       &nbsp; Unit of Measure (UOM)</label>
                       
                       <TH WIDTH="20%" ALIGN = "LEFT">
                      <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="UomLoc" value="newuom" <%=disabled%> <%=checkOption("newuom")%>>    
                       &nbsp; New Unit of Measure (UOM)</label>
                       
                       <TH WIDTH="20%" ALIGN = "LEFT">
                      <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="UomLoc" value="summarylnkuom" <%=disabled%> <%=checkOption("summarylnkuom")%>>    
                       &nbsp; Summary &#45; Unit of Measure (UOM) Link</label>
                       
                       <TH WIDTH="20%" ALIGN = "LEFT">
                      <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="UomLoc" value="edituom" <%=disabled%> <%=checkOption("edituom")%>>    
                       &nbsp; Edit Unit of Measure (UOM)</label>
                       
                       <TR>
                       
                       <TH WIDTH="20%" ALIGN = "LEFT">
	                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="UomLoc" value="locsummry"  <%=disabled%> <%=checkOption("locsummry")%>>     
	                  &nbsp; Location</label>
	                  
	                  <TH WIDTH="20%" ALIGN = "LEFT">
	                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="UomLoc" value="newloc"  <%=disabled%> <%=checkOption("newloc")%>>     
	                  &nbsp; New Location</label>
	                  
	                  <TH WIDTH="20%" ALIGN = "LEFT">
	                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="UomLoc" value="summarylnkloc"  <%=disabled%> <%=checkOption("summarylnkloc")%>>     
	                  &nbsp; Summary &#45; Location Link</label>
	                  
	                  <TH WIDTH="20%" ALIGN = "LEFT">
	                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="UomLoc" value="editloc"  <%=disabled%> <%=checkOption("editloc")%>>     
	                  &nbsp; Edit Location</label>
	                  
	                  <TR>
	                  
	                  <TH WIDTH="20%" ALIGN = "LEFT">
	                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="UomLoc" value="importloc"  <%=disabled%> <%=checkOption("importloc")%>>     
	                  &nbsp; Import Location</label>
	                   
	                    <TH WIDTH="20%" ALIGN = "LEFT">
	                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="UomLoc" value="loctypesummry"  <%=disabled%> <%=checkOption("loctypesummry")%>>    
	                  &nbsp; Location Type One</label>
	                  
	                  <TH WIDTH="20%" ALIGN = "LEFT">
	                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="UomLoc" value="newloctype"  <%=disabled%> <%=checkOption("newloctype")%>>    
	                  &nbsp; New Location Type One</label>
	                  
	                  <TH WIDTH="20%" ALIGN = "LEFT">
	                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="UomLoc" value="summarylnkloctype"  <%=disabled%> <%=checkOption("summarylnkloctype")%>>    
	                  &nbsp; Summary &#45; Location Type One Link</label> 
	                  
	                <TR>
	                  
	                  <TH WIDTH="20%" ALIGN = "LEFT">
	                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="UomLoc" value="editloctype"  <%=disabled%> <%=checkOption("editloctype")%>>    
	                  &nbsp; Edit Location Type One</label>
	                  
					  <TH WIDTH="20%" ALIGN = "LEFT">
	                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="UomLoc" value="loctypetwosummry"  <%=disabled%> <%=checkOption("loctypetwosummry")%>>    
	                  &nbsp; Location Type Two</label>
	                  
	                  <TH WIDTH="20%" ALIGN = "LEFT">
	                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="UomLoc" value="newloctypetwo"  <%=disabled%> <%=checkOption("newloctypetwo")%>>    
	                  &nbsp; New Location Type Two</label>
	                  
	                  <TH WIDTH="20%" ALIGN = "LEFT">
	                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="UomLoc" value="summarylnkloctypetwo"  <%=disabled%> <%=checkOption("summarylnkloctypetwo")%>>    
	                  &nbsp; Summary &#45; Location Type Two Link</label> 
	                  
	                <TR>
	                  
	                  <TH WIDTH="20%" ALIGN = "LEFT">
	                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="UomLoc" value="editloctypetwo"  <%=disabled%> <%=checkOption("editloctypetwo")%>>    
	                  &nbsp; Edit Location Type Two</label>
	                  
	                  <TH WIDTH="20%" ALIGN = "LEFT">
	                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="UomLoc" value="loctypethreesummry"  <%=disabled%> <%=checkOption("loctypethreesummry")%>>    
	                  &nbsp; Location Type Three</label>
	                  
	                  <TH WIDTH="20%" ALIGN = "LEFT">
	                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="UomLoc" value="newloctypethree"  <%=disabled%> <%=checkOption("newloctypethree")%>>    
	                  &nbsp; New Location Type Three</label>
	                  
	                  <TH WIDTH="20%" ALIGN = "LEFT">
	                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="UomLoc" value="summarylnkloctypethree"  <%=disabled%> <%=checkOption("summarylnkloctypethree")%>>    
	                  &nbsp; Summary &#45; Location Type Three Link</label> 
	                  
	                <TR>
	                  
	                  <TH WIDTH="20%" ALIGN = "LEFT">
	                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="UomLoc" value="editloctypethree"  <%=disabled%> <%=checkOption("editloctypethree")%>>    
	                  &nbsp; Edit Location Type Three</label>
	                  
	                  <TH WIDTH="20%" ALIGN = "LEFT">
	                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="UomLoc" value="assignuserlocation"  <%=disabled%> <%=checkOption("assignuserlocation")%>>    
	                  &nbsp; Assign User Location</label>
	                  
	                  <TH WIDTH="20%" ALIGN = "LEFT">
	                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="UomLoc" value="assigninvlocation"  <%=disabled%> <%=checkOption("assigninvlocation")%>>    
	                  &nbsp; Assign API Inventory Location</label>
	                  
	                  <TH WIDTH="20%" ALIGN = "LEFT">
	                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="UomLoc" value="rsnsummry"  <%=disabled%> <%=checkOption("rsnsummry")%>>     
	                  &nbsp; Reason Code</label>  
	                  
	                <TR>
	                  
	                  <TH WIDTH="20%" ALIGN = "LEFT">
	                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="UomLoc" value="newrsn"  <%=disabled%> <%=checkOption("newrsn")%>>     
	                  &nbsp; New Reason Code</label>
	                  
	                  <TH WIDTH="20%" ALIGN = "LEFT">
	                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="UomLoc" value="summarylnkrsn"  <%=disabled%> <%=checkOption("summarylnkrsn")%>>     
	                  &nbsp; Summary &#45; Reason Code Link</label> 
	                  
	                  <TH WIDTH="20%" ALIGN = "LEFT">
	                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="UomLoc" value="editrsn"  <%=disabled%> <%=checkOption("editrsn")%>>     
	                  &nbsp; Edit Reason Code</label>
	                  
                  	         </TABLE>
                  </div>
        </div>  
<div class="panel panel-default">
<div class="panel-heading" style="background: #eaeafa"><strong>Supplier</strong></div>
<div class="panel-body">
    <TABLE class="table1" style="font-size:14px;width: 100%;">
             <TR>                 
                  <TH WIDTH="20%" ALIGN = "LEFT">
                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="Supplier" value="vendorsummry" <%=disabled%><%=checkOption("vendorsummry")%>> 
                  &nbsp;  Supplier</label>
                  
                  <TH WIDTH="20%" ALIGN = "LEFT">
                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="Supplier" value="newvendor" <%=disabled%><%=checkOption("newvendor")%>> 
                  &nbsp;  New Supplier</label>
                  
                  <TH WIDTH="20%" ALIGN = "LEFT">
                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="Supplier" value="editvendor" <%=disabled%><%=checkOption("editvendor")%>> 
                  &nbsp;  Edit Supplier</label>
                  
                  <TH WIDTH="20%" ALIGN = "LEFT">
                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="Supplier" value="importvendor" <%=disabled%><%=checkOption("importvendor")%>> 
                  &nbsp;  Import Supplier</label>
                  
                  <TR>
                  
                  <TH WIDTH="20%" ALIGN = "LEFT">
                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="Supplier" value="exportvendor" <%=disabled%><%=checkOption("exportvendor")%>> 
                  &nbsp;  Export Supplier</label>
                  
                   <TH WIDTH="20%" ALIGN = "LEFT">
                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="Supplier" value="supplierdiscountsummary" <%=disabled%><%=checkOption("supplierdiscountsummary")%>> 
                  &nbsp; Supplier Discount</label>
                  
                  <TH WIDTH="20%" ALIGN = "LEFT">
                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="Supplier" value="importsupplierdiscount" <%=disabled%><%=checkOption("importsupplierdiscount")%>> 
                  &nbsp; Import Supplier Discount</label> 
                  
                  <TH WIDTH="20%" ALIGN = "LEFT">
                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="Supplier" value="exportsupplierdiscount" <%=disabled%><%=checkOption("exportsupplierdiscount")%>> 
                  &nbsp; Export Supplier Discount</label>
                  
                  <TR>
                     
	                  <TH WIDTH="20%" ALIGN = "LEFT">
	                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="Supplier" value="supplierTypeSummary" <%=disabled%><%=checkOption("supplierTypeSummary")%>> 
	                  &nbsp; Supplier Group</label>
	                  
	                  <TH WIDTH="20%" ALIGN = "LEFT">
	                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="Supplier" value="newsupplierType" <%=disabled%><%=checkOption("newsupplierType")%>> 
	                  &nbsp; New Supplier Group</label>
	                  
	                  <TH WIDTH="20%" ALIGN = "LEFT">
	                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="Supplier" value="editsupplierType" <%=disabled%><%=checkOption("editsupplierType")%>> 
	                  &nbsp; Edit Supplier Group</label>
	                  
	                  </TABLE>
                  </div>
        </div>
<div class="panel panel-default">
<div class="panel-heading" style="background: #eaeafa"><strong>Customer</strong></div>
<div class="panel-body">
    <TABLE class="table1" style="font-size:14px;width: 100%;">
             <TR>
             
             		 <TH WIDTH="20%" ALIGN = "LEFT">
	                 <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;" name="Customer" value="customersummry" <%=disabled%><%=checkOption("customersummry")%>> 
	                  &nbsp; Customer</label>
	                  
	                  <TH WIDTH="20%" ALIGN = "LEFT">
	                 <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;" name="Customer" value="newcustomer" <%=disabled%><%=checkOption("newcustomer")%>> 
	                  &nbsp; New Customer</label>
	                  
	                  <TH WIDTH="20%" ALIGN = "LEFT">
	                 <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;" name="Customer" value="editcustomer" <%=disabled%><%=checkOption("editcustomer")%>> 
	                  &nbsp; Edit Customer</label>
	                  
	                  <TH WIDTH="20%" ALIGN = "LEFT">
	                 <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;" name="Customer" value="importcustomer" <%=disabled%><%=checkOption("importcustomer")%>> 
	                  &nbsp; Import Customer</label>
	                  
	                  <TR>
	                  
	                  <TH WIDTH="20%" ALIGN = "LEFT">
	                 <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;" name="Customer" value="exportcustomer" <%=disabled%><%=checkOption("exportcustomer")%>> 
	                  &nbsp; Export Customer</label>
	                  
	                  <TH WIDTH="20%" ALIGN = "LEFT">
	                 <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;" name="Customer" value="customerdiscountsummary" <%=disabled%><%=checkOption("customerdiscountsummary")%>> 
	                   &nbsp; Customer Discount</label>
	                   
	                   <TH WIDTH="20%" ALIGN = "LEFT">
	                 <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;" name="Customer" value="importcustomerdiscount" <%=disabled%><%=checkOption("importcustomerdiscount")%>> 
	                   &nbsp; Import Customer Discount</label>
	                   
	                   <TH WIDTH="20%" ALIGN = "LEFT">
	                 <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;" name="Customer" value="exportcustomerdiscount" <%=disabled%><%=checkOption("exportcustomerdiscount")%>> 
	                   &nbsp; Export Customer Discount</label>
	                  
	                  <TR>
	                  
	                   <TH WIDTH="20%" ALIGN = "LEFT">
	                 <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;" name="Customer" value="ctsummry" <%=disabled%><%=checkOption("ctsummry")%>> 
	                 &nbsp; Customer Group</label>
	                 
	                 <TH WIDTH="20%" ALIGN = "LEFT">
	                 <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;" name="Customer" value="newct" <%=disabled%><%=checkOption("newct")%>> 
	                 &nbsp; New Customer Group</label>
	                 
	                 <TH WIDTH="20%" ALIGN = "LEFT">
	                 <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;" name="Customer" value="editct" <%=disabled%><%=checkOption("editct")%>> 
	                 &nbsp; Edit Customer Group</label>
	                 
	                 <% if(COMP_INDUSTRY.equals("Retail")) { %> 
	                 <TH WIDTH="20%" ALIGN = "LEFT">
	                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="Customer" value="AssignCus" <%=disabled%><%=checkOption("AssignCus")%>>    
	                  &nbsp; Assign User Customer</label>
	                  
	                  <TR>
	                  
	                  <TH WIDTH="20%" ALIGN = "LEFT">
	                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="Customer" value="Custproduct" <%=disabled%><%=checkOption("Custproduct")%>>    
	                  &nbsp; Assign Customer Product</label>
	                  <%} %>
	                  
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
                        &nbsp; New Employee</label>
	                 
                    <TH WIDTH="20%" ALIGN = "LEFT">
                        <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="UserEmployee" value="peditemoloyee" <%=disabled%> <%=checkOption("peditemoloyee")%>>
                        &nbsp; Edit Employee</label>
             
                     <TH WIDTH="20%" ALIGN = "LEFT">
                        <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="UserEmployee" value="pimportemployee" <%=disabled%> <%=checkOption("pimportemployee")%>>
                        &nbsp; Import Employee</label>
                        
             <TR>
             
                     <TH WIDTH="20%" ALIGN = "LEFT">
                        <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="UserEmployee" value="pexportmasterdata" <%=disabled%> <%=checkOption("pexportmasterdata")%>>
                        &nbsp; Export Employee</label>
                      </TABLE>
         </div>
  </div>
        
<div class="panel panel-default">
<div class="panel-heading" style="background: #eaeafa"><strong>Import Inventory/Stock Take Data</strong></div>
<div class="panel-body">
    <TABLE class="table1" style="font-size:14px;width: 100%;">
             <TR> 
	                  <TH WIDTH="20%" ALIGN = "LEFT">
	                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SystemAdmin" value="importdata" <%=disabled%><%=checkOption("importdata")%>> 
	                  &nbsp; Import Inventory/Stock Take Data</label>
                                
        	 </TABLE>
        	  </div>
        </div> 
        
<div class="panel panel-default">
<div class="panel-heading" style="background: #eaeafa"><strong>Order Admin</strong></div>
<div class="panel-body"> 
		<TABLE class="table1" style="font-size:14px;width: 100%;">
        	 <TR>                 
                                        
                  <TH WIDTH="20%" ALIGN = "LEFT">
                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="OrderAdmin" value="ordtypesumry" <%=disabled%> <%=checkOption("ordtypesumry")%>>
                  &nbsp;  Order Type  </label>
                  
                  <TH WIDTH="20%" ALIGN = "LEFT">
                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="OrderAdmin" value="newordtype" <%=disabled%> <%=checkOption("newordtype")%>>
                  &nbsp;  New Order Type  </label>
                  
                  <TH WIDTH="20%" ALIGN = "LEFT">
                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="OrderAdmin" value="summarylnkordtype" <%=disabled%> <%=checkOption("summarylnkordtype")%>>
                  &nbsp;  Summary &#45; Order Type Link </label>
                  
                  <TH WIDTH="20%" ALIGN = "LEFT">
                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="OrderAdmin" value="editordtype" <%=disabled%> <%=checkOption("editordtype")%>>
                  &nbsp;  Edit Order Type  </label>
                  
                  <TR>
                  
                     <TH WIDTH="20%" ALIGN = "LEFT">
                     <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;" name="OrderAdmin" value="editpaymenttype" <%=disabled%> <%=checkOption("editpaymenttype")%>>
                      &nbsp; Payment Type </label>
                      
                      <TH WIDTH="20%" ALIGN = "LEFT">
                     <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;" name="OrderAdmin" value="newpaymenttype" <%=disabled%> <%=checkOption("newpaymenttype")%>>
                      &nbsp; New Payment Type </label>
                      
                      <TH WIDTH="20%" ALIGN = "LEFT">
                     <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;" name="OrderAdmin" value="removepaymenttype" <%=disabled%> <%=checkOption("removepaymenttype")%>>
                      &nbsp; Remove Payment Type </label>
                      
                       <!-- ***************************************************** -->        
                      <TH WIDTH="20%" ALIGN = "LEFT">
                     <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;" name="OrderAdmin" value="Paytermedit" <%=disabled%> <%=checkOption("Paytermedit")%>>
                      &nbsp; Payment Terms</label>
                   <TR>   
                      <TH WIDTH="20%" ALIGN = "LEFT">
                     <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;" name="OrderAdmin" value="Paytermnew" <%=disabled%> <%=checkOption("Paytermnew")%>>
                      &nbsp; New Payment Terms </label>
                      
                      <TH WIDTH="20%" ALIGN = "LEFT">
                     <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;" name="OrderAdmin" value="Paytermremove" <%=disabled%> <%=checkOption("Paytermremove")%>>
                      &nbsp; Remove Payment Terms </label>
                      
                      <TH WIDTH="20%" ALIGN = "LEFT">
                     <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;" name="OrderAdmin" value="PaymentModeedit" <%=disabled%> <%=checkOption("PaymentModeedit")%>>
                      &nbsp; Payment Mode </label>
                      
                      <TH WIDTH="20%" ALIGN = "LEFT">
                     <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;" name="OrderAdmin" value="PaymentModenew" <%=disabled%> <%=checkOption("PaymentModenew")%>> 
                      &nbsp; New Payment Mode </label>
                      
                   <TR>   
                   
                      <TH WIDTH="20%" ALIGN = "LEFT">
                     <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;" name="OrderAdmin" value="PaymentModeremove" <%=disabled%> <%=checkOption("PaymentModeremove")%>>
                      &nbsp; Remove Payment Mode </label>
         <!--**************************************************  -->
                      
                      <TH WIDTH="20%" ALIGN = "LEFT">
                 <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="OrderAdmin" value="kittingSumm" <%=disabled%> <%=checkOption("kittingSumm")%>>
                  &nbsp; Bill Of Materials </label>
                  
                  
                  <TH WIDTH="20%" ALIGN = "LEFT">
                 <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="OrderAdmin" value="kittingNew" <%=disabled%> <%=checkOption("kittingNew")%>>
                  &nbsp; Bill Of Materials New</label>
                  
                  <TH WIDTH="20%" ALIGN = "LEFT">
                 <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="OrderAdmin" value="kittingEdit" <%=disabled%> <%=checkOption("kittingEdit")%>>
                  &nbsp; Bill Of Materials Edit</label>
                  
               <TR>   
                  
                  <TH WIDTH="20%" ALIGN = "LEFT">
                 <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="OrderAdmin" value="kittingExport" <%=disabled%> <%=checkOption("kittingExport")%>>
                  &nbsp; Bill Of Materials Export</label>
                  
                   <TH WIDTH="20%" ALIGN = "LEFT">
                 <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;" name="OrderAdmin" value="gstSummry" <%=disabled%> <%=checkOption("gstSummry")%>>
                  &nbsp; <%=taxbylabelordermanagement%> </label>
                  
                  
                  <TH WIDTH="20%" ALIGN = "LEFT">
                 <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;" name="OrderAdmin" value="newgst" <%=disabled%> <%=checkOption("newgst")%>>
                  &nbsp; New <%=taxbylabelordermanagement%> </label>
                  
                  <TH WIDTH="20%" ALIGN = "LEFT">
                 <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;" name="OrderAdmin" value="summarylnkgst" <%=disabled%> <%=checkOption("summarylnkgst")%>>
                  &nbsp; Summary &#45; <%=taxbylabelordermanagement%> Link</label>
                  
               <TR>   
                  
                  <TH WIDTH="20%" ALIGN = "LEFT">
                 <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;" name="OrderAdmin" value="editgst" <%=disabled%> <%=checkOption("editgst")%>>
                  &nbsp; Edit <%=taxbylabelordermanagement%> </label>
                  
                  <TH WIDTH="20%" ALIGN = "LEFT">
                 <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="OrderAdmin" value="cursummry" <%=disabled%> <%=checkOption("cursummry")%>>
                  &nbsp; Currency </label>
                  
                  
                  <TH WIDTH="20%" ALIGN = "LEFT">
                 <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="OrderAdmin" value="newcurs" <%=disabled%> <%=checkOption("newcurs")%>>
                  &nbsp; New Currency </label>
                  
                  <TH WIDTH="20%" ALIGN = "LEFT">
                 <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="OrderAdmin" value="summarylnkcurs" <%=disabled%> <%=checkOption("summarylnkcurs")%>>
                  &nbsp; Summary &#45; Currency Link</label>
                  
                  <TR>
                  
                  <TH WIDTH="20%" ALIGN = "LEFT">
                 <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="OrderAdmin" value="editcurs" <%=disabled%> <%=checkOption("editcurs")%>>
                  &nbsp; Edit Currency </label>
                  
                  </TABLE>
        	  </div>
        </div> 
        
<div class="panel panel-default">
<div class="panel-heading" style="background: #eaeafa"><strong>HSCODE / COO / Remarks / INCOTERM / Footer </strong></div>
<div class="panel-body"> 
		<TABLE class="table1" style="font-size:14px;width: 100%;">
        	 <TR> 
                  
                    <TH WIDTH="20%" ALIGN = "LEFT">
                 <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="OrderAdminHS" value="edithscode" <%=disabled%> <%=checkOption("edithscode")%>>
                  &nbsp; HSCODE</label>
                  
                  <TH WIDTH="20%" ALIGN = "LEFT">
                 <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="OrderAdminHS" value="newhscodes" <%=disabled%> <%=checkOption("newhscodes")%>>
                  &nbsp; New HSCODE</label>
                  
                  <TH WIDTH="20%" ALIGN = "LEFT">
                 <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="OrderAdminHS" value="removehscodes" <%=disabled%> <%=checkOption("removehscodes")%>>
                  &nbsp; Remove HSCODE</label>
                  
                    <TH WIDTH="20%" ALIGN = "LEFT">
                 <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="OrderAdminHS" value="editcoo" <%=disabled%> <%=checkOption("editcoo")%>>
                  &nbsp; COO</label>
                  
                  <TR>
                  
                  <TH WIDTH="20%" ALIGN = "LEFT">
                 <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="OrderAdminHS" value="newcoos" <%=disabled%> <%=checkOption("newcoos")%>>
                  &nbsp; New COO</label>
                  
                  <TH WIDTH="20%" ALIGN = "LEFT">
                 <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="OrderAdminHS" value="removecoos" <%=disabled%> <%=checkOption("removecoos")%>>
                  &nbsp; Remove COO</label>
                  
                    <TH WIDTH="20%" ALIGN = "LEFT">
                <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;" name="OrderAdminHS" value="editorderremarks" <%=disabled%> <%=checkOption("editorderremarks")%>>
                  &nbsp; Remarks</label>
                  
                  <TH WIDTH="20%" ALIGN = "LEFT">
                <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;" name="OrderAdminHS" value="neworderremarks" <%=disabled%> <%=checkOption("neworderremarks")%>>
                  &nbsp; New Remarks</label>
                  
                  <TR>
                  
                  <TH WIDTH="20%" ALIGN = "LEFT">
                <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;" name="OrderAdminHS" value="removeorderremarks" <%=disabled%> <%=checkOption("removeorderremarks")%>>
                  &nbsp; Remove Remarks</label>
                  
                    <TH WIDTH="20%" ALIGN = "LEFT">
                <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;" name="OrderAdminHS" value="editorderincoterms" <%=disabled%> <%=checkOption("editorderincoterms")%>>
                  &nbsp; INCTORERM</label>
                  
                    <TH WIDTH="20%" ALIGN = "LEFT">
                <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;" name="OrderAdminHS" value="neworderincoterms" <%=disabled%> <%=checkOption("neworderincoterms")%>>
                  &nbsp; New INCTORERM</label>
                  
                    <TH WIDTH="20%" ALIGN = "LEFT">
                <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;" name="OrderAdminHS" value="removeorderincoterms" <%=disabled%> <%=checkOption("removeorderincoterms")%>>
                  &nbsp; Remove INCTORERM</label>
                  
                  <TR> 
                  
                    <TH WIDTH="20%" ALIGN = "LEFT">
                 <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="OrderAdminHS" value="editorderfooter" <%=disabled%> <%=checkOption("editorderfooter")%>>
                  &nbsp; Footer</label>
                  
                  <TH WIDTH="20%" ALIGN = "LEFT">
                 <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="OrderAdminHS" value="neworderfooter" <%=disabled%> <%=checkOption("neworderfooter")%>>
                  &nbsp; New Footer</label>
                  
                  <TH WIDTH="20%" ALIGN = "LEFT">
                 <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="OrderAdminHS" value="removeorderfooter" <%=disabled%> <%=checkOption("removeorderfooter")%>>
                  &nbsp; Remove Footer</label>
                  
                  
                 </TABLE>
              </div>
        </div> 
        
        <div class="panel panel-default">
<div class="panel-heading" style="background: #eaeafa"><strong>Distribution</strong></div>
<div class="panel-body"> 
		<TABLE class="table1" style="font-size:14px;width: 100%;">
        	 <TR> 
        	  <TH WIDTH="20%" ALIGN = "LEFT">
                 <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="Distribution" value="packingMasterSummary"  <%=disabled%> <%=checkOption("packingMasterSummary")%>>
                  &nbsp; Packing List </label>
                  
                  <TH WIDTH="20%" ALIGN = "LEFT">
                 <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="Distribution" value="newpackingMaster"  <%=disabled%> <%=checkOption("newpackingMaster")%>>
                  &nbsp; New Packing List </label>
                  
                  <TH WIDTH="20%" ALIGN = "LEFT">
                 <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="Distribution" value="removepackingMaster"  <%=disabled%> <%=checkOption("removepackingMaster")%>>
                  &nbsp; Remove Packing List </label>
                  
                  <TH WIDTH="20%" ALIGN = "LEFT">
                 <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="Distribution" value="transport_mode"  <%=disabled%> <%=checkOption("transport_mode")%>>
                  &nbsp; Transport Mode </label>
                  
                  <TR>
                  
                  <TH WIDTH="20%" ALIGN = "LEFT">
                 <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="Distribution" value="newtransportmode"  <%=disabled%> <%=checkOption("newtransportmode")%>>
                  &nbsp; New Transport Mode </label>
                  
                  <TH WIDTH="20%" ALIGN = "LEFT">
                 <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="Distribution" value="removetransportmode"  <%=disabled%> <%=checkOption("removetransportmode")%>>
                  &nbsp; Remove Transport Mode </label>
                  
                     <TH WIDTH="20%" ALIGN = "LEFT">
                 <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="Distribution" value="clearagentsummary" <%=disabled%> <%=checkOption("clearagentsummary")%>>
                  &nbsp; Clearing Agent </label>
                  
                  <TH WIDTH="20%" ALIGN = "LEFT">
                 <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="Distribution" value="newclearagent" <%=disabled%> <%=checkOption("newclearagent")%>>
                  &nbsp; New Clearing Agent </label>
                  
                  <TR>
                  
                  <TH WIDTH="20%" ALIGN = "LEFT">
                 <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="Distribution" value="editclearagent" <%=disabled%> <%=checkOption("editclearagent")%>>
                  &nbsp; Edit Clearing Agent </label>
                  
                  <TH WIDTH="20%" ALIGN = "LEFT">
                 <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="Distribution" value="summarylnkclearagent" <%=disabled%> <%=checkOption("summarylnkclearagent")%>>
                  &nbsp; Summary Clearing Agent Link </label>
                  
                  <TH WIDTH="20%" ALIGN = "LEFT">
                 <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="Distribution" value="clearanceTypesummary" <%=disabled%> <%=checkOption("clearanceTypesummary")%>>
                  &nbsp; Clearance Type </label>
                  
                  <TH WIDTH="20%" ALIGN = "LEFT">
                 <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="Distribution" value="newclearancetype" <%=disabled%> <%=checkOption("newclearancetype")%>>
                  &nbsp; New Clearance Type </label>
                  
                  <TR>
                  
                  <TH WIDTH="20%" ALIGN = "LEFT">
                 <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="Distribution" value="editclearancetype" <%=disabled%> <%=checkOption("editclearancetype")%>>
                  &nbsp; Edit Clearance Type </label>
                  
                  <TH WIDTH="20%" ALIGN = "LEFT">
                 <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="Distribution" value="summarylnkclearancetype" <%=disabled%> <%=checkOption("summarylnkclearancetype")%>>
                  &nbsp; Summary Clearance Type Link </label>
                  
                  
                  <TH WIDTH="20%" ALIGN = "LEFT">
                 <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="Distribution" value="shippersummary"  <%=disabled%> <%=checkOption("shippersummary")%>>
                  &nbsp; Freight Forwarder </label>
                  
                  <TH WIDTH="20%" ALIGN = "LEFT">
                 <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="Distribution" value="shippernew"  <%=disabled%> <%=checkOption("shippernew")%>>
                  &nbsp; New Freight Forwarder </label>
                  
                  <TR>
                  
                  <TH WIDTH="20%" ALIGN = "LEFT">
                 <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="Distribution" value="shipperedit"  <%=disabled%> <%=checkOption("shipperedit")%>>
                  &nbsp; Edit Freight Forwarder </label>
        	 
        	 
        </TABLE>
        	  </div>
        </div>     	

<div class="panel panel-default">
<div class="panel-heading" style="background: #eaeafa"><strong>Printout Configuration</strong></div>
<div class="panel-body"> 
    	<TABLE class="table1" style="font-size:14px;width: 100%;">
	             <TR>
	             
	             <TH WIDTH="20%" ALIGN = "LEFT">
                    <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="URL" value="purchaseestimateconfig"  <%=disabled%> <%=checkOption("purchaseestimateconfig")%>>
                    &nbsp; Purchase Estimate Configuration</label>
	                  
	              <TH WIDTH="20%" ALIGN = "LEFT">
             <label class="checkbox-inline">       <INPUT Type=Checkbox  style="border:0;" name="URL" value="editibpt"  <%=disabled%> <%=checkOption("editibpt")%>>
                    &nbsp; Edit Purchase Order Printout   </label>         
                 
       				 <TH WIDTH="23%" ALIGN = "LEFT">
             <label class="checkbox-inline">       <INPUT Type=Checkbox  style="border:0;" name="URL" value="editibwithcostpt" <%=disabled%> <%=checkOption("editibwithcostpt")%>>
                    &nbsp; Edit Purchase Order Printout (with cost)</label>
                    
                    <TH WIDTH="23%" ALIGN = "LEFT">
              <label class="checkbox-inline">       <INPUT Type=Checkbox  style="border:0;" name="URL" value="editInboundOrderMailMsg"  <%=disabled%> <%=checkOption("editInboundOrderMailMsg")%>>
                     &nbsp; Edit Purchase Order Email Message</label>
                     
                    <TR>
                    
                    <TH WIDTH="23%" ALIGN = "LEFT">
              <label class="checkbox-inline">       <INPUT Type=Checkbox  style="border:0;" name="URL" value="editBillPrintout"  <%=disabled%> <%=checkOption("editBillPrintout")%>>
                     &nbsp; Edit Bill Printout</label>
                    
                     <TH WIDTH="23%" ALIGN = "LEFT">
                   <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;" name="URL" value="editestwithpt" <%=disabled%> <%=checkOption("editestwithpt")%>>
                    &nbsp; Edit Sales Estimate Order Printout (with price)</label>
                    
                    <TH WIDTH="23%" ALIGN = "LEFT">
                    <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="URL" value="editEstimateOrderMailMsg"  <%=disabled%> <%=checkOption("editEstimateOrderMailMsg")%>>
                    &nbsp; Edit Sales Estimate Order Email Message</label>
                   
                    <TH WIDTH="20%" ALIGN = "LEFT">
                  <label class="checkbox-inline">  <INPUT Type=Checkbox  style="border:0;" name="URL" value="editobpt" <%=disabled%> <%=checkOption("editobpt")%>>
                    &nbsp; Edit Sales Order Printout</label>
                     
                    <TR>
                  
                       <TH WIDTH="23%" ALIGN = "LEFT">
                  <label class="checkbox-inline">  <INPUT Type=Checkbox  style="border:0;" name="URL" value="editobwithpt" <%=disabled%> <%=checkOption("editobwithpt")%>>
                    &nbsp; Edit Sales Order Printout (with price)</label>
               
                    <TH WIDTH="23%" ALIGN = "LEFT">
                  <label class="checkbox-inline">  <INPUT Type=Checkbox  style="border:0;" name="URL" value="editOutboundOrderMailMsg"  <%=disabled%> <%=checkOption("editOutboundOrderMailMsg")%>>
                    &nbsp; Edit Sales Order Email Message</label>
                    
                      <TH WIDTH="20%" ALIGN = "LEFT">
                  <label class="checkbox-inline">  <INPUT Type=Checkbox  style="border:0;" name="URL" value="edittowop" <%=disabled%> <%=checkOption("edittowop")%>>
                    &nbsp; Edit Consignment Order Printout</label>
                    
                      <TH WIDTH="20%" ALIGN = "LEFT">
                  <label class="checkbox-inline">  <INPUT Type=Checkbox  style="border:0;" name="URL" value=""edittopt"" <%=disabled%> <%=checkOption("edittopt")%>>
                    &nbsp; Edit Consignment Order Printout(With Price)</label>
                     
                    <TR>
                    
                     <TH WIDTH="20%" ALIGN = "LEFT">
                    <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="OrderConfiguration" value="editTransferOrderMailMsg"  <%=disabled%> <%=checkOption("editTransferOrderMailMsg")%>>
                    &nbsp; Edit Consignment Order Email Message</label>
                   
                  <%--   <TH WIDTH="20%" ALIGN = "LEFT">
                  <label class="checkbox-inline">  <INPUT Type=Checkbox  style="border:0;" name="URL" value="editlopt" <%=disabled%> <%=checkOption("editlopt")%>>
                    &nbsp; Edit Rental Order Printout</label>
               
                    <TH WIDTH="20%" ALIGN = "LEFT">
                  <label class="checkbox-inline">  <INPUT Type=Checkbox  style="border:0;" name="URL" value="edittopt" <%=disabled%> <%=checkOption("edittopt")%>>
                    &nbsp; Edit Consignment Order Printout</label>

                    <TH WIDTH="20%" ALIGN = "LEFT">
                 <label class="checkbox-inline">   <INPUT Type=Checkbox  style="border:0;" name="URL" value="editTransferOrderMailMsg"  <%=disabled%> <%=checkOption("editTransferOrderMailMsg")%>>
                    &nbsp; Edit Consignment Order Email Message</label> --%>
                   
                    <TH WIDTH="20%" ALIGN = "LEFT">
                 <label class="checkbox-inline">   <INPUT Type=Checkbox  style="border:0;" name="URL" value="editTaxInvoiceMultiLanguage"  <%=disabled%> <%=checkOption("editTaxInvoiceMultiLanguage")%>>
                    &nbsp; Edit Invoice Printout</label>
                  
                     <TH WIDTH="20%" ALIGN = "LEFT">
                  <label class="checkbox-inline">  <INPUT Type=Checkbox  style="border:0;" name="URL" value="editRcptRecvHdr" <%=disabled%> <%=checkOption("editRcptRecvHdr")%>>
                    &nbsp; Edit Goods Receipt Printout</label>
                                   
                    <TH WIDTH="20%" ALIGN = "LEFT">
                 <label class="checkbox-inline">   <INPUT Type=Checkbox  style="border:0;" name="URL" value="editRcptIssueHdr" <%=disabled%> <%=checkOption("editRcptIssueHdr")%>>
                    &nbsp; Edit Goods Issue Printout</label>
                     
                    <TR>
                  
                    <TH WIDTH="20%" ALIGN = "LEFT">
                <label class="checkbox-inline">    <INPUT Type=Checkbox  style="border:0;" name="URL" value="editMoveHdr" <%=disabled%> <%=checkOption("editMoveHdr")%>>
                    &nbsp; Edit Stock Move Printout</label>
                     
                       <% if(COMP_INDUSTRY.equals("Retail")) { %> 
                    <TH WIDTH="20%" ALIGN = "LEFT">
                   <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;" name="OrderConfiguration" value="goodsshiftclosereceiptprintout" <%=disabled%> <%=checkOption("goodsshiftclosereceiptprintout")%>>
                    &nbsp; Edit POS Shift Close Receipt Printout </label>
                      <%} %>
                       <TH WIDTH="20%" ALIGN = "LEFT">
                   <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;" name="OrderConfiguration" value="editMinMaxEmailMsg" <%=disabled%> <%=checkOption("editMinMaxEmailMsg")%>>
                    &nbsp; Edit Inventory Min/Max Qty Email Alert </label>
                    
                       <TH WIDTH="20%" ALIGN = "LEFT">
                   <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;" name="OrderConfiguration" value="editInvexpiredateEmailMsg" <%=disabled%> <%=checkOption("editInvexpiredateEmailMsg")%>>
                    &nbsp; Edit Inventory Expiry Date Email Alert </label>
                      </TABLE>
                </div>
                   </div>
                   <%-- <div class="panel panel-default">
<div class="panel-heading" style="background: #eaeafa"><strong>Approval & Email Configuration</strong></div>
<div class="panel-body"> 
    	<TABLE class="table1" style="font-size:14px;width: 100%;">
	             <TR>
	             <TH WIDTH="20%" ALIGN = "LEFT">
                      <INPUT Type=Checkbox  style="border:0;" name="URL" value="editemailconfig" <%=disabled%> <%=checkOption("editemailconfig")%>>
                      &nbsp; Order Management Approval & Email Configuration
	             </TABLE>
                </div>
                   </div> --%> 
 <div class="panel panel-default">
    <div class="panel-heading" style="background: #eaeafa"><strong>Order Management - Create Masters</strong></div>
    <div class="panel-body"> 
    	<TABLE class="table1" style="font-size:14px;width: 100%;">
    	   	
                    <TR>
                   
                    <TH WIDTH="20%" ALIGN = "LEFT">
                <label class="checkbox-inline">  <INPUT Type=Checkbox  style="border:0;" name="URL" value="popproduct" <%=disabled%> <%=checkOption("popproduct")%> > 
                  &nbsp; Create Product Details &#45; Purchase, Bill, Sales Estimate, Sales,Invoice</label>
                   
                    <TH WIDTH="20%" ALIGN = "LEFT">
              <label class="checkbox-inline">    <INPUT Type=Checkbox  style="border:0;" name="URL" value="popsupplier" <%=disabled%> <%=checkOption("popsupplier")%> >
                  &nbsp; Create Supplier Details &#45; Purchase Order</label>
                  
                   <TH WIDTH="20%" ALIGN = "LEFT">
              <label class="checkbox-inline">    <INPUT Type=Checkbox  style="border:0;" name="URL" value="suppliertypepopup" <%=disabled%> <%=checkOption("suppliertypepopup")%>>
                  &nbsp; Create Supplier Type - Purchase Order</label>
                  
                  <TH WIDTH="20%" ALIGN = "LEFT">
               <label class="checkbox-inline">   <INPUT Type=Checkbox  style="border:0;" name="URL" value="popcustomer" <%=disabled%> <%=checkOption("popcustomer")%> >
                  &nbsp; Create Customer Details &#45; Sales Estimate, Sales,Invoice</label>
                
                 <TR> 
                 <TH WIDTH="20%" ALIGN = "LEFT">
              <label class="checkbox-inline">    <INPUT Type=Checkbox  style="border:0;" name="URL" value="customertypepopup" <%=disabled%> <%=checkOption("customertypepopup")%>>
                  &nbsp;Create Customer Type - Sales Estimate, Sales,Invoice</label>
                 
				  <TH WIDTH="20%" ALIGN = "LEFT">
             <label class="checkbox-inline">     <INPUT Type=Checkbox  style="border:0;" name="URL" value="ordertypepopup" <%=disabled%> <%=checkOption("ordertypepopup")%>>
                  &nbsp;Create Order Type - Purchase, Bill, Sales Estimate, Sales,Invoice</label>
				                    
                    <TH WIDTH="20%" ALIGN = "LEFT">
            <label class="checkbox-inline">      <INPUT Type=Checkbox  style="border:0;" name="URL" value="paymenttypepopup" <%=disabled%> <%=checkOption("paymenttypepopup")%>>
                  &nbsp;Payment Type - Purchase, Bill, Sales Estimate, Sales,Invoice</label>
                    
             </TABLE>
    </div>
</div> 

<div class="panel panel-default">
<div class="panel-heading" style="background: #eaeafa"><strong>Project</strong></div>
<div class="panel-body"> 
             <TABLE class="table1" style="font-size:14px;width: 100%;">
             <TR> 
                                     
                    <TH WIDTH="20%" ALIGN = "LEFT">
                   <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;" name="Project" value="summaryproject" <%=disabled%> <%=checkOption("summaryproject")%>>
                    &nbsp; Project</label>
             
             <TH WIDTH="20%" ALIGN = "LEFT">
                     <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="Project" value="newproject" <%=disabled%> <%=checkOption("newproject")%>>
                     &nbsp; New</label>
                     
                    <TH WIDTH="20%" ALIGN = "LEFT">
	                 <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="Project" value="summarylnkproject" <%=disabled%> <%=checkOption("summarylnkproject")%> >
	                 &nbsp; Summary &#45; Project Number Link</label>
                     
                    <TH WIDTH="20%" ALIGN = "LEFT">
                     <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="Project" value="editproject" <%=disabled%> <%=checkOption("editproject")%>>
                     &nbsp; Edit</label>
                        
                     <TR>
                        
                    <TH WIDTH="20%" ALIGN = "LEFT">
                     <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="Project" value="exportproject" <%=disabled%> <%=checkOption("exportproject")%>>
                     &nbsp; Export</label> 
                     
                     <TH WIDTH="20%" ALIGN = "LEFT">
                     <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="Project" value="deleteproject" <%=disabled%> <%=checkOption("deleteproject")%>>
                     &nbsp; Delete</label>
             </TABLE>
             </div>
        </div>

             
<div class="panel panel-default">
<div class="panel-heading" style="background: #eaeafa"><strong>Purchase Estimate</strong></div>
<div class="panel-body"> 
             <TABLE class="table1" style="font-size:14px;width: 100%;">
             <TR> 
                                     
                    <TH WIDTH="20%" ALIGN = "LEFT">
                   <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;" name="Purchaseestimate" value="summarypurchaseestimate" <%=disabled%> <%=checkOption("summarypurchaseestimate")%>>
                    &nbsp; Multiple Purchase Estimate</label>
             
             		<TH WIDTH="20%" ALIGN = "LEFT">
                     <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="Purchaseestimate" value="newpurchaseestimate" <%=disabled%> <%=checkOption("newpurchaseestimate")%>>
                     &nbsp; New</label>
                     
                    <TH WIDTH="20%" ALIGN = "LEFT">
	                 <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="Purchaseestimate" value="summarylnkpurchaseestimate" <%=disabled%> <%=checkOption("summarylnkpurchaseestimate")%> >
	                 &nbsp; Summary &#45; Multiple Purchase Estimate Number Link</label>
                     
                    <TH WIDTH="20%" ALIGN = "LEFT">
                     <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="Purchaseestimate" value="editpurchaseestimate" <%=disabled%> <%=checkOption("editpurchaseestimate")%>>
                     &nbsp; Edit</label>
                        
                     <TR>
                        
                    <TH WIDTH="20%" ALIGN = "LEFT">
                     <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="Purchaseestimate" value="morepurchaseestimate" <%=disabled%> <%=checkOption("morepurchaseestimate")%>>
                     &nbsp; More</label>
                                     
                    <TH WIDTH="20%" ALIGN = "LEFT">
                    <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="Purchaseestimate" value="summarypurchaseorderestimate" <%=disabled%> <%=checkOption("summarypurchaseorderestimate")%>>
                    &nbsp; Purchase Estimate</label>
                    
                    <TH WIDTH="20%" ALIGN = "LEFT">
                     <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="Purchaseestimate" value="newpurchaseorderestimate" <%=disabled%> <%=checkOption("newpurchaseorderestimate")%>>
                     &nbsp; New</label>
                    
                    <TH WIDTH="20%" ALIGN = "LEFT">
	                 <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="Purchaseestimate" value="summarylnkpurchaseorderest" <%=disabled%> <%=checkOption("summarylnkpurchaseorderest")%> >
	                 &nbsp; Summary &#45; Multiple Purchase Estimate Number Link</label>
	                 
                     <TR>                
	                 
                    <TH WIDTH="20%" ALIGN = "LEFT">
                     <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="Purchaseestimate" value="editpurchaseorderestimate" <%=disabled%> <%=checkOption("editpurchaseorderestimate")%>>
                     &nbsp; Edit</label>

				     <TH WIDTH="20%" ALIGN = "LEFT">
                     <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="Purchaseestimate" value="printpurchaseorderestimate" <%=disabled%> <%=checkOption("printpurchaseorderestimate")%>>
                     &nbsp; Print</label>
                     
                    <TH WIDTH="20%" ALIGN = "LEFT">
                     <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="Purchaseestimate" value="morepurchaseorderestimate" <%=disabled%> <%=checkOption("morepurchaseorderestimate")%>>
                     &nbsp; More</label>
                     
                    <TH WIDTH="20%" ALIGN = "LEFT">
                     <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="Purchaseestimate" value="reordersummary" <%=disabled%> <%=checkOption("reordersummary")%>>
                     &nbsp; Reorder Summary</label>

                    			
             </TABLE>
             </div>
        </div>
                      
             
<div class="panel panel-default">
<div class="panel-heading" style="background: #eaeafa"><strong>Purchase Order</strong></div>
<div class="panel-body"> 
             <TABLE class="table1" style="font-size:14px;width: 100%;">
             <TR> 
                                     
                    <TH WIDTH="20%" ALIGN = "LEFT">
                   <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;" name="OrderManagement1" value="summarypurchaseorder" <%=disabled%> <%=checkOption("summarypurchaseorder")%>>
                    &nbsp; Purchase Order</label>
             
             <TH WIDTH="20%" ALIGN = "LEFT">
                     <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="OrderManagement1" value="newpurchaseorder" <%=disabled%> <%=checkOption("newpurchaseorder")%>>
                     &nbsp; New</label>
                     
                    <TH WIDTH="20%" ALIGN = "LEFT">
	                 <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="OrderManagement1" value="summarylnkpurchaseorder" <%=disabled%> <%=checkOption("summarylnkpurchaseorder")%> >
	                 &nbsp; Summary &#45; Order Number Link</label>
                     
                    <TH WIDTH="20%" ALIGN = "LEFT">
                     <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="OrderManagement1" value="editpurchaseorder" <%=disabled%> <%=checkOption("editpurchaseorder")%>>
                     &nbsp; Edit</label>
                        
                     <TR>
                        
                    <TH WIDTH="20%" ALIGN = "LEFT">
                     <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="OrderManagement1" value="printpurchaseorder" <%=disabled%> <%=checkOption("printpurchaseorder")%>>
                     &nbsp; PDF/Print</label> 
                       
                    <TH WIDTH="20%" ALIGN = "LEFT">
                     <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="OrderManagement1" value="morepurchaseorder" <%=disabled%> <%=checkOption("morepurchaseorder")%>>
                     &nbsp; More</label>
                                     
                    <TH WIDTH="20%" ALIGN = "LEFT">
                    <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="OrderManagement1" value="ibsummary" <%=disabled%> <%=checkOption("ibsummary")%>>
                    &nbsp; Summary &#45; Purchase Order Details    </label>
                    
                    <TH WIDTH="20%" ALIGN = "LEFT">
                    <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="OrderManagement1" value="summarylnkibsummary" <%=disabled%> <%=checkOption("summarylnkibsummary")%>>
                    &nbsp; Summary &#45; Purchase Order Details Number Link</label>
                        
                     <TR>                
                    
                     <TH WIDTH="20%" ALIGN = "LEFT">
                    <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="OrderManagement1" value="ibsummarycost" <%=disabled%> <%=checkOption("ibsummarycost")%>>
                    &nbsp; Summary &#45; Purchase Order Details (with cost)</label>                
                    
                     <TH WIDTH="20%" ALIGN = "LEFT">
                    <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="OrderManagement1" value="summarylnkibsummarycost" <%=disabled%> <%=checkOption("summarylnkibsummarycost")%>>
                    &nbsp; Summary &#45; Purchase Order Details Number Link (with cost)</label>
                    
                    <%-- <TH WIDTH="20%" ALIGN = "LEFT">
                   <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;" name="OrderManagement1" value="maintinb" <%=disabled%> <%=checkOption("maintinb")%>>
                    &nbsp; Edit Purchase Order Details</label> --%>                     
                               
                    <TH WIDTH="20%" ALIGN = "LEFT">
                    <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="OrderManagement1" value="orderClose" <%=disabled%> <%=checkOption("orderClose")%>>
                    &nbsp; Close Outstanding Purchase Order</label>

                    			
             </TABLE>
             </div>
        </div>
                      
<div class="panel panel-default">
<div class="panel-heading" style="background: #eaeafa"><strong>Purchase Transaction</strong></div>
<div class="panel-body"> 
             <TABLE class="table1" style="font-size:14px;width: 100%;">
             <TR>
             
               <th WIDTH="20%" ALIGN = "LEFT">
                    <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="PurchaseTransaction" value="purchaseTransactionDashboard" <%=disabled%> <%=checkOption("purchaseTransactionDashboard")%>>
                    &nbsp; Purchase Transaction </label>
             
             		<TH WIDTH="20%" ALIGN = "LEFT">
                    <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="PurchaseTransaction" value="ibrecvbulk" <%=disabled%> <%=checkOption("ibrecvbulk")%>>
                    &nbsp; Purchase Order Receipt </label>
                        
                         <TH WIDTH="20%" ALIGN = "LEFT">
                       <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;" name="PurchaseTransaction" value="inbrectbyrange"   <%=disabled%> <%=checkOption("inbrectbyrange")%>>
                        &nbsp; Purchase Order Receipt (by serial)</label>
                        
                       <TH WIDTH="20%" ALIGN = "LEFT">
                        <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="PurchaseTransaction" value="inbRecbyprd"   <%=disabled%> <%=checkOption("inbRecbyprd")%>>
                        &nbsp; Purchase Order Receipt (by product)</label>                                              
                        
                            <TR> 
                            
                         <TH WIDTH="20%" ALIGN = "LEFT">
                        <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="PurchaseTransaction" value="ibrecvmultiple" <%=disabled%> <%=checkOption("ibrecvmultiple")%>>
                        &nbsp; Purchase Order Receipt (multiple) </label> 
                        
                                         
                                             
                       <%-- <TH WIDTH="20%" ALIGN = "LEFT">
                        <INPUT Type=Checkbox  style="border:0;" name="OrderManagement1" value="inbReverse"   <%=disabled%> <%=checkOption("inbReverse")%>>
                        &nbsp; Purchase Order Reversal --%>
                                                          
                        <TH WIDTH="20%" ALIGN = "LEFT">
                        <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="PurchaseTransaction" value="mulmiscrecipt"   <%=disabled%> <%=checkOption("mulmiscrecipt")%>>
                        &nbsp; Goods Receipt </label>
                        
                         <TH WIDTH="20%" ALIGN = "LEFT">
                        <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="PurchaseTransaction" value="inbMiscRceiptByRange"   <%=disabled%> <%=checkOption("inbMiscRceiptByRange")%>>
                        &nbsp; Goods Receipt (by serial)</label>
                       
                        <TH WIDTH="20%" ALIGN = "LEFT">
                        <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="PurchaseTransaction" value="editexpiry" <%=disabled%> <%=checkOption("editexpiry")%>>
                        &nbsp; Edit Inventory Expire Date</label>
             
             </TABLE>
             </div>
        </div>
        
        
        <div class="panel panel-default">
<div class="panel-heading" style="background: #eaeafa"><strong>Purchase Return</strong></div>
<div class="panel-body">
       <table class="table1" style="font-size:14px;width: 100%;">
                    <tr>

 					<th WIDTH="20%" ALIGN = "LEFT">
	                 <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="PurchaseReturn" value="POReturnSummary"   <%=disabled%> <%=checkOption("POReturnSummary")%> >
	                 &nbsp; Purchase Return</label>

	                <th WIDTH="20%" ALIGN = "LEFT">
	                 <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="PurchaseReturn" value="newpurchasereturn"  <%=disabled%> <%=checkOption("newpurchasereturn")%> >
	                 &nbsp; New</label>

	                <th WIDTH="20%" ALIGN = "LEFT">
	                 <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="PurchaseReturn" value="summarylnkpurchasereturn" <%=disabled%> <%=checkOption("summarylnkpurchasereturn")%> >
	                 &nbsp; Summary &#45; Return Number Link</label>

	              <!--   AZEEZ -->
	                 <th WIDTH="20%" ALIGN = "LEFT">
	                 <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="PurchaseReturn" value="exportpurchasereturn"  <%=disabled%> <%=checkOption("exportpurchasereturn")%> >
	                 &nbsp; Export</label>

	                 <tr>

	                 <th WIDTH="20%" ALIGN = "LEFT">
	                 <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="PurchaseReturn" value="excelpurchasereturn"  <%=disabled%> <%=checkOption("excelpurchasereturn")%> >
	                 &nbsp; Export Excel</label>
	                 <!--   AZEEZ -->

	                <th WIDTH="20%" ALIGN = "LEFT">
	                 <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="PurchaseReturn" value="applycreditnotepurchasereturn"  <%=disabled%> <%=checkOption("applycreditnotepurchasereturn")%> >
	                 &nbsp; Apply To Debit Note</label>

		</table>
     </div>
 </div>
 
 <div class="panel panel-default">
<div class="panel-heading" style="background: #eaeafa"><strong>Internal Product Return</strong></div>
<div class="panel-body">
       <TABLE class="table1" style="font-size:14px;width: 100%;">
                    <TR>
                    
 					<TH WIDTH="20%" ALIGN = "LEFT">
	                 <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="ProductReturn" value="productreturn" <%=disabled%> <%=checkOption("productreturn")%> >     
	                 &nbsp; Product Return</label>
	                 
	                <TH WIDTH="20%" ALIGN = "LEFT">
	                 <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="ProductReturn" value="newproductreturn" <%=disabled%> <%=checkOption("newproductreturn")%> >     
	                 &nbsp; New</label>
	                 
	                <TH WIDTH="20%" ALIGN = "LEFT">
	                 <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="ProductReturn" value="summarylnkproductreturn" <%=disabled%> <%=checkOption("summarylnkproductreturn")%> >
	                 &nbsp; Summary &#45; Return Number Link</label>
	                 
	                 <TH WIDTH="20%" ALIGN = "LEFT">
	                 <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="ProductReturn" value="exportproductreturn" <%=disabled%> <%=checkOption("exportproductreturn")%> >     
	                 &nbsp; Export</label>
	                 
	                 <TR>
	                 
		</TABLE>
     </div>
 </div>
 
        <div class="panel panel-default">
    <div class="panel-heading" style="background: #eaeafa"><strong>Purchase Reports</strong></div>
    <div class="panel-body">
    <TABLE class="table1" style="font-size:14px;width: 100%;">
                    <TR>
                    
                    	<TH WIDTH="20%" ALIGN = "LEFT">
		               <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;" name="PurchaseReports" value="ibordersumry" <%=disabled%> <%=checkOption("ibordersumry")%> >
		                &nbsp;  Purchase Order Summary Details</label>
		                </TH>
		                
                   		<TH WIDTH="20%" ALIGN = "LEFT">
                        	<label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="PurchaseReports" value="suppliersummary" <%=disabled%> <%=checkOption("suppliersummary")%> >
                        	&nbsp;  Purchase Order Summary(by supplier)</label>
                        </TH>	                         	
	                       
	                    <TH WIDTH="20%" ALIGN = "LEFT">
	                   <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;" name="PurchaseReports" value="ibordersumrywreccost" <%=disabled%> <%=checkOption("ibordersumrywreccost")%> >
	                    &nbsp;  Purchase Order Summary Details(by cost)</label>
	                    </TH> 
                     
	                    <TH WIDTH="20%" ALIGN = "LEFT">
	                   <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;" name="PurchaseReports" value="recvdibsummry"   <%=disabled%> <%=checkOption("recvdibsummry")%> >
	                    &nbsp;  Purchase Order Summary(by cost)</label>
	                    </TH>
	                    
	                    <TR>
	                        
	                    <TH WIDTH="20%" ALIGN = "LEFT">
	                   <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;" name="PurchaseReports" value="printpo"   <%=disabled%> <%=checkOption("printpo")%> >
	                    &nbsp;  Generate PDF (without cost)</label>
	                    </TH>
	                        
	                    <TH WIDTH="20%" ALIGN = "LEFT">
	                    <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="PurchaseReports" value="printpoinv"  <%=disabled%> <%=checkOption("printpoinv")%> >
	                    &nbsp;  Generate PDF (with cost)</label>
	                    </TH>
	                        
	                    <TH WIDTH="20%" ALIGN = "LEFT">
	                   <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;" name="PurchaseReports" value="recvsummry"   <%=disabled%> <%=checkOption("recvsummry")%> >
	                    &nbsp;  Order Receipt Summary</label>
	                    </TH>
	                    
	                        
	                    <TH WIDTH="20%" ALIGN = "LEFT">
	                   <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;" name="PurchaseReports" value="recvsummrywithcost"  <%=disabled%> <%=checkOption("recvsummrywithcost")%> >
	                    &nbsp;  Order Receipt Summary (with cost)</label>
	                    </TH>
                    
    </TABLE>
             </div>
        </div>
                   
<div class="panel panel-default">
<div class="panel-heading" style="background: #eaeafa"><strong>Sales Estimate</strong></div>
<div class="panel-body">
    <TABLE class="table1" style="font-size:14px;width: 100%;">
            <TR>
                                       
                    <TH WIDTH="20%" ALIGN = "LEFT">
                    <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SalesEstimate" value="summarysalesestimate" <%=disabled%> <%=checkOption("summarysalesestimate")%>>
                    &nbsp; Sales Estimate Order</label>
                    
                    <TH WIDTH="20%" ALIGN = "LEFT">
                     <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SalesEstimate" value="newsalesestimate" <%=disabled%> <%=checkOption("newsalesestimate")%>>
                     &nbsp; New</label>
                     
                    <TH WIDTH="20%" ALIGN = "LEFT">
	                 <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SalesEstimate" value="summarylnksalesestimate" <%=disabled%> <%=checkOption("summarylnksalesestimate")%>>
	                 &nbsp; Summary &#45; Order Number Link</label>
                     
                    <TH WIDTH="20%" ALIGN = "LEFT">
                     <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SalesEstimate" value="editsalesestimate" <%=disabled%> <%=checkOption("editsalesestimate")%>>
                     &nbsp; Edit</label>
                        
                     <TR>
                        
                    <TH WIDTH="20%" ALIGN = "LEFT">
                     <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SalesEstimate" value="printsalesestimate" <%=disabled%> <%=checkOption("printsalesestimate")%>>
                     &nbsp; PDF/Print</label> 
                       
                    <TH WIDTH="20%" ALIGN = "LEFT">
                     <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SalesEstimate" value="moresalesestimate" <%=disabled%> <%=checkOption("moresalesestimate")%>>
                     &nbsp; More</label>                       
                       
                    <TH WIDTH="20%" ALIGN = "LEFT">
                    <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SalesEstimate" value="estsummarywithoutprice" <%=disabled%> <%=checkOption("estsummarywithoutprice")%>>
                    &nbsp; Summary &#45; Sales Estimate Order Details </label>
                    
                    <TH WIDTH="20%" ALIGN = "LEFT">
                    <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SalesEstimate" value="summarylnkestsummarywhoutprice" <%=disabled%> <%=checkOption("summarylnkestsummarywhoutprice")%>>
                    &nbsp; Summary &#45; Sales Estimate Order Number Link </label>
                        
                     <TR>
                    
                    <TH WIDTH="20%" ALIGN = "LEFT">
                   <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;" name="SalesEstimate" value="estsummary" <%=disabled%> <%=checkOption("estsummary")%>>
                    &nbsp; Summary &#45; Sales Estimate Order Details (with price)</label>
                    
                    <TH WIDTH="20%" ALIGN = "LEFT">
                   <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;" name="SalesEstimate" value="summarylnkestsummary" <%=disabled%> <%=checkOption("summarylnkestsummary")%>>
                    &nbsp; Summary &#45; Sales Estimate Order Number Link (with price)</label>                    
                                              
                    <%-- <TH WIDTH="20%" ALIGN = "LEFT">
                   <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;" name="SalesEstimate" value="maintest" <%=disabled%> <%=checkOption("maintest")%>>
                    &nbsp;  Edit Sales Estimate Order Details</label> --%>
                    
                    <TH WIDTH="20%" ALIGN = "LEFT">
                    <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SalesEstimate" value="convertOB" <%=disabled%> <%=checkOption("convertOB")%>>
                    &nbsp;  Sales Estimate &#45; Convert to Sales</label>
                    
                   <TR>
                    
	                                  
                   </TABLE>
                   </div>
                  </div>
                  
<div class="panel panel-default">
<div class="panel-heading" style="background: #eaeafa"><strong>Sales Estimate Reports</strong></div>
<div class="panel-body">
    <TABLE class="table1" style="font-size:14px;width: 100%;">
            <TR>
            <TH WIDTH="20%" ALIGN = "LEFT">
	               <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;" name="SalesEstimateReports" value="estordsmrywithoutprice" <%=disabled%> <%=checkOption("estordsmrywithoutprice")%> >
	                &nbsp;  Sales Estimate Order Summary Details</label>
	                </TH>
	                        
	                <TH WIDTH="20%" ALIGN = "LEFT">
	               <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;" name="SalesEstimateReports" value="estordsmrywissprice" <%=disabled%> <%=checkOption("estordsmrywissprice")%> >
	                &nbsp;  Sales Estimate Order Summary Details(by price)</label>
	                </TH>

					<!-- Don't Remove Sales Counter, Temp. Blocked on 12.1.22 By Azees-->
					<!-- <TH WIDTH="20%" ALIGN = "LEFT">
	               <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;" name="SalesEstimateReports" value="summaryalternatbrandproduct"  <%=disabled%> <%=checkOption("summaryalternatbrandproduct")%> >
	                &nbsp; Sales Counter</label>
                    </TH>
                                                           
                    	<TH WIDTH="20%" ALIGN = "LEFT">
		                <label class="checkbox-inline">    <INPUT Type=Checkbox  style="border:0;" name="SalesEstimateReports" value="salescounterconvtoest" <%=disabled%> <%=checkOption("salescounterconvtoest")%> >
		                    &nbsp; Sales Counter Convert To Estimate</label>
	                    </TH>					
                    
                    <TR>
                        
                     	<TH WIDTH="20%" ALIGN = "LEFT">
			              <label class="checkbox-inline">   <INPUT Type=Checkbox  style="border:0;" name="SalesEstimateReports" value="salescounterconvtoinvc" <%=disabled%> <%=checkOption("salescounterconvtoinvc")%> >
			                 &nbsp; Sales Counter Convert To Invoice</label>
	                 	</TH>
	                 </TR> -->
     </TABLE>
                   </div>
                  </div>                          
                  
                  <div class="panel panel-default">
    <div class="panel-heading" style="background: #eaeafa"><strong>Sales Order</strong></div>
    <div class="panel-body">
    <TABLE class="table1">
    
    				<TR>
    				
    				<TH WIDTH="20%" ALIGN = "LEFT">
                    <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SalesOrder" value="summarysalesorder" <%=disabled%> <%=checkOption("summarysalesorder")%>>
                    &nbsp; Sales Order</label>
	                 
                    <TH WIDTH="20%" ALIGN = "LEFT">
                     <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SalesOrder" value="newsalesorder" <%=disabled%> <%=checkOption("newsalesorder")%>>
                     &nbsp; New</label>
                     
                    <TH WIDTH="20%" ALIGN = "LEFT">
	                 <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SalesOrder" value="summarylnksalesorder" <%=disabled%> <%=checkOption("summarylnksalesorder")%> >
	                 &nbsp; Summary &#45; Order Number Link</label>
	                 
                     <TH WIDTH="20%" ALIGN = "LEFT">
                     <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SalesOrder" value="exportforsalesorder" <%=disabled%> <%=checkOption("exportforsalesorder")%>>
                     &nbsp; Export</label>
                                             
                     <TR>
                    
                     <TH WIDTH="20%" ALIGN = "LEFT">
                     <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SalesOrder" value="editsalesorder" <%=disabled%> <%=checkOption("editsalesorder")%>>
                     &nbsp; Edit</label>
                        
                    <TH WIDTH="20%" ALIGN = "LEFT">
                     <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SalesOrder" value="printsalesorder" <%=disabled%> <%=checkOption("printsalesorder")%>>
                     &nbsp; PDF/Print</label> 
                       
                    <TH WIDTH="20%" ALIGN = "LEFT">
                     <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SalesOrder" value="moresalesorder" <%=disabled%> <%=checkOption("moresalesorder")%>>
                     &nbsp; More</label>
    				
    				<TH WIDTH="20%" ALIGN = "LEFT">
                    <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SalesOrder" value="printOB" <%=disabled%> <%=checkOption("printOB")%>>
                    &nbsp; Summary &#45; Sales Order</label>                    
                                   
                    <TR>                     
                    
                     <TH WIDTH="20%" ALIGN = "LEFT">
                    <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SalesOrder" value="summarylnkprintOB" <%=disabled%> <%=checkOption("summarylnkprintOB")%>>
                    &nbsp; Summary &#45; Sales Order Number Link</label>
                    
                    <TH WIDTH="20%" ALIGN = "LEFT">
                    <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SalesOrder" value="PrintOBInvoice" <%=disabled%> <%=checkOption("PrintOBInvoice")%>>
                    &nbsp; Summary &#45; Sales Order (with price)</label>
                    
                    <TH WIDTH="20%" ALIGN = "LEFT">
                    <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SalesOrder" value="summarylnkPrintOBInvoice" <%=disabled%> <%=checkOption("summarylnkPrintOBInvoice")%>>
                    &nbsp; Summary &#45; Sales Order Number Link (with price)</label>
                
                    <TH WIDTH="20%" ALIGN = "LEFT">
                    <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SalesOrder" value="obsummary" <%=disabled%> <%=checkOption("obsummary")%>>
                    &nbsp; Summary &#45; Sales Order Details </label>                  
				                       
                    <TR>
                    
                    <TH WIDTH="20%" ALIGN = "LEFT">
                    <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SalesOrder" value="obsummaryprice" <%=disabled%> <%=checkOption("obsummaryprice")%>>
                    &nbsp; Summary &#45; Sales Order Details (with price)</label>
                    
                    <TH WIDTH="20%" ALIGN = "LEFT">
                    <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SalesOrder" value="orderCloseSales" <%=disabled%> <%=checkOption("orderCloseSales")%>>
                    &nbsp; Close Outstanding Sales Order</label>  
    </TABLE>
    </div>
 </div>

<div class="panel panel-default">
<div class="panel-heading" style="background: #eaeafa"><strong>Sales Transaction</strong></div>
<div class="panel-body">
       <TABLE class="table1" style="font-size:14px;width: 100%;">
       						
       						<TR>
       						
       								<th WIDTH="20%" ALIGN = "LEFT">
	                        <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SalesTransaction" value="salesTransactionDashboard"   <%=disabled%> <%=checkOption("salesTransactionDashboard")%>>
	                        &nbsp;  Sales Transaction</label>
       						
       						<TH WIDTH="20%" ALIGN = "LEFT">
	                        <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SalesTransaction" value="outbndmulPicking"   <%=disabled%> <%=checkOption("outbndmulPicking")%>>
	                        &nbsp;  Sales Order Pick</label> 
	                        
	                        <TH WIDTH="20%" ALIGN = "LEFT">
	                        <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SalesTransaction" value="obpickingbyRange"   <%=disabled%> <%=checkOption("obpickingbyRange")%>>
	                        &nbsp;  Sales Order Pick (by serial)</label>
	                        
	                        <TH WIDTH="20%" ALIGN = "LEFT">
	                        <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SalesTransaction" value="outbndIssuing" <%=disabled%> <%=checkOption("outbndIssuing")%>>
	                        &nbsp; Sales Order Issue</label>
	                        
	                            <TR>
	                            
	                        <TH WIDTH="20%" ALIGN = "LEFT">
                            <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SalesTransaction" value="obpickissuebulk" <%=disabled%> <%=checkOption("obpickissuebulk")%>>
                            &nbsp;  Sales Order Pick & Issue </label>
                            
                                             
                         
                           <TH WIDTH="20%" ALIGN = "LEFT">
                           <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SalesTransaction" value="obpickissuebyprd" <%=disabled%> <%=checkOption("obpickissuebyprd")%>>
                           &nbsp; Sales Order Pick/Issue (by product)</label>                            
                           
	                        <TH WIDTH="20%" ALIGN = "LEFT">
	                        <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SalesTransaction" value="obpickissuemultiple" <%=disabled%> <%=checkOption("obpickissuemultiple")%>>
	                        &nbsp;  Sales Order Pick & Issue (multiple)	 </label>                                              
	                      
		                    <TH WIDTH="20%" ALIGN = "LEFT">
		                    <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SalesTransaction" value="outbndPkrev"   <%=disabled%> <%=checkOption("outbndPkrev")%>>
		                    &nbsp; Sales Order Pick Return</label>
	                        <TR>
	                         <%-- <TH WIDTH="20%" ALIGN = "LEFT">
		                     <INPUT Type=Checkbox  style="border:0;" name="SalesTransaction" value="obissuereversal" <%=disabled%> <%=checkOption("obissuereversal")%>>
		                     &nbsp;  Sales Order Pick & Issue Reversal --%>
		                    
		                    <TH WIDTH="20%" ALIGN = "LEFT">
		                   <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SalesTransaction" value="mulmiscissue"   <%=disabled%> <%=checkOption("mulmiscissue")%>>
		                    &nbsp; Goods Issue</label>
	                        
	                    
				  		    
		                   <TH WIDTH="20%" ALIGN = "LEFT">
		                   <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SalesTransaction" value="ubmiscissueByRange"   <%=disabled%> <%=checkOption("ubmiscissueByRange")%>>
		                   &nbsp; Goods Issue (by serial)</label>
		                   
		                   <%-- <TH WIDTH="20%" ALIGN = "LEFT">
                    	   <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SalesTransaction" value="creatednpl" <%=disabled%> <%=checkOption("creatednpl")%>>
                    	   &nbsp; Create Sales Packing List/Deliver Note (PL/DN)</label> --%>
		                    
		                     <TH WIDTH="20%" ALIGN = "LEFT">
                        	<label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SalesTransaction" value="packingSummary" <%=disabled%> <%=checkOption("packingSummary")%> >
                        	&nbsp; Packing List and Deliver Note </label>
                        	</TH>
       </TABLE>
    </div>
 </div>
 
 <div class="panel panel-default">
<div class="panel-heading" style="background: #eaeafa"><strong>Sales Return</strong></div>
<div class="panel-body">
       <table class="table1" style="font-size:14px;width: 100%;">
                    <tr>

 					<th WIDTH="20%" ALIGN = "LEFT">
	                 <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SalesReturn" value="SOReturnSummary"  <%=disabled%> <%=checkOption("SOReturnSummary")%> >
	                 &nbsp; Sales Return</label>

	                <th WIDTH="20%" ALIGN = "LEFT">
	                 <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SalesReturn" value="newsalesreturn"  <%=disabled%> <%=checkOption("newsalesreturn")%> >
	                 &nbsp; New</label>

	                <th WIDTH="20%" ALIGN = "LEFT">
	                 <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SalesReturn" value="summarylnksalesreturn" <%=disabled%> <%=checkOption("summarylnksalesreturn")%> >
	                 &nbsp; Summary &#45; Return Number Link</label>

	                  <th WIDTH="20%" ALIGN = "LEFT">
	                 <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SalesReturn" value="exportsalesreturn"  <%=disabled%> <%=checkOption("exportsalesreturn")%> >
	                 &nbsp; Export</label>



	                 <tr>

	                 <th WIDTH="20%" ALIGN = "LEFT">
	                 <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SalesReturn" value="excelsalesreturn"  <%=disabled%> <%=checkOption("excelsalesreturn")%> >
	                 &nbsp; Export Excel</label>

	                <th WIDTH="20%" ALIGN = "LEFT">
	                 <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SalesReturn" value="applycreditnotesalesreturn"  <%=disabled%> <%=checkOption("applycreditnotesalesreturn")%> >
	                 &nbsp; Apply To Credit Note</label>

		</table>
     </div>
 </div>
 
 <div class="panel panel-default">
<div class="panel-heading" style="background: #eaeafa"><strong>Internal Product Return Receive</strong></div>
<div class="panel-body">
       <TABLE class="table1" style="font-size:14px;width: 100%;">
                    <TR>
                    
 					<TH WIDTH="20%" ALIGN = "LEFT">
	                 <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="ProductReturnReceive" value="productreceive" <%=disabled%> <%=checkOption("productreceive")%> >     
	                 &nbsp; Product Return Receive</label>
	                 
	                <TH WIDTH="20%" ALIGN = "LEFT">
	                 <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="ProductReturnReceive" value="summarylnkproductreceive" <%=disabled%> <%=checkOption("summarylnkproductreceive")%> >
	                 &nbsp; Summary &#45; Return Number Link</label>
	                 
	                 <TH WIDTH="20%" ALIGN = "LEFT">
	                 <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="ProductReturn" value="exportproductreceive" <%=disabled%> <%=checkOption("exportproductreceive")%> >     
	                 &nbsp; Export</label>
	                 
	                <TH WIDTH="20%" ALIGN = "LEFT">
	                 <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="ProductReturnReceive" value="goodsreceiptbyproductreceive" <%=disabled%> <%=checkOption("goodsreceiptbyproductreceive")%> >     
	                 &nbsp; Receive</label>
	                 
	                 <TR>
	                 
		</TABLE>
     </div>
 </div>
 
 
 <div class="panel panel-default">
<div class="panel-heading" style="background: #eaeafa"><strong>Sales Reports</strong></div>
<div class="panel-body">
       <TABLE class="table1" style="font-size:14px;width: 100%;">
                    <TR>
                    
                    <TH WIDTH="20%" ALIGN = "LEFT">
                        	<label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SalesReports" value="obordersumry" <%=disabled%> <%=checkOption("obordersumry")%> >
                        	&nbsp;  Sales Order Summary Details</label>
                        	</TH>
                        	
                        	<TH WIDTH="20%" ALIGN = "LEFT">
                        	<label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SalesReports"value="obsalessmry"  <%=disabled%> <%=checkOption("obsalessmry")%> >
                        	&nbsp;   Sales Order Sales Summary</label>
                            </TH>
                            
                        	<TH WIDTH="20%" ALIGN = "LEFT">
                        	<label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SalesReports" value="containerSummary" <%=disabled%> <%=checkOption("containerSummary")%> >
                        	&nbsp; Sales Order Summary(by container)</label>
                            </TH>
                                                        
                             <TH WIDTH="20%" ALIGN = "LEFT">
                        	<label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SalesReports" value="invWithIssueReturn" <%=disabled%> <%=checkOption("invWithIssueReturn")%> >
                        	&nbsp; Sales Order Summary(by customer) </label>
                           </TH>
	                        
	                        <TR>
                           
                           <TH WIDTH="20%" ALIGN = "LEFT">
                        	<label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SalesReports" value="obordersumrywissprice" <%=disabled%> <%=checkOption("obordersumrywissprice")%> >
                        	&nbsp;  Sales Order Summary Details(by price)</label>
                        	 </TH>                 	 
                        	
                        	<%-- <TH WIDTH="20%" ALIGN = "LEFT">
                        	<label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SalesReports" value="obordersumrywavgcost" <%=disabled%> <%=checkOption("obordersumrywavgcost")%> >
                        	&nbsp;  Sales Order Summary Details (by average cost)</label>
                       		</TH> --%>
                       		
                        	<TH WIDTH="20%" ALIGN = "LEFT">
                        	<label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SalesReports" value="issuedobsummry" <%=disabled%> <%=checkOption("issuedobsummry")%> >
                        	&nbsp; Sales Order Summary(by price)</label>
                           </TH> 
                           
                             <th WIDTH="20%" ALIGN = "LEFT">
	                        	<label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SalesReports" value="salesperformance"  <%=disabled%> <%=checkOption("salesperformance")%> >
	                        	&nbsp; Sales Performance Summary</label>
	                        </TH>                       
                            
	                        <TH WIDTH="20%" ALIGN = "LEFT">
	                        	<label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SalesReports" value="printdo"  <%=disabled%> <%=checkOption("printdo")%> >
	                        	&nbsp; Generate PDF (without price)</label>
	                        </TH>
	                        
	                         <TR>
	                        
	                         <TH WIDTH="20%" ALIGN = "LEFT">
	                        	<label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SalesReports" value="printinvoice"  <%=disabled%> <%=checkOption("printinvoice")%> >
	                        	&nbsp; Generate PDF (with price)</label>
	                       </TH>
	                        
	                         <TH WIDTH="20%" ALIGN = "LEFT">
	                        	<label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SalesReports" value="issuesummry" <%=disabled%> <%=checkOption("issuesummry")%> >     
	                        	&nbsp;  Order Issue Summary</label>
	                        </TH>	                           
	                         
	                        <TH WIDTH="20%" ALIGN = "LEFT">
	                        	<label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SalesReports" value="issuesummrywithprice"  <%=disabled%> <%=checkOption("issuesummrywithprice")%> >     
	                        	&nbsp;  Order Issue Summary (with price)</label>
	                        </TH>
       </TABLE>
    </div>
 </div>         
 
 
        <!-- EDIT BY NAVAS -->
                      
                      
         <div class="panel panel-default">
    <div class="panel-heading" style="background: #eaeafa"><strong>Consignment</strong></div>
    <div class="panel-body">
    <TABLE class="table1">
    
    				<TR>
    				
    				<TH WIDTH="20%" ALIGN = "LEFT">
                    <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="Consignment" value="summaryconsignment" <%=disabled%> <%=checkOption("summaryConsignment")%>>
                    &nbsp; Consignment</label>
	                 
                    <TH WIDTH="20%" ALIGN = "LEFT">
                     <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="Consignment" value="newconsignment" <%=disabled%> <%=checkOption("newConsignment")%>>
                     &nbsp; New</label>
                     
                    <TH WIDTH="20%" ALIGN = "LEFT">
	                 <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="Consignment" value="summarylnkconsignment" <%=disabled%> <%=checkOption("summarylnkConsignment")%> >
	                 &nbsp; Summary &#45; Consignment NumberLink</label>
                     
                     <TH WIDTH="20%" ALIGN = "LEFT">
                     <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="Consignment" value="exportforconsignment" <%=disabled%> <%=checkOption("exportforConsignment")%>>
                     &nbsp; Export</label>
                                             
                     <TR>
                    
                     <TH WIDTH="20%" ALIGN = "LEFT">
                     <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="Consignment" value="editconsignment" <%=disabled%> <%=checkOption("editConsignment")%>>
                     &nbsp; Edit</label>
                         
                    <TH WIDTH="20%" ALIGN = "LEFT">
                     <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="Consignment" value="printconsignment" <%=disabled%> <%=checkOption("printConsignment")%>>
                     &nbsp; PDF/Print</label> 
                       
                    <TH WIDTH="20%" ALIGN = "LEFT">
                     <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="Consignment" value="moreconsignment" <%=disabled%> <%=checkOption("moreConsignment")%>>
                     &nbsp; More</label>
    				
    				 <TH WIDTH="20%" ALIGN = "LEFT">
                    <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="Consignment" value="ConsignmentsummaryWO" <%=disabled%> <%=checkOption("ConsignmentsummaryWO")%>>
                    &nbsp; Summary &#45; Consignment Order Details </label>                  
				                       
                    <TR>
                    
                    <TH WIDTH="20%" ALIGN = "LEFT">
                    <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="Consignment" value="Consignmentsummaryprice" <%=disabled%> <%=checkOption("Consignmentsummaryprice")%>>
                    &nbsp; Summary &#45; Consignment Order Details (with price)</label>
    				
    				<TH WIDTH="20%" ALIGN = "LEFT">
	                 <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="Consignment" value="orderCloseTransfer" <%=disabled%> <%=checkOption("orderCloseTransfer")%> >
	                 &nbsp; Close Outstanding Consignment Orders</label>
    </TABLE>
    </div>
 </div>
                      
			<div class="panel panel-default">
		    <div class="panel-heading" style="background: #eaeafa"><strong>Consignment Transaction</strong></div>
		    <div class="panel-body">
		    <TABLE class="table1">
    
    				<TR>
    				
    				<TH WIDTH="20%" ALIGN = "LEFT">
                    <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="RentalTransaction" value="bulkPickReceiveTO" <%=disabled%> <%=checkOption("bulkPickReceiveTO")%>>
                    &nbsp; Consignment Order Pick & Issue</label>
	                 
                    <TH WIDTH="60%" ALIGN = "LEFT">
                     <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="RentalTransaction" value="toreversal" <%=disabled%> <%=checkOption("toreversal")%>>
                     &nbsp; Consignment Order Reversal</label>
                     
                    
    				
    </TABLE>
    </div>
 </div>   
 
 <div class="panel panel-default">
    <div class="panel-heading" style="background: #eaeafa"><strong>Consignment Reports</strong></div>
    <div class="panel-body">
    <TABLE class="table1">
    
    				<TR>
    				
    				<TH WIDTH="20%" ALIGN = "LEFT">
                    <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="ConsignmentReports" value="consignmentsummrywithoutprice" <%=disabled%> <%=checkOption("consignmentsummrywithoutprice")%>>
                    &nbsp; Consignment Summary Details</label>
	                 
                    <TH WIDTH="20%" ALIGN = "LEFT">
                     <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="ConsignmentReports" value="consignmentsummrywithprice" <%=disabled%> <%=checkOption("consignmentsummrywithprice")%>>
                     &nbsp; Consignment Summary Details (With Price)</label>
                     
                    <TH WIDTH="20%" ALIGN = "LEFT">
	                 <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="ConsignmentReports" value="printto" <%=disabled%> <%=checkOption("printto")%> >
	                 &nbsp; Generate PDF (without price)</label>
	                 
	                <TH WIDTH="20%" ALIGN = "LEFT">
	                 <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="ConsignmentReports" value="printtowithprice" <%=disabled%> <%=checkOption("printtowithprice")%> >
	                 &nbsp; Generate PDF (with price)</label>
    				
    </TABLE>
    </div>
 </div>                                           
                  
                       
<%-- <div class="panel panel-default">
<div class="panel-heading" style="background: #eaeafa"><strong>Rental / Consignment</strong></div>
<div class="panel-body">
     <TABLE class="table1" style="font-size:14px;width: 100%;">
                 <TR>
                 
                 <TH WIDTH="20%" ALIGN = "LEFT">
                   <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;" name="OrderManagement1" value="lnordsummry" <%=disabled%> <%=checkOption("lnordsummry")%>>
                    &nbsp; Summary &#45; Rental Order Details</label>
                  
					<TH WIDTH="20%" ALIGN = "LEFT">
                    <INPUT Type=Checkbox  style="border:0;" name="OrderManagement1" value="loanOrderSummaryWithPrice" <%=disabled%> <%=checkOption("loanOrderSummaryWithPrice")%>>
                    &nbsp; Summary &#45; Rental Order With Price Details
                  
                    <TH WIDTH="20%" ALIGN = "LEFT"> 
                   <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;" name="OrderManagement1" value="maintlnord" <%=disabled%> <%=checkOption("maintlnord")%>>
                    &nbsp;  Edit Rental Order Details </label>
                               
                    <TH WIDTH="20%" ALIGN = "LEFT">
                   <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;" name="OrderManagement1" value="cratlnord" <%=disabled%> <%=checkOption("cratlnord")%>>
                    &nbsp; Create Rental Order Details</label>
                    					
					<TH WIDTH="20%" ALIGN = "LEFT">
                  <label class="checkbox-inline">  <INPUT Type=Checkbox  style="border:0;" name="OrderManagement1" value="orderCloseRental" <%=disabled%> <%=checkOption("orderCloseRental")%>>
                    &nbsp; Close Outstanding Rental Order</label>
                    
                    <TR>					
                                        
                    <TH WIDTH="20%" ALIGN = "LEFT">
                  <label class="checkbox-inline">  <INPUT Type=Checkbox  style="border:0;" name="OrderManagement1" value="trordsummry" <%=disabled%> <%=checkOption("trordsummry")%>>
                    &nbsp; Summary &#45; Consignment Order Details</label>					
                    
                    <TH WIDTH="20%" ALIGN = "LEFT">  
                  <label class="checkbox-inline">  <INPUT Type=Checkbox  style="border:0;" name="OrderManagement1" value="mainttrord" <%=disabled%> <%=checkOption("mainttrord")%>>
                    &nbsp; Edit Consignment Order Details  </label>                  
                  
                    <TH WIDTH="20%" ALIGN = "LEFT">
                  <label class="checkbox-inline">  <INPUT Type=Checkbox  style="border:0;" name="OrderManagement1" value="crattrord" <%=disabled%> <%=checkOption("crattrord")%>>
                    &nbsp; Create Consignment Order Details</label>
					
					<TH WIDTH="20%" ALIGN = "LEFT">
                  <label class="checkbox-inline">  <INPUT Type=Checkbox  style="border:0;" name="OrderManagement1" value="orderCloseTransfer" <%=disabled%> <%=checkOption("orderCloseTransfer")%>>
                    &nbsp; Close Outstanding Consignment Order</label>
                    
                    <TR>					
					
					<TH WIDTH="20%" ALIGN = "LEFT">
                   <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;" name="URL" value="lnordReceiving"   <%=disabled%> <%=checkOption("lnordReceiving")%>>
                    &nbsp;Rental Order Receipt   </label>                                     
                
                    <TH WIDTH="20%" ALIGN = "LEFT">
		          <label class="checkbox-inline">  <INPUT Type=Checkbox  style="border:0;" name="URL" value="lnordPicking"  <%=disabled%> <%=checkOption("lnordPicking")%>>
		            &nbsp;  Rental Order Pick & Issue  </label>                          
		                 
		            <TH WIDTH="20%" ALIGN = "LEFT">
		          <label class="checkbox-inline">  <INPUT Type=Checkbox  style="border:0;" name="URL" value="bulkPickReceiveTO" <%=disabled%> <%=checkOption("bulkPickReceiveTO")%>>
		            &nbsp;  Consignment Order Pick & Issue</label>
		            		               		                  		                  
	                <TH WIDTH="20%" ALIGN = "LEFT">
	              <label class="checkbox-inline">  <INPUT Type=Checkbox  style="border:0;" name="URL" value="toreversal" <%=disabled%> <%=checkOption("toreversal")%> >
	                &nbsp;  Consignment Order Reversal</label>
		            
		            <TR>
	                
	                <TH WIDTH="20%" ALIGN = "LEFT">
	                          <INPUT Type=Checkbox  style="border:0;" name="Reports" value=LoanOrderSummaryByPrice <%=disabled%> <%=checkOption("LoanOrderSummaryByPrice")%> >
	                          &nbsp;  Rental Order Details Summary With Price
	                          </TH> 	
	                
	                		  <TH WIDTH="20%" ALIGN = "LEFT">
	                          <INPUT Type=Checkbox  style="border:0;" name="Reports" value="RentalOrderSummary" <%=disabled%> <%=checkOption("RentalOrderSummary")%> >
	                          &nbsp;  Rental Order Details Summary
	                          </TH>	                                     		 
	                         
	                          <TH WIDTH="20%" ALIGN = "LEFT">
	                       <label class="checkbox-inline">   <INPUT Type=Checkbox  style="border:0;" name="Reports" value="printloanorder" <%=disabled%> <%=checkOption("printloanorder")%> >
	                          &nbsp;  Rental Order Details</label>
	                          </TH>
	                          
	                          <TH WIDTH="20%" ALIGN = "LEFT">
	                          <INPUT Type=Checkbox  style="border:0;" name="Reports" value="printLoanOrderInvoice" <%=disabled%> <%=checkOption("printLoanOrderInvoice")%> >
	                          &nbsp;  Rental Order Details Summary By Price
	                          </TH>
                                                      
                            <TH WIDTH="20%" ALIGN = "LEFT">
                        	<label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;" name="Reports"value="printto"  <%=disabled%> <%=checkOption("printto")%> >
                        	&nbsp;   Consignment Order Details</label>
                            </TH>
                 
                 </TABLE>
                    </div> 
                     </div>  --%>                    
<div class="panel panel-default">
<div class="panel-heading" style="background: #eaeafa"><strong>In House</strong></div>
<div class="panel-body">
     <TABLE class="table1" style="font-size:14px;width: 100%;">
                 <TR>
                    
                        
                      <TH WIDTH="20%" ALIGN = "LEFT">
                      <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;" name="URL" value="mulloctransfer" <%=disabled%> <%=checkOption("mulloctransfer")%>>  
                         &nbsp; Stock Move</label>
                         
                          <%-- NAME: Thansith --%>
                          <TH WIDTH="20%" ALIGN = "LEFT">
                      <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;" name="URL" value="newProcessingReceive" <%=disabled%> <%=checkOption("newProcessingReceive")%>>  
                         &nbsp; De-Kitting</label>
                         
                           <TH WIDTH="20%" ALIGN = "LEFT">
                         <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="URL" value="summarySemiFinished" <%=disabled%> <%=checkOption("summarySemiFinished")%>>  
                         &nbsp; Kitting</label>
                         
                         <TH WIDTH="20%" ALIGN = "LEFT">
                        <label class="checkbox-inline">	<INPUT Type=Checkbox  style="border:0;" name="Reports" value="manualstocktake"   <%=disabled%> <%=checkOption("manualstocktake")%> >
                        	&nbsp; Stock Take </label></TH>
                        	
                        	<TR>
                        	
                        	<TH WIDTH="20%" ALIGN = "LEFT">
                        	<label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="InHouse" value="showavlqtyonstk"   <%=disabled%> <%=checkOption("showavlqtyonstk")%> >
                        	&nbsp; Show Available Qty on Stock Take</label> </TH>
                         
                          <%--   END --%>
                         <TH WIDTH="20%" ALIGN = "LEFT">
                        <label class="checkbox-inline">	<INPUT Type=Checkbox  style="border:0;" name="Reports" value="rptPSVsINV"   <%=disabled%> <%=checkOption("rptPSVsINV")%> >
                        	&nbsp; Stock Take Summary </label></TH>
                        	
                       		<TH WIDTH="20%" ALIGN = "LEFT">
                        	 <label class="checkbox-inline">   <INPUT Type=Checkbox  style="border:0;" name="Reports" value="stocktakeWithPrice"  <%=disabled%> <%=checkOption("stocktakeWithPrice")%> >
                            &nbsp; Stock Take Summary (with price)</label>
                        	</TH>
                        	
                     	    <TH WIDTH="20%" ALIGN = "LEFT">
                        	<label class="checkbox-inline">	<INPUT Type=Checkbox  style="border:0;" name="Reports" value="stkreset"   <%=disabled%> <%=checkOption("stkreset")%> >
                        	&nbsp; Stock Take Reset</label> </TH>
                       
                        	<%-- <TH WIDTH="20%" ALIGN = "LEFT">
                       		<label class="checkbox-inline"> 	<INPUT Type=Checkbox  style="border:0;" name="Reports" value="kitdekitwithbom"   <%=disabled%> <%=checkOption("kitdekitwithbom")%> >
                        	&nbsp; Kitting/De-Kitting Summary</label>
                        	
                        	<TH WIDTH="20%" ALIGN = "LEFT">
                      		<label class="checkbox-inline">  <INPUT Type=Checkbox  style="border:0;" name="Reports" value="kitdekitwithbomnew" <%=disabled%> <%=checkOption("kitdekitwithbomnew")%>>
                       		 &nbsp; Kitting De-Kitting New</label>
                        	</TH>
                        	
                        	<TR>
                        	
                       		 <TH WIDTH="20%" ALIGN = "LEFT">
                     		 <label class="checkbox-inline">  <INPUT Type=Checkbox  style="border:0;" name="Reports" value="kitdekitwithbomedit" <%=disabled%> <%=checkOption("kitdekitwithbomedit")%>>
                     		   &nbsp; Kitting De-Kitting Edit</label>
                        	</TH>
                        	
                        	
                        	<TH WIDTH="20%" ALIGN = "LEFT">
                     	  	<label class="checkbox-inline"> 	<INPUT Type=Checkbox  style="border:0;" name="Reports" value="kitdekitwithbomsummry"   <%=disabled%> <%=checkOption("kitdekitwithbomsummry")%> >
                        	&nbsp; Summary - Kitting/De-Kitting</label></TH> --%>
                        	
                        	<TR>
                         
                        	 <TH WIDTH="20%" ALIGN = "LEFT">
                    		 <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;" name="Reports" value="generateBarcode" <%=disabled%> <%=checkOption("generateBarcode")%>>  
                        	 &nbsp; Generate Barcode</label>
                         
                         	<TH WIDTH="20%" ALIGN = "LEFT">
                       		 <label class="checkbox-inline">	<INPUT Type=Checkbox  style="border:0;" name="Reports" value="generateReceiptBarcode"   <%=disabled%> <%=checkOption("generateReceiptBarcode")%> >
                        	&nbsp; Generate Receipt Barcode</label></TH>
                        	
                       		<TH WIDTH="20%" ALIGN = "LEFT">
                         	<label class="checkbox-inline">   <INPUT Type=Checkbox  style="border:0;" name="Reports" value="generateLocBarcode"  <%=disabled%> <%=checkOption("generateLocBarcode")%> >
                            &nbsp; Generate Location Barcode</label>
                        	</TH>
                        	
                       		<TH WIDTH="20%" ALIGN = "LEFT">
                         	<label class="checkbox-inline">   <INPUT Type=Checkbox  style="border:0;" name="Reports" value="genproductbarcode"  <%=disabled%> <%=checkOption("genproductbarcode")%> >
                            &nbsp; Generate Product Barcode</label>
                        	</TH>
                        	
                        	<TR>
                        	
                       <TH WIDTH="20%" ALIGN = "LEFT">
                         	<label class="checkbox-inline">   <INPUT Type=Checkbox  style="border:0;" name="Reports" value="generateManualBarcode"  <%=disabled%> <%=checkOption("generateManualBarcode")%> >
                            &nbsp;Generate Manual Barcode </label>
                        	</TH>
                        	
                        	<%-- 	<TR>
                      
                        
                 
                        <TH WIDTH="20%" ALIGN = "LEFT">
                    <label class="checkbox-inline">    <INPUT Type=Checkbox  style="border:0;" name="URL" value="kitdekit" <%=disabled%> <%=checkOption("kitdekit")%>>
                        &nbsp; Kitting De-Kitting</label>
                        
                        <TH WIDTH="20%" ALIGN = "LEFT">
                      <label class="checkbox-inline">  <INPUT Type=Checkbox  style="border:0;" name="URL" value="bulkkitdekit" <%=disabled%> <%=checkOption("bulkkitdekit")%>>
                        &nbsp; Kitting De-Kitting(bulk with ref BOM) </label>                       
                                               	 
                                         --%>
              
                    </TABLE>
                    </div>
                     </div>
<div class="panel panel-default">
<div class="panel-heading" style="background: #eaeafa"><strong>Inventory Reports</strong></div>
<div class="panel-body">
           <TABLE >
                 <TR>
                    
                         <TH WIDTH="20%" ALIGN = "LEFT">
	                     <label class="checkbox-inline">   	<INPUT Type=Checkbox  style="border:0;" name="Reports" value="viewinventoryprodmultiuom"   <%=disabled%> <%=checkOption("viewinventoryprodmultiuom")%>>
	                        	&nbsp; Inventory Summary With Total Quantity</label>
                         </TH>
                         
                          <TH WIDTH="20%" ALIGN = "LEFT">
	                     <label class="checkbox-inline">   	<INPUT Type=Checkbox  style="border:0;" name="Reports" value="sumqtybyproduct"   <%=disabled%> <%=checkOption("sumqtybyproduct")%>>
	                        	&nbsp; Inventory Summary With Total Quantity (group by product)</label>
                         </TH>
							  						                 
                         <TH WIDTH="20%" ALIGN = "LEFT">
	                      <label class="checkbox-inline">  	<INPUT Type=Checkbox  style="border:0;" name="Reports" value="viewinventorybatchmultiuom"   <%=disabled%> <%=checkOption("viewinventorybatchmultiuom")%>>
	                        	&nbsp; Inventory Summary With Batch/Sno </label>
                         </TH>  
                    
                        <TH WIDTH="20%" ALIGN = "LEFT">
                        <label class="checkbox-inline">	<INPUT Type=Checkbox  style="border:0;" name="Reports" value="viewInventoryByProd"  <%=disabled%> <%=checkOption("viewInventoryByProd")%>>
                        	&nbsp; Inventory Summary With Total Quantity (with pcs)</label>
                        </TH> 
                        
                         <TR>
                        
                         <th WIDTH="20%" ALIGN = "LEFT">
                        	<label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="Reports" value="valuationsummary"  <%=disabled%> <%=checkOption("valuationsummary")%>>
                        	&nbsp; Inventory Valuation Summary With Total Quantity</label>
                        </TH> 
                         
                         <th WIDTH="20%" ALIGN = "LEFT">
                        	<label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="Reports" value="revenueshortage"  <%=disabled%> <%=checkOption("revenueshortage")%>>
                        	&nbsp; Revenue Risk Due to Inventory Shortage Summary</label>
                        </TH> 
                       
                        <TH WIDTH="20%" ALIGN = "LEFT">
                        <label class="checkbox-inline">	<INPUT Type=Checkbox  style="border:0;" name="Reports" value="msInvnopriceqty"   <%=disabled%> <%=checkOption("msInvnopriceqty")%>>
                        	&nbsp; Inventory Summary With Batch/Sno (with pcs)</label>
                        </TH>
                         
                       <TH WIDTH="20%" ALIGN = "LEFT">
                         <label class="checkbox-inline">   <INPUT Type=Checkbox  style="border:0;" name="Reports" value="msInvList"  <%=disabled%> <%=checkOption("msInvList")%>>
                            &nbsp; Inventory Summary (with min/max/zero qty) </label>
                       </TH> 
                        
                         <TR>
                    
                         
                    	<TH WIDTH="20%" ALIGN = "LEFT">
                        	<label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="Reports" value="msInvListwithExpireD"   <%=disabled%> <%=checkOption("msInvListwithExpireD")%>>
                        	&nbsp; Inventory Summary (with expiry date)</label>
                        </TH> 
                           
                    	<TH WIDTH="20%" ALIGN = "LEFT">
                        	<label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="Reports" value="invavalqty"   <%=disabled%> <%=checkOption("invavalqty")%>>
                        	&nbsp; Inventory Summary Available Quantity</label>
                        </TH> 
                      
                       <TH WIDTH="20%" ALIGN = "LEFT">
                           <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;" name="Reports" value="inventoryAgingSummary"  <%=disabled%> <%=checkOption("inventoryAgingSummary")%>>
                            &nbsp; Inventory Aging Summary </label>
                       </TH>
                        
                        <TH WIDTH="20%" ALIGN = "LEFT">
                            <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="Reports" value="invincomingoutgoingqty"  <%=disabled%> <%=checkOption("invincomingoutgoingqty")%>>
                            &nbsp; Inventory Summary (with goods receipt / goods issue)</label>
                       </TH> 
                        
                         <TR>
                       
                        <TH WIDTH="20%" ALIGN = "LEFT">
                            <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="Reports" value="costpriceinv" <%=disabled%> <%=checkOption("costpriceinv")%> >
                            &nbsp; Inventory Summary (With Purchase Cost / Sales Price)</label>
                       </TH> 
                       
                         <TH WIDTH="20%" ALIGN = "LEFT">
                           <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;" name="Reports" value="invopenclosestock"  <%=disabled%> <%=checkOption("invopenclosestock")%>>
                            &nbsp; Inventory Summary Opening/Closing Stock  </label>
                       </TH>
                       
                         <TH WIDTH="20%" ALIGN = "LEFT">
                           <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;" name="Reports" value="invopenclosestockavgcost"  <%=disabled%> <%=checkOption("invopenclosestockavgcost")%>>
                            &nbsp; Inventory Summary Opening/Closing Stock (with average cost)</label>
                       </TH>
                       
	                    <TH WIDTH="20%" ALIGN = "LEFT">
	                         <label class="checkbox-inline">   <INPUT Type=Checkbox  style="border:0;" name="Reports" value="msInvListwithcost"  <%=disabled%> <%=checkOption("msInvListwithcost")%>>
	                     &nbsp; Inventory Summary (with average cost)</label>
	                     </TH> 
                        
                         <TR> 
	                     
	                     <TH WIDTH="20%" ALIGN = "LEFT">
	                            <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="Reports" value="stkmovhis"  <%=disabled%> <%=checkOption("stkmovhis")%>>
	                     &nbsp; Inventory Movement Report</label>
	                     </TH> 
                         
                             <TH WIDTH="20%" ALIGN = "LEFT">
                            <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="Reports" value="salesforecasting"  <%=disabled%> <%=checkOption("salesforecasting")%>>
                            &nbsp; Sales Forecasting  </label>
                       </TH>           
                                 
                        </TABLE>
                   </div>
                          </div>
 <% if(ENABLE_POS.equals("1")) { %>                  
<div class="panel panel-default">
<div class="panel-heading" style="background: #eaeafa"><strong>POS Reports</strong></div>
<div class="panel-body">
           <TABLE>           
          			 <TR>
           				 <TH WIDTH="20%" ALIGN = "LEFT">
	     				 <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;" name="PosReports" value="possalesreports" <%=disabled%> <%=checkOption("possalesreports")%>>
	                     &nbsp; Sales Report</label>
	                     </TH>
	                     
						<TH WIDTH="20%" ALIGN = "LEFT">
	     				 <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;" name="PosReports" value="posvoidsales" <%=disabled%> <%=checkOption("posvoidsales")%>>
	                     &nbsp; Void Sales Report</label>
	                     </TH>
	                     
						<TH WIDTH="20%" ALIGN = "LEFT">
	     				 <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;" name="PosReports" value="posreturn" <%=disabled%> <%=checkOption("posreturn")%>>
	                     &nbsp; Return Sales Report</label>
	                     </TH>
	                     
						<TH WIDTH="20%" ALIGN = "LEFT">
	     				 <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;" name="PosReports" value="posdiscount" <%=disabled%> <%=checkOption("posdiscount")%>>
	                     &nbsp; Discount</label>
	                     </TH>
	                 <TR>
						<TH WIDTH="20%" ALIGN = "LEFT">
	     				 <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;" name="PosReports" value="posFOC" <%=disabled%> <%=checkOption("posFOC")%>>
	                     &nbsp; FOC</label>
	                     </TH>
	                     
						<TH WIDTH="20%" ALIGN = "LEFT">
	     				 <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;" name="PosReports" value="posexpenses" <%=disabled%> <%=checkOption("posexpenses")%>>
	                     &nbsp; Expense</label>
	                     </TH>
	                     
						<TH WIDTH="20%" ALIGN = "LEFT">
	     				 <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;" name="PosReports" value="posshiftclose" <%=disabled%> <%=checkOption("posshiftclose")%>>
	                     &nbsp; Shift Close</label>
	                     </TH>               
						<TH WIDTH="20%" ALIGN = "LEFT">
	     				 <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;" name="PosReports" value="posrevenuereports" <%=disabled%> <%=checkOption("posrevenuereports")%>>
	                     &nbsp; POS Revenue Report</label>
	                     </TH>               


      </TABLE>
         </div>
          </div>
  <%}%>
  
<div class="panel panel-default">
    <div class="panel-heading" style="background: #eaeafa"><strong>Integrations</strong></div>
    <div class="panel-body">
		<TABLE >
                    <TR>
                    
                    <TH WIDTH="20%" ALIGN = "LEFT">
	                        <label class="checkbox-inline">	<INPUT Type=Checkbox  style="border:0;" name="integrations_shopping_cart" value="integrations_shopping_cart"  <%=disabled%> <%=checkOption("integrations_shopping_cart")%>>
	                        	&nbsp; Shopping Cart</label>
                         </TH>
                         
                         <TH WIDTH="20%" ALIGN = "LEFT">
	                        <label class="checkbox-inline">	<INPUT Type=Checkbox  style="border:0;" name="integrations_ecommerce" value="integrations_ecommerce"  <%=disabled%> <%=checkOption("integrations_ecommerce")%>>
	                        	&nbsp; eCommerce</label>
                         </TH>
                    
                    </TABLE>
    </div>
</div>

<div class="panel panel-default">
    <div class="panel-heading" style="background: #eaeafa"><strong>Activity Logs</strong></div>
    <div class="panel-body">
		<TABLE >
                    <TR>
                    
                    <TH WIDTH="20%" ALIGN = "LEFT">
	                        <label class="checkbox-inline">	<INPUT Type=Checkbox  style="border:0;" name="ActivityLogs" value="msPOList"  <%=disabled%> <%=checkOption("msPOList")%>>
	                        	&nbsp; Activity Logs</label>
                         </TH>
                    
                    </TABLE>
    </div>
</div>


<div class="panel panel-default">
    <div class="panel-heading" style="background: #eaeafa"><strong>Owner App</strong></div>
    <div class="panel-body">
		<TABLE >
                    <TR>
                    
                    <TH WIDTH="20%" ALIGN = "LEFT">
	                        	<label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="OwnerApp" value="ownappOpenStkvalue"  <%=disabled%> <%=checkOption("ownappOpenStkvalue")%>>
	                        	&nbsp; Opening Stock Value</label>
                         </TH>
                    <TH WIDTH="20%" ALIGN = "LEFT">
	                        	<label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="OwnerApp" value="ownClsStkvalue" <%=disabled%> <%=checkOption("ownClsStkvalue")%> >
	                        	&nbsp; Closing Stock Value</label>
                         </TH>
                    <TH WIDTH="20%" ALIGN = "LEFT">
	                        	<label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="OwnerApp" value="ownappPurchase" <%=disabled%> <%=checkOption("ownappPurchase")%> >
	                        	&nbsp; Purchase</label>
                         </TH>
                         
                         <TH WIDTH="20%" ALIGN = "LEFT">
	                        	<label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="OwnerApp" value="ownappPurchaseapproval" <%=disabled%> <%=checkOption("ownappPurchaseapproval")%> >
	                        	&nbsp; Purchase Order Approval</label>
                         </TH>
                         
                         <TR>
                         
                    <TH WIDTH="20%" ALIGN = "LEFT">
	                        	<label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="OwnerApp" value="ownappErpsales" <%=disabled%> <%=checkOption("ownappErpsales")%> >
	                        	&nbsp; ERP Sales</label>
                         </TH>
                         
                    <TH WIDTH="20%" ALIGN = "LEFT">
	                        	<label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="OwnerApp" value="ownappPossales" <%=disabled%> <%=checkOption("ownappPossales")%> >
	                        	&nbsp; POS Sales</label>
                         </TH>
                    <TH WIDTH="20%" ALIGN = "LEFT">
	                        	<label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="OwnerApp" value="ownappProjectmanagment" <%=disabled%> <%=checkOption("ownappProjectmanagment")%> >
	                        	&nbsp; Project Management</label>
                         </TH>
                    
                    </TABLE>
    </div>
</div>

<%if(ISPEPPOL.equalsIgnoreCase("1")){ %>
<div class="panel panel-default">
    <div class="panel-heading" style="background: #eaeafa"><strong>Peppol Integration</strong></div>
    <div class="panel-body">
		<TABLE >
                    <TR>
                    
                    <TH WIDTH="20%" ALIGN = "LEFT">
	                        	<label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="PeppolIntegration" value="peppolregister"  <%=disabled%> <%=checkOption("peppolregister")%>>
	                        	&nbsp; Peppol ID Registration</label>
                         </TH>
                    <TH WIDTH="20%" ALIGN = "LEFT">
	                        	<label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="PeppolIntegration" value="peppolpurchase"  <%=disabled%> <%=checkOption("peppolpurchase")%>>
	                        	&nbsp; Download Purchase Invoice from Peppol</label>
                         </TH>
                    <TH WIDTH="20%" ALIGN = "LEFT">
	                        	<label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="PeppolIntegration" value="peppolsales"  <%=disabled%> <%=checkOption("peppolsales")%>>
	                        	&nbsp; Upload Sales Invoice To Peppol</label>
                         </TH>
                    <TH WIDTH="20%" ALIGN = "LEFT">
	                        	<label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="PeppolIntegration" value="peppolcustomer"  <%=disabled%> <%=checkOption("peppolcustomer")%>>
	                        	&nbsp; Peppol Customer Summary</label>
                         </TH>
                         
                         <TR>
                         
                    <TH WIDTH="20%" ALIGN = "LEFT">
	                        	<label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="PeppolIntegration" value="peppolsupplier"  <%=disabled%> <%=checkOption("peppolsupplier")%>>
	                        	&nbsp; Peppol Supplier Summary</label>
                         </TH>
                    
                    </TABLE>
    </div>
</div>
<%}%>                 
<%-- <div class="panel panel-default">
    <div class="panel-heading" style="background: #eaeafa"><strong>Accounting - Home Page</strong></div>
    <div class="panel-body">
		<TABLE class="table1" style="font-size:14px;width: 100%;">
			<tr>
				<th width="20%" align="left"><input type="checkbox" style="border: 0;" name="URL" value="profitlosswidget" <%=disabled%> <%=checkOption("profitlosswidget")%>>&nbsp;Profit Loss Widget</th>
				<th width="20%" align="left"><input type="checkbox" style="border: 0;" name="URL" value="expenseswidget" <%=disabled%> <%=checkOption("expenseswidget")%>>&nbsp;Expense Widget</th>
				<th width="20%" align="left"><input type="checkbox" style="border: 0;" name="URL" value="incomewidget" <%=disabled%> <%=checkOption("incomewidget")%>>&nbsp;Income Widget</th>
				<th width="20%" align="left"><input type="checkbox" style="border: 0;" name="URL" value="bankaccountswidget" <%=disabled%> <%=checkOption("bankaccountswidget")%>>&nbsp;Bank Accounts Widget</th>
			</tr>
			<tr>
				<th width="20%" align="left"><input type="checkbox" style="border: 0;" name="URL" value="purchasewidget" <%=disabled%> <%=checkOption("purchasewidget")%>>&nbsp;Purchase Widget</th>
				<th width="20%" align="left"><input type="checkbox" style="border: 0;" name="URL" value="saleswidget" <%=disabled%> <%=checkOption("saleswidget")%>>&nbsp;Sales Widget</th>
				<th width="20%" align="left"><input type="checkbox" style="border: 0;" name="URL" value="movementhistorywidget" <%=disabled%> <%=checkOption("movementhistorywidget")%>>&nbsp;Movement History Widget</th>
				<th></th>
			</tr>
	</table>
</div>
                          </div>
                          
<div class="panel panel-default">
    <div class="panel-heading" style="background: #eaeafa"><strong>Accounting - Masters</strong></div>
    <div class="panel-body">
    	<TABLE class="table1" style="font-size:14px;width: 100%;">					
			<tr>
				<th width="20%" align="left"><input type="checkbox" style="border: 0;" name="URL" value="createbankbranch" <%=disabled%> <%=checkOption("createbankbranch")%>>&nbsp;Create bank Branch</th>
				<th width="20%" align="left"><input type="checkbox" style="border: 0;" name="URL" value="listbankbranches" <%=disabled%> <%=checkOption("listbankbranches")%>>&nbsp;List Bank Branches</th>
				<th width="20%" align="left"><input type="checkbox" style="border: 0;" name="URL" value="importbankbranchss" <%=disabled%> <%=checkOption("importbankbranchss")%>>&nbsp;Import Bank Branches</th>
				<th></th>
			</tr>
			<tr>
				<th width="20%" align="left"><input type="checkbox" style="border: 0;" name="URL" value="createcustomer" <%=disabled%> <%=checkOption("createcustomer")%>>&nbsp;Create Customer</th>
				<th width="20%" align="left"><input type="checkbox" style="border: 0;" name="URL" value="listcustomers" <%=disabled%> <%=checkOption("listcustomers")%>>&nbsp;List Customers</th>
				<th width="20%" align="left"><input type="checkbox" style="border: 0;" name="URL" value="importcustomers" <%=disabled%> <%=checkOption("importcustomers")%>>&nbsp;Import Customers</th>
				<th></th>
			</tr>
			<tr>
				<th width="20%" align="left"><input type="checkbox" style="border: 0;" name="URL" value="createsupplier" <%=disabled%> <%=checkOption("createsupplier")%>>&nbsp;Create Supplier</th>
				<th width="20%" align="left"><input type="checkbox" style="border: 0;" name="URL" value="listsuppliers" <%=disabled%> <%=checkOption("listsuppliers")%>>&nbsp;List Suppliers</th>
				<th width="20%" align="left"><input type="checkbox" style="border: 0;" name="URL" value="importsuppliers" <%=disabled%> <%=checkOption("importsuppliers")%>>&nbsp;Import Suppliers</th>
				<th></th>
			</tr>
			<tr>
				<th width="20%" align="left"><input type="checkbox" style="border: 0;" name="URL" value="createaccount" <%=disabled%> <%=checkOption("createaccount")%>>&nbsp;Create Account</th>
				<th width="20%" align="left"><input type="checkbox" style="border: 0;" name="URL" value="listaccounts" <%=disabled%> <%=checkOption("listaccounts")%>>&nbsp;List Accounts</th>
				<th width="20%" align="left"><input type="checkbox" style="border: 0;" name="URL" value="importaccounts" <%=disabled%> <%=checkOption("importaccounts")%>>&nbsp;Import Accounts</th>
				<th></th>
			</tr>
			<tr>
				<th width="20%" align="left"><input type="checkbox" style="border: 0;" name="URL" value="createdaybook" <%=disabled%> <%=checkOption("createdaybook")%>>&nbsp;Create Daybook</th>
				<th width="20%" align="left"><input type="checkbox" style="border: 0;" name="URL" value="listdaybooks" <%=disabled%> <%=checkOption("listdaybooks")%>>&nbsp;List Daybooks</th>
				<th></th><th></th>
			</tr>
			<tr>
				<th width="20%" align="left"><input type="checkbox" style="border: 0;" name="URL" value="createcurrency" <%=disabled%> <%=checkOption("createcurrency")%>>&nbsp;Create Currency</th>
				<th width="20%" align="left"><input type="checkbox" style="border: 0;" name="URL" value="listcurrencies" <%=disabled%> <%=checkOption("listcurrencies")%>>&nbsp;List Currencies</th>
				<th></th><th></th>
			</tr>
	</table>
</div>
                          </div>
                          
<div class="panel panel-default">
    <div class="panel-heading" style="background: #eaeafa"><strong>Accounting - Transactions</strong></div>
    <div class="panel-body">
    	<TABLE class="table1" style="font-size:14px;width: 100%;">					
			<tr>
				<th width="20%" align="left"><input type="checkbox" style="border: 0;" name="URL" value="newfinancialtransaction" <%=disabled%> <%=checkOption("newfinancialtransaction")%>>&nbsp;New Financial Transaction</th>
				<th width="20%" align="left"><input type="checkbox" style="border: 0;" name="URL" value="listfinancialtransactions" <%=disabled%> <%=checkOption("listfinancialtransactions")%>>&nbsp;List Financial Transactions</th>
				<th width="20%" align="left"><input type="checkbox" style="border: 0;" name="URL" value="reconcilesalespurchase" <%=disabled%> <%=checkOption("reconcilesalespurchase")%>>&nbsp;Reconcile Sales &amp; Purchase</th>
				<th></th>
			</tr>
	</table>
</div>
                          </div>
                          
<div class="panel panel-default">
    <div class="panel-heading" style="background: #eaeafa"><strong>Accounting - Reports</strong></div>
    <div class="panel-body">
    	<TABLE class="table1" style="font-size:14px;width: 100%;">					
			<tr>
				<th width="20%" align="left"><input type="checkbox" style="border: 0;" name="URL" value="movementhistoryreport" <%=disabled%> <%=checkOption("movementhistoryreport")%>>&nbsp;Movement History Report</th>
				<th width="20%" align="left"><input type="checkbox" style="border: 0;" name="URL" value="financialtransactionsreport" <%=disabled%> <%=checkOption("financialtransactionsreport")%>>&nbsp;Financial Transactions Report</th>
				<th width="20%" align="left"><input type="checkbox" style="border: 0;" name="URL" value="profitlossstatement" <%=disabled%> <%=checkOption("profitlossstatement")%>>&nbsp;Profit Loss Statement</th>
				<th width="20%" align="left"><input type="checkbox" style="border: 0;" name="URL" value="trailbalance" <%=disabled%> <%=checkOption("trailbalance")%>>&nbsp;Trail Balance</th>
			</tr>
			<tr>
				<th width="20%" align="left"><input type="checkbox" style="border: 0;" name="URL" value="balancesheet" <%=disabled%> <%=checkOption("balancesheet")%>>&nbsp;Balance sheet</th>
				<th width="20%" align="left"><input type="checkbox" style="border: 0;" name="URL" value="customerwiseageing" <%=disabled%> <%=checkOption("customerwiseageing")%>>&nbsp;Customer-wise Ageing</th>
				<th width="20%" align="left"><input type="checkbox" style="border: 0;" name="URL" value="supplierwiseageing" <%=disabled%> <%=checkOption("supplierwiseageing")%>>&nbsp;Supplier-wise Ageing</th>
				<th width="20%" align="left"><input type="checkbox" style="border: 0;" name="URL" value="cashflowprojection">&nbsp;Cash Flow Projection</th>
			</tr>
        </table>
    </div>
</div> --%>                           
<!-- <div class="panel panel-default">
<div class="panel-heading" style="background: #eaeafa"><strong>PDA - MAIN MENU (STORE N TRACK)</strong></div>
<div class="panel-body">
                    <TABLE class="table1" style="font-size:14px;width: 100%;">
                            <TR>
                              <TH WIDTH="20%" ALIGN = "LEFT">
                                <INPUT Type=Checkbox  style="border:0;" name="URL" value="pdaInBound"  <%=disabled%> <%=checkOption("pdaInBound")%>>
                                &nbsp; INBOUND
                              <TH WIDTH="20%" ALIGN = "LEFT">
                                <INPUT Type=Checkbox  style="border:0;" name="URL" value="pdaOutBound"  <%=disabled%> <%=checkOption("pdaOutBound")%>>
                                &nbsp; OUTBOUND
                             <TH WIDTH="20%" ALIGN = "LEFT">
                                <INPUT Type=Checkbox  style="border:0;" name="URL" value="pdaWarehouse"  <%=disabled%> <%=checkOption("pdaWarehouse")%>>
                                &nbsp; IN PREMISES
                             
                            </TABLE>
                            </div>
 </div> -->
<div class="panel panel-default">
<div class="panel-heading" style="background: #eaeafa"><strong>PDA - Purchase Transaction</strong></div>
<div class="panel-body">
    	   <TABLE class="table1" style="font-size:14px;width: 100%;">
                     <TR>
                         <TH WIDTH="20%" ALIGN = "LEFT">
                         <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;" name="URL" value="pdainboundReceipt"  <%=disabled%> <%=checkOption("pdainboundReceipt")%>>
                          &nbsp; PURCHASE RECEIPT</label>
                          
                         <!--   <TH WIDTH="20%" ALIGN = "LEFT">
                          <INPUT Type=Checkbox  style="border:0;" name="URL" value="pdaTransferReceipt"  <%=disabled%> <%=checkOption("pdaTransferReceipt")%>>
                          &nbsp; Transfer Receipt
                          
                          <TH WIDTH="20%" ALIGN = "LEFT">
                          <INPUT Type=Checkbox  style="border:0;" name="URL" value="pdaLoanReceipt"  <%=disabled%> <%=checkOption("pdaLoanReceipt")%>>
                          &nbsp; Bulk Loan Receipt-->
                          
                          <TH WIDTH="20%" ALIGN = "LEFT">
                         <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;" name="URL" value="pdaMiscReceipt"  <%=disabled%> <%=checkOption("pdaMiscReceipt")%>>
                           &nbsp;GOODS RECEIPT</label>
                           
                          </TABLE>
                           </div>
                                </div>

<div class="panel panel-default">
<div class="panel-heading" style="background: #eaeafa"><strong>PDA - Sales Transaction</strong></div>
<div class="panel-body">
    	<TABLE class="table1" style="font-size:14px;width: 100%;">
                   <TR>
                        <TH WIDTH="20%" ALIGN = "LEFT">
                        <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="URL" value="pdaOutBoundIssue"  <%=disabled%> <%=checkOption("pdaOutBoundIssue")%>>
                        &nbsp; SALES PICK & ISSUE</label>
                        
                                             
                         <TH WIDTH="20%" ALIGN = "LEFT">
                        <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="URL" value="pdaMiscIssue"  <%=disabled%> <%=checkOption("pdaMiscIssue")%>>
                        &nbsp; GOODS ISSUE</label>
                                             
                         <TH WIDTH="20%" ALIGN = "LEFT">
                        <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="URL" value="pdaSalesPick"  <%=disabled%> <%=checkOption("pdaSalesPick")%>>
                        &nbsp; SALES PICK</label>
                                             
                         <TH WIDTH="20%" ALIGN = "LEFT">
                        <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="URL" value="pdaSalesIssue"  <%=disabled%> <%=checkOption("pdaSalesIssue")%>>
                        &nbsp; SALES ISSUE</label>
                        
                        <TR>
                                             
                         <TH WIDTH="20%" ALIGN = "LEFT">
                        <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="URL" value="pdaPickIssue"  <%=disabled%> <%=checkOption("pdaPickIssue")%>>
                        &nbsp; PICK/ISSUE</label>
                                             
                         <TH WIDTH="20%" ALIGN = "LEFT">
                        <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="URL" value="pdaSalesOrderCheck"  <%=disabled%> <%=checkOption("pdaSalesOrderCheck")%>>
                        &nbsp; SALES ORDER CHECK</label>
                 </TABLE>
                 </div>
                       </div>
 <div class="panel panel-default">
<div class="panel-heading" style="background: #eaeafa"><strong>PDA - In House</strong></div>
<div class="panel-body">
    	<TABLE class="table1" style="font-size:14px;width: 100%;">
                    <TR>
                      <TH WIDTH="20%" ALIGN = "LEFT">
                        <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="URL" value="pdaInventoryQry"  <%=disabled%> <%=checkOption("pdaInventoryQry")%>>
                        &nbsp; INV QUERY</label>
                        
                        <!-- <TH WIDTH="20%" ALIGN = "LEFT">
                        <INPUT Type=Checkbox  style="border:0;" name="URL" value="pdaPutAway"  <%=disabled%> <%=checkOption("pdaPutAway")%>>
                        &nbsp; Put Away-->
                       
                      <TH WIDTH="20%" ALIGN = "LEFT">
                       <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;" name="URL" value="pdaLocTransfer"  <%=disabled%> <%=checkOption("pdaLocTransfer")%>>
                        &nbsp; STOK MOVE</label>
                        
                      <TH WIDTH="20%" ALIGN = "LEFT">
                      <label class="checkbox-inline">  <INPUT Type=Checkbox  style="border:0;" name="URL" value="pdaStockTake"  <%=disabled%> <%=checkOption("pdaStockTake")%>>
                        &nbsp; STOCK TAKE</label>
                        
                       
                     <!--   <TH WIDTH="20%" ALIGN = "LEFT">
                        <INPUT Type=Checkbox  style="border:0;" name="URL" value="pdaTransferObOrder"  <%=disabled%> <%=checkOption("pdaTransferObOrder")%>>
                        &nbsp;  Outbound Transfer
                        
                    <TR> 
                     <TH WIDTH="20%" ALIGN = "LEFT">
                        <INPUT Type=Checkbox  style="border:0;" name="URL" value="pdaKitting"  <%=disabled%> <%=checkOption("pdaKitting")%>>
                        &nbsp; Kitting
                      </TH>
                      <TH WIDTH="20%" ALIGN = "LEFT">
                  <!--   <INPUT Type=Checkbox  style="border:0;" name="URL" value="pdaDeKitting"  <%=disabled%> <%=checkOption("pdaDeKitting")%>>
                        &nbsp; DeKitting -->
                      </TH> 
                         </TABLE>
                          </div>
                         </div>
<!--  <div class="panel panel-default">
<div class="panel-heading" style="background: #eaeafa"><strong>PDA -  MAIN MENU (SELL N TRACK)</strong></div>
<div class="panel-body">
    	<TABLE class="table1" style="font-size:14px;width: 100%;">
    	                <TR>
                              <TH WIDTH="20%" ALIGN = "LEFT">
                                <INPUT Type=Checkbox  style="border:0;" name="URL" value="pdaMobileSales" <%=disabled%> <%=checkOption("pdaMobileSales")%>>
                                &nbsp; Mobile Sales
                                
                              <TH WIDTH="20%" ALIGN = "LEFT">
                                <INPUT Type=Checkbox  style="border:0;" name="URL" value="pdaEnquiry"  <%=disabled%> <%=checkOption("pdaEnquiry")%>>
                                &nbsp; Enquiry
                                
                             <TH WIDTH="20%" ALIGN = "LEFT">
                                <INPUT Type=Checkbox  style="border:0;" name="URL" value="pdaPos"  <%=disabled%> <%=checkOption("pdaPos")%>>
                                &nbsp; POS
                             
                            </TABLE>
                            </div>
</div>-->
<!--  <div class="panel panel-default">
<div class="panel-heading" style="background: #eaeafa"><strong>PDA - MOBILE SALES</strong></div>
<div class="panel-body">  
                <TABLE class="table1" style="font-size:14px;width: 100%;">
                          <TR>
                          
                          <TH WIDTH="20%" ALIGN = "LEFT">
                          <INPUT Type=Checkbox  style="border:0;" name="URL" value="pdaMobileSalesCreate" <%=disabled%> <%=checkOption("pdaMobileSalesCreate")%>>
                          &nbsp; Create
                          
                          <TH WIDTH="20%" ALIGN = "LEFT">
                          <INPUT Type=Checkbox  style="border:0;" name="URL" value="pdaMobileSalesEdit"  <%=disabled%> <%=checkOption("pdaMobileSalesEdit")%>>
                          &nbsp; Edit
                          
                          <TH WIDTH="20%" ALIGN = "LEFT">
                          <INPUT Type=Checkbox  style="border:0;" name="URL" value="pdaMobileSalesSummary"  <%=disabled%> <%=checkOption("pdaMobileSalesSummary")%>>
                          &nbsp; Summary
                          
                         </TABLE>
                         </div>
 </div>-->
<!-- <div class="panel panel-default">
<div class="panel-heading" style="background: #eaeafa"><strong>PDA - ENQUIRY</strong></div>
<div class="panel-body">  
     <TABLE class="table1" style="font-size:14px;width: 100%;">
                    <TR>
                        <TH WIDTH="20%" ALIGN = "LEFT">
                        <INPUT Type=Checkbox  style="border:0;" name="URL" value="pdaEnquiryInvQryByProduct" <%=disabled%> <%=checkOption("pdaEnquiryInvQryByProduct")%>>
                        &nbsp; Query By Product
                        
                       <TH WIDTH="20%" ALIGN = "LEFT">
                        <INPUT Type=Checkbox  style="border:0;" name="URL" value="pdaEnquiryInvQryByLoc" <%=disabled%> <%=checkOption("pdaEnquiryInvQryByLoc")%>>
                        &nbsp; Query By Loc
                        
                        </TABLE>
                         </div>
</div> -->
                       
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
