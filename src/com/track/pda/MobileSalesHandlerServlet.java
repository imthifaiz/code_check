package com.track.pda;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;


import com.track.constants.IConstants;
import com.track.constants.IDBConstants;
import com.track.constants.MLoggerConstant;
import com.track.dao.CustomerBeanDAO;
import com.track.db.util.CurrencyUtil;
import com.track.db.util.HTReportUtil;
import com.track.db.util.UserLocUtil;
import com.track.db.util.DOUtil;
import com.track.gates.DbBean;
import com.track.util.DateUtils;
import com.track.util.IMLogger;
import com.track.util.MLogger;
import com.track.util.StrUtils;

/**
 * Servlet implementation class MobileSalesHandlerServlet
 */
public class MobileSalesHandlerServlet extends HttpServlet implements IMLogger{
	private boolean printLog = MLoggerConstant.MobileSalesHandlerServlet_PRINTPLANTMASTERLOG;
	private boolean printInfo = MLoggerConstant.MobileSalesHandlerServlet_PRINTPLANTMASTERINFO;
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MobileSalesHandlerServlet() {
        super();
        // TODO Auto-generated constructor stub
    }
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		JSONObject jsonObjectResult = new JSONObject();
		String action = StrUtils.fString(request.getParameter("ACTION")).trim();
		String plant = StrUtils.fString(
				(String) request.getSession().getAttribute("PLANT")).trim();
		String userName = StrUtils.fString(
				(String) request.getSession().getAttribute("LOGIN_USER"))
				.trim();
		try {
			if (action.equals("process_mobilesales_order")) {
				jsonObjectResult = this.process_mobilesales_order(request);
			}
			if (action.equals("process_mobilesales_Edit_AddProducts")) {
				jsonObjectResult = this.process_mobilesales_Edit_AddProducts(request);
			}
		} catch (Exception daRE) {
			JSONObject statusJsonObject = new JSONObject();
			statusJsonObject.put("statusCode", "99");
			statusJsonObject.put("statusMessage", daRE.toString());
			jsonObjectResult.put("status", statusJsonObject);
		}
		this.mLogger.info(this.printInfo, "[JSON OUTPUT] " + jsonObjectResult);
		response.setContentType("application/json");
		// response.setCharacterEncoding("UTF-8");
		response.getWriter().write(jsonObjectResult.toString());
		response.getWriter().flush();
		response.getWriter().close();
	}
	
	private JSONObject process_mobilesales_order(HttpServletRequest request) {
		JSONObject resultJson = new JSONObject();
		try {
			JSONObject resultJsonObject = new JSONObject();
			Boolean result = doprocessmobilesalesorder(request);
			JSONObject statusJsonObject = new JSONObject();
			if (result) {

				statusJsonObject.put("statusCode", "100");
				statusJsonObject.put("statusMessage",
						"Outbound Order Created Succesfully!");

			} else {
				statusJsonObject.put("statusCode", "99");
				statusJsonObject.put("statusMessage",
						"Unable to process mobile sales order!");
				resultJson.put("status", statusJsonObject);
			}

			resultJson.put("result", resultJsonObject);
			resultJson.put("status", statusJsonObject);

			
		} catch (Exception daRE) {
			JSONObject statusJsonObject = new JSONObject();
			statusJsonObject.put("statusCode", "99");
			statusJsonObject.put("statusMessage", daRE.getMessage());
			resultJson.put("status", statusJsonObject);
		}

		return resultJson;
	}
	
	private JSONObject process_mobilesales_Edit_AddProducts(HttpServletRequest request) {
		JSONObject resultJson = new JSONObject();
		try {
			JSONObject resultJsonObject = new JSONObject();
			Boolean result = doprocessmobilesaleseditaddproducts(request);
			JSONObject statusJsonObject = new JSONObject();
			if (result) {

				statusJsonObject.put("statusCode", "100");
				statusJsonObject.put("statusMessage",
						"Outbound Order Created Succesfully!");

			} else {
				statusJsonObject.put("statusCode", "99");
				statusJsonObject.put("statusMessage",
						"Unable to process mobile sales order!");
				resultJson.put("status", statusJsonObject);
			}

			resultJson.put("result", resultJsonObject);
			resultJson.put("status", statusJsonObject);

		} catch (Exception daRE) {
			JSONObject statusJsonObject = new JSONObject();
			statusJsonObject.put("statusCode", "99");
			statusJsonObject.put("statusMessage", daRE.getMessage());
			resultJson.put("status", statusJsonObject);
		}

		return resultJson;
	}
	
	
	private Boolean doprocessmobilesalesorder(HttpServletRequest request)
			throws Exception {
		try {
			//DOHder Part
			String display="",currencyid="",collectionDate="",cust_code="",cust_name="",plant="",strName="",strAddr1,strAddr2,strAddr3,strRemarks="";
			CustomerBeanDAO customerBeanDAO = new CustomerBeanDAO();
			CurrencyUtil curUtil = new CurrencyUtil();
			Hashtable<String, String> requestData = new Hashtable<String, String>();
			requestData.clear();
			requestData.put(IConstants.PLANT, StrUtils.fString(request.getParameter("PLANT")).trim());
			requestData.put("PROCESSING_MODE", StrUtils.fString(request.getParameter("PROCESSING_MODE")).trim());
			requestData.put(IConstants.LOGIN_USER, StrUtils.fString(request.getParameter("LOGIN_USER")).trim());
			requestData.put(IConstants.DONO, StrUtils.fString(	request.getParameter("DONO")).trim());
			//order date
			collectionDate=new DateUtils().parseDate(StrUtils.fString(request.getParameter("ORDER_DATE")).trim());
			if(collectionDate.length()==0){collectionDate = new DateUtils().getDate();}
			requestData.put(IConstants.ORDERDATE, collectionDate);
			
			requestData.put(IConstants.CUSTOMER_CODE, StrUtils.fString(request.getParameter("CUST_CODE")).trim());
			StrUtils strUtils = new StrUtils();
			cust_name = strUtils.InsertQuotes(strUtils.replaceCharacters2Recv(strUtils.fString(request.getParameter("CUST_NAME"))));
			requestData.put(IConstants.CUSTOMER_NAME, cust_name);
			
			//to get customer details
			plant=StrUtils.fString(request.getParameter("PLANT")).trim();
			cust_code=StrUtils.fString(request.getParameter("CUST_CODE")).trim();
			ArrayList arrCust = customerBeanDAO.getCustomerDetails(cust_code,plant);
			
			strName=(String)arrCust.get(1);
			strAddr1=(String)arrCust.get(2);
			strAddr2=(String)arrCust.get(3);
			strAddr3=(String)arrCust.get(4);
			strRemarks=(String)arrCust.get(16);
								
			requestData.put("PERSONINCHARGE", strName);
			requestData.put("ADDRESS", strAddr1); 
			requestData.put("ADDRESS2", strAddr2);
			requestData.put("ADDRESS3", strAddr3);
			requestData.put("REMARK2", strRemarks);
			
			requestData.put(IConstants.JOB_NUM, StrUtils.fString(request.getParameter("JOB_NUM")).trim());
			requestData.put(IConstants.ORDERTYPE , StrUtils.fString(request.getParameter("ORDER_TYPE")).trim());
			requestData.put("OUTBOUND_GST" , StrUtils.fString(request.getParameter("GST")).trim());
			//to get currencyid 
			Hashtable curHash =new Hashtable();
			display=StrUtils.fString(request.getParameter("CURRENCY_DISPLAY").trim());
			curHash.put(IConstants.PLANT, StrUtils.fString(request.getParameter("PLANT")).trim());
			curHash.put(IConstants.DISPLAY, display);
			if(display!=null&&display!="")
			{
				currencyid = curUtil.getCurrencyID(curHash,"CURRENCYID");
				if(currencyid == null || currencyid.equals("")){
					throw new Exception("Currency ID " +curHash + " not found please check More Details!");
				}
				requestData.put(IConstants.CURRENCYID, currencyid);
				
			}
			
			requestData.put(IConstants.REMARK , StrUtils.fString(request.getParameter("HDR_REMARK")).trim());
			requestData.put("STATUS" , StrUtils.fString(request.getParameter("STATUS_ID")).trim());
			requestData.put("SCUST_NAME" , StrUtils.fString(request.getParameter("SCUST_NAME")).trim());
			requestData.put("SCONTACT_NAME" , StrUtils.fString(request.getParameter("SCONTACT_NAME")).trim());
			requestData.put("SADDR1" , StrUtils.fString(request.getParameter("SADDR1")).trim());
			requestData.put("SADDR2" , StrUtils.fString(request.getParameter("SADDR2")).trim());
			requestData.put("SCITY" , StrUtils.fString(request.getParameter("SCITY")).trim());
			requestData.put("SCOUNTRY" , StrUtils.fString(request.getParameter("SCOUNTRY")).trim());
			requestData.put("SZIP" , StrUtils.fString(request.getParameter("SZIP")).trim());
			requestData.put("STELNO" , StrUtils.fString(request.getParameter("STELNO")).trim());
			requestData.put("ITEM_COUNTS", StrUtils.fString(request.getParameter("PRODUCT_LIST_SIZE")).trim());
			Integer itemCount = new Integer(StrUtils.fString(request.getParameter("PRODUCT_LIST_SIZE")).trim());
			
			//DODet part
			for (int i = 0; i < itemCount; i++) {
		
				//requestData.put("LNNO_" + i, StrUtils.fString(request.getParameter("LINNO_" + i)).trim());
				requestData.put("ITEM_" + i, StrUtils.fString(request.getParameter("PRODUCT_ID_" + i)).trim());
				requestData.put("ITEM_DESC_" + i, StrUtils.fString(request.getParameter("PRODUCT_DESC_" + i)).trim());
				requestData.put("ITEM_DETAIL_DESC" + i, StrUtils.fString(request.getParameter("PRODUCT_DETAIL_DESC_" + i)).trim());
				requestData.put("QTY_ORDER_" + i, StrUtils.fString(request.getParameter("QTY_" + i)).trim());
				requestData.put("UNITMO_"+ i, StrUtils.fString(request.getParameter("UOM_" + i)).trim());
				requestData.put("UNITPRICE_" + i, StrUtils.fString(request.getParameter("UNITPRICE_" + i)).trim());
				requestData.put("DETREMARK_"+ i, StrUtils.fString(request.getParameter("DET_REMARK_" + i)).trim());
			}
		
			DOUtil _DOUtil = new DOUtil();
			return _DOUtil.process_mobile_sales(requestData);
		
		} catch (Exception e) {
			throw e;
		}
		}

	 private Boolean doprocessmobilesaleseditaddproducts(HttpServletRequest request)
			throws Exception {
		try {
			//DOHder Part
			String display="",currencyid="",collectionDate="",cust_code="",cust_name="",plant="",strName="",strAddr1,strAddr2,strAddr3,strRemarks="";
			CustomerBeanDAO customerBeanDAO = new CustomerBeanDAO();
			CurrencyUtil curUtil = new CurrencyUtil();
			Hashtable<String, String> requestData = new Hashtable<String, String>();
			requestData.clear();
			requestData.put(IConstants.PLANT, StrUtils.fString(request.getParameter("PLANT")).trim());
			requestData.put("PROCESSING_MODE", StrUtils.fString(request.getParameter("PROCESSING_MODE")).trim());
			requestData.put(IConstants.LOGIN_USER, StrUtils.fString(request.getParameter("LOGIN_USER")).trim());
			requestData.put(IConstants.DONO, StrUtils.fString(	request.getParameter("DONO")).trim());
			
			requestData.put(IConstants.CUSTOMER_CODE, StrUtils.fString(request.getParameter("CUST_CODE")).trim());
			StrUtils strUtils = new StrUtils();
			cust_name = strUtils.InsertQuotes(strUtils.replaceCharacters2Recv(strUtils.fString(request.getParameter("CUST_NAME"))));
			requestData.put(IConstants.CUSTOMER_NAME, cust_name);
			requestData.put("ITEM_COUNTS", StrUtils.fString(request.getParameter("PRODUCT_LIST_SIZE")).trim());
			Integer itemCount = new Integer(StrUtils.fString(request.getParameter("PRODUCT_LIST_SIZE")).trim());
			
			//DODet part
			for (int i = 0; i < itemCount; i++) {
		
				//requestData.put("LNNO_" + i, StrUtils.fString(request.getParameter("LINNO_" + i)).trim());
				requestData.put("ITEM_" + i, StrUtils.fString(request.getParameter("PRODUCT_ID_" + i)).trim());
				requestData.put("ITEM_DESC_" + i, StrUtils.fString(request.getParameter("PRODUCT_DESC_" + i)).trim());
				requestData.put("ITEM_DETAIL_DESC" + i, StrUtils.fString(request.getParameter("PRODUCT_DETAIL_DESC_" + i)).trim());
				requestData.put("QTY_ORDER_" + i, StrUtils.fString(request.getParameter("QTY_" + i)).trim());
				requestData.put("UNITMO_"+ i, StrUtils.fString(request.getParameter("UOM_" + i)).trim());
				requestData.put("UNITPRICE_" + i, StrUtils.fString(request.getParameter("UNITPRICE_" + i)).trim());
				requestData.put("DETREMARK_"+ i, StrUtils.fString(request.getParameter("DET_REMARK_" + i)).trim());
			}
		
			DOUtil _DOUtil = new DOUtil();
			return _DOUtil.process_mobile_sales(requestData);
		
		} catch (Exception e) {
			throw e;
		}
		}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
