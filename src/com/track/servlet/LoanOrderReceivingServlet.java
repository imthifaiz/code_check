package com.track.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Struct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.track.constants.IConstants;
import com.track.constants.MLoggerConstant;
import com.track.dao.LoanDetDAO;
import com.track.db.util.InvMstUtil;
import com.track.db.util.LoanUtil;
import com.track.db.util.UserLocUtil;
import com.track.util.DateUtils;
import com.track.util.IMLogger;
import com.track.util.MLogger;
import com.track.util.StrUtils;
import com.track.util.XMLUtils;

public class LoanOrderReceivingServlet extends HttpServlet implements IMLogger {
	private boolean printLog = MLoggerConstant.LoanOrderReceivingServlet_PRINTPLANTMASTERLOG;
	private boolean printInfo = MLoggerConstant.LoanOrderReceivingServlet_PRINTPLANTMASTERINFO;
	private static final long serialVersionUID = -1625127959017913766L;

	LoanUtil _loanUtil = null;
	StrUtils strUtils =null;
	InvMstUtil _InvMstUtil = null;
	DateUtils dateUtils = null;

	String PLANT = "", REFTYPE = "", REFLNNO = "", USERNAME = "", BINNO = "",
			COMPANY = "";

	String xmlStr = "";
	String action = "";

	private static final String CONTENT_TYPE = "text/xml";

	public void init() throws ServletException {
		_loanUtil = new LoanUtil();
		_InvMstUtil = new InvMstUtil();
	     strUtils = new StrUtils();
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType(CONTENT_TYPE);
		PrintWriter out = response.getWriter();

		try {
			action = request.getParameter("action").trim();
			String plant = StrUtils.fString(
					(String) request.getSession().getAttribute("PLANT")).trim();
			String userName = StrUtils.fString(
					(String) request.getSession().getAttribute("LOGIN_USER"))
					.trim();
			this.setMapDataToLogger(this.populateMapData(plant, userName));
			_loanUtil.setmLogger(mLogger);
			_InvMstUtil.setmLogger(mLogger);
			if (this.printInfo) {
				diaplayInfoLogs(request);
			}
			if (action.equalsIgnoreCase("GO")) {
				boolean flag = DisplayLoanOrderHdrData(request, response);
				String dono = "";
				dono = StrUtils.fString(request.getParameter("DONO"));
				request.getSession().setAttribute("dono", "");
				response.sendRedirect("jsp/LoanOrderPicking.jsp?DONO=" + dono
						+ "&action=GO");
			} else if (action.equalsIgnoreCase("View")) {
				boolean flag = DisplayLoanOrderHdrData(request, response);
				String dono = "";
				dono = StrUtils.fString(request.getParameter("DONO"));
				request.getSession().setAttribute("dono", "");
				response.sendRedirect("jsp/LoanOrderReceiving.jsp?DONO=" + dono
						+ "&action=View");
			}

			else if (action.equalsIgnoreCase("LoanOrderPickingReverse")) {
				String xmlStr = "";
			}

			else if (action.equalsIgnoreCase("Receiving")) {
				String xmlStr = "";

				xmlStr = LoanOrderReceivingData(request, response);
			} else if (action.equalsIgnoreCase("Receiving Confirm")) {
				String xmlStr = "";

				xmlStr = process_LoanRecvConfirm(request, response);
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

	public void destroy() {
	}

	private String LoanOrderReceivingData(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException,
			Exception {

		String sepratedtoken1 = "";
		LoanDetDAO _loDetDAO = new LoanDetDAO();
		_loDetDAO.setmLogger(mLogger);
		StrUtils StrUtils = new StrUtils();
		Map receiveMaterial_HM = null;

		Map mp = new HashMap();
		String PLANT = "", DO_NUM = "", DO_LN_NUM = "", ITEM_NUM = "";
		String ITEM_DESCRIPTION = "", LOGIN_USER = "", CUST_NAME = "";

		String PICKED_QTY = "", RECV_QTY = "", DO_BATCH = "", LOC = "", ORDER_QTY = "", FRLOC = "", TOLOC = "",UOM="",UOMQTY="";

		try {
			String sepratedtoken = "";
			String totalString = StrUtils.fString(request
					.getParameter("TRAVELER"));
			StringTokenizer parser = new StringTokenizer(totalString, "=");
			while (parser.hasMoreTokens())

			{
				int count = 1;
				sepratedtoken = parser.nextToken();
				StringTokenizer parser1 = new StringTokenizer(sepratedtoken,
						",");

				while (parser1.hasMoreTokens())

				{
					sepratedtoken1 = parser1.nextToken();
					mp.put("data" + count, sepratedtoken1);
					count++;

				}

				HttpSession session = request.getSession();
				PLANT = StrUtils.fString((String) session.getAttribute("PLANT")).trim();
				DO_NUM = StrUtils.fString((String) mp.get("data1"));
				DO_LN_NUM = StrUtils.fString((String) mp.get("data2"));
				ITEM_NUM = StrUtils.fString((String) mp.get("data3"));
				ITEM_DESCRIPTION = StrUtils.fString((String) mp.get("data4"));
				if (ITEM_DESCRIPTION.equalsIgnoreCase("NOITEMDESC")) {
					ITEM_DESCRIPTION = "";
				}
				ORDER_QTY = StrUtils.fString((String) mp.get("data5"));
				PICKED_QTY = StrUtils.fString((String) mp.get("data6"));
				RECV_QTY = StrUtils.fString((String) mp.get("data7"));
				LOGIN_USER = StrUtils.fString((String) mp.get("data8"));
				FRLOC = StrUtils.fString((String) mp.get("data9"));
				TOLOC = StrUtils.fString((String) mp.get("data10"));
				CUST_NAME = StrUtils.fString((String) mp.get("data11"));
				UOM = StrUtils.fString((String) mp.get("data12"));
				UOMQTY = StrUtils.fString((String) mp.get("data13"));
                                double availQty = Double.parseDouble(PICKED_QTY)-Double.parseDouble(RECV_QTY);

				receiveMaterial_HM = new HashMap();
				receiveMaterial_HM.put(IConstants.PLANT, PLANT);
				receiveMaterial_HM.put(IConstants.ITEM, ITEM_NUM);
				receiveMaterial_HM.put(IConstants.QTY_ISSUE, PICKED_QTY);
				receiveMaterial_HM.put(IConstants.LOANDET_ORDNO, DO_NUM);
				receiveMaterial_HM.put(IConstants.LOANDET_ORDLNNO, DO_LN_NUM);
				receiveMaterial_HM.put(IConstants.LOC, LOC);
				receiveMaterial_HM.put(IConstants.LOGIN_USER, LOGIN_USER);
				receiveMaterial_HM.put(IConstants.CUSTOMER_CODE, _loanUtil
						.getCustCode(PLANT, DO_NUM));

				// getting job No for do
				receiveMaterial_HM.put(IConstants.JOB_NUM, _loanUtil.getJobNum(
						PLANT, DO_NUM));
				receiveMaterial_HM.put(IConstants.LOC2, DO_BATCH);

				xmlStr = "";

				List listQry = _loDetDAO.getLoanOrderReceivingDetails(PLANT,
						DO_NUM);
				request.getSession().setAttribute("customerlistqry3", listQry);
				response
						.sendRedirect("jsp/LoanOrderMultipleReceiving.jsp?ORDERNO="
								+ DO_NUM
								+ "&ORDERLNO="
								+ DO_LN_NUM
								+ "&CUSTNAME="
								+ StrUtils.replaceCharacters2Send(CUST_NAME)
								+ "&ITEMNO="
								+ ITEM_NUM
								+ "&ITEMDESC="
								+ StrUtils.replaceCharacters2Send(ITEM_DESCRIPTION)
								+ "&ORDERQTY="
								+ ORDER_QTY
								+ "&PICKED_QTY="
								+ PICKED_QTY
								+ "&UOM="
								+ UOM
								+ "&UOMQTY="
								+ UOMQTY
								+ "&RECVED_QTY="
								+ RECV_QTY
								+ "&FRLOC=" + FRLOC + "&TOLOC=" + TOLOC+"&BATCH=NOBATCH"+"&QTY="+StrUtils.displayDecimal(Double.toString(availQty)));

			}
		}

		catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			request.getSession().setAttribute("CATCHERROR",
					"Error in Receving Product ID!");
			response
					.sendRedirect("jsp/LoanOrderReceiving.jsp?action=View&PLANT="
							+ PLANT + "&DONO=" + DO_NUM);
			throw e;
		}

		return xmlStr;
	}

	/* ************Modification History*********************************
	   Bruhan,Oct 14 2014, Description: To include Transaction date
	*/
	private String process_LoanRecvConfirm(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException,
			Exception {

		LoanDetDAO _loDetDAO = new LoanDetDAO();
		_loDetDAO.setmLogger(mLogger);
		Map receiveMaterial_HM = null;
		String PLANT = "", ORD_NUM = "", ITEM_NUM = "", ORD_LN_NUM = "";
		String ITEM_DESCRIPTION = "", LOGIN_USER = "",REMARKS="";
		String ITEM_BATCH = "", ORDER_QTY = "", RECVED_QTY = "", ITEM_QTY = "", INV_QTY = "", RECVQTY = "", PICKED_QTY = "", ITEM_EXPDATE = "", LOC = "", 
		LOC1 = "", CUST_NAME = "",TRANSACTIONDATE = "",strMovHisTranDate="",strTranDate="",UOMQTY="",UOM="";
		try {
			HttpSession session = request.getSession();
			PLANT = StrUtils.fString((String) session.getAttribute("PLANT")).trim();
			ORD_NUM = StrUtils.fString(request.getParameter("ORDERNO"));
			ORD_LN_NUM = StrUtils.fString(request.getParameter("ORDERLNO"));
			CUST_NAME = StrUtils.fString(request.getParameter("CUSTNAME"));
			ITEM_NUM = StrUtils.fString(request.getParameter("ITEMNO"));
			ITEM_DESCRIPTION = StrUtils.fString(request.getParameter("ITEMDESC"));
			LOGIN_USER = StrUtils.fString(request.getParameter("LOGIN_USER"));
			ITEM_BATCH = StrUtils.fString(request.getParameter("BATCH"));
			ORDER_QTY = StrUtils.removeFormat(StrUtils.fString(request.getParameter("ORDERQTY")));
			RECVED_QTY = StrUtils.fString(request.getParameter("RECVED_QTY"));
			INV_QTY = StrUtils.removeFormat(StrUtils.fString(request.getParameter("INVQTY")));
			ITEM_QTY = StrUtils.removeFormat(StrUtils.fString(request.getParameter("QTY")));
			PICKED_QTY = StrUtils.removeFormat(StrUtils.fString(request.getParameter("PICKED_QTY")));
			
			ITEM_EXPDATE = StrUtils.fString(request.getParameter("REF"));
			LOC = StrUtils.fString(request.getParameter("LOC"));
			LOC1 = StrUtils.fString(request.getParameter("LOC1"));
			REMARKS = StrUtils.fString(request.getParameter("REF"));
			UOMQTY = StrUtils.removeFormat(StrUtils.fString(request.getParameter("UOMQTY")));
			UOM = StrUtils.removeFormat(StrUtils.fString(request.getParameter("UOM")));
				TRANSACTIONDATE = StrUtils.fString(request.getParameter("TRANSACTIONDATE"));
			if (TRANSACTIONDATE.length()>5)
				strMovHisTranDate    = TRANSACTIONDATE.substring(6)+"-"+TRANSACTIONDATE.substring(3,5)+"-"+TRANSACTIONDATE.substring(0,2);
			    strTranDate    = TRANSACTIONDATE.substring(0,2)+"/"+ TRANSACTIONDATE.substring(3,5)+"/"+TRANSACTIONDATE.substring(6);
			String DYNAMIC_RECEIVING_SIZE = StrUtils.fString(request.getParameter("DYNAMIC_PICKING_SIZE"));
			int DYNAMIC_RECEIVING_SIZE_INT = Integer.parseInt(DYNAMIC_RECEIVING_SIZE);
									
			boolean flag = false;
			//flag = _loanUtil.process_LoanOrderReceving(receiveMaterial_HM);
			for (int indexFlow = 0; indexFlow < DYNAMIC_RECEIVING_SIZE_INT; indexFlow++) {
				LOC1=StrUtils.fString(request.getParameter("LOC1"+"_"+indexFlow));
				ITEM_BATCH=StrUtils.fString(request.getParameter("BATCH"+"_"+indexFlow));
				RECVQTY=StrUtils.removeFormat(StrUtils.fString(request.getParameter("PICKINGQTY"+"_"+indexFlow)));
				double receivingQty = Double.parseDouble(((String) RECVQTY.trim()
						.toString()));
				receivingQty = strUtils.RoundDB(receivingQty, IConstants.DECIMALPTS);
				RECVQTY = String.valueOf(receivingQty);
				RECVQTY = StrUtils.formatThreeDecimal(RECVQTY);
				receiveMaterial_HM = new HashMap();
				receiveMaterial_HM.put(IConstants.PLANT, PLANT);
				receiveMaterial_HM.put(IConstants.ITEM, ITEM_NUM);
				receiveMaterial_HM.put(IConstants.ITEM_DESC,strUtils.InsertQuotes( ITEM_DESCRIPTION));
				receiveMaterial_HM.put(IConstants.LOANDET_ORDNO, ORD_NUM);
				receiveMaterial_HM.put(IConstants.LOANDET_ORDLNNO, ORD_LN_NUM);
				receiveMaterial_HM.put(IConstants.CUSTOMER_NAME, CUST_NAME);
				receiveMaterial_HM.put(IConstants.LOC, LOC);
				receiveMaterial_HM.put(IConstants.LOC1, LOC1);
				receiveMaterial_HM.put(IConstants.LOGIN_USER, LOGIN_USER);
				receiveMaterial_HM.put(IConstants.CUSTOMER_CODE, _loanUtil.getCustCode(PLANT, ORD_NUM));
				receiveMaterial_HM.put(IConstants.BATCH, ITEM_BATCH);
				receiveMaterial_HM.put(IConstants.RECV_QTY, RECVQTY);
				receiveMaterial_HM.put(IConstants.ORD_QTY, ORDER_QTY);
				receiveMaterial_HM.put(IConstants.INV_EXP_DATE, ITEM_EXPDATE);
				receiveMaterial_HM.put(IConstants.JOB_NUM, _loanUtil.getJobNum(	PLANT, ORD_NUM));
				receiveMaterial_HM.put(IConstants.REMARKS, REMARKS);
				receiveMaterial_HM.put("INV_QTY1", "1");
				receiveMaterial_HM.put(IConstants.TRAN_DATE, strMovHisTranDate);
    			receiveMaterial_HM.put(IConstants.RECVDATE, strTranDate);
				receiveMaterial_HM.put("UOM", UOM);
    			receiveMaterial_HM.put("UOMQTY", UOMQTY);
				xmlStr = "";

			// check for item in location
				Hashtable htLocMst = new Hashtable();
				htLocMst.put("plant", receiveMaterial_HM.get(IConstants.PLANT));
				htLocMst.put("item", receiveMaterial_HM.get(IConstants.ITEM));
				htLocMst.put("loc", receiveMaterial_HM.get(IConstants.LOC));
			
                        UserLocUtil uslocUtil = new UserLocUtil();
                        uslocUtil.setmLogger(mLogger);
                        boolean isvalidlocforUser  =uslocUtil.isValidLocInLocmstForUser(PLANT,LOGIN_USER,LOC1);
                        if(!isvalidlocforUser){
                            throw new Exception(" Loc :"+LOC1+" is not User Assigned Location");
                        }
                       
                        flag = _loanUtil.process_LoanOrderReceving(receiveMaterial_HM);
			}
			
			if (flag) {
				request.getSession()
						.setAttribute(
								"RESULT",
								"Product ID : " + ITEM_NUM
										+ "  Received successfully!");
				response
						.sendRedirect("jsp/LoanOrderReceiving.jsp?action=View&PLANT="
								+ PLANT + "&DONO=" + ORD_NUM);
			} else {

				List listQry = _loDetDAO.getLoanOrderReceivingDetails(PLANT,
						ORD_NUM);
				request.getSession().setAttribute("customerlistqry3", listQry);
				request.getSession().setAttribute("RESULTERROR",
						"Product ID failed to Receive : " + ITEM_NUM);
				response
						.sendRedirect("jsp/LoanOrderMultipleReceiving.jsp?action=resulterror&ORDERNO="
								+ ORD_NUM
								+ "&ORDERLNO="
								+ ORD_LN_NUM
								+ "&CUSTNAME="
								+ CUST_NAME
								+ "&ITEMNO="
								+ ITEM_NUM
								+ "&BATCH="
								+ ITEM_BATCH
								+ "&ITEMDESC="
								+ ITEM_DESCRIPTION
								+ "&ORDERQTY="
								+ ORDER_QTY
								+ "&PICKED_QTY="
								+ PICKED_QTY
								+ "&RECVQTY="
								+ RECVQTY
								+ "&QTY="
								+ ITEM_QTY + "&FRLOC=" + LOC + "&TOLOC=" + LOC1);

			}

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			List listQry = _loDetDAO.getLoanOrderReceivingDetails(PLANT,
					ORD_NUM);
			request.getSession().setAttribute("customerlistqry3", listQry);
			request.getSession().setAttribute("QTYERROR", e.getMessage());
			response
					.sendRedirect("jsp/LoanOrderMultipleReceiving.jsp?action=qtyerror&ORDERNO="
							+ ORD_NUM
							+ "&ORDERLNO="
							+ ORD_LN_NUM
							+ "&CUSTNAME="
							+ CUST_NAME
							+ "&ITEMNO="
							+ ITEM_NUM
							+ "&BATCH="
							+ ITEM_BATCH
							+ "&ITEMDESC="
							+ ITEM_DESCRIPTION
							+ "&ORDERQTY="
							+ ORDER_QTY
							+ "&PICKED_QTY="
							+ PICKED_QTY
							+ "&RECVQTY="
							+ RECVQTY
							+ "&QTY="
							+ ITEM_QTY
							+ "&FRLOC="
							+ LOC
							+ "&TOLOC=" + LOC1);

			throw e;
		}
		return xmlStr;
	}

	private boolean DisplayLoanOrderHdrData(HttpServletRequest request,
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

			String query = "ordno as dono,loc as toLoc,loc1 as frLoc,custName,custCode,jobNum,custName,personInCharge,contactNum,address,address2,address3,collectionDate,collectionTime,remark1,remark2,isnull(b.name,'') as contactname,isnull(b.telno,'') as telno,isnull(b.email,'') email,isnull(b.addr1,'') add1,isnull(b.addr2,'') add2,isnull(b.addr3,'') add3,isnull(b.remarks,'') remarks,isnull(b.addr4,'') add4,isnull(b.country,'') country,isnull(b.zip,'') zip";

			al = _loanUtil.getLoanHdrDetails(query, htCond);
			if (al.size() > 0) {
				m = (Map) al.get(0);

			}

		}
		request.getSession().setAttribute("podetVal", m);
		request.getSession().setAttribute("dono", dono);
		request.getSession().setAttribute("RESULT", fieldDesc);
		return true;
	}

	private String process_LoanRecvConfirmPDA(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException,
			Exception {

		Map receiveMaterial_HM = null;
		String PLANT = "", ORD_NUM = "", ITEM_NUM = "", ORD_LN_NUM = "";
		String ITEM_DESCRIPTION = "", LOGIN_USER = "";
		String ITEM_BATCH = "", ORDER_QTY = "", RECVED_QTY = "", ITEM_QTY = "", INV_QTY = "", RECVQTY = "", PICKED_QTY = "", ITEM_EXPDATE = "", LOC = "", LOC1 = "", CUST_NAME = "";
		try {
			HttpSession session = request.getSession();

			PLANT = StrUtils.fString((String) session.getAttribute("PLANT"))
					.trim();
			ORD_NUM = StrUtils.fString(request.getParameter("ORDERNO"));
			ORD_LN_NUM = StrUtils.fString(request.getParameter("ORDERLNO"));
			CUST_NAME = StrUtils.fString(request.getParameter("CUSTNAME"));
			ITEM_NUM = StrUtils.fString(request.getParameter("ITEMNO"));
			ITEM_DESCRIPTION = StrUtils.fString(request
					.getParameter("ITEMDESC"));
			LOGIN_USER = StrUtils.fString(request.getParameter("LOGIN_USER"));

			ITEM_BATCH = StrUtils.fString(request.getParameter("BATCH"));
			ORDER_QTY = StrUtils.fString(request.getParameter("ORDERQTY"));
			RECVED_QTY = StrUtils.fString(request.getParameter("RECVED_QTY"));
			INV_QTY = StrUtils.fString(request.getParameter("INVQTY"));
			ITEM_QTY = StrUtils.fString(request.getParameter("QTY"));
			PICKED_QTY = StrUtils.fString(request.getParameter("PICKED_QTY"));
			RECVQTY = StrUtils.fString(request.getParameter("RECVQTY"));

			ITEM_EXPDATE = StrUtils.fString(request.getParameter("REF"));

			LOC = StrUtils.fString(request.getParameter("LOC"));
			LOC1 = StrUtils.fString(request.getParameter("LOC1"));

			receiveMaterial_HM = new HashMap();
			receiveMaterial_HM.put(IConstants.PLANT, PLANT);
			receiveMaterial_HM.put(IConstants.ITEM, ITEM_NUM);
			receiveMaterial_HM.put(IConstants.ITEM_DESC, ITEM_DESCRIPTION);
			receiveMaterial_HM.put(IConstants.LOANDET_ORDNO, ORD_NUM);
			receiveMaterial_HM.put(IConstants.LOANDET_ORDLNNO, ORD_LN_NUM);
			receiveMaterial_HM.put(IConstants.CUSTOMER_NAME, CUST_NAME);
			receiveMaterial_HM.put(IConstants.LOC, LOC);
			receiveMaterial_HM.put(IConstants.LOC1, LOC1);
			receiveMaterial_HM.put(IConstants.LOGIN_USER, LOGIN_USER);
			receiveMaterial_HM.put(IConstants.CUSTOMER_CODE, _loanUtil
					.getCustCode(PLANT, ORD_NUM));
			receiveMaterial_HM.put(IConstants.BATCH, ITEM_BATCH);
			receiveMaterial_HM.put(IConstants.RECV_QTY, RECVQTY);
			receiveMaterial_HM.put(IConstants.ORD_QTY, ORDER_QTY);
			receiveMaterial_HM.put(IConstants.INV_EXP_DATE, ITEM_EXPDATE);

			// GET JOBNUM
			receiveMaterial_HM.put(IConstants.JOB_NUM, _loanUtil.getJobNum(
					PLANT, ORD_NUM));

			xmlStr = "";
			boolean flag = false;
			flag = _loanUtil.process_LoanOrderReceving(receiveMaterial_HM);
			if (flag == true) {
				xmlStr = XMLUtils.getXMLMessage(1, "Product  : "
						+ receiveMaterial_HM.get(IConstants.ITEM)
						+ "  Received successfully!");
			} else {
				xmlStr = XMLUtils.getXMLMessage(0,
						"Error in Receiving Product ID : "
								+ receiveMaterial_HM.get("item") + " Order");
			}

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		}
		return xmlStr;
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
