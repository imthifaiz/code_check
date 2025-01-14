<%--New page design begin --%>
<%@page import="com.track.constants.IConstants"%>
<%
String title = "Create Accounting User Access Rights";
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
<%--New page design end --%>
<%
session= request.getSession();
String plant = (String) session.getAttribute("PLANT");
%>
<script type="text/javascript" src="js/userLevel.js"></script>
<script type="text/javascript" src="js/general.js"></script>
<script type="text/javascript">


function checkAll(isChk)
{
	 if (document.form.Home)
	    {
    if (document.form.Home.disabled == false)
    	document.form.Home.checked = isChk;
    for (i = 0; i < document.form.Home.length; i++)
    {
            if (document.form.Home[i].disabled == false)
        	document.form.Home[i].checked = isChk;
   
}
	    }
if (document.form.UserAdmin)
{
    if (document.form.UserAdmin.disabled == false)
    	document.form.UserAdmin.checked = isChk;
    for (i = 0; i < document.form.UserAdmin.length; i++)
    {
            if (document.form.UserAdmin[i].disabled == false)
        	document.form.UserAdmin[i].checked = isChk;
    }
}



    if (document.form.SystemMaster)
    {
        if (document.form.SystemMaster.disabled == false)
        	document.form.SystemMaster.checked = isChk;
        for (i = 0; i < document.form.SystemMaster.length; i++)
        {
                if (document.form.SystemMaster[i].disabled == false)
            	document.form.SystemMaster[i].checked = isChk;
        }
    }




    if (document.form.SystemAdmin)
    {
        if (document.form.SystemAdmin.disabled == false)
        	document.form.SystemAdmin.checked = isChk;
        for (i = 0; i < document.form.SystemAdmin.length; i++)
        {
                if (document.form.SystemAdmin[i].disabled == false)
            	document.form.SystemAdmin[i].checked = isChk;
        }
    }

    if (document.form.AccountingAdmin)
    {
        if (document.form.AccountingAdmin.disabled == false)
        	document.form.AccountingAdmin.checked = isChk;
        for (i = 0; i < document.form.AccountingAdmin.length; i++)
        {
                if (document.form.AccountingAdmin[i].disabled == false)
            	document.form.AccountingAdmin[i].checked = isChk;
        }
    }

    if (document.form.OrderAdmin)
    {
        if (document.form.OrderAdmin.disabled == false)
        	document.form.OrderAdmin.checked = isChk;
        for (i = 0; i < document.form.OrderAdmin.length; i++)
        {
                if (document.form.OrderAdmin[i].disabled == false)
            	document.form.OrderAdmin[i].checked = isChk;
        }
    }

    if (document.form.OrderConfiguration)
    {
        if (document.form.OrderConfiguration.disabled == false)
        	document.form.OrderConfiguration.checked = isChk;
        for (i = 0; i < document.form.OrderConfiguration.length; i++)
        {
                if (document.form.OrderConfiguration[i].disabled == false)
            	document.form.OrderConfiguration[i].checked = isChk;
        }
    }
    
    if (document.form.EmailConfiguration)
    {
        if (document.form.EmailConfiguration.disabled == false)
        	document.form.EmailConfiguration.checked = isChk;
        for (i = 0; i < document.form.EmailConfiguration.length; i++)
        {
                if (document.form.EmailConfiguration[i].disabled == false)
            	document.form.EmailConfiguration[i].checked = isChk;
        }
    }

    if (document.form.OrderManagement)
    {
        if (document.form.OrderManagement.disabled == false)
        	document.form.OrderManagement.checked = isChk;
        for (i = 0; i < document.form.OrderManagement.length; i++)
        {
                if (document.form.OrderManagement[i].disabled == false)
            	document.form.OrderManagement[i].checked = isChk;
        }
    }


    if (document.form.OrderManagement1)
    {
        if (document.form.OrderManagement1.disabled == false)
        	document.form.OrderManagement1.checked = isChk;
        for (i = 0; i < document.form.OrderManagement1.length; i++)
        {
                if (document.form.OrderManagement1[i].disabled == false)
            	document.form.OrderManagement1[i].checked = isChk;
        }
    }




    if (document.form.SalesEstimate)
    {
        if (document.form.SalesEstimate.disabled == false)
        	document.form.SalesEstimate.checked = isChk;
        for (i = 0; i < document.form.SalesEstimate.length; i++)
        {
                if (document.form.SalesEstimate[i].disabled == false)
            	document.form.SalesEstimate[i].checked = isChk;
        }
    }


   if (document.form.InHouse)
    {
        if (document.form.InHouse.disabled == false)
        	document.form.InHouse.checked = isChk;
        for (i = 0; i < document.form.InHouse.length; i++)
        {
                if (document.form.InHouse[i].disabled == false)
            	document.form.InHouse[i].checked = isChk;
        }
    }


    if (document.form.SalesTransaction)
    {
        if (document.form.SalesTransaction.disabled == false)
        	document.form.SalesTransaction.checked = isChk;
        for (i = 0; i < document.form.SalesTransaction.length; i++)
        {
                if (document.form.SalesTransaction[i].disabled == false)
            	document.form.SalesTransaction[i].checked = isChk;
        }
    }

    if (document.form.RentalTransaction)
    {
        if (document.form.RentalTransaction.disabled == false)
        	document.form.RentalTransaction.checked = isChk;
        for (i = 0; i < document.form.RentalTransaction.length; i++)
        {
                if (document.form.RentalTransaction[i].disabled == false)
            	document.form.RentalTransaction[i].checked = isChk;
        }
    }

    if (document.form.Reports)
    {
        if (document.form.Reports.disabled == false)
        	document.form.Reports.checked = isChk;
        for (i = 0; i < document.form.Reports.length; i++)
        {
                if (document.form.Reports[i].disabled == false)
            	document.form.Reports[i].checked = isChk;
        }
    }
    
    if (document.form.AccountingReports)
    {
        if (document.form.AccountingReports.disabled == false)
        	document.form.AccountingReports.checked = isChk;
        for (i = 0; i < document.form.AccountingReports.length; i++)
        {
                if (document.form.AccountingReports[i].disabled == false)
            	document.form.AccountingReports[i].checked = isChk;
        }
    }
    
    if (document.form.ActivityLogs)
    {
        if (document.form.ActivityLogs.disabled == false)
        	document.form.ActivityLogs.checked = isChk;
        for (i = 0; i < document.form.ActivityLogs.length; i++)
        {
                if (document.form.ActivityLogs[i].disabled == false)
            	document.form.ActivityLogs[i].checked = isChk;
        }
    }

    if (document.form.AccountingHome)
    {
        if (document.form.AccountingHome.disabled == false)
        	document.form.AccountingHome.checked = isChk;
        for (i = 0; i < document.form.AccountingHome.length; i++)
        {
                if (document.form.AccountingHome[i].disabled == false)
            	document.form.AccountingHome[i].checked = isChk;
        }
    }

   if (document.form.AccountingMaster)
    {
        if (document.form.AccountingMaster.disabled == false)
        	document.form.AccountingMaster.checked = isChk;
        for (i = 0; i < document.form.AccountingMaster.length; i++)
        {
                if (document.form.AccountingMaster[i].disabled == false)
            	document.form.AccountingMaster[i].checked = isChk;
        }
    }

    if (document.form.AccountingTransactions)
    {
        if (document.form.AccountingTransactions.disabled == false)
        	document.form.AccountingTransactions.checked = isChk;
        for (i = 0; i < document.form.AccountingTransactions.length; i++)
        {
                if (document.form.AccountingTransactions[i].disabled == false)
            	document.form.AccountingTransactions[i].checked = isChk;
        }
    }

    if (document.form.AccountingReports)
    {
        if (document.form.AccountingReports.disabled == false)
        	document.form.AccountingReports.checked = isChk;
        for (i = 0; i < document.form.AccountingReports.length; i++)
        {
                if (document.form.AccountingReports[i].disabled == false)
            	document.form.AccountingReports[i].checked = isChk;
        }
    }


   if (document.form.PdaPurchaseTransaction)
    {
        if (document.form.PdaPurchaseTransaction.disabled == false)
        	document.form.PdaPurchaseTransaction.checked = isChk;
        for (i = 0; i < document.form.PdaPurchaseTransaction.length; i++)
        {
                if (document.form.PdaPurchaseTransaction[i].disabled == false)
            	document.form.PdaPurchaseTransaction[i].checked = isChk;
        }
    }


  if (document.form.PdaSalesTransaction)
    {
        if (document.form.PdaSalesTransaction.disabled == false)
        	document.form.PdaSalesTransaction.checked = isChk;
        for (i = 0; i < document.form.PdaSalesTransaction.length; i++)
        {
                if (document.form.PdaSalesTransaction[i].disabled == false)
            	document.form.PdaSalesTransaction[i].checked = isChk;
        }
    }


    if (document.form.PdaInHouse)
    {
        if (document.form.PdaInHouse.disabled == false)
        	document.form.PdaInHouse.checked = isChk;
        for (i = 0; i < document.form.PdaInHouse.length; i++)
        {
                if (document.form.PdaInHouse[i].disabled == false)
            	document.form.PdaInHouse[i].checked = isChk;
        }
    }
}



function checkAllHome(isChk)
{
    if (document.form.Home)
    {
        if (document.form.Home.disabled == false)
        	document.form.Home.checked = isChk;
        for (i = 0; i < document.form.Home.length; i++)
        {
                if (document.form.Home[i].disabled == false)
            	document.form.Home[i].checked = isChk;
        }
    }
}

function checkAllUserAdmin(isChk)
{
    if (document.form.UserAdmin)
    {
        if (document.form.UserAdmin.disabled == false)
        	document.form.UserAdmin.checked = isChk;
        for (i = 0; i < document.form.UserAdmin.length; i++)
        {
                if (document.form.UserAdmin[i].disabled == false)
            	document.form.UserAdmin[i].checked = isChk;
        }
    }
}

function checkAllSystemMaster(isChk)
{
    if (document.form.SystemMaster)
    {
        if (document.form.SystemMaster.disabled == false)
        	document.form.SystemMaster.checked = isChk;
        for (i = 0; i < document.form.SystemMaster.length; i++)
        {
                if (document.form.SystemMaster[i].disabled == false)
            	document.form.SystemMaster[i].checked = isChk;
        }
    }
}

function checkAllSystemAdmin(isChk)
{
    if (document.form.SystemAdmin)
    {
        if (document.form.SystemAdmin.disabled == false)
        	document.form.SystemAdmin.checked = isChk;
        for (i = 0; i < document.form.SystemAdmin.length; i++)
        {
                if (document.form.SystemAdmin[i].disabled == false)
            	document.form.SystemAdmin[i].checked = isChk;
        }
    }
}

function checkAllAccountingAdmin(isChk)
{
    if (document.form.AccountingAdmin)
    {
        if (document.form.AccountingAdmin.disabled == false)
        	document.form.AccountingAdmin.checked = isChk;
        for (i = 0; i < document.form.AccountingAdmin.length; i++)
        {
                if (document.form.AccountingAdmin[i].disabled == false)
            	document.form.AccountingAdmin[i].checked = isChk;
        }
    }
}


function checkAllOrderAdmin(isChk)
{
    if (document.form.OrderAdmin)
    {
        if (document.form.OrderAdmin.disabled == false)
        	document.form.OrderAdmin.checked = isChk;
        for (i = 0; i < document.form.OrderAdmin.length; i++)
        {
                if (document.form.OrderAdmin[i].disabled == false)
            	document.form.OrderAdmin[i].checked = isChk;
        }
    }
}

function checkAllOrderConfiguration(isChk)
{
    if (document.form.OrderConfiguration)
    {
        if (document.form.OrderConfiguration.disabled == false)
        	document.form.OrderConfiguration.checked = isChk;
        for (i = 0; i < document.form.OrderConfiguration.length; i++)
        {
                if (document.form.OrderConfiguration[i].disabled == false)
            	document.form.OrderConfiguration[i].checked = isChk;
        }
    }
}

function checkAllEmailConfiguration(isChk)
{
    if (document.form.EmailConfiguration)
    {
        if (document.form.EmailConfiguration.disabled == false)
        	document.form.EmailConfiguration.checked = isChk;
        for (i = 0; i < document.form.EmailConfiguration.length; i++)
        {
                if (document.form.EmailConfiguration[i].disabled == false)
            	document.form.EmailConfiguration[i].checked = isChk;
        }
    }
}

function checkAllOrderManagement(isChk)
{
    if (document.form.OrderManagement)
    {
        if (document.form.OrderManagement.disabled == false)
        	document.form.OrderManagement.checked = isChk;
        for (i = 0; i < document.form.OrderManagement.length; i++)
        {
                if (document.form.OrderManagement[i].disabled == false)
            	document.form.OrderManagement[i].checked = isChk;
        }
    }
}

function checkAllOrderManagement1(isChk)
{
    if (document.form.OrderManagement1)
    {
        if (document.form.OrderManagement1.disabled == false)
        	document.form.OrderManagement1.checked = isChk;
        for (i = 0; i < document.form.OrderManagement1.length; i++)
        {
                if (document.form.OrderManagement1[i].disabled == false)
            	document.form.OrderManagement1[i].checked = isChk;
        }
    }
}

function checkAllSalesEstimate(isChk)
{
    if (document.form.SalesEstimate)
    {
        if (document.form.SalesEstimate.disabled == false)
        	document.form.SalesEstimate.checked = isChk;
        for (i = 0; i < document.form.SalesEstimate.length; i++)
        {
                if (document.form.SalesEstimate[i].disabled == false)
            	document.form.SalesEstimate[i].checked = isChk;
        }
    }
}

function checkAllInHouse(isChk)
{
    if (document.form.InHouse)
    {
        if (document.form.InHouse.disabled == false)
        	document.form.InHouse.checked = isChk;
        for (i = 0; i < document.form.InHouse.length; i++)
        {
                if (document.form.InHouse[i].disabled == false)
            	document.form.InHouse[i].checked = isChk;
        }
    }
}

function checkAllSalesTransaction(isChk)
{
    if (document.form.SalesTransaction)
    {
        if (document.form.SalesTransaction.disabled == false)
        	document.form.SalesTransaction.checked = isChk;
        for (i = 0; i < document.form.SalesTransaction.length; i++)
        {
                if (document.form.SalesTransaction[i].disabled == false)
            	document.form.SalesTransaction[i].checked = isChk;
        }
    }
}

function checkAllRentalTransaction(isChk)
{
    if (document.form.RentalTransaction)
    {
        if (document.form.RentalTransaction.disabled == false)
        	document.form.RentalTransaction.checked = isChk;
        for (i = 0; i < document.form.RentalTransaction.length; i++)
        {
                if (document.form.RentalTransaction[i].disabled == false)
            	document.form.RentalTransaction[i].checked = isChk;
        }
    }
}

function checkAllReports(isChk)
{
    if (document.form.Reports)
    {
        if (document.form.Reports.disabled == false)
        	document.form.Reports.checked = isChk;
        for (i = 0; i < document.form.Reports.length; i++)
        {
                if (document.form.Reports[i].disabled == false)
            	document.form.Reports[i].checked = isChk;
        }
    }
}

function checkAllAccountingReports(isChk)
{
    if (document.form.AccountingReports)
    {
        if (document.form.AccountingReports.disabled == false)
        	document.form.AccountingReports.checked = isChk;
        for (i = 0; i < document.form.AccountingReports.length; i++)
        {
                if (document.form.AccountingReports[i].disabled == false)
            	document.form.AccountingReports[i].checked = isChk;
        }
    }
}

function checkAllActivityLogs(isChk)
{
    if (document.form.ActivityLogs)
    {
        if (document.form.ActivityLogs.disabled == false)
        	document.form.ActivityLogs.checked = isChk;
        for (i = 0; i < document.form.ActivityLogs.length; i++)
        {
                if (document.form.ActivityLogs[i].disabled == false)
            	document.form.ActivityLogs[i].checked = isChk;
        }
    }
}

function checkAllAccountingHome(isChk)
{
    if (document.form.AccountingHome)
    {
        if (document.form.AccountingHome.disabled == false)
        	document.form.AccountingHome.checked = isChk;
        for (i = 0; i < document.form.AccountingHome.length; i++)
        {
                if (document.form.AccountingHome[i].disabled == false)
            	document.form.AccountingHome[i].checked = isChk;
        }
    }
}

function checkAllAccountingMaster(isChk)
{
    if (document.form.AccountingMaster)
    {
        if (document.form.AccountingMaster.disabled == false)
        	document.form.AccountingMaster.checked = isChk;
        for (i = 0; i < document.form.AccountingMaster.length; i++)
        {
                if (document.form.AccountingMaster[i].disabled == false)
            	document.form.AccountingMaster[i].checked = isChk;
        }
    }
}

function checkAllAccountingTransactions(isChk)
{
    if (document.form.AccountingTransactions)
    {
        if (document.form.AccountingTransactions.disabled == false)
        	document.form.AccountingTransactions.checked = isChk;
        for (i = 0; i < document.form.AccountingTransactions.length; i++)
        {
                if (document.form.AccountingTransactions[i].disabled == false)
            	document.form.AccountingTransactions[i].checked = isChk;
        }
    }
}

function checkAllAccountingReports(isChk)
{
    if (document.form.AccountingReports)
    {
        if (document.form.AccountingReports.disabled == false)
        	document.form.AccountingReports.checked = isChk;
        for (i = 0; i < document.form.AccountingReports.length; i++)
        {
                if (document.form.AccountingReports[i].disabled == false)
            	document.form.AccountingReports[i].checked = isChk;
        }
    }
}

function checkAllPdaPurchaseTransaction(isChk)
{
    if (document.form.PdaPurchaseTransaction)
    {
        if (document.form.PdaPurchaseTransaction.disabled == false)
        	document.form.PdaPurchaseTransaction.checked = isChk;
        for (i = 0; i < document.form.PdaPurchaseTransaction.length; i++)
        {
                if (document.form.PdaPurchaseTransaction[i].disabled == false)
            	document.form.PdaPurchaseTransaction[i].checked = isChk;
        }
    }
}

function checkAllPdaSalesTransaction(isChk)
{
    if (document.form.PdaSalesTransaction)
    {
        if (document.form.PdaSalesTransaction.disabled == false)
        	document.form.PdaSalesTransaction.checked = isChk;
        for (i = 0; i < document.form.PdaSalesTransaction.length; i++)
        {
                if (document.form.PdaSalesTransaction[i].disabled == false)
            	document.form.PdaSalesTransaction[i].checked = isChk;
        }
    }
}

function checkAllPdaInHouse(isChk)
{
    if (document.form.PdaInHouse)
    {
        if (document.form.PdaInHouse.disabled == false)
        	document.form.PdaInHouse.checked = isChk;
        for (i = 0; i < document.form.PdaInHouse.length; i++)
        {
                if (document.form.PdaInHouse[i].disabled == false)
            	document.form.PdaInHouse[i].checked = isChk;
        }
    }
}



function checkAlltb(field)
{
	for (i = 0; i < field.length; i++)
		System.out.print(field);
		field[i].checked = true ;
}
</script>
<%--New page design begin --%>
<div class="container-fluid m-t-20">
	 <div class="box"> 
            <div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title"><%=title %></h1>
              <h1 style="font-size: 18px; cursor: pointer;"
				class="box-title pull-right"
				onclick="window.location.href='GroupSummaryAccounting.jsp'">
				<i class="glyphicon glyphicon-remove"></i>
			</h1>
		</div>
		
 <div class="box-body">
<%--New page design end --%>
<form name="form" class="form-horizontal" method="POST" action="userLevelAccountingSubmit.jsp" onSubmit="return validateLevel()">
  <div class="form-group">
  		<label class="control-label col-sm-4" for="Product Class ID">User Access Rights Group</label>
  		<div class="col-sm-4"><input MAXLENGTH=20 type="text" name="LEVEL_NAME" size="50" class="form-control"></div>
  </div>
  <div class="form-group">
  		<label class="control-label col-sm-4" for="Product Class ID">Remarks</label>
  		<div class="col-sm-4"><INPUT size="50" MAXLENGTH=60 name="REMARKS" class="form-control"></div>
  </div>
    <div class="row">
  		<div class="col-12 col-sm-12"><INPUT Type=Checkbox class="form-check-input"  style="border:0;"  onclick="return checkAll(this.checked);">
                       &nbsp; Select/Unselect All  </div>
  </div>
  <div class="row">
  		<div class="col-12 col-sm-12"><INPUT Type=Checkbox class="form-check-input"  style="border:0;" name="Home"  onclick="return checkAllHome(this.checked);">
                      &nbsp; Select/Unselect Dashboard  </div>
  </div>
 
  <div class="panel panel-default">
    <div class="panel-heading" style="background: #eaeafa"><strong>Dashboard</strong></div>
    <div class="panel-body">
    <TABLE class="table1">
                <TR>
                 <!-- <TH WIDTH="20%" ALIGN = "LEFT">
                  <INPUT Type=Checkbox  style="border:0;" name="Home" value="homepurchasetotal">
                  &nbsp; Total Purchase
                  
                   <TH WIDTH="20%" ALIGN = "LEFT">
                  <INPUT Type=Checkbox  style="border:0;" name="Home" value="homesaletotal">
                  &nbsp; Total Sales
                  
                  
                   <TH WIDTH="20%" ALIGN = "LEFT">
                  <INPUT Type=Checkbox  style="border:0;" name="Home" value="homeallactivity">
                  &nbsp; See All Activity
                  
                      <TH WIDTH="20%" ALIGN = "LEFT">
                  <INPUT Type=Checkbox  style="border:0;" name="Home" value="homereceivedproductgrid">
                  &nbsp;Top Received Product 
                  <TR>
                   <TH WIDTH="20%" ALIGN = "LEFT">
                  <INPUT Type=Checkbox  style="border:0;" name="Home" value="homeissuedproductgrid">
                  &nbsp; Top Issued Product
                  
                  <TH WIDTH="20%" ALIGN = "LEFT">
                  <INPUT Type=Checkbox  style="border:0;" name="Home" value="homeexpiringproductgrid">
                  &nbsp;Expiring Products
                  
                   <TH WIDTH="20%" ALIGN = "LEFT">
                  <INPUT Type=Checkbox  style="border:0;" name="Home" value="homestockproductgrid">
                  &nbsp; Stock Stock Replenishment Products -->
                  
                  <TH WIDTH="20%" ALIGN = "LEFT">
                <label class="checkbox-inline">  <INPUT Type=Checkbox  style="border:0;" name="Home" value="homeaccountingmanaget">
                  &nbsp; Accounting Management</label>
                  
                   <TH WIDTH="20%" ALIGN = "LEFT">
                  <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;" name="Home" value="homeaccounting">
                  &nbsp; Accounting  </label>                
                  
                   <!-- <TH WIDTH="20%" ALIGN = "LEFT">
                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="Home" value="homepurchase">
                  &nbsp; Purchase</label>
                  
                      <TH WIDTH="20%" ALIGN = "LEFT">
                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="Home" value="homesales">
                  &nbsp; Sales</label> 
                  <TR>
                   <TH WIDTH="20%" ALIGN = "LEFT">
                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="Home" value="homewarehouse">
                  &nbsp; Warehouse</label>   -->      
                          
                      </TABLE>
         </div>
  </div>
 
  <div class="row">
  		<div class="col-12 col-sm-12"><INPUT Type=Checkbox class="form-check-input"  style="border:0;" name="UserAdmin"  onclick="return checkAllUserAdmin(this.checked);">
                        &nbsp; Select/Unselect User Admin </div>
  </div>
 
 <div class="panel panel-default">
    <div class="panel-heading" style="background: #eaeafa"><strong>User Admin</strong></div>
    <div class="panel-body">
    <TABLE class="table1">
                    <TR>
                    <%if(plant.equalsIgnoreCase("track")){%>
                        <TH WIDTH="20%" ALIGN = "LEFT">
                        <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="UserAdmin" value="companySummry">
                        &nbsp; Summary &#45; Company Details </label>
                        <TH WIDTH="20%" ALIGN = "LEFT">
                        <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="UserAdmin" value="uaMaintCpny">
                        &nbsp; Edit Your Company Profile</label>
                        <TH WIDTH="20%" ALIGN = "LEFT">
                        <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="UserAdmin" value="uaCreateCpny">
                        &nbsp; Create Company Details</label>
                        <TH WIDTH="20%" ALIGN = "LEFT">
                        <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="UserAdmin" value="uaMaintAcct">
                        &nbsp;  Summary &#45; User Access Rights Group Details</label>
                        <%}%>
                     <TR>
                       <TH WIDTH="20%" ALIGN = "LEFT">
                          <%if(!plant.equalsIgnoreCase("track")){%>
                        <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="UserAdmin" value="uaMaintAcct">
                        &nbsp;  Summary &#45; User Access Rights Group Details<%}%></label> 
                        
                      
                    <TH WIDTH="20%" ALIGN = "LEFT">
                        <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="UserAdmin" value="maintGroup">
                        &nbsp; Edit User Access Rights Group Details</label>
                    <TH WIDTH="20%" ALIGN = "LEFT">
                        <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="UserAdmin" id="USER" value="uaNewAcct">
                        &nbsp; Create User Access Rights Group Details</label>
                     <TH WIDTH="20%" ALIGN = "LEFT">
                        <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="UserAdmin" value="uaMaintLevel">
                        &nbsp;  Summary &#45; User/Customer Details</label>
                     <TR>
                     <TH WIDTH="20%" ALIGN = "LEFT">
                        <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="UserAdmin" value="maintUser">
                        &nbsp;  Edit User Details</label>
                        
                      <TH WIDTH="20%" ALIGN = "LEFT">
                        <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="UserAdmin" value="uaChngPwd">
                        &nbsp; Edit Password Details</label>  
                        
                        
                      <TH WIDTH="20%" ALIGN = "LEFT">
                        <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="UserAdmin" value="customerMaintCpny">
                        &nbsp;  Edit Your Company Profile</label>
                     
                       
                      
                        <TH WIDTH="20%" ALIGN = "LEFT">  <%if(plant.equalsIgnoreCase("track")){%>
                        <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="UserAdmin" value="uaNewLevel">
                        &nbsp; Create User Details</label>
                          <TH WIDTH="20%" ALIGN = "LEFT">
                           <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="UserAdmin" value="uaauthcmpy">&nbsp;Authorise Company Details<%}%></label>
                      </TABLE>
         </div>
  </div>
  
    <div class="row">
  		<div class="col-12 col-sm-12"><INPUT Type=Checkbox class="form-check-input"  style="border:0;" name="SystemMaster"  onclick="return checkAllSystemMaster(this.checked);">
                        &nbsp; Select/Unselect System Master</div>
  </div>
   <div class="panel panel-default">
    <div class="panel-heading" style="background: #eaeafa"><strong>System Master</strong></div>
    <div class="panel-body">
    	<TABLE>
             <TR>
                  <TH WIDTH="20%" ALIGN = "LEFT">
                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SystemMaster" value="itemsummry" >
                  &nbsp; Summary &#45; Product Details</label>
                  
                     <TH WIDTH="20%" ALIGN = "LEFT">
                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SystemMaster" value="alternateItemSummary" >
                  &nbsp; Summary &#45; Alternate Products Details</label>
                  
                  <TH WIDTH="20%" ALIGN = "LEFT">
                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SystemMaster" value="maintItem" >
                  &nbsp;  Edit Product Details</label> 
                  
                  <TH WIDTH="20%" ALIGN = "LEFT">
                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SystemMaster" value="uaItemMast">
                  &nbsp; Create Product Details</label>
                  
                 <TR>
                  <TH WIDTH="20%" ALIGN = "LEFT">
                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SystemMaster" value="alternatebrandsummary"  >
                  &nbsp; Summary/Edit &#45; Alternate Brand Product Details</label>
                  
                   <TH WIDTH="20%" ALIGN = "LEFT">
                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SystemMaster" value="createalternatbrandproduct">
                  &nbsp; Create Alternate Brand Product Details</label>
                  
                 
                  <TH WIDTH="20%" ALIGN = "LEFT">
                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SystemMaster" value="prdClsSummary" >
                  &nbsp; Summary &#45; Product Class Details</label>
          
             
                   <TH WIDTH="20%" ALIGN = "LEFT">
                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SystemMaster" value="maintPrdCls" >
                  &nbsp; Edit Product Class Details</label>
                <TR>
                  <TH WIDTH="20%" ALIGN = "LEFT">
                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SystemMaster" value="uaPrdCls" >
                  &nbsp; Create Product Class Details</label>
               
                  <TH WIDTH="20%" ALIGN = "LEFT">
                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SystemMaster" value="prdTypeSummary" >
                  &nbsp; Summary &#45; Product Type Details</label> 
                  
                  <TH WIDTH="20%" ALIGN = "LEFT">
                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SystemMaster" value="maintPrdType" >
                  &nbsp; Edit Product Type Details</label>
                  
             
             	  <TH WIDTH="20%" ALIGN = "LEFT">
                  	<label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SystemMaster" value="uaPrdType"  >
                  &nbsp; Create Product Type Details</label>
                 <TR>
                  	<TH WIDTH="20%" ALIGN = "LEFT">
	                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SystemMaster" value="prdBrandSummary"  >
	                  &nbsp; Summary &#45; Product Brand Details</label> 
	           
	                  <TH WIDTH="20%" ALIGN = "LEFT">
	                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SystemMaster" value="maintPrdBrand"  >
	                  &nbsp; Edit Product Brand Details</label>
	                  
	                  <TH WIDTH="20%" ALIGN = "LEFT">
	                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SystemMaster" value="createPrdBrand"  >
	                  &nbsp; Create Product Brand Details</label>
	                  
	                  
	                  <TH WIDTH="20%" ALIGN = "LEFT">
                      <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SystemMaster" value="uomsummry" >
                       &nbsp; Summary &#45; Unit of Measure (UOM) Details</label>
                 	<TR>
	                    <TH WIDTH="20%" ALIGN = "LEFT">
	                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SystemMaster" value="maintUom" >
	                  &nbsp; Edit Unit of Measure (UOM) Details</label>
	                
	                  <TH WIDTH="20%" ALIGN = "LEFT">
	                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SystemMaster" value="uommst" >
	                  &nbsp; Create Unit of Measure (UOM) Details</label>  
	                 
	                   <TH WIDTH="20%" ALIGN = "LEFT">
	                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SystemMaster" value="locsummry"  >
	                  &nbsp; Summary &#45; Location Details</label>  
	                  
	                  <TH WIDTH="20%" ALIGN = "LEFT">
	                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SystemMaster" value="maintLoc"  >
	                  &nbsp; Edit Location Details</label>
                 <TR>
                  
	                  <TH WIDTH="20%" ALIGN = "LEFT">
	                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SystemMaster" value="msItemLoc" >
	                  &nbsp;  Create Location Details</label>  
	                  
	                    <TH WIDTH="20%" ALIGN = "LEFT">
	                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SystemMaster" value="loctypesummry"  >
	                  &nbsp; Summary &#45; Location Type Details</label>
	                  
	                 
	                  <TH WIDTH="20%" ALIGN = "LEFT">
	                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SystemMaster" value="maintLocType" >
	                  &nbsp; Edit Location Type Details</label>
	                  
	                  <TH WIDTH="20%" ALIGN = "LEFT">
	                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SystemMaster" value="createLocType"  >
	                  &nbsp;  Create Location Type Details</label> 
                <TR>   
	                   <TH WIDTH="20%" ALIGN = "LEFT">
	                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SystemMaster" value="rsnsummry"  >
	                  &nbsp; Summary &#45; Reason Code Details</label>
	                  
	                    <TH WIDTH="20%" ALIGN = "LEFT">
	                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SystemMaster" value="maintReason"  >
	                  &nbsp; Edit Reason Code Details</label>
	                  
	                  <TH WIDTH="20%" ALIGN = "LEFT">
	                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SystemMaster" value="rsnmst" >
	                  &nbsp; Create Reason Code Details</label>
	         </TABLE>
    </div>
  </div>
   <div class="row">
  		<div class="col-12 col-sm-12"><INPUT Type=Checkbox class="form-check-input"  style="border:0;" name="SystemAdmin"  onclick="return checkAllSystemAdmin(this.checked);">
                       &nbsp; Select/Unselect System Admin  </div>
  </div>
   <div class="panel panel-default">
    <div class="panel-heading" style="background: #eaeafa"><strong>System Admin</strong></div>
    <div class="panel-body">
    <TABLE>
             <TR>                 
                  <TH WIDTH="20%" ALIGN = "LEFT">
                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SystemAdmin" value="vendorsummry" >
                  &nbsp;  Summary &#45; Supplier Details</label>
                  
                   <TH WIDTH="20%" ALIGN = "LEFT">
                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SystemAdmin" value="supplierdiscountsummary " >
                  &nbsp; Summary &#45; Supplier Discount</label>
                  
                  <TH WIDTH="20%" ALIGN = "LEFT">
                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SystemAdmin" value="maintVendor" >
                  &nbsp; Edit Supplier Details</label>
                  
                  <TH WIDTH="20%" ALIGN = "LEFT">
                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SystemAdmin" value="uaVendMst">
                  &nbsp; Create Supplier Details</label>  
                 <TR>    
	                  <TH WIDTH="20%" ALIGN = "LEFT">
	                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SystemAdmin" value="supplierTypeSummary" >
	                  &nbsp; Summary/Edit &#45; Supplier Type Details</label>
	                   
	                  <TH WIDTH="20%" ALIGN = "LEFT">
	                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SystemAdmin" value="createSupplierType" >
	                  &nbsp; Create Supplier Type Details</label>
	                  
	                  <TH WIDTH="20%" ALIGN = "LEFT">
	                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SystemAdmin" value="customersummry" >
	                  &nbsp; Summary &#45; Customer Details</label>
	                         
	                  <TH WIDTH="20%" ALIGN = "LEFT">
	                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SystemAdmin" value="maintCustomer" >
	                  &nbsp;  Edit Customer Details</label>
                 <TR>               
                                           
	                  <TH WIDTH="20%" ALIGN = "LEFT">
	                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SystemAdmin" value="uaCustMst" >
	                  &nbsp; Create Customer Details</label> 
	                  
	                  <TH WIDTH="20%" ALIGN = "LEFT">
	                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SystemAdmin" value="customerdiscountsummary" >
	                   &nbsp; Summary &#45; Customer Discount</label>
	                  
	                   <TH WIDTH="20%" ALIGN = "LEFT">
	                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SystemAdmin" value="ctsummry" >
	                 &nbsp; Summary &#45; Customer Type Details</label>
	                 
	                    <TH WIDTH="20%" ALIGN = "LEFT">
	                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SystemAdmin" value="uaCtMst" >
	                  &nbsp; Edit Customer Type Details</label> 
                  <TR>    
	                  <TH WIDTH="20%" ALIGN = "LEFT">
	                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SystemAdmin" value="maintCt" >
	                  &nbsp; Create Customer Type Details</label> 
	                  
	                  
	               
	                 <!--  <TH WIDTH="20%" ALIGN = "LEFT">
	                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SystemAdmin" value="ualoanview" >
	                  &nbsp; Summary &#45; Rental Customer Details</label>
	                         
	                   <TH WIDTH="20%" ALIGN = "LEFT">
	        		  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SystemAdmin" value="uamaintln" >
	                  &nbsp; Edit Rental Customer Details</label>
	             
	                  <TH WIDTH="20%" ALIGN = "LEFT">
	                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SystemAdmin" value="uacratln" >
	                  &nbsp;  Create Rental Customer Details</label>
              <TR>         
	                  <TH WIDTH="20%" ALIGN = "LEFT">
	                   <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SystemAdmin" value="uatransview" >
	                  &nbsp;  Summary &#45; Consignment Customer Details</label>
	               
	                  <TH WIDTH="20%" ALIGN = "LEFT">
	                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SystemAdmin" value="uamaintrans" >
	                  &nbsp; Edit Consignment Customer Details</label>
	            
	                  <TH WIDTH="20%" ALIGN = "LEFT">
	                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SystemAdmin" value="uacrattrans" >
	                  &nbsp; Create Consignment Customer Details</label>  -->
	               
	                  <TH WIDTH="20%" ALIGN = "LEFT">
	                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SystemAdmin" value="employeesummry" >
	                  &nbsp; Employee</label>
                     
	                  <TH WIDTH="20%" ALIGN = "LEFT">
	                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SystemAdmin" value="importmasterdata" >
	                  &nbsp; Import Master Data</label>
	              
	                  <TH WIDTH="20%" ALIGN = "LEFT">
	                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SystemAdmin" value="importdata" >
	                  &nbsp; Import Inventory And Transaction Data</label>
                                
        	 </TABLE>
    </div>
    </div>
    
      <div class="row">
  		<div class="col-12 col-sm-12"><INPUT Type=Checkbox class="form-check-input"  style="border:0;" name="OrderAdmin"  onclick="return checkAllOrderAdmin(this.checked);">
                        &nbsp; Select/Unselect Order Admin  </div>
  </div>
<div class="panel panel-default">
    <div class="panel-heading" style="background: #eaeafa"><strong>Order Admin</strong></div>
    <div class="panel-body"> 
		<TABLE>
	             <TR>                 
       	                      
                   <TH WIDTH="20%" ALIGN = "LEFT">
                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="OrderAdmin" value="ordtypesumry" >
                  &nbsp;  Summary &#45; Order Type Details</label>  
                  
                         
                  <TH WIDTH="20%" ALIGN = "LEFT">
                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="OrderAdmin" value="maintordtype" >
                  &nbsp; Edit Order Type Details</label>
  
                  <TH WIDTH="20%" ALIGN = "LEFT">
                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="OrderAdmin" value="cratordtype" >
                  &nbsp; Create Order Type Details</label>  
                  
                  
                  <TH WIDTH="20%" ALIGN = "LEFT">
                  <label class="checkbox-inline">    <INPUT Type=Checkbox  style="border:0;" name="OrderAdmin" value="createpaymenttype" >
                      &nbsp; Summary &#45; Create Payment Type Details</label>
                 <TR>     
                   <TH WIDTH="20%" ALIGN = "LEFT">
                      <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="OrderAdmin" value="editpaymenttype" >
                      &nbsp; Edit Payment Type Details</label>
                  
                   <TH WIDTH="20%" ALIGN = "LEFT">
                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="OrderAdmin" value="gstSummry" >
                  &nbsp; Summary &#45; VAT Details</label>  
                  
                    <TH WIDTH="20%" ALIGN = "LEFT">
                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="OrderAdmin" value="maintgsttype" >
                  &nbsp; Edit VAT Details</label>
                 
                  
                  <TH WIDTH="20%" ALIGN = "LEFT">
                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="OrderAdmin" value="gsttype" >
                  &nbsp; Create VAT Details</label>
                 <TR>           
                  <TH WIDTH="20%" ALIGN = "LEFT">
                 <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="OrderAdmin" value="cursummry" >
                  &nbsp; Summary &#45; Currency Details</label>
                 
                  <TH WIDTH="20%" ALIGN = "LEFT">
                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="OrderAdmin" value="maintCurency" >
                  &nbsp; Edit Currency Details</label>
              
                  <TH WIDTH="20%" ALIGN = "LEFT">
                   <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="OrderAdmin" value="cratCurency" >
                  &nbsp; Create Currency Details</label>
                  
                
                  
                     <TH WIDTH="20%" ALIGN = "LEFT">
                 <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="OrderAdmin" value="createhscode" >
                  &nbsp; Create HSCODE Details</label>
                   <TR> 
                  
                    <TH WIDTH="20%" ALIGN = "LEFT">
                 <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name=OrderAdmin value="edithscode" >
                  &nbsp; Edit HSCODE Details</label>
                  
                   <TH WIDTH="20%" ALIGN = "LEFT">
                 <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="OrderAdmin" value="createcoo" >
                  &nbsp; Create COO Details</label>
                  
                  
                    <TH WIDTH="20%" ALIGN = "LEFT">
                 <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="OrderAdmin" value="editcoo" >
                  &nbsp; Edit COO Details</label>
                  
                  
                     <TH WIDTH="20%" ALIGN = "LEFT">
                 <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="OrderAdmin" value="createorderremarks" >
                  &nbsp; Create Remarks Details</label>
                  
                <TR>    
                    <TH WIDTH="20%" ALIGN = "LEFT">
                 <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="OrderAdmin" value="editorderremarks" >
                  &nbsp; Edit Remarks Details</label>
                  
                    <TH WIDTH="20%" ALIGN = "LEFT">
                 <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="OrderAdmin" value="createorderincoterms" >
                  &nbsp; Create INCOTERM Details</label>
                  
                  
                    <TH WIDTH="20%" ALIGN = "LEFT">
                 <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="OrderAdmin" value="editorderincoterms" >
                  &nbsp; Edit INCTORERM Details</label>
               
                     <TH WIDTH="20%" ALIGN = "LEFT">
                 <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="OrderAdmin" value="createorderfooter" >
                  &nbsp; Create Footer Details</label>
                   <TR>  
                  
                    <TH WIDTH="20%" ALIGN = "LEFT">
                 <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="OrderAdmin" value="editorderfooter" >
                  &nbsp; Edit Footer Details</label>
                                    
                     <TH WIDTH="20%" ALIGN = "LEFT">
                 <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="OrderAdmin" value="packingMaster" >
                  &nbsp; Create Packing Details</label> 
                  
                   <TH WIDTH="20%" ALIGN = "LEFT">
                 <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="OrderAdmin" value="packingMasterSummary" >
                  &nbsp; Edit Packing Details </label>            
                
              </TABLE>
	</div>
</div>    

  <div class="row">
  		<div class="col-12 col-sm-12"><INPUT Type=Checkbox class="form-check-input"  style="border:0;" name="OrderConfiguration"  onclick="return checkAllOrderConfiguration(this.checked);">
                        &nbsp; Select/Unselect Printout Configuration  </div>
  </div>

<div class="panel panel-default">
    <div class="panel-heading" style="background: #eaeafa"><strong>Printout Configuration</strong></div>
    <div class="panel-body"> 
    	<TABLE>
	             <TR>     
	              <TH WIDTH="20%" ALIGN = "LEFT">
                    <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="OrderConfiguration" value="editibpt"  >
                    &nbsp; Edit Purchase Order Printout</label>            
                 
       				 <TH WIDTH="23%" ALIGN = "LEFT">
                    <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="OrderConfiguration" value="editibwithcostpt" >
                    &nbsp; Edit Purchase Order Printout (with cost)</label>
                    
                    <TH WIDTH="23%" ALIGN = "LEFT">
                     <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="OrderConfiguration" value="editInboundOrderMailMsg"  >
                     &nbsp; Edit Purchase Order Email Message</label>
                     
                     <TH WIDTH="23%" ALIGN = "LEFT">
                    <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="OrderConfiguration" value="editestwithpt" >
                    &nbsp; Edit Sales Estimate Order Printout (with price)</label>
                  <TR>   
                    <TH WIDTH="23%" ALIGN = "LEFT">
                    <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="OrderConfiguration" value="editEstimateOrderMailMsg"  >
                    &nbsp; Edit Sales Estimate Order Email Message</label>
                   
                    <TH WIDTH="20%" ALIGN = "LEFT">
                    <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="OrderConfiguration" value="editobpt" >
                    &nbsp; Edit Sales Order Printout</label>
                  
                    <TH WIDTH="23%" ALIGN = "LEFT">
                    <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="OrderConfiguration" value="editobwithpt" >
                    &nbsp; Edit Sales Order Printout (with price)</label>
                    
                    <TH WIDTH="23%" ALIGN = "LEFT">
                    <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="OrderConfiguration" value="editOutboundOrderMailMsg"  >
                    &nbsp; Edit Sales Order Email Message</label>
                     <TR>
                    <TH WIDTH="20%" ALIGN = "LEFT">
                    <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="OrderConfiguration" value="editlopt" >
                    &nbsp; Edit Rental Order Printout</label>
                                   
                    <TH WIDTH="20%" ALIGN = "LEFT">
                    <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="OrderConfiguration" value="edittopt" >
                    &nbsp; Edit Consignment Order Printout</label>
                  
                    <TH WIDTH="20%" ALIGN = "LEFT">
                    <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="OrderConfiguration" value="editTransferOrderMailMsg"  >
                    &nbsp; Edit Consignment Order Email Message</label>
                    
                   <TH WIDTH="20%" ALIGN = "LEFT">
                    <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="OrderConfiguration" value="editTaxInvoiceMultiLanguage"  >
                    &nbsp; Edit Invoice Printout</label>
                   
                   <TR>
                    <TH WIDTH="20%" ALIGN = "LEFT">
                    <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="OrderConfiguration" value="editRcptRecvHdr" >
                    &nbsp; Edit Goods Receipt Printout</label>
                                   
                    <TH WIDTH="20%" ALIGN = "LEFT">
                    <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="OrderConfiguration" value="editRcptIssueHdr" >
                    &nbsp; Edit Goods Issue Printout</label>
                  
                    <TH WIDTH="20%" ALIGN = "LEFT">
                    <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="OrderConfiguration" value="editMoveHdr"  >
                    &nbsp; Edit Stock Move Printout</label>
                </TABLE>
    </div>
</div>


 
 
   <!-- <div class="row">
  		<div class="col-12 col-sm-12"><INPUT Type=Checkbox class="form-check-input"  style="border:0;" name="EmailConfiguration"  onclick="return checkAllEmailConfiguration(this.checked);">
                     <strong>   &nbsp; Select/Unselect Approval/Email Configuration </strong> </div>
  </div>
 <div class="panel panel-default">
    <div class="panel-heading" style="background: #eaeafa"><strong>Approval/Email Configuration</strong></div>
    <div class="panel-body"> 
    	<TABLE>
	             <TR>
	             <TH WIDTH="20%" ALIGN = "LEFT">
                      <INPUT Type=Checkbox  style="border:0;" name="EmailConfiguration" value="editemailconfig" >
                      &nbsp; Order Management Approval & Email Configuration 
	    </TABLE>
    </div>
</div> -->         
   <div class="row">
  		<div class="col-12 col-sm-12"><INPUT Type=Checkbox class="form-check-input"  style="border:0;" name="OrderManagement"  onclick="return checkAllOrderManagement(this.checked);">
                   &nbsp; Select/Unselect </div>
  </div>

<div class="panel panel-default">
    <div class="panel-heading" style="background: #eaeafa"><strong>Order Management - Create Masters</strong></div>
    <div class="panel-body"> 
    	<TABLE>
    	   	
                    <TR>
                   
                    <TH WIDTH="20%" ALIGN = "LEFT">
                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="OrderManagement" value="popproduct" >
                  &nbsp; Create Product Details &#45; Purchase,Sales Estimate, Sales, Direct Tax Invoice, Rental And Consignment Order</label>
                   
                    <TH WIDTH="20%" ALIGN = "LEFT">
                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="OrderManagement" value="popsupplier" >
                  &nbsp; Create Supplier Details &#45; Purchase Order</label>
                  
                   <TH WIDTH="20%" ALIGN = "LEFT">
                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="OrderManagement" value="suppliertypepopup">
                  &nbsp; Create Supplier Type - Purchase Order</label>
                  
                  <TH WIDTH="20%" ALIGN = "LEFT">
                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="OrderManagement" value="popcustomer" >
                  &nbsp; Create Customer Details &#45; Sales Estimate, Sales, Direct Tax Invoice, Rental And Consignment Order</label>
                
                 <TR> 
                 <TH WIDTH="20%" ALIGN = "LEFT">
                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="OrderManagement" value="customertypepopup">
                  &nbsp;Create Customer Type - Sales Estimate, Sales Order and Direct Tax Invoice.</label>
                 
				  <TH WIDTH="20%" ALIGN = "LEFT">
                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="OrderManagement" value="ordertypepopup">
                  &nbsp;Create Order Type- Purchase,Sales Estimate, Sales, Direct Tax Invoice,Rental And Consignment Order.</label>
				                    
                    <TH WIDTH="20%" ALIGN = "LEFT">
                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="OrderManagement" value="paymenttypepopup">
                  &nbsp;Payment Type- Purchase,Sales Estimate, Sales, Direct Tax Invoice,Rental And Consignmentr Order</label>
                    
             </TABLE>
    </div>
</div> 
  <div class="row">
  		<div class="col-12 col-sm-12"><INPUT Type=Checkbox class="form-check-input"  style="border:0;" name="OrderManagement1"  onclick="return checkAllOrderManagement1(this.checked);">
                       &nbsp; Select/Unselect Purchase </div>
  </div>
<div class="panel panel-default">
    <div class="panel-heading" style="background: #eaeafa"><strong>Purchase</strong></div>
    <div class="panel-body"> 
    	<TABLE>
                    <TR>
                                    
                    <TH WIDTH="20%" ALIGN = "LEFT">
                    <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="OrderManagement1" value="ibsummary" >
                    &nbsp; Summary &#45; Purchase Order Details</label>                    
                    
                     <TH WIDTH="20%" ALIGN = "LEFT">
                    <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="OrderManagement1" value="ibsummarycost" >
                    &nbsp; Summary &#45; Purchase Order Details (with cost)</label>
                    
                    <TH WIDTH="20%" ALIGN = "LEFT">
                    <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="OrderManagement1" value="maintinb" >
                    &nbsp; Edit Purchase Order Details</label>
                                
                    <TH WIDTH="20%" ALIGN = "LEFT">
                    <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="OrderManagement1" value="msPurOrd" >
                    &nbsp; Create Purchase Order Details</label>
                    
                   <TR>                    
                  
                   <TH WIDTH="20%" ALIGN = "LEFT">
                    <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="OrderManagement1" value="orderClose" >
                    &nbsp; Close Outstanding Purchase Order</label>
                    
                    <TH WIDTH="20%" ALIGN = "LEFT">
                        <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="OrderManagement1" value="ibrecvbulk" >
                        &nbsp; Purchase Order Receipt</label> 
                                                                                            
                        <TH WIDTH="20%" ALIGN = "LEFT">
                        <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="OrderManagement1" value="inbrectbyrange"   >
                        &nbsp; Purchase Order Receipt (by serial)</label>
                        
                       <TH WIDTH="20%" ALIGN = "LEFT">
                        <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="OrderManagement1" value="inbRecbyprd"   >
                        &nbsp; Purchase Order Receipt (by product ID)</label>
                        
                        <TR>
                        
                        <TH WIDTH="20%" ALIGN = "LEFT">
                        <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="OrderManagement1" value="ibrecvmultiple" >
                        &nbsp; Purchase Order Receipt (multiple)</label>
                        
                        <!--  <TH WIDTH="20%" ALIGN = "LEFT">
                        <INPUT Type=Checkbox  style="border:0;" name="OrderManagement1" value="inbReverse"  >
                        &nbsp; Purchase Order Reversal -->                        
                                    
                        <TH WIDTH="20%" ALIGN = "LEFT">
                        <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="OrderManagement1" value="mulmiscrecipt"   >
                        &nbsp; Goods Receipt</label> 
                        
                         <TH WIDTH="20%" ALIGN = "LEFT">
                        <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="OrderManagement1" value="inbMiscRceiptByRange"   >
                        &nbsp; Goods Receipt (by serial)</label>
                            
                        <TH WIDTH="20%" ALIGN = "LEFT">
                         <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="OrderManagement1" value="editexpiry">
                         &nbsp; Edit Inventory Expire Date</label>
                        </TH>
                        
                        <TR>
                        
                        <TH WIDTH="20%" ALIGN = "LEFT">
	                        	<label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="OrderManagement1" value="ibordersumry" >
	                        	&nbsp;  Purchase Order Summary Details</label>
	                        </TH>                        
                       
	                          <TH WIDTH="20%" ALIGN = "LEFT">
	                        	<label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="OrderManagement1" value="ibordersumrywreccost" >
	                        	&nbsp;  Purchase Order Summary Details(by cost)</label>
	                          </TH> 
                     
	                          <TH WIDTH="20%" ALIGN = "LEFT">
	                        	<label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="OrderManagement1" value="recvdibsummry"   >
	                        	&nbsp;  Purchase Order Summary(by cost)</label>
	                        </TH>	                        
	                         
	                          <TH WIDTH="20%" ALIGN = "LEFT">
	                        	<label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="OrderManagement1"value="printpo"   >
	                        	&nbsp;  Purchase Order Details</label>
	                        </TH>	                        
	                        
	                         <TR>
	                        	
	                        <TH WIDTH="20%" ALIGN = "LEFT">
	                        	<label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="OrderManagement1"value="printpoinv"  >
	                        	&nbsp;  Purchase Order Details (with cost)</label>
	                        </TH>
	                        
	                        
	                        <TH WIDTH="20%" ALIGN = "LEFT">
	                        	<label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="OrderManagement1" value="recvsummry"   >
	                        	&nbsp;  Order Receipt Summary</label>
	                        </TH>
	                        
	                        
	                         <TH WIDTH="20%" ALIGN = "LEFT">
	                        	<label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="OrderManagement1" value="recvsummrywithcost"   >
	                        	&nbsp;  Order Receipt Summary (with cost)</label>
	                        </TH>	                        
	                         
	                          <TH WIDTH="20%" ALIGN = "LEFT">
		                    <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="OrderManagement1" value="expensesSummary">
		                    &nbsp;  Expenses</label>
		                    </TH>
		                    
	                        <TR>
	                        
		                    
		                    <TH WIDTH="20%" ALIGN = "LEFT">
		                    <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="OrderManagement1" value="grnSummary">
		                    &nbsp;  Goods Receipt Summary</label>
		                    </TH>
		                   
		                    
		                    <TH WIDTH="20%" ALIGN = "LEFT">
		                    <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="OrderManagement1" value="billSummary">
		                    &nbsp;  Bill</label>
		                    </TH>		                     
		                          
		                    <TH WIDTH="20%" ALIGN = "LEFT">
		                    <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="OrderManagement1" value="billPaymentSummary">
		                    &nbsp;  Payment</label>
		                    </TH>
		                    
		                    <TH WIDTH="20%" ALIGN = "LEFT">
		                    <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="OrderManagement1" value="pdcpaymentSummary">
		                    &nbsp; PDC Payment</label>
		                    </TH>
		                    
		                    <TR>
		                    
		                    <TH WIDTH="20%" ALIGN = "LEFT">
		                    <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="OrderManagement1" value="POReturnSummary">
		                    &nbsp;Purchase Return</label>
		                    </TH>
		                    
		                    <TH WIDTH="20%" ALIGN = "LEFT">
		                    <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="OrderManagement1" value="supplierCreditNote">
		                    &nbsp;Purchase Credit Notes</label>
		                    </TH>
		                      
             </TABLE>
    </div>
</div>   
  <div class="row">
  		<div class="col-12 col-sm-12"><INPUT Type=Checkbox class="form-check-input"  style="border:0;" name="SalesEstimate"  onclick="return checkAllSalesEstimate(this.checked);">
                        &nbsp; Select/Unselect Sales Estimate </div>
  </div>
<div class="panel panel-default">
    <div class="panel-heading" style="background: #eaeafa"><strong>Sales Estimate</strong></div>
    <div class="panel-body">
    <TABLE>
                    <TR>
                    
                    <TH WIDTH="20%" ALIGN = "LEFT">
                   <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;" name="SalesEstimate" value="estsummarywithoutprice" >
                    &nbsp; Summary &#45; Sales Estimate Order Details</label> 
                    
                    <TH WIDTH="20%" ALIGN = "LEFT">
                    <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SalesEstimate" value="estsummary" >
                    &nbsp; Summary &#45; Sales Estimate Order Details (with price)</label>                      
                                              
                    <TH WIDTH="20%" ALIGN = "LEFT">
                    <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SalesEstimate" value="maintest">
                    &nbsp;  Edit Sales Estimate Order Details</label>
                    
                     <TH WIDTH="20%" ALIGN = "LEFT">
                    <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SalesEstimate" value="convertOB">
                    &nbsp;  Edit Sales Estimate &#45; Convert to Sales(button)</label>
                    
                    <TR>
                                       
                    <TH WIDTH="20%" ALIGN = "LEFT">
                    <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SalesEstimate" value="msestOrd" >
                    &nbsp; Create Sales Estimate Order Details</label>
                        
                     <TH WIDTH="20%" ALIGN = "LEFT">
	                 <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SalesEstimate" value="estordsmrywithoutprice" >
	                 &nbsp;  Sales Estimate Order Summary</label> 
	                 </TH>
	                        
	                 <TH WIDTH="20%" ALIGN = "LEFT">
	                 <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SalesEstimate" value="estordsmrywissprice" >
	                 &nbsp;  Sales Estimate Order Summary Details(by price)</label>
	                 </TH>
					 
					 <TH WIDTH="20%" ALIGN = "LEFT">
	                 <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SalesEstimate" value="summaryalternatbrandproduct"  >
	                 &nbsp; Sales Counter</label>
                     </TH>
                     
                     <TR>                                       
                    	<TH WIDTH="20%" ALIGN = "LEFT">
		                    <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SalesEstimate" value="salescounterconvtoest" >
		                    &nbsp; Sales Counter Convert To Estimate</label>
	                    </TH>
                        
                     	<TH WIDTH="20%" ALIGN = "LEFT">
			                 <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SalesEstimate" value="salescounterconvtoinvc" >
			                 &nbsp; Sales Counter Convert To Invoice</label>
	                 	</TH>
	                 </TR>
                       
                   </TABLE>
    </div>
</div>

<div class="row">
  		<div class="col-12 col-sm-12"><INPUT Type=Checkbox class="form-check-input"  style="border:0;" name="SalesTransaction"  onclick="return checkAllSalesTransaction(this.checked);">
                       &nbsp; Select/Unselect Sales </div>
  </div>

<div class="panel panel-default">
    <div class="panel-heading" style="background: #eaeafa"><strong>Sales</strong></div>
    <div class="panel-body">
    <TABLE>
    				<TR>
                     
                    <TH WIDTH="20%" ALIGN = "LEFT">
                    <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SalesTransaction" value="printOB">
                    &nbsp; Summary &#45; Sales Order </label>                   
                    
                   <TH WIDTH="20%" ALIGN = "LEFT">
                    <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SalesTransaction" value="PrintOBInvoice">
                    &nbsp; Summary &#45; Sales Order (with price)</label>
                 
                    <TH WIDTH="20%" ALIGN = "LEFT">
                    <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SalesTransaction" value="obsummary" >
                    &nbsp; Summary &#45; Sales Order Details</label>
                                      
                   <TH WIDTH="20%" ALIGN = "LEFT">
                    <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SalesTransaction" value="obsummaryprice">
                    &nbsp; Summary &#45; Sales Order Details (with price)</label>
                    
                    <TR>
                                                      
                    <TH WIDTH="20%" ALIGN = "LEFT">
                    <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SalesTransaction" value="maintoub">
                    &nbsp;  Edit Sales Order Details</label>                       
                               
                    <TH WIDTH="20%" ALIGN = "LEFT">
                    <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SalesTransaction" value="msSalesOrd" >
                    &nbsp; Create Sales Order Details</label>
                    
                    <TH WIDTH="20%" ALIGN = "LEFT">
                    <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SalesTransaction" value="orderCloseSales" >
                    &nbsp; Close Outstanding Sales Order</label>                   
                    
	                       <TH WIDTH="20%" ALIGN = "LEFT">
	                        <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SalesTransaction" value="outbndmulPicking"   >
	                        &nbsp;  Sales Order Pick</label>
	                        
	                        <TR> 
	                        
	                       <TH WIDTH="20%" ALIGN = "LEFT">
	                        <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SalesTransaction" value="obpickingbyRange"   >
	                        &nbsp;  Sales Order Pick (by serial)</label>
	                        
	                         <TH WIDTH="20%" ALIGN = "LEFT">
	                        <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SalesTransaction" value="outbndIssuing" >
	                        &nbsp; Sales Order Issue</label>
	                        
	                         <TH WIDTH="20%" ALIGN = "LEFT">
                            <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SalesTransaction" value="obpickissuebulk" >
                            &nbsp;  Sales Order Pick & Issue</label>   	  
                               
                            <TH WIDTH="20%" ALIGN = "LEFT">
	                        <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SalesTransaction" value="obpickissuebyprd" >
	                        &nbsp;  Sales Order Pick/Issue (by Product)</label>
	                        
	                        <TR>                          
                                                                            
	                       <TH WIDTH="20%" ALIGN = "LEFT">
	                       <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;" name="SalesTransaction" value="obpickissuemultiple" >
	                        &nbsp;  Sales Order Pick & Issue (multiple)</label>
	                       	                                             
		                    <TH WIDTH="20%" ALIGN = "LEFT">
		                    <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SalesTransaction" value="outbndPkrev"   >
		                    &nbsp; Sales Order Pick Return	</label>	                    
		                    
	                     
	                         <!-- <TH WIDTH="20%" ALIGN = "LEFT">
		                     <INPUT Type=Checkbox  style="border:0;" name="SalesTransaction" value="obissuereversal" >
		                     &nbsp;  Sales Order Pick & Issue Reversal -->
                          
                            <TH WIDTH="20%" ALIGN = "LEFT">
		                    <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SalesTransaction" value="mulmiscissue"   >
		                    &nbsp; Goods Issue</label>
				  		
		                   <TH WIDTH="20%" ALIGN = "LEFT">
		                   <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SalesTransaction" value="ubmiscissueByRange"  >
		                   &nbsp; Goods Issue (by serial) </label>                         

							<TR>
		                   		                   
		                   <TH WIDTH="20%" ALIGN = "LEFT">
                    	   <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SalesTransaction" value="creatednpl" >
                    	   &nbsp; Create Sales Packing List/Deliver Note (PL/DN)</label>
		                   
		                   
		                   <TH WIDTH="20%" ALIGN = "LEFT">
                    	   <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SalesTransaction" value="packingSummary" >
                    	   &nbsp; Summary/Edit Packing List and Deliver Note </label> 
                    	   
		                   <TH WIDTH="20%" ALIGN = "LEFT">
                        	<label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SalesTransaction" value="obordersumry" >
                        	&nbsp;  Sales Order Summary Details</label>
                        	</TH>                        	
                           
                        	<TH WIDTH="20%" ALIGN = "LEFT">
                        	<label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SalesTransaction"value="obsalessmry"  >
                        	&nbsp;   Sales Order Sales Summary</label>
                            </TH> 	
                    	   
                    	   <TR>
                            
                        	<TH WIDTH="20%" ALIGN = "LEFT">
                        	<label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SalesTransaction" value="containerSummary" >
                        	&nbsp; Sales Order Summary(by container)</label>
                            </TH> 
                            
                            <TH WIDTH="20%" ALIGN = "LEFT">
                        	<label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SalesTransaction" value="invWithIssueReturn" >
                        	&nbsp; Sales Order Summary(by customer) </label>
                            </TH>
                           
                            <TH WIDTH="20%" ALIGN = "LEFT">
                        	<label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SalesTransaction" value="obordersumrywissprice" >
                        	&nbsp;  Sales Order Summary Details(by price)</label>
                        	</TH>                        	 
                        	
                        	<TH WIDTH="20%" ALIGN = "LEFT">
                        	<label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SalesTransaction" value="obordersumrywavgcost" >
                        	&nbsp;  Sales Order Summary Details (by average cost)</label>
                       		</TH>
                           
                           <TR> 
                       		
                        	<TH WIDTH="20%" ALIGN = "LEFT">
                        	<label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SalesTransaction" value="issuedobsummry"  >
                        	&nbsp; Sales Order Summary(by price)</label>
                           </TH>
	                        
	                        <TH WIDTH="20%" ALIGN = "LEFT">
	                        <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SalesTransaction" value="printdo"  >
	                        &nbsp; Sales Order Details</label>
	                        </TH>
	                        
	                        <TH WIDTH="20%" ALIGN = "LEFT">
	                       <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;" name="SalesTransaction" value="printinvoice"  >
	                        &nbsp; Sales Order Details (with price)</label>
	                        </TH>                           	 
	                        
	                        <TH WIDTH="20%" ALIGN = "LEFT">
	                        <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SalesTransaction" value="issuesummry" >      
	                        &nbsp;  Order Issue Summary</label>
	                        </TH>	                        
	                        
	                        <TR>	                           
	                        
	                        <TH WIDTH="20%" ALIGN = "LEFT">
	                        <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SalesTransaction" value="issuesummrywithprice"  >      
	                        &nbsp;  Order Issue Summary (with price)</label>
	                        </TH>	                           
	                        
	                        <TH WIDTH="20%" ALIGN = "LEFT">
	                        <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SalesTransaction" value="ginotoinvoiceSummary"  >      
	                        &nbsp;  Goods Issued</label>
	                        </TH>	                        
	                        
	                        <TH WIDTH="20%" ALIGN = "LEFT">
	                        	<label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SalesTransaction" value="invoiceSummary">     
	                        	&nbsp;  Invoice</label>
	                        </TH>
	                        
	                        <TH WIDTH="20%" ALIGN = "LEFT">
	                        	<label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SalesTransaction" value="invoiceByOpen">     
	                        	&nbsp;  Invoice By Open</label>
	                        </TH>
		                    
	                        <TR>
	                        
	                        <TH WIDTH="20%" ALIGN = "LEFT">
	                        	<label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SalesTransaction" value="invoiceByDraft">     
	                        	&nbsp; Invoice By Draft</label>
	                        </TH>
	                        
	                        <TH WIDTH="20%" ALIGN = "LEFT">
	                        	<label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SalesTransaction" value="invoicePaymentSummary">     
	                        	&nbsp;  Payment Received</label>
	                        </TH>
	                        
	                        <TH WIDTH="20%" ALIGN = "LEFT">
	                        	<label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SalesTransaction" value="pdcpaymentReceiveSummary">     
	                        	&nbsp; PDC Payment Received</label>
	                        </TH>
	                        
	                        <TH WIDTH="20%" ALIGN = "LEFT">
		                    <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SalesTransaction" value="SOReturnSummary">
		                    &nbsp; Sales Return</label>
		                    </TH>
	                        
	                        <TR>
	                        
		                    <TH WIDTH="20%" ALIGN = "LEFT">
	                        	<label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SalesTransaction" value="customerCreditNote">     
	                        	&nbsp;  Credit Notes</label>
	                        </TH>	                           
	                        	         	   		                 
	              	    
	             </TR>         	
              </TABLE>
    </div>
</div>

<div class="row">
  		<div class="col-12 col-sm-12"><INPUT Type=Checkbox class="form-check-input"  style="border:0;" name="AccountingAdmin"  onclick="return checkAllAccountingAdmin(this.checked);">
                        &nbsp; Select/Unselect Accounting </div>
  </div>
 <div class="panel panel-default">
    <div class="panel-heading" style="background: #eaeafa"><strong>Accounting</strong></div>
    <div class="panel-body"> 
    	<TABLE style="width: 100%;">
	             <TR>	             
                 
                 <TH WIDTH="20%" ALIGN = "LEFT">
                      <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="AccountingAdmin" value="BankSummary" >
                      &nbsp; Banking</label>
                 
                 <TH WIDTH="20%" ALIGN = "LEFT">
                      <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="AccountingAdmin" value="chartOfAccounts" >
                      &nbsp; Chart of Accounts</label>
                 
                 <TH WIDTH="20%" ALIGN = "LEFT">
                      <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="AccountingAdmin" value="journalsummary" >
                      &nbsp; Journal Entry</label>
                 
                 <TH WIDTH="20%" ALIGN = "LEFT">
                      <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="AccountingAdmin" value="contrasummary" >
                      &nbsp; Contra Entry</label>
                      
                 <TR>
                                
                 <TH WIDTH="20%" ALIGN = "LEFT">
                      <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="AccountingAdmin" value="taxreturnsettings" >
                      &nbsp; Tax Settings</label>
                      
                 <TH WIDTH="20%" ALIGN = "LEFT">
	                        <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="AccountingAdmin" value="taxreturnpayments_acc"  >      
	                        &nbsp;  Tax Payments</label>
	                        </TH>
                 
                 <TH WIDTH="20%" ALIGN = "LEFT">
	                        <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="AccountingAdmin" value="taxAdjustmentSummary_acc"  >      
	                        &nbsp;  Tax Adjustments</label>
	                        </TH>     
           </TR>                 
	    </TABLE>
    </div>
</div>



<!-- 

<div class="row">
  		<div class="col-12 col-sm-12"><INPUT Type=Checkbox class="form-check-input"  style="border:0;" name="RentalTransaction"  onclick="return checkAllRentalTransaction(this.checked);">
                       &nbsp; Select/Unselect Rental / Consignment </div>
  </div>
  <div class="panel panel-default">
    <div class="panel-heading" style="background: #eaeafa"><strong>Rentall / Consignment</strong></div>
    <div class="panel-body"> 
    	<TABLE>
                    <TR>
					 
                    <TH WIDTH="20%" ALIGN = "LEFT">
                    <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="RentalTransaction" value="lnordsummry" >
                    &nbsp; Summary &#45; Rental Order Details</label>
                    
                    <TH WIDTH="20%" ALIGN = "LEFT">
                    <INPUT Type=Checkbox  style="border:0;" name="RentalTransaction" value="loanOrderSummaryWithPrice" >
                    &nbsp; Summary &#45; Rental Order With Price Details
                    
                    <TH WIDTH="20%" ALIGN = "LEFT"> 
                   <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;" name="RentalTransaction" value="maintlnord" >
                    &nbsp;  Edit Rental Order Details</label>                      
                              
                    <TH WIDTH="20%" ALIGN = "LEFT">
                    <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="RentalTransaction" value="cratlnord" >
                    &nbsp; Create Rental Order Details</label>
                    					
					<TH WIDTH="20%" ALIGN = "LEFT">
                    <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="RentalTransaction" value="orderCloseRental" >
                    &nbsp; Close Outstanding Rental Orders</label>
                    
                    <TR>					
                   
                    <TH WIDTH="20%" ALIGN = "LEFT">
                    <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="RentalTransaction" value="trordsummry" >
                    &nbsp; Summary &#45; Consignment Order Details</label>                    
					
                    <TH WIDTH="20%" ALIGN = "LEFT">  
                    <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="RentalTransaction" value="mainttrord" >
                    &nbsp; Edit Consignment Order Details</label>
                     
                    <TH WIDTH="20%" ALIGN = "LEFT">
                    <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="RentalTransaction" value="crattrord" >
                    &nbsp; Create Consignment Order Details</label>
					
					<TH WIDTH="20%" ALIGN = "LEFT">
                    <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="RentalTransaction" value="orderCloseTransfer" >
                    &nbsp; Close Outstanding Consignment Orders</label>
					
					<TR>					
					
					<TH WIDTH="20%" ALIGN = "LEFT">
                    <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="RentalTransaction" value="lnordReceiving"   >
                    &nbsp;Rental Order Receipt</label>                  
                   
                    <TH WIDTH="20%" ALIGN = "LEFT">
		            <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="RentalTransaction" value="lnordPicking" >
		            &nbsp;  Rental Order Pick & Issue</label>   
                                   
		            <TH WIDTH="20%" ALIGN = "LEFT">
		            <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="RentalTransaction" value="bulkPickReceiveTO" >
		            &nbsp;  Consignment Order Pick & Issue</label>
		                              		                  
	                <TH WIDTH="20%" ALIGN = "LEFT">
	                <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="RentalTransaction" value="toreversal" >
	                &nbsp;  Consignment Order Reversal</label>
		            
		            <TR>					
					
							  <TH WIDTH="20%" ALIGN = "LEFT">
	                          <INPUT Type=Checkbox  style="border:0;" name="RentalTransaction" value="LoanOrderSummaryByPrice" >
	                          &nbsp;  Rental Order Details Summary With Price
	                          </TH> 
							  
							  <TH WIDTH="20%" ALIGN = "LEFT">
	                          <INPUT Type=Checkbox  style="border:0;" name="RentalTransaction" value="RentalOrderSummary" >
	                          &nbsp;  Rental Order Details Summary 
	                          </TH>
					
	                <TH WIDTH="20%" ALIGN = "LEFT">
	               <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;" name="RentalTransaction" value="printloanorder"  >
	                &nbsp;  Rental Order Details</label>
	                </TH>
	                
	                		  <TH WIDTH="20%" ALIGN = "LEFT">
	                          <INPUT Type=Checkbox  style="border:0;" name="RentalTransaction" value="printLoanOrderInvoice" >
	                          &nbsp;  Rental Order Details Summary By Price
	                          </TH>
                                                
                    <TH WIDTH="20%" ALIGN = "LEFT">
                    <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="RentalTransaction" value="printto"  >
                    &nbsp;   Consignment Order Details</label>
                    </TH>
        </TABLE>
    </div>
</div> 
  
  <div class="row">
  		<div class="col-12 col-sm-12"><INPUT Type=Checkbox class="form-check-input"  style="border:0;" name="InHouse"  onclick="return checkAllInHouse(this.checked);">
                    &nbsp; Select/Unselect In House</div>
  </div>
<div class="panel panel-default">
    <div class="panel-heading" style="background: #eaeafa"><strong>In House</strong></div>
    <div class="panel-body">
    <TABLE>
                    <TR>
                    
                       <TH WIDTH="20%" ALIGN = "LEFT">
                       <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="InHouse" value="mulloctransfer" >  
                       &nbsp; Stock Move</label>
                         
                            <TH WIDTH="20%" ALIGN = "LEFT">
                        	<label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="InHouse" value="rptPSVsINV"   >
                        	&nbsp; Stock Take Summary</label> </TH>
                        	
                       		<TH WIDTH="20%" ALIGN = "LEFT">
                            <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="InHouse" value="stocktakeWithPrice"  >
                            &nbsp; Stock Take Summary (with price)</label>
                        	</TH>
                      
                      	    <TH WIDTH="20%" ALIGN = "LEFT">
                        	<label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="InHouse" value="stkreset"   >
                        	&nbsp; Stock Take Reset </label></TH>
                        	
                        	<TR>
                 
                        <TH WIDTH="20%" ALIGN = "LEFT">
                         <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="InHouse" value="kitdekit">
                         &nbsp; Kitting De-Kitting</label>
                        </TH>
                        
                        <TH WIDTH="20%" ALIGN = "LEFT">
                         <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="InHouse" value="kitdekitwithbom">
                         &nbsp; Kitting De-Kitting(with ref BOM)</label>                         
                        </TH>
                        
                        <TH WIDTH="20%" ALIGN = "LEFT">
                         <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="InHouse" value="bulkkitdekit">
                         &nbsp; Kitting De-Kitting(bulk with ref BOM)</label>
                        </TH>                         
                         
                         <TH WIDTH="20%" ALIGN = "LEFT">
                        	<label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="InHouse" value="kittingSumm"   >
                        	&nbsp; Kitting/De-Kitting Summary</label>
                        	</TH>                          
                        	 
                     </TABLE>
	</div>
</div>-->

  

 <div class="row">
  		<div class="col-12 col-sm-12"><INPUT Type=Checkbox class="form-check-input"  style="border:0;" name="Reports"  onclick="return checkAllReports(this.checked);">
                        &nbsp; Select/Unselect Inventory Reports </div>
  </div>

<div class="panel panel-default">
    <div class="panel-heading" style="background: #eaeafa"><strong>Inventory Reports</strong></div>
    <div class="panel-body">
		<TABLE >
                    <TR>
                    
                         <TH WIDTH="20%" ALIGN = "LEFT">
	                        	<label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="Reports" value="viewinventoryprodmultiuom"  >
	                        	&nbsp; Inventory Summary With Total Quantity</label>
                         </TH>
							  						                 
                         <TH WIDTH="20%" ALIGN = "LEFT">
	                        	<label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="Reports" value="viewinventorybatchmultiuom"  >
	                        	&nbsp; Inventory Summary With Batch/Sno</label> 
                         </TH>  
                    
                        <TH WIDTH="20%" ALIGN = "LEFT">
                        	<label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="Reports" value="viewInventoryByProd" >
                        	&nbsp; Inventory Summary With Total Quantity (with pcs)</label>
                        </TH>             

                        <TH WIDTH="20%" ALIGN = "LEFT">
                        	<label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="Reports" value="msInvnopriceqty"  >
                        	&nbsp; Inventory Summary With Batch/Sno (with pcs)</label>
                        </TH>
                        <TR>       
                         
                       <TH WIDTH="20%" ALIGN = "LEFT">
                            <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="Reports" value="msInvList"  >
                            &nbsp; Inventory Summary (with min/max/zero qty)</label> 
                       </TH>
                    
                         
                    	<TH WIDTH="20%" ALIGN = "LEFT">
                        	<label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="Reports" value="msInvListwithExpireD"  >
                        	&nbsp; Inventory Summary (with expiry date)</label>
                        </TH>
                                                                  
                        
	                    <TH WIDTH="20%" ALIGN = "LEFT">
	                           <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;" name="Reports" value="msInvListwithcost" >
	                     &nbsp; Inventory Summary (with average cost)</label>
	                     </TH>
	                                             
                       <TH WIDTH="20%" ALIGN = "LEFT">
                            <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="Reports" value="inventoryAgingSummary"  >
                            &nbsp; Inventory Aging Summary </label>
                       </TH>
                                           
                   </TABLE>
    </div>
</div>

 <div class="row">
  		<div class="col-12 col-sm-12"><INPUT Type=Checkbox class="form-check-input"  style="border:0;" name="AccountingReports"  onclick="return checkAllAccountingReports(this.checked);">
                        &nbsp; Select/Unselect Accounting Reports </div>
  </div>

<div class="panel panel-default">
    <div class="panel-heading" style="background: #eaeafa"><strong>Accounting Reports</strong></div>
    <div class="panel-body">
		<TABLE >
                    <TR>
                    
                         <TH WIDTH="20%" ALIGN = "LEFT">
	                        	<label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="AccountingReports" value="balancesheets"  >
	                        	&nbsp; Balance Sheet</label>
                         </TH>
							  						                 
                         <TH WIDTH="20%" ALIGN = "LEFT">
	                        	<label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="AccountingReports" value="profitloss"  >
	                        	&nbsp; Profit and Loss</label> 
                         </TH>
                         
                         <TH WIDTH="20%" ALIGN = "LEFT">
	                        	<label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="AccountingReports" value="expenseDetails"  >
	                        	&nbsp; Expense Details</label>
                         </TH>
							  						                 
                         <TH WIDTH="20%" ALIGN = "LEFT">
	                        	<label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="AccountingReports" value="expenseByCategory"  >
	                        	&nbsp; Expense by Category </label>
                         </TH>
                         
                         <TR>  
                    
                        <TH WIDTH="20%" ALIGN = "LEFT">
                        <label class="checkbox-inline">	<INPUT Type=Checkbox  style="border:0;" name="AccountingReports" value="expenseBySupplier" >
                        	&nbsp; Expense by Supplier</label>
                        </TH>             

                        <TH WIDTH="20%" ALIGN = "LEFT">
                        	<label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="AccountingReports" value="expenseByCustomer"  >
                        	&nbsp; Expense by Customer</label>
                        </TH>      
                         
                       <TH WIDTH="20%" ALIGN = "LEFT">
                           <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;" name="AccountingReports" value="billsdetails"  >
                            &nbsp; Bill Details </label>
                       </TH>
                         
                    	<TH WIDTH="20%" ALIGN = "LEFT">
                        	<label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="AccountingReports" value="paymentsmade"  >
                        	&nbsp; Payment Made</label>
                        </TH>
                                                                  
                        <TR>
                        
	                    <TH WIDTH="20%" ALIGN = "LEFT">
	                        <label class="checkbox-inline">    <INPUT Type=Checkbox  style="border:0;" name="AccountingReports" value="vendorbalances" >
	                     &nbsp; Supplier Balances</label>
	                     </TH>
	                                      

                       	<TH WIDTH="20%" ALIGN = "LEFT">
	                        	<label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="AccountingReports" value="vendorcreditdetails"  >
	                        	&nbsp; Supplier Credit Notes Details</label>
                         </TH>       
                         
                       <TH WIDTH="20%" ALIGN = "LEFT">
                            <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="AccountingReports" value="billsAgingSummary"  >
                            &nbsp; Supplier Aging Summary </label>
                       </TH>
                       
                                           	<TH WIDTH="20%" ALIGN = "LEFT">
                        	<label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="AccountingReports" value="invoicedetails"  >
                        	&nbsp; Invoice Details</label>
                        </TH>
	                     
	                     <TR>                 

                       	<TH WIDTH="20%" ALIGN = "LEFT">
	                        	<label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="AccountingReports" value="paymentsreceived"  >
	                        	&nbsp; Payment Received</label>
                         </TH>      
                         
                       <TH WIDTH="20%" ALIGN = "LEFT">
                            <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="AccountingReports" value="customerbalances"  >
                            &nbsp; Customer Balances </label>
                       </TH> 
                       
                       <TH WIDTH="20%" ALIGN = "LEFT">
                           <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;" name="AccountingReports" value="customercreditdetails"  >
                            &nbsp; Customer Credit Notes Details</label> 
                       </TH>
                       
                       <TH WIDTH="20%" ALIGN = "LEFT">
	                        <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="AccountingReports" value="invoiceAgingSummary"  >      
	                        &nbsp;  Customer Aging Summary</label>
	                        </TH>
	                        
	                        <TR>
	                        
	                        <TH WIDTH="20%" ALIGN = "LEFT">
	                        <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="AccountingReports" value="taxreturnsummary"  >      
	                        &nbsp;  Tax Return Summary</label>
	                        </TH>
	                        
	                        <TH WIDTH="20%" ALIGN = "LEFT">
	                        <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="AccountingReports" value="taxAdjustmentSummary"  >      
	                        &nbsp;  Tax Adjustments Summary</label>
	                        </TH>
	                        
	                        <TH WIDTH="20%" ALIGN = "LEFT">
	                        <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="AccountingReports" value="taxreturnpayments"  >      
	                        &nbsp;  Tax Payments Summary</label>
	                        </TH>
	                        
	                        <TH WIDTH="20%" ALIGN = "LEFT">
	                        <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="AccountingReports" value="generalledger"  >      
	                        &nbsp;  Detailed General Ledger</label>
	                        </TH>
	                        
	                        <TR>
	                        
	                        <TH WIDTH="20%" ALIGN = "LEFT">
	                        <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="AccountingReports" value="journalreport"  >      
	                        &nbsp;  Journal</label>
	                        </TH>
	                        
	                        <TH WIDTH="20%" ALIGN = "LEFT">
	                        <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="AccountingReports" value="trialbalance"  >      
	                        &nbsp;  Trail Balance</label>
	                        </TH>
                                           
                   </TABLE>
    </div>
</div>

<div class="row">
  		<div class="col-12 col-sm-12"><INPUT Type=Checkbox class="form-check-input"  style="border:0;" name="ActivityLogs"  onclick="return checkAllActivityLogs(this.checked);">
                        &nbsp; Select/Unselect Activity Logs </div>
  </div>

<div class="panel panel-default">
    <div class="panel-heading" style="background: #eaeafa"><strong>Activity Logs</strong></div>
    <div class="panel-body">
		<TABLE >
                    <TR>
                    
                    <TH WIDTH="20%" ALIGN = "LEFT">
	                        	<label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="ActivityLogs" value="msPOList"  >
	                        	&nbsp; Activity Logs</label>
                         </TH>
                    
                    </TABLE>
    </div>
</div>

 <!-- <div class="row">
  		<div class="col-12 col-sm-12"><INPUT Type=Checkbox class="form-check-input"  style="border:0;" name="AccountingHome"  onclick="return checkAllAccountingHome(this.checked);">
                     <strong>   &nbsp; Select/Unselect Accounting - Home Page</strong> </div>
  </div>
  
<div class="panel panel-default">
    <div class="panel-heading" style="background: #eaeafa"><strong>Accounting - Home Page</strong></div>
    <div class="panel-body">
		<table>
			<tr>
				<th width="20%" align="left"><input type="checkbox" style="border: 0;" name="AccountingHome" value="profitlosswidget">&nbsp;Profit Loss Widget</th>
				<th width="20%" align="left"><input type="checkbox" style="border: 0;" name="AccountingHome" value="expenseswidget">&nbsp;Expense Widget</th>
				<th width="20%" align="left"><input type="checkbox" style="border: 0;" name="AccountingHome" value="incomewidget">&nbsp;Income Widget</th>
				<th width="20%" align="left"><input type="checkbox" style="border: 0;" name="AccountingHome" value="bankaccountswidget">&nbsp;Bank Accounts Widget</th>
			</tr>
			<tr>
				<th width="20%" align="left"><input type="checkbox" style="border: 0;" name="AccountingHome" value="purchasewidget">&nbsp;Purchase Widget</th>
				<th width="20%" align="left"><input type="checkbox" style="border: 0;" name="AccountingHome" value="saleswidget">&nbsp;Sales Widget</th>
				<th width="20%" align="left"><input type="checkbox" style="border: 0;" name="AccountingHome" value="movementhistorywidget">&nbsp;Movement History Widget</th>
				<th></th>
			</tr>
	</table>
</div>
                          </div>
                      
                       <div class="row">
  		<div class="col-12 col-sm-12"><INPUT Type=Checkbox class="form-check-input"  style="border:0;" name="AccountingMaster"  onclick="return checkAllAccountingMaster(this.checked);">
                     <strong>   &nbsp; Select/Unselect Accounting - Masters</strong> </div>
  </div>
                          
<div class="panel panel-default">
    <div class="panel-heading" style="background: #eaeafa"><strong>Accounting - Masters</strong></div>
    <div class="panel-body">
    	<table>					
			<tr>
				<th width="20%" align="left"><input type="checkbox" style="border: 0;" name="AccountingMaster" value="createbankbranch">&nbsp;Create Bank Branch</th>
				<th width="20%" align="left"><input type="checkbox" style="border: 0;" name="AccountingMaster" value="listbankbranches">&nbsp;List Bank Branches</th>
				<th width="20%" align="left"><input type="checkbox" style="border: 0;" name="AccountingMaster" value="importbankbranchss">&nbsp;Import Bank Branches</th>
				<th></th>
			</tr>
			<tr>
				<th width="20%" align="left"><input type="checkbox" style="border: 0;" name="AccountingMaster" value="createcustomer">&nbsp;Create Customer</th>
				<th width="20%" align="left"><input type="checkbox" style="border: 0;" name="AccountingMaster" value="listcustomers">&nbsp;List Customers</th>
				<th width="20%" align="left"><input type="checkbox" style="border: 0;" name="AccountingMaster" value="importcustomers">&nbsp;Import Customers</th>
				<th></th>
			</tr>
			<tr>
				<th width="20%" align="left"><input type="checkbox" style="border: 0;" name="AccountingMaster" value="createsupplier">&nbsp;Create Supplier</th>
				<th width="20%" align="left"><input type="checkbox" style="border: 0;" name="AccountingMaster" value="listsuppliers">&nbsp;List Suppliers</th>
				<th width="20%" align="left"><input type="checkbox" style="border: 0;" name="AccountingMaster" value="importsuppliers">&nbsp;Import Suppliers</th>
				<th></th>
			</tr>
			<tr>
				<th width="20%" align="left"><input type="checkbox" style="border: 0;" name="AccountingMaster" value="createaccount">&nbsp;Create Account</th>
				<th width="20%" align="left"><input type="checkbox" style="border: 0;" name="AccountingMaster" value="listaccounts">&nbsp;List Accounts</th>
				<th width="20%" align="left"><input type="checkbox" style="border: 0;" name="AccountingMaster" value="importaccounts">&nbsp;Import Accounts</th>
				<th></th>
			</tr>
			<tr>
				<th width="20%" align="left"><input type="checkbox" style="border: 0;" name="AccountingMaster" value="createdaybook">&nbsp;Create Daybook</th>
				<th width="20%" align="left"><input type="checkbox" style="border: 0;" name="AccountingMaster" value="listdaybooks">&nbsp;List Daybooks</th>
				<th></th><th></th>
			</tr>
			<tr>
				<th width="20%" align="left"><input type="checkbox" style="border: 0;" name="AccountingMaster" value="createcurrency">&nbsp;Create Currency</th>
				<th width="20%" align="left"><input type="checkbox" style="border: 0;" name="AccountingMaster" value="listcurrencies">&nbsp;List Currencies</th>
				<th></th><th></th>
			</tr>
	</table>
</div>
                          </div>
                          
                                              <div class="row">
  		<div class="col-12 col-sm-12"><INPUT Type=Checkbox class="form-check-input"  style="border:0;" name="AccountingTransactions"  onclick="return checkAllAccountingTransactions(this.checked);">
                     <strong>   &nbsp; Select/Unselect Accounting - Transactions</strong> </div>
  </div>
                          
<div class="panel panel-default">
    <div class="panel-heading" style="background: #eaeafa"><strong>Accounting - Transactions</strong></div>
    <div class="panel-body">
    	<table>					
			<tr>
				<th width="20%" align="left"><input type="checkbox" style="border: 0;" name="AccountingTransactions" value="newfinancialtransaction">&nbsp;New Financial Transaction</th>
				<th width="20%" align="left"><input type="checkbox" style="border: 0;" name="AccountingTransactions" value="listfinancialtransactions">&nbsp;List Financial Transactions</th>
				<th width="20%" align="left"><input type="checkbox" style="border: 0;" name="AccountingTransactions" value="reconcilesalespurchase">&nbsp;Reconcile Sales &amp; Purchase</th>
				<th></th>
			</tr>
	</table>
</div>
                          </div>
                          
        <div class="row">
  		<div class="col-12 col-sm-12"><INPUT Type=Checkbox class="form-check-input"  style="border:0;" name="AccountingReports"  onclick="return checkAllAccountingReports(this.checked);">
                     <strong>   &nbsp; Select/Unselect Accounting - Reports</strong> </div>
  </div>
                          
<div class="panel panel-default">
    <div class="panel-heading" style="background: #eaeafa"><strong>Accounting - Reports</strong></div>
    <div class="panel-body">
    	<table>					
			<tr>
				<th width="20%" align="left"><input type="checkbox" style="border: 0;" name="AccountingReports" value="movementhistoryreport">&nbsp;Movement History Report</th>
				<th width="20%" align="left"><input type="checkbox" style="border: 0;" name="AccountingReports" value="financialtransactionsreport">&nbsp;Financial Transactions Report</th>
				<th width="20%" align="left"><input type="checkbox" style="border: 0;" name="AccountingReports" value="profitlossstatement">&nbsp;Profit Loss Statement</th>
				<th width="20%" align="left"><input type="checkbox" style="border: 0;" name="AccountingReports" value="trailbalance">&nbsp;Trail Balance</th>
			</tr>
			<tr>
				<th width="20%" align="left"><input type="checkbox" style="border: 0;" name="AccountingReports" value="balancesheet">&nbsp;Balance sheet</th>
				<th width="20%" align="left"><input type="checkbox" style="border: 0;" name="AccountingReports" value="customerwiseageing">&nbsp;Customer-wise Ageing</th>
				<th width="20%" align="left"><input type="checkbox" style="border: 0;" name="AccountingReports" value="supplierwiseageing">&nbsp;Supplier-wise Ageing</th>
				<th width="20%" align="left"><input type="checkbox" style="border: 0;" name="AccountingReports" value="cashflowprojection">&nbsp;Cash Flow Projection</th>
			</tr>
        </table>
    </div>
</div> -->
<!-- <div class="panel panel-default">
    <div class="panel-heading" style="background: #eaeafa"><strong>PDA - MAIN MENU</strong></div>
    <div class="panel-body">
    			<TABLE>
                 	<TR>
                    	<TH WIDTH="20%" ALIGN = "LEFT">
                             <INPUT Type=Checkbox  style="border:0;" name="URL" value="pdaInBound"  >
                                &nbsp; PURCHASE
                              <TH WIDTH="20%" ALIGN = "LEFT">
                                <INPUT Type=Checkbox  style="border:0;" name="URL" value="pdaOutBound"  >
                                &nbsp; SALES
                             <TH WIDTH="20%" ALIGN = "LEFT">
                                <INPUT Type=Checkbox  style="border:0;" name="URL" value="pdaWarehouse"  >
                                &nbsp; In House
                            </TABLE>
    </div>
</div>-->

    <div class="row">
  		<div class="col-12 col-sm-12"><INPUT Type=Checkbox class="form-check-input"  style="border:0;" name="PdaPurchaseTransaction"  onclick="return checkAllPdaPurchaseTransaction(this.checked);">
                        &nbsp; Select/Unselect PDA - Purchase Transaction </div>
  </div>
 <div class="panel panel-default">
    <div class="panel-heading" style="background: #eaeafa"><strong>PDA - Purchase Transaction</strong></div>
    <div class="panel-body">
    	<TABLE>
                          <TR>
                          <TH WIDTH="20%" ALIGN = "LEFT">
                          <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="PdaPurchaseTransaction" value="pdainboundReceipt"  >
                          &nbsp; PURCHASE RECEIPT</label>
                         <!-- > <TH WIDTH="20%" ALIGN = "LEFT">
                          <INPUT Type=Checkbox  style="border:0;" name="PdaPurchaseTransaction" value="pdaTransferReceipt"  >
                          &nbsp; Transfer Receipt
                          <TH WIDTH="20%" ALIGN = "LEFT">
                          <INPUT Type=Checkbox  style="border:0;" name="URL" value="pdaLoanReceipt"  >
                          &nbsp; Bulk Loan Receipt -->
                          <TH WIDTH="20%" ALIGN = "LEFT">
                          <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="PdaPurchaseTransaction" value="pdaMiscReceipt"  >
                           &nbsp; GOODS RECEIPT</label>
                          </TABLE>
    </div>
</div>

    <div class="row">
  		<div class="col-12 col-sm-12"><INPUT Type=Checkbox class="form-check-input"  style="border:0;" name="PdaSalesTransaction"  onclick="return checkAllPdaSalesTransaction(this.checked);">
                     <strong>   &nbsp; Select/Unselect PDA - Sales Transaction</strong> </div>
  </div>

<div class="panel panel-default">
    <div class="panel-heading" style="background: #eaeafa"><strong>PDA - Sales Transaction</strong></div>
    <div class="panel-body">
    	<TABLE>
                    <TR>
                                             
                        <TH WIDTH="20%" ALIGN = "LEFT">
                        <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="PdaSalesTransaction"  value="pdaOutBoundIssue"  >
                        &nbsp; SALES PICK & ISSUE</label>
                        
                       <!--   <TH WIDTH="20%" ALIGN = "LEFT">
                        <INPUT Type=Checkbox  style="border:0;" name="URL" value="pdaOutBoundPicking" >
                        &nbsp; Sales Pick
                    	<TH WIDTH="20%" ALIGN = "LEFT">
                        <INPUT Type=Checkbox  style="border:0;" name="URL" value="pdaOutBoundIssue"  >
                        &nbsp; Sales Issue-->
                        
                        <!--  <TH WIDTH="25%" ALIGN = "LEFT">
                        <INPUT Type=Checkbox  style="border:0;" name="URL" value="pdaTransferPickIssue"  >
                        &nbsp; Transfer Pick/Issue
                        <TH WIDTH="25%" ALIGN = "LEFT">
                        <INPUT Type=Checkbox  style="border:0;" name="URL" value="pdaLoanPickIssue" >
                        &nbsp; Bulk Loan Pick/Issue-->
                        
                         <TH WIDTH="20%" ALIGN = "LEFT">
                       <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;" name="PdaSalesTransaction" value="pdaMiscIssue" >
                        &nbsp; GOODS ISSUE</label>
                 </TABLE> 
    </div>
</div>

    <div class="row">
  		<div class="col-12 col-sm-12"><INPUT Type=Checkbox class="form-check-input"  style="border:0;" name="PdaInHouse"  onclick="return checkAllPdaInHouse(this.checked);">
                     <strong>   &nbsp; Select/Unselect PDA - In House</strong> </div>
  </div>

 <div class="panel panel-default">
    <div class="panel-heading" style="background: #eaeafa"><strong>PDA - In House</strong></div>
    <div class="panel-body">
    	<TABLE>
                    <TR>
                      <TH WIDTH="25%" ALIGN = "LEFT">
                       <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;" name="PdaInHouse" value="pdaInventoryQry">
                        &nbsp; INV QUERY</label>
                       <!--   
                        <TH WIDTH="20%" ALIGN = "LEFT">
                        <INPUT Type=Checkbox  style="border:0;" name="URL" value="pdaPutAway">
                        &nbsp; Put Away
                        -->
                      <TH WIDTH="20%" ALIGN = "LEFT">
                        <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="PdaInHouse" value="pdaLocTransfer">
                        &nbsp; STOCK MOVE</label>
                      <TH WIDTH="20%" ALIGN = "LEFT">
                        <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="PdaInHouse" value="pdaStockTake">
                        &nbsp; STOCK TAKE</label>
                       
                     <!--  <TH WIDTH="25%" ALIGN = "LEFT">
                        <INPUT Type=Checkbox  style="border:0;" name="URL" value="pdaTransferObOrder">
                        &nbsp;  Outbound Transfer
                    
                    
                     <TH WIDTH="20%" ALIGN = "LEFT">
                        <INPUT Type=Checkbox  style="border:0;" name="URL" value="pdaKitting">
                        &nbsp; Kitting
                      </TH>--> 
                </Table>
    </div>
</div>
<!--  <div class="panel panel-default">
    <div class="panel-heading" style="background: #eaeafa"><strong>PDA -  MAIN MENU (SELL N TRACK)</strong></div>
    <div class="panel-body">
    	<TABLE>
                            <TR>
                              <TH WIDTH="20%" ALIGN = "LEFT">
                                <INPUT Type=Checkbox  style="border:0;" name="URL" value="pdaMobileSales"  >
                                &nbsp; Mobile Sales
                              <TH WIDTH="20%" ALIGN = "LEFT">
                                 <INPUT Type=Checkbox  style="border:0;" name="URL" value="pdaEnquiry"  >
                                  &nbsp; Enquiry
                             <TH WIDTH="20%" ALIGN = "LEFT">
                                <INPUT Type=Checkbox  style="border:0;" name="URL" value="pdaPos"  >
                                &nbsp; POS
                             
                            </TABLE>
    </div>
</div>
<div class="panel panel-default">
    <div class="panel-heading" style="background: #eaeafa"><strong>PDA - MOBILE SALES</strong></div>
    <div class="panel-body">  
    <TABLE>
                          <TR>
                          <TH WIDTH="20%" ALIGN = "LEFT">
                          <INPUT Type=Checkbox  style="border:0;" name="URL" value="pdaMobileSalesCreate" >
                          &nbsp; Create
                          <TH WIDTH="20%" ALIGN = "LEFT">
                          <INPUT Type=Checkbox  style="border:0;" name="URL" value="pdaMobileSalesEdit"  >
                          &nbsp; Edit
                          <TH WIDTH="20%" ALIGN = "LEFT">
                          <INPUT Type=Checkbox  style="border:0;" name="URL" value="pdaMobileSalesSummary"  >
                          &nbsp; Summary
                         </TABLE>
     </div>
</div>
<div class="panel panel-default">
    <div class="panel-heading" style="background: #eaeafa"><strong>PDA - ENQUIRY</strong></div>
    <div class="panel-body">  
     <TABLE>
                    <TR>
                        <TH WIDTH="20%" ALIGN = "LEFT">
                        <INPUT Type=Checkbox  style="border:0;" name="URL" value="pdaEnquiryInvQryByProduct" >
                        &nbsp; Query By Product
                       <TH WIDTH="20%" ALIGN = "LEFT">
                        <INPUT Type=Checkbox  style="border:0;" name="URL" value="pdaEnquiryInvQryByLoc" >
                        &nbsp; Query By Loc
                
                 </TABLE>
    </div>
</div>-->
                      <INPUT Type=Hidden name="URL" value="logout">
                  <TABLE CELLSPACING="0" WIDTH="100%" align="center">
                    <TR>
                      <TH ALIGN = "CENTER" width="100%" style="text-align: center;">
                      <!-- <button type="button" name="C" class="btn btn-default" onClick="window.location.href='../home'"><strong>Back</strong></button> -->
                        <!-- <button type="button" name="C" class="btn btn-default" onClick="{backNavigation('settings.jsp','UA');}"><strong>Back</strong></button> -->      
                        <button type="Submit" name="Action" class="btn btn-success"> Save</button>
                        <!-- <INPUT Type=Button name="C" Value="Back" Size = 10 onClick="window.location.href='../home'"> &nbsp;
                          <INPUT Type=Submit name="Action" Value=" Save " Size = 20> -->
                  </TABLE>
              
        
</form>
</div>
</div>
</div>
</div>
<jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
</jsp:include>