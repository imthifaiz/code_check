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

@WebServlet({"/login", "/authenticate","/selectcompany","/selectapp","/home","/purchaseTransactionDashboard","/salesTransactionDashboard","/notification","/internalcrmsummary","/internalcrmedit"})
public class AuthenticateServlet extends HttpServlet implements IMLogger {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8136069338637043579L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String[] pathInfo = request.getRequestURI().split("/");
		String action = pathInfo[2];
	
		if(action.equalsIgnoreCase("login") || action.equalsIgnoreCase("authenticate")) {
			if(action.equalsIgnoreCase("login")) {
				try {
					RequestDispatcher rd = request.getRequestDispatcher("/jsp/login.jsp");
					rd.forward(request, response);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}else if(action.equalsIgnoreCase("authenticate")) {
				try {
					RequestDispatcher rd = request.getRequestDispatcher("/jsp/checkUser.jsp");
					rd.forward(request, response);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}else{
			//	Check the session validity before proceeding further
			if (request.getSession().isNew() || request.getSession().getAttribute("LOGIN_USER") == null)    //  Invalid Session
			{
				request.getSession().invalidate();
				System.out.println("New Session Divert it to Index Page");
				RequestDispatcher rd = request.getRequestDispatcher("/jsp/login.jsp");
				rd.forward(request, response);
			}else if(action.equalsIgnoreCase("selectcompany")) {
				try {
					RequestDispatcher rd = request.getRequestDispatcher("/jsp/companylist.jsp");
					rd.forward(request, response);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			else if(action.equalsIgnoreCase("selectapp")) {
				try {
					RequestDispatcher rd = request.getRequestDispatcher("/jsp/systemSelect.jsp");
					rd.forward(request, response);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}else if(action.equalsIgnoreCase("home")) {
				try {
					RequestDispatcher rd = request.getRequestDispatcher("/jsp/home.jsp");
					rd.forward(request, response);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}else if(action.equalsIgnoreCase("purchaseTransactionDashboard")) {
				try {
					RequestDispatcher rd = request.getRequestDispatcher("/jsp/purchaseTransactionDashboard.jsp");
					rd.forward(request, response);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}else if(action.equalsIgnoreCase("salesTransactionDashboard")) {
				try {
					RequestDispatcher rd = request.getRequestDispatcher("/jsp/salesTransactionDashboard.jsp");
					rd.forward(request, response);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}else if(action.equalsIgnoreCase("notification")) {
				try {
					RequestDispatcher rd = request.getRequestDispatcher("/jsp/notification.jsp");
					rd.forward(request, response);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}else if(action.equalsIgnoreCase("internalcrmsummary")) {
				try {
					String msg = StrUtils.fString(request.getParameter("msg"));
					String res = StrUtils.fString(request.getParameter("result"));
					String pgact = StrUtils.fString(request.getParameter("PGaction"));
					request.setAttribute("Msg", msg);
					request.setAttribute("result", res);
					request.setAttribute("PGaction", pgact);
					RequestDispatcher rd = request.getRequestDispatcher("/jsp/internalCrmSummary.jsp");
					rd.forward(request, response);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}else if(action.equalsIgnoreCase("internalcrmedit")) {
				try {
					String msg = StrUtils.fString(request.getParameter("msg"));
					String result = StrUtils.fString(request.getParameter("result"));
					request.setAttribute("Msg", msg);
					request.setAttribute("result", result);	
					RequestDispatcher rd = request.getRequestDispatcher("/jsp/internalCrmEdit.jsp");
					rd.forward(request, response);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request,response);
		
		String[] pathInfo = request.getRequestURI().split("/");
		String action = pathInfo[2];
		if(action.equalsIgnoreCase("notification")) {
			try {
				RequestDispatcher rd = request.getRequestDispatcher("/jsp/notification.jsp");
				rd.forward(request, response);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else if(action.equalsIgnoreCase("internalcrmsummary")) {
			try {
				String msg = StrUtils.fString(request.getParameter("msg"));
				String res = StrUtils.fString(request.getParameter("result"));
				String pgact = StrUtils.fString(request.getParameter("PGaction"));
				request.setAttribute("Msg", msg);
				request.setAttribute("result", res);
				request.setAttribute("PGaction", pgact);
				RequestDispatcher rd = request.getRequestDispatcher("/jsp/internalCrmSummary.jsp");
				rd.forward(request, response);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else if(action.equalsIgnoreCase("internalcrmedit")) {
			try {
				String msg = StrUtils.fString(request.getParameter("msg"));
				String result = StrUtils.fString(request.getParameter("result"));
				request.setAttribute("Msg", msg);
				request.setAttribute("result", result);	
				RequestDispatcher rd = request.getRequestDispatcher("/jsp/internalCrmEdit.jsp");
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
