// PAGE CREATED BY : IMTHI
// DATE 14-04-2022
// DESC : Product Promotion Servlet

package com.track.servlet;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.Vector;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.UserTransaction;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.track.constants.IConstants;
import com.track.constants.IDBConstants;
import com.track.constants.TransactionConstants;
import com.track.dao.MasterDAO;
import com.track.dao.MovHisDAO;
import com.track.dao.OutletBeanDAO;
import com.track.dao.PlantMstDAO;
import com.track.db.util.EmailMsgUtil;
import com.track.db.util.HTReportUtil;
import com.track.db.util.ItemUtil;
import com.track.db.util.OutletUtil;
import com.track.db.util.PlantMstUtil;
import com.track.gates.DbBean;
import com.track.util.DateUtils;
import com.track.util.IMLogger;
import com.track.util.StrUtils;
import com.track.util.ThrowableUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@WebServlet("/posreports/*")
public class PosSalesReportServlet extends HttpServlet implements IMLogger {

	private static final long serialVersionUID = 1L;

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String[] pathInfo = request.getPathInfo().split("/");
		String action = pathInfo[1];
		String username = StrUtils.fString((String) request.getSession().getAttribute("LOGIN_USER")).trim();
		String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
		boolean ajax = "XMLHttpRequest".equals(request.getHeader("X-Requested-With"));
		

		if (action.equalsIgnoreCase("PosSalesOrderSummary")) {
			JSONObject resultJson = new JSONObject();
			JSONArray jsonArray = new JSONArray();
			JSONArray jsonArrayErr = new JSONArray();
			HTReportUtil movHisUtil = new HTReportUtil();
			PlantMstUtil plantmstutil = new PlantMstUtil();
			// DateUtils _dateUtils = new DateUtils();
			ArrayList movQryList = new ArrayList();

			try {

				String FDATE = StrUtils.fString(request.getParameter("FDATE"));
				String TDATE = StrUtils.fString(request.getParameter("TDATE"));
				String TYPE = StrUtils.fString(request.getParameter("TYPE"));
				String ORDERNO = StrUtils.fString(request.getParameter("ORDERNO"));
				String CUSTOMERCODE = StrUtils.fString(request.getParameter("CUSTOMERCODE"));
				String OUTLET = StrUtils.fString(request.getParameter("OUTLET"));
				String TERMINAL = StrUtils.fString(request.getParameter("TERMINAL"));
				String CASHIER = StrUtils.fString(request.getParameter("CASHIER"));
				String SALESMAN = StrUtils.fString(request.getParameter("SALESMAN"));
				String ITEM = StrUtils.fString(request.getParameter("ITEM"));
				String PRD_BRAND_ID = StrUtils.fString(request.getParameter("PRD_BRAND_ID"));
				String PRD_TYPE_ID = StrUtils.fString(request.getParameter("PRD_TYPE_ID"));
				String PRD_CLS_ID = StrUtils.fString(request.getParameter("PRD_CLS_ID"));
				String ACTION = StrUtils.fString(request.getParameter("ACTION"));
				String PLANT = StrUtils.fString(request.getParameter("PLANT"));
				String PAYMENTTYPE = StrUtils.fString(request.getParameter("PAYMENTTYPE"));
				String numberOfDecimal = new PlantMstDAO().getNumberOfDecimal(plant);
				String ENABLE_POS =new PlantMstDAO().getispos(PLANT);
				 String ISTAXREGISTRED =new PlantMstDAO().getistaxregistred(PLANT);

				String fdate = "";
				String tdate = "";

				if (FDATE == null) {
					FDATE = "";
				} else {
					FDATE = FDATE.trim();
				}

				String curDate = DateUtils.getDate();

				if (FDATE.length() < 0 || FDATE == null || FDATE.equalsIgnoreCase("")) {
					FDATE = curDate;
				}

				if (FDATE.length() > 5) {
					fdate = FDATE.substring(6) + "-" + FDATE.substring(3, 5) + "-" + FDATE.substring(0, 2);
				}

				if (TDATE == null) {
					TDATE = "";
				} else {
					TDATE = TDATE.trim();
				}

				if (TDATE.length() > 5) {
					tdate = TDATE.substring(6) + "-" + TDATE.substring(3, 5) + "-" + TDATE.substring(0, 2);
				}

				if (TYPE.equalsIgnoreCase("Order")) {
					Hashtable ht = new Hashtable();

					if (StrUtils.fString(ORDERNO).length() > 0)
						ht.put("ORDERNO", ORDERNO);
					if (StrUtils.fString(CUSTOMERCODE).length() > 0)
						ht.put("CUSTOMERCODE", CUSTOMERCODE);
					if (StrUtils.fString(OUTLET).length() > 0)
						ht.put("OUTLET", OUTLET);
					if (StrUtils.fString(TERMINAL).length() > 0)
						ht.put("TERMINAL", TERMINAL);
					if (StrUtils.fString(CASHIER).length() > 0)
						ht.put("CASHIER", CASHIER);
					if (StrUtils.fString(SALESMAN).length() > 0)
						ht.put("SALESMAN", SALESMAN);
					if (StrUtils.fString(ITEM).length() > 0)
						ht.put("ITEM", ITEM);
					if (StrUtils.fString(PRD_BRAND_ID).length() > 0)
						ht.put("PRD_BRAND_ID", PRD_BRAND_ID);
					if (StrUtils.fString(PRD_TYPE_ID).length() > 0)
						ht.put("PRD_TYPE_ID", PRD_TYPE_ID);
					if (StrUtils.fString(PRD_CLS_ID).length() > 0)
						ht.put("PRD_CLS_ID", PRD_CLS_ID);
					if (StrUtils.fString(PLANT).length() > 0)
						ht.put("PLANT", PLANT);
					if (StrUtils.fString(PAYMENTTYPE).length() > 0)
						ht.put("PAYMENTTYPE", PAYMENTTYPE);

					movQryList = movHisUtil.getPosSalesorderSummaryByOrderwise(ht, fdate, tdate, "POS", PLANT);

					if (movQryList.size() > 0) {
						int Index = 0;

						for (int iCnt = 0; iCnt < movQryList.size(); iCnt++) {
							Map lineArr = (Map) movQryList.get(iCnt);

							Index = Index + 1;

							JSONObject resultJsonInt = new JSONObject();
							resultJsonInt.put("INDEX", Index);

							resultJsonInt.put("PITEM", "");
							resultJsonInt.put("PITEMDESC", "");
							resultJsonInt.put("PQTY", "");
							resultJsonInt.put("PAMOUNT", "");
							resultJsonInt.put("CCUSTOMER", "");
							resultJsonInt.put("CCUSTOMERNAME", "");
							resultJsonInt.put("CAMOUNT", "");
							resultJsonInt.put("CDISCOUNT", "");
							resultJsonInt.put("CTAX", "");
							resultJsonInt.put("CTOTALAMOUNT", "");
							resultJsonInt.put("ZCATEGORY", "");
							resultJsonInt.put("ZQTY", "");
							resultJsonInt.put("ZAMOUNT", "");
							resultJsonInt.put("PAYMODE", "");

							resultJsonInt.put("SALESDATE", StrUtils.fString((String) lineArr.get("SALESDATE")));
							resultJsonInt.put("DONO", StrUtils.fString((String) lineArr.get("DONO")));
							
							/*ArrayList movQryListpay = new ArrayList();
							movQryListpay = movHisUtil.getPaymentModeByDono((String) lineArr.get("DONO"), PLANT);
							
							String paymode="";
							for (int iCntp = 0; iCntp < movQryListpay.size(); iCntp++) {
								Map lineArrpay = (Map) movQryListpay.get(iCntp);
								if(iCntp == 0) {
									paymode = StrUtils.fString((String) lineArrpay.get("PAYMENTMODE"));
								}else {
									paymode +=", "+StrUtils.fString((String) lineArrpay.get("PAYMENTMODE"));
								}
	
							}
							resultJsonInt.put("PAYMODE", paymode);*/
							
							
							resultJsonInt.put("CNAME", StrUtils.fString((String) lineArr.get("CustName")));
							resultJsonInt.put("REFNUMBER", StrUtils.fString((String) lineArr.get("JobNum")));
							resultJsonInt.put("AMOUNT",
									StrUtils.addZeroes(
											Double.parseDouble(StrUtils.fString((String) lineArr.get("AMOUNT"))),
											numberOfDecimal));
							resultJsonInt.put("DISCOUNT",
									StrUtils.addZeroes(
											Double.parseDouble(
													StrUtils.fString((String) lineArr.get("DISCOUNT_AMOUNT"))),
											numberOfDecimal));
							resultJsonInt.put("EXPRICE",
									StrUtils.addZeroes(
											Double.parseDouble(
													StrUtils.fString((String) lineArr.get("EXPRICE"))),
											numberOfDecimal));
							resultJsonInt.put("TAX",
									StrUtils.addZeroes(
											Double.parseDouble(StrUtils.fString((String) lineArr.get("TAXAMOUNT"))),
											numberOfDecimal));
							double finaltotal = Double.parseDouble(StrUtils.fString((String) lineArr.get("TOTAL_PRICE")));
							 if(ENABLE_POS.equalsIgnoreCase("1") && ISTAXREGISTRED.equalsIgnoreCase("0")) {
								 	finaltotal = ((Double.parseDouble(StrUtils.fString((String) lineArr.get("AMOUNT"))))-Double.parseDouble(
											StrUtils.fString((String) lineArr.get("DISCOUNT_AMOUNT"))));
								 	finaltotal = ((finaltotal)-Double.parseDouble(
								 			StrUtils.fString((String) lineArr.get("EXPRICE"))));								 	
							 }
							resultJsonInt.put("TOTALAMOUNT", StrUtils.addZeroes(finaltotal, numberOfDecimal));
//							resultJsonInt.put("TOTALAMOUNT",
//									StrUtils.addZeroes(
//											Double.parseDouble(StrUtils.fString((String) lineArr.get("TOTAL_PRICE"))),
//											numberOfDecimal));
							String billtime = new DateUtils().parsecratDatetoTime(StrUtils.fString((String) lineArr.get("CRAT")));
							resultJsonInt.put("BILLTIME", billtime);
							
							jsonArray.add(resultJsonInt);

						}
					}

				} else if (TYPE.equalsIgnoreCase("Product")) {
					Hashtable ht = new Hashtable();

					if (StrUtils.fString(ORDERNO).length() > 0)
						ht.put("ORDERNO", ORDERNO);
					if (StrUtils.fString(CUSTOMERCODE).length() > 0)
						ht.put("CUSTOMERCODE", CUSTOMERCODE);
					if (StrUtils.fString(OUTLET).length() > 0)
						ht.put("OUTLET", OUTLET);
					if (StrUtils.fString(TERMINAL).length() > 0)
						ht.put("TERMINAL", TERMINAL);
					if (StrUtils.fString(CASHIER).length() > 0)
						ht.put("CASHIER", CASHIER);
					if (StrUtils.fString(SALESMAN).length() > 0)
						ht.put("SALESMAN", SALESMAN);
					if (StrUtils.fString(ITEM).length() > 0)
						ht.put("ITEM", ITEM);
					if (StrUtils.fString(PRD_BRAND_ID).length() > 0)
						ht.put("PRD_BRAND_ID", PRD_BRAND_ID);
					if (StrUtils.fString(PRD_TYPE_ID).length() > 0)
						ht.put("PRD_TYPE_ID", PRD_TYPE_ID);
					if (StrUtils.fString(PRD_CLS_ID).length() > 0)
						ht.put("PRD_CLS_ID", PRD_CLS_ID);
					if (StrUtils.fString(PLANT).length() > 0)
						ht.put("PLANT", PLANT);
					if (StrUtils.fString(PAYMENTTYPE).length() > 0)
						ht.put("PAYMENTTYPE", PAYMENTTYPE);

					movQryList = movHisUtil.getPosSalesorderSummaryByProductWise(ht, fdate, tdate, "POS", PLANT);

					if (movQryList.size() > 0) {
						int Index = 0;

						for (int iCnt = 0; iCnt < movQryList.size(); iCnt++) {
							Map lineArr = (Map) movQryList.get(iCnt);

							Index = Index + 1;

							JSONObject resultJsonInt = new JSONObject();
							resultJsonInt.put("INDEX", Index);
							resultJsonInt.put("SALESDATE", "");
							resultJsonInt.put("DONO", "");
							resultJsonInt.put("CNAME", "");
							resultJsonInt.put("REFNUMBER", "");
							resultJsonInt.put("AMOUNT", "");
							resultJsonInt.put("DISCOUNT", "");
							resultJsonInt.put("EXPRICE", "");
							resultJsonInt.put("TAX", "");
							resultJsonInt.put("TOTALAMOUNT", "");
							resultJsonInt.put("CCUSTOMER", "");
							resultJsonInt.put("CCUSTOMERNAME", "");
							resultJsonInt.put("CAMOUNT", "");
							resultJsonInt.put("CDISCOUNT", "");
							resultJsonInt.put("CTAX", "");
							resultJsonInt.put("CTOTALAMOUNT", "");
							resultJsonInt.put("ZCATEGORY", "");
							resultJsonInt.put("ZQTY", "");
							resultJsonInt.put("ZAMOUNT", "");
							resultJsonInt.put("PAYMODE", "");

							resultJsonInt.put("PITEM", StrUtils.fString((String) lineArr.get("ITEM")));
							resultJsonInt.put("PITEMDESC", StrUtils.fString((String) lineArr.get("ITEMDESC")));
							resultJsonInt.put("PQTY", StrUtils
									.addZeroes(Double.parseDouble(StrUtils.fString((String) lineArr.get("QTY"))), "3"));
							resultJsonInt.put("PAMOUNT",
									StrUtils.addZeroes(
											Double.parseDouble(StrUtils.fString((String) lineArr.get("AMOUNT"))),
											numberOfDecimal));
							
							resultJsonInt.put("OUTLET", "");
							resultJsonInt.put("TERMINAL", "");
							resultJsonInt.put("ORDDAY", "");
							resultJsonInt.put("ORDDATE", "");
							resultJsonInt.put("TOTALPRICE", "");
							resultJsonInt.put("TOTALCOST", "");
							resultJsonInt.put("GPPER", "");
							/*resultJsonInt.put("FROMDATE", "");
							resultJsonInt.put("FROMSTATS", "");
							resultJsonInt.put("TODATE", "");
							resultJsonInt.put("TOSTATS", "");
							resultJsonInt.put("DURATION", "");*/
							
							resultJsonInt.put("BILLTIME", "");
							
							jsonArray.add(resultJsonInt);

						}
					}

				} else if (TYPE.equalsIgnoreCase("Customer")) {
					Hashtable ht = new Hashtable();

					if (StrUtils.fString(ORDERNO).length() > 0)
						ht.put("ORDERNO", ORDERNO);
					if (StrUtils.fString(CUSTOMERCODE).length() > 0)
						ht.put("CUSTOMERCODE", CUSTOMERCODE);
					if (StrUtils.fString(OUTLET).length() > 0)
						ht.put("OUTLET", OUTLET);
					if (StrUtils.fString(TERMINAL).length() > 0)
						ht.put("TERMINAL", TERMINAL);
					if (StrUtils.fString(CASHIER).length() > 0)
						ht.put("CASHIER", CASHIER);
					if (StrUtils.fString(SALESMAN).length() > 0)
						ht.put("SALESMAN", SALESMAN);
					if (StrUtils.fString(ITEM).length() > 0)
						ht.put("ITEM", ITEM);
					if (StrUtils.fString(PRD_BRAND_ID).length() > 0)
						ht.put("PRD_BRAND_ID", PRD_BRAND_ID);
					if (StrUtils.fString(PRD_TYPE_ID).length() > 0)
						ht.put("PRD_TYPE_ID", PRD_TYPE_ID);
					if (StrUtils.fString(PRD_CLS_ID).length() > 0)
						ht.put("PRD_CLS_ID", PRD_CLS_ID);
					if (StrUtils.fString(PLANT).length() > 0)
						ht.put("PLANT", PLANT);
					if (StrUtils.fString(PAYMENTTYPE).length() > 0)
						ht.put("PAYMENTTYPE", PAYMENTTYPE);

					movQryList = movHisUtil.getPosSalesorderSummaryByCustomerwiseWithTax(ht, fdate, tdate, "POS", PLANT);

					if (movQryList.size() > 0) {
						int Index = 0;

						for (int iCnt = 0; iCnt < movQryList.size(); iCnt++) {
							Map lineArr = (Map) movQryList.get(iCnt);

							Index = Index + 1;

							JSONObject resultJsonInt = new JSONObject();
							resultJsonInt.put("INDEX", Index);
							resultJsonInt.put("SALESDATE", "");
							resultJsonInt.put("DONO", "");
							resultJsonInt.put("CNAME", "");
							resultJsonInt.put("REFNUMBER", "");
							resultJsonInt.put("AMOUNT", "");
							resultJsonInt.put("DISCOUNT", "");
							resultJsonInt.put("EXPRICE", "");
							resultJsonInt.put("TAX", "");
							resultJsonInt.put("TOTALAMOUNT", "");
							resultJsonInt.put("PITEM", "");
							resultJsonInt.put("PITEMDESC", "");
							resultJsonInt.put("PQTY", "");
							resultJsonInt.put("PAMOUNT", "");
							resultJsonInt.put("ZCATEGORY", "");
							resultJsonInt.put("ZQTY", "");
							resultJsonInt.put("ZAMOUNT", "");
							resultJsonInt.put("PAYMODE", "");

							resultJsonInt.put("CCUSTOMER", StrUtils.fString((String) lineArr.get("CUSTOMER")));
							resultJsonInt.put("CCUSTOMERNAME", StrUtils.fString((String) lineArr.get("CUSTOMERNAME")));
							resultJsonInt.put("CAMOUNT",
									StrUtils.addZeroes(
											Double.parseDouble(StrUtils.fString((String) lineArr.get("AMOUNT"))),
											numberOfDecimal));
							resultJsonInt.put("CDISCOUNT",
									StrUtils.addZeroes(
											Double.parseDouble(
													StrUtils.fString((String) lineArr.get("DISCOUNT_AMOUNT"))),
											numberOfDecimal));
							resultJsonInt.put("CTAX",
									StrUtils.addZeroes(
											Double.parseDouble(StrUtils.fString((String) lineArr.get("TAXAMOUNT"))),
											numberOfDecimal));
							double finaltotal = Double.parseDouble(StrUtils.fString((String) lineArr.get("TOTAL_PRICE")));
							 if(ENABLE_POS.equalsIgnoreCase("1") && ISTAXREGISTRED.equalsIgnoreCase("0"))
								 	finaltotal = ((Double.parseDouble(StrUtils.fString((String) lineArr.get("AMOUNT"))))-Double.parseDouble(
											StrUtils.fString((String) lineArr.get("DISCOUNT_AMOUNT"))));
							resultJsonInt.put("CTOTALAMOUNT", StrUtils.addZeroes(finaltotal, numberOfDecimal));
//							resultJsonInt.put("CTOTALAMOUNT",
//									StrUtils.addZeroes(
//											Double.parseDouble(StrUtils.fString((String) lineArr.get("TOTAL_PRICE"))),
//											numberOfDecimal));
							
							resultJsonInt.put("OUTLET", "");
							resultJsonInt.put("TERMINAL", "");
							resultJsonInt.put("ORDDAY", "");
							resultJsonInt.put("ORDDATE", "");
							resultJsonInt.put("TOTALPRICE", "");
							resultJsonInt.put("TOTALCOST", "");
							resultJsonInt.put("GPPER", "");
							/*resultJsonInt.put("FROMDATE", "");
							resultJsonInt.put("FROMSTATS", "");
							resultJsonInt.put("TODATE", "");
							resultJsonInt.put("TOSTATS", "");
							resultJsonInt.put("DURATION", "");*/
							
							resultJsonInt.put("BILLTIME", "");

							jsonArray.add(resultJsonInt);

						}
					}

				} else if (TYPE.equalsIgnoreCase("Category")) {
					Hashtable ht = new Hashtable();

					if (StrUtils.fString(ORDERNO).length() > 0)
						ht.put("ORDERNO", ORDERNO);
					if (StrUtils.fString(CUSTOMERCODE).length() > 0)
						ht.put("CUSTOMERCODE", CUSTOMERCODE);
					if (StrUtils.fString(OUTLET).length() > 0)
						ht.put("OUTLET", OUTLET);
					if (StrUtils.fString(TERMINAL).length() > 0)
						ht.put("TERMINAL", TERMINAL);
					if (StrUtils.fString(CASHIER).length() > 0)
						ht.put("CASHIER", CASHIER);
					if (StrUtils.fString(SALESMAN).length() > 0)
						ht.put("SALESMAN", SALESMAN);
					if (StrUtils.fString(ITEM).length() > 0)
						ht.put("ITEM", ITEM);
					if (StrUtils.fString(PRD_BRAND_ID).length() > 0)
						ht.put("PRD_BRAND_ID", PRD_BRAND_ID);
					if (StrUtils.fString(PRD_TYPE_ID).length() > 0)
						ht.put("PRD_TYPE_ID", PRD_TYPE_ID);
					if (StrUtils.fString(PRD_CLS_ID).length() > 0)
						ht.put("PRD_CLS_ID", PRD_CLS_ID);
					if (StrUtils.fString(PLANT).length() > 0)
						ht.put("PLANT", PLANT);
					if (StrUtils.fString(PAYMENTTYPE).length() > 0)
						ht.put("PAYMENTTYPE", PAYMENTTYPE);

					movQryList = movHisUtil.getPosSalesorderSummaryByCategorywiseWithTax(ht, fdate, tdate, "POS", PLANT);

					if (movQryList.size() > 0) {
						int Index = 0;

						for (int iCnt = 0; iCnt < movQryList.size(); iCnt++) {
							Map lineArr = (Map) movQryList.get(iCnt);

							Index = Index + 1;

							JSONObject resultJsonInt = new JSONObject();
							resultJsonInt.put("INDEX", Index);
							resultJsonInt.put("SALESDATE", "");
							resultJsonInt.put("DONO", "");
							resultJsonInt.put("CNAME", "");
							resultJsonInt.put("REFNUMBER", "");
							resultJsonInt.put("AMOUNT", "");
							resultJsonInt.put("DISCOUNT", "");
							resultJsonInt.put("EXPRICE", "");
							resultJsonInt.put("TAX", "");
							resultJsonInt.put("TOTALAMOUNT", "");
							resultJsonInt.put("CCUSTOMER", "");
							resultJsonInt.put("CCUSTOMERNAME", "");
							resultJsonInt.put("CAMOUNT", "");
							resultJsonInt.put("CDISCOUNT", "");
							resultJsonInt.put("CTAX", "");
							resultJsonInt.put("CTOTALAMOUNT", "");
							resultJsonInt.put("PITEM", "");
							resultJsonInt.put("PITEMDESC", "");
							resultJsonInt.put("PQTY", "");
							resultJsonInt.put("PAMOUNT", "");

							resultJsonInt.put("ZCATEGORY", StrUtils.fString((String) lineArr.get("CATEGORY")));
							resultJsonInt.put("ZQTY", StrUtils
									.addZeroes(Double.parseDouble(StrUtils.fString((String) lineArr.get("QTY"))), "3"));
							resultJsonInt.put("ZAMOUNT",
									StrUtils.addZeroes(
											Double.parseDouble(StrUtils.fString((String) lineArr.get("AMOUNT"))),
											numberOfDecimal));
							
							resultJsonInt.put("OUTLET", "");
							resultJsonInt.put("TERMINAL", "");
							resultJsonInt.put("ORDDAY", "");
							resultJsonInt.put("ORDDATE", "");
							resultJsonInt.put("TOTALPRICE", "");
							resultJsonInt.put("TOTALCOST", "");
							resultJsonInt.put("GPPER", "");
							resultJsonInt.put("PAYMODE", "");
							/*resultJsonInt.put("FROMDATE", "");
							resultJsonInt.put("FROMSTATS", "");
							resultJsonInt.put("TODATE", "");
							resultJsonInt.put("TOSTATS", "");
							resultJsonInt.put("DURATION", "");*/
							
							resultJsonInt.put("BILLTIME", "");

							jsonArray.add(resultJsonInt);

						}
					}
				} else if (TYPE.equalsIgnoreCase("Terminal")) {
					Hashtable ht = new Hashtable();
					
					if (StrUtils.fString(ORDERNO).length() > 0)
						ht.put("ORDERNO", ORDERNO);
					if (StrUtils.fString(CUSTOMERCODE).length() > 0)
						ht.put("CUSTOMERCODE", CUSTOMERCODE);
					if (StrUtils.fString(OUTLET).length() > 0)
						ht.put("OUTLET", OUTLET);
					if (StrUtils.fString(TERMINAL).length() > 0)
						ht.put("TERMINAL", TERMINAL);
					if (StrUtils.fString(CASHIER).length() > 0)
						ht.put("CASHIER", CASHIER);
					if (StrUtils.fString(SALESMAN).length() > 0)
						ht.put("SALESMAN", SALESMAN);
					if (StrUtils.fString(ITEM).length() > 0)
						ht.put("ITEM", ITEM);
					if (StrUtils.fString(PRD_BRAND_ID).length() > 0)
						ht.put("PRD_BRAND_ID", PRD_BRAND_ID);
					if (StrUtils.fString(PRD_TYPE_ID).length() > 0)
						ht.put("PRD_TYPE_ID", PRD_TYPE_ID);
					if (StrUtils.fString(PRD_CLS_ID).length() > 0)
						ht.put("PRD_CLS_ID", PRD_CLS_ID);
					if (StrUtils.fString(PLANT).length() > 0)
						ht.put("PLANT", PLANT);
					if (StrUtils.fString(PAYMENTTYPE).length() > 0)
						ht.put("PAYMENTTYPE", PAYMENTTYPE);
					
					//movQryList = movHisUtil.getPosSalesorderSummaryByTerminalwise(ht, fdate, tdate, "POS", PLANT);
					//movQryList = movHisUtil.getPosSalesorderSummaryByTerminalwiseWithExg(ht, fdate, tdate, "POS", PLANT);
					movQryList = movHisUtil.getPosSalesorderSummaryByTerminalwiseWithExgWithTax(ht, fdate, tdate, "POS", PLANT);
					if (movQryList.size() > 0) {
						int Index = 0;
						
						for (int iCnt = 0; iCnt < movQryList.size(); iCnt++) {
							Map lineArr = (Map) movQryList.get(iCnt);
							
							Index = Index + 1;
							
							JSONObject resultJsonInt = new JSONObject();
							resultJsonInt.put("INDEX", Index);
							resultJsonInt.put("SALESDATE", "");
							resultJsonInt.put("DONO", "");
							resultJsonInt.put("CNAME", "");
							resultJsonInt.put("REFNUMBER", "");
							resultJsonInt.put("AMOUNT", "");
							resultJsonInt.put("DISCOUNT", "");
							resultJsonInt.put("EXPRICE", "");
							resultJsonInt.put("TAX",
									StrUtils.addZeroes(
											Double.parseDouble(StrUtils.fString((String) lineArr.get("TAXAMOUNT"))),
											numberOfDecimal));
							resultJsonInt.put("TOTALAMOUNT", "");
							resultJsonInt.put("CCUSTOMER", "");
							resultJsonInt.put("CCUSTOMERNAME", "");
							resultJsonInt.put("CAMOUNT", "");
							resultJsonInt.put("CDISCOUNT", "");
							resultJsonInt.put("CTAX", "");
							resultJsonInt.put("CTOTALAMOUNT", "");
							resultJsonInt.put("PITEM", "");
							resultJsonInt.put("PITEMDESC", "");
							resultJsonInt.put("PQTY", "");
							resultJsonInt.put("PAMOUNT", "");
							
							resultJsonInt.put("PAYMODE", "");
							resultJsonInt.put("ZCATEGORY", "");
							resultJsonInt.put("ZQTY", "");
							resultJsonInt.put("ZAMOUNT", "");
							
							resultJsonInt.put("OUTLET", StrUtils.fString((String) lineArr.get("OUTLET")));
							
							String termName = new OutletBeanDAO().getOutletTerminalname(PLANT, (String) lineArr.get("OUTLET"), (String) lineArr.get("TERMINAL"));
							
							resultJsonInt.put("TERMINAL", termName);
							resultJsonInt.put("ORDDAY", StrUtils.fString((String) lineArr.get("ORDDAY")));
							resultJsonInt.put("ORDDATE", StrUtils.fString((String) lineArr.get("ORDDATE")));
							resultJsonInt.put("TOTALPRICE", StrUtils.addZeroes(
									Double.parseDouble(StrUtils.fString((String) lineArr.get("TOTALPRICE"))),
									numberOfDecimal));
							resultJsonInt.put("TOTALCOST", StrUtils.addZeroes(
									Double.parseDouble(StrUtils.fString((String) lineArr.get("TOTALCOST"))),
									numberOfDecimal));
							//double gpval = (Double.parseDouble(StrUtils.fString((String) lineArr.get("TOTALCOST"))) - Double.parseDouble(StrUtils.fString((String) lineArr.get("TOTALPRICE")))) / Double.parseDouble(StrUtils.fString((String) lineArr.get("TOTALCOST")));
							//double gpval = Double.parseDouble(StrUtils.fString((String) lineArr.get("TOTALCOST"))) / (Double.parseDouble(StrUtils.fString((String) lineArr.get("TOTALPRICE"))) - Double.parseDouble(StrUtils.fString((String) lineArr.get("TOTALCOST"))))  ;
							//gpval =gpval*100;
							
							double gpval =  (Double.parseDouble(StrUtils.fString((String) lineArr.get("TOTALPRICE"))) - Double.parseDouble(StrUtils.fString((String) lineArr.get("TOTALCOST")))) / Double.parseDouble(StrUtils.fString((String) lineArr.get("TOTALPRICE"))) ;
							gpval =gpval*100;
							
							resultJsonInt.put("GPPER", StrUtils.addZeroes(gpval, numberOfDecimal));
							/*String FRDATE =StrUtils.fString((String) lineArr.get("FROMDATE"));
							String TRDATE =StrUtils.fString((String) lineArr.get("TODATE"));
							String TERMINAL_STARTTIME =StrUtils.fString((String) lineArr.get("TERMINAL_STARTTIME"));
							String TERMINAL_ENDTIME =StrUtils.fString((String) lineArr.get("TERMINAL_ENDTIME"));
							if (!FRDATE.equalsIgnoreCase("")) {
								resultJsonInt.put("FROMDATE", FRDATE.substring(8, 10) + ":" + FRDATE.substring(10, 12));
								String ffd =FRDATE.substring(0, 8)+TERMINAL_STARTTIME.replace(":","")+"00";
								
								String fdate1 = new DateUtils().parsecratDate(ffd);
								String tdate1 = new DateUtils().parsecratDate(FRDATE);
								
								java.text.SimpleDateFormat sdf
					            = new  java.text.SimpleDateFormat(
					                "dd-MM-yyyy HH:mm:ss");
								
							    Date d1 = sdf.parse(fdate1);
					            Date d2 = sdf.parse(tdate1);
							    
								long difference_In_Time
				                = d2.getTime() - d1.getTime();
								
								long difference_In_Seconds
				                = java.util.concurrent.TimeUnit.MILLISECONDS
				                      .toSeconds(difference_In_Time)
				                  % 60;
				 
				            long difference_In_Minutes
				                = java.util.concurrent.TimeUnit
				                      .MILLISECONDS
				                      .toMinutes(difference_In_Time)
				                  % 60;
				            
				            long difference_In_Hours
			                = java.util.concurrent.TimeUnit
			                      .MILLISECONDS
			                      .toHours(difference_In_Time)
			                  % 24;
								
								resultJsonInt.put("FROMSTATS", difference_In_Hours+":"+difference_In_Minutes);
							} else {
								resultJsonInt.put("FROMDATE", "");
								resultJsonInt.put("FROMSTATS", "");
							}
							if (!TRDATE.equalsIgnoreCase("")) {
								resultJsonInt.put("TODATE", TRDATE.substring(8, 10) + ":" + TRDATE.substring(10, 12));
								String ttd =TRDATE.substring(0, 8)+TERMINAL_ENDTIME.replace(":","")+"00";
								String fdate1 = new DateUtils().parsecratDate(ttd);
								String tdate1 = new DateUtils().parsecratDate(TRDATE);
								
								java.text.SimpleDateFormat sdf
					            = new  java.text.SimpleDateFormat(
					                "dd-MM-yyyy HH:mm:ss");
								
							    Date d1 = sdf.parse(fdate1);
					            Date d2 = sdf.parse(tdate1);
							    
								long difference_In_Time
				                = d2.getTime() - d1.getTime();
								
								long difference_In_Seconds
				                = java.util.concurrent.TimeUnit.MILLISECONDS
				                      .toSeconds(difference_In_Time)
				                  % 60;
				 
				            long difference_In_Minutes
				                = java.util.concurrent.TimeUnit
				                      .MILLISECONDS
				                      .toMinutes(difference_In_Time)
				                  % 60;
				            
				            long difference_In_Hours
			                = java.util.concurrent.TimeUnit
			                      .MILLISECONDS
			                      .toHours(difference_In_Time)
			                  % 24;
								resultJsonInt.put("TOSTATS", difference_In_Hours+":"+difference_In_Minutes);
							} else {
								resultJsonInt.put("TODATE", "");
								resultJsonInt.put("TOSTATS", "");
							}
							if (!FRDATE.equalsIgnoreCase("")&&!TRDATE.equalsIgnoreCase("")) {
								String fdate1 = new DateUtils().parsecratDate(FRDATE);
								String tdate1 = new DateUtils().parsecratDate(TRDATE);
								
								java.text.SimpleDateFormat sdf
					            = new  java.text.SimpleDateFormat(
					                "dd-MM-yyyy HH:mm:ss");
								
							    Date d1 = sdf.parse(fdate1);
					            Date d2 = sdf.parse(tdate1);
							    
								long difference_In_Time
				                = d2.getTime() - d1.getTime();
								
								long difference_In_Seconds
				                = java.util.concurrent.TimeUnit.MILLISECONDS
				                      .toSeconds(difference_In_Time)
				                  % 60;
				 
				            long difference_In_Minutes
				                = java.util.concurrent.TimeUnit
				                      .MILLISECONDS
				                      .toMinutes(difference_In_Time)
				                  % 60;
				            
				            long difference_In_Hours
			                = java.util.concurrent.TimeUnit
			                      .MILLISECONDS
			                      .toHours(difference_In_Time)
			                  % 24;
								
								resultJsonInt.put("DURATION", difference_In_Hours+":"+difference_In_Minutes);
							} else								
								resultJsonInt.put("DURATION", "");*/
							
							resultJsonInt.put("BILLTIME", "");
							
							
							jsonArray.add(resultJsonInt);
							
						}
					}

				} else {

					Hashtable ht = new Hashtable();

					if (StrUtils.fString(ORDERNO).length() > 0)
						ht.put("ORDERNO", ORDERNO);
					if (StrUtils.fString(CUSTOMERCODE).length() > 0)
						ht.put("CUSTOMERCODE", CUSTOMERCODE);
					if (StrUtils.fString(OUTLET).length() > 0)
						ht.put("OUTLET", OUTLET);
					if (StrUtils.fString(TERMINAL).length() > 0)
						ht.put("TERMINAL", TERMINAL);
					if (StrUtils.fString(CASHIER).length() > 0)
						ht.put("CASHIER", CASHIER);
					if (StrUtils.fString(SALESMAN).length() > 0)
						ht.put("SALESMAN", SALESMAN);
					if (StrUtils.fString(ITEM).length() > 0)
						ht.put("ITEM", ITEM);
					if (StrUtils.fString(PRD_BRAND_ID).length() > 0)
						ht.put("PRD_BRAND_ID", PRD_BRAND_ID);
					if (StrUtils.fString(PRD_TYPE_ID).length() > 0)
						ht.put("PRD_TYPE_ID", PRD_TYPE_ID);
					if (StrUtils.fString(PRD_CLS_ID).length() > 0)
						ht.put("PRD_CLS_ID", PRD_CLS_ID);
					if (StrUtils.fString(PLANT).length() > 0)
						ht.put("PLANT", PLANT);
					if (StrUtils.fString(PAYMENTTYPE).length() > 0)
						ht.put("PAYMENTTYPE", PAYMENTTYPE);

					movQryList = movHisUtil.getPosSalesorderSummaryByOrderwisewithtax(ht, fdate, tdate, "POS", PLANT);

					if (movQryList.size() > 0) {
						int Index = 0;

						for (int iCnt = 0; iCnt < movQryList.size(); iCnt++) {
							Map lineArr = (Map) movQryList.get(iCnt);

							Index = Index + 1;

							JSONObject resultJsonInt = new JSONObject();
							resultJsonInt.put("INDEX", Index);
							resultJsonInt.put("PITEM", "");
							resultJsonInt.put("PITEMDESC", "");
							resultJsonInt.put("PQTY", "");
							resultJsonInt.put("PAMOUNT", "");
							resultJsonInt.put("CCUSTOMER", "");
							resultJsonInt.put("CCUSTOMERNAME", "");
							resultJsonInt.put("CAMOUNT", "");
							resultJsonInt.put("CDISCOUNT", "");
							resultJsonInt.put("CTAX", "");
							resultJsonInt.put("CTOTALAMOUNT", "");
							resultJsonInt.put("ZCATEGORY", "");
							resultJsonInt.put("ZQTY", "");
							resultJsonInt.put("ZAMOUNT", "");

							resultJsonInt.put("SALESDATE", StrUtils.fString((String) lineArr.get("SALESDATE")));
							resultJsonInt.put("DONO", StrUtils.fString((String) lineArr.get("DONO")));
							
							/*ArrayList movQryListpay = new ArrayList();
							movQryListpay = movHisUtil.getPaymentModeByDono((String) lineArr.get("DONO"), PLANT);
							
							String paymode="";
							for (int iCntp = 0; iCntp < movQryListpay.size(); iCntp++) {
								Map lineArrpay = (Map) movQryListpay.get(iCntp);
								if(iCntp == 0) {
									paymode = StrUtils.fString((String) lineArrpay.get("PAYMENTMODE"));
								}else {
									paymode +=", "+StrUtils.fString((String) lineArrpay.get("PAYMENTMODE"));
								}
	
							}
							resultJsonInt.put("PAYMODE", paymode);
							*/
							
							resultJsonInt.put("PAYMODE", StrUtils.fString((String) lineArr.get("PAYMENTMODE")));
							
							resultJsonInt.put("CNAME", StrUtils.fString((String) lineArr.get("CustName")));
							resultJsonInt.put("REFNUMBER", StrUtils.fString((String) lineArr.get("JobNum")));
							resultJsonInt.put("AMOUNT",
									StrUtils.addZeroes(
											Double.parseDouble(StrUtils.fString((String) lineArr.get("AMOUNT"))),
											numberOfDecimal));
							resultJsonInt.put("DISCOUNT",
									StrUtils.addZeroes(
											Double.parseDouble(
													StrUtils.fString((String) lineArr.get("DISCOUNT_AMOUNT"))),
											numberOfDecimal));
							resultJsonInt.put("EXPRICE",
									StrUtils.addZeroes(
											Double.parseDouble(
													StrUtils.fString((String) lineArr.get("EXPRICE"))),
											numberOfDecimal));
							resultJsonInt.put("TAX",
									StrUtils.addZeroes(
											Double.parseDouble(StrUtils.fString((String) lineArr.get("TAXAMOUNT"))),
											numberOfDecimal));
							double finaltotal = Double.parseDouble(StrUtils.fString((String) lineArr.get("TOTAL_PRICE")));
							 /*if(ENABLE_POS.equalsIgnoreCase("1") && ISTAXREGISTRED.equalsIgnoreCase("0"))
								 	finaltotal = ((Double.parseDouble(StrUtils.fString((String) lineArr.get("AMOUNT"))))-Double.parseDouble(
											StrUtils.fString((String) lineArr.get("DISCOUNT_AMOUNT"))));*/
							if(ISTAXREGISTRED.equalsIgnoreCase("0")) {
							 	finaltotal = ((Double.parseDouble(StrUtils.fString((String) lineArr.get("AMOUNT"))))-Double.parseDouble(
										StrUtils.fString((String) lineArr.get("DISCOUNT_AMOUNT"))));
							 	finaltotal = ((finaltotal)-Double.parseDouble(
							 			StrUtils.fString((String) lineArr.get("EXPRICE"))));
							}
							resultJsonInt.put("TOTALAMOUNT", StrUtils.addZeroes(finaltotal, numberOfDecimal));
//							resultJsonInt.put("TOTALAMOUNT",
//									StrUtils.addZeroes(
//											Double.parseDouble(StrUtils.fString((String) lineArr.get("TOTAL_PRICE"))),
//											numberOfDecimal));
							
							resultJsonInt.put("OUTLET", "");
							resultJsonInt.put("TERMINAL", "");
							resultJsonInt.put("ORDDAY", "");
							resultJsonInt.put("ORDDATE", "");
							resultJsonInt.put("TOTALPRICE", "");
							resultJsonInt.put("TOTALCOST", "");
							resultJsonInt.put("GPPER", "");
							/*resultJsonInt.put("FROMDATE", "");
							resultJsonInt.put("FROMSTATS", "");
							resultJsonInt.put("TODATE", "");
							resultJsonInt.put("TOSTATS", "");
							resultJsonInt.put("DURATION", "");*/
							
							String billtime = new DateUtils().parsecratDatetoTime(StrUtils.fString((String) lineArr.get("CRAT")));
							resultJsonInt.put("BILLTIME", billtime);
							
							
							jsonArray.add(resultJsonInt);

						}
					}

				}

				resultJson.put("items", jsonArray);
				JSONObject resultJsonInt = new JSONObject();
				resultJsonInt.put("ERROR_MESSAGE", "NO ERRORS!");
				resultJsonInt.put("ERROR_CODE", "100");
				jsonArrayErr.add(resultJsonInt);
				resultJson.put("errors", jsonArrayErr);

			} catch (Exception e) {
				e.printStackTrace();
				jsonArray.add("");
				resultJson.put("items", jsonArray);
				resultJson.put("SEARCH_DATA", "");
				JSONObject resultJsonInt = new JSONObject();
				resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
				resultJsonInt.put("ERROR_CODE", "98");
				jsonArrayErr.add(resultJsonInt);
				resultJson.put("ERROR", jsonArrayErr);
			}
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			response.getWriter().write(resultJson.toString());
			response.getWriter().flush();
			response.getWriter().close();
		} else if (action.equalsIgnoreCase("PosSalesDiscountSummary")) {
				JSONObject resultJson = new JSONObject();
				JSONArray jsonArray = new JSONArray();
				JSONArray jsonArrayErr = new JSONArray();
				HTReportUtil movHisUtil = new HTReportUtil();
				PlantMstUtil plantmstutil = new PlantMstUtil();
				// DateUtils _dateUtils = new DateUtils();
				ArrayList movQryList = new ArrayList();
				
				try {
					
					String FDATE = StrUtils.fString(request.getParameter("FDATE"));
					String TDATE = StrUtils.fString(request.getParameter("TDATE"));
					String TYPE = StrUtils.fString(request.getParameter("TYPE"));
					String ORDERNO = StrUtils.fString(request.getParameter("ORDERNO"));
					String CUSTOMERCODE = StrUtils.fString(request.getParameter("CUSTOMERCODE"));
					String OUTLET = StrUtils.fString(request.getParameter("OUTLET"));
					String TERMINAL = StrUtils.fString(request.getParameter("TERMINAL"));
					String CASHIER = StrUtils.fString(request.getParameter("CASHIER"));
					String SALESMAN = StrUtils.fString(request.getParameter("SALESMAN"));
					String ITEM = StrUtils.fString(request.getParameter("ITEM"));
					String PRD_BRAND_ID = StrUtils.fString(request.getParameter("PRD_BRAND_ID"));
					String PRD_TYPE_ID = StrUtils.fString(request.getParameter("PRD_TYPE_ID"));
					String PRD_CLS_ID = StrUtils.fString(request.getParameter("PRD_CLS_ID"));
					String ACTION = StrUtils.fString(request.getParameter("ACTION"));
					String PLANT = StrUtils.fString(request.getParameter("PLANT"));
					String numberOfDecimal = new PlantMstDAO().getNumberOfDecimal(plant);
					String ENABLE_POS =new PlantMstDAO().getispos(PLANT);
					String ISTAXREGISTRED =new PlantMstDAO().getistaxregistred(PLANT);
					
					String fdate = "";
					String tdate = "";
					
					if (FDATE == null) {
						FDATE = "";
					} else {
						FDATE = FDATE.trim();
					}
					
					String curDate = DateUtils.getDate();
					
					if (FDATE.length() < 0 || FDATE == null || FDATE.equalsIgnoreCase("")) {
						FDATE = curDate;
					}
					
					if (FDATE.length() > 5) {
						fdate = FDATE.substring(6) + "-" + FDATE.substring(3, 5) + "-" + FDATE.substring(0, 2);
					}
					
					if (TDATE == null) {
						TDATE = "";
					} else {
						TDATE = TDATE.trim();
					}
					
					if (TDATE.length() > 5) {
						tdate = TDATE.substring(6) + "-" + TDATE.substring(3, 5) + "-" + TDATE.substring(0, 2);
					}
					
					if (TYPE.equalsIgnoreCase("Order")) {
						Hashtable ht = new Hashtable();
						
						if (StrUtils.fString(ORDERNO).length() > 0)
							ht.put("ORDERNO", ORDERNO);
						if (StrUtils.fString(CUSTOMERCODE).length() > 0)
							ht.put("CUSTOMERCODE", CUSTOMERCODE);
						if (StrUtils.fString(OUTLET).length() > 0)
							ht.put("OUTLET", OUTLET);
						if (StrUtils.fString(TERMINAL).length() > 0)
							ht.put("TERMINAL", TERMINAL);
						if (StrUtils.fString(CASHIER).length() > 0)
							ht.put("CASHIER", CASHIER);
						if (StrUtils.fString(SALESMAN).length() > 0)
							ht.put("SALESMAN", SALESMAN);
						if (StrUtils.fString(ITEM).length() > 0)
							ht.put("ITEM", ITEM);
						if (StrUtils.fString(PRD_BRAND_ID).length() > 0)
							ht.put("PRD_BRAND_ID", PRD_BRAND_ID);
						if (StrUtils.fString(PRD_TYPE_ID).length() > 0)
							ht.put("PRD_TYPE_ID", PRD_TYPE_ID);
						if (StrUtils.fString(PRD_CLS_ID).length() > 0)
							ht.put("PRD_CLS_ID", PRD_CLS_ID);
						if (StrUtils.fString(PLANT).length() > 0)
							ht.put("PLANT", PLANT);
						
						movQryList = movHisUtil.getPosSalesDiscountSummaryByOrderwise(ht, fdate, tdate, "POS", PLANT);
						
						if (movQryList.size() > 0) {
							int Index = 0;
							
							for (int iCnt = 0; iCnt < movQryList.size(); iCnt++) {
								Map lineArr = (Map) movQryList.get(iCnt);
								
								Index = Index + 1;
								
								JSONObject resultJsonInt = new JSONObject();
								resultJsonInt.put("INDEX", Index);
								
								resultJsonInt.put("PITEM", "");
								resultJsonInt.put("PITEMDESC", "");
								resultJsonInt.put("PQTY", "");
								resultJsonInt.put("PAMOUNT", "");
								resultJsonInt.put("CCUSTOMER", "");
								resultJsonInt.put("CCUSTOMERNAME", "");
								resultJsonInt.put("CAMOUNT", "");
								resultJsonInt.put("CDISCOUNT", "");
								resultJsonInt.put("CTAX", "");
								resultJsonInt.put("CTOTALAMOUNT", "");
								resultJsonInt.put("ZCATEGORY", "");
								resultJsonInt.put("ZQTY", "");
								resultJsonInt.put("ZAMOUNT", "");
								
								resultJsonInt.put("SALESDATE", StrUtils.fString((String) lineArr.get("SALESDATE")));
								resultJsonInt.put("DONO", StrUtils.fString((String) lineArr.get("DONO")));
								resultJsonInt.put("CNAME", StrUtils.fString((String) lineArr.get("CustName")));
								resultJsonInt.put("REFNUMBER", StrUtils.fString((String) lineArr.get("JobNum")));
								resultJsonInt.put("AMOUNT",
										StrUtils.addZeroes(
												Double.parseDouble(StrUtils.fString((String) lineArr.get("AMOUNT"))),
												numberOfDecimal));
								resultJsonInt.put("DISCOUNT",
										StrUtils.addZeroes(
												Double.parseDouble(
														StrUtils.fString((String) lineArr.get("DISCOUNT_AMOUNT"))),
												numberOfDecimal));
								resultJsonInt.put("TAX",
										StrUtils.addZeroes(
												Double.parseDouble(StrUtils.fString((String) lineArr.get("TAXAMOUNT"))),
												numberOfDecimal));
								double finaltotal = Double.parseDouble(StrUtils.fString((String) lineArr.get("TOTAL_PRICE")));
								 if(ENABLE_POS.equalsIgnoreCase("1") && ISTAXREGISTRED.equalsIgnoreCase("0"))
									 	finaltotal = ((Double.parseDouble(StrUtils.fString((String) lineArr.get("AMOUNT"))))-Double.parseDouble(
												StrUtils.fString((String) lineArr.get("DISCOUNT_AMOUNT"))));
								resultJsonInt.put("TOTALAMOUNT", StrUtils.addZeroes(finaltotal, numberOfDecimal));
								jsonArray.add(resultJsonInt);
								
							}
						}
						
					}
					
					resultJson.put("items", jsonArray);
					JSONObject resultJsonInt = new JSONObject();
					resultJsonInt.put("ERROR_MESSAGE", "NO ERRORS!");
					resultJsonInt.put("ERROR_CODE", "100");
					jsonArrayErr.add(resultJsonInt);
					resultJson.put("errors", jsonArrayErr);
					
				} catch (Exception e) {
					e.printStackTrace();
					jsonArray.add("");
					resultJson.put("items", jsonArray);
					resultJson.put("SEARCH_DATA", "");
					JSONObject resultJsonInt = new JSONObject();
					resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
					resultJsonInt.put("ERROR_CODE", "98");
					jsonArrayErr.add(resultJsonInt);
					resultJson.put("ERROR", jsonArrayErr);
				}
				response.setContentType("application/json");
				response.setCharacterEncoding("UTF-8");
				response.getWriter().write(resultJson.toString());
				response.getWriter().flush();
				response.getWriter().close();
			} else if (action.equalsIgnoreCase("PosShiftCloseSummary")) {
			JSONObject resultJson = new JSONObject();
			JSONArray jsonArray = new JSONArray();
			JSONArray jsonArrayErr = new JSONArray();
			HTReportUtil movHisUtil = new HTReportUtil();
			PlantMstUtil plantmstutil = new PlantMstUtil();
			// DateUtils _dateUtils = new DateUtils();
			ArrayList movQryList = new ArrayList();

			try {

				String FDATE = StrUtils.fString(request.getParameter("FDATE"));
				String TDATE = StrUtils.fString(request.getParameter("TDATE"));
				String OUTLET = StrUtils.fString(request.getParameter("OUTLETCODE"));
				String TERMINAL = StrUtils.fString(request.getParameter("TERMINALCODE"));
				String CASHIER = StrUtils.fString(request.getParameter("CASHIER_NAME"));
				String PLANT = StrUtils.fString(request.getParameter("PLANT"));
				
				String numberOfDecimal = new PlantMstDAO().getNumberOfDecimal(PLANT);
				

				String fdate = "";
				String tdate = "";

				if (FDATE == null) {
					FDATE = "";
				} else {
					FDATE = FDATE.trim();
				}

				String curDate = DateUtils.getDate();

				if (FDATE.length() < 0 || FDATE == null || FDATE.equalsIgnoreCase("")) {
					FDATE = curDate;
				}

				if (FDATE.length() > 5) {
					fdate = FDATE.substring(6) + "-" + FDATE.substring(3, 5) + "-" + FDATE.substring(0, 2);
				}

				if (TDATE == null) {
					TDATE = "";
				} else {
					TDATE = TDATE.trim();
				}

				if (TDATE.length() > 5) {
					tdate = TDATE.substring(6) + "-" + TDATE.substring(3, 5) + "-" + TDATE.substring(0, 2);
				}

				Hashtable ht = new Hashtable();

				if (StrUtils.fString(OUTLET).length() > 0)
					ht.put("OUTLET", OUTLET);
				if (StrUtils.fString(TERMINAL).length() > 0)
					ht.put("TERMINAL", TERMINAL);
				if (StrUtils.fString(CASHIER).length() > 0)
					ht.put("CASHIER", CASHIER);
				if (StrUtils.fString(PLANT).length() > 0)
					ht.put("PLANT", PLANT);

				movQryList = movHisUtil.getPosShiftCloseSummary(ht, fdate, tdate, PLANT);

				if (movQryList.size() > 0) {
					int Index = 0;

					for (int iCnt = 0; iCnt < movQryList.size(); iCnt++) {
						Map lineArr = (Map) movQryList.get(iCnt);

						Index = Index + 1;

						JSONObject resultJsonInt = new JSONObject();
						resultJsonInt.put("INDEX", Index);
						resultJsonInt.put("ID", StrUtils.fString((String) lineArr.get("ID")));
						resultJsonInt.put("SHIFT", StrUtils.fString((String) lineArr.get("SHIFTID")));
						resultJsonInt.put("SHIFTDATE", StrUtils.fString((String) lineArr.get("SHIFTDATE")));
						resultJsonInt.put("OUTLET", StrUtils.fString((String) lineArr.get("OUTLET")));
						resultJsonInt.put("TERMINAL", StrUtils.fString((String) lineArr.get("TERMINAL")));
						resultJsonInt.put("CASHIER_ID", StrUtils.fString((String) lineArr.get("EMPNO")));
						resultJsonInt.put("SHIFTDATE", StrUtils.fString((String) lineArr.get("SHIFTDATE")));
						resultJsonInt.put("CASHIER", StrUtils.fString((String) lineArr.get("FNAME")));
						//resultJsonInt.put("SALESCOUNT",StrUtils.addZeroes(Double.parseDouble(StrUtils.fString((String) lineArr.get("SALESCOUNT"))),"2"));
						
						resultJsonInt.put("SALESCOUNT",Integer.valueOf(StrUtils.fString((String) lineArr.get("SALESCOUNT"))));
						resultJsonInt.put("CASH",
								StrUtils.addZeroes(Double.parseDouble(StrUtils.fString((String) lineArr.get("CASH"))),
										numberOfDecimal));
						resultJsonInt.put("TOTALSALESCOST",
								StrUtils.addZeroes(Double.parseDouble(StrUtils.fString((String) lineArr.get("TOTALSALESCOST"))),
										numberOfDecimal));
						resultJsonInt.put("OTHERSALES",
								StrUtils.addZeroes(Double.parseDouble(StrUtils.fString((String) lineArr.get("OTHERSALES"))),
										numberOfDecimal));
						resultJsonInt.put("VOIDEDSALES",
								StrUtils.addZeroes(Double.parseDouble(StrUtils.fString((String) lineArr.get("VOIDEDSALES"))),
										numberOfDecimal));
						resultJsonInt.put("TOTALDISCOUNT",
								StrUtils.addZeroes(Double.parseDouble(StrUtils.fString((String) lineArr.get("TOTALDISCOUNT"))),
										numberOfDecimal));
						resultJsonInt.put("FLOATAMOUNT",
								StrUtils.addZeroes(Double.parseDouble(StrUtils.fString((String) lineArr.get("FLOATAMOUNT"))),
										numberOfDecimal));
						resultJsonInt.put("RETURNEDAMOUNT",
								StrUtils.addZeroes(Double.parseDouble(StrUtils.fString((String) lineArr.get("RETURNEDAMOUNT"))),
										numberOfDecimal));
						resultJsonInt.put("EXPENSE",
								StrUtils.addZeroes(Double.parseDouble(StrUtils.fString((String) lineArr.get("EXPENSE"))),
										numberOfDecimal));
						resultJsonInt.put("TOTALAMOUNT",
								StrUtils.addZeroes(Double.parseDouble(StrUtils.fString((String) lineArr.get("TOTALAMOUNT"))),
										numberOfDecimal));
						
						Double tamount = Double.valueOf(StrUtils.fString((String) lineArr.get("TOTALAMOUNT")));
						Double tcost = Double.valueOf(StrUtils.fString((String) lineArr.get("TOTALSALESCOST")));
						//Double gp = (tcost/(tamount-tcost))*100;
						Double gp = ((tamount-tcost)/tamount)*100;
						if(gp.isNaN()) {
							gp = 0.0;
						}
						resultJsonInt.put("GP",StrUtils.addZeroes(gp,"2"));
						System.out.println("GP---------------------------"+gp);
						
						//double se = Double.valueOf((String) lineArr.get("EXPECTEDDRAWERAMOUNT")) - Double.valueOf((String) lineArr.get("DRAWERAMOUNT"));
						double se = Double.valueOf((String) lineArr.get("DRAWERAMOUNT")) - Double.valueOf((String) lineArr.get("EXPECTEDDRAWERAMOUNT"));
						String shortextra = StrUtils.addZeroes(se, numberOfDecimal);
						/*if(Double.valueOf((String) lineArr.get("EXPECTEDDRAWERAMOUNT")) > Double.valueOf((String) lineArr.get("DRAWERAMOUNT"))){
							shortextra = "-"+shortextra;
						}*/
						
						resultJsonInt.put("DIFFAMOUNT",shortextra);
						
						jsonArray.add(resultJsonInt);

					}
				}

				resultJson.put("items", jsonArray);
				JSONObject resultJsonInt = new JSONObject();
				resultJsonInt.put("ERROR_MESSAGE", "NO ERRORS!");
				resultJsonInt.put("ERROR_CODE", "100");
				jsonArrayErr.add(resultJsonInt);
				resultJson.put("errors", jsonArrayErr);

			} catch (Exception e) {
				e.printStackTrace();
				jsonArray.add("");
				resultJson.put("items", jsonArray);
				resultJson.put("SEARCH_DATA", "");
				JSONObject resultJsonInt = new JSONObject();
				resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
				resultJsonInt.put("ERROR_CODE", "98");
				jsonArrayErr.add(resultJsonInt);
				resultJson.put("ERROR", jsonArrayErr);
			}
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			response.getWriter().write(resultJson.toString());
			response.getWriter().flush();
			response.getWriter().close();
		}

	}
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String[] pathInfo = request.getPathInfo().split("/");
		String action = pathInfo[1];
		String username = StrUtils.fString((String) request.getSession().getAttribute("LOGIN_USER")).trim();
		String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
		
			if(action.equalsIgnoreCase("reports")) {
					try {
						String msg = StrUtils.fString(request.getParameter("msg"));
						request.setAttribute("Msg", msg);
						RequestDispatcher rd = request.getRequestDispatcher("/jsp/PosReports.jsp");
					rd.forward(request, response);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			if(action.equalsIgnoreCase("salesreports")) {
				try {
					String msg = StrUtils.fString(request.getParameter("msg"));
					request.setAttribute("Msg", msg);
					RequestDispatcher rd = request.getRequestDispatcher("/jsp/PosSalesReportsSummary.jsp");
				rd.forward(request, response);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		
			if(action.equalsIgnoreCase("salesreportsdetail")) {
				try {
					String type = StrUtils.fString(request.getParameter("TYPE"));
					RequestDispatcher rd = request.getRequestDispatcher("/jsp/PosSalesReportsDetail.jsp");
					if(type.equals("Order") || (type.equals("Customer")) || (type.equals("Void")) || (type.equals("Return")) || (type.equals("Discount")) || (type.equals("FOC")) ){
						 rd = request.getRequestDispatcher("/jsp/PosSalesReportsDetail.jsp");
					}else if(type.equals("Product")){
						 rd = request.getRequestDispatcher("/jsp/PosSalesReportsPrdDetail.jsp");
					}
					rd.forward(request, response);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
			if(action.equalsIgnoreCase("shiftclose")) {
				try {
					String msg = StrUtils.fString(request.getParameter("msg"));
					request.setAttribute("Msg", msg);
					RequestDispatcher rd = request.getRequestDispatcher("/jsp/ShiftCloseSummary.jsp");
					rd.forward(request, response);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
			if(action.equalsIgnoreCase("shiftclosedetail")) {
				try {
					String msg = StrUtils.fString(request.getParameter("msg"));
					request.setAttribute("Msg", msg);
					RequestDispatcher rd = request.getRequestDispatcher("/jsp/POSShiftCloseDetail.jsp");
					rd.forward(request, response);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
			if(action.equalsIgnoreCase("voidsales")) {
				try {
					String msg = StrUtils.fString(request.getParameter("msg"));
					request.setAttribute("Msg", msg);
					RequestDispatcher rd = request.getRequestDispatcher("/jsp/VoidSalesReportsSummary.jsp");
					rd.forward(request, response);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
			if(action.equalsIgnoreCase("expenses")) {
				try {
					String msg = StrUtils.fString(request.getParameter("msg"));
					request.setAttribute("Msg", msg);
					RequestDispatcher rd = request.getRequestDispatcher("/jsp/posExpenses.jsp");
					rd.forward(request, response);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
			if(action.equalsIgnoreCase("return")) {
				try {
					String msg = StrUtils.fString(request.getParameter("msg"));
					request.setAttribute("Msg", msg);
					RequestDispatcher rd = request.getRequestDispatcher("/jsp/ReturnSalesReportsSummary.jsp");
					rd.forward(request, response);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
			if(action.equalsIgnoreCase("discount")) {
				try {
					String msg = StrUtils.fString(request.getParameter("msg"));
					request.setAttribute("Msg", msg);
					RequestDispatcher rd = request.getRequestDispatcher("/jsp/DiscountSummary.jsp");
					rd.forward(request, response);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
			if(action.equalsIgnoreCase("FOC")) {
				try {
					String msg = StrUtils.fString(request.getParameter("msg"));
					request.setAttribute("Msg", msg);
					RequestDispatcher rd = request.getRequestDispatcher("/jsp/FOCSummary.jsp");
					rd.forward(request, response);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
			if(action.equalsIgnoreCase("revenuereports")) {
				try {
					String msg = StrUtils.fString(request.getParameter("msg"));
					request.setAttribute("Msg", msg);
					//RequestDispatcher rd = request.getRequestDispatcher("/jsp/PosRevenueReportsSummary.jsp");
					RequestDispatcher rd = request.getRequestDispatcher("/jsp/PosMonthlyConsilidatedSummary.jsp");
					rd.forward(request, response);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
			if(action.equalsIgnoreCase("revenuereportsmonth")) {
				try {
					String OUTLET = StrUtils.fString(request.getParameter("OUTLET"));
					String TERMINAL = StrUtils.fString(request.getParameter("TERMINAL"));
					String SYEAR = StrUtils.fString(request.getParameter("SYEAR"));
					String SMONTH = StrUtils.fString(request.getParameter("SMONTH"));
					
					Calendar cal = Calendar.getInstance();
				    cal.set(Integer.valueOf(SYEAR), Integer.valueOf(SMONTH), 1);
				    cal.add(Calendar.MONTH, -1);
				    System.out.println(cal.getTime());
				    int res = cal.getActualMaximum(Calendar.DATE);
				    
				    String scmonth = SMONTH;
				    String scyear = SYEAR;
				    if(scmonth.length() == 1) {
				    	scmonth="0"+scmonth;
				    } 
				    String pmfdate = "01/"+scmonth+"/"+scyear;
				    String pmtdate = String.valueOf(res)+"/"+scmonth+"/"+scyear;
					
					//request.setAttribute("OUTLET_CODE", OUTLET);
					//request.setAttribute("TERMINAL_CODE", TERMINAL);
					//request.setAttribute("FROM_DATE", pmfdate);
					//request.setAttribute("TO_DATE", pmtdate);
					
					RequestDispatcher rd = request.getRequestDispatcher("/jsp/PosRevenueReportsSummary.jsp?OUTLET_CODE="+OUTLET+"&TERMINALCODE="+TERMINAL+"&FROM_DATE="+pmfdate+"&TO_DATE="+pmtdate);
					rd.forward(request, response);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
			if(action.equalsIgnoreCase("cashbankin")) {
				try {
					String msg = StrUtils.fString(request.getParameter("msg"));
					request.setAttribute("Msg", msg);
					RequestDispatcher rd = request.getRequestDispatcher("/jsp/NewCashBankin.jsp");
					rd.forward(request, response);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		
	}
	
	
	@Override
	public HashMap<String, String> populateMapData(String companyCode, String userCode) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setMapDataToLogger(HashMap<String, String> dataForLogging) {
		// TODO Auto-generated method stub
		
	}

}

		