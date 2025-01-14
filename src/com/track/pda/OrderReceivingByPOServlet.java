package com.track.pda;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.transaction.UserTransaction;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.track.constants.IConstants;
import com.track.constants.IDBConstants;
import com.track.constants.MLoggerConstant;
import com.track.constants.TransactionConstants;
import com.track.dao.BillDAO;
import com.track.dao.CoaDAO;
import com.track.dao.EmployeeDAO;
import com.track.dao.ExpensesDAO;
import com.track.dao.FinCountryTaxTypeDAO;
import com.track.dao.IntegrationsDAO;
import com.track.dao.InvMstDAO;
import com.track.dao.InvoiceDAO;
import com.track.dao.ItemMstDAO;
import com.track.dao.MasterDAO;
import com.track.dao.MovHisDAO;
import com.track.dao.PlantMstDAO;
import com.track.dao.PoDetDAO;
import com.track.dao.PoHdrDAO;
import com.track.dao.RecvDetDAO;
import com.track.dao.TblControlDAO;
import com.track.dao.UomDAO;
import com.track.dao.VendMstDAO;
import com.track.db.object.CostofgoodsLanded;
import com.track.db.object.DoDet;
import com.track.db.object.DoHdr;
import com.track.db.object.FinCountryTaxType;
import com.track.db.object.Journal;
import com.track.db.object.JournalDetail;
import com.track.db.object.JournalHeader;
import com.track.db.object.PoDet;
import com.track.db.object.PoHdr;
import com.track.db.util.BillUtil;
import com.track.db.util.EmailMsgUtil;
import com.track.db.util.InvUtil;
import com.track.db.util.LocUtil;
import com.track.db.util.MasterUtil;
import com.track.db.util.POUtil;
import com.track.db.util.TblControlUtil;
import com.track.db.util.UserLocUtil;
import com.track.gates.DbBean;
import com.track.service.Costofgoods;
import com.track.service.JournalService;
import com.track.service.ShopifyService;
import com.track.serviceImplementation.CostofgoodsImpl;
import com.track.serviceImplementation.JournalEntry;
import com.track.servlet.DynamicFileServlet;
import com.track.util.DateUtils;
import com.track.util.IMLogger;
import com.track.util.MLogger;
import com.track.util.StrUtils;
import com.track.util.ThrowableUtil;
import com.track.util.XMLUtils;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;



//public class OrderReceivingByPOServlet extends HttpServlet implements SingleThreadModel {
public class OrderReceivingByPOServlet extends DynamicFileServlet implements IMLogger {
	private boolean printLog = MLoggerConstant.OrderReceivingByPOServlet_PRINTPLANTMASTERLOG;
	private boolean printInfo = MLoggerConstant.OrderReceivingByPOServlet_PRINTPLANTMASTERINFO;
	private static final long serialVersionUID = 170114335994094842L;
	private POUtil _POUtil = null;
	private InvMstDAO _InvMstDAO = null;
	private String PLANT = "";
	private String xmlStr = "";
	private RecvDetDAO _RecvDetDAO = null;
	
	private String action = "";
	
	private static final String CONTENT_TYPE = "text/xml";
	
	String autoinverr="";

	public void init() throws ServletException {
		_POUtil = new POUtil();
		_InvMstDAO = new InvMstDAO();
		_RecvDetDAO = new RecvDetDAO();
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType(CONTENT_TYPE);
		PrintWriter out = response.getWriter();
		boolean ajax = "XMLHttpRequest".equals(request.getHeader("X-Requested-With"));
		try {
			
			this.setMapDataToLogger(this.populateMapData(StrUtils
					.fString(request.getParameter("PLANT")), StrUtils
					.fString(request.getParameter("LOGIN_USER"))
					+ " PDA_USER"));
			_POUtil.setmLogger(mLogger);
			_InvMstDAO.setmLogger(mLogger);
			if (this.printInfo) {
				diaplayInfoLogs(request);
			}
			action = request.getParameter("action").trim();

			if (action.equalsIgnoreCase("load_inBoundOrders")) {
				xmlStr = "";
				xmlStr = load_inBoundOrders(request, response);
			}
                        
		    if (action.equalsIgnoreCase("load_inBoundOrdersByItem")) {
		            xmlStr = "";
		            xmlStr = load_inBoundOrdersByItem(request, response);
		    }

			else if (action.equalsIgnoreCase("load_inBoundOrder_item_details")) {
				xmlStr = "";
				xmlStr = load_inBoundOrder_item_details(request, response);
			}
			else if (action.equalsIgnoreCase("load_inBoundOrder_item_detailsBynext")) {
				xmlStr = "";
				xmlStr = load_inBoundOrder_item_detailsBynext(request, response);
			}
			else if (action.equalsIgnoreCase("load_inBoundOrder_item_ByItem")) {
				xmlStr = "";
				xmlStr = load_inBoundOrder_item_ByItem(request, response);
			}
			else if (action.equalsIgnoreCase("load_Allitem_details")) {
				xmlStr = "";
				xmlStr = load_Allitem_details(request, response);
			}
			else if (action.equalsIgnoreCase("load_All_Inv_item_details")) {
				xmlStr = "";
				xmlStr = load_All_Inv_item_details(request, response);
			}
			else if (action.equalsIgnoreCase("GET_RECEIPT_ITEM")) {
				xmlStr = "";
				xmlStr = load_receipt_item_details(request, response);
			}
			else if (action.equalsIgnoreCase("load_random_inBoundOrder_item_details")) {
				xmlStr = "";
				xmlStr = load_random_inBoundOrder_item_details(request, response);
			}

			else if (action
					.equalsIgnoreCase("process_InboundOrderGenerateBatch")) {
				xmlStr = "";
				xmlStr = process_InboundOrderGenerateBatch(request, response);
			}

			else if (action.equalsIgnoreCase("load_locations")) {
				xmlStr = "";
				xmlStr = load_locations(request, response);
			}

			else if (action.equalsIgnoreCase("process_receiveMaterial")) {

				xmlStr = process_orderReceiving(request, response);
			}
		else if (action.equalsIgnoreCase("process_receiveMaterial_random")) {

				xmlStr = process_orderReceiving_random(request, response);
			}

			else if (action.equalsIgnoreCase("load_item_details")) {
				xmlStr = "";
				xmlStr = load_item_details(request, response);
			}

			else if (action.equalsIgnoreCase("load_inBoundOrder_sup_details")) {
				xmlStr = "";
				xmlStr = load_inBoundOrder_sup_details(request, response);
			}

			else if (action.equalsIgnoreCase("load_po_details")) {
				xmlStr = "";
				xmlStr = load_po_Details(request, response);
			}

			else if (action.equalsIgnoreCase("Receive")) {
				xmlStr = "";
				xmlStr = process_orderReceivingByWMS(request, response);
			} 
            else if (action.equalsIgnoreCase("ReceiveByRange")) {
                xmlStr = "";
                xmlStr = process_orderReceivingByRange(request, response);
            } 
            else if (action.equalsIgnoreCase("MultipleReceive")) {
				xmlStr = "";
				xmlStr = processOrderMultiReceivingByWMS(request, response);
			} 
		        //Start added by Bruhan for Bulk receiving on 30 July 2012.
            else if (action.equalsIgnoreCase("BulkReceive")) {
				xmlStr = "";
				xmlStr = processOrderBulkReceivingByWMS(request, response);
				//End added by Bruhan for Bulk receiving on 30 July 2012.
			}else if (action.equalsIgnoreCase("Reverse")) {
				xmlStr = "";
				xmlStr = process_orderReverseByWMS(request, response);
			} else if (action.equalsIgnoreCase("ReverseConfirm")) {
				xmlStr = "";
				xmlStr = process_orderReverseConfrimByWMS(request, response);
			} else if (action.equalsIgnoreCase("Generate Batch")) {
				xmlStr = "";
				xmlStr = process_GenerateBatchByWMS(request, response);
			}
			else if (action.equalsIgnoreCase("BulkReceivebyOrders")) {
				xmlStr = "";
				xmlStr = IncomingDataBulkbyOrders(request, response);
			}
			else if (action.equalsIgnoreCase("load_InboundOrder_item_orderline_details")) {
				xmlStr = "";
				xmlStr =load_Inbound_item_orderline_details(request, response);
			}
			else if (action.equalsIgnoreCase("load_uom")) {
				xmlStr = "";
				xmlStr = load_uom(request, response);
			}
			else if (action.equalsIgnoreCase("load_inBoundOrdersPDApopup")) {
				xmlStr = "";
				xmlStr = load_inBoundOrdersPDApopup(request, response);
			}
			else if (action.equalsIgnoreCase("load_inBoundOrdersPDAAutoSuggestion")) {
				xmlStr = "";
				xmlStr = load_inBoundOrdersPDAAutoSuggestion(request, response);
			}
			else if (action.equalsIgnoreCase("load_Allitem_details_AutoSuggestion")) {
				xmlStr = "";
				xmlStr = load_Allitem_details_AutoSuggestion(request, response);
			}
			else if (action.equalsIgnoreCase("load_Inbound_item_orderdetails_ForPDA_UOMPopup")) {
				xmlStr = "";
				xmlStr = load_Inbound_item_orderdetails_ForPDA_UOMPopup(request, response);
			}
			else if (action.equalsIgnoreCase("load_locations_PDA")) {
				xmlStr = "";
				xmlStr = load_locations_PDA(request, response);
			}

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			if (!ajax) {
				xmlStr = XMLUtils.getXMLMessage(1, "Unable to process? "
						+ e.getMessage());
			}
		}
		if (!ajax) {
			out.write(xmlStr);
			out.close();
		}
	}
	/* ************Modification History*********************************
	   Oct 14 2014 Bruhan, Description: To include Transaction date
	*/
	@SuppressWarnings( { "unchecked", "static-access" })
	private String processOrderMultiReceivingByWMS(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		UserLocUtil uslocUtil = new UserLocUtil();
		uslocUtil.setmLogger(mLogger);
		Map receiveMaterial_HM = null;
		ItemMstDAO _ItemMstDAO = new ItemMstDAO();
		String PLANT = "", PO_NUM = "", PONO = "", ITEM_NUM = "", PO_LN_NUM = "";
		String ITEM_DESCRIPTION = "", LOGIN_USER = "",ISNONSTKFLG="";
		String ITEM_BATCH = "", EXPIREDATE = "", CUST_NAME = "", QTY = "", ITEM_QTY = "", RECEIVEDQTY = "", ITEM_REF = "", 
		ITEM_LOC = "", REMARKS = "", FAX = "",TRANSACTIONDATE = "",strMovHisTranDate="",strTranDate="", GRNO="";
		//String BALANCEQTY = "";
		//UserTransaction ut = null;
		try {
			HttpSession session = request.getSession(false);
			PLANT = StrUtils.fString((String) session.getAttribute("PLANT"));
			PO_NUM = StrUtils.fString(request.getParameter("ORDERNO"));
			PONO = StrUtils.fString(request.getParameter("ORDERNO"));
			PO_LN_NUM = StrUtils.fString(request.getParameter("ORDERLNO"));

			CUST_NAME = StrUtils.fString(request.getParameter("CUSTNAME"));
			ITEM_NUM = StrUtils.fString(request.getParameter("ITEMNO"));
			ITEM_DESCRIPTION = StrUtils.fString(request	.getParameter("ITEMDESC"));
			LOGIN_USER = StrUtils.fString(request.getParameter("LOGIN_USER"));

			QTY = StrUtils.removeFormat(StrUtils.fString(request.getParameter("ORDERQTY")));
			RECEIVEDQTY = StrUtils.removeFormat(StrUtils.fString(request.getParameter("RECEIVEDQTY")));
	    	double OrdQty = Double.parseDouble(((String) QTY.trim().toString()));
			double receivedqty = Double.parseDouble(((String) RECEIVEDQTY.trim()));
			double balqty = OrdQty - receivedqty;
			balqty = StrUtils.RoundDB(balqty, IConstants.DECIMALPTS);
				//System.out.println("ordqty"+OrdQty+"recivedqty"+receivedqty+"balqty"+balqty);		
			ITEM_QTY = StrUtils.fString(request.getParameter("RECEIVEQTY"));
			ITEM_REF = StrUtils.fString(request.getParameter("REF"));
//			BALANCEQTY = StrUtils.fString(request.getParameter("BALANCEQTY"));
			REMARKS = StrUtils.fString(request.getParameter("REMARKS"));
			FAX = StrUtils.fString(request.getParameter("FAX"));
			TRANSACTIONDATE = StrUtils.fString(request.getParameter("TRANSACTIONDATE"));
			GRNO = StrUtils.fString(request.getParameter("GRNO"));
			if (TRANSACTIONDATE.length()>5)
				strMovHisTranDate    = TRANSACTIONDATE.substring(6)+"-"+TRANSACTIONDATE.substring(3,5)+"-"+TRANSACTIONDATE.substring(0,2);
			    strTranDate    = TRANSACTIONDATE.substring(0,2)+"/"+ TRANSACTIONDATE.substring(3,5)+"/"+TRANSACTIONDATE.substring(6);
			String DYNAMIC_RECEIVING_SIZE = StrUtils.fString(request
					.getParameter("DYNAMIC_RECEIVING_SIZE"));
			int DYNAMIC_RECEIVING_SIZE_INT = Integer
					.parseInt(DYNAMIC_RECEIVING_SIZE);
			
//			boolean qtyFlag = false;
			if (balqty < Double.parseDouble(((String) ITEM_QTY.trim().toString()))) {
//				qtyFlag = true;
				throw new Exception(
						"Error  : Receiving Qty Should less than or equal to ordered Qty");
			}
			ISNONSTKFLG =  new ItemMstDAO().getNonStockFlag(PLANT,ITEM_NUM);
			
			Boolean transactionHandler = true;
			for (int indexFlow = 0; indexFlow < DYNAMIC_RECEIVING_SIZE_INT; indexFlow++) {
				ITEM_LOC = StrUtils.fString(request.getParameter("LOC_"
						+ indexFlow));
				ITEM_BATCH = StrUtils.fString(request.getParameter("BATCH_"
						+ indexFlow));

				if (ITEM_BATCH.length() == 0) {
					ITEM_BATCH = "NOBATCH";
				}
					
				
				String ITEM_QTY_NOW = StrUtils.fString(request
						.getParameter("RECEIVEQTY_" + indexFlow));
			double itemqty = StrUtils.RoundDB(Double.parseDouble(ITEM_QTY_NOW),IConstants.DECIMALPTS);
			
			if(itemqty<=0.000)
			{
				throw new Exception(
				"Error  : Receiving Qty Should be greater than Zero");
			
			}
			ITEM_QTY_NOW = String.valueOf(itemqty);
			ITEM_QTY_NOW = StrUtils.formatThreeDecimal(ITEM_QTY_NOW);
				EXPIREDATE = StrUtils.fString(request
						.getParameter("EXPIREDATE_" + indexFlow));

				receiveMaterial_HM = new HashMap();
				receiveMaterial_HM.put(IConstants.PLANT, PLANT);
				receiveMaterial_HM.put(IConstants.CUSTOMER_NAME, CUST_NAME);
				receiveMaterial_HM.put(IConstants.ITEM, ITEM_NUM);
				receiveMaterial_HM.put(IConstants.ITEM_DESC, _ItemMstDAO.getItemDesc(PLANT ,ITEM_NUM));
				receiveMaterial_HM.put(IConstants.PODET_PONUM, PO_NUM);
				receiveMaterial_HM.put(IConstants.PODET_POLNNO, PO_LN_NUM);
				receiveMaterial_HM.put(IConstants.LOC, ITEM_LOC);
				receiveMaterial_HM.put(IConstants.LOGIN_USER, LOGIN_USER);
				receiveMaterial_HM.put(IConstants.CUSTOMER_CODE, _POUtil.getCustCode(PLANT, PO_NUM));
				receiveMaterial_HM.put(IConstants.INV_BATCH, ITEM_BATCH);
				receiveMaterial_HM.put(IConstants.BATCH, ITEM_BATCH);
				receiveMaterial_HM.put(IConstants.QTY, QTY);
				receiveMaterial_HM.put(IConstants.ORD_QTY, QTY);
				receiveMaterial_HM.put(IConstants.INV_QTY, ITEM_QTY_NOW);
				receiveMaterial_HM.put(IConstants.RECV_QTY, ITEM_QTY_NOW);
				receiveMaterial_HM.put(IConstants.INV_EXP_DATE, ITEM_REF);
				receiveMaterial_HM.put(IConstants.USERFLD1, REMARKS);
				receiveMaterial_HM.put(IConstants.USERFLD2, FAX);
				receiveMaterial_HM.put(IConstants.CREATED_AT,(String) DateUtils.getDateTime());
				receiveMaterial_HM.put(IConstants.CREATED_BY, LOGIN_USER);
				receiveMaterial_HM.put(IConstants.JOB_NUM, _POUtil.getJobNum(PLANT, PO_NUM));
				receiveMaterial_HM.put(IConstants.REMARKS, ITEM_REF);
				receiveMaterial_HM.put(IConstants.EXPIREDATE, EXPIREDATE);
				receiveMaterial_HM.put(IConstants.TRAN_DATE, strMovHisTranDate);
    			receiveMaterial_HM.put(IConstants.RECVDATE, strTranDate);
    			receiveMaterial_HM.put(IConstants.GRNO, GRNO);//Ravindra
    			receiveMaterial_HM.put("NONSTKFLAG", ISNONSTKFLG);
				xmlStr = "";
				// check for item in location
				Hashtable htLocMst = new Hashtable();
				htLocMst.put("PLANT", receiveMaterial_HM.get(IConstants.PLANT));
				htLocMst.put("item", receiveMaterial_HM.get(IConstants.ITEM));
				htLocMst.put("loc", receiveMaterial_HM.get(IConstants.LOC));

				boolean isvalidlocforUser = uslocUtil
						.isValidLocInLocmstForUser(PLANT, LOGIN_USER, ITEM_LOC);
				_POUtil.setmLogger(mLogger);
				if (isvalidlocforUser) {
					transactionHandler = _POUtil
							.processMultiReceiveMaterialByWMS(receiveMaterial_HM) && true;
				} else {
					transactionHandler = transactionHandler && false;
					throw new Exception("Receiving Loc :" + ITEM_LOC
							+ " is not User Assigned Location");

				}
			}
			if (transactionHandler == true) {
				//DbBean.CommitTran(ut);
				request.getSession(false).setAttribute("isummaryvalue", receiveMaterial_HM);//Ravindra
				request.getSession(false).setAttribute("RESULTINBRECEIVE",
						"Product : " + ITEM_NUM + "  received successfully!");

				response
						.sendRedirect("/track/OrderReceivingServlet?action=ViewMultiple&RFLAG=1&PONO="
								+ PONO);//Ravindra
			} else {
				throw new Exception("Unable to process..!");
			}

		} catch (Exception e) {

			//DbBean.RollbackTran(ut);
			this.mLogger.exception(this.printLog, "", e);
			request.getSession(false).setAttribute("QTYERROR", e.getMessage());
			response
					.sendRedirect("jsp/InboundOrderMultipleReceiptSummary.jsp?action=qtyerror&ITEMNO="
							+ ITEM_NUM
							+ "&ITEMDESC="
							+ StrUtils.replaceCharacters2Send(ITEM_DESCRIPTION)
							+ "&LOC="
							+ ITEM_LOC
							+ "&ORDERNO="
							+ PO_NUM
							+ "&ORDERLNO="
							+ PO_LN_NUM
							+ "&CUSTNAME="
							+ StrUtils.replaceCharacters2Send(CUST_NAME)
							+ "&BATCH="
							+ ITEM_BATCH
							+ "&REF="
							+ ITEM_REF
							+ "&ORDERQTY="
							+ QTY
							+ "&RECEIVEQTY="
							+ ITEM_QTY
							+ "&RECEIVEDQTY=" + RECEIVEDQTY);
			throw e;
		}
		return xmlStr;
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

	public void destroy() {
	}

	private String load_po_Details(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {
		String str = "", aOrdNo = "";
		try {

			PLANT = StrUtils.fString(request.getParameter("PLANT"));
			aOrdNo = StrUtils.fString(request.getParameter("ORDER_NUM"));
			Hashtable ht = new Hashtable();
			ht.put("PLANT", PLANT);
			ht.put("PONO", aOrdNo);

			str = _POUtil.getItemDetailsByPO(PLANT, aOrdNo);

			if (str.equalsIgnoreCase("")) {
				str = XMLUtils.getXMLMessage(1, "Item details not found?");
			}

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			str = XMLUtils.getXMLMessage(1, e.getMessage());
		}
		return str;
	}

	private String load_item_details(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException,
			Exception {
		String aPlant = "", aItem = "";
		try {

			aPlant = StrUtils.fString(request.getParameter("PLANT"));
			aItem = StrUtils.fString(request.getParameter("ITEM_NUM"));

			Hashtable itemht = new Hashtable();
			itemht.put("ITEM", aItem);
			boolean isexits = new ItemMstDAO().isExisit(itemht, "");
			if (isexits) {
				String itemDesc = new ItemMstDAO().getItemDesc(aPlant, aItem);

				xmlStr = XMLUtils.getXMLHeader();
				xmlStr += XMLUtils.getStartNode("ItemDetails");
				xmlStr += XMLUtils.getXMLNode("status", "0");
				xmlStr += XMLUtils.getXMLNode("description", "");
				xmlStr += XMLUtils.getXMLNode("item", aItem);
				xmlStr += XMLUtils.getXMLNode("itemDesc", itemDesc);

				xmlStr += XMLUtils.getEndNode("ItemDetails");
			} else {
				xmlStr = XMLUtils.getXMLMessage(1, "Item Not found");
			}

			if (xmlStr.equalsIgnoreCase("")) {
				xmlStr = XMLUtils.getXMLMessage(1, "Item Not found");
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;

		}
		return xmlStr;
	}

	/* ************Modification History*********************************
	   Oct 14 2014 Bruhan, Description: To include Transaction date
	*/
	private String process_orderReceivingByWMS(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException,
			Exception {
		UserLocUtil uslocUtil = new UserLocUtil();
		uslocUtil.setmLogger(mLogger);
		Map receiveMaterial_HM = null;
		ItemMstDAO _ItemMstDAO = new ItemMstDAO();
		String PLANT = "", PO_NUM = "", PONO = "", ITEM_NUM = "", PO_LN_NUM = "",recflg="";
		String ITEM_DESCRIPTION = "", LOGIN_USER = "",ISNONSTKFLG="";
		String ITEM_BATCH = "", EXPIREDATE = "", CUST_NAME = "", QTY = "", ITEM_QTY = "", RECEIVEDQTY = "", ITEM_REF = "", ITEM_LOC = "", REMARKS = "", 
		FAX = "",TRANSACTIONDATE = "",  strMovHisTranDate="",strTranDate="",GRNO="",UOM="",UOMQTY="";
//		String BALANCEQTY = "";
        boolean ajax = "XMLHttpRequest".equals(request.getHeader("X-Requested-With"));
		JSONObject resultJson = new JSONObject();
		try {
			HttpSession session = request.getSession(false);
			PLANT = StrUtils.fString((String) session.getAttribute("PLANT"));
			PO_NUM = StrUtils.fString(request.getParameter("ORDERNO"));
			PONO = StrUtils.fString(request.getParameter("ORDERNO"));
			PO_LN_NUM = StrUtils.fString(request.getParameter("ORDERLNO"));

			CUST_NAME = StrUtils.fString(request.getParameter("CUSTNAME"));
			ITEM_NUM = StrUtils.fString(request.getParameter("ITEMNO"));
			ITEM_DESCRIPTION = StrUtils.fString(request
					.getParameter("ITEMDESC"));
			LOGIN_USER = StrUtils.fString(request.getParameter("LOGIN_USER"));
			EXPIREDATE = StrUtils.fString(request.getParameter("EXPIREDATE"));
			ITEM_BATCH = StrUtils.fString(request.getParameter("BATCH"));
			if (ITEM_BATCH.length() == 0) {
				ITEM_BATCH = "NOBATCH";
			}
			QTY = StrUtils.removeFormat(StrUtils.fString(request.getParameter("ORDERQTY")));
			RECEIVEDQTY =StrUtils.removeFormat( StrUtils.fString(request.getParameter("RECEIVEDQTY")));
			UOMQTY =StrUtils.removeFormat( StrUtils.fString(request.getParameter("UOMQTY")));
			UOM =StrUtils.removeFormat( StrUtils.fString(request.getParameter("UOM")));
			double OrdQty = Double.parseDouble(((String) QTY.trim().toString()));
			double receivedqty = Double.parseDouble(((String) RECEIVEDQTY.trim()));
			
			double balqty = OrdQty - receivedqty;
			balqty = StrUtils.RoundDB(balqty, IConstants.DECIMALPTS);
			ITEM_QTY = StrUtils.fString(request.getParameter("RECEIVEQTY"));
			double itemqty = Double.parseDouble(((String) ITEM_QTY.trim().toString()));
			itemqty = StrUtils.RoundDB(itemqty, IConstants.DECIMALPTS);
			ITEM_QTY = String.valueOf(itemqty);
			ITEM_QTY = StrUtils.formatThreeDecimal(ITEM_QTY);
			ITEM_REF = StrUtils.fString(request.getParameter("REF"));

			ITEM_LOC = StrUtils.fString(request.getParameter("LOC"));
//			BALANCEQTY = StrUtils.fString(request.getParameter("BALANCEQTY"));
			REMARKS = StrUtils.fString(request.getParameter("REMARKS"));
			FAX = StrUtils.fString(request.getParameter("FAX"));
		    recflg = request.getParameter("RECFLAG");
 			TRANSACTIONDATE = StrUtils.fString(request.getParameter("TRANSACTIONDATE"));
 			GRNO = StrUtils.fString(request.getParameter("GRNO"));//	Ravindra
			if (TRANSACTIONDATE.length()>5)
				strMovHisTranDate    = TRANSACTIONDATE.substring(6)+"-"+TRANSACTIONDATE.substring(3,5)+"-"+TRANSACTIONDATE.substring(0,2);
			    strTranDate    = TRANSACTIONDATE.substring(0,2)+"/"+ TRANSACTIONDATE.substring(3,5)+"/"+TRANSACTIONDATE.substring(6);
				
//			boolean qtyFlag = false;
			if(itemqty<=0.000)
			{
				throw new Exception(
				"Error  : Receiving Qty Should be greater than Zero");
			
			}
			if (balqty < itemqty ) {
//				qtyFlag = true;
				throw new Exception(
						"Error  : Receiving Qty Should less than or equal to ordered Qty");
			}

			if (ITEM_REF.length() > 8) {

				System.out.println("EXP date" + ITEM_REF);
			}
			
			ISNONSTKFLG =  new ItemMstDAO().getNonStockFlag(PLANT,ITEM_NUM);
			
			receiveMaterial_HM = new HashMap();
			receiveMaterial_HM.put(IConstants.PLANT, PLANT);
			receiveMaterial_HM.put(IConstants.CUSTOMER_NAME, CUST_NAME);
			receiveMaterial_HM.put(IConstants.ITEM, ITEM_NUM);
			receiveMaterial_HM.put(IConstants.ITEM_DESC, _ItemMstDAO.getItemDesc(PLANT ,ITEM_NUM));
			receiveMaterial_HM.put(IConstants.PODET_PONUM, PO_NUM);
			receiveMaterial_HM.put(IConstants.PODET_POLNNO, PO_LN_NUM);
			receiveMaterial_HM.put(IConstants.LOC, ITEM_LOC);
			receiveMaterial_HM.put(IConstants.LOGIN_USER, LOGIN_USER);
			receiveMaterial_HM.put(IConstants.CUSTOMER_CODE, _POUtil.getCustCode(PLANT, PO_NUM));
			receiveMaterial_HM.put(IConstants.INV_BATCH, ITEM_BATCH);
			receiveMaterial_HM.put(IConstants.BATCH, ITEM_BATCH);
			receiveMaterial_HM.put(IConstants.QTY, QTY);
			receiveMaterial_HM.put(IConstants.ORD_QTY, QTY);
			receiveMaterial_HM.put(IConstants.INV_QTY, ITEM_QTY);
			receiveMaterial_HM.put(IConstants.RECV_QTY, ITEM_QTY);
			receiveMaterial_HM.put(IConstants.INV_EXP_DATE, ITEM_REF);
			receiveMaterial_HM.put(IConstants.USERFLD1, REMARKS);
			receiveMaterial_HM.put(IConstants.USERFLD2, FAX);
			receiveMaterial_HM.put(IConstants.CREATED_AT,
					(String) DateUtils.getDateTime());
			receiveMaterial_HM.put(IConstants.CREATED_BY, LOGIN_USER);

			receiveMaterial_HM.put(IConstants.JOB_NUM, _POUtil.getJobNum(PLANT,
					PO_NUM));
			receiveMaterial_HM.put(IConstants.REMARKS, ITEM_REF);
			receiveMaterial_HM.put(IConstants.EXPIREDATE, EXPIREDATE);
			receiveMaterial_HM.put(IConstants.TRAN_DATE, strMovHisTranDate);
 			receiveMaterial_HM.put(IConstants.RECVDATE, strTranDate);
 			receiveMaterial_HM.put("NONSTKFLAG", ISNONSTKFLG);
 			receiveMaterial_HM.put(IConstants.GRNO, GRNO);
 			receiveMaterial_HM.put(IConstants.UOM, UOM);
 			receiveMaterial_HM.put("UOMQTY", UOMQTY);
			xmlStr = "";

			// check for item in location
			Hashtable htLocMst = new Hashtable();
			htLocMst.put("PLANT", receiveMaterial_HM.get(IConstants.PLANT));
			htLocMst.put("item", receiveMaterial_HM.get(IConstants.ITEM));
			htLocMst.put("loc", receiveMaterial_HM.get(IConstants.LOC));

			boolean isvalidlocforUser = uslocUtil.isValidLocInLocmstForUser(
					PLANT, LOGIN_USER, ITEM_LOC);
			if (isvalidlocforUser) {
				xmlStr = _POUtil
						.process_ReceiveMaterialByWMS(receiveMaterial_HM);
				
				if (xmlStr.contains("received successfully!")) {//Shopify Inventory Update
   					Hashtable htCond = new Hashtable();
   					htCond.put(IConstants.PLANT, PLANT);
   					if(new IntegrationsDAO().getShopifyConfigCount(htCond,"")) {
   						String nonstkflag = new ItemMstDAO().getNonStockFlag(PLANT,ITEM_NUM);						
						if(nonstkflag.equalsIgnoreCase("N")) {
   						String availqty ="0";
   						ArrayList invQryList = null;
   						htCond = new Hashtable();
   						invQryList= new InvUtil().getInvListSummaryAvailableQtyforShopify(PLANT,ITEM_NUM, new ItemMstDAO().getItemDesc(PLANT, ITEM_NUM),htCond);						
   						if (invQryList.size() > 0) {					
   							for (int iCnt =0; iCnt<invQryList.size(); iCnt++){
//   								String result="";
                                Map lineArr = (Map) invQryList.get(iCnt);
                                availqty = (String)lineArr.get("AVAILABLEQTY");
                                System.out.println(availqty);
   							}
   							double availableqty = Double.parseDouble(availqty);
       						new ShopifyService().UpdateShopifyInventoryItem(PLANT, ITEM_NUM, availableqty);
   						}	
						}
   					}
   				}else {
   					throw new Exception(xmlStr);
   				}
				
				new TblControlUtil().updateTblControlIESeqNo(PLANT, "GRN", "GN", GRNO);
                if(recflg.equalsIgnoreCase("ByProduct")) {
                	String message = "Order : " + PONO + "  received successfully!";
                	if (ajax) {
						//	Generate PDF and and send success response
						String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
						EmailMsgUtil emailMsgUtil = new EmailMsgUtil();
						Map<String, String> emailMsg = emailMsgUtil.getEmailMsgDetails(plant, IConstants.PURCHASE_ORDER_AR);
						String sendAttachment = emailMsg.get(IDBConstants.SEND_ATTACHMENT);
						request.setAttribute("chkdPoNo", new String[] {request.getParameter("ORDERNO")});
						if ("both".equals(sendAttachment) || "bothwithgrno".equals(sendAttachment) || "po".equals(sendAttachment) || "powithgrno".equals(sendAttachment)) {
							if (sendAttachment.contains("withgrno")) {
								request.setAttribute("printwithgrno", "Y");
							}
							viewPOReport(request, response, "printPOWITHBATCH");
						}
						//	Print with GRNO option is not available for invoice
						if ("both".equals(sendAttachment) || "bothwithgrno".equals(sendAttachment) || "invoice".equals(sendAttachment)) {
							viewPOInvoiceReport(request, response, "printPOInvoiceWithBatch");
						}
						resultJson.put("MESSAGE", message);
						resultJson.put("ERROR_CODE", "100");
						response.setContentType("application/json");
						response.setCharacterEncoding("UTF-8");
						response.getWriter().write(resultJson.toString());
						response.getWriter().flush();
						response.getWriter().close();
					}else {
                        request.getSession(false).setAttribute("RESULTINBRECEIVE",
                        		message);
                        response
                                        .sendRedirect("../purchasetransaction/receiptsummarybyproduct?action=View&ITEM="
                                                        + ITEM_NUM+"&DESC="+ITEM_DESCRIPTION); 
					}
                }if(recflg.equalsIgnoreCase("ByOrder")) {
                	String message = "Product : " + ITEM_NUM + "  received successfully!";
                	if (ajax) {
						//	Generate PDF and and send success response
						String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
						EmailMsgUtil emailMsgUtil = new EmailMsgUtil();
						Map<String, String> emailMsg = emailMsgUtil.getEmailMsgDetails(plant, IConstants.PURCHASE_ORDER_AR);
						String sendAttachment = emailMsg.get(IDBConstants.SEND_ATTACHMENT);
						request.setAttribute("chkdPoNo", new String[] {request.getParameter("ORDERNO")});
						if ("both".equals(sendAttachment) || "bothwithgrno".equals(sendAttachment) || "po".equals(sendAttachment) || "powithgrno".equals(sendAttachment)) {
							if (sendAttachment.contains("withgrno")) {
								request.setAttribute("printwithgrno", "Y");
							}
							viewPOReport(request, response, "printPOWITHBATCH");
						}
						//	Print with GRNO option is not available for invoice
						if ("both".equals(sendAttachment) || "bothwithgrno".equals(sendAttachment) || "invoice".equals(sendAttachment)) {
							viewPOInvoiceReport(request, response, "printPOInvoiceWithBatch");
						}
						resultJson.put("MESSAGE", message);
						resultJson.put("ERROR_CODE", "100");
						response.setContentType("application/json");
						response.setCharacterEncoding("UTF-8");
						response.getWriter().write(resultJson.toString());
						response.getWriter().flush();
						response.getWriter().close();
					}else {
					    request.getSession(false).setAttribute("RESULTINBRECEIVE",
					                    message);
						response
								.sendRedirect("jsp/InboundOrderSummary.jsp?action=View&PONO="
										+ PONO);
					}
                	}
			} else {
				
				String message = "Receiving Loc :" + ITEM_LOC
						+ " is not User Assigned Location";
            	if (ajax) {
            		resultJson.put("MESSAGE", message);
    				resultJson.put("ERROR_CODE", "98");
    				response.setContentType("application/json");
    				response.setCharacterEncoding("UTF-8");
    				response.getWriter().write(resultJson.toString());
    				response.getWriter().flush();
    				response.getWriter().close();
            	}else {
                    throw new Exception(message);
            	}

			}

		} catch (Exception e) {
			String message = ThrowableUtil.getMessage(e);
			if(ajax) {
				resultJson.put("MESSAGE", message);
				resultJson.put("ERROR_CODE", "99");
				response.setContentType("application/json");
				response.setCharacterEncoding("UTF-8");
				response.getWriter().write(resultJson.toString());
				response.getWriter().flush();
				response.getWriter().close();
			}else {
				this.mLogger.exception(this.printLog, "", e);
				request.getSession(false).setAttribute("QTYERROR", message);
				response
							.sendRedirect("../purchasetransaction/inboundorderreceiving?action=qtyerror&ITEMNO="
								+ ITEM_NUM
								+ "&ITEMDESC="
								+ StrUtils.replaceCharacters2Send(ITEM_DESCRIPTION)
								+ "&LOC="
								+ ITEM_LOC
								+ "&ORDERNO="
								+ PO_NUM
								+ "&ORDERLNO="
								+ PO_LN_NUM
								+ "&CUSTNAME="
								+ StrUtils.replaceCharacters2Send(CUST_NAME)
								+ "&BATCH="
								+ ITEM_BATCH
	                            + "&RECFLAG="
	                            + recflg
								+ "&REF="
								+ ITEM_REF
								+ "&ORDERQTY="
								+ QTY
								+ "&RECEIVEQTY="
								+ ITEM_QTY
								+ "&RECEIVEDQTY=" + RECEIVEDQTY+ "&UOM=" + UOM+ "&UOMQTY=" + UOMQTY);
				throw e;
			}
		}
		return xmlStr;
	}
        
        
    /* ************Modification History*********************************
	   Oct 14 2014 Bruhan, Description: To include Transaction date
	*/  
    @SuppressWarnings( { "unchecked", "static-access" })
    private String process_orderReceivingByRange(HttpServletRequest request,
                    HttpServletResponse response) throws Exception {
            UserLocUtil uslocUtil = new UserLocUtil();
            uslocUtil.setmLogger(mLogger);
            Map receiveMaterial_HM = null;
            ItemMstDAO _ItemMstDAO = new ItemMstDAO();            
            String PLANT = "", PO_NUM = "", PONO = "", ITEM_NUM = "", PO_LN_NUM = "",SRANGE="",ERANGE="",SUFFIX="",DTFRMT="";
            String ITEM_DESCRIPTION = "", LOGIN_USER = "",ISNONSTKFLG="";
            String ITEM_BATCH = "", EXPIREDATE = "", CUST_NAME = "", QTY = "", ITEM_QTY = "", RECEIVEDQTY = "", ITEM_REF = "", 
            ITEM_LOC = "", REMARKS = "", FAX = "",TRANSACTIONDATE = "",strMovHisTranDate="",strTranDate="",GRNO="",UOM="",UOMQTY="",UNITCOST="",priceval="";
            //String BALANCEQTY = "";
            double unitprice=0,totalprice=0,totalqty=0;
            boolean ajax = "XMLHttpRequest".equals(request.getHeader("X-Requested-With"));
    		JSONObject resultJson = new JSONObject();
           // UserTransaction ut = null;
            try {
                    HttpSession session = request.getSession(false);
                    PLANT = StrUtils.fString((String) session.getAttribute("PLANT"));
                    PO_NUM = StrUtils.fString(request.getParameter("ORDERNO"));
                    PONO = StrUtils.fString(request.getParameter("ORDERNO"));
                    PO_LN_NUM = StrUtils.fString(request.getParameter("ORDERLNO"));

                    CUST_NAME = StrUtils.fString(request.getParameter("CUSTNAME"));
                    ITEM_NUM = StrUtils.fString(request.getParameter("ITEMNO"));
                    ITEM_DESCRIPTION = StrUtils.fString(request
                                    .getParameter("ITEMDESC"));
                    LOGIN_USER = StrUtils.fString(request.getParameter("LOGIN_USER"));

                    QTY = StrUtils.removeFormat(StrUtils.fString(request.getParameter("ORDERQTY")));
                    RECEIVEDQTY = StrUtils.removeFormat(StrUtils.fString(request.getParameter("RECEIVEDQTY")));
        			UOMQTY = StrUtils.removeFormat(StrUtils.fString(request.getParameter("UOMQTY")));
        			UOM = StrUtils.removeFormat(StrUtils.fString(request.getParameter("UOM")));
        			UNITCOST = StrUtils.removeFormat(StrUtils.fString(request.getParameter("UNITCOST")));
                   double OrdQty = Double.parseDouble(((String) QTY.trim().toString()));
                    double receivedqty = Double.parseDouble(((String) RECEIVEDQTY.trim()));
                    double balqty = OrdQty - receivedqty;
                    balqty = StrUtils.RoundDB(balqty, IConstants.DECIMALPTS);
                    
                    ITEM_LOC =  StrUtils.fString(request.getParameter("LOC"));
                    SRANGE = StrUtils.fString(request.getParameter("SRANGE"));
                    ERANGE = StrUtils.fString(request.getParameter("ERANGE"));
                    SUFFIX = StrUtils.fString(request.getParameter("SUFFIX"));
                    DTFRMT = StrUtils.fString(request.getParameter("DTFRMT"));
                    long rangeSize = Long.parseLong(ERANGE);//-Long.parseLong(SRANGE)
                    // rangeSize = rangeSize+1;
                    ITEM_QTY = StrUtils.fString(request.getParameter("SERIALQTY"));
                    double itemqty = Double.parseDouble(((String) ITEM_QTY.trim().toString()));
                    itemqty = StrUtils.RoundDB(itemqty, IConstants.DECIMALPTS);
                    EXPIREDATE = StrUtils.fString(request.getParameter("EXPIREDATE"));
                    ITEM_REF = StrUtils.fString(request.getParameter("REF"));
//                    BALANCEQTY = StrUtils.fString(request.getParameter("BALANCEQTY"));
                    REMARKS = StrUtils.fString(request.getParameter("REMARKS"));
                    FAX = StrUtils.fString(request.getParameter("FAX"));
                     TRANSACTIONDATE = StrUtils.fString(request.getParameter("TRANSACTIONDATE"));
                     GRNO=StrUtils.fString(request.getParameter("GRNO"));//Ravindra
                     
         			
         			DateUtils dateUtils = new DateUtils();
         			String fromdates = dateUtils.parseDateddmmyyyy(TRANSACTIONDATE);
         			String time = DateUtils.getTimeHHmm();
         			String orderdate = fromdates+time+"12";
         			LocalDate date = LocalDate.parse(fromdates, DateTimeFormatter.BASIC_ISO_DATE);
         			String formattedDate = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
         			String tran = formattedDate;
         			
        			if (TRANSACTIONDATE.length()>5)
        				strMovHisTranDate    = TRANSACTIONDATE.substring(6)+"-"+TRANSACTIONDATE.substring(3,5)+"-"+TRANSACTIONDATE.substring(0,2);
        			    strTranDate    = TRANSACTIONDATE.substring(0,2)+"/"+ TRANSACTIONDATE.substring(3,5)+"/"+TRANSACTIONDATE.substring(6);
                     
					if(itemqty<=0.000)
        			{
        				throw new Exception(
        				"Error  : Receiving Qty Should be greater than Zero");
        			
        			}
//                    boolean qtyFlag = false;
                    if (balqty < itemqty*rangeSize) {
//                            qtyFlag = true;
                            throw new Exception(
                                            "Error  : Receiving Qty Should less than or equal to ordered Qty");
                    }
                    
                    boolean isvalidlocforUser = uslocUtil .isValidLocInLocmstForUser(PLANT, LOGIN_USER, ITEM_LOC);
                    _POUtil.setmLogger(mLogger);
                    if (!isvalidlocforUser) {
                        throw new Exception("Receiving Loc :" + ITEM_LOC + " is not User Assigned Location");
                    }
                    
                    ISNONSTKFLG =  new ItemMstDAO().getNonStockFlag(PLANT,ITEM_NUM);
                    
                    //Boolean transactionHandler = true;
                    //ut = DbBean.getUserTranaction();
                    //ut.begin();
                    boolean flag = false;
                            long SerialNo = 0;
                            for(int i=0;i<rangeSize;i++){
                            SerialNo= Long.parseLong(SRANGE)+i;
                          
                            ITEM_QTY = String.valueOf(itemqty);
                            ITEM_QTY = StrUtils.formatThreeDecimal(ITEM_QTY);
                            ITEM_BATCH =SUFFIX+DTFRMT+Long.toString(SerialNo);
                            

							unitprice = Double.parseDouble((String) UNITCOST.trim().toString());
							PlantMstDAO _PlantMstDAO  = new  PlantMstDAO();
							String numberOfDecimal = _PlantMstDAO.getNumberOfDecimal(PLANT);
							totalprice=totalprice+(unitprice* Float.parseFloat(ITEM_QTY));
							priceval=String.valueOf(totalprice);
							double priceValue ="".equals(priceval) ? 0.0d :  Double.parseDouble(priceval);
							priceval = StrUtils.addZeroes(priceValue, numberOfDecimal);
							totalqty=totalqty+Float.parseFloat(ITEM_QTY);
                            
                            Hashtable htInvnatch =  new Hashtable();
                            htInvnatch.put(IConstants.PLANT, PLANT);
                            htInvnatch.put(IDBConstants.USERFLD4, ITEM_BATCH);
                            boolean isExistsBatch = new InvMstDAO().isExisit(htInvnatch,"");
                            if(isExistsBatch){
                                throw new Exception(" Batch No :" +ITEM_BATCH+" Generated  already Exists to Receive " );
                            }
                            receiveMaterial_HM = new HashMap();
                            receiveMaterial_HM.put(IConstants.PLANT, PLANT);
                            receiveMaterial_HM.put(IConstants.CUSTOMER_NAME, CUST_NAME);
                            receiveMaterial_HM.put(IConstants.ITEM, ITEM_NUM);
                            receiveMaterial_HM.put(IConstants.ITEM_DESC, _ItemMstDAO.getItemDesc(PLANT ,ITEM_NUM));
                            receiveMaterial_HM.put(IConstants.PODET_PONUM, PO_NUM);
                            receiveMaterial_HM.put(IConstants.PODET_POLNNO, PO_LN_NUM);
                            receiveMaterial_HM.put(IConstants.LOC, ITEM_LOC);
                            receiveMaterial_HM.put(IConstants.LOGIN_USER, LOGIN_USER);
                            receiveMaterial_HM.put(IConstants.CUSTOMER_CODE, _POUtil.getCustCode(PLANT, PO_NUM));
                            receiveMaterial_HM.put(IConstants.INV_BATCH, ITEM_BATCH);
                            receiveMaterial_HM.put(IConstants.BATCH, ITEM_BATCH);
                            receiveMaterial_HM.put(IConstants.QTY, QTY);
                            receiveMaterial_HM.put(IConstants.ORD_QTY, QTY);
                            receiveMaterial_HM.put(IConstants.INV_QTY, ITEM_QTY);
                            receiveMaterial_HM.put(IConstants.RECV_QTY, ITEM_QTY);
                            receiveMaterial_HM.put(IConstants.INV_EXP_DATE, ITEM_REF);
                            receiveMaterial_HM.put(IConstants.USERFLD1, REMARKS);
                            receiveMaterial_HM.put(IConstants.USERFLD2, FAX);
                            receiveMaterial_HM.put(IConstants.CREATED_AT,  (String) DateUtils.getDateTime());
                            receiveMaterial_HM.put(IConstants.CREATED_BY, LOGIN_USER);

                            receiveMaterial_HM.put(IConstants.JOB_NUM, _POUtil.getJobNum( PLANT, PO_NUM));
                            receiveMaterial_HM.put(IConstants.REMARKS, ITEM_REF);
                            receiveMaterial_HM.put(IConstants.EXPIREDATE, EXPIREDATE);
                            receiveMaterial_HM.put(IConstants.TRAN_DATE, strMovHisTranDate);
                			receiveMaterial_HM.put(IConstants.RECVDATE, strTranDate);
                			receiveMaterial_HM.put("NONSTKFLAG", ISNONSTKFLG);
                			receiveMaterial_HM.put(IConstants.GRNO, GRNO);
                			receiveMaterial_HM.put(IConstants.UOM, UOM);
                			receiveMaterial_HM.put("UOMQTY", UOMQTY);

                            xmlStr = "";
                       
                            flag = _POUtil.process_ReceiveMaterialByRange(receiveMaterial_HM) && true;
                            
                            if (flag == true) {//Shopify Inventory Update
               					Hashtable htCond = new Hashtable();
               					htCond.put(IConstants.PLANT, PLANT);
               					if(new IntegrationsDAO().getShopifyConfigCount(htCond,"")) {
               						String nonstkflag = new ItemMstDAO().getNonStockFlag(PLANT,ITEM_NUM);						
            						if(nonstkflag.equalsIgnoreCase("N")) {
               						String availqty ="0";
               						ArrayList invQryList = null;
               						htCond = new Hashtable();
               						invQryList= new InvUtil().getInvListSummaryAvailableQtyforShopify(PLANT,ITEM_NUM, new ItemMstDAO().getItemDesc(PLANT, ITEM_NUM),htCond);						
               						if (invQryList.size() > 0) {					
               							for (int iCnt =0; iCnt<invQryList.size(); iCnt++){
//               								String result="";
                                            Map lineArr = (Map) invQryList.get(iCnt);
                                            availqty = (String)lineArr.get("AVAILABLEQTY");
                                            System.out.println(availqty);
               							}
               							double availableqty = Double.parseDouble(availqty);
                   						new ShopifyService().UpdateShopifyInventoryItem(PLANT, ITEM_NUM, availableqty);
               						}	
            						}
               					}
               				}
                        
                    }
                    if(flag){
                         TblControlDAO _TblControlDAO =new TblControlDAO();
                        _TblControlDAO.setmLogger(mLogger);
                         try {
                                int nxtSeq = Integer.parseInt(SRANGE)+Integer.parseInt(ERANGE);
                                Hashtable htTblCntUpdate = new Hashtable();
                                htTblCntUpdate.put(IDBConstants.PLANT,PLANT);
                                htTblCntUpdate.put(IDBConstants.TBL_FUNCTION,"RECV_BY_RANGE");
                                htTblCntUpdate.put(IDBConstants.TBL_PREFIX1,"");
                                StringBuffer updateQyery=new StringBuffer("set ");
                                updateQyery.append(IDBConstants.TBL_NEXT_SEQ +" = '"+ nxtSeq + "'");
                                flag=_TblControlDAO.update(updateQyery.toString(),htTblCntUpdate,"",PLANT);
                                
                                if(flag){
                                RecvDetDAO _RecvDetDAO = new RecvDetDAO();
            					Hashtable htRecvDet = new Hashtable();
            					htRecvDet.clear();
            					htRecvDet.put(IConstants.PLANT, PLANT);
            					htRecvDet.put(IConstants.VENDOR_CODE,  _POUtil.getCustCode(PLANT, PO_NUM));
            					htRecvDet.put(IConstants.GRNO, GRNO);                    
            					htRecvDet.put(IConstants.PODET_PONUM, PO_NUM);
            					htRecvDet.put(IConstants.STATUS, "NOT BILLED");
            					htRecvDet.put(IConstants.AMOUNT, priceval);
            					htRecvDet.put(IConstants.QTY, String.valueOf(totalqty));
            					htRecvDet.put("CRAT",DateUtils.getDateTime());
            					htRecvDet.put("CRBY",LOGIN_USER);
            					htRecvDet.put("UPAT",DateUtils.getDateTime());
                                flag = _RecvDetDAO.insertGRNtoBill(htRecvDet);
                                
                                //insert MovHis
                                MovHisDAO movHisDao = new MovHisDAO();
                        		movHisDao.setmLogger(mLogger);
                    			Hashtable htRecvHis = new Hashtable();
                    			htRecvHis.clear();
                    			htRecvHis.put(IDBConstants.PLANT, PLANT);
                    			htRecvHis.put("DIRTYPE", TransactionConstants.CREATE_GRN);
                    			htRecvHis.put(IDBConstants.ITEM, "");
                    			htRecvHis.put("MOVTID", "");
                    			htRecvHis.put("RECID", "");
                    			htRecvHis.put(IConstants.QTY, String.valueOf(totalqty));
                    			htRecvHis.put(IDBConstants.CREATED_BY, LOGIN_USER);
                    			htRecvHis.put(IDBConstants.REMARKS, PO_NUM);        					
                    			htRecvHis.put(IDBConstants.TRAN_DATE, DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));
                    			htRecvHis.put(IDBConstants.MOVHIS_ORDNUM, GRNO);
                    			htRecvHis.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
                    			flag = movHisDao.insertIntoMovHis(htRecvHis);
                                }
        
                        } catch (Exception e) {
    
                                this.mLogger.exception(this.printLog, "", e);
                                throw e;
    
                        }
                    }
                    if (flag) {
                            //DbBean.CommitTran(ut);
							new TblControlUtil().updateTblControlIESeqNo(PLANT, "GRN", "GN", GRNO);
							String message = "Product : " + ITEM_NUM + "  received successfully!";
							if (ajax) {
								//	Generate PDF and and send success response
								String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
								EmailMsgUtil emailMsgUtil = new EmailMsgUtil();
								Map<String, String> emailMsg = emailMsgUtil.getEmailMsgDetails(plant, IConstants.PURCHASE_ORDER_AR);
								String sendAttachment = emailMsg.get(IDBConstants.SEND_ATTACHMENT);
								request.setAttribute("chkdPoNo", new String[] {request.getParameter("ORDERNO")});
								if ("both".equals(sendAttachment) || "bothwithgrno".equals(sendAttachment) || "po".equals(sendAttachment) || "powithgrno".equals(sendAttachment)) {
									if (sendAttachment.contains("withgrno")) {
										request.setAttribute("printwithgrno", "Y");
									}
									viewPOReport(request, response, "printPOWITHBATCH");
								}
								//	Print with GRNO option is not available for invoice
								if ("both".equals(sendAttachment) || "bothwithgrno".equals(sendAttachment) || "invoice".equals(sendAttachment)) {
									viewPOInvoiceReport(request, response, "printPOInvoiceWithBatch");
								}
								resultJson.put("MESSAGE", message);
								resultJson.put("ERROR_CODE", "100");
								response.setContentType("application/json");
								response.setCharacterEncoding("UTF-8");
								response.getWriter().write(resultJson.toString());
								response.getWriter().flush();
								response.getWriter().close();
							}else {
	                            request.getSession(false).setAttribute("RESULTINBRECEIVE", message);
	                            response.sendRedirect("/track/OrderReceivingServlet?action=ViewByRange&PONO="+ PONO);
							}
                    } else {
                        //DbBean.RollbackTran(ut);
                    	String message = "Failed to Receive the Range..!";
                    	if (ajax) {
                    		resultJson.put("MESSAGE", message);
            				resultJson.put("ERROR_CODE", "98");
            				response.setContentType("application/json");
            				response.setCharacterEncoding("UTF-8");
            				response.getWriter().write(resultJson.toString());
            				response.getWriter().flush();
            				response.getWriter().close();
                    	}else {
                            throw new Exception(message);
                    	}
                    }

            } catch (Exception e) {

                    //DbBean.RollbackTran(ut);
                    this.mLogger.exception(this.printLog, "", e);
                    String message = ThrowableUtil.getMessage(e);
                    if (ajax) {
                    	resultJson.put("MESSAGE", message);
        				resultJson.put("ERROR_CODE", "99");
        				response.setContentType("application/json");
        				response.setCharacterEncoding("UTF-8");
        				response.getWriter().write(resultJson.toString());
        				response.getWriter().flush();
        				response.getWriter().close();
                    }else {
                    	request.getSession(false).setAttribute("QTYERROR", message);
                        response
                                         .sendRedirect("../purchasetransaction/receiptrange?action=qtyerror&ITEMNO="
                                                        + ITEM_NUM
                                                        + "&ITEMDESC="
                                                        + StrUtils.replaceCharacters2Send(ITEM_DESCRIPTION)
                                                        + "&LOC="
                                                        + ITEM_LOC
                                                        + "&ORDERNO="
                                                        + PO_NUM
                                                        + "&ORDERLNO="
                                                        + PO_LN_NUM
                                                        + "&CUSTNAME="
                                                        + StrUtils.replaceCharacters2Send(CUST_NAME)
                                                        + "&BATCH="
                                                        + ITEM_BATCH
                                                        + "&REF="
                                                        + ITEM_REF
                                                        + "&ORDERQTY="
                                                        + QTY
                                                        + "&SERIALQTY="
                                                        + ITEM_QTY
                                                         + "&RECEIVEDQTY=" + RECEIVEDQTY+ "&SRANGE=" + SRANGE+ "&ERANGE=" + ERANGE+ "&SUFFIX=" + SUFFIX+ "&DTFRMT=" + DTFRMT+"&EXPIREDATE="+EXPIREDATE+ "&UOM=" + UOM+ "&UOMQTY=" + UOMQTY);
                        throw e;
                    }
                    
            }
            return xmlStr;
    }
    
  

	private String process_orderReverseByWMS(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException,
			Exception {

//		Map receiveMaterial_HM = null;
		String PLANT = "", PO_NUM = "", CUST_NAME = "", ITEM_NUM = "";
		String ITEM_DESCRIPTION = "", LOGIN_USER = "";
		String ITEM_BATCH = "", QTY = "", RECEIVEDQTY = "", REVERSEDQTY = "", REVERSEQTY = "", ITEM_EXPDATE = "", ITEM_LOC = "";
//		String ITEM_QTY = "", PO_LN_NUM = "", PO_LNNO = "";

		HttpSession session = request.getSession(false);
		PLANT = StrUtils.fString((String) session.getAttribute("PLANT"));

		RecvDetDAO _RecvDetDAO = new RecvDetDAO();
		PoDetDAO _PoDetDAO = new PoDetDAO(PLANT);
		InvMstDAO _InvMstDAO = new InvMstDAO(PLANT);
		_RecvDetDAO.setmLogger(mLogger);
		_PoDetDAO.setmLogger(mLogger);
		_InvMstDAO.setmLogger(mLogger);

		boolean flag = false;
		boolean flagrecvinsert = false;
		boolean flagpodet = false;
		boolean flaginvdet = false;

		try {

			LOGIN_USER = StrUtils.fString(request.getParameter("LOGIN_USER"));
			PO_NUM = StrUtils.fString(request.getParameter("ORDERNO"));
//			PO_LNNO = StrUtils.fString(request.getParameter("ORDERLNO"));
			CUST_NAME = StrUtils.fString(request.getParameter("CUSTNAME"));
			ITEM_NUM = StrUtils.fString(request.getParameter("ITEMNO"));
			ITEM_DESCRIPTION = StrUtils.fString(request.getParameter("ITEMDESC"));
			ITEM_LOC = StrUtils.fString(request.getParameter("LOC"));
			ITEM_BATCH = StrUtils.fString(request.getParameter("BATCH"));
			ITEM_EXPDATE = StrUtils.fString(request.getParameter("REF"));
			QTY = StrUtils.fString(request.getParameter("ORDERQTY"));
			RECEIVEDQTY = StrUtils.fString(request.getParameter("RECEIVEQTY"));
			REVERSEDQTY = StrUtils.fString(request.getParameter("REVERSEDQTY"));
			REVERSEQTY = StrUtils.fString(request.getParameter("REVERSEQTY"));
			double revrsqty = Double.parseDouble(REVERSEQTY);
			revrsqty = StrUtils.RoundDB(revrsqty, IConstants.DECIMALPTS);
			REVERSEQTY = String.valueOf(revrsqty);
			Hashtable htRecvDet = new Hashtable();
			htRecvDet.clear();
			htRecvDet.put(IDBConstants.PLANT, PLANT);
			htRecvDet.put(IDBConstants.PODET_PONUM, PO_NUM);
			htRecvDet.put(IDBConstants.CUSTOMER_NAME, CUST_NAME);
			htRecvDet.put(IDBConstants.ITEM, ITEM_NUM);
			htRecvDet.put(IDBConstants.ITEM_DESC, StrUtils.InsertQuotes(ITEM_DESCRIPTION));
			htRecvDet.put(IDBConstants.LOC, ITEM_LOC);
			htRecvDet.put("BATCH", ITEM_BATCH);
			htRecvDet.put("REMARK", ITEM_EXPDATE);
			htRecvDet.put("ORDQTY", QTY);
			htRecvDet.put("RECQTY", RECEIVEDQTY);
			htRecvDet.put("REVERSEQTY", REVERSEQTY);
			htRecvDet.put(IDBConstants.CREATED_AT, (String) DateUtils.getDateTime());
			htRecvDet.put(IDBConstants.CREATED_BY, (String) LOGIN_USER);
		        htRecvDet.put(IDBConstants.RECVDET_TRANTYPE, "IB");
			int receivedqty = Integer.parseInt(((String) RECEIVEDQTY.trim()));
			int revQty = Integer.parseInt(((String) REVERSEQTY.trim().toString()));

//			boolean qtyFlag = false;
			if (receivedqty < revQty) {
//				qtyFlag = true;
				throw new Exception(
						"Error : Cannot Receive more than Order Qty!");
			}

			flagrecvinsert = _RecvDetDAO.insertRecvDet(htRecvDet);

			if (!flagrecvinsert) {
				flag = false;
				throw new Exception(
						"Product Reversed  Failed, Error In Insert RECVDET :"
								+ " " + ITEM_NUM);
			} else {
				flag = true;
			}

//			String extCont = "";
//			String sLnstat = "O";
			Hashtable htPoDet = new Hashtable();
			htPoDet.put("PLANT", (String) PLANT);
			htPoDet.put(IDBConstants.PODET_PONUM, PO_NUM);
			htPoDet.put(IDBConstants.ITEM, ITEM_NUM);
			String updatePoDet = "";

			updatePoDet = "set qtyrc= isNull(qtyrc,0) - " + REVERSEQTY
					+ " , LNSTAT='O' ";

			flagpodet = _PoDetDAO.update(updatePoDet, htPoDet, "");
			if (!flagpodet) {
				flag = false;
				throw new Exception(
						"Product Reversed  Failed, Error In update  PODET :"
								+ " " + ITEM_NUM);
			} else {
				flag = true;
			}

			Hashtable htInvMst = new Hashtable();
			htInvMst.put("PLANT", (String) PLANT);
			htInvMst.put(IDBConstants.ITEM, ITEM_NUM);
			htInvMst.put(IDBConstants.LOC, ITEM_LOC);
			htInvMst.put(IDBConstants.USERFLD4, ITEM_BATCH);
			String updateInvDet = "";
			updateInvDet = "set qty= isNull(qty,0) - " + REVERSEQTY + "";
			flaginvdet = _InvMstDAO.update(updateInvDet, htInvMst, "");
			if (!flaginvdet) {
				flag = false;
				throw new Exception(
						"Product Reversed  Failed, Not Enough Inventory available for product :"
								+ " " + ITEM_NUM +" in Loc "+ ITEM_LOC);
			} else {
				flag = true;
			}

			MovHisDAO mdao = new MovHisDAO(PLANT);
			mdao.setmLogger(mLogger);
			Hashtable htm = new Hashtable();
			htm.put("PLANT", (String) PLANT);
			htm.put("DIRTYPE", TransactionConstants.IB_REVERSE);
			htm.put("ORDNUM", PO_NUM);
			htm.put("RECID", "");
			htm.put("ITEM", ITEM_NUM);
			htm.put(IDBConstants.REMARKS, ITEM_EXPDATE);
			htm.put("LOC", ITEM_LOC);
			htm.put("Batno", ITEM_BATCH);
			htm.put("qty", REVERSEQTY);
			htm.put(IDBConstants.TRAN_DATE, DateUtils
					.getDateinyyyy_mm_dd(DateUtils.getDate()));
			htm.put("CRBY", (String) LOGIN_USER);
			htm.put("CRAT", (String) DateUtils.getDateTime());
			boolean inserted = mdao.insertIntoMovHis(htm);

			if (!inserted) {
				flag = false;
				throw new Exception(
						"Product Reversed  Failed, Error In update  MOVHIS :"
								+ " " + ITEM_NUM);
			} else {
				flag = true;
			}
			if (flag) {
				request.getSession(false).setAttribute("RESULT",
						"Product : " + ITEM_NUM + "  reversed successfully!");
				response
						.sendRedirect("jsp/InboundOrderSummary.jsp?action=View&PLANT="
								+ PLANT + "&PONO=" + PO_NUM);

			} else {
				request.getSession(false).setAttribute("RESULTERROR",
						"Error in reversing item : " + ITEM_NUM + " Order");
				response
						.sendRedirect("jsp/InboundOrderReverse.jsp?action=resulterror");
			}

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			request.getSession(false).setAttribute("CATCHREVERSERROR",
					e.getMessage());
			response
					.sendRedirect("jsp/InboundOrderReverse.jsp?action=catchreverseerror&ORDERNO="
							+ PO_NUM
							+ "&CUSTNAME="
							+ CUST_NAME
							+ "&ITEMNO="
							+ ITEM_NUM
							+ "&ITEMDESC="
							+ ITEM_DESCRIPTION
							+ "&LOC="
							+ ITEM_LOC
							+ "&BATCH="
							+ ITEM_BATCH
							+ "&REF="
							+ ITEM_EXPDATE
							+ "&ORDERQTY="
							+ QTY
							+ "&RECEIVEQTY="
							+ RECEIVEDQTY
							+ "&REVERSEDQTY="
							+ REVERSEDQTY);

			throw e;
		}
		return xmlStr;
	}

	private String process_GenerateBatchByWMS(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException,
			Exception {

		Map receiveMaterial_HM = null;
		String PLANT = "", PO_NUM = "", ITEM_NUM = "", PO_LN_NUM = "";
		String ITEM_DESCRIPTION = "", LOGIN_USER = "";
		String ITEM_BATCH = "", CUST_NAME = "", QTY = "", ITEM_QTY = "", RECEIVEDQTY = "", BALANCEQTY = "", ITEM_EXPDATE = "", ITEM_LOC = "", BATCHDATE = "";

		boolean flag = false;
		boolean updateFlag = false;
		boolean insertFlag = false;
		boolean resultflag = false;
		String sBatchSeq = "";
		String extCond = "";
		String rtnBatch = "";
		String sZero = "";

		HttpSession session = request.getSession(false);
		PLANT = StrUtils.fString((String) session.getAttribute("PLANT"));
		PO_NUM = StrUtils.fString(request.getParameter("ORDERNO"));
		PO_LN_NUM = StrUtils.fString(request.getParameter("ORDERLNO"));
		CUST_NAME = StrUtils.fString(request.getParameter("CUSTNAME"));
		ITEM_NUM = StrUtils.fString(request.getParameter("ITEMNO"));
		ITEM_DESCRIPTION = StrUtils.fString(request.getParameter("ITEMDESC"));
		LOGIN_USER = StrUtils.fString(request.getParameter("LOGIN_USER"));
		ITEM_BATCH = StrUtils.fString(request.getParameter("BATCH"));
		QTY = StrUtils.fString(request.getParameter("ORDERQTY"));
		RECEIVEDQTY = StrUtils.fString(request.getParameter("RECEIVEDQTY"));
		int OrdQty = Integer.parseInt(((String) QTY.trim().toString()));
		int receivedqty = Integer.parseInt(((String) RECEIVEDQTY.trim()));
		int balqty = OrdQty - receivedqty;
		ITEM_QTY = StrUtils.fString(request.getParameter("RECEIVEQTY"));
		ITEM_EXPDATE = StrUtils.fString(request.getParameter("REF"));

		ITEM_LOC = StrUtils.fString(request.getParameter("LOC"));
		BALANCEQTY = StrUtils.fString(request.getParameter("BALANCEQTY"));

		try {

			TblControlDAO _TblControlDAO = new TblControlDAO();
			_TblControlDAO.setmLogger(mLogger);
			BATCHDATE = _TblControlDAO.getBatchDate();
			Hashtable ht = new Hashtable();

			String query = " isnull(NXTSEQ,'') as NXTSEQ";
			ht.put(IDBConstants.PLANT, PLANT);
			ht.put(IDBConstants.TBL_FUNCTION, IDBConstants.TBL_BATCH_CAPTION);
			ht.put(IDBConstants.TBL_PREFIX1, BATCHDATE.substring(0, 6));
			boolean exitFlag = false;
			exitFlag = _TblControlDAO.isExisit(ht, extCond, PLANT);

			if (exitFlag == false) {

				rtnBatch = BATCHDATE.substring(0, 6) + "-" + "0000"
						+ IDBConstants.TBL_FIRST_NEX_SEQ;
				Map htInsert = null;
				Hashtable htTblCntInsert = new Hashtable();

				htTblCntInsert.put(IDBConstants.PLANT, (String) PLANT);

				htTblCntInsert.put(IDBConstants.TBL_FUNCTION,
						(String) IDBConstants.TBL_BATCH_CAPTION);
				htTblCntInsert.put(IDBConstants.TBL_PREFIX1, (String) BATCHDATE
						.substring(0, 6));
				htTblCntInsert.put(IDBConstants.TBL_NEXT_SEQ,
						(String) IDBConstants.TBL_FIRST_NEX_SEQ);
				htTblCntInsert
						.put(IDBConstants.CREATED_BY, (String) LOGIN_USER);
				htTblCntInsert.put(IDBConstants.CREATED_AT,
						(String) DateUtils.getDateTime());
				insertFlag = _TblControlDAO.insertTblControl(htTblCntInsert,
						PLANT);

				MovHisDAO mdao = new MovHisDAO(PLANT);
				mdao.setmLogger(mLogger);
				Hashtable htm = new Hashtable();
				htm.put("PLANT", (String) PLANT);
				htm.put("DIRTYPE", TransactionConstants.GENERATE_BATCH);
				htm.put("RECID", "");
				htm.put("CRBY", (String) LOGIN_USER);
				htm.put("CRAT", (String) DateUtils.getDateTime());
				boolean inserted = mdao.insertIntoMovHis(htm);
				if (insertFlag) {
					resultflag = true;
				} else if (!insertFlag) {

					throw new Exception(
							"Generate BatchNumber Failed, Error In Inserting Table:"
									+ " " + PLANT + "_ " + "TBLCONTROL");
				} else if (inserted) {
					resultflag = true;
				} else if (!inserted) {

					throw new Exception(
							"Generate BatchNumber Failed,  Error In Inserting Table:"
									+ " " + PLANT + "_ " + "MOVHIS");
				}

			} else {

				Map m = _TblControlDAO.selectRow(query, ht, extCond);
				sBatchSeq = (String) m.get("NXTSEQ");
				if (sBatchSeq.length() == 1) {
					sZero = "0000";
				} else if (sBatchSeq.length() == 2) {
					sZero = "000";
				} else if (sBatchSeq.length() == 3) {
					sZero = "00";
				} else if (sBatchSeq.length() == 4) {
					sZero = "0";
				}

				int inxtSeq = Integer.parseInt(((String) sBatchSeq.trim()
						.toString())) + 1;

				String updatedSeq = Integer.toString(inxtSeq);
				rtnBatch = BATCHDATE.substring(0, 6) + "-" + sZero + updatedSeq;

				Map htUpdate = null;

				Hashtable htTblCntUpdate = new Hashtable();
				htTblCntUpdate.put(IDBConstants.PLANT, PLANT);

				htTblCntUpdate.put(IDBConstants.TBL_FUNCTION,
						IDBConstants.TBL_BATCH_CAPTION);
				htTblCntUpdate.put(IDBConstants.TBL_PREFIX1, BATCHDATE
						.substring(0, 6));
				StringBuffer updateQyery = new StringBuffer("set ");
				updateQyery.append(IDBConstants.TBL_NEXT_SEQ + " = '"
						+ (String) updatedSeq.toString() + "'");

				updateFlag = _TblControlDAO.update(updateQyery.toString(),
						htTblCntUpdate, extCond, PLANT);

				MovHisDAO mdao = new MovHisDAO(PLANT);
				Hashtable htm = new Hashtable();
				htm.put("PLANT", (String) PLANT);
				htm.put("DIRTYPE", TransactionConstants.UPDATE_BATCH);
				htm.put("RECID", "");
				htm.put("CRBY", (String) LOGIN_USER);
				htm.put("CRAT", (String) DateUtils.getDateTime());
				boolean inserted = mdao.insertIntoMovHis(htm);

				if (updateFlag) {
					resultflag = true;
				} else if (!updateFlag) {

					throw new Exception(
							"Update BatchNumber Failed, Error In Updating Table:"
									+ " " + PLANT + "_ " + "TBLCONTROL");
				} else if (inserted) {
					resultflag = true;
				}

				else if (!inserted) {

					throw new Exception(
							"Update BatchNumber Failed,  Error In Inserting Table:"
									+ " " + PLANT + "_ " + "MOVHIS");
				}

			}

			if (resultflag) {

				request.getSession(false).setAttribute("BATCHRESULT", "");
				response
						.sendRedirect("jsp/InboundOrderReceiving.jsp?action=batchresult&ITEMNO="
								+ ITEM_NUM
								+ "&ITEMDESC="
								+ StrUtils
										.replaceCharacters2Send(ITEM_DESCRIPTION)
								+ "&LOC="
								+ ITEM_LOC
								+ "&ORDERNO="
								+ PO_NUM
								+ "&ORDERLNO="
								+ PO_LN_NUM
								+ "&CUSTNAME="
								+ StrUtils.replaceCharacters2Send(CUST_NAME)
								+ "&BATCH="
								+ rtnBatch
								+ "&REF="
								+ ITEM_EXPDATE
								+ "&ORDERQTY="
								+ QTY
								+ "&RECEIVEQTY="
								+ ITEM_QTY + "&RECEIVEDQTY=" + RECEIVEDQTY);

			} else {
				request.getSession(false).setAttribute("BATCHERROR",
						"Error In Batch Generation");
				response
						.sendRedirect("jsp/InboundOrderReceiving.jsp?action=batcherror&ITEMNO="
								+ ITEM_NUM
								+ "&ITEMDESC="
								+ StrUtils
										.replaceCharacters2Send(ITEM_DESCRIPTION)
								+ "&LOC="
								+ ITEM_LOC
								+ "&ORDERNO="
								+ PO_NUM
								+ "&ORDERLNO="
								+ PO_LN_NUM
								+ "&CUSTNAME="
								+ StrUtils.replaceCharacters2Send(CUST_NAME)
								+ "&BATCH="
								+ ITEM_BATCH
								+ "&REF="
								+ ITEM_EXPDATE
								+ "&ORDERQTY="
								+ QTY
								+ "&RECEIVEQTY="
								+ ITEM_QTY
								+ "&RECEIVEDQTY="
								+ RECEIVEDQTY);

			}

		} catch (Exception e) {
			MLogger.log(0, "" + e.getMessage());
			request.getSession(false).setAttribute("CATCHBATCHERROR",
					"Batch Generation Error:" + e.getMessage());
			response
					.sendRedirect("jsp/InboundOrderReceiving.jsp?action=catchbatcherror&ITEMNO="
							+ ITEM_NUM
							+ "&ITEMDESC="
							+ StrUtils.replaceCharacters2Send(ITEM_DESCRIPTION)
							+ "&LOC="
							+ ITEM_LOC
							+ "&ORDERNO="
							+ PO_NUM
							+ "&ORDERLNO="
							+ PO_LN_NUM
							+ "&CUSTNAME="
							+ StrUtils.replaceCharacters2Send(CUST_NAME)
							+ "&BATCH="
							+ ITEM_BATCH
							+ "&REF="
							+ ITEM_EXPDATE
							+ "&ORDERQTY="
							+ QTY
							+ "&RECEIVEQTY="
							+ ITEM_QTY
							+ "&RECEIVEDQTY=" + RECEIVEDQTY);

			throw e;
		}
		return xmlStr;
	}

		private String process_orderReceiving(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException,
			Exception {

		Map receiveMaterial_HM = null;
		String PLANT = "", PO_NUM = "", ITEM_NUM = "", PO_LN_NUM = "";
		String ITEM_DESCRIPTION = "", LOGIN_USER = "",ISNONSTKFLG="";
		String ITEM_BATCH = "", ITEM_QTY = "", EXPIREDATE = "", ITEM_EXPDATE = "", ITEM_LOC = "", CUST_NAME = "", ITEM_REMARKS = "", REMARKS = "", ORD_QTY = "";
		try {
			PLANT = StrUtils.fString(request.getParameter("PLANT"));
			PO_NUM = StrUtils.fString(request.getParameter("PO_NUM"));
			PO_LN_NUM = StrUtils.fString(request.getParameter("PO_LN_NUM"));
			ITEM_NUM = StrUtils.fString(request.getParameter("ITEM_NUM"));
			ITEM_DESCRIPTION = StrUtils.InsertQuotes(StrUtils.replaceCharacters2Recv(StrUtils.fString(request.getParameter("ITEM_DESC"))));
			LOGIN_USER = StrUtils.fString(request.getParameter("LOGIN_USER"));
			CUST_NAME = StrUtils.fString(request.getParameter("CUSTNAME"));
			ITEM_BATCH = StrUtils.fString(request.getParameter("ITEM_BATCH"));
			ITEM_QTY = StrUtils.fString(request.getParameter("ITEM_QTY"));
			ORD_QTY = StrUtils.fString(request.getParameter("ORD_QTY"));
			ITEM_REMARKS = StrUtils.fString(request.getParameter("ITEM_REMARKS"));
			REMARKS = StrUtils.fString(request.getParameter("REMARKS"));
			ITEM_LOC = StrUtils.fString(request.getParameter("ITEM_LOC"));
			EXPIREDATE = StrUtils.fString(request.getParameter("EXPIREDATE"));
			UserLocUtil uslocUtil = new UserLocUtil();
			uslocUtil.setmLogger(mLogger);
			boolean isvalidlocforUser = uslocUtil.isValidLocInLocmstForUser(
					PLANT, LOGIN_USER, ITEM_LOC);
			if (!isvalidlocforUser) {
				throw new Exception(" Loc :" + ITEM_LOC
						+ " is not User Assigned Location");
			}
			
		double itemqty1 = Double.parseDouble(ITEM_QTY);
		itemqty1 = StrUtils.RoundDB(itemqty1, IConstants.DECIMALPTS);
		ITEM_QTY = String.valueOf(itemqty1);
		ITEM_QTY = StrUtils.formatThreeDecimal(ITEM_QTY);
		ORD_QTY = StrUtils.removeFormat(ORD_QTY);

		String curDate =DateUtils.getDate();
		String strTranDate    = curDate.substring(6)+"-"+ curDate.substring(3,5)+"-"+curDate.substring(0,2);
		String strRecvDate    = curDate.substring(0,2)+"/"+ curDate.substring(3,5)+"/"+curDate.substring(6);

		ISNONSTKFLG =  new ItemMstDAO().getNonStockFlag(PLANT,ITEM_NUM);
		
			receiveMaterial_HM = new HashMap();
			receiveMaterial_HM.put(IConstants.PLANT, PLANT);
			receiveMaterial_HM.put(IConstants.ITEM, ITEM_NUM);
			receiveMaterial_HM.put(IConstants.ITEM_DESC, ITEM_DESCRIPTION);
			receiveMaterial_HM.put(IConstants.PODET_PONUM, PO_NUM);
			receiveMaterial_HM.put(IConstants.PODET_POLNNO, PO_LN_NUM);
			receiveMaterial_HM.put(IConstants.CUSTOMER_NAME, CUST_NAME);
			receiveMaterial_HM.put(IConstants.LOC, ITEM_LOC);
			receiveMaterial_HM.put(IConstants.LOGIN_USER, LOGIN_USER);
			receiveMaterial_HM.put(IConstants.CUSTOMER_CODE, _POUtil
					.getCustCode(PLANT, PO_NUM));
			receiveMaterial_HM.put(IConstants.INV_BATCH, ITEM_BATCH);
			receiveMaterial_HM.put(IConstants.BATCH, ITEM_BATCH);
			receiveMaterial_HM.put(IConstants.INV_QTY, ITEM_QTY);
			receiveMaterial_HM.put(IConstants.RECV_QTY, ITEM_QTY);
			receiveMaterial_HM.put(IConstants.ORD_QTY, ORD_QTY);
			receiveMaterial_HM.put(IConstants.USERFLD1, ITEM_REMARKS);
			receiveMaterial_HM.put(IConstants.USERFLD2, "");
			receiveMaterial_HM.put(IConstants.INV_EXP_DATE, "");
			receiveMaterial_HM.put(IConstants.CREATED_AT,
					(String) DateUtils.getDateTime());
			receiveMaterial_HM.put(IConstants.CREATED_BY, LOGIN_USER);
			receiveMaterial_HM.put(IConstants.REMARKS, REMARKS);
			receiveMaterial_HM.put(IConstants.JOB_NUM, _POUtil.getJobNum(PLANT,
					PO_NUM));
			receiveMaterial_HM.put(IConstants.EXPIREDATE, EXPIREDATE);

			receiveMaterial_HM.put(IConstants.TRAN_DATE, strTranDate);
			receiveMaterial_HM.put(IConstants.RECVDATE, strRecvDate);
			receiveMaterial_HM.put("NONSTKFLAG", ISNONSTKFLG);
			xmlStr = "";

			Hashtable htLocMst = new Hashtable();
			htLocMst.put("plant", receiveMaterial_HM.get(IConstants.PLANT));
			htLocMst.put("item", receiveMaterial_HM.get(IConstants.ITEM));
			htLocMst.put("loc", receiveMaterial_HM.get(IConstants.LOC));

			boolean flag = true;

			if (flag) {

				xmlStr = _POUtil.process_ReceiveMaterial(receiveMaterial_HM);
			} else {
				throw new Exception(" Product Received already ");
			}

		} catch (Exception e) {
			MLogger.log(0, "" + e.getMessage());
			throw e;
		}
		return xmlStr;
	}
	private String process_orderReceiving_random(HttpServletRequest request,
				HttpServletResponse response) throws IOException, ServletException,
				Exception {
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();
        String json_str = request.getParameter("finalresult");
        out.print(json_str);
      
        JSONArray hostArray = JSONArray.fromObject(json_str);
        
       
   
       			Map receiveMaterial_HM = null;
			String PLANT = "", PO_NUM = "", ITEM_NUM = "", PO_LN_NUM = "";
			String ITEM_DESCRIPTION = "", LOGIN_USER = "",ISNONSTKFLG="";
			String ITEM_BATCH = "", ITEM_QTY = "", EXPIREDATE = "", ITEM_EXPDATE = "", ITEM_LOC = "",UOM="", 
			CUST_NAME = "", ITEM_REMARKS = "", REMARKS = "", ORD_QTY = "",GRNO="",EMPNO="";
			
			 for(int i = 0; i < hostArray.size(); i++)
		        {
		        JSONObject hostObject = hostArray.getJSONObject(i);
		         PLANT = hostObject.getString("companyCode");
		         LOGIN_USER = hostObject.getString("loginUser");
		         PO_NUM = hostObject.getString("pono");
		         ITEM_NUM = hostObject.getString("itemNum");
		         ITEM_DESCRIPTION = hostObject.getString("itemDesc");
		         ITEM_BATCH = hostObject.getString("itemBatch");
		         ITEM_QTY = hostObject.getString("itemQty");
		         ITEM_LOC = hostObject.getString("itemLoc");
		         UOM = hostObject.getString("uom");
		         EMPNO = hostObject.getString("employeeNo");
		         GRNO = hostObject.getString("grno");
		         EXPIREDATE = hostObject.getString("expiryDate");
		         
		    

			try {
/*				PLANT = StrUtils.fString(request.getParameter("PLANT"));
				PO_NUM = StrUtils.fString(request.getParameter("PO_NUM"));
				//PO_LN_NUM = StrUtils.fString(request.getParameter("PO_LN_NUM"));
				ITEM_NUM = StrUtils.fString(request.getParameter("ITEM_NUM"));
				ITEM_DESCRIPTION = strUtils.InsertQuotes(strUtils.replaceCharacters2Recv(strUtils.fString(request.getParameter("ITEM_DESC"))));
				LOGIN_USER = StrUtils.fString(request.getParameter("LOGIN_USER"));
				CUST_NAME = StrUtils.fString(request.getParameter("CUSTNAME"));
				ITEM_BATCH = StrUtils.fString(request.getParameter("ITEM_BATCH"));
				ITEM_QTY = StrUtils.fString(request.getParameter("ITEM_QTY"));
				//ORD_QTY = StrUtils.fString(request.getParameter("ORD_QTY"));
				ITEM_REMARKS = StrUtils.fString(request.getParameter("ITEM_REMARKS"));
				REMARKS = StrUtils.fString(request.getParameter("REMARKS"));
				ITEM_LOC = StrUtils.fString(request.getParameter("ITEM_LOC"));
				EXPIREDATE = StrUtils.fString(request.getParameter("EXPIREDATE"));
				GRNO=StrUtils.fString(request.getParameter("GRNO"));
				EMPNO=StrUtils.fString(request.getParameter("EMPNO"));
				UOM=StrUtils.fString(request.getParameter("UOM"));*/
				UserLocUtil uslocUtil = new UserLocUtil();
				uslocUtil.setmLogger(mLogger);
				boolean isvalidlocforUser = uslocUtil.isValidLocInLocmstForUser(
						PLANT, LOGIN_USER, ITEM_LOC);
				if (!isvalidlocforUser) {
					throw new Exception(" Loc :" + ITEM_LOC
							+ " is not User Assigned Location");
				}
				String curDate =DateUtils.getDate();
				String strTranDate    = curDate.substring(6)+"-"+ curDate.substring(3,5)+"-"+curDate.substring(0,2);
				String strRecvDate    = curDate.substring(0,2)+"/"+ curDate.substring(3,5)+"/"+curDate.substring(6);
				
				ISNONSTKFLG =  new ItemMstDAO().getNonStockFlag(PLANT,ITEM_NUM);
				
			    double itemqty1 = Double.parseDouble(ITEM_QTY);
			    itemqty1 = StrUtils.RoundDB(itemqty1, IConstants.DECIMALPTS);
			    ITEM_QTY = String.valueOf(itemqty1);
			    ITEM_QTY = StrUtils.formatThreeDecimal(ITEM_QTY);
			 	receiveMaterial_HM = new HashMap();
				receiveMaterial_HM.put(IConstants.PLANT, PLANT);
				receiveMaterial_HM.put(IConstants.ITEM, ITEM_NUM);
				receiveMaterial_HM.put(IConstants.ITEM_DESC, ITEM_DESCRIPTION);
				receiveMaterial_HM.put(IConstants.PODET_PONUM, PO_NUM);
				//receiveMaterial_HM.put(IConstants.PODET_POLNNO, PO_LN_NUM);
				receiveMaterial_HM.put(IConstants.CUSTOMER_NAME, CUST_NAME);
				receiveMaterial_HM.put(IConstants.LOC, ITEM_LOC);
				receiveMaterial_HM.put(IConstants.LOGIN_USER, LOGIN_USER);
				receiveMaterial_HM.put(IConstants.CUSTOMER_CODE, _POUtil
						.getCustCode(PLANT, PO_NUM));
				receiveMaterial_HM.put(IConstants.INV_BATCH, ITEM_BATCH);
				receiveMaterial_HM.put(IConstants.BATCH, ITEM_BATCH);
				receiveMaterial_HM.put(IConstants.INV_QTY, ITEM_QTY);
				receiveMaterial_HM.put(IConstants.RECV_QTY, ITEM_QTY);
				//receiveMaterial_HM.put(IConstants.ORD_QTY, ORD_QTY);
				receiveMaterial_HM.put(IConstants.USERFLD1, ITEM_REMARKS);
				receiveMaterial_HM.put(IConstants.USERFLD2, "");
				receiveMaterial_HM.put(IConstants.INV_EXP_DATE, "");
				receiveMaterial_HM.put(IConstants.CREATED_AT,(String) DateUtils.getDateTime());
				receiveMaterial_HM.put(IConstants.CREATED_BY, LOGIN_USER);
				receiveMaterial_HM.put(IConstants.REMARKS, REMARKS);
				receiveMaterial_HM.put(IConstants.JOB_NUM, _POUtil.getJobNum(PLANT,	PO_NUM));
				receiveMaterial_HM.put(IConstants.EXPIREDATE, EXPIREDATE);
				receiveMaterial_HM.put("NONSTKFLAG", ISNONSTKFLG);
				receiveMaterial_HM.put(IConstants.TRAN_DATE, strTranDate);
				receiveMaterial_HM.put(IConstants.RECVDATE, strRecvDate);
				receiveMaterial_HM.put(IConstants.GRNO, GRNO);
				receiveMaterial_HM.put(IConstants.EMPNO, EMPNO);
				receiveMaterial_HM.put(IConstants.UNITMO, UOM);
				xmlStr = "";
				Hashtable htLocMst = new Hashtable();
				htLocMst.put("plant", receiveMaterial_HM.get(IConstants.PLANT));
				htLocMst.put("item", receiveMaterial_HM.get(IConstants.ITEM));
				htLocMst.put("loc", receiveMaterial_HM.get(IConstants.LOC));
				boolean flag = true;
				if (flag) {
					if(!GRNO.isEmpty()){
					flag=_RecvDetDAO.isExisit("select count(*) from "+PLANT+"_RECVDET where PONO='"+PO_NUM+"' and GRNO='"+GRNO+"'");
					}
					else{
						 flag = false;
					}
					xmlStr = _POUtil.process_ReceiveMaterial_Random(receiveMaterial_HM);
					if (!flag) {
						new TblControlUtil().updateTblControlIESeqNo(PLANT, "GRN", "GN", GRNO);
						}
					//To GRN
					if(xmlStr.contains("received successfully"))
					{
						double unitprice=0,totalprice=0,totalqty=0;
						String priceval="";
						Hashtable htPoDet = new Hashtable();
						ArrayList PODetails = null;
						String queryPoDet = "(UNITCOST*CURRENCYUSEQT) as UNITCOST";
						htPoDet.put(IConstants.PODET_PONUM, PO_NUM);
						htPoDet.put(IConstants.PLANT, PLANT);
						htPoDet.put(IConstants.ITEM, ITEM_NUM);
						htPoDet.put(IConstants.UNITMO, UOM);
						PODetails = _POUtil.getPoDetDetails(queryPoDet, htPoDet);
							if(PODetails.size() > 0) {	
												Map map1 = (Map) PODetails.get(0);
													unitprice = Double.parseDouble((String) map1.get("UNITCOST"));
													PlantMstDAO _PlantMstDAO  = new  PlantMstDAO();
													StrUtils strUtils     = new StrUtils();
													String numberOfDecimal = _PlantMstDAO.getNumberOfDecimal(PLANT);
													totalprice=totalprice+(unitprice* Float.parseFloat(ITEM_QTY));
													priceval=String.valueOf(totalprice);
													double priceValue ="".equals(priceval) ? 0.0d :  Double.parseDouble(priceval);
													priceval = StrUtils.addZeroes(priceValue, numberOfDecimal);
							}
							//insert GRNTOBILL
							RecvDetDAO _RecvDetDAO = new RecvDetDAO();
							Hashtable htRecvDet = new Hashtable();
							htRecvDet.clear();
							htRecvDet.put(IConstants.PLANT, PLANT);
							htRecvDet.put(IConstants.VENDOR_CODE, _POUtil
									.getCustCode(PLANT, PO_NUM));
							htRecvDet.put(IConstants.GRNO, GRNO);                    
							htRecvDet.put(IConstants.PODET_PONUM, PO_NUM);
							htRecvDet.put(IConstants.STATUS, "NOT BILLED");
							htRecvDet.put(IConstants.AMOUNT, priceval);
							htRecvDet.put(IConstants.QTY, String.valueOf(ITEM_QTY));
							htRecvDet.put("CRAT",DateUtils.getDateTime());
							htRecvDet.put("CRBY",LOGIN_USER);
							htRecvDet.put("UPAT",DateUtils.getDateTime());
							
							//insert MovHis
		                    MovHisDAO movHisDao = new MovHisDAO();
		            		movHisDao.setmLogger(mLogger);
		        			Hashtable htRecvHis = new Hashtable();
		        			htRecvHis.clear();
		        			htRecvHis.put(IDBConstants.PLANT, PLANT);
		        			htRecvHis.put("DIRTYPE", TransactionConstants.CREATE_GRN);
		        			htRecvHis.put(IDBConstants.ITEM, "");
		        			htRecvHis.put("MOVTID", "");
		        			htRecvHis.put("RECID", "");
		        			htRecvHis.put(IConstants.QTY, String.valueOf(ITEM_QTY));
		        			htRecvHis.put(IDBConstants.CREATED_BY, LOGIN_USER);
		        			htRecvHis.put(IDBConstants.REMARKS, PO_NUM);        					
		        			htRecvHis.put(IDBConstants.TRAN_DATE, DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));
		        			htRecvHis.put(IDBConstants.MOVHIS_ORDNUM, GRNO);
		        			htRecvHis.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
							
							flag=_RecvDetDAO.isExisit("select count(*) from "+PLANT+"_FINGRNOTOBILL where PONO='"+PO_NUM+"' and GRNO='"+GRNO+"'");
							
							if (!flag) {
								flag = _RecvDetDAO.insertGRNtoBill(htRecvDet);
								flag = movHisDao.insertIntoMovHis(htRecvHis);
							} 
							else
							{								
								htRecvDet = new Hashtable();
								htRecvDet.clear();
								htRecvDet.put(IConstants.PLANT, PLANT);
								htRecvDet.put(IConstants.GRNO, GRNO);
								htRecvDet.put(IConstants.PODET_PONUM, PO_NUM);
								
								htRecvHis = new Hashtable();
			        			htRecvHis.clear();
			        			htRecvHis.put(IDBConstants.PLANT, PLANT);
			        			htRecvHis.put("DIRTYPE", TransactionConstants.CREATE_GRN);
			        			htRecvHis.put(IDBConstants.REMARKS, PO_NUM);        					
			        			htRecvHis.put(IDBConstants.MOVHIS_ORDNUM, GRNO);
			        			
			        			flag=_RecvDetDAO.updateGRNtoBill(" set AMOUNT+="+priceval+",QTY+="+ITEM_QTY,htRecvDet,"",PLANT);
								flag=movHisDao.updateMovHis(" set QTY+="+ITEM_QTY,htRecvHis,"",PLANT);
							}
						
					}
					
					
				} else {
					throw new Exception(" Product Received already ");
				}

			} catch (Exception e) {
				MLogger.log(0, "" + e.getMessage());
				throw e;
			}
	}
			return xmlStr;
		}
	private String load_inBoundOrders(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {
		String str = "", aPlant = "", aOrdNo = "";
		try {

			PLANT = StrUtils.fString(request.getParameter("PLANT"));

			Hashtable ht = new Hashtable();
			ht.put("PLANT", PLANT);

			str = _POUtil.getOpenInBoundOrder(PLANT);

			if (str.equalsIgnoreCase("")) {
				str = XMLUtils.getXMLMessage(1, "No Inbound Orders Found");
			}

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			str = XMLUtils.getXMLMessage(1, e.getMessage());
		}

		return str;
	}


    private String load_inBoundOrdersByItem(HttpServletRequest request,
                    HttpServletResponse response) throws IOException, ServletException {
            String str = "", aPlant = "", aOrdNo = "";
            try {

                    PLANT = StrUtils.fString(request.getParameter("PLANT"));
                    String ITEM = StrUtils.fString(request.getParameter("ITEMNO"));

                    str = _POUtil.getOpenInBoundOrderByItem(PLANT,ITEM);

                    if (str.equalsIgnoreCase("")) {
                            str = XMLUtils.getXMLMessage(1, "No Inbound Orders Found for the Item : "+ITEM);
                    }

            } catch (Exception e) {
                    this.mLogger.exception(this.printLog, "", e);
                    str = XMLUtils.getXMLMessage(1, e.getMessage());
            }

            return str;
    }

	private String load_inBoundOrder_item_details(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {
		String str = "", aPlant = "", aOrdNo = "";
		try {

			PLANT = StrUtils.fString(request.getParameter("PLANT"));
			aOrdNo = StrUtils.fString(request.getParameter("ORDER_NUM"));

			Hashtable ht = new Hashtable();
			ht.put("PLANT", PLANT);
			ht.put("PONO", aOrdNo);

			str = _POUtil.getInBoundOrderItemDetails(PLANT, aOrdNo);

			if (str.equalsIgnoreCase("")) {
				str = XMLUtils.getXMLMessage(1, "Item details not found?");
			}

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			str = XMLUtils.getXMLMessage(1, e.getMessage());
		}
		return str;
	}
	private String load_receipt_item_details(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {
		String str = "", aPlant = "", aOrdNo = "",aItem;
		try {

			PLANT = StrUtils.fString(request.getParameter("PLANT"));
			aOrdNo = StrUtils.fString(request.getParameter("ORDER_NUM"));
			aItem = StrUtils.fString(request.getParameter("ITEM_NUM"));

			Hashtable ht = new Hashtable();
			ht.put("PLANT", PLANT);
			ht.put("PONO", aOrdNo);
			ht.put("ITEM", aItem);

			str = _POUtil.getInBoundOrderReceiptItem(PLANT, aOrdNo,aItem);

			if (str.equalsIgnoreCase("")) {
				str = XMLUtils.getXMLMessage(1, "Item details not found?");
			}

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			str = XMLUtils.getXMLMessage(1, e.getMessage());
		}
		return str;
	}
	private String load_random_inBoundOrder_item_details(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {
		String str = "", aPlant = "", aOrdNo = "";
		try {

			PLANT = StrUtils.fString(request.getParameter("PLANT"));
			aOrdNo = StrUtils.fString(request.getParameter("ORDER_NUM"));

			Hashtable ht = new Hashtable();
			ht.put("PLANT", PLANT);
			ht.put("PONO", aOrdNo);

			str = _POUtil.getRandomInBoundOrderItemDetails(PLANT, aOrdNo);

			if (str.equalsIgnoreCase("")) {
				str = XMLUtils.getXMLMessage(1, "Item details not found?");
			}

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			str = XMLUtils.getXMLMessage(1, e.getMessage());
		}
		return str;
	}
	private String load_inBoundOrder_item_detailsBynext(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {
		String str = "", aPlant = "", aOrdNo = "",start="",end="";
		try {

			PLANT = StrUtils.fString(request.getParameter("PLANT"));
			aOrdNo = StrUtils.fString(request.getParameter("ORDER_NUM"));
			start = StrUtils.fString(request.getParameter("START"));
			end = StrUtils.fString(request.getParameter("END"));
			
			Hashtable ht = new Hashtable();
			ht.put("PLANT", PLANT);
			ht.put("PONO", aOrdNo);
			ht.put("START", start);
			ht.put("END", end);

			str = _POUtil.getRandomInBoundOrderItemDetailsNext(PLANT, aOrdNo,start,end);

			if (str.equalsIgnoreCase("")) {
				str = XMLUtils.getXMLMessage(1, "Item details not found?");
			}

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			str = XMLUtils.getXMLMessage(1, e.getMessage());
		}
		return str;
	}
	private String load_inBoundOrder_item_ByItem(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {
		String str = "", aPlant = "", aOrdNo = "",start="",end="",itemNo;
		try {

			PLANT = StrUtils.fString(request.getParameter("PLANT"));
			aOrdNo = StrUtils.fString(request.getParameter("ORDER_NUM"));
			itemNo = StrUtils.fString(request.getParameter("ITEMNO"));
			start = StrUtils.fString(request.getParameter("START"));
			end = StrUtils.fString(request.getParameter("END"));
			
			Hashtable ht = new Hashtable();
			ht.put("PLANT", PLANT);
			ht.put("PONO", aOrdNo);
			ht.put("ITEMNO", itemNo);
			ht.put("START", start);
			ht.put("END", end);

			str = _POUtil.getRandomInBoundOrderItemNextItem(PLANT, aOrdNo,start,end,itemNo);

			if (str.equalsIgnoreCase("")) {
				str = XMLUtils.getXMLMessage(1, "Item details not found?");
			}

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			str = XMLUtils.getXMLMessage(1, e.getMessage());
		}
		return str;
	}
	private String load_Allitem_details(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {
		String str = "", aPlant = "", aOrdNo = "",start="",end="",itemNo;
		try {

			PLANT = StrUtils.fString(request.getParameter("PLANT"));
			itemNo = StrUtils.fString(request.getParameter("ITEMNO"));
			start = StrUtils.fString(request.getParameter("START"));
			end = StrUtils.fString(request.getParameter("END"));
			
			Hashtable ht = new Hashtable();
			ht.put("PLANT", PLANT);
			ht.put("ITEMNO", itemNo);
			ht.put("START", start);
			ht.put("END", end);

			str = _POUtil.getItemFromProductMst(PLANT,start,end,itemNo);

			if (str.equalsIgnoreCase("")) {
				str = XMLUtils.getXMLMessage(1, "Item details not found?");
			}

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			str = XMLUtils.getXMLMessage(1, e.getMessage());
		}
		return str;
	}
	private String load_All_Inv_item_details(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {
		String str = "", aPlant = "", aOrdNo = "",start="",end="",itemNo;
		try {

			PLANT = StrUtils.fString(request.getParameter("PLANT"));
			itemNo = StrUtils.fString(request.getParameter("ITEMNO"));
			start = StrUtils.fString(request.getParameter("START"));
			end = StrUtils.fString(request.getParameter("END"));
			
			Hashtable ht = new Hashtable();
			ht.put("PLANT", PLANT);
			ht.put("ITEMNO", itemNo);
			ht.put("START", start);
			ht.put("END", end);

			str = _POUtil.getItemFromInvMst(PLANT,start,end,itemNo);

			if (str.equalsIgnoreCase("")) {
				str = XMLUtils.getXMLMessage(1, "Item details not found?");
			}

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			str = XMLUtils.getXMLMessage(1, e.getMessage());
		}
		return str;
	}
	private String load_inBoundOrder_sup_details(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {
		String str = "", aPlant = "", aOrdNo = "";
		try {
			PLANT = StrUtils.fString(request.getParameter("PLANT"));
			aOrdNo = StrUtils.fString(request.getParameter("PO_NUM"));

			str = _POUtil.getOpenInBoundOrderSupDetails(PLANT, aOrdNo);

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			str = XMLUtils.getXMLMessage(1, e.getMessage());
		}
		return str;
	}

	private String load_locations(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException,
			Exception {
		String str = "";

		String PLANT = "", userID = "";
		try {
			PLANT = StrUtils.fString(request.getParameter("PLANT"));
			userID = StrUtils.fString(request.getParameter("LOGIN_USER"));
			LocUtil locUtil = new LocUtil();
			locUtil.setmLogger(mLogger);
			str = locUtil.getlocationsWithDesc(PLANT, userID);
			if (str.equalsIgnoreCase("")) {

				str = XMLUtils.getXMLMessage(1, "Details not found");
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		}
		return str;
	}

	private String process_InboundOrderGenerateBatch(
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, Exception {
		Map receiveMaterial_HM = null;
		String PLANT = "", BATCHDATE = "", LOGIN_USER = "";
		PLANT = StrUtils.fString(request.getParameter("PLANT"));
		LOGIN_USER = StrUtils.fString(request.getParameter("LOGIN_USER"));

		boolean flag = false;
		boolean updateFlag = false;
		boolean insertFlag = false;
		boolean resultflag = false;
		String sBatchSeq = "";
		String extCond = "";
		String rtnBatch = "";
		String sZero = "";

		try {

			TblControlDAO _TblControlDAO = new TblControlDAO();
			_TblControlDAO.setmLogger(mLogger);
			BATCHDATE = _TblControlDAO.getBatchDate();
			Hashtable ht = new Hashtable();

			String query = " isnull(NXTSEQ,'') as NXTSEQ";
			ht.put(IDBConstants.PLANT, PLANT);
			ht.put(IDBConstants.TBL_FUNCTION, IDBConstants.TBL_BATCH_CAPTION);
			ht.put(IDBConstants.TBL_PREFIX1, BATCHDATE.substring(0, 6));
			boolean exitFlag = false;
			exitFlag = _TblControlDAO.isExisit(ht, extCond, PLANT);

			if (exitFlag == false) {

				rtnBatch = BATCHDATE.substring(0, 6) + "-" + "0000"
						+ IDBConstants.TBL_FIRST_NEX_SEQ;
				Map htInsert = null;
				Hashtable htTblCntInsert = new Hashtable();

				htTblCntInsert.put(IDBConstants.PLANT, (String) PLANT);

				htTblCntInsert.put(IDBConstants.TBL_FUNCTION,
						(String) IDBConstants.TBL_BATCH_CAPTION);
				htTblCntInsert.put(IDBConstants.TBL_PREFIX1, (String) BATCHDATE
						.substring(0, 6));
				htTblCntInsert.put(IDBConstants.TBL_NEXT_SEQ,
						(String) IDBConstants.TBL_FIRST_NEX_SEQ);
				htTblCntInsert
						.put(IDBConstants.CREATED_BY, (String) LOGIN_USER);
				htTblCntInsert.put(IDBConstants.CREATED_AT,
						(String) DateUtils.getDateTime());
				insertFlag = _TblControlDAO.insertTblControl(htTblCntInsert,
						PLANT);

				MovHisDAO mdao = new MovHisDAO(PLANT);
				mdao.setmLogger(mLogger);
				Hashtable htm = new Hashtable();
				htm.put("PLANT", (String) PLANT);
				htm.put("DIRTYPE", TransactionConstants.GENERATE_BATCH);
				htm.put("RECID", "");
				htm.put("CRBY", (String) LOGIN_USER);
				htm.put("CRAT", (String) DateUtils.getDateTime());
				boolean inserted = mdao.insertIntoMovHis(htm);
				if (insertFlag) {
					resultflag = true;
				} else if (!insertFlag) {

					throw new Exception(
							"Generate BatchNumber Failed, Error In Inserting Table:"
									+ " " + PLANT + "_ " + "TBLCONTROL");
				} else if (inserted) {
					resultflag = true;
				} else if (!inserted) {

					throw new Exception(
							"Generate BatchNumber Failed,  Error In Inserting Table:"
									+ " " + PLANT + "_ " + "MOVHIS");
				}

			} else {

				Map m = _TblControlDAO.selectRow(query, ht, extCond);
				sBatchSeq = (String) m.get("NXTSEQ");
				if (sBatchSeq.length() == 1) {
					sZero = "0000";
				} else if (sBatchSeq.length() == 2) {
					sZero = "000";
				} else if (sBatchSeq.length() == 3) {
					sZero = "00";
				} else if (sBatchSeq.length() == 4) {
					sZero = "0";
				}

				int inxtSeq = Integer.parseInt(((String) sBatchSeq.trim()
						.toString())) + 1;

				String updatedSeq = Integer.toString(inxtSeq);
				rtnBatch = BATCHDATE.substring(0, 6) + "-" + sZero + updatedSeq;
				Map htUpdate = null;

				Hashtable htTblCntUpdate = new Hashtable();
				htTblCntUpdate.put(IDBConstants.PLANT, PLANT);
				htTblCntUpdate.put(IDBConstants.TBL_FUNCTION,
						IDBConstants.TBL_BATCH_CAPTION);
				htTblCntUpdate.put(IDBConstants.TBL_PREFIX1, BATCHDATE
						.substring(0, 6));
				StringBuffer updateQyery = new StringBuffer("set ");
				updateQyery.append(IDBConstants.TBL_NEXT_SEQ + " = '"
						+ (String) updatedSeq.toString() + "'");

				updateFlag = _TblControlDAO.update(updateQyery.toString(),
						htTblCntUpdate, extCond, PLANT);

				MovHisDAO mdao = new MovHisDAO(PLANT);
				mdao.setmLogger(mLogger);
				Hashtable htm = new Hashtable();
				htm.put("PLANT", (String) PLANT);
				htm.put("DIRTYPE", TransactionConstants.UPDATE_BATCH);
				htm.put("RECID", "");
				htm.put("CRBY", (String) LOGIN_USER);
				htm.put("CRAT", (String) DateUtils.getDateTime());
				boolean inserted = mdao.insertIntoMovHis(htm);

				if (updateFlag) {
					resultflag = true;
				} else if (!updateFlag) {

					throw new Exception(
							"Update BatchNumber Failed, Error In Updating Table:"
									+ " " + PLANT + "_ " + "TBLCONTROL");
				} else if (inserted) {
					resultflag = true;
				}

				else if (!inserted) {

					throw new Exception(
							"Update BatchNumber Failed,  Error In Inserting Table:"
									+ " " + PLANT + "_ " + "MOVHIS");
				}

			}

			if (resultflag == true) {
				xmlStr = XMLUtils.getXMLMessage(1, rtnBatch);

			} else {
				xmlStr = XMLUtils.getXMLMessage(0,
						"Error in generating batch : ");
			}

		} catch (Exception e) {
			throw e;
		}
		return xmlStr;
	}

	private String process_orderReverseConfrimByWMS(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException,
			Exception {
		Map receiveMaterial_HM = null;
		String PLANT = "", PO_NUM = "", CUST_NAME = "", ITEM_NUM = "", PO_LN_NUM = "", REMARKS = "";
		String ITEM_DESCRIPTION = "", LOGIN_USER = "";
		String ITEM_BATCH = "", QTY = "", ITEM_QTY = "", RECEIVEDQTY = "", REVERSEDQTY = "", REVERSEQTY = "", ITEM_EXPDATE = "", ITEM_LOC = "";

		HttpSession session = request.getSession(false);
		PLANT = StrUtils.fString((String) session.getAttribute("PLANT"));

		RecvDetDAO _RecvDetDAO = new RecvDetDAO();
		PoDetDAO _PoDetDAO = new PoDetDAO(PLANT);
		PoHdrDAO _PoHdrDAO = new PoHdrDAO(PLANT);
		InvMstDAO _InvMstDAO = new InvMstDAO(PLANT);

		_RecvDetDAO.setmLogger(mLogger);
		_PoDetDAO.setmLogger(mLogger);
		_PoHdrDAO.setmLogger(mLogger);
		_InvMstDAO.setmLogger(mLogger);

		boolean flag = false;
		boolean flagrecvdelete = false;
		boolean flagpodet = false;
		boolean flagpodet1 = false;
		boolean flaginvdet = false;
		
		UserTransaction ut = null;

		try {

			LOGIN_USER = StrUtils.fString(request.getParameter("LOGIN_USER"));
			PO_NUM = StrUtils.fString(request.getParameter("ORDERNO"));
			PO_LN_NUM = StrUtils.fString(request.getParameter("ORDERLNO"));
			CUST_NAME = StrUtils.fString(request.getParameter("CUSTNAME"));
			ITEM_NUM = StrUtils.fString(request.getParameter("ITEMNO"));
			ITEM_DESCRIPTION = StrUtils.fString(request
					.getParameter("ITEMDESC"));
			ITEM_LOC = StrUtils.fString(request.getParameter("LOC"));
			ITEM_BATCH = StrUtils.fString(request.getParameter("BATCH"));
			ITEM_EXPDATE = StrUtils.fString(request.getParameter("REF"));
			QTY = StrUtils.removeFormat(StrUtils.fString(request.getParameter("ORDERQTY")));
			RECEIVEDQTY = StrUtils.removeFormat(StrUtils.fString(request.getParameter("RECEIVEQTY")));
//			REVERSEDQTY = StrUtils.fString(request.getParameter("REVERSEDQTY"));
			REVERSEQTY = StrUtils.removeFormat(StrUtils.fString(request.getParameter("REVERSEQTY")));
			double revrsqty = Double.parseDouble(REVERSEQTY);
			revrsqty = StrUtils.RoundDB(revrsqty, IConstants.DECIMALPTS);
			REVERSEQTY = String.valueOf(revrsqty);
			
			ut = DbBean.getUserTranaction();
            ut.begin();
			
			
			Hashtable htRecvDet = new Hashtable();
			htRecvDet.clear();
			htRecvDet.put(IDBConstants.PLANT, PLANT);
			htRecvDet.put(IDBConstants.PODET_PONUM, PO_NUM);
			htRecvDet.put("LNNO", PO_LN_NUM);
			//htRecvDet.put(IDBConstants.CUSTOMER_NAME, strUtils.InsertQuotes(CUST_NAME));
			htRecvDet.put(IDBConstants.ITEM, ITEM_NUM);
			//htRecvDet.put(IDBConstants.ITEM_DESC,strUtils.InsertQuotes(ITEM_DESCRIPTION));
			htRecvDet.put(IDBConstants.LOC, ITEM_LOC);
			htRecvDet.put("BATCH", ITEM_BATCH);
			//htRecvDet.put("REMARK", ITEM_EXPDATE);
			//htRecvDet.put("ORDQTY", QTY);
			//htRecvDet.put("RECQTY", "-" + REVERSEQTY);
			//htRecvDet.put("REVERSEQTY", REVERSEQTY);
			//htRecvDet.put(IDBConstants.CREATED_AT, (String) DateUtils.getDateTime());
			//htRecvDet.put(IDBConstants.CREATED_BY, (String) LOGIN_USER);
		    //htRecvDet.put(IDBConstants.RECVDET_TRANTYPE, "IB");
			double receivedqty = Double.parseDouble(((String) RECEIVEDQTY.trim()));
			//double revQty = Double.parseDouble(((String) REVERSEQTY.trim().toString()));

					
//			boolean qtyFlag = false;
			if (receivedqty < revrsqty) {
//				qtyFlag = true;
				throw new Exception(
						"Error : Cannot Receive more than Order Qty!");
			}

			flagrecvdelete = _RecvDetDAO.deleteRECVDET(htRecvDet);

			if (!flagrecvdelete) {
				flag = false;
				throw new Exception(
						"Product Reversed  Failed, Error In Insert RECVDET :"
								+ " " + ITEM_NUM);
			} else {
				flag = true;
			}

			String extCont = "";
			String sLnstat = "O";
			Hashtable htPoDet = new Hashtable();
			htPoDet.put("PLANT", (String) PLANT);
			htPoDet.put(IDBConstants.PODET_PONUM, PO_NUM);
			htPoDet.put(IDBConstants.ITEM, ITEM_NUM);
			htPoDet.put(IDBConstants.PODET_POLNNO, PO_LN_NUM);
			
			String updatePoDet = "";

			updatePoDet = "set qtyrc= isNull(qtyrc,0) - " + REVERSEQTY
					+ " , LNSTAT='O' ";

			flagpodet = _PoDetDAO.update(updatePoDet, htPoDet, "");
			if (!flagpodet) {
				flag = false;
				throw new Exception(
						"Product Reversed  Failed, Error In update  PODET :"
								+ " " + ITEM_NUM);
			} else {
				flag = true;

			                                
                         boolean isExists = _PoDetDAO.isExisit(htPoDet,"  qtyrc =CAST('0' AS DECIMAL(18,3)) ");
                         if(isExists){
                             updatePoDet = "";

                             updatePoDet = "set LNSTAT='N' ";
                             flag = _PoDetDAO.update(updatePoDet, htPoDet, "");    
                         }
			}

			String updatePoHdr = "";
			Hashtable htCondipoHdr = new Hashtable();
			htCondipoHdr.put("PLANT", PLANT);
			htCondipoHdr.put(IDBConstants.PODET_PONUM, PO_NUM);
			flag = _PoDetDAO.isExisit(htCondipoHdr," isnull(LNSTAT,'') in ('O','C')");

			if (!flag)
				updatePoHdr = "set STATUS='N',ORDER_STATUS='Open' ";
			else
				updatePoHdr = "set STATUS='O',ORDER_STATUS='PARTIALLY PROCESSED' ";
			flagpodet = _PoHdrDAO.updatePO(updatePoHdr, htCondipoHdr, "");

			if (!flagpodet) {
				flag = false;
				throw new Exception(
						"Product Reversed  Failed, Error In update  PONO :"
								+ " " + PO_NUM);
			} else {
				flag = true;
			}
			try{

			Hashtable htInvMst = new Hashtable();
			htInvMst.put("PLANT", (String) PLANT);
			htInvMst.put(IDBConstants.ITEM, ITEM_NUM);
			htInvMst.put(IDBConstants.LOC, ITEM_LOC);
			htInvMst.put(IDBConstants.USERFLD4, ITEM_BATCH);
			String updateInvDet = "";
			updateInvDet = "set qty= isNull(qty,0) - " + REVERSEQTY + "";
			String extcond = " and qty >= " + REVERSEQTY;
			flaginvdet = _InvMstDAO.update(updateInvDet, htInvMst, extcond);
			if (!flaginvdet) {
				flag = false;
				throw new Exception(
						"Product Reversed  Failed, Not Enough Inventory available for product :"
								+ " " + ITEM_NUM +" in Loc "+ ITEM_LOC);
			} else {
				flag = true;
			}
			}
			catch(Exception e){
				
				throw new Exception(
						"Product Reversed  Failed, Not Enough inventory found in  InvMst for product:"
						+ ITEM_NUM+ "  with Batch:"+ITEM_BATCH+"  In Location:" + ITEM_LOC );
			}
			MovHisDAO mdao = new MovHisDAO(PLANT);
			mdao.setmLogger(mLogger);
			Hashtable htm = new Hashtable();
			htm.put(IDBConstants.PLANT, (String) PLANT);
			htm.put(IDBConstants.DIRTYPE, TransactionConstants.IB_REVERSE);
			htm.put("ORDNUM", PO_NUM);
			htm.put("RECID", "");
			htm.put(IDBConstants.ITEM, ITEM_NUM);
			htm.put(IDBConstants.LOC, ITEM_LOC);
			htm.put(IDBConstants.REMARKS, ITEM_EXPDATE);
			htm.put("Batno", ITEM_BATCH);
			htm.put(IDBConstants.QTY, REVERSEQTY);
			htm.put(IDBConstants.TRAN_DATE, DateUtils
					.getDateinyyyy_mm_dd(DateUtils.getDate()));
			htm.put(IDBConstants.CREATED_BY, (String) LOGIN_USER);
			htm.put(IDBConstants.CREATED_AT, (String) DateUtils
					.getDateTime());
			boolean inserted = mdao.insertIntoMovHis(htm);

			if (!inserted) {
				flag = false;
				throw new Exception(
						"Product Reversed  Failed, Error In update  MOVHIS :"
								+ " " + ITEM_NUM);
			} else {
				flag = true;
			}
			
			if (flag) {
			
			DbBean.CommitTran(ut);	
				flag = true;
			
		   } else {
			DbBean.RollbackTran(ut);
			flag = false;
			
				}

			if (flag) {
				request.getSession(false).setAttribute("RESULT",
						"Product : " + ITEM_NUM + "  reversed successfully!");

				response
						.sendRedirect("jsp/InboundsOrderReverse.jsp?action=View&PLANT="
								+ PLANT + "&PONO=" + PO_NUM);

			} else {
				request.getSession(false).setAttribute("RESULTERROR",
						"Error in reversing item : " + ITEM_NUM + " Order");
				response
						.sendRedirect("jsp/InboundOrderReverseConfirm.jsp?action=resulterror");
			}

		} catch (Exception e) {
			DbBean.RollbackTran(ut);
			MLogger.log(0, "" + e.getMessage());
			request.getSession(false).setAttribute("CATCHREVERSERROR",
					e.getMessage());
			response
					.sendRedirect("jsp/InboundOrderReverseConfirm.jsp?action=catchreverseerror&ORDERNO="
							+ PO_NUM
                                                        + "&ORDERLNO="
                                                        + PO_LN_NUM
							+ "&CUSTNAME="
							+ StrUtils.replaceCharacters2Send(CUST_NAME)
							+ "&ITEMNO="
							+ ITEM_NUM
							+ "&ITEMDESC="
							+ StrUtils.replaceCharacters2Send(ITEM_DESCRIPTION)
							+ "&LOC="
							+ ITEM_LOC
							+ "&BATCH="
							+ ITEM_BATCH
							+ "&REF="
							+ ITEM_EXPDATE
							+ "&ORDERQTY="
							+ QTY
							+ "&RECEIVEQTY="
							+ RECEIVEDQTY
							+ "&REVERSEDQTY="
							+ REVERSEDQTY);

			throw e;
		}
		return xmlStr;
	}
	

    /**
	 * Author Bruhan. 30 july 2012
	 * method : processOrderBulkReceivingByWMS(HttpServletRequest request,
			HttpServletResponse response) description : Process the information to recieve the checked ordered numbers.)
	 * 
	 * @param : HttpServletRequest request,
			HttpServletResponse response
	 * @return : String
	 * @throws Exception
	 * 
	 *  
	 * ************Modification History*********************************
	   Sep 25 2014 Bruhan, Description: To include Receive date
	 */
	
	private String processOrderBulkReceivingByWMS(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String chkdPoNo = "",collectionTime = "",orderLNo = "",receivingQty = "",ITEM_BATCH = "",item = "",ITEM_DESCRIPTION = "",ISNONSTKFLG="";
		String xmlStr = "" ,QTYOR="",PO_NUM = "",CUST_NAME = "",jobNum = "",UOMQTY="";
		String REMARKS = "",collectionDate = "",remark= "",ITEM_LOC= "",REF = "",EXPIREDATE="",RECVDATE = "",invoiceNum= "",strTranDate="",strRecvDate="",GRNO="",UNITMO="",
		createBill="",CUST_CODE="",priceval="",CLEARAGENT="",CLEARANCETYPE="",TRANSPORTID="",CONTACT_NAME="";
		double unitprice=0,totalprice=0,totalqty=0;
		String[]  checkedOrder;
		UserLocUtil uslocUtil = new UserLocUtil();
		//UserTransaction ut = null;
		Map receiveMaterial_HM = null;
		ItemMstDAO _ItemMstDAO = new ItemMstDAO();
		Map checkedPOS = new HashMap();
		Boolean allChecked = false,fullReceive = false;
		boolean ajax = "XMLHttpRequest".equals(request.getHeader("X-Requested-With"));
		JSONObject resultJson = new JSONObject();
		try {
			
			HttpSession session = request.getSession(false);
			PLANT = StrUtils.fString((String) session.getAttribute("PLANT"));
			String COMP_INDUSTRY = new PlantMstDAO().getCOMP_INDUSTRY(PLANT);
			PO_NUM = StrUtils.fString(request.getParameter("PONO"));
			String[] chkdPoNos  = request.getParameterValues("chkdPoNo");
//			ITEM_LOC = StrUtils.fString(request.getParameter("LOC_0"));
//			EXPIREDATE = StrUtils.fString(request.getParameter("EXPIREDATE_0"));
//			ITEM_LOC = StrUtils.fString(request.getParameter("LOC_"+orderLNo));
//			EXPIREDATE = StrUtils.fString(request.getParameter("EXPIREDATE_"+orderLNo));
			RECVDATE = StrUtils.fString(request.getParameter("RECVDATE_0"));
			REF = StrUtils.fString(request.getParameter("REF"));
			String LOGIN_USER = StrUtils.fString(request.getParameter("LOGIN_USER"));
			REMARKS = StrUtils.fString(request.getParameter("REMARK2"));
			CUST_NAME = StrUtils.fString(request.getParameter("CUST_NAME"));
			jobNum = StrUtils.fString(request.getParameter("JOB_NUM"));
			collectionDate = StrUtils.fString(request.getParameter("COLLECTION_DATE"));
			collectionTime = StrUtils.fString(request.getParameter("COLLECTION_TIME"));
			remark = StrUtils.fString(request.getParameter("reamark"));
			GRNO = StrUtils.fString(request.getParameter("GRNO"));//Ravindra
			createBill = StrUtils.fString(request.getParameter("createBill"));
			CUST_CODE = StrUtils.fString(request.getParameter("CUST_CODE"));
//			CLEARAGENT = StrUtils.fString(request.getParameter("CLEARAGENTID"));
			CLEARAGENT = StrUtils.fString(request.getParameter("clearingagent"));
			CLEARANCETYPE = StrUtils.fString(request.getParameter("typeofclearance"));
			TRANSPORTID = StrUtils.fString(request.getParameter("TRANSPORTID"));
			CONTACT_NAME = StrUtils.fString(request.getParameter("CONTACT_NAME"));
			//	invoiceNum = StrUtils.fString(request.getParameter("INVOICENUM"));
			if (RECVDATE.length()>5)
				strTranDate    = RECVDATE.substring(6)+"-"+ RECVDATE.substring(3,5)+"-"+RECVDATE.substring(0,2);
			    strRecvDate    = RECVDATE.substring(0,2)+"/"+ RECVDATE.substring(3,5)+"/"+RECVDATE.substring(6);
	
			if( request.getParameter("select")!=null){
				allChecked = true;
			}
			if(request.getParameter("fullReceive")!=null){
				fullReceive = true;
			}
//			boolean isvalidlocforUser = uslocUtil .isValidLocInLocmstForUser(PLANT, LOGIN_USER, ITEM_LOC);
//            _POUtil.setmLogger(mLogger);
//            if (!isvalidlocforUser) {
//                throw new Exception("Receiving Loc :" + ITEM_LOC + " is not User Assigned Location");
//            }
			
			DateUtils dateUtils = new DateUtils();
			String fromdates = dateUtils.parseDateddmmyyyy(strRecvDate);
			String time = DateUtils.getTimeHHmm();
			String orderdate = fromdates+time+"12";
			LocalDate date = LocalDate.parse(fromdates, DateTimeFormatter.BASIC_ISO_DATE);
			String formattedDate = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
			String tran = formattedDate;
			
            if (chkdPoNos != null)    {     
				for (int i = 0; i < chkdPoNos.length; i++)       { 
					orderLNo = chkdPoNos[i];
					receivingQty = StrUtils.fString(request.getParameter("receivingQty_"+orderLNo));
					ITEM_BATCH = StrUtils.fString(request.getParameter("BATCH_"+orderLNo));
					//if(COMP_INDUSTRY.equalsIgnoreCase("Centralised Kitchen")) {
					ITEM_LOC = StrUtils.fString(request.getParameter("LOC_"+orderLNo));
					EXPIREDATE = StrUtils.fString(request.getParameter("EXPIREDATE_"+orderLNo));
					/*}else {
						ITEM_LOC = StrUtils.fString(request.getParameter("LOC_0"));
						EXPIREDATE = StrUtils.fString(request.getParameter("EXPIREDATE_0"));
					}*/
					if (ITEM_BATCH.length() == 0) {
						ITEM_BATCH = "NOBATCH";
					}
					checkedPOS.put(orderLNo, receivingQty+":"+ITEM_BATCH);
				}
				session.setAttribute("checkedPOS", checkedPOS);
            }
            Boolean transactionHandler = true;
           // ut = DbBean.getUserTranaction();
           // ut.begin();
    		ArrayList PODetails = null;
    		ArrayList alPoHdr = null;

    		Hashtable htPoDet = new Hashtable();
    		String queryPoDet = "item,itemDesc,QTYOR,QTYRC,UNITMO,ISNULL((select ISNULL(QPUOM,1) from "+PLANT+"_UOM where UOM=UNITMO),1) UOMQTY,(UNITCOST*CURRENCYUSEQT) as UNITCOST";
    		// condition
    	process: 	
			if (chkdPoNos != null)    {     
				for (int i = 0; i < chkdPoNos.length; i++)       { 
					orderLNo = chkdPoNos[i];
					receivingQty = StrUtils.fString(request.getParameter("receivingQty_"+orderLNo));
					ITEM_BATCH = StrUtils.fString(request.getParameter("BATCH_"+orderLNo));
					//if(COMP_INDUSTRY.equalsIgnoreCase("Centralised Kitchen")) {
						ITEM_LOC = StrUtils.fString(request.getParameter("LOC_"+orderLNo));
						EXPIREDATE = StrUtils.fString(request.getParameter("EXPIREDATE_"+orderLNo));
						/*}else {
							ITEM_LOC = StrUtils.fString(request.getParameter("LOC_0"));
							EXPIREDATE = StrUtils.fString(request.getParameter("EXPIREDATE_0"));
						}*/
					if (ITEM_BATCH.length() == 0) {
						ITEM_BATCH = "NOBATCH";
					}
					boolean isvalidlocforUser = uslocUtil .isValidLocInLocmstForUser(PLANT, LOGIN_USER, ITEM_LOC);
		            _POUtil.setmLogger(mLogger);
		            if (!isvalidlocforUser) {
		                throw new Exception("Receiving Loc :" + ITEM_LOC + " is not User Assigned Location");
		            }
					htPoDet.put(IConstants.PODET_PONUM, PO_NUM);
		    		htPoDet.put(IConstants.PLANT, PLANT);
		    		htPoDet.put(IConstants.PODET_POLNNO, orderLNo);
					PODetails = _POUtil.getPoDetDetails(queryPoDet, htPoDet);
					if (PODetails.size() > 0) {	

							Map map1 = (Map) PODetails.get(0);
							item = (String) map1.get("item");
							ITEM_DESCRIPTION = (String) map1.get("itemDesc");
							QTYOR = (String) map1.get("QTYOR");
							UNITMO = (String) map1.get("UNITMO");
							ISNONSTKFLG =  new ItemMstDAO().getNonStockFlag(PLANT,item);
							UOMQTY= (String) map1.get("UOMQTY");
							unitprice = Double.parseDouble((String) map1.get("UNITCOST"));
							PlantMstDAO _PlantMstDAO  = new  PlantMstDAO();
							StrUtils strUtils     = new StrUtils();
							String numberOfDecimal = _PlantMstDAO.getNumberOfDecimal(PLANT);
							totalprice=totalprice+(unitprice* Float.parseFloat(receivingQty));
							priceval=String.valueOf(totalprice);
							double priceValue ="".equals(priceval) ? 0.0d :  Double.parseDouble(priceval);
							priceval = StrUtils.addZeroes(priceValue, numberOfDecimal);
							totalqty=totalqty+Float.parseFloat(receivingQty);
					}
					   receiveMaterial_HM = new HashMap();
                       receiveMaterial_HM.put(IConstants.PLANT, PLANT);
                       receiveMaterial_HM.put(IConstants.CUSTOMER_NAME, CUST_NAME);
                       receiveMaterial_HM.put(IConstants.ITEM, item);
                       receiveMaterial_HM.put(IConstants.ITEM_DESC, _ItemMstDAO.getItemDesc(PLANT ,item));
                       receiveMaterial_HM.put(IConstants.PODET_PONUM, PO_NUM);
                       receiveMaterial_HM.put(IConstants.PODET_POLNNO, orderLNo);
                       receiveMaterial_HM.put(IConstants.LOC, ITEM_LOC);
                       receiveMaterial_HM.put(IConstants.LOGIN_USER, LOGIN_USER);
                       receiveMaterial_HM.put(IConstants.CUSTOMER_CODE, _POUtil.getCustCode(PLANT, PO_NUM));
                       receiveMaterial_HM.put(IConstants.INV_BATCH, ITEM_BATCH);
                       receiveMaterial_HM.put(IConstants.BATCH, ITEM_BATCH);
                       receiveMaterial_HM.put(IConstants.QTY, QTYOR);
                       receiveMaterial_HM.put(IConstants.ORD_QTY, QTYOR);
                       receiveMaterial_HM.put(IConstants.INV_QTY, receivingQty);
                       receiveMaterial_HM.put(IConstants.RECV_QTY, receivingQty);
                       receiveMaterial_HM.put(IConstants.INV_EXP_DATE, "");
                       receiveMaterial_HM.put(IConstants.USERFLD1, REMARKS);
                      // receiveMaterial_HM.put(IConstants.USERFLD2, FAX);
                       receiveMaterial_HM.put(IConstants.CREATED_AT,  (String) DateUtils.getDateTime());
                       receiveMaterial_HM.put(IConstants.CREATED_BY, LOGIN_USER);
                       receiveMaterial_HM.put(IConstants.JOB_NUM, _POUtil.getJobNum( PLANT, PO_NUM));
                       receiveMaterial_HM.put(IConstants.REMARKS, REF);
                      receiveMaterial_HM.put(IConstants.EXPIREDATE, EXPIREDATE);
                      receiveMaterial_HM.put(IConstants.TRAN_DATE, strTranDate);
                      receiveMaterial_HM.put(IConstants.RECVDATE, strRecvDate);
                      receiveMaterial_HM.put(IConstants.GRNO, GRNO);//Ravindra
                      receiveMaterial_HM.put("NONSTKFLAG", ISNONSTKFLG);
                      receiveMaterial_HM.put("UOMQTY", UOMQTY);
                      receiveMaterial_HM.put(IDBConstants.UOM, UNITMO);
                       _POUtil.setmLogger(mLogger);
                       
       				if (isvalidlocforUser) {
       					transactionHandler = _POUtil
       							.processMultiReceiveMaterialByWMS(receiveMaterial_HM) && transactionHandler;
       					if(!transactionHandler) break process;
       				} else {
       					transactionHandler = transactionHandler && false;
       					throw new Exception("Receiving Loc :" + ITEM_LOC
       							+ " is not User Assigned Location");

       				}     	
       				
       				if (transactionHandler == true) {
       					Hashtable htCond = new Hashtable();
       					htCond.put(IConstants.PLANT, PLANT);
       					if(new IntegrationsDAO().getShopifyConfigCount(htCond,"")) {
       						String nonstkflag = new ItemMstDAO().getNonStockFlag(PLANT,item);						
    						if(nonstkflag.equalsIgnoreCase("N")) {
       						String availqty ="0";
       						ArrayList invQryList = null;
       						htCond = new Hashtable();
       						invQryList= new InvUtil().getInvListSummaryAvailableQtyforShopify(PLANT,item, new ItemMstDAO().getItemDesc(PLANT, item),htCond);
       						if(new PlantMstDAO().getisshopify(PLANT)) {
	       						if (invQryList.size() > 0) {					
	       							for (int iCnt =0; iCnt<invQryList.size(); iCnt++){
	       								String result="";
	                                    Map lineArr = (Map) invQryList.get(iCnt);
	                                    availqty = (String)lineArr.get("AVAILABLEQTY");
	                                    System.out.println(availqty);
	       							}
	       							double availableqty = Double.parseDouble(availqty);
	           						new ShopifyService().UpdateShopifyInventoryItem(PLANT, item, availableqty);
	       						}	
       						}
    						}
       					}
       				}
					
				}
					
			}	
			if (transactionHandler == true) {
				//GRNOTOBILL
				if(createBill.equalsIgnoreCase("Y"))
				{
					RecvDetDAO _RecvDetDAO = new RecvDetDAO();
					Hashtable htRecvDet = new Hashtable();
					htRecvDet.clear();
					htRecvDet.put(IConstants.PLANT, PLANT);
					htRecvDet.put(IConstants.VENDOR_CODE, CUST_CODE);
					htRecvDet.put(IConstants.GRNO, GRNO);                    
					htRecvDet.put(IConstants.PODET_PONUM, PO_NUM);
					htRecvDet.put(IConstants.STATUS, "NOT BILLED");
					htRecvDet.put(IConstants.AMOUNT, priceval);
					htRecvDet.put(IConstants.QTY, String.valueOf(totalqty));
//					htRecvDet.put("CRAT",DateUtils.getDateTime());
					htRecvDet.put("CRAT",orderdate);
					htRecvDet.put("CRBY",LOGIN_USER);
					htRecvDet.put("UPAT",DateUtils.getDateTime());
                    transactionHandler = _RecvDetDAO.insertGRNtoBill(htRecvDet);
                    
                    Hashtable htShipHdr = new Hashtable();
                    htShipHdr.put(IConstants.PLANT, PLANT);
                    htShipHdr.put("DIRTYPE", TransactionConstants.ORD_RECV);
                    htShipHdr.put(IConstants.ORDNUM, PO_NUM);
                    htShipHdr.put("CLEARING_AGENT_ID", CLEARAGENT);                    
                    htShipHdr.put("CLEARANCETYPE", CLEARANCETYPE);                    
                    if(TRANSPORTID.equalsIgnoreCase(""))
                    	TRANSPORTID="0";
                    htShipHdr.put("TRANSPORTID", TRANSPORTID);                    
                    htShipHdr.put("CONTACTNAME", CONTACT_NAME);                    
                    htShipHdr.put("TRACKINGNO", "");                    
                    htShipHdr.put("RECEIPTNO", GRNO);                    
                    htShipHdr.put("CRAT",DateUtils.getDateTime());
                    htShipHdr.put("CRBY",LOGIN_USER);
//                    htShipHdr.put("UPAT",DateUtils.getDateTime());
//                    htShipHdr.put("UPBY",LOGIN_USER);
                    transactionHandler = _RecvDetDAO.insertShippingHdr(htShipHdr);
                    
                    //insert MovHis
                    MovHisDAO movHisDao = new MovHisDAO();
            		movHisDao.setmLogger(mLogger);
        			Hashtable htRecvHis = new Hashtable();
        			htRecvHis.clear();
        			htRecvHis.put(IDBConstants.PLANT, PLANT);
        			htRecvHis.put("DIRTYPE", TransactionConstants.CREATE_GRN);
        			htRecvHis.put(IDBConstants.ITEM, "");
        			htRecvHis.put("MOVTID", "");
        			htRecvHis.put("RECID", "");
        			htRecvHis.put(IConstants.QTY, String.valueOf(totalqty));
        			htRecvHis.put(IDBConstants.CREATED_BY, LOGIN_USER);
        			htRecvHis.put(IDBConstants.REMARKS, PO_NUM);        					
//        			htRecvHis.put(IDBConstants.UOM, UNITMO);        					
        			htRecvHis.put(IDBConstants.TRAN_DATE, DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));
        			htRecvHis.put(IDBConstants.MOVHIS_ORDNUM, GRNO);
        			htRecvHis.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
        			transactionHandler = movHisDao.insertIntoMovHis(htRecvHis);

				}
					//DbBean.CommitTran(ut);
				new TblControlUtil().updateTblControlIESeqNo(PLANT, "GRN", "GN", GRNO);
				String message = "Products received successfully!";
					
					/*----------------- automatic invoice-----------------------*/
					String AutoConvStatus = new PlantMstDAO().getconvertRECEIPTBILL(PLANT);
					if(AutoConvStatus.equalsIgnoreCase("1")) {
						String invstatus = createAutoBill(request, response, receiveMaterial_HM, GRNO, PLANT, LOGIN_USER);
						if(invstatus.equalsIgnoreCase("ok")) {
							transactionHandler = true;
							message = "Products Received & Bill Created Successfully" ;
						}else {
							transactionHandler = false;
							message = "Products received successfully! Bill not created";
						}
					}					
					
					if (ajax) {
						//	Generate PDF and and send success response
						String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
						EmailMsgUtil emailMsgUtil = new EmailMsgUtil();
						Map<String, String> emailMsg = emailMsgUtil.getEmailMsgDetails(plant, IConstants.PURCHASE_ORDER_AR);
						String sendAttachment = emailMsg.get(IDBConstants.SEND_ATTACHMENT);
						request.setAttribute("chkdPoNo", new String[] {request.getParameter("PONO")});
						if ("both".equals(sendAttachment) || "bothwithgrno".equals(sendAttachment) || "po".equals(sendAttachment) || "powithgrno".equals(sendAttachment)) {
							if (sendAttachment.contains("withgrno")) {
								request.setAttribute("printwithgrno", "Y");
							}
							viewPOReport(request, response, "printPOWITHBATCH");
						}
						//	Print with GRNO option is not available for invoice
						if ("both".equals(sendAttachment) || "bothwithgrno".equals(sendAttachment) || "invoice".equals(sendAttachment)) {
							viewPOInvoiceReport(request, response, "printPOInvoiceWithBatch");
						}
						resultJson.put("MESSAGE", message);
						resultJson.put("ERROR_CODE", "100");
						response.setContentType("application/json");
						response.setCharacterEncoding("UTF-8");
						response.getWriter().write(resultJson.toString());
						response.getWriter().flush();
						response.getWriter().close();
					}else {
						request.getSession(false).setAttribute("RESULTINBRECEIVE",
								message);
						/*if(createBill.equalsIgnoreCase("Y")){
							response.sendRedirect("jsp/grntobillSummary.jsp?action=View&PONO="+PO_NUM+"&GRNO="+GRNO+"&VEND_NAME="+CUST_NAME+"&VENDNO="+CUST_CODE);
						}else {*/
							response.sendRedirect("../purchasetransaction/receiptsummary?action=View&PONO="+ PO_NUM);
						//}					
					}
					
				} else {
					String message = "Unable to process..!";
					if (ajax) {
						resultJson.put("MESSAGE", message);
						resultJson.put("ERROR_CODE", "98");
						response.setContentType("application/json");
						response.setCharacterEncoding("UTF-8");
						response.getWriter().write(resultJson.toString());
						response.getWriter().flush();
						response.getWriter().close();
					}else {
						//DbBean.RollbackTran(ut);
						throw new Exception(message);
					}
				}
			
		}
		catch(Exception e)
		{
			//DbBean.RollbackTran(ut);
			this.mLogger.exception(this.printLog, "", e);
			String message = ThrowableUtil.getMessage(e);			
			if (ajax) {
				resultJson.put("MESSAGE", message);
				resultJson.put("ERROR_CODE", "99");
				response.setContentType("application/json");
				response.setCharacterEncoding("UTF-8");
				response.getWriter().write(resultJson.toString());
				response.getWriter().flush();
				response.getWriter().close();
			}else {
				request.getSession(false).setAttribute("CATCHERROR", message);
				response
						.sendRedirect("../purchasetransaction/receiptsummary?action=catcerror&PONO="
								+ PO_NUM
								+ "&COLLECTION_DATE="
								+ collectionDate
								+ "&COLLECTION_TIME="
								+ collectionTime
								+ "&CUST_NAME="
								+ StrUtils.replaceCharacters2Send(CUST_NAME)
								+ "&REMARK="
								+ remark
								+ "&JOB_NUM="
								+ jobNum
								+ "&REMARK2="
								+ REMARKS
								+"&ITEM_LOC="
								+ITEM_LOC
								+ "&EXPIREDATE="
								+EXPIREDATE
								+ "&RECVDATE="
								+RECVDATE
								+"&REF="
								+REF
								+ "&allChecked="
								+allChecked
								+"&fullReceive="
								+fullReceive
								);
				throw e;
			}
		}
		return xmlStr;
	}
	
	public String createAutoBill(HttpServletRequest request,
			HttpServletResponse response,Map map,String grno, String plant, String username) {

		/* BillHdr*/
		boolean Isconvcost=false;
		String vendno = "", billNo = "", pono = "", billDate = "", dueDate = "", payTerms = "",
		itemRates = "", discount = "0", discountType = "", discountAccount = "", shippingCost = "", orderdiscount="",currencyid="",currencyuseqt="0",
		adjustment = "0", adjustmentLabel = "Adjustment", subTotal = "", totalAmount = "", billStatus = "Open", note = "",taxamount="",isshtax="0",
		shipRef = "", refNum = "",paction="",vendname="",purchaseloc = "",invsalesloc="",taxtreatment="",sREVERSECHARGE="0",sGOODSIMPORT="0",isgrn="0",
		orddiscounttype="",taxid="0",isdiscounttax="1",isorderdiscounttax="0",gst="0",projectid="",transportid="",empno="",empName="",shippingid="",shippingcust="",
		origin = "sales", deductInv = "0", billtype = "PURCHASE",curency="";
		String shipcontactname="",shipdesgination="",shipaddr1="",shipaddr2="",shipaddr3="",shipaddr4="",shipstate="",shipzip="",
				shipworkphone="",shipcountry="",shiphpno="",shipemail="",jdesc="";
		//	String ceqt="0",
		
		/*BillDet*/
		List item = new ArrayList(), accountName = new ArrayList(), qty = new ArrayList(),
				cost = new ArrayList(), detDiscount = new ArrayList(),detDiscountType = new ArrayList(), taxType = new ArrayList(),
				amount = new ArrayList(), polnno = new ArrayList(), landedCost = new ArrayList(), convcost= new ArrayList(),
				cost_aod = new ArrayList(), amount_aod = new ArrayList();
		//List index = new ArrayList();
		List loc = new ArrayList(), batch = new ArrayList(),uom = new ArrayList(),
				 ordQty = new ArrayList(),delLnno = new ArrayList(),delLnQty = new ArrayList(),
						 delLnLoc = new ArrayList(),delLnBatch = new ArrayList(),delLnUom = new ArrayList(),
						 delLnItem = new ArrayList();
		List<Hashtable<String,String>> billDetInfoList = null;
		List<Hashtable<String,String>> billAttachmentList = null;
		List<Hashtable<String,String>> billAttachmentInfoList = null;
		Hashtable<String,String> billDetInfo = null;
		Hashtable<String,String> billAttachment = null;
		UserTransaction ut = null;
		BillUtil billUtil = new BillUtil();
		RecvDetDAO recvDao = new RecvDetDAO();
		MovHisDAO movHisDao = new MovHisDAO();
		TblControlDAO _TblControlDAO = new TblControlDAO();
		boolean isAdded = false;
		String result="";
		int itemCount  = 0, accountNameCount  = 0, qtyCount  = 0, costCount  = 0, detDiscountCount  = 0,detDiscountTypeCount  = 0,
				taxTypeCount  = 0, amountCount  = 0, polnnoCount = 0, landedCostCount = 0,convcostCount=0, cost_aodCount=0,amount_aodCount=0;
		int locCount  = 0,batchCount  = 0,uomCount  = 0, ordQtyCount = 0,
				delLnnoCount  = 0,delLnQtyCount  = 0,delLnLocCount  = 0,delLnBatchCount  = 0,delLnUomCount= 0,
				delLnItemCount=0;
		String polnnoIn = "";
		try{
			
			pono= (String)map.get(IConstants.PODET_PONUM);
			vendno= (String)map.get(IConstants.CUSTOMER_CODE);
			billDate= (String)map.get(IConstants.RECVDATE);
			
			DateUtils dateUtils = new DateUtils();
			String fromdates = dateUtils.parseDateddmmyyyy(billDate);
			String time = DateUtils.getTimeHHmm();
			String orderdate = fromdates+time+"12";
			LocalDate date = LocalDate.parse(fromdates, DateTimeFormatter.BASIC_ISO_DATE);
			String formattedDate = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
			String tran = formattedDate;
			
			String numberOfDecimal =new PlantMstDAO().getNumberOfDecimal(plant);
			String ConvertedUnitCost="",ConvertedAmount="",ConvertedUnitCost_Aod="",ConvertedAmount_Aod="";
			Hashtable ht=new Hashtable();
		    ht.put("PONO", pono);
		    ht.put("PLANT", plant);
		    ht.put("GRNO", grno);
		    List listQry =new POUtil().getBillingDetailsByGRNO(ht);
		    PoHdr pohdr =new PoHdrDAO().getPoHdrByPono(plant, pono);
		    String actualShippingCost =new PoHdrDAO().getActualShippingCostForBill(plant, pono);
		    double shipcost = Double.valueOf(actualShippingCost) * pohdr.getCURRENCYUSEQT();
		    double oddiscount = pohdr.getORDERDISCOUNT();
		    if(!pohdr.getORDERDISCOUNTTYPE().equalsIgnoreCase("%")) {
		    	 String actualDiscountCost =new PoHdrDAO().getActualDiscoutForBill(plant, pono);
		    	 oddiscount = Double.valueOf(actualDiscountCost) * pohdr.getCURRENCYUSEQT();
		    }
		    
		    actualShippingCost = StrUtils.addZeroes(shipcost, numberOfDecimal);
		    
		    payTerms=pohdr.getPAYMENT_TERMS();
		    empno=pohdr.getEMPNO();
		    itemRates=String.valueOf(pohdr.getISTAXINCLUSIVE());
		    discountType=pohdr.getCURRENCYID();
		    dueDate=pohdr.getDELDATE();
		    //adjustment=String.valueOf(pohdr.getADJUSTMENT());
		    isshtax=String.valueOf(pohdr.getISSHIPPINGTAX());
		    taxtreatment=pohdr.getTAXTREATMENT();
		    purchaseloc=pohdr.getPURCHASE_LOCATION();
		    currencyid=pohdr.getCURRENCYID();
		    currencyuseqt=String.valueOf(pohdr.getCURRENCYUSEQT());
		    orddiscounttype=pohdr.getORDERDISCOUNTTYPE();
		    taxid=String.valueOf(pohdr.getTAXID());
		    isorderdiscounttax=String.valueOf(pohdr.getISDISCOUNTTAX());
		    orderdiscount=String.valueOf(oddiscount);
		    shippingCost=String.valueOf(actualShippingCost);
		    gst=String.valueOf(pohdr.getINBOUND_GST());
		    projectid=String.valueOf(pohdr.getPROJECTID());
		    transportid=String.valueOf(pohdr.getTRANSPORTID());
		    shipcontactname=pohdr.getSHIPCONTACTNAME();
		    shipdesgination=pohdr.getSHIPDESGINATION();
		    shipworkphone=pohdr.getSHIPWORKPHONE();
		    shiphpno=pohdr.getSHIPHPNO();
		    shipemail=pohdr.getSHIPEMAIL();
		    shipcountry=pohdr.getSHIPCOUNTRY();
		    shipaddr1=pohdr.getSHIPADDR1();
		    shipaddr2=pohdr.getSHIPADDR2();
		    shipaddr3=pohdr.getSHIPADDR3();
		    shipaddr4=pohdr.getSHIPADDR4();
		    shipstate=pohdr.getSHIPSTATE();
		    shipzip=pohdr.getSHIPZIP();
		    shippingid=pohdr.getSHIPPINGID();
		    shippingcust=pohdr.getSHIPPINGCUSTOMER();
		    refNum = pohdr.getJobNum();
		    FinCountryTaxType fintaxtype= new FinCountryTaxType();
		    String ptaxtype="";
		    String ptaxiszero="1";
		    String ptaxisshow ="0";
		    if(pohdr.getTAXID() != 0) {
            	fintaxtype =new FinCountryTaxTypeDAO().getCountryTaxTypesByid(pohdr.getTAXID());
            	ptaxtype=String.valueOf(fintaxtype.getTAXTYPE());
            	ptaxiszero=String.valueOf(fintaxtype.getISZERO());
            	ptaxisshow=String.valueOf(fintaxtype.getSHOWTAX());
            }
		    
		    double dsubTotal =0;
		    double dtotalAmount =0;
		    double dtaxTotal =0;
		    if (listQry.size() > 0) {
		    	for(int i =0; i<listQry.size(); i++) {   
		    		Map m=(Map)listQry.get(i);
		    		
		    		PoDet podet =new PoDetDAO().getPoDetByPonoItem(plant, (String)m.get("PONO"), (String)m.get("ITEM"));
					
		    		double dCost = Double.parseDouble((String)m.get("UNITCOST"));
					ConvertedUnitCost = StrUtils.addZeroes(dCost, numberOfDecimal);
					
					double dUnitCost_Aod = Double.parseDouble((String)m.get("UNITCOST_AOD"));
					ConvertedUnitCost_Aod = StrUtils.addZeroes(dUnitCost_Aod, numberOfDecimal);
					
					double dQty = Double.parseDouble((String)m.get("RECQTY"));
					double dAmount = dCost * dQty;
					ConvertedAmount = StrUtils.addZeroes(dAmount, numberOfDecimal);
					
					double dConvertedAmount_Aod = dUnitCost_Aod * dQty;
					ConvertedAmount_Aod = StrUtils.addZeroes(dConvertedAmount_Aod, numberOfDecimal);
					
					double totalamount = 0;
					double tdiscamount = podet.getDISCOUNT();    
					if(podet.getDISCOUNT_TYPE().equalsIgnoreCase("%")){
						totalamount = dAmount - (dAmount/100)*(podet.getDISCOUNT());
						//tamount = StrUtils.addZeroes(totalamount, numberOfDecimal);
					}else{
						totalamount = dAmount - ((podet.getDISCOUNT()/Double.parseDouble(String.valueOf(podet.getQTYOR())))*(Double.parseDouble((String)m.get("RECQTY"))));
						//tamount = StrUtils.addZeroes(totalamount, numberOfDecimal);
						tdiscamount = ((podet.getDISCOUNT()/Double.parseDouble(String.valueOf(podet.getQTYOR())))*(Double.parseDouble((String)m.get("RECQTY"))));
						//discamount = StrUtils.addZeroes(tdiscamount, numberOfDecimal);
					}
					dsubTotal=dsubTotal+totalamount;
					
					item.add(itemCount, StrUtils.fString((String)m.get("ITEM")));
					itemCount++;
					
					accountName.add(accountNameCount,StrUtils.fString(podet.getACCOUNT_NAME()));
					accountNameCount++;
					
					qty.add(qtyCount, StrUtils.fString((String)m.get("RECQTY")));
					qtyCount++;
					
					cost.add(costCount, StrUtils.fString((String)m.get("UNITCOST")));
					costCount++;
					
					detDiscount.add(detDiscountCount, StrUtils.fString(String.valueOf(tdiscamount)));
					detDiscountCount++;
					
					detDiscountType.add(detDiscountTypeCount, StrUtils.fString(podet.getDISCOUNT_TYPE()));
					detDiscountTypeCount++;
					
					taxType.add(taxTypeCount, StrUtils.fString(podet.getTAX_TYPE()));
					taxTypeCount++;
					
					amount.add(amountCount, StrUtils.fString(String.valueOf(totalamount)));
					amountCount++;
					
					polnno.add(polnnoCount, StrUtils.fString((String)m.get("LNNO")));
					polnnoCount++;
					
					landedCost.add(landedCostCount, StrUtils.fString("0.0"));
					landedCostCount++;
					
					if(Float.parseFloat(StrUtils.fString((String)m.get("UNITCOST")))>0) 
					{
					Isconvcost=true;
					}
					convcost.add(convcostCount, StrUtils.fString((String)m.get("UNITCOST")));
					convcostCount++;
					
					amount_aod.add(amount_aodCount, StrUtils.fString(ConvertedAmount_Aod));
					amount_aodCount++;
		    	}
		    }
		    
		    if(orddiscounttype.toString().equalsIgnoreCase("%"))
		    	orderdiscount = String.valueOf(( dsubTotal* (oddiscount /100)));
		    //else
		    	//orderdiscount = String.valueOf(((oddiscount) / (Double.parseDouble(currencyuseqt))));
		    
		    if(!itemRates.equalsIgnoreCase("0"))
		    {
		    	if(ptaxiszero.equalsIgnoreCase("0") && ptaxisshow .equalsIgnoreCase("1")){					
					double taxsubtotal =(100*dsubTotal) / (100+pohdr.getINBOUND_GST());
					dtotalAmount = taxsubtotal;
				    dsubTotal = taxsubtotal;
				}
		    }
		    
		    if(itemRates.equalsIgnoreCase("0")){
				if(ptaxiszero.equalsIgnoreCase("0") && ptaxisshow.equalsIgnoreCase("1")){
					dtaxTotal = dtaxTotal + ((dsubTotal/100)*pohdr.getINBOUND_GST());
					
					if(isshtax.equalsIgnoreCase("1")){
						dtaxTotal = (dtaxTotal) + ((Double.parseDouble(shippingCost)/100)*pohdr.getINBOUND_GST());
					}
					
					if(isorderdiscounttax.equalsIgnoreCase("1")){
						dtaxTotal = (dtaxTotal) - ((Double.parseDouble(orderdiscount)/100)*pohdr.getINBOUND_GST());
					}
				}
		 }else{
				if(ptaxiszero.equalsIgnoreCase("0") && ptaxisshow.equalsIgnoreCase("1")){
					
					dtaxTotal = (dtaxTotal) + ((dsubTotal/100)*pohdr.getINBOUND_GST());
					
					if(isshtax.equalsIgnoreCase("1")){
						dtaxTotal = (dtaxTotal) + ((Double.parseDouble(shippingCost)/100)*pohdr.getINBOUND_GST());
					}
					
					if(isorderdiscounttax.equalsIgnoreCase("1")){
						dtaxTotal = (dtaxTotal) - ((Double.parseDouble(orderdiscount)/100)*pohdr.getINBOUND_GST());
					}
					
				}
		}
		    
		    if(ptaxisshow.equalsIgnoreCase("0"))
		    	dtaxTotal=0;
		    
		    double pretotal = ((dsubTotal) - Double.parseDouble(orderdiscount) + (dtaxTotal)	 + Double.parseDouble(shippingCost)); 
		    
		    //if(pretotal >= Double.parseDouble(adjustment)){
				 
			 //}else{
				 //adjustment = "0";				 
			 //}
			 
		    dtotalAmount = ((dsubTotal)- Double.parseDouble(orderdiscount) + (dtaxTotal)
					 + Double.parseDouble(shippingCost) + Double.parseDouble(adjustment));
		    adjustment= String.valueOf(Double.parseDouble(adjustment)/ (Double.parseDouble(currencyuseqt)));
		    shippingCost= String.valueOf(Double.parseDouble(shippingCost)/ (Double.parseDouble(currencyuseqt)));
		    orderdiscount = StrUtils.addZeroes((Double.parseDouble(orderdiscount) / (Double.parseDouble(currencyuseqt))), numberOfDecimal);
		    subTotal = String.valueOf(dsubTotal / (Double.parseDouble(currencyuseqt)));
		    totalAmount = StrUtils.addZeroes((dtotalAmount / (Double.parseDouble(currencyuseqt))), numberOfDecimal);
		    taxamount =String.valueOf(dtaxTotal / (Double.parseDouble(currencyuseqt)));
			 
			billNo= _TblControlDAO.getNextOrder(plant,username,"BILL");
			//////////////////////
			billDetInfoList = new ArrayList<Hashtable<String,String>>();
			Hashtable billHdr =new Hashtable(); 
			billHdr.put("PLANT", plant);
			billHdr.put("VENDNO", vendno);
			billHdr.put("BILL", billNo);
			billHdr.put("PONO", pono);
			billHdr.put("GRNO", grno);
			billHdr.put("BILL_DATE", billDate);
			billHdr.put("DUE_DATE", dueDate);
			billHdr.put("PAYMENT_TERMS", payTerms);
			billHdr.put("EMPNO", empno);
			billHdr.put("ITEM_RATES", itemRates);
			billHdr.put("DISCOUNT_TYPE", discountType);
			billHdr.put("DISCOUNT_ACCOUNT", discountAccount);
			billHdr.put("ADJUSTMENT_LABEL", adjustmentLabel);
			billHdr.put("ADJUSTMENT", adjustment);
			billHdr.put("SUB_TOTAL", subTotal);
			billHdr.put("TOTAL_AMOUNT", totalAmount);
			billHdr.put("BILL_STATUS", billStatus);
			billHdr.put("SHIPMENT_CODE", shipRef);
			billHdr.put("REFERENCE_NUMBER", refNum);
			billHdr.put("NOTE", note);	
			billHdr.put("CRAT",DateUtils.getDateTime());
			billHdr.put("CRBY",username);
			billHdr.put("UPAT",DateUtils.getDateTime());
			billHdr.put("CREDITNOTESSTATUS","0");
			billHdr.put("TAXAMOUNT", taxamount);
			billHdr.put("ISSHIPPINGTAXABLE", isshtax);
			if(purchaseloc.equalsIgnoreCase(""))
				billHdr.put("PURCHASE_LOCATION", invsalesloc);
			else
				billHdr.put("PURCHASE_LOCATION", purchaseloc);

			
			billHdr.put("TAXTREATMENT",taxtreatment);
			billHdr.put(IDBConstants.REVERSECHARGE, sREVERSECHARGE);
			billHdr.put(IDBConstants.GOODSIMPORT, sGOODSIMPORT);
			billHdr.put(IDBConstants.CURRENCYID, currencyid);
			billHdr.put("CURRENCYUSEQT",currencyuseqt);
			billHdr.put("ORDERDISCOUNTTYPE",orddiscounttype);
			billHdr.put("TAXID",taxid);
			billHdr.put("ISDISCOUNTTAX",isdiscounttax);
			billHdr.put("ISORDERDISCOUNTTAX",isorderdiscounttax);
			billHdr.put("DISCOUNT", discount);
			billHdr.put("SHIPPINGCOST", shippingCost);
			billHdr.put("ORDER_DISCOUNT", orderdiscount);
			//billHdr.put("ORDER_DISCOUNT", String.valueOf(oddiscount));			
			billHdr.put("INBOUND_GST", gst);
			billHdr.put("PROJECTID", projectid);
			billHdr.put("TRANSPORTID", transportid);
			billHdr.put("SHIPCONTACTNAME",shipcontactname);
			billHdr.put("SHIPDESGINATION",shipdesgination);
			billHdr.put("SHIPWORKPHONE",shipworkphone);
			billHdr.put("SHIPHPNO",shiphpno);
			billHdr.put("SHIPEMAIL",shipemail);
			billHdr.put("SHIPCOUNTRY",shipcountry);
			billHdr.put("SHIPADDR1",shipaddr1);
			billHdr.put("SHIPADDR2",shipaddr2);
			billHdr.put("SHIPADDR3",shipaddr3);
			billHdr.put("SHIPADDR4",shipaddr4);
			billHdr.put("SHIPSTATE",shipstate);
			billHdr.put("SHIPZIP",shipzip);
			billHdr.put("SHIPPINGID", shippingid);
			billHdr.put("SHIPPINGCUSTOMER", shippingcust);
			billHdr.put("ORIGIN",origin);
			billHdr.put("DEDUCT_INV",deductInv);
			billHdr.put("BILL_TYPE",billtype);
			
			ExpensesDAO expenseDao=new ExpensesDAO();
			CoaDAO coaDao=new CoaDAO();
			BillDAO itemCogsDao=new BillDAO();
			List<CostofgoodsLanded> landedCostLst=new ArrayList<>();
//			List<ItemCogs> lstCogs=new ArrayList<>();
			ItemMstDAO itemmstDao=new ItemMstDAO();
			Costofgoods costofGoods=new CostofgoodsImpl();
			double expenseAmt=0.0;
			/*Get Transaction object*/
			ut = DbBean.getUserTranaction();				
			/*Begin Transaction*/
			ut.begin();
			int billHdrId = billUtil.addBillHdr(billHdr, plant);	
			//int billHdrId = 0;	
			if(billHdrId > 0) {
				if(shipRef!=null && !shipRef.isEmpty()) {
				List<String> expensesId=expenseDao.getExpesesHDR(pono, plant,shipRef);
				List<CostofgoodsLanded> expensesAccount=expenseDao.getExpesesDET(expensesId, plant);
				List<CostofgoodsLanded> listofLanded=coaDao.getCOAByName(expensesAccount, plant);
				for(int i=0;i<expensesAccount.size();i++) {
					expenseAmt+=expensesAccount.get(i).getAmount();
				}
			
				for(int i=0;i<expensesAccount.size();i++) {
					for(int j=0;j<listofLanded.size();j++) {
						if(expensesAccount.get(i).getAccount_name().equalsIgnoreCase(listofLanded.get(j).getAccount_name())) {
							expensesAccount.get(i).setLandedcostcal(listofLanded.get(j).getLandedcostcal());
						}
					}
				}
				boolean isBasedOnCost=false,isBasedOnWeight=false;
				for(int i=0;i<expensesAccount.size();i++) {
					if(expensesAccount.get(i).getLandedcostcal().equals("1")) {
						isBasedOnCost=true;
					}if(expensesAccount.get(i).getLandedcostcal().equals("2")) {
						isBasedOnWeight=true;
					}
				}
				int itemCnt=0;
				if(amount_aod.size()==0) {
					amount_aod = amount;
				}
				Double avg_rate=0.0;
				Double weightedQty=costofGoods.calculateWeightedQty(item,qty,plant);
				Double subtotal=costofGoods.getProductSubtotalAmount(amount);
				for(int i=0;i<item.size();i++) {
					itemCnt=item.size();
					CostofgoodsLanded costof=new CostofgoodsLanded();
					costof.setProd_id((String)item.get(i));
					CostofgoodsLanded weight=itemmstDao.getItemMSTDetails((String)item.get(i), plant);
					costof.setWeight(weight.getNet_weight());
					costof.setQuantity(Double.parseDouble((String)qty.get(i)));
					costof.setWeight_qty(weightedQty);
					
					CostofgoodsLanded reqObj=new CostofgoodsLanded();
					reqObj.setOrderdiscount(Double.parseDouble(orderdiscount));
					reqObj.setDiscount(Double.parseDouble(discount));
					reqObj.setDiscountType(discountType);
					reqObj.setShippingCharge(Double.parseDouble(shippingCost));
					reqObj.setLstamount(amount);
					reqObj.setSub_total(subtotal);
					reqObj.setAmount(Double.parseDouble((String)amount.get(i)));
				
					Double amt=costofGoods.calculateIndividualAmount(reqObj,itemCnt,i);
					costof.setAmount(amt);
					Double totalCost=costofGoods.calculateTotalCost(item,reqObj);
					costof.setTotal_cost(totalCost);
					
					if("%".equals(reqObj.getDiscountType())) {
					reqObj.setAmount(amt);
					reqObj.setTotal_cost(totalCost);
					reqObj.setOrderdiscount(Double.parseDouble(orderdiscount));
					amt=costofGoods.calculateIndividualAmountForOrderDiscount(reqObj,itemCnt,i);
					costof.setAmount(amt);
					totalCost=costofGoods.calculateTotalCostForOrderDiscount(item,reqObj);
					costof.setTotal_cost(totalCost);
					}
					
					costof.setUnit_cost(Double.parseDouble((String)cost.get(i)));
					costof.setExpenses_amount(expenseAmt);
					
					if(isBasedOnWeight && !isBasedOnCost) {
						// implement calculation based on weight
						avg_rate=costofGoods.calculateLandedWeightBased(costof);
					}else if(!isBasedOnWeight && isBasedOnCost){
						// implement calculation based on cost
						costof.setAmount((amt/costof.getQuantity()));
						avg_rate=costofGoods.calculateLandedCostBased(costof);
					}else if(isBasedOnWeight && isBasedOnCost) {
						// implement calculation based on both
						Double costedExpensesAmt=0.0,weight_allocation=0.0,weightedExpensesAmt=0.0,cost_allocation=0.0,landed_cost=0.0;
						for(int j=0;j<expensesAccount.size();j++) {
							if(expensesAccount.get(j).getLandedcostcal().equals("1")) {
								costedExpensesAmt+=expensesAccount.get(j).getAmount();
							}if(expensesAccount.get(j).getLandedcostcal().equals("2")) {
								weightedExpensesAmt+=expensesAccount.get(j).getAmount();
							}
						}
						costof.setExpenses_amount(weightedExpensesAmt);
						weight_allocation=costofGoods.calculateWeightAllocaiton(costof);
						costedExpensesAmt+=Double.parseDouble(shippingCost);
						costof.setExpenses_amount(costedExpensesAmt);
						cost_allocation=costofGoods.calculateCostAllocaiton(costof);
						landed_cost=weight_allocation+cost_allocation+costof.getAmount();
						avg_rate=(landed_cost/costof.getQuantity());
					}else {
						// implement calculation based on both not applicable
						Double costedExpensesAmt=0.0,cost_allocation=0.0,landed_cost=0.0;
						costedExpensesAmt=costof.getExpenses_amount()+Double.parseDouble(shippingCost);
						costof.setExpenses_amount(costedExpensesAmt);
						cost_allocation=costofGoods.calculateCostAllocaiton(costof);
						landed_cost=cost_allocation+costof.getAmount();
						avg_rate=(landed_cost/costof.getQuantity());
					}
					  costof.setAvg_rate(avg_rate);
					  costof.setBillhdrid(String.valueOf(billHdrId));
					  landedCostLst.add(costof);
					 
					  int cogsCnt=itemCogsDao.addItemCogs(costofGoods.entryProductDetails((String)qty.get(i), (String)item.get(i), plant, avg_rate, dueDate),plant);
					  System.out.println("Insert ItemCogs Status :"+ cogsCnt);
							

					}
				}

	
				
				for(int i =0 ; i < item.size() ; i++){
					int lnno = i+1;
					String convDiscount=""; 
					String convCost = String.valueOf((Float.parseFloat((String) cost.get(i)) / Float.parseFloat(currencyuseqt)));
					if(Isconvcost)
						convCost = String.valueOf((Float.parseFloat((String) convcost.get(i)) / Float.parseFloat(currencyuseqt)));
					if(!detDiscountType.get(i).toString().contains("%"))
					{
						convDiscount = String.valueOf((Float.parseFloat((String) detDiscount.get(i)) / Float.parseFloat(currencyuseqt)));
					}
					else
						convDiscount = (String) detDiscount.get(i);
					String convAmount = String.valueOf((Float.parseFloat((String) amount.get(i)) / Float.parseFloat(currencyuseqt)));
					double clancost = (Float.parseFloat((String) landedCost.get(i)) / Float.parseFloat(currencyuseqt));
					if(Double.isNaN(clancost)) {
						clancost = 0.0;
					}
					String convlandedCost = String.valueOf(clancost);

					billDetInfo = new Hashtable<String, String>();
					billDetInfo.put("PLANT", plant);
//					billDetInfo.put("LNNO", Integer.toString(lnno));
					billDetInfo.put("LNNO", (String) polnno.get(i));
					billDetInfo.put("BILLHDRID", Integer.toString(billHdrId));
					billDetInfo.put("ITEM", (String) item.get(i));
					billDetInfo.put("ACCOUNT_NAME", (String) accountName.get(i));
					billDetInfo.put("QTY", (String) qty.get(i));
					billDetInfo.put("COST", convCost);
					billDetInfo.put("DISCOUNT", convDiscount);
					billDetInfo.put("DISCOUNT_TYPE", (String) detDiscountType.get(i));
					billDetInfo.put("Tax_Type", (String) taxType.get(i));
					billDetInfo.put("Amount", convAmount);
					billDetInfo.put("LANDED_COST", convlandedCost);
					billDetInfo.put(IDBConstants.CURRENCYUSEQT, currencyuseqt);
					if(loc.size() > 0) {
						billDetInfo.put("LOC", (String) loc.get(i));
						billDetInfo.put("UOM", (String) uom.get(i));
						billDetInfo.put("BATCH", (String) batch.get(i));
					}
					billDetInfo.put("CRAT",DateUtils.getDateTime());
					billDetInfo.put("CRBY",username);
					billDetInfo.put("UPAT",DateUtils.getDateTime());
					billDetInfoList.add(billDetInfo);
					if(polnno.size() > 0) {
						if(!grno.equalsIgnoreCase("")) {
						if(i < (item.size()-1)) {
							polnnoIn += (String) polnno.get(i)+ ",";
						}else {
							polnnoIn += (String) polnno.get(i);
						}		
						}
					}
				}			
				isAdded = billUtil.addMultipleBillDet(billDetInfoList, plant);
				/*
				 * int attchSize = billAttachmentList.size(); for(int i =0 ; i < attchSize ;
				 * i++){ billAttachment = new Hashtable<String, String>(); billAttachment =
				 * billAttachmentList.get(i); billAttachment.put("BILLHDRID",
				 * Integer.toString(billHdrId)); billAttachmentInfoList.add(billAttachment); }
				 */
				if(!landedCostLst.isEmpty() && landedCostLst.size()>0) {
					BillDAO billDao=new BillDAO();
					for(int i=0;i<landedCostLst.size();i++) {
							int billUpt=billDao.updateLandedCost(landedCostLst.get(i), plant);
							System.out.println("Avg Rate Updated in Bill System " +landedCostLst.get(i).getProd_id()+" : "+billUpt);
					}
				}
				if(isAdded) {
					//if(billAttachmentInfoList.size() > 0)
						//isAdded = billUtil.addBillAttachments(billAttachmentInfoList, plant);

					
					curency = StrUtils.fString((String) request.getSession().getAttribute("BASE_CURRENCY")).trim();
					System.out.println("billStatus"+billStatus);
					if(!billStatus.equalsIgnoreCase("Draft"))
					{
						//Journal Entry
						JournalHeader journalHead=new JournalHeader();
						journalHead.setPLANT(plant);
						journalHead.setJOURNAL_DATE(billDate);
						journalHead.setJOURNAL_STATUS("PUBLISHED");
						journalHead.setJOURNAL_TYPE("Cash");
						journalHead.setCURRENCYID(curency);
						journalHead.setTRANSACTION_TYPE("BILL");
						journalHead.setTRANSACTION_ID(billNo);
						journalHead.setSUB_TOTAL(Double.parseDouble(subTotal));
						//journalHead.setTOTAL_AMOUNT(Double.parseDouble(totalAmount));
						journalHead.setCRAT(DateUtils.getDateTime());
						journalHead.setCRBY(username);

						
						List<JournalDetail> journalDetails=new ArrayList<>();
						List<JournalDetail> journalReversalList=new ArrayList<>();
						CoaDAO coaDAO=new CoaDAO();
						VendMstDAO vendorDAO=new VendMstDAO();
						ItemMstDAO itemMstDAO=new ItemMstDAO();
						Double totalItemNetWeight=0.00;
						Double totalline=0.00;
						for(Map billDet:billDetInfoList)
						{	
							Double quantity=Double.parseDouble(billDet.get("QTY").toString());
							totalline++;
							String netWeight=itemMstDAO.getItemNetWeight(plant, billDet.get("ITEM").toString());
//							TODO : Ravindra - Get verified
							if (netWeight == null || "".equals(netWeight)) {
								netWeight = "0";
							}
							Double Netweight=quantity*Double.parseDouble(netWeight);
							totalItemNetWeight+=Netweight;
							System.out.println("TotalNetWeight:"+totalItemNetWeight);
							
							
							JournalDetail journalDetail=new JournalDetail();
							journalDetail.setPLANT(plant);
							JSONObject coaJson=coaDAO.getCOAByName(plant, (String) billDet.get("ACCOUNT_NAME"));
							System.out.println("Json"+coaJson.toString());
							journalDetail.setACCOUNT_ID(Integer.parseInt(coaJson.getString("id")));
							journalDetail.setACCOUNT_NAME((String) billDet.get("ACCOUNT_NAME"));
							if(!orddiscounttype.toString().equalsIgnoreCase("%")) {
								journalDetail.setDEBITS((Double.parseDouble(billDet.get("Amount").toString()) - oddiscount/billDetInfoList.size()));
							}else {
								Double jodamt = ((Double.parseDouble(billDet.get("Amount").toString())/100)*oddiscount);
								journalDetail.setDEBITS(Double.parseDouble(billDet.get("Amount").toString()) -jodamt);
							}
							
							boolean isLoop=false;
							if(journalDetails.size()>0)
							{
								int i=0;
								for(JournalDetail journal:journalDetails) {
									int accountId=journal.getACCOUNT_ID();
									if(accountId==journalDetail.getACCOUNT_ID()) {
										isLoop=true;
										Double sumDetit=journal.getDEBITS()+journalDetail.getDEBITS();
										journalDetail.setDEBITS(sumDetit);
										journalDetails.set(i, journalDetail);
										break;
									}
									i++;

									
								}
								if(isLoop==false) {
									journalDetails.add(journalDetail);
								}
							}
							else
							{
								journalDetails.add(journalDetail);
							}

							
							
						}

						
						JournalDetail journalDetail_1=new JournalDetail();
						journalDetail_1.setPLANT(plant);
						JSONObject coaJson1=coaDAO.getCOAByName(plant, (String) vendno);
						if(coaJson1.isEmpty() || coaJson1.isNullObject()) {
							JSONObject vendorJson=vendorDAO.getVendorName(plant, (String) vendno);
							if(!vendorJson.isEmpty()) {
								coaJson1=coaDAO.getCOAByName(plant,vendorJson.getString("VNAME"));
								if(coaJson1.isEmpty() || coaJson1.isNullObject())
								{
									coaJson1=coaDAO.getCOAByName(plant, vendorJson.getString("VENDNO")+"-"+vendorJson.getString("VNAME"));
									jdesc=vendorJson.getString("VENDNO")+"-"+vendorJson.getString("VNAME");
								}

							
							}
						}
						if(coaJson1.isEmpty() || coaJson1.isNullObject())
						{
							
						}
						else
						{
							journalDetail_1.setACCOUNT_ID(Integer.parseInt(coaJson1.getString("id")));
							if(coaJson1.getString("account_name")!=null) {
								journalDetail_1.setACCOUNT_NAME(coaJson1.getString("account_name"));
							}
							journalDetail_1.setCREDITS(Double.parseDouble(totalAmount));
							journalDetails.add(journalDetail_1);
						}

						
						
						Double taxAmountFrom=Double.parseDouble(taxamount);
						if(taxAmountFrom>0)
						{
							JournalDetail journalDetail_2=new JournalDetail();
							journalDetail_2.setPLANT(plant);
							journalDetail_2.setDESCRIPTION(jdesc+"-"+billNo);
							
							MasterDAO masterDAO = new MasterDAO();
							String planttaxtype = masterDAO.GetTaxType(plant);
							
							if(planttaxtype.equalsIgnoreCase("TAX")) {
								JSONObject coaJson2=coaDAO.getCOAByName(plant, "VAT Input");
								journalDetail_2.setACCOUNT_ID(Integer.parseInt(coaJson2.getString("id")));
								journalDetail_2.setACCOUNT_NAME("VAT Input");
							}else if(planttaxtype.equalsIgnoreCase("GST")) {
								JSONObject coaJson2=coaDAO.getCOAByName(plant, "GST Receivable");
								journalDetail_2.setACCOUNT_ID(Integer.parseInt(coaJson2.getString("id")));
								journalDetail_2.setACCOUNT_NAME("GST Receivable");
							}else if(planttaxtype.equalsIgnoreCase("VAT")) {
								JSONObject coaJson2=coaDAO.getCOAByName(plant, "VAT Input");
								journalDetail_2.setACCOUNT_ID(Integer.parseInt(coaJson2.getString("id")));
								journalDetail_2.setACCOUNT_NAME("VAT Input");
							}else {
								JSONObject coaJson2=coaDAO.getCOAByName(plant, "VAT Input");
								journalDetail_2.setACCOUNT_ID(Integer.parseInt(coaJson2.getString("id")));
								journalDetail_2.setACCOUNT_NAME("VAT Input");
							}
							
							journalDetail_2.setDEBITS(taxAmountFrom);
							journalDetails.add(journalDetail_2);
						}

						
						
						
						Double discountFrom = Double.parseDouble(discount);
						Double orderDiscountFrom=0.00;
							
						if(discountFrom>0 || orderDiscountFrom>0)
						{
							if(!discountType.isEmpty())
							{
								if(discountType.equalsIgnoreCase("%"))
								{
									Double subTotalAfterOrderDiscount=Double.parseDouble(subTotal)-orderDiscountFrom;
									discountFrom=(subTotalAfterOrderDiscount*discountFrom)/100;
								}
							}
							discountFrom=discountFrom+orderDiscountFrom;
							JournalDetail journalDetail_3=new JournalDetail();
							journalDetail_3.setPLANT(plant);
							JSONObject coaJson3=coaDAO.getCOAByName(plant, "Discount Received");
							journalDetail_3.setACCOUNT_ID(Integer.parseInt(coaJson3.getString("id")));
							journalDetail_3.setACCOUNT_NAME("Discount Received");
							journalDetail_3.setCREDITS(discountFrom);
							journalDetails.add(journalDetail_3);
						}

						
						if(!shippingCost.isEmpty())
						{
							Double shippingCostFrom=Double.parseDouble(shippingCost);
							if(shippingCostFrom>0)
							{
								JournalDetail journalDetail_4=new JournalDetail();
								journalDetail_4.setPLANT(plant);
								JSONObject coaJson4=coaDAO.getCOAByName(plant, "Inward freight & shipping");
								journalDetail_4.setACCOUNT_ID(Integer.parseInt(coaJson4.getString("id")));
								journalDetail_4.setACCOUNT_NAME("Inward freight & shipping");
								journalDetail_4.setDEBITS(shippingCostFrom);
								journalDetails.add(journalDetail_4);

								
								for(Map billDet:billDetInfoList)
								{
									Double quantity=Double.parseDouble(billDet.get("QTY").toString());
									String netWeight=itemMstDAO.getItemNetWeight(plant, billDet.get("ITEM").toString());
									Double Netweight=quantity*Double.parseDouble(netWeight);
									Double calculatedShippingCost=0.0;
										if(totalItemNetWeight>0)
										{
											if(Netweight>0)
											{
												calculatedShippingCost=(shippingCostFrom*Netweight)/totalItemNetWeight;
											}
											else
											{
												calculatedShippingCost=0.00;
											}
										}
										else
										{
											calculatedShippingCost=shippingCostFrom/totalline;
										}
									System.out.println("calculatedShippingCost:"+calculatedShippingCost);
									
									JournalDetail journalDetail_5=new JournalDetail();
									journalDetail_5.setPLANT(plant);
									JSONObject coaJson5=coaDAO.getCOAByName(plant, (String) billDet.get("ACCOUNT_NAME"));
									journalDetail_5.setACCOUNT_ID(Integer.parseInt(coaJson5.getString("id")));
									journalDetail_5.setACCOUNT_NAME((String) billDet.get("ACCOUNT_NAME"));
									journalDetail_5.setDEBITS(calculatedShippingCost);
									boolean isLoop=false;
									if(journalReversalList.size()>0)
									{
										int i=0;
										for(JournalDetail journal:journalReversalList) {
											int accountId=journal.getACCOUNT_ID();
											if(accountId==journalDetail_5.getACCOUNT_ID()) {
												isLoop=true;
												Double sumDetit=journal.getDEBITS()+journalDetail_5.getDEBITS();
												journalDetail_5.setDEBITS(sumDetit);
												journalReversalList.set(i, journalDetail_5);
												break;
											}
											i++;

											
										}
										if(isLoop==false) {
											journalReversalList.add(journalDetail_5);
										}
									}
									else
									{
										journalReversalList.add(journalDetail_5);
									}
								}

								
								JournalDetail journalDetail_6=new JournalDetail();
								journalDetail_6.setPLANT(plant);
								JSONObject coaJson6=coaDAO.getCOAByName(plant, "Inward freight & shipping");
								journalDetail_6.setACCOUNT_ID(Integer.parseInt(coaJson6.getString("id")));
								journalDetail_6.setACCOUNT_NAME("Inward freight & shipping");
								journalDetail_6.setCREDITS(shippingCostFrom);
								journalReversalList.add(journalDetail_6);
							}

							
						}
						if(!adjustment.isEmpty())
						{
							Double adjustFrom=Double.parseDouble(adjustment);
							if(adjustFrom>0)
							{
								JournalDetail journalDetail_7=new JournalDetail();
								journalDetail_7.setPLANT(plant);
								JSONObject coaJson6=coaDAO.getCOAByName(plant, "Adjustment");
								journalDetail_7.setACCOUNT_ID(Integer.parseInt(coaJson6.getString("id")));
								journalDetail_7.setACCOUNT_NAME("Adjustment");
								journalDetail_7.setDEBITS(adjustFrom);
								journalDetails.add(journalDetail_7);
							}
							else if(adjustFrom<0)
							{
								JournalDetail journalDetail_7=new JournalDetail();
								journalDetail_7.setPLANT(plant);
								JSONObject coaJson6=coaDAO.getCOAByName(plant, "Adjustment");
								journalDetail_7.setACCOUNT_ID(Integer.parseInt(coaJson6.getString("id")));
								journalDetail_7.setACCOUNT_NAME("Adjustment");
								adjustFrom=Math.abs(adjustFrom);
								journalDetail_7.setCREDITS(adjustFrom);
								journalDetails.add(journalDetail_7);
							}
						}

						
						Journal journal=new Journal();
						Double totalDebitAmount=0.00;
						for(JournalDetail jourDet:journalDetails)
						{
							 totalDebitAmount=totalDebitAmount+jourDet.getDEBITS();
						}
						journalHead.setTOTAL_AMOUNT(totalDebitAmount);
						journal.setJournalHeader(journalHead);
						journal.setJournalDetails(journalDetails);
						JournalService journalService=new JournalEntry();
						journalService.addJournal(journal, username);
						Hashtable jhtMovHis = new Hashtable();
						jhtMovHis.put(IDBConstants.PLANT, plant);
						jhtMovHis.put("DIRTYPE", TransactionConstants.CREATE_JOURNAL);	
						jhtMovHis.put(IDBConstants.TRAN_DATE, DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));														
						jhtMovHis.put(IDBConstants.ITEM, "");
						jhtMovHis.put(IDBConstants.QTY, "0.0");
						jhtMovHis.put("RECID", "");
						jhtMovHis.put(IDBConstants.MOVHIS_ORDNUM,journal.getJournalHeader().getTRANSACTION_TYPE()+" "+journal.getJournalHeader().getTRANSACTION_ID());
						jhtMovHis.put(IDBConstants.CREATED_BY, username);		
						jhtMovHis.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
						jhtMovHis.put("REMARKS","");
						movHisDao.insertIntoMovHis(jhtMovHis);
						
						List<JournalDetail> expensepo_journaldetails=new ArrayList<>();
						if(shipRef != "") {
						ArrayList  movQryList = new MasterUtil().getExpenseDetailusingponoanddnol(plant, pono, shipRef);
						if (movQryList.size() > 0) {
							ht = new Hashtable();
							ht.put("PLANT",plant);
							ht.put("SHIPMENT_CODE",shipRef);
							ht.put("PONO",pono);
							List expenseHdrList = new ExpensesDAO().getExpensehdrbyponoandshipment(ht);
							for(int j=0;j < expenseHdrList.size();j++) {
//								Journal journal_2=new Journal();
								Map expenseHdr = (Map) expenseHdrList.get(j);									
								JournalHeader expensepo_journalHead=new JournalHeader();
								expensepo_journalHead.setPLANT(plant);								
								expensepo_journalHead.setJOURNAL_STATUS("PUBLISHED");
								expensepo_journalHead.setJOURNAL_TYPE("Cash");
								expensepo_journalHead.setCURRENCYID(curency);
								expensepo_journalHead.setTRANSACTION_ID((String) expenseHdr.get("ID"));
								expensepo_journalHead.setTRANSACTION_TYPE("EXPENSE FOR PO("+pono+")");
								expensepo_journalHead.setJOURNAL_DATE(billDate);
								expensepo_journalHead.setSUB_TOTAL(Double.parseDouble((String) expenseHdr.get("SUB_TOTAL")));
								expensepo_journalHead.setTOTAL_AMOUNT(Double.parseDouble((String) expenseHdr.get("TOTAL_AMOUNT")));
								expensepo_journalHead.setCRAT(DateUtils.getDateTime());
								expensepo_journalHead.setCRBY(username);
								
								
								JournalDetail inventorySelected=new JournalDetail();
								inventorySelected.setPLANT(plant);
								JSONObject coaJsonPaid=coaDAO.getCOAByName(plant, "Inventory Asset");
								inventorySelected.setACCOUNT_ID(Integer.parseInt(coaJsonPaid.getString("id")));
								inventorySelected.setACCOUNT_NAME("Inventory Asset");
								inventorySelected.setDEBITS(Double.parseDouble((String) expenseHdr.get("SUB_TOTAL")));
								expensepo_journaldetails.add(inventorySelected);
								
//								double expenseTaxAmount = 0.0;
								for(int i =0; i<movQryList.size(); i++) {
									Map arrCustLine = (Map)movQryList.get(i);
									if(Double.parseDouble((String)expenseHdr.get("ID")) == Double.parseDouble((String)arrCustLine.get("ID"))) {
										JournalDetail expensesSelected=new JournalDetail();
										expensesSelected.setPLANT(plant);
										JSONObject coaJson=coaDAO.getCOAByName(plant, (String)arrCustLine.get("EXPENSES_ACCOUNT"));
										expensesSelected.setACCOUNT_ID(Integer.parseInt(coaJson.getString("id")));
										expensesSelected.setACCOUNT_NAME((String)arrCustLine.get("EXPENSES_ACCOUNT"));
										expensesSelected.setCREDITS(Double.parseDouble((String)arrCustLine.get("AMOUNT").toString()));
										expensepo_journaldetails.add(expensesSelected);
										journalReversalList.add(expensesSelected);
//										expenseTaxAmount = Double.parseDouble((String)arrCustLine.get("TAXAMOUNT").toString());
									}

								}
								
								
								
								boolean isDebitExists = false;
								for(JournalDetail journDet:journalReversalList)
								{
									double debits = journDet.getDEBITS();
									if(journDet.getACCOUNT_NAME().equalsIgnoreCase("Inventory Asset")) {
										journDet.setDEBITS(debits+inventorySelected.getDEBITS());
										journDet.setCREDITS(discountFrom);
										isDebitExists = true;

									}
									
								}

								if(!isDebitExists) {
									Map billDet = billDetInfoList.get(0);
									JournalDetail journalDetail=new JournalDetail();
									journalDetail.setPLANT(plant);
									JSONObject coaJson5=coaDAO.getCOAByName(plant, (String) billDet.get("ACCOUNT_NAME"));
									journalDetail.setACCOUNT_ID(Integer.parseInt(coaJson5.getString("id")));
									journalDetail.setACCOUNT_NAME((String) billDet.get("ACCOUNT_NAME"));
									journalDetail.setDEBITS(inventorySelected.getDEBITS());
									journalDetail.setCREDITS(discountFrom);
									journalReversalList.add(journalDetail);
								}
								
								/*if(expenseTaxAmount>0)
								{
									JournalDetail journalDetail=new JournalDetail();
									journalDetail.setPLANT(plant);
									JSONObject coaJson2=coaDAO.getCOAByName(plant, "VAT Input");
									journalDetail.setACCOUNT_ID(Integer.parseInt(coaJson2.getString("id")));
									journalDetail.setACCOUNT_NAME("VAT Input");
									journalDetail.setCREDITS(expenseTaxAmount);
									journalReversalList.add(journalDetail);
								}*/
								
								if(discountFrom>0 || orderDiscountFrom>0)
								{
									JournalDetail journalDetail_3=new JournalDetail();
									journalDetail_3.setPLANT(plant);
									JSONObject coaJson3=coaDAO.getCOAByName(plant, "Discount Received");
									journalDetail_3.setACCOUNT_ID(Integer.parseInt(coaJson3.getString("id")));
									journalDetail_3.setACCOUNT_NAME("Discount Received");
									journalDetail_3.setDEBITS(discountFrom);
									journalReversalList.add(journalDetail_3);
								}
								
								/*journal_2.setJournalHeader(expensepo_journalHead);
								journal_2.setJournalDetails(expensepo_journaldetails);
								journalService.addJournal(journal_2, username);*/
							}
							
						}

						}
						
						if(journalReversalList.size()>0)
						{
							JournalHeader journalReversalHead=journalHead;
							Double totalDebitReversal=0.00;
							for(JournalDetail journDet:journalReversalList)
							{
								totalDebitReversal=totalDebitReversal+journDet.getDEBITS();
								

							}
							journalReversalHead.setTOTAL_AMOUNT(totalDebitReversal);
							Journal journal_1=new Journal();
							journalReversalHead.setTRANSACTION_TYPE("BILL_REVERSAL");
							journal_1.setJournalHeader(journalReversalHead);
							journal_1.setJournalDetails(journalReversalList);
							journalService.addJournal(journal_1, username);
							Hashtable jhtMovHis1 = new Hashtable();
							jhtMovHis1.put(IDBConstants.PLANT, plant);
							jhtMovHis1.put("DIRTYPE", TransactionConstants.CREATE_JOURNAL);	
							jhtMovHis1.put(IDBConstants.TRAN_DATE, DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));														
							jhtMovHis1.put(IDBConstants.ITEM, "");
							jhtMovHis1.put(IDBConstants.QTY, "0.0");
							jhtMovHis1.put("RECID", "");
							jhtMovHis1.put(IDBConstants.MOVHIS_ORDNUM,journal_1.getJournalHeader().getTRANSACTION_TYPE()+" "+journal_1.getJournalHeader().getTRANSACTION_ID());
							jhtMovHis1.put(IDBConstants.CREATED_BY, username);		
							jhtMovHis1.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
							jhtMovHis1.put("REMARKS","");
							movHisDao.insertIntoMovHis(jhtMovHis1);
						}
					}
				}
				if(isAdded && (polnno.size() > 0 && pono.length() > 0)) {
					if(!grno.equalsIgnoreCase("")) {
						String query = "SET BILL_STATUS = 'BILLED'";
						String extCond = " AND LNNO IN ("+polnnoIn+") ";
						Hashtable<String, String> htCondition = new Hashtable<String, String>();
						htCondition.put("PONO", pono);
						htCondition.put("GRNO", grno);				
						isAdded = recvDao.update(query, htCondition, extCond, plant);
					}
				}
			}

			
			
			
			if(isAdded) {
				for(int i =0 ; i < item.size() ; i++){
				Hashtable htMovHis = new Hashtable();
				htMovHis.clear();
				htMovHis.put(IDBConstants.PLANT, plant);					
				htMovHis.put("DIRTYPE", TransactionConstants.CREATE_BILL);
				if(isgrn.equalsIgnoreCase("1"))
					htMovHis.put("DIRTYPE", TransactionConstants.CONVERT_TO_BILL);	
				htMovHis.put(IDBConstants.TRAN_DATE, DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));
//				htMovHis.put(IDBConstants.TRAN_DATE, DateUtils.getDateinyyyy_mm_dd(billDate));														
				htMovHis.put(IDBConstants.ITEM, (String) item.get(i));
				String billqty = String.valueOf((String) qty.get(i));
				htMovHis.put(IDBConstants.QTY, billqty);
				htMovHis.put("RECID", "");
				htMovHis.put(IDBConstants.MOVHIS_ORDNUM, billNo);
				htMovHis.put(IDBConstants.CREATED_BY, username);		
				htMovHis.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
				htMovHis.put("REMARKS",pono+","+grno+","+refNum);
				isAdded = movHisDao.insertIntoMovHis(htMovHis); // Insert MOVHIS
				}
			}
						
			//Update BILL Seq
			if(isAdded) {
			Hashtable htv = new Hashtable();				
			htv.put("PLANT", plant);
			htv.put("FUNC", "BILL");
			isAdded = _TblControlDAO.isExisit(htv, "", plant);
			if (isAdded) 
				isAdded=_TblControlDAO.updateSeqNo("BILL",plant);
			}

			
			//GRN to Bill Status Update
			if(isAdded) {
				if(grno.length()>0&&pono.length()>0)
				{
				BillDAO billDao = new BillDAO();
				Hashtable<String, String> htCondition = new Hashtable<String, String>();
				htCondition.put("PONO", pono);
				htCondition.put("GRNO", grno);
					isAdded = billDao.updateGrntoBill("SET STATUS='BILLED' ", htCondition, "", plant);
				if(shipRef.length()>0) {
					ExpensesDAO expDao = new ExpensesDAO();
					String query = " SET STATUS='BILLED' ";
					htCondition = new Hashtable<String, String>();
					htCondition.put("PONO", pono);
					htCondition.put("SHIPMENT_CODE", shipRef);
					htCondition.put("PLANT", plant);
					int count = expDao.update(query, htCondition, "");
					if(count > 0) {
						isAdded = true;
					}else {
						isAdded = false;
					}						
				}
			}
			/* Added by Abhilash to handle COGS */
			for(Map billDet:billDetInfoList){			
				
				List pendingCogsInvoiceDetails = new InvoiceDAO().invoiceWoCOGS((String)billDet.get("ITEM"), plant);
				if(pendingCogsInvoiceDetails.size()>0) {								
					for(int i = 0; i < pendingCogsInvoiceDetails.size(); i++) {
						Double totalCostofGoodSold=0.00;
						CoaDAO coaDAO=new CoaDAO();
						Journal journal=new Journal();
						List<JournalDetail> journalDetails=new ArrayList<>();
						Map pendingCogsInvoice = (Map) pendingCogsInvoiceDetails.get(i);

						
						Map invDetail = new InvMstDAO().getInvDataByProduct((String)pendingCogsInvoice.get("ITEM"), plant);
						double inv_qty=0, bill_qty = 0, unbill_qty = 0, net_bill_qty=0, 
								invoiced_qty = 0, invoice_quantity = 0, quantity = 0;
						inv_qty = Double.parseDouble((String)invDetail.get("INV_QTY"));
						bill_qty = Double.parseDouble((String)invDetail.get("BILL_QTY"));
						unbill_qty = Double.parseDouble((String)invDetail.get("UNBILL_QTY"));
						invoiced_qty = Double.parseDouble((String)invDetail.get("INVOICE_QTY"));
						invoice_quantity = Double.parseDouble((String)pendingCogsInvoice.get("QTY"));
						quantity =Double.parseDouble(pendingCogsInvoice.get("QTY").toString());
						

						bill_qty = bill_qty - invoiced_qty;
						net_bill_qty = bill_qty + unbill_qty;							
						
						if((net_bill_qty != inv_qty) && (bill_qty >= invoice_quantity)) {
							ArrayList invQryList;
							Hashtable ht_cog = new Hashtable();
							ht_cog.put("a.PLANT",plant);
							ht_cog.put("a.ITEM",(String)billDet.get("ITEM"));
							invQryList= new InvUtil().getAverageCost(ht_cog, plant, (String)billDet.get("ITEM"), curency, curency);
							if(invQryList!=null){
								if(!invQryList.isEmpty()){
									Map lineArr = (Map) invQryList.get(0);
									String avg = StrUtils.addZeroes(Double.parseDouble((String)lineArr.get("AVERAGE_COST")),"2");
									totalCostofGoodSold += Double.parseDouble(avg)*(quantity);
								}
							}

							
							JournalService journalService=new JournalEntry();
							Journal journalCOG=journalService.getJournalByTransactionId(plant, (String)pendingCogsInvoice.get("INVOICE"), "COSTOFGOODSOLD");
							
							if(journalCOG.getJournalHeader()!=null){
								if(journalCOG.getJournalHeader().getID()>0){
									totalCostofGoodSold += journalCOG.getJournalHeader().getTOTAL_AMOUNT();
								}
							}

							
							JournalHeader journalHead=new JournalHeader();
							journalHead.setPLANT(plant);
							journalHead.setJOURNAL_DATE(DateUtils.getDate());
							journalHead.setJOURNAL_STATUS("PUBLISHED");
							journalHead.setJOURNAL_TYPE("Cash");
							journalHead.setCURRENCYID(curency);
							journalHead.setTRANSACTION_TYPE("COSTOFGOODSOLD");
							journalHead.setTRANSACTION_ID((String)pendingCogsInvoice.get("INVOICE"));
							journalHead.setSUB_TOTAL(totalCostofGoodSold);
							journalHead.setCRAT(DateUtils.getDateTime());
							journalHead.setCRBY(username);

							
							JournalDetail journalDetail_InvAsset=new JournalDetail();
							journalDetail_InvAsset.setPLANT(plant);
							JSONObject coaJson7=coaDAO.getCOAByName(plant, "Inventory Asset");
							System.out.println("Json"+coaJson7.toString());
							journalDetail_InvAsset.setACCOUNT_ID(Integer.parseInt(coaJson7.getString("id")));
							journalDetail_InvAsset.setACCOUNT_NAME(coaJson7.getString("account_name"));
							journalDetail_InvAsset.setCREDITS(totalCostofGoodSold);
							journalDetails.add(journalDetail_InvAsset);

							
							JournalDetail journalDetail_COG=new JournalDetail();
							journalDetail_COG.setPLANT(plant);
							JSONObject coaJson8=coaDAO.getCOAByName(plant, "Cost of goods sold");
							System.out.println("Json"+coaJson8.toString());
							journalDetail_COG.setACCOUNT_ID(Integer.parseInt(coaJson8.getString("id")));
							journalDetail_COG.setACCOUNT_NAME(coaJson8.getString("account_name"));
							journalDetail_COG.setDEBITS(totalCostofGoodSold);
							journalDetails.add(journalDetail_COG);

							
							journalHead.setTOTAL_AMOUNT(totalCostofGoodSold);
							journal.setJournalHeader(journalHead);
							journal.setJournalDetails(journalDetails);

							
							if(journalCOG.getJournalHeader()!=null)
							{
								if(journalCOG.getJournalHeader().getID()>0)
								{
									journalHead.setID(journalCOG.getJournalHeader().getID());
									journalService.updateJournal(journal, username);
									Hashtable jhtMovHis = new Hashtable();
									jhtMovHis.put(IDBConstants.PLANT, plant);
									jhtMovHis.put("DIRTYPE", TransactionConstants.EDIT_JOURNAL);	
									jhtMovHis.put(IDBConstants.TRAN_DATE, DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));														
									jhtMovHis.put(IDBConstants.ITEM, "");
									jhtMovHis.put(IDBConstants.QTY, "0.0");
									jhtMovHis.put("RECID", "");
									jhtMovHis.put(IDBConstants.MOVHIS_ORDNUM,journal.getJournalHeader().getTRANSACTION_TYPE()+" "+journal.getJournalHeader().getTRANSACTION_ID());
									jhtMovHis.put(IDBConstants.CREATED_BY, username);		
									jhtMovHis.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
									jhtMovHis.put("REMARKS","");
									movHisDao.insertIntoMovHis(jhtMovHis);
								}
								else
								{
									journalService.addJournal(journal, username);
									Hashtable jhtMovHis = new Hashtable();
									jhtMovHis.put(IDBConstants.PLANT, plant);
									jhtMovHis.put("DIRTYPE", TransactionConstants.CREATE_JOURNAL);	
									jhtMovHis.put(IDBConstants.TRAN_DATE, DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));														
									jhtMovHis.put(IDBConstants.ITEM, "");
									jhtMovHis.put(IDBConstants.QTY, "0.0");
									jhtMovHis.put("RECID", "");
									jhtMovHis.put(IDBConstants.MOVHIS_ORDNUM,journal.getJournalHeader().getTRANSACTION_TYPE()+" "+journal.getJournalHeader().getTRANSACTION_ID());
									jhtMovHis.put(IDBConstants.CREATED_BY, username);		
									jhtMovHis.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
									jhtMovHis.put("REMARKS","");
									movHisDao.insertIntoMovHis(jhtMovHis);
								}
							}
							 new InvoiceDAO().update_is_cogs_set((String)pendingCogsInvoice.get("INVOICEHDRID"), (String)pendingCogsInvoice.get("LNNO"), (String)pendingCogsInvoice.get("ITEM"), plant);
						}									
					}
				}					
			}
			}
			if(isAdded) {
				DbBean.CommitTran(ut);
				result = "ok";
			}else {
				DbBean.RollbackTran(ut);
	 			result = "Bill not created";
			}
			if(result.equalsIgnoreCase("Bill not created")) {
				 result = "not ok";
			}
			
		} catch (Exception e) {
			 e.printStackTrace();
			 autoinverr=e.toString();
			 result = "not ok";
		}
	
		return result;
	}
	private String IncomingDataBulkbyOrders(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String chkdPoNo = "",collectionTime = "",orderLNo = "",receivingQty = "",ITEM_BATCH = "",item = "",ITEM_DESCRIPTION = "",ISNONSTKFLG="";
		String xmlStr = "" ,QTYOR="",PO_NUM = "",CUST_NAME = "",jobNum = "",PONO="",polno="",QTYRC="",FROMDATE="",TODATE="";
		String REMARKS = "",collectionDate = "",remark= "",ITEM_LOC= "",REF = "",EXPIREDATE="",RECVDATE = "",invoiceNum= "",strTranDate="",strRecvDate="",GRNO="",UOM="",UOMQTY="";
		String[]  checkedOrder;
		UserLocUtil uslocUtil = new UserLocUtil();
		//UserTransaction ut = null;
		Map receiveMaterial_HM = null;
		ItemMstDAO _ItemMstDAO = new ItemMstDAO();
		Map checkedPOS = new HashMap();
		Boolean allChecked = false,fullReceive = false;
		String sepratedtoken1 = "";
		Map mp = null;
		mp = new HashMap();
		try {
			String sepratedtoken = "";
			String totalString = StrUtils.fString(request.getParameter("TRAVELER"));
			StringTokenizer parser = new StringTokenizer(totalString, "=");
			HttpSession session = request.getSession(false);
			PLANT = StrUtils.fString((String) session.getAttribute("PLANT"));
			String[] chkdPoNos  = request.getParameterValues("chkdPoNo");
			ITEM_LOC = StrUtils.fString(request.getParameter("LOC_0"));
			EXPIREDATE = StrUtils.fString(request.getParameter("EXPIREDATE_0"));
			RECVDATE = StrUtils.fString(request.getParameter("TRANSACTIONDATE"));
			REMARKS = StrUtils.fString(request.getParameter("REF"));
			String LOGIN_USER = StrUtils.fString(request.getParameter("LOGIN_USER"));
			CUST_NAME = StrUtils.fString(request.getParameter("CUST_NAME"));
			jobNum = StrUtils.fString(request.getParameter("JOB_NUM"));
			collectionDate = StrUtils.fString(request.getParameter("COLLECTION_DATE"));
			collectionTime = StrUtils.fString(request.getParameter("COLLECTION_TIME"));
			FROMDATE = StrUtils.fString(request.getParameter("FROM_DATE"));
			TODATE = StrUtils.fString(request.getParameter("TO_DATE"));
			GRNO = StrUtils.fString(request.getParameter("GRNO"));
			if (RECVDATE.length()>5)
				strTranDate    = RECVDATE.substring(6)+"-"+ RECVDATE.substring(3,5)+"-"+RECVDATE.substring(0,2);
			    strRecvDate    = RECVDATE.substring(0,2)+"/"+ RECVDATE.substring(3,5)+"/"+RECVDATE.substring(6);
	
			
			boolean isvalidlocforUser = uslocUtil .isValidLocInLocmstForUser(PLANT, LOGIN_USER, ITEM_LOC);
            _POUtil.setmLogger(mLogger);
            if (!isvalidlocforUser) {
                throw new Exception("Receiving Loc :" + ITEM_LOC + " is not User Assigned Location");
            }
            if (chkdPoNos != null)    {     
				for (int i = 0; i < chkdPoNos.length; i++) 
				{
					String	data = chkdPoNos[i];
					String[] chkdata = data.split(",");
					orderLNo=chkdata[0]+"_"+chkdata[1];
					
					receivingQty = StrUtils.fString(request.getParameter("receivingQTY_"+orderLNo));
					ITEM_BATCH = StrUtils.fString(request.getParameter("BATCH_"+orderLNo));
					if (ITEM_BATCH.length() == 0) {
						ITEM_BATCH = "NOBATCH";
					}
					checkedPOS.put(orderLNo, receivingQty+":"+ITEM_BATCH);
				}
				session.setAttribute("checkedPOS", checkedPOS);
            }
            Boolean transactionHandler = true;
          
    		ArrayList PODetails = null;
    		ArrayList alPoHdr = null;

    		while (parser.hasMoreTokens())

			{
				int count = 1;
				sepratedtoken = parser.nextToken();

				System.out.println("sepratedtoken ::" + sepratedtoken);
				StringTokenizer parser1 = new StringTokenizer(sepratedtoken,
						",");

				while (parser1.hasMoreTokens())

				{
					sepratedtoken1 = parser1.nextToken();

					mp.put("data" + count, sepratedtoken1);

					count++;

				}

			PONO = StrUtils.fString((String) mp.get("data1"));
			polno = StrUtils.fString((String) mp.get("data2"));
			QTYOR = StrUtils.fString((String) mp.get("data3"));
			QTYRC = StrUtils.fString((String) mp.get("data4"));
			CUST_NAME = StrUtils.replaceCharacters2Recv(StrUtils.fString((String) mp.get("data5")));
			UOM = StrUtils.fString((String) mp.get("data6"));
			UOMQTY = StrUtils.fString((String) mp.get("data7"));
			if( request.getParameter("select")!=null){
				allChecked = true;
			}
			if(request.getParameter("fullReceive")!=null){
				fullReceive = true;
			}
			
			receivingQty = StrUtils.fString(request.getParameter("receivingQTY_"+PONO+"_"+polno));
			ITEM_BATCH = StrUtils.fString(request.getParameter("BATCH_"+PONO+"_"+polno));
			if (ITEM_BATCH.length() == 0) {
					ITEM_BATCH = "NOBATCH";
			}
			
			Hashtable htPoDet = new Hashtable();
    		String queryPoDet = "item,itemDesc,QTYOR,QTYRC";
    		
			htPoDet.put(IConstants.PODET_PONUM, PONO);
		    htPoDet.put(IConstants.PLANT, PLANT);
		   	htPoDet.put(IConstants.PODET_POLNNO, polno);
			PODetails = _POUtil.getPoDetDetails(queryPoDet, htPoDet);
					if (PODetails.size() > 0) {	

							Map map1 = (Map) PODetails.get(0);
							item = (String) map1.get("item");
							ITEM_DESCRIPTION = (String) map1.get("itemDesc");
							QTYOR = (String) map1.get("QTYOR");
							ISNONSTKFLG =  new ItemMstDAO().getNonStockFlag(PLANT,item);
					}
					   receiveMaterial_HM = new HashMap();
                       receiveMaterial_HM.put(IConstants.PLANT, PLANT);
                       receiveMaterial_HM.put(IConstants.CUSTOMER_NAME, CUST_NAME);
                       receiveMaterial_HM.put(IConstants.ITEM, item);
                       receiveMaterial_HM.put(IConstants.ITEM_DESC, _ItemMstDAO.getItemDesc(PLANT ,item));
                       receiveMaterial_HM.put(IConstants.PODET_PONUM, PONO);
                       receiveMaterial_HM.put(IConstants.PODET_POLNNO, polno);
                       receiveMaterial_HM.put(IConstants.LOC, ITEM_LOC);
                       receiveMaterial_HM.put(IConstants.LOGIN_USER, LOGIN_USER);
                       receiveMaterial_HM.put(IConstants.CUSTOMER_CODE, _POUtil.getCustCode(PLANT, PONO));
                       receiveMaterial_HM.put(IConstants.INV_BATCH, ITEM_BATCH);
                       receiveMaterial_HM.put(IConstants.BATCH, ITEM_BATCH);
                       receiveMaterial_HM.put(IConstants.QTY, QTYOR);
                       receiveMaterial_HM.put(IConstants.ORD_QTY, QTYOR);
                       receiveMaterial_HM.put(IConstants.INV_QTY, receivingQty);
                       receiveMaterial_HM.put(IConstants.RECV_QTY, receivingQty);
                       receiveMaterial_HM.put(IConstants.INV_EXP_DATE, "");
                       receiveMaterial_HM.put(IConstants.USERFLD1, REMARKS);
                      // receiveMaterial_HM.put(IConstants.USERFLD2, FAX);
                       receiveMaterial_HM.put(IConstants.CREATED_AT,  (String) DateUtils.getDateTime());
                       receiveMaterial_HM.put(IConstants.CREATED_BY, LOGIN_USER);
                       receiveMaterial_HM.put(IConstants.JOB_NUM, _POUtil.getJobNum( PLANT, PONO));
                       receiveMaterial_HM.put(IConstants.REMARKS, REMARKS);
                      receiveMaterial_HM.put(IConstants.EXPIREDATE, EXPIREDATE);
                      receiveMaterial_HM.put(IConstants.TRAN_DATE, strTranDate);
                      receiveMaterial_HM.put(IConstants.RECVDATE, strRecvDate);
                      receiveMaterial_HM.put(IConstants.GRNO, GRNO);//Ravindra
                      receiveMaterial_HM.put("NONSTKFLAG", ISNONSTKFLG);
                      receiveMaterial_HM.put(IConstants.UOM, UOM);
                      receiveMaterial_HM.put("UOMQTY", UOMQTY);
                       _POUtil.setmLogger(mLogger);
                       
       				if (isvalidlocforUser) {
       					transactionHandler = _POUtil.processMultiReceiveMaterialByWMS(receiveMaterial_HM) && transactionHandler;
       					
       				} else {
       					transactionHandler = transactionHandler && false;
       					throw new Exception("Receiving Loc :" + ITEM_LOC
       							+ " is not User Assigned Location");

       				}     				

       				if (transactionHandler == true) {//Shopify Inventory Update
       					Hashtable htCond = new Hashtable();
       					htCond.put(IConstants.PLANT, PLANT);
       					if(new IntegrationsDAO().getShopifyConfigCount(htCond,"")) {
       						String nonstkflag = new ItemMstDAO().getNonStockFlag(PLANT,item);						
    						if(nonstkflag.equalsIgnoreCase("N")) {
       						String availqty ="0";
       						ArrayList invQryList = null;
       						htCond = new Hashtable();
       						invQryList= new InvUtil().getInvListSummaryAvailableQtyforShopify(PLANT,item, new ItemMstDAO().getItemDesc(PLANT, item),htCond);						
       						if (invQryList.size() > 0) {					
       							for (int iCnt =0; iCnt<invQryList.size(); iCnt++){
       								String result="";
                                    Map lineArr = (Map) invQryList.get(iCnt);
                                    availqty = (String)lineArr.get("AVAILABLEQTY");
                                    System.out.println(availqty);
       							}
       							double availableqty = Double.parseDouble(availqty);
           						new ShopifyService().UpdateShopifyInventoryItem(PLANT, item, availableqty);
       						}	
    						}
       					}
       				}
       				
				}
				if (transactionHandler == true) {
					new TblControlUtil().updateTblControlIESeqNo(PLANT, "GRN", "GN", GRNO);
					request.getSession(false).setAttribute("RESULT","Products  received successfully!");

					response.sendRedirect("../purchasetransaction/receiptsummarybymultiple?action=View&LOC="
									+ ITEM_LOC
									+"&FROM_DATE="
									+FROMDATE
									+"&TO_DATE="
									+TODATE
									+"&result=sucess");
				} else {
					request.getSession(false).setAttribute("RESULTERROR","Failed to Receive");
									
						response.sendRedirect("../purchasetransaction/receiptsummarybymultiple?result=error");
				}
			
		}
		catch(Exception e)
		{
			this.mLogger.exception(this.printLog, "", e);
			request.getSession(false).setAttribute("CATCHERROR", e.getMessage());
			response.sendRedirect("../purchasetransaction/receiptsummarybymultiple?action=View&PLANT="
						+ PLANT 
						+"&FROM_DATE="
						+FROMDATE
						+"&TO_DATE="
						+TODATE
						+"&LOC="
						+ITEM_LOC
						+"&REF="
						+REMARKS
						+ "&EXPIREDATE="
						+EXPIREDATE
						+ "&RECVDATE="
						+RECVDATE
						+ "&allChecked="
						+allChecked
						+"&fullReceive="
						+fullReceive
						);
			throw e;
		}
		return xmlStr;
	}

	   private String load_Inbound_item_orderline_details(HttpServletRequest request,
   			HttpServletResponse response) throws IOException, ServletException {
   		String str = "", aPlant = "", aOrdNo = "", aItem="",LOGIN_USER="",uom="";
   		try {
   			// MLogger.log(0, "load_outBoundOrder_item_details() Starts ");
   			PLANT = StrUtils.fString(request.getParameter("PLANT"));
   			aOrdNo = StrUtils.fString(request.getParameter("ORDER_NUM"));
   			aItem = StrUtils.fString(request.getParameter("ITEM_NUM"));
   			LOGIN_USER = StrUtils.fString(request.getParameter("LOGIN_USER"));
   			uom = StrUtils.fString(request.getParameter("UOM"));

   			Hashtable ht = new Hashtable();
   			ht.put("PLANT", PLANT);
   			ht.put("PONO", aOrdNo);
   			ht.put("ITEM", aItem);

   			str = _POUtil.getinboundOrder_item_orderline_details(PLANT, aOrdNo,aItem,uom,
   					Boolean.FALSE);

   			if (str.equalsIgnoreCase("")) {
   				str = XMLUtils.getXMLMessage(1, "Product details not found.");
   			}

   		} catch (Exception e) {
   			this.mLogger.exception(this.printLog, "", e);
   			str = XMLUtils.getXMLMessage(1, e.getMessage());
   		}

   		// MLogger.log(0, "load_outBoundOrder_item_details() Ends");

   		return str;
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
	private String load_uom(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException,
			Exception {
		String str = "";

		String PLANT = "", userID = "",pono = "",item = "";
		try {
			PLANT = StrUtils.fString(request.getParameter("PLANT"));
			userID = StrUtils.fString(request.getParameter("LOGIN_USER"));
			pono = StrUtils.fString(request.getParameter("PO_NUM"));
			item = StrUtils.fString(request.getParameter("ITEM_NUM"));
			POUtil poUtil = new POUtil();
			poUtil.setmLogger(mLogger);
			str = poUtil.getUomWithQty(PLANT, userID,pono,item);
			if (str.equalsIgnoreCase("")) {

				str = XMLUtils.getXMLMessage(1, "Details not found");
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		}
		return str;
	}
		//created by vicky Used for PDA purchase order popup
	private String load_inBoundOrdersPDApopup(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {
		String str = "", aPlant = "", aOrdNo = "",START,END,PONO;
		try {

			PLANT = StrUtils.fString(request.getParameter("PLANT"));
			START = StrUtils.fString(request.getParameter("START"));
			END = StrUtils.fString(request.getParameter("END"));
			PONO = StrUtils.fString(request.getParameter("PONO"));

			Hashtable ht = new Hashtable();
			ht.put("PLANT", PLANT);

			str = _POUtil.getPDApopupInBoundOrder(PLANT,START,END,PONO);

			if (str.equalsIgnoreCase("")) {
				str = XMLUtils.getXMLMessage(1, "No Inbound Orders Found");
			}

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			str = XMLUtils.getXMLMessage(1, e.getMessage());
		}

		return str;
	}
	//created by vicky Used for PDA purchase order AutoSuggestion
		private String load_inBoundOrdersPDAAutoSuggestion(HttpServletRequest request,
				HttpServletResponse response) throws IOException, ServletException {
			String str = "", aPlant = "", aOrdNo = "",PONO;
			try {

				PLANT = StrUtils.fString(request.getParameter("PLANT"));
				PONO = StrUtils.fString(request.getParameter("PONO"));

				Hashtable ht = new Hashtable();
				ht.put("PLANT", PLANT);

				str = _POUtil.getPDApopupInBoundOrder(PLANT,PONO);

				if (str.equalsIgnoreCase("")) {
					str = XMLUtils.getXMLMessage(1, "No Inbound Orders Found");
				}

			} catch (Exception e) {
				this.mLogger.exception(this.printLog, "", e);
				str = XMLUtils.getXMLMessage(1, e.getMessage());
			}

			return str;
		}
		//created by vicky Used for PDA Item AutoSuggestion
		private String load_Allitem_details_AutoSuggestion(HttpServletRequest request,
				HttpServletResponse response) throws IOException, ServletException {
			String str = "", aPlant = "", aOrdNo = "",start="",end="",itemNo;
			try {

				PLANT = StrUtils.fString(request.getParameter("PLANT"));
				itemNo = StrUtils.fString(request.getParameter("ITEMNO"));
				
				
				Hashtable ht = new Hashtable();
				ht.put("PLANT", PLANT);
				ht.put("ITEMNO", itemNo);

				str = _POUtil.getItemFromProductMstForAutoSuggestion(PLANT,itemNo);

				if (str.equalsIgnoreCase("")) {
					str = XMLUtils.getXMLMessage(1, "Item details not found?");
				}

			} catch (Exception e) {
				this.mLogger.exception(this.printLog, "", e);
				str = XMLUtils.getXMLMessage(1, e.getMessage());
			}
			return str;
		}
		//Created by Vicky Desc:Used for getting Purchase Order item For UOM popup Screen in PDA
		   private String load_Inbound_item_orderdetails_ForPDA_UOMPopup(HttpServletRequest request,
		   			HttpServletResponse response) throws IOException, ServletException {
		   		String str = "", aPlant = "", aOrdNo = "", aItem="",LOGIN_USER="",uom="";
		   		try {
		   			// MLogger.log(0, "load_outBoundOrder_item_details() Starts ");
		   			PLANT = StrUtils.fString(request.getParameter("PLANT"));
		   			aOrdNo = StrUtils.fString(request.getParameter("ORDER_NUM"));
		   			aItem = StrUtils.fString(request.getParameter("ITEM_NUM"));
		   			LOGIN_USER = StrUtils.fString(request.getParameter("LOGIN_USER"));
		   			

		   			Hashtable ht = new Hashtable();
		   			ht.put("PLANT", PLANT);
		   			ht.put("PONO", aOrdNo);
		   			ht.put("ITEM", aItem);

		   			str = _POUtil.getinboundOrder_item_order_ForUOMPopup(PLANT, aOrdNo,aItem,uom,
		   					Boolean.FALSE);

		   			if (str.equalsIgnoreCase("")) {
		   				str = XMLUtils.getXMLMessage(1, "Product details not found.");
		   			}

		   		} catch (Exception e) {
		   			this.mLogger.exception(this.printLog, "", e);
		   			str = XMLUtils.getXMLMessage(1, e.getMessage());
		   		}

		   		return str;
		   	}
//Created by vicky Desc:Used for fetching All locations details for PDA		   
			private String load_locations_PDA(HttpServletRequest request,
					HttpServletResponse response) throws IOException, ServletException,
					Exception {
				String str = "";

				String PLANT = "", userID = "";
				try {
					PLANT = StrUtils.fString(request.getParameter("PLANT"));
					userID = StrUtils.fString(request.getParameter("LOGIN_USER"));
					LocUtil locUtil = new LocUtil();
					locUtil.setmLogger(mLogger);
					str = locUtil.getlocationsWithDescForPda(PLANT, userID);
					if (str.equalsIgnoreCase("")) {

						str = XMLUtils.getXMLMessage(1, "Details not found");
					}
				} catch (Exception e) {
					this.mLogger.exception(this.printLog, "", e);
					throw e;
				}
				return str;
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
