<%--New page design begin --%>
<%@page import="com.track.constants.IConstants"%>
<%@ page import="com.track.dao.*"%>
<jsp:useBean id="ub" class="com.track.gates.userBean" />
<%
String title = "Create Accounting User Access Rights";
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
<%--New page design end --%>
<%
session= request.getSession();

String plant = (String) session.getAttribute("PLANT");
String region = session.getAttribute("REGION").toString();
String ISPEPPOL =new PlantMstDAO().getisPeppol(plant);
String taxbylabelordermanagement = ub.getTaxByLableOrderManagement(plant);
PlantMstDAO plantMstDAO = new PlantMstDAO();
String  MANAGEWORKFLOW1 = plantMstDAO.getMANAGEWORKFLOW1(plant);
String COMP_INDUSTRY = plantMstDAO.getCOMP_INDUSTRY(plant);//Check Company Industry
String  ENABLE_POS =new PlantMstDAO().getispos(plant);
%>
<script type="text/javascript" src="../jsp/js/userLevel.js"></script>
<script type="text/javascript" src="../jsp/js/general.js"></script>
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

    if (document.form.UomLoc)
    {
        if (document.form.UomLoc.disabled == false)
        	document.form.UomLoc.checked = isChk;
        for (i = 0; i < document.form.UomLoc.length; i++)
        {
                if (document.form.UomLoc[i].disabled == false)
            	document.form.UomLoc[i].checked = isChk;
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
    
    if (document.form.OrderAdminHS)
    {
        if (document.form.OrderAdminHS.disabled == false)
        	document.form.OrderAdminHS.checked = isChk;
        for (i = 0; i < document.form.OrderAdminHS.length; i++)
        {
                if (document.form.OrderAdminHS[i].disabled == false)
            	document.form.OrderAdminHS[i].checked = isChk;
        }
    }
    if (document.form.Distribution)
    {
        if (document.form.Distribution.disabled == false)
        	document.form.Distribution.checked = isChk;
        for (i = 0; i < document.form.Distribution.length; i++)
        {
                if (document.form.Distribution[i].disabled == false)
            	document.form.Distribution[i].checked = isChk;
        }
    }
    
    if (document.form.Supplier)
    {
        if (document.form.Supplier.disabled == false)
        	document.form.Supplier.checked = isChk;
        for (i = 0; i < document.form.Supplier.length; i++)
        {
                if (document.form.Supplier[i].disabled == false)
            	document.form.Supplier[i].checked = isChk;
        }
    }
    
    if (document.form.Customer)
    {
        if (document.form.Customer.disabled == false)
        	document.form.Customer.checked = isChk;
        for (i = 0; i < document.form.Customer.length; i++)
        {
                if (document.form.Customer[i].disabled == false)
            	document.form.Customer[i].checked = isChk;
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
    
    if (document.form.Project)
    {
        if (document.form.Project.disabled == false)
        	document.form.Project.checked = isChk;
        for (i = 0; i < document.form.Project.length; i++)
        {
                if (document.form.Project[i].disabled == false)
            	document.form.Project[i].checked = isChk;
        }
    }
    
    if (document.form.Purchaseestimate)
    {
        if (document.form.Purchaseestimate.disabled == false)
        	document.form.Purchaseestimate.checked = isChk;
        for (i = 0; i < document.form.Purchaseestimate.length; i++)
        {
                if (document.form.Purchaseestimate[i].disabled == false)
            	document.form.Purchaseestimate[i].checked = isChk;
        }
    }
    
    if (document.form.PurchaseTransaction)
    {
        if (document.form.PurchaseTransaction.disabled == false)
        	document.form.PurchaseTransaction.checked = isChk;
        for (i = 0; i < document.form.PurchaseTransaction.length; i++)
        {
                if (document.form.PurchaseTransaction[i].disabled == false)
            	document.form.PurchaseTransaction[i].checked = isChk;
        }
    }
    
    if (document.form.expenses)
    {
        if (document.form.expenses.disabled == false)
        	document.form.expenses.checked = isChk;
        for (i = 0; i < document.form.expenses.length; i++)
        {
                if (document.form.expenses[i].disabled == false)
            	document.form.expenses[i].checked = isChk;
        }
    }
    
    if (document.form.GoodsReceipt)
    {
        if (document.form.GoodsReceipt.disabled == false)
        	document.form.GoodsReceipt.checked = isChk;
        for (i = 0; i < document.form.GoodsReceipt.length; i++)
        {
                if (document.form.GoodsReceipt[i].disabled == false)
            	document.form.GoodsReceipt[i].checked = isChk;
        }
    }
    
    if (document.form.bill)
    {
        if (document.form.bill.disabled == false)
        	document.form.bill.checked = isChk;
        for (i = 0; i < document.form.bill.length; i++)
        {
                if (document.form.bill[i].disabled == false)
            	document.form.bill[i].checked = isChk;
        }
    }
    
    if (document.form.PurchaseReturn)
    {
        if (document.form.PurchaseReturn.disabled == false)
        	document.form.PurchaseReturn.checked = isChk;
        for (i = 0; i < document.form.PurchaseReturn.length; i++)
        {
                if (document.form.PurchaseReturn[i].disabled == false)
            	document.form.PurchaseReturn[i].checked = isChk;
        }
    }

    if (document.form.ProductReturn)
    {
        if (document.form.ProductReturn.disabled == false)
        	document.form.ProductReturn.checked = isChk;
        for (i = 0; i < document.form.ProductReturn.length; i++)
        {
                if (document.form.ProductReturn[i].disabled == false)
            	document.form.ProductReturn[i].checked = isChk;
        }
    }

    if (document.form.ProductReturnReceive)
    {
        if (document.form.ProductReturnReceive.disabled == false)
        	document.form.ProductReturnReceive.checked = isChk;
        for (i = 0; i < document.form.ProductReturnReceive.length; i++)
        {
                if (document.form.ProductReturnReceive[i].disabled == false)
            	document.form.ProductReturnReceive[i].checked = isChk;
        }
    }
    
    if (document.form.SupplierCreditNotes)
    {
        if (document.form.SupplierCreditNotes.disabled == false)
        	document.form.SupplierCreditNotes.checked = isChk;
        for (i = 0; i < document.form.SupplierCreditNotes.length; i++)
        {
                if (document.form.SupplierCreditNotes[i].disabled == false)
            	document.form.SupplierCreditNotes[i].checked = isChk;
        }
    }
    
    if (document.form.PurchaseReports)
    {
        if (document.form.PurchaseReports.disabled == false)
        	document.form.PurchaseReports.checked = isChk;
        for (i = 0; i < document.form.PurchaseReports.length; i++)
        {
                if (document.form.PurchaseReports[i].disabled == false)
            	document.form.PurchaseReports[i].checked = isChk;
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

    if (document.form.SalesEstimateReports)
    {
        if (document.form.SalesEstimateReports.disabled == false)
        	document.form.SalesEstimateReports.checked = isChk;
        for (i = 0; i < document.form.SalesEstimateReports.length; i++)
        {
                if (document.form.SalesEstimateReports[i].disabled == false)
            	document.form.SalesEstimateReports[i].checked = isChk;
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
    
    if (document.form.SalesOrder)
    {
        if (document.form.SalesOrder.disabled == false)
        	document.form.SalesOrder.checked = isChk;
        for (i = 0; i < document.form.SalesOrder.length; i++)
        {
                if (document.form.SalesOrder[i].disabled == false)
            	document.form.SalesOrder[i].checked = isChk;
        }
    }

    if (document.form.Consignment)
    {
        if (document.form.Consignment.disabled == false)
        	document.form.Consignment.checked = isChk;
        for (i = 0; i < document.form.Consignment.length; i++)
        {
                if (document.form.Consignment[i].disabled == false)
            	document.form.Consignment[i].checked = isChk;
        }
    }
    
    if (document.form.GoodsIssued)
    {
        if (document.form.GoodsIssued.disabled == false)
        	document.form.GoodsIssued.checked = isChk;
        for (i = 0; i < document.form.GoodsIssued.length; i++)
        {
                if (document.form.GoodsIssued[i].disabled == false)
            	document.form.GoodsIssued[i].checked = isChk;
        }
    }

    if (document.form.Invoice)
    {
        if (document.form.Invoice.disabled == false)
        	document.form.Invoice.checked = isChk;
        for (i = 0; i < document.form.Invoice.length; i++)
        {
                if (document.form.Invoice[i].disabled == false)
            	document.form.Invoice[i].checked = isChk;
        }
    }
    
    if (document.form.SalesReturn)
    {
        if (document.form.SalesReturn.disabled == false)
        	document.form.SalesReturn.checked = isChk;
        for (i = 0; i < document.form.SalesReturn.length; i++)
        {
                if (document.form.SalesReturn[i].disabled == false)
            	document.form.SalesReturn[i].checked = isChk;
        }
    }
    if (document.form.CreditNotes)
    {
        if (document.form.CreditNotes.disabled == false)
        	document.form.CreditNotes.checked = isChk;
        for (i = 0; i < document.form.CreditNotes.length; i++)
        {
                if (document.form.CreditNotes[i].disabled == false)
            	document.form.CreditNotes[i].checked = isChk;
        }
    }
    
    if (document.form.SalesReports)
    {
        if (document.form.SalesReports.disabled == false)
        	document.form.SalesReports.checked = isChk;
        for (i = 0; i < document.form.SalesReports.length; i++)
        {
                if (document.form.SalesReports[i].disabled == false)
            	document.form.SalesReports[i].checked = isChk;
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

    if (document.form.ConsignmentReports)
    {
        if (document.form.ConsignmentReports.disabled == false)
        	document.form.ConsignmentReports.checked = isChk;
        for (i = 0; i < document.form.ConsignmentReports.length; i++)
        {
                if (document.form.ConsignmentReports[i].disabled == false)
            	document.form.ConsignmentReports[i].checked = isChk;
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
    if (document.form.PosReports)
    {
        if (document.form.PosReports.disabled == false)
        	document.form.PosReports.checked = isChk;
        for (i = 0; i < document.form.PosReports.length; i++)
        {
                if (document.form.PosReports[i].disabled == false)
            	document.form.PosReports[i].checked = isChk;
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
    if (document.form.OwnerApp)
    {
        if (document.form.OwnerApp.disabled == false)
        	document.form.OwnerApp.checked = isChk;
        for (i = 0; i < document.form.OwnerApp.length; i++)
        {
                if (document.form.OwnerApp[i].disabled == false)
            	document.form.OwnerApp[i].checked = isChk;
        }
    }

    if (document.form.PeppolIntegration)
    {
        if (document.form.PeppolIntegration.disabled == false)
        	document.form.PeppolIntegration.checked = isChk;
        for (i = 0; i < document.form.PeppolIntegration.length; i++)
        {
                if (document.form.PeppolIntegration[i].disabled == false)
            	document.form.PeppolIntegration[i].checked = isChk;
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
    

    if (document.form.Banking)
    {
        if (document.form.Banking.disabled == false)
        	document.form.Banking.checked = isChk;
        for (i = 0; i < document.form.Banking.length; i++)
        {
                if (document.form.Banking[i].disabled == false)
            	document.form.Banking[i].checked = isChk;
        }
    }
    
    if (document.form.chartOfAccounts)
    {
        if (document.form.chartOfAccounts.disabled == false)
        	document.form.chartOfAccounts.checked = isChk;
        for (i = 0; i < document.form.chartOfAccounts.length; i++)
        {
                if (document.form.chartOfAccounts[i].disabled == false)
            	document.form.chartOfAccounts[i].checked = isChk;
        }
    }

    if (document.form.apSummary)
    {
        if (document.form.apSummary.disabled == false)
        	document.form.apSummary.checked = isChk;
        for (i = 0; i < document.form.apSummary.length; i++)
        {
                if (document.form.apSummary[i].disabled == false)
            	document.form.apSummary[i].checked = isChk;
        }
    }
    
    if (document.form.PaymentMade)
    {
        if (document.form.PaymentMade.disabled == false)
        	document.form.PaymentMade.checked = isChk;
        for (i = 0; i < document.form.PaymentMade.length; i++)
        {
                if (document.form.PaymentMade[i].disabled == false)
            	document.form.PaymentMade[i].checked = isChk;
        }
    }
    
    if (document.form.PDCPaymentMade)
    {
        if (document.form.PDCPaymentMade.disabled == false)
        	document.form.PDCPaymentMade.checked = isChk;
        for (i = 0; i < document.form.PDCPaymentMade.length; i++)
        {
                if (document.form.PDCPaymentMade[i].disabled == false)
            	document.form.PDCPaymentMade[i].checked = isChk;
        }
    }
    
    if (document.form.PaymentReceived)
    {
        if (document.form.PaymentReceived.disabled == false)
        	document.form.PaymentReceived.checked = isChk;
        for (i = 0; i < document.form.PaymentReceived.length; i++)
        {
                if (document.form.PaymentReceived[i].disabled == false)
            	document.form.PaymentReceived[i].checked = isChk;
        }
    }
    
    if (document.form.PDCPaymentReceived)
    {
        if (document.form.PDCPaymentReceived.disabled == false)
        	document.form.PDCPaymentReceived.checked = isChk;
        for (i = 0; i < document.form.PDCPaymentReceived.length; i++)
        {
                if (document.form.PDCPaymentReceived[i].disabled == false)
            	document.form.PDCPaymentReceived[i].checked = isChk;
        }
    }
    
    if (document.form.JournalEntry)
    {
        if (document.form.JournalEntry.disabled == false)
        	document.form.JournalEntry.checked = isChk;
        for (i = 0; i < document.form.JournalEntry.length; i++)
        {
                if (document.form.JournalEntry[i].disabled == false)
            	document.form.JournalEntry[i].checked = isChk;
        }
    }
    
    if (document.form.ContraEntry)
    {
        if (document.form.ContraEntry.disabled == false)
        	document.form.ContraEntry.checked = isChk;
        for (i = 0; i < document.form.ContraEntry.length; i++)
        {
                if (document.form.ContraEntry[i].disabled == false)
            	document.form.ContraEntry[i].checked = isChk;
        }
    }

//     if (document.form.AccountingReports)
//     {
//         if (document.form.AccountingReports.disabled == false)
//         	document.form.AccountingReports.checked = isChk;
//         for (i = 0; i < document.form.AccountingReports.length; i++)
//         {
//                 if (document.form.AccountingReports[i].disabled == false)
//             	document.form.AccountingReports[i].checked = isChk;
//         }
//     }


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

function checkAllUomLoc(isChk)
{
	if (document.form.UomLoc)
	{
	    if (document.form.UomLoc.disabled == false)
	    	document.form.UomLoc.checked = isChk;
	    for (i = 0; i < document.form.UomLoc.length; i++)
	    {
	            if (document.form.UomLoc[i].disabled == false)
	        	document.form.UomLoc[i].checked = isChk;
	    }
	}
}

function checkAllUserEmployee(isChk)
{
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
function checkAllDistribution(isChk)
{
    if (document.form.Distribution)
    {
        if (document.form.Distribution.disabled == false)
        	document.form.Distribution.checked = isChk;
        for (i = 0; i < document.form.Distribution.length; i++)
        {
                if (document.form.Distribution[i].disabled == false)
            	document.form.Distribution[i].checked = isChk;
        }
    }
}

function checkAllOrderAdminHS(isChk)
{
if (document.form.OrderAdminHS)
{
    if (document.form.OrderAdminHS.disabled == false)
    	document.form.OrderAdminHS.checked = isChk;
    for (i = 0; i < document.form.OrderAdminHS.length; i++)
    {
            if (document.form.OrderAdminHS[i].disabled == false)
        	document.form.OrderAdminHS[i].checked = isChk;
    }
}
}
function checkAllSupplier(isChk)
{
if (document.form.Supplier)
{
    if (document.form.Supplier.disabled == false)
    	document.form.Supplier.checked = isChk;
    for (i = 0; i < document.form.Supplier.length; i++)
    {
            if (document.form.Supplier[i].disabled == false)
        	document.form.Supplier[i].checked = isChk;
    }
}
}
function checkAllCustomer(isChk)
{
if (document.form.Customer)
{
    if (document.form.Customer.disabled == false)
    	document.form.Customer.checked = isChk;
    for (i = 0; i < document.form.Customer.length; i++)
    {
            if (document.form.Customer[i].disabled == false)
        	document.form.Customer[i].checked = isChk;
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

function checkAllProject(isChk)
{
    if (document.form.Project)
    {
        if (document.form.Project.disabled == false)
        	document.form.Project.checked = isChk;
        for (i = 0; i < document.form.Project.length; i++)
        {
                if (document.form.Project[i].disabled == false)
            	document.form.Project[i].checked = isChk;
        }
    }
}

function checkAllPurchaseestimate(isChk)
{
    if (document.form.Purchaseestimate)
    {
        if (document.form.Purchaseestimate.disabled == false)
        	document.form.Purchaseestimate.checked = isChk;
        for (i = 0; i < document.form.Purchaseestimate.length; i++)
        {
                if (document.form.Purchaseestimate[i].disabled == false)
            	document.form.Purchaseestimate[i].checked = isChk;
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

function checkAllPurchaseTransaction(isChk)
{
if (document.form.PurchaseTransaction)
{
    if (document.form.PurchaseTransaction.disabled == false)
    	document.form.PurchaseTransaction.checked = isChk;
    for (i = 0; i < document.form.PurchaseTransaction.length; i++)
    {
            if (document.form.PurchaseTransaction[i].disabled == false)
        	document.form.PurchaseTransaction[i].checked = isChk;
    }
}
}

function checkAllexpenses(isChk)
{
if (document.form.expenses)
{
    if (document.form.expenses.disabled == false)
    	document.form.expenses.checked = isChk;
    for (i = 0; i < document.form.expenses.length; i++)
    {
            if (document.form.expenses[i].disabled == false)
        	document.form.expenses[i].checked = isChk;
    }
}
}

function checkAllGoodsReceipt(isChk)
{
if (document.form.GoodsReceipt)
{
    if (document.form.GoodsReceipt.disabled == false)
    	document.form.GoodsReceipt.checked = isChk;
    for (i = 0; i < document.form.GoodsReceipt.length; i++)
    {
            if (document.form.GoodsReceipt[i].disabled == false)
        	document.form.GoodsReceipt[i].checked = isChk;
    }
}
}

function checkAllbill(isChk)
{
if (document.form.bill)
{
    if (document.form.bill.disabled == false)
    	document.form.bill.checked = isChk;
    for (i = 0; i < document.form.bill.length; i++)
    {
            if (document.form.bill[i].disabled == false)
        	document.form.bill[i].checked = isChk;
    }
}
}

function checkAllPurchaseReturn(isChk)
{
if (document.form.PurchaseReturn)
{
    if (document.form.PurchaseReturn.disabled == false)
    	document.form.PurchaseReturn.checked = isChk;
    for (i = 0; i < document.form.PurchaseReturn.length; i++)
    {
            if (document.form.PurchaseReturn[i].disabled == false)
        	document.form.PurchaseReturn[i].checked = isChk;
    }
}
}

function checkAllProductReturn(isChk)
{
if (document.form.ProductReturn)
{
    if (document.form.ProductReturn.disabled == false)
    	document.form.ProductReturn.checked = isChk;
    for (i = 0; i < document.form.ProductReturn.length; i++)
    {
            if (document.form.ProductReturn[i].disabled == false)
        	document.form.ProductReturn[i].checked = isChk;
    }
}
}

function checkAllProductReturnReceive(isChk)
{
if (document.form.ProductReturnReceive)
{
    if (document.form.ProductReturnReceive.disabled == false)
    	document.form.ProductReturnReceive.checked = isChk;
    for (i = 0; i < document.form.ProductReturnReceive.length; i++)
    {
            if (document.form.ProductReturnReceive[i].disabled == false)
        	document.form.ProductReturnReceive[i].checked = isChk;
    }
}
}

function checkAllSupplierCreditNotes(isChk)
{
if (document.form.SupplierCreditNotes)
{
    if (document.form.SupplierCreditNotes.disabled == false)
    	document.form.SupplierCreditNotes.checked = isChk;
    for (i = 0; i < document.form.SupplierCreditNotes.length; i++)
    {
            if (document.form.SupplierCreditNotes[i].disabled == false)
        	document.form.SupplierCreditNotes[i].checked = isChk;
    }
}
}

function checkAllPurchaseReports(isChk)
{
if (document.form.PurchaseReports)
{
    if (document.form.PurchaseReports.disabled == false)
    	document.form.PurchaseReports.checked = isChk;
    for (i = 0; i < document.form.PurchaseReports.length; i++)
    {
            if (document.form.PurchaseReports[i].disabled == false)
        	document.form.PurchaseReports[i].checked = isChk;
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

function checkAllSalesEstimateReports(isChk)
{
if (document.form.SalesEstimateReports)
{
    if (document.form.SalesEstimateReports.disabled == false)
    	document.form.SalesEstimateReports.checked = isChk;
    for (i = 0; i < document.form.SalesEstimateReports.length; i++)
    {
            if (document.form.SalesEstimateReports[i].disabled == false)
        	document.form.SalesEstimateReports[i].checked = isChk;
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

function checkAllSalesOrder(isChk)
{
if (document.form.SalesOrder)
{
    if (document.form.SalesOrder.disabled == false)
    	document.form.SalesOrder.checked = isChk;
    for (i = 0; i < document.form.SalesOrder.length; i++)
    {
            if (document.form.SalesOrder[i].disabled == false)
        	document.form.SalesOrder[i].checked = isChk;
    }
}
}

function checkAllConsignment(isChk)
{
if (document.form.Consignment)
{
    if (document.form.Consignment.disabled == false)
    	document.form.Consignment.checked = isChk;
    for (i = 0; i < document.form.Consignment.length; i++)
    {
            if (document.form.Consignment[i].disabled == false)
        	document.form.Consignment[i].checked = isChk;
    }
}
}

function checkAllGoodsIssued(isChk)
{
if (document.form.GoodsIssued)
{
    if (document.form.GoodsIssued.disabled == false)
    	document.form.GoodsIssued.checked = isChk;
    for (i = 0; i < document.form.GoodsIssued.length; i++)
    {
            if (document.form.GoodsIssued[i].disabled == false)
        	document.form.GoodsIssued[i].checked = isChk;
    }
}
}

function checkAllInvoice(isChk)
{
if (document.form.Invoice)
{
    if (document.form.Invoice.disabled == false)
    	document.form.Invoice.checked = isChk;
    for (i = 0; i < document.form.Invoice.length; i++)
    {
            if (document.form.Invoice[i].disabled == false)
        	document.form.Invoice[i].checked = isChk;
    }
}
}

function checkAllSalesReturn(isChk)
{
if (document.form.SalesReturn)
{
    if (document.form.SalesReturn.disabled == false)
    	document.form.SalesReturn.checked = isChk;
    for (i = 0; i < document.form.SalesReturn.length; i++)
    {
            if (document.form.SalesReturn[i].disabled == false)
        	document.form.SalesReturn[i].checked = isChk;
    }
}
}

function checkAllCreditNotes(isChk)
{
if (document.form.CreditNotes)
{
    if (document.form.CreditNotes.disabled == false)
    	document.form.CreditNotes.checked = isChk;
    for (i = 0; i < document.form.CreditNotes.length; i++)
    {
            if (document.form.CreditNotes[i].disabled == false)
        	document.form.CreditNotes[i].checked = isChk;
    }
}
}

function checkAllSalesReports(isChk)
{
if (document.form.SalesReports)
{
    if (document.form.SalesReports.disabled == false)
    	document.form.SalesReports.checked = isChk;
    for (i = 0; i < document.form.SalesReports.length; i++)
    {
            if (document.form.SalesReports[i].disabled == false)
        	document.form.SalesReports[i].checked = isChk;
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

function checkAllConsignmentReports(isChk)
{
    if (document.form.ConsignmentReports)
    {
        if (document.form.ConsignmentReports.disabled == false)
        	document.form.ConsignmentReports.checked = isChk;
        for (i = 0; i < document.form.ConsignmentReports.length; i++)
        {
                if (document.form.ConsignmentReports[i].disabled == false)
            	document.form.ConsignmentReports[i].checked = isChk;
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
function checkAllPOSReports(isChk)
{
    if (document.form.PosReports)
    {
        if (document.form.PosReports.disabled == false)
        	document.form.PosReports.checked = isChk;
        for (i = 0; i < document.form.PosReports.length; i++)
        {
                if (document.form.PosReports[i].disabled == false)
            	document.form.PosReports[i].checked = isChk;
        }
    }
}

function checkAllBanking(isChk)
{
if (document.form.Banking)
{
    if (document.form.Banking.disabled == false)
    	document.form.Banking.checked = isChk;
    for (i = 0; i < document.form.Banking.length; i++)
    {
            if (document.form.Banking[i].disabled == false)
        	document.form.Banking[i].checked = isChk;
    }
}
}
function checkAllchartOfAccounts(isChk)
{
if (document.form.chartOfAccounts)
{
    if (document.form.chartOfAccounts.disabled == false)
    	document.form.chartOfAccounts.checked = isChk;
    for (i = 0; i < document.form.chartOfAccounts.length; i++)
    {
            if (document.form.chartOfAccounts[i].disabled == false)
        	document.form.chartOfAccounts[i].checked = isChk;
    }
}
}
function checkAllapSummary(isChk)
{
if (document.form.apSummary)
{
    if (document.form.apSummary.disabled == false)
    	document.form.apSummary.checked = isChk;
    for (i = 0; i < document.form.apSummary.length; i++)
    {
            if (document.form.apSummary[i].disabled == false)
        	document.form.apSummary[i].checked = isChk;
    }
}
}
function checkAllPaymentMade(isChk)
{
if (document.form.PaymentMade)
{
    if (document.form.PaymentMade.disabled == false)
    	document.form.PaymentMade.checked = isChk;
    for (i = 0; i < document.form.PaymentMade.length; i++)
    {
            if (document.form.PaymentMade[i].disabled == false)
        	document.form.PaymentMade[i].checked = isChk;
    }
}
}
function checkAllPDCPaymentMade(isChk)
{
if (document.form.PDCPaymentMade)
{
    if (document.form.PDCPaymentMade.disabled == false)
    	document.form.PDCPaymentMade.checked = isChk;
    for (i = 0; i < document.form.PDCPaymentMade.length; i++)
    {
            if (document.form.PDCPaymentMade[i].disabled == false)
        	document.form.PDCPaymentMade[i].checked = isChk;
    }
}
}
function checkAllPaymentReceived(isChk)
{
if (document.form.PaymentReceived)
{
    if (document.form.PaymentReceived.disabled == false)
    	document.form.PaymentReceived.checked = isChk;
    for (i = 0; i < document.form.PaymentReceived.length; i++)
    {
            if (document.form.PaymentReceived[i].disabled == false)
        	document.form.PaymentReceived[i].checked = isChk;
    }
}
}
function checkAllPDCPaymentReceived(isChk)
{
if (document.form.PDCPaymentReceived)
{
    if (document.form.PDCPaymentReceived.disabled == false)
    	document.form.PDCPaymentReceived.checked = isChk;
    for (i = 0; i < document.form.PDCPaymentReceived.length; i++)
    {
            if (document.form.PDCPaymentReceived[i].disabled == false)
        	document.form.PDCPaymentReceived[i].checked = isChk;
    }
}
}

function checkAllJournalEntry(isChk)
{
if (document.form.JournalEntry)
{
    if (document.form.JournalEntry.disabled == false)
    	document.form.JournalEntry.checked = isChk;
    for (i = 0; i < document.form.JournalEntry.length; i++)
    {
            if (document.form.JournalEntry[i].disabled == false)
        	document.form.JournalEntry[i].checked = isChk;
    }
}
}

function checkAllContraEntry(isChk)
{
if (document.form.ContraEntry)
{
    if (document.form.ContraEntry.disabled == false)
    	document.form.ContraEntry.checked = isChk;
    for (i = 0; i < document.form.ContraEntry.length; i++)
    {
            if (document.form.ContraEntry[i].disabled == false)
        	document.form.ContraEntry[i].checked = isChk;
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
function checkAllOwnerApp(isChk)
{
    if (document.form.OwnerApp)
    {
        if (document.form.OwnerApp.disabled == false)
        	document.form.OwnerApp.checked = isChk;
        for (i = 0; i < document.form.OwnerApp.length; i++)
        {
                if (document.form.OwnerApp[i].disabled == false)
            	document.form.OwnerApp[i].checked = isChk;
        }
    }
}

function checkAllPeppolIntegration(isChk)
{
    if (document.form.PeppolIntegration)
    {
        if (document.form.PeppolIntegration.disabled == false)
        	document.form.PeppolIntegration.checked = isChk;
        for (i = 0; i < document.form.PeppolIntegration.length; i++)
        {
                if (document.form.PeppolIntegration[i].disabled == false)
            	document.form.PeppolIntegration[i].checked = isChk;
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

// function checkAllAccountingReports(isChk)
// {
//     if (document.form.AccountingReports)
//     {
//         if (document.form.AccountingReports.disabled == false)
//         	document.form.AccountingReports.checked = isChk;
//         for (i = 0; i < document.form.AccountingReports.length; i++)
//         {
//                 if (document.form.AccountingReports[i].disabled == false)
//             	document.form.AccountingReports[i].checked = isChk;
//         }
//     }
// }

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
	  <!-- Thanzith Modified on 23.02.2022 -->
            <ul class="breadcrumb backpageul">    
                <li><a href="../home"><span class="underline-on-hover">Dashboard</span></a></li>	
                 <li><a href="../useraccess/summary"><span class="underline-on-hover">Accounting User Access Rights Summary</span></a></li>                     
                <li><label>Create Accounting User Access Rights</label></li>                                   
            </ul>             
    <!-- Thanzith Modified on 23.02.2022 -->
            <div class="box-header menu-drop">
              <h1 style="font-size: 20px;" class="box-title"><%=title %></h1>
              <h1 style="font-size: 18px; cursor: pointer;"
				class="box-title pull-right"
					onclick="window.location.href='../useraccess/summary'">
				<i class="glyphicon glyphicon-remove"></i>
			</h1>
		</div>
		
 <div class="box-body">
<%--New page design end --%>
<form name="form" class="form-horizontal" method="POST" action="../jsp/userLevelAccountingSubmit.jsp" onSubmit="return validateLevel()">
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
                         
                   <% if(COMP_INDUSTRY.equals("Retail")) { %>            
                   <TH WIDTH="20%" ALIGN = "LEFT">
                  <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;" name="Home" value="homepos">
                  &nbsp;POS</label>                
                  <% }%>
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
                        <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="UserAdmin" value="customerMaintCpny">
                        &nbsp;  Edit Your Company Profile</label>
                        
                        <% if(COMP_INDUSTRY.equals("Retail")) { %>   
                        <TH WIDTH="20%" ALIGN = "LEFT">
                        <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="UserAdmin" value="outletsummary">
                        &nbsp;  Outlet Summary</label>
                        
                        
                       	<TH WIDTH="20%" ALIGN = "LEFT">
                        <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="UserAdmin" value="outletnew">
                        &nbsp;  New Outlet</label>
                        
                        
                        <TH WIDTH="20%" ALIGN = "LEFT">
                        <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="UserAdmin" value="outletedit">
                        &nbsp;  Edit Outlet</label>  
                        
                        <TR>
                        
                        <TH WIDTH="20%" ALIGN = "LEFT">
                        <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="UserAdmin" value="outletterminalummary">
                        &nbsp;  Outlet Terminal Summary</label>
                        
                       <TH WIDTH="20%" ALIGN = "LEFT">
                        <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="UserAdmin" value="outletterminalnew">
                        &nbsp;  New Outlet Terminal</label>
                        
                        <TH WIDTH="20%" ALIGN = "LEFT">
                        <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="UserAdmin" value="outletterminaledit">
                        &nbsp;  Edit Outlet Terminal</label>
                        
                        <TH WIDTH="20%" ALIGN = "LEFT">
                        <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="UserAdmin" value="companybanner">
                        &nbsp;  Banner</label>
                        <TR>
                         <%} %>
                         
                     	<TH WIDTH="20%" ALIGN = "LEFT">
                        <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="UserAdmin" value="contactsmry">
                        &nbsp;  Contacts</label>
                          
                        <%-- <% if(COMP_INDUSTRY.equals("Retail")) { %> 
                        <TR>
                        <%} %> --%>
                          
                     <TH WIDTH="20%" ALIGN = "LEFT">
                        <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="UserAdmin" value="contactnew">
                        &nbsp;  New Contacts</label>
                          
                     <TH WIDTH="20%" ALIGN = "LEFT">
                        <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="UserAdmin" value="contactedit">
                        &nbsp;  Edit Contacts</label>
                     
                     <% if(!COMP_INDUSTRY.equals("Retail")) { %> 
                        <TR>
                        <%} %>
                             
                     <TH WIDTH="20%" ALIGN = "LEFT">
                        <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="UserAdmin" value="uaMaintLevel">
                        &nbsp;  User Details</label>
                     
                     <% if(COMP_INDUSTRY.equals("Retail")) { %> 
                        <TR>
                        <%} %>
                       
                       <TH WIDTH="20%" ALIGN = "LEFT">
                     <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="UserAdmin" value="summarylnkuaMaintLevel">
                       &nbsp; Summary &#45; User Details Link</label>
                         
                       <TH WIDTH="20%" ALIGN = "LEFT">
                     <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="UserAdmin" value="edituaMaintLevel">
                       &nbsp; Edit User Details</label>
                        
                     <TH WIDTH="20%" ALIGN = "LEFT">
                        <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="UserAdmin" value="uaChngPwd">
                        &nbsp; Edit Password Details</label>
                     
                     <% if(!COMP_INDUSTRY.equals("Retail")) { %> 
                        <TR>
                        <%} %>
                        
                         <% if(MANAGEWORKFLOW1.equals("1")) {%>
                         <TH WIDTH="20%" ALIGN = "LEFT">
                        <label class="checkbox-inline" ><INPUT Type=Checkbox  style="border:0;" name="UserAdmin" value="manageworkflow">
                        &nbsp; Manage Workflow</label>
                        <%}%>
                     
                     <% if(MANAGEWORKFLOW1.equals("1")) {%>
                        <% if(COMP_INDUSTRY.equals("Retail")) { %> 
                        <TR>
                        <%} }%>
                     
                        <TH WIDTH="20%" ALIGN = "LEFT">
                          <%if(!plant.equalsIgnoreCase("track")){%>
                          <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="UserAdmin" value="uaMaintAcct">
                        &nbsp;  User Access Rights</label>
                        
                         <% if(MANAGEWORKFLOW1.equals("0")) {%>
                       <% if(COMP_INDUSTRY.equals("Retail")) { %> 
                        <TR>
                        <%} }%>
                        
                        <TH WIDTH="20%" ALIGN = "LEFT">
                          <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="UserAdmin" value="newuaMaintAcct">
                        &nbsp; New User Access Rights</label>
                        
                        <TH WIDTH="20%" ALIGN = "LEFT">
                          <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="UserAdmin" value="summarylnkuaMaintAcct" >
                        &nbsp; Summary &#45; User Access Rights Link</label>
                        
                       <% if(MANAGEWORKFLOW1.equals("1")) {%>
                        <% if(!COMP_INDUSTRY.equals("Retail")) { %> 
                        <TR>
                        <%} }%>
                        
                        <TH WIDTH="20%" ALIGN = "LEFT">
                          <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="UserAdmin" value="edituaMaintAcct">
                        &nbsp; Edit User Access Rights<%}%></label>  
                        
                         <% if(MANAGEWORKFLOW1.equals("1")) {%>
                        <% if(COMP_INDUSTRY.equals("Retail")) { %> 
                        <TR>
                        <%} }%>
                        
                        <% if(MANAGEWORKFLOW1.equals("0")) {%>
                        <% if(!COMP_INDUSTRY.equals("Retail")) { %> 
                        <TR>
                        <%} }%>
                      
                        <TH WIDTH="20%" ALIGN = "LEFT">  <%if(plant.equalsIgnoreCase("track")){%>
                        <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="UserAdmin" value="uaNewLevel">
                        &nbsp; Create User Details</label>
                        
                          <% if(MANAGEWORKFLOW1.equals("0")) {%>
                       <% if(COMP_INDUSTRY.equals("Retail")) { %> 
                        <TR>
                        <%} }%>
                        
                          <TH WIDTH="20%" ALIGN = "LEFT">
                           <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="UserAdmin" value="uaauthcmpy">&nbsp;Authorise Company Details<%}%></label>
                      </TABLE>
         </div>
  </div>
  
    <div class="row">
  		<div class="col-12 col-sm-12"><INPUT Type=Checkbox class="form-check-input"  style="border:0;" name="SystemMaster"  onclick="return checkAllSystemMaster(this.checked);">
                        &nbsp; Select/Unselect Product</div>
  </div>
   <div class="panel panel-default">
<div class="panel-heading" style="background: #eaeafa"><strong>Product</strong></div>
<div class="panel-body">
  <TABLE class="table1" style="font-size:14px;width: 100%;">
             <TR>
                  <TH WIDTH="20%" ALIGN = "LEFT">
                 <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;" name="SystemMaster" value="itemsummry" >
                  &nbsp; Product</label>
                  
                  <TH WIDTH="20%" ALIGN = "LEFT">
                 <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;" name="SystemMaster" value="newuitem" >
                  &nbsp; New Product</label>
                  
                  <TH WIDTH="20%" ALIGN = "LEFT">
                 <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;" name="SystemMaster" value="summarylnkuitem" >
                  &nbsp; Summary &#45; Product Link</label>
                  
                  <TH WIDTH="20%" ALIGN = "LEFT">
                 <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;" name="SystemMaster" value="edituitem" >
                  &nbsp; Edit Product</label>
                  
                  <TR>
                  
                  <TH WIDTH="20%" ALIGN = "LEFT">
                 <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;" name="SystemMaster" value="importuitem">
                  &nbsp; Import Product</label>
                  
                  <TH WIDTH="20%" ALIGN = "LEFT">
                 <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;" name="SystemMaster" value="exportuitem">
                  &nbsp; Export Product</label>
                  
                     <TH WIDTH="20%" ALIGN = "LEFT">
                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SystemMaster" value="alternateItemSummary">   
                  &nbsp; Alternate Products</label>
                  
                  <TH WIDTH="20%" ALIGN = "LEFT">
                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SystemMaster" value="importalternateItem">   
                  &nbsp; Import Alternate Products</label>
                  
                  <TR>
                  
                  <TH WIDTH="20%" ALIGN = "LEFT">
                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SystemMaster" value="exportalternateItem" >   
                  &nbsp; Export Alternate Products</label>
                  
                  <TH WIDTH="20%" ALIGN = "LEFT">
                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SystemMaster" value="alternatebrandsummary" > 
                  &nbsp; Alternate Brand Product</label>
                  
                  <TH WIDTH="20%" ALIGN = "LEFT">
                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SystemMaster" value="newalternatebrand"> 
                  &nbsp; New Alternate Brand Product</label>
                  
                  <TH WIDTH="20%" ALIGN = "LEFT">
                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SystemMaster" value="summarylnkalternatebrand"> 
                  &nbsp; Summary &#45; Alternate Brand Product Link</label>
                  
                  <TR>
                  
                  <TH WIDTH="20%" ALIGN = "LEFT">
                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SystemMaster" value="editalternatebrand"> 
                  &nbsp; Edit Alternate Brand Product</label>
                  
                  <TH WIDTH="20%" ALIGN = "LEFT">
                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SystemMaster" value="exportalternatebrand"> 
                  &nbsp; Export Alternate Brand Product</label>
                  
                  <% if(COMP_INDUSTRY.equals("Retail")) { %> 
                   <TH WIDTH="20%" ALIGN = "LEFT">
                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SystemMaster" value="productpromotionsummary">
                  &nbsp; Product Promotion</label>
                  
                   <TH WIDTH="20%" ALIGN = "LEFT">
                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SystemMaster" value="productpromotionnew">
                  &nbsp; New Product Promotion</label>
                  
                 <% if(COMP_INDUSTRY.equals("Retail")) { %> 
                        <TR>
                        <%} %>
                  
                   <TH WIDTH="20%" ALIGN = "LEFT">
                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SystemMaster" value="productpromotionedit">
                  &nbsp; Edit Product Promotion</label>
                  
                   <TH WIDTH="20%" ALIGN = "LEFT">
                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SystemMaster" value="categorypromotionsummary">
                  &nbsp; Category Promotion</label>
                  
                  <% if(!COMP_INDUSTRY.equals("Retail")) { %> 
                        <TR>
                        <%} %>
                  
                   <TH WIDTH="20%" ALIGN = "LEFT">
                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SystemMaster" value="categorypromotionnew">
                  &nbsp; New Category Promotion</label>
                  
                   <TH WIDTH="20%" ALIGN = "LEFT">
                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SystemMaster" value="categorypromotionedit">
                  &nbsp; Edit Category Promotion</label>
                  
                  <% if(COMP_INDUSTRY.equals("Retail")) { %> 
                        <TR>
                        <%} %>
                  
                  <TH WIDTH="20%" ALIGN = "LEFT">
                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SystemMaster" value="brandpromotionsummary">
                  &nbsp; Brand Promotion</label>
                  
                  <TH WIDTH="20%" ALIGN = "LEFT">
                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SystemMaster" value="brandpromotionnew">
                  &nbsp; New Brand Promotion</label>
                  
                  <% if(!COMP_INDUSTRY.equals("Retail")) { %> 
                        <TR>
                        <%} %>
                  
                  <TH WIDTH="20%" ALIGN = "LEFT">
                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SystemMaster" value="brandpromotionedit">
                  &nbsp; Edit Brand Promotion</label>
                  <%}%>
                  
                  <TH WIDTH="20%" ALIGN = "LEFT">
                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SystemMaster" value="postouchscreenconfig">
                  &nbsp; POS Touch Screen Config</label>
                  
                 <% if(COMP_INDUSTRY.equals("Retail")) { %> 
                        <TR>
                        <%} %>
                  
                  <TH WIDTH="20%" ALIGN = "LEFT">
                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SystemMaster" value="postouchscreenconfigdelete">
                  &nbsp; Delete-POS Touch Screen Config</label>
                  
                  <% if(!COMP_INDUSTRY.equals("Retail")) { %> 
                        <TR>
                        <%} %>
                  
                  <TH WIDTH="20%" ALIGN = "LEFT">
                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SystemMaster" value="productdeptsummary">
                  &nbsp; Product Department</label>
                  
                  <TH WIDTH="20%" ALIGN = "LEFT">
                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SystemMaster" value="newproductdept">
                  &nbsp; New Product Department</label>
                  
                  
                  <TH WIDTH="20%" ALIGN = "LEFT">
                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SystemMaster" value="summarylnkproductdept">
                  &nbsp; Summary &#45; Product Department Link</label>
                  
                  
                 <% if(COMP_INDUSTRY.equals("Retail")) { %> 
                        <TR>
                        <%} %>
                  
                 
                  
                  <TH WIDTH="20%" ALIGN = "LEFT">
                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SystemMaster" value="editproductdept" >
                  &nbsp; Edit Product Department</label>
                  
                   <% if(!COMP_INDUSTRY.equals("Retail")) { %> 
                        <TR>
                        <%} %>
                        
                  <TH WIDTH="20%" ALIGN = "LEFT">
                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SystemMaster" value="prdClsSummary">
                  &nbsp; Product Category</label>
                  <TH WIDTH="20%" ALIGN = "LEFT">
                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SystemMaster" value="newprdCls">
                  &nbsp; New Product Category</label>
                  
                  
                  <TH WIDTH="20%" ALIGN = "LEFT">
                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SystemMaster" value="summarylnkprdCls">
                  &nbsp; Summary &#45; Product Category Link</label>
                  
                  
                  <% if(COMP_INDUSTRY.equals("Retail")) { %> 
                        <TR>
                        <%} %>
                  
                  
                  <TH WIDTH="20%" ALIGN = "LEFT">
                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SystemMaster" value="editprdCls" >
                  &nbsp; Edit Product Category</label>
                  
                  <% if(!COMP_INDUSTRY.equals("Retail")) { %> 
                        <TR>
                        <%} %>
                  
                  <TH WIDTH="20%" ALIGN = "LEFT">
                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SystemMaster" value="prdTypeSummary">
                  &nbsp; Product Sub Category</label>
                  
                  <TH WIDTH="20%" ALIGN = "LEFT">
                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SystemMaster" value="newprdType" >
                  &nbsp; New Product Sub Category</label>
                  
                  
                  <TH WIDTH="20%" ALIGN = "LEFT">
                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SystemMaster" value="summarylnkprdType">
                  &nbsp; Summary &#45; Product Sub Category Link</label>
                  
                  
                 <% if(COMP_INDUSTRY.equals("Retail")) { %> 
                        <TR>
                        <%} %>
                  
                  
                  <TH WIDTH="20%" ALIGN = "LEFT">
                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SystemMaster" value="editprdType" >
                  &nbsp; Edit Product Sub Category</label>
                  
                  <% if(!COMP_INDUSTRY.equals("Retail")) { %> 
                        <TR>
                        <%} %>
                  
                  <TH WIDTH="20%" ALIGN = "LEFT">
	                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SystemMaster" value="prdBrandSummary" >
	                  &nbsp; Product Brand</label>
                  
                  <TH WIDTH="20%" ALIGN = "LEFT">
	                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SystemMaster" value="newprdBrand">
	                  &nbsp; New Product Brand</label>
                  
                  <TH WIDTH="20%" ALIGN = "LEFT">
	                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SystemMaster" value="summarylnkprdBrand">
	                  &nbsp; Summary &#45; Product Brand Link</label>
                  
                              
                 <% if(COMP_INDUSTRY.equals("Retail")) { %> 
                        <TR>
                        <%} %>
                  
                  <TH WIDTH="20%" ALIGN = "LEFT">
	                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SystemMaster" value="editprdBrand" >
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
                      <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="UomLoc" value="uomsummry">    
                       &nbsp; Unit of Measure (UOM)</label>
                       
                       <TH WIDTH="20%" ALIGN = "LEFT">
                      <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="UomLoc" value="newuom">    
                       &nbsp; New Unit of Measure (UOM)</label>
                       
                       <TH WIDTH="20%" ALIGN = "LEFT">
                      <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="UomLoc" value="summarylnkuom" >    
                       &nbsp; Summary &#45; Unit of Measure (UOM) Link</label>
                       
                       <TH WIDTH="20%" ALIGN = "LEFT">
                      <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="UomLoc" value="edituom" >    
                       &nbsp; Edit Unit of Measure (UOM)</label>
                       
                       <TR>
                       
                       <TH WIDTH="20%" ALIGN = "LEFT">
	                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="UomLoc" value="locsummry" >     
	                  &nbsp; Location</label>
	                  
	                  <TH WIDTH="20%" ALIGN = "LEFT">
	                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="UomLoc" value="newloc" >     
	                  &nbsp; New Location</label>
	                  
	                  <TH WIDTH="20%" ALIGN = "LEFT">
	                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="UomLoc" value="summarylnkloc">     
	                  &nbsp; Summary &#45; Location Link</label>
	                  
	                  <TH WIDTH="20%" ALIGN = "LEFT">
	                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="UomLoc" value="editloc">     
	                  &nbsp; Edit Location</label>
	                  
	                  <TR>
	                  
	                  <TH WIDTH="20%" ALIGN = "LEFT">
	                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="UomLoc" value="importloc">     
	                  &nbsp; Import Location</label>
	                   
	                  <TH WIDTH="20%" ALIGN = "LEFT">
	                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="UomLoc" value="loctypesummry" >    
	                  &nbsp; Location Type One</label>
	                  
	                  <TH WIDTH="20%" ALIGN = "LEFT">
	                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="UomLoc" value="newloctype">    
	                  &nbsp; New Location Type One</label>
	                  
	                  <TH WIDTH="20%" ALIGN = "LEFT">
	                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="UomLoc" value="summarylnkloctype">    
	                  &nbsp; Summary &#45; Location Type One Link</label> 
	                  
	                <TR>
	                  
	                  <TH WIDTH="20%" ALIGN = "LEFT">
	                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="UomLoc" value="editloctype">    
	                  &nbsp; Edit Location Type One</label>
	                  
	                  <TH WIDTH="20%" ALIGN = "LEFT">
	                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="UomLoc" value="loctypetwosummry" >    
	                  &nbsp; Location Type Two</label>
	                  
	                  <TH WIDTH="20%" ALIGN = "LEFT">
	                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="UomLoc" value="newloctypetwo">    
	                  &nbsp; New Location Type Two</label>
	                  
	                  <TH WIDTH="20%" ALIGN = "LEFT">
	                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="UomLoc" value="summarylnkloctypetwo">    
	                  &nbsp; Summary &#45; Location Type Two Link</label> 
	                  
	                <TR>
	                 <TH WIDTH="20%" ALIGN = "LEFT">
	                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="UomLoc" value="editloctypetwo">    
	                  &nbsp; Edit Location Type Two</label>
	                  
	                  <TH WIDTH="20%" ALIGN = "LEFT">
	                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="UomLoc" value="loctypethreesummry" >    
	                  &nbsp; Location Type Three</label>
	                  
	                  <TH WIDTH="20%" ALIGN = "LEFT">
	                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="UomLoc" value="newloctypethree">    
	                  &nbsp; New Location Type Three</label>
	                  
	                  <TH WIDTH="20%" ALIGN = "LEFT">
	                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="UomLoc" value="summarylnkloctypethree">    
	                  &nbsp; Summary &#45; Location Type Three Link</label> 
	                  
	                  <TR>
	                  
	                  <TH WIDTH="20%" ALIGN = "LEFT">
	                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="UomLoc" value="editloctypethree">    
	                  &nbsp; Edit Location Type Three</label>
	                  
	                 <TH WIDTH="20%" ALIGN = "LEFT">
	                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="UomLoc" value="rsnsummry" >     
	                  &nbsp; Reason Code</label>  
	                  
	                  <TH WIDTH="20%" ALIGN = "LEFT">
	                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="UomLoc" value="newrsn">     
	                  &nbsp; New Reason Code</label>
	                  
	                  <TH WIDTH="20%" ALIGN = "LEFT">
	                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="UomLoc" value="summarylnkrsn">     
	                  &nbsp; Summary &#45; Reason Code Link</label> 
	                  
	              <TR>
	                  
	                  <TH WIDTH="20%" ALIGN = "LEFT">
	                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="UomLoc" value="editrsn">     
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
                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="Supplier" value="vendorsummry"> 
                  &nbsp;  Supplier</label>
                  
                  <TH WIDTH="20%" ALIGN = "LEFT">
                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="Supplier" value="newvendor"> 
                  &nbsp;  New Supplier</label>
                  
                  <TH WIDTH="20%" ALIGN = "LEFT">
                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="Supplier" value="editvendor"> 
                  &nbsp;  Edit Supplier</label>
                  
                  <TH WIDTH="20%" ALIGN = "LEFT">
                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="Supplier" value="importvendor"> 
                  &nbsp;  Import Supplier</label>
                  
                  <TR>
                  
                  <TH WIDTH="20%" ALIGN = "LEFT">
                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="Supplier" value="exportvendor"> 
                  &nbsp;  Export Supplier</label>
                  
                   <TH WIDTH="20%" ALIGN = "LEFT">
                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="Supplier" value="supplierdiscountsummary"> 
                  &nbsp; Supplier Discount</label>
                  
                  <TH WIDTH="20%" ALIGN = "LEFT">
                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="Supplier" value="importsupplierdiscount"> 
                  &nbsp; Import Supplier Discount</label> 
                  
                  <TH WIDTH="20%" ALIGN = "LEFT">
                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="Supplier" value="exportsupplierdiscount"> 
                  &nbsp; Export Supplier Discount</label>
                  
                  <TR>
                     
	                  <TH WIDTH="20%" ALIGN = "LEFT">
	                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="Supplier" value="supplierTypeSummary"> 
	                  &nbsp; Supplier Group</label>
	                  
	                  <TH WIDTH="20%" ALIGN = "LEFT">
	                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="Supplier" value="newsupplierType"> 
	                  &nbsp; New Supplier Group</label>
	                  
	                  <TH WIDTH="20%" ALIGN = "LEFT">
	                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="Supplier" value="editsupplierType"> 
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
	                 <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;" name="Customer" value="customersummry"> 
	                  &nbsp; Customer</label>
	                  
	                  <TH WIDTH="20%" ALIGN = "LEFT">
	                 <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;" name="Customer" value="newcustomer"> 
	                  &nbsp; New Customer</label>
	                  
	                  <TH WIDTH="20%" ALIGN = "LEFT">
	                 <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;" name="Customer" value="editcustomer" > 
	                  &nbsp; Edit Customer</label>
	                  
	                  <TH WIDTH="20%" ALIGN = "LEFT">
	                 <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;" name="Customer" value="importcustomer"> 
	                  &nbsp; Import Customer</label>
	                  
	                  <TR>
	                  
	                  <TH WIDTH="20%" ALIGN = "LEFT">
	                 <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;" name="Customer" value="exportcustomer"> 
	                  &nbsp; Export Customer</label>
	                  
	                  <TH WIDTH="20%" ALIGN = "LEFT">
	                 <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;" name="Customer" value="customerdiscountsummary"> 
	                   &nbsp; Customer Discount</label>
	                   
	                   <TH WIDTH="20%" ALIGN = "LEFT">
	                 <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;" name="Customer" value="importcustomerdiscount" > 
	                   &nbsp; Import Customer Discount</label>
	                   
	                   <TH WIDTH="20%" ALIGN = "LEFT">
	                 <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;" name="Customer" value="exportcustomerdiscount"> 
	                   &nbsp; Export Customer Discount</label>
	                  
	                  <TR>
	                  
	                   <TH WIDTH="20%" ALIGN = "LEFT">
	                 <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;" name="Customer" value="ctsummry"> 
	                 &nbsp; Customer Group</label>
	                 
	                 <TH WIDTH="20%" ALIGN = "LEFT">
	                 <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;" name="Customer" value="newct"> 
	                 &nbsp; New Customer Group</label>
	                 
	                 <TH WIDTH="20%" ALIGN = "LEFT">
	                 <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;" name="Customer" value="editct"> 
	                 &nbsp; Edit Customer Group</label>
	                 
	                 <% if(COMP_INDUSTRY.equals("Retail")) { %> 
	                 <TH WIDTH="20%" ALIGN = "LEFT">
	                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="Customer" value="AssignCus">    
	                  &nbsp; Assign User Customer</label>
	                  
	                  <TR>
	                  
	                  <TH WIDTH="20%" ALIGN = "LEFT">
	                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="Customer" value="Custproduct">    
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
                        <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="UserEmployee" value="employeesummry">
                        &nbsp; Employee</label>
                                              
                    <TH WIDTH="20%" ALIGN = "LEFT">
                        <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="UserEmployee" value="pnewemployee">
                        &nbsp; New Employee</label>
	                 
                    <TH WIDTH="20%" ALIGN = "LEFT">
                        <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="UserEmployee" value="peditemoloyee">
                        &nbsp; Edit Employee</label>
             
                     <TH WIDTH="20%" ALIGN = "LEFT">
                        <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="UserEmployee" value="pimportemployee">
                        &nbsp; Import Employee</label>
                        
             <TR>
                        
                          <TH WIDTH="20%" ALIGN = "LEFT">
	                 <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="UserEmployee" value="pexportmasterdata" >
	                 &nbsp;  Export Employee</label>
	                 </TH>

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
	                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SystemAdmin" value="importdata"> 
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
                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="OrderAdmin" value="ordtypesumry" >
                  &nbsp;  Order Type  </label>
                  
                  <TH WIDTH="20%" ALIGN = "LEFT">
                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="OrderAdmin" value="newordtype">
                  &nbsp;  New Order Type  </label>
                  
                  <TH WIDTH="20%" ALIGN = "LEFT">
                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="OrderAdmin" value="summarylnkordtype">
                  &nbsp;  Summary &#45; Order Type Link </label>
                  
                  <TH WIDTH="20%" ALIGN = "LEFT">
                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="OrderAdmin" value="editordtype">
                  &nbsp;  Edit Order Type  </label>
                  
                  <TR>
                  
                     <TH WIDTH="20%" ALIGN = "LEFT">
                     <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;" name="OrderAdmin" value="editpaymenttype" >
                      &nbsp; Payment Type </label>
                      
                      <TH WIDTH="20%" ALIGN = "LEFT">
                     <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;" name="OrderAdmin" value="newpaymenttype">
                      &nbsp; New Payment Type </label>
                      
                      <TH WIDTH="20%" ALIGN = "LEFT">
                     <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;" name="OrderAdmin" value="removepaymenttype">
                      &nbsp; Remove Payment Type </label>
                  
                   <!-- ***************************************************** -->        
                      <TH WIDTH="20%" ALIGN = "LEFT">
                     <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;" name="OrderAdmin" value="Paytermedit">
                      &nbsp; Payment Terms</label>
                   <TR>   
                      <TH WIDTH="20%" ALIGN = "LEFT">
                     <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;" name="OrderAdmin" value="Paytermnew">
                      &nbsp; New Payment Terms </label>
                      
                      <TH WIDTH="20%" ALIGN = "LEFT">
                     <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;" name="OrderAdmin" value="Paytermremove">
                      &nbsp; Remove Payment Terms </label>
                      
                      <TH WIDTH="20%" ALIGN = "LEFT">
                     <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;" name="OrderAdmin" value="PaymentModeedit">
                      &nbsp; Payment Mode </label>
                      
                      <TH WIDTH="20%" ALIGN = "LEFT">
                     <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;" name="OrderAdmin" value="PaymentModenew">
                      &nbsp; New Payment Mode </label>
                      
                   <TR>   
                   
                      <TH WIDTH="20%" ALIGN = "LEFT">
                     <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;" name="OrderAdmin" value="PaymentModeremove">
                      &nbsp; Remove Payment Mode </label>
         <!--**************************************************  --> 
                  
                   <TH WIDTH="20%" ALIGN = "LEFT">
                 <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;" name="OrderAdmin" value="gstSummry">
                  &nbsp; <%=taxbylabelordermanagement%> </label>
                  
                  <TH WIDTH="20%" ALIGN = "LEFT">
                 <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;" name="OrderAdmin" value="newgst">
                  &nbsp; New <%=taxbylabelordermanagement%> </label>
                  
                  <TH WIDTH="20%" ALIGN = "LEFT">
                 <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;" name="OrderAdmin" value="summarylnkgst">
                  &nbsp; Summary &#45; <%=taxbylabelordermanagement%> Link</label>
                  
               <TR>   
                  
                  <TH WIDTH="20%" ALIGN = "LEFT">
                 <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;" name="OrderAdmin" value="editgst">
                  &nbsp; Edit <%=taxbylabelordermanagement%> </label>
                            
                  <TH WIDTH="20%" ALIGN = "LEFT">
                 <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="OrderAdmin" value="cursummry">
                  &nbsp; Currency </label>
                  
                  <TH WIDTH="20%" ALIGN = "LEFT">
                 <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="OrderAdmin" value="newcurs">
                  &nbsp; New Currency </label>
                  
                  <TH WIDTH="20%" ALIGN = "LEFT">
                 <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="OrderAdmin" value="summarylnkcurs">
                  &nbsp; Summary &#45; Currency Link</label>
                
                <TR>
                  
                  <TH WIDTH="20%" ALIGN = "LEFT">
                 <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="OrderAdmin" value="editcurs">
                  &nbsp; Edit Currency </label>
                  
                  </TABLE>
        	  </div>
        </div> 
              
 
 <div class="row">
  		<div class="col-12 col-sm-12"><INPUT Type=Checkbox class="form-check-input"  style="border:0;" name="OrderAdminHS"  onclick="return checkAllOrderAdminHS(this.checked);">
                      &nbsp; Select/Unselect HSCODE / COO / Remarks / INCOTERM / Footer </div>
  </div>       
<div class="panel panel-default">
<div class="panel-heading" style="background: #eaeafa"><strong>HSCODE / COO / Remarks / INCOTERM / Footer </strong></div>
<div class="panel-body"> 
		<TABLE class="table1" style="font-size:14px;width: 100%;">
        	 <TR> 
                  
                    <TH WIDTH="20%" ALIGN = "LEFT">
                 <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="OrderAdminHS" value="edithscode">
                  &nbsp; HSCODE</label>
                  
                  <TH WIDTH="20%" ALIGN = "LEFT">
                 <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="OrderAdminHS" value="newhscodes" >
                  &nbsp; New HSCODE</label>
                  
                  <TH WIDTH="20%" ALIGN = "LEFT">
                 <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="OrderAdminHS" value="removehscodes" >
                  &nbsp; Remove HSCODE</label>
                  
                    <TH WIDTH="20%" ALIGN = "LEFT">
                 <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="OrderAdminHS" value="editcoo">
                  &nbsp; COO</label>
                  
                  <TR>
                  
                  <TH WIDTH="20%" ALIGN = "LEFT">
                 <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="OrderAdminHS" value="newcoos">
                  &nbsp; New COO</label>
                  
                  <TH WIDTH="20%" ALIGN = "LEFT">
                 <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="OrderAdminHS" value="removecoos">
                  &nbsp; Remove COO</label>
                  
                    <TH WIDTH="20%" ALIGN = "LEFT">
                <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;" name="OrderAdminHS" value="editorderremarks">
                  &nbsp; Remarks</label>
                  
                  <TH WIDTH="20%" ALIGN = "LEFT">
                <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;" name="OrderAdminHS" value="neworderremarks">
                  &nbsp; New Remarks</label>
                  
                  <TR>
                  
                  <TH WIDTH="20%" ALIGN = "LEFT">
                <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;" name="OrderAdminHS" value="removeorderremarks">
                  &nbsp; Remove Remarks</label>
                  
                    <TH WIDTH="20%" ALIGN = "LEFT">
                <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;" name="OrderAdminHS" value="editorderincoterms">
                  &nbsp; INCTORERM</label>
                  
                    <TH WIDTH="20%" ALIGN = "LEFT">
                <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;" name="OrderAdminHS" value="neworderincoterms">
                  &nbsp; New INCTORERM</label>
                  
                    <TH WIDTH="20%" ALIGN = "LEFT">
                <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;" name="OrderAdminHS" value="removeorderincoterms">
                  &nbsp; Remove INCTORERM</label>
                  
                  <TR> 
                  
                    <TH WIDTH="20%" ALIGN = "LEFT">
                 <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="OrderAdminHS" value="editorderfooter">
                  &nbsp; Footer</label>
                  
                  <TH WIDTH="20%" ALIGN = "LEFT">
                 <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="OrderAdminHS" value="neworderfooter">
                  &nbsp; New Footer</label>
                  
                  <TH WIDTH="20%" ALIGN = "LEFT">
                 <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="OrderAdminHS" value="removeorderfooter">
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
                 <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="Distribution" value="packingMasterSummary">
                  &nbsp; Packing List </label>
                  
                  <TH WIDTH="20%" ALIGN = "LEFT">
                 <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="Distribution" value="newpackingMaster">
                  &nbsp; New Packing List </label>
                  
                  <TH WIDTH="20%" ALIGN = "LEFT">
                 <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="Distribution" value="removepackingMaster">
                  &nbsp; Remove Packing List </label>
                  
                  <TH WIDTH="20%" ALIGN = "LEFT">
                 <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="Distribution" value="transport_mode">
                  &nbsp; Transport Mode </label>
                  
                  <TR>
                  
                  <TH WIDTH="20%" ALIGN = "LEFT">
                 <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="Distribution" value="newtransportmode">
                  &nbsp; New Transport Mode </label>
                  
                  <TH WIDTH="20%" ALIGN = "LEFT">
                 <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="Distribution" value="removetransportmode">
                  &nbsp; Remove Transport Mode </label>
                  
                  <TH WIDTH="20%" ALIGN = "LEFT">
                 <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="Distribution" value="clearagentsummary">
                  &nbsp; Clearing Agent </label>
                  
                  <TH WIDTH="20%" ALIGN = "LEFT">
                 <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="Distribution" value="newclearagent">
                  &nbsp; New Clearing Agent </label>
                  
                  <TR>
                  
                  <TH WIDTH="20%" ALIGN = "LEFT">
                 <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="Distribution" value="editclearagent">
                  &nbsp; Edit Clearing Agent </label>
                  
                  <TH WIDTH="20%" ALIGN = "LEFT">
                 <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="Distribution" value="summarylnkclearagent">
                  &nbsp; Summary Clearing Agent Link </label>
                  
                  <TH WIDTH="20%" ALIGN = "LEFT">
                 <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="Distribution" value="clearanceTypesummary">
                  &nbsp; Clearance Type </label>
                  
                  <TH WIDTH="20%" ALIGN = "LEFT">
                 <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="Distribution" value="newclearancetype">
                  &nbsp; New Clearance Type </label>
                  
                  <TR>
                  
                  <TH WIDTH="20%" ALIGN = "LEFT">
                 <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="Distribution" value="editclearancetype">
                  &nbsp; Edit Clearance Type </label>
                  
                  <TH WIDTH="20%" ALIGN = "LEFT">
                 <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="Distribution" value="summarylnkclearancetype">
                  &nbsp; Summary Clearance Type Link </label>
                  
                  <TH WIDTH="20%" ALIGN = "LEFT">
                 <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="Distribution" value="shippersummary">
                  &nbsp; Freight Forwarder </label>
                  
                  <TH WIDTH="20%" ALIGN = "LEFT">
                 <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="Distribution" value="shippernew">
                  &nbsp; New Freight Forwarder </label>
                  
                  <TR>
                  
                  <TH WIDTH="20%" ALIGN = "LEFT">
                 <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="Distribution" value="shipperedit">
                  &nbsp; Edit Freight Forwarder </label>
        	 
        	 
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
    	<TABLE class="table1" style="font-size:14px;width: 100%;">
	             <TR>
	             
	             <TH WIDTH="20%" ALIGN = "LEFT">
                    <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="OrderConfiguration" value="purchaseestimateconfig"  >
                    &nbsp; Purchase Estimate Configuration</label>
                         
	              <TH WIDTH="20%" ALIGN = "LEFT">
                    <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="OrderConfiguration" value="editibpt"  >
                    &nbsp; Edit Purchase Order Printout</label>            
                 
       				 <TH WIDTH="23%" ALIGN = "LEFT">
                    <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="OrderConfiguration" value="editibwithcostpt" >
                    &nbsp; Edit Purchase Order Printout (with cost)</label>
                    
                    <TH WIDTH="23%" ALIGN = "LEFT">
                     <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="OrderConfiguration" value="editInboundOrderMailMsg"  >
                     &nbsp; Edit Purchase Order Email Message</label>
                     
                    <TR>
                     
                    <TH WIDTH="23%" ALIGN = "LEFT">
                    <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="OrderConfiguration" value="editBillPrintout" >
                    &nbsp; Edit Bill Printout</label>
                  
                  <th WIDTH="23%" ALIGN = "LEFT">
                    <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="OrderConfiguration" value="email_config_bill" >
                    &nbsp; Edit Bill Email Message</label>   
                     
                     <TH WIDTH="23%" ALIGN = "LEFT">
                    <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="OrderConfiguration" value="editestwithpt" >
                    &nbsp; Edit Sales Estimate Order Printout (with price)</label>
                    
                    <TH WIDTH="23%" ALIGN = "LEFT">
                    <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="OrderConfiguration" value="editEstimateOrderMailMsg"  >
                    &nbsp; Edit Sales Estimate Order Email Message</label>
                     
                    <TR>
                   
                    <TH WIDTH="20%" ALIGN = "LEFT">
                    <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="OrderConfiguration" value="editobpt" >
                    &nbsp; Edit Sales Order Printout</label>
                  
                    <TH WIDTH="23%" ALIGN = "LEFT">
                    <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="OrderConfiguration" value="editobwithpt" >
                    &nbsp; Edit Sales Order Printout (with price)</label>
                     
                    <TH WIDTH="23%" ALIGN = "LEFT">
                    <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="OrderConfiguration" value="editOutboundOrderMailMsg"  >
                    &nbsp; Edit Sales Order Email Message</label>
                    
                 <!--    <TH WIDTH="20%" ALIGN = "LEFT">
                    <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="OrderConfiguration" value="editlopt" >
                    &nbsp; Edit Rental Order Printout</label> -->
                                   
                    <TH WIDTH="20%" ALIGN = "LEFT">
                    <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="OrderConfiguration" value="edittowop" >
                    &nbsp; Edit Consignment Order Printout</label>
                     
                    <TR>
                    
                    <!-- NAVAS -->
                     <TH WIDTH="20%" ALIGN = "LEFT">
                    <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="OrderConfiguration" value="edittopt" >
                    &nbsp; Edit Consignment Order Printout (With Price)</label>
                   
                    <TH WIDTH="20%" ALIGN = "LEFT">
                    <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="OrderConfiguration" value="editTransferOrderMailMsg"  >
                    &nbsp; Edit Consignment Order Email Message</label>
                    
                   <TH WIDTH="20%" ALIGN = "LEFT">
                    <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="OrderConfiguration" value="editTaxInvoiceMultiLanguage"  >
                    &nbsp; Edit Invoice Printout</label>
                    
                    <th WIDTH="23%" ALIGN = "LEFT">
                    <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="OrderConfiguration" value="email_config_invoice" >
                    &nbsp; Edit Invoice Email Message</label>
                     
                    <TR>
                    
                     <th WIDTH="23%" ALIGN = "LEFT">
                    <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="OrderConfiguration" value="email_config_delivery" >
                    &nbsp; Edit Delivery Email Message</label>
                    
                    <TH WIDTH="20%" ALIGN = "LEFT">
                    <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="OrderConfiguration" value="editRcptRecvHdr" >
                    &nbsp; Edit Goods Receipt Printout</label>
                                   
                    <TH WIDTH="20%" ALIGN = "LEFT">
                    <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="OrderConfiguration" value="editRcptIssueHdr" >
                    &nbsp; Edit Goods Issue Printout</label>
                  
                    <TH WIDTH="20%" ALIGN = "LEFT">
                    <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="OrderConfiguration" value="editMoveHdr"  >
                    &nbsp; Edit Stock Move Printout</label>
                        <TR>
                    <% if(COMP_INDUSTRY.equals("Retail")) { %> 
                     <TH WIDTH="20%" ALIGN = "LEFT">
                    <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="OrderConfiguration" value="goodsshiftclosereceiptprintout"  >
                    &nbsp; Edit POS Shift Close Receipt Printout</label>
                    <%} %>
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
    	<TABLE class="table1" style="font-size:14px;width: 100%;">
	             <TR>
	             <TH WIDTH="20%" ALIGN = "LEFT">
                      <INPUT Type=Checkbox  style="border:0;" name="EmailConfiguration" value="editemailconfig" >
                      &nbsp; Order Management Approval & Email Configuration 
	    </TABLE>
    </div>
</div> -->         
   <div class="row">
  		<div class="col-12 col-sm-12"><INPUT Type=Checkbox class="form-check-input"  style="border:0;" name="OrderManagement"  onclick="return checkAllOrderManagement(this.checked);">
                   &nbsp; Select/Unselect Order Management - Create Masters </div>
  </div>

<div class="panel panel-default">
    <div class="panel-heading" style="background: #eaeafa"><strong>Order Management - Create Masters</strong></div>
    <div class="panel-body"> 
    	<TABLE class="table1" style="font-size:14px;width: 100%;">
    	   	
                    <TR>
                   
                    <TH WIDTH="20%" ALIGN = "LEFT">
                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="OrderManagement" value="popproduct" >
                  &nbsp; Create Product Details &#45; Purchase, Bill, Sales Estimate, Sales,Invoice</label>
                   
                    <TH WIDTH="20%" ALIGN = "LEFT">
                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="OrderManagement" value="popsupplier" >
                  &nbsp; Create Supplier Details &#45; Purchase Order</label>
                  
                   <TH WIDTH="20%" ALIGN = "LEFT">
                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="OrderManagement" value="suppliertypepopup">
                  &nbsp; Create Supplier Type - Purchase Order</label>
                  
                  <TH WIDTH="20%" ALIGN = "LEFT">
                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="OrderManagement" value="popcustomer" >
                  &nbsp; Create Customer Details &#45; Sales Estimate, Sales,Invoice</label>
                
                 <TR> 
                 <TH WIDTH="20%" ALIGN = "LEFT">
                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="OrderManagement" value="customertypepopup">
                  &nbsp;Create Customer Type - Sales Estimate, Sales,Invoice</label>
                 
				  <TH WIDTH="20%" ALIGN = "LEFT">
                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="OrderManagement" value="ordertypepopup">
                  &nbsp;Create Order Type - Purchase, Bill, Sales Estimate, Sales,Invoice</label>
				                    
                    <TH WIDTH="20%" ALIGN = "LEFT">
                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="OrderManagement" value="paymenttypepopup">
                  &nbsp;Payment Type - Purchase, Bill, Sales Estimate, Sales,Invoice</label>
                    
             </TABLE>
    </div>
</div>
<!-- imti project -->
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
                   <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;" name="Project" value="summaryproject">
                    &nbsp; Project</label>
             
             		<TH WIDTH="20%" ALIGN = "LEFT">
                     <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="Project" value="newproject" >
                     &nbsp; New</label>
                     
                    <TH WIDTH="20%" ALIGN = "LEFT">
	                 <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="Project" value="summarylnkproject">
	                 &nbsp; Summary &#45; Project Number Link</label>
                     
                    <TH WIDTH="20%" ALIGN = "LEFT">
                     <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="Project" value="editproject">
                     &nbsp; Edit</label>
                        
                     <TR>
                        
                    <TH WIDTH="20%" ALIGN = "LEFT">
                     <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="Project" value="exportproject">
                     &nbsp; Export</label>
                     
                     <TH WIDTH="20%" ALIGN = "LEFT">
                     <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="Project" value="deleteproject">
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
                   <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;" name="Purchaseestimate" value="summarypurchaseestimate">
                    &nbsp; Muliple Purchase Estimate</label>
             
             		<TH WIDTH="20%" ALIGN = "LEFT">
                     <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="Purchaseestimate" value="newpurchaseestimate" >
                     &nbsp; New</label>
                     
                    <TH WIDTH="20%" ALIGN = "LEFT">
	                 <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="Purchaseestimate" value="summarylnkpurchaseestimate">
	                 &nbsp; Summary &#45; Multi Purchase Estimate Number Link</label>
                     
                    <TH WIDTH="20%" ALIGN = "LEFT">
                     <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="Purchaseestimate" value="editpurchaseestimate">
                     &nbsp; Edit</label>
                        
                     <TR>
                        
                    <TH WIDTH="20%" ALIGN = "LEFT">
                     <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="Purchaseestimate" value="morepurchaseestimate">
                     &nbsp; More</label>
                                     
					<TH WIDTH="20%" ALIGN = "LEFT">
                    <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;" name="Purchaseestimate" value="summarypurchaseorderestimate">
                    &nbsp; Purchase Estimate</label>
                    
                    <TH WIDTH="20%" ALIGN = "LEFT">
                     <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="Purchaseestimate" value="newpurchaseorderestimate" >
                     &nbsp; New</label>
                     
                    <TH WIDTH="20%" ALIGN = "LEFT">
	                 <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="Purchaseestimate" value="summarylnkpurchaseorderest">
	                 &nbsp; Summary &#45; Multiple Purchase Estimate Number Link</label>
                        
                     <TR>                
                     
                    <TH WIDTH="20%" ALIGN = "LEFT">
                     <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="Purchaseestimate" value="editpurchaseorderestimate">
                     &nbsp; Edit</label>
                     
                     <TH WIDTH="20%" ALIGN = "LEFT">
                     <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="Purchaseestimate" value="printpurchaseorderestimate">
                     &nbsp; Print</label>
                    
                    <TH WIDTH="20%" ALIGN = "LEFT">
                     <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="Purchaseestimate" value="morepurchaseorderestimate">
                     &nbsp; More</label>
                    
                    <TH WIDTH="20%" ALIGN = "LEFT">
                     <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="Purchaseestimate" value="reordersummary">
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
                   <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;" name="OrderManagement1" value="summarypurchaseorder">
                    &nbsp; Purchase Order</label>
             
             <TH WIDTH="20%" ALIGN = "LEFT">
                     <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="OrderManagement1" value="newpurchaseorder" >
                     &nbsp; New</label>
                     
                    <TH WIDTH="20%" ALIGN = "LEFT">
	                 <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="OrderManagement1" value="summarylnkpurchaseorder">
	                 &nbsp; Summary &#45; Order Number Link</label>
                     
                    <TH WIDTH="20%" ALIGN = "LEFT">
                     <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="OrderManagement1" value="editpurchaseorder">
                     &nbsp; Edit</label>
                        
                     <TR>
                        
                    <TH WIDTH="20%" ALIGN = "LEFT">
                     <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="OrderManagement1" value="printpurchaseorder">
                     &nbsp; PDF/Print</label> 
                       
                    <TH WIDTH="20%" ALIGN = "LEFT">
                     <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="OrderManagement1" value="morepurchaseorder">
                     &nbsp; More</label>
                                     
                    <TH WIDTH="20%" ALIGN = "LEFT">
                    <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="OrderManagement1" value="ibsummary">
                    &nbsp; Summary &#45; Purchase Order Details    </label>                
                    
                     <TH WIDTH="20%" ALIGN = "LEFT">
                    <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="OrderManagement1" value="summarylnkibsummary">
                    &nbsp; Summary &#45; Purchase Order Details Number Link</label>
                        
                     <TR>                
                    
                     <TH WIDTH="20%" ALIGN = "LEFT">
                    <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="OrderManagement1" value="ibsummarycost">
                    &nbsp; Summary &#45; Purchase Order Details (with cost)</label>                
                    
                     <TH WIDTH="20%" ALIGN = "LEFT">
                    <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="OrderManagement1" value="summarylnkibsummarycost">
                    &nbsp; Summary &#45; Purchase Order Details Number Link (with cost)</label>
                    
                    <%-- <TH WIDTH="20%" ALIGN = "LEFT">
                   <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;" name="OrderManagement1" value="maintinb">
                    &nbsp; Edit Purchase Order Details</label> --%>                     
                  
                   <TH WIDTH="20%" ALIGN = "LEFT">
                    <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="OrderManagement1" value="orderClose" >
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
                        <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="PurchaseTransaction" value="purchaseTransactionDashboard" >
                        &nbsp; Purchase Transaction</label>  
             
             			<TH WIDTH="20%" ALIGN = "LEFT">
                        <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="PurchaseTransaction" value="ibrecvbulk" >
                        &nbsp; Purchase Order Receipt</label> 
                                                                                            
                        <TH WIDTH="20%" ALIGN = "LEFT">
                        <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="PurchaseTransaction" value="inbrectbyrange"   >
                        &nbsp; Purchase Order Receipt (by serial)</label>
                        
                       <TH WIDTH="20%" ALIGN = "LEFT">
                        <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="PurchaseTransaction" value="inbRecbyprd">
                        &nbsp; Purchase Order Receipt (by product)</label>
                        
                         <TR>
                         
                        <TH WIDTH="20%" ALIGN = "LEFT">
                        <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="PurchaseTransaction" value="ibrecvmultiple" >
                        &nbsp; Purchase Order Receipt (multiple)</label>
                        
                       
                        
                        <!--  <TH WIDTH="20%" ALIGN = "LEFT">
                        <INPUT Type=Checkbox  style="border:0;" name="OrderManagement1" value="inbReverse"  >
                        &nbsp; Purchase Order Reversal -->                        
                                    
                        <TH WIDTH="20%" ALIGN = "LEFT">
                        <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="PurchaseTransaction" value="mulmiscrecipt"   >
                        &nbsp; Goods Receipt</label> 
                        
                         <TH WIDTH="20%" ALIGN = "LEFT">
                        <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="PurchaseTransaction" value="inbMiscRceiptByRange"   >
                        &nbsp; Goods Receipt (by serial)</label>
                            
                        <TH WIDTH="20%" ALIGN = "LEFT">
                         <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="PurchaseTransaction" value="editexpiry">
                         &nbsp; Edit Inventory Expire Date</label>
                        </TH>
             
             </TABLE>
             </div>
        </div>

<div class="row">
<div class="col-12 col-sm-12"><INPUT Type=Checkbox class="form-check-input"  style="border:0;" name="expenses"  onclick="return checkAllexpenses(this.checked);">
                        &nbsp; Select/Unselect Expenses </div>
  </div>        
        <div class="panel panel-default">
<div class="panel-heading" style="background: #eaeafa"><strong>Expenses</strong></div>
<div class="panel-body">
       <TABLE class="table1" style="font-size:14px;width: 100%;">
                    <TR>
                    
                    <TH WIDTH="20%" ALIGN = "LEFT">
	                 <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="expenses" value="expensesSummary" >     
	                 &nbsp; Expenses</label>
	                 
	                <TH WIDTH="20%" ALIGN = "LEFT">
                     <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="expenses" value="newexpenses" >
                     &nbsp; New</label> 
	                
	                <TH WIDTH="20%" ALIGN = "LEFT">
	                 <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="expenses" value="summarylnkexpenses" >     
	                 &nbsp; Summary &#45; Expenses Number Link</label>
	                 
	                 <TH WIDTH="20%" ALIGN = "LEFT">
                     <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="expenses" value="exportexpense" >
                     &nbsp; Export</label> 
	                 
	                 
	               
                     
                    <TR> 
                    
                     <TH WIDTH="20%" ALIGN = "LEFT">
                     <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="expenses" value="editexpenses" >
                     &nbsp; Edit</label>
	                
	                <TH WIDTH="20%" ALIGN = "LEFT">
                     <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="expenses" value="printexpenses" >
                     &nbsp; PDF/Print</label>
                     
                     <TH WIDTH="20%" ALIGN = "LEFT">
                     <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="expenses" value="convertexpenses">
                     &nbsp; Convert to Invoice</label>
                     
                    <TH WIDTH="20%" ALIGN = "LEFT">
                     <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="expenses" value="moreexpenses">
                     &nbsp; More</label>  
		</TABLE>
    </div>
 </div>

<div class="row">
<div class="col-12 col-sm-12"><INPUT Type=Checkbox class="form-check-input"  style="border:0;" name="GoodsReceipt"  onclick="return checkAllGoodsReceipt(this.checked);">
                        &nbsp; Select/Unselect Goods Receipt </div>
  </div>
<div class="panel panel-default">
<div class="panel-heading" style="background: #eaeafa"><strong>Goods Receipt</strong></div>
<div class="panel-body">
       <TABLE class="table1" style="font-size:14px;width: 100%;">
                    <TR>
                    
                    <TH WIDTH="20%" ALIGN = "LEFT">
	                 <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="GoodsReceipt" value="grnSummary" >     
	                 &nbsp; Goods Receipt</label>
	                
	                <TH WIDTH="20%" ALIGN = "LEFT">
	                 <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="GoodsReceipt" value="summarylnkgrn">     
	                 &nbsp; Summary &#45; Order Number Link</label>
	                 
	                 <TH WIDTH="20%" ALIGN = "LEFT">
	                 <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="GoodsReceipt" value="exportgrn"  >     
	                 &nbsp; Export</label>
	                
	                <TH WIDTH="20%" ALIGN = "LEFT">
                     <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="GoodsReceipt" value="printgrn">
                     &nbsp; PDF/Print</label>
                     
                     <TR>
                     
                     <TH WIDTH="20%" ALIGN = "LEFT">
                     <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="GoodsReceipt" value="convertgrn" >
                     &nbsp; Convert to Bill</label> 
		</TABLE>
    </div>
 </div>

<div class="row">
<div class="col-12 col-sm-12"><INPUT Type=Checkbox class="form-check-input"  style="border:0;" name="bill"  onclick="return checkAllbill(this.checked);">
                        &nbsp; Select/Unselect Bill </div>
  </div>
 <div class="panel panel-default">
<div class="panel-heading" style="background: #eaeafa"><strong>Bill</strong></div>
<div class="panel-body">
       <TABLE class="table1" style="font-size:14px;width: 100%;">
                    <TR>
                    
                    <TH WIDTH="20%" ALIGN = "LEFT">
	                 <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="bill" value="billSummary">     
	                 &nbsp; Bill</label>
	                 
	                <TH WIDTH="20%" ALIGN = "LEFT">
                     <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="bill" value="newbill">
                     &nbsp; New</label>
                     
                    <TH WIDTH="20%" ALIGN = "LEFT">
	                 <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="bill" value="summarylnkbill">
	                 &nbsp; Summary &#45; Bill Number Link</label>
	                 
	                 <TH WIDTH="20%" ALIGN = "LEFT">
                     <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="bill" value="exportbill" >
                     &nbsp; Export</label>
                     
                   
                    
	                <TR>
	                
	                 <TH WIDTH="20%" ALIGN = "LEFT">
                     <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="bill" value="editbill" >
                     &nbsp; Edit</label>
                        
                    <TH WIDTH="20%" ALIGN = "LEFT">
                     <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="bill" value="printbill">
                     &nbsp; PDF/Print</label>
                     
                     <TH WIDTH="20%" ALIGN = "LEFT">
                     <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="bill" value="applycreditsbill">
                     &nbsp; Apply Credits</label>
                     
                     <TH WIDTH="20%" ALIGN = "LEFT">
                     <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="bill" value="recordpaymentbill">
                     &nbsp; Record Payment</label> 
                       
                     <TR>  
                       
                    <TH WIDTH="20%" ALIGN = "LEFT">
                     <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="bill" value="morebill">
                     &nbsp; More</label> 
                    
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
	                 <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="PurchaseReturn" value="POReturnSummary" >     
	                 &nbsp; Purchase Return</label>
	                 
	                <TH WIDTH="20%" ALIGN = "LEFT">
	                 <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="PurchaseReturn" value="newpurchasereturn">     
	                 &nbsp; New</label>
	                 
	                <TH WIDTH="20%" ALIGN = "LEFT">
	                 <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="PurchaseReturn" value="summarylnkpurchasereturn">
	                 &nbsp; Summary &#45; Return Number Link</label>
	                 
	                 <TH WIDTH="20%" ALIGN = "LEFT">
	                 <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="PurchaseReturn" value="exportpurchasereturn">     
	                 &nbsp; Export</label>
	                 
	               
	                 
	                 <TR>
	                 
	                  <TH WIDTH="20%" ALIGN = "LEFT">
	                 <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="PurchaseReturn" value="excelpurchasereturn">     
	                 &nbsp; Export Excel</label>
	                 
	                <TH WIDTH="20%" ALIGN = "LEFT">
	                 <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="PurchaseReturn" value="applycreditnotepurchasereturn">     
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
	                 <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="ProductReturn" value="productreturn" >     
	                 &nbsp; Product Return</label>
	                 
	                <TH WIDTH="20%" ALIGN = "LEFT">
	                 <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="ProductReturn" value="newproductreturn">     
	                 &nbsp; New</label>
	                 
	                <TH WIDTH="20%" ALIGN = "LEFT">
	                 <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="ProductReturn" value="summarylnkproductreturn">
	                 &nbsp; Summary &#45; Return Number Link</label>
	                 
	                 <TH WIDTH="20%" ALIGN = "LEFT">
	                 <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="ProductReturn" value="exportproductreturn">     
	                 &nbsp; Export</label>
	                 
	                 <TR>
	                 
		</TABLE>
     </div>
 </div>
 
 <div class="row">
<div class="col-12 col-sm-12"><INPUT Type=Checkbox class="form-check-input"  style="border:0;" name="SupplierCreditNotes"  onclick="return checkAllSupplierCreditNotes(this.checked);">
                        &nbsp; Select/Unselect Debit Note </div>
  </div> 
<div class="panel panel-default">
<div class="panel-heading" style="background: #eaeafa"><strong>Debit Note</strong></div>
<div class="panel-body">
       <TABLE class="table1" style="font-size:14px;width: 100%;">
                    <TR>
                    
                    <TH WIDTH="20%" ALIGN = "LEFT">
	                 <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SupplierCreditNotes" value="supplierCreditNote" >     
	                 &nbsp; Debit Note</label>
	                 
	                <TH WIDTH="20%" ALIGN = "LEFT">
	                 <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SupplierCreditNotes" value="newSupplierCreditNote" >     
	                 &nbsp; New</label>
	                 
	                <TH WIDTH="20%" ALIGN = "LEFT">
	                 <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SupplierCreditNotes" value="summarylnkSupplierCreditNote" >
	                 &nbsp; Summary &#45; Debit Number Link</label>
	                 
	                 <TH WIDTH="20%" ALIGN = "LEFT">
	                 <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SupplierCreditNotes" value="exportSupplierCreditNote" >     
	                 &nbsp; Export</label>
	                 
	               
                     
                    <TR> 
                    
                     <TH WIDTH="20%" ALIGN = "LEFT">
                     <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SupplierCreditNotes" value="editSupplierCreditNote" >
                     &nbsp; Edit</label>
                        
                    <TH WIDTH="20%" ALIGN = "LEFT">
                     <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SupplierCreditNotes" value="printSupplierCreditNote" >
                     &nbsp; PDF/Print</label>
                     
                    <TH WIDTH="20%" ALIGN = "LEFT">
                     <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SupplierCreditNotes" value="moreSupplierCreditNote" >
                     &nbsp; More</label>   
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
	                        	<label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="PurchaseReports" value="ibordersumry" >
	                        	&nbsp;  Purchase Order Summary Details</label>
	                        </TH>
	                        
                    		<TH WIDTH="20%" ALIGN = "LEFT">
	                        	<label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="PurchaseReports" value="suppliersummary" >
	                        	&nbsp;  Purchase Order Summary(by supplier)</label>
	                        </TH>                        
                       
	                          <TH WIDTH="20%" ALIGN = "LEFT">
	                        	<label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="PurchaseReports" value="ibordersumrywreccost" >
	                        	&nbsp;  Purchase Order Summary Details(by cost)</label>
	                          </TH> 
                     
	                          <TH WIDTH="20%" ALIGN = "LEFT">
	                        	<label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="PurchaseReports" value="recvdibsummry"   >
	                        	&nbsp;  Purchase Order Summary(by cost)</label>
	                          </TH>	                        
	                        
	                         <TR>
	                         
	                          <TH WIDTH="20%" ALIGN = "LEFT">
	                        	<label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="PurchaseReports"value="printpo"   >
	                        	&nbsp;  Generate PDF (without cost)</label>
	                        </TH>	                        
	                        	
	                        <TH WIDTH="20%" ALIGN = "LEFT">
	                        	<label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="PurchaseReports"value="printpoinv"  >
	                        	&nbsp;  Generate PDF (with cost)</label>
	                        </TH>
	                        
	                        
	                        <TH WIDTH="20%" ALIGN = "LEFT">
	                        	<label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="PurchaseReports" value="recvsummry"   >
	                        	&nbsp;  Order Receipt Summary</label>
	                        </TH>
	                        
	                        
	                         <TH WIDTH="20%" ALIGN = "LEFT">
	                        	<label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="PurchaseReports" value="recvsummrywithcost"   >
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
                    <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SalesEstimate" value="summarysalesestimate">
                    &nbsp; Sales Estimate Order</label>
                    
                    <TH WIDTH="20%" ALIGN = "LEFT">
                     <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SalesEstimate" value="newsalesestimate">
                     &nbsp; New</label>
                     
                    <TH WIDTH="20%" ALIGN = "LEFT">
	                 <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SalesEstimate" value="summarylnksalesestimate">
	                 &nbsp; Summary &#45; Order Number Link</label>
                     
                    <TH WIDTH="20%" ALIGN = "LEFT">
                     <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SalesEstimate" value="editsalesestimate">
                     &nbsp; Edit</label>
                        
                     <TR>
                        
                    <TH WIDTH="20%" ALIGN = "LEFT">
                     <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SalesEstimate" value="printsalesestimate">
                     &nbsp; PDF/Print</label> 
                       
                    <TH WIDTH="20%" ALIGN = "LEFT">
                     <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SalesEstimate" value="moresalesestimate">
                     &nbsp; More</label>                       
                       
                    <TH WIDTH="20%" ALIGN = "LEFT">
                    <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SalesEstimate" value="estsummarywithoutprice">
                    &nbsp; Summary &#45; Sales Estimate Order Details </label>
                    
                    <TH WIDTH="20%" ALIGN = "LEFT">
                    <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SalesEstimate" value="summarylnkestsummarywhoutprice">
                    &nbsp; Summary &#45; Sales Estimate Order Number Link </label>
                        
                     <TR>
                    
                    <TH WIDTH="20%" ALIGN = "LEFT">
                   <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;" name="SalesEstimate" value="estsummary">
                    &nbsp; Summary &#45; Sales Estimate Order Details (with price)</label>
                    
                    <TH WIDTH="20%" ALIGN = "LEFT">
                   <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;" name="SalesEstimate" value="summarylnkestsummary">
                    &nbsp; Summary &#45; Sales Estimate Order Number Link (with price)</label>                    
                                              
                    <%-- <TH WIDTH="20%" ALIGN = "LEFT">
                   <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;" name="SalesEstimate" value="maintest">
                    &nbsp;  Edit Sales Estimate Order Details</label> --%>
                    
                    <TH WIDTH="20%" ALIGN = "LEFT">
                    <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SalesEstimate" value="convertOB">
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
	               <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;" name="SalesEstimateReports" value="estordsmrywithoutprice">
	                &nbsp;  Sales Estimate Order Summary Details</label>
	                </TH>
	                        
	                <TH WIDTH="20%" ALIGN = "LEFT">
	               <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;" name="SalesEstimateReports" value="estordsmrywissprice">
	                &nbsp;  Sales Estimate Order Summary Details(by price)</label>
	                </TH>

					<!-- Don't Remove Sales Counter, Temp. Blocked on 12.1.22 By Azees-->
					<!-- <TH WIDTH="20%" ALIGN = "LEFT">
	               <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;" name="SalesEstimateReports" value="summaryalternatbrandproduct">
	                &nbsp; Sales Counter</label>
                    </TH>
                                                           
                    	<TH WIDTH="20%" ALIGN = "LEFT">
		                <label class="checkbox-inline">    <INPUT Type=Checkbox  style="border:0;" name="SalesEstimateReports" value="salescounterconvtoest" >
		                    &nbsp; Sales Counter Convert To Estimate</label>
	                    </TH>					
                    
                    <TR>
                        
                     	<TH WIDTH="20%" ALIGN = "LEFT">
			              <label class="checkbox-inline">   <INPUT Type=Checkbox  style="border:0;" name="SalesEstimateReports" value="salescounterconvtoinvc">
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
                    <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SalesOrder" value="summarysalesorder">
                    &nbsp; Sales Order</label>
	                 
                    <TH WIDTH="20%" ALIGN = "LEFT">
                     <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SalesOrder" value="newsalesorder">
                     &nbsp; New</label>
                     
                    <TH WIDTH="20%" ALIGN = "LEFT">
	                 <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SalesOrder" value="summarylnksalesorder">
	                 &nbsp; Summary &#45; Order Number Link</label>
                     
                     <TH WIDTH="20%" ALIGN = "LEFT">
                     <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SalesOrder" value="exportforsalesorder">
                     &nbsp; Export</label>
                                             
                     <TR>
                        
                     <TH WIDTH="20%" ALIGN = "LEFT">
                     <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SalesOrder" value="editsalesorder">
                     &nbsp; Edit</label>
                     
                    <TH WIDTH="20%" ALIGN = "LEFT">
                     <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SalesOrder" value="printsalesorder">
                     &nbsp; PDF/Print</label> 
                       
                    <TH WIDTH="20%" ALIGN = "LEFT">
                     <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SalesOrder" value="moresalesorder">
                     &nbsp; More</label>
                     
                    <TH WIDTH="20%" ALIGN = "LEFT">
                    <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SalesOrder" value="printOB">
                    &nbsp; Summary &#45; Sales Order </label>                   
                                        
                    <TR>                     
                    
                    <TH WIDTH="20%" ALIGN = "LEFT">
                    <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SalesOrder" value="summarylnkprintOB">
                    &nbsp; Summary &#45; Sales Order Number Link</label>
                    
                    <TH WIDTH="20%" ALIGN = "LEFT">
                    <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SalesOrder" value="PrintOBInvoice">
                    &nbsp; Summary &#45; Sales Order (with price)</label>
                    
                    <TH WIDTH="20%" ALIGN = "LEFT">
                    <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SalesOrder" value="summarylnkPrintOBInvoice">
                    &nbsp; Summary &#45; Sales Order Number Link (with price)</label>
                 
                    <TH WIDTH="20%" ALIGN = "LEFT">
                    <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SalesOrder" value="obsummary" >
                    &nbsp; Summary &#45; Sales Order Details</label>
                                                          
                    <TR>
                    
                    <TH WIDTH="20%" ALIGN = "LEFT">
                    <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SalesOrder" value="obsummaryprice">
                    &nbsp; Summary &#45; Sales Order Details (with price)</label>
                    
                    <TH WIDTH="20%" ALIGN = "LEFT">
                    <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SalesOrder" value="orderCloseSales" >
                    &nbsp; Close Outstanding Sales Order</label>
       </TABLE>
    </div>
</div>             

<div class="row">
  		<div class="col-12 col-sm-12"><INPUT Type=Checkbox class="form-check-input"  style="border:0;" name="SalesTransaction"  onclick="return checkAllSalesTransaction(this.checked);">
                       &nbsp; Select/Unselect Sales Transaction </div>
  </div>

<div class="panel panel-default">
    <div class="panel-heading" style="background: #eaeafa"><strong>Sales Transaction</strong></div>
    <div class="panel-body">
    <TABLE class="table1" style="font-size:14px;width: 100%;">
    				<TR>
                           <TH WIDTH="20%" ALIGN = "LEFT">
	                        <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SalesTransaction" value="salesTransactionDashboard"   >
	                        &nbsp;  Sales Transaction</label> 
                                       
                    
	                       <TH WIDTH="20%" ALIGN = "LEFT">
	                        <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SalesTransaction" value="outbndmulPicking"   >
	                        &nbsp;  Sales Order Pick</label> 
	                        
	                       <TH WIDTH="20%" ALIGN = "LEFT">
	                        <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SalesTransaction" value="obpickingbyRange"   >
	                        &nbsp;  Sales Order Pick (by serial)</label>
	                        
	                         <TH WIDTH="20%" ALIGN = "LEFT">
	                        <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SalesTransaction" value="outbndIssuing" >
	                        &nbsp; Sales Order Issue</label>
	                          <TR> 
	                         <TH WIDTH="20%" ALIGN = "LEFT">
                            <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SalesTransaction" value="obpickissuebulk" >
                            &nbsp;  Sales Order Pick & Issue</label>
	                        
	                        	  
                               
                            <TH WIDTH="20%" ALIGN = "LEFT">
	                        <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SalesTransaction" value="obpickissuebyprd" >
	                        &nbsp;  Sales Order Pick/Issue (by product)</label>                          
                                                                            
	                       <TH WIDTH="20%" ALIGN = "LEFT">
	                       <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;" name="SalesTransaction" value="obpickissuemultiple" >
	                        &nbsp;  Sales Order Pick & Issue (multiple)</label>
	                       	                                             
		                    <TH WIDTH="20%" ALIGN = "LEFT">
		                    <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SalesTransaction" value="outbndPkrev"   >
		                    &nbsp; Sales Order Pick Return	</label>	                    
		                    	<TR>
	                     
	                         <!-- <TH WIDTH="20%" ALIGN = "LEFT">
		                     <INPUT Type=Checkbox  style="border:0;" name="SalesTransaction" value="obissuereversal" >
		                     &nbsp;  Sales Order Pick & Issue Reversal -->
                          
                            <TH WIDTH="20%" ALIGN = "LEFT">
		                    <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SalesTransaction" value="mulmiscissue"   >
		                    &nbsp; Goods Issue</label>                        

						
				  		
		                   <TH WIDTH="20%" ALIGN = "LEFT">
		                   <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SalesTransaction" value="ubmiscissueByRange"  >
		                   &nbsp; Goods Issue (by serial) </label> 
		                   		                   
		                   <TH WIDTH="20%" ALIGN = "LEFT">
                    	   <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SalesTransaction" value="packingSummary" >
                    	   &nbsp; Packing List and Deliver Note </label> 
                    	   
		                   
	                       	              	    
	             </TR>         	
              </TABLE>
    </div>
</div>

<div class="row">
  		<div class="col-12 col-sm-12"><INPUT Type=Checkbox class="form-check-input"  style="border:0;" name="GoodsIssued"  onclick="return checkAllGoodsIssued(this.checked);">
                        &nbsp; Select/Unselect Goods Issued </div>
  </div> 
<div class="panel panel-default">
<div class="panel-heading" style="background: #eaeafa"><strong>Goods Issued</strong></div>
<div class="panel-body"> 
		<TABLE class="table1" style="font-size:14px;width: 100%;">
        	 <TR>
        	 
        	 <TH WIDTH="20%" ALIGN = "LEFT">
	                 <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="GoodsIssued" value="ginotoinvoiceSummary">     
	                 &nbsp;  Goods Issued</label>
	                
	                <TH WIDTH="20%" ALIGN = "LEFT">
	                 <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="GoodsIssued" value="summarylnkginotoinvoice" >     
	                 &nbsp; Summary &#45; Order Number Link</label>
	                
	                 <TH WIDTH="20%" ALIGN = "LEFT">
                     <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="GoodsIssued" value="exportlnkginotoinvoice">
                     &nbsp; Export</label>
                     
	                <TH WIDTH="20%" ALIGN = "LEFT">
                     <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="GoodsIssued" value="printginotoinvoice">
                     &nbsp; PDF/Print</label>
                     
                     <TR>
                     
                     <TH WIDTH="20%" ALIGN = "LEFT">
                     <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="GoodsIssued" value="convertginotoinvoice">
                     &nbsp; Convert to Invoice</label> 
        	 
        	          	
              </TABLE>
    </div>
</div>

<div class="row">
  		<div class="col-12 col-sm-12"><INPUT Type=Checkbox class="form-check-input"  style="border:0;" name="Invoice"  onclick="return checkAllInvoice(this.checked);">
                        &nbsp; Select/Unselect Invoice </div>
  </div> 
<div class="panel panel-default">
<div class="panel-heading" style="background: #eaeafa"><strong>Invoice</strong></div>
<div class="panel-body"> 
		<TABLE class="table1" style="font-size:14px;width: 100%;">
        	 <TR>
        	 
        	 
                    <TH WIDTH="20%" ALIGN = "LEFT">
	                 <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="Invoice" value="invoiceSummary" >     
	                 &nbsp; Invoice</label>
	                 
	                <TH WIDTH="20%" ALIGN = "LEFT">
                     <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="Invoice" value="newinvoice" >
                     &nbsp; New</label>
                     
                    <!-- <TH WIDTH="20%" ALIGN = "LEFT">
	                 <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="Invoice" value="invoiceByOpen" >     
	                 &nbsp;  Invoice By Open</label>
	                        
	                <TH WIDTH="20%" ALIGN = "LEFT">
	                 <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="Invoice" value="invoiceByDraft">     
	                 &nbsp; Invoice By Draft</label> -->
                     
                    <TH WIDTH="20%" ALIGN = "LEFT">
	                 <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="Invoice" value="summarylnkinvoice" >
	                 &nbsp; Summary &#45; Invoice Number Link</label>
	                 
	                 <TH WIDTH="20%" ALIGN = "LEFT">
                     <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="Invoice" value="exportinvoice" >
                     &nbsp; Export</label>
	                        
	                <TR>
                     
                    <TH WIDTH="20%" ALIGN = "LEFT">
                     <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="Invoice" value="editinvoice" >
                     &nbsp; Edit</label>
                        
                    <TH WIDTH="20%" ALIGN = "LEFT">
                     <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="Invoice" value="printwtinvoice">
                     &nbsp; PDF/Print</label>
                     
                      <TH WIDTH="20%" ALIGN = "LEFT">
                     <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="Invoice" value="applycreditsinvoice">
                     &nbsp; Apply Credits</label>
                     
                     <TH WIDTH="20%" ALIGN = "LEFT">
                     <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="Invoice" value="recordpaymentinvoice">
                     &nbsp; Record Payment</label>
	                        
	                <TR> 
                       
                    <TH WIDTH="20%" ALIGN = "LEFT">
                     <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="Invoice" value="moreinvoice" >
                     &nbsp; More</label> 
        	 
        	          	
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
	                 <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SalesReturn" value="SOReturnSummary">     
	                 &nbsp; Sales Return</label>
	                 
	                <TH WIDTH="20%" ALIGN = "LEFT">
	                 <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SalesReturn" value="newsalesreturn" >     
	                 &nbsp; New</label>
	                 
	                <TH WIDTH="20%" ALIGN = "LEFT">
	                 <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SalesReturn" value="summarylnksalesreturn">
	                 &nbsp; Summary &#45; Return Number Link</label>
	                 
	                  <TH WIDTH="20%" ALIGN = "LEFT">
	                 <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SalesReturn" value="exportsalesreturn" >     
	                 &nbsp; Export</label>
	                 
	                
	                 
	                 <TR>
	                 
	                 <TH WIDTH="20%" ALIGN = "LEFT">
	                 <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SalesReturn" value="excelsalesreturn" >     
	                 &nbsp; Export Excel</label>
	                 
	                <TH WIDTH="20%" ALIGN = "LEFT">
	                 <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SalesReturn" value="applycreditnotesalesreturn">     
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
	                 <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="ProductReturnReceive" value="productreceive" >     
	                 &nbsp; Product Return Receive</label>
	                 
	                <TH WIDTH="20%" ALIGN = "LEFT">
	                 <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="ProductReturnReceive" value="summarylnkproductreceive">
	                 &nbsp; Summary &#45; Return Number Link</label>
	                 
	                 <TH WIDTH="20%" ALIGN = "LEFT">
	                 <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="ProductReturn" value="exportproductreceive">     
	                 &nbsp; Export</label>
	                 
	                <TH WIDTH="20%" ALIGN = "LEFT">
	                 <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="ProductReturnReceive" value="goodsreceiptbyproductreceive">     
	                 &nbsp; Receive</label>
	                 
	                 <TR>
	                 
		</TABLE>
     </div>
 </div>

<div class="row">
  		<div class="col-12 col-sm-12"><INPUT Type=Checkbox class="form-check-input"  style="border:0;" name="CreditNotes"  onclick="return checkAllCreditNotes(this.checked);">
                        &nbsp; Select/Unselect Credit Note </div>
  </div> 
<div class="panel panel-default">
<div class="panel-heading" style="background: #eaeafa"><strong>Credit Note</strong></div>
<div class="panel-body"> 
		<TABLE class="table1" style="font-size:14px;width: 100%;">
        	 <TR>
        	 
        	 <TH WIDTH="20%" ALIGN = "LEFT">
	                 <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="CreditNotes" value="customerCreditNote">     
	                 &nbsp; Credit Note</label>
	                 
	                <TH WIDTH="20%" ALIGN = "LEFT">
	                 <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="CreditNotes" value="newCreditNote">     
	                 &nbsp; New</label>
	                 
	                <TH WIDTH="20%" ALIGN = "LEFT">
	                 <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="CreditNotes" value="summarylnkCreditNote">
	                 &nbsp; Summary &#45; Credit Number Link</label>
	                 
	                  <TH WIDTH="20%" ALIGN = "LEFT">
	                 <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="CreditNotes" value="exportCreditNote">     
	                 &nbsp; Export</label>
	                 
	               
                    <TR> 
                    
                     <TH WIDTH="20%" ALIGN = "LEFT">
                     <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="CreditNotes" value="editCreditNote">
                     &nbsp; Edit</label>
                     
                        
                    <TH WIDTH="20%" ALIGN = "LEFT">
                     <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="CreditNotes" value="printCreditNote">
                     &nbsp; PDF/Print</label>
                     
                    <TH WIDTH="20%" ALIGN = "LEFT">
                     <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="CreditNotes" value="moreCreditNote" >
                     &nbsp; More</label>
        	          	
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
                        	<label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SalesReports" value="obordersumry" >
                        	&nbsp;  Sales Order Summary Details</label>
                        	</TH>                        	
                           
                        	<TH WIDTH="20%" ALIGN = "LEFT">
                        	<label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SalesReports"value="obsalessmry"  >
                        	&nbsp;   Sales Order Sales Summary</label>
                            </TH>
                            
                        	<TH WIDTH="20%" ALIGN = "LEFT">
                        	<label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SalesReports" value="containerSummary" >
                        	&nbsp; Sales Order Summary(by container)</label>
                            </TH> 
                            
                           
                            <TH WIDTH="20%" ALIGN = "LEFT">
                        	<label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SalesReports" value="invWithIssueReturn" >
                        	&nbsp; Sales Order Summary(by customer) </label>
                            </TH> 	
                    	   
                    	   <TR>
                            
                            <TH WIDTH="20%" ALIGN = "LEFT">
                        	<label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SalesReports" value="salesoderdelivery" >
                        	&nbsp; Sales Order Delivery Summary</label>
                            </TH> 	
                    	   
                            <TH WIDTH="20%" ALIGN = "LEFT">
                        	<label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SalesReports" value="obordersumrywissprice" >
                        	&nbsp;  Sales Order Summary Details(by price)</label>
                        	</TH>                        	 
                        	
                        	<!-- <TH WIDTH="20%" ALIGN = "LEFT">
                        	<label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SalesReports" value="obordersumrywavgcost" >
                        	&nbsp;  Sales Order Summary Details (by average cost)</label>
                       		</TH> -->
                       		
                        	<TH WIDTH="20%" ALIGN = "LEFT">
                        	<label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SalesReports" value="issuedobsummry"  >
                        	&nbsp; Sales Order Summary(by price)</label>
                           </TH>
                       		
                        	<TH WIDTH="20%" ALIGN = "LEFT">
                        	<label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SalesReports" value="salessummarycustomer"  >
                        	&nbsp; Sales Invoice Summary (By Customer)</label>
                           </TH>
                           
                           <TR> 
	                        
	                        <TH WIDTH="20%" ALIGN = "LEFT">
	                        <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SalesReports" value="salesperformance"  >
	                        &nbsp; Sales Performance Summary</label>
	                        </TH>
	                        
	                        <TH WIDTH="20%" ALIGN = "LEFT">
	                        <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SalesReports" value="printdo"  >
	                        &nbsp; Generate PDF (without price)</label>
	                        </TH>
	                        
	                        <TH WIDTH="20%" ALIGN = "LEFT">
	                       <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;" name="SalesReports" value="printinvoice"  >
	                        &nbsp; Generate PDF (with price)</label>
	                        </TH>                           	 
                           
	                        
	                        <TH WIDTH="20%" ALIGN = "LEFT">
	                        <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SalesReports" value="issuesummry" >      
	                        &nbsp;  Order Issue Summary</label>
	                        </TH>	                           
	                        
	                        <TR>
	                        
	                        <TH WIDTH="20%" ALIGN = "LEFT">
	                        <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="SalesReports" value="issuesummrywithprice"  >      
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
       				<TABLE class="table1" style="font-size:14px;width: 100%;">
                    <TR>
                    
                    <TH WIDTH="20%" ALIGN = "LEFT">
                    <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="Consignment" value="summaryconsignment">
                    &nbsp; Consignment</label>
	                 
                    <TH WIDTH="20%" ALIGN = "LEFT">
                     <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="Consignment" value="newconsignment">
                     &nbsp; New </label>
                     
                    <TH WIDTH="20%" ALIGN = "LEFT">
	                 <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="Consignment" value="summarylnkconsignment">
	                 &nbsp; Summary &#45; Consignment NumberLink</label>
                     
                     <TH WIDTH="20%" ALIGN = "LEFT">
                     <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="Consignment" value="exportforconsignment">
                     &nbsp; Export</label>
                                             
                     <TR>
                        
                     <TH WIDTH="20%" ALIGN = "LEFT">
                     <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="Consignment" value="editconsignment">
                     &nbsp; Edit</label>
                     
                    <TH WIDTH="20%" ALIGN = "LEFT">
                     <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="Consignment" value="printconsignment">
                     &nbsp; PDF/Print</label> 
                       
                    <TH WIDTH="20%" ALIGN = "LEFT">
                     <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="Consignment" value="moreconsignment">
                     &nbsp; More</label>
                     
              		 <TH WIDTH="20%" ALIGN = "LEFT">
                    <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="Consignment" value="ConsignmentsummaryWO" >
                    &nbsp; Summary &#45; Consignment Order Details</label>
                                                          
                    <TR>
                    
                    <TH WIDTH="20%" ALIGN = "LEFT">
                    <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="Consignment" value="Consignmentsummaryprice">
                    &nbsp; Summary &#45; Consignment Order Details (with price)</label>
                    
                    
                    <TH WIDTH="20%" ALIGN = "LEFT">
                    <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="Consignment" value="orderCloseTransfer" >
                    &nbsp; Close Outstanding Consignment Orders</label>
       		</TABLE>
    </div>
</div>  
                      
         <div class="row">
  			<div class="col-12 col-sm-12"><INPUT Type=Checkbox class="form-check-input"  style="border:0;" name="RentalTransaction"  onclick="return checkAllRentalTransaction(this.checked);">
                       &nbsp; Select/Unselect Consignment Transaction </div>
  			</div>  
		<div class="panel panel-default">
			<div class="panel-heading" style="background: #eaeafa"><strong>Consignment Transaction</strong></div>
				<div class="panel-body">
       				<TABLE class="table1" style="font-size:14px;width: 100%;">
                    <TR>
                    
         <TH WIDTH="20%" ALIGN = "LEFT">
		            <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="RentalTransaction" value="bulkPickReceiveTO" >
		            &nbsp;  Consignment Order Pick & Issue</label>   

 		 <TH WIDTH="60%" ALIGN = "LEFT">
	                <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="RentalTransaction" value="toreversal" >
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
       				<TABLE class="table1" style="font-size:14px;width: 100%;">
                    <TR>
                    
         <TH WIDTH="20%" ALIGN = "LEFT">
		            <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="ConsignmentReports" value="consignmentsummrywithoutprice" >
		            &nbsp; Consignment Summary Details</label>   

 		 <TH WIDTH="20%" ALIGN = "LEFT">
	                <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="ConsignmentReports" value="consignmentsummrywithprice" >
	                &nbsp; Consignment Summary Details (With Price)</label>

		       <TH WIDTH="20%" ALIGN = "LEFT">
                    <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="ConsignmentReports" value="printto"  >
                    &nbsp; Generate PDF (without price)</label>
                    </TH>
                    
                    <TH WIDTH="20%" ALIGN = "LEFT">
                    <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="ConsignmentReports" value="printtowithprice"  >
                    &nbsp; Generate PDF (with price)</label>
                    </TH>
              
       		</TABLE>
    </div>
</div>
  
  
                      
<div class="row">
  		<div class="col-12 col-sm-12"><INPUT Type=Checkbox class="form-check-input"  style="border:0;" name="Banking"  onclick="return checkAllBanking(this.checked);">
                        &nbsp; Select/Unselect Banking </div>
  </div>                      
                      <div class="panel panel-default">
<div class="panel-heading" style="background: #eaeafa"><strong>Banking</strong></div>
<div class="panel-body"> 
		<TABLE class="table1" style="font-size:14px;width: 100%;">
        	 <TR>
        	 
        	 <TH WIDTH="20%" ALIGN = "LEFT">
              <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;" name="Banking" value="BankSummary">
              &nbsp; Banking</label>
              
             <TH WIDTH="20%" ALIGN = "LEFT">
	          <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="Banking" value="newbank">     
	          &nbsp; New</label>
	          
	         <TH WIDTH="20%" ALIGN = "LEFT">
	          <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="Banking" value="editbank">     
	          &nbsp; Edit</label>
	          
	         <TH WIDTH="20%" ALIGN = "LEFT">
	          <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="Banking" value="exportbank">     
	          &nbsp; Export</label>
	          
	          <TR>
	          
	         <TH WIDTH="20%" ALIGN = "LEFT">
	          <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="Banking" value="deletebank">     
	          &nbsp; Delete</label>   
                      
        </TABLE>
  </div>
</div>
'
<div class="row">
  		<div class="col-12 col-sm-12"><INPUT Type=Checkbox class="form-check-input"  style="border:0;" name="chartOfAccounts"  onclick="return checkAllchartOfAccounts(this.checked);">
                        &nbsp; Select/Unselect Chart of Accounts </div>
  </div>
<div class="panel panel-default">
<div class="panel-heading" style="background: #eaeafa"><strong>Chart of Accounts</strong></div>
<div class="panel-body"> 
		<TABLE class="table1" style="font-size:14px;width: 100%;">
        	 <TR>
        	 
        	 <TH WIDTH="20%" ALIGN = "LEFT">
              <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="chartOfAccounts" value="chartOfAccounts" >
              &nbsp; Chart of Accounts</label>
              
             <TH WIDTH="20%" ALIGN = "LEFT">
	          <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="chartOfAccounts" value="newchartOfAccounts" >     
	          &nbsp; New</label>
	          
	         <TH WIDTH="20%" ALIGN = "LEFT">
	          <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="chartOfAccounts" value="editchartOfAccounts" >     
	          &nbsp; Edit</label>
	          
	         <TH WIDTH="20%" ALIGN = "LEFT">
	          <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="chartOfAccounts" value="printchartOfAccounts">     
	          &nbsp; PDF/Print</label>
	          
	          <TR>
	          
	         <TH WIDTH="20%" ALIGN = "LEFT">
	          <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="chartOfAccounts" value="deletechartOfAccounts">     
	          &nbsp; Delete</label>  
              

        </TABLE>
  </div>
</div>

<div class="row">
  		<div class="col-12 col-sm-12"><INPUT Type=Checkbox class="form-check-input"  style="border:0;" name="apSummary"  onclick="return checkAllapSummary(this.checked);">
                        &nbsp; Select/Unselect AP Document / POS Expenses </div>
  </div>
<div class="panel panel-default">
<div class="panel-heading" style="background: #eaeafa"><strong>AP Document / POS Expenses</strong></div>
<div class="panel-body"> 
		<TABLE class="table1" style="font-size:14px;width: 100%;">
        	 <TR>
        	 
        	 <TH WIDTH="20%" ALIGN = "LEFT">
              <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="apSummary" value="apsummary" >
              &nbsp; AP Document</label>
              
             <TH WIDTH="20%" ALIGN = "LEFT">
	          <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="apSummary" value="possummary" >     
	          &nbsp; POS Expenses</label>

        </TABLE>
  </div>
</div>

<div class="row">
  		<div class="col-12 col-sm-12"><INPUT Type=Checkbox class="form-check-input"  style="border:0;" name="PaymentMade"  onclick="return checkAllPaymentMade(this.checked);">
                        &nbsp; Select/Unselect Payment </div>
  </div>
<div class="panel panel-default">
<div class="panel-heading" style="background: #eaeafa"><strong>Payment</strong></div>
<div class="panel-body"> 
		<TABLE class="table1" style="font-size:14px;width: 100%;">
        	 <TR>
			 
			 <TH WIDTH="20%" ALIGN = "LEFT">
	          <label class="checkbox-inline">  <INPUT Type=Checkbox  style="border:0;" name="PaymentMade" value="billPaymentSummary" >
	          &nbsp; Payment</label>
	          
	         <TH WIDTH="20%" ALIGN = "LEFT">
	          <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="PaymentMade" value="newPaymentMade" >     
	          &nbsp; New</label>
	          
	         <TH WIDTH="20%" ALIGN = "LEFT">
	          <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="PaymentMade" value="summarylnkPaymentMade" >
	          &nbsp; Summary &#45; Payment Number Link</label> 
	          
	           <TH WIDTH="20%" ALIGN = "LEFT">
	          <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="PaymentMade" value="exportPaymentMade" >     
	          &nbsp; Export</label>
	          
	        
	          <TR>
	          
	           <TH WIDTH="20%" ALIGN = "LEFT">
	          <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="PaymentMade" value="editPaymentMade">     
	          &nbsp; Edit</label>
	          
	          
	         <TH WIDTH="20%" ALIGN = "LEFT">
	          <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="PaymentMade" value="printPaymentMade" >     
	          &nbsp; PDF/Print</label>
	          
	         <TH WIDTH="20%" ALIGN = "LEFT">
	          <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="PaymentMade" value="deletePaymentMade" >     
	          &nbsp; Delete</label>
	         
	         <TH WIDTH="20%" ALIGN = "LEFT">
              <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="PaymentMade" value="morePaymentMade" >
              &nbsp; More</label>  
	                    
	    </TABLE>
  </div>
</div>

<div class="row">
  		<div class="col-12 col-sm-12"><INPUT Type=Checkbox class="form-check-input"  style="border:0;" name="PDCPaymentMade"  onclick="return checkAllPDCPaymentMade(this.checked);">
                        &nbsp; Select/Unselect PDC Issued </div>
  </div>
<div class="panel panel-default">
<div class="panel-heading" style="background: #eaeafa"><strong>PDC Issued</strong></div>
<div class="panel-body"> 
		<TABLE class="table1" style="font-size:14px;width: 100%;">
        	 <TR>
        	 
        	 <TH WIDTH="20%" ALIGN = "LEFT">
	          <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;" name="PDCPaymentMade" value="pdcpaymentSummary" >
	          &nbsp; PDC Issued</label>
	          
	         <TH WIDTH="20%" ALIGN = "LEFT">
	          <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="PDCPaymentMade" value="newPDCPaymentMade" >     
	          &nbsp; Process Payment</label>
	          
	          <TH WIDTH="20%" ALIGN = "LEFT">
	          <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="PDCPaymentMade" value="exportPDCPaymentMade" >     
	          &nbsp; Export</label>  
	          
	         <TH WIDTH="20%" ALIGN = "LEFT">
	          <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="PDCPaymentMade" value="editPDCPaymentMade" >     
	          &nbsp; Edit</label>  
        </TABLE>
  </div>
</div>	 	 

<div class="row">
  		<div class="col-12 col-sm-12"><INPUT Type=Checkbox class="form-check-input"  style="border:0;" name="PaymentReceived"  onclick="return checkAllPaymentReceived(this.checked);">
                        &nbsp; Select/Unselect Receipt </div>
  </div>
<div class="panel panel-default">
<div class="panel-heading" style="background: #eaeafa"><strong>Receipt</strong></div>
<div class="panel-body"> 
		<TABLE class="table1" style="font-size:14px;width: 100%;">
        	 <TR>
        	 
			 <TH WIDTH="20%" ALIGN = "LEFT">
	          <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="PaymentReceived" value="invoicePaymentSummary">
	          &nbsp; Receipt</label>
	          
	         <TH WIDTH="20%" ALIGN = "LEFT">
	          <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="PaymentReceived" value="newPaymentReceived" >     
	          &nbsp; New</label>
	          
	         <TH WIDTH="20%" ALIGN = "LEFT">
	          <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="PaymentReceived" value="summarylnkPaymentReceived">
	          &nbsp; Summary &#45; Receipt Number Link</label>
	          
	            
	         <TH WIDTH="20%" ALIGN = "LEFT">
	          <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="PaymentReceived" value="editPaymentReceived" >     
	          &nbsp; Edit</label>
	          
	          <TR>
	          
	         <TH WIDTH="20%" ALIGN = "LEFT">
	          <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="PaymentReceived" value="printPaymentReceived" >     
	          &nbsp; PDF/Print</label>
	          
	         <TH WIDTH="20%" ALIGN = "LEFT">
	          <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="PaymentReceived" value="deletePaymentReceived" >     
	          &nbsp; Delete</label>
	         
	         <TH WIDTH="20%" ALIGN = "LEFT">
              <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="PaymentReceived" value="morePaymentReceived">
              &nbsp; More</label>  
	                    
	    </TABLE>
  </div>
</div>

<div class="row">
  		<div class="col-12 col-sm-12"><INPUT Type=Checkbox class="form-check-input"  style="border:0;" name="PDCPaymentReceived"  onclick="return checkAllPDCPaymentReceived(this.checked);">
                        &nbsp; Select/Unselect PDC Received </div>
  </div>
<div class="panel panel-default">
<div class="panel-heading" style="background: #eaeafa"><strong>PDC Received</strong></div>
<div class="panel-body"> 
		<TABLE class="table1" style="font-size:14px;width: 100%;">
        	 <TR>
        	 
        	 <TH WIDTH="20%" ALIGN = "LEFT">
	          <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="PDCPaymentReceived" value="pdcpaymentReceiveSummary" >
	          &nbsp; PDC Received</label>
	          
	         <TH WIDTH="20%" ALIGN = "LEFT">
	          <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="PDCPaymentReceived" value="newPDCPaymentReceived" >     
	          &nbsp; Process Payment</label>
	          
	          <TH WIDTH="20%" ALIGN = "LEFT">
	          <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="PDCPaymentReceived" value="exportPDCPaymentReceived" >     
	          &nbsp; Export</label>
	          
	         <TH WIDTH="20%" ALIGN = "LEFT">
	          <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="PDCPaymentReceived" value="editPDCPaymentReceived" >     
	          &nbsp; Edit</label>
	            
        </TABLE>
  </div>
</div>

<div class="row">
  		<div class="col-12 col-sm-12"><INPUT Type=Checkbox class="form-check-input"  style="border:0;" name="JournalEntry"  onclick="return checkAllJournalEntry(this.checked);">
                        &nbsp; Select/Unselect Manual Journals </div>
  </div>
<div class="panel panel-default">
<div class="panel-heading" style="background: #eaeafa"><strong>Manual Journals</strong></div>
<div class="panel-body"> 
		<TABLE class="table1" style="font-size:14px;width: 100%;">
        	 <TR>
        	 
        	 <TH WIDTH="20%" ALIGN = "LEFT">
              <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;" name="JournalEntry" value="journalsummary">
              &nbsp; Manual Journals </label>
             
             <TH WIDTH="20%" ALIGN = "LEFT">
              <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;" name="JournalEntry" value="newjournal" >
              &nbsp; New</label>
              
             <TH WIDTH="20%" ALIGN = "LEFT">
	          <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="JournalEntry" value="summarylnkjournal">
	          &nbsp; Summary &#45; Journal Number Link</label> 
	          
	         <TH WIDTH="20%" ALIGN = "LEFT">
	          <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="JournalEntry" value="editjournal">     
	          &nbsp; Edit</label>
	          
	          <TR>
	          
	         <TH WIDTH="20%" ALIGN = "LEFT">
	          <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="JournalEntry" value="printjournal" >     
	          &nbsp; PDF/Print</label>  
              
        </TABLE>
  </div>
</div>
<div class="row">
  		<div class="col-12 col-sm-12"><INPUT Type=Checkbox class="form-check-input"  style="border:0;" name="ContraEntry"  onclick="return checkAllContraEntry(this.checked);">
                        &nbsp; Select/Unselect Contra </div>
  </div>	 
<div class="panel panel-default">
<div class="panel-heading" style="background: #eaeafa"><strong>Contra</strong></div>
<div class="panel-body"> 
		<TABLE class="table1" style="font-size:14px;width: 100%;">
        	 <TR>
        	 
        	 <TH WIDTH="20%" ALIGN = "LEFT">
              <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;" name="ContraEntry" value="contrasummary">
              &nbsp; Contra</label>
             
             <TH WIDTH="20%" ALIGN = "LEFT">
              <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;" name="ContraEntry" value="newcontra">
              &nbsp; New</label>
              
             <TH WIDTH="20%" ALIGN = "LEFT">
	          <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="ContraEntry" value="summarylnkcontra" >
	          &nbsp; Summary &#45; Contra Number Link</label> 
	          
	         <TH WIDTH="20%" ALIGN = "LEFT">
	          <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="ContraEntry" value="editcontra" >     
	          &nbsp; Edit</label>
	          
	          <TR>
	          
	         <TH WIDTH="20%" ALIGN = "LEFT">
	          <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="ContraEntry" value="printcontra">     
	          &nbsp; PDF/Print</label>  
              
        </TABLE>
  </div>
</div>	 
                      

<div class="row">
  		<div class="col-12 col-sm-12"><INPUT Type=Checkbox class="form-check-input"  style="border:0;" name="AccountingAdmin"  onclick="return checkAllAccountingAdmin(this.checked);">
                        &nbsp; Select/Unselect Tax </div>
  </div>
 <div class="panel panel-default">
    <div class="panel-heading" style="background: #eaeafa"><strong>Tax</strong></div>
    <div class="panel-body"> 
    	<TABLE style="width: 100%;">
	             <TR>
                               
                 <TH WIDTH="20%" ALIGN = "LEFT">
                      <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="AccountingAdmin" value="taxreturnsettings" >
                      &nbsp; Tax Settings</label>
                 
                 <TH WIDTH="20%" ALIGN = "LEFT">
              <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;" name="AccountingAdmin" value="taxreturnsummary_acc">
              &nbsp; Tax Return  </label>       
                      
                 <TH WIDTH="20%" ALIGN = "LEFT" style="display: <%=region.equals("ASIA PACIFIC") ? "none" : "block"%>;">
	                        <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="AccountingAdmin" value="taxreturnpayments_acc"  >      
	                        &nbsp;  Tax Payments</label>
	                        </TH>
                 
                 <TH WIDTH="20%" ALIGN = "LEFT" style="display: <%=region.equals("ASIA PACIFIC") ? "none" : "block"%>;">
	                        <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="AccountingAdmin" value="taxAdjustmentSummary_acc"  >      
	                        &nbsp;  Tax Adjustments</label>
	                        </TH>     
	              <TR>
	               <TH WIDTH="20%" ALIGN = "LEFT">
              <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;" name="AccountingAdmin" value="taxReports">
              &nbsp; Tax Reports  </label>       
              
               <TH WIDTH="20%" ALIGN = "LEFT">
              <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;" name="AccountingAdmin" value="taxFilingSummary">
              &nbsp; Tax Filing Summary  </label>       
              
               <TH WIDTH="20%" ALIGN = "LEFT">
              <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;" name="AccountingAdmin" value="taxFilingDetailedSummary">
              &nbsp; Tax Filing Detailed Summary </label>       
              
               <TH WIDTH="20%" ALIGN = "LEFT">
              <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;" name="AccountingAdmin" value="taxLiabilityReport">
              &nbsp; Tax Liability Report  </label>       
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
    	<TABLE class="table1" style="font-size:14px;width: 100%;">
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
    <TABLE class="table1" style="font-size:14px;width: 100%;">
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
	                        	<label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="Reports" value="sumqtybyproduct"  >
	                        	&nbsp; Inventory Summary With Total Quantity (group by product)</label>
                         </TH>
							  						                 
                         <TH WIDTH="20%" ALIGN = "LEFT">
	                        	<label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="Reports" value="viewinventorybatchmultiuom"  >
	                        	&nbsp; Inventory Summary With Batch/Sno</label> 
                         </TH>  
                    
                        <TH WIDTH="20%" ALIGN = "LEFT">
                        	<label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="Reports" value="viewInventoryByProd" >
                        	&nbsp; Inventory Summary With Total Quantity (with pcs)</label>
                        </TH> 
                        
                         <TR>
                                     
                        <TH WIDTH="20%" ALIGN = "LEFT">
                        	<label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="Reports" value="valuationsummary" >
                        	&nbsp; Inventory Valuation Summary With Total Quantity</label>
                        </TH>  
                               
                        <TH WIDTH="20%" ALIGN = "LEFT">
                        	<label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="Reports" value="revenueshortage"  >
                        	&nbsp; Revenue Risk Due to Inventory Shortage Summary</label>
                        </TH>
                        
                        <TH WIDTH="20%" ALIGN = "LEFT">
                        	<label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="Reports" value="msInvnopriceqty"  >
                        	&nbsp; Inventory Summary With Batch/Sno (with pcs)</label>
                        </TH>
                         
                       <TH WIDTH="20%" ALIGN = "LEFT">
                            <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="Reports" value="msInvList"  >
                            &nbsp; Inventory Summary (with min/max/zero qty)</label> 
                       </TH> 
                        
                         <TR>
                    
                         
                    	<TH WIDTH="20%" ALIGN = "LEFT">
                        	<label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="Reports" value="msInvListwithExpireD"  >
                        	&nbsp; Inventory Summary (with expiry date)</label>
                        </TH> 
                        
                        <TH WIDTH="20%" ALIGN = "LEFT">
                        	<label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="Reports" value="invavalqty"  >
                        	&nbsp; Inventory Summary Available Quantity </label>
                        </TH>
	                                  
                       <TH WIDTH="20%" ALIGN = "LEFT">
                            <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="Reports" value="inventoryAgingSummary"  >
                            &nbsp; Inventory Aging Summary </label>
                       </TH>                                     
                         
                            <TH WIDTH="20%" ALIGN = "LEFT">
                            <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="Reports" value="invincomingoutgoingqty"  >
                            &nbsp; Inventory Summary (with goods receipt / goods issue)</label>
                       </TH> 
                        
                         <TR> 
                       
                            <TH WIDTH="20%" ALIGN = "LEFT">
                            <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="Reports" value="costpriceinv"  >
                            &nbsp; Inventory Summary (With Purchase Cost / Sales Price)</label>
                       </TH> 

                            <TH WIDTH="20%" ALIGN = "LEFT">
                            <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="Reports" value="invopenclosestock"  >
                            &nbsp; Inventory Summary Opening/Closing Stock  </label>
                       </TH> 
                     
                            <TH WIDTH="20%" ALIGN = "LEFT">
                            <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="Reports" value="invopenclosestockavgcost"  >
                            &nbsp; Inventory Summary Opening/Closing Stock (with average cost)</label>
                       </TH> 
                       
                         
	                    <TH WIDTH="20%" ALIGN = "LEFT">
	                           <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;" name="Reports" value="msInvListwithcost" >
	                     &nbsp; Inventory Summary (with average cost)</label>
	                     </TH> 
                        
                         <TR>
                         
	                    <TH WIDTH="20%" ALIGN = "LEFT">
	                           <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;" name="Reports" value="stkmovhis" >
	                     &nbsp; Inventory Movement Report</label>
	                     </TH> 
                         
                             <TH WIDTH="20%" ALIGN = "LEFT">
                            <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="Reports" value="salesforecasting"  >
                            &nbsp; Sales Forecasting  </label>
                       </TH>
                                           
                   </TABLE>
    </div>
</div>

<!-- POS Reports Show Only on Inventory 11.08.22 -->
<%-- <% if(ENABLE_POS.equals("1")) { %> 
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
	     				 <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;" name="PosReports" value="possalesreports">
	                     &nbsp; Sales Report</label>
	                     </TH>
	                     
						<TH WIDTH="20%" ALIGN = "LEFT">
	     				 <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;" name="PosReports" value="posvoidsales">
	                     &nbsp; Void Sales Report</label>
	                     </TH>
	                     
						<TH WIDTH="20%" ALIGN = "LEFT">
	     				 <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;" name="PosReports" value="posreturn">
	                     &nbsp; Return Sales Report</label>
	                     </TH>
	                     
						<TH WIDTH="20%" ALIGN = "LEFT">
	     				 <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;" name="PosReports" value="posdiscount">
	                     &nbsp; Discount</label>
	                     </TH>
	                 <TR>
						<TH WIDTH="20%" ALIGN = "LEFT">
	     				 <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;" name="PosReports" value="posFOC">
	                     &nbsp; FOC</label>
	                     </TH>
	                     
						<TH WIDTH="20%" ALIGN = "LEFT">
	     				 <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;" name="PosReports" value="posexpenses">
	                     &nbsp; Expense</label>
	                     </TH>
	                     
						<TH WIDTH="20%" ALIGN = "LEFT">
	     				 <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;" name="PosReports" value="posshiftclose">
	                     &nbsp; Shift Close</label>
	                     </TH>
		
		 </TABLE>
    </div>
</div>
<%}%> --%>


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
	                        	<label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="AccountingReports" value="parentbalance"  >
	                        	&nbsp; Consolidated Balance Sheet</label>
                         </TH>
							  						                 
                         <TH WIDTH="20%" ALIGN = "LEFT">
	                        	<label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="AccountingReports" value="profitloss"  >
	                        	&nbsp; Profit and Loss</label> 
                         </TH>
                         
                         <TH WIDTH="20%" ALIGN = "LEFT">
	                        	<label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="AccountingReports" value="parentprofitloss"  >
	                        	&nbsp; Consolidated Profit and Loss</label>
                         </TH>
                         
                 <TR>  
                         
                         <TH WIDTH="20%" ALIGN = "LEFT">
	                        	<label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="AccountingReports" value="statementofcashflows"  >
	                        	&nbsp; Statement Of Cash Flows</label>
                         </TH>
                         
                         <TH WIDTH="20%" ALIGN = "LEFT">
	                        	<label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="AccountingReports" value="expenseDetails"  >
	                        	&nbsp; Expense Details</label>
                         </TH>
							  						                 
                         <TH WIDTH="20%" ALIGN = "LEFT">
	                        	<label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="AccountingReports" value="expenseByCategory"  >
	                        	&nbsp; Expense by Category </label>
                         </TH>
                    
                         <TH WIDTH="20%" ALIGN = "LEFT">
	                        <label class="checkbox-inline">	<INPUT Type=Checkbox  style="border:0;" name="AccountingReports" value="expenseBySupplier" >
	                        	&nbsp; Expense by Supplier</label>
                         </TH>             
                                                                  
               <TR>

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
                        
	                     <TH WIDTH="20%" ALIGN = "LEFT">
	                        <label class="checkbox-inline">    <INPUT Type=Checkbox  style="border:0;" name="AccountingReports" value="vendorbalances" >
	                     	&nbsp; Supplier Balances</label>
	                     </TH>
                             
	         <TR>                 

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

                         <TH WIDTH="20%" ALIGN = "LEFT">
	                        	<label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="AccountingReports" value="paymentsreceived"  >
	                        	&nbsp; Payment Received</label>
                         </TH>      
	                        
	      <TR>
	                        
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
                       
                         <TH WIDTH="20%" ALIGN = "LEFT">
	                        <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="AccountingReports" value="bankreconcilationsummary"  >      
	                        &nbsp;  Bank Reconciliation</label>
	                     </TH>
	                        
	                        <!-- <TH WIDTH="20%" ALIGN = "LEFT">
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
	                        </TH> -->
	                        
	          <TR>
	                        
	                     <TH WIDTH="20%" ALIGN = "LEFT">
	                        <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="AccountingReports" value="generalledger"  >      
	                        &nbsp;  Detailed General Ledger</label>
	                     </TH>
	                        
	                     <TH WIDTH="20%" ALIGN = "LEFT">
	                        <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="AccountingReports" value="journalreport"  >      
	                        &nbsp;  Journal</label>
	                     </TH>
	                        
	                     <TH WIDTH="20%" ALIGN = "LEFT">
	                        <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="AccountingReports" value="parentjournalreport"  >      
	                        &nbsp;  Consolidated Journal</label>
	                     </TH>
	                        
	                     <TH WIDTH="20%" ALIGN = "LEFT">
	                        <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="AccountingReports" value="trialbalance"  >      
	                        &nbsp;  Trail Balance</label>
	                     </TH>
	                        
	        <TR>
	                        
	                     <TH WIDTH="20%" ALIGN = "LEFT">
	                    	<label class="checkbox-inline">    <INPUT Type=Checkbox  style="border:0;" name="AccountingReports" value="parenttrialbalance" >      
	                        &nbsp;  Consolidated Trail Balance</label>
	                     </TH>
	                        
	                     <TH WIDTH="20%" ALIGN = "LEFT">
	                    	<label class="checkbox-inline">    <INPUT Type=Checkbox  style="border:0;" name="AccountingReports" value="projectprofitloss" >      
	                        &nbsp;  Project Profit and Loss</label>
	                     </TH>
	                        
	                     <TH WIDTH="20%" ALIGN = "LEFT">
	                    	<label class="checkbox-inline">    <INPUT Type=Checkbox  style="border:0;" name="AccountingReports" value="projecttrialbalance" >      
	                        &nbsp;  Project Trial Balance</label>
	                     </TH>
	                        
	                     <TH WIDTH="20%" ALIGN = "LEFT">
	                    	<label class="checkbox-inline">    <INPUT Type=Checkbox  style="border:0;" name="AccountingReports" value="consolprojecttrialbalance" >      
	                        &nbsp;  Consolidated Project Trial Balance</label>
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
	                        	<label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="OwnerApp" value="ownappOpenStkvalue"  >
	                        	&nbsp; Opening Stock Value</label>
                         </TH>
                    <TH WIDTH="20%" ALIGN = "LEFT">
	                        	<label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="OwnerApp" value="ownClsStkvalue"  >
	                        	&nbsp; Closing Stock Value</label>
                         </TH>
                    <TH WIDTH="20%" ALIGN = "LEFT">
	                        	<label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="OwnerApp" value="ownappPurchase"  >
	                        	&nbsp; Purchase</label>
                         </TH>
                         
                         <TH WIDTH="20%" ALIGN = "LEFT">
	                        	<label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="OwnerApp" value="ownappPurchaseapproval" >
	                        	&nbsp; Purchase Order Approval</label>
                         </TH>
                         
                         <TR>
                         
                    <TH WIDTH="20%" ALIGN = "LEFT">
	                        	<label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="OwnerApp" value="ownappErpsales"  >
	                        	&nbsp; ERP Sales</label>
                         </TH>
                         
                    <TH WIDTH="20%" ALIGN = "LEFT">
	                        	<label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="OwnerApp" value="ownappPossales"  >
	                        	&nbsp; POS Sales</label>
                         </TH>
                    <TH WIDTH="20%" ALIGN = "LEFT">
	                        	<label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="OwnerApp" value="ownappProjectmanagment"  >
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
	                        	<label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="PeppolIntegration" value="peppolregister"  >
	                        	&nbsp; Peppol ID Registration</label>
                         </TH>
                    <TH WIDTH="20%" ALIGN = "LEFT">
	                        	<label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="PeppolIntegration" value="peppolpurchase"  >
	                        	&nbsp; Download Purchase Invoice from Peppol</label>
                         </TH>
                    <TH WIDTH="20%" ALIGN = "LEFT">
	                        	<label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="PeppolIntegration" value="peppolsales"  >
	                        	&nbsp; Upload Sales Invoice To Peppol</label>
                         </TH>
                    <TH WIDTH="20%" ALIGN = "LEFT">
	                        	<label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="PeppolIntegration" value="peppolcustomer"  >
	                        	&nbsp; Peppol Customer Summary</label>
                         </TH>
                         
                     <TR>
                         
                    <TH WIDTH="20%" ALIGN = "LEFT">
	                        	<label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="PeppolIntegration" value="peppolsupplier"  >
	                        	&nbsp; Peppol Supplier Summary</label>
                         </TH>
                    
                    </TABLE>
    </div>
</div>
<%}%>

 <!-- <div class="row">
  		<div class="col-12 col-sm-12"><INPUT Type=Checkbox class="form-check-input"  style="border:0;" name="AccountingHome"  onclick="return checkAllAccountingHome(this.checked);">
                     <strong>   &nbsp; Select/Unselect Accounting - Home Page</strong> </div>
  </div>
  
<div class="panel panel-default">
    <div class="panel-heading" style="background: #eaeafa"><strong>Accounting - Home Page</strong></div>
    <div class="panel-body">
		<TABLE class="table1" style="font-size:14px;width: 100%;">
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
    	<TABLE class="table1" style="font-size:14px;width: 100%;">					
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
    	<TABLE class="table1" style="font-size:14px;width: 100%;">					
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
    	<TABLE class="table1" style="font-size:14px;width: 100%;">					
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
    			<TABLE class="table1" style="font-size:14px;width: 100%;">
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
    	<TABLE class="table1" style="font-size:14px;width: 100%;">
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
    	<TABLE class="table1" style="font-size:14px;width: 100%;">
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
                        
                         <TH WIDTH="20%" ALIGN = "LEFT">
                       <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;" name="PdaSalesTransaction" value="pdaSalesPick" >
                        &nbsp; SALES PICK</label>
                        
                         <TH WIDTH="20%" ALIGN = "LEFT">
                       <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;" name="PdaSalesTransaction" value="pdaSalesIssue" >
                        &nbsp; SALES ISSUE</label>
                        
                        <TR>
                        
                         <TH WIDTH="20%" ALIGN = "LEFT">
                       <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;" name="PdaSalesTransaction" value="pdaPickIssue" >
                        &nbsp; PICK/ISSUE</label>
                        
                         <TH WIDTH="20%" ALIGN = "LEFT">
                       <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;" name="PdaSalesTransaction" value="pdaSalesOrderCheck" >
                        &nbsp; SALES ORDER CHECK</label>
                        
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
    	<TABLE class="table1" style="font-size:14px;width: 100%;">
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
    	<TABLE class="table1" style="font-size:14px;width: 100%;">
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
    <TABLE class="table1" style="font-size:14px;width: 100%;">
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
     <TABLE class="table1" style="font-size:14px;width: 100%;">
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