package com.track.servlet;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.track.util.IMLogger;
import com.track.util.StrUtils;


@WebServlet("/receivable/*")
public class ReceivableServlet extends HttpServlet implements IMLogger {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String[] pathInfo = request.getPathInfo().split("/");
		String action = pathInfo[1];
//		String username = StrUtils.fString((String) request.getSession().getAttribute("LOGIN_USER")).trim();
//		String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
if(action.equalsIgnoreCase("invoice")) {
			
			
			try {
				String msg = StrUtils.fString(request.getParameter("msg"));
				request.setAttribute("Msg", msg);
				RequestDispatcher rd = request.getRequestDispatcher("/jsp/invoicedetails.jsp");
			rd.forward(request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}	
if(action.equalsIgnoreCase("payreceived")) {
	
	
	try {
		String msg = StrUtils.fString(request.getParameter("msg"));
		request.setAttribute("Msg", msg);
		RequestDispatcher rd = request.getRequestDispatcher("/jsp/paymentsreceived.jsp");
	rd.forward(request, response);
} catch (Exception e) {
	e.printStackTrace();
}
}

if(action.equalsIgnoreCase("cusbalances")) {
	
	
	try {
		String msg = StrUtils.fString(request.getParameter("msg"));
		request.setAttribute("Msg", msg);
		RequestDispatcher rd = request.getRequestDispatcher("/jsp/customerbalances.jsp");
	rd.forward(request, response);
} catch (Exception e) {
	e.printStackTrace();
}
}

if(action.equalsIgnoreCase("cuscreditdetails")) {
	
	
	try {
		String msg = StrUtils.fString(request.getParameter("msg"));
		request.setAttribute("Msg", msg);
		RequestDispatcher rd = request.getRequestDispatcher("/jsp/customercreditdetails.jsp");
	rd.forward(request, response);
} catch (Exception e) {
	e.printStackTrace();
}
}
if(action.equalsIgnoreCase("cusagingsummary")) {
	
	
	try {
		String msg = StrUtils.fString(request.getParameter("msg"));
		request.setAttribute("Msg", msg);
		RequestDispatcher rd = request.getRequestDispatcher("/jsp/invoiceAgingSummary.jsp");
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
//		String username = StrUtils.fString((String) request.getSession().getAttribute("LOGIN_USER")).trim();
//		String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
//		String currency = StrUtils.fString((String) request.getSession().getAttribute("BASE_CURRENCY"));
//		String region = StrUtils.fString((String) request.getSession().getAttribute("REGION"));
		
	if(action.equalsIgnoreCase("invoice")) {
			
			
			try {
				String msg = StrUtils.fString(request.getParameter("msg"));
				request.setAttribute("Msg", msg);
				RequestDispatcher rd = request.getRequestDispatcher("/jsp/invoicedetails.jsp");
			rd.forward(request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	if(action.equalsIgnoreCase("payreceived")) {
		
		
		try {
			String msg = StrUtils.fString(request.getParameter("msg"));
			request.setAttribute("Msg", msg);
			RequestDispatcher rd = request.getRequestDispatcher("/jsp/paymentsreceived.jsp");
		rd.forward(request, response);
	} catch (Exception e) {
		e.printStackTrace();
	}
}
	
	if(action.equalsIgnoreCase("cusbalances")) {
		
		
		try {
			String msg = StrUtils.fString(request.getParameter("msg"));
			request.setAttribute("Msg", msg);
			RequestDispatcher rd = request.getRequestDispatcher("/jsp/customerbalances.jsp");
		rd.forward(request, response);
	} catch (Exception e) {
		e.printStackTrace();
	}
}
	
	if(action.equalsIgnoreCase("cuscreditdetails")) {
		
		
		try {
			String msg = StrUtils.fString(request.getParameter("msg"));
			request.setAttribute("Msg", msg);
			RequestDispatcher rd = request.getRequestDispatcher("/jsp/customercreditdetails.jsp");
		rd.forward(request, response);
	} catch (Exception e) {
		e.printStackTrace();
	}
}
	if(action.equalsIgnoreCase("cusagingsummary")) {
		
		
		try {
			String msg = StrUtils.fString(request.getParameter("msg"));
			request.setAttribute("Msg", msg);
			RequestDispatcher rd = request.getRequestDispatcher("/jsp/invoiceAgingSummary.jsp");
		rd.forward(request, response);
	} catch (Exception e) {
		e.printStackTrace();
	}
}

	
		
}
	
	@Override
	public HashMap<String, String> populateMapData(String companyCode, String userCode) {
		return null;
	}

	@Override
	public void setMapDataToLogger(HashMap<String, String> dataForLogging) {
	}

}

		