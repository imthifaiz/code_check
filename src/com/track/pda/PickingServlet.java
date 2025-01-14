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
import com.track.dao.ItemMstDAO;
import com.track.db.util.POUtil;
import com.track.util.IMLogger;
import com.track.util.MLogger;
import com.track.util.StrUtils;
import com.track.util.XMLUtils;

public class PickingServlet extends HttpServlet implements IMLogger {
	private boolean printLog = MLoggerConstant.PickingServlet_PRINTPLANTMASTERLOG;
	private boolean printInfo = MLoggerConstant.PickingServlet_PRINTPLANTMASTERINFO;
	private static final long serialVersionUID = 3783850566116314477L;
	private POUtil _POUtil = null;
	private String PLANT = "";

	private String xmlStr = "";
	private String action = "";

	private static final String CONTENT_TYPE = "text/xml";

	public void init() throws ServletException {
		_POUtil = new POUtil();
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType(CONTENT_TYPE);
		PrintWriter out = response.getWriter();
		this.setMapDataToLogger(this.populateMapData(StrUtils.fString(request
				.getParameter("PLANT")), StrUtils.fString(request
						.getParameter("LOGIN_USER")) + " PDA_USER"));
		if (this.printInfo) {
			diaplayInfoLogs(request);
		}
		try {
			action = request.getParameter("action").trim();

			if (action.equalsIgnoreCase("load_item_details")) {
				xmlStr = "";
				xmlStr = load_item_details(request, response);
			}

			else if (action.equalsIgnoreCase("load_po_details")) {
				xmlStr = "";
				xmlStr = load_po_Details(request, response);
			}

			else if (action.equalsIgnoreCase("process_receiveMaterial")) {
				String xmlStr = "";
				xmlStr = process_orderReceiving(request, response);
			}

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			xmlStr = XMLUtils.getXMLMessage(1, "Unable to process? "
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

	private String load_po_Details(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {
		String str = "", aPlant = "", aItemNum = "";
		try {

			PLANT = StrUtils.fString(request.getParameter("PLANT"));
			aItemNum = StrUtils.fString(request.getParameter("ITEM_NUM"));

			Hashtable ht = new Hashtable();
			ht.put("plant", PLANT);
			ht.put("item", aItemNum);

			str = _POUtil.getItemDetails(PLANT, aItemNum);

			if (str.equalsIgnoreCase("")) {
				str = XMLUtils.getXMLMessage(1, "Item details not found?");
			}

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			str = XMLUtils.getXMLMessage(1, e.getMessage());
		}

		return str;
	}

	private String load_item_details(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException,
			Exception {
		String str = "", aPlant = "", aItem = "";
		try {

			aPlant = StrUtils.fString(request.getParameter("PLANT"));
			aItem = StrUtils.fString(request.getParameter("ITEM_NUM"));

			String itemDesc = new ItemMstDAO().getItemDesc(aPlant, aItem);

			// /
			xmlStr = XMLUtils.getXMLHeader();
			xmlStr += XMLUtils.getStartNode("ItemDetails");

			xmlStr += XMLUtils.getXMLNode("status", "0");
			xmlStr += XMLUtils.getXMLNode("description", "");
			xmlStr += XMLUtils.getXMLNode("item", aItem);
			xmlStr += XMLUtils.getXMLNode("itemDesc", itemDesc);

			xmlStr += XMLUtils.getEndNode("ItemDetails");

			// /

			if (xmlStr.equalsIgnoreCase("")) {
				xmlStr = XMLUtils.getXMLMessage(1, "Item Not found");
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
			// str = XMLUtils.getXMLMessage(1, e.getMessage());
		}

		return xmlStr;
	}

	private String process_orderReceiving(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException,
			Exception {
		Map receiveMaterial_HM = null;
		String PLANT = "", PO_NUM = "", ITEM_NUM = "", PO_LN_NUM = "";
		String LOGIN_USER = "";
		String ITEM_BATCH = "", ITEM_QTY = "", ITEM_EXPDATE = "", ITEM_LOC = "";
		try {
			PLANT = StrUtils.fString(request.getParameter("PLANT"));
			PO_NUM = StrUtils.fString(request.getParameter("PO_NUM"));
			PO_LN_NUM = StrUtils.fString(request.getParameter("PO_LN_NUM"));
			ITEM_NUM = StrUtils.fString(request.getParameter("ITEM_NUM"));
			LOGIN_USER = StrUtils.fString(request.getParameter("LOGIN_USER"));

			ITEM_BATCH = StrUtils.fString(request.getParameter("ITEM_BATCH"));
			ITEM_QTY = StrUtils.fString(request.getParameter("ITEM_QTY"));
			ITEM_EXPDATE = StrUtils.fString(request
					.getParameter("ITEM_EXPDATE"));

			ITEM_LOC = StrUtils.fString(request.getParameter("ITEM_LOC"));

			if (ITEM_EXPDATE.length() > 8) {
				ITEM_EXPDATE = ITEM_EXPDATE.substring(6, 10) + "-"
						+ ITEM_EXPDATE.substring(3, 5) + "-"
						+ ITEM_EXPDATE.substring(0, 2);
			}

			receiveMaterial_HM = new HashMap();
			receiveMaterial_HM.put(IConstants.PLANT, PLANT);
			receiveMaterial_HM.put(IConstants.ITEM, ITEM_NUM);
			receiveMaterial_HM.put(IConstants.PODET_PONUM, PO_NUM);
			receiveMaterial_HM.put(IConstants.PODET_POLNNO, PO_LN_NUM);
			receiveMaterial_HM.put(IConstants.LOC, ITEM_LOC);
			receiveMaterial_HM.put(IConstants.LOGIN_USER, LOGIN_USER);
			receiveMaterial_HM.put(IConstants.CUSTOMER_CODE, _POUtil
					.getCustCode(PLANT, PO_NUM));
			receiveMaterial_HM.put(IConstants.INV_BATCH, ITEM_BATCH);
			receiveMaterial_HM.put(IConstants.INV_QTY, ITEM_QTY);
			receiveMaterial_HM.put(IConstants.INV_EXP_DATE, ITEM_EXPDATE);

			// GET JOBNUM
			receiveMaterial_HM.put(IConstants.JOB_NUM, _POUtil.getJobNum(PLANT,
					PO_NUM));

			xmlStr = "";

			// check for item in location
			Hashtable htLocMst = new Hashtable();
			htLocMst.put("plant", receiveMaterial_HM.get(IConstants.PLANT));
			htLocMst.put("item", receiveMaterial_HM.get(IConstants.ITEM));
			htLocMst.put("loc", receiveMaterial_HM.get(IConstants.LOC));

			boolean flag = true;

			if (flag) {

				xmlStr = _POUtil.process_ReceiveMaterial(receiveMaterial_HM);
			} else {
				throw new Exception(" Product Received already ");
			}

		} catch (Exception e) {

			this.mLogger.exception(this.printLog, "", e);

			throw e;
		}

		return xmlStr;
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
