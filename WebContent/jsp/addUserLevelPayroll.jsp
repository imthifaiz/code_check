<%--New page design begin --%>
<%@page import="com.track.constants.IConstants"%>
<%
String title = "Create Payroll User Access Rights";
%>

<%@include file="sessionCheck.jsp" %>
<jsp:include page="header2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
	<jsp:param name="mainmenu" value="<%=IConstants.SETTINGS %>" />
	<jsp:param name="submenu" value="<%=IConstants.USER_ADMIN %>" />
</jsp:include>
<link rel="stylesheet" href="../jsp/css/typeahead.css">
<link rel="stylesheet" href="../jsp/css/accounting.css">
<%--New page design end --%>
<%
session= request.getSession();
String plant = (String) session.getAttribute("PLANT");
%>
<script type="text/javascript" src="../jsp/js/userLevel.js"></script>
<script type="text/javascript" src="../jsp/js/general.js"></script>
<script type="text/javascript">


function checkAll(isChk)
{
	 if (document.form.UserDasnboard)
	    {
    if (document.form.UserDasnboard.disabled == false)
    	document.form.UserDasnboard.checked = isChk;
    for (i = 0; i < document.form.UserDasnboard.length; i++)
    {
            if (document.form.UserDasnboard[i].disabled == false)
        	document.form.UserDasnboard[i].checked = isChk;
   
}
	    }
	 
	 if (document.form.payreports)
	    {
 if (document.form.payreports.disabled == false)
 	document.form.payreports.checked = isChk;
 for (i = 0; i < document.form.payreports.length; i++)
 {
         if (document.form.payreports[i].disabled == false)
     	document.form.payreports[i].checked = isChk;

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
	 if (document.form.pPrint_Config)
	 {
	     if (document.form.pPrint_Config.disabled == false)
	     	document.form.pPrint_Config.checked = isChk;
	     for (i = 0; i < document.form.pPrint_Config.length; i++)
	     {
	             if (document.form.pPrint_Config[i].disabled == false)
	         	document.form.pPrint_Config[i].checked = isChk;
	     }
	 }

	 if (document.form.pbulkpayslip)
	 {
	     if (document.form.pbulkpayslip.disabled == false)
	     	document.form.pbulkpayslip.checked = isChk;
	     for (i = 0; i < document.form.pbulkpayslip.length; i++)
	     {
	             if (document.form.pbulkpayslip[i].disabled == false)
	         	document.form.pbulkpayslip[i].checked = isChk;
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



    if (document.form.userEmployeeType)
    {
        if (document.form.userEmployeeType.disabled == false)
        	document.form.userEmployeeType.checked = isChk;
        for (i = 0; i < document.form.userEmployeeType.length; i++)
        {
                if (document.form.userEmployeeType[i].disabled == false)
            	document.form.userEmployeeType[i].checked = isChk;
        }
    }

    if (document.form.pdepartment)
    {
        if (document.form.pdepartment.disabled == false)
        	document.form.pdepartment.checked = isChk;
        for (i = 0; i < document.form.pdepartment.length; i++)
        {
                if (document.form.pdepartment[i].disabled == false)
            	document.form.pdepartment[i].checked = isChk;
        }
    }


    if (document.form.psalarytype)
    {
        if (document.form.psalarytype.disabled == false)
        	document.form.psalarytype.checked = isChk;
        for (i = 0; i < document.form.psalarytype.length; i++)
        {
                if (document.form.psalarytype[i].disabled == false)
            	document.form.psalarytype[i].checked = isChk;
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

    if (document.form.pLeavetype)
    {
        if (document.form.pLeavetype.disabled == false)
        	document.form.pLeavetype.checked = isChk;
        for (i = 0; i < document.form.pLeavetype.length; i++)
        {
                if (document.form.pLeavetype[i].disabled == false)
            	document.form.pLeavetype[i].checked = isChk;
        }
    }

    if (document.form.pholidaytype)
    {
        if (document.form.pholidaytype.disabled == false)
        	document.form.pholidaytype.checked = isChk;
        for (i = 0; i < document.form.pholidaytype.length; i++)
        {
                if (document.form.pholidaytype[i].disabled == false)
            	document.form.pholidaytype[i].checked = isChk;
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


   //resvi adds
   
    if (document.form.Shift)
    {
        if (document.form.Shift.disabled == false)
        	document.form.Shift.checked = isChk;
        for (i = 0; i < document.form.Shift.length; i++)
        {
                if (document.form.Shift[i].disabled == false)
            	document.form.Shift[i].checked = isChk;
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

    if (document.form.payaddition)
    {
        if (document.form.payaddition.disabled == false)
        	document.form.payaddition.checked = isChk;
        for (i = 0; i < document.form.payaddition.length; i++)
        {
                if (document.form.payaddition[i].disabled == false)
            	document.form.payaddition[i].checked = isChk;
        }
    }


    if (document.form.paydeduction)
    {
        if (document.form.paydeduction.disabled == false)
        	document.form.paydeduction.checked = isChk;
        for (i = 0; i < document.form.paydeduction.length; i++)
        {
                if (document.form.paydeduction[i].disabled == false)
            	document.form.paydeduction[i].checked = isChk;
        }
    }




    if (document.form.UserPayroll)
    {
        if (document.form.UserPayroll.disabled == false)
        	document.form.UserPayroll.checked = isChk;
        for (i = 0; i < document.form.UserPayroll.length; i++)
        {
                if (document.form.UserPayroll[i].disabled == false)
            	document.form.UserPayroll[i].checked = isChk;
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

   
       if (document.form.PayrollPayment)
       {
           if (document.form.PayrollPayment.disabled == false)
           	document.form.PayrollPayment.checked = isChk;
           for (i = 0; i < document.form.PayrollPayment.length; i++)
           {
                   if (document.form.PayrollPayment[i].disabled == false)
               	document.form.PayrollPayment[i].checked = isChk;
           }
       }

       if (document.form.pClaim)
       {
           if (document.form.pClaim.disabled == false)
           	document.form.pClaim.checked = isChk;
           for (i = 0; i < document.form.pClaim.length; i++)
           {
                   if (document.form.pClaim[i].disabled == false)
               	document.form.pClaim[i].checked = isChk;
           }
       }

       if (document.form.pClaimPayment)
       {
           if (document.form.pClaimPayment.disabled == false)
           	document.form.pClaimPayment.checked = isChk;
           for (i = 0; i < document.form.pClaimPayment.length; i++)
           {
                   if (document.form.pClaimPayment[i].disabled == false)
               	document.form.pClaimPayment[i].checked = isChk;
           }
       }
   


    if (document.form.payrollreport)
    {
        if (document.form.payrollreport.disabled == false)
        	document.form.payrollreport.checked = isChk;
        for (i = 0; i < document.form.payrollreport.length; i++)
        {
                if (document.form.payrollreport[i].disabled == false)
            	document.form.payrollreport[i].checked = isChk;
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


  if (document.form.Pdapayrollreport)
    {
        if (document.form.Pdapayrollreport.disabled == false)
        	document.form.Pdapayrollreport.checked = isChk;
        for (i = 0; i < document.form.Pdapayrollreport.length; i++)
        {
                if (document.form.Pdapayrollreport[i].disabled == false)
            	document.form.Pdapayrollreport[i].checked = isChk;
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
    if (document.form.UserDasnboard)
    {
        if (document.form.UserDasnboard.disabled == false)
        	document.form.UserDasnboard.checked = isChk;
        for (i = 0; i < document.form.UserDasnboard.length; i++)
        {
                if (document.form.UserDasnboard[i].disabled == false)
            	document.form.UserDasnboard[i].checked = isChk;
        }
    }
}

function checkallpayreports(isChk)
{
    if (document.form.payreports)
    {
        if (document.form.payreports.disabled == false)
        	document.form.payreports.checked = isChk;
        for (i = 0; i < document.form.payreports.length; i++)
        {
                if (document.form.payreports[i].disabled == false)
            	document.form.payreports[i].checked = isChk;
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

function checkAllEmployeeType(isChk)
{
    if (document.form.userEmployeeType)
    {
        if (document.form.userEmployeeType.disabled == false)
        	document.form.userEmployeeType.checked = isChk;
        for (i = 0; i < document.form.userEmployeeType.length; i++)
        {
                if (document.form.userEmployeeType[i].disabled == false)
            	document.form.userEmployeeType[i].checked = isChk;
        }
    }
}

function checkAllDepartment(isChk)
{
    if (document.form.pdepartment)
    {
        if (document.form.pdepartment.disabled == false)
        	document.form.pdepartment.checked = isChk;
        for (i = 0; i < document.form.pdepartment.length; i++)
        {
                if (document.form.pdepartment[i].disabled == false)
            	document.form.pdepartment[i].checked = isChk;
        }
    }
}

function checkAllSalaryType(isChk)
{
    if (document.form.psalarytype)
    {
        if (document.form.psalarytype.disabled == false)
        	document.form.psalarytype.checked = isChk;
        for (i = 0; i < document.form.psalarytype.length; i++)
        {
                if (document.form.psalarytype[i].disabled == false)
            	document.form.psalarytype[i].checked = isChk;
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


function checkAllLeaveType(isChk)
{
    if (document.form.pLeavetype)
    {
        if (document.form.pLeavetype.disabled == false)
        	document.form.pLeavetype.checked = isChk;
        for (i = 0; i < document.form.pLeavetype.length; i++)
        {
                if (document.form.pLeavetype[i].disabled == false)
            	document.form.pLeavetype[i].checked = isChk;
        }
    }
}

function checkAllHolidayType(isChk)
{
    if (document.form.pholidaytype)
    {
        if (document.form.pholidaytype.disabled == false)
        	document.form.pholidaytype.checked = isChk;
        for (i = 0; i < document.form.pholidaytype.length; i++)
        {
                if (document.form.pholidaytype[i].disabled == false)
            	document.form.pholidaytype[i].checked = isChk;
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

//resvi adds

function checkAllShift(isChk)
{
if (document.form.Shift)
{
    if (document.form.Shift.disabled == false)
    	document.form.Shift.checked = isChk;
    for (i = 0; i < document.form.Shift.length; i++)
    {
            if (document.form.Shift[i].disabled == false)
        	document.form.Shift[i].checked = isChk;
    }
}
}
// ends

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

function checkAllAddition(isChk)
{
    if (document.form.payaddition)
    {
        if (document.form.payaddition.disabled == false)
        	document.form.payaddition.checked = isChk;
        for (i = 0; i < document.form.payaddition.length; i++)
        {
                if (document.form.payaddition[i].disabled == false)
            	document.form.payaddition[i].checked = isChk;
        }
    }
}

function checkAllDeduction(isChk)
{
    if (document.form.paydeduction)
    {
        if (document.form.paydeduction.disabled == false)
        	document.form.paydeduction.checked = isChk;
        for (i = 0; i < document.form.paydeduction.length; i++)
        {
                if (document.form.paydeduction[i].disabled == false)
            	document.form.paydeduction[i].checked = isChk;
        }
    }
}

function checkAllPayroll(isChk)
{
    if (document.form.UserPayroll)
    {
        if (document.form.UserPayroll.disabled == false)
        	document.form.UserPayroll.checked = isChk;
        for (i = 0; i < document.form.UserPayroll.length; i++)
        {
                if (document.form.UserPayroll[i].disabled == false)
            	document.form.UserPayroll[i].checked = isChk;
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

function checkAllPayrollPayment(isChk)
{
    if (document.form.PayrollPayment)
    {
        if (document.form.PayrollPayment.disabled == false)
        	document.form.PayrollPayment.checked = isChk;
        for (i = 0; i < document.form.PayrollPayment.length; i++)
        {
                if (document.form.PayrollPayment[i].disabled == false)
            	document.form.PayrollPayment[i].checked = isChk;
        }
    }
}

function checkAllClaim(isChk)
{
    if (document.form.pClaim)
    {
        if (document.form.pClaim.disabled == false)
        	document.form.pClaim.checked = isChk;
        for (i = 0; i < document.form.pClaim.length; i++)
        {
                if (document.form.pClaim[i].disabled == false)
            	document.form.pClaim[i].checked = isChk;
        }
    }
}

function checkAllClaimPayment(isChk)
{
    if (document.form.pClaimPayment)
    {
        if (document.form.pClaimPayment.disabled == false)
        	document.form.pClaimPayment.checked = isChk;
        for (i = 0; i < document.form.pClaimPayment.length; i++)
        {
                if (document.form.pClaimPayment[i].disabled == false)
            	document.form.pClaimPayment[i].checked = isChk;
        }
    }
}

function checkAllBulkPayslip(isChk)
{
    if (document.form.pbulkpayslip)
    {
        if (document.form.pbulkpayslip.disabled == false)
        	document.form.pbulkpayslip.checked = isChk;
        for (i = 0; i < document.form.pbulkpayslip.length; i++)
        {
                if (document.form.pbulkpayslip[i].disabled == false)
            	document.form.pbulkpayslip[i].checked = isChk;
        }
    }
}

function checkAllPrint_Config(isChk)
{
    if (document.form.pPrint_Config)
    {
        if (document.form.pPrint_Config.disabled == false)
        	document.form.pPrint_Config.checked = isChk;
        for (i = 0; i < document.form.pPrint_Config.length; i++)
        {
                if (document.form.pPrint_Config[i].disabled == false)
            	document.form.pPrint_Config[i].checked = isChk;
        }
    }
}

function checkAllPayrollReort(isChk)
{
    if (document.form.payrollreport)
    {
        if (document.form.payrollreport.disabled == false)
        	document.form.payrollreport.checked = isChk;
        for (i = 0; i < document.form.payrollreport.length; i++)
        {
                if (document.form.payrollreport[i].disabled == false)
            	document.form.payrollreport[i].checked = isChk;
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

function checkAllPdapayrollreport(isChk)
{
    if (document.form.Pdapayrollreport)
    {
        if (document.form.Pdapayrollreport.disabled == false)
        	document.form.Pdapayrollreport.checked = isChk;
        for (i = 0; i < document.form.Pdapayrollreport.length; i++)
        {
                if (document.form.Pdapayrollreport[i].disabled == false)
            	document.form.Pdapayrollreport[i].checked = isChk;
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
	 <!-- Thanzith Modified on 28.02.2022 -->
	 <ul class="breadcrumb backpageul">    
                <li class="underline-on-hover"><a href="../home">Dashboard</a></li>	
                 <li><a href="../useraccess/paysummary"><span class="underline-on-hover">Payroll User Access Rights Summary</span></a></li>	
                <li><label>Create Payroll User Access Rights</label></li>                                   
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
<%--New page design end --%>
<form name="form" class="form-horizontal" method="POST" action="../jsp/userLevelPayrollSubmit.jsp" onSubmit="return validateLevel()">
  <div class="form-group">
  		<label class="control-label col-sm-4" for="Product Class ID">Payroll User Access Rights</label>
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
  		<div class="col-12 col-sm-12"><INPUT Type=Checkbox class="form-check-input"  style="border:0;" name="UserDasnboard"  onclick="return checkAllHome(this.checked);">
                      &nbsp; Select/Unselect Dashboard  </div>
  </div>
 
  <div class="panel panel-default">
    <div class="panel-heading" style="background: #eaeafa"><strong>Dashboard</strong></div>
    <div class="panel-body">
    <TABLE class="table1">
                <TR>
                 <TH WIDTH="20%" ALIGN = "LEFT">
                        <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="UserDasnboard" value="homePayroll">
                        &nbsp; Payroll</label>   
                  
      
                          
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
        <TABLE class="table1" style="font-size:14px;width: 100%;">
                    <TR>
             			<TH WIDTH="20%" ALIGN = "LEFT">
                        <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="UserAdmin" value="customerMaintCpny">
                        &nbsp;  Edit Your Company Profile</label>

                     <TH WIDTH="20%" ALIGN = "LEFT">
                        <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="UserAdmin" value="uaMaintLevel">
                        &nbsp; User Details</label>
                        
                      <TH WIDTH="20%" ALIGN = "LEFT">
                     <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="UserAdmin" value="summarylnkuaMaintLevel">
                       &nbsp; Summary &#45; User Details Link</label>
                       
                       <TH WIDTH="20%" ALIGN = "LEFT">
                     <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="UserAdmin" value="edituaMaintLevel">
                       &nbsp; Edit User Details</label>
                     
                     <TR>
                        
                      <TH WIDTH="20%" ALIGN = "LEFT">
                        <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="UserAdmin" value="uaChngPwd">
                        &nbsp; Edit Password Details</label>  
                        
                       <TH WIDTH="20%" ALIGN = "LEFT">
                        <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="UserAdmin" value="uaMaintAcct">
                        &nbsp;  User Access Rights</label> 
                        
                    	<TH WIDTH="20%" ALIGN = "LEFT">
                          <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="UserAdmin" value="newuaMaintAcct">
                        &nbsp; New User Access Rights</label>
                        
                        <TH WIDTH="20%" ALIGN = "LEFT">
                          <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="UserAdmin" value="summarylnkuaMaintAcct">
                        &nbsp; Summary &#45; User Access Rights Link</label>
                        
                        <TR>
                        
                        <TH WIDTH="20%" ALIGN = "LEFT">
                          <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="UserAdmin" value="edituaMaintAcct">
                        &nbsp; Edit User Access Rights</label>
                       
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
                        &nbsp; New</label>
                        
                     <TH WIDTH="20%" ALIGN = "LEFT">
	                 <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="UserEmployee" value="psummarylnkemployee">
	                 &nbsp; Summary &#45; Employee Link</label>
	                 
                    <TH WIDTH="20%" ALIGN = "LEFT">
                        <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="UserEmployee" value="peditemoloyee">
                        &nbsp; Edit</label>
                        
             <TR>
             
                     <TH WIDTH="20%" ALIGN = "LEFT">
                        <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="UserEmployee" value="pimportemployee">
                        &nbsp; Import Employee</label>
                                       
                    <TH WIDTH="20%" ALIGN = "LEFT">
                    <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="UserEmployee" value="pimportemployeeleavetype" >
                    &nbsp; Import Employee Leave Type</label>
                        
                     <TH WIDTH="20%" ALIGN = "LEFT">
	                 <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="UserEmployee" value="pimportemployeesalarytype" >
	                 &nbsp; Import Employee Salary Type</label> 
	                 </TH>
	                        
	                 <TH WIDTH="20%" ALIGN = "LEFT">
	                 <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="UserEmployee" value="pexportmasterdata" >
	                 &nbsp;  Export Master Data</label>
	                 </TH>
                     
                     <TR>
					 
					 <TH WIDTH="20%" ALIGN = "LEFT">
	                 <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="UserEmployee" value="pexportleavedetails"  >
	                 &nbsp; Export Leave Details</label>
                     </TH>
                                       
                    <TH WIDTH="20%" ALIGN = "LEFT">
                    <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="UserEmployee" value="pexportsalarydetails" >
                    &nbsp; Export Salary Details</label>
                     

                      </TABLE>
         </div>
  </div>
  
    <div class="row">
  		<div class="col-12 col-sm-12"><INPUT Type=Checkbox class="form-check-input"  style="border:0;" name="userEmployeeType"  onclick="return checkAllEmployeeType(this.checked);">
                        &nbsp; Select/Unselect Employee Type</div>
  </div>
   <div class="panel panel-default">
    <div class="panel-heading" style="background: #eaeafa"><strong>Employee Type</strong></div>
    <div class="panel-body">
    	<TABLE class="table1" style="font-size:14px;width: 100%;">
             <TR>
                  <TH WIDTH="20%" ALIGN = "LEFT">
                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="userEmployeeType" value="EmployeeTypeSummary" >
                  &nbsp; Employee Type</label>
                  
                     <TH WIDTH="20%" ALIGN = "LEFT">
                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="userEmployeeType" value="Pnewemployeetype" >
                  &nbsp; New</label>
                  
                  <TH WIDTH="20%" ALIGN = "LEFT">
                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="userEmployeeType" value="peditemployeetype" >
                  &nbsp;  Edit</label> 
                  
                  <TH WIDTH="20%" ALIGN = "LEFT">
                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="userEmployeeType" value="pdeleteemployeetype">
                  &nbsp; Delete</label>
                  
                  <TR>
                  
                  <TH WIDTH="20%" ALIGN = "LEFT">
                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="userEmployeeType" value="pexportemployeetype">
                  &nbsp; Export</label>
                  
               
	         </TABLE>
    </div>
  </div>
  
  <div class="row">
  		<div class="col-12 col-sm-12"><INPUT Type=Checkbox class="form-check-input"  style="border:0;" name="pdepartment"  onclick="return checkAllDepartment(this.checked);">
                       &nbsp; Select/Unselect Department</div>
  </div>
  <div class="panel panel-default">
    <div class="panel-heading" style="background: #eaeafa"><strong>Department</strong></div>
    <div class="panel-body">
    <TABLE class="table1" style="font-size:14px;width: 100%;">
             <TR>                 
                  <TH WIDTH="20%" ALIGN = "LEFT">
                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="pdepartment" value="DepartmentSummary" >
                  &nbsp;  Department</label>
                  
                   <TH WIDTH="20%" ALIGN = "LEFT">
                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="pdepartment" value="newDepartment" >
                  &nbsp; New</label>
                  
                  <TH WIDTH="20%" ALIGN = "LEFT">
                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="pdepartment" value="editDepartment" >
                  &nbsp; Edit</label>
                  
                  <TH WIDTH="20%" ALIGN = "LEFT">
                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="pdepartment" value="exportDepartment" >
                  &nbsp; Export</label>
                  
                                
        	 </TABLE>
    </div>
    </div>
  
   <div class="row">
  		<div class="col-12 col-sm-12"><INPUT Type=Checkbox class="form-check-input"  style="border:0;" name="psalarytype"  onclick="return checkAllSalaryType(this.checked);">
                       &nbsp; Select/Unselect Salary Type</div>
  </div>
   <div class="panel panel-default">
    <div class="panel-heading" style="background: #eaeafa"><strong>Salary Type</strong></div>
    <div class="panel-body">
    <TABLE class="table1" style="font-size:14px;width: 100%;">
             <TR>                 
                  <TH WIDTH="20%" ALIGN = "LEFT">
                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="psalarytype" value="EmployeeSalarySummary" >
                  &nbsp;  Salary Type</label>
                  
                   <TH WIDTH="20%" ALIGN = "LEFT">
                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="psalarytype" value="pnewsalarytype " >
                  &nbsp; New</label>
                  
                  <TH WIDTH="20%" ALIGN = "LEFT">
                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="psalarytype" value="peditsalarytype" >
                  &nbsp; Edit</label>
                  
                  <TH WIDTH="20%" ALIGN = "LEFT">
                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="psalarytype" value="pexportsalarytype" >
                  &nbsp; Export</label>
                  
                                
        	 </TABLE>
    </div>
    </div>
    
      <div class="row">
  		<div class="col-12 col-sm-12"><INPUT Type=Checkbox class="form-check-input"  style="border:0;" name="pLeavetype"  onclick="return checkAllLeaveType(this.checked);">
                        &nbsp; Select/Unselect Leave Type</div>
  </div>
<div class="panel panel-default">
    <div class="panel-heading" style="background: #eaeafa"><strong>Leave Type</strong></div>
    <div class="panel-body"> 
		<TABLE class="table1" style="font-size:14px;width: 100%;">
	             <TR>                 
       	                      
                   <TH WIDTH="20%" ALIGN = "LEFT">
                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="pLeavetype" value="LeaveTypeSummary" >
                  &nbsp; Leave Type</label>  
                  
                         
                  <TH WIDTH="20%" ALIGN = "LEFT">
                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="pLeavetype" value="pnewleavetype" >
                  &nbsp; New</label>
  
                  <TH WIDTH="20%" ALIGN = "LEFT">
                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="pLeavetype" value="peditlevaetype" >
                  &nbsp; Edit</label>  
                  
                  
                  <TH WIDTH="20%" ALIGN = "LEFT">
                  <label class="checkbox-inline">    <INPUT Type=Checkbox  style="border:0;" name="pLeavetype" value="pdeleteleavetype" >
                      &nbsp; Delete</label>
                      
                  <TR>    
                  
                  <TH WIDTH="20%" ALIGN = "LEFT">
                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="pLeavetype" value="pexportleavetype" >
                  &nbsp; Export</label>
               
                
              </TABLE>
	</div>
</div>    

  <div class="row">
  		<div class="col-12 col-sm-12"><INPUT Type=Checkbox class="form-check-input"  style="border:0;" name="pholidaytype"  onclick="return checkAllHolidayType(this.checked);">
                        &nbsp; Select/Unselect Holiday</div>
  </div>

<div class="panel panel-default">
    <div class="panel-heading" style="background: #eaeafa"><strong>Holiday</strong></div>
    <div class="panel-body"> 
    	<TABLE class="table1" style="font-size:14px;width: 100%;">
	             <TR>     
	              <TH WIDTH="20%" ALIGN = "LEFT">
                    <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="pholidaytype" value="HolidayMstSummary"  >
                    &nbsp; Holiday Type</label>            
                 
       				 <TH WIDTH="23%" ALIGN = "LEFT">
                    <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="pholidaytype" value="pnewholiday" >
                    &nbsp; New</label>
                    
                    <TH WIDTH="23%" ALIGN = "LEFT">
                     <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="pholidaytype" value="peditholiday"  >
                     &nbsp; Edit</label>
                     
                     <TH WIDTH="23%" ALIGN = "LEFT">
                    <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="pholidaytype" value="pdeleteholiday" >
                    &nbsp; Delete</label>
                  <TR>   
                    <TH WIDTH="23%" ALIGN = "LEFT">
                    <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="pholidaytype" value="pimportholiday"  >
                    &nbsp;  Import</label>
                    
                    <TH WIDTH="20%" ALIGN = "LEFT">
                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="pholidaytype" value="pexportholiday" >
                  &nbsp; Export</label>
                   
                  
                               
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

<!--   Resvi -->

<div class="row">
  		<div class="col-12 col-sm-12"><INPUT Type=Checkbox class="form-check-input"  style="border:0;" name="Shift"  onclick="return checkAllShift(this.checked);">
                        &nbsp; Select/Unselect Shift </div>
  </div>                      
                      <div class="panel panel-default">
<div class="panel-heading" style="background: #eaeafa"><strong>Shift</strong></div>
<div class="panel-body"> 
		<TABLE class="table1" style="font-size:14px;width: 100%;">
        	 <TR>
        	 
        	 <TH WIDTH="20%" ALIGN = "LEFT">
              <label class="checkbox-inline"> <INPUT Type=Checkbox  style="border:0;" name="Shift" value="ShiftSummary">
              &nbsp; Shift</label>
              
             <TH WIDTH="20%" ALIGN = "LEFT">
	          <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="Shift" value="pnewshift">     
	          &nbsp; New</label>
	          
	         <TH WIDTH="20%" ALIGN = "LEFT">
	          <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="Shift" value="peditshift">     
	          &nbsp; Edit</label>
	          
	         <TH WIDTH="20%" ALIGN = "LEFT">
	          <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="Shift" value="pexportshift">     
	          &nbsp; Export</label>
	          
	          <TR>
	          
	         <TH WIDTH="20%" ALIGN = "LEFT">
	          <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="Shift" value="pdeleteshift">     
	          &nbsp; Delete</label>   
                      
        </TABLE>
  </div>
</div>

<!-- ends -->

  <div class="row">
  		<div class="col-12 col-sm-12"><INPUT Type=Checkbox class="form-check-input"  style="border:0;" name="pPrint_Config"  onclick="return checkAllPrint_Config(this.checked);">
                       &nbsp; Select/Unselect Printout Configuration</div>
  </div>
  <div class="panel panel-default">
    <div class="panel-heading" style="background: #eaeafa"><strong>Printout Configuration</strong></div>
    <div class="panel-body">
    <TABLE class="table1" style="font-size:14px;width: 100%;">
    
    		 <TR>
                    <TH WIDTH="20%" ALIGN = "LEFT">
	                 <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name=pPrint_Config value="email_configpayslip" >
	                 &nbsp; Edit PaySlip Email Configuration</label>
	                 </TH>
</TABLE>
</div>
</div>
       
   <div class="row">
  		<div class="col-12 col-sm-12"><INPUT Type=Checkbox class="form-check-input"  style="border:0;" name="payaddition"  onclick="return checkAllAddition(this.checked);">
                   &nbsp; Select/Unselect Addition</div>
  </div>

<div class="panel panel-default">
    <div class="panel-heading" style="background: #eaeafa"><strong>Addition</strong></div>
    <div class="panel-body"> 
    	<TABLE class="table1" style="font-size:14px;width: 100%;">
    	   	
                    <TR>
                   
                    <TH WIDTH="20%" ALIGN = "LEFT">
                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="payaddition" value="payrolladdmstsummary" >
                  &nbsp; Addition</label>
                   
                    <TH WIDTH="20%" ALIGN = "LEFT">
                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="payaddition" value="pnewaddition" >
                  &nbsp; New</label>
                  
                   <TH WIDTH="20%" ALIGN = "LEFT">
                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="payaddition" value="peditaddition">
                  &nbsp; Edit</label>
                    
                    <TH WIDTH="20%" ALIGN = "LEFT">
                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="payaddition" value="pexportaddition" >
                  &nbsp; Export</label>
                  
              
                    
             </TABLE>
    </div>
</div> 
  <div class="row">
  		<div class="col-12 col-sm-12"><INPUT Type=Checkbox class="form-check-input"  style="border:0;" name="paydeduction"  onclick="return checkAllDeduction(this.checked);">
                       &nbsp; Select/Unselect Deduction </div>
  </div>
<div class="panel panel-default">
    <div class="panel-heading" style="background: #eaeafa"><strong>Deduction</strong></div>
    <div class="panel-body"> 
    	<TABLE class="table1" style="font-size:14px;width: 100%;">
                    <TR>
                                    
                    <TH WIDTH="20%" ALIGN = "LEFT">
                    <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="paydeduction" value="PayrollDeductionSummary" >
                    &nbsp; Deduction</label>                    
                   
                    
                    <TH WIDTH="20%" ALIGN = "LEFT">
                    <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="paydeduction" value="pnewdeduction" >
                    &nbsp; New</label>
                                
                    <TH WIDTH="20%" ALIGN = "LEFT">
                    <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="paydeduction" value="peditdeduction" >
                    &nbsp; Edit</label>
                    
                    <TH WIDTH="20%" ALIGN = "LEFT">
                  <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="paydeduction" value="pexporteduction" >
                  &nbsp; Export</label>
                
              
		                      
             </TABLE>
    </div>
</div>   
  <div class="row">
  		<div class="col-12 col-sm-12"><INPUT Type=Checkbox class="form-check-input"  style="border:0;" name="UserPayroll"  onclick="return checkAllPayroll(this.checked);">
                        &nbsp; Select/Unselect Payroll</div>
  </div>
<div class="panel panel-default">
    <div class="panel-heading" style="background: #eaeafa"><strong>Payroll</strong></div>
    <div class="panel-body">
    <TABLE class="table1" style="font-size:14px;width: 100%;">
                    <TR>
                                                                                       
                    <!-- <TH WIDTH="20%" ALIGN = "LEFT">
                    <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="UserPayroll" value="pgraduity">
                    &nbsp;  Graduity</label> -->
                    
                     <TH WIDTH="20%" ALIGN = "LEFT">
                    <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="UserPayroll" value="PayrollSummary">
                    &nbsp;  Payroll</label>
                       <TH WIDTH="20%" ALIGN = "LEFT">
                    <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="UserPayroll" value="pnewpayroll" >
                    &nbsp; New</label>
                                       	                        
	                 <TH WIDTH="20%" ALIGN = "LEFT">
	                 <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="UserPayroll" value="pdetailpayroll" >
	                 &nbsp; Summary - Payroll Link</label>
	                 </TH>
                        
                     <TH WIDTH="20%" ALIGN = "LEFT">
	                 <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="UserPayroll" value="pexportpayroll">
	                 &nbsp;  Export</label> 
	                 </TH>
                    
                    <TR>
                        
                     <TH WIDTH="20%" ALIGN = "LEFT">
	                 <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="UserPayroll" value="peditpayroll" >
	                 &nbsp;  Edit</label> 
	                 </TH>
					 
					 <TH WIDTH="20%" ALIGN = "LEFT">
	                 <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="UserPayroll" value="payrollprint"  >
	                 &nbsp;  PDF/Print</label>
                     </TH>
                     <!--  <TH WIDTH="20%" ALIGN = "LEFT">
	                 <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="UserPayroll" value="summaryalternatbrandproduct"  >
	                 &nbsp;  Generate Payroll</label>
                     </TH> -->
                     
                  
                   </TABLE>
    </div>
</div>


<div class="row">
  		<div class="col-12 col-sm-12"><INPUT Type=Checkbox class="form-check-input"  style="border:0;" name="PayrollPayment"  onclick="return checkAllPayrollPayment(this.checked);">
                       &nbsp; Select/Unselect Payroll Payment </div>
  </div>
<div class="panel panel-default">
    <div class="panel-heading" style="background: #eaeafa"><strong>Payroll Payment</strong></div>
    <div class="panel-body">
    <TABLE class="table1" style="font-size:14px;width: 100%;">
                    <TR>
                     <TH WIDTH="20%" ALIGN = "LEFT">
                    <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="PayrollPayment" value="PayrollPaymentSummary" >
                    &nbsp;  Payroll Payment</label>
                    
                       <TH WIDTH="20%" ALIGN = "LEFT">
                    <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="PayrollPayment" value="pnewPayrollPayment" >
                    &nbsp; New</label>
                                       	                        
	                 <TH WIDTH="20%" ALIGN = "LEFT">
	                 <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="PayrollPayment" value="pdetailPayrollPayment" >
	                 &nbsp; Summary - Payroll Payment Link</label>
	                 </TH>
                        
                     <TH WIDTH="20%" ALIGN = "LEFT">
	                 <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="PayrollPayment" value="pexportPayrollPayment">
	                 &nbsp;  Export</label> 
	                 </TH>
                    
                    <TR>
                        
                     <TH WIDTH="20%" ALIGN = "LEFT">
	                 <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="PayrollPayment" value="peditPayrollPayment">
	                 &nbsp;  Edit</label> 
	                 </TH>
                        
                     <TH WIDTH="20%" ALIGN = "LEFT">
	                 <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="PayrollPayment" value="pprintPayrollPayment" >
	                 &nbsp;  PDF/Print</label>
                     </TH>
     </TABLE>
    </div>
</div> 

<div class="row">
  		<div class="col-12 col-sm-12"><INPUT Type=Checkbox class="form-check-input"  style="border:0;" name="pClaim"  onclick="return checkAllClaim(this.checked);">
                       &nbsp; Select/Unselect Claim </div>
  </div>
<div class="panel panel-default">
    <div class="panel-heading" style="background: #eaeafa"><strong>Claim</strong></div>
    <div class="panel-body">
    <TABLE class="table1" style="font-size:14px;width: 100%;">
                    <TR>
                     <TH WIDTH="20%" ALIGN = "LEFT">
                    <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="pClaim" value="HrClaimSummary" >
                    &nbsp;  Claim</label>
                    
                       <TH WIDTH="20%" ALIGN = "LEFT">
                    <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="pClaim" value="pnewClaim">
                    &nbsp; Process Claim</label>
                        
                     <TH WIDTH="20%" ALIGN = "LEFT">
	                 <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="pClaim" value="pexportClaim">
	                 &nbsp;  Export</label> 
	                 </TH>
     </TABLE>
    </div>
</div>  

<div class="row">
  		<div class="col-12 col-sm-12"><INPUT Type=Checkbox class="form-check-input"  style="border:0;" name="pClaimPayment"  onclick="return checkAllClaimPayment(this.checked);">
                       &nbsp; Select/Unselect Claim Payment </div>
  </div>
<div class="panel panel-default">
    <div class="panel-heading" style="background: #eaeafa"><strong>Claim Payment</strong></div>
    <div class="panel-body">
    <TABLE class="table1" style="font-size:14px;width: 100%;">
                    <TR>
                    <TH WIDTH="20%" ALIGN = "LEFT">
                    <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="pClaimPayment" value="ClaimPaymentSummary">
                    &nbsp;  Claim Payment</label>
                    
                    <TH WIDTH="20%" ALIGN = "LEFT">
	                 <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="pClaimPayment" value="pdetailClaimPayment" >
	                 &nbsp; Summary - Claim Payment Link</label>
	                 </TH>
                        
                     <TH WIDTH="20%" ALIGN = "LEFT">
	                 <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="pClaimPayment" value="pexportClaimPayment" >
	                 &nbsp;  Export</label> 
	                 </TH>
                        
                     <TH WIDTH="20%" ALIGN = "LEFT">
	                 <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="pClaimPayment" value="peditClaimPayment" >
	                 &nbsp;  Edit</label> 
	                 </TH>
                    
                    <TR>
                        
                     <TH WIDTH="20%" ALIGN = "LEFT">
	                 <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="pClaimPayment" value="pprintClaimPayment" >
	                 &nbsp;  PDF/Print</label>
                     </TH>
                     
                     <TH WIDTH="20%" ALIGN = "LEFT">
	                 <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="pClaimPayment" value="pemailClaimPayment" >
	                 &nbsp; Email</label> 
	                 </TH>
     </TABLE>
    </div>
</div>

<div class="row">
  		<div class="col-12 col-sm-12"><INPUT Type=Checkbox class="form-check-input"  style="border:0;" name="pbulkpayslip"  onclick="return checkAllBulkPayslip(this.checked);">
                       &nbsp; Select/Unselect Bulk Payslip </div>
  </div>
  <div class="panel panel-default">
    <div class="panel-heading" style="background: #eaeafa"><strong>Bulk Payslip</strong></div>
    <div class="panel-body">
    <TABLE class="table1" style="font-size:14px;width: 100%;">
    
    		 <TR>
                    
                    <TH WIDTH="20%" ALIGN = "LEFT">
	                 <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name=pbulkpayslip value="payslipgenerate" >
	                 &nbsp; Bulk Payslip Processing</label>
	                 </TH>
    
    
</TABLE>
</div>
</div>
<div class="row">
  		<div class="col-12 col-sm-12"><INPUT Type=Checkbox class="form-check-input"  style="border:0;" name="payrollreport"  onclick="return checkAllPayrollReort(this.checked);">
                       &nbsp; Select/Unselect Payroll Reports </div>
  </div>

<div class="panel panel-default">
    <div class="panel-heading" style="background: #eaeafa"><strong>Payroll Reports</strong></div>
    <div class="panel-body">
    <TABLE class="table1" style="font-size:14px;width: 100%;">
    				<TR>
                     
                    <TH WIDTH="20%" ALIGN = "LEFT">
                    <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="payrollreport" value="PayrollDetailedReport">
                    &nbsp; Payroll Report</label>   
                    </TH>                
                    
                   <TH WIDTH="20%" ALIGN = "LEFT">
                    <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="payrollreport" value="paysummaryexport">
                    &nbsp; Payroll Report Export</label>
                    </TH>
                 
                    <TH WIDTH="20%" ALIGN = "LEFT">
                    <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="payrollreport" value="paysummaryemail" >
                    &nbsp; Payroll Report Email</label>
                    </TH>
                    
                    <TH WIDTH="20%" ALIGN = "LEFT">
                    <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="payrollreport" value="DownloadPayslip">
                    &nbsp; Payslip</label>
                    </TH>
                    
                    <TR>
                    
                    <TH WIDTH="20%" ALIGN = "LEFT">
                    <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="payrollreport" value="rpprintPayslip">
                    &nbsp; Payslip Print</label>
                    </TH>
                    
                    <TH WIDTH="20%" ALIGN = "LEFT">
                    <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="payrollreport" value="rpemailPayslip">
                    &nbsp; Payslip Email</label>
                    </TH>
                                      
              
                  
              </TABLE>
    </div>
</div>

  <div class="row">
  		<div class="col-12 col-sm-12"><INPUT Type=Checkbox class="form-check-input"  style="border:0;" name="payreports"  onclick="return checkallpayreports(this.checked);">
                      &nbsp; Select/Unselect Reports  </div>
  </div>
 
  <div class="panel panel-default">
    <div class="panel-heading" style="background: #eaeafa"><strong>Reports</strong></div>
    <div class="panel-body">
    <TABLE class="table1">
                <TR>
                 <TH WIDTH="20%" ALIGN = "LEFT">
                        <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="payreports" value="msPOList">
                        &nbsp; Activity Logs</label>   
                  
      
                          
                      </TABLE>
         </div>
  </div>


 </div>


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
<jsp:include page="footer2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
</jsp:include>