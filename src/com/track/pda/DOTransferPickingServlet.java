package com.track.pda;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

//import org.omg.CosTransactions._TransactionalObjectStub;

import com.track.constants.IConstants;
import com.track.constants.IDBConstants;
import com.track.constants.MLoggerConstant;
import com.track.constants.TransactionConstants;
import com.track.dao.DoDetDAO;
import com.track.dao.ItemMstDAO;
import com.track.dao.MovHisDAO;
import com.track.dao.DoTransferDetDAO;
import com.track.dao.DoTransferHdrDAO;
import com.track.db.util.DOTransferUtil;
import com.track.db.util.DOUtil;
import com.track.db.util.LocUtil;
import com.track.db.util.POUtil;
import com.track.db.util.TOUtil;
import com.track.db.util.UserLocUtil;
import com.track.util.DateUtils;
import com.track.util.IMLogger;
import com.track.util.MLogger;
import com.track.util.StrUtils;
import com.track.util.XMLUtils;

public class DOTransferPickingServlet extends HttpServlet implements IMLogger {
	private boolean printLog = MLoggerConstant.TransferOrderServlet_PRINTPLANTMASTERLOG;
	private boolean printInfo = MLoggerConstant.TransferOrderServlet_PRINTPLANTMASTERINFO;
	private static final String CONTENT_TYPE = "text/html; charset=windows-1252";
	private POUtil _POUtil = null;

	String action = "";
	String xmlStr = "";
	String PLANT = "";
	DOTransferUtil _DOTransferUtil = null;
	DoTransferHdrDAO _DoTransferHdrDAO = null;
	DoTransferDetDAO _DoTransferDetDAO = null;

	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		_POUtil = new POUtil();
		_DOTransferUtil = new DOTransferUtil();
		_DoTransferHdrDAO = new DoTransferHdrDAO();
		_DoTransferDetDAO = new DoTransferDetDAO();
	}

	// @SuppressWarnings("static-access")
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType(CONTENT_TYPE);
		PrintWriter out = response.getWriter();

		try {

			action = request.getParameter("action").trim();
			String rflag = StrUtils.fString(request.getParameter("RFLAG"));
			this.setMapDataToLogger(this.populateMapData(StrUtils
					.fString(request.getParameter("PLANT")), StrUtils
					.fString(request.getParameter("LOGIN_USER"))
					+ " PDA_USER"));
			_DOTransferUtil.setmLogger(mLogger);

			if (this.printInfo) {
				diaplayInfoLogs(request);
			}
			if (action.equalsIgnoreCase("View")) {
				boolean flag = DisplayDoTransferData(request, response);
				if (rflag.equals("2")) {

					String dono = "";
					dono = StrUtils.fString(request.getParameter("DONO"));

					request.getSession().setAttribute("dono", "");
					response.sendRedirect("jsp/DOTransferSummary.jsp?DONO="
							+ dono + "&action=View");
				}
			}

			// for PDA
			else if (action.equalsIgnoreCase("load_TransferoutBoundOrders")) {
				xmlStr = "";
				xmlStr = load_TransferoutBoundOrders(request, response);
			} else if (action
					.equalsIgnoreCase("load_TransferoutBoundOrder_item_details")) {
				xmlStr = "";
				xmlStr = load_TransferoutBoundOrder_item_details(request,
						response);
			} else if (action
					.equalsIgnoreCase("load_TransferoutBoundOrder_sup_details")) {
				xmlStr = "";
				xmlStr = load_TransferoutBoundOrder_sup_details(request,
						response);
			} else if (action.equalsIgnoreCase("GET_BATCH_CODES")) {
				xmlStr = "";
				xmlStr = getDoTransferBatchCode(request, response);
			} else if (action.equalsIgnoreCase("GET_FROM_LOC_CODES")) {
				xmlStr = "";
				xmlStr = getDoTransferFromLocCode(request, response);
			}
                        else if (action.equalsIgnoreCase("GET_TO_LOC_CODES")) {
                               xmlStr = "";
                               xmlStr = getDoTransferToLocCode(request, response);
                       }
                        else if (action.equalsIgnoreCase("process_do_Transfer")) {
				xmlStr = "";
				xmlStr = process_DoTranferOrderPickingByPDA(request, response);
			}
			//start code by Bruhan for random outbound transfer on 19 june 2013
              else if (action.equalsIgnoreCase("load_random_TransferoutBoundOrder_item_details")) {
            				xmlStr = "";
            				xmlStr = load_random_outBoundTransferOrder_item_details(request, response);
              }
            			
			
			  else if (action.equalsIgnoreCase("process_do_Transfer_Random")) {
            				xmlStr = "";
            				xmlStr = process_DoTranferOrderPickingRandomByPDA(request, response);
            }
			//End code by Bruhan for random outbound transfer on 19 june 2013
			// for PDA End
			else {
				boolean flag = DisplayDoTransferData(request, response);
				String dono = "";
				dono = StrUtils.fString(request.getParameter("DONO"));
				request.getSession().setAttribute("dono", "");
				response.sendRedirect("jsp/DoTransferSummary.jsp?DONO=" + dono
						+ "&action=View");
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			xmlStr = XMLUtils.getXMLMessage(1, e.getMessage());
		}

		out.write(xmlStr);
		out.close();
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
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

	private boolean DisplayDoTransferData(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException,
			Exception {

		String dono = StrUtils.fString(request.getParameter("DONO"));
		String fieldDesc = "";

		ArrayList al = new ArrayList();
		Hashtable htCond = new Hashtable();
		Map m = new HashMap();

		if (dono.length() > 0) {

			HttpSession session = request.getSession();
			String plant = StrUtils.fString(
					(String) session.getAttribute("PLANT")).trim();
			htCond.put("PLANT", plant);
			htCond.put("DONO", dono);

			String query = "dono,custName,custCode,jobNum,custName,personInCharge,contactNum,address,address2,address3,collectionDate,collectionTime,remark1,remark2,isnull(b.name,'') as contactname,isnull(b.telno,'') as telno,isnull(b.email,'') email,isnull(b.addr1,'') add1,isnull(b.addr2,'') add2,isnull(b.addr3,'') add3,isnull(b.remarks,'') remarks,isnull(b.addr4,'') add4,isnull(b.country,'') country,isnull(b.zip,'') zip";

			al = _DOTransferUtil.getDoTransferDetails(query, htCond);
			if (al.size() > 0) {
				m = (Map) al.get(0);

			}

		}
		request.getSession().setAttribute("podetVal", m);
		request.getSession().setAttribute("dono", dono);
		request.getSession().setAttribute("RESULT", fieldDesc);

		return true;
	}

	// Coding for pda start

	private String load_TransferoutBoundOrders(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {
		String str = "", aPlant = "", aOrdNo = "";
		try {
			// MLogger.log(0, "load_outBoundOrders() Starts ");
			PLANT = StrUtils.fString(request.getParameter("PLANT"));

			Hashtable ht = new Hashtable();
			ht.put("PLANT", PLANT);

			str = _DOTransferUtil.getOpenDoTransferOutBoundOrder(PLANT);

			if (str.equalsIgnoreCase("")) {
				str = XMLUtils.getXMLMessage(1,
						"No Transfer Outbound Orders Found");
			}

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			str = XMLUtils.getXMLMessage(1, e.getMessage());
		}

		// MLogger.log(0, "load_outBoundOrders() Ends");

		return str;
	}

	private String load_TransferoutBoundOrder_item_details(
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		String str = "", aPlant = "", aOrdNo = "";
		try {
			// MLogger.log(0, "load_outBoundOrder_item_details() Starts ");
			String PLANT = StrUtils.fString(request.getParameter("PLANT"));
			aOrdNo = StrUtils.fString(request.getParameter("ORDER_NUM"));

			Hashtable ht = new Hashtable();
			ht.put("PLANT", PLANT);
			ht.put("DONO", aOrdNo);

			str = _DOTransferUtil.getDoOutBoundOrderItemDetails(PLANT, aOrdNo,
					Boolean.FALSE);

			if (str.equalsIgnoreCase("")) {
				str = XMLUtils.getXMLMessage(1, "Item details not found?");
			}

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			str = XMLUtils.getXMLMessage(1, e.getMessage());
		}

		// MLogger.log(0, "load_outBoundOrder_item_details() Ends");

		return str;
	}

	private String load_TransferoutBoundOrder_sup_details(
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		String str = "", aPlant = "", aOrdNo = "";
		try {

			String PLANT = StrUtils.fString(request.getParameter("PLANT"));
			aOrdNo = StrUtils.fString(request.getParameter("DO_NUM"));

			str = _DOTransferUtil.getOpenDoTransferoutBoundOrderSupDetails(
					PLANT, aOrdNo);
			if (str.equals("")) {
				str = XMLUtils.getXMLMessage(1, "No Orders Found!");
			}

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			str = XMLUtils.getXMLMessage(1, e.getMessage());
		}

		return str;
	}

	private String getDoTransferBatchCode(HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		String xmlStr = "";
		try {
			String itemNo = StrUtils.fString(request.getParameter("ITEM_NUM"));
			String plant = StrUtils.fString(request.getParameter("PLANT"));
			String fromLoc = StrUtils.fString(request.getParameter("LOC"));
			String batch = StrUtils.fString(request.getParameter("BATCH"));

			xmlStr = _DOTransferUtil.getDoTransferBatchDetails(plant, itemNo,
					fromLoc, batch);
			if (xmlStr.length() == 0) {
				xmlStr = XMLUtils.getXMLMessage(1, "Batch Not found");
			}

			return xmlStr;
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		}
	}

	private String getDoTransferFromLocCode(HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		String xmlStr = "";
		try {
			
			String plant = StrUtils.fString(request.getParameter("PLANT"));
		   String userID = StrUtils.fString(request.getParameter("LOGIN_USER"));

			xmlStr = _DOTransferUtil.getDoTransferLocDetails(plant,userID);
			if (xmlStr.length() == 0) {
				xmlStr = XMLUtils.getXMLMessage(1, "Loc Not found");
			}

			return xmlStr;

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		}
	}
        
    private String getDoTransferToLocCode(HttpServletRequest request,
                    HttpServletResponse response) throws Exception {

            String xmlStr = "";
            try {
                    
                    String plant = StrUtils.fString(request.getParameter("PLANT"));
               String userID = StrUtils.fString(request.getParameter("LOGIN_USER"));

                    xmlStr = _DOTransferUtil.getDoTransferLocDetails(plant,userID);

                    if (xmlStr.length() == 0) {
                            xmlStr = XMLUtils.getXMLMessage(1, "Loc Not found");
                    }

                    return xmlStr;

            } catch (Exception e) {
                    this.mLogger.exception(this.printLog, "", e);
                    throw e;
            }
    }

/* ************Modification History*********************************
	   Oct 17 2014 Bruhan, Description: To include Issue date
	*/
	private String process_DoTranferOrderPickingByPDA(
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, Exception {

		DOUtil _DOUtil = new DOUtil();
		Map receiveMaterial_HM = null;
		String PLANT = "", DO_NUM = "", ITEM_NUM = "", DO_LN_NUM = "";
		String ITEM_DESCRIPTION = "", LOGIN_USER = "";
		String ITEM_BATCH = "", ORDER_QTY = "", ITEM_QTY = "", INV_QTY = "", ISSUED_QTY = "", PICKING_QTY = "", ITEM_EXPDATE = "", ITEM_LOC = "", FROM_LOC = "", TO_LOC = "", CUST_NAME = "";
		String CONTACT_NAME = "", TELNO = "", EMAIL = "", ADD1 = "", ADD2 = "", ADD3 = "", PICKED_QTY = "";

		try {

			PLANT = StrUtils.fString(request.getParameter("PLANT"));
			DO_NUM = StrUtils.fString(request.getParameter("DO_NUM"));
			DO_LN_NUM = StrUtils.fString(request.getParameter("DO_LN_NUM"));
			CUST_NAME = StrUtils.fString(request.getParameter("CUST_NAME"));
			ITEM_NUM = StrUtils.fString(request.getParameter("ITEM_NUM"));
			ITEM_DESCRIPTION = StrUtils.fString(request.getParameter("ITEM_DESCRIPTION"));
			LOGIN_USER = StrUtils.fString(request.getParameter("LOGIN_USER"));
			ITEM_BATCH = StrUtils.fString(request.getParameter("ITEM_BATCH"));
			ORDER_QTY = StrUtils.removeFormat(StrUtils.fString(request.getParameter("ORDER_QTY")));
			INV_QTY = StrUtils.removeFormat(StrUtils.fString(request.getParameter("INV_QTY")));
			ISSUED_QTY = StrUtils.removeFormat(StrUtils.fString(request.getParameter("ISSUED_QTY")));
			PICKED_QTY = StrUtils.removeFormat(StrUtils.fString(request.getParameter("PICKED_QTY")));
			PICKING_QTY = StrUtils.removeFormat(StrUtils.fString(request.getParameter("PICKING_QTY")));
			ITEM_EXPDATE = StrUtils.fString(request.getParameter("REMARKS"));
			ITEM_LOC = StrUtils.fString(request.getParameter("ITEM_FROMLOC"));
			FROM_LOC = StrUtils.fString(request.getParameter("ITEM_FROMLOC"));
			TO_LOC = StrUtils.fString(request.getParameter("ITEM_TOLOC"));
         	DateUtils _dateUtils = new DateUtils();
			String curDate =_dateUtils.getDate();
			String strTranDate    = curDate.substring(6)+"-"+ curDate.substring(3,5)+"-"+curDate.substring(0,2);
			String strIssueDate    = curDate.substring(0,2)+"/"+ curDate.substring(3,5)+"/"+curDate.substring(6);
              
		    UserLocUtil uslocUtil = new UserLocUtil();
		    uslocUtil.setmLogger(mLogger);
		    boolean isvalidlocforUser  =uslocUtil.isValidLocInLocmstForUser(PLANT,LOGIN_USER,ITEM_LOC);
		    if (isvalidlocforUser) {
		        isvalidlocforUser  =uslocUtil.isValidLocInLocmstForUser(PLANT,LOGIN_USER,TO_LOC);
		        
		        if(!isvalidlocforUser){
		            throw new Exception("To Loc :"+TO_LOC+" is not a User Assigned Location/Valid Location");
		          }
		    }else{
		       
		        throw new Exception("From Loc :"+ITEM_LOC+" is not a User Assigned Location/Valid Location");
		    }

			
			ITEM_DESCRIPTION = StrUtils.replaceCharacters2Recv(ITEM_DESCRIPTION);
			CUST_NAME = StrUtils.replaceCharacters2Recv(CUST_NAME);

			double orderqty = Double.parseDouble(ORDER_QTY);
			orderqty =  StrUtils.RoundDB(orderqty, IConstants.DECIMALPTS);
			ORDER_QTY = String.valueOf(orderqty);
			
			double pickingqty = Double.parseDouble(PICKING_QTY);
			pickingqty =  StrUtils.RoundDB(pickingqty, IConstants.DECIMALPTS);
			PICKING_QTY = String.valueOf(pickingqty);
			
			
			receiveMaterial_HM = new HashMap();
			receiveMaterial_HM.put(IConstants.PLANT, PLANT);
			receiveMaterial_HM.put(IConstants.ITEM, ITEM_NUM);
			receiveMaterial_HM.put(IConstants.ITEM_DESC, ITEM_DESCRIPTION);
			receiveMaterial_HM.put(IConstants.PODET_PONUM, DO_NUM);
			receiveMaterial_HM.put(IConstants.PODET_POLNNO, DO_LN_NUM);
			receiveMaterial_HM.put(IConstants.CUSTOMER_NAME, CUST_NAME);
			receiveMaterial_HM.put(IConstants.LOC, ITEM_LOC);
			receiveMaterial_HM.put(IConstants.LOC2, TO_LOC);
			receiveMaterial_HM.put(IConstants.LOGIN_USER, LOGIN_USER);
			receiveMaterial_HM.put(IConstants.CUSTOMER_CODE, _DOUtil
					.getCustCode(PLANT, DO_NUM));
			receiveMaterial_HM.put(IConstants.BATCH, ITEM_BATCH);
			receiveMaterial_HM.put(IConstants.QTY, PICKING_QTY);
			receiveMaterial_HM.put(IConstants.ORD_QTY, ORDER_QTY);
			receiveMaterial_HM.put(IConstants.INV_EXP_DATE, ITEM_EXPDATE);
			receiveMaterial_HM.put(IConstants.JOB_NUM, _DOTransferUtil
					.getJobNum(PLANT, DO_NUM));
			receiveMaterial_HM.put(IConstants.REMARKS, "");
			receiveMaterial_HM.put("INV_QTY1", "1");
			receiveMaterial_HM.put(IConstants.TRAN_DATE, strTranDate);
			receiveMaterial_HM.put(IConstants.ISSUEDATE, strIssueDate);
			xmlStr = "";

			boolean flag = true;

			if (flag) {
				;
				xmlStr = _DOTransferUtil
						.process_DoTransferPickingForPDA(receiveMaterial_HM);
			} else {
				throw new Exception(" Product Transfered already ");
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			xmlStr = XMLUtils.getXMLMessage(1, e.getMessage());
			throw e;
		}
		return xmlStr;
	}
	//start code by Bruhan for random outbound transfer on 19 june 2013
	
	private String load_random_outBoundTransferOrder_item_details(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {
		String str = "", aPlant = "", aOrdNo = "";
		try {
			// MLogger.log(0, "load_outBoundOrder_item_details() Starts ");
			PLANT = StrUtils.fString(request.getParameter("PLANT"));
			aOrdNo = StrUtils.fString(request.getParameter("ORDER_NUM"));

			Hashtable ht = new Hashtable();
			ht.put("PLANT", PLANT);
			ht.put("DONO", aOrdNo);

			str = _DOTransferUtil.getRandomOutBoundTransferOrderItemDetails(PLANT, aOrdNo,
					Boolean.FALSE);

			if (str.equalsIgnoreCase("")) {
				str = XMLUtils.getXMLMessage(1, "Item details not found?");
			}

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			str = XMLUtils.getXMLMessage(1, e.getMessage());
		}

		// MLogger.log(0, "load_outBoundOrder_item_details() Ends");

		return str;
	}
		
	
	/* ************Modification History*********************************
	   Oct 17 2014 Bruhan, Description: To include Issue date
	*/
	
	private String process_DoTranferOrderPickingRandomByPDA(
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, Exception {

		DOUtil _DOUtil = new DOUtil();
		Map receiveMaterial_HM = null;
		String PLANT = "", DO_NUM = "", ITEM_NUM = "", DO_LN_NUM = "";
		String ITEM_DESCRIPTION = "", LOGIN_USER = "";
		String ITEM_BATCH = "", ORDER_QTY = "", ITEM_QTY = "", INV_QTY = "", ISSUED_QTY = "", PICKING_QTY = "", ITEM_EXPDATE = "", ITEM_LOC = "", FROM_LOC = "", TO_LOC = "", CUST_NAME = "";
		String CONTACT_NAME = "", TELNO = "", EMAIL = "", ADD1 = "", ADD2 = "", ADD3 = "", PICKED_QTY = "";

		try {

			PLANT = StrUtils.fString(request.getParameter("PLANT"));
			DO_NUM = StrUtils.fString(request.getParameter("DO_NUM"));
			//DO_LN_NUM = StrUtils.fString(request.getParameter("DO_LN_NUM"));
			CUST_NAME = StrUtils.fString(request.getParameter("CUST_NAME"));
			ITEM_NUM = StrUtils.fString(request.getParameter("ITEM_NUM"));
			ITEM_DESCRIPTION = StrUtils.fString(request.getParameter("ITEM_DESCRIPTION"));
			LOGIN_USER = StrUtils.fString(request.getParameter("LOGIN_USER"));
			ITEM_BATCH = StrUtils.fString(request.getParameter("ITEM_BATCH"));
			ORDER_QTY = StrUtils.removeFormat(StrUtils.fString(request.getParameter("ORDER_QTY")));
			INV_QTY = StrUtils.removeFormat(StrUtils.fString(request.getParameter("INV_QTY")));
			ISSUED_QTY = StrUtils.removeFormat(StrUtils.fString(request.getParameter("ISSUED_QTY")));
			PICKED_QTY = StrUtils.removeFormat(StrUtils.fString(request.getParameter("PICKED_QTY")));
			PICKING_QTY = StrUtils.removeFormat(StrUtils.fString(request.getParameter("PICKING_QTY")));
			ITEM_EXPDATE = StrUtils.fString(request.getParameter("REMARKS"));
			ITEM_LOC = StrUtils.fString(request.getParameter("ITEM_FROMLOC"));
			FROM_LOC = StrUtils.fString(request.getParameter("ITEM_FROMLOC"));
			TO_LOC = StrUtils.fString(request.getParameter("ITEM_TOLOC"));
			DateUtils _dateUtils = new DateUtils();
			String curDate =_dateUtils.getDate();
			String strTranDate    = curDate.substring(6)+"-"+ curDate.substring(3,5)+"-"+curDate.substring(0,2);
			String strIssueDate    = curDate.substring(0,2)+"/"+ curDate.substring(3,5)+"/"+curDate.substring(6);
                        
                        
		    UserLocUtil uslocUtil = new UserLocUtil();
		    uslocUtil.setmLogger(mLogger);
		    boolean isvalidlocforUser  =uslocUtil.isValidLocInLocmstForUser(PLANT,LOGIN_USER,ITEM_LOC);
		    if (isvalidlocforUser) {
		        isvalidlocforUser  =uslocUtil.isValidLocInLocmstForUser(PLANT,LOGIN_USER,TO_LOC);
		        
		        if(!isvalidlocforUser){
		            throw new Exception("To Loc :"+TO_LOC+" is not a User Assigned Location/Valid Location");
		          }
		    }else{
		       
		        throw new Exception("From Loc :"+ITEM_LOC+" is not a User Assigned Location/Valid Location");
		    }

			
			ITEM_DESCRIPTION = StrUtils.replaceCharacters2Recv(ITEM_DESCRIPTION);
			CUST_NAME = StrUtils.replaceCharacters2Recv(CUST_NAME);

			//double orderqty = Double.parseDouble(ORDER_QTY);
			//orderqty =  StrUtils.RoundDB(orderqty, IConstants.DECIMALPTS);
			//ORDER_QTY = String.valueOf(orderqty);
			
			double pickingqty = Double.parseDouble(PICKING_QTY);
			pickingqty =  StrUtils.RoundDB(pickingqty, IConstants.DECIMALPTS);
			PICKING_QTY = String.valueOf(pickingqty);
			
			
			receiveMaterial_HM = new HashMap();
			receiveMaterial_HM.put(IConstants.PLANT, PLANT);
			receiveMaterial_HM.put(IConstants.ITEM, ITEM_NUM);
			receiveMaterial_HM.put(IConstants.ITEM_DESC, ITEM_DESCRIPTION);
			receiveMaterial_HM.put(IConstants.DODET_DONUM, DO_NUM);
			receiveMaterial_HM.put(IConstants.CUSTOMER_NAME, CUST_NAME);
			receiveMaterial_HM.put(IConstants.LOC, ITEM_LOC);
			receiveMaterial_HM.put(IConstants.LOC2, TO_LOC);
			receiveMaterial_HM.put(IConstants.LOGIN_USER, LOGIN_USER);
			receiveMaterial_HM.put(IConstants.CUSTOMER_CODE, _DOUtil
					.getCustCode(PLANT, DO_NUM));
			receiveMaterial_HM.put(IConstants.BATCH, ITEM_BATCH);
			receiveMaterial_HM.put(IConstants.QTY, PICKING_QTY);
			receiveMaterial_HM.put(IConstants.ORD_QTY, ORDER_QTY);
			receiveMaterial_HM.put(IConstants.INV_EXP_DATE, ITEM_EXPDATE);
			receiveMaterial_HM.put(IConstants.JOB_NUM, _DOTransferUtil
					.getJobNum(PLANT, DO_NUM));
			receiveMaterial_HM.put(IConstants.REMARKS, "");
			receiveMaterial_HM.put("INV_QTY1", "1");
			receiveMaterial_HM.put(IConstants.TRAN_DATE, strTranDate);
			receiveMaterial_HM.put(IConstants.ISSUEDATE, strIssueDate);
			xmlStr = "";

			boolean flag = true;

			if (flag) {
				;
				xmlStr = _DOTransferUtil.process_DoTransferPickingForPDA_Random(receiveMaterial_HM);
				
			} else {
				throw new Exception(" Product Transfered already ");
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			xmlStr = XMLUtils.getXMLMessage(1, e.getMessage());
			throw e;
		}
		return xmlStr;
	}
	
	//End code by Bruhan for random outbound transfer on 19 june 2013

}
