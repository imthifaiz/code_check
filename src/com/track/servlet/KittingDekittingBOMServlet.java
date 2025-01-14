package com.track.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.track.constants.IConstants;
import com.track.dao.ProductionBomDAO;
import com.track.dao.TblControlDAO;
import com.track.db.util.POUtil;
import com.track.util.IMLogger;
import com.track.util.StrUtils;
import com.track.util.ThrowableUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * Servlet implementation class KittingDekittingBOMServlet
 */
@WebServlet("/kittingdekitting/*")
public class KittingDekittingBOMServlet extends HttpServlet implements IMLogger {
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
						String result = StrUtils.fString(request.getParameter("result"));
						if(result.equalsIgnoreCase("null"))
							result ="";
						request.setAttribute("Msg", msg);
						request.setAttribute("action", result);
						request.setAttribute("RESULT", result);
						RequestDispatcher rd = request.getRequestDispatcher("/jsp/Kitting_DekittingwithBOMSummary.jsp");
					rd.forward(request, response);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		
		if(action.equalsIgnoreCase("edit")) {
			String msg= "";
			String actionvl   = StrUtils.fString(request.getParameter("action"));
			String pitem  = StrUtils.fString(request.getParameter("ITEM"));
			String pdesc  = StrUtils.fString(request.getParameter("DESC"));
			String pbatch  = StrUtils.fString(request.getParameter("BATCH_0"));
			String KONO  = StrUtils.fString(request.getParameter("KONO"));
			
			request.setAttribute("Msg", msg);
			request.setAttribute("action", actionvl);
			request.setAttribute("ITEM", pitem);
			request.setAttribute("DESC", pdesc);
			request.setAttribute("BATCH_0", pbatch);
			request.setAttribute("KONO", KONO);
			request.setAttribute("ISEDIT", "ISEDIT");
			try {RequestDispatcher rd = request.getRequestDispatcher("/jsp/Kitting_DekittingwithBOM.jsp");
			request.setAttribute("Msg", msg);
			rd.forward(request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
		
	if(action.equalsIgnoreCase("summarydetails")) {
				
				
				try {
					String msg = StrUtils.fString(request.getParameter("msg"));
					String result = StrUtils.fString(request.getParameter("result"));
					request.setAttribute("Msg", msg);
					request.setAttribute("action", result);
					request.setAttribute("RESULT", result);
					RequestDispatcher rd = request.getRequestDispatcher("/jsp/view_kitting.jsp");
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
		
		if (action.equalsIgnoreCase("Auto-Generate")) {
			String kono = "";
			TblControlDAO _TblControlDAO = new TblControlDAO();
			JSONObject json = new JSONObject();
			try {
				kono = _TblControlDAO.getNextOrder(plant, username, IConstants.KITTING);
				json.put("KONO", kono);
				response.setStatus(200);
			} catch (Exception e) {
				e.printStackTrace();
				response.setStatus(500);
			}
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			response.getWriter().write(json.toString());
			response.getWriter().flush();
			response.getWriter().close();
		}
		
		if(action.equalsIgnoreCase("new")) {
			
			
			try {
				String msg = StrUtils.fString(request.getParameter("msg"));
				String actionvl   = StrUtils.fString(request.getParameter("action"));
				String pitem  = StrUtils.fString(request.getParameter("ITEM"));
				String pbatch  = StrUtils.fString(request.getParameter("BATCH_0"));
				request.setAttribute("Msg", msg);
				request.setAttribute("action", actionvl);
				request.setAttribute("ITEM", pitem);
				request.setAttribute("BATCH_0", pbatch);
				request.setAttribute("ISEDIT", "");
				RequestDispatcher rd = request.getRequestDispatcher("/jsp/Kitting_DekittingwithBOM.jsp");
			rd.forward(request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
		
	if(action.equalsIgnoreCase("summary")) {
			
			
			try {
				String msg = StrUtils.fString(request.getParameter("msg"));
				String result = StrUtils.fString(request.getParameter("result"));
				request.setAttribute("Msg", msg);
				request.setAttribute("action", result);
				request.setAttribute("RESULT", result);
				RequestDispatcher rd = request.getRequestDispatcher("/jsp/Kitting_DekittingwithBOMSummary.jsp");
			rd.forward(request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	

	if(action.equalsIgnoreCase("edit")) {
			String msg= "";
			String actionvl   = StrUtils.fString(request.getParameter("action"));
			String pitem  = StrUtils.fString(request.getParameter("ITEM"));
			String pdesc  = StrUtils.fString(request.getParameter("DESC"));
			String pbatch  = StrUtils.fString(request.getParameter("BATCH_0"));
			String KONO  = StrUtils.fString(request.getParameter("KONO"));
			
			request.setAttribute("Msg", msg);
			request.setAttribute("action", actionvl);
			request.setAttribute("ITEM", pitem);
			request.setAttribute("DESC", pdesc);
			request.setAttribute("BATCH_0", pbatch);
			request.setAttribute("KONO", KONO);
			request.setAttribute("ISEDIT", "ISEDIT");
			try {RequestDispatcher rd = request.getRequestDispatcher("/jsp/Kitting_DekittingwithBOM.jsp");
			request.setAttribute("Msg", msg);
			rd.forward(request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	if(action.equalsIgnoreCase("summarydetails")) {
		
		
		try {
			String msg = StrUtils.fString(request.getParameter("msg"));
			String result = StrUtils.fString(request.getParameter("result"));
			if(result.equalsIgnoreCase(""))
				result="View";
			request.setAttribute("Msg", msg);
			request.setAttribute("action", result);
			request.setAttribute("RESULT", result);
			RequestDispatcher rd = request.getRequestDispatcher("/jsp/view_kitting.jsp");
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
