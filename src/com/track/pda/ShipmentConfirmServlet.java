package com.track.pda;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.track.constants.IConstants;
import com.track.constants.MLoggerConstant;
import com.track.db.util.DOUtil;
import com.track.util.DateUtils;
import com.track.util.IMLogger;
import com.track.util.MLogger;
import com.track.util.StrUtils;
import com.track.util.XMLUtils;

public class ShipmentConfirmServlet extends HttpServlet implements IMLogger {
	private boolean printLog = MLoggerConstant.ShipmentConfirmServlet_PRINTPLANTMASTERLOG;
	private boolean printInfo = MLoggerConstant.ShipmentConfirmServlet_PRINTPLANTMASTERINFO;
	private static final long serialVersionUID = 5257931803128127447L;

	private DOUtil _DOUtil = null;

	private String PLANT = "";
	private String action = "";

	private static final String CONTENT_TYPE = "text/xml";

	public void init() throws ServletException {
		_DOUtil = new DOUtil();
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType(CONTENT_TYPE);
		String xmlStr = "";
		PrintWriter out = response.getWriter();
		try {
			action = request.getParameter("action").trim();
			this.setMapDataToLogger(this.populateMapData(StrUtils.fString(request
					.getParameter("PLANT")), StrUtils.fString(request
							.getParameter("LOGIN_USER")) + " PDA_USER"));
			_DOUtil.setmLogger(mLogger);
			if (this.printInfo) {
				diaplayInfoLogs(request);
			}
			if (action.equalsIgnoreCase("load_outBoundshipHisOrders")) {
				xmlStr = "";
				xmlStr = getOpenShipHisOrder(request, response);
			}
			if (action.equalsIgnoreCase("load_outBoundIssuesForPrint")) {
				xmlStr = "";
				xmlStr = getClosedShipHisOrderForPrint(request, response);
			}
			
			if (action.equalsIgnoreCase("load_ServerTimeForPrint")) {
				xmlStr = "";
				xmlStr = getServerTimeForPrint(request, response);
			}

			else if (action.equalsIgnoreCase("load_shipHis_item_details")) {
				xmlStr = "";
				xmlStr = getShipHisItemDetails(request, response);
			}
			
			else if (action.equalsIgnoreCase("load_shipHis_item_details_for_print")) {
				xmlStr = "";
				xmlStr = getShipHisItemDetailsForPrint(request, response);
			}
			
			else if (action.equalsIgnoreCase("load_customer_details_for_print")) {
				xmlStr = "";
				xmlStr = getCustomerDetailsForPrint(request, response);
			}

			else if (action.equalsIgnoreCase("load_outBoundOrder_sup_details")) {
				xmlStr = "";
				xmlStr = load_outBoundOrder_sup_details(request, response);
			}
			
			else if (action.equalsIgnoreCase("load_outBoundOrder_sup_detailsForPrint")) {
				xmlStr = "";
				xmlStr = load_outBoundOrder_sup_detailsForPrint(request, response);
			}

			else if (action.equalsIgnoreCase("process_ship_confirm")) {

				xmlStr = process_Ship_Confirm(request, response);
			}
			
			else if (action.equalsIgnoreCase("load_CompanyDetailsForPrint")) {

				xmlStr = load_company_detailsForPrint(request, response);
			}
			else if (action.equalsIgnoreCase("load_shipHis_item_details_pda")) {
				xmlStr = "";
				xmlStr = getShipHisItemDetails_pda(request, response);
			}

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			xmlStr = XMLUtils.getXMLMessage(1, "Unable to process! "
					+ e.getMessage());
		}

		out.write(xmlStr);
		out.close();
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

	public void destroy() {
	}

	private String process_Ship_Confirm(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException,
			Exception {
		Map shipConf_HM = null;
		String PLANT = "", DO_NUM = "";
		String LOGIN_USER = "";
		String CUST_NAME = "", REMARKS = "";
		String xmlStr = "";
		String productNumbers = "";
		String pickedQty = "";
		String orderLineNumbers = "";
		try {
			PLANT = StrUtils.fString(request.getParameter("PLANT"));
			DO_NUM = StrUtils.fString(request.getParameter("DO_NUM"));
			LOGIN_USER = StrUtils.fString(request.getParameter("LOGIN_USER"));
			CUST_NAME = StrUtils.fString(request.getParameter("CUSTNAME"));
			REMARKS = StrUtils.fString(request.getParameter("REMARKS"));
			productNumbers = StrUtils.fString(request
					.getParameter("PRODUCT_NUMS"));
			orderLineNumbers = StrUtils.fString(request
					.getParameter("DO_LINE_NUMS"));
			pickedQty = StrUtils.fString(request.getParameter("PICKED_QTYS"));
			
			
			CUST_NAME=StrUtils.replaceCharacters2Recv(CUST_NAME);
			REMARKS=StrUtils.replaceCharacters2Recv(REMARKS);

			List<String> productNumberList = new ArrayList<String>();
			List<String> orderLineNumberList = new ArrayList<String>();
			List<String> pickedQtyList = new ArrayList<String>();

			REMARKS = StrUtils.replaceCharacters2Recv(REMARKS);
			CUST_NAME = StrUtils.replaceCharacters2Recv(CUST_NAME);
			
			productNumberList = tokenizeString(productNumbers);
			orderLineNumberList = tokenizeString(orderLineNumbers);
			pickedQtyList = tokenizeString(pickedQty);
			shipConf_HM = new HashMap();
			shipConf_HM.put(IConstants.PLANT, PLANT);
			shipConf_HM.put(IConstants.DODET_DONUM, DO_NUM);
			shipConf_HM.put(IConstants.CUSTOMER_NAME, CUST_NAME);
			shipConf_HM.put(IConstants.LOGIN_USER, LOGIN_USER);
			shipConf_HM.put(IConstants.CUSTOMER_CODE, _DOUtil.getCustCode(
					PLANT, DO_NUM));
			shipConf_HM.put(IConstants.REMARKS, REMARKS);
			shipConf_HM.put("PRODUCT_LIST", productNumberList);
			shipConf_HM.put("ORDER_NO_LIST", orderLineNumberList);
			shipConf_HM.put("QTY_LIST", pickedQtyList);

			xmlStr = "";

			boolean flag = true;

			if (flag) {

				xmlStr = _DOUtil.process_ship_confirmation(shipConf_HM);
			} else {
				throw new Exception(" Ship Confirmation failed ");
			}

		} catch (Exception e) {

			this.mLogger.exception(this.printLog, "", e);
			throw e;
		}
		return xmlStr;
	}

	private List<String> tokenizeString(String delimiterString) {
		List<String> resultList = new ArrayList<String>();
		try {

			resultList = new ArrayList<String>(Arrays.asList(delimiterString
					.split("AA12AA12AB12BB12BB12CC12CC")));
		} catch (Exception e) {

		}
		return resultList;
	}

	// Samatha

	private String getOpenShipHisOrder(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {
		String str = "", aPlant = "", aOrdNo = "";
		try {
			PLANT = StrUtils.fString(request.getParameter("PLANT"));

			Hashtable ht = new Hashtable();
			ht.put("PLANT", PLANT);
			_DOUtil.setmLogger(mLogger);
		//	str = _DOUtil.getOpenShipHisOrder(PLANT);
			str = _DOUtil.getOpenDOHDROrder(PLANT);
			

			if (str.equalsIgnoreCase("")) {
				str = XMLUtils.getXMLMessage(1,
						"No Outbound Orders Found to Confirm");
			}

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			str = XMLUtils.getXMLMessage(1, e.getMessage());
		}

		return str;
	}
	
	private String getClosedShipHisOrderForPrint(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {
		String str = "", aPlant = "", aOrdNo = "";
		try {
			PLANT = StrUtils.fString(request.getParameter("PLANT"));

			Hashtable ht = new Hashtable();
			ht.put("PLANT", PLANT);
			_DOUtil.setmLogger(mLogger);
			str = _DOUtil.getClosedShipHisOrderForPrint(PLANT);

			if (str.equalsIgnoreCase("")) {
				str = XMLUtils.getXMLMessage(1,
						"No Outbound Orders Found to Confirm");
			}

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			str = XMLUtils.getXMLMessage(1, e.getMessage());
		}

		return str;
	}
	
	private String getServerTimeForPrint(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {
		String str = "", aPlant = "", aOrdNo = "";
		try {

			str = _DOUtil.getServerTimeForPrint(aPlant);

			if (str.equalsIgnoreCase("")) {
				str = XMLUtils.getXMLMessage(1, "");
			}

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			str = XMLUtils.getXMLMessage(1, e.getMessage());
		}

		return str;
	}

	private String getShipHisItemDetails(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {
		String str = "", aPlant = "", aOrdNo = "";
		try {

			PLANT = StrUtils.fString(request.getParameter("PLANT"));
			aOrdNo = StrUtils.fString(request.getParameter("ORDER_NUM"));

			Hashtable ht = new Hashtable();
			ht.put("PLANT", PLANT);
			ht.put("DONO", aOrdNo);
			_DOUtil.setmLogger(mLogger);
			str = _DOUtil.getShipHisItemDetails(PLANT, aOrdNo);

			if (str.equalsIgnoreCase("")) {
				str = XMLUtils.getXMLMessage(1, "Product details not found!");
			}

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			str = XMLUtils.getXMLMessage(1, e.getMessage());
		}

		return str;
	}
	
	private String getShipHisItemDetailsForPrint(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {
		String str = "", aPlant = "", aOrdNo = "";
		try {

			PLANT = StrUtils.fString(request.getParameter("PLANT"));
			aOrdNo = StrUtils.fString(request.getParameter("ORDER_NUM"));

			Hashtable ht = new Hashtable();
			ht.put("PLANT", PLANT);
			ht.put("DONO", aOrdNo);
			_DOUtil.setmLogger(mLogger);
			str = _DOUtil.getShipHisItemDetailsForPrint(PLANT, aOrdNo);

			if (str.equalsIgnoreCase("")) {
				str = XMLUtils.getXMLMessage(1, "Product details not found!");
			}

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			str = XMLUtils.getXMLMessage(1, e.getMessage());
		}

		return str;
	}
	
	private String getCustomerDetailsForPrint(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {
		String str = "", aPlant = "", aOrdNo = "";
		try {

			PLANT = StrUtils.fString(request.getParameter("PLANT"));
			aOrdNo = StrUtils.fString(request.getParameter("ORDER_NUM"));

			Hashtable ht = new Hashtable();
			ht.put("PLANT", PLANT);
			ht.put("DONO", aOrdNo);
			_DOUtil.setmLogger(mLogger);
			str = _DOUtil.getCustomerDetailsForPrint(PLANT, aOrdNo);

			if (str.equalsIgnoreCase("")) {
				str = XMLUtils.getXMLMessage(1, "Product details not found!");
			}

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			str = XMLUtils.getXMLMessage(1, e.getMessage());
		}

		return str;
	}

	private String load_outBoundOrder_sup_details(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {
		String str = "", aPlant = "", aOrdNo = "";
		try {

			PLANT = StrUtils.fString(request.getParameter("PLANT"));
			aOrdNo = StrUtils.fString(request.getParameter("DO_NUM"));

			str = _DOUtil.getOpenoutBoundOrderSupDetails(PLANT, aOrdNo);
			if (str.equalsIgnoreCase("")) {
				str = XMLUtils.getXMLMessage(1, "Product details not found!");
			}

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			str = XMLUtils.getXMLMessage(1, e.getMessage());
		}
		return str;
	}
	
	private String load_outBoundOrder_sup_detailsForPrint(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {
		String str = "", aPlant = "", aOrdNo = "";
		try {

			PLANT = StrUtils.fString(request.getParameter("PLANT"));
			aOrdNo = StrUtils.fString(request.getParameter("DO_NUM"));

			str = _DOUtil.getOpenoutBoundOrderSupDetailsForPrint(PLANT, aOrdNo);
			if (str.equalsIgnoreCase("")) {
				str = XMLUtils.getXMLMessage(1, "Product details not found!");
			}

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			str = XMLUtils.getXMLMessage(1, e.getMessage());
		}
		return str;
	}
	
	private String load_company_detailsForPrint(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {
		String str = "", aPlant = "", aOrdNo = "";
		try {

			PLANT = StrUtils.fString(request.getParameter("PLANT"));

			str = _DOUtil.getCompanyDetailsForPrint(PLANT);
			if (str.equalsIgnoreCase("")) {
				str = XMLUtils.getXMLMessage(1, "Product details not found!");
			}

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			str = XMLUtils.getXMLMessage(1, e.getMessage());
		}
		return str;
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
	
	private String getShipHisItemDetails_pda(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {
		String str = "", aPlant = "", aOrdNo = "";
		try {

			PLANT = StrUtils.fString(request.getParameter("PLANT"));
			aOrdNo = StrUtils.fString(request.getParameter("ORDER_NUM"));

			Hashtable ht = new Hashtable();
			ht.put("PLANT", PLANT);
			ht.put("DONO", aOrdNo);
			_DOUtil.setmLogger(mLogger);
			str = _DOUtil.getShipHisItemDetailsPDA(PLANT, aOrdNo);

			if (str.equalsIgnoreCase("")) {
				str = XMLUtils.getXMLMessage(1, "Product details not found!");
			}

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			str = XMLUtils.getXMLMessage(1, e.getMessage());
		}

		return str;
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
