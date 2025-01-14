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

import com.track.constants.IConstants;
import com.track.constants.MLoggerConstant;
import com.track.dao.ItemMstDAO;
import com.track.db.util.InvMstUtil;
import com.track.db.util.LoanUtil;
import com.track.db.util.UserLocUtil;
import com.track.util.DateUtils;
import com.track.util.IMLogger;
import com.track.util.MLogger;
import com.track.util.StrUtils;
import com.track.util.XMLUtils;

public class LoanPickingServlet extends HttpServlet implements IMLogger {
	private boolean printLog = MLoggerConstant.LoanPickingServlet_PRINTPLANTMASTERLOG;
	private boolean printInfo = MLoggerConstant.LoanPickingServlet_PRINTPLANTMASTERINFO;
	private static final long serialVersionUID = -1590225182999455465L;
	private static final String CONTENT_TYPE = "text/xml";
	private LoanUtil loanUtil = new LoanUtil();;

	public void init() throws ServletException {

	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType(CONTENT_TYPE);
		PrintWriter out = response.getWriter();
		String outputValue = "";
		try {
			String action = request.getParameter("action").trim();
			this.setMapDataToLogger(this.populateMapData(StrUtils.fString(request
					.getParameter("PLANT")), StrUtils.fString(request
							.getParameter("LOGIN_USER")) + " PDA_USER"));
			loanUtil.setmLogger(mLogger);
			if (this.printInfo) {
				diaplayInfoLogs(request);
			}
			if (action.equalsIgnoreCase("LOAD_LOAN_ORDERS")) {
				outputValue = "";
				outputValue = loadLoanOrders(request, response);
			}
			if (action.equalsIgnoreCase("LOAD_LOAN_ORDER_SUB_DETAILS")) {
				outputValue = "";
				outputValue = loadLoanOrderSubDetails(request, response);
			}
			if (action.equalsIgnoreCase("LOAD_LOAN_ORDER_ITEM_DETAILS")) {
				outputValue = "";
				outputValue = loadLoanOrderItemDetails(request, response);
			}
			if (action.equalsIgnoreCase("LOAD_BATCH_DETAILS")) {
				outputValue = "";
				outputValue = loadBatchDetails(request, response);
			}
			if (action.equalsIgnoreCase("PROCESS_LOAN_PICKING")) {
				outputValue = "";
				outputValue = processLoanPicking(request, response);
			}
			if (action.equalsIgnoreCase("GET_BATCH_CODES")) {
				outputValue = "";
				outputValue = getBatchCode(request, response);
			}
			if (action.equalsIgnoreCase("PROCESS_LOAN_RECEIPTING")) {
				outputValue = "";
				outputValue = processLoanReceipting(request, response);
			}

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			outputValue = XMLUtils.getXMLMessage(1, "Unable to process! "
					+ e.getMessage());
		}
		out.write(outputValue);
		out.close();
	}

	private String processLoanReceipting(HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		String xmlStr = "";
		try {
			String plant = StrUtils.fString(request.getParameter("PLANT"));
			String orderNum = StrUtils.fString(request
					.getParameter("ORDER_NUM"));
			String orderLnNum = StrUtils.fString(request
					.getParameter("ORDER_LN_NUM"));
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
			String orederQty = StrUtils
					.fString(request.getParameter("ORD_QTY"));
			String itemRemarks = StrUtils.fString(request
					.getParameter("ITEM_REMARKS"));
			String customerName = StrUtils.fString(request
					.getParameter("CUSTNAME"));
			String toLoc = StrUtils.fString(request.getParameter("TO_LOC"));

			Map<String, String> receiveMaterial_HM = new HashMap<String, String>();

			receiveMaterial_HM.put(IConstants.PLANT, plant);
			receiveMaterial_HM.put(IConstants.ITEM, itemNo);
			receiveMaterial_HM.put(IConstants.ITEM_DESC, itemDesc);
			receiveMaterial_HM.put(IConstants.LOANDET_ORDNO, orderNum);
			receiveMaterial_HM.put(IConstants.LOANDET_ORDLNNO, orderLnNum);
			receiveMaterial_HM.put(IConstants.LOC, itemLoc);
			receiveMaterial_HM.put(IConstants.ORD_QTY, orederQty);
			receiveMaterial_HM.put(IConstants.LOGIN_USER, loginUser);
			receiveMaterial_HM.put(IConstants.CUSTOMER_CODE, this.loanUtil
					.getCustCode(plant, orderNum));
			receiveMaterial_HM.put(IConstants.BATCH, itemBatch);
			receiveMaterial_HM.put(IConstants.RECV_QTY, itemQty);
			receiveMaterial_HM.put(IConstants.INV_EXP_DATE, itemExpdate);
			receiveMaterial_HM.put(IConstants.JOB_NUM, this.loanUtil.getJobNum(
					plant, orderNum));
			receiveMaterial_HM.put(IConstants.LOC, itemLoc);
			receiveMaterial_HM.put(IConstants.LOC1, toLoc);
			receiveMaterial_HM.put(IConstants.CUSTOMER_NAME, customerName);

			boolean flag = true;

			if (flag) {
				boolean flagTemp = true;
				flagTemp = this.loanUtil
						.process_LoanOrderReceving(receiveMaterial_HM);
				xmlStr = "";
				if (flagTemp == true) {
					xmlStr = XMLUtils.getXMLMessage(1,
							"Product :  issued successfully!");
				} else {
					xmlStr = XMLUtils.getXMLMessage(0,
							"Error in issuing Product ");
				}
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

	private String getBatchCode(HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		String xmlStr = "";
		try {
			String itemNo = StrUtils.fString(request.getParameter("ITEM_NUM"));
			String plant = StrUtils.fString(request.getParameter("PLANT"));
		        String userID = StrUtils.fString(request.getParameter("LOGIN_USER"));
			String fromLoc = StrUtils.fString(request.getParameter("FROM_LOC"));
			xmlStr = new InvMstUtil().getBatchDetail(plant, itemNo, fromLoc,userID);
			if (xmlStr.equalsIgnoreCase("")) {
				xmlStr = XMLUtils.getXMLMessage(1, "Item Not found");
			}

			return xmlStr;
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		}
	}

	private String loadLoanOrderSubDetails(HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		String xmlStr = "";
		try {
			String orderNum = StrUtils.fString(request
					.getParameter("ORDER_NUM"));
			String plant = StrUtils.fString(request.getParameter("PLANT"));
			String loginUser = StrUtils.fString(request
					.getParameter("LOGIN_USER"));

			xmlStr = this.loanUtil.getOpenLoanOrderSupDetailsPda(plant,
					orderNum, Boolean.TRUE);
			if (xmlStr.equalsIgnoreCase("")) {
				xmlStr = XMLUtils.getXMLMessage(1, "No Outbound Orders Found");
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		}
		return xmlStr;
	}

	/* ************Modification History*********************************
	   Oct 24 2014 Bruhan, Description: To include Issue date
	*/
	private String processLoanPicking(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
	        StrUtils strUtils = new StrUtils();
		String xmlStr = "";
		try {
			String plant = StrUtils.fString(request.getParameter("PLANT"));
			String orderNum = StrUtils.fString(request
					.getParameter("ORDER_NUM"));
			String orderLnNum = StrUtils.fString(request
					.getParameter("ORDER_LN_NUM"));
			String itemNo = StrUtils.fString(request.getParameter("ITEM_NUM"));
			String itemDesc = StrUtils.fString(request
					.getParameter("ITEM_DESC"));
			String loginUser = StrUtils.fString(request
					.getParameter("LOGIN_USER"));
			String itemBatch = StrUtils.fString(request
					.getParameter("ITEM_BATCH"));
			String itemQty = StrUtils.removeFormat(StrUtils.fString(request.getParameter("ITEM_QTY")));
			String itemExpdate = StrUtils.fString(request
					.getParameter("ITEM_EXPDATE"));
			String itemLoc = StrUtils.fString(request.getParameter("ITEM_LOC"));
			String fax = StrUtils.fString(request.getParameter("FAX"));
			String ref = StrUtils.fString(request.getParameter("REF"));
                        String REMARKS = StrUtils.fString(request.getParameter("REF"));
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
			
			
			itemDesc=StrUtils.replaceCharacters2Recv(itemDesc);
			//itemRemarks=StrUtils.replaceCharacters2Recv(itemRemarks);
			customerName =StrUtils.replaceCharacters2Recv(customerName );
			
			double orderqty = Double.parseDouble(orederQty);
			double itemqty = Double.parseDouble(itemQty);
			
			orderqty = StrUtils.RoundDB(orderqty, IConstants.DECIMALPTS);
			itemqty = StrUtils.RoundDB(itemqty, IConstants.DECIMALPTS);
			
			itemQty = String.valueOf(itemqty);
			orederQty = String.valueOf(orderqty);
			orederQty = StrUtils.formatThreeDecimal(orederQty);
		    UserLocUtil uslocUtil = new UserLocUtil();
		    uslocUtil.setmLogger(mLogger);
		    boolean isvalidlocforUser  =uslocUtil.isValidLocInLocmstForUser(plant,loginUser,itemLoc);
		    if (!isvalidlocforUser) {
		        throw new Exception("Picking Loc :"+toLoc+" is not User Assigned Location");
		    }
			Map<String, String> receiveMaterial_HM = new HashMap<String, String>();

			receiveMaterial_HM.put(IConstants.PLANT, plant);
			receiveMaterial_HM.put(IConstants.ITEM, itemNo);
			receiveMaterial_HM.put(IConstants.ITEM_DESC, strUtils.InsertQuotes(new ItemMstDAO().getItemDesc(plant,itemNo)));
			receiveMaterial_HM.put(IConstants.LOANDET_ORDNO, orderNum);
			receiveMaterial_HM.put(IConstants.LOANDET_ORDLNNO, orderLnNum);
			receiveMaterial_HM.put(IConstants.ORD_QTY, orederQty);
			receiveMaterial_HM.put(IConstants.LOGIN_USER, loginUser);
			receiveMaterial_HM.put(IConstants.CUSTOMER_CODE, this.loanUtil
					.getCustCode(plant, orderNum));
			receiveMaterial_HM.put(IConstants.BATCH, itemBatch);
			receiveMaterial_HM.put(IConstants.QTY_ISSUE, itemQty);
			receiveMaterial_HM.put(IConstants.INV_EXP_DATE, itemExpdate);
			receiveMaterial_HM.put(IConstants.JOB_NUM, this.loanUtil.getJobNum(
					plant, orderNum));
			receiveMaterial_HM.put(IConstants.LOC, itemLoc);
			receiveMaterial_HM.put(IConstants.LOC1, toLoc);
			receiveMaterial_HM.put(IConstants.CUSTOMER_NAME, customerName);
			receiveMaterial_HM.put("INV_QTY1", "1");
		         receiveMaterial_HM.put(IConstants.REMARKS, REMARKS);
		    receiveMaterial_HM.put(IConstants.REMARKS, REMARKS);
		    receiveMaterial_HM.put(IConstants.TRAN_DATE, strTranDate);
		    receiveMaterial_HM.put(IConstants.ISSUEDATE, strIssueDate);

			boolean flag = true;

			if (flag) {
				boolean flagTemp = true;
				flagTemp = this.loanUtil
						.process_LoanOrderPicking(receiveMaterial_HM);
				xmlStr = "";
				if (flagTemp == true) {
					xmlStr = XMLUtils.getXMLMessage(1, "Product :  " + itemNo
							+ " Picked successfully!");
				} else {
					xmlStr = XMLUtils.getXMLMessage(0,
							"Error in Picking Product :  " + itemNo);
				}
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
		String xmlStr = "";
		try {

			String plant = StrUtils.fString(request.getParameter("PLANT"));
			String item = StrUtils.fString(request.getParameter("ITEM"));
			String batch = StrUtils.fString(request.getParameter("BATCH"));
			String orderNo = StrUtils.fString(request.getParameter("ORDER_NO"));

			String query = " LOC, LOC1 ";
			Hashtable<String, String> ht = new Hashtable<String, String>();
			ht.put("PLANT", plant);
			ht.put("DONO", orderNo);
			ArrayList<Map<String, String>> resultV = this.loanUtil
					.getLoanHdrDetails(query, ht);
			if (resultV.size() > 0) {
				xmlStr = XMLUtils.getXMLHeader();
				xmlStr += XMLUtils.getStartNode("ItemDetails");
				for (Map<String, String> map : resultV) {
					xmlStr += XMLUtils.getXMLNode("status", "0");
					xmlStr += XMLUtils.getXMLNode("description", "");
					xmlStr += XMLUtils.getXMLNode("FROM_LOC", (String) map
							.get("LOC"));
					xmlStr += XMLUtils.getXMLNode("TO_LOC", (String) map
							.get("LOC1"));

				}
				xmlStr += XMLUtils.getEndNode("ItemDetails");

			} else {
				xmlStr = XMLUtils.getXMLMessage(1, "Not a valid batch ");
			}

		}

		catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			xmlStr = XMLUtils.getXMLMessage(1, e.getMessage());
		}

		return xmlStr;
	}
        
      
	private String loadLoanOrderItemDetails(HttpServletRequest request,
			HttpServletResponse response) {
		// MLogger.log(0, "loadLoanOrderItemDetails() Starts ");
		String str = "";
		try {
			String plant = StrUtils.fString(request.getParameter("PLANT"));
			String orderNum = StrUtils.fString(request
					.getParameter("ORDER_NUM"));

			str = this.loanUtil.getLoanOrderItemDetailsPda(plant, orderNum,
					Boolean.FALSE);
			if (str.equalsIgnoreCase("")) {
				str = XMLUtils.getXMLMessage(1, "No Loan Orders Found");
			}
		}

		catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			str = XMLUtils.getXMLMessage(1, e.getMessage());
		}

		// MLogger.log(0, "loadLoanOrderItemDetails() Ends");
		return str;
	}

	private String loadLoanOrders(HttpServletRequest request,
			HttpServletResponse response) {
		String str = "";
		try {
			// MLogger.log(0, "loadLoanOrders() Starts ");
			String plant = StrUtils.fString(request.getParameter("PLANT"));

			Hashtable<String, String> ht = new Hashtable<String, String>();
			ht.put(IConstants.PLANT, plant);

			str = this.loanUtil.getLoanOrders(plant, Boolean.TRUE);

			if (str.equalsIgnoreCase("")) {
				str = XMLUtils.getXMLMessage(1, "No Outbound Orders Found");
			}

		}

		catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			str = XMLUtils.getXMLMessage(1, e.getMessage());
		}
		// MLogger.log(0, "loadLoanOrders() Ends");
		return str;
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

	public void destroy() {
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
