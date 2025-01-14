package com.track.servlet;

import java.io.IOException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;

import com.track.constants.IConstants;
import com.track.constants.IDBConstants;
import com.track.constants.MLoggerConstant;
import com.track.constants.TransactionConstants;
import com.track.dao.CustomerBeanDAO;
import com.track.dao.InvMstDAO;
import com.track.dao.MovHisDAO;
import com.track.db.util.CustUtil;
import com.track.db.util.LocUtil;
import com.track.gates.sqlBean;
import com.track.util.DateUtils;
import com.track.util.IMLogger;
import com.track.util.MLogger;
import com.track.util.StrUtils;

/**
 * Servlet implementation class CustomerStatusServlet
 */
public class CustomerStatusServlet  extends HttpServlet implements IMLogger {
	private static final String CONTENT_TYPE = "text/html; charset=windows-1252";
	private boolean printLog = MLoggerConstant.CustomerStatusServlet_PRINTPLANTMASTERLOG;
	private boolean printInfo = MLoggerConstant.CustomerStatusServlet_PRINTPLANTMASTERINFO;
	String action = "";
	DateUtils dateutils = new DateUtils();
	CustUtil _CustUtil = null;

       
  
    public void init(ServletConfig config) throws ServletException {
		super.init(config);
		 _CustUtil = new  CustUtil();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	try {
		action = request.getParameter("action").trim();
		String plant = StrUtils.fString(
				(String) request.getSession().getAttribute("PLANT")).trim();
		String userName = StrUtils.fString(
				(String) request.getSession().getAttribute("LOGIN_USER"))
				.trim();
		this.setMapDataToLogger(this.populateMapData(plant, userName));
		_CustUtil.setmLogger(mLogger);
		if (this.printInfo) {
			diaplayInfoLogs(request);
		}
		if (action.equalsIgnoreCase("New")) {
			response.sendRedirect("jsp/createCustomerStatus.jsp?action=NEW");
		}

		else if (action.equalsIgnoreCase("ADD")) {
			String result = addCustomerStatus(request, response);
			response.sendRedirect("jsp/createCustomerStatus.jsp?action=SHOW_RESULT&result="+ result);

		} else if (action.equalsIgnoreCase("UPDATE")) {

			String result = updateCustomerStatus(request, response);
			response.sendRedirect("jsp/maintCustomerStatus.jsp?action=SHOW_RESULT&result="+ result);
		}

		else if (action.equalsIgnoreCase("DELETE")) {

			String result =DeleteCustomerStatus(request, response);
			response.sendRedirect("jsp/maintCustomerStatus.jsp?action=SHOW_RESULT&result="
							+ result);
		}
	} catch (Exception ex) {
		this.mLogger.exception(this.printLog, "", ex);
	       String result = "<font class = " + IConstants.FAILED_COLOR + ">Exception : " + ex.getMessage() + "</font>";
	       request.getSession().setAttribute("errResult", result);
		response.sendRedirect("jsp/createCustomerStatus.jsp?action=SHOW_RESULT_VALUE");
	}


	}
	
	private String addCustomerStatus(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException,
			Exception {
		String result = "";
		try {
			HttpSession session = request.getSession();
			String plant = (String) session.getAttribute("PLANT");
			String user = (String) session.getAttribute("LOGIN_USER");
			String customerstatusid = request.getParameter("CUSTOMER_STATUS_ID").trim();
			String desc = request.getParameter("DESC").trim();
			String remarks = request.getParameter("REMARKS").trim();
			if(customerstatusid.length()>0){
                        String specialcharsnotAllowed = StrUtils.isValidAlphaNumeric(customerstatusid);
                        if(specialcharsnotAllowed.length()>0){
                        throw new Exception("Customer Status Id value : '" + customerstatusid + "' has special characters "+specialcharsnotAllowed+" that are  not allowed .");
                }
             }
			Hashtable ht = new Hashtable();
			ht.put("PLANT", plant);
			ht.put("CUSTOMER_STATUS_ID", customerstatusid);
			ht.put("CUSTOMER_STATUS_DESC", desc);
			ht.put("REMARKS", remarks);
			ht.put("CRBY", user);
			ht.put("CRAT", dateutils.getDateTime());
			ht.put("ISACTIVE", "Y");
			MovHisDAO mdao = new MovHisDAO(plant);
			mdao.setmLogger(mLogger);
			Hashtable htm = new Hashtable();
			htm.put("PLANT", plant);
			//htm.put("DIRTYPE", TransactionConstants.ADD_CUSTOMER_STATUS);
			htm.put("RECID", "");
			htm.put("ITEM",customerstatusid);
			htm.put("CRBY", user);
			htm.put("REMARKS", remarks);
			htm.put("CRAT", dateutils.getDateTime());
			htm.put(IDBConstants.TRAN_DATE, dateutils.getDateinyyyy_mm_dd(dateutils.getDate()));
			boolean flag = _CustUtil.addCustomerStatus(ht);
			boolean inserted = mdao.insertIntoMovHis(htm);
			if (flag && inserted) {
				request.getSession().setAttribute("customerStatusData", ht);
				result = "<font class = " + IConstants.SUCCESS_COLOR+ ">CustomerStatus: " + customerstatusid + " Created Successfully</font>";

			} else {
				result = "<font class = " + IConstants.FAILED_COLOR	+ "> Customer Status Id:"+ customerstatusid +" Exists Already</font>";
				
			}

		} catch (Exception e) {
			throw e;
		}

		return result;
	}
	private String updateCustomerStatus(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException,
			Exception {

		String result = "";
		HttpSession session = request.getSession();
		String plant = (String) session.getAttribute("PLANT");
		String user = (String) session.getAttribute("LOGIN_USER");
		Hashtable htCondition = new Hashtable();

		try {

			String customerstatusid = request.getParameter("CUSTOMER_STATUS_ID").trim();
			String desc = request.getParameter("DESC").trim();
			String remarks = request.getParameter("REMARKS").trim();
			String isActive = request.getParameter("ACTIVE").trim();
			Hashtable ht = new Hashtable();
			ht.put("PLANT", plant);
			ht.put("CUSTOMER_STATUS_ID", customerstatusid);
			ht.put("CUSTOMER_STATUS_DESC", desc);
			ht.put("REMARKS", remarks);
			ht.put("ISACTIVE", isActive);
			ht.put("UPAT", dateutils.getDateTime());
			ht.put("UPBY", user);
			htCondition.put("CUSTOMER_STATUS_ID", customerstatusid);
			htCondition.put("PLANT", plant);
					

			MovHisDAO mdao = new MovHisDAO(plant);
			mdao.setmLogger(mLogger);
			Hashtable htm = new Hashtable();
			htm.put("PLANT", plant);
			//htm.put("DIRTYPE", TransactionConstants.UPDATE_CUSTOMER_STATUS);
			htm.put("RECID", "");
			htm.put("ITEM",customerstatusid);
			htm.put("UPBY", user);
			htm.put("REMARKS", remarks);
			htm.put("CRBY", user);
			htm.put("CRAT", dateutils.getDateTime());
			htm.put("UPAT", dateutils.getDateTime());
			htm.put(IDBConstants.TRAN_DATE, dateutils.getDateinyyyy_mm_dd(dateutils.getDate()));
			if(_CustUtil.isValidCustomerStatusInmst(plant, customerstatusid)){
			boolean flag = _CustUtil.updateCustomerStatus(ht, htCondition, " ");
			boolean inserted = mdao.insertIntoMovHis(htm);
			if (flag && inserted) {

				request.getSession().setAttribute("customerStatusMstData", ht);
				result = "<font class = " + IConstants.SUCCESS_COLOR
						+ ">CustomerStatus:" + customerstatusid + " updated Successfully</font>";

			} else {
				throw new Exception("Unable to update CustomerStatus ");
			}
			}
			else{
				request.getSession().setAttribute("customerStatusMstData", ht);
				result = "<font class = " + IConstants.FAILED_COLOR
						+ ">location ID:" +  customerstatusid + " doesn't not Exists. Try again</font>";
				
			}

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		}

		return result;
	}

	private String DeleteCustomerStatus(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException,
			Exception {

		String result = "", locId = "", locDesc = "", remarks = "";
		HttpSession session = request.getSession();
		String plant = (String) session.getAttribute("PLANT");
		String user = (String) session.getAttribute("LOGIN_USER");
		Hashtable htValues = new Hashtable();
		Hashtable htCondition = new Hashtable();
		CustomerBeanDAO _CustomerBeanDAO = new CustomerBeanDAO();
		sqlBean sqlbn= new sqlBean();
		boolean wiplocflag=false;

		try {
			String customerstatusid = request.getParameter("CUSTOMER_STATUS_ID").trim();
			Hashtable ht = new Hashtable();
			htCondition.put("CUSTOMER_STATUS_ID", customerstatusid);
			htCondition.put("PLANT", plant);
			boolean isExitflag  = _CustomerBeanDAO.isExisitCustomer(htCondition,"");
            if (isExitflag) {
				result = "<font class = " + IDBConstants.FAILED_COLOR
						+ " >Customer Status Exists In Customer Master</font>";
			}
	     	else{
			boolean flag = _CustUtil.deleteCustomerStatus(customerstatusid, plant);
    		MovHisDAO mdao = new MovHisDAO(plant);
			mdao.setmLogger(mLogger);
			Hashtable htm = new Hashtable();
			htm.put("PLANT", plant);
			//htm.put("DIRTYPE", TransactionConstants.DEL_CUSTOMER_STATUS);
			htm.put("RECID", "");
			htm.put("ITEM",locId);
			htm.put("UPBY", user);
			htm.put("REMARKS", remarks);
			htm.put("CRBY", user);
			htm.put("CRAT", dateutils.getDateTime());
			htm.put("UPAT", dateutils.getDateTime());
			htm.put(IDBConstants.TRAN_DATE, dateutils.getDateinyyyy_mm_dd(dateutils.getDate()));
			
			flag = mdao.insertIntoMovHis(htm);

			if (flag) {
				ht = new Hashtable();
				ht.put("PLANT", plant);
				ht.put("CUSTOMER_STATUS_ID", "");
				

				request.getSession().setAttribute("customerStatusMstData", ht);
				result = "<font class = " + IConstants.SUCCESS_COLOR
						+ ">CustomerStatus Deleted Successfully</font>&"
						+ "action = show_result";

			} else {
				result = "<font class = " + IConstants.FAILED_COLOR
						+ ">Unable to delete Customer Status</font>";
				
			}
			}

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		}

		return result;
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
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
