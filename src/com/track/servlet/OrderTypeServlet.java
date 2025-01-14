package com.track.servlet;

import java.io.IOException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.track.constants.IConstants;
import com.track.constants.IDBConstants;
import com.track.constants.MLoggerConstant;
import com.track.constants.TransactionConstants;
import com.track.dao.*;
import com.track.db.util.OrderTypeUtil;
import com.track.util.DateUtils;
import com.track.util.IMLogger;
import com.track.util.MLogger;
import com.track.util.StrUtils;

public class OrderTypeServlet extends HttpServlet implements IMLogger {
	private static final String CONTENT_TYPE = "text/html; charset=windows-1252";
	private boolean printLog = MLoggerConstant.OrderTypeServlet_PRINTPLANTMASTERLOG;
	private boolean printInfo = MLoggerConstant.OrderTypeServlet_PRINTPLANTMASTERINFO;
	String action = "";
	DateUtils dateutils = new DateUtils();
	OrderTypeUtil _OrderTypeUtil = null;

	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		_OrderTypeUtil = new OrderTypeUtil();
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		try {

			action = request.getParameter("action").trim();
			String plant = StrUtils.fString(
					(String) request.getSession().getAttribute("PLANT")).trim();
			String userName = StrUtils.fString(
					(String) request.getSession().getAttribute("LOGIN_USER"))
					.trim();
			this.setMapDataToLogger(this.populateMapData(plant, userName));
			_OrderTypeUtil.setmLogger(mLogger);
			if (this.printInfo) {
				diaplayInfoLogs(request);
			}
			if (action.equalsIgnoreCase("New")) {
				response.sendRedirect("jsp/OrderType_View.jsp?action=NEW");
			}

			else if (action.equalsIgnoreCase("ADD")) {
               
				String result = addOrderType(request, response);
				response
						.sendRedirect("../ordertype/summary?action=SHOW_RESULT&result="
								+ result);

			}
			else if (action.equalsIgnoreCase("ADD1")) {
	               
				String result = addOrderType1(request, response);
				response
						.sendRedirect("jsp/OrderType_list.jsp?action=SHOW_RESULT&result="
								+ result);

			} 
			else if (action.equalsIgnoreCase("UPDATE")) {

				String result = updateOrderType(request, response);
				response
						.sendRedirect("../ordertype/summary?action=SHOW_RESULT&result="
								+ result);
			}

			else if (action.equalsIgnoreCase("DELETE")) {

				String result = DeleteOrderType(request, response);
				response
						.sendRedirect("../ordertype/summary?action=SHOW_RESULT&result="
								+ result);
			}
                        
                  
		} catch (Exception ex) {
			if (action.equalsIgnoreCase("ADD1")) {
			this.mLogger.exception(this.printLog, "", ex);
			String result = "<font class = " + IConstants.FAILED_COLOR
					+ ">Exception : " + ex.getMessage() + "</font>";
			response
					.sendRedirect("jsp/OrderType_list.jsp?action=SHOW_RESULT_VALUE&result="
							+ result);
			
			}
			else{
			this.mLogger.exception(this.printLog, "", ex);
			String result = "<font class = " + IConstants.FAILED_COLOR
					+ ">Exception : " + ex.getMessage() + "</font>";
			response
					.sendRedirect("jsp/OrderType_View.jsp?action=SHOW_RESULT_VALUE&result="
							+ result);
			}

		}

	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

	private String addOrderType(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException,
			Exception {
		String result = "";
	
		try {
			HttpSession session = request.getSession();
			String plant = (String) session.getAttribute("PLANT");
			String user = (String) session.getAttribute("LOGIN_USER");

			String ordertype = request.getParameter("ORDERTYPE").trim();
			String orderdesc = request.getParameter("ORDERDESC").trim();
			String type = request.getParameter("TYPE").trim();
			String remarks = request.getParameter("REMARKS").trim();
						

			Hashtable ht = new Hashtable();
			ht.put("PLANT", plant);
			ht.put("ORDERTYPE", ordertype);
			ht.put("TYPE", type);
			ht.put("ORDERDESC", orderdesc);
			ht.put("REMARKS", remarks);
			ht.put("ISACTIVE", "Y");
			

			MovHisDAO mdao = new MovHisDAO(plant);
			mdao.setmLogger(mLogger);
			Hashtable htm = new Hashtable();
			htm.put("PLANT", plant);
			htm.put("DIRTYPE", TransactionConstants.ADD_ORDERTYPE);
			htm.put("RECID", "");
			htm.put("CRBY", user);
			htm.put("REMARKS", remarks);
			//htm.put("LOC", locId);
			htm.put("CRAT", dateutils.getDateTime());
			htm.put(IDBConstants.TRAN_DATE, dateutils
					.getDateinyyyy_mm_dd(dateutils.getDate()));
			boolean isvalidOrderType = new OrderTypeBeanDAO().isExists(ordertype, type,plant);
			if(!isvalidOrderType) {
			boolean flag = _OrderTypeUtil.addOrderType(ht);
			
			
			boolean inserted = mdao.insertIntoMovHis(htm);
			if (flag && inserted) {
				request.getSession().setAttribute("orderTypeData", ht);
				result = "<font class = " + IConstants.SUCCESS_COLOR
						+ ">OrderType Added Successfully</font>";

			} 
			} else {
				result = "<font class = " + IConstants.FAILED_COLOR
						+ "> OrderType Exists Already</font>";
				
			}

		} catch (Exception e) {
			throw e;
		}

		return result;
	}
	
	private String addOrderType1(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException,
			Exception {
		String result = "";
	
		try {
			HttpSession session = request.getSession();
			String plant = (String) session.getAttribute("PLANT");
			String user = (String) session.getAttribute("LOGIN_USER");

			String ordertype = request.getParameter("ORDERTYPE").trim();
			String orderdesc = request.getParameter("ORDERDESC").trim();
			String type = request.getParameter("TYPE").trim();
			String remarks = request.getParameter("REMARKS").trim();
			String formtype = request.getParameter("FORMTYPE").trim();
						

			Hashtable ht = new Hashtable();
			ht.put("PLANT", plant);
			ht.put("ORDERTYPE", ordertype);
			ht.put("TYPE", type);
			ht.put("ORDERDESC", orderdesc);
			ht.put("REMARKS", remarks);
			ht.put("ISACTIVE", "Y");
			

			MovHisDAO mdao = new MovHisDAO(plant);
			mdao.setmLogger(mLogger);
			Hashtable htm = new Hashtable();
			htm.put("PLANT", plant);
			htm.put("DIRTYPE", TransactionConstants.ADD_ORDERTYPE);
			htm.put("RECID", "");
			htm.put("CRBY", user);
			htm.put("REMARKS", remarks);
			//htm.put("LOC", locId);
			htm.put("CRAT", dateutils.getDateTime());
			htm.put(IDBConstants.TRAN_DATE, dateutils
					.getDateinyyyy_mm_dd(dateutils.getDate()));

			boolean flag = _OrderTypeUtil.addOrderType(ht);
			
			
			boolean inserted = mdao.insertIntoMovHis(htm);
			
			if (flag && inserted) {
				
					request.getSession().setAttribute("orderTypeData", ht);
				result = "<font class = " + IConstants.SUCCESS_COLOR
						+ ">OrderType Added Successfully</font>&FORMTYPE="+ formtype;

			

			} else {
				result = "<font class = " + IConstants.FAILED_COLOR
						+ "> OrderType Exists Already</font>";
				
			}

		} catch (Exception e) {
			throw e;
		}

		return result;
	}

	private String updateOrderType(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException,
			Exception {

		String result = "", ordertype = "", orderdesc = "", type = "",remarks = "", isActive = "";
		HttpSession session = request.getSession();
		String plant = (String) session.getAttribute("PLANT");
		String user = (String) session.getAttribute("LOGIN_USER");
		Hashtable htValues = new Hashtable();
		Hashtable htCondition = new Hashtable();

		try {

			ordertype = request.getParameter("ORDERTYPE").trim();
			orderdesc= request.getParameter("ORDERDESC").trim();
			type= request.getParameter("TYPE").trim();
			isActive = request.getParameter("ACTIVE").trim();
			remarks = request.getParameter("REMARKS").trim();

			Hashtable ht = new Hashtable();
			ht.put("PLANT", plant);
			ht.put("ORDERTYPE", ordertype);
			ht.put("ORDERDESC", orderdesc);
			ht.put("TYPE", type);
			ht.put("ISACTIVE", "Y");
			ht.put("REMARKS", remarks);

			if (orderdesc.length() > 0 || !orderdesc.equalsIgnoreCase(null)) {
				htValues.put("ORDERDESC", orderdesc);
			}
			//if (type.length() > 0 || !type.equalsIgnoreCase(null)) {
			//	htValues.put("TYPE", type);
			//}
			if (remarks.length() > 0 || !remarks.equalsIgnoreCase(null)) {
				htValues.put("REMARKS", remarks);
			}
			if (isActive.length() > 0 || !isActive.equalsIgnoreCase(null)) {
				htValues.put("ISACTIVE", isActive);
			}

			htCondition.put("ORDERTYPE", ht.get("ORDERTYPE"));
			htCondition.put("TYPE", ht.get("TYPE"));
			htCondition.put("PLANT", ht.get("PLANT"));

			MovHisDAO mdao = new MovHisDAO(plant);
			mdao.setmLogger(mLogger);
			Hashtable htm = new Hashtable();
			htm.put("PLANT", plant);
			htm.put("DIRTYPE", TransactionConstants.UPD_ORDERTYPE);
			htm.put("RECID", "");
			htm.put("UPBY", user);
			htm.put("REMARKS", remarks);
			htm.put("CRBY", user);
			htm.put("CRAT", dateutils.getDateTime());
			htm.put("UPAT", dateutils.getDateTime());
			htm.put(IDBConstants.TRAN_DATE, dateutils
					.getDateinyyyy_mm_dd(dateutils.getDate()));

			boolean flag = _OrderTypeUtil.updateOrderType(htValues, htCondition, " ");
			boolean inserted = mdao.insertIntoMovHis(htm);
			if (flag && inserted) {

				request.getSession().setAttribute("orderTypeData", ht);
				result = "<font class = " + IConstants.SUCCESS_COLOR
						+ ">OrderType updated Successfully</font>";

			} else {
				throw new Exception("Unable to update OrderType ");
			}

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		}

		return result;
	}

	private String DeleteOrderType(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException,
			Exception {

		String result = "", ordertype = "", orderdesc = "", remarks = "",type="",TABLE_NAME="";
		HttpSession session = request.getSession();
		String plant = (String) session.getAttribute("PLANT");
		Hashtable htValues = new Hashtable();
		Hashtable htCondition = new Hashtable();
		ordertype = request.getParameter("ORDERTYPE").trim();
		type = request.getParameter("TYPE").trim();
		try {
			Hashtable ht = new Hashtable();
			htCondition.put("PLANT", plant);
			htCondition.put("ORDERTYPE", ordertype);
			htCondition.put("TYPE", type);
			
			PoHdrDAO dao = new PoHdrDAO();
			DoHdrDAO dodao = new DoHdrDAO();
		    EstHdrDAO estdao = new EstHdrDAO();
		    ToHdrDAO todao = new ToHdrDAO();
		    POSHdrDAO posdao = new POSHdrDAO();
		    InvoiceDAO invoiceDAO = new InvoiceDAO();
		    BillDAO billDAO = new BillDAO();
		    
		    boolean isExistPO=false;
		    boolean isExistDO=false;
		    boolean isExistEstimate=false;
		    boolean isExistConsignment=false;
		    boolean isExistTaxInvoice=false;
		   	Hashtable htmh = new Hashtable();
		   
		   	htmh.put(IConstants.PLANT,plant);
			htmh.put("ORDERTYPE",ordertype);
		  //htmh.put("TYPE",type);
			
		  
		   
		   
		   	//if(movementhistoryExist || isExistDO || isExistEstimate)
		   	if(type.equalsIgnoreCase("PURCHASE")){
		   		isExistPO = dao.isExisit(htmh,"ordertype in(select ordertype from " + "[" + plant + "_ORDERTYPE] WHERE TYPE='PURCHASE')");
		   	if(isExistPO)
		   	{	
		   		ht = new Hashtable();
				ht.put("PLANT", plant);
				ht.put("ORDERTYPE", "");
				//ht.put("WHID", "");
				ht.put("ORDERDESC", "");
				ht.put("TYPE", "");
				ht.put("REMARKS", "");
				ht.put("ISACTIVE", "");
				request.getSession().setAttribute("orderTypeData", ht);
		   		result = "<font class = " + IDBConstants.FAILED_COLOR
		   					+ " >Order Type Exists In Purchase Orders</font>";
		   		
		   	}
		   	else{
			
			boolean flag = _OrderTypeUtil.deleteOrderType(ordertype, type,plant);
			if (flag) {
				ht = new Hashtable();
				ht.put("PLANT", plant);
				ht.put("ORDERTYPE", "");
				//ht.put("WHID", "");
				ht.put("ORDERDESC", "");
				ht.put("TYPE", "");
				ht.put("REMARKS", "");
				ht.put("ISACTIVE", "");
				request.getSession().setAttribute("orderTypeData", ht);
				result = "<font class = " + IConstants.SUCCESS_COLOR
						+ ">OrderType Deleted Successfully</font>&"
						+ "action = show_result";

			} else {
				result = "<font class = " + IConstants.FAILED_COLOR
						+ ">Unable to delete ordertype</font>";
				
			}
		   	}
		   
		}
		   	
		   	if(type.equalsIgnoreCase("BILL")){
		   		isExistPO = billDAO.isExisit(htmh,"ordertype in(select ordertype from " + "[" + plant + "_ORDERTYPE] WHERE TYPE='BILL')");
		   		if(isExistPO)
		   		{	
		   			ht = new Hashtable();
		   			ht.put("PLANT", plant);
		   			ht.put("ORDERTYPE", "");
		   			//ht.put("WHID", "");
		   			ht.put("ORDERDESC", "");
		   			ht.put("TYPE", "");
		   			ht.put("REMARKS", "");
		   			ht.put("ISACTIVE", "");
		   			request.getSession().setAttribute("orderTypeData", ht);
		   			result = "<font class = " + IDBConstants.FAILED_COLOR
		   					+ " >Order Type Exists In Bill</font>";
		   			
		   		}
		   		else{
		   			
		   			boolean flag = _OrderTypeUtil.deleteOrderType(ordertype, type,plant);
		   			if (flag) {
		   				ht = new Hashtable();
		   				ht.put("PLANT", plant);
		   				ht.put("ORDERTYPE", "");
		   				//ht.put("WHID", "");
		   				ht.put("ORDERDESC", "");
		   				ht.put("TYPE", "");
		   				ht.put("REMARKS", "");
		   				ht.put("ISACTIVE", "");
		   				request.getSession().setAttribute("orderTypeData", ht);
		   				result = "<font class = " + IConstants.SUCCESS_COLOR
		   						+ ">OrderType Deleted Successfully</font>&"
		   						+ "action = show_result";
		   				
		   			} else {
		   				result = "<font class = " + IConstants.FAILED_COLOR
		   						+ ">Unable to delete ordertype</font>";
		   				
		   			}
		   		}
		   		
		   	}
		   	
		   	//sales
		   	if(type.equalsIgnoreCase("SALES")){
		   	 	isExistDO = dodao.isExisit(htmh," ordertype in(select ordertype from " + "[" + plant + "_ORDERTYPE] WHERE TYPE='SALES')");
		   	if(isExistDO)
		   	{	
		   		ht = new Hashtable();
				ht.put("PLANT", plant);
				ht.put("ORDERTYPE", "");
				//ht.put("WHID", "");
				ht.put("ORDERDESC", "");
				ht.put("TYPE", "");
				ht.put("REMARKS", "");
				ht.put("ISACTIVE", "");
				request.getSession().setAttribute("orderTypeData", ht);
		   		result = "<font class = " + IDBConstants.FAILED_COLOR
		   					+ " >Order Type Exists In Sales Orders</font>";
		   		
		   	}
		   	else{
			
			boolean flag = _OrderTypeUtil.deleteOrderType(ordertype, type,plant);
			if (flag) {
				ht = new Hashtable();
				ht.put("PLANT", plant);
				ht.put("ORDERTYPE", "");
				//ht.put("WHID", "");
				ht.put("ORDERDESC", "");
				ht.put("TYPE", "");
				ht.put("REMARKS", "");
				ht.put("ISACTIVE", "");
				request.getSession().setAttribute("orderTypeData", ht);
				result = "<font class = " + IConstants.SUCCESS_COLOR
						+ ">OrderType Deleted Successfully</font>&"
						+ "action = show_result";

			} else {
				result = "<font class = " + IConstants.FAILED_COLOR
						+ ">Unable to delete ordertype</font>";
				
			}
		   	}
		
		}
		   	
		   	//INVOICE
		   	if(type.equalsIgnoreCase("INVOICE")){
		   		isExistDO = invoiceDAO.isExisit(htmh," ordertype in(select ordertype from " + "[" + plant + "_ORDERTYPE] WHERE TYPE='INVOICE')");
		   		if(isExistDO)
		   		{	
		   			ht = new Hashtable();
		   			ht.put("PLANT", plant);
		   			ht.put("ORDERTYPE", "");
		   			//ht.put("WHID", "");
		   			ht.put("ORDERDESC", "");
		   			ht.put("TYPE", "");
		   			ht.put("REMARKS", "");
		   			ht.put("ISACTIVE", "");
		   			request.getSession().setAttribute("orderTypeData", ht);
		   			result = "<font class = " + IDBConstants.FAILED_COLOR
		   					+ " >Order Type Exists In Invoice</font>";
		   			
		   		}
		   		else{
		   			
		   			boolean flag = _OrderTypeUtil.deleteOrderType(ordertype, type,plant);
		   			if (flag) {
		   				ht = new Hashtable();
		   				ht.put("PLANT", plant);
		   				ht.put("ORDERTYPE", "");
		   				//ht.put("WHID", "");
		   				ht.put("ORDERDESC", "");
		   				ht.put("TYPE", "");
		   				ht.put("REMARKS", "");
		   				ht.put("ISACTIVE", "");
		   				request.getSession().setAttribute("orderTypeData", ht);
		   				result = "<font class = " + IConstants.SUCCESS_COLOR
		   						+ ">OrderType Deleted Successfully</font>&"
		   						+ "action = show_result";
		   				
		   			} else {
		   				result = "<font class = " + IConstants.FAILED_COLOR
		   						+ ">Unable to delete ordertype</font>";
		   				
		   			}
		   		}
		   		
		   	}
		   	
		   	//Sales Estimate
		   
		   	if(type.equalsIgnoreCase("SALES ESTIMATE")){
		   		isExistEstimate= estdao.isExisit(htmh," ordertype in(select ordertype from " + "[" + plant + "_ORDERTYPE] WHERE TYPE='SALES ESTIMATE')");
		   	if(isExistEstimate)
		   	{	
		   		ht = new Hashtable();
				ht.put("PLANT", plant);
				ht.put("ORDERTYPE", "");
				//ht.put("WHID", "");
				ht.put("ORDERDESC", "");
				ht.put("TYPE", "");
				ht.put("REMARKS", "");
				ht.put("ISACTIVE", "");
				request.getSession().setAttribute("orderTypeData", ht);
		   		result = "<font class = " + IDBConstants.FAILED_COLOR
		   					+ " >Order Type Exists In Sales Estimate Orders</font>";
		   		
		   	}
		   	else{
			
			boolean flag = _OrderTypeUtil.deleteOrderType(ordertype, type,plant);
			if (flag) {
				ht = new Hashtable();
				ht.put("PLANT", plant);
				ht.put("ORDERTYPE", "");
				//ht.put("WHID", "");
				ht.put("ORDERDESC", "");
				ht.put("TYPE", "");
				ht.put("REMARKS", "");
				ht.put("ISACTIVE", "");
				request.getSession().setAttribute("orderTypeData", ht);
				result = "<font class = " + IConstants.SUCCESS_COLOR
						+ ">OrderType Deleted Successfully</font>&"
						+ "action = show_result";

			} else {
				result = "<font class = " + IConstants.FAILED_COLOR
						+ ">Unable to delete ordertype</font>";
				
			}
		   }
		
		}
			//CONSIGNMENT BY NAVAS
			   
		   	if(type.equalsIgnoreCase("CONSIGNMENT")){
		   		isExistConsignment= todao.isExisit(htmh," ordertype in(select ordertype from " + "[" + plant + "_ORDERTYPE] WHERE TYPE='CONSIGNMENT')");
		   	if(isExistConsignment)
		   	{	
		   		ht = new Hashtable();
				ht.put("PLANT", plant);
				ht.put("ORDERTYPE", "");
				//ht.put("WHID", "");
				ht.put("ORDERDESC", "");
				ht.put("TYPE", "");
				ht.put("REMARKS", "");
				ht.put("ISACTIVE", "");
				request.getSession().setAttribute("orderTypeData", ht);
		   		result = "<font class = " + IDBConstants.FAILED_COLOR
		   					+ " >Order Type Exists In Consignment Orders</font>";
		   		
		   	}
		   	else{
			
			boolean flag = _OrderTypeUtil.deleteOrderType(ordertype, type,plant);
			if (flag) {
				ht = new Hashtable();
				ht.put("PLANT", plant);
				ht.put("ORDERTYPE", "");
				//ht.put("WHID", "");
				ht.put("ORDERDESC", "");
				ht.put("TYPE", "");
				ht.put("REMARKS", "");
				ht.put("ISACTIVE", "");
				request.getSession().setAttribute("orderTypeData", ht);
				result = "<font class = " + IConstants.SUCCESS_COLOR
						+ ">OrderType Deleted Successfully</font>&"
						+ "action = show_result";

			} else {
				result = "<font class = " + IConstants.FAILED_COLOR
						+ ">Unable to delete ordertype</font>";
				
			}
		   }
		
		}
		   	
		   	
		   	//Tax Invoice
			   
		   	if(type.equalsIgnoreCase("TAX INVOICE")){
		   		isExistTaxInvoice= posdao.isExisit(htmh," ordertype in(select ordertype from " + "[" + plant + "_ORDERTYPE] WHERE TYPE='TAX INVOICE')");
		   	if(isExistTaxInvoice)
		   	{	
		   		ht = new Hashtable();
				ht.put("PLANT", plant);
				ht.put("ORDERTYPE", "");
				//ht.put("WHID", "");
				ht.put("ORDERDESC", "");
				ht.put("TYPE", "");
				ht.put("REMARKS", "");
				ht.put("ISACTIVE", "");
				request.getSession().setAttribute("orderTypeData", ht);
		   		result = "<font class = " + IDBConstants.FAILED_COLOR
		   					+ " >Order Type Exists In Direct Tax Invoice</font>";
		   		
		   	}
		   	else{
			
			boolean flag = _OrderTypeUtil.deleteOrderType(ordertype, type,plant);
			if (flag) {
				ht = new Hashtable();
				ht.put("PLANT", plant);
				ht.put("ORDERTYPE", "");
				//ht.put("WHID", "");
				ht.put("ORDERDESC", "");
				ht.put("TYPE", "");
				ht.put("REMARKS", "");
				ht.put("ISACTIVE", "");
				request.getSession().setAttribute("orderTypeData", ht);
				result = "<font class = " + IConstants.SUCCESS_COLOR
						+ ">OrderType Deleted Successfully</font>&"
						+ "action = show_result";

			} else {
				result = "<font class = " + IConstants.FAILED_COLOR
						+ ">Unable to delete ordertype</font>";
				
			}
		   }
		
		}


		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		}

		return result;
	}
        
       
     
        

	public HashMap<String, String> populateMapData(String companyCode,String userCode) {
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