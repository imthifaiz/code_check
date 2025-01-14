package com.track.servlet;

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
import com.track.db.util.UserLocUtil;
import com.track.util.DateUtils;
import com.track.util.IMLogger;
import com.track.util.MLogger;
import com.track.util.StrUtils;
import com.track.util.XMLUtils;

public class DOTransferServlet extends HttpServlet implements IMLogger {
	private boolean printLog = MLoggerConstant.TransferOrderServlet_PRINTPLANTMASTERLOG;
	private boolean printInfo = MLoggerConstant.TransferOrderServlet_PRINTPLANTMASTERINFO;
	private static final String CONTENT_TYPE = "text/html; charset=windows-1252";
	private POUtil _POUtil = null;
	
	String action = "";
	String xmlStr = "";
	DOTransferUtil _DOTransferUtil = null;
	DoTransferHdrDAO _DoTransferHdrDAO=null;
	DoTransferDetDAO _DoTransferDetDAO=null;
	
	StrUtils strUtils = new StrUtils();
	
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		_POUtil = new POUtil();
		_DOTransferUtil = new DOTransferUtil();
		_DoTransferHdrDAO=new DoTransferHdrDAO();
		_DoTransferDetDAO=new DoTransferDetDAO();
	}

	//@SuppressWarnings("static-access")
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType(CONTENT_TYPE);
		PrintWriter out = response.getWriter();

		try {

			action = request.getParameter("action").trim();
			String rflag = StrUtils.fString(request.getParameter("RFLAG"));
			this.setMapDataToLogger(this.populateMapData(StrUtils.fString(request.getParameter("PLANT")), StrUtils
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
			else if (action.equalsIgnoreCase("Pick/Transfer")) {
				String xmlStr = "";
               
				xmlStr = DoTransferPickingDataByWMS(request, response);
			} //Pick/Issue Confirm
			else if (action.equalsIgnoreCase("Pick/Transfer Confirm")) {  
				String xmlStr = "";
				
				xmlStr = process_DoTranferOrderPickingByWMS(request, response);
			} 
			else
			{
				boolean flag = DisplayDoTransferData(request, response);
				String dono = "";
				dono = StrUtils.fString(request.getParameter("DONO"));
				request.getSession().setAttribute("dono", "");
				response.sendRedirect("jsp/DoTransferSummary.jsp?DONO="
						+ dono + "&action=View");
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

	
	private String DoTransferPickingDataByWMS(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException,
			Exception {

		String sepratedtoken1 = "";
		boolean flag = false;

		DoTransferDetDAO _DoTransferDetDAO = new DoTransferDetDAO();
		_DoTransferDetDAO.setmLogger(mLogger);
		
		Map receiveMaterial_HM = null;

		Map mp = null;
		mp = new HashMap();
		String PLANT = "", TO_NUM = "", TO_LN_NUM = "", ITEM_NUM = "";
		String ITEM_DESCRIPTION = "", LOGIN_USER = "", CUST_NAME = "", FROM_WAREHOUSE = "", TO_WAREHOUSE = "";

		String PICKED_QTY = "", RECEIVED_QTY = "", TO_BATCH = "", LOC = "", ORDER_QTY = "",QTY_ISSUE="";

		try {

			String sepratedtoken = "";

			String totalString = StrUtils.fString(request.getParameter("TRAVELER"));

			StringTokenizer parser = new StringTokenizer(totalString, "=");

			while (parser.hasMoreTokens())

			{
				
				int count = 1;
				sepratedtoken = parser.nextToken();
				StringTokenizer parser1 = new StringTokenizer(sepratedtoken,",");
				while (parser1.hasMoreTokens())

				{
					sepratedtoken1 = parser1.nextToken();

					mp.put("data" + count, sepratedtoken1);

					count++;

				}
				HttpSession session = request.getSession();
				PLANT = StrUtils.fString((String) session.getAttribute("PLANT")).trim();
				TO_NUM = strUtils.fString((String) mp.get("data1"));
				TO_LN_NUM = strUtils.fString((String) mp.get("data2"));
				ITEM_NUM = strUtils.fString((String) mp.get("data3"));
				ITEM_DESCRIPTION =strUtils.replaceCharacters2Send(strUtils.fString((String) mp.get("data4")));
				ORDER_QTY = strUtils.fString((String) mp.get("data5"));
				PICKED_QTY = strUtils.fString((String) mp.get("data6"));
				QTY_ISSUE = strUtils.fString((String) mp.get("data7"));
				LOGIN_USER = strUtils.fString((String) mp.get("data8"));
				LOC = strUtils.fString((String) mp.get("data9"));
				CUST_NAME = strUtils.replaceCharacters2Send(strUtils.fString((String) mp.get("data11")));
				
				xmlStr = "";

			}
		}

		catch (Exception e) {
			 
			this.mLogger.exception(this.printLog, "", e);
			request.getSession().setAttribute("CATCHERROR",
					"Error in Picking Product!");
			response
					.sendRedirect("jsp/DOTransferSummary.jsp?action=View&PLANT="
							+ PLANT + "&DONO=" + TO_NUM);
			throw e;
		}
		if (!flag) {
			
			List listQry = _DoTransferDetDAO.getDoTransferReceivingDetailsByWMS(PLANT,TO_NUM);
			request.getSession().setAttribute("assigneelistqry1", listQry);
			request.getSession().setAttribute("RESULT",	"Product ID : " + ITEM_NUM + "  Picked successfully!");
			response.sendRedirect("jsp/DOTransferPicking.jsp?ORDERNO="
					+ TO_NUM + "&ORDERLNO=" + TO_LN_NUM + "&CUSTNAME="
					+ CUST_NAME + "&ITEMNO=" + ITEM_NUM + "&ITEMDESC="
					+ strUtils.replaceCharacters2Send(ITEM_DESCRIPTION) + "&ISSUEDQTY=" + QTY_ISSUE + "&PICKEDQTY=" + PICKED_QTY
					+ "&ORDERQTY=" + ORDER_QTY+"&BATCH=NOBATCH");
		}
		return xmlStr;
	}
	
	@SuppressWarnings("unchecked")
	private String process_DoTranferOrderPickingByWMS(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException,
			Exception {
	
		Map receiveMaterial_HM = null;
		DOUtil _DOUtil=new DOUtil();
		String PLANT = "", PO_NUM = "", ITEM_NUM = "", PO_LN_NUM = "";
		String ITEM_DESCRIPTION = "", LOGIN_USER = "",REMARKS="";
		String ITEM_BATCH = "", ORDER_QTY = "", ITEM_QTY = "", INV_QTY = "", ISSUED_QTY="",PICKING_QTY = "", ITEM_EXPDATE = "", ITEM_LOC = "", FROM_LOC="",TO_LOC="",CUST_NAME = "";
		String  PICKED_QTY = "";
		 
		try {
			HttpSession session = request.getSession();
			PLANT = StrUtils.fString((String) session.getAttribute("PLANT")).trim();
			PO_NUM = StrUtils.fString(request.getParameter("ORDERNO"));
			PO_LN_NUM = StrUtils.fString(request.getParameter("ORDERLNO"));
			CUST_NAME = StrUtils.fString(request.getParameter("CUSTNAME"));
			ITEM_NUM = StrUtils.fString(request.getParameter("ITEMNO"));
			ITEM_DESCRIPTION = StrUtils.fString(request.getParameter("ITEMDESC"));
			LOGIN_USER = StrUtils.fString(request.getParameter("LOGIN_USER"));
			ITEM_BATCH = StrUtils.fString(request.getParameter("BATCH"));
			ORDER_QTY = StrUtils.removeFormat(StrUtils.fString(request.getParameter("ORDERQTY")));
			INV_QTY = StrUtils.removeFormat(StrUtils.fString(request.getParameter("INVQTY")));
			ITEM_QTY = StrUtils.removeFormat(StrUtils.fString(request.getParameter("QTY")));
			ISSUED_QTY= StrUtils.removeFormat(StrUtils.fString(request.getParameter("ISSUEDQTY")));
			ORDER_QTY = StrUtils.removeFormat(StrUtils.fString(request.getParameter("ORDERQTY")));
			PICKED_QTY = StrUtils.removeFormat(StrUtils.fString(request.getParameter("PICKEDQTY")));
			PICKING_QTY = StrUtils.removeFormat(StrUtils.fString(request.getParameter("PICKINGQTY")));
			ITEM_EXPDATE = StrUtils.fString(request.getParameter("REF"));
			ITEM_LOC = StrUtils.fString(request.getParameter("FROMLOC"));
			FROM_LOC = StrUtils.fString(request.getParameter("FROMLOC"));
			TO_LOC = StrUtils.fString(request.getParameter("TOLOC"));
			REMARKS = StrUtils.fString(request.getParameter("REF"));
			double invqty = 0;
			double orderqty = Double.parseDouble(((String) ORDER_QTY.trim()));
			if(ITEM_QTY.length()>0){
			 invqty = Double.parseDouble(((String) ITEM_QTY.trim()));}
			double issuedqty = Double.parseDouble(((String) ISSUED_QTY.trim()));
			double pickedqty = Double.parseDouble(((String) PICKED_QTY.trim()));
			double pickingQty = Double.parseDouble(((String) PICKING_QTY.trim().toString()));
                            pickingQty = StrUtils.RoundDB(pickingQty, IConstants.DECIMALPTS);
                            double sumpkqty = pickingQty + pickedqty;
                            sumpkqty = StrUtils.RoundDB(sumpkqty, IConstants.DECIMALPTS);
                            double totissueqty = orderqty-issuedqty;
                            totissueqty = StrUtils.RoundDB(totissueqty, IConstants.DECIMALPTS);
			boolean qtyFlag = false;
		         boolean isExistsforTransfer = false;
			
			if (pickingQty > (totissueqty) 
					|| (sumpkqty) > (totissueqty)|| pickingQty > invqty  ) {
				qtyFlag = true;
                                
			    Hashtable htPickDet = new Hashtable();
			    htPickDet.clear();
			    
			    htPickDet.put("a."+IDBConstants.PLANT,PLANT);
			    htPickDet.put("DONO", PO_NUM);
			    htPickDet.put("DOLNO",PO_LN_NUM);
			    htPickDet.put("a.ITEM", ITEM_NUM);
			    htPickDet.put("BATCH", ITEM_BATCH);
			    htPickDet.put("TOLOC", ITEM_LOC);
                            String extracond ="   group by DONO,DOLNO,a.ITEM,TOLOC,BATCH having SUM(PickQty)>="+pickingQty+" and SUM(b.qty)>= "+pickingQty+" ";
			    isExistsforTransfer= _DoTransferDetDAO.isValidPrdToTransferPick(htPickDet,PLANT, extracond);
				
                                if(!isExistsforTransfer){
                                    throw new Exception("Error in picking item : Picking Qty Should less than Order Qty or Order Qty - Issued Qty !");
                                }
				
			}

			receiveMaterial_HM = new HashMap();
			receiveMaterial_HM.put(IConstants.PLANT, PLANT);
			receiveMaterial_HM.put(IConstants.ITEM, ITEM_NUM);
			receiveMaterial_HM.put(IConstants.ITEM_DESC, strUtils.InsertQuotes(ITEM_DESCRIPTION));
			receiveMaterial_HM.put(IConstants.PODET_PONUM, PO_NUM);
			receiveMaterial_HM.put(IConstants.PODET_POLNNO, PO_LN_NUM);
			receiveMaterial_HM.put(IConstants.CUSTOMER_NAME, CUST_NAME);
			receiveMaterial_HM.put(IConstants.LOC, ITEM_LOC);
			receiveMaterial_HM.put(IConstants.LOC2, TO_LOC);
			receiveMaterial_HM.put(IConstants.LOGIN_USER, LOGIN_USER);
			receiveMaterial_HM.put(IConstants.CUSTOMER_CODE, _DOUtil.getCustCode(PLANT, PO_NUM));
			receiveMaterial_HM.put(IConstants.BATCH, ITEM_BATCH);
			receiveMaterial_HM.put(IConstants.QTY, PICKING_QTY);
			receiveMaterial_HM.put(IConstants.ORD_QTY, ORDER_QTY);
			receiveMaterial_HM.put(IConstants.INV_EXP_DATE, ITEM_EXPDATE);
                        receiveMaterial_HM.put(IConstants.JOB_NUM, _DOTransferUtil.getJobNum(PLANT,	PO_NUM));
                        receiveMaterial_HM.put(IConstants.REMARKS, REMARKS);
                        receiveMaterial_HM.put("INV_QTY1", "1");

			xmlStr = "";
			// check for item in location
			Hashtable htLocMst = new Hashtable();
			htLocMst.put("plant", receiveMaterial_HM.get(IConstants.PLANT));
			htLocMst.put("item", receiveMaterial_HM.get(IConstants.ITEM));
			htLocMst.put("loc", receiveMaterial_HM.get(IConstants.LOC));
                        
		    UserLocUtil uslocUtil = new UserLocUtil();
		    uslocUtil.setmLogger(mLogger);
		    boolean isvalidlocforUser  =uslocUtil.isValidLocInLocmstForUser(PLANT,LOGIN_USER,ITEM_LOC);
		    if (isvalidlocforUser) {
                        isvalidlocforUser  =uslocUtil.isValidLocInLocmstForUser(PLANT,LOGIN_USER,TO_LOC);
                        if(!isvalidlocforUser){
                            throw new Exception("To Loc :"+TO_LOC+" is not a User Assigned Location/Valid Location");
                        }
                    }else{
                            throw new Exception("From Loc :"+ITEM_LOC+" is not User Assigned Location/Valid Location");
                    }
		       
			boolean flag = false;
				
			flag = _DOTransferUtil.process_DoTransferPickingForPC(receiveMaterial_HM);
          	       if (flag) {
				request.getSession().setAttribute("RESULT","Product : " + ITEM_NUM + "  Picked successfully!");
				response.sendRedirect("jsp/DOTransferSummary.jsp?action=View&PLANT="
								+ PLANT + "&DONO=" + PO_NUM);
			} else {

				request.getSession().setAttribute("RESULTERROR","Failed to Pick Product : " + ITEM_NUM);
				response.sendRedirect("jsp/DOTransferPicking.jsp?action=resulterror");
			}

		} catch (Exception e) {
			
			this.mLogger.exception(this.printLog, "", e);
			DoDetDAO _DoDetDAO = new DoDetDAO();
			_DoDetDAO.setmLogger(mLogger);
			List listQry = _DoTransferDetDAO.getDoTransferDetailsByWMS(PLANT,PO_NUM);
			request.getSession().setAttribute("customerlistqry3", listQry);
			request.getSession().setAttribute("QTYERROR1", e.getMessage());
				
			response.sendRedirect("jsp/DOTransferPicking.jsp?action=qtyerror&ITEMNO="
							+ ITEM_NUM
							+ "&ITEMDESC="
							+ strUtils.replaceCharacters2Send(ITEM_DESCRIPTION)
							+ "&LOC="
							+ ITEM_LOC
							+ "&FROMLOC="
							+ FROM_LOC
							+ "&TOLOC="
							+ TO_LOC
							+ "&ORDERNO="
							+ PO_NUM
							+ "&ORDERLNO="
							+ PO_LN_NUM
							+ "&CUSTNAME="
							+ CUST_NAME
							+ "&BATCH="
							+ ITEM_BATCH
							+ "&REF="
							+ ITEM_EXPDATE
							+ "&ORDERQTY="
							+ ORDER_QTY
							+ "&QTY="
							+ ITEM_QTY
							+ "&INVQTY="
							+ INV_QTY
							+ "&ISSUEDQTY="
							+ ISSUED_QTY
							+ "&PICKEDQTY="
							+ PICKED_QTY + "&PICKINGQTY=" + PICKING_QTY);
		
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

	public HashMap<String, String> populateMapData(String companyCode,
			String userCode) {
		HashMap<String, String> loggerDetailsHasMap = new HashMap<String, String>();
		loggerDetailsHasMap.put(MLogger.COMPANY_CODE, companyCode);
		loggerDetailsHasMap.put(MLogger.USER_CODE,userCode);
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
			String plant = StrUtils.fString((String) session.getAttribute("PLANT")).trim();
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
}
