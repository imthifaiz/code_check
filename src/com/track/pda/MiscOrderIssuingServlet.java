package com.track.pda;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
import com.track.dao.DoDetDAO;
import com.track.dao.DoHdrDAO;
import com.track.dao.IntegrationsDAO;
import com.track.dao.InvMstDAO;
import com.track.dao.ItemMstDAO;
import com.track.dao.MovHisDAO;
import com.track.dao.POSDetDAO;
import com.track.dao.RsnMst;
import com.track.dao.ShipHisDAO;
import com.track.db.util.DOUtil;
import com.track.db.util.EmployeeUtil;
import com.track.db.util.InvMstUtil;
import com.track.db.util.InvUtil;
import com.track.db.util.ItemMstUtil;
import com.track.db.util.MiscIssuingUtil;
import com.track.db.util.RsnMstUtil;
import com.track.db.util.TblControlUtil;
import com.track.db.util.UserLocUtil;
import com.track.gates.DbBean;
import com.track.service.ShopifyService;
import com.track.util.DateUtils;
import com.track.util.IMLogger;
import com.track.util.MLogger;
import com.track.util.StrUtils;
import com.track.util.XMLUtils;

public class MiscOrderIssuingServlet extends HttpServlet implements IMLogger {
	private boolean printLog = MLoggerConstant.MiscOrderIssuingServlet_PRINTPLANTMASTERLOG;
	private boolean printInfo = MLoggerConstant.MiscOrderIssuingServlet_PRINTPLANTMASTERINFO;

	private static final long serialVersionUID = 2858030810385042727L;
	private DOUtil _DOUtil = null;
	private MiscIssuingUtil _MiscIssuingUtil = null;

	private InvMstUtil _InvMstUtil = null;

	private String PLANT = "";
	private String xmlStr = "";
	String action = "";

	private static final String CONTENT_TYPE = "text/xml";

	public void init() throws ServletException {
		_DOUtil = new DOUtil();
		_MiscIssuingUtil = new MiscIssuingUtil();

		_InvMstUtil = new InvMstUtil();
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType(CONTENT_TYPE);
		PrintWriter out = response.getWriter();
		this.setMapDataToLogger(this.populateMapData(StrUtils.fString(request
				.getParameter("PLANT")), StrUtils.fString(request
				.getParameter("LOGIN_USER"))
				+ " PDA_USER"));
		_DOUtil.setmLogger(mLogger);
		_MiscIssuingUtil.setmLogger(mLogger);
		_InvMstUtil.setmLogger(mLogger);
		if (this.printInfo) {
			diaplayInfoLogs(request);
		}
		try {
			action = request.getParameter("action").trim();
			if (action.equalsIgnoreCase("load_all_open_do")) {
				xmlStr = "";
				xmlStr = load_all_open_do(request, response);
			}
			if (action.equalsIgnoreCase("load_all_do_items")) {
				xmlStr = "";
				xmlStr = load_all_do_items(request, response);
			}

			if (action.equalsIgnoreCase("load_lotdetails")) {
				xmlStr = "";
				xmlStr = load_lotdetails(request, response);
			}
			if (action.equalsIgnoreCase("load_item_details")) {
				xmlStr = "";
				xmlStr = load_item_Details(request, response);
			}
		
			if (action.equalsIgnoreCase("load_item_DetailsForBatchWithAvlQty")) {
				xmlStr = "";
				xmlStr = load_item_DetailsForBatchWithAvlQty(request, response);
			}
			
			if (action.equalsIgnoreCase("load_item_Details_Uomspinner")) {
				xmlStr = "";
				xmlStr = load_item_Details_Uomspinner(request, response);
			}
			
			if (action.equalsIgnoreCase("load_item_Details_byexpirydate")) {
				xmlStr = "";
				xmlStr =  load_item_Details_byexpirydate(request, response);
			}
		
			else if (action.equalsIgnoreCase("process_issueMaterial")) {
				xmlStr = "";
				xmlStr = process_orderIssuing(request, response);
			} else if (action.equalsIgnoreCase("validate_data")) {
				xmlStr = "";
				xmlStr = validateData(request, response);
			
			} else if (action.equalsIgnoreCase("validate_dataPDA")) {
				xmlStr = "";
				xmlStr = validateDataPDA(request, response);
			}
		    else if (action.equalsIgnoreCase("validate_NoBatch")) {
                xmlStr = "";
                xmlStr = validateDataPDANOBATCH(request, response);
             }

			else if (action.equalsIgnoreCase("load_reason_codes")) {
				xmlStr = "";
				xmlStr = load_Reason_Codes(request, response);
			}
			else if (action.equalsIgnoreCase("MultipleMiscOrderIssue")) {
				xmlStr = "";
				xmlStr = process_orderMultiIssuingByWMS(request, response);
			}else if (action.equalsIgnoreCase("MiscOrderIssueByRange")) {
				xmlStr = process_orderIssuingByRange(request, response);
			} else if (action.equalsIgnoreCase("GenerateTransactionId")) {
				xmlStr = "";
				xmlStr = GenerateTransactionId(request, response);
			}
			else if (action.equalsIgnoreCase("load_employee_details")) {
				xmlStr = "";
				xmlStr = load_employee_details(request, response);
			}
			else if (action.equalsIgnoreCase("validate_batchscanned_qty")) {
				xmlStr = "";
				xmlStr =validate_batchscanned_qty(request, response);
			}
			else if (action.equalsIgnoreCase("Load_Batch_Details")) {
				xmlStr = "";
				xmlStr =Load_Batch_Details(request, response);
			}
			else if (action.equalsIgnoreCase("Sales_Check_Batch_Validation")) {
				xmlStr = "";
				xmlStr =Sales_Check_Batch_Validation(request, response);
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

	private String validateData(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String str = "", aPlant = "", item = "", batchId = "", loc = "", Qty = "", reasonCode = "", aLoginUser = "";
		try {

			aPlant = StrUtils.fString(request.getParameter("PLANT"));
			aLoginUser = StrUtils.fString(request.getParameter("LOGIN_USER"));
			item = StrUtils.fString(request.getParameter("ITEM"));
			batchId = StrUtils.fString(request.getParameter("BATCH"));
			loc = StrUtils.fString(request.getParameter("LOC"));
			ItemMstDAO itemMstDAO = new ItemMstDAO();
			itemMstDAO.setmLogger(mLogger);
			boolean isValiddata = false;
			if (item.length() > 0) {
				String scannedItemNo = itemMstDAO.getItemIdFromAlternate(
						aPlant, item);

				if (scannedItemNo == null) {
					throw new Exception("Not a Valid Data");
				} else {
					item = scannedItemNo;
				}
			}
			isValiddata = _InvMstUtil.isValidInventoryData(aPlant, item, loc,batchId, Qty, "", aLoginUser);

			if (isValiddata) {
				xmlStr = XMLUtils.getXMLMessage(0, "Valid Data");
			} else {
				xmlStr = XMLUtils.getXMLMessage(1, "Not a Valid Data");
			}

		} catch (Exception e) {
			throw e;
		}

		return xmlStr;
	}
	
	
	private String validateDataPDA(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String str = "", aPlant = "", item = "", batchId = "", loc = "", Qty = "", reasonCode = "", aLoginUser = "";
		try {

			aPlant = StrUtils.fString(request.getParameter("PLANT"));
			aLoginUser = StrUtils.fString(request.getParameter("LOGIN_USER"));
			item = StrUtils.fString(request.getParameter("ITEM"));
			batchId = StrUtils.fString(request.getParameter("BATCH"));
			loc = StrUtils.fString(request.getParameter("LOC"));
			ItemMstDAO itemMstDAO = new ItemMstDAO();
			itemMstDAO.setmLogger(mLogger);
			boolean isValiddata = false;
			if (item.length() > 0) {
				String scannedItemNo = itemMstDAO.getItemIdFromAlternate(
						aPlant, item);

				if (scannedItemNo == null) {
					throw new Exception("Not a Valid Data");
				} else {
					item = scannedItemNo;
				}
			}
			isValiddata = _InvMstUtil.isValidInventoryData(aPlant, item, loc,
					"", Qty, "", aLoginUser);

			if (isValiddata) {
				xmlStr = XMLUtils.getXMLMessage(0, "Valid Data");
			} else {
				xmlStr = XMLUtils.getXMLMessage(1, "Not a Valid Data");
			}

		} catch (Exception e) {
			throw e;
		}

		return xmlStr;
	}
        

    private String validateDataPDANOBATCH(HttpServletRequest request,
                            HttpServletResponse response) throws Exception {
                    String str = "", aPlant = "", item = "", batchId = "", loc = "", Qty = "", reasonCode = "", aLoginUser = "";
                    try {

                            aPlant = StrUtils.fString(request.getParameter("PLANT"));
                            aLoginUser = StrUtils.fString(request.getParameter("LOGIN_USER"));
                            item = StrUtils.fString(request.getParameter("ITEM"));
                            batchId = StrUtils.fString(request.getParameter("BATCH"));
                            loc = StrUtils.fString(request.getParameter("LOC"));
                            ItemMstDAO itemMstDAO = new ItemMstDAO();
                            itemMstDAO.setmLogger(mLogger);
                            boolean isValiddata = false;
                            if (item.length() > 0) {
                                    String scannedItemNo = itemMstDAO.getItemIdFromAlternate(
                                                    aPlant, item);

                                    if (scannedItemNo == null) {
                                            throw new Exception("Not a Valid Data");
                                    } else {
                                            item = scannedItemNo;
                                    }
                            }
                            isValiddata = _InvMstUtil.isValidInventory(aPlant, item, loc,
                                            batchId);

                            if (isValiddata) {
                                    xmlStr = XMLUtils.getXMLMessage(0, "Valid Data");
                            } else {
                                    xmlStr = XMLUtils.getXMLMessage(1, "Not a Valid Data");
                            }

                    } catch (Exception e) {
                            throw e;
                    }

                    return xmlStr;
            }


	private String load_all_open_do(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException,
			Exception {
		String str = "", aPlant = "";
		try {
			// MLogger.log(0, "load_all_open_do() Starts ");
			PLANT = StrUtils.fString(request.getParameter("PLANT"));

			str = _DOUtil.load_all_open_do_details_xml(PLANT);

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
			// MLogger.log(0, "load_all_do_items() Starts ");
			aPlant = StrUtils.fString(request.getParameter("PLANT"));
			aDoNum = StrUtils.fString(request.getParameter("DO_NUM"));

			str = _DOUtil.load_all_do_items_xml(aPlant, aDoNum);

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

			PLANT = StrUtils.fString(request.getParameter("PLANT"));

			str = _DOUtil.load_all_open_do_details_xml(PLANT);

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
		String str = "", aPlant = "", aItem = "", aBatch = "", aLoc = "";
		try {

			aPlant = StrUtils.fString(request.getParameter("PLANT"));
			aItem = StrUtils.fString(request.getParameter("ITEM"));
			aBatch = StrUtils.fString(request.getParameter("BATCH"));
			aLoc = StrUtils.fString(request.getParameter("LOC"));

			str = _InvMstUtil.load_lotdetails_xml(aPlant, aItem, aBatch, aLoc,
					"");

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

	/* ************Modification History*********************************
	   Oct 17 2014 Bruhan, Description: To include Issue date
	*/
	private String process_orderIssuing(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException,
			Exception {

		Map issMat_HM = null;
		String PLANT = "", DO_NUM = "", PO_LN_NUM = "", ITEM_NUM = "",ISNONSTKFLG="";
		String ITEM_DESCRIPTION = "", LOGIN_USER = "",REMARKS="";
		String ITEM_QTY = "", ITEM_BATCH = "", ITEM_LOC = "", RSNDESC = "", USERID = "",TRANSACTIONID="",INVID="",EMPNO="", UOM = "";

		try {
			PLANT = StrUtils.fString(request.getParameter("PLANT"));
			USERID = StrUtils.fString(request.getParameter("LOGIN_USER"));
			ITEM_NUM = StrUtils.fString(request.getParameter("ITEM_NUM"));
			LOGIN_USER = StrUtils.fString(request.getParameter("LOGIN_USER"));
			ITEM_QTY = StrUtils.removeFormat(StrUtils.fString(request.getParameter("ITEM_QTY")));
			ITEM_BATCH = StrUtils.fString(request.getParameter("ITEM_BATCH"));
			ITEM_LOC = StrUtils.fString(request.getParameter("ITEM_LOC"));
			RSNDESC = StrUtils.fString(request.getParameter("RSNDESC"));
			RSNDESC = StrUtils.replaceCharacters2Recv(RSNDESC);
			REMARKS = StrUtils.fString(request.getParameter("REMARKS"));
			TRANSACTIONID=StrUtils.fString(request.getParameter("ORDERNO"));
			INVID=StrUtils.fString(request.getParameter("INVID"));
			EMPNO=StrUtils.fString(request.getParameter("EMPNO"));
			UOM=StrUtils.fString(request.getParameter("UOM"));
			
			DateUtils _dateUtils = new DateUtils();
			String curDate =_dateUtils.getDate();
			String strTranDate    = curDate.substring(6)+"-"+ curDate.substring(3,5)+"-"+curDate.substring(0,2);
			String strIssueDate    = curDate.substring(0,2)+"/"+ curDate.substring(3,5)+"/"+curDate.substring(6);
			double doqty = Double.parseDouble(ITEM_QTY);
			doqty =  StrUtils.RoundDB(doqty, IConstants.DECIMALPTS);
			ITEM_QTY = String.valueOf(doqty);
			ITEM_QTY = StrUtils.formatThreeDecimal(ITEM_QTY);  
			ISNONSTKFLG = new ItemMstDAO().getNonStockFlag(PLANT, ITEM_NUM);
			issMat_HM = new HashMap();
			issMat_HM.put(IConstants.PLANT, PLANT);
			issMat_HM.put(IConstants.ITEM, ITEM_NUM);
		    issMat_HM.put(IConstants.LOC, ITEM_LOC);
			issMat_HM.put(IConstants.LOGIN_USER, LOGIN_USER);
			issMat_HM.put(IConstants.INV_EXP_DATE, "");
			issMat_HM.put(IConstants.QTY, ITEM_QTY);
			issMat_HM.put(IConstants.BATCH, ITEM_BATCH);
			issMat_HM.put(IConstants.REMARKS, REMARKS);
			issMat_HM.put(IConstants.RSNDESC, RSNDESC);
			issMat_HM.put("NONSTKFLAG", ISNONSTKFLG);
			issMat_HM.put(IConstants.TRAN_DATE, strTranDate);
			issMat_HM.put(IConstants.ISSUEDATE, strIssueDate);
			issMat_HM.put(IConstants.DONO, TRANSACTIONID);
			issMat_HM.put(IConstants.INVID, INVID);
			issMat_HM.put(IConstants.EMPNO, EMPNO);
			issMat_HM.put(IConstants.BATCH_ID, INVID);
			issMat_HM.put(IConstants.UNITMO, UOM);
			
			//Get Item Count
			POSDetDAO posdetDAO = new POSDetDAO();			
			posdetDAO.setmLogger(mLogger);
			List prdlist = posdetDAO.listProductsForPOSTranID(PLANT, TRANSACTIONID, " (TRANSTATUS <> '')");
			int gcount=prdlist.size()+1;
			issMat_HM.put(IConstants.DODET_DOLNNO, String.valueOf(gcount));
			
			//Check Stock
			Hashtable ht1 = new Hashtable();
			ht1.put(IConstants.PLANT, PLANT);
			ht1.put(IConstants.LOGIN_USER, LOGIN_USER);
			ht1.put(IConstants.LOC, ITEM_LOC);
			ht1.put(IConstants.ITEM, ITEM_NUM);
			ht1.put(IConstants.BATCH, ITEM_BATCH);
			String UOMQTY="1";
			Hashtable<String, String> htTrand1 = new Hashtable<String, String>();
			MovHisDAO movHisDao1 = new MovHisDAO();
			ArrayList getuomqty = movHisDao1.selectForReport("select ISNULL(QPUOM,1) as UOMQTY from "+PLANT+"_UOM where UOM='"+UOM+"'",htTrand1);
			if(getuomqty.size()>0)
			{
			Map mapval = (Map) getuomqty.get(0);
			UOMQTY=(String)mapval.get("UOMQTY");
			}
			issMat_HM.put("UOMQTY", UOMQTY);
			double invumqty = Double.valueOf(ITEM_QTY) * Double.valueOf(UOMQTY);			
			ht1.put(IConstants.QTY, String.valueOf(invumqty));
			boolean flag = true;
			if(!ISNONSTKFLG.equalsIgnoreCase("Y"))
			{
				flag = CheckInvMstForGoddsIssue(ht1, request, response);
				if(!flag)
				{
					xmlStr = XMLUtils.getXMLMessage(0,
	 						"Not Enough Inventory For Product : " + ITEM_NUM);
					throw new Exception(" Not Enough Inventory For Product ");
				}
			}
				if (flag) {
				
			xmlStr = _MiscIssuingUtil.process_MiscIssueMaterial(issMat_HM);
			
				} else {
					throw new Exception(" Issuing is not successfull ");
				}
				if(xmlStr.contains("Issued successfully!"))
				{
					DoDetDAO _DoDetDAO = new DoDetDAO();
					DateUtils dateutils = new DateUtils();
					ShipHisDAO _ShipHisDAO = null;
					_ShipHisDAO = new ShipHisDAO();
					flag=_ShipHisDAO.isExisit("select count(*) from "+PLANT+"_FINGINOTOINVOICE where  GINO='"+TRANSACTIONID+"'");
					if (flag) {
						MovHisDAO movHisDao = new MovHisDAO();
						Hashtable htRecvDet = new Hashtable();
						htRecvDet.clear();
						htRecvDet.put(IConstants.PLANT, PLANT);
						htRecvDet.put("GINO", TRANSACTIONID);
						
						
						flag=_DoDetDAO.updateGINOtoInvoice(" set QTY+="+ITEM_QTY,htRecvDet,"",PLANT);
	        			
						Hashtable htRecvHis = new Hashtable();
	        			htRecvHis.clear();
	        			htRecvHis.put(IDBConstants.PLANT, PLANT);
	        			htRecvHis.put("DIRTYPE", TransactionConstants.CREATE_GINO);       					
	        			htRecvHis.put(IDBConstants.MOVHIS_ORDNUM, TRANSACTIONID);
						flag=movHisDao.updateMovHis(" set QTY+="+ITEM_QTY,htRecvHis,"",PLANT);
						
					}else{
				new TblControlUtil().updateTblControlIESeqNo(PLANT, "GINO", "GI",TRANSACTIONID);
				  Hashtable	ht=new Hashtable();
				  ht.clear();
				  ht.put(IConstants.PLANT,PLANT);
	              ht.put("GINO",TRANSACTIONID);
	              ht.put(IConstants.CUSTOMER_CODE, "");
	              ht.put(IConstants.DODET_DONUM,"");
	              ht.put(IConstants.STATUS, "NON INVOICEABLE");
	              ht.put(IConstants.AMOUNT, "");
	              ht.put(IConstants.QTY, ITEM_QTY);
	              ht.put("CRAT",dateutils.getDateTime());
	              ht.put("CRBY",LOGIN_USER);
	              ht.put("UPAT",dateutils.getDateTime());
	               _DoDetDAO.insertGINOtoInvoice(ht);
	              
	              //insert MovHis
	                MovHisDAO movHisDao = new MovHisDAO();
	        		movHisDao.setmLogger(mLogger);
	    			Hashtable htRecvHis = new Hashtable();
	    			htRecvHis.clear();
	    			htRecvHis.put(IDBConstants.PLANT, PLANT);
	    			htRecvHis.put("DIRTYPE", TransactionConstants.CREATE_GINO);
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
			// this.mLogger.exception(this.printLog, "", e);
			throw e;
		}

		return xmlStr;
	}


	private String process_orderMultiIssuingByWMS(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException,
			Exception {

		Map issMat_HM = null;

		String PLANT = "", USERID = "", DO_NUM = "", PO_LN_NUM = "", ITEM_NUM = "",ISNONSTKFLG="";
		String ITEM_DESCRIPTION = "", LOGIN_USER = "",REMARKS="";
		String DO_QTY = "", INV_QTY = "", DO_BATCH = "", LOC = "", 
				RSNDESC = "", ITEM_EXPDATE = ""	,TRANSACTIONDATE = "",strMovHisTranDate="",strTranDate="";
		UserTransaction ut = null;
		try {
			HttpSession session = request.getSession();
			PLANT = StrUtils.fString((String) session.getAttribute("PLANT"));
			USERID = StrUtils.fString((String) session.getAttribute("LOGIN_USER"));
			LOGIN_USER = StrUtils.fString((String) session.getAttribute("LOGIN_USER"));
			Boolean transactionHandler = true;
			ut = DbBean.getUserTranaction();
			ut.begin();
			String DYNAMIC_ISSUING_SIZE =StrUtils.fString(request.getParameter("DYNAMIC_ISSUING_SIZE")); 
			int DYNAMIC_ISSUING_SIZE_CNT = Integer.parseInt(DYNAMIC_ISSUING_SIZE);
		for(int index=0;index<DYNAMIC_ISSUING_SIZE_CNT;index++){
			ITEM_NUM = StrUtils.fString(request.getParameter("ITEMNO"+"_"+index));
			ITEM_DESCRIPTION = StrUtils.fString(request.getParameter("ITEMDESC"+"_"+index));
			ISNONSTKFLG = new ItemMstDAO().getNonStockFlag(PLANT, ITEM_NUM);
			INV_QTY = StrUtils.fString(request.getParameter("INVQTY"+"_"+index));
			DO_QTY = StrUtils.fString(request.getParameter("QTY"+"_"+index));
			DO_BATCH = StrUtils.fString(request.getParameter("BATCH"+"_"+index));
			LOC = StrUtils.fString(request.getParameter("LOC"+"_"+index));
			RSNDESC = StrUtils.fString(request.getParameter("REASONCODE"+"_"+index));
			ITEM_EXPDATE = StrUtils.fString(request.getParameter("REFDET"+"_"+index));
			REMARKS = StrUtils.fString(request.getParameter("REFDET"+"_"+index));
			 TRANSACTIONDATE = StrUtils.fString(request.getParameter("TRANSACTIONDATE"));
			if (TRANSACTIONDATE.length()>5)
				strMovHisTranDate    = TRANSACTIONDATE.substring(6)+"-"+TRANSACTIONDATE.substring(3,5)+"-"+TRANSACTIONDATE.substring(0,2);
			    strTranDate    = TRANSACTIONDATE.substring(0,2)+"/"+ TRANSACTIONDATE.substring(3,5)+"/"+TRANSACTIONDATE.substring(6);
			double scanqty=0;
			scanqty = Double.parseDouble(DO_QTY);
			scanqty = StrUtils.RoundDB(scanqty, IConstants.DECIMALPTS);
			DO_QTY = String.valueOf(scanqty);
			DO_QTY = StrUtils.formatThreeDecimal(DO_QTY);  
			ItemMstUtil itemMstUtil = new ItemMstUtil();
			itemMstUtil.setmLogger(mLogger);
			ITEM_NUM = itemMstUtil.isValidAlternateItemInItemmst(PLANT,ITEM_NUM);
			issMat_HM = new HashMap();
			issMat_HM.put(IConstants.PLANT, PLANT);
			issMat_HM.put(IConstants.ITEM, ITEM_NUM);
			issMat_HM.put(IConstants.DODET_DONUM, DO_NUM);
			issMat_HM.put(IConstants.DODET_DOLNNO, PO_LN_NUM);
			issMat_HM.put(IConstants.LOC, LOC);
			issMat_HM.put(IConstants.LOGIN_USER, LOGIN_USER);
			issMat_HM.put(IConstants.INV_EXP_DATE, ITEM_EXPDATE);
			issMat_HM.put(IConstants.CUSTOMER_CODE, _DOUtil.getCustCode(PLANT, DO_NUM));
			// getting job No for do
			issMat_HM.put(IConstants.JOB_NUM, _DOUtil.getJobNum(PLANT,DO_NUM));
			issMat_HM.put(IConstants.QTY, DO_QTY);
			issMat_HM.put(IConstants.BATCH, DO_BATCH);
			issMat_HM.put(IConstants.REMARKS, REMARKS);
			issMat_HM.put(IConstants.RSNDESC, RSNDESC);
			issMat_HM.put("NONSTKFLAG", ISNONSTKFLG);
			issMat_HM.put(IConstants.TRAN_DATE, strMovHisTranDate);
			issMat_HM.put(IConstants.ISSUEDATE, strTranDate);
			xmlStr = "";
			RsnMst rsnMst = new RsnMst();
			rsnMst.setmLogger(mLogger);
			Hashtable htRsnMst = new Hashtable();
			htRsnMst.put(IConstants.PLANT, PLANT);
			htRsnMst.put(IConstants.RSNCODE, RSNDESC);
			if (!rsnMst.isExists(htRsnMst)) {
				throw new Exception("Invalid Reason Code!");
			}
			boolean flag = false;

			if (!flag) {
				transactionHandler = _MiscIssuingUtil.process_MultiMiscIssueMaterial(issMat_HM);
				
			} else {
				transactionHandler = transactionHandler&&false;
				
			}
			}
			if(transactionHandler==true)
			{	DbBean.CommitTran(ut);
				request.getSession().setAttribute("RESULT",
						"Products : "  + "Issued successfully!");
				response.sendRedirect("jsp/MultiMiscOrderIssuing.jsp?action=result");
			}
			else
			{
				DbBean.RollbackTran(ut);
				request.getSession().setAttribute("RESSULTERROR",
						"Error in issuing Products: " );
				response
						.sendRedirect("jsp/MultiMiscOrderIssuing.jsp?action=resulterror&ITEMNO="
								+ ITEM_NUM
								+ "&ITEMDESC="
								+ ITEM_DESCRIPTION
								+ "&LOC="
								+ LOC
								+ "&BATCH="
								+ DO_BATCH
								+ "&REFDET="
								+ ITEM_EXPDATE
								+ "&INVQTY="
								+ INV_QTY
								+ "&QTY="
								+ DO_QTY
								+ "&REASONCODE="
								+ RSNDESC);

			}
	
		}
		catch (Exception e) {
			DbBean.RollbackTran(ut);
			this.mLogger.exception(this.printLog, "", e);
			request.getSession().setAttribute("CATCHERROR",
					"Error:" + e.getMessage());
			response
					.sendRedirect("jsp/MultiMiscOrderIssuing.jsp?action=catcherror&ITEMNO="
							+ ITEM_NUM
							+ "&ITEMDESC="
							+ ITEM_DESCRIPTION
							+ "&LOC="
							+ LOC
							+ "&BATCH="
							+ DO_BATCH
							+ "&REFDET="
							+ ITEM_EXPDATE
							+ "&INVQTY="
							+ INV_QTY
							+ "&QTY="
							+ DO_QTY + "&REASONCODE=" + RSNDESC);

			throw e;
		}
	
		return xmlStr;
	}
        
	/* ************Modification History*********************************
	   Oct 15 2014 Bruhan, Description: To include Transaction date
	*/   
    private String process_orderIssuingByRange(HttpServletRequest request,
                    HttpServletResponse response) throws IOException, ServletException,
                    Exception {

            Map issMat_HM = null;

            String PLANT = "", USERID = "", DO_NUM = "", PO_LN_NUM = "", ITEM_NUM = "",ISNONSTKFLG="";
            String ITEM_DESCRIPTION = "", LOGIN_USER = "";
            String DO_QTY = "", INV_QTY = "", SUFFIX = "",DO_BATCH="",DTFRMT="", LOC = "", 
					SRANGE="",ERANGE="",RSNDESC = "", ITEM_EXPDATE = "",TRANSACTIONDATE = "",strMovHisTranDate="",strTranDate="",REMARKS="";
            double pickingQty = 0,totalqty=0;
            String alertitem="";
            //UserTransaction ut = null;
            try {
                    HttpSession session = request.getSession();
                    PLANT = StrUtils.fString((String) request.getSession().getAttribute("PLANT"));
                    String ISINVENTORYMINQTY = new DoHdrDAO().getISINVENTORYMINQTY(PLANT);//Thanzi
                    USERID = StrUtils.fString((String) session.getAttribute("LOGIN_USER"));
                    DO_NUM = StrUtils.fString(request.getParameter("TRANID"));
                    ITEM_NUM = StrUtils.fString(request.getParameter("ITEMNO"));
                    ITEM_DESCRIPTION = StrUtils.fString(request.getParameter("ITEMDESC"));
                    LOGIN_USER = StrUtils.fString((String) session.getAttribute("LOGIN_USER"));

                    INV_QTY = StrUtils.fString(request.getParameter("INVQTY"));
                    DO_QTY = StrUtils.fString(request.getParameter("QTY"));
                
                    SUFFIX = StrUtils.fString(request.getParameter("SUFFIX"));
                    DTFRMT = StrUtils.fString(request.getParameter("DTFRMT"));
                    SRANGE = StrUtils.fString(request.getParameter("SRANGE"));
                    ERANGE = StrUtils.fString(request.getParameter("ERANGE"));
                    long rangeSize = Long.parseLong(ERANGE)-Long.parseLong(SRANGE);
                    rangeSize = rangeSize+1;
          
                    LOC = StrUtils.fString(request.getParameter("LOC"));
                    RSNDESC = StrUtils.fString(request.getParameter("REASONCODE"));
                    ITEM_EXPDATE = StrUtils.fString(request.getParameter("REFDET"));
					TRANSACTIONDATE = StrUtils.fString(request.getParameter("TRANSACTIONDATE"));
        			if (TRANSACTIONDATE.length()>5)
        				strMovHisTranDate    = TRANSACTIONDATE.substring(6)+"-"+TRANSACTIONDATE.substring(3,5)+"-"+TRANSACTIONDATE.substring(0,2);
        			    strTranDate    = TRANSACTIONDATE.substring(0,2)+"/"+ TRANSACTIONDATE.substring(3,5)+"/"+TRANSACTIONDATE.substring(6);
        			    
        			    DateUtils dateUtils = new DateUtils();
        				String fromdates = dateUtils.parseDateddmmyyyy(strTranDate);
        				String time = DateUtils.getTimeHHmm();
        				String orderdate = fromdates+time+"12";
        				LocalDate dates = LocalDate.parse(fromdates, DateTimeFormatter.BASIC_ISO_DATE);
        				String formattedDate = dates.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        				String tran = formattedDate;
        				
                    ItemMstUtil itemMstUtil = new ItemMstUtil();
                    itemMstUtil.setmLogger(mLogger);
                    ITEM_NUM = itemMstUtil.isValidAlternateItemInItemmst(PLANT, ITEM_NUM);
                    ISNONSTKFLG = new ItemMstDAO().getNonStockFlag(PLANT, ITEM_NUM);
                    REMARKS = StrUtils.fString(request.getParameter("REFDET"));
                    String UOM = StrUtils.fString(request.getParameter("UOM")).trim();
                    UserLocUtil uslocUtil = new UserLocUtil();
                    uslocUtil.setmLogger(mLogger);
                    boolean isvalidlocforUser = uslocUtil.isValidLocInLocmstForUser(PLANT, LOGIN_USER, LOC);
                    if (!isvalidlocforUser) {
                            throw new Exception(" Loc :" + LOC + " is not User Assigned Location/valid location");
                    }
                    double scanqty =0;
                    scanqty = Double.parseDouble(DO_QTY);
    				scanqty = StrUtils.RoundDB(scanqty, IConstants.DECIMALPTS);
    				DO_QTY = String.valueOf(scanqty);
    			DO_QTY = StrUtils.formatThreeDecimal(DO_QTY);  	
                    RsnMst rsnMst = new RsnMst();
                    rsnMst.setmLogger(mLogger);
                    Hashtable htRsnMst = new Hashtable();
                    htRsnMst.put(IConstants.PLANT, PLANT);
                    htRsnMst.put(IConstants.RSNCODE, RSNDESC);
                    if (!rsnMst.isExists(htRsnMst)) {
                            throw new Exception("Invalid Reason Code!");
                    }
                Boolean transactionHandler = false;
                //ut = DbBean.getUserTranaction();
                //ut.begin();
                long SerialNo = 0;
                com.track.dao.TblControlDAO _TblControlDAO = new com.track.dao.TblControlDAO();
				_TblControlDAO.setmLogger(mLogger);
			//	DO_NUM = _TblControlDAO.getNextOrder(PLANT, USERID, "GIRECEIPT");
                for(int i=0;i<rangeSize;i++){
                	
                    SerialNo= Long.parseLong(SRANGE)+i;
                    DO_BATCH =SUFFIX+DTFRMT+Long.toString(SerialNo);
                    pickingQty = Double.parseDouble(((String) DO_QTY.trim().toString()));
                    totalqty=totalqty+pickingQty;
                    
                    issMat_HM = new HashMap();
                    issMat_HM.put(IConstants.PLANT, PLANT);
                    issMat_HM.put(IConstants.ITEM, ITEM_NUM);
                    PO_LN_NUM=String.valueOf(i+1);
                    issMat_HM.put(IConstants.DODET_DONUM, DO_NUM);
                    issMat_HM.put(IConstants.DODET_DOLNNO, PO_LN_NUM);
                    issMat_HM.put(IConstants.LOC, LOC);
                    issMat_HM.put(IConstants.LOGIN_USER, LOGIN_USER);
                    issMat_HM.put(IConstants.INV_EXP_DATE, ITEM_EXPDATE);
                    issMat_HM.put(IConstants.CUSTOMER_CODE, _DOUtil.getCustCode(PLANT, DO_NUM));
                    // getting job No for do
                    issMat_HM.put(IConstants.JOB_NUM, _DOUtil.getJobNum(PLANT,DO_NUM));
                    issMat_HM.put(IConstants.QTY, DO_QTY);
                    issMat_HM.put(IConstants.BATCH, DO_BATCH);
                    issMat_HM.put(IConstants.RSNDESC, RSNDESC);
                    issMat_HM.put("NONSTKFLAG", ISNONSTKFLG);
					issMat_HM.put(IConstants.TRAN_DATE, strMovHisTranDate);
        			issMat_HM.put(IConstants.ISSUEDATE, strTranDate);
        			issMat_HM.put(IConstants.REMARKS, REMARKS);
        			issMat_HM.put(IConstants.UNITMO, UOM);
        			issMat_HM.put(IConstants.EMPNO, "");
        			String UOMQTY="1";
    				Hashtable<String, String> htTrand1 = new Hashtable<String, String>();
    				MovHisDAO movHisDao1 = new MovHisDAO();
    				ArrayList getuomqty = movHisDao1.selectForReport("select ISNULL(QPUOM,1) as UOMQTY from "+PLANT+"_UOM where UOM='"+UOM+"'",htTrand1);
    				if(getuomqty.size()>0)
    				{
    				Map mapval = (Map) getuomqty.get(0);
    				UOMQTY=(String)mapval.get("UOMQTY");
    				}
    				issMat_HM.put("UOMQTY", UOMQTY);
    				
    				List listQry = new InvMstDAO().getOutBoundPickingBatchByWMS(PLANT,ITEM_NUM, LOC, DO_BATCH);
					double invqty = 0;
                    double Detuctqty = 0;
					double STKQTY = 0;
					if (listQry.size() > 0) {
						for (int z = 0; z < listQry.size(); z++) {
							Map m = (Map) listQry.get(z);
							String ITEM_QTY = (String) m.get("qty");
			                String MINSTKQTY = (String) m.get("MINSTKQTY");
							invqty += Double.parseDouble(((String) ITEM_QTY.trim()));
							STKQTY = Double.parseDouble(((String) MINSTKQTY.trim()));
						}
						if(STKQTY!=0) {
						Detuctqty = invqty-pickingQty;
						if(STKQTY>Detuctqty) {
							if(alertitem.equalsIgnoreCase("")) {
								alertitem =ITEM_NUM;
							}else {
								alertitem = alertitem+" , "+ITEM_NUM;
							}
						}
							
						}
						
						if(ISINVENTORYMINQTY.equalsIgnoreCase("1")) 
							alertitem = alertitem;
						else 
							alertitem="";
					}
    				
    				if(!ISNONSTKFLG.equalsIgnoreCase("Y"))
    				{
        			ArrayList al = _InvMstUtil.getItemList(PLANT, ITEM_NUM, "USERFLD4='" + DO_BATCH + "'");
        			if (al.isEmpty()) {
        				throw new Exception("Stock does not exist for the item : " + ITEM_NUM + " and batch : " + DO_BATCH);
        			}
        			System.out.println("Checking stock for batch : " + DO_BATCH);
        			issMat_HM.put(IConstants.INVID, ((Map)al.get(0)).get(IConstants.INVID));
    				}
                    xmlStr = "";
                            transactionHandler = _MiscIssuingUtil.process_MiscIssueMaterialByRange(issMat_HM);
                            
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
                   
                   if(transactionHandler){
                	   
      					 DoDetDAO _DoDetDAO = new DoDetDAO();
      						DateUtils dateutils = new DateUtils();
      					  Hashtable	ht=new Hashtable();
      					  ht.clear();
      					  ht.put(IConstants.PLANT,PLANT);
      	                  ht.put("GINO",DO_NUM);
      	                  ht.put(IConstants.CUSTOMER_CODE, "");
      	                  ht.put(IConstants.DODET_DONUM,"");
      	                  ht.put(IConstants.STATUS, "NON INVOICEABLE");
      	                  ht.put(IConstants.AMOUNT, "");
      	                  ht.put(IConstants.QTY,String.valueOf(totalqty));
      	                  ht.put("CRAT",dateutils.getDateTime());
      	                  ht.put("CRBY",LOGIN_USER);
      	                  ht.put("UPAT",dateutils.getDateTime());
      					
      	                transactionHandler = _DoDetDAO.insertGINOtoInvoice(ht);
      	              //insert MovHis
      	                MovHisDAO movHisDao = new MovHisDAO();
      	        		movHisDao.setmLogger(mLogger);
      	    			Hashtable htRecvHis = new Hashtable();
      	    			htRecvHis.clear();
      	    			htRecvHis.put(IDBConstants.PLANT, PLANT);
      	    			htRecvHis.put("DIRTYPE", TransactionConstants.CREATE_GINO);
      	    			htRecvHis.put(IDBConstants.ITEM, "");
      	    			htRecvHis.put("MOVTID", "");
      	    			htRecvHis.put("RECID", "");
      	    			htRecvHis.put(IConstants.QTY, String.valueOf(totalqty));
      	    			htRecvHis.put(IDBConstants.CREATED_BY, LOGIN_USER);
      	    			htRecvHis.put(IDBConstants.REMARKS, "");        					
      	    			htRecvHis.put(IDBConstants.TRAN_DATE, dateutils.getDateinyyyy_mm_dd(dateutils.getDate()));
      	    			htRecvHis.put(IDBConstants.MOVHIS_ORDNUM, DO_NUM);
      	    			htRecvHis.put(IDBConstants.CREATED_AT, dateutils.getDateTime());
      	    			transactionHandler = movHisDao.insertIntoMovHis(htRecvHis);
      	    			
      	    			new TblControlUtil().updateTblControlIESeqNo(PLANT, "GINO", "GI", DO_NUM);
                            //DbBean.CommitTran(ut);
//                            request.getSession().setAttribute("RESULT", "Product : " + ITEM_NUM + " " + "issued successfully!");
                            if(alertitem.equalsIgnoreCase("")) {
                            	request.getSession().setAttribute("RESULT", "Product : " + ITEM_NUM + " " + "issued successfully!");
        					}else {
        						request.getSession().setAttribute("RESULT", "Product : " + ITEM_NUM + " " + "issued successfully! <br><span style=\"color: orange;\">Product  ["+alertitem+"] reached the minimun Inventory Quantity</span>");
        					}
                            
                            response.sendRedirect("../salestransaction/goodsissueserial?action=result");
                    } else {
                            //DbBean.RollbackTran(ut);
                            request.getSession().setAttribute("RESSULTERROR",  "Error in issuing Product: " + ITEM_NUM);
                            response.sendRedirect("../salestransaction/goodsissueserial?action=resulterror&ITEMNO="
                                                            + ITEM_NUM
                                                            + "&ITEMDESC="
                                                            + ITEM_DESCRIPTION
                                                            + "&LOC="
                                                            + LOC
                                                            + "&REFDET="
                                                            + ITEM_EXPDATE
                                                            + "&INVQTY="
                                                            + INV_QTY
                                                            + "&QTY="
                                                            + DO_QTY
                                                            + "&REASONCODE="
                                                            + RSNDESC+ "&SRANGE=" + SRANGE+ "&ERANGE=" + ERANGE+ "&SUFFIX=" + SUFFIX+ "&DTFRMT=" + DTFRMT);

                    }

            }

            catch (Exception e) {
                //DbBean.RollbackTran(ut);
                    this.mLogger.exception(this.printLog, "", e);
                    request.getSession().setAttribute("CATCHERROR","Error:" + e.getMessage());
                    response
                                    .sendRedirect("../salestransaction/goodsissueserial?action=catcherror&ITEMNO="
                                                    + ITEM_NUM
                                                    + "&ITEMDESC="
                                                    + ITEM_DESCRIPTION
                                                    + "&LOC="
                                                    + LOC
                                                    + "&REFDET="
                                                    + ITEM_EXPDATE
                                                    + "&INVQTY="
                                                    + INV_QTY
                                                    + "&QTY="
                                                    + DO_QTY + "&REASONCODE=" + RSNDESC+ "&SRANGE=" + SRANGE+ "&ERANGE=" + ERANGE+ "&SUFFIX=" + SUFFIX+ "&DTFRMT=" + DTFRMT);

                    throw e;
            }
            return xmlStr;
    }


	private String load_Reason_Codes(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {
		String str = "";
		RsnMstUtil rsnUtil = new RsnMstUtil();
		rsnUtil.setmLogger(mLogger);
		try {

			PLANT = StrUtils.fString(request.getParameter("PLANT"));

			str = rsnUtil.getReasonCode(PLANT);

			if (str.equalsIgnoreCase("")) {

				str = XMLUtils.getXMLMessage(1, "Details not found");
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		}

		return str;
	}

	private String load_item_Details(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {
		String str = "", aPlant = "", aItemNum = "", aLoc = "",aBatch="",UOM="";
		try {

			PLANT = StrUtils.fString(request.getParameter("PLANT"));
			String userID = StrUtils
					.fString(request.getParameter("LOGIN_USER"));
			aItemNum = StrUtils.fString(request.getParameter("ITEM_NUM"));
			aLoc = StrUtils.fString(request.getParameter("LOC"));
			aBatch= StrUtils.fString(request.getParameter("BATCH"));
			UOM=StrUtils.fString(request.getParameter("UOM"));
			str = _InvMstUtil.getMutiUombatchDetails(PLANT, aItemNum, aLoc, userID,aBatch,UOM);

			if (str.equalsIgnoreCase("")) {
				str = XMLUtils.getXMLMessage(1, "Batch detail not found");
			}

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			str = XMLUtils.getXMLMessage(1, e.getMessage());
		}

		return str;
	}
	
	private String load_item_DetailsForBatchWithAvlQty(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {
		String str = "", aPlant = "", aItemNum = "", aLoc = "",aBatch="",UOM="";
		try {

			PLANT = StrUtils.fString(request.getParameter("PLANT"));
			String userID = StrUtils
					.fString(request.getParameter("LOGIN_USER"));
			aItemNum = StrUtils.fString(request.getParameter("ITEM_NUM"));
			aLoc = StrUtils.fString(request.getParameter("LOC"));
			aBatch= StrUtils.fString(request.getParameter("BATCH"));
			UOM=StrUtils.fString(request.getParameter("UOM"));
			str = _InvMstUtil.getMutiUombatchDetailsForBatchAvlQty(PLANT, aItemNum, aLoc, userID,aBatch,UOM);

			if (str.equalsIgnoreCase("")) {
				str = XMLUtils.getXMLMessage(1, "Batch detail not found");
			}

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			str = XMLUtils.getXMLMessage(1, e.getMessage());
		}

		return str;
	}
	
	private String load_item_Details_Uomspinner(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {
		String str = "", aPlant = "", aItemNum = "", aLoc = "",aBatch="",UOM="";
		try {

			PLANT = StrUtils.fString(request.getParameter("PLANT"));
			String userID = StrUtils
					.fString(request.getParameter("LOGIN_USER"));
			aItemNum = StrUtils.fString(request.getParameter("ITEM_NUM"));
			aLoc = StrUtils.fString(request.getParameter("LOC"));
			aBatch= StrUtils.fString(request.getParameter("BATCH"));
			UOM=StrUtils.fString(request.getParameter("UOM"));
			str = _InvMstUtil.getMutiUombatchDetailsForSpinner(PLANT, aItemNum, aLoc, userID,aBatch,UOM);

			if (str.equalsIgnoreCase("")) {
				str = XMLUtils.getXMLMessage(1, "Batch detail not found");
			}

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			str = XMLUtils.getXMLMessage(1, e.getMessage());
		}

		return str;
	}
	
	private String load_item_Details_byexpirydate(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {
		String str = "", aPlant = "", aItemNum = "", aLoc = "",aBatch="";
		try {

			PLANT = StrUtils.fString(request.getParameter("PLANT"));
			String userID = StrUtils
					.fString(request.getParameter("LOGIN_USER"));
			aItemNum = StrUtils.fString(request.getParameter("ITEM_NUM"));
			aLoc = StrUtils.fString(request.getParameter("LOC"));
			aBatch= StrUtils.fString(request.getParameter("BATCH"));

			str = _InvMstUtil.getbatchDetailsbyexpirydate(PLANT, aItemNum, aLoc, userID,aBatch);

			if (str.equalsIgnoreCase("")) {
				str = XMLUtils.getXMLMessage(1, "Item details not found?");
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

			PLANT = StrUtils.fString(request.getParameter("PLANT"));
			LOGIN_USER = StrUtils.fString(request.getParameter("LOGIN_USER"));
			com.track.dao.TblControlDAO _TblControlDAO=new   com.track.dao.TblControlDAO();
			//String genPosTranid = _TblControlDAO.getNextOrder(PLANT,LOGIN_USER,"GIRECEIPT");
			//String genPosTranid = _TblControlDAO.getNextOrder(PLANT,login_user,"GRRECEIPT");
			String genPosTranid = _TblControlDAO.getNextOrder(PLANT,LOGIN_USER,"GINO");
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
	
	private String load_employee_details(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {
		String str = "";
		EmployeeUtil emoloyeeUtil = new EmployeeUtil();
		emoloyeeUtil.setmLogger(mLogger);
		try {

			PLANT = StrUtils.fString(request.getParameter("PLANT"));
			String loginuser = StrUtils.fString(request.getParameter("LOGIN_USER"));
			String EMPNO = StrUtils.fString(request.getParameter("EMPNO"));
			
			str = emoloyeeUtil.getPDAEmployeeDetails(PLANT,EMPNO);

			if (str.equalsIgnoreCase("")) {

				str = XMLUtils.getXMLMessage(1, "Details not found");
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		}

		return str;
	}
	
	private String validate_batchscanned_qty(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String str = "", aPlant = "", item = "", ITEM_BATCH = "", LOC = "", Qty = "", aLoginUser = "",
				ISNONSTKFLG="",ITEM_QTY="",INVID="";
		double scannedqty = 0;
		try {
			InvMstDAO invMstDAO = new InvMstDAO();
			aPlant = StrUtils.fString(request.getParameter("PLANT"));
			aLoginUser = StrUtils.fString(request.getParameter("LOGIN_USER"));
			item = StrUtils.fString(request.getParameter("ITEM"));
			ITEM_BATCH = StrUtils.fString(request.getParameter("BATCH"));
			LOC = StrUtils.fString(request.getParameter("LOC"));
			INVID=StrUtils.fString(request.getParameter("INVID"));
			Qty = StrUtils.fString(request.getParameter("SCANNEDQTY"));
			scannedqty = Double.parseDouble(((String) Qty.trim().toString()));
		    ISNONSTKFLG = new ItemMstDAO().getNonStockFlag(aPlant, item);
			boolean isNotFound = false;
			if(!ISNONSTKFLG.equalsIgnoreCase("Y")){
				List listQry = invMstDAO.getOutBoundPickingBatchBYPDA(aPlant,item, LOC, ITEM_BATCH,INVID);
				double invqty = 0;
				if (listQry.size() > 0) {
					for (int j = 0; j < listQry.size(); j++) {
						Map m = (Map) listQry.get(j);
						ITEM_QTY = (String) m.get("qty");
						invqty += Double.parseDouble(((String) ITEM_QTY.trim()));
					}
					if(invqty < scannedqty){
						isNotFound=true;	
						throw new Exception(
								"Error in Issue : Invalid Batch/Inventory not found for the scanned Product/Loc/Batch.");
						
					}
				} else{
					isNotFound=true;	
					throw new Exception(
							"Error in Issue : Invalid Batch/Inventory not found for the scanned Product/Loc/Batch.");
					
				}
				
				
				}
				

			if (!isNotFound) {
				xmlStr = XMLUtils.getXMLMessage(0, "Inventory found");
			} else {
				xmlStr = XMLUtils.getXMLMessage(1, "Inventory not found");
			}

		} catch (Exception e) {
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
	//Created by Vicky Used for Sales Check PDA
	private String Load_Batch_Details(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {
		String str = "", aPlant = "", aItemNum = "", aLoc = "",aBatch="",UOM="";
		try {

			PLANT = StrUtils.fString(request.getParameter("PLANT"));
			String userID = StrUtils
					.fString(request.getParameter("LOGIN_USER"));
			String dono = StrUtils
					.fString(request.getParameter("DONO"));
			aItemNum = StrUtils.fString(request.getParameter("ITEM_NUM"));
			aBatch= StrUtils.fString(request.getParameter("BATCH"));
			UOM=StrUtils.fString(request.getParameter("UOM"));
			str = _InvMstUtil.getbatchDetailsPDACheck(PLANT, dono,aItemNum, userID,aBatch,UOM);

			if (str.equalsIgnoreCase("")) {
				str = XMLUtils.getXMLMessage(1, "Batch detail not found");
			}

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			str = XMLUtils.getXMLMessage(1, e.getMessage());
		}

		return str;
	}
	//Created by Vicky Used for Sales Check PDA Batch Validation
		private String Sales_Check_Batch_Validation(HttpServletRequest request,
				HttpServletResponse response) throws IOException, ServletException {
			String str = "", aPlant = "", aItemNum = "", aLoc = "",aBatch="",UOM="";
			try {

				PLANT = StrUtils.fString(request.getParameter("PLANT"));
				String userID = StrUtils
						.fString(request.getParameter("LOGIN_USER"));
				String dono = StrUtils
						.fString(request.getParameter("DONO"));
				aItemNum = StrUtils.fString(request.getParameter("ITEM_NUM"));
				aBatch= StrUtils.fString(request.getParameter("BATCH"));
				UOM=StrUtils.fString(request.getParameter("UOM"));
				str = _InvMstUtil.getbatchValidatePDACheck(PLANT, dono,aItemNum, userID,aBatch,UOM);

				if (str.equalsIgnoreCase("")) {
					str = XMLUtils.getXMLMessage(1, "Batch detail not found");
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
