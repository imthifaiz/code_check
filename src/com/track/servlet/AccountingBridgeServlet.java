package com.track.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.NotFoundException;

import org.apache.commons.lang.RandomStringUtils;

import com.track.constants.IConstants;
import com.track.util.StrUtils;
import com.track.util.http.HttpUtils;

/**
 * Servlet implementation class AccountingBridgeServlet
 */
@WebServlet("/AccountingBridgeServlet")
public class AccountingBridgeServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AccountingBridgeServlet() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//		Call Accounting module for delete item : Start
		String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
		String User_id = StrUtils.fString((String) request.getSession().getAttribute("LOGIN_USER")).trim();
		String accModuleResponse = "";
		try {
			accModuleResponse = HttpUtils.connect(HttpUtils.getHostInfo(request) + IConstants.ACCOUNTING_APPLICATION_URL.replace("trackaccounting", plant) + "restUser/handshake/" + request.getSession(false).getId() + "/" + User_id + "/" + plant);
			System.out.println("accModuleResponse : " + accModuleResponse);
			response.sendRedirect(HttpUtils.getHostInfo(request) + IConstants.ACCOUNTING_APPLICATION_URL.replace("trackaccounting", plant) + "user/login/fi/" + accModuleResponse);
		}catch(NotFoundException nfe) {
			response.sendRedirect(HttpUtils.getHostInfo(request) + IConstants.ACCOUNTING_APPLICATION_URL.replace("trackaccounting", "track") + "home");
		}
//		String accModuleResponse = "";
//		try {
//			accModuleResponse = HttpUtils.connect(HttpUtils.getHostInfo(request) + "trackaccounting/restUser/handshake/" + request.getSession(false).getId() + "/" + User_id + "/" + plant);
//			System.out.println("accModuleResponse : " + accModuleResponse);
//			response.sendRedirect(HttpUtils.getHostInfo(request) + "trackaccounting/user/login/fi/" + accModuleResponse);
//		}catch(NotFoundException nfe) {
//			response.sendRedirect(HttpUtils.getHostInfo(request) + IConstants.ACCOUNTING_APPLICATION_URL.replace("trackaccounting", "track") + "home");
//		}
		//	Call Accounting module for delete item : End
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.addHeader("Access-Control-Allow-Origin", "*");
		String randomString = "";
		while("".equals(randomString) || getServletContext().getAttribute(randomString) != null) {
			randomString = RandomStringUtils.randomAlphanumeric(32);
		}
		if (request.getParameter("d") == null) {
			getServletContext().setAttribute(randomString, request.getParameter("s") + "~" + request.getParameter("u"));
		}else {
			getServletContext().setAttribute(randomString, request.getParameter("s") + "~" + request.getParameter("u") + "~" + request.getParameter("d"));
		}
		response.getOutputStream().println(randomString);
	}
}
