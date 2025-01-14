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
import com.track.dao.BillDAO;
import com.track.dao.CurrencyDAO;
import com.track.dao.CustomerBeanDAO;
import com.track.dao.DoDetDAO;
import com.track.dao.DoHdrDAO;
import com.track.dao.EmailMsgDAO;
import com.track.dao.EmployeeDAO;
import com.track.dao.EstDetDAO;
import com.track.dao.EstHdrDAO;
import com.track.dao.FinCountryTaxTypeDAO;
import com.track.dao.InvoiceDAO;
import com.track.dao.InvoicePaymentDAO;
import com.track.dao.ItemMstDAO;
import com.track.dao.MasterDAO;
import com.track.dao.MovHisDAO;
import com.track.dao.OrderTypeBeanDAO;
import com.track.dao.OrderTypeDAO;
import com.track.dao.PlantMstDAO;
import com.track.dao.PoDetDAO;
import com.track.dao.ShipHisDAO;
import com.track.dao.TblControlDAO;
import com.track.dao.TransportModeDAO;
import com.track.db.object.DoHdr;
import com.track.db.object.FinCountryTaxType;
import com.track.db.object.InvPaymentDetail;
import com.track.db.util.CurrencyUtil;
import com.track.db.util.CustUtil;
import com.track.db.util.DOTransferUtil;
import com.track.db.util.DOUtil;
import com.track.db.util.EmailMsgUtil;
import com.track.db.util.ExpensesUtil;
import com.track.db.util.HTReportUtil;
import com.track.db.util.ItemMstUtil;
import com.track.db.util.MasterUtil;
import com.track.db.util.OrderPaymentUtil;
import com.track.db.util.POUtil;
import com.track.db.util.PlantMstUtil;
import com.track.db.util.TblControlUtil;
/*- ************Modification History*********************************
Dec 16 2014 Bruhan, Description:Modify Method:viewDOReport,viewInvoiceReport - Include parameter RCBNO 

*/
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
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
@SuppressWarnings({"rawtypes", "unchecked"})
public class DeleveryOrderServlet extends HttpServlet implements IMLogger {
/**
	 * 
	 */
	private static final long serialVersionUID = -5999733331550282445L;
	//	private static final String CONTENT_TYPE = "text/html; charset=windows-1252";
	private MLogger mLogger = new MLogger();
	private boolean printLog = MLoggerConstant.DeleveryOrderServlet_PRINTPLANTMASTERLOG;
	private boolean printInfo = MLoggerConstant.DeleveryOrderServlet_PRINTPLANTMASTERINFO;

	public MLogger getmLogger() {
		return mLogger;
	}

	public void setmLogger(MLogger mLogger) {
		this.mLogger = mLogger;
	}

	String action = "";
	String xmlStr = "";
	String statusValue = "";
	DOUtil dOUtil = null;
	HTReportUtil htutil = null;
	DOTransferUtil _DOTransferUtil = null;
	CurrencyUtil curUtil = new CurrencyUtil();
	EmailMsgDAO emailDao = null;
	EmailMsgUtil mailUtil = null;
	TblControlDAO _TblControlDAO = null;
	DoDetDAO _DoDetDAO = null;
	PoDetDAO _PoDetDAO = null;
	ItemMstDAO _ItemMstDAO = null;
	POUtil pOUtil = null;
	MasterDAO _MasterDAO = null;
	PlantMstDAO _PlantMstDAO = null;
	MasterUtil _masterUtil = null;

	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		dOUtil = new DOUtil();
		htutil = new HTReportUtil();
		_DOTransferUtil = new DOTransferUtil();
		emailDao = new EmailMsgDAO();
		mailUtil = new EmailMsgUtil();
		_TblControlDAO = new TblControlDAO();
		_DoDetDAO = new DoDetDAO();
		_PoDetDAO = new PoDetDAO();
		_ItemMstDAO = new ItemMstDAO();
		pOUtil = new POUtil();
		_MasterDAO = new MasterDAO();
		_PlantMstDAO = new PlantMstDAO();
		_masterUtil = new MasterUtil();

	}

	/*********************
	 * Modification History*********************************** Bruhan,Oct 23 2014,To
	 * update Add Products,Add product,Updatedodet and Delete to allow outbound
	 * order to amend,add products once a order was processed
	 * 
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			action = StrUtils.fString(request.getParameter("Submit")).trim();
//			String orderNo = StrUtils.fString(request.getParameter("DONO")).trim();
			String rflag = StrUtils.fString((String) request.getSession().getAttribute("RFLAG"));
			statusValue = StrUtils.fString(request.getParameter("statusValue"));
			String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
			String userName = StrUtils.fString((String) request.getSession().getAttribute("LOGIN_USER")).trim();
			this.setMapDataToLogger(this.populateMapData(plant, userName));
			dOUtil.setmLogger(mLogger);
			if (this.printInfo) {
				diaplayInfoLogs(request);
			}
			if (action.equalsIgnoreCase("View")) {

//				boolean flag = 
						DisplayData(request, response);
				String dono = "";
				if (rflag.equals("1")) {
					dono = (String) request.getSession().getAttribute("dono");
					request.getSession().setAttribute("dono", "");
					response.sendRedirect("jsp/CreateOutgoingOrder.jsp?DONO=" + dono + "&action=View");
				} else if (rflag.equals("2")) {
					dono = (String) request.getSession().getAttribute("dono");
					request.getSession().setAttribute("dono", "");
					response.sendRedirect("jsp/OutBoundSummary.jsp?DONO=" + dono + "&action=View");
				} else if (rflag.equals("3")) {
					dono = (String) request.getSession().getAttribute("dono");
					request.getSession().setAttribute("dono", "");
					response.sendRedirect("jsp/CreateOutgoingOrder.jsp?DONO=" + dono + "&action=View");
				} else if (rflag.equals("4")) {
					dono = (String) request.getSession().getAttribute("dono");
					if (request.getParameter("DONO") != null)
						dono = request.getParameter("DONO");
					request.getSession().setAttribute("dono", "");
					response.sendRedirect(
							"jsp/maintOutgoingOrder.jsp?DONO=" + dono + "&statusValue=" + statusValue + "&action=View");
				} else {
					dono = (String) request.getSession().getAttribute("dono");
					if (request.getParameter("DONO") != null)
						dono = request.getParameter("DONO");
					request.getSession().setAttribute("dono", "");
					response.sendRedirect(
							"jsp/maintOutgoingOrder.jsp?DONO=" + dono + "&statusValue=" + statusValue + "&action=View");
				}
			} else if (action.equalsIgnoreCase("print")) {

				String dono = StrUtils.fString(request.getParameter("DONO"));
				String fieldDesc = "<tr><td> Please enter serach condition and press GO</td></tr>";

				ArrayList al = new ArrayList();
				Hashtable<String, String> htCond = new Hashtable<>();
				Map m = new HashMap();
				DOUtil _doUtil = new DOUtil();
				if (dono.length() > 0) {
					htCond.put("PLANT", plant);
					htCond.put("DONO", dono);
					String query = "dono,isnull(ordertype,''),isnull(outbound_Gst,0) outbound_Gst,custName,custCode,isnull(jobNum,'') jobNum, isnull(ordertype,'') ordertype,custName,isnull(personInCharge,'')personInCharge ,isnull(contactNum,'') contactNum,isnull(address,'') address,isnull(address2,'') address2,isnull(address3,'') address3,collectionDate,isnull(collectionTime,'')collectionTime,remark1,remark2,isnull(b.name,'') as contactname,isnull(b.telno,'') as telno,isnull(b.email,'') email,isnull(b.addr1,'') add1,isnull(b.addr2,'') add2,isnull(b.addr3,'') add3,isnull(b.remarks,'') remarks,isnull(b.addr4,'') add4,isnull(b.country,'') country,isnull(b.zip,'') zip  ";
					al = _doUtil.getOutGoingDoHdrDetails(query, htCond);
					if (al.size() > 0) {
						m = (Map) al.get(0);
						fieldDesc = _doUtil.listDODETToPrint(plant, dono, rflag);
					} else {
						fieldDesc = "<tr><td colspan=\"9\" align=\"center\">No Records Available</td></tr>";
					}

				}
				request.getSession().setAttribute("podetVal", m);
				request.getSession().setAttribute("RESULT", fieldDesc);
				response.sendRedirect("jsp/CreateOutgoingOrder_print.jsp?DONO=" + dono + "&action=View");

			} else if (action.equalsIgnoreCase("Auto-Generate")) {

				if (rflag.equals("1") || rflag.equals("3")) {
					response.sendRedirect("jsp/CreateOutgoingOrder.jsp?action=Auto-Generate");
				} else if (rflag.equals("5")) {
					response.sendRedirect("jsp/createMobileOrder.jsp?action=Auto-Generate");
				} else if (rflag.equals("7")) {
					response.sendRedirect("jsp/createMobileEnquiry.jsp?action=Auto-Generate");
				} else if (rflag.equals("9")) {
					response.sendRedirect("jsp/createMobileRegistration.jsp?action=Auto-Generate");
				}

			}
			if (action.equalsIgnoreCase("Print Outbound Order")) {
				viewDOReport(request, response);
			}
			if (action.equalsIgnoreCase("Print Outbound Order With Price")) {
				viewInvoiceReport(request, response);
			}
			if (action.equalsIgnoreCase("Print Invoice With Price")) {
				printInvoiceReport(request, response);
			}
			if (action.equalsIgnoreCase("Print Bill With Price")) {
				printBillReport(request, response);
			}
			if (action.equalsIgnoreCase("Print Multiple DO")) {
				viewMultipleDOReport(request, response);
			}
			if (action.equalsIgnoreCase("Print Multiple Invoice")) {
				viewMultipleDOInvoiceReport(request, response);
			}
			if (action.equalsIgnoreCase("Print Pending Outbound Order")) {
				viewPendingDOReport(request, response);
			}
			// start code by Bruhan for export to excel Sales order product details on
			// 25 April 2013
			if (action.equalsIgnoreCase("Export To Excel")) {
				HSSFWorkbook wb = new HSSFWorkbook();
				wb = this.writeToExcel(request, response, wb);
				ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
				wb.write(outByteStream);
				byte[] outArray = outByteStream.toByteArray();
				response.setContentType("application/ms-excel");
				response.setContentLength(outArray.length);
				response.setHeader("Expires:", "0"); // eliminates browser caching
				response.setHeader("Content-Disposition", "attachment; filename=Salesorder.xls");
				OutputStream outStream = response.getOutputStream();
				outStream.write(outArray);
				outStream.flush();

			}
			// End code by Bruhan for export to excel Sales order product details on 25
			// April 2013
			if (action.equalsIgnoreCase("Print Mobile Order With Price")) {
				viewInvoiceReport(request, response);
			}
			if (action.equalsIgnoreCase("Print Mobile Enquiry")) {
				viewDOReport(request, response);
			}
			if (action.equalsIgnoreCase("Print Mobile Registration")) {
				viewDOReport(request, response);
			} else if (action.equalsIgnoreCase("Add Products")) {

				String ordertypeString = "", orderaddProductPage = "";
				String result = "";
				ArrayList al = new ArrayList();
				Hashtable htCond = new Hashtable();
				String dono = StrUtils.fString(request.getParameter("DONO")).trim();
				String RFLAG = StrUtils.fString(request.getParameter("RFLAG")).trim();

				htCond.put("PLANT", plant);
				htCond.put("DONO", dono);
				String query = "dono,custCode,custName,jobNum";
				dOUtil.setmLogger(mLogger);
				al = dOUtil.getDoHdrDetails(query, htCond);
				if (RFLAG.equals("1") || RFLAG.equals("3") || RFLAG.equals("4")) {
					ordertypeString = "Outbound";
					orderaddProductPage = "createDODET.jsp";
				} else if (RFLAG.equals("5")) {
					ordertypeString = "Mobile Shopping";
					orderaddProductPage = "addMobileOrderProducts.jsp";

				} else if (RFLAG.equals("6")) {
					ordertypeString = "Maint Mobile Shopping";
					orderaddProductPage = "addMobileOrderProducts.jsp";
				} else if (RFLAG.equals("7")) {
					ordertypeString = "Mobile Enquiry";
					orderaddProductPage = "addMobileEnquiryProducts.jsp";
				} else if (RFLAG.equals("8")) {
					ordertypeString = "Mobile Enquiry";
					orderaddProductPage = "addMobileEnquiryProducts.jsp";
				} else if (RFLAG.equals("9")) {
					ordertypeString = "Mobile Registration";
					orderaddProductPage = "addMobileRegisterProducts.jsp";
				} else if (RFLAG.equals("10")) {
					ordertypeString = "Mobile Registration";
					orderaddProductPage = "addMobileRegisterProducts.jsp";
				}
				if (al.size() < 1) {

					result = "<font color=\"red\"> Please Save " + ordertypeString
							+ " order first before adding product  <br><br><center>"
							+ "<input type=\"button\" value=\"Back\" onClick=\"window.location.href='javascript:history.back()'\"> ";
					result = ""  + "<br><h3>" + result;
					request.getSession().setAttribute("RESULT", result);
					response.sendRedirect("jsp/displayResult2User.jsp");
				} else {

					if (DbBean.OB_MODIFICATION.equals("Y")) {

						Map m = (Map) al.get(0);
						dono = (String) m.get("dono");
						String jobNum = (String) m.get("jobNum");
						String custName = (String) m.get("custName");
						String custCode = (String) m.get("custCode");
						response.sendRedirect("jsp/" + orderaddProductPage + "?Submit=add&DONO=" + dono + "&JOB_NUM="
								+ StrUtils.replaceCharacters2Send(jobNum) + "&CUST_CODE=" + custCode + "&RFLAG=" + RFLAG
								+ "&CUST_NAME=" + StrUtils.replaceCharacters2Send(custName));

					} else {
						Boolean flag = dOUtil.isOpenOutBoundOrder(plant, dono);
						if (!flag) {
							throw new Exception(" Processed Sales Order Cannot be modified");
						}
						Map m = (Map) al.get(0);
						dono = (String) m.get("dono");
						String jobNum = (String) m.get("jobNum");
						String custName = (String) m.get("custName");
						String custCode = (String) m.get("custCode");
						response.sendRedirect("jsp/" + orderaddProductPage + "?Submit=add&DONO=" + dono + "&JOB_NUM="
								+ StrUtils.replaceCharacters2Send(jobNum) + "&CUST_CODE=" + custCode + "&RFLAG=" + RFLAG + "&CUST_NAME="
								+ StrUtils.replaceCharacters2Send(custName));
					}
				}

			} else if (action.equalsIgnoreCase("UPDATE")) {
				String result = updateDoHdr(request, response);
				request.getSession().setAttribute("RESULT", result);
				response.sendRedirect("jsp/displayResult2User.jsp");
			} else if (action.equalsIgnoreCase("SAVE")) {
				String result = "";
				result = SaveData(request, response);
				request.getSession().setAttribute("RESULT", result);
				response.sendRedirect("jsp/displayResult2User.jsp");
			} else if (action.equalsIgnoreCase("Delete")) {
				try {
					boolean flag = false;
					String fieldDesc = "<tr><td> Please enter serach condition and press GO</td></tr>";
//					String User_id = StrUtils.fString(request.getParameter("LOGIN_USER")).trim();
					String ordno = StrUtils.fString(request.getParameter("DONO")).trim();
					String ordlno = StrUtils.fString(request.getParameter("DOLNNO")).trim();
					String item = StrUtils.fString(request.getParameter("ITEM")).trim();
					Hashtable htdet = new Hashtable();
					htdet.put(IDBConstants.PLANT, plant);
					htdet.put(IDBConstants.DODET_DONUM, ordno);
					htdet.put(IDBConstants.DODET_DOLNNO, ordlno);
					htdet.put(IDBConstants.DODET_ITEM, item);
					// htdet.put(IDBConstants.CREATED_BY, User_id);
					boolean isExistsPayment = false;
					isExistsPayment = new OrderPaymentUtil().isExistsOrderPaymentDetails(plant, ordno);
					if (!isExistsPayment) {
						String q = "", estno = "", estlnno = "", qty = "", queryest = "", updateEstHdr = "",
								updatestatus = "";
						q = " isnull(estno,'') estno,isnull(estlnno,'') as estlnno,isnull(qtyor,0)as qty ";
						ArrayList aldet = _DoDetDAO.selectDoDet(q, htdet, " plant <> ''");
						// delete dodet multi remarks
						/*
						 * Hashtable htRemarksDel = new Hashtable();
						 * htRemarksDel.put(IDBConstants.PLANT,plant);
						 * htRemarksDel.put(IDBConstants.DODET_DONUM, ordno);
						 * htRemarksDel.put(IDBConstants.DODET_DOLNNO,ordlno);
						 * htRemarksDel.put(IDBConstants.DODET_ITEM, item); flag =
						 * _DoDetDAO.deleteDoMultiRemarks(htRemarksDel);
						 */
						// delete dodet multi remarks end
						for (int i = 0; i < aldet.size(); i++) {
							Map m = (Map) aldet.get(i);
							estno = (String) m.get("estno");
							estlnno = (String) m.get("estlnno");
							qty = (String) m.get("qty");

						}

						if (estno.length() > 0) {
							EstDetDAO _EstDetDAO = new EstDetDAO();
							EstHdrDAO _EstHdrDAO = new EstHdrDAO();
							Hashtable<String, String> htestCond = new Hashtable<>();
							htestCond.put("PLANT", plant);
							htestCond.put("estno", estno);
							htestCond.put("estlnno", estlnno);
							htestCond.put("item", item);

							queryest = "set qtyis= isNull(qtyis,0) - " + qty;
							flag = _EstDetDAO.update(queryest, htestCond, "");

							updatestatus = "set STATUS=CASE WHEN qtyis > 0 THEN 'O' ELSE 'N' END";
							flag = _EstDetDAO.update(updatestatus, htestCond, "");

							updateEstHdr = "set  status='Pending' ";
							htestCond.remove("estlnno");
							htestCond.remove("item");
							flag = _EstHdrDAO.update(updateEstHdr, htestCond, "");

						}
						dOUtil.setmLogger(mLogger);
						htdet.put(IDBConstants.CREATED_BY, userName);
						flag = dOUtil.deleteOBDetLineDetails(htdet);

						if (flag) {
							Hashtable<String, String> htCond = new Hashtable<>();
							htCond.put("PLANT", plant);
							htCond.put("DONO", ordno);
							String query = "dono,custCode";
							ArrayList al = dOUtil.getDoHdrDetails(query, htCond);
							Map m = (Map) al.get(0);
							//	Call Accounting module for delete item : Start
							/*if ("yes".equals(request.getSession(false).getAttribute("isAccountingEnabled"))) {
								try {
										Form form = new Form();
										String custCode = (String) m.get("custCode");
										form.param("data", "{\"dono\":\"" + ordno + "\",\"customer\":\"" + custCode + "\",\"transactionType\":\"3\",\"plant\":\"" + plant + "\",\"user\":\"" + userName + "\",\"action\":\"delproduct\"}");
										String finTransactionResponse = HttpUtils.connect(HttpUtils.getHostInfo(request) + IConstants.ACCOUNTING_APPLICATION_URL.replace("trackaccounting", plant) + "restFinTransaction/create", form);
										if (finTransactionResponse == null || finTransactionResponse.startsWith("Error : ")) {
											throw new Exception(finTransactionResponse);
										}
								}catch(Exception e) {
									throw new Exception(ThrowableUtil.getMessage(e), e);
								}
							}*/
							//	Call Accounting module for delete item : End
							// DOHDR Update
							String updateDoHdr = "";
							boolean updateHdrFlag = false;
							boolean isExitFlag = false;
							DoHdrDAO _DoHdrDAO = new DoHdrDAO();
							DoDetDAO _DoDetDAO = new DoDetDAO();
							Hashtable htConditoHdr = new Hashtable();
							htConditoHdr.put("PLANT", plant);
							htConditoHdr.put("dono", ordno);
							isExitFlag = _DoDetDAO.isExisit(htConditoHdr, "lnstat in ('O','N')");
							if (isExitFlag) {
								updateHdrFlag = false;
							} else {
								updateDoHdr = "set  Status='C',PickStaus='C' ";
								updateHdrFlag = true;
							}

							if (updateHdrFlag == true) {
								flag = _DoHdrDAO.update(updateDoHdr, htConditoHdr, "");
							}
							// DOHDR Update
							al = new ArrayList();
							htCond = new Hashtable();
							m = new HashMap();
							if (ordno.length() > 0) {
								htCond.put("PLANT", plant);
								htCond.put("DONO", ordno);
								query = "isnull(dono,'') dono,isnull(outbound_Gst,0) outbound_Gst,isnull(ordertype,'')ordertype ,"
										+ "(select isnull(display,'') display from " + "[" + plant
										+ "_CURRENCYMST] where currencyid = a.currencyid) currencyid,"
										+ "isnull(custCode,'') custCode,isnull(jobNum,'') jobNum, isnull(ordertype,'') ordertype,"
										+ "isnull(personInCharge,'') personInCharge,isnull(contactNum,'') contactNum,isnull(address,'') address,"
										+ "isnull(address2,'') address2,isnull(address3,'') address3,isnull(collectionDate,'') collectionDate,"
										+ "isnull(collectionTime,'') collectionTime,isnull(remark1,'') remark1,isnull(remark2,'') remarks2,"
										+ "b.CNAME as custName,isnull(b.name,'') as contactname,isnull(b.telno,'') as telno,"
										+ "isnull(b.hpno,'') as hpno,isnull(b.hpno,'') as hpno,isnull(b.email,'') email,"
										+ "isnull(b.addr1,'') add1,isnull(b.addr2,'') add2,isnull(b.addr3,'') add3,"
										+ "isnull(b.remarks,'') remarks,isnull(b.addr4,'') add4,isnull(b.country,'') country,"
										+ "isnull(b.zip,'') zip,isnull(deliverydate,'') deliverydate,isnull(timeslots,'') timeslots,"
										+ "isnull(shippingid,'')as shippingid, isnull(shippingcustomer,'') as shippingcustomer,"
										+ "isnull(STATUS_ID,'') as statusid,isnull(empno,'') as empno,isnull(remark3,'') remark3,"
										+ "ISNULL(CUSTOMER_STATUS_ID,'') customerstatusid,ISNULL(CUSTOMER_TYPE_ID,'') customertypeid,"
										+ "isnull(orderdiscount,0) as orderdiscount,isnull(shippingcost,0) as shippingcost,"
										+ "isnull(incoterms,'') as incoterms,ISNULL(DELIVERYDATEFORMAT,'') as DELIVERYDATEFORMAT,isnull(a.paymenttype,'') payment_terms,"
										+ "ISNULL(A.SALES_LOCATION,'') SALES_LOCATION,ISNULL(A.TAXTREATMENT,'') TAXTREATMENT ";
								al = dOUtil.getOutGoingDoHdrDetails(query, htCond);
								if (al.size() > 0) {
									m = (Map) al.get(0);
									fieldDesc = dOUtil.listDODET(plant, ordno, rflag);
								} else {
									fieldDesc = "<tr><td colspan=\"9\" align=\"center\">No Records Available</td></tr>";

								}
							}
							request.getSession().setAttribute("podetVal", m);
							request.getSession().setAttribute("dono", ordno);
							request.getSession().setAttribute("RESULT", fieldDesc);
							if (rflag.equals("1")) {
								response.sendRedirect("jsp/CreateOutgoingOrder.jsp?DONO=" + ordno + "&action=View");
							}

							else {
								response.sendRedirect("jsp/maintOutgoingOrder.jsp?DONO=" + ordno + "&action=View");
							}
						} else {
							throw new Exception("Product ID Not Deleted Successfully");
						}
					} else {
						throw new Exception("Payment is processed for Sales order: " + ordno
								+ ". Please void payment before Deleting the Product.");
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

			} else if (action.equalsIgnoreCase("REMOVE_DO")) {
				String User_id = StrUtils.fString(request.getParameter("LOGIN_USER")).trim();
				String dono = StrUtils.fString(request.getParameter("DONO")).trim();
				String rFlag = StrUtils.fString(request.getParameter("RFLAG")).trim();
				DoDetDAO dodetDao = new DoDetDAO();

				Hashtable htDoHrd = new Hashtable();
				htDoHrd.put(IDBConstants.PLANT, plant);
				htDoHrd.put(IDBConstants.DODET_DONUM, dono);
				boolean isValidOrder = new DoHdrDAO().isExisit(htDoHrd, "");
				boolean isOrderInProgress = dodetDao.isExisit(htDoHrd,
						"LNSTAT in ('O' ,'C') and ITEM NOT IN(SELECT ITEM FROM [" + plant
								+ "_ITEMMST] where NONSTKFLAG='Y')");
				if (isValidOrder) {
					if (!isOrderInProgress) {

						boolean isExistsPaymentDetails = false;
						isExistsPaymentDetails = new OrderPaymentUtil().isExistsOrderPaymentDetails(plant, dono);
						if (!isExistsPaymentDetails) {
//							Hashtable htCond = new Hashtable();
//							htCond.put("PLANT", plant);
//							htCond.put("DONO", dono);
//							String query = "dono,custCode";
//							ArrayList al = dOUtil.getDoHdrDetails(query, htCond);
//							Map m = (Map) al.get(0);
//							String custCode = (String) m.get("custCode");
							Boolean value = this.dOUtil.removeRow(plant, dono, User_id);
							if (value) {
								//	Call Accounting module for new order : Start
								/*if ("yes".equals(request.getSession(false).getAttribute("isAccountingEnabled"))) {
									try {
											Form form = new Form();
											form.param("data", "{\"dono\":\"" + dono + "\",\"customer\":\"" + custCode + "\",\"transactionType\":\"3\",\"plant\":\"" + plant + "\",\"user\":\"" + userName + "\",\"action\":\"deleteorder\"}");
											String finTransactionResponse = HttpUtils.connect(HttpUtils.getHostInfo(request) + IConstants.ACCOUNTING_APPLICATION_URL.replace("trackaccounting", plant) + "restFinTransaction/create", form);
											if (finTransactionResponse == null || finTransactionResponse.startsWith("Error : ")) {
												throw new Exception(finTransactionResponse);
											}
									}catch(Exception e) {
										throw new Exception(ThrowableUtil.getMessage(e), e);
									}
								}*/
								//	Call Accounting module for new order : End
								response.sendRedirect("/track/deleveryorderservlet?statusValue=100&DONO=" + dono
										+ "&Submit=View&rflag=" + rFlag + "");
							} else {
								response.sendRedirect("/track/deleveryorderservlet?statusValue=97&DONO=" + dono
										+ "&Submit=View&rflag=" + rFlag + "");
							}
						} else {
							response.sendRedirect("/track/deleveryorderservlet?statusValue=96&DONO=" + dono
									+ "&Submit=View&rflag=" + rFlag + "");
						}
					} else {
						response.sendRedirect("/track/deleveryorderservlet?statusValue=98&DONO=" + dono
								+ "&Submit=View&rflag=" + rFlag + "");
					}
				} else {
					response.sendRedirect("/track/deleveryorderservlet?statusValue=99&DONO=" + dono
							+ "&Submit=View&rflag=" + rFlag + "");
				}

			} else if (action.equalsIgnoreCase("Add Product")) {
				boolean dotransferdetflag = false;
				String dono = "", custName = "", custCode = "", jobNum = "", dolno = "", item = "", itemDesc = "",
						result = "", taxby = "", prodgst = "";
				String ordertypeString = "", orderaddProductPage = "";
				MovHisDAO movHisDao = new MovHisDAO();
				ShipHisDAO shipdao = new ShipHisDAO();
				movHisDao.setmLogger(mLogger);
				shipdao.setmLogger(mLogger);
				//boolean shipflag = false;
				dono = StrUtils.fString(request.getParameter("DONO")).trim();
				dolno = StrUtils.fString(request.getParameter("DOLNNO")).trim();
				String user_id = StrUtils.fString(request.getParameter("LOGIN_USER")).trim();
				custCode = StrUtils.fString(request.getParameter("CUST_CODE")).trim();
				custName = StrUtils.fString(request.getParameter("CUST_NAME")).trim();
				jobNum = StrUtils.fString(request.getParameter("JOB_NUM")).trim();
				item = StrUtils.fString(request.getParameter("ITEM")).trim();
				itemDesc = StrUtils.fString(request.getParameter("DESC")).trim();
				String qty = StrUtils.fString(request.getParameter("QTY")).trim();
				String uom = StrUtils.fString(request.getParameter("UOM")).trim();
				String price = StrUtils.removeFormat(StrUtils.fString(request.getParameter("UNITPRICE")).trim());
				String UNITPRICERD= StrUtils.fString(request.getParameter("UNITPRICERD")).trim();
				String PRODUCTDELIVERYDATE= StrUtils.fString(request.getParameter("PRODUCTDELIVERYDATE")).trim();
				if(price.equalsIgnoreCase(""))
					price="0";
				if(UNITPRICERD.equalsIgnoreCase("0"))						
					UNITPRICERD=price;
				else if(UNITPRICERD.equalsIgnoreCase(""))
					UNITPRICERD=price;
				// String productRemarks =
				// StrUtils.fString(request.getParameter("PRDREMARKS")).trim();
				// Start code added by Bruhan for base currency on Aug 28th 2012.
				price = new DOUtil().getConvertedUnitCostToLocalCurrency(plant, dono, UNITPRICERD);
				//	Begin : Ravindra : Check price and quantity variation and update DO Header status
				String hdrUpdate = " SET STATUS_ID = 'PARTIALLY PAID' ";
				Hashtable<String, String> htCondition = new Hashtable<>();
				htCondition.put("PLANT", plant);
				htCondition.put("DONO", dono);
				htCondition.put("STATUS_ID", "PAID");//	Only paid orders status has to be changed
				try {
					new DoHdrDAO().update(hdrUpdate, htCondition, "");
				}catch(Exception e) {
					if (e == null || !"Unable to update!".equals(ThrowableUtil.getMessage(e))) {
						throw new Exception("While updating stauts", e);
					}
				}
				//	End : Ravindra : Check price and quantity variation and update DO Header status
				// Start code added by Bruhan for base currency on Aug 28th 2012.
				String RFLAG = StrUtils.fString(request.getParameter("RFLAG")).trim();
				System.out.print("RFLAG ##############################" + RFLAG);
				if (RFLAG.equals("1") || RFLAG.equals("3") || RFLAG.equals("4")) {
					ordertypeString = "Outbound";
					orderaddProductPage = "createDODET.jsp";
				} else if (RFLAG.equals("5")) {
					ordertypeString = "Mobile Shopping";
					orderaddProductPage = "addMobileOrderProducts.jsp";

				} else if (RFLAG.equals("6")) {
					ordertypeString = "Mobile Shopping";
					orderaddProductPage = "addMobileOrderProducts.jsp";

				} else if (RFLAG.equals("7")) {
					ordertypeString = "Mobile Enquiry";
					orderaddProductPage = "addMobileEnquiryProducts.jsp";
				}

				else if (RFLAG.equals("8")) {
					System.out.print("RFLAG ENTERED##############################" + RFLAG);
					ordertypeString = "Mobile Enquiry";
					orderaddProductPage = "addMobileEnquiryProducts.jsp";
				} else if (RFLAG.equals("9")) {
					ordertypeString = "Mobile Registration";
					orderaddProductPage = "addMobileRegisterProducts.jsp";
				}

				else if (RFLAG.equals("10")) {
					System.out.print("RFLAG ENTERED##############################" + RFLAG);
					ordertypeString = "Mobile Registration";
					orderaddProductPage = "addMobileRegisterProducts.jsp";
				}
				ItemMstUtil itemMstUtil = new ItemMstUtil();
				itemMstUtil.setmLogger(mLogger);
				String temItem = itemMstUtil.isValidAlternateItemInItemmst(plant, item);
				if (temItem != "") {
					item = temItem;
				} else {
					throw new Exception("Product not found!");
				}

				/// Get Non Stock Type
				Map mPrddet = new ItemMstDAO().getProductNonStockDetails(plant, item);
				String nonstocktype = StrUtils.fString((String) mPrddet.get("NONSTKFLAG"));
				String nonstocktypeDesc = StrUtils.fString((String) mPrddet.get("STOCKTYPEDESC"));
				itemDesc = StrUtils.fString((String) mPrddet.get("ITEMDESC"));

				Hashtable htPodet = new Hashtable();
//				Hashtable htShiphis = new Hashtable();
				htPodet.put(IDBConstants.PLANT, plant);
				htPodet.put(IDBConstants.DODET_DONUM, dono);
				htPodet.put(IDBConstants.DODET_ITEM, item);
				htPodet.put(IDBConstants.UNITMO,uom);
				htPodet.put("ITEMDESC", StrUtils.InsertQuotes(itemDesc));
		//		htPodet.put("UNITMO", StrUtils.fString((String) mPrddet.get("STKUOM")));
				htPodet.put(IDBConstants.DODET_ITEM_DESC, StrUtils.InsertQuotes(itemDesc));
				htPodet.put(IDBConstants.DODET_JOB_NUM, jobNum);
				htPodet.put(IDBConstants.DODET_CUST_NAME, StrUtils.InsertQuotes(custName));
				htPodet.put(IDBConstants.DODET_QTYOR, qty);
				htPodet.put(IDBConstants.DODET_UNITPRICE, price);
				htPodet.put(IDBConstants.DODET_QTYIS, "0");
				htPodet.put(IDBConstants.DODET_DOLNNO, dolno);
				String CURRENCYUSEQT = new DOUtil().getCurrencyUseQT(plant, dono);
				htPodet.put(IDBConstants.CURRENCYUSEQT, CURRENCYUSEQT);

				if (nonstocktype.equals("Y")) {

					if (nonstocktypeDesc.equalsIgnoreCase("discount")
							|| nonstocktypeDesc.equalsIgnoreCase("Credit/Refund")) {
						htPodet.put(IDBConstants.DODET_UNITPRICE, "-" + price);
					}

				}
				htPodet.put(IDBConstants.DODET_PRODUCTDELIVERYDATE, PRODUCTDELIVERYDATE);
				htPodet.put(IDBConstants.DODET_LNSTATUS, "N");
				htPodet.put(IDBConstants.DODET_PICKSTATUS, "N");

//				java.util.Date dt = new java.util.Date();
//				SimpleDateFormat dfVisualDate = new SimpleDateFormat("dd/MM/yyyy");
//				String today = dfVisualDate.format(dt);
				htPodet.put(IDBConstants.TRAN_DATE, DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));
				/* to get product gst */
				taxby = _PlantMstDAO.getTaxBy(plant);
				if (taxby.equalsIgnoreCase("BYPRODUCT")) {
					prodgst = _ItemMstDAO.getProductGst(plant, item);
					htPodet.put(IDBConstants.PRODGST, prodgst);
				}
				/* to get product gst end */
				dOUtil.setmLogger(mLogger);
				boolean flag = dOUtil.saveDoDetDetails(htPodet);

				// insert dodet multi remarks
				if (flag) {
					//	Call Accounting module for add item : Start
					/*if ("yes".equals(request.getSession(false).getAttribute("isAccountingEnabled"))) {
						try {
								Form form = new Form();
								form.param("data", "{\"dono\":\"" + dono + "\",\"customer\":\"" + custCode + "\",\"transactionType\":\"3\",\"plant\":\"" + plant + "\",\"user\":\"" + userName + "\",\"action\":\"addproduct\"}");
								String finTransactionResponse = HttpUtils.connect(HttpUtils.getHostInfo(request) + IConstants.ACCOUNTING_APPLICATION_URL.replace("trackaccounting", plant) + "restFinTransaction/create", form);
								if (finTransactionResponse == null || finTransactionResponse.startsWith("Error : ")) {
									throw new Exception(finTransactionResponse);
								}
						}catch(Exception e) {
							throw new Exception(ThrowableUtil.getMessage(e), e);
						}
					}*/
					//	Call Accounting module for add item : End
					String DYNAMIC_REMARKS_SIZE = StrUtils.fString(request.getParameter("DYNAMIC_REMARKS_SIZE"));
					int remarks_count = Integer.parseInt(DYNAMIC_REMARKS_SIZE);
					String productDORemarks = "";
					for (int index = 0; index < remarks_count; index++) {
						productDORemarks = StrUtils.fString(request.getParameter("PRDREMARKS" + "_" + index));
						Hashtable<String, String> htRemarks = new Hashtable<>();
						htRemarks.put(IDBConstants.PLANT, plant);
						htRemarks.put(IDBConstants.DODET_DONUM, dono);
						htRemarks.put(IDBConstants.DODET_DOLNNO, dolno);
						htRemarks.put(IDBConstants.DODET_ITEM, item);
						htRemarks.put(IDBConstants.REMARKS, StrUtils.InsertQuotes(productDORemarks));
						htRemarks.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
						htRemarks.put(IDBConstants.CREATED_BY, userName);
						flag = dOUtil.saveDoMultiRemarks(htRemarks);
						Hashtable htMaster = new Hashtable();
						if (flag) {
							if (productDORemarks.length() > 0) {
								htMaster.clear();
								htMaster.put(IDBConstants.PLANT, plant);
								htMaster.put(IDBConstants.REMARKS, StrUtils.InsertQuotes(productDORemarks));
								
								if (!_MasterDAO.isExisitRemarks(htMaster, "")) {
									htMaster.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
								htMaster.put(IDBConstants.CREATED_BY, user_id);
									_MasterDAO.InsertRemarks(htMaster);
									Hashtable htRecvHis = new Hashtable();
									htRecvHis.put(IDBConstants.PLANT, plant);
									htRecvHis.put("DIRTYPE", "CREATE_REMARKS");
									htRecvHis.put("ORDNUM", "");
									htRecvHis.put(IDBConstants.ITEM, "");
									htRecvHis.put("BATNO", "");
									htRecvHis.put(IDBConstants.LOC, "");
									htRecvHis.put("REMARKS", StrUtils.InsertQuotes(productDORemarks));
									htRecvHis.put(IDBConstants.CREATED_BY, user_id);
									htRecvHis.put(IDBConstants.TRAN_DATE,
											DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));
									htRecvHis.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
									flag = movHisDao.insertIntoMovHis(htRecvHis);
								}
							}
						}

					}
				}
				//// insert dodet multi remarks end

				if (flag) {

					// To check DOHDR status and update
					Boolean hdrFlag = false;
//					DoDetDAO _DoDetDAO = new DoDetDAO();
					DoHdrDAO _DoHdrDAO = new DoHdrDAO();
					String updateDoHdr = "";
					Hashtable htConditoHdr = new Hashtable();
					htConditoHdr.put("PLANT", plant);
					htConditoHdr.put("dono", dono);
					hdrFlag = _DoHdrDAO.isExisit(htConditoHdr,
							" isnull(pickstaus,'') in ('C') and isnull(STATUS,'') in ('C')");
					if (hdrFlag) {
						updateDoHdr = "set  STATUS='O',PickStaus='O' ";
						flag = _DoHdrDAO.update(updateDoHdr, htConditoHdr, "");
					}
					// To check DOHDR status and update end

					dotransferdetflag = _DOTransferUtil.saveDoTransferDetDetails(htPodet);

					Hashtable htMovhis = new Hashtable();
					htMovhis.clear();
					htMovhis.put(IDBConstants.PLANT, plant);
					htMovhis.put("DIRTYPE", TransactionConstants.OB_ADD_ITEM);
					htMovhis.put(IDBConstants.CUSTOMER_CODE, custCode);
					htMovhis.put(IDBConstants.POHDR_JOB_NUM, jobNum);
					htMovhis.put(IDBConstants.ITEM, item);
					htMovhis.put(IDBConstants.QTY, qty);
					htMovhis.put(IDBConstants.MOVHIS_ORDNUM, dono);
					htMovhis.put(IDBConstants.CREATED_BY, user_id);
					htMovhis.put("MOVTID", "");
					htMovhis.put("RECID", "");
					htMovhis.put(IDBConstants.TRAN_DATE, DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));
					htMovhis.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
					if (dotransferdetflag)
						flag = movHisDao.insertIntoMovHis(htMovhis);

				}
				if (flag)
					result = "<font color=\"green\">Product  Added Successfully</font><br><br><center>"
							+ "<input type=\"button\" value=\" OK \" onClick=\"window.location.href='/track/deleveryorderservlet?DONO="
							+ dono + "&Submit=View'\">";
				else
					result = "<font color=\"red\"> Error in adding Product   <br><br><center>"
							+ "<input type=\"button\" value=\"Back\" onClick=\"window.location.href='javascript:history.back()'\"> ";
				result = "" + ordertypeString + "Order <br><h3>" + result;
				response.sendRedirect("jsp/" + orderaddProductPage + "?Submit=add&DONO=" + dono + "&JOB_NUM=" + StrUtils.replaceCharacters2Send(jobNum)
						+ "&CUST_CODE=" + custCode + "&RFLAG=" + RFLAG + "&CUST_NAME="
						+ StrUtils.replaceCharacters2Send(custName));

			}
			// ---Added by Bruhan on May 21 2014, Description:To open Sales summary in
			// excel powershell format
			else if (action.equals("ExportExcelOutboundOrderSummary")) {

//				String newHtml = "";
				HSSFWorkbook wb = new HSSFWorkbook();
//				String PLANT = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
				wb = this.writeToExcelOutboundOrderSummary(request, response, wb);
				String type = StrUtils.fString(request.getParameter("DIRTYPE"));				
				ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
				wb.write(outByteStream);
				byte[] outArray = outByteStream.toByteArray();
				response.setContentType("application/ms-excel");
				response.setContentLength(outArray.length);
				response.setHeader("Expires:", "0"); // eliminates browser caching
				
				if(type.equalsIgnoreCase("OB_SUMMARY_ISSUE")) {
					response.setHeader("Content-Disposition", "attachment; filename=SalesOrderSummaryDetails.xls");
				}else if(type.equalsIgnoreCase("OB_SUMMARY_ISS_WITH_PRICE")) {
					response.setHeader("Content-Disposition", "attachment; filename=SalesOrderSummaryByPrice.xls");
				}
				else
					response.setHeader("Content-Disposition", "attachment; filename=SalesOrderSummary.xls");
				OutputStream outStream = response.getOutputStream();
				outStream.write(outArray);
				outStream.flush();
				outStream.close();

			}
			else if (action.equals("ExportExcelVoidSummary")) {
				
				HSSFWorkbook wb = new HSSFWorkbook();
				wb = this.writeToExcelVoidSummary(request, response, wb);
				ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
				wb.write(outByteStream);
				byte[] outArray = outByteStream.toByteArray();
				response.setContentType("application/ms-excel");
				response.setContentLength(outArray.length);
				response.setHeader("Expires:", "0"); // eliminates browser caching
				
				response.setHeader("Content-Disposition", "attachment; filename=VoidSalesSummary.xls");
				OutputStream outStream = response.getOutputStream();
				outStream.write(outArray);
				outStream.flush();
				outStream.close();
				
			}
			else if (action.equals("ExportExcelPOSreturnSummary")) {
				
				HSSFWorkbook wb = new HSSFWorkbook();
				wb = this.writeToExcelVoidSummary(request, response, wb);
				ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
				wb.write(outByteStream);
				byte[] outArray = outByteStream.toByteArray();
				response.setContentType("application/ms-excel");
				response.setContentLength(outArray.length);
				response.setHeader("Expires:", "0"); // eliminates browser caching
				
				response.setHeader("Content-Disposition", "attachment; filename=POSReturnSummary.xls");
				OutputStream outStream = response.getOutputStream();
				outStream.write(outArray);
				outStream.flush();
				outStream.close();
				
			}
			else if (action.equals("ExportExcelDiscountSummary")) {
				
				HSSFWorkbook wb = new HSSFWorkbook();
				wb = this.writeToExcelPOSSummary(request, response, wb);
				ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
				wb.write(outByteStream);
				byte[] outArray = outByteStream.toByteArray();
				response.setContentType("application/ms-excel");
				response.setContentLength(outArray.length);
				response.setHeader("Expires:", "0"); // eliminates browser caching
				
				response.setHeader("Content-Disposition", "attachment; filename=POSDiscountSummary.xls");
				OutputStream outStream = response.getOutputStream();
				outStream.write(outArray);
				outStream.flush();
				outStream.close();
				
			}
			else if (action.equals("ExportExcelFOCSummary")) {
				
				HSSFWorkbook wb = new HSSFWorkbook();
				wb = this.writeToExcelPOSFOCSummary(request, response, wb);
				ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
				wb.write(outByteStream);
				byte[] outArray = outByteStream.toByteArray();
				response.setContentType("application/ms-excel");
				response.setContentLength(outArray.length);
				response.setHeader("Expires:", "0"); // eliminates browser caching
				
				response.setHeader("Content-Disposition", "attachment; filename=POSFOCSummary.xls");
				OutputStream outStream = response.getOutputStream();
				outStream.write(outArray);
				outStream.flush();
				outStream.close();
				
			}
			else if (action.equals("ExportExcelPOSExpensesSummary")) {
				
				HSSFWorkbook wb = new HSSFWorkbook();
				wb = this.writeToExcelPOSExpensesSummary(request, response, wb);
				ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
				wb.write(outByteStream);
				byte[] outArray = outByteStream.toByteArray();
				response.setContentType("application/ms-excel");
				response.setContentLength(outArray.length);
				response.setHeader("Expires:", "0"); // eliminates browser caching
				
				response.setHeader("Content-Disposition", "attachment; filename=POSExpensesSummary.xls");
				OutputStream outStream = response.getOutputStream();
				outStream.write(outArray);
				outStream.flush();
				outStream.close();
				
			}
			// ---End Added by Bruhan on May 21 2014, Description:To open Sales summary in
			// excel powershell format
			
      //   resvi starts
			
			else if (action.equals("ExportExcelConsignmentOrderSummaryIssue")) {

//				String newHtml = "";
				HSSFWorkbook wb = new HSSFWorkbook();
//				String PLANT = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
				wb = this.writeToExcelConsignmentOrderSummaryIssue(request, response, wb);
//				String type = StrUtils.fString(request.getParameter("DIRTYPE"));				
				ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
				wb.write(outByteStream);
				byte[] outArray = outByteStream.toByteArray();
				response.setContentType("application/ms-excel");
				response.setContentLength(outArray.length);
				response.setHeader("Expires:", "0"); // eliminates browser caching
				
				response.setHeader("Content-Disposition", "attachment; filename=ConsignmentOrderSummaryByPrice.xls");
				OutputStream outStream = response.getOutputStream();
				outStream.write(outArray);
				outStream.flush();
				outStream.close();

			}
       //			ends 
			
			else if (action.equals("ExportExcelOutboundOrderSummaryWithRemarks")) {

//				String newHtml = "";
				HSSFWorkbook wb = new HSSFWorkbook();
//				String PLANT = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
				wb = this.writeToExcelOutboundOrderSummaryWithRemarks(request, response, wb);
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
			
			//created by navas
			else if (action.equals("ExportExcelConsignmentOrderSummary")) {

//				String newHtml = "";
				HSSFWorkbook wb = new HSSFWorkbook();
//				String PLANT = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
				wb = this.writeToExcelConsignmentOrderSummary(request, response, wb);			
				ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
				wb.write(outByteStream);
				byte[] outArray = outByteStream.toByteArray();
				response.setContentType("application/ms-excel");
				response.setContentLength(outArray.length);
				response.setHeader("Expires:", "0"); // eliminates browser caching
				
				
					response.setHeader("Content-Disposition", "attachment; filename=ConsignmentSummary.xls");
				OutputStream outStream = response.getOutputStream();
				outStream.write(outArray);
				outStream.flush();
				outStream.close();

			}
//end by navas

			// start code by Bruhan to add price editable on edit Sales order on
			// 26/02/2013
			else if (action.equalsIgnoreCase("Updatedodet")) {
				try {
					String dono = "", dolno = "", item = "", itemDesc = "", fieldDesc = "";//result = "", 
					dono = StrUtils.fString(request.getParameter("DONO")).trim();
					dolno = StrUtils.fString(request.getParameter("DOLNNO")).trim();
					item = StrUtils.fString(request.getParameter("ITEM")).trim();
					itemDesc = StrUtils.fString(request.getParameter("DESC")).trim();
					String qty = StrUtils.removeFormat(StrUtils.fString(request.getParameter("QTY")).trim());
					String price = StrUtils.removeFormat(StrUtils.fString(request.getParameter("UNITPRICE")).trim());
					String UNITPRICERD= StrUtils.fString(request.getParameter("UNITPRICERD")).trim();
					String PRODUCTDELIVERYDATE= StrUtils.fString(request.getParameter("PRODUCTDELIVERYDATE")).trim();
					if(price.equalsIgnoreCase(""))
						price="0";
					if(UNITPRICERD.equalsIgnoreCase("0"))						
						UNITPRICERD=price;
					else if(UNITPRICERD.equalsIgnoreCase(""))
						UNITPRICERD=price;
					price = new DOUtil().getConvertedUnitCostToLocalCurrency(plant, dono, UNITPRICERD);					
					//	Begin : Ravindra : Check price and quantity variation and update DO Header status
					Hashtable htUpdatedodet = new Hashtable();
					htUpdatedodet.clear();
					htUpdatedodet.put(IDBConstants.PLANT, plant);
					htUpdatedodet.put(IDBConstants.DODET_DONUM, dono);
					htUpdatedodet.put("DOLNNO", dolno);
					String q = "isnull(unitprice, 0) as price, isnull(qtyor,0)as qty";
					ArrayList aldet = _DoDetDAO.selectDoDet(q, htUpdatedodet, " plant <> ''");
					Map m = (Map) aldet.get(0);
					String qtyOld = (String) m.get("qty");
					String priceOld = (String) m.get("price");
					if (!qtyOld.equals(qty) || !priceOld.equals(price)) {
						String hdrUpdate = " SET STATUS_ID = 'PARTIALLY PAID' ";
						Hashtable htCondition = new Hashtable();
						htCondition.put("PLANT", plant);
						htCondition.put("DONO", dono);
						htCondition.put("STATUS_ID", "PAID");//	Only paid orders status has to be changed
						try {
							new DoHdrDAO().update(hdrUpdate, htCondition, "");
						}catch(Exception e) {
							if (e == null || !"Unable to update!".equals(ThrowableUtil.getMessage(e))) {
								throw new Exception("While updating stauts", e);
							}
						}
					}
					//	End : Ravindra : Check price and quantity variation and update DO Header status
					htUpdatedodet = new Hashtable();
					htUpdatedodet.clear();
					htUpdatedodet.put(IDBConstants.PLANT, plant);
					htUpdatedodet.put(IDBConstants.DODET_DONUM, dono);
					htUpdatedodet.put("DOLNNO", dolno);
					htUpdatedodet.put(IDBConstants.ITEM, item);
					htUpdatedodet.put(IDBConstants.ITEM_DESC, StrUtils.InsertQuotes(itemDesc));
					// htUpdatedodet.put(IDBConstants.DODET_COMMENT1,StrUtils.InsertQuotes(productRemarks));
					// to check item as nonstock item and non stock type in 2&4 then check price has
					// minus otherwise prefix minus with price
					ItemMstDAO _ItemMstDao = new ItemMstDAO();
					Hashtable htIsExist = new Hashtable();
					htIsExist.put(IDBConstants.PLANT, plant);
					htIsExist.put(IDBConstants.ITEM, item);
					htIsExist.put("NONSTKFLAG", "Y");
					boolean isExistNonStock = _ItemMstDao.isExisit(htIsExist, "NONSTKTYPEID IN(2,4)");
					if (isExistNonStock) {
						String s = price;
						if (s.indexOf("-") == -1) {
							price = "-" + price;
						}
					}
					// end to check item as nonstock item and non stock type as 2 then check cost
					// has minus sympol otherwise prefix minus with cost
					htUpdatedodet.put("UNITPRICE", price);
					htUpdatedodet.put("ORDQTY", qty);
					htUpdatedodet.put("PRODUCTDELIVERYDATE", PRODUCTDELIVERYDATE);

					String estno = "", estlnno = "", qtyor = "", queryest = "", updateEstHdr = "",
							updatestatus = "";
					q = "";
//					boolean estflag;
					Hashtable htUpdateestdet = new Hashtable();
					htUpdateestdet.clear();
					htUpdateestdet.put(IDBConstants.PLANT, plant);
					htUpdateestdet.put(IDBConstants.DODET_DONUM, dono);
					htUpdateestdet.put("DOLNNO", dolno);
					htUpdateestdet.put(IDBConstants.ITEM, item);

					q = " isnull(estno,'') estno,isnull(estlnno,'') as estlnno,isnull(qtyor,0)as qty ";
					aldet = _DoDetDAO.selectDoDet(q, htUpdateestdet, " plant <> ''");
					for (int i = 0; i < aldet.size(); i++) {
						m = (Map) aldet.get(i);
						estno = (String) m.get("estno");
						estlnno = (String) m.get("estlnno");
						qtyor = (String) m.get("qty");

					}

					if (estno.length() > 0) {
						EstDetDAO _EstDetDAO = new EstDetDAO();
						EstHdrDAO _EstHdrDAO = new EstHdrDAO();
						Hashtable htestCond = new Hashtable();
						htestCond.put("PLANT", plant);
						htestCond.put("estno", estno);
						htestCond.put("estlnno", estlnno);
						htestCond.put("item", item);

						if (Double.parseDouble(qtyor) > Double.parseDouble(qty)) {
							double qtyis = Double.parseDouble(qtyor) - Double.parseDouble(qty);
							queryest = "set qtyis= isNull(qtyis,0) - " + qtyis;
//							estflag = 
									_EstDetDAO.update(queryest, htestCond, "");

							updatestatus = "set STATUS=CASE WHEN qtyis > 0 THEN 'O' ELSE 'N' END";
//							estflag = 
									_EstDetDAO.update(updatestatus, htestCond, "");

							updateEstHdr = "set  status='Pending' ";
							htestCond.remove("estlnno");
							htestCond.remove("item");
//							estflag = 
									_EstHdrDAO.update(updateEstHdr, htestCond, "");
						}
					}
					boolean flag = dOUtil.updateDoDetDetails(htUpdatedodet);

					// delete & insert dodet multi remarks
					if (flag) {
						//	Call Accounting module for update item : Start
						/*if ("yes".equals(request.getSession(false).getAttribute("isAccountingEnabled"))) {
							try {
									Form form = new Form();
									Hashtable htCond = new Hashtable();
									htCond.put("PLANT", plant);
									htCond.put("DONO", dono);
									String query = "dono,custCode";
									ArrayList al = dOUtil.getDoHdrDetails(query, htCond);
									m = (Map) al.get(0);
									String custCode = (String) m.get("custCode");
									form.param("data", "{\"dono\":\"" + dono + "\",\"customer\":\"" + custCode + "\",\"transactionType\":\"3\",\"plant\":\"" + plant + "\",\"user\":\"" + userName + "\",\"action\":\"updateproduct\"}");							
									String finTransactionResponse = HttpUtils.connect(HttpUtils.getHostInfo(request) + IConstants.ACCOUNTING_APPLICATION_URL.replace("trackaccounting", plant) + "restFinTransaction/create", form);
									if (finTransactionResponse == null || finTransactionResponse.startsWith("Error : ")) {
										throw new Exception(finTransactionResponse);
									}
							}catch(Exception e) {
								throw new Exception(ThrowableUtil.getMessage(e), e);
							}
						}*/
						//	Call Accounting module for update item : End
						Hashtable htRemarksDel = new Hashtable();
						htRemarksDel.put(IDBConstants.PLANT, plant);
						htRemarksDel.put(IDBConstants.DODET_DONUM, dono);
						htRemarksDel.put(IDBConstants.DODET_DOLNNO, dolno);
						htRemarksDel.put(IDBConstants.DODET_ITEM, item);
						flag = _DoDetDAO.deleteDoMultiRemarks(htRemarksDel);
					}
					// delete dodet multi remarks end

					String strMovHisRemarks = "";
					if (flag) {
						String DYNAMIC_REMARKS_SIZE = StrUtils.fString(request.getParameter("DYNAMIC_REMARKS_SIZE"));
						int remarks_count = Integer.parseInt(DYNAMIC_REMARKS_SIZE);
						String productDORemarks = "";
						for (int index = 0; index < remarks_count; index++) {
							productDORemarks = StrUtils.fString(request.getParameter("PRDREMARKS" + "_" + index));
							strMovHisRemarks = strMovHisRemarks + "," + productDORemarks;
							Hashtable htRemarks = new Hashtable();
							htRemarks.put(IDBConstants.PLANT, plant);
							htRemarks.put(IDBConstants.DODET_DONUM, dono);
							htRemarks.put(IDBConstants.DODET_DOLNNO, dolno);
							htRemarks.put(IDBConstants.DODET_ITEM, item);
							htRemarks.put(IDBConstants.REMARKS, StrUtils.InsertQuotes(productDORemarks));
							htRemarks.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
							htRemarks.put(IDBConstants.CREATED_BY, userName);
							flag = dOUtil.saveDoMultiRemarks(htRemarks);
						}
					}
					String numberOfDecimal= _PlantMstDAO.getNumberOfDecimal(plant);
					float pricevalue ="".equals(price) ? 0.0f :  Float.parseFloat(price);
					price = StrUtils.addZeroes(pricevalue, numberOfDecimal);
					// delete & insert dodet multi remarks end

					MovHisDAO movhisDao = new MovHisDAO();
					Hashtable htmovHis = new Hashtable();
					movhisDao.setmLogger(mLogger);
					htmovHis.clear();
					htmovHis.put(IDBConstants.PLANT, plant);
					htmovHis.put("DIRTYPE", "SALES_ORDER_UPDATE_PRODUCT");
					htmovHis.put(IDBConstants.ITEM, item);
					htmovHis.put(IDBConstants.MOVHIS_ORDNUM, dono);
					htmovHis.put(IDBConstants.MOVHIS_ORDLNO, dolno);
					htmovHis.put("QTY", qty);
					htmovHis.put("REMARKS", price + "," + StrUtils.InsertQuotes(strMovHisRemarks));
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
						if (dono.length() > 0) {
							htCond.put("PLANT", plant);
							htCond.put("DONO", dono);

							String query = "isnull(dono,'') dono,isnull(outbound_Gst,0) outbound_Gst,isnull(ordertype,'')ordertype ,"
									+ "(select isnull(display,'') display from " + "[" + plant
									+ "_CURRENCYMST] where currencyid = a.currencyid) currencyid,"
									+ "isnull(custCode,'') custCode,isnull(jobNum,'') jobNum, isnull(ordertype,'') ordertype,"
									+ "isnull(personInCharge,'') personInCharge,isnull(contactNum,'') contactNum,isnull(address,'') address,"
									+ "isnull(address2,'') address2,isnull(address3,'') address3,isnull(collectionDate,'') collectionDate,"
									+ "isnull(collectionTime,'') collectionTime,isnull(remark1,'') remark1,isnull(remark2,'') remarks2,"
									+ "b.CNAME as custName,isnull(b.name,'') as contactname,isnull(b.telno,'') as telno,"
									+ "isnull(b.hpno,'') as hpno,isnull(b.hpno,'') as hpno,isnull(b.email,'') email,"
									+ "isnull(b.addr1,'') add1,isnull(b.addr2,'') add2,isnull(b.addr3,'') add3,"
									+ "isnull(b.remarks,'') remarks,isnull(b.addr4,'') add4,isnull(b.country,'') country,"
									+ "isnull(b.zip,'') zip,isnull(deliverydate,'') deliverydate,isnull(timeslots,'') timeslots,"
									+ "isnull(shippingid,'')as shippingid, isnull(shippingcustomer,'') as shippingcustomer,"
									+ "isnull(STATUS_ID,'') as statusid,isnull(empno,'') as empno,isnull(remark3,'') remark3,"
									+ "ISNULL(CUSTOMER_STATUS_ID,'') customerstatusid,ISNULL(CUSTOMER_TYPE_ID,'') customertypeid,"
									+ "isnull(orderdiscount,0) as orderdiscount,isnull(shippingcost,0) as shippingcost,"
									+ "isnull(incoterms,'') as incoterms,isnull(dnplremarks,'') as dnplremarks,isnull(a.DELIVERYDATEFORMAT,'') as DELIVERYDATEFORMAT,isnull(a.PAYMENTTYPE,'') payment_terms,"
									+ "ISNULL(A.SALES_LOCATION,'') SALES_LOCATION,ISNULL(A.TAXTREATMENT,'') TAXTREATMENT ";
							al = dOUtil.getOutGoingDoHdrDetails(query, htCond);
							if (al.size() > 0) {
								m = (Map) al.get(0);
								fieldDesc = dOUtil.listDODET(plant, dono, rflag);
								System.out.print(fieldDesc);
							} else {
								fieldDesc = "<tr><td colspan=\"9\" align=\"center\">No Records Available</td></tr>";

							}
						}
						request.getSession().setAttribute("podetVal", m);
						request.getSession().setAttribute("dono", dono);
						request.getSession().setAttribute("RESULT", fieldDesc);

						if (rflag.equals("1")) {
							response.sendRedirect("jsp/CreateOutgoingOrder.jsp?DONO=" + dono + "&action=View");
						} else {

							response.sendRedirect("jsp/maintOutgoingOrder.jsp?DONO=" + dono + "&action=View");
						}

					} else {
						throw new Exception("Price  Not Updated");
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

			} else if (action.equalsIgnoreCase("Copy OB")) {

				String deldate = "", jobNum = "", custName = "",
						personIncharge = ""/*, vend = "", ordertype = "", custCode = "", contactNum = ""*/;
				String remark1 = "", collectionTime = ""/*, remark2 = "", address = "", address2 = "", address3 = "", collectionDate = ""*/;
				String telno = "", email = "", currencyid = "", add1 = "", add2 = "", add3 = "",
						add4 = "", country = "", zip = "", remarks = "", gst = ""/*, contactname = ""*/;
				String empno = ""/*,sCustName = "", sContactName = "", sAddr1 = "", sAddr2 = "", sCity = "", sCountry = "",
						sZip = "", sTelno = "", paymenttype=""*/;

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
				String newdono = _TblControlDAO.getNextOrder(plant, userName, IConstants.OUTBOUND);

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
				empno = StrUtils.fString(request.getParameter("EMP_NAME"));
//				paymenttype = StrUtils.fString(request.getParameter("PAYMENTTYPE"));
				// request.getSession().setAttribute("RFLAG","5");
//				boolean flag = 
						DisplayData(request, response);

				response.sendRedirect("/track/jsp/CopyToOutbound.jsp?OLDDONO=" + dono + "&action=View" + "&DONO="
						+ newdono + "&REFNO=" + jobNum + "&ORDDATE=" + deldate + "&TIME=" + collectionTime + "&REMARK1="
						+ StrUtils.replaceCharacters2Send(remark1) + "&TAX=" + gst + "&CURRENCY=" + currencyid + "&CUSTNAME=" + custName
						+ "&PERSONINCHARGE=" + personIncharge + "&TELNO=" + telno + "&EMAIL=" + email + "&ADD1=" + add1
						+ "&ADD2=" + add2 + "&ADD3=" + add3 + "&ADD4=" + add4 + "&COUNTRY=" + country + "&ZIP=" + zip
						+ "&CUSTCODE=" + custcode + "&REMARK2=" + StrUtils.replaceCharacters2Send(remarks) + "&EMPNO=" + empno + "");

			} else if (action.equalsIgnoreCase("Convert OB")) {

				String deldate = "", jobNum = "", remark1 = "", remark2 = "", collectionTime = "", currencyid = "", gst = ""/*, vend = "", custName = "", ordertype = "", custCode = "",
						personIncharge = "", contactNum = "", address = "", address2 = "",
						address3 = "", collectionDate = "", contactname = "", telno = "",
						email = "", add1 = "", add2 = "", add3 = "", add4 = "", country = "", zip = "",
						remarks = "", empno = "", shippingcustomer = "", shippingid = "", orderdiscount = "",
						shippingcost = "", incoterms = "",sTAXTREATMENT="",sPURCHASE_LOC=""*/;

				String pono = StrUtils.fString(request.getParameter("PONO"));

				Hashtable htpodet = new Hashtable();
				htpodet.put("PLANT", plant);
				htpodet.put("PONO", pono);

				boolean validpono = _PoDetDAO.isExisit(htpodet);
				if (!validpono) {
					throw new Exception("Not a valid Order Number : " + pono + ", Please enter valid Order Number..");
				}

				com.track.dao.TblControlDAO _TblControlDAO = new com.track.dao.TblControlDAO();
				_TblControlDAO.setmLogger(mLogger);
				String newdono = _TblControlDAO.getNextOrder(plant, userName, IConstants.OUTBOUND);

//				String custcode = StrUtils.fString(request.getParameter("CUST_CODE1"));
				jobNum = StrUtils.fString(request.getParameter("JOB_NUM"));
				deldate = StrUtils.fString(request.getParameter("DELDATE"));
				collectionTime = StrUtils.fString(request.getParameter("COLLECTION_TIME"));
				remark1 = StrUtils.fString(request.getParameter("REMARK1"));
				remark2 = StrUtils.fString(request.getParameter("REMARK3"));
				gst = StrUtils.fString(request.getParameter("GST"));
				currencyid = StrUtils.fString(request.getParameter("DISPLAY"));

				ArrayList al = new ArrayList();
				Hashtable htCond = new Hashtable();
				Map m = new HashMap();
				POUtil _poUtil = new POUtil();
				if (pono.length() > 0) {
					htCond.put("PLANT", plant);
					htCond.put("PONO", pono);
					String query = " a.pono,isnull(a.ordertype,'') ordertype,"
							+ "(select isnull(display,'') display from " + "[" + plant
							+ "_CURRENCYMST] where currencyid = a.currencyid) currencyid,"
							+ "a.inbound_Gst,a.custName,a.custCode,isnull(a.jobNum,'') as jobNum,a.collectionDate,"
							+ "a.collectionTime,isnull(a.remark1,'') as remark1,isnull(a.remark3,'') as remark3,"
							+ "isnull(a.deldate,'') as deldate,isnull(STATUS_ID,'') as statusid,"
							+ "isnull(orderdiscount,0) as orderdiscount,isnull(shippingcost,0) as shippingcost,"
							+ "isnull(incoterms,'') as incoterms,isnull(pay_terms,'') payment_terms,isnull(DELIVERYDATEFORMAT,'') DELIVERYDATEFORMAT,"
							+ "ISNULL(A.PURCHASE_LOCATION,'') PURCHASE_LOCATION,ISNULL(A.TAXTREATMENT,'') TAXTREATMENT";
					// String query = " a.pono,isnull(a.ordertype,'') ordertype,(select
					// isnull(display,'') display from "+"["+plant+"_CURRENCYMST] where currencyid =
					// a.currencyid)
					// currencyid,a.inbound_Gst,a.custName,a.custCode,isnull(a.jobNum,'') as
					// jobNum,a.collectionDate,a.collectionTime,isnull(a.remark1,'') as
					// remark1,isnull(a.remark3,'') as remark3,isnull(a.deldate,'') as
					// deldate,isnull(STATUS_ID,'') as statusid";
					al = _poUtil.getSupplierHdrDetails(query, htCond, "");
					if (al.size() > 0) {
						m = (Map) al.get(0);

					}

				}
				request.getSession().setAttribute("podetVal", m);

				response.sendRedirect("/track/jsp/ConvertInboundToOutbound.jsp?PONO=" + pono + "&action=View" + "&DONO="
						+ newdono + "&REFNO=" + jobNum + "&ORDDATE=" + deldate + "&TIME=" + collectionTime + "&REMARK1="
						+ StrUtils.replaceCharacters2Send(remark1) + "&TAX=" + gst + "&CURRENCY=" + currencyid + "&REMARK2=" + StrUtils.replaceCharacters2Send(remark2) + "");

			} else if (action.equalsIgnoreCase("copy To Outbound Order")) {

				try {
					String olddono = "", olddolno = "", dono = "", dolno = "", item = "", itemDesc = "", result = "",
							copytype = "", q = "", prdRemarks = "", itemuom = "", price = "",
									custCode = "", custName = "", jobNum = "", personIncharge = "",
							contactNum = "", ordertype = "", orderstatus = "", deliveryDate = "", address = "",
							address2 = "", address3 = "", collectionDate = "", collectionTime = "", remark1 = "",
							remark2 = "", currencyid = "", outbound_Gst = "", empno = "", issuingQty = "",
							shippingid = "", shippingcustomer = "", orderdiscount = "", shippingcost = "",
							incoterms = "", taxby = "", prodgst = "",paymenttype="",deliverydateformat="",PRODUCTDELIVERYDATE="",sTAXTREATMENT="",sPURCHASE_LOC="";//fieldDesc = "", qtyor = "", issuedqty = "", 
					MovHisDAO movHisDao = new MovHisDAO();
//					Boolean allChecked = false, fullIssue = false;
					Map checkedDO = new HashMap();
					boolean flag = false;
//					boolean dotransferhdr = false;
					boolean dotransferdetflag = false;
					HttpSession session = request.getSession();
					ArrayList aldet = new ArrayList();

					copytype = StrUtils.fString(request.getParameter("copytype")).trim();
					if (copytype.equalsIgnoreCase("OBTOOB")) {
						olddono = StrUtils.fString(request.getParameter("OLDDONO")).trim();
					} else {
						olddono = StrUtils.fString(request.getParameter("PONO")).trim();
					}

					if (olddono.length() > 0) {

						dono = StrUtils.fString(request.getParameter("DONO"));
						String[] chkdDONo = request.getParameterValues("chkdDONO");

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
								.parseDate(StrUtils.fString(request.getParameter("DELDATE")).trim());
						deliveryDate = StrUtils.fString(request.getParameter("DELIVERYDATE")).trim();
						collectionTime = StrUtils.fString(request.getParameter("COLLECTION_TIME")).trim();
						remark1 = StrUtils.fString(request.getParameter("REMARK1")).trim();
						remark2 = StrUtils.fString(request.getParameter("REMARK3")).trim();
						outbound_Gst = StrUtils.fString(request.getParameter("GST"));
						currencyid = StrUtils.fString(request.getParameter("DISPLAY"));
						ordertype = StrUtils.fString(request.getParameter("ORDERTYPE"));
						orderstatus = StrUtils.fString(request.getParameter("STATUS_ID"));
						empno = StrUtils.fString(request.getParameter("EMP_NAME"));
						orderstatus = StrUtils.fString(request.getParameter("STATUS_ID"));
						orderdiscount = StrUtils.fString(request.getParameter("ORDERDISCOUNT")).trim();
						shippingcost = StrUtils.fString(request.getParameter("SHIPPINGCOST")).trim();
						incoterms = StrUtils.fString(request.getParameter("INCOTERMS")).trim();
						shippingcustomer = StrUtils.fString(request.getParameter("SHIPPINGCUSTOMER")).trim();
						shippingid = StrUtils.fString(request.getParameter("SHIPPINGID")).trim();
						paymenttype = StrUtils.fString(request.getParameter("PAYMENTTYPE")).trim();
						deliverydateformat = StrUtils.fString((request.getParameter("DELIVERYDATEFORMAT") != null) ? "1": "0").trim();
						sTAXTREATMENT=StrUtils.fString(request.getParameter("TAXTREATMENT")).trim();
						sPURCHASE_LOC=StrUtils.fString(request.getParameter("PURCHASE_LOC")).trim();
						Hashtable ht = new Hashtable();
						ht.clear();

						ht.put(IDBConstants.PLANT, plant);
						ht.put(IDBConstants.DOHDR_DONUM, dono);
						ht.put(IDBConstants.DOHDR_CUST_CODE, custCode);
						ht.put(IDBConstants.DOHDR_CUST_NAME, StrUtils.InsertQuotes(custName));
						ht.put(IDBConstants.DOHDR_JOB_NUM, jobNum);
						ht.put(IDBConstants.DOHDR_PERSON_INCHARGE, StrUtils.InsertQuotes(personIncharge));
						ht.put(IDBConstants.DOHDR_CONTACT_NUM, contactNum);
						ht.put(IDBConstants.DOHDR_ADDRESS, address);
						ht.put(IDBConstants.DOHDR_ADDRESS2, address2);
						ht.put(IDBConstants.DOHDR_ADDRESS3, address3);
						ht.put(IDBConstants.CREATED_BY, userName);
						ht.put(IDBConstants.DOHDR_COL_DATE, collectionDate);
						ht.put(IDBConstants.DOHDR_COL_TIME, collectionTime);
						ht.put(IDBConstants.DOHDR_REMARK1, StrUtils.InsertQuotes(remark1));
						ht.put(IDBConstants.DOHDR_REMARK3, StrUtils.InsertQuotes(remark2));
						ht.put(IDBConstants.STATUS, "N");
						ht.put(IDBConstants.ORDERTYPE, ordertype);
						ht.put(IDBConstants.CURRENCYID, currencyid);
						ht.put(IDBConstants.TIMESLOTS, "");
						ht.put(IDBConstants.DELIVERYDATE, deliveryDate);
						ht.put(IDBConstants.DOHDR_GST, outbound_Gst);
						ht.put(IDBConstants.ORDSTATUSID, orderstatus);
						ht.put(IDBConstants.DOHDR_EMPNO, empno);
						ht.put(IDBConstants.SHIPPINGCUSTOMER, shippingcustomer);
						ht.put(IDBConstants.SHIPPINGID, shippingid);
						ht.put(IDBConstants.ORDERDISCOUNT, orderdiscount);
						ht.put(IDBConstants.SHIPPINGCOST, shippingcost);
						ht.put(IDBConstants.INCOTERMS, incoterms);
						ht.put(IDBConstants.PAYMENTTYPE, paymenttype);
						ht.put(IDBConstants.POHDR_DELIVERYDATEFORMAT, deliverydateformat);
						ht.put(IDBConstants.TAXTREATMENT, sTAXTREATMENT);
						ht.put(IDBConstants.SALES_LOCATION, sPURCHASE_LOC);
						Boolean isCustomerAvailable = Boolean.valueOf(true);
						CustUtil custUtils = new CustUtil();
						isCustomerAvailable = custUtils.isExistCustomerName(StrUtils.InsertQuotes(custName), plant);
						if (isCustomerAvailable) {

						dOUtil.setmLogger(mLogger);
						flag = dOUtil.saveDoHdrDetails(ht);
						ht.remove(IDBConstants.ORDERTYPE);
						ht.remove(IDBConstants.ORDSTATUSID);

						if (flag) {
//							dotransferhdr = 
									_DOTransferUtil.saveDoTransferHdrDetails(ht);

							Hashtable htmovHis = new Hashtable();
							htmovHis.clear();
							htmovHis.put(IDBConstants.PLANT, plant);
							htmovHis.put("DIRTYPE", TransactionConstants.CONVERT_OUTBOUND);
							htmovHis.put(IDBConstants.CUSTOMER_CODE, custCode);
							htmovHis.put(IDBConstants.POHDR_JOB_NUM, jobNum);
							htmovHis.put(IDBConstants.MOVHIS_ORDNUM, olddono);
							htmovHis.put(IDBConstants.CREATED_BY, userName);
							htmovHis.put("MOVTID", "");
							htmovHis.put("RECID", "");
							if (!remark1.equals("")) {
								htmovHis.put(IDBConstants.REMARKS, StrUtils.InsertQuotes(custName) + "," + dono + ","
										+ StrUtils.InsertQuotes(remark1));
							} else {
								htmovHis.put(IDBConstants.REMARKS, StrUtils.InsertQuotes(custName) + "," + dono);
							}

							htmovHis.put(IDBConstants.TRAN_DATE, DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));
							htmovHis.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());

							flag = movHisDao.insertIntoMovHis(htmovHis);

							Hashtable htRecvHis = new Hashtable();
							htRecvHis.clear();
							htRecvHis.put(IDBConstants.PLANT, plant);
							htRecvHis.put("DIRTYPE", TransactionConstants.CREATE_OB);
							htRecvHis.put(IDBConstants.CUSTOMER_CODE, custCode);
							htRecvHis.put(IDBConstants.POHDR_JOB_NUM, jobNum);
							htRecvHis.put(IDBConstants.MOVHIS_ORDNUM, dono);
							htRecvHis.put(IDBConstants.CREATED_BY, userName);
							htRecvHis.put("MOVTID", "");
							htRecvHis.put("RECID", "");
							if (!remark1.equals("")) {
								htRecvHis.put(IDBConstants.REMARKS,
										StrUtils.InsertQuotes(custName) + "," + StrUtils.InsertQuotes(remark1));
							} else {
								htRecvHis.put(IDBConstants.REMARKS, StrUtils.InsertQuotes(custName));
							}

							htRecvHis.put(IDBConstants.TRAN_DATE, DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));
							htRecvHis.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());

							flag = movHisDao.insertIntoMovHis(htRecvHis);

							new TblControlUtil().updateTblControlSeqNo(plant, IConstants.OUTBOUND, "S", dono);

						}

//						if (request.getParameter("select") != null) {
//							allChecked = true;
//						}
//						if (request.getParameter("fullIssue") != null) {
//							fullIssue = true;
//						}
						if (chkdDONo != null) {
							for (int i = 0; i < chkdDONo.length; i++) {
								olddolno = chkdDONo[i];
								issuingQty = StrUtils.fString(request.getParameter("issuingQty_" + dolno));
								checkedDO.put(olddolno, issuingQty);
							}
							session.setAttribute("checkedDONO", checkedDO);
						}

						process: if (chkdDONo != null) {
							for (int i = 0; i < chkdDONo.length; i++) {
								dolno = chkdDONo[i];
								issuingQty = StrUtils
										.formatNum(StrUtils.fString(request.getParameter("issuingQty_" + dolno)));

								if (copytype.equalsIgnoreCase("OBTOOB")) {
									Hashtable htCond = new Hashtable();
									htCond.put("PLANT", plant);
									htCond.put("DONO", olddono);
									htCond.put("DOLNNO", dolno);

									q = "item,isnull(itemdesc,'') itemdesc,isnull(PRODUCTDELIVERYDATE,'') PRODUCTDELIVERYDATE,isnull(unitmo,'') uom,isnull(unitprice,0) unitprice,isnull(qtyor,0) as qtyor,isnull(qtyis,0) as qtyis,isnull(comment1,'') as prdRemarks ";
									aldet = _DoDetDAO.selectDoDet(q, htCond, " plant <> '' order by dolnno");
								} else {
									Hashtable htCond = new Hashtable();
									htCond.put("PLANT", plant);
									htCond.put("PONO", olddono);
									htCond.put("POLNNO", dolno);

									q = "item,isnull(itemdesc,'') itemdesc,isnull(PRODUCTDELIVERYDATE,'') PRODUCTDELIVERYDATE,isnull(unitmo,'') uom,isnull(qtyor,0) as qtyor,isnull(comment1,'') as prdRemarks ";
									aldet = _PoDetDAO.selectPoDet(q, htCond, " plant <> '' order by polnno");

								}
								if (aldet.size() > 0) {
									Map m = (Map) aldet.get(0);
									item = (String) m.get("item");
									itemDesc = (String) m.get("itemdesc");
									prdRemarks = (String) m.get("prdRemarks");
									itemuom = (String) m.get("uom");
									PRODUCTDELIVERYDATE =  (String) m.get("PRODUCTDELIVERYDATE");
									// price = (String) m.get("unitprice");
									if (copytype.equalsIgnoreCase("OBTOOB")) {
										price = (String) m.get("unitprice");
									} else {
										//price = _ItemMstDAO.getItemPrice(plant, item);
										price = _ItemMstDAO.getLocalCurrencyWRD(plant,currencyid, item,itemuom);
									}

//									qtyor = StrUtils.formatNum((String) m.get("qtyor"));

								}
								Map mPrddet = new ItemMstDAO().getProductNonStockDetails(plant, item);
								String nonstocktype = StrUtils.fString((String) mPrddet.get("NONSTKFLAG"));
								String nonstocktypeDesc = StrUtils.fString((String) mPrddet.get("STOCKTYPEDESC"));

								Hashtable htdodet = new Hashtable();

								htdodet.put(IDBConstants.PLANT, plant);
								htdodet.put(IDBConstants.DODET_DONUM, dono);

								htdodet.put(IDBConstants.DODET_ITEM, item);
								htdodet.put("ITEMDESC", StrUtils.InsertQuotes(itemDesc));
								htdodet.put("UNITMO", itemuom);
								htdodet.put(IDBConstants.DODET_ITEM_DESC, StrUtils.InsertQuotes(itemDesc));

								htdodet.put(IDBConstants.DODET_JOB_NUM, jobNum);
								htdodet.put(IDBConstants.DODET_CUST_NAME, StrUtils.InsertQuotes(custName));
								htdodet.put(IDBConstants.DODET_QTYOR, issuingQty);
								htdodet.put(IDBConstants.DODET_UNITPRICE, price);
								htdodet.put(IDBConstants.DODET_QTYIS, "0");
								htdodet.put(IDBConstants.DODET_COMMENT1, StrUtils.InsertQuotes(prdRemarks));
								htdodet.put(IDBConstants.DODET_DOLNNO, Integer.toString(i + 1));

								String CURRENCYUSEQT = new DOUtil().getCurrencyUseQT(plant, dono);
								htdodet.put(IDBConstants.CURRENCYUSEQT, CURRENCYUSEQT);
								if (nonstocktype.equals("Y") && !(copytype.equalsIgnoreCase("OBTOOB"))) {

									if (nonstocktypeDesc.equalsIgnoreCase("discount")
											|| nonstocktypeDesc.equalsIgnoreCase("Credit/Refund")) {
										htdodet.put(IDBConstants.DODET_UNITPRICE, "-" + price);

									}

								}
								htdodet.put(IDBConstants.DODET_PRODUCTDELIVERYDATE, PRODUCTDELIVERYDATE);
								htdodet.put(IDBConstants.DODET_LNSTATUS, "N");
								htdodet.put(IDBConstants.DODET_PICKSTATUS, "N");
								/* to get product gst */
								taxby = _PlantMstDAO.getTaxBy(plant);
								if (taxby.equalsIgnoreCase("BYPRODUCT")) {
									prodgst = _ItemMstDAO.getProductGst(plant, item);
									htdodet.put(IDBConstants.PRODGST, prodgst);
								}
								/* to get product gst end */

//								java.util.Date dt = new java.util.Date();
//								SimpleDateFormat dfVisualDate = new SimpleDateFormat("dd/MM/yyyy");
//								String today = dfVisualDate.format(dt);

								htdodet.put(IDBConstants.TRAN_DATE, DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));
								dOUtil.setmLogger(mLogger);
								if (flag) {
									flag = dOUtil.saveDoDetDetails(htdodet);
								}
								if (flag) {
									//	Call Accounting module for new order : Start
									/*if ("yes".equals(request.getSession(false).getAttribute("isAccountingEnabled"))) {
										try {
											Hashtable htAcct = new Hashtable();
											htAcct.put(IDBConstants.PLANT, plant);
											if (_MasterDAO.isExisitAccount(htAcct, " ACCOUNT_TITLE='Customers'")) {
												//	Get Acct. Id
												String Acctid=getNextIdDetails(plant,userName);
												if(Acctid!="")
												{
													if (!_MasterDAO.isExisitAccount(htAcct, " CUSTOMER_NO='"+custCode+"' AND ACCOUNT_TYPE=1 AND PARENT_ACCOUNT_NO=12  ")) {
													flag = _MasterDAO.InsertAccount(htAcct,Acctid,custName,"",custCode,userName,1,12);//insert Supplier Account
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
											form.param("data", "{\"dono\":\"" + dono + "\",\"xndate\":\"" + collectionDate + "\",\"customer\":\"" + custCode + "\",\"amount\":\"0\",\"transactionType\":\"3\",\"plant\":\"" + plant + "\",\"user\":\"" + userName + "\",\"currency\":\"" + currencyid + "\",\"action\":\"copytooutbound\"}");							
											String finTransactionResponse = HttpUtils.connect(HttpUtils.getHostInfo(request) + IConstants.ACCOUNTING_APPLICATION_URL.replace("trackaccounting", plant) + "restFinTransaction/create", form);
											if (finTransactionResponse == null || finTransactionResponse.startsWith("Error : ")) {
												throw new Exception(finTransactionResponse);
											}
													}
												}
											}
										}catch(Exception e) {
											throw new Exception(ThrowableUtil.getMessage(e), e);
										}
									}*/
									//	Call Accounting module for new order : End
									// insert dodet multi remarks

									ArrayList almultiremarks = new ArrayList();
									String productDORemarks = "";
									if (copytype.equalsIgnoreCase("OBTOOB")) {
										almultiremarks = dOUtil.listDoMultiRemarks(plant, olddono, dolno);
									} else {
										almultiremarks = pOUtil.listPoMultiRemarks(plant, olddono, dolno);
									}
									for (int index = 0; index < almultiremarks.size(); index++) {

										Map remarks = (Map) almultiremarks.get(index);

										productDORemarks = (String) remarks.get("remarks");

										Hashtable htRemarks = new Hashtable();
										htRemarks.put(IDBConstants.PLANT, plant);
										htRemarks.put(IDBConstants.DODET_DONUM, dono);
										htRemarks.put(IDBConstants.DODET_DOLNNO, Integer.toString(i + 1));
										htRemarks.put(IDBConstants.DODET_ITEM, item);
										htRemarks.put(IDBConstants.REMARKS, StrUtils.InsertQuotes(productDORemarks));
										htRemarks.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
										htRemarks.put(IDBConstants.CREATED_BY, userName);
										flag = dOUtil.saveDoMultiRemarks(htRemarks);
									}
								}

								if (flag) {
									dotransferdetflag = _DOTransferUtil.saveDoTransferDetDetails(htdodet);
									Hashtable htMovhis = new Hashtable();
									htMovhis.clear();
									htMovhis.put(IDBConstants.PLANT, plant);
									htMovhis.put("DIRTYPE", TransactionConstants.OB_ADD_ITEM);
									htMovhis.put(IDBConstants.CUSTOMER_CODE, custCode);
									htMovhis.put(IDBConstants.POHDR_JOB_NUM, jobNum);
									htMovhis.put(IDBConstants.ITEM, item);
									htMovhis.put(IDBConstants.QTY, issuingQty);
									htMovhis.put(IDBConstants.MOVHIS_ORDNUM, dono);
									htMovhis.put(IDBConstants.CREATED_BY, userName);
									htMovhis.put("MOVTID", "");
									htMovhis.put("RECID", "");
									htMovhis.put(IDBConstants.TRAN_DATE,
											DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));
									htMovhis.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
									if (dotransferdetflag)
										flag = movHisDao.insertIntoMovHis(htMovhis);

								}

								if (!flag)
									break process;
							}
						}

						if (flag) {
							if (copytype.equalsIgnoreCase("OBTOOB")) {
								result = "<font color=\"green\"> Converted To Sales Order Successfully!  <br><br><center>"
										+ "<input type=\"button\" value=\"Back\" onClick=\"window.location.href='/track/deleveryorderservlet?DONO="
										+ olddono + "&Submit=View&RFLAG=4'\"> ";
							} else {
								result = "<font color=\"green\"> Converted To Sales Order Successfully!  <br><br><center>"
										+ "<input type=\"button\" value=\"Back\" onClick=\"window.location.href='/track/purchaseorderservlet?PONO="
										+ olddono + "&Submit=View&RFLAG=4'\"> ";
							}

							/*
							 * result =
							 * "<font color=\"green\"> Converted To Sales Order Successfully!  <br><br><center>"
							 * +
							 * "<input type=\"button\" value=\"Back\" onClick=\"window.location.href='/track/jsp/copyfunction.jsp'\">"
							 * ;
							 */
						} else {
							result = "<font color=\"red\"> Error in Converting To Sales Order  - Please Check the Data  <br><br><center>"
									+ "<input type=\"button\" value=\"Back\" onClick=\"window.location.href='javascript:history.back()'\"> ";

						}
						}
						else {
							result = "<font color=\"red\"> Error in Sales Order  - Invalid Customer <br><br><center>"
									+ "<input type=\"button\" value=\"Back\" onClick=\"window.location.href='javascript:history.back()'\"> ";
							result = "<br><h3>" + result;
						}


					} else {

						result = "<font color=\"red\"> Please select Order Number first before converting to Sales Order. <br><br><center>"
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

			} else if (action.equalsIgnoreCase("ExportOutboundSalesSummary")) {
				HSSFWorkbook wb = new HSSFWorkbook();
				wb = this.writeToExcelOutboundSalessmry(request, response, wb);
				ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
				wb.write(outByteStream);
				byte[] outArray = outByteStream.toByteArray();
				response.setContentType("application/ms-excel");
				response.setContentLength(outArray.length);
				response.setHeader("Expires:", "0"); // eliminates browser caching
				response.setHeader("Content-Disposition", "attachment; filename=OutboundsalesSummary.xls");
				OutputStream outStream = response.getOutputStream();
				outStream.write(outArray);
				outStream.flush();

			}
			// End code by Bruhan to add price editable on edit outbound order on
			// 26/02/2013
			else if (action.equalsIgnoreCase("getRemarks")) {
				String dono = StrUtils.fString(request.getParameter("DONO")).trim();
				String item = StrUtils.fString(request.getParameter("ITEM")).trim();
				String dolno = StrUtils.fString(request.getParameter("DOLNNO")).trim();
				List al = new ArrayList();

				if (dono.length() > 0) {

					String query = "REMARKS,DOLNNO";
					Hashtable ht = new Hashtable();
					ht.put(IDBConstants.DODET_DONUM, dono);
					ht.put(IDBConstants.DODET_DOLNNO, dolno);
					ht.put(IDBConstants.DODET_ITEM, item);
					ht.put(IDBConstants.PLANT, plant);
					al = _DoDetDAO.selectRemarks(query, ht);
					JSONObject json = new JSONObject();
					json.put("remarksList", al);
					response.getWriter().print(json.toString());
				} else {
					JSONObject json = new JSONObject();
					json.put("error", "No data found");
					response.getWriter().print(json.toString());
				}

			} else if (action.equalsIgnoreCase("GET_PREVIOUS_ORDER_DETAILS")) {
				JSONObject jsonObjectResult = new JSONObject();
				jsonObjectResult = this.getPreviousOrderDetails(request);
				response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                response.getWriter().write(jsonObjectResult.toString());
                response.getWriter().flush();
                response.getWriter().close();
			}
		} catch (Exception ex) {
			this.mLogger.exception(this.printLog, "", ex);

			String result = "<font class = " + IConstants.FAILED_COLOR + ">Exception : " + ex.getMessage() + "</font>";
			result = result + " <br><br><center> "
					+ "<input type=\"button\" value=\"Back\" onClick=\"window.location.href='javascript:history.back()'\"> ";
			result = "<br><h3>" + result;
			request.getSession().setAttribute("RESULT", result);
			response.sendRedirect("jsp/displayResult2User.jsp");

		}

	}

	private void diaplayInfoLogs(HttpServletRequest request) {
		try {
			Map requestParameterMap = request.getParameterMap();
			Set<String> keyMap = requestParameterMap.keySet();
			StringBuffer requestParams = new StringBuffer();
			requestParams.append("[[Class Name : " + this.getClass() + "]]\t");
			requestParams.append("[[Paramter Mapping]]\t");
			for (String key : keyMap) {
				requestParams.append("[" + key + " : " + request.getParameter(key) + "] ");
			}
			this.mLogger.auditInfo(this.printInfo, requestParams.toString());

		} catch (Exception e) {

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
//			Hashtable ht = new Hashtable();
//			HttpSession session = request.getSession();
//			String plant = StrUtils.fString((String) session.getAttribute("PLANT")).trim();
//			ht.put("PLANT", plant);
//			ht.put("WHID", "");
//			ht.put("LOC", locId);
//			ht.put("LOCDESC", locDesc);
//			ht.put("USERFLD1", remarks);
//			boolean flag = false;
//			if (flag) {
//				request.getSession().setAttribute("locMstData", ht);
//				result = "<font class = " + IConstants.SUCCESS_COLOR + ">Location Added Successfully</font>";
//			} else {
//				throw new Exception("Unable to add location ");
//			}
//
//		} catch (Exception e) {
//			throw e;
//		}
//
//		return result;
//	}

	private String updateDoHdr(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, Exception {
		boolean flag = false;
		Hashtable ht = new Hashtable();
		MovHisDAO movHisDao = new MovHisDAO();
		movHisDao.setmLogger(mLogger);
		String ordertypeString = "";
		String rflag = "", result = "", pono = "", custName = "", display = "", ordertype = "", jobNum = "",
				currencyid = "", custCode = "", user = "", personIncharge = "", contactNum = "", address = "",
				address2 = "", address3 = "", collectionDate = "", collectionTime = "", remark1 = "", remark2 = "",
				deliverydate = "", timeslots = "", outbound_Gst = "", orderstatus = "", empno = "", remark3 = "",
				shippingcustomer = "", shippingid = "", orderdiscount = "", shippingcost = "", incoterms = "",paymenttype="",sTAXTREATMENT="",sSALES_LOC="";
		try {
			rflag = StrUtils.fString(request.getParameter("RFLAG")).trim();
			pono = StrUtils.fString(request.getParameter("DONO")).trim();
			custName = StrUtils.fString(request.getParameter("CUST_NAME")).trim();
			user = StrUtils.fString(request.getParameter("LOGIN_USER")).trim();
			jobNum = StrUtils.fString(request.getParameter("JOB_NUM")).trim();
			personIncharge = StrUtils.fString(request.getParameter("PERSON_INCHARGE")).trim();
			contactNum = StrUtils.fString(request.getParameter("CONTACT_NUM")).trim();
			custCode = StrUtils.fString(request.getParameter("CUST_CODE1")).trim();
			address = StrUtils.fString(request.getParameter("ADDRESS")).trim();
			address2 = StrUtils.fString(request.getParameter("ADDRESS2")).trim();
			address3 = StrUtils.fString(request.getParameter("ADDRESS3")).trim();
			collectionDate = new DateUtils().parseDate(StrUtils.fString(request.getParameter("DELDATE")).trim());
			collectionTime = StrUtils.fString(request.getParameter("COLLECTION_TIME")).trim();
			remark1 = StrUtils.fString(request.getParameter("REMARK1")).trim();
			remark2 = StrUtils.fString(request.getParameter("REMARK2")).trim();
			remark3 = StrUtils.fString(request.getParameter("REMARK3")).trim();
			ordertype = StrUtils.fString(request.getParameter("ORDERTYPE")).trim();
			display = StrUtils.fString(request.getParameter("DISPLAY")).trim();
			deliverydate = StrUtils.fString(request.getParameter("DELIVERYDATE")).trim();
			timeslots = StrUtils.fString(request.getParameter("TIMESLOTS")).trim();
			outbound_Gst = StrUtils.fString(request.getParameter("GST")).trim();
			paymenttype = StrUtils.fString(request.getParameter("PAYMENTTYPE")).trim();
			if (collectionDate.length() == 0) {
				collectionDate = DateUtils.getDate();
			}
			orderstatus = StrUtils.fString(request.getParameter("STATUS_ID")).trim();
			empno = StrUtils.fString(request.getParameter("EMP_NAME")).trim();
			shippingcustomer = StrUtils.fString(request.getParameter("SHIPPINGCUSTOMER")).trim();
			shippingid = StrUtils.fString(request.getParameter("SHIPPINGID")).trim();
			orderdiscount = StrUtils.fString(request.getParameter("ORDERDISCOUNT")).trim();
			shippingcost = StrUtils.fString(request.getParameter("SHIPPINGCOST")).trim();
			incoterms = StrUtils.fString(request.getParameter("INCOTERMS")).trim();
			String DATEFORMAT = StrUtils.fString((request.getParameter("DATEFORMAT") != null) ? "1": "0").trim();
			sTAXTREATMENT=StrUtils.fString(request.getParameter("TAXTREATMENT")).trim();
			sSALES_LOC=StrUtils.fString(request.getParameter("SALES_LOC")).trim();
			HttpSession session = request.getSession();
			String plant = StrUtils.fString((String) session.getAttribute("PLANT")).trim();
			Hashtable curHash = new Hashtable();
			curHash.put(IConstants.PLANT, plant);
			curHash.put(IConstants.DISPLAY, display);
			if (display != null && display != "") {
				currencyid = curUtil.getCurrencyID(curHash, "CURRENCYID");
			}

			ht.put(IDBConstants.PLANT, plant);
			ht.put(IDBConstants.DODET_DONUM, pono);
			ht.put(IDBConstants.DOHDR_CUST_CODE, custCode);
			ht.put(IDBConstants.DOHDR_CUST_NAME, StrUtils.InsertQuotes(custName));
			ht.put(IDBConstants.DOHDR_JOB_NUM, jobNum);
			ht.put(IDBConstants.DOHDR_PERSON_INCHARGE, StrUtils.InsertQuotes(personIncharge));
			ht.put(IDBConstants.DOHDR_CONTACT_NUM, contactNum);
			ht.put(IDBConstants.DOHDR_ADDRESS, address);
			ht.put(IDBConstants.DOHDR_ADDRESS2, address2);
			ht.put(IDBConstants.DOHDR_ADDRESS3, address3);
			ht.put(IDBConstants.DOHDR_COL_DATE, collectionDate);
			ht.put(IDBConstants.DOHDR_COL_TIME, collectionTime);
			ht.put(IDBConstants.DOHDR_REMARK1, StrUtils.InsertQuotes(remark1));
			ht.put(IDBConstants.DOHDR_REMARK2, StrUtils.InsertQuotes(remark2));
			ht.put(IDBConstants.DOHDR_REMARK3, StrUtils.InsertQuotes(remark3));
			ht.put(IDBConstants.STATUS, "N");
			ht.put(IDBConstants.ORDERTYPE, ordertype);
			ht.put(IDBConstants.CURRENCYID, currencyid);
			ht.put(IDBConstants.TIMESLOTS, timeslots);
			ht.put(IDBConstants.DELIVERYDATE, deliverydate);
			ht.put(IDBConstants.DOHDR_GST, outbound_Gst);
			ht.put(IDBConstants.ORDSTATUSID, orderstatus);
			ht.put(IDBConstants.DOHDR_EMPNO, empno);
			ht.put(IDBConstants.SHIPPINGCUSTOMER, shippingcustomer);
			ht.put(IDBConstants.SHIPPINGID, shippingid);
			ht.put(IDBConstants.ORDERDISCOUNT, orderdiscount);
			ht.put(IDBConstants.SHIPPINGCOST, shippingcost);
			ht.put(IDBConstants.INCOTERMS, incoterms);
			ht.put(IDBConstants.PAYMENTTYPE, paymenttype);
			ht.put(IDBConstants.POHDR_DELIVERYDATEFORMAT, DATEFORMAT);
			ht.put(IDBConstants.TAXTREATMENT, sTAXTREATMENT);
			ht.put(IDBConstants.SALES_LOCATION, sSALES_LOC);
//			boolean isvalidOrderType = true;

			CustomerBeanDAO customerBeanDAO = new CustomerBeanDAO();
			customerBeanDAO.setmLogger(mLogger);
			boolean isvalidCustomer = customerBeanDAO.isExistsCustomerName(custName, plant);
			Boolean isvalidShippingCustomer = Boolean.valueOf(true);
			if (shippingcustomer.length() > 0) {
				isvalidShippingCustomer = _MasterDAO.isExistsShippingDetails(shippingcustomer, plant);
			}
			if (isvalidCustomer) {
				if (isvalidShippingCustomer) {

					dOUtil.setmLogger(mLogger);
					flag = dOUtil.updateDoHdr(ht);
					if (flag) {
						//	Call Accounting module for new order : Start
						/*if ("yes".equals(request.getSession(false).getAttribute("isAccountingEnabled"))) {
							try {
								Form form = new Form();
								form.param("data", "{\"dono\":\"" + pono + "\",\"xndate\":\"" + collectionDate + "\",\"customer\":\"" + custCode + "\",\"amount\":\"0\",\"transactionType\":\"3\",\"plant\":\"" + plant + "\",\"user\":\"" + user + "\",\"currency\":\"" + currencyid + "\",\"action\":\"update\"}");							
								String finTransactionResponse = HttpUtils.connect(HttpUtils.getHostInfo(request) + IConstants.ACCOUNTING_APPLICATION_URL.replace("trackaccounting", plant) + "restFinTransaction/create", form);
								if (finTransactionResponse == null || finTransactionResponse.startsWith("Error : ")) {
									throw new Exception(finTransactionResponse);
								}
							}catch(Exception e) {
								throw new Exception(ThrowableUtil.getMessage(e), e);
							}						
						}*/
						//	Call Accounting module for new order : End
						Hashtable htRecvHis = new Hashtable();
						htRecvHis.clear();
						htRecvHis.put(IDBConstants.PLANT, plant);
						htRecvHis.put("DIRTYPE", TransactionConstants.UPDATE_OB);
						htRecvHis.put(IDBConstants.CUSTOMER_CODE, custCode);
						htRecvHis.put(IDBConstants.POHDR_JOB_NUM, jobNum);
						htRecvHis.put(IDBConstants.MOVHIS_ORDNUM, pono);
						htRecvHis.put(IDBConstants.CREATED_BY, user);
						htRecvHis.put("MOVTID", "");
						htRecvHis.put("RECID", "");
						if (!remark1.equals("")) {
							htRecvHis.put(IDBConstants.REMARKS,
									StrUtils.InsertQuotes(custName) + "," + StrUtils.InsertQuotes(remark1));
						} else {
							htRecvHis.put(IDBConstants.REMARKS, StrUtils.InsertQuotes(custName));
						}

						htRecvHis.put(IDBConstants.TRAN_DATE, DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));
						htRecvHis.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());

						flag = movHisDao.insertIntoMovHis(htRecvHis);
//						boolean masterHisFlag = false;
						Hashtable htMaster = new Hashtable();
						if(flag){
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
//								masterHisFlag = 
										movHisDao.insertIntoMovHis(htRecvHis);
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
//								masterHisFlag = 
										movHisDao.insertIntoMovHis(htRecvHis);
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
//								masterHisFlag = 
										movHisDao.insertIntoMovHis(htRecvHis);
							}
						}
					}
					}

					if (rflag.equals("1") || rflag.equals("3") || rflag.equals("4")) {
						ordertypeString = "Sales Order";
					} else if (rflag.equals("5")) { // createMobileOrder
						ordertypeString = "Mobile Order";
					} else if (rflag.equals("6")) { // maintMobileOrder
						ordertypeString = "Mobile Order";
					} else if (rflag.equals("7")) { // createMobileEnquiry
						ordertypeString = "Mobile Enquiry Order ";
					} else if (rflag.equals("8")) { // maintMobileEnquiry
						ordertypeString = "Mobile Enquiry Order ";
					} else if (rflag.equals("9")) { // createMobileEnquiry
						ordertypeString = "Mobile Registration Order ";
					} else if (rflag.equals("10")) { // maintMobileEnquiry
						ordertypeString = "Mobile Registration Order ";
					} else {
						ordertypeString = " Order ";
					}

					if (flag) {

						result = "<font color=\"green\"> " + ordertypeString
								+ " Updated Successfully.  <br><br><center>"
								+ "<input type=\"button\" value=\"Back\" onClick=\"window.location.href='/track/deleveryorderservlet?DONO="
								+ pono + "&Submit=View'\"> ";
						result = ""  + "<br><h3>" + result + "</h3>";

					} else {
						result = "<font color=\"red\"> Error in Updating " + ordertypeString
								+ "  - Please Check the Data  <br><br><center>"
								+ "<input type=\"button\" value=\"Back\" onClick=\"window.location.href='javascript:history.back()'\"> ";
						result = "" + ordertypeString + "<br><h3>" + result + "</h3>";
					}
				} else {
					result = "<font color=\"red\"> Enter Valid Shipping Customer <br><br><center>"
							+ "<input type=\"button\" value=\"Back\" onClick=\"window.location.href='javascript:history.back()'\"> ";
					result = "" + ordertypeString + "<br><h3>" + result + "</h3>";
				}
			} else {
				result = "<font color=\"red\"> Enter Valid Customer <br><br><center>"
						+ "<input type=\"button\" value=\"Back\" onClick=\"window.location.href='javascript:history.back()'\"> ";
				result = "" + ordertypeString + "<br><h3>" + result + "</h3>";
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		}

		return result;
	}

//	private String DeletePo(HttpServletRequest request, HttpServletResponse response)
//			throws ServletException, IOException, Exception {
//		String result = "", pono = "";
//		Hashtable htCondition = new Hashtable();
//		try {
//			pono = request.getParameter("PONO").trim();
//			Hashtable ht = new Hashtable();
//			HttpSession session = request.getSession();
//			String plant = StrUtils.fString((String) session.getAttribute("PLANT")).trim();
//			htCondition.put("LOC", pono);
//			htCondition.put("PLANT", plant);
//			boolean flag = false;
//			if (flag) {
//				ht = new Hashtable();
//				ht.put("PLANT", plant);
//				ht.put("WHID", "");
//				ht.put("LOC", "");
//				ht.put("LOCDESC", "");
//				ht.put("USERFLD1", "");
//				request.getSession().setAttribute("locMstData", ht);
//				result = "<font class = " + IConstants.SUCCESS_COLOR + ">Location Deleted Successfully</font>&"
//						+ "action = show_result";
//			} else {
//				throw new Exception("Unable to delete location ");
//			}
//		} catch (Exception e) {
//			this.mLogger.exception(this.printLog, "", e);
//			throw e;
//		}
//		return result;
//	}

	private String SaveData(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, Exception {
		HttpSession session = request.getSession();
		boolean flag = false;
//		boolean isAutoGenerate = false;
//		boolean dotransferhdr = false;
		Hashtable ht = new Hashtable();
		// Hashtable htIsExist = new Hashtable();
		MovHisDAO movHisDao = new MovHisDAO();
		movHisDao.setmLogger(mLogger);
		String rflag = "", ordertypeString = "", result = "", dono = "", custName = "", display = "", ordertype = "",
				currencyid = "", custCode = "", jobNum = "", personIncharge = "", user_id = "", contactNum = "",
				address = "", address2 = "", address3 = "", collectionDate = "", collectionTime = "", deliverydate = "",
				timeslots = "", outbound_Gst = "", orderstatus = "", empno = "", remark1 = "", remark2 = "",
				remark3 = "", deldate = "", processno = "", shippingcustomer = "",
				shippingid = "", orderdiscount = "", shippingcost = "", incoterms = "",DATEFORMAT="",paymenttype="",sTAXTREATMENT="",sSALES_LOC="";
		/*
		 * shipaddr2 = "", shipCity = "", shipCountry = "", shipZip = "", shiptelno = "", 
		 * shipCustname = "", shipCname = "", shipaddr1 = "",
		 * */
		String plant = StrUtils.fString((String) session.getAttribute("PLANT")).trim();

		try {
			rflag = StrUtils.fString(request.getParameter("RFLAG")).trim();
			dono = StrUtils.fString(request.getParameter("DONO")).trim();
			custName = StrUtils.replaceCharacters2Recv(StrUtils.fString(request.getParameter("CUST_NAME"))).trim();
			custCode = StrUtils.fString(request.getParameter("CUST_CODE1")).trim();
			user_id = StrUtils.fString(request.getParameter("LOGIN_USER")).trim();
			jobNum = StrUtils.fString(request.getParameter("JOB_NUM")).trim();
			personIncharge = StrUtils.fString(request.getParameter("PERSON_INCHARGE")).trim();
			contactNum = StrUtils.fString(request.getParameter("CONTACT_NUM")).trim();
			address = StrUtils.fString(request.getParameter("ADD1")).trim();
			address2 = StrUtils.fString(request.getParameter("ADD2")).trim();
			address3 = StrUtils.fString(request.getParameter("ADD3")).trim();
			collectionDate = new DateUtils().parseDate(StrUtils.fString(request.getParameter("DELDATE")).trim());
			collectionTime = StrUtils.fString(request.getParameter("COLLECTION_TIME")).trim();
			remark1 = StrUtils.fString(request.getParameter("REMARK1")).trim();
			remark2 = StrUtils.fString(request.getParameter("REMARK2")).trim();
			remark3 = StrUtils.fString(request.getParameter("REMARK3")).trim();
			display = StrUtils.fString(request.getParameter("DISPLAY")).trim();
			ordertype = StrUtils.fString(request.getParameter("ORDERTYPE")).trim();
			deliverydate = StrUtils.fString(request.getParameter("DELIVERYDATE")).trim();
			timeslots = StrUtils.fString(request.getParameter("TIMESLOTS")).trim();
			outbound_Gst = StrUtils.fString(request.getParameter("GST")).trim();
			orderstatus = StrUtils.fString(request.getParameter("STATUS_ID")).trim();
			empno = StrUtils.fString(request.getParameter("EMP_NAME")).trim();
			shippingcustomer = StrUtils.fString(request.getParameter("SHIPPINGCUSTOMER")).trim();
			shippingid = StrUtils.fString(request.getParameter("SHIPPINGID")).trim();
			empno = StrUtils.fString(request.getParameter("EMP_NAME")).trim();
			deldate = new DateUtils().parseDate(StrUtils.fString(request.getParameter("DELDATE")).trim());
			orderdiscount = StrUtils.fString(request.getParameter("ORDERDISCOUNT")).trim();
			shippingcost = StrUtils.fString(request.getParameter("SHIPPINGCOST")).trim();
			incoterms = StrUtils.fString(request.getParameter("INCOTERMS")).trim();
			paymenttype = StrUtils.fString(request.getParameter("PAYMENTTYPE")).trim();
			DATEFORMAT = StrUtils.fString((request.getParameter("DATEFORMAT") != null) ? "1": "0").trim();
			sTAXTREATMENT=StrUtils.fString(request.getParameter("TAXTREATMENT")).trim();
			sSALES_LOC=StrUtils.fString(request.getParameter("SALES_LOC")).trim();
			if (collectionDate.length() == 0) {
				collectionDate = DateUtils.getDate();
			}

//			isAutoGenerate = Boolean.valueOf(StrUtils.fString(request.getParameter("ISAUTOGENERATE")).trim());
			Hashtable curHash = new Hashtable();
			curHash.put(IConstants.PLANT, plant);
			curHash.put(IConstants.DISPLAY, display);
			if (display != null && display != "") {
				currencyid = curUtil.getCurrencyID(curHash, "CURRENCYID");
			}
			boolean isvalidOrderType = true;
			if (!ordertype.equals("") && !ordertype.equals("MOBILE ORDER") && !ordertype.equals("MOBILE ENQUIRY")
					&& !ordertype.equals("MOBILE REGISTRATION")) {
				OrderTypeBeanDAO orderTypeBeanDAO = new OrderTypeBeanDAO();
				orderTypeBeanDAO.setmLogger(mLogger);
				isvalidOrderType = orderTypeBeanDAO.isOrderTypeExists(ordertype, plant);
			}
			CustomerBeanDAO customerBeanDAO = new CustomerBeanDAO();
			customerBeanDAO.setmLogger(mLogger);
			boolean isvalidCustomer = customerBeanDAO.isExistsCustomerName(custName, plant);
			Boolean isvalidShippingCustomer = Boolean.valueOf(true);
			if (shippingcustomer.length() > 0) {
				isvalidShippingCustomer = _MasterDAO.isExistsShippingDetails(shippingcustomer, plant);
			}
			if (isvalidCustomer) {
				if (isvalidShippingCustomer) {
					if (isvalidOrderType) {

						ht.put(IDBConstants.PLANT, plant);
						ht.put(IDBConstants.DOHDR_DONUM, dono);
						ht.put(IDBConstants.DOHDR_CUST_CODE, custCode);
						ht.put(IDBConstants.DOHDR_CUST_NAME, StrUtils.InsertQuotes(custName));
						ht.put(IDBConstants.DOHDR_JOB_NUM, jobNum);
						ht.put(IDBConstants.DOHDR_PERSON_INCHARGE, StrUtils.InsertQuotes(personIncharge));
						ht.put(IDBConstants.DOHDR_CONTACT_NUM, contactNum);
						ht.put(IDBConstants.DOHDR_ADDRESS, address);
						ht.put(IDBConstants.DOHDR_ADDRESS2, address2);
						ht.put(IDBConstants.DOHDR_ADDRESS3, address3);
						ht.put(IDBConstants.CREATED_BY, user_id);
						ht.put(IDBConstants.DOHDR_COL_DATE, collectionDate);
						ht.put(IDBConstants.DOHDR_COL_TIME, collectionTime);
						ht.put(IDBConstants.DOHDR_REMARK1, StrUtils.InsertQuotes(remark1));
						ht.put(IDBConstants.DOHDR_REMARK2, StrUtils.InsertQuotes(remark2));
						ht.put(IDBConstants.DOHDR_REMARK3, StrUtils.InsertQuotes(remark3));
						ht.put(IDBConstants.STATUS, "N");
						ht.put(IDBConstants.ORDERTYPE, ordertype);
						ht.put(IDBConstants.CURRENCYID, currencyid);
						ht.put(IDBConstants.TIMESLOTS, timeslots);
						ht.put(IDBConstants.DOHDR_DEL_DATE, deldate);
						ht.put(IDBConstants.DELIVERYDATE, deliverydate);
						ht.put(IDBConstants.DOHDR_GST, outbound_Gst);
						ht.put(IDBConstants.ORDSTATUSID, orderstatus);
						ht.put(IDBConstants.DOHDR_EMPNO, empno);
						ht.put(IDBConstants.SHIPPINGCUSTOMER, shippingcustomer);
						ht.put(IDBConstants.SHIPPINGID, shippingid);
						ht.put(IDBConstants.ORDERDISCOUNT, orderdiscount);
						ht.put(IDBConstants.SHIPPINGCOST, shippingcost);
						ht.put(IDBConstants.INCOTERMS, incoterms);
						ht.put(IDBConstants.PAYMENTTYPE, paymenttype);
						ht.put(IDBConstants.POHDR_DELIVERYDATEFORMAT, DATEFORMAT);
						ht.put(IDBConstants.SALES_LOCATION, sSALES_LOC);
						ht.put(IDBConstants.TAXTREATMENT, sTAXTREATMENT);
						dOUtil.setmLogger(mLogger);
						flag = dOUtil.saveWMSDoHdrDetails(ht);
						ht.remove(IDBConstants.ORDERTYPE);
						ht.remove(IDBConstants.ORDSTATUSID);

						if (flag) {
//							dotransferhdr = 
									_DOTransferUtil.saveDoTransferHdrDetails(ht);
							Hashtable htRecvHis = new Hashtable();
							htRecvHis.clear();
							htRecvHis.put(IDBConstants.PLANT, plant);
							htRecvHis.put("DIRTYPE", TransactionConstants.CREATE_OB);
							htRecvHis.put(IDBConstants.CUSTOMER_CODE, custCode);
							htRecvHis.put(IDBConstants.POHDR_JOB_NUM, jobNum);
							htRecvHis.put(IDBConstants.MOVHIS_ORDNUM, dono);
							htRecvHis.put(IDBConstants.CREATED_BY, user_id);
							htRecvHis.put("MOVTID", "");
							htRecvHis.put("RECID", "");
							if (!remark1.equals("")) {
								htRecvHis.put(IDBConstants.REMARKS,
										StrUtils.InsertQuotes(custName) + "," + StrUtils.InsertQuotes(remark1));
							} else {
								htRecvHis.put(IDBConstants.REMARKS, StrUtils.InsertQuotes(custName));
							}

							htRecvHis.put(IDBConstants.TRAN_DATE, DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));
							htRecvHis.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());

							flag = movHisDao.insertIntoMovHis(htRecvHis);

//							boolean masterHisFlag = false;
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
//										masterHisFlag = 
												movHisDao.insertIntoMovHis(htRecvHis);
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
//										masterHisFlag = 
												movHisDao.insertIntoMovHis(htRecvHis);
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
//										masterHisFlag = 
												movHisDao.insertIntoMovHis(htRecvHis);
									}
								}
							}

						}

						if (rflag.equals("1") || rflag.equals("3") || rflag.equals("4")) {
							ordertypeString = "Sales Order";
						} else if (rflag.equals("5")) { // createMobileOrder
							ordertypeString = "Mobile Order";
						} else if (rflag.equals("6")) { // maintMobileOrder
							ordertypeString = "Mobile Order";
						} else if (rflag.equals("7")) {
							ordertypeString = "Mobile Enquiry Order ";
						} else if (rflag.equals("9")) {
							ordertypeString = "Mobile Registration Order ";
						}
						if (flag) {
							new TblControlUtil().updateTblControlSeqNo(plant, IConstants.OUTBOUND, "S", dono, !(rflag.equals("1") || rflag.equals("3") || rflag.equals("4")));
							
							result = "<font color=\"green\"> " + ordertypeString
									+ " Created Successfully.  <br><br><center>"
									+ "<input type=\"button\" value=\"Back\" onClick=\"window.location.href='/track/deleveryorderservlet?DONO="
									+ dono + "&Submit=View'\"> ";
							result = " " + "<br><h3>" + result + "</h3>";
							//	Call Accounting module for new order : Start
							/*if ("yes".equals(request.getSession(false).getAttribute("isAccountingEnabled"))) {
								try {
									Hashtable htAcct = new Hashtable();
									htAcct.put(IDBConstants.PLANT, plant);
									if (_MasterDAO.isExisitAccount(htAcct, " ACCOUNT_TITLE='Customers'")) {
										//	Get Acct. Id
										String Acctid=getNextIdDetails(plant,user_id);
										if(Acctid!="")
										{
											if (!_MasterDAO.isExisitAccount(htAcct, " CUSTOMER_NO='"+custCode+"' AND ACCOUNT_TYPE=1 AND PARENT_ACCOUNT_NO=12  ")) {
											flag = _MasterDAO.InsertAccount(htAcct,Acctid,custName,"",custCode,user_id,1,12);//insert Supplier Account
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
									form.param("data", "{\"dono\":\"" + dono + "\",\"xndate\":\"" + collectionDate + "\",\"customer\":\"" + custCode + "\",\"amount\":\"0\",\"transactionType\":\"3\",\"plant\":\"" + plant + "\",\"user\":\"" + user_id + "\",\"currency\":\"" + currencyid + "\",\"action\":\"header\"}");							
									String finTransactionResponse = HttpUtils.connect(HttpUtils.getHostInfo(request) + IConstants.ACCOUNTING_APPLICATION_URL.replace("trackaccounting", plant) + "restFinTransaction/create", form);
									if (finTransactionResponse == null || finTransactionResponse.startsWith("Error : ")) {
										throw new Exception(finTransactionResponse);
									}
										}
										}
									}
								}catch(Exception e) {
									throw new Exception(ThrowableUtil.getMessage(e), e);
								}							
							}*/
							//	Call Accounting module for new order : End
							result = " " + "<br><h3>" + result + "</h3>";
							// Start added by Bruhan for Email Notification on 11 July 2012.
							String isAutoEmail = emailDao.getIsAutoEmailDetails(plant, IConstants.SALES_ORDER);
							if (isAutoEmail.equalsIgnoreCase("Y"))
								mailUtil.sendEmail(plant, dono, IConstants.SALES_ORDER);
							// End added by Bruhan for Email Notification on 11 July 2012.

						} else {
							result = "<font color=\"red\"> Error in Creating " + ordertypeString
									+ "  - Please Check the Data  <br><br><center>"
									+ "<input type=\"button\" value=\"Back\" onClick=\"window.location.href='javascript:history.back()'\"> ";
							result = " " + ordertypeString + " <br><h3>" + result + "</h3>";

						}
					} else {
						result = "<font color=\"red\"> Enter Valid Order Type <br><br><center>"
								+ "<input type=\"button\" value=\"Back\" onClick=\"window.location.href='javascript:history.back()'\"> ";
						result = "<br><h3>" + result + "</h3>";
					}
				} else {
					result = "<font color=\"red\"> Enter Valid Shipping Customer <br><br><center>"
							+ "<input type=\"button\" value=\"Back\" onClick=\"window.location.href='javascript:history.back()'\"> ";
					result = " " + ordertypeString + "<br><h3>" + result + "</h3>";
				}
			} else {
				result = "<font color=\"red\"> Enter Valid Customer <br><br><center>"
						+ "<input type=\"button\" value=\"Back\" onClick=\"window.location.href='javascript:history.back()'\"> ";
				result = " " + ordertypeString + "<br><h3>" + result + "</h3>";
			}

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			if (ThrowableUtil.getMessage(e).equalsIgnoreCase("Outbound order created already")) {
				processno = dono;
				dono = _TblControlDAO.getNextOrder(plant, user_id, IConstants.OUTBOUND);
				ht.clear();
				ht.put(IDBConstants.PLANT, plant);
				ht.put(IDBConstants.DOHDR_DONUM, dono);
				ht.put(IDBConstants.DOHDR_CUST_CODE, custCode);
				ht.put(IDBConstants.DOHDR_CUST_NAME, StrUtils.InsertQuotes(custName));
				ht.put(IDBConstants.DOHDR_JOB_NUM, jobNum);
				ht.put(IDBConstants.DOHDR_PERSON_INCHARGE, StrUtils.InsertQuotes(personIncharge));
				ht.put(IDBConstants.DOHDR_CONTACT_NUM, contactNum);
				ht.put(IDBConstants.DOHDR_ADDRESS, address);
				ht.put(IDBConstants.DOHDR_ADDRESS2, address2);
				ht.put(IDBConstants.DOHDR_ADDRESS3, address3);
				ht.put(IDBConstants.CREATED_BY, user_id);
				ht.put(IDBConstants.DOHDR_COL_DATE, collectionDate);
				ht.put(IDBConstants.DOHDR_COL_TIME, collectionTime);
				ht.put(IDBConstants.DOHDR_REMARK1, StrUtils.InsertQuotes(remark1));
				ht.put(IDBConstants.DOHDR_REMARK2, StrUtils.InsertQuotes(remark2));
				ht.put(IDBConstants.DOHDR_REMARK3, StrUtils.InsertQuotes(remark3));
				ht.put(IDBConstants.STATUS, "N");
				ht.put(IDBConstants.ORDERTYPE, ordertype);
				ht.put(IDBConstants.CURRENCYID, currencyid);
				ht.put(IDBConstants.TIMESLOTS, timeslots);
				ht.put(IDBConstants.DOHDR_DEL_DATE, deldate);
				ht.put(IDBConstants.DELIVERYDATE, deliverydate);
				ht.put(IDBConstants.DOHDR_GST, outbound_Gst);
				ht.put(IDBConstants.ORDSTATUSID, orderstatus);
				ht.put(IDBConstants.DOHDR_EMPNO, empno);
				ht.put(IDBConstants.SHIPPINGCUSTOMER, shippingcustomer);
				ht.put(IDBConstants.SHIPPINGID, shippingid);
				ht.put(IDBConstants.ORDERDISCOUNT, orderdiscount);
				ht.put(IDBConstants.SHIPPINGCOST, shippingcost);
				ht.put(IDBConstants.INCOTERMS, incoterms);
				ht.put(IDBConstants.PAYMENTTYPE, paymenttype);
				ht.put(IDBConstants.POHDR_DELIVERYDATEFORMAT, DATEFORMAT);
				ht.put(IDBConstants.SALES_LOCATION, sSALES_LOC);
				ht.put(IDBConstants.TAXTREATMENT, sTAXTREATMENT);
				// Added for Shiiping address

				dOUtil.setmLogger(mLogger);
				flag = dOUtil.saveWMSDoHdrDetails(ht);
				ht.remove(IDBConstants.ORDERTYPE);
				ht.remove(IDBConstants.ORDSTATUSID);
				if (flag) {
//					dotransferhdr = 
							_DOTransferUtil.saveDoTransferHdrDetails(ht);
					Hashtable htRecvHis = new Hashtable();
					htRecvHis.clear();
					htRecvHis.put(IDBConstants.PLANT, plant);
					htRecvHis.put("DIRTYPE", TransactionConstants.CREATE_OB);
					htRecvHis.put(IDBConstants.CUSTOMER_CODE, custCode);
					htRecvHis.put(IDBConstants.POHDR_JOB_NUM, jobNum);
					htRecvHis.put(IDBConstants.MOVHIS_ORDNUM, dono);
					htRecvHis.put(IDBConstants.CREATED_BY, user_id);
					htRecvHis.put("MOVTID", "");
					htRecvHis.put("RECID", "");
					if (!remark1.equals("")) {
						htRecvHis.put(IDBConstants.REMARKS,
								StrUtils.InsertQuotes(custName) + "," + StrUtils.InsertQuotes(remark1));
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
							htMaster.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
							htMaster.put(IDBConstants.CREATED_BY, user_id);
							if (!_MasterDAO.isExisitRemarks(htMaster, "")) {
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
					new TblControlUtil().updateTblControlSeqNo(plant, IConstants.OUTBOUND, "S", dono);
				}
				result = "<font color=\"green\"> Sales order " + processno
						+ " has already been used. System has auto created a new sales order " + dono
						+ " for you.<br><br><center>"
						+ "<input type=\"button\" value=\"Back\" onClick=\"window.location.href='/track/deleveryorderservlet?DONO="
						+ dono + "&Submit=View'\"> ";
				result = " " + ordertypeString + "<br><h3>" + result + "</h3>";
				processno = "";

			} else {
				throw e;
			}

		}

		return result;
	}

	/*******
	 * Modification History ****************************** Bruhan,July
	 * 22,2014,Description: To check is null for collectiondate and collectiontime
	 * 
	 * 
	 */
	private boolean DisplayData(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException, Exception {
		String dono = StrUtils.fString(request.getParameter("DONO"));
		String fieldDesc = "<tr><td> Please Choose options from the list box shown above</td></tr>";
		ArrayList al = new ArrayList();
		Hashtable htCond = new Hashtable();
		Map m = new HashMap();
		if (dono.length() > 0) {
			HttpSession session = request.getSession();
			String plant = StrUtils.fString((String) session.getAttribute("PLANT")).trim();
			String rflag = StrUtils.fString((String) session.getAttribute("RFLAG")).trim();
			htCond.put("PLANT", plant);
			htCond.put("DONO", dono);

			String query = "isnull(dono,'') dono,isnull(outbound_Gst,0) outbound_Gst,isnull(ordertype,'')ordertype ,"
					+ "(select isnull(display,'') display from " + "[" + plant
					+ "_CURRENCYMST] where currencyid = a.currencyid) currencyid,"
					+ "isnull(custCode,'') custCode,isnull(jobNum,'') jobNum, isnull(ordertype,'') ordertype,"
					+ "isnull(personInCharge,'') personInCharge,isnull(contactNum,'') contactNum,isnull(address,'') address,"
					+ "isnull(address2,'') address2,isnull(address3,'') address3,isnull(collectionDate,'') collectionDate,"
					+ "isnull(collectionTime,'') collectionTime,isnull(remark1,'') remark1,isnull(remark2,'') remarks2,"
					+ "b.CNAME as custName,isnull(b.name,'') as contactname,isnull(b.telno,'') as telno,"
					+ "isnull(b.hpno,'') as hpno,isnull(b.hpno,'') as hpno,isnull(b.email,'') email,"
					+ "isnull(b.addr1,'') add1,isnull(b.addr2,'') add2,isnull(b.addr3,'') add3,"
					+ "isnull(b.remarks,'') remarks,isnull(b.addr4,'') add4,isnull(b.country,'') country,"
					+ "isnull(b.zip,'') zip,isnull(deliverydate,'') deliverydate,isnull(timeslots,'') timeslots,"
					+ "isnull(shippingid,'')as shippingid, isnull(shippingcustomer,'') as shippingcustomer,"
					+ "isnull(STATUS_ID,'') as statusid,isnull(empno,'') as empno,isnull(remark3,'') remark3,"
					+ "ISNULL(CUSTOMER_STATUS_ID,'') customerstatusid,ISNULL(CUSTOMER_TYPE_ID,'') customertypeid,"
					+ "isnull(orderdiscount,0) as orderdiscount,isnull(shippingcost,0) as shippingcost,"
					+ "isnull(incoterms,'') as incoterms,isnull(DELIVERYDATEFORMAT,'') as DELIVERYDATEFORMAT,isnull(PAYMENTTYPE,'') payment_terms,"
					+ "ISNULL(A.SALES_LOCATION,'') SALES_LOCATION,ISNULL(A.TAXTREATMENT,'') TAXTREATMENT ";
			dOUtil.setmLogger(mLogger);
			al = dOUtil.getOutGoingDoHdrDetails(query, htCond);
			if (al.size() > 0) {
				m = (Map) al.get(0);
				if (rflag.equals("7") || rflag.equals("8") || rflag.equals("9") || rflag.equals("10")) {// mobile
																										// Enquiry
					fieldDesc = dOUtil.listDODETForMobileEnquiry(plant, dono, rflag);
				} else {
					fieldDesc = dOUtil.listDODET(plant, dono, rflag);
				}
			} else {

				fieldDesc = "<tr><td colspan=\"9\" align=\"center\">No Records Available</td></tr>";
			}	

		}
		request.getSession().setAttribute("podetVal", m);
		request.getSession().setAttribute("dono", dono);
		request.getSession().setAttribute("RESULT", fieldDesc);
		return true;
	}

	public void viewInvoiceReport(HttpServletRequest request, HttpServletResponse response)
			throws IOException, Exception {
		Connection con = null;
		boolean ajax = "XMLHttpRequest".equals(request.getHeader("X-Requested-With"));
		try {
			CustUtil cUtil = new CustUtil();
			PlantMstUtil pmUtil = new PlantMstUtil();
			selectBean sb = new selectBean();
			DOUtil _DOUtil = new DOUtil();
			CurrencyUtil currUtil = new CurrencyUtil();
			sb.setmLogger(mLogger);
			cUtil.setmLogger(mLogger);
			pmUtil.setmLogger(mLogger);
			currUtil.setmLogger(mLogger);
			HttpSession session = request.getSession();
			String Plant = (String) session.getAttribute("PLANT");
			if(Plant == null)
				Plant = (String) request.getAttribute("PLANT");//for mail - Azees 09_2022
			List listQry = pmUtil.getPlantMstDetails(Plant);//Azees Fix Query Load 09.22
			Map maps = (Map) listQry.get(0);
//			Map m = null;
			String CNAME = "", CADD1 = "", CADD2 = "", CADD3 = "", CADD4 = "", CZIP = "", CCOUNTRY = "", CSTATE = "",SEALNAME="",SIGNNAME="",
					CTEL = "", CFAX = "", CONTACTNAME = "", CHPNO = "", CEMAIL = "", CRCBNO = "",companyregno = "",CWEBSITE="";
			String SHPCUSTOMERNAME = "", SHPCONTACTNAME = "", SHPTELEPHONE = "", SHPHANDPHONE = "", SHPUNITNO = "", SHPBUILDING = "", SHPSTREET = "", SHPCITY = "", SHPSTATE = "",
					SHPCOUNTRY = "", SHPPOSTALCODE = "", DATAORDERDISCOUNT = "", DATASHIPPINGCOST = "",
					DATAINCOTERMS = "",ORDERDISCOUNTTYPE = "",TAX_TYPE="",ISDISCOUNTTAX="",ISSHIPPINGTAX="",
							ISORDERDISCOUNTTAX = "",DISCOUNT = "",DISCOUNT_TYPE="",PROJECT_NAME="",sales_location=""/*, 
							SHPFAX = "", SHPEMAIL = "",AdjustmentAmount=""*/;
			int TAXID=0;
			String DONO = null;
//			String amt = (String)request.getParameter("AMOUNT"); //gets value from salesorder detail page (salesorderdetail.jsp)
//			double totalAmount = Double.parseDouble(amt);
			List lstDoNo = new ArrayList();
			if (request.getParameter("lstDoNo") != null) {
				lstDoNo = Arrays.asList(request.getParameter("lstDoNo").split(","));
			}else if (request.getParameter("chkdDoNo") != null) {
				lstDoNo = Arrays.asList(request.getParameterValues("chkdDoNo"));
			} else if (request.getParameter("DONO") != null) {
				lstDoNo = Arrays.asList(request.getParameter("DONO").split(","));
			} else {
				lstDoNo = Arrays.asList(((String)request.getAttribute("DONO")).split(","));
			}
			List jasperPrintList = new ArrayList();
//			String file = "";
			for (int lstDoNoIndex = 0; lstDoNoIndex < lstDoNo.size(); lstDoNoIndex++) {
				DONO = StrUtils.fString(lstDoNo.get(lstDoNoIndex).toString());
				//System.out.println("DONO : " + DONO);
				String PLANT =Plant;
				//String PLANT = (String) session.getAttribute("PLANT");
				String PRINTBY = StrUtils.fString(request.getParameter("PRINT"));
				String Brand = StrUtils.fString(request.getParameter("BRAND"));
				String Type = StrUtils.fString(request.getParameter("TYPE"));
				String SHIPPINGID = StrUtils.fString(request.getParameter("SHIPPINGID"));
				String Class = StrUtils.fString(request.getParameter("CLASS"));
				String taxby =new PlantMstDAO().getTaxBy(PLANT);
				String imagePath = DbBean.COMPANY_LOGO_PATH + "/" + PLANT.toLowerCase() + DbBean.LOGO_FILE;
				String imagePath1 = DbBean.COMPANY_LOGO_PATH + "/" + PLANT.toLowerCase() + "Logo1.gif";
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

				String imagePath2, Orientation = "";
				con = DbBean.getConnection();
//				String SysDate = DateUtils.getDate();
				String jasperPath = DbBean.JASPER_INPUT + "/" + "rptInvoice";
				String COMP_INDUSTRY = new PlantMstDAO().getCOMP_INDUSTRY(Plant);//Check Company Industry
				if(COMP_INDUSTRY.equalsIgnoreCase("Centralised Kitchen"))
					jasperPath = DbBean.JASPER_INPUT + "/" + "rptInvoiceKitchen";
//				Date sysTime1 = new Date(System.currentTimeMillis());
//				Date cDt = new Date(System.currentTimeMillis());
				//ArrayList listQry = pmUtil.getPlantMstDetails(PLANT);//Azees Fix Query Load 09.22

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
					companyregno = (String) map.get("companyregnumber");//imtiuen
					CWEBSITE = (String) map.get("WEBSITE");
				}
				ArrayList arrCust = cUtil.getCustomerDetailsForDO(DONO, PLANT);
				//System.out.println("arrCust size : " + arrCust.size());
				if (arrCust.size() > 0) {
					String sCustCode = (String) arrCust.get(0);
					String sCustName = (String) arrCust.get(1);
					String sAddr1 = (String) arrCust.get(2);
					String sAddr2 = (String) arrCust.get(3);
					String sAddr3 = (String) arrCust.get(4);
					String sCountry = (String) arrCust.get(5);
					String sZip = (String) arrCust.get(6);
//					String sCons = (String) arrCust.get(7);
//					String sCustNameL = (String) arrCust.get(8);
					String sContactName = (String) arrCust.get(9);
//					String sDesgination = (String) arrCust.get(10);
					String sTelNo = (String) arrCust.get(11);
//					String sHpNo = (String) arrCust.get(12);
					String sFax = (String) arrCust.get(13);
					String sEmail = (String) arrCust.get(14);
					String sRemarks = (String) arrCust.get(15);
					String sAddr4 = (String) arrCust.get(16);
					String orderRemarks = (String) arrCust.get(18);
//					String sPayTerms = (String) arrCust.get(19);
//					String sPayDays = (String) arrCust.get(20);
					String orderRemarks3 = (String) arrCust.get(21);
					String sDeliveryDate = (String) arrCust.get(22);
					String sEmpno = (String) arrCust.get(23);
					String sState = (String) arrCust.get(24);
					SHIPPINGID = (String) arrCust.get(25);
					String sRcbno = (String) arrCust.get(26);
					String suenno = (String) arrCust.get(27);//imtiuen
					String pays = (String) arrCust.get(28);
					//String orderType = new DoHdrDAO().getOrderTypeForDO(PLANT, DONO);
					Map parameters = new HashMap();
					parameters.put("numberOfDecimal", Integer.parseInt(numberOfDecimal));
					
					Hashtable htCond = new Hashtable();
					htCond.put("PLANT", PLANT);
					htCond.put("DONO", DONO);
					String query = "currencyid,isnull(outbound_Gst,0) as outbound_Gst,isnull(incoterms,'') incoterms,"
							+ "CASE WHEN isnull(ORDERDISCOUNTTYPE,'%')='%' THEN isnull(orderdiscount,0) ELSE (isnull(orderdiscount,0)*ISNULL(CURRENCYUSEQT,1)) END AS orderdiscount,"
							+ "CASE WHEN isnull(DISCOUNT_TYPE,'%')='%' THEN isnull(DISCOUNT,0) ELSE (isnull(DISCOUNT,0)*ISNULL(CURRENCYUSEQT,1)) END AS DISCOUNT,"
							+ "isnull(CRBY,'') as CRBY,isnull(PAYMENTTYPE,'') as PAYMENTTYPE,isnull(shippingcost,0)*ISNULL(CURRENCYUSEQT,1) shippingcost,"
							+ "isnull(ORDERDISCOUNTTYPE,'%') ORDERDISCOUNTTYPE,isnull(ITEM_RATES,0) ISTAXINCLUSIVE,ISNULL(TAXID,0) TAXID,"
							+ "ISNULL((select DISTINCT TAXTYPE from FINCOUNTRYTAXTYPE B WHERE B.ID="+PLANT+"_dohdr.TAXID),'') TAX_TYPE,"
							+ "ISNULL((select DISTINCT DISPLAY from "+PLANT+"_CURRENCYMST B WHERE B.CURRENCYID="+PLANT+"_dohdr.currencyid),'') curDisplay,"
							+ "ISNULL((select DISTINCT PROJECT_NAME from "+PLANT+"_FINPROJECT B WHERE B.ID="+PLANT+"_dohdr.PROJECTID),'') PROJECT_NAME,"
							+ "ISNULL((select DISTINCT PREFIX from FINSALESLOCATION B WHERE B.STATE="+PLANT+"_dohdr.sales_location),'') sales_location,"
							+ "ISNULL(ADJUSTMENT,0)*ISNULL(CURRENCYUSEQT,1) ADJUSTMENT,ISNULL(ISDISCOUNTTAX,0) ISDISCOUNTTAX,ISNULL(ISSHIPPINGTAX,0) ISSHIPPINGTAX,ISNULL(ISORDERDISCOUNTTAX,0) ISORDERDISCOUNTTAX,"
							+ "ISNULL(DISCOUNT_TYPE,'') DISCOUNT_TYPE";
					ArrayList arraydohdr = new ArrayList();
					arraydohdr =new DOUtil().getDoHdrDetails(query, htCond);
					Map dataMap = (Map) arraydohdr.get(0);
					String gstValue = dataMap.get("outbound_Gst").toString();
					DATAORDERDISCOUNT = dataMap.get("orderdiscount").toString();
					DATASHIPPINGCOST = dataMap.get("shippingcost").toString();
					DATAINCOTERMS = dataMap.get("incoterms").toString();
					// Get Currency Display
//					Hashtable curHash = new Hashtable();//Azees Fix Query Load 09.22
//					curHash.put(IDBConstants.PLANT, PLANT);
//					curHash.put(IDBConstants.CURRENCYID, (String) dataMap.get("currencyid"));
//					String curDisplay = currUtil.getCurrencyID(curHash, "DISPLAY");
					String curDisplay =  (String) dataMap.get("curDisplay");
					System.out.println("CurDisplay" + curDisplay);
					if (request.getParameter("with_batch") != null
							&& "yes".equals(request.getParameter("with_batch"))) {
						parameters.put("PrintBatch", "yes");
					}
					// Customer Details
					parameters.put("imagePath", imagePath);
					parameters.put("imagePath1", imagePath1);
					parameters.put("OrderNo", DONO);
					parameters.put("company", PLANT);
					parameters.put("taxInvoiceTo_CompanyName", sCustName);
					if(!COMP_INDUSTRY.equalsIgnoreCase("Centralised Kitchen"))
					{
					parameters.put("taxInvoiceTo_BlockAddress", sAddr1 + "  " + sAddr2);
					parameters.put("taxInvoiceTo_RoadAddress", sAddr3 + "  " + sAddr4);
					if (sState.equals("")) {
						parameters.put("taxInvoiceTo_Country",sCountry);
					}else {
						parameters.put("taxInvoiceTo_Country",sState+ "\n" +sCountry);
					}
					} else {
						String taxInvoiceTo_BlockAddress="";
						if(sAddr2.length()>0)
							taxInvoiceTo_BlockAddress=sAddr2+ "  ";
						parameters.put("taxInvoiceTo_BlockAddress", taxInvoiceTo_BlockAddress + sAddr3);//1.Building+Street
						parameters.put("taxInvoiceTo_RoadAddress", sAddr1);//2.Uint No.
						if (sState.equals("")) {
							parameters.put("taxInvoiceTo_Country",sCountry);
						}else {
							parameters.put("taxInvoiceTo_Country",sState+ "\n" +sCountry);
						}
					}
					parameters.put("taxInvoiceTo_State", sState);
					parameters.put("taxInvoiceTo_ZIPCode", sZip);
					parameters.put("taxInvoiceTo_AttentionTo", sContactName);
					parameters.put("taxInvoiceTo_CCTO", "");
					parameters.put("To_TelNo", sTelNo);
					parameters.put("To_Fax", sFax);
					parameters.put("To_Email", sEmail);
					parameters.put("sRCBNO", sRcbno);
					parameters.put("sUENNO", suenno);//imtiuen
					parameters.put("fromAddress_UENNO", companyregno);//imtiuen
					
					//AUTHOR : imthiyas  
					//DATE : June 28,2021
					//DESC : display transportmode and paymentterms in jasper
					DoHdrDAO doHdrDAO= new DoHdrDAO();
					DoHdr doheader = doHdrDAO.getDoHdrById(PLANT, DONO);
					TransportModeDAO transportmodedao = new TransportModeDAO();
					String transportmode = "";
					if(doheader.getTRANSPORTID() > 0){
						transportmode = transportmodedao.getTransportModeById(PLANT, doheader.getTRANSPORTID());
					}
					String paymentterms = "";
					paymentterms = doheader.getPAYMENT_TERMS();
					parameters.put("trans", transportmode);
					parameters.put("payterms", paymentterms);
					//end
					
					// Company Details
//					parameters.put("fromAddress_CompanyName", CNAME);
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
					
					parameters.put("fromAddress_CompanyName", CNAME);
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
					parameters.put("fromAddress_Website", " "+CWEBSITE);
					parameters.put("currentTime", _DOUtil.getOrderDate(PLANT, DONO));
					parameters.put("taxInvoiceNo", "");
					parameters.put("InvoiceTerms", "");
					//parameters.put("refNo", _DOUtil.getJobNum(PLANT, DONO));//Azees Fix Query Load 09.22
					parameters.put("refNo", doheader.getJobNum());
					OrderTypeDAO ODAO = new OrderTypeDAO();
					String orderDesc = "";
					String orderType = doheader.getORDERTYPE();
					if(orderType.equals(""))
						orderType="OUTBOUND ORDER";
					String ordertype = orderType;
					if (ordertype.equalsIgnoreCase("Mobile Order")) {
						ordertype = "Mobile Order";
					} else {
						ordertype = "Outbound Order";
					}
					// report template parameter added on June 22 2011 By Bruhan

					DOUtil poUtil = new DOUtil();
					Map ma = poUtil.getDOReceiptInvoiceHdrDetails(PLANT, ordertype);
					Orientation = (String) ma.get("PrintOrientation");
					if (orderType.equals("OUTBOUND ORDER")) {
						orderDesc = (String) ma.get("HDR1");
					} else {
						orderDesc = ODAO.getOrderTypeDesc(PLANT, orderType);
					}
					if (ma.get("DISPLAYBYORDERTYPE").equals("1"))
						parameters.put("OrderHeader", orderDesc);
					else
						parameters.put("OrderHeader", (String) ma.get("HDR1"));
					// parameters.put("OrderHeader", (String) ma.get("HDR1"));
					parameters.put("ToHeader", (String) ma.get("HDR2"));
					parameters.put("FromHeader", (String) ma.get("HDR3"));
					parameters.put("Date", (String) ma.get("DATE"));
					parameters.put("OrderNoHdr", (String) ma.get("ORDERNO"));
					parameters.put("RefNo", (String) ma.get("REFNO"));
					parameters.put("Terms", (String) ma.get("TERMS"));
					parameters.put("PreBy", (String) ma.get("PREPAREDBY"));
					parameters.put("PreByUser", dataMap.get("CRBY").toString());
//					if (ma.get("PRINTCUSTERMS").equals("1")) {
						//parameters.put("TermsDetails", sPayTerms);
						parameters.put("TermsDetails", dataMap.get("PAYMENTTYPE").toString());
//					} else {
//						parameters.put("TermsDetails", (String) ma.get("TERMSDETAILS"));
//					}
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
					if (taxby.equalsIgnoreCase("BYORDER")) {
						parameters.put("TotalTax", (String) ma.get("TOTALTAX") + " " + "(" + gstValue + "%" + ")");
					} else {
						parameters.put("TotalTax", (String) ma.get("TOTALTAX"));
					}
					parameters.put("Total", (String) ma.get("TOTAL") + " " + "(" + curDisplay + ")");
					
					String PRINTROUNDOFFTOTALWITHDECIMAL =  (String) ma.get("PRINTROUNDOFFTOTALWITHDECIMAL");
					parameters.put("PrintRoundoffTotalwithDecimal", PRINTROUNDOFFTOTALWITHDECIMAL);
					parameters.put("RoundoffTotalwithDecimal", (String) ma.get("ROUNDOFFTOTALWITHDECIMAL") + " " + "(" + curDisplay + ")");
					

//					double dTotalCost ="".equals(totalAmount) ? 0.0d : totalAmount;
					InvoicePaymentDAO invoicePaymentDAO = new InvoicePaymentDAO();
					double orderadvanceamt = invoicePaymentDAO.getcreditamoutusingorderno(PLANT, DONO);
					double paidamountfororder = invoicePaymentDAO.getpaidamoutusingorderno(PLANT, DONO);	
					double pdcamount = 0.0;			
					
					InvPaymentDetail InvPaymentDetail = new InvPaymentDetail();
					List<InvPaymentDetail> invdetlist = invoicePaymentDAO.getInvoicePaymentDetailsbydono(DONO, PLANT, dataMap.get("CRBY").toString());
					for(InvPaymentDetail invdet:invdetlist){
						Hashtable ht = new Hashtable();	
						ht.put("PAYMENTID",String.valueOf(invdet.getRECEIVEHDRID()));
						ht.put("PLANT",PLANT);
						List pdcDetailListpc = invoicePaymentDAO.getpdcbipayid(ht);
						for(int k =0; k < pdcDetailListpc.size(); k++) {
							Map pdcdet=(Map)pdcDetailListpc.get(k);
							String status = (String)pdcdet.get("STATUS");
							if(status.equalsIgnoreCase("NOT PROCESSED")) {
								pdcamount = pdcamount+(Double.parseDouble((String)pdcdet.get("CHEQUE_AMOUNT")) * Double.parseDouble((String)pdcdet.get("CURRENCYUSEQT")));
							}
						}
					}
					
					String invoiceAmount=StrUtils.addZeroes(((orderadvanceamt + paidamountfororder + pdcamount)), numberOfDecimal);
					
//					double advpayment = Double.parseDouble(invoiceAmount);
//					double balance = totalAmount-advpayment;
//					String balancedue = Double.toString(balance);  
//					DecimalFormat df = new DecimalFormat();
//					df.setMaximumFractionDigits(3);
//					balancedue = df.format(balance);
					parameters.put("PaymentMade", invoiceAmount);
					parameters.put("BalanceDue", invoiceAmount);
//					parameters.put("PaymentMade", advpayment);
//					parameters.put("BalanceDue", balancedue);
				
					parameters.put("PRINTPAYMENTMADE", (String) ma.get("PRINTPAYMENTMADE"));
					parameters.put("PRINTADJUSTMENT", (String) ma.get("PRINTADJUSTMENT"));
					parameters.put("PRINTSHIPPINGCOST", (String) ma.get("PRINTSHIPPINGCOST"));
					parameters.put("PRINTORDERDISCOUNT", (String) ma.get("PRINTORDERDISCOUNT"));
					parameters.put("PRINTBALANCEDUE", (String) ma.get("PRINTBALANCEDUE"));
					parameters.put("BalancedueHdr", (String) ma.get("BALANCEDUE"));
					parameters.put("PaymentMadeHdr", (String) ma.get("PAYMENTMADE"));
					parameters.put("PRINTWITHINCOTERM", (String) ma.get("PRINTINCOTERM"));
					
					parameters.put("Footer1", (String) ma.get("F1"));
					parameters.put("Footer2", (String) ma.get("F2"));
					parameters.put("Footer3", (String) ma.get("F3"));
					parameters.put("Footer4", (String) ma.get("F4"));
					parameters.put("Footer5", (String) ma.get("F5"));
					parameters.put("Footer6", (String) ma.get("F6"));
					parameters.put("Footer7", (String) ma.get("F7"));
					parameters.put("Footer8", (String) ma.get("F8"));
					parameters.put("Footer9", (String) ma.get("F9"));
					parameters.put("PRINTWITHPRODUCT", (String) ma.get("PRINTWITHPRODUCT"));
					parameters.put("PrdXtraDetails", (String) ma.get("PRINTXTRADETAILS"));
					parameters.put("PRITNWITHHSCODE", (String) ma.get("PRITNWITHHSCODE"));
					parameters.put("PRITNWITHCOO", (String) ma.get("PRITNWITHCOO"));
					parameters.put("PRINTBUYER", (String) ma.get("PRINTBUYER"));
					parameters.put("PRINTBUYERSIGN", (String) ma.get("PRINTBUYERSIGN"));
					parameters.put("PRINTWITHPRODUCTREMARKS", (String) ma.get("PRINTWITHPRODUCTREMARKS"));
					parameters.put("PRINTWITHBRAND", (String) ma.get("PRINTWITHBRAND"));
					parameters.put("CUSTOMERRCBNO", (String) ma.get("CUSTOMERRCBNO"));
					parameters.put("TOTALAFTERDISCOUNT", (String) ma.get("TOTALAFTERDISCOUNT"));
					parameters.put("HSCODE", (String) ma.get("HSCODE"));
					parameters.put("COO", (String) ma.get("COO"));
					String both =(String) ma.get("PRINTWITHUENNO");//by imthi to disply both uen and gst
					if(both.equalsIgnoreCase("2")) {
						parameters.put("RCBNO", ", "+(String) ma.get("RCBNO"));
					}else {
						parameters.put("RCBNO", (String) ma.get("RCBNO"));
					}
					parameters.put("UENNO", (String) ma.get("UENNO"));//imtiuen
					parameters.put("CUSTOMERUENNO", (String) ma.get("CUSTOMERUENNO"));//imtiuen
					parameters.put("PRINTUENNO",  ma.get("PRINTWITHUENNO"));//imthiuen
					parameters.put("PRINTCUSTOMERUENNO",  ma.get("PRINTWITHCUSTOMERUENNO"));//imthiuen
					parameters.put("PRINTWITHSHIPINGADD",  ma.get("PRINTWITHSHIPINGADD"));
					if(COMP_INDUSTRY.equalsIgnoreCase("Centralised Kitchen"))
					{
						parameters.put("AddOnCost",  "Add On Cost");
						parameters.put("Cost",  "Cost");
					}
					int PRINTWITHSHIPINGADD = Integer.valueOf((String) ma.get("PRINTWITHSHIPINGADD"));
					if(PRINTWITHSHIPINGADD == 1) {
					// get shipping details from shipping master table
					ArrayList arrShippingDetails = _masterUtil.getOutboundShippingDetails(DONO, sCustCode, PLANT);					
					if (arrShippingDetails.size() > 0) {
						parameters.put("shipToId", sCustCode);
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
					}else {
						parameters.put("shipToId", "");
					}
					}else {
						parameters.put("shipToId", "");
					}
					//AUTHOR : imthiyas  
					//DATE : June 28,2021
					//DESC : display transportmode and paymentterms in jasper
					parameters.put("trans", transportmode);
					
					if (ma.get("PRINTCUSTERMS").equals("1")) {
						parameters.put("payterms", pays);
						} else {
							parameters.put("payterms", paymentterms);
						}
//					parameters.put("payterms", paymentterms);
					parameters.put("PRINTWITHTRANSPORT_MODE", (String) ma.get("PRINTWITHTRANSPORT_MODE"));
					parameters.put("TRANSPORT_MODE", (String) ma.get("TRANSPORT_MODE"));
					parameters.put("TERMSDETAILS", (String) ma.get("TERMSDETAILS"));
					//end
					
					if (ma.get("PCUSREMARKS").equals("1")) {
						parameters.put("SupRemarks", sRemarks);
					} else {
						parameters.put("SupRemarks", "");
					}
					parameters.put("curDisplay", curDisplay);
					if (orderRemarks.length() > 0)
						orderRemarks = (String) ma.get("REMARK1") + " : " + orderRemarks;
					if (orderRemarks3.length() > 0)
						orderRemarks3 = (String) ma.get("REMARK2") + " : " + orderRemarks3;
					parameters.put("orderRemarks", orderRemarks);
					parameters.put("orderRemarks3", orderRemarks3);
//					if (sDeliveryDate.length() > 0)
//						sDeliveryDate = sDeliveryDate;
					parameters.put("DeliveryDt", sDeliveryDate);
					parameters.put("DeliveryDate", (String) ma.get("DELIVERYDATE"));
					parameters.put("lblDeliveryDate", sDeliveryDate);
					parameters.put("DATADELIVERYDATE ", (String) ma.get("DELIVERYDATE"));
					parameters.put("lblExpiryDate", "");//TODO : Check the input
					parameters.put("DATAEXPIRYDATE ", "");//TODO : Check the input
					parameters.put("Employee", (String) ma.get("EMPLOYEE"));
					//String empname = new EmployeeDAO().getEmpname(PLANT, sEmpno, "");
					if (ma.get("PRINTEMPLOYEE").equals("1")) {
						parameters.put("Employee", (String) ma.get("EMPLOYEE"));
						String empname = new EmployeeDAO().getEmpname(PLANT, sEmpno, "");
						parameters.put("EmployeeName", empname);
					} else {
						parameters.put("Employee", "");
						parameters.put("EmployeeName", "");
					}
					/*
					 * if(ma.get("PRINTEMPLOYEE").equals("1")){ parameters.put("Employee", (String)
					 * ma.get("EMPLOYEE")); String empname = new
					 * EmployeeDAO().getEmpname(PLANT,sEmpno, ""); parameters.put("EmployeeName",
					 * empname); } else { parameters.put("Employee", "");
					 * parameters.put("EmployeeName", ""); }
					 */

					/*
					 * if(sShipCustName.length()>0) { parameters.put("STo_CustName", (String)
					 * ma.get("SHIPTO")+" : "+ "\n"+ sShipCustName);
					 * 
					 * 
					 * }else{ parameters.put("STo_CustName", sShipCustName); }
					 */
					// parameter shipping address
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
					
					//new changes - azees 12/2020 
					parameters.put("ISTAXINCLUSIVE",  dataMap.get("ISTAXINCLUSIVE").toString());
					ORDERDISCOUNTTYPE=dataMap.get("ORDERDISCOUNTTYPE").toString();
					DISCOUNT_TYPE=dataMap.get("DISCOUNT_TYPE").toString();
					ISSHIPPINGTAX=dataMap.get("ISSHIPPINGTAX").toString();
					ISDISCOUNTTAX=dataMap.get("ISDISCOUNTTAX").toString();
					ISORDERDISCOUNTTAX=dataMap.get("ISORDERDISCOUNTTAX").toString();
					PROJECT_NAME=dataMap.get("PROJECT_NAME").toString();
					sales_location=dataMap.get("sales_location").toString();
					DISCOUNT=dataMap.get("DISCOUNT").toString();
					double doubledatabilldiscount = new Double(DISCOUNT);
					parameters.put("DO_DISCOUNT", doubledatabilldiscount);
					parameters.put("ORDERDISCOUNTTYPE", ORDERDISCOUNTTYPE);
					parameters.put("DO_DISCOUNT_TYPE", DISCOUNT_TYPE);
					parameters.put("AdjustmentAmount", dataMap.get("ADJUSTMENT").toString());
					parameters.put("ISDISCOUNTTAX", ISDISCOUNTTAX);
					parameters.put("ISSHIPPINGTAX", ISSHIPPINGTAX);
					parameters.put("ISORDERDISCOUNTTAX", ISORDERDISCOUNTTAX);
					parameters.put("PROJECTNAME", PROJECT_NAME);
					if(ISSHIPPINGTAX.equalsIgnoreCase("0"))
						ISSHIPPINGTAX="Tax Exclusive";
					else
						ISSHIPPINGTAX="Tax Inclusive";
					if(ISDISCOUNTTAX.equalsIgnoreCase("0"))
						ISDISCOUNTTAX="Tax Exclusive";
					else
						ISDISCOUNTTAX="Tax Inclusive";
					if(ISORDERDISCOUNTTAX.equalsIgnoreCase("0"))
						ISORDERDISCOUNTTAX="Tax Exclusive";
					else
						ISORDERDISCOUNTTAX="Tax Inclusive";
					if(DISCOUNT_TYPE.equalsIgnoreCase("%"))
						parameters.put("lblDiscountDO", "Discount ("+ DISCOUNT + "%" +")"+" ("+ISDISCOUNTTAX+")");
					else
						parameters.put("lblDiscountDO", "Discount ("+DISCOUNT_TYPE+")"+" ("+ISDISCOUNTTAX+")");
					TAX_TYPE = dataMap.get("TAX_TYPE").toString();
					/*if(TAX_TYPE.equalsIgnoreCase("EXEMPT") || TAX_TYPE.equalsIgnoreCase("OUT OF SCOPE"))
						TAX_TYPE = StrUtils.fString(TAX_TYPE+" [0.0%]").trim();
					else
						TAX_TYPE = StrUtils.fString(TAX_TYPE+" ["+gstValue+"%]").trim();*/
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
						gstValue="0.0";
					} else if(ptaxiszero == 0 && ptaxisshow == 0){
						TAX_TYPE = "";
						gstValue="0.0";
					} else if(ptaxiszero == 0 && ptaxisshow == 1){						
						if(sales_location.equalsIgnoreCase(""))
							TAX_TYPE = StrUtils.fString(TAX_TYPE+" ["+gstValue+"%]").trim();
						else
							TAX_TYPE = StrUtils.fString(TAX_TYPE+"("+sales_location+") ["+gstValue+"%]").trim();
					} else {							

						if(sales_location.equalsIgnoreCase(""))
							TAX_TYPE = StrUtils.fString(TAX_TYPE+" [0.0%]").trim();
						else
							TAX_TYPE = StrUtils.fString(TAX_TYPE+"("+sales_location+") [0.0%]").trim();
						gstValue="0.0";//Author: Azees  Create date: August 05,2021  Description: zero tax issue 
					}
	  				parameters.put("TAX_TYPE", TAX_TYPE);
					
	  				parameters.put("Adjustment",  ma.get("ADJUSTMENT"));
					parameters.put("PRODUCTRATESARE",  ma.get("PRODUCTRATESARE"));
					parameters.put("PROJECT",  ma.get("PROJECT"));
					parameters.put("ISPROJECT",  ma.get("PRINTWITHPROJECT"));
					
					// parameter shipping address end
	  				if(ORDERDISCOUNTTYPE.equalsIgnoreCase("%"))
						parameters.put("lblOrderDiscount",
								(String) ma.get("ORDERDISCOUNT") + " " + "(" + DATAORDERDISCOUNT + "%" + ") ("+ ISORDERDISCOUNTTAX + ")");
						else
							parameters.put("lblOrderDiscount",
									(String) ma.get("ORDERDISCOUNT") + " " + "(" + ORDERDISCOUNTTYPE + ") ("+ ISORDERDISCOUNTTAX + ")");
					parameters.put("lblShippingCost", (String) ma.get("SHIPPINGCOST") + " " + "(" + curDisplay + ") ("+ ISSHIPPINGTAX + ")");
					parameters.put("lblINCOTERM", (String) ma.get("INCOTERM"));
					parameters.put("lblDeliveryDate", (String) ma.get("DELIVERYDATE"));
					parameters.put("TAXBY", taxby);
					double doubledataorderdiscount = new Double(DATAORDERDISCOUNT);
					double doubledatashippingcost = new Double(DATASHIPPINGCOST);
					parameters.put("DATAORDERDISCOUNT", doubledataorderdiscount);
					parameters.put("DATASHIPPINGCOST", doubledatashippingcost);
					parameters.put("DATAINCOTERMS", DATAINCOTERMS);
					parameters.put("STo", (String) ma.get("SHIPTO"));
					parameters.put("Discount", (String) ma.get("DISCOUNT"));
					parameters.put("NetRate", (String) ma.get("NETRATE"));
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
					//imti start seal,sign condition in printoutconfig
					String PRINTWITHCOMPANYSEAL = (String) ma.get("PRINTWITHCOMPANYSEAL");
					String PRINTWITHCOMPANYSIG= (String) ma.get("PRINTWITHCOMPANYSIG");
					if(PRINTWITHCOMPANYSEAL.equalsIgnoreCase("0")){
		                sealPath = DbBean.COMPANY_LOGO_PATH + "/" + DbBean.NO_LOGO_FILE;
		                }
					if(PRINTWITHCOMPANYSIG.equalsIgnoreCase("0")){
						signPath = DbBean.COMPANY_LOGO_PATH + "/" + DbBean.NO_LOGO_FILE;
		                }
					parameters.put("sealPath", sealPath);
					parameters.put("signPath", signPath);
					
					if (Orientation.equals("Portrait")) {
					if (ma.get("PRINTWITHDISCOUNT").equals("1")){
						if(COMP_INDUSTRY.equalsIgnoreCase("Centralised Kitchen"))
							jasperPath = DbBean.JASPER_INPUT + "/" + "rptInvoiceKitchenWithDiscountPortrait";
						else
							jasperPath = DbBean.JASPER_INPUT + "/" + "rptInvoiceWithDiscountPortrait";
						}else {
						jasperPath = DbBean.JASPER_INPUT + "/" + "rptInvoicePortrait";

					}
					}
					parameters.put("PRINTWITHDELIVERYDATE",  ma.get("PRINTWITHDELIVERYDATE"));
					parameters.put("PRODUCTDELIVERYDATE",  ma.get("PRODUCTDELIVERYDATE"));
					parameters.put("PRINTWITHPRODUCTDELIVERYDATE",  ma.get("PRINTWITHPRODUCTDELIVERYDATE"));
					parameters.put("CALCULATETAXWITHSHIPPINGCOST",  ma.get("CALCULATETAXWITHSHIPPINGCOST"));
					parameters.put("CompanyDate", (String) ma.get("COMPANYDATE"));
					parameters.put("CompanyName", (String) ma.get("COMPANYNAME"));
					parameters.put("CompanyStamp", (String) ma.get("COMPANYSTAMP"));
					parameters.put("CompanySig", (String) ma.get("COMPANYSIG"));
					parameters.put("Seller", (String) ma.get("SELLER"));
					parameters.put("SellerSign", (String) ma.get("SELLERSIGNATURE"));
					parameters.put("Buyer", (String) ma.get("BUYER"));
					parameters.put("BuyerSign", (String) ma.get("BUYERSIGNATURE"));
					// End report template parameter

					// String gstValue = sb.getGST("GST",PLANT);
					double gst = new Double(gstValue).doubleValue() / 100;
					parameters.put("Gst", gst);
					parameters.put("orderType", orderType);

					if (PRINTBY.equalsIgnoreCase("GROUPBY")) {

						if (Brand.equalsIgnoreCase("BRAND")) {
							if (Type.equalsIgnoreCase("TYPE") && Class.equalsIgnoreCase("CLASS")) {
								parameters.put("Cond", " order by b.PRD_BRAND_ID,b.ITEMTYPE,b.PRD_CLS_ID,DOLNNO ");
								parameters.put("Groupby", Brand + "" + Type + "" + Class);
							} else if (Type.equalsIgnoreCase("TYPE")) {
								parameters.put("Cond", " order by b.PRD_BRAND_ID,b.ITEMTYPE,DOLNNO ");
								parameters.put("Groupby", Brand + "" + Type);
							} else if (Class.equalsIgnoreCase("CLASS")) {
								parameters.put("Cond", " order by b.PRD_BRAND_ID,b.PRD_CLS_ID,DOLNNO ");
								parameters.put("Groupby", Brand + "" + Class);
							} else {
								parameters.put("Cond", " order by b.PRD_BRAND_ID,DOLNNO ");
								parameters.put("Groupby", Brand);
							}

						} else if (Type.equalsIgnoreCase("TYPE")) {
							if (Class.equalsIgnoreCase("CLASS")) {
								parameters.put("Cond", " order by b.ITEMTYPE,b.PRD_CLS_ID,DOLNNO ");
								parameters.put("Groupby", Type + "" + Class);
							} else {
								parameters.put("Cond", " order by b.ITEMTYPE,DOLNNO ");
								parameters.put("Groupby", Type);
							}
						} else {
							parameters.put("Cond", " order by b.PRD_CLS_ID,DOLNNO ");
							parameters.put("Groupby", Class);
						}

						jasperPath = DbBean.JASPER_INPUT + "/" + "rptInvoiceByGrouping";
						if (Orientation.equals("Portrait")) {
							if (ma.get("PRINTWITHDISCOUNT").equals("1")){
								jasperPath = DbBean.JASPER_INPUT + "/" + "rptInvoiceByGroupingWithDiscountPortrait";
							}
							else
							{
							jasperPath = DbBean.JASPER_INPUT + "/" + "rptInvoiceByGroupingPortrait";
							}
						}
					}
					
					query = "item,itemdesc,unitprice";
					DoDetDAO dodetdao = new DoDetDAO();						
					ArrayList arraydodet = dodetdao.selectDoDet(query, htCond);
					if(arraydodet.size() > 3)
					{ }
					else
						parameters.put(JRParameter.IS_IGNORE_PAGINATION, true);

					long start = System.currentTimeMillis();
					System.out.println("**************" + " Start Up Time : " + start + "**********");
					System.out.println("jasperPath : " + jasperPath);
					JasperCompileManager.compileReportToFile(jasperPath + ".jrxml", jasperPath + ".jasper");
					jasperPrintList.add((JasperFillManager.fillReport(jasperPath + ".jasper", parameters, con)));
				}

			}
			//System.out.println("jasperPrintList size : " + jasperPrintList.size());
			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

			JRPdfExporter exporter = new JRPdfExporter();
			exporter.setParameter(JRExporterParameter.JASPER_PRINT_LIST, jasperPrintList);
			exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, byteArrayOutputStream);
			// exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME,
			// "D:/jasper/MultipleDO.pdf");
			exporter.exportReport();

			byte[] bytes = byteArrayOutputStream.toByteArray();

			//response.addHeader("Content-disposition", "attachment;filename=MultipleInvoice.pdf");
			
			
			if (!ajax) {
				if ((String)request.getAttribute("ISAUTOMAIL") == null) {//for mail - Azees 09_2022
				//response.addHeader("Content-disposition", "attachment;filename=" + fileName + ".pdf");
				response.addHeader("Content-disposition", "inline;filename=MultipleInvoice.pdf");
				response.setContentLength(bytes.length);
				response.getOutputStream().write(bytes);
				response.setContentType("application/pdf");
				response.getOutputStream().flush();
				response.getOutputStream().close();
				} else {
					try(FileOutputStream fos = new FileOutputStream(DbBean.COMPANY_MAIL_ATTACHMENT_PATH + "/Invoice_" + DONO + ".pdf")){
						fos.write(bytes);
					}
				}
			}else {
//				System.out.println(jasperPath + ".pdf");
				
				try(FileOutputStream fos = new FileOutputStream(DbBean.COMPANY_MAIL_ATTACHMENT_PATH + "/Invoice_" + DONO + ".pdf")){
					fos.write(bytes);
				}
				
			}

		} catch (IOException e) {

			e.printStackTrace();

		} finally {
			DbBean.closeConnection(con);
		}
	}

	public void printInvoiceReport(HttpServletRequest request, HttpServletResponse response)
			throws IOException, Exception {
		Connection con = null;
		boolean ajax = "XMLHttpRequest".equals(request.getHeader("X-Requested-With"));
		try {
			CustUtil cUtil = new CustUtil();
			PlantMstUtil pmUtil = new PlantMstUtil();
			selectBean sb = new selectBean();
			DOUtil _DOUtil = new DOUtil();
			CurrencyUtil currUtil = new CurrencyUtil();
			sb.setmLogger(mLogger);
			cUtil.setmLogger(mLogger);
			pmUtil.setmLogger(mLogger);
			currUtil.setmLogger(mLogger);
			HttpSession session = request.getSession();
			String Plant = (String) session.getAttribute("PLANT");
			String pltCountry = (String) session.getAttribute("COUNTRY");
			if(Plant == null)
				Plant = (String) request.getAttribute("PLANT");//for mail - Azees 03_2022
			//List viewlistQry = pmUtil.getPlantMstDetails(Plant);//Azees Fix Query Load 09.22
			//Map maps = (Map) viewlistQry.get(0);
//			Map m = null;
			String CNAME = "", CADD1 = "", CADD2 = "", CADD3 = "", CADD4 = "", CZIP = "", CCOUNTRY = "", CSTATE = "",SEALNAME="",SIGNNAME="",
					CTEL = "", CFAX = "", CONTACTNAME = "", CHPNO = "", CEMAIL = "", CRCBNO = "",companyregno = "",CWEBSITE="";
			String SHPCUSTOMERNAME = "", SHPCONTACTNAME = "", SHPTELEPHONE = "", SHPHANDPHONE = "", SHPUNITNO = "", SHPBUILDING = "", SHPSTREET = "", SHPCITY = "", SHPSTATE = "",
					SHPCOUNTRY = "", SHPPOSTALCODE = "", DATAORDERDISCOUNT = "", DATASHIPPINGCOST = "",invoiceHdrId="0",totalAmount="",currencySeqt="",ORDERTYPE="",
					DATAINCOTERMS = "",ORDERDISCOUNTTYPE = "",TAX_TYPE="",ISDISCOUNTTAX="",ISSHIPPINGTAX="",
							ISORDERDISCOUNTTAX = "",DISCOUNT = "",DISCOUNT_TYPE="",PROJECT_NAME="",sales_location="",paymentterms = ""/*, 
							SHPFAX = "", SHPEMAIL = "",AdjustmentAmount=""*/;
			int TAXID=0,TRANSPORTID=0;
			String INVOICE = null;
			List lstDoNo = new ArrayList();
			if (request.getParameter("lstDoNo") != null) {
				lstDoNo = Arrays.asList(request.getParameter("lstDoNo").split(","));
			}else if (request.getParameter("chkdDoNo") != null) {
				lstDoNo = Arrays.asList(request.getParameterValues("chkdDoNo"));
			} else if (request.getParameter("INVOICE") != null) {
				lstDoNo = Arrays.asList(request.getParameter("INVOICE").split(","));
			} else {
				lstDoNo = Arrays.asList(((String)request.getAttribute("INVOICE")).split(","));
			}
			List jasperPrintList = new ArrayList();
			JasperPrint jasperPrint = new JasperPrint();
			JasperPrint jasperPrint1 = new JasperPrint();
			JasperPrint jasperPrint2 = new JasperPrint();
//			String file = "";
			//Azees Fix Query Load 09.22
			
			//Imti based on pdf url
			String printPDF = (String)request.getParameter("PRINT"); 
			if(printPDF==null) {printPDF = "defafult";}
			if(printPDF=="") {printPDF = "defafult";}
			if(printPDF.equalsIgnoreCase("")){printPDF = "defafult";}
			//end
			
			String PLANT =Plant;
			ArrayList listQry = pmUtil.getPlantMstDetails(PLANT);
			Map maps = (Map) listQry.get(0);
			for (int lstDoNoIndex = 0; lstDoNoIndex < lstDoNo.size(); lstDoNoIndex++) {
				INVOICE = StrUtils.fString(lstDoNo.get(lstDoNoIndex).toString());
				//System.out.println("DONO : " + DONO);
				//String PLANT = (String) session.getAttribute("PLANT");
				String PRINTBY = StrUtils.fString(request.getParameter("PRINT"));
				String Brand = StrUtils.fString(request.getParameter("BRAND"));
				String Type = StrUtils.fString(request.getParameter("TYPE"));
				String SHIPPINGID = StrUtils.fString(request.getParameter("SHIPPINGID"));
				String Class = StrUtils.fString(request.getParameter("CLASS"));
				String taxby = new PlantMstDAO().getTaxBy(PLANT);
				String imagePath = DbBean.COMPANY_LOGO_PATH + "/" + PLANT.toLowerCase() + DbBean.LOGO_FILE;
				String imagePath1 = DbBean.COMPANY_LOGO_PATH + "/" + PLANT.toLowerCase() + "Logo1.gif";
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

				String imagePath2, Orientation = "";
				con = DbBean.getConnection();
//				String SysDate = DateUtils.getDate();
				String jasperPath = DbBean.JASPER_INPUT + "/" + "rptInvoice";
				String COMP_INDUSTRY = new PlantMstDAO().getCOMP_INDUSTRY(Plant);//Check Company Industry
				if(COMP_INDUSTRY.equalsIgnoreCase("Centralised Kitchen"))
					jasperPath = DbBean.JASPER_INPUT + "/" + "rptInvoiceKitchen";
//				Date sysTime1 = new Date(System.currentTimeMillis());
//				Date cDt = new Date(System.currentTimeMillis());
				

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
					companyregno = (String) map.get("companyregnumber");//imtiuen
					CWEBSITE = (String) map.get("WEBSITE");
				}
				ArrayList arrCust = cUtil.getCustomerDetailsForInvoice(INVOICE, PLANT);
				//System.out.println("arrCust size : " + arrCust.size());
				if (arrCust.size() > 0) {
					String sCustCode = (String) arrCust.get(0);
					String sCustName = (String) arrCust.get(1);
					String sAddr1 = (String) arrCust.get(2);
					String sAddr2 = (String) arrCust.get(3);
					String sAddr3 = (String) arrCust.get(4);
					String sCountry = (String) arrCust.get(5);
					String sZip = (String) arrCust.get(6);
//					String sCons = (String) arrCust.get(7);
//					String sCustNameL = (String) arrCust.get(8);
					String sContactName = (String) arrCust.get(9);
//					String sDesgination = (String) arrCust.get(10);
					String sTelNo = (String) arrCust.get(11);
//					String sHpNo = (String) arrCust.get(12);
					String sFax = (String) arrCust.get(13);
					String sEmail = (String) arrCust.get(14);
					String sRemarks = (String) arrCust.get(15);
					String sAddr4 = (String) arrCust.get(16);
					String orderRemarks = (String) arrCust.get(18);
//					String sPayTerms = (String) arrCust.get(19);
//					String sPayDays = (String) arrCust.get(20);
					String orderRemarks3 = (String) arrCust.get(21);
					String sDeliveryDate = (String) arrCust.get(22);
					String sEmpno = (String) arrCust.get(23);
					String sState = (String) arrCust.get(24);
					SHIPPINGID = (String) arrCust.get(25);
					String sRcbno = (String) arrCust.get(26);
					String suenno = (String) arrCust.get(27);//imtiuen
					String pays = (String) arrCust.get(40);//imtiuen
					String orderType ="";
					Map parameters = new HashMap();
					parameters.put("numberOfDecimal", Integer.parseInt(numberOfDecimal));
					
					Hashtable htCond = new Hashtable();
					htCond.put("PLANT", PLANT);
					htCond.put("INVOICE", INVOICE);
					String query = "ID AS INVOICEHDRID,INVOICE_DATE,ORDERTYPE,currencyid,DUE_DATE,isnull(OUTBOUD_GST,0) as outbound_Gst,isnull(incoterms,'') incoterms,ISNULL(jobNum,'') jobNum,"
							+ " ISNULL( " + "INVOICE_DATE +' '+LEFT(INVOICE_DATE, 2)+':'+RIGHT(INVOICE_DATE, 2)" + ",'') as " + "date" + ", "
							+ "CASE WHEN isnull(ORDERDISCOUNTTYPE,'%')='%' THEN isnull(ORDER_DISCOUNT,0) ELSE (isnull(ORDER_DISCOUNT,0)*ISNULL(CURRENCYUSEQT,1)) END AS orderdiscount,"
							+ "CASE WHEN isnull(DISCOUNT_TYPE,'%')='%' THEN isnull(DISCOUNT,0) ELSE (isnull(DISCOUNT,0)*ISNULL(CURRENCYUSEQT,1)) END AS DISCOUNT,"
							+ "isnull(CRBY,'') as CRBY,'' as PAYMENTTYPE,isnull(shippingcost,0)*ISNULL(CURRENCYUSEQT,1) shippingcost,DONO,GINO,"
							+ "isnull(ORDERDISCOUNTTYPE,'%') ORDERDISCOUNTTYPE,isnull(ITEM_RATES,0) ISTAXINCLUSIVE,ISNULL(TAXID,0) TAXID,"
							+ "ISNULL((select DISTINCT CollectionDate from "+PLANT+"_DOHDR D WHERE D.DONO="+PLANT+"_FININVOICEHDR.DONO),'') orderdate,"
							+ "ISNULL((select DISTINCT TAXTYPE from FINCOUNTRYTAXTYPE B WHERE B.ID="+PLANT+"_FININVOICEHDR.TAXID),'') TAX_TYPE,"
							+ "ISNULL((select DISTINCT PROJECT_NAME from "+PLANT+"_FINPROJECT B WHERE B.ID="+PLANT+"_FININVOICEHDR.PROJECTID),'') PROJECT_NAME,"
							+ "ISNULL((select DISTINCT PREFIX from FINSALESLOCATION B WHERE B.STATE="+PLANT+"_FININVOICEHDR.sales_location),'') sales_location,"
							+ "ISNULL((select DISTINCT DISPLAY from "+PLANT+"_CURRENCYMST B WHERE B.CURRENCYID="+PLANT+"_FININVOICEHDR.currencyid),'') curDisplay,"
							+ "ISNULL(ADJUSTMENT,0)*ISNULL(CURRENCYUSEQT,1) ADJUSTMENT,ISNULL(ISDISCOUNTTAX,0) ISDISCOUNTTAX,ISNULL(ISSHIPPINGTAX,0) ISSHIPPINGTAX,ISNULL(ISORDERDISCOUNTTAX,0) ISORDERDISCOUNTTAX,"
							+ "ISNULL(DISCOUNT_TYPE,'') DISCOUNT_TYPE,ISNULL(TRANSPORTID, '') TRANSPORTID,ISNULL(CURRENCYUSEQT, '') currencyseqt,ISNULL(TOTAL_AMOUNT, '') totalamount,ISNULL(PAYMENT_TERMS, '') PAYMENT_TERMS";
					ArrayList arraydohdr = new ArrayList();
					arraydohdr = new InvoiceDAO().selectInvoiceHdr(query, htCond);
					Map dataMap = (Map) arraydohdr.get(0);
					String gstValue = dataMap.get("outbound_Gst").toString();
					DATAORDERDISCOUNT = dataMap.get("orderdiscount").toString();
					DATASHIPPINGCOST = dataMap.get("shippingcost").toString();
					ORDERTYPE = dataMap.get("ORDERTYPE").toString();
					DATAINCOTERMS = dataMap.get("incoterms").toString();
					invoiceHdrId=dataMap.get("INVOICEHDRID").toString();
					TRANSPORTID = Integer.valueOf((String)dataMap.get("TRANSPORTID"));
					currencySeqt=dataMap.get("currencyseqt").toString();
					totalAmount=dataMap.get("totalamount").toString();
					
		            
					// Get Currency Display
//					Hashtable curHash = new Hashtable();//Azees Fix Query Load 09.22
//					curHash.put(IDBConstants.PLANT, PLANT);
//					curHash.put(IDBConstants.CURRENCYID, (String) dataMap.get("currencyid"));
//					String curDisplay = currUtil.getCurrencyID(curHash, "DISPLAY");
					String curDisplay =  (String) dataMap.get("curDisplay");
					System.out.println("CurDisplay" + curDisplay);
					if (request.getParameter("with_batch") != null
							&& "yes".equals(request.getParameter("with_batch"))) {
						parameters.put("PrintBatch", "yes");
					}
		            
					//Imthi added on 28-03-2022
					//Calculation for BALANCEDUE
					InvoicePaymentDAO InvPaymentdao = new InvoicePaymentDAO();
		        	double InvoicePaymentAmt = InvPaymentdao.getbalacedue(PLANT, invoiceHdrId);
		        	InvoicePaymentAmt=(InvoicePaymentAmt*Float.parseFloat(currencySeqt));
		        	double TotamtCur=(Double.valueOf(totalAmount)*Float.parseFloat(currencySeqt));
		        	double baldue=(TotamtCur-InvoicePaymentAmt); 
		            String balduest = Numbers.toMillionFormat(baldue, numberOfDecimal);
//		            String totAmt = "\033[0;1m" + totalAmount;
//		            System.out.print("\033[0;1m" +"imthiyas faizil"+ totalAmount);
		            parameters.put("BalanceDue", balduest);
		            parameters.put("TotalAmt", totalAmount);
		            
		            
		        	double paymentmade=InvPaymentdao.getbalacedue(PLANT, invoiceHdrId);
		        	double paymentMadeloc =paymentmade;
		        	paymentmade=(paymentmade*Float.parseFloat(currencySeqt));
		        	String Spaymentmade = Numbers.toMillionFormat(paymentmade, numberOfDecimal);
		        	parameters.put("PaymentMade",Spaymentmade);
		            //End
					// Customer Details
					parameters.put("imagePath", imagePath);
					parameters.put("imagePath1", imagePath1);
					parameters.put("OrderNo", INVOICE);
					parameters.put("company", PLANT);
					parameters.put("taxInvoiceTo_CompanyName", sCustName);
					if(!COMP_INDUSTRY.equalsIgnoreCase("Centralised Kitchen"))
					{
						if(sAddr1.equals("")) {
//							sAddr2 = sAddr2;
							parameters.put("taxInvoiceTo_BlockAddress", sAddr2);
						}else {
//							sAddr2 = " "+sAddr2;
							parameters.put("taxInvoiceTo_BlockAddress", sAddr1 +" "+ sAddr2);
						}
//					parameters.put("taxInvoiceTo_BlockAddress", sAddr1 + sAddr2);
					parameters.put("taxInvoiceTo_RoadAddress", sAddr3 + "  " + sAddr4);
					parameters.put("taxInvoiceTo_Country", sCountry);
					} else {
						String taxInvoiceTo_BlockAddress="";
						if(sAddr2.length()>0)
							taxInvoiceTo_BlockAddress=sAddr2+ "  ";
						parameters.put("taxInvoiceTo_RoadAddress", taxInvoiceTo_BlockAddress + sAddr3);//1.Building+Street
						if(sAddr1.equals("")) {
							parameters.put("taxInvoiceTo_BlockAddress", "");//2.Uint No.
						}else {
							parameters.put("taxInvoiceTo_BlockAddress", sAddr1 +"\n");//2.Uint No.
						}
//						parameters.put("taxInvoiceTo_BlockAddress", sAddr1);//2.Uint No.
						parameters.put("taxInvoiceTo_Country", sCountry);
					}
					parameters.put("taxInvoiceTo_State", sState);
					parameters.put("taxInvoiceTo_ZIPCode", sZip);
					parameters.put("taxInvoiceTo_AttentionTo", sContactName);
					parameters.put("taxInvoiceTo_CCTO", "");
					parameters.put("To_TelNo", sTelNo);
					parameters.put("To_Fax", sFax);
					parameters.put("To_Email", sEmail);
					parameters.put("sRCBNO", sRcbno);
					parameters.put("sUENNO", suenno);//imtiuen
					parameters.put("fromAddress_UENNO", companyregno);//imtiuen
					
					TransportModeDAO transportmodedao = new TransportModeDAO();
					String transportmode = "";
					if(TRANSPORTID > 0){
						transportmode = transportmodedao.getTransportModeById(PLANT, TRANSPORTID);
					}
					paymentterms=dataMap.get("PAYMENT_TERMS").toString();
					parameters.put("trans", transportmode);
					parameters.put("payterms", paymentterms);
					//end
					
					// Company Details
					parameters.put("fromAddress_CompanyName", CNAME);
					if(!COMP_INDUSTRY.equalsIgnoreCase("Centralised Kitchen"))
					{
					if(CADD1.equals("")) {
						parameters.put("fromAddress_BlockAddress", CADD2);
					}else {
						parameters.put("fromAddress_BlockAddress", CADD1 + ", " + CADD2);
					}
					//parameters.put("fromAddress_BlockAddress", CADD1 + " " + CADD2);
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
						//parameters.put("fromAddress_RoadAddress", CADD1); //Unit No.
						if(CSTATE.equals("")) {
							parameters.put("fromAddress_Country",  CCOUNTRY);
						}else {
						parameters.put("fromAddress_Country", CSTATE + "," + CCOUNTRY);
						}
						//parameters.put("fromAddress_Country", CCOUNTRY);
					}
					parameters.put("fromAddress_ZIPCode", CZIP);
					parameters.put("fromAddress_TpNo", CTEL);
					parameters.put("fromAddress_FaxNo", CFAX);
					parameters.put("fromAddress_ContactPersonName", CONTACTNAME);
					parameters.put("fromAddress_ContactPersonMobile", CHPNO);
					parameters.put("fromAddress_ContactPersonEmail", CEMAIL);
					parameters.put("fromAddress_RCBNO", CRCBNO);
					parameters.put("fromAddress_Website", CWEBSITE);
					parameters.put("currentTime", dataMap.get("INVOICE_DATE").toString());
					parameters.put("taxInvoiceNo", "");
					parameters.put("InvoiceTerms", "");
					//parameters.put("refNo", dataMap.get("jobNum").toString());
//					String rfno = "";
//					if(dataMap.get("DONO").toString().length()>0)
//						rfno ="SO: "+dataMap.get("DONO").toString()+",";
//					if(dataMap.get("GINO").toString().length()>0)
//						rfno =rfno+"DO: "+dataMap.get("GINO").toString();
//					parameters.put("refNo", rfno);
					OrderTypeDAO ODAO = new OrderTypeDAO();
					String orderDesc = "";
					String ordertype = orderType;
					
					// report template parameter added on June 22 2011 By Bruhan

					DOUtil poUtil = new DOUtil();
					Map ma = poUtil.getDOReceiptInvoiceHdrDetailsDO(PLANT,"Tax Invoice English");					
						orderDesc = (String) ma.get("HDR1");
						if (ORDERTYPE.equals(""))  {
							ORDERTYPE= orderDesc;
						}else { 
							if (ma.get("DISPLAYBYORDERTYPE").equals("1")) {
								orderDesc = ORDERTYPE;
							}
						}
					if (ma.get("DISPLAYBYORDERTYPE").equals("1"))
						parameters.put("OrderHeader", orderDesc);
					else
						parameters.put("OrderHeader", (String) ma.get("HDR1"));
					// parameters.put("OrderHeader", (String) ma.get("HDR1"));
					parameters.put("ToHeader", (String) ma.get("HDR2"));
					parameters.put("FromHeader", (String) ma.get("HDR3"));
					parameters.put("Date", (String) ma.get("INVOICEDATE"));
					parameters.put("OrderNoHdr", (String) ma.get("INVOICENO"));
					parameters.put("RefNo", (String) ma.get("REFNO"));
					parameters.put("Terms", (String) ma.get("TERMS"));
					parameters.put("PreBy", (String) ma.get("PREPAREDBY"));
					parameters.put("PreByUser", dataMap.get("CRBY").toString());
					
					parameters.put("PRINTPAYMENTMADE", (String) ma.get("PRINTPAYMENTMADE"));
					parameters.put("PRINTADJUSTMENT", (String) ma.get("PRINTADJUSTMENT"));
					parameters.put("PRINTSHIPPINGCOST", (String) ma.get("PRINTSHIPPINGCOST"));
					parameters.put("PRINTORDERDISCOUNT", (String) ma.get("PRINTORDERDISCOUNT"));
					parameters.put("PaymentMadeHdr", (String) ma.get("PAYMENTMADE"));
					
					//imti
					if(ma.get("PRINTCUSTERMS").equals("1")) {
						parameters.put("payterms", pays);
					}else{
						parameters.put("payterms", paymentterms);
					}
					//end
					
//					if (ma.get("PRINTCUSTERMS").equals("1")) {
//						//parameters.put("TermsDetails", sPayTerms);
//						parameters.put("TermsDetails", dataMap.get("PAYMENTTYPE").toString());
//					} else {
//						parameters.put("TermsDetails", (String) ma.get("TERMSDETAILS"));
//					}
					
					parameters.put("TermsDetails", (String) ma.get("TERMSDETAILS"));
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
					if (taxby.equalsIgnoreCase("BYORDER")) {
						parameters.put("TotalTax", (String) ma.get("TOTALTAX") + " " + "(" + gstValue + "%" + ")");
					} else {
						parameters.put("TotalTax", (String) ma.get("TOTALTAX"));
					}
					parameters.put("Total", (String) ma.get("TOTAL") + " " + "(" + curDisplay + ")");
					
					String rfno = "";
					if(dataMap.get("DONO").toString().length()>0)
						rfno =(String) ma.get("ORDERNO")+" "+dataMap.get("DONO").toString()+",";
					if(dataMap.get("GINO").toString().length()>0)
						rfno =rfno+(String) ma.get("GINO")+" "+dataMap.get("GINO").toString();
					parameters.put("refNo", rfno);
					
					
					String DONOHDR = (String) ma.get("ORDERNO");
					String DONO = dataMap.get("DONO").toString();
					String DONODATEHDR = (String) ma.get("DATE");
					String orderdate =dataMap.get("orderdate").toString();
					String GINOHDR = (String) ma.get("GINO");
					String GINO = dataMap.get("GINO").toString();
					String DUEDATEHDR = (String) ma.get("DELIVERYDATE");
					String duedate =dataMap.get("DUE_DATE").toString();
					
					parameters.put("DONOHDR", (String) ma.get("ORDERNO"));
					parameters.put("DONO", DONO); 
					parameters.put("DONODATEHDR", (String) ma.get("DATE"));
					parameters.put("orderdate", orderdate);
					parameters.put("GINOHDR", (String) ma.get("GINO"));
					parameters.put("GINO", GINO);
					parameters.put("DUEDATEHDR", DUEDATEHDR);
					parameters.put("duedate", duedate);
					
					String PRINTROUNDOFFTOTALWITHDECIMAL =  (String) ma.get("PRINTROUNDOFFTOTALWITHDECIMAL");
					parameters.put("PrintRoundoffTotalwithDecimal", PRINTROUNDOFFTOTALWITHDECIMAL);
					parameters.put("RoundoffTotalwithDecimal", (String) ma.get("ROUNDOFFTOTALWITHDECIMAL") + " " + "(" + curDisplay + ")");
					
					String Balancedue = (String) ma.get("BALANCEDUE"); 
					parameters.put("BalanceDueHdr", Balancedue+" ("+curDisplay+")");
					parameters.put("PRINTBALANCEDUE", (String) ma.get("PRINTBALANCEDUE"));
					if (ma.get("F1").equals("  ")) parameters.put("Footer1", ""); else parameters.put("Footer1", (String) ma.get("F1")); 
					if (ma.get("F2").equals("  ")) parameters.put("Footer2", ""); else parameters.put("Footer2", (String) ma.get("F2")); 
					if (ma.get("F3").equals("  ")) parameters.put("Footer3", ""); else parameters.put("Footer3", (String) ma.get("F3")); 
					if (ma.get("F4").equals("  ")) parameters.put("Footer4", ""); else parameters.put("Footer4", (String) ma.get("F4")); 
					if (ma.get("F5").equals("  ")) parameters.put("Footer5", ""); else parameters.put("Footer5", (String) ma.get("F5")); 
					if (ma.get("F6").equals("  ")) parameters.put("Footer6", ""); else parameters.put("Footer6", (String) ma.get("F6")); 
					if (ma.get("F7").equals("  ")) parameters.put("Footer7", ""); else parameters.put("Footer7", (String) ma.get("F7")); 
					if (ma.get("F8").equals("  ")) parameters.put("Footer8", ""); else parameters.put("Footer8", (String) ma.get("F8")); 
					if (ma.get("F9").equals("  ")) parameters.put("Footer9", ""); else parameters.put("Footer9", (String) ma.get("F9")); 
					if (ma.get("F10").equals("  ")) parameters.put("Footer10", ""); else parameters.put("Footer10", (String) ma.get("F10")); 
					if (ma.get("F11").equals("  ")) parameters.put("Footer11", ""); else parameters.put("Footer11", (String) ma.get("F11")); 
					if (ma.get("F12").equals("  ")) parameters.put("Footer12", ""); else parameters.put("Footer12", (String) ma.get("F12")); 
					if (ma.get("F13").equals("  ")) parameters.put("Footer13", ""); else parameters.put("Footer13", (String) ma.get("F13")); 
//					parameters.put("Footer1", (String) ma.get("F1"));
//					parameters.put("Footer2", (String) ma.get("F2"));
//					parameters.put("Footer3", (String) ma.get("F3"));
//					parameters.put("Footer4", (String) ma.get("F4"));
//					parameters.put("Footer5", (String) ma.get("F5"));
//					parameters.put("Footer6", (String) ma.get("F6"));
//					parameters.put("Footer7", (String) ma.get("F7"));
//					parameters.put("Footer8", (String) ma.get("F8"));
//					parameters.put("Footer9", (String) ma.get("F9"));
//					parameters.put("Footer10", (String) ma.get("F10"));
//					parameters.put("Footer11", (String) ma.get("F11"));
//					parameters.put("Footer12", (String) ma.get("F12"));
//					parameters.put("Footer13", (String) ma.get("F13"));
					parameters.put("PRINTWITHPRODUCT", (String) ma.get("PRINTWITHPRODUCT"));
					parameters.put("PrdXtraDetails", (String) ma.get("PRINTXTRADETAILS"));
					parameters.put("PRITNWITHHSCODE", (String) ma.get("PRITNWITHHSCODE"));
					parameters.put("PRITNWITHCOO", (String) ma.get("PRITNWITHCOO"));
					parameters.put("PRINTWITHPRODUCTREMARKS", (String) ma.get("PRINTWITHPRODUCTREMARKS"));
					parameters.put("PRINTWITHBRAND", (String) ma.get("PRINTWITHBRAND"));
					parameters.put("CUSTOMERRCBNO", (String) ma.get("CUSTOMERRCBNO"));
					parameters.put("TOTALAFTERDISCOUNT", (String) ma.get("TOTALAFTERDISCOUNT"));
					parameters.put("HSCODE", (String) ma.get("HSCODE"));
					parameters.put("COO", (String) ma.get("COO"));
					String both =(String) ma.get("PRINTWITHUENNO");//by imthi to disply both uen and gst
					if(both.equalsIgnoreCase("2")) {
						parameters.put("RCBNO", ", "+(String) ma.get("RCBNO"));
					}else {
						parameters.put("RCBNO", (String) ma.get("RCBNO"));
					}
//					parameters.put("RCBNO", (String) ma.get("RCBNO"));
					parameters.put("UENNO", (String) ma.get("UENNO"));//imtiuen
					parameters.put("CUSTOMERUENNO", (String) ma.get("CUSTOMERUENNO"));//imtiuen
					parameters.put("PRINTUENNO",  ma.get("PRINTWITHUENNO"));//imthiuen
					parameters.put("PRINTCUSTOMERUENNO",  ma.get("PRINTWITHCUSTOMERUENNO"));//imthiuen
					parameters.put("PRINTWITHSHIPINGADD",  ma.get("PRINTWITHSHIPINGADD"));
					
					// Author: Imthi  Create date: 26-03-2022  Description: Company and address in footer on Jasper
					parameters.put("PRINTWITHCUSTNAMEADRRESS",  ma.get("PRINTWITHCUSTNAMEADRRESS"));
					if (ma.get("PRINTWITHCUSTNAMEADRRESS").equals("1")) {	
						if(CADD1.equals("")) {
							if(ma.get("F2").equals("  ")){
								parameters.put("taxInvoiceTo_BlockAddressFooter",  CADD2);
							}else {
								parameters.put("taxInvoiceTo_BlockAddressFooter", "\n"+ CADD2);
								
							}
						}else {
							if(ma.get("F2").equals("  ")){
								parameters.put("taxInvoiceTo_BlockAddressFooter", CADD1 +" "+ CADD2);
							}else {
								parameters.put("taxInvoiceTo_BlockAddressFooter", "\n"+ CADD1 +" "+ CADD2+"\n");
							}
						}
						
						parameters.put("taxInvoiceTo_CompanyNameFooter", CNAME);
						if(pltCountry.equals("Singapore")) {
							if(CADD3.equals("") && CADD4.equals("")) {
								parameters.put("taxInvoiceTo_RoadAddressFooter", CCOUNTRY+" "+CZIP+"\n");
							}else {
								parameters.put("taxInvoiceTo_RoadAddressFooter", CADD3 + " " + CADD4+" "+CCOUNTRY+" "+CZIP+"\n");
							}
//						parameters.put("taxInvoiceTo_CountryFooter", "\n"+CCOUNTRY+" "+CZIP+"\n");
						}else {
							if(CADD3.equals("") && CADD4.equals("")) {
								parameters.put("taxInvoiceTo_RoadAddressFooter", CCOUNTRY+" "+CZIP+"\n");
							}else {
								parameters.put("taxInvoiceTo_RoadAddressFooter", CADD3 + " " + CADD4+"\n"+CCOUNTRY+" "+CZIP+"\n");
							}
						}
//						parameters.put("taxInvoiceTo_ZIPCodeFooter", CZIP +"\n");
					}else {
						parameters.put("Footer2", (String) ma.get("F2")+"\n");
						parameters.put("taxInvoiceTo_CompanyNameFooter", "");
						parameters.put("taxInvoiceTo_BlockAddressFooter", "");
						parameters.put("taxInvoiceTo_RoadAddressFooter", "");
						parameters.put("taxInvoiceTo_CountryFooter", "");
						parameters.put("taxInvoiceTo_ZIPCodeFooter", "");
					}
					
					
					int PRINTWITHSHIPINGADD = Integer.valueOf((String) ma.get("PRINTWITHSHIPINGADD"));
					if(PRINTWITHSHIPINGADD == 1) {
						 if (SHIPPINGID.equals("0") || SHIPPINGID.equals("NULL") || SHIPPINGID.equals("null") || SHIPPINGID.equals(""))  {
							 SHIPPINGID = sCustCode;
						 }else {
							SHIPPINGID = SHIPPINGID;	
						 }
					// get shipping details from shipping master table
					ArrayList arrShippingDetails = cUtil.getCustomerDetailsForInvoice(INVOICE, PLANT);					
					if (arrShippingDetails.size() > 0) {
						parameters.put("shipToId", SHIPPINGID);
						SHPCUSTOMERNAME = (String) arrShippingDetails.get(1);
						SHPCONTACTNAME = (String) arrShippingDetails.get(28);
						SHPTELEPHONE = (String) arrShippingDetails.get(30);
						SHPHANDPHONE = (String) arrShippingDetails.get(31);
//						SHPFAX = (String) arrShippingDetails.get(5);
//						SHPEMAIL = (String) arrShippingDetails.get(6);
						SHPUNITNO = (String) arrShippingDetails.get(33);
						SHPBUILDING = (String) arrShippingDetails.get(34);
						SHPSTREET = (String) arrShippingDetails.get(35);
						SHPCITY = (String) arrShippingDetails.get(36);
						SHPSTATE = (String) arrShippingDetails.get(37);
						SHPCOUNTRY = (String) arrShippingDetails.get(38);
						SHPPOSTALCODE = (String) arrShippingDetails.get(39);
					}else {
						parameters.put("shipToId", "");
					}
					}else {
						parameters.put("shipToId", "");
					}
					//AUTHOR : imthiyas  
					//DATE : June 28,2021
					//DESC : display transportmode and paymentterms in jasper
					parameters.put("trans", transportmode);
//					parameters.put("payterms", paymentterms);
					parameters.put("PRINTWITHTRANSPORT_MODE", (String) ma.get("PRINTWITHTRANSPORT_MODE"));
					parameters.put("TRANSPORT_MODE", (String) ma.get("TRANSPORT_MODE"));
					parameters.put("TERMSDETAILS", (String) ma.get("TERMSDETAILS"));
					//end
					
					if (ma.get("PCUSREMARKS").equals("1")) {
						parameters.put("SupRemarks", sRemarks);
					} else {
						parameters.put("SupRemarks", "");
					}
					parameters.put("curDisplay", curDisplay);
					if (orderRemarks.length() > 0)
						orderRemarks = (String) ma.get("REMARK3") + " : " + orderRemarks;
					if (orderRemarks3.length() > 0)
						orderRemarks3 = (String) ma.get("REMARK1") + " : " + orderRemarks3;
					parameters.put("orderRemarks", orderRemarks);
					parameters.put("orderRemarks3", orderRemarks3);
//					if (sDeliveryDate.length() > 0)
//						sDeliveryDate = sDeliveryDate;
					parameters.put("DeliveryDt", sDeliveryDate);
					parameters.put("DeliveryDate", (String) ma.get("DELIVERYDATE"));
					parameters.put("lblDeliveryDate", sDeliveryDate);
					parameters.put("DATADELIVERYDATE ", (String) ma.get("DELIVERYDATE"));
					parameters.put("lblExpiryDate", "");//TODO : Check the input
					parameters.put("DATAEXPIRYDATE ", "");//TODO : Check the input
					parameters.put("Employee", (String) ma.get("EMPLOYEE"));
					String empname = new EmployeeDAO().getEmpname(PLANT, sEmpno, "");
					if (ma.get("PRINTEMPLOYEE").equals("1")) {// Author: Azees  Create date: July 10,2021  Description: Employee on Jasper
						parameters.put("Employee", (String) ma.get("EMPLOYEE"));
						parameters.put("EmployeeName", empname);
					} else {
						parameters.put("Employee", "");
						parameters.put("EmployeeName", "");
					}
					//parameters.put("EmployeeName", empname);

					// parameter shipping address
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
					
					//new changes - azees 12/2020 
					parameters.put("ISTAXINCLUSIVE",  dataMap.get("ISTAXINCLUSIVE").toString());
					ORDERDISCOUNTTYPE=dataMap.get("ORDERDISCOUNTTYPE").toString();
					DISCOUNT_TYPE=dataMap.get("DISCOUNT_TYPE").toString();
					ISSHIPPINGTAX=dataMap.get("ISSHIPPINGTAX").toString();
					ISDISCOUNTTAX=dataMap.get("ISDISCOUNTTAX").toString();
					ISORDERDISCOUNTTAX=dataMap.get("ISORDERDISCOUNTTAX").toString();
					PROJECT_NAME=dataMap.get("PROJECT_NAME").toString();
					sales_location=dataMap.get("sales_location").toString();
					DISCOUNT=dataMap.get("DISCOUNT").toString();
					double doubledatabilldiscount = new Double(DISCOUNT);
					parameters.put("DO_DISCOUNT", doubledatabilldiscount);
					parameters.put("ORDERDISCOUNTTYPE", ORDERDISCOUNTTYPE);
					parameters.put("DO_DISCOUNT_TYPE", DISCOUNT_TYPE);
					parameters.put("AdjustmentAmount", dataMap.get("ADJUSTMENT").toString());
					parameters.put("ISDISCOUNTTAX", ISDISCOUNTTAX);
					parameters.put("ISSHIPPINGTAX", ISSHIPPINGTAX);
					parameters.put("ISORDERDISCOUNTTAX", ISORDERDISCOUNTTAX);
					parameters.put("PROJECTNAME", PROJECT_NAME);
					if(ISSHIPPINGTAX.equalsIgnoreCase("0"))
						ISSHIPPINGTAX="Tax Exclusive";
					else
						ISSHIPPINGTAX="Tax Inclusive";
					if(ISDISCOUNTTAX.equalsIgnoreCase("0"))
						ISDISCOUNTTAX="Tax Exclusive";
					else
						ISDISCOUNTTAX="Tax Inclusive";
					if(ISORDERDISCOUNTTAX.equalsIgnoreCase("0"))
						ISORDERDISCOUNTTAX="Tax Exclusive";
					else
						ISORDERDISCOUNTTAX="Tax Inclusive";
					if(DISCOUNT_TYPE.equalsIgnoreCase("%"))
						parameters.put("lblDiscountDO", "Discount ("+ DISCOUNT + "%" +")"+" ("+ISDISCOUNTTAX+")");
					else
						parameters.put("lblDiscountDO", "Discount ("+DISCOUNT_TYPE+")"+" ("+ISDISCOUNTTAX+")");
					TAX_TYPE = dataMap.get("TAX_TYPE").toString();
					/*if(TAX_TYPE.equalsIgnoreCase("EXEMPT") || TAX_TYPE.equalsIgnoreCase("OUT OF SCOPE"))
						TAX_TYPE = StrUtils.fString(TAX_TYPE+" [0.0%]").trim();
					else
						TAX_TYPE = StrUtils.fString(TAX_TYPE+" ["+gstValue+"%]").trim();*/
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
						gstValue="0.0";
					} else if(ptaxiszero == 0 && ptaxisshow == 0){
						TAX_TYPE = "";
						gstValue="0.0";
					} else if(ptaxiszero == 0 && ptaxisshow == 1){						
						if(sales_location.equalsIgnoreCase(""))
							TAX_TYPE = StrUtils.fString(TAX_TYPE+" ["+gstValue+"%]").trim();
						else
							TAX_TYPE = StrUtils.fString(TAX_TYPE+"("+sales_location+") ["+gstValue+"%]").trim();
					} else {							

						if(sales_location.equalsIgnoreCase(""))
							TAX_TYPE = StrUtils.fString(TAX_TYPE+" [0.0%]").trim();
						else
							TAX_TYPE = StrUtils.fString(TAX_TYPE+"("+sales_location+") [0.0%]").trim();
						gstValue="0.0";//Author: Azees  Create date: August 05,2021  Description: zero tax issue 
					}
	  				parameters.put("TAX_TYPE", TAX_TYPE);
					
	  				parameters.put("Adjustment",  ma.get("ADJUSTMENT"));
					parameters.put("PRODUCTRATESARE",  ma.get("PRODUCTRATESARE"));
					parameters.put("PROJECT",  ma.get("PROJECT"));
					parameters.put("ISPROJECT",  ma.get("PRINTWITHPROJECT"));
					
					// parameter shipping address end
	  				if(ORDERDISCOUNTTYPE.equalsIgnoreCase("%"))
						parameters.put("lblOrderDiscount",
								(String) ma.get("ORDERDISCOUNT") + " " + "(" + DATAORDERDISCOUNT + "%" + ") ("+ ISORDERDISCOUNTTAX + ")");
						else
							parameters.put("lblOrderDiscount",
									(String) ma.get("ORDERDISCOUNT") + " " + "(" + ORDERDISCOUNTTYPE + ") ("+ ISORDERDISCOUNTTAX + ")");
					parameters.put("lblShippingCost", (String) ma.get("SHIPPINGCOST") + " " + "(" + curDisplay + ") ("+ ISSHIPPINGTAX + ")");
					parameters.put("lblINCOTERM", (String) ma.get("INCOTERM"));
					parameters.put("lblDeliveryDate", (String) ma.get("DELIVERYDATE"));
					parameters.put("TAXBY", taxby);
					double doubledataorderdiscount = new Double(DATAORDERDISCOUNT);
					double doubledatashippingcost = new Double(DATASHIPPINGCOST);
					parameters.put("DATAORDERDISCOUNT", doubledataorderdiscount);
					parameters.put("DATASHIPPINGCOST", doubledatashippingcost);
					parameters.put("DATAINCOTERMS", DATAINCOTERMS);
					parameters.put("STo", (String) ma.get("SHIPTO"));
					parameters.put("Discount", (String) ma.get("DISCOUNT"));
					parameters.put("NetRate", (String) ma.get("NETRATE"));
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
					//imti start seal,sign condition in printoutconfig
					String PRINTWITHCOMPANYSEAL = (String) ma.get("PRINTWITHCOMPANYSEAL");
					String PRINTWITHCOMPANYSIG= (String) ma.get("PRINTWITHCOMPANYSIG");
					if(PRINTWITHCOMPANYSEAL.equalsIgnoreCase("0")){
		                sealPath = DbBean.COMPANY_LOGO_PATH + "/" + DbBean.NO_LOGO_FILE;
		                }
					if(PRINTWITHCOMPANYSIG.equalsIgnoreCase("0")){
						signPath = DbBean.COMPANY_LOGO_PATH + "/" + DbBean.NO_LOGO_FILE;
		                }
					parameters.put("sealPath", sealPath);
					parameters.put("signPath", signPath);
					
					int PRINTWITHPRD = Integer.valueOf((String) ma.get("PRINTWITHPRODUCT"));
					int PRINTDEFAULT = Integer.valueOf((String) ma.get("ISPRINTDEFAULT"));
					int PRINTWITHPRODUCTREMARKS = Integer.valueOf((String) ma.get("PRINTWITHPRODUCTREMARKS"));
					if(COMP_INDUSTRY.equalsIgnoreCase("Centralised Kitchen")) {
					jasperPath = DbBean.JASPER_INPUT + "/" + "printInvoiceKitchenWithDiscountPortrait";
					if(PRINTWITHSHIPINGADD == 0) {
						jasperPath = DbBean.JASPER_INPUT + "/" + "printInvoiceKitchenWithoutShippingDiscountPortrait";
					}
					}else {
						jasperPath = DbBean.JASPER_INPUT + "/" + "printInvoiceWithDiscountPortrait";
						if(PRINTWITHSHIPINGADD == 0 && PRINTWITHPRD ==1) {
							jasperPath = DbBean.JASPER_INPUT + "/" + "printInvoiceWithoutShippingDiscountPortrait";
						}else if(PRINTWITHSHIPINGADD == 0 && PRINTWITHPRD ==0) {
							jasperPath = DbBean.JASPER_INPUT + "/" + "printInvoiceWithoutProductShippingDiscountPortrait";
						}
					}
					
					parameters.put("PRINTWITHDELIVERYDATE",  ma.get("PRINTWITHDELIVERYDATE"));
					parameters.put("PRODUCTDELIVERYDATE",  ma.get("PRODUCTDELIVERYDATE"));
					parameters.put("PRINTWITHPRODUCTDELIVERYDATE",  ma.get("PRINTWITHPRODUCTDELIVERYDATE"));
					parameters.put("CALCULATETAXWITHSHIPPINGCOST",  ma.get("CALCULATETAXWITHSHIPPINGCOST"));
					parameters.put("CompanyDate", (String) ma.get("COMPANYDATE"));
					parameters.put("CompanyName", (String) ma.get("COMPANYNAME"));
					parameters.put("CompanyStamp", (String) ma.get("COMPANYSTAMP"));
					parameters.put("CompanySig", (String) ma.get("COMPANYSIG"));
					parameters.put("Seller", (String) ma.get("SELLER"));
					parameters.put("SellerSign", (String) ma.get("SELLERSIGNATURE"));
					parameters.put("Buyer", (String) ma.get("BUYER"));
					parameters.put("BuyerSign", (String) ma.get("BUYERSIGNATURE"));
					// End report template parameter

					// String gstValue = sb.getGST("GST",PLANT);
					double gst = new Double(gstValue).doubleValue() / 100;
					parameters.put("Gst", gst);
					parameters.put("orderType", orderType);
					
					Hashtable ht = new Hashtable();
					ht.put("PLANT", PLANT);
					ht.put("INVOICEHDRID", invoiceHdrId);
					//List invoiceDetList = new InvoiceDAO().getConvInvoiceDetByHdrId(ht);//Azees Fix Query Load 09.22
					//if(invoiceDetList.size() > 3)
					int invoiceDetListCount = new InvoiceDAO().Ordercount(PLANT,invoiceHdrId);
					if(PRINTWITHSHIPINGADD == 0 && PRINTWITHPRD ==0  && PRINTWITHPRODUCTREMARKS ==0) {
						if(invoiceDetListCount > 15)
						{ }
						else
						parameters.put(JRParameter.IS_IGNORE_PAGINATION, true);
					}else {
						if(invoiceDetListCount > 3)
						{ }
						else
							parameters.put(JRParameter.IS_IGNORE_PAGINATION, true);
					}

					long start = System.currentTimeMillis();
					System.out.println("**************" + " Start Up Time : " + start + "**********");
					System.out.println("jasperPath : " + jasperPath);
					JasperCompileManager.compileReportToFile(jasperPath + ".jrxml", jasperPath + ".jasper");
					jasperPrintList.add((JasperFillManager.fillReport(jasperPath + ".jasper", parameters, con)));
					jasperPrint = JasperFillManager.fillReport(jasperPath + ".jasper",parameters,con);
					
					//imti added to print store a data in pdf based on print confog page 
					if(PRINTDEFAULT == 1) {
						
						parameters.put("OrderHeader", (String) ma.get("CUSTOMERHEADER"));
						System.out.println("**************" + " Start Up Time : " + start + "**********");
						System.out.println("jasperPath : " + jasperPath);
						JasperCompileManager.compileReportToFile(jasperPath + ".jrxml", jasperPath + ".jasper");
						jasperPrintList.add((JasperFillManager.fillReport(jasperPath + ".jasper", parameters, con)));
						jasperPrint1 = JasperFillManager.fillReport(jasperPath + ".jasper",parameters,con);
					}else if(PRINTDEFAULT == 2) {
						
						parameters.put("OrderHeader", (String) ma.get("CUSTOMERHEADER"));
						System.out.println("**************" + " Start Up Time : " + start + "**********");
						System.out.println("jasperPath : " + jasperPath);
						JasperCompileManager.compileReportToFile(jasperPath + ".jrxml", jasperPath + ".jasper");
						jasperPrintList.add((JasperFillManager.fillReport(jasperPath + ".jasper", parameters, con)));
						jasperPrint1 = JasperFillManager.fillReport(jasperPath + ".jasper",parameters,con);
						
						parameters.put("OrderHeader", (String) ma.get("COLLECTIONHEADER"));
						System.out.println("**************" + " Start Up Time : " + start + "**********");
						System.out.println("jasperPath : " + jasperPath);
						JasperCompileManager.compileReportToFile(jasperPath + ".jrxml", jasperPath + ".jasper");
						jasperPrintList.add((JasperFillManager.fillReport(jasperPath + ".jasper", parameters, con)));
						jasperPrint2 = JasperFillManager.fillReport(jasperPath + ".jasper",parameters,con);
					}
					//imti end
				}

			}
			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

			JRPdfExporter exporter = new JRPdfExporter();
			exporter.setParameter(JRExporterParameter.JASPER_PRINT_LIST, jasperPrintList);
			exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, byteArrayOutputStream);
			exporter.exportReport();

			//imti added 11-01-2023 based on printout config page
			if(printPDF.equalsIgnoreCase("defafult")){
				 OutputStream out = response.getOutputStream();
				 JasperExportManager.exportReportToPdfStream(jasperPrint, out);
				 out.flush();
				 out.close();
			 }else if(printPDF.equalsIgnoreCase("customer")){
				 OutputStream out = response.getOutputStream();
				 JasperExportManager.exportReportToPdfStream(jasperPrint1, out);
				 out.flush();
				 out.close();
			 }else if(printPDF.equalsIgnoreCase("collection")){
				 OutputStream out = response.getOutputStream();
				 JasperExportManager.exportReportToPdfStream(jasperPrint2, out);
				 out.flush();
				 out.close();
			}
			//imti end
			    
			byte[] bytes = byteArrayOutputStream.toByteArray();

			
			
			if (!ajax) {
				if ((String)request.getAttribute("ISAUTOMAIL") == null) {//for mail - Azees 03_2022
				response.addHeader("Content-disposition", "inline;filename=TaxInvoice.pdf");
				response.setContentLength(bytes.length);
				response.getOutputStream().write(bytes);
				response.setContentType("application/pdf");
				response.getOutputStream().flush();
				response.getOutputStream().close();	
				} else {
					try(FileOutputStream fos = new FileOutputStream(DbBean.COMPANY_MAIL_ATTACHMENT_PATH + "/Invoice_" + INVOICE + ".pdf")){
						fos.write(bytes);					
				}
				}
			}else {
				
				try(FileOutputStream fos = new FileOutputStream(DbBean.COMPANY_MAIL_ATTACHMENT_PATH + "/Invoice_" + INVOICE + ".pdf")){
					fos.write(bytes);
				}
				
			}

		} catch (IOException e) {

			e.printStackTrace();

		} finally {
			DbBean.closeConnection(con);
		}
	}
	
	
	public void printBillReport(HttpServletRequest request, HttpServletResponse response)
			throws IOException, Exception {
		Connection con = null;
		boolean ajax = "XMLHttpRequest".equals(request.getHeader("X-Requested-With"));
		try {
			CustUtil cUtil = new CustUtil();
			PlantMstUtil pmUtil = new PlantMstUtil();
			selectBean sb = new selectBean();
			DOUtil _DOUtil = new DOUtil();
			CurrencyUtil currUtil = new CurrencyUtil();
			sb.setmLogger(mLogger);
			cUtil.setmLogger(mLogger);
			pmUtil.setmLogger(mLogger);
			currUtil.setmLogger(mLogger);
			HttpSession session = request.getSession();
			String Plant = (String) session.getAttribute("PLANT");
			List viewlistQry = pmUtil.getPlantMstDetails(Plant);
			Map maps = (Map) viewlistQry.get(0);
//			Map m = null;
			String CNAME = "", CADD1 = "", CADD2 = "", CADD3 = "", CADD4 = "", CZIP = "", CCOUNTRY = "", CSTATE = "",SEALNAME="",SIGNNAME="",
					CTEL = "", CFAX = "", CONTACTNAME = "", CHPNO = "", CEMAIL = "", CRCBNO = "",companyregno = "",CWEBSITE="";
			String SHPCUSTOMERNAME = "", SHPCONTACTNAME = "", SHPTELEPHONE = "", SHPHANDPHONE = "", SHPUNITNO = "", SHPBUILDING = "", SHPSTREET = "", SHPCITY = "", SHPSTATE = "",
					SHPCOUNTRY = "", SHPPOSTALCODE = "", DATAORDERDISCOUNT = "", DATASHIPPINGCOST = "",invoiceHdrId="0",
					DATAINCOTERMS = "",ORDERDISCOUNTTYPE = "",TAX_TYPE="",ISDISCOUNTTAX="",ISSHIPPINGTAX="",ORDERTYPE="",
							ISORDERDISCOUNTTAX = "",DISCOUNT = "",DISCOUNT_TYPE="",PROJECT_NAME="",sales_location="",paymentterms = ""/*, 
							SHPFAX = "", SHPEMAIL = "",AdjustmentAmount=""*/;
			int TAXID=0,TRANSPORTID=0;
			String BILL = null;
			List lstDoNo = new ArrayList();
			if (request.getParameter("lstDoNo") != null) {
				lstDoNo = Arrays.asList(request.getParameter("lstDoNo").split(","));
			}else if (request.getParameter("chkdDoNo") != null) {
				lstDoNo = Arrays.asList(request.getParameterValues("chkdDoNo"));
			} else if (request.getParameter("BILL") != null) {
				lstDoNo = Arrays.asList(request.getParameter("BILL").split(","));
			} else {
				lstDoNo = Arrays.asList(((String)request.getAttribute("BILL")).split(","));
			}
			List jasperPrintList = new ArrayList();
//			String file = "";
			for (int lstDoNoIndex = 0; lstDoNoIndex < lstDoNo.size(); lstDoNoIndex++) {
				BILL = StrUtils.fString(lstDoNo.get(lstDoNoIndex).toString());
				//System.out.println("DONO : " + DONO);
				String PLANT = (String) session.getAttribute("PLANT");
				String PRINTBY = StrUtils.fString(request.getParameter("PRINT"));
				String Brand = StrUtils.fString(request.getParameter("BRAND"));
				String Type = StrUtils.fString(request.getParameter("TYPE"));
				String SHIPPINGID = StrUtils.fString(request.getParameter("SHIPPINGID"));
				String Class = StrUtils.fString(request.getParameter("CLASS"));
				String taxby = _PlantMstDAO.getTaxBy(PLANT);
				String imagePath = DbBean.COMPANY_LOGO_PATH + "/" + PLANT.toLowerCase() + DbBean.LOGO_FILE;
				String imagePath1 = DbBean.COMPANY_LOGO_PATH + "/" + PLANT.toLowerCase() + "Logo1.gif";
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

				String imagePath2, Orientation = "";
				con = DbBean.getConnection();
//				String SysDate = DateUtils.getDate();
				String jasperPath = DbBean.JASPER_INPUT + "/" + "rptInvoice";
				String COMP_INDUSTRY = new PlantMstDAO().getCOMP_INDUSTRY(Plant);//Check Company Industry
				if(COMP_INDUSTRY.equalsIgnoreCase("Centralised Kitchen"))
					jasperPath = DbBean.JASPER_INPUT + "/" + "rptInvoiceKitchen";
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
					companyregno = (String) map.get("companyregnumber");//imtiuen
					CWEBSITE = (String) map.get("WEBSITE");
				}
				ArrayList arrCust = cUtil.getVendorDetailsForBILL(PLANT,BILL);
				//System.out.println("arrCust size : " + arrCust.size());
				if (arrCust.size() > 0) {
//					String sCustCode = (String) arrCust.get(0);
					String sCustName = (String) arrCust.get(1);
					String sAddr1 = (String) arrCust.get(2);
					String sAddr2 = (String) arrCust.get(3);
					String sAddr3 = (String) arrCust.get(4);
					String sCountry = (String) arrCust.get(5);
					String sZip = (String) arrCust.get(6);
//					String sCons = (String) arrCust.get(7);
//					String sCustNameL = (String) arrCust.get(8);
					String sContactName = (String) arrCust.get(8);
//					String sDesgination = (String) arrCust.get(9);
					String sTelNo = (String) arrCust.get(10);
//					String sHpNo = (String) arrCust.get(11);
					String sFax = (String) arrCust.get(13);
					String sEmail = (String) arrCust.get(12);
					String sRemarks = (String) arrCust.get(14);
					String sAddr4 = (String) arrCust.get(15);
					String orderRemarks = (String) arrCust.get(18);
//					String sPayTerms = (String) arrCust.get(19);
//					String sPayDays = (String) arrCust.get(21);
					String sState = (String) arrCust.get(22);
					SHIPPINGID = (String) arrCust.get(23);
					String sRcbno = (String) arrCust.get(24);
					String suenno = (String) arrCust.get(25);//imtiuen
					
					String orderType ="";
					Map parameters = new HashMap();
					parameters.put("numberOfDecimal", Integer.parseInt(numberOfDecimal));
					
					Hashtable htCond = new Hashtable();
					htCond.put("PLANT", PLANT);
					htCond.put("BILL", BILL);
					String query = "ID AS BILLHDRID,BILL_DATE,ISNULL(ORDERTYPE, '') ORDERTYPE,EMPNO,REFERENCE_NUMBER,DUE_DATE,currencyid,isnull(INBOUND_GST,0) as inbound_Gst,"
							+ " ISNULL( " + "BILL_DATE +' '+LEFT(BILL_DATE, 2)+':'+RIGHT(BILL_DATE, 2)" + ",'') as " + "date" + ", "
							+ "CASE WHEN isnull(ORDERDISCOUNTTYPE,'%')='%' THEN isnull(ORDER_DISCOUNT,0) ELSE (isnull(ORDER_DISCOUNT,0)*ISNULL(CURRENCYUSEQT,1)) END AS orderdiscount,"
							+ "CASE WHEN isnull(DISCOUNT_TYPE,'%')='%' THEN isnull(DISCOUNT,0) ELSE (isnull(DISCOUNT,0)*ISNULL(CURRENCYUSEQT,1)) END AS DISCOUNT,"
							+ "isnull(CRBY,'') as CRBY,'' as PAYMENTTYPE,isnull(shippingcost,0)*ISNULL(CURRENCYUSEQT,1) shippingcost,PONO,GRNO,"
							+ "isnull(ORDERDISCOUNTTYPE,'%') ORDERDISCOUNTTYPE,isnull(ITEM_RATES,0) ISTAXINCLUSIVE,ISNULL(TAXID,0) TAXID,"
							+ "ISNULL((select DISTINCT TAXTYPE from FINCOUNTRYTAXTYPE B WHERE B.ID="+PLANT+"_FINBILLHDR.TAXID),'') TAX_TYPE,"
							+ "ISNULL((select DISTINCT PROJECT_NAME from "+PLANT+"_FINPROJECT B WHERE B.ID="+PLANT+"_FINBILLHDR.PROJECTID),'') PROJECT_NAME,"
							+ "ISNULL((select DISTINCT CollectionDate from "+PLANT+"_POHDR P WHERE P.PONO="+PLANT+"_FINBILLHDR.PONO),'') orderdate,"
							+ "ISNULL((select DISTINCT RECVDATE from "+PLANT+"_RECVDET R WHERE R.GRNO="+PLANT+"_FINBILLHDR.GRNO),'') grnodate,"
//							+ "ISNULL((select DISTINCT PREFIX from FINSALESLOCATION B WHERE B.STATE="+PLANT+"_FINBILLHDR.sales_location),'') sales_location,"
							+ "ISNULL(ADJUSTMENT,0)*ISNULL(CURRENCYUSEQT,1) ADJUSTMENT,ISNULL(ISDISCOUNTTAX,0) ISDISCOUNTTAX,ISNULL(ISSHIPPINGTAXABLE,0) ISSHIPPINGTAX,ISNULL(ISORDERDISCOUNTTAX,0) ISORDERDISCOUNTTAX,"
							+ "ISNULL(DISCOUNT_TYPE,'') DISCOUNT_TYPE,ISNULL(TRANSPORTID, '') TRANSPORTID,ISNULL(PAYMENT_TERMS, '') PAYMENT_TERMS";
					ArrayList arraydohdr = new ArrayList();
					arraydohdr = new BillDAO().selectBillHdr(query, htCond);
					Map dataMap = (Map) arraydohdr.get(0);
					String gstValue = dataMap.get("inbound_Gst").toString();
					Double gsts = new Double(gstValue);	
					DATAORDERDISCOUNT = dataMap.get("orderdiscount").toString();
					DATASHIPPINGCOST = dataMap.get("shippingcost").toString();
					ORDERTYPE = dataMap.get("ORDERTYPE").toString();
					invoiceHdrId=dataMap.get("BILLHDRID").toString();
					String sEmpno = dataMap.get("EMPNO").toString();
					String orderdate = dataMap.get("orderdate").toString();
					String grnodate = dataMap.get("grnodate").toString();
					String refno = dataMap.get("REFERENCE_NUMBER").toString();
					String PaymentTerms = dataMap.get("PAYMENT_TERMS").toString();
					String DueDate = dataMap.get("DUE_DATE").toString();
					TRANSPORTID = Integer.valueOf((String)dataMap.get("TRANSPORTID"));
					// Get Currency Display
					Hashtable curHash = new Hashtable();
					curHash.put(IDBConstants.PLANT, PLANT);
					curHash.put(IDBConstants.CURRENCYID, (String) dataMap.get("currencyid"));
					String curDisplay = currUtil.getCurrencyID(curHash, "DISPLAY");
					System.out.println("CurDisplay" + curDisplay);
					if (request.getParameter("with_batch") != null
							&& "yes".equals(request.getParameter("with_batch"))) {
						parameters.put("PrintBatch", "yes");
					}
					parameters.put("orderdates", orderdate);
					parameters.put("paymentterms", PaymentTerms);
					parameters.put("duedate", DueDate);
					parameters.put("referNo", refno);
					// Customer Details
					parameters.put("imagePath", imagePath);
					parameters.put("imagePath1", imagePath1);
					parameters.put("inboundOrderNo", BILL);
					parameters.put("inboundOrderNo", BILL);
					parameters.put("orderRemarks", orderRemarks);
					parameters.put("company", PLANT);
					parameters.put("taxInvoiceTo_CompanyName", sCustName);
					
					
					if(!COMP_INDUSTRY.equalsIgnoreCase("Centralised Kitchen"))
					{
					parameters.put("taxInvoiceTo_BlockAddress", sAddr1 + "  " + sAddr2);
					parameters.put("taxInvoiceTo_RoadAddress", sAddr3 + "  " + sAddr4);
					parameters.put("taxInvoiceTo_Country", sCountry);
					} else {
						String taxInvoiceTo_BlockAddress="";
						if(sAddr2.length()>0)
							taxInvoiceTo_BlockAddress=sAddr2+ "  ";
						parameters.put("taxInvoiceTo_BlockAddress", taxInvoiceTo_BlockAddress + sAddr3);//1.Building+Street
						parameters.put("taxInvoiceTo_RoadAddress", sAddr1);//2.Uint No.
						parameters.put("taxInvoiceTo_Country", sCountry);
					}
					parameters.put("taxInvoiceTo_State", sState);
					parameters.put("taxInvoiceTo_ZIPCode", sZip);
					parameters.put("taxInvoiceTo_AttentionTo", sContactName);
					parameters.put("taxInvoiceTo_CCTO", "");
					parameters.put("To_TelNo", sTelNo);
					parameters.put("To_Fax", sFax);
					parameters.put("To_Email", sEmail);
					parameters.put("sRCBNO", sRcbno);
					parameters.put("sUENNO", suenno);//imtiuen
					parameters.put("fromAddress_UENNO", companyregno);//imtiuen
					
					
					TransportModeDAO transportmodedao = new TransportModeDAO();
					String transportmode = "";
					if(TRANSPORTID > 0){
						transportmode = transportmodedao.getTransportModeById(PLANT, TRANSPORTID);
					}
					paymentterms=dataMap.get("PAYMENT_TERMS").toString();
					parameters.put("trans", transportmode);
					parameters.put("payterms", paymentterms);
					//end
					
					// Company Details
//					parameters.put("fromAddress_CompanyName", CNAME);
//					if(!COMP_INDUSTRY.equalsIgnoreCase("Centralised Kitchen"))
//					{
//					parameters.put("fromAddress_BlockAddress", CADD1 + " " + CADD2);
//					parameters.put("fromAddress_RoadAddress", CADD3 + " " + CADD4);
//					if(CSTATE.equals("")) {
//					parameters.put("fromAddress_Country", CSTATE + "" + CCOUNTRY);
//					}else {
//						parameters.put("fromAddress_Country", CSTATE + "," + CCOUNTRY);
//					}
//					} else {
//						String fromAddress_BlockAddress="";
//						if(CADD2.length()>0)
//						fromAddress_BlockAddress=CADD2 + ",";
//						parameters.put("fromAddress_BlockAddress", fromAddress_BlockAddress + CADD3); //1.Building+Street
//						parameters.put("fromAddress_RoadAddress", CADD1); //Unit No.
//						parameters.put("fromAddress_Country", CCOUNTRY);
//					}
					
					parameters.put("fromAddress_CompanyName", CNAME);
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
					parameters.put("fromAddress_Website", CWEBSITE);
					parameters.put("currentTime", dataMap.get("BILL_DATE").toString());
					parameters.put("taxInvoiceNo", "");
					parameters.put("InvoiceTerms", "");
					//parameters.put("refNo", dataMap.get("jobNum").toString());
//					String rfno = "";
//					if(dataMap.get("PONO").toString().length()>0)
//						rfno ="PO: "+dataMap.get("PONO").toString();
//					if(dataMap.get("GRNO").toString().length()>0)
//						rfno =rfno+",DO: "+dataMap.get("GRNO").toString();
//					parameters.put("refNo", rfno);
					OrderTypeDAO ODAO = new OrderTypeDAO();
					String orderDesc = "";
					String ordertype = orderType;
					
					// report template parameter added on June 22 2011 By Bruhan

//					DOUtil poUtil = new DOUtil();
//					Map ma = poUtil.getDOReceiptInvoiceHdrDetailsDO(PLANT,"Tax Invoice English");	
					BillDAO billDAO = new BillDAO();
					Map ma = billDAO.getBillHeaderDetails(PLANT);	
					
//					if (orderType.equals("Bill header")) {
//						orderDesc = (String) ma.get("HDR1");
//					} else {
//						orderDesc = ODAO.getOrderTypeDesc(PLANT, orderType);
//					}
					
						orderDesc = (String) ma.get("BILLHEADER");
						
					if (ORDERTYPE.equals(""))  {
						ORDERTYPE= orderDesc;
					}else { 
						if (ma.get("DISPLAYBYORDERTYPE").equals("1")) {
							orderDesc = ORDERTYPE;
						}
					}
						
						if (ma.get("DISPLAYBYORDERTYPE").equals("1"))
						parameters.put("OrderHeader", orderDesc);
					else
						parameters.put("OrderHeader", (String) ma.get("BILLHEADER"));
					// parameters.put("OrderHeader", (String) ma.get("HDR1"));
					parameters.put("ToHeader", (String) ma.get("TOHEADER"));
					parameters.put("FromHeader", (String) ma.get("FROMHEADER"));
					parameters.put("SHIPTO", (String) ma.get("SHIPTO"));
					parameters.put("Date", (String) ma.get("BILLDATE"));
					parameters.put("OrderNo", (String) ma.get("BILLNO"));
					parameters.put("RefNo", (String) ma.get("REFNO"));
					parameters.put("duedateHdr", (String) ma.get("DUEDATE"));
					parameters.put("Terms", (String) ma.get("TERMS"));
					parameters.put("PreBy", (String) ma.get("PREPAREDBY"));
					parameters.put("orderdateHdr", (String) ma.get("DATE"));
					parameters.put("PreByUser", dataMap.get("CRBY").toString());
					if (ma.get("PRINTSUPTERMS").equals("1")) {
						//parameters.put("TermsDetails", sPayTerms);
						parameters.put("TermsDetails", dataMap.get("PAYMENTTYPE").toString());
					} else {
						parameters.put("TermsDetails", (String) ma.get("TERMSDETAILS"));
					}
					parameters.put("SoNo", (String) ma.get("SONO"));
					parameters.put("Item", (String) ma.get("ITEM"));
					parameters.put("Description", (String) ma.get("DESCRIPTION"));
					parameters.put("OrderQty", (String) ma.get("BILLQTY"));
					
					parameters.put("UOM", (String) ma.get("UOM"));
					
					parameters.put("Rate", (String) ma.get("RATE"));
					parameters.put("TaxAmount", (String) ma.get("TAX"));
					parameters.put("Amt", (String) ma.get("ITEMAMOUNT"));
					parameters.put("SubTotal", (String) ma.get("SUBTOTAL") + " " + "(" + curDisplay + ")");
					if (taxby.equalsIgnoreCase("BYORDER")) {
						parameters.put("TotalTax", (String) ma.get("TOTALTAX") + " " + "(" + gstValue + "%" + ")");
					} else {
						parameters.put("TotalTax", (String) ma.get("TOTALTAX"));
					}
					parameters.put("Total", (String) ma.get("TOTAL") + " " + "(" + curDisplay + ")");
					
					if(ORDERDISCOUNTTYPE.equalsIgnoreCase("%"))
						parameters.put("lblOrderDiscount",
								(String) ma.get("ORDERDISCOUNT") + " " + "(" + DATAORDERDISCOUNT + "%" + ") ("+ ISDISCOUNTTAX + ")");
						else
							parameters.put("lblOrderDiscount",
									(String) ma.get("ORDERDISCOUNT") + " " + "(" + ORDERDISCOUNTTYPE + ") ("+ ISDISCOUNTTAX + ")");
						parameters.put("lblShippingCost",
								(String) ma.get("SHIPPINGCOST") + " " + "(" + curDisplay + ") ("+ ISSHIPPINGTAX + ")");
					
					String PRINTROUNDOFFTOTALWITHDECIMAL =  (String) ma.get("PRINTROUNDOFFTOTALWITHDECIMAL");
					parameters.put("PrintRoundoffTotalwithDecimal", PRINTROUNDOFFTOTALWITHDECIMAL);
					parameters.put("RoundoffTotalwithDecimal", (String) ma.get("ROUNDOFFTOTALWITHDECIMAL") + " " + "(" + curDisplay + ")");
					
					String rfno = "";
					if(dataMap.get("PONO").toString().length()>0)
						rfno =(String) ma.get("ORDERNO")+" "+dataMap.get("PONO").toString()+",";
					if(dataMap.get("GRNO").toString().length()>0)
						rfno =rfno+(String) ma.get("GRNO")+" "+dataMap.get("GRNO").toString();
					parameters.put("refNo", rfno);
					
//					if(dataMap.get("PONO").toString().length()>0) {
					parameters.put("PonoHdr", (String) ma.get("ORDERNO"));
					parameters.put("orderdateHdr", (String) ma.get("DATE"));
					parameters.put("GinodateHdr", (String) ma.get("GRNDATE"));
					String PONO = dataMap.get("PONO").toString();
					parameters.put("PONO", PONO);
//					}
//					if(dataMap.get("GRNO").toString().length()>0) {
					parameters.put("GrnoHdr", (String) ma.get("GRNO"));
					String GRNO = dataMap.get("GRNO").toString();
					parameters.put("GRNO", GRNO);
					parameters.put("GRNODATE", grnodate);
//					}
					
					parameters.put("Footer1", (String) ma.get("FOOTER1"));
					parameters.put("Footer2", (String) ma.get("FOOTER2"));
					parameters.put("Footer3", (String) ma.get("FOOTER3"));
					parameters.put("Footer4", (String) ma.get("FOOTER4"));
					parameters.put("Footer5", (String) ma.get("FOOTER5"));
					parameters.put("Footer6", (String) ma.get("FOOTER6"));
					parameters.put("Footer7", (String) ma.get("FOOTER7"));
					parameters.put("Footer8", (String) ma.get("FOOTER8"));
					parameters.put("Footer9", (String) ma.get("FOOTER9"));
					parameters.put("PRINTWITHPRODUCT", (String) ma.get("PRINTWITHPRODUCT"));
					parameters.put("PrdXtraDetails", (String) ma.get("PRINTDETAILDESCRIPTION"));
					parameters.put("PRITNWITHHSCODE", (String) ma.get("PRITNWITHHSCODE"));
					parameters.put("PRITNWITHCOO", (String) ma.get("PRITNWITHCOO"));
					parameters.put("PRINTWITHPRODUCTREMARKS", (String) ma.get("PRINTWITHPRODUCTREMARKS"));
					parameters.put("PRINTWITHBRAND", (String) ma.get("PRINTWITHBRAND"));
					parameters.put("SUPPLIERRCBNO", (String) ma.get("SUPPLIERRCBNO"));
					parameters.put("TOTALAFTERDISCOUNT", (String) ma.get("TOTALAFTERDISCOUNT"));
					parameters.put("HSCODE", (String) ma.get("HSCODE"));
					parameters.put("COO", (String) ma.get("COO"));
					parameters.put("RCBNO", (String) ma.get("RCBNO"));
					parameters.put("UENNO", (String) ma.get("UENNO"));//imtiuen
					parameters.put("SUPPLIERUENNO", (String) ma.get("SUPPLIERUENNO"));//imtiuen
					parameters.put("PRINTUENNO",  ma.get("PRINTWITHUENNO"));//imthiuen
					parameters.put("PRINTSUPPLIERUENNO",  ma.get("PRINTWITHSUPPLIERUENNO"));//imthiuen
					parameters.put("PRINTWITHSHIPINGADD",  ma.get("PRINTWITHSHIPINGADD"));
					parameters.put("PAYMENTTERMS",  ma.get("PAYMENTTERMS"));
					
//					int PRINTWITHSHIPINGADD = Integer.valueOf((String) ma.get("PRINTWITHSHIPINGADD"));
//					if(PRINTWITHSHIPINGADD == 1) {
					// get shipping details from shipping master table
					ArrayList arrShippingDetails = cUtil.getCustomerDetailsForBill(PLANT,BILL);					
					ArrayList arrShippingDetailsvend = cUtil.getCustomerDetailsForBillVendmst(PLANT,BILL);					
					if (arrShippingDetails.size() > 0) {
						parameters.put("shipToId", SHIPPINGID);
						SHPCUSTOMERNAME = (String) arrShippingDetails.get(1);
						SHPCONTACTNAME = (String) arrShippingDetails.get(28);
						SHPTELEPHONE = (String) arrShippingDetails.get(31);
						SHPHANDPHONE = (String) arrShippingDetails.get(20);
//						SHPFAX = (String) arrShippingDetails.get(5);
//						SHPEMAIL = (String) arrShippingDetails.get(6);
						SHPUNITNO = (String) arrShippingDetails.get(33);
						SHPBUILDING = (String) arrShippingDetails.get(34);
						SHPSTREET = (String) arrShippingDetails.get(35);
						SHPCITY = (String) arrShippingDetails.get(36);
						SHPSTATE = (String) arrShippingDetails.get(37);
						SHPCOUNTRY = (String) arrShippingDetails.get(38);
						SHPPOSTALCODE = (String) arrShippingDetails.get(39);
						
					}else {
//						parameters.put("shipToId", "");
						if (arrShippingDetailsvend.size() > 0) {
							parameters.put("shipToId", SHIPPINGID);
							SHPCUSTOMERNAME = (String) arrShippingDetailsvend.get(1);
							SHPCONTACTNAME = (String) arrShippingDetailsvend.get(28);
							SHPTELEPHONE = (String) arrShippingDetailsvend.get(31);
							SHPHANDPHONE = (String) arrShippingDetailsvend.get(20);
//							SHPFAX = (String) arrShippingDetails.get(5);
//							SHPEMAIL = (String) arrShippingDetails.get(6);
							SHPUNITNO = (String) arrShippingDetailsvend.get(33);
							SHPBUILDING = (String) arrShippingDetailsvend.get(34);
							SHPSTREET = (String) arrShippingDetailsvend.get(35);
							SHPCITY = (String) arrShippingDetailsvend.get(36);
							SHPSTATE = (String) arrShippingDetailsvend.get(37);
							SHPCOUNTRY = (String) arrShippingDetailsvend.get(38);
							SHPPOSTALCODE = (String) arrShippingDetailsvend.get(39);
						}else {
							parameters.put("shipToId", "");
						}
					}
//					}else {
//						parameters.put("shipToId", "");
//					}
					//AUTHOR : imthiyas  
					//DATE : June 28,2021
					//DESC : display transportmode and paymentterms in jasper
					parameters.put("trans", transportmode);
					parameters.put("payterms", paymentterms);
					parameters.put("PRINTWITHTRANSPORT_MODE", (String) ma.get("PRINTWITHTRANSPORT_MODE"));
					parameters.put("TRANSPORT_MODE", (String) ma.get("TRANSPORT_MODE"));
					parameters.put("TERMSDETAILS", (String) ma.get("TERMSDETAILS"));
					//end
					
//					if (ma.get("PCUSREMARKS").equals("1")) {
						parameters.put("SupRemarks", sRemarks);
//					} else {
						parameters.put("SupRemarks", "");
//					}
					parameters.put("curDisplay", curDisplay);
					if (orderRemarks.length() > 0)
						orderRemarks = (String) ma.get("NOTES") + " : " + orderRemarks;
//					if (orderRemarks3.length() > 0)
//						orderRemarks3 = (String) ma.get("REMARK2") + " : " + orderRemarks3;
					parameters.put("orderRemarks", orderRemarks);
//					parameters.put("orderRemarks3", orderRemarks3);
//					if (sDeliveryDate.length() > 0)
//						sDeliveryDate = sDeliveryDate;
//					parameters.put("DeliveryDt", sDeliveryDate);
					parameters.put("DeliveryDate", (String) ma.get("DELIVERYDATE"));
//					parameters.put("lblDeliveryDate", sDeliveryDate);
					parameters.put("DATADELIVERYDATE ", (String) ma.get("DELIVERYDATE"));
					parameters.put("lblExpiryDate", "");//TODO : Check the input
					parameters.put("DATAEXPIRYDATE ", "");//TODO : Check the input
//					parameters.put("Employee", (String) ma.get("EMPLOYEE"));
//					String empname = new EmployeeDAO().getEmpname(PLANT, sEmpno, "");
//					parameters.put("EmployeeName", empname);
					if (ma.get("PRINTEMPLOYEE").equals("1")) {// Author: Azees  Create date: July 10,2021  Description: Employee on Jasper
						parameters.put("Employee", (String) ma.get("EMPLOYEE"));
						String empname = new EmployeeDAO().getEmpname(PLANT, sEmpno, "");
						parameters.put("EmployeeName", empname);
					} else {
						parameters.put("Employee", "");
						parameters.put("EmployeeName", "");
					}
					// parameter shipping address
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
					
					//new changes - azees 12/2020 
					parameters.put("ISTAXINCLUSIVE",  dataMap.get("ISTAXINCLUSIVE").toString());
					ORDERDISCOUNTTYPE=dataMap.get("ORDERDISCOUNTTYPE").toString();
					DISCOUNT_TYPE=dataMap.get("DISCOUNT_TYPE").toString();
					ISSHIPPINGTAX=dataMap.get("ISSHIPPINGTAX").toString();
					ISDISCOUNTTAX=dataMap.get("ISDISCOUNTTAX").toString();
					ISORDERDISCOUNTTAX=dataMap.get("ISORDERDISCOUNTTAX").toString();
					PROJECT_NAME=dataMap.get("PROJECT_NAME").toString();
					DISCOUNT=dataMap.get("DISCOUNT").toString();
					double doubledatabilldiscount = new Double(DISCOUNT);
					parameters.put("DO_DISCOUNT", doubledatabilldiscount);
					parameters.put("ORDERDISCOUNTTYPE", ORDERDISCOUNTTYPE);
					parameters.put("DO_DISCOUNT_TYPE", DISCOUNT_TYPE);
					parameters.put("AdjustmentAmount", dataMap.get("ADJUSTMENT").toString());
					parameters.put("ISDISCOUNTTAX", ISDISCOUNTTAX);
					parameters.put("ISSHIPPINGTAX", ISSHIPPINGTAX);
					parameters.put("ISORDERDISCOUNTTAX", ISORDERDISCOUNTTAX);
					parameters.put("PROJECTNAME", PROJECT_NAME);
					if(ISSHIPPINGTAX.equalsIgnoreCase("0"))
						ISSHIPPINGTAX="Tax Exclusive";
					else
						ISSHIPPINGTAX="Tax Inclusive";
					if(ISDISCOUNTTAX.equalsIgnoreCase("0"))
						ISDISCOUNTTAX="Tax Exclusive";
					else
						ISDISCOUNTTAX="Tax Inclusive";
					if(ISORDERDISCOUNTTAX.equalsIgnoreCase("0"))
						ISORDERDISCOUNTTAX="Tax Exclusive";
					else
						ISORDERDISCOUNTTAX="Tax Inclusive";
					if(DISCOUNT_TYPE.equalsIgnoreCase("%"))
						parameters.put("lblDiscountDO", "Discount ("+ DISCOUNT + "%" +")"+" ("+ISDISCOUNTTAX+")");
					else
						parameters.put("lblDiscountDO", "Discount ("+DISCOUNT_TYPE+")"+" ("+ISDISCOUNTTAX+")");
					TAX_TYPE = dataMap.get("TAX_TYPE").toString();
					/*if(TAX_TYPE.equalsIgnoreCase("EXEMPT") || TAX_TYPE.equalsIgnoreCase("OUT OF SCOPE"))
						TAX_TYPE = StrUtils.fString(TAX_TYPE+" [0.0%]").trim();
					else
						TAX_TYPE = StrUtils.fString(TAX_TYPE+" ["+gstValue+"%]").trim();*/
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
						gstValue="0.0";
					} else if(ptaxiszero == 0 && ptaxisshow == 0){
						TAX_TYPE = "";
						gstValue="0.0";
					} else if(ptaxiszero == 0 && ptaxisshow == 1){						
						if(sales_location.equalsIgnoreCase(""))
							TAX_TYPE = StrUtils.fString(TAX_TYPE+" ["+gstValue+"%]").trim();
						else
							TAX_TYPE = StrUtils.fString(TAX_TYPE+"("+sales_location+") ["+gstValue+"%]").trim();
					} else {							

						if(sales_location.equalsIgnoreCase(""))
							TAX_TYPE = StrUtils.fString(TAX_TYPE+" [0.0%]").trim();
						else
							TAX_TYPE = StrUtils.fString(TAX_TYPE+"("+sales_location+") [0.0%]").trim();
						gstValue="0.0";//Author: Azees  Create date: August 05,2021  Description: zero tax issue 
					}
	  				parameters.put("TAX_TYPE", TAX_TYPE);
	  				gsts = new Double(gstValue);
	  				gsts = gsts / 100;
					System.out.println("GST : " + gsts);
					parameters.put("taxPercentage", gsts.doubleValue());
					
	  				parameters.put("Adjustment",  ma.get("ADJUSTMENT"));
					parameters.put("PRODUCTRATESARE",  ma.get("PRODUCTRATESARE"));
					parameters.put("PROJECT",  ma.get("PROJECT"));
					parameters.put("ISPROJECT",  ma.get("PRINTWITHPROJECT"));
					
					// parameter shipping address end
	  				if(ORDERDISCOUNTTYPE.equalsIgnoreCase("%"))
						parameters.put("lblOrderDiscount",
								(String) ma.get("ORDERDISCOUNT") + " " + "(" + DATAORDERDISCOUNT + "%" + ") ("+ ISORDERDISCOUNTTAX + ")");
						else
							parameters.put("lblOrderDiscount",
									(String) ma.get("ORDERDISCOUNT") + " " + "(" + ORDERDISCOUNTTYPE + ") ("+ ISORDERDISCOUNTTAX + ")");
					parameters.put("lblShippingCost", (String) ma.get("SHIPPINGCOST") + " " + "(" + curDisplay + ") ("+ ISSHIPPINGTAX + ")");
					parameters.put("lblINCOTERM", (String) ma.get("INCOTERM"));
					parameters.put("lblDeliveryDate", (String) ma.get("DELIVERYDATE"));
					parameters.put("TAXBY", taxby);
					double doubledataorderdiscount = new Double(DATAORDERDISCOUNT);
					double doubledatashippingcost = new Double(DATASHIPPINGCOST);
					parameters.put("DATAORDERDISCOUNT", doubledataorderdiscount);
					parameters.put("DATASHIPPINGCOST", doubledatashippingcost);
					parameters.put("DATAINCOTERMS", DATAINCOTERMS);
					parameters.put("STo", (String) ma.get("SHIPTO"));
					parameters.put("Discount", (String) ma.get("ITEMDISCOUNT"));
					parameters.put("NetRate", (String) ma.get("NETRATE"));
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
					//imti start seal,sign condition in printoutconfig
					String PRINTWITHCOMPANYSEAL = (String) ma.get("PRINTWITHCOMPANYSEAL");
					String PRINTWITHCOMPANYSIG= (String) ma.get("PRINTWITHCOMPANYSIG");
					int printwithshipingadd = Integer.valueOf((String) ma.get("PRINTWITHSHIPINGADD"));
					if(PRINTWITHCOMPANYSEAL.equalsIgnoreCase("0")){
		                sealPath = DbBean.COMPANY_LOGO_PATH + "/" + DbBean.NO_LOGO_FILE;
		                }
					if(PRINTWITHCOMPANYSIG.equalsIgnoreCase("0")){
						signPath = DbBean.COMPANY_LOGO_PATH + "/" + DbBean.NO_LOGO_FILE;
		                }
					parameters.put("sealPath", sealPath);
					parameters.put("signPath", signPath);
					if(COMP_INDUSTRY.equalsIgnoreCase("Centralised Kitchen"))
					{
						if(printwithshipingadd == 1) 
							jasperPath = DbBean.JASPER_INPUT + "/" + "printBillKitchenWithDiscountPortrait";
						else
							jasperPath = DbBean.JASPER_INPUT + "/" + "printBillKitchenWithOutShippingDiscountPortrait";
					}else {
						if(printwithshipingadd == 1) 
							jasperPath = DbBean.JASPER_INPUT + "/" + "printBillWithDiscountPortrait";
						else
							jasperPath = DbBean.JASPER_INPUT + "/" + "printBillWithOutShipingDiscountPortrait";
						
					}
					parameters.put("PRINTWITHDELIVERYDATE",  ma.get("PRINTWITHDELIVERYDATE"));
					parameters.put("PRODUCTDELIVERYDATE",  ma.get("PRODUCTDELIVERYDATE"));
					parameters.put("PRINTWITHPRODUCTDELIVERYDATE",  ma.get("PRINTWITHPRODUCTDELIVERYDATE"));
					parameters.put("CALCULATETAXWITHSHIPPINGCOST",  ma.get("CALCULATETAXWITHSHIPPINGCOST"));
					parameters.put("CompanyDate", (String) ma.get("COMPANYDATE"));
					parameters.put("CompanyName", (String) ma.get("COMPANYNAME"));
					parameters.put("CompanyStamp", (String) ma.get("COMPANYSTAMP"));
					parameters.put("CompanySig", (String) ma.get("COMPANYSIG"));
					parameters.put("Seller", (String) ma.get("SELLER"));
					parameters.put("SellerSign", (String) ma.get("SELLERSIGNATURE"));
					parameters.put("Buyer", (String) ma.get("BUYER"));
					parameters.put("BuyerSign", (String) ma.get("BUYERSIGNATURE"));
					// End report template parameter

					// String gstValue = sb.getGST("GST",PLANT);
					double gst = new Double(gstValue).doubleValue() / 100;
					parameters.put("Gst", gst);
					parameters.put("orderType", orderType);
					
					Hashtable ht = new Hashtable();
					ht.put("PLANT", PLANT);
					ht.put("BILLHDRID", invoiceHdrId);
					List invoiceDetList = new BillDAO().getBillDetByHdrId(ht);
					if(invoiceDetList.size() > 3)
					{ }
					else
						parameters.put(JRParameter.IS_IGNORE_PAGINATION, true);

					long start = System.currentTimeMillis();
					System.out.println("**************" + " Start Up Time : " + start + "**********");
					System.out.println("jasperPath : " + jasperPath);
					JasperCompileManager.compileReportToFile(jasperPath + ".jrxml", jasperPath + ".jasper");
					jasperPrintList.add((JasperFillManager.fillReport(jasperPath + ".jasper", parameters, con)));
				}

			}
			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

			JRPdfExporter exporter = new JRPdfExporter();
			exporter.setParameter(JRExporterParameter.JASPER_PRINT_LIST, jasperPrintList);
			exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, byteArrayOutputStream);
			exporter.exportReport();

			byte[] bytes = byteArrayOutputStream.toByteArray();

			
			
			if (!ajax) {
				response.addHeader("Content-disposition", "inline;filename=Bill.pdf");
				response.setContentLength(bytes.length);
				response.getOutputStream().write(bytes);
				response.setContentType("application/pdf");
				response.getOutputStream().flush();
				response.getOutputStream().close();			
			}else {
				
				try(FileOutputStream fos = new FileOutputStream(DbBean.COMPANY_MAIL_ATTACHMENT_PATH + "/Bill_" + BILL + ".pdf")){
					fos.write(bytes);
				}
				
			}

		} catch (IOException e) {

			e.printStackTrace();

		} finally {
			DbBean.closeConnection(con);
		}
	}
	
	public void viewDOReport(HttpServletRequest request, HttpServletResponse response) throws IOException, Exception {
		Connection con = null;
		boolean ajax = "XMLHttpRequest".equals(request.getHeader("X-Requested-With"));
		try {
			CustUtil cUtil = new CustUtil();
			PlantMstUtil pmUtil = new PlantMstUtil();
			DOUtil _DOUtil = new DOUtil();
//			Map m = null;
			HttpSession session = request.getSession();
			String CNAME = "", CADD1 = "", CADD2 = "", CADD3 = "", CADD4 = "", CZIP = "", CCOUNTRY = "", CTEL = "",SEALNAME="",SIGNNAME="",
					CFAX = "", CONTACTNAME = "", CHPNO = "", CEMAIL = "", CRCBNO = "",companyregno = "", CSTATE = "",CWEBSITE="";
			String DONO = null;
			List lstDoNo = new ArrayList();
			if (request.getParameter("lstDoNo") != null) {
				lstDoNo = Arrays.asList(request.getParameter("lstDoNo").split(","));
			} else if (request.getParameter("chkdDoNo") != null) {
				lstDoNo = Arrays.asList(request.getParameterValues("chkdDoNo"));
			} else if (request.getParameter("DONO") != null) {
				lstDoNo = Arrays.asList(request.getParameter("DONO").split(","));
			} else {
				lstDoNo = Arrays.asList(((String)request.getAttribute("DONO")).split(","));
			}
			String Plant = (String) session.getAttribute("PLANT");
			String COMP_INDUSTRY = new PlantMstDAO().getCOMP_INDUSTRY(Plant);//Check Company Industry
			List listQry = pmUtil.getPlantMstDetails(Plant);//Azees Fix Query Load 09.22
			Map maps = (Map) listQry.get(0);
			List jasperPrintList = new ArrayList();
			String file = "";
			for (int lstDoNoIndex = 0; lstDoNoIndex < lstDoNo.size(); lstDoNoIndex++) {
				DONO = StrUtils.fString(lstDoNo.get(lstDoNoIndex).toString());
				String PLANT = (String) session.getAttribute("PLANT");
				String PRINTBY = StrUtils.fString(request.getParameter("PRINT"));
				String Brand = StrUtils.fString(request.getParameter("BRAND"));
				String Container = StrUtils.fString(request.getParameter("CONTAINER"));
				String Type = StrUtils.fString(request.getParameter("TYPE"));
				String Class = StrUtils.fString(request.getParameter("CLASS"));
				String SHIPPINGID = StrUtils.fString(request.getParameter("SHIPPINGID"));
				String imagePath = DbBean.COMPANY_LOGO_PATH + "/" + PLANT.toLowerCase() + DbBean.LOGO_FILE;
				String imagePath1 = DbBean.COMPANY_LOGO_PATH + "/" + PLANT.toLowerCase() + "Logo1.gif";
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
				String imagePath2, Orientation = "";

				File checkImageFile = new File(imagePath);
				if (!checkImageFile.exists()) {
					imagePath = DbBean.COMPANY_LOGO_PATH + "/" + DbBean.NO_LOGO_FILE;
				}
				File checkImageFile1 = new File(imagePath1);
				if (!checkImageFile1.exists()) {
					imagePath1 = DbBean.COMPANY_LOGO_PATH + "/" + DbBean.NO_LOGO_FILE;
				}

				con = DbBean.getConnection();
//				String SysDate = DateUtils.getDate();
				String jasperPath = DbBean.JASPER_INPUT + "/" + "rptDO";
//				java.util.Date cDt = new java.util.Date(System.currentTimeMillis());
				//ArrayList listQry = pmUtil.getPlantMstDetails(PLANT);//Azees Fix Query Load 09.22
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
					companyregno = (String) map.get("companyregnumber");//imtiuen
					CWEBSITE = (String) map.get("WEBSITE");
				}
				ArrayList arrCust = cUtil.getCustomerDetailsForDO(DONO, PLANT);
				if (arrCust.size() > 0) {

					String sCustCode = (String) arrCust.get(0);
					String sCustName = (String) arrCust.get(1);
					String sAddr1 = (String) arrCust.get(2);
					String sAddr2 = (String) arrCust.get(3);
					String sAddr3 = (String) arrCust.get(4);
					String sCountry = (String) arrCust.get(5);
					String sZip = (String) arrCust.get(6);
//					String sCons = (String) arrCust.get(7);
//					String sCustNameL = (String) arrCust.get(8);
					String sContactName = (String) arrCust.get(9);
//					String sDesgination = (String) arrCust.get(10);
					String sTelNo = (String) arrCust.get(11);
//					String sHpNo = (String) arrCust.get(12);
					String sFax = (String) arrCust.get(13);
					String sEmail = (String) arrCust.get(14);
					String sRemarks = (String) arrCust.get(15);
					String sAddr4 = (String) arrCust.get(16);
					SHIPPINGID = (String) arrCust.get(25);
					String sRcbno = (String) arrCust.get(26);
					String suenno = (String) arrCust.get(27);//imtiuen
					
					ArrayList arrShippingDetails = _masterUtil.getOutboundShippingDetails(DONO, sCustCode, PLANT);
					Map parameters = new HashMap();
					if (request.getParameter("with_batch") != null
							&& "yes".equals(request.getParameter("with_batch"))) {
						parameters.put("PrintBatch", "yes");
					}
					String sShipCustName = "";					

					String orderRemarks = (String) arrCust.get(18);
//					String sPayTerms = (String) arrCust.get(19);
					String orderRemarks3 = (String) arrCust.get(21);
					String sDeliveryDate = (String) arrCust.get(22);
					String sEmpno = (String) arrCust.get(23);
					String sState = (String) arrCust.get(24);

					//String orderType = new DoHdrDAO().getOrderTypeForDO(PLANT, DONO);
					// Customer Details
					parameters.put("imagePath", imagePath);
					parameters.put("imagePath1", imagePath1);
					parameters.put("OrderNo", DONO);
					parameters.put("company", PLANT);
					parameters.put("To_CompanyName", sCustName);
					if(!COMP_INDUSTRY.equalsIgnoreCase("Centralised Kitchen"))
					{
					parameters.put("To_BlockAddress", sAddr1 + "  " + sAddr2);
					parameters.put("To_RoadAddress", sAddr3 + "  " + sAddr4);
					if (sState.equals("")) {
						parameters.put("To_Country", sCountry);
					}else {
						parameters.put("To_Country", sState + "\n" + sCountry);
					}
//					parameters.put("To_Country", sState + "," + sCountry);
					} else {
						String taxInvoiceTo_BlockAddress="";
						if(sAddr2.length()>0)
							taxInvoiceTo_BlockAddress=sAddr2+ "  ";
						parameters.put("To_BlockAddress", taxInvoiceTo_BlockAddress + sAddr3);//1.Building+Street
						parameters.put("To_RoadAddress", sAddr1);//2.Uint No.
						if (sState.equals("")) {
							parameters.put("To_Country", sCountry);
						}else {
							parameters.put("To_Country", sState + "\n" + sCountry);
						}
//						parameters.put("To_Country", sCountry);
					}
					parameters.put("To_ZIPCode", sZip);
					parameters.put("To_AttentionTo", sContactName);
					parameters.put("To_CCTO", "");
					parameters.put("To_TelNo", sTelNo);
					parameters.put("To_Fax", sFax);
					parameters.put("To_Email", sEmail);
					parameters.put("sRCBNO", sRcbno);
					parameters.put("sUENNO", suenno);//imtiuen
					
					////AUTHOR : imthiyas  
					//DATE : June 28,2021
					//DESC : display transportmode and paymentterms in jasper
					DoHdrDAO doHdrDAO= new DoHdrDAO();
					DoHdr doheader = doHdrDAO.getDoHdrById(PLANT, DONO);
					TransportModeDAO transportmodedao = new TransportModeDAO();
					String transportmode = "";
					if(doheader.getTRANSPORTID() > 0){
						transportmode = transportmodedao.getTransportModeById(PLANT, doheader.getTRANSPORTID());
					}
					String paymentterms = "";
					paymentterms = doheader.getPAYMENT_TERMS();
					String orderType = doheader.getORDERTYPE();
					if(orderType.equals(""))
						orderType="OUTBOUND ORDER";
					parameters.put("trans", transportmode);
					parameters.put("payterms", paymentterms);
					//end

					// Company Details
//					parameters.put("fromAddress_CompanyName", CNAME);
//					if(!COMP_INDUSTRY.equalsIgnoreCase("Centralised Kitchen"))
//					{
//					parameters.put("fromAddress_BlockAddress", CADD1 + " " + CADD2);
//					parameters.put("fromAddress_RoadAddress", CADD3 + " " + CADD4);
//					if(CSTATE.equals("")) {
//					parameters.put("fromAddress_Country", CSTATE + "" + CCOUNTRY);
//					}else {
//						parameters.put("fromAddress_Country", CSTATE + "," + CCOUNTRY);
//					}
//					} else {
//						String fromAddress_BlockAddress="";
//						if(CADD2.length()>0)
//							fromAddress_BlockAddress=CADD2 + ",";
//						parameters.put("fromAddress_BlockAddress", fromAddress_BlockAddress + CADD3); //1.Building+Street
//						parameters.put("fromAddress_RoadAddress", CADD1); //Unit No.
//						parameters.put("fromAddress_Country", CCOUNTRY);
//					}
					
					parameters.put("fromAddress_CompanyName", CNAME);
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
					parameters.put("fromAddress_Website", " "+CWEBSITE);
					// Start code by Bruhan for getting Orderdate in report instead currentdate on
					// 25/March/2013
					// parameters.put("currentTime", SysDate);
					parameters.put("currentTime", _DOUtil.getOrderDate(PLANT, DONO));
					// End code by Bruhan for getting Orderdate in report instead currentdate on
					// 25/March/2013
					//parameters.put("refNo", _DOUtil.getJobNum(PLANT, DONO));//Azees Fix Query Load 09.22
					parameters.put("refNo", doheader.getJobNum());
					parameters.put("orderType", orderType);
					parameters.put("InvoiceTerms", "");
					String ordertype = orderType;
					if (ordertype.equalsIgnoreCase("Mobile Enquiry")) {
						ordertype = "Mobile Enquiry";
					} else if (ordertype.equalsIgnoreCase("Mobile Registration")) {
						ordertype = "Mobile Registration";
					} else {
						ordertype = "Outbound Order";
					}
					OrderTypeDAO ODAO = new OrderTypeDAO();
					String orderDesc = "";

					// report template parameter added on June 22 2011 By Bruhan
					DOUtil poUtil = new DOUtil();
					Map ma = poUtil.getDOReceiptHdrDetails(PLANT, ordertype);
					Orientation = (String) ma.get("PrintOrientation");
					// parameters.put("OrderHeader", (String) ma.get("HDR1"));
					if (orderType.equals("OUTBOUND ORDER")) {
						orderDesc = (String) ma.get("HDR1");
					} else {
						orderDesc = ODAO.getOrderTypeDesc(PLANT, orderType);
					}
					if (ma.get("DISPLAYBYORDERTYPE").equals("1"))
						parameters.put("OrderHeader", orderDesc);
					else
						parameters.put("OrderHeader", (String) ma.get("HDR1"));
					parameters.put("ToHeader", (String) ma.get("HDR2"));
					parameters.put("FromHeader", (String) ma.get("HDR3"));
					parameters.put("Date", (String) ma.get("DATE"));
					parameters.put("OrderNoHdr", (String) ma.get("ORDERNO"));
					parameters.put("RefNo", (String) ma.get("REFNO"));
					parameters.put("SoNo", (String) ma.get("SONO"));
					parameters.put("Item", (String) ma.get("ITEM"));
					parameters.put("Description", (String) ma.get("DESCRIPTION"));
					

					parameters.put("OrderQty", (String) ma.get("ORDERQTY"));
					parameters.put("PROJECT",  ma.get("PROJECT"));
					parameters.put("ISPROJECT",  ma.get("PRINTWITHPROJECT"));
					parameters.put("UOM", (String) ma.get("UOM"));
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
					parameters.put("PrdXtraDetails", (String) ma.get("PRINTXTRADETAILS"));
					parameters.put("PRINTWITHINCOTERM", (String) ma.get("PRINTINCOTERM"));
					
					parameters.put("CUSTOMERRCBNO", (String) ma.get("CUSTOMERRCBNO"));
					parameters.put("PRITNWITHHSCODE", (String) ma.get("PRITNWITHHSCODE"));
					parameters.put("PRITNWITHCOO", (String) ma.get("PRITNWITHCOO"));
					String PRINTWITHREMARK1 = (String) ma.get("PRINTWITHREMARK1");
					String PRINTWITHREMARK2 = (String) ma.get("PRINTWITHREMARK2");
					parameters.put("PRINTWITHPRODUCTREMARKS", (String) ma.get("PRINTWITHPRODUCTREMARKS"));
					parameters.put("PRINTWITHBRAND", (String) ma.get("PRINTWITHBRAND"));
					
					
					//AUTHOR : imthiyas  
					//DATE : June 28,2021
					//DESC : display transportmode and paymentterms in jasper
					parameters.put("trans", transportmode);
					parameters.put("payterms", paymentterms);
					parameters.put("PRINTWITHTRANSPORT_MODE", (String) ma.get("PRINTWITHTRANSPORT_MODE"));
					parameters.put("TRANSPORT_MODE", (String) ma.get("TRANSPORT_MODE"));
					parameters.put("TERMSDETAILS", (String) ma.get("TERMSDETAILS"));
					//end
					parameters.put("PRINTWITHSHIPINGADD",  ma.get("PRINTWITHSHIPINGADD"));
					int PRINTWITHSHIPINGADD = Integer.valueOf((String) ma.get("PRINTWITHSHIPINGADD"));
					if(PRINTWITHSHIPINGADD == 1) {
						if (arrShippingDetails.size() > 0) {
							parameters.put("shipToId", sCustCode);
							sShipCustName = (String) arrShippingDetails.get(1);
							String sShipContactName = (String) arrShippingDetails.get(2);
							String sshipTelno = (String) arrShippingDetails.get(3);
							String sShipPhone = (String) arrShippingDetails.get(4);
//							String sShipFax = (String) arrShippingDetails.get(5);
//							String sShipEmail = (String) arrShippingDetails.get(6);
							String sShipAddr1 = (String) arrShippingDetails.get(7);
							String sShipAddr2 = (String) arrShippingDetails.get(8);
							String sShipStreet = (String) arrShippingDetails.get(9);
							String sShipCity = (String) arrShippingDetails.get(10);
							String sShipState = (String) arrShippingDetails.get(11);
							String sShipCountry = (String) arrShippingDetails.get(12);
							String sShipZip = (String) arrShippingDetails.get(13);
							// ship to Address
							/*
							 * if(sShipCustName.length()>0) { parameters.put("STo_CustName", "SHIP TO : "+
							 * "\n"+ "\n"+ sShipCustName);
							 * 
							 * }else{ parameters.put("STo_CustName", sShipCustName); }
							 */
							parameters.put("STo_Addr1", sShipAddr1);
							parameters.put("STo_Addr2", sShipAddr2);
							parameters.put("STo_Addr3", sShipStreet);
							parameters.put("STo_City", sShipCity);
							if(COMP_INDUSTRY.equalsIgnoreCase("Centralised Kitchen"))	
								if (sShipState.equals("")) 
									parameters.put("STo_Country", sShipCountry);
								else 
									parameters.put("STo_Country", sShipState + "\n" +sShipCountry);
							else
								if (sShipState.equals("")) 
									parameters.put("STo_Country", sShipCountry);
								else 
							parameters.put("STo_Country", sShipState + "\n" + sShipCountry);
							System.out.println("STo_Country : " + parameters.get("STo_Country"));
							parameters.put("STo_ZIP", sShipZip);
							if (sshipTelno.length() > 0) {
								parameters.put("STo_Telno", "Tel: " + sshipTelno);
							} else {
								parameters.put("STo_Telno", sshipTelno);
							}
							if (sShipContactName.length() > 0) {
								parameters.put("STo_AttentionTo", "Attn: " + sShipContactName);
							} else {
								parameters.put("STo_AttentionTo", sShipContactName);
							}
							if (sShipPhone.length() > 0) {
								parameters.put("STo_Phone", "HP: " + sShipPhone);
							} else {
								parameters.put("STo_Phone", sShipPhone);
							}
						}else {
							parameters.put("shipToId", "");
						}
					}else {
						parameters.put("shipToId", "");
					}
					parameters.put("HSCODE", (String) ma.get("HSCODE"));
					parameters.put("COO", (String) ma.get("COO"));
					parameters.put("RCBNO", (String) ma.get("RCBNO"));
					parameters.put("UENNO", (String) ma.get("UENNO"));//imtiuen
					parameters.put("CUSTOMERUENNO", (String) ma.get("CUSTOMERUENNO"));//imtiuen
					parameters.put("PRINTUENNO",  ma.get("PRINTWITHUENNO"));//imthiuen
					parameters.put("PRINTCUSTOMERUENNO",  ma.get("PRINTWITHCUSTOMERUENNO"));//imthiuen
					if (ma.get("PCUSREMARKS").equals("1"))
						parameters.put("SupRemarks", sRemarks);
					else
						parameters.put("SupRemarks", "");
					parameters.put("TermsDetails", (String) ma.get("TERMSDETAILS"));
					parameters.put("LocQty", (String) ma.get("PrintLocStock"));
					if (ma.get("PrintLocStock").equals("1")) {
						jasperPath = DbBean.JASPER_INPUT + "/" + "rptDOWithOnHandQty";
					}

					if(PRINTWITHREMARK1.equalsIgnoreCase("1")) {
						if (orderRemarks.length() > 0)
							orderRemarks = (String) ma.get("REMARK1") + " : " + orderRemarks;
						}
						else
							orderRemarks ="";
						if(PRINTWITHREMARK2.equalsIgnoreCase("1")) {
						if (orderRemarks3.length() > 0)
							orderRemarks3 = (String) ma.get("REMARK2") + " : " + orderRemarks3;
						}
						else
							orderRemarks3 ="";
					parameters.put("orderRemarks", orderRemarks);
					parameters.put("orderRemarks3", orderRemarks3);
//					if (sDeliveryDate.length() > 0)
//						sDeliveryDate = sDeliveryDate;
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
					//imti start seal,sign condition in printoutconfig
					String PRINTWITHCOMPANYSEAL = (String) ma.get("PRINTWITHCOMPANYSEAL");
					String PRINTWITHCOMPANYSIG= (String) ma.get("PRINTWITHCOMPANYSIG");
					if(PRINTWITHCOMPANYSEAL.equalsIgnoreCase("0")){
		                sealPath = DbBean.COMPANY_LOGO_PATH + "/" + DbBean.NO_LOGO_FILE;
		                }
					if(PRINTWITHCOMPANYSIG.equalsIgnoreCase("0")){
						signPath = DbBean.COMPANY_LOGO_PATH + "/" + DbBean.NO_LOGO_FILE;
		                }
					parameters.put("sealPath", sealPath);
					parameters.put("signPath", signPath);
					if (Orientation.equals("Portrait")) {
						jasperPath = DbBean.JASPER_INPUT + "/" + "rptDOPortrait";
						if (ma.get("PrintLocStock").equals("1")) {
							jasperPath = DbBean.JASPER_INPUT + "/" + "rptDOWithOnHandQtyPortrait";
						}
					}
					
					parameters.put("PRINTWITHDELIVERYDATE",  ma.get("PRINTWITHDELIVERYDATE"));	
					parameters.put("PRODUCTDELIVERYDATE",  ma.get("PRODUCTDELIVERYDATE"));
					parameters.put("PRINTWITHPRODUCTDELIVERYDATE",  ma.get("PRINTWITHPRODUCTDELIVERYDATE"));
					parameters.put("CompanyDate", (String) ma.get("COMPANYDATE"));
					parameters.put("CompanyName", (String) ma.get("COMPANYNAME"));
					parameters.put("CompanyStamp", (String) ma.get("COMPANYSTAMP"));
					parameters.put("CompanySig", (String) ma.get("COMPANYSIG"));
					// report template parameter added on June 22 2011 end

					long start = System.currentTimeMillis();
					System.out.println("**************" + " Start Up Time : " + start + "**********");
					if (ma.get("PRINTBARCODE").equals("1")) {
						jasperPath = DbBean.JASPER_INPUT + "/" + "rptDOWithBarcode";
						if (Orientation.equals("Portrait")) {
							jasperPath = DbBean.JASPER_INPUT + "/" + "rptDOWithBarcodePortrait";
						}
					}
					if (PRINTBY.equalsIgnoreCase("GROUPBY")) {
						if (Container.equalsIgnoreCase("CONTAINER")) {
							parameters.put("Cond", " order by b.CONTAINER,DOLNNO ");
							parameters.put("Groupby", Container);
						}else if (Brand.equalsIgnoreCase("BRAND")) {
							if (Type.equalsIgnoreCase("TYPE") && Class.equalsIgnoreCase("CLASS")) {
								parameters.put("Cond", " order by b.PRD_BRAND_ID,b.ITEMTYPE,b.PRD_CLS_ID,DOLNNO ");
								parameters.put("Groupby", Brand + "" + Type + "" + Class);
							} else if (Type.equalsIgnoreCase("TYPE")) {
								parameters.put("Cond", " order by b.PRD_BRAND_ID,b.ITEMTYPE,DOLNNO ");
								parameters.put("Groupby", Brand + "" + Type);
							} else if (Class.equalsIgnoreCase("CLASS")) {
								parameters.put("Cond", " order by b.PRD_BRAND_ID,b.PRD_CLS_ID,DOLNNO ");
								parameters.put("Groupby", Brand + "" + Class);
							} else {
								parameters.put("Cond", " order by b.PRD_BRAND_ID,DOLNNO ");
								parameters.put("Groupby", Brand);
							}

						} else if (Type.equalsIgnoreCase("TYPE")) {
							if (Class.equalsIgnoreCase("CLASS")) {
								parameters.put("Cond", " order by b.ITEMTYPE,b.PRD_CLS_ID,DOLNNO ");
								parameters.put("Groupby", Type + "" + Class);
							} else {
								parameters.put("Cond", " order by b.ITEMTYPE,DOLNNO ");
								parameters.put("Groupby", Type);
							}
						} else {
							parameters.put("Cond", " order by b.PRD_CLS_ID,DOLNNO ");
							parameters.put("Groupby", Class);
						}
						/*
						 * if(PRINTBY.equalsIgnoreCase("BRAND")) { parameters.put("Cond",
						 * " order by b.PRD_BRAND_ID,DOLNNO "); } else
						 * if(PRINTBY.equalsIgnoreCase("CLASS")) { parameters.put("Cond",
						 * " order by b.PRD_CLS_ID,DOLNNO "); } else { parameters.put("Cond",
						 * " order by b.ITEMTYPE,DOLNNO "); }
						 */

						jasperPath = DbBean.JASPER_INPUT + "/" + "rptDOByGrouping";
						if (ma.get("PrintLocStock").equals("1")) {
							jasperPath = DbBean.JASPER_INPUT + "/" + "rptDOWithOnHandQtyByGrouping";
						}

						if (ma.get("PRINTBARCODE").equals("1")) {
							jasperPath = DbBean.JASPER_INPUT + "/" + "rptDOBarcodeByGrouping";
						}

						if (Orientation.equals("Portrait")) {
							jasperPath = DbBean.JASPER_INPUT + "/" + "rptDOByGroupingPortrait";
							if (ma.get("PrintLocStock").equals("1")) {
								jasperPath = DbBean.JASPER_INPUT + "/" + "rptDOWithOnHandQtyByGroupingPortrait";
							}

							if (ma.get("PRINTBARCODE").equals("1")) {
								jasperPath = DbBean.JASPER_INPUT + "/" + "rptDOBarcodeByGroupingPortrait";
							}
						}

					}
					if(COMP_INDUSTRY.equalsIgnoreCase("Centralised Kitchen") &&  ma.get("PRINTBARCODE").equals("1") && ma.get("PrintLocStock").equals("1")) {
						jasperPath = DbBean.JASPER_INPUT + "/" + "rptDOWithBarcodePortrait";
					}
					else if(COMP_INDUSTRY.equalsIgnoreCase("Centralised Kitchen") && ma.get("PrintLocStock").equals("1")) {
					jasperPath = DbBean.JASPER_INPUT + "/" + "rptDOWithOnHandQtyPortrait";
					}else if(COMP_INDUSTRY.equalsIgnoreCase("Centralised Kitchen") &&  ma.get("PRINTBARCODE").equals("1")) {
						jasperPath = DbBean.JASPER_INPUT + "/" + "rptDOWithBarcodePortrait";
					}
					else if(COMP_INDUSTRY.equalsIgnoreCase("Centralised Kitchen")) {
						jasperPath = DbBean.JASPER_INPUT + "/" + "rptDOKitchenPortrait";
					}
					Hashtable htCond = new Hashtable();
					htCond.put("PLANT", PLANT);
					htCond.put("DONO", DONO);
					String query = "currencyid,isnull(outbound_Gst,0) as outbound_Gst,isnull(orderdiscount,0) orderdiscount,"
							+ "ISNULL((select DISTINCT PROJECT_NAME from "+PLANT+"_FINPROJECT B WHERE B.ID="+PLANT+"_dohdr.PROJECTID),'') PROJECT_NAME,"
							+ "isnull(shippingcost,0) shippingcost,isnull(CRBY,'') as CRBY," + "isnull(incoterms,'') incoterms";
					ArrayList arraydohdr = new ArrayList();
					arraydohdr = dOUtil.getDoHdrDetails(query, htCond);
					Map dataMap = (Map) arraydohdr.get(0);
//					String gstValue = dataMap.get("outbound_Gst").toString();
					// DATAORDERDISCOUNT = dataMap.get("orderdiscount").toString();
					// DATASHIPPINGCOST = dataMap.get("shippingcost").toString();
					String DATAINCOTERMS = dataMap.get("incoterms").toString();
					String PROJECT_NAME = dataMap.get("PROJECT_NAME").toString();
					parameters.put("DATAINCOTERMS", DATAINCOTERMS);
					parameters.put("PROJECTNAME", PROJECT_NAME);
					parameters.put("PreBy", (String) ma.get("PREPAREDBY"));
					parameters.put("PreByUser", dataMap.get("CRBY").toString());
					
					query = "item,itemdesc,unitprice";
					DoDetDAO dodetdao = new DoDetDAO();						
					ArrayList arraydodet = dodetdao.selectDoDet(query, htCond);
					if(arraydodet.size() > 3)
					{ }
					else
						parameters.put(JRParameter.IS_IGNORE_PAGINATION, true);
					System.out.println("jasperPath : " + jasperPath);
					JasperCompileManager.compileReportToFile(jasperPath + ".jrxml", jasperPath + ".jasper");
					jasperPrintList.add((JasperFillManager.fillReport(jasperPath + ".jasper", parameters, con)));
					file = jasperPath.substring(jasperPath.lastIndexOf("/") + 1);
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
			//response.addHeader("Content-disposition", "attachment;filename=" + file + ".pdf");
			
			
			if (!ajax) {
				//response.addHeader("Content-disposition", "attachment;filename=" + fileName + ".pdf");
				response.addHeader("Content-disposition", "inline;filename=" + file + ".pdf");
				response.setContentLength(bytes.length);
				response.getOutputStream().write(bytes);
				response.setContentType("application/pdf");
				response.getOutputStream().flush();
				response.getOutputStream().close();				
			}else {
//				System.out.println(jasperPath + ".pdf");
				
				try(FileOutputStream fos = new FileOutputStream(DbBean.COMPANY_MAIL_ATTACHMENT_PATH + "/Picking_List_" + DONO + ".pdf")){
					fos.write(bytes);
				}
				
			}

		} catch (IOException e) {

			e.printStackTrace();

		} finally {
			DbBean.closeConnection(con);
		}
	}
	public void viewPendingDOReport(HttpServletRequest request, HttpServletResponse response) throws IOException, Exception {
		Connection con = null;
		boolean ajax = "XMLHttpRequest".equals(request.getHeader("X-Requested-With"));
		try {
			CustUtil cUtil = new CustUtil();
			PlantMstUtil pmUtil = new PlantMstUtil();
			DOUtil _DOUtil = new DOUtil();
//			Map m = null;
			HttpSession session = request.getSession();
			String CNAME = "", CADD1 = "", CADD2 = "", CADD3 = "", CADD4 = "", CZIP = "", CCOUNTRY = "", CTEL = "",SEALNAME="",SIGNNAME="",
					CFAX = "", CONTACTNAME = "", CHPNO = "", CEMAIL = "", CRCBNO = "",companyregno = "", CSTATE = "",CWEBSITE="";
			String DONO = null;
			List lstDoNo = new ArrayList();
			String Plant = (String) session.getAttribute("PLANT");
			String userName = StrUtils.fString((String) request.getSession().getAttribute("LOGIN_USER")).trim();
			Hashtable htd = new Hashtable();
			htd.put(IConstants.PLANT, Plant);
			htd.put("USERID", userName);
			htd.put("STATUS", "PENDING");
				lstDoNo =new DoHdrDAO().getPendingPrintOrder(htd);
				
			String COMP_INDUSTRY = new PlantMstDAO().getCOMP_INDUSTRY(Plant);//Check Company Industry
			List viewlistQry = pmUtil.getPlantMstDetails(Plant);
			Map maps = (Map) viewlistQry.get(0);
			List jasperPrintList = new ArrayList();
			String file = "";
			for (int lstDoNoIndex = 0; lstDoNoIndex < lstDoNo.size(); lstDoNoIndex++) {
				Map mapdo = (Map) lstDoNo.get(lstDoNoIndex);
				DONO = (String) mapdo.get("ORDERNO");
				String PLANT = (String) session.getAttribute("PLANT");
				String PRINTBY = StrUtils.fString(request.getParameter("PRINT"));
				String Brand = StrUtils.fString(request.getParameter("BRAND"));
				String Container = StrUtils.fString(request.getParameter("CONTAINER"));
				String Type = StrUtils.fString(request.getParameter("TYPE"));
				String Class = StrUtils.fString(request.getParameter("CLASS"));
				String SHIPPINGID = StrUtils.fString(request.getParameter("SHIPPINGID"));
				String imagePath = DbBean.COMPANY_LOGO_PATH + "/" + PLANT.toLowerCase() + DbBean.LOGO_FILE;
				String imagePath1 = DbBean.COMPANY_LOGO_PATH + "/" + PLANT.toLowerCase() + "Logo1.gif";
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
				String imagePath2, Orientation = "";
				
				File checkImageFile = new File(imagePath);
				if (!checkImageFile.exists()) {
					imagePath = DbBean.COMPANY_LOGO_PATH + "/" + DbBean.NO_LOGO_FILE;
				}
				File checkImageFile1 = new File(imagePath1);
				if (!checkImageFile1.exists()) {
					imagePath1 = DbBean.COMPANY_LOGO_PATH + "/" + DbBean.NO_LOGO_FILE;
				}
				
				con = DbBean.getConnection();
//				String SysDate = DateUtils.getDate();
				String jasperPath = DbBean.JASPER_INPUT + "/" + "rptDO";
//				java.util.Date cDt = new java.util.Date(System.currentTimeMillis());
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
					companyregno = (String) map.get("companyregnumber");//imtiuen
					CWEBSITE = (String) map.get("WEBSITE");
				}
				ArrayList arrCust = cUtil.getCustomerDetailsForDO(DONO, PLANT);
				if (arrCust.size() > 0) {
					
					String sCustCode = (String) arrCust.get(0);
					String sCustName = (String) arrCust.get(1);
					String sAddr1 = (String) arrCust.get(2);
					String sAddr2 = (String) arrCust.get(3);
					String sAddr3 = (String) arrCust.get(4);
					String sCountry = (String) arrCust.get(5);
					String sZip = (String) arrCust.get(6);
//					String sCons = (String) arrCust.get(7);
//					String sCustNameL = (String) arrCust.get(8);
					String sContactName = (String) arrCust.get(9);
//					String sDesgination = (String) arrCust.get(10);
					String sTelNo = (String) arrCust.get(11);
//					String sHpNo = (String) arrCust.get(12);
					String sFax = (String) arrCust.get(13);
					String sEmail = (String) arrCust.get(14);
					String sRemarks = (String) arrCust.get(15);
					String sAddr4 = (String) arrCust.get(16);
					SHIPPINGID = (String) arrCust.get(25);
					String sRcbno = (String) arrCust.get(26);
					String suenno = (String) arrCust.get(27);//imtiuen
					
					ArrayList arrShippingDetails = _masterUtil.getOutboundShippingDetails(DONO, sCustCode, PLANT);
					Map parameters = new HashMap();
					if (request.getParameter("with_batch") != null
							&& "yes".equals(request.getParameter("with_batch"))) {
						parameters.put("PrintBatch", "yes");
					}
					String sShipCustName = "";					
					
					String orderRemarks = (String) arrCust.get(18);
//					String sPayTerms = (String) arrCust.get(19);
					String orderRemarks3 = (String) arrCust.get(21);
					String sDeliveryDate = (String) arrCust.get(22);
					String sEmpno = (String) arrCust.get(23);
					String sState = (String) arrCust.get(24);
					
					String orderType = new DoHdrDAO().getOrderTypeForDO(PLANT, DONO);
					// Customer Details
					parameters.put("imagePath", imagePath);
					parameters.put("imagePath1", imagePath1);
					parameters.put("OrderNo", DONO);
					parameters.put("company", PLANT);
					parameters.put("To_CompanyName", sCustName);
					if(!COMP_INDUSTRY.equalsIgnoreCase("Centralised Kitchen"))
					{
						parameters.put("To_BlockAddress", sAddr1 + "  " + sAddr2);
						parameters.put("To_RoadAddress", sAddr3 + "  " + sAddr4);
						if (sState.equals("")) {
							parameters.put("To_Country", sCountry);
						}else {
							parameters.put("To_Country", sState + "\n" + sCountry);
						}
//					parameters.put("To_Country", sState + "," + sCountry);
					} else {
						String taxInvoiceTo_BlockAddress="";
						if(sAddr2.length()>0)
							taxInvoiceTo_BlockAddress=sAddr2+ "  ";
						parameters.put("To_BlockAddress", taxInvoiceTo_BlockAddress + sAddr3);//1.Building+Street
						parameters.put("To_RoadAddress", sAddr1);//2.Uint No.
						if (sState.equals("")) {
							parameters.put("To_Country", sCountry);
						}else {
							parameters.put("To_Country", sState + "\n" + sCountry);
						}
//						parameters.put("To_Country", sCountry);
					}
					parameters.put("To_ZIPCode", sZip);
					parameters.put("To_AttentionTo", sContactName);
					parameters.put("To_CCTO", "");
					parameters.put("To_TelNo", sTelNo);
					parameters.put("To_Fax", sFax);
					parameters.put("To_Email", sEmail);
					parameters.put("sRCBNO", sRcbno);
					parameters.put("sUENNO", suenno);//imtiuen
					
					////AUTHOR : imthiyas  
					//DATE : June 28,2021
					//DESC : display transportmode and paymentterms in jasper
					DoHdrDAO doHdrDAO= new DoHdrDAO();
					DoHdr doheader = doHdrDAO.getDoHdrById(PLANT, DONO);
					TransportModeDAO transportmodedao = new TransportModeDAO();
					String transportmode = "";
					if(doheader.getTRANSPORTID() > 0){
						transportmode = transportmodedao.getTransportModeById(PLANT, doheader.getTRANSPORTID());
					}
					String paymentterms = "";
					paymentterms = doheader.getPAYMENT_TERMS();
					parameters.put("trans", transportmode);
					parameters.put("payterms", paymentterms);
					//end
					
					// Company Details
					parameters.put("fromAddress_CompanyName", CNAME);
					if(!COMP_INDUSTRY.equalsIgnoreCase("Centralised Kitchen"))
					{
						parameters.put("fromAddress_BlockAddress", CADD1 + " " + CADD2);
						parameters.put("fromAddress_RoadAddress", CADD3 + " " + CADD4);
						if(CSTATE.equals("")) {
							parameters.put("fromAddress_Country", CSTATE + "" + CCOUNTRY);
						}else {
							parameters.put("fromAddress_Country", CSTATE + "," + CCOUNTRY);
						}
					} else {
						String fromAddress_BlockAddress="";
						if(CADD2.length()>0)
							fromAddress_BlockAddress=CADD2 + ",";
						parameters.put("fromAddress_BlockAddress", fromAddress_BlockAddress + CADD3); //1.Building+Street
						parameters.put("fromAddress_RoadAddress", CADD1); //Unit No.
						parameters.put("fromAddress_Country", CCOUNTRY);
					}
					parameters.put("fromAddress_ZIPCode", CZIP);
					parameters.put("fromAddress_TpNo", CTEL);
					parameters.put("fromAddress_FaxNo", CFAX);
					parameters.put("fromAddress_ContactPersonName", CONTACTNAME);
					parameters.put("fromAddress_ContactPersonMobile", CHPNO);
					parameters.put("fromAddress_ContactPersonEmail", CEMAIL);
					parameters.put("fromAddress_RCBNO", CRCBNO);
					parameters.put("fromAddress_UENNO", companyregno);//imtiuen
					parameters.put("fromAddress_Website", " "+CWEBSITE);
					// Start code by Bruhan for getting Orderdate in report instead currentdate on
					// 25/March/2013
					// parameters.put("currentTime", SysDate);
					parameters.put("currentTime", _DOUtil.getOrderDate(PLANT, DONO));
					// End code by Bruhan for getting Orderdate in report instead currentdate on
					// 25/March/2013
					parameters.put("refNo", _DOUtil.getJobNum(PLANT, DONO));
					parameters.put("orderType", orderType);
					parameters.put("InvoiceTerms", "");
					String ordertype = orderType;
					if (ordertype.equalsIgnoreCase("Mobile Enquiry")) {
						ordertype = "Mobile Enquiry";
					} else if (ordertype.equalsIgnoreCase("Mobile Registration")) {
						ordertype = "Mobile Registration";
					} else {
						ordertype = "Outbound Order";
					}
					OrderTypeDAO ODAO = new OrderTypeDAO();
					String orderDesc = "";
					
					// report template parameter added on June 22 2011 By Bruhan
					DOUtil poUtil = new DOUtil();
					Map ma = poUtil.getDOReceiptHdrDetails(PLANT, ordertype);
					Orientation = (String) ma.get("PrintOrientation");
					// parameters.put("OrderHeader", (String) ma.get("HDR1"));
					if (orderType.equals("OUTBOUND ORDER")) {
						orderDesc = (String) ma.get("HDR1");
					} else {
						orderDesc = ODAO.getOrderTypeDesc(PLANT, orderType);
					}
					if (ma.get("DISPLAYBYORDERTYPE").equals("1"))
						parameters.put("OrderHeader", orderDesc);
					else
						parameters.put("OrderHeader", (String) ma.get("HDR1"));
					parameters.put("ToHeader", (String) ma.get("HDR2"));
					parameters.put("FromHeader", (String) ma.get("HDR3"));
					parameters.put("Date", (String) ma.get("DATE"));
					parameters.put("OrderNoHdr", (String) ma.get("ORDERNO"));
					parameters.put("RefNo", (String) ma.get("REFNO"));
					parameters.put("SoNo", (String) ma.get("SONO"));
					parameters.put("Item", (String) ma.get("ITEM"));
					parameters.put("Description", (String) ma.get("DESCRIPTION"));
					
					
					parameters.put("OrderQty", (String) ma.get("ORDERQTY"));
					parameters.put("PROJECT",  ma.get("PROJECT"));
					parameters.put("ISPROJECT",  ma.get("PRINTWITHPROJECT"));
					parameters.put("UOM", (String) ma.get("UOM"));
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
					parameters.put("PrdXtraDetails", (String) ma.get("PRINTXTRADETAILS"));
					
					parameters.put("CUSTOMERRCBNO", (String) ma.get("CUSTOMERRCBNO"));
					parameters.put("PRITNWITHHSCODE", (String) ma.get("PRITNWITHHSCODE"));
					parameters.put("PRITNWITHCOO", (String) ma.get("PRITNWITHCOO"));
					String PRINTWITHREMARK1 = (String) ma.get("PRINTWITHREMARK1");
					String PRINTWITHREMARK2 = (String) ma.get("PRINTWITHREMARK2");
					parameters.put("PRINTWITHPRODUCTREMARKS", (String) ma.get("PRINTWITHPRODUCTREMARKS"));
					parameters.put("PRINTWITHBRAND", (String) ma.get("PRINTWITHBRAND"));
					
					//AUTHOR : imthiyas  
					//DATE : June 28,2021
					//DESC : display transportmode and paymentterms in jasper
					parameters.put("trans", transportmode);
					parameters.put("payterms", paymentterms);
					parameters.put("PRINTWITHTRANSPORT_MODE", (String) ma.get("PRINTWITHTRANSPORT_MODE"));
					parameters.put("TRANSPORT_MODE", (String) ma.get("TRANSPORT_MODE"));
					parameters.put("TERMSDETAILS", (String) ma.get("TERMSDETAILS"));
					//end
					parameters.put("PRINTWITHSHIPINGADD",  ma.get("PRINTWITHSHIPINGADD"));
					int PRINTWITHSHIPINGADD = Integer.valueOf((String) ma.get("PRINTWITHSHIPINGADD"));
					if(PRINTWITHSHIPINGADD == 1) {
						if (arrShippingDetails.size() > 0) {
							parameters.put("shipToId", sCustCode);
							sShipCustName = (String) arrShippingDetails.get(1);
							String sShipContactName = (String) arrShippingDetails.get(2);
							String sshipTelno = (String) arrShippingDetails.get(3);
							String sShipPhone = (String) arrShippingDetails.get(4);
//							String sShipFax = (String) arrShippingDetails.get(5);
//							String sShipEmail = (String) arrShippingDetails.get(6);
							String sShipAddr1 = (String) arrShippingDetails.get(7);
							String sShipAddr2 = (String) arrShippingDetails.get(8);
							String sShipStreet = (String) arrShippingDetails.get(9);
							String sShipCity = (String) arrShippingDetails.get(10);
							String sShipState = (String) arrShippingDetails.get(11);
							String sShipCountry = (String) arrShippingDetails.get(12);
							String sShipZip = (String) arrShippingDetails.get(13);
							// ship to Address
							/*
							 * if(sShipCustName.length()>0) { parameters.put("STo_CustName", "SHIP TO : "+
							 * "\n"+ "\n"+ sShipCustName);
							 * 
							 * }else{ parameters.put("STo_CustName", sShipCustName); }
							 */
							parameters.put("STo_Addr1", sShipAddr1);
							parameters.put("STo_Addr2", sShipAddr2);
							parameters.put("STo_Addr3", sShipStreet);
							parameters.put("STo_City", sShipCity);
							if(COMP_INDUSTRY.equalsIgnoreCase("Centralised Kitchen"))	
								if (sShipState.equals("")) 
									parameters.put("STo_Country", sShipCountry);
								else 
									parameters.put("STo_Country", sShipState + "\n" +sShipCountry);
							else
								if (sShipState.equals("")) 
									parameters.put("STo_Country", sShipCountry);
								else 
									parameters.put("STo_Country", sShipState + "\n" + sShipCountry);
							System.out.println("STo_Country : " + parameters.get("STo_Country"));
							parameters.put("STo_ZIP", sShipZip);
							if (sshipTelno.length() > 0) {
								parameters.put("STo_Telno", "Tel: " + sshipTelno);
							} else {
								parameters.put("STo_Telno", sshipTelno);
							}
							if (sShipContactName.length() > 0) {
								parameters.put("STo_AttentionTo", "Attn: " + sShipContactName);
							} else {
								parameters.put("STo_AttentionTo", sShipContactName);
							}
							if (sShipPhone.length() > 0) {
								parameters.put("STo_Phone", "HP: " + sShipPhone);
							} else {
								parameters.put("STo_Phone", sShipPhone);
							}
						}else {
							parameters.put("shipToId", "");
						}
					}else {
						parameters.put("shipToId", "");
					}
					parameters.put("HSCODE", (String) ma.get("HSCODE"));
					parameters.put("COO", (String) ma.get("COO"));
					parameters.put("RCBNO", (String) ma.get("RCBNO"));
					parameters.put("UENNO", (String) ma.get("UENNO"));//imtiuen
					parameters.put("CUSTOMERUENNO", (String) ma.get("CUSTOMERUENNO"));//imtiuen
					parameters.put("PRINTUENNO",  ma.get("PRINTWITHUENNO"));//imthiuen
					parameters.put("PRINTCUSTOMERUENNO",  ma.get("PRINTWITHCUSTOMERUENNO"));//imthiuen
					if (ma.get("PCUSREMARKS").equals("1"))
						parameters.put("SupRemarks", sRemarks);
					else
						parameters.put("SupRemarks", "");
					parameters.put("TermsDetails", (String) ma.get("TERMSDETAILS"));
					parameters.put("LocQty", (String) ma.get("PrintLocStock"));
					if (ma.get("PrintLocStock").equals("1")) {
						jasperPath = DbBean.JASPER_INPUT + "/" + "rptDOWithOnHandQty";
					}
					
					if(PRINTWITHREMARK1.equalsIgnoreCase("1")) {
						if (orderRemarks.length() > 0)
							orderRemarks = (String) ma.get("REMARK1") + " : " + orderRemarks;
					}
					else
						orderRemarks ="";
					if(PRINTWITHREMARK2.equalsIgnoreCase("1")) {
						if (orderRemarks3.length() > 0)
							orderRemarks3 = (String) ma.get("REMARK2") + " : " + orderRemarks3;
					}
					else
						orderRemarks3 ="";
					parameters.put("orderRemarks", orderRemarks);
					parameters.put("orderRemarks3", orderRemarks3);
//					if (sDeliveryDate.length() > 0)
//						sDeliveryDate = sDeliveryDate;
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
					//imti start seal,sign condition in printoutconfig
					String PRINTWITHCOMPANYSEAL = (String) ma.get("PRINTWITHCOMPANYSEAL");
					String PRINTWITHCOMPANYSIG= (String) ma.get("PRINTWITHCOMPANYSIG");
					if(PRINTWITHCOMPANYSEAL.equalsIgnoreCase("0")){
						sealPath = DbBean.COMPANY_LOGO_PATH + "/" + DbBean.NO_LOGO_FILE;
					}
					if(PRINTWITHCOMPANYSIG.equalsIgnoreCase("0")){
						signPath = DbBean.COMPANY_LOGO_PATH + "/" + DbBean.NO_LOGO_FILE;
					}
					parameters.put("sealPath", sealPath);
					parameters.put("signPath", signPath);
					if (Orientation.equals("Portrait")) {
						jasperPath = DbBean.JASPER_INPUT + "/" + "rptDOPortrait";
						if (ma.get("PrintLocStock").equals("1")) {
							jasperPath = DbBean.JASPER_INPUT + "/" + "rptDOWithOnHandQtyPortrait";
						}
					}
					
					parameters.put("PRINTWITHDELIVERYDATE",  ma.get("PRINTWITHDELIVERYDATE"));	
					parameters.put("PRODUCTDELIVERYDATE",  ma.get("PRODUCTDELIVERYDATE"));
					parameters.put("PRINTWITHPRODUCTDELIVERYDATE",  ma.get("PRINTWITHPRODUCTDELIVERYDATE"));
					parameters.put("CompanyDate", (String) ma.get("COMPANYDATE"));
					parameters.put("CompanyName", (String) ma.get("COMPANYNAME"));
					parameters.put("CompanyStamp", (String) ma.get("COMPANYSTAMP"));
					parameters.put("CompanySig", (String) ma.get("COMPANYSIG"));
					// report template parameter added on June 22 2011 end
					
					long start = System.currentTimeMillis();
					System.out.println("**************" + " Start Up Time : " + start + "**********");
					if (ma.get("PRINTBARCODE").equals("1")) {
						jasperPath = DbBean.JASPER_INPUT + "/" + "rptDOWithBarcode";
						if (Orientation.equals("Portrait")) {
							jasperPath = DbBean.JASPER_INPUT + "/" + "rptDOWithBarcodePortrait";
						}
					}
					if (PRINTBY.equalsIgnoreCase("GROUPBY")) {
						if (Container.equalsIgnoreCase("CONTAINER")) {
							parameters.put("Cond", " order by b.CONTAINER,DOLNNO ");
							parameters.put("Groupby", Container);
						}else if (Brand.equalsIgnoreCase("BRAND")) {
							if (Type.equalsIgnoreCase("TYPE") && Class.equalsIgnoreCase("CLASS")) {
								parameters.put("Cond", " order by b.PRD_BRAND_ID,b.ITEMTYPE,b.PRD_CLS_ID,DOLNNO ");
								parameters.put("Groupby", Brand + "" + Type + "" + Class);
							} else if (Type.equalsIgnoreCase("TYPE")) {
								parameters.put("Cond", " order by b.PRD_BRAND_ID,b.ITEMTYPE,DOLNNO ");
								parameters.put("Groupby", Brand + "" + Type);
							} else if (Class.equalsIgnoreCase("CLASS")) {
								parameters.put("Cond", " order by b.PRD_BRAND_ID,b.PRD_CLS_ID,DOLNNO ");
								parameters.put("Groupby", Brand + "" + Class);
							} else {
								parameters.put("Cond", " order by b.PRD_BRAND_ID,DOLNNO ");
								parameters.put("Groupby", Brand);
							}
							
						} else if (Type.equalsIgnoreCase("TYPE")) {
							if (Class.equalsIgnoreCase("CLASS")) {
								parameters.put("Cond", " order by b.ITEMTYPE,b.PRD_CLS_ID,DOLNNO ");
								parameters.put("Groupby", Type + "" + Class);
							} else {
								parameters.put("Cond", " order by b.ITEMTYPE,DOLNNO ");
								parameters.put("Groupby", Type);
							}
						} else {
							parameters.put("Cond", " order by b.PRD_CLS_ID,DOLNNO ");
							parameters.put("Groupby", Class);
						}
						/*
						 * if(PRINTBY.equalsIgnoreCase("BRAND")) { parameters.put("Cond",
						 * " order by b.PRD_BRAND_ID,DOLNNO "); } else
						 * if(PRINTBY.equalsIgnoreCase("CLASS")) { parameters.put("Cond",
						 * " order by b.PRD_CLS_ID,DOLNNO "); } else { parameters.put("Cond",
						 * " order by b.ITEMTYPE,DOLNNO "); }
						 */
						
						jasperPath = DbBean.JASPER_INPUT + "/" + "rptDOByGrouping";
						if (ma.get("PrintLocStock").equals("1")) {
							jasperPath = DbBean.JASPER_INPUT + "/" + "rptDOWithOnHandQtyByGrouping";
						}
						
						if (ma.get("PRINTBARCODE").equals("1")) {
							jasperPath = DbBean.JASPER_INPUT + "/" + "rptDOBarcodeByGrouping";
						}
						
						if (Orientation.equals("Portrait")) {
							jasperPath = DbBean.JASPER_INPUT + "/" + "rptDOByGroupingPortrait";
							if (ma.get("PrintLocStock").equals("1")) {
								jasperPath = DbBean.JASPER_INPUT + "/" + "rptDOWithOnHandQtyByGroupingPortrait";
							}
							
							if (ma.get("PRINTBARCODE").equals("1")) {
								jasperPath = DbBean.JASPER_INPUT + "/" + "rptDOBarcodeByGroupingPortrait";
							}
						}
						
					}
					if(COMP_INDUSTRY.equalsIgnoreCase("Centralised Kitchen") &&  ma.get("PRINTBARCODE").equals("1") && ma.get("PrintLocStock").equals("1")) {
						jasperPath = DbBean.JASPER_INPUT + "/" + "rptDOWithBarcodePortrait";
					}
					else if(COMP_INDUSTRY.equalsIgnoreCase("Centralised Kitchen") && ma.get("PrintLocStock").equals("1")) {
						jasperPath = DbBean.JASPER_INPUT + "/" + "rptDOWithOnHandQtyPortrait";
					}else if(COMP_INDUSTRY.equalsIgnoreCase("Centralised Kitchen") &&  ma.get("PRINTBARCODE").equals("1")) {
						jasperPath = DbBean.JASPER_INPUT + "/" + "rptDOWithBarcodePortrait";
					}
					else if(COMP_INDUSTRY.equalsIgnoreCase("Centralised Kitchen")) {
						jasperPath = DbBean.JASPER_INPUT + "/" + "rptDOKitchenPortrait";
					}
					Hashtable htCond = new Hashtable();
					htCond.put("PLANT", PLANT);
					htCond.put("DONO", DONO);
					String query = "currencyid,isnull(outbound_Gst,0) as outbound_Gst,isnull(orderdiscount,0) orderdiscount,"
							+ "ISNULL((select DISTINCT PROJECT_NAME from "+PLANT+"_FINPROJECT B WHERE B.ID="+PLANT+"_dohdr.PROJECTID),'') PROJECT_NAME,"
							+ "isnull(shippingcost,0) shippingcost,isnull(CRBY,'') as CRBY," + "isnull(incoterms,'') incoterms";
					ArrayList arraydohdr = new ArrayList();
					arraydohdr = dOUtil.getDoHdrDetails(query, htCond);
					Map dataMap = (Map) arraydohdr.get(0);
//					String gstValue = dataMap.get("outbound_Gst").toString();
					// DATAORDERDISCOUNT = dataMap.get("orderdiscount").toString();
					// DATASHIPPINGCOST = dataMap.get("shippingcost").toString();
					String DATAINCOTERMS = dataMap.get("incoterms").toString();
					String PROJECT_NAME = dataMap.get("PROJECT_NAME").toString();
					parameters.put("DATAINCOTERMS", DATAINCOTERMS);
					parameters.put("PROJECTNAME", PROJECT_NAME);
					parameters.put("PreBy", (String) ma.get("PREPAREDBY"));
					parameters.put("PreByUser", dataMap.get("CRBY").toString());
					
					query = "item,itemdesc,unitprice";
					DoDetDAO dodetdao = new DoDetDAO();						
					ArrayList arraydodet = dodetdao.selectDoDet(query, htCond);
					if(arraydodet.size() > 3)
					{ }
					else
						parameters.put(JRParameter.IS_IGNORE_PAGINATION, true);
					System.out.println("jasperPath : " + jasperPath);
					JasperCompileManager.compileReportToFile(jasperPath + ".jrxml", jasperPath + ".jasper");
					jasperPrintList.add((JasperFillManager.fillReport(jasperPath + ".jasper", parameters, con)));
					file = jasperPath.substring(jasperPath.lastIndexOf("/") + 1);
					
					//UPDATE PENDING STATUS
					Hashtable htCondition = new Hashtable();
					htCondition.put("PLANT", Plant);
					htCondition.put("ORDERNO", DONO);
					String updateED = "set STATUS='COMPLETED', UPBY='"+userName+"', UPAT='"+DateUtils.getDateTime()+"'";
					boolean	insertFlag =new DoHdrDAO().updatePendingPrintOrder(updateED, htCondition, "");
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
			//response.addHeader("Content-disposition", "attachment;filename=" + file + ".pdf");
			
			
			if (!ajax) {
				//response.addHeader("Content-disposition", "attachment;filename=" + fileName + ".pdf");
				response.addHeader("Content-disposition", "inline;filename=" + file + ".pdf");
				response.setContentLength(bytes.length);
				response.getOutputStream().write(bytes);
				response.setContentType("application/pdf");
				response.getOutputStream().flush();
				response.getOutputStream().close();				
			}else {
//				System.out.println(jasperPath + ".pdf");
				
				try(FileOutputStream fos = new FileOutputStream(DbBean.COMPANY_MAIL_ATTACHMENT_PATH + "/Picking_List_" + DONO + ".pdf")){
					fos.write(bytes);
				}
				
			}
			
		} catch (IOException e) {
			
			e.printStackTrace();
			
		} finally {
			DbBean.closeConnection(con);
		}
	}

//	private boolean DisplayOutGoingIssueData(HttpServletRequest request, HttpServletResponse response)
//			throws ServletException, IOException, Exception {
//		String dono = StrUtils.fString(request.getParameter("DONO"));
//		String fieldDesc = "<tr><td> Please Choose options from the list box shown above</td></tr>";
//		ArrayList al = new ArrayList();
//		Hashtable htCond = new Hashtable();
//		Map m = new HashMap();
//		if (dono.length() > 0) {
//
//			HttpSession session = request.getSession();
//			String plant = StrUtils.fString((String) session.getAttribute("PLANT")).trim();
//			htCond.put("PLANT", plant);
//			htCond.put("DONO", dono);
//
//			String query = "dono,isnull(outbound_Gst,0) as outbound_Gst,custName,custCode,jobNum,custName,personInCharge,contactNum,address,address2,address3,collectionDate,collectionTime,remark1,remark2,isnull(b.name,'') as contactname,isnull(b.telno,'') as telno,isnull(b.hpno,'') as hpno,isnull(b.email,'') email,isnull(b.addr1,'') add1,isnull(b.addr2,'') add2,isnull(b.addr3,'') add3,isnull(b.remarks,'') remarks,isnull(b.addr4,'') add4,isnull(b.country,'') country,isnull(b.zip,'') zip";
//			dOUtil.setmLogger(mLogger);
//			al = dOUtil.getOutGoingDoHdrDetails(query, htCond);
//			if (al.size() > 0) {
//				m = (Map) al.get(0);
//			} else {
//
//			}
//
//		}
//		request.getSession().setAttribute("podetVal", m);
//		request.getSession().setAttribute("dono", dono);
//		request.getSession().setAttribute("RESULT", fieldDesc);
//
//		return true;
//	}

	// Start code by Bruhan for outbound order product export to excel on 25 April
	// 2013
	private HSSFWorkbook writeToExcel(HttpServletRequest request, HttpServletResponse response, HSSFWorkbook wb) {
		String plant = "";
		ArrayList listQry = new ArrayList();
		try {
			HttpSession session = request.getSession();
			plant = session.getAttribute("PLANT").toString();
			String DONO = StrUtils.fString(request.getParameter("DONO")).trim();
//			String CUSTNAME = StrUtils.fString(request.getParameter("CUST_NAME")).trim();
			String DIRTYPE = StrUtils.fString(request.getParameter("DIRTYPE"));
			Hashtable htCond = new Hashtable();
			htCond.put("A.PLANT", plant);
			htCond.put("A.DONO", DONO);
			PlantMstDAO plantMstDAO = new PlantMstDAO();
			String numberOfDecimal = plantMstDAO.getNumberOfDecimal(plant);

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
					String orderdiscount = "",shippingcost="";
					orderdiscount = StrUtils.fString((String) lineArr.get("orderdiscount"));
					shippingcost = StrUtils.fString((String) lineArr.get("shippingcost"));
					float orderdiscountValue ="".equals(orderdiscount) ? 0.0f :  Float.parseFloat(orderdiscount);
					double shippingcostValue ="".equals(shippingcost) ? 0.0d :  Double.parseDouble(shippingcost);
					
					if(orderdiscountValue==0f){
						orderdiscount="0.000";
					}else{
						orderdiscount=orderdiscount.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
					}
					
					shippingcost = StrUtils.addZeroes(shippingcostValue, numberOfDecimal);
					if (collectiondate.length() > 0) {
						collectiondate = collectiondate.substring(3, 5) + "/" + collectiondate.substring(0, 2) + "/"
								+ collectiondate.substring(6);
						
					}

					dataStyle = createDataStyle(wb);
					HSSFRow row = sheet.createRow((short) iCnt + 1);

					HSSFCell cell = row.createCell(k++);
					cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String) lineArr.get("dono"))));
					cell.setCellStyle(dataStyle);

					cell = row.createCell(k++);
					cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String) lineArr.get("dolnno"))));
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

					cell = row.createCell(k++);
					cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String) lineArr.get("remarks"))));
					cell.setCellStyle(dataStyle);

					cell = row.createCell(k++);
					cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String) lineArr.get("remarks2"))));
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
					// cell.setCellType(cell.CELL_TYPE_NUMERIC);
					
					cell.setCellValue(Double.parseDouble(StrUtils.fString(Numbers.toMillionFormat((String) lineArr.get("qtyor"),Integer.valueOf(numberOfDecimal)))));
					cell.setCellStyle(dataStyle);

					cell = row.createCell(k++);
					cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String) lineArr.get("unitmo"))));
					cell.setCellStyle(dataStyle);
					// By Samatha on 20/09/2013
					String ConvertedUnitPrice = new DoHdrDAO().getUnitCostBasedOnCurIDSelected(plant, DONO,
							(String) lineArr.get("dolnno"), (String) lineArr.get("item"));
					double dConvertedUnitPrice ="".equals(ConvertedUnitPrice) ? 0.0d :  Double.parseDouble(ConvertedUnitPrice);
					ConvertedUnitPrice = StrUtils.addZeroes(dConvertedUnitPrice, numberOfDecimal);
					cell = row.createCell(k++);
					HSSFCellStyle numericCellStyle = createDataStyle(wb);
					numericCellStyle.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
					cell.setCellValue(new HSSFRichTextString(StrUtils.fString(Numbers.toMillionFormat(ConvertedUnitPrice,Integer.valueOf(numberOfDecimal)))));
					//cell.setCellValue(Double.parseDouble(StrUtils.fString(ConvertedUnitPrice)));
					cell.setCellStyle(numericCellStyle);

					cell = row.createCell(k++);
					cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String) lineArr.get("currencyid"))));
					cell.setCellStyle(dataStyle);

					cell = row.createCell(k++);
					cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String) lineArr.get("empno"))));
					cell.setCellStyle(dataStyle);

					cell = row.createCell(k++);
					cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String) lineArr.get("itemdesc"))));
					cell.setCellStyle(dataStyle);
					
					cell = row.createCell(k++);
					cell.setCellValue(new HSSFRichTextString(StrUtils.fString(Numbers.toMillionFormat(orderdiscount,Integer.valueOf(numberOfDecimal)))));
					cell.setCellStyle(numericCellStyle);
					
					cell = row.createCell(k++);
					cell.setCellValue(new HSSFRichTextString(StrUtils.fString(Numbers.toMillionFormat(shippingcost,Integer.valueOf(numberOfDecimal)))));
					cell.setCellStyle(numericCellStyle);
							
					cell = row.createCell(k++);
					cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String) lineArr.get("incoterms"))));
					cell.setCellStyle(dataStyle);
					
					cell = row.createCell(k++);
					cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String) lineArr.get("shippingcustomer"))));
					cell.setCellStyle(dataStyle);
					
					cell = row.createCell(k++);
					cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String) lineArr.get("SALES_LOCATION"))));
					cell.setCellStyle(dataStyle);
					
					cell = row.createCell(k++);
					cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String) lineArr.get("TAXTREATMENT"))));
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

			HSSFRow rowhead = sheet.createRow((short) 0);
			HSSFCell cell = rowhead.createCell(k++);
			cell.setCellValue(new HSSFRichTextString("DONO"));
			cell.setCellStyle(styleHeader);

			cell = rowhead.createCell(k++);
			cell.setCellValue(new HSSFRichTextString("DOLNNO"));
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
			cell.setCellValue(new HSSFRichTextString("Customer ID"));
			cell.setCellStyle(styleHeader);
			
			cell = rowhead.createCell(k++);
			cell.setCellValue(new HSSFRichTextString("Payment Type"));
			cell.setCellStyle(styleHeader);

			cell = rowhead.createCell(k++);
			cell.setCellValue(new HSSFRichTextString("Product ID"));
			cell.setCellStyle(styleHeader);

			cell = rowhead.createCell(k++);
			cell.setCellValue(new HSSFRichTextString("Quantity"));
			cell.setCellStyle(styleHeader);

			cell = rowhead.createCell(k++);
			cell.setCellValue(new HSSFRichTextString("Uom"));
			cell.setCellStyle(styleHeader);

			cell = rowhead.createCell(k++);
			cell.setCellValue(new HSSFRichTextString("Unit price"));
			cell.setCellStyle(styleHeader);

			cell = rowhead.createCell(k++);
			cell.setCellValue(new HSSFRichTextString("Currency ID"));
			cell.setCellStyle(styleHeader);

			cell = rowhead.createCell(k++);
			cell.setCellValue(new HSSFRichTextString("Employee ID"));
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
			cell.setCellValue(new HSSFRichTextString("Incoterm"));
			cell.setCellStyle(styleHeader);
			
			cell = rowhead.createCell(k++);
			cell.setCellValue(new HSSFRichTextString("Shipping Customer"));
			cell.setCellStyle(styleHeader);
			
			cell = rowhead.createCell(k++);
			cell.setCellValue(new HSSFRichTextString("Sales Location"));
			cell.setCellStyle(styleHeader);
			
			cell = rowhead.createCell(k++);
			cell.setCellValue(new HSSFRichTextString("Tax Treatment"));
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
			sheet.setColumnWidth(8, 5000);
			sheet.setColumnWidth(9, 10000);
			sheet.setColumnWidth(10, 5000);
			sheet.setColumnWidth(11, 3000);
			sheet.setColumnWidth(12, 5000);
			sheet.setColumnWidth(13, 5000);
			sheet.setColumnWidth(14, 3000);
			sheet.setColumnWidth(15, 4000);
			sheet.setColumnWidth(16, 12000);
			sheet.setColumnWidth(17, 3700);
			sheet.setColumnWidth(18, 3500);
			sheet.setColumnWidth(19, 7000);
			sheet.setColumnWidth(20, 7000);

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		}
		return sheet;
	}

	private HSSFWorkbook writeToExcelVoidSummary(HttpServletRequest request, HttpServletResponse response,
			HSSFWorkbook wb) {
		HTReportUtil movHisUtil = new HTReportUtil();
		ArrayList movQryList = new ArrayList();
		int maxRowsPerSheet = 65535;
		String fdate = "", tdate = "", taxby = "";
		int SheetId =1;
		PlantMstUtil plantmstutil = new PlantMstUtil();
		try {

			
			String PLANT = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
			String baseCurrency = StrUtils.fString((String) request.getSession().getAttribute("BASE_CURRENCY")).trim();
			String ITEMS = StrUtils.fString(request.getParameter("ITEM"));
			String FROM_DATE = StrUtils.fString(request.getParameter("FROM_DATE"));
			String TO_DATE = StrUtils.fString(request.getParameter("TO_DATE"));
			String CUSTOMER = StrUtils.fString(request.getParameter("CUSTOMER"));
			String CUST_NAME = StrUtils.fString(request.getParameter("CUST_NAME"));
			String ORDER = StrUtils.fString(request.getParameter("ORDERNO"));
			String SALES_PERSON = StrUtils.fString(request.getParameter("SALES_MAN"));
			String OUTLETCODE = StrUtils.fString(request.getParameter("OUTLETCODE"));
			String TERMINALCODE = StrUtils.fString(request.getParameter("TERMINALCODE"));
			String PRD_TYPE = StrUtils.fString(request.getParameter("PRD_TYPE_ID"));
			String PRD_BRAND = StrUtils.fString(request.getParameter("PRD_BRAND_ID"));
			String PRD_CLS = StrUtils.fString(request.getParameter("PRD_CLS_ID"));
			
			String ENABLE_POS =new PlantMstDAO().getispos(PLANT);
			String ISTAXREGISTRED =new PlantMstDAO().getistaxregistred(PLANT);

			List viewlistQry = plantmstutil.getPlantMstDetails(PLANT);
		    Map map = (Map) viewlistQry.get(0);
		    
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
			taxby = _PlantMstDAO.getTaxBy(PLANT);
			PlantMstDAO plantMstDAO = new PlantMstDAO();
	        String numberOfDecimal = plantMstDAO.getNumberOfDecimal(PLANT);
	        DecimalFormat decimalFormat = new DecimalFormat("#.#####");
			 decimalFormat.setRoundingMode(java.math.RoundingMode.FLOOR);
	        if(action.equals("ExportExcelPOSreturnSummary"))
	        	movQryList = movHisUtil.getPOSreturnSummary(ht, fdate,tdate,PLANT,CUSTOMER,ORDER,OUTLETCODE,TERMINALCODE,CUST_NAME,SALES_PERSON,ITEMS,PRD_BRAND,PRD_CLS,PRD_TYPE);
	        else
	        	movQryList = movHisUtil.getVoidSummary(ht, fdate,tdate,PLANT,CUSTOMER,ORDER,OUTLETCODE,TERMINALCODE,CUST_NAME,SALES_PERSON,ITEMS,PRD_BRAND,PRD_CLS,PRD_TYPE);
	        
	        if (movQryList.size() > 0) {
				HSSFSheet sheet = null;
				HSSFCellStyle styleHeader = null;
				HSSFCellStyle dataStyle = null;
				HSSFCellStyle dataStyleSpecial = null;
				HSSFCellStyle CompHeader = null;
				dataStyle = createDataStyle(wb);
				dataStyleSpecial = createDataStyle(wb);
				CreationHelper createHelper = wb.getCreationHelper();
				sheet = wb.createSheet("Sheet"+SheetId);
				styleHeader = createStyleHeader(wb);
				CompHeader = createCompStyleHeader(wb);
				sheet = this.createWidthVoidSummary(sheet);
				sheet = this.createHeaderVoidSummary(sheet, styleHeader, PLANT);
				sheet = this.createHeaderCompanyReports(sheet,CompHeader,reportType,PLANT);
				int index = 2;
				float subtotal=0;
				 double gst=0,total=0;
				 float gsttotal=0;
				 float grandtotal=0,gstpercentage=0,prodgstsubtotal1=0;
					 
				 gst=prodgstsubtotal1;
				 for (int iCnt = 0; iCnt < movQryList.size(); iCnt++) {
					 Map lineArr = (Map) movQryList.get(iCnt);
					 int k = 0;
					 int iIndex = iCnt + 1;
					 String dono = (String) lineArr.get("dono");
					 String TOTALValue =  decimalFormat.format(total); 
					 String SUBTOTAL = (String)lineArr.get("subtotal");
					 
					 float totalVal="".equals(TOTALValue) ? 0.0f :  Float.parseFloat(TOTALValue);
					 float subTotalVal="".equals(SUBTOTAL) ? 0.0f :  Float.parseFloat(SUBTOTAL);
					 
					 double tot = Double.parseDouble(TOTALValue);
					 TOTALValue = StrUtils.addZeroes(tot, numberOfDecimal);
					 double subtot = Double.parseDouble(SUBTOTAL);
					 SUBTOTAL = StrUtils.addZeroes(subtot, numberOfDecimal);
					 
					 String item_discounttype = StrUtils.fString((String)lineArr.get("ORDERDISCOUNTTYPE"));
					 String item_discount = StrUtils.fString((String)lineArr.get("ORDERDISCOUNT"));
					 String ISdiscount = StrUtils.fString((String)lineArr.get("ISORDERDISCOUNTTAX"));
					 String discountANDtype = StrUtils.fString((String)lineArr.get("ORDERDISCOUNT"))+"("+StrUtils.fString((String)lineArr.get("ORDERDISCOUNTTYPE"))+")";
						 
					 //discount calculation
					 double discount =0,dDiscount=0;
					 if(item_discounttype.equalsIgnoreCase("%")){
						 dDiscount = Double.parseDouble(item_discount);
						 discount = ((Double.parseDouble(SUBTOTAL))/100)*dDiscount;
					 }else{
						 dDiscount = Double.parseDouble(item_discount);
						 discount =  dDiscount;
					 }
					 
					 //tax calculation
					 double tax =0.0;
					 int taxid = Integer.parseInt(((String)lineArr.get("TAXID").toString()));
					 gstpercentage =  Float.parseFloat(((String) lineArr.get("outbound_gst").toString())) ;
					 FinCountryTaxType fintaxtype = new FinCountryTaxTypeDAO().getCountryTaxTypesByid(taxid);
					 if(taxid != 0){
						 if(fintaxtype.getISZERO() == 0 && fintaxtype.getSHOWTAX() == 1){
							 if(ISdiscount.equalsIgnoreCase("1"))
								 tax = ((Double.parseDouble(SUBTOTAL)-discount)*gstpercentage)/100;
							 else 
								 tax = (Double.parseDouble(SUBTOTAL)*gstpercentage)/100;
						 }else 
							 gstpercentage=Float.parseFloat("0.000");
					 }else 
						 gstpercentage=Float.parseFloat("0.000");
					 
					 //total amount
					 double finaltotal = ((Double.parseDouble(SUBTOTAL))-discount+tax);
					 if(ENABLE_POS.equalsIgnoreCase("1") && ISTAXREGISTRED.equalsIgnoreCase("0"))
						 	finaltotal = ((Double.parseDouble(SUBTOTAL))-discount);
					 
					 HSSFRow row = sheet.createRow(index);

						HSSFCell cell = row.createCell(k++);
						cell.setCellValue(iCnt + 1);
						cell.setCellStyle(dataStyle);

						cell = row.createCell(k++);
						cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String) lineArr.get("CollectionDate"))));
						cell.setCellStyle(dataStyle);
						
						 cell = row.createCell(k++);
						 cell.setCellValue(new HSSFRichTextString(dono));
						 cell.setCellStyle(dataStyle);

						 cell = row.createCell(k++);
						 cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String) lineArr.get("custname"))));
						 cell.setCellStyle(dataStyle);
						 
						 cell = row.createCell(k++);
							cell.setCellValue(SUBTOTAL);
							cell.setCellStyle(dataStyle);
							
							cell = row.createCell(k++);
							cell.setCellValue(StrUtils.addZeroes(discount, numberOfDecimal));
							cell.setCellStyle(dataStyle);
							
							if(ENABLE_POS.equalsIgnoreCase("1") && ISTAXREGISTRED.equalsIgnoreCase("0")) {
							} else {
							cell = row.createCell(k++);
							cell.setCellValue(StrUtils.addZeroes(tax, numberOfDecimal));
							cell.setCellStyle(dataStyle);
							}
							cell = row.createCell(k++);
							cell.setCellValue(StrUtils.addZeroes(finaltotal, numberOfDecimal));
							cell.setCellStyle(dataStyle);
						

						index++;
						if ((index - 2) % maxRowsPerSheet == 0) {
							index = 2;
							SheetId++;
							 sheet = wb.createSheet("Sheet"+SheetId);
							styleHeader = createStyleHeader(wb);
							CompHeader = createCompStyleHeader(wb);
							sheet = this.createWidthVoidSummary(sheet);
							sheet = this.createHeaderVoidSummary(sheet, styleHeader, PLANT);
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
	
	private HSSFWorkbook writeToExcelPOSFOCSummary(HttpServletRequest request, HttpServletResponse response,
			HSSFWorkbook wb) {
		HTReportUtil movHisUtil = new HTReportUtil();
		ArrayList movQryList = new ArrayList();
		int maxRowsPerSheet = 65535;
		String fdate = "", tdate = "", taxby = "";
		int SheetId =1;
		PlantMstUtil plantmstutil = new PlantMstUtil();
		try {
			
			
			String PLANT = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
			String baseCurrency = StrUtils.fString((String) request.getSession().getAttribute("BASE_CURRENCY")).trim();
			String ITEMS = StrUtils.fString(request.getParameter("ITEM"));
			String FROM_DATE = StrUtils.fString(request.getParameter("FROM_DATE"));
			String TO_DATE = StrUtils.fString(request.getParameter("TO_DATE"));
			String CUSTOMER = StrUtils.fString(request.getParameter("CUSTOMER"));
			String CUST_NAME = StrUtils.fString(request.getParameter("CUST_NAME"));
			String ORDER = StrUtils.fString(request.getParameter("ORDERNO"));
			String SALES_PERSON = StrUtils.fString(request.getParameter("SALES_MAN"));
			String OUTLETCODE = StrUtils.fString(request.getParameter("OUTLETCODE"));
			String TERMINALCODE = StrUtils.fString(request.getParameter("TERMINALCODE"));
			String PRD_TYPE = StrUtils.fString(request.getParameter("PRD_TYPE_ID"));
			String PRD_BRAND = StrUtils.fString(request.getParameter("PRD_BRAND_ID"));
			String PRD_CLS = StrUtils.fString(request.getParameter("PRD_CLS_ID"));
			
			List viewlistQry = plantmstutil.getPlantMstDetails(PLANT);
			Map map = (Map) viewlistQry.get(0);
			
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
			taxby = _PlantMstDAO.getTaxBy(PLANT);
			PlantMstDAO plantMstDAO = new PlantMstDAO();
			String numberOfDecimal = plantMstDAO.getNumberOfDecimal(PLANT);
			DecimalFormat decimalFormat = new DecimalFormat("#.#####");
			decimalFormat.setRoundingMode(java.math.RoundingMode.FLOOR);
				movQryList = movHisUtil.getPOSFOCSummary(ht, fdate,tdate,PLANT,CUSTOMER,ORDER,OUTLETCODE,TERMINALCODE,CUST_NAME,SALES_PERSON,ITEMS,PRD_BRAND,PRD_CLS,PRD_TYPE);
			
			if (movQryList.size() > 0) {
				HSSFSheet sheet = null;
				HSSFCellStyle styleHeader = null;
				HSSFCellStyle dataStyle = null;
				HSSFCellStyle dataStyleSpecial = null;
				HSSFCellStyle CompHeader = null;
				dataStyle = createDataStyle(wb);
				dataStyleSpecial = createDataStyle(wb);
				CreationHelper createHelper = wb.getCreationHelper();
				sheet = wb.createSheet("Sheet"+SheetId);
				styleHeader = createStyleHeader(wb);
				CompHeader = createCompStyleHeader(wb);
				sheet = this.createWidthFOCSummary(sheet);
				sheet = this.createHeaderFOCSummary(sheet, styleHeader);
				sheet = this.createHeaderCompanyReports(sheet,CompHeader,reportType,PLANT);
				int index = 2;
				
				for (int iCnt = 0; iCnt < movQryList.size(); iCnt++) {
					Map lineArr = (Map) movQryList.get(iCnt);
					int k = 0;
					int iIndex = iCnt + 1;
					String dono = (String) lineArr.get("dono");
					String QTY = (String)lineArr.get("QTYOR");
					
					double subtot = Double.parseDouble(QTY);
					QTY = StrUtils.addZeroes(subtot, "3");
					
					
					HSSFRow row = sheet.createRow(index);
					
					HSSFCell cell = row.createCell(k++);
					cell.setCellValue(iCnt + 1);
					cell.setCellStyle(dataStyle);
					
					cell = row.createCell(k++);
					cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String) lineArr.get("CollectionDate"))));
					cell.setCellStyle(dataStyle);
					
					cell = row.createCell(k++);
					cell.setCellValue(new HSSFRichTextString(dono));
					cell.setCellStyle(dataStyle);
					
					cell = row.createCell(k++);
					cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String) lineArr.get("custname"))));
					cell.setCellStyle(dataStyle);

					cell = row.createCell(k++);
					cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String) lineArr.get("item"))));
					cell.setCellStyle(dataStyle);
					
					cell = row.createCell(k++);
					cell.setCellValue(QTY);
					cell.setCellStyle(dataStyle);
					
					
					index++;
					if ((index - 2) % maxRowsPerSheet == 0) {
						index = 2;
						SheetId++;
						sheet = wb.createSheet("Sheet"+SheetId);
						styleHeader = createStyleHeader(wb);
						CompHeader = createCompStyleHeader(wb);
						sheet = this.createWidthFOCSummary(sheet);
						sheet = this.createHeaderFOCSummary(sheet, styleHeader);
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
	
	private HSSFWorkbook writeToExcelPOSSummary(HttpServletRequest request, HttpServletResponse response,
			HSSFWorkbook wb) {
		HTReportUtil movHisUtil = new HTReportUtil();
		ArrayList movQryList = new ArrayList();
		int maxRowsPerSheet = 65535;
		String fdate = "", tdate = "", taxby = "";
		int SheetId =1;
		PlantMstUtil plantmstutil = new PlantMstUtil();
		try {
			
			
			String PLANT = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
			String baseCurrency = StrUtils.fString((String) request.getSession().getAttribute("BASE_CURRENCY")).trim();
			String ITEMS = StrUtils.fString(request.getParameter("ITEM"));
			String FROM_DATE = StrUtils.fString(request.getParameter("FROM_DATE"));
			String TO_DATE = StrUtils.fString(request.getParameter("TO_DATE"));
			String CUSTOMER = StrUtils.fString(request.getParameter("CUSTOMER"));
			String CUST_NAME = StrUtils.fString(request.getParameter("CUST_NAME"));
			String ORDER = StrUtils.fString(request.getParameter("ORDERNO"));
			String SALES_PERSON = StrUtils.fString(request.getParameter("SALES_MAN"));
			String OUTLETCODE = StrUtils.fString(request.getParameter("OUTLETCODE"));
			String TERMINALCODE = StrUtils.fString(request.getParameter("TERMINALCODE"));
			String PRD_TYPE = StrUtils.fString(request.getParameter("PRD_TYPE_ID"));
			String PRD_BRAND = StrUtils.fString(request.getParameter("PRD_BRAND_ID"));
			String PRD_CLS = StrUtils.fString(request.getParameter("PRD_CLS_ID"));
			
			String ENABLE_POS =new PlantMstDAO().getispos(PLANT);
			 String ISTAXREGISTRED =new PlantMstDAO().getistaxregistred(PLANT);
			
			List viewlistQry = plantmstutil.getPlantMstDetails(PLANT);
			Map map = (Map) viewlistQry.get(0);
			
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
			taxby = _PlantMstDAO.getTaxBy(PLANT);
			PlantMstDAO plantMstDAO = new PlantMstDAO();
			String numberOfDecimal = plantMstDAO.getNumberOfDecimal(PLANT);
			DecimalFormat decimalFormat = new DecimalFormat("#.#####");
			decimalFormat.setRoundingMode(java.math.RoundingMode.FLOOR);
			
				if(StrUtils.fString(ORDER).length() > 0)  ht.put("ORDERNO",ORDER);
				if(StrUtils.fString(CUSTOMER).length() > 0)  ht.put("CUSTOMERCODE",CUSTOMER);
				if(StrUtils.fString(OUTLETCODE).length() > 0)  ht.put("OUTLET",OUTLETCODE);
				if(StrUtils.fString(TERMINALCODE).length() > 0)  ht.put("TERMINAL",TERMINALCODE);
				if(StrUtils.fString(CUST_NAME).length() > 0)  ht.put("CASHIER",CUST_NAME);
				if(StrUtils.fString(SALES_PERSON).length() > 0)  ht.put("SALESMAN",SALES_PERSON);
				if(StrUtils.fString(ITEMS).length() > 0)  ht.put("ITEM",ITEMS);
				if(StrUtils.fString(PRD_BRAND).length() > 0)  ht.put("PRD_BRAND_ID",PRD_BRAND);
				if(StrUtils.fString(PRD_TYPE).length() > 0)  ht.put("PRD_TYPE_ID",PRD_TYPE);
				if(StrUtils.fString(PRD_CLS).length() > 0)  ht.put("PRD_CLS_ID",PRD_CLS);
				if(StrUtils.fString(PLANT).length() > 0)  ht.put("PLANT",PLANT);
				
				movQryList = movHisUtil.getPosSalesDiscountSummaryByOrderwise(ht, fdate, tdate, "POS", PLANT);
			
			if (movQryList.size() > 0) {
				HSSFSheet sheet = null;
				HSSFCellStyle styleHeader = null;
				HSSFCellStyle dataStyle = null;
				HSSFCellStyle dataStyleSpecial = null;
				HSSFCellStyle CompHeader = null;
				dataStyle = createDataStyle(wb);
				dataStyleSpecial = createDataStyle(wb);
				CreationHelper createHelper = wb.getCreationHelper();
				sheet = wb.createSheet("Sheet"+SheetId);
				styleHeader = createStyleHeader(wb);
				CompHeader = createCompStyleHeader(wb);
				sheet = this.createWidthVoidSummary(sheet);
				sheet = this.createHeaderVoidSummary(sheet, styleHeader, PLANT);
				sheet = this.createHeaderCompanyReports(sheet,CompHeader,reportType,PLANT);
				int index = 2;
				
				for (int iCnt = 0; iCnt < movQryList.size(); iCnt++) {
					Map lineArr = (Map) movQryList.get(iCnt);
					int k = 0;
					int iIndex = iCnt + 1;
					String dono = (String) lineArr.get("dono");
					
					double finaltotal = Double.parseDouble(StrUtils.fString((String) lineArr.get("TOTAL_PRICE")));
					 if(ENABLE_POS.equalsIgnoreCase("1") && ISTAXREGISTRED.equalsIgnoreCase("0"))
						 	finaltotal = ((Double.parseDouble(StrUtils.fString((String) lineArr.get("AMOUNT"))))-Double.parseDouble(
									StrUtils.fString((String) lineArr.get("DISCOUNT_AMOUNT"))));
					
					HSSFRow row = sheet.createRow(index);
					
					HSSFCell cell = row.createCell(k++);
					cell.setCellValue(iCnt + 1);
					cell.setCellStyle(dataStyle);
					
					cell = row.createCell(k++);
					cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("SALESDATE"))));
					cell.setCellStyle(dataStyle);
					
					cell = row.createCell(k++);
					cell.setCellValue(new HSSFRichTextString(dono));
					cell.setCellStyle(dataStyle);
					
					cell = row.createCell(k++);
					cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String) lineArr.get("custname"))));
					cell.setCellStyle(dataStyle);
					
					cell = row.createCell(k++);
					cell.setCellValue(StrUtils.addZeroes(Double.parseDouble(StrUtils.fString((String) lineArr.get("AMOUNT"))), numberOfDecimal));
					cell.setCellStyle(dataStyle);
					
					cell = row.createCell(k++);
					cell.setCellValue(StrUtils.addZeroes(Double.parseDouble(StrUtils.fString((String) lineArr.get("DISCOUNT_AMOUNT"))), numberOfDecimal));
					cell.setCellStyle(dataStyle);
					
					if(ENABLE_POS.equalsIgnoreCase("1") && ISTAXREGISTRED.equalsIgnoreCase("0")) {
					} else {
						cell = row.createCell(k++);
						cell.setCellValue(StrUtils.addZeroes(Double.parseDouble(StrUtils.fString((String) lineArr.get("TAXAMOUNT"))), numberOfDecimal));
						cell.setCellStyle(dataStyle);
					}
					
//					cell = row.createCell(k++);
//					cell.setCellValue(StrUtils.addZeroes(Double.parseDouble(StrUtils.fString((String) lineArr.get("TOTAL_PRICE"))), DbBean.NOOFDECIMALPTSFORWEIGHT));
//					cell.setCellStyle(dataStyle);

					cell = row.createCell(k++);
					cell.setCellValue(StrUtils.addZeroes(finaltotal, numberOfDecimal));
					cell.setCellStyle(dataStyle);
					
					
					index++;
					if ((index - 2) % maxRowsPerSheet == 0) {
						index = 2;
						SheetId++;
						sheet = wb.createSheet("Sheet"+SheetId);
						styleHeader = createStyleHeader(wb);
						CompHeader = createCompStyleHeader(wb);
						sheet = this.createWidthVoidSummary(sheet);
						sheet = this.createHeaderVoidSummary(sheet, styleHeader, PLANT);
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
	private HSSFWorkbook writeToExcelPOSExpensesSummary(HttpServletRequest request, HttpServletResponse response,
			HSSFWorkbook wb) {
		ExpensesUtil movHisUtil = new ExpensesUtil();
		ArrayList movQryList = new ArrayList();
		int maxRowsPerSheet = 65535;
		String fdate = "", tdate = "", taxby = "";
		int SheetId =1;
		PlantMstUtil plantmstutil = new PlantMstUtil();
		try {
			
			
			String PLANT = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
			String baseCurrency = StrUtils.fString((String) request.getSession().getAttribute("BASE_CURRENCY")).trim();
			String ITEMS = StrUtils.fString(request.getParameter("ITEM"));
			String FROM_DATE = StrUtils.fString(request.getParameter("FROM_DATE"));
			String TO_DATE = StrUtils.fString(request.getParameter("TO_DATE"));
			String CUST_NAME = StrUtils.fString(request.getParameter("CUST_NAME"));
			String OUTLETCODE = StrUtils.fString(request.getParameter("OUTLETCODE"));
			String TERMINALCODE = StrUtils.fString(request.getParameter("TERMINALCODE"));
			
			List viewlistQry = plantmstutil.getPlantMstDetails(PLANT);
			Map map = (Map) viewlistQry.get(0);
			
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
			taxby = _PlantMstDAO.getTaxBy(PLANT);
			PlantMstDAO plantMstDAO = new PlantMstDAO();
			String numberOfDecimal = plantMstDAO.getNumberOfDecimal(PLANT);
			DecimalFormat decimalFormat = new DecimalFormat("#.#####");
			decimalFormat.setRoundingMode(java.math.RoundingMode.FLOOR);
			if(StrUtils.fString(PLANT).length() > 0)  ht.put("PLANT",PLANT);
			
			movQryList = movHisUtil.getPOSExpenses(ht, fdate,tdate,PLANT,OUTLETCODE,TERMINALCODE,CUST_NAME);
			
			if (movQryList.size() > 0) {
				HSSFSheet sheet = null;
				HSSFCellStyle styleHeader = null;
				HSSFCellStyle dataStyle = null;
				HSSFCellStyle dataStyleSpecial = null;
				HSSFCellStyle CompHeader = null;
				dataStyle = createDataStyle(wb);
				dataStyleSpecial = createDataStyle(wb);
				CreationHelper createHelper = wb.getCreationHelper();
				sheet = wb.createSheet("Sheet"+SheetId);
				styleHeader = createStyleHeader(wb);
				CompHeader = createCompStyleHeader(wb);
				sheet = this.createWidthPOSExpensesSummary(sheet);
				sheet = this.createHeaderPOSExpensesSummary(sheet, styleHeader);
				sheet = this.createHeaderCompanyReports(sheet,CompHeader,reportType,PLANT);
				int index = 2;
				
				for (int iCnt = 0; iCnt < movQryList.size(); iCnt++) {
					Map lineArr = (Map) movQryList.get(iCnt);
					int k = 0;
					int iIndex = iCnt + 1;
					String dono = (String) lineArr.get("ID");
					
					HSSFRow row = sheet.createRow(index);
					
					HSSFCell cell = row.createCell(k++);
					cell.setCellValue(iCnt + 1);
					cell.setCellStyle(dataStyle);
					
					cell = row.createCell(k++);
					cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("EXPENSES_DATE"))));
					cell.setCellStyle(dataStyle);
					
					cell = row.createCell(k++);
					cell.setCellValue(new HSSFRichTextString(dono));
					cell.setCellStyle(dataStyle);
					
					cell = row.createCell(k++);
					cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String) lineArr.get("EXPENSES_ACCOUNT"))));
					cell.setCellStyle(dataStyle);
					
					cell = row.createCell(k++);
					cell.setCellValue(StrUtils.addZeroes(Double.parseDouble(StrUtils.fString((String) lineArr.get("AMOUNT"))), DbBean.NOOFDECIMALPTSFORWEIGHT));
					cell.setCellStyle(dataStyle);
					
					index++;
					if ((index - 2) % maxRowsPerSheet == 0) {
						index = 2;
						SheetId++;
						sheet = wb.createSheet("Sheet"+SheetId);
						styleHeader = createStyleHeader(wb);
						CompHeader = createCompStyleHeader(wb);
						sheet = this.createWidthPOSExpensesSummary(sheet);
						sheet = this.createHeaderPOSExpensesSummary(sheet, styleHeader);
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
	// ---Added by Bruhan on May 21 2014, Description:To open outbound order summary
	// in excel powershell format
	private HSSFWorkbook writeToExcelOutboundOrderSummary(HttpServletRequest request, HttpServletResponse response,
			HSSFWorkbook wb) {
		HTReportUtil movHisUtil = new HTReportUtil();
		ArrayList movQryList = new ArrayList();
		int maxRowsPerSheet = 65535;
		String fdate = "", tdate = "", taxby = "";
		int SheetId =1;
		PlantMstUtil plantmstutil = new PlantMstUtil();
		try {

			
			String PLANT = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
			String baseCurrency = StrUtils.fString((String) request.getSession().getAttribute("BASE_CURRENCY")).trim();
			String ITEMNO = StrUtils.fString(request.getParameter("ITEM"));
			String PRD_DESCRIP = StrUtils.fString(request.getParameter("DESC"));
			String FROM_DATE = StrUtils.fString(request.getParameter("FROM_DATE"));
			String TO_DATE = StrUtils.fString(request.getParameter("TO_DATE"));
			String CUSTNAME = StrUtils.fString(request.getParameter("CUSTOMER"));
			String CUSTOMERID = StrUtils.fString(request.getParameter("CUSTOMERID"));
			String ORDERNO = StrUtils.fString(request.getParameter("ORDERNO"));
			String JOBNO = StrUtils.fString(request.getParameter("JOBNO"));
			String PICKSTATUS = StrUtils.fString(request.getParameter("PICKSTATUS"));
			String ISSUESTATUS = StrUtils.fString(request.getParameter("ISSUESTATUS"));
			String ORDERTYPE = StrUtils.fString(request.getParameter("ORDERTYPE"));
			String PRD_TYPE_ID = StrUtils.fString(request.getParameter("PRD_TYPE_ID"));
			String PRD_BRAND_ID = StrUtils.fString(request.getParameter("PRD_BRAND_ID"));
			String PRD_DEPT_ID = StrUtils.fString(request.getParameter("PRD_DEPT_ID"));
			String PRD_CLS_ID = StrUtils.fString(request.getParameter("PRD_CLS_ID"));
			String statusID = StrUtils.fString(request.getParameter("STATUS_ID"));
			String type = StrUtils.fString(request.getParameter("DIRTYPE"));
			String SORT = StrUtils.fString(request.getParameter("SORT"));
			String EMPNO = StrUtils.fString(request.getParameter("EMP_NAME"));
			String CUSTOMERTYPE = StrUtils.fString(request.getParameter("CUSTOMER_TYPE_ID"));
			String CURRENCYID = StrUtils.fString(request.getParameter("CURRENCYID"));
			String CURRENCYDISPLAY = StrUtils.fString(request.getParameter("CURRENCYDISPLAY"));
			String INVOICENO = StrUtils.fString(request.getParameter("INVOICENO"));
			String UOM = StrUtils.fString(request.getParameter("UOM"));
			String sort  = StrUtils.fString(request.getParameter("SORT"));
			String LOC     = StrUtils.fString(request.getParameter("LOC"));
			String LOC_TYPE_ID = StrUtils.fString(request.getParameter("LOC_TYPE_ID"));
			String LOC_TYPE_ID2 = StrUtils.fString(request.getParameter("LOC_TYPE_ID2"));
			String LOC_TYPE_ID3 = StrUtils.fString(request.getParameter("LOC_TYPE_ID3"));
			String POSSEARCH = StrUtils.fString(request.getParameter("POSSEARCH"));
			List viewlistQry = plantmstutil.getPlantMstDetails(PLANT);
		    Map map = (Map) viewlistQry.get(0);
		    String DELIVERYAPP = StrUtils.fString((String)map.get("ISRIDERRAPP"));
			
			//imti start
			if(PICKSTATUS.equalsIgnoreCase("OPEN"))
				PICKSTATUS="N";
			else if(PICKSTATUS.equalsIgnoreCase("PARTIALLY PICKED"))
				PICKSTATUS="O";
			else if(PICKSTATUS.equalsIgnoreCase("PICKED"))
				PICKSTATUS="C";
			//imti end
			
			//imti start
//			if(ISSUESTATUS.equalsIgnoreCase("OPEN"))
//				ISSUESTATUS="N";
//			else if(ISSUESTATUS.equalsIgnoreCase("PARTIALLY ISSUED"))
//				ISSUESTATUS="O";
//			else if(ISSUESTATUS.equalsIgnoreCase("ISSUED"))
//				ISSUESTATUS="C";
			
			if(DELIVERYAPP.equals("1")){
				if(ISSUESTATUS.equalsIgnoreCase("OPEN"))
					ISSUESTATUS="N";
				else if(ISSUESTATUS.equalsIgnoreCase("PARTIALLY ISSUED"))
					ISSUESTATUS="O";
				else if(ISSUESTATUS.equalsIgnoreCase("ISSUED"))
					ISSUESTATUS="C";
				else if(ISSUESTATUS.equalsIgnoreCase("SHIPPED"))
					ISSUESTATUS="S";
				else if(ISSUESTATUS.equalsIgnoreCase("DELIVERED"))
					ISSUESTATUS="D";
			}else{
				if(ISSUESTATUS.equalsIgnoreCase("OPEN"))
					ISSUESTATUS="N";
				else if(ISSUESTATUS.equalsIgnoreCase("PARTIALLY ISSUED"))
					ISSUESTATUS="O";
				else if(ISSUESTATUS.equalsIgnoreCase("SHIPPED"))
					ISSUESTATUS="C";
				else if(ISSUESTATUS.equalsIgnoreCase("DELIVERED"))
					ISSUESTATUS="DELIVERED";
				else if(ISSUESTATUS.equalsIgnoreCase("INTRANSIT"))
					ISSUESTATUS="INTRANSIT";
		 		}
			
			
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
			taxby = _PlantMstDAO.getTaxBy(PLANT);
			PlantMstDAO plantMstDAO = new PlantMstDAO();
	        String numberOfDecimal = plantMstDAO.getNumberOfDecimal(PLANT);
			if (type.equals("OUTBOUND")) {

				if (StrUtils.fString(JOBNO).length() > 0)
					ht.put("A.JOBNUM", JOBNO);
				if (StrUtils.fString(ITEMNO).length() > 0)
					ht.put("B.ITEM", ITEMNO);
				if (StrUtils.fString(ORDERNO).length() > 0)
					ht.put("B.DONO", ORDERNO);
				if (StrUtils.fString(CUSTOMERID).length() > 0)
					ht.put("A.CUSTCODE", CUSTOMERID);
				if (StrUtils.fString(ORDERTYPE).length() > 0)
					ht.put("A.ORDERTYPE", ORDERTYPE);
				if (StrUtils.fString(ISSUESTATUS).length() > 0) {
					if(ISSUESTATUS.equalsIgnoreCase("DRAFT"))
		        	{
		        	ht.put("ORDER_STATUS","Draft");
		        	ht.put("B.LNSTAT","N");
		        	}
		        	else
		        	{
		        		ht.put("B.LNSTAT",ISSUESTATUS);

		        	if(ISSUESTATUS.equalsIgnoreCase("N"))
		        	ht.put("ORDER_STATUS","OPEN");
		        	}
		        	}   
				if (StrUtils.fString(PICKSTATUS).length() > 0){
					if(PICKSTATUS.equalsIgnoreCase("DRAFT"))
				{
		        	ht.put("ORDER_STATUS","Draft");
		        	ht.put("B.PICKSTATUS","N");
		        	}
		        	else
		        	{
		        	ht.put("B.PICKSTATUS",PICKSTATUS);

		        	if(PICKSTATUS.equalsIgnoreCase("N"))
		        	ht.put("ORDER_STATUS","OPEN");
		        	}
		        	}
				if (StrUtils.fString(PRD_TYPE_ID).length() > 0)
					ht.put("C.ITEMTYPE", PRD_TYPE_ID);
				if (StrUtils.fString(PRD_BRAND_ID).length() > 0)
					ht.put("C.PRD_BRAND_ID", PRD_BRAND_ID);
				if (StrUtils.fString(PRD_CLS_ID).length() > 0)
					ht.put("C.PRD_CLS_ID", PRD_CLS_ID);
				if (StrUtils.fString(PRD_DEPT_ID).length() > 0)
					ht.put("C.PRD_DEPT_ID", PRD_DEPT_ID);
				if (StrUtils.fString(statusID).length() > 0)
					ht.put("A.STATUS_ID", statusID);
				if (StrUtils.fString(EMPNO).length() > 0)
					ht.put("A.EMPNO", EMPNO);
				if (StrUtils.fString(CUSTOMERTYPE).length() > 0)
					ht.put("CUSTTYPE", CUSTOMERTYPE);
				if (StrUtils.fString(UOM).length() > 0)
					ht.put("SKTUOM", UOM);

				movQryList = movHisUtil.getWorkOrderSummaryList(ht, fdate, tdate, type, PLANT, PRD_DESCRIP, CUSTNAME,UOM,
						"",POSSEARCH);

			} else if (type.equals("OB_SUMMARY_ORD_WITH_PRICE")) {

				if (StrUtils.fString(JOBNO).length() > 0)
					ht.put("A.JOBNUM", JOBNO);
				if (StrUtils.fString(ITEMNO).length() > 0)
					ht.put("B.ITEM", ITEMNO);
				if (StrUtils.fString(ORDERNO).length() > 0)
					ht.put("A.DONO", ORDERNO);
				if (StrUtils.fString(CUSTOMERID).length() > 0)
					ht.put("A.CUSTCODE", CUSTOMERID);
				if (StrUtils.fString(ORDERTYPE).length() > 0)
					ht.put("A.ORDERTYPE", ORDERTYPE);
				if (StrUtils.fString(ISSUESTATUS).length() > 0)
					ht.put("B.LNSTAT", ISSUESTATUS);
				if (StrUtils.fString(PICKSTATUS).length() > 0)
					ht.put("A.PickStaus", PICKSTATUS);
				if (StrUtils.fString(PRD_TYPE_ID).length() > 0)
					ht.put("C.ITEMTYPE", PRD_TYPE_ID);
				if (StrUtils.fString(PRD_BRAND_ID).length() > 0)
					ht.put("C.PRD_BRAND_ID", PRD_BRAND_ID);
				if (StrUtils.fString(PRD_CLS_ID).length() > 0)
					ht.put("C.PRD_CLS_ID", PRD_CLS_ID);
				if (StrUtils.fString(PRD_DEPT_ID).length() > 0)
					ht.put("C.PRD_DEPT_ID", PRD_DEPT_ID);
				if (StrUtils.fString(statusID).length() > 0)
					ht.put("A.STATUS_ID", statusID);
				if (StrUtils.fString(EMPNO).length() > 0)
					ht.put("A.EMPNO", EMPNO);
				if (StrUtils.fString(CUSTOMERTYPE).length() > 0)
					ht.put("CUSTTYPE", CUSTOMERTYPE);
				if (StrUtils.fString(UOM).length() > 0)
					ht.put("UNITMO", UOM);
				
				if (taxby.equalsIgnoreCase("BYORDER")) {
					movQryList = movHisUtil.getCustomerDOInvoiceSummary(ht, fdate, tdate, type, PLANT, PRD_DESCRIP,
							CUSTNAME, SORT,POSSEARCH);
				} else {
					movQryList = movHisUtil.getCustomerDOInvoiceSummaryByProductGst(ht, fdate, tdate, type, PLANT,
							PRD_DESCRIP, CUSTNAME, SORT,POSSEARCH);

				}

			} else if ((type.equals("OB_SUMMARY_ISS_WITH_PRICE")) || (type.equals("OB_SUMMARY_ISSUE"))) {

				if (StrUtils.fString(JOBNO).length() > 0)
					ht.put("JOBNUM", JOBNO);
				if (StrUtils.fString(ITEMNO).length() > 0)
					ht.put("ITEM", ITEMNO);
				if (StrUtils.fString(ORDERNO).length() > 0)
					ht.put("DONO", ORDERNO);
				if (StrUtils.fString(INVOICENO).length() > 0)
					ht.put("INVOICENO", INVOICENO);
				if (StrUtils.fString(ORDERTYPE).length() > 0)
					ht.put("ORDERTYPE", ORDERTYPE);
				if (StrUtils.fString(ISSUESTATUS).length() > 0)
					ht.put("STATUS", ISSUESTATUS);
				if (StrUtils.fString(PICKSTATUS).length() > 0)
					ht.put("PickStaus", PICKSTATUS);
				if (StrUtils.fString(PRD_TYPE_ID).length() > 0)
					ht.put("ITEMTYPE", PRD_TYPE_ID);
				if (StrUtils.fString(PRD_BRAND_ID).length() > 0)
					ht.put("PRD_BRAND_ID", PRD_BRAND_ID);
				if (StrUtils.fString(PRD_CLS_ID).length() > 0)
					ht.put("PRD_CLS_ID", PRD_CLS_ID);
				if (StrUtils.fString(PRD_DEPT_ID).length() > 0)
					ht.put("PRD_DEPT_ID", PRD_DEPT_ID);
				if (StrUtils.fString(statusID).length() > 0)
					ht.put("STATUS_ID", statusID);
				if (StrUtils.fString(EMPNO).length() > 0)
					ht.put("EMPNO", EMPNO);
				if (StrUtils.fString(UOM).length() > 0)
					ht.put("UNITMO", UOM);
				if (StrUtils.fString(CUSTOMERTYPE).length() > 0)
					ht.put("CUSTTYPE", CUSTOMERTYPE);

				if (StrUtils.fString(LOC).length() > 0)	ht.put("LOC", LOC);
				if (StrUtils.fString(LOC_TYPE_ID).length() > 0)	ht.put("LOC_TYPE_ID", LOC_TYPE_ID);
				if (StrUtils.fString(LOC_TYPE_ID2).length() > 0)	ht.put("LOC_TYPE_ID2", LOC_TYPE_ID2);
				if (StrUtils.fString(LOC_TYPE_ID3).length() > 0)	ht.put("LOC_TYPE_ID3", LOC_TYPE_ID3);
				if (StrUtils.fString(sort).length() > 0)	ht.put("SORT", sort);
				if (taxby.equalsIgnoreCase("BYORDER")) {
					movQryList = movHisUtil.getCustomerDOInvoiceIssueSummary(ht, fdate, tdate, type, PLANT, PRD_DESCRIP,
							CUSTNAME, SORT,POSSEARCH);
				} else {
					movQryList = movHisUtil.getCustomerDOInvoiceIssueSummaryByProductGst(ht, fdate, tdate, type, PLANT,
							PRD_DESCRIP, CUSTNAME, SORT,POSSEARCH);
				}

			} else if (type.equals("OB_SUMMARY_ISS_WITH_AVGCOST")) {

				if (StrUtils.fString(JOBNO).length() > 0)
					ht.put("JOBNUM", JOBNO);
				if (StrUtils.fString(ITEMNO).length() > 0)
					ht.put("ITEM", ITEMNO);
				if (StrUtils.fString(ORDERNO).length() > 0)
					ht.put("DONO", ORDERNO);
				if (StrUtils.fString(ORDERTYPE).length() > 0)
					ht.put("ORDERTYPE", ORDERTYPE);
				if (StrUtils.fString(ISSUESTATUS).length() > 0)
					ht.put("STATUS", ISSUESTATUS);
				if (StrUtils.fString(PICKSTATUS).length() > 0)
					ht.put("PickStaus", PICKSTATUS);
				if (StrUtils.fString(PRD_TYPE_ID).length() > 0)
					ht.put("ITEMTYPE", PRD_TYPE_ID);
				if (StrUtils.fString(PRD_BRAND_ID).length() > 0)
					ht.put("PRD_BRAND_ID", PRD_BRAND_ID);
				if (StrUtils.fString(PRD_CLS_ID).length() > 0)
					ht.put("PRD_CLS_ID", PRD_CLS_ID);
				if (StrUtils.fString(PRD_DEPT_ID).length() > 0)
					ht.put("PRD_DEPT_ID", PRD_DEPT_ID);
				if (StrUtils.fString(statusID).length() > 0)
					ht.put("STATUS_ID", statusID);
				if (StrUtils.fString(EMPNO).length() > 0)
					ht.put("EMPNO", EMPNO);
				if (StrUtils.fString(CUSTOMERTYPE).length() > 0)
					ht.put("CUSTTYPE", CUSTOMERTYPE);

				if (CURRENCYID.equals("")) {
					CurrencyDAO _CurrencyDAO = new CurrencyDAO();
					List listQry = _CurrencyDAO.getCurrencyList(PLANT, CURRENCYDISPLAY);
					for (int i = 0; i < listQry.size(); i++) {
						Map m = (Map) listQry.get(i);
						CURRENCYID = (String) m.get("currencyid");
					}
				}
				if (taxby.equalsIgnoreCase("BYORDER")) {
					movQryList = movHisUtil.getCustomerDOAvgCostIssueSummary(ht, fdate, tdate, type, PLANT, PRD_DESCRIP,
							CUSTNAME, SORT, CURRENCYID, baseCurrency);
				} else {
					movQryList = movHisUtil.getCustomerDOAvgCostIssueSummaryByProductGst(ht, fdate, tdate, type, PLANT,
							PRD_DESCRIP, CUSTNAME, SORT, CURRENCYID, baseCurrency);
				}
			}

//			Boolean workSheetCreated = true;
			if (movQryList.size() > 0) {
				HSSFSheet sheet = null;
				HSSFCellStyle styleHeader = null;
				HSSFCellStyle dataStyle = null;
				HSSFCellStyle dataStyleSpecial = null;
				HSSFCellStyle CompHeader = null;
				dataStyle = createDataStyle(wb);
				dataStyleSpecial = createDataStyle(wb);
				CreationHelper createHelper = wb.getCreationHelper();
				sheet = wb.createSheet("Sheet"+SheetId);
				styleHeader = createStyleHeader(wb);
				CompHeader = createCompStyleHeader(wb);
				sheet = this.createWidthOutboundSummary(sheet, type);
				sheet = this.createHeaderOutboundSummary(sheet, styleHeader, type, sort,PLANT);
				sheet = this.createHeaderCompanyReports(sheet,CompHeader,reportType,PLANT);
				int index = 2;
				double unitprice = 0, Price = 0, tax = 0, PricewTax = 0;//orderPriceSubTot = 0, issPriceSubTot = 0, 
				float gstpercentage = 0;
				String  deliverydateandtime = "";//strDiffQty = "",
//				DecimalFormat decformat = new DecimalFormat("#,##0.00");
				DecimalFormat decimalFormat = new DecimalFormat("#.#####");
				decimalFormat.setRoundingMode(RoundingMode.FLOOR);
				CustomerBeanDAO customerBeanDAO = new CustomerBeanDAO();
				String customerstatusid = "", customerstatusdesc = "", customertypeid = "", customertypedesc = "";

				for (int iCnt = 0; iCnt < movQryList.size(); iCnt++) {
					Map lineArr = (Map) movQryList.get(iCnt);
					int k = 0;
					customerstatusid = customerBeanDAO.getCustomerStatusId(PLANT, (String) lineArr.get("custname"));
					if (customerstatusid == null || customerstatusid.equals("")) {
						customerstatusdesc = "";
					} else {
						customerstatusdesc = customerBeanDAO.getCustomerStatusDesc(PLANT, customerstatusid);
					}
					customertypeid = customerBeanDAO.getCustomerTypeId(PLANT, (String) lineArr.get("custname"));
					if (customertypeid == null || customertypeid.equals("")) {
						customertypedesc = "";
					} else {
						customertypedesc = customerBeanDAO.getCustomerTypeDesc(PLANT, customertypeid);
					}

					if (type.equals("OB_SUMMARY_ORD_WITH_PRICE")) {
						gstpercentage = Float.parseFloat(((String) lineArr.get("Tax").toString()));
						unitprice = Double.parseDouble((String) lineArr.get("unitprice"));
						//unitprice = StrUtils.RoundDB(unitprice, Integer.parseInt(DbBean.NOOFDECIMALPTSFORCURRENCY));
						// indiviual subtotal price details
						Price = Double.parseDouble((String) lineArr.get("ordPrice"));
						//Price = StrUtils.RoundDB(Price, Integer.parseInt(DbBean.NOOFDECIMALPTSFORCURRENCY));
						//tax = (Price * gstpercentage) / 100;
						tax = Double.parseDouble((String) lineArr.get("taxval"));
						//tax = StrUtils.RoundDB(tax, 2);
						PricewTax = Price + tax;
						//PricewTax = StrUtils.RoundDB(PricewTax, 2);

					} else if (type.equals("OB_SUMMARY_ISS_WITH_PRICE")) {

						gstpercentage = Float.parseFloat(((String) lineArr.get("Tax").toString()));

						unitprice = Double.parseDouble((String) lineArr.get("unitprice"));
						//unitprice = StrUtils.RoundDB(unitprice, Integer.parseInt(DbBean.NOOFDECIMALPTSFORCURRENCY));

						Price = Double.parseDouble((String) lineArr.get("issPrice"));
						//Price = StrUtils.RoundDB(Price, Integer.parseInt(DbBean.NOOFDECIMALPTSFORCURRENCY));
						//tax = (Price * gstpercentage) / 100;
						tax = Double.parseDouble((String) lineArr.get("taxval"));
						//tax = StrUtils.RoundDB(tax, 2);
						PricewTax = Price + tax;
						//PricewTax = StrUtils.RoundDB(PricewTax, 2);

					} else if (type.equals("OB_SUMMARY_ISS_WITH_AVGCOST")) {

						gstpercentage = Float.parseFloat(((String) lineArr.get("Tax").toString()));

						unitprice = Double.parseDouble((String) lineArr.get("AVERAGE_COST"));
						//unitprice = StrUtils.RoundDB(unitprice, Integer.parseInt(DbBean.NOOFDECIMALPTSFORCURRENCY));

						Price = (Double.parseDouble((String) lineArr.get("qty")))
								* Double.parseDouble((String) lineArr.get("AVERAGE_COST"));
//						Price = StrUtils.RoundDB(Price, Integer.parseInt(DbBean.NOOFDECIMALPTSFORCURRENCY));
						tax = (Price * gstpercentage) / 100;
						//tax = StrUtils.RoundDB(tax, 2);
						//tax = Double.parseDouble((String) lineArr.get("taxval"));
						PricewTax = Price + tax;
						//PricewTax = StrUtils.RoundDB(PricewTax, 2);

					}
					deliverydateandtime = (String) lineArr.get("deliverydate") + " "
							+ (String) lineArr.get("deliverytime");
					
					String PricewTaxValue =  decimalFormat.format(PricewTax);
					String taxValue =  decimalFormat.format(tax);
					String PriceValue =  decimalFormat.format(Price);
					String unitPriceValue =  decimalFormat.format(unitprice);
					String gstpercentageValue = String.valueOf(gstpercentage);
					//String unitPriceValue = String.valueOf(unitprice);
					String qtyValue= (String) lineArr.get("qty");
					String qtyPickValue= (String) lineArr.get("qtypick");
					String qtyOrValue= (String) lineArr.get("qtyor");
					
					/*float PricewTaxVal ="".equals(PricewTaxValue) ? 0.0f :  Float.parseFloat(PricewTaxValue);*/
					/*float taxVal ="".equals(taxValue) ? 0.0f :  Float.parseFloat(taxValue);*/
					/*float PriceVal ="".equals(PriceValue) ? 0.0f :  Float.parseFloat(PriceValue);*/
					float gstpercentageVal ="".equals(gstpercentageValue) ? 0.0f :  Float.parseFloat(gstpercentageValue);
					/*float unitPriceVal ="".equals(unitPriceValue) ? 0.0f :  Float.parseFloat(unitPriceValue);*/
					float qtyVal ="".equals(qtyValue) ? 0.0f :  Float.parseFloat(qtyValue);
					float qtyPickVal =qtyPickValue == null || "".equals(qtyPickValue) ? 0.0f :  Float.parseFloat(qtyPickValue);
					float qtyOrVal ="".equals(qtyOrValue) ? 0.0f :  Float.parseFloat(qtyOrValue);
					//imti start		 
		 			String lnstat= StrUtils.fString((String) lineArr.get("lnstat"));
		 			String delivery_status= StrUtils.fString((String) lineArr.get("DELIVERY_STATUS"));
		 			String intransit_status= StrUtils.fString((String) lineArr.get("INTRANSIT_STATUS"));
		 			String ordstat= (String)lineArr.get("ORDER_STATUS");
					if(lnstat.equalsIgnoreCase("N"))
					{
						lnstat="OPEN";
						if(ordstat.equalsIgnoreCase("Draft"))
							lnstat="DRAFT";				
					}
		 		       else if(lnstat.equalsIgnoreCase("O"))
		 		    	   lnstat="PARTIALLY ISSUED";
//		 		       else if(lnstat.equalsIgnoreCase("C"))
//		 		    	   lnstat="ISSUED";
					
					if(DELIVERYAPP.equals("1")){
					 if(lnstat.equalsIgnoreCase("C"))
						 lnstat="ISSUED";
						else if(lnstat.equalsIgnoreCase("S"))
							lnstat="SHIPPED";
						else if(lnstat.equalsIgnoreCase("D"))
							lnstat="DELIVERED";
					}else{
						 if(lnstat.equalsIgnoreCase("C"))
			 		    	   lnstat="SHIPPED";
						 if(ISSUESTATUS.equalsIgnoreCase("O")) {
							 if(lnstat.equalsIgnoreCase("O")) 
								 lnstat="PARTIALLY ISSUED";
						 }
						 if(ISSUESTATUS.equalsIgnoreCase("C")) {
							 if(lnstat.equalsIgnoreCase("C"))
								 lnstat="SHIPPED";
						 }
						 
							if(ISSUESTATUS.equalsIgnoreCase("")) {
						 if(!intransit_status.equalsIgnoreCase("")){
							 lnstat = intransit_status.toUpperCase(); 
						 }
						 if(!delivery_status.equalsIgnoreCase("")){
							 lnstat = delivery_status.toUpperCase(); 
						 }
							}
						 
						 if(ISSUESTATUS.equalsIgnoreCase("DELIVERED")) {
							 lnstat = delivery_status.toUpperCase(); 
						 }else if(ISSUESTATUS.equalsIgnoreCase("INTRANSIT")) {
							 lnstat = intransit_status.toUpperCase(); 
						 }
				 		}
		 				//imti end
		 
		   	   		//imti start		 
		  			String pickstatus= StrUtils.fString((String) lineArr.get("pickstatus"));
//		  			String DELIVERY_STATUS= StrUtils.fString((String) lineArr.get("DELIVERY_STATUS"));
		  			String ordstats= (String)lineArr.get("ORDER_STATUS");
					if(pickstatus.equalsIgnoreCase("N"))
					{
						pickstatus="OPEN";
						if(ordstats.equalsIgnoreCase("Draft"))
							pickstatus="DRAFT";				
					}
		  		       else if(pickstatus.equalsIgnoreCase("O"))
		  		    	 pickstatus="PARTIALLY PICKED";
		  		       else if(pickstatus.equalsIgnoreCase("C"))
		  		    	 pickstatus="PICKED";
		  //imti end
		   			 
					double unitPriceVal ="".equals(unitPriceValue) ? 0.0d :  Double.parseDouble(unitPriceValue);
					double PriceVal ="".equals(PriceValue) ? 0.0d :  Double.parseDouble(PriceValue);
					double taxVal ="".equals(taxValue) ? 0.0d :  Double.parseDouble(taxValue);
					double PricewTaxVal ="".equals(PricewTaxValue) ? 0.0d :  Double.parseDouble(PricewTaxValue);
					
		    		
					/*if(PricewTaxVal==0f){
		    			PricewTaxValue="0.000";
		    		}else{
		    			PricewTaxValue=PricewTaxValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
		    		}if(taxVal==0f){
		    			taxValue="0.000";
		    		}else{
		    			taxValue=taxValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
		    		}if(PriceVal==0f){
		    			PriceValue="0.00000";
		    		}else{
		    			PriceValue=PriceValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
		    		}*/if(gstpercentageVal==0f){
		    			gstpercentageValue="0.000";
		    		}else{
		    			gstpercentageValue=gstpercentageValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
		    		}/*if(unitPriceVal==0f){
		    			unitPriceValue="0.00000";
		    		}else{
		    			unitPriceValue=unitPriceValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
		    		}*/if(qtyVal==0f){
		    			qtyValue="0.000";
		    		}else{
		    			qtyValue=qtyValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
		    		}if(qtyPickVal==0f){
		    			qtyPickValue="0.000";
		    		}else{
		    			qtyPickValue=qtyPickValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
		    		}if(qtyOrVal==0f){
		    			qtyOrValue="0.000";
		    		}else{
		    			qtyOrValue=qtyOrValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
		    		}
		    		
		    		unitPriceValue = StrUtils.addZeroes(unitPriceVal, numberOfDecimal);
		    		PriceValue = StrUtils.addZeroes(PriceVal, numberOfDecimal);
		    		taxValue = StrUtils.addZeroes(taxVal, numberOfDecimal);
		    		PricewTaxValue = StrUtils.addZeroes(PricewTaxVal, numberOfDecimal);
					
					HSSFRow row = sheet.createRow(index);

					HSSFCell cell = row.createCell(k++);
					cell.setCellValue(iCnt + 1);
					cell.setCellStyle(dataStyle);

					cell = row.createCell(k++);
					cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String) lineArr.get("dono"))));
					cell.setCellStyle(dataStyle);
					
					if (type.equals("OB_SUMMARY_ISS_WITH_PRICE")) {
					cell = row.createCell(k++);
					cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String) lineArr.get("INVOICENO"))));
					cell.setCellStyle(dataStyle);
					}
					
					if ((type.equals("OB_SUMMARY_ISS_WITH_PRICE")) || (type.equals("OB_SUMMARY_ISSUE")) || (type.equals("OB_SUMMARY_ISS_WITH_AVGCOST"))) {
					cell = row.createCell(k++);
					cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String) lineArr.get("orderType"))));
					cell.setCellStyle(dataStyle);
					} else{
						cell = row.createCell(k++);
						cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String) lineArr.get("ordertype"))));
						cell.setCellStyle(dataStyle);
					}

					if (!type.equals("OUTBOUND")) {
						cell = row.createCell(k++);
						cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String) lineArr.get("jobnum"))));
						cell.setCellStyle(dataStyle);
					} else {
						cell = row.createCell(k++);
						cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String) lineArr.get("jobNum"))));
						cell.setCellStyle(dataStyle);
					}

					cell = row.createCell(k++);
					cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String) lineArr.get("custname"))));
					cell.setCellStyle(dataStyle);

					if(sort.equalsIgnoreCase("LOCATION")) {
					cell = row.createCell(k++);
					cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String) lineArr.get("LOC"))));
					cell.setCellStyle(dataStyle);
					}
					
					cell = row.createCell(k++);
					cell.setCellValue(new HSSFRichTextString(StrUtils.fString(customertypedesc)));
					cell.setCellStyle(dataStyle);
					
					cell = row.createCell(k++);
					cell.setCellValue(new HSSFRichTextString(StrUtils.fString(customerstatusdesc)));
					cell.setCellStyle(dataStyle); 

					cell = row.createCell(k++);
					cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String) lineArr.get("remarks"))));
					cell.setCellStyle(dataStyle);

					cell = row.createCell(k++);
					cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String) lineArr.get("remarks2"))));
					cell.setCellStyle(dataStyle);

					cell = row.createCell(k++);
					cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String) lineArr.get("item"))));
					cell.setCellStyle(dataStyle);

					cell = row.createCell(k++);
					cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String) lineArr.get("itemdesc"))));
					cell.setCellStyle(dataStyle);

					if (type.equals("OUTBOUND") || type.equals("OB_SUMMARY_ORD_WITH_PRICE")) {
						cell = row.createCell(k++);
						cell.setCellValue(
								new HSSFRichTextString(StrUtils.fString((String) lineArr.get("DetailItemDesc"))));
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
					if (!type.equals("OUTBOUND")) {
						cell = row.createCell(k++);
						Calendar c = Calendar.getInstance();
						c.setTime(_shortDateformatter.parse(StrUtils.fString((String) lineArr.get("trandate"))));
						dataStyleSpecial.setDataFormat(createHelper.createDataFormat().getFormat("yyyy-MM-dd"));
						cell.setCellValue(c.getTime());
						cell.setCellStyle(dataStyleSpecial);

					} else {

						cell = row.createCell(k++);
						Calendar c = Calendar.getInstance();
						c.setTime(_shortDateformatter.parse(StrUtils.fString((String) lineArr.get("CollectionDate"))));
						dataStyleSpecial.setDataFormat(createHelper.createDataFormat().getFormat("yyyy-MM-dd"));
						cell.setCellValue(c.getTime());
						cell.setCellStyle(dataStyleSpecial);

					}

					if (type.equals("OUTBOUND") || type.equals("OB_SUMMARY_ORD_WITH_PRICE")) {
						cell = row.createCell(k++);
						cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String) deliverydateandtime)));
						cell.setCellStyle(dataStyle);
					}
					
					cell = row.createCell(k++);
					cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String) lineArr.get("UOM"))));
					cell.setCellStyle(dataStyle);

					cell = row.createCell(k++);
					cell.setCellValue(qtyOrValue);
					cell.setCellStyle(dataStyle);

					if (!type.equals("OUTBOUND")) {
						cell = row.createCell(k++);
						cell.setCellValue(qtyPickValue);
						cell.setCellStyle(dataStyle);
					} else {
						cell = row.createCell(k++);
						cell.setCellValue(qtyPickValue);
						cell.setCellStyle(dataStyle);

					}

					cell = row.createCell(k++);
					cell.setCellValue(qtyValue);
					cell.setCellStyle(dataStyle);

					cell = row.createCell(k++);
					cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String) lineArr.get("empname"))));
					cell.setCellStyle(dataStyle);

					if (type.equals("OB_SUMMARY_ORD_WITH_PRICE") || type.equals("OB_SUMMARY_ISS_WITH_PRICE")
							|| type.equals("OB_SUMMARY_ISS_WITH_AVGCOST")) {
						cell = row.createCell(k++);
						cell.setCellValue(unitPriceValue);
						cell.setCellStyle(dataStyle);

						cell = row.createCell(k++);
						cell.setCellValue(gstpercentageValue);
						cell.setCellStyle(dataStyle);

						cell = row.createCell(k++);
						cell.setCellValue(PriceValue);
						cell.setCellStyle(dataStyle);

						cell = row.createCell(k++);
						cell.setCellValue(taxValue);
						cell.setCellStyle(dataStyle);

						cell = row.createCell(k++);
						cell.setCellValue(PricewTaxValue);
						cell.setCellStyle(dataStyle);

					}

					if (type.equals("OUTBOUND")) {
						cell = row.createCell(k++);
						cell.setCellValue(new HSSFRichTextString(pickstatus));
						cell.setCellStyle(dataStyle);

						cell = row.createCell(k++);
						cell.setCellValue(new HSSFRichTextString(lnstat));
						cell.setCellStyle(dataStyle);

					}
					if ((type.equals("OB_SUMMARY_ISSUE")) || (type.equals("OB_SUMMARY_ISS_WITH_PRICE"))) {
					String PICK =	StrUtils.fString((String) lineArr.get("PickStatus"));
					if(PICK.equals("C")) {
						PICK="PICKED";
					}else if(PICK.equals("O")) {
						PICK= "PARTIALLY PICKED";
					}

					String ISSUE =	StrUtils.fString((String) lineArr.get("LNSTAT"));
					if(DELIVERYAPP.equals("1")){
					if(ISSUE.equals("C")) {
						ISSUE="ISSUED";
					}else if(ISSUE.equals("O")) {
						ISSUE= "PARTIALLY ISSUED";
					}else if(ISSUE.equals("S")) {
						ISSUE= "SHIPPED";
					}else if(ISSUE.equals("D")) {
						ISSUE= "DELIVERED";
					}
					}else {
						 if(ISSUE.equals("O")) {
							ISSUE= "PARTIALLY ISSUED";
						}else if(ISSUE.equals("C")) {
							ISSUE= "SHIPPED";
						}	
						 if(ISSUESTATUS.equalsIgnoreCase("O")) {
							 if(ISSUE.equalsIgnoreCase("O")) 
								 ISSUE="PARTIALLY ISSUED";
						 }
						 if(ISSUESTATUS.equalsIgnoreCase("C")) {
							 if(ISSUE.equalsIgnoreCase("C"))
								 ISSUE="SHIPPED";
						 }
							if(ISSUESTATUS.equalsIgnoreCase("")) {
						 if(!intransit_status.equalsIgnoreCase("")){
							 ISSUE = intransit_status.toUpperCase(); 
						 }
						 if(!delivery_status.equalsIgnoreCase("")){
							 ISSUE = delivery_status.toUpperCase(); 
						 }
							}
						 
						 if(ISSUESTATUS.equalsIgnoreCase("DELIVERED")) {
							 ISSUE = delivery_status.toUpperCase(); 
						 }else if(ISSUESTATUS.equalsIgnoreCase("INTRANSIT")) {
							 ISSUE = intransit_status.toUpperCase(); 
						 }
					}
					
					cell = row.createCell(k++);
					cell.setCellValue(new HSSFRichTextString(PICK));
					cell.setCellStyle(dataStyle);
					
					cell = row.createCell(k++);
					cell.setCellValue(new HSSFRichTextString(ISSUE));
					cell.setCellStyle(dataStyle);
					}
					
//					cell = row.createCell(k++);
//					cell.setCellValue(new HSSFRichTextString(DELIVERY_STATUS));
//					cell.setCellStyle(dataStyle);
					
					cell = row.createCell(k++);
				    cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String) lineArr.get("SALES_LOCATION"))));
				    cell.setCellStyle(dataStyle);
				    
				    cell = row.createCell(k++);
				    cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String) lineArr.get("TAXTREATMENT"))));
				    cell.setCellStyle(dataStyle);
					
					    cell = row.createCell(k++);
					    cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String) lineArr.get("users"))));
					    cell.setCellStyle(dataStyle);
					

					index++;
					if ((index - 2) % maxRowsPerSheet == 0) {
						index = 2;
						SheetId++;
						 sheet = wb.createSheet("Sheet"+SheetId);
						styleHeader = createStyleHeader(wb);
						CompHeader = createCompStyleHeader(wb);
						sheet = this.createWidthOutboundSummary(sheet, type);
						sheet = this.createHeaderOutboundSummary(sheet, styleHeader, type);
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

//	resviya starts
	
	private HSSFWorkbook writeToExcelConsignmentOrderSummaryIssue(HttpServletRequest request, HttpServletResponse response,
			HSSFWorkbook wb) {
		HTReportUtil movHisUtil = new HTReportUtil();
		ArrayList movQryList = new ArrayList();
		int maxRowsPerSheet = 65535;
		String fdate = "", tdate = "", taxby = "";
		int SheetId =1;
		try {

			String PLANT = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
//			String baseCurrency = StrUtils.fString((String) request.getSession().getAttribute("BASE_CURRENCY")).trim();
			String ITEMNO = StrUtils.fString(request.getParameter("ITEM"));
			String PRD_DESCRIP = StrUtils.fString(request.getParameter("DESC"));
			String FROM_DATE = StrUtils.fString(request.getParameter("FROM_DATE"));
			String TO_DATE = StrUtils.fString(request.getParameter("TO_DATE"));
			String CUSTNAME = StrUtils.fString(request.getParameter("CUSTOMER"));
			String GINO = StrUtils.fString(request.getParameter("GINO"));
			String FROMWAREHOUSE = StrUtils.fString(request.getParameter("FROMWAREHOUSE"));
			String TOWAREHOUSE = StrUtils.fString(request.getParameter("TOWAREHOUSE"));
//			String CUSTOMERID = StrUtils.fString(request.getParameter("CUSTOMERID"));
			String ORDERNO = StrUtils.fString(request.getParameter("ORDERNO"));
			String JOBNO = StrUtils.fString(request.getParameter("JOBNO"));
			String PICKSTATUS = StrUtils.fString(request.getParameter("PICKSTATUS"));
			String ISSUESTATUS = StrUtils.fString(request.getParameter("ISSUESTATUS"));
			String ORDERTYPE = StrUtils.fString(request.getParameter("ORDERTYPE"));
			String PRD_TYPE_ID = StrUtils.fString(request.getParameter("PRD_TYPE_ID"));
			String PRD_BRAND_ID = StrUtils.fString(request.getParameter("PRD_BRAND_ID"));
			String PRD_DEPT_ID = StrUtils.fString(request.getParameter("PRD_DEPT_ID"));
			String PRD_CLS_ID = StrUtils.fString(request.getParameter("PRD_CLS_ID"));
			String statusID = StrUtils.fString(request.getParameter("STATUS_ID"));
			String type = StrUtils.fString(request.getParameter("DIRTYPE"));
			String SORT = StrUtils.fString(request.getParameter("SORT"));
			String EMPNO = StrUtils.fString(request.getParameter("EMP_NAME"));
			String CUSTOMERTYPE = StrUtils.fString(request.getParameter("CUSTOMER_TYPE_ID"));
//			String CURRENCYID = StrUtils.fString(request.getParameter("CURRENCYID"));
//			String CURRENCYDISPLAY = StrUtils.fString(request.getParameter("CURRENCYDISPLAY"));
			String UOM = StrUtils.fString(request.getParameter("UOM"));
			//imti start
			if(PICKSTATUS.equalsIgnoreCase("OPEN"))
				PICKSTATUS="N";
			else if(PICKSTATUS.equalsIgnoreCase("PARTIALLY PICKED"))
				PICKSTATUS="O";
			else if(PICKSTATUS.equalsIgnoreCase("PICKED"))
				PICKSTATUS="C";
			//imti end
			//imti start
			if(ISSUESTATUS.equalsIgnoreCase("OPEN"))
				ISSUESTATUS="N";
			else if(ISSUESTATUS.equalsIgnoreCase("PARTIALLY ISSUED"))
				ISSUESTATUS="O";
			else if(ISSUESTATUS.equalsIgnoreCase("ISSUED"))
				ISSUESTATUS="C";
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
			taxby = _PlantMstDAO.getTaxBy(PLANT);
			PlantMstDAO plantMstDAO = new PlantMstDAO();
	        String numberOfDecimal = plantMstDAO.getNumberOfDecimal(PLANT);
			
	        
				if (StrUtils.fString(JOBNO).length() > 0)
					ht.put("JOBNUM", JOBNO);
				if (StrUtils.fString(ITEMNO).length() > 0)
					ht.put("ITEM", ITEMNO);
				if (StrUtils.fString(ORDERNO).length() > 0)
					ht.put("TONO", ORDERNO);
				
				if (StrUtils.fString(GINO).length() > 0)
					ht.put("GINO", GINO);
				
				if (StrUtils.fString(FROMWAREHOUSE).length() > 0)
					ht.put("FROMWAREHOUSE", FROMWAREHOUSE);
				if (StrUtils.fString(TOWAREHOUSE).length() > 0)
					ht.put("TOWAREHOUSE", TOWAREHOUSE);
				
				if (StrUtils.fString(ORDERTYPE).length() > 0)
					ht.put("ORDERTYPE", ORDERTYPE);
				if (StrUtils.fString(ISSUESTATUS).length() > 0)
					ht.put("STATUS", ISSUESTATUS);
				if (StrUtils.fString(PICKSTATUS).length() > 0)
					ht.put("PickStaus", PICKSTATUS);
				if (StrUtils.fString(PRD_TYPE_ID).length() > 0)
					ht.put("ITEMTYPE", PRD_TYPE_ID);
				if (StrUtils.fString(PRD_BRAND_ID).length() > 0)
					ht.put("PRD_BRAND_ID", PRD_BRAND_ID);
				if (StrUtils.fString(PRD_DEPT_ID).length() > 0)
					ht.put("PRD_DEPT_ID", PRD_DEPT_ID);
				if (StrUtils.fString(PRD_CLS_ID).length() > 0)
					ht.put("PRD_CLS_ID", PRD_CLS_ID);
				if (StrUtils.fString(statusID).length() > 0)
					ht.put("STATUS_ID", statusID);
				if (StrUtils.fString(EMPNO).length() > 0)
					ht.put("EMPNO", EMPNO);
				if (StrUtils.fString(UOM).length() > 0)
					ht.put("UNITMO", UOM);
				if (StrUtils.fString(CUSTOMERTYPE).length() > 0)
					ht.put("CUSTTYPE", CUSTOMERTYPE);
				if (taxby.equalsIgnoreCase("BYORDER")) {
					movQryList = movHisUtil.getConsignmentDOInvoiceSummary(ht, fdate, tdate, type, PLANT, PRD_DESCRIP,
							CUSTNAME, SORT);
				} else {
					movQryList = movHisUtil.getConsignmentDOInvoiceSummaryByProductGst(ht, fdate, tdate, type, PLANT,
							PRD_DESCRIP, CUSTNAME, SORT);
				}
			

//			Boolean workSheetCreated = true;
			if (movQryList.size() > 0) {
				HSSFSheet sheet = null;
				HSSFCellStyle styleHeader = null;
				HSSFCellStyle dataStyle = null;
				HSSFCellStyle dataStyleSpecial = null;
				HSSFCellStyle CompHeader = null;
				dataStyle = createDataStyle(wb);
				dataStyleSpecial = createDataStyle(wb);
				CreationHelper createHelper = wb.getCreationHelper();
				sheet = wb.createSheet("Sheet"+SheetId);
				styleHeader = createStyleHeader(wb);
				CompHeader = createCompStyleHeader(wb);
				sheet = this.createWidthOutboundSummary(sheet, type);
				sheet = this.createHeaderConsignmentSummary(sheet, styleHeader, type);
				sheet = this.createHeaderCompanyReports(sheet,CompHeader,reportType,PLANT);
				int index = 2;
				double unitprice = 0, Price = 0, tax = 0, PricewTax = 0;//orderPriceSubTot = 0, issPriceSubTot = 0, 
				float gstpercentage = 0;
//				String strDiffQty = "", deliverydateandtime = "";
//				DecimalFormat decformat = new DecimalFormat("#,##0.00");
				DecimalFormat decimalFormat = new DecimalFormat("#.#####");
				decimalFormat.setRoundingMode(RoundingMode.FLOOR);
				CustomerBeanDAO customerBeanDAO = new CustomerBeanDAO();
				String customertypeid = "", customertypedesc = "";//customerstatusid = "", customerstatusdesc = "", 

				for (int iCnt = 0; iCnt < movQryList.size(); iCnt++) {
					Map lineArr = (Map) movQryList.get(iCnt);
					int k = 0;
//					customerstatusid = customerBeanDAO.getCustomerStatusId(PLANT, (String) lineArr.get("custname"));
//					if (customerstatusid == null || customerstatusid.equals("")) {
//						customerstatusdesc = "";
//					} else {
//						customerstatusdesc = customerBeanDAO.getCustomerStatusDesc(PLANT, customerstatusid);
//					}
					customertypeid = customerBeanDAO.getCustomerTypeId(PLANT, (String) lineArr.get("custname"));
					if (customertypeid == null || customertypeid.equals("")) {
						customertypedesc = "";
					} else {
						customertypedesc = customerBeanDAO.getCustomerTypeDesc(PLANT, customertypeid);
					}

					

						gstpercentage = Float.parseFloat(((String) lineArr.get("Tax").toString()));

						unitprice = Double.parseDouble((String) lineArr.get("unitprice"));
						//unitprice = StrUtils.RoundDB(unitprice, Integer.parseInt(DbBean.NOOFDECIMALPTSFORCURRENCY));

						Price = Double.parseDouble((String) lineArr.get("issPrice"));
						//Price = StrUtils.RoundDB(Price, Integer.parseInt(DbBean.NOOFDECIMALPTSFORCURRENCY));
						//tax = (Price * gstpercentage) / 100;
						tax = Double.parseDouble((String) lineArr.get("taxval"));
						//tax = StrUtils.RoundDB(tax, 2);
						PricewTax = Price + tax;
						//PricewTax = StrUtils.RoundDB(PricewTax, 2);

					 
//					deliverydateandtime = (String) lineArr.get("deliverydate") + " "
//							+ (String) lineArr.get("deliverytime");
					
					String PricewTaxValue =  decimalFormat.format(PricewTax);
					String taxValue =  decimalFormat.format(tax);
					String PriceValue =  decimalFormat.format(Price);
					String unitPriceValue =  decimalFormat.format(unitprice);
					String gstpercentageValue = String.valueOf(gstpercentage);
					//String unitPriceValue = String.valueOf(unitprice);
					String qtyValue= (String) lineArr.get("qty");
					String qtyPickValue= (String) lineArr.get("qtypick");
					String qtyOrValue= (String) lineArr.get("qtyor");
					String qtyInvValue= (String) lineArr.get("qtyac");
					
					/*float PricewTaxVal ="".equals(PricewTaxValue) ? 0.0f :  Float.parseFloat(PricewTaxValue);*/
					/*float taxVal ="".equals(taxValue) ? 0.0f :  Float.parseFloat(taxValue);*/
					/*float PriceVal ="".equals(PriceValue) ? 0.0f :  Float.parseFloat(PriceValue);*/
					float gstpercentageVal ="".equals(gstpercentageValue) ? 0.0f :  Float.parseFloat(gstpercentageValue);
					/*float unitPriceVal ="".equals(unitPriceValue) ? 0.0f :  Float.parseFloat(unitPriceValue);*/
					float qtyVal ="".equals(qtyValue) ? 0.0f :  Float.parseFloat(qtyValue);
					float qtyPickVal =qtyPickValue == null || "".equals(qtyPickValue) ? 0.0f :  Float.parseFloat(qtyPickValue);
					float qtyOrVal ="".equals(qtyOrValue) ? 0.0f :  Float.parseFloat(qtyOrValue);
					float qtyInvVal ="".equals(qtyInvValue) ? 0.0f :  Float.parseFloat(qtyInvValue);
					
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
		 		    	   lnstat="PARTIALLY ISSUED";
		 		       else if(lnstat.equalsIgnoreCase("C"))
		 		    	   lnstat="ISSUED";
		 				//imti end
		 
		   	   		//imti start		 
		  			String pickstatus= StrUtils.fString((String) lineArr.get("pickstatus"));
		  			String ordstats= (String)lineArr.get("ORDER_STATUS");
					if(pickstatus.equalsIgnoreCase("N"))
					{
						pickstatus="OPEN";
						if(ordstats.equalsIgnoreCase("Draft"))
							pickstatus="DRAFT";				
					}
		  		       else if(pickstatus.equalsIgnoreCase("O"))
		  		    	 pickstatus="PARTIALLY PICKED";
		  		       else if(pickstatus.equalsIgnoreCase("C"))
		  		    	 pickstatus="PICKED";
		  //imti end
		   			 
					double unitPriceVal ="".equals(unitPriceValue) ? 0.0d :  Double.parseDouble(unitPriceValue);
					double PriceVal ="".equals(PriceValue) ? 0.0d :  Double.parseDouble(PriceValue);
					double taxVal ="".equals(taxValue) ? 0.0d :  Double.parseDouble(taxValue);
					double PricewTaxVal ="".equals(PricewTaxValue) ? 0.0d :  Double.parseDouble(PricewTaxValue);
					
		    		
					/*if(PricewTaxVal==0f){
		    			PricewTaxValue="0.000";
		    		}else{
		    			PricewTaxValue=PricewTaxValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
		    		}if(taxVal==0f){
		    			taxValue="0.000";
		    		}else{
		    			taxValue=taxValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
		    		}if(PriceVal==0f){
		    			PriceValue="0.00000";
		    		}else{
		    			PriceValue=PriceValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
		    		}*/if(gstpercentageVal==0f){
		    			gstpercentageValue="0.000";
		    		}else{
		    			gstpercentageValue=gstpercentageValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
		    		}/*if(unitPriceVal==0f){
		    			unitPriceValue="0.00000";
		    		}else{
		    			unitPriceValue=unitPriceValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
		    		}*/if(qtyVal==0f){
		    			qtyValue="0.000";
		    		}else{
		    			qtyValue=qtyValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
		    		}if(qtyPickVal==0f){
		    			qtyPickValue="0.000";
		    		}else{
		    			qtyPickValue=qtyPickValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
		    		}if(qtyOrVal==0f){
		    			qtyOrValue="0.000";
		    		}else{
		    			qtyOrValue=qtyOrValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
		    		}if(qtyInvVal==0f){
		    			qtyInvValue="0.000";
		    		}else{
		    			qtyInvValue=qtyInvValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
		    		}
		    		
		    		unitPriceValue = StrUtils.addZeroes(unitPriceVal, numberOfDecimal);
		    		PriceValue = StrUtils.addZeroes(PriceVal, numberOfDecimal);
		    		taxValue = StrUtils.addZeroes(taxVal, numberOfDecimal);
		    		PricewTaxValue = StrUtils.addZeroes(PricewTaxVal, numberOfDecimal);
		    		
		    		qtyInvValue = StrUtils.addZeroes(qtyInvVal, "3");
		    		qtyOrValue = StrUtils.addZeroes(qtyOrVal, "3");
		    		qtyPickValue = StrUtils.addZeroes(qtyPickVal, "3");
		    		qtyValue = StrUtils.addZeroes(qtyVal, "3");
					
					HSSFRow row = sheet.createRow(index);

					HSSFCell cell = row.createCell(k++);
					cell.setCellValue(iCnt + 1);
					cell.setCellStyle(dataStyle);

					cell = row.createCell(k++);
					cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String) lineArr.get("tono"))));
					cell.setCellStyle(dataStyle);

					cell = row.createCell(k++);
					cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String) lineArr.get("GINO"))));
					cell.setCellStyle(dataStyle);
					
						cell = row.createCell(k++);
						cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String) lineArr.get("ordertype"))));
						cell.setCellStyle(dataStyle);
					

					
						cell = row.createCell(k++);
						cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String) lineArr.get("jobNum"))));
						cell.setCellStyle(dataStyle);
					

					cell = row.createCell(k++);
					cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String) lineArr.get("custname"))));
					cell.setCellStyle(dataStyle);
					
					cell = row.createCell(k++);
					cell.setCellValue(new HSSFRichTextString(StrUtils.fString((customertypedesc))));
					cell.setCellStyle(dataStyle);
					
					cell = row.createCell(k++);
					cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String) lineArr.get("fromwarehouse"))));
					cell.setCellStyle(dataStyle);
					
					cell = row.createCell(k++);
					cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String) lineArr.get("towarehouse"))));
					cell.setCellStyle(dataStyle);

					cell = row.createCell(k++);
					cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String) lineArr.get("remarks"))));
					cell.setCellStyle(dataStyle);

					cell = row.createCell(k++);
					cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String) lineArr.get("remarks2"))));
					cell.setCellStyle(dataStyle);

					cell = row.createCell(k++);
					cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String) lineArr.get("item"))));
					cell.setCellStyle(dataStyle);

					cell = row.createCell(k++);
					cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String) lineArr.get("itemdesc"))));
					cell.setCellStyle(dataStyle);

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
					
						cell = row.createCell(k++);
						Calendar c = Calendar.getInstance();
						c.setTime(_shortDateformatter.parse(StrUtils.fString((String) lineArr.get("trandate"))));
						dataStyleSpecial.setDataFormat(createHelper.createDataFormat().getFormat("yyyy-MM-dd"));
						cell.setCellValue(c.getTime());
						cell.setCellStyle(dataStyleSpecial);
					
					
					cell = row.createCell(k++);
					cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String) lineArr.get("UOM"))));
					cell.setCellStyle(dataStyle);

					cell = row.createCell(k++);
					cell.setCellValue(qtyOrValue);
					cell.setCellStyle(dataStyle);

					
						cell = row.createCell(k++);
						cell.setCellValue(qtyPickValue);
						cell.setCellStyle(dataStyle);
					
					cell = row.createCell(k++);
					cell.setCellValue(qtyValue);
					cell.setCellStyle(dataStyle);

					cell = row.createCell(k++);
					cell.setCellValue(qtyInvValue);
					cell.setCellStyle(dataStyle);

					cell = row.createCell(k++);
					cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String) lineArr.get("empname"))));
					cell.setCellStyle(dataStyle);

					
						cell = row.createCell(k++);
						cell.setCellValue(unitPriceValue);
						cell.setCellStyle(dataStyle);

						cell = row.createCell(k++);
						cell.setCellValue(gstpercentageValue);
						cell.setCellStyle(dataStyle);

						cell = row.createCell(k++);
						cell.setCellValue(PriceValue);
						cell.setCellStyle(dataStyle);

						cell = row.createCell(k++);
						cell.setCellValue(taxValue);
						cell.setCellStyle(dataStyle);

						cell = row.createCell(k++);
						cell.setCellValue(PricewTaxValue);
						cell.setCellStyle(dataStyle);

					    cell = row.createCell(k++);
					    cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String) lineArr.get("users"))));
					    cell.setCellStyle(dataStyle);
					

					index++;
					if ((index - 2) % maxRowsPerSheet == 0) {
						index = 2;
						SheetId++;
						 sheet = wb.createSheet("Sheet"+SheetId);
						styleHeader = createStyleHeader(wb);
						CompHeader = createCompStyleHeader(wb);
						sheet = this.createWidthOutboundSummary(sheet, type);
						sheet = this.createHeaderConsignmentSummary(sheet, styleHeader, type);
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

//	ends
	private HSSFWorkbook writeToExcelOutboundOrderSummaryWithRemarks(HttpServletRequest request,
			HttpServletResponse response, HSSFWorkbook wb) {
		HTReportUtil movHisUtil = new HTReportUtil();
		ArrayList movQryList = new ArrayList();
		int maxRowsPerSheet = 65535;
		String fdate = "", tdate = "";
		int SheetId =1;
		try {

			String PLANT = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
//			String baseCurrency = StrUtils.fString((String) request.getSession().getAttribute("BASE_CURRENCY")).trim();
			String ITEMNO = StrUtils.fString(request.getParameter("ITEM"));
			String PRD_DESCRIP = StrUtils.fString(request.getParameter("DESC"));
			String FROM_DATE = StrUtils.fString(request.getParameter("FROM_DATE"));
			String TO_DATE = StrUtils.fString(request.getParameter("TO_DATE"));
			String CUSTNAME = StrUtils.fString(request.getParameter("CUSTOMER"));
			String CUSTOMERID = StrUtils.fString(request.getParameter("CUSTOMERID"));
			String ORDERNO = StrUtils.fString(request.getParameter("ORDERNO"));
			String JOBNO = StrUtils.fString(request.getParameter("JOBNO"));
			String PICKSTATUS = StrUtils.fString(request.getParameter("PICKSTATUS"));
			String ISSUESTATUS = StrUtils.fString(request.getParameter("ISSUESTATUS"));
			String ORDERTYPE = StrUtils.fString(request.getParameter("ORDERTYPE"));
			String PRD_TYPE_ID = StrUtils.fString(request.getParameter("PRD_TYPE_ID"));
			String PRD_BRAND_ID = StrUtils.fString(request.getParameter("PRD_BRAND_ID"));
			String PRD_CLS_ID = StrUtils.fString(request.getParameter("PRD_CLS_ID"));
			String statusID = StrUtils.fString(request.getParameter("STATUS_ID"));
			String type = StrUtils.fString(request.getParameter("DIRTYPE"));
//			String SORT = StrUtils.fString(request.getParameter("SORT"));
			String EMPNO = StrUtils.fString(request.getParameter("EMP_NAME"));
			String CUSTOMERTYPE = StrUtils.fString(request.getParameter("CUSTOMER_TYPE_ID"));
//			String CURRENCYID = StrUtils.fString(request.getParameter("CURRENCYID"));
//			String CURRENCYDISPLAY = StrUtils.fString(request.getParameter("CURRENCYDISPLAY"));
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
				ht.put("B.DONO", ORDERNO);
			if (StrUtils.fString(CUSTOMERID).length() > 0)
				ht.put("A.CUSTCODE", CUSTOMERID);
			if (StrUtils.fString(ORDERTYPE).length() > 0)
				ht.put("A.ORDERTYPE", ORDERTYPE);
			if (StrUtils.fString(ISSUESTATUS).length() > 0)
				ht.put("B.LNSTAT", ISSUESTATUS);
			if (StrUtils.fString(PICKSTATUS).length() > 0)
				ht.put("B.PICKSTATUS", PICKSTATUS);
			if (StrUtils.fString(PRD_TYPE_ID).length() > 0)
				ht.put("C.ITEMTYPE", PRD_TYPE_ID);
			if (StrUtils.fString(PRD_BRAND_ID).length() > 0)
				ht.put("C.PRD_BRAND_ID", PRD_BRAND_ID);
			if (StrUtils.fString(PRD_CLS_ID).length() > 0)
				ht.put("C.PRD_CLS_ID", PRD_CLS_ID);
			if (StrUtils.fString(statusID).length() > 0)
				ht.put("A.STATUS_ID", statusID);
			if (StrUtils.fString(EMPNO).length() > 0)
				ht.put("A.EMPNO", EMPNO);
			if (StrUtils.fString(CUSTOMERTYPE).length() > 0)
				ht.put("CUSTTYPE", CUSTOMERTYPE);

			movQryList = movHisUtil.getWorkOrderSummaryList(ht, fdate, tdate, "OUTBOUNDPRODUCTREMARKS", PLANT,
					PRD_DESCRIP, CUSTNAME, "",UOM,POSSEARCH);

//			Boolean workSheetCreated = true;
			if (movQryList.size() > 0) {
				HSSFSheet sheet = null;
				HSSFCellStyle styleHeader = null;
				HSSFCellStyle dataStyle = null;
				HSSFCellStyle CompHeader = null;
//				HSSFCellStyle dataStyleSpecial = null;
				dataStyle = createDataStyle(wb);
//				dataStyleSpecial = createDataStyle(wb);
//				CreationHelper createHelper = wb.getCreationHelper();
				 sheet = wb.createSheet("Sheet"+SheetId);
				styleHeader = createStyleHeader(wb);
				CompHeader = createCompStyleHeader(wb);
				sheet = this.createWidthOutboundSummaryWithRemarks(sheet, type);
				sheet = this.createHeaderOutboundSummaryWithRemarks(sheet, styleHeader, type);
				sheet = this.createHeaderCompanyReports(sheet,CompHeader,reportType,PLANT);
				int index = 2;
//				CustomerBeanDAO customerBeanDAO = new CustomerBeanDAO();
//				String customertypedesc = "";//customerstatusid = "", customerstatusdesc = "", customertypeid = "", 

				for (int iCnt = 0; iCnt < movQryList.size(); iCnt++) {
					Map lineArr = (Map) movQryList.get(iCnt);
					int k = 0;

					HSSFRow row = sheet.createRow(index);

					HSSFCell cell = row.createCell(k++);
					cell.setCellValue(iCnt + 1);
					cell.setCellStyle(dataStyle);

					cell = row.createCell(k++);
					cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String) lineArr.get("dono"))));
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
						styleHeader = createStyleHeader(wb);
						CompHeader = createCompStyleHeader(wb);
						sheet = this.createWidthOutboundSummary(sheet, type);
						sheet = this.createHeaderOutboundSummary(sheet, styleHeader, type);
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


	//created by navas
	private HSSFWorkbook writeToExcelConsignmentOrderSummary(HttpServletRequest request, HttpServletResponse response,
	HSSFWorkbook wb) {
	HTReportUtil movHisUtil = new HTReportUtil();
	ArrayList movQryList = new ArrayList();
	int maxRowsPerSheet = 65535;
	String fdate = "", tdate = "";//, taxby = ""
	int SheetId =1;
	try {

	String PLANT = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
//	String baseCurrency = StrUtils.fString((String) request.getSession().getAttribute("BASE_CURRENCY")).trim();
	String ITEMNO = StrUtils.fString(request.getParameter("ITEM"));
	String PRD_DESCRIP = StrUtils.fString(request.getParameter("DESC"));
	String FROM_DATE = StrUtils.fString(request.getParameter("FROM_DATE"));
	String FROMWAREHOUSE = StrUtils.fString(request.getParameter("FROMWAREHOUSE"));
	String TOWAREHOUSE = StrUtils.fString(request.getParameter("TOWAREHOUSE"));
	String TO_DATE = StrUtils.fString(request.getParameter("TO_DATE"));
	String CUSTNAME = StrUtils.fString(request.getParameter("CUSTOMER"));
//	String CUSTOMERID = StrUtils.fString(request.getParameter("CUSTOMERID"));
	String ORDERNO = StrUtils.fString(request.getParameter("ORDERNO"));
	String JOBNO = StrUtils.fString(request.getParameter("JOBNO"));
	String PICKSTATUS = StrUtils.fString(request.getParameter("PICKSTATUS"));
	String ISSUESTATUS = StrUtils.fString(request.getParameter("ISSUESTATUS"));
	String ORDERTYPE = StrUtils.fString(request.getParameter("ORDERTYPE"));
	String PRD_TYPE_ID = StrUtils.fString(request.getParameter("PRD_TYPE_ID"));
	String PRD_BRAND_ID = StrUtils.fString(request.getParameter("PRD_BRAND_ID"));
	String PRD_DEPT_ID = StrUtils.fString(request.getParameter("PRD_DEPT_ID"));
	String PRD_CLS_ID = StrUtils.fString(request.getParameter("PRD_CLS_ID"));
	String statusID = StrUtils.fString(request.getParameter("STATUS_ID"));
	String type = StrUtils.fString(request.getParameter("DIRTYPE"));
	String SORT = StrUtils.fString(request.getParameter("SORT"));
	String EMPNO = StrUtils.fString(request.getParameter("EMP_NAME"));
	String CUSTOMERTYPE = StrUtils.fString(request.getParameter("CUSTOMER_TYPE_ID"));
//	String CURRENCYID = StrUtils.fString(request.getParameter("CURRENCYID"));
//	String CURRENCYDISPLAY = StrUtils.fString(request.getParameter("CURRENCYDISPLAY"));
	String INVOICENO = StrUtils.fString(request.getParameter("INVOICENO"));
	String UOM = StrUtils.fString(request.getParameter("UOM"));
	//imti start
	if(PICKSTATUS.equalsIgnoreCase("OPEN"))
	PICKSTATUS="N";
	else if(PICKSTATUS.equalsIgnoreCase("PARTIALLY PICKED"))
	PICKSTATUS="O";
	else if(PICKSTATUS.equalsIgnoreCase("PICKED"))
	PICKSTATUS="C";
	//imti end
	//imti start
	if(ISSUESTATUS.equalsIgnoreCase("OPEN"))
	ISSUESTATUS="N";
	else if(ISSUESTATUS.equalsIgnoreCase("PARTIALLY ISSUED"))
	ISSUESTATUS="O";
	else if(ISSUESTATUS.equalsIgnoreCase("ISSUED"))
	ISSUESTATUS="C";
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
//	taxby = _PlantMstDAO.getTaxBy(PLANT);
	PlantMstDAO plantMstDAO = new PlantMstDAO();
	String numberOfDecimal = plantMstDAO.getNumberOfDecimal(PLANT);


	if (StrUtils.fString(JOBNO).length() > 0)
	ht.put("JOBNUM", JOBNO);
	if (StrUtils.fString(ITEMNO).length() > 0)
	ht.put("ITEM", ITEMNO);
	if (StrUtils.fString(ORDERNO).length() > 0)
	ht.put("TONO", ORDERNO);
	if (StrUtils.fString(INVOICENO).length() > 0)
	ht.put("INVOICENO", INVOICENO);
	if (StrUtils.fString(FROMWAREHOUSE).length() > 0)
		ht.put("FROMWAREHOUSE", FROMWAREHOUSE);
		
	if (StrUtils.fString(TOWAREHOUSE).length() > 0)
		ht.put("TOWAREHOUSE", TOWAREHOUSE);
		
	if (StrUtils.fString(ORDERTYPE).length() > 0)
	ht.put("ORDERTYPE", ORDERTYPE);
	if (StrUtils.fString(ISSUESTATUS).length() > 0)
	ht.put("STATUS", ISSUESTATUS);
	if (StrUtils.fString(PICKSTATUS).length() > 0)
	ht.put("PickStaus", PICKSTATUS);
	if (StrUtils.fString(PRD_TYPE_ID).length() > 0)
	ht.put("ITEMTYPE", PRD_TYPE_ID);
	if (StrUtils.fString(PRD_BRAND_ID).length() > 0)
	ht.put("PRD_BRAND_ID", PRD_BRAND_ID);
	if (StrUtils.fString(PRD_CLS_ID).length() > 0)
	ht.put("PRD_CLS_ID", PRD_CLS_ID);
	if (StrUtils.fString(PRD_DEPT_ID).length() > 0)
		ht.put("PRD_DEPT_ID", PRD_DEPT_ID);
	if (StrUtils.fString(statusID).length() > 0)
	ht.put("STATUS_ID", statusID);
	if (StrUtils.fString(EMPNO).length() > 0)
	ht.put("EMPNO", EMPNO);
	if (StrUtils.fString(UOM).length() > 0)
	ht.put("UNITMO", UOM);
	if (StrUtils.fString(CUSTOMERTYPE).length() > 0)
	ht.put("CUSTTYPE", CUSTOMERTYPE);
	// if (taxby.equalsIgnoreCase("BYORDER")) {
	// movQryList = movHisUtil.getCustomerTOInvoiceIssueSummary(ht, fdate, tdate, type, PLANT, PRD_DESCRIP,
	// CUSTNAME, SORT);
	// } else {
	// movQryList = movHisUtil.getCustomerDOInvoiceIssueSummaryByProductGst(ht, fdate, tdate, type, PLANT,
	// PRD_DESCRIP, CUSTNAME, SORT);
	// }
	movQryList = movHisUtil.getCustomerTOInvoiceIssueSummary(ht, fdate, tdate, type, PLANT, PRD_DESCRIP,CUSTNAME, SORT);


//	Boolean workSheetCreated = true;
	if (movQryList.size() > 0) {
	HSSFSheet sheet = null;
	HSSFCellStyle styleHeader = null;
	HSSFCellStyle dataStyle = null;
	HSSFCellStyle dataStyleSpecial = null;
	HSSFCellStyle CompHeader = null;
	dataStyle = createDataStyle(wb);
	dataStyleSpecial = createDataStyle(wb);
	CreationHelper createHelper = wb.getCreationHelper();
	sheet = wb.createSheet("Sheet"+SheetId);
	styleHeader = createStyleHeader(wb);
	CompHeader = createCompStyleHeader(wb);
	sheet = this.createWidthOutboundSummary(sheet, type);
	sheet = this.createHeaderConsignmentSummary(sheet, styleHeader, type);
	sheet = this.createHeaderCompanyReports(sheet,CompHeader,reportType,PLANT);
	int index = 2;
	double unitprice = 0, Price = 0, tax = 0, PricewTax = 0;//orderPriceSubTot = 0, issPriceSubTot = 0, 
	float gstpercentage = 0;
//	String strDiffQty = "", deliverydateandtime = "";
//	DecimalFormat decformat = new DecimalFormat("#,##0.00");
	DecimalFormat decimalFormat = new DecimalFormat("#.#####");
	decimalFormat.setRoundingMode(RoundingMode.FLOOR);
	CustomerBeanDAO customerBeanDAO = new CustomerBeanDAO();
	String customertypeid = "", customertypedesc = "";//customerstatusid = "", customerstatusdesc = "", 

	for (int iCnt = 0; iCnt < movQryList.size(); iCnt++) {
	Map lineArr = (Map) movQryList.get(iCnt);
	int k = 0;
//	customerstatusid = customerBeanDAO.getCustomerStatusId(PLANT, (String) lineArr.get("custname"));
//	if (customerstatusid == null || customerstatusid.equals("")) {
//	customerstatusdesc = "";
//	} else {
//	customerstatusdesc = customerBeanDAO.getCustomerStatusDesc(PLANT, customerstatusid);
//	}
	customertypeid = customerBeanDAO.getCustomerTypeId(PLANT, (String) lineArr.get("custname"));
	if (customertypeid == null || customertypeid.equals("")) {
	customertypedesc = "";
	} else {
	customertypedesc = customerBeanDAO.getCustomerTypeDesc(PLANT, customertypeid);
	}


//	deliverydateandtime = (String) lineArr.get("deliverydate") + " "
//	+ (String) lineArr.get("deliverytime");

	String PricewTaxValue = decimalFormat.format(PricewTax);
	String taxValue = decimalFormat.format(tax);
	String PriceValue = decimalFormat.format(Price);
	String unitPriceValue = decimalFormat.format(unitprice);
	String gstpercentageValue = String.valueOf(gstpercentage);
	//String unitPriceValue = String.valueOf(unitprice);
	String qtyValue= (String) lineArr.get("qty");
	String qtyPickValue= (String) lineArr.get("qtypick");
	String qtyOrValue= (String) lineArr.get("qtyor");
	String qtyInvValue= (String) lineArr.get("qtyac"); //resvi
	/*float PricewTaxVal ="".equals(PricewTaxValue) ? 0.0f : Float.parseFloat(PricewTaxValue);*/
	/*float taxVal ="".equals(taxValue) ? 0.0f : Float.parseFloat(taxValue);*/
	/*float PriceVal ="".equals(PriceValue) ? 0.0f : Float.parseFloat(PriceValue);*/
	float gstpercentageVal ="".equals(gstpercentageValue) ? 0.0f : Float.parseFloat(gstpercentageValue);
	/*float unitPriceVal ="".equals(unitPriceValue) ? 0.0f : Float.parseFloat(unitPriceValue);*/
	float qtyVal ="".equals(qtyValue) ? 0.0f : Float.parseFloat(qtyValue);
	float qtyPickVal =qtyPickValue == null || "".equals(qtyPickValue) ? 0.0f : Float.parseFloat(qtyPickValue);
	float qtyOrVal ="".equals(qtyOrValue) ? 0.0f : Float.parseFloat(qtyOrValue);
	float qtyInvVal ="".equals(qtyInvValue) ? 0.0f : Float.parseFloat(qtyInvValue);  //resvi
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
	lnstat="PARTIALLY ISSUED";
	else if(lnstat.equalsIgnoreCase("C"))
	lnstat="ISSUED";
	//imti end

	//imti start
	String pickstatus= StrUtils.fString((String) lineArr.get("pickstatus"));
	String ordstats= (String)lineArr.get("ORDER_STATUS");
	if(pickstatus.equalsIgnoreCase("N"))
	{
	pickstatus="OPEN";
	if(ordstats.equalsIgnoreCase("Draft"))
	pickstatus="DRAFT";
	}
	else if(pickstatus.equalsIgnoreCase("O"))
	pickstatus="PARTIALLY PICKED";
	else if(pickstatus.equalsIgnoreCase("C"))
	pickstatus="PICKED";
	//imti end

	double unitPriceVal ="".equals(unitPriceValue) ? 0.0d : Double.parseDouble(unitPriceValue);
	double PriceVal ="".equals(PriceValue) ? 0.0d : Double.parseDouble(PriceValue);
	double taxVal ="".equals(taxValue) ? 0.0d : Double.parseDouble(taxValue);
	double PricewTaxVal ="".equals(PricewTaxValue) ? 0.0d : Double.parseDouble(PricewTaxValue);

	/*if(PricewTaxVal==0f){
	PricewTaxValue="0.000";
	}else{
	PricewTaxValue=PricewTaxValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
	}if(taxVal==0f){
	taxValue="0.000";
	}else{
	taxValue=taxValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
	}if(PriceVal==0f){
	PriceValue="0.00000";
	}else{
	PriceValue=PriceValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
	}*/if(gstpercentageVal==0f){
	gstpercentageValue="0.000";
	}else{
	gstpercentageValue=gstpercentageValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
	}/*if(unitPriceVal==0f){
	unitPriceValue="0.00000";
	}else{
	unitPriceValue=unitPriceValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
	}*/if(qtyVal==0f){
	qtyValue="0.000";
	}else{
	qtyValue=qtyValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
	}if(qtyPickVal==0f){
	qtyPickValue="0.000";
	}else{
	qtyPickValue=qtyPickValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
	}if(qtyOrVal==0f){
	qtyOrValue="0.000";
	}else{
	qtyOrValue=qtyOrValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
	}
	
	if(qtyInvVal==0f){
		qtyInvValue="0.000";
		}else{
		qtyInvValue=qtyInvValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
		}

	unitPriceValue = StrUtils.addZeroes(unitPriceVal, numberOfDecimal);
	PriceValue = StrUtils.addZeroes(PriceVal, numberOfDecimal);
	taxValue = StrUtils.addZeroes(taxVal, numberOfDecimal);
	PricewTaxValue = StrUtils.addZeroes(PricewTaxVal, numberOfDecimal);
	
	qtyInvValue = StrUtils.addZeroes(qtyInvVal, "3");
	qtyOrValue = StrUtils.addZeroes(qtyOrVal, "3");
	qtyPickValue = StrUtils.addZeroes(qtyPickVal, "3");
	qtyValue = StrUtils.addZeroes(qtyVal, "3");

	HSSFRow row = sheet.createRow(index);

	HSSFCell cell = row.createCell(k++);
	cell.setCellValue(iCnt + 1);
	cell.setCellStyle(dataStyle);

	cell = row.createCell(k++);
	cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String) lineArr.get("tono"))));
	cell.setCellStyle(dataStyle);


	cell = row.createCell(k++);
	cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String) lineArr.get("ordertype"))));
	cell.setCellStyle(dataStyle);



	cell = row.createCell(k++);
	cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String) lineArr.get("jobNum"))));
	cell.setCellStyle(dataStyle);


	cell = row.createCell(k++);
	cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String) lineArr.get("custname"))));
	cell.setCellStyle(dataStyle);

	cell = row.createCell(k++);
	cell.setCellValue(new HSSFRichTextString(StrUtils.fString(customertypedesc)));
	cell.setCellStyle(dataStyle);
	
	cell = row.createCell(k++);
	cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String) lineArr.get("fromlocation"))));
	cell.setCellStyle(dataStyle);

	cell = row.createCell(k++);
	cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String) lineArr.get("tolocation"))));
	cell.setCellStyle(dataStyle);

	cell = row.createCell(k++);
	cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String) lineArr.get("remarks"))));
	cell.setCellStyle(dataStyle);

	cell = row.createCell(k++);
	cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String) lineArr.get("remarks2"))));
	cell.setCellStyle(dataStyle);
	cell = row.createCell(k++);
	cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String) lineArr.get(""))));
	cell.setCellStyle(dataStyle);

	cell = row.createCell(k++);
	cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String) lineArr.get("item"))));
	cell.setCellStyle(dataStyle);

	cell = row.createCell(k++);
	cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String) lineArr.get("itemdesc"))));
	cell.setCellStyle(dataStyle);

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

	cell = row.createCell(k++);
	Calendar c = Calendar.getInstance();
	c.setTime(_shortDateformatter.parse(StrUtils.fString((String) lineArr.get("trandate"))));
	dataStyleSpecial.setDataFormat(createHelper.createDataFormat().getFormat("yyyy-MM-dd"));
	cell.setCellValue(c.getTime());
	cell.setCellStyle(dataStyleSpecial);




	cell = row.createCell(k++);
	cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String) lineArr.get("UOM"))));
	cell.setCellStyle(dataStyle);

	cell = row.createCell(k++);
	cell.setCellValue(qtyOrValue);
	cell.setCellStyle(dataStyle);


	cell = row.createCell(k++);
	cell.setCellValue(qtyPickValue);
	cell.setCellStyle(dataStyle);

	cell = row.createCell(k++);
	cell.setCellValue(qtyValue);
	cell.setCellStyle(dataStyle);

	cell = row.createCell(k++);
	cell.setCellValue(qtyInvValue);
	cell.setCellStyle(dataStyle);
	
	cell = row.createCell(k++);
	cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String) lineArr.get("empname"))));
	cell.setCellStyle(dataStyle);



	/*
	* if (type.equals("CONSIGNMENT")) { cell = row.createCell(k++);
	* cell.setCellValue(new HSSFRichTextString(pickstatus));
	* cell.setCellStyle(dataStyle);
	*
	* cell = row.createCell(k++); cell.setCellValue(new
	* HSSFRichTextString(lnstat)); cell.setCellStyle(dataStyle);
	*
	* }
	*/

	cell = row.createCell(k++);
	cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String) lineArr.get("SALES_LOCATION"))));
	cell.setCellStyle(dataStyle);

	cell = row.createCell(k++);
	cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String) lineArr.get("TAXTREATMENT"))));
	cell.setCellStyle(dataStyle);

	cell = row.createCell(k++);
	cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String) lineArr.get("users"))));
	cell.setCellStyle(dataStyle);


	index++;
	if ((index - 2) % maxRowsPerSheet == 0) {
	index = 2;
	SheetId++;
	sheet = wb.createSheet("Sheet"+SheetId);
	styleHeader = createStyleHeader(wb);
	CompHeader = createCompStyleHeader(wb);
	sheet = this.createWidthOutboundSummary(sheet, type);
	sheet = this.createHeaderConsignmentSummary(sheet, styleHeader, type);
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
	//end by navas
	
	private HSSFSheet createWidthOutboundSummary(HSSFSheet sheet, String type) {
		int i = 0;
		try {
			sheet.setColumnWidth(i++, 3500);
			sheet.setColumnWidth(i++, 3500);
			sheet.setColumnWidth(i++, 3500);
			sheet.setColumnWidth(i++, 3500);
			sheet.setColumnWidth(i++, 4200);
			sheet.setColumnWidth(i++, 4200);
			sheet.setColumnWidth(i++, 4200);
			sheet.setColumnWidth(i++, 4200);
			sheet.setColumnWidth(i++, 4200);
			sheet.setColumnWidth(i++, 4200);
			sheet.setColumnWidth(i++, 6500);
			sheet.setColumnWidth(i++, 4200);
			sheet.setColumnWidth(i++, 4200);
			sheet.setColumnWidth(i++, 4200);
			if (type.equals("OUTBOUND") || type.equals("OB_SUMMARY_ORD_WITH_PRICE")) {
				sheet.setColumnWidth(i++, 3500);
			}
			sheet.setColumnWidth(i++, 4200);
			sheet.setColumnWidth(i++, 3200);
			sheet.setColumnWidth(i++, 3200);
			sheet.setColumnWidth(i++, 3200);
			sheet.setColumnWidth(i++, 4000);

			if (type.equals("OB_SUMMARY_ORD_WITH_PRICE") || type.equals("OB_SUMMARY_ISS_WITH_PRICE")
					|| type.equals("OB_SUMMARY_ISS_WITH_AVGCOST")) {
				sheet.setColumnWidth(i++, 3200);
				sheet.setColumnWidth(i++, 3200);
				sheet.setColumnWidth(i++, 3200);
				sheet.setColumnWidth(i++, 3200);
				sheet.setColumnWidth(i++, 3200);

			}
			if (type.equals("OUTBOUND") || type.equals("OB_SUMMARY_ISSUE")) {
				sheet.setColumnWidth(i++, 3200);
				sheet.setColumnWidth(i++, 3200);
			}
			
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		}
		return sheet;
	}
	private HSSFSheet createWidthVoidSummary(HSSFSheet sheet) {
		int i = 0;
		try {
			sheet.setColumnWidth(i++, 3500);
			sheet.setColumnWidth(i++, 3500);
			sheet.setColumnWidth(i++, 3500);
			sheet.setColumnWidth(i++, 4200);
			sheet.setColumnWidth(i++, 3500);
			sheet.setColumnWidth(i++, 3500);
			sheet.setColumnWidth(i++, 3500);
			sheet.setColumnWidth(i++, 3500);
	
} catch (Exception e) {
	this.mLogger.exception(this.printLog, "", e);
}
return sheet;
}
	private HSSFSheet createWidthFOCSummary(HSSFSheet sheet) {
		int i = 0;
		try {
			sheet.setColumnWidth(i++, 3500);
			sheet.setColumnWidth(i++, 3500);
			sheet.setColumnWidth(i++, 3500);
			sheet.setColumnWidth(i++, 4200);
			sheet.setColumnWidth(i++, 4200);
			sheet.setColumnWidth(i++, 3500);
			
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		}
		return sheet;
	}
	private HSSFSheet createWidthPOSExpensesSummary(HSSFSheet sheet) {
		int i = 0;
		try {
			sheet.setColumnWidth(i++, 3500);
			sheet.setColumnWidth(i++, 3500);
			sheet.setColumnWidth(i++, 3500);
			sheet.setColumnWidth(i++, 4200);
			sheet.setColumnWidth(i++, 3500);
			
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		}
		return sheet;
	}

	private HSSFSheet createWidthOutboundSummaryWithRemarks(HSSFSheet sheet, String type) {
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
	
	private HSSFSheet createHeaderVoidSummary(HSSFSheet sheet, HSSFCellStyle styleHeader, String PLANT) {
		int k = 0;
		try {
			String ENABLE_POS =new PlantMstDAO().getispos(PLANT);
			 String ISTAXREGISTRED =new PlantMstDAO().getistaxregistred(PLANT);
			HSSFRow rowhead = sheet.createRow(1);

			HSSFCell cell = rowhead.createCell(k++);
			cell.setCellValue(new HSSFRichTextString("S/N"));
			cell.setCellStyle(styleHeader);

			cell = rowhead.createCell(k++);
			cell.setCellValue(new HSSFRichTextString("DATE"));
			cell.setCellStyle(styleHeader);
	
			cell = rowhead.createCell(k++);
			cell.setCellValue(new HSSFRichTextString("BILL"));
			cell.setCellStyle(styleHeader);

			cell = rowhead.createCell(k++);
			cell.setCellValue(new HSSFRichTextString("CUSTOMER"));
			cell.setCellStyle(styleHeader);

			cell = rowhead.createCell(k++);
			cell.setCellValue(new HSSFRichTextString("AMOUNT"));
			cell.setCellStyle(styleHeader);

			cell = rowhead.createCell(k++);
			cell.setCellValue(new HSSFRichTextString("DISCOUNT"));
			cell.setCellStyle(styleHeader);

			if(ENABLE_POS.equalsIgnoreCase("1") && ISTAXREGISTRED.equalsIgnoreCase("0")) {
			} else {
				cell = rowhead.createCell(k++);
				cell.setCellValue(new HSSFRichTextString("TAX"));
				cell.setCellStyle(styleHeader);
			}
			
			cell = rowhead.createCell(k++);
			cell.setCellValue(new HSSFRichTextString("TOTAL AMOUNT"));
			cell.setCellStyle(styleHeader);

} catch (Exception e) {
	this.mLogger.exception(this.printLog, "", e);
}
return sheet;
}
	
	private HSSFSheet createHeaderFOCSummary(HSSFSheet sheet, HSSFCellStyle styleHeader) {
		int k = 0;
		try {
			
			HSSFRow rowhead = sheet.createRow(1);
			
			HSSFCell cell = rowhead.createCell(k++);
			cell.setCellValue(new HSSFRichTextString("S/N"));
			cell.setCellStyle(styleHeader);
			
			cell = rowhead.createCell(k++);
			cell.setCellValue(new HSSFRichTextString("DATE"));
			cell.setCellStyle(styleHeader);
			
			cell = rowhead.createCell(k++);
			cell.setCellValue(new HSSFRichTextString("BILL"));
			cell.setCellStyle(styleHeader);
			
			cell = rowhead.createCell(k++);
			cell.setCellValue(new HSSFRichTextString("CUSTOMER"));
			cell.setCellStyle(styleHeader);
			
			cell = rowhead.createCell(k++);
			cell.setCellValue(new HSSFRichTextString("ITEM"));
			cell.setCellStyle(styleHeader);
			
			cell = rowhead.createCell(k++);
			cell.setCellValue(new HSSFRichTextString("QTY"));
			cell.setCellStyle(styleHeader);
			
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		}
		return sheet;
	}

	private HSSFSheet createHeaderPOSExpensesSummary(HSSFSheet sheet, HSSFCellStyle styleHeader) {
		int k = 0;
		try {
			
			HSSFRow rowhead = sheet.createRow(1);
			
			HSSFCell cell = rowhead.createCell(k++);
			cell.setCellValue(new HSSFRichTextString("S/N"));
			cell.setCellStyle(styleHeader);
			
			cell = rowhead.createCell(k++);
			cell.setCellValue(new HSSFRichTextString("DATE"));
			cell.setCellStyle(styleHeader);
			
			cell = rowhead.createCell(k++);
			cell.setCellValue(new HSSFRichTextString("ID"));
			cell.setCellStyle(styleHeader);
			
			cell = rowhead.createCell(k++);
			cell.setCellValue(new HSSFRichTextString("DESCRIPTION"));
			cell.setCellStyle(styleHeader);
			
			cell = rowhead.createCell(k++);
			cell.setCellValue(new HSSFRichTextString("AMOUNT"));
			cell.setCellStyle(styleHeader);
			
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		}
		return sheet;
	}

	private HSSFSheet createHeaderOutboundSummary(HSSFSheet sheet, HSSFCellStyle styleHeader, String type) {
		int k = 0;
		try {

			HSSFRow rowhead = sheet.createRow(1);

			HSSFCell cell = rowhead.createCell(k++);
			cell.setCellValue(new HSSFRichTextString("S/N"));
			cell.setCellStyle(styleHeader);

			cell = rowhead.createCell(k++);
			cell.setCellValue(new HSSFRichTextString("ORDER NO"));
			cell.setCellStyle(styleHeader);

			if (type.equals("OB_SUMMARY_ISS_WITH_PRICE")) {
				cell = rowhead.createCell(k++);
				cell.setCellValue(new HSSFRichTextString("GINO"));
				cell.setCellStyle(styleHeader);
			}
			cell = rowhead.createCell(k++);
			cell.setCellValue(new HSSFRichTextString("ORDER TYPE"));
			cell.setCellStyle(styleHeader);

			cell = rowhead.createCell(k++);
			cell.setCellValue(new HSSFRichTextString("REF NO"));
			cell.setCellStyle(styleHeader);

			cell = rowhead.createCell(k++);
			cell.setCellValue(new HSSFRichTextString("CUSTOMER NAME"));
			cell.setCellStyle(styleHeader);

			cell = rowhead.createCell(k++);
			cell.setCellValue(new HSSFRichTextString("CUSTOMER TYPE"));
			cell.setCellStyle(styleHeader);
			
			if (!type.equals("CONSIGNMENT_SUMMARY_ISSUE")) {
				if (!type.equals("CONSIGNMENT_SUMMARY_ISS_WITH_PRICE")) {
					cell = rowhead.createCell(k++);
					cell.setCellValue(new HSSFRichTextString("Customer Status"));
					cell.setCellStyle(styleHeader);
					}			
			} else {  
				cell = rowhead.createCell(k++);
				cell.setCellValue(new HSSFRichTextString("CUSTOMER STATUS"));
				cell.setCellStyle(styleHeader);
			}
			if (type.equals("CONSIGNMENT_SUMMARY_ISSUE")||type.equals("CONSIGNMENT_SUMMARY_ISS_WITH_PRICE")) {
			
			cell = rowhead.createCell(k++);
			cell.setCellValue(new HSSFRichTextString("FROM LOCATION"));
			cell.setCellStyle(styleHeader);

			cell = rowhead.createCell(k++);
			cell.setCellValue(new HSSFRichTextString("TO LOCATION"));
			cell.setCellStyle(styleHeader);
			
			}
			
			cell = rowhead.createCell(k++);
			cell.setCellValue(new HSSFRichTextString("REMARKS1"));
			cell.setCellStyle(styleHeader);

			cell = rowhead.createCell(k++);
			cell.setCellValue(new HSSFRichTextString("REMARKS2"));
			cell.setCellStyle(styleHeader);

			cell = rowhead.createCell(k++);
			cell.setCellValue(new HSSFRichTextString("PRODUCT ID"));
			cell.setCellStyle(styleHeader);

			cell = rowhead.createCell(k++);
			cell.setCellValue(new HSSFRichTextString("DESCRIPTION"));
			cell.setCellStyle(styleHeader);

			if (type.equals("OUTBOUND") || type.equals("OB_SUMMARY_ORD_WITH_PRICE")) {
				cell = rowhead.createCell(k++);
				cell.setCellValue(new HSSFRichTextString("DETAIL DESCRIPTION"));
				cell.setCellStyle(styleHeader);
			}

			cell = rowhead.createCell(k++);
			cell.setCellValue(new HSSFRichTextString("DEPARTMENT"));
			cell.setCellStyle(styleHeader);
			
			cell = rowhead.createCell(k++);
			cell.setCellValue(new HSSFRichTextString("CATEGORY"));
			cell.setCellStyle(styleHeader);

			cell = rowhead.createCell(k++);
			cell.setCellValue(new HSSFRichTextString("SUB CATEGORY"));
			cell.setCellStyle(styleHeader);

			cell = rowhead.createCell(k++);
			cell.setCellValue(new HSSFRichTextString("BRAND"));
			cell.setCellStyle(styleHeader);

			cell = rowhead.createCell(k++);
			cell.setCellValue(new HSSFRichTextString("DATE"));
			cell.setCellStyle(styleHeader);

			if (type.equals("OUTBOUND") || type.equals("OB_SUMMARY_ORD_WITH_PRICE")) {
				cell = rowhead.createCell(k++);
				cell.setCellValue(new HSSFRichTextString("DELIVERY DATE&TIME"));
				cell.setCellStyle(styleHeader);
			}
			
			cell = rowhead.createCell(k++);
			cell.setCellValue(new HSSFRichTextString("UOM"));
			cell.setCellStyle(styleHeader);

			cell = rowhead.createCell(k++);
			cell.setCellValue(new HSSFRichTextString("ORDER QTY"));
			cell.setCellStyle(styleHeader);

			cell = rowhead.createCell(k++);
			cell.setCellValue(new HSSFRichTextString("PICK QTY"));
			cell.setCellStyle(styleHeader);

			cell = rowhead.createCell(k++);
			cell.setCellValue(new HSSFRichTextString("ISSUE QTY"));
			cell.setCellStyle(styleHeader);
			
			if (type.equals("CONSIGNMENT_SUMMARY_ISSUE")||type.equals("CONSIGNMENT_SUMMARY_ISS_WITH_PRICE")) {
			cell = rowhead.createCell(k++);
			cell.setCellValue(new HSSFRichTextString("INVOICED QTY"));
			cell.setCellStyle(styleHeader);
			}

			cell = rowhead.createCell(k++);
			cell.setCellValue(new HSSFRichTextString("EMPLOYEE"));
			cell.setCellStyle(styleHeader);

			if (type.equals("OB_SUMMARY_ORD_WITH_PRICE") || type.equals("OB_SUMMARY_ISS_WITH_PRICE")||type.equals("CONSIGNMENT_SUMMARY_ISS_WITH_PRICE")
					|| type.equals("OB_SUMMARY_ISS_WITH_AVGCOST")) {
				if (type.equals("OB_SUMMARY_ISS_WITH_AVGCOST")) {
					cell = rowhead.createCell(k++);
					cell.setCellValue(new HSSFRichTextString("AVG COST"));
					cell.setCellStyle(styleHeader);
				} else {
					cell = rowhead.createCell(k++);
					cell.setCellValue(new HSSFRichTextString("UNIT PRICE"));
					cell.setCellStyle(styleHeader);
				}
				cell = rowhead.createCell(k++);
				cell.setCellValue(new HSSFRichTextString("TAX%"));
				cell.setCellStyle(styleHeader);

				if (type.equals("OB_SUMMARY_ORD_WITH_PRICE")) {
					cell = rowhead.createCell(k++);
					cell.setCellValue(new HSSFRichTextString("ORDER PRICE"));
					cell.setCellStyle(styleHeader);
				} else if (type.equals("OB_SUMMARY_ISS_WITH_AVGCOST")) {
					cell = rowhead.createCell(k++);
					cell.setCellValue(new HSSFRichTextString("RECV COST"));
					cell.setCellStyle(styleHeader);
				} else {
					cell = rowhead.createCell(k++);
					cell.setCellValue(new HSSFRichTextString("ISSUE PRICE"));
					cell.setCellStyle(styleHeader);
				}
				cell = rowhead.createCell(k++);
				cell.setCellValue(new HSSFRichTextString("TAX"));
				cell.setCellStyle(styleHeader);

				cell = rowhead.createCell(k++);
				cell.setCellValue(new HSSFRichTextString("TOTAL"));
				cell.setCellStyle(styleHeader);
			}

			if (type.equals("OUTBOUND")) {
				cell = rowhead.createCell(k++);
				cell.setCellValue(new HSSFRichTextString("PICK STATUS"));
				cell.setCellStyle(styleHeader);

				cell = rowhead.createCell(k++);
				cell.setCellValue(new HSSFRichTextString("ISSUE STATUS"));
				cell.setCellStyle(styleHeader);
			}
			
			cell = rowhead.createCell(k++);
			cell.setCellValue(new HSSFRichTextString("SALES LOCATION"));
			cell.setCellStyle(styleHeader);
			
			cell = rowhead.createCell(k++);
			cell.setCellValue(new HSSFRichTextString("TAX TREATMENT"));
			cell.setCellStyle(styleHeader);
			
			   cell = rowhead.createCell(k++);
			   cell.setCellValue(new HSSFRichTextString("USER"));
			   cell.setCellStyle(styleHeader);

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		}
		return sheet;
	}

	private HSSFSheet createHeaderOutboundSummary(HSSFSheet sheet, HSSFCellStyle styleHeader, String type, String Sort, String PLANT) {
		
		int k = 0;
		try {

			HSSFRow rowhead = sheet.createRow(1);

			HSSFCell cell = rowhead.createCell(k++);
			cell.setCellValue(new HSSFRichTextString("S/N"));
			cell.setCellStyle(styleHeader);

			cell = rowhead.createCell(k++);
			cell.setCellValue(new HSSFRichTextString("ORDER NO"));
			cell.setCellStyle(styleHeader);

			if (type.equals("OB_SUMMARY_ISS_WITH_PRICE")) {
				cell = rowhead.createCell(k++);
				cell.setCellValue(new HSSFRichTextString("GINO"));
				cell.setCellStyle(styleHeader);
			}
			cell = rowhead.createCell(k++);
			cell.setCellValue(new HSSFRichTextString("ORDER TYPE"));
			cell.setCellStyle(styleHeader);

			cell = rowhead.createCell(k++);
			cell.setCellValue(new HSSFRichTextString("REF NO"));
			cell.setCellStyle(styleHeader);

			cell = rowhead.createCell(k++);
			cell.setCellValue(new HSSFRichTextString("CUSTOMER NAME"));
			cell.setCellStyle(styleHeader);
			
			if(Sort.equalsIgnoreCase("LOCATION")) {
				cell = rowhead.createCell(k++);
				cell.setCellValue(new HSSFRichTextString("LOCATION"));
				cell.setCellStyle(styleHeader);
			}
			
			
			cell = rowhead.createCell(k++);
			cell.setCellValue(new HSSFRichTextString("CUSTOMER TYPE"));
			cell.setCellStyle(styleHeader);
			
			if (!type.equals("CONSIGNMENT_SUMMARY_ISSUE")) {
				if (!type.equals("CONSIGNMENT_SUMMARY_ISS_WITH_PRICE")) {
					cell = rowhead.createCell(k++);
					cell.setCellValue(new HSSFRichTextString("CUSTOMER STATUS"));
					cell.setCellStyle(styleHeader);
					}			
			} else {  
				cell = rowhead.createCell(k++);
				cell.setCellValue(new HSSFRichTextString("CUSTOMER STATUS"));
				cell.setCellStyle(styleHeader);
			}
			if (type.equals("CONSIGNMENT_SUMMARY_ISSUE")||type.equals("CONSIGNMENT_SUMMARY_ISS_WITH_PRICE")) {
			
			cell = rowhead.createCell(k++);
			cell.setCellValue(new HSSFRichTextString("FROM LOCATION"));
			cell.setCellStyle(styleHeader);

			cell = rowhead.createCell(k++);
			cell.setCellValue(new HSSFRichTextString("TO LOCATION"));
			cell.setCellStyle(styleHeader);
			
			}
			
			cell = rowhead.createCell(k++);
			cell.setCellValue(new HSSFRichTextString("REMARKS1"));
			cell.setCellStyle(styleHeader);

			cell = rowhead.createCell(k++);
			cell.setCellValue(new HSSFRichTextString("REMARKS2"));
			cell.setCellStyle(styleHeader);

			cell = rowhead.createCell(k++);
			cell.setCellValue(new HSSFRichTextString("PRODUCT ID"));
			cell.setCellStyle(styleHeader);

			cell = rowhead.createCell(k++);
			cell.setCellValue(new HSSFRichTextString("DESCRIPTION"));
			cell.setCellStyle(styleHeader);

			if (type.equals("OUTBOUND") || type.equals("OB_SUMMARY_ORD_WITH_PRICE")) {
				cell = rowhead.createCell(k++);
				cell.setCellValue(new HSSFRichTextString("Detail Description"));
				cell.setCellStyle(styleHeader);
			}

			cell = rowhead.createCell(k++);
			cell.setCellValue(new HSSFRichTextString("DEPARTMENT"));
			cell.setCellStyle(styleHeader);

			
			cell = rowhead.createCell(k++);
			cell.setCellValue(new HSSFRichTextString("CATEGORY"));
			cell.setCellStyle(styleHeader);

			cell = rowhead.createCell(k++);
			cell.setCellValue(new HSSFRichTextString("SUB CATEGORY"));
			cell.setCellStyle(styleHeader);

			cell = rowhead.createCell(k++);
			cell.setCellValue(new HSSFRichTextString("BRAND"));
			cell.setCellStyle(styleHeader);

			cell = rowhead.createCell(k++);
			cell.setCellValue(new HSSFRichTextString("DATE"));
			cell.setCellStyle(styleHeader);

			if (type.equals("OUTBOUND") || type.equals("OB_SUMMARY_ORD_WITH_PRICE")) {
				cell = rowhead.createCell(k++);
				cell.setCellValue(new HSSFRichTextString("DELIVERY DATE&TIME"));
				cell.setCellStyle(styleHeader);
			}
			
			cell = rowhead.createCell(k++);
			cell.setCellValue(new HSSFRichTextString("UOM"));
			cell.setCellStyle(styleHeader);

			cell = rowhead.createCell(k++);
			cell.setCellValue(new HSSFRichTextString("ORDER QTY"));
			cell.setCellStyle(styleHeader);

			cell = rowhead.createCell(k++);
			cell.setCellValue(new HSSFRichTextString("PICK QTY"));
			cell.setCellStyle(styleHeader);

			cell = rowhead.createCell(k++);
			cell.setCellValue(new HSSFRichTextString("ISSUE QTY"));
			cell.setCellStyle(styleHeader);
			
			if (type.equals("CONSIGNMENT_SUMMARY_ISSUE")||type.equals("CONSIGNMENT_SUMMARY_ISS_WITH_PRICE")) {
			cell = rowhead.createCell(k++);
			cell.setCellValue(new HSSFRichTextString("INVOICED QTY"));
			cell.setCellStyle(styleHeader);
			}

			cell = rowhead.createCell(k++);
			cell.setCellValue(new HSSFRichTextString("EMPLOYEE"));
			cell.setCellStyle(styleHeader);

			if (type.equals("OB_SUMMARY_ORD_WITH_PRICE") || type.equals("OB_SUMMARY_ISS_WITH_PRICE")||type.equals("CONSIGNMENT_SUMMARY_ISS_WITH_PRICE")
					|| type.equals("OB_SUMMARY_ISS_WITH_AVGCOST")) {

				if (type.equals("OB_SUMMARY_ISS_WITH_AVGCOST")) {
					cell = rowhead.createCell(k++);
					cell.setCellValue(new HSSFRichTextString("AVG COST"));
					cell.setCellStyle(styleHeader);
				} else {
					cell = rowhead.createCell(k++);
					cell.setCellValue(new HSSFRichTextString("UNIT PRICE"));
					cell.setCellStyle(styleHeader);
				}
				cell = rowhead.createCell(k++);
				cell.setCellValue(new HSSFRichTextString("TAX%"));
				cell.setCellStyle(styleHeader);

				if (type.equals("OB_SUMMARY_ORD_WITH_PRICE")) {
					cell = rowhead.createCell(k++);
					cell.setCellValue(new HSSFRichTextString("ORDER PRICE"));
					cell.setCellStyle(styleHeader);
				} else if (type.equals("OB_SUMMARY_ISS_WITH_AVGCOST")) {
					cell = rowhead.createCell(k++);
					cell.setCellValue(new HSSFRichTextString("RECV COST"));
					cell.setCellStyle(styleHeader);
				} else {
					cell = rowhead.createCell(k++);
					cell.setCellValue(new HSSFRichTextString("ISSUE PRICE"));
					cell.setCellStyle(styleHeader);
				}
				cell = rowhead.createCell(k++);
				cell.setCellValue(new HSSFRichTextString("TAX"));
				cell.setCellStyle(styleHeader);

				cell = rowhead.createCell(k++);
				cell.setCellValue(new HSSFRichTextString("TOTAL"));
				cell.setCellStyle(styleHeader);
			}

			if (type.equals("OUTBOUND")) {
				
				PlantMstUtil plantmstutil = new PlantMstUtil();
				List viewlistQry = plantmstutil.getPlantMstDetails(PLANT);
			    Map map = (Map) viewlistQry.get(0);
			    String DELIVERYAPP = StrUtils.fString((String)map.get("ISRIDERRAPP"));
			    
				cell = rowhead.createCell(k++);
				cell.setCellValue(new HSSFRichTextString("PICK STATUS"));
				cell.setCellStyle(styleHeader);

				if(DELIVERYAPP.equals("1")){
			    
				cell = rowhead.createCell(k++);
				cell.setCellValue(new HSSFRichTextString("ISSUE STATUS"));
				cell.setCellStyle(styleHeader);
				}else {
					cell = rowhead.createCell(k++);
					cell.setCellValue(new HSSFRichTextString("STATUS"));
					cell.setCellStyle(styleHeader);
				}
				
//				cell = rowhead.createCell(k++);
//				cell.setCellValue(new HSSFRichTextString("DELIVERY STATUS"));
//				cell.setCellStyle(styleHeader);
			}
			if ((type.equals("OB_SUMMARY_ISSUE"))|| (type.equals("OB_SUMMARY_ISS_WITH_PRICE"))) {
			PlantMstUtil plantmstutil = new PlantMstUtil();
			List viewlistQry = plantmstutil.getPlantMstDetails(PLANT);
		    Map map = (Map) viewlistQry.get(0);
		    String DELIVERYAPP = StrUtils.fString((String)map.get("ISRIDERRAPP"));
			
			cell = rowhead.createCell(k++);
			cell.setCellValue(new HSSFRichTextString("PICK STATUS"));
			cell.setCellStyle(styleHeader);
			
			if(DELIVERYAPP.equals("1")){
			    
			cell = rowhead.createCell(k++);
			cell.setCellValue(new HSSFRichTextString("ISSUE STATUS"));
			cell.setCellStyle(styleHeader);
			}else {
				cell = rowhead.createCell(k++);
				cell.setCellValue(new HSSFRichTextString("STATUS"));
				cell.setCellStyle(styleHeader);
			}
//			cell = rowhead.createCell(k++);
//			cell.setCellValue(new HSSFRichTextString("DELIVERY STATUS"));
//			cell.setCellStyle(styleHeader);
			}
			
			cell = rowhead.createCell(k++);
			cell.setCellValue(new HSSFRichTextString("SALES LOCATION"));
			cell.setCellStyle(styleHeader);
			
			cell = rowhead.createCell(k++);
			cell.setCellValue(new HSSFRichTextString("TAX TREATMENT"));
			cell.setCellStyle(styleHeader);
			
			   cell = rowhead.createCell(k++);
			   cell.setCellValue(new HSSFRichTextString("USER"));
			   cell.setCellStyle(styleHeader);

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		}
		return sheet;
	}
	
	private HSSFSheet createHeaderConsignmentSummary(HSSFSheet sheet, HSSFCellStyle styleHeader, String type) {
		int k = 0;
		try {

			HSSFRow rowhead = sheet.createRow(1);

			HSSFCell cell = rowhead.createCell(k++);
			cell.setCellValue(new HSSFRichTextString("S/N"));
			cell.setCellStyle(styleHeader);

			cell = rowhead.createCell(k++);
			cell.setCellValue(new HSSFRichTextString("ORDER NO"));
			cell.setCellStyle(styleHeader);

			cell = rowhead.createCell(k++);
			cell.setCellValue(new HSSFRichTextString("GINO"));
			cell.setCellStyle(styleHeader);

			cell = rowhead.createCell(k++);
			cell.setCellValue(new HSSFRichTextString("ORDER TYPE"));
			cell.setCellStyle(styleHeader);

			cell = rowhead.createCell(k++);
			cell.setCellValue(new HSSFRichTextString("REF NO"));
			cell.setCellStyle(styleHeader);

			cell = rowhead.createCell(k++);
			cell.setCellValue(new HSSFRichTextString("CUSTOMER NAME"));
			cell.setCellStyle(styleHeader);

			cell = rowhead.createCell(k++);
			cell.setCellValue(new HSSFRichTextString("CUSTOMER TYPE"));
			cell.setCellStyle(styleHeader);
			
			
			
			
			cell = rowhead.createCell(k++);
			cell.setCellValue(new HSSFRichTextString("FROM LOCATION"));
			cell.setCellStyle(styleHeader);

			cell = rowhead.createCell(k++);
			cell.setCellValue(new HSSFRichTextString("TO LOCATION"));
			cell.setCellStyle(styleHeader);
			
			
			
			cell = rowhead.createCell(k++);
			cell.setCellValue(new HSSFRichTextString("REMARKS1"));
			cell.setCellStyle(styleHeader);

			cell = rowhead.createCell(k++);
			cell.setCellValue(new HSSFRichTextString("REMARKS2"));
			cell.setCellStyle(styleHeader);

			cell = rowhead.createCell(k++);
			cell.setCellValue(new HSSFRichTextString("PRODUCT ID"));
			cell.setCellStyle(styleHeader);

			cell = rowhead.createCell(k++);
			cell.setCellValue(new HSSFRichTextString("DESCRIPTION"));
			cell.setCellStyle(styleHeader);

			cell = rowhead.createCell(k++);
			cell.setCellValue(new HSSFRichTextString("DEPARTMENT"));
			cell.setCellStyle(styleHeader);
			
			cell = rowhead.createCell(k++);
			cell.setCellValue(new HSSFRichTextString("CATEGORY"));
			cell.setCellStyle(styleHeader);

			cell = rowhead.createCell(k++);
			cell.setCellValue(new HSSFRichTextString("SUB CATEGORY"));
			cell.setCellStyle(styleHeader);

			cell = rowhead.createCell(k++);
			cell.setCellValue(new HSSFRichTextString("BRAND"));
			cell.setCellStyle(styleHeader);

			cell = rowhead.createCell(k++);
			cell.setCellValue(new HSSFRichTextString("DATE"));
			cell.setCellStyle(styleHeader);
			
			cell = rowhead.createCell(k++);
			cell.setCellValue(new HSSFRichTextString("UOM"));
			cell.setCellStyle(styleHeader);

			cell = rowhead.createCell(k++);
			cell.setCellValue(new HSSFRichTextString("ORDER QTY"));
			cell.setCellStyle(styleHeader);

			cell = rowhead.createCell(k++);
			cell.setCellValue(new HSSFRichTextString("PICK QTY"));
			cell.setCellStyle(styleHeader);

			cell = rowhead.createCell(k++);
			cell.setCellValue(new HSSFRichTextString("ISSUE QTY"));
			cell.setCellStyle(styleHeader);
			
			
			cell = rowhead.createCell(k++);
			cell.setCellValue(new HSSFRichTextString("INVOICED QTY"));
			cell.setCellStyle(styleHeader);			

			cell = rowhead.createCell(k++);
			cell.setCellValue(new HSSFRichTextString("EMPLOYEE"));
			cell.setCellStyle(styleHeader);

			if (type.equals("CONSIGNMENT_SUMMARY_ISS_WITH_PRICE")) {
				
					cell = rowhead.createCell(k++);
					cell.setCellValue(new HSSFRichTextString("UNIT PRICE"));
					cell.setCellStyle(styleHeader);
				
				cell = rowhead.createCell(k++);
				cell.setCellValue(new HSSFRichTextString("TAX%"));
				cell.setCellStyle(styleHeader);

				
					cell = rowhead.createCell(k++);
					cell.setCellValue(new HSSFRichTextString("ISSUE PRICE"));
					cell.setCellStyle(styleHeader);
				
				cell = rowhead.createCell(k++);
				cell.setCellValue(new HSSFRichTextString("TAX"));
				cell.setCellStyle(styleHeader);

				cell = rowhead.createCell(k++);
				cell.setCellValue(new HSSFRichTextString("TOTAL"));
				cell.setCellStyle(styleHeader);
			}
			if(!type.equals("CONSIGNMENT_SUMMARY_ISS_WITH_PRICE")) {
			cell = rowhead.createCell(k++);
			cell.setCellValue(new HSSFRichTextString("SALES LOCATION"));
			cell.setCellStyle(styleHeader);
			
			cell = rowhead.createCell(k++);
			cell.setCellValue(new HSSFRichTextString("TAX TREATMENT"));
			cell.setCellStyle(styleHeader);
			}
			   cell = rowhead.createCell(k++);
			   cell.setCellValue(new HSSFRichTextString("USER"));
			   cell.setCellStyle(styleHeader);

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		}
		return sheet;
	}

	
	// ---End Added by Bruhan on May 21 2014, Description:To open outbound order
	// summary in excel powershell format

	private HSSFSheet createHeaderOutboundSummaryWithRemarks(HSSFSheet sheet, HSSFCellStyle styleHeader, String type) {
		int k = 0;
		try {

			HSSFRow rowhead = sheet.createRow(1);

			HSSFCell cell = rowhead.createCell(k++);
			cell.setCellValue(new HSSFRichTextString("S/N"));
			cell.setCellStyle(styleHeader);

			cell = rowhead.createCell(k++);
			cell.setCellValue(new HSSFRichTextString("ORDER NO"));
			cell.setCellStyle(styleHeader);

			cell = rowhead.createCell(k++);
			cell.setCellValue(new HSSFRichTextString("PRODUCT ID"));
			cell.setCellStyle(styleHeader);

			cell = rowhead.createCell(k++);
			cell.setCellValue(new HSSFRichTextString("REMARKS"));
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

	// End code by Bruhan for outbound order product export to excel on 25 April
	// 2013

	public void viewMultipleDOReport(HttpServletRequest request, HttpServletResponse response)
			throws IOException, Exception {
		viewDOReport(request, response);
	}

	public void viewMultipleDOInvoiceReport(HttpServletRequest request, HttpServletResponse response)
			throws IOException, Exception {
		viewInvoiceReport(request, response);
	}

	private HSSFWorkbook writeToExcelOutboundSalessmry(HttpServletRequest request, HttpServletResponse response,
			HSSFWorkbook wb) {
		String plant = "";
		ArrayList listQry = new ArrayList();
		ShipHisDAO shipdao = new ShipHisDAO();
		int SheetId =1;
		try {
			HttpSession session = request.getSession();
			plant = session.getAttribute("PLANT").toString();
			String ITEM = StrUtils.fString(request.getParameter("ITEM"));
			String CUSTOMER = StrUtils.fString(request.getParameter("CUSTOMER"));
			String LOC = StrUtils.fString(request.getParameter("LOC"));
			String SORT = StrUtils.fString(request.getParameter("SORT"));
			String PRD_DESCRIP = StrUtils.fString(request.getParameter("PRD_DESCRIP"));
			String FROM_DATE = StrUtils.fString(request.getParameter("FROM_DATE"));
			String TO_DATE = StrUtils.fString(request.getParameter("TO_DATE"));
			String RADIOSEARCH = StrUtils.fString(request.getParameter("RADIOSEARCH"));
			String POSSEARCH = StrUtils.fString(request.getParameter("POSSEARCH"));
			String reportType ="";
			listQry = shipdao.getproductcustomersaleswithzeroqty(plant, ITEM, PRD_DESCRIP, CUSTOMER, LOC, SORT, FROM_DATE,
					TO_DATE,POSSEARCH,"");
			PlantMstDAO plantMstDAO = new PlantMstDAO();
			String numberOfDecimal = plantMstDAO.getNumberOfDecimal(plant);

			if (listQry.size() > 0) {
				HSSFSheet sheet = null;
				HSSFCellStyle styleHeader = null;
				HSSFCellStyle dataStyle = null;
				HSSFCellStyle CompHeader = null;
				sheet = wb.createSheet("Sheet"+SheetId);
				styleHeader = createStyleHeader(wb);
				CompHeader = createCompStyleHeader(wb);
				sheet = this.createWidthOutboundSalessmry(sheet);
				sheet = this.createHeaderOutboundSalessmry(sheet, styleHeader, SORT);
				sheet = this.createHeaderCompanyReports(sheet,CompHeader,reportType,plant);

				for (int iCnt = 0; iCnt < listQry.size(); iCnt++) {
					Map lineArr = (Map) listQry.get(iCnt);
					int k = 0;

					double TOTPRICE = Double.parseDouble((String) lineArr.get("TOTPRICE"));
					TOTPRICE = StrUtils.RoundDB(TOTPRICE, Integer.parseInt(DbBean.NOOFDECIMALPTSFORCURRENCY));
					
					String totQtyValue =(String) lineArr.get("TOTQTY");
					String totPriceValue =(String) lineArr.get("TOTPRICE");
					
					float totQtyVal ="".equals(totQtyValue) ? 0.0f :  Float.parseFloat(totQtyValue);
					/*float totPriceVal ="".equals(totPriceValue) ? 0.0f :  Float.parseFloat(totPriceValue);*/
					double totPriceVal ="".equals(totPriceValue) ? 0.0d :  Double.parseDouble(totPriceValue);
		    		
		    		if(totQtyVal==0f){
		    			totQtyValue="0.000";
		    		}else{
		    			totQtyValue=totQtyValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
		    		}/*if(totPriceVal==0f){
		    			totPriceValue="0.00000";
		    		}else{
		    			totPriceValue=totPriceValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
		    		}*/
		    		totQtyValue = StrUtils.addZeroes(totQtyVal, "3");
		    		totPriceValue = StrUtils.addZeroes(totPriceVal, numberOfDecimal);

					dataStyle = createDataStyle(wb);

					if(RADIOSEARCH.equalsIgnoreCase("1")) {
					
					HSSFRow row = sheet.createRow((short) iCnt + 2);
					HSSFCell cell = row.createCell(k++);
					cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String) lineArr.get("ID"))));
					cell.setCellStyle(dataStyle);

					cell = row.createCell(k++);
					cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String) lineArr.get("NAME"))));
					cell.setCellStyle(dataStyle);
					
					cell = row.createCell(k++);
					cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String) lineArr.get("UNITPRICE"))));
					cell.setCellStyle(dataStyle);
					
					cell = row.createCell(k++);
					cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String) lineArr.get("COST"))));
					cell.setCellStyle(dataStyle);

					cell = row.createCell(k++);
					cell.setCellValue(new HSSFRichTextString(totQtyValue));
					cell.setCellStyle(dataStyle);

					cell = row.createCell(k++);
					cell.setCellValue(totPriceValue);
					cell.setCellStyle(dataStyle);
					
					} else if(RADIOSEARCH.equalsIgnoreCase("2") && totQtyVal!=0) {
						
					HSSFRow row = sheet.createRow((short) iCnt + 2);
					HSSFCell cell = row.createCell(k++);
					cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String) lineArr.get("ID"))));
					cell.setCellStyle(dataStyle);
					
					cell = row.createCell(k++);
					cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String) lineArr.get("NAME"))));
					cell.setCellStyle(dataStyle);
					
					cell = row.createCell(k++);
					cell.setCellValue(new HSSFRichTextString(totQtyValue));
					cell.setCellStyle(dataStyle);
					
					cell = row.createCell(k++);
					cell.setCellValue(totPriceValue);
					cell.setCellStyle(dataStyle);
					
					} else if(RADIOSEARCH.equalsIgnoreCase("3") && totQtyVal==0) {
						
					HSSFRow row = sheet.createRow((short) iCnt + 2);
					HSSFCell cell = row.createCell(k++);
					cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String) lineArr.get("ID"))));
					cell.setCellStyle(dataStyle);
					
					cell = row.createCell(k++);
					cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String) lineArr.get("NAME"))));
					cell.setCellStyle(dataStyle);
					
					cell = row.createCell(k++);
					cell.setCellValue(new HSSFRichTextString(totQtyValue));
					cell.setCellStyle(dataStyle);
					
					cell = row.createCell(k++);
					cell.setCellValue(totPriceValue);
					cell.setCellStyle(dataStyle);
					}

				}

			} else if (listQry.size() < 1) {

				System.out.println("No Records Found To List");
			}

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		}

		return wb;
	}

	private HSSFSheet createHeaderOutboundSalessmry(HSSFSheet sheet, HSSFCellStyle styleHeader, String SORT) {
		int k = 0;
		try {

			HSSFRow rowhead = sheet.createRow((short) 1);
			HSSFCell cell = rowhead.createCell(k++);
			if (SORT.equalsIgnoreCase("PRODUCT")) {
				cell.setCellValue(new HSSFRichTextString("PRODUCT ID"));
			} else {
				cell.setCellValue(new HSSFRichTextString("CUSTOMER ID"));
			}
			cell.setCellStyle(styleHeader);

			cell = rowhead.createCell(k++);
			if (SORT.equalsIgnoreCase("PRODUCT")) {
				cell.setCellValue(new HSSFRichTextString("DESCRIPTION"));
			} else {
				cell.setCellValue(new HSSFRichTextString("CUSTOMER NAME"));
			}
			cell.setCellStyle(styleHeader);

			cell = rowhead.createCell(k++);
			cell.setCellValue(new HSSFRichTextString("SALES QTY"));
			cell.setCellStyle(styleHeader);

			cell = rowhead.createCell(k++);
			cell.setCellValue(new HSSFRichTextString("TOTAL AMOUNT"));
			cell.setCellStyle(styleHeader);

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		}
		return sheet;
	}

	private HSSFSheet createWidthOutboundSalessmry(HSSFSheet sheet) {

		try {
			sheet.setColumnWidth(0, 8000);
			sheet.setColumnWidth(1, 8000);
			sheet.setColumnWidth(2, 4000);
			sheet.setColumnWidth(3, 4000);

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		}
		return sheet;
	}

	private HSSFSheet createHeaderCompanyReports(HSSFSheet sheet, HSSFCellStyle styleHeader,String reportType,String PLANT){
		int k = 0;
		try{
			String CNAME = "", CZIP = "", CCOUNTRY = "", CSTATE="";//CADD4 = "", CADD3 = "", CTEL = "",CADD2 = "", CADD1 = "", CRCBNO = "", CEMAIL = "", COL1="",CHPNO = "",, COL2="" , CONTACTNAME = "", CFAX = ""
			PlantMstUtil pmUtil = new PlantMstUtil();
			ArrayList listQry = pmUtil.getPlantMstDetails(PLANT);
			for (int i = 0; i < listQry.size(); i++) {
				Map map = (Map) listQry.get(i);
				CNAME = (String) map.get("PLNTDESC");
				/*CADD1 = (String) map.get("ADD1");
				CADD2 = (String) map.get("ADD2");
				COL1=CADD1+" "+CADD2;
				CADD3 = (String) map.get("ADD3");
				CADD4 = (String) map.get("ADD4");
				if((CADD3+CADD4).length()>1)
					COL1=COL1+", "+(CADD3+" "+CADD4);*/
				
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
//		            insertFlag= 
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
	
	private JSONObject getPreviousOrderDetails(HttpServletRequest request) {
		JSONObject resultJson = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        JSONArray jsonArrayErr = new JSONArray();
        
        DOUtil _DOUtil = new DOUtil();
        _DOUtil.setmLogger(mLogger);
        
        try {
        	String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
		    String custCode = StrUtils.fString(request.getParameter("CUSTCODE")).trim();
		    String item = StrUtils.fString(request.getParameter("ITEM")).trim();
		    String rows = StrUtils.fString(request.getParameter("ROWS")).trim();
		    Hashtable ht=new Hashtable();
//		    String extCond="";
		    ht.put("PLANT",plant);
		    ht.put("ITEM",item);
		    ht.put("CUSTCODE",custCode);
		    if(rows.equalsIgnoreCase(""))
		    	rows = "1";
		    List listQry = _DOUtil.getPreviousOrderDetails(ht,rows);
		    if (listQry.size() > 0) {
		    	 for(int i =0; i<listQry.size(); i++) {		   
				  Map m=(Map)listQry.get(i);
				  String pono = (String)m.get("DONO");
				  String custcode = (String)m.get("CustCode");
				  String custName = (String)m.get("CNAME");
				  item = (String)m.get("ITEM");
				  String collectionDate = (String)m.get("CollectionDate");
				  String unitPrice = (String)m.get("UNITPRICE");
				  JSONObject resultJsonInt = new JSONObject();
				  resultJsonInt.put("DONO", pono);
				  resultJsonInt.put("CUSTCODE", custcode);
				  resultJsonInt.put("CNAME", custName);				  
				  resultJsonInt.put("ITEM", item);
				  resultJsonInt.put("COLLECTIONDATE", collectionDate);
				  resultJsonInt.put("UNITPRICE", unitPrice);		  
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
}
