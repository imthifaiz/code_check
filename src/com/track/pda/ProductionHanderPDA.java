package com.track.pda;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.track.constants.IConstants;
import com.track.constants.MLoggerConstant;
import com.track.db.util.PalletiationUtil;
import com.track.util.IMLogger;
import com.track.util.MLogger;
import com.track.util.StrUtils;
import com.track.util.XMLUtils;


public class ProductionHanderPDA extends HttpServlet implements IMLogger {
	//private static final long serialVersionUID = 1L;
	
	private boolean printLog = MLoggerConstant.ProductionHanderPDA_KITTINGLOG;
	private boolean printInfo = MLoggerConstant.ProductionHanderPDA_KITTINGINFO;
	
	private String PLANT = "";
	private String xmlStr = "";
	
	
	private static final String CONTENT_TYPE = "text/xml";

	StrUtils strUtils = new StrUtils();


	/**
	 * @see HttpServlet#HttpServlet()
	 */
	/*public ProductionHanderPDA() {
		super();
		// TODO Auto-generated constructor stub
	}*?

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	
	public void init() throws ServletException {
		
	}
	
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		response.setContentType(CONTENT_TYPE);
		PrintWriter out = response.getWriter();
		
		String action = StrUtils.fString(request.getParameter("ACTION")).trim();
		String xmlOutput = "";
		
		
		this.setMapDataToLogger(this.populateMapData(StrUtils.fString(request
				.getParameter("PLANT")), StrUtils.fString(request
				.getParameter("LOGIN_USER"))
				+ " PDA_USER"));
		
		try {
			if (action.equals("DO_PALLETIZATION")) {
				
				xmlOutput = this.doPalletization(request);
			}
		} catch (Exception e) {
			this.mLogger.exception(true, "", e);
			xmlOutput = XMLUtils.getXMLMessage(1, e.getMessage());
		}

		out.write(xmlOutput);
		out.close();
	}

	private String doPalletization(HttpServletRequest request) {
		String xmlOutput = "";
		try {
			
			Boolean result = doPalletizationTaransaction(request);
			if (result) {
				xmlOutput = XMLUtils.getXMLMessage(0, "Sucess!");
			} else {
				xmlOutput = XMLUtils.getXMLMessage(1, "Unable to process!");
			}
		} catch (Exception e) {

		}
		return xmlOutput;
	}

	private Boolean doPalletizationTaransaction(HttpServletRequest request)
			throws Exception {
		try {
			Hashtable<String, String> requestData = new Hashtable<String, String>();
			requestData.clear();
			
			requestData.put(IConstants.PLANT, StrUtils.fString(
					request.getParameter("PLANT")).trim());
			requestData.put(IConstants.REMARKS, StrUtils.fString(
					request.getParameter("REMARKS")).trim());
			requestData.put("PARENT_PRODUCT", StrUtils.fString(request.getParameter("PARENT_PRODUCT")).trim());
								
			requestData.put("PARENT_PRODUCT_LOC", StrUtils.fString(request.getParameter("PARENT_PRODUCT_LOC")).trim());
			
			requestData.put("PARENT_PRODUCT_BATCH", StrUtils.fString(request.getParameter("PARENT_PRODUCT_BATCH")).trim());
		
			requestData.put(IConstants.LOGIN_USER, StrUtils.fString(
					request.getParameter("LOGIN_USER")).trim());
			requestData.put("ITEM_COUNTS", StrUtils.fString(
					request.getParameter("PRODUCT_LIST_SIZE")).trim());
			requestData.put("INV_QTY", "1");//StrUtils.fString( request.getParameter("PARENT_PRODUCT_QTY")).trim()
			Integer itemCount = new Integer(StrUtils.fString(request.getParameter("PRODUCT_LIST_SIZE")).trim());
			
			
			for (int i = 0; i < itemCount; i++) {
				requestData.put("PRODUCT_NO_" + i, StrUtils.fString(request.getParameter("PRODUCT_NO_" + i)).trim());
				
				requestData.put("CHILD_PRODUCT_LOC_" + i, StrUtils.fString(request.getParameter("CHILD_PRODUCT_LOC_" + i)).trim());
				
				requestData.put("CHILD_PRODUCT_BATCH_" + i, StrUtils.fString(request.getParameter("CHILD_PRODUCT_BATCH_" + i)).trim());
				
				double childqty = Double.parseDouble(request.getParameter("QTY_" + i));
				childqty = StrUtils.RoundDB(childqty, IConstants.DECIMALPTS);
				String kitqty = StrUtils.removeFormat(String.valueOf(childqty));
				
				requestData.put("QTY_" + i, kitqty );
			
			}
			

			PalletiationUtil palletiationUtil = new PalletiationUtil();
			return palletiationUtil.doPalletization(requestData);

		} catch (Exception e) {
			throw e;
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
			this.mLogger.auditInfo(true, requestParams.toString());

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
