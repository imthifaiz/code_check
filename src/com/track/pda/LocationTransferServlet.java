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
import com.track.dao.InvMstDAO;
import com.track.dao.LocMstDAO;
import com.track.dao.MovHisDAO;
import com.track.dao.POSDetDAO;
import com.track.dao.POSHdrDAO;
import com.track.dao.RsnMst;
import com.track.db.util.InvMstUtil;
import com.track.db.util.ItemMstUtil;
import com.track.db.util.LocUtil;
import com.track.db.util.UserLocUtil;
import com.track.gates.DbBean;
import com.track.util.DateUtils;
import com.track.util.IMLogger;
import com.track.util.MLogger;
import com.track.util.StrUtils;
import com.track.util.XMLUtils;
/*- ************Modification History*********************************
 Dec 5 2014 Bruhan, Description:Modify Method:doGet:process_putaway
 Dec 5 2014 Bruhan, Description:New Method:process_PutAway
 Jan 1 2015,Bruhan, Decription: Include getAssigneeLoc_ItemList,getAssignLoc_LocList
*/

public class LocationTransferServlet extends HttpServlet implements IMLogger {
	private boolean printLog = MLoggerConstant.LocationTransferServlet_PRINTPLANTMASTERLOG;
	private boolean printInfo = MLoggerConstant.LocationTransferServlet_PRINTPLANTMASTERINFO;

	private static final long serialVersionUID = -1710895417460542434L;
	private InvMstUtil _InvMstUtil = null;

	private LocMstDAO _LocMstDAO = new LocMstDAO();
	private String xmlStr = "";
	private String action = "";
	private static final String CONTENT_TYPE = "text/xml";

	public void init() throws ServletException {
		_InvMstUtil = new InvMstUtil();
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType(CONTENT_TYPE);
		PrintWriter out = response.getWriter();

		try {
			action = request.getParameter("action").trim();
			this.setMapDataToLogger(this.populateMapData(StrUtils
					.fString(request.getParameter("PLANT")), StrUtils
					.fString(request.getParameter("LOGIN_USER"))
					+ " PDA_USER"));
			_LocMstDAO.setmLogger(mLogger);
			_InvMstUtil.setmLogger(mLogger);
			if (this.printInfo) {
				diaplayInfoLogs(request);
			}
			if (action.equalsIgnoreCase("load_item_details")) {
				xmlStr = "";
				xmlStr = load_item_details(request, response);
			} else if (action.equalsIgnoreCase("load_all_do_items")) {
				xmlStr = "";
				xmlStr = load_all_do_items(request, response);
			}

			else if (action.equalsIgnoreCase("process_locationTransfer")) {
				xmlStr = "";
				xmlStr = process_locationTransfer(request, response);
			}

			 else if (action.equalsIgnoreCase("load_lotdetails")) {
				xmlStr = "";
				xmlStr = load_lotdetails(request, response);
			} else if (action.equalsIgnoreCase("TransferIn")) {
				xmlStr = "";
				xmlStr = process_locationTransferByWMS(request, response);
			} else if (action.equalsIgnoreCase("LocationTransfer")) {
				xmlStr = "";
				xmlStr = process_locationTransferLTByWMS(request, response);
			}
			 else if (action.equalsIgnoreCase("MultiLocationTransfer")) {
					xmlStr = "";
				
					xmlStr = process_MultilocationTransferLTByWMS(request, response);
				}
			else if (action.equalsIgnoreCase("GET_ALL_LOCATION")) {
				xmlStr = "";
				//xmlStr = getLocationsFromLocationMaster(request, response);
				xmlStr = getLocationsFromLocationMasterNew(request, response);
			} else if (action.equalsIgnoreCase("GET_ALL_LOCATIONNEW")) {
				xmlStr = "";
				xmlStr = getLocationsFromLocationMasterNew(request, response);
		
			} else if (action.equalsIgnoreCase("GET_BATCH_CODES")) {
				xmlStr = "";
				xmlStr = getBatchCode(request, response);

			}	
			else if (action.equalsIgnoreCase("GET_ASSIGN_LOCATION")) {
				xmlStr = "";
				xmlStr =  getAssignLoc_LocList(request, response);
			} 
			else if (action.equalsIgnoreCase("GET_ASSIGN_INV_LOCATION")) {
				xmlStr = "";
				xmlStr =  getAssignInvLoc_LocList(request, response);
		
			}
		  else if (action.equalsIgnoreCase("GenerateTransactionId")) {
			xmlStr = "";
			xmlStr = GenerateTransactionId(request, response);
		  }
		  else if (action.equalsIgnoreCase("GET_ALL_LOCATIONPDA")) {
				xmlStr = "";
				xmlStr = getLocationsFromLocationPDA(request, response);
		
			}
			else {
				throw new Exception(
						"The requested action not found in the server ");
			}

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			xmlStr = XMLUtils.getXMLMessage(1, e.getMessage());
		}

		out.write(xmlStr);
		out.close();
	}

	private String getBatchCode(HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		String xmlStr = "";
		try {
			String itemNo = StrUtils.fString(request.getParameter("ITEM_NUM"));
			String plant = StrUtils.fString(request.getParameter("PLANT"));
			String userID = StrUtils
					.fString(request.getParameter("LOGIN_USER"));
			String loc = StrUtils.fString(request.getParameter("FROM_LOC"));
			InvMstUtil invMstUtil = new InvMstUtil();
			invMstUtil.setmLogger(mLogger);
			xmlStr = invMstUtil.getBatchDetail(plant, itemNo, loc, userID);
			if (xmlStr.equalsIgnoreCase("")) {
				xmlStr = XMLUtils.getXMLMessage(1, "Item Not found");
			}

			return xmlStr;
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		}
	}

	private String getLocationsFromLocationMaster(HttpServletRequest request,
			HttpServletResponse response) {
		String str = "", aPlant = "", aUser = "";
		try {

			aPlant = StrUtils.fString(request.getParameter("PLANT"));
			aUser = StrUtils.fString(request.getParameter("LOGIN_USER"));
			_InvMstUtil.setmLogger(mLogger);

			str = _InvMstUtil.loadLocationsXml(aPlant, aUser);

			if (str.equalsIgnoreCase("")) {
				str = XMLUtils.getXMLMessage(1, "No locations found ");
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			str = XMLUtils.getXMLMessage(1, e.getMessage());
		}
		return str;
	}
	
	private String getLocationsFromLocationMasterNew(HttpServletRequest request,
			HttpServletResponse response) {
		String str = "", aPlant = "", aLoc="",aUser = "";
		try {

			aPlant = StrUtils.fString(request.getParameter("PLANT"));
			aUser = StrUtils.fString(request.getParameter("LOGIN_USER"));
			aLoc = StrUtils.fString(request.getParameter("LOC"));
			_InvMstUtil.setmLogger(mLogger);

			str = _InvMstUtil.loadLocationsXmlPDA(aPlant, aLoc,aUser);

			if (str.equalsIgnoreCase("")) {
				str = XMLUtils.getXMLMessage(1, "No locations found ");
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			str = XMLUtils.getXMLMessage(1, e.getMessage());
		}
		return str;
	}

	
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

	public void destroy() {
	}

	private String load_item_details(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException,
			Exception {
		String str = "", aPlant = "", aItem = "";
		try {
			// MLogger.log(0, "load_item_details() Starts ");
			aPlant = StrUtils.fString(request.getParameter("PLANT"));
			aItem = StrUtils.fString(request.getParameter("ITEM"));

			str = _InvMstUtil.load_item_details_xml(aPlant, aItem);

			if (str.equalsIgnoreCase("")) {
				str = XMLUtils
						.getXMLMessage(1, "No open outgoing order found ");
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;

		}
		return str;
	}

	private String QueryInventoryDetails(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException,
			Exception {
		String str = "", aPlant = "", aItem = "";
		try {
			aPlant = StrUtils.fString(request.getParameter("PLANT"));
			aItem = StrUtils.fString(request.getParameter("ITEM"));

			str = _InvMstUtil.QueryInventory_xml(aPlant, aItem);

			if (str.equalsIgnoreCase("")) {
				str = XMLUtils
						.getXMLMessage(1, "No open outgoing order found ");
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;

		}
		return str;
	}

	private String load_all_do_items(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException,
			Exception {
		String str = "", aPlant = "", aDoNum = "";
		try {
			aPlant = StrUtils.fString(request.getParameter("PLANT"));
			aDoNum = StrUtils.fString(request.getParameter("DO_NUM"));

			str = _InvMstUtil.load_all_do_items_xml(aPlant, aDoNum);

			if (str.equalsIgnoreCase("")) {
				str = XMLUtils
						.getXMLMessage(1, "No open outgoing order found ");
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);

			throw e;

		}
		return str;
	}

	private String load_do_detais(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException,
			Exception {
		String str = "", aPlant = "";
		try {
			aPlant = StrUtils.fString(request.getParameter("PLANT"));
			if (str.equalsIgnoreCase("")) {
				str = XMLUtils
						.getXMLMessage(1, "No open outgoing order found ");
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;

		}

		return str;
	}

	private String load_lotdetails(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException,
			Exception {
		String str = "", aPlant = "", aItem = "", aBatch = "", loc = "";
		try {
			aPlant = StrUtils.fString(request.getParameter("PLANT"));
			aItem = StrUtils.fString(request.getParameter("ITEM"));
			aBatch = StrUtils.fString(request.getParameter("BATCH"));
			loc = StrUtils.fString(request.getParameter("LOC"));

			str = _InvMstUtil.load_lotdetails_xml(aPlant, aItem, aBatch, loc);

			if (str.equalsIgnoreCase("")) {
				str = XMLUtils
						.getXMLMessage(1, "No open outgoing order found ");
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;

		}

		return str;
	}
	 /*
     ************Modification History*********************************
 	   Oct 21 2014 Bruhan, Description: To include Transaction date
 	   April 29 2015 Bruhan, Description: To include REMARKS
 	*/
	private String process_locationTransfer(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {
		// MLogger.log(0, "process_locationTransfer() Starts");
		InvMstDAO _InvMstDAO = new InvMstDAO();
		_InvMstDAO.setmLogger(mLogger);
		Map<String, String> locTran_HM = null;
		String PLANT = "", FROM_LOC = "", ITEM_NUM = "", TO_LOC = "", TRAN_TYPE = "";
		String LOGIN_USER = "";
		String BATCH = "", Qty = "",INVQTY="",EXPIRYDATE="",REMARKS="",RSNDESC="",TRANSACTIONID="",EMPNO="",INVID="", UOM = "";
		int qty = 0;
		int invqty = 0;

		try {
			PLANT = StrUtils.fString(request.getParameter("PLANT"));
			ITEM_NUM = StrUtils.fString(request.getParameter("ITEM_NUM"));
			FROM_LOC = StrUtils.fString(request.getParameter("FROM_LOC"));
			TO_LOC = StrUtils.fString(request.getParameter("TO_LOC"));
			TRAN_TYPE = StrUtils.fString(request.getParameter("TRAN_TYPE"));
			LOGIN_USER = StrUtils.fString(request.getParameter("LOGIN_USER"));
			BATCH = StrUtils.fString(request.getParameter("BATCH"));
			Qty = StrUtils.fString(request.getParameter("QTY"));
			REMARKS = StrUtils.fString(request.getParameter("REMARKS"));
			RSNDESC = StrUtils.fString(request.getParameter("REASONCODE"));
			TRANSACTIONID=StrUtils.fString(request.getParameter("ORDERNO"));
			EMPNO=StrUtils.fString(request.getParameter("EMPNO"));
			INVID=StrUtils.fString(request.getParameter("INVID"));
			UOM=StrUtils.fString(request.getParameter("UOM"));
			DateUtils _dateUtils = new DateUtils();
			String curDate =_dateUtils.getDate();
			String strTranDate    = curDate.substring(6)+"-"+ curDate.substring(3,5)+"-"+curDate.substring(0,2);
			String strIssueDate    = curDate.substring(0,2)+"/"+ curDate.substring(3,5)+"/"+curDate.substring(6);
			
			// To check transaction already exists in POSHeader
			posServlet _posServlet = new posServlet();
			POSHdrDAO poshdr = new POSHdrDAO();
			Hashtable<String, String> htTrandId = new Hashtable<String, String>();
			htTrandId.put("POSTRANID", TRANSACTIONID);			
			boolean istranidExist = _posServlet.isExisit(PLANT, htTrandId);
			double qty1 = Double.parseDouble(Qty);
			qty1 = StrUtils.RoundDB(qty1, IConstants.DECIMALPTS );
			Qty = String.valueOf(qty1);

			List listQry = _InvMstDAO.getLocationTransferBatch(PLANT,
					ITEM_NUM, FROM_LOC, BATCH);
			if (listQry.size() > 0) {
				Map m = (Map) listQry.get(0);
				String sQty = (String) m.get("qty");
								
				
				EXPIRYDATE =  (String) m.get("expiredate");
				INVQTY = sQty;
				}else{
                      throw new Exception("Please check the Batch and Quantity");
               }
			
			locTran_HM = new HashMap<String, String>();
			locTran_HM.put(IConstants.PLANT, PLANT);
			locTran_HM.put(IConstants.ITEM, ITEM_NUM);
			locTran_HM.put(IConstants.LOC, FROM_LOC);
			locTran_HM.put(IConstants.LOC2, TO_LOC);
			locTran_HM.put(IConstants.TRAN_TYPE, TRAN_TYPE);
			locTran_HM.put(IConstants.LOGIN_USER, LOGIN_USER);
			locTran_HM.put(IConstants.BATCH, BATCH);
			locTran_HM.put(IConstants.QTY, Qty);
			locTran_HM.put(IConstants.CUSTOMER_CODE, "");//invMstDAO.getCustCode(PLANT, ITEM_NUM) // Commented by Samatha as Userfld2 is always empty data
			locTran_HM.put(IConstants.EXPIREDATE, EXPIRYDATE);
			locTran_HM.put(IConstants.TRAN_DATE, strTranDate);
			locTran_HM.put("INV_QTY1", "1");
			locTran_HM.put(IConstants.REMARKS, REMARKS);
            locTran_HM.put(IConstants.RSNDESC, RSNDESC);
            locTran_HM.put(IConstants.PONO, TRANSACTIONID);
            locTran_HM.put(IConstants.INVID, INVID);
            locTran_HM.put(IConstants.EMPNO, EMPNO);
            locTran_HM.put(IConstants.ISPDA, "ISPDA");
            locTran_HM.put(IConstants.BATCH_ID, INVID);
			locTran_HM.put(IConstants.ISSUEDATE, strIssueDate);
			locTran_HM.put(IConstants.UNITMO, UOM);
			xmlStr = "";
			Hashtable<String, String> htLocMst = new Hashtable<String, String>();
			htLocMst.put("PLANT", locTran_HM.get(IConstants.PLANT));
			htLocMst.put("loc", locTran_HM.get(IConstants.LOC2));
			_LocMstDAO.setmLogger(mLogger);
			_InvMstUtil.setmLogger(mLogger);
			
			//Check Stock
			Hashtable ht1 = new Hashtable();
			ht1.put(IConstants.PLANT, PLANT);
			ht1.put(IConstants.LOGIN_USER, LOGIN_USER);
			ht1.put(IConstants.LOC, FROM_LOC);
			ht1.put(IConstants.ITEM, ITEM_NUM);
			ht1.put(IConstants.BATCH, BATCH);
			String UOMQTY="1";
			Hashtable<String, String> htTrand1 = new Hashtable<String, String>();
			MovHisDAO movHisDao1 = new MovHisDAO();
			ArrayList getuomqty = movHisDao1.selectForReport("select ISNULL(QPUOM,1) as UOMQTY from "+PLANT+"_UOM where UOM='"+UOM+"'",htTrand1);
			if(getuomqty.size()>0)
			{
			Map mapval = (Map) getuomqty.get(0);
			UOMQTY=(String)mapval.get("UOMQTY");
			}
			locTran_HM.put("UOMQTY", UOMQTY);
			double invumqty = Double.valueOf(Qty) * Double.valueOf(UOMQTY);			
			ht1.put(IConstants.QTY, String.valueOf(invumqty));
			boolean flag = true;
			
				flag = CheckInvMstForGoddsIssue(ht1, request, response);
				if(!flag)
				{
					xmlStr = XMLUtils.getXMLMessage(0,
	 						"Not Enough Inventory For Product : " + ITEM_NUM);
					throw new Exception(" Not Enough Inventory For Product ");
				}
				if (flag) {
			
			xmlStr = _InvMstUtil.process_LocationTransfer(locTran_HM);
			POSHdrDAO posHdr = new POSHdrDAO();
			POSDetDAO posdet = new POSDetDAO();
			Hashtable<String, String> ht = new Hashtable<String, String>();
			ht.put(IDBConstants.PLANT, PLANT);
			ht.put(IDBConstants.POS_TRANID, TRANSACTIONID);
			String QryUpdate = " SET STATUS ='C',RECEIPTNO='" + TRANSACTIONID + "',UPAT ='" + DateUtils.getDate()
					+ "' ";
			boolean posHsrUpt = posHdr.update(QryUpdate, ht, "");
				} else {
					throw new Exception(" Transfer is not successfull ");
				}
			
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			xmlStr = XMLUtils.getXMLMessage(0,
					"Error in transfering Product : "
							//+ locTran_HM.get(IConstants.ITEM)
					+ ITEM_NUM
							+ " to location : "
							//+ locTran_HM.get(IConstants.LOC2) + " Error :: "
							+ TO_LOC + " Error :: "
							+ e.getMessage());
		}

		return xmlStr;
	}

	private String process_locationTransferByWMS(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {
		// MLogger.log(0, "process_locationTransferWMS() Starts");
		InvMstDAO _InvMstDAO = new InvMstDAO();
		LocMstDAO _LocMstDAO = new LocMstDAO();
		String checkqty = "";

		Map locTran_HM = null;
		String PLANT = "", FROM_LOC = "", ITEM_NUM = "", TO_LOC = "", TRAN_TYPE = "";
		String ITEM_DESCRIPTION = "", LOGIN_USER = "";
		String BATCH = "", Qty = "";

		HttpSession session = request.getSession();
		String plant = session.getAttribute("PLANT").toString();
		
		try {
			PLANT = plant;// 
			ITEM_NUM = StrUtils.fString(request.getParameter("ITEMNO"));
			ITEM_DESCRIPTION = StrUtils.fString(request
					.getParameter("ITEMDESC"));
			FROM_LOC = StrUtils.fString(request.getParameter("FROMLOC"));
			TO_LOC = StrUtils.fString(request.getParameter("TOLOC"));
			TRAN_TYPE = "LOC_TRANSFER";// StrUtils.fString(request.getParameter("TRAN_TYPE"));
			LOGIN_USER = StrUtils.fString(request.getParameter("LOGINUSER"));
			BATCH = StrUtils.fString(request.getParameter("BATCH"));
			Qty = StrUtils.fString(request.getParameter("QTY"));

			locTran_HM = new HashMap();
			locTran_HM.put(IConstants.PLANT, PLANT);
			locTran_HM.put(IConstants.ITEM, ITEM_NUM);
			locTran_HM.put(IConstants.LOC, FROM_LOC);
			locTran_HM.put(IConstants.LOC2, TO_LOC);
			locTran_HM.put(IConstants.TRAN_TYPE, TRAN_TYPE);
			locTran_HM.put(IConstants.LOGIN_USER, LOGIN_USER);

			locTran_HM.put(IConstants.BATCH, BATCH);
			locTran_HM.put(IConstants.QTY, Qty);

			locTran_HM.put(IConstants.CUSTOMER_CODE, "");//invMstDAO.getCustCode(PLANT, ITEM_NUM) // Commented by Samatha as Userfld2 is always empty data

			xmlStr = "";
			// check for location
			Hashtable htLocMst = new Hashtable();
			htLocMst.put("PLANT", plant);
			htLocMst.put("loc", locTran_HM.get(IConstants.LOC2));

			boolean flag = _LocMstDAO.isExisit(htLocMst, "");

			PLANT = plant;
			ITEM_NUM = StrUtils.fString(request.getParameter("ITEMNO"));
			ITEM_DESCRIPTION = StrUtils.fString(request
					.getParameter("ITEMDESC"));
			FROM_LOC = StrUtils.fString(request.getParameter("FROMLOC"));
			TO_LOC = StrUtils.fString(request.getParameter("TOLOC"));
			TRAN_TYPE = "LOC_TRANSFER";// StrUtils.fString(request.getParameter("TRAN_TYPE"));
			LOGIN_USER = StrUtils.fString(request.getParameter("LOGINUSER"));
			BATCH = StrUtils.fString(request.getParameter("BATCH"));
			Qty = StrUtils.fString(request.getParameter("QTY"));

			if (flag) {
				xmlStr = _InvMstUtil.process_LocationTransfer(locTran_HM);
				request.getSession().setAttribute(
						"RESULT",
						"Product : " + ITEM_NUM + " " + "transfered to" + " "
								+ TO_LOC + " " + "successfully!");
				response
						.sendRedirect("jsp/InboundOrderPutAway.jsp?action=result");
			} else {
				request.getSession().setAttribute(
						"RESULTERROR",
						"Loc : " + locTran_HM.get(IConstants.LOC2)
								+ " not found");
				response
						.sendRedirect("jsp/InboundOrderPutAway.jsp?action=resulterror&ITEMNO="
								+ ITEM_NUM
								+ "&ITEMDESC="
								+ ITEM_DESCRIPTION
								+ "&FROMLOC="
								+ FROM_LOC
								+ "&TOLOC="
								+ TO_LOC
								+ "&BATCH=" + BATCH + "&QTY=" + Qty);

			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			request.getSession().setAttribute("RESULTCATCHERROR",
					e.getMessage());
			response
					.sendRedirect("jsp/InboundOrderPutAway.jsp?action=resultcatcherror&ITEMNO="
							+ ITEM_NUM
							+ "&ITEMDESC="
							+ ITEM_DESCRIPTION
							+ "&FROMLOC="
							+ FROM_LOC
							+ "&TOLOC="
							+ TO_LOC
							+ "&BATCH=" + BATCH + "&QTY=" + Qty);

		}
		// MLogger.log(0, "process_locationTransferWMS() Ends");

		return xmlStr;
	}

	private String process_locationTransferLTByWMS(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {
		// MLogger.log(0, "process_locationTransferLTWMS() Starts");
		InvMstDAO invMstDAO = new InvMstDAO();
		invMstDAO.setmLogger(mLogger);
		String checkqty = "";

		Map locTran_HM = null;
		String PLANT = "", FROM_LOC = "", ITEM_NUM = "", TO_LOC = "", TRAN_TYPE = "";
		String ITEM_DESCRIPTION = "", LOGIN_USER = "";
		String BATCH = "", Qty = "", INVQTY = "", UOM = "",EXPIRYDATE="";
		HttpSession session = request.getSession();
		 PLANT = (String) session.getAttribute("PLANT");
		/*
		 * HttpSession transferQty = request.getSession(); String
		 * checkQty=transferQty.getAttribute("TRANSFERINQTY").toString();
		 */
		double qty = 0;
		double invqty = 0;
		try {
			
			ITEM_NUM = StrUtils.fString(request.getParameter("ITEMNO"));
			ITEM_DESCRIPTION = StrUtils.fString(request
					.getParameter("ITEMDESC"));
			FROM_LOC = StrUtils.fString(request.getParameter("FROM_LOC"));
			TO_LOC = StrUtils.fString(request.getParameter("TOLOC"));
			TRAN_TYPE = "LOC_TRANSFER";// StrUtils.fString(request.getParameter("TRAN_TYPE"));
			LOGIN_USER = StrUtils.fString(request.getParameter("LOGINUSER"));
			BATCH = StrUtils.fString(request.getParameter("BATCH"));
			Qty = StrUtils.removeFormat(StrUtils.fString(request.getParameter("QTY")));
			INVQTY = StrUtils.removeFormat(StrUtils.fString(request.getParameter("INVQTY")));
			UOM = StrUtils.fString(request.getParameter("UOM"));
			ItemMstUtil itemMstUtil = new ItemMstUtil();
			itemMstUtil.setmLogger(mLogger);
			ITEM_NUM = itemMstUtil.isValidAlternateItemInItemmst(PLANT,
					ITEM_NUM);
			double tranqty = Double.parseDouble(Qty);
			tranqty = StrUtils.RoundDB(tranqty, IConstants.DECIMALPTS);
			

			List listQry = invMstDAO.getLocationTransferBatch(PLANT,
					ITEM_NUM, FROM_LOC, BATCH);
			if (listQry.size() > 0) {
				Map m = (Map) listQry.get(0);
				String sQty = (String) m.get("qty");
								
				
				EXPIRYDATE =  (String) m.get("expiredate");
				INVQTY = sQty;
				}else{
                                    throw new Exception("Please check the Batch and Quantity");
               }
			try {
				qty = Double.parseDouble(((String) Qty.trim()));
				qty = StrUtils.RoundDB(qty, IConstants.DECIMALPTS);
				invqty = Double.parseDouble(((String) INVQTY.trim()));
			} catch (Exception ex) {
				throw new Exception(
						"Number Format Exception. Please check the Batch and Quantity");

			}
			if (qty > invqty) {

				throw new Exception(
						"Error in transfer item : Transfer Qty Should less than or equal to InvQty:"
								+ invqty);
			}
			Qty = String.valueOf(tranqty);
			locTran_HM = new HashMap();
			locTran_HM.put(IConstants.PLANT, PLANT);
			locTran_HM.put(IConstants.ITEM, ITEM_NUM);
			locTran_HM.put(IConstants.LOC, FROM_LOC);
			locTran_HM.put(IConstants.LOC2, TO_LOC);
			locTran_HM.put(IConstants.TRAN_TYPE, TRAN_TYPE);
			locTran_HM.put(IConstants.LOGIN_USER, LOGIN_USER);

			locTran_HM.put(IConstants.BATCH, BATCH);
			locTran_HM.put(IConstants.QTY, Qty);

			locTran_HM.put(IConstants.CUSTOMER_CODE, "");//invMstDAO.getCustCode(PLANT, ITEM_NUM) // Commented by Samatha as Userfld2 is always empty data
		
			locTran_HM.put(IConstants.EXPIREDATE, EXPIRYDATE);
			locTran_HM.put("INV_QTY1", "1");


			UserLocUtil uslocUtil = new UserLocUtil();
			uslocUtil.setmLogger(mLogger);
			boolean isvalidlocforUser = uslocUtil.isValidLocInLocmstForUser(
					PLANT, LOGIN_USER, FROM_LOC);
			if (isvalidlocforUser) {
				isvalidlocforUser = uslocUtil.isValidLocInLocmstForUser(PLANT,
						LOGIN_USER, TO_LOC);

				if (!isvalidlocforUser) {
					throw new Exception("To Loc :" + TO_LOC
							+ " is not a User Assigned Location/valid location");
				}
			} else {

				throw new Exception("From Loc :" + FROM_LOC
						+ " is not a User Assigned Location/valid location");
			}
			ITEM_NUM = StrUtils.fString(request.getParameter("ITEMNO"));
			ITEM_DESCRIPTION = StrUtils.fString(request
					.getParameter("ITEMDESC"));
			FROM_LOC = StrUtils.fString(request.getParameter("FROM_LOC"));
			TO_LOC = StrUtils.fString(request.getParameter("TOLOC"));
			TRAN_TYPE = "LOC_TRANSFER";// StrUtils.fString(request.getParameter("TRAN_TYPE"));
			LOGIN_USER = StrUtils.fString(request.getParameter("LOGINUSER"));
			BATCH = StrUtils.fString(request.getParameter("BATCH"));
			Qty = StrUtils.fString(request.getParameter("QTY"));

			_InvMstUtil.setmLogger(mLogger);
                        boolean flag= false;
			flag = _InvMstUtil.process_LocationTransferforPC(locTran_HM);
	
                        if(flag){
			request.getSession().setAttribute(
					"RESULT",
					"Product : " + ITEM_NUM + " " + "transfered to" + " "
							+ TO_LOC + " " + "successfully!");
			response.sendRedirect("jsp/LocationTransfer.jsp?action=result");
                        }
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);

			request.getSession().setAttribute("RESULTCATCHERROR",
					"Error  : " + e.getMessage());

			response
					.sendRedirect("jsp/LocationTransfer.jsp?action=resultcatcherror&ITEMNO="
							+ ITEM_NUM
							+ "&ITEMDESC="
							+ ITEM_DESCRIPTION
							+ "&FROM_LOC="
							+ FROM_LOC
							+ "&TOLOC="
							+ TO_LOC
							+ "&UOM="
							+ UOM
							+ "&INVQTY="
							+ INVQTY
							+ "&BATCH="
							+ BATCH + "&QTY=" + Qty);

		}
		// MLogger.log(0, "process_locationTransferLTWMS() Ends");

		return xmlStr;
	}
	/*************Modification History*********************************
	   Oct 21 2014 Bruhan, Description: To include Transaction date
	*/ 
	String process_MultilocationTransferLTByWMS(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {
		// MLogger.log(0, "process_locationTransferLTWMS() Starts");
		InvMstDAO invMstDAO = new InvMstDAO();
		invMstDAO.setmLogger(mLogger);
		String checkqty = "";
		UserTransaction ut=null;
		Map locTran_HM = null;
		String PLANT = "", FROM_LOC = "", ITEM_NUM = "", TO_LOC = "", TRAN_TYPE = "";
		String ITEM_DESCRIPTION = "", LOGIN_USER = "";
		String BATCH = "", Qty = "", INVQTY = "", UOM = "",EXPIRYDATE="",RSNDESC="",REMARKS="",TRANSACTIONDATE = "",strMovHisTranDate="",strTranDate="";
		HttpSession session = request.getSession();
		 PLANT = (String) session.getAttribute("PLANT");
		/*
		 * HttpSession transferQty = request.getSession(); String
		 * checkQty=transferQty.getAttribute("TRANSFERINQTY").toString();
		 */
		double qty = 0;
		double invqty = 0;
		try {
			
			
			FROM_LOC = StrUtils.fString(request.getParameter("FROM_LOC"));
			TO_LOC = StrUtils.fString(request.getParameter("TOLOC"));
			TRAN_TYPE = "LOC_TRANSFER";// StrUtils.fString(request.getParameter("TRAN_TYPE"));
			LOGIN_USER = StrUtils.fString(request.getParameter("LOGINUSER"));
	
			Boolean transactionHandler = true;
			ut = DbBean.getUserTranaction();
			ut.begin();
			
			//Product count
			String DYNAMIC_PRODUCT_SIZE = StrUtils.fString(request.getParameter("DYNAMIC_PRODUCT_SIZE"));
			
			int product_cnt = Integer.parseInt(DYNAMIC_PRODUCT_SIZE);
			for(int index=0;index<product_cnt;index++)
			{
				
			ITEM_NUM = StrUtils.fString(request.getParameter("ITEMNO"+"_"+index));
			ITEM_DESCRIPTION = StrUtils.fString(request
					.getParameter("ITEMDESC"+"_"+index));
			BATCH = StrUtils.fString(request.getParameter("BATCH"+"_"+index));
			Qty = StrUtils.removeFormat(StrUtils.fString(request.getParameter("QTY"+"_"+index)));
			INVQTY = StrUtils.removeFormat(StrUtils.fString(request.getParameter("INVQTY"+"_"+index)));
			UOM = StrUtils.fString(request.getParameter("UOM"+"_"+index));
			RSNDESC = StrUtils.fString(request.getParameter("REASONCODE"+"_"+index));
			//ITEM_EXPDATE = StrUtils.fString(request.getParameter("REFDET"+"_"+index));
			REMARKS = StrUtils.fString(request.getParameter("REFDET"+"_"+index));
			TRANSACTIONDATE = StrUtils.fString(request.getParameter("TRANSACTIONDATE"));
			if (TRANSACTIONDATE.length()>5)
				strMovHisTranDate    = TRANSACTIONDATE.substring(6)+"-"+TRANSACTIONDATE.substring(3,5)+"-"+TRANSACTIONDATE.substring(0,2);
			    strTranDate    = TRANSACTIONDATE.substring(0,2)+"/"+ TRANSACTIONDATE.substring(3,5)+"/"+TRANSACTIONDATE.substring(6);
			ItemMstUtil itemMstUtil = new ItemMstUtil();
			itemMstUtil.setmLogger(mLogger);
			ITEM_NUM = itemMstUtil.isValidAlternateItemInItemmst(PLANT,
					ITEM_NUM);
			

			List listQry = invMstDAO.getLocationTransferBatch(PLANT,
					ITEM_NUM, FROM_LOC, BATCH);
			if (listQry.size() > 0) {
				Map m = (Map) listQry.get(0);
				String sQty = (String) m.get("qty");
								
				
				EXPIRYDATE =  (String) m.get("expiredate");
				INVQTY = sQty;
				}else{
                                    throw new Exception("Please check the Batch and Quantity");
               }
//			try {
				qty = Double.parseDouble(((String) Qty.trim()));
				invqty = Double.parseDouble(((String) INVQTY.trim()));
				qty = StrUtils.RoundDB(qty, IConstants.DECIMALPTS);
				invqty = StrUtils.RoundDB(invqty, IConstants.DECIMALPTS);
				Qty = String.valueOf(qty);
				Qty = StrUtils.formatThreeDecimal(Qty);
			//} catch (Exception ex) {
//				throw new Exception(
//						"Number Format Exception. Please check the Batch and Quantity");
//
//			}
//			if (qty > invqty) {
//
//				throw new Exception(
//						"Error in transfer item : Transfer Qty Should less than or equal to InvQty:"
//								+ invqty);
//			}
			
			locTran_HM = new HashMap();
			locTran_HM.put(IConstants.PLANT, PLANT);
			locTran_HM.put(IConstants.ITEM, ITEM_NUM);
			locTran_HM.put(IConstants.LOC, FROM_LOC);
			locTran_HM.put(IConstants.LOC2, TO_LOC);
			locTran_HM.put(IConstants.TRAN_TYPE, TRAN_TYPE);
			locTran_HM.put(IConstants.LOGIN_USER, LOGIN_USER);

			locTran_HM.put(IConstants.BATCH, BATCH);
			locTran_HM.put(IConstants.QTY, Qty);

			locTran_HM.put(IConstants.CUSTOMER_CODE, "");//invMstDAO.getCustCode(PLANT, ITEM_NUM) // Commented by Samatha as Userfld2 is always empty data
		
			locTran_HM.put(IConstants.EXPIREDATE, EXPIRYDATE);
			locTran_HM.put("INV_QTY1", "1");
			locTran_HM.put(IConstants.RSNDESC, RSNDESC);
			locTran_HM.put(IConstants.REMARKS, REMARKS);
			locTran_HM.put(IConstants.TRAN_DATE, strMovHisTranDate);
		  UserLocUtil uslocUtil = new UserLocUtil();
			uslocUtil.setmLogger(mLogger);
			boolean isvalidlocforUser = uslocUtil.isValidLocInLocmstForUser(
					PLANT, LOGIN_USER, FROM_LOC);
			if (isvalidlocforUser) {
				isvalidlocforUser = uslocUtil.isValidLocInLocmstForUser(PLANT,
						LOGIN_USER, TO_LOC);

				if (!isvalidlocforUser) {
					throw new Exception("To Loc :" + TO_LOC
							+ " is not a User Assigned Location/valid location");
				}
			} else {

				throw new Exception("From Loc :" + FROM_LOC
						+ " is not a User Assigned Location/valid location");
			}

		TO_LOC = StrUtils.fString(request.getParameter("TOLOC"));
			_InvMstUtil.setmLogger(mLogger);
				RsnMst rsnMst = new RsnMst();
				rsnMst.setmLogger(mLogger);
				boolean flag = false;
				Hashtable htRsnMst = new Hashtable();
				htRsnMst.put(IConstants.PLANT, PLANT);
				htRsnMst.put(IConstants.RSNCODE, RSNDESC);
				if (!rsnMst.isExists(htRsnMst)) {
					throw new Exception("Invalid Reason Code!");
				}
				if(!flag)
				transactionHandler = _InvMstUtil.process_MultiLocationTransferforPC(locTran_HM);
				else
					transactionHandler= transactionHandler&&false;
					
			}
		
			if(transactionHandler==true)
			{
				DbBean.CommitTran(ut);
				request.getSession().setAttribute(
						"RESULT",
						"Products "  + "transfered to" + " "
								+ TO_LOC + " " + "successfully!");
				response.sendRedirect("jsp/MultiLocationTransfer.jsp?action=result");
			}
			else{
				DbBean.RollbackTran(ut);
				request.getSession().setAttribute(
						"RESULTERROR",
						"Error in transfering the location");
				response
						.sendRedirect("jsp/MultiLocationTransfer.jsp?action=resulterror");
			}
                    		} catch (Exception e) {
		         DbBean.RollbackTran(ut);
			this.mLogger.exception(this.printLog, "", e);

			request.getSession().setAttribute("RESULTCATCHERROR",
					"Error  : " + e.getMessage());

			response
					.sendRedirect("jsp/MultiLocationTransfer.jsp?action=resultcatcherror&ITEMNO="
							+ ITEM_NUM
							+ "&ITEMDESC="
							+ ITEM_DESCRIPTION
							+ "&FROM_LOC="
							+ FROM_LOC
							+ "&TOLOC="
							+ TO_LOC
							+ "&UOM="
							+ UOM
							+ "&INVQTY="
							+ INVQTY
							+ "&REASONCODE="
							+ RSNDESC
							+ "&BATCH="
							+ BATCH + "&QTY=" + Qty);

		}
		// MLogger.log(0, "process_locationTransferLTWMS() Ends");

		return xmlStr;
	}
	
	/*String process_PutawayLocByWMS(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {
		// MLogger.log(0, "process_locationTransferLTWMS() Starts");
		InvMstDAO invMstDAO = new InvMstDAO();
		invMstDAO.setmLogger(mLogger);
		String checkqty = "";
		UserTransaction ut=null;
		Map locTran_HM = null;
		String PLANT = "", FROM_LOC = "", ITEM_NUM = "", TO_LOC = "", TRAN_TYPE = "";
		String ITEM_DESCRIPTION = "", LOGIN_USER = "";
		String BATCH = "", Qty = "", INVQTY = "", UOM = "",EXPIRYDATE="",RSNDESC="",REMARKS="",TRANSACTIONDATE = "",strMovHisTranDate="",strTranDate="";
		HttpSession session = request.getSession();
		 PLANT = (String) session.getAttribute("PLANT");
		
		double qty = 0;
		double invqty = 0;
		try {
			
			
			FROM_LOC = StrUtils.fString(request.getParameter("FROM_LOC"));
			//TO_LOC = StrUtils.fString(request.getParameter("TOLOC"));
			//TRAN_TYPE = "LOC_TRANSFER";// StrUtils.fString(request.getParameter("TRAN_TYPE"));
			LOGIN_USER = StrUtils.fString(request.getParameter("LOGINUSER"));
	
			Boolean transactionHandler = true;
			ut = DbBean.getUserTranaction();
			ut.begin();
			
			//Product count
			String DYNAMIC_PRODUCT_SIZE = StrUtils.fString(request.getParameter("DYNAMIC_PRODUCT_SIZE"));
			
			int product_cnt = Integer.parseInt(DYNAMIC_PRODUCT_SIZE);
			for(int index=0;index<product_cnt;index++)
			{
				
			ITEM_NUM = StrUtils.fString(request.getParameter("ITEMNO"+"_"+index));
			ITEM_DESCRIPTION = StrUtils.fString(request.getParameter("ITEMDESC"+"_"+index));
			TO_LOC = StrUtils.fString(request.getParameter("TOLOC"+"_"+index));
			BATCH = StrUtils.fString(request.getParameter("BATCH"+"_"+index));
			Qty = StrUtils.removeFormat(StrUtils.fString(request.getParameter("QTY"+"_"+index)));
			INVQTY = StrUtils.removeFormat(StrUtils.fString(request.getParameter("INVQTY"+"_"+index)));
			UOM = StrUtils.fString(request.getParameter("UOM"+"_"+index));
			RSNDESC = StrUtils.fString(request.getParameter("REASONCODE"+"_"+index));
			//ITEM_EXPDATE = StrUtils.fString(request.getParameter("REFDET"+"_"+index));
			REMARKS = StrUtils.fString(request.getParameter("REFDET"+"_"+index));
			TRANSACTIONDATE = StrUtils.fString(request.getParameter("TRANSACTIONDATE"));
			if (TRANSACTIONDATE.length()>5)
				strMovHisTranDate    = TRANSACTIONDATE.substring(6)+"-"+TRANSACTIONDATE.substring(3,5)+"-"+TRANSACTIONDATE.substring(0,2);
			    strTranDate    = TRANSACTIONDATE.substring(0,2)+"/"+ TRANSACTIONDATE.substring(3,5)+"/"+TRANSACTIONDATE.substring(6);
			ItemMstUtil itemMstUtil = new ItemMstUtil();
			itemMstUtil.setmLogger(mLogger);
			ITEM_NUM = itemMstUtil.isValidAlternateItemInItemmst(PLANT,
					ITEM_NUM);
			

			List listQry = invMstDAO.getLocationTransferBatch(PLANT,
					ITEM_NUM, FROM_LOC, BATCH);
			if (listQry.size() > 0) {
				Map m = (Map) listQry.get(0);
				String sQty = (String) m.get("qty");
								
				
				EXPIRYDATE =  (String) m.get("expiredate");
				INVQTY = sQty;
				}else{
                                    throw new Exception("Please check the Batch and Quantity");
               }

				qty = Double.parseDouble(((String) Qty.trim()));
				invqty = Double.parseDouble(((String) INVQTY.trim()));
				qty = StrUtils.RoundDB(qty, IConstants.DECIMALPTS);
				invqty = StrUtils.RoundDB(invqty, IConstants.DECIMALPTS);
				Qty = String.valueOf(qty);
				Qty = StrUtils.formatThreeDecimal(Qty);

			locTran_HM = new HashMap();
			locTran_HM.put(IConstants.PLANT, PLANT);
			locTran_HM.put(IConstants.ITEM, ITEM_NUM);
			locTran_HM.put(IConstants.LOC, FROM_LOC);
			locTran_HM.put(IConstants.LOC2, TO_LOC);
			//locTran_HM.put(IConstants.TRAN_TYPE, TRAN_TYPE);
			locTran_HM.put(IConstants.LOGIN_USER, LOGIN_USER);
			locTran_HM.put(IConstants.BATCH, BATCH);
			locTran_HM.put(IConstants.QTY, Qty);
			locTran_HM.put(IConstants.CUSTOMER_CODE, "");//invMstDAO.getCustCode(PLANT, ITEM_NUM) // Commented by Samatha as Userfld2 is always empty data
		
			locTran_HM.put(IConstants.EXPIREDATE, EXPIRYDATE);
			locTran_HM.put("INV_QTY1", "1");
			locTran_HM.put(IConstants.RSNDESC, RSNDESC);
			locTran_HM.put(IConstants.REMARKS, REMARKS);
			locTran_HM.put(IConstants.TRAN_DATE, strMovHisTranDate);
			locTran_HM.put(IConstants.TRAN_TYPE, "");
	
		  UserLocUtil uslocUtil = new UserLocUtil();
			uslocUtil.setmLogger(mLogger);
			boolean isvalidlocforUser = uslocUtil.isValidLocInLocmstForUser(
					PLANT, LOGIN_USER, FROM_LOC);
			if (isvalidlocforUser) {
				isvalidlocforUser = uslocUtil.isValidLocInLocmstForUser(PLANT,
						LOGIN_USER, TO_LOC);

				if (!isvalidlocforUser) {
					throw new Exception("To Loc :" + TO_LOC
							+ " is not a User Assigned Location/valid location");
				}
			} else {

				throw new Exception("From Loc :" + FROM_LOC
						+ " is not a User Assigned Location/valid location");
			}

		_InvMstUtil.setmLogger(mLogger);
				RsnMst rsnMst = new RsnMst();
				rsnMst.setmLogger(mLogger);
				boolean flag = false;
				Hashtable htRsnMst = new Hashtable();
				htRsnMst.put(IConstants.PLANT, PLANT);
				htRsnMst.put(IConstants.RSNCODE, RSNDESC);
				if (!rsnMst.isExists(htRsnMst)) {
					throw new Exception("Invalid Reason Code!");
				}
				if(!flag)
				transactionHandler = _InvMstUtil.process_PutawayLocforPC(locTran_HM);
				else
					transactionHandler= transactionHandler&&false;
					
			}
		
			if(transactionHandler==true)
			{
				DbBean.CommitTran(ut);
				request.getSession().setAttribute(
						"RESULT",
						"Products putaway to location successfully!");
				response.sendRedirect("jsp/PutawayLocation.jsp?action=result");
			}
			else{
				DbBean.RollbackTran(ut);
				request.getSession().setAttribute(
						"RESULTERROR",
						"Error in putaway location");
				response
						.sendRedirect("jsp/PutawayLocation.jsp?action=resulterror");
			}
                    		} catch (Exception e) {
		         DbBean.RollbackTran(ut);
			this.mLogger.exception(this.printLog, "", e);

			request.getSession().setAttribute("RESULTCATCHERROR",
					"Error  : " + e.getMessage());

			response
					.sendRedirect("jsp/PutawayLocation.jsp?action=resultcatcherror&ITEMNO="
							+ ITEM_NUM
							+ "&ITEMDESC="
							+ ITEM_DESCRIPTION
							+ "&FROM_LOC="
							+ FROM_LOC
							+ "&TOLOC="
							+ TO_LOC
							+ "&UOM="
							+ UOM
							+ "&INVQTY="
							+ INVQTY
							+ "&REASONCODE="
							+ RSNDESC
							+ "&BATCH="
							+ BATCH + "&QTY=" + Qty);

		}
		// MLogger.log(0, "process_locationTransferLTWMS() Ends");

		return xmlStr;
	}*/

	/*private String process_PutAwayPDA(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {
		// MLogger.log(0, "process_locationTransfer() Starts");
		InvMstDAO _InvMstDAO = new InvMstDAO();
		_InvMstDAO.setmLogger(mLogger);
		Map<String, String> locTran_HM = null;
		String PLANT = "", FROM_LOC = "", ITEM_NUM = "", TO_LOC = "", TRAN_TYPE = "";
		String LOGIN_USER = "";
		String BATCH = "", Qty = "",INVQTY="",EXPIRYDATE="",REMARKS="";
		int qty = 0;
		int invqty = 0;

		try {
			PLANT = StrUtils.fString(request.getParameter("PLANT"));
			ITEM_NUM = StrUtils.fString(request.getParameter("ITEM_NUM"));
			FROM_LOC = StrUtils.fString(request.getParameter("FROM_LOC"));
			TO_LOC = StrUtils.fString(request.getParameter("TO_LOC"));
			TRAN_TYPE = StrUtils.fString(request.getParameter("TRAN_TYPE"));
			LOGIN_USER = StrUtils.fString(request.getParameter("LOGIN_USER"));

			BATCH = StrUtils.fString(request.getParameter("BATCH"));
			Qty = StrUtils.fString(request.getParameter("QTY"));
			REMARKS = StrUtils.fString(request.getParameter("REMARKS"));
			System.out.println("remarks....."+REMARKS);
			DateUtils _dateUtils = new DateUtils();
			String curDate =_dateUtils.getDate();
			String strTranDate    = curDate.substring(6)+"-"+ curDate.substring(3,5)+"-"+curDate.substring(0,2);
			String strIssueDate    = curDate.substring(0,2)+"/"+ curDate.substring(3,5)+"/"+curDate.substring(6);
		
			double qty1 = Double.parseDouble(Qty);
			qty1 = StrUtils.RoundDB(qty1, IConstants.DECIMALPTS );
			Qty = String.valueOf(qty1);
			List listQry = _InvMstDAO.getLocationTransferBatch(PLANT,
					ITEM_NUM, FROM_LOC, BATCH);
			if (listQry.size() > 0) {
				Map m = (Map) listQry.get(0);
				String sQty = (String) m.get("qty");
								
				
				EXPIRYDATE =  (String) m.get("expiredate");
				INVQTY = sQty;
				}else{
                      throw new Exception("Please check the Batch and Quantity");
               }
			
			locTran_HM = new HashMap<String, String>();
			locTran_HM.put(IConstants.PLANT, PLANT);
			locTran_HM.put(IConstants.ITEM, ITEM_NUM);
			locTran_HM.put(IConstants.LOC, FROM_LOC);
			locTran_HM.put(IConstants.LOC2, TO_LOC);
			locTran_HM.put(IConstants.TRAN_TYPE, TRAN_TYPE);
			locTran_HM.put(IConstants.LOGIN_USER, LOGIN_USER);

			locTran_HM.put(IConstants.BATCH, BATCH);
			locTran_HM.put(IConstants.QTY, Qty);

			locTran_HM.put(IConstants.CUSTOMER_CODE, "");//invMstDAO.getCustCode(PLANT, ITEM_NUM) // Commented by Samatha as Userfld2 is always empty data
			locTran_HM.put(IConstants.EXPIREDATE, EXPIRYDATE);
			locTran_HM.put(IConstants.TRAN_DATE, strTranDate);
			locTran_HM.put(IConstants.REMARKS, REMARKS);
			
			locTran_HM.put("INV_QTY1", "1");
			xmlStr = "";
			Hashtable<String, String> htLocMst = new Hashtable<String, String>();
			htLocMst.put("PLANT", locTran_HM.get(IConstants.PLANT));
			htLocMst.put("loc", locTran_HM.get(IConstants.LOC2));
			_LocMstDAO.setmLogger(mLogger);
			_InvMstUtil.setmLogger(mLogger);
			xmlStr = _InvMstUtil.process_PutAwayPDA(locTran_HM);
			
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
     		if(TRAN_TYPE.equals("PUT_AWAY"))
			{
			xmlStr = XMLUtils.getXMLMessage(0,
					"Error in put away Product : "
							+ locTran_HM.get(IConstants.ITEM)
							+ " to location : "
							+ locTran_HM.get(IConstants.LOC2) + " Error :: "
							+ e.getMessage());
			}
			
			if(TRAN_TYPE.equals("ASSIGN_LOC"))
			{
				xmlStr = XMLUtils.getXMLMessage(0,
					"Error in assign loc Product : "
							+ locTran_HM.get(IConstants.ITEM)
							+ " to location : "
							+ locTran_HM.get(IConstants.LOC2) + " Error :: "
							+ e.getMessage());
			}
 	}

		return xmlStr;
	}*/
	private String getAssignLoc_LocList(HttpServletRequest request,
			HttpServletResponse response) {
		String str = "", aPlant = "", aLoc="",aUser = "";
		try {

			aPlant = StrUtils.fString(request.getParameter("PLANT"));
			aUser = StrUtils.fString(request.getParameter("LOGIN_USER"));
			aLoc = StrUtils.fString(request.getParameter("LOC"));
			_InvMstUtil.setmLogger(mLogger);

			str = _InvMstUtil.loadAssignLocationsXmlPDA(aPlant, aLoc,aUser);

			if (str.equalsIgnoreCase("")) {
				str = XMLUtils.getXMLMessage(1, "No locations found ");
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			str = XMLUtils.getXMLMessage(1, e.getMessage());
		}
		return str;
	}
	
	private String getAssignInvLoc_LocList(HttpServletRequest request,
			HttpServletResponse response) {
		String str = "", aPlant = "", aLoc="",aUser = "",aItem="";
		try {

			aPlant = StrUtils.fString(request.getParameter("PLANT"));
			aUser = StrUtils.fString(request.getParameter("LOGIN_USER"));
			aItem = StrUtils.fString(request.getParameter("ITEM"));
			aLoc = StrUtils.fString(request.getParameter("LOC"));
			_InvMstUtil.setmLogger(mLogger);

			str = _InvMstUtil.loadAssignInvLocationsXmlPDA(aPlant, aLoc,aItem,aUser);

			if (str.equalsIgnoreCase("")) {
				str = XMLUtils.getXMLMessage(1, "No locations found ");
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			str = XMLUtils.getXMLMessage(1, e.getMessage());
		}
		return str;
	}

	private String GenerateTransactionId(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {
		String str = "", aItemNum = "",LOGIN_USER ="";
		try {

			String PLANT = StrUtils.fString(request.getParameter("PLANT"));
			LOGIN_USER = StrUtils.fString(request.getParameter("LOGIN_USER"));
			com.track.dao.TblControlDAO _TblControlDAO=new   com.track.dao.TblControlDAO();
			String genPosTranid = _TblControlDAO.getNextOrder(PLANT,LOGIN_USER,"STOCKMOVE");
			//String genPosTranid = _TblControlDAO.getNextOrder(PLANT,login_user,"GRRECEIPT");

			Hashtable ht = new Hashtable();
			
			xmlStr = XMLUtils.getXMLHeader();
			xmlStr = xmlStr + XMLUtils.getStartNode("itemDetails");
			xmlStr = xmlStr + XMLUtils.getXMLNode("status", "0");
			xmlStr = xmlStr + XMLUtils.getXMLNode("description", "");
			xmlStr = xmlStr + XMLUtils.getXMLNode("TransactionId", genPosTranid);
			xmlStr = xmlStr + XMLUtils.getEndNode("itemDetails");

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
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
		loggerDetailsHasMap.put(MLogger.USER_CODE, userCode);
		return loggerDetailsHasMap;

	}
	public boolean CheckInvMstForGoddsIssue(Map<String, String> map, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		boolean flag = true;
		String extCond = "";
		InvMstDAO _InvMstDAO = new InvMstDAO();
		_InvMstDAO.setmLogger(mLogger);
		Boolean batchChecked = false;
		
		try {
			

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
			actqty = Double.valueOf(actualqty);

			if (request.getParameter("chkbatch") != null) {
				batchChecked = true;
			}

			// Calculate total qty in the loc
			for (int j = 0; j < invlist.size(); j++) {
				Map lineitem = (Map) invlist.get(j);
				String lineitemqty = (String) lineitem.get("qty");				

				totalqty = totalqty + Double.valueOf(lineitemqty);

			}

			if (actqty > totalqty) {
				flag = false;
				String msg = "Not Enough Inventory For Product";
				Exception e = null;
				e.printStackTrace(new PrintWriter(msg));
				throw e;
			}

			if (totalqty == 0) {
				flag = false;
				String msg = "Not Enough Inventory For Product";
				Exception e = null;
				e.printStackTrace(new PrintWriter(msg));
				throw e;
			}

		} catch (Exception e) {
			flag = false;
		}
		return flag;
		}
	//created by vicky Desc:Used for getting All To locations in PDA stock move
	private String getLocationsFromLocationPDA(HttpServletRequest request,
			HttpServletResponse response) {
		String str = "", aPlant = "", aLoc="",aUser = "";
		try {

			aPlant = StrUtils.fString(request.getParameter("PLANT"));
			aUser = StrUtils.fString(request.getParameter("LOGIN_USER"));
			aLoc = StrUtils.fString(request.getParameter("LOC"));
			_InvMstUtil.setmLogger(mLogger);

			str = _InvMstUtil.loadLocationsForPDA(aPlant, aLoc,aUser);

			if (str.equalsIgnoreCase("")) {
				str = XMLUtils.getXMLMessage(1, "No locations found ");
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			str = XMLUtils.getXMLMessage(1, e.getMessage());
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