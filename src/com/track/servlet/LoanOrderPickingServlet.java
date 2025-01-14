package com.track.servlet;

import java.io.IOException;
import java.io.PrintWriter;
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
import com.track.constants.IDBConstants;
import com.track.constants.MLoggerConstant;
import com.track.dao.InvMstDAO;
import com.track.dao.LoanDetDAO;
import com.track.db.util.InvMstUtil;
import com.track.db.util.LoanUtil;
import com.track.db.util.UserLocUtil;
import com.track.util.DateUtils;
import com.track.util.IMLogger;
import com.track.util.MLogger;
import com.track.util.StrUtils;
import com.track.util.XMLUtils;

public class LoanOrderPickingServlet extends HttpServlet implements IMLogger {
	private boolean printLog = MLoggerConstant.LoanOrderPickingServlet_PRINTPLANTMASTERLOG;
	private boolean printInfo = MLoggerConstant.LoanOrderPickingServlet_PRINTPLANTMASTERINFO;

	LoanUtil _loanUtil = null;

	InvMstUtil _InvMstUtil = null;
	DateUtils dateUtils = null;

	// UserTransaction ut;
	String PLANT = "", REFTYPE = "", REFLNNO = "", USERNAME = "", BINNO = "",
			COMPANY = "";

	String xmlStr = "";
	String action = "";
	
	StrUtils strUtils = new StrUtils();

	private static final String CONTENT_TYPE = "text/xml";

	public void init() throws ServletException {
		_loanUtil = new LoanUtil();

		_InvMstUtil = new InvMstUtil();
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
				response.sendRedirect("jsp/LoanOrderPicking.jsp?DONO=" + dono
						+ "&action=View");
			}

			else if (action.equalsIgnoreCase("LoanOrderPickingReverse")) {
				String xmlStr = "";
			}

			else if (action.equalsIgnoreCase("Pick/Issue")) {
				String xmlStr = "";

				xmlStr = LoanOrderPickingData(request, response);
			} else if (action.equalsIgnoreCase("Pick/Issue Confirm")) {
				String xmlStr = "";

				xmlStr = process_LoanPickConfirm(request, response);
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

	private String LoanOrderPickingData(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException,
			Exception {

		String sepratedtoken1 = "";
	    String  AvailQty="0";
		LoanDetDAO _loDetDAO = new LoanDetDAO();
		_loDetDAO.setmLogger(mLogger);

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
				PLANT = StrUtils
						.fString((String) session.getAttribute("PLANT")).trim();
				DO_NUM = StrUtils.fString((String) mp.get("data1"));
				DO_LN_NUM = StrUtils.fString((String) mp.get("data2"));
				ITEM_NUM = StrUtils.fString((String) mp.get("data3"));
				ITEM_DESCRIPTION = StrUtils.fString((String) mp.get("data4"));
				if (ITEM_DESCRIPTION.equalsIgnoreCase("NOITEMDESC")) {
					ITEM_DESCRIPTION = "";
				}
				ORDER_QTY = StrUtils.fString((String) mp.get("data5"));
				PICKED_QTY = StrUtils.fString((String) mp.get("data6"));
                                 //availQty = Double.parseDouble(ORDER_QTY)-Double.parseDouble(PICKED_QTY);
				RECV_QTY = StrUtils.fString((String) mp.get("data7"));
				LOGIN_USER = StrUtils.fString((String) mp.get("data8"));
				FRLOC = StrUtils.fString((String) mp.get("data9"));
				TOLOC = StrUtils.fString((String) mp.get("data10"));
				CUST_NAME = StrUtils.fString((String) mp.get("data11"));
				UOM = StrUtils.fString((String) mp.get("data12"));
				UOMQTY = StrUtils.fString((String) mp.get("data13"));
				
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
                                
                                try{
                                     AvailQty = new InvMstUtil().getAvailableInventoryQty(PLANT,ITEM_NUM,FRLOC,"NOBATCH",LOGIN_USER);
                                     if( Double.valueOf(AvailQty)>0)
                                     {
                                    	 
                                    	 float x = Float.valueOf(AvailQty) / Float.valueOf(UOMQTY);
                                    	 int y = (int) x;
                                    	 AvailQty=String.valueOf(y);
                                     }
                                }catch(Exception e){
                                    this.mLogger.exception(this.printLog, "", e);
                                }

				xmlStr = "";

				List listQry = _loDetDAO.getLoanOrderPickDetails(PLANT, DO_NUM);
				request.getSession().setAttribute("customerlistqry3", listQry);
				response.sendRedirect("jsp/LoanOrderMultiplePicking.jsp?ORDERNO="
						+ DO_NUM + "&ORDERLNO=" + DO_LN_NUM + "&ITEMNO=" + ITEM_NUM + "&ITEMDESC="
						+ strUtils.replaceCharacters2Send(ITEM_DESCRIPTION) + "&ORDERQTY=" + ORDER_QTY
						+ "&PICKED_QTY=" + PICKED_QTY + "&FRLOC=" + FRLOC + "&UOM=" + UOM + "&UOMQTY=" + UOMQTY
						+ "&TOLOC=" + TOLOC +"&BATCH=NOBATCH"+"&QTY="+AvailQty);

			}
		}

		catch (Exception e) {

			this.mLogger.exception(this.printLog, "", e);

			request.getSession().setAttribute("CATCHERROR",
					"Error in Picking Product ID!");
			response.sendRedirect("jsp/LoanOrderPicking.jsp?action=View&PLANT="
					+ PLANT + "&DONO=" + DO_NUM);
			throw e;
		}

		return xmlStr;
	}

	/* ************Modification History*********************************
	   Oct 15 2014 Bruhan, Description: To include Transaction date
	*/
	private String process_LoanPickConfirm(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException,
			Exception {
		LoanDetDAO _loDetDAO = new LoanDetDAO();
		_loDetDAO.setmLogger(mLogger);
		Map pick_HM = null;
		String PLANT = "", ORD_NUM = "", ITEM_NUM = "", ORDERLNO = "";
		String ITEM_DESCRIPTION = "", LOGIN_USER = "";
		String ITEM_BATCH = "",BATCH_ID="-1", ORDER_QTY = "", ITEM_QTY = "", INV_QTY = "", PICKING_QTY = "",REMARKS="", 
			   ITEM_EXPDATE = "", LOC = "", LOC1 = "", CUST_NAME = "",TRANSACTIONDATE = "",strMovHisTranDate="",strTranDate="",UOMQTY="",UOM="";
		try {
			HttpSession session = request.getSession();
			PLANT = StrUtils.fString((String) session.getAttribute("PLANT")).trim();
			ORD_NUM = StrUtils.fString(request.getParameter("ORDERNO"));
			ORDERLNO = StrUtils.fString(request.getParameter("ORDERLNO"));
			CUST_NAME = StrUtils.fString(request.getParameter("CUSTNAME"));
			ITEM_NUM = StrUtils.fString(request.getParameter("ITEMNO"));
			ITEM_DESCRIPTION = StrUtils.fString(request.getParameter("ITEMDESC"));
			LOGIN_USER = StrUtils.fString(request.getParameter("LOGIN_USER"));
			ITEM_BATCH = StrUtils.fString(request.getParameter("BATCH"));
			BATCH_ID  = StrUtils.fString(request.getParameter("BATCH_ID"));
			ORDER_QTY = StrUtils.removeFormat(StrUtils.fString(request.getParameter("ORDERQTY")));
			INV_QTY = StrUtils.removeFormat( StrUtils.fString(request.getParameter("INVQTY")));
			ITEM_QTY = StrUtils.removeFormat(StrUtils.fString(request.getParameter("QTY")));
			//PICKING_QTY = StrUtils.removeFormat(StrUtils.fString(request.getParameter("PICKINGQTY")));
			ITEM_EXPDATE = StrUtils.fString(request.getParameter("REF"));
		    REMARKS = StrUtils.fString(request.getParameter("REF"));
			LOC = StrUtils.fString(request.getParameter("LOC"));
			LOC1 = StrUtils.fString(request.getParameter("LOC1"));
			TRANSACTIONDATE = StrUtils.fString(request.getParameter("TRANSACTIONDATE"));
			UOMQTY = StrUtils.removeFormat(StrUtils.fString(request.getParameter("UOMQTY")));
			UOM = StrUtils.removeFormat(StrUtils.fString(request.getParameter("UOM")));
			if (TRANSACTIONDATE.length()>5)
				strMovHisTranDate    = TRANSACTIONDATE.substring(6)+"-"+TRANSACTIONDATE.substring(3,5)+"-"+TRANSACTIONDATE.substring(0,2);
			    strTranDate    = TRANSACTIONDATE.substring(0,2)+"/"+ TRANSACTIONDATE.substring(3,5)+"/"+TRANSACTIONDATE.substring(6);
		  String DYNAMIC_RECEIVING_SIZE = StrUtils.fString(request.getParameter("DYNAMIC_PICKING_SIZE"));
			int DYNAMIC_RECEIVING_SIZE_INT = Integer.parseInt(DYNAMIC_RECEIVING_SIZE);

			xmlStr = "";
			boolean flag = false;
			String msgval="";
			for (int indexFlow = 0; indexFlow < DYNAMIC_RECEIVING_SIZE_INT; indexFlow++) {
				LOC1=StrUtils.fString(request.getParameter("LOC1"+"_"+indexFlow));
				ITEM_BATCH=StrUtils.fString(request.getParameter("BATCH"+"_"+indexFlow));
				BATCH_ID=StrUtils.fString(request.getParameter("BATCH_ID"+"_"+indexFlow));
				PICKING_QTY=StrUtils.removeFormat(StrUtils.fString(request.getParameter("PICKINGQTY"+"_"+indexFlow)));
				//REMARKS=StrUtils.fString(request.getParameter("REMARKS"+"_"+indexFlow));
				double receivingQty = Double.parseDouble(((String) PICKING_QTY.trim().toString()));
				receivingQty = strUtils.RoundDB(receivingQty, IConstants.DECIMALPTS);
				PICKING_QTY = String.valueOf(receivingQty);
				PICKING_QTY = StrUtils.formatThreeDecimal(PICKING_QTY);
											
				pick_HM = new HashMap();
				pick_HM.put(IConstants.PLANT, PLANT);
				pick_HM.put(IConstants.ITEM, ITEM_NUM);
				pick_HM.put(IConstants.ITEM_DESC, strUtils.InsertQuotes(ITEM_DESCRIPTION));
				pick_HM.put(IConstants.LOANDET_ORDNO, ORD_NUM);
				pick_HM.put(IConstants.LOANDET_ORDLNNO, ORDERLNO);
				pick_HM.put(IConstants.CUSTOMER_NAME, CUST_NAME);
				pick_HM.put(IConstants.LOC, LOC);
				pick_HM.put(IConstants.LOC1, LOC1);
				pick_HM.put(IConstants.LOGIN_USER, LOGIN_USER);
				pick_HM.put(IConstants.CUSTOMER_CODE, _loanUtil.getCustCode(PLANT,ORD_NUM));
				pick_HM.put(IConstants.BATCH, ITEM_BATCH);
				pick_HM.put(IConstants.INVID, BATCH_ID);
				if (BATCH_ID != "-1") {
				pick_HM.put(IConstants.BATCH_ID, BATCH_ID);
				}
				pick_HM.put(IConstants.QTY, PICKING_QTY);
				pick_HM.put(IConstants.QTY_ISSUE, PICKING_QTY);
				pick_HM.put(IConstants.ORD_QTY, ORDER_QTY);
				pick_HM.put(IConstants.INV_EXP_DATE, ITEM_EXPDATE);
				pick_HM.put("INV_QTY1", "1");
			    pick_HM.put(IConstants.REMARKS, REMARKS);
				pick_HM.put(IConstants.JOB_NUM, _loanUtil.getJobNum(PLANT, ORD_NUM));
				pick_HM.put(IConstants.TRAN_DATE, strMovHisTranDate);
				pick_HM.put(IConstants.ISSUEDATE, strTranDate);
				pick_HM.put("UOM", UOM);
				pick_HM.put("UOMQTY", UOMQTY);
				xmlStr = "";
				
				 UserLocUtil uslocUtil = new UserLocUtil();
	            	uslocUtil.setmLogger(mLogger);
	            	boolean isvalidlocforUser  =uslocUtil.isValidLocInLocmstForUser(PLANT,LOGIN_USER,LOC);
	            	if (!isvalidlocforUser) {
	            	     throw new Exception("From Loc :"+LOC+" is not User Assigned Location");
	            	 }
	            	
	            	Hashtable ht1 = new Hashtable();
					ht1.put(IConstants.PLANT, PLANT);
					ht1.put(IConstants.LOGIN_USER, (String) session.getAttribute("LOGIN_USER"));
					ht1.put(IConstants.LOC, LOC);
					ht1.put(IConstants.ITEM, ITEM_NUM);
					ht1.put(IConstants.BATCH, ITEM_BATCH);
					ht1.put(IConstants.BATCH_ID, BATCH_ID);
					ht1.put(IConstants.QTY, PICKING_QTY);
	            	ht1.put("UOM", UOM);
					ht1.put("UOMQTY", UOMQTY);
					
	            	boolean invAvailable = false;
	            	invAvailable = CheckInvMstForLoanIssue(ht1, request, response);
              if(invAvailable)
              {
				flag = _loanUtil.process_LoanOrderPicking(pick_HM);
              }
              else
              {
            	  msgval=  "Not Enough Inventory For Product:";
              }
            		    
			}
			
			
			if (flag) {
				request.getSession().setAttribute("RESULT",
						"Product ID : " + ITEM_NUM + "  Picked successfully!");
				response
						.sendRedirect("jsp/LoanOrderPicking.jsp?action=View&PLANT="
								+ PLANT + "&DONO=" + ORD_NUM);
			} else {
				List listQry = _loDetDAO
						.getLoanOrderPickDetails(PLANT, ORD_NUM);
				request.getSession().setAttribute("customerlistqry3", listQry);
if(msgval=="")
{
				request.getSession().setAttribute("RESULTERROR",
						"Product ID failed to Pick : " + ITEM_NUM);
}
else
{
	request.getSession().setAttribute("RESULTERROR",
			msgval + ITEM_NUM);
	}
				response
						.sendRedirect("jsp/LoanOrderMultiplePicking.jsp?action=resulterror&ORDERNO="
								+ ORD_NUM
								+ "&ORDERLNO="
								+ ORDERLNO
								
								+ "&ITEMNO="
								+ ITEM_NUM
								+ "&BATCH="
								+ ITEM_BATCH
								+ "&ITEMDESC="
								+ strUtils.replaceCharacters2Send(ITEM_DESCRIPTION)
								+ "&ORDERQTY="
								+ ORDER_QTY
								+ "&PICKING_QTY="
								+ PICKING_QTY
								+ "&QTY="
								+ ITEM_QTY
								+ "&FRLOC="
								+ LOC + "&TOLOC=" + LOC1);

			}

		} catch (Exception e) {

			MLogger.log(0, "" + e.getMessage());
			request.getSession().setAttribute("QTYERROR",
					e.getMessage());
			List listQry = _loDetDAO.getLoanOrderPickDetails(PLANT, ORD_NUM);
			request.getSession().setAttribute("customerlistqry3", listQry);
			response
					.sendRedirect("jsp/LoanOrderMultiplePicking.jsp?action=qtyerror&ORDERNO="
							+ ORD_NUM
							+ "&ORDERLNO="
							+ ORDERLNO
							+ "&CUSTNAME="
							+ CUST_NAME
							+ "&ITEMNO="
							+ ITEM_NUM
							+ "&BATCH="
							+ ITEM_BATCH
							+ "&ITEMDESC="
							+ strUtils.replaceCharacters2Send(ITEM_DESCRIPTION)
							+ "&ORDERQTY="
							+ ORDER_QTY
							+ "&PICKING_QTY="
							+ PICKING_QTY
							+ "&QTY="
							+ ITEM_QTY
							+ "&FRLOC="
							+ LOC + "&TOLOC=" + LOC1);

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

			String query = "ordno as dono,loc as frLoc,loc1 as toLoc,custName,custCode,jobNum,custName,personInCharge,contactNum,address,address2,address3,collectionDate,collectionTime,remark1,remark2,isnull(b.name,'') as contactname,isnull(b.telno,'') as telno,isnull(b.email,'') email,isnull(b.addr1,'') add1,isnull(b.addr2,'') add2,isnull(b.addr3,'') add3,isnull(b.remarks,'') remarks,isnull(b.addr4,'') add4,isnull(b.country,'') country,isnull(b.zip,'') zip";

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
	public boolean CheckInvMstForLoanIssue(Map<String, String> map, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		boolean flag = true;
		String extCond = "";
		InvMstDAO _InvMstDAO = new InvMstDAO();
		_InvMstDAO.setmLogger(mLogger);
		Boolean batchChecked = false;
		String posType = StrUtils.fString(request.getParameter("POS_TYPE")).trim();
		String tranType = StrUtils.fString(request.getParameter("TRANTYPE")).trim();
		String UOMQTY = StrUtils.fString(request.getParameter("UOMQTY")).trim();
		try {
			String Tranid = StrUtils.fString(request.getParameter("TRANID"));

			Hashtable<String, String> htInvMst = new Hashtable<String, String>();
			htInvMst.clear();
			HttpSession session = request.getSession();
			htInvMst.put(IDBConstants.PLANT, map.get(IConstants.PLANT));
			htInvMst.put(IDBConstants.ITEM, map.get(IConstants.ITEM));
			htInvMst.put(IDBConstants.LOC, map.get(IConstants.LOC));
			htInvMst.put(IDBConstants.USERFLD4, map.get(IConstants.BATCH));
			if (map.get(IConstants.BATCH_ID) != null && !"".equals(map.get(IConstants.BATCH_ID)) && !"-1".equals(map.get(IConstants.BATCH_ID))) {
				htInvMst.put(IDBConstants.INVID, map.get(IConstants.BATCH_ID));
			}
			StringBuffer sql = new StringBuffer(" ");
			List invlist = _InvMstDAO.selectInvMstByCrat("item,loc,userfld4 as batch,qty,crat,id", htInvMst);
			String actualqty = "";
			double actqty = 0;
			double lnqty = 0, balancqty = 0;
			double totalqty = 0;
			actualqty = map.get(IConstants.QTY);
			//actualqty = StrUtils.TrunkateDecimalForImportData(actualqty);
			//actqty = Integer.parseInt(actualqty);
			actqty = Double.parseDouble(actualqty)* Double.parseDouble(UOMQTY);
			if (request.getParameter("chkbatch") != null) {
				batchChecked = true;
			}

			// Calculate total qty in the loc
			for (int j = 0; j < invlist.size(); j++) {
				Map lineitem = (Map) invlist.get(j);
				String lineitemqty = (String) lineitem.get("qty");
				lineitemqty = StrUtils.TrunkateDecimalForImportData(lineitemqty);
				totalqty = totalqty + Integer.parseInt(lineitemqty);
			}

			if (actqty > totalqty) {
				flag = false;				
			}

			if (totalqty == 0) {
				flag = false;				
			}

		} catch (Exception e) {			
				flag = false;
		}

		return flag;
	}
}
