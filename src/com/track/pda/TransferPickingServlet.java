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
import com.track.db.util.InvMstUtil;
import com.track.db.util.TOUtil;
import com.track.db.util.UserLocUtil;
import com.track.util.DateUtils;
import com.track.util.IMLogger;
import com.track.util.MLogger;
import com.track.util.StrUtils;
import com.track.util.XMLUtils;

public class TransferPickingServlet extends HttpServlet implements IMLogger {
	private boolean printLog = MLoggerConstant.TransferPickingServlet_PRINTPLANTMASTERLOG;
	private boolean printInfo = MLoggerConstant.TransferPickingServlet_PRINTPLANTMASTERINFO;
	private static final long serialVersionUID = -1590225182999455465L;
	private static final String CONTENT_TYPE = "text/xml";
	private TOUtil tOUtil = new TOUtil();;

	public void init() throws ServletException {

	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType(CONTENT_TYPE);
		PrintWriter out = response.getWriter();
		String outputValue = "";
		this.setMapDataToLogger(this.populateMapData(StrUtils.fString(request
				.getParameter("PLANT")), StrUtils.fString(request
						.getParameter("LOGIN_USER")) + " PDA_USER"));
		tOUtil.setmLogger(mLogger);
		if (this.printInfo) {
			diaplayInfoLogs(request);
		}
		try {
			String action = request.getParameter("action").trim();

			if (action.equalsIgnoreCase("LOAD_TRANSFER_ORDERS")) {
				outputValue = "";
				outputValue = loadTransferOrders(request, response);
			}
			if (action.equalsIgnoreCase("LOAD_TRANSFER_ORDER_SUB_DETAILS")) {
				outputValue = "";
				outputValue = loadTransferOrderSubDetails(request, response);
			}
			if (action.equalsIgnoreCase("LOAD_TRANSFER_ORDER_ITEM_DETAILS")) {
				outputValue = "";
				outputValue = loadTransferOrderItemDetails(request, response);
			}
			if (action.equalsIgnoreCase("LOAD_BATCH_DETAILS")) {
				outputValue = "";
				outputValue = loadBatchDetails(request, response);
			}
			if (action.equalsIgnoreCase("PROCESS_TRANSFER_PICKING")) {
				outputValue = "";
				outputValue = processTransferPicking(request, response);
			}
			if (action.equalsIgnoreCase("PROCESS_TRANSFER_PICKING_RANDOM")) {
				outputValue = "";
				outputValue = processTransferPickingRandom(request, response);
			}
					
			if (action.equalsIgnoreCase("GET_BATCH_CODES")) {
				outputValue = "";
				outputValue = getBatchCode(request, response);
			}
			 if (action.equalsIgnoreCase("GET_PICKING_ITEM")) {
				 outputValue = "";
				 outputValue =  getPickingItem(request, response);
			}
			if (action.equalsIgnoreCase("GET_BATCH_CODES_RANDOM")) {
				 outputValue = "";
				 outputValue = getBatchCodeRandom(request, response);
			}
			if (action.equalsIgnoreCase("GET_BATCH_CODES_RANDOM_BYBATCH")) {
				 outputValue = "";
				 outputValue = getBatchCodeByBatchRandom(request, response);
			}
			
			
			if (action.equalsIgnoreCase("LOAD_TRANSFER_ORDER_ITEM_DETAILS_RANDOM")) {
				outputValue = "";
				outputValue = loadTransferOrderItemDetailsRandom(request, response);
			}
			 
			 
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			outputValue = XMLUtils.getXMLMessage(1, "Unable to process! "
					+ e.getMessage());
		}
		out.write(outputValue);
		out.close();
	}

	private String getBatchCode(HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		String xmlStr = "";
		try {
			String itemNo = StrUtils.fString(request.getParameter("ITEM_NUM"));
			String plant = StrUtils.fString(request.getParameter("PLANT"));
		        String userID = StrUtils.fString(request.getParameter("LOGIN_USER"));
			String fromLoc = StrUtils.fString(request.getParameter("FROM_LOC"));
                      
                    
			InvMstUtil itM = new InvMstUtil();
			itM.setmLogger(mLogger);
			xmlStr = itM.getBatchDetailForTransferOrder(plant, itemNo, fromLoc,userID);
			if (xmlStr.length() == 0) {
				xmlStr = XMLUtils.getXMLMessage(1, "Batch Not found");
			}

			return xmlStr;
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		}
	}

	private String loadTransferOrderSubDetails(HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		String xmlStr = "";
		try {
			String toNum = StrUtils.fString(request.getParameter("TO_NUM"));
			String plant = StrUtils.fString(request.getParameter("PLANT"));
			String loginUser = StrUtils.fString(request
					.getParameter("LOGIN_USER"));

			xmlStr = this.tOUtil.getOpenTransOrderSupDetails(plant, toNum);
			if (xmlStr.equalsIgnoreCase("")) {
				xmlStr = XMLUtils.getXMLMessage(1, "No Outbound Orders Found");
			}
		} catch (Exception e) {

			this.mLogger.exception(this.printLog, "", e);

		}
		return xmlStr;
	}

	/* ************Modification History*********************************
	   Oct 17 2014 Bruhan, Description: To include Issue date
	*/
	private String processTransferPicking(HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		String xmlStr = "";
		try {
			String plant = StrUtils.fString(request.getParameter("PLANT"));
			String toNum = StrUtils.fString(request.getParameter("TO_NUM"));
			String toLnNum = StrUtils
					.fString(request.getParameter("TO_LN_NUM"));
			String itemNo = StrUtils.fString(request.getParameter("ITEM_NUM"));
			String itemDesc = StrUtils.fString(request
					.getParameter("ITEM_DESC"));
			String loginUser = StrUtils.fString(request
					.getParameter("LOGIN_USER"));
			String itemBatch = StrUtils.fString(request
					.getParameter("ITEM_BATCH"));
			String itemQty = StrUtils.fString(request.getParameter("ITEM_QTY"));
			String itemExpdate = StrUtils.fString(request
					.getParameter("ITEM_EXPDATE"));
			String itemLoc = StrUtils.fString(request.getParameter("ITEM_LOC"));
			String fax = StrUtils.fString(request.getParameter("FAX"));
			String ref = StrUtils.fString(request.getParameter("REF"));
			String orederQty = StrUtils.removeFormat(StrUtils
					.fString(request.getParameter("ORD_QTY")));
			String itemRemarks = StrUtils.fString(request
					.getParameter("ITEM_REMARKS"));
			String customerName = StrUtils.fString(request
					.getParameter("CUSTNAME"));
			String toLoc = StrUtils.fString(request.getParameter("TO_LOC"));
			DateUtils _dateUtils = new DateUtils();
			String curDate =_dateUtils.getDate();
			String strTranDate    = curDate.substring(6)+"-"+ curDate.substring(3,5)+"-"+curDate.substring(0,2);
			String strIssueDate    = curDate.substring(0,2)+"/"+ curDate.substring(3,5)+"/"+curDate.substring(6);
		double itemqty = Double.parseDouble(itemQty);	
		double ordqty = Double.parseDouble(orederQty);	
		itemqty = StrUtils.RoundDB(itemqty, IConstants.DECIMALPTS);
		ordqty = StrUtils.RoundDB(ordqty, IConstants.DECIMALPTS);
		
		itemQty = String.valueOf(itemqty);
		orederQty = String.valueOf(ordqty);
	
		    UserLocUtil uslocUtil = new UserLocUtil();
		    uslocUtil.setmLogger(mLogger);
		        boolean isvalidlocforUser  =uslocUtil.isValidLocInLocmstForUser(plant,loginUser,itemLoc);
		        if(!isvalidlocforUser){
		            throw new Exception("From Loc :"+itemLoc+" is not User Assigned Location");
		        }

			itemDesc=StrUtils.replaceCharacters2Recv(itemDesc);
			customerName=StrUtils.replaceCharacters2Recv(customerName);

			Map<String, String> receiveMaterial_HM = new HashMap<String, String>();
			receiveMaterial_HM.put(IConstants.PLANT, plant);
			receiveMaterial_HM.put(IConstants.ITEM, itemNo);
			receiveMaterial_HM.put(IConstants.ITEM_DESC, itemDesc);
			receiveMaterial_HM.put(IConstants.TODET_TONUM, toNum);
			receiveMaterial_HM.put(IConstants.TODET_TOLNNO, toLnNum);
			receiveMaterial_HM.put(IConstants.LOC, itemLoc);
			receiveMaterial_HM.put(IConstants.LOC2, "TEMP_TO_"+itemLoc);
			receiveMaterial_HM.put(IConstants.ORD_QTY, orederQty);
			receiveMaterial_HM.put(IConstants.LOGIN_USER, loginUser);
			receiveMaterial_HM.put(IConstants.CUSTOMER_CODE, tOUtil.getCustCode(plant, toNum));
			receiveMaterial_HM.put(IConstants.BATCH, itemBatch);
			receiveMaterial_HM.put(IConstants.QTY, itemQty);
			receiveMaterial_HM.put(IConstants.INV_EXP_DATE, itemExpdate);
			receiveMaterial_HM.put(IConstants.JOB_NUM, tOUtil.getJobNum(plant,
					toNum));
			receiveMaterial_HM.put(IConstants.FROMLOC, itemLoc);
			receiveMaterial_HM.put(IConstants.TOLOC, toLoc);
			receiveMaterial_HM.put(IConstants.CUSTOMER_NAME, customerName);
			receiveMaterial_HM.put(IConstants.REMARKS, "");
			receiveMaterial_HM.put("INV_QTY", "1");
            receiveMaterial_HM.put("TYPE", "");
 			receiveMaterial_HM.put(IConstants.TRAN_DATE, strTranDate);
            receiveMaterial_HM.put(IConstants.ISSUEDATE, strIssueDate);
			boolean flag = true;

			if (flag) {

				xmlStr = tOUtil.process_ToPickingForPDA(receiveMaterial_HM);
			} else {
				throw new Exception(" Product Picked already ");
			}

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			xmlStr = XMLUtils.getXMLMessage(1, e.getMessage());
			throw e;
		}
		return xmlStr;
	}
	
	/* ************Modification History*********************************
	   Oct 17 2014 Bruhan, Description: To include Issue date
	*/
	private String processTransferPickingRandom(HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		String xmlStr = "";
		try {
			String plant = StrUtils.fString(request.getParameter("PLANT"));
			String toNum = StrUtils.fString(request.getParameter("TO_NUM"));
			String itemNo = StrUtils.fString(request.getParameter("ITEM_NUM"));
			String itemDesc = StrUtils.fString(request.getParameter("ITEM_DESC"));
			String loginUser = StrUtils.fString(request.getParameter("LOGIN_USER"));
			String itemBatch = StrUtils.fString(request.getParameter("ITEM_BATCH"));
			String itemQty = StrUtils.fString(request.getParameter("ITEM_QTY"));
			String itemExpdate = StrUtils.fString(request.getParameter("ITEM_EXPDATE"));
			String itemLoc = StrUtils.fString(request.getParameter("ITEM_LOC"));
			String itemRemarks = StrUtils.fString(request.getParameter("ITEM_REMARKS"));
			String customerName = StrUtils.fString(request.getParameter("CUSTNAME"));
			String toLoc = StrUtils.fString(request.getParameter("TO_LOC"));
			DateUtils _dateUtils = new DateUtils();
			String curDate =_dateUtils.getDate();
			String strTranDate    = curDate.substring(6)+"-"+ curDate.substring(3,5)+"-"+curDate.substring(0,2);
			String strIssueDate    = curDate.substring(0,2)+"/"+ curDate.substring(3,5)+"/"+curDate.substring(6);
			double itemqty = Double.parseDouble(itemQty);	
			itemqty = StrUtils.RoundDB(itemqty, IConstants.DECIMALPTS);
			itemQty = String.valueOf(itemqty);
			
		    UserLocUtil uslocUtil = new UserLocUtil();
		    uslocUtil.setmLogger(mLogger);
		        boolean isvalidlocforUser  =uslocUtil.isValidLocInLocmstForUser(plant,loginUser,itemLoc);
		        if(!isvalidlocforUser){
		            throw new Exception("From Loc :"+itemLoc+" is not User Assigned Location");
		        }
			itemDesc=StrUtils.replaceCharacters2Recv(itemDesc);
			customerName=StrUtils.replaceCharacters2Recv(customerName);

			Map<String, String> receiveMaterial_HM = new HashMap<String, String>();
			receiveMaterial_HM.put(IConstants.PLANT, plant);
			receiveMaterial_HM.put(IConstants.ITEM, itemNo);
			receiveMaterial_HM.put(IConstants.ITEM_DESC, itemDesc);
			receiveMaterial_HM.put(IConstants.TODET_TONUM, toNum);
			receiveMaterial_HM.put(IConstants.LOC, itemLoc);
			receiveMaterial_HM.put(IConstants.LOC2, "TEMP_TO_"+itemLoc);
			receiveMaterial_HM.put(IConstants.LOGIN_USER, loginUser);
			receiveMaterial_HM.put(IConstants.CUSTOMER_CODE, tOUtil.getCustCode(plant, toNum));
			receiveMaterial_HM.put(IConstants.BATCH, itemBatch);
			receiveMaterial_HM.put(IConstants.QTY, itemQty);
			receiveMaterial_HM.put(IConstants.INV_EXP_DATE, itemExpdate);
			receiveMaterial_HM.put(IConstants.JOB_NUM, tOUtil.getJobNum(plant,toNum));
			receiveMaterial_HM.put(IConstants.FROMLOC, itemLoc);
			receiveMaterial_HM.put(IConstants.TOLOC, toLoc);
			receiveMaterial_HM.put(IConstants.CUSTOMER_NAME, customerName);
			receiveMaterial_HM.put(IConstants.REMARKS, "");
			receiveMaterial_HM.put("INV_QTY", "1");
			receiveMaterial_HM.put(IConstants.TRAN_DATE, strTranDate);
			receiveMaterial_HM.put(IConstants.ISSUEDATE, strIssueDate);
			boolean flag = true;

			if (flag) {

				xmlStr = tOUtil.process_ToPickingForPDARandom(receiveMaterial_HM);
			} else {
				throw new Exception(" Product Received already ");
			}

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			xmlStr = XMLUtils.getXMLMessage(1, e.getMessage());
			throw e;
		}
		return xmlStr;
	}

	private String loadBatchDetails(HttpServletRequest request,
			HttpServletResponse response) {

		String str = "";
		try {

			String plant = StrUtils.fString(request.getParameter("PLANT"));
			String item = StrUtils.fString(request.getParameter("ITEM"));
			String batch = StrUtils.fString(request.getParameter("BATCH"));

			InvMstUtil _InvMstUtil = new InvMstUtil();
			_InvMstUtil.setmLogger(mLogger);
			str = _InvMstUtil.load_inv_details_for_Lot_xml(plant, item, batch);
			if (str.equalsIgnoreCase("")) {
				str = XMLUtils.getXMLMessage(1, "Not a valid batch ");
			}
		}

		catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			str = XMLUtils.getXMLMessage(1, e.getMessage());
		}
		return str;
	}

	private String loadTransferOrderItemDetails(HttpServletRequest request,
			HttpServletResponse response) {

		String str = "";
		try {
			String plant = StrUtils.fString(request.getParameter("PLANT"));
			String toNum = StrUtils.fString(request.getParameter("TO_NUM"));

			str = this.tOUtil.getTransOrderItemDetails(plant, toNum,
					Boolean.FALSE);
			if (str.equalsIgnoreCase("")) {
				str = XMLUtils.getXMLMessage(1, "No Outbound Orders Found");
			}
		}

		catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			str = XMLUtils.getXMLMessage(1, e.getMessage());
		}

		return str;
	}

	private String loadTransferOrders(HttpServletRequest request,
			HttpServletResponse response) {
		String str = "";
		try {

			String plant = StrUtils.fString(request.getParameter("PLANT"));

			Hashtable<String, String> ht = new Hashtable<String, String>();
			ht.put(IConstants.PLANT, plant);

			str = this.tOUtil.getOpenTransferOrder(plant, Boolean
					.valueOf(false));

			if (str.equalsIgnoreCase("")) {
				str = XMLUtils.getXMLMessage(1, "No Outbound Orders Found");
			}

		}

		catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			str = XMLUtils.getXMLMessage(1, e.getMessage());
		}

		return str;
	}
	
	private String getPickingItem(HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		String xmlStr = "";
		try {
			String toNum = StrUtils.fString(request.getParameter("TO_NUM"));
			String itemNo = StrUtils.fString(request.getParameter("ITEM_NUM"));
		    String plant = StrUtils.fString(request.getParameter("PLANT"));
		    String userID = StrUtils.fString(request.getParameter("LOGIN_USER"));
			xmlStr = tOUtil.getPickingItemDetailsTransfer(plant, toNum,itemNo,userID);
			if (xmlStr.equalsIgnoreCase("")) {
				xmlStr = XMLUtils.getXMLMessage(1, "Item Not found");
			}
			return xmlStr;
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		}
	}
	private String getBatchCodeRandom(HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		String xmlStr = "";
		try {
			String itemNo = StrUtils.fString(request.getParameter("ITEM_NUM"));
			String plant = StrUtils.fString(request.getParameter("PLANT"));
		     String userID = StrUtils.fString(request.getParameter("LOGIN_USER"));
		     String loc = StrUtils.fString(request.getParameter("FROM_LOC"));
			String batch = StrUtils.fString(request.getParameter("BATCH"));
                      
                    
			InvMstUtil itM = new InvMstUtil();
			itM.setmLogger(mLogger);
			xmlStr = itM.getBatchDetailForTransferRandom(plant, itemNo,loc, batch,userID);
			if (xmlStr.length() == 0) {
				xmlStr = XMLUtils.getXMLMessage(1, "Batch Not found");
			}

			return xmlStr;
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		}
	}
	private String getBatchCodeByBatchRandom(HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		String xmlStr = "";
		try {
			String itemNo = StrUtils.fString(request.getParameter("ITEM_NUM"));
			String plant = StrUtils.fString(request.getParameter("PLANT"));
		     String userID = StrUtils.fString(request.getParameter("LOGIN_USER"));
		     String loc = StrUtils.fString(request.getParameter("FROM_LOC"));
			String batch = StrUtils.fString(request.getParameter("BATCH"));
                      
                    
			InvMstUtil itM = new InvMstUtil();
			itM.setmLogger(mLogger);
			xmlStr = itM.getBatchDetailByBatchTransferRandom(plant, itemNo,loc, batch,userID);
			if (xmlStr.length() == 0) {
				xmlStr = XMLUtils.getXMLMessage(1, "Batch Not found");
			}

			return xmlStr;
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		}
	}
	private String loadTransferOrderItemDetailsRandom(HttpServletRequest request,
			HttpServletResponse response) {

		String str = "";
		try {
			String plant = StrUtils.fString(request.getParameter("PLANT"));
			String toNum = StrUtils.fString(request.getParameter("TO_NUM"));

			str = this.tOUtil.getTransOrderItemDetailsRandom(plant, toNum,
					Boolean.FALSE);
			if (str.equalsIgnoreCase("")) {
				str = XMLUtils.getXMLMessage(1, "No Outbound Orders Found");
			}
		}

		catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			str = XMLUtils.getXMLMessage(1, e.getMessage());
		}

		return str;
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

	public void destroy() {
	}

	public HashMap<String, String> populateMapData(String companyCode,
			String userCode) {
		HashMap<String, String> loggerDetailsHasMap = new HashMap<String, String>();
		loggerDetailsHasMap.put(MLogger.COMPANY_CODE, companyCode);
		loggerDetailsHasMap.put(MLogger.USER_CODE, userCode);
		return loggerDetailsHasMap;

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
