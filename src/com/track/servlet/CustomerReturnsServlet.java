package com.track.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.transaction.UserTransaction;

import net.sf.json.JSONObject;

import com.track.constants.IConstants;
import com.track.constants.IDBConstants;
import com.track.constants.MLoggerConstant;
import com.track.dao.BomDAO;
import com.track.dao.InvMstDAO;
import com.track.dao.ItemMstDAO;
import com.track.dao.LocMstDAO;
import com.track.dao.RsnMst;
import com.track.dao.ShipHisDAO;
import com.track.db.util.CustomerReturnUtil;
import com.track.db.util.ItemMstUtil;
import com.track.db.util.MiscReceivingUtil;
import com.track.db.util.POUtil;
import com.track.db.util.UserLocUtil;
import com.track.gates.DbBean;
import com.track.util.DateUtils;
import com.track.util.IMLogger;
import com.track.util.MLogger;
import com.track.util.StrUtils;

/**
 * Servlet implementation class customerReturns
 */
public class CustomerReturnsServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private boolean printLog = MLoggerConstant.CustomerReturnServlet_PRINTPLANTMASTERLOG;
	private boolean printInfo = MLoggerConstant.CustomerReturnServlet_PRINTPLANTMASTERINFO;

	private static final String CONTENT_TYPE = "text/xml";
	private String xmlStr = "";
	private String action = "";
	CustomerReturnUtil _customerReturnUtil = null;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public CustomerReturnsServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	public void init() throws ServletException {

		_customerReturnUtil = new CustomerReturnUtil();

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

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub

		response.setContentType(CONTENT_TYPE);
		PrintWriter out = response.getWriter();
		
		JSONObject jsonObjectResult = new JSONObject();

		this.setMapDataToLogger(this.populateMapData(StrUtils.fString(request
				.getParameter("PLANT")), StrUtils.fString(request
				.getParameter("LOGIN_USER"))
				+ " PDA_USER"));

		_customerReturnUtil.setmLogger(mLogger);
		if (this.printInfo) {
			diaplayInfoLogs(request);
		}

		try {
			action = request.getParameter("action").trim();

			if (action.equalsIgnoreCase("customerReturns")) {
				xmlStr = "";
				xmlStr = this.process_customerReturns(request, response);

			} else if (action.equalsIgnoreCase("customerReturnsMultiple")) {
				xmlStr = "";

				xmlStr = this
						.process_customerReturnsMultiple(request, response);

			}
						

		} catch (Exception e) {
			System.out.print("Exception Message" + e.getMessage());
		}
		out.write(xmlStr);
		out.close();
		
	
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

	private String process_customerReturns(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException,
			Exception {

		Map receiveMaterial_HM = null;
		String PLANT = "", PO_NUM = "", ITEM_NUM = "", PO_LN_NUM = "", CUSTNAME = "";
		String ITEM_DESCRIPTION = "", LOGIN_USER = "", REMARKS = "", EXPIREDATE = "", expdate = "", status = "";
		String ITEM_BATCH = "", CUST_CODE = "", Claimprice = "", ITEM_QTY = "", loc1 = "", 
		REMARK = "", ITEM_LOC = "", RSNDESC = "", OUTBNDORDNO = "",ORDERNO="",REASONCODE="",LOC="";
		ArrayList loclist = null;
		try {
			HttpSession session = request.getSession();
			PLANT = StrUtils.fString((String) session.getAttribute("PLANT"));
			ITEM_NUM = StrUtils.fString(request.getParameter("ITEM"));
			LOGIN_USER = StrUtils.fString(request.getParameter("LOGINUSER"));
			ITEM_DESCRIPTION = StrUtils.fString(request.getParameter("ITEMDESC"));
			ITEM_BATCH = StrUtils.fString(request.getParameter("BATCH"));
			ITEM_QTY = StrUtils.fString(request.getParameter("QTY"));
			REMARK = StrUtils.fString(request.getParameter("REMARK"));
			REASONCODE = StrUtils.fString(request.getParameter("REASONCODE"));
			ORDERNO = StrUtils.fString(request.getParameter("ORDERNO"));
			LOC = StrUtils.fString(request.getParameter("LOC"));																	// price
			if (Claimprice == null || Claimprice.equalsIgnoreCase(""))
				Claimprice = "";
			ItemMstUtil itemMstUtil = new ItemMstUtil();
			List shipqry = null;
			InvMstDAO invdao = new InvMstDAO();
			ShipHisDAO shipdao = new ShipHisDAO();
			itemMstUtil.setmLogger(mLogger);
			invdao.setmLogger(mLogger);
				
			ITEM_NUM = itemMstUtil.isValidAlternateItemInItemmst(PLANT,
					ITEM_NUM);
			receiveMaterial_HM = new HashMap();
			receiveMaterial_HM.put(IConstants.PLANT, PLANT);
			receiveMaterial_HM.put(IConstants.ITEM, ITEM_NUM);
			receiveMaterial_HM.put(IConstants.ITEM_DESC, ITEM_DESCRIPTION);
			receiveMaterial_HM.put(IConstants.LOGIN_USER, LOGIN_USER);

			receiveMaterial_HM.put(IConstants.INV_BATCH, ITEM_BATCH);
																		
			receiveMaterial_HM.put(IConstants.QTY, ITEM_QTY);

			receiveMaterial_HM.put(IConstants.REASONCODE, REASONCODE);
			
			receiveMaterial_HM.put(IConstants.ORDERNO, ORDERNO);
			receiveMaterial_HM.put(IConstants.REMARKS, REMARK);
			receiveMaterial_HM.put(IConstants.LOC ,LOC);
			
			double itemqty = Double.parseDouble(ITEM_QTY);
			itemqty = StrUtils.RoundDB(itemqty , IConstants.DECIMALPTS);
			ITEM_QTY = String.valueOf(itemqty);
			
			xmlStr = "";
			Hashtable htshiphis = new Hashtable();
			htshiphis.put(IDBConstants.PLANT, PLANT);
			htshiphis.put(IDBConstants.ITEM, ITEM_NUM);
			htshiphis.put(IDBConstants.TBL_BATCH_CAPTION, ITEM_BATCH);
			htshiphis.put(IDBConstants.DODET_DONUM, ORDERNO);
			htshiphis.put(IDBConstants.STATUS, "C");
			Boolean isProductAvalable = Boolean.valueOf(true);
			// Get expiry date By product and serialno
			
			
		   //Process Check Parent Item Qty
			  
		    ItemMstDAO itemmstdao=new ItemMstDAO();
		    Hashtable htItem = new Hashtable();
		    htItem.put(IDBConstants.PLANT, PLANT);
		    htItem.put(IDBConstants.ITEM, ITEM_NUM);
		   
			    //if(itemmstdao.isExisit(htItem, " userfld1='K'") )
			    if(itemmstdao.isExisit(htItem, " ") )
			    {
			    	int qty = Integer.parseInt(((String) ITEM_QTY.trim()));
					if (qty  > 1) {
						 throw new Exception("Parent item return quantity should be 1");
					
					}
									    	
			    }
			
			  //Process Check Parent Item Qty
			
			shipqry = shipdao
					.selectShipHis(
							"distinct isnull(cname,'') as cname,isnull(expirydat,'') as expiredate,loc1,isnull(userfld2,'') as custcode,isnull(STATUS,'') as STATUS",
							htshiphis, "");
			for (int i = 0; i < shipqry.size(); i++) {
				
			

				Map m = (Map) shipqry.get(i);
				EXPIREDATE = (String) m.get("expiredate");
				
				CUSTNAME = (String) m.get("cname");
				
				loc1 = (String) m.get("loc1");
				
				CUST_CODE = (String) m.get("custcode");
				
				status = (String) m.get("STATUS");
				

				if (!status.equalsIgnoreCase("C")) {
					
					throw new Exception(" Selected product " + ITEM_NUM + " - " + ITEM_BATCH + " is not issued yet.");
				}
			}
			
			expdate = EXPIREDATE;
		
			
			if (!EXPIREDATE.equals(null) && !EXPIREDATE.equals("")) {
				
						
				boolean dtflg = DateUtils.checkDateCustomerReturn(EXPIREDATE);// yyyymmdd
			
				if (dtflg == false) {
					throw new Exception("Product ID Expired");
				}
			}
			
			receiveMaterial_HM.put(IConstants.EXPIREDATE, expdate);
			receiveMaterial_HM.put(IDBConstants.CUSTNAME, CUSTNAME);
			receiveMaterial_HM.put(IDBConstants.CUSTOMER_CODE, CUST_CODE);
			
			
			if (isProductAvalable) {
			
				xmlStr = _customerReturnUtil
						.process_CustomerReturns(receiveMaterial_HM);

				request.getSession().setAttribute(
						"RESULT",
						"Product : " + ITEM_NUM + " "
								+ "returned successfully!");
				response.sendRedirect("jsp/customerReturns.jsp?action=result");
			} else {
				request.getSession().setAttribute("RESULTERROR",
						"Error in returning Product:" + ITEM_NUM);
				response
						.sendRedirect("jsp/customerReturns.jsp?action=resulterror");
			}

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			request.getSession().setAttribute("RESULTERROR",
					"Error:" + e.getMessage());
			response.sendRedirect("jsp/customerReturns.jsp?action=resulterror");

			throw e;
		}

		return xmlStr;
	}

	private String process_customerReturnsMultiple(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException,
			Exception {

		Map receiveMaterial_HM = null;
		String PLANT = "", PO_NUM = "", ITEM_NUM = "", CUSTNAME = "";
		String ITEM_DESCRIPTION = "", LOGIN_USER = "", EXPIREDATE = "", expdate = "", status = "";
		String ITEM_BATCH = "", CUST_CODE = "", ITEM_QTY = "", loc1 = "", REMARK = "", 
		CLAIMPRICE = "", OUTBNDORDNO = "",ORDERNO="",REASONCODE="",LOC="";

		UserTransaction ut = null;
		try {
			HttpSession session = request.getSession();
			PLANT = StrUtils.fString((String) session.getAttribute("PLANT"));

			LOGIN_USER = StrUtils.fString(request.getParameter("LOGINUSER"));
			int DYNAMIC_RETURNS_SIZE = Integer.parseInt((request
					.getParameter("DYNAMIC_RETURNS_SIZE")));
			ItemMstUtil itemMstUtil = new ItemMstUtil();
			List shipqry = null;
			InvMstDAO invdao = new InvMstDAO();
			ShipHisDAO shipdao = new ShipHisDAO();
			itemMstUtil.setmLogger(mLogger);
			invdao.setmLogger(mLogger);

			Boolean transactionHandler = true;
			ut = DbBean.getUserTranaction();
			ut.begin();
			for (int index = 0; index < DYNAMIC_RETURNS_SIZE; index++) {

				ITEM_NUM = StrUtils.fString(request.getParameter("ITEM" + "_"
						+ index));
				ITEM_DESCRIPTION = StrUtils.fString(request
						.getParameter("ITEMDESC" + "_" + index));
				ITEM_BATCH = StrUtils.fString(request.getParameter("BATCH"
						+ "_" + index));
				ITEM_QTY = StrUtils.fString(request.getParameter("QTY" + "_"
						+ index));
				REMARK = StrUtils.fString(request.getParameter("REMARK" + "_"
						+ index));
				REASONCODE = StrUtils.fString(request.getParameter("REASONCODE"
						+ "_" + index));// Merchant Name
				ORDERNO = StrUtils.fString(request.getParameter("ORDERNO"
						+ "_" + index));
				LOC = StrUtils.fString(request.getParameter("FROMLOC"
						+ "_" + index));
				
				double itemqty = Double.parseDouble(ITEM_QTY);
				itemqty = StrUtils.RoundDB(itemqty , IConstants.DECIMALPTS);
				ITEM_QTY = String.valueOf(itemqty);


				ITEM_NUM = itemMstUtil.isValidAlternateItemInItemmst(PLANT,
						ITEM_NUM);
				
				// Process Check Parent Item Qty
			    ItemMstDAO itemmstdao=new ItemMstDAO();
			    Hashtable htItem = new Hashtable();
			    htItem.put(IDBConstants.PLANT, PLANT);
			    htItem.put(IDBConstants.ITEM, ITEM_NUM);
			    	//if(itemmstdao.isExisit(htItem, " userfld1='K'") )
				    if(itemmstdao.isExisit(htItem, " ") )
				    {
				    	double qty = Double.parseDouble(( StrUtils.fString(request.getParameter("QTY" + "_"
								+ index))));
						if (qty  > 1) {
								throw new Exception("Parent item return quantity should be 1");
						
						}
										    	
				    }
				
				  //End Process Check Parent Item Qty
					
				receiveMaterial_HM = new HashMap();
				receiveMaterial_HM.put(IConstants.PLANT, PLANT);
				receiveMaterial_HM.put(IConstants.ITEM, ITEM_NUM);
				receiveMaterial_HM.put(IConstants.ITEM_DESC, ITEM_DESCRIPTION);
				receiveMaterial_HM.put(IConstants.LOGIN_USER, LOGIN_USER);

				receiveMaterial_HM.put(IConstants.INV_BATCH, ITEM_BATCH);// Serial
																			// No
				receiveMaterial_HM.put(IConstants.QTY, ITEM_QTY);

				receiveMaterial_HM.put(IConstants.REASONCODE , REASONCODE);

				receiveMaterial_HM.put(IConstants.REMARKS, REMARK);
				receiveMaterial_HM.put(IConstants.ORDERNO, ORDERNO);
				receiveMaterial_HM.put(IConstants.LOC ,LOC);
				


				xmlStr = "";
				Hashtable htshiphis = new Hashtable();
				htshiphis.put(IDBConstants.PLANT, PLANT);
				htshiphis.put(IDBConstants.ITEM, ITEM_NUM);
				htshiphis.put(IDBConstants.TBL_BATCH_CAPTION, ITEM_BATCH);
				htshiphis.put(IDBConstants.DODET_DONUM, ORDERNO);
				htshiphis.put(IDBConstants.STATUS, "C");
				Boolean isProductAvalable = Boolean.valueOf(true);
			
				shipqry = shipdao
						.selectShipHis(
								"distinct isnull(cname,'') as cname,isnull(expirydat,'') as expiredate,loc1,isnull(userfld2,'') as custcode,isnull(STATUS,'') as STATUS",
								htshiphis, "");
				for (int i = 0; i < shipqry.size(); i++) {

					Map m = (Map) shipqry.get(i);
					EXPIREDATE = (String) m.get("expiredate");
					CUSTNAME = (String) m.get("cname");
					loc1 = (String) m.get("loc1");
					CUST_CODE = (String) m.get("custcode");
					status = (String) m.get("STATUS");

					if (!status.equals("C")) {
						throw new Exception(
								" Selected product " + ITEM_NUM + " - " + ITEM_BATCH + " is not issued yet.");
					}
				}
				expdate = EXPIREDATE;
				
				if (!EXPIREDATE.equals(null) && !EXPIREDATE.equals("")) {
			
					boolean dtflg = DateUtils.checkDateCustomerReturn(EXPIREDATE);// yyyymmdd
					if (dtflg == false) {
						throw new Exception("Product ID Expired");
					}
				}
			
				
				receiveMaterial_HM.put(IConstants.EXPIREDATE, expdate);
				receiveMaterial_HM.put(IDBConstants.CUSTNAME, CUSTNAME);
				receiveMaterial_HM.put(IDBConstants.CUSTOMER_CODE, CUST_CODE);
				if (isProductAvalable) {

					transactionHandler = _customerReturnUtil
							.process_CustomerRetunsMultiple(receiveMaterial_HM);

				} else {
					transactionHandler = transactionHandler && false;
				}
			}
			if (transactionHandler == true) {
				DbBean.CommitTran(ut);
				request.getSession().setAttribute("RESULT",
						"Products " + "Returned successfully!");
				response
						.sendRedirect("jsp/customerReturnsMultiple.jsp?action=result");
			} else {
				DbBean.RollbackTran(ut);
				request.getSession().setAttribute("RESULTERROR",
						"Error in Returning Products");
				response
						.sendRedirect("jsp/customerReturnsMultiple.jsp?action=resulterror");
			}

		} catch (Exception e) {
			DbBean.RollbackTran(ut);
			this.mLogger.exception(this.printLog, "", e);
			request.getSession().setAttribute("RESULTERROR",
					"Error:" + e.getMessage());
			response
					.sendRedirect("jsp/customerReturnsMultiple.jsp?action=resulterror");

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

}
