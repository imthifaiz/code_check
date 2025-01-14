package com.track.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.track.constants.IConstants;
import com.track.constants.IDBConstants;
import com.track.constants.MLoggerConstant;
import com.track.constants.TransactionConstants;
import com.track.dao.MovHisDAO;
import com.track.dao.TblControlDAO;
import com.track.dao.TransportModeDAO;
import com.track.db.util.TransportModeUtil;
import com.track.util.DateUtils;
import com.track.util.MLogger;
import com.track.util.StrUtils;

/**
 * Servlet implementation class TransportModeServlet
 */
@WebServlet("/transportmode/*")
@SuppressWarnings({"rawtypes", "unchecked"})
public class TransportModeServlet extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private boolean printLog = MLoggerConstant.TRANSPORTMODEServlet_PRINTPLANTMASTERLOG;
	private boolean printInfo = MLoggerConstant.TRANSPORTMODERServlet_PRINTPLANTMASTERINFO;
	String action = "";
	TblControlDAO _TblControlDAO = null;
	TransportModeDAO transportModeDAO = null;
	TransportModeUtil transportModeUtil = null;

	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		 _TblControlDAO = new TblControlDAO();
		 transportModeDAO = new TransportModeDAO();
		 transportModeUtil = new TransportModeUtil();
	}

       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public TransportModeServlet() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			HttpSession session = request.getSession();
			String[] pathInfo = request.getRequestURI().split("/");
			String action = pathInfo[3];
			String plant = StrUtils.fString(
					(String) request.getSession().getAttribute("PLANT")).trim();
			String userName = StrUtils.fString(
					(String) request.getSession().getAttribute("LOGIN_USER"))
					.trim();
			String tranID = null, tranDesc = null;
			this.setMapDataToLogger(this.populateMapData(plant, userName));
			if (this.printInfo) {
				diaplayInfoLogs(request);
			}
			if (action.equalsIgnoreCase("Auto_ID")) {
				tranID = this.generateTransportModeID(plant, userName);
				request.getRequestDispatcher("../jsp/transportModeNew.jsp?tranID="
						+ tranID).forward(request, response);
				return;

			}else if (action.equalsIgnoreCase("NEW")) {
				tranID = this.generateTransportModeID(plant, userName);
				request.getRequestDispatcher("../jsp/transportModeNew.jsp?tranID="
						+ tranID).forward(request, response);
				return;

			}else if (action.equalsIgnoreCase("ADD")) {
				tranID = request.getParameter("TRANSPORT_MODE_ID");
				tranDesc = request.getParameter("TRANSPORT_MODE_DESC");
				String responseMsg = this.addTransportMode(plant, userName,
						tranID, tranDesc);
				request.getRequestDispatcher("../jsp/transportModeSummary.jsp?response="
						+ responseMsg).forward(request, response);
				return;

			}else if (action.equalsIgnoreCase("EDIT")) {
				tranID = request.getParameter("tranID");
				tranDesc = request.getParameter("tranModeDesc");
				request.getRequestDispatcher("../jsp/transportModeEdit.jsp?tranID="+tranID+"&tranModeDesc="+tranDesc).forward(request, response);
				return;

			}else if(action.equalsIgnoreCase("UPDATE")){
				tranID = request.getParameter("TRANSPORT_MODE_ID");
				tranDesc = request.getParameter("TRANSPORT_MODE_DESC");
				String responseMsg = this.updateTransportMode(plant, userName,
						tranID, tranDesc);
				request.getRequestDispatcher("../jsp/transportModeSummary.jsp?response="
						+ responseMsg).forward(request, response);
				return;
				
			}else if(action.equalsIgnoreCase("DELETE")){
				tranID = request.getParameter("TRANSPORT_MODE_ID");
				tranDesc = request.getParameter("TRANSPORT_MODE_DESC");
				String responseMsg = this.deleteTrasnportMode(plant, userName,
						tranID, tranDesc);
				request.getRequestDispatcher("../jsp/transportModeSummary.jsp?response="
						+ responseMsg).forward(request, response);
				
			}else if(action.equalsIgnoreCase("SUMMARY")){
				tranID = StrUtils.fString(request.getParameter("TRAN_ID"));
				String responseMsg = "";
				ArrayList<Map<String, String>> transportModeList = this.getTranModeList(plant,tranID);
				if(transportModeList.size()==0){
					responseMsg = "No data Found";
				}
				session.setAttribute("transportModeList", transportModeList);
				request.getRequestDispatcher("../jsp/transportModeSummary.jsp?response="
						+ responseMsg+"&brandID="+tranID).forward(request, response);
				return;
			}else if(action.equalsIgnoreCase("LIST")){
				request.getRequestDispatcher("../jsp/transportModeList.jsp").forward(request, response);
				return;
			}
		} catch (Exception ex) {
			this.mLogger.exception(this.printLog, "", ex);
			String result = "<font class = " + IConstants.FAILED_COLOR
					+ ">Exception : " + ex.getMessage() + "</font>";
			request.getRequestDispatcher("../jsp/transportModeSummary.jsp?response="
					+ result).forward(request, response);
			return;
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

	
	//Generate the next sequence Brand ID for the product -Bruhan.//11/9/12
	private String generateTransportModeID(String plant, String userName)
			throws ServletException, IOException, Exception {
		String tranID = "", sBatchSeq = "", sZero = "";
		boolean exitFlag = false;
		try {
			Hashtable<String, String> ht = new Hashtable<String, String>();
			String query = " isnull(NXTSEQ,'') as NXTSEQ";
			ht.put(IDBConstants.PLANT, plant);
			ht.put(IDBConstants.TBL_FUNCTION, "TRANSPORTMODE");
			exitFlag = _TblControlDAO.isExisit(ht, "", plant);
			if (exitFlag == false) {
				Hashtable<String, String> htTblCntInsert = new Hashtable<String, String>();

				htTblCntInsert.put(IDBConstants.PLANT, plant);

				htTblCntInsert.put(IDBConstants.TBL_FUNCTION, "TRANSPORTMODE");
				htTblCntInsert.put(IDBConstants.TBL_PREFIX1, "TM");
				htTblCntInsert.put(IDBConstants.TBL_MINSEQ, "000");
				htTblCntInsert.put(IDBConstants.TBL_MAXSEQ, "999");
				htTblCntInsert.put(IDBConstants.TBL_NEXT_SEQ,
						(String) IDBConstants.TBL_FIRST_NEX_SEQ);
				htTblCntInsert.put(IDBConstants.CREATED_BY, userName);
				htTblCntInsert.put(IDBConstants.CREATED_AT, DateUtils
						.getDateTime());
				_TblControlDAO.insertTblControl(htTblCntInsert,
						plant);

				tranID = "TM" + "001";
			} else {
				Map m = _TblControlDAO.selectRow(query, ht, "");
				sBatchSeq = (String) m.get("NXTSEQ");

				int inxtSeq = Integer.parseInt(((String) sBatchSeq.trim()
						.toString())) + 1;

				String updatedSeq = Integer.toString(inxtSeq);
				if (updatedSeq.length() == 1) {
					sZero = "00";
				} else if (updatedSeq.length() == 2) {
					sZero = "0";
				}


				Hashtable<String, String> htTblCntUpdate = new Hashtable<String, String>();
				htTblCntUpdate.put(IDBConstants.PLANT, plant);
				htTblCntUpdate.put(IDBConstants.TBL_FUNCTION, "TRANSPORTMODE");
				htTblCntUpdate.put(IDBConstants.TBL_PREFIX1, "TM");
				StringBuffer updateQyery = new StringBuffer("set ");
				updateQyery.append(IDBConstants.TBL_NEXT_SEQ + " = '"
						+ (String) updatedSeq.toString() + "'");

				_TblControlDAO.update(updateQyery
						.toString(), htTblCntUpdate, "", plant);

				tranID = "TM" + sZero + updatedSeq;
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		}
		return tranID;

	}
	
	//Adds the transport mode to the Transprot Mode master table -Murali
	private String addTransportMode(String plant, String userName,
			String tranId, String TranDesc)
			throws ServletException, IOException, Exception {
		String response = "";
		Hashtable<String, String> ht = new Hashtable<String, String>();
		ht.put(IDBConstants.PLANT, plant);
		ht.put(IDBConstants.ID, tranId);
		if (!(transportModeUtil.isExistsItemType(ht))) // if the Item exists already
		{
			ht.put(IDBConstants.PLANT, plant);
			ht.put(IDBConstants.ID, tranId);
			ht.put(IDBConstants.TRANSPORT_MODE, TranDesc);
			ht.put(IDBConstants.CREATED_BY, userName);
			ht.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
			//ht.put(IDBConstants.LOGIN_USER, userName);

			MovHisDAO mdao = new MovHisDAO(plant);
			mdao.setmLogger(mLogger);
			Hashtable<String, String> htm = new Hashtable<String, String>();
			htm.put(IDBConstants.PLANT, plant);
			htm.put("DIRTYPE", TransactionConstants.ADD_TRANSPORT_MODE);
			htm.put("RECID", tranId);
			htm.put("REMARKS", TranDesc);
			htm.put("UPBY", userName);
			htm.put("CRBY", userName);
			htm.put("CRAT", DateUtils.getDateTime());
			htm.put("UPAT", DateUtils.getDateTime());
	          
//	          if(!"TM001".equals(tranId))
//	  		  {	
//	        	boolean exitFlag = false;
//				Hashtable htv = new Hashtable();				
//				htv.put(IDBConstants.PLANT, plant);
//				htv.put(IDBConstants.TBL_FUNCTION, "TRANSPORTMODE");
//				exitFlag = _TblControlDAO.isExisit(htv, "", plant);
//				if (exitFlag) 
//			)		_TblControlDAO.updateSeqNo("TRANSPORTMODE",plant);
//				else
//				{
//	            	Hashtable htTblCntInsert  = new Hashtable();           
//	            	htTblCntInsert.put(IDBConstants.PLANT,plant);          
//	            	htTblCntInsert.put(IDBConstants.TBL_FUNCTION,"TRANSPORTMODE");
//	            	htTblCntInsert.put(IDBConstants.TBL_PREFIX1,"TM");
//	             	htTblCntInsert.put("MINSEQ","0000");
//	             	htTblCntInsert.put("MAXSEQ","9999");
//	            	htTblCntInsert.put(IDBConstants.TBL_NEXT_SEQ, IDBConstants.TBL_FIRST_NEX_SEQ);
//	            	htTblCntInsert.put(IDBConstants.CREATED_BY, userName);
//	            	htTblCntInsert.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
//	            	_TblControlDAO.insertTblControl(htTblCntInsert,plant);
//				}
//			}
			
			boolean itemInserted = transportModeUtil.insertTransportModeMst(ht);
			boolean inserted = mdao.insertIntoMovHis(htm);
			if (itemInserted && inserted) {
				response = "<font class = " + IDBConstants.SUCCESS_COLOR
						+ ">Transaction Mode Added Successfully</font>";

			} else {
				response = "<font class = " + IDBConstants.FAILED_COLOR
						+ ">Failed to add New Transaction Mode </font>";

			}
		} else {
			response = "<font class = " + IDBConstants.FAILED_COLOR
					+ ">Transaction Mode  Exists already. Try again</font>";

		}
		return response;
	}
	
	
	//Updates the transport mode in the Transport Mode master table -Murali
	private String updateTransportMode(String plant, String userName,
			String tranId, String TranDesc)
			throws ServletException, IOException, Exception {
		String response = "";
		Hashtable<String, String> ht = new Hashtable<String, String>();
		ht.put(IDBConstants.PLANT, plant);
		ht.put(IDBConstants.ID, tranId);
		if ((transportModeUtil.isExistsItemType(ht))) // if the Item exists already
		{
			Hashtable<String, String> htUpdate = new Hashtable<String, String>();
			htUpdate.put(IDBConstants.ID, tranId);
			htUpdate.put(IDBConstants.TRANSPORT_MODE, TranDesc);
			/*htUpdate.put(IConstants.ISACTIVE, isActive);*/
			new DateUtils();
			htUpdate.put(IDBConstants.UPDATED_AT, DateUtils
					.getDateTime());
			htUpdate.put(IDBConstants.UPDATED_BY, userName);

			Hashtable<String,String> htCondition = new Hashtable<String,String>();
			htCondition.put(IDBConstants.ID, tranId);
			htCondition.put(IDBConstants.PLANT, plant);

			MovHisDAO mdao = new MovHisDAO(plant);
			mdao.setmLogger(mLogger);
			Hashtable<String, String> htm = new Hashtable<String, String>();
			htm.put("PLANT", plant);
			/*htm.put("DIRTYPE",TransactionConstants.UPD_TRANSPORT_MODE);
			htm.put("RECID", "");
			htm.put("ITEM",tranId);*/
			htm.put("UPBY", userName);
			htm.put("CRBY", userName);
			htm.put("CRAT", DateUtils.getDateTime());
			htm.put("REMARKS", TranDesc);
			htm.put("UPAT", DateUtils.getDateTime());
			htm.put(IDBConstants.TRAN_DATE, DateUtils
					.getDateinyyyy_mm_dd(DateUtils.getDate()));

			boolean Updated = transportModeUtil.updateTranModelMst(htUpdate,
					htCondition);
			boolean inserted = mdao.insertIntoMovHis(htm);
			if (Updated && inserted ) {
				response = "<font class = " + IDBConstants.SUCCESS_COLOR
						+ " >Transaction Mode  Updated Successfully</font>";
			} else {
				response = "<font class = " + IDBConstants.FAILED_COLOR
						+ " >Failed to Update Transaction Mode </font>";
			}
		} else {
			response = "<font class = "
					+ IDBConstants.FAILED_COLOR
					+ ">Transaction Mode  doesn't not Exists. Try again</font>";

		}
		
		return response;
		
	}
	
	private String deleteTrasnportMode(String plant, String userName,
			String tranId, String TranDesc)
			throws ServletException, IOException, Exception {
		String response = "";
		Hashtable htcondition = new Hashtable();
		htcondition.put(IDBConstants.PLANT, plant);
		htcondition.put(IDBConstants.ID, tranId);
//		
//		boolean itembrandflag  = transportModeDAO.isExists(htcondition);
//		if (itembrandflag) {
//			response = "<font class = " + IDBConstants.FAILED_COLOR
//					+ " >Transaction Mode Exists In Products</font>";
//		} 
//		else{
			if(transportModeUtil.isExistsItemType(htcondition))
			{
				boolean flag = transportModeUtil.deleteTranModelMst(htcondition);
				
				MovHisDAO mdao = new MovHisDAO(plant);
				mdao.setmLogger(mLogger);
				Hashtable<String, String> htm = new Hashtable<String, String>();
				htm.put("PLANT", plant);
				htm.put("DIRTYPE",TransactionConstants.DEL_TRANSPORT_MODE);
				htm.put("RECID", "");
				htm.put("ITEM",tranId);
				htm.put("UPBY", userName);
				htm.put("CRBY", userName);
				htm.put("CRAT", DateUtils.getDateTime());
				htm.put("REMARKS", TranDesc);
				htm.put("UPAT", DateUtils.getDateTime());
				htm.put(IDBConstants.TRAN_DATE, DateUtils
						.getDateinyyyy_mm_dd(DateUtils.getDate()));
				
				flag = mdao.insertIntoMovHis(htm);
				
				if(flag)
					{response = "<font class = " + IDBConstants.SUCCESS_COLOR
						+ " >Transaction Mode Deleted Successfully </font>";}
				else {
					response = "<font class = "+IConstants.FAILED_COLOR+">Failed to Delete Transaction Mode</font>";
                
      				}
			}else{
				response = "<font class = "+IConstants.FAILED_COLOR+">Transaction Mode doesn't  Exists. Try again</font>";
			}
		
//		}

		
		
		return response;
		
	}
	
	//Get the list of brands from the brand Master table. -Bruhan //11/9/12
	private ArrayList<Map<String, String>> getTranModeList(String plant,String tranID) throws Exception{
		return transportModeUtil.getTranModeList(tranID,plant,"");
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

	public HashMap<String, String> populateMapData(String companyCode,
			String userCode) {
		HashMap<String, String> loggerDetailsHasMap = new HashMap<String, String>();
		loggerDetailsHasMap.put(MLogger.COMPANY_CODE, companyCode);
		loggerDetailsHasMap.put(MLogger.USER_CODE, userCode);
		return loggerDetailsHasMap;

	}

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
}
