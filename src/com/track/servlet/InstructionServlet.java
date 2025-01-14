package com.track.servlet;

import java.io.IOException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.UserTransaction;

import com.track.constants.IConstants;
import com.track.constants.IDBConstants;
import com.track.constants.MLoggerConstant;
import com.track.db.util.InstructionUtil;
import com.track.db.util.POUtil;
import com.track.gates.DbBean;
import com.track.util.DateUtils;
import com.track.util.IMLogger;
import com.track.util.MLogger;
import com.track.util.StrUtils;

/**
 * Servlet implementation class Instructions
 */
public class InstructionServlet extends HttpServlet implements IMLogger {
	private static final long serialVersionUID = 1L;
	private static final String CONTENT_TYPE = "text/html; charset=windows-1252";
	private boolean printLog = MLoggerConstant.PurchaseOrderServlet_PRINTPLANTMASTERLOG;
	private boolean printInfo = MLoggerConstant.PurchaseOrderServlet_PRINTPLANTMASTERINFO;
	String action = "";
	private InstructionUtil _insUtil;
	StrUtils _StrUtils = null;
	DateUtils dateutils = null;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public InstructionServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	public void init() {
		_insUtil = new InstructionUtil();
		dateutils = new DateUtils();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		boolean flag = false;
		StringBuffer Step1, Step2, Step3, Step4, Step5;
		String result = "";
		UserTransaction ut = null;
		try {
			action = _StrUtils.fString(request.getParameter("Submit")).trim();
			String plant = StrUtils.fString(
					(String) request.getSession().getAttribute("PLANT")).trim();
			String userName = StrUtils.fString(
					(String) request.getSession().getAttribute("LOGIN_USER"))
					.trim();
			if (action.equalsIgnoreCase("CREATE_INSTRN")) {
				Step1 = new StringBuffer(request.getParameter("Step1").trim());

				Step2 = new StringBuffer(request.getParameter("Step2").trim());
				Step3 = new StringBuffer(request.getParameter("Step3").trim());
				Step4 = new StringBuffer(request.getParameter("Step4").trim());
				Step5 = new StringBuffer(request.getParameter("Step5").trim());
				Hashtable insupdate = new Hashtable();
				insupdate.put(IDBConstants.UPDATED_AT, dateutils.getDateTime());
				insupdate.put(IDBConstants.STEP1, Step1.toString());
				insupdate.put(IDBConstants.STEP2, Step2.toString());
				insupdate.put(IDBConstants.STEP3, Step3.toString());
				insupdate.put(IDBConstants.STEP4, Step4.toString());
				insupdate.put(IDBConstants.STEP5, Step5.toString());
				insupdate.put(IConstants.UPDATED_BY, userName);
				Hashtable inscondtn = new Hashtable();
				inscondtn.put(IConstants.PLANT, plant);
				boolean resflag = _insUtil.isExistIns(inscondtn, "");
				ut = com.track.gates.DbBean.getUserTranaction();
				ut.begin();
				if (resflag){
					flag = _insUtil.updateMst(insupdate, inscondtn);}
				else
				{
					insupdate.put(IConstants.PLANT, plant);
					flag = _insUtil.insertMst(insupdate);
				}	
				if (flag) {
					DbBean.CommitTran(ut);
					result = "<font color=\"green\"> Instructions updated successfully</font>";

					response.sendRedirect("jsp/instructions.jsp?result="
							+ result);

				} else {
					DbBean.RollbackTran(ut);
					result = "<font color=\"red\"> Failed to edit Instructions </font>";
					response.sendRedirect("jsp/instructions.jsp?result="
							+ result);
				}

			}

		} catch (Exception ex) {
			DbBean.RollbackTran(ut);
			this.mLogger.exception(this.printLog, "", ex);
			result = "<font class = " + IConstants.FAILED_COLOR + ">Error : "
					+ ex.getMessage() + "</font>";
			response.sendRedirect("jsp/instructions.jsp?result=" + result);
		}

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
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
			e.printStackTrace();
		}

	}

	public HashMap<String, String> populateMapData(String companyCode,
			String userCode) {
		HashMap<String, String> loggerDetailsHasMap = new HashMap<String, String>();
		loggerDetailsHasMap.put(MLogger.COMPANY_CODE, companyCode);
		loggerDetailsHasMap.put(MLogger.USER_CODE, userCode);
		return loggerDetailsHasMap;

	}
}
