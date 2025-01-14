
package com.track.servlet;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.transaction.UserTransaction;

import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperRunManager;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.track.constants.IConstants;
import com.track.constants.IDBConstants;
import com.track.constants.MLoggerConstant;
import com.track.constants.TransactionConstants;
import com.track.dao.CurrencyDAO;
import com.track.dao.CustomerBeanDAO;
import com.track.dao.DNPLDetDAO;
import com.track.dao.DoDetDAO;
import com.track.dao.DoHdrDAO;
import com.track.dao.EmployeeDAO;
import com.track.dao.IntegrationsDAO;
import com.track.dao.InvMstDAO;
import com.track.dao.ItemMstDAO;
import com.track.dao.ItemSesBeanDAO;
import com.track.dao.LocMstDAO;
import com.track.dao.MasterDAO;
import com.track.dao.MovHisDAO;
import com.track.dao.OrderTypeBeanDAO;
import com.track.dao.POSDetDAO;
import com.track.dao.POSHdrDAO;
import com.track.dao.POSPaymentDetailDAO;
import com.track.dao.PlantMstDAO;
import com.track.dao.RecvDetDAO;
import com.track.dao.SalesDetailDAO;
import com.track.dao.ShipHisDAO;
import com.track.dao.StockTakeDAO;
import com.track.dao.TblControlDAO;
import com.track.db.util.CurrencyUtil;
import com.track.db.util.CustUtil;
import com.track.db.util.DOUtil;
import com.track.db.util.GstTypeUtil;
import com.track.db.util.HTReportUtil;
import com.track.db.util.InvMstUtil;
import com.track.db.util.InvUtil;
import com.track.db.util.ItemUtil;
import com.track.db.util.LocUtil;
import com.track.db.util.MasterUtil;
import com.track.db.util.MiscIssuingUtil;
import com.track.db.util.POSUtil;
import com.track.db.util.PlantMstUtil;
import com.track.db.util.TblControlUtil;
import com.track.db.util.UserLocUtil;
import com.track.gates.DbBean;
import com.track.gates.Generator;
import com.track.gates.selectBean;
import com.track.gates.userBean;
import com.track.job.ApiInventoryUpdateJob;
import com.track.pda.posServlet;
import com.track.service.ShopifyService;
import com.track.tables.DOHDR;
import com.track.tables.ITEMMST;
import com.track.util.DateUtils;
import com.track.util.IMLogger;
import com.track.util.MLogger;
import com.track.util.StrUtils;
import com.track.dao.OrderTypeDAO;
import com.track.dao.DNPLHdrDAO;

import edu.emory.mathcs.backport.java.util.Arrays;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.json.JSONObject;
import net.sf.jasperreports.engine.design.JRDesignBand;

/**
 * Servlet implementation class DynamicProductServlet
 */
public class DynamicProductServlets extends HttpServlet implements IMLogger {
	private static final long serialVersionUID = 1L;
	private boolean printLog = MLoggerConstant.LoanOrderPickingServlet_PRINTPLANTMASTERLOG;
	//private boolean printInfo = MLoggerConstant.LoanOrderPickingServlet_PRINTPLANTMASTERINFO;
	GstTypeUtil _GstTypeUtil = new GstTypeUtil();
	private DOUtil _DOUtil = null;
	MasterUtil _masterUtil = new MasterUtil();
	PlantMstDAO _PlantMstDAO = new PlantMstDAO();
	
	/**
	 * @see HttpServlet#HttpServlet()
	 */
	/*
	 * public DynamicProductServlet() { super(); }
	 */

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

	@SuppressWarnings("unchecked")
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String posPageToDirect = "";
		String action = "", cmdAction = "", nonstockflag = "";
		HttpSession session = request.getSession(false);
		String login_user = "";
		ItemUtil itemUtil = new ItemUtil();
		StrUtils strUtils = new StrUtils();
		boolean match = false;
		Boolean batchChecked = false;
		Boolean InvFlag = false;
		posServlet _posServlet = new posServlet();
		POSHdrDAO poshdr = new POSHdrDAO();
		MasterDAO _MasterDAO = new MasterDAO();
		CurrencyUtil curUtil = new CurrencyUtil();
		DOUtil dOUtil =new DOUtil();
		
		try {
			action = StrUtils.fString(request.getParameter("action"));
			cmdAction = StrUtils.fString(request.getParameter("cmd"));
			if(action.equalsIgnoreCase("")&&cmdAction.equalsIgnoreCase("ADD"))
				action = cmdAction;
			String posType = StrUtils.fString(request.getParameter("POS_TYPE")).trim(); // whether to check against
																						// Inventory

			String TRANTYPE = StrUtils.fString(request.getParameter("TRANTYPE")).trim();

			if (posType.equalsIgnoreCase("NO_INV_CHECK")) {
				posPageToDirect = "posWoInv.jsp";
			} else if (posType.equalsIgnoreCase("WITHOUTPRICE")) {
				posPageToDirect = "../salestransaction/goodsissue";
			} else if (posType.equalsIgnoreCase("WITHOUTCOST")) {
				posPageToDirect = "../purchasetransaction/goodsreceipt";
			} else {
				posPageToDirect = "dynamicprds.jsp";
			}

			if (TRANTYPE.equalsIgnoreCase("GOODSRECEIPTWITHOUTBATCH")) {
				posPageToDirect = "MultiMiscOrderReceiving.jsp";
			} else if (TRANTYPE.equalsIgnoreCase("GOODSRECEIPTWITHBATCH")) {
				posPageToDirect = "../purchasetransaction/goodsreceipt";
			}

			if (TRANTYPE.equalsIgnoreCase("GOODSISSUEWITHOUTBATCH")) {
				posPageToDirect = "MultiMiscOrderIssuing.jsp";
			} else if (TRANTYPE.equalsIgnoreCase("GOODSISSUEWITHBATCH")) {
				posPageToDirect = "../salestransaction/goodsissue";
			} else if (TRANTYPE.equalsIgnoreCase("GOODSISSUEWITHBATCHPRICE")) {
				posPageToDirect = "dynamicprdswithprice.jsp";
			} else if (TRANTYPE.equalsIgnoreCase("MOVEWITHBATCH")) {
				posPageToDirect = "../inhouse/new";
			} else if (TRANTYPE.equalsIgnoreCase("MOVEWITHOUTBATCH")) {
				posPageToDirect = "MultiLocationTransfer.jsp";
			}
			
			
			if (action.equalsIgnoreCase("bulkissue")) {
				String xmlStr = "";

				String[] chkdLnNo = request.getParameterValues("chkdLnNo");

				for (int i = 0; i < chkdLnNo.length; i++) {

					String data = chkdLnNo[i];
					String desc = StrUtils.fString(request.getParameter("desc" + data));

					String s = "";

				}

			}
			
			String PLANT = session.getAttribute("PLANT").toString();
			String item = StrUtils.fString(request.getParameter("ITEM")).trim();
			String scanqty = StrUtils.fString(request.getParameter("QTY")).trim();
			String batch = StrUtils.fString(request.getParameter("BATCH_0")).trim();
			String[] mitem  = request.getParameterValues("item"); 
			String[] mbatch  = request.getParameterValues("batch"); 
			String[] mscanqty  = request.getParameterValues("qty");
			String sBatchId = StrUtils.fString(request.getParameter("BATCH_ID_0")).trim();
			int batchId = -1;
			if (!"".equals(sBatchId)) {
				batchId = Integer.parseInt(sBatchId);
			}
			if (request.getParameter("chkbatch") != null) {
				batchChecked = true;
			}

			login_user = (String) session.getAttribute("LOGIN_USER");
			String loc = StrUtils.fString(request.getParameter("LOC")).trim();
			String tranID = StrUtils.fString(request.getParameter("TRANID")).trim();
			String RecvtranID = StrUtils.fString(request.getParameter("RECVTRANID")).trim();
			// String prodID = StrUtils.fString(request.getParameter("ITEM")).trim();
			String remarks = StrUtils.fString(request.getParameter("REMARKS")).trim();
			String reasoncode = StrUtils.fString(request.getParameter("REASONCODE")).trim();
			String employee = StrUtils.fString(request.getParameter("EMP_NAME")).trim();
			String EXPIREDATE = StrUtils.fString(request.getParameter("EXPIREDATE")).trim();
			// String trantype= StrUtils.fString(request.getParameter("TRANTYPE")).trim();

			// total discount calculation
			String TOTAL = StrUtils.fString(request.getParameter("TOTAL"));
			String SUBTOTAL = StrUtils.fString(request.getParameter("SUBTOTAL"));
			String TAX = StrUtils.fString(request.getParameter("TAX"));
			String TOTALDISCOUNT = StrUtils.fString(request.getParameter("TOTALDISCOUNT"));
			String DISCOUNTSUBTOTAL = StrUtils.fString(request.getParameter("DISCOUNTSUBTOTAL"));
			//
			
			// manual stocktake
			String Mitem = StrUtils.fString(request.getParameter("ITEMS")).trim();
			String Mloc = StrUtils.fString(request.getParameter("LOCS")).trim();
			String Mbatch = StrUtils.fString(request.getParameter("BATCHS")).trim();
			String Mqty = StrUtils.fString(request.getParameter("QTY")).trim();
			String Mremark = StrUtils.fString(request.getParameter("REMARKS")).trim();
			String Muom = StrUtils.fString(request.getParameter("UOMS")).trim();

			// System.out.println("scanned qty"+scanqty);
			float scqty = 0;

			if (scanqty.length() > 0)
				scqty = Float.parseFloat(scanqty);

			Vector<ITEMMST> buylist = (Vector<ITEMMST>) session.getAttribute("poslist");
			Vector<ITEMMST> getlist = (Vector<ITEMMST>) session.getAttribute("recvlist");
			session.setAttribute("errmsg", "");
			this.setMapDataToLogger(this.populateMapData(PLANT, login_user));
			itemUtil.setmLogger(mLogger);
			POSHdrDAO posHdr = new POSHdrDAO();
			posHdr.setmLogger(mLogger);

			POSDetDAO posdet = new POSDetDAO();
			posdet.setmLogger(mLogger);
			String errmsg = "";
			/*
			 * if (item == "" || item == null) {
			 * response.sendRedirect("jsp/dynamicprds.jsp"); }
			 */

			if (request.getParameter("action1") != null
					&& request.getParameter("action1").equalsIgnoreCase("Discount")) {
				Vector<ITEMMST> itemlist = (Vector<ITEMMST>) session.getAttribute("poslist");
				Hashtable<String, String> htcond = new Hashtable<String, String>();
				htcond.put(IDBConstants.POS_TRANID, tranID);
				htcond.put(IDBConstants.PLANT, PLANT);
				for (int j = 0; j < itemlist.size(); j++) {
					ITEMMST itemdao = itemlist.elementAt(j);
					if (itemdao.getITEM().equalsIgnoreCase(item)) {
						float disccnt = Float.parseFloat(request.getParameter(item));
						itemdao.setDISCOUNT(disccnt);

						float up = itemdao.getUNITPRICE();
						float qty = itemdao.getStkqty();
						float tprice = (up - up * disccnt / 100) * qty;

						// update POSDET discount
						htcond.put(IDBConstants.ITEM, item);
						String query = " set DISCOUNT=" + disccnt + ",TOTALPRICE=" + tprice;
						boolean flag = posdet.update(query, htcond, "");
						itemdao.setTotalPrice(tprice);
					}
					itemlist.setElementAt(itemdao, j);
				}

				session.setAttribute("poslist", itemlist);
				session.setAttribute("LOC", loc);
				// response.sendRedirect("jsp/"+posPageToDirect+"?TRANID="+tranID);
				RequestDispatcher view = request.getRequestDispatcher("jsp/" + posPageToDirect + "?TRANID=" + tranID);

				view.forward(request, response);

			}

			    else if (cmdAction.equalsIgnoreCase("RESETSTK") && TRANTYPE.equalsIgnoreCase("MANUALSTOCKTAKE")) {
			DateUtils dtUtils = new DateUtils();
			String pltCountry = new PlantMstDAO().getCOUNTRY_TIMEZONE(PLANT);
			if(pltCountry.equalsIgnoreCase(""))
				pltCountry="Asia/Singapore";
			posPageToDirect = "../inhouse/manualstocktake";
			StrUtils su =null;
			boolean flag = false;
			String msg = "";
			
			Hashtable ht = new Hashtable();
			Hashtable<String, String> htInvMst = new Hashtable<String, String>();
			
			ArrayList invQryList= new InvUtil().getInvListSummaryWithOutPriceMultiUom(ht,PLANT,"","","","","",Mloc,"");//"","","","",plant,"");
			for (int iCnt =0; iCnt<invQryList.size(); iCnt++){
                Map lineArr = (Map) invQryList.get(iCnt);
                String inItem =(String)lineArr.get("ITEM");
                String inBatch =(String)lineArr.get("BATCH");
                String inLoc =(String)lineArr.get("LOC");
                String inUom =(String)lineArr.get("INVENTORYUOM");
                String inStkqty =(String)lineArr.get("QTY");
                
                htInvMst.put(IDBConstants.PLANT, PLANT);
    			htInvMst.put(IDBConstants.ITEM, inItem);
    			htInvMst.put("BATCH", inBatch);
    			htInvMst.put(IDBConstants.LOC, inLoc);
    			htInvMst.put("STATUS", "N");
    			boolean result=new ItemUtil().isExistsItemMst(inItem,PLANT);
    			flag = new StockTakeDAO().isExisit(htInvMst, "");
    			
    			if(!flag) {
    				
				htInvMst.clear();
				htInvMst.clear();
				htInvMst.put(IDBConstants.PLANT, PLANT);
				htInvMst.put(IDBConstants.ITEM, inItem);
				htInvMst.put(IDBConstants.QTY, "0");
				htInvMst.put(IDBConstants.LOC, inLoc);
				htInvMst.put("BATCH", inBatch);
				htInvMst.put("DIFFQTY", inStkqty);
				htInvMst.put("INVFLAG", "0");
				htInvMst.put(IDBConstants.UOM, inUom);
				htInvMst.put(IDBConstants.STATUS, "N");
				htInvMst.put("CR_DATE",  DateUtils.getDate());
				htInvMst.put(IDBConstants.USERFLD1,su.InsertQuotes( new ItemMstDAO().getItemDesc(PLANT,inItem)));
				htInvMst.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
				htInvMst.put(IDBConstants.CREATED_BY, login_user);
				htInvMst.put(IDBConstants.REMARKS, Mremark);
				flag = new StockTakeDAO().insertStkTake(htInvMst);
				msg = " "+Mitem+" Product Added Successfully ";
    			}
    			
			}
			
			response.sendRedirect(posPageToDirect+"?result="+msg);
		} 
				  

			         	
			
			
		} catch (Exception e) {
			String expireDate = "", refno = "";
			refno = request.getParameter("REFERENCENO");
			String empID = StrUtils.fString(request.getParameter("EMP_ID")).trim();
			String refNo = StrUtils.fString(request.getParameter("REFERENCENO")).trim();
			String serialized = StrUtils.fString(request.getParameter("serialized")).trim();
			String tranDate = StrUtils.fString(request.getParameter("TRANSACTIONDATE")).trim();
			String holdReasonCode = StrUtils.fString(request.getParameter("REASONCODE")).trim();
			String holdRemarks = StrUtils.fString(request.getParameter("REMARKS")).trim();
			expireDate = request.getParameter("EXPIREDATE");
			refno = request.getParameter("REFERENCENO");
			String empName = request.getParameter("EMP_NAME");
			String loc = StrUtils.fString(request.getParameter("LOC")).trim();
			String fromlocdesc = StrUtils.fString(request.getParameter("FROM_LOCDESC")).trim();
			String to_LOC = StrUtils.fString(request.getParameter("TOLOC")).trim();
			String tolocdesc = StrUtils.fString(request.getParameter("TOLOCDESC")).trim();
			String defualtQty = StrUtils.fString(request.getParameter("defaultQty")).trim();
			String bulkcheckout = StrUtils.fString(request.getParameter("bulkcheckout")).trim();
			if (action.equalsIgnoreCase("delete")) {
				response.sendRedirect("jsp/" + posPageToDirect + "?TRANID=" + request.getParameter("TRANID")
						+ "&CHKBATCH=" + batchChecked + "&FROM_LOC=" + loc + "&TO_LOC=" + to_LOC + "&serialized="
						+ serialized + "&EMP_ID=" + empID + "&REFERENCENO=" + refNo + "&serialized=" + serialized
						+ "&defualtQty=" + defualtQty + "&bulkcheckout=" + bulkcheckout + "&TRANSACTIONDATE=" + tranDate
						+ "&REASONCODE=" + holdReasonCode + "&REMARKS=" + holdRemarks + "&EMP_NAME=" + "&FROM_LOCDESC="
						+ fromlocdesc + "&TOLOCDESC=" + tolocdesc + "&EMP_NAME=" + empName);
			}
			if (action.equalsIgnoreCase("ADD") && InvFlag == true) {
				session.setAttribute("errormsg", "");
				session.setAttribute("RESULTTOTALDISCOUNT", "");
				session.setAttribute("TOTALDISCOUNT", "");
				session.setAttribute("TOTALSUBTOTAL", "");
				session.setAttribute("TOTALTAX", "");

			}
			if (action.equalsIgnoreCase("ADD") && InvFlag == false) {

				session.setAttribute("errormsg", "");
				session.setAttribute("RESULTTOTALDISCOUNT", "");
				session.setAttribute("TOTALDISCOUNT", "");
				session.setAttribute("TOTALSUBTOTAL", "");
				session.setAttribute("TOTALTAX", "");
				response.sendRedirect("jsp/" + posPageToDirect + "?TRANID=" + request.getParameter("TRANID")
						+ "&CHKBATCH=" + batchChecked + "&LOC=" + loc + "&TOLOC=" + to_LOC + "&FROM_LOCDESC="
						+ fromlocdesc + "&TOLOCDESC=" + tolocdesc + "&serialized=" + serialized + "&EMP_ID=" + empID
						+ "&REFERENCENO=" + refNo + "&serialized=" + serialized + "&defualtQty=" + defualtQty
						+ "&bulkcheckout=" + bulkcheckout + "&TRANSACTIONDATE=" + tranDate + "&REASONCODE="
						+ holdReasonCode + "&REMARKS=" + holdRemarks + "&EMP_NAME=" + empName);
				// RequestDispatcher view =
				// request.getRequestDispatcher("jsp/"+posPageToDirect+"?TRANID="+request.getParameter("TRANID")+"&CHKBATCH="+batchChecked);

				// view.forward(request, response);
			}
			if (action.equalsIgnoreCase("ADDPRODUCT") && InvFlag == true) {
				session.setAttribute("errormsg", "");
			}
			if (action.equalsIgnoreCase("ADDPRODUCT") && InvFlag == false) {

				session.setAttribute("errormsg", "");
				// response.sendRedirect("jsp/"+posPageToDirect+"?TRANID="+request.getParameter("TRANID")+"&CHKBATCH="+batchChecked+"&LOC="+request.getParameter("LOC"));
				RequestDispatcher view = request
						.getRequestDispatcher("jsp/" + posPageToDirect + "?TRANID=" + request.getParameter("TRANID")
								+ "&CHKBATCH=" + batchChecked + "&LOC=" + request.getParameter("LOC"));
				view.forward(request, response);
			}
			if (action.equalsIgnoreCase("ADDRECVPRODUCT") && InvFlag == true) {
				session.setAttribute("errormsg", "");
			}

			InvFlag = false;
			e.printStackTrace();
		}
	}

	public HashMap<String, String> populateMapData(String companyCode, String userCode) {
		HashMap<String, String> loggerDetailsHasMap = new HashMap<String, String>();
		loggerDetailsHasMap.put(MLogger.COMPANY_CODE, companyCode);
		loggerDetailsHasMap.put(MLogger.USER_CODE, userCode);
		return loggerDetailsHasMap;

	}

	public String[] getReceiptMsg(String msgkey) {
		String[] msgarray = null;
		msgarray = msgkey.split("##");

		return msgarray;
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