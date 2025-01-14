package com.track.pda;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.track.constants.MLoggerConstant;
import com.track.dao.InvMstDAO;
import com.track.dao.ItemMstDAO;
import com.track.dao.LocMstDAO;
import com.track.util.IMLogger;
import com.track.util.MLogger;
import com.track.util.StrUtils;
import com.track.util.XMLUtils;

public class CommonValidationServlet extends HttpServlet implements IMLogger {
	private boolean printLog = MLoggerConstant.CommonValidationServlet_PRINTPLANTMASTERLOG;
	private boolean printInfo = MLoggerConstant.CommonValidationServlet_PRINTPLANTMASTERINFO;
	private static final long serialVersionUID = 5327935462674844609L;

	private String PLANT = "";

	private String xmlStr = "";
	private String action = "";

	private static final String CONTENT_TYPE = "text/xml";

	public void init() throws ServletException {

	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType(CONTENT_TYPE);
		PrintWriter out = response.getWriter();

		this.setMapDataToLogger(this.populateMapData(StrUtils.fString(request
				.getParameter("PLANT")), StrUtils.fString(request
				.getParameter("LOGIN_USER"))
				+ " PDA_USER"));
		if (this.printInfo) {
			diaplayInfoLogs(request);
		}
		try {
			action = request.getParameter("action").trim();

			if (action.equalsIgnoreCase("load_item_details")) {
				xmlStr = "";
				xmlStr = load_Item_Details(request, response);
			}

			if (action.equalsIgnoreCase("validate_location")) {
				xmlStr = "";
				xmlStr = validate_location(request, response);
			}

			if (action.equalsIgnoreCase("validate_item")) {
				xmlStr = "";
				xmlStr = validate_item(request, response);
			}

			if (action.equalsIgnoreCase("validate_batch")) {
				xmlStr = "";
				xmlStr = validateBatch(request, response);
			}
			if (action.equalsIgnoreCase("validate_OBLocation")) {
				xmlStr = "";
				xmlStr = validate_OBLocation(request, response);
			}
			if (action.equalsIgnoreCase("validate_GoodsIssueLocation")) {
				xmlStr = "";
				xmlStr = validate_GoodsIssueLocation(request, response);
			}
					

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			xmlStr = XMLUtils.getXMLMessage(1, "Unable to process? "
					+ e.getMessage());
		}
		out.write(xmlStr);
		out.close();
	}

	@SuppressWarnings("all")
	private String validateBatch(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String str = "", aPlant = "", item = "", batchId = "";
		try {

			aPlant = StrUtils.fString(request.getParameter("PLANT"));
			item = StrUtils.fString(request.getParameter("ITEM"));
			batchId = StrUtils.fString(request.getParameter("BATCH"));

			Hashtable<String, String> ht = new Hashtable<String, String>();
			ht.put("PLANT", aPlant);
			ht.put("ITEM", item);
			ht.put("USERFLD4", batchId);
			InvMstDAO itM = new InvMstDAO();
			itM.setmLogger(mLogger);
			boolean itemFound = itM.isExisit(ht, "");

			xmlStr = XMLUtils.getXMLHeader();
			xmlStr = xmlStr + XMLUtils.getStartNode("itemDetails");

			if (itemFound) {
				xmlStr = xmlStr + XMLUtils.getXMLNode("status", "0");
				xmlStr = xmlStr
						+ XMLUtils.getXMLNode("description", "Product found");
			} else {
				xmlStr = xmlStr + XMLUtils.getXMLNode("status", "1");
				xmlStr = xmlStr
						+ XMLUtils.getXMLNode("description",
								"Product not found");
			}

			xmlStr = xmlStr + XMLUtils.getEndNode("itemDetails");

			if (xmlStr.equalsIgnoreCase("")) {
				throw new Exception("Product not found");
			}
		} catch (Exception e) {
			throw e;
		}

		return xmlStr;
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

	public void destroy() {
	}

	private String load_Item_Details(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {
		String str = "", aPlant = "", aItemNum = "";
		try {

			PLANT = StrUtils.fString(request.getParameter("PLANT"));
			aItemNum = StrUtils.fString(request.getParameter("ITEM_NUM"));

			Hashtable ht = new Hashtable();
			ItemMstDAO itM = new ItemMstDAO();
			itM.setmLogger(mLogger);
			String itemDesc = itM.getItemDesc(PLANT, aItemNum);
			xmlStr = XMLUtils.getXMLHeader();
			xmlStr = xmlStr + XMLUtils.getStartNode("itemDetails");

			xmlStr = xmlStr + XMLUtils.getXMLNode("status", "0");
			xmlStr = xmlStr + XMLUtils.getXMLNode("description", "");
			xmlStr = xmlStr + XMLUtils.getXMLNode("item", aItemNum);
			xmlStr = xmlStr + XMLUtils.getXMLNode("itemDesc", itemDesc);
			

			xmlStr = xmlStr + XMLUtils.getEndNode("itemDetails");

			if (str.equalsIgnoreCase("")) {
				str = XMLUtils.getXMLMessage(1, "Item details not found?");
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			str = XMLUtils.getXMLMessage(1, e.getMessage());
		}

		// MLogger.log(0, "load_Item_Details() Ends");

		return str;
	}

	private String validate_location(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException,
			Exception {
		String str = "", aPlant = "", loc = "";
		try {

			PLANT = StrUtils.fString(request.getParameter("PLANT"));
			loc = StrUtils.fString(request.getParameter("LOC"));

			Hashtable ht = new Hashtable();
			ht.put("PLANT", PLANT);
			ht.put("LOC", loc);
			
			LocMstDAO itM = new LocMstDAO();
			itM.setmLogger(mLogger);
			boolean locFound = itM.isExisit(ht, " ISACTIVE='Y'");

			xmlStr = XMLUtils.getXMLHeader();
			xmlStr = xmlStr + XMLUtils.getStartNode("locationDetails");
			
			xmlStr = XMLUtils.getXMLHeader();
			xmlStr = xmlStr + XMLUtils.getStartNode("locationDetails");
			
			

			if (locFound) {
				xmlStr = xmlStr + XMLUtils.getXMLNode("status", "0");
				xmlStr = xmlStr
						+ XMLUtils.getXMLNode("description", "Location found");
			} else {
				xmlStr = xmlStr + XMLUtils.getXMLNode("status", "1");
				xmlStr = xmlStr
						+ XMLUtils.getXMLNode("description",
								"Location not found");
			}

			xmlStr = xmlStr + XMLUtils.getEndNode("locationDetails");
			
			

			if (xmlStr.equalsIgnoreCase("")) {
				throw new Exception("Location not found");
			}
		} catch (Exception e) {
			throw e;
		}
		// MLogger.log(0, "validate_location() Ends");
		return xmlStr;
	}
	
	private String validate_OBLocation(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException,
			Exception {
		String str = "",xmlStr="";
		try {

			String plant = StrUtils.fString(request.getParameter("PLANT"));
			String userid= StrUtils.fString(request.getParameter("LOGIN_USER"));
			String dono = StrUtils.fString(request.getParameter("DONO"));
			String item = StrUtils.fString(request.getParameter("ITEM"));
			String loc = StrUtils.fString(request.getParameter("LOC"));
			
			ArrayList alInvMst = null;
			InvMstDAO _InvMstDAO = new InvMstDAO();
			_InvMstDAO.setmLogger(mLogger);
			alInvMst = _InvMstDAO.ValidateOBLocation(plant,dono,item,loc);
									
			xmlStr = XMLUtils.getXMLHeader();
			xmlStr = xmlStr + XMLUtils.getStartNode("locationDetails");
			
			if (alInvMst.size() > 0) {
				xmlStr = xmlStr + XMLUtils.getXMLNode("status", "0");
				xmlStr = xmlStr
						+ XMLUtils.getXMLNode("description", "Location found");
			} else {
				xmlStr = xmlStr + XMLUtils.getXMLNode("status", "1");
				xmlStr = xmlStr
						+ XMLUtils.getXMLNode("description",
								"Location not found");
			}

			xmlStr = xmlStr + XMLUtils.getEndNode("locationDetails");
				

			
		} catch (Exception e) {
			throw e;
		}
		// MLogger.log(0, "validate_location() Ends");
		return xmlStr;
	}
	
	private String validate_GoodsIssueLocation(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException,
			Exception {
		String str = "",xmlStr="";
		try {

			String plant = StrUtils.fString(request.getParameter("PLANT"));
			String userid= StrUtils.fString(request.getParameter("LOGIN_USER"));
			String item = StrUtils.fString(request.getParameter("ITEM"));
			String loc = StrUtils.fString(request.getParameter("LOC"));
			
			ArrayList alInvMst = null;
			InvMstDAO _InvMstDAO = new InvMstDAO();
			_InvMstDAO.setmLogger(mLogger);
			alInvMst = _InvMstDAO.ValidateGoodsIssueLocation(plant,item,loc);
									
			xmlStr = XMLUtils.getXMLHeader();
			xmlStr = xmlStr + XMLUtils.getStartNode("locationDetails");
			
			if (alInvMst.size() > 0) {
				xmlStr = xmlStr + XMLUtils.getXMLNode("status", "0");
				xmlStr = xmlStr
						+ XMLUtils.getXMLNode("description", "Location found");
			} else {
				xmlStr = xmlStr + XMLUtils.getXMLNode("status", "1");
				xmlStr = xmlStr
						+ XMLUtils.getXMLNode("description",
								"Location not found");
			}

			xmlStr = xmlStr + XMLUtils.getEndNode("locationDetails");
				

			
		} catch (Exception e) {
			throw e;
		}
		// MLogger.log(0, "validate_location() Ends");
		return xmlStr;
	}


	private String validate_item(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException,
			Exception {
		String str = "", aPlant = "", item = "";
		try {

			PLANT = StrUtils.fString(request.getParameter("PLANT"));
			item = StrUtils.fString(request.getParameter("ITEM"));

			Hashtable ht = new Hashtable();
			ht.put("PLANT", PLANT);
			ht.put("ITEM", item);
			ItemMstDAO itM = new ItemMstDAO();
			itM.setmLogger(mLogger);
			boolean itemFound = itM.isExisit(ht, "");
			// MLogger.log(0, "isExisit() : " + itemFound);

			xmlStr = XMLUtils.getXMLHeader();
			xmlStr = xmlStr + XMLUtils.getStartNode("itemDetails");

			if (itemFound) {
				xmlStr = xmlStr + XMLUtils.getXMLNode("status", "0");
				xmlStr = xmlStr
						+ XMLUtils.getXMLNode("description", "Product found");
			} else {
				xmlStr = xmlStr + XMLUtils.getXMLNode("status", "1");
				xmlStr = xmlStr
						+ XMLUtils.getXMLNode("description",
								"Product not found");
			}

			xmlStr = xmlStr + XMLUtils.getEndNode("itemDetails");

			if (xmlStr.equalsIgnoreCase("")) {
				throw new Exception("Product not found");
			}
		} catch (Exception e) {
			throw e;
		}
		// MLogger.log(0, "validate_item() Ends");
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