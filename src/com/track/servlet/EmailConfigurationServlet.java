package com.track.servlet;

import java.io.IOException;
import java.util.Hashtable;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.track.constants.IConstants;
import com.track.constants.IDBConstants;
import com.track.dao.EmailMsgDAO;
import com.track.db.util.EmailMsgUtil;
import com.track.util.StrUtils;
import com.track.util.ThrowableUtil;

import net.sf.json.JSONObject;

@WebServlet("/email_config/*")
public class EmailConfigurationServlet extends UcloServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8136069338637043579L;
	
	//private static final Logger logger = Logger.getLogger(EmailConfigurationServlet.class);

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String[] pathInfo = request.getPathInfo().split("/");
		String action = pathInfo[1];
		HttpSession session = request.getSession(false);
		if (session.isNew() || session.getAttribute("LOGIN_USER") == null) // Invalid Session
		{
			session.invalidate();
			System.out.println("New Session Divert it to Index Page");
			response.sendRedirect(getRootURI(request) + "/login");
			return;
		}
		String jspPath = null;
		if ("bill".equalsIgnoreCase(action)) {
			jspPath = "../jsp/editBillEmailMsg.jsp";
		} else if ("invoice".equalsIgnoreCase(action)) {
			jspPath = "../jsp/editInvoiceEmailMsg.jsp";
		} else if ("expiredate".equalsIgnoreCase(action)) {
			jspPath = "../jsp/editInvexpiredateEmailMsg.jsp";
		}else if ("payslip".equalsIgnoreCase(action)) {
			jspPath = "../jsp/editPaySlipEmailMsg.jsp";
		}else if ("minmax".equalsIgnoreCase(action)) {
			jspPath = "../jsp/editMinMaxEmailMsg.jsp";
		}else if ("delivery".equalsIgnoreCase(action)) {
			jspPath = "../jsp/editDeliveryEmailconfig.jsp";
		}else if ("detail_receiving_list".equalsIgnoreCase(action)){
			EmailMsgDAO emailMsgDAO = new EmailMsgDAO();
			JSONObject resultJson = new JSONObject();
			try {
				Map<String, String> emailMsgDetails = emailMsgDAO.getEmailMsgDetails(StrUtils.fString((String) session.getAttribute("PLANT")), IConstants.PURCHASE_ORDER);
				for(String key : emailMsgDetails.keySet()) {
					resultJson.put(key, StrUtils.fString(emailMsgDetails.get(key)));
				}
				resultJson.put("ERROR_CODE", "100");
			} catch (Exception e) {
				resultJson.put("MESSAGE", ThrowableUtil.getMessage(e));
				resultJson.put("ERROR_CODE", "98");
				e.printStackTrace();
			}
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			response.getWriter().write(resultJson.toString());
			response.getWriter().flush();
			response.getWriter().close();
			return;
		}  else if ("detail_po".equalsIgnoreCase(action)){
			EmailMsgDAO emailMsgDAO = new EmailMsgDAO();
			JSONObject resultJson = new JSONObject();
			try {
				Map<String, String> emailMsgDetails = emailMsgDAO.getEmailMsgDetails(StrUtils.fString((String) session.getAttribute("PLANT")), IConstants.PURCHASE_ORDER_AR);
				for(String key : emailMsgDetails.keySet()) {
					resultJson.put(key, StrUtils.fString(emailMsgDetails.get(key)));
				}
				resultJson.put("ERROR_CODE", "100");
			} catch (Exception e) {
				resultJson.put("MESSAGE", ThrowableUtil.getMessage(e));
				resultJson.put("ERROR_CODE", "98");
				e.printStackTrace();
			}
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			response.getWriter().write(resultJson.toString());
			response.getWriter().flush();
			response.getWriter().close();
			return;
		} else if ("detail_picking_list".equalsIgnoreCase(action)){
			EmailMsgDAO emailMsgDAO = new EmailMsgDAO();
			JSONObject resultJson = new JSONObject();
			try {
				Map<String, String> emailMsgDetails = emailMsgDAO.getEmailMsgDetails(StrUtils.fString((String) session.getAttribute("PLANT")), IConstants.SALES_ORDER);
				for(String key : emailMsgDetails.keySet()) {
					resultJson.put(key, StrUtils.fString(emailMsgDetails.get(key)));
				}
				resultJson.put("ERROR_CODE", "100");
			} catch (Exception e) {
				resultJson.put("MESSAGE", ThrowableUtil.getMessage(e));
				resultJson.put("ERROR_CODE", "98");
				e.printStackTrace();
			}
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			response.getWriter().write(resultJson.toString());
			response.getWriter().flush();
			response.getWriter().close();
			return;
		}  else if ("detail_do".equalsIgnoreCase(action)){
			EmailMsgDAO emailMsgDAO = new EmailMsgDAO();
			JSONObject resultJson = new JSONObject();
			try {
				Map<String, String> emailMsgDetails = emailMsgDAO.getEmailMsgDetails(StrUtils.fString((String) session.getAttribute("PLANT")), IConstants.SALES_ORDER_API);
				for(String key : emailMsgDetails.keySet()) {
					resultJson.put(key, StrUtils.fString(emailMsgDetails.get(key)));
				}
				resultJson.put("ERROR_CODE", "100");
			} catch (Exception e) {
				resultJson.put("MESSAGE", ThrowableUtil.getMessage(e));
				resultJson.put("ERROR_CODE", "98");
				e.printStackTrace();
			}
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			response.getWriter().write(resultJson.toString());
			response.getWriter().flush();
			response.getWriter().close();
			return;
		} else if ("detail_estimate".equalsIgnoreCase(action)){
			EmailMsgDAO emailMsgDAO = new EmailMsgDAO();
			JSONObject resultJson = new JSONObject();
			try {
				Map<String, String> emailMsgDetails = emailMsgDAO.getEmailMsgDetails(StrUtils.fString((String) session.getAttribute("PLANT")), IConstants.ESTIMATE_ORDER);
				for(String key : emailMsgDetails.keySet()) {
					resultJson.put(key, StrUtils.fString(emailMsgDetails.get(key)));
				}
				resultJson.put("ERROR_CODE", "100");
			} catch (Exception e) {
				resultJson.put("MESSAGE", ThrowableUtil.getMessage(e));
				resultJson.put("ERROR_CODE", "98");
				e.printStackTrace();
			}
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			response.getWriter().write(resultJson.toString());
			response.getWriter().flush();
			response.getWriter().close();
			return;
		}  else if ("detail_estimate_ucso".equalsIgnoreCase(action)){
			EmailMsgDAO emailMsgDAO = new EmailMsgDAO();
			JSONObject resultJson = new JSONObject();
			try {
				Map<String, String> emailMsgDetails = emailMsgDAO.getEmailMsgDetails(StrUtils.fString((String) session.getAttribute("PLANT")), IConstants.ESTIMATE_ORDER_UCSO);
				for(String key : emailMsgDetails.keySet()) {
					resultJson.put(key, StrUtils.fString(emailMsgDetails.get(key)));
				}
				resultJson.put("ERROR_CODE", "100");
			} catch (Exception e) {
				resultJson.put("MESSAGE", ThrowableUtil.getMessage(e));
				resultJson.put("ERROR_CODE", "98");
				e.printStackTrace();
			}
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			response.getWriter().write(resultJson.toString());
			response.getWriter().flush();
			response.getWriter().close();
			return;
		}  else if ("detail_consignment".equalsIgnoreCase(action)){
			EmailMsgDAO emailMsgDAO = new EmailMsgDAO();
			JSONObject resultJson = new JSONObject();
			try {
				Map<String, String> emailMsgDetails = emailMsgDAO.getEmailMsgDetails(StrUtils.fString((String) session.getAttribute("PLANT")), IConstants.CONSIGNMENT_ORDER);
				for(String key : emailMsgDetails.keySet()) {
					resultJson.put(key, StrUtils.fString(emailMsgDetails.get(key)));
				}
				resultJson.put("ERROR_CODE", "100");
			} catch (Exception e) {
				resultJson.put("MESSAGE", ThrowableUtil.getMessage(e));
				resultJson.put("ERROR_CODE", "98");
				e.printStackTrace();
			}
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			response.getWriter().write(resultJson.toString());
			response.getWriter().flush();
			response.getWriter().close();
			return;
		}  else if ("detail_consignment_api".equalsIgnoreCase(action)){
			EmailMsgDAO emailMsgDAO = new EmailMsgDAO();
			JSONObject resultJson = new JSONObject();
			try {
				Map<String, String> emailMsgDetails = emailMsgDAO.getEmailMsgDetails(StrUtils.fString((String) session.getAttribute("PLANT")), IConstants.CONSIGNMENT_ORDER_API);
				for(String key : emailMsgDetails.keySet()) {
					resultJson.put(key, StrUtils.fString(emailMsgDetails.get(key)));
				}
				resultJson.put("ERROR_CODE", "100");
			} catch (Exception e) {
				resultJson.put("MESSAGE", ThrowableUtil.getMessage(e));
				resultJson.put("ERROR_CODE", "98");
				e.printStackTrace();
			}
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			response.getWriter().write(resultJson.toString());
			response.getWriter().flush();
			response.getWriter().close();
			return;
		}  else {
			throw new IOException("Invlid action : " + action);
		}
		try {
			RequestDispatcher rd = request.getRequestDispatcher(jspPath);
			rd.forward(request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String[] pathInfo = request.getPathInfo().split("/");
		String action = pathInfo[1];
		HttpSession session = request.getSession(false);
		if (session.isNew() || session.getAttribute("LOGIN_USER") == null) // Invalid Session
		{
			session.invalidate();
			System.out.println("New Session Divert it to Index Page");
			response.sendRedirect(getRootURI(request) + "/login");
			return;
		}
		String result = "Failed to edit the details";
		Hashtable<String, String> ht = new Hashtable<String, String>();
		if ("bill".equalsIgnoreCase(action)) {
			ht.put(IDBConstants.MODULE_TYPE, IConstants.BILL);
		} else if ("invoice".equalsIgnoreCase(action)) {
				ht.put(IDBConstants.MODULE_TYPE, IConstants.INVOICE);
		} else if ("expiredate".equalsIgnoreCase(action)) {
			ht.put(IDBConstants.MODULE_TYPE, IConstants.EXPIREDATE);
		}
		else if ("minmax".equalsIgnoreCase(action)) {
			ht.put(IDBConstants.MODULE_TYPE, IConstants.MINMAX);
		}
		else if ("payslip".equalsIgnoreCase(action)) {
			ht.put(IDBConstants.MODULE_TYPE, IConstants.PAYSLIP);
		}
		else if ("delivery".equalsIgnoreCase(action)) {
			ht.put(IDBConstants.MODULE_TYPE, IConstants.DELIVERY);
	}else {
			throw new IOException("Invlid action : " + action);
		}
		try {
			StringBuffer Body1 = new StringBuffer();
			StringBuffer Body2 = new StringBuffer();
			StringBuffer ccBody1 = new StringBuffer();
			StringBuffer ccBody2 = new StringBuffer();
			String emailFrom = StrUtils.fString(request.getParameter("emailFrom"));
			String subject = StrUtils.fString(request.getParameter("subject"));
			Body1.append(StrUtils.fString(request.getParameter("Body1")));
			Body2.append(StrUtils.fString(request.getParameter("Body2")));
			String CC_CHECK = StrUtils.fString(request.getParameter("CC_CHECK"));
			ccBody1.append(StrUtils.fString(request.getParameter("ccBody1")));
			ccBody2.append(StrUtils.fString(request.getParameter("ccBody2")));
			String isAutoEmail = (request.getParameter("ISAUTOEMAIL") == null ? "N"
					: (request.getParameter("ISAUTOEMAIL").equals("N") ? "N" : "Y"));
			String sendAttachment = StrUtils.fString(request.getParameter("send_attachment"));
			String expiryin = StrUtils.fString(request.getParameter("EXPIRYIN"));
			String emailTo = StrUtils.fString(request.getParameter("emailTo"));
			String time = StrUtils.fString(request.getParameter("TIME"));

			ht.put(IDBConstants.PLANT, StrUtils.fString((String) session.getAttribute("PLANT")));
			ht.put(IDBConstants.LOGIN_USER, StrUtils.fString((String) session.getAttribute("LOGIN_USER")));
			ht.put(IDBConstants.EMAIL_FROM, emailFrom);
			if(!emailTo.equalsIgnoreCase("")|| emailTo!=null) {
			 ht.put(IDBConstants.EMAIL_TO,emailTo);
			}else {
				ht.put(IDBConstants.EMAIL_TO, "");
			}
			ht.put(IDBConstants.SUBJECT, subject);
			ht.put(IDBConstants.BODY1, Body1.toString());
			// ht.put(IDBConstants.ORDER_NO,OrderNo);
			ht.put(IDBConstants.ORDER_NO, "");
			// ht.put(IDBConstants.BODY2,Body2.toString());
			ht.put(IDBConstants.BODY2, "");
			// ht.put(IDBConstants.WEB_LINK,webLink);
			ht.put(IDBConstants.WEB_LINK, "");
			if (!CC_CHECK.equalsIgnoreCase("Y"))
				CC_CHECK = "N";
			// ht.put(IDBConstants.IS_CC_CHECKED,CC_CHECK);
			ht.put(IDBConstants.IS_CC_CHECKED, "");
			// ht.put(IDBConstants.CC_SUBJECT,ccsubject);
			ht.put(IDBConstants.CC_SUBJECT, "");
			// ht.put(IDBConstants.CC_BODY1,ccBody1.toString());
			ht.put(IDBConstants.CC_BODY1, "");
			// ht.put(IDBConstants.CC_BODY2,ccBody2.toString());
			ht.put(IDBConstants.CC_BODY2, "");
			// ht.put(IDBConstants.CC_WEB_LINK,ccwebLink);
			ht.put(IDBConstants.CC_WEB_LINK, "");
			// ht.put(IDBConstants.CC_EMAILTO,ccemailTo);
			ht.put(IDBConstants.CC_EMAILTO, "");
			ht.put(IDBConstants.ISAUTOEMAIL, isAutoEmail);
			ht.put(IDBConstants.SEND_ATTACHMENT, sendAttachment);
			ht.put(IDBConstants.UPONCREATION, "0");
            ht.put(IDBConstants.CONVERTFROMEST, "0");
            ht.put(IDBConstants.UPONAPPROVE, "0");
            if(!expiryin.equalsIgnoreCase("")|| expiryin!=null) {
            	ht.put("EXPIRYIN", expiryin);
            }
            if(!time.equalsIgnoreCase("")|| time!=null) {
            	ht.put("TIME", time);
            }
			if (new EmailMsgUtil().updateEmailMessageFormat(ht)) {
				if ("bill".equalsIgnoreCase(action)) {
					result = "Email Configuration for Bill edited successfully";
			} else if ("expiredate".equalsIgnoreCase(action)) {
					result = "Email Configuration for ExpireDate edited successfully";
				} else if ("invoice".equalsIgnoreCase(action)) {
					result = "Email Configuration for Invoice edited successfully";
				}
				else if ("payslip".equalsIgnoreCase(action)) {
					result = "Email Configuration for Payslip edited successfully";
				}
				else if ("delivery".equalsIgnoreCase(action)) {
					result = "Email Configuration for Delivery edited successfully";
				}
				else if ("minmax".equalsIgnoreCase(action)) {
					result = "Email Configuration  edited successfully";
				}
			}
		} catch (Exception e) {
			result = e.getMessage() == null ? result : e.getMessage();
		}
		session.setAttribute("result", result);
		response.sendRedirect(getRootURI(request) + "/email_config" + request.getPathInfo());
	}

}
