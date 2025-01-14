<%@ include file="header.jsp" %>
<%@page import="com.track.constants.IConstants"%>
<%
String title = "User Access Rights Group Details";
%>
<%@include file="sessionCheck.jsp" %>
<jsp:include page="header2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
	<jsp:param name="mainmenu" value="<%=IConstants.SETTINGS %>" />
	<jsp:param name="submenu" value="<%=IConstants.USER_ADMIN %>" />
</jsp:include>
<link rel="stylesheet" href="css/typeahead.css">
<link rel="stylesheet" href="css/accounting.css">
<style>
th {
vertical-align: top;
}
</style>
<script language="JavaScript" type="text/javascript" src="js/userLevel.js"></script>
<script language="JavaScript" type="text/javascript" src="js/general.js"></script>

<%--<style>
.table1c tr td{padding: 30px}
</style> --%>


<jsp:useBean id="sl"  class="com.track.gates.selectBean" />
<jsp:useBean id="ub"  class="com.track.gates.userBean" />


<div class="container-fluid m-t-20">
	 <div class="box"> 
            <div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title"><%=title %></h1>
              <h1 style="font-size: 18px; cursor: pointer;"
				class="box-title pull-right"
				onclick="window.location.href='GroupSummary.jsp'">
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
document.form.action="maintUserLevel.jsp?action=update";
document.form.submit();
}
</script>


<form name="form" class="form-horizontal" method="POST" action="maintUserLevel.jsp">
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

<form class="form-horizontal" method="POST" action="userLevelSubmit.jsp">
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
           
               </TABLE>
         </div>
  </div>
					
<div class="panel panel-default">
    <div class="panel-heading" style="background: #eaeafa"><strong>User Admin</strong></div>
    <div class="panel-body">
    <TABLE class="table1" style="font-size:14px">
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
                          <%if(!plant1.equalsIgnoreCase("track")){%>
                         <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;background=#dddddd" name="URL" value="uaMaintAcct"  <%=disabled%> <%=checkOption("uaMaintAcct")%>>
                        &nbsp;  Summary &#45; User Access Rights Group Details<%}%></label>
                  
                     <TH WIDTH="20%" ALIGN = "LEFT">
                     <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;background=#dddddd" name="URL" value="maintGroup" <%=disabled%> <%=checkOption("maintGroup")%>>
                      &nbsp;  Edit User Access Rights Group Details</label>
                      
                      <TH WIDTH="20%" ALIGN = "LEFT">
                      <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;background=#dddddd" name="URL" id="USER" value="uaNewAcct" <%=disabled%> <%=checkOption("uaNewAcct")%>>
                       &nbsp; Create User Access Rights Group Details</label>
                       
                     <TH WIDTH="20%" ALIGN = "LEFT">
                    <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;background=#dddddd" name="URL" value="uaMaintLevel" <%=disabled%> <%=checkOption("uaMaintLevel")%>>
                       &nbsp; Summary &#45; User/Customer Details</label>
                        
                   <TR>
                       <TH WIDTH="20%" ALIGN = "LEFT">
                       <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;background=#dddddd" name="URL" value="maintUser" <%=disabled%> <%=checkOption("maintUser")%>>
                        &nbsp;  Edit User Details</label>
                        
                     <TH WIDTH="20%" ALIGN = "LEFT">
                       <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;background=#dddddd" name="URL" value="uaChngPwd" <%=disabled%> <%=checkOption("uaChngPwd")%>>
                        &nbsp; Edit Password Details </label>      
                        
                     <TH WIDTH="20%" ALIGN = "LEFT">
                   <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;" name="URL" value="customerMaintCpny" <%=disabled%> <%=checkOption("customerMaintCpny")%>>
                    &nbsp; Edit Your Company Profile</label>
                    
                     <TH WIDTH="20%" ALIGN = "LEFT">
                     <%if(plant1.equalsIgnoreCase("track")){%>
                   <label class="checkbox-inline">  <INPUT Type=Checkbox  style="border:0;background=#dddddd" name="URL" value="uaNewLevel" <%=disabled%> <%=checkOption("uaNewLevel")%>>
                      &nbsp; Create User Details</label>
                      
                     <TH WIDTH="20%" ALIGN = "LEFT">
                   <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;background=#dddddd" name="URL" value="uaauthcmpy" <%=disabled%> <%=checkOption("uaauthcmpy")%>>&nbsp;Authorise Company Details<%}%> </label>     
                    </TABLE>
                 </div>
        </div>     
<div class="panel panel-default">
<div class="panel-heading" style="background: #eaeafa"><strong>System Master</strong></div>
<div class="panel-body">
  	<TABLE>
             <TR>
                  <TH WIDTH="20%" ALIGN = "LEFT">
                 <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;" name="URL" value="itemsummry"  <%=disabled%> <%=checkOption("itemsummry")%>>
                  &nbsp; Summary &#45; Product Details</label>
                  
                     <TH WIDTH="20%" ALIGN = "LEFT">
                <label class="checkbox-inline">  <INPUT Type=Checkbox  style="border:0;" name="URL" value="alternateItemSummary"  <%=disabled%> <%=checkOption("alternateItemSummary")%>>   
                  &nbsp; Summary &#45; Alternate Products Details</label>
                  
                  <TH WIDTH="20%" ALIGN = "LEFT">
                 <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;" name="URL" value="maintItem" <%=disabled%>  <%=disabled%> <%=checkOption("maintItem")%>>
                  &nbsp;  Edit Product Details </label>
                  
                  <TH WIDTH="20%" ALIGN = "LEFT">
                 <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;" name="URL" value="uaItemMast"<%=disabled%> <%=checkOption("uaItemMast")%>>
                  &nbsp; Create Product Details</label>
                  
                 <TR>
                  <TH WIDTH="20%" ALIGN = "LEFT">
                <label class="checkbox-inline">  <INPUT Type=Checkbox  style="border:0;" name="URL" value="alternatebrandsummary" <%=disabled%> <%=checkOption("alternatebrandsummary")%>> 
                  &nbsp; Summary/Edit &#45; Alternate Brand Product Details</label>
                  
                   <TH WIDTH="20%" ALIGN = "LEFT">
                <label class="checkbox-inline">  <INPUT Type=Checkbox  style="border:0;" name="URL" value="createalternatbrandproduct"<%=disabled%> <%=checkOption("createalternatbrandproduct")%>>
                  &nbsp; Create Alternate Brand Product Details</label>
                  
               
                  <TH WIDTH="20%" ALIGN = "LEFT">
                 <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;" name="URL" value="prdClsSummary" <%=disabled%> <%=checkOption("prdClsSummary")%>>
                  &nbsp; Summary &#45; Product Class Details</label>
          
             
                   <TH WIDTH="20%" ALIGN = "LEFT">
                 <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;" name="URL" value="maintPrdCls" <%=disabled%> <%=checkOption("maintPrdCls")%>>   
                  &nbsp; Edit Product Class Details</label>
                <TR>
                  <TH WIDTH="20%" ALIGN = "LEFT">
                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="URL" value="uaPrdCls" <%=disabled%> <%=checkOption("uaPrdCls")%>>
                  &nbsp; Create Product Class Details</label>
               
                  <TH WIDTH="20%" ALIGN = "LEFT">
                 <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;" name="URL" value="prdTypeSummary" <%=disabled%> <%=checkOption("prdTypeSummary")%>>
                  &nbsp; Summary &#45; Product Type Details </label>
                  
                  <TH WIDTH="20%" ALIGN = "LEFT">
                 <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;" name="URL" value="maintPrdType" <%=disabled%> <%=checkOption("maintPrdType")%>>    
                  &nbsp; Edit Product Type Details</label>
                  
             
             	  <TH WIDTH="20%" ALIGN = "LEFT">
                  <label class="checkbox-inline">	<INPUT Type=Checkbox  style="border:0;" name="URL" value="uaPrdType" <%=disabled%> <%=checkOption("uaPrdType")%>> 
                  &nbsp; Create Product Type Details</label>
                 <TR>
                  	<TH WIDTH="20%" ALIGN = "LEFT">
	                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="URL" value="prdBrandSummary"  <%=disabled%> <%=checkOption("prdBrandSummary")%>>
	                  &nbsp; Summary &#45; Product Brand Details </label>
	           
	                  <TH WIDTH="20%" ALIGN = "LEFT">
	                <label class="checkbox-inline">  <INPUT Type=Checkbox  style="border:0;" name="URL" value="maintPrdBrand"  <%=disabled%> <%=checkOption("maintPrdBrand")%>> 
	                  &nbsp; Edit Product Brand Details</label>
	                  
	                  <TH WIDTH="20%" ALIGN = "LEFT">
	                <label class="checkbox-inline">  <INPUT Type=Checkbox  style="border:0;" name="URL" value="createPrdBrand"  <%=disabled%> <%=checkOption("createPrdBrand")%>> 
	                  &nbsp; Create Product Brand Details</label>
	                  
	                  
	                  <TH WIDTH="20%" ALIGN = "LEFT">
                     <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;" name="URL" value="uomsummry" <%=disabled%> <%=checkOption("uomsummry")%>>    
                       &nbsp; Summary &#45; Unit of Measure (UOM) Details</label>
                 	<TR>
	                    <TH WIDTH="20%" ALIGN = "LEFT">
	                 <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;" name="URL" value="maintUom" <%=disabled%> <%=checkOption("maintUoM")%>>   
	                  &nbsp; Edit Unit of Measure (UOM) Details</label>
	                
	                  <TH WIDTH="20%" ALIGN = "LEFT">
	                 <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;" name="URL" value="uommst" <%=disabled%> <%=checkOption("uommst")%>>     
	                  &nbsp; Create Unit of Measure (UOM) Details  </label>
	                 
	                   <TH WIDTH="20%" ALIGN = "LEFT">
	                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="URL" value="locsummry"  <%=disabled%> <%=checkOption("locsummry")%>>     
	                  &nbsp; Summary &#45; Location Details  </label>
	                  
	                  <TH WIDTH="20%" ALIGN = "LEFT">
	                 <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;" name="URL" value="maintLoc"   <%=disabled%> <%=checkOption("maintLoc")%>>   
	                  &nbsp; Edit Location Details</label>
                 <TR>
                  
	                  <TH WIDTH="20%" ALIGN = "LEFT">
	                 <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;" name="URL" value="msItemLoc" <%=disabled%> <%=checkOption("msItemLoc")%>>     
	                  &nbsp;  Create Location Details  </label>
	                  
	                    <TH WIDTH="20%" ALIGN = "LEFT">
	                <label class="checkbox-inline">  <INPUT Type=Checkbox  style="border:0;" name="URL" value="loctypesummry"  <%=disabled%> <%=checkOption("loctypesummry")%>>    
	                  &nbsp; Summary &#45; Location Type Details</label>
	                  
	                 
	                  <TH WIDTH="20%" ALIGN = "LEFT">
	                <label class="checkbox-inline">  <INPUT Type=Checkbox  style="border:0;" name="URL" value="maintLocType" <%=disabled%> <%=checkOption("maintLocType")%>>    
	                  &nbsp; Edit Location Type Details</label>
	                  
	                  <TH WIDTH="20%" ALIGN = "LEFT">
	                 <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;" name="URL" value="createLocType"  <%=disabled%><%=checkOption("createLocType")%>>     
	                  &nbsp;  Create Location Type Details </label>
                <TR>   
	                   <TH WIDTH="20%" ALIGN = "LEFT">
	                <label class="checkbox-inline">  <INPUT Type=Checkbox  style="border:0;" name="URL" value="rsnsummry"  <%=disabled%> <%=checkOption("rsnsummry")%>>     
	                  &nbsp; Summary &#45; Reason Code Details</label>
	                  
	                    <TH WIDTH="20%" ALIGN = "LEFT">
	                <label class="checkbox-inline">  <INPUT Type=Checkbox  style="border:0;" name="URL" value="maintReason"  <%=disabled%> <%=checkOption("maintReason")%>>      
	                  &nbsp; Edit Reason Code Details</label>
	                   
	                  <TH WIDTH="20%" ALIGN = "LEFT">
	                <label class="checkbox-inline">  <INPUT Type=Checkbox  style="border:0;" name="URL" value="rsnmst" <%=disabled%><%=checkOption("rsnmst")%>>    
	                  &nbsp; Create Reason Code Details</label>
	         </TABLE>
                  </div>
        </div>  
<div class="panel panel-default">
<div class="panel-heading" style="background: #eaeafa"><strong>System Admin</strong></div>
<div class="panel-body">
    <TABLE>
             <TR>                 
                  <TH WIDTH="20%" ALIGN = "LEFT">
                <label class="checkbox-inline">  <INPUT Type=Checkbox  style="border:0;" name="URL" value="vendorsummry" <%=disabled%><%=checkOption("vendorsummry")%>> 
                  &nbsp;  Summary &#45; Supplier Details</label>
                  
                   <TH WIDTH="20%" ALIGN = "LEFT">
               <label class="checkbox-inline">   <INPUT Type=Checkbox  style="border:0;" name="URL" value="supplierdiscountsummary " <%=disabled%><%=checkOption("supplierdiscountsummary")%>> 
                  &nbsp; Summary &#45; Supplier Discount</label>
                  
                  <TH WIDTH="20%" ALIGN = "LEFT">
                <label class="checkbox-inline">  <INPUT Type=Checkbox  style="border:0;" name="URL" value="maintVendor" <%=disabled%><%=checkOption("maintVendor")%>> 
                  &nbsp; Edit Supplier Details</label>
                  
                  <TH WIDTH="20%" ALIGN = "LEFT">
               <label class="checkbox-inline">   <INPUT Type=Checkbox  style="border:0;" name="URL" value="uaVendMst" <%=disabled%><%=checkOption("uaVendMst")%>> 
                  &nbsp; Create Supplier Details  </label>
                 <TR>    
	                  <TH WIDTH="20%" ALIGN = "LEFT">
	           <label class="checkbox-inline">       <INPUT Type=Checkbox  style="border:0;" name="URL" value="supplierTypeSummary" <%=disabled%><%=checkOption("supplierTypeSummary")%>> 
	                  &nbsp; Summary/Edit &#45; Supplier Type Details</label>
	                   
	                  <TH WIDTH="20%" ALIGN = "LEFT">
	            <label class="checkbox-inline">      <INPUT Type=Checkbox  style="border:0;" name="URL" value="createSupplierType" <%=disabled%><%=checkOption("createSupplierType")%>> 
	                  &nbsp; Create Supplier Type Details</label>
	                  
	                  <TH WIDTH="20%" ALIGN = "LEFT">
	              <label class="checkbox-inline">    <INPUT Type=Checkbox  style="border:0;" name="URL" value="customersummry" <%=disabled%><%=checkOption("customersummry")%>> 
	                  &nbsp; Summary &#45; Customer Details</label>
	                         
	                  <TH WIDTH="20%" ALIGN = "LEFT">
	               <label class="checkbox-inline">   <INPUT Type=Checkbox  style="border:0;" name="URL" value="maintCustomer" <%=disabled%><%=checkOption("maintCustomer")%>> 
	                  &nbsp;  Edit Customer Details</label>
                 <TR>               
                                           
	                  <TH WIDTH="20%" ALIGN = "LEFT">
	            <label class="checkbox-inline">      <INPUT Type=Checkbox  style="border:0;" name="URL" value="uaCustMst" <%=disabled%><%=checkOption("uaCustMst")%>> 
	                  &nbsp; Create Customer Details </label>
	                  
	                  <TH WIDTH="20%" ALIGN = "LEFT">
	            <label class="checkbox-inline">      <INPUT Type=Checkbox  style="border:0;" name="URL" value="customerdiscountsummary" <%=disabled%><%=checkOption("customerdiscountsummary")%>> 
	                   &nbsp; Summary &#45; Customer Discount</label>
	                  
	                   <TH WIDTH="20%" ALIGN = "LEFT">
	            <label class="checkbox-inline">      <INPUT Type=Checkbox  style="border:0;" name="URL" value="ctsummry" <%=disabled%><%=checkOption("ctsummry")%>> 
	                 &nbsp; Summary &#45; Customer Type Details</label>
	                 
	                    <TH WIDTH="20%" ALIGN = "LEFT">
	              <label class="checkbox-inline">    <INPUT Type=Checkbox  style="border:0;" name="URL" value="uaCtMst" <%=disabled%><%=checkOption("uaCtMst")%>> 
	                  &nbsp; Edit Customer Type Details </label>
                  <TR>    
	                  <TH WIDTH="20%" ALIGN = "LEFT">
	              <label class="checkbox-inline">    <INPUT Type=Checkbox  style="border:0;" name="URL" value="maintCt" <%=disabled%><%=checkOption("maintCt")%>> 
	                  &nbsp; Create Customer Type Details </label>
	               
	                  <%-- <TH WIDTH="20%" ALIGN = "LEFT">
	              <label class="checkbox-inline">    <INPUT Type=Checkbox  style="border:0;" name="URL" value="ualoanview" <%=disabled%><%=checkOption("ualoanview")%>> 
	                  &nbsp; Summary &#45; Rental Customer Details</label>
	                         
	                   <TH WIDTH="20%" ALIGN = "LEFT">
	        	<label class="checkbox-inline">	  <INPUT Type=Checkbox  style="border:0;" name="URL" value="uamaintln" <%=disabled%><%=checkOption("uamaintln")%>> 
	                  &nbsp; Edit Rental Customer Details</label>
	             
	                  <TH WIDTH="20%" ALIGN = "LEFT">
	             <label class="checkbox-inline">     <INPUT Type=Checkbox  style="border:0;" name="URL" value="uacratln" <%=disabled%><%=checkOption("uacratln")%>> 
	                  &nbsp;  Create Rental Customer Details</label>
              <TR>         
	                  <TH WIDTH="20%" ALIGN = "LEFT">
	              <label class="checkbox-inline">     <INPUT Type=Checkbox  style="border:0;" name="URL" value="uatransview" <%=disabled%><%=checkOption("uatransview")%>> 
	                  &nbsp;  Summary &#45; Consignment Customer Details</label>
	               
	                  <TH WIDTH="20%" ALIGN = "LEFT">
	             <label class="checkbox-inline">     <INPUT Type=Checkbox  style="border:0;" name="URL" value="uamaintrans" <%=disabled%><%=checkOption("uamaintrans")%>> 
	                  &nbsp; Edit Consignment Customer Details</label>
	            
	                  <TH WIDTH="20%" ALIGN = "LEFT">
	             <label class="checkbox-inline">     <INPUT Type=Checkbox  style="border:0;" name="URL" value="uacrattrans" <%=disabled%><%=checkOption("uacrattrans")%>> 
	                  &nbsp; Create Consignment Customer Details </label> --%>
	               
	                  <TH WIDTH="20%" ALIGN = "LEFT">
	             <label class="checkbox-inline">     <INPUT Type=Checkbox  style="border:0;" name="URL" value="employeesummry" <%=disabled%><%=checkOption("employeesummry")%>> 
	                  &nbsp; Employee</label>
               
	                  <TH WIDTH="20%" ALIGN = "LEFT">
	             <label class="checkbox-inline">     <INPUT Type=Checkbox  style="border:0;" name="URL" value="importmasterdata" <%=disabled%><%=checkOption("importmasterdata")%>> 
	                  &nbsp; Import Master Data</label>
	              
	                  <TH WIDTH="20%" ALIGN = "LEFT">
	               <label class="checkbox-inline">   <INPUT Type=Checkbox  style="border:0;" name="URL" value="importdata" <%=disabled%><%=checkOption("importdata")%>> 
	                  &nbsp; Import Inventory And Transaction Data</label>
                                
        	 </TABLE>
        	  </div>
        </div>
         
<div class="panel panel-default">
<div class="panel-heading" style="background: #eaeafa"><strong>Order Admin</strong></div>
<div class="panel-body"> 
		<TABLE class="table1" style="font-size:14px">
        	 <TR>                 
                                        
                  <TH WIDTH="20%" ALIGN = "LEFT">
                <label class="checkbox-inline">  <INPUT Type=Checkbox  style="border:0;" name="URL" value="ordtypesumry" <%=disabled%> <%=checkOption("ordtypesumry")%>>
                  &nbsp;  Summary &#45; Order Type Details  </label>
                  
                      
                  <TH WIDTH="20%" ALIGN = "LEFT">
                <label class="checkbox-inline">  <INPUT Type=Checkbox  style="border:0;" name="URL" value="maintordtype" <%=disabled%> <%=checkOption("maintordtype")%>>
                  &nbsp; Edit Order Type Details</label>
  
                  <TH WIDTH="20%" ALIGN = "LEFT">
               <label class="checkbox-inline">   <INPUT Type=Checkbox  style="border:0;" name="URL" value="cratordtype" <%=disabled%> <%=checkOption("cratordtype")%>>
                  &nbsp; Create Order Type Details  </label>
                  
                     <TH WIDTH="20%" ALIGN = "LEFT">
                <label class="checkbox-inline">      <INPUT Type=Checkbox  style="border:0;" name="URL" value="createpaymenttype" <%=disabled%> <%=checkOption("createpaymenttype")%>>
                      &nbsp; Summary &#45; Create Payment Type Details</label>
                 <TR>     
                   <TH WIDTH="20%" ALIGN = "LEFT">
                <label class="checkbox-inline">      <INPUT Type=Checkbox  style="border:0;" name="URL" value="editpaymenttype" <%=disabled%> <%=checkOption("editpaymenttype")%>>
                      &nbsp; Edit Payment Type Details</label>
                  
                   <TH WIDTH="20%" ALIGN = "LEFT">
              <label class="checkbox-inline">    <INPUT Type=Checkbox  style="border:0;" name="URL" value="gstSummry" <%=disabled%> <%=checkOption("gstSummry")%>>
                  &nbsp; Summary &#45; Vat Details  </label>
                  
                    <TH WIDTH="20%" ALIGN = "LEFT">
              <label class="checkbox-inline">    <INPUT Type=Checkbox  style="border:0;" name="URL" value="maintgsttype" <%=disabled%> <%=checkOption("maintgsttype")%>>
                  &nbsp; Edit Vat Details</label>
                 
                  <TH WIDTH="20%" ALIGN = "LEFT">
                <label class="checkbox-inline">  <INPUT Type=Checkbox  style="border:0;" name="URL" value="gsttype" <%=disabled%> <%=checkOption("gsttype")%>>
                  &nbsp; Create Vat Details</label>
                   <TR>         
                  <TH WIDTH="20%" ALIGN = "LEFT">
              <label class="checkbox-inline">   <INPUT Type=Checkbox  style="border:0;" name="URL" value="cursummry" <%=disabled%> <%=checkOption("cursummry")%>>
                  &nbsp; Summary &#45; Currency Details</label>
                 
                  <TH WIDTH="20%" ALIGN = "LEFT">
               <label class="checkbox-inline">   <INPUT Type=Checkbox  style="border:0;" name="URL" value="maintCurency" <%=disabled%> <%=checkOption("maintCurency")%>>
                  &nbsp; Edit Currency Details</label>
              
                  <TH WIDTH="20%" ALIGN = "LEFT">
              <label class="checkbox-inline">     <INPUT Type=Checkbox  style="border:0;" name="URL" value="cratCurency" <%=disabled%> <%=checkOption("cratCurency")%>>
                  &nbsp; Create Currency Details</label>
                                    
                                
                  
                     <TH WIDTH="20%" ALIGN = "LEFT">
              <label class="checkbox-inline">   <INPUT Type=Checkbox  style="border:0;" name="URL" value="createhscode" <%=disabled%> <%=checkOption("createhscode")%>>
                  &nbsp; Create HSCODE Details</label>
                   <TR> 
                  
                    <TH WIDTH="20%" ALIGN = "LEFT">
              <label class="checkbox-inline">   <INPUT Type=Checkbox  style="border:0;" name="URL" value="edithscode" <%=disabled%> <%=checkOption("edithscode")%>>
                  &nbsp; Edit HSCODE Details</label>
                  
                   <TH WIDTH="20%" ALIGN = "LEFT">
              <label class="checkbox-inline">   <INPUT Type=Checkbox  style="border:0;" name="URL" value="createcoo" <%=disabled%> <%=checkOption("createcoo")%>>
                  &nbsp; Create COO Details</label>
                  
                  
                    <TH WIDTH="20%" ALIGN = "LEFT">
              <label class="checkbox-inline">   <INPUT Type=Checkbox  style="border:0;" name="URL" value="editcoo" <%=disabled%> <%=checkOption("editcoo")%>>
                  &nbsp; Edit COO Details</label>
                  
                                
                     <TH WIDTH="20%" ALIGN = "LEFT">
              <label class="checkbox-inline">   <INPUT Type=Checkbox  style="border:0;" name="URL" value="createorderremarks" <%=disabled%> <%=checkOption("createorderremarks")%>>
                  &nbsp; Create Remarks Details</label>
                   <TR> 
                  
                    <TH WIDTH="20%" ALIGN = "LEFT">
            <label class="checkbox-inline">     <INPUT Type=Checkbox  style="border:0;" name="URL" value="editorderremarks" <%=disabled%> <%=checkOption("editorderremarks")%>>
                  &nbsp; Edit Remarks Details</label>
                  
                    <TH WIDTH="20%" ALIGN = "LEFT">
              <label class="checkbox-inline">   <INPUT Type=Checkbox  style="border:0;" name="URL" value="createorderincoterms" <%=disabled%> <%=checkOption("createorderincoterms")%>>
                  &nbsp; Create INCOTERM Details</label>
                  
                  
                    <TH WIDTH="20%" ALIGN = "LEFT">
               <label class="checkbox-inline">  <INPUT Type=Checkbox  style="border:0;" name="URL" value="editorderincoterms" <%=disabled%> <%=checkOption("editorderincoterms")%>>
                  &nbsp; Edit INCTORERM Details</label>
               
                     <TH WIDTH="20%" ALIGN = "LEFT">
             <label class="checkbox-inline">    <INPUT Type=Checkbox  style="border:0;" name="URL" value="createorderfooter" <%=disabled%> <%=checkOption("createorderfooter")%>>
                  &nbsp; Create Footer Details</label>
                  
                   <TR>  
                    <TH WIDTH="20%" ALIGN = "LEFT">
             <label class="checkbox-inline">    <INPUT Type=Checkbox  style="border:0;" name="URL" value="editorderfooter" <%=disabled%> <%=checkOption("editorderfooter")%>>
                  &nbsp; Edit Footer Details</label>
                  
                   <TH WIDTH="20%" ALIGN = "LEFT">
            <label class="checkbox-inline">     <INPUT Type=Checkbox  style="border:0;" name="URL" value="packingMaster" <%=disabled%> <%=checkOption("packingMaster")%>>
                  &nbsp; Create Packing Details </label>
                  
                   <TH WIDTH="20%" ALIGN = "LEFT">
            <label class="checkbox-inline">     <INPUT Type=Checkbox  style="border:0;" name="URL" value="packingMasterSummary" <%=disabled%> <%=checkOption("packingMasterSummary")%>>
                  &nbsp; Edit Packing Details </label>
                     
                  
                 </TABLE>
              </div>
        </div>  
<div class="panel panel-default">
<div class="panel-heading" style="background: #eaeafa"><strong>Printout Configuration</strong></div>
<div class="panel-body"> 
    	<TABLE class="table1" style="font-size:14px">
	             <TR>     
	              <TH WIDTH="20%" ALIGN = "LEFT">
             <label class="checkbox-inline">       <INPUT Type=Checkbox  style="border:0;" name="URL" value="editibpt"  <%=disabled%> <%=checkOption("editibpt")%>>
                    &nbsp; Edit Purchase Order Printout   </label>         
                 
       				 <TH WIDTH="23%" ALIGN = "LEFT">
             <label class="checkbox-inline">       <INPUT Type=Checkbox  style="border:0;" name="URL" value="editibwithcostpt" <%=disabled%> <%=checkOption("editibwithcostpt")%>>
                    &nbsp; Edit Purchase Order Printout (with cost)</label>
                    
                    <TH WIDTH="23%" ALIGN = "LEFT">
              <label class="checkbox-inline">       <INPUT Type=Checkbox  style="border:0;" name="URL" value="editInboundOrderMailMsg"  <%=disabled%> <%=checkOption("editInboundOrderMailMsg")%>>
                     &nbsp; Edit Purchase Order Email Message</label>
                     
                     <TH WIDTH="23%" ALIGN = "LEFT">
                   <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;" name="URL" value="editestwithpt" <%=disabled%> <%=checkOption("editestwithpt")%>>
                    &nbsp; Edit Sales Estimate Order Printout (with price)</label>
                    <TR> 
                    <TH WIDTH="23%" ALIGN = "LEFT">
                    <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="URL" value="editEstimateOrderMailMsg"  <%=disabled%> <%=checkOption("editEstimateOrderMailMsg")%>>
                    &nbsp; Edit Sales Estimate Order Email Message</label>
                   
                    <TH WIDTH="20%" ALIGN = "LEFT">
                  <label class="checkbox-inline">  <INPUT Type=Checkbox  style="border:0;" name="URL" value="editobpt" <%=disabled%> <%=checkOption("editobpt")%>>
                    &nbsp; Edit Sales Order Printout</label>
                  
                       <TH WIDTH="23%" ALIGN = "LEFT">
                  <label class="checkbox-inline">  <INPUT Type=Checkbox  style="border:0;" name="URL" value="editobwithpt" <%=disabled%> <%=checkOption("editobwithpt")%>>
                    &nbsp; Edit Sales Order Printout (with price)</label>
                    
                    <TH WIDTH="23%" ALIGN = "LEFT">
                  <label class="checkbox-inline">  <INPUT Type=Checkbox  style="border:0;" name="URL" value="editOutboundOrderMailMsg"  <%=disabled%> <%=checkOption("editOutboundOrderMailMsg")%>>
                    &nbsp; Edit Sales Order Email Message</label>
                   
                   <TR>
               
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
                  
                    <TH WIDTH="20%" ALIGN = "LEFT">
                <label class="checkbox-inline">    <INPUT Type=Checkbox  style="border:0;" name="URL" value="editMoveHdr" <%=disabled%> <%=checkOption("editMoveHdr")%>>
                    &nbsp; Edit Stock Move Printout</label>
                      </TABLE>
                </div>
                   </div>
                   <%-- <div class="panel panel-default">
<div class="panel-heading" style="background: #eaeafa"><strong>Approval & Email Configuration</strong></div>
<div class="panel-body"> 
    	<TABLE class="table1" style="font-size:14px">
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
    	<TABLE>
    	   	
                    <TR>
                   
                    <TH WIDTH="20%" ALIGN = "LEFT">
                <label class="checkbox-inline">  <INPUT Type=Checkbox  style="border:0;" name="URL" value="popproduct" <%=disabled%> <%=checkOption("popproduct")%> > 
                  &nbsp; Create Product Details &#45; Purchase,Sales Estimate, Sales, Direct Tax Invoice, Rental And Consignment Order</label>
                   
                    <TH WIDTH="20%" ALIGN = "LEFT">
              <label class="checkbox-inline">    <INPUT Type=Checkbox  style="border:0;" name="URL" value="popsupplier" <%=disabled%> <%=checkOption("popsupplier")%> >
                  &nbsp; Create Supplier Details &#45; Purchase Order</label>
                  
                   <TH WIDTH="20%" ALIGN = "LEFT">
              <label class="checkbox-inline">    <INPUT Type=Checkbox  style="border:0;" name="URL" value="suppliertypepopup" <%=disabled%> <%=checkOption("suppliertypepopup")%>>
                  &nbsp; Create Supplier Type - Purchase Order</label>
                  
                  <TH WIDTH="20%" ALIGN = "LEFT">
               <label class="checkbox-inline">   <INPUT Type=Checkbox  style="border:0;" name="URL" value="popcustomer" <%=disabled%> <%=checkOption("popcustomer")%> >
                  &nbsp; Create Customer Details &#45; Sales Estimate, Sales, Direct Tax Invoice, Rental And Consignment Order</label>
                
                 <TR> 
                 <TH WIDTH="20%" ALIGN = "LEFT">
              <label class="checkbox-inline">    <INPUT Type=Checkbox  style="border:0;" name="URL" value="customertypepopup" <%=disabled%> <%=checkOption("customertypepopup")%>>
                  &nbsp;Create Customer Type - Sales Estimate, Sales Order and Direct Tax Invoice.</label>
                 
				  <TH WIDTH="20%" ALIGN = "LEFT">
             <label class="checkbox-inline">     <INPUT Type=Checkbox  style="border:0;" name="URL" value="ordertypepopup" <%=disabled%> <%=checkOption("ordertypepopup")%>>
                  &nbsp;Create Order Type- Purchase,Sales Estimate, Sales, Direct Tax Invoice,Rental And Consignment Order.</label>
				                    
                    <TH WIDTH="20%" ALIGN = "LEFT">
            <label class="checkbox-inline">      <INPUT Type=Checkbox  style="border:0;" name="URL" value="paymenttypepopup" <%=disabled%> <%=checkOption("paymenttypepopup")%>>
                  &nbsp;Payment Type- Purchase,Sales Estimate, Sales, Direct Tax Invoice,Rental And Consignment Order</label>
                    
             </TABLE>
    </div>
</div> 
                   
<div class="panel panel-default">
<div class="panel-heading" style="background: #eaeafa"><strong>Purchase</strong></div>
<div class="panel-body"> 
             <TABLE class="table1" style="font-size:14px">
             <TR> 
                                     
                    <TH WIDTH="20%" ALIGN = "LEFT">
                 <label class="checkbox-inline">   <INPUT Type=Checkbox  style="border:0;" name="OrderManagement1" value="ibsummary" <%=disabled%> <%=checkOption("ibsummary")%>>
                    &nbsp; Summary &#45; Purchase Order Details  </label>                  
                    
                    <TH WIDTH="20%" ALIGN = "LEFT">
                 <label class="checkbox-inline">   <INPUT Type=Checkbox  style="border:0;" name="OrderManagement1" value="ibsummarycost" <%=disabled%> <%=checkOption("ibsummarycost")%>>
                    &nbsp; Summary &#45; Purchase Order Details (with cost)</label>
                    
                    <TH WIDTH="20%" ALIGN = "LEFT">
                 <label class="checkbox-inline">   <INPUT Type=Checkbox  style="border:0;" name="OrderManagement1" value="maintinb" <%=disabled%> <%=checkOption("maintinb")%>>
                    &nbsp; Edit Purchase Order Details</label>
                               
                    <TH WIDTH="20%" ALIGN = "LEFT">
                <label class="checkbox-inline">    <INPUT Type=Checkbox  style="border:0;" name="OrderManagement1" value="msPurOrd" <%=disabled%> <%=checkOption("msPurOrd")%>>
                    &nbsp; Create Purchase Order Details </label>                 
                    
                    <TR> 
                               
                    <TH WIDTH="20%" ALIGN = "LEFT">
                 <label class="checkbox-inline">   <INPUT Type=Checkbox  style="border:0;" name="OrderManagement1" value="orderClose" <%=disabled%> <%=checkOption("orderClose")%>>
                    &nbsp; Close Outstanding Purchase Order</label>
                    
                        <TH WIDTH="20%" ALIGN = "LEFT">
               <label class="checkbox-inline">         <INPUT Type=Checkbox  style="border:0;" name="URL" value="ibrecvbulk" <%=disabled%> <%=checkOption("ibrecvbulk")%>>
                        &nbsp; Purchase Order Receipt</label> 
                        
                        <TH WIDTH="20%" ALIGN = "LEFT">
                  <label class="checkbox-inline">      <INPUT Type=Checkbox  style="border:0;" name="URL" value="inbrectbyrange"   <%=disabled%> <%=checkOption("inbrectbyrange")%>>
                        &nbsp; Purchase Order Receipt (by serial)</label>
                        
                        <TH WIDTH="20%" ALIGN = "LEFT">
                  <label class="checkbox-inline">      <INPUT Type=Checkbox  style="border:0;" name="URL" value="inbRecbyprd"   <%=disabled%> <%=checkOption("inbRecbyprd")%>>
                        &nbsp; Purchase Order Receipt (by product ID)</label>
                        
                        <TR>                                              
                        
                        <TH WIDTH="20%" ALIGN = "LEFT">
                  <label class="checkbox-inline">      <INPUT Type=Checkbox  style="border:0;" name="URL" value="ibrecvmultiple" <%=disabled%> <%=checkOption("ibrecvmultiple")%>>
                        &nbsp; Purchase Order Receipt (multiple)</label>
                                             
                        <%-- <TH WIDTH="20%" ALIGN = "LEFT">
                        <INPUT Type=Checkbox  style="border:0;" name="URL" value="inbReverse"   <%=disabled%> <%=checkOption("inbReverse")%>>
                        &nbsp; Purchase Order Reversal --%>
                                                          
                        <TH WIDTH="20%" ALIGN = "LEFT">
                   <label class="checkbox-inline">     <INPUT Type=Checkbox  style="border:0;" name="URL" value="mulmiscrecipt"   <%=disabled%> <%=checkOption("mulmiscrecipt")%>>
                        &nbsp; Goods Receipt </label>
                        
                        <TH WIDTH="20%" ALIGN = "LEFT">
                     <label class="checkbox-inline">   <INPUT Type=Checkbox  style="border:0;" name="URL" value="inbMiscRceiptByRange"   <%=disabled%> <%=checkOption("inbMiscRceiptByRange")%>>
                        &nbsp; Goods Receipt (by serial)</label>
                        
                        <TH WIDTH="20%" ALIGN = "LEFT">
                     <label class="checkbox-inline">   <INPUT Type=Checkbox  style="border:0;" name="URL" value="editexpiry" <%=disabled%> <%=checkOption("editexpiry")%>>
                        &nbsp; Edit Inventory Expire Date</label>
                        
                        <TR>
                        
                        <TH WIDTH="20%" ALIGN = "LEFT">
		             <label class="checkbox-inline">   <INPUT Type=Checkbox  style="border:0;" name="Reports" value="ibordersumry" <%=disabled%> <%=checkOption("ibordersumry")%> >
		                &nbsp;  Purchase Order Summary Details</label>
		                </TH>	
	                       
	                            <TH WIDTH="20%" ALIGN = "LEFT">
	                     <label class="checkbox-inline">   	<INPUT Type=Checkbox  style="border:0;" name="Reports" value="ibordersumrywreccost" <%=disabled%> <%=checkOption("ibordersumrywreccost")%> >
	                        	&nbsp;  Purchase Order Summary Details(by cost)</label>
	                            </TH> 
                     
	                            <TH WIDTH="20%" ALIGN = "LEFT">
	                        <label class="checkbox-inline">	<INPUT Type=Checkbox  style="border:0;" name="Reports" value="recvdibsummry"   <%=disabled%> <%=checkOption("recvdibsummry")%> >
	                        	&nbsp;  Purchase Order Summary(by cost)</label>
	                            </TH>
	                        
	                         <TH WIDTH="20%" ALIGN = "LEFT">
	                        <label class="checkbox-inline">	<INPUT Type=Checkbox  style="border:0;" name="Reports"value="printpo"   <%=disabled%> <%=checkOption("printpo")%> >
	                        	&nbsp;  Purchase Order Details</label>
	                         </TH>
	                            
	                        <TR>
	                        
	                        <TH WIDTH="20%" ALIGN = "LEFT">
	                        <label class="checkbox-inline">	<INPUT Type=Checkbox  style="border:0;" name="Reports"value="printpoinv"  <%=disabled%> <%=checkOption("printpoinv")%> >
	                        	&nbsp;  Purchase Order Details (with cost)</label>
	                        </TH>
	                        
	                        <TH WIDTH="20%" ALIGN = "LEFT">
	                        <label class="checkbox-inline">	<INPUT Type=Checkbox  style="border:0;" name="Reports" value="recvsummry"   <%=disabled%> <%=checkOption("recvsummry")%> >
	                        	&nbsp;  Order Receipt Summary</label>
	                        </TH>
	                        
	                        
	                         <TH WIDTH="20%" ALIGN = "LEFT">
	                        <label class="checkbox-inline">	<INPUT Type=Checkbox  style="border:0;" name="Reports" value="recvsummrywithcost"  <%=disabled%> <%=checkOption("recvsummrywithcost")%> >
	                        	&nbsp;  Order Receipt Summary (with cost)</label>
	                        </TH>
	                         
	                      <%--    <TH WIDTH="20%" ALIGN = "LEFT">
		                  <label class="checkbox-inline">  <INPUT Type=Checkbox  style="border:0;" name="OrderManagement1" value="expensesSummary"   <%=disabled%> <%=checkOption("expensesSummary")%> >
		                    &nbsp;  Expenses</label>
		                    </TH>
	                        
	                         <TR>
		                    
		                    
		                    <TH WIDTH="20%" ALIGN = "LEFT">
		                   <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;" name="OrderManagement1" value="grnSummary"   <%=disabled%> <%=checkOption("grnSummary")%> >
		                    &nbsp;  Goods Receipt Summary</label>
		                    </TH>		                   
		                    
		                    <TH WIDTH="20%" ALIGN = "LEFT">
		                   <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;" name="OrderManagement1" value="billSummary"   <%=disabled%> <%=checkOption("billSummary")%> >
		                    &nbsp;  Bill</label>
		                    </TH>
		                          
		                    <TH WIDTH="20%" ALIGN = "LEFT">
		                  <label class="checkbox-inline">  <INPUT Type=Checkbox  style="border:0;" name="OrderManagement1" value="billPaymentSummary"  <%=disabled%> <%=checkOption("billPaymentSummary")%> >
		                    &nbsp;  Payment</label>
		                    </TH>
		                    
		                    <TH WIDTH="20%" ALIGN = "LEFT">
		                  <label class="checkbox-inline">  <INPUT Type=Checkbox  style="border:0;" name="OrderManagement1" value="pdcpaymentSummary"   <%=disabled%> <%=checkOption("pdcpaymentSummary")%> >
		                    &nbsp; PDC Payment</label>
		                    </TH>
		                    
		                     <TR>
		                    
		                    <TH WIDTH="20%" ALIGN = "LEFT">
		                  <label class="checkbox-inline">  <INPUT Type=Checkbox  style="border:0;" name="OrderManagement1" value="POReturnSummary"   <%=disabled%> <%=checkOption("POReturnSummary")%> >
		                    &nbsp; Purchase Return</label>
		                    </TH>
		                    
		                    <TH WIDTH="20%" ALIGN = "LEFT">
		                   <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;" name="OrderManagement1" value="supplierCreditNote"   <%=disabled%> <%=checkOption("supplierCreditNote")%> >
		                    &nbsp; Purchase Credit Notes</label>
		                    </TH> --%>
									
             </TABLE>
             </div>
        </div>   
<div class="panel panel-default">
<div class="panel-heading" style="background: #eaeafa"><strong>Sales Estimate</strong></div>
<div class="panel-body">
     <TABLE class="table1" style="font-size:14px">
            <TR>
            
                    <TH WIDTH="20%" ALIGN = "LEFT">
                  <label class="checkbox-inline">  <INPUT Type=Checkbox  style="border:0;" name="OrderManagement1" value="estsummarywithoutprice" <%=disabled%> <%=checkOption("estsummarywithoutprice")%>>
                    &nbsp; Summary &#45; Sales Estimate Order Details </label>
                    
                    <TH WIDTH="20%" ALIGN = "LEFT">
                 <label class="checkbox-inline">   <INPUT Type=Checkbox  style="border:0;" name="OrderManagement1" value="estsummary" <%=disabled%> <%=checkOption("estsummary")%>>
                    &nbsp; Summary &#45; Sales Estimate Order Details (with price) </label>                   
                                              
                    <TH WIDTH="20%" ALIGN = "LEFT">
                  <label class="checkbox-inline">  <INPUT Type=Checkbox  style="border:0;" name="OrderManagement1" value="maintest" <%=disabled%> <%=checkOption("maintest")%>>
                    &nbsp;  Edit Sales Estimate Order Details</label>
                    
                    <TH WIDTH="20%" ALIGN = "LEFT">
                   <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;" name="OrderManagement1" value="convertOB" <%=disabled%> <%=checkOption("convertOB")%>>
                    &nbsp;  Edit Sales Estimate &#45; Convert to Sales(button)</label>
                    
                   <TR>
                                       
                    <TH WIDTH="20%" ALIGN = "LEFT">
                   <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;" name="OrderManagement1" value="msestOrd" <%=disabled%> <%=checkOption("msestOrd")%>>
                    &nbsp; Create Sales Estimate Order Details</label>
                    
                    <TH WIDTH="20%" ALIGN = "LEFT">
	              <label class="checkbox-inline">  <INPUT Type=Checkbox  style="border:0;" name="Reports" value="estordsmrywithoutprice" <%=disabled%> <%=checkOption("estordsmrywithoutprice")%> >
	                &nbsp;  Sales Estimate Order Summary Details</label>
	                </TH>
	                        
	                <TH WIDTH="20%" ALIGN = "LEFT">
	              <label class="checkbox-inline">  <INPUT Type=Checkbox  style="border:0;" name="Reports" value="estordsmrywissprice" <%=disabled%> <%=checkOption("estordsmrywissprice")%> >
	                &nbsp;  Sales Estimate Order Summary Details(by price)</label>
	                </TH>
					
					<TH WIDTH="20%" ALIGN = "LEFT">
	               <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;" name="Reports" value="summaryalternatbrandproduct"  <%=disabled%> <%=checkOption("summaryalternatbrandproduct")%> >
	                &nbsp; Sales Counter</label>
                    </TH>
                    
                    <TR>                                       
                    	<TH WIDTH="20%" ALIGN = "LEFT">
		              <label class="checkbox-inline">      <INPUT Type=Checkbox  style="border:0;" name="SalesEstimate" value="salescounterconvtoest" <%=disabled%> <%=checkOption("salescounterconvtoest")%> >
		                    &nbsp; Sales Counter Convert To Estimate</label>
	                    </TH>
                        
                     	<TH WIDTH="20%" ALIGN = "LEFT">
			           <label class="checkbox-inline">      <INPUT Type=Checkbox  style="border:0;" name="SalesEstimate" value="salescounterconvtoinvc" <%=disabled%> <%=checkOption("salescounterconvtoinvc")%> >
			                 &nbsp; Sales Counter Convert To Invoice</label>
	                 	</TH>
	                 </TR>    
                   </TABLE>
                   </div>
                  </div>
                  <div class="panel panel-default">
<div class="panel-heading" style="background: #eaeafa"><strong>Sales</strong></div>
<div class="panel-body">
       <TABLE class="table1" style="font-size:14px">
                    <TR>
	                     
	                     <TH WIDTH="20%" ALIGN = "LEFT">
                   <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;" name="OrderManagement1" value="printOB" <%=disabled%> <%=checkOption("printOB")%>>
                    &nbsp; Summary &#45; Sales Order  </label>                  
               
                    <TH WIDTH="20%" ALIGN = "LEFT">
                 <label class="checkbox-inline">   <INPUT Type=Checkbox  style="border:0;" name="OrderManagement1" value="PrintOBInvoice" <%=disabled%> <%=checkOption("PrintOBInvoice")%>>
                    &nbsp; Summary &#45; Sales Order (with price)</label>
                
                    <TH WIDTH="20%" ALIGN = "LEFT">
                 <label class="checkbox-inline">   <INPUT Type=Checkbox  style="border:0;" name="OrderManagement1" value="obsummary" <%=disabled%> <%=checkOption("obsummary")%>>
                    &nbsp; Summary &#45; Sales Order Details</label>
                                        
                    <TH WIDTH="20%" ALIGN = "LEFT">
                  <label class="checkbox-inline">  <INPUT Type=Checkbox  style="border:0;" name="OrderManagement1" value="obsummaryprice" <%=disabled%> <%=checkOption("obsummaryprice")%>>
                    &nbsp; Summary &#45; Sales Order Details (with price)</label>
                    
					<TR>					
                                             
                    <TH WIDTH="20%" ALIGN = "LEFT">
                 <label class="checkbox-inline">   <INPUT Type=Checkbox  style="border:0;" name="OrderManagement1" value="maintoub" <%=disabled%> <%=checkOption("maintoub")%>>
                    &nbsp;  Edit Sales Order Details</label>
                                                        
                    <TH WIDTH="20%" ALIGN = "LEFT">
               <label class="checkbox-inline">     <INPUT Type=Checkbox  style="border:0;" name="OrderManagement1" value="msSalesOrd" <%=disabled%> <%=checkOption("msSalesOrd")%>>
                    &nbsp; Create Sales Order Details</label>
					
					<TH WIDTH="20%" ALIGN = "LEFT">
                 <label class="checkbox-inline">   <INPUT Type=Checkbox  style="border:0;" name="OrderManagement1" value="orderCloseSales" <%=disabled%> <%=checkOption("orderCloseSales")%>>
                    &nbsp; Close Outstanding Sales Order</label>
	                     
	                        <TH WIDTH="20%" ALIGN = "LEFT">
	                     <label class="checkbox-inline">   <INPUT Type=Checkbox  style="border:0;" name="URL" value="outbndmulPicking"   <%=disabled%> <%=checkOption("outbndmulPicking")%>>
	                        &nbsp;  Sales Order Pick</label>
	                        
	                        <TR> 
	                        
	                        <TH WIDTH="20%" ALIGN = "LEFT">
	                     <label class="checkbox-inline">   <INPUT Type=Checkbox  style="border:0;" name="URL" value="obpickingbyRange"   <%=disabled%> <%=checkOption("obpickingbyRange")%>>
	                        &nbsp;  Sales Order Pick (by serial)</label>
	                        
	                         <TH WIDTH="20%" ALIGN = "LEFT">
	                      <label class="checkbox-inline">  <INPUT Type=Checkbox  style="border:0;" name="URL" value="outbndIssuing" <%=disabled%> <%=checkOption("outbndIssuing")%>>
	                        &nbsp; Sales Order Issue</label>
	                        
	                        <TH WIDTH="20%" ALIGN = "LEFT">
                         <label class="checkbox-inline">   <INPUT Type=Checkbox  style="border:0;" name="URL" value="obpickissuebulk" <%=disabled%> <%=checkOption("obpickissuebulk")%>>
                            &nbsp;  Sales Order Pick & Issue  </label>                    
                         
                           <TH WIDTH="20%" ALIGN = "LEFT">
                         <label class="checkbox-inline">  <INPUT Type=Checkbox  style="border:0;" name="URL" value="obpickissuebyprd" <%=disabled%> <%=checkOption("obpickissuebyprd")%>>
                           &nbsp; Sales Order Pick/Issue (by Product)</label>
	                        
	                        <TR>                            
                           
	                        <TH WIDTH="20%" ALIGN = "LEFT">
	                     <label class="checkbox-inline">   <INPUT Type=Checkbox  style="border:0;" name="URL" value="obpickissuemultiple" <%=disabled%> <%=checkOption("obpickissuemultiple")%>>
	                        &nbsp;  Sales Order Pick & Issue (multiple)</label>	                                               
	                      
		                    <TH WIDTH="20%" ALIGN = "LEFT">
		                 <label class="checkbox-inline">   <INPUT Type=Checkbox  style="border:0;" name="URL" value="outbndPkrev"   <%=disabled%> <%=checkOption("outbndPkrev")%>>
		                    &nbsp; Sales Order Pick Return</label>
	                    
	                         <%-- <TH WIDTH="20%" ALIGN = "LEFT">
		                     <INPUT Type=Checkbox  style="border:0;" name="URL" value="obissuereversal" <%=disabled%> <%=checkOption("obissuereversal")%>>
		                     &nbsp;  Sales Order Pick & Issue Reversal --%>	                       
		                    
		                    <TH WIDTH="20%" ALIGN = "LEFT">
		                   <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;" name="URL" value="mulmiscissue"   <%=disabled%> <%=checkOption("mulmiscissue")%>>
		                    &nbsp; Goods Issue</label>
				  		    
		                   <TH WIDTH="20%" ALIGN = "LEFT">
		               <label class="checkbox-inline">    <INPUT Type=Checkbox  style="border:0;" name="URL" value="ubmiscissueByRange"   <%=disabled%> <%=checkOption("ubmiscissueByRange")%>>
		                   &nbsp; Goods Issue (by serial)</label>	
		                     
		                     <TR>
		                   
		            <TH WIDTH="20%" ALIGN = "LEFT">
                  <label class="checkbox-inline">  <INPUT Type=Checkbox  style="border:0;" name="OrderManagement1" value="creatednpl" <%=disabled%> <%=checkOption("creatednpl")%>>
                    &nbsp; Create Sales Packing List/Deliver Note (PL/DN)</label>
                     
                     <TH WIDTH="20%" ALIGN = "LEFT">
                  <label class="checkbox-inline">  <INPUT Type=Checkbox  style="border:0;" name="OrderManagement1" value="packingSummary" <%=disabled%> <%=checkOption("packingSummary")%>>
                    &nbsp; Summary/Edit Packing List and Deliver Note</label>
                     
                     <TH WIDTH="20%" ALIGN = "LEFT">
                  <label class="checkbox-inline">   <INPUT Type=Checkbox  style="border:0;" name="Reports" value="obordersumry" <%=disabled%> <%=checkOption("obordersumry")%> >
                     &nbsp;  Sales Order Summary Details</label>
                     </TH>                           
                        	
                        	<TH WIDTH="20%" ALIGN = "LEFT">
                     <label class="checkbox-inline">   	<INPUT Type=Checkbox  style="border:0;" name="Reports"value="obsalessmry"  <%=disabled%> <%=checkOption("obsalessmry")%> >
                        	&nbsp;   Sales Order Sales Summary</label>
                            </TH> 
                     
                     <TR>
                            
                        	<TH WIDTH="20%" ALIGN = "LEFT">
                      <label class="checkbox-inline">  	<INPUT Type=Checkbox  style="border:0;" name="Reports" value="containerSummary" <%=disabled%> <%=checkOption("containerSummary")%> >
                        	&nbsp; Sales Order Summary(by container)</label>
                            </TH>
                           
                             <TH WIDTH="20%" ALIGN = "LEFT">
                       <label class="checkbox-inline"> 	<INPUT Type=Checkbox  style="border:0;" name="Reports" value="invWithIssueReturn" <%=disabled%> <%=checkOption("invWithIssueReturn")%> >
                        	&nbsp; Sales Order Summary(by customer) </label>
                           </TH>
                           
                            <TH WIDTH="20%" ALIGN = "LEFT">
                        <label class="checkbox-inline">	<INPUT Type=Checkbox  style="border:0;" name="Reports" value="obordersumrywissprice" <%=disabled%> <%=checkOption("obordersumrywissprice")%> >
                        	&nbsp;  Sales Order Summary Details(by price)</label>
                        	</TH>                        	 
                        	
                        	<TH WIDTH="20%" ALIGN = "LEFT">
                        <label class="checkbox-inline">	<INPUT Type=Checkbox  style="border:0;" name="Reports" value="obordersumrywavgcost" <%=disabled%> <%=checkOption("obordersumrywavgcost")%> >
                        	&nbsp;  Sales Order Summary Details (by average cost)</label>
                       		</TH>
                           
                           <TR>
                       		
                        	<TH WIDTH="20%" ALIGN = "LEFT">
                        <label class="checkbox-inline">	<INPUT Type=Checkbox  style="border:0;" name="Reports" value="issuedobsummry" <%=disabled%> <%=checkOption("issuedobsummry")%> >
                        	&nbsp; Sales Order Summary(by price)</label>
                            </TH>
                            
	                        <TH WIDTH="20%" ALIGN = "LEFT">
	                    <label class="checkbox-inline">    	<INPUT Type=Checkbox  style="border:0;" name="Reports" value="printdo"  <%=disabled%> <%=checkOption("printdo")%> >
	                        	&nbsp; Sales Order Details</label>
	                        </TH>
	                          
	                        <TH WIDTH="20%" ALIGN = "LEFT">
	                      <label class="checkbox-inline">  	<INPUT Type=Checkbox  style="border:0;" name="Reports" value="printinvoice"  <%=disabled%> <%=checkOption("printinvoice")%> >
	                        	&nbsp; Sales Order Details (with price)</label>
	                        </TH>                    	 
	                         
	                         <TH WIDTH="20%" ALIGN = "LEFT">
	                      <label class="checkbox-inline">  	<INPUT Type=Checkbox  style="border:0;" name="Reports" value="issuesummry" <%=disabled%> <%=checkOption("issuesummry")%> >     
	                        	&nbsp;  Order Issue Summary</label>
	                         </TH>                       
                            
                            <TR>
	                        
	                      <TH WIDTH="20%" ALIGN = "LEFT">
	                       <label class="checkbox-inline"> 	<INPUT Type=Checkbox  style="border:0;" name="Reports" value="issuesummrywithprice"  <%=disabled%> <%=checkOption("issuesummrywithprice")%> >     
	                        	&nbsp;  Order Issue Summary (with price)</label>
	                        </TH>
	                        
	                      <%-- <TH WIDTH="20%" ALIGN = "LEFT">
	                      <label class="checkbox-inline">  	<INPUT Type=Checkbox  style="border:0;" name="Reports" value="ginotoinvoiceSummary"  <%=disabled%> <%=checkOption("ginotoinvoiceSummary")%> >     
	                        	&nbsp;  Goods Issued</label>
	                        </TH>
	                        
	                        <TH WIDTH="20%" ALIGN = "LEFT">
	                       <label class="checkbox-inline"> 	<INPUT Type=Checkbox  style="border:0;" name="SalesTransaction" value="invoiceSummary"  <%=disabled%> <%=checkOption("invoiceSummary")%> >     
	                        	&nbsp;  Invoice</label>
	                        </TH>
	                        
	                        <TH WIDTH="20%" ALIGN = "LEFT">
	                       <label class="checkbox-inline"> 	<INPUT Type=Checkbox  style="border:0;" name="SalesTransaction" value="invoiceByOpen" <%=disabled%> <%=checkOption("invoiceByOpen")%>>     
	                        	&nbsp;  Invoice By Open</label>
	                        </TH>
	                        
	                        <TR>
	                        
	                        <TH WIDTH="20%" ALIGN = "LEFT">
	                       <label class="checkbox-inline"> 	<INPUT Type=Checkbox  style="border:0;" name="SalesTransaction" value="invoiceByDraft" <%=disabled%> <%=checkOption("invoiceByDraft")%>>     
	                        	&nbsp; Invoice By Draft</label>
	                        </TH>
	                        
	                        <TH WIDTH="20%" ALIGN = "LEFT">
	                       <label class="checkbox-inline"> 	<INPUT Type=Checkbox  style="border:0;" name="SalesTransaction" value="invoicePaymentSummary"  <%=disabled%> <%=checkOption("invoicePaymentSummary")%> >     
	                        	&nbsp;  Payment Received</label>
	                        </TH>
	                         
	                        <TH WIDTH="20%" ALIGN = "LEFT">
	                       <label class="checkbox-inline"> 	<INPUT Type=Checkbox  style="border:0;" name="SalesTransaction" value="pdcpaymentReceiveSummary"  <%=disabled%> <%=checkOption("pdcpaymentReceiveSummary")%> >     
	                        	&nbsp; PDC Payment Received</label>
	                        </TH>
	                        
	                        <TH WIDTH="20%" ALIGN = "LEFT">
		                  <label class="checkbox-inline">  <INPUT Type=Checkbox  style="border:0;" name="SalesTransaction" value="SOReturnSummary"   <%=disabled%> <%=checkOption("SOReturnSummary")%> >
		                    &nbsp; Sales Return</label>
		                    </TH>
	                        
	                         <TR>
	                        		                   
		                    <TH WIDTH="20%" ALIGN = "LEFT">
	                       <label class="checkbox-inline"> 	<INPUT Type=Checkbox  style="border:0;" name="SalesTransaction" value="customerCreditNote"  <%=disabled%> <%=checkOption("customerCreditNote")%> >     
	                        	&nbsp;  Credit Notes</label>
	                        </TH> --%>
                        </TABLE>
                        </div>
                      </div>
                      
                    
                      
<%-- <div class="panel panel-default">
<div class="panel-heading" style="background: #eaeafa"><strong>Rental / Consignment</strong></div>
<div class="panel-body">
     <TABLE class="table1" style="font-size:14px">
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
     <TABLE class="table1" style="font-size:14px">
                 <TR>
                    
                        
                      <TH WIDTH="20%" ALIGN = "LEFT">
                      <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;" name="URL" value="mulloctransfer" <%=disabled%> <%=checkOption("mulloctransfer")%>>  
                         &nbsp; Stock Move</label>
                         
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
                        	
                        <%-- 	<TR>
                      
                        <TH WIDTH="20%" ALIGN = "LEFT">
                      <label class="checkbox-inline">  <INPUT Type=Checkbox  style="border:0;" name="URL" value="kitdekit" <%=disabled%> <%=checkOption("kitdekit")%>>
                        &nbsp; Kitting De-Kitting</label>
                 
                        <TH WIDTH="20%" ALIGN = "LEFT">
                    <label class="checkbox-inline">    <INPUT Type=Checkbox  style="border:0;" name="URL" value="kitdekitwithbom" <%=disabled%> <%=checkOption("kitdekitwithbom")%>>
                        &nbsp; Kitting De-Kitting(with ref BOM)</label>
                        
                        <TH WIDTH="20%" ALIGN = "LEFT">
                      <label class="checkbox-inline">  <INPUT Type=Checkbox  style="border:0;" name="URL" value="bulkkitdekit" <%=disabled%> <%=checkOption("bulkkitdekit")%>>
                        &nbsp; Kitting De-Kitting(bulk with ref BOM) </label>                       
                                               	 
                            <TH WIDTH="20%" ALIGN = "LEFT">
                       <label class="checkbox-inline"> 	<INPUT Type=Checkbox  style="border:0;" name="Reports" value="kittingSumm"   <%=disabled%> <%=checkOption("kittingSumm")%> >
                        	&nbsp; Kitting/De-Kitting Summary</label>
                        	</TH>             --%>                                 
                        
                        <!-- </TH> -->
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
	                      <label class="checkbox-inline">  	<INPUT Type=Checkbox  style="border:0;" name="Reports" value="viewinventorybatchmultiuom"   <%=disabled%> <%=checkOption("viewinventorybatchmultiuom")%>>
	                        	&nbsp; Inventory Summary With Batch/Sno </label>
                         </TH>  
                    
                        <TH WIDTH="20%" ALIGN = "LEFT">
                        <label class="checkbox-inline">	<INPUT Type=Checkbox  style="border:0;" name="Reports" value="viewInventoryByProd"  <%=disabled%> <%=checkOption("viewInventoryByProd")%>>
                        	&nbsp; Inventory Summary With Total Quantity (with pcs)</label>
                        </TH>             

                        <TH WIDTH="20%" ALIGN = "LEFT">
                        <label class="checkbox-inline">	<INPUT Type=Checkbox  style="border:0;" name="Reports" value="msInvnopriceqty"   <%=disabled%> <%=checkOption("msInvnopriceqty")%>>
                        	&nbsp; Inventory Summary With Batch/Sno (with pcs)</label>
                        </TH>
                        <TR>       
                         
                       <TH WIDTH="20%" ALIGN = "LEFT">
                         <label class="checkbox-inline">   <INPUT Type=Checkbox  style="border:0;" name="Reports" value="msInvList"  <%=disabled%> <%=checkOption("msInvList")%>>
                            &nbsp; Inventory Summary (with min/max/zero qty) </label>
                       </TH>
                    
                         
                    	<TH WIDTH="20%" ALIGN = "LEFT">
                        	<label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="Reports" value="msInvListwithExpireD"   <%=disabled%> <%=checkOption("msInvListwithExpireD")%>>
                        	&nbsp; Inventory Summary (with expiry date)</label>
                        </TH>
                                                                  
                        
	                    <TH WIDTH="20%" ALIGN = "LEFT">
	                         <label class="checkbox-inline">   <INPUT Type=Checkbox  style="border:0;" name="Reports" value="msInvListwithcost"  <%=disabled%> <%=checkOption("msInvListwithcost")%>>
	                     &nbsp; Inventory Summary (with average cost)</label>
	                     </TH>     
                         
                       <TH WIDTH="20%" ALIGN = "LEFT">
                           <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;" name="Reports" value="inventoryAgingSummary"  <%=disabled%> <%=checkOption("inventoryAgingSummary")%>>
                            &nbsp; Inventory Aging Summary </label>
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
                          
<%-- <div class="panel panel-default">
    <div class="panel-heading" style="background: #eaeafa"><strong>Accounting - Home Page</strong></div>
    <div class="panel-body">
		<table>
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
    	<table>					
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
    	<table>					
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
    	<table>					
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
                    <TABLE class="table1" style="font-size:14px">
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
    	   <TABLE class="table1" style="font-size:14px">
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
    	<TABLE class="table1" style="font-size:14px">
                   <TR>
                        <TH WIDTH="20%" ALIGN = "LEFT">
                        <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="URL" value="pdaOutBoundIssue"  <%=disabled%> <%=checkOption("pdaOutBoundIssue")%>>
                        &nbsp; SALES PICK & ISSUE</label>
                        
                                             
                         <TH WIDTH="20%" ALIGN = "LEFT">
                        <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="URL" value="pdaMiscIssue"  <%=disabled%> <%=checkOption("pdaMiscIssue")%>>
                        &nbsp; GOODS ISSUE</label>
                 </TABLE>
                 </div>
                       </div>
 <div class="panel panel-default">
<div class="panel-heading" style="background: #eaeafa"><strong>PDA - In House</strong></div>
<div class="panel-body">
    	<TABLE class="table1" style="font-size:14px">
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
    	<TABLE class="table1" style="font-size:14px">
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
                <TABLE class="table1" style="font-size:14px">
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
     <TABLE class="table1" style="font-size:14px">
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
