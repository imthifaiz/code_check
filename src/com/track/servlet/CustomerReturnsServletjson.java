package com.track.servlet;

import java.io.IOException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import com.track.constants.IDBConstants;
import com.track.constants.MLoggerConstant;
import com.track.dao.InvMstDAO;
import com.track.dao.ItemMstDAO;
import com.track.dao.LocMstDAO;
import com.track.dao.ShipHisDAO;
import com.track.dao.TblControlDAO;
import com.track.db.util.ItemMstUtil;

import com.track.db.util.UserLocUtil;
import com.track.util.DateUtils;
import com.track.util.IMLogger;
import com.track.util.MLogger;
import com.track.util.StrUtils;

import java.util.ArrayList;
import java.util.List;

public class CustomerReturnsServletjson  extends HttpServlet implements IMLogger {
	
	private static final long serialVersionUID = 1L;
	private boolean printLog = MLoggerConstant.CustomerReturnServletjson_PRINTPLANTMASTERLOG;
	private boolean printInfo = MLoggerConstant.CustomerReturnServletjson_PRINTPLANTMASTERINFO;
	
	
	public CustomerReturnsServletjson () {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public HashMap<String, String> populateMapData(String companyCode,
			String userCode) {
		HashMap<String, String> loggerDetailsHasMap = new HashMap<String, String>();
		loggerDetailsHasMap.put(MLogger.COMPANY_CODE, companyCode);
		loggerDetailsHasMap.put(MLogger.USER_CODE, userCode);
		return loggerDetailsHasMap;

	}

	public void setMapDataToLogger(HashMap<String, String> dataForLogging) {
		this.mLogger.setLoggerConstans(dataForLogging);
	}

	private MLogger mLogger = new MLogger();

	public MLogger getmLogger() {
		return mLogger;
	}

	public void setmLogger(MLogger mLogger) {
		this.mLogger = mLogger;
	}

	
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String action = StrUtils.fString(request.getParameter("ACTION")).trim();
		String plant = StrUtils.fString(
				(String) request.getSession().getAttribute("PLANT")).trim();
		String userName = StrUtils.fString(
				(String) request.getSession().getAttribute("LOGIN_USER"))
				.trim();
		this.setMapDataToLogger(this.populateMapData(plant, userName));
		
		if (this.printInfo) {
			diaplayInfoLogs(request);
		}
		JSONObject jsonObjectResult = new JSONObject();
		if (action.equalsIgnoreCase("VALIDATE_BATCH")) {
			
			String item = StrUtils.fString(request.getParameter("ITEM")).trim();
			String orderno = StrUtils.fString(request.getParameter("ORDERNO")).trim();
		    String batch = StrUtils.fString(request.getParameter("BATCH")).trim();
				jsonObjectResult = this.process_validateBatch(plant, item,orderno,batch);
		}
					
	   
		this.mLogger.auditInfo(this.printInfo, "[JSON OUTPUT] "
				+ jsonObjectResult);
		response.setContentType("application/json");
		//((ServletRequest) response).setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().write(jsonObjectResult.toString());
		response.getWriter().flush();
		response.getWriter().close();
	}
	
	
	@SuppressWarnings("unchecked")
	private JSONObject process_validateBatch(String plant, String item,String orderno,String batch)  {
		JSONObject resultJson = new JSONObject();
		try {

			ShipHisDAO   shipHisDAO = new ShipHisDAO();
			shipHisDAO.setmLogger(mLogger);
			Hashtable ht = new Hashtable();
			ht.put(IDBConstants.PLANT, plant);
			ht.put(IDBConstants.ITEM , item);
			ht.put("DONO" , orderno);
			ht.put("BATCH", batch);
			
			
			if (shipHisDAO.isExisit(ht,
					" STATUS='C'")) {
				
				resultJson.put("status", "100");
			} else {
				
				resultJson.put("status", "99");
			}
			return resultJson;
		} catch (Exception daRE) {
			resultJson.put("status", "99");
			return resultJson;
		}
	}

	
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
	
	
	private void diaplayInfoLogs(HttpServletRequest request) {
		try {
			Map requestParameterMap = request.getParameterMap();
			Set<String> keyMap = requestParameterMap.keySet();
			StringBuffer requestParams = new StringBuffer();
			requestParams.append("Class Name : " + this.getClass() + "\n");
			requestParams.append("Paramter Mapping : \n");
			for (String key : keyMap) {
				requestParams.append("[" + key + " : "
						+ request.getParameter(key) + "] ");
			}
			this.mLogger.auditInfo(this.printInfo, requestParams.toString());

		} catch (Exception e) {

		}

	}



}
