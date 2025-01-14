package com.track.servlet;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.RoundingMode;
import java.sql.Connection;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.util.CellRangeAddress;

import com.track.constants.IConstants;
import com.track.constants.IDBConstants;
import com.track.constants.MLoggerConstant;
import com.track.constants.TransactionConstants;
import com.track.dao.CurrencyDAO;
import com.track.dao.CustomerBeanDAO;
import com.track.dao.DoDetDAO;
import com.track.dao.EmailMsgDAO;
import com.track.dao.EmployeeDAO;
import com.track.dao.EstDetDAO;
import com.track.dao.FinCountryTaxTypeDAO;
import com.track.dao.FinProjectDAO;
import com.track.dao.InvMstDAO;
import com.track.dao.ItemMstDAO;
import com.track.dao.MasterDAO;
import com.track.dao.MovHisDAO;
import com.track.dao.OrderTypeBeanDAO;
import com.track.dao.OrderTypeDAO;
import com.track.dao.PlantMstDAO;
import com.track.dao.PoDetDAO;
import com.track.dao.PoEstHdrDAO;
import com.track.dao.PoHdrDAO;
import com.track.dao.RecvDetDAO;
import com.track.dao.TblControlDAO;
import com.track.dao.TransportModeDAO;
import com.track.dao.VendMstDAO;
import com.track.db.object.FinCountryTaxType;
import com.track.db.object.FinProject;
import com.track.db.object.PoDet;
import com.track.db.object.PoEstHdr;
import com.track.db.object.PoHdr;
import com.track.db.util.CurrencyUtil;
import com.track.db.util.CustUtil;
import com.track.db.util.DOUtil;
import com.track.db.util.EmailMsgUtil;
import com.track.db.util.HTReportUtil;
import com.track.db.util.ItemMstUtil;
import com.track.db.util.ItemUtil;
import com.track.db.util.MasterUtil;
import com.track.db.util.OrderPaymentUtil;
import com.track.db.util.OrderTypeUtil;
import com.track.db.util.POUtil;
import com.track.db.util.PoEstUtil;//Resvi
import com.track.db.util.MultiPoUtil;//Resvi
import com.track.db.util.PlantMstUtil;
import com.track.db.util.TblControlUtil;
import com.track.gates.DbBean;
import com.track.gates.selectBean;
import com.track.util.DateUtils;
import com.track.util.IMLogger;
import com.track.util.MLogger;
import com.track.util.Numbers;
import com.track.util.StrUtils;
import com.track.util.ThrowableUtil;

import edu.emory.mathcs.backport.java.util.Arrays;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
@SuppressWarnings({"rawtypes", "unchecked"})
public class PurchaseOrderServlet extends HttpServlet implements IMLogger {
	/**
	 * 
	 */
	private static final long serialVersionUID = 680255718336936308L;
	//private static final String CONTENT_TYPE = "text/html; charset=windows-1252";
	private boolean printLog = MLoggerConstant.PurchaseOrderServlet_PRINTPLANTMASTERLOG;
	private boolean printInfo = MLoggerConstant.PurchaseOrderServlet_PRINTPLANTMASTERINFO;
	String action = "";
	POUtil _POUtil = null;
	PoEstUtil _PoEstUtil = null;//Resvi
	HTReportUtil htutil = null;
	StrUtils _StrUtils = null;
	EmailMsgDAO emailDao = null;
	EmailMsgUtil mailUtil = null;
	TblControlDAO _TblControlDAO = null;
	RecvDetDAO _RecvDetDAO = null;
	PoDetDAO _poDetDAO = null;
	DoDetDAO _DoDetDAO = null;
	ItemMstDAO _ItemMstDAO = null;
	DOUtil _dOUtil = null;
	MultiPoUtil _MultiPoUtil = null;//Resvi
	MasterDAO _MasterDAO = null;
	PlantMstDAO _PlantMstDAO = null;
	MasterUtil _masterUtil = null;
	OrderTypeBeanDAO OrderTypeBeanDAO = null;

	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		_POUtil = new POUtil();
		_PoEstUtil = new PoEstUtil();//Resvi
		htutil = new HTReportUtil();
		_StrUtils = new StrUtils();
		emailDao = new EmailMsgDAO();
		mailUtil = new EmailMsgUtil();
		_TblControlDAO = new TblControlDAO();
		_RecvDetDAO = new RecvDetDAO();
		_poDetDAO = new PoDetDAO();
		_DoDetDAO = new DoDetDAO();
		_ItemMstDAO = new ItemMstDAO();
		_dOUtil = new DOUtil();
		_MasterDAO = new MasterDAO();
		_PlantMstDAO = new PlantMstDAO();
		_masterUtil = new MasterUtil();
		OrderTypeBeanDAO = new OrderTypeBeanDAO();
	}

	/*********************
	 * Modification History*********************************** Bruhan,Oct 23 2014,To
	 * update Add Products,Add product, Updatepodet and DeleteProduct to allow
	 * inbound order to amend,add products once a order was processed
	 * 
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			System.out.println("MENU : " + request.getSession(false).getAttribute("DROPDOWN_MENU"));
			action = StrUtils.fString(request.getParameter("Submit")).trim();
			String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
			System.out.println("action............." + action);
			String userName = StrUtils.fString((String) request.getSession().getAttribute("LOGIN_USER")).trim();
			String baseCurrency = StrUtils.fString((String) request.getSession().getAttribute("BASE_CURRENCY")).trim();
			this.setMapDataToLogger(this.populateMapData(plant, userName));

			_POUtil.setmLogger(mLogger);
			if (this.printInfo) {
				diaplayInfoLogs(request);
			}
			//String test = StrUtils.fString(request.getParameter("PONO")).trim();
			String rflag = StrUtils.fString(request.getParameter("RFLAG"));

			if (action.equalsIgnoreCase("View")) {
				String pono = StrUtils.fString(request.getParameter("PONO"));

				String statusValue = StrUtils.fString(request.getParameter("statusValue"));
				String fieldDesc = "<tr><td> Please enter Search condition and press View</td></tr>";
				ArrayList al = new ArrayList();
				Hashtable htCond = new Hashtable();
				Map m = new HashMap();

				if (pono.length() > 0) {
					htCond.put("PLANT", plant);
					htCond.put("PONO", pono);

					String query = " a.pono,isnull(a.ordertype,'') ordertype,"
							+ "(select isnull(display,'') display from " + "[" + plant
							+ "_CURRENCYMST] where currencyid = a.currencyid) currencyid,"
							+ "isnull(a.inbound_Gst,0) inbound_Gst,b.vname as custName,a.custCode,isnull(a.jobNum,'') as jobNum,"
							+ "a.personInCharge,a.contactNum,a.address,a.collectionDate,a.collectionTime,isnull(a.remark1,'') as remark1,"
							+ "isnull(a.remark2,'') as remark2,isnull(a.remark3,'') as remark3,isnull(b.name,'') as contactname,"
							+ "isnull(b.telno,'') as telno,isnull(b.email,'') email,isnull(b.addr1,'') add1,isnull(b.addr2,'') add2,"
							+ "isnull(b.addr3,'') add3,isnull(b.remarks,'') remarks,isnull(b.addr4,'') add4,"
							+ "isnull(b.country,'') country,isnull(b.zip,'') zip,isnull(a.deldate,'') as deldate,"
							+ "isnull(STATUS_ID,'') as statusid, "
							+ "isnull(shippingid,'')as shippingid, isnull(shippingcustomer,'') as shippingcustomer,"
							+ "isnull(orderdiscount,0) as orderdiscount,isnull(shippingcost,0) as shippingcost,"
							+ "isnull(incoterms,'') as incoterms,isnull(a.PAYMENTTYPE,'') payment_terms,isnull(a.DELIVERYDATEFORMAT,'') DELIVERYDATEFORMAT,isnull(LOCALEXPENSES,0) as localexpenses,"
							+ "ISNULL(A.PURCHASE_LOCATION,'') PURCHASE_LOCATION,ISNULL(A.TAXTREATMENT,'') TAXTREATMENT,ISNULL(A.REVERSECHARGE,'') REVERSECHARGE,ISNULL(A.GOODSIMPORT,'') GOODSIMPORT";
					

					al = _POUtil.getSupplierHdrDetails(query, htCond, "");
					if (al.size() > 0) {
						m = (Map) al.get(0);
						fieldDesc = _POUtil.listPODET(pono, plant, rflag);
					} else {

						fieldDesc = "<tr><td colspan=\"10\" align=\"center\">No Records Available</td></tr>";
					}

				}
				if (rflag.equals("1")) {
					request.getSession().setAttribute("podetVal", m);
					request.getSession().setAttribute("RESULT", fieldDesc);
					response.sendRedirect("jsp/CreateIncomingOrder.jsp?PONO=" + pono + "&action=View");
				} else if (rflag.equals("2")) {
					request.getSession().setAttribute("podetVal", m);
					request.getSession().setAttribute("RESULT", fieldDesc);
					response.sendRedirect("jsp/InboundSummary.jsp?PONO=" + pono + "&action=View");
				} else if (rflag.equals("3")) {
					request.getSession().setAttribute("podetVal", m);
					request.getSession().setAttribute("RESULT", fieldDesc);
					response.sendRedirect("jsp/CreateIncomingOrder.jsp?PONO=" + pono + "&action=View");
				} else if (rflag.equals("4")) {
					request.getSession().setAttribute("podetVal", m);
					request.getSession().setAttribute("RESULT1", fieldDesc);
					response.sendRedirect(
							"jsp/maintIncomingOrder.jsp?statusValue=" + statusValue + "&PONO=" + pono + "&action=View");
				} else {
					request.getSession().setAttribute("podetVal", m);
					request.getSession().setAttribute("RESULT", fieldDesc);
					response.sendRedirect("jsp/CreateIncomingOrder.jsp?PONO=" + pono + "&action=View");
				}

			}

			else if (action.equalsIgnoreCase("print")) {
				String pono = StrUtils.fString(request.getParameter("PONO"));
				String fieldDesc = "<tr><td> Please enter search condition and press View</td></tr>";
				ArrayList al = new ArrayList();
				Hashtable htCond = new Hashtable();
				Map m = new HashMap();
				POUtil _poUtil = new POUtil();				
				if (pono.length() > 0) {
					htCond.put("PLANT", plant);
					htCond.put("PONO", pono);
					String query = " a.pono,isnull(a.ordertype,'') ordertype,(select isnull(display,'') display from "
							+ "[" + plant
							+ "_CURRENCYMST] where currencyid = a.currencyid) currencyid,a.inbound_Gst,a.custName,a.custCode,isnull(a.jobNum,'') as jobNum,a.custName,a.personInCharge,a.contactNum,a.address,a.collectionDate,a.collectionTime,isnull(a.remark1,'') as remark1,a.remark2,isnull(b.name,'') as contactname,isnull(b.telno,'') as telno,isnull(b.email,'') email,isnull(b.addr1,'') add1,isnull(b.addr2,'') add2,isnull(b.addr3,'') add3,isnull(b.remarks,'') remarks,isnull(b.addr4,'') add4,isnull(b.country,'') country,isnull(b.zip,'') zip,isnull(a.deldate,'') as deldate,isnull(scust_name,'')as sCust_Name, isnull(scname,'') as sContact_Name,isnull(sAddr1,'') as sAddr1,isnull(sAddr2,'') as sAddr2,isnull(sCity,'') as sCity,isnull(sCountry,'') as sCountry,isnull(sZip,'') as sZip,isnull(sTelNo,'') as sTelno  ";
					al = _poUtil.getSupplierHdrDetails(query, htCond, "");
					if (al.size() > 0) {
						m = (Map) al.get(0);
						fieldDesc = _POUtil.listPODET(pono, plant, rflag);
					} else {
						fieldDesc = "<tr><td colspan=\"10\" align=\"center\">No Records Available</td></tr>";

					}

				}
				request.getSession().setAttribute("podetVal", m);
				request.getSession().setAttribute("RESULT", fieldDesc);
				response.sendRedirect("jsp/CreateIncomingOrder_print.jsp?PONO=" + pono + "&action=View");

			} else if (action.equalsIgnoreCase("Auto-Generate")) {
				response.sendRedirect("jsp/CreateIncomingOrder.jsp?action=Auto-Generate");
			}

			else if (action.equalsIgnoreCase("Print Inbound Order")) {
				String COMP_INDUSTRY = new PlantMstDAO().getCOMP_INDUSTRY(plant);//Check Company Industry
				if(COMP_INDUSTRY.equalsIgnoreCase("Centralised Kitchen"))
					viewPOReport(request, response, "printPOKitchen");
				else
				viewPOReport(request, response, "printPO");
			} else if (action.equalsIgnoreCase("Print Inbound Order With Cost")) {
				String COMP_INDUSTRY = new PlantMstDAO().getCOMP_INDUSTRY(plant);//Check Company Industry
				if(COMP_INDUSTRY.equalsIgnoreCase("Centralised Kitchen"))
					viewPOReport(request, response, "printPOInvoiceKitchen");
				else
					viewPOReport(request, response, "printPOInvoice");
			}
			else if (action.equalsIgnoreCase("Print Estimate Order With Cost")) {
				String COMP_INDUSTRY = new PlantMstDAO().getCOMP_INDUSTRY(plant);//Check Company Industry
				if(COMP_INDUSTRY.equalsIgnoreCase("Centralised Kitchen"))
					viewESReport(request, response, "printPEInvoiceKitchen");
				else
					viewESReport(request, response, "printPEInvoiceKitchen");
			}

			// start code by Bruhan for export to excel Purchase order details on 13 sep
			// 2013
			else if (action.equalsIgnoreCase("Export To Excel")) {
				HSSFWorkbook wb = new HSSFWorkbook();
				wb = this.writeToExcel(request, response, wb);
				ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
				wb.write(outByteStream);
				byte[] outArray = outByteStream.toByteArray();
				response.setContentType("application/ms-excel");
				response.setContentLength(outArray.length);
				response.setHeader("Expires:", "0"); // eliminates browser caching
				response.setHeader("Content-Disposition", "attachment; filename=Purchaseorder.xls");
				OutputStream outStream = response.getOutputStream();
				outStream.write(outArray);
				outStream.flush();

			}
			// End code by Bruhan for export to excel Purchase order details on 13 sep 2013

			else if (action.equalsIgnoreCase("Add Products")) {
				String result = "";
				ArrayList al = new ArrayList();
				Hashtable htCond = new Hashtable();
				String pono = StrUtils.fString(request.getParameter("PONO")).trim();
				String RFLAG = StrUtils.fString(request.getParameter("RFLAG")).trim();
				htCond.put("PLANT", plant);
				htCond.put("PONO", pono);
				String query = "pono,custCode,custName,jobNum,ISNULL(CURRENCYID,'" + baseCurrency + "') AS CurrencyID"; // pass
																														// isnull
																														// value
																														// as
																														// local
																														// currency
																														// if
																														// currency
																														// id
																														// is
																														// null
				al = _POUtil.getPoHdrDetails(query, htCond);
				
				if (al.size() < 1) {
					result = "<font color=\"red\"> Please save Purchase order first before adding product  <br><br><center>"
							+ "<input type=\"button\" value=\"Back\" onClick=\"window.location.href='javascript:history.back()'\"> ";

					result = "<br><h3>" + result;
					request.getSession().setAttribute("RESULT", result);
					response.sendRedirect("jsp/displayResult2User.jsp");

				} else {
					if (DbBean.IB_MODIFICATION.equals("Y")) {
						Map m = (Map) al.get(0);
						pono = (String) m.get("pono");
						String jobNum = (String) m.get("jobNum");
						String custCode = (String) m.get("custCode");
						String custName = (String) m.get("custName");
						response.sendRedirect("jsp/createPODET.jsp?Submit=add&PONO=" + pono + "&JOB_NUM=" + StrUtils.replaceCharacters2Send(jobNum)
								+ "&CUST_CODE=" + custCode + "&RFLAG=" + RFLAG + "&CUST_NAME="
								+ StrUtils.replaceCharacters2Send(custName));
					} else {

						_POUtil.isOpenInboundOrder(plant, pono);
						Map m = (Map) al.get(0);
						pono = (String) m.get("pono");
						String jobNum = (String) m.get("jobNum");
						String custCode = (String) m.get("custCode");
						String custName = (String) m.get("custName");
						response.sendRedirect("jsp/createPODET.jsp?Submit=add&PONO=" + pono + "&JOB_NUM=" + StrUtils.replaceCharacters2Send(jobNum)
								+ "&CUST_CODE=" + custCode + "&RFLAG=" + RFLAG + "&CUST_NAME="
								+ StrUtils.replaceCharacters2Send(custName));

					}
				}
			} else if (action.equalsIgnoreCase("UPDATE")) {
				String result = updatePoHdr(request, response);
				request.getSession().setAttribute("RESULT", result);
				response.sendRedirect("jsp/displayResult2User.jsp");
			} else if (action.equalsIgnoreCase("SAVE")) {
				String result = "";
				result = SavePoHdr(request, response);
				request.getSession().setAttribute("RESULT", result);
				response.sendRedirect("jsp/displayResult2User.jsp");
			} else if (action.equalsIgnoreCase("Updatepodet")) {
				try {
					String pono = "", polnno = "", item = "", itemDesc = "",  fieldDesc = "";//result = "",
					pono = StrUtils.fString(request.getParameter("PONO")).trim();
					polnno = StrUtils.fString(request.getParameter("POLNNO")).trim();
					item = StrUtils.fString(request.getParameter("ITEM")).trim();
					itemDesc = StrUtils.fString(request.getParameter("DESC")).trim();
					String qty = StrUtils.removeFormat(StrUtils.fString(request.getParameter("QTY")).trim());
					String cost = StrUtils.removeFormat(StrUtils.fString(request.getParameter("UNITCOST")).trim());
					String uom = StrUtils.removeFormat(StrUtils.fString(request.getParameter("MULTIUOM")).trim());
					String UNITPRICERD= StrUtils.fString(request.getParameter("UNITCOSTRD")).trim();
					String PRODUCTDELIVERYDATE= StrUtils.fString(request.getParameter("PRODUCTDELIVERYDATE")).trim();
					if(cost.equalsIgnoreCase(""))
						cost="0";
					if(UNITPRICERD.equalsIgnoreCase("0"))						
						UNITPRICERD=cost;
					else if(UNITPRICERD.equalsIgnoreCase(""))
						UNITPRICERD=cost;
					cost = new POUtil().getConvertedUnitCostToLocalCurrency(plant, pono, UNITPRICERD);
					//	Begin : Ravindra : Check price and quantity variation and update PO Header status
					Hashtable<String, String> htUpdatePOdet = new Hashtable<>();
					htUpdatePOdet.clear();
					htUpdatePOdet.put(IDBConstants.PLANT, plant);
					htUpdatePOdet.put(IDBConstants.PODET_PONUM, pono);
					htUpdatePOdet.put("POLNNO", polnno);
					String q = "isnull(unitcost, 0) as cost, isnull(qtyor,0)as qty";
					ArrayList aldet = _poDetDAO.selectPoDet(q, htUpdatePOdet, " plant <> ''");
					Map m = (Map) aldet.get(0);
					String qtyOld = (String) m.get("qty");
					String costOld = (String) m.get("cost");
					if (!qtyOld.equals(qty) || !costOld.equals(cost)) {
						String hdrUpdate = " SET STATUS_ID = 'PARTIALLY PAID' ";
						Hashtable<String, String> htCondition = new Hashtable<>();
						htCondition.put("PLANT", plant);
						htCondition.put("PONO", pono);
						htCondition.put("STATUS_ID", "PAID");//	Only paid orders status has to be changed
						try {
							new PoHdrDAO().updatePO(hdrUpdate, htCondition, "");
						}catch(Exception e) {
							if (e == null || !"Unable to update!".equals(ThrowableUtil.getMessage(e))) {
								throw new Exception("While updating stauts", e);
							}
						}
					}
					//	End : Ravindra : Check price and quantity variation and update PO Header status
					// String productRemarks =
					// StrUtils.fString(request.getParameter("PRDREMARKS")).trim();
					Hashtable htUpdateRecvdet = new Hashtable();
					htUpdateRecvdet.clear();
					htUpdateRecvdet.put(IDBConstants.PLANT, plant);
					htUpdateRecvdet.put(IDBConstants.PODET_PONUM, pono);
					htUpdateRecvdet.put(IDBConstants.PODET_POLNNO, polnno);
					htUpdateRecvdet.put(IDBConstants.UNITMO,uom);
					htUpdateRecvdet.put(IDBConstants.ITEM, item);
					htUpdateRecvdet.put(IDBConstants.ITEM_DESC, StrUtils.InsertQuotes(itemDesc));
					// htUpdateRecvdet.put(IDBConstants.PODET_COMMENT1,StrUtils.InsertQuotes(productRemarks));
					// to check item as nonstock item and non stock type as 2 then check cost has
					// minus sympol otherwise prefix minus with cost
					ItemMstDAO _ItemMstDao = new ItemMstDAO();
					Hashtable htIsExist = new Hashtable();
					htIsExist.put(IDBConstants.PLANT, plant);
					htIsExist.put(IDBConstants.ITEM, item);
					htIsExist.put("NONSTKFLAG", "Y");
					htIsExist.put("NONSTKTYPEID", "2");
					boolean isExistNonStock = _ItemMstDao.isExisit(htIsExist, "");
					if (isExistNonStock) {
						String s = cost;
						if (s.indexOf("-") == -1) {
							cost = "-" + cost;
						}
					}
					// end to check item as nonstock item and non stock type as 2 then check cost
					// has minus sympol otherwise prefix minus with cost
					htUpdateRecvdet.put(IDBConstants.PODET_UNITCOST, cost);
					htUpdateRecvdet.put(IDBConstants.PODET_QTYOR, qty);
					htUpdateRecvdet.put(IDBConstants.PODET_PRODUCTDELIVERYDATE, PRODUCTDELIVERYDATE);
					boolean flag = _POUtil.updatePoDetDetails(htUpdateRecvdet);

					// delete & insert podet multi remarks
					if (flag) {
						//	Call Accounting module for update item : Start
						/*if ("yes".equals(request.getSession(false).getAttribute("isAccountingEnabled"))) {
							try {
								Form form = new Form();
								Hashtable htCond = new Hashtable();
								htCond.put("PLANT", plant);
								htCond.put("PONO", pono);
								String query = "pono,custCode";
								ArrayList al = _POUtil.getPoHdrDetails(query, htCond);
								m = (Map) al.get(0);
								String custCode = (String) m.get("custCode");
								form.param("data", "{\"pono\":\"" + pono + "\",\"supplier\":\"" + custCode + "\",\"transactionType\":\"4\",\"plant\":\"" + plant + "\",\"user\":\"" + userName + "\",\"action\":\"updateproduct\"}");							
								String finTransactionResponse = HttpUtils.connect(HttpUtils.getHostInfo(request) + IConstants.ACCOUNTING_APPLICATION_URL.replace("trackaccounting", plant) + "restFinTransaction/create", form);
								if (finTransactionResponse == null || finTransactionResponse.startsWith("Error : ")) {
									throw new Exception(finTransactionResponse);
								}
							}catch(Exception e) {
								throw new Exception(ThrowableUtil.getExceptionMessage(e), e);
							}
						}*/
						//	Call Accounting module for update item : End
						Hashtable htPoRemarksDel = new Hashtable();
						htPoRemarksDel.put(IDBConstants.PLANT, plant);
						htPoRemarksDel.put(IDBConstants.PODET_PONUM, pono);
						htPoRemarksDel.put(IDBConstants.PODET_POLNNO, polnno);
						htPoRemarksDel.put(IDBConstants.PODET_ITEM, item);
						flag = _poDetDAO.deletePoMultiRemarks(htPoRemarksDel);
					}
					// delete podet multi remarks end

					String strMovHisRemarks = "";
					if (flag) {
//						DateUtils dateUtils = new DateUtils();
						String DYNAMIC_REMARKS_SIZE = StrUtils.fString(request.getParameter("DYNAMIC_REMARKS_SIZE"));
						int remarks_count = Integer.parseInt(DYNAMIC_REMARKS_SIZE);
						String productRemarks = "";
						for (int index = 0; index < remarks_count; index++) {
							productRemarks = StrUtils.fString(request.getParameter("PRDREMARKS" + "_" + index));
							strMovHisRemarks = strMovHisRemarks + "," + productRemarks;
							Hashtable htPoRemarks = new Hashtable();
							htPoRemarks.put(IDBConstants.PLANT, plant);
							htPoRemarks.put(IDBConstants.PODET_PONUM, pono);
							htPoRemarks.put(IDBConstants.PODET_POLNNO, polnno);
							htPoRemarks.put(IDBConstants.PODET_ITEM, item);
							htPoRemarks.put(IDBConstants.REMARKS, StrUtils.InsertQuotes(productRemarks));
							htPoRemarks.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
							htPoRemarks.put(IDBConstants.CREATED_BY, userName);
							flag = _POUtil.savePoMultiRemarks(htPoRemarks);
						}
					}
					String numberOfDecimal= _PlantMstDAO.getNumberOfDecimal(plant);
					float costvalue ="".equals(cost) ? 0.0f :  Float.parseFloat(cost);
					cost = StrUtils.addZeroes(costvalue, numberOfDecimal);
					// delete & insert podet multi remarks end
					MovHisDAO movhisDao = new MovHisDAO();
					Hashtable htmovHis = new Hashtable();
					movhisDao.setmLogger(mLogger);
					htmovHis.clear();
					htmovHis.put(IDBConstants.PLANT, plant);
					htmovHis.put("DIRTYPE", "PURCHASE_ORDER_UPDATE_PRODUCT");
					htmovHis.put(IDBConstants.ITEM, item);
					htmovHis.put(IDBConstants.MOVHIS_ORDNUM, pono);
					htmovHis.put(IDBConstants.MOVHIS_ORDLNO, polnno);
					htmovHis.put("QTY", qty);
					htmovHis.put("REMARKS", cost + "," + StrUtils.InsertQuotes(strMovHisRemarks));
					htmovHis.put(IDBConstants.CREATED_BY, userName);
					htmovHis.put("MOVTID", "");
					htmovHis.put("RECID", "");
					htmovHis.put(IDBConstants.TRAN_DATE,
							DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));
					htmovHis.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
					flag = movhisDao.insertIntoMovHis(htmovHis);

					if (flag) {
						ArrayList al = new ArrayList();
						Hashtable htCond = new Hashtable();
						m = new HashMap();
						if (pono.length() > 0) {
							htCond.put("PLANT", plant);
							htCond.put("PONO", pono);

							// String query = " a.pono,isnull(a.ordertype,'') ordertype,(select
							// isnull(display,'') display from "+"["+plant+"_CURRENCYMST] where currencyid =
							// a.currencyid)
							// currencyid,a.inbound_Gst,a.custName,a.custCode,isnull(a.jobNum,'') as
							// jobNum,a.custName,a.personInCharge,a.contactNum,a.address,a.collectionDate,a.collectionTime,isnull(a.remark1,'')
							// as remark1,isnull(a.remark2,'') as remark2,isnull(a.remark3,'') as
							// remark3,isnull(b.name,'') as contactname,isnull(b.telno,'') as
							// telno,isnull(b.email,'') email,isnull(b.addr1,'') add1,isnull(b.addr2,'')
							// add2,isnull(b.addr3,'') add3,isnull(b.remarks,'') remarks,isnull(b.addr4,'')
							// add4,isnull(b.country,'') country,isnull(b.zip,'') zip,isnull(a.deldate,'')
							// as deldate,isnull(scust_name,'')as sCust_Name, isnull(scname,'') as
							// sContact_Name,isnull(sAddr1,'') as sAddr1,isnull(sAddr2,'') as
							// sAddr2,isnull(sCity,'') as sCity,isnull(sCountry,'') as
							// sCountry,isnull(sZip,'') as sZip,isnull(sTelNo,'') as
							// sTelno,isnull(STATUS_ID,'') as statusid ";
							String query = " a.pono,isnull(a.ordertype,'') ordertype,"
									+ "(select isnull(display,'') display from " + "[" + plant
									+ "_CURRENCYMST] where currencyid = a.currencyid) currencyid,"
									+ "isnull(a.inbound_Gst,0) inbound_Gst,b.vname as custName,a.custCode,isnull(a.jobNum,'') as jobNum,"
									+ "a.personInCharge,a.contactNum,a.address,a.collectionDate,a.collectionTime,isnull(a.remark1,'') as remark1,"
									+ "isnull(a.remark2,'') as remark2,isnull(a.remark3,'') as remark3,isnull(b.name,'') as contactname,"
									+ "isnull(b.telno,'') as telno,isnull(b.email,'') email,isnull(b.addr1,'') add1,isnull(b.addr2,'') add2,"
									+ "isnull(b.addr3,'') add3,isnull(b.remarks,'') remarks,isnull(b.addr4,'') add4,"
									+ "isnull(b.country,'') country,isnull(b.zip,'') zip,isnull(a.deldate,'') as deldate,"
									+ "isnull(STATUS_ID,'') as statusid, "
									+ "isnull(shippingid,'')as shippingid, isnull(shippingcustomer,'') as shippingcustomer,"
									+ "isnull(orderdiscount,0) as orderdiscount,isnull(shippingcost,0) as shippingcost,"
									+ "isnull(incoterms,'') as incoterms,isnull(a.DELIVERYDATEFORMAT,'') DELIVERYDATEFORMAT,isnull(a.PAYMENTTYPE,'') payment_terms,isnull(LOCALEXPENSES,0) localexpenses,"
									+ "ISNULL(A.PURCHASE_LOCATION,'') PURCHASE_LOCATION,ISNULL(A.TAXTREATMENT,'') TAXTREATMENT,ISNULL(A.REVERSECHARGE,'') REVERSECHARGE,ISNULL(A.GOODSIMPORT,'') GOODSIMPORT";
							
							al = _POUtil.getSupplierHdrDetails(query, htCond, "");
							if (al.size() > 0) {
								m = (Map) al.get(0);
								fieldDesc = _POUtil.listPODET(pono, plant, rflag);
							} else {

								fieldDesc = "<tr><td colspan=\"10\" align=\"center\">No Records Available</td></tr>";
							}

						}
						if (rflag.equals("1")) {
							request.getSession().setAttribute("podetVal", m);
							request.getSession().setAttribute("RESULT", fieldDesc);
							response.sendRedirect("jsp/CreateIncomingOrder.jsp?PONO=" + pono + "&action=View");
						} else {
							request.getSession().setAttribute("podetVal", m);
							request.getSession().setAttribute("RESULT1", fieldDesc);
							response.sendRedirect("jsp/maintIncomingOrder.jsp?PONO=" + pono + "&action=View");
						}

					} else {
						throw new Exception("Cost  Not Updated");
					}
				} catch (Exception ex) {
					this.mLogger.exception(this.printLog, "", ex);

					String result = "<font class = " + IConstants.FAILED_COLOR + ">Error : " + ex.getMessage()
							+ "</font>";
					result = result + " <br><br><center> "
							+ "<input type=\"button\" value=\"Back\" onClick=\"window.location.href='javascript:history.back()'\"> ";
					result = "<br><h3>" + result;
					request.getSession().setAttribute("RESULT", result);
					response.sendRedirect("jsp/displayResult2User.jsp");
				}
			}

			else if (action.equalsIgnoreCase("DeleteProduct")) {
				try {
					boolean flag = false;
					String fieldDesc = "<tr><td> Please enter serach condition and press GO</td></tr>";
//					String User_id = StrUtils.fString(request.getParameter("LOGIN_USER")).trim();
					String pono = StrUtils.fString(request.getParameter("PONO")).trim();
					String polnno = StrUtils.fString(request.getParameter("POLNNO")).trim();
					String item = StrUtils.fString(request.getParameter("ITEM")).trim();
					Hashtable htdet = new Hashtable();
					htdet.put(IDBConstants.PLANT, plant);
					htdet.put(IDBConstants.PODET_PONUM, pono);
					htdet.put(IDBConstants.PODET_POLNNO, polnno);
					htdet.put(IDBConstants.PODET_ITEM, item);
					htdet.put(IDBConstants.CREATED_BY, userName);
					_POUtil.setmLogger(mLogger);
					flag = _POUtil.deleteIBLineDetails(htdet);

					/*
					 * //delete & insert podet multi remarks if(flag){
					 * 
					 * Hashtable htPoRemarksDel = new Hashtable();
					 * htPoRemarksDel.put(IDBConstants.PLANT,plant);
					 * htPoRemarksDel.put(IDBConstants.PODET_PONUM, pono);
					 * htPoRemarksDel.put(IDBConstants.PODET_POLNNO,polnno);
					 * htPoRemarksDel.put(IDBConstants.PODET_ITEM, item); flag =
					 * _poDetDAO.deletePoMultiRemarks(htPoRemarksDel); }
					 */
					// delete podet multi remarks end

					if (flag) {
						Hashtable htCond = new Hashtable();
						htCond.put("PLANT", plant);
						htCond.put("PONO", pono);
						String query = "pono,custCode";
						ArrayList al = _POUtil.getPoHdrDetails(query, htCond);
						Map m = (Map) al.get(0);
						//	Call Accounting module for delete item : Start
						/*if ("yes".equals(request.getSession(false).getAttribute("isAccountingEnabled"))) {
							try {
								Form form = new Form();
								String custCode = (String) m.get("custCode");
								form.param("data", "{\"pono\":\"" + pono + "\",\"supplier\":\"" + custCode + "\",\"transactionType\":\"4\",\"plant\":\"" + plant + "\",\"user\":\"" + userName + "\",\"action\":\"delproduct\"}");							
								String finTransactionResponse = HttpUtils.connect(HttpUtils.getHostInfo(request) + IConstants.ACCOUNTING_APPLICATION_URL.replace("trackaccounting", plant) + "restFinTransaction/create", form);
								if (finTransactionResponse == null || finTransactionResponse.startsWith("Error : ")) {
									throw new Exception(finTransactionResponse);
								}
							}catch(Exception e) {
								throw new Exception(ThrowableUtil.getExceptionMessage(e), e);
							}
						}*/
						//	Call Accounting module for delete item : End
						// POHDR Update
						String updateDoHdr = "";
						boolean updateHdrFlag = false;
						boolean isExitFlag = false;
						PoHdrDAO _PoHdrDAO = new PoHdrDAO();
						PoDetDAO _PoDetDAO = new PoDetDAO();
						Hashtable htConditoHdr = new Hashtable();
						htConditoHdr.put("PLANT", plant);
						htConditoHdr.put("pono", pono);
						isExitFlag = _PoDetDAO.isExisit(htConditoHdr, "lnstat in ('O','N')");
						if (isExitFlag) {
							updateHdrFlag = false;
						} else {
							updateDoHdr = "set  status='C' ";
							updateHdrFlag = true;
						}

						if (updateHdrFlag == true) {
							flag = _PoHdrDAO.updatePO(updateDoHdr, htConditoHdr, "");
						}
						// POHDR Update
						al = new ArrayList();
						htCond = new Hashtable();
						m = new HashMap();
						if (pono.length() > 0) {
							htCond.put("PLANT", plant);
							htCond.put("PONO", pono);

							// String query = " a.pono,isnull(a.ordertype,'') ordertype,(select
							// isnull(display,'') display from "+"["+plant+"_CURRENCYMST] where currencyid =
							// a.currencyid)
							// currencyid,a.inbound_Gst,a.custName,a.custCode,isnull(a.jobNum,'') as
							// jobNum,a.custName,a.personInCharge,a.contactNum,a.address,a.collectionDate,a.collectionTime,isnull(a.remark1,'')
							// as remark1,a.remark2,isnull(b.name,'') as contactname,isnull(b.telno,'') as
							// telno,isnull(b.email,'') email,isnull(b.addr1,'') add1,isnull(b.addr2,'')
							// add2,isnull(b.addr3,'') add3,isnull(b.remarks,'') remarks,isnull(b.addr4,'')
							// add4,isnull(b.country,'') country,isnull(b.zip,'') zip,isnull(a.deldate,'')
							// as deldate,isnull(scust_name,'')as sCust_Name, isnull(scname,'') as
							// sContact_Name,isnull(sAddr1,'') as sAddr1,isnull(sAddr2,'') as
							// sAddr2,isnull(sCity,'') as sCity,isnull(sCountry,'') as
							// sCountry,isnull(sZip,'') as sZip,isnull(sTelNo,'') as sTelno ";
							query = " a.pono,isnull(a.ordertype,'') ordertype,"
									+ "(select isnull(display,'') display from " + "[" + plant
									+ "_CURRENCYMST] where currencyid = a.currencyid) currencyid,"
									+ "isnull(a.inbound_Gst,0) inbound_Gst,b.vname as custName,a.custCode,isnull(a.jobNum,'') as jobNum,"
									+ "a.personInCharge,a.contactNum,a.address,a.collectionDate,a.collectionTime,isnull(a.remark1,'') as remark1,"
									+ "isnull(a.remark2,'') as remark2,isnull(a.remark3,'') as remark3,isnull(b.name,'') as contactname,"
									+ "isnull(b.telno,'') as telno,isnull(b.email,'') email,isnull(b.addr1,'') add1,isnull(b.addr2,'') add2,"
									+ "isnull(b.addr3,'') add3,isnull(b.remarks,'') remarks,isnull(b.addr4,'') add4,"
									+ "isnull(b.country,'') country,isnull(b.zip,'') zip,isnull(a.deldate,'') as deldate,"
									+ "isnull(STATUS_ID,'') as statusid, "
									+ "isnull(shippingid,'')as shippingid, isnull(shippingcustomer,'') as shippingcustomer,"
									+ "isnull(orderdiscount,0) as orderdiscount,isnull(shippingcost,0) as shippingcost,"
									+ "isnull(incoterms,'') as incoterms,isnull(a.DELIVERYDATEFORMAT,'') DELIVERYDATEFORMAT,isnull(pay_terms,'') payment_terms,isnull(LOCALEXPENSES,0) localexpenses,"
									+ "ISNULL(A.PURCHASE_LOCATION,'') PURCHASE_LOCATION,ISNULL(A.TAXTREATMENT,'') TAXTREATMENT,ISNULL(A.REVERSECHARGE,'') REVERSECHARGE,ISNULL(A.GOODSIMPORT,'') GOODSIMPORT";

							al = _POUtil.getSupplierHdrDetails(query, htCond, "");
							if (al.size() > 0) {
								m = (Map) al.get(0);
								fieldDesc = _POUtil.listPODET(pono, plant, rflag);
							} else {

								fieldDesc = "<tr><td colspan=\"10\" align=\"center\">No Records Available</td></tr>";
							}

						}
						if (rflag.equals("1")) {
							request.getSession().setAttribute("podetVal", m);
							request.getSession().setAttribute("RESULT", fieldDesc);
							response.sendRedirect("jsp/CreateIncomingOrder.jsp?PONO=" + pono + "&action=View");
						} else {
							request.getSession().setAttribute("podetVal", m);
							request.getSession().setAttribute("RESULT1", fieldDesc);
							response.sendRedirect("jsp/maintIncomingOrder.jsp?PONO=" + pono + "&action=View");

						}

					} else {
						throw new Exception("Product ID Not Deleted Successfully");
					}
				} catch (Exception ex) {
					this.mLogger.exception(this.printLog, "", ex);

					String result = "<font class = " + IConstants.FAILED_COLOR + ">Error : " + ex.getMessage()
							+ "</font>";
					result = result + " <br><br><center> "
							+ "<input type=\"button\" value=\"Back\" onClick=\"window.location.href='javascript:history.back()'\"> ";
					result = "<br><h3>" + result;
					request.getSession().setAttribute("RESULT", result);
					response.sendRedirect("jsp/displayResult2User.jsp");
				}
			} else if (action.equalsIgnoreCase("DELETE")) {
//				sqlBean sqlbn = new sqlBean();
				String User_id = StrUtils.fString(request.getParameter("LOGIN_USER")).trim();
				String pono = StrUtils.fString(request.getParameter("PONO")).trim();
				Hashtable htPoHrd = new Hashtable();
				htPoHrd.put(IDBConstants.PLANT, plant);
				htPoHrd.put(IDBConstants.PODET_PONUM, pono);
				// Amended by Samatha On 23/4/2014 to allow to delete order ignoring nonstock
				// items
				boolean isValidOrder = new PoHdrDAO().isExisit(htPoHrd, "");
				boolean isOrderInProgress = new PoDetDAO().isExisit(htPoHrd,
						"LNSTAT in ('O' ,'C') and ITEM NOT IN(SELECT ITEM FROM [" + plant
								+ "_ITEMMST] where NONSTKFLAG='Y')");
				if (isValidOrder) {
					if (!isOrderInProgress) {
						boolean isExistsPaymentDetails = false;
						isExistsPaymentDetails = new OrderPaymentUtil().isExistsOrderPaymentDetails(plant, pono);
						if (!isExistsPaymentDetails) {
							Hashtable htCond = new Hashtable();
							htCond.put("PLANT", plant);
							htCond.put("PONO", pono);
//							String query = "pono,custCode";
//							ArrayList al = _POUtil.getPoHdrDetails(query, htCond);
//							Map m = (Map) al.get(0);
//							String custCode = (String) m.get("custCode");
							Boolean value = this._POUtil.removeRow(plant, pono, User_id);
							if (value) {
								//	Call Accounting module for new order : Start
								/*if ("yes".equals(request.getSession(false).getAttribute("isAccountingEnabled"))) {
									try {
										Form form = new Form();
										form.param("data", "{\"pono\":\"" + pono + "\",\"supplier\":\"" + custCode + "\",\"amount\":\"0\",\"transactionType\":\"4\",\"plant\":\"" + plant + "\",\"user\":\"" + userName + "\",\"action\":\"deleteorder\"}");							
										String finTransactionResponse = HttpUtils.connect(HttpUtils.getHostInfo(request) + IConstants.ACCOUNTING_APPLICATION_URL.replace("trackaccounting", plant) + "restFinTransaction/create", form);
										if (finTransactionResponse == null || finTransactionResponse.startsWith("Error : ")) {
											throw new Exception(finTransactionResponse);
										}
									}catch(Exception e) {
										throw new Exception(ThrowableUtil.getExceptionMessage(e), e);
									}
								}*/
								//	Call Accounting module for new order : End
								response.sendRedirect("/track/purchaseorderservlet?statusValue=100&PONO=" + pono
										+ "&Submit=View&RFLAG=4");
							} else {
								response.sendRedirect("/track/purchaseorderservlet?statusValue=97&PONO=" + pono
										+ "&Submit=View&RFLAG=4");
							}
						} else {
							response.sendRedirect(
									"/track/purchaseorderservlet?statusValue=96&PONO=" + pono + "&Submit=View&RFLAG=4");
						}
					} else {
						response.sendRedirect(
								"/track/purchaseorderservlet?statusValue=98&PONO=" + pono + "&Submit=View&RFLAG=4");
					}
				} else {
					response.sendRedirect(
							"/track/purchaseorderservlet?statusValue=99&PONO=" + pono + "&Submit=View&RFLAG=4");
				}

			} else if (action.equalsIgnoreCase("Add Product")) {
				MovHisDAO movHisDao = new MovHisDAO();
				movHisDao.setmLogger(mLogger);
//				RecvDetDAO _RecvDetDAO = new RecvDetDAO();
//				DateUtils dateUtils = new DateUtils();
				String pono = "", unitCost = "", manufacturer = "", custCode = "", custName = "", jobNum = "",
						polno = "", item = "", qty = "", itemDesc = "", result = "", taxby = "", prodgst = "",UNITPRICERD="",PRODUCTDELIVERYDATE="";
				String User_id = StrUtils.fString(request.getParameter("LOGIN_USER")).trim();
				pono = StrUtils.fString(request.getParameter("PONO")).trim();
				polno = StrUtils.fString(request.getParameter("POLNNO")).trim();
				custCode = StrUtils.fString(request.getParameter("CUST_CODE")).trim();
				custName = StrUtils.fString(request.getParameter("CUST_NAME")).trim();
				jobNum = StrUtils.fString(request.getParameter("JOB_NUM")).trim();
				item = StrUtils.fString(request.getParameter("ITEM")).trim();
				itemDesc = StrUtils.fString(request.getParameter("DESC")).trim();
				qty = StrUtils.fString(request.getParameter("QTY")).trim();
				String RFLAG = StrUtils.fString(request.getParameter("RFLAG")).trim();
				unitCost = StrUtils.removeFormat(StrUtils.fString(request.getParameter("UNITCOST")).trim());
				PRODUCTDELIVERYDATE = StrUtils.removeFormat(StrUtils.fString(request.getParameter("PRODUCTDELIVERYDATE")).trim());
				String uom = StrUtils.fString(request.getParameter("MULTIUOM").trim());
				UNITPRICERD= StrUtils.fString(request.getParameter("UNITCOSTRD")).trim();
				if(unitCost.equalsIgnoreCase(""))
					unitCost="0";
				if(UNITPRICERD.equalsIgnoreCase("0"))						
					UNITPRICERD=unitCost;
				else if(UNITPRICERD.equalsIgnoreCase(""))
					UNITPRICERD=unitCost;
				unitCost = new POUtil().getConvertedUnitCostToLocalCurrency(plant, pono, UNITPRICERD);
				//	Begin : Ravindra : Check price and quantity variation and update PO Header status
				String hdrUpdate = " SET STATUS_ID = 'PARTIALLY PAID'";
				Hashtable htCondition = new Hashtable();
				htCondition.put("PLANT", plant);
				htCondition.put("PONO", pono);
				htCondition.put("STATUS_ID", "PAID");//	Only paid orders status has to be changed
				try {
					new PoHdrDAO().updatePO(hdrUpdate, htCondition, "");
				}catch(Exception e) {
					if (e == null || !"Unable to update!".equals(ThrowableUtil.getMessage(e))) {
						throw new Exception("While updating stauts", e);
					}
				}
				//	End : Ravindra : Check price and quantity variation and update PO Header status
				manufacturer = StrUtils.fString(request.getParameter("MANUFACT")).trim();
				// String productRemarks =
				// StrUtils.fString(request.getParameter("PRDREMARKS")).trim();
				ItemMstUtil itemMstUtil = new ItemMstUtil();
				itemMstUtil.setmLogger(mLogger);
				itemMstUtil.isValidItemInItemmst(plant, item);

				/// Get Non Stock Type
				// String nonstocktype = new ItemMstDAO().getNonStockFlag(plant,item);
				Map mPrddet = new ItemMstDAO().getProductNonStockDetails(plant, item);
				String nonstocktype = StrUtils.fString((String) mPrddet.get("NONSTKFLAG"));
				String nonstocktypeDesc = StrUtils.fString((String) mPrddet.get("STOCKTYPEDESC"));
				itemDesc = StrUtils.fString((String) mPrddet.get("ITEMDESC"));

				// ADD ALL TO PODET
				Hashtable htPodet = new Hashtable();
				htPodet.put(IDBConstants.PLANT, plant);
				htPodet.put(IDBConstants.PODET_PONUM, pono);
				htPodet.put(IDBConstants.PODET_POLNNO, polno);
				htPodet.put(IDBConstants.PODET_ITEM, item);
				htPodet.put("ITEMDESC", StrUtils.InsertQuotes(itemDesc));
			//	htPodet.put("UNITMO", StrUtils.fString((String) mPrddet.get("STKUOM")));
				htPodet.put(IDBConstants.UNITMO,uom);
				htPodet.put(IDBConstants.PODET_ITEM_DESC,
						StrUtils.InsertQuotes(StrUtils.fString(new ItemMstDAO().getItemDesc(plant, item))));
				htPodet.put(IDBConstants.PODET_UNITCOST, unitCost);
				htPodet.put(IDBConstants.PODET_JOB_NUM, jobNum);
				htPodet.put(IDBConstants.PODET_CUST_NAME, StrUtils.InsertQuotes(custName));
				htPodet.put(IDBConstants.PODET_QTYOR, qty);
				htPodet.put(IDBConstants.PODET_PRODUCTDELIVERYDATE, PRODUCTDELIVERYDATE);

				htPodet.put(IDBConstants.MANUFACTURER, StrUtils.InsertQuotes(manufacturer));
				if (nonstocktype.equals("Y")) {
					if (nonstocktypeDesc.equalsIgnoreCase("discount")) {
						htPodet.put(IDBConstants.PODET_UNITCOST, "-" + unitCost);

					}
				}

				htPodet.put(IDBConstants.PODET_LNSTATUS, "N");

//				java.util.Date dt = new java.util.Date();
//				SimpleDateFormat dfVisualDate = new SimpleDateFormat("dd/MM/yyyy");
//				String today = dfVisualDate.format(dt);

				htPodet.put(IDBConstants.TRAN_DATE, DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));
				String CURRENCYUSEQT = new POUtil().getCurrencyUseQT(plant, pono);
				htPodet.put(IDBConstants.CURRENCYUSEQT, CURRENCYUSEQT);
				// CHECK ITEM IN PODET
				Hashtable ht = new Hashtable();
				ht.put(IDBConstants.PLANT, plant);
				ht.put(IDBConstants.PODET_ITEM, item);

				Boolean isProductAvailable = Boolean.valueOf(false);
				ItemUtil itemUtil = new ItemUtil();
				itemUtil.setmLogger(mLogger);
//				Hashtable htRecvDet = new Hashtable();
				isProductAvailable = itemUtil.isExistsItemMst(item, plant);
				taxby = _PlantMstDAO.getTaxBy(plant);
				if (taxby.equalsIgnoreCase("BYPRODUCT")) {
					prodgst = _ItemMstDAO.getProductGst(plant, item);
					htPodet.put(IDBConstants.PRODGST, prodgst);
				}
				/* to get product gst end */
				if (isProductAvailable) {
					boolean flag = false;
					flag = _POUtil.savePoDetDetails(htPodet);
					// insert podet multi remarks
					if (flag) {
						String DYNAMIC_REMARKS_SIZE = StrUtils.fString(request.getParameter("DYNAMIC_REMARKS_SIZE"));
						int remarks_count = Integer.parseInt(DYNAMIC_REMARKS_SIZE);
						String productRemarks = "";
						for (int index = 0; index < remarks_count; index++) {
							productRemarks = StrUtils.fString(request.getParameter("PRDREMARKS" + "_" + index));
							Hashtable htPoRemarks = new Hashtable();
							htPoRemarks.put(IDBConstants.PLANT, plant);
							htPoRemarks.put(IDBConstants.PODET_PONUM, pono);
							htPoRemarks.put(IDBConstants.PODET_POLNNO, polno);
							htPoRemarks.put(IDBConstants.PODET_ITEM, item);
							htPoRemarks.put(IDBConstants.REMARKS, StrUtils.InsertQuotes(productRemarks));
							htPoRemarks.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
							htPoRemarks.put(IDBConstants.CREATED_BY, userName);
							flag = _POUtil.savePoMultiRemarks(htPoRemarks);
							Hashtable htMaster = new Hashtable();
							if (flag) {
								if (productRemarks.length() > 0) {
									htMaster.clear();
									htMaster.put(IDBConstants.PLANT, plant);
									htMaster.put(IDBConstants.REMARKS, StrUtils.InsertQuotes(productRemarks));
									if (!_MasterDAO.isExisitRemarks(htMaster, "")) {
									htMaster.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
									htMaster.put(IDBConstants.CREATED_BY, userName);
										_MasterDAO.InsertRemarks(htMaster);
										Hashtable htRecvHis = new Hashtable();
										htRecvHis.put(IDBConstants.PLANT, plant);
										htRecvHis.put("DIRTYPE", "CREATE_REMARKS");
										htRecvHis.put("ORDNUM", "");
										htRecvHis.put(IDBConstants.ITEM, "");
										htRecvHis.put("BATNO", "");
										htRecvHis.put(IDBConstants.LOC, "");
										htRecvHis.put("REMARKS", StrUtils.InsertQuotes(productRemarks));
										htRecvHis.put(IDBConstants.CREATED_BY, userName);
										htRecvHis.put(IDBConstants.TRAN_DATE,
												DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));
										htRecvHis.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
										flag = movHisDao.insertIntoMovHis(htRecvHis);
									}
								}

							}

						}
					}
					//// insert podet multi remarks end

					if (flag) {
						// To check DOHDR status and update
						Boolean hdrFlag = false;
//						PoDetDAO _PoDetDAO = new PoDetDAO();
						PoHdrDAO _PoHdrDAO = new PoHdrDAO();
						String updateDoHdr = "";
						Hashtable htConditoHdr = new Hashtable();
						htConditoHdr.put("PLANT", plant);
						htConditoHdr.put("pono", pono);
						hdrFlag = _PoHdrDAO.isExisit(htConditoHdr, " isnull(STATUS,'') in ('C')");
						if (hdrFlag) {
							updateDoHdr = "set  STATUS='O' ";
							flag = _PoHdrDAO.updatePO(updateDoHdr, htConditoHdr, "");
						}
						// To check DOHDR status and update end
						Hashtable htRecvHis = new Hashtable();
						htRecvHis.clear();
						htRecvHis.put(IDBConstants.PLANT, plant);
						htRecvHis.put("DIRTYPE", TransactionConstants.IB_ADD_ITEM);
						htRecvHis.put(IDBConstants.CUSTOMER_CODE, custCode);
						htRecvHis.put(IDBConstants.POHDR_JOB_NUM, jobNum);
						htRecvHis.put(IDBConstants.ITEM, item);
						htRecvHis.put(IDBConstants.QTY, qty);
						htRecvHis.put(IDBConstants.MOVHIS_ORDNUM, pono);
						htRecvHis.put(IDBConstants.CREATED_BY, User_id);
						htRecvHis.put("MOVTID", "");
						htRecvHis.put("RECID", "");
						htRecvHis.put(IDBConstants.TRAN_DATE, DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));
						htRecvHis.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());

						flag = movHisDao.insertIntoMovHis(htRecvHis);

					}

					if (flag) {
						result = "<font color=\"green\">Product Added Successfully</font><br><br><center>"
								+ "<input type=\"button\" value=\" OK \" onClick=\"window.location.href='/track/PurchaseOrderServlet?PONO="
								+ pono + "&Submit=View'\">";
						//	Call Accounting module for new order : Start
						/*if ("yes".equals(request.getSession(false).getAttribute("isAccountingEnabled"))) {
							try {
								Form form = new Form();
								form.param("data", "{\"pono\":\"" + pono + "\",\"supplier\":\"" + custCode + "\",\"transactionType\":\"4\",\"plant\":\"" + plant + "\",\"user\":\"" + User_id + "\",\"action\":\"addproduct\"}");
								String finTransactionResponse = HttpUtils.connect(HttpUtils.getHostInfo(request) + IConstants.ACCOUNTING_APPLICATION_URL.replace("trackaccounting", plant) + "restFinTransaction/create", form);
								if (finTransactionResponse == null || finTransactionResponse.startsWith("Error : ")) {
									throw new Exception(finTransactionResponse);
								}
							}catch(Exception e) {
								throw new Exception(ThrowableUtil.getExceptionMessage(e), e);
							}
						}*/
						//	Call Accounting module for add item : End
					}else
						result = "<font color=\"red\"> Error in Product Addition   <br><br><center>"
								+ "<input type=\"button\" value=\"Back\" onClick=\"window.location.href='javascript:history.back()'\"> ";

					result = "<br><h3>" + result;

				} else {
					result = "<font color=\"red\"> Product Not Available   <br><br><center>"
							+ "<input type=\"button\" value=\"Back\" onClick=\"window.location.href='javascript:history.back()'\"> ";

				}

				response.sendRedirect(
						"jsp/createPODET.jsp?Submit=add&PONO=" + pono + "&JOB_NUM=" + StrUtils.replaceCharacters2Send(jobNum) + "&CUST_CODE=" + custCode
								+ "&RFLAG=" + RFLAG + "&CUST_NAME=" + StrUtils.replaceCharacters2Send(custName));

			}

			// ---Added by Bruhan on May 21 2014, Description:To open Inbound order summary in
			// excel powershell format
			else if (action.equals("ExportExcelInboundOrderSummary")) {
//				String newHtml = "";
				HSSFWorkbook wb = new HSSFWorkbook();
//				StrUtils strUtils = new StrUtils();
//				String PLANT = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
				wb = this.writeToExcelInboundOrderSummary(request, response, wb);
				String type = StrUtils.fString(request.getParameter("DIRTYPE"));
				ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
				wb.write(outByteStream);
				byte[] outArray = outByteStream.toByteArray();
				response.setContentType("application/ms-excel");
				response.setContentLength(outArray.length);
				response.setHeader("Expires:", "0"); // eliminates browser caching
				
				if(type.equalsIgnoreCase("IB_SUMMARY_RECV")) {
					response.setHeader("Content-Disposition", "attachment; filename=PurchaseOrdersummaryDetails.xls");
				}else if(type.equalsIgnoreCase("IB_SUMMARY_RECV_WITH_COST")) {
					response.setHeader("Content-Disposition", "attachment; filename=PurchaseOrderSummaryDetailsByCost.xls");
				}
				else
				response.setHeader("Content-Disposition", "attachment; filename=PurchaseOrderSummary.xls");
				OutputStream outStream = response.getOutputStream();
				outStream.write(outArray);
				outStream.flush();
				outStream.close();
			} else if (action.equals("ExportExcelInboundOrderWithRemarksSummary")) {
//				String newHtml = "";
				HSSFWorkbook wb = new HSSFWorkbook();
//				String PLANT = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
				wb = this.writeToExcelInboundOrderWithRemarksSummary(request, response, wb);
				ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
				wb.write(outByteStream);
				byte[] outArray = outByteStream.toByteArray();
				response.setContentType("application/ms-excel");
				response.setContentLength(outArray.length);
				response.setHeader("Expires:", "0"); // eliminates browser caching
				response.setHeader("Content-Disposition", "attachment; filename=PurchaseOrderSummary.xls");
				OutputStream outStream = response.getOutputStream();
				outStream.write(outArray);
				outStream.flush();
				outStream.close();
			}
			// ---End Added by Bruhan on May 21 2014, Description:To open Inbound order
			// summary in excel powershell format

			else if (action.equalsIgnoreCase("Copy IB")) {

				String deldate = "", jobNum = "", custName = "", 
						personIncharge = "", remark1 = "", 
						collectionTime = "", telno = "",
						email = "", currencyid = "", add1 = "", add2 = "", add3 = "", add4 = "", country = "", zip = "",
						remarks = "", gst = "";
						//sCountry = "", sZip = "", sTelno = "", fieldDesc = "",sTAXTREATMENT="",sPURCHASE_LOC="",sREVERSECHARGE="",sGOODSIMPORT="",address2 = "",sCustName = "", sContactName = "", sAddr1 = "", sAddr2 = "", sCity = "",
//address3 = "", collectionDate = "", vend = "", ordertype = "", custCode = "",contactNum = "", address = "", contactname = "", remark2 = "", 
				String pono = StrUtils.fString(request.getParameter("PONO"));

				Hashtable htpodet = new Hashtable();
				htpodet.put("PLANT", plant);
				htpodet.put("PONO", pono);

				boolean validpono = _poDetDAO.isExisit(htpodet);
				if (!validpono) {
					throw new Exception("Not a valid Order Number : " + pono + ", Please enter valid Order Number..");
				}

				com.track.dao.TblControlDAO _TblControlDAO = new com.track.dao.TblControlDAO();
				_TblControlDAO.setmLogger(mLogger);
				String newpono = _TblControlDAO.getNextOrder(plant, userName, IConstants.INBOUND);

				String custcode = StrUtils.fString(request.getParameter("CUST_CODE1"));
				jobNum = StrUtils.fString(request.getParameter("JOB_NUM"));
				deldate = StrUtils.fString(request.getParameter("DELDATE"));
				collectionTime = StrUtils.fString(request.getParameter("COLLECTION_TIME"));
				remark1 = StrUtils.fString(request.getParameter("REMARK1"));
				gst = StrUtils.fString(request.getParameter("GST"));
				currencyid = StrUtils.fString(request.getParameter("DISPLAY"));
				custName = StrUtils.fString(request.getParameter("CUST_NAME"));
				personIncharge = StrUtils.fString(request.getParameter("PERSON_INCHARGE"));
				telno = StrUtils.fString(request.getParameter("TELNO"));
				email = StrUtils.fString(request.getParameter("EMAIL"));
				add1 = StrUtils.fString(request.getParameter("ADD1"));
				add2 = StrUtils.fString(request.getParameter("ADD2"));
				add3 = StrUtils.fString(request.getParameter("ADD3"));
				add4 = StrUtils.fString(request.getParameter("ADD4"));
				country = StrUtils.fString(request.getParameter("COUNTRY"));
				zip = StrUtils.fString(request.getParameter("ZIP"));
				remarks = StrUtils.fString(request.getParameter("REMARK2"));

				ArrayList al = new ArrayList();
				Hashtable htCond = new Hashtable();
				Map m = new HashMap();

				if (pono.length() > 0) {
					htCond.put("PLANT", plant);
					htCond.put("PONO", pono);
			
					String query = " a.pono,isnull(a.ordertype,'') ordertype,"
							+ "(select isnull(display,'') display from " + "[" + plant
							+ "_CURRENCYMST] where currencyid = a.currencyid) currencyid,"
							+ "isnull(a.inbound_Gst,0) inbound_Gst,b.vname as custName,a.custCode,isnull(a.jobNum,'') as jobNum,"
							+ "a.personInCharge,a.contactNum,a.address,a.collectionDate,a.collectionTime,isnull(a.remark1,'') as remark1,"
							+ "isnull(a.remark2,'') as remark2,isnull(a.remark3,'') as remark3,isnull(b.name,'') as contactname,"
							+ "isnull(b.telno,'') as telno,isnull(b.email,'') email,isnull(b.addr1,'') add1,isnull(b.addr2,'') add2,"
							+ "isnull(b.addr3,'') add3,isnull(b.remarks,'') remarks,isnull(b.addr4,'') add4,"
							+ "isnull(b.country,'') country,isnull(b.zip,'') zip,isnull(a.deldate,'') as deldate,"
							+ "isnull(STATUS_ID,'') as statusid, "
							+ "isnull(shippingid,'')as shippingid, isnull(shippingcustomer,'') as shippingcustomer,"
							+ "isnull(orderdiscount,0) as orderdiscount,isnull(shippingcost,0) as shippingcost,"
							+ "isnull(incoterms,'') as incoterms,isnull(a.DELIVERYDATEFORMAT,'') DELIVERYDATEFORMAT,isnull(a.PAYMENTTYPE,'') payment_terms,isnull(LOCALEXPENSES,'') localexpenses,"
							+ "ISNULL(A.PURCHASE_LOCATION,'') PURCHASE_LOCATION,ISNULL(A.TAXTREATMENT,'') TAXTREATMENT,ISNULL(A.REVERSECHARGE,'') REVERSECHARGE,ISNULL(A.GOODSIMPORT,'') GOODSIMPORT";
					al = _POUtil.getSupplierHdrDetails(query, htCond, "");
					if (al.size() > 0) {
						m = (Map) al.get(0);

					} else {

//						fieldDesc = "<tr><td colspan=\"10\" align=\"center\">No Records Available</td></tr>";
					}

					request.getSession().setAttribute("podetVal", m);
				}

				response.sendRedirect("/track/jsp/CopyToInbound.jsp?OLDPONO=" + pono + "&action=View" + "&PONO="
						+ newpono + "&REFNO=" + jobNum + "&ORDDATE=" + deldate + "&TIME=" + collectionTime + "&REMARK1="
						+ StrUtils.replaceCharacters2Send(remark1) + "&TAX=" + gst + "&CURRENCY=" + currencyid + "&CUSTNAME=" + custName
						+ "&PERSONINCHARGE=" + personIncharge + "&TELNO=" + telno + "&EMAIL=" + email + "&ADD1=" + add1
						+ "&ADD2=" + add2 + "&ADD3=" + add3 + "&ADD4=" + add4 + "&COUNTRY=" + country + "&ZIP=" + zip
						+ "&CUSTCODE=" + custcode + "&REMARK2=" + StrUtils.replaceCharacters2Send(remarks) + "");

			}

			else if (action.equalsIgnoreCase("Convert IB")) {

				String deldate = "", jobNum = ""/*, vend = "", custName = "", ordertype = "", custCode = "",
						personIncharge = "", contactNum = ""*/;
				String remark1 = "", remark2 = "", collectionDate = "",
						collectionTime = "";
				String currencyid = "", gst = "";//contactname = "", telno = "", email = "", add1 = "", add2 = "", add3 = "",add4 = "", country = "", zip = "", remarks = "", 
				//String sCustName = "", sContactName = "", sAddr1 = "", sAddr2 = "", sCity = "", sCountry = "",address = "", address2 = "", address3 = "", 
					//	sZip = "", sTelno = "", empno = "";

				String dono = StrUtils.fString(request.getParameter("DONO"));

				Hashtable htdodet = new Hashtable();
				htdodet.put("PLANT", plant);
				htdodet.put("DONO", dono);

				boolean validdono = _DoDetDAO.isExisit(htdodet);
				if (!validdono) {
					throw new Exception("Not a valid Order Number : " + dono + ", Please enter valid Order Number..");
				}

				com.track.dao.TblControlDAO _TblControlDAO = new com.track.dao.TblControlDAO();
				_TblControlDAO.setmLogger(mLogger);
				String newpono = _TblControlDAO.getNextOrder(plant, userName, IConstants.INBOUND);

//				String custcode = StrUtils.fString(request.getParameter("CUST_CODE1"));
				jobNum = StrUtils.fString(request.getParameter("JOB_NUM"));
				deldate = StrUtils.fString(request.getParameter("DELDATE"));
				collectionDate = StrUtils.fString(request.getParameter("ORDDATE"));
				collectionTime = StrUtils.fString(request.getParameter("COLLECTION_TIME"));
				remark1 = StrUtils.fString(request.getParameter("REMARK1"));
				remark2 = StrUtils.fString(request.getParameter("REMARK3"));
				gst = StrUtils.fString(request.getParameter("GST"));
				currencyid = StrUtils.fString(request.getParameter("DISPLAY"));

				ArrayList al = new ArrayList();
				Hashtable htCond = new Hashtable();
				Map m = new HashMap();
//				POUtil _poUtil = new POUtil();
				if (dono.length() > 0) {
					htCond.put("PLANT", plant);
					htCond.put("DONO", dono);

					String query = "dono,isnull(outbound_Gst,0) outbound_Gst,"
							+ "(select isnull(display,'') display from " + "[" + plant
							+ "_CURRENCYMST] where currencyid = a.currencyid) currencyid,"
							+ "jobNum,isnull(collectionDate,'') collectionDate,isnull(collectionTime,'') collectionTime,"
							+ "isnull(remark1,'') remark1,isnull(deliverydate,'') deliverydate,"
							+ "isnull(remark3,'') remark3,isnull(STATUS_ID,'') as statusid,"
							+ "isnull(orderdiscount,0) as orderdiscount,isnull(shippingcost,0) as shippingcost,"
							+ "isnull(incoterms,'') as incoterms,isnull(pay_terms,'') payment_terms,isnull(DELIVERYDATEFORMAT,'') DELIVERYDATEFORMAT,  "
							+ "ISNULL(A.SALES_LOCATION,'') SALES_LOCATION,ISNULL(A.TAXTREATMENT,'') TAXTREATMENT ";
					al = _dOUtil.getOutGoingDoHdrDetails(query, htCond);
					if (al.size() > 0) {
						m = (Map) al.get(0);

					}

				}
				request.getSession().setAttribute("podetVal", m);

				response.sendRedirect("/track/jsp/ConvertOutboundToInbound.jsp?DONO=" + dono + "&action=View" + "&PONO="
						+ newpono + "&REFNO=" + jobNum + "&ORDDATE=" + collectionDate + "&DElDATE=" + deldate + "&TIME="
						+ collectionTime + "&REMARK1=" + StrUtils.replaceCharacters2Send(remark1) + "&TAX=" + gst + "&CURRENCY=" + currencyid
						+ "&REMARK2=" + StrUtils.replaceCharacters2Send(remark2) + "");

			} else if (action.equalsIgnoreCase("copy To Inbound Order")) {

				try {
					String oldpono = "", pono = "", polno = "", item = "", itemDesc = "", result = "",
							copytype = "", q = "", prdRemarks = "", itemuom = "", cost = "", 
							//qtyor = "", fieldDesc = "",receiveqty = "", 
							remarks = "", custCode = "", custName = "", jobNum = "",
							personIncharge = "", contactNum = "", ordertype = "", orderstatus = "", deliveryDate = "",
							address = "", address2 = "", address3 = "", collectionDate = "", collectionTime = "",
							remark1 = "", remark2 = "", currencyid = "", inbound_Gst = "", receivingQty = "",
							shippingcustomer = "", shippingid = "", orderdiscount = "", shippingcost = "", localexpenses = "",
							incoterms = "", taxby = "", prodgst = "",paymenttype="",PRODUCTDELIVERYDATE="",sTAXTREATMENT="",sPURCHASE_LOC="",sREVERSECHARGE="",sGOODSIMPORT="";
//					DateUtils dateUtils = new DateUtils();
					MovHisDAO movHisDao = new MovHisDAO();
//					Boolean allChecked = false, fullIssue = false;
					Map checkedPO = new HashMap();
					boolean flag = false;

					HttpSession session = request.getSession();
					ArrayList aldet = new ArrayList();

					copytype = StrUtils.fString(request.getParameter("copytype")).trim();
					if (copytype.equalsIgnoreCase("IBTOIB")) {
						oldpono = StrUtils.fString(request.getParameter("OLDPONO")).trim();
					} else {
						oldpono = StrUtils.fString(request.getParameter("DONO")).trim();
					}

					if (oldpono.length() > 0) {

						pono = StrUtils.fString(request.getParameter("PONO"));
						String[] chkdPONo = request.getParameterValues("chkdPONO");

						custCode = StrUtils.fString(request.getParameter("CUST_CODE1")).trim();
						custName = StrUtils.replaceCharacters2Recv(StrUtils.fString(request.getParameter("CUST_NAME")))
								.trim();
						jobNum = StrUtils.fString(request.getParameter("JOB_NUM")).trim();
						personIncharge = StrUtils.fString(request.getParameter("PERSON_INCHARGE")).trim();
						contactNum = StrUtils.fString(request.getParameter("CONTACT_NUM")).trim();
						address = StrUtils.fString(request.getParameter("ADD1")).trim();
						address2 = StrUtils.fString(request.getParameter("ADD2")).trim();
						address3 = StrUtils.fString(request.getParameter("ADD3")).trim();
						collectionDate = new DateUtils()
								.parseDate(StrUtils.fString(request.getParameter("ORDDATE")).trim());
						deliveryDate =StrUtils.fString(request.getParameter("DELDATE")).trim();
						collectionTime = StrUtils.fString(request.getParameter("COLLECTION_TIME")).trim();
						remark1 = StrUtils.fString(request.getParameter("REMARK1")).trim();
						remark2 = StrUtils.fString(request.getParameter("REMARK3")).trim();
						remarks = StrUtils.fString(request.getParameter("REMARK2")).trim();
						inbound_Gst = StrUtils.fString(request.getParameter("GST"));
						currencyid = StrUtils.fString(request.getParameter("DISPLAY"));
						ordertype = StrUtils.fString(request.getParameter("ORDERTYPE"));
						orderstatus = StrUtils.fString(request.getParameter("STATUS_ID"));
						orderdiscount = StrUtils.fString(request.getParameter("ORDERDISCOUNT")).trim();
						shippingcost = StrUtils.fString(request.getParameter("SHIPPINGCOST")).trim();
						incoterms = StrUtils.fString(request.getParameter("INCOTERMS")).trim();
						shippingcustomer = StrUtils.fString(request.getParameter("SHIPPINGCUSTOMER")).trim();
						shippingid = StrUtils.fString(request.getParameter("SHIPPINGID")).trim();
						localexpenses = StrUtils.fString(request.getParameter("LOCALEXPENSES")).trim();
						paymenttype = StrUtils.fString(request.getParameter("PAYMENTTYPE")).trim();
						String deliverydateformat = StrUtils.fString((request.getParameter("DELIVERYDATEFORMAT") != null) ? "1": "0").trim();
						sTAXTREATMENT=StrUtils.fString(request.getParameter("TAXTREATMENT")).trim();
						sPURCHASE_LOC=StrUtils.fString(request.getParameter("PURCHASE_LOC")).trim();
						sREVERSECHARGE=StrUtils.fString((request.getParameter("REVERSECHARGE") != null) ? "1": "0").trim();
						sGOODSIMPORT=StrUtils.fString((request.getParameter("GOODSIMPORT") != null) ? "1": "0").trim();
						Hashtable ht = new Hashtable();
						ht.clear();

						ht.put(IDBConstants.PLANT, plant);
						ht.put(IDBConstants.POHDR_PONUM, pono);
						ht.put(IDBConstants.POHDR_CUST_CODE, custCode);
						ht.put(IDBConstants.POHDR_CUST_NAME, StrUtils.InsertQuotes(custName));
						ht.put(IDBConstants.POHDR_JOB_NUM, jobNum);
						ht.put(IDBConstants.POHDR_PERSON_INCHARGE, StrUtils.InsertQuotes(personIncharge));
						ht.put(IDBConstants.POHDR_CONTACT_NUM, contactNum);
						ht.put(IDBConstants.POHDR_ADDRESS, address);
						ht.put(IDBConstants.POHDR_ADDRESS2, address2);
						ht.put(IDBConstants.POHDR_ADDRESS3, address3);
						ht.put(IDBConstants.CREATED_BY, userName);
						ht.put(IDBConstants.POHDR_COL_DATE, collectionDate);
						ht.put(IDBConstants.POHDR_COL_TIME, collectionTime);
						ht.put(IDBConstants.POHDR_REMARK1, StrUtils.InsertQuotes(remark1));
						ht.put(IDBConstants.POHDR_REMARK3, StrUtils.InsertQuotes(remark2));
						ht.put(IDBConstants.POHDR_REMARK2, StrUtils.InsertQuotes(remarks));
						ht.put(IDBConstants.STATUS, "N");
						ht.put(IDBConstants.ORDERTYPE, ordertype);
						ht.put(IDBConstants.CURRENCYID, currencyid);
						ht.put(IDBConstants.POHDR_DEL_DATE, deliveryDate);
						ht.put(IDBConstants.POHDR_GST, inbound_Gst);
						ht.put(IDBConstants.ORDSTATUSID, orderstatus);
						ht.put(IDBConstants.SHIPPINGCUSTOMER, shippingcustomer);
						ht.put(IDBConstants.SHIPPINGID, shippingid);
						ht.put(IDBConstants.ORDERDISCOUNT, orderdiscount);
						ht.put(IDBConstants.SHIPPINGCOST, shippingcost);
						ht.put(IDBConstants.INCOTERMS, incoterms);
						ht.put(IDBConstants.LOCALEXPENSES, localexpenses);
						ht.put(IDBConstants.PAYMENTTYPE, paymenttype);
						ht.put(IDBConstants.POHDR_DELIVERYDATEFORMAT, deliverydateformat);
						ht.put(IDBConstants.TAXTREATMENT, sTAXTREATMENT);
						ht.put(IDBConstants.PURCHASE_LOCATION, sPURCHASE_LOC);
						ht.put(IDBConstants.REVERSECHARGE, sREVERSECHARGE);
						ht.put(IDBConstants.GOODSIMPORT, sGOODSIMPORT);
						Boolean isCustomerAvailable = Boolean.valueOf(true);
						CustUtil custUtils = new CustUtil();
						custUtils.setmLogger(mLogger);

						isCustomerAvailable = custUtils.isExistVendorName(StrUtils.InsertQuotes(custName), plant);
						if (isCustomerAvailable) {

							Boolean isOrderTypeValied = Boolean.valueOf(true);
							if (!ordertype.equals("")) {
								OrderTypeBeanDAO orderTypeBeanDAO = new OrderTypeBeanDAO();
								isOrderTypeValied = orderTypeBeanDAO.isOrderTypeExists(ordertype, plant);
							}
							if (isOrderTypeValied) {
								flag = _POUtil.savePoHdrDetails(ht);

							}

							if (flag) {

								Hashtable htmovHis = new Hashtable();
								htmovHis.clear();
								htmovHis.put(IDBConstants.PLANT, plant);
								htmovHis.put("DIRTYPE", TransactionConstants.CONVERT_INBOUND);
								htmovHis.put(IDBConstants.CUSTOMER_CODE, custCode);
								htmovHis.put(IDBConstants.POHDR_JOB_NUM, jobNum);
								htmovHis.put(IDBConstants.MOVHIS_ORDNUM, oldpono);
								htmovHis.put(IDBConstants.CREATED_BY, userName);
								htmovHis.put("MOVTID", "");
								htmovHis.put("RECID", "");
								if (!remark1.equals("")) {
									htmovHis.put(IDBConstants.REMARKS, StrUtils.InsertQuotes(custName) + "," + pono
											+ "," + StrUtils.InsertQuotes(remark1));
								} else {
									htmovHis.put(IDBConstants.REMARKS, StrUtils.InsertQuotes(custName) + "," + pono);
								}

								htmovHis.put(IDBConstants.TRAN_DATE,
										DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));
								htmovHis.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());

								flag = movHisDao.insertIntoMovHis(htmovHis);

								Hashtable htRecvHis = new Hashtable();
								htRecvHis.clear();
								htRecvHis.put(IDBConstants.PLANT, plant);
								htRecvHis.put("DIRTYPE", TransactionConstants.CREATE_IB);
								htRecvHis.put(IDBConstants.CUSTOMER_CODE, custCode);
								htRecvHis.put(IDBConstants.POHDR_JOB_NUM, jobNum);
								htRecvHis.put(IDBConstants.MOVHIS_ORDNUM, pono);
								htRecvHis.put(IDBConstants.CREATED_BY, userName);
								htRecvHis.put("MOVTID", "");
								htRecvHis.put("RECID", "");
								if (!remark1.equals("")) {
									htRecvHis.put(IDBConstants.REMARKS,
											StrUtils.InsertQuotes(custName) + "," + StrUtils.InsertQuotes(remark1));
								} else {
									htRecvHis.put(IDBConstants.REMARKS, StrUtils.InsertQuotes(custName));
								}

								htRecvHis.put(IDBConstants.TRAN_DATE,
										DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));
								htRecvHis.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());

								flag = movHisDao.insertIntoMovHis(htRecvHis);

								new TblControlUtil().updateTblControlSeqNo(plant, IConstants.INBOUND, "P", pono);

							}
						

//						if (request.getParameter("select") != null) {
//							allChecked = true;
//						}
//						if (request.getParameter("fullIssue") != null) {
//							fullIssue = true;
//						}
						if (chkdPONo != null) {
							for (int i = 0; i < chkdPONo.length; i++) {
								polno = chkdPONo[i];
								receivingQty = StrUtils.fString(request.getParameter("receivingQty_" + polno));
								checkedPO.put(polno, receivingQty);
							}
							session.setAttribute("checkedPONO", checkedPO);
						}

						process: if (chkdPONo != null) {
							for (int i = 0; i < chkdPONo.length; i++) {
								polno = chkdPONo[i];
								receivingQty = StrUtils
										.formatNum(StrUtils.fString(request.getParameter("receivingQty_" + polno)));

								if (copytype.equalsIgnoreCase("IBTOIB")) {
									Hashtable htCond = new Hashtable();
									htCond.put("PLANT", plant);
									htCond.put("PONO", oldpono);
									htCond.put("POLNNO", polno);

									q = "item,isnull(itemdesc,'') itemdesc,isnull(unitmo,'') uom,isnull(PRODUCTDELIVERYDATE,'') PRODUCTDELIVERYDATE,isnull(unitcost,0) unitcost,isnull(qtyor,0) as qtyor,isnull(comment1,'') as prdRemarks ";
									aldet = _poDetDAO.selectPoDet(q, htCond, " plant <> '' order by polnno");
								} else {
									Hashtable htCond = new Hashtable();
									htCond.put("PLANT", plant);
									htCond.put("DONO", oldpono);
									htCond.put("DOLNNO", polno);

									q = "item,isnull(itemdesc,'') itemdesc,isnull(unitmo,'') uom,isnull(PRODUCTDELIVERYDATE,'') PRODUCTDELIVERYDATE,isnull(qtyor,0) as qtyor,isnull(comment1,'') as prdRemarks ";
									aldet = _DoDetDAO.selectDoDet(q, htCond, " plant <> '' order by dolnno");

								}
								if (aldet.size() > 0) {
									Map m = (Map) aldet.get(0);
									item = (String) m.get("item");
									itemDesc = (String) m.get("itemdesc");
									prdRemarks = (String) m.get("prdRemarks");
									itemuom = (String) m.get("uom");
									PRODUCTDELIVERYDATE = (String) m.get("PRODUCTDELIVERYDATE");
									
									if (copytype.equalsIgnoreCase("IBTOIB")) {
										cost = (String) m.get("unitcost");
									} else {
										//cost = _ItemMstDAO.getItemCost(plant, item);
										cost = _ItemMstDAO.getLocalCurrencyCost(plant, currencyid,item,itemuom);
									}
//									qtyor = StrUtils.formatNum((String) m.get("qtyor"));

								}

								Map mPrddet = new ItemMstDAO().getProductNonStockDetails(plant, item);
								String nonstocktype = StrUtils.fString((String) mPrddet.get("NONSTKFLAG"));
								String nonstocktypeDesc = StrUtils.fString((String) mPrddet.get("STOCKTYPEDESC"));

								Hashtable htPodet = new Hashtable();

								htPodet.put(IDBConstants.PLANT, plant);
								htPodet.put(IDBConstants.PODET_PONUM, pono);

								htPodet.put(IDBConstants.PODET_ITEM, item);
								htPodet.put("ITEMDESC", StrUtils.InsertQuotes(itemDesc));
								htPodet.put("UNITMO", itemuom);
								htPodet.put(IDBConstants.PODET_ITEM_DESC, StrUtils.InsertQuotes(itemDesc));

								htPodet.put(IDBConstants.PODET_JOB_NUM, jobNum);
								htPodet.put(IDBConstants.PODET_CUST_NAME, StrUtils.InsertQuotes(custName));
								htPodet.put(IDBConstants.PODET_QTYOR, receivingQty);
								htPodet.put(IDBConstants.PODET_QTYRC, "0");
								htPodet.put(IDBConstants.PODET_UNITCOST, cost);
								htPodet.put(IDBConstants.PODET_COMMENT1, StrUtils.InsertQuotes(prdRemarks));
								htPodet.put(IDBConstants.PODET_POLNNO, Integer.toString(i + 1));

								String CURRENCYUSEQT = new POUtil().getCurrencyUseQT(plant, pono);
								htPodet.put(IDBConstants.CURRENCYUSEQT, CURRENCYUSEQT);

								if (nonstocktype.equals("Y") && !(copytype.equalsIgnoreCase("IBTOIB"))) {

									if (nonstocktypeDesc.equalsIgnoreCase("discount")
											|| nonstocktypeDesc.equalsIgnoreCase("Credit/Refund")) {
										htPodet.put(IDBConstants.PODET_UNITCOST, "-" + cost);

									}

								}

								htPodet.put(IDBConstants.PODET_LNSTATUS, "N");
								htPodet.put(IDBConstants.MANUFACTURER, " ");
								htPodet.put(IDBConstants.PODET_PRODUCTDELIVERYDATE, PRODUCTDELIVERYDATE);
								
//								java.util.Date dt = new java.util.Date();
//								SimpleDateFormat dfVisualDate = new SimpleDateFormat("dd/MM/yyyy");
//								String today = dfVisualDate.format(dt);

								htPodet.put(IDBConstants.TRAN_DATE, DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));
								/* to get product gst */
								taxby = _PlantMstDAO.getTaxBy(plant);
								if (taxby.equalsIgnoreCase("BYPRODUCT")) {
									prodgst = _ItemMstDAO.getProductGst(plant, item);
									htPodet.put(IDBConstants.PRODGST, prodgst);
								}
								/* to get product gst end */

								Boolean isProductAvailable = Boolean.valueOf(false);
								ItemUtil itemUtil = new ItemUtil();
								itemUtil.setmLogger(mLogger);
//								Hashtable htRecvDet = new Hashtable();
								isProductAvailable = itemUtil.isExistsItemMst(item, plant);
								if (flag) {
									if (isProductAvailable) {

										flag = _POUtil.savePoDetDetails(htPodet);

										if (flag) {
											//	Call Accounting module for new order : Start
											/*if ("yes".equals(request.getSession(false).getAttribute("isAccountingEnabled"))) {
												try {
													Hashtable htAcct = new Hashtable();
													htAcct.put(IDBConstants.PLANT, plant);
													if (_MasterDAO.isExisitAccount(htAcct, " ACCOUNT_TITLE='Suppliers'")) {
														//	Get Acct. Id
														String Acctid=getNextIdDetails(plant,userName);
														if(Acctid!="")
														{
															if (!_MasterDAO.isExisitAccount(htAcct, " VENDOR_NO='"+custCode+"' AND ACCOUNT_TYPE=2 AND PARENT_ACCOUNT_NO=13  ")) {
															flag = _MasterDAO.InsertAccount(htAcct,Acctid,custName,custCode,"",userName,2,13);//insert Supplier Account
															if (flag) {
															if(Acctid!="A0001")// Update Account Id
												      		  {	
												    			boolean exitFlag = false;
												    			boolean  updateFlag= false;
												    			Hashtable htv = new Hashtable();				
												    			htv.put(IDBConstants.PLANT, plant);
												    			htv.put(IDBConstants.TBL_FUNCTION, "ACCOUNT");
												    			exitFlag = _TblControlDAO.isExisit(htv, "", plant);
												    			if (exitFlag) 
												    			  updateFlag=_TblControlDAO.updateSeqNo("ACCOUNT",plant);
												    			else
												    			{
												    				boolean insertFlag = false;
												    				Map htInsert=null;
												                	Hashtable htTblCntInsert  = new Hashtable();           
												                	htTblCntInsert.put(IDBConstants.PLANT,plant);          
												                	htTblCntInsert.put(IDBConstants.TBL_FUNCTION,"ACCOUNT");
												                	htTblCntInsert.put(IDBConstants.TBL_PREFIX1,"A");
												                 	htTblCntInsert.put("MINSEQ","0000");
												                 	htTblCntInsert.put("MAXSEQ","9999");
												                	htTblCntInsert.put(IDBConstants.TBL_NEXT_SEQ, (String)IDBConstants.TBL_FIRST_NEX_SEQ);
												                	htTblCntInsert.put(IDBConstants.CREATED_BY, userName);
												                	htTblCntInsert.put(IDBConstants.CREATED_AT, (String)DateUtils.getDateTime());
												                	insertFlag= _TblControlDAO.insertTblControl(htTblCntInsert,plant);
												    			}
												    		}
															}
															}
															if (flag) {
													Form form = new Form();
													form.param("data", "{\"pono\":\"" + pono + "\",\"xndate\":\"" + collectionDate + "\",\"supplier\":\"" + custCode + "\",\"amount\":\"0\",\"transactionType\":\"4\",\"plant\":\"" + plant + "\",\"user\":\"" + userName + "\",\"currency\":\"" + currencyid + "\",\"action\":\"copytoinbound\"}");							
													String finTransactionResponse = HttpUtils.connect(HttpUtils.getHostInfo(request) + IConstants.ACCOUNTING_APPLICATION_URL.replace("trackaccounting", plant) + "restFinTransaction/create", form);
													if (finTransactionResponse == null || finTransactionResponse.startsWith("Error : ")) {
														throw new Exception(finTransactionResponse);
													}
															}
														}
													}
												}catch(Exception e) {
													throw new Exception(ThrowableUtil.getExceptionMessage(e), e);
												}												
											}*/
											//	Call Accounting module for new order : End
											// insert dodet multi remarks
											ArrayList almultiremarks = new ArrayList();
											String productPORemarks = "";
											if (copytype.equalsIgnoreCase("IBTOIB")) {
												almultiremarks = _POUtil.listPoMultiRemarks(plant, oldpono, polno);
											} else {
												almultiremarks = _dOUtil.listDoMultiRemarks(plant, oldpono, polno);
											}
											for (int index = 0; index < almultiremarks.size(); index++) {

												Map mulremarks = (Map) almultiremarks.get(index);

												productPORemarks = (String) mulremarks.get("remarks");

												Hashtable htRemarks = new Hashtable();
												htRemarks.put(IDBConstants.PLANT, plant);
												htRemarks.put(IDBConstants.PODET_PONUM, pono);
												htRemarks.put(IDBConstants.PODET_POLNNO, Integer.toString(i + 1));
												htRemarks.put(IDBConstants.PODET_ITEM, item);
												htRemarks.put(IDBConstants.REMARKS,
														StrUtils.InsertQuotes(productPORemarks));
												htRemarks.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
												htRemarks.put(IDBConstants.CREATED_BY, userName);

												flag = _POUtil.savePoMultiRemarks(htRemarks);
											}

										}
									}

									if (flag) {

										Hashtable htMovhis = new Hashtable();
										htMovhis.clear();
										htMovhis.put(IDBConstants.PLANT, plant);
										htMovhis.put("DIRTYPE", TransactionConstants.IB_ADD_ITEM);
										htMovhis.put(IDBConstants.CUSTOMER_CODE, custCode);
										htMovhis.put(IDBConstants.POHDR_JOB_NUM, jobNum);
										htMovhis.put(IDBConstants.ITEM, item);
										htMovhis.put(IDBConstants.QTY, receivingQty);
										htMovhis.put(IDBConstants.MOVHIS_ORDNUM, pono);
										htMovhis.put(IDBConstants.CREATED_BY, userName);
										htMovhis.put("MOVTID", "");
										htMovhis.put("RECID", "");
										htMovhis.put(IDBConstants.TRAN_DATE,
												DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));
										htMovhis.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());

										flag = movHisDao.insertIntoMovHis(htMovhis);

									}

									if (!flag)
										break process;
								}
							}
						}

						if (flag) {
							if (copytype.equalsIgnoreCase("IBTOIB")) {
								result = "<font color=\"green\"> Converted To Purchase Order Successfully!  <br><br><center>"
										+ "<input type=\"button\" value=\"Back\" onClick=\"window.location.href='/track/purchaseorderservlet?PONO="
										+ oldpono + "&Submit=View&RFLAG=4'\"> ";
							} else {
								result = "<font color=\"green\"> Converted To Purchase Order Successfully!  <br><br><center>"
										+ "<input type=\"button\" value=\"Back\" onClick=\"window.location.href='/track/deleveryorderservlet?DONO="
										+ oldpono + "&Submit=View&RFLAG=4'\"> ";
							}

						} else {
							result = "<font color=\"red\"> Error in Converting To Purchase Order  - Please Check the Data  <br><br><center>"
									+ "<input type=\"button\" value=\"Back\" onClick=\"window.location.href='javascript:history.back()'\"> ";

						}
						}
						else {
							result = "<font color=\"red\"> Error in Purchase Order  - Invalid Supplier <br><br><center>"
									+ "<input type=\"button\" value=\"Back\" onClick=\"window.location.href='javascript:history.back()'\"> ";
							result = "<br><h3>" + result;
						}


					} else {

						result = "<font color=\"red\"> Please select Order Number first before converting to Purchase Order. <br><br><center>"
								+ "<input type=\"button\" value=\"Back\" onClick=\"window.location.href='javascript:history.back()'\"> ";

					}

					request.getSession().setAttribute("RESULT", result);
					response.sendRedirect("jsp/displayResult2User.jsp");

				} catch (Exception ex) {
					this.mLogger.exception(this.printLog, "", ex);

					String result = "<font class = " + IConstants.FAILED_COLOR + ">Error : " + ex.getMessage()
							+ "</font>";
					result = result + " <br><br><center> "
							+ "<input type=\"button\" value=\"Back\" onClick=\"window.location.href='javascript:history.back()'\"> ";
					result = "<br><h3>" + result;
					request.getSession().setAttribute("RESULT", result);
					response.sendRedirect("jsp/displayResult2User.jsp");
				}

			} else if (action.equalsIgnoreCase("getRemarks")) {
				String pono = StrUtils.fString(request.getParameter("PONO"));
				String item = StrUtils.fString(request.getParameter("ITEM"));
				String polnno = StrUtils.fString(request.getParameter("POLNNO"));
				List al = new ArrayList();

				if (pono.length() > 0) {

					String query = "REMARKS,POLNNO";
					Hashtable ht = new Hashtable();
					ht.put(IDBConstants.PODET_PONUM, pono);
					ht.put(IDBConstants.PODET_ITEM, item);
					ht.put(IDBConstants.PODET_POLNNO, polnno);
					ht.put(IDBConstants.PLANT, plant);
					al = _poDetDAO.selectRemarks(query, ht);
					JSONObject json = new JSONObject();
					json.put("remarksList", al);
					response.getWriter().print(json.toString());
				} else {
					JSONObject json = new JSONObject();
					json.put("error", "No data found");
					response.getWriter().print(json.toString());
				}
			}
			else if (action.equalsIgnoreCase("GET_ORDER_NO_FOR_AUTO_SUGGESTION")) {
				JSONObject jsonObjectResult = new JSONObject();
				jsonObjectResult = this.getOrderNoForAutoSuggestion(request);
				response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                response.getWriter().write(jsonObjectResult.toString());
                response.getWriter().flush();
                response.getWriter().close();
			} 
			//Resvi Add For MultiPurchase Order Number
			else if (action.equalsIgnoreCase("GET_MULTI_PURCHASEEST_ORDER_NO_FOR_AUTO_SUGGESTION")) {
				JSONObject jsonObjectResult = new JSONObject();
				jsonObjectResult = this.getMultiPurchaseEstOrderNoForAutoSuggestion(request);
				response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                response.getWriter().write(jsonObjectResult.toString());
                response.getWriter().flush();
                response.getWriter().close();
			} 
          //	Ends
			//Resvi Add For Purchase Order Estimate Number
			else if (action.equalsIgnoreCase("GET_PURCHASEEST_ORDER_NO_FOR_AUTO_SUGGESTION")) {
				JSONObject jsonObjectResult = new JSONObject();
				jsonObjectResult = this.getPurchaseEstOrderNoForAutoSuggestion(request);
				response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                response.getWriter().write(jsonObjectResult.toString());
                response.getWriter().flush();
                response.getWriter().close();
			} 
//			Ends
			
			else if (action.equalsIgnoreCase("GET_ORDER_NO_FOR_FORCE_CLOSE")) {
				JSONObject jsonObjectResult = new JSONObject();
				jsonObjectResult = this.getOrderNoForForceClose(request);
				response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                response.getWriter().write(jsonObjectResult.toString());
                response.getWriter().flush();
                response.getWriter().close();
			} 
			else if (action.equalsIgnoreCase("GET_ORDER_TYPE_FOR_AUTO_SUGGESTION")) {
				JSONObject jsonObjectResult = new JSONObject();
				jsonObjectResult = this.getOrderTypeForAutoSuggestion(request);
				response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                response.getWriter().write(jsonObjectResult.toString());
                response.getWriter().flush();
                response.getWriter().close();
			} 
			else if (action.equalsIgnoreCase("GET_ORDER_DETAILS_FOR_BILLING")) {
				JSONObject jsonObjectResult = new JSONObject();
				jsonObjectResult = this.getOrderDetailsForBilling(request);
				response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                response.getWriter().write(jsonObjectResult.toString());
                response.getWriter().flush();
                response.getWriter().close();
			} else if (action.equalsIgnoreCase("GET_BILLING_DETAILS_BY_GRNO")) {
				JSONObject jsonObjectResult = new JSONObject();
				jsonObjectResult = this.getBillingDetailsByGRNO(request);
				response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                response.getWriter().write(jsonObjectResult.toString());
                response.getWriter().flush();
                response.getWriter().close();
			} else if (action.equalsIgnoreCase("GET_GRNO_FOR_AUTO_SUGGESTION")) {
				JSONObject jsonObjectResult = new JSONObject();
				jsonObjectResult = this.getGRNOForAutoSuggestion(request);
				response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                response.getWriter().write(jsonObjectResult.toString());
                response.getWriter().flush();
                response.getWriter().close();
			} else if (action.equalsIgnoreCase("GET_PREVIOUS_ORDER_DETAILS")) {
				JSONObject jsonObjectResult = new JSONObject();
				jsonObjectResult = this.getPreviousOrderDetails(request);
				response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                response.getWriter().write(jsonObjectResult.toString());
                response.getWriter().flush();
                response.getWriter().close();
			}else if (action.equalsIgnoreCase("GET_PREVIOUS_ESTIMATE_ORDER_DETAILS")) {
				JSONObject jsonObjectResult = new JSONObject();
				jsonObjectResult = this.getPreviousEstimateOrderDetails(request);
				response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                response.getWriter().write(jsonObjectResult.toString());
                response.getWriter().flush();
                response.getWriter().close();
			}else if (action.equalsIgnoreCase("GET_ORDER_NO_FOR_ORDER_RECEIPT_AUTOSUGGESTION")) {
				JSONObject jsonObjectResult = new JSONObject();
				jsonObjectResult = this.getOrderNoForOrderReceiptAutoSuggestion(request);
				response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                response.getWriter().write(jsonObjectResult.toString());
                response.getWriter().flush();
                response.getWriter().close();
			}
		} catch (Exception ex) {

			this.mLogger.exception(this.printLog, "", ex);
			String result = "<font class = " + IConstants.FAILED_COLOR + ">Error : " + ex.getMessage() + "</font>";
			result = result + " <br><br><center> "
					+ "<input type=\"button\" value=\"Back\" onClick=\"window.location.href='javascript:history.back()'\"> ";

			result = "<br><h3>" + result;
			request.getSession().setAttribute("RESULT", result);
			response.sendRedirect("jsp/displayResult2User.jsp");

		}

	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

//	private String addLocation(HttpServletRequest request, HttpServletResponse response)
//			throws ServletException, IOException, Exception {
//		String result = "";
//		try {
//			String locId = request.getParameter("LOC_ID").trim();
//			String locDesc = request.getParameter("LOC_DESC").trim();
//			String remarks = request.getParameter("REMARKS").trim();
//
//			Hashtable ht = new Hashtable();
//			ht.put("PLANT", "SIS");
//			ht.put("WHID", "");
//			ht.put("LOC", locId);
//			ht.put("LOCDESC", locDesc);
//			ht.put("USERFLD1", remarks);
//			boolean flag = false;
//
//			if (flag) {
//				request.getSession().setAttribute("locMstData", ht);
//				result = "<font class = " + IConstants.SUCCESS_COLOR + ">Location Added Successfully</font>";
//
//			} else {
//				throw new Exception("Unable to add location ");
//			}
//
//		} catch (Exception e) {
//			this.mLogger.exception(this.printLog, "", e);
//			throw e;
//		}
//
//		return result;
//	}

//	private String DeletePo(HttpServletRequest request, HttpServletResponse response)
//			throws ServletException, IOException, Exception {
//
//		String result = "", pono = "";
//		Hashtable htCondition = new Hashtable();
//		try {
//			pono = request.getParameter("PONO").trim();
//			Hashtable ht = new Hashtable();
//			htCondition.put("LOC", pono);
//			htCondition.put("PLANT", "SIS");
//			boolean flag = false;
//			if (flag) {
//				ht = new Hashtable();
//				ht.put("PLANT", "SIS");
//				ht.put("WHID", "");
//				ht.put("LOC", "");
//				ht.put("LOCDESC", "");
//				ht.put("USERFLD1", "");
//				request.getSession().setAttribute("locMstData", ht);
//				result = "<font class = " + IConstants.SUCCESS_COLOR + ">Location Deleted Successfully</font>&"
//						+ "action = show_result";
//
//			} else {
//				throw new Exception("Unable to delete location ");
//			}
//
//		} catch (Exception e) {
//			this.mLogger.exception(this.printLog, "", e);
//			throw e;
//		}
//		return result;
//	}

	private String SavePoHdr(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, Exception {
		MovHisDAO movHisDao = new MovHisDAO();
		CurrencyUtil curUtil = new CurrencyUtil();
		movHisDao.setmLogger(mLogger);
		// CurrencyUtil.setmLogger(mLogger);
//		DateUtils dateUtils = new DateUtils();
		boolean flag = false;//, isAutoGenerate = false;
		Hashtable ht = new Hashtable();
		String result = "", pono = "", custCode = "", currencyid = "", display = "", custName = "", ordertype = "",
				jobNum = "", personIncharge = "", plant = "", user_id = "", contactNum = "", address = "",
				address2 = "", address3 = "", collectionDate = "", collectionTime = "", remark1 = "", remark2 = "",
				remark3 = "", inbound_Gst = "", deldate = "", orderstatus = "", shippingcustomer = "", shippingid = "",
				orderdiscount = "", shippingcost = "", incoterms = "", processPono = "", 
				localexpenses = "",paymenttype="",DATEFORMAT="",empName="",empno="",sTAXTREATMENT="",sPURCHASE_LOC="",sREVERSECHARGE="",sGOODSIMPORT="";//zip = "", email = "", add4 = "", country = "",
		//telno = "", 
		try {

			HttpSession session = request.getSession();
			plant = StrUtils.fString((String) session.getAttribute("PLANT")).trim();
			pono = StrUtils.fString(request.getParameter("PONO")).trim();
			user_id = StrUtils.fString(request.getParameter("LOGIN_USER")).trim();

//			country = StrUtils.fString(request.getParameter("COUNTRY"));
//			zip = StrUtils.fString(request.getParameter("ZIP"));
//			add4 = StrUtils.fString(request.getParameter("ADD4"));
//			email = StrUtils.fString(request.getParameter("EMAIL"));
//			telno = StrUtils.fString(request.getParameter("TELNO"));
			custName = StrUtils.fString(request.getParameter("CUST_NAME")).trim();
			custCode = StrUtils.fString(request.getParameter("CUST_CODE1")).trim();
			jobNum = StrUtils.fString(request.getParameter("JOB_NUM")).trim();
			personIncharge = StrUtils.fString(request.getParameter("PERSON_INCHARGE")).trim();
			contactNum = StrUtils.fString(request.getParameter("CONTACT_NUM")).trim();
			address = StrUtils.fString(request.getParameter("ADD1")).trim();
			address2 = StrUtils.fString(request.getParameter("ADD2")).trim();
			address3 = StrUtils.fString(request.getParameter("ADD3")).trim();
			collectionDate = new DateUtils().parseDate(StrUtils.fString(request.getParameter("ORDDATE")).trim());
			if (collectionDate.length() == 0) {
				collectionDate = DateUtils.getDate();
			}
			collectionTime = StrUtils.fString(request.getParameter("COLLECTION_TIME")).trim();
			remark1 = StrUtils.fString(request.getParameter("REMARK1")).trim();
			remark2 = StrUtils.fString(request.getParameter("REMARK2")).trim();
			remark3 = StrUtils.fString(request.getParameter("REMARK3")).trim();
			ordertype = StrUtils.fString(request.getParameter("ORDERTYPE")).trim();
			inbound_Gst = StrUtils.fString(request.getParameter("GST")).trim();
			deldate = StrUtils.fString(request.getParameter("DELDATE")).trim();
			//deldate = DateUtils.parseDate(StrUtils.fString(request.getParameter("DELDATE")).trim());
//			isAutoGenerate = Boolean.valueOf(StrUtils.fString(request.getParameter("ISAUTOGENERATE")).trim());
			display = StrUtils.fString(request.getParameter("DISPLAY")).trim();
			orderstatus = StrUtils.fString(request.getParameter("STATUS_ID")).trim();
			shippingcustomer = StrUtils.fString(request.getParameter("SHIPPINGCUSTOMER")).trim();
			shippingid = StrUtils.fString(request.getParameter("SHIPPINGID")).trim();
			orderdiscount = StrUtils.fString(request.getParameter("ORDERDISCOUNT")).trim();
			shippingcost = StrUtils.fString(request.getParameter("SHIPPINGCOST")).trim();
			incoterms = StrUtils.fString(request.getParameter("INCOTERMS")).trim();
			empName = StrUtils.fString(request.getParameter("EMP_NAME")).trim();
			empno = StrUtils.fString(request.getParameter("EMPNO")).trim();
			localexpenses = StrUtils.fString(request.getParameter("LOCALEXPENSES")).trim();
			paymenttype = StrUtils.fString(request.getParameter("PAYMENTTYPE")).trim();
			DATEFORMAT = StrUtils.fString((request.getParameter("DATEFORMAT") != null) ? "1": "0").trim();
			sTAXTREATMENT=StrUtils.fString(request.getParameter("TAXTREATMENT")).trim();
			sPURCHASE_LOC=StrUtils.fString(request.getParameter("PURCHASE_LOC")).trim();
			sREVERSECHARGE=StrUtils.fString((request.getParameter("REVERSECHARGE") != null) ? "1": "0").trim();
			sGOODSIMPORT=StrUtils.fString((request.getParameter("GOODSIMPORT") != null) ? "1": "0").trim();
			Hashtable curHash = new Hashtable();
			curHash.put(IConstants.PLANT, plant);
			curHash.put(IConstants.DISPLAY, display);
			if (display != null && display != "") {
				currencyid = curUtil.getCurrencyID(curHash, "CURRENCYID");
			}

			if (inbound_Gst.length() == 0) {
				inbound_Gst = "0";
			}
			if(!empName.isEmpty())
			{
				if(empno.isEmpty())
				{
				ArrayList arrList = new ArrayList();
				EmployeeDAO movHisDAO= new EmployeeDAO();
				Hashtable htData =new Hashtable();
				htData.put("PLANT", plant);
				htData.put("FNAME", empName);
				arrList = movHisDAO.getEmployeeDetails("EMPNO", htData,"");
				if(!arrList.isEmpty()) {
				Map m = (Map) arrList.get(0);
				empno = (String) m.get("EMPNO");
				}
				}
			}
			ht.put(IDBConstants.PLANT, plant);
			ht.put(IDBConstants.POHDR_PONUM, pono);
			ht.put(IDBConstants.POHDR_CUST_CODE, custCode);
			ht.put(IDBConstants.POHDR_CUST_NAME, StrUtils.InsertQuotes(custName));
			ht.put(IDBConstants.POHDR_JOB_NUM, jobNum);
			ht.put(IDBConstants.POHDR_PERSON_INCHARGE, StrUtils.InsertQuotes(personIncharge));
			ht.put(IDBConstants.POHDR_CONTACT_NUM, contactNum);
			ht.put(IDBConstants.POHDR_ADDRESS, address);
			ht.put(IDBConstants.POHDR_ADDRESS2, address2);
			ht.put(IDBConstants.POHDR_ADDRESS3, address3);
			ht.put(IDBConstants.CREATED_BY, user_id);
			ht.put(IDBConstants.POHDR_COL_DATE, collectionDate);
			ht.put(IDBConstants.POHDR_COL_TIME, collectionTime);
			ht.put(IDBConstants.POHDR_REMARK1, StrUtils.InsertQuotes(remark1));
			ht.put(IDBConstants.POHDR_REMARK2, StrUtils.InsertQuotes(remark2)); // supplier remark
			ht.put(IDBConstants.POHDR_REMARK3, StrUtils.InsertQuotes(remark3)); // order header remark2
			ht.put(IDBConstants.ORDERTYPE, ordertype);
			ht.put(IDBConstants.POHDR_GST, inbound_Gst);
			ht.put(IDBConstants.CURRENCYID, currencyid);
			ht.put(IDBConstants.STATUS, "N");
			ht.put(IDBConstants.POHDR_DEL_DATE, deldate);
			ht.put(IDBConstants.ORDSTATUSID, orderstatus);
			ht.put(IDBConstants.SHIPPINGCUSTOMER, shippingcustomer);
			ht.put(IDBConstants.SHIPPINGID, shippingid);
			ht.put(IDBConstants.ORDERDISCOUNT, orderdiscount);
			ht.put(IDBConstants.SHIPPINGCOST, shippingcost);
			ht.put(IDBConstants.INCOTERMS, incoterms);
			ht.put(IDBConstants.LOCALEXPENSES, localexpenses);
			ht.put(IDBConstants.PAYMENTTYPE, paymenttype);
			ht.put(IDBConstants.POHDR_DELIVERYDATEFORMAT, DATEFORMAT);
			ht.put(IDBConstants.TAXTREATMENT, sTAXTREATMENT);
			ht.put(IDBConstants.PURCHASE_LOCATION, sPURCHASE_LOC);
			ht.put(IDBConstants.REVERSECHARGE, sREVERSECHARGE);
			ht.put(IDBConstants.GOODSIMPORT, sGOODSIMPORT);
			Boolean isCustomerAvailable = Boolean.valueOf(true);
			Boolean isvalidShippingCustomer = Boolean.valueOf(true);
			CustUtil custUtils = new CustUtil();
			custUtils.setmLogger(mLogger);

			isCustomerAvailable = custUtils.isExistVendorName(StrUtils.InsertQuotes(custName), plant);
			if (shippingcustomer.length() > 0) {
				isvalidShippingCustomer = _MasterDAO.isExistsShippingDetails(shippingcustomer, plant);
			}
			if (isCustomerAvailable) {
				if (isvalidShippingCustomer) {

					Boolean isOrderTypeValied = Boolean.valueOf(true);
					if (!ordertype.equals("")) {
						OrderTypeBeanDAO orderTypeBeanDAO = new OrderTypeBeanDAO();
						isOrderTypeValied = orderTypeBeanDAO.isOrderTypeExists(ordertype, plant);
					}
					if (isOrderTypeValied) {
						flag = _POUtil.savePoHdrDetails(ht);
						if (flag) {
							Hashtable htRecvHis = new Hashtable();
							htRecvHis.clear();
							htRecvHis.put(IDBConstants.PLANT, plant);
							htRecvHis.put(IDBConstants.DIRTYPE, TransactionConstants.CREATE_IB);
							htRecvHis.put(IDBConstants.CUSTOMER_CODE, custCode);
							htRecvHis.put(IDBConstants.POHDR_JOB_NUM, jobNum);
							htRecvHis.put(IDBConstants.MOVHIS_ORDNUM, pono);
							htRecvHis.put(IDBConstants.CREATED_BY, user_id);
							htRecvHis.put("MOVTID", "");
							htRecvHis.put("RECID", "");
							htRecvHis.put(IDBConstants.REMARKS,
									StrUtils.InsertQuotes(custName) + "," + StrUtils.InsertQuotes(remark1));
							if (!remark1.equals("") && !remark3.equals("")) {
								htRecvHis.put(IDBConstants.REMARKS, StrUtils.InsertQuotes(custName) + ","
										+ StrUtils.InsertQuotes(remark1) + "," + StrUtils.InsertQuotes(remark3));
							} else if (!remark1.equals("") && remark3.equals("")) {
								htRecvHis.put(IDBConstants.REMARKS,
										StrUtils.InsertQuotes(custName) + "," + StrUtils.InsertQuotes(remark1));
							} else if (remark1.equals("") && !remark3.equals("")) {
								htRecvHis.put(IDBConstants.REMARKS,
										StrUtils.InsertQuotes(custName) + "," + StrUtils.InsertQuotes(remark3));
							} else {
								htRecvHis.put(IDBConstants.REMARKS, StrUtils.InsertQuotes(custName));
							}

							htRecvHis.put(IDBConstants.TRAN_DATE, DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));
							htRecvHis.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
							flag = movHisDao.insertIntoMovHis(htRecvHis);

							Hashtable htMaster = new Hashtable();
							if (flag) {
								if (remark1.length() > 0) {
									htMaster.clear();
									htMaster.put(IDBConstants.PLANT, plant);
									htMaster.put(IDBConstants.REMARKS, remark1);									
									if (!_MasterDAO.isExisitRemarks(htMaster, "")) {
										htMaster.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
									htMaster.put(IDBConstants.CREATED_BY, user_id);
										_MasterDAO.InsertRemarks(htMaster);

										htRecvHis.clear();
										htRecvHis.put(IDBConstants.PLANT, plant);
										htRecvHis.put("DIRTYPE", "CREATE_REMARKS");
										htRecvHis.put("ORDNUM", "");
										htRecvHis.put(IDBConstants.ITEM, "");
										htRecvHis.put("BATNO", "");
										htRecvHis.put(IDBConstants.LOC, "");
										htRecvHis.put("REMARKS", remark1);
										htRecvHis.put(IDBConstants.CREATED_BY, user_id);
										htRecvHis.put(IDBConstants.TRAN_DATE,
												DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));
										htRecvHis.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
										flag = movHisDao.insertIntoMovHis(htRecvHis);
									}
								}
								if (remark3.length() > 0) {
									htMaster.clear();
									htMaster.put(IDBConstants.PLANT, plant);
									htMaster.put(IDBConstants.REMARKS, remark3);									
									if (!_MasterDAO.isExisitRemarks(htMaster, "")) {
										htMaster.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
									htMaster.put(IDBConstants.CREATED_BY, user_id);
										_MasterDAO.InsertRemarks(htMaster);

										htRecvHis.clear();
										htRecvHis.put(IDBConstants.PLANT, plant);
										htRecvHis.put("DIRTYPE", "CREATE_REMARKS");
										htRecvHis.put("ORDNUM", "");
										htRecvHis.put(IDBConstants.ITEM, "");
										htRecvHis.put("BATNO", "");
										htRecvHis.put(IDBConstants.LOC, "");
										htRecvHis.put("REMARKS", remark3);
										htRecvHis.put(IDBConstants.CREATED_BY, user_id);
										htRecvHis.put(IDBConstants.TRAN_DATE,
												DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));
										htRecvHis.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
										flag = movHisDao.insertIntoMovHis(htRecvHis);
									}
								}

								if (incoterms.length() > 0) {
									htMaster.clear();
									htMaster.put(IDBConstants.PLANT, plant);
									htMaster.put(IDBConstants.INCOTERMS, incoterms);
									
									if (!_MasterDAO.isExisitINCOTERMMST(htMaster, "")) {
										htMaster.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
									htMaster.put(IDBConstants.CREATED_BY, user_id);
										_MasterDAO.InsertINCOTERMS(htMaster);

										htRecvHis.clear();
										htRecvHis.put(IDBConstants.PLANT, plant);
										htRecvHis.put("DIRTYPE", "ADD_INCOTERMS_MST");
										htRecvHis.put("ORDNUM", "");
										htRecvHis.put(IDBConstants.ITEM, "");
										htRecvHis.put("BATNO", "");
										htRecvHis.put(IDBConstants.LOC, "");
										htRecvHis.put("REMARKS", incoterms);
										htRecvHis.put(IDBConstants.CREATED_BY, user_id);
										htRecvHis.put(IDBConstants.TRAN_DATE,
												DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));
										htRecvHis.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
										flag = movHisDao.insertIntoMovHis(htRecvHis);
									}
								}
							}
						}
						if (flag) {
							result = "<font color=\"green\"> Purchase Order Created Successfully.  <br><br><center>"
									+ "<input type=\"button\" value=\"Back\" onClick=\"window.location.href='/track/purchaseorderservlet?PONO="
									+ pono + "&Submit=View'\"> ";
							result = "<br><h3>" + result;
							new TblControlUtil().updateTblControlSeqNo(plant, IConstants.INBOUND, "P", pono);
							//	Call Accounting module for new order : Start
							/*if ("yes".equals(request.getSession(false).getAttribute("isAccountingEnabled"))) {
								try {
									Hashtable htAcct = new Hashtable();
									htAcct.put(IDBConstants.PLANT, plant);
									if (_MasterDAO.isExisitAccount(htAcct, " ACCOUNT_TITLE='Suppliers'")) {
										//	Get Acct. Id
										String Acctid=getNextIdDetails(plant,user_id);
										if(Acctid!="")
										{
											if (!_MasterDAO.isExisitAccount(htAcct, " VENDOR_NO='"+custCode+"' AND ACCOUNT_TYPE=2 AND PARENT_ACCOUNT_NO=13  ")) {
											flag = _MasterDAO.InsertAccount(htAcct,Acctid,custName,custCode,"",user_id,2,13);//insert Supplier Account
											if (flag) {
											if(Acctid!="A0001")// Update Account Id
								      		  {	
								    			boolean exitFlag = false;
								    			boolean  updateFlag= false;
								    			Hashtable htv = new Hashtable();				
								    			htv.put(IDBConstants.PLANT, plant);
								    			htv.put(IDBConstants.TBL_FUNCTION, "ACCOUNT");
								    			exitFlag = _TblControlDAO.isExisit(htv, "", plant);
								    			if (exitFlag) 
								    			  updateFlag=_TblControlDAO.updateSeqNo("ACCOUNT",plant);
								    			else
								    			{
								    				boolean insertFlag = false;
								    				Map htInsert=null;
								                	Hashtable htTblCntInsert  = new Hashtable();           
								                	htTblCntInsert.put(IDBConstants.PLANT,plant);          
								                	htTblCntInsert.put(IDBConstants.TBL_FUNCTION,"ACCOUNT");
								                	htTblCntInsert.put(IDBConstants.TBL_PREFIX1,"A");
								                 	htTblCntInsert.put("MINSEQ","0000");
								                 	htTblCntInsert.put("MAXSEQ","9999");
								                	htTblCntInsert.put(IDBConstants.TBL_NEXT_SEQ, (String)IDBConstants.TBL_FIRST_NEX_SEQ);
								                	htTblCntInsert.put(IDBConstants.CREATED_BY, user_id);
								                	htTblCntInsert.put(IDBConstants.CREATED_AT, (String)DateUtils.getDateTime());
								                	insertFlag= _TblControlDAO.insertTblControl(htTblCntInsert,plant);
								    			}
								    		}
											}
											}
											if (flag) {
									Form form = new Form();
									form.param("data", "{\"pono\":\"" + pono + "\",\"xndate\":\"" + collectionDate + "\",\"supplier\":\"" + custCode + "\",\"amount\":\"0\",\"transactionType\":\"4\",\"plant\":\"" + plant + "\",\"user\":\"" + user_id + "\",\"currency\":\"" + currencyid + "\",\"action\":\"header\"}");							
									String finTransactionResponse = HttpUtils.connect(HttpUtils.getHostInfo(request) + IConstants.ACCOUNTING_APPLICATION_URL.replace("trackaccounting", plant) + "restFinTransaction/create", form);
									if (finTransactionResponse == null || finTransactionResponse.startsWith("Error : ")) {
										throw new Exception(finTransactionResponse);
									}
										}
										}
										
									}
								}catch(Exception e) {
									throw new Exception(ThrowableUtil.getExceptionMessage(e), e);
								}
							}*/								
							//	Call Accounting module for new order : End
							result = "<br><h3>" + result;							
							String isAutoEmail = emailDao.getIsAutoEmailDetails(plant, IConstants.PURCHASE_ORDER);
							if (isAutoEmail.equalsIgnoreCase("Y"))
								mailUtil.sendEmail(plant, pono, IConstants.PURCHASE_ORDER);
						} else {
							result = "<font color=\"red\"> Error in Purchase Order  - Please Check the Data  <br><br><center>"
									+ "<input type=\"button\" value=\"Back\" onClick=\"window.location.href='javascript:history.back()'\"> ";
							result = "<br><h3>" + result;
						}
					} else {
						result = "<font color=\"red\"> Error in Purchase Order  - Invalid Order Type  <br><br><center>"
								+ "<input type=\"button\" value=\"Back\" onClick=\"window.location.href='javascript:history.back()'\"> ";
						result = "<br><h3>" + result;
					}
				} else {
					result = "<font color=\"red\"> Error in Purchase Order  - Invalid Shipping Customer  <br><br><center>"
							+ "<input type=\"button\" value=\"Back\" onClick=\"window.location.href='javascript:history.back()'\"> ";
					result = "<br><h3>" + result;
				}
			} else {
				result = "<font color=\"red\"> Error in Purchase Order  - Invalid Supplier<br><br><center>"
						+ "<input type=\"button\" value=\"Back\" onClick=\"window.location.href='javascript:history.back()'\"> ";
				result = "<br><h3>" + result;
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			if (ThrowableUtil.getMessage(e).equalsIgnoreCase("incoming order created already"))

			{
				processPono = pono;
				pono = _TblControlDAO.getNextOrder(plant, user_id, IConstants.INBOUND);
				ht.clear();
				ht.put(IDBConstants.PLANT, plant);
				ht.put(IDBConstants.POHDR_PONUM, pono);
				ht.put(IDBConstants.POHDR_CUST_CODE, custCode);
				ht.put(IDBConstants.POHDR_CUST_NAME, StrUtils.InsertQuotes(custName));
				ht.put(IDBConstants.POHDR_JOB_NUM, jobNum);
				ht.put(IDBConstants.POHDR_PERSON_INCHARGE, StrUtils.InsertQuotes(personIncharge));
				ht.put(IDBConstants.POHDR_CONTACT_NUM, contactNum);
				ht.put(IDBConstants.POHDR_ADDRESS, address);
				ht.put(IDBConstants.POHDR_ADDRESS2, address2);
				ht.put(IDBConstants.POHDR_ADDRESS3, address3);
				ht.put(IDBConstants.CREATED_BY, user_id);
				ht.put(IDBConstants.POHDR_COL_DATE, collectionDate);
				ht.put(IDBConstants.POHDR_COL_TIME, collectionTime);
				ht.put(IDBConstants.POHDR_REMARK1, StrUtils.InsertQuotes(remark1));
				ht.put(IDBConstants.POHDR_REMARK2, StrUtils.InsertQuotes(remark2)); // supplier remark
				ht.put(IDBConstants.POHDR_REMARK3, StrUtils.InsertQuotes(remark3)); // order header remark2
				ht.put(IDBConstants.ORDERTYPE, ordertype);
				ht.put(IDBConstants.POHDR_GST, inbound_Gst);
				ht.put(IDBConstants.CURRENCYID, currencyid);
				ht.put(IDBConstants.STATUS, "N");
				ht.put(IDBConstants.POHDR_DEL_DATE, deldate);
				ht.put(IDBConstants.ORDSTATUSID, orderstatus);
				ht.put(IDBConstants.SHIPPINGCUSTOMER, shippingcustomer);
				ht.put(IDBConstants.SHIPPINGID, shippingid);
				ht.put(IDBConstants.ORDERDISCOUNT, orderdiscount);
				ht.put(IDBConstants.SHIPPINGCOST, shippingcost);
				ht.put(IDBConstants.INCOTERMS, incoterms);
				ht.put(IDBConstants.LOCALEXPENSES, localexpenses);
				ht.put(IDBConstants.PAYMENTTYPE, paymenttype);
				ht.put(IDBConstants.POHDR_DELIVERYDATEFORMAT, DATEFORMAT);
				ht.put(IDBConstants.TAXTREATMENT, sTAXTREATMENT);
				ht.put(IDBConstants.PURCHASE_LOCATION, sPURCHASE_LOC);
				ht.put(IDBConstants.REVERSECHARGE, sREVERSECHARGE);
				ht.put(IDBConstants.GOODSIMPORT, sGOODSIMPORT);
				flag = _POUtil.savePoHdrDetails(ht);
				if (flag) {

					Hashtable htRecvHis = new Hashtable();
					htRecvHis.clear();
					htRecvHis.put(IDBConstants.PLANT, plant);
					htRecvHis.put(IDBConstants.DIRTYPE, TransactionConstants.CREATE_IB);
					htRecvHis.put(IDBConstants.CUSTOMER_CODE, custCode);
					htRecvHis.put(IDBConstants.POHDR_JOB_NUM, jobNum);
					htRecvHis.put(IDBConstants.MOVHIS_ORDNUM, pono);
					htRecvHis.put(IDBConstants.CREATED_BY, user_id);
					htRecvHis.put("MOVTID", "");
					htRecvHis.put("RECID", "");
					htRecvHis.put(IDBConstants.REMARKS,
							StrUtils.InsertQuotes(custName) + "," + StrUtils.InsertQuotes(remark1));
					if (!remark1.equals("") && !remark3.equals("")) {
						htRecvHis.put(IDBConstants.REMARKS, StrUtils.InsertQuotes(custName) + ","
								+ StrUtils.InsertQuotes(remark1) + "," + StrUtils.InsertQuotes(remark3));
					} else if (!remark1.equals("") && remark3.equals("")) {
						htRecvHis.put(IDBConstants.REMARKS,
								StrUtils.InsertQuotes(custName) + "," + StrUtils.InsertQuotes(remark1));
					} else if (remark1.equals("") && !remark3.equals("")) {
						htRecvHis.put(IDBConstants.REMARKS,
								StrUtils.InsertQuotes(custName) + "," + StrUtils.InsertQuotes(remark3));
					} else {
						htRecvHis.put(IDBConstants.REMARKS, StrUtils.InsertQuotes(custName));
					}

					htRecvHis.put(IDBConstants.TRAN_DATE, DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));
					htRecvHis.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
					flag = movHisDao.insertIntoMovHis(htRecvHis);
					if (flag) {
						Hashtable htMaster = new Hashtable();
						if (remark1.length() > 0) {
							htMaster.clear();
							htMaster.put(IDBConstants.PLANT, plant);
							htMaster.put(IDBConstants.REMARKS, remark1);							
							if (!_MasterDAO.isExisitRemarks(htMaster, "")) {
								htMaster.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
							htMaster.put(IDBConstants.CREATED_BY, user_id);
								_MasterDAO.InsertRemarks(htMaster);

								htRecvHis.clear();
								htRecvHis.put(IDBConstants.PLANT, plant);
								htRecvHis.put("DIRTYPE", "CREATE_REMARKS");
								htRecvHis.put("ORDNUM", "");
								htRecvHis.put(IDBConstants.ITEM, "");
								htRecvHis.put("BATNO", "");
								htRecvHis.put(IDBConstants.LOC, "");
								htRecvHis.put("REMARKS", remark1);
								htRecvHis.put(IDBConstants.CREATED_BY, user_id);
								htRecvHis.put(IDBConstants.TRAN_DATE,
										DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));
								htRecvHis.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
								flag = movHisDao.insertIntoMovHis(htRecvHis);
							}
						}
						if (remark3.length() > 0) {
							htMaster.clear();
							htMaster.put(IDBConstants.PLANT, plant);
							htMaster.put(IDBConstants.REMARKS, remark3);							
							if (!_MasterDAO.isExisitRemarks(htMaster, "")) {
								htMaster.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
							htMaster.put(IDBConstants.CREATED_BY, user_id);
								_MasterDAO.InsertRemarks(htMaster);

								htRecvHis.clear();
								htRecvHis.put(IDBConstants.PLANT, plant);
								htRecvHis.put("DIRTYPE", "CREATE_REMARKS");
								htRecvHis.put("ORDNUM", "");
								htRecvHis.put(IDBConstants.ITEM, "");
								htRecvHis.put("BATNO", "");
								htRecvHis.put(IDBConstants.LOC, "");
								htRecvHis.put("REMARKS", remark3);
								htRecvHis.put(IDBConstants.CREATED_BY, user_id);
								htRecvHis.put(IDBConstants.TRAN_DATE,
										DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));
								htRecvHis.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
								flag = movHisDao.insertIntoMovHis(htRecvHis);
							}
						}

						if (incoterms.length() > 0) {
							htMaster.clear();
							htMaster.put(IDBConstants.PLANT, plant);
							htMaster.put(IDBConstants.INCOTERMS, incoterms);
							
							if (!_MasterDAO.isExisitINCOTERMMST(htMaster, "")) {
								htMaster.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
							htMaster.put(IDBConstants.CREATED_BY, user_id);
								_MasterDAO.InsertINCOTERMS(htMaster);

								htRecvHis.clear();
								htRecvHis.put(IDBConstants.PLANT, plant);
								htRecvHis.put("DIRTYPE", "ADD_INCOTERMS_MST");
								htRecvHis.put("ORDNUM", "");
								htRecvHis.put(IDBConstants.ITEM, "");
								htRecvHis.put("BATNO", "");
								htRecvHis.put(IDBConstants.LOC, "");
								htRecvHis.put("REMARKS", incoterms);
								htRecvHis.put(IDBConstants.CREATED_BY, user_id);
								htRecvHis.put(IDBConstants.TRAN_DATE,
										DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));
								htRecvHis.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
								flag = movHisDao.insertIntoMovHis(htRecvHis);
							}
						}
					}
					new TblControlUtil().updateTblControlSeqNo(plant, IConstants.INBOUND, "P", pono);

				}
				result = "<font color=\"green\"> Purchase order " + processPono
						+ " has already been used. System has auto created a new Purchase order " + pono
						+ " for you.<br><br><center>"
						+ "<input type=\"button\" value=\"Back\" onClick=\"window.location.href='/track/purchaseorderservlet?PONO="
						+ pono + "&Submit=View'\"> ";
				result = "<br><h3>" + result;
				processPono = "";
			} else {
				throw e;
			}

		}

		return result;
	}

	private String updatePoHdr(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, Exception {
		boolean flag = false;
		Hashtable ht = new Hashtable();
		String result = "", pono = "", custName = "", display = "", jobNum = "", currencyid = "", custCode = "",
				personIncharge = "", contactNum = "", address = "", address2 = "", address3 = "", delDate = "",
				collectionDate = "", collectionTime = "", remark1 = "", remark2 = "", remark3 = "", ordertype = "",
				orderstatus = "", inbound_Gst, shippingcustomer = "", shippingid = "", orderdiscount = "",
				shippingcost = "", incoterms = "", localexpenses = "",paymenttype="",sTAXTREATMENT="",sPURCHASE_LOC="",sREVERSECHARGE="",sGOODSIMPORT="";
		try {
			HttpSession session = request.getSession();
//			DateUtils dateUtils = new DateUtils();
			CurrencyUtil curUtil = new CurrencyUtil();
			String plant = StrUtils.fString((String) session.getAttribute("PLANT")).trim();
			String user = StrUtils.fString((String) session.getAttribute("LOGIN_USER")).trim();
			pono = StrUtils.fString(request.getParameter("PONO")).trim();
			custCode = StrUtils.fString(request.getParameter("CUST_CODE1")).trim();
			custName = StrUtils.fString(request.getParameter("CUST_NAME")).trim();
			jobNum = StrUtils.fString(request.getParameter("JOB_NUM")).trim();
			personIncharge = StrUtils.fString(request.getParameter("PERSON_INCHARGE")).trim();
			contactNum = StrUtils.fString(request.getParameter("CONTACT_NUM")).trim();
			address = StrUtils.fString(request.getParameter("ADDRESS")).trim();
			address2 = StrUtils.fString(request.getParameter("ADDRESS2")).trim();
			address3 = StrUtils.fString(request.getParameter("ADDRESS3")).trim();
			collectionDate = new DateUtils().parseDate(StrUtils.fString(request.getParameter("ORDDATE")).trim());
			if (collectionDate.length() == 0) {
				collectionDate = DateUtils.getDate();
			}
			collectionTime = StrUtils.fString(request.getParameter("COLLECTION_TIME")).trim();
		//	delDate = DateUtils.parseDate(StrUtils.fString(request.getParameter("DELDATE")).trim());
			delDate = StrUtils.fString(request.getParameter("DELDATE")).trim();
			remark1 = StrUtils.fString(request.getParameter("REMARK1")).trim();
			remark2 = StrUtils.fString(request.getParameter("REMARK2")).trim();
			remark3 = StrUtils.fString(request.getParameter("REMARK3")).trim();
			ordertype = StrUtils.fString(request.getParameter("ORDERTYPE")).trim();
			inbound_Gst = StrUtils.fString(request.getParameter("GST")).trim();
			display = StrUtils.fString(request.getParameter("DISPLAY")).trim();// Currency_id
			shippingcustomer = StrUtils.fString(request.getParameter("SHIPPINGCUSTOMER")).trim();
			shippingid = StrUtils.fString(request.getParameter("SHIPPINGID")).trim();
			orderstatus = StrUtils.fString(request.getParameter("STATUS_ID")).trim();
			orderdiscount = StrUtils.fString(request.getParameter("ORDERDISCOUNT")).trim();
			shippingcost = StrUtils.fString(request.getParameter("SHIPPINGCOST")).trim();
			incoterms = StrUtils.fString(request.getParameter("INCOTERMS")).trim();
			localexpenses = StrUtils.fString(request.getParameter("LOCALEXPENSES")).trim();
			paymenttype = StrUtils.fString(request.getParameter("PAYMENTTYPE")).trim();
			String DATEFORMAT = StrUtils.fString((request.getParameter("DATEFORMAT") != null) ? "1": "0").trim();
			sTAXTREATMENT=StrUtils.fString(request.getParameter("TAXTREATMENT")).trim();
			sPURCHASE_LOC=StrUtils.fString(request.getParameter("PURCHASE_LOC")).trim();
			sREVERSECHARGE=StrUtils.fString((request.getParameter("REVERSECHARGE") != null) ? "1": "0").trim();
			sGOODSIMPORT=StrUtils.fString((request.getParameter("GOODSIMPORT") != null) ? "1": "0").trim();
			Hashtable curHash = new Hashtable();
			curHash.put(IConstants.PLANT, plant);
			curHash.put(IConstants.DISPLAY, display);
			if (display != null && display != "") {
				currencyid = curUtil.getCurrencyID(curHash, "CURRENCYID");
			}
			if (inbound_Gst.length() == 0) {
				inbound_Gst = "0";
			}
			ht.put(IDBConstants.PLANT, plant);
			ht.put(IDBConstants.POHDR_PONUM, pono);
			ht.put(IDBConstants.POHDR_CUST_NAME, StrUtils.InsertQuotes(custName));
			ht.put(IDBConstants.POHDR_CUST_CODE, custCode);
			ht.put(IDBConstants.POHDR_JOB_NUM, jobNum);
			ht.put(IDBConstants.POHDR_PERSON_INCHARGE, StrUtils.InsertQuotes(personIncharge));
			ht.put(IDBConstants.POHDR_CONTACT_NUM, contactNum);
			ht.put(IDBConstants.POHDR_ADDRESS, address);
			ht.put(IDBConstants.POHDR_ADDRESS2, address2);
			ht.put(IDBConstants.POHDR_ADDRESS3, address3);
			ht.put(IDBConstants.POHDR_COL_DATE, collectionDate);
			ht.put(IDBConstants.POHDR_COL_TIME, collectionTime);
			ht.put(IDBConstants.POHDR_REMARK1, StrUtils.InsertQuotes(remark1));
			ht.put(IDBConstants.POHDR_REMARK2, StrUtils.InsertQuotes(remark2));
			ht.put(IDBConstants.POHDR_REMARK3, StrUtils.InsertQuotes(remark3));
			ht.put(IDBConstants.STATUS, "N");
			ht.put(IDBConstants.ORDERTYPE, ordertype);
			ht.put(IDBConstants.POHDR_GST, inbound_Gst);
			ht.put(IDBConstants.CURRENCYID, currencyid);
			ht.put(IDBConstants.POHDR_DELDATE, delDate);
			ht.put(IDBConstants.ORDSTATUSID, orderstatus);
			ht.put(IDBConstants.SHIPPINGCUSTOMER, shippingcustomer);
			ht.put(IDBConstants.SHIPPINGID, shippingid);
			ht.put(IDBConstants.ORDERDISCOUNT, orderdiscount);
			ht.put(IDBConstants.SHIPPINGCOST, shippingcost);
			ht.put(IDBConstants.INCOTERMS, incoterms);
			ht.put(IDBConstants.LOCALEXPENSES, localexpenses);
			ht.put(IDBConstants.PAYMENTTYPE, paymenttype);
			ht.put(IDBConstants.POHDR_DELIVERYDATEFORMAT, DATEFORMAT);
			ht.put(IDBConstants.TAXTREATMENT, sTAXTREATMENT);
			ht.put(IDBConstants.PURCHASE_LOCATION, sPURCHASE_LOC);
			ht.put(IDBConstants.REVERSECHARGE, sREVERSECHARGE);
			ht.put(IDBConstants.GOODSIMPORT, sGOODSIMPORT);
			Boolean isCustomerAvailable = Boolean.valueOf(true);
			Boolean isvalidShippingCustomer = Boolean.valueOf(true);
			CustomerBeanDAO customerBeanDAO = new CustomerBeanDAO();
			customerBeanDAO.setmLogger(mLogger);
			isCustomerAvailable = customerBeanDAO.isExistsVendorName(StrUtils.InsertQuotes(custName), plant);
			if (shippingcustomer.length() > 0) {
				isvalidShippingCustomer = _MasterDAO.isExistsShippingDetails(shippingcustomer, plant);
			}
			if (isCustomerAvailable) {
				if (isvalidShippingCustomer) {

					MovHisDAO movHisDao = new MovHisDAO(plant);
					flag = _POUtil.updatePoHdr(ht);
					if (flag) {
						//	Call Accounting module for new order : Start
						/*if ("yes".equals(request.getSession(false).getAttribute("isAccountingEnabled"))) {
							try {
								Form form = new Form();
								form.param("data", "{\"pono\":\"" + pono + "\",\"xndate\":\"" + collectionDate + "\",\"supplier\":\"" + custCode + "\",\"amount\":\"0\",\"transactionType\":\"4\",\"plant\":\"" + plant + "\",\"user\":\"" + user + "\",\"currency\":\"" + currencyid + "\",\"action\":\"update\"}");							
								String finTransactionResponse = HttpUtils.connect(HttpUtils.getHostInfo(request) + IConstants.ACCOUNTING_APPLICATION_URL.replace("trackaccounting", plant) + "restFinTransaction/create", form);
								if (finTransactionResponse == null || finTransactionResponse.startsWith("Error : ")) {
									throw new Exception(finTransactionResponse);
								}
							}catch(Exception e) {
								throw new Exception(ThrowableUtil.getExceptionMessage(e), e);
							}
						}*/
						//	Call Accounting module for new order : End
						Hashtable htRecvHis = new Hashtable();
						htRecvHis.clear();
						htRecvHis.put(IDBConstants.PLANT, plant);
						htRecvHis.put("DIRTYPE", TransactionConstants.UPDATE_IB);
						htRecvHis.put(IDBConstants.CUSTOMER_CODE, custCode);
						htRecvHis.put(IDBConstants.POHDR_JOB_NUM, jobNum);
						htRecvHis.put(IDBConstants.MOVHIS_ORDNUM, pono);
						htRecvHis.put(IDBConstants.CREATED_BY, user);
						htRecvHis.put("MOVTID", "");
						htRecvHis.put("RECID", "");
						if (!remark1.equals("") && !remark3.equals("")) {
							htRecvHis.put(IDBConstants.REMARKS, StrUtils.InsertQuotes(custName) + ","
									+ StrUtils.InsertQuotes(remark1) + "," + StrUtils.InsertQuotes(remark3));
						} else if (!remark1.equals("") && remark3.equals("")) {
							htRecvHis.put(IDBConstants.REMARKS,
									StrUtils.InsertQuotes(custName) + "," + StrUtils.InsertQuotes(remark1));
						} else if (remark1.equals("") && !remark3.equals("")) {
							htRecvHis.put(IDBConstants.REMARKS,
									StrUtils.InsertQuotes(custName) + "," + StrUtils.InsertQuotes(remark3));
						} else {
							htRecvHis.put(IDBConstants.REMARKS, StrUtils.InsertQuotes(custName));
						}

						htRecvHis.put(IDBConstants.TRAN_DATE, DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));
						htRecvHis.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
						flag = movHisDao.insertIntoMovHis(htRecvHis);

						if (flag) {
							Hashtable htMaster = new Hashtable();
							if (remark1.length() > 0) {
								htMaster.clear();
								htMaster.put(IDBConstants.PLANT, plant);
								htMaster.put(IDBConstants.REMARKS, remark1);								
								if (!_MasterDAO.isExisitRemarks(htMaster, "")) {
									htMaster.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
								htMaster.put(IDBConstants.CREATED_BY, user);
									_MasterDAO.InsertRemarks(htMaster);

									htRecvHis.clear();
									htRecvHis.put(IDBConstants.PLANT, plant);
									htRecvHis.put("DIRTYPE", "CREATE_REMARKS");
									htRecvHis.put("ORDNUM", "");
									htRecvHis.put(IDBConstants.ITEM, "");
									htRecvHis.put("BATNO", "");
									htRecvHis.put(IDBConstants.LOC, "");
									htRecvHis.put("REMARKS", remark1);
									htRecvHis.put(IDBConstants.CREATED_BY, user);
									htRecvHis.put(IDBConstants.TRAN_DATE,
											DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));
									htRecvHis.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
									flag = movHisDao.insertIntoMovHis(htRecvHis);
								}
							}
							if (remark3.length() > 0) {
								htMaster.clear();
								htMaster.put(IDBConstants.PLANT, plant);
								htMaster.put(IDBConstants.REMARKS, remark3);								
								if (!_MasterDAO.isExisitRemarks(htMaster, "")) {
									htMaster.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
								htMaster.put(IDBConstants.CREATED_BY, user);
									_MasterDAO.InsertRemarks(htMaster);

									htRecvHis.clear();
									htRecvHis.put(IDBConstants.PLANT, plant);
									htRecvHis.put("DIRTYPE", "CREATE_REMARKS");
									htRecvHis.put("ORDNUM", "");
									htRecvHis.put(IDBConstants.ITEM, "");
									htRecvHis.put("BATNO", "");
									htRecvHis.put(IDBConstants.LOC, "");
									htRecvHis.put("REMARKS", remark3);
									htRecvHis.put(IDBConstants.CREATED_BY, user);
									htRecvHis.put(IDBConstants.TRAN_DATE,
											DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));
									htRecvHis.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
									flag = movHisDao.insertIntoMovHis(htRecvHis);
								}
							}

							if (incoterms.length() > 0) {
								htMaster.clear();
								htMaster.put(IDBConstants.PLANT, plant);
								htMaster.put(IDBConstants.INCOTERMS, incoterms);
								
								if (!_MasterDAO.isExisitINCOTERMMST(htMaster, "")) {
									htMaster.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
								htMaster.put(IDBConstants.CREATED_BY, user);
									_MasterDAO.InsertINCOTERMS(htMaster);

									htRecvHis.clear();
									htRecvHis.put(IDBConstants.PLANT, plant);
									htRecvHis.put("DIRTYPE", "ADD_INCOTERMS_MST");
									htRecvHis.put("ORDNUM", "");
									htRecvHis.put(IDBConstants.ITEM, "");
									htRecvHis.put("BATNO", "");
									htRecvHis.put(IDBConstants.LOC, "");
									htRecvHis.put("REMARKS", incoterms);
									htRecvHis.put(IDBConstants.CREATED_BY, user);
									htRecvHis.put(IDBConstants.TRAN_DATE,
											DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));
									htRecvHis.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
									flag = movHisDao.insertIntoMovHis(htRecvHis);
								}
							}
						}
					}
					if (flag) {
						result = "<font color=\"green\"> Purchase Order Updated Successfully.  <br><br><center>"
								+ "<input type=\"button\" value=\"Back\" onClick=\"window.location.href='/track/purchaseorderservlet?PONO="
								+ pono + "&Submit=View&RFLAG=4'\"> ";

						result = "<br><h3>" + result + "</h3>";

					} else {
						result = "<font color=\"red\"> Error in Updating Purchase Order  - Please Check the Data  <br><br><center>"
								+ "<input type=\"button\" value=\"Back\" onClick=\"window.location.href='javascript:history.back()'\"> ";
						result = "<br><h3>" + result + "</h3>";

					}
				} else {
					result = "<font color=\"red\"> Error in Updating Purchase Order  - Invalid Shipping Customer<br><br><center>"
							+ "<input type=\"button\" value=\"Back\" onClick=\"window.location.href='javascript:history.back()'\"> ";
					result = "<br><h3>" + result + "</h3>";

				}
			} else {
				result = "<font color=\"red\"> Error in Updating Purchase Order  - Invalid Supplier<br><br><center>"
						+ "<input type=\"button\" value=\"Back\" onClick=\"window.location.href='javascript:history.back()'\"> ";
				result = "<br><h3>" + result + "</h3>";

			}

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		}

		return result;
	}

	protected void viewPOReport(HttpServletRequest request, HttpServletResponse response, String fileName) {

		String jasperPath = "";

		Connection con = null;
		
		boolean ajax = "XMLHttpRequest".equals(request.getHeader("X-Requested-With"));
		
		try {

			CustomerBeanDAO customerBeanDAO = new CustomerBeanDAO();
			PlantMstUtil pmUtil = new PlantMstUtil();
			selectBean sb = new selectBean();
			POUtil pOUtil = new POUtil();
			CurrencyUtil currUtil = new CurrencyUtil();
			PlantMstUtil plantmstutil = new PlantMstUtil();
			sb.setmLogger(mLogger);
			customerBeanDAO.setmLogger(mLogger);
			pmUtil.setmLogger(mLogger);
			pOUtil.setmLogger(mLogger);

			HttpSession session = request.getSession();
			String CNAME = "", CADD1 = "", CADD2 = "", CADD3 = "", CADD4 = "", CZIP = "", CCOUNTRY = "", CTEL = "",
					CFAX = "", CONTACTNAME = "", CHPNO = "", CEMAIL = "", CRCBNO = "",companyregno="",SEALNAME="",SIGNNAME="", CSTATE = "",CWEBSITE="";
			String SHPCUSTOMERNAME = "", SHPCONTACTNAME = "", SHPTELEPHONE = "", SHPHANDPHONE = "", 
					SHPUNITNO = "", SHPBUILDING = "", SHPSTREET = "", SHPCITY = "", SHPSTATE = "",
					SHPCOUNTRY = "", SHPPOSTALCODE = "", DATAORDERDISCOUNT = "", DATASHIPPINGCOST = "",sEmpno = "",
					DATAINCOTERMS = "",ORDERDISCOUNTTYPE = "",TAX_TYPE="",ISDISCOUNTTAX="",ISSHIPPINGTAX="",PROJECT_NAME="",sales_location="";//SHPFAX = "",
			//AdjustmentAmount="",SHPEMAIL = "", 
					int TAXID=0;

			String PONO = null;
			List lstPoNo = new ArrayList();
			if (request.getParameter("lstPoNo") != null) {
				lstPoNo = Arrays.asList(request.getParameter("lstPoNo").split(","));
			} else if (request.getParameter("PONO") != null) {
				lstPoNo = Arrays.asList(request.getParameter("PONO").split(","));
			} else {
				lstPoNo = Arrays.asList(((String)request.getAttribute("PONO")).split(","));
			}
			String Plant = (String) session.getAttribute("PLANT");
			if(Plant == null)
				Plant = (String) request.getAttribute("PLANT");//for mail - Azees 03_2022
			List viewlistQry = plantmstutil.getPlantMstDetails(Plant);
			
			 Map maps = (Map) viewlistQry.get(0);
			List jasperPrintList = new ArrayList();
			for (int lstPoNoIndex = 0; lstPoNoIndex < lstPoNo.size(); lstPoNoIndex++) {
				PONO = StrUtils.fString(lstPoNo.get(lstPoNoIndex).toString());
				System.out.println("PONO : " + PONO);
				//String PLANT = (String) session.getAttribute("PLANT");
				String PLANT = Plant;
				String baseCurrency = (String) session.getAttribute("BASE_CURRENCY");
				String SHIPPINGID = StrUtils.fString(request.getParameter("SHIPPINGID"));
				String imagePath = DbBean.COMPANY_LOGO_PATH + "/" + PLANT.toLowerCase() + DbBean.LOGO_FILE;
	            SEALNAME=StrUtils.fString((String)maps.get("SEALNAME"));
                SIGNNAME=StrUtils.fString((String)maps.get("SIGNATURENAME"));
                String sealPath = "",signPath="";
                //imti get seal name from plantmst
                if(SEALNAME.equalsIgnoreCase("")){
                sealPath = DbBean.COMPANY_LOGO_PATH + "/" + DbBean.NO_LOGO_FILE;
                }else {
                	sealPath = DbBean.COMPANY_SEAL_PATH + "/" + SEALNAME;
                }
                if(SIGNNAME.equalsIgnoreCase("")){
                	signPath = DbBean.COMPANY_LOGO_PATH + "/" + DbBean.NO_LOGO_FILE;
                    }else {
                    	signPath = DbBean.COMPANY_SIGNATURE_PATH + "/" + SIGNNAME;
                    }
                //imti end
				String imagePath1 = DbBean.COMPANY_LOGO_PATH + "/" + PLANT.toLowerCase() + "Logo1.gif";
				String imagePath2, Orientation = "";
				PlantMstDAO plantMstDAO = new PlantMstDAO();
				String numberOfDecimal = plantMstDAO.getNumberOfDecimal(PLANT);
				File checkImageFile = new File(imagePath);
				if (!checkImageFile.exists()) {
					imagePath = DbBean.COMPANY_LOGO_PATH + "/" + DbBean.NO_LOGO_FILE;
				}
				File checkImageFile1 = new File(imagePath1);
				if (!checkImageFile1.exists()) {
					imagePath1 = DbBean.COMPANY_LOGO_PATH + "/" + DbBean.NO_LOGO_FILE;
				}

				con = DbBean.getConnection();

				Hashtable htCond = new Hashtable();
				htCond.put("PLANT", PLANT);
				htCond.put("PONO", PONO);
				String query = "isnull(jobNum,'') as jobNum,isnull(remark1,'') as remark1,isnull(EMPNO,'') as EMPNO,"
						+ "custCode,isnull(inbound_Gst,0) as inbound_Gst,isnull(collectionDate + ' ' + LEFT(CollectionTime, 2)+':'+RIGHT(CollectionTime, 2),'') as date,"
						+ "isnull(deldate,'') as deldate,CASE WHEN isnull(ORDERDISCOUNTTYPE,'%')='%' THEN isnull(orderdiscount,0) ELSE (isnull(orderdiscount,0)*ISNULL(CURRENCYUSEQT,1)) END AS orderdiscount," 
						+ "isnull(ORDERDISCOUNTTYPE,'%') ORDERDISCOUNTTYPE,isnull(ISTAXINCLUSIVE,0) ISTAXINCLUSIVE,"
						+ "ISNULL(SHIPPINGCOST,0)*ISNULL(CURRENCYUSEQT,1) shippingcost,isnull(CRBY,'') as CRBY,isnull(PAYMENTTYPE,'') as PAYMENTTYPE,ISNULL(TAXID,0) TAXID," 
						+ "ISNULL((select DISTINCT TAXTYPE from FINCOUNTRYTAXTYPE B WHERE B.ID="+PLANT+"_pohdr.TAXID),'') TAX_TYPE,"
						+ "ISNULL((select DISTINCT PROJECT_NAME from "+PLANT+"_FINPROJECT B WHERE B.ID="+PLANT+"_pohdr.PROJECTID),'') PROJECT_NAME,"
						+ "ISNULL((select DISTINCT PREFIX from FINSALESLOCATION B WHERE B.STATE="+PLANT+"_pohdr.PURCHASE_LOCATION),'') sales_location,"
						+ "isnull(incoterms,'') incoterms,ISNULL(ADJUSTMENT,0)*ISNULL(CURRENCYUSEQT,1) ADJUSTMENT,ISNULL(ISDISCOUNTTAX,0) ISDISCOUNTTAX,ISNULL(ISSHIPPINGTAX,0) ISSHIPPINGTAX";

				ArrayList arrayRemark1 = new ArrayList();
				arrayRemark1 = pOUtil.getPoHdrDetails(query, htCond);
				Map dataMap = (Map) arrayRemark1.get(0);

//				String SysDate = DateUtils.getDate();
//				Date sysTime1 = new Date(System.currentTimeMillis());
//				Date cDt = new Date(System.currentTimeMillis());
				ArrayList listQry = pmUtil.getPlantMstDetails(PLANT);

				for (int i = 0; i < listQry.size(); i++) {
					Map map = (Map) listQry.get(i);

					CNAME = (String) map.get("PLNTDESC");
					CADD1 = (String) map.get("ADD1");
					CADD2 = (String) map.get("ADD2");
					CADD3 = (String) map.get("ADD3");
					CADD4 = (String) map.get("ADD4");
					CCOUNTRY = (String) map.get("COUNTY");
					CZIP = (String) map.get("ZIP");
					CTEL = (String) map.get("TELNO");
					CFAX = (String) map.get("FAX");
					CONTACTNAME = (String) map.get("NAME");
					CHPNO = (String) map.get("HPNO");
					CEMAIL = (String) map.get("EMAIL");
					CRCBNO = (String) map.get("RCBNO");
					CSTATE = (String) map.get("STATE");
					CWEBSITE = (String) map.get("WEBSITE");
					companyregno = (String) map.get("companyregnumber");//imtiuen
					

				}

				ArrayList arrCust = customerBeanDAO.getVendorDetailsForPO(PLANT, PONO);
				if (arrCust.size() > 0) {
					String sCustCode = (String) arrCust.get(0);
					String sCustName = (String) arrCust.get(1);
					String sAddr1 = (String) arrCust.get(2);
					String sAddr2 = (String) arrCust.get(3);
					String sAddr3 = (String) arrCust.get(4);
					String sCountry = (String) arrCust.get(5);
					String sZip = (String) arrCust.get(6);
//					String sCons = (String) arrCust.get(7);
					String sContactName = (String) arrCust.get(8);
//					String sDesgination = (String) arrCust.get(9);
					String sTelNo = (String) arrCust.get(10);
//					String sHpNo = (String) arrCust.get(11);
					String sEmail = (String) arrCust.get(12);
					String sFax = (String) arrCust.get(13);
					String sRemarks = (String) arrCust.get(14);
					String sAddr4 = (String) arrCust.get(15);
//					String sIsActive = (String) arrCust.get(16);
					String orderRemarks = (String) arrCust.get(18);
					String pays = (String) arrCust.get(38);
//					String sPayTerms = (String) arrCust.get(19);
					String orderRemarks3 = (String) arrCust.get(20);
					String State = (String) arrCust.get(22);
					SHIPPINGID = (String) arrCust.get(23);
					String sRcbno = (String) arrCust.get(24);
					String suenno = (String) arrCust.get(25);//imtiuen
					// get shipping details from shipping master table
					ArrayList arrShippingDetails =new MasterUtil().getInboundShippingDetails(PONO, SHIPPINGID, PLANT);
					ArrayList arrShippingvendDetails =new MasterUtil().getInboundShippingDetailsVendmst(PONO, SHIPPINGID, PLANT);
					Map parameters = new HashMap();
					parameters.put("numberOfDecimal", Integer.parseInt(numberOfDecimal));
					if (arrShippingDetails.size() > 0) {
						parameters.put("shipToId", SHIPPINGID);
						SHPCUSTOMERNAME = (String) arrShippingDetails.get(1);
						SHPCONTACTNAME = (String) arrShippingDetails.get(2);
						SHPTELEPHONE = (String) arrShippingDetails.get(3);
						SHPHANDPHONE = (String) arrShippingDetails.get(4);
//						SHPFAX = (String) arrShippingDetails.get(5);
//						SHPEMAIL = (String) arrShippingDetails.get(6);
						SHPUNITNO = (String) arrShippingDetails.get(7);
						SHPBUILDING = (String) arrShippingDetails.get(8);
						SHPSTREET = (String) arrShippingDetails.get(9);
						SHPCITY = (String) arrShippingDetails.get(10);
						SHPSTATE = (String) arrShippingDetails.get(11);
						SHPCOUNTRY = (String) arrShippingDetails.get(12);
						SHPPOSTALCODE = (String) arrShippingDetails.get(13);
					} else {
//						parameters.put("shipToId", "");
						
						if (arrShippingvendDetails.size() > 0) {
							parameters.put("shipToId", SHIPPINGID);
							SHPCUSTOMERNAME = (String) arrShippingvendDetails.get(1);
							SHPCONTACTNAME = (String) arrShippingvendDetails.get(2);
							SHPTELEPHONE = (String) arrShippingvendDetails.get(3);
							SHPHANDPHONE = (String) arrShippingvendDetails.get(4);
//							SHPFAX = (String) arrShippingDetails.get(5);
//							SHPEMAIL = (String) arrShippingDetails.get(6);
							SHPUNITNO = (String) arrShippingvendDetails.get(7);
							SHPBUILDING = (String) arrShippingvendDetails.get(8);
							SHPSTREET = (String) arrShippingvendDetails.get(9);
							SHPCITY = (String) arrShippingvendDetails.get(10);
							SHPSTATE = (String) arrShippingvendDetails.get(11);
							SHPCOUNTRY = (String) arrShippingvendDetails.get(12);
							SHPPOSTALCODE = (String) arrShippingvendDetails.get(13);
						}else {
							parameters.put("shipToId", "");
						}
					}

					String orderType = new PoHdrDAO().getOrderTypeForPO(PLANT, PONO);
					if ("yes".equals(request.getParameter("with_batch"))) {
						parameters.put("PrintBatch", "yes");
					}
					if ("yes".equals(request.getParameter("is_from_reports"))) {
						parameters.put("IsFromOrderManagement", "no");
					} else {
						parameters.put("IsFromOrderManagement", "yes");
					}
					// Customer Details
					parameters.put("imagePath", imagePath);
					parameters.put("imagePath1", imagePath1);
					parameters.put("inboundOrderNo", PONO);
					parameters.put("inboundOrderNo", PONO);
					parameters.put("orderRemarks", orderRemarks);
					parameters.put("company", PLANT);
					parameters.put("taxInvoiceTo_CompanyName", sCustName);
					if(fileName.equalsIgnoreCase("printPOInvoice")) {
					parameters.put("taxInvoiceTo_BlockAddress", sAddr1 + "  " + sAddr2);
					parameters.put("taxInvoiceTo_RoadAddress", sAddr3 + "  " + sAddr4);
					if (State.equals("")) {
						parameters.put("taxInvoiceTo_Country", sCountry);
					}else {
						parameters.put("taxInvoiceTo_Country", State + "\n" + sCountry);
					}
					} else {
						String taxInvoiceTo_BlockAddress="";
						if(sAddr2.length()>0)
							taxInvoiceTo_BlockAddress=sAddr2+ "  ";
						parameters.put("taxInvoiceTo_BlockAddress", taxInvoiceTo_BlockAddress + sAddr3);//1.Building+Street
						parameters.put("taxInvoiceTo_RoadAddress", sAddr1);//2.Uint No.
						if (State.equals("")) {
							parameters.put("taxInvoiceTo_Country", sCountry);
						}else {
							parameters.put("taxInvoiceTo_Country", State + "\n" + sCountry);
						}
					}
					parameters.put("taxInvoiceTo_ZIPCode", sZip);
					parameters.put("taxInvoiceTo_AttentionTo", sContactName);
					parameters.put("taxInvoiceTo_CCTO", "");
					parameters.put("taxInvoiceTo_Telno", sTelNo);
					parameters.put("taxInvoiceTo_Fax", sFax);
					parameters.put("taxInvoiceTo_Email", sEmail);
					parameters.put("sRCBNO", sRcbno);
					parameters.put("sUENNO", suenno);//imtiuen
					PoHdrDAO poHdrDAO= new PoHdrDAO();
					PoHdr poheader = poHdrDAO.getPoHdrByPono(PLANT, PONO);
					TransportModeDAO transportmodedao = new TransportModeDAO();
					String transportmode = "";
					if(poheader.getTRANSPORTID() > 0){
						transportmode = transportmodedao.getTransportModeById(PLANT, poheader.getTRANSPORTID());
					}
					parameters.put("trans", transportmode);//imtiuen
					String paymentterms = "";
					paymentterms = poheader.getPAYMENT_TERMS();
					parameters.put("payterms", paymentterms);//imtiuen

					// Company Details
					parameters.put("fromAddress_CompanyName", CNAME);
//					if(fileName.equalsIgnoreCase("printPOInvoice")) {
//						parameters.put("fromAddress_BlockAddress", CADD1 + "," + CADD2);
//						parameters.put("fromAddress_RoadAddress", CADD3 + "," + CADD4);
//						parameters.put("fromAddress_Country", CSTATE + ", " + CCOUNTRY);
//					} else {
//					String fromAddress_BlockAddress="";
//					if(CADD2.length()>0)
//						fromAddress_BlockAddress=CADD2 + ",";
//					parameters.put("fromAddress_BlockAddress", fromAddress_BlockAddress + CADD3); //1.Building+Street
//					parameters.put("fromAddress_RoadAddress", CADD1); //Unit No.
//					parameters.put("fromAddress_Country", CCOUNTRY);
//					}
					String COMP_INDUSTRY = new PlantMstDAO().getCOMP_INDUSTRY(Plant);//Check Company Industry
//					if(!COMP_INDUSTRY.equalsIgnoreCase("Centralised Kitchen"))
//					{
//					parameters.put("fromAddress_BlockAddress", CADD1 + " " + CADD2);
//					parameters.put("fromAddress_RoadAddress", CADD3 + " " + CADD4);
//					if(CSTATE.equals("")) {
//						parameters.put("fromAddress_Country", CSTATE + "" + CCOUNTRY);
//					}else {
//					parameters.put("fromAddress_Country", CSTATE + "," + CCOUNTRY);
//					}
//					} else {
//						String fromAddress_BlockAddress="";
//						if(CADD2.length()>0)
//							fromAddress_BlockAddress=CADD2 + ",";
//						parameters.put("fromAddress_BlockAddress", fromAddress_BlockAddress + CADD3); //1.Building+Street
//						parameters.put("fromAddress_RoadAddress", CADD1); //Unit No.
//						parameters.put("fromAddress_Country", CCOUNTRY);
//					}
					
					
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
					parameters.put("fromAddress_Website", CWEBSITE);
					parameters.put("fromAddress_RCBNO", CRCBNO);
					parameters.put("fromAddress_UENNO", companyregno);//imtiuen
					// Start code by Bruhan for getting refno in report instead of remarks on
					// 19/March/2013
					parameters.put("currentTime", (String) dataMap.get("date"));
					System.out.println("Referance No :" + (String) dataMap.get("jobNum"));
					parameters.put("referanceNO", (String) dataMap.get("jobNum"));
					// End code by Bruhan for getting refno in report instead of remarks on
					// 19/March/2013
					parameters.put("InvoiceTerms", "");
					// Included Delivery Date By Samatha
					String deldate = StrUtils.fString((String) dataMap.get("deldate"));
					DATAORDERDISCOUNT = dataMap.get("orderdiscount").toString();
					DATASHIPPINGCOST = dataMap.get("shippingcost").toString();
					DATAINCOTERMS = dataMap.get("incoterms").toString();
					sEmpno = dataMap.get("EMPNO").toString();
					
//					if (deldate.length() > 0)
//						deldate = deldate;
					parameters.put("DeliveryDt", deldate);
					// End Delivery Date By Samatha
					String inbgst = (String) dataMap.get("inbound_Gst");
					Double gst = new Double(inbgst);					
					/*gst = gst / 100;
					System.out.println("GST : " + gst);
					parameters.put("taxPercentage", gst.doubleValue());*/
					parameters.put("orderType", orderType);
					parameters.put("localCurrency", baseCurrency);
					//new changes - azees 12/2020 
					parameters.put("ISTAXINCLUSIVE",  dataMap.get("ISTAXINCLUSIVE").toString());
					ORDERDISCOUNTTYPE=dataMap.get("ORDERDISCOUNTTYPE").toString();
					PROJECT_NAME=dataMap.get("PROJECT_NAME").toString();
					sales_location=dataMap.get("sales_location").toString();
					parameters.put("ORDERDISCOUNTTYPE", ORDERDISCOUNTTYPE);
					parameters.put("AdjustmentAmount", dataMap.get("ADJUSTMENT").toString());
					parameters.put("ISDISCOUNTTAX", dataMap.get("ISDISCOUNTTAX").toString());
					parameters.put("ISSHIPPINGTAX", dataMap.get("ISSHIPPINGTAX").toString());
					parameters.put("PROJECTNAME", PROJECT_NAME);
					ISSHIPPINGTAX=dataMap.get("ISSHIPPINGTAX").toString();
					ISDISCOUNTTAX=dataMap.get("ISDISCOUNTTAX").toString();
					if(ISSHIPPINGTAX.equalsIgnoreCase("0"))
						ISSHIPPINGTAX="Tax Exclusive";
					else
						ISSHIPPINGTAX="Tax Inclusive";
					if(ISDISCOUNTTAX.equalsIgnoreCase("0"))
						ISDISCOUNTTAX="Tax Exclusive";
					else
						ISDISCOUNTTAX="Tax Inclusive";
					TAX_TYPE = dataMap.get("TAX_TYPE").toString();
					/*if(TAX_TYPE.equalsIgnoreCase("EXEMPT") || TAX_TYPE.equalsIgnoreCase("OUT OF SCOPE"))
						TAX_TYPE = StrUtils.fString(TAX_TYPE+" [0.0%]").trim();
					else
						TAX_TYPE = StrUtils.fString(TAX_TYPE+" ["+inbgst+"%]").trim();*/
					TAXID = Integer.valueOf(dataMap.get("TAXID").toString());
					FinCountryTaxType fintaxtype = new FinCountryTaxType();
					Short ptaxiszero=0,ptaxisshow=0;
					
					if(TAXID > 0){
						FinCountryTaxTypeDAO finCountryTaxTypeDAO = new FinCountryTaxTypeDAO();
						fintaxtype = finCountryTaxTypeDAO.getCountryTaxTypesByid(TAXID);
						ptaxiszero=fintaxtype.getISZERO();
						ptaxisshow=fintaxtype.getSHOWTAX();
					}
					if(ptaxiszero == 1 && ptaxisshow == 0){							
						TAX_TYPE = "";
						inbgst="0.0";
					} else if(ptaxiszero == 0 && ptaxisshow == 0){
						TAX_TYPE = "";
						inbgst="0.0";
					} else if(ptaxiszero == 0 && ptaxisshow == 1){						
						if(sales_location.equalsIgnoreCase(""))
							TAX_TYPE = StrUtils.fString(TAX_TYPE+" ["+inbgst+"%]").trim();
						else
							TAX_TYPE = StrUtils.fString(TAX_TYPE+"("+sales_location+") ["+inbgst+"%]").trim();
					} else {							
						
						if(sales_location.equalsIgnoreCase(""))
							TAX_TYPE = StrUtils.fString(TAX_TYPE+" [0.0%]").trim();
						else
							TAX_TYPE = StrUtils.fString(TAX_TYPE+"("+sales_location+") [0.0%]").trim();
						inbgst="0.0";//Author: Azees  Create date: August 05,2021  Description: zero tax issue
					}
	  				parameters.put("TAX_TYPE", TAX_TYPE);
	  				gst = new Double(inbgst);
	  				gst = gst / 100;
					System.out.println("GST : " + gst);
					parameters.put("taxPercentage", gst.doubleValue());
					
					// Get Currency ID
					String currencyid = new PoHdrDAO().getCurrencyID(PLANT, PONO);
					// Get Currency Display
					Hashtable curHash = new Hashtable();
					curHash.put(IDBConstants.PLANT, PLANT);
					curHash.put(IDBConstants.CURRENCYID, currencyid);
					String curDisplay = currUtil.getCurrencyID(curHash, "DISPLAY");
					OrderTypeDAO ODAO = new OrderTypeDAO();
					String orderDesc = "";
					double doubledataorderdiscount = new Double(DATAORDERDISCOUNT);
					double doubledatashippingcost = new Double(DATASHIPPINGCOST);

					// get data's for inbound report data config

					if (fileName.equals("printPO")||fileName.equals("printPOKitchen")) {
						POUtil poUtil = new POUtil();
						Map m = poUtil.getPOReceiptHdrDetails(PLANT);
//						Map k = poUtil.getPOReceiptInvoiceHdrDetails(PLANT); //imti
						Orientation = (String) m.get("PrintOrientation");
						if (orderType.equals("INBOUND ORDER")) {
							orderDesc = (String) m.get("HDR1");
						} else {
							orderDesc = ODAO.getOrderTypeDesc(PLANT, orderType);
						}
						if (m.get("DISPLAYBYORDERTYPE").equals("1"))
							parameters.put("OrderHeader", orderDesc);
						else
							parameters.put("OrderHeader", (String) m.get("HDR1"));
						parameters.put("ToHeader", (String) m.get("HDR2"));
						parameters.put("FromHeader", (String) m.get("HDR3"));
						parameters.put("SHIPTO", (String) m.get("SHIPTO"));
						parameters.put("Date", (String) m.get("DATE"));
						parameters.put("OrderNo", (String) m.get("ORDERNO"));
						parameters.put("RefNo", (String) m.get("REFNO"));
						parameters.put("SoNo", (String) m.get("SONO"));
						parameters.put("Item", (String) m.get("ITEM"));
						parameters.put("Description", (String) m.get("DESCRIPTION"));
						parameters.put("Terms", (String) m.get("TERMS"));
						parameters.put("OrderQty", (String) m.get("ORDERQTY"));
						
						parameters.put("UOM", (String) m.get("UOM"));
						parameters.put("Footer1", (String) m.get("F1"));
						parameters.put("Footer2", (String) m.get("F2"));
						parameters.put("Footer3", (String) m.get("F3"));
						parameters.put("Footer4", (String) m.get("F4"));
						parameters.put("Footer5", (String) m.get("F5"));
						parameters.put("Footer6", (String) m.get("F6"));
						parameters.put("Footer7", (String) m.get("F7"));
						parameters.put("Footer8", (String) m.get("F8"));
						parameters.put("Footer9", (String) m.get("F9"));
						parameters.put("PrdXtraDetails", (String) m.get("PRINTXTRADETAILS"));
						parameters.put("PRINTWITHPRODUCTREMARKS", (String) m.get("PRINTWITHPRODUCTREMARKS"));
						parameters.put("PRINTWITHBRAND", (String) m.get("PRINTWITHBRAND"));
						parameters.put("RCBNO", (String) m.get("RCBNO"));
						parameters.put("PRINTWITHTRANSPORT_MODE", (String) m.get("PRINTWITHTRANSPORT_MODE"));
						parameters.put("TRANSPORT_MODE", (String) m.get("TRANSPORT_MODE"));
						parameters.put("TERMSDETAILS", (String) m.get("TERMSDETAILS"));
						parameters.put("UENNO", (String) m.get("UENNO"));//imtiuen
						parameters.put("SUPPLIERRCBNO", (String) m.get("SUPPLIERRCBNO"));
						parameters.put("SUPPLIERUENNO", (String) m.get("SUPPLIERUENNO"));//imtiuen
						if (m.get("PSUPREMARKS").equals("1"))
							parameters.put("SupRemarks", sRemarks);
						else
							parameters.put("SupRemarks", "");
						parameters.put("DeliveryDate", (String) m.get("DELIVERYDATE"));
						if (orderRemarks.length() > 0)
							orderRemarks = (String) m.get("REMARK1") + " : " + orderRemarks;
						if (orderRemarks3.length() > 0)
							orderRemarks3 = (String) m.get("REMARK2") + " : " + orderRemarks3;
						// ship to Address
						if(ORDERDISCOUNTTYPE.equalsIgnoreCase("%"))
						parameters.put("lblOrderDiscount",
								(String) m.get("ORDERDISCOUNT") + " " + "(" + DATAORDERDISCOUNT + "%" + ")");
						else
							parameters.put("lblOrderDiscount",
									(String) m.get("ORDERDISCOUNT") + " " + "(" + ORDERDISCOUNTTYPE + ")");
						parameters.put("lblShippingCost",
								(String) m.get("SHIPPINGCOST") + " " + "(" + curDisplay + ")");
						parameters.put("lblINCOTERM", (String) m.get("INCOTERM"));
						parameters.put("SHPCUSTOMERNAME", SHPCUSTOMERNAME);
						if (SHPCONTACTNAME.length() > 0) {
							parameters.put("SHPCONTACTNAME", "Attn: " + SHPCONTACTNAME);
						} else {
							parameters.put("SHPCONTACTNAME", SHPCONTACTNAME);
						}
						if (SHPTELEPHONE.length() > 0) {
							parameters.put("SHPTELEPHONE", "Tel: " + SHPTELEPHONE);
						} else {
							parameters.put("SHPTELEPHONE", SHPTELEPHONE);
						}
						if (SHPHANDPHONE.length() > 0) {
							parameters.put("SHPHANDPHONE", "HP: " + SHPHANDPHONE);
						} else {
							parameters.put("SHPHANDPHONE", SHPHANDPHONE);
						}
						parameters.put("SHPUNITNO", SHPUNITNO);
						parameters.put("SHPBUILDING", SHPBUILDING);
						parameters.put("SHPSTREET", SHPSTREET);
						parameters.put("SHPCITY", SHPCITY);
						parameters.put("SHPSTATE", SHPSTATE);
						parameters.put("SHPCOUNTRY", SHPCOUNTRY);
						parameters.put("SHPPOSTALCODE", SHPPOSTALCODE);
						parameters.put("DATAORDERDISCOUNT", doubledataorderdiscount);
						parameters.put("DATASHIPPINGCOST", doubledatashippingcost);
						parameters.put("DATAINCOTERMS", DATAINCOTERMS);
						if (m.get("PRINTEMPLOYEE").equals("1")) {// Author: Azees  Create date: July 10,2021  Description: Employee on Jasper
							parameters.put("Employee", (String) m.get("EMPLOYEE"));
							String empname = new EmployeeDAO().getEmpname(PLANT, sEmpno, "");
							parameters.put("EmployeeName", empname);
						} else {
							parameters.put("Employee", "");
							parameters.put("EmployeeName", "");
						}
						// parameter shipping address end
						// if (Orientation.equals("Portrait")) {

						if (!checkImageFile.exists()) {
							imagePath2 = imagePath1;
							imagePath = "";
						} else if (!checkImageFile1.exists()) {
							imagePath2 = imagePath;
							imagePath1 = "";
						} else {
							imagePath2 = "";
						}
						parameters.put("PRINTWITHDELIVERYDATE",  m.get("PRINTWITHDELIVERYDATE"));
						parameters.put("PRODUCTDELIVERYDATE",  m.get("PRODUCTDELIVERYDATE"));
						parameters.put("PRINTWITHPRODUCTDELIVERYDATE",  m.get("PRINTWITHPRODUCTDELIVERYDATE"));
						parameters.put("PROJECT",  m.get("PROJECT"));
						parameters.put("ISPROJECT",  m.get("PRINTWITHPROJECT"));
						parameters.put("PRINTUENNO",  m.get("PRINTWITHUENNO"));//imthiuen
						parameters.put("PRINTSUPPLIERUENNO",  m.get("PRINTWITHSUPPLIERUENNO"));//imthiuen
						parameters.put("imagePath", imagePath);
						parameters.put("imagePath1", imagePath1);
						parameters.put("imagePath2", imagePath2);
						// }
						//imti start seal,sign condition in printoutconfig
						String PRINTWITHCOMPANYSEAL = (String) m.get("PRINTWITHCOMPANYSEAL");
						String PRINTWITHCOMPANYSIG= (String) m.get("PRINTWITHCOMPANYSIG");
						if(PRINTWITHCOMPANYSEAL.equalsIgnoreCase("0")){
			                sealPath = DbBean.COMPANY_LOGO_PATH + "/" + DbBean.NO_LOGO_FILE;
			                }
						if(PRINTWITHCOMPANYSIG.equalsIgnoreCase("0")){
							signPath = DbBean.COMPANY_LOGO_PATH + "/" + DbBean.NO_LOGO_FILE;
			                }
						parameters.put("sealPath", sealPath);
						parameters.put("signPath", signPath);
						parameters.put("CompanyDate", (String) m.get("COMPANYDATE"));
						parameters.put("CompanyName", (String) m.get("COMPANYNAME"));
						parameters.put("CompanyStamp", (String) m.get("COMPANYSTAMP"));
						parameters.put("CompanySig", (String) m.get("COMPANYSIG"));
						parameters.put("PreBy", (String) m.get("PREPAREDBY"));
						parameters.put("PreByUser", dataMap.get("CRBY").toString());
						parameters.put("PrintRecmLoc", (String) m.get("PRINTRECMLOC"));
						ArrayList arrayposdao = new ArrayList();
						arrayposdao = _poDetDAO.selectPoDet("distinct ITEM,ItemDesc,UNITCOST ", htCond);
						if(arrayposdao.size() > 3)
						{ }
						else
							parameters.put(JRParameter.IS_IGNORE_PAGINATION, true);
						if (m.get("PRINTBARCODE").equals("1") && fileName.equalsIgnoreCase("printPOKitchen")) 
							jasperPath = DbBean.JASPER_INPUT + "/" + "printPOWithBarcodePortrait";
						else if(fileName.equalsIgnoreCase("printPOKitchen"))
							jasperPath = DbBean.JASPER_INPUT + "/" + fileName +"Portrait";
						else {
						if (m.get("PRINTBARCODE").equals("1")) {
							jasperPath = DbBean.JASPER_INPUT + "/" + fileName+ "WithBarcode";
							if (Orientation.equals("Portrait")) {
								jasperPath = DbBean.JASPER_INPUT + "/" + fileName+ "WithBarcodePortrait";
							}
						} else {
							jasperPath = DbBean.JASPER_INPUT + "/" + fileName;
							if (Orientation.equals("Portrait")) {
								jasperPath = DbBean.JASPER_INPUT + "/" + fileName +"Portrait";
							}
						}
						}

					}
					if (fileName.equals("printPOInvoice")||fileName.equals("printPOInvoiceKitchen")) {
						POUtil poUtil = new POUtil();
						Map m = poUtil.getPOReceiptInvoiceHdrDetails(PLANT);
						Orientation = (String) m.get("PrintOrientation");
						if (orderType.equals("INBOUND ORDER")) {
							orderDesc = (String) m.get("HDR1");
						} else {
							orderDesc = ODAO.getOrderTypeDesc(PLANT, orderType);
						}
						if (m.get("DISPLAYBYORDERTYPE").equals("1"))
							parameters.put("OrderHeader", orderDesc);
						else
							parameters.put("OrderHeader", (String) m.get("HDR1"));
						// parameters.put("OrderHeader", (String) m.get("HDR1"));
						parameters.put("ToHeader", (String) m.get("HDR2"));
						parameters.put("FromHeader", (String) m.get("HDR3"));
						parameters.put("SHIPTO", (String) m.get("SHIPTO"));
						parameters.put("Date", (String) m.get("DATE"));
						parameters.put("OrderNo", (String) m.get("ORDERNO"));
						parameters.put("RefNo", (String) m.get("REFNO"));
						parameters.put("Terms", (String) m.get("TERMS"));
//						if (m.get("PRINTSUPTERMS").equals("1")) {
							//parameters.put("TermsDetails", sPayTerms);
							parameters.put("TermsDetails", dataMap.get("PAYMENTTYPE").toString());
//						} else {
//							parameters.put("TermsDetails", (String) m.get("TERMSDETAILS"));
//						}
						//AUTHOR : imthiyas  
						//DATE : June 28,2021
						//DESC : display transportmode and paymentterms in jasper
						parameters.put("trans", transportmode);
						
						if (m.get("PRINTSUPTERMS").equals("1")) {
							parameters.put("payterms", pays);
							} else {
								parameters.put("payterms", paymentterms);
							}
						
//						parameters.put("payterms", paymentterms);
						parameters.put("PRINTWITHTRANSPORT_MODE", (String) m.get("PRINTWITHTRANSPORT_MODE"));
						parameters.put("TRANSPORT_MODE", (String) m.get("TRANSPORT_MODE"));
						parameters.put("TERMSDETAILS", (String) m.get("TERMSDETAILS"));
						//end
						
						
						parameters.put("SoNo", (String) m.get("SONO"));
						parameters.put("Item", (String) m.get("ITEM"));
						parameters.put("BarCode", (String) m.get("BARCODE"));
						parameters.put("Description", (String) m.get("DESCRIPTION"));
						parameters.put("OrderQty", (String) m.get("ORDERQTY"));
						parameters.put("UOM", (String) m.get("UOM"));
						parameters.put("Rate", (String) m.get("RATE"));
						parameters.put("TaxAmount", (String) m.get("TAXAMOUNT"));
						parameters.put("Amt", (String) m.get("AMT"));
						parameters.put("SubTotal", (String) m.get("SUBTOTAL") + " " + "(" + curDisplay + ")");
						String taxby =new PlantMstDAO().getTaxBy(PLANT);
						parameters.put("TAXBY", taxby);
						if (taxby.equalsIgnoreCase("BYORDER")) {
							parameters.put("TotalTax", (String) m.get("TOTALTAX") + " " + "(" + inbgst + "%" + ")");
						} else {
							parameters.put("TotalTax", (String) m.get("TOTALTAX"));
						}
						parameters.put("Total", (String) m.get("TOTAL") + " " + "(" + curDisplay + ")");
						
						String PRINTROUNDOFFTOTALWITHDECIMAL =  (String) m.get("PRINTROUNDOFFTOTALWITHDECIMAL");
						parameters.put("PrintRoundoffTotalwithDecimal", PRINTROUNDOFFTOTALWITHDECIMAL);
						parameters.put("RoundoffTotalwithDecimal", (String) m.get("ROUNDOFFTOTALWITHDECIMAL") + " " + "(" + curDisplay + ")");
						parameters.put("CALCULATETAXWITHSHIPPINGCOST",  m.get("CALCULATETAXWITHSHIPPINGCOST"));
						parameters.put("Adjustment",  m.get("ADJUSTMENT"));
						parameters.put("PROJECT",  m.get("PROJECT"));
						parameters.put("ISPROJECT",  m.get("PRINTWITHPROJECT"));
						parameters.put("PRINTUENNO",  m.get("PRINTWITHUENNO"));//imtiuen
						parameters.put("PRINTSUPPLIERUENNO",  m.get("PRINTWITHSUPPLIERUENNO"));//imtiuen
						parameters.put("PRODUCTRATESARE",  m.get("PRODUCTRATESARE"));
						parameters.put("Footer1", (String) m.get("F1"));
						parameters.put("Footer2", (String) m.get("F2"));
						parameters.put("Footer3", (String) m.get("F3"));
						parameters.put("Footer4", (String) m.get("F4"));
						parameters.put("Footer5", (String) m.get("F5"));
						parameters.put("Footer6", (String) m.get("F6"));
						parameters.put("Footer7", (String) m.get("F7"));
						parameters.put("Footer8", (String) m.get("F8"));
						parameters.put("Footer9", (String) m.get("F9"));
						parameters.put("PrdXtraDetails", (String) m.get("PRINTXTRADETAILS"));
						parameters.put("CompanyDate", (String) m.get("COMPANYDATE"));
						parameters.put("CompanyName", (String) m.get("COMPANYNAME"));
						parameters.put("CompanyStamp", (String) m.get("COMPANYSTAMP"));
						parameters.put("CompanySig", (String) m.get("COMPANYSIG"));
						parameters.put("PrintRecmLoc", (String) m.get("PRINTRECMLOC"));
						parameters.put("PRINTWITHBRAND", (String) m.get("PRINTWITHBRAND"));
						parameters.put("PRINTWITHPRODUCTREMARKS", (String) m.get("PRINTWITHPRODUCTREMARKS"));
						parameters.put("RCBNO", (String) m.get("RCBNO"));
						parameters.put("UENNO", (String) m.get("UENNO"));//imtiuen
						parameters.put("TOTALAFTERDISCOUNT", (String) m.get("TOTALAFTERDISCOUNT"));
						parameters.put("SUPPLIERRCBNO", (String) m.get("SUPPLIERRCBNO"));
						parameters.put("SUPPLIERUENNO", (String) m.get("SUPPLIERUENNO"));//imtiuen
						if (m.get("PSUPREMARKS").equals("1"))
							parameters.put("SupRemarks", sRemarks);
						else
							parameters.put("SupRemarks", "");
						parameters.put("DeliveryDate", (String) m.get("DELIVERYDATE"));
						if (orderRemarks.length() > 0)
							orderRemarks = (String) m.get("REMARK1") + " : " + orderRemarks;
						if (orderRemarks3.length() > 0)
							orderRemarks3 = (String) m.get("REMARK2") + " : " + orderRemarks3;
						// ship to Address
						if(ORDERDISCOUNTTYPE.equalsIgnoreCase("%"))
						parameters.put("lblOrderDiscount",
								(String) m.get("ORDERDISCOUNT") + " " + "(" + DATAORDERDISCOUNT + "%" + ") ("+ ISDISCOUNTTAX + ")");
						else
							parameters.put("lblOrderDiscount",
									(String) m.get("ORDERDISCOUNT") + " " + "(" + ORDERDISCOUNTTYPE + ") ("+ ISDISCOUNTTAX + ")");
						parameters.put("lblShippingCost",
								(String) m.get("SHIPPINGCOST") + " " + "(" + curDisplay + ") ("+ ISSHIPPINGTAX + ")");
						parameters.put("lblINCOTERM", (String) m.get("INCOTERM"));
						parameters.put("STo", (String) m.get("SHIPTO"));
						parameters.put("SHPCUSTOMERNAME", SHPCUSTOMERNAME);
						if (SHPCONTACTNAME.length() > 0) {
							parameters.put("SHPCONTACTNAME", "Attn: " + SHPCONTACTNAME);
						} else {
							parameters.put("SHPCONTACTNAME", SHPCONTACTNAME);
						}
						if (SHPTELEPHONE.length() > 0) {
							parameters.put("SHPTELEPHONE", "Tel: " + SHPTELEPHONE);
						} else {
							parameters.put("SHPTELEPHONE", SHPTELEPHONE);
						}
						if (SHPHANDPHONE.length() > 0) {
							parameters.put("SHPHANDPHONE", "HP: " + SHPHANDPHONE);
						} else {
							parameters.put("SHPHANDPHONE", SHPHANDPHONE);
						}
						parameters.put("SHPUNITNO", SHPUNITNO);
						parameters.put("SHPBUILDING", SHPBUILDING);
						parameters.put("SHPSTREET", SHPSTREET);
						parameters.put("SHPCITY", SHPCITY);
						parameters.put("SHPSTATE", SHPSTATE);
						parameters.put("SHPCOUNTRY", SHPCOUNTRY);
						parameters.put("SHPPOSTALCODE", SHPPOSTALCODE);
						parameters.put("DATAORDERDISCOUNT", doubledataorderdiscount);
						parameters.put("DATASHIPPINGCOST", doubledatashippingcost);
						parameters.put("DATAINCOTERMS", DATAINCOTERMS);
						if (m.get("PRINTEMPLOYEE").equals("1")) {// Author: Azees  Create date: July 10,2021  Description: Employee on Jasper
							parameters.put("Employee", (String) m.get("EMPLOYEE"));
							String empname = new EmployeeDAO().getEmpname(PLANT, sEmpno, "");
							parameters.put("EmployeeName", empname);
						} else {
							parameters.put("Employee", "");
							parameters.put("EmployeeName", "");
						}

						if (!checkImageFile.exists()) {
							imagePath2 = imagePath1;
							imagePath = "";
						} else if (!checkImageFile1.exists()) {
							imagePath2 = imagePath;
							imagePath1 = "";
						} else {
							imagePath2 = "";
						}
						parameters.put("PRINTWITHDELIVERYDATE",  m.get("PRINTWITHDELIVERYDATE"));
						parameters.put("PRODUCTDELIVERYDATE",  m.get("PRODUCTDELIVERYDATE"));
						parameters.put("PRINTWITHPRODUCTDELIVERYDATE",  m.get("PRINTWITHPRODUCTDELIVERYDATE"));
						parameters.put("imagePath", imagePath);
						//imti start seal,sign condition in printoutconfig
						String PRINTWITHCOMPANYSEAL = (String) m.get("PRINTWITHCOMPANYSEAL");
						String PRINTWITHCOMPANYSIG= (String) m.get("PRINTWITHCOMPANYSIG");
						if(PRINTWITHCOMPANYSEAL.equalsIgnoreCase("0")){
			                sealPath = DbBean.COMPANY_LOGO_PATH + "/" + DbBean.NO_LOGO_FILE;
			                }
						if(PRINTWITHCOMPANYSIG.equalsIgnoreCase("0")){
							signPath = DbBean.COMPANY_LOGO_PATH + "/" + DbBean.NO_LOGO_FILE;
			                }
						parameters.put("sealPath", sealPath);
						parameters.put("signPath", signPath);
						parameters.put("imagePath1", imagePath1);
						parameters.put("imagePath2", imagePath2);
						if (Orientation.equals("Portrait")) {

							jasperPath = DbBean.JASPER_INPUT + "/" + fileName+"Portrait";

						}

						parameters.put("Discount", (String) m.get("DISCOUNT"));
						parameters.put("NetRate", (String) m.get("NETRATE"));
						parameters.put("PreBy", (String) m.get("PREPAREDBY"));
						parameters.put("PreByUser", dataMap.get("CRBY").toString());
						parameters.put("AuthSign", (String) m.get("AUTHSIGNATURE"));
						ArrayList arrayposdao = new ArrayList();
						arrayposdao =new PoDetDAO().selectPoDet("distinct ITEM,ItemDesc,UNITCOST ", htCond);
						if(arrayposdao.size() > 3)
						{ }
						else
							parameters.put(JRParameter.IS_IGNORE_PAGINATION, true);
						if (m.get("PRINTWITHDISCOUNT").equals("1")) {
							jasperPath = DbBean.JASPER_INPUT + "/" + fileName+"WithDiscount";
							if (Orientation.equals("Portrait")) {
								jasperPath = DbBean.JASPER_INPUT + "/" + fileName +"WithDiscountPortrait";
							}
						} else {
							jasperPath = DbBean.JASPER_INPUT + "/" + fileName;
							if (Orientation.equals("Portrait")) {

								jasperPath = DbBean.JASPER_INPUT + "/" +fileName+ "Portrait";

							}
						}

						// jasperPath = DbBean.JASPER_INPUT + "/" + fileName;

					}
					parameters.put("orderRemarks", orderRemarks);
					parameters.put("orderRemarks3", orderRemarks3);
					System.out.println("orderRemarks : " + orderRemarks);
					System.out.println("orderRemarks3 : " + orderRemarks3);
					System.out.println("jasperPath : " + jasperPath);
					JasperCompileManager.compileReportToFile(jasperPath + ".jrxml", jasperPath + ".jasper");
					jasperPrintList.add((JasperFillManager.fillReport(jasperPath + ".jasper", parameters, con)));
				}
			}
			System.out.println("jasperPrintList count : " + jasperPrintList.size());
			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
			JRPdfExporter exporter = new JRPdfExporter();
			exporter.setParameter(JRExporterParameter.JASPER_PRINT_LIST, jasperPrintList);
			exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, byteArrayOutputStream);
			exporter.exportReport();
			/// inbound report data config code end
			
			byte[] bytes = byteArrayOutputStream.toByteArray();
			if (!ajax) {
				if ((String)request.getAttribute("ISAUTOMAIL") == null) {//for mail - Azees 03_2022
				//response.addHeader("Content-disposition", "attachment;filename=" + fileName + ".pdf");
				response.addHeader("Content-disposition", "inline;filename=" + fileName + ".pdf");
				response.setContentLength(bytes.length);
				response.getOutputStream().write(bytes);
				response.setContentType("application/pdf");
				response.getOutputStream().flush();
				response.getOutputStream().close();
				} else {					
					try(FileOutputStream fos = new FileOutputStream(DbBean.COMPANY_MAIL_ATTACHMENT_PATH + ("printPO".equals(fileName) ? "/Receiving_List_" : "/Invoice_") + PONO + ".pdf")){
						fos.write(bytes);
					}
				}
			}else {
//				System.out.println(jasperPath + ".pdf");
				
				try(FileOutputStream fos = new FileOutputStream(DbBean.COMPANY_MAIL_ATTACHMENT_PATH + ("printPO".equals(fileName) ? "/Receiving_List_" : "/Invoice_") + PONO + ".pdf")){
					fos.write(bytes);
				}
				
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DbBean.closeConnection(con);
		}
	}

	protected void viewESReport(HttpServletRequest request, HttpServletResponse response, String fileName) {

		String jasperPath = "";

		Connection con = null;
		
		boolean ajax = "XMLHttpRequest".equals(request.getHeader("X-Requested-With"));
		
		try {

			CustomerBeanDAO customerBeanDAO = new CustomerBeanDAO();
			PlantMstUtil pmUtil = new PlantMstUtil();
			selectBean sb = new selectBean();
//			POUtil pOUtil = new POUtil();
			PoEstUtil pOEstUtil = new PoEstUtil();
			CurrencyUtil currUtil = new CurrencyUtil();
			PlantMstUtil plantmstutil = new PlantMstUtil();
			sb.setmLogger(mLogger);
			customerBeanDAO.setmLogger(mLogger);
			pmUtil.setmLogger(mLogger);
//			pOUtil.setmLogger(mLogger);
			pOEstUtil.setmLogger(mLogger);

			HttpSession session = request.getSession();
			String CNAME = "", CADD1 = "", CADD2 = "", CADD3 = "", CADD4 = "", CZIP = "", CCOUNTRY = "", CTEL = "",
					CFAX = "", CONTACTNAME = "", CHPNO = "", CEMAIL = "", CRCBNO = "",companyregno="",SEALNAME="",SIGNNAME="", CSTATE = "",CWEBSITE="";
			String SHPCUSTOMERNAME = "", SHPCONTACTNAME = "", SHPTELEPHONE = "", SHPHANDPHONE = "", 
					SHPUNITNO = "", SHPBUILDING = "", SHPSTREET = "", SHPCITY = "", SHPSTATE = "",
					SHPCOUNTRY = "", SHPPOSTALCODE = "", DATAORDERDISCOUNT = "", DATASHIPPINGCOST = "",sEmpno = "",
					DATAINCOTERMS = "",ORDERDISCOUNTTYPE = "",TAX_TYPE="",ISDISCOUNTTAX="",ISSHIPPINGTAX="",PROJECT_NAME="",sales_location="";//SHPFAX = "",
			//AdjustmentAmount="",SHPEMAIL = "", 
					int TAXID=0;

			String POESTNO = null;
			List lstPoNo = new ArrayList();
			if (request.getParameter("lstPoNo") != null) {
				lstPoNo = Arrays.asList(request.getParameter("lstPoNo").split(","));
			} else if (request.getParameter("POESTNO") != null) {
				lstPoNo = Arrays.asList(request.getParameter("POESTNO").split(","));
			} else {
				lstPoNo = Arrays.asList(((String)request.getAttribute("POESTNO")).split(","));
			}
			String Plant = (String) session.getAttribute("PLANT");
			List viewlistQry = plantmstutil.getPlantMstDetails(Plant);
			
			 Map maps = (Map) viewlistQry.get(0);
			List jasperPrintList = new ArrayList();
			for (int lstPoNoIndex = 0; lstPoNoIndex < lstPoNo.size(); lstPoNoIndex++) {
				POESTNO = StrUtils.fString(lstPoNo.get(lstPoNoIndex).toString());
				System.out.println("POESTNO : " + POESTNO);
				String PLANT = (String) session.getAttribute("PLANT");
				String baseCurrency = (String) session.getAttribute("BASE_CURRENCY");
				String SHIPPINGID = StrUtils.fString(request.getParameter("SHIPPINGID"));
				String imagePath = DbBean.COMPANY_LOGO_PATH + "/" + PLANT.toLowerCase() + DbBean.LOGO_FILE;
	            SEALNAME=StrUtils.fString((String)maps.get("SEALNAME"));
                SIGNNAME=StrUtils.fString((String)maps.get("SIGNATURENAME"));
                String sealPath = "",signPath="";
                //imti get seal name from plantmst
                if(SEALNAME.equalsIgnoreCase("")){
                sealPath = DbBean.COMPANY_LOGO_PATH + "/" + DbBean.NO_LOGO_FILE;
                }else {
                	sealPath = DbBean.COMPANY_SEAL_PATH + "/" + SEALNAME;
                }
                if(SIGNNAME.equalsIgnoreCase("")){
                	signPath = DbBean.COMPANY_LOGO_PATH + "/" + DbBean.NO_LOGO_FILE;
                    }else {
                    	signPath = DbBean.COMPANY_SIGNATURE_PATH + "/" + SIGNNAME;
                    }
                //imti end
				String imagePath1 = DbBean.COMPANY_LOGO_PATH + "/" + PLANT.toLowerCase() + "Logo1.gif";
				String imagePath2, Orientation = "";
				PlantMstDAO plantMstDAO = new PlantMstDAO();
				String numberOfDecimal = plantMstDAO.getNumberOfDecimal(PLANT);
				File checkImageFile = new File(imagePath);
				if (!checkImageFile.exists()) {
					imagePath = DbBean.COMPANY_LOGO_PATH + "/" + DbBean.NO_LOGO_FILE;
				}
				File checkImageFile1 = new File(imagePath1);
				if (!checkImageFile1.exists()) {
					imagePath1 = DbBean.COMPANY_LOGO_PATH + "/" + DbBean.NO_LOGO_FILE;
				}

				con = DbBean.getConnection();

				Hashtable htCond = new Hashtable();
				htCond.put("PLANT", PLANT);
				htCond.put("POESTNO", POESTNO);
				String query = "isnull(jobNum,'') as jobNum,isnull(remark1,'') as remark1,isnull(EMPNO,'') as EMPNO,"
						+ "custCode,isnull(inbound_Gst,0) as inbound_Gst,isnull(collectionDate + ' ' + LEFT(CollectionTime, 2)+':'+RIGHT(CollectionTime, 2),'') as date,"
						+ "isnull(deldate,'') as deldate,isnull(EXPIREDATE,'') as expiredate,CASE WHEN isnull(ORDERDISCOUNTTYPE,'%')='%' THEN isnull(orderdiscount,0) ELSE (isnull(orderdiscount,0)*ISNULL(CURRENCYUSEQT,1)) END AS orderdiscount," 
						+ "isnull(ORDERDISCOUNTTYPE,'%') ORDERDISCOUNTTYPE,isnull(ISTAXINCLUSIVE,0) ISTAXINCLUSIVE,"
						+ "ISNULL(SHIPPINGCOST,0)*ISNULL(CURRENCYUSEQT,1) shippingcost,isnull(CRBY,'') as CRBY,isnull(PAYMENTTYPE,'') as PAYMENTTYPE,ISNULL(TAXID,0) TAXID," 
						+ "ISNULL((select DISTINCT TAXTYPE from FINCOUNTRYTAXTYPE B WHERE B.ID="+PLANT+"_poesthdr.TAXID),'') TAX_TYPE,"
						+ "ISNULL((select DISTINCT PROJECT_NAME from "+PLANT+"_FINPROJECT B WHERE B.ID="+PLANT+"_poesthdr.PROJECTID),'') PROJECT_NAME,"
						+ "ISNULL((select DISTINCT PREFIX from FINSALESLOCATION B WHERE B.STATE="+PLANT+"_poesthdr.PURCHASE_LOCATION),'') sales_location,"
						+ "isnull(incoterms,'') incoterms,ISNULL(ADJUSTMENT,0)*ISNULL(CURRENCYUSEQT,1) ADJUSTMENT,ISNULL(ISDISCOUNTTAX,0) ISDISCOUNTTAX,ISNULL(ISSHIPPINGTAX,0) ISSHIPPINGTAX";

				ArrayList arrayRemark1 = new ArrayList();
				arrayRemark1 = pOEstUtil.getPoEstHdrDetails(query, htCond);
				Map dataMap = (Map) arrayRemark1.get(0);

//				String SysDate = DateUtils.getDate();
//				Date sysTime1 = new Date(System.currentTimeMillis());
//				Date cDt = new Date(System.currentTimeMillis());
				ArrayList listQry = pmUtil.getPlantMstDetails(PLANT);

				for (int i = 0; i < listQry.size(); i++) {
					Map map = (Map) listQry.get(i);

					CNAME = (String) map.get("PLNTDESC");
					CADD1 = (String) map.get("ADD1");
					CADD2 = (String) map.get("ADD2");
					CADD3 = (String) map.get("ADD3");
					CADD4 = (String) map.get("ADD4");
					CCOUNTRY = (String) map.get("COUNTY");
					CZIP = (String) map.get("ZIP");
					CTEL = (String) map.get("TELNO");
					CFAX = (String) map.get("FAX");
					CONTACTNAME = (String) map.get("NAME");
					CHPNO = (String) map.get("HPNO");
					CEMAIL = (String) map.get("EMAIL");
					CRCBNO = (String) map.get("RCBNO");
					CSTATE = (String) map.get("STATE");
					CWEBSITE = (String) map.get("WEBSITE");
					companyregno = (String) map.get("companyregnumber");//imtiuen
					

				}

				ArrayList arrCust = customerBeanDAO.getVendorDetailsForPOEST(PLANT, POESTNO);
				if (arrCust.size() > 0) {
//					String sCustCode = (String) arrCust.get(0);
					String sCustName = (String) arrCust.get(1);
					String sAddr1 = (String) arrCust.get(2);
					String sAddr2 = (String) arrCust.get(3);
					String sAddr3 = (String) arrCust.get(4);
					String sCountry = (String) arrCust.get(5);
					String sZip = (String) arrCust.get(6);
//					String sCons = (String) arrCust.get(7);
					String sContactName = (String) arrCust.get(8);
//					String sDesgination = (String) arrCust.get(9);
					String sTelNo = (String) arrCust.get(10);
//					String sHpNo = (String) arrCust.get(11);
					String sEmail = (String) arrCust.get(12);
					String sFax = (String) arrCust.get(13);
					String sRemarks = (String) arrCust.get(14);
					String sAddr4 = (String) arrCust.get(15);
//					String sIsActive = (String) arrCust.get(16);
					String orderRemarks = (String) arrCust.get(18);
//					String sPayTerms = (String) arrCust.get(19);
					String orderRemarks3 = (String) arrCust.get(20);
					String State = (String) arrCust.get(22);
					SHIPPINGID = (String) arrCust.get(23);
					String sRcbno = (String) arrCust.get(24);
					String suenno = (String) arrCust.get(25);//imtiuen
					// get shipping details from shipping master table
					ArrayList arrShippingDetails = _masterUtil.getPurchaseEstimateShippingDetails(POESTNO, SHIPPINGID, PLANT);
//					ArrayList arrShippingDetails = _masterUtil.getEstimateShippingDetails(POESTNO, SHIPPINGID, PLANT);
//					ArrayList arrShippingDetails = _masterUtil.getInboundShippingDetails(POESTNO, SHIPPINGID, PLANT);
					Map parameters = new HashMap();
					parameters.put("numberOfDecimal", Integer.parseInt(numberOfDecimal));
					if (arrShippingDetails.size() > 0) {
						parameters.put("shipToId", SHIPPINGID);
						SHPCUSTOMERNAME = (String) arrShippingDetails.get(1);
						SHPCONTACTNAME = (String) arrShippingDetails.get(2);
						SHPTELEPHONE = (String) arrShippingDetails.get(3);
						SHPHANDPHONE = (String) arrShippingDetails.get(4);
//						SHPFAX = (String) arrShippingDetails.get(5);
//						SHPEMAIL = (String) arrShippingDetails.get(6);
						SHPUNITNO = (String) arrShippingDetails.get(7);
						SHPBUILDING = (String) arrShippingDetails.get(8);
						SHPSTREET = (String) arrShippingDetails.get(9);
						SHPCITY = (String) arrShippingDetails.get(10);
						SHPSTATE = (String) arrShippingDetails.get(11);
						SHPCOUNTRY = (String) arrShippingDetails.get(12);
						SHPPOSTALCODE = (String) arrShippingDetails.get(13);
					} else {
						parameters.put("shipToId", "");
					}

					String orderType = new PoEstHdrDAO().getOrderTypeForPO(PLANT, POESTNO);
					if ("yes".equals(request.getParameter("with_batch"))) {
						parameters.put("PrintBatch", "yes");
					}
					if ("yes".equals(request.getParameter("is_from_reports"))) {
						parameters.put("IsFromOrderManagement", "no");
					} else {
						parameters.put("IsFromOrderManagement", "yes");
					}
					// Customer Details
					parameters.put("imagePath", imagePath);
					parameters.put("imagePath1", imagePath1);
					parameters.put("inboundOrderNo", POESTNO);
					parameters.put("inboundOrderNo", POESTNO);
					parameters.put("orderRemarks", orderRemarks);
					parameters.put("company", PLANT);
					parameters.put("taxInvoiceTo_CompanyName", sCustName);
					if(fileName.equalsIgnoreCase("printPOInvoice")) {
					parameters.put("taxInvoiceTo_BlockAddress", sAddr1 + "  " + sAddr2);
					parameters.put("taxInvoiceTo_RoadAddress", sAddr3 + "  " + sAddr4);
					if (State.equals("")) {
						parameters.put("taxInvoiceTo_Country",sCountry);
					}else {
						parameters.put("taxInvoiceTo_Country",State+ "\n" +sCountry);
					}
//					parameters.put("taxInvoiceTo_Country", State + ", " + sCountry);
					} else {
						String taxInvoiceTo_BlockAddress="";
						if(sAddr2.length()>0)
							taxInvoiceTo_BlockAddress=sAddr2+ "  ";
						parameters.put("taxInvoiceTo_BlockAddress", taxInvoiceTo_BlockAddress + sAddr3);//1.Building+Street
						parameters.put("taxInvoiceTo_RoadAddress", sAddr1);//2.Uint No.
						if (State.equals("")) {
							parameters.put("taxInvoiceTo_Country",sCountry);
						}else {
							parameters.put("taxInvoiceTo_Country",State+ "\n" +sCountry);
						}
//						parameters.put("taxInvoiceTo_Country", sCountry);
					}
					parameters.put("taxInvoiceTo_ZIPCode", sZip);
					parameters.put("taxInvoiceTo_AttentionTo", sContactName);
					parameters.put("taxInvoiceTo_CCTO", "");
					parameters.put("taxInvoiceTo_Telno", sTelNo);
					parameters.put("taxInvoiceTo_Fax", sFax);
					parameters.put("taxInvoiceTo_Email", sEmail);
					parameters.put("sRCBNO", sRcbno);
					parameters.put("sUENNO", suenno);//imtiuen
//					PoHdrDAO poEstHdrDAO= new PoHdrDAO();
					PoEstHdrDAO poEstHdrDAO= new PoEstHdrDAO();
					PoEstHdr poheader = poEstHdrDAO.getPoHdrByPono(PLANT, POESTNO);
					TransportModeDAO transportmodedao = new TransportModeDAO();
					String transportmode = "";
					if(poheader.getTRANSPORTID() > 0){
						transportmode = transportmodedao.getTransportModeById(PLANT, poheader.getTRANSPORTID());
					}
					parameters.put("trans", transportmode);//imtiuen
					String paymentterms = "";
					paymentterms = poheader.getPAYMENT_TERMS();
					parameters.put("payterms", paymentterms);//imtiuen

					// Company Details
					parameters.put("fromAddress_CompanyName", CNAME);
					if(fileName.equalsIgnoreCase("printPOInvoice")) {
//						parameters.put("fromAddress_BlockAddress", CADD1 + "," + CADD2);
//						parameters.put("fromAddress_RoadAddress", CADD3 + "," + CADD4);
//						parameters.put("fromAddress_Country", CSTATE + ", " + CCOUNTRY);
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
							parameters.put("fromAddress_Country", CCOUNTRY);
						}else {
							parameters.put("fromAddress_Country", CSTATE + "," + CCOUNTRY);
						}
					} else {
					String fromAddress_BlockAddress="";
					if(CADD2.length()>0)
//						fromAddress_BlockAddress=CADD2 + ",";
//					parameters.put("fromAddress_BlockAddress", fromAddress_BlockAddress + CADD3); //1.Building+Street
//					parameters.put("fromAddress_RoadAddress", CADD1); //Unit No.
//					parameters.put("fromAddress_Country", CCOUNTRY);
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
					parameters.put("fromAddress_Website", CWEBSITE);
					parameters.put("fromAddress_RCBNO", CRCBNO);
					parameters.put("fromAddress_UENNO", companyregno);//imtiuen
					// Start code by Bruhan for getting refno in report instead of remarks on
					// 19/March/2013
					parameters.put("currentTime", (String) dataMap.get("date"));
					System.out.println("Referance No :" + (String) dataMap.get("jobNum"));
					parameters.put("referanceNO", (String) dataMap.get("jobNum"));
					// End code by Bruhan for getting refno in report instead of remarks on
					// 19/March/2013
					parameters.put("InvoiceTerms", "");
					// Included Delivery Date By Samatha
					String deldate = StrUtils.fString((String) dataMap.get("deldate"));
					String expiredate = StrUtils.fString((String) dataMap.get("expiredate"));
					DATAORDERDISCOUNT = dataMap.get("orderdiscount").toString();
					DATASHIPPINGCOST = dataMap.get("shippingcost").toString();
					DATAINCOTERMS = dataMap.get("incoterms").toString();
					sEmpno = dataMap.get("EMPNO").toString();
					
//					if (deldate.length() > 0)
//						deldate = deldate;
					parameters.put("DeliveryDt", deldate);
					parameters.put("ExpireDt", expiredate);
					// End Delivery Date By Samatha
					String inbgst = (String) dataMap.get("inbound_Gst");
					Double gst = new Double(inbgst);					
					/*gst = gst / 100;
					System.out.println("GST : " + gst);
					parameters.put("taxPercentage", gst.doubleValue());*/
					parameters.put("orderType", orderType);
					parameters.put("localCurrency", baseCurrency);
					//new changes - azees 12/2020 
					parameters.put("ISTAXINCLUSIVE",  dataMap.get("ISTAXINCLUSIVE").toString());
					ORDERDISCOUNTTYPE=dataMap.get("ORDERDISCOUNTTYPE").toString();
					PROJECT_NAME=dataMap.get("PROJECT_NAME").toString();
					sales_location=dataMap.get("sales_location").toString();
					parameters.put("ORDERDISCOUNTTYPE", ORDERDISCOUNTTYPE);
					parameters.put("AdjustmentAmount", dataMap.get("ADJUSTMENT").toString());
					parameters.put("ISDISCOUNTTAX", dataMap.get("ISDISCOUNTTAX").toString());
					parameters.put("ISSHIPPINGTAX", dataMap.get("ISSHIPPINGTAX").toString());
					parameters.put("PROJECTNAME", PROJECT_NAME);
					ISSHIPPINGTAX=dataMap.get("ISSHIPPINGTAX").toString();
					ISDISCOUNTTAX=dataMap.get("ISDISCOUNTTAX").toString();
					if(ISSHIPPINGTAX.equalsIgnoreCase("0"))
						ISSHIPPINGTAX="Tax Exclusive";
					else
						ISSHIPPINGTAX="Tax Inclusive";
					if(ISDISCOUNTTAX.equalsIgnoreCase("0"))
						ISDISCOUNTTAX="Tax Exclusive";
					else
						ISDISCOUNTTAX="Tax Inclusive";
					TAX_TYPE = dataMap.get("TAX_TYPE").toString();
					/*if(TAX_TYPE.equalsIgnoreCase("EXEMPT") || TAX_TYPE.equalsIgnoreCase("OUT OF SCOPE"))
						TAX_TYPE = StrUtils.fString(TAX_TYPE+" [0.0%]").trim();
					else
						TAX_TYPE = StrUtils.fString(TAX_TYPE+" ["+inbgst+"%]").trim();*/
					TAXID = Integer.valueOf(dataMap.get("TAXID").toString());
					FinCountryTaxType fintaxtype = new FinCountryTaxType();
					Short ptaxiszero=0,ptaxisshow=0;
					
					if(TAXID > 0){
						FinCountryTaxTypeDAO finCountryTaxTypeDAO = new FinCountryTaxTypeDAO();
						fintaxtype = finCountryTaxTypeDAO.getCountryTaxTypesByid(TAXID);
						ptaxiszero=fintaxtype.getISZERO();
						ptaxisshow=fintaxtype.getSHOWTAX();
					}
					if(ptaxiszero == 1 && ptaxisshow == 0){							
						TAX_TYPE = "";
						inbgst="0.0";
					} else if(ptaxiszero == 0 && ptaxisshow == 0){
						TAX_TYPE = "";
						inbgst="0.0";
					} else if(ptaxiszero == 0 && ptaxisshow == 1){						
						if(sales_location.equalsIgnoreCase(""))
							TAX_TYPE = StrUtils.fString(TAX_TYPE+" ["+inbgst+"%]").trim();
						else
							TAX_TYPE = StrUtils.fString(TAX_TYPE+"("+sales_location+") ["+inbgst+"%]").trim();
					} else {							
						
						if(sales_location.equalsIgnoreCase(""))
							TAX_TYPE = StrUtils.fString(TAX_TYPE+" [0.0%]").trim();
						else
							TAX_TYPE = StrUtils.fString(TAX_TYPE+"("+sales_location+") [0.0%]").trim();
						inbgst="0.0";//Author: Azees  Create date: August 05,2021  Description: zero tax issue
					}
	  				parameters.put("TAX_TYPE", TAX_TYPE);
	  				gst = new Double(inbgst);
	  				gst = gst / 100;
					System.out.println("GST : " + gst);
					parameters.put("taxPercentage", gst.doubleValue());
					
					// Get Currency ID
					String currencyid = new PoEstHdrDAO().getCurrencyID(PLANT, POESTNO);
					// Get Currency Display
					Hashtable curHash = new Hashtable();
					curHash.put(IDBConstants.PLANT, PLANT);
					curHash.put(IDBConstants.CURRENCYID, currencyid);
					String curDisplay = currUtil.getCurrencyID(curHash, "DISPLAY");
					OrderTypeDAO ODAO = new OrderTypeDAO();
					String orderDesc = "";
					double doubledataorderdiscount = new Double(DATAORDERDISCOUNT);
					double doubledatashippingcost = new Double(DATASHIPPINGCOST);

					// get data's for inbound report data config
					if (fileName.equals("printPOInvoice")||fileName.equals("printPEInvoiceKitchen")) {
//						PoEstUtil poEstUtil = new PoEstUtil();
						POUtil poEstUtil = new POUtil();
						Map m = poEstUtil.getPurchaseEstimateHdrDetails(PLANT);
						Orientation = (String) m.get("PrintOrientation");
						if (orderType.equals("INBOUND ORDER")) {
							orderDesc = (String) m.get("HDR1");
						} else {
							orderDesc = ODAO.getOrderTypeDesc(PLANT, orderType);
						}
						if (m.get("DISPLAYBYORDERTYPE").equals("1"))
							parameters.put("OrderHeader", orderDesc);
						else
						parameters.put("OrderHeader", (String) m.get("HDR1"));
						parameters.put("ToHeader", (String) m.get("HDR2"));
						parameters.put("FromHeader", (String) m.get("HDR3"));
						parameters.put("SHIPTO", (String) m.get("SHIPTO"));
						parameters.put("Date", (String) m.get("DATE"));
						parameters.put("OrderNo", (String) m.get("ORDERNO"));
						parameters.put("RefNo", (String) m.get("REFNO"));
						parameters.put("Terms", (String) m.get("TERMS"));
						if (m.get("PRINTSUPTERMS").equals("1")) {
							//parameters.put("TermsDetails", sPayTerms);
							parameters.put("TermsDetails", dataMap.get("PAYMENTTYPE").toString());
						} else {
							parameters.put("TermsDetails", (String) m.get("TERMSDETAILS"));
						}
						
						//AUTHOR : imthiyas  
						//DATE : June 28,2021
						//DESC : display transportmode and paymentterms in jasper
						parameters.put("trans", transportmode);
						parameters.put("payterms", paymentterms);
						parameters.put("PRINTWITHTRANSPORT_MODE", (String) m.get("PRINTWITHTRANSPORT_MODE"));
						parameters.put("TRANSPORT_MODE", (String) m.get("TRANSPORT_MODE"));
						parameters.put("TERMSDETAILS", (String) m.get("TERMSDETAILS"));
						//end
						
						
						parameters.put("SoNo", (String) m.get("SONO"));
						parameters.put("Item", (String) m.get("ITEM"));
						parameters.put("BarCode", (String) m.get("BARCODE"));
						parameters.put("Description", (String) m.get("DESCRIPTION"));
						parameters.put("OrderQty", (String) m.get("ORDERQTY"));
						parameters.put("UOM", (String) m.get("UOM"));
						parameters.put("Rate", (String) m.get("RATE"));
						parameters.put("TaxAmount", (String) m.get("TAXAMOUNT"));
						parameters.put("Amt", (String) m.get("AMT"));
						parameters.put("SubTotal", (String) m.get("SUBTOTAL") + " " + "(" + curDisplay + ")");
						String taxby = _PlantMstDAO.getTaxBy(PLANT);
						parameters.put("TAXBY", taxby);
						if (taxby.equalsIgnoreCase("BYORDER")) {
							parameters.put("TotalTax", (String) m.get("TOTALTAX") + " " + "(" + inbgst + "%" + ")");
						} else {
							parameters.put("TotalTax", (String) m.get("TOTALTAX"));
						}
						parameters.put("Total", (String) m.get("TOTAL") + " " + "(" + curDisplay + ")");
						
						String PRINTROUNDOFFTOTALWITHDECIMAL =  (String) m.get("PRINTROUNDOFFTOTALWITHDECIMAL");
						parameters.put("PrintRoundoffTotalwithDecimal", PRINTROUNDOFFTOTALWITHDECIMAL);
						parameters.put("RoundoffTotalwithDecimal", (String) m.get("ROUNDOFFTOTALWITHDECIMAL") + " " + "(" + curDisplay + ")");
						parameters.put("CALCULATETAXWITHSHIPPINGCOST",  m.get("CALCULATETAXWITHSHIPPINGCOST"));
						parameters.put("Adjustment",  m.get("ADJUSTMENT"));
						parameters.put("PROJECT",  m.get("PROJECT"));
						parameters.put("ISPROJECT",  m.get("PRINTWITHPROJECT"));
						parameters.put("PRINTUENNO",  m.get("PRINTWITHUENNO"));//imtiuen
						parameters.put("PRINTSUPPLIERUENNO",  m.get("PRINTWITHSUPPLIERUENNO"));//imtiuen
						parameters.put("PRODUCTRATESARE",  m.get("PRODUCTRATESARE"));
						parameters.put("Footer1", (String) m.get("F1"));
						parameters.put("Footer2", (String) m.get("F2"));
						parameters.put("Footer3", (String) m.get("F3"));
						parameters.put("Footer4", (String) m.get("F4"));
						parameters.put("Footer5", (String) m.get("F5"));
						parameters.put("Footer6", (String) m.get("F6"));
						parameters.put("Footer7", (String) m.get("F7"));
						parameters.put("Footer8", (String) m.get("F8"));
						parameters.put("Footer9", (String) m.get("F9"));
						parameters.put("PrdXtraDetails", (String) m.get("PRINTXTRADETAILS"));
						parameters.put("CompanyDate", (String) m.get("COMPANYDATE"));
						parameters.put("CompanyName", (String) m.get("COMPANYNAME"));
						parameters.put("CompanyStamp", (String) m.get("COMPANYSTAMP"));
						parameters.put("CompanySig", (String) m.get("COMPANYSIG"));
						parameters.put("PrintRecmLoc", (String) m.get("PRINTRECMLOC"));
						parameters.put("PRINTWITHBRAND", (String) m.get("PRINTWITHBRAND"));
						parameters.put("PRINTWITHPRODUCTREMARKS", (String) m.get("PRINTWITHPRODUCTREMARKS"));
						parameters.put("RCBNO", (String) m.get("RCBNO"));
						parameters.put("UENNO", (String) m.get("UENNO"));//imtiuen
						parameters.put("TOTALAFTERDISCOUNT", (String) m.get("TOTALAFTERDISCOUNT"));
						parameters.put("SUPPLIERRCBNO", (String) m.get("SUPPLIERRCBNO"));
						parameters.put("SUPPLIERUENNO", (String) m.get("SUPPLIERUENNO"));//imtiuen
						if (m.get("PSUPREMARKS").equals("1"))
							parameters.put("SupRemarks", sRemarks);
						else
							parameters.put("SupRemarks", "");
						parameters.put("DeliveryDate", (String) m.get("DELIVERYDATE"));
						parameters.put("ExpireDate", (String) m.get("EXPIREDATE"));
						if (orderRemarks.length() > 0)
							orderRemarks = (String) m.get("REMARK1") + " : " + orderRemarks;
						if (orderRemarks3.length() > 0)
							orderRemarks3 = (String) m.get("REMARK2") + " : " + orderRemarks3;
						// ship to Address
						if(ORDERDISCOUNTTYPE.equalsIgnoreCase("%"))
						parameters.put("lblOrderDiscount",
								(String) m.get("ORDERDISCOUNT") + " " + "(" + DATAORDERDISCOUNT + "%" + ") ("+ ISDISCOUNTTAX + ")");
						else
							parameters.put("lblOrderDiscount",
									(String) m.get("ORDERDISCOUNT") + " " + "(" + ORDERDISCOUNTTYPE + ") ("+ ISDISCOUNTTAX + ")");
						parameters.put("lblShippingCost",
								(String) m.get("SHIPPINGCOST") + " " + "(" + curDisplay + ") ("+ ISSHIPPINGTAX + ")");
						parameters.put("lblINCOTERM", (String) m.get("INCOTERM"));
						parameters.put("STo", (String) m.get("SHIPTO"));
						parameters.put("SHPCUSTOMERNAME", SHPCUSTOMERNAME);
						if (SHPCONTACTNAME.length() > 0) {
							parameters.put("SHPCONTACTNAME", "Attn : " + SHPCONTACTNAME);
						} else {
							parameters.put("SHPCONTACTNAME", SHPCONTACTNAME);
						}
						if (SHPTELEPHONE.length() > 0) {
							parameters.put("SHPTELEPHONE", "Tel : " + SHPTELEPHONE);
						} else {
							parameters.put("SHPTELEPHONE", SHPTELEPHONE);
						}
						if (SHPHANDPHONE.length() > 0) {
							parameters.put("SHPHANDPHONE", "HP : " + SHPHANDPHONE);
						} else {
							parameters.put("SHPHANDPHONE", SHPHANDPHONE);
						}
						parameters.put("SHPUNITNO", SHPUNITNO);
						parameters.put("SHPBUILDING", SHPBUILDING);
						parameters.put("SHPSTREET", SHPSTREET);
						parameters.put("SHPCITY", SHPCITY);
//						parameters.put("SHPSTATE", SHPSTATE);
						if (State.equals("")) {
							parameters.put("SHPCOUNTRY",SHPCOUNTRY);
						}else {
							parameters.put("SHPCOUNTRY",SHPSTATE+ "\n" +SHPCOUNTRY);
						}
//						parameters.put("SHPCOUNTRY", SHPCOUNTRY);
						parameters.put("SHPPOSTALCODE", SHPPOSTALCODE);
						parameters.put("DATAORDERDISCOUNT", doubledataorderdiscount);
						parameters.put("DATASHIPPINGCOST", doubledatashippingcost);
						parameters.put("DATAINCOTERMS", DATAINCOTERMS);
						if (m.get("PRINTEMPLOYEE").equals("1")) {// Author: Azees  Create date: July 10,2021  Description: Employee on Jasper
							parameters.put("Employee", (String) m.get("EMPLOYEE"));
							String empname = new EmployeeDAO().getEmpname(PLANT, sEmpno, "");
							parameters.put("EmployeeName", empname);
						} else {
							parameters.put("Employee", "");
							parameters.put("EmployeeName", "");
						}

						if (!checkImageFile.exists()) {
							imagePath2 = imagePath1;
							imagePath = "";
						} else if (!checkImageFile1.exists()) {
							imagePath2 = imagePath;
							imagePath1 = "";
						} else {
							imagePath2 = "";
						}
						parameters.put("PRINTWITHDELIVERYDATE",  m.get("PRINTWITHDELIVERYDATE"));
						parameters.put("PRODUCTDELIVERYDATE",  m.get("PRODUCTDELIVERYDATE"));
						parameters.put("PRINTWITHPRODUCTDELIVERYDATE",  m.get("PRINTWITHPRODUCTDELIVERYDATE"));
						parameters.put("PRINTWITHEXPIREDATE",  m.get("PRINTWITHEXPIREDATE"));
						parameters.put("imagePath", imagePath);
						//imti start seal,sign condition in printoutconfig
						String PRINTWITHCOMPANYSEAL = (String) m.get("PRINTWITHCOMPANYSEAL");
						String PRINTWITHCOMPANYSIG= (String) m.get("PRINTWITHCOMPANYSIG");
						if(PRINTWITHCOMPANYSEAL.equalsIgnoreCase("0")){
			                sealPath = DbBean.COMPANY_LOGO_PATH + "/" + DbBean.NO_LOGO_FILE;
			                }
						if(PRINTWITHCOMPANYSIG.equalsIgnoreCase("0")){
							signPath = DbBean.COMPANY_LOGO_PATH + "/" + DbBean.NO_LOGO_FILE;
			                }
						parameters.put("sealPath", sealPath);
						parameters.put("signPath", signPath);
						parameters.put("imagePath1", imagePath1);
						parameters.put("imagePath2", imagePath2);
						if (Orientation.equals("Portrait")) {

							jasperPath = DbBean.JASPER_INPUT + "/" + fileName+"Portrait";

						}

						parameters.put("Discount", (String) m.get("DISCOUNT"));
						parameters.put("NetRate", (String) m.get("NETRATE"));
						parameters.put("PreBy", (String) m.get("PREPAREDBY"));
						parameters.put("PreByUser", dataMap.get("CRBY").toString());
						parameters.put("AuthSign", (String) m.get("AUTHSIGNATURE"));
						ArrayList arrayposdao = new ArrayList();
						arrayposdao = _poDetDAO.selectPoEstDet("distinct ITEM,ItemDesc,UNITCOST ", htCond);
						if(arrayposdao.size() > 3)
						{ }
						else
							parameters.put(JRParameter.IS_IGNORE_PAGINATION, true);
						if (m.get("PRINTWITHDISCOUNT").equals("1")) {
							jasperPath = DbBean.JASPER_INPUT + "/" + fileName+"WithDiscount";
							if (Orientation.equals("Portrait")) {
								jasperPath = DbBean.JASPER_INPUT + "/" + fileName +"WithDiscountPortrait";
							}
						} else {
							jasperPath = DbBean.JASPER_INPUT + "/" + fileName;
							if (Orientation.equals("Portrait")) {

								jasperPath = DbBean.JASPER_INPUT + "/" +fileName+ "Portrait";

							}
						}

						// jasperPath = DbBean.JASPER_INPUT + "/" + fileName;

					}
					parameters.put("orderRemarks", orderRemarks);
					parameters.put("orderRemarks3", orderRemarks3);
					System.out.println("orderRemarks : " + orderRemarks);
					System.out.println("orderRemarks3 : " + orderRemarks3);
					System.out.println("jasperPath : " + jasperPath);
					JasperCompileManager.compileReportToFile(jasperPath + ".jrxml", jasperPath + ".jasper");
					jasperPrintList.add((JasperFillManager.fillReport(jasperPath + ".jasper", parameters, con)));
				}
			}
			System.out.println("jasperPrintList count : " + jasperPrintList.size());
			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
			JRPdfExporter exporter = new JRPdfExporter();
			exporter.setParameter(JRExporterParameter.JASPER_PRINT_LIST, jasperPrintList);
			exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, byteArrayOutputStream);
			exporter.exportReport();
			/// inbound report data config code end
			
			byte[] bytes = byteArrayOutputStream.toByteArray();
			if (!ajax) {
				//response.addHeader("Content-disposition", "attachment;filename=" + fileName + ".pdf");
				response.addHeader("Content-disposition", "inline;filename=" + fileName + ".pdf");
				response.setContentLength(bytes.length);
				response.getOutputStream().write(bytes);
				response.setContentType("application/pdf");
				response.getOutputStream().flush();
				response.getOutputStream().close();				
			}else {
//				System.out.println(jasperPath + ".pdf");
				
				try(FileOutputStream fos = new FileOutputStream(DbBean.COMPANY_MAIL_ATTACHMENT_PATH + ("printPO".equals(fileName) ? "/Receiving_List_" : "/Invoice_") + POESTNO + ".pdf")){
					fos.write(bytes);
				}
				
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DbBean.closeConnection(con);
		}
	}
	// start code by Bruhan for export to excel inbound order details on 13 sep
	// 2013
	private HSSFWorkbook writeToExcel(HttpServletRequest request, HttpServletResponse response, HSSFWorkbook wb) {
		String plant = "";
		ArrayList listQry = new ArrayList();
		try {
			HttpSession session = request.getSession();
			plant = session.getAttribute("PLANT").toString();
			String PONO = StrUtils.fString(request.getParameter("PONO")).trim();
			// String CUSTNAME = StrUtils.fString(request.getParameter("CUST_NAME")).trim();
			String DIRTYPE = StrUtils.fString(request.getParameter("DIRTYPE"));
			Hashtable htCond = new Hashtable();
			htCond.put("A.PLANT", plant);
			htCond.put("A.PONO", PONO);
			/* Start by Abhilash on 25/09/2019 */
			PlantMstDAO plantMstDAO = new PlantMstDAO();
			String numberOfDecimal = plantMstDAO.getNumberOfDecimal(plant);
			/* End by Abhilash on 25/09/2019 */
			
			listQry = htutil.getOrderDetailsExportList(htCond, DIRTYPE, plant);

			if (listQry.size() > 0) {
				HSSFSheet sheet = null;
				HSSFCellStyle styleHeader = null;
				HSSFCellStyle dataStyle = null;
				sheet = wb.createSheet("Sheet1");
				styleHeader = createStyleHeader(wb);
				sheet = this.createWidth(sheet);
				sheet = this.createHeader(sheet, styleHeader);

				for (int iCnt = 0; iCnt < listQry.size(); iCnt++) {
					Map lineArr = (Map) listQry.get(iCnt);
					int k = 0;
					String collectiondate = StrUtils.fString((String) lineArr.get("collectiondate"));
					if (collectiondate.length() > 0) {
						collectiondate = collectiondate.substring(3, 5) + "/" + collectiondate.substring(0, 2) + "/"
								+ collectiondate.substring(6);
						// DateFormat df = new SimpleDateFormat("MM/dd/YYYY");
						// collectiondate = df.format(collectiondate);
					}

					dataStyle = createDataStyle(wb);
					HSSFRow row = sheet.createRow(iCnt + 1);

					HSSFCell cell = row.createCell(k++);
					cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String) lineArr.get("pono"))));
					cell.setCellStyle(dataStyle);

					cell = row.createCell(k++);
					cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String) lineArr.get("polnno"))));
					cell.setCellStyle(dataStyle);

					cell = row.createCell(k++);
					cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String) lineArr.get("jobnum"))));
					cell.setCellStyle(dataStyle);

					cell = row.createCell(k++);
					cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String) lineArr.get("ordertype"))));
					cell.setCellStyle(dataStyle);

					cell = row.createCell(k++);
					cell.setCellValue(new HSSFRichTextString(collectiondate));
					cell.setCellStyle(dataStyle);

					cell = row.createCell(k++);
					cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String) lineArr.get("collectiontime"))));
					cell.setCellStyle(dataStyle);

					HSSFCellStyle numericCellStyle = createDataStyle(wb);
					numericCellStyle.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
					
					cell = row.createCell(k++);
					cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String) lineArr.get("remarks"))));
					cell.setCellStyle(dataStyle);
					cell = row.createCell(k++);
					cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String) lineArr.get("remarks3"))));
					cell.setCellStyle(dataStyle);

					cell = row.createCell(k++);
					cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String) lineArr.get("inbound_gst"))));
					cell.setCellStyle(dataStyle);

					cell = row.createCell(k++);
					cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String) lineArr.get("custcode"))));
					cell.setCellStyle(dataStyle);

					cell = row.createCell(k++);
					cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String) lineArr.get("paymenttype"))));
					cell.setCellStyle(dataStyle);
					
					cell = row.createCell(k++);
					cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String) lineArr.get("item"))));
					cell.setCellStyle(dataStyle);

					cell = row.createCell(k++);
					cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String) lineArr.get("manufacturer"))));
					cell.setCellStyle(dataStyle);

					cell = row.createCell(k++);
					// cell.setCellType(cell.CELL_TYPE_NUMERIC);
					cell.setCellValue(new HSSFRichTextString(StrUtils.fString(Numbers.toMillionFormat((String) lineArr.get("qtyor"),Integer.valueOf(numberOfDecimal)))));
					cell.setCellStyle(numericCellStyle);

					cell = row.createCell(k++);
					cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String) lineArr.get("unitmo"))));
					cell.setCellStyle(numericCellStyle);

					// By Samatha on 20/09/2013
					String ConvertedUnitCost = new PoHdrDAO().getUnitCostBasedOnCurIDSelected(plant, PONO,
							(String) lineArr.get("polnno"), (String) lineArr.get("item"));
					// String cost = StrUtils.currencyWtoutSymbol(ConvertedUnitCost);
					
					/* Start by Abhilash on 25/09/2019 */
					double dCost = Double.parseDouble(ConvertedUnitCost);
					ConvertedUnitCost = Numbers.toMillionFormat(dCost, numberOfDecimal);
					/* End by Abhilash on 25/09/2019 */

					cell = row.createCell(k++);
					cell.setCellValue(new HSSFRichTextString(StrUtils.fString(ConvertedUnitCost)));
					//cell.setCellValue(Double.parseDouble(StrUtils.fString(ConvertedUnitCost)));
					cell.setCellStyle(numericCellStyle);

					cell = row.createCell(k++);
					cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String) lineArr.get("currencyid"))));
					cell.setCellStyle(dataStyle);

					cell = row.createCell(k++);
					cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String) lineArr.get("itemdesc"))));
					cell.setCellStyle(dataStyle);
					

					cell = row.createCell(k++);
					cell.setCellValue(new HSSFRichTextString(StrUtils.fString(Numbers.toMillionFormat((String) lineArr.get("orderdiscount"),Integer.valueOf(numberOfDecimal)))));
					cell.setCellStyle(numericCellStyle);
					
					/* Start by Abhilash on 25/09/2019 */
					double dShippingCost = Double.parseDouble((String) lineArr.get("shippingcost"));
					String shippingCost = Numbers.toMillionFormat(dShippingCost, numberOfDecimal);
					/* End by Abhilash on 25/09/2019 */
					
					cell = row.createCell(k++);
					cell.setCellValue(new HSSFRichTextString(StrUtils.fString(shippingCost)));
					cell.setCellStyle(numericCellStyle);
					
					/* Start by Abhilash on 28/09/2019 */
					double dlocalexpenses = Double.parseDouble((String) lineArr.get("localexpenses"));
					String localExpenses = StrUtils.addZeroes(dlocalexpenses, numberOfDecimal);
					/* End by Abhilash on 28/09/2019 */
					
					cell = row.createCell(k++);
					cell.setCellValue(new HSSFRichTextString(StrUtils.fString(localExpenses)));
					cell.setCellStyle(dataStyle);

					cell = row.createCell(k++);
					cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String) lineArr.get("incoterms"))));
					cell.setCellStyle(dataStyle);
					

					cell = row.createCell(k++);
					cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String) lineArr.get("shippingcustomer"))));
					cell.setCellStyle(dataStyle);
					
					cell = row.createCell(k++);
					cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String) lineArr.get("PURCHASE_LOCATION"))));
					cell.setCellStyle(dataStyle);
					
					cell = row.createCell(k++);
					cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String) lineArr.get("TAXTREATMENT"))));
					cell.setCellStyle(dataStyle);
					
					cell = row.createCell(k++);
					cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String) lineArr.get("REVERSECHARGE"))));
					cell.setCellStyle(dataStyle);
					
					cell = row.createCell(k++);
					cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String) lineArr.get("GOODSIMPORT"))));
					cell.setCellStyle(dataStyle);

				}

			} else if (listQry.size() < 1) {

				System.out.println("No Records Found To List");
			}

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		}

		return wb;
	}

	private HSSFSheet createHeader(HSSFSheet sheet, HSSFCellStyle styleHeader) {
		int k = 0;
		try {

			HSSFRow rowhead = sheet.createRow(0);
			HSSFCell cell = rowhead.createCell(k++);
			cell.setCellValue(new HSSFRichTextString("PONO"));
			cell.setCellStyle(styleHeader);

			cell = rowhead.createCell(k++);
			cell.setCellValue(new HSSFRichTextString("POLNNO"));
			cell.setCellStyle(styleHeader);

			cell = rowhead.createCell(k++);
			cell.setCellValue(new HSSFRichTextString("Ref No"));
			cell.setCellStyle(styleHeader);

			cell = rowhead.createCell(k++);
			cell.setCellValue(new HSSFRichTextString("OrderType"));
			cell.setCellStyle(styleHeader);

			cell = rowhead.createCell(k++);
			cell.setCellValue(new HSSFRichTextString("Order Date"));
			cell.setCellStyle(styleHeader);

			cell = rowhead.createCell(k++);
			cell.setCellValue(new HSSFRichTextString("Time"));
			cell.setCellStyle(styleHeader);

			cell = rowhead.createCell(k++);
			cell.setCellValue(new HSSFRichTextString("Remarks1"));
			cell.setCellStyle(styleHeader);
			cell = rowhead.createCell(k++);
			cell.setCellValue(new HSSFRichTextString("Remarks2"));
			cell.setCellStyle(styleHeader);

			cell = rowhead.createCell(k++);
			cell.setCellValue(new HSSFRichTextString("Purchase VAT"));
			cell.setCellStyle(styleHeader);

			cell = rowhead.createCell(k++);
			cell.setCellValue(new HSSFRichTextString("Supplier ID"));
			cell.setCellStyle(styleHeader);
			
			cell = rowhead.createCell(k++);
			cell.setCellValue(new HSSFRichTextString("Payment Type"));
			cell.setCellStyle(styleHeader);

			cell = rowhead.createCell(k++);
			cell.setCellValue(new HSSFRichTextString("Product ID"));
			cell.setCellStyle(styleHeader);

			cell = rowhead.createCell(k++);
			cell.setCellValue(new HSSFRichTextString("Manufacturer"));
			cell.setCellStyle(styleHeader);

			cell = rowhead.createCell(k++);
			cell.setCellValue(new HSSFRichTextString("Quantity"));
			cell.setCellStyle(styleHeader);

			cell = rowhead.createCell(k++);
			cell.setCellValue(new HSSFRichTextString("Uom"));
			cell.setCellStyle(styleHeader);

			cell = rowhead.createCell(k++);
			cell.setCellValue(new HSSFRichTextString("Unit Cost"));
			cell.setCellStyle(styleHeader);

			cell = rowhead.createCell(k++);
			cell.setCellValue(new HSSFRichTextString("Currency ID"));
			cell.setCellStyle(styleHeader);

			cell = rowhead.createCell(k++);
			cell.setCellValue(new HSSFRichTextString("Product Description"));
			cell.setCellStyle(styleHeader);
			
			cell = rowhead.createCell(k++);
			cell.setCellValue(new HSSFRichTextString("Order Discount"));
			cell.setCellStyle(styleHeader);
			
			cell = rowhead.createCell(k++);
			cell.setCellValue(new HSSFRichTextString("Shipping Cost"));
			cell.setCellStyle(styleHeader);
			
			cell = rowhead.createCell(k++);
			cell.setCellValue(new HSSFRichTextString("Local Expenses"));
			cell.setCellStyle(styleHeader);
			
			cell = rowhead.createCell(k++);
			cell.setCellValue(new HSSFRichTextString("Incoterm"));
			cell.setCellStyle(styleHeader);
			
			cell = rowhead.createCell(k++);
			cell.setCellValue(new HSSFRichTextString("Shipping Customer"));
			cell.setCellStyle(styleHeader);
			
			cell = rowhead.createCell(k++);
			cell.setCellValue(new HSSFRichTextString("Purchase Location"));
			cell.setCellStyle(styleHeader);
			
			cell = rowhead.createCell(k++);
			cell.setCellValue(new HSSFRichTextString("Tax Treatment"));
			cell.setCellStyle(styleHeader);
			
			cell = rowhead.createCell(k++);
			cell.setCellValue(new HSSFRichTextString("Reverse Charge"));
			cell.setCellStyle(styleHeader);
			
			cell = rowhead.createCell(k++);
			cell.setCellValue(new HSSFRichTextString("Goods Import"));
			cell.setCellStyle(styleHeader);

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		}
		return sheet;
	}

	private HSSFSheet createWidth(HSSFSheet sheet) {

		try {
			sheet.setColumnWidth(0, 5000);
			sheet.setColumnWidth(1, 3000);
			sheet.setColumnWidth(2, 7000);
			sheet.setColumnWidth(3, 6000);
			sheet.setColumnWidth(4, 3000);
			sheet.setColumnWidth(5, 3000);
			sheet.setColumnWidth(6, 10000);
			sheet.setColumnWidth(7, 10000);
			sheet.setColumnWidth(8, 3000);
			sheet.setColumnWidth(9, 8000);
			sheet.setColumnWidth(10, 10000);
			sheet.setColumnWidth(11, 5000);
			sheet.setColumnWidth(12, 8000);
			sheet.setColumnWidth(13, 3000);
			sheet.setColumnWidth(14, 3000);
			sheet.setColumnWidth(15, 3000);
			sheet.setColumnWidth(16, 3000);
			sheet.setColumnWidth(17, 12000);
			sheet.setColumnWidth(18, 3700);
			sheet.setColumnWidth(19, 3500);
			sheet.setColumnWidth(20, 7000);
			sheet.setColumnWidth(21, 7000);

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		}
		return sheet;
	}

	// ---Added by Bruhan on May 22 2014, Description:To open Inbound order summary in
	// excel powershell format
	private HSSFWorkbook writeToExcelInboundOrderSummary(HttpServletRequest request, HttpServletResponse response,
			HSSFWorkbook wb) {
//		StrUtils strUtils = new StrUtils();
		HTReportUtil movHisUtil = new HTReportUtil();
//		DateUtils _dateUtils = new DateUtils();
		ArrayList movQryList = new ArrayList();
		int maxRowsPerSheet = 65535;
		String fdate = "", tdate = "", taxby = "";
		int SheetId =1;
		try {
			String PLANT = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
			String ITEMNO = StrUtils.fString(request.getParameter("ITEM"));
			String PRD_DESCRIP = StrUtils.fString(request.getParameter("DESC"));
			String FROM_DATE = StrUtils.fString(request.getParameter("FROM_DATE"));
			String TO_DATE = StrUtils.fString(request.getParameter("TO_DATE"));
			String CUSTNAME = StrUtils.fString(request.getParameter("CUSTOMER"));
			String CUSTOMERID = StrUtils.fString(request.getParameter("CUSTOMERID"));
			String ORDERNO = StrUtils.fString(request.getParameter("ORDERNO"));
			String JOBNO = StrUtils.fString(request.getParameter("JOBNO"));
			String STATUS = StrUtils.fString(request.getParameter("STATUS"));
			String ORDERTYPE = StrUtils.fString(request.getParameter("ORDERTYPE"));
			String PRD_TYPE_ID = StrUtils.fString(request.getParameter("PRD_TYPE_ID"));
			String PRD_BRAND_ID = StrUtils.fString(request.getParameter("PRD_BRAND_ID"));
			String PRD_CLS_ID = StrUtils.fString(request.getParameter("PRD_CLS_ID"));
			String PRD_DEPT_ID = StrUtils.fString(request.getParameter("PRD_DEPT_ID"));//Resvi
//			String statusID = StrUtils.fString(request.getParameter("STATUS_ID"));
//			String SUPPLIERTYPE=StrUtils.fString(request.getParameter("SUPPLIERTYPE"));
			String GRNO = StrUtils.fString(request.getParameter("GRNO"));
			String type = StrUtils.fString(request.getParameter("DIRTYPE"));
			String VIEWSTATUS = StrUtils.fString(request.getParameter("VIEWSTATUS"));
			String LOCALEXPENSES=StrUtils.fString(request.getParameter("LOCALEXPENSES"));
			
			String  CURRENCYID      = StrUtils.fString(request.getParameter("CURRENCYID"));
	        String  CURRENCYDISPLAY = StrUtils.fString(request.getParameter("CURRENCYDISPLAY"));
	        String  CURRENCYUSEQT = StrUtils.fString(request.getParameter("currencyuseqt"));
	        
			String DELDATE=StrUtils.fString(request.getParameter("DELDATE"));
			String UOM=StrUtils.fString(request.getParameter("UOM"));
			String POSSEARCH = StrUtils.fString(request.getParameter("POSSEARCH"));
			
            if(CURRENCYID.equals(""))
            {
                     CurrencyDAO  _CurrencyDAO  = new CurrencyDAO();
                     List listQry = _CurrencyDAO.getCurrencyListWithcurrencySeq(PLANT,CURRENCYDISPLAY);
                     for(int i =0; i<listQry.size(); i++) {
                         Map m=(Map)listQry.get(i);
                         CURRENCYID=(String)m.get("currencyid");
                         CURRENCYUSEQT=(String)m.get("CURRENCYUSEQT");
                     }
            }
            
			//imti start
			if(STATUS.equalsIgnoreCase("OPEN"))
				STATUS="N";
			else if(STATUS.equalsIgnoreCase("PARTIALLY RECEIVED"))
				STATUS="O";
			else if(STATUS.equalsIgnoreCase("RECEIVED"))
				STATUS="C";
			//imti end
			
			String reportType ="";
			if (FROM_DATE == null)
				FROM_DATE = "";
			else
				FROM_DATE = FROM_DATE.trim();
			String curDate = DateUtils.getDate();
			if (FROM_DATE.length() < 0 || FROM_DATE == null || FROM_DATE.equalsIgnoreCase(""))
				FROM_DATE = curDate;
			if (FROM_DATE.length() > 5)
				fdate = FROM_DATE.substring(6) + "-" + FROM_DATE.substring(3, 5) + "-" + FROM_DATE.substring(0, 2);
			if (TO_DATE == null)
				TO_DATE = "";
			else
				TO_DATE = TO_DATE.trim();
			if (TO_DATE.length() > 5)
				tdate = TO_DATE.substring(6) + "-" + TO_DATE.substring(3, 5) + "-" + TO_DATE.substring(0, 2);
			Hashtable ht = new Hashtable();
			if (type.equals("INBOUND")) {

				if (StrUtils.fString(JOBNO).length() > 0)
					ht.put("A.JOBNUM", JOBNO);
				if (StrUtils.fString(ITEMNO).length() > 0)
					ht.put("B.ITEM", ITEMNO);
				if (StrUtils.fString(ORDERNO).length() > 0)
					ht.put("B.PONO", ORDERNO);
				if (StrUtils.fString(CUSTOMERID).length() > 0)
					ht.put("A.CUSTCODE", CUSTOMERID);
				if (StrUtils.fString(STATUS).length() > 0)
				{	
				if(STATUS.equalsIgnoreCase("DRAFT"))
	        	{
	        		ht.put("ORDER_STATUS","DRAFT");
	        		ht.put("B.LNSTAT","N");	
	        	}
	        	else
	        	{
	        		ht.put("B.LNSTAT", STATUS);
	        	   
	        	   if(STATUS.equalsIgnoreCase("N"))
	           		ht.put("ORDER_STATUS","OPEN");
	        	}
				}
				if (StrUtils.fString(ORDERTYPE).length() > 0)
					ht.put("A.ORDERTYPE", ORDERTYPE);
				if (StrUtils.fString(PRD_TYPE_ID).length() > 0)
					ht.put("C.ITEMTYPE", PRD_TYPE_ID);
				if (StrUtils.fString(PRD_BRAND_ID).length() > 0)
					ht.put("C.PRD_BRAND_ID", PRD_BRAND_ID);
				if (StrUtils.fString(PRD_CLS_ID).length() > 0)
					ht.put("C.PRD_CLS_ID", PRD_CLS_ID);
				if (StrUtils.fString(PRD_DEPT_ID).length() > 0)
					ht.put("C.PRD_DEPT_ID", PRD_DEPT_ID);
				if (StrUtils.fString(UOM).length() > 0)
					ht.put("B.SKTUOM", UOM);
				
				movQryList = movHisUtil.getWorkOrderSummaryList(ht, fdate, tdate, type, PLANT, PRD_DESCRIP, CUSTNAME,
						VIEWSTATUS,UOM,POSSEARCH);

			} else if (type.equals("IB_SUMMARY_ORD_WITH_COST")) {
				
				if (StrUtils.fString(JOBNO).length() > 0)
					ht.put("A.JOBNUM", JOBNO);
				if (StrUtils.fString(ITEMNO).length() > 0)
					ht.put("B.ITEM", ITEMNO);
				if (StrUtils.fString(ORDERNO).length() > 0)
					ht.put("A.PONO", ORDERNO);
				if (StrUtils.fString(STATUS).length() > 0)
				{	
				if(STATUS.equalsIgnoreCase("DRAFT"))
	        	{
	        		ht.put("ORDER_STATUS","DRAFT");
	        		ht.put("B.LNSTAT","N");	
	        	}
	        	else
	        	{
	        		ht.put("B.LNSTAT", STATUS);
	        	   
	        	   if(STATUS.equalsIgnoreCase("N"))
	           		ht.put("ORDER_STATUS","OPEN");
	        	}
				}
				if (StrUtils.fString(ORDERTYPE).length() > 0)
					ht.put("A.ORDERTYPE", ORDERTYPE);
				if (StrUtils.fString(PRD_TYPE_ID).length() > 0)
					ht.put("C.ITEMTYPE", PRD_TYPE_ID);
				if (StrUtils.fString(PRD_BRAND_ID).length() > 0)
					ht.put("C.PRD_BRAND_ID", PRD_BRAND_ID);
				if (StrUtils.fString(PRD_CLS_ID).length() > 0)
					ht.put("C.PRD_CLS_ID", PRD_CLS_ID);
				if (StrUtils.fString(PRD_DEPT_ID).length() > 0)
					ht.put("C.PRD_DEPT_ID", PRD_DEPT_ID);
				if (StrUtils.fString(DELDATE).length() > 0)
					ht.put("A.DELDATE", DELDATE);
				if (StrUtils.fString(UOM).length() > 0)
					ht.put("B.UNITMO", UOM);
			
				taxby = _PlantMstDAO.getTaxBy(PLANT);
				if (LOCALEXPENSES.equalsIgnoreCase("1"))
		        {
					if (taxby.equalsIgnoreCase("BYORDER")) {
						movQryList = movHisUtil.getSupplierPOInvoiceLocalExpSummary(ht, fdate, tdate, type, PLANT, PRD_DESCRIP,
								CUSTNAME, VIEWSTATUS);
					} else {
						movQryList = movHisUtil.getSupplierPOInvoiceLocalExpSummaryByProductGst(ht, fdate, tdate, type, PLANT,
								PRD_DESCRIP, CUSTNAME, VIEWSTATUS);
					}
		        }
				else
				{
					if (taxby.equalsIgnoreCase("BYORDER")) {
						movQryList = movHisUtil.getSupplierPOInvoiceSummary(ht, fdate, tdate, type, PLANT, PRD_DESCRIP,
								CUSTNAME, VIEWSTATUS);
					} else {
						movQryList = movHisUtil.getSupplierPOInvoiceSummaryByProductGst(ht, fdate, tdate, type, PLANT,
								PRD_DESCRIP, CUSTNAME, VIEWSTATUS);
					}					
				}
			} else if (type.equals("IB_SUMMARY_RECV_WITH_COST")) {
				if (StrUtils.fString(JOBNO).length() > 0)
					ht.put("JOBNUM", JOBNO);
				if (StrUtils.fString(ITEMNO).length() > 0)
					ht.put("ITEM", ITEMNO);
				if (StrUtils.fString(ORDERNO).length() > 0)
					ht.put("PONO", ORDERNO);
				if (StrUtils.fString(GRNO).length() > 0)
					ht.put("GRNO", GRNO);
				if (StrUtils.fString(STATUS).length() > 0)
					ht.put("STATUS", STATUS);
				if (StrUtils.fString(ORDERTYPE).length() > 0)
					ht.put("ORDERTYPE", ORDERTYPE);
				if (StrUtils.fString(PRD_TYPE_ID).length() > 0)
					ht.put("ITEMTYPE", PRD_TYPE_ID);
				if (StrUtils.fString(PRD_BRAND_ID).length() > 0)
					ht.put("PRD_BRAND_ID", PRD_BRAND_ID);
				if (StrUtils.fString(PRD_CLS_ID).length() > 0)
					ht.put("PRD_CLS_ID", PRD_CLS_ID);
				if (StrUtils.fString(PRD_DEPT_ID).length() > 0)
					ht.put("PRD_DEPT_ID", PRD_DEPT_ID);
				
				taxby = _PlantMstDAO.getTaxBy(PLANT);
				if (LOCALEXPENSES.equalsIgnoreCase("1")){
					 if (taxby.equalsIgnoreCase("BYORDER")) {
							movQryList = movHisUtil.getReportIBSummaryDetailsByCostlocalexpenses(ht, fdate, tdate, type, PLANT, PRD_DESCRIP,
									CUSTNAME);
						} else {
							movQryList = movHisUtil.getReportIBSummaryDetailsByCostByProductGstlocalexpenses(ht, fdate, tdate, type, PLANT,
									PRD_DESCRIP, CUSTNAME);
						}	 
					 
				 }
				 
				 else{
					 if (taxby.equalsIgnoreCase("BYORDER")) {
							movQryList = movHisUtil.getReportIBSummaryDetailsByCost(ht, fdate, tdate, type, PLANT, PRD_DESCRIP,
									CUSTNAME);
						} else {
							movQryList = movHisUtil.getReportIBSummaryDetailsByCostByProductGst(ht, fdate, tdate, type, PLANT,
									PRD_DESCRIP, CUSTNAME);
						}	 
					 
				 }
				
			} else if (type.equals("IB_SUMMARY_RECV")) {
				if (StrUtils.fString(JOBNO).length() > 0)
					ht.put("JOBNUM", JOBNO);
				if (StrUtils.fString(ITEMNO).length() > 0)
					ht.put("ITEM", ITEMNO);
				if (StrUtils.fString(ORDERNO).length() > 0)
					ht.put("PONO", ORDERNO);
				if (StrUtils.fString(STATUS).length() > 0)
					ht.put("STATUS", STATUS);
				if (StrUtils.fString(ORDERTYPE).length() > 0)
					ht.put("ORDERTYPE", ORDERTYPE);
				if (StrUtils.fString(PRD_TYPE_ID).length() > 0)
					ht.put("ITEMTYPE", PRD_TYPE_ID);
				if (StrUtils.fString(PRD_BRAND_ID).length() > 0)
					ht.put("PRD_BRAND_ID", PRD_BRAND_ID);
				if (StrUtils.fString(PRD_CLS_ID).length() > 0)
					ht.put("PRD_CLS_ID", PRD_CLS_ID);
				if (StrUtils.fString(PRD_DEPT_ID).length() > 0)
					ht.put("PRD_DEPT_ID", PRD_DEPT_ID);
				movQryList = movHisUtil.getReportIBSummaryDetailsByCost(ht, fdate, tdate, type, PLANT, PRD_DESCRIP,
						CUSTNAME);
			}

//			Boolean workSheetCreated = true;
			if (movQryList.size() >= 0) {
				HSSFSheet sheet = null;
				HSSFCellStyle styleHeader = null;
				HSSFCellStyle dataStyle = null;
				HSSFCellStyle dataStyleSpecial = null;
				HSSFCellStyle CompHeader = null;
				dataStyle = createDataStyle(wb);
				dataStyleSpecial = createDataStyle(wb);
				sheet = wb.createSheet("Sheet"+SheetId);
				styleHeader = createStyleHeader(wb);
				CompHeader = createCompStyleHeader(wb);
				sheet = this.createWidthInboundSummary(sheet, type);
				sheet = this.createHeaderInboundSummary(sheet, styleHeader, type,PLANT);
				 sheet = this.createHeaderCompanyReports(sheet,CompHeader,reportType,PLANT);
				CreationHelper createHelper = wb.getCreationHelper();

				int index = 2;
				// double orderCostSubTot=0,recvCostSubTot=0,unitcost=0;
				double unitcost = 0, Cost = 0, tax = 0, CostwTax = 0;//orderPriceSubTot = 0,issPriceSubTot = 0,  
				float gstpercentage = 0;
//				String strDiffQty = "", deliverydateandtime = "";
//				DecimalFormat decformat = new DecimalFormat("#,##0.00");
				
				String numberOfDecimal = _PlantMstDAO.getNumberOfDecimal(PLANT);
				
				DecimalFormat decimalFormat = new DecimalFormat("#.#####");
				decimalFormat.setRoundingMode(RoundingMode.FLOOR);
				for (int iCnt = 0; iCnt < movQryList.size(); iCnt++) {
					Map lineArr = (Map) movQryList.get(iCnt);
					int k = 0;
					tax = 0;
					if (type.equals("IB_SUMMARY_ORD_WITH_COST")) {

						unitcost = Double.parseDouble((String) lineArr.get("unitcost"));
						//unitcost = StrUtils.RoundDB(unitcost, 2);
						gstpercentage = Float.parseFloat(((String) lineArr.get("Tax").toString()));

						// indiviual subtotal price details
						Cost = Double.parseDouble((String) lineArr.get("OrderCost"));
						//tax = Double.parseDouble((String) lineArr.get("taxval"));
           	           int taxid = Integer.parseInt(((String)lineArr.get("TAXID").toString()));
           	           FinCountryTaxType fintaxtype = new FinCountryTaxTypeDAO().getCountryTaxTypesByid(taxid);
           	           if(taxid != 0){
           	        	   if(fintaxtype.getISZERO() == 0 && fintaxtype.getSHOWTAX() == 1){	              	        		   
           	        		   tax = (Cost*gstpercentage)/100;
           	        	   } else 
           	        		 gstpercentage=Float.parseFloat("0.000");
           	        	   
           	           }else 
           	        		 gstpercentage=Float.parseFloat("0.000");
						//Cost = StrUtils.RoundDB(Cost, 2);
						//tax = (Cost * gstpercentage) / 100;
						CostwTax = Cost + tax;
					}
					if (type.equals("IB_SUMMARY_RECV_WITH_COST")) {

						unitcost = Double.parseDouble((String) lineArr.get("unitcost"));
						//unitcost = StrUtils.RoundDB(unitcost, 2);
						gstpercentage = Float.parseFloat(((String) lineArr.get("Tax").toString()));

						// indiviual subtotal price details
						Cost = Double.parseDouble((String) lineArr.get("RecvCost"));
           	           int taxid = Integer.parseInt(((String)lineArr.get("TAXID").toString()));
           	           FinCountryTaxType fintaxtype = new FinCountryTaxTypeDAO().getCountryTaxTypesByid(taxid);
           	           if(taxid != 0){
           	        	   if(fintaxtype.getISZERO() == 0 && fintaxtype.getSHOWTAX() == 1){	              	        		   
           	        		   tax = (Cost*gstpercentage)/100;
           	        	   } else 
           	        		 gstpercentage=Float.parseFloat("0.000");
           	        	   
           	           }else 
           	        		 gstpercentage=Float.parseFloat("0.000");
						//Cost = StrUtils.RoundDB(Cost, 2);
						//tax = (Cost * gstpercentage) / 100;
						CostwTax = Cost + tax;
					}

//					deliverydateandtime = (String) lineArr.get("deliverydate") + " "
//							+ (String) lineArr.get("deliverytime");
					
					//String CostwTaxValue = String.valueOf(CostwTax);
					//String taxValue = String.valueOf(tax);
					//String CostValue = String.valueOf(Cost);
					String CostwTaxValue =   StrUtils.addZeroes(CostwTax, numberOfDecimal);
					String taxValue =   StrUtils.addZeroes(tax, numberOfDecimal);
					String CostValue =   StrUtils.addZeroes(Cost, numberOfDecimal);
					String gstpercentageValue = String.valueOf(gstpercentage);
					//String unitcostValue = String.valueOf(unitcost);
					String unitcostValue = StrUtils.addZeroes(unitcost, numberOfDecimal);
					String qtyOrValue = (String) lineArr.get("qtyor");
					String qtyValue = (String) lineArr.get("qty");
					
//					float CostwTaxVal ="".equals(CostwTaxValue) ? 0.0f :  Float.parseFloat(CostwTaxValue);
//					float taxVal ="".equals(taxValue) ? 0.0f :  Float.parseFloat(taxValue);
//					float CostVal ="".equals(CostValue) ? 0.0f :  Float.parseFloat(CostValue);
//					float gstpercentageVal ="".equals(gstpercentageValue) ? 0.0f :  Float.parseFloat(gstpercentageValue);
//					float unitcostVal ="".equals(unitcostValue) ? 0.0f :  Float.parseFloat(unitcostValue);
					float qtyOrVal ="".equals(qtyOrValue) ? 0.0f :  Float.parseFloat(qtyOrValue);
					float qtyVal ="".equals(qtyValue) ? 0.0f :  Float.parseFloat(qtyValue);
					
					
                    //DISPLAY COST BASED ON CURRENCY VALUE
                    float CurunitcostValue=Float.parseFloat(unitcostValue)*Float.parseFloat(CURRENCYUSEQT);
                    	String CurunitcostValues = StrUtils.addZeroes(CurunitcostValue, numberOfDecimal);
                    float CurCostValue=Float.parseFloat(CostValue)*Float.parseFloat(CURRENCYUSEQT);
                    	String CurCostValues = StrUtils.addZeroes(CurCostValue, numberOfDecimal);
                    float CurtaxValue=Float.parseFloat(taxValue)*Float.parseFloat(CURRENCYUSEQT);
                    	String CurtaxValues = StrUtils.addZeroes(CurtaxValue, numberOfDecimal);
                    float CurCostwTaxValue=Float.parseFloat(CostwTaxValue)*Float.parseFloat(CURRENCYUSEQT);
                    	String CurCostwTaxValues = StrUtils.addZeroes(CurCostwTaxValue, numberOfDecimal);
                    
					//imti start
					String lnstat= StrUtils.fString((String) lineArr.get("lnstat"));
					String ordstat= (String)lineArr.get("ORDER_STATUS");
					if(lnstat.equalsIgnoreCase("N"))
					{
						lnstat="OPEN";
					if(ordstat.equalsIgnoreCase("Draft"))
						lnstat="DRAFT";
					}
					   else if(lnstat.equalsIgnoreCase("O"))
						   lnstat="PARTIALLY RECEIVED";
					   else if(lnstat.equalsIgnoreCase("C"))
						   lnstat="RECEIVED";
					//imti end
							    		
		    		/*if(CostwTaxVal==0f){
		    			CostwTaxValue="0.00000";
		    		}else{
		    			CostwTaxValue=CostwTaxValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
		    		}if(taxVal==0f){
		    			taxValue="0.00000";
		    		}else{
		    			taxValue=taxValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
		    		}if(CostVal==0f){
		    			CostValue="0.00000";
		    		}else{
		    			CostValue=CostValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
		    		}if(gstpercentageVal==0f){
		    			gstpercentageValue="0.00000";
		    		}else{
		    			gstpercentageValue=gstpercentageValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
		    		}if(unitcostVal==0f){
		    			unitcostValue="0.00000";
		    		}else{
		    			unitcostValue=unitcostValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
		    		}*/if(qtyOrVal==0f){
		    			qtyOrValue="0.000";
		    		}else{
		    			qtyOrValue=qtyOrValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
		    		}if(qtyVal==0f){
		    			qtyValue="0.000";
		    		}else{
		    			qtyValue=qtyValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
		    		}
		    		
		    		String currencyid ="",exchangerate="",SrecvCostwTaxconv="",StaxValcon="",SrcvCostconv="";
		    		double currencyseqt=0,rcvCostconv=0;
		    		double recvCostwTaxConv=0,taxValcon=0;
		    		if (!type.equals("INBOUND")) {
		    		currencyid = (String)lineArr.get("CURRENCYID");
                    currencyseqt = Double.valueOf((String)lineArr.get("CURRENCYUSEQT"));
                    
                    
                    rcvCostconv = Cost * currencyseqt; 
                     SrcvCostconv= String.valueOf(rcvCostconv);
                    SrcvCostconv = currencyid + String.format("%.2f", rcvCostconv);
                    
                    recvCostwTaxConv = CostwTax * currencyseqt; 
                     SrecvCostwTaxconv = currencyid + String.format("%.2f", recvCostwTaxConv);
                     exchangerate = String.format("%.2f", currencyseqt);
                    
                    taxValcon = tax  * currencyseqt; 
                     StaxValcon = currencyid + String.format("%.2f", taxValcon);
		    		}
					
					
					HSSFRow row = sheet.createRow(index);

					HSSFCell cell = row.createCell(k++);
					cell.setCellValue(iCnt + 1);
					cell.setCellStyle(dataStyle);

					cell = row.createCell(k++);
					cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String) lineArr.get("pono"))));
					cell.setCellStyle(dataStyle);

					cell = row.createCell(k++);
					cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String) lineArr.get("ordertype"))));
					cell.setCellStyle(dataStyle);
					
					String jobnum = StrUtils.fString((String) lineArr.get("jobnum"));
					String jobNum = StrUtils.fString((String) lineArr.get("jobNum"));
					if(!jobnum.isEmpty()) {
						jobnum = jobnum;
					}else if(!jobNum.isEmpty()) {
						jobNum =jobnum ; 
						jobnum = jobNum;
						
					}
					if (!type.equals("INBOUND")) {
						cell = row.createCell(k++);
						cell.setCellValue(new HSSFRichTextString(jobnum));
						cell.setCellStyle(dataStyle);
					} else {
						cell = row.createCell(k++);
						cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String) lineArr.get("jobNum"))));
						cell.setCellStyle(dataStyle);
					}

					cell = row.createCell(k++);
					cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String) lineArr.get("custname"))));
					cell.setCellStyle(dataStyle);

					cell = row.createCell(k++);
					cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String) lineArr.get("remarks"))));
					cell.setCellStyle(dataStyle);
					if (type.equals("INBOUND") || type.equals("IB_SUMMARY_ORD_WITH_COST")
							|| type.equals("IB_SUMMARY_RECV_WITH_COST") || type.equals("IB_SUMMARY_RECV")) {
						cell = row.createCell(k++);
						cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String) lineArr.get("remarks3"))));
						cell.setCellStyle(dataStyle);
					}

					cell = row.createCell(k++);
					cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String) lineArr.get("item"))));
					cell.setCellStyle(dataStyle);

					cell = row.createCell(k++);
					cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String) lineArr.get("itemdesc"))));
					cell.setCellStyle(dataStyle);
					if (type.equals("INBOUND") || type.equals("IB_SUMMARY_ORD_WITH_COST")) {
					cell = row.createCell(k++);
					cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String) lineArr.get("DetailItemDesc"))));
					cell.setCellStyle(dataStyle);
					}
					
					cell = row.createCell(k++);
					cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String) lineArr.get("PRD_DEPT_ID"))));
					cell.setCellStyle(dataStyle);
					
					cell = row.createCell(k++);
					cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String) lineArr.get("prd_cls_id"))));
					cell.setCellStyle(dataStyle);

					cell = row.createCell(k++);
					cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String) lineArr.get("itemtype"))));
					cell.setCellStyle(dataStyle);

					cell = row.createCell(k++);
					cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String) lineArr.get("prd_brand_id"))));
					cell.setCellStyle(dataStyle);

					SimpleDateFormat _shortDateformatter = new SimpleDateFormat("yyyy-MM-dd");
					if (!type.equals("INBOUND")) {
						cell = row.createCell(k++);

						Calendar c = Calendar.getInstance();
						c.setTime(_shortDateformatter.parse(StrUtils.fString((String) lineArr.get("trandate"))));
						dataStyleSpecial.setDataFormat(createHelper.createDataFormat().getFormat("yyyy-MM-dd"));
						cell.setCellValue(c.getTime());
						cell.setCellStyle(dataStyleSpecial);

					} else if (type.equals("INBOUND")) {
						cell = row.createCell(k++);
						Calendar c = Calendar.getInstance();
						c.setTime(_shortDateformatter.parse(StrUtils.fString((String) lineArr.get("CollectionDate"))));
						dataStyleSpecial.setDataFormat(createHelper.createDataFormat().getFormat("yyyy-MM-dd"));
						cell.setCellValue(c.getTime());
						cell.setCellStyle(dataStyleSpecial);

					}
					
					
					cell = row.createCell(k++);
					cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String) lineArr.get("UOM"))));
					cell.setCellStyle(dataStyle);


					cell = row.createCell(k++);
					cell.setCellValue(qtyOrValue);

					cell.setCellStyle(dataStyle);

					cell = row.createCell(k++);
					cell.setCellValue(qtyValue);
					cell.setCellStyle(dataStyle);

					if (!type.equals("INBOUND")) {
						if (!type.equals("IB_SUMMARY_RECV")) {
							cell = row.createCell(k++);
							cell.setCellValue(unitcostValue);
//							cell.setCellValue(CurunitcostValues);
							cell.setCellStyle(dataStyle);
							
							cell = row.createCell(k++);
							cell.setCellValue(gstpercentageValue);
							cell.setCellStyle(dataStyle);
							
							cell = row.createCell(k++);
							cell.setCellValue(exchangerate);
							cell.setCellStyle(dataStyle);

							cell = row.createCell(k++);
							cell.setCellValue(CostValue);
//							cell.setCellValue(CurCostValues);
							cell.setCellStyle(dataStyle);
							
							cell = row.createCell(k++);
							cell.setCellValue(SrcvCostconv);
							cell.setCellStyle(dataStyle);

							cell = row.createCell(k++);
							cell.setCellValue(taxValue);
//							cell.setCellValue(CurtaxValues);
							cell.setCellStyle(dataStyle);
							
							cell = row.createCell(k++);
							cell.setCellValue(StaxValcon);
							cell.setCellStyle(dataStyle);

							cell = row.createCell(k++);
							cell.setCellValue(CostwTaxValue);
//							cell.setCellValue(CurCostwTaxValues);
							cell.setCellStyle(dataStyle);
							
							cell = row.createCell(k++);
							cell.setCellValue(SrecvCostwTaxconv);
							cell.setCellStyle(dataStyle);
						}
					}
					if (type.equals("INBOUND")) {

						cell = row.createCell(k++);
						cell.setCellValue(new HSSFRichTextString(lnstat));
						cell.setCellStyle(dataStyle);

					}
					
					cell = row.createCell(k++);
					cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String) lineArr.get("PURCHASE_LOCATION"))));
					cell.setCellStyle(dataStyle);
					
					cell = row.createCell(k++);
					cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String) lineArr.get("TAXTREATMENT"))));
					cell.setCellStyle(dataStyle);
					
					cell = row.createCell(k++);
					cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String) lineArr.get("REVERSECHARGE"))));
					cell.setCellStyle(dataStyle);
					
					cell = row.createCell(k++);
					cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String) lineArr.get("GOODSIMPORT"))));
					cell.setCellStyle(dataStyle);
					
					cell = row.createCell(k++);
					cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String) lineArr.get("users"))));
					cell.setCellStyle(dataStyle);

					index++;
					 if((index-2)%maxRowsPerSheet==0){
						 index = 2;
						 SheetId++;
						 sheet = wb.createSheet("Sheet"+SheetId);
						styleHeader = createStyleHeader(wb);
						 CompHeader = createCompStyleHeader(wb);
						sheet = this.createWidthInboundSummary(sheet, type);
						sheet = this.createHeaderInboundSummary(sheet, styleHeader, type,PLANT);
						sheet = this.createHeaderCompanyReports(sheet,CompHeader,reportType,PLANT);

					}

				}

			} else if (movQryList.size() < 1) {
				System.out.println("No Records Found To List");
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		}

		return wb;
	}

	private HSSFWorkbook writeToExcelInboundOrderWithRemarksSummary(HttpServletRequest request,
			HttpServletResponse response, HSSFWorkbook wb) {
//		StrUtils strUtils = new StrUtils();
		HTReportUtil movHisUtil = new HTReportUtil();
//		DateUtils _dateUtils = new DateUtils();
		ArrayList movQryList = new ArrayList();
		int maxRowsPerSheet = 65535; int SheetId =1;
		String fdate = "", tdate = "";//, taxby = "";
		try {
			String PLANT = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
			String ITEMNO = StrUtils.fString(request.getParameter("ITEM"));
			String PRD_DESCRIP = StrUtils.fString(request.getParameter("DESC"));
			String FROM_DATE = StrUtils.fString(request.getParameter("FROM_DATE"));
			String TO_DATE = StrUtils.fString(request.getParameter("TO_DATE"));
			String CUSTNAME = StrUtils.fString(request.getParameter("CUSTOMER"));
			String CUSTOMERID = StrUtils.fString(request.getParameter("CUSTOMERID"));
			String ORDERNO = StrUtils.fString(request.getParameter("ORDERNO"));
			String JOBNO = StrUtils.fString(request.getParameter("JOBNO"));
			String STATUS = StrUtils.fString(request.getParameter("STATUS"));
			String ORDERTYPE = StrUtils.fString(request.getParameter("ORDERTYPE"));
			String PRD_TYPE_ID = StrUtils.fString(request.getParameter("PRD_TYPE_ID"));
			String PRD_BRAND_ID = StrUtils.fString(request.getParameter("PRD_BRAND_ID"));
			String PRD_CLS_ID = StrUtils.fString(request.getParameter("PRD_CLS_ID"));
			//String statusID = StrUtils.fString(request.getParameter("STATUS_ID"));
			String type = StrUtils.fString(request.getParameter("DIRTYPE"));
			String VIEWSTATUS = StrUtils.fString(request.getParameter("VIEWSTATUS"));
			String UOM = StrUtils.fString(request.getParameter("UOM"));
			String POSSEARCH = StrUtils.fString(request.getParameter("POSSEARCH"));
			String reportType ="";
			if (FROM_DATE == null)
				FROM_DATE = "";
			else
				FROM_DATE = FROM_DATE.trim();
			String curDate = DateUtils.getDate();
			if (FROM_DATE.length() < 0 || FROM_DATE == null || FROM_DATE.equalsIgnoreCase(""))
				FROM_DATE = curDate;
			if (FROM_DATE.length() > 5)
				fdate = FROM_DATE.substring(6) + "-" + FROM_DATE.substring(3, 5) + "-" + FROM_DATE.substring(0, 2);
			if (TO_DATE == null)
				TO_DATE = "";
			else
				TO_DATE = TO_DATE.trim();
			if (TO_DATE.length() > 5)
				tdate = TO_DATE.substring(6) + "-" + TO_DATE.substring(3, 5) + "-" + TO_DATE.substring(0, 2);
			Hashtable ht = new Hashtable();

			if (StrUtils.fString(JOBNO).length() > 0)
				ht.put("A.JOBNUM", JOBNO);
			if (StrUtils.fString(ITEMNO).length() > 0)
				ht.put("B.ITEM", ITEMNO);
			if (StrUtils.fString(ORDERNO).length() > 0)
				ht.put("B.PONO", ORDERNO);
			if (StrUtils.fString(CUSTOMERID).length() > 0)
				ht.put("A.CUSTCODE", CUSTOMERID);
			if (StrUtils.fString(STATUS).length() > 0)
				ht.put("B.LNSTAT", STATUS);
			if (StrUtils.fString(ORDERTYPE).length() > 0)
				ht.put("A.ORDERTYPE", ORDERTYPE);
			if (StrUtils.fString(PRD_TYPE_ID).length() > 0)
				ht.put("C.ITEMTYPE", PRD_TYPE_ID);
			if (StrUtils.fString(PRD_BRAND_ID).length() > 0)
				ht.put("C.PRD_BRAND_ID", PRD_BRAND_ID);
			if (StrUtils.fString(PRD_CLS_ID).length() > 0)
				ht.put("C.PRD_CLS_ID", PRD_CLS_ID);
			if (StrUtils.fString(UOM).length() > 0)
				ht.put("B.SKTUOM", UOM);

			movQryList = movHisUtil.getWorkOrderSummaryList(ht, fdate, tdate, "PRODUCTREMARKS", PLANT, PRD_DESCRIP,
					CUSTNAME, VIEWSTATUS,UOM,POSSEARCH);

//			Boolean workSheetCreated = true;
			if (movQryList.size() > 0) {
				HSSFSheet sheet = null;
				HSSFCellStyle styleHeader = null;
				HSSFCellStyle dataStyle = null;
//				HSSFCellStyle dataStyleSpecial = null;
				HSSFCellStyle CompHeader = null;
				dataStyle = createDataStyle(wb);
//				dataStyleSpecial = 
						createDataStyle(wb);
				sheet = wb.createSheet("Sheet"+SheetId);
				styleHeader = createStyleHeader(wb);
				CompHeader = createCompStyleHeader(wb);
				sheet = this.createWidthInboundSummaryWithRemarks(sheet, type);
				sheet = this.createHeaderInboundSummaryWithRemarks(sheet, styleHeader, type);
				sheet = this.createHeaderCompanyReports(sheet,CompHeader,reportType,PLANT);
//				CreationHelper createHelper = wb.getCreationHelper();

				int index = 2;

				for (int iCnt = 0; iCnt < movQryList.size(); iCnt++) {
					Map lineArr = (Map) movQryList.get(iCnt);
					int k = 0;

					HSSFRow row = sheet.createRow(index);

					HSSFCell cell = row.createCell(k++);
					cell.setCellValue(iCnt + 1);
					cell.setCellStyle(dataStyle);

					cell = row.createCell(k++);
					cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String) lineArr.get("pono"))));
					cell.setCellStyle(dataStyle);

					cell = row.createCell(k++);
					cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String) lineArr.get("item"))));
					cell.setCellStyle(dataStyle);

					cell = row.createCell(k++);
					cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String) lineArr.get("prdremarks"))));
					cell.setCellStyle(dataStyle);

					index++;
					 if((index-2)%maxRowsPerSheet==0){
							 index = 2;
							 SheetId++;
							 sheet = wb.createSheet("Sheet"+SheetId);
							 CompHeader = createCompStyleHeader(wb);
						styleHeader = createStyleHeader(wb);
						sheet = this.createWidthInboundSummary(sheet, type);
						sheet = this.createHeaderInboundSummary(sheet, styleHeader, type,PLANT);
						sheet = this.createHeaderCompanyReports(sheet,CompHeader,reportType,PLANT);

					}
				}
			} else if (movQryList.size() < 1) {
				System.out.println("No Records Found To List");
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		}

		return wb;
	}

	private HSSFSheet createWidthInboundSummary(HSSFSheet sheet, String type) {
		int i = 0;
		try {
			sheet.setColumnWidth(i++, 1000);
			sheet.setColumnWidth(i++, 4000);
			sheet.setColumnWidth(i++, 3500);
			sheet.setColumnWidth(i++, 4000);
			sheet.setColumnWidth(i++, 6500);
			if (type.equals("INBOUND") || type.equals("IB_SUMMARY_ORD_WITH_COST")
					|| type.equals("IB_SUMMARY_RECV_WITH_COST") || type.equals("IB_SUMMARY_RECV")) {
				sheet.setColumnWidth(i++, 4200);
				sheet.setColumnWidth(i++, 4200);
			} else {
				sheet.setColumnWidth(i++, 4200);
			}
			sheet.setColumnWidth(i++, 4200);
			sheet.setColumnWidth(i++, 7500);
			sheet.setColumnWidth(i++, 4500);
			sheet.setColumnWidth(i++, 4500);
			sheet.setColumnWidth(i++, 4500);
			sheet.setColumnWidth(i++, 3500);
			sheet.setColumnWidth(i++, 3200);
			sheet.setColumnWidth(i++, 3200);

			if (!type.equals("INBOUND")) {
				if (!type.equals("IB_SUMMARY_RECV")) {
					sheet.setColumnWidth(i++, 3200);
					sheet.setColumnWidth(i++, 3200);
					sheet.setColumnWidth(i++, 3200);
					sheet.setColumnWidth(i++, 3200);
					sheet.setColumnWidth(i++, 3200);
					sheet.setColumnWidth(i++, 4500);
					sheet.setColumnWidth(i++, 4500);
					sheet.setColumnWidth(i++, 4500);
					sheet.setColumnWidth(i++, 4500);
					sheet.setColumnWidth(i++, 4500);
					sheet.setColumnWidth(i++, 4500);
					sheet.setColumnWidth(i++, 4500);
					sheet.setColumnWidth(i++, 4500);
					sheet.setColumnWidth(i++, 4500);
				}
			}
			if (type.equals("INBOUND")) {
				sheet.setColumnWidth(i++, 3200);
			}
			//sheet.setColumnWidth(i++, 3200);

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		}
		return sheet;
	}

	private HSSFSheet createWidthInboundSummaryWithRemarks(HSSFSheet sheet, String type) {
		int i = 0;
		try {
			sheet.setColumnWidth(i++, 1000);
			sheet.setColumnWidth(i++, 4000);
			sheet.setColumnWidth(i++, 4000);
			sheet.setColumnWidth(i++, 8000);
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		}
		return sheet;
	}

	private HSSFSheet createHeaderInboundSummary(HSSFSheet sheet, HSSFCellStyle styleHeader, String type,String plant) {
		int k = 0;
		try {
			String currency=_PlantMstDAO.getBaseCurrency(plant);

			HSSFRow rowhead = sheet.createRow(1);

			HSSFCell cell = rowhead.createCell(k++);
			cell.setCellValue(new HSSFRichTextString("S/N"));
			cell.setCellStyle(styleHeader);

			cell = rowhead.createCell(k++);
			cell.setCellValue(new HSSFRichTextString("Order No"));
			cell.setCellStyle(styleHeader);

			cell = rowhead.createCell(k++);
			cell.setCellValue(new HSSFRichTextString("Order Type"));
			cell.setCellStyle(styleHeader);

			cell = rowhead.createCell(k++);
			cell.setCellValue(new HSSFRichTextString("Ref No"));
			cell.setCellStyle(styleHeader);

			cell = rowhead.createCell(k++);
			cell.setCellValue(new HSSFRichTextString("Supplier Name"));
			cell.setCellStyle(styleHeader);

			if (type.equals("INBOUND") || type.equals("IB_SUMMARY_ORD_WITH_COST")
					|| type.equals("IB_SUMMARY_RECV_WITH_COST") || type.equals("IB_SUMMARY_RECV")) {
				cell = rowhead.createCell(k++);
				cell.setCellValue(new HSSFRichTextString("Remarks1"));
				cell.setCellStyle(styleHeader);

				cell = rowhead.createCell(k++);
				cell.setCellValue(new HSSFRichTextString("Remarks2"));
				cell.setCellStyle(styleHeader);
			} else {
				cell = rowhead.createCell(k++);
				cell.setCellValue(new HSSFRichTextString("Remarks"));
				cell.setCellStyle(styleHeader);
			}

			cell = rowhead.createCell(k++);
			cell.setCellValue(new HSSFRichTextString("Product ID"));
			cell.setCellStyle(styleHeader);

			cell = rowhead.createCell(k++);
			cell.setCellValue(new HSSFRichTextString("Description"));
			cell.setCellStyle(styleHeader);

			if (type.equals("INBOUND") || type.equals("IB_SUMMARY_ORD_WITH_COST")) {
				cell = rowhead.createCell(k++);
				cell.setCellValue(new HSSFRichTextString("Detail Description"));
				cell.setCellStyle(styleHeader);
			}

			cell = rowhead.createCell(k++);
			cell.setCellValue(new HSSFRichTextString("Department"));
			cell.setCellStyle(styleHeader);
			
			cell = rowhead.createCell(k++);
			cell.setCellValue(new HSSFRichTextString("Category"));
			cell.setCellStyle(styleHeader);

			cell = rowhead.createCell(k++);
			cell.setCellValue(new HSSFRichTextString("Sub Category"));
			cell.setCellStyle(styleHeader);

			cell = rowhead.createCell(k++);
			cell.setCellValue(new HSSFRichTextString("Brand"));
			cell.setCellStyle(styleHeader);

			if (type.equals("INBOUND") || type.equals("IB_SUMMARY_ORD_WITH_COST")) {
				cell = rowhead.createCell(k++);
				cell.setCellValue(new HSSFRichTextString("Date"));
				cell.setCellStyle(styleHeader);
			} else {
				cell = rowhead.createCell(k++);
				cell.setCellValue(new HSSFRichTextString("Received Date"));
				cell.setCellStyle(styleHeader);
			}
			
			cell = rowhead.createCell(k++);
			cell.setCellValue(new HSSFRichTextString("UOM"));
			cell.setCellStyle(styleHeader);

			cell = rowhead.createCell(k++);
			cell.setCellValue(new HSSFRichTextString("Order Qty"));
			cell.setCellStyle(styleHeader);

			cell = rowhead.createCell(k++);
			cell.setCellValue(new HSSFRichTextString("Receive Qty"));
			cell.setCellStyle(styleHeader);

			if (!type.equals("INBOUND")) {
				if (!type.equals("IB_SUMMARY_RECV")) {
					cell = rowhead.createCell(k++);
					cell.setCellValue(new HSSFRichTextString("Unit Cost"));
					cell.setCellStyle(styleHeader);

					cell = rowhead.createCell(k++);
					cell.setCellValue(new HSSFRichTextString("Tax%"));
					cell.setCellStyle(styleHeader);

					if (type.equals("IB_SUMMARY_ORD_WITH_COST")) {
						
						cell = rowhead.createCell(k++);
						cell.setCellValue(new HSSFRichTextString("EXCHANGE RATE"));
						cell.setCellStyle(styleHeader);
						
						cell = rowhead.createCell(k++);
						cell.setCellValue(new HSSFRichTextString("Order Cost ("+currency+")"));
						cell.setCellStyle(styleHeader);
						
						
						cell = rowhead.createCell(k++);
						cell.setCellValue(new HSSFRichTextString("Order Cost"));
						cell.setCellStyle(styleHeader);
					} else {
						if (!type.equals("INBOUND")) {
						cell = rowhead.createCell(k++);
						cell.setCellValue(new HSSFRichTextString("EXCHANGE RATE"));
						cell.setCellStyle(styleHeader);


						cell = rowhead.createCell(k++);
						cell.setCellValue(new HSSFRichTextString("Recv Cost ("+currency+")"));
						cell.setCellStyle(styleHeader);
						}
						
						cell = rowhead.createCell(k++);
						cell.setCellValue(new HSSFRichTextString("Recv Cost"));
						cell.setCellStyle(styleHeader);
					}
					if (!type.equals("INBOUND")) {
					cell = rowhead.createCell(k++);
					cell.setCellValue(new HSSFRichTextString("Tax ("+currency+")"));
					cell.setCellStyle(styleHeader);}
					
					cell = rowhead.createCell(k++);
					cell.setCellValue(new HSSFRichTextString("Tax"));
					cell.setCellStyle(styleHeader);
					if (!type.equals("INBOUND")) {
					cell = rowhead.createCell(k++);
					cell.setCellValue(new HSSFRichTextString("Total ("+currency+")"));
					cell.setCellStyle(styleHeader);}

					cell = rowhead.createCell(k++);
					cell.setCellValue(new HSSFRichTextString("Total"));
					cell.setCellStyle(styleHeader);
				}
			}
			if (type.equals("INBOUND")) {
				cell = rowhead.createCell(k++);
				cell.setCellValue(new HSSFRichTextString("Receive Status"));
				cell.setCellStyle(styleHeader);

			}
			
			cell = rowhead.createCell(k++);
			cell.setCellValue(new HSSFRichTextString("Purchase Location"));
			cell.setCellStyle(styleHeader);
			
			cell = rowhead.createCell(k++);
			cell.setCellValue(new HSSFRichTextString("Tax Treatment"));
			cell.setCellStyle(styleHeader);
			
			cell = rowhead.createCell(k++);
			cell.setCellValue(new HSSFRichTextString("Reverse Charge"));
			cell.setCellStyle(styleHeader);
			
			cell = rowhead.createCell(k++);
			cell.setCellValue(new HSSFRichTextString("Goods Import"));
			cell.setCellStyle(styleHeader);
			
			cell = rowhead.createCell(k++);
			cell.setCellValue(new HSSFRichTextString("User"));
			cell.setCellStyle(styleHeader);

			/*cell = rowhead.createCell(k++);
			cell.setCellValue(new HSSFRichTextString("Order Status"));
			cell.setCellStyle(styleHeader);*/
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		}
		return sheet;
	}

	// ---End Added by Bruhan on May 22 2014, Description:To open Inbound order
	// summary in excel powershell format

	private HSSFSheet createHeaderInboundSummaryWithRemarks(HSSFSheet sheet, HSSFCellStyle styleHeader, String type) {
		int k = 0;
		try {

			HSSFRow rowhead = sheet.createRow(1);

			HSSFCell cell = rowhead.createCell(k++);
			cell.setCellValue(new HSSFRichTextString("S/N"));
			cell.setCellStyle(styleHeader);

			cell = rowhead.createCell(k++);
			cell.setCellValue(new HSSFRichTextString("Order No"));
			cell.setCellStyle(styleHeader);

			cell = rowhead.createCell(k++);
			cell.setCellValue(new HSSFRichTextString("Product ID"));
			cell.setCellStyle(styleHeader);

			cell = rowhead.createCell(k++);
			cell.setCellValue(new HSSFRichTextString("Remarks"));
			cell.setCellStyle(styleHeader);

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		}
		return sheet;
	}

	private HSSFCellStyle createStyleHeader(HSSFWorkbook wb) {

		// Create style
		HSSFCellStyle styleHeader = wb.createCellStyle();
		HSSFFont fontHeader = wb.createFont();
		fontHeader.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		fontHeader.setFontName("Arial");
		styleHeader.setFont(fontHeader);
		styleHeader.setFillForegroundColor(HSSFColor.GREY_40_PERCENT.index);
		styleHeader.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		styleHeader.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		styleHeader.setWrapText(true);
		return styleHeader;
	}

	private HSSFCellStyle createDataStyle(HSSFWorkbook wb) {
		// Create style
		HSSFCellStyle dataStyle = wb.createCellStyle();
		dataStyle.setWrapText(true);
		return dataStyle;
	}
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
	
	private HSSFSheet createHeaderCompanyReports(HSSFSheet sheet, HSSFCellStyle styleHeader,String reportType,String PLANT){
		int k = 0;
		try{
			String CNAME = "", CZIP = "", CCOUNTRY = "", CSTATE="";
					//, CTEL = "",CFAX = "", CONTACTNAME = "", CHPNO = "", CEMAIL = "", CRCBNO = "",COL1="",COL2="", CADD1 = "", CADD2 = "", CADD3 = "", CADD4 = "" ;
			PlantMstUtil pmUtil = new PlantMstUtil();
			ArrayList listQry = pmUtil.getPlantMstDetails(PLANT);
			for (int i = 0; i < listQry.size(); i++) {
				Map map = (Map) listQry.get(i);
				CNAME = (String) map.get("PLNTDESC");
								
				CCOUNTRY = (String) map.get("COUNTY");
				CSTATE = (String) map.get("STATE");
				CZIP = (String) map.get("ZIP");
				if((CSTATE).length()>1)
					CNAME=CNAME+", "+CSTATE;
				
				if((CCOUNTRY).length()>1)
					CNAME=CNAME+", "+CCOUNTRY;
				
				if((CZIP).length()>1)
					CNAME=CNAME+"-"+CZIP;
					
					
			}
			
		HSSFRow rowhead = sheet.createRow(0);
		HSSFCell cell = rowhead.createCell(k++);
		cell.setCellValue(new HSSFRichTextString(CNAME));	
		cell.setCellStyle(styleHeader);
		CellRangeAddress cellRangeAddress = new CellRangeAddress(0, 0, 0, 4);
		sheet.addMergedRegion(cellRangeAddress);
										
		}
		catch(Exception e){
			this.mLogger.exception(this.printLog, "", e);
		}
		return sheet;
	}
	private HSSFCellStyle createCompStyleHeader(HSSFWorkbook wb){
		
		//Create style
		 HSSFCellStyle styleHeader = wb.createCellStyle();
		  HSSFFont fontHeader  = wb.createFont();
		  fontHeader.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		  fontHeader.setFontName("Arial");	
		  styleHeader.setFont(fontHeader);
		  styleHeader.setFillForegroundColor(HSSFColor.WHITE.index);
		  styleHeader.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		  styleHeader.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		  styleHeader.setWrapText(true);
		  return styleHeader;
	}

	public HashMap<String, String> populateMapData(String companyCode, String userCode) {
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
	public String getNextIdDetails(String plant,String user_id)
	{
		String	Acctid = "",sBatchSeq="",sZero="";
//		boolean insertFlag=false; // Get Account Id
		_TblControlDAO.setmLogger(mLogger);								  	
	    Hashtable  htnxt  =new Hashtable();
	    String query=" isnull(NXTSEQ,'') as NXTSEQ";
	    htnxt.put(IDBConstants.PLANT,plant);
	    htnxt.put(IDBConstants.TBL_FUNCTION,"ACCOUNT");
	    try{
	       boolean exitFlag=false; 
	       //boolean resultflag=false;
	       exitFlag=_TblControlDAO.isExisit(htnxt,"",plant);
	       if (exitFlag==false)
		      {         
//		            Map htInsert=null;
		            Hashtable htTblCntInsert  = new Hashtable();
		            htTblCntInsert.put(IDBConstants.PLANT,plant);
		            htTblCntInsert.put(IDBConstants.TBL_FUNCTION,"ACCOUNT");
		            htTblCntInsert.put(IDBConstants.TBL_PREFIX1,"A");
		            htTblCntInsert.put(IDBConstants.TBL_MINSEQ,"0000");
		            htTblCntInsert.put(IDBConstants.TBL_MAXSEQ,"9999");
		            htTblCntInsert.put(IDBConstants.TBL_NEXT_SEQ, (String)IDBConstants.TBL_FIRST_NEX_SEQ);
		            htTblCntInsert.put(IDBConstants.CREATED_BY, user_id);
		            htTblCntInsert.put(IDBConstants.CREATED_AT, (String)DateUtils.getDateTime());
//		            insertFlag= _
		            _TblControlDAO.insertTblControl(htTblCntInsert,plant);
		            Acctid="A"+"0001";
		      }
	       else
		      {
		           //--if exitflag is not false than we updated nxtseq batch number based on plant,currentmonth
		           Map m= _TblControlDAO.selectRow(query, htnxt,"");
		           sBatchSeq=(String)m.get("NXTSEQ");
		           int inxtSeq=Integer.parseInt(((String)sBatchSeq.trim().toString()))+1;
		           String updatedSeq=Integer.toString(inxtSeq);
		            if(updatedSeq.length()==1)
		           {
		             sZero="000";
		           }
		           else if(updatedSeq.length()==2)
		           {
		             sZero="00";
		           }
		           else if(updatedSeq.length()==3)
		           {
		             sZero="0";
		           }
//		            Map htUpdate = null;
		           Hashtable htTblCntUpdate = new Hashtable();
		           htTblCntUpdate.put(IDBConstants.PLANT,plant);
		           htTblCntUpdate.put(IDBConstants.TBL_FUNCTION,"ACCOUNT");
		           htTblCntUpdate.put(IDBConstants.TBL_PREFIX1,"A");
		           StringBuffer updateQyery=new StringBuffer("set ");
		           updateQyery.append(IDBConstants.TBL_NEXT_SEQ +" = '"+ (String)updatedSeq.toString()+ "'");   
		           Acctid="A"+sZero+updatedSeq;
		        }
		      } catch(Exception e)
		         {
		    	  mLogger.exception(true,
							"ERROR IN PAGE", e);
		         }
		return Acctid;
	}
	
	private JSONObject getOrderNoForAutoSuggestion(HttpServletRequest request) {
		JSONObject resultJson = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        JSONArray jsonArrayErr = new JSONArray();
        
        POUtil itemUtil = new POUtil();
//        StrUtils strUtils = new StrUtils();
        itemUtil.setmLogger(mLogger);
        
        try {
        	String plant = StrUtils.fString(request.getParameter("PLANT")).trim();
		    String pono = StrUtils.fString(request.getParameter("PONO")).trim();
		    String cname = StrUtils.fString(request.getParameter("CNAME")).trim();
		    String type = StrUtils.fString(request.getParameter("TYPE")).trim();
		    
		    Hashtable ht=new Hashtable();
		     String extCond="";
		     ht.put("PLANT",plant);
		     if(pono.length()>0) extCond=" AND plant='"+plant+"' and pono like '"+pono+"%' ";
		     if(cname.length()>0) extCond +=" AND custName = '"+cname+"' ";
		     /*extCond=extCond+" and STATUS <>'C'";*/
		     if(type.equalsIgnoreCase("RETURN")) {
		    	 extCond=extCond + " and STATUS <> 'N'";
		     }
		     extCond=extCond+"ORDER BY CONVERT(date, CollectionDate, 103) desc";
		     ArrayList listQry = itemUtil.getPoHdrDetails("pono,custName,custcode,jobNum,status,collectiondate",ht,extCond);
		     if (listQry.size() > 0) {
		    	 for(int i =0; i<listQry.size(); i++) {		   
				  Map m=(Map)listQry.get(i);
				  pono = (String)m.get("pono");
				  String custName = StrUtils.replaceCharacters2Send((String)m.get("custName"));
				  String custcode = (String)m.get("custcode");
				  String orderdate = (String)m.get("collectiondate");
				  String jobNum = (String)m.get("jobNum");
				  String status = (String)m.get("status");
				  JSONObject resultJsonInt = new JSONObject();
				  resultJsonInt.put("PONO", pono);
				  resultJsonInt.put("CUSTNAME", custName);
				  resultJsonInt.put("CUSTCODE", custcode);
				  resultJsonInt.put("ORDERDATE", orderdate);
				  resultJsonInt.put("JOBNUM", jobNum);
				  resultJsonInt.put("STATUS", status);				  
				  jsonArray.add(resultJsonInt);
			     }	     
			     resultJson.put("orders", jsonArray);
	             JSONObject resultJsonInt = new JSONObject();
	             resultJsonInt.put("ERROR_MESSAGE", "NO ERRORS!");
	             resultJsonInt.put("ERROR_CODE", "100");
	             jsonArrayErr.add(resultJsonInt);
	             resultJson.put("errors", jsonArrayErr);
		     } else {
                 JSONObject resultJsonInt = new JSONObject();
                 resultJsonInt.put("ERROR_MESSAGE", "NO ORDER RECORD FOUND!");
                 resultJsonInt.put("ERROR_CODE", "99");
                 jsonArrayErr.add(resultJsonInt);
                 jsonArray.add("");
                 resultJson.put("items", jsonArray);
                 resultJson.put("errors", jsonArrayErr);
		     }
        }catch (Exception e) {
            resultJson.put("SEARCH_DATA", "");
            JSONObject resultJsonInt = new JSONObject();
            resultJsonInt.put("ERROR_MESSAGE",  ThrowableUtil.getMessage(e));
            resultJsonInt.put("ERROR_CODE", "98");
            jsonArrayErr.add(resultJsonInt);
            resultJson.put("ERROR", jsonArrayErr);
        }
        return resultJson;
	}

	
	
	private JSONObject getMultiPurchaseEstOrderNoForAutoSuggestion(HttpServletRequest request) {
		JSONObject resultJson = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        JSONArray jsonArrayErr = new JSONArray();
        
        MultiPoUtil _MultiPoUtil = new MultiPoUtil();
//        StrUtils strUtils = new StrUtils();
        _MultiPoUtil.setmLogger(mLogger);
        
        try {
        	String plant = StrUtils.fString(request.getParameter("PLANT")).trim();
		    String pomultiestno = StrUtils.fString(request.getParameter("POMULTIESTNO")).trim();
		    
		    Hashtable ht=new Hashtable();
		     String extCond="";
		     ht.put("PLANT",plant);
		     if(pomultiestno.length()>0) extCond=" AND plant='"+plant+"' and pomultiestno like '"+pomultiestno+"%' ";
		     /*extCond=extCond+" and STATUS <>'C'";*/
		    
		     extCond=extCond+"ORDER BY CONVERT(date, CollectionDate, 103) desc";
		     ArrayList listQry = _MultiPoUtil.getMultiPoEstHdrDetails("pomultiestno,jobNum,status,collectiondate",ht,extCond);
		     if (listQry.size() > 0) {
		    	 for(int i =0; i<listQry.size(); i++) {		   
				  Map m=(Map)listQry.get(i);
				  pomultiestno = (String)m.get("pomultiestno");
				
				  String orderdate = (String)m.get("collectiondate");
				  String jobNum = (String)m.get("jobNum");
				  String status = (String)m.get("status");
				  JSONObject resultJsonInt = new JSONObject();
				  resultJsonInt.put("POMULTIESTNO", pomultiestno);
				
				  resultJsonInt.put("ORDERDATE", orderdate);
				  resultJsonInt.put("JOBNUM", jobNum);
				  resultJsonInt.put("STATUS", status);				  
				  jsonArray.add(resultJsonInt);
			     }	     
			     resultJson.put("orders", jsonArray);
	             JSONObject resultJsonInt = new JSONObject();
	             resultJsonInt.put("ERROR_MESSAGE", "NO ERRORS!");
	             resultJsonInt.put("ERROR_CODE", "100");
	             jsonArrayErr.add(resultJsonInt);
	             resultJson.put("errors", jsonArrayErr);
		     } else {
                 JSONObject resultJsonInt = new JSONObject();
                 resultJsonInt.put("ERROR_MESSAGE", "NO ORDER RECORD FOUND!");
                 resultJsonInt.put("ERROR_CODE", "99");
                 jsonArrayErr.add(resultJsonInt);
                 jsonArray.add("");
                 resultJson.put("items", jsonArray);
                 resultJson.put("errors", jsonArrayErr);
		     }
        }catch (Exception e) {
            resultJson.put("SEARCH_DATA", "");
            JSONObject resultJsonInt = new JSONObject();
            resultJsonInt.put("ERROR_MESSAGE",  ThrowableUtil.getMessage(e));
            resultJsonInt.put("ERROR_CODE", "98");
            jsonArrayErr.add(resultJsonInt);
            resultJson.put("ERROR", jsonArrayErr);
        }
        return resultJson;
	}

	
	//Resvi Starts
	
	private JSONObject getPurchaseEstOrderNoForAutoSuggestion(HttpServletRequest request) {
		JSONObject resultJson = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        JSONArray jsonArrayErr = new JSONArray();
        
        PoEstUtil _PoEstUtil = new PoEstUtil();
//        StrUtils strUtils = new StrUtils();
        _PoEstUtil.setmLogger(mLogger);
        
        try {
        	String plant = StrUtils.fString(request.getParameter("PLANT")).trim();
		    String POESTNO = StrUtils.fString(request.getParameter("POESTNO")).trim();
		    String cname = StrUtils.fString(request.getParameter("CNAME")).trim();
		    String type = StrUtils.fString(request.getParameter("TYPE")).trim();
		    
		    Hashtable ht=new Hashtable();
		     String extCond="";
		     ht.put("PLANT",plant);
		     if(POESTNO.length()>0) extCond=" AND plant='"+plant+"' and POESTNO like '"+POESTNO+"%' ";
		     if(cname.length()>0) extCond +=" AND custName = '"+cname+"' ";
		     /*extCond=extCond+" and STATUS <>'C'";*/
		     if(type.equalsIgnoreCase("RETURN")) {
		    	 extCond=extCond + " and STATUS <> 'N'";
		     }
		     extCond=extCond+"ORDER BY CONVERT(date, CollectionDate, 103) desc";
		     ArrayList listQry = _PoEstUtil.getPoEstHdrDetails("POESTNO,custName,custcode,jobNum,status,collectiondate",ht,extCond);
		     if (listQry.size() > 0) {
		    	 for(int i =0; i<listQry.size(); i++) {		   
				  Map m=(Map)listQry.get(i);
				  POESTNO = (String)m.get("POESTNO");
				  String custName = StrUtils.replaceCharacters2Send((String)m.get("custName"));
				  String custcode = (String)m.get("custcode");
				  String orderdate = (String)m.get("collectiondate");
				  String jobNum = (String)m.get("jobNum");
				  String status = (String)m.get("status");
				  JSONObject resultJsonInt = new JSONObject();
				  resultJsonInt.put("POESTNO", POESTNO);
				  resultJsonInt.put("CUSTNAME", custName);
				  resultJsonInt.put("CUSTCODE", custcode);
				  resultJsonInt.put("ORDERDATE", orderdate);
				  resultJsonInt.put("JOBNUM", jobNum);
				  resultJsonInt.put("STATUS", status);				  
				  jsonArray.add(resultJsonInt);
			     }	     
			     resultJson.put("orders", jsonArray);
	             JSONObject resultJsonInt = new JSONObject();
	             resultJsonInt.put("ERROR_MESSAGE", "NO ERRORS!");
	             resultJsonInt.put("ERROR_CODE", "100");
	             jsonArrayErr.add(resultJsonInt);
	             resultJson.put("errors", jsonArrayErr);
		     } else {
                 JSONObject resultJsonInt = new JSONObject();
                 resultJsonInt.put("ERROR_MESSAGE", "NO ORDER RECORD FOUND!");
                 resultJsonInt.put("ERROR_CODE", "99");
                 jsonArrayErr.add(resultJsonInt);
                 jsonArray.add("");
                 resultJson.put("items", jsonArray);
                 resultJson.put("errors", jsonArrayErr);
		     }
        }catch (Exception e) {
            resultJson.put("SEARCH_DATA", "");
            JSONObject resultJsonInt = new JSONObject();
            resultJsonInt.put("ERROR_MESSAGE",  ThrowableUtil.getMessage(e));
            resultJsonInt.put("ERROR_CODE", "98");
            jsonArrayErr.add(resultJsonInt);
            resultJson.put("ERROR", jsonArrayErr);
        }
        return resultJson;
	}

//	Ends
	
	private JSONObject getOrderNoForForceClose(HttpServletRequest request) {
		JSONObject resultJson = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        JSONArray jsonArrayErr = new JSONArray();
        
        POUtil itemUtil = new POUtil();
//        StrUtils strUtils = new StrUtils();
        itemUtil.setmLogger(mLogger);
        OrderTypeUtil orderUtil = new OrderTypeUtil();
        orderUtil.setmLogger(mLogger);
        
        try {
        	String plant = StrUtils.fString(request.getParameter("PLANT")).trim();
		    String pono = StrUtils.fString(request.getParameter("PONO")).trim();
		    String status = StrUtils.fString(request.getParameter("STATUS")).trim();
		    String orderNo = StrUtils.fString(request.getParameter("orderNo")).trim();
		    
		    
		    ArrayList listQry = orderUtil.getOrderHdrDetails(plant,"INBOUND",pono,status);
		     if (listQry.size() > 0) {
		    	 for(int i =0; i<listQry.size(); i++) {		   
		    		  Map m=(Map)listQry.get(i);
		    	      orderNo     = (String)m.get("orderNo");
		    	      String custName    = (String)m.get("CustName");
		    	      String status1      =  (String)m.get("status");
				  JSONObject resultJsonInt = new JSONObject();
				  resultJsonInt.put("PONO", orderNo);
				  resultJsonInt.put("CUSTNAME", custName);
				  resultJsonInt.put("STATUS", status1);				  
				  jsonArray.add(resultJsonInt);
			     }	     
			     resultJson.put("orders", jsonArray);
	             JSONObject resultJsonInt = new JSONObject();
	             resultJsonInt.put("ERROR_MESSAGE", "NO ERRORS!");
	             resultJsonInt.put("ERROR_CODE", "100");
	             jsonArrayErr.add(resultJsonInt);
	             resultJson.put("errors", jsonArrayErr);
		     } else {
                 JSONObject resultJsonInt = new JSONObject();
                 resultJsonInt.put("ERROR_MESSAGE", "NO ORDER RECORD FOUND!");
                 resultJsonInt.put("ERROR_CODE", "99");
                 jsonArrayErr.add(resultJsonInt);
                 jsonArray.add("");
                 resultJson.put("items", jsonArray);
                 resultJson.put("errors", jsonArrayErr);
		     }
        }catch (Exception e) {
            resultJson.put("SEARCH_DATA", "");
            JSONObject resultJsonInt = new JSONObject();
            resultJsonInt.put("ERROR_MESSAGE",  ThrowableUtil.getMessage(e));
            resultJsonInt.put("ERROR_CODE", "98");
            jsonArrayErr.add(resultJsonInt);
            resultJson.put("ERROR", jsonArrayErr);
        }
        return resultJson;
	}

	
	
	private JSONObject getOrderTypeForAutoSuggestion(HttpServletRequest request) {
        JSONObject resultJson = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        JSONArray jsonArrayErr = new JSONArray();
        try {
//        	StrUtils strUtils = new StrUtils();
//        	ItemMstUtil itemUtil = new ItemMstUtil();
        	String plant = StrUtils.fString(request.getParameter("PLANT")).trim();
        	String QUERY = StrUtils.fString(request.getParameter("QUERY")).trim();
        	String OTYPE = StrUtils.fString(request.getParameter("OTYPE"));
        	
        	
        
        	ArrayList listQry = OrderTypeBeanDAO.getOrderTypeList(plant,OTYPE,QUERY);
        	if (listQry.size() > 0) {
        		
        		for(int i =0; i<listQry.size(); i++) {
        			Map m=(Map)listQry.get(i);
        			JSONObject resultJsonInt = new JSONObject();
        			resultJsonInt.put("ORDERTYPE", StrUtils.fString((String)m.get("ORDERTYPE")));
                    resultJsonInt.put("ORDERDESC", StrUtils.fString((String)m.get("ORDERDESC")));                    
                    jsonArray.add(resultJsonInt);
        		}
        		resultJson.put("ORDERTYPEMST", jsonArray);
                JSONObject resultJsonInt = new JSONObject();
                resultJsonInt.put("ERROR_MESSAGE", "NO ERRORS!");
                resultJsonInt.put("ERROR_CODE", "100");
                jsonArrayErr.add(resultJsonInt);
                resultJson.put("errors", jsonArrayErr);
        	} else {
        		
        		JSONObject resultJsonInt = new JSONObject();
                resultJsonInt.put("ERROR_MESSAGE", "NO PRODUCT RECORD FOUND!");
                resultJsonInt.put("ERROR_CODE", "99");
                jsonArrayErr.add(resultJsonInt);
                resultJson.put("errors", jsonArrayErr);         
        	}
//        	 resultJson.put("ORDERTYPEMST", jsonArray);
        } catch (Exception e) {
	            resultJson.put("SEARCH_DATA", "");
	            JSONObject resultJsonInt = new JSONObject();
	            resultJsonInt.put("ERROR_MESSAGE",  ThrowableUtil.getMessage(e));
	            resultJsonInt.put("ERROR_CODE", "98");
	            jsonArrayErr.add(resultJsonInt);
	            resultJson.put("ERROR", jsonArrayErr);
        }
	    return resultJson;
	}
	
	private JSONObject getOrderDetailsForBilling(HttpServletRequest request) {
		JSONObject resultJson = new JSONObject();
		JSONArray jsonArray = new JSONArray();
        JSONArray jsonArrayErr = new JSONArray();
		POUtil poUtil = new POUtil();
		PlantMstDAO plantMstDAO = new PlantMstDAO();
		PoHdrDAO poHdrDao = new PoHdrDAO();
		try {
			String plant = StrUtils.fString(request.getParameter("PLANT")).trim();
		    String pono = StrUtils.fString(request.getParameter("PONO")).trim();
		    
		    String numberOfDecimal = plantMstDAO.getNumberOfDecimal(plant);
		    String ConvertedUnitCost="",ConvertedAmount="";
		    Hashtable ht=new Hashtable();
		    ht.put("PONO", pono);
		    ht.put("PLANT", plant);

		    List listQry = poUtil.getOrderDetailsForBilling(ht);
		    String actualShippingCost = poHdrDao.getActualShippingCostForBill(plant, pono);
		   
		    if (listQry.size() > 0) {
		    	for(int i =0; i<listQry.size(); i++) {   
		    		Map m=(Map)listQry.get(i);
					JSONObject resultJsonInt = new JSONObject();
					
					double dCost = Double.parseDouble((String)m.get("UNITCOST"));
					ConvertedUnitCost = StrUtils.addZeroes(dCost, numberOfDecimal);
					
					double dQty = Double.parseDouble((String)m.get("QTYOR"));
					double dAmount = dCost * dQty;
					ConvertedAmount = StrUtils.addZeroes(dAmount, numberOfDecimal);
					
					resultJsonInt.put("PONO", (String)m.get("PONO"));
					resultJsonInt.put("POLNNO", (String)m.get("POLNNO"));
					resultJsonInt.put("CURRENCYID", (String)m.get("CURRENCYID"));
					resultJsonInt.put("INBOUND_GST", (String)m.get("INBOUND_GST"));
					resultJsonInt.put("ORDERDISCOUNT", (String)m.get("ORDERDISCOUNT"));
					resultJsonInt.put("SHIPPINGCOST", (String)m.get("SHIPPINGCOST"));
					resultJsonInt.put("LOCALEXPENSES", (String)m.get("LOCALEXPENSES"));
					resultJsonInt.put("EMPNO", (String)m.get("EMPNO"));
					resultJsonInt.put("EMP_NAME", (String)m.get("EMP_NAME"));
					resultJsonInt.put("ITEM", (String)m.get("ITEM"));
					resultJsonInt.put("ITEMDESC", (String)m.get("ITEMDESC"));
					resultJsonInt.put("UNITCOST", ConvertedUnitCost);
					resultJsonInt.put("UNITMO", (String)m.get("UNITMO"));
					resultJsonInt.put("QTYOR", (String)m.get("QTYOR"));
					resultJsonInt.put("CONVCOST", (String)m.get("CONVCOST"));
					resultJsonInt.put("ACCOUNT", "Inventory Asset");
					resultJsonInt.put("AMOUNT", ConvertedAmount);
					String catlogpath = StrUtils.fString((String)m.get("CATLOGPATH"));
                    resultJsonInt.put("CATLOGPATH", ((catlogpath == "") ? "../jsp/dist/img/NO_IMG.png" : "/track/ReadFileServlet/?fileLocation="+catlogpath));
                    resultJsonInt.put("ACTUALSHIPPINGCOST", actualShippingCost);
                    resultJsonInt.put("PURCHASE_LOCATION", (String)m.get("PURCHASE_LOCATION"));
                    resultJsonInt.put("STATE_PREFIX", (String)m.get("STATE_PREFIX"));
                    jsonArray.add(resultJsonInt);
		    	}
		    	resultJson.put("orders", jsonArray);
	            JSONObject resultJsonInt = new JSONObject();
	            resultJsonInt.put("ERROR_MESSAGE", "NO ERRORS!");
	            resultJsonInt.put("ERROR_CODE", "100");
	            jsonArrayErr.add(resultJsonInt);
	            resultJson.put("errors", jsonArrayErr);
		    } else {
                JSONObject resultJsonInt = new JSONObject();
                resultJsonInt.put("ERROR_MESSAGE", "NO ORDER RECORD FOUND!");
                resultJsonInt.put("ERROR_CODE", "99");
                jsonArrayErr.add(resultJsonInt);
                jsonArray.add("");
                resultJson.put("items", jsonArray);
                resultJson.put("errors", jsonArrayErr);
		     }
		}catch (Exception e) {
			resultJson.put("SEARCH_DATA", "");
            JSONObject resultJsonInt = new JSONObject();
            resultJsonInt.put("ERROR_MESSAGE",  ThrowableUtil.getMessage(e));
            resultJsonInt.put("ERROR_CODE", "98");
            jsonArrayErr.add(resultJsonInt);
            resultJson.put("ERROR", jsonArrayErr);
		}
		return resultJson;
	}
	
	private JSONObject getBillingDetailsByGRNO(HttpServletRequest request) {
		JSONObject resultJson = new JSONObject();
		JSONArray jsonArray = new JSONArray();
        JSONArray jsonArrayErr = new JSONArray();
		POUtil poUtil = new POUtil();
		PoDetDAO podetdao = new PoDetDAO();
		PoHdrDAO poHdrDao = new PoHdrDAO();
		FinCountryTaxType fintaxtype= new FinCountryTaxType();
		FinCountryTaxTypeDAO finCountryTaxTypeDAO = new FinCountryTaxTypeDAO();
		PlantMstDAO plantMstDAO = new PlantMstDAO();
		ItemUtil itemUtil = new ItemUtil();
		try {
			String plant = StrUtils.fString(request.getParameter("PLANT")).trim();
		    String pono = StrUtils.fString(request.getParameter("PONO")).trim();
		    String grno = StrUtils.fString(request.getParameter("GRNO")).trim();
		    
		    String numberOfDecimal = plantMstDAO.getNumberOfDecimal(plant);
		    String ConvertedUnitCost="",ConvertedAmount="",ConvertedUnitCost_Aod="",ConvertedAmount_Aod="";
		    Hashtable ht=new Hashtable();
		    ht.put("PONO", pono);
		    ht.put("PLANT", plant);
		    ht.put("GRNO", grno);
		    List listQry = poUtil.getBillingDetailsByGRNO(ht);
		    PoHdr pohdr = poHdrDao.getPoHdrByPono(plant, pono);
		    String actualShippingCost = poHdrDao.getActualShippingCostForBill(plant, pono);
		    double shipcost = Double.valueOf(actualShippingCost) * pohdr.getCURRENCYUSEQT();
		    double oddiscount = pohdr.getORDERDISCOUNT();
		    if(!pohdr.getORDERDISCOUNTTYPE().equalsIgnoreCase("%")) {
		    	 String actualDiscountCost = poHdrDao.getActualDiscoutForBill(plant, pono);
		    	 oddiscount = Double.valueOf(actualDiscountCost) * pohdr.getCURRENCYUSEQT();
		    }
		    
		    actualShippingCost = StrUtils.addZeroes(shipcost, numberOfDecimal);
		    if (listQry.size() > 0) {
		    	for(int i =0; i<listQry.size(); i++) {   
		    		Map m=(Map)listQry.get(i);
					JSONObject resultJsonInt = new JSONObject();
					
					double dCost = Double.parseDouble((String)m.get("UNITCOST"));
					ConvertedUnitCost = StrUtils.addZeroes(dCost, numberOfDecimal);
					
					double dUnitCost_Aod = Double.parseDouble((String)m.get("UNITCOST_AOD"));
					ConvertedUnitCost_Aod = StrUtils.addZeroes(dUnitCost_Aod, numberOfDecimal);
					
					double dQty = Double.parseDouble((String)m.get("RECQTY"));
					double dAmount = dCost * dQty;
					ConvertedAmount = StrUtils.addZeroes(dAmount, numberOfDecimal);
					
					double dConvertedAmount_Aod = dUnitCost_Aod * dQty;
					ConvertedAmount_Aod = StrUtils.addZeroes(dConvertedAmount_Aod, numberOfDecimal);
					
					PoDet podet = podetdao.getPoDetByPonoItem(plant, (String)m.get("PONO"), (String)m.get("ITEM"));
					
					resultJsonInt.put("PONO", (String)m.get("PONO"));
					resultJsonInt.put("POLNNO", (String)m.get("LNNO"));
					resultJsonInt.put("CURRENCYID", (String)m.get("CURRENCYID"));
					resultJsonInt.put("INBOUND_GST", (String)m.get("INBOUND_GST"));
					resultJsonInt.put("ORDERDISCOUNT", oddiscount);
					resultJsonInt.put("SHIPPINGCOST", (String)m.get("SHIPPINGCOST"));
					resultJsonInt.put("LOCALEXPENSES", (String)m.get("LOCALEXPENSES"));
					resultJsonInt.put("ITEM", (String)m.get("ITEM"));
					resultJsonInt.put("ITEMDESC", (String)m.get("ITEMDESC"));
					resultJsonInt.put("UNITCOST", ConvertedUnitCost);
					//resultJsonInt.put("UNITMO", (String)m.get("UNITMO"));
					resultJsonInt.put("QTYOR", (String)m.get("RECQTY"));
					//resultJsonInt.put("CONVCOST", (String)m.get("CONVCOST"));
					resultJsonInt.put("ACCOUNT", "Inventory Asset");
					resultJsonInt.put("AMOUNT", ConvertedAmount);
					String catlogpath = StrUtils.fString((String)m.get("CATLOGPATH"));
                    resultJsonInt.put("CATLOGPATH", ((catlogpath == "") ? "../jsp/dist/img/NO_IMG.png" : "/track/ReadFileServlet/?fileLocation="+catlogpath));
                    resultJsonInt.put("ACTUALSHIPPINGCOST", actualShippingCost);
                    resultJsonInt.put("PURCHASE_LOCATION", (String)m.get("PURCHASE_LOCATION"));
                    resultJsonInt.put("STATE_PREFIX", (String)m.get("STATE_PREFIX"));
                    resultJsonInt.put("TAXTREATMENT", (String)m.get("TAXTREATMENT"));
                    resultJsonInt.put("CURRENCY_CODE", (String)m.get("CURRENCY_CODE"));
                    resultJsonInt.put("CURRENCYUSEQT", (String)m.get("CURRENCYUSEQT"));
                    resultJsonInt.put("BASECOST", (String)m.get("BASECOST"));
                    resultJsonInt.put("CONVCOST", (String)m.get("UNITCOST"));
                    resultJsonInt.put("EMPNO", (String)m.get("EMPNO"));
                    resultJsonInt.put("EMP_NAME", (String)m.get("EMP_NAME"));
                    resultJsonInt.put("GOOD_DATE", (String)m.get("RECVDATE"));
                    
                    resultJsonInt.put("ITEM_DISCOUNT", podet.getDISCOUNT());
                    resultJsonInt.put("UOM", podet.getUNITMO());
                    resultJsonInt.put("ITEM_DISCOUNT_TYPE", podet.getDISCOUNT_TYPE());
                    resultJsonInt.put("TAX_TYPE", podet.getTAX_TYPE());
                    resultJsonInt.put("ITEMRATES", pohdr.getISTAXINCLUSIVE());
                    resultJsonInt.put("ORQTY", podet.getQTYOR());
                    resultJsonInt.put("ORDERDISCOUNT_TYPE", pohdr.getORDERDISCOUNTTYPE());
                    resultJsonInt.put("ISDISCOUNTTAX", pohdr.getISDISCOUNTTAX());
                    resultJsonInt.put("ISSHIPPINGTAX", pohdr.getISSHIPPINGTAX());
                    resultJsonInt.put("TAXID", pohdr.getTAXID());
					resultJsonInt.put("CONVERTEDUNITCOST_AOD", ConvertedUnitCost_Aod);
                    resultJsonInt.put("CONVERTEDAMOUNT_AOD", ConvertedAmount_Aod);
                    if(pohdr.getTAXID() != 0) {
                    	fintaxtype = finCountryTaxTypeDAO.getCountryTaxTypesByid(pohdr.getTAXID());
                    	resultJsonInt.put("ptaxtype",fintaxtype.getTAXTYPE());
                    	resultJsonInt.put("ptaxiszero",fintaxtype.getISZERO());
                    	resultJsonInt.put("ptaxisshow",fintaxtype.getSHOWTAX());
                    }else {
                    	resultJsonInt.put("ptaxtype","");
                    	resultJsonInt.put("ptaxiszero","1");
                    	resultJsonInt.put("ptaxisshow","0");
                    }
            		resultJsonInt.put("TRANSPORTID", (String)m.get("TRANSPORTID"));
					TransportModeDAO transportmodedao = new TransportModeDAO();
            		String transportmode = "";
            		int transportid = Integer.valueOf((String)m.get("TRANSPORTID"));
            		if(transportid > 0){
            			transportmode = transportmodedao.getTransportModeById(plant,transportid);
            		}else{
            			transportmode = "";
            		}
            		resultJsonInt.put("TRANSPORTNAME", transportmode);
            		resultJsonInt.put("PAYMENT_TERMS", pohdr.getPAYMENT_TERMS());
            		resultJsonInt.put("DELDATE", pohdr.getDELDATE());
                    
                    
                    resultJsonInt.put("SHIPPINGCUSTOMER", pohdr.getSHIPPINGCUSTOMER());
                    resultJsonInt.put("SHIPPINGID", pohdr.getSHIPPINGID());
                    if(pohdr.getPROJECTID() == 0) {
                    	 resultJsonInt.put("PROJECTNAME", "");
                    	 resultJsonInt.put("PROJECTID", "");
                    }else {
                    	FinProjectDAO finProjectDAO = new FinProjectDAO();
                		FinProject finProject= finProjectDAO.getFinProjectById(plant, pohdr.getPROJECTID());
                		resultJsonInt.put("PROJECTNAME", finProject.getPROJECT_NAME());
                		resultJsonInt.put("PROJECTID", pohdr.getPROJECTID());
                    }
                    
                  //IMTI modified on 14-03-2022 to display qty and cost hover
                    String IBDiscount = new POUtil().getIBDiscountSelectedItemVNO(plant,pohdr.getCustCode(),
                    		(String)m.get("ITEM"));
					String discounttype = "";

					int plusIndex = IBDiscount.indexOf("%");
					if (plusIndex != -1) {
						IBDiscount = IBDiscount.substring(0, plusIndex);
						discounttype = "BYPERCENTAGE";
					}
					if(IBDiscount.equalsIgnoreCase(""))
						IBDiscount="0.00";
        			List listItem = itemUtil.queryItemMstDetailsforpurchase((String)m.get("ITEM"), plant);
        			
        			//IMTI modified on 17-03-2022 To display aval.qty in hover
        			String estQty = "", avlbQty = "";
                    Hashtable hts = new Hashtable();
             	 	hts.put("item", (String)m.get("ITEM"));
             	 	hts.put("plant", plant);
             	 	Map ma = new EstDetDAO().getEstQtyByProduct(hts);
             	 	estQty = (String) ma.get("ESTQTY");
             	 	
					Vector arrItem = (Vector) listItem.get(0);
					if (arrItem.size() > 0) {
						//ma = new InvMstDAO().getPOAvailableQtyByProduct(hts,StrUtils.fString((String)arrItem.get(41)));
						ma = new InvMstDAO().getPOAvailableQtyByProduct(hts,"0");
						avlbQty= (String) ma.get("AVLBQTY");
						resultJsonInt.put("minstkqty", StrUtils.fString((String) arrItem.get(8)));
						resultJsonInt.put("maxstkqty", StrUtils.fString((String) arrItem.get(21)));
						resultJsonInt.put("stockonhand", StrUtils.fString((String) arrItem.get(22)));
						resultJsonInt.put("outgoingqty", StrUtils.fString((String) arrItem.get(23)));
						resultJsonInt.put("customerdiscount", IBDiscount);
						resultJsonInt.put("discounttype", discounttype);
						resultJsonInt.put("incommingqty", StrUtils.fString((String) arrItem.get(41)));
						resultJsonInt.put("EstQty",estQty);
//						resultJsonInt.put("EstQty", "0.00");
						resultJsonInt.put("AvlbQty",avlbQty);
//						resultJsonInt.put("AvlbQty", "0.00");
					}
					//END
					
                    jsonArray.add(resultJsonInt);
		    	}
		    	resultJson.put("orders", jsonArray);
	            JSONObject resultJsonInt = new JSONObject();
	            resultJsonInt.put("ERROR_MESSAGE", "NO ERRORS!");
	            resultJsonInt.put("ERROR_CODE", "100");
	            jsonArrayErr.add(resultJsonInt);
	            resultJson.put("errors", jsonArrayErr);
		    } else {
                JSONObject resultJsonInt = new JSONObject();
                resultJsonInt.put("ERROR_MESSAGE", "NO ORDER RECORD FOUND!");
                resultJsonInt.put("ERROR_CODE", "99");
                jsonArrayErr.add(resultJsonInt);
                jsonArray.add("");
                resultJson.put("items", jsonArray);
                resultJson.put("errors", jsonArrayErr);
		     }
		}catch (Exception e) {
			resultJson.put("SEARCH_DATA", "");
            JSONObject resultJsonInt = new JSONObject();
            resultJsonInt.put("ERROR_MESSAGE",  ThrowableUtil.getMessage(e));
            resultJsonInt.put("ERROR_CODE", "98");
            jsonArrayErr.add(resultJsonInt);
            resultJson.put("ERROR", jsonArrayErr);
		}
		return resultJson;
	}
	private JSONObject getGRNOForAutoSuggestion(HttpServletRequest request) {
		JSONObject resultJson = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        JSONArray jsonArrayErr = new JSONArray();
        
        POUtil itemUtil = new POUtil();
//        StrUtils strUtils = new StrUtils();
        itemUtil.setmLogger(mLogger);
        
        try {
        	String plant = StrUtils.fString(request.getParameter("PLANT")).trim();
		    String pono = StrUtils.fString(request.getParameter("PONO")).trim();
		    String grno = StrUtils.fString(request.getParameter("GRNO")).trim();
		    String cname = StrUtils.fString(request.getParameter("CNAME")).trim();
		    
		    Hashtable ht=new Hashtable();
		     String extCond="";
		     ht.put("PLANT",plant);		     
		     if(grno.length()>0) extCond=" AND plant='"+plant+"' AND GRNO like '"+grno+"%' ";
		     if(pono.length()>0) extCond=" AND PONO like '"+pono+"%' ";
		     if(cname.length()>0) extCond +=" AND CNAME = '"+cname+"' ";
		     
		     extCond=extCond+" and (grno!='' or  grno is not null)";
		     ArrayList listQry = itemUtil.getIBRecvList("distinct ISNULL(GRNO,'') GRNO ", ht, extCond);
		     if (listQry.size() > 0) {
		    	 for(int i =0; i<listQry.size(); i++) {		   
				  Map m=(Map)listQry.get(i);
				  grno = (String)m.get("GRNO");
				  
				  JSONObject resultJsonInt = new JSONObject();
				  resultJsonInt.put("GRNO", grno);				  
				  jsonArray.add(resultJsonInt);
			     }	     
			     resultJson.put("orders", jsonArray);
	             JSONObject resultJsonInt = new JSONObject();
	             resultJsonInt.put("ERROR_MESSAGE", "NO ERRORS!");
	             resultJsonInt.put("ERROR_CODE", "100");
	             jsonArrayErr.add(resultJsonInt);
	             resultJson.put("errors", jsonArrayErr);
		     } else {
                 JSONObject resultJsonInt = new JSONObject();
                 resultJsonInt.put("ERROR_MESSAGE", "NO ORDER RECORD FOUND!");
                 resultJsonInt.put("ERROR_CODE", "99");
                 jsonArrayErr.add(resultJsonInt);
                 jsonArray.add("");
                 resultJson.put("items", jsonArray);
                 resultJson.put("errors", jsonArrayErr);
		     }
        }catch (Exception e) {
            resultJson.put("SEARCH_DATA", "");
            JSONObject resultJsonInt = new JSONObject();
            resultJsonInt.put("ERROR_MESSAGE",  ThrowableUtil.getMessage(e));
            resultJsonInt.put("ERROR_CODE", "98");
            jsonArrayErr.add(resultJsonInt);
            resultJson.put("ERROR", jsonArrayErr);
        }
        return resultJson;
	}
	
	private JSONObject getPreviousOrderDetails(HttpServletRequest request) {
		JSONObject resultJson = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        JSONArray jsonArrayErr = new JSONArray();
        
        POUtil poUtil = new POUtil();
//        StrUtils strUtils = new StrUtils();
        poUtil.setmLogger(mLogger);
        
        try {
        	String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
		    String custCode = StrUtils.fString(request.getParameter("CUSTCODE")).trim();
		    String item = StrUtils.fString(request.getParameter("ITEM")).trim();
		    String rows = StrUtils.fString(request.getParameter("ROWS")).trim();
		    String uom = StrUtils.fString(request.getParameter("UOM")).trim();
		    Hashtable ht=new Hashtable();
//		    String extCond="";
		    ht.put("PLANT",plant);
		    ht.put("ITEM",item);
		    ht.put("VENDNO",custCode);
		    ht.put("UOM",uom);
		    if(rows.equalsIgnoreCase(""))
		    	rows = "1";
		    List listQry = poUtil.getPreviousOrderDetails(ht,rows);
		    if (listQry.size() > 0) {
		    	 for(int i =0; i<listQry.size(); i++) {		   
				  Map m=(Map)listQry.get(i);
				  String pono = (String)m.get("PONO");
				  String custcode = (String)m.get("CustCode");
				  String custName = (String)m.get("VNAME");
				  item = (String)m.get("ITEM");
				  String collectionDate = (String)m.get("CollectionDate");
				  String unitCost = (String)m.get("UNITCOST");
				  JSONObject resultJsonInt = new JSONObject();
				  resultJsonInt.put("PONO", pono);
				  resultJsonInt.put("CUSTCODE", custcode);
				  resultJsonInt.put("CUSTNAME", custName);				  
				  resultJsonInt.put("ITEM", item);
				  resultJsonInt.put("COLLECTIONDATE", collectionDate);
				  resultJsonInt.put("UNITCOST", unitCost);			  
				  jsonArray.add(resultJsonInt);
			     }
			     resultJson.put("orders", jsonArray);
	             JSONObject resultJsonInt = new JSONObject();
	             resultJsonInt.put("ERROR_MESSAGE", "NO ERRORS!");
	             resultJsonInt.put("ERROR_CODE", "100");
	             jsonArrayErr.add(resultJsonInt);
	             resultJson.put("errors", jsonArrayErr);
		     } else {
                 JSONObject resultJsonInt = new JSONObject();
                 resultJsonInt.put("ERROR_MESSAGE", "NO ORDER RECORD FOUND!");
                 resultJsonInt.put("ERROR_CODE", "99");
                 jsonArrayErr.add(resultJsonInt);
                 jsonArray.add("");
                 resultJson.put("orders", jsonArray);
                 resultJson.put("errors", jsonArrayErr);
		     }
        }catch (Exception e) {
            resultJson.put("SEARCH_DATA", "");
            JSONObject resultJsonInt = new JSONObject();
            resultJsonInt.put("ERROR_MESSAGE",  ThrowableUtil.getMessage(e));
            resultJsonInt.put("ERROR_CODE", "98");
            jsonArrayErr.add(resultJsonInt);
            resultJson.put("ERROR", jsonArrayErr);
        }
        return resultJson;
	}
	
	
	
	private JSONObject getPreviousEstimateOrderDetails(HttpServletRequest request) {
		JSONObject resultJson = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        JSONArray jsonArrayErr = new JSONArray();
        
        PoEstUtil poEstUtil = new PoEstUtil();
//        StrUtils strUtils = new StrUtils();
        poEstUtil.setmLogger(mLogger);
        
        try {
        	String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
		    String custCode = StrUtils.fString(request.getParameter("CUSTCODE")).trim();
		    String item = StrUtils.fString(request.getParameter("ITEM")).trim();
		    String rows = StrUtils.fString(request.getParameter("ROWS")).trim();
		    String uom = StrUtils.fString(request.getParameter("UOM")).trim();
		    Hashtable ht=new Hashtable();
//		    String extCond="";
		    ht.put("PLANT",plant);
		    ht.put("ITEM",item);
		    ht.put("VENDNO",custCode);
		    ht.put("UOM",uom);
		    if(rows.equalsIgnoreCase(""))
		    	rows = "1";
		    List listQry = poEstUtil.getPreviousOrderDetails(ht,rows);
		    if (listQry.size() > 0) {
		    	 for(int i =0; i<listQry.size(); i++) {		   
				  Map m=(Map)listQry.get(i);
				  String POESTNO = (String)m.get("POESTNO");
				  String custcode = (String)m.get("CustCode");
				  String custName = (String)m.get("VNAME");
				  item = (String)m.get("ITEM");
				  String collectionDate = (String)m.get("CollectionDate");
				  String unitCost = (String)m.get("UNITCOST");
				  JSONObject resultJsonInt = new JSONObject();
				  resultJsonInt.put("POESTNO", POESTNO);
				  resultJsonInt.put("CUSTCODE", custcode);
				  resultJsonInt.put("CUSTNAME", custName);				  
				  resultJsonInt.put("ITEM", item);
				  resultJsonInt.put("COLLECTIONDATE", collectionDate);
				  resultJsonInt.put("UNITCOST", unitCost);			  
				  jsonArray.add(resultJsonInt);
			     }
			     resultJson.put("orders", jsonArray);
	             JSONObject resultJsonInt = new JSONObject();
	             resultJsonInt.put("ERROR_MESSAGE", "NO ERRORS!");
	             resultJsonInt.put("ERROR_CODE", "100");
	             jsonArrayErr.add(resultJsonInt);
	             resultJson.put("errors", jsonArrayErr);
		     } else {
                 JSONObject resultJsonInt = new JSONObject();
                 resultJsonInt.put("ERROR_MESSAGE", "NO ORDER RECORD FOUND!");
                 resultJsonInt.put("ERROR_CODE", "99");
                 jsonArrayErr.add(resultJsonInt);
                 jsonArray.add("");
                 resultJson.put("orders", jsonArray);
                 resultJson.put("errors", jsonArrayErr);
		     }
        }catch (Exception e) {
            resultJson.put("SEARCH_DATA", "");
            JSONObject resultJsonInt = new JSONObject();
            resultJsonInt.put("ERROR_MESSAGE",  ThrowableUtil.getMessage(e));
            resultJsonInt.put("ERROR_CODE", "98");
            jsonArrayErr.add(resultJsonInt);
            resultJson.put("ERROR", jsonArrayErr);
        }
        return resultJson;
	}
	
	
	private JSONObject getOrderNoForOrderReceiptAutoSuggestion(HttpServletRequest request) {
		JSONObject resultJson = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        JSONArray jsonArrayErr = new JSONArray();
        
        POUtil itemUtil = new POUtil();
//        StrUtils strUtils = new StrUtils();
        itemUtil.setmLogger(mLogger);
        
        try {
        	String plant = StrUtils.fString(request.getParameter("PLANT")).trim();
		    String pono = StrUtils.fString(request.getParameter("PONO")).trim();
		    String cname = StrUtils.fString(request.getParameter("CNAME")).trim();
//		    String type = StrUtils.fString(request.getParameter("TYPE")).trim();
		    
		    Hashtable ht=new Hashtable();
//		     String extCond="";
		     ht.put("PLANT",plant);
		     ht.put("ORDNO",pono);
		     ht.put("VNAME",cname);
		     ht.put("VENDNO",new VendMstDAO().getVendorNoByName(ht));
		     
		     ArrayList listQry = itemUtil.getOrderNoForOrderReceipt(ht);
		     if (listQry.size() > 0) {
		    	 for(int i =0; i<listQry.size(); i++) {		   
				  Map m=(Map)listQry.get(i);
				  pono = (String)m.get("ORDERNO");
				  /*String custName = StrUtils.replaceCharacters2Send((String)m.get("custName"));
				  String custcode = (String)m.get("custcode");
				  String orderdate = (String)m.get("collectiondate");
				  String jobNum = (String)m.get("jobNum");
				  String status = (String)m.get("status");*/
				  JSONObject resultJsonInt = new JSONObject();
				  resultJsonInt.put("PONO", pono);
				  /*resultJsonInt.put("CUSTNAME", custName);
				  resultJsonInt.put("CUSTCODE", custcode);
				  resultJsonInt.put("ORDERDATE", orderdate);
				  resultJsonInt.put("JOBNUM", jobNum);
				  resultJsonInt.put("STATUS", status);*/			  
				  jsonArray.add(resultJsonInt);
			     }	     
			     resultJson.put("orders", jsonArray);
	             JSONObject resultJsonInt = new JSONObject();
	             resultJsonInt.put("ERROR_MESSAGE", "NO ERRORS!");
	             resultJsonInt.put("ERROR_CODE", "100");
	             jsonArrayErr.add(resultJsonInt);
	             resultJson.put("errors", jsonArrayErr);
		     } else {
                 JSONObject resultJsonInt = new JSONObject();
                 resultJsonInt.put("ERROR_MESSAGE", "NO ORDER RECORD FOUND!");
                 resultJsonInt.put("ERROR_CODE", "99");
                 jsonArrayErr.add(resultJsonInt);
                 jsonArray.add("");
                 resultJson.put("items", jsonArray);
                 resultJson.put("errors", jsonArrayErr);
		     }
        }catch (Exception e) {
            resultJson.put("SEARCH_DATA", "");
            JSONObject resultJsonInt = new JSONObject();
            resultJsonInt.put("ERROR_MESSAGE",  ThrowableUtil.getMessage(e));
            resultJsonInt.put("ERROR_CODE", "98");
            jsonArrayErr.add(resultJsonInt);
            resultJson.put("ERROR", jsonArrayErr);
        }
        return resultJson;
	}

}
