
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
import net.sf.jasperreports.engine.design.JRDesignBand;

/**
 * Servlet implementation class DynamicProductServlet
 */
public class DynamicProductServlet extends HttpServlet implements IMLogger {
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
//				posPageToDirect = "MultiLocationTransferWithBatch.jsp";
				//posPageToDirect = "../inhouse/stockmove";
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
			
			if (cmdAction.equalsIgnoreCase("ExportExcelSummary")) {
				
				String newHtml = "";
				HSSFWorkbook wb = new HSSFWorkbook();
				String PLANT = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
				wb = this.writeToExcelSummary(request, response, wb);
				ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
				wb.write(outByteStream);
				byte[] outArray = outByteStream.toByteArray();
				response.setContentType("application/ms-excel");
				response.setContentLength(outArray.length);
				response.setHeader("Expires:", "0"); // eliminates browser caching
				response.setHeader("Content-Disposition", "attachment; filename=SalesOrderSummary.xls");
				OutputStream outStream = response.getOutputStream();
				outStream.write(outArray);
				outStream.flush();
				outStream.close();
			
				}
			// reset to new pos transaction

			if (cmdAction.equalsIgnoreCase("NewPOS")) {
				try {

					Vector<ITEMMST> itemlist = (Vector<ITEMMST>) session.getAttribute("poslist");
					if (!itemlist.isEmpty())
						itemlist.clear();
					session.setAttribute("poslist", itemlist);
					session.setAttribute("errormsg", "");
					session.setAttribute("RESULTTOTALDISCOUNT", "");
					session.setAttribute("TOTALDISCOUNT", "");
					session.setAttribute("TOTALSUBTOTAL", "");
					session.setAttribute("TOTALTAX", "");

					// response.sendRedirect("jsp/"+posPageToDirect);
					RequestDispatcher view = request.getRequestDispatcher("jsp/" + posPageToDirect);
					view.forward(request, response);
				} catch (Exception e) {
					session.setAttribute("errormsg", "");
					// response.sendRedirect("jsp/"+posPageToDirect);
					RequestDispatcher view = request.getRequestDispatcher("jsp/" + posPageToDirect);
					view.forward(request, response);
				}

			}
			if (cmdAction.equalsIgnoreCase("NewPOSReceive")) {
				try {

					Vector<ITEMMST> itemlist = (Vector<ITEMMST>) session.getAttribute("recvlist");
					if (!itemlist.isEmpty())
						itemlist.clear();
					session.setAttribute("recvlist", itemlist);
					session.setAttribute("errormsg", "");
					session.setAttribute("RESULTTOTALDISCOUNT", "");
					session.setAttribute("TOTALDISCOUNT", "");
					session.setAttribute("TOTALSUBTOTAL", "");
					session.setAttribute("TOTALTAX", "");

					// response.sendRedirect("jsp/"+posPageToDirect);
					// RequestDispatcher view =
					// request.getRequestDispatcher("jsp/"+posPageToDirect);
					// view.forward(request, response);
					String message = "jsp/" + posPageToDirect;
					response.getWriter().write(message);
				} catch (Exception e) {
					session.setAttribute("errormsg", "");
					// response.sendRedirect("jsp/"+posPageToDirect);
					// RequestDispatcher view =
					// request.getRequestDispatcher("jsp/"+posPageToDirect);
					// view.forward(request, response);
					String message = "jsp/" + posPageToDirect;
					response.getWriter().write(message);
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

			else if (action.equalsIgnoreCase("delete2")) {
				String fromloc = "";
				session.setAttribute("RESULTTOTALDISCOUNT", "");
				session.setAttribute("TOTALDISCOUNT", "");
				session.setAttribute("TOTALSUBTOTAL", "");
				session.setAttribute("TOTALTAX", "");
				session.setAttribute("CALCDISCOUNT", "");
				int iCnt = 0;
				String appenddisc = "";
				String[] chkvalues = request.getParameterValues("chk");
				int indexarray[] = new int[chkvalues.length];
				Vector<ITEMMST> updatelist = new Vector<ITEMMST>();
				loc = StrUtils.fString(request.getParameter("FROM_LOC")).trim();
				fromloc = StrUtils.fString(request.getParameter("LOC")).trim();
				if (loc.length() > 0 || fromloc.length() > 0) {

					if (chkvalues.length > 0) {

						for (int j = 0; j < chkvalues.length; j++) {
							indexarray[iCnt] = Integer.parseInt(chkvalues[iCnt]);
							iCnt++;
						}
						String ItemInPosforTranId = "";
						String BatchInPosforTranId = "";
						for (int j = 0; j < buylist.size(); j++) {
							int index = Arrays.binarySearch(indexarray, j);
							if (index < 0) {
								ITEMMST itembeans = buylist.get(j);
								ITEMMST additem = new ITEMMST();
								additem.setITEM(itembeans.getITEM());
								additem.setITEMDESC(itembeans.getITEMDESC());
								additem.setUNITPRICE(itembeans.getUNITPRICE());
								additem.setStkqty(itembeans.getStkqty());
								additem.setDISCOUNT(itembeans.getDISCOUNT());
								additem.setTotalPrice(itembeans.getTotalPrice());
								additem.setGSTTAX(itembeans.getGSTTAX());
								additem.setNONSTOCKFLAG(itembeans.getNONSTOCKFLAG());// added by radhika
								additem.setBATCH(itembeans.getBATCH());
								updatelist.add(additem);

							} else {
								ITEMMST itembeans = buylist.get(j);
								if (ItemInPosforTranId.length() > 0) {
									ItemInPosforTranId = ItemInPosforTranId + "," + "'" + itembeans.getITEM() + "'";
								} else {
									ItemInPosforTranId = "'" + itembeans.getITEM() + "'";
								}

								if (BatchInPosforTranId.length() > 0) {
									BatchInPosforTranId = BatchInPosforTranId + "," + "'" + itembeans.getBATCH() + "'";
								} else {
									BatchInPosforTranId = "'" + itembeans.getBATCH() + "'";
								}
							}

						}

						posdet.deleteProductForTranId(PLANT, ItemInPosforTranId, tranID, BatchInPosforTranId);
						buylist.removeAllElements();
						session.setAttribute("poslist", updatelist);
					}

					session.setAttribute("errmsg", "");
				} else {
					session.setAttribute("errmsg", "Please select checkbox");
				}

				// response.sendRedirect("jsp/"+posPageToDirect+"?TRANID="+tranID+"&CHKBATCH="+batchChecked+"&LOC="+loc);
				RequestDispatcher view = request.getRequestDispatcher(
						"jsp/" + posPageToDirect + "?TRANID=" + tranID + "&CHKBATCH=" + batchChecked + "&LOC=" + loc);
				view.forward(request, response);
			} else if (cmdAction.equalsIgnoreCase("New")) {
				// POSHdrDAO poshdr = new POSHdrDAO();
				UserLocUtil uslocUtil = new UserLocUtil();
				uslocUtil.setmLogger(mLogger);
				poshdr.setmLogger(mLogger);
				boolean isvalidlocforUser = uslocUtil.isValidLocInLocmstForUser(PLANT, login_user, loc);
				if (isvalidlocforUser) {

					String genPosTranid = poshdr.genPosTranId(PLANT, login_user, loc);

					if (buylist != null) {
						buylist.removeAllElements();
						session.setAttribute("poslist", buylist);
						session.setAttribute("errmsg", "");
					}
					session.setAttribute("LOCATION", loc);
					response.sendRedirect("jsp/" + posPageToDirect + "?TRANID=" + genPosTranid);
				} else {
					session.setAttribute("errmsg",
							"Location : " + loc + " is not a User Assigned Location/Valid Location ");
					response.sendRedirect("jsp/" + posPageToDirect);
				}
			} else if (action.equalsIgnoreCase("Hold")) {
				boolean posHsrUpt = false;
				
				String rflag = "", ordertypeString = "", result = "", dono = "", custName = "", display = "", ordertype = "",
						currencyid = "", custCode = "", jobNum = "", personIncharge = "", user_id = "", contactNum = "",
						address = "", address2 = "", address3 = "",  collectionTime = "", deliverydate = "",
						timeslots = "", outbound_Gst = "", orderstatus = "",  remark1 = "", remark2 = "",
						remark3 = "", deldate = "", processno = "", shipCustname = "", shipCname = "", shipaddr1 = "",
						shipaddr2 = "", shipCity = "", shipCountry = "", shipZip = "", shiptelno = "", shippingcustomer = "",
						shippingid = "", orderdiscount = "", shippingcost = "", incoterms = "", recstatus="I",EDIT="";
						
				dono = StrUtils.fString(request.getParameter("TRANID")).trim();
				custName = StrUtils.replaceCharacters2Recv(StrUtils.fString(request.getParameter("CUST_NAME"))).trim();
				custCode = StrUtils.fString(request.getParameter("CUST_CODE1")).trim();
				personIncharge = StrUtils.fString(request.getParameter("PERSON_INCHARGE")).trim();
				contactNum = StrUtils.fString(request.getParameter("CONTACT_NUM")).trim();
				address = StrUtils.fString(request.getParameter("ADD1")).trim();
				address2 = StrUtils.fString(request.getParameter("ADD2")).trim();
				address3 = StrUtils.fString(request.getParameter("ADD3")).trim();
				remark1 = StrUtils.fString(request.getParameter("REMARK1")).trim();
				remark2 = StrUtils.fString(request.getParameter("REMARK2")).trim();
				remark3 = StrUtils.fString(request.getParameter("REMARK3")).trim();
				display = StrUtils.fString(request.getParameter("DISPLAY")).trim();
				ordertype = StrUtils.fString(request.getParameter("ORDERTYPE")).trim();
				deliverydate = StrUtils.fString(request.getParameter("DELIVERYDATE")).trim();				
				outbound_Gst = StrUtils.fString(request.getParameter("GST")).trim();
				shippingcustomer = StrUtils.fString(request.getParameter("SHIPPINGCUSTOMER")).trim();
				shippingid = StrUtils.fString(request.getParameter("SHIPPINGID")).trim();
				orderdiscount = StrUtils.fString(request.getParameter("ORDERDISCOUNT")).trim();
				shippingcost = StrUtils.fString(request.getParameter("SHIPPINGCOST")).trim();
				incoterms = StrUtils.fString(request.getParameter("INCOTERMS")).trim();
				collectionTime = StrUtils.fString(request.getParameter("COLLECTION_TIME")).trim();
				EDIT = StrUtils.fString(request.getParameter("EDIT")).trim();
				if(EDIT=="")
				{
					
				
				Hashtable ht = new Hashtable();
				boolean isvalidOrderType = true;
				if (ordertype.length() > 0) {
				if (!ordertype.equals("") && !ordertype.equals("MOBILE ORDER") && !ordertype.equals("MOBILE ENQUIRY")
						&& !ordertype.equals("MOBILE REGISTRATION")) {
					OrderTypeBeanDAO orderTypeBeanDAO = new OrderTypeBeanDAO();
					orderTypeBeanDAO.setmLogger(mLogger);
					isvalidOrderType = orderTypeBeanDAO.isOrderTypeExists(ordertype, PLANT);
				}
				}
				CustomerBeanDAO customerBeanDAO = new CustomerBeanDAO();
				customerBeanDAO.setmLogger(mLogger);
				boolean isvalidCustomer =  Boolean.valueOf(true);
				if (custName.length() > 0) {
				isvalidCustomer = customerBeanDAO.isExistsCustomerName(custName, PLANT);
				}
				Boolean isvalidShippingCustomer = Boolean.valueOf(true);
				if (shippingcustomer.length() > 0) {
					isvalidShippingCustomer = _MasterDAO.isExistsShippingDetails(shippingcustomer, PLANT);
				}
				
				String locationID = StrUtils.fString(request.getParameter("LOC")).trim();
				String empID = StrUtils.fString(request.getParameter("EMP_ID")).trim();
				String refNo = StrUtils.fString(request.getParameter("REFERENCENO")).trim();
				String tranDate = StrUtils.fString(request.getParameter("TRANSACTIONDATE")).trim();

				String holdReasonCode = StrUtils.fString(request.getParameter("REASONCODE")).trim();
				String holdRemarks = StrUtils.fString(request.getParameter("REMARKS")).trim();
				// String trantype = StrUtils.fString(request.getParameter("TRANTYPE")).trim();
				// To check transaction already exists in POSHeader
				Hashtable<String, String> htTrandId = new Hashtable<String, String>();
				htTrandId.put("POSTRANID", tranID);
				boolean istranidExist = _posServlet.isExisit(PLANT, htTrandId);
				if (istranidExist == false) {
					String genPosTranid = poshdr.genPosManualTranId(PLANT, login_user, loc, tranID);
				}
								
				
				if (isvalidCustomer) {
					if (isvalidShippingCustomer) {
						if (isvalidOrderType) {
							
							ht.put(IDBConstants.PLANT, PLANT);
							ht.put(IDBConstants.POS_TRANID, dono);
							ht.put(IDBConstants.DOHDR_CUST_CODE, custCode);
							ht.put(IDBConstants.DOHDR_CUST_NAME, strUtils.InsertQuotes(custName));
							ht.put(IDBConstants.DOHDR_JOB_NUM, refNo);
							ht.put(IDBConstants.DOHDR_PERSON_INCHARGE, strUtils.InsertQuotes(personIncharge));
							ht.put(IDBConstants.DOHDR_CONTACT_NUM, contactNum);
							ht.put(IDBConstants.DOHDR_ADDRESS, address);
							ht.put(IDBConstants.DOHDR_ADDRESS2, address2);
							ht.put(IDBConstants.DOHDR_ADDRESS3, address3);									
							ht.put(IDBConstants.DOHDR_REMARK1, strUtils.InsertQuotes(remark1));
							ht.put(IDBConstants.DOHDR_REMARK2, strUtils.InsertQuotes(remark2));
							ht.put(IDBConstants.DOHDR_REMARK3, strUtils.InsertQuotes(remark3));
							ht.put(IDBConstants.STATUS, "H");
							ht.put(IDBConstants.ORDERTYPE, ordertype);
							ht.put(IDBConstants.CURRENCYID, currencyid);								
							
							ht.put(IDBConstants.DELIVERYDATE, deliverydate);
							ht.put(IDBConstants.DOHDR_GST, outbound_Gst);									
							ht.put(IDBConstants.DOHDR_EMPNO, empID);
							ht.put(IDBConstants.SHIPPINGCUSTOMER, shippingcustomer);
							ht.put(IDBConstants.SHIPPINGID, shippingid);
							ht.put(IDBConstants.ORDERDISCOUNT, orderdiscount);
							ht.put(IDBConstants.SHIPPINGCOST, shippingcost);
							ht.put(IDBConstants.INCOTERMS, incoterms);
							
							POSUtil posUtil = new POSUtil();
							posUtil.setmLogger(mLogger);
							posHsrUpt = posUtil.updatePosHdr(ht);
							
							}
						else {
							
							session.setAttribute("errmsg", " Enter Valid ORDER TYPE! ");
							response.sendRedirect(
									"jsp/" + posPageToDirect + "?TRANID=" + tranID + "&CHKBATCH=" + batchChecked);
								
							}
					}
					else {
						
						session.setAttribute("errmsg", " Enter Valid Shipping Customer! ");
						response.sendRedirect(
								"jsp/" + posPageToDirect + "?TRANID=" + tranID + "&CHKBATCH=" + batchChecked);
							
						}
				}
				else {
					session.setAttribute("errmsg", " Enter Valid Customer! ");
					response.sendRedirect(
							"jsp/" + posPageToDirect + "?TRANID=" + tranID + "&CHKBATCH=" + batchChecked);
					}
				
				
				// To check transaction already exists in POSHeader end
				// update poshdr set status ='H' and display new page
				ht = new Hashtable<String, String>();
				ht.put(IDBConstants.PLANT, PLANT);
				ht.put(IDBConstants.POS_TRANID, tranID);
				String QryUpdate = " SET STATUS ='H',UPAT ='" + DateUtils.getDate() + "',EMPNO='" + empID + "',REFNO='"
						+ refNo + "',TRANDT='" + tranDate + "',RSNCODE='" + holdReasonCode + "',LOC='" + locationID
						+ "',REMARKS='" + holdRemarks + "' ";
				try {
					posHsrUpt = posHdr.update(QryUpdate, ht, " AND STATUS <>'C'");
				} catch (Exception e) {

				}

				if (posHsrUpt) {
					session.setAttribute("errmsg", "");
					buylist.removeAllElements();
					new TblControlUtil().updateTblControlSeqNoWithoutorder(PLANT, "GIRECEIPT",
							"GI");
					session.setAttribute("poslist", buylist);
					session.setAttribute("LOCATION", loc);
					// response.sendRedirect("jsp/"+posPageToDirect);
					RequestDispatcher view = request.getRequestDispatcher("jsp/" + posPageToDirect);
					view.forward(request, response);
				} else {
					session.setAttribute("errmsg", " Pos Data for TranId cannot be Changed to Hold Status ");
					// response.sendRedirect("jsp/"+posPageToDirect+"?TRANID="+tranID);
					RequestDispatcher view = request
							.getRequestDispatcher("jsp/" + posPageToDirect + "?TRANID=" + tranID);
					view.forward(request, response);
				}
				}
				else {
					session.setAttribute("errmsg", " Hold Not Possible for Edit Pos");
					// response.sendRedirect("jsp/"+posPageToDirect+"?TRANID="+tranID);
					RequestDispatcher view = request
							.getRequestDispatcher("jsp/" + posPageToDirect + "?TRANID=" + tranID);
					view.forward(request, response);
				}
			} else if (action.equalsIgnoreCase("HoldTran")) {
				boolean posHsrUpt = false;
				// To check transaction already exists in POSHeader
				Hashtable<String, String> htTrandId = new Hashtable<String, String>();
				htTrandId.put("POSTRANID", RecvtranID);
				boolean istranidExist = _posServlet.isExisit(PLANT, htTrandId);

				String locationID = StrUtils.fString(request.getParameter("LOC")).trim();
				String empID = StrUtils.fString(request.getParameter("EMP_ID")).trim();
				String refNo = StrUtils.fString(request.getParameter("REFERENCENO")).trim();
				String tranDate = StrUtils.fString(request.getParameter("TRANSACTIONDATE")).trim();

				String holdReasonCode = StrUtils.fString(request.getParameter("REASONCODE")).trim();
				String holdRemarks = StrUtils.fString(request.getParameter("REMARKS")).trim();
				String serialized = StrUtils.fString(request.getParameter("serialized")).trim();
				String defualtQty = StrUtils.fString(request.getParameter("defualtQty")).trim();
				String expirydate = StrUtils.fString(request.getParameter("expirydate")).trim();

				if (istranidExist == false) {
					String genPosTranid = poshdr.genPosManualTranId(PLANT, login_user, loc, RecvtranID);
				}
				// To check transaction already exists in POSHeader end
				// update poshdr set status ='H' and display new page
				Hashtable<String, String> ht = new Hashtable<String, String>();
				ht.put(IDBConstants.PLANT, PLANT);
				ht.put(IDBConstants.POS_TRANID, RecvtranID);
				String QryUpdate = " SET STATUS ='H',UPAT ='" + DateUtils.getDate() + "',EMPNO='" + empID + "',REFNO='"
						+ refNo + "',TRANDT='" + tranDate + "',LOC='" + locationID + "',RSNCODE='" + holdReasonCode
						+ "',REMARKS='" + holdRemarks + "' ";
				try {
					posHsrUpt = posHdr.update(QryUpdate, ht, " AND STATUS <>'C'");
				} catch (Exception e) {

				}

				if (posHsrUpt) {
					session.setAttribute("errmsg", "");
					getlist.removeAllElements();
					session.setAttribute("recvlist", getlist);
					session.setAttribute("LOCATION", loc);
					new TblControlUtil().updateTblControlSeqNoWithoutorder(PLANT, "GRRECEIPT", "GR");
					RequestDispatcher view = request.getRequestDispatcher("jsp/" + posPageToDirect + "?serialized="
							+ serialized + "&defualtQty=" + defualtQty + "&expirydate=" + expirydate);
					view.forward(request, response);
				} else {
					session.setAttribute("errmsg", " Pos Data for TranId cannot be Changed to Hold Status ");
					RequestDispatcher view = request.getRequestDispatcher(
							"jsp/" + posPageToDirect + "?RECVTRANID=" + RecvtranID + "&serialized=" + serialized
									+ "&defualtQty=" + defualtQty + "&expirydate=" + expirydate);

					view.forward(request, response);
				}
			} else if (action.equalsIgnoreCase("StockHold")) {
				boolean posHsrUpt = false;
				String from_locationID = StrUtils.fString(request.getParameter("FROM_LOC")).trim();
				String to_locationID = StrUtils.fString(request.getParameter("TOLOC")).trim();
				String empID = StrUtils.fString(request.getParameter("EMP_ID")).trim();
				String refNo = StrUtils.fString(request.getParameter("REFERENCENO")).trim();
				String tranDate = StrUtils.fString(request.getParameter("TRANSACTIONDATE")).trim();

				String holdReasonCode = StrUtils.fString(request.getParameter("REASONCODE")).trim();
				String holdRemarks = StrUtils.fString(request.getParameter("REMARKS")).trim();
				// To check transaction already exists in POSHeader
				Hashtable<String, String> htTrandId = new Hashtable<String, String>();
				htTrandId.put("POSTRANID", tranID);
				boolean istranidExist = _posServlet.isExisit(PLANT, htTrandId);
				if (istranidExist == false) {
					String genPosTranid = poshdr.genPosManualTranId(PLANT, login_user, from_locationID, tranID);
				}
				// To check transaction already exists in POSHeader end
				// update poshdr set status ='H' and display new page
				Hashtable<String, String> ht = new Hashtable<String, String>();
				ht.put(IDBConstants.PLANT, PLANT);
				ht.put(IDBConstants.POS_TRANID, tranID);
				String QryUpdate = " SET STATUS ='H',UPAT ='" + DateUtils.getDate() + "',EMPNO='" + empID + "',REFNO='"
						+ refNo + "',TRANDT='" + tranDate + "',RSNCODE='" + holdReasonCode + "',LOC='" + from_locationID
						+ "',TOLOC='" + to_locationID + "',REMARKS='" + holdRemarks + "' ";
				try {
					posHsrUpt = posHdr.update(QryUpdate, ht, " AND STATUS <>'C'");
				} catch (Exception e) {

				}

				if (posHsrUpt) {
					session.setAttribute("errmsg", "");
					buylist.removeAllElements();
					session.setAttribute("poslist", buylist);
					session.setAttribute("LOCATION", loc);
					// response.sendRedirect("jsp/"+posPageToDirect);
					RequestDispatcher view = request.getRequestDispatcher("jsp/" + posPageToDirect);
					view.forward(request, response);
				} else {
					session.setAttribute("errmsg", " Pos Data for TranId cannot be Changed to Hold Status ");
					// response.sendRedirect("jsp/"+posPageToDirect+"?TRANID="+tranID);
					RequestDispatcher view = request
							.getRequestDispatcher("jsp/" + posPageToDirect + "?TRANID=" + tranID);
					view.forward(request, response);
				}
			} else if (cmdAction.equalsIgnoreCase("View")) {
				Vector<ITEMMST> veclist = this.getItemDetailsForPOSTranId(request, tranID, buylist);
				session.setAttribute("poslist", veclist);
				session.setAttribute("LOCATION", tranID.substring(0, tranID.indexOf("_")));
				response.sendRedirect("jsp/" + posPageToDirect + "?TRANID=" + tranID + "&CHKBATCH=" + batchChecked);
			} else if (cmdAction.equalsIgnoreCase("print")) {
				boolean ajax = false;
				ajax = "XMLHttpRequest".equals(request.getHeader("X-Requested-With"));

				if (ajax) {

					try {
						UserTransaction ut = null;
						ut = com.track.gates.DbBean.getUserTranaction();
						ut.begin();

						SalesDetailDAO salesdao = new SalesDetailDAO();
						POSPaymentDetailDAO paymentDAO = new POSPaymentDetailDAO();
						String rcpNo = "";
						Generator gen = new Generator();
						DateUtils dateUtils = new DateUtils();
						// Generate Receipt no
						rcpNo = this.genReceipt(request);
						boolean flag = false;
						boolean invflag = false;
						// To check transaction already exists in POSHeader
						Hashtable<String, String> htTrandId = new Hashtable<String, String>();
						htTrandId.put("POSTRANID", tranID);
						boolean istranidExist = _posServlet.isExisit(PLANT, htTrandId);
						if (istranidExist == false) {
							String genPosTranid = poshdr.genPosManualTranId(PLANT, login_user, loc, tranID);
						}
						// To check transaction already exists in POSHeader end
						String xmlutil = "";
						HashMap<String, String> htrecv = new HashMap<String, String>();
						String paymentmodes = "";
						MiscIssuingUtil miscutil = new MiscIssuingUtil();
						Hashtable<String, String> htm = new Hashtable<String, String>();
						String formatdt = DateUtils.getDate();
						htm.put("PURCHASEDATE", DateUtils.getDateinyyyy_mm_dd(formatdt));
						htm.put("PURCHASETIME", DateUtils.Time());
						htm.put(IConstants.REMARKS, remarks);
						htrecv.put(IConstants.PLANT, PLANT);
						htrecv.put(IConstants.REMARKS, remarks);
						htrecv.put(IConstants.LOGIN_USER, (String) session.getAttribute("LOGIN_USER"));
						htrecv.put(IConstants.LOC, loc);
						miscutil.setmLogger(mLogger);
						salesdao.setmLogger(mLogger);

						String appenddisc = "";
						paymentmodes = request.getParameter("paymentmodes");
						String amountsToSave = request.getParameter("amountsToSave");
						String amount = "0";
						// amounts = request.getParameter("amounts"));
						Hashtable<String, String> insPaymentHt = new Hashtable<String, String>();
						String paymentModeArray[] = paymentmodes.split(",");
						String amountsToSaveArray[] = amountsToSave.split(",");
						for (int j = 0; j < buylist.size(); j++) {
							ITEMMST product = buylist.get(j);
							htm.put(IConstants.PLANT, PLANT);
							htm.put(IConstants.ITEM, product.getITEM());
							float discnt = product.getDISCOUNT();
							htm.put(IConstants.ITEM_DESC, strUtils.InsertQuotes(product.getITEMDESC()));
							htm.put(IConstants.BATCH, product.getBATCH());
							htm.put(IConstants.QTY, String.valueOf(product.getStkqty()));
							htm.put(IDBConstants.PRICE, String.valueOf(product.getPRICEWITHTAX()));
							htm.put(IDBConstants.PAYMENTMODE, paymentmodes);
							htm.put("TranId", rcpNo);
							//htm.put("TranType", TransactionConstants.POS_TRANSACTION);
							htm.put(IDBConstants.LOC, loc);
							htm.put(IDBConstants.POS_DISCOUNT, String.valueOf(product.getDISCOUNT()));
							htm.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
							htm.put(IDBConstants.CREATED_BY, login_user);
							htrecv.put(IConstants.ITEM, product.getITEM());
							htrecv.put(IConstants.QTY, String.valueOf(product.getStkqty()));
							htrecv.put(IConstants.BATCH, product.getBATCH());
							//htrecv.put("DIRTYPE", TransactionConstants.POS_TRANSACTION);
							// added by radhika for nonstock flag
							nonstockflag = product.getNONSTOCKFLAG();
							// Deducting from inventory
							String tranid = (String) session.getAttribute("tranid");
							htrecv.put(IConstants.MOVHIS_ORDNUM, tranid);
							try {
								if (!posType.equalsIgnoreCase("NO_INV_CHECK") && !nonstockflag.equalsIgnoreCase("Y")) {
									flag = miscutil.process_PosIssueMaterial(htrecv);

									if (flag == false)
										errmsg = "Not Enough Inventory";
									// System.out.print("After issuing"+flag);
								} else {
									flag = processMovHisForPosWoInvCheck(htrecv);

								}
								salesdao.setmLogger(mLogger);
								if (flag == true)
									flag = salesdao.insertIntoSalesDet(htm);

							} catch (Exception e) {
								flag = false;
								DbBean.RollbackTran(ut);
								throw e;

							}

						}

						if (flag == true) {
							for (int paymode = 0; paymode < paymentModeArray.length; paymode++) {
								insPaymentHt.put(IDBConstants.RECEIPTNO, rcpNo);
								insPaymentHt.put(IDBConstants.PLANT, PLANT);
								String paymentMode = paymentModeArray[paymode];
								insPaymentHt.put(IDBConstants.PAYMENTMODE, paymentMode);
								insPaymentHt.put(IDBConstants.AMOUNT, amountsToSaveArray[paymode]);
								flag = paymentDAO.insertPOSPaymentDetails(insPaymentHt);
							}

						}
						if (flag == true)
							// update POSHDR AND PODET To Close Status
							buylist.removeAllElements();

						Hashtable<String, String> ht = new Hashtable<String, String>();
						ht.put(IDBConstants.PLANT, PLANT);
						ht.put(IDBConstants.POS_TRANID, tranID);
						String QryUpdate = " SET STATUS ='C',RECEIPTNO='" + rcpNo + "',UPAT ='" + DateUtils.getDate()
								+ "' ";
						boolean posHsrUpt = posHdr.update(QryUpdate, ht, "");
						if (posHsrUpt) {
							QryUpdate = " SET TRANSTATUS ='C',UPAT ='" + DateUtils.getDate() + "' ";
							posdet.update(QryUpdate, ht, "");
						}

						session.setAttribute("poslist", buylist);
						DbBean.CommitTran(ut);

						if (flag == false) {
							DbBean.RollbackTran(ut);
							session.setAttribute("errmsg", errmsg);
							response.sendRedirect(
									"jsp/" + posPageToDirect + "?TRANID=" + tranID + "&CHKBATCH=" + batchChecked);
						} else {
							// viewPOSReport(request, response, rcpNo);
							session.setAttribute("errmsg", "");
							buylist.removeAllElements();
							session.setAttribute("poslist", buylist);
							session.setAttribute("LOCATION", loc);
							// response.sendRedirect("jsp/"+posPageToDirect);

							response.setContentType("text/html");
							PrintWriter out = response.getWriter();
							out.write("Success:" + rcpNo);
							out.close();

						}

					} catch (Exception e) {
						session.setAttribute("errmsg", e.getMessage());
						response.sendRedirect(
								"jsp/" + posPageToDirect + "?TRANID=" + tranID + "&CHKBATCH=" + batchChecked);
					}
				} else {
					String recNo = request.getParameter("RCPNO");
					viewPOSReport(request, response, recNo);
				}
			} else if (cmdAction.equalsIgnoreCase("NEWTRANID")) {

				com.track.dao.TblControlDAO _TblControlDAO = new com.track.dao.TblControlDAO();
				_TblControlDAO.setmLogger(mLogger);
				String genPosTranid = _TblControlDAO.getNextOrder(PLANT, login_user, "GIRECEIPT");
				if (buylist != null) {
					buylist.removeAllElements();
					session.setAttribute("poslist", buylist);
					session.setAttribute("poslist", null);
					session.setAttribute("errmsg", "");
				}
				session.setAttribute("LOCATION", loc);
				response.sendRedirect(
						"jsp/" + posPageToDirect + "?TRANID=" + genPosTranid + "&LOC=" + loc + "&BATCH=NOBATCH");
						} else if (cmdAction.equalsIgnoreCase("TINEWTRANID")) {

				com.track.dao.TblControlDAO _TblControlDAO = new com.track.dao.TblControlDAO();
				_TblControlDAO.setmLogger(mLogger);
				String genPosTranid = _TblControlDAO.getNextOrder(PLANT, login_user, "TAXINVOICE");
				Boolean chkflag = checktrans(PLANT,genPosTranid);				
				if (buylist != null) {
					buylist.removeAllElements();
					session.setAttribute("poslist", buylist);
					session.setAttribute("poslist", null);
					session.setAttribute("errmsg", "");
				}
				session.setAttribute("LOCATION", loc);
				response.sendRedirect(
						"jsp/" + posPageToDirect + "?TRANID=" + genPosTranid + "&LOC=" + loc + "&BATCH=NOBATCH");
			} else if (cmdAction.equalsIgnoreCase("PrintInvoiceWP")) {

				viewTaxInvoiceReport(request, response, tranID);
				
			}  else if (cmdAction.equalsIgnoreCase("ViewTran")) {
				String cmd = "ViewTran";
				Vector<ITEMMST> veclist = this.getItemDetailsForPOSTranId(request, tranID, buylist);
				session.setAttribute("poslist", veclist);
				if(veclist.size() > 0)
				{
				Hashtable htv1 = new Hashtable();
				htv1.put(IDBConstants.PLANT, PLANT);
				htv1.put(IDBConstants.POS_TRANID, tranID);
				ArrayList prdlist = posHdr.selectPosHdr("POSTRANID as TRANID,STATUS,PersonInCharge as PERSON_INCHARGE,contactNum as CONTACT_NUM,REMARK1,REMARKS as REMARK2,REMARK3,CustCode as CUST_CODE,CustName as CUST_NAME,RSNCODE as REASONCODE,JobNum as REFERENCENO,Address as ADD1,Address2 as ADD2,Address3 as ADD3,'' as ADD4,EMPNO as EMP_NAME,ORDERTYPE,DELIVERYDATE,SHIPPINGID,SHIPPINGCUSTOMER,ORDERDISCOUNT,SHIPPINGCOST,INCOTERMS,OUTBOUND_GST as GST,'' as EDIT,ISNULL(CashCust,'') as CashCust,ISNULL((select CUSTOMER_TYPE_ID from "+PLANT+"_CUSTMST where CustCode=CUSTNO),'') as CUSTOMERTYPEDESC,ISNULL(DELIVERYDATEFORMAT,'') as DELIVERYDATEFORMAT,ISNULL(PAYMENTTYPE,'') as PAYMENTTYPE,CURRENCYID as currencyid",htv1);
				session.setAttribute("prdlist", prdlist);
				}
				response.sendRedirect("jsp/" + posPageToDirect + "?TRANID=" + tranID + "&CHKBATCH=" + batchChecked
						+ "&LOC=" + loc + "&cmd=" + cmd+"&STATUS=OPEN");
				
			}  else if (cmdAction.equalsIgnoreCase("EditTran")) {
				String cmd = "EditTran";
				boolean flag= checktrans(PLANT,tranID);
				Vector<ITEMMST> veclist = this.editItemDetailsForPOSTranId(request, tranID, buylist);
				
				session.setAttribute("poslist", veclist);
				if(veclist.size() > 0)
				{
				Hashtable htv1 = new Hashtable();
				htv1.put(IDBConstants.PLANT, PLANT);
				htv1.put(IDBConstants.POS_TRANID, tranID);
				ArrayList prdlist = posHdr.selectPosHdr("POSTRANID as TRANID,PersonInCharge as PERSON_INCHARGE,contactNum as CONTACT_NUM,REMARK1,REMARKS as REMARK2,REMARK3,CustCode as CUST_CODE,CustName as CUST_NAME,RSNCODE as REASONCODE,JobNum as REFERENCENO,Address as ADD1,Address2 as ADD2,Address3 as ADD3,'' as ADD4,EMPNO as EMP_NAME,ORDERTYPE,DELIVERYDATE,SHIPPINGID,SHIPPINGCUSTOMER,ORDERDISCOUNT,SHIPPINGCOST,INCOTERMS,OUTBOUND_GST as GST,'EDIT' as EDIT,ISNULL(CashCust,'') as CashCust,ISNULL((select CUSTOMER_TYPE_ID from "+PLANT+"_CUSTMST where CustCode=CUSTNO),'') as CUSTOMERTYPEDESC,ISNULL(DELIVERYDATEFORMAT,'') as DELIVERYDATEFORMAT,ISNULL(PAYMENTTYPE,'') as PAYMENTTYPE,CURRENCYID as currencyid",htv1);
				session.setAttribute("prdlist", prdlist);
				}
				response.sendRedirect("jsp/" + posPageToDirect + "?TRANID=" + tranID + "&CHKBATCH=" + batchChecked
						+ "&LOC=" + loc + "&cmd=" + cmd);
						
			} else if (action.equalsIgnoreCase("ADDSTOCKMOVEPRODUCT")) {

				String expireDate = "", refno = "";
				refno = request.getParameter("REFERENCENO");

				String empID = StrUtils.fString(request.getParameter("EMP_ID")).trim();
				String refNo = StrUtils.fString(request.getParameter("REFERENCENO")).trim();

				String serialized = StrUtils.fString(request.getParameter("serialized")).trim();
				String defaultQty = StrUtils.fString(request.getParameter("defaultQty")).trim();
				String expirydate = StrUtils.fString(request.getParameter("expirydate")).trim();
				String tranDate = StrUtils.fString(request.getParameter("TRANSACTIONDATE")).trim();

				String holdReasonCode = StrUtils.fString(request.getParameter("REASONCODE")).trim();
				String holdRemarks = StrUtils.fString(request.getParameter("REMARKS")).trim();

				loc = StrUtils.fString(request.getParameter("FROM_LOC")).trim();
				String to_LOC = StrUtils.fString(request.getParameter("TOLOC")).trim();
				String fromlocdesc = StrUtils.fString(request.getParameter("FROM_LOCDESC")).trim();
				String tolocdesc = StrUtils.fString(request.getParameter("TOLOCDESC")).trim();
				if (loc.length() > 0) {

					UserLocUtil uslocUtil = new UserLocUtil();
					uslocUtil.setmLogger(mLogger);
					boolean isvalidlocforUser = uslocUtil.isValidLocInLocmstForUser(PLANT, login_user, loc);

					if (isvalidlocforUser) {
						isvalidlocforUser = uslocUtil.isValidLocInLocmstForUser(PLANT, login_user, to_LOC);
						if (!isvalidlocforUser) {

							session.setAttribute("errmsg",
									"To LOC : " + to_LOC + " is not a User Assigned Location/Valid Location ");
							// response.sendRedirect("jsp/"+posPageToDirect+".jsp&LOC="+loc);
							RequestDispatcher view = request.getRequestDispatcher(
									"jsp/" + posPageToDirect + "?FROM_LOC=" + loc + "&TO_LOC=" + to_LOC);
							view.forward(request, response);

						} else {

							if (item.length() > 0) {
								boolean itemFound = true;
								ItemMstDAO itM = new ItemMstDAO();
								itM.setmLogger(mLogger);
								String scannedItemNo = itM.getItemIdFromAlternate(PLANT, item);
								if (scannedItemNo == null) {
									itemFound = false;
								} else {
									item = scannedItemNo;
								}
								if (itemFound) {

									ITEMMST items = getItemDetails(request, item, scqty, batch);
									ITEMMST prodbean1 = items;
									nonstockflag = prodbean1.getNONSTOCKFLAG();
									/* if(!nonstockflag.equalsIgnoreCase("Y")){ */

									items.setEmpNo(empID);
									items.setRefNo(refNo);
									items.setTranDate(tranDate);
									items.setReasonCode(reasoncode);
									items.setRemarks(remarks);
									Hashtable ht1 = new Hashtable();
									ht1.put(IConstants.PLANT, PLANT);
									ht1.put(IConstants.LOGIN_USER, (String) session.getAttribute("LOGIN_USER"));
									ht1.put(IConstants.LOC, loc);
									ht1.put(IConstants.ITEM, prodbean1.getITEM());
									ht1.put(IConstants.BATCH, prodbean1.getBATCH());
									String stkqty1 = String.valueOf(prodbean1.getStkqty());
									//stkqty1 = StrUtils.TrunkateDecimalForImportData(stkqty1);
									ht1.put(IConstants.QTY, stkqty1);
									nonstockflag = prodbean1.getNONSTOCKFLAG();
									if (!nonstockflag.equalsIgnoreCase("Y")) {
										// boolean invAvailable=CheckInvMst(ht1,request,response);

										boolean invAvailable = false;
										// invAvailable=CheckInvMst(ht1,request,response);
										try {
											invAvailable = CheckInvMstForGoddsIssue(ht1, request, response);

										} catch (Exception e) {
											InvFlag = true;
											session.setAttribute("errmsg", "Not Enough Inventory For Product:"
													+ prodbean1.getITEM() + "  with batch:" + prodbean1.getBATCH());
											String msg = session.getAttribute("errmsg").toString();
											session.setAttribute("poslist", buylist);
											e.printStackTrace(new PrintWriter(msg));
											throw e;
										}

										if (!invAvailable) { // response.sendRedirect("jsp/"+posPageToDirect+"?TRANID="+tranID+"&CHKBATCH="+batchChecked+"&LOC="+loc);
											session.setAttribute("errmsg", "Not Enough Inventory For Product:"
													+ prodbean1.getITEM() + "  with batch:" + prodbean1.getBATCH());
											String msg = session.getAttribute("errmsg").toString();
											session.setAttribute("poslist", buylist);
											RequestDispatcher view = request.getRequestDispatcher("jsp/"
													+ posPageToDirect + "?TRANID=" + tranID + "&CHKBATCH="
													+ batchChecked + "&FROM_LOC=" + loc + "&TO_LOC=" + to_LOC
													+ "&FROM_LOCDESC=" + fromlocdesc + "&TOLOCDESC=" + tolocdesc);
											view.forward(request, response);
											return;
										}

									}

									if (buylist == null) {
										match = false;
										buylist = new Vector<ITEMMST>();
										buylist.addElement(items);
										// To check transaction already exists in POSHeader
										Hashtable<String, String> htTrandId = new Hashtable<String, String>();
										htTrandId.put("POSTRANID", tranID);
										boolean istranidExist = _posServlet.isExisit(PLANT, htTrandId);
										boolean istranidExist1 = _posServlet.isRecvtranIDExisit(PLANT, htTrandId);
										if (istranidExist1 == true) {
											try {
												throw new Exception();
											} catch (Exception e) {
												session.setAttribute("errmsg",
														"Tran Id : " + tranID + " Exists already! ");
												String msg = session.getAttribute("errmsg").toString();
												e.printStackTrace(new PrintWriter(msg));
												throw e;
											}
										}
										if (istranidExist == false) {
											String genPosTranid = poshdr.genPosManualTranId(PLANT, login_user, loc,
													tranID);
										}
										// To check transaction already exists in POSHeader end
										Hashtable<String, String> ht = new Hashtable<String, String>();
										ht.put(IDBConstants.PLANT, PLANT);
										ht.put(IDBConstants.POS_TRANID, tranID);
										String QryUpdate = " SET STATUS ='O',UPAT ='" + DateUtils.getDate()
												+ "',EMPNO='" + empID + "',REFNO='" + refNo + "',TRANDT='" + tranDate
												+ "',RSNCODE='" + holdReasonCode + "',REMARKS='" + holdRemarks
												+ "',LOC='" + loc + "',TOLOC='" + to_LOC + "'";
										boolean posHsrUpt = posHdr.update(QryUpdate, ht, "");
										if (posHsrUpt) {

											ht.put(IDBConstants.POS_ITEM, items.getITEM());
											ht.put(IDBConstants.POS_ITEMDESC,
													strUtils.InsertQuotes(items.getITEMDESC()));
											ht.put(IDBConstants.POS_QTY, String.valueOf(items.getStkqty()));
											ht.put(IDBConstants.POS_LOC, loc);
											ht.put(IDBConstants.POS_TOLOC, to_LOC);
											ht.put(IDBConstants.POSDET_STATUS, "N");
											ht.put(IDBConstants.POS_UNITPRICE, "0");
											ht.put(IDBConstants.POS_TOTALPRICE, "0");
											ht.put(IDBConstants.POS_TRANDT, DateUtils.getDate());
											ht.put(IDBConstants.POS_TRANTM, DateUtils.Time());
											ht.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
											ht.put(IDBConstants.CREATED_BY, login_user);
											ht.put(IDBConstants.POS_BATCH, batch);
											// ht.put(IDBConstants.REFNO,refno);
											ht.put("POSTYPE", TRANTYPE);
											try {

												posdet.insertIntoPosDet(ht);
												new TblControlUtil().updateTblControlSeqNo(PLANT, "GIRECEIPT", "GI",
														tranID);

											} catch (Exception e) {
												session.setAttribute("errmsg",
														"Tran Id : " + tranID + " Exists already! ");
												String msg = session.getAttribute("errmsg").toString();
												e.printStackTrace(new PrintWriter(msg));
												throw e;
											}
										}

									} else {
										for (int i = 0; i < buylist.size(); i++) {

											ITEMMST itembean = buylist.elementAt(i);

											if (itembean.getITEM().equalsIgnoreCase(items.getITEM())
													&& itembean.getBATCH().equalsIgnoreCase(items.getBATCH())) {
												if (serialized.equalsIgnoreCase("1")) {
													session.setAttribute("errmsg",
															"Duplicates are not allowed when serialized");
													match = true;
												} else {
													if (scqty > 1) {
														itembean.setStkqty((itembean.getStkqty() + scqty));
														scqty = itembean.getStkqty();
													} else {
														itembean.setStkqty((itembean.getStkqty() + 1));
														scqty = itembean.getStkqty();
													}

													buylist.setElementAt(itembean, i);
													match = true;
													Hashtable<String, String> ht = new Hashtable<String, String>();
													ht.put(IDBConstants.PLANT, PLANT);
													ht.put(IDBConstants.POS_TRANID, tranID);
													ht.put(IDBConstants.POS_ITEM, items.getITEM());
													ht.put(IDBConstants.POS_BATCH, items.getBATCH());
													String Qry = " SET QTY ='" + itembean.getStkqty() + "'";
													posdet.update(Qry, ht, "");
													// update Qty for Same Item
												}
											}
										}
										if (!match) {
											buylist.addElement(items);
											// Insert into posdet with status as N
											Hashtable<String, String> ht = new Hashtable<String, String>();
											ht.put(IDBConstants.PLANT, PLANT);
											ht.put(IDBConstants.POS_TRANID, tranID);
											ht.put(IDBConstants.POS_ITEM, items.getITEM());
											ht.put(IDBConstants.POS_ITEMDESC,
													strUtils.InsertQuotes(items.getITEMDESC()));
											ht.put(IDBConstants.POS_QTY, String.valueOf(items.getStkqty()));
											ht.put(IDBConstants.POS_LOC, loc);
											ht.put(IDBConstants.POS_DISCOUNT, "0");
											ht.put(IDBConstants.POSDET_STATUS, "N");
											ht.put(IDBConstants.POS_UNITPRICE, "0");
											ht.put(IDBConstants.POS_TOTALPRICE, "0");
											ht.put(IDBConstants.POS_TRANDT, DateUtils.getDate());
											ht.put(IDBConstants.POS_TRANTM, DateUtils.Time());
											ht.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
											ht.put(IDBConstants.CREATED_BY, login_user);
											ht.put(IDBConstants.POS_BATCH, batch);
											// ht.put(IDBConstants.REFNO,refno);
											ht.put("POSTYPE", TRANTYPE);

											try {

												Hashtable<String, String> htTrandId = new Hashtable<String, String>();
												htTrandId.put("POSTRANID", tranID);
												boolean istranidExist1 = _posServlet.isRecvtranIDExisit(PLANT,
														htTrandId);
												if (istranidExist1 == true) {

													throw new Exception();
												}
												posdet.insertIntoPosDet(ht);
												new TblControlUtil().updateTblControlSeqNo(PLANT, "GIRECEIPT", "GI",
														tranID);

											} catch (Exception e) {
												getlist.remove(items);
												session.setAttribute("errmsg",
														"Tran Id : " + tranID + " Exists already! ");
												String msg = session.getAttribute("errmsg").toString();
												e.printStackTrace(new PrintWriter(msg));
												throw e;
											}
										}
									}
									session.setAttribute("poslist", buylist);
									session.setAttribute("LOCATION", loc);
									if (!posType.equalsIgnoreCase("NO_INV_CHECK")) {
										boolean checkarry[] = new boolean[buylist.size()];
										int index = 0;
										Vector newbuylst = (Vector) session.getAttribute("poslist");
										// Check qty exists in inventory
										for (int i = 0; i < newbuylst.size(); i++) {
											Hashtable ht = new Hashtable();
											ITEMMST prodbean = (ITEMMST) newbuylst.elementAt(i);

											ht.put(IConstants.PLANT, PLANT);
											ht.put(IConstants.LOGIN_USER, (String) session.getAttribute("LOGIN_USER"));
											ht.put(IConstants.LOC, loc);
											ht.put(IConstants.ITEM, prodbean.getITEM());
											ht.put(IConstants.BATCH, prodbean.getBATCH());
											String stkqty = String.valueOf(prodbean.getStkqty());
											//stkqty = StrUtils.TrunkateDecimalForImportData(stkqty);
											ht.put(IConstants.QTY, stkqty);
											nonstockflag = prodbean.getNONSTOCKFLAG();
											if (!nonstockflag.equalsIgnoreCase("Y")) {
												checkarry[index] = CheckInvMst(ht, request, response);
												try {
													checkarry[index] = CheckInvMst(ht, request, response);

												} catch (Exception e) {
													InvFlag = true;
													session.setAttribute("errmsg",
															"Not Enough Inventory For Product:" + prodbean.getITEM()
																	+ "  with batch:" + prodbean.getBATCH());
													String msg = session.getAttribute("errmsg").toString();
													session.setAttribute("poslist", buylist);
													e.printStackTrace(new PrintWriter(msg));
													throw e;
												}

											}

											if (checkarry[index] == true) { // response.sendRedirect("jsp/"+posPageToDirect+"?TRANID="+tranID+"&CHKBATCH="+batchChecked+"&LOC="+loc);
												RequestDispatcher view = request.getRequestDispatcher("jsp/"
														+ posPageToDirect + "?TRANID=" + tranID + "&CHKBATCH="
														+ batchChecked + "&FROM_LOC=" + loc + "&TO_LOC=" + to_LOC
														+ "&FROM_LOCDESC=" + fromlocdesc + "&TOLOCDESC=" + tolocdesc);
												view.forward(request, response);
											}

											index++;
										}
									}
									/*
									 * }
									 * 
									 * else{ session.setAttribute("errmsg", "Product Id is non inventory product");
									 * }
									 */
								} else {
									session.setAttribute("errmsg", "Product Id not found");
								}

							}
							// session.setAttribute("errmsg", "");
							// response.sendRedirect("jsp/"+posPageToDirect+"?TRANID="+tranID+"&CHKBATCH="+batchChecked+"&LOC="+loc);
							RequestDispatcher view = request.getRequestDispatcher("jsp/" + posPageToDirect + "?TRANID="
									+ tranID + "&CHKBATCH=" + batchChecked + "&FROM_LOC=" + loc + "&TO_LOC=" + to_LOC);
							view.forward(request, response);

						}
					} else {
						session.setAttribute("errmsg",
								"From LOC : " + loc + " is not a User Assigned Location/Valid Location ");
						// response.sendRedirect("jsp/"+posPageToDirect+".jsp&LOC="+loc);
						RequestDispatcher view = request.getRequestDispatcher(
								"jsp/" + posPageToDirect + "?FROM_LOC=" + loc + "&TO_LOC=" + to_LOC);
						view.forward(request, response);
					}
				} else {
					session.setAttribute("errmsg", "Please Enter Location");
					// response.sendRedirect("jsp/"+posPageToDirect+"&LOC="+loc);
					RequestDispatcher view = request
							.getRequestDispatcher("jsp/" + posPageToDirect + "?FROM_LOC=" + loc + "&TO_LOC=" + to_LOC);
					view.forward(request, response);
				}

			} else if (action.equalsIgnoreCase("deleteMessage")) {
				String[] chkdLnNo = request.getParameterValues("chkdLnNo");
				userBean _userBean = new userBean();
				for (int msg = 0; msg < chkdLnNo.length; msg++)

				{

					String messageID = chkdLnNo[msg];
					String s = "";

					Hashtable ht = new Hashtable();
					ht.put("MESSAGEID", messageID);

					_userBean.deleteMessage(ht);

				}
				posPageToDirect = "importantMessagesforUsers.jsp";

				RequestDispatcher view = request.getRequestDispatcher("jsp/" + posPageToDirect);
				view.forward(request, response);

			} else if (action.equalsIgnoreCase("deletePasswordMessage")) {
				String messageid = StrUtils.fString(request.getParameter("messageid"));
				userBean _userBean = new userBean();

				Hashtable ht = new Hashtable();
				ht.put("MESSAGEID", messageid);

				_userBean.deleteMessage(ht);

				posPageToDirect = "changePasswordMessages.jsp";

				RequestDispatcher view = request.getRequestDispatcher("jsp/" + posPageToDirect);
				view.forward(request, response);

			} else if (action.equalsIgnoreCase("Updatepasswordmessage")) {
				StrUtils _StrUtils = null;
				String plant = StrUtils.fString((String) session.getAttribute("PLANT"));
				String sUserId = (String) session.getAttribute("LOGIN_USER");
				String fileLocation = "", filetempLocation = "", result2 = "", strpath = "", sImagePath = "",
						imgtempath = "", message = "", messageid = "";
				// String message = StrUtils.fString(request.getParameter("description"));
				// String messageid2 = StrUtils.fString(request.getParameter("messageid"));
				userBean _userBean = new userBean();

				boolean isMultipart = ServletFileUpload.isMultipartContent(request);

				fileLocation = DbBean.CHANGEPASSWORDLOGO_IMAGE_PATH + "/" + plant;
				filetempLocation = DbBean.CHANGEPASSWORDLOGO_IMAGE_PATH + "/temp" + "/" + plant;
				boolean imageSizeflg = false;

				File filePath = null;

				if (!isMultipart) {
					System.out.println("File Not Uploaded");
				} else {
					FileItemFactory factory = new DiskFileItemFactory();
					ServletFileUpload upload = new ServletFileUpload(factory);

					List items = upload.parseRequest(request);
					Iterator iterator = items.iterator();
					while (iterator.hasNext()) {
						FileItem item1 = (FileItem) iterator.next();
						if (item1.isFormField()) {
							if (item1.getFieldName().equalsIgnoreCase("messageid")) {
								messageid = item1.getString();
							}
							if (item1.getFieldName().equalsIgnoreCase("description")) {
								message = item1.getString();
							}
							if (item1.getFieldName().equalsIgnoreCase("imgtempath")) {
								imgtempath = item1.getString();
							}

						} else if (!item1.isFormField() && (item1.getName().length() > 0)) {
							String fileName = item1.getName();
							long size = item1.getSize();

							size = size / 1024;
							// size = size / 1000;
							System.out.println("size of the Image imported :::" + size);
							// checking image size for 2MB
							if (size > 2040) // condtn checking Image size
							{
								String result = "<font color=\"red\">  Employee Image size greater than 1 MB </font>";

								imageSizeflg = true;

							}
							File path = new File(fileLocation);
							if (!path.exists()) {
								boolean status = path.mkdirs();
							}
							fileName = "companylogo.jpeg";
							File uploadedFile = new File(path + "/" + fileName);

							strpath = path + "/" + fileName;
							String catlogpath = uploadedFile.getAbsolutePath();

							File filePath1 = new File(catlogpath);

							if (filePath1.exists()) {
								filePath1.delete();
							}

							if (!imageSizeflg && !uploadedFile.exists())
								item1.write(uploadedFile);

							// delete temp uploaded file
							File tempPath = new File(filetempLocation);
							if (tempPath.exists()) {
								File tempUploadedfile = new File(tempPath + "/" + fileName);
								if (tempUploadedfile.exists()) {
									tempUploadedfile.delete();
								}
							}

						}

					}
				}

				/*
				 * String message = StrUtils.fString(request.getParameter("description"));
				 * String messageid = StrUtils.fString(request.getParameter("messageid"));
				 * userBean _userBean = new userBean();
				 */

				Hashtable ht = new Hashtable();
				if (strpath == "") {
					ht.put("LOGOPATH", imgtempath);
				} else {
					ht.put("LOGOPATH", strpath);
				}

				ht.put("MESSAGEID", messageid);
				ht.put("MESSAGE", message);

				_userBean.Updatepasswordmessage(ht);

				posPageToDirect = "changePasswordMessages.jsp";

				RequestDispatcher view = request.getRequestDispatcher("jsp/" + posPageToDirect);
				view.forward(request, response);

			} else if (action.equalsIgnoreCase("removepasswordlogo")) {
				/*StrUtils _StrUtils = null;
				String plant = StrUtils.fString((String) session.getAttribute("PLANT"));
				String sUserId = (String) session.getAttribute("LOGIN_USER");
				String fileLocation = "", filetempLocation = "", result2 = "", strpath = "", sImagePath = "",
						message = "", messageid = "";
				// String message = StrUtils.fString(request.getParameter("description"));
				// String messageid2 = StrUtils.fString(request.getParameter("messageid"));
				userBean _userBean = new userBean();

				boolean isMultipart = ServletFileUpload.isMultipartContent(request);

				fileLocation = DbBean.CHANGEPASSWORDLOGO_IMAGE_PATH + "/" + plant;
				filetempLocation = DbBean.CHANGEPASSWORDLOGO_IMAGE_PATH + "/temp" + "/" + plant;
				boolean imageSizeflg = false;

				File filePath = null;

				if (!isMultipart) {
					System.out.println("File Not Uploaded");
				} else {
					FileItemFactory factory = new DiskFileItemFactory();
					ServletFileUpload upload = new ServletFileUpload(factory);

					List items = upload.parseRequest(request);
					Iterator iterator = items.iterator();

					while (iterator.hasNext()) {
						FileItem item1 = (FileItem) iterator.next();
						if (item1.isFormField()) {
							if (item1.getFieldName().equalsIgnoreCase("messageid")) {
								messageid = item1.getString();
							}*/
							/*
							 * else if (item.getFieldName() .equalsIgnoreCase("USER_ID1")) { USER_ID1 =
							 * item.getString(); }
							 */
						/*} else {

							String fileName = item1.getName();

							File path = new File(fileLocation);
							_StrUtils = new StrUtils();
							if (!path.exists()) {
								boolean status = path.mkdirs();
							}
							fileName = "companylogo.jpeg";
							File uploadedFile = new File(path + "/" + fileName);

							strpath = path + "/" + fileName;
							filePath = new File(strpath);
						}
					}

					Hashtable ht = new Hashtable();
					ht.put("LOGOPATH", "");
					ht.put("MESSAGEID", messageid);
					boolean flag = _userBean.RemovePasswordLogo(ht);
					if (flag) {
						if (filePath.exists()) {
							filePath.delete();
						}
						String result = "<font color=\"green\">Image Deleted successfully</font>";

						response.setContentType("text/html");// for return the success message
						PrintWriter out = response.getWriter();
						out.write(result);
						out.close();
						RequestDispatcher view = request
								.getRequestDispatcher("jsp/changePasswordMessages.jsp?result=" + result);
					}*/

					/*
					 * posPageToDirect = "changePasswordMessages.jsp";
					 * 
					 * RequestDispatcher view =
					 * request.getRequestDispatcher("jsp/"+posPageToDirect); view.forward(request,
					 * response);
					 */

				//}

			} else if (action.equalsIgnoreCase("ADDBULKSTOCKMOVEPRODUCT")) {

				String expireDate = "", refno = "";
				refno = request.getParameter("REFERENCENO");

				String empID = StrUtils.fString(request.getParameter("EMP_ID")).trim();
				String refNo = StrUtils.fString(request.getParameter("REFERENCENO")).trim();

				String serialized = StrUtils.fString(request.getParameter("serialized")).trim();
				String tranDate = StrUtils.fString(request.getParameter("TRANSACTIONDATE")).trim();

				String holdReasonCode = StrUtils.fString(request.getParameter("REASONCODE")).trim();
				String holdRemarks = StrUtils.fString(request.getParameter("REMARKS")).trim();
				// String trantype = StrUtils.fString(request.getParameter("TRANTYPE")).trim();

				loc = StrUtils.fString(request.getParameter("LOC0")).trim();
				String to_LOC = StrUtils.fString(request.getParameter("TOLOC")).trim();
				String fromlocdesc = StrUtils.fString(request.getParameter("FROM_LOCDESC")).trim();
				String tolocdesc = StrUtils.fString(request.getParameter("TOLOCDESC")).trim();
				if (loc.length() > 0) {

					UserLocUtil uslocUtil = new UserLocUtil();
					uslocUtil.setmLogger(mLogger);
					boolean isvalidlocforUser = uslocUtil.isValidLocInLocmstForUser(PLANT, login_user, loc);

					if (isvalidlocforUser) {
						isvalidlocforUser = uslocUtil.isValidLocInLocmstForUser(PLANT, login_user, to_LOC);
						if (!isvalidlocforUser) {

							session.setAttribute("errmsg",
									"To LOC : " + to_LOC + " is not a User Assigned Location/Valid Location ");
							// response.sendRedirect("jsp/"+posPageToDirect+".jsp&LOC="+loc);
							RequestDispatcher view = request.getRequestDispatcher(
									"jsp/" + posPageToDirect + "?FROM_LOC=" + loc + "&TO_LOC=" + to_LOC);
							view.forward(request, response);

						} else {
							String[] chkdLnNo = request.getParameterValues("chkdLnNo");
							for (int prod1 = 0; prod1 < chkdLnNo.length; prod1++)

							{

								String checkValue = chkdLnNo[prod1];
								item = StrUtils.fString(request.getParameter("item" + checkValue));

								batch = StrUtils.fString(request.getParameter("nobatch" + checkValue));
								Double qty = Double
										.parseDouble(StrUtils.fString(request.getParameter("QTY" + checkValue)));
								// scqty = Integer.parseInt(StrUtils.fString(request.getParameter("QTY"+item)));

								scqty = qty.intValue();

								if (item.length() > 0) {
									boolean itemFound = true;
									ItemMstDAO itM = new ItemMstDAO();
									itM.setmLogger(mLogger);
									String scannedItemNo = itM.getItemIdFromAlternate(PLANT, item);
									if (scannedItemNo == null) {
										itemFound = false;
									} else {
										item = scannedItemNo;
									}
									if (itemFound) {

										ITEMMST items = getItemDetails(request, item, scqty, batch);
										ITEMMST prodbean1 = items;
										nonstockflag = prodbean1.getNONSTOCKFLAG();
										/* if(!nonstockflag.equalsIgnoreCase("Y")){ */

										items.setEmpNo(empID);
										items.setRefNo(refNo);
										items.setTranDate(tranDate);
										items.setReasonCode(reasoncode);
										items.setRemarks(remarks);

										Hashtable ht1 = new Hashtable();

										ht1.put(IConstants.PLANT, PLANT);
										ht1.put(IConstants.LOGIN_USER, (String) session.getAttribute("LOGIN_USER"));
										ht1.put(IConstants.LOC, loc);
										ht1.put(IConstants.ITEM, prodbean1.getITEM());
										ht1.put(IConstants.BATCH, prodbean1.getBATCH());
										String stkqty1 = String.valueOf(prodbean1.getStkqty());
										//stkqty1 = StrUtils.TrunkateDecimalForImportData(stkqty1);
										ht1.put(IConstants.QTY, stkqty1);
										nonstockflag = prodbean1.getNONSTOCKFLAG();
										if (!nonstockflag.equalsIgnoreCase("Y")) {
											// boolean invAvailable=CheckInvMst(ht1,request,response);

											boolean invAvailable = false;
											// invAvailable=CheckInvMst(ht1,request,response);
											try {
												invAvailable = CheckInvMstForGoddsIssue(ht1, request, response);

											} catch (Exception e) {
												InvFlag = true;
												session.setAttribute("errmsg", "Not Enough Inventory For Product:"
														+ prodbean1.getITEM() + "  with batch:" + prodbean1.getBATCH());
												String msg = session.getAttribute("errmsg").toString();
												session.setAttribute("poslist", buylist);
												e.printStackTrace(new PrintWriter(msg));
												throw e;
											}

											if (!invAvailable) { // response.sendRedirect("jsp/"+posPageToDirect+"?TRANID="+tranID+"&CHKBATCH="+batchChecked+"&LOC="+loc);
												session.setAttribute("errmsg", "Not Enough Inventory For Product:"
														+ prodbean1.getITEM() + "  with batch:" + prodbean1.getBATCH());
												String msg = session.getAttribute("errmsg").toString();
												session.setAttribute("poslist", buylist);
												RequestDispatcher view = request.getRequestDispatcher("jsp/"
														+ posPageToDirect + "?TRANID=" + tranID + "&CHKBATCH="
														+ batchChecked + "&FROM_LOC=" + loc + "&TO_LOC=" + to_LOC
														+ "&FROM_LOCDESC=" + fromlocdesc + "&TOLOCDESC=" + tolocdesc);
												view.forward(request, response);
												return;
											}

										}

										if (buylist == null) {
											match = false;
											buylist = new Vector<ITEMMST>();
											buylist.addElement(items);
											// To check transaction already exists in POSHeader
											Hashtable<String, String> htTrandId = new Hashtable<String, String>();
											htTrandId.put("POSTRANID", tranID);
											boolean istranidExist = _posServlet.isExisit(PLANT, htTrandId);
											boolean istranidExist1 = _posServlet.isRecvtranIDExisit(PLANT, htTrandId);
											if (istranidExist1 == true) {
												try {
													throw new Exception();
												} catch (Exception e) {
													session.setAttribute("errmsg",
															"Tran Id : " + tranID + " Exists already! ");
													String msg = session.getAttribute("errmsg").toString();
													e.printStackTrace(new PrintWriter(msg));
													throw e;
												}
											}
											if (istranidExist == false) {
												String genPosTranid = poshdr.genPosManualTranId(PLANT, login_user, loc,
														tranID);
											}
											// To check transaction already exists in POSHeader end
											Hashtable<String, String> ht = new Hashtable<String, String>();
											ht.put(IDBConstants.PLANT, PLANT);
											ht.put(IDBConstants.POS_TRANID, tranID);
											String QryUpdate = " SET STATUS ='O',UPAT ='" + DateUtils.getDate()
													+ "',EMPNO='" + empID + "',REFNO='" + refNo + "',TRANDT='"
													+ tranDate + "',RSNCODE='" + holdReasonCode + "',REMARKS='"
													+ holdRemarks + "',LOC='" + loc + "',TOLOC='" + to_LOC + "'";
											boolean posHsrUpt = posHdr.update(QryUpdate, ht, "");
											if (posHsrUpt) {

												ht.put(IDBConstants.POS_ITEM, items.getITEM());
												ht.put(IDBConstants.POS_ITEMDESC,
														strUtils.InsertQuotes(items.getITEMDESC()));
												ht.put(IDBConstants.POS_QTY, String.valueOf(items.getStkqty()));
												ht.put(IDBConstants.POS_LOC, loc);
												ht.put(IDBConstants.POS_TOLOC, to_LOC);
												ht.put(IDBConstants.POSDET_STATUS, "N");
												ht.put(IDBConstants.POS_UNITPRICE, "0");
												ht.put(IDBConstants.POS_TOTALPRICE, "0");
												ht.put(IDBConstants.POS_TRANDT, DateUtils.getDate());
												ht.put(IDBConstants.POS_TRANTM, DateUtils.Time());
												ht.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
												ht.put(IDBConstants.CREATED_BY, login_user);
												ht.put(IDBConstants.POS_BATCH, batch);
												// ht.put(IDBConstants.REFNO,refno);
												ht.put("POSTYPE", TRANTYPE);

												try {

													posdet.insertIntoPosDet(ht);
													new TblControlUtil().updateTblControlSeqNo(PLANT, "GIRECEIPT", "GI",
															tranID);

												} catch (Exception e) {
													session.setAttribute("errmsg",
															"Tran Id : " + tranID + " Exists already! ");
													String msg = session.getAttribute("errmsg").toString();
													e.printStackTrace(new PrintWriter(msg));
													throw e;
												}
											}

										} else {
											for (int i = 0; i < buylist.size(); i++) {

												ITEMMST itembean = buylist.elementAt(i);

												if (itembean.getITEM().equalsIgnoreCase(items.getITEM())
														&& itembean.getBATCH().equalsIgnoreCase(items.getBATCH())) {
													if (serialized.equalsIgnoreCase("1")) {
														session.setAttribute("errmsg",
																"Duplicates are not allowed when serialized");
														match = true;
													} else {
														if (scqty > 1) {
															itembean.setStkqty((itembean.getStkqty() + scqty));
															scqty = itembean.getStkqty();
														} else {
															itembean.setStkqty((itembean.getStkqty() + 1));
															scqty = itembean.getStkqty();
														}

														buylist.setElementAt(itembean, i);
														match = true;
														Hashtable<String, String> ht = new Hashtable<String, String>();
														ht.put(IDBConstants.PLANT, PLANT);
														ht.put(IDBConstants.POS_TRANID, tranID);
														ht.put(IDBConstants.POS_ITEM, items.getITEM());
														ht.put(IDBConstants.POS_BATCH, items.getBATCH());
														String Qry = " SET QTY ='" + itembean.getStkqty() + "'";
														posdet.update(Qry, ht, "");
														// update Qty for Same Item
													}
												}
											}
											if (!match) {
												buylist.addElement(items);
												// Insert into posdet with status as N
												Hashtable<String, String> ht = new Hashtable<String, String>();
												ht.put(IDBConstants.PLANT, PLANT);
												ht.put(IDBConstants.POS_TRANID, tranID);
												ht.put(IDBConstants.POS_ITEM, items.getITEM());
												ht.put(IDBConstants.POS_ITEMDESC,
														strUtils.InsertQuotes(items.getITEMDESC()));
												ht.put(IDBConstants.POS_QTY, String.valueOf(items.getStkqty()));
												ht.put(IDBConstants.POS_LOC, loc);
												ht.put(IDBConstants.POS_DISCOUNT, "0");
												ht.put(IDBConstants.POSDET_STATUS, "N");
												ht.put(IDBConstants.POS_UNITPRICE, "0");
												ht.put(IDBConstants.POS_TOTALPRICE, "0");
												ht.put(IDBConstants.POS_TRANDT, DateUtils.getDate());
												ht.put(IDBConstants.POS_TRANTM, DateUtils.Time());
												ht.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
												ht.put(IDBConstants.CREATED_BY, login_user);
												ht.put(IDBConstants.POS_BATCH, batch);
												// ht.put(IDBConstants.REFNO,refno);
												ht.put("POSTYPE", TRANTYPE);

												try {

													Hashtable<String, String> htTrandId = new Hashtable<String, String>();
													htTrandId.put("POSTRANID", tranID);
													boolean istranidExist1 = _posServlet.isRecvtranIDExisit(PLANT,
															htTrandId);
													if (istranidExist1 == true) {

														throw new Exception();
													}
													posdet.insertIntoPosDet(ht);
													new TblControlUtil().updateTblControlSeqNo(PLANT, "GIRECEIPT", "GI",
															tranID);

												} catch (Exception e) {
													getlist.remove(items);
													session.setAttribute("errmsg",
															"Tran Id : " + tranID + " Exists already! ");
													String msg = session.getAttribute("errmsg").toString();
													e.printStackTrace(new PrintWriter(msg));
													throw e;
												}
											}
										}
										session.setAttribute("poslist", buylist);
										session.setAttribute("LOCATION", loc);
										if (!posType.equalsIgnoreCase("NO_INV_CHECK")) {
											boolean checkarry[] = new boolean[buylist.size()];
											int index = 0;
											Vector newbuylst = (Vector) session.getAttribute("poslist");
											// Check qty exists in inventory
											for (int i = 0; i < newbuylst.size(); i++) {
												Hashtable ht = new Hashtable();
												ITEMMST prodbean = (ITEMMST) newbuylst.elementAt(i);

												ht.put(IConstants.PLANT, PLANT);
												ht.put(IConstants.LOGIN_USER,
														(String) session.getAttribute("LOGIN_USER"));
												ht.put(IConstants.LOC, loc);
												ht.put(IConstants.ITEM, prodbean.getITEM());
												ht.put(IConstants.BATCH, prodbean.getBATCH());
												String stkqty = String.valueOf(prodbean.getStkqty());
												//stkqty = StrUtils.TrunkateDecimalForImportData(stkqty);
												ht.put(IConstants.QTY, stkqty);
												nonstockflag = prodbean.getNONSTOCKFLAG();
												if (!nonstockflag.equalsIgnoreCase("Y")) {
													checkarry[index] = CheckInvMst(ht, request, response);
													try {
														checkarry[index] = CheckInvMst(ht, request, response);

													} catch (Exception e) {
														InvFlag = true;
														session.setAttribute("errmsg",
																"Not Enough Inventory For Product:" + prodbean.getITEM()
																		+ "  with batch:" + prodbean.getBATCH());
														String msg = session.getAttribute("errmsg").toString();
														session.setAttribute("poslist", buylist);
														e.printStackTrace(new PrintWriter(msg));
														throw e;
													}

												}

												if (checkarry[index] == true) { // response.sendRedirect("jsp/"+posPageToDirect+"?TRANID="+tranID+"&CHKBATCH="+batchChecked+"&LOC="+loc);
													RequestDispatcher view = request.getRequestDispatcher("jsp/"
															+ posPageToDirect + "?TRANID=" + tranID + "&CHKBATCH="
															+ batchChecked + "&FROM_LOC=" + loc + "&TO_LOC=" + to_LOC);
													view.forward(request, response);
												}

												index++;
											}
										}
										/*
										 * }
										 * 
										 * else{ session.setAttribute("errmsg", "Product Id is non inventory product");
										 * }
										 */
									} else {
										session.setAttribute("errmsg", "Product Id not found");
									}

								}
							}
							// session.setAttribute("errmsg", "");
							// response.sendRedirect("jsp/"+posPageToDirect+"?TRANID="+tranID+"&CHKBATCH="+batchChecked+"&LOC="+loc);
							RequestDispatcher view = request.getRequestDispatcher("jsp/" + posPageToDirect + "?TRANID="
									+ tranID + "&CHKBATCH=" + batchChecked + "&FROM_LOC=" + loc + "&TO_LOC=" + to_LOC);
							view.forward(request, response);

						}
					} else {
						session.setAttribute("errmsg",
								"From LOC : " + loc + " is not a User Assigned Location/Valid Location ");
						// response.sendRedirect("jsp/"+posPageToDirect+".jsp&LOC="+loc);
						RequestDispatcher view = request.getRequestDispatcher(
								"jsp/" + posPageToDirect + "?FROM_LOC=" + loc + "&TO_LOC=" + to_LOC);
						view.forward(request, response);
					}
				} else {
					session.setAttribute("errmsg", "Please Enter Location");
					// response.sendRedirect("jsp/"+posPageToDirect+"&LOC="+loc);
					RequestDispatcher view = request
							.getRequestDispatcher("jsp/" + posPageToDirect + "?FROM_LOC=" + loc + "&TO_LOC=" + to_LOC);
					view.forward(request, response);
				}

			} else if (cmdAction.equalsIgnoreCase("ADD") && TRANTYPE.equalsIgnoreCase("GOODSISSUEWITHBATCH")) {
				String cmd="ADD";
				String expireDate = "", refno = "";
				refno = request.getParameter("REFERENCENO");
				String empID = StrUtils.fString(request.getParameter("EMP_ID")).trim();
				String refNo = StrUtils.fString(request.getParameter("REFERENCENO")).trim();
				String serialized = StrUtils.fString(request.getParameter("serialized")).trim();
				String defualtQty = StrUtils.fString(request.getParameter("defaultQty")).trim();
				String bulkcheckout = StrUtils.fString(request.getParameter("bulkcheckout")).trim();
				String tranDate = StrUtils.fString(request.getParameter("TRANSACTIONDATE")).trim();
				String holdReasonCode = StrUtils.fString(request.getParameter("REASONCODE")).trim();
				String holdRemarks = StrUtils.fString(request.getParameter("REMARKS")).trim();
				String uom = StrUtils.fString(request.getParameter("UOM")).trim();
				expireDate = request.getParameter("EXPIREDATE");
				refno = request.getParameter("REFERENCENO");
				String empName = request.getParameter("EMP_NAME");
				loc = StrUtils.fString(request.getParameter("LOC")).trim();
				String locdesc = StrUtils.fString(request.getParameter("LOCDESC")).trim();

				if (loc.length() > 0) {

					UserLocUtil uslocUtil = new UserLocUtil();
					uslocUtil.setmLogger(mLogger);
					boolean isvalidlocforUser = uslocUtil.isValidLocInLocmstForUser(PLANT, login_user, loc);
					if (isvalidlocforUser) {
						if (item.length() > 0) {
							boolean itemFound = true;
							ItemMstDAO itM = new ItemMstDAO();
							itM.setmLogger(mLogger);
							String scannedItemNo = itM.getItemIdFromAlternate(PLANT, item);
							if (scannedItemNo == null) {
								itemFound = false;
							} else {
								item = scannedItemNo;
							}
							if (itemFound) {
								ITEMMST items = getItemDetails(request, item, scqty, batch);
								items.setBATCHID(batchId);
								ITEMMST prodbean1 = items;
								nonstockflag = prodbean1.getNONSTOCKFLAG();
								/* if(!nonstockflag.equalsIgnoreCase("Y")){ */
								items.setLoc(loc);
								items.setEmpNo(empID);
								items.setRefNo(refNo);
								items.setTranDate(tranDate);
								items.setReasonCode(reasoncode);
								items.setRemarks(remarks);
								items.setSTKUOM(uom);
								Hashtable ht1 = new Hashtable();

								ht1.put(IConstants.PLANT, PLANT);
								ht1.put(IConstants.LOGIN_USER, (String) session.getAttribute("LOGIN_USER"));
								ht1.put(IConstants.LOC, loc);
								ht1.put(IConstants.ITEM, prodbean1.getITEM());
								ht1.put(IConstants.BATCH, prodbean1.getBATCH());
								if (prodbean1.getBATCHID() != -1) {
									ht1.put(IConstants.BATCH_ID, String.valueOf(prodbean1.getBATCHID()));
								}
								String stkqty1 = String.valueOf(prodbean1.getStkqty());
								//stkqty1 = StrUtils.TrunkateDecimalForImportData(stkqty1);
								String UOMQTY="1";
								Hashtable<String, String> htTrand1 = new Hashtable<String, String>();
								MovHisDAO movHisDao1 = new MovHisDAO();
								ArrayList getuomqty = movHisDao1.selectForReport("select ISNULL(QPUOM,1) as UOMQTY from "+PLANT+"_UOM where UOM='"+uom+"'",htTrand1);
								if(getuomqty.size()>0)
								{
								Map mapval = (Map) getuomqty.get(0);
								UOMQTY=(String)mapval.get("UOMQTY");
								}
								double invumqty = Double.valueOf(stkqty1) * Double.valueOf(UOMQTY);
								ht1.put(IConstants.QTY, String.valueOf(invumqty));
								nonstockflag = prodbean1.getNONSTOCKFLAG();
								if (!nonstockflag.equalsIgnoreCase("Y")) {
									// boolean invAvailable=CheckInvMst(ht1,request,response);

									boolean invAvailable = false;
									// invAvailable=CheckInvMst(ht1,request,response);
									try {
										invAvailable = CheckInvMstForGoddsIssue(ht1, request, response);

									} catch (Exception e) {
										InvFlag = true;
										session.setAttribute("errmsg", "Not Enough Inventory For Product:"
												+ prodbean1.getITEM() + "  with batch:" + prodbean1.getBATCH());
										String msg = session.getAttribute("errmsg").toString();
										session.setAttribute("poslist", buylist);
										e.printStackTrace(new PrintWriter(msg));
										throw e;
									}

									if (!invAvailable) { // response.sendRedirect("jsp/"+posPageToDirect+"?TRANID="+tranID+"&CHKBATCH="+batchChecked+"&LOC="+loc);
										session.setAttribute("errmsg", "Not Enough Inventory For Product:"
												+ prodbean1.getITEM() + "  with batch:" + prodbean1.getBATCH());
										String msg = session.getAttribute("errmsg").toString();
										session.setAttribute("poslist", buylist);
										// RequestDispatcher view =
										// request.getRequestDispatcher("jsp/"+posPageToDirect+"?TRANID="+tranID+"&CHKBATCH="
										// +batchChecked+"&LOC="+loc);
										// view.forward(request, response);
										response.sendRedirect("jsp/" + posPageToDirect + "?TRANID=" + tranID
												+ "&CHKBATCH=" + batchChecked + "&LOC=" + loc);
										return;
									}

								}

								if (buylist == null || buylist.size() == 0) {
									match = false;
									buylist = new Vector<ITEMMST>();
									buylist.addElement(items);
									// To check transaction already exists in POSHeader
									Hashtable<String, String> htTrandId = new Hashtable<String, String>();
									htTrandId.put("POSTRANID", tranID);
									boolean istranidExist = _posServlet.isExisit(PLANT, htTrandId);
									boolean istranidExist1 = _posServlet.isRecvtranIDExisit(PLANT, htTrandId);
									if (istranidExist1 == true) {
										try {
											throw new Exception();
										} catch (Exception e) {
											session.setAttribute("errmsg", "Tran Id : " + tranID + " Exists already! ");
											String msg = session.getAttribute("errmsg").toString();
											e.printStackTrace(new PrintWriter(msg));
											throw e;
										}
									}
									if (istranidExist == false) {
										String genPosTranid = poshdr.genPosManualTranId(PLANT, login_user, loc, tranID);
									}
									// To check transaction already exists in POSHeader end
									Hashtable<String, String> ht = new Hashtable<String, String>();
									ht.put(IDBConstants.PLANT, PLANT);
									ht.put(IDBConstants.POS_TRANID, tranID);
									String QryUpdate = " SET STATUS ='O',UPAT ='" + DateUtils.getDate() + "',EMPNO='"
											+ empID + "',REFNO='" + refNo + "',TRANDT='" + tranDate + "',RSNCODE='"
											+ holdReasonCode + "',REMARKS='" + holdRemarks + "',LOC='" + loc + "' ";
									boolean posHsrUpt = posHdr.update(QryUpdate, ht, "");
									if (posHsrUpt) {

										ht.put(IDBConstants.POS_ITEM, items.getITEM());
										ht.put(IDBConstants.POS_ITEMDESC, strUtils.InsertQuotes(items.getITEMDESC()));
										ht.put(IDBConstants.POS_QTY, String.valueOf(items.getStkqty()));
										ht.put(IDBConstants.POS_LOC, loc);

										ht.put(IDBConstants.POSDET_STATUS, "N");
										ht.put(IDBConstants.POS_UNITPRICE, "0");
										ht.put(IDBConstants.POS_TOTALPRICE, "0");
										ht.put(IDBConstants.POS_TRANDT, DateUtils.getDate());
										ht.put(IDBConstants.POS_TRANTM, DateUtils.Time());
										ht.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
										ht.put(IDBConstants.CREATED_BY, login_user);
										ht.put(IDBConstants.POS_BATCH, batch);
										// ht.put(IDBConstants.REFNO,refno);
										ht.put("POSTYPE", TRANTYPE);
										ht.put(IConstants.UNITMO,uom);
										ht.put(IConstants.OUT_DOLNNO, Integer.toString(1));
										
										try {
											//Check POS_DET Data Already Exists - Azees 4.8.19
											Hashtable<String, String> htup = new Hashtable<String, String>();
											htup.put(IDBConstants.PLANT, PLANT);
											htup.put(IDBConstants.POS_TRANID, tranID);
											boolean chkpos = posdet.isExisit(htup,"");
											if(chkpos) /* Check if any line items exists for TrainId before adding the first line item*/
											{
												posdet.deletePosTranId(PLANT, tranID); /* Delete all the previous line items*/
											}
											htup.put(IDBConstants.POS_ITEM, items.getITEM());
											htup.put(IDBConstants.POS_BATCH, batch);											
											htup.put(IDBConstants.LOC, loc);
											htup.put(IConstants.UNITMO,uom);																				
											chkpos = posdet.isExisit(htup,"");
											if(chkpos==false)
											{
											posdet.insertIntoPosDet(ht);
											}
											/*new TblControlUtil().updateTblControlSeqNoWithoutorder(PLANT, "GIRECEIPT",
													"GI");*/

										} catch (Exception e) {
											session.setAttribute("errmsg", "Tran Id : " + tranID + " Exists already! ");
											String msg = session.getAttribute("errmsg").toString();
											e.printStackTrace(new PrintWriter(msg));
											throw e;
										}
									}

								} else {
									//Azees Udated 6.11.18
									int matchnew=0; 
									Boolean flag=true;									
									for (int i = 0; i < buylist.size(); i++) {

										ITEMMST itembean = buylist.elementAt(i);

										if (itembean.getITEM().equalsIgnoreCase(items.getITEM())
												&& itembean.getBATCH().equalsIgnoreCase(items.getBATCH()) && itembean.getSTKUOM().equalsIgnoreCase(items.getSTKUOM())) {
											if (serialized.equalsIgnoreCase("1")) {
												session.setAttribute("errmsg",
														"Duplicates are not allowed when serialized");
												match = true;
											} else {
												match = true;
												if(itembean.getBATCHID() == items.getBATCHID())
												{
													float scqty1=  itembean.getStkqty() + scqty;
													double invuomqty = Double.valueOf(scqty1) * Double.valueOf(UOMQTY);
														
													
													
													ht1.put(IConstants.ITEM, items.getITEM());
													ht1.put(IConstants.BATCH, items.getBATCH());
													if (prodbean1.getBATCHID() != -1) {
														ht1.put(IConstants.BATCH_ID, String.valueOf(items.getBATCHID()));
													}
													
													
													ht1.put(IConstants.QTY,  String.valueOf(invuomqty));
													
													flag = CheckInvMstForGoddsIssue(ht1, request, response);
													if (flag == false) {
														
																errmsg = "Not Enough Inventory";
																break;
															}
													else
													{
														if (scqty > 1) {
														    itembean.setStkqty((itembean.getStkqty() + scqty));
														    
														} else {
															itembean.setStkqty((itembean.getStkqty() + 1));	
															
														}
														
														buylist.setElementAt(itembean, i);
													}
												     
												}
												else
												{
													Boolean found = false;
													 for(int j=0;j<buylist.size();j++)
													 {
														 ITEMMST tester=buylist.elementAt(j);
												           if(tester.getBATCHID()==items.getBATCHID()){
												        	   
												        	   found=true;
												                break;
												            }
													 }
													 if(!found)
													 {
														if(matchnew==0)
														{
															matchnew=1;
														}
													 }
												}
												
												}
												
											
										}
									}
									if(!flag)
									{

											session.setAttribute("errmsg", "Not Enough Inventory For Product:"
													+ prodbean1.getITEM() + "  with batch:" + prodbean1.getBATCH());
											String msg = session.getAttribute("errmsg").toString();
											session.setAttribute("poslist", buylist);
											
											response.sendRedirect("jsp/" + posPageToDirect + "?TRANID=" + tranID
													+ "&CHKBATCH=" + batchChecked + "&LOC=" + loc);
											return;
										
									}
									if(matchnew==1)
									{
										buylist.addElement(items);
									}
									for (int i = 0; i < buylist.size(); i++) {

										ITEMMST itembean = buylist.elementAt(i);
										if (itembean.getITEM().equalsIgnoreCase(items.getITEM())
												&& itembean.getBATCH().equalsIgnoreCase(items.getBATCH())&& itembean.getSTKUOM().equalsIgnoreCase(items.getSTKUOM())) {
										match = true;
										Hashtable<String, String> ht = new Hashtable<String, String>();
										ht.put(IDBConstants.PLANT, PLANT);
										ht.put(IDBConstants.POS_TRANID, tranID);
										ht.put(IDBConstants.POS_ITEM, items.getITEM());
										ht.put(IDBConstants.POS_BATCH, items.getBATCH());
										String Qry="";
										if(i == 0)
										{
										Qry = " SET QTY ='0'";
										posdet.update(Qry, ht, "");
										}
										
										Qry = " SET QTY =QTY+'" + itembean.getStkqty() + "'";
										posdet.update(Qry, ht, "");
										
									}
									}
									if (!match) {
										buylist.addElement(items);
										// Insert into posdet with status as N

										Hashtable<String, String> ht = new Hashtable<String, String>();
										ht.put(IDBConstants.PLANT, PLANT);
										ht.put(IDBConstants.POS_TRANID, tranID);
										ht.put(IDBConstants.POS_ITEM, items.getITEM());
										ht.put(IDBConstants.POS_ITEMDESC, strUtils.InsertQuotes(items.getITEMDESC()));
										ht.put(IDBConstants.POS_QTY, String.valueOf(items.getStkqty()));
										ht.put(IDBConstants.POS_LOC, loc);
										ht.put(IDBConstants.POS_DISCOUNT, "0");
										ht.put(IDBConstants.POSDET_STATUS, "N");
										ht.put(IDBConstants.POS_UNITPRICE, "0");
										ht.put(IDBConstants.POS_TOTALPRICE, "0");
										ht.put(IDBConstants.POS_TRANDT, DateUtils.getDate());
										ht.put(IDBConstants.POS_TRANTM, DateUtils.Time());
										ht.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
										ht.put(IDBConstants.CREATED_BY, login_user);
										ht.put(IDBConstants.POS_BATCH, batch);
										// ht.put(IDBConstants.REFNO,refno);
										ht.put("POSTYPE", TRANTYPE);
										ht.put(IConstants.UNITMO,uom);
										ht.put(IConstants.OUT_DOLNNO, Integer.toString(buylist.size()));
										try {
											Hashtable<String, String> htTrandId = new Hashtable<String, String>();
											htTrandId.put("POSTRANID", tranID);
											boolean istranidExist1 = _posServlet.isRecvtranIDExisit(PLANT, htTrandId);
											if (istranidExist1 == true) {

												throw new Exception();
											}

											posdet.insertIntoPosDet(ht);
											// new
											// TblControlUtil().updateTblControlSeqNo(PLANT,"GIRECEIPT","GI",tranID);
											/*new TblControlUtil().updateTblControlSeqNoWithoutorder(PLANT, "GIRECEIPT",
													"GI");*/

										} catch (Exception e) {
											buylist.remove(items);
											session.setAttribute("errmsg", "Tran Id : " + tranID + " Exists already! ");
											String msg = session.getAttribute("errmsg").toString();
											e.printStackTrace(new PrintWriter(msg));
											throw e;
										}
									}
								}
								session.setAttribute("poslist", buylist);
								session.setAttribute("LOCATION", loc);
								if (!posType.equalsIgnoreCase("NO_INV_CHECK")) {
									boolean checkarry[] = new boolean[buylist.size()];
									int index = 0;
									Vector newbuylst = (Vector) session.getAttribute("poslist");
									// Check qty exists in inventory
									for (int i = 0; i < newbuylst.size(); i++) {
										Hashtable ht = new Hashtable();
										ITEMMST prodbean = (ITEMMST) newbuylst.elementAt(i);

										ht.put(IConstants.PLANT, PLANT);
										ht.put(IConstants.LOGIN_USER, (String) session.getAttribute("LOGIN_USER"));
										ht.put(IConstants.LOC, prodbean.getLoc());
										ht.put(IConstants.ITEM, prodbean.getITEM());
										ht.put(IConstants.BATCH, prodbean.getBATCH());
										if (Integer. valueOf(prodbean.getBATCHID()) != -1) {
											ht.put(IConstants.BATCH_ID, String.valueOf(prodbean.getBATCHID()));
										}
										String stkqty = String.valueOf(prodbean.getStkqty());
										//stkqty = StrUtils.TrunkateDecimalForImportData(stkqty);
										ht.put(IConstants.QTY, stkqty);
										nonstockflag = prodbean.getNONSTOCKFLAG();
										if (!nonstockflag.equalsIgnoreCase("Y")) {
											checkarry[index] = CheckInvMst(ht, request, response);
											try {
												checkarry[index] = CheckInvMst(ht, request, response);

											} catch (Exception e) {

												InvFlag = true;
												session.setAttribute("errmsg", "Not Enough Inventory For Product:"
														+ prodbean.getITEM() + "  with batch:" + prodbean.getBATCH());
												String msg = session.getAttribute("errmsg").toString();
												session.setAttribute("poslist", buylist);
												e.printStackTrace(new PrintWriter(msg));
												throw e;
											}

										}

										if (checkarry[index] == true) {
											response.sendRedirect("jsp/" + posPageToDirect + "?TRANID=" + tranID
													+ "&CHKBATCH=" + batchChecked + "&LOC=" + loc + "&serialized="
													+ serialized + "&defualtQty=" + defualtQty + "&bulkcheckout="
													+ bulkcheckout + "&EMP_ID=" + empID + "&REFERENCENO=" + refNo
													+ "&TRANSACTIONDATE=" + tranDate + "&REASONCODE=" + holdReasonCode
													+ "&REMARKS=" + holdRemarks + "&EMP_NAME=" + empName + "&LOCDESC="
													+ locdesc + "&BATCH=NOBATCH"+"&cmd="+ cmd);

										}

										index++;
									}
								}

							} else {
								session.setAttribute("errmsg", "Product Id not found");
							}

						}

						response.sendRedirect("jsp/" + posPageToDirect + "?TRANID=" + tranID + "&CHKBATCH="
								+ batchChecked + "&LOC=" + loc + "&serialized=" + serialized + "&defualtQty="
								+ defualtQty + "&bulkcheckout=" + bulkcheckout + "&defualtQty=" + defualtQty
								+ "&bulkcheckout=" + bulkcheckout + "&EMP_ID=" + empID + "&REFERENCENO=" + refNo
								+ "&TRANSACTIONDATE=" + tranDate + "&REASONCODE=" + holdReasonCode + "&REMARKS="
								+ holdRemarks + "&EMP_NAME=" + empName + "&LOCDESC=" + locdesc + "&BATCH=NOBATCH"+"&cmd="+ cmd);

					} else {
						session.setAttribute("errmsg",
								"Location : " + loc + " is not a User Assigned Location/Valid Location ");
						response.sendRedirect("jsp/" + posPageToDirect + ".jsp&LOC=" + loc + "&serialized=" + serialized
								+ "&defualtQty=" + defualtQty + "&bulkcheckout=" + bulkcheckout + "&EMP_ID=" + empID
								+ "&REFERENCENO=" + refNo + "&TRANSACTIONDATE=" + tranDate + "&REASONCODE="
								+ holdReasonCode + "&REMARKS=" + holdRemarks + "&EMP_NAME=" + empName + "&LOCDESC="
								+ locdesc + "&BATCH=NOBATCH"+"&cmd="+ cmd);

					}
				} else {
					session.setAttribute("errmsg", "Please Enter Location");
					response.sendRedirect("jsp/" + posPageToDirect + "&LOC=" + loc + "&serialized=" + serialized
							+ "&defualtQty=" + defualtQty + "&bulkcheckout=" + bulkcheckout + "&EMP_ID=" + empID
							+ "&REFERENCENO=" + refNo + "&TRANSACTIONDATE=" + tranDate + "&REASONCODE=" + holdReasonCode
							+ "&REMARKS=" + holdRemarks + "&EMP_NAME=" + empName + "&LOCDESC=" + locdesc
							+ "&BATCH=NOBATCH"+"&cmd="+ cmd);
				}
				
				} else if (cmdAction.equalsIgnoreCase("ADD") && TRANTYPE.equalsIgnoreCase("GOODSISSUEWITHBATCHPRICE")) {

				Hashtable htv = new Hashtable();
				String cmd = "ADD";
				String expireDate = "", refno = "";
				String rflag = "", ordertypeString = "", result = "", dono = "", custName = "", display = "", ordertype = "",
						currencyid = "", custCode = "", jobNum = "", personIncharge = "", user_id = "", contactNum = "",
						address = "", address2 = "", address3 = "",  collectionTime = "", deliverydate = "",
						timeslots = "", outbound_Gst = "", orderstatus = "",  remark1 = "", remark2 = "",
						remark3 = "", deldate = "", processno = "", shipCustname = "", shipCname = "", shipaddr1 = "",
						shipaddr2 = "", shipCity = "", shipCountry = "", shipZip = "", shiptelno = "", shippingcustomer = "",
						shippingid = "", orderdiscount = "", shippingcost = "", incoterms = "",EDIT="",CashCust="",INVOICENO="",UNITPRICE="",UNITPRICERD="";
				
				dono = StrUtils.fString(request.getParameter("TRANID")).trim();
				custName = StrUtils.replaceCharacters2Recv(StrUtils.fString(request.getParameter("CUST_NAME"))).trim();
				custCode = StrUtils.fString(request.getParameter("CUST_CODE1")).trim();
				personIncharge = StrUtils.fString(request.getParameter("PERSON_INCHARGE")).trim();
				contactNum = StrUtils.fString(request.getParameter("CONTACT_NUM")).trim();
				jobNum = StrUtils.fString(request.getParameter("REASONCODE")).trim();
				address = StrUtils.fString(request.getParameter("ADD1")).trim();
				address2 = StrUtils.fString(request.getParameter("ADD2")).trim();
				address3 = StrUtils.fString(request.getParameter("ADD3")).trim();
				remark1 = StrUtils.fString(request.getParameter("REMARK1")).trim();
				remark2 = StrUtils.fString(request.getParameter("REMARK2")).trim();
				remark3 = StrUtils.fString(request.getParameter("REMARK3")).trim();
				display = StrUtils.fString(request.getParameter("DISPLAY")).trim();
				ordertype = StrUtils.fString(request.getParameter("ORDERTYPE")).trim();
				deliverydate = StrUtils.fString(request.getParameter("DELIVERYDATE")).trim();				
				outbound_Gst = StrUtils.fString(request.getParameter("GST")).trim();
				shippingcustomer = StrUtils.fString(request.getParameter("SHIPPINGCUSTOMER")).trim();
				shippingid = StrUtils.fString(request.getParameter("SHIPPINGID")).trim();
				orderdiscount = StrUtils.fString(request.getParameter("ORDERDISCOUNT")).trim();
				shippingcost = StrUtils.fString(request.getParameter("SHIPPINGCOST")).trim();
				incoterms = StrUtils.fString(request.getParameter("INCOTERMS")).trim();
				collectionTime = StrUtils.fString(request.getParameter("COLLECTION_TIME")).trim();
				EDIT= StrUtils.fString(request.getParameter("EDIT")).trim();
				CashCust= StrUtils.fString(request.getParameter("CashCust")).trim();
				INVOICENO= StrUtils.fString(request.getParameter("INVOICENO")).trim();
				String INVOICECHECK = StrUtils.fString(request.getParameter("INVOICECHECK")).trim();
				UNITPRICE= StrUtils.fString(request.getParameter("UNITPRICE")).trim();
				UNITPRICERD= StrUtils.fString(request.getParameter("UNITPRICERD")).trim();
				if(UNITPRICE.equalsIgnoreCase(""))
					UNITPRICE="0";
				if(UNITPRICERD.equalsIgnoreCase("0"))
					UNITPRICERD=UNITPRICE;
				else if(UNITPRICERD.equalsIgnoreCase(""))
					UNITPRICERD=UNITPRICE;
				if(CashCust.equals("on"))
					CashCust="1";
				refno = request.getParameter("REFERENCENO");
				String empID = StrUtils.fString(request.getParameter("EMP_ID")).trim();
				String refNo = StrUtils.fString(request.getParameter("REFERENCENO")).trim();
				String serialized = StrUtils.fString(request.getParameter("serialized")).trim();
				String defualtQty = StrUtils.fString(request.getParameter("defaultQty")).trim();
				String bulkcheckout = StrUtils.fString(request.getParameter("bulkcheckout")).trim();
				String tranDate = StrUtils.fString(request.getParameter("TRANSACTIONDATE")).trim();
				String holdReasonCode = StrUtils.fString(request.getParameter("REASONCODE")).trim();
				String holdRemarks = StrUtils.fString(request.getParameter("REMARKS")).trim();
				String paymenttype= StrUtils.fString(request.getParameter("PAYMENTTYPE")).trim();
				String DATEFORMAT = StrUtils.fString((request.getParameter("DATEFORMAT") != null) ? "1": "0").trim();
				expireDate = request.getParameter("EXPIREDATE");
				refno = request.getParameter("REFERENCENO");
				String empName = request.getParameter("EMP_NAME");
				loc = StrUtils.fString(request.getParameter("LOC")).trim();
				String locdesc = StrUtils.fString(request.getParameter("LOCDESC")).trim();
                String uom = StrUtils.fString(request.getParameter("UOM")).trim();
				Hashtable curHash = new Hashtable();
				curHash.put(IConstants.PLANT, PLANT);
				curHash.put(IConstants.DISPLAY, display);
				if (display != null && display != "") {
					currencyid = curUtil.getCurrencyID(curHash, "CURRENCYID");
				}
				boolean isvalidOrderType = true;
				if (ordertype.length() > 0) {
				if (!ordertype.equals("") && !ordertype.equals("MOBILE ORDER") && !ordertype.equals("MOBILE ENQUIRY")
						&& !ordertype.equals("MOBILE REGISTRATION")) {
					OrderTypeBeanDAO orderTypeBeanDAO = new OrderTypeBeanDAO();
					orderTypeBeanDAO.setmLogger(mLogger);
					isvalidOrderType = orderTypeBeanDAO.isOrderTypeExists(ordertype, PLANT);
				}
				}
				CustomerBeanDAO customerBeanDAO = new CustomerBeanDAO();
				customerBeanDAO.setmLogger(mLogger);
				boolean isvalidCustomer = Boolean.valueOf(true);
				if (custName.length() > 0) {
				 isvalidCustomer = customerBeanDAO.isExistsCustomerName(custName, PLANT);
				}
				Boolean isvalidShippingCustomer = Boolean.valueOf(true);
				if (shippingcustomer.length() > 0) {
					isvalidShippingCustomer = _MasterDAO.isExistsShippingDetails(shippingcustomer, PLANT);
				}
				
				
				if (loc.length() > 0) {
					
					UserLocUtil uslocUtil = new UserLocUtil();
					uslocUtil.setmLogger(mLogger);
					boolean isvalidlocforUser = uslocUtil.isValidLocInLocmstForUser(PLANT, login_user, loc);
					if (isvalidlocforUser) {
						if (item.length() > 0) {
							boolean itemFound = true;
							ItemMstDAO itM = new ItemMstDAO();
							itM.setmLogger(mLogger);
							String scannedItemNo = itM.getItemIdFromAlternate(PLANT, item);
							if (scannedItemNo == null) {
								itemFound = false;
							} else {
								item = scannedItemNo;
							}
							if (itemFound) {
								if(batch.equalsIgnoreCase(""))
									batch="NOBATCH";
								ITEMMST items = getItemDetails(request, item, scqty, batch);
								items.setBATCHID(batchId);
								ITEMMST prodbean1 = items;
								nonstockflag = prodbean1.getNONSTOCKFLAG();
								/* if(!nonstockflag.equalsIgnoreCase("Y")){ */
								items.setUSERFLD4(UNITPRICERD);
								items.setUSERFLD6(UNITPRICE);
								items.setEmpNo(empID);
								items.setRefNo(refNo);
								items.setTranDate(tranDate);
								items.setReasonCode(reasoncode);
								items.setRemarks(remarks);
								items.setUSERFLD5(INVOICENO);
								items.setLoc(loc);
								items.setSTKUOM(uom);
								Hashtable ht1 = new Hashtable();

								ht1.put(IConstants.PLANT, PLANT);
								ht1.put(IConstants.LOGIN_USER, (String) session.getAttribute("LOGIN_USER"));
								ht1.put(IConstants.LOC, loc);
								ht1.put(IConstants.ITEM, prodbean1.getITEM());
								ht1.put(IConstants.BATCH, prodbean1.getBATCH());
								if (prodbean1.getBATCHID() != -1) {
									ht1.put(IConstants.BATCH_ID, String.valueOf(prodbean1.getBATCHID()));
								}
								String stkqty1 = String.valueOf(prodbean1.getStkqty());
								//stkqty1 = StrUtils.TrunkateDecimalForImportData(stkqty1);
								String UOMQTY="1";
								Hashtable<String, String> htTrand1 = new Hashtable<String, String>();
								MovHisDAO movHisDao1 = new MovHisDAO();
								ArrayList getuomqty = movHisDao1.selectForReport("select ISNULL(QPUOM,1) as UOMQTY from "+PLANT+"_UOM where UOM='"+uom+"'",htTrand1);
								if(getuomqty.size()>0)
								{
								Map mapval = (Map) getuomqty.get(0);
								UOMQTY=(String)mapval.get("UOMQTY");
								}
								double invumqty = Double.valueOf(stkqty1) * Double.valueOf(UOMQTY);
								ht1.put(IConstants.QTY, String.valueOf(invumqty));
								nonstockflag = prodbean1.getNONSTOCKFLAG();
								if (!nonstockflag.equalsIgnoreCase("Y")) {
									// boolean invAvailable=CheckInvMst(ht1,request,response);

									boolean invAvailable = false;
									// invAvailable=CheckInvMst(ht1,request,response);
									try {
										
										invAvailable = CheckInvMstForGoddsIssue(ht1, request, response);

									} catch (Exception e) {
										InvFlag = true;
										session.setAttribute("errmsg", "Not Enough Inventory For Product:"
												+ prodbean1.getITEM() + "  with batch:" + prodbean1.getBATCH());
										String msg = session.getAttribute("errmsg").toString();
										session.setAttribute("poslist", buylist);
										e.printStackTrace(new PrintWriter(msg));
										throw e;
									}

									if (!invAvailable) { // response.sendRedirect("jsp/"+posPageToDirect+"?TRANID="+tranID+"&CHKBATCH="+batchChecked+"&LOC="+loc);
										session.setAttribute("errmsg", "Not Enough Inventory For Product:"
												+ prodbean1.getITEM() + "  with batch:" + prodbean1.getBATCH());
										String msg = session.getAttribute("errmsg").toString();
										session.setAttribute("poslist", buylist);
										// RequestDispatcher view =
										// request.getRequestDispatcher("jsp/"+posPageToDirect+"?TRANID="+tranID+"&CHKBATCH="
										// +batchChecked+"&LOC="+loc);
										// view.forward(request, response);
										response.sendRedirect("jsp/" + posPageToDirect + "?TRANID=" + tranID
												+ "&CHKBATCH=" + batchChecked + "&LOC=" + loc+"&INVOICENO="+INVOICENO+"&INVOICECHECK="+INVOICECHECK);
										return;
									}

								}

								if (buylist == null || buylist.size() == 0) {
									Hashtable<String, String> ht = new Hashtable<String, String>();
									match = false;
									buylist = new Vector<ITEMMST>();
									buylist.addElement(items);
									// To check transaction already exists in POSHeader
									Hashtable<String, String> htTrandId = new Hashtable<String, String>();
									htTrandId.put("POSTRANID", tranID);
									boolean istranidExist = _posServlet.isExisit(PLANT, htTrandId);
									/*boolean istranidExist1 = _posServlet.isRecvtranIDExisit(PLANT, htTrandId);
									if (istranidExist1 == true) {
										try {
											throw new Exception();
										} catch (Exception e) {
											session.setAttribute("errmsg", "Tran Id : " + tranID + " Exists already! ");
											String msg = session.getAttribute("errmsg").toString();
											e.printStackTrace(new PrintWriter(msg));
											throw e;
										}
									}*/
									if (isvalidCustomer) {
										if (isvalidShippingCustomer) {
											if (isvalidOrderType) {
									
										
										ht.put(IDBConstants.PLANT, PLANT);
										ht.put(IDBConstants.POS_TRANID, tranID);
										ht.put(IDBConstants.DOHDR_CUST_CODE, custCode);
										ht.put(IDBConstants.DOHDR_CUST_NAME, strUtils.InsertQuotes(custName));
										ht.put(IDBConstants.DOHDR_JOB_NUM, refNo);
										ht.put(IDBConstants.DOHDR_PERSON_INCHARGE, strUtils.InsertQuotes(personIncharge));
										ht.put(IDBConstants.DOHDR_CONTACT_NUM, contactNum);
										ht.put(IDBConstants.DOHDR_ADDRESS, address);
										ht.put(IDBConstants.DOHDR_ADDRESS2, address2);
										ht.put(IDBConstants.DOHDR_ADDRESS3, address3);
										ht.put(IDBConstants.DOHDR_COL_DATE, tranDate);
										ht.put(IDBConstants.DOHDR_COL_TIME, collectionTime);
										ht.put(IDBConstants.DOHDR_REMARK1, strUtils.InsertQuotes(remark1));								
										ht.put(IDBConstants.DOHDR_REMARK3, strUtils.InsertQuotes(remark3));								
										ht.put(IDBConstants.ORDERTYPE, ordertype);
										ht.put(IDBConstants.CURRENCYID, currencyid);
										ht.put(IDBConstants.TIMESLOTS, timeslots);								
										ht.put(IDBConstants.DELIVERYDATE, deliverydate);
										ht.put(IDBConstants.DOHDR_GST, outbound_Gst);
										ht.put(IDBConstants.ORDSTATUSID, orderstatus);								
										ht.put(IDBConstants.SHIPPINGCUSTOMER, shippingcustomer);
										ht.put(IDBConstants.SHIPPINGID, shippingid);
										ht.put(IDBConstants.ORDERDISCOUNT, orderdiscount);
										ht.put(IDBConstants.SHIPPINGCOST, shippingcost);
										ht.put(IDBConstants.INCOTERMS, incoterms);
										ht.put(IDBConstants.CASHCUST, CashCust);
										ht.put(IDBConstants.PAYMENTTYPE, paymenttype);
										ht.put(IDBConstants.POSHDR_DELIVERYDATEFORMAT, DATEFORMAT);
										if (istranidExist == false) {
										String genPosTranid = poshdr.insPosManualTranId(login_user, loc, ht);
									}
									else{
										boolean flagup;
										POSUtil posUtil = new POSUtil();
										posUtil.setmLogger(mLogger);
										flagup = posUtil.updatePosHdr(ht);
									}
											}
											else {
												try{
													throw new Exception();
													} catch (Exception e) {
												getlist.remove(items);
												session.setAttribute("errmsg", "Tran Id : " + tranID + " Enter Valid ORDER TYPE! ");
												String msg = session.getAttribute("errmsg").toString();
												e.printStackTrace(new PrintWriter(msg));
												throw e;
													}
												}
										}
										else {
											try{
												throw new Exception();
												} catch (Exception e) {
											getlist.remove(items);
											session.setAttribute("errmsg", "Tran Id : " + tranID + " Enter Valid Shipping Customer! ");
											String msg = session.getAttribute("errmsg").toString();
											e.printStackTrace(new PrintWriter(msg));
											throw e;
												}
											}
									}
									else {
										try{
											throw new Exception();
											} catch (Exception e) {
										getlist.remove(items);
										session.setAttribute("errmsg", "Tran Id : " + tranID + " Enter Valid Customer! ");
										String msg = session.getAttribute("errmsg").toString();
										e.printStackTrace(new PrintWriter(msg));
										throw e;
											}
										}
									
									// To check transaction already exists in POSHeader end
									ht = new Hashtable<String, String>();
									ht.put(IDBConstants.PLANT, PLANT);
									ht.put(IDBConstants.POS_TRANID, tranID);
									String QryUpdate = " SET STATUS ='O',UPAT ='" + DateUtils.getDate() + "',EMPNO='"
											+ empID + "',REFNO='" + refNo + "',TRANDT='" + tranDate + "',RSNCODE='"
											+ holdReasonCode + "',REMARKS='" + holdRemarks + "',LOC='" + loc + "' ";
									boolean posHsrUpt = posHdr.update(QryUpdate, ht, "");
									String price = StrUtils.removeFormat(UNITPRICERD);
									price = new POSUtil().getConvertedUnitCostToLocalCurrency(PLANT, tranID, price);
									/*if(nonstockflag.equalsIgnoreCase("Y"))
									{
									String s = price;
									if (s.indexOf("-") == -1) {
										price = "-" + price;
									}
									}*/
									if (posHsrUpt) {										
										ht.put(IDBConstants.POS_ITEM, items.getITEM());
										ht.put(IDBConstants.POS_ITEMDESC, strUtils.InsertQuotes(items.getITEMDESC()));
										ht.put(IDBConstants.POS_QTY, String.valueOf(items.getStkqty()));
										ht.put(IDBConstants.POS_LOC, loc);
										float totval = items.getStkqty()* (Float.parseFloat(price));
										ht.put(IDBConstants.POSDET_STATUS, "N");
										ht.put(IDBConstants.POS_UNITPRICE, price);
										ht.put(IDBConstants.POS_TOTALPRICE, String.valueOf(totval));
										ht.put(IDBConstants.POS_TRANDT, DateUtils.getDate());
										ht.put(IDBConstants.POS_TRANTM, DateUtils.Time());
										ht.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
										ht.put(IDBConstants.CREATED_BY, login_user);
										ht.put(IDBConstants.POS_BATCH, batch);
										ht.put(IDBConstants.UNITMO, uom);
										// ht.put(IDBConstants.REFNO,refno);
										ht.put("POSTYPE", TRANTYPE);
										String CURRENCYUSEQT = new POSUtil().getCurrencyUseQT(PLANT, tranID);
										ht.put(IDBConstants.CURRENCYUSEQT, CURRENCYUSEQT);
										ht.put(IConstants.OUT_DOLNNO, Integer.toString(1));
										try {
											/*if(EDIT=="")
											{
												
											posdet.insertIntoPosDet(ht);
											}
											else
											{*/	
												Hashtable<String, String> htup = new Hashtable<String, String>();
												htup.put(IDBConstants.PLANT, PLANT);
												htup.put(IDBConstants.POS_TRANID, tranID);
												htup.put(IDBConstants.POS_ITEM, items.getITEM());
												htup.put(IDBConstants.POS_BATCH, items.getBATCH());
												htup.put(IDBConstants.POS_UNITPRICE, price);
												htup.put(IDBConstants.LOC, String.valueOf(items.getLoc()));
												//htup.put(IDBConstants.POS_QTY, String.valueOf(items.getStkqty()));										
												boolean chkpos = posdet.isExisit(htup,"");
												if(chkpos)
												{
												htup.remove(IDBConstants.POS_QTY);
												String Qry="";												
												Qry = " SET QTY ='0'";
												posdet.update(Qry, htup, "");
												
												
												Qry = " SET QTY =QTY+'" + items.getStkqty() + "', TOTALPRICE=(QTY+'" + items.getStkqty() + "')*UNITPRICE ";
												posdet.update(Qry, htup, "");
												}
												else
												{
													posdet.insertIntoPosDet(ht);
												}
											//}
											/*new TblControlUtil().updateTblControlSeqNoWithoutorder(PLANT, "GIRECEIPT",
													"GI");*/

										} catch (Exception e) {
											session.setAttribute("errmsg", "Tran Id : " + tranID + " Exists already! ");
											String msg = session.getAttribute("errmsg").toString();
											e.printStackTrace(new PrintWriter(msg));
											throw e;
										}
										
									}
											

								} else {
									//Azees Udated 6.11.18
									
									//update POSHDR
									if (isvalidCustomer) {
										if (isvalidShippingCustomer) {
											if (isvalidOrderType) {
												
												Hashtable<String, String> ht = new Hashtable<String, String>();
												ht.put(IDBConstants.PLANT, PLANT);
												ht.put(IDBConstants.POS_TRANID, tranID);
												ht.put(IDBConstants.DOHDR_CUST_CODE, custCode);
												ht.put(IDBConstants.DOHDR_CUST_NAME, strUtils.InsertQuotes(custName));
												ht.put(IDBConstants.DOHDR_JOB_NUM, refNo);
												ht.put(IDBConstants.DOHDR_PERSON_INCHARGE, strUtils.InsertQuotes(personIncharge));
												ht.put(IDBConstants.DOHDR_CONTACT_NUM, contactNum);
												ht.put(IDBConstants.DOHDR_ADDRESS, address);
												ht.put(IDBConstants.DOHDR_ADDRESS2, address2);
												ht.put(IDBConstants.DOHDR_ADDRESS3, address3);
												ht.put(IDBConstants.DOHDR_COL_DATE, tranDate);
												ht.put(IDBConstants.DOHDR_COL_TIME, collectionTime);
												ht.put(IDBConstants.DOHDR_REMARK1, strUtils.InsertQuotes(remark1));								
												ht.put(IDBConstants.DOHDR_REMARK3, strUtils.InsertQuotes(remark3));								
												ht.put(IDBConstants.ORDERTYPE, ordertype);
												ht.put(IDBConstants.CURRENCYID, currencyid);
												ht.put(IDBConstants.TIMESLOTS, timeslots);								
												ht.put(IDBConstants.DELIVERYDATE, deliverydate);
												ht.put(IDBConstants.DOHDR_GST, outbound_Gst);
												ht.put(IDBConstants.ORDSTATUSID, orderstatus);								
												ht.put(IDBConstants.SHIPPINGCUSTOMER, shippingcustomer);
												ht.put(IDBConstants.SHIPPINGID, shippingid);
												ht.put(IDBConstants.ORDERDISCOUNT, orderdiscount);
												ht.put(IDBConstants.SHIPPINGCOST, shippingcost);
												ht.put(IDBConstants.INCOTERMS, incoterms);
												ht.put(IDBConstants.CASHCUST, CashCust);
												ht.put(IDBConstants.PAYMENTTYPE, paymenttype);
												ht.put(IDBConstants.POSHDR_DELIVERYDATEFORMAT, DATEFORMAT);
												boolean flagup;
												POSUtil posUtil = new POSUtil();
												posUtil.setmLogger(mLogger);
												flagup = posUtil.updatePosHdr(ht);
												
												ht = new Hashtable<String, String>();
												ht.put(IDBConstants.PLANT, PLANT);
												ht.put(IDBConstants.POS_TRANID, tranID);
												String QryUpdate = " SET STATUS ='O',UPAT ='" + DateUtils.getDate() + "',EMPNO='"
														+ empID + "',REFNO='" + refNo + "',TRANDT='" + tranDate + "',RSNCODE='"
														+ holdReasonCode + "',REMARKS='" + holdRemarks + "',LOC='" + loc + "' ";
												boolean posHsrUpt = posHdr.update(QryUpdate, ht, "");
											}
											else {
												try{
													throw new Exception();
													} catch (Exception e) {
												getlist.remove(items);
												session.setAttribute("errmsg", "Tran Id : " + tranID + " Enter Valid ORDER TYPE! ");
												String msg = session.getAttribute("errmsg").toString();
												e.printStackTrace(new PrintWriter(msg));
												throw e;
													}
												}
										}
										else {
											try{
												throw new Exception();
												} catch (Exception e) {
											getlist.remove(items);
											session.setAttribute("errmsg", "Tran Id : " + tranID + " Enter Valid Shipping Customer! ");
											String msg = session.getAttribute("errmsg").toString();
											e.printStackTrace(new PrintWriter(msg));
											throw e;
												}
											}
									}
									else {
										try{
											throw new Exception();
											} catch (Exception e) {
										getlist.remove(items);
										session.setAttribute("errmsg", "Tran Id : " + tranID + " Enter Valid Customer! ");
										String msg = session.getAttribute("errmsg").toString();
										e.printStackTrace(new PrintWriter(msg));
										throw e;
											}
										}
									
									int matchnew=0; 
									Boolean flag=true;									
									for (int i = 0; i < buylist.size(); i++) {

										ITEMMST itembean = buylist.elementAt(i);

										if (itembean.getITEM().equalsIgnoreCase(items.getITEM()) && itembean.getBATCH().equalsIgnoreCase(items.getBATCH())
												 && itembean.getUSERFLD6().equalsIgnoreCase(items.getUSERFLD6()) && itembean.getLoc().equalsIgnoreCase(items.getLoc())) {
											if (serialized.equalsIgnoreCase("1")) {
												session.setAttribute("errmsg",
														"Duplicates are not allowed when serialized");
												match = true;
											} else {
												match = true;
												if(itembean.getBATCHID() == items.getBATCHID() && itembean.getUSERFLD6().equalsIgnoreCase(items.getUSERFLD6())&& itembean.getLoc().equalsIgnoreCase(items.getLoc()))
												{
													float scqty1=  itembean.getStkqty() + scqty;
													
														
													
													
													ht1.put(IConstants.ITEM, items.getITEM());
													ht1.put(IConstants.BATCH, items.getBATCH());
													if (prodbean1.getBATCHID() != -1) {
														ht1.put(IConstants.BATCH_ID, String.valueOf(items.getBATCHID()));
													}
													
													
													ht1.put(IConstants.QTY,  String.valueOf(scqty1));
													
													flag = CheckInvMstForGoddsIssue(ht1, request, response);
													if (flag == false) {
														
																errmsg = "Not Enough Inventory";
																break;
															}
													else
													{
														if (scqty > 1) {
														    itembean.setStkqty((itembean.getStkqty() + scqty));
														    
														} else {
															itembean.setStkqty((itembean.getStkqty() + 1));	
															
														}
														
														buylist.setElementAt(itembean, i);
													}
												     
												}
												else
												{
													Boolean found = false;
													 for(int j=0;j<buylist.size();j++)
													 {
														 ITEMMST tester=buylist.elementAt(j);
												           if(tester.getBATCHID()==items.getBATCHID()&&tester.getUSERFLD6().equalsIgnoreCase(items.getUSERFLD6())&& tester.getLoc().equalsIgnoreCase(items.getLoc())){
												        	   
												        	   found=true;
												                break;
												            }
													 }
													 if(!found)
													 {
														if(matchnew==0)
														{
															matchnew=1;
														}
													 }
												}
												
												}
												
											
										}
									}
									if(!flag)
									{

											session.setAttribute("errmsg", "Not Enough Inventory For Product:"
													+ prodbean1.getITEM() + "  with batch:" + prodbean1.getBATCH());
											String msg = session.getAttribute("errmsg").toString();
											session.setAttribute("poslist", buylist);
											
											response.sendRedirect("jsp/" + posPageToDirect + "?TRANID=" + tranID
													+ "&CHKBATCH=" + batchChecked + "&LOC=" + loc);
											return;
										
									}
									if(matchnew==1)
									{
										buylist.addElement(items);
									}
									String price = StrUtils.removeFormat(items.getUSERFLD4());
									price = new POSUtil().getConvertedUnitCostToLocalCurrency(PLANT, tranID, price);
									/*if(nonstockflag.equalsIgnoreCase("Y"))
									{
									String s = price;
									if (s.indexOf("-") == -1) {
										price = "-" + price;
									}
									}*/
									for (int i = 0; i < buylist.size(); i++) {

										ITEMMST itembean = buylist.elementAt(i);
										if (itembean.getITEM().equalsIgnoreCase(items.getITEM())&& itembean.getBATCH().equalsIgnoreCase(items.getBATCH())
												&& itembean.getUSERFLD6().equalsIgnoreCase(items.getUSERFLD6())&& itembean.getLoc().equalsIgnoreCase(items.getLoc())) {
										match = true;
										Hashtable<String, String> ht = new Hashtable<String, String>();
										ht.put(IDBConstants.PLANT, PLANT);
										ht.put(IDBConstants.POS_TRANID, tranID);
										ht.put(IDBConstants.POS_ITEM, items.getITEM());
										ht.put(IDBConstants.POS_BATCH, items.getBATCH());
										ht.put(IDBConstants.POS_UNITPRICE, price);
										ht.put(IDBConstants.LOC, String.valueOf(items.getLoc()));
										String Qry="";
										if(i == 0)
										{
										Qry = " SET QTY ='0'";
										posdet.update(Qry, ht, "");
										}
										
										Qry = " SET QTY ='" + itembean.getStkqty() + "', TOTALPRICE=('" + itembean.getStkqty() + "')*UNITPRICE ";
										posdet.update(Qry, ht, "");
										
									}
									}
									if (!match) {
										buylist.addElement(items);
										// Insert into posdet with status as N

										Hashtable<String, String> ht = new Hashtable<String, String>();
										ht.put(IDBConstants.PLANT, PLANT);
										ht.put(IDBConstants.POS_TRANID, tranID);
										ht.put(IDBConstants.POS_ITEM, items.getITEM());
										ht.put(IDBConstants.POS_ITEMDESC, strUtils.InsertQuotes(items.getITEMDESC()));
										ht.put(IDBConstants.POS_QTY, String.valueOf(items.getStkqty()));
										ht.put(IDBConstants.POS_LOC, String.valueOf(items.getLoc()));
										ht.put(IDBConstants.POS_DISCOUNT, "0");
										ht.put(IDBConstants.POSDET_STATUS, "N");
										float totval = items.getStkqty()* (Float.parseFloat(price));
										ht.put(IDBConstants.POS_UNITPRICE, price);
										ht.put(IDBConstants.POS_TOTALPRICE, String.valueOf(totval));
										ht.put(IDBConstants.POS_TRANDT, DateUtils.getDate());
										ht.put(IDBConstants.POS_TRANTM, DateUtils.Time());
										ht.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
										ht.put(IDBConstants.CREATED_BY, login_user);
										ht.put(IDBConstants.POS_BATCH, batch);
										// ht.put(IDBConstants.REFNO,refno);
										ht.put("POSTYPE", TRANTYPE);
                                        ht.put(IDBConstants.UNITMO, uom);
                                        ht.put(IConstants.OUT_DOLNNO, Integer.toString(buylist.size()));
										String CURRENCYUSEQT = new POSUtil().getCurrencyUseQT(PLANT, tranID);
										ht.put(IDBConstants.CURRENCYUSEQT, CURRENCYUSEQT);
										try {
											Hashtable<String, String> htup = new Hashtable<String, String>();
											htup.put(IDBConstants.PLANT, PLANT);
											htup.put(IDBConstants.POS_TRANID, tranID);
											htup.put(IDBConstants.POS_ITEM, items.getITEM());
											htup.put(IDBConstants.POS_BATCH, items.getBATCH());
											htup.put(IDBConstants.POS_UNITPRICE, price);
											htup.put(IDBConstants.LOC, String.valueOf(items.getLoc()));
											//htup.put(IDBConstants.POS_QTY, String.valueOf(items.getStkqty()));										
											boolean chkpos = posdet.isExisit(htup,"");
											if(chkpos)
											{
											htup.remove(IDBConstants.POS_QTY);
											String Qry="";												
											Qry = " SET QTY ='0'";
											posdet.update(Qry, htup, "");
											
											
											Qry = " SET QTY =QTY+'" + items.getStkqty() + "'";
											posdet.update(Qry, htup, "");
											}
											else
											{

											posdet.insertIntoPosDet(ht);
											}
											

										} catch (Exception e) {
											getlist.remove(items);
											session.setAttribute("errmsg", "Tran Id : " + tranID + " Exists already! ");
											String msg = session.getAttribute("errmsg").toString();
											e.printStackTrace(new PrintWriter(msg));
											throw e;
										}
									}
								}
								session.setAttribute("poslist", buylist);
								session.setAttribute("LOCATION", loc);
								if (!posType.equalsIgnoreCase("NO_INV_CHECK")) {
									boolean checkarry[] = new boolean[buylist.size()];
									int index = 0;
									Vector newbuylst = (Vector) session.getAttribute("poslist");
									// Check qty exists in inventory
									for (int i = 0; i < newbuylst.size(); i++) {
										Hashtable ht = new Hashtable();
										ITEMMST prodbean = (ITEMMST) newbuylst.elementAt(i);

										ht.put(IConstants.PLANT, PLANT);
										ht.put(IConstants.LOGIN_USER, (String) session.getAttribute("LOGIN_USER"));
										ht.put(IConstants.LOC, prodbean.getLoc());
										//ht.put(IConstants.LOC, loc);
										ht.put(IConstants.ITEM, prodbean.getITEM());
										//ht.put(IConstants.ITEM, item);
										ht.put(IConstants.BATCH, prodbean.getBATCH());
										//ht.put(IConstants.BATCH, batch);
										if (Integer. valueOf(prodbean.getBATCHID()) != -1) {
											ht.put(IConstants.BATCH_ID, String.valueOf(prodbean.getBATCHID()));
										}
										String stkqty = String.valueOf(prodbean.getStkqty());
										//stkqty = StrUtils.TrunkateDecimalForImportData(stkqty);
										ht.put(IConstants.QTY, stkqty);
										nonstockflag = prodbean.getNONSTOCKFLAG();
										if (!nonstockflag.equalsIgnoreCase("Y")) {
											checkarry[index] = CheckInvMst(ht, request, response);
											try {
												checkarry[index] = CheckInvMst(ht, request, response);

											} catch (Exception e) {

												InvFlag = true;
												session.setAttribute("errmsg", "Not Enough Inventory For Product:"
														+ prodbean.getITEM() + "  with batch:" + prodbean.getBATCH());
												String msg = session.getAttribute("errmsg").toString();
												session.setAttribute("poslist", buylist);
												e.printStackTrace(new PrintWriter(msg));
												throw e;
											}

										}

										if (checkarry[index] == true) {
											response.sendRedirect("jsp/" + posPageToDirect + "?TRANID=" + tranID
													+ "&CHKBATCH=" + batchChecked + "&LOC=" + loc + "&serialized="
													+ serialized + "&defualtQty=" + defualtQty + "&bulkcheckout="
													+ bulkcheckout + "&EMP_ID=" + empID + "&REFERENCENO=" + refNo
													+ "&TRANSACTIONDATE=" + tranDate + "&REASONCODE=" + holdReasonCode
													+ "&REMARKS=" + StrUtils.replaceCharacters2Send(holdRemarks) + "&EMP_NAME=" + empName + "&LOCDESC="
													+ locdesc + "&BATCH=NOBATCH"+"&INVOICENO="+INVOICENO +"&INVOICECHECK="+INVOICECHECK);

										}

										index++;
									}
								}

							} else {
								session.setAttribute("errmsg", "Product Id not found");
							}

						}
						
						Hashtable htv1 = new Hashtable();
						htv1.put(IDBConstants.PLANT, PLANT);
						htv1.put(IDBConstants.POS_TRANID, tranID);
						ArrayList prdlist= new ArrayList();
						if(EDIT!="")
						prdlist = posHdr.selectPosHdr("POSTRANID as TRANID,PersonInCharge as PERSON_INCHARGE,contactNum as CONTACT_NUM,REMARK1,REMARKS as REMARK2,REMARK3,CustCode as CUST_CODE,CustName as CUST_NAME,RSNCODE as REASONCODE,JobNum as REFERENCENO,ISNULL(DELIVERYDATEFORMAT,'') as DELIVERYDATEFORMAT,Address as ADD1,Address2 as ADD2,Address3 as ADD3,'' as ADD4,EMPNO as EMP_NAME,ORDERTYPE,DELIVERYDATE,SHIPPINGID,SHIPPINGCUSTOMER,ORDERDISCOUNT,SHIPPINGCOST,INCOTERMS,OUTBOUND_GST as GST,'EDIT' as EDIT,ISNULL(CashCust,'') as CashCust,ISNULL((select CUSTOMER_TYPE_ID from "+PLANT+"_CUSTMST where CustCode=CUSTNO),'') as CUSTOMERTYPEDESC,ISNULL(PAYMENTTYPE,'') as PAYMENTTYPE,CURRENCYID as currencyid",htv1);
						else
						prdlist = posHdr.selectPosHdr("POSTRANID as TRANID,PersonInCharge as PERSON_INCHARGE,contactNum as CONTACT_NUM,REMARK1,REMARKS as REMARK2,REMARK3,CustCode as CUST_CODE,CustName as CUST_NAME,RSNCODE as REASONCODE,JobNum as REFERENCENO,ISNULL(DELIVERYDATEFORMAT,'') as DELIVERYDATEFORMAT,Address as ADD1,Address2 as ADD2,Address3 as ADD3,'' as ADD4,EMPNO as EMP_NAME,ORDERTYPE,DELIVERYDATE,SHIPPINGID,SHIPPINGCUSTOMER,ORDERDISCOUNT,SHIPPINGCOST,INCOTERMS,OUTBOUND_GST as GST,'' as EDIT,ISNULL(CashCust,'') as CashCust,ISNULL((select CUSTOMER_TYPE_ID from "+PLANT+"_CUSTMST where CustCode=CUSTNO),'') as CUSTOMERTYPEDESC,ISNULL(PAYMENTTYPE,'') as PAYMENTTYPE,CURRENCYID as currencyid",htv1);
						session.setAttribute("prdlist", prdlist);
						
						response.sendRedirect("jsp/" + posPageToDirect + "?TRANID=" + tranID + "&CHKBATCH="
								+ batchChecked + "&LOC=" + loc + "&serialized=" + serialized + "&defualtQty="
								+ defualtQty + "&bulkcheckout=" + bulkcheckout + "&defualtQty=" + defualtQty
								+ "&bulkcheckout=" + bulkcheckout + "&EMP_ID=" + empID + "&REFERENCENO=" + refNo + "&DATEFORMAT=" + DATEFORMAT 
								+ "&TRANSACTIONDATE=" + tranDate + "&REASONCODE=" + holdReasonCode + "&REMARKS="
								+ StrUtils.replaceCharacters2Send(holdRemarks) + "&EMP_NAME=" + empName + "&LOCDESC=" + locdesc + "&BATCH=NOBATCH"+"&INVOICENO="+INVOICENO+"&INVOICECHECK="+INVOICECHECK+"&PAYMENTTYPE="+StrUtils.replaceCharacters2Send(paymenttype)+ "&cmd=" + cmd);

					} else {
						session.setAttribute("errmsg",
								"Location : " + loc + " is not a User Assigned Location/Valid Location ");
						response.sendRedirect("jsp/" + posPageToDirect + "&LOC=" + loc + "&serialized=" + serialized
								+ "&defualtQty=" + defualtQty + "&bulkcheckout=" + bulkcheckout + "&EMP_ID=" + empID
								+ "&REFERENCENO=" + refNo + "&DATEFORMAT=" + DATEFORMAT  + "&TRANSACTIONDATE=" + tranDate + "&REASONCODE="
								+ holdReasonCode + "&REMARKS=" + StrUtils.replaceCharacters2Send(holdRemarks) + "&EMP_NAME=" + empName + "&LOCDESC="
								+ locdesc + "&BATCH=NOBATCH"+"&INVOICENO="+INVOICENO+"&INVOICECHECK="+INVOICECHECK);

					}
				} else {
					session.setAttribute("errmsg", "Please Enter Location");
					response.sendRedirect("jsp/" + posPageToDirect + "&LOC=" + loc + "&serialized=" + serialized
							+ "&defualtQty=" + defualtQty + "&bulkcheckout=" + bulkcheckout + "&EMP_ID=" + empID
							+ "&REFERENCENO=" + refNo + "&DATEFORMAT=" + DATEFORMAT  + "&TRANSACTIONDATE=" + tranDate + "&REASONCODE=" + holdReasonCode
							+ "&REMARKS=" + StrUtils.replaceCharacters2Send(holdRemarks) + "&EMP_NAME=" + empName + "&LOCDESC=" + locdesc
							+ "&BATCH=NOBATCH"+"&INVOICENO="+INVOICENO+"&INVOICECHECK="+INVOICECHECK);
				}
				
				
		} else if (cmdAction.equalsIgnoreCase("ADD") && TRANTYPE.equalsIgnoreCase("MANUALSTOCKTAKE")) {
			DateUtils dtUtils = new DateUtils();
			String pltCountry = new PlantMstDAO().getCOUNTRY_TIMEZONE(PLANT);
			if(pltCountry.equalsIgnoreCase(""))
				pltCountry="Asia/Singapore";
			posPageToDirect = "../inhouse/manualstocktake";
			StrUtils su =null;
			boolean flag = false;
			String msg = "";
			
			Hashtable<String, String> htInvMst = new Hashtable<String, String>();
			htInvMst.clear();
			htInvMst.put(IDBConstants.PLANT, PLANT);
			htInvMst.put(IDBConstants.ITEM, Mitem);
			htInvMst.put("BATCH", Mbatch);
			htInvMst.put(IDBConstants.LOC, Mloc);
			htInvMst.put("STATUS", "N");
			
			//check for existence of item
			boolean result=new ItemUtil().isExistsItemMst(Mitem,PLANT);
			
			String INVQTY="0",INVFLAG="0";
			InvMstDAO  _InvMstDAO  = new InvMstDAO();
            ArrayList listQry = _InvMstDAO.getOutBoundPickingBatchByWMSMultiUOMWithNegQty(PLANT,Mitem,Mloc,Mbatch,Muom);
            if (listQry.size() > 0) {	               
         	   for(int i =0; i<listQry.size(); i++) {
                   Map arrCustLine = (Map)listQry.get(i);
                   INVQTY = (String)arrCustLine.get("qty");
         	   }
            }
            
            
            String STKQTY="0";
            ArrayList StklistQry = new StockTakeDAO().selectInvMstDetails(" isnull(qty,0) as qty ",htInvMst,"");
            if (StklistQry.size() > 0) {	               
          	   for(int i =0; i<StklistQry.size(); i++) {
                    Map arrCustLine = (Map)StklistQry.get(i);
                    STKQTY = (String)arrCustLine.get("qty");
          	   }
             }
            
            double stk = Double.parseDouble(Mqty)+Double.parseDouble(STKQTY);
			double invqty = Double.parseDouble(INVQTY);
			double diff = (invqty - stk);
			String diffval = strUtils.addZeroes((diff), "3");
			
			if(stk==invqty)
				INVFLAG="1";
			//check for valid location
			Hashtable htcdn = new Hashtable();
			htcdn.put(IConstants.PLANT, PLANT);
			htcdn.put(IDBConstants.LOC, Mloc);
			boolean  result1=new LocMstDAO().isExisit(htcdn, "");	
			 if(result==true&&result1==true){
				 flag = new StockTakeDAO().isExisit(htInvMst, "");
				 if (flag) {
					StringBuffer sql = new StringBuffer(" SET ");
//					if(Mremark.equalsIgnoreCase(""))
//						sql.append("" + IDBConstants.QTY + " = '" + Mqty + "' ,UPAT='" + DateUtils.getDateTime() + "' ,UPBY='" + login_user +"'");
//					else
//						sql.append("" + IDBConstants.QTY + " = QTY + '" + Double.parseDouble(Mqty) + "' ,DIFFQTY= DIFFQTY+ '" + Double.parseDouble(diffval) + "',INVFLAG='" + INVFLAG + "',REMARKS='" + Mremark + "' ,UPAT='" + DateUtils.getDateTime() + "' ,UPBY='" + login_user +"'");
						sql.append("" + IDBConstants.QTY + " = QTY + '" + Double.parseDouble(Mqty) + "' ,DIFFQTY=  '" + Double.parseDouble(diffval) + "',INVFLAG='" + INVFLAG + "',REMARKS='" + Mremark + "' ,UPAT='" + DateUtils.getDateTime() + "' ,UPBY='" + login_user +"'");
//					msg = " Product ["+Mitem+"] Updated Successfully ";
					msg = " "+Mitem+" Product Updated Successfully ";
					
					flag = new StockTakeDAO().update(sql.toString(), htInvMst, "");
				} else {
					
					htInvMst.clear();
	      			htInvMst.put(IDBConstants.PLANT, PLANT);
	      			htInvMst.put(IDBConstants.ITEM, Mitem);
	      			htInvMst.put(IDBConstants.LOC, Mloc);
	      			htInvMst.put(IDBConstants.BATCH, Mbatch);
					flag = new InvMstDAO().isExisit(htInvMst, "");
					if(!flag) {
						
						htInvMst.put(IDBConstants.EXPIREDATE, "");
						htInvMst.put(IDBConstants.QTY,"0");
						htInvMst.put(IDBConstants.USERFLD3, "");
						htInvMst.put(IDBConstants.CREATED_AT, DateUtils.getDate().toString().replaceAll("/", "") + "000000");
						htInvMst.put(IDBConstants.CREATED_BY, login_user);
						htInvMst.put(IDBConstants.STATUS, "");
			
						flag = _InvMstDAO.insertInvMst(htInvMst);
						
					}
					
					htInvMst.clear();
					htInvMst.put(IDBConstants.PLANT, PLANT);
					htInvMst.put(IDBConstants.ITEM, Mitem);
					htInvMst.put(IDBConstants.QTY, Mqty);
					htInvMst.put(IDBConstants.LOC, Mloc);
					htInvMst.put("BATCH", Mbatch);
					htInvMst.put("DIFFQTY", diffval);
					htInvMst.put("INVFLAG", INVFLAG);
					htInvMst.put(IDBConstants.UOM, Muom);
					htInvMst.put(IDBConstants.STATUS, "N");
					htInvMst.put("CR_DATE",  DateUtils.getDate());
//					htInvMst.put("CR_DATE", dtUtils.getDateinyyyy_mm_dd(dtUtils.getDate()));
					htInvMst.put(IDBConstants.USERFLD1,su.InsertQuotes( new ItemMstDAO().getItemDesc(PLANT,Mitem)));
					htInvMst.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
					htInvMst.put(IDBConstants.CREATED_BY, login_user);
					htInvMst.put(IDBConstants.REMARKS, Mremark);
					flag = new StockTakeDAO().insertStkTake(htInvMst);
					msg = " "+Mitem+" Product Added Successfully ";
				 }
			 }
			 
//			 RequestDispatcher view = request.getRequestDispatcher(posPageToDirect);
//			 view.forward(request, response);
//			 response.sendRedirect(posPageToDirect);
			 response.sendRedirect(posPageToDirect+"?result="+msg);
		} else if (cmdAction.equalsIgnoreCase("STKTAKE_REMOVE")) {
			posPageToDirect = "../inhouse/manualstocktake";
			boolean flag = false;
			String msg = "";
			String[] chkstkid  = request.getParameterValues("CHKSTKID"); 
			Hashtable<String, String> htInvMst = new Hashtable<String, String>();
		    for (String ID : chkstkid) {
		    	int STKID = Integer.valueOf(ID);
		    	flag = new StockTakeDAO().deleteStkTake(PLANT,STKID );
		    }
		    msg = "Product Removed Successfully ";
			response.sendRedirect(posPageToDirect+"?result="+msg);
			
		}else if (cmdAction.equalsIgnoreCase("ADDPROCESS") && TRANTYPE.equalsIgnoreCase("MANUALSTOCKTAKE")) {
			DateUtils dtUtils = new DateUtils();
			String pltCountry = new PlantMstDAO().getCOUNTRY_TIMEZONE(PLANT);
			if(pltCountry.equalsIgnoreCase("")) pltCountry="Asia/Singapore";
			posPageToDirect = "../inhouse/manualstocktake";
			String msg = "Stock take qty could not be empty",STOCKID="",STKQTY="";
			Hashtable<String, String> htInvMst = new Hashtable<String, String>();
			StrUtils su =null;
			boolean flag = false;
			
			ArrayList invQryList= new InvUtil().getManualStktakeByStatus(PLANT,"N");
			if (invQryList.size() > 0) {
				for (int iCnt =0; iCnt<invQryList.size(); iCnt++){
                    String results="";
                    Map lineArr = (Map) invQryList.get(iCnt);
                    Mitem = strUtils.fString((String) lineArr.get("ITEM"));
                    Mloc = strUtils.fString((String) lineArr.get("LOC"));
                    Mbatch = strUtils.fString((String) lineArr.get("BATCH"));
                    STOCKID = strUtils.fString((String) lineArr.get("ID"));
                    STKQTY = strUtils.fString((String) lineArr.get("QTY"));
            		htInvMst.clear();
        			htInvMst.put(IDBConstants.PLANT, PLANT);
        			htInvMst.put(IDBConstants.ITEM, Mitem);
        			htInvMst.put(IDBConstants.LOC, Mloc);
        			htInvMst.put(IDBConstants.BATCH, Mbatch);
//        			htInvMst.put("STATUS", "N");
        			
        			//check for existence of item
        			boolean result=new ItemUtil().isExistsItemMst(Mitem, PLANT);
        			
        			//check for valid location
        			Hashtable htcdn = new Hashtable();
        			htcdn.put(IConstants.PLANT,PLANT);
        			htcdn.put(IDBConstants.LOC, Mloc);
        			boolean  result1=new LocMstDAO().isExisit(htcdn, "");
        			
        			if(result==true&&result1==true){
        				flag = new InvMstDAO().isExisit(htInvMst, "");
        				if (flag) {
        					StringBuffer sql1 = new StringBuffer(" SET ");
        				    sql1.append(IDBConstants.QTY + " = '"+ STKQTY + "', ");
        					sql1.append(IDBConstants.UPDATED_AT + " = '"+ DateUtils.getDateTime() + "', ");
        					sql1.append(IDBConstants.UPDATED_BY + " = '"+ login_user + "'");
        					flag = new InvMstDAO().update(sql1.toString(), htInvMst, "");
        					if(flag) {				
        						StringBuffer sql2 = new StringBuffer(" SET ");
        						sql2.append(" ORDQTY = '" + STKQTY + "', ");
        						sql2.append(" RECQTY = '" + STKQTY + "', ");
        						sql2.append(IDBConstants.UPDATED_AT + " = '" + DateUtils.getDateTime() + "', ");
        						sql2.append(IDBConstants.UPDATED_BY + " = '" + login_user + "'");
        						
        						Hashtable htRecvDet = new Hashtable();
        						htRecvDet.put(IDBConstants.PLANT, PLANT);
        						htRecvDet.put("PONO","");
        						htRecvDet.put(IDBConstants.ITEM, Mitem);
        						htRecvDet.put(IDBConstants.LOC, Mloc);
        						htRecvDet.put("BATCH", Mbatch);
        						htRecvDet.put("UNITCOST", "");				
        						htRecvDet.put("TRAN_TYPE", "INVENTORYUPLOAD");
        						boolean isExists = new RecvDetDAO().isExisit(htRecvDet, PLANT);
        						if(isExists) {
        							flag = new RecvDetDAO().update(sql2.toString(), htRecvDet, "",PLANT);
        						}
        					}
        					
        					StringBuffer sql = new StringBuffer(" SET ");
        						sql.append("STATUS='C'");
        						htInvMst.clear();
        						htInvMst.put(IDBConstants.PLANT, PLANT);
        						htInvMst.put("ID", STOCKID);
        					
        					flag = new StockTakeDAO().update(sql.toString(), htInvMst, "");
        				} 
//        				else {
//        				        htInvMst.put(IDBConstants.QTY, STKQTY);
//
//	        					htInvMst.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
//	        					htInvMst.put(IDBConstants.CREATED_BY, login_user);
//	        					flag = new InvMstDAO().insertInvMst(htInvMst);
//	        					if(flag) {
//	        						Hashtable htRecvDet = new Hashtable();
//	        						htRecvDet.put(IDBConstants.PLANT, PLANT);
//	        						htRecvDet.put("PONO","");
//	        						htRecvDet.put(IDBConstants.ITEM, Mitem);
//	        						htRecvDet.put(IDBConstants.LOC, Mloc);
//	        						htRecvDet.put("BATCH", Mbatch);
//	        						htRecvDet.put("ORDQTY", STKQTY);
//	        						htRecvDet.put("RECQTY", STKQTY);
//	        						htRecvDet.put("UNITCOST", "");				
//	        						htRecvDet.put("TRAN_TYPE", "INVENTORYUPLOAD");
//	        						htRecvDet.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
//	        						htRecvDet.put(IDBConstants.CREATED_BY, login_user);
//	        						flag = new RecvDetDAO().insertRecvDet(htRecvDet);
//	        					}
//        				}
        				msg = "Processed Successfully ";
        				 }
        			
        			
				}
			}
			 response.sendRedirect(posPageToDirect+"?result="+msg);

			
		}
				else if (action.equalsIgnoreCase("ADDBULKPRODUCT")) {

				String expireDate = "", refno = "";
				refno = request.getParameter("REFERENCENO");

				String empID = StrUtils.fString(request.getParameter("EMP_ID")).trim();
				String refNo = StrUtils.fString(request.getParameter("REFERENCENO")).trim();

				String serialized = StrUtils.fString(request.getParameter("serialized")).trim();
				String tranDate = StrUtils.fString(request.getParameter("TRANSACTIONDATE")).trim();

				String holdReasonCode = StrUtils.fString(request.getParameter("REASONCODE")).trim();
				String holdRemarks = StrUtils.fString(request.getParameter("REMARKS")).trim();

				loc = StrUtils.fString(request.getParameter("LOC0")).trim();
				tranID = StrUtils.fString(request.getParameter("TRANID")).trim();
				if (loc.length() > 0) {

					UserLocUtil uslocUtil = new UserLocUtil();
					uslocUtil.setmLogger(mLogger);
					boolean isvalidlocforUser = uslocUtil.isValidLocInLocmstForUser(PLANT, login_user, loc);
					if (isvalidlocforUser) {
						String[] chkdLnNo = request.getParameterValues("chkdLnNo");
						for (int prod1 = 0; prod1 < chkdLnNo.length; prod1++)

						{

							String checkValue = chkdLnNo[prod1];

							item = StrUtils.fString(request.getParameter("item" + checkValue));
							batch = StrUtils.fString(request.getParameter("nobatch" + checkValue));
							Double qty = Double.parseDouble(StrUtils.fString(request.getParameter("QTY" + checkValue)));
							// scqty = Integer.parseInt(StrUtils.fString(request.getParameter("QTY"+item)));
							scqty = qty.intValue();

							if (item.length() > 0) {
								boolean itemFound = true;
								ItemMstDAO itM = new ItemMstDAO();
								itM.setmLogger(mLogger);
								String scannedItemNo = itM.getItemIdFromAlternate(PLANT, item);
								if (scannedItemNo == null) {
									itemFound = false;
								} else {
									item = scannedItemNo;
								}
								if (itemFound) {
									ITEMMST items = getItemDetails(request, item, scqty, batch);
									ITEMMST prodbean1 = items;
									nonstockflag = prodbean1.getNONSTOCKFLAG();
									/* if(!nonstockflag.equalsIgnoreCase("Y")){ */

									items.setEmpNo(empID);
									items.setRefNo(refNo);
									items.setTranDate(tranDate);
									items.setReasonCode(reasoncode);
									items.setRemarks(remarks);

									Hashtable ht1 = new Hashtable();

									ht1.put(IConstants.PLANT, PLANT);
									ht1.put(IConstants.LOGIN_USER, (String) session.getAttribute("LOGIN_USER"));
									ht1.put(IConstants.LOC, loc);
									ht1.put(IConstants.ITEM, prodbean1.getITEM());
									ht1.put(IConstants.BATCH, prodbean1.getBATCH());
									String stkqty1 = String.valueOf(prodbean1.getStkqty());
									//stkqty1 = StrUtils.TrunkateDecimalForImportData(stkqty1);
									ht1.put(IConstants.QTY, stkqty1);
									nonstockflag = prodbean1.getNONSTOCKFLAG();
									if (!nonstockflag.equalsIgnoreCase("Y")) {
										// boolean invAvailable=CheckInvMst(ht1,request,response);

										boolean invAvailable = false;
										// invAvailable=CheckInvMst(ht1,request,response);
										try {
											invAvailable = CheckInvMstForGoddsIssue(ht1, request, response);

										} catch (Exception e) {
											InvFlag = true;
											session.setAttribute("errmsg", "Not Enough Inventory For Product:"
													+ prodbean1.getITEM() + "  with batch:" + prodbean1.getBATCH());
											String msg = session.getAttribute("errmsg").toString();
											session.setAttribute("poslist", buylist);
											e.printStackTrace(new PrintWriter(msg));
											throw e;
										}

										if (!invAvailable) { // response.sendRedirect("jsp/"+posPageToDirect+"?TRANID="+tranID+"&CHKBATCH="+batchChecked+"&LOC="+loc);
											session.setAttribute("errmsg", "Not Enough Inventory For Product:"
													+ prodbean1.getITEM() + "  with batch:" + prodbean1.getBATCH());
											String msg = session.getAttribute("errmsg").toString();
											session.setAttribute("poslist", buylist);
											RequestDispatcher view = request
													.getRequestDispatcher("jsp/" + posPageToDirect + "?TRANID=" + tranID
															+ "&CHKBATCH=" + batchChecked + "&LOC=" + loc);
											view.forward(request, response);
											return;
										}

									}

									if (buylist == null) {
										match = false;
										buylist = new Vector<ITEMMST>();
										buylist.addElement(items);
										// To check transaction already exists in POSHeader
										Hashtable<String, String> htTrandId = new Hashtable<String, String>();
										htTrandId.put("POSTRANID", tranID);
										boolean istranidExist = _posServlet.isExisit(PLANT, htTrandId);
										if (istranidExist == false) {
											String genPosTranid = poshdr.genPosManualTranId(PLANT, login_user, loc,
													tranID);
										}
										// To check transaction already exists in POSHeader end
										Hashtable<String, String> ht = new Hashtable<String, String>();
										ht.put(IDBConstants.PLANT, PLANT);
										ht.put(IDBConstants.POS_TRANID, tranID);
										String QryUpdate = " SET STATUS ='O',UPAT ='" + DateUtils.getDate()
												+ "',EMPNO='" + empID + "',REFNO='" + refNo + "',TRANDT='" + tranDate
												+ "',RSNCODE='" + holdReasonCode + "',REMARKS='" + holdRemarks + "' ";
										boolean posHsrUpt = posHdr.update(QryUpdate, ht, "");
										if (posHsrUpt) {

											ht.put(IDBConstants.POS_ITEM, items.getITEM());
											ht.put(IDBConstants.POS_ITEMDESC,
													strUtils.InsertQuotes(items.getITEMDESC()));
											ht.put(IDBConstants.POS_QTY, String.valueOf(items.getStkqty()));
											ht.put(IDBConstants.POS_LOC, loc);

											ht.put(IDBConstants.POSDET_STATUS, "N");
											ht.put(IDBConstants.POS_UNITPRICE, "0");
											ht.put(IDBConstants.POS_TOTALPRICE, "0");
											ht.put(IDBConstants.POS_TRANDT, DateUtils.getDate());
											ht.put(IDBConstants.POS_TRANTM, DateUtils.Time());
											ht.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
											ht.put(IDBConstants.CREATED_BY, login_user);
											ht.put(IDBConstants.POS_BATCH, batch);
											// ht.put(IDBConstants.REFNO,refno);
											ht.put("POSTYPE", TRANTYPE);

											try {

												posdet.insertIntoPosDet(ht);
												new TblControlUtil().updateTblControlSeqNo(PLANT, "GIRECEIPT", "GI",
														tranID);

											} catch (Exception e) {
												session.setAttribute("errmsg",
														"Tran Id : " + tranID + " Exists already! ");
												String msg = session.getAttribute("errmsg").toString();
												e.printStackTrace(new PrintWriter(msg));
												throw e;
											}
										}

									} else {
										for (int i = 0; i < buylist.size(); i++) {

											ITEMMST itembean = buylist.elementAt(i);

											if (itembean.getITEM().equalsIgnoreCase(items.getITEM())
													&& itembean.getBATCH().equalsIgnoreCase(items.getBATCH())) {
												if (serialized.equalsIgnoreCase("1")) {
													session.setAttribute("errmsg",
															"Duplicates are not allowed when serialized");
													match = true;
												} else {
													if (scqty > 1) {
														itembean.setStkqty((itembean.getStkqty() + scqty));
														scqty = itembean.getStkqty();
													} else {
														itembean.setStkqty((itembean.getStkqty() + 1));
														scqty = itembean.getStkqty();
													}

													buylist.setElementAt(itembean, i);
													match = true;
													Hashtable<String, String> ht = new Hashtable<String, String>();
													ht.put(IDBConstants.PLANT, PLANT);
													ht.put(IDBConstants.POS_TRANID, tranID);
													ht.put(IDBConstants.POS_ITEM, items.getITEM());
													ht.put(IDBConstants.POS_BATCH, items.getBATCH());
													String Qry = " SET QTY ='" + itembean.getStkqty() + "'";
													posdet.update(Qry, ht, "");
													// update Qty for Same Item
												}
											}
										}
										if (!match) {
											buylist.addElement(items);
											// Insert into posdet with status as N
											Hashtable<String, String> ht = new Hashtable<String, String>();
											ht.put(IDBConstants.PLANT, PLANT);
											ht.put(IDBConstants.POS_TRANID, tranID);
											ht.put(IDBConstants.POS_ITEM, items.getITEM());
											ht.put(IDBConstants.POS_ITEMDESC,
													strUtils.InsertQuotes(items.getITEMDESC()));
											ht.put(IDBConstants.POS_QTY, String.valueOf(items.getStkqty()));
											ht.put(IDBConstants.POS_LOC, loc);
											ht.put(IDBConstants.POS_DISCOUNT, "0");
											ht.put(IDBConstants.POSDET_STATUS, "N");
											ht.put(IDBConstants.POS_UNITPRICE, "0");
											ht.put(IDBConstants.POS_TOTALPRICE, "0");
											ht.put(IDBConstants.POS_TRANDT, DateUtils.getDate());
											ht.put(IDBConstants.POS_TRANTM, DateUtils.Time());
											ht.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
											ht.put(IDBConstants.CREATED_BY, login_user);
											ht.put(IDBConstants.POS_BATCH, batch);
											// ht.put(IDBConstants.REFNO,refno);
											ht.put("POSTYPE", TRANTYPE);

											try {

												posdet.insertIntoPosDet(ht);
												new TblControlUtil().updateTblControlSeqNo(PLANT, "GIRECEIPT", "GI",
														tranID);

											} catch (Exception e) {
												session.setAttribute("errmsg",
														"Tran Id : " + tranID + " Exists already! ");
												String msg = session.getAttribute("errmsg").toString();
												e.printStackTrace(new PrintWriter(msg));
												throw e;
											}
										}
									}
									session.setAttribute("poslist", buylist);
									session.setAttribute("LOCATION", loc);
									if (!posType.equalsIgnoreCase("NO_INV_CHECK")) {
										boolean checkarry[] = new boolean[buylist.size()];
										int index = 0;
										Vector newbuylst = (Vector) session.getAttribute("poslist");
										// Check qty exists in inventory
										for (int i = 0; i < newbuylst.size(); i++) {
											Hashtable ht = new Hashtable();
											ITEMMST prodbean = (ITEMMST) newbuylst.elementAt(i);

											ht.put(IConstants.PLANT, PLANT);
											ht.put(IConstants.LOGIN_USER, (String) session.getAttribute("LOGIN_USER"));
											ht.put(IConstants.LOC, loc);
											ht.put(IConstants.ITEM, prodbean.getITEM());
											ht.put(IConstants.BATCH, prodbean.getBATCH());
											String stkqty = String.valueOf(prodbean.getStkqty());
											//stkqty = StrUtils.TrunkateDecimalForImportData(stkqty);
											ht.put(IConstants.QTY, stkqty);
											nonstockflag = prodbean.getNONSTOCKFLAG();
											if (!nonstockflag.equalsIgnoreCase("Y")) {
												checkarry[index] = CheckInvMst(ht, request, response);
												try {
													checkarry[index] = CheckInvMst(ht, request, response);

												} catch (Exception e) {
													InvFlag = true;
													session.setAttribute("errmsg",
															"Not Enough Inventory For Product:" + prodbean.getITEM()
																	+ "  with batch:" + prodbean.getBATCH());
													String msg = session.getAttribute("errmsg").toString();
													session.setAttribute("poslist", buylist);
													e.printStackTrace(new PrintWriter(msg));
													throw e;
												}

											}

											if (checkarry[index] == true) { // response.sendRedirect("jsp/"+posPageToDirect+"?TRANID="+tranID+"&CHKBATCH="+batchChecked+"&LOC="+loc);
												RequestDispatcher view = request
														.getRequestDispatcher("jsp/" + posPageToDirect + "?TRANID="
																+ tranID + "&CHKBATCH=" + batchChecked + "&LOC=" + loc);
												view.forward(request, response);
											}

											index++;
										}
									}
									/*
									 * }
									 * 
									 * else{ session.setAttribute("errmsg", "Product Id is non inventory product");
									 * }
									 */
								} else {
									session.setAttribute("errmsg", "Product Id not found");
								}

							}
						}
						// session.setAttribute("errmsg", "");
						// response.sendRedirect("jsp/"+posPageToDirect+"?TRANID="+tranID+"&CHKBATCH="+batchChecked+"&LOC="+loc);
						RequestDispatcher view = request.getRequestDispatcher("jsp/" + posPageToDirect + "?TRANID="
								+ tranID + "&CHKBATCH=" + batchChecked + "&LOC=" + loc);
						view.forward(request, response);

					} else {
						session.setAttribute("errmsg",
								"Location : " + loc + " is not a User Assigned Location/Valid Location ");
						// response.sendRedirect("jsp/"+posPageToDirect+".jsp&LOC="+loc);
						RequestDispatcher view = request.getRequestDispatcher("jsp/" + posPageToDirect + "?LOC=" + loc);
						view.forward(request, response);
					}
				} else {
					session.setAttribute("errmsg", "Please Enter Location");
					// response.sendRedirect("jsp/"+posPageToDirect+"&LOC="+loc);
					RequestDispatcher view = request.getRequestDispatcher("jsp/" + posPageToDirect + "?LOC=" + loc);
					view.forward(request, response);
				}

			} else if (cmdAction.equalsIgnoreCase("printproduct")) {

				String TRAN_TYPE = StrUtils.fString(request.getParameter("TRANTYPE")).trim();
				String ISINVENTORYMINQTY =new DoHdrDAO().getISINVENTORYMINQTY(PLANT);

				if (TRAN_TYPE.equalsIgnoreCase("GOODSISSUEWITHOUTBATCH")) {
					posPageToDirect = "MultiMiscOrderIssuing.jsp";
				} else if (TRAN_TYPE.equalsIgnoreCase("GOODSISSUEWITHBATCH")) {
					posPageToDirect = "../salestransaction/goodsissue";
				} else if (TRAN_TYPE.equalsIgnoreCase("GOODSISSUEWITHBATCHPRICE")) {
					posPageToDirect = "dynamicprdswithprice.jsp";
				} else if (TRAN_TYPE.equalsIgnoreCase("MOVEWITHBATCH")) {
//					posPageToDirect = "MultiLocationTransferWithBatch.jsp";
					//posPageToDirect = "../inhouse/stockmove";
					posPageToDirect = "../inhouse/new";
				} else if (TRANTYPE.equalsIgnoreCase("MOVEWITHOUTBATCH")) {
					posPageToDirect = "MultiLocationTransfer.jsp";
				}
				String empID = StrUtils.fString(request.getParameter("EMP_ID")).trim();
				String refNo = StrUtils.fString(request.getParameter("REFERENCENO")).trim();
				String tranDate = StrUtils.fString(request.getParameter("TRANSACTIONDATE")).trim();
				String rsncode = StrUtils.fString(request.getParameter("REASONCODE")).trim();
				double pickingQty = 0,totalqty=0;
				String Quantity ="";
				String alertitem="";
				boolean ajax = false;
				ajax = "XMLHttpRequest".equals(request.getHeader("X-Requested-With"));

				DateUtils dateUtils = new DateUtils();
				String fromdates = dateUtils.parseDateddmmyyyy(tranDate);
				String time = DateUtils.getTimeHHmm();
				String orderdate = fromdates+time+"12";
				LocalDate dates = LocalDate.parse(fromdates, DateTimeFormatter.BASIC_ISO_DATE);
				String formattedDate = dates.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
				String tran = formattedDate;
				
				if (ajax) {

					try {

						UserTransaction ut = null;
						ut = com.track.gates.DbBean.getUserTranaction();
						ut.begin();

						SalesDetailDAO salesdao = new SalesDetailDAO();
						POSPaymentDetailDAO paymentDAO = new POSPaymentDetailDAO();
						String rcpNo = "";
						Generator gen = new Generator();

						// Generate Receipt no
						// rcpNo = this.genReceipt(request);
						boolean flag = false;
						boolean invflag = false;
						// To check transaction already exists in POSHeader
						Hashtable<String, String> htTrandId = new Hashtable<String, String>();
						htTrandId.put("POSTRANID", tranID);
						boolean istranidExist = _posServlet.isExisit(PLANT, htTrandId);
						if (istranidExist == false) {
							String genPosTranid = poshdr.genPosManualTranId(PLANT, login_user, loc, tranID);
						}
						// To check transaction already exists in POSHeader end
						String xmlutil = "";
						HashMap<String, String> htrecv = new HashMap<String, String>();
						String paymentmodes = "";
						MiscIssuingUtil miscutil = new MiscIssuingUtil();
						Hashtable<String, String> htm = new Hashtable<String, String>();
						String formatdt = DateUtils.getDate();
						htm.put("PURCHASEDATE", DateUtils.getDateinyyyy_mm_dd(formatdt));
						htm.put("PURCHASETIME", DateUtils.Time());
						htm.put(IConstants.REMARKS, remarks);
						if (reasoncode.equals("NOREASONCODE"))
						{
							htm.put(IConstants.REASONCODE, " ");
						}
						else{
							htm.put(IConstants.REASONCODE, reasoncode);	
						}
						//htm.put(IConstants.REASONCODE, reasoncode);
						htm.put("EMPNAME", employee);
						htrecv.put(IConstants.PLANT, PLANT);
						if (remarks.length() > 0) {
							htrecv.put(IConstants.REMARKS, "Goods_Issue(with receipt)," + remarks + " " + employee);
						} else {
							htrecv.put(IConstants.REMARKS, "Goods_Issue(with receipt)  " + employee);
						}
						htrecv.put(IConstants.LOGIN_USER, (String) session.getAttribute("LOGIN_USER"));
						htrecv.put(IConstants.LOC, loc);
						miscutil.setmLogger(mLogger);
						salesdao.setmLogger(mLogger);

						String appenddisc = "";
						for (int j = 0; j < buylist.size(); j++) {
							ITEMMST product = buylist.get(j);
							Quantity=String.valueOf(product.getStkqty());
							pickingQty=Double.parseDouble(((String) Quantity.trim().toString()));
							totalqty=totalqty+pickingQty;
							
							htm.put(IConstants.PLANT, PLANT);
							htm.put(IConstants.ITEM, product.getITEM());
							htm.put(IConstants.ITEM_DESC, strUtils.InsertQuotes(product.getITEMDESC()));
							htm.put(IConstants.BATCH, product.getBATCH());
							htm.put(IConstants.QTY, String.valueOf(product.getStkqty()));
							htm.put(IDBConstants.PRICE, "0");
							htm.put(IDBConstants.PAYMENTMODE, paymentmodes);
							htm.put("TranId", tranID);
							htm.put("TranType", "GOODSISSUE");
							htm.put(IDBConstants.LOC, loc);
							htm.put(IDBConstants.POS_DISCOUNT, "0");
							htm.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
							htm.put(IDBConstants.CREATED_BY, login_user);
							htm.put(IConstants.UNITMO, product.getSTKUOM());
							htrecv.put(IConstants.ITEM, product.getITEM());
							htrecv.put(IConstants.QTY, String.valueOf(product.getStkqty()));
							htrecv.put(IConstants.BATCH, product.getBATCH());
							if (product.getBATCHID() != -1) {
								htrecv.put(IConstants.BATCH_ID, String.valueOf(product.getBATCHID()));
							}
							else
							{
								htrecv.put(IConstants.BATCH_ID, "-1");
							}
							htrecv.put("TranId", tranID);
							htrecv.put("DIRTYPE", "CHECK_OUT");
							htrecv.put(IConstants.TRAN_TYPE, TRANTYPE);
							htrecv.put(IConstants.REFNO, refNo);
							htrecv.put(IConstants.CNAME, empID);
							htrecv.put(IConstants.REASONCODE, rsncode);
							htrecv.put(IConstants.EMPNO, empID);
							htrecv.put(IConstants.ISSUEDATE, tranDate);
							htrecv.put(IConstants.RECIEVEDATE, tranDate);							
							htrecv.put("TRANDATE", tran);
							nonstockflag = product.getNONSTOCKFLAG();	
							htrecv.put(IConstants.NONSTKFLAG, nonstockflag);
							// Deducting from inventory
							// String tranid=(String)session.getAttribute("tranid");
							htrecv.put(IConstants.MOVHIS_ORDNUM, tranID);
							htrecv.put(IConstants.UOM, product.getSTKUOM());
							htrecv.put(IConstants.OUT_DOLNNO, Integer.toString(j + 1));
							String UOMQTY="1";
							Hashtable<String, String> htTrand1 = new Hashtable<String, String>();
							MovHisDAO movHisDao1 = new MovHisDAO();
							ArrayList getuomqty = movHisDao1.selectForReport("select ISNULL(QPUOM,1) as UOMQTY from "+PLANT+"_UOM where UOM='"+product.getSTKUOM()+"'",htTrand1);
							if(getuomqty.size()>0)
							{
							Map mapval = (Map) getuomqty.get(0);
							UOMQTY=(String)mapval.get("UOMQTY");
							}
							htrecv.put("UOMQTY", UOMQTY);
							List listQry = new InvMstDAO().getOutBoundPickingBatchByWMS(PLANT,product.getITEM(), loc, product.getBATCH());
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
										alertitem =product.getITEM();
									}else {
										alertitem = alertitem+" , "+product.getITEM();
									}
								}
									
								}
							}
							try {
								// if(!posType.equalsIgnoreCase("NO_INV_CHECK")&&!nonstockflag.equalsIgnoreCase("Y")){
								if (!posType.equalsIgnoreCase("NO_INV_CHECK") && !nonstockflag.equalsIgnoreCase("Y")) 
									flag = processIssueHis(htrecv);
								else
									flag = processShipHis(htrecv);
									//POSDET Update Line No.
									Hashtable<String, String> htup = new Hashtable<String, String>();
									htup.put(IDBConstants.PLANT, PLANT);
									htup.put(IDBConstants.POS_TRANID, tranID);
									htup.put(IDBConstants.POS_ITEM, product.getITEM());
									htup.put(IDBConstants.POS_BATCH, product.getBATCH());
									htup.put(IConstants.LOC, loc);
									htup.put(IConstants.UNITMO, product.getSTKUOM());
									htup.put(IConstants.QTY, String.valueOf(product.getStkqty()));
									String Qry="SET DOLNNO ='" + (j+1) +"'";
									posdet.update(Qry, htup, "");

									if (flag == false) {
										errmsg = "Not Enough Inventory";
									} else {
										flag = miscutil.process_PosIssueMaterial(htrecv);
										if (flag == false) {
											errmsg = "Not Enough Inventory";
										} 
										/*else {
										flag = processMovHisForPosWoInvCheck(htrecv);
										}*/
									}
								
								salesdao.setmLogger(mLogger);
								if (flag == true) {
									flag = salesdao.insertIntoSalesDet(htm);
									// processDOHDR(htrecv);
								}
								// new TblControlUtil().updateTblControlSeqNo(PLANT,"GIRECEIPT","GI",tranID);
								
								if (flag == true) {//Shopify Inventory Update
	               					/*Hashtable htCond = new Hashtable();
	               					htCond.put(IConstants.PLANT, PLANT);
	               					if(new IntegrationsDAO().getShopifyConfigCount(htCond,"")) {
	               						String nonstkflag = new ItemMstDAO().getNonStockFlag(PLANT,product.getITEM());						
	            						if(nonstkflag.equalsIgnoreCase("N")) {
	               						String availqty ="0";
	               						ArrayList invQryList = null;
	               						htCond = new Hashtable();
	               						invQryList= new InvUtil().getInvListSummaryAvailableQtyforShopify(PLANT,product.getITEM(), new ItemMstDAO().getItemDesc(PLANT, product.getITEM()),htCond);						
	               						if (invQryList.size() > 0) {					
	               							for (int iCnt =0; iCnt<invQryList.size(); iCnt++){
	               								String result="";
	                                            Map lineArr = (Map) invQryList.get(iCnt);
	                                            availqty = (String)lineArr.get("AVAILABLEQTY");
	                                            System.out.println(availqty);
	               							}
	               							double availableqty = Double.parseDouble(availqty);
	                   						new ShopifyService().UpdateShopifyInventoryItem(PLANT, product.getITEM(), availableqty);
	               						}
	            						}
	               					}*/
	               					
	               					Thread apiInvUpdateJob = new Thread(new ApiInventoryUpdateJob(product.getITEM(), PLANT));
	               					apiInvUpdateJob.start();
	               				}

							} catch (Exception e) {
								flag = false;
								DbBean.RollbackTran(ut);
								throw e;

							}

						}
						 if(flag == true){
							 DoDetDAO _DoDetDAO = new DoDetDAO();
								DateUtils dateutils = new DateUtils();
							  Hashtable	ht=new Hashtable();
							  ht.clear();
							  ht.put(IConstants.PLANT,PLANT);
			                  ht.put("GINO",tranID);
			                  ht.put(IConstants.CUSTOMER_CODE, "");
			                  ht.put(IConstants.DODET_DONUM,"");
			                  ht.put(IConstants.STATUS, "NON INVOICEABLE");
			                  ht.put(IConstants.AMOUNT, "");
			                  ht.put(IConstants.QTY, String.valueOf(totalqty));
//			                  ht.put("CRAT",dateutils.getDateTime());
			                  ht.put("CRAT",orderdate);
			                  ht.put("CRBY",login_user);
			                  ht.put("UPAT",dateutils.getDateTime());
				              flag = _DoDetDAO.insertGINOtoInvoice(ht);
				              
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
				    			htRecvHis.put(IDBConstants.CREATED_BY, login_user);
				    			htRecvHis.put(IDBConstants.REMARKS, "");        					
				    			htRecvHis.put(IDBConstants.TRAN_DATE, dateutils.getDateinyyyy_mm_dd(dateutils.getDate()));
				    			htRecvHis.put(IDBConstants.MOVHIS_ORDNUM, tranID);
				    			htRecvHis.put(IDBConstants.CREATED_AT, dateutils.getDateTime());
				    			flag = movHisDao.insertIntoMovHis(htRecvHis);
						 }
						if (flag == true)
							// update POSHDR AND PODET To Close Status
							buylist.removeAllElements();
							/*new TblControlUtil().updateTblControlSeqNoWithoutorder(PLANT, "GIRECEIPT",
								"GI");*/
						 new TblControlUtil().updateTblControlIESeqNo(PLANT, "GINO", "GI", tranID);
						Hashtable<String, String> ht = new Hashtable<String, String>();
						ht.put(IDBConstants.PLANT, PLANT);
						ht.put(IDBConstants.POS_TRANID, tranID);
						String QryUpdate = " SET STATUS ='C',RECEIPTNO='" + tranID + "',UPAT ='" + DateUtils.getDate()
								+ "' ";
						boolean posHsrUpt = posHdr.update(QryUpdate, ht, "");
						if (posHsrUpt) {
							QryUpdate = " SET TRANSTATUS ='C',UPAT ='" + DateUtils.getDate() + "' ";
							posdet.update(QryUpdate, ht, "");
						}

						session.setAttribute("poslist", buylist);
						// session.setAttribute("poslist", buylist);
						DbBean.CommitTran(ut);

						if (flag == false) {
							DbBean.RollbackTran(ut);
							session.setAttribute("errmsg", errmsg);
							// response.sendRedirect("jsp/"+posPageToDirect+"?TRANID="+tranID+"&CHKBATCH="+batchChecked);
							RequestDispatcher view = request.getRequestDispatcher(
								"jsp/" + posPageToDirect + "?TRANID=" + tranID + "&CHKBATCH=" + batchChecked);
							view.forward(request, response);
							
							
						} else {
							// viewPOSReport(request, response, rcpNo);
							POSUtil posUtil = new POSUtil();
							Map mhdr = posUtil.getPosReceiptHdrDetails(PLANT);
							String download = (String) mhdr.get("DOWNLOADPDF");
							session.setAttribute("errmsg", "");
							buylist.removeAllElements();
							session.setAttribute("poslist", buylist);
							// session.setAttribute("poslist", buylist);

							response.setContentType("text/html");
							PrintWriter out = response.getWriter();
							if(ISINVENTORYMINQTY.equalsIgnoreCase("1")) 
								alertitem = alertitem;
							else 
								alertitem="";
							if(alertitem.equalsIgnoreCase("")) {
//							out.write("Success:" + tranID + ":" + download);
							out.write("Success:" + tranID + ":" + download+":" +"ERROR"+":" +alertitem);
							}else {
								out.write("Success:" + tranID + ":" + download+":" +"Warning"+":" +alertitem);
							}
							out.close();

						}

					} catch (Exception e) {
						session.setAttribute("errmsg", e.getMessage());
						response.sendRedirect(
								"jsp/" + posPageToDirect + "?TRANID=" + tranID + "&CHKBATCH=" + batchChecked);
						// RequestDispatcher view =
						// request.getRequestDispatcher("jsp/"+posPageToDirect+"?TRANID="+tranID+"&CHKBATCH="+batchChecked);
						// view.forward(request, response);
					}
				} else {
					// String recNo = request.getParameter("RCPNO");
					viewPOSProductReport(request, response, tranID);
				}
				} else if (cmdAction.equalsIgnoreCase("printproductwp")) {

				String TRAN_TYPE = StrUtils.fString(request.getParameter("TRANTYPE")).trim();

				if (TRAN_TYPE.equalsIgnoreCase("GOODSISSUEWITHBATCHPRICE")) {
					posPageToDirect = "dynamicprdswithprice.jsp";
				}
				Hashtable ht = new Hashtable();
				String rflag = "", ordertypeString = "", result = "", dono = "", custName = "", display = "", ordertype = "",
						currencyid = "", custCode = "", jobNum = "", personIncharge = "", user_id = "", contactNum = "",
						address = "", address2 = "", address3 = "",  collectionTime = "", deliverydate = "",
						timeslots = "", outbound_Gst = "", orderstatus = "",  remark1 = "", remark2 = "",
						remark3 = "", deldate = "", processno = "", shipCustname = "", shipCname = "", shipaddr1 = "",
						shipaddr2 = "", shipCity = "", shipCountry = "", shipZip = "", shiptelno = "", shippingcustomer = "",
						shippingid = "", orderdiscount = "", shippingcost = "", incoterms = "", recstatus="I",EDIT="",CashCust="", INVOICENO = "";
				String lineqty = "";
				dono = StrUtils.fString(request.getParameter("TRANID")).trim();
				custName = StrUtils.replaceCharacters2Recv(StrUtils.fString(request.getParameter("CUST_NAME"))).trim();
				custCode = StrUtils.fString(request.getParameter("CUST_CODE1")).trim();
				personIncharge = StrUtils.fString(request.getParameter("PERSON_INCHARGE")).trim();
				contactNum = StrUtils.fString(request.getParameter("CONTACT_NUM")).trim();
				address = StrUtils.fString(request.getParameter("ADD1")).trim();
				address2 = StrUtils.fString(request.getParameter("ADD2")).trim();
				address3 = StrUtils.fString(request.getParameter("ADD3")).trim();
				remark1 = StrUtils.fString(request.getParameter("REMARK1")).trim();
				remark2 = StrUtils.fString(request.getParameter("REMARK2")).trim();
				remark3 = StrUtils.fString(request.getParameter("REMARK3")).trim();
				display = StrUtils.fString(request.getParameter("DISPLAY")).trim();
				ordertype = StrUtils.fString(request.getParameter("ORDERTYPE")).trim();
				deliverydate = StrUtils.fString(request.getParameter("DELIVERYDATE")).trim();				
				outbound_Gst = StrUtils.fString(request.getParameter("GST")).trim();
				shippingcustomer = StrUtils.fString(request.getParameter("SHIPPINGCUSTOMER")).trim();
				shippingid = StrUtils.fString(request.getParameter("SHIPPINGID")).trim();
				orderdiscount = StrUtils.fString(request.getParameter("ORDERDISCOUNT")).trim();
				shippingcost = StrUtils.fString(request.getParameter("SHIPPINGCOST")).trim();
				incoterms = StrUtils.fString(request.getParameter("INCOTERMS")).trim();
				collectionTime = StrUtils.fString(request.getParameter("COLLECTION_TIME")).trim();
				EDIT = StrUtils.fString(request.getParameter("EDIT")).trim();
				CashCust = StrUtils.fString(request.getParameter("CashCust")).trim();
				if(CashCust.equals("on"))
					CashCust="1";
				String empID = StrUtils.fString(request.getParameter("EMP_ID")).trim();
				String refNo = StrUtils.fString(request.getParameter("REFERENCENO")).trim();
				String tranDate = StrUtils.fString(request.getParameter("TRANSACTIONDATE")).trim();
				String rsncode = StrUtils.fString(request.getParameter("REASONCODE")).trim();
				String uom = StrUtils.fString(request.getParameter("UOM")).trim();
				String paymenttype= StrUtils.fString(request.getParameter("PAYMENTTYPE")).trim();
				INVOICENO = StrUtils.fString(request.getParameter("INVOICENO"));
				String INVOICECHECK = StrUtils.fString(request.getParameter("INVOICECHECK"));
				String DATEFORMAT = StrUtils.fString((request.getParameter("DATEFORMAT") != null) ? "1": "0").trim();
				Hashtable curHash = new Hashtable();
				curHash.put(IConstants.PLANT, PLANT);
				curHash.put(IConstants.DISPLAY, display);
				if (display != null && display != "") {
					currencyid = curUtil.getCurrencyID(curHash, "CURRENCYID");
				}
				boolean isvalidOrderType = true;
				if (ordertype.length() > 0) {
				if (!ordertype.equals("") && !ordertype.equals("MOBILE ORDER") && !ordertype.equals("MOBILE ENQUIRY")
						&& !ordertype.equals("MOBILE REGISTRATION")) {
					OrderTypeBeanDAO orderTypeBeanDAO = new OrderTypeBeanDAO();
					orderTypeBeanDAO.setmLogger(mLogger);
					isvalidOrderType = orderTypeBeanDAO.isOrderTypeExists(ordertype, PLANT);
				}
				}
				CustomerBeanDAO customerBeanDAO = new CustomerBeanDAO();
				customerBeanDAO.setmLogger(mLogger);
				boolean isvalidCustomer =  Boolean.valueOf(true);
				if (custName.length() > 0) {
				isvalidCustomer = customerBeanDAO.isExistsCustomerName(custName, PLANT);
				}
				Boolean isvalidShippingCustomer = Boolean.valueOf(true);
				if (shippingcustomer.length() > 0) {
					isvalidShippingCustomer = _MasterDAO.isExistsShippingDetails(shippingcustomer, PLANT);
				}
				
							
						
				boolean flag = false;
				boolean ajax = false;
				ajax = "XMLHttpRequest".equals(request.getHeader("X-Requested-With"));

				if (ajax) {

					try {
						if (isvalidCustomer) {
							if (isvalidShippingCustomer) {
								if (isvalidOrderType) {
									
									ht.put(IDBConstants.PLANT, PLANT);
									ht.put(IDBConstants.POS_TRANID, dono);
									ht.put(IDBConstants.DOHDR_CUST_CODE, custCode);
									ht.put(IDBConstants.DOHDR_CUST_NAME, strUtils.InsertQuotes(custName));
									ht.put(IDBConstants.DOHDR_JOB_NUM, refNo);
									ht.put(IDBConstants.DOHDR_PERSON_INCHARGE, strUtils.InsertQuotes(personIncharge));
									ht.put(IDBConstants.DOHDR_CONTACT_NUM, contactNum);
									ht.put(IDBConstants.DOHDR_ADDRESS, address);
									ht.put(IDBConstants.DOHDR_ADDRESS2, address2);
									ht.put(IDBConstants.DOHDR_ADDRESS3, address3);									
									ht.put(IDBConstants.DOHDR_REMARK1, strUtils.InsertQuotes(remark1));
									ht.put(IDBConstants.DOHDR_REMARK2, strUtils.InsertQuotes(remark2));
									ht.put(IDBConstants.DOHDR_REMARK3, strUtils.InsertQuotes(remark3));
									ht.put(IDBConstants.STATUS, "O");
									ht.put(IDBConstants.ORDERTYPE, ordertype);
									ht.put(IDBConstants.CURRENCYID, currencyid);								
									
									ht.put(IDBConstants.DELIVERYDATE, deliverydate);
									ht.put(IDBConstants.DOHDR_GST, outbound_Gst);									
									ht.put(IDBConstants.DOHDR_EMPNO, empID);
									ht.put(IDBConstants.SHIPPINGCUSTOMER, shippingcustomer);
									ht.put(IDBConstants.SHIPPINGID, shippingid);
									ht.put(IDBConstants.ORDERDISCOUNT, orderdiscount);
									ht.put(IDBConstants.SHIPPINGCOST, shippingcost);
									ht.put(IDBConstants.INCOTERMS, incoterms);
									ht.put(IDBConstants.CASHCUST, CashCust);
									ht.put(IDBConstants.UNITMO, uom);
									ht.put(IDBConstants.PAYMENTTYPE, paymenttype);
									ht.put(IDBConstants.POSHDR_DELIVERYDATEFORMAT, DATEFORMAT);
									POSUtil posUtil = new POSUtil();
									posUtil.setmLogger(mLogger);
									flag = posUtil.updatePosHdr(ht);
									
									
						UserTransaction ut = null;
						ut = com.track.gates.DbBean.getUserTranaction();
						ut.begin();

						SalesDetailDAO salesdao = new SalesDetailDAO();
						POSPaymentDetailDAO paymentDAO = new POSPaymentDetailDAO();
						String rcpNo = "";
						Generator gen = new Generator();
						DateUtils dateUtils = new DateUtils();

						// Generate Receipt no
						// rcpNo = this.genReceipt(request);
						
						boolean invflag = false;
						// To check transaction already exists in POSHeader
						Hashtable<String, String> htTrandId = new Hashtable<String, String>();
						htTrandId.put("POSTRANID", tranID);
						boolean istranidExist = _posServlet.isExisit(PLANT, htTrandId);
						if (istranidExist == false) {
							String genPosTranid = poshdr.genPosManualTranId(PLANT, login_user, loc, tranID);
						}
						// To check transaction already exists in POSHeader end
						String xmlutil = "";
						HashMap<String, String> htrecv = new HashMap<String, String>();
						String paymentmodes = "";
						MiscIssuingUtil miscutil = new MiscIssuingUtil();
						Hashtable<String, String> htm = new Hashtable<String, String>();
						String formatdt = DateUtils.getDate();
						htm.put("PURCHASEDATE", DateUtils.getDateinyyyy_mm_dd(formatdt));
						htm.put("PURCHASETIME", DateUtils.Time());
						htm.put(IConstants.REMARKS, remarks);
						if (reasoncode.equals("NOREASONCODE"))
						{
							htm.put(IConstants.REASONCODE, " ");
						}
						else{
							htm.put(IConstants.REASONCODE, reasoncode);	
						}
						
						htm.put("EMPNAME", employee);
						htrecv.put(IConstants.PLANT, PLANT);
						if (remarks.length() > 0) {
							htrecv.put(IConstants.REMARKS, "Tax_Invoice," + remarks + " " + employee);
						} else {
							htrecv.put(IConstants.REMARKS, "Tax_Invoice  " + employee);
						}
						htrecv.put(IConstants.LOGIN_USER, (String) session.getAttribute("LOGIN_USER"));
						
						miscutil.setmLogger(mLogger);
						salesdao.setmLogger(mLogger);

						String appenddisc = "";
						for (int j = 0; j < buylist.size(); j++) {
							ITEMMST product = buylist.get(j);
							loc=String.valueOf(product.getLoc());
							String price = StrUtils.removeFormat(product.getUSERFLD4());
							price = new POSUtil().getConvertedUnitCostToLocalCurrency(PLANT, tranID, price);
							
							//Due to decimal Issue converting unitprice in float value and making transaction -- Azees 								
							float PricewTaxVal ="".equals(price) ? 0.0f :  Float.parseFloat(price);
							String priceval = String.valueOf(PricewTaxVal);
							nonstockflag = product.getNONSTOCKFLAG();
							/*if(nonstockflag.equalsIgnoreCase("Y"))
							{
							String s = price;
							if (s.indexOf("-") == -1) {
								price = "-" + price;
							}
							s = priceval;
							if (s.indexOf("-") == -1) {
								priceval = "-" + priceval;
							}
							}*/
							htm.put(IConstants.PLANT, PLANT);
							htm.put(IConstants.ITEM, product.getITEM());
							htm.put(IConstants.ITEM_DESC, strUtils.InsertQuotes(product.getITEMDESC()));
							htm.put(IConstants.BATCH, product.getBATCH());
							htm.put(IConstants.QTY, String.valueOf(product.getStkqty()));
							htm.put(IDBConstants.PRICE, price);
							htm.put(IDBConstants.PAYMENTMODE, paymentmodes);
							htm.put("TranId", tranID);
							htm.put("TranType", "TAXINVOICE");
							htm.put(IDBConstants.LOC, String.valueOf(product.getLoc()));
							htm.put(IDBConstants.POS_DISCOUNT, "0");
							htm.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
							htm.put(IDBConstants.CREATED_BY, login_user);
							htm.put(IConstants.UNITMO, product.getSTKUOM());
							htrecv.put(IConstants.ITEM, product.getITEM());
							htrecv.put(IConstants.QTY, String.valueOf(product.getStkqty()));
							htrecv.put(IConstants.BATCH, product.getBATCH());
							htrecv.put(IConstants.LOC, String.valueOf(product.getLoc()));
							if (product.getBATCHID() != -1) {
								htrecv.put(IConstants.BATCH_ID, String.valueOf(product.getBATCHID()));
							}
							else
							{
								htrecv.put(IConstants.BATCH_ID, "-1");
							}
							htrecv.put("TranId", tranID);
							htrecv.put("DIRTYPE", "TAXINVOICE_OUT");
							htrecv.put(IConstants.TRAN_TYPE, TRANTYPE);
							htrecv.put(IConstants.REFNO, refNo);
							htrecv.put(IConstants.CNAME, empID);
							htrecv.put(IConstants.REASONCODE, rsncode);
							htrecv.put(IConstants.EMPNO, empID);
							htrecv.put(IConstants.ISSUEDATE, tranDate);
							htrecv.put(IConstants.RECIEVEDATE, tranDate);
							// Deducting from inventory
							// String tranid=(String)session.getAttribute("tranid");
							htrecv.put(IConstants.MOVHIS_ORDNUM, tranID);
							htrecv.put(IConstants.OUT_DOLNNO, Integer.toString(j + 1));
							htrecv.put(IConstants.INVOICENO, INVOICENO);
							htrecv.put(IConstants.CURRENCYID, currencyid);
							htrecv.put("NONSTKFLAG", nonstockflag);
							String UOMQTY="1";
							Hashtable<String, String> htTrand1 = new Hashtable<String, String>();
							MovHisDAO movHisDao1 = new MovHisDAO();
							ArrayList getuomqty = movHisDao1.selectForReport("select ISNULL(QPUOM,1) as UOMQTY from "+PLANT+"_UOM where UOM='"+String.valueOf(product.getSTKUOM())+"'",htTrand1);
							if(getuomqty.size()>0)
							{
							Map mapval = (Map) getuomqty.get(0);
							UOMQTY=(String)mapval.get("UOMQTY");
							}
							htrecv.put("UOMQTY", UOMQTY);
							try {								
								
									htrecv.put(IDBConstants.PRICE, price);
									htrecv.put(IDBConstants.DOHDR_CUST_NAME, strUtils.InsertQuotes(custName));
									if(EDIT!="")//SHIPHIS Insert and Update
									{
										ShipHisDAO shiphstdao = new ShipHisDAO();										
										shiphstdao.setmLogger(mLogger);
										Hashtable<String, String> htTrandId1 = new Hashtable<String, String>();										
										htTrandId1.put(IConstants.DODET_DONUM, tranID);
										htTrandId1.put(IConstants.PLANT, PLANT);
										htTrandId1.put(IConstants.ITEM, product.getITEM());
										htTrandId1.put(IConstants.LOC, String.valueOf(product.getLoc()));	
										if (product.getBATCHID() != -1) {
											htTrandId1.put(IConstants.INVID, String.valueOf(product.getBATCHID()));
										}
										htTrandId1.put(IDBConstants.PRICE, price);
										//htTrandId1.put(IDBConstants.PRICE, String.valueOf(product.getUSERFLD6()));
										flag = shiphstdao.isExisit(htTrandId1);
										Boolean chkinv=true;
										if(!INVOICENO.equalsIgnoreCase("")) {
											htTrandId1.put(IConstants.INVOICENO, INVOICENO);
											chkinv = shiphstdao.isExisit(htTrandId1);											
											}
										htTrandId1.remove(IDBConstants.INVOICENO);
										if (flag == false)
										{											
											htTrandId1.remove(IDBConstants.PRICE);								
											htTrandId1.put(IDBConstants.PRICE, priceval);
											flag = shiphstdao.isExisit(htTrandId1);
											if(!INVOICENO.equalsIgnoreCase("")) {
												htTrandId1.put(IConstants.INVOICENO, INVOICENO);
												chkinv = shiphstdao.isExisit(htTrandId1);											
												}
											htTrandId1.remove(IDBConstants.INVOICENO);
										}
										
										if (flag == false)
										{
											if (!posType.equalsIgnoreCase("NO_INV_CHECK") && !nonstockflag.equalsIgnoreCase("Y"))
												flag = processIssueHis(htrecv);
											else
												flag = processShipHis(htrecv);
										}
										else
										{
											String Qry="";												
											Qry = " SET ORDQTY ='0',PICKQTY ='0'";
											shiphstdao.update(Qry, htTrandId1, "");
											
											
											Qry = " SET ORDQTY =ORDQTY+'" + String.valueOf(product.getStkqty()) + "',PICKQTY =PICKQTY+'" + String.valueOf(product.getStkqty()) + "'";
											shiphstdao.update(Qry, htTrandId1, "");
											
											if (chkinv == false)
											{
												Qry="";												
												Qry = " SET INVOICENO ='"+INVOICENO+"'";
												shiphstdao.update(Qry, htTrandId1, " AND INVOICENO='' ");
											}
										}
										
									}
									else
									{
										if (!posType.equalsIgnoreCase("NO_INV_CHECK") && !nonstockflag.equalsIgnoreCase("Y"))
											flag = processIssueHis(htrecv);
										else
											flag = processShipHis(htrecv);
									}
									htrecv.remove(IDBConstants.DOHDR_CUST_NAME);
									htrecv.remove(IDBConstants.PRICE);
									if (flag == false) {
										errmsg = "Not Enough Inventory";
									} else {
										
										if(EDIT!="")
										{
											//INSERT MOVHIS
										Hashtable<String, String> htTrandId1 = new Hashtable<String, String>();
										MovHisDAO movHisDao = new MovHisDAO();
										/*htTrandId1.put("ORDNUM", tranID);
										htTrandId1.put(IConstants.PLANT, PLANT);
										htTrandId1.put(IConstants.LOC, String.valueOf(product.getLoc()));	
										htTrandId1.put("BATNO", product.getBATCH());
										htTrandId1.put(IConstants.QTY, String.valueOf(product.getStkqty()));
										htTrandId1.put(IConstants.ITEM, product.getITEM());
										htTrandId1.put("MOVTID", "OUT");*/																				
										ArrayList getpreqty = movHisDao.selectForReport("select  (ISNULL((select SUM(QTY) from ["+PLANT+"_MOVHIS] where ORDNUM='"+tranID+"' AND ITEM = '"+product.getITEM()+"' AND LOC = '"+String.valueOf(product.getLoc())+"' AND PLANT = '"+PLANT+"'  AND BATNO = '"+product.getBATCH()+"' AND MOVTID='IN'),0)) - (ISNULL((select SUM(QTY) from ["+PLANT+"_MOVHIS] where ORDNUM='"+tranID+"' AND ITEM = '"+product.getITEM()+"' AND LOC = '"+String.valueOf(product.getLoc())+"' AND PLANT = '"+PLANT+"'  AND BATNO = '"+product.getBATCH()+"' AND MOVTID='OUT'),0)) as Qty ",htTrandId1);
										if( getpreqty.size()>0)
										{
											double currstqty=0,newstqty=0;
										Map map = (Map) getpreqty.get(0);										
										double tqty =  Double.parseDouble((String)map.get("Qty"));
										if(tqty <= 0)
										//flag = movHisDao.isExisit(htTrandId1,"");
										//if (flag == false)
											{
											String stqty =  String.valueOf(tqty);
											stqty =stqty.replace("-", "");											
											newstqty =  Double.parseDouble(stqty);
											//get shipshis qty
											htTrandId1 = new Hashtable<String, String>();
											htTrandId1.put("POSTRANID", tranID);
											htTrandId1.put(IConstants.PLANT, PLANT);
											htTrandId1.put(IConstants.LOC, String.valueOf(product.getLoc()));	
											htTrandId1.put(IConstants.BATCH, product.getBATCH());											
											htTrandId1.put(IConstants.ITEM, product.getITEM());
											htTrandId1.put(IConstants.ITEM_DESC, product.getITEMDESC());
											//htTrandId1.put(IDBConstants.PRICE, price);
											getpreqty = movHisDao.selectForReport("select ISNULL(SUM(QTY),0) as Qty from "+PLANT+"_POSDET WHERE PLANT='"+PLANT+"' ",htTrandId1);
											if( getpreqty.size()>0)
											{
											map = (Map) getpreqty.get(0);
											newstqty =  Double.parseDouble((String)map.get("Qty"))-newstqty;
											}
											//currstqty =  currstqty + product.getStkqty();
											
											//newstqty =  currstqty - Double.parseDouble(stqty);
											///htTrandId1.remove(IConstants.QTY);											
											 //getpreqty = movHisDao.selectForReport("select ISNULL(SUM(QTY),0) as Qty from "+PLANT+"_MOVHIS WHERE PLANT='"+PLANT+"' ",htTrandId1);
											//if( getpreqty.size()>0)
											//{
												//	for (int i = 0; i < getpreqty.size(); i++) {
													//	 map = (Map) getpreqty.get(i);														
														//newstqty =  currstqty - Double.parseDouble((String)map.get("Qty"));
													//}	
													htrecv.remove(IConstants.QTY);
													htrecv.put(IConstants.QTY, String.valueOf(newstqty));	
											//}
											}
											htrecv.remove("DIRTYPE");
											htrecv.put("DIRTYPE", TransactionConstants.EDIT_DIRECT_TAX_INVOICE);
											if(newstqty>0)
											{
												if (!posType.equalsIgnoreCase("NO_INV_CHECK"))
													flag = miscutil.process_PosIssueMaterial(htrecv);
											}
											htrecv.put(IConstants.QTY, String.valueOf(product.getStkqty()));
											if (flag == false) {
												errmsg = "Not Enough Inventory";
											}
											/*else {																						
													flag = processMovHisForPosWoInvCheck(htrecv);										
												}*/
											}									
										}
										else
										{										
										if (!posType.equalsIgnoreCase("NO_INV_CHECK"))
												flag = miscutil.process_PosIssueMaterial(htrecv);
										if (flag == false) {
											errmsg = "Not Enough Inventory";
										}
										
										/*else {																						
												flag = processMovHisForPosWoInvCheck(htrecv);										
											}*/
									}
									}
								
								salesdao.setmLogger(mLogger);
								if (flag == true) {
									if(EDIT!="")
									{
									// INSERT SALES_DETAIL
									Hashtable<String, String> htTrandId1 = new Hashtable<String, String>();
									htTrandId1.put("TRANID", tranID);
									htTrandId1.put(IConstants.PLANT, PLANT);
									htTrandId1.put(IConstants.LOC, String.valueOf(product.getLoc()));	
									htTrandId1.put(IConstants.BATCH, product.getBATCH());
									//htTrandId1.put(IConstants.QTY, String.valueOf(product.getStkqty()));
									htTrandId1.put(IConstants.ITEM, product.getITEM());
									htTrandId1.put(IConstants.ITEM_DESC, product.getITEMDESC());
									htTrandId1.put(IDBConstants.PRICE, price);
									htTrandId1.put(IConstants.UNITMO, product.getSTKUOM());
									flag = salesdao.isExisit(htTrandId1,"");
									if (flag == false)
									{
										htTrandId1.remove(IDBConstants.PRICE);								
										htTrandId1.put(IDBConstants.PRICE, priceval);
										flag = salesdao.isExisit(htTrandId1,"");
									}
									if (flag == false)
										flag = salesdao.insertIntoSalesDet(htm);									
									else
									{
										String Qry="";												
										Qry = " SET QTY ='0'";
										salesdao.update(Qry, htTrandId1, "");
										
										
										Qry = " SET QTY =QTY+'" + String.valueOf(product.getStkqty()) + "'";
										salesdao.update(Qry, htTrandId1, "");
									}
									}
									else
										flag = salesdao.insertIntoSalesDet(htm);
									
								}
								
								//POSDET Update Line No.
								Hashtable<String, String> htup = new Hashtable<String, String>();
								htup.put(IDBConstants.PLANT, PLANT);
								htup.put(IDBConstants.POS_TRANID, tranID);
								htup.put(IDBConstants.POS_ITEM, product.getITEM());
								htup.put(IDBConstants.POS_BATCH, product.getBATCH());
								htup.put(IConstants.LOC,String.valueOf(product.getLoc()));
								htup.put(IConstants.UNITMO, product.getSTKUOM());
								htup.put(IConstants.QTY, String.valueOf(product.getStkqty()));
								String Qry="SET DOLNNO ='" + (j+1) +"'";
								posdet.update(Qry, htup, "");
								

							} catch (Exception e) {
								flag = false;
								DbBean.RollbackTran(ut);
								throw e;

							}

						}

						if (flag == true)
							// update POSHDR AND PODET To Close Status
							buylist.removeAllElements();
						// update seq No
						if(EDIT=="")
						{
						new TblControlUtil().updateTblControlSeqNoWithoutorder(PLANT, "TAXINVOICE",
								"TI");
						}
						if(INVOICECHECK!="")
						{
					    new TblControlUtil().updateTblControlIESeqNo(PLANT, "INVOICE", "IN", INVOICENO);
						}
						Hashtable<String, String> hts = new Hashtable<String, String>();
						hts.put(IDBConstants.PLANT, PLANT);
						hts.put(IDBConstants.POS_TRANID, tranID);
						String QryUpdate = " SET STATUS ='C',RECEIPTNO='" + tranID + "',UPAT ='" + DateUtils.getDate()
								+ "' ";
						boolean posHsrUpt = posHdr.update(QryUpdate, hts, "");
						if (posHsrUpt) {
							QryUpdate = " SET TRANSTATUS ='C',UPAT ='" + DateUtils.getDate() + "' ";
							posdet.update(QryUpdate, hts, "");
						}
						
						session.setAttribute("poslist", buylist);
						
						DbBean.CommitTran(ut);

						if (flag == false) {
							DbBean.RollbackTran(ut);
							session.setAttribute("errmsg", errmsg);
							
							RequestDispatcher view = request.getRequestDispatcher(
								"jsp/" + posPageToDirect + "?TRANID=" + tranID + "&CHKBATCH=" + batchChecked);
							view.forward(request, response);
							
							
						} else {
							
							posUtil = new POSUtil();
							Map mhdr = posUtil.getPosReceiptHdrDetails(PLANT);
							String download = (String) mhdr.get("DOWNLOADPDF");
							session.setAttribute("errmsg", "");
							buylist.removeAllElements();
							session.setAttribute("poslist", buylist);
							

							response.setContentType("text/html");
							PrintWriter out = response.getWriter();
							out.write("Success:" + tranID + ":" + download);
							out.close();

						}
								
								}
								else {
									
									session.setAttribute("errmsg", " Enter Valid ORDER TYPE! ");
									response.sendRedirect(
											"jsp/" + posPageToDirect + "?TRANID=" + tranID + "&CHKBATCH=" + batchChecked);
										
									}
							}
							else {
								
								session.setAttribute("errmsg", " Enter Valid Shipping Customer! ");
								response.sendRedirect(
										"jsp/" + posPageToDirect + "?TRANID=" + tranID + "&CHKBATCH=" + batchChecked);
									
								}
						}
						else {
							session.setAttribute("errmsg", " Enter Valid Customer! ");
							response.sendRedirect(
									"jsp/" + posPageToDirect + "?TRANID=" + tranID + "&CHKBATCH=" + batchChecked);
							}

					} catch (Exception e) {
						session.setAttribute("errmsg", e.getMessage());
						response.sendRedirect(
								"jsp/" + posPageToDirect + "?TRANID=" + tranID + "&CHKBATCH=" + batchChecked);
						
					}
				} else {
					
					viewTaxInvoiceReport(request, response, tranID);
				}
			}else if (action.equalsIgnoreCase("ADD") && TRANTYPE.equalsIgnoreCase("MOVEWITHBATCH")) {

				String expireDate = "", refno = "";
				refno = request.getParameter("REFERENCENO");
				String cmd="ADD";
				String empID = StrUtils.fString(request.getParameter("EMP_ID")).trim();
				String refNo = StrUtils.fString(request.getParameter("REFERENCENO")).trim();

				String serialized = StrUtils.fString(request.getParameter("serialized")).trim();
				String defualtQty = StrUtils.fString(request.getParameter("defaultQty")).trim();
				String bulkcheckout = StrUtils.fString(request.getParameter("bulkcheckout")).trim();
				String tranDate = StrUtils.fString(request.getParameter("TRANSACTIONDATE")).trim();
				String holdReasonCode = StrUtils.fString(request.getParameter("REASONCODE")).trim();
				String holdRemarks = StrUtils.fString(request.getParameter("REMARKS")).trim();
				String uom = StrUtils.fString(request.getParameter("UOM")).trim();
				String empName = StrUtils.fString(request.getParameter("EMP_NAME")).trim();

				loc = StrUtils.fString(request.getParameter("FROM_LOC")).trim();
				String fromlocdesc = StrUtils.fString(request.getParameter("FROM_LOCDESC")).trim();

				String to_LOC = StrUtils.fString(request.getParameter("TOLOC")).trim();
				String tolocdesc = StrUtils.fString(request.getParameter("TOLOCDESC")).trim();
				
				Hashtable<String, String> htTrandcnd = new Hashtable<String, String>();
				htTrandcnd.put("POSTRANID", tranID);
				boolean istranidExists = _posServlet.isExisit(PLANT, htTrandcnd);
				if(istranidExists) {
					htTrandcnd.clear();
					htTrandcnd.put(IDBConstants.PLANT, PLANT);
					htTrandcnd.put(IDBConstants.POS_TRANID, tranID);
					POSHdrDAO poshdrdao = new POSHdrDAO();
					poshdrdao.setmLogger(mLogger);					
					boolean flag = poshdrdao.deletePosHdr(htTrandcnd);
					posdet.deletePosTranId(PLANT, tranID);	
					buylist = null;
//					posdet.deleteProductForTranId(PLANT, item, tranID, batch);
				}
				
				if (loc.length() > 0) {

					UserLocUtil uslocUtil = new UserLocUtil();
					uslocUtil.setmLogger(mLogger);
					boolean isvalidlocforUser = uslocUtil.isValidLocInLocmstForUser(PLANT, login_user, loc);

					if (isvalidlocforUser) {
						isvalidlocforUser = uslocUtil.isValidLocInLocmstForUser(PLANT, login_user, to_LOC);
						if (!isvalidlocforUser) {

							session.setAttribute("errmsg",
									"To LOC : " + to_LOC + " is not a User Assigned Location/Valid Location ");
							response.sendRedirect("jsp/" + posPageToDirect + "?FROM_LOC=" + loc + "&TO_LOC=" + to_LOC
									+ "&serialized=" + serialized + "&defualtQty=" + defualtQty + "&bulkcheckout="
									+ bulkcheckout);

						} else {

							for (int z = 0; z < mitem.length; z++) {
								  item = mitem[z];    
								  batch = mbatch[z]; 
								  String qty = mscanqty[z]; 
								  scqty = Float.parseFloat(qty); 
//								  if(istranidExists) {
//									  posdet.deleteProductForTranId(PLANT, "'"+item+"'", tranID, "'"+batch+"'");
//									  buylist = null;
//								  }
								  
								boolean itemFound = true;
								ItemMstDAO itM = new ItemMstDAO();
								itM.setmLogger(mLogger);
								String scannedItemNo = itM.getItemIdFromAlternate(PLANT, item);
								if (scannedItemNo == null) {
									itemFound = false;
								} else {
									item = scannedItemNo;
								}
								if (itemFound) {

									ITEMMST items = getItemDetails(request, item, scqty, batch);
									items.setBATCHID(batchId);
									ITEMMST prodbean1 = items;
									nonstockflag = prodbean1.getNONSTOCKFLAG();
									items.setEmpNo(empID);
									items.setEmpName(empName);
									items.setRefNo(refNo);
									items.setTranDate(tranDate);
									items.setReasonCode(reasoncode);
									items.setRemarks(remarks);
									items.setLocDesc(fromlocdesc);
									items.setToLocDesc(tolocdesc);
									items.setSTKUOM(uom);
									Hashtable ht1 = new Hashtable();
									ht1.put(IConstants.PLANT, PLANT);
									ht1.put(IConstants.LOGIN_USER, (String) session.getAttribute("LOGIN_USER"));
									ht1.put(IConstants.LOC, loc);
									ht1.put(IConstants.ITEM, prodbean1.getITEM());
									ht1.put(IConstants.BATCH, prodbean1.getBATCH());
									if (prodbean1.getBATCHID() != -1) {
										ht1.put(IConstants.BATCH_ID, String.valueOf(prodbean1.getBATCHID()));
									}
									String stkqty1 = String.valueOf(prodbean1.getStkqty());
									//stkqty1 = StrUtils.TrunkateDecimalForImportData(stkqty1);
									String UOMQTY="1";
									Hashtable<String, String> htTrand1 = new Hashtable<String, String>();
									MovHisDAO movHisDao1 = new MovHisDAO();
									ArrayList getuomqty = movHisDao1.selectForReport("select ISNULL(QPUOM,1) as UOMQTY from "+PLANT+"_UOM where UOM='"+uom+"'",htTrand1);
									if(getuomqty.size()>0)
									{
									Map mapval = (Map) getuomqty.get(0);
									UOMQTY=(String)mapval.get("UOMQTY");
									}
									double invumqty = Double.valueOf(stkqty1) * Double.valueOf(UOMQTY);
									ht1.put(IConstants.QTY, String.valueOf(invumqty));									
									nonstockflag = prodbean1.getNONSTOCKFLAG();
									if (!nonstockflag.equalsIgnoreCase("Y")) {
										boolean invAvailable = false;
										try {
											invAvailable = CheckInvMstForGoddsIssue(ht1, request, response);

										} catch (Exception e) {
											InvFlag = true;
											session.setAttribute("errmsg", "Not Enough Inventory For Product:"
													+ prodbean1.getITEM() + "  with batch:" + prodbean1.getBATCH());
											String msg = session.getAttribute("errmsg").toString();
											session.setAttribute("poslist", buylist);
											e.printStackTrace(new PrintWriter(msg));
											throw e;
										}

										if (!invAvailable) {
											session.setAttribute("errmsg", "Not Enough Inventory For Product:"
													+ prodbean1.getITEM() + "  with batch:" + prodbean1.getBATCH());
											String msg = session.getAttribute("errmsg").toString();
											session.setAttribute("poslist", buylist);
											response.sendRedirect("jsp/" + posPageToDirect + "?TRANID=" + tranID
													+ "&CHKBATCH=" + batchChecked + "&FROM_LOC=" + loc
													+ "&FROM_LOCDESC=" + fromlocdesc + "&TOLOCDESC=" + tolocdesc
													+ "&TO_LOC=" + to_LOC + "&serialized=" + serialized + "&defualtQty="
													+ defualtQty + "&bulkcheckout=" + bulkcheckout + "&BATCH=NOBATCH"+"&cmd=" + cmd);

											return;
										}

									}

									if (buylist == null) {
										match = false;
										buylist = new Vector<ITEMMST>();
										buylist.addElement(items);
										// To check transaction already exists in POSHeader
										Hashtable<String, String> htTrandId = new Hashtable<String, String>();
										htTrandId.put("POSTRANID", tranID);
										boolean istranidExist = _posServlet.isExisit(PLANT, htTrandId);
										boolean istranidExist1 = _posServlet.isRecvtranIDExisit(PLANT, htTrandId);
										if (istranidExist1 == true) {
											try {
												throw new Exception("Tran Id : " + tranID + " Exists already! ");
											} catch (Exception e) {
												session.setAttribute("errmsg",
														"Tran Id : " + tranID + " Exists already! ");
												String msg = session.getAttribute("errmsg").toString();
												e.printStackTrace(new PrintWriter(msg));
												throw e;
											}
										}
										if (istranidExist == false) {
											String genPosTranid = poshdr.genPosManualTranId(PLANT, login_user, loc,
													tranID);
										}
										// To check transaction already exists in POSHeader end
										Hashtable<String, String> ht = new Hashtable<String, String>();
										ht.put(IDBConstants.PLANT, PLANT);
										ht.put(IDBConstants.POS_TRANID, tranID);
										String QryUpdate = " SET STATUS ='O',UPAT ='" + DateUtils.getDate()
												+ "',EMPNO='" + empID + "',REFNO='" + refNo + "',TRANDT='" + tranDate
												+ "',RSNCODE='" + holdReasonCode + "',REMARKS='" + holdRemarks
												+ "',LOC='" + loc + "',TOLOC='" + to_LOC + "'";
										boolean posHsrUpt = posHdr.update(QryUpdate, ht, "");
										if (posHsrUpt) {

											ht.put(IDBConstants.POS_ITEM, items.getITEM());
											ht.put(IDBConstants.POS_ITEMDESC,
													strUtils.InsertQuotes(items.getITEMDESC()));
											ht.put(IDBConstants.POS_QTY, String.valueOf(items.getStkqty()));
											ht.put(IDBConstants.POS_LOC, loc);
											ht.put(IDBConstants.POS_TOLOC, to_LOC);
											ht.put(IDBConstants.POSDET_STATUS, "N");
											ht.put(IDBConstants.POS_UNITPRICE, "0");
											ht.put(IDBConstants.POS_TOTALPRICE, "0");
											ht.put(IDBConstants.POS_TRANDT, DateUtils.getDate());
											ht.put(IDBConstants.POS_TRANTM, DateUtils.Time());
											ht.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
											ht.put(IDBConstants.CREATED_BY, login_user);
											ht.put(IDBConstants.POS_BATCH, batch);
											ht.put("POSTYPE", TRANTYPE);
											ht.put(IConstants.UNITMO,uom);
											ht.put(IConstants.OUT_DOLNNO, Integer.toString(1));
											
											try {
												//Check POS_DET Data Already Exists - Azees 4.8.19
												Hashtable<String, String> htup = new Hashtable<String, String>();
												htup.put(IDBConstants.PLANT, PLANT);
												htup.put(IDBConstants.POS_TRANID, tranID);
												htup.put(IDBConstants.POS_ITEM, items.getITEM());
												htup.put(IDBConstants.POS_BATCH, batch);											
												htup.put(IDBConstants.LOC, loc);
												htup.put(IConstants.UNITMO,uom);																				
												boolean chkpos = posdet.isExisit(htup,"");
												if(chkpos==false)
												{
												posdet.insertIntoPosDet(ht);
												}
												if (!istranidExist) {
													new TblControlUtil().updateTblControlSeqNoWithoutorder(PLANT,
															"STOCKMOVE", "SM");
												}

											} catch (Exception e) {
												session.setAttribute("errmsg",
														"Tran Id : " + tranID + " Exists already! ");
												String msg = session.getAttribute("errmsg").toString();
												e.printStackTrace(new PrintWriter(msg));
												throw e;
											}
										}

									} else {
										//Azees Udated 16.11.18
										int matchnew=0; 
										Boolean flag=true;
										
										for (int i = 0; i < buylist.size(); i++) {

											ITEMMST itembean = buylist.elementAt(i);

											if (itembean.getITEM().equalsIgnoreCase(items.getITEM())
													&& itembean.getBATCH().equalsIgnoreCase(items.getBATCH()) && itembean.getSTKUOM().equalsIgnoreCase(items.getSTKUOM())) {
												if (serialized.equalsIgnoreCase("1")) {
													session.setAttribute("errmsg",
															"Duplicates are not allowed when serialized");
													match = true;
												} else {
													/*if (scqty > 1) {
														itembean.setStkqty((itembean.getStkqty() + scqty));
														scqty = itembean.getStkqty();
													} else {
														itembean.setStkqty((itembean.getStkqty() + 1));
														scqty = itembean.getStkqty();
													}

													buylist.setElementAt(itembean, i);
													match = true;
													Hashtable<String, String> ht = new Hashtable<String, String>();
													ht.put(IDBConstants.PLANT, PLANT);
													ht.put(IDBConstants.POS_TRANID, tranID);
													ht.put(IDBConstants.POS_ITEM, items.getITEM());
													ht.put(IDBConstants.POS_BATCH, items.getBATCH());
													String Qry = " SET QTY ='" + itembean.getStkqty() + "'";
													posdet.update(Qry, ht, "");
													// update Qty for Same Item
													*/	
													match = true;
													if(itembean.getBATCHID() == items.getBATCHID())
													{
														float scqty1=  itembean.getStkqty() + scqty;
														double invuomqty = Double.valueOf(scqty1) * Double.valueOf(UOMQTY);
															
														
														
														ht1.put(IConstants.ITEM, items.getITEM());
														ht1.put(IConstants.BATCH, items.getBATCH());
														if (prodbean1.getBATCHID() != -1) {
															ht1.put(IConstants.BATCH_ID, String.valueOf(items.getBATCHID()));
														}
														
														
														ht1.put(IConstants.QTY,  String.valueOf(invuomqty));
														
														flag = CheckInvMstForGoddsIssue(ht1, request, response);
														if (flag == false) {
															
																	errmsg = "Not Enough Inventory";
																	break;
																}
														else
														{
															if (scqty > 1) {
															    itembean.setStkqty((itembean.getStkqty() + scqty));
															    
															} else {
																itembean.setStkqty((itembean.getStkqty() + 1));	
																
															}
															
															buylist.setElementAt(itembean, i);
														}
													     
													}
													else
													{
														Boolean found = false;
														 for(int j=0;j<buylist.size();j++)
														 {
															 ITEMMST tester=buylist.elementAt(j);
													           if(tester.getBATCHID()==items.getBATCHID()){
													        	   
													        	   found=true;
													                break;
													            }
														 }
														 if(!found)
														 {
															if(matchnew==0)
															{
																matchnew=1;
															}
														 }
													}
													
													}
													
												
											}
										}
										if(!flag)
										{
											session.setAttribute("errmsg", "Not Enough Inventory For Product:"
													+ prodbean1.getITEM() + "  with batch:" + prodbean1.getBATCH());
											String msg = session.getAttribute("errmsg").toString();
											session.setAttribute("poslist", buylist);
											
											response.sendRedirect("jsp/" + posPageToDirect + "?TRANID=" + tranID
													+ "&CHKBATCH=" + batchChecked + "&LOC=" + loc);
											return;
										
									}
									if(matchnew==1)
									{
										buylist.addElement(items);
									}
									for (int i = 0; i < buylist.size(); i++) {

										ITEMMST itembean = buylist.elementAt(i);
										if (itembean.getITEM().equalsIgnoreCase(items.getITEM())
												&& itembean.getBATCH().equalsIgnoreCase(items.getBATCH()) && itembean.getSTKUOM().equalsIgnoreCase(items.getSTKUOM())) {
										match = true;
										Hashtable<String, String> ht = new Hashtable<String, String>();
										ht.put(IDBConstants.PLANT, PLANT);
										ht.put(IDBConstants.POS_TRANID, tranID);
										ht.put(IDBConstants.POS_ITEM, items.getITEM());
										ht.put(IDBConstants.POS_BATCH, items.getBATCH());
										String Qry="";
										if(i == 0)
										{
										Qry = " SET QTY ='0'";
										posdet.update(Qry, ht, "");
												}
												Qry = " SET QTY =QTY+'" + itembean.getStkqty() + "'";
										posdet.update(Qry, ht, "");
										
									}
											}
										
										
										   if (!match) {
											buylist.addElement(items);
											// Insert into posdet with status as N
											Hashtable<String, String> ht = new Hashtable<String, String>();
											ht.put(IDBConstants.PLANT, PLANT);
											ht.put(IDBConstants.POS_TRANID, tranID);
											ht.put(IDBConstants.POS_ITEM, items.getITEM());
											ht.put(IDBConstants.POS_ITEMDESC,
													strUtils.InsertQuotes(items.getITEMDESC()));
											ht.put(IDBConstants.POS_QTY, String.valueOf(items.getStkqty()));
											ht.put(IDBConstants.POS_LOC, loc);
											ht.put(IDBConstants.POS_DISCOUNT, "0");
											ht.put(IDBConstants.POSDET_STATUS, "N");
											ht.put(IDBConstants.POS_UNITPRICE, "0");
											ht.put(IDBConstants.POS_TOTALPRICE, "0");
											ht.put(IDBConstants.POS_TRANDT, DateUtils.getDate());
											ht.put(IDBConstants.POS_TRANTM, DateUtils.Time());
											ht.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
											ht.put(IDBConstants.CREATED_BY, login_user);
											ht.put(IDBConstants.POS_BATCH, batch);
											// ht.put(IDBConstants.REFNO,refno);
											ht.put("POSTYPE", TRANTYPE);
											ht.put(IConstants.UNITMO,uom);
											ht.put(IConstants.OUT_DOLNNO, Integer.toString(buylist.size()));
											try {

												Hashtable<String, String> htTrandId = new Hashtable<String, String>();
												htTrandId.put("POSTRANID", tranID);
												boolean istranidExist = _posServlet.isExisit(PLANT, htTrandId);
												boolean istranidExist1 = _posServlet.isRecvtranIDExisit(PLANT,
														htTrandId);
												if (istranidExist1 == true) {

													throw new Exception();
												}
												posdet.insertIntoPosDet(ht);
												if (!istranidExist) {
													new TblControlUtil().updateTblControlSeqNoWithoutorder(PLANT,
															"STOCKMOVE", "SM");
												}

											} catch (Exception e) {
												getlist.remove(items);
												session.setAttribute("errmsg",
														"Tran Id : " + tranID + " Exists already! ");
												String msg = session.getAttribute("errmsg").toString();
												e.printStackTrace(new PrintWriter(msg));
												throw e;
											}
										}
									}
									session.setAttribute("poslist", buylist);
									session.setAttribute("LOCATION", loc);
									if (!posType.equalsIgnoreCase("NO_INV_CHECK")) {
										boolean checkarry[] = new boolean[buylist.size()];
										int index = 0;
										Vector newbuylst = (Vector) session.getAttribute("poslist");
										// Check qty exists in inventory
										for (int i = 0; i < newbuylst.size(); i++) {
											Hashtable ht = new Hashtable();
											ITEMMST prodbean = (ITEMMST) newbuylst.elementAt(i);

											ht.put(IConstants.PLANT, PLANT);
											ht.put(IConstants.LOGIN_USER, (String) session.getAttribute("LOGIN_USER"));
											ht.put(IConstants.LOC, loc);
											ht.put(IConstants.ITEM, prodbean.getITEM());
											ht.put(IConstants.BATCH, prodbean.getBATCH());
											String stkqty = String.valueOf(prodbean.getStkqty());
											//stkqty = StrUtils.TrunkateDecimalForImportData(stkqty);
											ht.put(IConstants.QTY, stkqty);
											nonstockflag = prodbean.getNONSTOCKFLAG();
											if (!nonstockflag.equalsIgnoreCase("Y")) {
												checkarry[index] = CheckInvMst(ht, request, response);
												try {
													checkarry[index] = CheckInvMst(ht, request, response);

												} catch (Exception e) {
													InvFlag = true;
													session.setAttribute("errmsg",
															"Not Enough Inventory For Product:" + prodbean.getITEM()
																	+ "  with batch:" + prodbean.getBATCH());
													String msg = session.getAttribute("errmsg").toString();
													session.setAttribute("poslist", buylist);
													e.printStackTrace(new PrintWriter(msg));
													throw e;
												}

											}

											if (checkarry[index] == true) {
												response.sendRedirect("jsp/" + posPageToDirect + "?TRANID=" + tranID
														+ "&CHKBATCH=" + batchChecked + "&LOC=" + loc + "&TOLOC="
														+ to_LOC + "&serialized=" + serialized + "&EMP_ID=" + empID
														+ "&REFERENCENO=" + refNo + "&serialized=" + serialized
														+ "&defualtQty=" + defualtQty + "&bulkcheckout=" + bulkcheckout
														+ "&TRANSACTIONDATE=" + tranDate + "&REASONCODE="
														+ holdReasonCode + "&REMARKS=" + holdRemarks + "&FROM_LOCDESC="
														+ fromlocdesc + "&TOLOCDESC=" + tolocdesc + "&EMP_NAME="
														+ empName + "&BATCH=NOBATCH"+"&cmd=" + cmd);
											}

											index++;
										}
									}
								} else {
									session.setAttribute("errmsg", "Product Id not found");
								}

							
						}
							posPageToDirect = "../inhouse/stockmove";
//							String msg= "'"+tranID+"' Stock Move Created But Products Not transfered to "+to_LOC+"";
							String msg= "'"+tranID+"' Stock Move Created And Products Hold Sucessfully.";
							response.sendRedirect("jsp/" + posPageToDirect + "?msg=" + msg);
//							response.sendRedirect("jsp/" + posPageToDirect + "?TRANID=" + tranID + "&CHKBATCH="
//									+ batchChecked + "&FROM_LOC=" + loc + "&TO_LOC=" + to_LOC + "&serialized="
//									+ serialized + "&EMP_ID=" + empID + "&REFERENCENO=" + refNo + "&serialized="
//									+ serialized + "&defualtQty=" + defualtQty + "&bulkcheckout=" + bulkcheckout
//									+ "&TRANSACTIONDATE=" + tranDate + "&REASONCODE=" + holdReasonCode + "&REMARKS="
//									+ holdRemarks + "&FROM_LOCDESC=" + fromlocdesc + "&TOLOCDESC=" + tolocdesc
//									+ "&EMP_NAME=" + empName + "&BATCH=NOBATCH"+"&cmd=" + cmd);

						}
					} else {
						session.setAttribute("errmsg",
								"From LOC : " + loc + " is not a User Assigned Location/Valid Location ");
						response.sendRedirect("jsp/" + posPageToDirect + "?TRANID=" + tranID + "&CHKBATCH="
								+ batchChecked + "&FROM_LOC=" + loc + "&TO_LOC=" + to_LOC + "&serialized=" + serialized
								+ "&EMP_ID=" + empID + "&EMP_NAME=" + empName + "&REFERENCENO=" + refNo + "&serialized="
								+ serialized + "&defualtQty=" + defualtQty + "&bulkcheckout=" + bulkcheckout
								+ "&TRANSACTIONDATE=" + tranDate + "&REASONCODE=" + holdReasonCode + "&REMARKS="
								+ holdRemarks + "&FROM_LOCDESC=" + fromlocdesc + "&TOLOCDESC=" + tolocdesc
								+ "&BATCH=NOBATCH"+"&cmd=" + cmd);

					}
				} else {
					session.setAttribute("errmsg", "Please Enter Location");
					// response.sendRedirect("jsp/"+posPageToDirect+"&LOC="+loc);
					// RequestDispatcher view =
					// request.getRequestDispatcher("jsp/"+posPageToDirect+"?FROM_LOC="+loc+"&TO_LOC="+to_LOC);
					// view.forward(request, response);
					response.sendRedirect("jsp/" + posPageToDirect + "?TRANID=" + tranID + "&CHKBATCH=" + batchChecked
							+ "&FROM_LOC=" + loc + "&TO_LOC=" + to_LOC + "&serialized=" + serialized + "&EMP_ID="
							+ empID + "&EMP_NAME=" + empName + "&REFERENCENO=" + refNo + "&serialized=" + serialized
							+ "&defualtQty=" + defualtQty + "&bulkcheckout=" + bulkcheckout + "&TRANSACTIONDATE="
							+ tranDate + "&REASONCODE=" + holdReasonCode + "&REMARKS=" + holdRemarks + "&EMP_NAME="
							+ empName + "&FROM_LOCDESC=" + fromlocdesc + "&TOLOCDESC=" + tolocdesc + "&BATCH=NOBATCH"+"&cmd=" + cmd);
				}

			}if (cmdAction.equalsIgnoreCase("printstockmoveproduct")) {
				boolean ajax = false;

				String loginUSER = StrUtils.fString(request.getParameter("LOGIN_USER"));

				String from_LOC = StrUtils.fString(request.getParameter("FROM_LOC")).trim();
				String to_LOC = StrUtils.fString(request.getParameter("TOLOC")).trim();
				remarks = StrUtils.fString(request.getParameter("REMARKS")).trim();
				ajax = "XMLHttpRequest".equals(request.getHeader("X-Requested-With"));

				if (ajax) {

					try {

						String TRAN_TYPE = StrUtils.fString(request.getParameter("TRANTYPE")).trim();

						if (TRAN_TYPE.equalsIgnoreCase("GOODSISSUEWITHOUTBATCH")) {
							posPageToDirect = "MultiMiscOrderIssuing.jsp";
						} else if (TRAN_TYPE.equalsIgnoreCase("GOODSISSUEWITHBATCH")) {
							posPageToDirect = "../salestransaction/goodsissue";
						} else if (TRAN_TYPE.equalsIgnoreCase("GOODSISSUEWITHBATCHPRICE")) {
							posPageToDirect = "dynamicprdswithprice.jsp";
						} else if (TRAN_TYPE.equalsIgnoreCase("MOVEWITHBATCH")) {
//							posPageToDirect = "MultiLocationTransferWithBatch.jsp";
							//posPageToDirect = "../inhouse/stockmove";
							posPageToDirect = "../inhouse/new";
						} else if (TRANTYPE.equalsIgnoreCase("MOVEWITHOUTBATCH")) {
							posPageToDirect = "MultiLocationTransfer.jsp";
						}
						String empID = StrUtils.fString(request.getParameter("EMP_ID")).trim();
						String refNo = StrUtils.fString(request.getParameter("REFERENCENO")).trim();
						String tranDate = StrUtils.fString(request.getParameter("TRANSACTIONDATE")).trim();
						String rsncode = StrUtils.fString(request.getParameter("REASONCODE")).trim();
						UserTransaction ut = null;
						ut = com.track.gates.DbBean.getUserTranaction();
						ut.begin();

						SalesDetailDAO salesdao = new SalesDetailDAO();
						POSPaymentDetailDAO paymentDAO = new POSPaymentDetailDAO();
						String rcpNo = "";
						Generator gen = new Generator();
						DateUtils dateUtils = new DateUtils();

						// Generate Receipt no
						// rcpNo = this.genReceipt(request);
						boolean flag = false;
						boolean invflag = false;
						// To check transaction already exists in POSHeader
						Hashtable<String, String> htTrandId = new Hashtable<String, String>();
						htTrandId.put("POSTRANID", tranID);
						boolean istranidExist = _posServlet.isExisit(PLANT, htTrandId);
						if (istranidExist == false) {
							String genPosTranid = poshdr.genPosManualTranId(PLANT, login_user, loc, tranID);
						}
						// To check transaction already exists in POSHeader end
						String xmlutil = "";
						HashMap<String, String> htrecv = new HashMap<String, String>();
						String paymentmodes = "";
						MiscIssuingUtil miscutil = new MiscIssuingUtil();
						Hashtable<String, String> htm = new Hashtable<String, String>();
						String formatdt = DateUtils.getDate();
						htm.put("PURCHASEDATE", DateUtils.getDateinyyyy_mm_dd(formatdt));
						htm.put("PURCHASETIME", DateUtils.Time());
						htm.put(IConstants.REMARKS, remarks);
						htm.put(IConstants.REASONCODE, reasoncode);
						htm.put("EMPNAME", employee);
						htrecv.put(IConstants.PLANT, PLANT);
						if (remarks.length() > 0) {
							htrecv.put(IConstants.REMARKS, "Stock Move," + remarks + " " + employee);
						} else {
							htrecv.put(IConstants.REMARKS, "Stock Move)  " + employee);
						}
						htrecv.put(IConstants.LOGIN_USER, (String) session.getAttribute("LOGIN_USER"));
						htrecv.put(IConstants.LOC, loc);
						miscutil.setmLogger(mLogger);
						salesdao.setmLogger(mLogger);

						String appenddisc = "";
						htm.put("PURCHASEDATE", DateUtils.getDateinyyyy_mm_dd(formatdt));
						htm.put("PURCHASETIME", DateUtils.Time());
						htm.put(IConstants.REMARKS, remarks);
						htm.put(IConstants.REASONCODE, reasoncode);
						htm.put("EMPNAME", employee);
						for (int j = 0; j < buylist.size(); j++) {
							ITEMMST product = buylist.get(j);
							htm.put(IConstants.PLANT, PLANT);
							htm.put(IConstants.ITEM, product.getITEM());
							htm.put(IConstants.ITEM_DESC, strUtils.InsertQuotes(product.getITEMDESC()));
							htm.put(IConstants.BATCH, product.getBATCH());
							htm.put(IConstants.QTY, String.valueOf(product.getStkqty()));
							htm.put(IConstants.UOM, String.valueOf(product.getSTKUOM()));
							htm.put(IDBConstants.PRICE, "0");
							htm.put(IDBConstants.PAYMENTMODE, paymentmodes);
							htm.put("TranId", tranID);
							htm.put("TranType", "MOVEWITHBATCH");
							htm.put(IDBConstants.LOC, from_LOC);
							htm.put(IConstants.TOLOC, to_LOC);
							htm.put(IDBConstants.POS_DISCOUNT, "0");
							htm.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
							htm.put(IDBConstants.CREATED_BY, login_user);
							htrecv.put(IConstants.ITEM, product.getITEM());
							htrecv.put(IConstants.QTY, String.valueOf(product.getStkqty()));
							htrecv.put(IConstants.BATCH, product.getBATCH());
							htrecv.put(IConstants.UOM, String.valueOf(product.getSTKUOM()));
							if (product.getBATCHID() != -1) {
								htrecv.put(IConstants.BATCH_ID, String.valueOf(product.getBATCHID()));
							}
							htrecv.put("TranId", tranID);
							htrecv.put("DIRTYPE", "MOVE");
							htrecv.put(IConstants.TRAN_TYPE, TRANTYPE);
							htrecv.put(IConstants.REFNO, refNo);
							htrecv.put(IConstants.CNAME, empID);
							htrecv.put(IConstants.REASONCODE, rsncode);
							htrecv.put(IConstants.EMPNO, empID);
							htrecv.put(IConstants.ISSUEDATE, tranDate);

							htrecv.put(IConstants.REMARKS, remarks);

							nonstockflag = product.getNONSTOCKFLAG();
							// Deducting from inventory
							// String tranid=(String)session.getAttribute("tranid");
							htrecv.put(IConstants.MOVHIS_ORDNUM, tranID);
							try {
								// if(!posType.equalsIgnoreCase("NO_INV_CHECK")&&!nonstockflag.equalsIgnoreCase("Y")){
								if (!posType.equalsIgnoreCase("NO_INV_CHECK") && !nonstockflag.equalsIgnoreCase("Y")) {
									// flag = miscutil.process_PosIssueMaterial(htrecv);

									Map locTran_HM = new HashMap();
									locTran_HM.put(IConstants.PLANT, PLANT);
									locTran_HM.put(IConstants.ITEM, product.getITEM());
									locTran_HM.put(IConstants.LOC, from_LOC);
									locTran_HM.put(IConstants.LOC2, to_LOC);
									locTran_HM.put(IConstants.LOGIN_USER, loginUSER);
									locTran_HM.put(IConstants.CUSTOMER_CODE, "");
									locTran_HM.put(IConstants.REMARKS, remarks);
									locTran_HM.put(IConstants.TRAN_DATE, tranDate);
									if (rsncode.equals("NOREASONCODE"))
									{
										locTran_HM.put(IConstants.RSNDESC, "");
									}
									else
									{
										locTran_HM.put(IConstants.RSNDESC, rsncode);	
									}
									//locTran_HM.put(IConstants.RSNDESC, rsncode);
									locTran_HM.put(IConstants.REASONCODE, "");
									locTran_HM.put(IConstants.EMPNO, empID);
									locTran_HM.put(IConstants.TR_TOLNNO, Integer.toString(j + 1));
									locTran_HM.put("TranId", tranID);
									locTran_HM.put(IConstants.REFNO, refNo);
									locTran_HM.put(IConstants.BATCH, product.getBATCH());
									locTran_HM.put(IConstants.ISPDA,"");
									if (product.getBATCHID() != -1) {
										locTran_HM.put(IConstants.BATCH_ID, String.valueOf(product.getBATCHID()));
									}
									locTran_HM.put(IConstants.QTY, String.valueOf(product.getStkqty()));
									locTran_HM.put(IConstants.ISSUEDATE, tranDate);
									String UOMQTY="1";
									Hashtable<String, String> htTrand1 = new Hashtable<String, String>();
									MovHisDAO movHisDao1 = new MovHisDAO();
									ArrayList getuomqty = movHisDao1.selectForReport("select ISNULL(QPUOM,1) as UOMQTY from "+PLANT+"_UOM where UOM='"+product.getSTKUOM()+"'",htTrand1);
									if(getuomqty.size()>0)
									{
									Map mapval = (Map) getuomqty.get(0);
									UOMQTY=(String)mapval.get("UOMQTY");
									}
									locTran_HM.put("UOMQTY", UOMQTY);
									locTran_HM.put(IConstants.UNITMO, product.getSTKUOM());
									locTran_HM.put("PDA","");
									InvMstUtil _InvMstUtil = new InvMstUtil();
									flag = _InvMstUtil.process_MultiLocationTransferforPC(locTran_HM);
									//flag = salesdao.insertIntoSalesDet(htm);
									processTOHDR(locTran_HM);
									
									//POSDET Update Line No.
									Hashtable<String, String> htup = new Hashtable<String, String>();
									htup.put(IDBConstants.PLANT, PLANT);
									htup.put(IDBConstants.POS_TRANID, tranID);
									htup.put(IDBConstants.POS_ITEM, product.getITEM());
									htup.put(IDBConstants.POS_BATCH, product.getBATCH());
									htup.put(IConstants.LOC, from_LOC);
									htup.put(IConstants.UNITMO, product.getSTKUOM());
									htup.put(IConstants.QTY, String.valueOf(product.getStkqty()));
									String Qry="SET DOLNNO ='" + (j+1) +"'";
									posdet.update(Qry, htup, "");
								
									if (flag == false)
										errmsg = "Not Enough Inventory";
									// System.out.print("After issuing"+flag);
								} else {

									flag = processMovHisForPosWoInvCheck(htrecv);

									// flag = processIssueHis(htrecv);
								}
								salesdao.setmLogger(mLogger);
								// if (flag == true)
								// flag = salesdao.insertIntoSalesDet(htm);
								// new TblControlUtil().updateTblControlSeqNo(PLANT,"GIRECEIPT","GI",tranID);

							} catch (Exception e) {
								flag = false;
								DbBean.RollbackTran(ut);
								throw e;

							}

						}

						System.out.println("flag1 : " + flag);
						if (flag == true)
							// update POSHDR AND PODET To Close Status
							buylist.removeAllElements();

						Hashtable<String, String> ht = new Hashtable<String, String>();
						ht.put(IDBConstants.PLANT, PLANT);
						ht.put(IDBConstants.POS_TRANID, tranID);
						String QryUpdate = " SET STATUS ='C',RECEIPTNO='" + tranID + "',UPAT ='" + DateUtils.getDate()
								+ "' ";
						boolean posHsrUpt = posHdr.update(QryUpdate, ht, "");
						if (posHsrUpt) {
							QryUpdate = " SET TRANSTATUS ='C',UPAT ='" + DateUtils.getDate() + "' ";
							posdet.update(QryUpdate, ht, "");
						}

						session.setAttribute("poslist", buylist);
						// session.setAttribute("poslist", buylist);
						DbBean.CommitTran(ut);
						System.out.println("flag : " + flag);
						if (flag == false) {
							DbBean.RollbackTran(ut);
							session.setAttribute("errmsg", errmsg);
							// response.sendRedirect("jsp/"+posPageToDirect+"?TRANID="+tranID+"&CHKBATCH="+batchChecked);
							RequestDispatcher view = request.getRequestDispatcher(
									"jsp/" + posPageToDirect + "?TRANID=" + tranID + "&CHKBATCH=" + batchChecked);
							view.forward(request, response);
						} else {
							// viewPOSReport(request, response, rcpNo);
							POSUtil posUtil = new POSUtil();
							Map mhdr = posUtil.getPosMoveHdrDetails(PLANT);
							String download = (String) mhdr.get("DOWNLOADPDF");
							session.setAttribute("errmsg", "");
							buylist.removeAllElements();
							session.setAttribute("poslist", buylist);
							// session.setAttribute("poslist", buylist);

							response.setContentType("text/html");
							PrintWriter out = response.getWriter();
							out.write("Success:" + tranID + ":" + download);
							out.close();

						}

					} catch (Exception e) {
						e.printStackTrace();
						session.setAttribute("errmsg", e.getMessage());
						// response.sendRedirect("jsp/"+posPageToDirect+"?TRANID="+tranID+"&CHKBATCH="+batchChecked);
						RequestDispatcher view = request.getRequestDispatcher(
								"jsp/" + posPageToDirect + "?TRANID=" + tranID + "&CHKBATCH=" + batchChecked);
						view.forward(request, response);
					}
				} else {
					// String recNo = request.getParameter("RCPNO");
					viewPOSMoveProductReport(request, response, tranID);
				}
			} else if (cmdAction.equalsIgnoreCase("ViewProduct")) {
				String cmd = "ViewProduct";

				Vector<ITEMMST> veclist = this.getHoldItemDetailsForPOSTranId(request, RecvtranID, getlist);
				session.setAttribute("recvlist", veclist);
				response.sendRedirect("jsp/" + posPageToDirect + "?RECVTRANID=" + RecvtranID + "&CHKBATCH="
						+ batchChecked + "&LOC=" + loc + "&cmd=" + cmd);
			} else if (cmdAction.equalsIgnoreCase("NEWID")) {
				com.track.dao.TblControlDAO _TblControlDAO = new com.track.dao.TblControlDAO();
				_TblControlDAO.setmLogger(mLogger);
				String genPosTranid = _TblControlDAO.getNextOrder(PLANT, login_user, "GRRECEIPT");
				if (getlist != null) {
					getlist.removeAllElements();
					session.setAttribute("recvlist", getlist);
					session.setAttribute("recvlist", null);
					session.setAttribute("errmsg", "");
				}
				session.setAttribute("LOCATION", loc);
				response.sendRedirect(
						"jsp/" + posPageToDirect + "?RECVTRANID=" + genPosTranid + "&LOC=" + loc + "&BATCH=NOBATCH");
				/*
				 * RequestDispatcher view =
				 * request.getRequestDispatcher("jsp/"+posPageToDirect+"?RECVTRANID="+
				 * genPosTranid+"&LOC="+loc); view.forward(request, response);
				 */

			} else if (action.equalsIgnoreCase("GETLOC")) {
				loc = StrUtils.fString(request.getParameter("LOC")).trim();
				UserLocUtil uslocUtil = new UserLocUtil();
				uslocUtil.setmLogger(mLogger);
				boolean isvalidlocforUser = uslocUtil.isValidLocInLocmstForUser(PLANT, login_user, loc);
				if (isvalidlocforUser) {
					LocUtil _LocUtil = new LocUtil();
					ArrayList arrCust = _LocUtil.getLocDetails(loc, PLANT, (String) session.getAttribute("LOGIN_USER"));
					String locDesc = "";
					for (int i = 0; i < arrCust.size(); i++) {
						Map arrCustLine = (Map) arrCust.get(i);
						locDesc = StrUtils.fString(strUtils.removeQuotes((String) arrCustLine.get("LOCDESC")));
					}
					RequestDispatcher view = request
							.getRequestDispatcher("jsp/" + posPageToDirect + "?LOC=" + loc + "&LOCDESC=" + locDesc);
					view.forward(request, response);

				} else {
					session.setAttribute("errmsg",
							"Location : " + loc + " is not a User Assigned Location/Valid Location ");
					/*RequestDispatcher view = request
							.getRequestDispatcher("jsp/" + posPageToDirect + "?LOC=" + loc + "&iserrorVal=True");
					view.forward(request, response);*/
					response.sendRedirect("jsp/" + posPageToDirect + "?LOC=" + loc + "&iserrorVal=True");
				}

			}

			else if (action.equalsIgnoreCase("FROMLOCCHECK")) {
				loc = StrUtils.fString(request.getParameter("FROM_LOC")).trim();
				UserLocUtil uslocUtil1 = new UserLocUtil();
				uslocUtil1.setmLogger(mLogger);
				boolean isvalidlocforUser1 = uslocUtil1.isValidLocInLocmstForUser(PLANT, login_user, loc);
				if (isvalidlocforUser1) {
					LocUtil _LocUtil = new LocUtil();
					ArrayList arrCust = _LocUtil.getLocDetails(loc, PLANT, (String) session.getAttribute("LOGIN_USER"));
					String locDesc = "";
					for (int i = 0; i < arrCust.size(); i++) {
						Map arrCustLine = (Map) arrCust.get(i);
						locDesc = StrUtils.fString(strUtils.removeQuotes((String) arrCustLine.get("LOCDESC")));
					}
					RequestDispatcher view = request.getRequestDispatcher(
							"jsp/" + posPageToDirect + "?FROM_LOC=" + loc + "&FROM_LOCDESC=" + locDesc);
					view.forward(request, response);

				} else {
					session.setAttribute("errmsg",
							"FROM Location : " + loc + " is not a User Assigned Location/Valid Location ");
					RequestDispatcher view = request
							.getRequestDispatcher("jsp/" + posPageToDirect + "?FROM_LOC=" + loc + "&iserrorVal=True");
					view.forward(request, response);
				}

			} else if (action.equalsIgnoreCase("TOLOCCHECK")) {
				loc = StrUtils.fString(request.getParameter("TOLOC")).trim();
				UserLocUtil uslocUtil2 = new UserLocUtil();
				uslocUtil2.setmLogger(mLogger);
				boolean isvalidlocforUser2 = uslocUtil2.isValidLocInLocmstForUser(PLANT, login_user, loc);
				if (isvalidlocforUser2) {
					LocUtil _LocUtil = new LocUtil();
					ArrayList arrCust = _LocUtil.getLocDetails(loc, PLANT, (String) session.getAttribute("LOGIN_USER"));
					String locDesc = "";
					for (int i = 0; i < arrCust.size(); i++) {
						Map arrCustLine = (Map) arrCust.get(i);
						locDesc = StrUtils.fString(strUtils.removeQuotes((String) arrCustLine.get("LOCDESC")));
					}
					RequestDispatcher view = request
							.getRequestDispatcher("jsp/" + posPageToDirect + "?TOLOC=" + loc + "&TOLOCDESC=" + locDesc);
					view.forward(request, response);

				} else {
					session.setAttribute("errmsg",
							"TO Location : " + loc + " is not a User Assigned Location/Valid Location ");
					RequestDispatcher view = request
							.getRequestDispatcher("jsp/" + posPageToDirect + "?TOLOC=" + loc + "&iserrorVal=True");
					view.forward(request, response);
				}

			} else if (cmdAction.equalsIgnoreCase("ADD") && TRANTYPE.equalsIgnoreCase("GOODSRECEIPTWITHBATCH")) {
				String cmd="ADD";
				String expireDate = "", refno = "";
				expireDate = StrUtils.fString(request.getParameter("EXPIREDATE")).trim();
				refno = request.getParameter("REFERENCENO");
				String empID = StrUtils.fString(request.getParameter("EMP_ID")).trim();
				String empName = request.getParameter("EMP_NAME");
				String serialized = StrUtils.fString(request.getParameter("serialized")).trim();
				String defualtQty = StrUtils.fString(request.getParameter("defualtQty")).trim();
				String expirydate = StrUtils.fString(request.getParameter("expirydate")).trim();
				String refNo = StrUtils.fString(request.getParameter("REFERENCENO")).trim();
				String tranDate = StrUtils.fString(request.getParameter("TRANSACTIONDATE")).trim();
				loc = StrUtils.fString(request.getParameter("LOC")).trim();
				String locdesc = StrUtils.fString(request.getParameter("LOCDESC")).trim();
				String holdReasonCode = StrUtils.fString(request.getParameter("REASONCODE")).trim();
				String holdRemarks = StrUtils.fString(request.getParameter("REMARKS")).trim();
				String uom = StrUtils.fString(request.getParameter("UOM")).trim();
				if (loc.length() > 0) {

					UserLocUtil uslocUtil = new UserLocUtil();
					uslocUtil.setmLogger(mLogger);
					boolean isvalidlocforUser = uslocUtil.isValidLocInLocmstForUser(PLANT, login_user, loc);
					if (isvalidlocforUser) {
						if (item.length() > 0) {
							boolean itemFound = true;
							ItemMstDAO itM = new ItemMstDAO();
							itM.setmLogger(mLogger);
							String scannedItemNo = itM.getItemIdFromAlternate(PLANT, item);
							if (scannedItemNo == null) {
								itemFound = false;
							} else {
								item = scannedItemNo;
							}
							if (itemFound) {
								ITEMMST recvitems = getPOSItemDetails(request, item, scqty, batch, expireDate);

								recvitems.setEmpNo(empID);
								recvitems.setRefNo(refNo);
								recvitems.setTranDate(tranDate);
								recvitems.setReasonCode(reasoncode);
								recvitems.setRemarks(remarks);
								recvitems.setLoc(loc);
								recvitems.setSTKUOM(uom);
								if (getlist == null) {
									match = false;
									getlist = new Vector<ITEMMST>();
									getlist.addElement(recvitems);
									// To check transaction already exists in POSHeader
									Hashtable<String, String> htTrandId = new Hashtable<String, String>();
									htTrandId.put("POSTRANID", RecvtranID);
									boolean istranidExist = _posServlet.isExisit(PLANT, htTrandId);

									boolean istranidExist1 = _posServlet.isRecvtranIDExisit(PLANT, htTrandId);
									if (istranidExist1 == true) {
										try {
											throw new Exception();
										} catch (Exception e) {
											session.setAttribute("errmsg",
													"Tran Id : " + RecvtranID + " Exists already! ");
											String msg = session.getAttribute("errmsg").toString();
											e.printStackTrace(new PrintWriter(msg));
											throw e;
										}
									}
									if (istranidExist == false) {

										String genPosTranid = poshdr.genPosManualTranId(PLANT, login_user, loc,
												RecvtranID);
									}

									// To check transaction already exists in POSHeader end
									Hashtable<String, String> ht = new Hashtable<String, String>();
									ht.put(IDBConstants.PLANT, PLANT);
									ht.put(IDBConstants.POS_TRANID, RecvtranID);
									String QryUpdate = " SET STATUS ='O',UPAT ='" + DateUtils.getDate() + "',EMPNO='"
											+ empID + "',REFNO='" + refNo + "',TRANDT='" + tranDate + "',RSNCODE='"
											+ holdReasonCode + "',REMARKS='" + holdRemarks + "',LOC='" + loc + "' ";
									boolean posHsrUpt = posHdr.update(QryUpdate, ht, "");
									if (posHsrUpt) {

										ht.put(IDBConstants.POS_ITEM, recvitems.getITEM());
										ht.put(IDBConstants.POS_ITEMDESC,
												strUtils.InsertQuotes(recvitems.getITEMDESC()));
										ht.put(IDBConstants.POS_QTY, String.valueOf(recvitems.getStkqty()));
										ht.put(IDBConstants.POS_LOC, loc);
										ht.put(IDBConstants.POSDET_STATUS, "N");
										ht.put(IDBConstants.POS_UNITPRICE, "0");
										ht.put(IDBConstants.POS_TOTALPRICE, "0");
										ht.put(IDBConstants.POS_TRANDT, DateUtils.getDate());
										ht.put(IDBConstants.POS_TRANTM, DateUtils.Time());
										ht.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
										ht.put(IDBConstants.CREATED_BY, login_user);
										ht.put(IDBConstants.POS_BATCH, batch);
										ht.put("POSTYPE", TRANTYPE);
										ht.put(IDBConstants.EXPIREDATE, expireDate);
										ht.put(IConstants.UNITMO,uom);
										ht.put(IConstants.OUT_DOLNNO, Integer.toString(1));
										// ht.put(IDBConstants.REFNO,refno);

										try {
											// htTrandId.put("STATUS","C");
											//Check POS_DET Data Already Exists
											Hashtable<String, String> htup = new Hashtable<String, String>();
											htup.put(IDBConstants.PLANT, PLANT);
											htup.put(IDBConstants.POS_TRANID, RecvtranID);
											
											boolean chkpos = posdet.isExisit(htup,"");
											if(chkpos) /* Check if any line items exists for TrainId before adding the first line item*/
											{
												posdet.deletePosTranId(PLANT, RecvtranID); /* Delete all the previous line items*/
											}											
											htup.put(IDBConstants.POS_ITEM, recvitems.getITEM());
											htup.put(IDBConstants.POS_BATCH, batch);											
											htup.put(IDBConstants.LOC, String.valueOf(recvitems.getLoc()));
											htup.put(IConstants.UNITMO,uom);											
											chkpos = posdet.isExisit(htup,"");
											if(chkpos==false)
											{
											posdet.insertIntoPosDet(ht); // pp
											}else {
												String Qry = " SET QTY ='" + recvitems.getStkqty() + "'";
												posdet.update(Qry, htup, "");
											}
											// Check with Bruhan-Ravindra
											//new TblControlUtil().updateTblControlSeqNoWithoutorder(PLANT, "GRRECEIPT",
												//	"GR");

											// (PLANT,"GRRECEIPT","GR");

										} catch (Exception e) {
											session.setAttribute("errmsg",
													"Tran Id : " + RecvtranID + " Exists already! ");
											String msg = session.getAttribute("errmsg").toString();
											e.printStackTrace(new PrintWriter(msg));
											throw e;
										}
									}

								} else {
									for (int i = 0; i < getlist.size(); i++) {

										ITEMMST recvitembean = getlist.elementAt(i);

										if (recvitembean.getITEM().equalsIgnoreCase(recvitems.getITEM())
												&& recvitembean.getBATCH().equalsIgnoreCase(recvitems.getBATCH()) && recvitembean.getSTKUOM().equalsIgnoreCase(recvitems.getSTKUOM())) {

											if (serialized.equalsIgnoreCase("1")) {
												match = true;
												session.setAttribute("errmsg",
														"Duplicates are not allowed when serialized");

											} else {
												if (scqty > 1) {
													recvitembean.setStkqty((recvitembean.getStkqty() + scqty));
													scqty = recvitembean.getStkqty();
												} else {
													recvitembean.setStkqty((recvitembean.getStkqty() + 1));
													scqty = recvitembean.getStkqty();
												}

												getlist.setElementAt(recvitembean, i);
												match = true;
												Hashtable<String, String> ht = new Hashtable<String, String>();
												ht.put(IDBConstants.PLANT, PLANT);
												ht.put(IDBConstants.POS_TRANID, RecvtranID);
												ht.put(IDBConstants.POS_ITEM, recvitems.getITEM());
												ht.put(IDBConstants.POS_BATCH, recvitems.getBATCH());
												String Qry = " SET QTY ='" + recvitembean.getStkqty() + "'";
												posdet.update(Qry, ht, "");
												// update Qty for Same Item

											}

										}
									}
									if (!match) {
										getlist.addElement(recvitems);
										// Insert into posdet with status as N
										Hashtable<String, String> ht = new Hashtable<String, String>();
										ht.put(IDBConstants.PLANT, PLANT);
										ht.put(IDBConstants.POS_TRANID, RecvtranID);
										ht.put(IDBConstants.POS_ITEM, recvitems.getITEM());
										ht.put(IDBConstants.POS_ITEMDESC,
												strUtils.InsertQuotes(recvitems.getITEMDESC()));
										ht.put(IDBConstants.POS_QTY, String.valueOf(recvitems.getStkqty()));
										ht.put(IDBConstants.POS_LOC, loc);
										ht.put(IDBConstants.POS_DISCOUNT, "0");
										ht.put(IDBConstants.POSDET_STATUS, "N");
										ht.put(IDBConstants.POS_UNITPRICE, "0");
										ht.put(IDBConstants.POS_TOTALPRICE, "0");
										ht.put(IDBConstants.POS_TRANDT, DateUtils.getDate());
										ht.put(IDBConstants.POS_TRANTM, DateUtils.Time());
										ht.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
										ht.put(IDBConstants.CREATED_BY, login_user);
										ht.put(IDBConstants.POS_BATCH, batch);
										ht.put("POSTYPE", TRANTYPE);
										ht.put("SUBMITSTATUS", "N");
										ht.put(IDBConstants.EXPIREDATE, expireDate);
										ht.put(IConstants.UNITMO,uom);
										ht.put(IConstants.OUT_DOLNNO, Integer.toString(getlist.size()));
										// ht.put(IDBConstants.REFNO,refno);
										Hashtable<String, String> htTrandId = new Hashtable<String, String>();
										htTrandId.put("POSTRANID", RecvtranID);

										try {
											// htTrandId.put("STATUS","C");
											boolean istranidExist1 = _posServlet.isRecvtranIDExisit(PLANT, htTrandId);
											if (istranidExist1 == true) {

												throw new Exception();
											}
											posdet.insertIntoPosDet(ht); // pp
											// new
											// TblControlUtil().updateTblControlSeqNoWithoutorder(PLANT,"GRRECEIPT","GR",RecvtranID);
											//new TblControlUtil().updateTblControlSeqNoWithoutorder(PLANT, "GRRECEIPT",
												//	"GR");

										} catch (Exception e) {
											getlist.remove(recvitems);
											session.setAttribute("errmsg",
													"Tran Id : " + RecvtranID + " Exists already! ");
											String msg = session.getAttribute("errmsg").toString();
											e.printStackTrace(new PrintWriter(msg));
											throw e;
										}
									}
								}

								session.setAttribute("recvlist", getlist);
								session.setAttribute("LOCATION", loc);

							} else {
								session.setAttribute("errmsg", "Product Id not found");
							}

						}
						response.sendRedirect("jsp/" + posPageToDirect + "?RECVTRANID=" + RecvtranID + "&CHKBATCH="
								+ batchChecked + "&LOC=" + loc + "&serialized=" + serialized + "&defualtQty="
								+ defualtQty + "&expirydate=" + expirydate + "&EMP_ID=" + empID + "&REFERENCENO="
								+ refNo + "&TRANSACTIONDATE=" + tranDate + "&REASONCODE=" + holdReasonCode + "&REMARKS="
								+ holdRemarks + "&EMP_NAME=" + empName + "&LOCDESC=" + locdesc + "&BATCH=NOBATCH"+"&cmd=" + cmd);

					} else {
						session.setAttribute("errmsg",
								"Location : " + loc + " is not a User Assigned Location/Valid Location ");
						response.sendRedirect("jsp/" + posPageToDirect + ".jsp&LOC=" + loc + "&serialized=" + serialized
								+ "&defualtQty=" + defualtQty + "&expirydate=" + expirydate + "&EMP_ID=" + empID
								+ "&REFERENCENO=" + refNo + "&TRANSACTIONDATE=" + tranDate + "&REASONCODE="
								+ holdReasonCode + "&REMARKS=" + holdRemarks + "&EMP_NAME=" + empName + "&LOCDESC="
								+ locdesc + "&BATCH=NOBATCH"+"&cmd=" + cmd);

					}
				} else {
					session.setAttribute("errmsg", "Please Enter Location");
					response.sendRedirect("jsp/" + posPageToDirect + "&LOC=" + loc + "&serialized=" + serialized
							+ "&defualtQty=" + defualtQty + "&expirydate=" + expirydate + "&EMP_ID=" + empID
							+ "&REFERENCENO=" + refNo + "&TRANSACTIONDATE=" + tranDate + "&REASONCODE=" + holdReasonCode
							+ "&REMARKS=" + holdRemarks + "&EMP_NAME=" + empName + "&LOCDESC=" + locdesc
							+ "&BATCH=NOBATCH");
				}

			}  else if (cmdAction.equalsIgnoreCase("Delete") && TRANTYPE.equalsIgnoreCase("GOODSRECEIPTWITHBATCH")) {

				String expireDate = "", refno = "";
				String cmd="Delete";
				expireDate = request.getParameter("EXPIREDATE");
				refno = request.getParameter("REFERENCENO");
				String empID = StrUtils.fString(request.getParameter("EMP_ID")).trim();
				String empName = StrUtils.fString(request.getParameter("EMP_NAME")).trim();
				String serialized = StrUtils.fString(request.getParameter("serialized")).trim();
				String refNo = StrUtils.fString(request.getParameter("REFERENCENO")).trim();
				String tranDate = StrUtils.fString(request.getParameter("TRANSACTIONDATE")).trim();
				loc = StrUtils.fString(request.getParameter("LOC")).trim();
				String locdesc = StrUtils.fString(request.getParameter("LOCDESC")).trim();
				String holdReasonCode = StrUtils.fString(request.getParameter("REASONCODE")).trim();
				String holdRemarks = StrUtils.fString(request.getParameter("REMARKS")).trim();
				String defualtQty = StrUtils.fString(request.getParameter("defualtQty")).trim();
				String expirydate = StrUtils.fString(request.getParameter("expirydate")).trim();
				
				session.setAttribute("RESULTTOTALDISCOUNT", "");
				session.setAttribute("TOTALDISCOUNT", "");
				session.setAttribute("TOTALSUBTOTAL", "");
				session.setAttribute("TOTALTAX", "");
				session.setAttribute("CALCDISCOUNT", "");
				int iCnt = 0;
				String appenddisc = "";
				String[] chkvalues = request.getParameterValues("chk");
				int indexarray[] = new int[chkvalues.length];
				Vector<ITEMMST> updatelist = new Vector<ITEMMST>();

				if (loc.length() > 0) {

					if (chkvalues.length > 0) {

						for (int j = 0; j < chkvalues.length; j++) {
							indexarray[iCnt] = Integer.parseInt(chkvalues[iCnt]);
							iCnt++;
						}
						String ItemInPosforTranId = "";
						String BatchInPosforTranId = "";
						String UOMInPosforTranId = "";
						for (int j = 0; j < getlist.size(); j++) {
							int index = Arrays.binarySearch(indexarray, j);
							if (index < 0) {
								ITEMMST recvitembeans = getlist.get(j);
								ITEMMST recvadditem = new ITEMMST();
								recvadditem.setITEM(recvitembeans.getITEM());
								recvadditem.setITEMDESC(recvitembeans.getITEMDESC());
								recvadditem.setUNITPRICE(recvitembeans.getUNITPRICE());
								recvadditem.setStkqty(recvitembeans.getStkqty());
								recvadditem.setDISCOUNT(recvitembeans.getDISCOUNT());
								recvadditem.setTotalPrice(recvitembeans.getTotalPrice());
								recvadditem.setGSTTAX(recvitembeans.getGSTTAX());
								recvadditem.setNONSTOCKFLAG(recvitembeans.getNONSTOCKFLAG());
								recvadditem.setBATCH(recvitembeans.getBATCH());
								recvadditem.setEXPIREDATE(recvitembeans.getEXPREDATE());
								recvadditem.setSTKUOM(recvitembeans.getSTKUOM());
								updatelist.add(recvadditem);

							} else {
								ITEMMST recvitembeans = getlist.get(j);
								if (ItemInPosforTranId.length() > 0) {
									ItemInPosforTranId = ItemInPosforTranId + "," + "'" + recvitembeans.getITEM() + "'";
								} else {
									ItemInPosforTranId = "'" + recvitembeans.getITEM() + "'";
								}

								if (BatchInPosforTranId.length() > 0) {
									BatchInPosforTranId = BatchInPosforTranId + "," + "'" + recvitembeans.getBATCH()
											+ "'";
								} else {
									BatchInPosforTranId = "'" + recvitembeans.getBATCH() + "'";
								}
								
								if (UOMInPosforTranId.length() > 0) {
									UOMInPosforTranId = UOMInPosforTranId + "," + "'" + recvitembeans.getSTKUOM() + "'";
								} else {
									UOMInPosforTranId = "'" + recvitembeans.getSTKUOM() + "'";
								}
							}

						}

					//	posdet.deleteProductForTranId(PLANT, ItemInPosforTranId, RecvtranID, BatchInPosforTranId);
						posdet.deleteProductUOMForTranId(PLANT, ItemInPosforTranId, RecvtranID, BatchInPosforTranId,UOMInPosforTranId);
						getlist.removeAllElements();
						session.setAttribute("recvlist", updatelist);
					}

					session.setAttribute("errmsg", "");
				} else {
					session.setAttribute("errmsg", "Please select checkbox");
				}

				response.sendRedirect("jsp/" + posPageToDirect + "?RECVTRANID=" + RecvtranID + "&CHKBATCH="
						+ batchChecked + "&LOC=" + loc + "&serialized=" + serialized + "&defualtQty=" + defualtQty
						+ "&expirydate=" + expirydate + "&EMP_ID=" + empID + "&REFERENCENO=" + refNo
						+ "&TRANSACTIONDATE=" + tranDate + "&REASONCODE=" + holdReasonCode + "&REMARKS=" + holdRemarks
						+ "&EMP_NAME=" + empName + "&LOCDESC=" + locdesc + "&BATCH=NOBATCH"+ "&cmd="+ cmd);

			} else if (cmdAction.equalsIgnoreCase("Delete") && TRANTYPE.equalsIgnoreCase("GOODSISSUEWITHBATCH")) {

				String expireDate = "", refno = "";
				String cmd="Delete";
				expireDate = request.getParameter("EXPIREDATE");
				refno = request.getParameter("REFERENCENO");
				String empID = StrUtils.fString(request.getParameter("EMP_ID")).trim();
				String empName = StrUtils.fString(request.getParameter("EMP_NAME")).trim();
				String serialized = StrUtils.fString(request.getParameter("serialized")).trim();
				String defualtQty = StrUtils.fString(request.getParameter("defualtQty")).trim();
				String bulkcheckout = StrUtils.fString(request.getParameter("bulkcheckout")).trim();
				String refNo = StrUtils.fString(request.getParameter("REFERENCENO")).trim();
				String tranDate = StrUtils.fString(request.getParameter("TRANSACTIONDATE")).trim();
				loc = StrUtils.fString(request.getParameter("LOC")).trim();
				String locdesc = StrUtils.fString(request.getParameter("LOCDESC")).trim();
				String holdReasonCode = StrUtils.fString(request.getParameter("REASONCODE")).trim();
				String holdRemarks = StrUtils.fString(request.getParameter("REMARKS")).trim();

				session.setAttribute("RESULTTOTALDISCOUNT", "");
				session.setAttribute("TOTALDISCOUNT", "");
				session.setAttribute("TOTALSUBTOTAL", "");
				session.setAttribute("TOTALTAX", "");
				session.setAttribute("CALCDISCOUNT", "");
				int iCnt = 0;
				String appenddisc = "";
				String[] chkvalues = request.getParameterValues("chk");
				int indexarray[] = new int[chkvalues.length];
				Vector<ITEMMST> updatelist = new Vector<ITEMMST>();

				if (loc.length() > 0) {

					if (chkvalues.length > 0) {

						for (int j = 0; j < chkvalues.length; j++) {
							indexarray[iCnt] = Integer.parseInt(chkvalues[iCnt]);
							iCnt++;
						}
						String ItemInPosforTranId = "";
						String BatchInPosforTranId = "";
						String UOMInPosforTranId = "";
						for (int j = 0; j < buylist.size(); j++) {
							int index = Arrays.binarySearch(indexarray, j);
							if (index < 0) {
								ITEMMST recvitembeans = buylist.get(j);
								ITEMMST recvadditem = new ITEMMST();
								recvadditem.setITEM(recvitembeans.getITEM());
								recvadditem.setITEMDESC(recvitembeans.getITEMDESC());
								recvadditem.setUNITPRICE(recvitembeans.getUNITPRICE());
								recvadditem.setStkqty(recvitembeans.getStkqty());
								recvadditem.setDISCOUNT(recvitembeans.getDISCOUNT());
								recvadditem.setTotalPrice(recvitembeans.getTotalPrice());
								recvadditem.setGSTTAX(recvitembeans.getGSTTAX());
								recvadditem.setNONSTOCKFLAG(recvitembeans.getNONSTOCKFLAG());
								recvadditem.setBATCH(recvitembeans.getBATCH());
								recvadditem.setSTKUOM(recvitembeans.getSTKUOM());
								updatelist.add(recvadditem);

							} else {
								ITEMMST recvitembeans = buylist.get(j);
								if (ItemInPosforTranId.length() > 0) {
									ItemInPosforTranId = ItemInPosforTranId + "," + "'" + recvitembeans.getITEM() + "'";
								} else {
									ItemInPosforTranId = "'" + recvitembeans.getITEM() + "'";
								}

								if (BatchInPosforTranId.length() > 0) {
									BatchInPosforTranId = BatchInPosforTranId + "," + "'" + recvitembeans.getBATCH()
											+ "'";
								} else {
									BatchInPosforTranId = "'" + recvitembeans.getBATCH() + "'";
								}
								if (UOMInPosforTranId.length() > 0) {
									UOMInPosforTranId = UOMInPosforTranId + "," + "'" + recvitembeans.getSTKUOM() + "'";
								} else {
									UOMInPosforTranId = "'" + recvitembeans.getSTKUOM() + "'";
								}
							}

						}

					//	posdet.deleteProductForTranId(PLANT, ItemInPosforTranId, tranID, BatchInPosforTranId);
						posdet.deleteProductUOMForTranId(PLANT, ItemInPosforTranId, tranID, BatchInPosforTranId,UOMInPosforTranId);
						buylist.removeAllElements();
						session.setAttribute("poslist", updatelist);
					}

					session.setAttribute("errmsg", "");
				} else {
					session.setAttribute("errmsg", "Please select checkbox");
				}

				response.sendRedirect("jsp/" + posPageToDirect + "?TRANID=" + tranID + "&CHKBATCH=" + batchChecked
						+ "&LOC=" + loc + "" + "&serialized=" + serialized + "&defualtQty=" + defualtQty
						+ "&bulkcheckout=" + bulkcheckout + "&EMP_ID=" + empID + "&REFERENCENO=" + refNo
						+ "&TRANSACTIONDATE=" + tranDate + "&REASONCODE=" + holdReasonCode + "&REMARKS=" + holdRemarks
						+ "&EMP_NAME=" + empName + "&LOCDESC=" + locdesc + "&BATCH=NOBATCH"+"&cmd="+ cmd);
			} else if (cmdAction.equalsIgnoreCase("Delete") && TRANTYPE.equalsIgnoreCase("GOODSISSUEWITHBATCHPRICE")) {

				String expireDate = "", refno = "",EDIT="",INVOICENO="";
				String cmd = "Delete";
				expireDate = request.getParameter("EXPIREDATE");
				refno = request.getParameter("REFERENCENO");
				String empID = StrUtils.fString(request.getParameter("EMP_ID")).trim();
				String empName = StrUtils.fString(request.getParameter("EMP_NAME")).trim();
				String serialized = StrUtils.fString(request.getParameter("serialized")).trim();
				String defualtQty = StrUtils.fString(request.getParameter("defualtQty")).trim();
				String bulkcheckout = StrUtils.fString(request.getParameter("bulkcheckout")).trim();
				String refNo = StrUtils.fString(request.getParameter("REFERENCENO")).trim();
				String tranDate = StrUtils.fString(request.getParameter("TRANSACTIONDATE")).trim();
				loc = StrUtils.fString(request.getParameter("LOC")).trim();
				String locdesc = StrUtils.fString(request.getParameter("LOCDESC")).trim();
				String holdReasonCode = StrUtils.fString(request.getParameter("REASONCODE")).trim();
				String holdRemarks = StrUtils.fString(request.getParameter("REMARKS")).trim();
				EDIT= StrUtils.fString(request.getParameter("EDIT")).trim();
				INVOICENO= StrUtils.fString(request.getParameter("INVOICENO")).trim();
				String INVOICECHECK= StrUtils.fString(request.getParameter("INVOICECHECK")).trim();
				String custName = StrUtils.replaceCharacters2Recv(StrUtils.fString(request.getParameter("CUST_NAME"))).trim();
				String custCode = StrUtils.fString(request.getParameter("CUST_CODE1")).trim();
				String LOGIN_USER = StrUtils.fString(request.getParameter("LOGIN_USER"));
				session.setAttribute("RESULTTOTALDISCOUNT", "");
				session.setAttribute("TOTALDISCOUNT", "");
				session.setAttribute("TOTALSUBTOTAL", "");
				session.setAttribute("TOTALTAX", "");
				session.setAttribute("CALCDISCOUNT", "");
				int iCnt = 0;
				String appenddisc = "";
				String[] chkvalues = request.getParameterValues("chk");
				int indexarray[] = new int[chkvalues.length];
				Vector<ITEMMST> updatelist = new Vector<ITEMMST>();
				Map receiveMaterial_HM = null;
				process: 
				//if (loc.length() > 0) {

					if (chkvalues.length > 0) {

						for (int j = 0; j < chkvalues.length; j++) {
							indexarray[iCnt] = Integer.parseInt(chkvalues[iCnt]);
							iCnt++;
						}
						String ItemInPosforTranId = "";
						String BatchInPosforTranId = "";
						String UNITPRICE = "";
						String CHKPRICE = "";
						String Location = "";
						for (int j = 0; j < buylist.size(); j++) {
							int index = Arrays.binarySearch(indexarray, j);
							if (index < 0) {
								ITEMMST recvitembeans = buylist.get(j);
								ITEMMST recvadditem = new ITEMMST();
								recvadditem.setITEM(recvitembeans.getITEM());
								recvadditem.setITEMDESC(recvitembeans.getITEMDESC());
								recvadditem.setUNITPRICE(recvitembeans.getUNITPRICE());
								recvadditem.setUSERFLD4(recvitembeans.getUSERFLD4());
								recvadditem.setUSERFLD6(recvitembeans.getUSERFLD6());
								recvadditem.setStkqty(recvitembeans.getStkqty());
								recvadditem.setDISCOUNT(recvitembeans.getDISCOUNT());
								recvadditem.setTotalPrice(recvitembeans.getTotalPrice());
								recvadditem.setGSTTAX(recvitembeans.getGSTTAX());
								recvadditem.setNONSTOCKFLAG(recvitembeans.getNONSTOCKFLAG());
								recvadditem.setBATCH(recvitembeans.getBATCH());
								recvadditem.setSTKUOM(recvitembeans.getSTKUOM());
								recvadditem.setLoc(recvitembeans.getLoc());
								updatelist.add(recvadditem);

								/*if(EDIT!="")
								{						
								boolean flag =true;
								
								receiveMaterial_HM = new HashMap();
								receiveMaterial_HM.put(IConstants.PLANT, PLANT);
								receiveMaterial_HM.put(IConstants.ITEM, recvitembeans.getITEM());
								receiveMaterial_HM.put(IConstants.ITEM_DESC, recvitembeans.getITEMDESC());
								receiveMaterial_HM.put(IConstants.DODET_DONUM, tranID);
								receiveMaterial_HM.put(IConstants.DODET_DOLNNO, tranID);
								receiveMaterial_HM.put(IConstants.CUSTOMER_NAME, custName);
								receiveMaterial_HM.put(IConstants.LOC, loc);						
								receiveMaterial_HM.put(IConstants.LOC2, loc);
								receiveMaterial_HM.put(IConstants.LOGIN_USER, LOGIN_USER);
								receiveMaterial_HM.put(IConstants.CUSTOMER_CODE, custCode);
								receiveMaterial_HM.put(IConstants.BATCH, recvitembeans.getBATCH());
								receiveMaterial_HM.put(IConstants.QTY, recvitembeans.getStkqty());
								receiveMaterial_HM.put(IConstants.ORD_QTY, recvitembeans.getStkqty());
								receiveMaterial_HM.put("ISSUEDQTY", tranDate);
								receiveMaterial_HM.put(IConstants.REMARKS, holdRemarks);
								receiveMaterial_HM.put(IConstants.RSNDESC, holdReasonCode);
								receiveMaterial_HM.put(IConstants.TRAN_DATE, tranDate);
								receiveMaterial_HM.put(IConstants.ISPDA, "GIWP");								
								flag = dOUtil.process_OBISSUEReversal(receiveMaterial_HM)&& true;
								if(flag)
								{
								SalesDetailDAO salesdao = new SalesDetailDAO();
								Hashtable<String, String> htTrandId = new Hashtable<String, String>();
								htTrandId.put("TRANID", tranID);
								htTrandId.put(IConstants.PLANT, PLANT);
								htTrandId.put(IConstants.LOC, loc);	
								htTrandId.put(IConstants.BATCH, recvitembeans.getBATCH());
								htTrandId.put(IConstants.QTY, String.valueOf(recvitembeans.getStkqty()));
								htTrandId.put(IConstants.ITEM, recvitembeans.getITEM());
								htTrandId.put(IConstants.ITEM_DESC, recvitembeans.getITEMDESC());
								
								flag = salesdao.isExisit(htTrandId,"");
								if(flag)
								{
									String updateissuedet = "";
									updateissuedet = "set Qty =Qty -" + recvitembeans.getStkqty() +",UPAT='" +DateUtils.getDateTime()+"',UPBY='"+LOGIN_USER+"'";
									flag=salesdao.update(updateissuedet, htTrandId, "");
									if(flag)
									{
										htTrandId.remove(IConstants.QTY);
										flag = salesdao.isExisit(htTrandId," Qty=0");
										if(flag)
										{
											htTrandId.put(IConstants.QTY,"0");
											flag = salesdao.deleteSales(htTrandId);
										}
									}
								}
								}
								if(!flag)
									break process;
								}*/
								
								
							} else {
								ITEMMST recvitembeans = buylist.get(j);
								String price = StrUtils.removeFormat(recvitembeans.getUSERFLD4());
								price = new POSUtil().getConvertedUnitCostToLocalCurrency(PLANT, tranID, price);
								
								//Due to decimal Issue converting unitprice in float value and making transaction -- Azees 								
								float PricewTaxVal ="".equals(price) ? 0.0f :  Float.parseFloat(price);
								String priceval = String.valueOf(PricewTaxVal);
								
								if (ItemInPosforTranId.length() > 0) {
									ItemInPosforTranId = ItemInPosforTranId + "," + "'" + recvitembeans.getITEM() + "'";
								} else {
									ItemInPosforTranId = "'" + recvitembeans.getITEM() + "'";
								}

								if (BatchInPosforTranId.length() > 0) {
									BatchInPosforTranId = BatchInPosforTranId + "," + "'" + recvitembeans.getBATCH()
											+ "'";
								} else {
									BatchInPosforTranId = "'" + recvitembeans.getBATCH() + "'";
								}
								
								if (UNITPRICE.length() > 0) {
									UNITPRICE = UNITPRICE + "," + "'" + price
											+ "'";
								} else {
									UNITPRICE = "'" + price + "'";
								}
								
								if (Location.length() > 0) {
									Location = Location + "," + "'" + recvitembeans.getLoc()
											+ "'";
								} else {
									Location = "'" + recvitembeans.getLoc() + "'";
								}
								
								if(EDIT!="")
								{						
								boolean flag =true;
								boolean chkflag =true;
								SalesDetailDAO salesdao = new SalesDetailDAO();
								Hashtable<String, String> htTrandId = new Hashtable<String, String>();
								htTrandId.put("TRANID", tranID);
								htTrandId.put(IConstants.PLANT, PLANT);
								htTrandId.put(IConstants.LOC, recvitembeans.getLoc());	
								htTrandId.put(IConstants.BATCH, recvitembeans.getBATCH());
								htTrandId.put(IConstants.QTY, String.valueOf(recvitembeans.getStkqty()));
								htTrandId.put(IConstants.ITEM, recvitembeans.getITEM());
								htTrandId.put(IConstants.ITEM_DESC, recvitembeans.getITEMDESC());
								htTrandId.put("UNITPRICE", price);
								htTrandId.put(IConstants.UNITMO, recvitembeans.getSTKUOM());
								chkflag = salesdao.isExisit(htTrandId,"");// Check Sales DAO
								if(chkflag)
								{
								receiveMaterial_HM = new HashMap();
								receiveMaterial_HM.put(IConstants.PLANT, PLANT);
								receiveMaterial_HM.put(IConstants.ITEM, recvitembeans.getITEM());
								receiveMaterial_HM.put(IConstants.ITEM_DESC, recvitembeans.getITEMDESC());
								receiveMaterial_HM.put(IConstants.DODET_DONUM, tranID);
								//receiveMaterial_HM.put(IConstants.DODET_DOLNNO, Integer.toString(j + 1));
								receiveMaterial_HM.put(IConstants.CUSTOMER_NAME, custName);
								receiveMaterial_HM.put(IConstants.LOC, recvitembeans.getLoc());						
								receiveMaterial_HM.put(IConstants.LOC2, recvitembeans.getLoc());
								receiveMaterial_HM.put(IConstants.LOGIN_USER, LOGIN_USER);
								receiveMaterial_HM.put(IConstants.CUSTOMER_CODE, custCode);
								receiveMaterial_HM.put(IConstants.BATCH, recvitembeans.getBATCH());
								receiveMaterial_HM.put(IConstants.QTY, recvitembeans.getStkqty());
								receiveMaterial_HM.put(IConstants.ORD_QTY, recvitembeans.getStkqty());
								receiveMaterial_HM.put("ISSUEDQTY", tranDate);
								receiveMaterial_HM.put(IConstants.REMARKS, holdRemarks);
								receiveMaterial_HM.put(IConstants.RSNDESC, holdReasonCode);
								receiveMaterial_HM.put(IConstants.TRAN_DATE, tranDate.substring(6, 10) + "-" + tranDate.substring(3, 5) + "-"
										+ tranDate.substring(0, 2));
								receiveMaterial_HM.put(IConstants.ISPDA, "GIWP");
								receiveMaterial_HM.put("UNITPRICE", price);
								String UOMQTY="1";
								Hashtable<String, String> htTrand1 = new Hashtable<String, String>();
								MovHisDAO movHisDao1 = new MovHisDAO();
								ArrayList getuomqty = movHisDao1.selectForReport("select ISNULL(QPUOM,1) as UOMQTY from "+PLANT+"_UOM where UOM='"+String.valueOf(recvitembeans.getSTKUOM())+"'",htTrand1);
								if(getuomqty.size()>0)
								{
								Map mapval = (Map) getuomqty.get(0);
								UOMQTY=(String)mapval.get("UOMQTY");
								}
								receiveMaterial_HM.put("UOMQTY", UOMQTY);
								flag = dOUtil.process_OBISSUEReversal(receiveMaterial_HM)&& true;
								if(flag)
								{
								
									String updateissuedet = "";
									updateissuedet = "set Qty =Qty -" + recvitembeans.getStkqty() +",UPAT='" +DateUtils.getDateTime()+"',UPBY='"+LOGIN_USER+"'";
									flag=salesdao.update(updateissuedet, htTrandId, "");
									if(flag)
									{
										htTrandId.remove(IConstants.QTY);
										flag = salesdao.isExisit(htTrandId," Qty=0");
										if(flag)
										{
											htTrandId.put(IConstants.QTY,"0");
											flag = salesdao.deleteSales(htTrandId);
										}
									}
								}
								}
								else
								{
									if (CHKPRICE.length() > 0) {
										CHKPRICE = CHKPRICE + "," + "'" + priceval
												+ "'";
									} else {
										CHKPRICE = "'" + priceval + "'";
									}
								
									htTrandId.remove("UNITPRICE");								
									htTrandId.put("UNITPRICE", priceval);
								
									chkflag = salesdao.isExisit(htTrandId,"");
									if(chkflag)
									{
									receiveMaterial_HM = new HashMap();
									receiveMaterial_HM.put(IConstants.PLANT, PLANT);
									receiveMaterial_HM.put(IConstants.ITEM, recvitembeans.getITEM());
									receiveMaterial_HM.put(IConstants.ITEM_DESC, recvitembeans.getITEMDESC());
									receiveMaterial_HM.put(IConstants.DODET_DONUM, tranID);
									//receiveMaterial_HM.put(IConstants.DODET_DOLNNO, Integer.toString(j + 1));
									receiveMaterial_HM.put(IConstants.CUSTOMER_NAME, custName);
									receiveMaterial_HM.put(IConstants.LOC, recvitembeans.getLoc());						
									receiveMaterial_HM.put(IConstants.LOC2, recvitembeans.getLoc());
									receiveMaterial_HM.put(IConstants.LOGIN_USER, LOGIN_USER);
									receiveMaterial_HM.put(IConstants.CUSTOMER_CODE, custCode);
									receiveMaterial_HM.put(IConstants.BATCH, recvitembeans.getBATCH());
									receiveMaterial_HM.put(IConstants.QTY, recvitembeans.getStkqty());
									receiveMaterial_HM.put(IConstants.ORD_QTY, recvitembeans.getStkqty());
									receiveMaterial_HM.put("ISSUEDQTY", tranDate);
									receiveMaterial_HM.put(IConstants.REMARKS, holdRemarks);
									receiveMaterial_HM.put(IConstants.RSNDESC, holdReasonCode);
									receiveMaterial_HM.put(IConstants.TRAN_DATE, tranDate.substring(6, 10) + "-" + tranDate.substring(3, 5) + "-"
											+ tranDate.substring(0, 2));
									receiveMaterial_HM.put(IConstants.ISPDA, "GIWP");
									receiveMaterial_HM.put("UNITPRICE", priceval);
									String UOMQTY="1";
									Hashtable<String, String> htTrand1 = new Hashtable<String, String>();
									MovHisDAO movHisDao1 = new MovHisDAO();
									ArrayList getuomqty = movHisDao1.selectForReport("select ISNULL(QPUOM,1) as UOMQTY from "+PLANT+"_UOM where UOM='"+String.valueOf(recvitembeans.getSTKUOM())+"'",htTrand1);
									if(getuomqty.size()>0)
									{
									Map mapval = (Map) getuomqty.get(0);
									UOMQTY=(String)mapval.get("UOMQTY");
									}
									receiveMaterial_HM.put("UOMQTY", UOMQTY);
									flag = dOUtil.process_OBISSUEReversal(receiveMaterial_HM)&& true;
									if(flag)
									{
									
										String updateissuedet = "";
										updateissuedet = "set Qty =Qty -" + recvitembeans.getStkqty() +",UPAT='" +DateUtils.getDateTime()+"',UPBY='"+LOGIN_USER+"'";
										flag=salesdao.update(updateissuedet, htTrandId, "");
										if(flag)
										{
											htTrandId.remove(IConstants.QTY);
											flag = salesdao.isExisit(htTrandId," Qty=0");
											if(flag)
											{
												htTrandId.put(IConstants.QTY,"0");
												flag = salesdao.deleteSales(htTrandId);
											}
										}
									}
								}
								}
								if(!flag)
									break process;
								}
								
								
							}

						}
						//Delete From POSDET
						posdet.deleteProductForTranIdPrice(PLANT, ItemInPosforTranId, tranID, BatchInPosforTranId,UNITPRICE,Location);
						
							if (CHKPRICE.length() > 0)
						posdet.deleteProductForTranIdPrice(PLANT, ItemInPosforTranId, tranID, BatchInPosforTranId,CHKPRICE,Location);							
						
												buylist.removeAllElements();
						session.setAttribute("poslist", updatelist);
					}

				//	session.setAttribute("errmsg", "");
				//} 
				else {
					session.setAttribute("errmsg", "Please select checkbox");
				}

				response.sendRedirect("jsp/" + posPageToDirect + "?TRANID=" + tranID + "&CHKBATCH=" + batchChecked
						+ "&LOC=" + loc + "" + "&serialized=" + serialized + "&defualtQty=" + defualtQty
						+ "&bulkcheckout=" + bulkcheckout + "&EMP_ID=" + empID + "&REFERENCENO=" + refNo
						+ "&TRANSACTIONDATE=" + tranDate + "&REASONCODE=" + holdReasonCode + "&REMARKS=" + StrUtils.replaceCharacters2Send(holdRemarks)
						+ "&EMP_NAME=" + empName + "&LOCDESC=" + locdesc + "&BATCH=NOBATCH"+"&INVOICENO="+INVOICENO+"&INVOICECHECK="+INVOICECHECK+ "&cmd=" + cmd);
			} else if (cmdAction.equalsIgnoreCase("DeleteAll") && TRANTYPE.equalsIgnoreCase("GOODSISSUEWITHBATCHPRICE")) {
				
				MovHisDAO movHisDao = new MovHisDAO();
				DateUtils dateUtils = null;
				String expireDate = "", refno = "",EDIT="",INVOICENO="";
				String cmd = "Delete";
				expireDate = request.getParameter("EXPIREDATE");
				refno = request.getParameter("REFERENCENO");
				String empID = StrUtils.fString(request.getParameter("EMP_ID")).trim();
				String empName = StrUtils.fString(request.getParameter("EMP_NAME")).trim();
				String serialized = StrUtils.fString(request.getParameter("serialized")).trim();
				String defualtQty = StrUtils.fString(request.getParameter("defualtQty")).trim();
				String bulkcheckout = StrUtils.fString(request.getParameter("bulkcheckout")).trim();
				String refNo = StrUtils.fString(request.getParameter("REFERENCENO")).trim();
				String tranDate = StrUtils.fString(request.getParameter("TRANSACTIONDATE")).trim();
				loc = StrUtils.fString(request.getParameter("LOC")).trim();
				String locdesc = StrUtils.fString(request.getParameter("LOCDESC")).trim();
				String holdReasonCode = StrUtils.fString(request.getParameter("REASONCODE")).trim();
				String holdRemarks = StrUtils.fString(request.getParameter("REMARKS")).trim();
				EDIT= StrUtils.fString(request.getParameter("EDIT")).trim();
				INVOICENO= StrUtils.fString(request.getParameter("INVOICENO")).trim();
				String custName = StrUtils.replaceCharacters2Recv(StrUtils.fString(request.getParameter("CUST_NAME"))).trim();
				String custCode = StrUtils.fString(request.getParameter("CUST_CODE1")).trim();
				String LOGIN_USER = StrUtils.fString(request.getParameter("LOGIN_USER"));
				session.setAttribute("RESULTTOTALDISCOUNT", "");
				session.setAttribute("TOTALDISCOUNT", "");
				session.setAttribute("TOTALSUBTOTAL", "");
				session.setAttribute("TOTALTAX", "");
				session.setAttribute("CALCDISCOUNT", "");
				int iCnt = 0;
				String appenddisc = "";
				
				Vector<ITEMMST> updatelist = new Vector<ITEMMST>();
				Map receiveMaterial_HM = null;
				process:
					if(buylist.size()>0)
					{
					String ItemInPosforTranId = "";
					String BatchInPosforTranId = "";
					String UNITPRICE = "";
					String CHKPRICE = "";
					String Location = "";
					for (int j = 0; j < buylist.size(); j++) {
							
							ITEMMST recvitembeans = buylist.get(j);
							String price = StrUtils.removeFormat(recvitembeans.getUSERFLD4());
							price = new POSUtil().getConvertedUnitCostToLocalCurrency(PLANT, tranID, price);
							
							// Due to decimal Issue converting unitprice in float value and making transaction -- Azees 
							float PricewTaxVal ="".equals(price) ? 0.0f :  Float.parseFloat(price);
							String priceval = String.valueOf(PricewTaxVal);
							
							if (ItemInPosforTranId.length() > 0) {
								ItemInPosforTranId = ItemInPosforTranId + "," + "'" + recvitembeans.getITEM() + "'";
							} else {
								ItemInPosforTranId = "'" + recvitembeans.getITEM() + "'";
							}

							if (BatchInPosforTranId.length() > 0) {
								BatchInPosforTranId = BatchInPosforTranId + "," + "'" + recvitembeans.getBATCH()
										+ "'";
							} else {
								BatchInPosforTranId = "'" + recvitembeans.getBATCH() + "'";
							}
							
							if (UNITPRICE.length() > 0) {
								UNITPRICE = UNITPRICE + "," + "'" + price
										+ "'";
							} else {
								UNITPRICE = "'" + price + "'";
							}
							
							if (Location.length() > 0) {
								Location = Location + "," + "'" + recvitembeans.getLoc()
										+ "'";
							} else {
								Location = "'" + recvitembeans.getLoc() + "'";
							}
							boolean flag =true;
							Hashtable htMovHis = new Hashtable();
							htMovHis.clear();
							htMovHis.put(IDBConstants.PLANT, PLANT);							
							htMovHis.put("DIRTYPE", TransactionConstants.DELETE_DIRECT_TAX_INVOICE);
							htMovHis.put(IDBConstants.TRAN_DATE, dateUtils.getDateinyyyy_mm_dd(tranDate));														
							htMovHis.put(IDBConstants.ITEM, recvitembeans.getITEM());
							htMovHis.put("BATNO", recvitembeans.getBATCH());
							String qty = String.valueOf(recvitembeans.getStkqty());
							htMovHis.put(IDBConstants.QTY, qty);
							htMovHis.put("MOVTID", "IN");
							htMovHis.put("RECID", "");
							htMovHis.put(IDBConstants.LOC,recvitembeans.getLoc());
							htMovHis.put(IDBConstants.MOVHIS_ORDNUM, tranID);
							htMovHis.put(IDBConstants.CREATED_BY, LOGIN_USER);		
							htMovHis.put(IDBConstants.CREATED_AT, dateUtils.getDateTime());
							htMovHis.put("REMARKS", holdRemarks+","+holdReasonCode);
							String UOMQTY="1";
							Hashtable<String, String> htTrand1 = new Hashtable<String, String>();
							MovHisDAO movHisDao1 = new MovHisDAO();
							ArrayList getuomqty = movHisDao1.selectForReport("select ISNULL(QPUOM,1) as UOMQTY from "+PLANT+"_UOM where UOM='"+String.valueOf(recvitembeans.getSTKUOM())+"'",htTrand1);
							if(getuomqty.size()>0)
							{
							Map mapval = (Map) getuomqty.get(0);
							UOMQTY=(String)mapval.get("UOMQTY");
							}
							boolean chkflag =true;
							SalesDetailDAO salesdao = new SalesDetailDAO();
							Hashtable<String, String> htTrandId = new Hashtable<String, String>();
							htTrandId.put("TRANID", tranID);
							htTrandId.put(IConstants.PLANT, PLANT);
							htTrandId.put(IConstants.LOC, recvitembeans.getLoc());	
							htTrandId.put(IConstants.BATCH, recvitembeans.getBATCH());
							htTrandId.put(IConstants.QTY, String.valueOf(recvitembeans.getStkqty()));
							htTrandId.put(IConstants.ITEM, recvitembeans.getITEM());
							htTrandId.put(IConstants.ITEM_DESC, recvitembeans.getITEMDESC());
							htTrandId.put("UNITPRICE", price);
							htTrandId.put(IConstants.UNITMO, recvitembeans.getSTKUOM());
							chkflag = salesdao.isExisit(htTrandId,""); // Check SALES_DETAIL
							if(chkflag)
							{	

								ShipHisDAO shiphstdao = new ShipHisDAO();										
								shiphstdao.setmLogger(mLogger);
								Hashtable<String, String> htTrandId1 = new Hashtable<String, String>();										
								htTrandId1.put(IConstants.DODET_DONUM, tranID);
								htTrandId1.put(IConstants.PLANT, PLANT);
								htTrandId1.put(IConstants.ITEM, recvitembeans.getITEM());
								htTrandId1.put(IConstants.LOC, String.valueOf(recvitembeans.getLoc()));	
								if (recvitembeans.getBATCHID() != -1) {
									htTrandId1.put(IConstants.INVID, String.valueOf(recvitembeans.getBATCHID()));
								}
								htTrandId1.put(IDBConstants.PRICE, price);
								flag = shiphstdao.isExisit(htTrandId1);	// Check SHIPHIS Based on Unitprice	 						
								if(flag)
								{
									Map mPrddet = new ItemMstDAO().getProductNonStockDetails(PLANT,(String)recvitembeans.getITEM());
							        String nonstocktype= StrUtils.fString((String) mPrddet.get("NONSTKFLAG"));
							        if(!nonstocktype.equals("Y"))	
								    {
							        ArrayList ALIssueddetails = new ArrayList();							        
									ALIssueddetails = shiphstdao.getIssuedDetailsforreverse(PLANT, htTrandId1);
									InvMstDAO _InvMstDAO = new InvMstDAO();
									_InvMstDAO.setmLogger(mLogger);
									Hashtable htInvMst = new Hashtable();

									htInvMst.clear();
									htInvMst.put(IDBConstants.PLANT, PLANT);
									htInvMst.put(IDBConstants.ITEM, String.valueOf(recvitembeans.getITEM()));
									htInvMst.put(IDBConstants.LOC, String.valueOf(recvitembeans.getLoc()));
									htInvMst.put(IDBConstants.USERFLD4, String.valueOf(recvitembeans.getBATCH()));
									
									double reverseqty = Double.parseDouble(qty);
									
									if (ALIssueddetails.size() > 0) {
										for (int i = 0; i < ALIssueddetails.size(); i++) {
											Map m = (Map) ALIssueddetails.get(i);
											{
												if(reverseqty==0) break;
												String ID = (String)m.get("ID").toString();
												String PQTY = (String)m.get("PICKQTY").toString();
												double issueqty = Double.parseDouble(PQTY);
												htInvMst.put(IDBConstants.INVID, ID);
												
										        	  
										        	
									StringBuffer sql1 = new StringBuffer(" SET ");

									 if(reverseqty >= issueqty)
									 {
										 double inqty = Double.valueOf(issueqty) * Double.valueOf(UOMQTY);
										 sql1.append(IDBConstants.QTY + " = QTY +'" + inqty + "'");
									 }
									 else
									 {
										 double inqty = Double.valueOf(reverseqty) * Double.valueOf(UOMQTY);
										 sql1.append(IDBConstants.QTY + " = QTY +'" + inqty + "'");
										 issueqty=reverseqty;
									 }
									 
									sql1.append("," + IDBConstants.UPDATED_AT + " = '"
											+ dateUtils.getDateTime() + "'");

									flag = _InvMstDAO.isExisit(htInvMst, "");
									if(flag){
										if(reverseqty>0)
										{
											flag = _InvMstDAO.update(sql1.toString(), htInvMst, "");
										reverseqty = (reverseqty-issueqty);
										}

									} else {
										htInvMst.remove(IDBConstants.INVID);
										if(reverseqty >= issueqty)
										{
											double inqty = Double.valueOf(issueqty) * Double.valueOf(UOMQTY);
											htInvMst.put(IDBConstants.QTY, String.valueOf(inqty));
										}
										else
										{
											double inqty = Double.valueOf(reverseqty) * Double.valueOf(UOMQTY);
											htInvMst.put(IDBConstants.QTY, String.valueOf(inqty));
										}
										htInvMst.put(IDBConstants.USERFLD3,"");
										htInvMst.put(IDBConstants.CREATED_AT, (tranDate.substring(6, 10) + "-" + tranDate.substring(3, 5) + "-"
												+ tranDate.substring(0, 2)).toString().replaceAll("-", "").replaceAll("/", "") + "000000");
										htInvMst.put(IDBConstants.STATUS, "");
										htInvMst.put(IConstants.EXPIREDATE,"");
										flag = _InvMstDAO.insertInvMst(htInvMst);
										
									}
											}
										}
										 ALIssueddetails = new ArrayList();
										}
								    }
									flag = salesdao.deleteSales(htTrandId); // Delete SALES_DETAIL
									flag = shiphstdao.deleteSHIPHIS(htTrandId1); // Delete SHIPHIS
									flag = movHisDao.insertIntoMovHis(htMovHis); // Insert MOVHIS
								}
							}
							else
							{

									if (CHKPRICE.length() > 0) {
										CHKPRICE = CHKPRICE + "," + "'" + priceval
												+ "'";
									} else {
										CHKPRICE = "'" + priceval + "'";
									}
								
									htTrandId.remove("UNITPRICE");								
									htTrandId.put("UNITPRICE", priceval);
									chkflag = salesdao.isExisit(htTrandId,""); // Check SALES_DETAIL
									if(chkflag)
									{
									ShipHisDAO shiphstdao = new ShipHisDAO();										
									shiphstdao.setmLogger(mLogger);
									Hashtable<String, String> htTrandId1 = new Hashtable<String, String>();										
									htTrandId1.put(IConstants.DODET_DONUM, tranID);
									htTrandId1.put(IConstants.PLANT, PLANT);
									htTrandId1.put(IConstants.ITEM, recvitembeans.getITEM());
									htTrandId1.put(IConstants.LOC, String.valueOf(recvitembeans.getLoc()));	
									if (recvitembeans.getBATCHID() != -1) {
										htTrandId1.put(IConstants.INVID, String.valueOf(recvitembeans.getBATCHID()));
									}
									htTrandId1.put(IDBConstants.PRICE, priceval);
									flag = shiphstdao.isExisit(htTrandId1);	// Check SHIPHIS 	 						
									if(flag)
									{
										Map mPrddet = new ItemMstDAO().getProductNonStockDetails(PLANT,(String)recvitembeans.getITEM());
								        String nonstocktype= StrUtils.fString((String) mPrddet.get("NONSTKFLAG"));
								        if(!nonstocktype.equals("Y"))	
									    {
								        ArrayList ALIssueddetails = new ArrayList();							        
										ALIssueddetails = shiphstdao.getIssuedDetailsforreverse(PLANT, htTrandId1);
										InvMstDAO _InvMstDAO = new InvMstDAO();
										_InvMstDAO.setmLogger(mLogger);
										Hashtable htInvMst = new Hashtable();

										htInvMst.clear();
										htInvMst.put(IDBConstants.PLANT, PLANT);
										htInvMst.put(IDBConstants.ITEM, String.valueOf(recvitembeans.getITEM()));
										htInvMst.put(IDBConstants.LOC, String.valueOf(recvitembeans.getLoc()));
										htInvMst.put(IDBConstants.USERFLD4, String.valueOf(recvitembeans.getBATCH()));
										
										double reverseqty = Double.parseDouble(qty);
										
										if (ALIssueddetails.size() > 0) {
											for (int i = 0; i < ALIssueddetails.size(); i++) {
												Map m = (Map) ALIssueddetails.get(i);
												{
													if(reverseqty==0) break;
													String ID = (String)m.get("ID").toString();
													String PQTY = (String)m.get("PICKQTY").toString();
													double issueqty = Double.parseDouble(PQTY);
													htInvMst.put(IDBConstants.INVID, ID);
													
											        	  
											        	
										StringBuffer sql1 = new StringBuffer(" SET ");

										 if(reverseqty >= issueqty)
										 {
											 double inqty = Double.valueOf(issueqty) * Double.valueOf(UOMQTY);
											 sql1.append(IDBConstants.QTY + " = QTY +'" + inqty + "'");
										 }
										 else
										 {
											 double inqty = Double.valueOf(reverseqty) * Double.valueOf(UOMQTY);
											 sql1.append(IDBConstants.QTY + " = QTY +'" + inqty + "'");
											 issueqty=reverseqty;
										 }
										 
										sql1.append("," + IDBConstants.UPDATED_AT + " = '"
												+ dateUtils.getDateTime() + "'");

										flag = _InvMstDAO.isExisit(htInvMst, "");
										if(flag){
											if(reverseqty>0)
											{
												flag = _InvMstDAO.update(sql1.toString(), htInvMst, "");
											reverseqty = (reverseqty-issueqty);
											}

										} else {
											htInvMst.remove(IDBConstants.INVID);
											if(reverseqty >= issueqty)
											{
												double inqty = Double.valueOf(issueqty) * Double.valueOf(UOMQTY);
												htInvMst.put(IDBConstants.QTY, String.valueOf(inqty));
											}
											else
											{
												double inqty = Double.valueOf(reverseqty) * Double.valueOf(UOMQTY);
												htInvMst.put(IDBConstants.QTY, String.valueOf(inqty));
											}
											htInvMst.put(IDBConstants.USERFLD3,"");
											htInvMst.put(IDBConstants.CREATED_AT, (tranDate.substring(6, 10) + "-" + tranDate.substring(3, 5) + "-"
													+ tranDate.substring(0, 2)).toString().replaceAll("-", "").replaceAll("/", "") + "000000");
											htInvMst.put(IDBConstants.STATUS, "");
											htInvMst.put(IConstants.EXPIREDATE,"");
											flag = _InvMstDAO.insertInvMst(htInvMst);
											
										}
												}
											}
											 ALIssueddetails = new ArrayList();
											}
									    }
										flag = salesdao.deleteSales(htTrandId); // Delete SALES_DETAIL
										flag = shiphstdao.deleteSHIPHIS(htTrandId1); // Delete SHIPHIS
										flag = movHisDao.insertIntoMovHis(htMovHis); // Insert MOVHIS
									}
									}

							}
							if(!flag)
								break process;
							
							
					}
					// Delete POSDET
				posdet.deleteProductForTranIdPrice(PLANT, ItemInPosforTranId, tranID, BatchInPosforTranId,UNITPRICE,Location);
				
				if (CHKPRICE.length() > 0)
					posdet.deleteProductForTranIdPrice(PLANT, ItemInPosforTranId, tranID, BatchInPosforTranId,CHKPRICE,Location);
				
				buylist.removeAllElements();
				session.setAttribute("poslist", updatelist);
				DNPLHdrDAO _DNPLHdrDAO = new DNPLHdrDAO();
				DNPLDetDAO _DNPLDetDAO = new DNPLDetDAO();
				boolean flag =true;
				
				Hashtable<String, String> ht = new Hashtable<String, String>();
				ht.put(IDBConstants.PLANT, PLANT);
				ht.put(IDBConstants.DNPLDET_DONUM, tranID);				
				flag = _DNPLHdrDAO.isExisit(ht,""); // Check DNPLHDR
				
				if(flag)
				{
						flag = _DNPLHdrDAO.delete(ht); // Delete DNPLHDR
					if(flag)					
						flag = _DNPLDetDAO.delete(ht); // Delete DNPLDET
				}
					ht = new Hashtable<String, String>();
					ht.put(IDBConstants.PLANT, PLANT);
					ht.put(IDBConstants.POS_TRANID, tranID);
					POSHdrDAO poshdrdao = new POSHdrDAO();
					poshdrdao.setmLogger(mLogger);					
					flag = poshdrdao.deletePosHdr(ht);
					}
				request.getSession().setAttribute("RESULT","Transaction " +tranID+ " Deleted Successfully");
				response.sendRedirect("jsp/" + posPageToDirect + "?result=sucess");
			} else if (cmdAction.equalsIgnoreCase("printreceiveproduct")) {

				String TRAN_TYPE = StrUtils.fString(request.getParameter("TRANTYPE")).trim();

				if (TRAN_TYPE.equalsIgnoreCase("GOODSRECEIPTWITHOUTBATCH")) {
					posPageToDirect = "MultiMiscOrderReceiving.jsp";
				} else if (TRAN_TYPE.equalsIgnoreCase("GOODSRECEIPT")) {
					posPageToDirect = "../purchasetransaction/goodsreceipt";
				}

				String empID = StrUtils.fString(request.getParameter("EMP_ID")).trim();
				String rsncode = StrUtils.fString(request.getParameter("REASONCODE")).trim();
				String refNo = StrUtils.fString(request.getParameter("REFERENCENO")).trim();
				String tranDate = StrUtils.fString(request.getParameter("TRANSACTIONDATE")).trim();
                String Quantity="";
                double totalqty=0,pickingQty=0;
				boolean ajax = false;
				ajax = "XMLHttpRequest".equals(request.getHeader("X-Requested-With"));

				DateUtils dateUtils = new DateUtils();
				String fromdates = dateUtils.parseDateddmmyyyy(tranDate);
				String time = DateUtils.getTimeHHmm();
				String orderdate = fromdates+time+"12";
				LocalDate dates = LocalDate.parse(fromdates, DateTimeFormatter.BASIC_ISO_DATE);
				String formattedDate = dates.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
				String tran = formattedDate;
				
				if (ajax) {
					try {
						UserTransaction ut = null;
						ut = com.track.gates.DbBean.getUserTranaction();
						ut.begin();

						SalesDetailDAO salesdao = new SalesDetailDAO();
						POSPaymentDetailDAO paymentDAO = new POSPaymentDetailDAO();
						String rcpNo = "";
						Generator gen = new Generator();

						boolean flag = false;
						boolean invflag = false;
						// To check transaction already exists in POSHeader
						Hashtable<String, String> htTrandId = new Hashtable<String, String>();
						htTrandId.put("POSTRANID", RecvtranID);
						boolean istranidExist = _posServlet.isExisit(PLANT, htTrandId);
						if (istranidExist == false) {
							String genPosTranid = poshdr.genPosManualTranId(PLANT, login_user, loc, RecvtranID);
						}
						// To check transaction already exists in POSHeader end
						String xmlutil = "";
						HashMap<String, String> htrecv = new HashMap<String, String>();
						String paymentmodes = "", date = "";
						if (EXPIREDATE.length() > 8) {
							date = EXPIREDATE.substring(6, 10) + "-" + EXPIREDATE.substring(3, 5) + "-"
									+ EXPIREDATE.substring(0, 2);
						}
						MiscIssuingUtil miscutil = new MiscIssuingUtil();
						Hashtable<String, String> htm = new Hashtable<String, String>();
						String formatdt = DateUtils.getDate();
						htm.put("PURCHASEDATE", DateUtils.getDateinyyyy_mm_dd(formatdt));
						htm.put("PURCHASETIME", DateUtils.Time());
						htm.put(IConstants.REMARKS, remarks);
						//htm.put(IConstants.REASONCODE, reasoncode);
						if (reasoncode.equals("NOREASONCODE"))
						{
							htm.put(IConstants.REASONCODE, " ");
						}
						else{
							htm.put(IConstants.REASONCODE, reasoncode);	
						}
						htm.put("EMPNAME", employee);
						htrecv.put(IConstants.PLANT, PLANT);
						if (remarks.length() > 0) {
							htrecv.put(IConstants.REMARKS, "" + remarks + " " + employee);
						} else {
							htrecv.put(IConstants.REMARKS, "" + employee);
						}
						htrecv.put(IConstants.LOGIN_USER, (String) session.getAttribute("LOGIN_USER"));
						htrecv.put(IConstants.LOC, loc);
						miscutil.setmLogger(mLogger);
						salesdao.setmLogger(mLogger);

						String appenddisc = "";
						for (int j = 0; j < getlist.size(); j++) {
							ITEMMST recvproduct = getlist.get(j);

                          Quantity=String.valueOf(recvproduct.getStkqty());
                          pickingQty=Double.parseDouble(((String) Quantity.trim().toString()));
                          totalqty=totalqty+pickingQty;
							
							htm.put(IConstants.PLANT, PLANT);
							htm.put(IConstants.ITEM, recvproduct.getITEM());
							htm.put(IConstants.ITEM_DESC, strUtils.InsertQuotes(recvproduct.getITEMDESC()));
							htm.put(IConstants.BATCH, recvproduct.getBATCH());
							htm.put(IConstants.QTY, String.valueOf(recvproduct.getStkqty()));
							htm.put(IDBConstants.PRICE, "0");
							htm.put(IDBConstants.PAYMENTMODE, paymentmodes);
							htm.put("TranId", RecvtranID);
							htm.put("TranType", "GOODSRECEIPT");
							htm.put(IDBConstants.LOC, loc);
							htm.put(IDBConstants.POS_DISCOUNT, "0");
							htm.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
							htm.put(IDBConstants.CREATED_BY, login_user);
							htm.put(IConstants.UNITMO, recvproduct.getSTKUOM());
							htrecv.put(IConstants.ITEM, recvproduct.getITEM());
							htrecv.put(IConstants.QTY, String.valueOf(recvproduct.getStkqty()));
							htrecv.put(IConstants.BATCH, recvproduct.getBATCH());
							htrecv.put(IConstants.UOM, recvproduct.getSTKUOM());
							htrecv.put("TranId", RecvtranID);
							htrecv.put("DIRTYPE", "GOODSRECEIPT");
							// htrecv.put("DIRTYPE", "GOODS_RECEIVE");
							if(EXPIREDATE.equalsIgnoreCase("")){
								EXPIREDATE =recvproduct.getEXPREDATE();
							}
							htrecv.put("EXPIREDATE", EXPIREDATE);
							htrecv.put(IConstants.TRAN_TYPE, TRANTYPE);
							htrecv.put(IConstants.REFNO, refNo);
							htrecv.put(IConstants.CNAME, empID);
							htrecv.put(IConstants.RECVDATE, tranDate);
							htrecv.put(IConstants.EMPNO, empID);
							htrecv.put(IConstants.REASONCODE, rsncode);
							htrecv.put("USERFLD3", date);
							htrecv.put("TRANDATE", tran);
							htrecv.put(IConstants.LNNO, Integer.toString(j+1));
							nonstockflag = recvproduct.getNONSTOCKFLAG();
							// Deducting from inventory
							// String tranid=(String)session.getAttribute("tranid");
							htrecv.put(IConstants.MOVHIS_ORDNUM, RecvtranID);
							String UOMQTY="1";
							Hashtable<String, String> htTrand1 = new Hashtable<String, String>();
							MovHisDAO movHisDao1 = new MovHisDAO();
							ArrayList getuomqty = movHisDao1.selectForReport("select ISNULL(QPUOM,1) as UOMQTY from "+PLANT+"_UOM where UOM='"+String.valueOf(recvproduct.getSTKUOM())+"'",htTrand1);
							if(getuomqty.size()>0)
							{
							Map mapval = (Map) getuomqty.get(0);
							UOMQTY=(String)mapval.get("UOMQTY");
							}
							htrecv.put("UOMQTY", UOMQTY);
							try {
								// if(!posType.equalsIgnoreCase("NO_INV_CHECK")&&!nonstockflag.equalsIgnoreCase("Y")){
								if (!posType.equalsIgnoreCase("NO_INV_CHECK") && !nonstockflag.equalsIgnoreCase("Y")) {
									flag = miscutil.process_PosReceiveMaterial(htrecv);
									
									//POSDET Update Line No.
									Hashtable<String, String> htup = new Hashtable<String, String>();
									htup.put(IDBConstants.PLANT, PLANT);
									htup.put(IDBConstants.POS_TRANID, RecvtranID);
									htup.put(IDBConstants.POS_ITEM, recvproduct.getITEM());
									htup.put(IDBConstants.POS_BATCH, recvproduct.getBATCH());
									htup.put(IConstants.LOC, loc);
									htup.put(IConstants.UNITMO, recvproduct.getSTKUOM());
									htup.put(IConstants.QTY, String.valueOf(recvproduct.getStkqty()));
									String Qry="SET DOLNNO ='" + (j+1) +"'";
									posdet.update(Qry, htup, "");

									if (flag == false)
										errmsg = "Error In Updating Inventory Matser";
									// System.out.print("After issuing"+flag);
								} else {
									flag = processMovHisForPosWoInvCheck(htrecv);
									flag = processRecvDet(htrecv);
								}
								salesdao.setmLogger(mLogger);
								if (flag == true)
									flag = salesdao.insertIntoSalesDet(htm);
								// new TblControlUtil().updateTblControlSeqNo(PLANT,"GIRECEIPT","GI",tranID);
								
								if (flag == true) {//Shopify Inventory Update
	               					/*Hashtable htCond = new Hashtable();
	               					htCond.put(IConstants.PLANT, PLANT);
	               					if(new IntegrationsDAO().getShopifyConfigCount(htCond,"")) {
	               						String nonstkflag = new ItemMstDAO().getNonStockFlag(PLANT,recvproduct.getITEM());						
	            						if(nonstkflag.equalsIgnoreCase("N")) {
	               						String availqty ="0";
	               						ArrayList invQryList = null;
	               						htCond = new Hashtable();
	               						invQryList= new InvUtil().getInvListSummaryAvailableQtyforShopify(PLANT,recvproduct.getITEM(), new ItemMstDAO().getItemDesc(PLANT, recvproduct.getITEM()),htCond);						
	               						if (invQryList.size() > 0) {					
	               							for (int iCnt =0; iCnt<invQryList.size(); iCnt++){
	               								String result="";
	                                            Map lineArr = (Map) invQryList.get(iCnt);
	                                            availqty = (String)lineArr.get("AVAILABLEQTY");
	                                            System.out.println(availqty);
	               							}
	               							double availableqty = Double.parseDouble(availqty);
	                   						new ShopifyService().UpdateShopifyInventoryItem(PLANT, recvproduct.getITEM(), availableqty);
	               						}	
	            						}
	               					}*/
									Thread apiInvUpdateJob = new Thread(new ApiInventoryUpdateJob(recvproduct.getITEM(), PLANT));
	               					apiInvUpdateJob.start();
	               				}
								
							} catch (Exception e) {
								flag = false;
								DbBean.RollbackTran(ut);
								throw e;

							}

						}
						if (flag == true){
					    	 RecvDetDAO recvdetDao = new RecvDetDAO();
					    	 recvdetDao.setmLogger(mLogger);
					    	 Hashtable htRecvDet = new Hashtable();
					    	 DateUtils dateutils = new DateUtils();
								htRecvDet.clear();
								htRecvDet.put(IConstants.PLANT, PLANT);
								htRecvDet.put(IConstants.GRNO,RecvtranID);
								htRecvDet.put(IConstants.VENDOR_CODE, "");
								htRecvDet.put(IConstants.STATUS, "NON BILLABLE");
								htRecvDet.put(IConstants.PODET_PONUM, "");
								htRecvDet.put(IConstants.AMOUNT, "");
								htRecvDet.put(IConstants.QTY, String.valueOf(totalqty));
								htRecvDet.put("CRAT", orderdate);
//								htRecvDet.put("CRAT",dateutils.getDateTime());
								htRecvDet.put("CRBY", login_user);
								htRecvDet.put("UPAT",dateutils.getDateTime());
					
								flag = recvdetDao.insertGRNtoBill(htRecvDet);
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
			        			htRecvHis.put(IConstants.QTY,String.valueOf(totalqty));
			        			htRecvHis.put(IDBConstants.CREATED_BY, login_user);
			        			htRecvHis.put(IDBConstants.REMARKS, "");        					
			        			htRecvHis.put(IDBConstants.TRAN_DATE, dateutils.getDateinyyyy_mm_dd(dateutils.getDate()));
			        			htRecvHis.put(IDBConstants.MOVHIS_ORDNUM, RecvtranID);
			        			htRecvHis.put(IDBConstants.CREATED_AT, dateutils.getDateTime());
			        			flag = movHisDao.insertIntoMovHis(htRecvHis);
						}
						if (flag == true)
							
							// update POSHDR AND PODET To Close Status
							getlist.removeAllElements();
						Hashtable<String, String> ht = new Hashtable<String, String>();
						ht.put(IDBConstants.PLANT, PLANT);
						ht.put(IDBConstants.POS_TRANID, RecvtranID);
						String QryUpdate = " SET STATUS ='C',RECEIPTNO='" + RecvtranID + "',UPAT ='"
								+ DateUtils.getDate() + "' ";
						boolean posHsrUpt = posHdr.update(QryUpdate, ht, "");
						if (posHsrUpt) {
							QryUpdate = " SET TRANSTATUS ='C',UPAT ='" + DateUtils.getDate() + "' ";
							posdet.update(QryUpdate, ht, "");
						}
/*						new TblControlUtil().updateTblControlSeqNoWithoutorder(PLANT, "GRRECEIPT", "GR");
						session.setAttribute("recvlist", getlist);*/
						new TblControlUtil().updateTblControlIESeqNo(PLANT,"GRN","GN",RecvtranID);
						session.setAttribute("recvlist", getlist);
						// session.setAttribute("poslist", buylist);
						DbBean.CommitTran(ut);

						if (flag == false) {
							DbBean.RollbackTran(ut);
							session.setAttribute("errmsg", errmsg);
							RequestDispatcher view = request.getRequestDispatcher("jsp/" + posPageToDirect
									+ "?RECVTRANID=" + RecvtranID + "&CHKBATCH=" + batchChecked);

							view.forward(request, response);
						} else {
							POSUtil posUtil = new POSUtil();
							Map mhdr = posUtil.getPosReceiveReceiptHdrDetails(PLANT);
							String download = (String) mhdr.get("DOWNLOADPDF");
							// viewPOSReport(request, response, rcpNo);
							session.setAttribute("errmsg", "");
							getlist.removeAllElements();
							session.setAttribute("recvlist", getlist);
							response.setContentType("text/html");
							PrintWriter out = response.getWriter();
							out.write("Success:" + RecvtranID + ":" + download);
							out.close();

						}

					} catch (Exception e) {
						session.setAttribute("errmsg", e.getMessage());
						RequestDispatcher view = request.getRequestDispatcher(
								"jsp/" + posPageToDirect + "?RECVTRANID=" + RecvtranID + "&CHKBATCH=" + batchChecked);
						view.forward(request, response);
					}
				} else {
					viewReceiveProductReport(request, response, RecvtranID);
				}
			} else if (cmdAction.equalsIgnoreCase("MOVENEWTRANID")) {
				// New Stock Move Transaction
				com.track.dao.TblControlDAO _TblControlDAO = new com.track.dao.TblControlDAO();
				_TblControlDAO.setmLogger(mLogger);
				String genPosTranid = _TblControlDAO.getNextOrder(PLANT, login_user, "STOCKMOVE");
				if (buylist != null) {
					buylist.removeAllElements();
					session.setAttribute("poslist", buylist);
					session.setAttribute("poslist", null);
					session.setAttribute("errmsg", "");
				}
				session.setAttribute("LOCATION", loc);
//				response.sendRedirect(
//						"../inhouse/stockmove" + "?TRANID=" + genPosTranid + "&LOC=" + loc + "&BATCH=NOBATCH");
				response.sendRedirect(
						"jsp/" + posPageToDirect + "?TRANID=" + genPosTranid + "&LOC=" + loc + "&BATCH=NOBATCH");
			} 
			
//			COMMENTED BY IMTHI BELOW CODE IS FOR OLD STOCK MOVE

//			else if (cmdAction.equalsIgnoreCase("ADD") && TRANTYPE.equalsIgnoreCase("MOVEWITHBATCH")) {
//
//				String expireDate = "", refno = "";
//				refno = request.getParameter("REFERENCENO");
//				String cmd="ADD";
//				String empID = StrUtils.fString(request.getParameter("EMP_ID")).trim();
//				String refNo = StrUtils.fString(request.getParameter("REFERENCENO")).trim();
//
//				String serialized = StrUtils.fString(request.getParameter("serialized")).trim();
//				String defualtQty = StrUtils.fString(request.getParameter("defaultQty")).trim();
//				String bulkcheckout = StrUtils.fString(request.getParameter("bulkcheckout")).trim();
//				String tranDate = StrUtils.fString(request.getParameter("TRANSACTIONDATE")).trim();
//				String holdReasonCode = StrUtils.fString(request.getParameter("REASONCODE")).trim();
//				String holdRemarks = StrUtils.fString(request.getParameter("REMARKS")).trim();
//				String uom = StrUtils.fString(request.getParameter("UOM")).trim();
//				String empName = StrUtils.fString(request.getParameter("EMP_NAME")).trim();
//
//				loc = StrUtils.fString(request.getParameter("FROM_LOC")).trim();
//				String fromlocdesc = StrUtils.fString(request.getParameter("FROM_LOCDESC")).trim();
//
//				String to_LOC = StrUtils.fString(request.getParameter("TOLOC")).trim();
//				String tolocdesc = StrUtils.fString(request.getParameter("TOLOCDESC")).trim();
//				if (loc.length() > 0) {
//
//					UserLocUtil uslocUtil = new UserLocUtil();
//					uslocUtil.setmLogger(mLogger);
//					boolean isvalidlocforUser = uslocUtil.isValidLocInLocmstForUser(PLANT, login_user, loc);
//
//					if (isvalidlocforUser) {
//						isvalidlocforUser = uslocUtil.isValidLocInLocmstForUser(PLANT, login_user, to_LOC);
//						if (!isvalidlocforUser) {
//
//							session.setAttribute("errmsg",
//									"To LOC : " + to_LOC + " is not a User Assigned Location/Valid Location ");
//							response.sendRedirect("jsp/" + posPageToDirect + "?FROM_LOC=" + loc + "&TO_LOC=" + to_LOC
//									+ "&serialized=" + serialized + "&defualtQty=" + defualtQty + "&bulkcheckout="
//									+ bulkcheckout);
//
//						} else {
//
//							if (item.length() > 0) {
//								boolean itemFound = true;
//								ItemMstDAO itM = new ItemMstDAO();
//								itM.setmLogger(mLogger);
//								String scannedItemNo = itM.getItemIdFromAlternate(PLANT, item);
//								if (scannedItemNo == null) {
//									itemFound = false;
//								} else {
//									item = scannedItemNo;
//								}
//								if (itemFound) {
//
//									ITEMMST items = getItemDetails(request, item, scqty, batch);
//									items.setBATCHID(batchId);
//									ITEMMST prodbean1 = items;
//									nonstockflag = prodbean1.getNONSTOCKFLAG();
//									items.setEmpNo(empID);
//									items.setEmpName(empName);
//									items.setRefNo(refNo);
//									items.setTranDate(tranDate);
//									items.setReasonCode(reasoncode);
//									items.setRemarks(remarks);
//									items.setLocDesc(fromlocdesc);
//									items.setToLocDesc(tolocdesc);
//									items.setSTKUOM(uom);
//									Hashtable ht1 = new Hashtable();
//									ht1.put(IConstants.PLANT, PLANT);
//									ht1.put(IConstants.LOGIN_USER, (String) session.getAttribute("LOGIN_USER"));
//									ht1.put(IConstants.LOC, loc);
//									ht1.put(IConstants.ITEM, prodbean1.getITEM());
//									ht1.put(IConstants.BATCH, prodbean1.getBATCH());
//									if (prodbean1.getBATCHID() != -1) {
//										ht1.put(IConstants.BATCH_ID, String.valueOf(prodbean1.getBATCHID()));
//									}
//									String stkqty1 = String.valueOf(prodbean1.getStkqty());
//									//stkqty1 = StrUtils.TrunkateDecimalForImportData(stkqty1);
//									String UOMQTY="1";
//									Hashtable<String, String> htTrand1 = new Hashtable<String, String>();
//									MovHisDAO movHisDao1 = new MovHisDAO();
//									ArrayList getuomqty = movHisDao1.selectForReport("select ISNULL(QPUOM,1) as UOMQTY from "+PLANT+"_UOM where UOM='"+uom+"'",htTrand1);
//									if(getuomqty.size()>0)
//									{
//									Map mapval = (Map) getuomqty.get(0);
//									UOMQTY=(String)mapval.get("UOMQTY");
//									}
//									double invumqty = Double.valueOf(stkqty1) * Double.valueOf(UOMQTY);
//									ht1.put(IConstants.QTY, String.valueOf(invumqty));									
//									nonstockflag = prodbean1.getNONSTOCKFLAG();
//									if (!nonstockflag.equalsIgnoreCase("Y")) {
//										boolean invAvailable = false;
//										try {
//											invAvailable = CheckInvMstForGoddsIssue(ht1, request, response);
//
//										} catch (Exception e) {
//											InvFlag = true;
//											session.setAttribute("errmsg", "Not Enough Inventory For Product:"
//													+ prodbean1.getITEM() + "  with batch:" + prodbean1.getBATCH());
//											String msg = session.getAttribute("errmsg").toString();
//											session.setAttribute("poslist", buylist);
//											e.printStackTrace(new PrintWriter(msg));
//											throw e;
//										}
//
//										if (!invAvailable) {
//											session.setAttribute("errmsg", "Not Enough Inventory For Product:"
//													+ prodbean1.getITEM() + "  with batch:" + prodbean1.getBATCH());
//											String msg = session.getAttribute("errmsg").toString();
//											session.setAttribute("poslist", buylist);
//											response.sendRedirect("jsp/" + posPageToDirect + "?TRANID=" + tranID
//													+ "&CHKBATCH=" + batchChecked + "&FROM_LOC=" + loc
//													+ "&FROM_LOCDESC=" + fromlocdesc + "&TOLOCDESC=" + tolocdesc
//													+ "&TO_LOC=" + to_LOC + "&serialized=" + serialized + "&defualtQty="
//													+ defualtQty + "&bulkcheckout=" + bulkcheckout + "&BATCH=NOBATCH"+"&cmd=" + cmd);
//
//											return;
//										}
//
//									}
//
//									if (buylist == null) {
//										match = false;
//										buylist = new Vector<ITEMMST>();
//										buylist.addElement(items);
//										// To check transaction already exists in POSHeader
//										Hashtable<String, String> htTrandId = new Hashtable<String, String>();
//										htTrandId.put("POSTRANID", tranID);
//										boolean istranidExist = _posServlet.isExisit(PLANT, htTrandId);
//										boolean istranidExist1 = _posServlet.isRecvtranIDExisit(PLANT, htTrandId);
//										if (istranidExist1 == true) {
//											try {
//												throw new Exception("Tran Id : " + tranID + " Exists already! ");
//											} catch (Exception e) {
//												session.setAttribute("errmsg",
//														"Tran Id : " + tranID + " Exists already! ");
//												String msg = session.getAttribute("errmsg").toString();
//												e.printStackTrace(new PrintWriter(msg));
//												throw e;
//											}
//										}
//										if (istranidExist == false) {
//											String genPosTranid = poshdr.genPosManualTranId(PLANT, login_user, loc,
//													tranID);
//										}
//										// To check transaction already exists in POSHeader end
//										Hashtable<String, String> ht = new Hashtable<String, String>();
//										ht.put(IDBConstants.PLANT, PLANT);
//										ht.put(IDBConstants.POS_TRANID, tranID);
//										String QryUpdate = " SET STATUS ='O',UPAT ='" + DateUtils.getDate()
//												+ "',EMPNO='" + empID + "',REFNO='" + refNo + "',TRANDT='" + tranDate
//												+ "',RSNCODE='" + holdReasonCode + "',REMARKS='" + holdRemarks
//												+ "',LOC='" + loc + "',TOLOC='" + to_LOC + "'";
//										boolean posHsrUpt = posHdr.update(QryUpdate, ht, "");
//										if (posHsrUpt) {
//
//											ht.put(IDBConstants.POS_ITEM, items.getITEM());
//											ht.put(IDBConstants.POS_ITEMDESC,
//													strUtils.InsertQuotes(items.getITEMDESC()));
//											ht.put(IDBConstants.POS_QTY, String.valueOf(items.getStkqty()));
//											ht.put(IDBConstants.POS_LOC, loc);
//											ht.put(IDBConstants.POS_TOLOC, to_LOC);
//											ht.put(IDBConstants.POSDET_STATUS, "N");
//											ht.put(IDBConstants.POS_UNITPRICE, "0");
//											ht.put(IDBConstants.POS_TOTALPRICE, "0");
//											ht.put(IDBConstants.POS_TRANDT, DateUtils.getDate());
//											ht.put(IDBConstants.POS_TRANTM, DateUtils.Time());
//											ht.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
//											ht.put(IDBConstants.CREATED_BY, login_user);
//											ht.put(IDBConstants.POS_BATCH, batch);
//											ht.put("POSTYPE", TRANTYPE);
//											ht.put(IConstants.UNITMO,uom);
//											ht.put(IConstants.OUT_DOLNNO, Integer.toString(1));
//											
//											try {
//												//Check POS_DET Data Already Exists - Azees 4.8.19
//												Hashtable<String, String> htup = new Hashtable<String, String>();
//												htup.put(IDBConstants.PLANT, PLANT);
//												htup.put(IDBConstants.POS_TRANID, tranID);
//												htup.put(IDBConstants.POS_ITEM, items.getITEM());
//												htup.put(IDBConstants.POS_BATCH, batch);											
//												htup.put(IDBConstants.LOC, loc);
//												htup.put(IConstants.UNITMO,uom);																				
//												boolean chkpos = posdet.isExisit(htup,"");
//												if(chkpos==false)
//												{
//												posdet.insertIntoPosDet(ht);
//												}
//												if (!istranidExist) {
//													new TblControlUtil().updateTblControlSeqNoWithoutorder(PLANT,
//															"STOCKMOVE", "SM");
//												}
//
//											} catch (Exception e) {
//												session.setAttribute("errmsg",
//														"Tran Id : " + tranID + " Exists already! ");
//												String msg = session.getAttribute("errmsg").toString();
//												e.printStackTrace(new PrintWriter(msg));
//												throw e;
//											}
//										}
//
//									} else {
//										//Azees Udated 16.11.18
//										int matchnew=0; 
//										Boolean flag=true;
//										
//										for (int i = 0; i < buylist.size(); i++) {
//
//											ITEMMST itembean = buylist.elementAt(i);
//
//											if (itembean.getITEM().equalsIgnoreCase(items.getITEM())
//													&& itembean.getBATCH().equalsIgnoreCase(items.getBATCH()) && itembean.getSTKUOM().equalsIgnoreCase(items.getSTKUOM())) {
//												if (serialized.equalsIgnoreCase("1")) {
//													session.setAttribute("errmsg",
//															"Duplicates are not allowed when serialized");
//													match = true;
//												} else {
//													/*if (scqty > 1) {
//														itembean.setStkqty((itembean.getStkqty() + scqty));
//														scqty = itembean.getStkqty();
//													} else {
//														itembean.setStkqty((itembean.getStkqty() + 1));
//														scqty = itembean.getStkqty();
//													}
//
//													buylist.setElementAt(itembean, i);
//													match = true;
//													Hashtable<String, String> ht = new Hashtable<String, String>();
//													ht.put(IDBConstants.PLANT, PLANT);
//													ht.put(IDBConstants.POS_TRANID, tranID);
//													ht.put(IDBConstants.POS_ITEM, items.getITEM());
//													ht.put(IDBConstants.POS_BATCH, items.getBATCH());
//													String Qry = " SET QTY ='" + itembean.getStkqty() + "'";
//													posdet.update(Qry, ht, "");
//													// update Qty for Same Item
//													*/	
//													match = true;
//													if(itembean.getBATCHID() == items.getBATCHID())
//													{
//														float scqty1=  itembean.getStkqty() + scqty;
//														double invuomqty = Double.valueOf(scqty1) * Double.valueOf(UOMQTY);
//															
//														
//														
//														ht1.put(IConstants.ITEM, items.getITEM());
//														ht1.put(IConstants.BATCH, items.getBATCH());
//														if (prodbean1.getBATCHID() != -1) {
//															ht1.put(IConstants.BATCH_ID, String.valueOf(items.getBATCHID()));
//														}
//														
//														
//														ht1.put(IConstants.QTY,  String.valueOf(invuomqty));
//														
//														flag = CheckInvMstForGoddsIssue(ht1, request, response);
//														if (flag == false) {
//															
//																	errmsg = "Not Enough Inventory";
//																	break;
//																}
//														else
//														{
//															if (scqty > 1) {
//															    itembean.setStkqty((itembean.getStkqty() + scqty));
//															    
//															} else {
//																itembean.setStkqty((itembean.getStkqty() + 1));	
//																
//															}
//															
//															buylist.setElementAt(itembean, i);
//														}
//													     
//													}
//													else
//													{
//														Boolean found = false;
//														 for(int j=0;j<buylist.size();j++)
//														 {
//															 ITEMMST tester=buylist.elementAt(j);
//													           if(tester.getBATCHID()==items.getBATCHID()){
//													        	   
//													        	   found=true;
//													                break;
//													            }
//														 }
//														 if(!found)
//														 {
//															if(matchnew==0)
//															{
//																matchnew=1;
//															}
//														 }
//													}
//													
//													}
//													
//												
//											}
//										}
//										if(!flag)
//										{
//											session.setAttribute("errmsg", "Not Enough Inventory For Product:"
//													+ prodbean1.getITEM() + "  with batch:" + prodbean1.getBATCH());
//											String msg = session.getAttribute("errmsg").toString();
//											session.setAttribute("poslist", buylist);
//											
//											response.sendRedirect("jsp/" + posPageToDirect + "?TRANID=" + tranID
//													+ "&CHKBATCH=" + batchChecked + "&LOC=" + loc);
//											return;
//										
//									}
//									if(matchnew==1)
//									{
//										buylist.addElement(items);
//									}
//									for (int i = 0; i < buylist.size(); i++) {
//
//										ITEMMST itembean = buylist.elementAt(i);
//										if (itembean.getITEM().equalsIgnoreCase(items.getITEM())
//												&& itembean.getBATCH().equalsIgnoreCase(items.getBATCH()) && itembean.getSTKUOM().equalsIgnoreCase(items.getSTKUOM())) {
//										match = true;
//										Hashtable<String, String> ht = new Hashtable<String, String>();
//										ht.put(IDBConstants.PLANT, PLANT);
//										ht.put(IDBConstants.POS_TRANID, tranID);
//										ht.put(IDBConstants.POS_ITEM, items.getITEM());
//										ht.put(IDBConstants.POS_BATCH, items.getBATCH());
//										String Qry="";
//										if(i == 0)
//										{
//										Qry = " SET QTY ='0'";
//										posdet.update(Qry, ht, "");
//												}
//												Qry = " SET QTY =QTY+'" + itembean.getStkqty() + "'";
//										posdet.update(Qry, ht, "");
//										
//									}
//											}
//										
//										
//										   if (!match) {
//											buylist.addElement(items);
//											// Insert into posdet with status as N
//											Hashtable<String, String> ht = new Hashtable<String, String>();
//											ht.put(IDBConstants.PLANT, PLANT);
//											ht.put(IDBConstants.POS_TRANID, tranID);
//											ht.put(IDBConstants.POS_ITEM, items.getITEM());
//											ht.put(IDBConstants.POS_ITEMDESC,
//													strUtils.InsertQuotes(items.getITEMDESC()));
//											ht.put(IDBConstants.POS_QTY, String.valueOf(items.getStkqty()));
//											ht.put(IDBConstants.POS_LOC, loc);
//											ht.put(IDBConstants.POS_DISCOUNT, "0");
//											ht.put(IDBConstants.POSDET_STATUS, "N");
//											ht.put(IDBConstants.POS_UNITPRICE, "0");
//											ht.put(IDBConstants.POS_TOTALPRICE, "0");
//											ht.put(IDBConstants.POS_TRANDT, DateUtils.getDate());
//											ht.put(IDBConstants.POS_TRANTM, DateUtils.Time());
//											ht.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
//											ht.put(IDBConstants.CREATED_BY, login_user);
//											ht.put(IDBConstants.POS_BATCH, batch);
//											// ht.put(IDBConstants.REFNO,refno);
//											ht.put("POSTYPE", TRANTYPE);
//											ht.put(IConstants.UNITMO,uom);
//											ht.put(IConstants.OUT_DOLNNO, Integer.toString(buylist.size()));
//											try {
//
//												Hashtable<String, String> htTrandId = new Hashtable<String, String>();
//												htTrandId.put("POSTRANID", tranID);
//												boolean istranidExist = _posServlet.isExisit(PLANT, htTrandId);
//												boolean istranidExist1 = _posServlet.isRecvtranIDExisit(PLANT,
//														htTrandId);
//												if (istranidExist1 == true) {
//
//													throw new Exception();
//												}
//												posdet.insertIntoPosDet(ht);
//												if (!istranidExist) {
//													new TblControlUtil().updateTblControlSeqNoWithoutorder(PLANT,
//															"STOCKMOVE", "SM");
//												}
//
//											} catch (Exception e) {
//												getlist.remove(items);
//												session.setAttribute("errmsg",
//														"Tran Id : " + tranID + " Exists already! ");
//												String msg = session.getAttribute("errmsg").toString();
//												e.printStackTrace(new PrintWriter(msg));
//												throw e;
//											}
//										}
//									}
//									session.setAttribute("poslist", buylist);
//									session.setAttribute("LOCATION", loc);
//									if (!posType.equalsIgnoreCase("NO_INV_CHECK")) {
//										boolean checkarry[] = new boolean[buylist.size()];
//										int index = 0;
//										Vector newbuylst = (Vector) session.getAttribute("poslist");
//										// Check qty exists in inventory
//										for (int i = 0; i < newbuylst.size(); i++) {
//											Hashtable ht = new Hashtable();
//											ITEMMST prodbean = (ITEMMST) newbuylst.elementAt(i);
//
//											ht.put(IConstants.PLANT, PLANT);
//											ht.put(IConstants.LOGIN_USER, (String) session.getAttribute("LOGIN_USER"));
//											ht.put(IConstants.LOC, loc);
//											ht.put(IConstants.ITEM, prodbean.getITEM());
//											ht.put(IConstants.BATCH, prodbean.getBATCH());
//											String stkqty = String.valueOf(prodbean.getStkqty());
//											//stkqty = StrUtils.TrunkateDecimalForImportData(stkqty);
//											ht.put(IConstants.QTY, stkqty);
//											nonstockflag = prodbean.getNONSTOCKFLAG();
//											if (!nonstockflag.equalsIgnoreCase("Y")) {
//												checkarry[index] = CheckInvMst(ht, request, response);
//												try {
//													checkarry[index] = CheckInvMst(ht, request, response);
//
//												} catch (Exception e) {
//													InvFlag = true;
//													session.setAttribute("errmsg",
//															"Not Enough Inventory For Product:" + prodbean.getITEM()
//																	+ "  with batch:" + prodbean.getBATCH());
//													String msg = session.getAttribute("errmsg").toString();
//													session.setAttribute("poslist", buylist);
//													e.printStackTrace(new PrintWriter(msg));
//													throw e;
//												}
//
//											}
//
//											if (checkarry[index] == true) {
//												response.sendRedirect("jsp/" + posPageToDirect + "?TRANID=" + tranID
//														+ "&CHKBATCH=" + batchChecked + "&LOC=" + loc + "&TOLOC="
//														+ to_LOC + "&serialized=" + serialized + "&EMP_ID=" + empID
//														+ "&REFERENCENO=" + refNo + "&serialized=" + serialized
//														+ "&defualtQty=" + defualtQty + "&bulkcheckout=" + bulkcheckout
//														+ "&TRANSACTIONDATE=" + tranDate + "&REASONCODE="
//														+ holdReasonCode + "&REMARKS=" + holdRemarks + "&FROM_LOCDESC="
//														+ fromlocdesc + "&TOLOCDESC=" + tolocdesc + "&EMP_NAME="
//														+ empName + "&BATCH=NOBATCH"+"&cmd=" + cmd);
//											}
//
//											index++;
//										}
//									}
//								} else {
//									session.setAttribute("errmsg", "Product Id not found");
//								}
//
//							}
//							response.sendRedirect("jsp/" + posPageToDirect + "?TRANID=" + tranID + "&CHKBATCH="
//									+ batchChecked + "&FROM_LOC=" + loc + "&TO_LOC=" + to_LOC + "&serialized="
//									+ serialized + "&EMP_ID=" + empID + "&REFERENCENO=" + refNo + "&serialized="
//									+ serialized + "&defualtQty=" + defualtQty + "&bulkcheckout=" + bulkcheckout
//									+ "&TRANSACTIONDATE=" + tranDate + "&REASONCODE=" + holdReasonCode + "&REMARKS="
//									+ holdRemarks + "&FROM_LOCDESC=" + fromlocdesc + "&TOLOCDESC=" + tolocdesc
//									+ "&EMP_NAME=" + empName + "&BATCH=NOBATCH"+"&cmd=" + cmd);
//
//						}
//					} else {
//						session.setAttribute("errmsg",
//								"From LOC : " + loc + " is not a User Assigned Location/Valid Location ");
//						response.sendRedirect("jsp/" + posPageToDirect + "?TRANID=" + tranID + "&CHKBATCH="
//								+ batchChecked + "&FROM_LOC=" + loc + "&TO_LOC=" + to_LOC + "&serialized=" + serialized
//								+ "&EMP_ID=" + empID + "&EMP_NAME=" + empName + "&REFERENCENO=" + refNo + "&serialized="
//								+ serialized + "&defualtQty=" + defualtQty + "&bulkcheckout=" + bulkcheckout
//								+ "&TRANSACTIONDATE=" + tranDate + "&REASONCODE=" + holdReasonCode + "&REMARKS="
//								+ holdRemarks + "&FROM_LOCDESC=" + fromlocdesc + "&TOLOCDESC=" + tolocdesc
//								+ "&BATCH=NOBATCH"+"&cmd=" + cmd);
//
//					}
//				} else {
//					session.setAttribute("errmsg", "Please Enter Location");
//					// response.sendRedirect("jsp/"+posPageToDirect+"&LOC="+loc);
//					// RequestDispatcher view =
//					// request.getRequestDispatcher("jsp/"+posPageToDirect+"?FROM_LOC="+loc+"&TO_LOC="+to_LOC);
//					// view.forward(request, response);
//					response.sendRedirect("jsp/" + posPageToDirect + "?TRANID=" + tranID + "&CHKBATCH=" + batchChecked
//							+ "&FROM_LOC=" + loc + "&TO_LOC=" + to_LOC + "&serialized=" + serialized + "&EMP_ID="
//							+ empID + "&EMP_NAME=" + empName + "&REFERENCENO=" + refNo + "&serialized=" + serialized
//							+ "&defualtQty=" + defualtQty + "&bulkcheckout=" + bulkcheckout + "&TRANSACTIONDATE="
//							+ tranDate + "&REASONCODE=" + holdReasonCode + "&REMARKS=" + holdRemarks + "&EMP_NAME="
//							+ empName + "&FROM_LOCDESC=" + fromlocdesc + "&TOLOCDESC=" + tolocdesc + "&BATCH=NOBATCH"+"&cmd=" + cmd);
//				}
//
//			}
			
// END OF COMMENT FOR OLD STOCK MOVE SAVE			
			
			else if (cmdAction.equalsIgnoreCase("delete")) {
				String fromloc = "";
				String expireDate = "", refno = "";
				refno = request.getParameter("REFERENCENO");
				String cmd = StrUtils.fString(request.getParameter("cmd")).trim();
				String empID = StrUtils.fString(request.getParameter("EMP_ID")).trim();
				String refNo = StrUtils.fString(request.getParameter("REFERENCENO")).trim();
				String serialized = StrUtils.fString(request.getParameter("serialized")).trim();
				String tranDate = StrUtils.fString(request.getParameter("TRANSACTIONDATE")).trim();
				String holdReasonCode = StrUtils.fString(request.getParameter("REASONCODE")).trim();
				String holdRemarks = StrUtils.fString(request.getParameter("REMARKS")).trim();
				String empName = StrUtils.fString(request.getParameter("EMP_NAME")).trim();
				loc = StrUtils.fString(request.getParameter("FROM_LOC")).trim();
				String fromlocdesc = StrUtils.fString(request.getParameter("FROM_LOCDESC")).trim();
				String to_LOC = StrUtils.fString(request.getParameter("TOLOC")).trim();
				String tolocdesc = StrUtils.fString(request.getParameter("TOLOCDESC")).trim();
				session.setAttribute("RESULTTOTALDISCOUNT", "");
				session.setAttribute("TOTALDISCOUNT", "");
				session.setAttribute("TOTALSUBTOTAL", "");
				session.setAttribute("TOTALTAX", "");
				session.setAttribute("CALCDISCOUNT", "");
				int iCnt = 0;
				String appenddisc = "";
				String[] chkvalues = request.getParameterValues("chk");
				int indexarray[] = new int[chkvalues.length];
				Vector<ITEMMST> updatelist = new Vector<ITEMMST>();
				loc = StrUtils.fString(request.getParameter("FROM_LOC")).trim();
				fromloc = StrUtils.fString(request.getParameter("LOC")).trim();
				if (loc.length() > 0 || fromloc.length() > 0) {

					if (chkvalues.length > 0) {

						for (int j = 0; j < chkvalues.length; j++) {
							indexarray[iCnt] = Integer.parseInt(chkvalues[iCnt]);
							iCnt++;
						}
						String ItemInPosforTranId = "";
						String BatchInPosforTranId = "";
						String UOMInPosforTranId = "";
						for (int j = 0; j < buylist.size(); j++) {
							int index = Arrays.binarySearch(indexarray, j);
							if (index < 0) {
								ITEMMST itembeans = buylist.get(j);
								ITEMMST additem = new ITEMMST();
								additem.setITEM(itembeans.getITEM());
								additem.setITEMDESC(itembeans.getITEMDESC());
								additem.setUNITPRICE(itembeans.getUNITPRICE());
								additem.setStkqty(itembeans.getStkqty());
								additem.setDISCOUNT(itembeans.getDISCOUNT());
								additem.setTotalPrice(itembeans.getTotalPrice());
								additem.setGSTTAX(itembeans.getGSTTAX());
								additem.setNONSTOCKFLAG(itembeans.getNONSTOCKFLAG());// added by radhika
								additem.setBATCH(itembeans.getBATCH());
								additem.setSTKUOM(itembeans.getSTKUOM());
								updatelist.add(additem);

							} else {
								ITEMMST itembeans = buylist.get(j);
								if (ItemInPosforTranId.length() > 0) {
									ItemInPosforTranId = ItemInPosforTranId + "," + "'" + itembeans.getITEM() + "'";
								} else {
									ItemInPosforTranId = "'" + itembeans.getITEM() + "'";
								}

								if (BatchInPosforTranId.length() > 0) {
									BatchInPosforTranId = BatchInPosforTranId + "," + "'" + itembeans.getBATCH() + "'";
								} else {
									BatchInPosforTranId = "'" + itembeans.getBATCH() + "'";
								}
								
								if (UOMInPosforTranId.length() > 0) {
									UOMInPosforTranId = UOMInPosforTranId + "," + "'" + itembeans.getSTKUOM() + "'";
								} else {
									UOMInPosforTranId = "'" + itembeans.getSTKUOM() + "'";
								}
								
							}

						}

					//	posdet.deleteProductForTranId(PLANT, ItemInPosforTranId, tranID, BatchInPosforTranId);
						posdet.deleteProductUOMForTranId(PLANT, ItemInPosforTranId, tranID, BatchInPosforTranId,UOMInPosforTranId);
						buylist.removeAllElements();
						session.setAttribute("poslist", updatelist);
					}

					session.setAttribute("errmsg", "");
				} else {
					session.setAttribute("errmsg", "Please select checkbox");
				}
				String defualtQty = StrUtils.fString(request.getParameter("defaultQty")).trim();
				String bulkcheckout = StrUtils.fString(request.getParameter("bulkcheckout")).trim();

				response.sendRedirect("jsp/" + posPageToDirect + "?TRANID=" + tranID + "&CHKBATCH=" + batchChecked
						+ "&FROM_LOC=" + loc + "&TO_LOC=" + to_LOC + "&serialized=" + serialized + "&EMP_ID=" + empID
						+ "&REFERENCENO=" + refNo + "&serialized=" + serialized + "&defualtQty=" + defualtQty
						+ "&bulkcheckout=" + bulkcheckout + "&TRANSACTIONDATE=" + tranDate + "&REASONCODE="
						+ holdReasonCode + "&REMARKS=" + holdRemarks + "&FROM_LOCDESC=" + fromlocdesc + "&TOLOCDESC="
						+ tolocdesc + "&EMP_NAME=" + empName + "&BATCH=NOBATCH" + "&cmd=" + cmd);
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

			if (action.equalsIgnoreCase("ADDRECVPRODUCT") && InvFlag == false) {

				session.setAttribute("errormsg", "");
				// response.sendRedirect("jsp/"+posPageToDirect+"?TRANID="+request.getParameter("TRANID")+"&CHKBATCH="+batchChecked+"&LOC="+request.getParameter("LOC"));
				RequestDispatcher view = request.getRequestDispatcher("jsp/" + posPageToDirect + "?TRANID="
						+ request.getParameter("TRANID") + "&CHKBATCH=" + batchChecked + "&LOC="
						+ request.getParameter("LOC") + "&TOLOC=" + request.getParameter("TOLOC") + "&FROM_LOCDESC="
						+ request.getParameter("FROM_LOCDESC") + "&TOLOCDESC=" + request.getParameter("TOLOCDESC"));
				view.forward(request, response);
			}
			if (action.equalsIgnoreCase("ADDSTOCKMOVEPRODUCT") && InvFlag == true) {
				session.setAttribute("errormsg", "");
			}

			if (action.equalsIgnoreCase("ADDSTOCKMOVEPRODUCT") && InvFlag == false) {

				session.setAttribute("errormsg", "");
				// response.sendRedirect("jsp/"+posPageToDirect+"?TRANID="+request.getParameter("TRANID")+"&CHKBATCH="+batchChecked+"&LOC="+request.getParameter("LOC"));
				RequestDispatcher view = request.getRequestDispatcher("jsp/" + posPageToDirect + "?TRANID="
						+ request.getParameter("TRANID") + "&CHKBATCH=" + batchChecked + "&LOC="
						+ request.getParameter("LOC") + "&TOLOC=" + request.getParameter("TOLOC") + "&FROM_LOCDESC="
						+ request.getParameter("FROM_LOCDESC") + "&TOLOCDESC=" + request.getParameter("TOLOCDESC"));
				view.forward(request, response);
			}
			if (action.equalsIgnoreCase("ADDBULKSTOCKMOVEPRODUCT") && InvFlag == true) {
				session.setAttribute("errormsg", "");
			}

			if (action.equalsIgnoreCase("ADDBULKSTOCKMOVEPRODUCT") && InvFlag == false) {

				session.setAttribute("errormsg", "");
				// response.sendRedirect("jsp/"+posPageToDirect+"?TRANID="+request.getParameter("TRANID")+"&CHKBATCH="+batchChecked+"&LOC="+request.getParameter("LOC"));
				RequestDispatcher view = request.getRequestDispatcher("jsp/" + posPageToDirect + "?TRANID="
						+ request.getParameter("TRANID") + "&CHKBATCH=" + batchChecked + "&LOC="
						+ request.getParameter("LOC") + "&TOLOC=" + request.getParameter("TOLOC") + "&FROM_LOCDESC="
						+ request.getParameter("FROM_LOCDESC") + "&TOLOCDESC=" + request.getParameter("TOLOCDESC"));
				view.forward(request, response);
			}
			
			if (cmdAction.equalsIgnoreCase("ADD") && InvFlag == false) {
				session.setAttribute("errormsg", "");
				session.setAttribute("RESULTTOTALDISCOUNT", "");
				session.setAttribute("TOTALDISCOUNT", "");
				session.setAttribute("TOTALSUBTOTAL", "");
				session.setAttribute("TOTALTAX", "");
				response.sendRedirect("jsp/" + posPageToDirect);
				/*response.sendRedirect("jsp/" + posPageToDirect + "?TRANID=" + request.getParameter("TRANID")
						+ "&CHKBATCH=" + batchChecked + "&LOC=" + loc + "&TOLOC=" + to_LOC + "&FROM_LOCDESC="
						+ fromlocdesc + "&TOLOCDESC=" + tolocdesc + "&serialized=" + serialized + "&EMP_ID=" + empID
						+ "&REFERENCENO=" + refNo + "&serialized=" + serialized + "&defualtQty=" + defualtQty
						+ "&bulkcheckout=" + bulkcheckout + "&TRANSACTIONDATE=" + tranDate + "&REASONCODE="
						+ holdReasonCode + "&REMARKS=" + holdRemarks + "&EMP_NAME=" + empName);*/
				// RequestDispatcher view =
				// request.getRequestDispatcher("jsp/"+posPageToDirect+"?TRANID="+request.getParameter("TRANID")+"&CHKBATCH="+batchChecked);

				// view.forward(request, response);
			}

			InvFlag = false;
			e.printStackTrace();
		}
	}

	private String genReceipt(HttpServletRequest request) throws IOException, ServletException, Exception {
		String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
		String userName = StrUtils.fString((String) request.getSession().getAttribute("LOGIN_USER")).trim();
		String DATE = "", sBatchSeq = "", rtnBatch = "", sZero = "";

		try {

			TblControlDAO _TblControlDAO = new TblControlDAO();
			_TblControlDAO.setmLogger(mLogger);
			DATE = _TblControlDAO.getDate();
			Hashtable<String, String> ht = new Hashtable<String, String>();
			String query = " isnull(NXTSEQ,'') as NXTSEQ";
			ht.put(IDBConstants.PLANT, plant);
			ht.put(IDBConstants.TBL_FUNCTION, IDBConstants.TBL_POS_CAPTION);
			ht.put(IDBConstants.TBL_PREFIX1, DATE.substring(0, 6));
			boolean exitFlag = _TblControlDAO.isExisit(ht, "", plant);
			if (exitFlag == false) {

				rtnBatch = DATE.substring(0, 6) + "0000" + IDBConstants.TBL_FIRST_NEX_SEQ;
				Hashtable<String, String> htTblCntInsert = new Hashtable<String, String>();
				htTblCntInsert.put(IDBConstants.PLANT, (String) plant);
				htTblCntInsert.put(IDBConstants.TBL_FUNCTION, (String) IDBConstants.TBL_POS_CAPTION);
				htTblCntInsert.put(IDBConstants.TBL_PREFIX1, (String) DATE.substring(0, 6));
				htTblCntInsert.put(IDBConstants.TBL_NEXT_SEQ, (String) IDBConstants.TBL_FIRST_NEX_SEQ);
				htTblCntInsert.put(IDBConstants.CREATED_BY, (String) userName);
				new DateUtils();
				htTblCntInsert.put(IDBConstants.CREATED_AT, (String) DateUtils.getDateTime());
				_TblControlDAO.insertTblControl(htTblCntInsert, plant);

			} else {

				Map m = _TblControlDAO.selectRow(query, ht, "");
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
				rtnBatch = DATE.substring(0, 6) + sZero + updatedSeq;
				Hashtable<String, String> htTblCntUpdate = new Hashtable<String, String>();
				htTblCntUpdate.put(IDBConstants.PLANT, plant);
				htTblCntUpdate.put(IDBConstants.TBL_FUNCTION, IDBConstants.TBL_POS_CAPTION);
				htTblCntUpdate.put(IDBConstants.TBL_PREFIX1, DATE.substring(0, 6));
				StringBuffer updateQyery = new StringBuffer("set ");
				updateQyery.append(IDBConstants.TBL_NEXT_SEQ + " = '" + (String) updatedSeq.toString() + "'");

				_TblControlDAO.update(updateQyery.toString(), htTblCntUpdate, "", plant);

			}

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		}
		return rtnBatch;
	}

	private ITEMMST getItemDetails(HttpServletRequest req, String item, float scanqty, String batch) throws Exception {
		HttpSession session1 = req.getSession();
		ITEMMST items = new ITEMMST();

		String itemdesc = "", qty = "", price = "", minsprice = "", nonstockflag = "",uom="";
		float discount = 0, fgsttax = 0;
		String disccntstr = "";
		ItemSesBeanDAO itemDAO = new ItemSesBeanDAO();
		String PLANT = session1.getAttribute("PLANT").toString();
		itemDAO.setmLogger(mLogger);
		List prdlist = itemDAO.queryProducts(PLANT, " where item='" + item + "'");
		for (int i = 0; i < 1; i++) {
			Vector vecItem = (Vector) prdlist.get(0);
			itemdesc = (String) vecItem.get(1);
			qty = (String) vecItem.get(8);
			//qty = StrUtils.TrunkateDecimalForImportData(qty);
			price = (String) vecItem.get(12);
			discount = Float.parseFloat((String) vecItem.get(13));
			minsprice = (String) vecItem.get(14);
			nonstockflag = (String) vecItem.get(15); // added by radhika
			uom = (String) vecItem.get(3);
		}

		if (discount > 0) {
			items.setDISCOUNT(discount);

		}
		items.setSTKUOM(uom);
		items.setITEM(item);
		items.setITEMDESC(itemdesc + disccntstr);
		items.setDISCOUNT(discount);
//		int minqty = 1;
		float pricef = Float.parseFloat(price);
		float minspricef = Float.parseFloat(minsprice);
		items.setMINSPRICE(minspricef);
		items.setStkqty(scanqty);// }
		// start code by radhika to add batch in pos transaction on 4 dec 13
		items.setBATCH(batch);
		// end code by radhika to add batch in pos transaction on 4 dec 13
		// added by radhika
		items.setNONSTOCKFLAG(nonstockflag);
		// end
		items.setUNITPRICE(Float.parseFloat(price));
		items.setUSERFLD6(price);
		if (scanqty > 1) {

			items.setTotalPrice((pricef - pricef * discount / 100) * scanqty);
		} else {
			items.setTotalPrice(pricef - pricef * discount / 100);
		}
		// getting tax(GST)
		List gsttax = _GstTypeUtil.qryGstType("POS", PLANT, " ");
		for (int i = 0; i < 1; i++) {
			Vector vecgsttax = (Vector) gsttax.get(0);
			fgsttax = Float.parseFloat((String) vecgsttax.get(2));
		}
		if (fgsttax > 0) {
			items.setGSTTAX(fgsttax);

		}
		float gsttaxamount = (items.getGSTTAX() / 100);
		float totalgsttax = items.getTotalPrice() * gsttaxamount;
		items.setPRICEWITHTAX(items.getTotalPrice() + totalgsttax);
		return items;
	}

	private ITEMMST getPOSItemDetails(HttpServletRequest req, String item, float scanqty, String batch, String expDate)
			throws Exception {
		HttpSession session1 = req.getSession();
		ITEMMST items = new ITEMMST();

		String itemdesc = "", qty = "", price = "", minsprice = "", nonstockflag = "";
		float discount = 0, fgsttax = 0;
		String disccntstr = "";
		ItemSesBeanDAO itemDAO = new ItemSesBeanDAO();
		String PLANT = session1.getAttribute("PLANT").toString();
		itemDAO.setmLogger(mLogger);
		List prdlist = itemDAO.queryProducts(PLANT, " where item='" + item + "'");
		for (int i = 0; i < 1; i++) {
			Vector vecItem = (Vector) prdlist.get(0);
			itemdesc = (String) vecItem.get(1);
			qty = (String) vecItem.get(8);
			//qty = StrUtils.TrunkateDecimalForImportData(qty);
			price = (String) vecItem.get(12);
			discount = Float.parseFloat((String) vecItem.get(13));
			minsprice = (String) vecItem.get(14);
			nonstockflag = (String) vecItem.get(15); // added by radhika

		}

		if (discount > 0) {
			items.setDISCOUNT(discount);

		}
		items.setITEM(item);
		items.setEXPIREDATE(expDate);
		items.setITEMDESC(itemdesc + disccntstr);
		items.setDISCOUNT(discount);
//		int minqty = 1;
		float pricef = Float.parseFloat(price);
		float minspricef = Float.parseFloat(minsprice);
		items.setMINSPRICE(minspricef);
		items.setStkqty(scanqty);// }
		// start code by radhika to add batch in pos transaction on 4 dec 13
		items.setBATCH(batch);
		// end code by radhika to add batch in pos transaction on 4 dec 13
		// added by radhika
		items.setNONSTOCKFLAG(nonstockflag);
		// end
		items.setUNITPRICE(pricef);
		if (scanqty > 1) {

			items.setTotalPrice((pricef - pricef * discount / 100) * scanqty);
		} else {
			items.setTotalPrice(pricef - pricef * discount / 100);
		}
		// getting tax(GST)
		List gsttax = _GstTypeUtil.qryGstType("POS", PLANT, " ");
		for (int i = 0; i < 1; i++) {
			Vector vecgsttax = (Vector) gsttax.get(0);
			fgsttax = Float.parseFloat((String) vecgsttax.get(2));
		}
		if (fgsttax > 0) {
			items.setGSTTAX(fgsttax);

		}
		float gsttaxamount = (items.getGSTTAX() / 100);
		float totalgsttax = items.getTotalPrice() * gsttaxamount;
		items.setPRICEWITHTAX(items.getTotalPrice() + totalgsttax);
		return items;
	}

	private Vector<ITEMMST> getItemDetailsForPOSTranId(HttpServletRequest req, String TranId, Vector<ITEMMST> list)
			throws Exception {
		HttpSession session1 = req.getSession();
		Vector<ITEMMST> vecRec = new Vector<ITEMMST>();
		String item = "", itemdesc = "", batch = "", qty = "", price = "", TotalPrice = "", discnt = "",uom="",
				expirydate = ""; // minsprice = "";
//		float discount = 0;
//		String disccntstr = "";
		POSDetDAO posdetDAO = new POSDetDAO();
		String PLANT = session1.getAttribute("PLANT").toString();
		posdetDAO.setmLogger(mLogger);
		List prdlist = posdetDAO.listProductsForPOSTranID(PLANT, TranId, " (TRANSTATUS='C' or TRANSTATUS='N')");
		float fgsttax = 0;
		List gsttax = _GstTypeUtil.qryGstType("POS", PLANT, " ");
		for (int i = 0; i < 1; i++) {
			Vector vecgsttax = (Vector) gsttax.get(0);
			fgsttax = Float.parseFloat((String) vecgsttax.get(2));
		}
		for (int i = 0; i < prdlist.size(); i++) {
			ITEMMST items = new ITEMMST();
			Vector vecItem = (Vector) prdlist.get(i);
			item = (String) vecItem.get(0);
			itemdesc = (String) vecItem.get(1);
			qty = (String) vecItem.get(2);
			//qty = StrUtils.TrunkateDecimalForImportData(qty);
			price = (String) vecItem.get(3);
			if(Float.parseFloat(price)>0)
			price = new POSUtil().getConvertedLocalCurrencyToUnitCost(PLANT, TranId, price);
			items.setUSERFLD4(price);
			TotalPrice = (String) vecItem.get(4);
			if(Float.parseFloat(TotalPrice)>0)
			TotalPrice = new POSUtil().getConvertedLocalCurrencyToUnitCost(PLANT, TranId, TotalPrice);
			discnt = (String) vecItem.get(5);
			batch = (String) vecItem.get(6);
			expirydate = (String) vecItem.get(7);
			uom = (String) vecItem.get(18);
			items.setEXPIREDATE(expirydate);
			items.setITEM(item);
			items.setITEMDESC(itemdesc);
			items.setBATCH(batch);
			float qtyf = Float.parseFloat(qty);
			items.setStkqty(qtyf);
			float pricef = Float.parseFloat(price);
			items.setUNITPRICE( Float.parseFloat(price));
			float dicountf = Float.parseFloat(discnt);
			items.setDISCOUNT(dicountf);
			float totalPrice = Float.parseFloat(TotalPrice);
			items.setTotalPrice(totalPrice);
			double prval = Double.valueOf((String)vecItem.get(3)); 
			   BigDecimal bd = new BigDecimal((String)vecItem.get(3));
			 System.out.println(bd);
			 DecimalFormat format = new DecimalFormat("#.#####");		
			 format.setRoundingMode(RoundingMode.FLOOR);
			 items.setUSERFLD6(format.format(bd));
			//items.setUSERFLD6((String) vecItem.get(3));
			items.setEmpNo((String) vecItem.get(8));
			items.setRefNo((String) vecItem.get(9));
			items.setTranDate((String) vecItem.get(10));
			items.setReasonCode((String) vecItem.get(11));
			items.setRemarks((String) vecItem.get(12));
			items.setEmpName((String) vecItem.get(13));
			items.setLoc((String) vecItem.get(14));
			items.setFromLoc((String) vecItem.get(14));
			items.setToLoc((String) vecItem.get(15));
			items.setLocDesc((String) vecItem.get(16));
			items.setToLocDesc((String) vecItem.get(17));
			items.setSTKUOM((String) vecItem.get(18));
			if (fgsttax > 0) {
				items.setGSTTAX(fgsttax);
			}
			vecRec.addElement(items);

		}
		return vecRec;
	}

	private Vector<ITEMMST> getHoldItemDetailsForPOSTranId(HttpServletRequest req, String TranId, Vector<ITEMMST> list)
			throws Exception {
		HttpSession session1 = req.getSession();
		Vector<ITEMMST> vecRec = new Vector<ITEMMST>();
		String item = "", itemdesc = "", batch = "", qty = "", price = "", TotalPrice = "", discnt = "",
				expirydate = ""; // minsprice = "";
//		float discount = 0;
//		String disccntstr = "";
		POSDetDAO posdetDAO = new POSDetDAO();
		String PLANT = session1.getAttribute("PLANT").toString();
		posdetDAO.setmLogger(mLogger);
		List prdlist = posdetDAO.listHoldProductsForPOSTranID(PLANT, TranId, " TRANSTATUS<>'C'");
		float fgsttax = 0;
		List gsttax = _GstTypeUtil.qryGstType("POS", PLANT, " ");
		for (int i = 0; i < 1; i++) {
			Vector vecgsttax = (Vector) gsttax.get(0);
			fgsttax = Float.parseFloat((String) vecgsttax.get(2));
		}
		for (int i = 0; i < prdlist.size(); i++) {
			ITEMMST items = new ITEMMST();
			Vector vecItem = (Vector) prdlist.get(i);
			item = (String) vecItem.get(0);
			itemdesc = (String) vecItem.get(1);
			qty = (String) vecItem.get(2);
			//qty = StrUtils.TrunkateDecimalForImportData(qty);
			price = (String) vecItem.get(3);
			TotalPrice = (String) vecItem.get(4);
			discnt = (String) vecItem.get(5);
			batch = (String) vecItem.get(6);
			expirydate = (String) vecItem.get(7);
			items.setEXPIREDATE(expirydate);
			items.setITEM(item);
			items.setITEMDESC(itemdesc);
			items.setBATCH(batch);
			float qtyf = Float.parseFloat(qty);
			items.setStkqty(qtyf);
			float pricef = Float.parseFloat(price);
			items.setUNITPRICE(pricef);
			float dicountf = Float.parseFloat(discnt);
			items.setDISCOUNT(dicountf);
			float totalPrice = Float.parseFloat(TotalPrice);
			items.setTotalPrice(totalPrice);

			if (fgsttax > 0) {
				items.setGSTTAX(fgsttax);
			}

			items.setEmpNo((String) vecItem.get(8));
			items.setRefNo((String) vecItem.get(9));
			items.setTranDate((String) vecItem.get(10));
			items.setReasonCode((String) vecItem.get(11));
			items.setRemarks((String) vecItem.get(12));
			items.setEmpName((String) vecItem.get(13));
			items.setLoc((String) vecItem.get(14));
			items.setLocDesc((String) vecItem.get(15));
			items.setSTKUOM((String) vecItem.get(16));			
			vecRec.addElement(items);

		}
		return vecRec;
	}

	public void viewPOSReport(HttpServletRequest request, HttpServletResponse response, String tranid)
			throws IOException, Exception {
		java.sql.Connection con = null;
		try {
//			SalesDetailDAO saledao = new SalesDetailDAO();
//			Map m = null;
//			CustUtil cUtil = new CustUtil();
			PlantMstUtil pmUtil = new PlantMstUtil();
			LocUtil lcUtil = new LocUtil();
			HttpSession session = request.getSession();
			String CFAX = "", CWEBSITE = "", CRCBNO = "", CGSTREGNO = "", PLANTDESC = "", CADD1 = "", CADD3 = "",
					CADD2 = "", CADD4 = "", CCOUNTRY = "", CZIP = "", CTEL = "";
			String CONTACTNAME = "", CHPNO = "", CEMAIL = "";
			String PLANT = (String) session.getAttribute("PLANT");
			String userid = (String) session.getAttribute("LOGIN_USER");
			String loc = request.getParameter("LOC");
			String paymentmode = request.getParameter("paymentmodes");
			String amountsToPrint = request.getParameter("amountsToPrint");
			String paymentModeArray[] = paymentmode.split(",");
			String amountsToPrintArray[] = amountsToPrint.split(",");
			con = DbBean.getConnection();
			String SysDate = DateUtils.getDate();
			String SysTime = DateUtils.Time();
			String jasperPath = DbBean.JASPER_INPUT + "/" + "rptPOS";
			String imagePath = DbBean.COMPANY_LOGO_PATH + "/" + PLANT.toLowerCase() + "Logo.gif";
			String imagePath1 = DbBean.COMPANY_LOGO_PATH + "/" + PLANT.toLowerCase() + "Logo1.gif";
			File checkImageFile = new File(imagePath);
			if (!checkImageFile.exists()) {
				imagePath = DbBean.COMPANY_LOGO_PATH + "/" + DbBean.NO_LOGO_FILE;
			}
			File checkImageFile1 = new File(imagePath1);
			if (!checkImageFile1.exists()) {
				imagePath1 = DbBean.COMPANY_LOGO_PATH + "/" + DbBean.NO_LOGO_FILE;
			}
//			Date sysTime1 = new Date(System.currentTimeMillis());
//			Date cDt = new Date(System.currentTimeMillis());
			Map parameters = new HashMap();
			ArrayList listQryPlantMst = pmUtil.getPlantMstDetails(PLANT);
			for (int i = 0; i < listQryPlantMst.size(); i++) {
				Map map = (Map) listQryPlantMst.get(i);

				PLANTDESC = (String) map.get("PLNTDESC");
				CADD1 = (String) map.get("ADD1");
				CADD2 = (String) map.get("ADD2");
				CADD3 = (String) map.get("ADD3");
				CADD4 = (String) map.get("ADD4");
				CCOUNTRY = (String) map.get("COUNTY");
				CZIP = (String) map.get("ZIP");
				CTEL = (String) map.get("TELNO");
				CFAX = (String) map.get("FAX");
				CRCBNO = StrUtils.fString((String) map.get("RCBNO"));
				CONTACTNAME = (String) map.get("NAME");
				CHPNO = (String) map.get("HPNO");
				CEMAIL = (String) map.get("EMAIL");
			}

			POSUtil posUtil = new POSUtil();
			Map mhdr = posUtil.getPosReceiptHdrDetails(PLANT);
			String PaymentMode = (String) mhdr.get("PAYMENT_MODE");
			String SalesRep = (String) mhdr.get("SALES_REP");
			String SerialNo = (String) mhdr.get("SERIAL_NO");
			String ReceiptNo = (String) mhdr.get("RECEIPT_NO");
			String Product = (String) mhdr.get("PRODUCT");
			String Description = (String) mhdr.get("PROD_DESC");
			String UnitPrice = (String) mhdr.get("UNIT_PRICE");
			String Qty = (String) mhdr.get("QTY");
			String Discount = (String) mhdr.get("DISCOUNT");
			String Total = (String) mhdr.get("TOTAL");
			String Subtotal = (String) mhdr.get("SUBTOTAL");
			String Tax = (String) mhdr.get("TAX");
			String TotalAmt = (String) mhdr.get("TOTAL_AMT");
//			String PaymentPaid = (String) mhdr.get("AMOUNT_PAID");
			String ChangeRemaining = (String) mhdr.get("CHANGE");
			String DisplayByLoc = (String) mhdr.get("ADDRBYLOC");
			String HEADER = (String) mhdr.get("HDR");
			String GREET1 = (String) mhdr.get("G1");
			String GREET2 = (String) mhdr.get("G2");
			String FOOT1 = (String) mhdr.get("F1");
			String FOOT2 = (String) mhdr.get("F2");
			String msgWithcompanyAdrr = GREET1 + "##" + GREET2 + "##" + FOOT1 + "##" + FOOT2 + "##";
			if(msgWithcompanyAdrr.equals("########"))
			{
				msgWithcompanyAdrr="";
			}
			if (DisplayByLoc.equals("1")) {
				if (PLANTDESC.length() > 0)
					msgWithcompanyAdrr = msgWithcompanyAdrr + PLANTDESC + ",";
				if (CADD1.length() > 0)
					msgWithcompanyAdrr = msgWithcompanyAdrr + " Address:" + CADD1 + ",";
				if (CADD2.length() > 0)
					msgWithcompanyAdrr = msgWithcompanyAdrr + CADD2 + ",";
				if (CADD3.length() > 0)
					msgWithcompanyAdrr = msgWithcompanyAdrr + CADD3 + ",";
				if (CADD4.length() > 0)
					msgWithcompanyAdrr = msgWithcompanyAdrr + CADD4 + ",";
				if (CCOUNTRY.length() > 0)
					msgWithcompanyAdrr = msgWithcompanyAdrr + CCOUNTRY;
				if (CZIP.length() > 0)
					msgWithcompanyAdrr = msgWithcompanyAdrr + "(" + CZIP + ")";
				if (CTEL.length() > 0)
					msgWithcompanyAdrr = msgWithcompanyAdrr + "Tel:" + CTEL + ",";
				if (CFAX.length() > 0)
					msgWithcompanyAdrr = msgWithcompanyAdrr + "Fax:" + CFAX + "##";
				if (CFAX == "null" || CFAX == "")
					msgWithcompanyAdrr = msgWithcompanyAdrr + "##";
				String strAdr2 = "";
				if (CWEBSITE.length() > 0)
					msgWithcompanyAdrr = msgWithcompanyAdrr + "Website:" + CWEBSITE;
				strAdr2 = strAdr2 + CWEBSITE;
				if (strAdr2.length() > 0) {
					strAdr2 = ",";
				}
				if (CRCBNO.length() > 0)
					msgWithcompanyAdrr = msgWithcompanyAdrr + strAdr2 + " Business Registration No:" + CRCBNO;
				if (CGSTREGNO.length() > 0)
					msgWithcompanyAdrr = msgWithcompanyAdrr + "," + " GST Registration No: " + CGSTREGNO;

				ArrayList listQry = lcUtil.getLocListDetails(PLANT, loc);
				for (int i = 0; i < listQry.size(); i++) {
					Map map1 = (Map) listQry.get(i);

					PLANTDESC = (String) map1.get("COMNAME");
					CADD1 = (String) map1.get("ADD1");
					CADD2 = (String) map1.get("ADD2");
					CADD3 = (String) map1.get("ADD3");
					CADD4 = (String) map1.get("ADD4");
					CCOUNTRY = (String) map1.get("COUNTRY");
					CZIP = (String) map1.get("ZIP");
					CTEL = (String) map1.get("TELNO");
					CFAX = (String) map1.get("FAX");
					CRCBNO = StrUtils.fString((String) map1.get("RCBNO"));
					CONTACTNAME = "";
					CHPNO = "";
					CEMAIL = "";

				}
			}
			String[] receiptmsgarray = getReceiptMsg(msgWithcompanyAdrr);
			int index = 1;
			// POS tran Details
			for (int i = 0; i < receiptmsgarray.length; i++) {
				parameters.put("MSG" + index, receiptmsgarray[i]);
				index = index + 1;
			}
			parameters.put("imagePath", imagePath);
			parameters.put("imagePath1", imagePath1);
			parameters.put("ReceiptNo", tranid);
			parameters.put("user", userid);
			parameters.put("company", PLANT);
			parameters.put("currentDate", SysDate);
			parameters.put("curTime", SysTime);
			parameters.put("paymentType", paymentmode);
			parameters.put("heading", HEADER);
			parameters.put("lblPaymentMethods", PaymentMode);
			parameters.put("lblSalesRep", SalesRep);
			parameters.put("lblSerialNo", SerialNo);
			parameters.put("lblReceiptNo", ReceiptNo);
			parameters.put("lblItem", Product);
			parameters.put("lblDescription", Description);
			parameters.put("lblUnitPrice", UnitPrice);
			parameters.put("lblQty", Qty);
			parameters.put("lblDiscount", Discount);
			parameters.put("lblTotal", Total);
			parameters.put("lblSubtotal", Subtotal);
			parameters.put("lblTax", Tax);
			parameters.put("lblTotalAmt", TotalAmt);
			parameters.put("lblPaymentMode1", paymentModeArray[0]);
			parameters.put("amount1", amountsToPrintArray[0]);
			if (paymentModeArray.length > 1) {
				parameters.put("lblPaymentMode2", paymentModeArray[1]);
				parameters.put("amount2", amountsToPrintArray[1]);
			} else {
				parameters.put("lblPaymentMode2", "");
				parameters.put("amount2", "0");
			}

			parameters.put("lblCashTendered", "Cash Tendered");
			parameters.put("lblChange", ChangeRemaining);
			double discount = 0, subtotalwithdiscount = 0, tax = 0, totalwithtax = 0, totalsum = 0, sumsubtotal = 0,
					gsttax = 0;
			if ((String) session.getAttribute("CALCDISCOUNT") != null
					&& !session.getAttribute("CALCDISCOUNT").equals("")) {
				discount = Double.parseDouble((String) session.getAttribute("CALCDISCOUNT"));
			} else {
				discount = 0;

			}
			if ((String) session.getAttribute("TOTALSUBTOTAL") != null
					&& !session.getAttribute("TOTALSUBTOTAL").equals("")) {
				subtotalwithdiscount = Double.parseDouble((String) session.getAttribute("TOTALSUBTOTAL"));
			} else {
				subtotalwithdiscount = 0;
			}

			if ((String) session.getAttribute("TOTALTAX") != null && !session.getAttribute("TOTALTAX").equals("")) {
				tax = Double.parseDouble((String) session.getAttribute("TOTALTAX"));
			} else {
				tax = Double.parseDouble((String) session.getAttribute("gsttax"));
			}

			if ((String) session.getAttribute("TOTALDISCOUNT") != null
					&& !session.getAttribute("TOTALDISCOUNT").equals("")) {
				totalwithtax = Double.parseDouble((String) session.getAttribute("TOTALDISCOUNT"));
			} else {
				totalwithtax = Double.parseDouble((String) session.getAttribute("totalSum"));
			}
			parameters.put("lblTotalDiscount", "Total Discount");
			parameters.put("lblSubtotalWithDis", "SubTotal With Discount");
			parameters.put("discount", discount);
			parameters.put("subtotalwithdiscount", subtotalwithdiscount);
			parameters.put("tax", tax);
			parameters.put("totalwithtax", totalwithtax);
			parameters.put("fromAddress_CompanyName", PLANTDESC);
			parameters.put("fromAddress_BlockAddress", CADD1 + "  " + CADD2);
			parameters.put("fromAddress_RoadAddress", CADD3 + "  " + CADD4);
			parameters.put("fromAddress_Country", CCOUNTRY);
			parameters.put("fromAddress_ZIPCode", CZIP);
			parameters.put("fromAddress_TpNo", CTEL);
			parameters.put("fromAddress_FaxNo", CFAX);

			if (CONTACTNAME.length() > 1) {
				parameters.put("fromAddress_ContactPersonName", CONTACTNAME);
			} else {
				parameters.put("fromAddress_ContactPersonName", "");

			}

			if (CHPNO.length() > 1) {
				parameters.put("fromAddress_ContactPersonMobile", CHPNO);
			} else {
				parameters.put("fromAddress_ContactPersonMobile", "");
			}
			if (CEMAIL.length() > 1) {
				parameters.put("fromAddress_ContactPersonEmail", CEMAIL);
			} else {
				parameters.put("fromAddress_ContactPersonEmail", "");

			}
			parameters.put("referanceNO", CRCBNO);
			String gstValue = new selectBean().getGST("POS", PLANT);
			double gst = new Double(gstValue).doubleValue() / 100;
			parameters.put("Gst", gst);
			long start = System.currentTimeMillis();
			System.out.println("**************" + " Start Up Time : " + start + "**********");
			byte[] bytes = JasperRunManager.runReportToPdf(jasperPath + ".jasper", parameters, con);
			response.addHeader("Content-disposition", "attachment;filename=reporte.pdf");
			response.setContentLength(bytes.length);
			response.getOutputStream().write(bytes);
			response.setContentType("application/pdf");
		} catch (IOException e) {

			e.printStackTrace();

		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}
	}

	public void viewPOSProductReport(HttpServletRequest request, HttpServletResponse response, String tranid)
			throws IOException, Exception {
		java.sql.Connection con = null;
		try {
			SalesDetailDAO saledao = new SalesDetailDAO();
			Map m = null;
			CustUtil cUtil = new CustUtil();
			PlantMstUtil pmUtil = new PlantMstUtil();
			LocUtil lcUtil = new LocUtil();
			HttpSession session = request.getSession();
			String CFAX = "", CWEBSITE = "", CRCBNO = "", CGSTREGNO = "", PLANTDESC = "", CADD1 = "", CADD3 = "",
					CADD2 = "", CADD4 = "", CCOUNTRY = "", CZIP = "", CTEL = "";
			String CONTACTNAME = "", CHPNO = "", CEMAIL = "";
			String PLANT = (String) session.getAttribute("PLANT");
			String userid = (String) session.getAttribute("LOGIN_USER");
			String loc = request.getParameter("LOC");
			con = DbBean.getConnection();
			String tranDate = StrUtils.fString(request.getParameter("TRANSACTIONDATE")).trim();
			String SysDate = DateUtils.getDate();
			String SysTime = DateUtils.Time();
			String jasperPath = DbBean.JASPER_INPUT + "/" + "rptPOSProduct";
			String imagePath = DbBean.COMPANY_LOGO_PATH + "/" + PLANT.toLowerCase() + "Logo.gif";
			String imagePath1 = DbBean.COMPANY_LOGO_PATH + "/" + PLANT.toLowerCase() + "Logo1.gif";
			File checkImageFile = new File(imagePath);
			if (!checkImageFile.exists()) {
				imagePath = DbBean.COMPANY_LOGO_PATH + "/" + DbBean.NO_LOGO_FILE;
			}
			File checkImageFile1 = new File(imagePath1);
			if (!checkImageFile1.exists()) {
				imagePath1 = DbBean.COMPANY_LOGO_PATH + "/" + DbBean.NO_LOGO_FILE;
			}
			Date sysTime1 = new Date(System.currentTimeMillis());
			Date cDt = new Date(System.currentTimeMillis());
			Map parameters = new HashMap();
			ArrayList listQryPlantMst = pmUtil.getPlantMstDetails(PLANT);
			for (int i = 0; i < listQryPlantMst.size(); i++) {
				Map map = (Map) listQryPlantMst.get(i);

				PLANTDESC = (String) map.get("PLNTDESC");
				CADD1 = (String) map.get("ADD1");
				CADD2 = (String) map.get("ADD2");
				CADD3 = (String) map.get("ADD3");
				CADD4 = (String) map.get("ADD4");
				CCOUNTRY = (String) map.get("COUNTY");
				CZIP = (String) map.get("ZIP");
				CTEL = (String) map.get("TELNO");
				CFAX = (String) map.get("FAX");
				CRCBNO = StrUtils.fString((String) map.get("RCBNO"));
				CONTACTNAME = (String) map.get("NAME");
				CHPNO = (String) map.get("HPNO");
				CEMAIL = (String) map.get("EMAIL");
			}

			POSUtil posUtil = new POSUtil();
			Map mhdr = posUtil.getPosReceiptHdrDetails(PLANT);
			String Orientation = (String) mhdr.get("PrintOrientation");
			String PaymentMode = (String) mhdr.get("PAYMENT_MODE");
			String SalesRep = (String) mhdr.get("SALES_REP");
			String SerialNo = (String) mhdr.get("SERIAL_NO");
			String ReceiptNo = (String) mhdr.get("RECEIPT_NO");
			String Product = (String) mhdr.get("PRODUCT");
			String Description = (String) mhdr.get("PROD_DESC");
			String UnitPrice = (String) mhdr.get("UNIT_PRICE");
			String Qty = (String) mhdr.get("QTY");
			String Discount = (String) mhdr.get("DISCOUNT");
			String Total = (String) mhdr.get("TOTAL");
			String Subtotal = (String) mhdr.get("SUBTOTAL");
			String Tax = (String) mhdr.get("TAX");
			String TotalAmt = (String) mhdr.get("TOTAL_AMT");
			String PaymentPaid = (String) mhdr.get("AMOUNT_PAID");
			String ChangeRemaining = (String) mhdr.get("CHANGE");
			String DisplayByLoc = (String) mhdr.get("ADDRBYLOC");
			String HEADER = (String) mhdr.get("HDR");
			String GREET1 = (String) mhdr.get("G1");
			String GREET2 = (String) mhdr.get("G2");
			String FOOT1 = (String) mhdr.get("F1");
			String FOOT2 = (String) mhdr.get("F2");
			String UOM = (String) mhdr.get("UNITMO");
			String msgWithcompanyAdrr = GREET1 + "##" + GREET2 + "##" + FOOT1 + "##" + FOOT2 + "##";
			if(msgWithcompanyAdrr.equals("########"))
			{
				msgWithcompanyAdrr="";
			}
			if (DisplayByLoc.equals("1")) {
				if (PLANTDESC.length() > 0)
					msgWithcompanyAdrr = msgWithcompanyAdrr + PLANTDESC + ",";
				if (CADD1.length() > 0)
					msgWithcompanyAdrr = msgWithcompanyAdrr + " Address:" + CADD1 + ",";
				if (CADD2.length() > 0)
					msgWithcompanyAdrr = msgWithcompanyAdrr + CADD2 + ",";
				if (CADD3.length() > 0)
					msgWithcompanyAdrr = msgWithcompanyAdrr + CADD3 + ",";
				if (CADD4.length() > 0)
					msgWithcompanyAdrr = msgWithcompanyAdrr + CADD4 + ",";
				if (CCOUNTRY.length() > 0)
					msgWithcompanyAdrr = msgWithcompanyAdrr + CCOUNTRY;
				if (CZIP.length() > 0)
					msgWithcompanyAdrr = msgWithcompanyAdrr + "(" + CZIP + ")";
				if (CTEL.length() > 0)
					msgWithcompanyAdrr = msgWithcompanyAdrr + "Tel:" + CTEL + ",";
				if (CFAX.length() > 0)
					msgWithcompanyAdrr = msgWithcompanyAdrr + "Fax:" + CFAX + "##";
				if (CFAX == "null" || CFAX == "")
					msgWithcompanyAdrr = msgWithcompanyAdrr + "##";
				String strAdr2 = "";
				if (CWEBSITE.length() > 0)
					msgWithcompanyAdrr = msgWithcompanyAdrr + "Website:" + CWEBSITE;
				strAdr2 = strAdr2 + CWEBSITE;
				if (strAdr2.length() > 0) {
					strAdr2 = ",";
				}
				if (CRCBNO.length() > 0)
					// msgWithcompanyAdrr= msgWithcompanyAdrr +strAdr2+" Business Registration
					// No:"+CRCBNO ;
					msgWithcompanyAdrr = msgWithcompanyAdrr + strAdr2;
				if (CGSTREGNO.length() > 0)
					msgWithcompanyAdrr = msgWithcompanyAdrr + "," + " GST Registration No: " + CGSTREGNO;

				ArrayList listQry = lcUtil.getLocListDetails(PLANT, loc);
				for (int i = 0; i < listQry.size(); i++) {
					Map map1 = (Map) listQry.get(i);

					PLANTDESC = (String) map1.get("COMNAME");
					CADD1 = (String) map1.get("ADD1");
					CADD2 = (String) map1.get("ADD2");
					CADD3 = (String) map1.get("ADD3");
					CADD4 = (String) map1.get("ADD4");
					CCOUNTRY = (String) map1.get("COUNTRY");
					CZIP = (String) map1.get("ZIP");
					CTEL = (String) map1.get("TELNO");
					CFAX = (String) map1.get("FAX");
					CRCBNO = StrUtils.fString((String) map1.get("RCBNO"));

				}
			}
			if (Orientation.equals("Portrait")) {
				jasperPath = DbBean.JASPER_INPUT + "/" + "rptPOSProductPortrait";
			}
			String[] receiptmsgarray = getReceiptMsg(msgWithcompanyAdrr);
			int index = 1;
			// POS tran Details
			for (int i = 0; i < receiptmsgarray.length; i++) {
				parameters.put("MSG" + index, receiptmsgarray[i]);
				index = index + 1;
			}

			parameters.put("rcbNo", CRCBNO);
			parameters.put("UOM", UOM);
			parameters.put("imagePath", imagePath);
			parameters.put("imagePath1", imagePath1);
			parameters.put("ReceiptNo", tranid);
			parameters.put("user", userid);
			parameters.put("company", PLANT);
			parameters.put("currentDate", tranDate);
			parameters.put("curTime", SysTime);
			parameters.put("heading", HEADER);
			parameters.put("lblSalesRep", SalesRep);
			parameters.put("lblSerialNo", SerialNo);
			parameters.put("lblReceiptNo", ReceiptNo);
			parameters.put("lblItem", Product);
			parameters.put("lblDescription", Description);
			parameters.put("lblQty", Qty);
			parameters.put("fromAddress_CompanyName", PLANTDESC);
			parameters.put("fromAddress_BlockAddress", CADD1 + "  " + CADD2);
			parameters.put("fromAddress_RoadAddress", CADD3 + "  " + CADD4);
			parameters.put("fromAddress_Country", CCOUNTRY);
			parameters.put("fromAddress_ZIPCode", CZIP);
			parameters.put("fromAddress_TpNo", CTEL);
			parameters.put("fromAddress_FaxNo", CFAX);
			parameters.put("FromHeader", "From");

			ShipHisDAO _ShipHisDAO = new ShipHisDAO();

			ArrayList shipHisDetailsList = _ShipHisDAO.getShipHisDetailByTranId(PLANT, tranid);

			String recDetRefNO = "";
			String recDetEmpNO = "";
			String empFirstName = "";
			String empLastName = "";

			for (int i = 0; i < shipHisDetailsList.size(); i++) {
				Map map = (Map) shipHisDetailsList.get(i);

				recDetRefNO = (String) map.get("REFNO");
				recDetEmpNO = (String) map.get("EMPNO");

			}
			EmployeeDAO _EmployeeDAO = new EmployeeDAO();
			ArrayList empDetailsList = _EmployeeDAO.getEmployeeName(recDetEmpNO, PLANT, "");

			for (int i = 0; i < empDetailsList.size(); i++) {
				Map map = (Map) empDetailsList.get(i);

				empFirstName = (String) map.get("FNAME");
				empLastName = (String) map.get("LNAME");

			}

			parameters.put("REFNO", recDetRefNO);
			parameters.put("EMPNAME", empFirstName + " " + empLastName);

			if (CONTACTNAME.length() > 1) {
				parameters.put("fromAddress_ContactPersonName", CONTACTNAME);
			} else {
				parameters.put("fromAddress_ContactPersonName", "");

			}

			if (CHPNO.length() > 1) {
				parameters.put("fromAddress_ContactPersonMobile", CHPNO);
			} else {
				parameters.put("fromAddress_ContactPersonMobile", "");
			}
			if (CEMAIL.length() > 1) {
				parameters.put("fromAddress_ContactPersonEmail", CEMAIL);
			} else {
				parameters.put("fromAddress_ContactPersonEmail", "");

			}
			// parameters.put("referanceNO",CRCBNO);
			String gstValue = new selectBean().getGST("POS", PLANT);

			long start = System.currentTimeMillis();
			System.out.println("**************" + " Start Up Time : " + start + "**********");
			JasperCompileManager.compileReportToFile(jasperPath + ".jrxml", jasperPath + ".jasper");
			byte[] bytes = JasperRunManager.runReportToPdf(jasperPath + ".jasper", parameters, con);
			response.addHeader("Content-disposition", "attachment;filename=reporte.pdf");
			response.setContentLength(bytes.length);
			response.getOutputStream().write(bytes);
			response.setContentType("application/pdf");
		} catch (IOException e) {

			e.printStackTrace();

		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}
	}

	public void viewReceiveProductReport(HttpServletRequest request, HttpServletResponse response, String tranid)
			throws IOException, Exception {
		java.sql.Connection con = null;
		try {
			SalesDetailDAO saledao = new SalesDetailDAO();
			Map m = null;
			CustUtil cUtil = new CustUtil();
			PlantMstUtil pmUtil = new PlantMstUtil();
			LocUtil lcUtil = new LocUtil();
			HttpSession session = request.getSession();
			String CFAX = "", CWEBSITE = "", CRCBNO = "", CGSTREGNO = "", PLANTDESC = "", CADD1 = "", CADD3 = "",
					CADD2 = "", CADD4 = "", CCOUNTRY = "", CZIP = "", CTEL = "";
			String CONTACTNAME = "", CHPNO = "", CEMAIL = "";
			String PLANT = (String) session.getAttribute("PLANT");
			String userid = (String) session.getAttribute("LOGIN_USER");
			String loc = request.getParameter("LOC");
			con = DbBean.getConnection();
			String SysDate = DateUtils.getDate();
			String SysTime = DateUtils.Time();
			String jasperPath = DbBean.JASPER_INPUT + "/" + "rptReceiveProduct";
			String imagePath = DbBean.COMPANY_LOGO_PATH + "/" + PLANT.toLowerCase() + "Logo.gif";
			String imagePath1 = DbBean.COMPANY_LOGO_PATH + "/" + PLANT.toLowerCase() + "Logo1.gif";
			String tranDate = StrUtils.fString(request.getParameter("TRANSACTIONDATE")).trim();
			File checkImageFile = new File(imagePath);
			if (!checkImageFile.exists()) {
				imagePath = DbBean.COMPANY_LOGO_PATH + "/" + DbBean.NO_LOGO_FILE;
			}
			File checkImageFile1 = new File(imagePath1);
			if (!checkImageFile1.exists()) {
				imagePath1 = DbBean.COMPANY_LOGO_PATH + "/" + DbBean.NO_LOGO_FILE;
			}
			Date sysTime1 = new Date(System.currentTimeMillis());
			Date cDt = new Date(System.currentTimeMillis());
			Map parameters = new HashMap();
			ArrayList listQryPlantMst = pmUtil.getPlantMstDetails(PLANT);
			for (int i = 0; i < listQryPlantMst.size(); i++) {
				Map map = (Map) listQryPlantMst.get(i);

				PLANTDESC = (String) map.get("PLNTDESC");
				CADD1 = (String) map.get("ADD1");
				CADD2 = (String) map.get("ADD2");
				CADD3 = (String) map.get("ADD3");
				CADD4 = (String) map.get("ADD4");
				CCOUNTRY = (String) map.get("COUNTY");
				CZIP = (String) map.get("ZIP");
				CTEL = (String) map.get("TELNO");
				CFAX = (String) map.get("FAX");
				CRCBNO = StrUtils.fString((String) map.get("RCBNO"));
				CONTACTNAME = (String) map.get("NAME");
				CHPNO = (String) map.get("HPNO");
				CEMAIL = (String) map.get("EMAIL");
			}

			POSUtil posUtil = new POSUtil();
			Map mhdr = posUtil.getPosReceiveReceiptHdrDetails(PLANT);
			String Orientation = (String) mhdr.get("PrintOrientation");
			String PaymentMode = (String) mhdr.get("PAYMENT_MODE");
			String SalesRep = (String) mhdr.get("SALES_REP");
			String SerialNo = (String) mhdr.get("SERIAL_NO");
			String ReceiptNo = (String) mhdr.get("RECEIPT_NO");
			String Product = (String) mhdr.get("PRODUCT");
			String Description = (String) mhdr.get("PROD_DESC");
			String UnitPrice = (String) mhdr.get("UNIT_COST");
			String Qty = (String) mhdr.get("QTY");
			String Discount = (String) mhdr.get("DISCOUNT");
			String Total = (String) mhdr.get("TOTAL");
			String Subtotal = (String) mhdr.get("SUBTOTAL");
			String Tax = (String) mhdr.get("TAX");
			String TotalAmt = (String) mhdr.get("TOTAL_AMT");
			String PaymentPaid = (String) mhdr.get("AMOUNT_PAID");
			String ChangeRemaining = (String) mhdr.get("CHANGE");
			String DisplayByLoc = (String) mhdr.get("ADDRBYLOC");
			String HEADER = (String) mhdr.get("HDR");
			String GREET1 = (String) mhdr.get("G1");
			String GREET2 = (String) mhdr.get("G2");
			String FOOT1 = (String) mhdr.get("F1");
			String FOOT2 = (String) mhdr.get("F2");
			String UOM = (String) mhdr.get("UNITMO");
			String msgWithcompanyAdrr = GREET1 + "##" + GREET2 + "##" + FOOT1 + "##" + FOOT2 + "##";
			if(msgWithcompanyAdrr.equals("########"))
			{
				msgWithcompanyAdrr="";
			}
			if (DisplayByLoc.equals("1")) {
				if (PLANTDESC.length() > 0)
					msgWithcompanyAdrr = msgWithcompanyAdrr + PLANTDESC + ",";
				if (CADD1.length() > 0)
					msgWithcompanyAdrr = msgWithcompanyAdrr + " Address:" + CADD1 + ",";
				if (CADD2.length() > 0)
					msgWithcompanyAdrr = msgWithcompanyAdrr + CADD2 + ",";
				if (CADD3.length() > 0)
					msgWithcompanyAdrr = msgWithcompanyAdrr + CADD3 + ",";
				if (CADD4.length() > 0)
					msgWithcompanyAdrr = msgWithcompanyAdrr + CADD4 + ",";
				if (CCOUNTRY.length() > 0)
					msgWithcompanyAdrr = msgWithcompanyAdrr + CCOUNTRY;
				if (CZIP.length() > 0)
					msgWithcompanyAdrr = msgWithcompanyAdrr + "(" + CZIP + ")";
				if (CTEL.length() > 0)
					msgWithcompanyAdrr = msgWithcompanyAdrr + "Tel:" + CTEL + ",";
				if (CFAX.length() > 0)
					msgWithcompanyAdrr = msgWithcompanyAdrr + "Fax:" + CFAX + "##";
				if (CFAX == "null" || CFAX == "")
					msgWithcompanyAdrr = msgWithcompanyAdrr + "##";
				String strAdr2 = "";
				if (CWEBSITE.length() > 0)
					msgWithcompanyAdrr = msgWithcompanyAdrr + "Website:" + CWEBSITE;
				strAdr2 = strAdr2 + CWEBSITE;
				if (strAdr2.length() > 0) {
					strAdr2 = ",";
				}
				if (CRCBNO.length() > 0)
					msgWithcompanyAdrr = msgWithcompanyAdrr + strAdr2;
				if (CGSTREGNO.length() > 0)
					msgWithcompanyAdrr = msgWithcompanyAdrr + "," + " GST Registration No: " + CGSTREGNO;

				ArrayList listQry = lcUtil.getLocListDetails(PLANT, loc);
				for (int i = 0; i < listQry.size(); i++) {
					Map map1 = (Map) listQry.get(i);

					PLANTDESC = (String) map1.get("COMNAME");
					CADD1 = (String) map1.get("ADD1");
					CADD2 = (String) map1.get("ADD2");
					CADD3 = (String) map1.get("ADD3");
					CADD4 = (String) map1.get("ADD4");
					CCOUNTRY = (String) map1.get("COUNTRY");
					CZIP = (String) map1.get("ZIP");
					CTEL = (String) map1.get("TELNO");
					CFAX = (String) map1.get("FAX");
					CRCBNO = StrUtils.fString((String) map1.get("RCBNO"));

				}
			}
			if (Orientation.equals("Portrait")) {
				jasperPath = DbBean.JASPER_INPUT + "/" + "rptReceiveProductPortrait";
			}
			String[] receiptmsgarray = getReceiptMsg(msgWithcompanyAdrr);
			int index = 1;
			// POS tran Details
			for (int i = 0; i < receiptmsgarray.length; i++) {
				parameters.put("MSG" + index, receiptmsgarray[i]);
				index = index + 1;
			}
			parameters.put("UOM", UOM);
			parameters.put("imagePath", imagePath);
			parameters.put("imagePath1", imagePath1);
			parameters.put("ReceiptNo", tranid);
			parameters.put("rcbNo", CRCBNO);
			parameters.put("user", userid);
			parameters.put("company", PLANT);
			parameters.put("currentDate", tranDate);
			parameters.put("curTime", SysTime);
			parameters.put("heading", HEADER);
			parameters.put("lblSalesRep", SalesRep);
			parameters.put("lblSerialNo", SerialNo);
			parameters.put("lblReceiptNo", ReceiptNo);
			parameters.put("lblItem", Product);
			parameters.put("lblDescription", Description);
			parameters.put("lblQty", Qty);
			parameters.put("fromAddress_CompanyName", PLANTDESC);
			parameters.put("fromAddress_BlockAddress", CADD1 + "  " + CADD2);
			parameters.put("fromAddress_RoadAddress", CADD3 + "  " + CADD4);
			parameters.put("fromAddress_Country", CCOUNTRY);
			parameters.put("fromAddress_ZIPCode", CZIP);
			parameters.put("fromAddress_TpNo", CTEL);
			parameters.put("fromAddress_FaxNo", CFAX);
			parameters.put("FromHeader", "From");

			RecvDetDAO _RecvDetDAO = new RecvDetDAO();

			ArrayList recDetDetailsList = _RecvDetDAO.getRecDetDetailsByTranID(tranid, PLANT);

			String recDetRefNO = "";
			String recDetEmpNO = "";
			String empFirstName = "";
			String empLastName = "";

			for (int i = 0; i < recDetDetailsList.size(); i++) {
				Map map = (Map) recDetDetailsList.get(i);

				recDetRefNO = (String) map.get("REFNO");
				recDetEmpNO = (String) map.get("EMPNO");

			}
			EmployeeDAO _EmployeeDAO = new EmployeeDAO();
			ArrayList empDetailsList = _EmployeeDAO.getEmployeeName(recDetEmpNO, PLANT, "");

			for (int i = 0; i < empDetailsList.size(); i++) {
				Map map = (Map) empDetailsList.get(i);

				empFirstName = (String) map.get("FNAME");
				empLastName = (String) map.get("LNAME");

			}

			parameters.put("REFNO", recDetRefNO);
			parameters.put("EMPNAME", empFirstName + " " + empLastName);

			if (CONTACTNAME.length() > 1) {
				parameters.put("fromAddress_ContactPersonName", CONTACTNAME);
			} else {
				parameters.put("fromAddress_ContactPersonName", "");

			}

			if (CHPNO.length() > 1) {
				parameters.put("fromAddress_ContactPersonMobile", CHPNO);
			} else {
				parameters.put("fromAddress_ContactPersonMobile", "");
			}
			if (CEMAIL.length() > 1) {
				parameters.put("fromAddress_ContactPersonEmail", CEMAIL);
			} else {
				parameters.put("fromAddress_ContactPersonEmail", "");

			}
			// parameters.put("referanceNO",CRCBNO);
			String gstValue = new selectBean().getGST("POS", PLANT);

			long start = System.currentTimeMillis();
			System.out.println("**************" + " Start Up Time : " + start + "**********");
			System.out.println("**************" + " Jasper Name : " + jasperPath + "**********");
			JasperCompileManager.compileReportToFile(jasperPath + ".jrxml", jasperPath + ".jasper");
			byte[] bytes = JasperRunManager.runReportToPdf(jasperPath + ".jasper", parameters, con);
			response.addHeader("Content-disposition", "attachment;filename=reporte.pdf");
			response.setContentLength(bytes.length);
			response.getOutputStream().write(bytes);
			response.setContentType("application/pdf");
		} catch (IOException e) {

			e.printStackTrace();

		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
		}
	}

	public void viewPOSMoveProductReport(HttpServletRequest request, HttpServletResponse response, String tranid)
			throws IOException, Exception {
		java.sql.Connection con = null;
		try {
			POSHdrDAO poshdrdao = new POSHdrDAO();
			Hashtable<String, String> ht = new Hashtable<>();
			Map m = null;
			CustUtil cUtil = new CustUtil();
			PlantMstUtil pmUtil = new PlantMstUtil();
			LocUtil lcUtil = new LocUtil();
			HttpSession session = request.getSession();
			String PLANTDESC = "", CADD1 = "", CADD2 = "", CADD3 = "", CADD4 = "", CZIP = "", CCOUNTRY = "", CSTATE = "",SEALNAME="",SIGNNAME="",COUNTRY_CODE="",
					CTEL = "", CFAX = "", CONTACTNAME = "", CHPNO = "", CEMAIL = "", CRCBNO = "",companyregno="", EMPNO = "",CWEBSITE="";
			String CGSTREGNO="";
			String PLANT = (String) session.getAttribute("PLANT");
			String userid = (String) session.getAttribute("LOGIN_USER");
			String loc = request.getParameter("FROM_LOC");
			String empid = StrUtils.fString(request.getParameter("EMP_ID")).trim();
			String empname = StrUtils.fString(request.getParameter("EMP_NAME")).trim();
			String refNo = StrUtils.fString(request.getParameter("REFERENCENO")).trim();
			String emp = empid + " " + empname;
			String tranDate = StrUtils.fString(request.getParameter("TRANSACTIONDATE")).trim();
			con = DbBean.getConnection();
			String SysDate = DateUtils.getDate();
			String SysTime = DateUtils.Time();
			String jasperPath = DbBean.JASPER_INPUT + "/" + "rptPOSMove";
			String imagePath = DbBean.COMPANY_LOGO_PATH + "/" + PLANT.toLowerCase() + "Logo.gif";
			String imagePath1 = DbBean.COMPANY_LOGO_PATH + "/" + PLANT.toLowerCase() + "Logo1.gif";
			String imagePath2, Orientation = "";
			File checkImageFile = new File(imagePath);

			if (!checkImageFile.exists()) {
				imagePath = DbBean.COMPANY_LOGO_PATH + "/" + DbBean.NO_LOGO_FILE;
			}
			File checkImageFile1 = new File(imagePath1);
			if (!checkImageFile1.exists()) {
				imagePath1 = DbBean.COMPANY_LOGO_PATH + "/" + DbBean.NO_LOGO_FILE;
			}
			Date sysTime1 = new Date(System.currentTimeMillis());
			Date cDt = new Date(System.currentTimeMillis());
			Map parameters = new HashMap();
			String COMP_INDUSTRY = new PlantMstDAO().getCOMP_INDUSTRY(PLANT);
			ArrayList listQryPlantMst = pmUtil.getPlantMstDetails(PLANT);
			for (int i = 0; i < listQryPlantMst.size(); i++) {
				Map map = (Map) listQryPlantMst.get(i);
				PLANTDESC = (String) map.get("PLNTDESC");
				CADD1 = (String) map.get("ADD1");
				CADD2 = (String) map.get("ADD2");
				CADD3 = (String) map.get("ADD3");
				CADD4 = (String) map.get("ADD4");
				CCOUNTRY = (String) map.get("COUNTY");
				CSTATE = (String) map.get("STATE");
				CZIP = (String) map.get("ZIP");
				CTEL = (String) map.get("TELNO");
				CFAX = (String) map.get("FAX");
				CRCBNO = StrUtils.fString((String) map.get("RCBNO"));
				CONTACTNAME = (String) map.get("NAME");
				CHPNO = (String) map.get("HPNO");
				CEMAIL = (String) map.get("EMAIL");
				CWEBSITE = (String) map.get("WEBSITE");
				companyregno = (String) map.get("companyregnumber");//imtiuen				
				SIGNNAME=StrUtils.fString((String)map.get("SIGNATURENAME"));
				COUNTRY_CODE=StrUtils.fString((String)map.get("COUNTRY_CODE"));
			}

			POSUtil posUtil = new POSUtil();
			Map mhdr = posUtil.getPosMoveHdrDetails(PLANT);
			Orientation = (String) mhdr.get("PrintOrientation");
			String PaymentMode = (String) mhdr.get("PAYMENT_MODE");
			String SalesRep = (String) mhdr.get("SALES_REP");
			String SerialNo = (String) mhdr.get("SERIAL_NO");
			String ReceiptNo = (String) mhdr.get("RECEIPT_NO");
			String Product = (String) mhdr.get("PRODUCT");
			String Description = (String) mhdr.get("PROD_DESC");
			String UnitPrice = (String) mhdr.get("UNIT_PRICE");
			String Qty = (String) mhdr.get("QTY");
			String Discount = (String) mhdr.get("DISCOUNT");
			String Total = (String) mhdr.get("TOTAL");
			String Subtotal = (String) mhdr.get("SUBTOTAL");
			String Tax = (String) mhdr.get("TAX");
			String TotalAmt = (String) mhdr.get("TOTAL_AMT");
			String PaymentPaid = (String) mhdr.get("AMOUNT_PAID");
			String ChangeRemaining = (String) mhdr.get("CHANGE");
			String DisplayByLoc = (String) mhdr.get("ADDRBYLOC");
			String HEADER = (String) mhdr.get("HDR");
			String GREET1 = (String) mhdr.get("G1");
			String GREET2 = (String) mhdr.get("G2");
			String FOOT1 = (String) mhdr.get("F1");
			String FOOT2 = (String) mhdr.get("F2");
			String UOM = (String) mhdr.get("UNITMO");
			String msgWithcompanyAdrr = GREET1 + "##" + GREET2 + "##" + FOOT1 + "##" + FOOT2 + "##";
			if(msgWithcompanyAdrr.equals("########"))
			{
				msgWithcompanyAdrr="";
			}
			if (DisplayByLoc.equals("1")) {
				if (PLANTDESC.length() > 0)
					msgWithcompanyAdrr = msgWithcompanyAdrr + PLANTDESC + ",";
				if (CADD1.length() > 0)
					msgWithcompanyAdrr = msgWithcompanyAdrr + " Address:" + CADD1 + ",";
				if (CADD2.length() > 0)
					msgWithcompanyAdrr = msgWithcompanyAdrr + CADD2 + ",";
				if (CADD3.length() > 0)
					msgWithcompanyAdrr = msgWithcompanyAdrr + CADD3 + ",";
				if (CADD4.length() > 0)
					msgWithcompanyAdrr = msgWithcompanyAdrr + CADD4 + ",";
				if (CCOUNTRY.length() > 0)
					msgWithcompanyAdrr = msgWithcompanyAdrr + CCOUNTRY;
				if (CZIP.length() > 0)
					msgWithcompanyAdrr = msgWithcompanyAdrr + "(" + CZIP + ")";
				if (CTEL.length() > 0)
					msgWithcompanyAdrr = msgWithcompanyAdrr + "Tel:" + CTEL + ",";
				if (CFAX.length() > 0)
					msgWithcompanyAdrr = msgWithcompanyAdrr + "Fax:" + CFAX + "##";
				if (CFAX == "null" || CFAX == "")
					msgWithcompanyAdrr = msgWithcompanyAdrr + "##";
				String strAdr2 = "";
				if (CWEBSITE.length() > 0)
					msgWithcompanyAdrr = msgWithcompanyAdrr + "Website:" + CWEBSITE;
				strAdr2 = strAdr2 + CWEBSITE;
				if (strAdr2.length() > 0) {
					strAdr2 = ",";
				}
				if (CRCBNO.length() > 0)
					// msgWithcompanyAdrr= msgWithcompanyAdrr +strAdr2+" Business Registration
					// No:"+CRCBNO ;
					msgWithcompanyAdrr = msgWithcompanyAdrr + strAdr2;
				if (CGSTREGNO.length() > 0)
					msgWithcompanyAdrr = msgWithcompanyAdrr + "," + " GST Registration No: " + CGSTREGNO;

				ArrayList listQry = lcUtil.getLocListDetails(PLANT, loc);
				for (int i = 0; i < listQry.size(); i++) {
					Map map1 = (Map) listQry.get(i);

					PLANTDESC = (String) map1.get("COMNAME");
					CADD1 = (String) map1.get("ADD1");
					CADD2 = (String) map1.get("ADD2");
					CADD3 = (String) map1.get("ADD3");
					CADD4 = (String) map1.get("ADD4");
					CCOUNTRY = (String) map1.get("COUNTRY");
					CZIP = (String) map1.get("ZIP");
					CTEL = (String) map1.get("TELNO");
					CFAX = (String) map1.get("FAX");
					CRCBNO = StrUtils.fString((String) map1.get("RCBNO"));
					// CONTACTNAME = "";
					// CHPNO =""; commented because CEMAIL, CONTACTNAME getting empty in reports
					// CEMAIL = "";

				}
			}
			if (tranid.length() > 0) ht.put("TRANID", tranid);
            if (PLANT.length() > 0) ht.put("PLANT", PLANT);
			ArrayList<Map<String, String>> listQry = poshdrdao.getsalesdetailsforsummary(PLANT, ht, "", "");
            for (Map<String, String> m1 : listQry) {
            	parameters.put("currentDate", m1.get("PURCHASEDATE"));
            	parameters.put("FROM_LOC", m1.get("LOC"));
            	parameters.put("TOLOC", m1.get("TOLOC"));
            }

			if (Orientation.equals("Portrait")) {
				jasperPath = DbBean.JASPER_INPUT + "/" + "rptPOSMovePortrait";
			}
			String[] receiptmsgarray = getReceiptMsg(msgWithcompanyAdrr);
			int index = 1;
			// POS tran Details
			for (int i = 0; i < receiptmsgarray.length; i++) {
				parameters.put("MSG" + index, receiptmsgarray[i]);
				index = index + 1;
			}

			parameters.put("rcbNo", CRCBNO);
			parameters.put("UOM", UOM);
			/*
			 * parameters.put("imagePath", imagePath); parameters.put("imagePath1",
			 * imagePath1);
			 */
			parameters.put("ReceiptNo", tranid);
			parameters.put("user", userid);
			parameters.put("company", PLANT);
			//parameters.put("currentDate", tranDate);
			parameters.put("curTime", SysTime);
			parameters.put("heading", HEADER);
			parameters.put("lblSalesRep", SalesRep);
			parameters.put("lblSerialNo", SerialNo);
			parameters.put("lblReceiptNo", ReceiptNo);
			parameters.put("lblItem", Product);
			parameters.put("lblDescription", Description);
			parameters.put("lblQty", Qty);
			parameters.put("fromAddress_CompanyName", PLANTDESC);
			parameters.put("fromAddress_BlockAddress", CADD1 + "  " + CADD2);
			parameters.put("fromAddress_RoadAddress", CADD3 + "  " + CADD4);
			parameters.put("fromAddress_Country", CCOUNTRY);
			parameters.put("fromAddress_ZIPCode", CZIP);
			parameters.put("fromAddress_TpNo", CTEL);
			parameters.put("fromAddress_FaxNo", CFAX);
			parameters.put("FromHeader", "From");
			if (!checkImageFile.exists()) {
				imagePath2 = imagePath1;
				imagePath = "";
			} else if (!checkImageFile1.exists()) {
				imagePath2 = imagePath;
				imagePath1 = "";
			} else {
				imagePath2 = "";
			}
			parameters.put("imagePath", imagePath);
			parameters.put("imagePath1", imagePath1);
			parameters.put("imagePath2", imagePath2);

			ShipHisDAO _ShipHisDAO = new ShipHisDAO();

			ArrayList shipHisDetailsList = _ShipHisDAO.getShipHisDetailByTranId(PLANT, tranid);

			String recDetRefNO = "";
			String recDetEmpNO = "";
			String empFirstName = "";
			String empLastName = "";

			if (emp.length() > 1)
			parameters.put("EMPNAME", emp);
			else
			parameters.put("EMPNAME", "");
			parameters.put("REFNO", refNo);
			if (CONTACTNAME.length() > 1) {
				parameters.put("fromAddress_ContactPersonName", CONTACTNAME);
			} else {
				parameters.put("fromAddress_ContactPersonName", "");

			}

			if (CHPNO.length() > 1) {
				parameters.put("fromAddress_ContactPersonMobile", CHPNO);
			} else {
				parameters.put("fromAddress_ContactPersonMobile", "");
			}
			if (CEMAIL.length() > 1) {
				parameters.put("fromAddress_ContactPersonEmail", CEMAIL);
			} else {
				parameters.put("fromAddress_ContactPersonEmail", "");

			}
			
			if(!COMP_INDUSTRY.equalsIgnoreCase("Centralised Kitchen"))
			{
				if(CADD1.equals("")) {
					parameters.put("fromAddress_BlockAddress", CADD2);
				}else {
					parameters.put("fromAddress_BlockAddress", CADD1 + ", " + CADD2);
				}
				if(CADD3.equals("")) {
					parameters.put("fromAddress_RoadAddress", CADD4);
				}else {
					parameters.put("fromAddress_RoadAddress", CADD3 + "," + CADD4);
				}
				if(CSTATE.equals("")) {
					parameters.put("fromAddress_Country", CSTATE + "" + CCOUNTRY);
				}else {
					parameters.put("fromAddress_Country", CSTATE + "," + CCOUNTRY);
				}
			} else {
				String fromAddress_BlockAddress="";
				if(CADD2.length()>0)
					fromAddress_BlockAddress=CADD2 + ", ";
					parameters.put("fromAddress_BlockAddress", fromAddress_BlockAddress + CADD3); //1.Building+Street
					if(CADD1.equals("")) {
						parameters.put("fromAddress_RoadAddress", CADD1); //Unit No.
					}else {
						parameters.put("fromAddress_RoadAddress", " "+CADD1 + ",");
					}
					if(CSTATE.equals("")) {
						parameters.put("fromAddress_Country",  CCOUNTRY);
					}else {
						parameters.put("fromAddress_Country", CSTATE + "," + CCOUNTRY);
					}
			}
			parameters.put("fromAddress_ZIPCode", CZIP);
			parameters.put("fromAddress_TpNo", CTEL);
			parameters.put("fromAddress_FaxNo", CFAX);
			parameters.put("fromAddress_ContactPersonName", CONTACTNAME);
			parameters.put("fromAddress_ContactPersonMobile", CHPNO);
			parameters.put("fromAddress_ContactPersonEmail", CEMAIL);
			parameters.put("fromAddress_RCBNO", CRCBNO);
			parameters.put("fromAddress_UENNO", companyregno);//imtiuen
			parameters.put("fromAddress_Website", CWEBSITE);
			parameters.put("PRINTUENNO","1");
			parameters.put("TELEPHONE","Tel No.:");
			parameters.put("HANDPHONE","HP No.:");
			parameters.put("UENNO","GST No.");
			
			// parameters.put("referanceNO",CRCBNO);
			String gstValue = new selectBean().getGST("POS", PLANT);

			long start = System.currentTimeMillis();
			System.out.println("**************" + " Start Up Time : " + start + "**********");
			System.out.println("**************" + " Jasper path stock move: " + jasperPath + "**********");
			JasperCompileManager.compileReportToFile(jasperPath + ".jrxml", jasperPath + ".jasper");
			byte[] bytes = JasperRunManager.runReportToPdf(jasperPath + ".jasper", parameters, con);
			
			//response.addHeader("Content-disposition", "attachment;filename=reporte.pdf");
			response.addHeader("Content-disposition", "inline;filename=reporte.pdf");
			response.setContentLength(bytes.length);
			response.getOutputStream().write(bytes);
			response.setContentType("application/pdf");
			response.getOutputStream().flush();
			response.getOutputStream().close();
		} catch (IOException e) {

			e.printStackTrace();

		} finally {
			if (con != null) {
				DbBean.closeConnection(con);
			}
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

	public boolean CheckInvMst(Map<String, String> map, HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		boolean flag = false;
		String extCond = "";
		InvMstDAO _InvMstDAO = new InvMstDAO();
		_InvMstDAO.setmLogger(mLogger);
		Boolean batchChecked = false;
		String posType = StrUtils.fString(request.getParameter("POS_TYPE")).trim();
		String tranType = StrUtils.fString(request.getParameter("TRANTYPE")).trim();
		try {
			String Tranid = StrUtils.fString(request.getParameter("TRANID"));

			Hashtable<String, String> htInvMst = new Hashtable<String, String>();
			htInvMst.clear();
			HttpSession session = request.getSession();
			htInvMst.put(IDBConstants.PLANT, map.get(IConstants.PLANT));
			htInvMst.put(IDBConstants.ITEM, map.get(IConstants.ITEM));
			htInvMst.put(IDBConstants.LOC, map.get(IConstants.LOC));
			htInvMst.put(IDBConstants.USERFLD4, map.get(IConstants.BATCH));
			StringBuffer sql = new StringBuffer(" ");
			List invlist = _InvMstDAO.selectInvMstByCrat("item,loc,userfld4 as batch,qty,crat", htInvMst);
			String actualqty = "";
			double actqty = 0;
			double lnqty = 0, balancqty = 0;
			double totalqty = 0;
			actualqty = map.get(IConstants.QTY);
			//actualqty = StrUtils.TrunkateDecimalForImportData(actualqty);
			actqty = Double.valueOf(actualqty);

			if (request.getParameter("chkbatch") != null) {
				batchChecked = true;
			}

			// Calculate total qty in the loc
			for (int j = 0; j < invlist.size(); j++) {
				Map lineitem = (Map) invlist.get(j);
				String lineitemqty = (String) lineitem.get("qty");
				//lineitemqty = StrUtils.TrunkateDecimalForImportData(lineitemqty);

				totalqty = totalqty + Double.valueOf(lineitemqty);

			}

			if (actqty > totalqty) {
				String msg = "Not Enough Inventory For Product";
				Exception e = null;
				e.printStackTrace(new PrintWriter(msg));
				throw e;
			}

			if (totalqty == 0) {
				String msg = "Not Enough Inventory For Product";
				Exception e = null;
				e.printStackTrace(new PrintWriter(msg));
				throw e;
			}

		} catch (Exception e) {
			if (tranType.equalsIgnoreCase("GOODSISSUEWITHOUTBATCH")) {
				// response.sendRedirect("jsp/MultiMiscOrderIssuing.jsp?TRANID="+request.getParameter("TRANID")+"&CHKBATCH="+batchChecked+"&LOC="+request.getParameter("LOC"));
				RequestDispatcher view = request.getRequestDispatcher("jsp/MultiMiscOrderIssuing.jsp?TRANID="
						+ request.getParameter("TRANID") + "&CHKBATCH=" + batchChecked + "&LOC="
						+ request.getParameter("LOC") + "&TOLOC=" + request.getParameter("TOLOC") + "&FROM_LOCDESC="
						+ request.getParameter("FROM_LOCDESC") + "&TOLOCDESC=" + request.getParameter("TOLOCDESC"));
				view.forward(request, response);
			} else if (tranType.equalsIgnoreCase("GOODSISSUEWITHBATCH")) {
				// response.sendRedirect("jsp/dynamicprdswithoutprice.jsp?TRANID="+request.getParameter("TRANID")+"&CHKBATCH="+batchChecked+"&LOC="+request.getParameter("LOC"));

				// RequestDispatcher view =
				// request.getRequestDispatcher("jsp/dynamicprdswithoutprice.jsp?TRANID="+request.getParameter("TRANID")+"&CHKBATCH="+batchChecked+"&LOC="+request.getParameter("LOC"));
				// view.forward(request, response);
				response.sendRedirect("../salestransaction/goodsissue?TRANID=" + request.getParameter("TRANID")
						+ "&CHKBATCH=" + batchChecked + "&LOC=" + request.getParameter("LOC") + "&TOLOC="
						+ request.getParameter("TOLOC") + "&FROM_LOCDESC=" + request.getParameter("FROM_LOCDESC")
						+ "&TOLOCDESC=" + request.getParameter("TOLOCDESC"));
			} else if (tranType.equalsIgnoreCase("GOODSISSUEWITHBATCHPRICE")) {
					
					response.sendRedirect("jsp/dynamicprdswithprice.jsp?TRANID=" + request.getParameter("TRANID")
							+ "&CHKBATCH=" + batchChecked + "&LOC=" + request.getParameter("LOC") + "&TOLOC="
							+ request.getParameter("TOLOC") + "&FROM_LOCDESC=" + request.getParameter("FROM_LOCDESC")
							+ "&TOLOCDESC=" + request.getParameter("TOLOCDESC"));
			} else if (posType.equalsIgnoreCase("WITHOUTPRICE")) {
				// response.sendRedirect("jsp/dynamicprdswithoutprice.jsp?TRANID="+request.getParameter("TRANID")+"&CHKBATCH="+batchChecked+"&LOC="+request.getParameter("LOC"));
				RequestDispatcher view = request.getRequestDispatcher("../salestransaction/goodsissue?TRANID="
						+ request.getParameter("TRANID") + "&CHKBATCH=" + batchChecked + "&LOC="
						+ request.getParameter("LOC") + "&TOLOC=" + request.getParameter("TOLOC") + "&FROM_LOCDESC="
						+ request.getParameter("FROM_LOCDESC") + "&TOLOCDESC=" + request.getParameter("TOLOCDESC"));
				view.forward(request, response);
			} else {
				// response.sendRedirect("jsp/dynamicprds.jsp?TRANID="+request.getParameter("TRANID")+"&CHKBATCH="+batchChecked+"&LOC="+request.getParameter("LOC"));
				RequestDispatcher view = request.getRequestDispatcher("jsp/dynamicprds.jsp?TRANID="
						+ request.getParameter("TRANID") + "&CHKBATCH=" + batchChecked + "&LOC="
						+ request.getParameter("LOC") + "&TOLOC=" + request.getParameter("TOLOC") + "&FROM_LOCDESC="
						+ request.getParameter("FROM_LOCDESC") + "&TOLOCDESC=" + request.getParameter("TOLOCDESC"));
				view.forward(request, response);
			}

		}

		return flag;
	}

	public boolean CheckInvMstForGoddsIssue(Map<String, String> map, HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		boolean flag = true;
		String extCond = "";
		InvMstDAO _InvMstDAO = new InvMstDAO();
		_InvMstDAO.setmLogger(mLogger);
		Boolean batchChecked = false;
		String posType = StrUtils.fString(request.getParameter("POS_TYPE")).trim();
		String tranType = StrUtils.fString(request.getParameter("TRANTYPE")).trim();
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
			actqty = Double.valueOf(actualqty);

			if (request.getParameter("chkbatch") != null) {
				batchChecked = true;
			}

			// Calculate total qty in the loc
			for (int j = 0; j < invlist.size(); j++) {
				Map lineitem = (Map) invlist.get(j);
				String lineitemqty = (String) lineitem.get("qty");
				//lineitemqty = StrUtils.TrunkateDecimalForImportData(lineitemqty);

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
			if (tranType.equalsIgnoreCase("GOODSISSUEWITHOUTBATCH")) {
				flag = false;
				// response.sendRedirect("jsp/MultiMiscOrderIssuing.jsp?TRANID="+request.getParameter("TRANID")+"&CHKBATCH="+batchChecked+"&LOC="+request.getParameter("LOC"));
				/*
				 * RequestDispatcher view =
				 * request.getRequestDispatcher("jsp/MultiMiscOrderIssuing.jsp?TRANID="+request.
				 * getParameter("TRANID")+"&CHKBATCH="+batchChecked+"&LOC="+request.getParameter
				 * ("LOC") +"&TOLOC="+request.getParameter("TOLOC")+"&FROM_LOCDESC="+request.
				 * getParameter("FROM_LOCDESC")+"&TOLOCDESC="+request.getParameter("TOLOCDESC"))
				 * ; view.forward(request, response);
				 */
				// throw e;
			} else if (tranType.equalsIgnoreCase("GOODSISSUEWITHBATCH")) {
				flag = false;
				// response.sendRedirect("jsp/dynamicprdswithoutprice.jsp?TRANID="+request.getParameter("TRANID")+"&CHKBATCH="+batchChecked+"&LOC="+request.getParameter("LOC"));

				/*
				 * RequestDispatcher view =
				 * request.getRequestDispatcher("jsp/dynamicprdswithoutprice.jsp?TRANID="+
				 * request.getParameter("TRANID")+"&CHKBATCH="+batchChecked+"&LOC="+request.
				 * getParameter("LOC")
				 * +"&TOLOC="+request.getParameter("TOLOC")+"&FROM_LOCDESC="+request.
				 * getParameter("FROM_LOCDESC")+"&TOLOCDESC="+request.getParameter("TOLOCDESC"))
				 * ; view.forward(request, response);
				 */
				 } else if (tranType.equalsIgnoreCase("GOODSISSUEWITHBATCHPRICE")) {
					flag = false;
					
			}
			else if (tranType.equalsIgnoreCase("MOVEWITHBATCH")) {
				// response.sendRedirect("jsp/dynamicprdswithoutprice.jsp?TRANID="+request.getParameter("TRANID")+"&CHKBATCH="+batchChecked+"&LOC="+request.getParameter("LOC"));
				/*
				 * RequestDispatcher view =
				 * request.getRequestDispatcher("jsp/dynamicprdswithoutprice.jsp?TRANID="+
				 * request.getParameter("TRANID")+"&CHKBATCH="+batchChecked+"&LOC="+request.
				 * getParameter("LOC")
				 * +"&TOLOC="+request.getParameter("TOLOC")+"&FROM_LOCDESC="+request.
				 * getParameter("FROM_LOCDESC")+"&TOLOCDESC="+request.getParameter("TOLOCDESC"))
				 * ; view.forward(request, response);
				 */
				flag = false;
			} else {
				// response.sendRedirect("jsp/dynamicprds.jsp?TRANID="+request.getParameter("TRANID")+"&CHKBATCH="+batchChecked+"&LOC="+request.getParameter("LOC"));
				/*
				 * RequestDispatcher view =
				 * request.getRequestDispatcher("jsp/MultiLocationTransferWithBatch.jsp?TRANID="
				 * +request.getParameter("TRANID")+"&CHKBATCH="+batchChecked+"&LOC="+request.
				 * getParameter("LOC")
				 * +"&TOLOC="+request.getParameter("TOLOC")+"&FROM_LOCDESC="+request.
				 * getParameter("FROM_LOCDESC")+"&TOLOCDESC="+request.getParameter("TOLOCDESC"))
				 * ; view.forward(request, response);
				 */
				flag = false;
			}

		}

		return flag;
	}

	private boolean processMovHisForPosWoInvCheck(Map map) throws Exception {
		boolean flag = false;
		MovHisDAO movHisDao = new MovHisDAO();
		movHisDao.setmLogger(mLogger);
		DateUtils dtUtils = new DateUtils();
		try {
			Hashtable htRecvHis = new Hashtable();
			htRecvHis.clear();
			htRecvHis.put(IDBConstants.PLANT, map.get(IConstants.PLANT));
			htRecvHis.put("DIRTYPE", map.get("DIRTYPE"));
			htRecvHis.put(IDBConstants.ITEM, map.get(IConstants.ITEM));
			htRecvHis.put("MOVTID", "");
			htRecvHis.put("RECID", "");
			htRecvHis.put(IDBConstants.LOC, map.get(IConstants.LOC));
			htRecvHis.put("BATNO", "");
			htRecvHis.put("QTY", map.get(IConstants.QTY));
			htRecvHis.put(IDBConstants.CREATED_BY, map.get(IConstants.LOGIN_USER));
			htRecvHis.put(IDBConstants.REMARKS, (map.get(IConstants.REMARKS) + "," + map.get(IConstants.REASONCODE)
					+ "," + map.get(IConstants.EMPNO) + "," + map.get(IConstants.RECVDATE)));
			htRecvHis.put(IDBConstants.TRAN_DATE, dtUtils.getDateinyyyy_mm_dd(dtUtils.getDate()));
			htRecvHis.put(IDBConstants.MOVHIS_ORDNUM, map.get(IConstants.MOVHIS_ORDNUM));
			htRecvHis.put(IDBConstants.CREATED_AT, dtUtils.getDateTime());
			flag = movHisDao.insertIntoMovHis(htRecvHis);

		} catch (Exception e) {

			this.mLogger.exception(this.printLog, "", e);
			throw e;

		}
		return flag;
	}

	public boolean processIssueHis(Map map) throws Exception {
		boolean flag = false;
		String rems = "";
		DateUtils dtUtils = new DateUtils();
		ShipHisDAO shiphstdao = new ShipHisDAO();
		StrUtils su = new StrUtils();
		shiphstdao.setmLogger(mLogger);
		try {

			Hashtable htIssueDet = new Hashtable();
			Hashtable htInvMst = new Hashtable();
			htInvMst.clear();
			htInvMst.put(IDBConstants.PLANT, map.get(IConstants.PLANT));
			htInvMst.put(IDBConstants.ITEM, map.get(IConstants.ITEM));
			htInvMst.put(IDBConstants.LOC, map.get(IConstants.LOC));
			htInvMst.put(IDBConstants.USERFLD4, map.get(IConstants.BATCH));
			String extCond = "";
			if (map.get(IConstants.BATCH_ID) != null && !"".equals(StrUtils.fString(map.get(IConstants.BATCH_ID).toString())) && !"-1".equals(map.get(IConstants.BATCH_ID).toString())) {
				htInvMst.put(IDBConstants.INVID, map.get(IConstants.BATCH_ID));
				extCond = "QTY >= " + map.get(IConstants.QTY);
			}else{
				extCond = "QTY > 0";
			}
			InvMstDAO _InvMstDAO = new InvMstDAO();
			ArrayList alStock = _InvMstDAO.selectInvMstByCrat("ID, CRAT, QTY", htInvMst, extCond);
			if (!alStock.isEmpty()) {
				double quantityToAdjust = Double.parseDouble("" + map.get(IConstants.QTY));
				Iterator iterStock = alStock.iterator();
				while(quantityToAdjust > 0.0 && iterStock.hasNext()) {
					Map mapIterStock = (Map)iterStock.next();
					double currRecordQuantity = Double.parseDouble("" + mapIterStock.get(IConstants.QTY));
					double adjustedQuantity = quantityToAdjust > currRecordQuantity ? currRecordQuantity : quantityToAdjust;
					htIssueDet.clear();
					htIssueDet.put(IDBConstants.PLANT, map.get(IConstants.PLANT));
					//htIssueDet.put(IDBConstants.DODET_DONUM, map.get("TranId"));
					htIssueDet.put(IDBConstants.DODET_DONUM, "");
					htIssueDet.put(IDBConstants.CUSTOMER_NAME, "");
					htIssueDet.put(IDBConstants.ITEM, map.get(IConstants.ITEM));
					// Retrieve item description
					ItemMstDAO itM = new ItemMstDAO();
					itM.setmLogger(mLogger);
					String sDesc = itM.getItemDesc((String) map.get(IConstants.PLANT), (String) map.get(IConstants.ITEM));
					htIssueDet.put(IDBConstants.ITEM_DESC, su.InsertQuotes(sDesc));
					htIssueDet.put("BATCH", map.get(IConstants.BATCH));
					htIssueDet.put(IDBConstants.LOC, map.get(IConstants.LOC));
					htIssueDet.put("LOC1", map.get(IConstants.LOC));
					htIssueDet.put("DOLNO", map.get(IConstants.OUT_DOLNNO));
					htIssueDet.put("ORDQTY", map.get(IConstants.QTY));
					htIssueDet.put("PICKQTY", String.valueOf(adjustedQuantity));
					htIssueDet.put("REVERSEQTY", "0");
					htIssueDet.put("STATUS", "C");
					htIssueDet.put(IDBConstants.CREATED_BY, map.get(IConstants.LOGIN_USER));
					htIssueDet.put(IDBConstants.CREATED_AT, dtUtils.getDateTime());
					htIssueDet.put(IDBConstants.ISSUEDATE, map.get(IConstants.ISSUEDATE));
					htIssueDet.put(IDBConstants.EMPNO, map.get(IConstants.EMPNO));
					htIssueDet.put(IDBConstants.RSNCODE, map.get(IConstants.REASONCODE));
					htIssueDet.put(IDBConstants.REFNO, map.get(IConstants.REFNO));
					htIssueDet.put("REMARK", map.get(IConstants.REMARKS));
					htIssueDet.put("INVOICENO", map.get("TranId"));
					/*
					 * Hashtable htDoNO = new Hashtable(); htDoNO.put(IDBConstants.PLANT,
					 * map.get(IConstants.PLANT)); htDoNO.put(IDBConstants.DODET_DONUM,
					 * map.get("TranId")); boolean isDoNOExist=shiphstdao.isExisit(htDoNO);
					 * if(isDoNOExist==true){ flag = shiphstdao.update("", htIssueDet, ""); }
					 */
					htIssueDet.put(IDBConstants.INVID, mapIterStock.get(IDBConstants.INVID));
					
					if(map.get("DIRTYPE") =="TAXINVOICE_OUT")
					{
					htIssueDet.put("TRAN_TYPE", "TAXINVOICE");
					htIssueDet.put(IConstants.CURRENCYID, map.get(IConstants.CURRENCYID));
					htIssueDet.put(IConstants.INVOICENO, map.get(IConstants.INVOICENO));
					htIssueDet.put(IDBConstants.CUSTOMER_NAME, map.get(IConstants.DOHDR_CUST_NAME));
					htIssueDet.put(IDBConstants.PRICE, map.get(IConstants.PRICE));
					}
					else
						htIssueDet.put("TRAN_TYPE", map.get(IConstants.TRAN_TYPE));
					
					flag = shiphstdao.insertShipHis(htIssueDet);
					quantityToAdjust -= adjustedQuantity;
				}
			}

		} catch (Exception e) {

			this.mLogger.exception(this.printLog, "", e);
			throw e;

		}
		return flag;
	}

	public boolean processDOHDR(Map map) throws Exception {
		boolean flag = false;
		String rems = "";
		DateUtils dtUtils = new DateUtils();
		ShipHisDAO shiphstdao = new ShipHisDAO();
		StrUtils su = new StrUtils();
		shiphstdao.setmLogger(mLogger);
		try {
			Hashtable htIssueDet = new Hashtable();
			htIssueDet.clear();
			htIssueDet.put(IDBConstants.PLANT, map.get(IConstants.PLANT));
			htIssueDet.put(IDBConstants.DODET_DONUM, map.get("TranId"));
			htIssueDet.put(IDBConstants.ORDERTYPE, "");
			htIssueDet.put(IDBConstants.DEL_DATE, "");
			htIssueDet.put("STATUS", "C");

			htIssueDet.put(IDBConstants.POHDR_CUST_CODE, "");
			htIssueDet.put(IDBConstants.WOHDR_CUST_NAME, "");
			htIssueDet.put(IConstants.EST_REF_NO, map.get(IConstants.REFNO));
			htIssueDet.put(IDBConstants.POHDR_PERSON_INCHARGE, "");
			htIssueDet.put(IDBConstants.POHDR_ADDRESS, "");
			htIssueDet.put(IDBConstants.POHDR_ADDRESS2, "");
			htIssueDet.put(IDBConstants.POHDR_ADDRESS3, "");
			htIssueDet.put(IDBConstants.POHDR_COL_DATE, map.get(IConstants.ISSUEDATE));
			htIssueDet.put(IDBConstants.TOHDR_REMARK1, map.get(IConstants.REMARKS));
			htIssueDet.put(IDBConstants.TOHDR_REMARK2, "");

			htIssueDet.put(IDBConstants.DELIVERYDATE, "");
			htIssueDet.put(IDBConstants.DOHDR_GST, "0");
			htIssueDet.put(IDBConstants.ORDSTATUSID, "");
			htIssueDet.put(IDBConstants.DOHDR_REMARK3, "");

			String baseCurrency = "";
			// Retrieve item description
			PlantMstDAO plantDAO = new PlantMstDAO();
			plantDAO.setmLogger(mLogger);
			ArrayList plantDetList = plantDAO.getPlantMstDetails((String) map.get(IConstants.PLANT));
			for (int i = 0; i < plantDetList.size(); i++) {
				Map plntdet = (Map) plantDetList.get(i);

				baseCurrency = (String) plntdet.get("BASE_CURRENCY");

			}

			htIssueDet.put(IDBConstants.CURRENCYID, baseCurrency);

			htIssueDet.put(IDBConstants.CURRENCYID, baseCurrency);
			String DONO = (String) map.get("TranId");
			String plant = (String) map.get(IConstants.PLANT);
			boolean istranidExist = shiphstdao.isExisited(plant, DONO);
			if (istranidExist == false) {
				flag = shiphstdao.insertDOHDR(htIssueDet);

				processDODET(map);
			}

		} catch (Exception e) {

			this.mLogger.exception(this.printLog, "", e);
			throw e;

		}
		return flag;
	}

	public boolean processDODET(Map map) throws Exception {
		boolean flag = false;
		String rems = "";
		DateUtils dtUtils = new DateUtils();
		ShipHisDAO shiphstdao = new ShipHisDAO();
		StrUtils su = new StrUtils();
		shiphstdao.setmLogger(mLogger);
		try {
			Hashtable htIssueDet = new Hashtable();
			htIssueDet.clear();
			htIssueDet.put(IDBConstants.PLANT, map.get(IConstants.PLANT));
			htIssueDet.put(IDBConstants.DODET_DONUM, map.get("TranId"));
			htIssueDet.put(IDBConstants.DODET_DOLNNO, map.get(IConstants.OUT_DOLNNO));
			htIssueDet.put(IDBConstants.DODET_PICKSTATUS, "C");
			htIssueDet.put(IDBConstants.LNSTAT, "C");
			htIssueDet.put(IDBConstants.ITEM, map.get(IConstants.ITEM));

			ItemMstDAO itM = new ItemMstDAO();
			itM.setmLogger(mLogger);
			String sDesc = itM.getItemDesc((String) map.get(IConstants.PLANT), (String) map.get(IConstants.ITEM));

			htIssueDet.put(IDBConstants.ITEM_DESC, su.InsertQuotes(sDesc));
			// Retrieve item description
			htIssueDet.put(IDBConstants.TRAN_DATE, map.get(IConstants.ISSUEDATE));

			String itemPrice = itM.getItemPrice((String) map.get(IConstants.PLANT), (String) map.get(IConstants.ITEM));

			htIssueDet.put(IDBConstants.DODET_UNITPRICE, itemPrice);

			htIssueDet.put(IDBConstants.DODET_QTYOR, map.get(IConstants.QTY));
			htIssueDet.put(IDBConstants.DODET_QTYIS, map.get(IConstants.QTY));

			htIssueDet.put(IDBConstants.DODET_ITEM_DESC, su.InsertQuotes(sDesc));
			htIssueDet.put(IConstants.INVMST_CUST_CODE, map.get(IConstants.REFNO));
			htIssueDet.put(IConstants.DODET_CUST_NAME, "");

			String baseCurrency = "";
			// Retrieve item description
			PlantMstDAO plantDAO = new PlantMstDAO();
			plantDAO.setmLogger(mLogger);
			ArrayList plantDetList = plantDAO.getPlantMstDetails((String) map.get(IConstants.PLANT));
			for (int i = 0; i < plantDetList.size(); i++) {
				Map plntdet = (Map) plantDetList.get(i);

				baseCurrency = (String) plntdet.get("BASE_CURRENCY");

			}

			String currencyUseQT = "";

			CurrencyDAO currDAO = new CurrencyDAO();
			currDAO.setmLogger(mLogger);
			ArrayList currDetList = currDAO.getCurrencyDetails(baseCurrency, (String) map.get(IConstants.PLANT));
			for (int i = 0; i < currDetList.size(); i++) {

				ArrayList al = (ArrayList) currDetList.get(i);
				currencyUseQT = (String) al.get(3);

			}

			htIssueDet.put(IConstants.CURRENCYUSEQT, currencyUseQT);

			flag = shiphstdao.insertDODET(htIssueDet);

		} catch (Exception e) {

			this.mLogger.exception(this.printLog, "", e);
			throw e;

		}
		return flag;
	}

	public boolean processTOHDR(Map map) throws Exception {
		boolean flag = false;
		String rems = "";
		DateUtils dtUtils = new DateUtils();
		ShipHisDAO shiphstdao = new ShipHisDAO();
		StrUtils su = new StrUtils();
		shiphstdao.setmLogger(mLogger);
		try {
			Hashtable htIssueDet = new Hashtable();
			htIssueDet.clear();
			htIssueDet.put(IDBConstants.PLANT, map.get(IConstants.PLANT));
			htIssueDet.put(IDBConstants.TOHDR_TONUM, map.get("TranId"));
			htIssueDet.put(IDBConstants.TOHDR_FROM_WAREHOUSE, map.get(IConstants.LOC));
			htIssueDet.put(IDBConstants.TOHDR_TO_WAREHOUSE, map.get(IConstants.LOC2));
			htIssueDet.put("STATUS", "C");

			htIssueDet.put(IDBConstants.POHDR_CUST_CODE, "");
			htIssueDet.put(IDBConstants.WOHDR_CUST_NAME, "");
			htIssueDet.put(IConstants.EST_REF_NO, map.get(IConstants.REFNO));
			htIssueDet.put(IDBConstants.POHDR_PERSON_INCHARGE, "");
			htIssueDet.put(IDBConstants.POHDR_ADDRESS, "");
			htIssueDet.put(IDBConstants.POHDR_ADDRESS2, "");
			htIssueDet.put(IDBConstants.POHDR_ADDRESS3, "");
			htIssueDet.put(IDBConstants.POHDR_COL_DATE, map.get(IConstants.TRAN_DATE));
			htIssueDet.put(IDBConstants.TOHDR_REMARK1, map.get(IConstants.REMARKS));
			htIssueDet.put(IDBConstants.TOHDR_REMARK2, "");
			htIssueDet.put(IDBConstants.TOHDR_SCUST_NAME, "");
			htIssueDet.put(IDBConstants.TOHDR_SCONTACT_NAME, "");
			htIssueDet.put(IDBConstants.TOHDR_SADDR1, "");
			htIssueDet.put(IDBConstants.TOHDR_SADDR2, "");
			htIssueDet.put(IDBConstants.TOHDR_SCITY, "");
			htIssueDet.put(IDBConstants.TOHDR_SCOUNTRY, "");
			htIssueDet.put(IDBConstants.TOHDR_SZIP, "");
			flag = shiphstdao.insertTOHDR(htIssueDet);

			processTODET(map);

		} catch (Exception e) {

			this.mLogger.exception(this.printLog, "", e);
			throw e;

		}
		return flag;
	}

	public boolean processTODET(Map map) throws Exception {
		boolean flag = false;
		String rems = "";
		DateUtils dtUtils = new DateUtils();
		ShipHisDAO shiphstdao = new ShipHisDAO();
		StrUtils su = new StrUtils();
		shiphstdao.setmLogger(mLogger);
		try {
			Hashtable htIssueDet = new Hashtable();
			htIssueDet.clear();
			htIssueDet.put(IDBConstants.PLANT, map.get(IConstants.PLANT));
			htIssueDet.put(IDBConstants.TOHDR_TONUM, map.get("TranId"));
			htIssueDet.put(IDBConstants.TODET_TOLNNO, map.get(IConstants.TR_TOLNNO));
			htIssueDet.put(IDBConstants.DODET_PICKSTATUS, "C");
			htIssueDet.put(IDBConstants.LNSTAT, "C");
			htIssueDet.put(IDBConstants.ITEM, map.get(IConstants.ITEM));

			ItemMstDAO itM = new ItemMstDAO();
			itM.setmLogger(mLogger);
			String sDesc = itM.getItemDesc((String) map.get(IConstants.PLANT), (String) map.get(IConstants.ITEM));

			htIssueDet.put(IDBConstants.ITEM_DESC, su.InsertQuotes(sDesc));
			// Retrieve item description
			htIssueDet.put(IDBConstants.TRAN_DATE, map.get(IConstants.TRAN_DATE));

			htIssueDet.put(IDBConstants.DODET_QTYOR, map.get(IConstants.QTY));
			htIssueDet.put(IDBConstants.LOANDET_QTYRC, map.get(IConstants.QTY));

			htIssueDet.put(IDBConstants.DODET_ITEM_DESC, su.InsertQuotes(sDesc));
			htIssueDet.put(IConstants.INVMST_CUST_CODE, map.get(IConstants.REFNO));
			htIssueDet.put(IConstants.DODET_CUST_NAME, "");
			htIssueDet.put(IDBConstants.DODET_COMMENT1, map.get(IConstants.REMARKS));

			String itemUOM = itM.getItemUOM((String) map.get(IConstants.PLANT), (String) map.get(IConstants.ITEM));

			htIssueDet.put(IDBConstants.UNITMO, itemUOM);
			flag = shiphstdao.insertTODET(htIssueDet);

		} catch (Exception e) {

			this.mLogger.exception(this.printLog, "", e);
			throw e;

		}
		return flag;
	}

	public boolean processRecvDet(Map map) throws Exception {
		boolean flag = false;
		RecvDetDAO recvdetDao = new RecvDetDAO();
		recvdetDao.setmLogger(mLogger);
		StrUtils su = new StrUtils();
		DateUtils dateUtils = new DateUtils();
		try {
			Hashtable htRecvDet = new Hashtable();
			htRecvDet.clear();
			htRecvDet.put(IDBConstants.PLANT, map.get(IConstants.PLANT));
			//htRecvDet.put(IDBConstants.PODET_PONUM, map.get("TranId"));
			htRecvDet.put(IDBConstants.PODET_PONUM, "");
			htRecvDet.put(IDBConstants.CUSTOMER_NAME, "");
			htRecvDet.put(IDBConstants.ITEM, map.get(IConstants.ITEM));
			ItemMstDAO itM = new ItemMstDAO();
			itM.setmLogger(mLogger);
			String sDesc = itM.getItemDesc((String) map.get(IConstants.PLANT), (String) map.get(IConstants.ITEM));
			htRecvDet.put(IDBConstants.ITEM_DESC, su.InsertQuotes(sDesc));
			htRecvDet.put("BATCH", map.get(IConstants.BATCH));
			htRecvDet.put(IDBConstants.LOC, map.get(IConstants.LOC));
			htRecvDet.put("RECQTY", map.get(IConstants.QTY));
			htRecvDet.put(IDBConstants.CREATED_BY, map.get(IConstants.LOGIN_USER));
			htRecvDet.put(IDBConstants.CREATED_AT, dateUtils.getDateTime());
			htRecvDet.put("REMARK", map.get(IConstants.REMARKS));
			htRecvDet.put(IDBConstants.RSNCODE, map.get(IConstants.REASONCODE));
			htRecvDet.put(IDBConstants.EMPNO, map.get(IConstants.EMPNO));
			htRecvDet.put(IDBConstants.RECVDET_TRANTYPE, "GR");
			htRecvDet.put(IDBConstants.RECVDATE, map.get(IConstants.RECVDATE));
			htRecvDet.put(IDBConstants.LNNO, map.get(IConstants.LNNO));
			htRecvDet.put("GRNO",map.get("TranId"));
			htRecvDet.put(IDBConstants.TRAN_TYPE,  map.get(IConstants.TRAN_TYPE));
			flag = recvdetDao.insertRecvDet(htRecvDet);

		} catch (Exception e) {

			this.mLogger.exception(this.printLog, "", e);
			throw e;

		}
		return flag;
	}

	private MLogger mLogger = new MLogger();

	public MLogger getmLogger() {
		return mLogger;
	}

	public void setmLogger(MLogger mLogger) {
		this.mLogger = mLogger;
	}
	private HSSFWorkbook writeToExcelSummary(HttpServletRequest request, HttpServletResponse response,
			HSSFWorkbook wb) {
		StrUtils strUtils = new StrUtils();
		HTReportUtil movHisUtil = new HTReportUtil();
		DateUtils _dateUtils = new DateUtils();
		ArrayList movQryList = new ArrayList();
		int maxRowsPerSheet = 65535;
		String fdate = "", tdate = "";
		try {

			String PLANT = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		}
		return wb;
	}
	private Vector<ITEMMST> editItemDetailsForPOSTranId(HttpServletRequest req, String TranId, Vector<ITEMMST> list)
			throws Exception {
		HttpSession session1 = req.getSession();
		Vector<ITEMMST> vecRec = new Vector<ITEMMST>();
		String item = "", itemdesc = "", batch = "", qty = "", price = "", TotalPrice = "", discnt = "",
				expirydate = ""; // minsprice = "";
//		float discount = 0;
//		String disccntstr = "";
		POSDetDAO posdetDAO = new POSDetDAO();
		String PLANT = session1.getAttribute("PLANT").toString();
		posdetDAO.setmLogger(mLogger);
		List prdlist = posdetDAO.listProductsForPOSTranID(PLANT, TranId, " TRANSTATUS in ('C','O','N')");
		float fgsttax = 0;
		List gsttax = _GstTypeUtil.qryGstType("POS", PLANT, " ");
		for (int i = 0; i < 1; i++) {
			Vector vecgsttax = (Vector) gsttax.get(0);
			fgsttax = Float.parseFloat((String) vecgsttax.get(2));
		}
		for (int i = 0; i < prdlist.size(); i++) {
			ITEMMST items = new ITEMMST();
			Vector vecItem = (Vector) prdlist.get(i);
			item = (String) vecItem.get(0);
			itemdesc = (String) vecItem.get(1);
			qty = (String) vecItem.get(2);
			//qty = StrUtils.TrunkateDecimalForImportData(qty);
			price = (String) vecItem.get(3);
			price = new POSUtil().getConvertedLocalCurrencyToUnitCost(PLANT, TranId, price);
			items.setUSERFLD4(price);
			TotalPrice = (String) vecItem.get(4);
			TotalPrice = new POSUtil().getConvertedLocalCurrencyToUnitCost(PLANT, TranId, TotalPrice);
			discnt = (String) vecItem.get(5);
			batch = (String) vecItem.get(6);
			expirydate = (String) vecItem.get(7);
			items.setEXPIREDATE(expirydate);
			items.setITEM(item);
			items.setITEMDESC(itemdesc);
			items.setBATCH(batch);
			float qtyf = Float.parseFloat(qty);
			items.setStkqty(qtyf);
			float pricef = Float.parseFloat(price);
			items.setUNITPRICE(pricef);
			double prval = Double.valueOf(price); 
			   BigDecimal bd = new BigDecimal(price);
			 System.out.println(bd);
			 DecimalFormat format = new DecimalFormat("#.#####");		
			 format.setRoundingMode(RoundingMode.FLOOR);
			 items.setUSERFLD6(format.format(bd));
			//items.setUSERFLD6(price);
			float dicountf = Float.parseFloat(discnt);
			items.setDISCOUNT(dicountf);
			float totalPrice = Float.parseFloat(TotalPrice);
			items.setTotalPrice(totalPrice);

			items.setEmpNo((String) vecItem.get(8));
			items.setRefNo((String) vecItem.get(9));
			items.setTranDate((String) vecItem.get(10));
			items.setReasonCode((String) vecItem.get(11));
			items.setRemarks((String) vecItem.get(12));
			items.setEmpName((String) vecItem.get(13));
			items.setLoc((String) vecItem.get(14));
			items.setFromLoc((String) vecItem.get(14));
			items.setToLoc((String) vecItem.get(15));
			items.setLocDesc((String) vecItem.get(16));
			items.setToLocDesc((String) vecItem.get(17));
			items.setSTKUOM((String) vecItem.get(18));
			items.setUSERFLD5((String) vecItem.get(19));
			if (fgsttax > 0) {
				items.setGSTTAX(fgsttax);
			}
			vecRec.addElement(items);

		}
		return vecRec;
	}
	
	private void viewTaxInvoiceReport(HttpServletRequest request, HttpServletResponse response, String tranid)
			throws IOException, Exception {
		java.sql.Connection con = null;
		try {
			DOUtil poUtil = new DOUtil();
			CustUtil cUtil = new CustUtil();
			PlantMstUtil pmUtil = new PlantMstUtil();
			selectBean sb = new selectBean();
			DOUtil _DOUtil = new DOUtil();
			CurrencyUtil currUtil = new CurrencyUtil();
			sb.setmLogger(mLogger);
			cUtil.setmLogger(mLogger);
			pmUtil.setmLogger(mLogger);
			currUtil.setmLogger(mLogger);
			Map m = null;
			HttpSession session = request.getSession();
			String CNAME = "", CADD1 = "", CADD2 = "", CADD3 = "", CADD4 = "", CZIP = "", CCOUNTRY = "", CSTATE="", CTEL = "",
					CFAX = "", CONTACTNAME = "", CHPNO = "", CEMAIL = "", CRCBNO = "";
			String DONO = "";
			//String[] chkdDoNo = request.getParameterValues("chkdDoNo");
			String[] chkdDoNo = new String[] {tranid};
			String PLANT = (String) session.getAttribute("PLANT");
			PlantMstDAO plantMstDAO = new PlantMstDAO();
	        int numberOfDecimal = Integer.parseInt(plantMstDAO.getNumberOfDecimal(PLANT));

			// ********To get from & to date issue date range*********************
			//String FROM_DATE = StrUtils.fString(request.getParameter("FROM_DATE"));
			//String TO_DATE = StrUtils.fString(request.getParameter("TO_DATE"));
			//String dtCondStr = "  and ISNULL(a.issuedate,'')<>'' AND CAST((SUBSTRING(a.issuedate, 7, 4) + '-' + SUBSTRING(a.issuedate, 4, 2) + '-' + SUBSTRING(a.issuedate, 1, 2)) AS date)";
			String sCondition = "", fdate = "", tdate = "", signaturePath = "";
			/*if (FROM_DATE.length() > 5)
				fdate = FROM_DATE.substring(6) + "-" + FROM_DATE.substring(3, 5) + "-" + FROM_DATE.substring(0, 2);
			if (TO_DATE == null)
				TO_DATE = "";
			else
				TO_DATE = TO_DATE.trim();
			if (TO_DATE.length() > 5)
				tdate = TO_DATE.substring(6) + "-" + TO_DATE.substring(3, 5) + "-" + TO_DATE.substring(0, 2);
			if (fdate.length() > 0) {
				sCondition = sCondition + dtCondStr + " >= '" + fdate + "'  ";
				if (tdate.length() > 0) {
					sCondition = sCondition + dtCondStr + " <= '" + tdate + "'  ";
				}
			} else {
				if (tdate.length() > 0) {
					sCondition = sCondition + dtCondStr + "  <= '" + tdate + "'  ";
				}
			}*/

			// ********To get from & to date receive date range*********************

			String imagePath = DbBean.COMPANY_LOGO_PATH + "/" + PLANT.toLowerCase() + DbBean.LOGO_FILE;
			String imagePath1 = DbBean.COMPANY_LOGO_PATH + "/" + PLANT.toLowerCase() + "Logo1.gif";
			String imagePath2, Orientation = "", PRINTDELIVERYNOTE = "", PRINTPACKINGLIST = "";

			File checkImageFile = new File(imagePath);
			if (!checkImageFile.exists()) {
				imagePath = DbBean.COMPANY_LOGO_PATH + "/" + DbBean.NO_LOGO_FILE;
			}
			File checkImageFile1 = new File(imagePath1);
			if (!checkImageFile1.exists()) {
				imagePath1 = DbBean.COMPANY_LOGO_PATH + "/" + DbBean.NO_LOGO_FILE;
			}

			con = DbBean.getConnection();

			String SysDate = DateUtils.getDate();
			String jasperPath = DbBean.JASPER_INPUT + "/" + "TaxInvoiceWithCustomer";
			Date sysTime1 = new Date(System.currentTimeMillis());
			Date cDt = new Date(System.currentTimeMillis());
			ArrayList listQry = pmUtil.getPlantMstDetails(PLANT);
			List jasperPrintList = new ArrayList();
			for (int i = 0; i < listQry.size(); i++) {
				Map map = (Map) listQry.get(i);

				CNAME = (String) map.get("PLNTDESC");
				CADD1 = (String) map.get("ADD1");
				CADD2 = (String) map.get("ADD2");
				CADD3 = (String) map.get("ADD3");
				CADD4 = (String) map.get("ADD4");
				CCOUNTRY = (String) map.get("COUNTY");
				CSTATE = (String) map.get("STATE");
				CZIP = (String) map.get("ZIP");
				CTEL = (String) map.get("TELNO");
				CFAX = (String) map.get("FAX");
				CONTACTNAME = (String) map.get("NAME");
				CHPNO = (String) map.get("HPNO");
				CEMAIL = (String) map.get("EMAIL");
				CRCBNO = (String) map.get("RCBNO");

			}
			for (int i = 0; i < chkdDoNo.length; i++) {
				//	Get all INVOICENOs from SHIPHIS table
				String query = "DISTINCT isnull(invoiceno, '') as INVOICENO,ISNULL(issuedate,'') as ISSUEDATE";
				DONO = chkdDoNo[i];
				Hashtable htCond = new Hashtable();
				htCond.put("PLANT", PLANT);
				htCond.put("DONO", DONO);
				ArrayList alShipDetail = new ArrayList();
				Map ma = poUtil.getDOReceiptInvoiceHdrDetailsDO(PLANT, "Tax Invoice English");		
				String PRINTMULTILANG =  (String) ma.get("PRINTMULTILANG");
				String MPRINTHDR = "Tax Invoice English";
				if(PRINTMULTILANG.equals("1"))
				{
					MPRINTHDR = "Tax Invoice Other Languages";
					ma = poUtil.getDOReceiptInvoiceHdrDetailsDO(PLANT, MPRINTHDR);	
				}
				String PRINTWITHTAXINVOICE= (String) ma.get("PRINTWITHTAXINVOICE");
				if (PRINTWITHTAXINVOICE.equals("1")) {
					DOUtil dOUtil = new DOUtil();
					alShipDetail = dOUtil.getOBIssueList(query, htCond, " and INVOICENO!='' and  INVOICENO is not null");
				}
				htCond.remove("DONO");
				htCond.put("POSTRANID", DONO);
				for(int j = 0; j < (request.getParameter("printwithinvoiceno") != null ? alShipDetail.size() : 1); j ++) {
					String INVOICENO = "",ISSUEDATE="";
					if (PRINTWITHTAXINVOICE.equals("1")) {
						if(alShipDetail.size()>0)
						{
						INVOICENO = ((Map)alShipDetail.get(j)).get("INVOICENO").toString();
						ISSUEDATE = ((Map)alShipDetail.get(j)).get("ISSUEDATE").toString();
						}						
					}
						jasperPath = DbBean.JASPER_INPUT + "/" + "TaxInvoiceWith";						
					
					
					signaturePath = DbBean.COMPANY_SIGN_PATH + "/" + PLANT.toLowerCase() + "/" + "sign" + DONO + ".bmp";
					File checkSignatureFile = new File(signaturePath);
					if (!checkSignatureFile.exists()) {
						signaturePath = DbBean.COMPANY_LOGO_PATH + "/" + "NoLogo.jpg";
					}
					ArrayList arrCust = cUtil.getCustomerDetailsForTaxInv(DONO, PLANT);
					if (arrCust.size() > 0) {
						String sCustCode = (String) arrCust.get(0);
						String sSHIPPINGID = (String) arrCust.get(25);
						String sCustName = (String) arrCust.get(1);
						String sCashCust= (String) arrCust.get(27);
						
							if(sCustCode.equals("CASH"))
							{
								jasperPath=jasperPath+"OutCustomer";
								if(sCustName=="")
									sCustName ="CASH";
							}
							else
							{
								 if(sSHIPPINGID==""||sSHIPPINGID=="-1")
									 jasperPath=jasperPath+"Shipping";
								 else
									 jasperPath=jasperPath+"Customer";		 
							}
								
						
						String sAddr1 = (String) arrCust.get(2);
						String sAddr2 = (String) arrCust.get(3);
						String sAddr3 = (String) arrCust.get(4);
						String sCountry = (String) arrCust.get(5);
						String sZip = (String) arrCust.get(6);
						String sCons = (String) arrCust.get(7);
						String sCustNameL = (String) arrCust.get(8);
						String sContactName = (String) arrCust.get(9);
						String sDesgination = (String) arrCust.get(10);
						String sTelNo = (String) arrCust.get(11);
						String sHpNo = (String) arrCust.get(12);
						String sFax = (String) arrCust.get(13);
						String sEmail = (String) arrCust.get(14);
						String sRemarks = (String) arrCust.get(15);
						String sAddr4 = (String) arrCust.get(16);

						String SHIPPINGID = (String) arrCust.get(25);
						String sRcbno = (String) arrCust.get(26);
						ArrayList arrShippingDetails = _masterUtil.getTaxInvShippingDetails(DONO, SHIPPINGID, PLANT);
						Map parameters = new HashMap();
						if (request.getParameter("with_batch") != null
								&& "yes".equals(request.getParameter("with_batch"))) {
							parameters.put("PrintBatch", "yes");
						}
						String sShipCustName = "";
						if (arrShippingDetails.size() > 0) {
							parameters.put("shipToId", SHIPPINGID);
							sShipCustName = (String) arrShippingDetails.get(1);
							String sShipContactName = (String) arrShippingDetails.get(2);
							String sshipTelno = (String) arrShippingDetails.get(3);
							String sShipPhone = (String) arrShippingDetails.get(4);
							String sShipFax = (String) arrShippingDetails.get(5);
							String sShipEmail = (String) arrShippingDetails.get(6);
							String sShipAddr1 = (String) arrShippingDetails.get(7);
							String sShipAddr2 = (String) arrShippingDetails.get(8);
							String sShipStreet = (String) arrShippingDetails.get(9);
							String sShipCity = (String) arrShippingDetails.get(10);
							String sShipState = (String) arrShippingDetails.get(11);
							String sShipCountry = (String) arrShippingDetails.get(12);
							String sShipZip = (String) arrShippingDetails.get(13);
							
							parameters.put("STo_Addr1", sShipAddr1);
							parameters.put("STo_Addr2", sShipAddr2);
							parameters.put("STo_Addr3", sShipStreet);
							parameters.put("STo_City", sShipCity);
							parameters.put("STo_Country", sShipState + "," + sShipCountry);
							System.out.println("STo_Country : " + parameters.get("STo_Country"));
							parameters.put("STo_ZIP", sShipZip);
							if (sshipTelno.length() > 0) {
								parameters.put("STo_Telno", "Tel : " + sshipTelno);
							} else {
								parameters.put("STo_Telno", sshipTelno);
							}
							if (sShipContactName.length() > 0) {
								parameters.put("STo_AttentionTo", "Attn : " + sShipContactName);
							} else {
								parameters.put("STo_AttentionTo", sShipContactName);
							}
							if (sShipPhone.length() > 0) {
								parameters.put("STo_Phone", "HP : " + sShipPhone);
							} else {
								parameters.put("STo_Phone", sShipPhone);
							}
							//Multi Language
							if(PRINTMULTILANG.equals("1"))
							{
							if (sshipTelno.length() > 0) {
								parameters.put("STo_Telno", sshipTelno);
							}
							if (sShipContactName.length() > 0) {
								parameters.put("STo_AttentionTo", sShipContactName);
							}
							if (sShipPhone.length() > 0) {
								parameters.put("STo_Phone", sShipPhone);
							}
							}
							//
							
						} else {
							parameters.put("shipToId", "");
						}
						query = "currencyid,isnull(outbound_Gst,0) as outbound_Gst,isnull(orderdiscount,0) orderdiscount,"
								+ "CRBY,TRANDT,TRANTM,JobNum,isnull(shippingcost,0) shippingcost,isnull(PAYMENTTYPE,'') PAYMENTTYPE," + "isnull(incoterms,'') incoterms";
						ArrayList arraydohdr = new ArrayList();
						arraydohdr = new POSHdrDAO().selectPosHdr(query, htCond);
						Map dataMap = (Map) arraydohdr.get(0);
						String gstValue = dataMap.get("outbound_Gst").toString();
						String DATAORDERDISCOUNT = dataMap.get("orderdiscount").toString();
						String DATASHIPPINGCOST = dataMap.get("shippingcost").toString();
						String DATAINCOTERMS = dataMap.get("incoterms").toString();
						String orderRemarks = (String) arrCust.get(18);
						String orderRemarksMultiLang1 = (String) arrCust.get(18);
						String sPayTerms = (String) arrCust.get(19);
						String orderRemarks3 = (String) arrCust.get(21);
						String orderRemarksMultiLang2 = (String) arrCust.get(21);
						String sDeliveryDate = (String) arrCust.get(22);
						String sEmpno = (String) arrCust.get(23);
						String sState = (String) arrCust.get(24);
						String orderType = new POSHdrDAO().getOrderTypeForTaxInv(PLANT, DONO);
						parameters.put("imagePath", imagePath);
						parameters.put("imagePath1", imagePath1);

						parameters.put("signaturePath", signaturePath);
						// Customer Details
						parameters.put("OrderNo", DONO);
						parameters.put("company", PLANT);
						parameters.put("taxInvoiceTo_CompanyName", sCustName);
						parameters.put("taxInvoiceTo_BlockAddress", sAddr1 + "  " + sAddr2);
						parameters.put("taxInvoiceTo_RoadAddress", sAddr3 + "  " + sAddr4);
						parameters.put("taxInvoiceTo_Country", sState + ", " + sCountry);
						parameters.put("taxInvoiceTo_ZIPCode", sZip);
						parameters.put("taxInvoiceTo_AttentionTo", sContactName);
						parameters.put("taxInvoiceTo_CCTO", "");
						parameters.put("To_TelNo", sTelNo);
						parameters.put("To_Fax", sFax);
						parameters.put("To_Email", sEmail);
						parameters.put("sRCBNO", sRcbno);
						// Company Details
						parameters.put("fromAddress_CompanyName", CNAME);
						parameters.put("fromAddress_BlockAddress", CADD1 + "  " + CADD2);
						parameters.put("fromAddress_RoadAddress", CADD3 + "  " + CADD4);
						parameters.put("fromAddress_Country", CSTATE + ", " + CCOUNTRY);
						parameters.put("fromAddress_ZIPCode", CZIP);
						parameters.put("fromAddress_TpNo", CTEL);
						parameters.put("fromAddress_FaxNo", CFAX);
						parameters.put("fromAddress_ContactPersonName", CONTACTNAME);
						parameters.put("fromAddress_ContactPersonMobile", CHPNO);
						parameters.put("fromAddress_ContactPersonEmail", CEMAIL);
						parameters.put("fromAddress_RCBNO", CRCBNO);
						parameters.put("currentTime", dataMap.get("TRANDT").toString()+" "+dataMap.get("TRANTM").toString());
						parameters.put("taxInvoiceNo", "");
						parameters.put("InvoiceTerms", "");
						parameters.put("refNo", dataMap.get("JobNum").toString());
						//arraydohdr = _DOUtil.getDoHdrDetails(query, htCond);
						Hashtable curHash = new Hashtable();
						curHash.put(IDBConstants.PLANT, PLANT);
						curHash.put(IDBConstants.CURRENCYID, dataMap.get("currencyid"));
						String curDisplay = currUtil.getCurrencyID(curHash, "DISPLAY");
						OrderTypeDAO ODAO = new OrderTypeDAO();
						String orderDesc = "";

						
						ma = poUtil.getDOReceiptInvoiceHdrDetailsDO(PLANT, MPRINTHDR);
						Orientation = (String) ma.get("PrintOrientation");
						PRINTDELIVERYNOTE = (String)ma.get("PRINTDELIVERYNOTE");
						PRINTPACKINGLIST = (String)ma.get("PRINTPACKINGLIST"); 
						if (orderType.equals("Tax Invoice English")) {
							orderDesc = (String) ma.get("HDR1");
						} else {
							orderDesc = ODAO.getOrderTypeDesc(PLANT, orderType);
							
						}
						if(orderDesc=="" || orderDesc==null)
							orderDesc="TAX INVOICE";
						if (ma.get("DISPLAYBYORDERTYPE").equals("1"))
							parameters.put("OrderHeader", orderDesc);
						else
						{
							if((String) ma.get("HDR1")!="")
								parameters.put("OrderHeader", (String) ma.get("HDR1"));
							else
								parameters.put("OrderHeader", orderDesc);
						}
						// parameters.put("OrderHeader", (String) ma.get("HDR1"));
						parameters.put("ToHeader", (String) ma.get("HDR2"));
						parameters.put("FromHeader", (String) ma.get("HDR3"));
						parameters.put("Date", (String) ma.get("DATE"));
						parameters.put("OrderNoHdr", (String) ma.get("ORDERNO"));
						parameters.put("RefNo", (String) ma.get("REFNO"));
						parameters.put("Terms", (String) ma.get("TERMS"));
						parameters.put("PreBy", (String) ma.get("PREPAREDBY"));
						parameters.put("PreByUser", dataMap.get("CRBY").toString());
						if (ma.get("PRINTCUSTERMS").equals("1")) {
							//parameters.put("TermsDetails", sPayTerms);
							parameters.put("TermsDetails", dataMap.get("PAYMENTTYPE").toString());
						} else {
							parameters.put("TermsDetails", (String) ma.get("TERMSDETAILS"));
						}
						parameters.put("SoNo", (String) ma.get("SONO"));
						parameters.put("Item", (String) ma.get("ITEM"));
						parameters.put("Description", (String) ma.get("DESCRIPTION"));
						parameters.put("OrderQty", (String) ma.get("ORDERQTY"));
						parameters.put("UOM", (String) ma.get("UOM"));
						;
						parameters.put("Rate", (String) ma.get("RATE"));
						parameters.put("TaxAmount", (String) ma.get("TAXAMOUNT"));
						parameters.put("Amt", (String) ma.get("AMT"));
						parameters.put("SubTotal", (String) ma.get("SUBTOTAL") + " " + "(" + curDisplay + ")");
						String taxby = _PlantMstDAO.getTaxBy(PLANT);
						parameters.put("TAXBY", taxby);
						if (taxby.equalsIgnoreCase("BYORDER")) {
							parameters.put("TotalTax", (String) ma.get("TOTALTAX") + " " + "(" + gstValue + "%" + ")");
						} else {
							parameters.put("TotalTax", (String) ma.get("TOTALTAX"));
						}
						parameters.put("Total", (String) ma.get("TOTAL") + " " + "(" + curDisplay + ")");
						
						String PRINTROUNDOFFTOTALWITHDECIMAL =  (String) ma.get("PRINTROUNDOFFTOTALWITHDECIMAL");
						parameters.put("PrintRoundoffTotalwithDecimal", PRINTROUNDOFFTOTALWITHDECIMAL);
						parameters.put("RoundoffTotalwithDecimal", (String) ma.get("ROUNDOFFTOTALWITHDECIMAL") + " " + "(" + curDisplay + ")");
						
						int FooterLength = ((String)ma.get("F1")).length();
						int FooterLength1 = ((String)ma.get("F2")).length();
						int FooterLength2 = ((String)ma.get("F3")).length();
						int FooterLength3 = ((String)ma.get("F4")).length();
						int FooterLength4 = ((String)ma.get("F5")).length();
						int FooterLength5 = ((String)ma.get("F6")).length();
						int FooterLength6 = ((String)ma.get("F7")).length();
						int FooterLength7 = ((String)ma.get("F8")).length();
						int FooterLength8 = ((String)ma.get("F9")).length();
						int flength =FooterLength; 
						int flength1 =FooterLength1;
						int flength2 =FooterLength2;
						int flength3 =FooterLength3;
						int flength4 =FooterLength4; 
						int flength5 =FooterLength5;
						int flength6 =FooterLength6;
						int flength7 =FooterLength7;
						int flength8 =FooterLength8; 
												
						parameters.put("Footer1", (String) ma.get("F1"));
						parameters.put("Footer2", (String) ma.get("F2"));
						parameters.put("Footer3", (String) ma.get("F3"));
						parameters.put("Footer4", (String) ma.get("F4"));
						parameters.put("Footer5", (String) ma.get("F5"));
						parameters.put("Footer6", (String) ma.get("F6"));
						parameters.put("Footer7", (String) ma.get("F7"));
						parameters.put("Footer8", (String) ma.get("F8"));
						parameters.put("Footer9", (String) ma.get("F9"));
						parameters.put("lblINCOTERM", (String) ma.get("INCOTERM"));
						parameters.put("DATAINCOTERMS", DATAINCOTERMS);
						parameters.put("PRINTWITHPRODUCT", (String) ma.get("PRINTWITHPRODUCT"));
						parameters.put("PrdXtraDetails", (String) ma.get("PRINTXTRADETAILS"));
						parameters.put("RCBNO", (String) ma.get("RCBNO"));
						parameters.put("CUSTOMERRCBNO", (String) ma.get("CUSTOMERRCBNO"));
						parameters.put("TOTALAFTERDISCOUNT", (String) ma.get("TOTALAFTERDISCOUNT"));
						
						parameters.put("lblContactName", (String) ma.get("CONTACTNAME"));
						parameters.put("lblEmail", (String) ma.get("EMAIL"));
						parameters.put("lblFax", (String) ma.get("FAX"));
						parameters.put("lblTelephone", (String) ma.get("TELEPHONE"));
						parameters.put("lblHandphone", (String) ma.get("HANDPHONE"));
						parameters.put("lblAttention", (String) ma.get("ATTENTION"));
						parameters.put("lblQtyTotal", (String) ma.get("QTYTOTAL"));
						parameters.put("lblremark3", (String) ma.get("REMARK3"));
						parameters.put("lblFooterPage", (String) ma.get("FOOTERPAGE"));
						parameters.put("lblFooterOf", (String) ma.get("FOOTEROF"));
						parameters.put("lblCashCustomer", (String) ma.get("CASHCUSTOMER"));
						parameters.put("Discount", (String) ma.get("DISCOUNT"));
						parameters.put("NetRate", (String) ma.get("NETRATE"));
						
						String PRINTWITHINVOICE= (String) ma.get("PRINTWITHTAXINVOICE");
						if (PRINTWITHINVOICE.equals("1")) {
							parameters.put("printwithinvoiceno", "yes");
							parameters.put("INVOICENOLabel", (String) ma.get("INVOICENO"));
							parameters.put("INVOICEDATE", (String) ma.get("INVOICEDATE"));
							parameters.put("INVOICENO", INVOICENO);
							if (INVOICENO.length() == 0)
							{	
								parameters.put("InvoiceDt","");	
							}else{								
								parameters.put("InvoiceDt",ISSUEDATE);							
							}
						
						}else {
							parameters.put("printwithinvoiceno", "no");
							parameters.put("INVOICENO", "");
							parameters.put("InvoiceDt","");	
						}
						parameters.put("HSCODE", (String) ma.get("HSCODE"));
						parameters.put("COO", (String) ma.get("COO"));
						parameters.put("PRITNWITHHSCODE", (String) ma.get("PRITNWITHHSCODE"));
						parameters.put("PRITNWITHCOO", (String) ma.get("PRITNWITHCOO"));
						parameters.put("PRINTWITHPRODUCTREMARKS", (String) ma.get("PRINTWITHPRODUCTREMARKS"));
						parameters.put("PRINTWITHBRAND", (String) ma.get("PRINTWITHBRAND"));
						parameters.put("IssueDateRange", sCondition);
						if (ma.get("PCUSREMARKS").equals("1")) {
							parameters.put("SupRemarks", sRemarks);
						} else {
							parameters.put("SupRemarks", "");
						}
						parameters.put("curDisplay", curDisplay);

						double gst = new Double(gstValue).doubleValue() / 100;
						parameters.put("Gst", gst);
						parameters.put("orderType", orderType);

						if (orderRemarks.length() > 0)
							orderRemarks = (String) ma.get("REMARK1") + " : " + orderRemarks;
						if (orderRemarks3.length() > 0)
							orderRemarks3 = (String) ma.get("REMARK2") + " : " + orderRemarks3;
						int Remarks1Length = orderRemarks.length();  
						int Remarks2Length = orderRemarks3.length();
						parameters.put("orderRemarks", orderRemarks);
						parameters.put("orderRemarks3", orderRemarks3);
						if (sDeliveryDate.length() > 0)
							sDeliveryDate = sDeliveryDate;
						parameters.put("DeliveryDt", sDeliveryDate);
						parameters.put("DeliveryDate", (String) ma.get("DELIVERYDATE"));
						if (ma.get("PRINTEMPLOYEE").equals("1")) {
							parameters.put("Employee", (String) ma.get("EMPLOYEE"));
							String empname = new EmployeeDAO().getEmpname(PLANT, sEmpno, "");
							parameters.put("EmployeeName", empname);
						} else {
							parameters.put("Employee", "");
							parameters.put("EmployeeName", "");
						}

						

						if (sShipCustName.length() > 0) {
							parameters.put("STo_CustName", (String) ma.get("SHIPTO") + " : " + "\n" + sShipCustName);

						} else {
							parameters.put("STo_CustName", sShipCustName);
						}
						parameters.put("STo_CustName", sShipCustName);
						parameters.put("STo", (String) ma.get("SHIPTO"));

						if (!checkImageFile.exists()) {
							imagePath2 = imagePath1;
							imagePath = "";
						} else if (!checkImageFile1.exists()) {
							imagePath2 = imagePath;
							imagePath1 = "";
						} else {
							imagePath2 = "";
						}
						parameters.put("imagePath", imagePath);
						parameters.put("imagePath1", imagePath1);
						parameters.put("imagePath2", imagePath2);
						JasperDesign jasper = null;
						String fileName="printPO";
						if (Orientation.equals("Portrait")) {
							if (ma.get("PRINTWITHDISCOUNT").equals("1")){
								jasperPath += "WithDiscountPortrait";
							}else {
								jasperPath += "Portrait";
								jasper = getJasperPotraitFooterHeight(flength,flength1,flength2,flength3,flength4,flength5,flength6,flength7,flength8, Remarks1Length, Remarks2Length, jasperPath, fileName);

						}
						}
						
							if(Orientation.equals("Landscape")){
								jasper = getJasperLandscapeFooterHeight(FooterLength, jasperPath, fileName);
						}
							
						if(PRINTMULTILANG.equals("1"))// Set Multi lang jasper
							jasperPath +="MutiLang";
						
						parameters.put("CompanyDate", (String) ma.get("COMPANYDATE"));
						parameters.put("CompanyName", (String) ma.get("COMPANYNAME"));
						parameters.put("CompanyStamp", (String) ma.get("COMPANYSTAMP"));
						parameters.put("CompanySig", (String) ma.get("COMPANYSIG"));
						parameters.put("Seller", (String) ma.get("SELLER"));
						parameters.put("SellerSign", (String) ma.get("SELLERSIGNATURE"));
						parameters.put("Buyer", (String) ma.get("BUYER"));
						parameters.put("BuyerSign", (String) ma.get("BUYERSIGNATURE"));
						parameters.put("lblOrderDiscount",
								(String) ma.get("ORDERDISCOUNT") + " " + "(" + DATAORDERDISCOUNT + "%" + ")");
						parameters.put("lblShippingCost",
								(String) ma.get("SHIPPINGCOST") + " " + "(" + curDisplay + ")");
						double doubledataorderdiscount = new Double(DATAORDERDISCOUNT);
						double doubledatashippingcost = new Double(DATASHIPPINGCOST);
						parameters.put("DATAORDERDISCOUNT", doubledataorderdiscount);
						parameters.put("DATASHIPPINGCOST", doubledatashippingcost);
						parameters.put("lblINCOTERM", (String) ma.get("INCOTERM"));
						parameters.put("DATAINCOTERMS", DATAINCOTERMS);
						POSDetDAO posdao = new POSDetDAO();
						posdao.setmLogger(mLogger);						   
						ArrayList arrayposdao = new ArrayList();
						arrayposdao = posdao.getPosDistinct(PLANT, tranid);
						if(arrayposdao.size() > 3)
						{ }
						else
							parameters.put(JRParameter.IS_IGNORE_PAGINATION, true);
							
						if(PRINTMULTILANG.equals("1"))
						{
							Map ma1 = poUtil.getDOReceiptInvoiceHdrDetailsDO(PLANT, "Tax Invoice English");//Check print multi lang
							parameters.put("ToHeader", (String) ma1.get("HDR2")+" "+(String) ma.get("HDR2"));
							parameters.put("FromHeader", (String) ma1.get("HDR3")+" "+(String) ma.get("HDR3"));
							parameters.put("STo", (String) ma1.get("SHIPTO")+" "+(String) ma.get("SHIPTO"));
							parameters.put("Date", (String) ma1.get("DATE")+" "+(String) ma.get("DATE"));
							parameters.put("DeliveryDate", (String) ma1.get("DELIVERYDATE")+" "+(String) ma.get("DELIVERYDATE"));
							parameters.put("OrderNoHdr", (String) ma1.get("ORDERNO")+" "+(String) ma.get("ORDERNO"));
							parameters.put("RefNo", (String) ma1.get("REFNO")+" "+(String) ma.get("REFNO"));
							parameters.put("Employee", (String) ma1.get("EMPLOYEE")+" "+(String) ma.get("EMPLOYEE"));
							parameters.put("INVOICENOLabel", (String) ma1.get("INVOICENO")+" "+(String) ma.get("INVOICENO"));
							parameters.put("INVOICEDATE", (String) ma1.get("INVOICEDATE")+" "+(String) ma.get("INVOICEDATE"));
							parameters.put("lblINCOTERM", (String) ma1.get("INCOTERM")+" "+(String) ma.get("INCOTERM"));
							parameters.put("Terms", (String) ma1.get("TERMS")+" "+(String) ma.get("TERMS"));
							parameters.put("PreBy", (String) ma1.get("PREPAREDBY")+" "+(String) ma.get("PREPAREDBY"));
							parameters.put("RCBNO", (String) ma1.get("RCBNO")+" "+(String) ma.get("RCBNO"));
							parameters.put("CUSTOMERRCBNO", (String) ma1.get("CUSTOMERRCBNO")+" "+(String) ma.get("CUSTOMERRCBNO"));
							parameters.put("SoNo", (String) ma1.get("SONO")+" "+"\n"+(String) ma.get("SONO"));
							parameters.put("Item", (String) ma1.get("ITEM")+" "+"\n"+(String) ma.get("ITEM"));
							parameters.put("Description", (String) ma1.get("DESCRIPTION")+" "+"\n"+(String) ma.get("DESCRIPTION"));
							parameters.put("OrderQty", (String) ma1.get("ORDERQTY")+" "+"\n"+(String) ma.get("ORDERQTY"));
							parameters.put("UOM", (String) ma1.get("UOM")+" "+"\n"+(String) ma.get("UOM"));
							parameters.put("Rate", (String) ma1.get("RATE")+" "+"\n"+(String) ma.get("RATE"));
							parameters.put("TaxAmount", (String) ma1.get("TAXAMOUNT")+" "+"\n"+(String) ma.get("TAXAMOUNT"));
							parameters.put("Amt", (String) ma1.get("AMT")+" "+"\n"+(String) ma.get("AMT"));
							parameters.put("SubTotal", (String) ma1.get("SUBTOTAL")+" "+"(" + curDisplay + ")"+" "+(String) ma.get("SUBTOTAL"));
							parameters.put("lblOrderDiscount", (String) ma1.get("ORDERDISCOUNT")+" "+ "(" + DATAORDERDISCOUNT + "%" + ")"+" "+(String) ma.get("ORDERDISCOUNT"));
							parameters.put("TOTALAFTERDISCOUNT", (String) ma1.get("TOTALAFTERDISCOUNT")+" "+(String) ma.get("TOTALAFTERDISCOUNT"));
							parameters.put("lblShippingCost", (String) ma1.get("SHIPPINGCOST")+" "+"(" + curDisplay + ")"+" "+(String) ma.get("SHIPPINGCOST"));
							parameters.put("TotalTax", (String) ma1.get("TOTALTAX")+" "+ "(" + gstValue + "%" + ")"+" "+(String) ma.get("TOTALTAX"));
							parameters.put("Total", (String) ma1.get("TOTAL")+" "+"(" + curDisplay + ")"+" "+(String) ma.get("TOTAL"));
							parameters.put("RoundoffTotalwithDecimal", (String) ma1.get("ROUNDOFFTOTALWITHDECIMAL")+" "+"(" + curDisplay + ")"+" "+(String) ma.get("ROUNDOFFTOTALWITHDECIMAL"));
							parameters.put("lblContactName", (String) ma1.get("CONTACTNAME")+" "+(String) ma.get("CONTACTNAME"));
							parameters.put("lblEmail", (String) ma1.get("EMAIL")+" "+(String) ma.get("EMAIL"));
							parameters.put("lblFax", (String) ma1.get("FAX")+" "+(String) ma.get("FAX"));
							parameters.put("lblTelephone", (String) ma1.get("TELEPHONE")+" "+(String) ma.get("TELEPHONE"));
							parameters.put("lblHandphone", (String) ma1.get("HANDPHONE")+" "+(String) ma.get("HANDPHONE"));
							parameters.put("lblAttention", (String) ma1.get("ATTENTION")+" "+(String) ma.get("ATTENTION"));
							parameters.put("lblQtyTotal", (String) ma1.get("QTYTOTAL")+" "+(String) ma.get("QTYTOTAL"));
							parameters.put("lblremark3", (String) ma1.get("REMARK3")+" "+(String) ma.get("REMARK3"));
							parameters.put("Seller", (String) ma1.get("SELLER")+" "+(String) ma.get("SELLER"));
							parameters.put("SellerSign", (String) ma1.get("SELLERSIGNATURE")+" "+(String) ma.get("SELLERSIGNATURE"));
							parameters.put("Buyer", (String) ma1.get("BUYER")+" "+(String) ma.get("BUYER"));
							parameters.put("BuyerSign", (String) ma1.get("BUYERSIGNATURE")+" "+(String) ma.get("BUYERSIGNATURE"));
							parameters.put("lblFooterPage", (String) ma1.get("FOOTERPAGE")+" "+(String) ma.get("FOOTERPAGE"));
							parameters.put("lblFooterOf", (String) ma1.get("FOOTEROF")+" "+(String) ma.get("FOOTEROF"));
							parameters.put("lblCashCustomer", (String) ma1.get("CASHCUSTOMER")+" "+(String) ma.get("CASHCUSTOMER"));
							parameters.put("Discount", (String) ma1.get("DISCOUNT")+" "+"\n"+(String) ma.get("DISCOUNT"));
							parameters.put("NetRate", (String) ma1.get("NETRATE")+" "+"\n"+(String) ma.get("NETRATE"));
							if (orderRemarksMultiLang1.length() > 0)
								orderRemarksMultiLang1 = orderRemarksMultiLang1;
							if (orderRemarksMultiLang2.length() > 0)
								orderRemarksMultiLang2 = orderRemarksMultiLang2;
							int Remarks3Length = orderRemarksMultiLang1.length();  
							int Remarks4Length = orderRemarksMultiLang2.length();
							parameters.put("orderRemarks", orderRemarksMultiLang1);
							parameters.put("orderRemarks3", orderRemarksMultiLang2);
							parameters.put("lblorderRemarks", (String) ma1.get("REMARK1")+" "+(String) ma.get("REMARK1"));
							parameters.put("lblorderRemarks3", (String) ma1.get("REMARK2")+" "+(String) ma.get("REMARK2"));
							parameters.put("Footer1", (String) ma.get("F1"));
							parameters.put("Footer2", (String) ma.get("F2"));
							parameters.put("Footer3", (String) ma.get("F3"));
							parameters.put("Footer4", (String) ma.get("F4"));
							parameters.put("Footer5", (String) ma.get("F5"));
							parameters.put("Footer6", (String) ma.get("F6"));
							parameters.put("Footer7", (String) ma.get("F7"));
							parameters.put("Footer8", (String) ma.get("F8"));
							parameters.put("Footer9", (String) ma.get("F9"));
							
						}
						parameters.put("numberOfDecimal", numberOfDecimal);
						parameters.put("PRINTWITHDELIVERYDATE", (String) ma.get("PRINTWITHDELIVERYDATE"));
						long start = System.currentTimeMillis();
						System.out.println("**************" + " Start Up Time : " + start + "**********");
						System.out.println("jasperPath : " + jasperPath);
						//JasperCompileManager.compileReportToFile(jasper, jasperPath + ".jasper");
						JasperCompileManager.compileReportToFile(jasperPath + ".jrxml", jasperPath + ".jasper");
						jasperPrintList.add((JasperFillManager.fillReport(jasperPath + ".jasper", parameters, con)));
						String jasperPathDN="rptTAXINVOICEPLWITH";
						if(sCustCode.equals("CASH"))
						{
							jasperPathDN=jasperPathDN+"OutCustomer";
							if(sCustName=="")
								sCustName ="CASH";
						}
						else
						{
							 if(sSHIPPINGID==""||sSHIPPINGID=="-1")
								 jasperPathDN=jasperPathDN+"Shipping";
							 else
								 jasperPathDN=jasperPathDN+"Customer";		 
						}
						//In Landscape, Check if delivery note is enabled
						if ("1".equals(PRINTDELIVERYNOTE)) {
							jasperPath = DbBean.JASPER_INPUT + "/" + jasperPathDN;
							parameters.put("OrderHeader", "Delivery Note");
							parameters.put("PackingListNumber", "Delivery Note Number");
							parameters.put("PartNo", "Part No");
							parameters.put("HSCode", "HS Code");
							parameters.put("COO", "COO");
							parameters.put("NetWeight", "Net Weight");
							parameters.put("GrossWeight", "Gross Weight");
							parameters.put("Dimension", "Dimension");
							parameters.put("Packing", "Packing");
							DNPLHdrDAO _DnPLHdrDAO = new DNPLHdrDAO();
							Hashtable htDnPlCondition = new Hashtable();
							htDnPlCondition.put("PLANT", PLANT);
							htDnPlCondition.put("DONO", DONO);
							htDnPlCondition.put("INVOICENO", INVOICENO);
							ArrayList aldnpl = _DnPLHdrDAO.selectdnplDNPLHDR("*", htDnPlCondition);
							Iterator iterdnpl = aldnpl.iterator();
							while(iterdnpl.hasNext()) {
								Map mapdnpl = (Map)iterdnpl.next();
								parameters.put("PackingListNo", mapdnpl.get("DNNO"));
								parameters.put("TotalDimension", mapdnpl.get("NETDIMENSION"));
								parameters.put("TotalPacking", mapdnpl.get("NETPACKING"));
								parameters.put("INVOICENO", mapdnpl.get("INVOICENO"));
								parameters.put("InvoiceNumber", "Invoice No. ");
								parameters.put("InvoiceDate", "Invoice Dt. ");
								parameters.put("InvoiceDt", ISSUEDATE);
								JasperCompileManager.compileReportToFile(jasperPath + ".jrxml", jasperPath + ".jasper");
								jasperPrintList.add((JasperFillManager.fillReport(jasperPath + ".jasper", parameters, con)));
							}
						}
						//In Landscape, Check if packing list is enabled
						if ("1".equals(PRINTPACKINGLIST)) {
							jasperPath = DbBean.JASPER_INPUT + "/" + jasperPathDN;
							parameters.put("OrderHeader", "Packing List");
							parameters.put("PackingListNumber", "Packing List Number");
							parameters.put("PartNo", "Part No");
							parameters.put("HSCode", "HS Code");
							parameters.put("COO", "COO");
							parameters.put("NetWeight", "Net Weight");
							parameters.put("GrossWeight", "Gross Weight");
							parameters.put("Dimension", "Dimension");
							parameters.put("Packing", "Packing");
							DNPLHdrDAO _DnPLHdrDAO = new DNPLHdrDAO();
							Hashtable htDnPlCondition = new Hashtable();
							htDnPlCondition.put("PLANT", PLANT);
							htDnPlCondition.put("DONO", DONO);
							htDnPlCondition.put("INVOICENO", INVOICENO);
							ArrayList aldnpl = _DnPLHdrDAO.selectdnplDNPLHDR("*", htDnPlCondition);
							Iterator iterdnpl = aldnpl.iterator();
							while(iterdnpl.hasNext()) {
								Map mapdnpl = (Map)iterdnpl.next();
								parameters.put("PackingListNo", mapdnpl.get("PLNO"));
								parameters.put("TotalDimension", mapdnpl.get("NETDIMENSION"));
								parameters.put("TotalPacking", mapdnpl.get("NETPACKING"));
								parameters.put("INVOICENO", mapdnpl.get("INVOICENO"));
								parameters.put("InvoiceNumber", "Invoice No. ");
								parameters.put("InvoiceDate", "Invoice Dt. ");
								parameters.put("InvoiceDt", ISSUEDATE);
								JasperCompileManager.compileReportToFile(jasperPath + ".jrxml", jasperPath + ".jasper");
								jasperPrintList.add((JasperFillManager.fillReport(jasperPath + ".jasper", parameters, con)));
							}
						}

					}
				}
			}

			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

			JRPdfExporter exporter = new JRPdfExporter();
			exporter.setParameter(JRExporterParameter.JASPER_PRINT_LIST, jasperPrintList);
			exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, byteArrayOutputStream);
			
			exporter.exportReport();

			byte[] bytes = byteArrayOutputStream.toByteArray();

			response.addHeader("Content-disposition", "attachment;filename=MultipleInvoice.pdf");
			response.setContentLength(bytes.length);
			response.getOutputStream().write(bytes);
			response.setContentType("application/pdf");
			//response.getOutputStream().flush();
			//response.getOutputStream().close();	
		} catch (Exception e) {

			e.printStackTrace();

		} finally {
			DbBean.closeConnection(con);
		}
	}
	public JasperDesign getJasperPotraitFooterHeight(int flength,int flength1,int flength2,int flength3,int flength4,int flength5,int flength6,int flength7,int flength8,int Remarks1Length,int Remarks2Length,String jasperPath,String fileName){
		JasperDesign jasperDesign = null;
		final double contLength = 125;
		
		//For Remark1
		int finalRemarks1Leng = 0;		
		double remark1Resl = (Remarks1Length / contLength);
	
		if(remark1Resl == 0){
			finalRemarks1Leng = 0;
		}else if(remark1Resl > 0 && remark1Resl <= 1){
			 finalRemarks1Leng = (int) (contLength * 1) ; 
		 }else if(remark1Resl > 1  && remark1Resl <= 2){
			 finalRemarks1Leng = (int) (contLength * 2) ;
		 }else if(remark1Resl > 2  && remark1Resl <= 3){
			 finalRemarks1Leng = (int) (contLength * 3) ;
		 }else if(remark1Resl > 3  && remark1Resl <= 4){
			 finalRemarks1Leng = (int) (contLength * 4) ;
		 }else if(remark1Resl > 4  && remark1Resl <= 5){
			 finalRemarks1Leng = (int) (contLength * 5) ;
		 }else if(remark1Resl > 5  && remark1Resl <= 6){
			 finalRemarks1Leng = (int) (contLength * 6) ;
		 }else if(remark1Resl > 6  && remark1Resl <= 7){
			 finalRemarks1Leng = (int) (contLength * 7) ;
		 }else if(remark1Resl > 7  && remark1Resl <= 8){
			 finalRemarks1Leng = (int) (contLength * 8) ;
		 }
		 else {
			 finalRemarks1Leng = (int) (contLength * 9) ;
		 }
		 
		 //Remarks 2 
		
			int finalRemarks2Leng = 0;
			double remark2Resl = Remarks2Length / contLength;
			if(remark2Resl == 0){
				finalRemarks2Leng = 0;
			}else if(remark2Resl > 0 && remark2Resl <= 1){
				 finalRemarks2Leng = (int) (contLength * 1) ; 
			 }else if(remark2Resl > 1 && remark2Resl <= 2){
				 finalRemarks2Leng = (int) (contLength * 2) ; 
			 }else {
				 finalRemarks2Leng = (int) (contLength * 3) ;
			 }
			 
			 //Footer
			
				int finalFooterLength = 0;
				double FooterResult = flength / contLength;
				if(FooterResult == 0){
					finalFooterLength = 0;
				}else if(FooterResult > 0 && FooterResult <= 1){
					 finalFooterLength = (int) (contLength * 1) ; 
				 }else if(FooterResult > 1 && FooterResult <= 2){
					 finalFooterLength = (int) (contLength * 2) ;
				 }else if(FooterResult > 2 && FooterResult <= 3){
					 finalFooterLength = (int) (contLength * 3) ;
				 }else if(FooterResult > 3 && FooterResult <= 4){
					 finalFooterLength = (int) (contLength * 4) ;
				 }else if(FooterResult > 4 && FooterResult <= 5){
					 finalFooterLength = (int) (contLength * 5) ;
				 }else if(FooterResult > 5 && FooterResult <= 6){
					 finalFooterLength = (int) (contLength * 6) ;
				 }else if(FooterResult > 6 && FooterResult <= 7){
					 finalFooterLength = (int) (contLength * 7) ;
				 }
				 else if(FooterResult > 7  && FooterResult <= 8){
					 finalFooterLength = (int) (contLength * 8) ;
				 }else {
					 finalFooterLength = (int) (contLength * 9) ;
				 }
				
				int finalFooterLength1 = 0;
				double FooterResult1 = flength1 / contLength;
				if(FooterResult == 0){
					finalFooterLength1 = 0;
				 }else if(FooterResult1 > 0 && FooterResult1 <= 1){
					 finalFooterLength1 = (int) (contLength * 1) ; 
				 }else if(FooterResult1 > 1 && FooterResult1 <= 2){
					 finalFooterLength1 = (int) (contLength * 2) ;
				 }else if(FooterResult1 > 2 && FooterResult1 <= 3){
					 finalFooterLength1 = (int) (contLength * 3) ;
				 }else if(FooterResult1 > 3 && FooterResult1 <= 4){
					 finalFooterLength1 = (int) (contLength * 4) ;
				 }else if(FooterResult1 > 4 && FooterResult1 <= 5){
					 finalFooterLength1 = (int) (contLength * 5) ;
				 }else if(FooterResult1 > 5 && FooterResult1 <= 6){
					 finalFooterLength1 = (int) (contLength * 6) ;
				 }else if(FooterResult1 > 6 && FooterResult1 <= 7){
					 finalFooterLength1 = (int) (contLength * 7) ;
				 }
				 else if(FooterResult1 > 7  && FooterResult1 <= 8){
					 finalFooterLength1 = (int) (contLength * 8) ;
				 }else {
					 finalFooterLength1 = (int) (contLength * 9) ;
				 }
				 
				int finalFooterLength2 = 0;
				double FooterResult2 = flength2 / contLength;
				if(FooterResult2 == 0){
					finalFooterLength = 0;
				 }else if(FooterResult2 > 0 && FooterResult2 <= 1){
					 finalFooterLength2 = (int) (contLength * 1) ; 
				 }else if(FooterResult2 > 1 && FooterResult2 <= 2){
					 finalFooterLength2 = (int) (contLength * 2) ;
				 }else if(FooterResult2 > 2 && FooterResult2 <= 3){
					 finalFooterLength2 = (int) (contLength * 3) ;
				 }else if(FooterResult2 > 3 && FooterResult2 <= 4){
					 finalFooterLength2 = (int) (contLength * 4) ;
				 }else if(FooterResult2 > 4 && FooterResult2 <= 5){
					 finalFooterLength2 = (int) (contLength * 5) ;
				 }else if(FooterResult2 > 5 && FooterResult2 <= 6){
					 finalFooterLength2 = (int) (contLength * 6) ;
				 }else if(FooterResult2 > 6 && FooterResult2 <= 7){
					 finalFooterLength2 = (int) (contLength * 7) ;
				 }
				 else if(FooterResult2 > 7  && FooterResult2 <= 8){
					 finalFooterLength2 = (int) (contLength * 8) ;
				 }else {
					 finalFooterLength2 = (int) (contLength * 9) ;
				 } 
				
				int finalFooterLength3 = 0;
				double FooterResult3 = flength3 / contLength;
				if(FooterResult3 == 0){
					 finalFooterLength3 = 0;
				 }else if(FooterResult3 > 0 && FooterResult3 <= 1){
					 finalFooterLength3 = (int) (contLength * 1) ; 
				 }else if(FooterResult3 > 1 && FooterResult3 <= 2){
					 finalFooterLength3 = (int) (contLength * 2) ;
				 }else if(FooterResult3 > 2 && FooterResult3 <= 3){
					 finalFooterLength3 = (int) (contLength * 3) ;
				 }else if(FooterResult3 > 3 && FooterResult3 <= 4){
					 finalFooterLength3 = (int) (contLength * 4) ;
				 }else if(FooterResult3 > 4 && FooterResult3 <= 5){
					 finalFooterLength3 = (int) (contLength * 5) ;
				 }else if(FooterResult3 > 5 && FooterResult3 <= 6){
					 finalFooterLength3 = (int) (contLength * 6) ;
				 }else if(FooterResult3 > 6 && FooterResult3 <= 7){
					 finalFooterLength3 = (int) (contLength * 7) ;
				 }
				 else if(FooterResult3 > 7  && FooterResult3 <= 8){
					 finalFooterLength3 = (int) (contLength * 8) ;
				 }else {
					 finalFooterLength3 = (int) (contLength * 9) ;
				 }
				
				
				int finalFooterLength4 = 0;
				 double FooterResult4 = flength4 / contLength;
				 if(FooterResult4 == 0){
					finalFooterLength4 = 0;
				 }else if(FooterResult4 > 0 && FooterResult4 <= 1){
					 finalFooterLength4 = (int) (contLength * 1) ; 
				 }else if(FooterResult4 > 1 && FooterResult4 <= 2){
					 finalFooterLength4 = (int) (contLength * 2) ;
				 }else if(FooterResult4 > 2 && FooterResult4 <= 3){
					 finalFooterLength4 = (int) (contLength * 3) ;
				 }else if(FooterResult4 > 3 && FooterResult4 <= 4){
					 finalFooterLength4 = (int) (contLength * 4) ;
				 }else if(FooterResult4 > 4 && FooterResult4 <= 5){
					 finalFooterLength4 = (int) (contLength * 5) ;
				 }else if(FooterResult4 > 5 && FooterResult4 <= 6){
					 finalFooterLength4 = (int) (contLength * 6) ;
				 }else if(FooterResult4 > 6 && FooterResult4 <= 7){
					 finalFooterLength4 = (int) (contLength * 7) ;
				 }else if(FooterResult4 > 7  && FooterResult4 <= 8){
					 finalFooterLength4 = (int) (contLength * 8) ;
				 }else {
					 finalFooterLength4 = (int) (contLength * 9) ;
				 }

				 int finalFooterLength5 = 0;
				 double FooterResult5 = flength5 / contLength;
				 if(FooterResult5 == 0){
					finalFooterLength5 = 0;
				 }else if(FooterResult5 > 0 && FooterResult5 <= 1){
					 finalFooterLength5 = (int) (contLength * 1) ; 
				 }else if(FooterResult5 > 1 && FooterResult5 <= 2){
					 finalFooterLength5 = (int) (contLength * 2) ;
				 }else if(FooterResult5 > 2 && FooterResult5 <= 3){
					 finalFooterLength5 = (int) (contLength * 3) ;
				 }else if(FooterResult5 > 3 && FooterResult5 <= 4){
					 finalFooterLength5 = (int) (contLength * 4) ;
				 }else if(FooterResult5 > 4 && FooterResult5 <= 5){
					 finalFooterLength5 = (int) (contLength * 5) ;
				 }else if(FooterResult5 > 5 && FooterResult5 <= 6){
					 finalFooterLength5 = (int) (contLength * 6) ;
				 }else if(FooterResult5 > 6 && FooterResult5 <= 7){
					 finalFooterLength5 = (int) (contLength * 7) ;
				 }else if(FooterResult5 > 7  && FooterResult5 <= 8){
					 finalFooterLength5 = (int) (contLength * 8) ;
				 }else {
					 finalFooterLength5 = (int) (contLength * 9) ;
				 }				 
				
				 int finalFooterLength6 = 0;
				 double FooterResult6 = flength6 / contLength;
				 if(FooterResult6 == 0){
					finalFooterLength6 = 0;
				 }else if(FooterResult6 > 0 && FooterResult6 <= 1){
					 finalFooterLength6 = (int) (contLength * 1) ; 
				 }else if(FooterResult6 > 1 && FooterResult6 <= 2){
					 finalFooterLength6 = (int) (contLength * 2) ;
				 }else if(FooterResult6 > 2 && FooterResult6 <= 3){
					 finalFooterLength6 = (int) (contLength * 3) ;
				 }else if(FooterResult6 > 3 && FooterResult6 <= 4){
					 finalFooterLength6 = (int) (contLength * 4) ;
				 }else if(FooterResult6 > 4 && FooterResult6 <= 5){
					 finalFooterLength6 = (int) (contLength * 5) ;
				 }else if(FooterResult6 > 5 && FooterResult6 <= 6){
					 finalFooterLength6 = (int) (contLength * 6) ;
				 }else if(FooterResult6 > 6 && FooterResult6 <= 7){
					 finalFooterLength6 = (int) (contLength * 7) ;
				 }else if(FooterResult6 > 7  && FooterResult6 <= 8){
					 finalFooterLength6 = (int) (contLength * 8) ;
				 }else {
					 finalFooterLength6 = (int) (contLength * 9) ;
				 }

				 int finalFooterLength7 = 0;
				 double FooterResult7 = flength7 / contLength;
				 if(FooterResult7 == 0){
					finalFooterLength7 = 0;
				 }else if(FooterResult7 > 0 && FooterResult7 <= 1){
					 finalFooterLength7 = (int) (contLength * 1) ; 
				 }else if(FooterResult7 > 1 && FooterResult7 <= 2){
					 finalFooterLength7 = (int) (contLength * 2) ;
				 }else if(FooterResult7 > 2 && FooterResult7 <= 3){
					 finalFooterLength7 = (int) (contLength * 3) ;
				 }else if(FooterResult7 > 3 && FooterResult7 <= 4){
					 finalFooterLength7 = (int) (contLength * 4) ;
				 }else if(FooterResult7 > 4 && FooterResult7 <= 5){
					 finalFooterLength7 = (int) (contLength * 5) ;
				 }else if(FooterResult7 > 5 && FooterResult7 <= 6){
					 finalFooterLength7 = (int) (contLength * 6) ;
				 }else if(FooterResult7 > 6 && FooterResult7 <= 7){
					 finalFooterLength7 = (int) (contLength * 7) ;
				 }else if(FooterResult7 > 7  && FooterResult7 <= 8){
					 finalFooterLength7 = (int) (contLength * 8) ;
				 }else {
					 finalFooterLength7 = (int) (contLength * 9) ;
				 }
				 
				 int finalFooterLength8 = 0;
				 double FooterResult8 = flength8 / contLength;
				 if(FooterResult8 == 0){
					finalFooterLength8 = 0;
				 }else if(FooterResult8 > 0 && FooterResult8 <= 1){
					 finalFooterLength8 = (int) (contLength * 1) ; 
				 }else if(FooterResult8 > 1 && FooterResult8 <= 2){
					 finalFooterLength8 = (int) (contLength * 2) ;
				 }else if(FooterResult8 > 2 && FooterResult8 <= 3){
					 finalFooterLength8 = (int) (contLength * 3) ;
				 }else if(FooterResult8 > 3 && FooterResult8 <= 4){
					 finalFooterLength8 = (int) (contLength * 4) ;
				 }else if(FooterResult8 > 4 && FooterResult8 <= 5){
					 finalFooterLength8 = (int) (contLength * 5) ;
				 }else if(FooterResult8 > 5 && FooterResult8 <= 6){
					 finalFooterLength8 = (int) (contLength * 6) ;
				 }else if(FooterResult8 > 6 && FooterResult8 <= 7){
					 finalFooterLength8 = (int) (contLength * 7) ;
				 }else if(FooterResult8 > 7  && FooterResult8 <= 8){
					 finalFooterLength8 = (int) (contLength * 8) ;
				 }else {
					 finalFooterLength8 = (int) (contLength * 9) ;
				 }			
		 int totalLength =  (finalRemarks1Leng + finalRemarks2Leng + finalFooterLength+finalFooterLength1+finalFooterLength2+finalFooterLength3+ finalFooterLength4+finalFooterLength5+finalFooterLength6+finalFooterLength7+finalFooterLength8);
		 
		    try {
				jasperDesign = JRXmlLoader.load(jasperPath+".jrxml");
				JRDesignBand banda = (JRDesignBand) jasperDesign.getLastPageFooter();
				
				  if(fileName.equals("printPOInvoice"))
			        {
				 
					    	if(totalLength ==0){
					    		 banda.setHeight(220);
					    	}
					    	// 1 Line
					    	if(totalLength >=1 && totalLength <=243){
					    		 banda.setHeight(230);
					    	}
					    	// 2 Line
					    	else if(totalLength >=244 && totalLength <=366){
					    		 banda.setHeight(240);
					    	}
					    	// 3 Line
					    	else if(totalLength >=367 && totalLength <=400){
					    		 banda.setHeight(255);
					    	}
					    	// 4 Line
					    	else if(totalLength >=401 && totalLength <=522){
					    		 banda.setHeight(260);
					    	}
					    	// 4 Line
					    	else if(totalLength >=523 && totalLength <=655){
					    		 banda.setHeight(275);
					    	}
					    	else if(totalLength >=656 && totalLength <=800){
					    		 banda.setHeight(285);
					    	}
					    
					    	// 7 Line
					    	else if(totalLength >=801 && totalLength <=1050){
					    		 banda.setHeight(300);
					    	}
					    	
					    	else if(totalLength >=1051 && totalLength <=1550){
					    		 banda.setHeight(340);
					    	}
					    	else if(totalLength >=1551 && totalLength <=1850){ 
					    		 banda.setHeight(380);
					    	}
					    	
					    	else if(totalLength >=1851 && totalLength <=2200){ 
					    		 banda.setHeight(420);
					    	}
					    	
					    	else if(totalLength >=2201 && totalLength <=2700){ 
					    		 banda.setHeight(450);
					    	}
				    	
				    }
				  
				      if (fileName.equals("printPO")){
				    	
					    	if(totalLength ==0){
					    		 banda.setHeight(178);
					    	}// 1 Line
					    	if(totalLength >=1 && totalLength <=243){
					    		 banda.setHeight(185);
					    	}
					    	// 2 Line
					    	else if(totalLength >=244 && totalLength <=366){
					    		 banda.setHeight(190);
					    	}
					    	// 3 Line
					    	else if(totalLength >=367 && totalLength <=400){
					    		 banda.setHeight(280);
					    	}
					    	// 4 Line
					    	else if(totalLength >=401 && totalLength <=522){
					    		 banda.setHeight(290);
					    	}
					    	// 4 Line
					    	else if(totalLength >=523 && totalLength <=655){
					    		 banda.setHeight(315);
					    	}
					    	else if(totalLength >=656 && totalLength <=800){
					    		 banda.setHeight(340);
					    	}
					    
					    	// 7 Line
					    	else if(totalLength >=801 && totalLength <=1050){
					    		 banda.setHeight(370);
					    	}
					    	
					    	else if(totalLength >=1051 && totalLength <=1400){
					    		 banda.setHeight(403);
					    	}
					    	else if(totalLength >=1401 && totalLength <=1500){
					    		 banda.setHeight(420);
					    	}
					    	else if(totalLength >=1551 && totalLength <=1700){ 
					    		 banda.setHeight(430);
					    	}
					    	else if(totalLength >=1701 && totalLength <=1850){ 
					    		 banda.setHeight(440);
					    	}
					    	
					    	else if(totalLength >=1851 && totalLength <=2200){ 
					    		 banda.setHeight(480);
					    	}
					    	
					    	else if(totalLength >=2201 && totalLength <=2700){ 
					    		 banda.setHeight(500);
					    	}
					    	
				    }
			        
			} catch (Exception e) {
				System.out.println("Exception occure in calculating the length");
				e.printStackTrace();
			}
	
		return jasperDesign;
	}

	public JasperDesign getJasperLandscapeFooterHeight(int FooterLength,String jasperPath,String fileName){
		JasperDesign jasperDesign = null;
		
		 
		    try {
				jasperDesign = JRXmlLoader.load(jasperPath+".jrxml");
				JRDesignBand banda = (JRDesignBand) jasperDesign.getLastPageFooter();
				
				  if(fileName.equals("printPOInvoice"))
			        {
				    
				    	 if(FooterLength>=0 && FooterLength<=404){
					    	 banda.setHeight(170);
					    }
					    else if(FooterLength>=405 && FooterLength <=580){
					    	 banda.setHeight(190);
					    }
					    else if(FooterLength>=581 && FooterLength<=821){
					    	 banda.setHeight(205); 
					    }
						 else if(FooterLength>=822){
							 banda.setHeight(215);
						 }
					    
						/* else{
							 banda.setHeight(200);
						 }*/
				    }
				  if(fileName.equals("printPO"))
					{
				    	if(FooterLength>=0 && FooterLength<=404){
					    	 banda.setHeight(130);
					    }
					    else if(FooterLength>=405 && FooterLength <=580){
					    	 banda.setHeight(150);
					    }
					    else if(FooterLength>=581 && FooterLength<=821){
					    	 banda.setHeight(165); 
					    }
						 else if(FooterLength>=822){
							 banda.setHeight(175);
						 }
					}
			        
			} catch (Exception e) {
				System.out.println("Exception occure in calculating the length");
				e.printStackTrace();
			}
	
		return jasperDesign;
	}
	public boolean checktrans(String Plant,String Tranid) throws Exception {
		boolean flag = true;
		SalesDetailDAO salesdao = new SalesDetailDAO();
		POSDetDAO posdet = new POSDetDAO();
		Hashtable<String, String> htTrandId = new Hashtable<String, String>();
							htTrandId.put("TRANID", Tranid);
							htTrandId.put(IConstants.PLANT, Plant);
							boolean chkflag = salesdao.isExisit(htTrandId,"");
							if(!chkflag)
							{
								htTrandId.remove("TRANID");
								htTrandId.put("POSTRANID", Tranid);
								chkflag =posdet.isExisit(htTrandId, "");
								if(chkflag)
									posdet.deletePosTranId(Plant, Tranid);
							}

		return flag;
	}
	public boolean processShipHis(Map map) throws Exception {
		boolean flag = false;
		String rems = "";
		DateUtils dtUtils = new DateUtils();
		ShipHisDAO shiphstdao = new ShipHisDAO();
		StrUtils su = new StrUtils();
		shiphstdao.setmLogger(mLogger);
		try {

			Hashtable htIssueDet = new Hashtable();
				double quantityToAdjust = Double.parseDouble("" + map.get(IConstants.QTY));
				
					htIssueDet.clear();
					htIssueDet.put(IDBConstants.PLANT, map.get(IConstants.PLANT));
					//htIssueDet.put(IDBConstants.DODET_DONUM, map.get("TranId"));
					htIssueDet.put(IDBConstants.DODET_DONUM, "");
					htIssueDet.put(IDBConstants.CUSTOMER_NAME, "");
					htIssueDet.put(IDBConstants.ITEM, map.get(IConstants.ITEM));
					// Retrieve item description
					ItemMstDAO itM = new ItemMstDAO();
					itM.setmLogger(mLogger);
					String sDesc = itM.getItemDesc((String) map.get(IConstants.PLANT), (String) map.get(IConstants.ITEM));
					htIssueDet.put(IDBConstants.ITEM_DESC, su.InsertQuotes(sDesc));
					htIssueDet.put("BATCH", map.get(IConstants.BATCH));
					htIssueDet.put("INVOICENO",map.get("TranId"));
					htIssueDet.put(IDBConstants.LOC, map.get(IConstants.LOC));
					htIssueDet.put("LOC1", map.get(IConstants.LOC));
					htIssueDet.put("DOLNO", map.get(IConstants.OUT_DOLNNO));
					htIssueDet.put("ORDQTY", map.get(IConstants.QTY));
					htIssueDet.put("PICKQTY", String.valueOf(quantityToAdjust));
					htIssueDet.put("REVERSEQTY", "0");
					htIssueDet.put("STATUS", "C");
					htIssueDet.put(IDBConstants.CREATED_BY, map.get(IConstants.LOGIN_USER));
					htIssueDet.put(IDBConstants.CREATED_AT, dtUtils.getDateTime());
					htIssueDet.put(IDBConstants.ISSUEDATE, map.get(IConstants.ISSUEDATE));
					htIssueDet.put(IDBConstants.EMPNO, map.get(IConstants.EMPNO));
					htIssueDet.put(IDBConstants.RSNCODE, map.get(IConstants.REASONCODE));
					htIssueDet.put(IDBConstants.REFNO, map.get(IConstants.REFNO));
					htIssueDet.put("REMARK", map.get(IConstants.REMARKS));
					
					if(map.get("TRAN_TYPE").equals("GOODSISSUEWITHBATCHPRICE"))
					{
						htIssueDet.put("TRAN_TYPE", "TAXINVOICE");
						htIssueDet.put(IConstants.INVOICENO, map.get(IConstants.INVOICENO));
						htIssueDet.put(IDBConstants.PRICE, map.get(IConstants.PRICE));
						htIssueDet.put(IConstants.CURRENCYID, map.get(IConstants.CURRENCYID));
						htIssueDet.put(IDBConstants.CUSTOMER_NAME, map.get(IConstants.DOHDR_CUST_NAME));
					}
					else
						htIssueDet.put("TRAN_TYPE", map.get(IConstants.TRAN_TYPE));
					flag = shiphstdao.insertShipHis(htIssueDet);				

		} catch (Exception e) {

			this.mLogger.exception(this.printLog, "", e);
			throw e;

		}
		return flag;
	}

}