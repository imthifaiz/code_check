package com.track.servlet;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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

import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperRunManager;

import com.track.constants.IConstants;
import com.track.constants.IDBConstants;
import com.track.constants.MLoggerConstant;
import com.track.constants.TransactionConstants;
import com.track.dao.CustomerBeanDAO;
import com.track.dao.DoDetDAO;
import com.track.dao.DoHdrDAO;
import com.track.dao.EstDetDAO;
import com.track.dao.EstHdrDAO;
import com.track.dao.ItemMstDAO;
import com.track.dao.LoanDetDAO;
import com.track.dao.LoanHdrDAO;
import com.track.dao.MasterDAO;
import com.track.dao.MovHisDAO;
import com.track.dao.PlantMstDAO;
import com.track.dao.PoHdrDAO;
import com.track.dao.TblControlDAO;
import com.track.dao.ToDetDAO;
import com.track.dao.ToHdrDAO;
import com.track.db.util.CustUtil;
import com.track.db.util.DOUtil;
import com.track.db.util.HTReportUtil;
import com.track.db.util.ItemMstUtil;
import com.track.db.util.LoanUtil;
import com.track.db.util.LocUtil;
import com.track.db.util.MasterUtil;
import com.track.db.util.POUtil;
import com.track.db.util.PlantMstUtil;
import com.track.db.util.TblControlUtil;
import com.track.db.util.UserLocUtil;
import com.track.gates.DbBean;
import com.track.util.DateUtils;
import com.track.util.IMLogger;
import com.track.util.MLogger;
import com.track.util.StrUtils;

public class LoanOrderServlet extends HttpServlet implements IMLogger {
	private boolean printLog = MLoggerConstant.LoanOrderServlet_PRINTPLANTMASTERLOG;
	private boolean printInfo = MLoggerConstant.LoanOrderServlet_PRINTPLANTMASTERINFO;
	private static final long serialVersionUID = 8702864634734939856L;

	private static final String CONTENT_TYPE = "text/html; charset=windows-1252";

	String action = "";
	MasterDAO _MasterDAO = null;
	MasterUtil _masterUtil = null;
	POUtil _POUtil = null;
	LoanUtil _loanUtil = null;
	StrUtils _StrUtils = null;
	StrUtils strUtils = null;
	TblControlDAO _TblControlDAO = null;
	HTReportUtil htutil = null;

	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		_POUtil = new POUtil();
		htutil = new HTReportUtil();
		_loanUtil = new LoanUtil();
		_StrUtils = new StrUtils();
		strUtils = new StrUtils();
		 _TblControlDAO = new TblControlDAO();
		 _MasterDAO = new MasterDAO();
		  _masterUtil = new MasterUtil();
	}

 /*********************Modification History***********************************
		 * Bruhan,Nov 10 2014,To update Add Products,Add product and UpdateLoandet allow loan order to amend,add products once a order was processed
		 * 
		 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		try {

			action = _StrUtils.fString(request.getParameter("Submit")).trim();
			String plant = StrUtils.fString(
					(String) request.getSession().getAttribute("PLANT")).trim();
			String userName = StrUtils.fString(
					(String) request.getSession().getAttribute("LOGIN_USER"))
					.trim();
			this.setMapDataToLogger(this.populateMapData(plant, userName));

			_POUtil.setmLogger(mLogger);
			_loanUtil.setmLogger(mLogger);
			if (this.printInfo) {
				diaplayInfoLogs(request);
			}
			String test = _StrUtils.fString(request.getParameter("DONO"))
					.trim();
			String statusValue = StrUtils.fString(
					request.getParameter("statusValue")).trim();
			HttpSession session = request.getSession();

			String rflag = _StrUtils.fString(
					(String) session.getAttribute("RFLAG")).trim();
			if (action.equalsIgnoreCase("View")) {

				String dono = _StrUtils.fString(request.getParameter("DONO"));

				String fieldDesc = "<tr><td> Please enter serach condition and press GO</td></tr>";

				ArrayList al = new ArrayList();
				Hashtable htCond = new Hashtable();
				Map m = new HashMap();

				if (dono.length() > 0) {

					htCond.put("PLANT", plant);
					htCond.put("DONO", dono);

					String query = " a.ORDNO as pono,a.LOC as frLoc,a.LOC1 as toLoc,a.custCode,a.EXPIRYDATEFORMAT,a.DELIVERYDATEFORMAT,a.DAYS,a.jobNum,a.custName,a.personInCharge,a.contactNum,a.address,a.collectionDate,a.collectionTime,a.remark1,a.remark2,isnull(b.name,'') as contactname,isnull(b.telno,'') as telno,isnull(b.email,'') email,isnull(b.addr1,'') add1,isnull(b.addr2,'') add2,isnull(b.addr3,'') add3,isnull(b.remarks,'') remarks,isnull(b.addr4,'') add4,isnull(b.country,'') country,isnull(b.zip,'') zip,isnull(a.expiredate,'') expiredate,isnull(a.ordertype,'') ordertype,isnull(a.currencyid,'') currencyid,isnull(a.RENTAL_GST,'') rentalgst,isnull(a.EMPNO,'') employeeid,isnull(a.DELIVERYDATE,'') deliverydate,isnull(a.PAYMENTTYPE,'') paymenttype,isnull(a.SHIPPINGCUSTOMER,'') shippingcustomer,isnull(a.ORDERDISCOUNT,'') orderdiscount,isnull(a.SHIPPINGCOST,'') shippingcost";
					al = _loanUtil.getLoanAssigneeHdrDetails(query, htCond, "");
					if (al.size() > 0) {
						m = (Map) al.get(0);
						fieldDesc = _loanUtil.listLoanDET(dono, plant, rflag);

					} else {
						fieldDesc = "<tr><td colspan=\"8\" align=\"center\">No Records Available</td></tr>";
					}

				}
				if (rflag.equals("1")) {
					request.getSession().setAttribute("podetVal", m);
					request.getSession().setAttribute("RESULT", fieldDesc);
					response.sendRedirect("jsp/CreateLoanOrder.jsp?DONO="
							+ dono + "&action=GO");
				} else if (rflag.equals("2")) {
					request.getSession().setAttribute("podetVal", m);
					request.getSession().setAttribute("RESULT", fieldDesc);
					response.sendRedirect("jsp/InboundSummary.jsp?DONO=" + dono
							+ "&action=GO");
				} else if (rflag.equals("3")) {
					request.getSession().setAttribute("podetVal", m);
					request.getSession().setAttribute("RESULT", fieldDesc);
					response.sendRedirect("jsp/CreateLoanOrder.jsp?DONO="
							+ dono + "&action=GO");
				} else if (rflag.equals("4")) {
					request.getSession().setAttribute("podetVal", m);
					request.getSession().setAttribute("RESULT", fieldDesc);
					response.sendRedirect("jsp/maintLoanOrder.jsp?statusValue="
							+ statusValue + "&DONO=" + dono + "&action=GO");
				} else {
					request.getSession().setAttribute("podetVal", m);
					request.getSession().setAttribute("RESULT", fieldDesc);
					response.sendRedirect("jsp/CreateLoanOrder.jsp?DONO="
							+ dono + "&action=GO");
				}

			}

			else if (action.equalsIgnoreCase("Updateloandet")) {
				try {
					
					 LoanHdrDAO _LoanHdrDAO = new LoanHdrDAO();
               		 LoanDetDAO _LoanDetDAO = new LoanDetDAO();
					String dono = "", dolno = "", item = "", itemDesc = "", result = "", fieldDesc = "";
					dono = StrUtils.fString(request.getParameter("DONO")).trim();
					dolno = StrUtils.fString(request.getParameter("DOLNNO")).trim();
					item = StrUtils.fString(request.getParameter("ITEM")).trim();
					itemDesc = StrUtils.fString(request.getParameter("DESC")).trim();
					String qtyor = StrUtils.removeFormat(StrUtils.fString(request.getParameter("qtyor")).trim());
					String price = StrUtils.removeFormat(StrUtils.fString(request.getParameter("RENTALPRICE")).trim());
					String RENTALPRICERD= StrUtils.fString(request.getParameter("RENTALPRICERD")).trim();
					String uom= StrUtils.fString(request.getParameter("UOM")).trim();
					if(price.equalsIgnoreCase(""))
						price="0";
					if(RENTALPRICERD.equalsIgnoreCase("0"))						
						RENTALPRICERD=price;
					else if(RENTALPRICERD.equalsIgnoreCase(""))
						RENTALPRICERD=price;
				
					Hashtable htUpdatedodet = new Hashtable();
					htUpdatedodet.clear();
					htUpdatedodet.put(IDBConstants.PLANT, plant);
					htUpdatedodet.put(IDBConstants.LOANDET_ORDNO, dono);
					htUpdatedodet.put(IDBConstants.LOANDET_ORDLNNO, dolno);
					
					String q = "isnull(RENTALPRICE, 0) as RENTALPRICE, isnull(qtyor,0)as qtyor";
					ArrayList aldet = _LoanDetDAO.selectLoanDet(q, htUpdatedodet, " plant <> ''");
					Map m = (Map) aldet.get(0);
					String qtyOld = (String) m.get("qtyor");
					String priceOld = (String) m.get("RENTALPRICE");
					//String uomOld = (String) m.get("UOM");
					if (!qtyOld.equals(qtyor) || !priceOld.equals(price)) {
						String hdrUpdate = " SET RENTALPRICE = " + RENTALPRICERD +", QTYOR="+ qtyor ;
						Hashtable htCondition = new Hashtable();
						htCondition.put("PLANT", plant);
						htCondition.put("ORDNO", dono);
						htCondition.put("ORDLNNO", dolno);
						
						try {
						
							boolean flag =	new LoanDetDAO().update(hdrUpdate, htCondition, "");
						
							
					if (flag) {
								
								//	Call Accounting module for update item : End
								Hashtable htRemarksDel = new Hashtable();
								htRemarksDel.put(IDBConstants.PLANT, plant);
								htRemarksDel.put(IDBConstants.LOANDET_ORDNO, dono);
								htRemarksDel.put(IDBConstants.LOANDET_ORDLNNO, dolno);
								htRemarksDel.put(IDBConstants.LOANDET_ITEM, item);
								flag = _LoanDetDAO.deleteLoanMultiRemarks(htRemarksDel);
							}
							
							String strMovHisRemarks = "";
							if (flag) {
								DateUtils dateUtils = new DateUtils();
								String DYNAMIC_REMARKS_SIZE = StrUtils.fString(request.getParameter("DYNAMIC_REMARKS_SIZE"));
								int remarks_count = Integer.parseInt(DYNAMIC_REMARKS_SIZE);
								String productDORemarks = "";
								for (int index = 0; index < remarks_count; index++) {
									productDORemarks = StrUtils.fString(request.getParameter("PRDREMARKS" + "_" + index));
									strMovHisRemarks = strMovHisRemarks + "," + productDORemarks;
									Hashtable htRemarks = new Hashtable();
									htRemarks.put(IDBConstants.PLANT, plant);
									htRemarks.put(IDBConstants.LOANDET_ORDNO, dono);
									htRemarks.put(IDBConstants.LOANDET_ORDLNNO, dolno);
									htRemarks.put(IDBConstants.LOANDET_ITEM, item);
									htRemarks.put(IDBConstants.REMARKS, strUtils.InsertQuotes(productDORemarks));
									htRemarks.put(IDBConstants.CREATED_AT, dateUtils.getDateTime());
									htRemarks.put(IDBConstants.CREATED_BY, userName);
									flag = _loanUtil.saveLoanMultiRemarks(htRemarks);
								}
							}
							
							MovHisDAO movhisDao = new MovHisDAO();
							Hashtable htmovHis = new Hashtable();
							movhisDao.setmLogger(mLogger);
							htmovHis.clear();
							htmovHis.put(IDBConstants.PLANT, plant);
							htmovHis.put("DIRTYPE", "RENTAL_ORDER_UPDATE_PRODUCT");
							htmovHis.put(IDBConstants.ITEM, item);
							htmovHis.put(IDBConstants.MOVHIS_ORDNUM, dono);
							htmovHis.put(IDBConstants.MOVHIS_ORDLNO, dolno);
							htmovHis.put("QTY", qtyor);
							//htmovHis.put("REMARKS", price + "," + strUtils.InsertQuotes(strMovHisRemarks));
							htmovHis.put(IDBConstants.CREATED_BY, userName);
							htmovHis.put("MOVTID", "");
							htmovHis.put("RECID", "");
							htmovHis.put(IDBConstants.TRAN_DATE,
									new DateUtils().getDateinyyyy_mm_dd(new DateUtils().getDate()));
							htmovHis.put(IDBConstants.CREATED_AT, new DateUtils().getDateTime());
							flag = movhisDao.insertIntoMovHis(htmovHis);
							if (flag){
							ArrayList al = new ArrayList();
							Hashtable htCond = new Hashtable();
							Map a = new HashMap();
							if (dono.length() > 0) {
								htCond.put("PLANT", plant);
								htCond.put("DONO", dono);
									String query = " a.ORDNO as pono,a.LOC as frLoc,a.LOC1 as toLoc,a.custCode,a.jobNum,a.custName,a.personInCharge,a.contactNum,a.address,a.collectionDate,a.collectionTime,a.remark1,a.remark2,isnull(b.name,'') as contactname,isnull(b.telno,'') as telno,isnull(b.email,'') email,isnull(b.addr1,'') add1,isnull(b.addr2,'') add2,isnull(b.addr3,'') add3,isnull(b.remarks,'') remarks,isnull(b.addr4,'') add4,isnull(b.country,'') country,isnull(b.zip,'') zip,isnull(a.expiredate,'') expiredate,isnull(a.ordertype,'') ordertype,isnull(a.currencyid,'') currencyid,isnull(a.RENTAL_GST,'') rentalgst,isnull(a.EMPNO,'') employeeid,isnull(a.DELIVERYDATE,'') deliverydate,isnull(a.PAYMENTTYPE,'') paymenttype,isnull(a.SHIPPINGCUSTOMER,'') shippingcustomer,isnull(a.ORDERDISCOUNT,'') orderdiscount,isnull(a.SHIPPINGCOST,'') shippingcost,ISNULL(a.EXPIRYDATEFORMAT,'') EXPIRYDATEFORMAT,isnull(a.DELIVERYDATEFORMAT,'') DELIVERYDATEFORMAT,isnull(a.DAYS,'') DAYS";
								//String query = " a.ORDNO as pono,a.LOC as frLoc,a.LOC1 as toLoc,a.custName,a.jobNum,a.custName,a.personInCharge,a.contactNum,a.address,a.collectionDate,a.collectionTime,a.remark1,a.remark2,isnull(b.name,'') as contactname,isnull(b.telno,'') as telno,isnull(b.email,'') email,isnull(b.addr1,'') add1,isnull(b.addr2,'') add2,isnull(b.addr3,'') add3,isnull(b.remarks,'') remarks,isnull(b.addr4,'') add4,isnull(b.country,'') country,isnull(b.zip,'') zip,isnull(expiredate,'') expiredate";
								al = _loanUtil.getLoanAssigneeHdrDetails(query,
										htCond, "");

								if (al.size() > 0) {
									a = (Map) al.get(0);
									fieldDesc = _loanUtil.listLoanDET(dono, plant,
											rflag);

								} else {

									fieldDesc = "<tr><td colspan=\"8\" align=\"center\">No Records Available</td></tr>";

								}
							
							}
							
								/*result = "<font color=\"green\">Rental Order Updated Successfully</font><br><br><center>";
								
								result = result + " <br><br><center> "
										+ "<input type=\"button\" value=\"Back\" onClick=\"window.location.href='javascript:history.back()'\"> ";
										
								
								result = "<br><h3>" + result;
								request.getSession().setAttribute("RESULT", result);
								response.sendRedirect("jsp/displayResult2User.jsp");*/
								request.getSession().setAttribute("podetVal", a);
								request.getSession().setAttribute("RESULT", fieldDesc);
								if (rflag.equals("1")) {
									response.sendRedirect("jsp/CreateLoanOrder.jsp?DONO="
											+ dono + "&action=GO");
								} else {
									response.sendRedirect("jsp/maintLoanOrder.jsp?DONO="
											+ dono + "&action=GO");
								}
							}
								else{
								result = "<font color=\"red\"> Error in Adding Product ID   <br><br><center>"
										+ "<input type=\"button\" value=\"Back\" onClick=\"window.location.href='javascript:history.back()'\"> ";

							result = "Rental Order<br><h3>" + result;
								}
						}catch(Exception e) {
							if (e == null || !"Unable to update!".equals(e.getMessage())) {
								throw new Exception("Unable to update", e);
							}
							
						}
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
			
			else if (action.equalsIgnoreCase("print")) {

				String dono = _StrUtils.fString(request.getParameter("DONO"));
				String fieldDesc = "<tr><td> Please enter serach condition and press GO</td></tr>";

				ArrayList al = new ArrayList();
				Hashtable htCond = new Hashtable();
				Map m = new HashMap();

				if (dono.length() > 0) {

					htCond.put("PLANT", plant);
					htCond.put("DONO", dono);

					String query = " a.ORDNO as pono,a.LOC as frLoc,a.LOC1 as toLoc,a.custName,a.jobNum,a.custName,a.personInCharge,a.contactNum,a.address,a.collectionDate,a.collectionTime,a.remark1,a.remark2,isnull(b.name,'') as contactname,isnull(b.telno,'') as telno,isnull(b.email,'') email,isnull(b.addr1,'') add1,isnull(b.addr2,'') add2,isnull(b.addr3,'') add3,isnull(b.remarks,'') remarks,isnull(b.addr4,'') add4,isnull(b.country,'') country,isnull(b.zip,'') zip";
					al = _loanUtil.getLoanAssigneeHdrDetails(query, htCond, "");
					if (al.size() > 0) {
						m = (Map) al.get(0);

						fieldDesc = _loanUtil.listLoanDETToPrint(dono, plant,
								rflag);

					} else {

						fieldDesc = "<tr><td colspan=\"8\" align=\"center\">No Records Available</td></tr>";

					}

				}
				request.getSession().setAttribute("podetVal", m);
				request.getSession().setAttribute("RESULT", fieldDesc);
				response.sendRedirect("jsp/CreateLoanOrder_print.jsp?DONO="
						+ dono + "&action=View");

			} else if (action.equalsIgnoreCase("Auto-Generate")) {
				response.sendRedirect("jsp/CreateLoanOrder.jsp?action=Auto-Generate");
			} else if (action.equalsIgnoreCase("Print Loan Order")) {
				viewLoanOrderReport(request, response);
			}
			 else if (action.equalsIgnoreCase("Print Loan Order With Price")) {
					viewLoanOrderInvoiceReport(request, response);
				}
			 else if (action.equalsIgnoreCase("Export To Excel")) {
					HSSFWorkbook wb = new HSSFWorkbook();
					wb = this.writeToExcel(request, response, wb);
					ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
					wb.write(outByteStream);
					byte[] outArray = outByteStream.toByteArray();
					response.setContentType("application/ms-excel");
					response.setContentLength(outArray.length);
					response.setHeader("Expires:", "0"); // eliminates browser caching
					response.setHeader("Content-Disposition", "attachment; filename=Rentalorder.xls");
					OutputStream outStream = response.getOutputStream();
					outStream.write(outArray);
					outStream.flush();

				}
			// viewLoanOrderReport
			else if (action.equalsIgnoreCase("Add Products")) {

				String result = "";
				ArrayList al = new ArrayList();
				Hashtable htCond = new Hashtable();
				String dono = _StrUtils.fString(request.getParameter("DONO"))
						.trim();
				String cname = _StrUtils.fString(
						request.getParameter("CUST_NAME")).trim();

				htCond.put("PLANT", plant);
				htCond.put("ORDNO", dono);

				String query = "ORDNO,custCode,custName,jobNum";

				al = _loanUtil.getLoanHdrList(query, htCond, "");

				if (al.size() < 1) {
					result = "<font color=\"red\"> Please Save Rental Order first before adding product  <br><br><center>"
							+ "<input type=\"button\" value=\"Back\" onClick=\"window.location.href='javascript:history.back()'\"> ";
					result = "<br><h3>" + result;
					request.getSession().setAttribute("RESULT", result);
					response.sendRedirect("jsp/displayResult2User.jsp");

				} else {
					if(DbBean.LO_MODIFICATION.equals("Y"))
					{
						Map m = (Map) al.get(0);
						dono = (String) m.get("ORDNO");
						String jobNum = (String) m.get("jobNum");
						String custCode = (String) m.get("custCode");
						String custName = (String) m.get("custName");

						response.sendRedirect("jsp/createLOANDET.jsp?Submit=add&DONO="
								+ dono
								+ "&JOB_NUM="
								+ jobNum
								+ "&CUST_CODE="
								+ custCode
								+ "&CUST_NAME="
								+ StrUtils.replaceCharacters2Send(custName));
					}
					else
					{
						_loanUtil.isOpenLoanOrder(plant, dono);
						Map m = (Map) al.get(0);
						dono = (String) m.get("ORDNO");
						String jobNum = (String) m.get("jobNum");
						String custCode = (String) m.get("custCode");
						String custName = (String) m.get("custName");

						response.sendRedirect("jsp/createLOANDET.jsp?Submit=add&DONO="
								+ dono
								+ "&JOB_NUM="
								+ jobNum
								+ "&CUST_CODE="
								+ custCode
								+ "&CUST_NAME="
								+ StrUtils.replaceCharacters2Send(custName));
					}
					
				}
			}
			else if (action.equalsIgnoreCase("UPDATE")) {

				String result = updateLoanHdr(request, response);

				request.getSession().setAttribute("RESULT", result);
				response.sendRedirect("jsp/displayResult2User.jsp");
			}

			else if (action.equalsIgnoreCase("SAVE")) {

				String result = "";
				result = SaveLoanHdr(request, response);
				request.getSession().setAttribute("RESULT", result);
				response.sendRedirect("jsp/displayResult2User.jsp");
			} else if (action.equalsIgnoreCase("REMOVE_LO")) {
				String User_id = StrUtils.fString(
						request.getParameter("LOGIN_USER")).trim();
				String dono = StrUtils.fString(request.getParameter("DONO"))
						.trim();
				Hashtable htLoHrd = new Hashtable();
				htLoHrd.put(IDBConstants.PLANT, plant);
				htLoHrd.put("ORDNO", dono);
				boolean isValidOrder = new LoanHdrDAO().isExisit(htLoHrd,"");
				boolean isOrderInProgress = new LoanDetDAO().isExisit(htLoHrd, " LNSTAT in ('O' ,'C') and ITEM NOT IN(SELECT ITEM FROM ["+plant+"_ITEMMST] where NONSTKFLAG='Y')");
				if(isValidOrder){
					if (!isOrderInProgress) {
						Boolean value = this._loanUtil.removeRow(plant, dono,
								User_id);
						if (value) {
							response.sendRedirect("/track/loanorderservlet?statusValue=100&DONO="
									+ dono + "&Submit=View&RFLAG=4");
						} else {
							response.sendRedirect("/track/loanorderservlet?statusValue=97&DONO="
									+ dono + "&Submit=View&RFLAG=4");
						}
					} else {
						response.sendRedirect("/track/loanorderservlet?statusValue=98&DONO="
								+ dono + "&Submit=View&RFLAG=4");
					}
				} else {
					response.sendRedirect("/track/loanorderservlet?statusValue=99&DONO="
							+ dono + "&Submit=View&RFLAG=4");
				}
			} else if (action.equalsIgnoreCase("Delete")) {
				try {
					boolean flag = false;
					String fieldDesc = "<tr><td> Please enter serach condition and press GO</td></tr>";

					String User_id = _StrUtils.fString(
							request.getParameter("LOGIN_USER")).trim();
					String ordno = _StrUtils.fString(
							request.getParameter("DONO")).trim();
					String ordlno = _StrUtils.fString(
							request.getParameter("DOLNNO")).trim();
					String item = _StrUtils.fString(
							request.getParameter("ITEM")).trim();
					Hashtable htdet = new Hashtable();
					htdet.put(IDBConstants.PLANT, plant);
					htdet.put(IDBConstants.LOANDET_ORDNO, ordno);
					htdet.put(IDBConstants.LOANDET_ORDLNNO, ordlno);
					htdet.put(IDBConstants.LOANDET_ITEM, item);
					htdet.put(IDBConstants.CREATED_BY, userName);
					flag = _loanUtil.deleteLoanDetLineDetails(htdet);
					if (flag) {
					//LOANHDR Update
           		     String updateLoanHdr = "";
                 	 boolean updateHdrFlag  =false;
               		 boolean isExitFlag=false;
               		 LoanHdrDAO _LoanHdrDAO = new LoanHdrDAO();
               		 LoanDetDAO _LoanDetDAO = new LoanDetDAO();
               		 Hashtable htConditoHdr = new Hashtable();
    				 htConditoHdr.put("PLANT", plant);
    				 htConditoHdr.put("ordno", ordno);
    				     
	                    isExitFlag = _LoanDetDAO.isExisit(htConditoHdr,	"pickStatus in ('C') and qtyis <> qtyrc   ");
				    	if (isExitFlag)
				    	{
				    		updateLoanHdr = "set  Status='O'";
							updateHdrFlag=true;
				    	}
						else
						{
							isExitFlag = _LoanDetDAO.isExisit(htConditoHdr,	"pickStatus in ('C') and qtyis = qtyrc ");
							if (isExitFlag)
	 				    	{
								updateLoanHdr = "set  Status='C' ";
								updateHdrFlag=true;
	 				    	}
				    	}
				    	
				    	if(updateHdrFlag==true){
	        				 flag =_LoanHdrDAO.update(updateLoanHdr, htConditoHdr, "");
	           			}
				    	//LOANHDR Update
				    	
						ArrayList al = new ArrayList();
						Hashtable htCond = new Hashtable();
						Map m = new HashMap();
						if (ordno.length() > 0) {
							htCond.put("PLANT", plant);
							htCond.put("DONO", ordno);
								String query = " a.ORDNO as pono,a.LOC as frLoc,a.LOC1 as toLoc,a.custCode,a.jobNum,a.custName,a.personInCharge,a.contactNum,a.address,a.collectionDate,a.collectionTime,a.remark1,a.remark2,isnull(b.name,'') as contactname,isnull(b.telno,'') as telno,isnull(b.email,'') email,isnull(b.addr1,'') add1,isnull(b.addr2,'') add2,isnull(b.addr3,'') add3,isnull(b.remarks,'') remarks,isnull(b.addr4,'') add4,isnull(b.country,'') country,isnull(b.zip,'') zip,isnull(a.expiredate,'') expiredate,isnull(a.ordertype,'') ordertype,isnull(a.currencyid,'') currencyid,isnull(a.RENTAL_GST,'') rentalgst,isnull(a.EMPNO,'') employeeid,isnull(a.DELIVERYDATE,'') deliverydate,isnull(a.PAYMENTTYPE,'') paymenttype,isnull(a.SHIPPINGCUSTOMER,'') shippingcustomer,isnull(a.ORDERDISCOUNT,'') orderdiscount,isnull(a.EXPIRYDATEFORMAT,'') EXPIRYDATEFORMAT,isnull(a.DELIVERYDATEFORMAT,'') DELIVERYDATEFORMAT,isnull(a.SHIPPINGCOST,'') shippingcost";
							//String query = " a.ORDNO as pono,a.LOC as frLoc,a.LOC1 as toLoc,a.custName,a.jobNum,a.custName,a.personInCharge,a.contactNum,a.address,a.collectionDate,a.collectionTime,a.remark1,a.remark2,isnull(b.name,'') as contactname,isnull(b.telno,'') as telno,isnull(b.email,'') email,isnull(b.addr1,'') add1,isnull(b.addr2,'') add2,isnull(b.addr3,'') add3,isnull(b.remarks,'') remarks,isnull(b.addr4,'') add4,isnull(b.country,'') country,isnull(b.zip,'') zip,isnull(expiredate,'') expiredate";
							al = _loanUtil.getLoanAssigneeHdrDetails(query,
									htCond, "");

							if (al.size() > 0) {
								m = (Map) al.get(0);
								fieldDesc = _loanUtil.listLoanDET(ordno, plant,
										rflag);

							} else {

								fieldDesc = "<tr><td colspan=\"8\" align=\"center\">No Records Available</td></tr>";

							}

						}
						request.getSession().setAttribute("podetVal", m);
						request.getSession().setAttribute("RESULT", fieldDesc);
						if (rflag.equals("1")) {
							response.sendRedirect("jsp/CreateLoanOrder.jsp?DONO="
									+ ordno + "&action=GO");
						} else {
							response.sendRedirect("jsp/maintLoanOrder.jsp?DONO="
									+ ordno + "&action=GO");
						}
					} else {
						throw new Exception(
								"Product ID Not Deleted Successfully");
					}
				} catch (Exception ex) {

					this.mLogger.exception(this.printLog, "", ex);
					String result = "<font class = " + IConstants.FAILED_COLOR
							+ ">Error : " + ex.getMessage() + "</font>";
					result = result
							+ " <br><br><center> "
							+ "<input type=\"button\" value=\"Back\" onClick=\"window.location.href='javascript:history.back()'\"> ";
					result = "Rental Order<br><h3>" + result;
					request.getSession().setAttribute("RESULT", result);
					response.sendRedirect("jsp/displayResult2User.jsp");

				}

			} else if (action.equalsIgnoreCase("Add Product")) {

				MovHisDAO movHisDao = new MovHisDAO();
				movHisDao.setmLogger(mLogger);
				DateUtils dateUtils = new DateUtils();
				String orderNo = "", custCode = "", custName = "", jobNum = "", ordlno = "", item = "", qty = "",listprice="",itemDesc = "", result = "",rentalprice="",prodgst="",rentalpricerd="";
				String User_id = _StrUtils.fString(request.getParameter("LOGIN_USER")).trim();
				orderNo = _StrUtils.fString(request.getParameter("DONO")).trim();
				ordlno = _StrUtils.fString(request.getParameter("DOLNNO")).trim();
				custCode = _StrUtils.fString(request.getParameter("CUST_CODE")).trim();
				custName = _StrUtils.fString(request.getParameter("CUST_NAME")).trim();
				jobNum = _StrUtils.fString(request.getParameter("JOB_NUM")).trim();
				item = _StrUtils.fString(request.getParameter("ITEM")).trim();
				itemDesc = _StrUtils.fString(request.getParameter("DESC")).trim();
				qty = _StrUtils.fString(request.getParameter("QTY")).trim();
				rentalprice = StrUtils.removeFormat(StrUtils.fString(request.getParameter("RENTALPRICE")).trim());
				rentalpricerd= StrUtils.fString(request.getParameter("RENTALPRICERD")).trim();
				//rentalprice = _StrUtils.fString(request.getParameter("RENTALPRICE")).trim();
				listprice = _StrUtils.fString(request.getParameter("LISTPRICE")).trim();
				prodgst = _StrUtils.fString(request.getParameter("PRODGST")).trim();
				String uom =_StrUtils.fString(request.getParameter("UOM")).trim();
				
				if(rentalprice.equalsIgnoreCase(""))
					rentalprice="0";
				if(rentalpricerd.equalsIgnoreCase("0"))						
					rentalpricerd=rentalprice;
				else if(rentalpricerd.equalsIgnoreCase(""))
					rentalpricerd=rentalprice;
				
				ItemMstUtil itemMstUtil = new ItemMstUtil();
				itemMstUtil.setmLogger(mLogger);
				String temItem = itemMstUtil.isValidAlternateItemInItemmst(
						plant, item);
				if (temItem != "") {
					item = temItem;
				} else {
					throw new Exception("Product not found!");
				}
				Map mPrddet = new ItemMstDAO().getProductNonStockDetails(plant,item);
				String nonstocktype= StrUtils.fString((String) mPrddet.get("NONSTKFLAG"));

				double quanty = Double.parseDouble(qty);
				quanty = StrUtils.RoundDB(quanty, IConstants.DECIMALPTS);
				qty = String.valueOf(quanty);

				Hashtable htdet = new Hashtable();
				htdet.put(IDBConstants.PLANT, plant);
				htdet.put(IDBConstants.LOANDET_ORDNO, orderNo);
				htdet.put(IDBConstants.LOANDET_ORDLNNO, ordlno);
				htdet.put(IDBConstants.LOANDET_ITEM, item);
				htdet.put(IDBConstants.UNITMO,uom);
				htdet.put("ITEMDESC", _StrUtils.InsertQuotes(itemDesc));
			//	htdet.put("UNITMO", _StrUtils.fString(new ItemMstDAO().getItemUOM(plant, item)));
				htdet.put(IDBConstants.LOANDET_ITEM_DESC, _StrUtils.InsertQuotes(StrUtils.fString(new ItemMstDAO().getItemDesc(plant, item))));
				if(nonstocktype.equals("Y"))	
				 {
					htdet.put(IDBConstants.LOANDET_LNSTATUS, "C");
					htdet.put(IDBConstants.LOANDET_PICKSTATUS, "C");
					htdet.put(IDBConstants.LOANDET_RECVSTATUS, "C");
					htdet.put(IDBConstants.LOANDET_QTYIS, qty);
					htdet.put(IDBConstants.LOANDET_QTYRC, qty);
				 }else{
					 htdet.put(IDBConstants.LOANDET_LNSTATUS, "N");
					 htdet.put(IDBConstants.LOANDET_PICKSTATUS, "N");
					 htdet.put(IDBConstants.LOANDET_RECVSTATUS, "N");
				 }
				htdet.put(IDBConstants.LOANDET_JOB_NUM, jobNum);
				htdet.put(IDBConstants.LOANDET_CUST_NAME, custName);
				htdet.put(IDBConstants.LOANDET_QTYOR, qty);
				htdet.put(IDBConstants.LOANDET_RENTALPRICE, rentalprice);
				htdet.put(IDBConstants.LOANDET_PRODGST, prodgst);
				htdet.put("LISTPRICE", listprice);

				java.util.Date dt = new java.util.Date();
				SimpleDateFormat dfVisualDate = new SimpleDateFormat("dd/MM/yyyy");
				String today = dfVisualDate.format(dt);

				htdet.put(IDBConstants.TRAN_DATE,DateUtils.getDateinyyyy_mm_dd(today));

				boolean flag = false;
				flag = _loanUtil.saveLoanDetDetails(htdet);

				if (flag) {
				String DYNAMIC_REMARKS_SIZE = StrUtils.fString(request.getParameter("DYNAMIC_REMARKS_SIZE"));
				int remarks_count = Integer.parseInt(DYNAMIC_REMARKS_SIZE);
				String productDORemarks = "";
				for (int index = 0; index < remarks_count; index++) {
					productDORemarks = StrUtils.fString(request.getParameter("PRDREMARKS" + "_" + index));
					Hashtable<String, String> htRemarks = new Hashtable();
					htRemarks.put(IDBConstants.PLANT, plant);
					htRemarks.put(IDBConstants.LOANDET_ORDNO, orderNo);
					htRemarks.put(IDBConstants.LOANDET_ORDLNNO, ordlno);
					htRemarks.put(IDBConstants.LOANDET_ITEM, item);
					htRemarks.put(IDBConstants.REMARKS, strUtils.InsertQuotes(productDORemarks));
					htRemarks.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
					htRemarks.put(IDBConstants.CREATED_BY, userName);
					flag = new LoanUtil().saveLoanMultiRemarks(htRemarks);
					
					Hashtable htMaster = new Hashtable();
					if (flag) {
						if (productDORemarks.length() > 0) {
							htMaster.clear();
							htMaster.put(IDBConstants.PLANT, plant);
							htMaster.put(IDBConstants.REMARKS, strUtils.InsertQuotes(productDORemarks));
							
							if (!_MasterDAO.isExisitRemarks(htMaster, "")) {
								htMaster.put(IDBConstants.CREATED_AT, dateUtils.getDateTime());
							htMaster.put(IDBConstants.CREATED_BY, User_id);
								_MasterDAO.InsertRemarks(htMaster);
								Hashtable htRecvHis = new Hashtable();
								htRecvHis.put(IDBConstants.PLANT, plant);
								htRecvHis.put("DIRTYPE", "CREATE_REMARKS");
								htRecvHis.put("ORDNUM", "");
								htRecvHis.put(IDBConstants.ITEM, "");
								htRecvHis.put("BATNO", "");
								htRecvHis.put(IDBConstants.LOC, "");
								htRecvHis.put("REMARKS", strUtils.InsertQuotes(productDORemarks));
								htRecvHis.put(IDBConstants.CREATED_BY, User_id);
								htRecvHis.put(IDBConstants.TRAN_DATE,
										DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));
								htRecvHis.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
								flag = movHisDao.insertIntoMovHis(htRecvHis);
							}
						}
					}
				}
			}
				
				
				if(nonstocktype.equals("Y"))	
				 {
					Hashtable htPickDet = new Hashtable();
					htPickDet.clear();
					htPickDet.put(IDBConstants.PLANT,plant);
					htPickDet.put("ORDNO", orderNo);
					htPickDet.put("ORDLNNO", ordlno);
					htPickDet.put(IDBConstants.CUSTOMER_NAME, custName);
					htPickDet.put(IDBConstants.ITEM,item);
					htPickDet.put(IDBConstants.ITEM_DESC, _StrUtils.InsertQuotes(itemDesc));
					htPickDet.put("BATCH","NOBATCH");
					htPickDet.put(IDBConstants.LOC,"");
					htPickDet.put(IConstants.LOC1,"");
					htPickDet.put("ORDQTY", qty);
					htPickDet.put("PICKQTY", qty);
					htPickDet.put("REVERSEQTY", "0");
					htPickDet.put(IDBConstants.CREATED_AT, dateUtils.getDateTime());
					htPickDet.put(IDBConstants.CREATED_BY, User_id);
					htPickDet.put(IDBConstants.ISSUEDATE,today );
					flag = new LoanHdrDAO().insertintoLoanPick(htPickDet);
				 }
				
				if (flag) {
					//To check LOHDR status and update
					    Boolean hdrFlag=false;
						LoanDetDAO Loan = new LoanDetDAO();
						LoanHdrDAO _LoanHdrDAO = new LoanHdrDAO();
						String updateDoHdr = "";
						Hashtable htConditoHdr = new Hashtable();
						htConditoHdr.put("PLANT", plant);
						htConditoHdr.put("ordno", orderNo);
						hdrFlag = _LoanHdrDAO.isExisit(htConditoHdr,"  isnull(STATUS,'') in ('C')");
	    				if (hdrFlag){
							updateDoHdr = "set  STATUS='O'";
						   flag = _LoanHdrDAO.update(updateDoHdr, htConditoHdr, "");
	    				}
					//To check DOHDR status and update end
					Hashtable htMovHis = new Hashtable();
					htMovHis.clear();
					htMovHis.put(IDBConstants.PLANT, plant);
					htMovHis.put("DIRTYPE", TransactionConstants.LOAN_ADD_ITEM);
					htMovHis.put(IDBConstants.CUSTOMER_CODE, custCode);
					htMovHis.put(IDBConstants.POHDR_JOB_NUM, jobNum);
					htMovHis.put(IDBConstants.ITEM, item);
					htMovHis.put(IDBConstants.QTY, qty);
					htMovHis.put(IDBConstants.MOVHIS_ORDNUM, orderNo);
					htMovHis.put(IDBConstants.CREATED_BY, User_id);
					htMovHis.put("MOVTID", "");
					htMovHis.put("RECID", "");
					htMovHis.put(IDBConstants.TRAN_DATE,dateUtils.getDateinyyyy_mm_dd(dateUtils.getDate()));
					htMovHis.put(IDBConstants.CREATED_AT,dateUtils.getDateTime());
					flag = movHisDao.insertIntoMovHis(htMovHis);

				}

				if (flag)
					result = "<font color=\"green\">Product ID Added Successfully</font><br><br><center>"
							+ "<input type=\"button\" value=\" OK \" onClick=\"window.location.href='/track/loanorderservlet?DONO="
							+ orderNo + "&Submit=View'\">";
				else
					result = "<font color=\"red\"> Error in Adding Product ID   <br><br><center>"
							+ "<input type=\"button\" value=\"Back\" onClick=\"window.location.href='javascript:history.back()'\"> ";

				result = "Rental Order<br><h3>" + result;

				response.sendRedirect("jsp/createLOANDET.jsp?Submit=add&DONO="
						+ orderNo + "&JOB_NUM=" + jobNum + "&CUST_CODE="
						+ custCode + "&CUST_NAME="
						+ StrUtils.replaceCharacters2Send(custName));

			}
		} catch (Exception ex) {
			this.mLogger.exception(this.printLog, "", ex);
			String result = "<font class = " + IConstants.FAILED_COLOR
					+ ">Error : " + ex.getMessage() + "</font>";
			result = result
					+ " <br><br><center> "
					+ "<input type=\"button\" value=\"Back\" onClick=\"window.location.href='javascript:history.back()'\"> ";
			result = "Rental Order<br><h3>" + result;
			request.getSession().setAttribute("RESULT", result);
			response.sendRedirect("jsp/displayResult2User.jsp");

		}

	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

	private String SaveLoanHdr(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException,
			Exception {

		MovHisDAO movHisDao = new MovHisDAO();
		movHisDao.setmLogger(mLogger);
		DateUtils dateUtils = new DateUtils();
		boolean flag = false,isAutoGenerate = false;
		Hashtable ht = new Hashtable();
		String result = "", ordno = "", custCode = "", custName = "", jobNum = "", personIncharge = "", plant = "", user_id = "", 
				contactNum = "", address = "", address2 = "", address3 = "", collectionDate = "", collectionTime = "", remark1 = "", remark2 = "", frLoc = "", toLoc = "",
				expiredate="",processno="",ordertype="",currencyid="",rental_gst="",empid="",deliverydate="",paymenttype="",shippingcustomer="",orderdiscount="",shippingcost="",days="";

		try {

			HttpSession session = request.getSession();
			ordno = _StrUtils.fString(request.getParameter("DONO")).trim();
			plant = _StrUtils.fString((String) session.getAttribute("PLANT"))
					.trim();
			custCode = _StrUtils.fString(request.getParameter("CUST_CODE"))
					.trim();

			user_id = _StrUtils.fString(request.getParameter("LOGIN_USER"))
					.trim();
			custName = _StrUtils.fString(request.getParameter("CUST_NAME"))
					.trim();
			custCode = _StrUtils.fString(request.getParameter("CUST_CODE"))
					.trim();
			jobNum = _StrUtils.fString(request.getParameter("JOB_NUM")).trim();
			personIncharge = _StrUtils.fString(
					request.getParameter("PERSON_INCHARGE")).trim();
			contactNum = _StrUtils.fString(request.getParameter("CONTACT_NUM"))
					.trim();

			address = _StrUtils.fString(request.getParameter("ADD1")).trim();
			address2 = _StrUtils.fString(request.getParameter("ADD2"))
					.trim();
			address3 = _StrUtils.fString(request.getParameter("ADD3"))
					.trim();
			collectionDate = _StrUtils.fString(request.getParameter("DELDATE"))
					.trim();
			collectionTime = _StrUtils.fString(
					request.getParameter("COLLECTION_TIME")).trim();
			remark1 = _StrUtils.fString(request.getParameter("REMARK1")).trim();
			remark2 = _StrUtils.fString(request.getParameter("REMARK2")).trim();
			frLoc = _StrUtils.fString(request.getParameter("FRLOC")).trim();
			toLoc = _StrUtils.fString(request.getParameter("TOLOC")).trim();
			expiredate= _StrUtils.fString(request.getParameter("EXPIREDATE")).trim();
			ordertype = _StrUtils.fString(request.getParameter("ORDERTYPE")).trim();
			currencyid= _StrUtils.fString(request.getParameter("DISPLAY")).trim();
			rental_gst= _StrUtils.fString(request.getParameter("GST")).trim();
			empid= _StrUtils.fString(request.getParameter("EMP_NAME")).trim();
			deliverydate= _StrUtils.fString(request.getParameter("DELIVERYDATE")).trim();
			paymenttype= _StrUtils.fString(request.getParameter("PAYMENTTYPE")).trim();
			shippingcustomer= _StrUtils.fString(request.getParameter("SHIPPINGCUSTOMER")).trim();
			orderdiscount= _StrUtils.fString(request.getParameter("ORDERDISCOUNT")).trim();
			shippingcost= _StrUtils.fString(request.getParameter("SHIPPINGCOST")).trim();
			days= _StrUtils.fString(request.getParameter("DAYS")).trim();
			String DATEFORMAT = StrUtils.fString((request.getParameter("DATEFORMAT") != null) ? "1": "0").trim();
			String EDATEFORMAT = StrUtils.fString((request.getParameter("EDATEFORMAT") != null) ? "1": "0").trim();
			isAutoGenerate = Boolean.valueOf(_StrUtils.fString(request.getParameter("ISAUTOGENERATE")).trim()); 
			
			UserLocUtil uslocUtil = new UserLocUtil();
			uslocUtil.setmLogger(mLogger);
			boolean isvalidlocforUser = uslocUtil.isValidLocInLocmstForUser(
					plant, user_id, frLoc);
			if (isvalidlocforUser) {
				CustomerBeanDAO customerBeanDAO = new CustomerBeanDAO();
				customerBeanDAO.setmLogger(mLogger);
				boolean isValidLoanAss = customerBeanDAO
						.isExistsLoanAssigneeName(custName, plant);
				if (isValidLoanAss) {
					ht.put(IDBConstants.PLANT, plant);
					ht.put(IDBConstants.LOANHDR_ORDNO, ordno);
					ht.put("CRBY", user_id);
					ht.put(IDBConstants.LOANHDR_CUST_CODE, custCode);
					ht.put(IDBConstants.LOANHDR_CUST_NAME, custName);
					ht.put(IDBConstants.LOANHDR_JOB_NUM, jobNum);
					ht.put(IDBConstants.LOANHDR_PERSON_INCHARGE, personIncharge);
					ht.put(IDBConstants.LOANHDR_CONTACT_NUM, contactNum);
					ht.put(IDBConstants.LOANHDR_ADDRESS, address);
					ht.put(IDBConstants.LOANHDR_ADDRESS2, address2);
					ht.put(IDBConstants.LOANHDR_ADDRESS3, address3);
					ht.put(IDBConstants.LOANHDR_COL_DATE, collectionDate);
					ht.put(IDBConstants.LOANHDR_COL_TIME, collectionTime);
					ht.put(IDBConstants.LOANHDR_REMARK1, remark1);
					ht.put(IDBConstants.LOANHDR_REMARK2, remark2);
					ht.put("LOC", frLoc);
					ht.put("LOC1", toLoc);
					ht.put(IDBConstants.STATUS, "N");
					ht.put(IDBConstants.EXPIREDATE, expiredate);
					ht.put(IDBConstants.ORDERTYPE, ordertype);
					ht.put(IDBConstants.CURRENCYID, currencyid);
					ht.put(IDBConstants.LOANHDR_GST, rental_gst);
					ht.put(IDBConstants.LOANHDR_EMPNO, empid);
					ht.put(IDBConstants.DELIVERYDATE, deliverydate);
					ht.put(IDBConstants.PAYMENTTYPE, paymenttype);
					ht.put(IDBConstants.SHIPPINGCUSTOMER, shippingcustomer);
					ht.put(IDBConstants.ORDERDISCOUNT, orderdiscount);
					ht.put(IDBConstants.SHIPPINGCOST, shippingcost);
					ht.put(IDBConstants.LOANHDR_DELIVERYDATEFORMAT, DATEFORMAT);
					ht.put(IDBConstants.LOANHDR_EXPIRYDATEFORMAT, EDATEFORMAT);
					if(days.equals("NaN") || days.equals(null))
					{
						ht.put("DAYS", "");
					}else
					{
						ht.put("DAYS", days);
					}
					ht.put(IDBConstants.LOANHDR_PERSON_INCHARGE, personIncharge);
					flag = _loanUtil.saveLoanHdrDetails(ht);

					if (flag) {
						Hashtable htRecvHis = new Hashtable();
						htRecvHis.clear();
						htRecvHis.put(IDBConstants.PLANT, plant);
						htRecvHis.put("DIRTYPE",
								TransactionConstants.CREATE_LOANASSIGN);
						htRecvHis.put(IDBConstants.CUSTOMER_CODE, custCode);
						htRecvHis.put(IDBConstants.POHDR_JOB_NUM, jobNum);
						htRecvHis.put(IDBConstants.MOVHIS_ORDNUM, ordno);
						htRecvHis.put(IDBConstants.CREATED_BY, user_id);
						htRecvHis.put("MOVTID", "");
						htRecvHis.put("RECID", "");

						if (!remark1.equals("")) {
							htRecvHis.put(IDBConstants.REMARKS, custName + ","
									+ remark1);
						} else {
							htRecvHis.put(IDBConstants.REMARKS, custName);
						}
						htRecvHis.put(IDBConstants.TRAN_DATE, dateUtils
								.getDateinyyyy_mm_dd(dateUtils.getDate()));
						htRecvHis.put(IDBConstants.CREATED_AT,
								dateUtils.getDateTime());

						flag = movHisDao.insertIntoMovHis(htRecvHis);

					}

					if (flag) {
						result = "<font color=\"green\"> Rental Order Created Successfully.  <br><br><center>"
								+ "<input type=\"button\" value=\"Back\" onClick=\"window.location.href='/track/loanorderservlet?DONO="
								+ ordno + "&Submit=View'\"> ";
						result = "<br><h3>" + result + "</h3>";
						
						//UPDATE TBLCTRL
						/*if(isAutoGenerate){
							 
					            _TblControlDAO.updateSeqNo(IConstants.LOAN, plant);
						}*/
						 new TblControlUtil().updateTblControlSaleEstiamateAndRentalServiceSeqNo(plant,IConstants.LOAN,"R",ordno);
						

					} else {
						result = "<font color=\"red\"> Error in Rental Order  - Please Check the Data  <br><br><center>"
								+ "<input type=\"button\" value=\"Back\" onClick=\"window.location.href='javascript:history.back()'\"> ";
						result = "<br><h3>" + result + "</h3>";

					}
				} else {
					result = "<font color=\"red\"> Enter Valid Customer <br><br><center>"
							+ "<input type=\"button\" value=\"Back\" onClick=\"window.location.href='javascript:history.back()'\"> ";
					result = "<br><h3>" + result + "</h3>";
				}
			} else {
				result = "<font color=\"red\"> Enter Valid From Location <br><br><center>"
						+ "<input type=\"button\" value=\"Back\" onClick=\"window.location.href='javascript:history.back()'\"> ";
				result = "<br><h3>" + result + "</h3>";
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			if(e.getMessage().equalsIgnoreCase("Rental order created already")) 
			{
				processno=ordno;
				ordno = _TblControlDAO.getNextOrder(plant,user_id,IConstants.LOAN);
				ht.clear();
				ht.put(IDBConstants.PLANT, plant);
				ht.put(IDBConstants.LOANHDR_ORDNO, ordno);
				ht.put(IDBConstants.LOANHDR_CUST_CODE, custCode);
				ht.put(IDBConstants.LOANHDR_CUST_NAME, custName);
				ht.put(IDBConstants.LOANHDR_JOB_NUM, jobNum);
				ht.put(IDBConstants.LOANHDR_PERSON_INCHARGE, personIncharge);
				ht.put(IDBConstants.LOANHDR_CONTACT_NUM, contactNum);
				ht.put(IDBConstants.LOANHDR_ADDRESS, address);
				ht.put(IDBConstants.LOANHDR_ADDRESS2, address2);
				ht.put(IDBConstants.LOANHDR_ADDRESS3, address3);
				ht.put(IDBConstants.LOANHDR_COL_DATE, collectionDate);
				ht.put(IDBConstants.LOANHDR_COL_TIME, collectionTime);
				ht.put(IDBConstants.LOANHDR_REMARK1, remark1);
				ht.put(IDBConstants.LOANHDR_REMARK2, remark2);
				ht.put("LOC", frLoc);
				ht.put("LOC1", toLoc);
				ht.put(IDBConstants.STATUS, "N");
				ht.put(IDBConstants.EXPIREDATE, expiredate);
				ht.put(IDBConstants.ORDERTYPE, ordertype);
				ht.put(IDBConstants.CURRENCYID, currencyid);
				ht.put(IDBConstants.LOANHDR_GST, rental_gst);
				ht.put(IDBConstants.LOANHDR_EMPNO, empid);
				ht.put(IDBConstants.DELIVERYDATE, deliverydate);
				ht.put(IDBConstants.PAYMENTTYPE, paymenttype);
				ht.put(IDBConstants.SHIPPINGCUSTOMER, shippingcustomer);
				ht.put(IDBConstants.ORDERDISCOUNT, orderdiscount);
				ht.put(IDBConstants.SHIPPINGCOST, shippingcost);
				ht.put(IDBConstants.LOANHDR_PERSON_INCHARGE, personIncharge);
				flag = _loanUtil.saveLoanHdrDetails(ht);

				if (flag) {
					Hashtable htRecvHis = new Hashtable();
					htRecvHis.clear();
					htRecvHis.put(IDBConstants.PLANT, plant);
					htRecvHis.put("DIRTYPE",
							TransactionConstants.CREATE_LOANASSIGN);
					htRecvHis.put(IDBConstants.CUSTOMER_CODE, custCode);
					htRecvHis.put(IDBConstants.POHDR_JOB_NUM, jobNum);
					htRecvHis.put(IDBConstants.MOVHIS_ORDNUM, ordno);
					htRecvHis.put(IDBConstants.CREATED_BY, user_id);
					htRecvHis.put("MOVTID", "");
					htRecvHis.put("RECID", "");

					if (!remark1.equals("")) {
						htRecvHis.put(IDBConstants.REMARKS, custName + ","
								+ remark1);
					} else {
						htRecvHis.put(IDBConstants.REMARKS, custName);
					}
					htRecvHis.put(IDBConstants.TRAN_DATE, dateUtils
							.getDateinyyyy_mm_dd(dateUtils.getDate()));
					htRecvHis.put(IDBConstants.CREATED_AT,
							dateUtils.getDateTime());

					flag = movHisDao.insertIntoMovHis(htRecvHis);
					 new TblControlUtil().updateTblControlSaleEstiamateAndRentalServiceSeqNo(plant,IConstants.LOAN,"R",ordno);
				}
						result = "<font color=\"green\"> Order Number " + processno + " has already been used. System has auto created a new Rental order " + ordno + " for you.<br><br><center>"
						+ "<input type=\"button\" value=\"Back\" onClick=\"window.location.href='/track/loanorderservlet?DONO="
						+ ordno + "&Submit=View'\"> ";
				result = "<br><h3>" + result + "</h3>";
				processno="";
			}
			else
			{
		    	throw e;
			}
		}

		return result;
	}

	private String updateLoanHdr(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException,
			Exception {
		boolean flag = false;
		Hashtable ht = new Hashtable();
		String result = "", ordno = "", custName = "", user = "", jobNum = "", custCode = "", personIncharge = "", contactNum = "", address = "", address2 = "", address3 = "", collectionDate = "", collectionTime = "", remark1 = "", remark2 = "", frLoc = "", toLoc = "",expiredate="",ordertype="",currencyid="",rental_gst="",empid="",deliverydate="",paymenttype="",shippingcustomer="",orderdiscount="",shippingcost="",days="";

		try {
			HttpSession session = request.getSession();
			MovHisDAO movHisDao = new MovHisDAO();
			movHisDao.setmLogger(mLogger);
			DateUtils dateUtils = new DateUtils();
			String plant = _StrUtils.fString(
					(String) session.getAttribute("PLANT")).trim();
			ordno = _StrUtils.fString(request.getParameter("DONO")).trim();
			custName = _StrUtils.fString(request.getParameter("CUST_NAME"))
					.trim();
			custCode = StrUtils.fString(request.getParameter("CUST_CODE"))
					.trim();
			jobNum = _StrUtils.fString(request.getParameter("JOB_NUM")).trim();
			personIncharge = _StrUtils.fString(
					request.getParameter("PERSON_INCHARGE")).trim();
			contactNum = _StrUtils.fString(request.getParameter("CONTACT_NUM"))
					.trim();
			user = _StrUtils.fString(
					(String) session.getAttribute("LOGIN_USER")).trim();
			address = _StrUtils.fString(request.getParameter("ADDRESS")).trim();
			address2 = _StrUtils.fString(request.getParameter("ADDRESS2"))
					.trim();
			address3 = _StrUtils.fString(request.getParameter("ADDRESS3"))
					.trim();
			collectionDate = _StrUtils.fString(request.getParameter("DELDATE"))
					.trim();
			collectionTime = _StrUtils.fString(
					request.getParameter("COLLECTION_TIME")).trim();
			remark1 = _StrUtils.fString(request.getParameter("REMARK1")).trim();
			remark2 = _StrUtils.fString(request.getParameter("REMARK2")).trim();
			frLoc = _StrUtils.fString(request.getParameter("FRLOC")).trim();
			toLoc = _StrUtils.fString(request.getParameter("TOLOC")).trim();
			expiredate=_StrUtils.fString(request.getParameter("EXPIREDATE")).trim();
			ordertype = _StrUtils.fString(request.getParameter("ORDERTYPE")).trim();
			currencyid= _StrUtils.fString(request.getParameter("DISPLAY")).trim();
			rental_gst= _StrUtils.fString(request.getParameter("GST")).trim();
			empid= _StrUtils.fString(request.getParameter("EMP_NAME")).trim();
			deliverydate= _StrUtils.fString(request.getParameter("DELIVERYDATE")).trim();
			paymenttype= _StrUtils.fString(request.getParameter("PAYMENTTYPE")).trim();
			shippingcustomer= _StrUtils.fString(request.getParameter("SHIPPINGCUSTOMER")).trim();
			orderdiscount= _StrUtils.fString(request.getParameter("ORDERDISCOUNT")).trim();
			shippingcost= _StrUtils.fString(request.getParameter("SHIPPINGCOST")).trim();
			days= _StrUtils.fString(request.getParameter("DAYS")).trim();
			String DATEFORMAT = StrUtils.fString((request.getParameter("DATEFORMAT") != null) ? "1": "0").trim();
			String EDATEFORMAT = StrUtils.fString((request.getParameter("EDATEFORMAT") != null) ? "1": "0").trim();

			ht.put(IDBConstants.PLANT, plant);
			ht.put(IDBConstants.LOANHDR_ORDNO, ordno);
			ht.put(IDBConstants.LOANHDR_CUST_NAME, custName);
			ht.put(IDBConstants.LOANHDR_JOB_NUM, jobNum);
			ht.put(IDBConstants.LOANHDR_PERSON_INCHARGE, personIncharge);
			ht.put(IDBConstants.LOANHDR_CONTACT_NUM, contactNum);
			ht.put(IDBConstants.LOANHDR_ADDRESS, address);
			ht.put(IDBConstants.LOANHDR_ADDRESS2, address2);
			ht.put(IDBConstants.LOANHDR_ADDRESS3, address3);
			ht.put(IDBConstants.LOANHDR_COL_DATE, collectionDate);
			ht.put(IDBConstants.LOANHDR_COL_TIME, collectionTime);
			ht.put(IDBConstants.LOANHDR_REMARK1, remark1);
			ht.put(IDBConstants.LOANHDR_REMARK2, remark2);
			ht.put("LOC", frLoc);
			ht.put("LOC1", toLoc);
			ht.put(IDBConstants.STATUS, "N");
			ht.put(IDBConstants.EXPIREDATE, expiredate);
			ht.put(IDBConstants.ORDERTYPE, ordertype);
			ht.put(IDBConstants.CURRENCYID, currencyid);
			ht.put(IDBConstants.LOANHDR_GST, rental_gst);
			ht.put(IDBConstants.LOANHDR_EMPNO, empid);
			ht.put(IDBConstants.DELIVERYDATE, deliverydate);
			ht.put(IDBConstants.PAYMENTTYPE, paymenttype);
			ht.put(IDBConstants.SHIPPINGCUSTOMER, shippingcustomer);
			ht.put(IDBConstants.ORDERDISCOUNT, orderdiscount);
			ht.put(IDBConstants.SHIPPINGCOST, shippingcost);
			ht.put(IDBConstants.LOANHDR_PERSON_INCHARGE, personIncharge);
			ht.put(IDBConstants.LOANHDR_DELIVERYDATEFORMAT, DATEFORMAT);
			ht.put(IDBConstants.LOANHDR_EXPIRYDATEFORMAT, EDATEFORMAT);
			if(days.equals("NaN") || days.equals(null))
			{
				ht.put("DAYS","");
			}else
			{
				ht.put("DAYS", days);
			}

			UserLocUtil uslocUtil = new UserLocUtil();
			uslocUtil.setmLogger(mLogger);
			boolean isvalidlocforUser = uslocUtil.isValidLocInLocmstForUser(
					plant, user, frLoc);
			if (isvalidlocforUser) {
				CustomerBeanDAO customerBeanDAO = new CustomerBeanDAO();
				customerBeanDAO.setmLogger(mLogger);
				boolean isValidLoanAss = customerBeanDAO
						.isExistsLoanAssigneeName(custName, plant);
				if (isValidLoanAss) {

					flag = _loanUtil.updateLoanHdr(ht);
					if (flag) {
						Hashtable htRecvHis = new Hashtable();
						htRecvHis.clear();
						htRecvHis.put(IDBConstants.PLANT, plant);
						htRecvHis.put("DIRTYPE",
								TransactionConstants.UPD_LOAN_ASSIGNEE);
						htRecvHis.put(IDBConstants.CUSTOMER_CODE, custCode);
						htRecvHis.put(IDBConstants.POHDR_JOB_NUM, jobNum);
						htRecvHis.put(IDBConstants.MOVHIS_ORDNUM, ordno);
						htRecvHis.put(IDBConstants.CREATED_BY, user);
						htRecvHis.put("MOVTID", "");
						htRecvHis.put("RECID", "");
						if (!remark1.equals("")) {
							htRecvHis.put(IDBConstants.REMARKS, custName + ","
									+ remark1);
						} else {
							htRecvHis.put(IDBConstants.REMARKS, custName);
						}
						htRecvHis.put(IDBConstants.TRAN_DATE, DateUtils
								.getDateinyyyy_mm_dd(DateUtils.getDate()));
						htRecvHis.put(IDBConstants.CREATED_AT,
								DateUtils.getDateTime());

						flag = movHisDao.insertIntoMovHis(htRecvHis);

					}
					if (flag) {

						result = "<font color=\"green\"> Rental Order Updated Successfully.  <br><br><center>"
								+ "<input type=\"button\" value=\"Back\" onClick=\"window.location.href='/track/loanorderservlet?DONO="
								+ ordno + "&Submit=View'\"> ";
						result = "<br><h3>" + result + "</h3>";

					} else {
						result = "<font color=\"red\"> Error in Updating Rental Order  - Please Check the Data  <br><br><center>"
								+ "<input type=\"button\" value=\"Back\" onClick=\"window.location.href='javascript:history.back()'\"> ";
						result = "<br><h3>" + result + "</h3>";

					}
				} else {
					result = "<font color=\"red\"> Enter Valid Customer <br><br><center>"
							+ "<input type=\"button\" value=\"Back\" onClick=\"window.location.href='javascript:history.back()'\"> ";
					result = "<br><h3>" + result + "</h3>";
				}
			} else {
				result = "<font color=\"red\"> Enter Valid From Location <br><br><center>"
						+ "<input type=\"button\" value=\"Back\" onClick=\"window.location.href='javascript:history.back()'\"> ";
				result = "<br><h3>" + result + "</h3>";
			}
		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
			throw e;
		}

		return result;
	}

	public void viewLoanOrderReport(HttpServletRequest request,
			HttpServletResponse response) throws IOException, Exception {
		Connection con = null;
		try {

			CustUtil cUtil = new CustUtil();
			PlantMstUtil pmUtil = new PlantMstUtil();
    			HttpSession session = request.getSession();
			String CNAME = "", CADD1 = "", CADD2 = "", CADD3 = "", CADD4 = "", CZIP = "", CCOUNTRY = "", CTEL = "", CFAX = "", CONTACTNAME = "", CHPNO = "", CEMAIL = "",RCBNO="";
			;
			
			String SHPCUSTOMERNAME = "", SHPCONTACTNAME = "", SHPTELEPHONE = "", SHPHANDPHONE = "", SHPFAX = "",
					SHPEMAIL = "", SHPUNITNO = "", SHPBUILDING = "", SHPSTREET = "", SHPCITY = "", SHPSTATE = "",
					SHPCOUNTRY = "", SHPPOSTALCODE = "";

			String ORDERNO = StrUtils.fString(request.getParameter("DONO"));
			String SHIPPINGID = StrUtils.fString(request.getParameter("SHIPPINGCUSTOMER"));
			String PLANT = (String) session.getAttribute("PLANT");
			String imagePath =  DbBean.COMPANY_LOGO_PATH + "/"+ PLANT.toLowerCase() + DbBean.LOGO_FILE;
			String imagePath1 =  DbBean.COMPANY_LOGO_PATH + "/"+ PLANT.toLowerCase() + "Logo1.gif";
			
			File checkImageFile = new File(imagePath);
	        if (!checkImageFile.exists()) {
	           imagePath =  DbBean.COMPANY_LOGO_PATH + "/"+ DbBean.NO_LOGO_FILE; 
	        }
	        File checkImageFile1 = new File(imagePath1);
	        if (!checkImageFile1.exists()) {
	           imagePath1 =  DbBean.COMPANY_LOGO_PATH + "/"+ DbBean.NO_LOGO_FILE; 
	        }
	        
	        String imagePath2,Orientation="";			

			con = DbBean.getConnection();

			String SysDate = DateUtils.getDate();
			String jasperPath = DbBean.JASPER_INPUT + "/" + "rptLoanOrder";

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
				RCBNO = (String) map.get("RCBNO");
				

			}
			ArrayList arrCust = cUtil.getAssigneeDetailsForLoan(ORDERNO, PLANT);
			if (arrCust.size() > 0) {
				String sCustCode = (String) arrCust.get(0);
				String sCustName = (String) arrCust.get(1);
				String sAddr1 = (String) arrCust.get(2);
				String sAddr2 = (String) arrCust.get(3);
				String sAddr3 = (String) arrCust.get(4);
				String sCountry = (String) arrCust.get(5);
				String sZip = (String) arrCust.get(6);
				String sCons = (String) arrCust.get(7);
				String sContactName = (String) arrCust.get(8);
				String sDesgination = (String) arrCust.get(9);
				String sTelNo = (String) arrCust.get(10);
				String sHpNo = (String) arrCust.get(11);
				String sEmail = (String) arrCust.get(12);
				String sFax = (String) arrCust.get(13);
				String sRemarks = (String) arrCust.get(14);
				String sAddr4 = (String) arrCust.get(15);
				String sIsactive = (String) arrCust.get(16);
				String orderRemarks = (String) arrCust.get(17);
				String ORDTYPE = (String) arrCust.get(18);
				String CRBY = (String) arrCust.get(19);
				
					ArrayList arrShippingDetails = _masterUtil.getRentalShippingDetails(ORDERNO, SHIPPINGID, PLANT);
				Map parameters = new HashMap();
				if (arrShippingDetails.size() > 0) {
					parameters.put("shipToId", SHIPPINGID);
					SHPCUSTOMERNAME = (String) arrShippingDetails.get(1);
					SHPCONTACTNAME = (String) arrShippingDetails.get(2);
					SHPTELEPHONE = (String) arrShippingDetails.get(3);
					SHPHANDPHONE = (String) arrShippingDetails.get(4);
					SHPFAX = (String) arrShippingDetails.get(5);
					SHPEMAIL = (String) arrShippingDetails.get(6);
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

				//Map parameters = new HashMap();
				parameters.put("imagePath", imagePath);
				parameters.put("imagePath1", imagePath1);

				// Customer Details
				parameters.put("OrderNo", ORDERNO);
				parameters.put("company", PLANT);
				parameters.put("To_CompanyName", sCustName);
				parameters.put("To_BlockAddress", sAddr1 + "  " + sAddr2);
				parameters.put("To_RoadAddress", sAddr3 + "  " + sAddr4);
				parameters.put("To_Country", sCountry);
				parameters.put("To_ZIPCode", sZip);
				parameters.put("To_AttentionTo", sContactName);
				parameters.put("To_CCTO", "");
				parameters.put("To_TelNo", sTelNo);
                parameters.put("To_Email", sEmail);
                parameters.put("To_Fax", sFax);
				parameters.put("SupRemarks", sRemarks);

				// Company Details
				parameters.put("fromAddress_CompanyName", CNAME);
				parameters.put("fromAddress_BlockAddress", CADD1 + "  " + CADD2);
				parameters.put("fromAddress_RoadAddress", CADD3 + "  " + CADD4);
				parameters.put("fromAddress_Country", CCOUNTRY);
				parameters.put("fromAddress_ZIPCode", CZIP);
				parameters.put("fromAddress_TpNo", CTEL);
				parameters.put("fromAddress_FaxNo", CFAX);
				parameters.put("fromAddress_ContactPersonName", CONTACTNAME);
				parameters.put("fromAddress_ContactPersonMobile", CHPNO);
				parameters.put("fromAddress_ContactPersonEmail", CEMAIL);
				parameters.put("currentTime", SysDate);
				parameters.put("refNo",new LoanUtil().getJobNum(PLANT, ORDERNO));
				parameters.put("InvoiceTerms", "");
				parameters.put("fromAddress_RCBNO", RCBNO);
				parameters.put("prepareduser", CRBY);
				 
				LoanUtil loUtil = new LoanUtil();
				Map ma = loUtil.getLOReceiptHdrDetails(PLANT);
				Orientation = (String) ma.get("PrintOrientation");
				if (ma.get("DISPLAYBYORDERTYPE").equals("1"))
					parameters.put("OrderHeader", ORDTYPE);
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
				parameters.put("VAT", (String) ma.get("VAT"));
				parameters.put("EMPNAME", (String) ma.get("EMPNAME"));
				parameters.put("SHIPTO", (String) ma.get("SHIPTO"));
				parameters.put("SELLER", (String) ma.get("SELLER"));
				parameters.put("SELLERAUTHORIZED", (String) ma.get("SELLERAUTHORIZED"));
				parameters.put("BUYER", (String) ma.get("BUYER"));
				parameters.put("BUYERAUTHORIZED", (String) ma.get("BUYERAUTHORIZED"));
				
				parameters.put("PRINTDETAILDESC", (String) ma.get("PRINTDETAILDESC"));
				parameters.put("PRINTPRDREMARKS", (String) ma.get("PRINTPRDREMARKS"));
				parameters.put("PRINTWITHPRDID", (String) ma.get("PRINTWITHPRDID"));
				parameters.put("PRINTWITHBRAND", (String) ma.get("PRINTWITHBRAND"));
				parameters.put("PRINTWITHEMPLOYEE", (String) ma.get("PRINTWITHEMPLOYEE"));
				parameters.put("PREPAREDBY", (String) ma.get("PREPAREDBY"));
				
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
				parameters.put("SHPSTATE", SHPSTATE);
				parameters.put("SHPCOUNTRY", SHPCOUNTRY);
				parameters.put("SHPPOSTALCODE", SHPPOSTALCODE);
			

				parameters.put("OrderQty", (String) ma.get("ORDERQTY"));
				if(orderRemarks.length()>0) orderRemarks = (String) ma.get("REMARK1") +" : "+orderRemarks;
				parameters.put("OrderRemarks", orderRemarks);
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
				parameters.put("expireDate",new LoanUtil().getExpireDate(PLANT, ORDERNO));
		          if(Orientation.equals("Portrait")){
	                								   
					        if (!checkImageFile.exists()) {
						           imagePath2 = imagePath1; 
						           imagePath = "";
						        }
					        else if(!checkImageFile1.exists()){
					        	imagePath2 = imagePath; 
					        	imagePath1 = "";
					        }
					        else{
					        	imagePath2 ="";	        	
					        }
							parameters.put("imagePath", imagePath);
							parameters.put("imagePath1", imagePath1);
							parameters.put("imagePath2", imagePath2);
					        
							jasperPath = DbBean.JASPER_INPUT + "/" + "rptLoanOrderPortrait";
							
	                 }
	                 
				long start = System.currentTimeMillis();
				System.out.println("**************" + " Start Up Time : "
						+ start + "**********");
				JasperCompileManager.compileReportToFile(
	    		 		jasperPath+".jrxml", jasperPath+".jasper");

				byte[] bytes = JasperRunManager.runReportToPdf(jasperPath
						+ ".jasper", parameters, con);

				//response.addHeader("Content-disposition","attachment;filename=reporte.pdf");
				response.addHeader("Content-disposition","inline;filename=reporte.pdf");
				response.setContentLength(bytes.length);
				response.getOutputStream().write(bytes);
				response.setContentType("application/pdf");
				response.getOutputStream().flush();
				response.getOutputStream().close();
			}

		} catch (IOException e) {

			e.printStackTrace();

		} finally {
			DbBean.closeConnection(con);
		}
	}
	
	public void viewLoanOrderInvoiceReport(HttpServletRequest request,
			HttpServletResponse response) throws IOException, Exception {
		Connection con = null;
		try {

			CustUtil cUtil = new CustUtil();
			PlantMstUtil pmUtil = new PlantMstUtil();
    			HttpSession session = request.getSession();
			String CNAME = "", CADD1 = "", CADD2 = "", CADD3 = "", CADD4 = "", CZIP = "", CCOUNTRY = "", CTEL = "", CFAX = "", CONTACTNAME = "", CHPNO = "", CEMAIL = "",RCBNO="";
			;
			String SHPCUSTOMERNAME = "", SHPCONTACTNAME = "", SHPTELEPHONE = "", SHPHANDPHONE = "", SHPFAX = "",
					SHPEMAIL = "", SHPUNITNO = "", SHPBUILDING = "", SHPSTREET = "", SHPCITY = "", SHPSTATE = "",
					SHPCOUNTRY = "", SHPPOSTALCODE = "";
			String ORDERNO = StrUtils.fString(request.getParameter("DONO"));
			String PLANT = (String) session.getAttribute("PLANT");
			String SHIPPINGID = StrUtils.fString(request.getParameter("SHIPPINGCUSTOMER"));
			String imagePath =  DbBean.COMPANY_LOGO_PATH + "/"+ PLANT.toLowerCase() + DbBean.LOGO_FILE;
			String imagePath1 =  DbBean.COMPANY_LOGO_PATH + "/"+ PLANT.toLowerCase() + "Logo1.gif";
			
			File checkImageFile = new File(imagePath);
	        if (!checkImageFile.exists()) {
	           imagePath =  DbBean.COMPANY_LOGO_PATH + "/"+ DbBean.NO_LOGO_FILE; 
	        }
	        File checkImageFile1 = new File(imagePath1);
	        if (!checkImageFile1.exists()) {
	           imagePath1 =  DbBean.COMPANY_LOGO_PATH + "/"+ DbBean.NO_LOGO_FILE; 
	        }
	        
	        String imagePath2,Orientation="";			

			con = DbBean.getConnection();

			String SysDate = DateUtils.getDate();
			String jasperPath = DbBean.JASPER_INPUT + "/" + "rptLoanOrderWithPrice";

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
				RCBNO = (String) map.get("RCBNO");
				
			}
			ArrayList arrCust = cUtil.getAssigneeDetailsForLoan(ORDERNO, PLANT);
			if (arrCust.size() > 0) {
				String sCustCode = (String) arrCust.get(0);
				String sCustName = (String) arrCust.get(1);
				String sAddr1 = (String) arrCust.get(2);
				String sAddr2 = (String) arrCust.get(3);
				String sAddr3 = (String) arrCust.get(4);
				String sCountry = (String) arrCust.get(5);
				String sZip = (String) arrCust.get(6);
				String sCons = (String) arrCust.get(7);
				String sContactName = (String) arrCust.get(8);
				String sDesgination = (String) arrCust.get(9);
				String sTelNo = (String) arrCust.get(10);
				String sHpNo = (String) arrCust.get(11);
				String sEmail = (String) arrCust.get(12);
				String sFax = (String) arrCust.get(13);
				String sRemarks = (String) arrCust.get(14);
				String sAddr4 = (String) arrCust.get(15);
				String sIsactive = (String) arrCust.get(16);
				String orderRemarks = (String) arrCust.get(17);
				String ORDTYPE = (String) arrCust.get(18);
				String CRBY = (String) arrCust.get(19);
				String PAYMENT_TYPE = (String) arrCust.get(20);
				
				ArrayList arrShippingDetails = _masterUtil.getRentalShippingDetails(ORDERNO, SHIPPINGID, PLANT);
				Map parameters = new HashMap();
				if (arrShippingDetails.size() > 0) {
					parameters.put("shipToId", SHIPPINGID);
					SHPCUSTOMERNAME = (String) arrShippingDetails.get(1);
					SHPCONTACTNAME = (String) arrShippingDetails.get(2);
					SHPTELEPHONE = (String) arrShippingDetails.get(3);
					SHPHANDPHONE = (String) arrShippingDetails.get(4);
					SHPFAX = (String) arrShippingDetails.get(5);
					SHPEMAIL = (String) arrShippingDetails.get(6);
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

				//Map parameters = new HashMap();
				parameters.put("imagePath", imagePath);
				parameters.put("imagePath1", imagePath1);

				// Customer Details
				parameters.put("OrderNo", ORDERNO);
				parameters.put("company", PLANT);
				parameters.put("To_CompanyName", sCustName);
				parameters.put("To_BlockAddress", sAddr1 + "  " + sAddr2);
				parameters.put("To_RoadAddress", sAddr3 + "  " + sAddr4);
				parameters.put("To_Country", sCountry);
				parameters.put("To_ZIPCode", sZip);
				parameters.put("To_AttentionTo", sContactName);
				parameters.put("To_CCTO", "");
				parameters.put("To_TelNo", sTelNo);
                parameters.put("To_Email", sEmail);
                parameters.put("To_Fax", sFax);
				parameters.put("SupRemarks", sRemarks);
				parameters.put("TermsDetails", PAYMENT_TYPE);
				
				//parameters.put("employee", employee);

				// Company Details
				parameters.put("fromAddress_CompanyName", CNAME);
				parameters.put("fromAddress_BlockAddress", CADD1 + "  " + CADD2);
				parameters.put("fromAddress_RoadAddress", CADD3 + "  " + CADD4);
				parameters.put("fromAddress_Country", CCOUNTRY);
				parameters.put("fromAddress_ZIPCode", CZIP);
				parameters.put("fromAddress_TpNo", CTEL);
				parameters.put("fromAddress_FaxNo", CFAX);
				parameters.put("fromAddress_ContactPersonName", CONTACTNAME);
				parameters.put("fromAddress_ContactPersonMobile", CHPNO);
				parameters.put("fromAddress_ContactPersonEmail", CEMAIL);
				parameters.put("currentTime", SysDate);
				parameters.put("refNo",new LoanUtil().getJobNum(PLANT, ORDERNO));
				parameters.put("InvoiceTerms", "");
				parameters.put("fromAddress_RCBNO", RCBNO);
				parameters.put("prepareduser", CRBY);
				
				LoanUtil loUtil = new LoanUtil();
				Map ma = loUtil.getLOReceiptHdrDetails(PLANT);
				Orientation = (String) ma.get("PrintOrientation");
				if (ma.get("DISPLAYBYORDERTYPE").equals("1"))
					parameters.put("OrderHeader", ORDTYPE);
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
				
				parameters.put("DELDATE", (String) ma.get("DELDATE"));
				parameters.put("EMPNAME", (String) ma.get("EMPNAME"));
				parameters.put("ORDDISCOUNT", (String) ma.get("ORDDISCOUNT"));
				parameters.put("PREPAREDBY", (String) ma.get("PREPAREDBY"));
				parameters.put("SELLER", (String) ma.get("SELLER"));
				parameters.put("SELLERAUTHORIZED", (String) ma.get("SELLERAUTHORIZED"));
				parameters.put("BUYER", (String) ma.get("BUYER"));
				parameters.put("BUYERAUTHORIZED", (String) ma.get("BUYERAUTHORIZED"));
				parameters.put("COMPANYNAME", (String) ma.get("COMPANYNAME"));
				parameters.put("COMPANYSTAMP", (String) ma.get("COMPANYSTAMP"));
				parameters.put("SIGNATURE", (String) ma.get("SIGNATURE"));
				parameters.put("SHIPPINGCOST", (String) ma.get("SHIPPINGCOST"));
				parameters.put("VAT", (String) ma.get("VAT"));
				parameters.put("CUSTOMERVAT", (String) ma.get("CUSTOMERVAT"));
				parameters.put("ORDDATE", (String) ma.get("ORDDATE"));
				parameters.put("SHIPTO", (String) ma.get("SHIPTO"));
				parameters.put("TOTALAFTERDISCOUNT", (String) ma.get("TOTALAFTERDISCOUNT"));
				parameters.put("NETAMT", (String) ma.get("NETAMT"));
				parameters.put("TAXAMT", (String) ma.get("TAXAMT"));
				parameters.put("TOTALAMT", (String) ma.get("TOTALAMT"));
				parameters.put("RATE", (String) ma.get("RATE"));
				parameters.put("PAYMENTTYPE", (String) ma.get("PAYMENTTYPE"));
				parameters.put("AMOUNT", (String) ma.get("AMOUNT"));
				parameters.put("TOTALTAX", (String) ma.get("TOTALTAX"));
				parameters.put("TOTALWITHTAX", (String) ma.get("TOTALWITHTAX"));
				
				parameters.put("PRINTDETAILDESC", (String) ma.get("PRINTDETAILDESC"));
				parameters.put("PRINTPRDREMARKS", (String) ma.get("PRINTPRDREMARKS"));
				parameters.put("PRINTWITHPRDID", (String) ma.get("PRINTWITHPRDID"));
				parameters.put("PRINTWITHBRAND", (String) ma.get("PRINTWITHBRAND"));
				parameters.put("PRINTWITHEMPLOYEE", (String) ma.get("PRINTWITHEMPLOYEE"));
				parameters.put("PREPAREDBY", (String) ma.get("PREPAREDBY"));
				parameters.put("TOTALROUNDOFF", (String) ma.get("TOTALROUNDOFF"));
				parameters.put("ROUNDOFF", (String) ma.get("ROUNDOFF"));
				
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
				parameters.put("SHPSTATE", SHPSTATE);
				parameters.put("SHPCOUNTRY", SHPCOUNTRY);
				parameters.put("SHPPOSTALCODE", SHPPOSTALCODE);

				parameters.put("OrderQty", (String) ma.get("ORDERQTY"));
				if(orderRemarks.length()>0) orderRemarks = (String) ma.get("REMARK1") +" : "+orderRemarks;
				parameters.put("OrderRemarks", orderRemarks);
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
				parameters.put("expireDate",new LoanUtil().getExpireDate(PLANT, ORDERNO));
		          if(Orientation.equals("Portrait")){
	                								   
					        if (!checkImageFile.exists()) {
						           imagePath2 = imagePath1; 
						           imagePath = "";
						        }
					        else if(!checkImageFile1.exists()){
					        	imagePath2 = imagePath; 
					        	imagePath1 = "";
					        }
					        else{
					        	imagePath2 ="";	        	
					        }
							parameters.put("imagePath", imagePath);
							parameters.put("imagePath1", imagePath1);
							parameters.put("imagePath2", imagePath2);
					        
							jasperPath = DbBean.JASPER_INPUT + "/" + "rptLoanOrderWithPricePortrait";
							
	                 }
	                 
				long start = System.currentTimeMillis();
				System.out.println("**************" + " Start Up Time : "
						+ start + "**********");
				JasperCompileManager.compileReportToFile(
	    		 		jasperPath+".jrxml", jasperPath+".jasper");

				byte[] bytes = JasperRunManager.runReportToPdf(jasperPath
						+ ".jasper", parameters, con);

				//response.addHeader("Content-disposition","attachment;filename=reporte.pdf");
				response.addHeader("Content-disposition","inline;filename=reporte.pdf");
				response.setContentLength(bytes.length);
				response.getOutputStream().write(bytes);
				response.setContentType("application/pdf");
				response.getOutputStream().flush();
				response.getOutputStream().close();
			}

		} catch (IOException e) {

			e.printStackTrace();

		} finally {
			DbBean.closeConnection(con);
		}
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
	
	private HSSFWorkbook writeToExcel(HttpServletRequest request, HttpServletResponse response, HSSFWorkbook wb) {
		String plant = "";
		ArrayList listQry = new ArrayList();
		try {
			HttpSession session = request.getSession();
			plant = session.getAttribute("PLANT").toString();
			String ORDNO = StrUtils.fString(request.getParameter("DONO")).trim();
			// String CUSTNAME = StrUtils.fString(request.getParameter("CUST_NAME")).trim();
			String DIRTYPE = StrUtils.fString(request.getParameter("DIRTYPE"));
			Hashtable htCond = new Hashtable();
			htCond.put("A.PLANT", plant);
			htCond.put("A.ORDNO", ORDNO);
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
					cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String) lineArr.get("ORDNO"))));
					cell.setCellStyle(dataStyle);

					cell = row.createCell(k++);
					cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String) lineArr.get("ORDLNNO"))));
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
					cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String) lineArr.get("rental_gst"))));
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

					/*cell = row.createCell(k++);
					cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String) lineArr.get("manufacturer"))));
					cell.setCellStyle(dataStyle);*/

					cell = row.createCell(k++);
					// cell.setCellType(cell.CELL_TYPE_NUMERIC);
					cell.setCellValue(Double.parseDouble(StrUtils.fString((String) lineArr.get("qtyor"))));
					cell.setCellStyle(dataStyle);

					cell = row.createCell(k++);
					cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String) lineArr.get("unitmo"))));
					cell.setCellStyle(dataStyle);

					// By Samatha on 20/09/2013
					String ConvertedUnitCost = new LoanHdrDAO().getUnitCostBasedOnCurIDSelected(plant, ORDNO,
							(String) lineArr.get("ORDLNNO"), (String) lineArr.get("item"));
					// String cost = StrUtils.currencyWtoutSymbol(ConvertedUnitCost);
					
					/* Start by Abhilash on 25/09/2019 */
					double dCost = Double.parseDouble(ConvertedUnitCost);
					ConvertedUnitCost = StrUtils.addZeroes(dCost, numberOfDecimal);
					/* End by Abhilash on 25/09/2019 */

					cell = row.createCell(k++);
					cell.setCellValue(new HSSFRichTextString(StrUtils.fString(ConvertedUnitCost)));
					//cell.setCellValue(Double.parseDouble(StrUtils.fString(ConvertedUnitCost)));
					cell.setCellStyle(dataStyle);

					cell = row.createCell(k++);
					cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String) lineArr.get("currencyid"))));
					cell.setCellStyle(dataStyle);

					cell = row.createCell(k++);
					cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String) lineArr.get("itemdesc"))));
					cell.setCellStyle(dataStyle);
					

					cell = row.createCell(k++);
					cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String) lineArr.get("orderdiscount"))));
					cell.setCellStyle(dataStyle);
					
					/* Start by Abhilash on 25/09/2019 */
					double dShippingCost = Double.parseDouble((String) lineArr.get("shippingcost"));
					String shippingCost = StrUtils.addZeroes(dShippingCost, numberOfDecimal);
					/* End by Abhilash on 25/09/2019 */
					
					cell = row.createCell(k++);
					cell.setCellValue(new HSSFRichTextString(StrUtils.fString(shippingCost)));
					cell.setCellStyle(dataStyle);
					
					/* Start by Abhilash on 28/09/2019 
					double dlocalexpenses = Double.parseDouble((String) lineArr.get("localexpenses"));
					String localExpenses = StrUtils.addZeroes(dlocalexpenses, numberOfDecimal);
					 End by Abhilash on 28/09/2019 
					
					cell = row.createCell(k++);
					cell.setCellValue(new HSSFRichTextString(StrUtils.fString(localExpenses)));
					cell.setCellStyle(dataStyle);*/

					cell = row.createCell(k++);
					cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String) lineArr.get("shippingcustomer"))));
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
			cell.setCellValue(new HSSFRichTextString("Order No"));
			cell.setCellStyle(styleHeader);

			cell = rowhead.createCell(k++);
			cell.setCellValue(new HSSFRichTextString("Order Line No"));
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
			cell.setCellValue(new HSSFRichTextString("Rental VAT"));
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

			/*cell = rowhead.createCell(k++);
			cell.setCellValue(new HSSFRichTextString("Manufacturer"));
			cell.setCellStyle(styleHeader);*/

			cell = rowhead.createCell(k++);
			cell.setCellValue(new HSSFRichTextString("Quantity"));
			cell.setCellStyle(styleHeader);

			cell = rowhead.createCell(k++);
			cell.setCellValue(new HSSFRichTextString("Uom"));
			cell.setCellStyle(styleHeader);

			cell = rowhead.createCell(k++);
			cell.setCellValue(new HSSFRichTextString("Rental Price"));
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
			cell.setCellValue(new HSSFRichTextString("Shipping Customer"));
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
	
	private HSSFSheet createWidth(HSSFSheet sheet) {

		try {
			sheet.setColumnWidth(0, 5000);
			sheet.setColumnWidth(1, 3000);
			sheet.setColumnWidth(2, 7000);
			sheet.setColumnWidth(3, 6000);
			sheet.setColumnWidth(4, 3000);
			sheet.setColumnWidth(5, 3000);
			sheet.setColumnWidth(6, 6000);
			sheet.setColumnWidth(7, 3000);
			sheet.setColumnWidth(8, 5000);
			sheet.setColumnWidth(9, 6000);
			sheet.setColumnWidth(10, 7000);
			sheet.setColumnWidth(11, 5000);
			sheet.setColumnWidth(12, 3000);
			sheet.setColumnWidth(13, 4000);
			sheet.setColumnWidth(14, 4000);
			sheet.setColumnWidth(15, 4000);
			sheet.setColumnWidth(16, 5000);
			sheet.setColumnWidth(17, 5000);
			sheet.setColumnWidth(18, 7000);
			//sheet.setColumnWidth(19, 7000);
			//sheet.setColumnWidth(20, 7000);
			//sheet.setColumnWidth(21, 7000);

		} catch (Exception e) {
			this.mLogger.exception(this.printLog, "", e);
		}
		return sheet;
	}
	private HSSFCellStyle createDataStyle(HSSFWorkbook wb) {
		// Create style
		HSSFCellStyle dataStyle = wb.createCellStyle();
		dataStyle.setWrapText(true);
		return dataStyle;
	}
}
