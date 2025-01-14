package com.track.servlet;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.track.constants.MLoggerConstant;
import com.track.gates.DbBean;
//import com.track.sync.SyncDO1;
//import com.track.sync.SyncPO;
import com.track.util.IMLogger;
import com.track.util.MLogger;
import com.track.util.StrUtils;

public class SyncServlet extends HttpServlet implements Runnable, IMLogger {
	public SyncServlet() {
	}

	private boolean printLog = MLoggerConstant.SyncServlet_PRINTPLANTMASTERLOG;
	private boolean printInfo = MLoggerConstant.SyncServlet_PRINTPLANTMASTERINFO;
	private static Thread emailThread = null;
	private HttpServletRequest req;
	private HttpServletResponse res;
	public int count = 0;

	private static final boolean DEBUG = true;
	//SyncDO1 objSyncDO1 = new SyncDO1();
	//SyncPO objSyncPO = new SyncPO();

	public void init() {

	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String reDirect = "/wms/jsp/bpcs_transaction_started.jsp";
		String resDest = "/wms/jsp/bpcs_transaction_stopped.jsp";

		try {
			String action = request.getParameter("action");
			String plant = StrUtils.fString(
					(String) request.getSession().getAttribute("PLANT")).trim();
			String userName = StrUtils.fString(
					(String) request.getSession().getAttribute("LOGIN_USER"))
					.trim();
			this.setMapDataToLogger(this.populateMapData(plant, userName));
			if (this.printInfo) {
				diaplayInfoLogs(request);
			}
			if (action.equals("start") == true) {
				start();

				response.sendRedirect(reDirect);
			}

			if (action.equals("stop") == true) {
				stop();

				response.sendRedirect(resDest);
				return;
			}
		} catch (Exception e) {
			// response.sendRedirect(reDirect); return;
		}
	}

	/**
	 * run method internally called by the start method
	 */

	public void run() {
		Thread myThread = Thread.currentThread();

		do {
			try {
				performTask(req, res);
				long sleep = Long.parseLong(DbBean.THREAD_SLEEPING);
				try {
					Thread.sleep(sleep);
				} catch (InterruptedException e) {
				}
			} catch (Exception e) {
				this.mLogger.exception(this.printLog, "", e);
			}
		} while (emailThread == myThread);

	}

	public void start() {

		if (emailThread == null) {
			emailThread = new Thread(this, "Transaction");
			emailThread.start();
		}
	}

	public void stop() {

		emailThread = null;

	}

	/**
	 * method performTask this will call every 1 second.
	 * 
	 * @param request
	 * @param response
	 * @throws IOException
	 * @throws ServletException
	 */
	public void performTask(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {
		try {

			//objSyncDO1.syncDOProcess();
			//objSyncPO.syncPOProcess();
			//objSyncDO1.syncDOProcess1();

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		}
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
