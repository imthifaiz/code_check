<%@page import="com.track.constants.IConstants"%>
<%@ include file="header.jsp"%>

<%
String title = "Edit Payroll User Access Rights";
%>
<%@include file="sessionCheck.jsp" %>
<jsp:include page="header2.jsp" flush="true">
	<jsp:param name="title" value="<%=title %>" />
	<jsp:param name="mainmenu" value="<%=IConstants.SETTINGS %>" />
	<jsp:param name="submenu" value="<%=IConstants.USER_ADMIN %>" />
</jsp:include>
<link rel="stylesheet" href="../jsp/css/typeahead.css">
<link rel="stylesheet" href="../jsp/css/accounting.css">
<script type="text/javascript" src="../jsp/js/general.js"></script>

<script type="text/javascript" src="../jsp/js/userLevel.js"></script>

<script type="text/javascript">


function checkAll(isChk)
{
	 if (document.levelform.UserDasnboard)
	    {
    if (document.levelform.UserDasnboard.disabled == false)
    	document.levelform.UserDasnboard.checked = isChk;
    for (i = 0; i < document.levelform.UserDasnboard.length; i++)
    {
            if (document.levelform.UserDasnboard[i].disabled == false)
        	document.levelform.UserDasnboard[i].checked = isChk;
   
}
	    }
	 
	 

	 if (document.levelform.payreports)
	    {
 if (document.levelform.payreports.disabled == false)
 	document.levelform.payreports.checked = isChk;
 for (i = 0; i < document.levelform.payreports.length; i++)
 {
         if (document.levelform.payreports[i].disabled == false)
     	document.levelform.payreports[i].checked = isChk;

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

	 if (document.levelform.pPrint_Config)
	 {
	     if (document.levelform.pPrint_Config.disabled == false)
	     	document.levelform.pPrint_Config.checked = isChk;
	     for (i = 0; i < document.levelform.pPrint_Config.length; i++)
	     {
	             if (document.levelform.pPrint_Config[i].disabled == false)
	         	document.levelform.pPrint_Config[i].checked = isChk;
	     }
	 }

	 if (document.levelform.pbulkpayslip)
	 {
	     if (document.levelform.pbulkpayslip.disabled == false)
	     	document.levelform.pbulkpayslip.checked = isChk;
	     for (i = 0; i < document.levelform.pbulkpayslip.length; i++)
	     {
	             if (document.levelform.pbulkpayslip[i].disabled == false)
	         	document.levelform.pbulkpayslip[i].checked = isChk;
	     }
	 }
	 
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



    if (document.levelform.userEmployeeType)
    {
        if (document.levelform.userEmployeeType.disabled == false)
        	document.levelform.userEmployeeType.checked = isChk;
        for (i = 0; i < document.levelform.userEmployeeType.length; i++)
        {
                if (document.levelform.userEmployeeType[i].disabled == false)
            	document.levelform.userEmployeeType[i].checked = isChk;
        }
    }


    if (document.levelform.pdepartment)
    {
        if (document.levelform.pdepartment.disabled == false)
        	document.levelform.pdepartment.checked = isChk;
        for (i = 0; i < document.levelform.pdepartment.length; i++)
        {
                if (document.levelform.pdepartment[i].disabled == false)
            	document.levelform.pdepartment[i].checked = isChk;
        }
    }

    if (document.levelform.psalarytype)
    {
        if (document.levelform.psalarytype.disabled == false)
        	document.levelform.psalarytype.checked = isChk;
        for (i = 0; i < document.levelform.psalarytype.length; i++)
        {
                if (document.levelform.psalarytype[i].disabled == false)
            	document.levelform.psalarytype[i].checked = isChk;
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

    if (document.levelform.pLeavetype)
    {
        if (document.levelform.pLeavetype.disabled == false)
        	document.levelform.pLeavetype.checked = isChk;
        for (i = 0; i < document.levelform.pLeavetype.length; i++)
        {
                if (document.levelform.pLeavetype[i].disabled == false)
            	document.levelform.pLeavetype[i].checked = isChk;
        }
    }

    if (document.levelform.pholidaytype)
    {
        if (document.levelform.pholidaytype.disabled == false)
        	document.levelform.pholidaytype.checked = isChk;
        for (i = 0; i < document.levelform.pholidaytype.length; i++)
        {
                if (document.levelform.pholidaytype[i].disabled == false)
            	document.levelform.pholidaytype[i].checked = isChk;
        }
    }
    
    if (document.levelform.Banking)
    {
        if (document.levelform.Banking.disabled == false)
        	document.levelform.Banking.checked = isChk;
        for (i = 0; i < document.levelform.Banking.length; i++)
        {
                if (document.levelform.Banking[i].disabled == false)
            	document.levelform.Banking[i].checked = isChk;
        }
    }
//resvi
    if (document.levelform.Shift)
    {
        if (document.levelform.Shift.disabled == false)
        	document.levelform.Shift.checked = isChk;
        for (i = 0; i < document.levelform.Shift.length; i++)
        {
                if (document.levelform.Shift[i].disabled == false)
            	document.levelform.Shift[i].checked = isChk;
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

    if (document.levelform.payaddition)
    {
        if (document.levelform.payaddition.disabled == false)
        	document.levelform.payaddition.checked = isChk;
        for (i = 0; i < document.levelform.payaddition.length; i++)
        {
                if (document.levelform.payaddition[i].disabled == false)
            	document.levelform.payaddition[i].checked = isChk;
        }
    }


    if (document.levelform.paydeduction)
    {
        if (document.levelform.paydeduction.disabled == false)
        	document.levelform.paydeduction.checked = isChk;
        for (i = 0; i < document.levelform.paydeduction.length; i++)
        {
                if (document.levelform.paydeduction[i].disabled == false)
            	document.levelform.paydeduction[i].checked = isChk;
        }
    }




    if (document.levelform.UserPayroll)
    {
        if (document.levelform.UserPayroll.disabled == false)
        	document.levelform.UserPayroll.checked = isChk;
        for (i = 0; i < document.levelform.UserPayroll.length; i++)
        {
                if (document.levelform.UserPayroll[i].disabled == false)
            	document.levelform.UserPayroll[i].checked = isChk;
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

   if (document.levelform.PayrollPayment)
   {
       if (document.levelform.PayrollPayment.disabled == false)
       	document.levelform.PayrollPayment.checked = isChk;
       for (i = 0; i < document.levelform.PayrollPayment.length; i++)
       {
               if (document.levelform.PayrollPayment[i].disabled == false)
           	document.levelform.PayrollPayment[i].checked = isChk;
       }
   }

   if (document.levelform.pClaim)
   {
       if (document.levelform.pClaim.disabled == false)
       	document.levelform.pClaim.checked = isChk;
       for (i = 0; i < document.levelform.pClaim.length; i++)
       {
               if (document.levelform.pClaim[i].disabled == false)
           	document.levelform.pClaim[i].checked = isChk;
       }
   }

   if (document.levelform.pClaimPayment)
   {
       if (document.levelform.pClaimPayment.disabled == false)
       	document.levelform.pClaimPayment.checked = isChk;
       for (i = 0; i < document.levelform.pClaimPayment.length; i++)
       {
               if (document.levelform.pClaimPayment[i].disabled == false)
           	document.levelform.pClaimPayment[i].checked = isChk;
       }
   }

    if (document.levelform.payrollreport)
    {
        if (document.levelform.payrollreport.disabled == false)
        	document.levelform.payrollreport.checked = isChk;
        for (i = 0; i < document.levelform.payrollreport.length; i++)
        {
                if (document.levelform.payrollreport[i].disabled == false)
            	document.levelform.payrollreport[i].checked = isChk;
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


  if (document.levelform.Pdapayrollreport)
    {
        if (document.levelform.Pdapayrollreport.disabled == false)
        	document.levelform.Pdapayrollreport.checked = isChk;
        for (i = 0; i < document.levelform.Pdapayrollreport.length; i++)
        {
                if (document.levelform.Pdapayrollreport[i].disabled == false)
            	document.levelform.Pdapayrollreport[i].checked = isChk;
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
    if (document.levelform.UserDasnboard)
    {
        if (document.levelform.UserDasnboard.disabled == false)
        	document.levelform.UserDasnboard.checked = isChk;
        for (i = 0; i < document.levelform.UserDasnboard.length; i++)
        {
                if (document.levelform.UserDasnboard[i].disabled == false)
            	document.levelform.UserDasnboard[i].checked = isChk;
        }
    }
}

function checkallpayreports(isChk)
{
    if (document.levelform.payreports)
    {
        if (document.levelform.payreports.disabled == false)
        	document.levelform.payreports.checked = isChk;
        for (i = 0; i < document.levelform.payreports.length; i++)
        {
                if (document.levelform.payreports[i].disabled == false)
            	document.levelform.payreports[i].checked = isChk;
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

function checkAllDepartment(isChk)
{
    if (document.levelform.pdepartment)
    {
        if (document.levelform.pdepartment.disabled == false)
        	document.levelform.pdepartment.checked = isChk;
        for (i = 0; i < document.levelform.pdepartment.length; i++)
        {
                if (document.levelform.pdepartment[i].disabled == false)
            	document.levelform.pdepartment[i].checked = isChk;
        }
    }
}

function checkAllEmployeeType(isChk)
{
    if (document.levelform.userEmployeeType)
    {
        if (document.levelform.userEmployeeType.disabled == false)
        	document.levelform.userEmployeeType.checked = isChk;
        for (i = 0; i < document.levelform.userEmployeeType.length; i++)
        {
                if (document.levelform.userEmployeeType[i].disabled == false)
            	document.levelform.userEmployeeType[i].checked = isChk;
        }
    }
}

function checkAllSalaryType(isChk)
{
    if (document.levelform.psalarytype)
    {
        if (document.levelform.psalarytype.disabled == false)
        	document.levelform.psalarytype.checked = isChk;
        for (i = 0; i < document.levelform.psalarytype.length; i++)
        {
                if (document.levelform.psalarytype[i].disabled == false)
            	document.levelform.psalarytype[i].checked = isChk;
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


function checkAllLeaveType(isChk)
{
    if (document.levelform.pLeavetype)
    {
        if (document.levelform.pLeavetype.disabled == false)
        	document.levelform.pLeavetype.checked = isChk;
        for (i = 0; i < document.levelform.pLeavetype.length; i++)
        {
                if (document.levelform.pLeavetype[i].disabled == false)
            	document.levelform.pLeavetype[i].checked = isChk;
        }
    }
}

function checkAllHolidayType(isChk)
{
    if (document.levelform.pholidaytype)
    {
        if (document.levelform.pholidaytype.disabled == false)
        	document.levelform.pholidaytype.checked = isChk;
        for (i = 0; i < document.levelform.pholidaytype.length; i++)
        {
                if (document.levelform.pholidaytype[i].disabled == false)
            	document.levelform.pholidaytype[i].checked = isChk;
        }
    }
}

function checkAllBanking(isChk)
{
if (document.levelform.Banking)
{
    if (document.levelform.Banking.disabled == false)
    	document.levelform.Banking.checked = isChk;
    for (i = 0; i < document.levelform.Banking.length; i++)
    {
            if (document.levelform.Banking[i].disabled == false)
        	document.levelform.Banking[i].checked = isChk;
    }
}
}

//resvi adds

function checkAllShift(isChk)
{
if (document.levelform.Shift)
{
    if (document.levelform.Shift.disabled == false)
    	document.levelform.Shift.checked = isChk;
    for (i = 0; i < document.levelform.Shift.length; i++)
    {
            if (document.levelform.Shift[i].disabled == false)
        	document.levelform.Shift[i].checked = isChk;
    }
}
}
// ends

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

function checkAllAddition(isChk)
{
    if (document.levelform.payaddition)
    {
        if (document.levelform.payaddition.disabled == false)
        	document.levelform.payaddition.checked = isChk;
        for (i = 0; i < document.levelform.payaddition.length; i++)
        {
                if (document.levelform.payaddition[i].disabled == false)
            	document.levelform.payaddition[i].checked = isChk;
        }
    }
}

function checkAllDeduction(isChk)
{
    if (document.levelform.paydeduction)
    {
        if (document.levelform.paydeduction.disabled == false)
        	document.levelform.paydeduction.checked = isChk;
        for (i = 0; i < document.levelform.paydeduction.length; i++)
        {
                if (document.levelform.paydeduction[i].disabled == false)
            	document.levelform.paydeduction[i].checked = isChk;
        }
    }
}

function checkAllPayroll(isChk)
{
    if (document.levelform.UserPayroll)
    {
        if (document.levelform.UserPayroll.disabled == false)
        	document.levelform.UserPayroll.checked = isChk;
        for (i = 0; i < document.levelform.UserPayroll.length; i++)
        {
                if (document.levelform.UserPayroll[i].disabled == false)
            	document.levelform.UserPayroll[i].checked = isChk;
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

function checkAllPayrollPayment(isChk)
{
    if (document.levelform.PayrollPayment)
    {
        if (document.levelform.PayrollPayment.disabled == false)
        	document.levelform.PayrollPayment.checked = isChk;
        for (i = 0; i < document.levelform.PayrollPayment.length; i++)
        {
                if (document.levelform.PayrollPayment[i].disabled == false)
            	document.levelform.PayrollPayment[i].checked = isChk;
        }
    }
}

function checkAllClaim(isChk)
{
    if (document.levelform.pClaim)
    {
        if (document.levelform.pClaim.disabled == false)
        	document.levelform.pClaim.checked = isChk;
        for (i = 0; i < document.levelform.pClaim.length; i++)
        {
                if (document.levelform.pClaim[i].disabled == false)
            	document.levelform.pClaim[i].checked = isChk;
        }
    }
}

function checkAllClaimPayment(isChk)
{
    if (document.levelform.pClaimPayment)
    {
        if (document.levelform.pClaimPayment.disabled == false)
        	document.levelform.pClaimPayment.checked = isChk;
        for (i = 0; i < document.levelform.pClaimPayment.length; i++)
        {
                if (document.levelform.pClaimPayment[i].disabled == false)
            	document.levelform.pClaimPayment[i].checked = isChk;
        }
    }
}


function checkAllPayrollReort(isChk)
{
    if (document.levelform.payrollreport)
    {
        if (document.levelform.payrollreport.disabled == false)
        	document.levelform.payrollreport.checked = isChk;
        for (i = 0; i < document.levelform.payrollreport.length; i++)
        {
                if (document.levelform.payrollreport[i].disabled == false)
            	document.levelform.payrollreport[i].checked = isChk;
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

function checkAllPdapayrollreport(isChk)
{
    if (document.levelform.Pdapayrollreport)
    {
        if (document.levelform.Pdapayrollreport.disabled == false)
        	document.levelform.Pdapayrollreport.checked = isChk;
        for (i = 0; i < document.levelform.Pdapayrollreport.length; i++)
        {
                if (document.levelform.Pdapayrollreport[i].disabled == false)
            	document.levelform.Pdapayrollreport[i].checked = isChk;
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

function checkAlltb(field)
{
	for (i = 0; i < field.length; i++)
		System.out.print(field);
		field[i].checked = true ;
}
</script>

<jsp:useBean id="sl" class="com.track.gates.selectBean" />
<jsp:useBean id="ub" class="com.track.gates.userBean" />


<div class="container-fluid m-t-20">
	 <div class="box"> 
	 <!-- Thanzith Modified on 28.02.2022 -->
	 <ul class="breadcrumb backpageul">    
                <li class="underline-on-hover"><a href="../home">Dashboard</a></li>	
                 <li><a href="../useraccess/paysummary"><span class="underline-on-hover">Payroll User Access Rights Summary</span></a></li>	
                <li><label>Edit Payroll User Access Rights</label></li>                                   
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
		
 
<%!ArrayList al;

	//Method for selecting the CheckBox as 'Checked'
	public String checkOption(String str)
	
		{
		for (int i = 0; i < al.size(); i++) {
			if (str.equalsIgnoreCase(al.get(i).toString())){
				return "checked";
			}
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
%>
<script>
function onUpdate()
{
	var chk=confirm('Are you sure you would like to save?');
	if(chk){
	document.levelform.action="../jsp/userLevelPayrollSubmit.jsp?Action=Update";
	document.levelform.submit();}
	 else
	 {
		 return false;
	 }
}
</script>


<form name="form" class="form-horizontal" method="POST" action="../useraccess/payedit">
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
 <label class="control-label col-sm-4">Select Payroll User Access Rights</label>
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
		<%=sl.getUserLevelspayroll("0", plant, "DefaultGroup")%>
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
    		response.sendRedirect("../useraccess/payedit?LEVEL_NAME");
    		return;
    	}

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
    
   </form>
   
     
   
   
  <form class="form-horizontal" method="POST" name="levelform" action="../jsp/userLevelPayrollSubmit.jsp">
   <INPUT type="Hidden" name="LEVEL_NAME" value="<%=level_name%>">
	<P align="center"><font face="Verdana" color="black" size="2"> <b>GROUPS</b></font><font face="Verdana" color="black" size="2"><b> - <%=new String(level_name).toUpperCase()%></b></font></P>
	
	
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
                        <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="UserDasnboard" value="homePayroll" <%=disabled%> <%=checkOption("homePayroll")%>>
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
	                 <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name=pPrint_Config value="email_configpayslip" <%=disabled%> <%=checkOption("email_configpayslip")%> >
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
  <div class="row">
  		<div class="col-12 col-sm-12"><INPUT Type=Checkbox class="form-check-input"  style="border:0;" name="UserPayroll"  onclick="return checkAllPayroll(this.checked);">
                        &nbsp; Select/Unselect Payroll</div>
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
	                 <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name=pbulkpayslip value="payslipgenerate" <%=disabled%> <%=checkOption("payslipgenerate")%>>
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
                        <label class="checkbox-inline"><INPUT Type=Checkbox  style="border:0;" name="payreports" value="msPOList" <%=disabled%> <%=checkOption("msPOList")%>>
                        &nbsp; Activity Logs</label>   
                  
      
                          
                      </TABLE>
         </div>
  </div>
         
    
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
