package com.track.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import com.track.constants.IConstants;
import com.track.constants.MLoggerConstant;
import com.track.dao.InvMstDAO;
import com.track.dao.LoanDetDAO;
import com.track.dao.LoanHdrDAO;
import com.track.util.IMLogger;
import com.track.util.MLogger;
import com.track.util.StrUtils;

/**
 * Servlet implementation class LoanOrderHandlerServlet
 */
public class LoanOrderHandlerServlet extends HttpServlet implements IMLogger {
	private boolean printLog = MLoggerConstant.LoanOrderHandlerServlet_PRINTPLANTMASTERLOG;
	private boolean printInfo = MLoggerConstant.LoanOrderHandlerServlet_PRINTPLANTMASTERINFO;
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public LoanOrderHandlerServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
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
		if (action.equals("LOAD_LOAN_ORDER_DETAILS")) {
			String loanOrderNo = StrUtils.fString(
					request.getParameter("ORDER_NO")).trim();
			jsonObjectResult = this.loanOrderValidation(plant, loanOrderNo);
		}
		if (action.equals("VALIDATE_BATCH_DETAILS")) {
			jsonObjectResult = this.validateBatch(request);
		}
		
	if (action.equals("VALIDATE_BATCH_DETAILS_LOAN_RECEIVE")) {
			jsonObjectResult = this.validateBatchLoanReceive(request);
		}
		if (action.equals("VALIDATE_PICKING_BATCH_DETAILS")) {
			jsonObjectResult = this.validatePickingBatch(request);
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
	private JSONObject validateBatch(HttpServletRequest request) {
		JSONObject resultJson = new JSONObject();
		try {
			LoanDetDAO loanMstDAO = new LoanDetDAO();
			loanMstDAO.setmLogger(mLogger);

			String plant = request.getParameter("PLANT");
			String ordNo = request.getParameter("ORDER_NO");
			String ordLineNo = request.getParameter("ORDER_LNNO");
			String itemNo = request.getParameter("ITEMNO");
			String location = request.getParameter("LOC");
			String batch = request.getParameter("BATCH");

			JSONObject resultJsonObject = new JSONObject();
			List resultList = loanMstDAO.getLoanOrderBatchListToRecv(plant,
					ordNo, ordLineNo, itemNo, location, batch);
			
			

			if (resultList.size() > 0) {
				Map m = (Map) resultList.get(0);
				
			
				String sBatch = (String) m.get("batch");
				String spickQty = (String) m.get("pickQty");
				String srecvQty = (String) m.get("recQty");
								
				//long availQty = Integer.parseInt(spickQty)- Integer.parseInt(srecvQty);
				double availQty=Double.parseDouble(spickQty)-Double.parseDouble(srecvQty);
					
			     String QTY=StrUtils.formatNum(Double.toString(availQty));
				if (availQty > 0) {
					
					resultJsonObject.put("BATCH", sBatch);
					resultJsonObject.put("PICK_QTY", spickQty);
					resultJsonObject.put("RECEIVED_QTY", srecvQty);
					
					
					resultJsonObject.put("QTY", QTY);
					resultJson.put("result", resultJsonObject);
					resultJson.put("status", "100");
				} else {
					
					resultJson.put("status", "99");
				}
			} else {
				resultJson.put("status", "99");
			}
			return resultJson;
		} catch (Exception daRE) {
			resultJson.put("status", "99");
			return resultJson;
		}
	}
	@SuppressWarnings("unchecked")
	private JSONObject validateBatchLoanReceive(HttpServletRequest request) {
		JSONObject resultJson = new JSONObject();
		try {
			LoanDetDAO loanMstDAO = new LoanDetDAO();
			loanMstDAO.setmLogger(mLogger);

			String plant = StrUtils.fString(request.getParameter("PLANT"));
			String ordNo = StrUtils.fString(request.getParameter("ORDER_NO"));
			String ordLineNo = StrUtils.fString(request.getParameter("ORDER_LNNO"));
			String itemNo = StrUtils.fString(request.getParameter("ITEMNO"));
			String location = StrUtils.fString(request.getParameter("LOC"));
			String batch = StrUtils.fString(request.getParameter("BATCH"));

			JSONObject resultJsonObject = new JSONObject();
			List resultList = loanMstDAO.getNewLoanOrderBatchListToRecv(plant,
					ordNo, ordLineNo, itemNo, location, batch);
			
			

			if (resultList.size() > 0) {
				Map m = (Map) resultList.get(0);
				
			
				String sBatch = (String) m.get("batch");
				String spickQty = (String) m.get("pickQty");
				String srecvQty = (String) m.get("recQty");
								
				//long availQty = Integer.parseInt(spickQty)- Integer.parseInt(srecvQty);
				double availQty=Double.parseDouble(spickQty)-Double.parseDouble(srecvQty);
					
			     String QTY=StrUtils.formatNum(Double.toString(availQty));
				if (availQty > 0) {
					
					resultJsonObject.put("BATCH", sBatch);
					resultJsonObject.put("PICK_QTY", spickQty);
					resultJsonObject.put("RECEIVED_QTY", srecvQty);
					
					
					resultJsonObject.put("QTY", QTY);
					resultJson.put("result", resultJsonObject);
					resultJson.put("status", "100");
				} else {
					
					resultJson.put("status", "99");
				}
			} else {
				resultJson.put("status", "99");
			}
			return resultJson;
		} catch (Exception daRE) {
			resultJson.put("status", "99");
			return resultJson;
		}
	}
	
	@SuppressWarnings("unchecked")
	private JSONObject validatePickingBatch(HttpServletRequest request) {
		JSONObject resultJson = new JSONObject();
		try {
			String plant = StrUtils.fString(request.getParameter("PLANT")).trim();
			String location = StrUtils.fString(request.getParameter("LOC")).trim();			
			String item = StrUtils.fString(request.getParameter("ITEMNO")).trim();
			String batch = StrUtils.fString(request.getParameter("BATCH")).trim();
			String uom = StrUtils.fString(request.getParameter("UOM")).trim();
			InvMstDAO invMstDAO = new InvMstDAO();
			invMstDAO.setmLogger(mLogger);
			//ArrayList resultList = invMstDAO.getOutBoundPickingBatchByWMS(plant, item, location, batch);
			ArrayList resultList = invMstDAO.getTotalQuantityForOutBoundPickingBatchByWMSMuliUOM(plant,item, location, batch,uom);
			JSONObject resultJsonObject = new JSONObject();
			
			
			 String QTY="";
			if (resultList.size() > 0) {
				Map resultMap = (Map) resultList.get(0);
				//StrUtils.formatNum(Double.toString(availQty));
				
				if (resultMap.size() > 0) {
					QTY=(String)resultMap.get("qty");
					QTY=StrUtils.formatNum(QTY);
					resultJsonObject.put("BATCH", resultMap.get("batch"));
					resultJsonObject.put("QTY", QTY);
					resultJson.put("result", resultJsonObject);
					resultJson.put("status", "100");
				} else {
					resultJson.put("status", "99");
				}
			} else {
				resultJson.put("status", "99");
			}
			return resultJson;
		} catch (Exception daRE) {
			resultJson.put("status", "99");
			return resultJson;
		}
	}
	

	@SuppressWarnings("unchecked")
	private JSONObject loanOrderValidation(String plant, String loanOrderNo) {
		JSONObject resultJson = new JSONObject();
		try {
			JSONObject resultJsonObject = new JSONObject();
			LoanHdrDAO loanHdrDAO = new LoanHdrDAO();
			loanHdrDAO.setmLogger(mLogger);
			Hashtable ht = new Hashtable();
			ht.put(IConstants.PLANT, plant);
			ht.put(IConstants.LOANHDR_ORDNO, loanOrderNo);
			Map resultMap = loanHdrDAO.selectRow("custName,jobNum", ht);
			if (resultMap.size() > 0) {

				resultJsonObject.put("CUSTNAME", resultMap.get("custName"));
				resultJsonObject.put("JOBNUM", resultMap.get("jobNum"));
				resultJson.put("result", resultJsonObject);
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

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

	@SuppressWarnings("unchecked")
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

}
