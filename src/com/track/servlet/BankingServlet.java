package com.track.servlet;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.track.serviceImplementation.JournalEntry;
import com.track.util.IMLogger;
import com.track.util.StrUtils;

@WebServlet("/banking/*")
public class BankingServlet extends HttpServlet implements IMLogger {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String[] pathInfo = request.getPathInfo().split("/");
		String action = pathInfo[1];
		String username = StrUtils.fString((String) request.getSession().getAttribute("LOGIN_USER")).trim();
		String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
		
if(action.equalsIgnoreCase("summary")) {
			try {
				String msg = StrUtils.fString(request.getParameter("msg"));
				request.setAttribute("Msg", msg);
				RequestDispatcher rd = request.getRequestDispatcher("/jsp/BankSummary.jsp");
			rd.forward(request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
		if(action.equalsIgnoreCase("new")) {
			try {
				String msg = StrUtils.fString(request.getParameter("msg"));
				request.setAttribute("Msg", msg);
				RequestDispatcher rd = request.getRequestDispatcher("/jsp/createBank.jsp");
			rd.forward(request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
		if(action.equalsIgnoreCase("edit")) {
			try {
				String msg = StrUtils.fString(request.getParameter("msg"));
				request.setAttribute("Msg", msg);
				RequestDispatcher rd = request.getRequestDispatcher("/jsp/maint_Bank.jsp");
			rd.forward(request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
		if(action.equalsIgnoreCase("chartofaccounts")) {
			try {
				String msg = StrUtils.fString(request.getParameter("msg"));
				request.setAttribute("Msg", msg);
				RequestDispatcher rd = request.getRequestDispatcher("/jsp/chartOfAccounts.jsp");
			rd.forward(request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
		if(action.equalsIgnoreCase("chartofaccdetails")) {
			try {
				String msg = StrUtils.fString(request.getParameter("msg"));
				request.setAttribute("Msg", msg);
				RequestDispatcher rd = request.getRequestDispatcher("/jsp/chartOfAccountDetail.jsp");
			rd.forward(request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
		if(action.equalsIgnoreCase("chartofaccedit")) {
			try {
				String msg = StrUtils.fString(request.getParameter("msg"));
				request.setAttribute("Msg", msg);
				RequestDispatcher rd = request.getRequestDispatcher("/jsp/chartOfAccountEdit.jsp");
			rd.forward(request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
		if(action.equalsIgnoreCase("billpaysummary")) {
			try {
				String msg = StrUtils.fString(request.getParameter("msg"));
				request.setAttribute("Msg", msg);
				RequestDispatcher rd = request.getRequestDispatcher("/jsp/billPaymentSummary.jsp");
			rd.forward(request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
		if(action.equalsIgnoreCase("createbillpay")) {
			try {
				String msg = StrUtils.fString(request.getParameter("msg"));
				request.setAttribute("Msg", msg);
				RequestDispatcher rd = request.getRequestDispatcher("/jsp/createBillpayment.jsp");
			rd.forward(request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
		if(action.equalsIgnoreCase("billpaydetail")) {
			try {
				String msg = StrUtils.fString(request.getParameter("msg"));
				request.setAttribute("Msg", msg);
				RequestDispatcher rd = request.getRequestDispatcher("/jsp/billPaymentDetail.jsp");
			rd.forward(request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
		if(action.equalsIgnoreCase("editbillpay")) {
			try {
				String msg = StrUtils.fString(request.getParameter("msg"));
				request.setAttribute("Msg", msg);
				RequestDispatcher rd = request.getRequestDispatcher("/jsp/editBillPayment.jsp");
			rd.forward(request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
		if(action.equalsIgnoreCase("editvoucherpay")) {
			try {
				String msg = StrUtils.fString(request.getParameter("msg"));
				request.setAttribute("Msg", msg);
				RequestDispatcher rd = request.getRequestDispatcher("/jsp/editVoucherPayment.jsp");
			rd.forward(request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
		if(action.equalsIgnoreCase("pdcpaysummary")) {
			try {
				String msg = StrUtils.fString(request.getParameter("msg"));
				request.setAttribute("Msg", msg);
				RequestDispatcher rd = request.getRequestDispatcher("/jsp/pdcpaymentSummary.jsp");
			rd.forward(request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
		if(action.equalsIgnoreCase("pdcpayprocess")) {
			try {
				String msg = StrUtils.fString(request.getParameter("msg"));
				request.setAttribute("Msg", msg);
				RequestDispatcher rd = request.getRequestDispatcher("/jsp/pdcpaymentprocess.jsp");
			rd.forward(request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
		if(action.equalsIgnoreCase("editpdcpay")) {
			try {
				String msg = StrUtils.fString(request.getParameter("msg"));
				request.setAttribute("Msg", msg);
				RequestDispatcher rd = request.getRequestDispatcher("/jsp/editpdcpayment.jsp");
			rd.forward(request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
		
		if(action.equalsIgnoreCase("invoicepaysummary")) {
			try {
				String msg = StrUtils.fString(request.getParameter("msg"));
				request.setAttribute("Msg", msg);
				RequestDispatcher rd = request.getRequestDispatcher("/jsp/invoicePaymentSummary.jsp");
			rd.forward(request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
		if(action.equalsIgnoreCase("createinvoicepay")) {
			try {
				String msg = StrUtils.fString(request.getParameter("msg"));
				request.setAttribute("Msg", msg);
				RequestDispatcher rd = request.getRequestDispatcher("/jsp/invoicePaymentNew.jsp");
			rd.forward(request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
		if(action.equalsIgnoreCase("editinvoicevoucherpay")) {
			try {
				String msg = StrUtils.fString(request.getParameter("msg"));
				request.setAttribute("Msg", msg);
				RequestDispatcher rd = request.getRequestDispatcher("/jsp/voucherEditPayment.jsp");
			rd.forward(request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
		if(action.equalsIgnoreCase("invoicepaydetail")) {
			try {
				String msg = StrUtils.fString(request.getParameter("msg"));
				request.setAttribute("Msg", msg);
				RequestDispatcher rd = request.getRequestDispatcher("/jsp/invoicePaymentDetail.jsp");
			rd.forward(request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
		if(action.equalsIgnoreCase("editinvoicepay")) {
			try {
				String msg = StrUtils.fString(request.getParameter("msg"));
				request.setAttribute("Msg", msg);
				RequestDispatcher rd = request.getRequestDispatcher("/jsp/invoiceEditPayment.jsp");
			rd.forward(request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
		if(action.equalsIgnoreCase("pdcpayreceivesummary")) {
			try {
				String msg = StrUtils.fString(request.getParameter("msg"));
				request.setAttribute("Msg", msg);
				RequestDispatcher rd = request.getRequestDispatcher("/jsp/pdcpaymentReceiveSummary.jsp");
			rd.forward(request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
		if(action.equalsIgnoreCase("pdcpayreceiveprocess")) {
			try {
				String msg = StrUtils.fString(request.getParameter("msg"));
				request.setAttribute("Msg", msg);
				RequestDispatcher rd = request.getRequestDispatcher("/jsp/pdcpaymentReceiveprocess.jsp");
			rd.forward(request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
		if(action.equalsIgnoreCase("editpdcpayreceive")) {
			try {
				String msg = StrUtils.fString(request.getParameter("msg"));
				request.setAttribute("Msg", msg);
				RequestDispatcher rd = request.getRequestDispatcher("/jsp/editpdcpaymentReceive.jsp");
			rd.forward(request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}	
		if(action.equalsIgnoreCase("journalsummary")) {
			try {
				String msg = StrUtils.fString(request.getParameter("msg"));
				request.setAttribute("Msg", msg);
				RequestDispatcher rd = request.getRequestDispatcher("/jsp/journalsummary.jsp");
			rd.forward(request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}	
		if(action.equalsIgnoreCase("createjournal")) {
			try {
				String msg = StrUtils.fString(request.getParameter("msg"));
				request.setAttribute("Msg", msg);
				RequestDispatcher rd = request.getRequestDispatcher("/jsp/journalentry.jsp");
			rd.forward(request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}	
		if(action.equalsIgnoreCase("journaldetail")) {
			try {
				String msg = StrUtils.fString(request.getParameter("msg"));
				request.setAttribute("Msg", msg);
				RequestDispatcher rd = request.getRequestDispatcher("/jsp/journaldetail.jsp");
			rd.forward(request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}	
		if(action.equalsIgnoreCase("journaledit")) {
			try {
				String msg = StrUtils.fString(request.getParameter("msg"));
				request.setAttribute("Msg", msg);
				RequestDispatcher rd = request.getRequestDispatcher("/jsp/journaledit.jsp");
			rd.forward(request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}	
		if(action.equalsIgnoreCase("journalcopy")) {
			try {
				String msg = StrUtils.fString(request.getParameter("msg"));
				request.setAttribute("Msg", msg);
				RequestDispatcher rd = request.getRequestDispatcher("/jsp/journalcopy.jsp");
			rd.forward(request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
		
		if(action.equalsIgnoreCase("journaldelete")) {
			String msg="Unable to delete the Journal";
			try {
				String id = StrUtils.fString(request.getParameter("ID"));
				boolean result = new JournalEntry().DeleteJournal(plant, id);
				if(result) {
					msg = "Journal Deleted";
				}
				
		} catch (Exception e) {
			e.printStackTrace();
		}
			request.setAttribute("Msg", msg);
			RequestDispatcher rd = request.getRequestDispatcher("/jsp/journalsummary.jsp");
			rd.forward(request, response);
	}
		
		if(action.equalsIgnoreCase("contrasummary")) {
			try {
				String msg = StrUtils.fString(request.getParameter("msg"));
				request.setAttribute("Msg", msg);
				RequestDispatcher rd = request.getRequestDispatcher("/jsp/contrasummary.jsp");
			rd.forward(request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}	
		if(action.equalsIgnoreCase("createcontra")) {
			try {
				String msg = StrUtils.fString(request.getParameter("msg"));
				request.setAttribute("Msg", msg);
				RequestDispatcher rd = request.getRequestDispatcher("/jsp/contraentry.jsp");
			rd.forward(request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}	
		if(action.equalsIgnoreCase("contradetail")) {
			try {
				String msg = StrUtils.fString(request.getParameter("msg"));
				request.setAttribute("Msg", msg);
				RequestDispatcher rd = request.getRequestDispatcher("/jsp/contradetail.jsp");
			rd.forward(request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}	
		if(action.equalsIgnoreCase("contraedit")) {
			try {
				String msg = StrUtils.fString(request.getParameter("msg"));
				request.setAttribute("Msg", msg);
				RequestDispatcher rd = request.getRequestDispatcher("/jsp/contraedit.jsp");
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
		String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
//		String username = StrUtils.fString((String) request.getSession().getAttribute("LOGIN_USER")).trim();
//		String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
//		String currency = StrUtils.fString((String) request.getSession().getAttribute("BASE_CURRENCY"));
//		String region = StrUtils.fString((String) request.getSession().getAttribute("REGION"));
		
	if(action.equalsIgnoreCase("summary")) {
			
			
			try {
				String msg = StrUtils.fString(request.getParameter("msg"));
				request.setAttribute("Msg", msg);
				RequestDispatcher rd = request.getRequestDispatcher("/jsp/BankSummary.jsp");
			rd.forward(request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	if(action.equalsIgnoreCase("new")) {
		
		
		try {
			String msg = StrUtils.fString(request.getParameter("msg"));
			request.setAttribute("Msg", msg);
			RequestDispatcher rd = request.getRequestDispatcher("/jsp/createBank.jsp");
		rd.forward(request, response);
	} catch (Exception e) {
		e.printStackTrace();
	}
}
	if(action.equalsIgnoreCase("edit")) {
		try {
			String msg = StrUtils.fString(request.getParameter("msg"));
			request.setAttribute("Msg", msg);
			RequestDispatcher rd = request.getRequestDispatcher("/jsp/maint_Bank.jsp");
		rd.forward(request, response);
	} catch (Exception e) {
		e.printStackTrace();
	}
}

	if(action.equalsIgnoreCase("chartofaccounts")) {
		try {
			String msg = StrUtils.fString(request.getParameter("msg"));
			request.setAttribute("Msg", msg);
			RequestDispatcher rd = request.getRequestDispatcher("/jsp/chartOfAccounts.jsp");
		rd.forward(request, response);
	} catch (Exception e) {
		e.printStackTrace();
	}
}
	if(action.equalsIgnoreCase("chartofaccdetails")) {
		try {
			String msg = StrUtils.fString(request.getParameter("msg"));
			request.setAttribute("Msg", msg);
			RequestDispatcher rd = request.getRequestDispatcher("/jsp/chartOfAccountDetail.jsp");
		rd.forward(request, response);
	} catch (Exception e) {
		e.printStackTrace();
	}
}
	if(action.equalsIgnoreCase("chartofaccedit")) {
		try {
			String msg = StrUtils.fString(request.getParameter("msg"));
			request.setAttribute("Msg", msg);
			RequestDispatcher rd = request.getRequestDispatcher("/jsp/chartOfAccountEdit.jsp");
		rd.forward(request, response);
	} catch (Exception e) {
		e.printStackTrace();
	}
}
	if(action.equalsIgnoreCase("billpaysummary")) {
		try {
			String msg = StrUtils.fString(request.getParameter("msg"));
			request.setAttribute("Msg", msg);
			RequestDispatcher rd = request.getRequestDispatcher("/jsp/billPaymentSummary.jsp");
		rd.forward(request, response);
	} catch (Exception e) {
		e.printStackTrace();
	}
}
	if(action.equalsIgnoreCase("createbillpay")) {
		try {
			String msg = StrUtils.fString(request.getParameter("msg"));
			request.setAttribute("Msg", msg);
			RequestDispatcher rd = request.getRequestDispatcher("/jsp/createBillpayment.jsp");
		rd.forward(request, response);
	} catch (Exception e) {
		e.printStackTrace();
	}
}
	if(action.equalsIgnoreCase("billpaydetail")) {
		try {
			String msg = StrUtils.fString(request.getParameter("msg"));
			request.setAttribute("Msg", msg);
			RequestDispatcher rd = request.getRequestDispatcher("/jsp/billPaymentDetail.jsp");
		rd.forward(request, response);
	} catch (Exception e) {
		e.printStackTrace();
	}
}
	if(action.equalsIgnoreCase("editbillpay")) {
		try {
			String msg = StrUtils.fString(request.getParameter("msg"));
			request.setAttribute("Msg", msg);
			RequestDispatcher rd = request.getRequestDispatcher("/jsp/editBillPayment.jsp");
		rd.forward(request, response);
	} catch (Exception e) {
		e.printStackTrace();
	}
}
	if(action.equalsIgnoreCase("editvoucherpay")) {
		try {
			String msg = StrUtils.fString(request.getParameter("msg"));
			request.setAttribute("Msg", msg);
			RequestDispatcher rd = request.getRequestDispatcher("/jsp/editVoucherPayment.jsp");
		rd.forward(request, response);
	} catch (Exception e) {
		e.printStackTrace();
	}
}
	if(action.equalsIgnoreCase("pdcpaysummary")) {
		try {
			String msg = StrUtils.fString(request.getParameter("msg"));
			request.setAttribute("Msg", msg);
			RequestDispatcher rd = request.getRequestDispatcher("/jsp/pdcpaymentSummary.jsp");
		rd.forward(request, response);
	} catch (Exception e) {
		e.printStackTrace();
	}
}
	if(action.equalsIgnoreCase("pdcpayprocess")) {
		try {
			String msg = StrUtils.fString(request.getParameter("msg"));
			request.setAttribute("Msg", msg);
			RequestDispatcher rd = request.getRequestDispatcher("/jsp/pdcpaymentprocess.jsp");
		rd.forward(request, response);
	} catch (Exception e) {
		e.printStackTrace();
	}
}
	if(action.equalsIgnoreCase("editpdcpay")) {
		try {
			String msg = StrUtils.fString(request.getParameter("msg"));
			request.setAttribute("Msg", msg);
			RequestDispatcher rd = request.getRequestDispatcher("/jsp/editpdcpayment.jsp");
		rd.forward(request, response);
	} catch (Exception e) {
		e.printStackTrace();
	}
}
	if(action.equalsIgnoreCase("invoicepaysummary")) {
		try {
			String msg = StrUtils.fString(request.getParameter("msg"));
			request.setAttribute("Msg", msg);
			RequestDispatcher rd = request.getRequestDispatcher("/jsp/invoicePaymentSummary.jsp");
		rd.forward(request, response);
	} catch (Exception e) {
		e.printStackTrace();
	}
}
	if(action.equalsIgnoreCase("createinvoicepay")) {
		try {
			String msg = StrUtils.fString(request.getParameter("msg"));
			request.setAttribute("Msg", msg);
			RequestDispatcher rd = request.getRequestDispatcher("/jsp/invoicePaymentNew.jsp");
		rd.forward(request, response);
	} catch (Exception e) {
		e.printStackTrace();
	}
}
	if(action.equalsIgnoreCase("editinvoicevoucherpay")) {
		try {
			String msg = StrUtils.fString(request.getParameter("msg"));
			request.setAttribute("Msg", msg);
			RequestDispatcher rd = request.getRequestDispatcher("/jsp/voucherEditPayment.jsp");
		rd.forward(request, response);
	} catch (Exception e) {
		e.printStackTrace();
	}
}
	if(action.equalsIgnoreCase("invoicepaydetail")) {
		try {
			String msg = StrUtils.fString(request.getParameter("msg"));
			request.setAttribute("Msg", msg);
			RequestDispatcher rd = request.getRequestDispatcher("/jsp/invoicePaymentDetail.jsp");
		rd.forward(request, response);
	} catch (Exception e) {
		e.printStackTrace();
	}
}
	if(action.equalsIgnoreCase("editinvoicepay")) {
		try {
			String msg = StrUtils.fString(request.getParameter("msg"));
			request.setAttribute("Msg", msg);
			RequestDispatcher rd = request.getRequestDispatcher("/jsp/invoiceEditPayment.jsp");
		rd.forward(request, response);
	} catch (Exception e) {
		e.printStackTrace();
	}
}
	if(action.equalsIgnoreCase("pdcpayreceivesummary")) {
		try {
			String msg = StrUtils.fString(request.getParameter("msg"));
			request.setAttribute("Msg", msg);
			RequestDispatcher rd = request.getRequestDispatcher("/jsp/pdcpaymentReceiveSummary.jsp");
		rd.forward(request, response);
	} catch (Exception e) {
		e.printStackTrace();
	}
}
	if(action.equalsIgnoreCase("pdcpayreceiveprocess")) {
		try {
			String msg = StrUtils.fString(request.getParameter("msg"));
			request.setAttribute("Msg", msg);
			RequestDispatcher rd = request.getRequestDispatcher("/jsp/pdcpaymentReceiveprocess.jsp");
		rd.forward(request, response);
	} catch (Exception e) {
		e.printStackTrace();
	}
}
	if(action.equalsIgnoreCase("editpdcpayreceive")) {
		try {
			String msg = StrUtils.fString(request.getParameter("msg"));
			request.setAttribute("Msg", msg);
			RequestDispatcher rd = request.getRequestDispatcher("/jsp/editpdcpaymentReceive.jsp");
		rd.forward(request, response);
	} catch (Exception e) {
		e.printStackTrace();
	}
}
	if(action.equalsIgnoreCase("journalsummary")) {
		try {
			String msg = StrUtils.fString(request.getParameter("msg"));
			request.setAttribute("Msg", msg);
			RequestDispatcher rd = request.getRequestDispatcher("/jsp/journalsummary.jsp");
		rd.forward(request, response);
	} catch (Exception e) {
		e.printStackTrace();
	}
}	
	if(action.equalsIgnoreCase("createjournal")) {
		try {
			String msg = StrUtils.fString(request.getParameter("msg"));
			request.setAttribute("Msg", msg);
			RequestDispatcher rd = request.getRequestDispatcher("/jsp/journalentry.jsp");
		rd.forward(request, response);
	} catch (Exception e) {
		e.printStackTrace();
	}
}	
	if(action.equalsIgnoreCase("journaldetail")) {
		try {
			String msg = StrUtils.fString(request.getParameter("msg"));
			request.setAttribute("Msg", msg);
			RequestDispatcher rd = request.getRequestDispatcher("/jsp/journaldetail.jsp");
		rd.forward(request, response);
	} catch (Exception e) {
		e.printStackTrace();
	}
}	
	if(action.equalsIgnoreCase("journaledit")) {
		try {
			String msg = StrUtils.fString(request.getParameter("msg"));
			request.setAttribute("Msg", msg);
			RequestDispatcher rd = request.getRequestDispatcher("/jsp/journaledit.jsp");
		rd.forward(request, response);
	} catch (Exception e) {
		e.printStackTrace();
	}
}	
	if(action.equalsIgnoreCase("journalcopy")) {
		try {
			String msg = StrUtils.fString(request.getParameter("msg"));
			request.setAttribute("Msg", msg);
			RequestDispatcher rd = request.getRequestDispatcher("/jsp/journalcopy.jsp");
		rd.forward(request, response);
	} catch (Exception e) {
		e.printStackTrace();
	}
}
	
	if(action.equalsIgnoreCase("journaldelete")) {
		String msg="Unable to delete the Journal";
		try {
			String id = StrUtils.fString(request.getParameter("ID"));
			boolean result = new JournalEntry().DeleteJournal(plant, id);
			if(result) {
				msg = "Journal Deleted";
			}
			
	} catch (Exception e) {
		e.printStackTrace();
	}
		request.setAttribute("Msg", msg);
		RequestDispatcher rd = request.getRequestDispatcher("/jsp/journalsummary.jsp");
		rd.forward(request, response);
}
	if(action.equalsIgnoreCase("contrasummary")) {
		try {
			String msg = StrUtils.fString(request.getParameter("msg"));
			request.setAttribute("Msg", msg);
			RequestDispatcher rd = request.getRequestDispatcher("/jsp/contrasummary.jsp");
		rd.forward(request, response);
	} catch (Exception e) {
		e.printStackTrace();
	}
}	
	if(action.equalsIgnoreCase("createcontra")) {
		try {
			String msg = StrUtils.fString(request.getParameter("msg"));
			request.setAttribute("Msg", msg);
			RequestDispatcher rd = request.getRequestDispatcher("/jsp/contraentry.jsp");
		rd.forward(request, response);
	} catch (Exception e) {
		e.printStackTrace();
	}
}	
	if(action.equalsIgnoreCase("contradetail")) {
		try {
			String msg = StrUtils.fString(request.getParameter("msg"));
			request.setAttribute("Msg", msg);
			RequestDispatcher rd = request.getRequestDispatcher("/jsp/contradetail.jsp");
		rd.forward(request, response);
	} catch (Exception e) {
		e.printStackTrace();
	}
}	
	if(action.equalsIgnoreCase("contraedit")) {
		try {
			String msg = StrUtils.fString(request.getParameter("msg"));
			request.setAttribute("Msg", msg);
			RequestDispatcher rd = request.getRequestDispatcher("/jsp/contraedit.jsp");
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

		