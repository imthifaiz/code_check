package com.track.pda;

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

import com.track.constants.IConstants;
import com.track.constants.IDBConstants;
import com.track.constants.MLoggerConstant;
import com.track.constants.TransactionConstants;
import com.track.dao.IntegrationsDAO;
import com.track.dao.InvMstDAO;
import com.track.dao.ItemMstDAO;
import com.track.dao.MovHisDAO;
import com.track.dao.POSDetDAO;
import com.track.dao.RecvDetDAO;
import com.track.dao.RsnMst;
import com.track.dao.TblControlDAO;
import com.track.db.util.DOUtil;
import com.track.db.util.InvMstUtil;
import com.track.db.util.InvUtil;
import com.track.db.util.ItemMstUtil;
import com.track.db.util.MiscReceivingUtil;
import com.track.db.util.POUtil;
import com.track.db.util.TblControlUtil;
import com.track.db.util.UomUtil;
import com.track.db.util.UserLocUtil;
import com.track.gates.DbBean;
import com.track.service.ShopifyService;
import com.track.util.DateUtils;
import com.track.util.IMLogger;
import com.track.util.MLogger;
import com.track.util.StrUtils;
import com.track.util.XMLUtils;	
import com.track.dao.RecvDetDAO;

public class MiscOrderReceivingServlet extends HttpServlet implements IMLogger {
	private boolean printLog = MLoggerConstant.MiscOrderReceivingServlet_PRINTPLANTMASTERLOG;
	private boolean printInfo = MLoggerConstant.MiscOrderReceivingServlet_PRINTPLANTMASTERINFO;
	private static final long serialVersionUID = -1581044121462190369L;
	private POUtil _POUtil = null;
	private MiscReceivingUtil _MiscReceivingUtil = null;
	
	private String PLANT = "";

	private String xmlStr = "";
	private String action = "";

	StrUtils strUtils = new StrUtils();

	private static final String CONTENT_TYPE = "text/xml";

	public void init() throws ServletException {
		_POUtil = new POUtil();
		_MiscReceivingUtil = new MiscReceivingUtil();

	}

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType(CONTENT_TYPE);
		PrintWriter out = response.getWriter();
		this.setMapDataToLogger(this.populateMapData(StrUtils.fString(request.getParameter("PLANT")),
				StrUtils.fString(request.getParameter("LOGIN_USER")) + " PDA_USER"));
		_POUtil.setmLogger(mLogger);
		_MiscReceivingUtil.setmLogger(mLogger);
		if (this.printInfo) {
			diaplayInfoLogs(request);
		}
		try {
			action = request.getParameter("action").trim();

			if (action.equalsIgnoreCase("load_item_details")) {
				xmlStr = "";
				xmlStr = load_Item_Details(request, response);
			} else if (action.equalsIgnoreCase("validate_data")) {
				xmlStr = "";
				xmlStr = validateData(request, response);
			} else if (action.equalsIgnoreCase("validateDataPDAMiscReceive")) {
				xmlStr = "";
				xmlStr = validateDataPDAMiscReceive(request, response);
			} else if (action.equalsIgnoreCase("validateDataByItemReceive")) {
				xmlStr = "";
				xmlStr = validateDataByItemReceive(request, response);
			} else if (action.equalsIgnoreCase("process_receiveMaterial")) {
				xmlStr = "";
				xmlStr = process_orderMiscReceiving(request, response);
			} else if (action.equalsIgnoreCase("MiscOrderReceiving")) {
				xmlStr = "";
				xmlStr = process_orderMiscReceivingByWMS(request, response);
			} else if (action.equalsIgnoreCase("MultipleMiscOrderReceiving")) {
				xmlStr = "";
				xmlStr = process_orderMultipleMiscReceivingByWMS(request, response);
			} else if (action.equalsIgnoreCase("MiscOrderReceivingByRange")) {
				xmlStr = "";
				xmlStr = process_orderMiscReceivingByRange(request, response);
			} else if (action.equalsIgnoreCase("Generate Batch")) {
				xmlStr = "";
				xmlStr = process_GenerateBatchByWMS(request, response);
			} else if (action.equalsIgnoreCase("process_InboundOrderGenerateBatch")) {
				xmlStr = "";
				xmlStr = process_InboundOrderGenerateBatch(request, response);
			} else if (action.equalsIgnoreCase("GET_ALL_LOCATION")) {
				xmlStr = "";
				xmlStr = getLocationsFromLocationMaster(request, response);
			} else if (action.equalsIgnoreCase("GenerateTransactionId")) {
				xmlStr = "";
				xmlStr = GenerateTransactionId(request, response);
			}
			else if (action.equalsIgnoreCase("validateReceiveLocation")) {
			xmlStr = "";
			xmlStr = validateReceiveLocation(request, response);
			}
			else if (action.equalsIgnoreCase("load_all_uom")) {
				xmlStr = "";
				xmlStr = load_all_uom(request, response);
				}
			else if (action.equals("load_do_grn")) {
				xmlStr = "";
				xmlStr = generateGRN(request, response);	       	       
		    }
			
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			xmlStr = XMLUtils.getXMLMessage(1, "Unable to process? " + e.getMessage());
		}

		out.write(xmlStr);
		out.close();
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

	public void destroy() {
	}

	private String validateData(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String str = "", aPlant = "", item = "", batchId = "", loc = "", Qty = "", reasonCode = "", aLoginUser = "";
		try {
			ItemMstUtil itemMstUtil = new ItemMstUtil();
			itemMstUtil.setmLogger(mLogger);
			aPlant = StrUtils.fString(request.getParameter("PLANT"));
			aLoginUser = StrUtils.fString(request.getParameter("LOGIN_USER"));
			item = StrUtils.fString(request.getParameter("ITEM"));
			batchId = StrUtils.fString(request.getParameter("BATCH"));
			loc = StrUtils.fString(request.getParameter("LOC"));
			UserLocUtil uslocUtil = new UserLocUtil();
			uslocUtil.setmLogger(mLogger);
			boolean isvalidlocforUser = uslocUtil.isValidLocInLocmstForUser(aPlant, aLoginUser, loc);
			if (isvalidlocforUser) {
				if (item.length() > 0) {
					isvalidlocforUser = itemMstUtil.isValidItemInItemmst(aPlant, item);
				}
			}

			if (isvalidlocforUser) {
				xmlStr = XMLUtils.getXMLMessage(0, "Valid Product");
			} else {
				xmlStr = XMLUtils.getXMLMessage(1, "Not a Valid Product or It is Parent Product");
			}

		} catch (Exception e) {
			throw e;
		}

		return xmlStr;
	}

	private String validateDataPDAMiscReceive(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String str = "", aPlant = "", item = "", batchId = "", loc = "", Qty = "", reasonCode = "", aLoginUser = "";
		try {
			ItemMstUtil itemMstUtil = new ItemMstUtil();
			itemMstUtil.setmLogger(mLogger);
			aPlant = StrUtils.fString(request.getParameter("PLANT"));
			aLoginUser = StrUtils.fString(request.getParameter("LOGIN_USER"));
			item = StrUtils.fString(request.getParameter("ITEM"));
			// batchId = StrUtils.fString(request.getParameter("BATCH"));
			// loc = StrUtils.fString(request.getParameter("LOC"));
			// UserLocUtil uslocUtil = new UserLocUtil();
			// uslocUtil.setmLogger(mLogger);
			boolean isvalidItem = itemMstUtil.isValidItemInItemmstPDAMiscReceive(aPlant, item);
			// boolean isvalidlocforUser = uslocUtil.isValidLocInLocmstForUser(
			// aPlant, aLoginUser, loc);
			/*
			 * if (isvalidlocforUser) { if (item.length() > 0) { isvalidlocforUser =
			 * itemMstUtil.isValidItemInItemmstPDAMiscReceive( aPlant, item); } }
			 */

			if (isvalidItem) {
				xmlStr = XMLUtils.getXMLMessage(0, "Valid Product");
			} else {
				xmlStr = XMLUtils.getXMLMessage(1, "Not a Valid Product or It is Parent Product");
			}

		} catch (Exception e) {
			throw e;
		}

		return xmlStr;
	}

	private String validateDataByItemReceive(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String str = "", aPlant = "", item = "", batchId = "", loc = "", Qty = "", reasonCode = "", aLoginUser = "";
		try {
			ItemMstUtil itemMstUtil = new ItemMstUtil();
			itemMstUtil.setmLogger(mLogger);
			aPlant = StrUtils.fString(request.getParameter("PLANT"));
			aLoginUser = StrUtils.fString(request.getParameter("LOGIN_USER"));
			item = StrUtils.fString(request.getParameter("ITEM"));
			
			UserLocUtil uslocUtil = new UserLocUtil();
			uslocUtil.setmLogger(mLogger);
			boolean isvalidproduct = itemMstUtil.isValidItemInItemmstPDAMiscReceive(aPlant, item);
			
			if (isvalidproduct) {
				xmlStr = XMLUtils.getXMLMessage(0, "Valid Product");
			} else {
				xmlStr = XMLUtils.getXMLMessage(1, "Not a Valid Product");
			}

		} catch (Exception e) {
			throw e;
		}

		return xmlStr;
	}
	
	private String validateReceiveLocation(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String str = "", aPlant = "", item = "", batchId = "", loc = "", Qty = "", reasonCode = "", aLoginUser = "";
		try {
			
			aPlant = StrUtils.fString(request.getParameter("PLANT"));
			aLoginUser = StrUtils.fString(request.getParameter("LOGIN_USER"));
			loc = StrUtils.fString(request.getParameter("LOC"));
			UserLocUtil uslocUtil = new UserLocUtil();
			uslocUtil.setmLogger(mLogger);
			boolean isvalidlocforUser = uslocUtil.isValidLocInLocmstForUser(aPlant, aLoginUser, loc);
			

			if (isvalidlocforUser) {
				xmlStr = XMLUtils.getXMLMessage(0, "Valid Location");
			} else {
				xmlStr = XMLUtils.getXMLMessage(1, "Not a Valid Location");
			}

		} catch (Exception e) {
			throw e;
		}

		return xmlStr;
	}

	private String getLocationsFromLocationMaster(HttpServletRequest request, HttpServletResponse response) {
		String str = "", aPlant = "";
		try {

			aPlant = StrUtils.fString(request.getParameter("PLANT"));
			InvMstUtil _InvMstUtil = new InvMstUtil();
			str = _InvMstUtil.loadLocationsXml2(aPlant);

			if (str.equalsIgnoreCase("")) {
				str = XMLUtils.getXMLMessage(1, "No open outgoing order found ");
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			str = XMLUtils.getXMLMessage(1, e.getMessage());
		}
		return str;
	}

	private String load_Item_Details(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		String str = "", aItemNum = "", aLoginUser = "";
		try {

			PLANT = StrUtils.fString(request.getParameter("PLANT"));
			aItemNum = StrUtils.fString(request.getParameter("ITEM_NUM"));
			aLoginUser = StrUtils.fString(request.getParameter("LOGIN_USER"));

			Hashtable ht = new Hashtable();

			String itemDesc = new ItemMstDAO().getItemDesc(PLANT, aItemNum);
			String uom = new ItemMstDAO().getItemUOM(PLANT, aItemNum);
			xmlStr = XMLUtils.getXMLHeader();
			xmlStr = xmlStr + XMLUtils.getStartNode("itemDetails");

			// xmlStr = xmlStr + XMLUtils.getXMLNode("status", "0");
			// xmlStr = xmlStr + XMLUtils.getXMLNode("description", "");
			xmlStr = xmlStr + XMLUtils.getXMLNode("item", aItemNum);
			xmlStr = xmlStr + XMLUtils.getXMLNode("itemDesc", itemDesc);
			xmlStr = xmlStr + XMLUtils.getXMLNode("uom", uom);

			xmlStr = xmlStr + XMLUtils.getEndNode("itemDetails");

			/*
			 * if (str.equalsIgnoreCase("")) { str = XMLUtils.getXMLMessage(1,
			 * "Product details not found!"); }
			 */
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			xmlStr = xmlStr + XMLUtils.getXMLMessage(1, e.getMessage());
		}

		return xmlStr;
	}

	/*
	 * ************Modification History********************************* Oct 17 2014
	 * Bruhan, Description: To include Receive date
	 */
	private String process_orderMiscReceiving(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, Exception {

		Map receiveMaterial_HM = null;
		RecvDetDAO _RecvDetDAO = new RecvDetDAO();
		String PLANT = "", PO_NUM = "", ITEM_NUM = "", PO_LN_NUM = "";
		String LOGIN_USER = "";
		String ITEM_BATCH = "", ITEM_QTY = "", EXPIREDATE = "", ITEM_EXPDATE = "", ITEM_LOC = "", RSNDESC = "",
				TRANSACTIONID = "", EMPNO = "", UOM = "",INVID="";
		try {
			PLANT = StrUtils.fString(request.getParameter("PLANT"));
			ITEM_NUM = StrUtils.fString(request.getParameter("ITEM_NUM"));
			LOGIN_USER = StrUtils.fString(request.getParameter("LOGIN_USER"));
			EXPIREDATE = StrUtils.fString(request.getParameter("EXPIREDATE"));
			ITEM_BATCH = StrUtils.fString(request.getParameter("ITEM_BATCH"));
			ITEM_QTY = StrUtils.fString(request.getParameter("ITEM_QTY"));
			// ITEM_EXPDATE = StrUtils.fString(request.getParameter("ITEM_EXPDATE"));
			String REMARKS = StrUtils.fString(request.getParameter("REMARKS"));
			ITEM_LOC = StrUtils.fString(request.getParameter("ITEM_LOC"));
			RSNDESC = StrUtils.fString(request.getParameter("RSNDESC"));

			TRANSACTIONID=StrUtils.fString(request.getParameter("ORDERNO"));
			System.out.println("ordrenumber"+TRANSACTIONID);
			EMPNO=StrUtils.fString(request.getParameter("EMPNO"));
			UOM=StrUtils.fString(request.getParameter("UOM"));
			



			RSNDESC = StrUtils.replaceCharacters2Recv(RSNDESC);
			String nonstocktype = new ItemMstDAO().getNonStockFlag(PLANT, ITEM_NUM);
			/*if (nonstocktype.equalsIgnoreCase("Y")) {
				throw new Exception(" " + ITEM_NUM + " is a non stock product, receiving is not allowed ");
			}*/

			if (ITEM_EXPDATE.length() > 8) {
				ITEM_EXPDATE = ITEM_EXPDATE.substring(6, 10) + "-" + ITEM_EXPDATE.substring(3, 5) + "-"
						+ ITEM_EXPDATE.substring(0, 2);
			}
			DateUtils _dateUtils = new DateUtils();
			String curDate = _dateUtils.getDate();
			String strTranDate = curDate.substring(6) + "-" + curDate.substring(3, 5) + "-" + curDate.substring(0, 2);
			String strRecvDate = curDate.substring(0, 2) + "/" + curDate.substring(3, 5) + "/" + curDate.substring(6);

			double itemqty = Double.parseDouble(ITEM_QTY);
			itemqty = StrUtils.RoundDB(itemqty, IConstants.DECIMALPTS);
			ITEM_QTY = String.valueOf(itemqty);
			ITEM_QTY = StrUtils.formatThreeDecimal(ITEM_QTY);
			receiveMaterial_HM = new HashMap();
			receiveMaterial_HM.put(IConstants.PLANT, PLANT);
			receiveMaterial_HM.put(IConstants.ITEM, ITEM_NUM);
			receiveMaterial_HM.put(IConstants.LOC, ITEM_LOC);
			receiveMaterial_HM.put(IConstants.LOGIN_USER, LOGIN_USER);
			receiveMaterial_HM.put(IConstants.INV_BATCH, ITEM_BATCH);
			receiveMaterial_HM.put(IConstants.INV_QTY, ITEM_QTY);
			receiveMaterial_HM.put(IConstants.INV_EXP_DATE, ITEM_EXPDATE);
			receiveMaterial_HM.put(IConstants.RSNDESC, RSNDESC);
			receiveMaterial_HM.put(IDBConstants.REMARKS, REMARKS);
			receiveMaterial_HM.put(IDBConstants.EXPIREDATE, EXPIREDATE);
			receiveMaterial_HM.put(IConstants.TRAN_DATE, strTranDate);
			receiveMaterial_HM.put(IConstants.RECVDATE, strRecvDate);
			receiveMaterial_HM.put(IConstants.PONO, TRANSACTIONID);
			receiveMaterial_HM.put(IConstants.EMPNO, EMPNO);
			receiveMaterial_HM.put(IConstants.ISPDA, "ISPDA");
			receiveMaterial_HM.put(IConstants.UNITMO, UOM);
			receiveMaterial_HM.put(IConstants.NONSTKFLAG, nonstocktype);
			//Get Item Count
			
			POSDetDAO posdetDAO = new POSDetDAO();			
			posdetDAO.setmLogger(mLogger);
			List prdlist = posdetDAO.listProductsForPOSTranID(PLANT, TRANSACTIONID, " (TRANSTATUS <> '')");
			int gcount=prdlist.size()+1;
			receiveMaterial_HM.put(IConstants.DODET_DOLNNO, String.valueOf(gcount));

			String UOMQTY="1";
			Hashtable<String, String> htTrand1 = new Hashtable<String, String>();
			MovHisDAO movHisDao1 = new MovHisDAO();
			ArrayList getuomqty = movHisDao1.selectForReport("select ISNULL(QPUOM,1) as UOMQTY from "+PLANT+"_UOM where UOM='"+UOM+"'",htTrand1);
			if(getuomqty.size()>0)
			{
			Map mapval = (Map) getuomqty.get(0);
			UOMQTY=(String)mapval.get("UOMQTY");
			}
			receiveMaterial_HM.put("UOMQTY", UOMQTY);

			xmlStr = "";
			xmlStr = _MiscReceivingUtil.process_MiscReceiveMaterial(receiveMaterial_HM);
			
			
			if(xmlStr.contains("received successfully!"))
			{
				
				boolean flag = true;
				flag=_RecvDetDAO.isExisit("select count(*) from "+PLANT+"_FINGRNOTOBILL where  GRNO='"+TRANSACTIONID+"'");
				if (flag) {
					MovHisDAO movHisDao = new MovHisDAO();
					Hashtable htRecvDet = new Hashtable();
					htRecvDet.clear();
					htRecvDet.put(IConstants.PLANT, PLANT);
					htRecvDet.put(IConstants.GRNO, TRANSACTIONID);
					
					
					flag=_RecvDetDAO.updateGRNtoBill(" set QTY+="+ITEM_QTY,htRecvDet,"",PLANT);
        			
					Hashtable htRecvHis = new Hashtable();
        			htRecvHis.clear();
        			htRecvHis.put(IDBConstants.PLANT, PLANT);
        			htRecvHis.put("DIRTYPE", TransactionConstants.CREATE_GRN);
        			        					
        			htRecvHis.put(IDBConstants.MOVHIS_ORDNUM, TRANSACTIONID);
					flag=movHisDao.updateMovHis(" set QTY+="+ITEM_QTY,htRecvHis,"",PLANT);
					
				}else{
			new TblControlUtil().updateTblControlIESeqNo(PLANT,"GRN","GN",TRANSACTIONID);
			DateUtils dateutils = new DateUtils();
			Hashtable htRecvDet = new Hashtable();
			htRecvDet.clear();
			htRecvDet.put(IConstants.PLANT, PLANT);
			htRecvDet.put(IConstants.VENDOR_CODE,"");
			htRecvDet.put(IConstants.GRNO, TRANSACTIONID);                    
			htRecvDet.put(IConstants.PODET_PONUM, "");
			htRecvDet.put(IConstants.STATUS, "NON BILLABLE");
			htRecvDet.put(IConstants.AMOUNT, "");
			htRecvDet.put(IConstants.QTY, ITEM_QTY);
			htRecvDet.put("CRAT",dateutils.getDateTime());
			htRecvDet.put("CRBY",LOGIN_USER);
			htRecvDet.put("UPAT",dateutils.getDateTime());
			 _RecvDetDAO.insertGRNtoBill(htRecvDet);
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
			htRecvHis.put(IConstants.QTY, ITEM_QTY);
			htRecvHis.put(IDBConstants.CREATED_BY, LOGIN_USER);
			htRecvHis.put(IDBConstants.REMARKS, "");        					
			htRecvHis.put(IDBConstants.TRAN_DATE, dateutils.getDateinyyyy_mm_dd(dateutils.getDate()));
			htRecvHis.put(IDBConstants.MOVHIS_ORDNUM, TRANSACTIONID);
			htRecvHis.put(IDBConstants.CREATED_AT, dateutils.getDateTime());
		    movHisDao.insertIntoMovHis(htRecvHis);
			}
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			xmlStr = XMLUtils.getXMLMessage(0, e.getMessage());
		}

		return xmlStr;
	}

	private String process_orderMiscReceivingByWMS(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, Exception {

		Map receiveMaterial_HM = null;
		String PLANT = "", PO_NUM = "", ITEM_NUM = "", PO_LN_NUM = "";
		String ITEM_DESCRIPTION = "", LOGIN_USER = "", REMARKS = "";
		String ITEM_BATCH = "", ITEM_QTY = "", ITEM_EXPDATE = "", ITEM_LOC = "", RSNDESC = "", EXPIREDATE = "";
		try {
			HttpSession session = request.getSession();
			PLANT = StrUtils.fString((String) session.getAttribute("PLANT"));
			ITEM_NUM = StrUtils.fString(request.getParameter("ITEMNO"));
			LOGIN_USER = StrUtils.fString(request.getParameter("LOGINUSER"));

			ITEM_BATCH = StrUtils.fString(request.getParameter("BATCH"));
			ITEM_QTY = StrUtils.fString(request.getParameter("QTY"));
			double itemqty = Double.parseDouble(ITEM_QTY);
			itemqty = StrUtils.RoundDB(itemqty, IConstants.DECIMALPTS);
			ITEM_QTY = String.valueOf(itemqty);
			ITEM_EXPDATE = StrUtils.fString(request.getParameter("EXPIREDATE"));

			ITEM_LOC = StrUtils.fString(request.getParameter("LOC"));
			RSNDESC = StrUtils.fString(request.getParameter("REASONCODE"));
			REMARKS = StrUtils.fString(request.getParameter("REFDET"));
			EXPIREDATE = StrUtils.fString(request.getParameter("EXPIREDATE"));
			if (ITEM_EXPDATE.length() > 8) {
				ITEM_EXPDATE = ITEM_EXPDATE.substring(6, 10) + "-" + ITEM_EXPDATE.substring(3, 5) + "-"
						+ ITEM_EXPDATE.substring(0, 2);
			}
			ItemMstUtil itemMstUtil = new ItemMstUtil();
			itemMstUtil.setmLogger(mLogger);
			ITEM_NUM = itemMstUtil.isValidAlternateItemInItemmst(PLANT, ITEM_NUM);
			receiveMaterial_HM = new HashMap();
			receiveMaterial_HM.put(IConstants.PLANT, PLANT);
			receiveMaterial_HM.put(IConstants.ITEM, ITEM_NUM);
			receiveMaterial_HM.put(IConstants.PODET_PONUM, PO_NUM);
			receiveMaterial_HM.put(IConstants.PODET_POLNNO, PO_LN_NUM);
			receiveMaterial_HM.put(IConstants.LOC, ITEM_LOC);
			receiveMaterial_HM.put(IConstants.LOGIN_USER, LOGIN_USER);
			receiveMaterial_HM.put(IConstants.CUSTOMER_CODE, _POUtil.getCustCode(PLANT, PO_NUM));
			receiveMaterial_HM.put(IConstants.INV_BATCH, ITEM_BATCH);
			receiveMaterial_HM.put(IConstants.INV_QTY, ITEM_QTY);
			receiveMaterial_HM.put(IConstants.INV_EXP_DATE, ITEM_EXPDATE);
			receiveMaterial_HM.put(IConstants.RSNDESC, RSNDESC);
			receiveMaterial_HM.put(IConstants.JOB_NUM, _POUtil.getJobNum(PLANT, PO_NUM));
			receiveMaterial_HM.put(IConstants.REMARKS, REMARKS);
			receiveMaterial_HM.put(IConstants.EXPIREDATE, EXPIREDATE);
			xmlStr = "";
			Boolean isProductAvalable = Boolean.valueOf(true);

			if (isProductAvalable) {

				UserLocUtil uslocUtil = new UserLocUtil();
				uslocUtil.setmLogger(mLogger);
				boolean isvalidlocforUser = uslocUtil.isValidLocInLocmstForUser(PLANT, LOGIN_USER, ITEM_LOC);
				if (!isvalidlocforUser) {
					throw new Exception(" Loc :" + ITEM_LOC + " is not User Assigned Location/valid location");
				}
				RsnMst rsnMst = new RsnMst();
				rsnMst.setmLogger(mLogger);
				Hashtable htRsnMst = new Hashtable();
				htRsnMst.put(IConstants.PLANT, PLANT);
				htRsnMst.put(IConstants.RSNCODE, RSNDESC);
				if (!rsnMst.isExists(htRsnMst)) {
					throw new Exception("Invalid Reason Code!");
				}

				xmlStr = _MiscReceivingUtil.process_MiscReceiveMaterial(receiveMaterial_HM);

				request.getSession().setAttribute("RESULT", "Product : " + ITEM_NUM + " " + "received successfully!");
				response.sendRedirect("jsp/MiscOrderReceiving.jsp?action=result");
			} else {
				request.getSession().setAttribute("RESULTERROR",
						"Error in receiving Mis Order Receiving Item:" + ITEM_NUM + " " + "Error: Invalid Product");
				response.sendRedirect("jsp/MiscOrderReceiving.jsp?action=resulterror");
			}

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			request.getSession().setAttribute("RESULTERROR", "Error:" + e.getMessage());
			response.sendRedirect("jsp/MiscOrderReceiving.jsp?action=resulterror");

			throw e;
		}

		return xmlStr;
	}

	/*
	 * ************Modification History********************************* Oct 13 2014
	 * Bruhan, Description: To include Receive date
	 */
	private String process_orderMultipleMiscReceivingByWMS(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, Exception {

		Map receiveMaterial_HM = null;
		UserTransaction ut = null;
		String PLANT = "", PO_NUM = "", ITEM_NUM = "", PO_LN_NUM = "";
		String ITEM_DESCRIPTION = "", LOGIN_USER = "", REMARKS = "";
		String ITEM_BATCH = "", ITEM_QTY = "", ITEM_EXPDATE = "", ITEM_LOC = "", RSNDESC = "", EXPIREDATE = "",
				TRANSACTIONDATE = "", strMovHisTranDate = "", strTranDate = "";
		try {
			HttpSession session = request.getSession();
			PLANT = StrUtils.fString((String) session.getAttribute("PLANT"));

			LOGIN_USER = StrUtils.fString(request.getParameter("LOGINUSER"));
			String DYNAMIC_RECEIVING_SIZE = StrUtils.fString(request.getParameter("DYNAMIC_RECEIVING_SIZE"));
			Boolean transactionHandler = true;
			ut = DbBean.getUserTranaction();
			ut.begin();
			int receving_count = Integer.parseInt(DYNAMIC_RECEIVING_SIZE);
			for (int index = 0; index < receving_count; index++) {

				ITEM_NUM = StrUtils.fString(request.getParameter("ITEMNO" + "_" + index));
				String nonstocktype = new ItemMstDAO().getNonStockFlag(PLANT, ITEM_NUM);
				/*
				 * if(nonstocktype.equalsIgnoreCase("Y")){ throw new Exception (" "+ITEM_NUM+
				 * " is a non stock product, cannot be received "); }
				 */
				ITEM_BATCH = StrUtils.fString(request.getParameter("BATCH" + "_" + index));
				ITEM_QTY = StrUtils.fString(request.getParameter("QTY" + "_" + index));
				double itemqty = Double.parseDouble(ITEM_QTY);
				itemqty = strUtils.RoundDB(itemqty, IConstants.DECIMALPTS);
				ITEM_QTY = String.valueOf(itemqty);
				ITEM_EXPDATE = StrUtils.fString(request.getParameter("EXPIREDATE" + "_" + index));

				ITEM_LOC = StrUtils.fString(request.getParameter("LOC" + "_" + index));
				RSNDESC = StrUtils.fString(request.getParameter("REASONCODE" + "_" + index));
				REMARKS = StrUtils.fString(request.getParameter("REFDET" + "_" + index));
				TRANSACTIONDATE = StrUtils.fString(request.getParameter("TRANSACTIONDATE"));
				if (TRANSACTIONDATE.length() > 5)
					strMovHisTranDate = TRANSACTIONDATE.substring(6) + "-" + TRANSACTIONDATE.substring(3, 5) + "-"
							+ TRANSACTIONDATE.substring(0, 2);
				strTranDate = TRANSACTIONDATE.substring(0, 2) + "/" + TRANSACTIONDATE.substring(3, 5) + "/"
						+ TRANSACTIONDATE.substring(6);
				EXPIREDATE = StrUtils.fString(request.getParameter("EXPIREDATE" + "_" + index));
				if (ITEM_EXPDATE.length() > 8) {
					ITEM_EXPDATE = ITEM_EXPDATE.substring(6, 10) + "-" + ITEM_EXPDATE.substring(3, 5) + "-"
							+ ITEM_EXPDATE.substring(0, 2);
				}
				ItemMstUtil itemMstUtil = new ItemMstUtil();
				itemMstUtil.setmLogger(mLogger);
				ITEM_NUM = itemMstUtil.isValidAlternateItemInItemmst(PLANT, ITEM_NUM);
				receiveMaterial_HM = new HashMap();
				receiveMaterial_HM.put(IConstants.PLANT, PLANT);
				receiveMaterial_HM.put(IConstants.ITEM, ITEM_NUM);
				receiveMaterial_HM.put(IConstants.PODET_PONUM, PO_NUM);
				receiveMaterial_HM.put(IConstants.PODET_POLNNO, PO_LN_NUM);
				receiveMaterial_HM.put(IConstants.LOC, ITEM_LOC);
				receiveMaterial_HM.put(IConstants.LOGIN_USER, LOGIN_USER);
				receiveMaterial_HM.put(IConstants.CUSTOMER_CODE, _POUtil.getCustCode(PLANT, PO_NUM));
				receiveMaterial_HM.put(IConstants.INV_BATCH, ITEM_BATCH);
				receiveMaterial_HM.put(IConstants.INV_QTY, ITEM_QTY);
				receiveMaterial_HM.put(IConstants.INV_EXP_DATE, ITEM_EXPDATE);
				receiveMaterial_HM.put(IConstants.RSNDESC, RSNDESC);
				receiveMaterial_HM.put(IConstants.JOB_NUM, _POUtil.getJobNum(PLANT, PO_NUM));
				receiveMaterial_HM.put(IConstants.REMARKS, REMARKS);
				receiveMaterial_HM.put(IConstants.EXPIREDATE, EXPIREDATE);
				receiveMaterial_HM.put(IConstants.TRAN_DATE, strMovHisTranDate);
				receiveMaterial_HM.put(IConstants.RECVDATE, strTranDate);
				receiveMaterial_HM.put("NONSTKFLAG", nonstocktype);
				xmlStr = "";
				Boolean isProductAvalable = Boolean.valueOf(true);

				if (isProductAvalable) {

					UserLocUtil uslocUtil = new UserLocUtil();
					uslocUtil.setmLogger(mLogger);
					boolean isvalidlocforUser = uslocUtil.isValidLocInLocmstForUser(PLANT, LOGIN_USER, ITEM_LOC);
					if (!isvalidlocforUser) {
						throw new Exception(" Loc :" + ITEM_LOC + " is not User Assigned Location/valid location");
					}
					RsnMst rsnMst = new RsnMst();
					rsnMst.setmLogger(mLogger);
					Hashtable htRsnMst = new Hashtable();
					htRsnMst.put(IConstants.PLANT, PLANT);
					htRsnMst.put(IConstants.RSNCODE, RSNDESC);
					if (!rsnMst.isExists(htRsnMst)) {
						throw new Exception("Invalid Reason Code!");
					}

					transactionHandler = _MiscReceivingUtil.process_MultiMiscReceiveMaterial(receiveMaterial_HM);

				} else {
					transactionHandler = transactionHandler && false;

				}
			}
			if (transactionHandler == true) {
				DbBean.CommitTran(ut);
				request.getSession().setAttribute("RESULT", "Products : " + " " + "Received successfully!");
				response.sendRedirect("jsp/MultiMiscOrderReceiving.jsp?action=result");
			} else {
				DbBean.RollbackTran(ut);
				request.getSession().setAttribute("RESULTERROR",
						"Error in receiving Mis Order Receiving Item:" + " " + "Error: Invalid Product");
				response.sendRedirect("jsp/MultiMiscOrderReceiving.jsp?action=resulterror");
			}

		} catch (Exception e) {
			DbBean.RollbackTran(ut);
			this.mLogger.exception(this.printLog, "", e);
			request.getSession().setAttribute("RESULTERROR", "Error:" + e.getMessage());
			response.sendRedirect("jsp/MultiMiscOrderReceiving.jsp?action=resulterror");

			throw e;
		}

		return xmlStr;
	}

	/*
	 * ************Modification History********************************* Oct 13 2014
	 * Bruhan, Description: To include Transaction date
	 */
	private String process_orderMiscReceivingByRange(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, Exception {

		Map receiveMaterial_HM = null;
		String PLANT = "", TransId = "", ITEM_NUM = "", PO_LN_NUM = "", SUFFIX = "", DTFRMT = "", SRANGE = "",
				ERANGE = "";
		String ITEM_DESCRIPTION = "", LOGIN_USER = "", REMARKS = "", TRANSACTIONDATE = "", strMovHisTranDate = "",
				strTranDate = "";
		String ITEM_BATCH = "", ITEM_QTY = "", ITEM_EXPDATE = "", ITEM_LOC = "", RSNDESC = "", EXPIREDATE = "";
		double pickingQty = 0,totalqty=0;
		// UserTransaction ut = null;
		try {

			HttpSession session = request.getSession();
			PLANT = StrUtils.fString((String) session.getAttribute("PLANT"));
			ITEM_NUM = StrUtils.fString(request.getParameter("ITEMNO"));
			String nonstocktype = new ItemMstDAO().getNonStockFlag(PLANT, ITEM_NUM);
			/*
			 * if(nonstocktype.equalsIgnoreCase("Y")){ throw new Exception (" "+ITEM_NUM+
			 * " is a non stock product, cannot be received "); }
			 */
			LOGIN_USER = StrUtils.fString(request.getParameter("LOGINUSER"));
			TransId = StrUtils.fString(request.getParameter("RECVTRANID"));
			SUFFIX = StrUtils.fString(request.getParameter("SUFFIX"));
			DTFRMT = StrUtils.fString(request.getParameter("DTFRMT"));
			SRANGE = StrUtils.fString(request.getParameter("SRANGE"));
			ERANGE = StrUtils.fString(request.getParameter("ERANGE"));
			long rangeSize = Long.parseLong(ERANGE);

			ITEM_QTY = StrUtils.fString(request.getParameter("QTY"));
			ITEM_EXPDATE = StrUtils.fString(request.getParameter("EXPIREDATE"));

			ITEM_LOC = StrUtils.fString(request.getParameter("LOC"));
			RSNDESC = StrUtils.fString(request.getParameter("REASONCODE"));
			REMARKS = StrUtils.fString(request.getParameter("REFDET"));
			String UOM = StrUtils.fString(request.getParameter("UOM"));
			TRANSACTIONDATE = StrUtils.fString(request.getParameter("TRANSACTIONDATE"));
			if (TRANSACTIONDATE.length() > 5)
				strMovHisTranDate = TRANSACTIONDATE.substring(6) + "-" + TRANSACTIONDATE.substring(3, 5) + "-"
						+ TRANSACTIONDATE.substring(0, 2);
			strTranDate = TRANSACTIONDATE.substring(0, 2) + "/" + TRANSACTIONDATE.substring(3, 5) + "/"
					+ TRANSACTIONDATE.substring(6);
			EXPIREDATE = StrUtils.fString(request.getParameter("EXPIREDATE"));
			if (ITEM_EXPDATE.length() > 8) {
				ITEM_EXPDATE = ITEM_EXPDATE.substring(6, 10) + "-" + ITEM_EXPDATE.substring(3, 5) + "-"
						+ ITEM_EXPDATE.substring(0, 2);
			}
			ItemMstUtil itemMstUtil = new ItemMstUtil();
			itemMstUtil.setmLogger(mLogger);
			ITEM_NUM = itemMstUtil.isValidAlternateItemInItemmst(PLANT, ITEM_NUM);

			UserLocUtil uslocUtil = new UserLocUtil();
			uslocUtil.setmLogger(mLogger);
			boolean isvalidlocforUser = uslocUtil.isValidLocInLocmstForUser(PLANT, LOGIN_USER, ITEM_LOC);
			if (!isvalidlocforUser) {
				throw new Exception(" Loc :" + ITEM_LOC + " is not User Assigned Location/valid location");
			}
			RsnMst rsnMst = new RsnMst();
			rsnMst.setmLogger(mLogger);
			Hashtable htRsnMst = new Hashtable();
			htRsnMst.put(IConstants.PLANT, PLANT);
			htRsnMst.put(IConstants.RSNCODE, RSNDESC);
			if (!rsnMst.isExists(htRsnMst)) {
				throw new Exception("Invalid Reason Code!");
			}
			xmlStr = "";
			Boolean transactionHandler = false;
			// ut = DbBean.getUserTranaction();
			// ut.begin();
			long SerialNo = 0;
			com.track.dao.TblControlDAO _TblControlDAO = new com.track.dao.TblControlDAO();
			_TblControlDAO.setmLogger(mLogger);
			/*PO_NUM = _TblControlDAO.getNextOrder(PLANT, LOGIN_USER, "GRRECEIPT");*/
			for (int i = 0; i < rangeSize; i++) {
				SerialNo = Long.parseLong(SRANGE) + i;
				ITEM_BATCH = SUFFIX + DTFRMT + Long.toString(SerialNo);

				Hashtable htInvnatch = new Hashtable();
				htInvnatch.put(IConstants.PLANT, PLANT);
				htInvnatch.put(IDBConstants.USERFLD4, ITEM_BATCH);
				htInvnatch.put(IDBConstants.ITEM, ITEM_NUM);

				boolean isExistsBatch = new InvMstDAO().isExisit(htInvnatch, " qty <> 0 ");
				if (isExistsBatch) {
					throw new Exception(" Batch No :" + ITEM_BATCH
							+ " Generated  already Exists to Receive for the Product :" + ITEM_NUM);
				}
				pickingQty = Double.parseDouble(((String) ITEM_QTY.trim().toString()));
				totalqty=totalqty+pickingQty;
				
				receiveMaterial_HM = new HashMap();
				receiveMaterial_HM.put(IConstants.PLANT, PLANT);
				receiveMaterial_HM.put(IConstants.ITEM, ITEM_NUM);
				receiveMaterial_HM.put(IConstants.PODET_PONUM, TransId);
				receiveMaterial_HM.put(IConstants.PODET_POLNNO, PO_LN_NUM);
				receiveMaterial_HM.put(IConstants.LOC, ITEM_LOC);
				receiveMaterial_HM.put(IConstants.LOGIN_USER, LOGIN_USER);
				receiveMaterial_HM.put(IConstants.CUSTOMER_CODE, _POUtil.getCustCode(PLANT, TransId));
				receiveMaterial_HM.put(IConstants.INV_BATCH, ITEM_BATCH);
				receiveMaterial_HM.put(IConstants.INV_QTY, ITEM_QTY);
				receiveMaterial_HM.put(IConstants.INV_EXP_DATE, ITEM_EXPDATE);
				receiveMaterial_HM.put(IConstants.RSNDESC, RSNDESC);
				receiveMaterial_HM.put(IConstants.JOB_NUM, _POUtil.getJobNum(PLANT, TransId));
				receiveMaterial_HM.put(IConstants.REMARKS, REMARKS);
				receiveMaterial_HM.put(IConstants.EXPIREDATE, EXPIREDATE);
				receiveMaterial_HM.put(IConstants.TRAN_DATE, strMovHisTranDate);
				receiveMaterial_HM.put(IConstants.RECVDATE, strTranDate);
				receiveMaterial_HM.put(IConstants.EMPNO, "");// TODO:Check with Bruhan. Input is not there in the page Goods Receipt (By Range)
				receiveMaterial_HM.put("NONSTKFLAG", nonstocktype);
				receiveMaterial_HM.put(IConstants.ISPDA, "ISPDA");
				receiveMaterial_HM.put(IConstants.UNITMO, UOM);
				String lnno =String.valueOf((i+1));
				receiveMaterial_HM.put(IConstants.DODET_DOLNNO, lnno);
				String UOMQTY="1";
				Hashtable<String, String> htTrand1 = new Hashtable<String, String>();
				MovHisDAO movHisDao1 = new MovHisDAO();
				ArrayList getuomqty = movHisDao1.selectForReport("select ISNULL(QPUOM,1) as UOMQTY from "+PLANT+"_UOM where UOM='"+UOM+"'",htTrand1);
				if(getuomqty.size()>0)
				{
				Map mapval = (Map) getuomqty.get(0);
				UOMQTY=(String)mapval.get("UOMQTY");
				}
				receiveMaterial_HM.put("UOMQTY", UOMQTY);
				transactionHandler = _MiscReceivingUtil.process_MiscReceiveMaterialByRange(receiveMaterial_HM);
				
				if (transactionHandler == true) {//Shopify Inventory Update
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
   								String result="";
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
			
			if (transactionHandler) {
				RecvDetDAO _RecvDetDAO = new RecvDetDAO();
				DateUtils dateutils = new DateUtils();
				Hashtable htRecvDet = new Hashtable();
				htRecvDet.clear();
				htRecvDet.put(IConstants.PLANT, PLANT);
				htRecvDet.put(IConstants.VENDOR_CODE,"");
				htRecvDet.put(IConstants.GRNO, TransId);                    
				htRecvDet.put(IConstants.PODET_PONUM, "");
				htRecvDet.put(IConstants.STATUS, "NON BILLABLE");
				htRecvDet.put(IConstants.AMOUNT, "");
				htRecvDet.put(IConstants.QTY, String.valueOf(totalqty));
				htRecvDet.put("CRAT",dateutils.getDateTime());
				htRecvDet.put("CRBY",LOGIN_USER);
				htRecvDet.put("UPAT",dateutils.getDateTime());
				transactionHandler = _RecvDetDAO.insertGRNtoBill(htRecvDet);
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
    			htRecvHis.put(IDBConstants.REMARKS, "");        					
    			htRecvHis.put(IDBConstants.TRAN_DATE, dateutils.getDateinyyyy_mm_dd(dateutils.getDate()));
    			htRecvHis.put(IDBConstants.MOVHIS_ORDNUM, TransId);
    			htRecvHis.put(IDBConstants.CREATED_AT, dateutils.getDateTime());
    			transactionHandler = movHisDao.insertIntoMovHis(htRecvHis);
				_TblControlDAO.setmLogger(mLogger);
				try {
					int nxtSeq = Integer.parseInt(SRANGE) + Integer.parseInt(ERANGE);
					Hashtable htTblCntUpdate = new Hashtable();
					htTblCntUpdate.put(IDBConstants.PLANT, PLANT);
					htTblCntUpdate.put(IDBConstants.TBL_FUNCTION, "RECV_BY_RANGE");
					htTblCntUpdate.put(IDBConstants.TBL_PREFIX1, "");
					StringBuffer updateQyery = new StringBuffer("set ");
					updateQyery.append(IDBConstants.TBL_NEXT_SEQ + " = '" + nxtSeq + "'");
					transactionHandler = _TblControlDAO.update(updateQyery.toString(), htTblCntUpdate, "", PLANT);

				} catch (Exception e) {

					this.mLogger.exception(this.printLog, "", e);
					throw e;

				}
			}
			if (transactionHandler) {
				// DbBean.CommitTran(ut);
				request.getSession().setAttribute("RESULT", "Product : " + ITEM_NUM + " " + "received successfully!");
				response.sendRedirect("../purchasetransaction/goodsreceiptserial?action=result");
			} else {
				throw new Exception("Error in receiving Mis Order Receiving");

			}

		} catch (Exception e) {
			// DbBean.RollbackTran(ut);
			this.mLogger.exception(this.printLog, "", e);
			request.getSession().setAttribute("RESULTERROR", "Error:" + e.getMessage());
			response.sendRedirect("../purchasetransaction/goodsreceiptserial?action=resulterror");

			throw e;
		}

		return xmlStr;
	}

	private String process_GenerateBatchByWMS(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException, Exception {

		Map receiveMaterial_HM = null;
		String PLANT = "", PO_NUM = "", ITEM_NUM = "", PO_LN_NUM = "";
		String ITEM_DESCRIPTION = "", LOGIN_USER = "";
		String ITEM_BATCH = "", CUST_NAME = "", QTY = "", ITEM_QTY = "", RECEIVEDQTY = "", BALANCEQTY = "",
				ITEM_EXPDATE = "", ITEM_LOC = "", BATCHDATE = "";

		boolean flag = false;
		boolean updateFlag = false;
		boolean insertFlag = false;
		boolean resultflag = false;
		String sBatchSeq = "";
		String extCond = "";
		String rtnBatch = "";
		String sZero = "";

		HttpSession session = request.getSession();
		PLANT = StrUtils.fString((String) session.getAttribute("PLANT"));
		ITEM_NUM = StrUtils.fString(request.getParameter("ITEMNO"));
		ITEM_DESCRIPTION = StrUtils.fString(request.getParameter("ITEMDESC"));
		ITEM_LOC = StrUtils.fString(request.getParameter("LOC"));
		ITEM_BATCH = StrUtils.fString(request.getParameter("BATCH"));
		LOGIN_USER = StrUtils.fString(request.getParameter("LOGIN_USER"));

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

				rtnBatch = BATCHDATE.substring(0, 6) + "-" + "0000" + IDBConstants.TBL_FIRST_NEX_SEQ;
				Map htInsert = null;
				Hashtable htTblCntInsert = new Hashtable();

				htTblCntInsert.put(IDBConstants.PLANT, (String) PLANT);

				htTblCntInsert.put(IDBConstants.TBL_FUNCTION, (String) IDBConstants.TBL_BATCH_CAPTION);
				htTblCntInsert.put(IDBConstants.TBL_PREFIX1, (String) BATCHDATE.substring(0, 6));
				htTblCntInsert.put(IDBConstants.TBL_NEXT_SEQ, (String) IDBConstants.TBL_FIRST_NEX_SEQ);
				htTblCntInsert.put(IDBConstants.CREATED_BY, (String) LOGIN_USER);
				htTblCntInsert.put(IDBConstants.CREATED_AT, (String) new DateUtils().getDateTime());
				insertFlag = _TblControlDAO.insertTblControl(htTblCntInsert, PLANT);

				MovHisDAO mdao = new MovHisDAO(PLANT);
				mdao.setmLogger(mLogger);
				Hashtable htm = new Hashtable();
				htm.put("PLANT", (String) PLANT);
				htm.put("DIRTYPE", "MISC GENERATE_BATCH");
				htm.put("RECID", "");
				htm.put("CRBY", (String) LOGIN_USER);
				htm.put("CRAT", (String) new DateUtils().getDateTime());
				boolean inserted = mdao.insertIntoMovHis(htm);
				if (insertFlag) {
					resultflag = true;
				} else if (!insertFlag) {

					throw new Exception("Generate BatchNumber Failed, Error In Inserting Table:" + " " + PLANT + "_ "
							+ "TBLCONTROL");
				} else if (inserted) {
					resultflag = true;
				} else if (!inserted) {

					throw new Exception(
							"Generate BatchNumber Failed,  Error In Inserting Table:" + " " + PLANT + "_ " + "MOVHIS");
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

				int inxtSeq = Integer.parseInt(((String) sBatchSeq.trim().toString())) + 1;

				String updatedSeq = Integer.toString(inxtSeq);
				rtnBatch = BATCHDATE.substring(0, 6) + "-" + sZero + updatedSeq;

				Map htUpdate = null;

				Hashtable htTblCntUpdate = new Hashtable();
				htTblCntUpdate.put(IDBConstants.PLANT, PLANT);
				htTblCntUpdate.put(IDBConstants.TBL_FUNCTION, IDBConstants.TBL_BATCH_CAPTION);
				htTblCntUpdate.put(IDBConstants.TBL_PREFIX1, BATCHDATE.substring(0, 6));
				StringBuffer updateQyery = new StringBuffer("set ");
				updateQyery.append(IDBConstants.TBL_NEXT_SEQ + " = '" + (String) updatedSeq.toString() + "'");

				updateFlag = _TblControlDAO.update(updateQyery.toString(), htTblCntUpdate, extCond, PLANT);

				MovHisDAO mdao = new MovHisDAO(PLANT);
				mdao.setmLogger(mLogger);
				Hashtable htm = new Hashtable();
				htm.put("PLANT", (String) PLANT);
				htm.put("DIRTYPE", "UPDATE_BATCH");
				htm.put("RECID", "");
				htm.put("CRBY", (String) LOGIN_USER);
				htm.put("CRAT", (String) new DateUtils().getDateTime());
				boolean inserted = mdao.insertIntoMovHis(htm);

				if (updateFlag) {
					resultflag = true;
				} else if (!updateFlag) {

					throw new Exception("Update BatchNumber Failed, Error In Misc Updating Table:" + " " + PLANT + "_ "
							+ "TBLCONTROL");
				} else if (inserted) {
					resultflag = true;
				}

				else if (!inserted) {

					throw new Exception("Update BatchNumber Failed,  Error In Misc  Inserting Table:" + " " + PLANT
							+ "_ " + "MOVHIS");
				}

			}

			if (resultflag) {

				request.getSession().setAttribute("BATCHRESULT", "");
				response.sendRedirect("jsp/MiscOrderReceiving.jsp?action=batchresult&ITEMNO=" + ITEM_NUM + "&ITEMDESC="
						+ strUtils.replaceCharacters2Send(ITEM_DESCRIPTION) + "&LOC=" + ITEM_LOC + "&BATCH="
						+ rtnBatch);
			} else {
				request.getSession().setAttribute("BATCHERROR", "Error In Misc Batch Generation");
				response.sendRedirect("jsp/MiscOrderReceiving.jsp?action=batcherror&ITEMNO=" + ITEM_NUM + "&ITEMDESC="
						+ strUtils.replaceCharacters2Send(ITEM_DESCRIPTION) + "&LOC=" + ITEM_LOC + "&BATCH="
						+ ITEM_BATCH);

			}

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			request.getSession().setAttribute("CATCHBATCHERROR", "Batch Generation Error:" + e.getMessage());
			response.sendRedirect("jsp/MiscOrderReceiving.jsp?action=catchbatcherror&ITEMNO=" + ITEM_NUM + "&ITEMDESC="
					+ strUtils.replaceCharacters2Send(ITEM_DESCRIPTION) + "&LOC=" + ITEM_LOC + "&BATCH=" + ITEM_BATCH);

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
				requestParams.append("[" + key + " : " + request.getParameter(key) + "] ");
			}
			this.mLogger.auditInfo(this.printInfo, requestParams.toString());

		} catch (Exception e) {

		}

	}

	public HashMap<String, String> populateMapData(String companyCode, String userCode) {
		HashMap<String, String> loggerDetailsHasMap = new HashMap<String, String>();
		loggerDetailsHasMap.put(MLogger.COMPANY_CODE, companyCode);
		loggerDetailsHasMap.put(MLogger.USER_CODE, userCode);
		return loggerDetailsHasMap;

	}

	private String process_InboundOrderGenerateBatch(HttpServletRequest request, HttpServletResponse response)
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

				rtnBatch = BATCHDATE.substring(0, 6) + "-" + "0000" + IDBConstants.TBL_FIRST_NEX_SEQ;
				Map htInsert = null;
				Hashtable htTblCntInsert = new Hashtable();

				htTblCntInsert.put(IDBConstants.PLANT, (String) PLANT);

				htTblCntInsert.put(IDBConstants.TBL_FUNCTION, (String) IDBConstants.TBL_BATCH_CAPTION);
				htTblCntInsert.put(IDBConstants.TBL_PREFIX1, (String) BATCHDATE.substring(0, 6));
				htTblCntInsert.put(IDBConstants.TBL_NEXT_SEQ, (String) IDBConstants.TBL_FIRST_NEX_SEQ);
				htTblCntInsert.put(IDBConstants.CREATED_BY, (String) LOGIN_USER);
				htTblCntInsert.put(IDBConstants.CREATED_AT, (String) new DateUtils().getDateTime());
				insertFlag = _TblControlDAO.insertTblControl(htTblCntInsert, PLANT);

				MovHisDAO mdao = new MovHisDAO(PLANT);
				mdao.setmLogger(mLogger);
				Hashtable htm = new Hashtable();
				htm.put("PLANT", (String) PLANT);
				htm.put("DIRTYPE", TransactionConstants.GENERATE_BATCH);
				htm.put("RECID", "");
				htm.put("CRBY", (String) LOGIN_USER);
				htm.put("CRAT", (String) new DateUtils().getDateTime());
				boolean inserted = mdao.insertIntoMovHis(htm);
				if (insertFlag) {
					resultflag = true;
				} else if (!insertFlag) {

					throw new Exception("Generate BatchNumber Failed, Error In Inserting Table:" + " " + PLANT + "_ "
							+ "TBLCONTROL");
				} else if (inserted) {
					resultflag = true;
				} else if (!inserted) {

					throw new Exception(
							"Generate BatchNumber Failed,  Error In Inserting Table:" + " " + PLANT + "_ " + "MOVHIS");
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

				int inxtSeq = Integer.parseInt(((String) sBatchSeq.trim().toString())) + 1;

				String updatedSeq = Integer.toString(inxtSeq);
				rtnBatch = BATCHDATE.substring(0, 6) + "-" + sZero + updatedSeq;
				Map htUpdate = null;

				Hashtable htTblCntUpdate = new Hashtable();
				htTblCntUpdate.put(IDBConstants.PLANT, PLANT);
				htTblCntUpdate.put(IDBConstants.TBL_FUNCTION, IDBConstants.TBL_BATCH_CAPTION);
				htTblCntUpdate.put(IDBConstants.TBL_PREFIX1, BATCHDATE.substring(0, 6));
				StringBuffer updateQyery = new StringBuffer("set ");
				updateQyery.append(IDBConstants.TBL_NEXT_SEQ + " = '" + (String) updatedSeq.toString() + "'");

				updateFlag = _TblControlDAO.update(updateQyery.toString(), htTblCntUpdate, extCond, PLANT);

				MovHisDAO mdao = new MovHisDAO(PLANT);
				mdao.setmLogger(mLogger);
				Hashtable htm = new Hashtable();
				htm.put("PLANT", (String) PLANT);
				htm.put("DIRTYPE", TransactionConstants.UPDATE_BATCH);
				htm.put("RECID", "");
				htm.put("CRBY", (String) LOGIN_USER);
				htm.put("CRAT", (String) new DateUtils().getDateTime());
				boolean inserted = mdao.insertIntoMovHis(htm);

				if (updateFlag) {
					resultflag = true;
				} else if (!updateFlag) {

					throw new Exception(
							"Update BatchNumber Failed, Error In Updating Table:" + " " + PLANT + "_ " + "TBLCONTROL");
				} else if (inserted) {
					resultflag = true;
				}

				else if (!inserted) {

					throw new Exception(
							"Update BatchNumber Failed,  Error In Inserting Table:" + " " + PLANT + "_ " + "MOVHIS");
				}

			}

			if (resultflag == true) {
				xmlStr = XMLUtils.getXMLMessage(1, rtnBatch);

			} else {
				xmlStr = XMLUtils.getXMLMessage(0, "Error in generating batch : ");
			}

		} catch (Exception e) {
			throw e;
		}
		return xmlStr;
	}

	private String GenerateTransactionId(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		String str = "", aItemNum = "", LOGIN_USER = "";
		try {

			PLANT = StrUtils.fString(request.getParameter("PLANT"));
			LOGIN_USER = StrUtils.fString(request.getParameter("LOGIN_USER"));
			com.track.dao.TblControlDAO _TblControlDAO = new com.track.dao.TblControlDAO();
			//String genPosTranid = _TblControlDAO.getNextOrder(PLANT, LOGIN_USER, "GRRECEIPT");
			// String genPosTranid =
			// _TblControlDAO.getNextOrder(PLANT,login_user,"GRRECEIPT");
			String genPosTranid = _TblControlDAO.getNextOrder(PLANT,LOGIN_USER,"GRN");
			Hashtable ht = new Hashtable();

			xmlStr = XMLUtils.getXMLHeader();
			xmlStr = xmlStr + XMLUtils.getStartNode("itemDetails");

			xmlStr = xmlStr + XMLUtils.getXMLNode("status", "0");
			xmlStr = xmlStr + XMLUtils.getXMLNode("description", "");
			xmlStr = xmlStr + XMLUtils.getXMLNode("TransactionId", genPosTranid);
			xmlStr = xmlStr + XMLUtils.getEndNode("itemDetails");

			/*
			 * if (str.equalsIgnoreCase("")) { str = XMLUtils.getXMLMessage(1,
			 * "Error to Generate TransactionId"); }
			 */
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			// xmlStr = XMLUtils.getXMLMessage(1, e.getMessage());
		}

		return xmlStr;
	}
	private String load_all_uom(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException,
			Exception {
		String str = "";

		String PLANT = "", userID = "";
		try {
			PLANT = StrUtils.fString(request.getParameter("PLANT"));
			userID = StrUtils.fString(request.getParameter("LOGIN_USER"));
			
			UomUtil uomUtil = new UomUtil();
			uomUtil.setmLogger(mLogger);
			str = uomUtil.getAllUomWithQty(PLANT, userID);
			if (str.equalsIgnoreCase("")) {

				str = XMLUtils.getXMLMessage(1, "Details not found");
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		}
		return str;
	}
	private String generateGRN(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException,
			Exception {
		String str = "";

		String PLANT = "", userID = "";
		try {
			PLANT = StrUtils.fString(request.getParameter("PLANT"));
			userID = StrUtils.fString(request.getParameter("LOGIN_USER"));
			com.track.dao.TblControlDAO _TblControlDAO=new   com.track.dao.TblControlDAO();
			_TblControlDAO.setmLogger(mLogger); 
			
			str = _TblControlDAO.getNextOrder(PLANT,userID,"GRN");
			if (str!="") {
				    xmlStr += XMLUtils.getXMLHeader();
					xmlStr += XMLUtils.getStartNode("record");
					xmlStr += XMLUtils.getXMLNode("GRNO", str);
					xmlStr += XMLUtils.getEndNode("record");
				    System.out.println(xmlStr);
				    str=xmlStr;
			}
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
