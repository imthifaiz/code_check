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
import com.track.util.IMLogger;

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
import com.track.dao.DoDetDAO;
import com.track.dao.InvoiceDAO;
import com.track.dao.MovHisDAO;
import com.track.dao.PlantMstDAO;
import com.track.dao.TblControlDAO;
import com.track.db.util.DOUtil;
import com.track.db.util.InvoiceUtil;
import com.track.db.util.MasterUtil;
import com.track.db.util.PrdClassUtil;
import com.track.db.util.TblControlUtil;
import com.track.gates.DbBean;
import com.track.gates.selectBean;
import com.track.gates.userBean;
import com.track.util.DateUtils;
import com.track.util.FileHandling;
import com.track.util.IMLogger;
import com.track.util.StrUtils;
import com.track.util.ThrowableUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@WebServlet("/invoice/*")
public class SalesInvoiceServlet extends HttpServlet implements IMLogger {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String[] pathInfo = request.getPathInfo().split("/");
		String action = pathInfo[1];
		String username = StrUtils.fString((String) request.getSession().getAttribute("LOGIN_USER")).trim();
		String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
		
		if (action.equalsIgnoreCase("addRemarks")) {
			JSONObject resultJson = new JSONObject();
			UserTransaction ut = null;
			try {
				ut = DbBean.getUserTranaction();
				ut.begin();
				String[] remarks = request.getParameterValues("remarks");
				String item = request.getParameter("r_item");
				String invoice = request.getParameter("r_invoice");
				String invlno = request.getParameter("r_lnno");
				String dono = request.getParameter("r_dnno");
				boolean insertFlag = false;

				Hashtable<String, String> htRemarksDel = new Hashtable<>();
				htRemarksDel.put(IDBConstants.PLANT, plant);
				htRemarksDel.put(IDBConstants.INVOICE, invoice);
				htRemarksDel.put(IDBConstants.LNNO, invlno);
				htRemarksDel.put(IDBConstants.ITEM, item);
				if (new InvoiceDAO().isExisitInvoiceMultiRemarks(htRemarksDel)) {
					new InvoiceDAO().deleteInvoiceMultiRemarks(htRemarksDel);
				}

				for (int i = 0; i < remarks.length; i++) {
					Hashtable<String, String> htRemarks = new Hashtable<>();
					htRemarks.put(IDBConstants.PLANT, plant);
					htRemarks.put(IDBConstants.INVOICE, invoice);
					htRemarks.put(IDBConstants.LNNO, invlno);
					htRemarks.put(IDBConstants.ITEM, item);
					htRemarks.put(IDBConstants.REMARKS, StrUtils.InsertQuotes(remarks[i]));
					htRemarks.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
					htRemarks.put(IDBConstants.CREATED_BY, username);
					insertFlag = new InvoiceUtil().saveInvoiceMultiRemarks(htRemarks);
				}

				if (insertFlag) {
					ut.commit();
					resultJson.put("MESSAGE", "Invoice Order Created Successfully.");
					resultJson.put("ERROR_CODE", "100");
				}
			} catch (Exception e) {
				DbBean.RollbackTran(ut);
				e.printStackTrace();
				resultJson.put("MESSAGE", ThrowableUtil.getMessage(e));
				resultJson.put("ERROR_CODE", "98");
			}
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			response.getWriter().write(resultJson.toString());
			response.getWriter().flush();
			response.getWriter().close();
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
		
	if(action.equalsIgnoreCase("summary")) {
		
		try {
			String msg = StrUtils.fString(request.getParameter("msg"));
			request.setAttribute("Msg", msg);
			RequestDispatcher rd = request.getRequestDispatcher("/jsp/invoiceSummary.jsp");
			rd.forward(request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	if(action.equalsIgnoreCase("new")) {
		
		try {
			String msg = StrUtils.fString(request.getParameter("msg"));
			request.setAttribute("Msg", msg);
			RequestDispatcher rd = request.getRequestDispatcher("/jsp/createInvoice.jsp");
			rd.forward(request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	if(action.equalsIgnoreCase("detail")) {
		
		try {
			String msg = StrUtils.fString(request.getParameter("msg"));
			request.setAttribute("Msg", msg);
			RequestDispatcher rd = request.getRequestDispatcher("/jsp/invoiceDetail.jsp");
			rd.forward(request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	if(action.equalsIgnoreCase("import")) {
		
		try {
			String msg = StrUtils.fString(request.getParameter("msg"));
			request.setAttribute("Msg", msg);
			RequestDispatcher rd = request.getRequestDispatcher("/jsp/importInvoice.jsp");
			rd.forward(request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	if(action.equalsIgnoreCase("recordpayment")) {
		
		try {
			String msg = StrUtils.fString(request.getParameter("msg"));
			request.setAttribute("Msg", msg);
			RequestDispatcher rd = request.getRequestDispatcher("/jsp/invoicePayment.jsp");
			rd.forward(request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	if(action.equalsIgnoreCase("edit")) {
		
		try {
			String msg = StrUtils.fString(request.getParameter("msg"));
			request.setAttribute("Msg", msg);
			RequestDispatcher rd = request.getRequestDispatcher("/jsp/IssueingGoodsInvoice.jsp");
			rd.forward(request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	if(action.equalsIgnoreCase("copy")) {
		
		try {
			String msg = StrUtils.fString(request.getParameter("msg"));
			request.setAttribute("Msg", msg);
			RequestDispatcher rd = request.getRequestDispatcher("/jsp/IssueingGoodsInvoice.jsp");
			rd.forward(request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	} 
	
	if (action.equalsIgnoreCase("getSalesOrderRemarks")) {
		String invoice = "", invlno = "",dono="";//, item = "";
		JSONArray jsonArray = new JSONArray();
		JSONObject resultJson = new JSONObject();
		List al = new ArrayList();
		try {
			invoice = StrUtils.fString(request.getParameter("INVOICE"));
			invlno = StrUtils.fString(request.getParameter("INLNO"));
			dono = StrUtils.fString(request.getParameter("DONO"));
//			item = StrUtils.fString(request.getParameter("ITEM"));

			Hashtable<String, String> ht = new Hashtable<>();
//			Hashtable<String, String> htm = new Hashtable<>();
			ht.put(IDBConstants.PLANT, plant);
			ht.put(IDBConstants.INVOICE, invoice);
			ht.put(IDBConstants.LNNO, invlno);
			
//			if(!dono.equals("")) {
//				htm.put(IDBConstants.PLANT, plant);
//				htm.put(IDBConstants.DONO, dono);
//				htm.put("DOLNNO", invlno);
//			}

			
//			if(!dono.equals("")) {
//			 al = new DoDetDAO().selectRemarks("ISNULL(REMARKS,'') REMARKS ", htm);
//			}else {
			 al = new InvoiceDAO().selectRemarks("ISNULL(REMARKS,'') REMARKS ", ht);
//			}
			if (al.size() > 0) {
				for (int i = 0; i < al.size(); i++) {
					Map m = (Map) al.get(i);
					JSONObject resultJsonInt = new JSONObject();
					resultJsonInt.put("remarks", (String) m.get("REMARKS"));
					jsonArray.add(resultJsonInt);
				}
			} else {
				jsonArray.add("");
			}
			resultJson.put("REMARKS", jsonArray);
		} catch (Exception e) {
			e.printStackTrace();
			response.setStatus(500);
		}
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write(resultJson.toString());
		response.getWriter().flush();
		response.getWriter().close();
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

		