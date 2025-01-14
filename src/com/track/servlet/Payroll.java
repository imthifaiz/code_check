package com.track.servlet;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.UserTransaction;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.track.constants.IConstants;
import com.track.constants.IDBConstants;
import com.track.constants.TransactionConstants;
import com.track.dao.LocMstTwoDAO;
import com.track.dao.LocTypeDAO;
import com.track.dao.MovHisDAO;
import com.track.dao.PlantMstDAO;
import com.track.dao.TblControlDAO;
import com.track.db.object.LOC_TYPE_MST2;
import com.track.db.util.LocTypeUtil;
import com.track.db.util.MasterUtil;
import com.track.db.util.TblControlUtil;
import com.track.gates.DbBean;
import com.track.gates.selectBean;
import com.track.gates.userBean;
import com.track.util.DateUtils;
import com.track.util.FileHandling;
import com.track.util.IMLogger;
import com.track.util.StrUtils;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@WebServlet("/payroll/*")
public class Payroll extends HttpServlet implements IMLogger {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String[] pathInfo = request.getPathInfo().split("/");
		String action = pathInfo[1];
		String username = StrUtils.fString((String) request.getSession().getAttribute("LOGIN_USER")).trim();
		String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
	if(action.equalsIgnoreCase("employee")) {
			
			
			try {
				String msg = StrUtils.fString(request.getParameter("msg"));
				request.setAttribute("Msg", msg);
				RequestDispatcher rd = request.getRequestDispatcher("/jsp/employeeSummary.jsp");
			rd.forward(request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	

if(action.equalsIgnoreCase("payslipgenerate")) {
	
	try {
		String msg = StrUtils.fString(request.getParameter("msg"));
		request.setAttribute("Msg", msg);
		RequestDispatcher rd = request.getRequestDispatcher("/jsp/printPayslipGenerate.jsp");
		rd.forward(request, response);
	} catch (Exception e) {
		e.printStackTrace();
	}
}

	if(action.equalsIgnoreCase("importemployee")) {
		
		
		try {
			String msg = StrUtils.fString(request.getParameter("msg"));
			request.setAttribute("Msg", msg);
			RequestDispatcher rd = request.getRequestDispatcher("/jsp/importEmployeeExcelSheet.jsp");
		rd.forward(request, response);
	} catch (Exception e) {
		e.printStackTrace();
	}
}
	
	if(action.equalsIgnoreCase("importempleave")) {
		
		
		try {
			String msg = StrUtils.fString(request.getParameter("msg"));
			request.setAttribute("Msg", msg);
			RequestDispatcher rd = request.getRequestDispatcher("/jsp/importEmployeeLeaveDetExcelSheet.jsp");
		rd.forward(request, response);
	} catch (Exception e) {
		e.printStackTrace();
	}
}
   
if(action.equalsIgnoreCase("importempsalary")) {
		
		
		try {
			String msg = StrUtils.fString(request.getParameter("msg"));
			request.setAttribute("Msg", msg);
			RequestDispatcher rd = request.getRequestDispatcher("/jsp/importEmployeeSalaryDetExcelSheet.jsp");
		rd.forward(request, response);
	} catch (Exception e) {
		e.printStackTrace();
	}
}
if(action.equalsIgnoreCase("createemployee")) {
	
	
	try {
		String msg = StrUtils.fString(request.getParameter("msg"));
		request.setAttribute("Msg", msg);
		RequestDispatcher rd = request.getRequestDispatcher("/jsp/create_employee.jsp");
	rd.forward(request, response);
} catch (Exception e) {
	e.printStackTrace();
}
}
if(action.equalsIgnoreCase("editemployee")) {
	
	
	try {
		String msg = StrUtils.fString(request.getParameter("msg"));
		request.setAttribute("Msg", msg);
		RequestDispatcher rd = request.getRequestDispatcher("/jsp/maint_employee.jsp");
	rd.forward(request, response);
} catch (Exception e) {
	e.printStackTrace();
}
}

if(action.equalsIgnoreCase("employeetype")) {
	
	
	try {
		String msg = StrUtils.fString(request.getParameter("msg"));
		request.setAttribute("Msg", msg);
		RequestDispatcher rd = request.getRequestDispatcher("/jsp/EmployeeTypeSummary.jsp");
	rd.forward(request, response);
} catch (Exception e) {
	e.printStackTrace();
}
}
if(action.equalsIgnoreCase("createemployeetype")) {
	
	
	try {
		String msg = StrUtils.fString(request.getParameter("msg"));
		request.setAttribute("Msg", msg);
		RequestDispatcher rd = request.getRequestDispatcher("/jsp/CreateEmpType.jsp");
	rd.forward(request, response);
} catch (Exception e) {
	e.printStackTrace();
}
}
if(action.equalsIgnoreCase("editemployeetype")) {
	
	
	try {
		String msg = StrUtils.fString(request.getParameter("msg"));
		request.setAttribute("Msg", msg);
		RequestDispatcher rd = request.getRequestDispatcher("/jsp/EditEmployeeType.jsp");
	rd.forward(request, response);
} catch (Exception e) {
	e.printStackTrace();
}
}
if(action.equalsIgnoreCase("department")) {
	
	
	try {
		String msg = StrUtils.fString(request.getParameter("msg"));
		request.setAttribute("Msg", msg);
		RequestDispatcher rd = request.getRequestDispatcher("/jsp/DepartmentSummary.jsp");
	rd.forward(request, response);
} catch (Exception e) {
	e.printStackTrace();
}
}
if(action.equalsIgnoreCase("createdepartment")) {
	
	
	try {
		String msg = StrUtils.fString(request.getParameter("msg"));
		request.setAttribute("Msg", msg);
		RequestDispatcher rd = request.getRequestDispatcher("/jsp/CreateDepartment.jsp");
	rd.forward(request, response);
} catch (Exception e) {
	e.printStackTrace();
}
}
if(action.equalsIgnoreCase("editdepartment")) {
	
	
	try {
		String msg = StrUtils.fString(request.getParameter("msg"));
		request.setAttribute("Msg", msg);
		RequestDispatcher rd = request.getRequestDispatcher("/jsp/EditDepartment.jsp");
	rd.forward(request, response);
} catch (Exception e) {
	e.printStackTrace();
}
}
		
if(action.equalsIgnoreCase("editsalary")) {
	
	
	try {
		String msg = StrUtils.fString(request.getParameter("msg"));
		request.setAttribute("Msg", msg);
		RequestDispatcher rd = request.getRequestDispatcher("/jsp/EditSalary.jsp");
	rd.forward(request, response);
} catch (Exception e) {
	e.printStackTrace();
}
}
if(action.equalsIgnoreCase("employeesalary")) {
	
	
	try {
		String msg = StrUtils.fString(request.getParameter("msg"));
		request.setAttribute("Msg", msg);
		RequestDispatcher rd = request.getRequestDispatcher("/jsp/EmployeeSalarySummary.jsp");
	rd.forward(request, response);
} catch (Exception e) {
	e.printStackTrace();
}
}
if(action.equalsIgnoreCase("createsalary")) {
	
	
	try {
		String msg = StrUtils.fString(request.getParameter("msg"));
		request.setAttribute("Msg", msg);
		RequestDispatcher rd = request.getRequestDispatcher("/jsp/CreateSalary.jsp");
	rd.forward(request, response);
} catch (Exception e) {
	e.printStackTrace();
}
}
	

if(action.equalsIgnoreCase("leavetype")) {
	
	
	try {
		String msg = StrUtils.fString(request.getParameter("msg"));
		request.setAttribute("Msg", msg);
		RequestDispatcher rd = request.getRequestDispatcher("/jsp/LeaveTypeSummary.jsp");
	rd.forward(request, response);
} catch (Exception e) {
	e.printStackTrace();
}
}
if(action.equalsIgnoreCase("createleavetype")) {
	
	
	try {
		String msg = StrUtils.fString(request.getParameter("msg"));
		request.setAttribute("Msg", msg);
		RequestDispatcher rd = request.getRequestDispatcher("/jsp/CreateLeaveType.jsp");
	rd.forward(request, response);
} catch (Exception e) {
	e.printStackTrace();
}
}
if(action.equalsIgnoreCase("editleavetype")) {
	
	
	try {
		String msg = StrUtils.fString(request.getParameter("msg"));
		request.setAttribute("Msg", msg);
		RequestDispatcher rd = request.getRequestDispatcher("/jsp/EditLeaveType.jsp");
	rd.forward(request, response);
} catch (Exception e) {
	e.printStackTrace();
}
}

if(action.equalsIgnoreCase("holiday")) {
	
	
	try {
		String msg = StrUtils.fString(request.getParameter("msg"));
		request.setAttribute("Msg", msg);
		RequestDispatcher rd = request.getRequestDispatcher("/jsp/HolidayMstSummary.jsp");
	rd.forward(request, response);
} catch (Exception e) {
	e.printStackTrace();
}
}
if(action.equalsIgnoreCase("importholiday")) {
	
	
	try {
		String msg = StrUtils.fString(request.getParameter("msg"));
		request.setAttribute("Msg", msg);
		RequestDispatcher rd = request.getRequestDispatcher("/jsp/importHolidayMst.jsp");
	rd.forward(request, response);
} catch (Exception e) {
	e.printStackTrace();
}
}
if(action.equalsIgnoreCase("createholiday")) {
	
	
	try {
		String msg = StrUtils.fString(request.getParameter("msg"));
		request.setAttribute("Msg", msg);
		RequestDispatcher rd = request.getRequestDispatcher("/jsp/createHolidayMst.jsp");
	rd.forward(request, response);
} catch (Exception e) {
	e.printStackTrace();
}
}

if(action.equalsIgnoreCase("editholiday")) {
	
	
	try {
		String msg = StrUtils.fString(request.getParameter("msg"));
		request.setAttribute("Msg", msg);
		RequestDispatcher rd = request.getRequestDispatcher("/jsp/EditHolidayMst.jsp");
	rd.forward(request, response);
} catch (Exception e) {
	e.printStackTrace();
}
}


if(action.equalsIgnoreCase("addsummary")) {
	
	
	try {
		String msg = StrUtils.fString(request.getParameter("msg"));
		request.setAttribute("Msg", msg);
		RequestDispatcher rd = request.getRequestDispatcher("/jsp/payrolladdmstsummary.jsp");
	rd.forward(request, response);
} catch (Exception e) {
	e.printStackTrace();
}
}
if(action.equalsIgnoreCase("createadd")) {
	
	
	try {
		String msg = StrUtils.fString(request.getParameter("msg"));
		request.setAttribute("Msg", msg);
		RequestDispatcher rd = request.getRequestDispatcher("/jsp/createpayrolladditionmst.jsp");
	rd.forward(request, response);
} catch (Exception e) {
	e.printStackTrace();
}
}

if(action.equalsIgnoreCase("editadd")) {
	
	
	try {
		String msg = StrUtils.fString(request.getParameter("msg"));
		request.setAttribute("Msg", msg);
		RequestDispatcher rd = request.getRequestDispatcher("/jsp/EditPayrolladdmst.jsp");
	rd.forward(request, response);
} catch (Exception e) {
	e.printStackTrace();
}
}


if(action.equalsIgnoreCase("deduction")) {
	
	
	try {
		String msg = StrUtils.fString(request.getParameter("msg"));
		request.setAttribute("Msg", msg);
		RequestDispatcher rd = request.getRequestDispatcher("/jsp/PayrollDeductionSummary.jsp");
	rd.forward(request, response);
} catch (Exception e) {
	e.printStackTrace();
}
}

if(action.equalsIgnoreCase("creatededuction")) {
	
	
	try {
		String msg = StrUtils.fString(request.getParameter("msg"));
		request.setAttribute("Msg", msg);
		RequestDispatcher rd = request.getRequestDispatcher("/jsp/CreatePayrollDeduction.jsp");
	rd.forward(request, response);
} catch (Exception e) {
	e.printStackTrace();
}
}


if(action.equalsIgnoreCase("editdeduction")) {
	
	
	try {
		String msg = StrUtils.fString(request.getParameter("msg"));
		request.setAttribute("Msg", msg);
		RequestDispatcher rd = request.getRequestDispatcher("/jsp/EditPayrollDeduction.jsp");
	rd.forward(request, response);
} catch (Exception e) {
	e.printStackTrace();
}
}


if(action.equalsIgnoreCase("summary")) {
	
	
	try {
		String msg = StrUtils.fString(request.getParameter("msg"));
		request.setAttribute("Msg", msg);
		RequestDispatcher rd = request.getRequestDispatcher("/jsp/PayrollSummary.jsp");
	rd.forward(request, response);
} catch (Exception e) {
	e.printStackTrace();
}
}



if(action.equalsIgnoreCase("new")) {
	
	
	try {
		String msg = StrUtils.fString(request.getParameter("msg"));
		request.setAttribute("Msg", msg);
		RequestDispatcher rd = request.getRequestDispatcher("/jsp/CreatePayroll.jsp");
	rd.forward(request, response);
} catch (Exception e) {
	e.printStackTrace();
}
}

if(action.equalsIgnoreCase("detail")) {
	
	
	try {
		String msg = StrUtils.fString(request.getParameter("msg"));
		request.setAttribute("Msg", msg);
		RequestDispatcher rd = request.getRequestDispatcher("/jsp/Payrolldetail.jsp");
	rd.forward(request, response);
} catch (Exception e) {
	e.printStackTrace();
}
}


if(action.equalsIgnoreCase("edit")) {
	
	
	try {
		String msg = StrUtils.fString(request.getParameter("msg"));
		request.setAttribute("Msg", msg);
		RequestDispatcher rd = request.getRequestDispatcher("/jsp/EditPayroll.jsp");
	rd.forward(request, response);
} catch (Exception e) {
	e.printStackTrace();
}
}




if(action.equalsIgnoreCase("paymentsummary")) {
	
	
	try {
		String msg = StrUtils.fString(request.getParameter("msg"));
		request.setAttribute("Msg", msg);
		RequestDispatcher rd = request.getRequestDispatcher("/jsp/PayrollPaymentSummary.jsp");
	rd.forward(request, response);
} catch (Exception e) {
	e.printStackTrace();
}
}

if(action.equalsIgnoreCase("paymentprocess")) {
	
	
	try {
		String msg = StrUtils.fString(request.getParameter("msg"));
		request.setAttribute("Msg", msg);
		RequestDispatcher rd = request.getRequestDispatcher("/jsp/PayrollProcessing.jsp");
	rd.forward(request, response);
} catch (Exception e) {
	e.printStackTrace();
}
}



if(action.equalsIgnoreCase("paymentdetail")) {
	
	
	try {
		String msg = StrUtils.fString(request.getParameter("msg"));
		request.setAttribute("Msg", msg);
		RequestDispatcher rd = request.getRequestDispatcher("/jsp/PayrollPaymentDetail.jsp");
	rd.forward(request, response);
} catch (Exception e) {
	e.printStackTrace();
}
}



if(action.equalsIgnoreCase("editpayment")) {
	
	
	try {
		String msg = StrUtils.fString(request.getParameter("msg"));
		request.setAttribute("Msg", msg);
		RequestDispatcher rd = request.getRequestDispatcher("/jsp/EditPayrollPayment.jsp");
	rd.forward(request, response);
} catch (Exception e) {
	e.printStackTrace();
}
}


if(action.equalsIgnoreCase("claimsummary")) {
	
	
	try {
		String msg = StrUtils.fString(request.getParameter("msg"));
		request.setAttribute("Msg", msg);
		RequestDispatcher rd = request.getRequestDispatcher("/jsp/HrClaimSummary.jsp");
	rd.forward(request, response);
} catch (Exception e) {
	e.printStackTrace();
}
}

if(action.equalsIgnoreCase("processclaim")) {
	
	
	try {
		String msg = StrUtils.fString(request.getParameter("msg"));
		request.setAttribute("Msg", msg);
		RequestDispatcher rd = request.getRequestDispatcher("/jsp/ProcessClaim.jsp");
	rd.forward(request, response);
} catch (Exception e) {
	e.printStackTrace();
}
}



if(action.equalsIgnoreCase("claimpayment")) {
	
	
	try {
		String msg = StrUtils.fString(request.getParameter("msg"));
		request.setAttribute("Msg", msg);
		RequestDispatcher rd = request.getRequestDispatcher("/jsp/ClaimPaymentSummary.jsp");
	rd.forward(request, response);
} catch (Exception e) {
	e.printStackTrace();
}
}

if(action.equalsIgnoreCase("claimpaymentdetail")) {
	
	
	try {
		String msg = StrUtils.fString(request.getParameter("msg"));
		request.setAttribute("Msg", msg);
		RequestDispatcher rd = request.getRequestDispatcher("/jsp/ClaimPaymentDetail.jsp");
	rd.forward(request, response);
} catch (Exception e) {
	e.printStackTrace();
}
}


if(action.equalsIgnoreCase("editclaimpayment")) {
	
	
	try {
		String msg = StrUtils.fString(request.getParameter("msg"));
		request.setAttribute("Msg", msg);
		RequestDispatcher rd = request.getRequestDispatcher("/jsp/EditClaimPayment.jsp");
	rd.forward(request, response);
} catch (Exception e) {
	e.printStackTrace();
}
}


if(action.equalsIgnoreCase("reports")) {
	
	
	try {
		String msg = StrUtils.fString(request.getParameter("msg"));
		request.setAttribute("Msg", msg);
		RequestDispatcher rd = request.getRequestDispatcher("/jsp/PayrollReport.jsp");
	rd.forward(request, response);
} catch (Exception e) {
	e.printStackTrace();
}
}

if(action.equalsIgnoreCase("detailedreport")) {
	
	
	try {
		String msg = StrUtils.fString(request.getParameter("msg"));
		request.setAttribute("Msg", msg);
		RequestDispatcher rd = request.getRequestDispatcher("/jsp/PayrollDetailedReport.jsp");
	rd.forward(request, response);
} catch (Exception e) {
	e.printStackTrace();
}
}


if(action.equalsIgnoreCase("payslip")) {
	
	
	try {
		String msg = StrUtils.fString(request.getParameter("msg"));
		request.setAttribute("Msg", msg);
		RequestDispatcher rd = request.getRequestDispatcher("/jsp/DownloadPayslip.jsp");
	rd.forward(request, response);
} catch (Exception e) {
	e.printStackTrace();
}
}

if(action.equalsIgnoreCase("editemployee")) {
	
	
	try {
		String msg = StrUtils.fString(request.getParameter("msg"));
		request.setAttribute("Msg", msg);
		RequestDispatcher rd = request.getRequestDispatcher("/jsp/maint_employee.jsp");
	rd.forward(request, response);
} catch (Exception e) {
	e.printStackTrace();
}
}




}
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String[] pathInfo = request.getPathInfo().split("/");
		String action = pathInfo[1];
		String username = StrUtils.fString((String) request.getSession().getAttribute("LOGIN_USER")).trim();
		String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
		String currency = StrUtils.fString((String) request.getSession().getAttribute("BASE_CURRENCY"));
		String region = StrUtils.fString((String) request.getSession().getAttribute("REGION"));
		
	if(action.equalsIgnoreCase("employee")) {
			
			
			try {
				String msg = StrUtils.fString(request.getParameter("msg"));
				request.setAttribute("Msg", msg);
				RequestDispatcher rd = request.getRequestDispatcher("/jsp/employeeSummary.jsp");
			rd.forward(request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	if(action.equalsIgnoreCase("importemployee")) {
		
		
		try {
			String msg = StrUtils.fString(request.getParameter("msg"));
			request.setAttribute("Msg", msg);
			RequestDispatcher rd = request.getRequestDispatcher("/jsp/importEmployeeExcelSheet.jsp");
		rd.forward(request, response);
	} catch (Exception e) {
		e.printStackTrace();
	}
}
	
	if(action.equalsIgnoreCase("importempleave")) {
		
		
		try {
			String msg = StrUtils.fString(request.getParameter("msg"));
			request.setAttribute("Msg", msg);
			RequestDispatcher rd = request.getRequestDispatcher("/jsp/importEmployeeLeaveDetExcelSheet.jsp");
		rd.forward(request, response);
	} catch (Exception e) {
		e.printStackTrace();
	}
}
   
if(action.equalsIgnoreCase("importempsalary")) {
		
		
		try {
			String msg = StrUtils.fString(request.getParameter("msg"));
			request.setAttribute("Msg", msg);
			RequestDispatcher rd = request.getRequestDispatcher("/jsp/importEmployeeSalaryDetExcelSheet.jsp");
		rd.forward(request, response);
	} catch (Exception e) {
		e.printStackTrace();
	}
}
if(action.equalsIgnoreCase("createemployee")) {
	
	
	try {
		String msg = StrUtils.fString(request.getParameter("msg"));
		request.setAttribute("Msg", msg);
		RequestDispatcher rd = request.getRequestDispatcher("/jsp/create_employee.jsp");
	rd.forward(request, response);
} catch (Exception e) {
	e.printStackTrace();
}
}
if(action.equalsIgnoreCase("editemployee")) {
	
	
	try {
		String msg = StrUtils.fString(request.getParameter("msg"));
		request.setAttribute("Msg", msg);
		RequestDispatcher rd = request.getRequestDispatcher("/jsp/maint_employee.jsp");
	rd.forward(request, response);
} catch (Exception e) {
	e.printStackTrace();
}
}

if(action.equalsIgnoreCase("payslipgenerate")) {
	
	try {
		String msg = StrUtils.fString(request.getParameter("msg"));
		request.setAttribute("Msg", msg);
		RequestDispatcher rd = request.getRequestDispatcher("/jsp/printPayslipGenerate.jsp");
		rd.forward(request, response);
	} catch (Exception e) {
		e.printStackTrace();
	}
}

if(action.equalsIgnoreCase("employeetype")) {
	
	
	try {
		String msg = StrUtils.fString(request.getParameter("msg"));
		request.setAttribute("Msg", msg);
		RequestDispatcher rd = request.getRequestDispatcher("/jsp/EmployeeTypeSummary.jsp");
	rd.forward(request, response);
} catch (Exception e) {
	e.printStackTrace();
}
}
if(action.equalsIgnoreCase("createemployeetype")) {
	
	
	try {
		String msg = StrUtils.fString(request.getParameter("msg"));
		request.setAttribute("Msg", msg);
		RequestDispatcher rd = request.getRequestDispatcher("/jsp/CreateEmpType.jsp");
	rd.forward(request, response);
} catch (Exception e) {
	e.printStackTrace();
}
}
if(action.equalsIgnoreCase("editemployeetype")) {
	
	
	try {
		String msg = StrUtils.fString(request.getParameter("msg"));
		request.setAttribute("Msg", msg);
		RequestDispatcher rd = request.getRequestDispatcher("/jsp/EditEmployeeType.jsp");
	rd.forward(request, response);
} catch (Exception e) {
	e.printStackTrace();
}
}
if(action.equalsIgnoreCase("department")) {
	
	
	try {
		String msg = StrUtils.fString(request.getParameter("msg"));
		request.setAttribute("Msg", msg);
		RequestDispatcher rd = request.getRequestDispatcher("/jsp/DepartmentSummary.jsp");
	rd.forward(request, response);
} catch (Exception e) {
	e.printStackTrace();
}
}
if(action.equalsIgnoreCase("createdepartment")) {
	
	
	try {
		String msg = StrUtils.fString(request.getParameter("msg"));
		request.setAttribute("Msg", msg);
		RequestDispatcher rd = request.getRequestDispatcher("/jsp/CreateDepartment.jsp");
	rd.forward(request, response);
} catch (Exception e) {
	e.printStackTrace();
}
}
if(action.equalsIgnoreCase("editdepartment")) {
	
	
	try {
		String msg = StrUtils.fString(request.getParameter("msg"));
		request.setAttribute("Msg", msg);
		RequestDispatcher rd = request.getRequestDispatcher("/jsp/EditDepartment.jsp");
	rd.forward(request, response);
} catch (Exception e) {
	e.printStackTrace();
}
}
		
if(action.equalsIgnoreCase("editsalary")) {
	
	
	try {
		String msg = StrUtils.fString(request.getParameter("msg"));
		request.setAttribute("Msg", msg);
		RequestDispatcher rd = request.getRequestDispatcher("/jsp/EditSalary.jsp");
	rd.forward(request, response);
} catch (Exception e) {
	e.printStackTrace();
}
}
if(action.equalsIgnoreCase("employeesalary")) {
	
	
	try {
		String msg = StrUtils.fString(request.getParameter("msg"));
		request.setAttribute("Msg", msg);
		RequestDispatcher rd = request.getRequestDispatcher("/jsp/EmployeeSalarySummary.jsp");
	rd.forward(request, response);
} catch (Exception e) {
	e.printStackTrace();
}
}
if(action.equalsIgnoreCase("createsalary")) {
	
	
	try {
		String msg = StrUtils.fString(request.getParameter("msg"));
		request.setAttribute("Msg", msg);
		RequestDispatcher rd = request.getRequestDispatcher("/jsp/CreateSalary.jsp");
	rd.forward(request, response);
} catch (Exception e) {
	e.printStackTrace();
}
}
	

if(action.equalsIgnoreCase("leavetype")) {
	
	
	try {
		String msg = StrUtils.fString(request.getParameter("msg"));
		request.setAttribute("Msg", msg);
		RequestDispatcher rd = request.getRequestDispatcher("/jsp/LeaveTypeSummary.jsp");
	rd.forward(request, response);
} catch (Exception e) {
	e.printStackTrace();
}
}
if(action.equalsIgnoreCase("createleavetype")) {
	
	
	try {
		String msg = StrUtils.fString(request.getParameter("msg"));
		request.setAttribute("Msg", msg);
		RequestDispatcher rd = request.getRequestDispatcher("/jsp/CreateLeaveType.jsp");
	rd.forward(request, response);
} catch (Exception e) {
	e.printStackTrace();
}
}
if(action.equalsIgnoreCase("editleavetype")) {
	
	
	try {
		String msg = StrUtils.fString(request.getParameter("msg"));
		request.setAttribute("Msg", msg);
		RequestDispatcher rd = request.getRequestDispatcher("/jsp/EditLeaveType.jsp");
	rd.forward(request, response);
} catch (Exception e) {
	e.printStackTrace();
}
}

if(action.equalsIgnoreCase("holiday")) {
	
	
	try {
		String msg = StrUtils.fString(request.getParameter("msg"));
		request.setAttribute("Msg", msg);
		RequestDispatcher rd = request.getRequestDispatcher("/jsp/HolidayMstSummary.jsp");
	rd.forward(request, response);
} catch (Exception e) {
	e.printStackTrace();
}
}
if(action.equalsIgnoreCase("importholiday")) {
	
	
	try {
		String msg = StrUtils.fString(request.getParameter("msg"));
		request.setAttribute("Msg", msg);
		RequestDispatcher rd = request.getRequestDispatcher("/jsp/importHolidayMst.jsp");
	rd.forward(request, response);
} catch (Exception e) {
	e.printStackTrace();
}
}
if(action.equalsIgnoreCase("createholiday")) {
	
	
	try {
		String msg = StrUtils.fString(request.getParameter("msg"));
		request.setAttribute("Msg", msg);
		RequestDispatcher rd = request.getRequestDispatcher("/jsp/createHolidayMst.jsp");
	rd.forward(request, response);
} catch (Exception e) {
	e.printStackTrace();
}
}

if(action.equalsIgnoreCase("editholiday")) {
	
	
	try {
		String msg = StrUtils.fString(request.getParameter("msg"));
		request.setAttribute("Msg", msg);
		RequestDispatcher rd = request.getRequestDispatcher("/jsp/EditHolidayMst.jsp");
	rd.forward(request, response);
} catch (Exception e) {
	e.printStackTrace();
}
}


if(action.equalsIgnoreCase("addsummary")) {
	
	
	try {
		String msg = StrUtils.fString(request.getParameter("msg"));
		request.setAttribute("Msg", msg);
		RequestDispatcher rd = request.getRequestDispatcher("/jsp/payrolladdmstsummary.jsp");
	rd.forward(request, response);
} catch (Exception e) {
	e.printStackTrace();
}
}
if(action.equalsIgnoreCase("createadd")) {
	
	
	try {
		String msg = StrUtils.fString(request.getParameter("msg"));
		request.setAttribute("Msg", msg);
		RequestDispatcher rd = request.getRequestDispatcher("/jsp/createpayrolladditionmst.jsp");
	rd.forward(request, response);
} catch (Exception e) {
	e.printStackTrace();
}
}

if(action.equalsIgnoreCase("editadd")) {
	
	
	try {
		String msg = StrUtils.fString(request.getParameter("msg"));
		request.setAttribute("Msg", msg);
		RequestDispatcher rd = request.getRequestDispatcher("/jsp/EditPayrolladdmst.jsp");
	rd.forward(request, response);
} catch (Exception e) {
	e.printStackTrace();
}
}


if(action.equalsIgnoreCase("deduction")) {
	
	
	try {
		String msg = StrUtils.fString(request.getParameter("msg"));
		request.setAttribute("Msg", msg);
		RequestDispatcher rd = request.getRequestDispatcher("/jsp/PayrollDeductionSummary.jsp");
	rd.forward(request, response);
} catch (Exception e) {
	e.printStackTrace();
}
}

if(action.equalsIgnoreCase("creatededuction")) {
	
	
	try {
		String msg = StrUtils.fString(request.getParameter("msg"));
		request.setAttribute("Msg", msg);
		RequestDispatcher rd = request.getRequestDispatcher("/jsp/CreatePayrollDeduction.jsp");
	rd.forward(request, response);
} catch (Exception e) {
	e.printStackTrace();
}
}


if(action.equalsIgnoreCase("editdeduction")) {
	
	
	try {
		String msg = StrUtils.fString(request.getParameter("msg"));
		request.setAttribute("Msg", msg);
		RequestDispatcher rd = request.getRequestDispatcher("/jsp/EditPayrollDeduction.jsp");
	rd.forward(request, response);
} catch (Exception e) {
	e.printStackTrace();
}
}


if(action.equalsIgnoreCase("summary")) {
	
	
	try {
		String msg = StrUtils.fString(request.getParameter("msg"));
		request.setAttribute("Msg", msg);
		RequestDispatcher rd = request.getRequestDispatcher("/jsp/PayrollSummary.jsp");
	rd.forward(request, response);
} catch (Exception e) {
	e.printStackTrace();
}
}



if(action.equalsIgnoreCase("new")) {
	
	
	try {
		String msg = StrUtils.fString(request.getParameter("msg"));
		request.setAttribute("Msg", msg);
		RequestDispatcher rd = request.getRequestDispatcher("/jsp/CreatePayroll.jsp");
	rd.forward(request, response);
} catch (Exception e) {
	e.printStackTrace();
}
}

if(action.equalsIgnoreCase("detail")) {
	
	
	try {
		String msg = StrUtils.fString(request.getParameter("msg"));
		request.setAttribute("Msg", msg);
		RequestDispatcher rd = request.getRequestDispatcher("/jsp/Payrolldetail.jsp");
	rd.forward(request, response);
} catch (Exception e) {
	e.printStackTrace();
}
}


if(action.equalsIgnoreCase("edit")) {
	
	
	try {
		String msg = StrUtils.fString(request.getParameter("msg"));
		request.setAttribute("Msg", msg);
		RequestDispatcher rd = request.getRequestDispatcher("/jsp/EditPayroll.jsp");
	rd.forward(request, response);
} catch (Exception e) {
	e.printStackTrace();
}
}




if(action.equalsIgnoreCase("paymentsummary")) {
	
	
	try {
		String msg = StrUtils.fString(request.getParameter("msg"));
		request.setAttribute("Msg", msg);
		RequestDispatcher rd = request.getRequestDispatcher("/jsp/PayrollPaymentSummary.jsp");
	rd.forward(request, response);
} catch (Exception e) {
	e.printStackTrace();
}
}

if(action.equalsIgnoreCase("paymentprocess")) {
	
	
	try {
		String msg = StrUtils.fString(request.getParameter("msg"));
		request.setAttribute("Msg", msg);
		RequestDispatcher rd = request.getRequestDispatcher("/jsp/PayrollProcessing.jsp");
	rd.forward(request, response);
} catch (Exception e) {
	e.printStackTrace();
}
}



if(action.equalsIgnoreCase("paymentdetail")) {
	
	
	try {
		String msg = StrUtils.fString(request.getParameter("msg"));
		request.setAttribute("Msg", msg);
		RequestDispatcher rd = request.getRequestDispatcher("/jsp/PayrollPaymentDetail.jsp");
	rd.forward(request, response);
} catch (Exception e) {
	e.printStackTrace();
}
}



if(action.equalsIgnoreCase("editpayment")) {
	
	
	try {
		String msg = StrUtils.fString(request.getParameter("msg"));
		request.setAttribute("Msg", msg);
		RequestDispatcher rd = request.getRequestDispatcher("/jsp/EditPayrollPayment.jsp");
	rd.forward(request, response);
} catch (Exception e) {
	e.printStackTrace();
}
}


if(action.equalsIgnoreCase("claimsummary")) {
	
	
	try {
		String msg = StrUtils.fString(request.getParameter("msg"));
		request.setAttribute("Msg", msg);
		RequestDispatcher rd = request.getRequestDispatcher("/jsp/HrClaimSummary.jsp");
	rd.forward(request, response);
} catch (Exception e) {
	e.printStackTrace();
}
}

if(action.equalsIgnoreCase("processclaim")) {
	
	
	try {
		String msg = StrUtils.fString(request.getParameter("msg"));
		request.setAttribute("Msg", msg);
		RequestDispatcher rd = request.getRequestDispatcher("/jsp/ProcessClaim.jsp");
	rd.forward(request, response);
} catch (Exception e) {
	e.printStackTrace();
}
}



if(action.equalsIgnoreCase("claimpayment")) {
	
	
	try {
		String msg = StrUtils.fString(request.getParameter("msg"));
		request.setAttribute("Msg", msg);
		RequestDispatcher rd = request.getRequestDispatcher("/jsp/ClaimPaymentSummary.jsp");
	rd.forward(request, response);
} catch (Exception e) {
	e.printStackTrace();
}
}

if(action.equalsIgnoreCase("claimpaymentdetail")) {
	
	
	try {
		String msg = StrUtils.fString(request.getParameter("msg"));
		request.setAttribute("Msg", msg);
		RequestDispatcher rd = request.getRequestDispatcher("/jsp/ClaimPaymentDetail.jsp");
	rd.forward(request, response);
} catch (Exception e) {
	e.printStackTrace();
}
}


if(action.equalsIgnoreCase("editclaimpayment")) {
	
	
	try {
		String msg = StrUtils.fString(request.getParameter("msg"));
		request.setAttribute("Msg", msg);
		RequestDispatcher rd = request.getRequestDispatcher("/jsp/EditClaimPayment.jsp");
	rd.forward(request, response);
} catch (Exception e) {
	e.printStackTrace();
}
}


if(action.equalsIgnoreCase("reports")) {
	
	
	try {
		String msg = StrUtils.fString(request.getParameter("msg"));
		request.setAttribute("Msg", msg);
		RequestDispatcher rd = request.getRequestDispatcher("/jsp/PayrollReport.jsp");
	rd.forward(request, response);
} catch (Exception e) {
	e.printStackTrace();
}
}

if(action.equalsIgnoreCase("detailedreport")) {
	
	
	try {
		String msg = StrUtils.fString(request.getParameter("msg"));
		request.setAttribute("Msg", msg);
		RequestDispatcher rd = request.getRequestDispatcher("/jsp/PayrollDetailedReport.jsp");
	rd.forward(request, response);
} catch (Exception e) {
	e.printStackTrace();
}
}


if(action.equalsIgnoreCase("payslip")) {
	
	
	try {
		String msg = StrUtils.fString(request.getParameter("msg"));
		request.setAttribute("Msg", msg);
		RequestDispatcher rd = request.getRequestDispatcher("/jsp/DownloadPayslip.jsp");
	rd.forward(request, response);
} catch (Exception e) {
	e.printStackTrace();
}
}



}
	
	@Override
	public HashMap<String, String> populateMapData(String companyCode, String userCode) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setMapDataToLogger(HashMap<String, String> dataForLogging) {
		// TODO Auto-generated method stub
		
	}

}

		