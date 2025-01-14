<%@page import="com.track.constants.IConstants"%>
<%@ page import="com.track.dao.*"%>
<%@ include file="header.jsp"%>

<%
String title = "Edit User Access Rights Group";
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
<script type="text/javascript" src="../jsp/js/general.js"></script>

<script type="text/javascript" src="../jsp/js/userLevel.js"></script>

<script>
function checkAll(isChk)
{
	 if (document.levelform.Home)
	    {
    if (document.levelform.Home.disabled == false)
    	document.levelform.Home.checked = isChk;
    for (i = 0; i < document.levelform.Home.length; i++)
    {
            if (document.levelform.Home[i].disabled == false)
        	document.levelform.Home[i].checked = isChk;
   
}
	    }
if (document.levelform.UserAdmin)
{
    if (document.levelform.UserAdmin.disabled == false)
    	document.levelform.UserAdmin.checked = isChk;
    for (i = 0; i < document.levelform.UserAdmin.length; i++)
    {
            if (document.levelform.UserAdmin[i].disabled == false)
        	document.levelform.UserAdmin[i].checked = isChk;
    }
}



    if (document.levelform.SystemMaster)
    {
        if (document.levelform.SystemMaster.disabled == false)
        	document.levelform.SystemMaster.checked = isChk;
        for (i = 0; i < document.levelform.SystemMaster.length; i++)
        {
                if (document.levelform.SystemMaster[i].disabled == false)
            	document.levelform.SystemMaster[i].checked = isChk;
        }
    }

    if (document.levelform.UomLoc)
	{
	    if (document.levelform.UomLoc.disabled == false)
	    	document.levelform.UomLoc.checked = isChk;
	    for (i = 0; i < document.levelform.UomLoc.length; i++)
	    {
	            if (document.levelform.UomLoc[i].disabled == false)
	        	document.levelform.UomLoc[i].checked = isChk;
	    }
	}

    if (document.form.UserEmployee)
    {
        if (document.form.UserEmployee.disabled == false)
        	document.form.UserEmployee.checked = isChk;
        for (i = 0; i < document.form.UserEmployee.length; i++)
        {
                if (document.form.UserEmployee[i].disabled == false)
            	document.form.UserEmployee[i].checked = isChk;
        }
    }
    

    if (document.levelform.SystemAdmin)
    {
        if (document.levelform.SystemAdmin.disabled == false)
        	document.levelform.SystemAdmin.checked = isChk;
        for (i = 0; i < document.levelform.SystemAdmin.length; i++)
        {
                if (document.levelform.SystemAdmin[i].disabled == false)
            	document.levelform.SystemAdmin[i].checked = isChk;
        }
    }

    if (document.levelform.AccountingAdmin)
    {
        if (document.levelform.AccountingAdmin.disabled == false)
        	document.levelform.AccountingAdmin.checked = isChk;
        for (i = 0; i < document.levelform.AccountingAdmin.length; i++)
        {
                if (document.levelform.AccountingAdmin[i].disabled == false)
            	document.levelform.AccountingAdmin[i].checked = isChk;
        }
    }



    if (document.levelform.OrderAdmin)
    {
        if (document.levelform.OrderAdmin.disabled == false)
        	document.levelform.OrderAdmin.checked = isChk;
        for (i = 0; i < document.levelform.OrderAdmin.length; i++)
        {
                if (document.levelform.OrderAdmin[i].disabled == false)
            	document.levelform.OrderAdmin[i].checked = isChk;
        }
    }
    if (document.levelform.Distribution)
    {
        if (document.levelform.Distribution.disabled == false)
        	document.levelform.Distribution.checked = isChk;
        for (i = 0; i < document.levelform.Distribution.length; i++)
        {
                if (document.levelform.Distribution[i].disabled == false)
            	document.levelform.Distribution[i].checked = isChk;
        }
    }


    if (document.levelform.OrderAdminHS)
    {
        if (document.levelform.OrderAdminHS.disabled == false)
        	document.levelform.OrderAdminHS.checked = isChk;
        for (i = 0; i < document.levelform.OrderAdminHS.length; i++)
        {
                if (document.levelform.OrderAdminHS[i].disabled == false)
            	document.levelform.OrderAdminHS[i].checked = isChk;
        }
    }
    
    if (document.levelform.Supplier)
    {
        if (document.levelform.Supplier.disabled == false)
        	document.levelform.Supplier.checked = isChk;
        for (i = 0; i < document.levelform.Supplier.length; i++)
        {
                if (document.levelform.Supplier[i].disabled == false)
            	document.levelform.Supplier[i].checked = isChk;
        }
    }
    
    if (document.levelform.Customer)
    {
        if (document.levelform.Customer.disabled == false)
        	document.levelform.Customer.checked = isChk;
        for (i = 0; i < document.levelform.Customer.length; i++)
        {
                if (document.levelform.Customer[i].disabled == false)
            	document.levelform.Customer[i].checked = isChk;
        }
    }



    if (document.levelform.OrderConfiguration)
    {
        if (document.levelform.OrderConfiguration.disabled == false)
        	document.levelform.OrderConfiguration.checked = isChk;
        for (i = 0; i < document.levelform.OrderConfiguration.length; i++)
        {
                if (document.levelform.OrderConfiguration[i].disabled == false)
            	document.levelform.OrderConfiguration[i].checked = isChk;
        }
    }

    if (document.levelform.EmailConfiguration)
    {
        if (document.levelform.EmailConfiguration.disabled == false)
        	document.levelform.EmailConfiguration.checked = isChk;
        for (i = 0; i < document.levelform.EmailConfiguration.length; i++)
        {
                if (document.levelform.EmailConfiguration[i].disabled == false)
            	document.levelform.EmailConfiguration[i].checked = isChk;
        }
    }


    if (document.levelform.OrderManagement)
    {
        if (document.levelform.OrderManagement.disabled == false)
        	document.levelform.OrderManagement.checked = isChk;
        for (i = 0; i < document.levelform.OrderManagement.length; i++)
        {
                if (document.levelform.OrderManagement[i].disabled == false)
            	document.levelform.OrderManagement[i].checked = isChk;
        }
    }




    if (document.levelform.OrderManagement1)
    {
        if (document.levelform.OrderManagement1.disabled == false)
        	document.levelform.OrderManagement1.checked = isChk;
        for (i = 0; i < document.levelform.OrderManagement1.length; i++)
        {
                if (document.levelform.OrderManagement1[i].disabled == false)
            	document.levelform.OrderManagement1[i].checked = isChk;
        }
    }
    
    if (document.levelform.Project)
    {
        if (document.levelform.Project.disabled == false)
        	document.levelform.Project.checked = isChk;
        for (i = 0; i < document.levelform.Project.length; i++)
        {
                if (document.levelform.Project[i].disabled == false)
            	document.levelform.Project[i].checked = isChk;
        }
    }
    
    if (document.levelform.Purchaseestimate)
    {
        if (document.levelform.Purchaseestimate.disabled == false)
        	document.levelform.Purchaseestimate.checked = isChk;
        for (i = 0; i < document.levelform.Purchaseestimate.length; i++)
        {
                if (document.levelform.Purchaseestimate[i].disabled == false)
            	document.levelform.Purchaseestimate[i].checked = isChk;
        }
    }
    
    if (document.levelform.PurchaseTransaction)
    {
        if (document.levelform.PurchaseTransaction.disabled == false)
        	document.levelform.PurchaseTransaction.checked = isChk;
        for (i = 0; i < document.levelform.PurchaseTransaction.length; i++)
        {
                if (document.levelform.PurchaseTransaction[i].disabled == false)
            	document.levelform.PurchaseTransaction[i].checked = isChk;
        }
    }

    if (document.levelform.PurchaseReturn)
    {
        if (document.levelform.PurchaseReturn.disabled == false)
        	document.levelform.PurchaseReturn.checked = isChk;
        for (i = 0; i < document.levelform.PurchaseReturn.length; i++)
        {
                if (document.levelform.PurchaseReturn[i].disabled == false)
            	document.levelform.PurchaseReturn[i].checked = isChk;
        }
    }

    if (document.levelform.ProductReturn)
    {
        if (document.levelform.ProductReturn.disabled == false)
        	document.levelform.ProductReturn.checked = isChk;
        for (i = 0; i < document.levelform.ProductReturn.length; i++)
        {
                if (document.levelform.ProductReturn[i].disabled == false)
            	document.levelform.ProductReturn[i].checked = isChk;
        }
    }

    if (document.levelform.ProductReturnReceive)
    {
        if (document.levelform.ProductReturnReceive.disabled == false)
        	document.levelform.ProductReturnReceive.checked = isChk;
        for (i = 0; i < document.levelform.ProductReturnReceive.length; i++)
        {
                if (document.levelform.ProductReturnReceive[i].disabled == false)
            	document.levelform.ProductReturnReceive[i].checked = isChk;
        }
    }
        

    if (document.levelform.PurchaseReports)
    {
        if (document.levelform.PurchaseReports.disabled == false)
        	document.levelform.PurchaseReports.checked = isChk;
        for (i = 0; i < document.levelform.PurchaseReports.length; i++)
        {
                if (document.levelform.PurchaseReports[i].disabled == false)
            	document.levelform.PurchaseReports[i].checked = isChk;
        }
    }
    

    if (document.levelform.SalesEstimate)
    {
        if (document.levelform.SalesEstimate.disabled == false)
        	document.levelform.SalesEstimate.checked = isChk;
        for (i = 0; i < document.levelform.SalesEstimate.length; i++)
        {
                if (document.levelform.SalesEstimate[i].disabled == false)
            	document.levelform.SalesEstimate[i].checked = isChk;
        }
    }
    
    if (document.levelform.SalesEstimateReports)
    {
        if (document.levelform.SalesEstimateReports.disabled == false)
        	document.levelform.SalesEstimateReports.checked = isChk;
        for (i = 0; i < document.levelform.SalesEstimateReports.length; i++)
        {
                if (document.levelform.SalesEstimateReports[i].disabled == false)
            	document.levelform.SalesEstimateReports[i].checked = isChk;
        }
    }

   if (document.levelform.InHouse)
    {
        if (document.levelform.InHouse.disabled == false)
        	document.levelform.InHouse.checked = isChk;
        for (i = 0; i < document.levelform.InHouse.length; i++)
        {
                if (document.levelform.InHouse[i].disabled == false)
            	document.levelform.InHouse[i].checked = isChk;
        }
    }


    if (document.levelform.SalesTransaction)
    {
        if (document.levelform.SalesTransaction.disabled == false)
        	document.levelform.SalesTransaction.checked = isChk;
        for (i = 0; i < document.levelform.SalesTransaction.length; i++)
        {
                if (document.levelform.SalesTransaction[i].disabled == false)
            	document.levelform.SalesTransaction[i].checked = isChk;
        }
    }


    if (document.levelform.SalesReturn)
    {
        if (document.levelform.SalesReturn.disabled == false)
        	document.levelform.SalesReturn.checked = isChk;
        for (i = 0; i < document.levelform.SalesReturn.length; i++)
        {
                if (document.levelform.SalesReturn[i].disabled == false)
            	document.levelform.SalesReturn[i].checked = isChk;
        }
    }
    
    if (document.levelform.SalesOrder)
    {
        if (document.levelform.SalesOrder.disabled == false)
        	document.levelform.SalesOrder.checked = isChk;
        for (i = 0; i < document.levelform.SalesOrder.length; i++)
        {
                if (document.levelform.SalesOrder[i].disabled == false)
            	document.levelform.SalesOrder[i].checked = isChk;
        }
    }

    /* NAVAS */
    
    if (document.levelform.Consignment)
  {
      if (document.levelform.Consignment.disabled == false)
      	document.levelform.Consignment.checked = isChk;
      for (i = 0; i < document.levelform.Consignment.length; i++)
      {
              if (document.levelform.Consignment[i].disabled == false)
          	document.levelform.Consignment[i].checked = isChk;
      }
  }

    if (document.levelform.SalesReports)
    {
        if (document.levelform.SalesReports.disabled == false)
        	document.levelform.SalesReports.checked = isChk;
        for (i = 0; i < document.levelform.SalesReports.length; i++)
        {
                if (document.levelform.SalesReports[i].disabled == false)
            	document.levelform.SalesReports[i].checked = isChk;
        }
    }

    if (document.levelform.RentalTransaction)
    {
        if (document.levelform.RentalTransaction.disabled == false)
        	document.levelform.RentalTransaction.checked = isChk;
        for (i = 0; i < document.levelform.RentalTransaction.length; i++)
        {
                if (document.levelform.RentalTransaction[i].disabled == false)
            	document.levelform.RentalTransaction[i].checked = isChk;
        }
    }

    if (document.levelform.ConsignmentReports)
    {
        if (document.levelform.ConsignmentReports.disabled == false)
        	document.levelform.ConsignmentReports.checked = isChk;
        for (i = 0; i < document.levelform.ConsignmentReports.length; i++)
        {
                if (document.levelform.ConsignmentReports[i].disabled == false)
            	document.levelform.ConsignmentReports[i].checked = isChk;
        }
    }
    
    
    if (document.levelform.Reports)
    {
        if (document.levelform.Reports.disabled == false)
        	document.levelform.Reports.checked = isChk;
        for (i = 0; i < document.levelform.Reports.length; i++)
        {
                if (document.levelform.Reports[i].disabled == false)
            	document.levelform.Reports[i].checked = isChk;
        }
    }
    if (document.levelform.PosReports)
    {
        if (document.levelform.PosReports.disabled == false)
        	document.levelform.PosReports.checked = isChk;
        for (i = 0; i < document.levelform.PosReports.length; i++)
        {
                if (document.levelform.PosReports[i].disabled == false)
            	document.levelform.PosReports[i].checked = isChk;
        }
    }



    if (document.levelform.AccountingHome)
    {
        if (document.levelform.AccountingHome.disabled == false)
        	document.levelform.AccountingHome.checked = isChk;
        for (i = 0; i < document.levelform.AccountingHome.length; i++)
        {
                if (document.levelform.AccountingHome[i].disabled == false)
            	document.levelform.AccountingHome[i].checked = isChk;
        }
    }


   if (document.levelform.AccountingMaster)
    {
        if (document.levelform.AccountingMaster.disabled == false)
        	document.levelform.AccountingMaster.checked = isChk;
        for (i = 0; i < document.levelform.AccountingMaster.length; i++)
        {
                if (document.levelform.AccountingMaster[i].disabled == false)
            	document.levelform.AccountingMaster[i].checked = isChk;
        }
    }



    if (document.levelform.AccountingTransactions)
    {
        if (document.levelform.AccountingTransactions.disabled == false)
        	document.levelform.AccountingTransactions.checked = isChk;
        for (i = 0; i < document.levelform.AccountingTransactions.length; i++)
        {
                if (document.levelform.AccountingTransactions[i].disabled == false)
            	document.levelform.AccountingTransactions[i].checked = isChk;
        }
    }



    if (document.levelform.AccountingReports)
    {
        if (document.levelform.AccountingReports.disabled == false)
        	document.levelform.AccountingReports.checked = isChk;
        for (i = 0; i < document.levelform.AccountingReports.length; i++)
        {
                if (document.levelform.AccountingReports[i].disabled == false)
            	document.levelform.AccountingReports[i].checked = isChk;
        }
    }
    if (document.levelform.OwnerApp)
    {
        if (document.levelform.OwnerApp.disabled == false)
        	document.levelform.OwnerApp.checked = isChk;
        for (i = 0; i < document.levelform.OwnerApp.length; i++)
        {
                if (document.levelform.OwnerApp[i].disabled == false)
            	document.levelform.OwnerApp[i].checked = isChk;
        }
    }


   if (document.levelform.PdaPurchaseTransaction)
    {
        if (document.levelform.PdaPurchaseTransaction.disabled == false)
        	document.levelform.PdaPurchaseTransaction.checked = isChk;
        for (i = 0; i < document.levelform.PdaPurchaseTransaction.length; i++)
        {
                if (document.levelform.PdaPurchaseTransaction[i].disabled == false)
            	document.levelform.PdaPurchaseTransaction[i].checked = isChk;
        }
    }


  if (document.levelform.PdaSalesTransaction)
    {
        if (document.levelform.PdaSalesTransaction.disabled == false)
        	document.levelform.PdaSalesTransaction.checked = isChk;
        for (i = 0; i < document.levelform.PdaSalesTransaction.length; i++)
        {
                if (document.levelform.PdaSalesTransaction[i].disabled == false)
            	document.levelform.PdaSalesTransaction[i].checked = isChk;
        }
    }


    if (document.levelform.PdaInHouse)
    {
        if (document.levelform.PdaInHouse.disabled == false)
        	document.levelform.PdaInHouse.checked = isChk;
        for (i = 0; i < document.levelform.PdaInHouse.length; i++)
        {
                if (document.levelform.PdaInHouse[i].disabled == false)
            	document.levelform.PdaInHouse[i].checked = isChk;
        }
    }
}

function checkAllHome(isChk)

{
    if (document.levelform.Home)  
    {
        if (document.levelform.Home.disabled == false)
        	document.levelform.Home.checked = isChk;
        for (i = 0; i < document.levelform.Home.length; i++)
        {
            if (document.levelform.Home[i].disabled == false)
            	document.levelform.Home[i].checked = isChk;
        }
    }
}

function checkAllUserAdmin(isChk)
{
    if (document.levelform.UserAdmin)
    {
        if (document.levelform.UserAdmin.disabled == false)
        	document.levelform.UserAdmin.checked = isChk;
        for (i = 0; i < document.levelform.UserAdmin.length; i++)
        {
                if (document.levelform.UserAdmin[i].disabled == false)
            	document.levelform.UserAdmin[i].checked = isChk;
        }
    }
}

function checkAllSystemMaster(isChk)
{
    if (document.levelform.SystemMaster)
    {
        if (document.levelform.SystemMaster.disabled == false)
        	document.levelform.SystemMaster.checked = isChk;
        for (i = 0; i < document.levelform.SystemMaster.length; i++)
        {
                if (document.levelform.SystemMaster[i].disabled == false)
            	document.levelform.SystemMaster[i].checked = isChk;
        }
    }
}

function checkAllUomLoc(isChk)
{
	if (document.levelform.UomLoc)
	{
	    if (document.levelform.UomLoc.disabled == false)
	    	document.levelform.UomLoc.checked = isChk;
	    for (i = 0; i < document.levelform.UomLoc.length; i++)
	    {
	            if (document.levelform.UomLoc[i].disabled == false)
	        	document.levelform.UomLoc[i].checked = isChk;
	    }
	}
}

function checkAllUserEmployee(isChk)
{
    if (document.levelform.UserEmployee)
    {
        if (document.levelform.UserEmployee.disabled == false)
        	document.levelform.UserEmployee.checked = isChk;
        for (i = 0; i < document.levelform.UserEmployee.length; i++)
        {
                if (document.levelform.UserEmployee[i].disabled == false)
            	document.levelform.UserEmployee[i].checked = isChk;
        }
    }
}

function checkAllSystemAdmin(isChk)
{
    if (document.levelform.SystemAdmin)
    {
        if (document.levelform.SystemAdmin.disabled == false)
        	document.levelform.SystemAdmin.checked = isChk;
        for (i = 0; i < document.levelform.SystemAdmin.length; i++)
        {
                if (document.levelform.SystemAdmin[i].disabled == false)
            	document.levelform.SystemAdmin[i].checked = isChk;
        }
    }
}

function checkAllAccountingAdmin(isChk)
{
    if (document.levelform.AccountingAdmin)
    {
        if (document.levelform.AccountingAdmin.disabled == false)
        	document.levelform.AccountingAdmin.checked = isChk;
        for (i = 0; i < document.levelform.AccountingAdmin.length; i++)
        {
                if (document.levelform.AccountingAdmin[i].disabled == false)
            	document.levelform.AccountingAdmin[i].checked = isChk;
        }
    }
}

function checkAllOrderAdmin(isChk)
{
    if (document.levelform.OrderAdmin)
    {
        if (document.levelform.OrderAdmin.disabled == false)
        	document.levelform.OrderAdmin.checked = isChk;
        for (i = 0; i < document.levelform.OrderAdmin.length; i++)
        {
                if (document.levelform.OrderAdmin[i].disabled == false)
            	document.levelform.OrderAdmin[i].checked = isChk;
        }
    }
}
function checkAllDistribution(isChk)
{
    if (document.levelform.Distribution)
    {
        if (document.levelform.Distribution.disabled == false)
        	document.levelform.Distribution.checked = isChk;
        for (i = 0; i < document.levelform.Distribution.length; i++)
        {
                if (document.levelform.Distribution[i].disabled == false)
            	document.levelform.Distribution[i].checked = isChk;
        }
    }
}

function checkAllOrderAdminHS(isChk)
{
if (document.levelform.OrderAdminHS)
{
    if (document.levelform.OrderAdminHS.disabled == false)
    	document.levelform.OrderAdminHS.checked = isChk;
    for (i = 0; i < document.levelform.OrderAdminHS.length; i++)
    {
            if (document.levelform.OrderAdminHS[i].disabled == false)
        	document.levelform.OrderAdminHS[i].checked = isChk;
    }
}
}

function checkAllSupplier(isChk)
{
if (document.levelform.Supplier)
{
    if (document.levelform.Supplier.disabled == false)
    	document.levelform.Supplier.checked = isChk;
    for (i = 0; i < document.levelform.Supplier.length; i++)
    {
            if (document.levelform.Supplier[i].disabled == false)
        	document.levelform.Supplier[i].checked = isChk;
    }
}
}
function checkAllCustomer(isChk)
{
if (document.levelform.Customer)
{
    if (document.levelform.Customer.disabled == false)
    	document.levelform.Customer.checked = isChk;
    for (i = 0; i < document.levelform.Customer.length; i++)
    {
            if (document.levelform.Customer[i].disabled == false)
        	document.levelform.Customer[i].checked = isChk;
    }
}
}

function checkAllOrderConfiguration(isChk)
{
    if (document.levelform.OrderConfiguration)
    {
        if (document.levelform.OrderConfiguration.disabled == false)
        	document.levelform.OrderConfiguration.checked = isChk;
        for (i = 0; i < document.levelform.OrderConfiguration.length; i++)
        {
                if (document.levelform.OrderConfiguration[i].disabled == false)
            	document.levelform.OrderConfiguration[i].checked = isChk;
        }
    }
}

function checkAllEmailConfiguration(isChk)
{
    if (document.levelform.EmailConfiguration)
    {
        if (document.levelform.EmailConfiguration.disabled == false)
        	document.levelform.EmailConfiguration.checked = isChk;
        for (i = 0; i < document.levelform.EmailConfiguration.length; i++)
        {
                if (document.levelform.EmailConfiguration[i].disabled == false)
            	document.levelform.EmailConfiguration[i].checked = isChk;
        }
    }
}

function checkAllOrderManagement(isChk)
{
    if (document.levelform.OrderManagement)
    {
        if (document.levelform.OrderManagement.disabled == false)
        	document.levelform.OrderManagement.checked = isChk;
        for (i = 0; i < document.levelform.OrderManagement.length; i++)
        {
                if (document.levelform.OrderManagement[i].disabled == false)
            	document.levelform.OrderManagement[i].checked = isChk;
        }
    }
}

function checkAllProject(isChk)
{
    if (document.levelform.Project)
    {
        if (document.levelform.Project.disabled == false)
        	document.levelform.Project.checked = isChk;
        for (i = 0; i < document.levelform.Project.length; i++)
        {
                if (document.levelform.Project[i].disabled == false)
            	document.levelform.Project[i].checked = isChk;
        }
    }
}

function checkAllPurchaseestimate(isChk)
{
    if (document.levelform.Purchaseestimate)
    {
        if (document.levelform.Purchaseestimate.disabled == false)
        	document.levelform.Purchaseestimate.checked = isChk;
        for (i = 0; i < document.levelform.Purchaseestimate.length; i++)
        {
                if (document.levelform.Purchaseestimate[i].disabled == false)
            	document.levelform.Purchaseestimate[i].checked = isChk;
        }
    }
}

function checkAllOrderManagement1(isChk)
{
    if (document.levelform.OrderManagement1)
    {
        if (document.levelform.OrderManagement1.disabled == false)
        	document.levelform.OrderManagement1.checked = isChk;
        for (i = 0; i < document.levelform.OrderManagement1.length; i++)
        {
                if (document.levelform.OrderManagement1[i].disabled == false)
            	document.levelform.OrderManagement1[i].checked = isChk;
        }
    }
}

function checkAllPurchaseTransaction(isChk)
{
if (document.levelform.PurchaseTransaction)
{
    if (document.levelform.PurchaseTransaction.disabled == false)
    	document.levelform.PurchaseTransaction.checked = isChk;
    for (i = 0; i < document.levelform.PurchaseTransaction.length; i++)
    {
            if (document.levelform.PurchaseTransaction[i].disabled == false)
        	document.levelform.PurchaseTransaction[i].checked = isChk;
    }
}
}

function checkAllPurchaseReturn(isChk)
{
if (document.levelform.PurchaseReturn)
{
    if (document.levelform.PurchaseReturn.disabled == false)
    	document.levelform.PurchaseReturn.checked = isChk;
    for (i = 0; i < document.levelform.PurchaseReturn.length; i++)
    {
            if (document.levelform.PurchaseReturn[i].disabled == false)
        	document.levelform.PurchaseReturn[i].checked = isChk;
    }
}
}

function checkAllProductReturn(isChk)
{
if (document.levelform.ProductReturn)
{
    if (document.levelform.ProductReturn.disabled == false)
    	document.levelform.ProductReturn.checked = isChk;
    for (i = 0; i < document.levelform.ProductReturn.length; i++)
    {
            if (document.levelform.ProductReturn[i].disabled == false)
        	document.levelform.ProductReturn[i].checked = isChk;
    }
}
}

function checkAllProductReturnReceive(isChk)
{
if (document.levelform.ProductReturnReceive)
{
    if (document.levelform.ProductReturnReceive.disabled == false)
    	document.levelform.ProductReturnReceive.checked = isChk;
    for (i = 0; i < document.levelform.ProductReturnReceive.length; i++)
    {
            if (document.levelform.ProductReturnReceive[i].disabled == false)
        	document.levelform.ProductReturnReceive[i].checked = isChk;
    }
}
}

function checkAllPurchaseReports(isChk)
{
if (document.levelform.PurchaseReports)
{
    if (document.levelform.PurchaseReports.disabled == false)
    	document.levelform.PurchaseReports.checked = isChk;
    for (i = 0; i < document.levelform.PurchaseReports.length; i++)
    {
            if (document.levelform.PurchaseReports[i].disabled == false)
        	document.levelform.PurchaseReports[i].checked = isChk;
    }
}
}

function checkAllSalesEstimate(isChk)
{
    if (document.levelform.SalesEstimate)
    {
        if (document.levelform.SalesEstimate.disabled == false)
        	document.levelform.SalesEstimate.checked = isChk;
        for (i = 0; i < document.levelform.SalesEstimate.length; i++)
        {
                if (document.levelform.SalesEstimate[i].disabled == false)
            	document.levelform.SalesEstimate[i].checked = isChk;
        }
    }
}



function checkAllSalesEstimateReports(isChk)
{
    if (document.levelform.SalesEstimateReports)
    {
        if (document.levelform.SalesEstimateReports.disabled == false)
        	document.levelform.SalesEstimateReports.checked = isChk;
        for (i = 0; i < document.levelform.SalesEstimateReports.length; i++)
        {
                if (document.levelform.SalesEstimateReports[i].disabled == false)
            	document.levelform.SalesEstimateReports[i].checked = isChk;
        }
    }
}

function checkAllInHouse(isChk)
{
    if (document.levelform.InHouse)
    {
        if (document.levelform.InHouse.disabled == false)
        	document.levelform.InHouse.checked = isChk;
        for (i = 0; i < document.levelform.InHouse.length; i++)
        {
                if (document.levelform.InHouse[i].disabled == false)
            	document.levelform.InHouse[i].checked = isChk;
        }
    }
}

function checkAllSalesTransaction(isChk)
{
    if (document.levelform.SalesTransaction)
    {
        if (document.levelform.SalesTransaction.disabled == false)
        	document.levelform.SalesTransaction.checked = isChk;
        for (i = 0; i < document.levelform.SalesTransaction.length; i++)
        {
                if (document.levelform.SalesTransaction[i].disabled == false)
            	document.levelform.SalesTransaction[i].checked = isChk;
        }
    }
}

function checkAllSalesReturn(isChk)
{
if (document.levelform.SalesReturn)
{
    if (document.levelform.SalesReturn.disabled == false)
    	document.levelform.SalesReturn.checked = isChk;
    for (i = 0; i < document.levelform.SalesReturn.length; i++)
    {
            if (document.levelform.SalesReturn[i].disabled == false)
        	document.levelform.SalesReturn[i].checked = isChk;
    }
}
}

function checkAllSalesOrder(isChk)
{
if (document.levelform.SalesOrder)
{
    if (document.levelform.SalesOrder.disabled == false)
    	document.levelform.SalesOrder.checked = isChk;
    for (i = 0; i < document.levelform.SalesOrder.length; i++)
    {
            if (document.levelform.SalesOrder[i].disabled == false)
        	document.levelform.SalesOrder[i].checked = isChk;
    }
}
}

/* NAVAS */

function checkAllConsignment(isChk)
{
if (document.levelform.Consignment)
{
    if (document.levelform.Consignment.disabled == false)
    	document.levelform.Consignment.checked = isChk;
    for (i = 0; i < document.levelform.Consignment.length; i++)
    {
            if (document.levelform.Consignment[i].disabled == false)
        	document.levelform.Consignment[i].checked = isChk;
    }
}
}

function checkAllSalesReports(isChk)
{
if (document.levelform.SalesReports)
{
    if (document.levelform.SalesReports.disabled == false)
    	document.levelform.SalesReports.checked = isChk;
    for (i = 0; i < document.levelform.SalesReports.length; i++)
    {
            if (document.levelform.SalesReports[i].disabled == false)
        	document.levelform.SalesReports[i].checked = isChk;
    }
}
}

function checkAllRentalTransaction(isChk)
{
    if (document.levelform.RentalTransaction)
    {
        if (document.levelform.RentalTransaction.disabled == false)
        	document.levelform.RentalTransaction.checked = isChk;
        for (i = 0; i < document.levelform.RentalTransaction.length; i++)
        {
                if (document.levelform.RentalTransaction[i].disabled == false)
            	document.levelform.RentalTransaction[i].checked = isChk;
        }
    }
}

function checkAllConsignmentReports(isChk)
{
    if (document.levelform.ConsignmentReports)
    {
        if (document.levelform.ConsignmentReports.disabled == false)
        	document.levelform.ConsignmentReports.checked = isChk;
        for (i = 0; i < document.levelform.ConsignmentReports.length; i++)
        {
                if (document.levelform.ConsignmentReports[i].disabled == false)
            	document.levelform.ConsignmentReports[i].checked = isChk;
        }
    }
}


function checkAllReports(isChk)
{
    if (document.levelform.Reports)
    {
        if (document.levelform.Reports.disabled == false)
        	document.levelform.Reports.checked = isChk;
        for (i = 0; i < document.levelform.Reports.length; i++)
        {
                if (document.levelform.Reports[i].disabled == false)
            	document.levelform.Reports[i].checked = isChk;
        }
    }
}
function checkAllPOSReports(isChk)
{
    if (document.levelform.PosReports)
    {
        if (document.levelform.PosReports.disabled == false)
        	document.levelform.PosReports.checked = isChk;
        for (i = 0; i < document.levelform.PosReports.length; i++)
        {
                if (document.levelform.PosReports[i].disabled == false)
            	document.levelform.PosReports[i].checked = isChk;
        }
    }
}

function checkAllAccountingReports(isChk)
{
    if (document.levelform.AccountingReports)
    {
        if (document.levelform.AccountingReports.disabled == false)
        	document.levelform.AccountingReports.checked = isChk;
        for (i = 0; i < document.levelform.AccountingReports.length; i++)
        {
                if (document.levelform.AccountingReports[i].disabled == false)
            	document.levelform.AccountingReports[i].checked = isChk;
        }
    }
}

function checkAllActivityLogs(isChk)
{
    if (document.levelform.ActivityLogs)
    {
        if (document.levelform.ActivityLogs.disabled == false)
        	document.levelform.ActivityLogs.checked = isChk;
        for (i = 0; i < document.levelform.ActivityLogs.length; i++)
        {
                if (document.levelform.ActivityLogs[i].disabled == false)
            	document.levelform.ActivityLogs[i].checked = isChk;
        }
    }
}
function checkAllOwnerApp(isChk)
{
    if (document.levelform.OwnerApp)
    {
        if (document.levelform.OwnerApp.disabled == false)
        	document.levelform.OwnerApp.checked = isChk;
        for (i = 0; i < document.levelform.OwnerApp.length; i++)
        {
                if (document.levelform.OwnerApp[i].disabled == false)
            	document.levelform.OwnerApp[i].checked = isChk;
        }
    }
}

function checkAllPeppolIntegration(isChk)
{
    if (document.levelform.PeppolIntegration)
    {
        if (document.levelform.PeppolIntegration.disabled == false)
        	document.levelform.PeppolIntegration.checked = isChk;
        for (i = 0; i < document.levelform.PeppolIntegration.length; i++)
        {
                if (document.levelform.PeppolIntegration[i].disabled == false)
            	document.levelform.PeppolIntegration[i].checked = isChk;
        }
    }
}

function checkAllAccountingHome(isChk)
{
    if (document.levelform.AccountingHome)
    {
        if (document.levelform.AccountingHome.disabled == false)
        	document.levelform.AccountingHome.checked = isChk;
        for (i = 0; i < document.levelform.AccountingHome.length; i++)
        {
                if (document.levelform.AccountingHome[i].disabled == false)
            	document.levelform.AccountingHome[i].checked = isChk;
        }
    }
}

function checkAllAccountingMaster(isChk)
{
    if (document.levelform.AccountingMaster)
    {
        if (document.levelform.AccountingMaster.disabled == false)
        	document.levelform.AccountingMaster.checked = isChk;
        for (i = 0; i < document.levelform.AccountingMaster.length; i++)
        {
                if (document.levelform.AccountingMaster[i].disabled == false)
            	document.levelform.AccountingMaster[i].checked = isChk;
        }
    }
}

function checkAllAccountingTransactions(isChk)
{
    if (document.levelform.AccountingTransactions)
    {
        if (document.levelform.AccountingTransactions.disabled == false)
        	document.levelform.AccountingTransactions.checked = isChk;
        for (i = 0; i < document.levelform.AccountingTransactions.length; i++)
        {
                if (document.levelform.AccountingTransactions[i].disabled == false)
            	document.levelform.AccountingTransactions[i].checked = isChk;
        }
    }
}

function checkAllAccountingReports(isChk)
{
    if (document.levelform.AccountingReports)
    {
        if (document.levelform.AccountingReports.disabled == false)
        	document.levelform.AccountingReports.checked = isChk;
        for (i = 0; i < document.levelform.AccountingReports.length; i++)
        {
                if (document.levelform.AccountingReports[i].disabled == false)
            	document.levelform.AccountingReports[i].checked = isChk;
        }
    }
}

function checkAllPdaPurchaseTransaction(isChk)
{
    if (document.levelform.PdaPurchaseTransaction)
    {
        if (document.levelform.PdaPurchaseTransaction.disabled == false)
        	document.levelform.PdaPurchaseTransaction.checked = isChk;
        for (i = 0; i < document.levelform.PdaPurchaseTransaction.length; i++)
        {
                if (document.levelform.PdaPurchaseTransaction[i].disabled == false)
            	document.levelform.PdaPurchaseTransaction[i].checked = isChk;
        }
    }
}

function checkAllPdaSalesTransaction(isChk)
{
    if (document.levelform.PdaSalesTransaction)
    {
        if (document.levelform.PdaSalesTransaction.disabled == false)
        	document.levelform.PdaSalesTransaction.checked = isChk;
        for (i = 0; i < document.levelform.PdaSalesTransaction.length; i++)
        {
                if (document.levelform.PdaSalesTransaction[i].disabled == false)
            	document.levelform.PdaSalesTransaction[i].checked = isChk;
        }
    }
}

function checkAllPdaInHouse(isChk)
{
    if (document.levelform.PdaInHouse)
    {
        if (document.levelform.PdaInHouse.disabled == false)
        	document.levelform.PdaInHouse.checked = isChk;
        for (i = 0; i < document.levelform.PdaInHouse.length; i++)
        {
                if (document.levelform.PdaInHouse[i].disabled == false)
            	document.levelform.PdaInHouse[i].checked = isChk;
        }
    }
}
</script>

<jsp:useBean id="sl" class="com.track.gates.selectBean" />
<jsp:useBean id="ub" class="com.track.gates.userBean" />


<div class="container-fluid m-t-20">
	 <div class="box"> 
	 <!-- Thanzith Modified on 28.02.2022 -->
	 <ul class="breadcrumb backpageul">    
                <li class="underline-on-hover"><a href="../home">Dashboard</a></li>	
                 <li><a href="../useraccess/invsummary"><span class="underline-on-hover">User Access Rights Group Summary</span></a></li>	
                <li><label>Edit User Access Rights Group</label></li>                                   
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
		
 
<%!ArrayList al;

	//Method for selecting the CheckBox as 'Checked'
	public String checkOption(String str)
	
			{
		for (int i = 0; i < al.size(); i++) {
			if (str.equalsIgnoreCase(al.get(i).toString()))
				return "checked";
		}
		return ""; 
	}%>
<%
	ub.setmLogger(mLogger);
	sl.setmLogger(mLogger);
	String caption = "Maintain";
	String delParam = "";
	String disabled = "";
	String disabledInView = "";
	String plant = ((String) session.getAttribute("PLANT")).trim();
	PlantMstDAO plantMstDAO = new PlantMstDAO();
	String ISPEPPOL =new PlantMstDAO().getisPeppol(plant);
	String  MANAGEWORKFLOW1 = plantMstDAO.getMANAGEWORKFLOW1(plant);
	String COMP_INDUSTRY = plantMstDAO.getCOMP_INDUSTRY(plant);//Check Company Industry
	String  ENABLE_POS =new PlantMstDAO().getispos(plant);
	String plant1 = plant;
	if (plant.equalsIgnoreCase("track")) {
		plant = "";
	} else {
		plant = plant + "_";
	}
	Enumeration e = request.getParameterNames();
	while (e.hasMoreElements()) {
		String s = e.nextElement().toString();

		if (s.equalsIgnoreCase("view")) {
			caption = "View";
			disabledInView = "disabled";
		}

		else if (s.equalsIgnoreCase("del")) {
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
	var chk=confirm('Are you sure you would like to save?');
	if(chk){
	document.levelform.action="../jsp/userLevelSubmit.jsp?Action=Update";
	document.levelform.submit();}
	 else
	 {
		 return false;
	 }
}
</script>


<form name="form" class="form-horizontal" method="POST" action="../useraccess/invedit">
<br>
<%=delParam%>
<div class="form-group">
<label class="control-label col-sm-4">Company</label>
<div class="col-sm-4">
       <%
			String comp = plant1.toUpperCase();
  		%>
 <input value="<%=comp%>" class="form-control" readonly>
 </div>
 </div>

 <div class="form-group">
 <label class="control-label col-sm-4">Select User Access Rights Group</label>
 <div class="col-sm-4">
 <SELECT class="form-control" style="width: 100%" data-toggle="dropdown" data-placement="right" id="LEVEL_NAME" NAME="LEVEL_NAME" <%=disabledInView%> >
      <OPTION selected value=''> -----Choose User Access Rights Group----- </OPTION>
		<%
		  if (plant1.equalsIgnoreCase("track")) {
			%>
		<%=sl.getUserLevels("1")%>
			<%
			} else {
			%>
		<%=sl.getUserLevels("1", plant, "DefaultGroup','MaintenanceGroup")%>
			<%
			}
			%>
		</SELECT>
		   </div>
		     
  <div class=form-inline>
   <div class="col-sm-2">
   <button type="submit" class="Submit btn btn-default" value="Go" <%=disabledInView%>>View</button>&nbsp;&nbsp;
   <!-- <button type="button" class="Submit btn btn-default" onClick="window.location.href='../home'"><b>Back</b></button>&nbsp;&nbsp; -->
   <!-- <button type="button" class="Submit btn btn-default" onClick="{backNavigation('settings.jsp','UA');}"><b>Back</b></button>&nbsp;&nbsp; -->
    </div>
     </div>
     </div>
    
    <br>
    <%
		String level_name = request.getParameter("LEVEL_NAME");
    	if (level_name == null){
    		response.sendRedirect("../useraccess/invedit?LEVEL_NAME");
    		return;
    	}

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
    
   </form>
   
     
   
   
  <form class="form-horizontal" method="POST" name="levelform" action="../jsp/userLevelSubmit.jsp">
   <INPUT type="Hidden" name="LEVEL_NAME" value="<%=level_name%>">
	<P align="center"><font face="Verdana" color="black" size="2"> <b>GROUPS</b></font><font face="Verdana" color="black" size="2"><b> - <%=new String(level_name).toUpperCase()%></b></font></P>
	 <div class="row">
  		<div class="col-12 col-sm-12"><INPUT Type=Checkbox class="form-check-input"  style="border:0;"  onclick="return checkAll(this.checked);">
                     <strong>   &nbsp; Select/Unselect All </strong> </div>
  </div>
	  <div class="row">
  		<div class="col-12 col-sm-12"><INPUT Type=Checkbox class="form-check-input"  style="border:0;" name="Home"  onclick="return checkAllHome(this.checked);">
                      &nbsp; Select/Unselect Dashboard </div>
  </div>
	
	 <div class="panel panel-default">
    <div class="panel-heading" style="background: #eaeafa"><strong>Dashboard</strong></div>
    <div class="panel-body">
    <TABLE class="table1">
                <TR>
                   <%-- <TH WIDTH="20%" ALIGN = "LEFT">
                  <INPUT Type=Checkbox  style="border:0;" name="Home" value="homepurchasetotal" <%=disabled%> <%=checkOption("homepurchasetotal")%>>
                  &nbsp; Total Purchase
                  
                   <TH WIDTH="20%" ALIGN = "LEFT">
                  <INPUT Type=Checkbox  style="border:0;" name="Home" value="homesaletotal" <%=disabled%> <%=checkOption("homesaletotal")%>>
                  &nbsp; Total Sales
                                  
                  <TH WIDTH="20%" ALIGN = "LEFT">
                  <INPUT Type=Checkbox  style="border:0;" name="Home" value="homeallactivity" <%=disabled%> <%=checkOption("homeallactivity")%>>
                  &nbsp; See All Activity
                  
                   <TH WIDTH="20%" ALIGN = "LEFT">
                  <INPUT Type=Checkbox  style="border:0;" name="Home" value="homereceivedproductgrid" <%=disabled%> <%=checkOption("homereceivedproductgrid")%> >
                  &nbsp; Top Received Products 
                  <TR>
                   <TH WIDTH="20%" ALIGN = "LEFT">
                  <INPUT Type=Checkbox  style="border:0;" name="Home" value="homeissuedproductgrid" <%=disabled%> <%=checkOption("homeissuedproductgrid")%> >
                  &nbsp; Top Issued Products 
                  
                  <TH WIDTH="20%" ALIGN = "LEFT">
                  <INPUT Type=Checkbox  style="border:0;" name="Home" value="homeexpiringproductgrid" <%=disabled%> <%=checkOption("homeexpiringproductgrid")%> >
                  &nbsp; Expiring Products 
                  
                     <TH WIDTH="20%" ALIGN = "LEFT">
                  <INPUT Type=Checkbox  style="border:0;" name="Home" value="homestockproductgrid" <%=disabled%> <%=checkOption("homestockproductgrid")%> >
                  &nbsp; Stock Replenishment Products --%>
                  
                 <%--  <TH WIDTH="20%" ALIGN = "LEFT">
                 <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;" name="Home" value="homeaccountingmanaget" <%=disabled%> <%=checkOption("homeaccountingmanaget")%>>
                  &nbsp; Accounting Management</label>
                  
                   <TH WIDTH="20%" ALIGN = "LEFT">
                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="Home" value="homeaccounting" <%=disabled%> <%=checkOption("homeaccounting")%>>
                  &nbsp; Accounting</label> --%>
                                  
                  <TH WIDTH="20%" ALIGN = "LEFT">
                 <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;" name="Home" value="homepurchase" <%=disabled%> <%=checkOption("homepurchase")%>>
                  &nbsp; Purchase</label>
                  
                   <TH WIDTH="20%" ALIGN = "LEFT">
                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="Home" value="homesales" <%=disabled%> <%=checkOption("homesales")%> >
                  &nbsp; Sales </label>

                   <TH WIDTH="20%" ALIGN = "LEFT">
                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="Home" value="homewarehouse" <%=disabled%> <%=checkOption("homewarehouse")%> >
                  &nbsp; Warehouse </label>
                  
                  <% if(COMP_INDUSTRY.equals("Retail")) { %> 
                   <th WIDTH="20%" ALIGN = "LEFT">
                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="Home" value="homepos" <%=disabled%> <%=checkOption("homepos")%>>
                  &nbsp;POS</label>
                  <%} %>
                    
                      </TABLE>
         </div>
  </div>
	 <div class="row">
  		<div class="col-12 col-sm-12"><INPUT Type=Checkbox class="form-check-input"  style="border:0;" name="UserAdmin"  onclick="return checkAllUserAdmin(this.checked);">
                     &nbsp; Select/Unselect User Admin  </div>
  </div>
	<div class="panel panel-default">
    <div class="panel-heading" style="background: #eaeafa"><strong>User Admin</strong></div>
    <div class="panel-body">
    <TABLE class="table1" style="font-size:14px;width: 100%;">
                 <TR>
                    <%if(plant1.equalsIgnoreCase("track")){%>
                     <TH WIDTH="20%" ALIGN = "LEFT">
                     <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;background=#dddddd" name="UserAdmin" value="companySummry" <%=disabled%> <%=checkOption("companySummry")%>>
                      &nbsp; Summary &#45; Company Details</label>
                      
                      <TH WIDTH="20%" ALIGN = "LEFT">
                      <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;background=#dddddd" name="UserAdmin" value="uaMaintCpny" <%=disabled%> <%=checkOption("uaMaintCpny")%>>
                       &nbsp; Edit Your Company Profile</label>
                      
                      <TH WIDTH="20%" ALIGN = "LEFT">
                      <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;background=#dddddd" name="UserAdmin" value="uaCreateCpny" <%=disabled%> <%=checkOption("uaCreateCpny")%>>
                       &nbsp; Create Company Details</label>
                      
                       <TH WIDTH="20%" ALIGN = "LEFT">
                       <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;background=#dddddd" name="UserAdmin" value="uaMaintAcct" <%=disabled%> <%=checkOption("uaMaintAcct")%>>
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
                    <%} %>
                        --%>
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
                       &nbsp;  User Details</label>
                        
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
                     <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;background=#dddddd" name="UserAdmin" value="uaNewLevel" <%=disabled%> <%=checkOption("uaNewLevel")%>>
                      &nbsp; Create User Details</label>
                     
                     <% if(MANAGEWORKFLOW1.equals("0")) {%>
                       <% if(COMP_INDUSTRY.equals("Retail")) { %> 
                        <TR>
                        <%} }%>
                      
                     <TH WIDTH="20%" ALIGN = "LEFT">
                    <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;background=#dddddd" name="UserAdmin" value="uaauthcmpy" <%=disabled%> <%=checkOption("uaauthcmpy")%>>&nbsp;Authorise Company Details<%}%></label>      
                    </TABLE>
                 </div>
        </div>   
        
         <div class="row">
  		<div class="col-12 col-sm-12"><INPUT Type=Checkbox class="form-check-input"  style="border:0;" name="SystemMaster"  onclick="return checkAllSystemMaster(this.checked);">
                     &nbsp; Select/Unselect Product </div>
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
        
        <div class="row">
  		<div class="col-12 col-sm-12"><INPUT Type=Checkbox class="form-check-input"  style="border:0;" name="UomLoc"  onclick="return checkAllUomLoc(this.checked);">
                        &nbsp; Select/Unselect UOM / Location / Reason Code</div>
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
                    <div class="row">
  		<div class="col-12 col-sm-12"><INPUT Type=Checkbox class="form-check-input"  style="border:0;" name="Supplier"  onclick="return checkAllSupplier(this.checked);">
                       &nbsp; Select/Unselect Supplier  </div>
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
        
        <div class="row">
  		<div class="col-12 col-sm-12"><INPUT Type=Checkbox class="form-check-input"  style="border:0;" name="Customer"  onclick="return checkAllCustomer(this.checked);">
                       &nbsp; Select/Unselect Customer </div>
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
        
         <div class="row">
  		<div class="col-12 col-sm-12"><INPUT Type=Checkbox class="form-check-input"  style="border:0;" name="UserEmployee"  onclick="return checkAllUserEmployee(this.checked);">
                        &nbsp; Select/Unselect Employee</div>
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
 
 <div class="row">
  		<div class="col-12 col-sm-12"><INPUT Type=Checkbox class="form-check-input"  style="border:0;" name="SystemAdmin"  onclick="return checkAllSystemAdmin(this.checked);">
                       &nbsp; Select/Unselect System Admin  </div>
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
        
        <div class="row">
  		<div class="col-12 col-sm-12"><INPUT Type=Checkbox class="form-check-input"  style="border:0;" name="OrderAdmin"  onclick="return checkAllOrderAdmin(this.checked);">
                      &nbsp; Select/Unselect Order Admin </div>
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
 
 <div class="row">
  		<div class="col-12 col-sm-12"><INPUT Type=Checkbox class="form-check-input"  style="border:0;" name="OrderAdminHS"  onclick="return checkAllOrderAdminHS(this.checked);">
                      &nbsp; Select/Unselect HSCODE / COO / Remarks / INCOTERM / Footer  </div>
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
        
         <div class="row">
<div class="col-12 col-sm-12"><INPUT Type=Checkbox class="form-check-input"  style="border:0;" name="Distribution"  onclick="return checkAllDistribution(this.checked);">
                      &nbsp; Select/Unselect Distribution </div>
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
                <div class="row">
  		<div class="col-12 col-sm-12"><INPUT Type=Checkbox class="form-check-input"  style="border:0;" name="OrderConfiguration"  onclick="return checkAllOrderConfiguration(this.checked);">
                       &nbsp; Select/Unselect Printout Configuration </div>
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
                    <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="OrderConfiguration" value="editibpt"  <%=disabled%> <%=checkOption("editibpt")%>>
                    &nbsp; Edit Purchase Order Printout </label>           
                 
       				 <TH WIDTH="23%" ALIGN = "LEFT">
                   <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;" name="OrderConfiguration" value="editibwithcostpt" <%=disabled%> <%=checkOption("editibwithcostpt")%>>
                    &nbsp; Edit Purchase Order Printout (with cost)</label>
                    
                    <TH WIDTH="23%" ALIGN = "LEFT">
                   <label class="checkbox-inline">  <INPUT Type=Checkbox  style="border:0;" name="OrderConfiguration" value="editInboundOrderMailMsg"  <%=disabled%> <%=checkOption("editInboundOrderMailMsg")%>>
                     &nbsp; Edit Purchase Order Email Message</label>
                     
                    <TR>
                    
                    <TH WIDTH="23%" ALIGN = "LEFT">
                   <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;" name="OrderConfiguration" value="editBillPrintout" <%=disabled%> <%=checkOption("editBillPrintout")%>>
                    &nbsp; Edit Bill Printout</label>
                    
                     <TH WIDTH="23%" ALIGN = "LEFT">
                    <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="OrderConfiguration" value="editestwithpt" <%=disabled%> <%=checkOption("editestwithpt")%>>
                    &nbsp; Edit Sales Estimate Order Printout (with price)</label>
                    
                    <TH WIDTH="23%" ALIGN = "LEFT">
                    <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="OrderConfiguration" value="editEstimateOrderMailMsg"  <%=disabled%> <%=checkOption("editEstimateOrderMailMsg")%>>
                    &nbsp; Edit Sales Estimate Order Email Message</label>
                   
                    <TH WIDTH="20%" ALIGN = "LEFT">
                   <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;" name="OrderConfiguration" value="editobpt" <%=disabled%> <%=checkOption("editobpt")%>>
                    &nbsp; Edit Sales Order Printout</label>
                     
                    <TR>
                  
                       <TH WIDTH="23%" ALIGN = "LEFT">
                   <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;" name="OrderConfiguration" value="editobwithpt" <%=disabled%> <%=checkOption("editobwithpt")%>>
                    &nbsp; Edit Sales Order Printout (with price)</label>
               
                    <TH WIDTH="23%" ALIGN = "LEFT">
                   <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;" name="OrderConfiguration" value="editOutboundOrderMailMsg"  <%=disabled%> <%=checkOption("editOutboundOrderMailMsg")%>>
                    &nbsp; Edit Sales Order Email Message</label>
                    
                      <TH WIDTH="20%" ALIGN = "LEFT">
                   <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;" name="OrderConfiguration" value="edittowop" <%=disabled%> <%=checkOption("edittowop")%>>
                    &nbsp; Edit Consignment Order Printout</label>
                    
                    
                      <TH WIDTH="20%" ALIGN = "LEFT">
                   <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;" name="OrderConfiguration" value="edittopt" <%=disabled%> <%=checkOption("edittopt")%>>
                    &nbsp; Edit Consignment Order Printout (With Price)</label>
                     
                    <TR>
                    

                    <TH WIDTH="20%" ALIGN = "LEFT">
                    <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="OrderConfiguration" value="editTransferOrderMailMsg"  <%=disabled%> <%=checkOption("editTransferOrderMailMsg")%>>
                    &nbsp; Edit Consignment Order Email Message</label>
                   
                    <%-- <TH WIDTH="20%" ALIGN = "LEFT">
                    <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="OrderConfiguration" value="editlopt" <%=disabled%> <%=checkOption("editlopt")%>>
                    &nbsp; Edit Rental Order Printout</label>
               
                    <TH WIDTH="20%" ALIGN = "LEFT">
                   <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;" name="OrderConfiguration" value="edittopt" <%=disabled%> <%=checkOption("edittopt")%>>
                    &nbsp; Edit Consignment Order Printout</label>

                    <TH WIDTH="20%" ALIGN = "LEFT">
                    <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="OrderConfiguration" value="editTransferOrderMailMsg"  <%=disabled%> <%=checkOption("editTransferOrderMailMsg")%>>
                    &nbsp; Edit Consignment Order Email Message</label> --%>
                  
                    <TH WIDTH="20%" ALIGN = "LEFT">
                    <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="OrderConfiguration" value="editTaxInvoiceMultiLanguage"  <%=disabled%> <%=checkOption("editTaxInvoiceMultiLanguage")%>>
                    &nbsp; Edit Invoice Printout</label>
                    
                    <TH WIDTH="20%" ALIGN = "LEFT">
                    <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="OrderConfiguration" value="editRcptRecvHdr" <%=disabled%> <%=checkOption("editRcptRecvHdr")%>>
                    &nbsp; Edit Goods Receipt Printout</label>
                                   
                    <TH WIDTH="20%" ALIGN = "LEFT">
                    <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="OrderConfiguration" value="editRcptIssueHdr" <%=disabled%> <%=checkOption("editRcptIssueHdr")%>>
                    &nbsp; Edit Goods Issue Printout</label>
                     
                    <TR>
                  
                  
                    <TH WIDTH="20%" ALIGN = "LEFT">
                   <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;" name="OrderConfiguration" value="editMoveHdr" <%=disabled%> <%=checkOption("editMoveHdr")%>>
                    &nbsp; Edit Stock Move Printout </label>
                      
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
                   
                   <%-- <div class="row">
  		<div class="col-12 col-sm-12"><INPUT Type=Checkbox class="form-check-input"  style="border:0;" name="EmailConfiguration"  onclick="return checkAllEmailConfiguration(this.checked);">
                     <strong>   &nbsp; Select/Unselect Approval/Email Configuration</strong> </div>
  </div> 
<div class="panel panel-default">
<div class="panel-heading" style="background: #eaeafa"><strong>Approval/Email Configuration</strong></div>
<div class="panel-body"> 
		<TABLE class="table1" style="font-size:14px;width: 100%;">
        	 <TR>
        	 <TH WIDTH="20%" ALIGN = "LEFT">
                      <INPUT Type=Checkbox  style="border:0;" name="EmailConfiguration" value="editemailconfig" <%=disabled%> <%=checkOption("editemailconfig")%>>
                      &nbsp; Order Management Approval & Email Configuration
        	 </TABLE>
              </div>
        </div> --%>
                    
                              <div class="row">
  		<div class="col-12 col-sm-12"><INPUT Type=Checkbox class="form-check-input"  style="border:0;" name="OrderManagement"  onclick="return checkAllOrderManagement(this.checked);">
                        &nbsp; Select/Unselect </div>
  </div> 
                   <div class="panel panel-default">
    <div class="panel-heading" style="background: #eaeafa"><strong>Order Management - Create Masters</strong></div>
    <div class="panel-body"> 
    	<TABLE class="table1" style="font-size:14px;width: 100%;">
    	   	
                    <TR>
                   
                    <TH WIDTH="20%" ALIGN = "LEFT">
                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="OrderManagement" value="popproduct" <%=disabled%> <%=checkOption("popproduct")%> > 
                  &nbsp; Create Product Details &#45; Purchase, Bill, Sales Estimate, Sales,Invoice</label>
                   
                    <TH WIDTH="20%" ALIGN = "LEFT">
                 <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;" name="OrderManagement" value="popsupplier" <%=disabled%> <%=checkOption("popsupplier")%> >
                  &nbsp; Create Supplier Details &#45; Purchase Order</label>
                  
                   <TH WIDTH="20%" ALIGN = "LEFT">
                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="OrderManagement" value="suppliertypepopup" <%=disabled%> <%=checkOption("suppliertypepopup")%>>
                  &nbsp; Create Supplier Type - Purchase Order</label>
                  
                  <TH WIDTH="20%" ALIGN = "LEFT">
                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="OrderManagement" value="popcustomer" <%=disabled%> <%=checkOption("popcustomer")%> >
                  &nbsp; Create Customer Details &#45; Sales Estimate, Sales,Invoice</label>
                
                 <TR> 
                 <TH WIDTH="20%" ALIGN = "LEFT">
                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="OrderManagement" value="customertypepopup" <%=disabled%> <%=checkOption("customertypepopup")%>>
                  &nbsp;Create Customer Type - Sales Estimate, Sales,Invoice</label>
                 
				  <TH WIDTH="20%" ALIGN = "LEFT">
                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="OrderManagement" value="ordertypepopup" <%=disabled%> <%=checkOption("ordertypepopup")%>>
                  &nbsp;Create Order Type - Purchase, Bill, Sales Estimate, Sales,Invoice</label>
				                    
                    <TH WIDTH="20%" ALIGN = "LEFT">
                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="OrderManagement" value="paymenttypepopup" <%=disabled%> <%=checkOption("paymenttypepopup")%>>
                  &nbsp;Payment Type - Purchase, Bill, Sales Estimate, Sales,Invoice</label>
                    
             </TABLE>
    </div>
</div> 

<div class="row">
  		<div class="col-12 col-sm-12"><INPUT Type=Checkbox class="form-check-input"  style="border:0;" name="Project"  onclick="return checkAllProject(this.checked);">
                        &nbsp; Select/Unselect Project</div>
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

<div class="row">
  		<div class="col-12 col-sm-12"><INPUT Type=Checkbox class="form-check-input"  style="border:0;" name="Purchaseestimate"  onclick="return checkAllPurchaseestimate(this.checked);">
                        &nbsp; Select/Unselect Purchase Estimate</div>
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
                   <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;" name="Purchaseestimate" value="summarypurchaseorderestimate" <%=disabled%> <%=checkOption("summarypurchaseorderestimate")%>>
                    &nbsp; Purchase  Estimate</label>
                    
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
        

<div class="row">
  		<div class="col-12 col-sm-12"><INPUT Type=Checkbox class="form-check-input"  style="border:0;" name="OrderManagement1"  onclick="return checkAllOrderManagement1(this.checked);">
                        &nbsp; Select/Unselect Purchase Order</div>
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
                    &nbsp; Summary &#45; Purchase Order Details </label>
                    
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
        
                           <div class="row">
  		<div class="col-12 col-sm-12"><INPUT Type=Checkbox class="form-check-input"  style="border:0;" name="PurchaseTransaction"  onclick="return checkAllPurchaseTransaction(this.checked);">
                        &nbsp; Select/Unselect Purchase Transaction</div>
  </div>               
<div class="panel panel-default">
<div class="panel-heading" style="background: #eaeafa"><strong>Purchase Transaction</strong></div>
<div class="panel-body"> 
             <TABLE class="table1" style="font-size:14px;width: 100%;">
             <TR>
             
             
              <TH WIDTH="20%" ALIGN = "LEFT">
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


<div class="row">
 <div class="col-12 col-sm-12"><INPUT Type=Checkbox class="form-check-input"  style="border:0;" name="PurchaseReturn"  onclick="return checkAllPurchaseReturn(this.checked);">
                        &nbsp; Select/Unselect Purchase Return </div>
  </div>
 <div class="panel panel-default">
<div class="panel-heading" style="background: #eaeafa"><strong>Purchase Return</strong></div>
<div class="panel-body">
       <TABLE class="table1" style="font-size:14px;width: 100%;">
                    <TR>
                    
 					<TH WIDTH="20%" ALIGN = "LEFT">
	                 <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="PurchaseReturn" value="POReturnSummary"   <%=disabled%> <%=checkOption("POReturnSummary")%> >     
	                 &nbsp; Purchase Return</label>
	                 
	                <TH WIDTH="20%" ALIGN = "LEFT">
	                 <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="PurchaseReturn" value="newpurchasereturn"  <%=disabled%> <%=checkOption("newpurchasereturn")%> >     
	                 &nbsp; New</label>
	                 
	                <TH WIDTH="20%" ALIGN = "LEFT">
	                 <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="PurchaseReturn" value="summarylnkpurchasereturn" <%=disabled%> <%=checkOption("summarylnkpurchasereturn")%> >
	                 &nbsp; Summary &#45; Return Number Link</label>
	                 
	                 <TH WIDTH="20%" ALIGN = "LEFT">
	                 <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="PurchaseReturn" value="exportpurchasereturn"  <%=disabled%> <%=checkOption("exportpurchasereturn")%> >     
	                 &nbsp; Export</label>
	                 
	                
	                 
	                 <TR>
	                 
	                 <TH WIDTH="20%" ALIGN = "LEFT">
	                 <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="PurchaseReturn" value="excelpurchasereturn"  <%=disabled%> <%=checkOption("excelpurchasereturn")%> >     
	                 &nbsp; Export Excel</label>
	                 
	                <TH WIDTH="20%" ALIGN = "LEFT">
	                 <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="PurchaseReturn" value="applycreditnotepurchasereturn"  <%=disabled%> <%=checkOption("applycreditnotepurchasereturn")%> >     
	                 &nbsp; Apply To Debit Note</label>
	                 
		</TABLE>
     </div>
 </div>
 
 <div class="row">
 <div class="col-12 col-sm-12"><INPUT Type=Checkbox class="form-check-input"  style="border:0;" name="ProductReturn"  onclick="return checkAllProductReturn(this.checked);">
                        &nbsp; Select/Unselect Internal Product Return </div>
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
 
<div class="row">
        <div class="col-12 col-sm-12"><INPUT Type=Checkbox class="form-check-input"  style="border:0;" name="PurchaseReports"  onclick="return checkAllPurchaseReports(this.checked);">
                        &nbsp; Select/Unselect Purchase Reports </div>
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
        
                           <div class="row">
  		<div class="col-12 col-sm-12"><INPUT Type=Checkbox class="form-check-input"  style="border:0;" name="SalesEstimate"  onclick="return checkAllSalesEstimate(this.checked);">
                       &nbsp; Select/Unselect Sales Estimate </div>
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

<div class="row">
  		<div class="col-12 col-sm-12"><INPUT Type=Checkbox class="form-check-input"  style="border:0;" name="SalesEstimateReports"  onclick="return checkAllSalesEstimateReports(this.checked);">
                       &nbsp; Select/Unselect Sales Estimate Reports</div>
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

                  
                    
                  <div class="row">
  		<div class="col-12 col-sm-12"><INPUT Type=Checkbox class="form-check-input"  style="border:0;" name="SalesOrder"  onclick="return checkAllSalesOrder(this.checked);">
                       &nbsp; Select/Unselect Sales Order </div>
  </div>  
<div class="panel panel-default">
<div class="panel-heading" style="background: #eaeafa"><strong>Sales Order</strong></div>
<div class="panel-body">
       <TABLE class="table1" style="font-size:14px;width: 100%;">
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
                  
                  <div class="row">
  		<div class="col-12 col-sm-12"><INPUT Type=Checkbox class="form-check-input"  style="border:0;" name="SalesTransaction"  onclick="return checkAllSalesTransaction(this.checked);">
                       &nbsp; Select/Unselect SalesTransaction </div>
  </div>  
<div class="panel panel-default">
<div class="panel-heading" style="background: #eaeafa"><strong>Sales Transaction</strong></div>
<div class="panel-body">
       <TABLE class="table1" style="font-size:14px;width: 100%;">
                    <TR>
			   
			            <TH WIDTH="20%" ALIGN = "LEFT">
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
		                     
		                   
		                    
		                     <TH WIDTH="20%" ALIGN = "LEFT">
                        	<label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SalesTransaction" value="packingSummary" <%=disabled%> <%=checkOption("packingSummary")%> >
                        	&nbsp;  Packing List and Deliver Note </label>
                        	</TH>
	                               
                        </TABLE>
                        </div>
                      </div>
                      
                      
                      <div class="row">
  		<div class="col-12 col-sm-12"><INPUT Type=Checkbox class="form-check-input"  style="border:0;" name="SalesReturn"  onclick="return checkAllSalesReturn(this.checked);">
                        &nbsp; Select/Unselect Sales Return </div>
  </div> 
<div class="panel panel-default">
<div class="panel-heading" style="background: #eaeafa"><strong>Sales Return</strong></div>
<div class="panel-body"> 
		<TABLE class="table1" style="font-size:14px;width: 100%;">
        	 <TR>
        	 
        	 
 					<TH WIDTH="20%" ALIGN = "LEFT">
	                 <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SalesReturn" value="SOReturnSummary"  <%=disabled%> <%=checkOption("SOReturnSummary")%> >     
	                 &nbsp; Sales Return</label>
	                 
	                <TH WIDTH="20%" ALIGN = "LEFT">
	                 <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SalesReturn" value="newsalesreturn"  <%=disabled%> <%=checkOption("newsalesreturn")%> >     
	                 &nbsp; New</label>
	                 
	                <TH WIDTH="20%" ALIGN = "LEFT">
	                 <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SalesReturn" value="summarylnksalesreturn" <%=disabled%> <%=checkOption("summarylnksalesreturn")%> >
	                 &nbsp; Summary &#45; Return Number Link</label>
	                 
	                   <TH WIDTH="20%" ALIGN = "LEFT">
	                 <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SalesReturn" value="exportsalesreturn"  <%=disabled%> <%=checkOption("exportsalesreturn")%> >     
	                 &nbsp; Export</label>
	                 
	                
	                 <TR>
	                 
	                 <TH WIDTH="20%" ALIGN = "LEFT">
	                 <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SalesReturn" value="excelsalesreturn"  <%=disabled%> <%=checkOption("excelsalesreturn")%> >     
	                 &nbsp; Export Excel</label>
	                 
	                 
	                <TH WIDTH="20%" ALIGN = "LEFT">
	                 <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SalesReturn" value="applycreditnotesalesreturn"  <%=disabled%> <%=checkOption("applycreditnotesalesreturn")%> >     
	                 &nbsp; Apply To Credit Note</label>
        	 
        	          	
              </TABLE>
    </div>
</div>

<div class="row">
 <div class="col-12 col-sm-12"><INPUT Type=Checkbox class="form-check-input"  style="border:0;" name="ProductReturnReceive"  onclick="return checkAllProductReturnReceive(this.checked);">
                        &nbsp; Select/Unselect Internal Product Return Receive</div>
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
 
                      
                  
                  <div class="row">
  		<div class="col-12 col-sm-12"><INPUT Type=Checkbox class="form-check-input"  style="border:0;" name="SalesReports"  onclick="return checkAllSalesReports(this.checked);">
                        &nbsp; Select/Unselect Sales Reports </div>
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
                       		</TH>
                       		 --%>
                        	<TH WIDTH="20%" ALIGN = "LEFT">
                        	<label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SalesReports" value="issuedobsummry" <%=disabled%> <%=checkOption("issuedobsummry")%> >
                        	&nbsp; Sales Order Summary(by price)</label>
                           </TH>
                             
                             <TH WIDTH="20%" ALIGN = "LEFT">
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
                      
                 <div class="row">
  					<div class="col-12 col-sm-12"><INPUT Type=Checkbox class="form-check-input"  style="border:0;" name="Consignment"  onclick="return checkAllConsignment(this.checked);">
                        &nbsp; Select/Unselect Consignment </div>
  				</div>       
         <div class="panel panel-default">
    <div class="panel-heading" style="background: #eaeafa"><strong>Consignment</strong></div>
    <div class="panel-body">
    <TABLE class="table1">
    
    				<TR>
    				
    				<TH WIDTH="20%" ALIGN = "LEFT">
                    <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="Consignment" value="summaryconsignment" <%=disabled%> <%=checkOption("summaryconsignment")%>>
                    &nbsp; Consignment</label>
	                 
                    <TH WIDTH="20%" ALIGN = "LEFT">
                     <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="Consignment" value="newconsignment" <%=disabled%> <%=checkOption("newconsignment")%>>
                     &nbsp; New</label>
                     
                    <TH WIDTH="20%" ALIGN = "LEFT">
	                 <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="Consignment" value="summarylnkconsignment" <%=disabled%> <%=checkOption("summarylnkconsignment")%> >
	                 &nbsp; Summary &#45; Consignment NumberLink</label>
                     
                     <TH WIDTH="20%" ALIGN = "LEFT">
                     <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="Consignment" value="exportforconsignment" <%=disabled%> <%=checkOption("exportforconsignment")%>>
                     &nbsp; Export</label>
                                             
                     <TR>
                    
                     <TH WIDTH="20%" ALIGN = "LEFT">
                     <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="Consignment" value="editconsignment" <%=disabled%> <%=checkOption("editconsignment")%>>
                     &nbsp; Edit</label>
                         
                    <TH WIDTH="20%" ALIGN = "LEFT">
                     <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="Consignment" value="printconsignment" <%=disabled%> <%=checkOption("printconsignment")%>>
                     &nbsp; PDF/Print</label> 
                       
                    <TH WIDTH="20%" ALIGN = "LEFT">
                     <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="Consignment" value="moreconsignment" <%=disabled%> <%=checkOption("moreconsignment")%>>
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
                      
       <div class="row">
  	<div class="col-12 col-sm-12"><INPUT Type=Checkbox class="form-check-input"  style="border:0;" name="RentalTransaction"  onclick="return checkAllRentalTransaction(this.checked);">
        &nbsp; Select/Unselect Consignment Transaction</div>
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
                     &nbsp;  Consignment Order Reversal</label>
                     
                   
    				
    </TABLE>
    </div>
 </div> 
 
  			<div class="row">
  			<div class="col-12 col-sm-12"><INPUT Type=Checkbox class="form-check-input"  style="border:0;" name="ConsignmentReports"  onclick="return checkAllConsignmentReports(this.checked);">
   		   &nbsp; Select/Unselect Consignment Reports </div>
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
                   
                   
                              
                      
                      <%-- <div class="row">
  		<div class="col-12 col-sm-12"><INPUT Type=Checkbox class="form-check-input"  style="border:0;" name="RentalTransaction"  onclick="return checkAllRentalTransaction(this.checked);">
                       &nbsp; Select/Unselect Rental / Consignment </div>
  </div>   
<div class="panel panel-default">
<div class="panel-heading" style="background: #eaeafa"><strong>Rental / Consignment</strong></div>
<div class="panel-body">
      <TABLE class="table1" style="font-size:14px;width: 100%;">
      
      <TR>
					
                    <TH WIDTH="20%" ALIGN = "LEFT">
                   <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;" name="RentalTransaction" value="lnordsummry" <%=disabled%> <%=checkOption("lnordsummry")%>>
                    &nbsp; Summary &#45; Rental Order Details</label>
                    
                    <TH WIDTH="20%" ALIGN = "LEFT">
                    <INPUT Type=Checkbox  style="border:0;" name="RentalTransaction" value="loanOrderSummaryWithPrice" <%=disabled%> <%=checkOption("loanOrderSummaryWithPrice")%>>
                    &nbsp; Summary &#45; Rental Order With Price Details                  
                  
                    <TH WIDTH="20%" ALIGN = "LEFT"> 
                    <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="RentalTransaction" value="maintlnord" <%=disabled%> <%=checkOption("maintlnord")%>>
                    &nbsp;  Edit Rental Order Details </label>
                               
                    <TH WIDTH="20%" ALIGN = "LEFT">
                   <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;" name="RentalTransaction" value="cratlnord" <%=disabled%> <%=checkOption("cratlnord")%>>
                    &nbsp; Create Rental Order Details</label>
					
					<TH WIDTH="20%" ALIGN = "LEFT">
                    <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="RentalTransaction" value="orderCloseRental" <%=disabled%> <%=checkOption("orderCloseRental")%>>
                    &nbsp; Close Outstanding Rental Orders</label>
                    
                    <TR>					
                                        
                    <TH WIDTH="20%" ALIGN = "LEFT">
                    <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="RentalTransaction" value="trordsummry" <%=disabled%> <%=checkOption("trordsummry")%>>
                    &nbsp; Summary &#45; Consignment Order Details</label>
                    
                    <TH WIDTH="20%" ALIGN = "LEFT">  
                    <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="RentalTransaction" value="mainttrord" <%=disabled%> <%=checkOption("mainttrord")%>>
                    &nbsp; Edit Consignment Order Details</label>                    
                  
                    <TH WIDTH="20%" ALIGN = "LEFT">
                    <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="RentalTransaction" value="crattrord" <%=disabled%> <%=checkOption("crattrord")%>>
                    &nbsp; Create Consignment Order Details</label>
					
					<TH WIDTH="20%" ALIGN = "LEFT">
                    <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="RentalTransaction" value="orderCloseTransfer" <%=disabled%> <%=checkOption("orderCloseTransfer")%>>
                    &nbsp; Close Outstanding Consignment Orders</label>
                    
                    <TR>					

					<TH WIDTH="20%" ALIGN = "LEFT">
                    <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="RentalTransaction" value="lnordReceiving"   <%=disabled%> <%=checkOption("lnordReceiving")%>>
                    &nbsp;Rental Order Receipt</label>                     
		                   
		                    <TH WIDTH="20%" ALIGN = "LEFT">
		                    <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="RentalTransaction" value="lnordPicking"  <%=disabled%> <%=checkOption("lnordPicking")%>>
		                    &nbsp;  Rental Order Pick & Issue</label>                         
		                 
		                    <TH WIDTH="20%" ALIGN = "LEFT">
		                    <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="RentalTransaction" value="bulkPickReceiveTO" <%=disabled%> <%=checkOption("bulkPickReceiveTO")%>>
		                    &nbsp;  Consignment Order Pick & Issue</label>
		               		                  		                  
	                 	    <TH WIDTH="20%" ALIGN = "LEFT">
	                      	<label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="RentalTransaction" value="toreversal" <%=disabled%> <%=checkOption("toreversal")%> >
	                      	&nbsp;  Consignment Order Reversal</label>
		                    
		                    <TR>	                      	
	                      	
							<TH WIDTH="20%" ALIGN = "LEFT">
	                          <INPUT Type=Checkbox  style="border:0;" name="RentalTransaction" value="LoanOrderSummaryByPrice" <%=disabled%> <%=checkOption("LoanOrderSummaryByPrice")%> >
	                          &nbsp;  Rental Order Details Summary With Price
	                          </TH> 
							  
							  <TH WIDTH="20%" ALIGN = "LEFT">
	                          <INPUT Type=Checkbox  style="border:0;" name="RentalTransaction" value="RentalOrderSummary" <%=disabled%> <%=checkOption("RentalOrderSummary")%> >
	                          &nbsp;  Rental Order Details Summary 
	                          </TH>
							
	                      	  <TH WIDTH="20%" ALIGN = "LEFT">
	                          <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="RentalTransaction" value="printloanorder" <%=disabled%> <%=checkOption("printloanorder")%> >
	                          &nbsp;  Rental Order Details</label>
	                          </TH>
							  
							  <TH WIDTH="20%" ALIGN = "LEFT">
	                          <INPUT Type=Checkbox  style="border:0;" name="RentalTransaction" value="printLoanOrderInvoice" <%=disabled%> <%=checkOption("printLoanOrderInvoice")%> >
	                          &nbsp;  Rental Order Details Summary By Price
	                          </TH>	                          
                                                      
                           <TH WIDTH="20%" ALIGN = "LEFT">
                        	<label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="RentalTransaction"value="printto"  <%=disabled%> <%=checkOption("printto")%> >
                        	&nbsp;   Consignment Order Details</label>
                           </TH>
							
	              	 	 </TR>
      
      </TABLE>
                    </div>
                     </div> --%>
                                     
                                       <div class="row">
  		<div class="col-12 col-sm-12"><INPUT Type=Checkbox class="form-check-input"  style="border:0;" name="InHouse"  onclick="return checkAllInHouse(this.checked);">
                       &nbsp; Select/Unselect In House </div>
  </div>   
<div class="panel panel-default">
<div class="panel-heading" style="background: #eaeafa"><strong>In House</strong></div>
<div class="panel-body">
      <TABLE class="table1" style="font-size:14px;width: 100%;">
                 
                 <TR>                   
                        
                         <TH WIDTH="20%" ALIGN = "LEFT">
                         <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="InHouse" value="mulloctransfer" <%=disabled%> <%=checkOption("mulloctransfer")%>>  
                         &nbsp; Stock Move</label>
                         
                             <%-- NAME: Thansith --%>                      
                          
                          <TH WIDTH="20%" ALIGN = "LEFT">
                         <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="InHouse" value="newProcessingReceive" <%=disabled%> <%=checkOption("newProcessingReceive")%>>  
                         &nbsp; De-Kitting</label>
                         
                           <TH WIDTH="20%" ALIGN = "LEFT">
                       	   <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="InHouse" value="summarySemiFinished" <%=disabled%> <%=checkOption("summarySemiFinished")%>>  
                           &nbsp; Kitting</label>
                                                  
                         <%--   END --%>
                         
                            <TH WIDTH="20%" ALIGN = "LEFT">
                        	<label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="InHouse" value="manualstocktake"   <%=disabled%> <%=checkOption("manualstocktake")%> >
                        	&nbsp; Stock Take</label> </TH>
                        	
                        	<TR>
                        	
                        	<TH WIDTH="20%" ALIGN = "LEFT">
                        	<label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="InHouse" value="showavlqtyonstk"   <%=disabled%> <%=checkOption("showavlqtyonstk")%> >
                        	&nbsp; Show Available Qty on Stock Take</label> </TH>
                         
                            <TH WIDTH="20%" ALIGN = "LEFT">
                        	<label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="InHouse" value="rptPSVsINV"   <%=disabled%> <%=checkOption("rptPSVsINV")%> >
                        	&nbsp; Stock Take Summary</label> </TH>
                        	
                       		<TH WIDTH="20%" ALIGN = "LEFT">
                           <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;" name="InHouse" value="stocktakeWithPrice"  <%=disabled%> <%=checkOption("stocktakeWithPrice")%> >
                            &nbsp; Stock Take Summary (with price)</label>
                        	</TH>
                        	
                        	<TH WIDTH="20%" ALIGN = "LEFT">
                        	<label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="InHouse" value="stkreset"   <%=disabled%> <%=checkOption("stkreset")%> >
                        	&nbsp; Stock Take Reset </label></TH>
                        	
                        	<%-- <TH WIDTH="20%" ALIGN = "LEFT">
                        	<label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="InHouse" value="kitdekitwithbom"   <%=disabled%> <%=checkOption("kitdekitwithbom")%> >
                        	&nbsp; Kitting/De-Kitting</label>
                        	
                        	<TH WIDTH="20%" ALIGN = "LEFT">
                        	<label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="InHouse" value="kitdekitwithbomnew" <%=disabled%> <%=checkOption("kitdekitwithbomnew")%>>
                       		 &nbsp; Kitting De-Kitting New</label>
                        	
                        	 <TR>
                        	
                        	<TH WIDTH="20%" ALIGN = "LEFT">
                       		 <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="InHouse" value="kitdekitwithbomedit" <%=disabled%> <%=checkOption("kitdekitwithbomedit")%>>
                       		 &nbsp; Kitting De-Kitting Edit</label>
                        
                        	<TH WIDTH="20%" ALIGN = "LEFT">
                       		<label class="checkbox-inline"> 	<INPUT Type=Checkbox  style="border:0;" name="InHouse" value="kitdekitwithbomsummry"   <%=disabled%> <%=checkOption("kitdekitwithbomsummry")%> >
                        	&nbsp; Summary - Kitting/De-Kitting</label> --%>
                        	
                        	<TR>
                         
                        	<TH WIDTH="20%" ALIGN = "LEFT">
                        	 <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="InHouse" value="generateBarcode" <%=disabled%> <%=checkOption("generateBarcode")%>>  
                         	&nbsp; Generate Barcode</label>
                         
                            <TH WIDTH="20%" ALIGN = "LEFT">
                        	<label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="InHouse" value="generateReceiptBarcode"   <%=disabled%> <%=checkOption("generateReceiptBarcode")%> >
                        	&nbsp; Generate Receipt Barcode</label> </TH>
                        	
                       		<TH WIDTH="20%" ALIGN = "LEFT">
                           <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;" name="InHouse" value="generateLocBarcode"  <%=disabled%> <%=checkOption("generateLocBarcode")%> >
                            &nbsp; Generate Location Barcode</label>
                        	</TH>
                        	
                       		<TH WIDTH="20%" ALIGN = "LEFT">
                           <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;" name="InHouse" value="genproductbarcode"  <%=disabled%> <%=checkOption("genproductbarcode")%> >
                            &nbsp; Generate Product Barcode</label>
                        	</TH>
                        	
                        	<TR>
                        	
                        	 <TH WIDTH="20%" ALIGN = "LEFT">
                           <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;" name="InHouse" value="generateManualBarcode"  <%=disabled%> <%=checkOption("generateManualBarcode")%> >
                            &nbsp; Generate Manual Barcode </label>
                        	</TH>
                        	
                        	<%-- <TR> 
                 
                        <TH WIDTH="20%" ALIGN = "LEFT">
                        <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="InHouse" value="kitdekit" <%=disabled%> <%=checkOption("kitdekit")%>>
                        &nbsp; Kitting De-Kitting</label>
                        
                        <TH WIDTH="20%" ALIGN = "LEFT">
                        <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="InHouse" value="bulkkitdekit" <%=disabled%> <%=checkOption("bulkkitdekit")%>>
                        &nbsp; Kitting De-Kitting(bulk with ref BOM) </label>                       
                                               	 
                            
                        	</TH>                  --%>                                  
                        
                    </TABLE>
                    </div>
                     </div>
                     
             
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
	                        	<label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="Reports" value="viewinventoryprodmultiuom"   <%=disabled%> <%=checkOption("viewinventoryprodmultiuom")%>>
	                        	&nbsp; Inventory Summary With Total Quantity</label>
                         </TH>
                         
                         <TH WIDTH="20%" ALIGN = "LEFT">
	                        	<label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="Reports" value="sumqtybyproduct"   <%=disabled%> <%=checkOption("sumqtybyproduct")%>>
	                        	&nbsp; Inventory Summary With Total Quantity (group by product)</label>
                         </TH>
							  						                 
                         <TH WIDTH="20%" ALIGN = "LEFT">
	                        	<label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="Reports" value="viewinventorybatchmultiuom"   <%=disabled%> <%=checkOption("viewinventorybatchmultiuom")%>>
	                        	&nbsp; Inventory Summary With Batch/Sno</label> 
                         </TH>  
                    
                        <TH WIDTH="20%" ALIGN = "LEFT">
                        	<label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="Reports" value="viewInventoryByProd"  <%=disabled%> <%=checkOption("viewInventoryByProd")%>>
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
                        	<label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="Reports" value="msInvnopriceqty"   <%=disabled%> <%=checkOption("msInvnopriceqty")%>>
                        	&nbsp; Inventory Summary With Batch/Sno (with pcs)</label>
                        </TH>
                              
                         
                       <TH WIDTH="20%" ALIGN = "LEFT">
                            <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="Reports" value="msInvList"  <%=disabled%> <%=checkOption("msInvList")%>>
                            &nbsp; Inventory Summary (with min/max/zero qty)</label> 
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
                            <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="Reports" value="inventoryAgingSummary"  <%=disabled%> <%=checkOption("inventoryAgingSummary")%>>
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
                            <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="Reports" value="invopenclosestock"  <%=disabled%> <%=checkOption("invopenclosestock")%>>
                            &nbsp; Inventory Summary Opening/Closing Stock  </label>
                       </TH>  
                     
                          <TH WIDTH="20%" ALIGN = "LEFT">
                            <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="Reports" value="invopenclosestockavgcost"  <%=disabled%> <%=checkOption("invopenclosestockavgcost")%>>
                            &nbsp; Inventory Summary Opening/Closing Stock (with average cost)</label>
                       </TH> 
                       
	                    <TH WIDTH="20%" ALIGN = "LEFT">
	                            <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="Reports" value="msInvListwithcost"  <%=disabled%> <%=checkOption("msInvListwithcost")%>>
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
<div class="row">
<div class="col-12 col-sm-12"><INPUT Type=Checkbox class="form-check-input"  style="border:0;" name="PosReports"  onclick="return checkAllPOSReports(this.checked);">
                       &nbsp; Select/Unselect POS Reports </div>
  </div> 
<div class="panel panel-default">
<div class="panel-heading" style="background: #eaeafa"><strong>POS Reports</strong></div>
<div class="panel-body">
             <TABLE >
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
<div class="row">
  		<div class="col-12 col-sm-12"><INPUT Type=Checkbox class="form-check-input"  style="border:0;" name="Reports"  onclick="">
                       &nbsp; Select/Unselect Integrations</div>
  </div> 
  
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
	                        	<label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="ActivityLogs" value="msPOList"  <%=disabled%> <%=checkOption("msPOList")%>>
	                        	&nbsp; Activity Logs</label>
                         </TH>
                    
                    </TABLE>
    </div>
</div>

<div class="row">
  		<div class="col-12 col-sm-12"><INPUT Type=Checkbox class="form-check-input"  style="border:0;" name="OwnerApp"  onclick="return checkAllOwnerApp(this.checked);">
                        &nbsp; Select/Unselect Owner App </div>
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
<div class="row">
  		<div class="col-12 col-sm-12"><INPUT Type=Checkbox class="form-check-input"  style="border:0;" name="PeppolIntegration"  onclick="return checkAllPeppolIntegration(this.checked);">
                        &nbsp; Select/Unselect Peppol Integration </div>
  </div>
  
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
                              <%-- <div class="row">
  		<div class="col-12 col-sm-12"><INPUT Type=Checkbox class="form-check-input"  style="border:0;" name="AccountingHome"  onclick="return checkAllAccountingHome(this.checked);">
                     <strong>   &nbsp; Select/Unselect Accounting - Home Page</strong> </div>
  </div>
<div class="panel panel-default">
    <div class="panel-heading" style="background: #eaeafa"><strong>Accounting - Home Page</strong></div>
    <div class="panel-body">
		<TABLE class="table1" style="font-size:14px;width: 100%;">
			<tr>
				<th width="20%" align="left"><input type="checkbox" style="border: 0;" name="AccountingHome" value="profitlosswidget" <%=disabled%> <%=checkOption("profitlosswidget")%>>&nbsp;Profit Loss Widget</th>
				<th width="20%" align="left"><input type="checkbox" style="border: 0;" name="AccountingHome" value="expenseswidget" <%=disabled%> <%=checkOption("expenseswidget")%>>&nbsp;Expense Widget</th>
				<th width="20%" align="left"><input type="checkbox" style="border: 0;" name="AccountingHome" value="incomewidget" <%=disabled%> <%=checkOption("incomewidget")%>>&nbsp;Income Widget</th>
				<th width="20%" align="left"><input type="checkbox" style="border: 0;" name="AccountingHome" value="bankaccountswidget" <%=disabled%> <%=checkOption("bankaccountswidget")%>>&nbsp;Bank Accounts Widget</th>
			</tr>
			<tr>
				<th width="20%" align="left"><input type="checkbox" style="border: 0;" name="AccountingHome" value="purchasewidget" <%=disabled%> <%=checkOption("purchasewidget")%>>&nbsp;Purchase Widget</th>
				<th width="20%" align="left"><input type="checkbox" style="border: 0;" name="AccountingHome" value="saleswidget" <%=disabled%> <%=checkOption("saleswidget")%>>&nbsp;Sales Widget</th>
				<th width="20%" align="left"><input type="checkbox" style="border: 0;" name="AccountingHome" value="movementhistorywidget" <%=disabled%> <%=checkOption("movementhistorywidget")%>>&nbsp;Movement History Widget</th>
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
    	<TABLE class="table1" style="font-size:14px;width: 100%;">		
			<tr>
				<th width="20%" align="left"><input type="checkbox" style="border: 0;" name="AccountingMaster" value="createbankbranch" <%=disabled%> <%=checkOption("createbankbranch")%>>&nbsp;Create bank Branch</th>
				<th width="20%" align="left"><input type="checkbox" style="border: 0;" name="AccountingMaster" value="listbankbranches" <%=disabled%> <%=checkOption("listbankbranches")%>>&nbsp;List Bank Branches</th>
				<th width="20%" align="left"><input type="checkbox" style="border: 0;" name="AccountingMaster" value="importbankbranchss" <%=disabled%> <%=checkOption("importbankbranchss")%>>&nbsp;Import Bank Branches</th>
				<th></th>
			</tr>
			<tr>
				<th width="20%" align="left"><input type="checkbox" style="border: 0;" name="AccountingMaster" value="createcustomer" <%=disabled%> <%=checkOption("createcustomer")%>>&nbsp;Create Customer</th>
				<th width="20%" align="left"><input type="checkbox" style="border: 0;" name="AccountingMaster" value="listcustomers" <%=disabled%> <%=checkOption("listcustomers")%>>&nbsp;List Customers</th>
				<th width="20%" align="left"><input type="checkbox" style="border: 0;" name="AccountingMaster" value="importcustomers" <%=disabled%> <%=checkOption("importcustomers")%>>&nbsp;Import Customers</th>
				<th></th>
			</tr>
			<tr>
				<th width="20%" align="left"><input type="checkbox" style="border: 0;" name="AccountingMaster" value="createsupplier" <%=disabled%> <%=checkOption("createsupplier")%>>&nbsp;Create Supplier</th>
				<th width="20%" align="left"><input type="checkbox" style="border: 0;" name="AccountingMaster" value="listsuppliers" <%=disabled%> <%=checkOption("listsuppliers")%>>&nbsp;List Suppliers</th>
				<th width="20%" align="left"><input type="checkbox" style="border: 0;" name="AccountingMaster" value="importsuppliers" <%=disabled%> <%=checkOption("importsuppliers")%>>&nbsp;Import Suppliers</th>
				<th></th>
			</tr>
			<tr>
				<th width="20%" align="left"><input type="checkbox" style="border: 0;" name="AccountingMaster" value="createaccount" <%=disabled%> <%=checkOption("createaccount")%>>&nbsp;Create Account</th>
				<th width="20%" align="left"><input type="checkbox" style="border: 0;" name="AccountingMaster" value="listaccounts" <%=disabled%> <%=checkOption("listaccounts")%>>&nbsp;List Accounts</th>
				<th width="20%" align="left"><input type="checkbox" style="border: 0;" name="AccountingMaster" value="importaccounts" <%=disabled%> <%=checkOption("importaccounts")%>>&nbsp;Import Accounts</th>
				<th></th>
			</tr>
			<tr>
				<th width="20%" align="left"><input type="checkbox" style="border: 0;" name="AccountingMaster" value="createdaybook" <%=disabled%> <%=checkOption("createdaybook")%>>&nbsp;Create Daybook</th>
				<th width="20%" align="left"><input type="checkbox" style="border: 0;" name="AccountingMaster" value="listdaybooks" <%=disabled%> <%=checkOption("listdaybooks")%>>&nbsp;List Daybooks</th>
				<th></th><th></th>
			</tr>
			<tr>
				<th width="20%" align="left"><input type="checkbox" style="border: 0;" name="AccountingMaster" value="createcurrency" <%=disabled%> <%=checkOption("createcurrency")%>>&nbsp;Create Currency</th>
				<th width="20%" align="left"><input type="checkbox" style="border: 0;" name="AccountingMaster" value="listcurrencies" <%=disabled%> <%=checkOption("listcurrencies")%>>&nbsp;List Currencies</th>
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
    	<TABLE class="table1" style="font-size:14px;width: 100%;">					
			<tr>
				<th width="20%" align="left"><input type="checkbox" style="border: 0;" name="AccountingTransactions" value="newfinancialtransaction" <%=disabled%> <%=checkOption("newfinancialtransaction")%>>&nbsp;New Financial Transaction</th>
				<th width="20%" align="left"><input type="checkbox" style="border: 0;" name="AccountingTransactions" value="listfinancialtransactions" <%=disabled%> <%=checkOption("listfinancialtransactions")%>>&nbsp;List Financial Transactions</th>
				<th width="20%" align="left"><input type="checkbox" style="border: 0;" name="AccountingTransactions" value="reconcilesalespurchase" <%=disabled%> <%=checkOption("reconcilesalespurchase")%>>&nbsp;Reconcile Sales &amp; Purchase</th>
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
    	<TABLE class="table1" style="font-size:14px;width: 100%;">					
			<tr>
				<th width="20%" align="left"><input type="checkbox" style="border: 0;" name="AccountingReports" value="movementhistoryreport" <%=disabled%> <%=checkOption("movementhistoryreport")%>>&nbsp;Movement History Report</th>
				<th width="20%" align="left"><input type="checkbox" style="border: 0;" name="AccountingReports" value="financialtransactionsreport" <%=disabled%> <%=checkOption("financialtransactionsreport")%>>&nbsp;Financial Transactions Report</th>
				<th width="20%" align="left"><input type="checkbox" style="border: 0;" name="AccountingReports" value="profitlossstatement" <%=disabled%> <%=checkOption("profitlossstatement")%>>&nbsp;Profit Loss Statement</th>
				<th width="20%" align="left"><input type="checkbox" style="border: 0;" name="AccountingReports" value="trailbalance" <%=disabled%> <%=checkOption("trailbalance")%>>&nbsp;Trail Balance</th>
			</tr>
			<tr>
				<th width="20%" align="left"><input type="checkbox" style="border: 0;" name="AccountingReports" value="balancesheet" <%=disabled%> <%=checkOption("balancesheet")%>>&nbsp;Balance sheet</th>
				<th width="20%" align="left"><input type="checkbox" style="border: 0;" name="AccountingReports" value="customerwiseageing" <%=disabled%> <%=checkOption("customerwiseageing")%>>&nbsp;Customer-wise Ageing</th>
				<th width="20%" align="left"><input type="checkbox" style="border: 0;" name="AccountingReports" value="supplierwiseageing" <%=disabled%> <%=checkOption("supplierwiseageing")%>>&nbsp;Supplier-wise Ageing</th>
				<th width="20%" align="left"><input type="checkbox" style="border: 0;" name="AccountingReports" value="cashflowprojection">&nbsp;Cash Flow Projection</th>
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
 <div class="row">
  		<div class="col-12 col-sm-12"><INPUT Type=Checkbox class="form-check-input"  style="border:0;" name="PdaPurchaseTransaction"  onclick="return checkAllPdaPurchaseTransaction(this.checked);">
                     <strong>   &nbsp; Select/Unselect PDA - Purchase Transaction</strong> </div>
  </div>     
<div class="panel panel-default">
<div class="panel-heading" style="background: #eaeafa"><strong>PDA - Purchase Transaction</strong></div>
<div class="panel-body">
    	   <TABLE class="table1" style="font-size:14px;width: 100%;">
                     <TR>
                         <TH WIDTH="20%" ALIGN = "LEFT">
                          <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="PdaPurchaseTransaction" value="pdainboundReceipt"  <%=disabled%> <%=checkOption("pdainboundReceipt")%>>
                          &nbsp; PURCHASE RECEIPT</label>
                          
                         <!--   <TH WIDTH="20%" ALIGN = "LEFT">
                          <INPUT Type=Checkbox  style="border:0;" name="URL" value="pdaTransferReceipt"  <%=disabled%> <%=checkOption("pdaTransferReceipt")%>>
                          &nbsp; Transfer Receipt
                          
                          <TH WIDTH="20%" ALIGN = "LEFT">
                          <INPUT Type=Checkbox  style="border:0;" name="URL" value="pdaLoanReceipt"  <%=disabled%> <%=checkOption("pdaLoanReceipt")%>>
                          &nbsp; Bulk Loan Receipt-->
                          
                          <TH WIDTH="20%" ALIGN = "LEFT">
                          <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="PdaPurchaseTransaction" value="pdaMiscReceipt"  <%=disabled%> <%=checkOption("pdaMiscReceipt")%>>
                           &nbsp;GOODS RECEIPT</label>
                           
                          </TABLE>
                           </div>
                                </div>
<div class="row">
  		<div class="col-12 col-sm-12"><INPUT Type=Checkbox class="form-check-input"  style="border:0;" name="PdaSalesTransaction"  onclick="return checkAllPdaSalesTransaction(this.checked);">
                    &nbsp; Select/Unselect PDA - Sales Transaction</div>
  </div> 
<div class="panel panel-default">
<div class="panel-heading" style="background: #eaeafa"><strong>PDA - Sales Transaction</strong></div>
<div class="panel-body">
    	<TABLE class="table1" style="font-size:14px;width: 100%;">
    	
    	<TR>
                     
                        <TH WIDTH="20%" ALIGN = "LEFT">
                        <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="PdaSalesTransaction" value="pdaOutBoundIssue"  <%=disabled%> <%=checkOption("pdaOutBoundIssue")%>>
                        &nbsp; SALES PICK & ISSUE</label>
                        
                                             
                         <TH WIDTH="20%" ALIGN = "LEFT">
                       <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;" name="PdaSalesTransaction" value="pdaMiscIssue"  <%=disabled%> <%=checkOption("pdaMiscIssue")%>>
                        &nbsp; GOODS ISSUE</label>
                                             
                         <TH WIDTH="20%" ALIGN = "LEFT">
                       <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;" name="PdaSalesTransaction" value="pdaSalesPick"  <%=disabled%> <%=checkOption("pdaSalesPick")%>>
                        &nbsp; SALES PICK</label>
                                             
                         <TH WIDTH="20%" ALIGN = "LEFT">
                       <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;" name="PdaSalesTransaction" value="pdaSalesIssue"  <%=disabled%> <%=checkOption("pdaSalesIssue")%>>
                        &nbsp; SALES ISSUE</label>
                        
                        <TR>
                                             
                         <TH WIDTH="20%" ALIGN = "LEFT">
                       <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;" name="PdaSalesTransaction" value="pdaPickIssue"  <%=disabled%> <%=checkOption("pdaPickIssue")%>>
                        &nbsp; PICK/ISSUE</label>
                                             
                         <TH WIDTH="20%" ALIGN = "LEFT">
                       <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;" name="PdaSalesTransaction" value="pdaSalesOrderCheck"  <%=disabled%> <%=checkOption("pdaSalesOrderCheck")%>>
                        &nbsp; SALES ORDER CHECK</label>
                 </TABLE>
                 </div>
                       </div>
                       <div class="row">
  		<div class="col-12 col-sm-12"><INPUT Type=Checkbox class="form-check-input"  style="border:0;" name="PdaInHouse"  onclick="return checkAllPdaInHouse(this.checked);">
                      &nbsp; Select/Unselect PDA - In House </div>
  </div> 
 <div class="panel panel-default">
<div class="panel-heading" style="background: #eaeafa"><strong>PDA - In House</strong></div>
<div class="panel-body">
    	<TABLE class="table1" style="font-size:14px;width: 100%;">
                    <TR>
                      <TH WIDTH="20%" ALIGN = "LEFT">
                       <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;" name="PdaInHouse" value="pdaInventoryQry"  <%=disabled%> <%=checkOption("pdaInventoryQry")%>>
                        &nbsp; INV QUERY</label>
                        
                        <!-- <TH WIDTH="20%" ALIGN = "LEFT">
                        <INPUT Type=Checkbox  style="border:0;" name="URL" value="pdaPutAway"  <%=disabled%> <%=checkOption("pdaPutAway")%>>
                        &nbsp; Put Away-->
                       
                      <TH WIDTH="20%" ALIGN = "LEFT">
                        <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="PdaInHouse" value="pdaLocTransfer"  <%=disabled%> <%=checkOption("pdaLocTransfer")%>>
                        &nbsp; STOCK MOVE</label>
                        
                      <TH WIDTH="20%" ALIGN = "LEFT">
                       <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;" name="PdaInHouse" value="pdaStockTake"  <%=disabled%> <%=checkOption("pdaStockTake")%>>
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
      <input name="REMARKS" <%=disabled%> value="<%=al.get(al.size() - 3).toString()%>"
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
      <input  size="50" MAXLENGTH=60 disabled value="<%=al.get(al.size() - 1).toString()%>" class="form-control">
	   </div>
		  </div>
           
           
     <div class="form-group">        
     <div class="col-sm-offset-4 col-sm-8">
     <!-- <button type="button" class="Submit btn btn-default" name="C" onClick="window.location.href='../home'"><b>Back</b></button>&nbsp;&nbsp; -->
     <!-- <button type="button" class="Submit btn btn-default"  name="C" Value="Back" onClick="{backNavigation('settings.jsp','UA');}"><b>Back</b></button>&nbsp;&nbsp; -->
	 <button type="Submit" class="btn btn-success" name="upd" value="Save" onClick="return onUpdate();">Save</button>&nbsp;&nbsp;
	 <button type="Submit" class="Submit btn btn-default"  name="Action" Value="Delete" onClick="return confirm('Are you sure you would like to delete ?');"><b>Delete</b></button>&nbsp;&nbsp;
     
    </div>
    </div>
    
    
    <!--
    <%
    out
    .write("<INPUT Type=\"Submit\" name=\"Action\" Value=\"Delete\" onClick=\"return confirm('Are you sure you would like to delete ?');\">&nbsp;");
    %>  -->
     
    
 <%
	} //  Closing else
%>
</form>
</div>
</div>
</div>


<jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
</jsp:include>
