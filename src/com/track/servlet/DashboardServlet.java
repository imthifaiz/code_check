package com.track.servlet;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
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

import com.track.constants.MLoggerConstant;
import com.track.dao.BillDAO;
import com.track.dao.BillPaymentDAO;
import com.track.dao.CustMstDAO;
import com.track.dao.DoHdrDAO;
import com.track.dao.InvoiceDAO;
import com.track.dao.InvoicePaymentDAO;
import com.track.dao.JournalDAO;
import com.track.dao.OutletBeanDAO;
import com.track.dao.ParentChildCmpDetDAO;
import com.track.dao.PlantMstDAO;
import com.track.dao.VendMstDAO;
import com.track.db.util.AgeingUtil;
import com.track.db.util.BillUtil;
import com.track.db.util.CustUtil;
import com.track.db.util.DOUtil;
import com.track.db.util.ESTUtil;
import com.track.db.util.ExpensesUtil;
import com.track.db.util.InvMstUtil;
import com.track.db.util.InvoiceUtil;
import com.track.db.util.ItemMstUtil;
import com.track.db.util.POUtil;
import com.track.db.util.VendUtil;
import com.track.gates.DbBean;
import com.track.gates.userBean;
import com.track.service.ReportService;
import com.track.serviceImplementation.ReportServiceImpl;
import com.track.util.DateUtils;
import com.track.util.IMLogger;
import com.track.util.MLogger;
import com.track.util.StrUtils;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * Servlet implementation class DashboardServlet
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class DashboardServlet extends HttpServlet implements IMLogger {
	private static final long serialVersionUID = 1L;
//	private boolean printLog = MLoggerConstant.DashboardServlet_PRINTPLANTMASTERLOG;
	private boolean printInfo = MLoggerConstant.DashboardServlet_PRINTPLANTMASTERINFO;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public DashboardServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		JSONObject jsonObjectResult = new JSONObject();

		HttpSession session = request.getSession(false);
		String action = StrUtils.fString(request.getParameter("ACTION")).trim();
		String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
		String userName = StrUtils.fString((String) request.getSession().getAttribute("LOGIN_USER")).trim();
		this.setMapDataToLogger(this.populateMapData(plant, userName));
		JSONArray jsonResultArray = new JSONArray();
		if (this.printInfo) {
			diaplayInfoLogs(request);
		}
		if ("CURRENT_DATE".equals(action)) {
			jsonObjectResult = this.getCurrentDate(request);
		} else if ("TOTAL_RECEIPT".equals(action)) {
			jsonObjectResult = this.getTotalReceipt(request);
		} else if ("TOTAL_ISSUE".equals(action)) {
			jsonObjectResult = this.getTotalIssue(request);
		} else if ("TOP_RECEIVED_PRODUCTS".equals(action)) {
			jsonObjectResult = this.getTopReceivedProducts(request);
		} else if ("TOP_ISSUED_PRODUCTS".equals(action)) {
			jsonObjectResult = this.getTopIssuedProducts(request);
		} else if ("EXPIRING_PRODUCTS".equals(action)) {
			jsonObjectResult = this.getExpiringProducts(request);
		} else if ("LOW_STOCK_PRODUCTS".equals(action)) {
			jsonObjectResult = this.getLowStockProducts(request);
		} else if ("TOTAL_RECEIPT_BY_PRODUCT".equals(action)) {
			jsonObjectResult = this.getTotalReceiptByProduct(request);
		} else if ("TOTAL_ISSUE_BY_PRODUCT".equals(action)) {
			jsonObjectResult = this.getTotalIssueByProduct(request);
		} else if ("NEW_SUPPLIERS".equals(action)) {
			jsonObjectResult = this.getNewSuppliers(request);
		} else if ("TOTAL_SUPPLIERS".equals(action)) {
			jsonObjectResult = this.getTotalSuppliers(request);
		} else if ("TOP_SUPPLIERS".equals(action)) {
			jsonObjectResult = this.getTopSuppliers(request);
		} else if ("PURCHASE_SUMMARY".equals(action)) {
			jsonObjectResult = this.getPurchaseSummary(request);
		} else if ("PURCHASE_DELIVERY_DATE".equals(action)) {
			jsonResultArray = this.getPurchaseDeliveryDate(request);
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			this.mLogger.auditInfo(this.printInfo, jsonResultArray.toString());
			response.getWriter().write(jsonResultArray.toString());
			response.getWriter().flush();
			response.getWriter().close();
		} else if ("EXPIRED_PURCHASE_ORDER".equals(action)) {
			jsonResultArray = this.getExpiredPurchaseOrder(request);
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			this.mLogger.auditInfo(this.printInfo, jsonResultArray.toString());
			response.getWriter().write(jsonResultArray.toString());
			response.getWriter().flush();
			response.getWriter().close();
		} else if ("SALES_SUMMARY".equals(action)) {
			jsonObjectResult = this.getSalesSummary(request);
		} else if ("TOP_CUSTOMERS".equals(action)) {
			jsonObjectResult = this.getTopCustomers(request);
		} else if ("NEW_CUSTOMERS".equals(action)) {
			jsonObjectResult = this.getNewCustomers(request);
		} else if ("TOTAL_CUSTOMERS".equals(action)) {
			jsonObjectResult = this.getTotalCustomers(request);
		} else if ("PENDING_SALES_ESTIMATE".equals(action)) {
			jsonResultArray = this.getPendingSalesEstimate(request);
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			this.mLogger.auditInfo(this.printInfo, jsonResultArray.toString());
			response.getWriter().write(jsonResultArray.toString());
			response.getWriter().flush();
			response.getWriter().close();
		} else if ("SALES_ORDER_DELIVERY_DATE".equals(action)) {
			jsonResultArray = this.getSalesOrderDeliveryDate(request);
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			this.mLogger.auditInfo(this.printInfo, jsonResultArray.toString());
			response.getWriter().write(jsonResultArray.toString());
			response.getWriter().flush();
			response.getWriter().close();
		} else if ("TOP_SALES_PRODUCT".equals(action)) {
			jsonObjectResult = this.getTopSalesProduct(request);
		} else if ("PO_WO_PRICE_SUMMARY".equals(action)) {
			jsonObjectResult = this.getPoWoPriceSummary(request);
		} else if ("GRN_SUMMARY".equals(action)) {
			jsonObjectResult = this.getGrnSummary(request);
		} else if ("GRI_SUMMARY".equals(action)) {
			jsonObjectResult = this.getGriSummary(request);
		} else if ("SALES_ORDER_EXPIRED_DELIVERY_DATE".equals(action)) {
			jsonResultArray = this.getSalesOrderExpiredDeliveryDate(request);
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			this.mLogger.auditInfo(this.printInfo, jsonResultArray.toString());
			response.getWriter().write(jsonResultArray.toString());
			response.getWriter().flush();
			response.getWriter().close();
		} else if ("GET_READY_TO_PACK_ORDERS".equals(action)) {
			jsonObjectResult = this.getReadyToPackOrders(request);
		} else if ("TOTAL_PURCHASE_BY_BILL".equals(action)) {
			jsonObjectResult = this.getTotalPurchaseByBill(request);
		}else if ("TOTAL_PURCHASE_BY_BILL_FROM_JOURNAL".equals(action)) {
			jsonObjectResult = this.getTotalPurchaseByBillFromJournal(request);
		} else if ("TOTAL_SALES_BY_INVOICE".equals(action)) {
			jsonObjectResult = this.getTotalSalesByInvoice(request);
		}else if ("TOTAL_SALES_BY_INVOICE_FROM_JOURNAL".equals(action)) {
			jsonObjectResult = this.getTotalSalesByInvoiceFromJournal(request);
		}else if ("TOTAL_PURCHASE_SUMMARY_BY_BILL".equals(action)) {
			jsonObjectResult = this.getTotalPurchaseSummaryByBill(request);
		}else if ("TOTAL_PURCHASE_SUMMARY_BY_BILL_FROM_JOURNAL".equals(action)) {
			jsonObjectResult = this.getTotalPurchaseSummaryByBillFromJournal(request);
		}else if ("TOTAL_SALES_SUMMARY_BY_INVOICE".equals(action)) {
			jsonObjectResult = this.getTotalSalesSummaryByInvoice(request);
		}else if ("TOTAL_SALES_SUMMARY_BY_INVOICE_FROM_JOURNAL".equals(action)) {
			jsonObjectResult = this.getTotalSalesSummaryByInvoiceFromJournal(request);
		} else if ("GET_TOTAL_INCOME".equals(action)) {
			jsonObjectResult = this.getTotalIncomeForDashboard(request);
		} else if ("GET_TOTAL_INCOME_FROM_JOURNAL".equals(action)) {
			jsonObjectResult = this.getTotalIncomeFromJournalForDashboard(request);
		} else if ("GET_TOTAL_EXPENSE".equals(action)) {
			jsonObjectResult = this.getTotalExpenseForDashboard(request);
		} else if ("GET_TOTAL_EXPENSE_FROM_JOURNAL".equals(action)) {
			jsonObjectResult = this.getTotalExpenseForDashboardFromJournal(request);
		}  else if ("GET_TOTAL_PAYMENT_MADE_FROM_JOURNAL".equals(action)) {
			jsonObjectResult = this.getTotalPaymentMadeForDashboardFromJournal(request);
		}  else if ("GET_TOTAL_PAYMENT_RECEIPT_FROM_JOURNAL".equals(action)) {
			jsonObjectResult = this.getTotalPaymentReceiptForDashboardFromJournal(request);
		} else if ("GET_TOTAL_INCOME_SUMMARY".equals(action)) {
			jsonObjectResult = this.getTotalIncomeSummaryByInvoice(request);
		} else if ("GET_TOTAL_INCOME_SUMMARY_FROM_JOURNAL".equals(action)) {
			jsonObjectResult = this.getTotalIncomeSummaryByInvoiceFromJournal(request);
		} else if ("GET_TOTAL_EXPENSE_SUMMARY".equals(action)) {
			jsonObjectResult = this.getTotalExpenseSummaryByBill(request);
		} else if ("GET_TOTAL_EXPENSE_SUMMARY_FROM_JOURNY".equals(action)) {
			jsonObjectResult = this.getTotalExpenseSummaryByBillFromJourny(request);
		} else if ("GET_TOTAL_PAYMENT_MADE_SUMMARY_FROM_JOURNY".equals(action)) {
			jsonObjectResult = this.getTotalPaymentMadeSummaryFromJourny(request);
		} else if ("GET_TOTAL_PAYMENT_RECEIPT_SUMMARY_FROM_JOURNY".equals(action)) {
			jsonObjectResult = this.getTotalPaymentReceiptSummaryFromJourny(request);
		} else if ("GET_PAYMENT_PDC".equals(action)) {
			jsonObjectResult = this.getPaymentPdcForDashboard(request);
		} else if ("GET_PAYMENT_RECV_PDC".equals(action)) {
			jsonObjectResult = this.getPaymentRecvPdcForDashboard(request);
		} else if ("GET_ACCOUNT_PAYABLE".equals(action)) {
			jsonObjectResult = this.getAccountPayableForDashboard(request);
		} else if ("GET_ACCOUNT_RECEIVABLE".equals(action)) {
			jsonObjectResult = this.getAccountReceivableForDashboard(request);
		} else if ("GET_ACCOUNT_PAYABLE_BY_SUPPLIER".equals(action)) {
			jsonObjectResult = this.getPayableAmountBySupplier(request);
		} else if ("GET_ACCOUNT_RECEIVABLE_BY_CUSTOMER".equals(action)) {
			jsonObjectResult = this.getReceivableAmountByCustomer(request);
		} else if (action.equalsIgnoreCase("PRINTAGEINGREPORT")) {			
			jsonObjectResult = this.PrintAgeingReport(request);			
		} else if ("GET_TOTAL_ASSET".equals(action)) {
			jsonObjectResult = this.getTotalAsset(request);
		} else if ("GET_TOTAL_CASH_IN_HAND".equals(action)) {
			jsonObjectResult = this.getTotalCashInHand(request);
		} else if ("GET_TOTAL_CASH_IN_HAND_ALL".equals(action)) {
			jsonObjectResult = this.getTotalCashInHandAll(request);
		} else if ("GET_TOTAL_LIABILITY".equals(action)) {
			jsonObjectResult = this.getTotalLiability(request);
		} else if ("GET_TOTAL_CASH_AT_BANK".equals(action)) {
			jsonObjectResult = this.getTotalCashAtBank(request);
		} else if ("GET_TOTAL_CASH_AT_BANK_ALL".equals(action)) {
			jsonObjectResult = this.getTotalCashAtBankAll(request);
		} else if ("GET_NET_PROFIT".equals(action)) {
			jsonObjectResult = this.getNetProfit(request);
		} else if ("GET_GROSS_PROFIT".equals(action)) {
			jsonObjectResult = this.getGrossProfit(request);
		}
		 else if ("GET_MENU_LIST_FOR_SUGGESTION".equals(action)) {
			jsonObjectResult = this.getProductListForSuggestion(request);
		}else if("LOGIN_TRANSFER".equals(action)) {
			String logpage = StrUtils.fString(request.getParameter("LOGPAGE")).trim();
			try {
				 userBean ub = new userBean();
				 String user_id = userName;
				 String company = plant;
				if(logpage.equalsIgnoreCase("INVENTORY")){
					  request.getSession().setAttribute("SYSTEMNOW", "INVENTORY");
					 
			          String level_name = ub.getUserLevel(user_id,company);
			          ArrayList menulist = new ArrayList();
			          ArrayList menuListWithSequence = new ArrayList();
			          if(company.equalsIgnoreCase("track"))
			          {
			          	menulist = ub.getDropDownMenu(level_name);
			          	menuListWithSequence = ub.getDropDownMenuWithRowColSequence(level_name, company);
			          }
			          else
			          {
			             menulist = ub.getDropDownMenu(level_name,company);
			             menuListWithSequence = ub.getDropDownMenuWithRowColSequence(level_name, company);
			             if (menuListWithSequence.isEmpty()){
			            	 response.sendRedirect("login?warn=User Level Not Found (or) Not Authorised");
			             }
			          }
	
			          if(menulist.size()< 8)
			          {
			        	  response.sendRedirect("login?warn=User Level Not Found (or) Not Authorised");
			          }
						session.setAttribute("DROPDOWN_MENU",menulist);
						session.setAttribute("DROPDOWN_MENU_WITH_SEQUENCE", menuListWithSequence);
								
					
						response.sendRedirect("home");
				}
					
				if(logpage.equalsIgnoreCase("ACCOUNTING")){
					request.getSession().setAttribute("SYSTEMNOW", "ACCOUNTING");
					String level_name = ub.getUserLevelAcct(user_id,company);
			          ArrayList menulist = new ArrayList();
			          ArrayList menuListWithSequence = new ArrayList();
			          if(company.equalsIgnoreCase("track"))
			          {
			          	menulist = ub.getDropDownMenu(level_name);
			          	menuListWithSequence = ub.getDropDownMenuWithRowColSequence(level_name, company);
			          }
			          else
			          {
			             menulist = ub.getDropDownMenuAccounting(level_name,company);
			             menuListWithSequence = ub.getDropDownMenuWithRowColSequenceAccounting(level_name, company);
			             if (menuListWithSequence.isEmpty()){
			            	 response.sendRedirect("login?warn=User Level Not Found (or) Not Authorised");
			             }
			          }
	
			          if(menulist.size()< 8)
			          {
			        	  response.sendRedirect("login?warn=User Level Not Found (or) Not Authorised");
			          }
						session.setAttribute("DROPDOWN_MENU",menulist);
						session.setAttribute("DROPDOWN_MENU_WITH_SEQUENCE", menuListWithSequence);
								
					
						response.sendRedirect("home");
				}
					
				if(logpage.equalsIgnoreCase("PAYROLL")) {
					request.getSession().setAttribute("SYSTEMNOW", "PAYROLL");
					String level_name = ub.getUserLevelPay(user_id,company);
			          ArrayList menulist = new ArrayList();
			          ArrayList menuListWithSequence = new ArrayList();
			          if(company.equalsIgnoreCase("track"))
			          {
			          	menulist = ub.getDropDownMenu(level_name);
			          	menuListWithSequence = ub.getDropDownMenuWithRowColSequence(level_name, company);
			          }
			          else
			          {
			             menulist = ub.getDropDownMenuPayroll(level_name,company);
			             menuListWithSequence = ub.getDropDownMenuWithRowColSequencePayroll(level_name, company);
			             if (menuListWithSequence.isEmpty()){
			            	 response.sendRedirect("login?warn=User Level Not Found (or) Not Authorised");
			             }
			          }
	
			          if(menulist.size()< 8)
			          {
			        	  response.sendRedirect("login?warn=User Level Not Found (or) Not Authorised");
			          }
						session.setAttribute("DROPDOWN_MENU",menulist);
						session.setAttribute("DROPDOWN_MENU_WITH_SEQUENCE", menuListWithSequence);
								
					
					response.sendRedirect("home");
				}
			}catch (Exception e) {
				response.sendRedirect("selectapp");
			}
			
			
		}else if(action.equalsIgnoreCase("GET_INCOME_SUMMARY")) {
	        JSONArray jsonArrayErr = new JSONArray();
	        InvoiceUtil invoiceUtil = new InvoiceUtil();
	        PlantMstDAO plantMstDAO = new PlantMstDAO();
			try {
				String numberOfDecimal = plantMstDAO.getNumberOfDecimal(plant);
				String fromDate = StrUtils.fString(request.getParameter("FROM_DATE")).trim();
				String toDate = StrUtils.fString(request.getParameter("TO_DATE")).trim();
				String Account = StrUtils.fString(request.getParameter("ACCOUNTNAME")).trim();
				String Customer = StrUtils.fString(request.getParameter("CUSTOMER")).trim();
				String currency = StrUtils.fString((String) request.getSession().getAttribute("BASE_CURRENCY")).trim();
				
				ArrayList income = invoiceUtil.getTotalIncomeSummaryFromJournlForDashboard(plant, fromDate, toDate, Account, numberOfDecimal);
				double totalamt =0;
				ArrayList incomefilter = new ArrayList<>();
				if(Customer.equalsIgnoreCase("") || Customer == null) {
					for(int i=0 ; i<income.size();i++)
		      		 {
						Map m=(Map)income.get(i);
						totalamt = totalamt + Double.valueOf((String)m.get("TOTAL_AMOUNT"));
		       		 }
					incomefilter.addAll(income);
				}else {
					for(int i=0 ; i<income.size();i++)
		      		 {
						Map m=(Map)income.get(i);
						
						String Cust = (String)m.get("NAME");
						if(Customer.equalsIgnoreCase(Cust)) {
							incomefilter.add(m);
							totalamt = totalamt + Double.valueOf((String)m.get("TOTAL_AMOUNT"));
						}
						
		       		 }
				}
				
				ArrayList zerofilter = new ArrayList<>();
				
				for(int i=0 ; i<incomefilter.size();i++)
	      		 {
					Map m=(Map)incomefilter.get(i);
					
					double tamount = Double.valueOf((String)m.get("TOTAL_AMOUNT"));
					if(tamount != 0) {
						zerofilter.add(m);
					}
					
	       		 }
				
				String totalWithoutCurrency = StrUtils.addZeroes(totalamt, numberOfDecimal);
				String total = currency+totalWithoutCurrency;
				jsonObjectResult.put("INCOME", zerofilter);   
				jsonObjectResult.put("total", total);
				jsonObjectResult.put("currency", currency);
				jsonObjectResult.put("totalWithoutCurrency", totalWithoutCurrency);
				
			} catch (Exception e) {
				jsonObjectResult.put("SEARCH_DATA", "");
	            JSONObject resultJsonInt = new JSONObject();
	            resultJsonInt.put("ERROR_MESSAGE",  e.getMessage());
	            resultJsonInt.put("ERROR_CODE", "98");
	            jsonArrayErr.add(resultJsonInt);
	            jsonObjectResult.put("ERROR", jsonArrayErr);
			}
		}else if(action.equalsIgnoreCase("GET_EXPENSE_SUMMARY")) {
	        JSONArray jsonArrayErr = new JSONArray();
	        ExpensesUtil expensesUtil = new ExpensesUtil();
	        PlantMstDAO plantMstDAO = new PlantMstDAO();
			try {
				String numberOfDecimal = plantMstDAO.getNumberOfDecimal(plant);
				String fromDate = StrUtils.fString(request.getParameter("FROM_DATE")).trim();
				String toDate = StrUtils.fString(request.getParameter("TO_DATE")).trim();
				String Account = StrUtils.fString(request.getParameter("ACCOUNTNAME")).trim();
				String Supplier = StrUtils.fString(request.getParameter("SUPPLIER")).trim();
				String currency = StrUtils.fString((String) request.getSession().getAttribute("BASE_CURRENCY")).trim();
				
				ArrayList expense = expensesUtil.getTotalExpenseSummaryFromJournlForDashboard(plant, fromDate, toDate, Account, numberOfDecimal);
				double totalamt =0;
				
				ArrayList expensefilter = new ArrayList<>();
				if(Supplier.equalsIgnoreCase("") || Supplier == null) {
					for(int i=0 ; i<expense.size();i++)
		      		 {
						Map m=(Map)expense.get(i);
						totalamt = totalamt + Double.valueOf((String)m.get("TOTAL_AMOUNT"));
		       		 }
					expensefilter.addAll(expense);
				}else {
					for(int i=0 ; i<expense.size();i++)
		      		 {
						Map m=(Map)expense.get(i);
						String suplr = (String)m.get("NAME");
						if(Supplier.equalsIgnoreCase(suplr)) {
							expensefilter.add(m);
							totalamt = totalamt + Double.valueOf((String)m.get("TOTAL_AMOUNT"));
						}
		       		 }
				}
				
				ArrayList zerofilter = new ArrayList<>();
				
				for(int i=0 ; i<expensefilter.size();i++)
	      		 {
					Map m=(Map)expensefilter.get(i);
					
					double tamount = Double.valueOf((String)m.get("TOTAL_AMOUNT"));
					if(tamount != 0) {
						zerofilter.add(m);
					}
					
	       		 }
				String totalWithoutCurrency = StrUtils.addZeroes(totalamt, numberOfDecimal);
				String total = currency+totalWithoutCurrency;
				jsonObjectResult.put("EXPENSE", zerofilter);   
				jsonObjectResult.put("total", total);
				jsonObjectResult.put("currency", currency);
				jsonObjectResult.put("totalWithoutCurrency", totalWithoutCurrency);
				
			} catch (Exception e) {
				jsonObjectResult.put("SEARCH_DATA", "");
	            JSONObject resultJsonInt = new JSONObject();
	            resultJsonInt.put("ERROR_MESSAGE",  e.getMessage());
	            resultJsonInt.put("ERROR_CODE", "98");
	            jsonArrayErr.add(resultJsonInt);
	            jsonObjectResult.put("ERROR", jsonArrayErr);
			}
		}else if(action.equalsIgnoreCase("GET_PAYMENT_ISSUED_SUMMARY")) {
	        JSONArray jsonArrayErr = new JSONArray();
	        ExpensesUtil expensesUtil = new ExpensesUtil();
	        PlantMstDAO plantMstDAO = new PlantMstDAO();
			try {
				String numberOfDecimal = plantMstDAO.getNumberOfDecimal(plant);
				String fromDate = StrUtils.fString(request.getParameter("FROM_DATE")).trim();
				String toDate = StrUtils.fString(request.getParameter("TO_DATE")).trim();
				String Account = StrUtils.fString(request.getParameter("ACCOUNTNAME")).trim();
				String Supplier = StrUtils.fString(request.getParameter("SUPPLIER")).trim();
				String currency = StrUtils.fString((String) request.getSession().getAttribute("BASE_CURRENCY")).trim();
				
				ArrayList payIssue = expensesUtil.getPaymentIssuedSummaryFromJournlForDashboard(plant, fromDate, toDate, Account, numberOfDecimal);
				double totalamt =0;
				
				ArrayList payIssuefilter = new ArrayList<>();
				if(Supplier.equalsIgnoreCase("") || Supplier == null) {
					for(int i=0 ; i<payIssue.size();i++)
		      		 {
						Map m=(Map)payIssue.get(i);
						totalamt = totalamt + Double.valueOf((String)m.get("TOTAL_AMOUNT"));
		       		 }
					payIssuefilter.addAll(payIssue);
				}else {
					for(int i=0 ; i<payIssue.size();i++)
		      		 {
						Map m=(Map)payIssue.get(i);
						String suplr = (String)m.get("NAME");
						if(Supplier.equalsIgnoreCase(suplr)) {
							payIssuefilter.add(m);
							totalamt = totalamt + Double.valueOf((String)m.get("TOTAL_AMOUNT"));
						}
		       		 }
				}
				
				ArrayList zerofilter = new ArrayList<>();
				
				for(int i=0 ; i<payIssuefilter.size();i++)
	      		 {
					Map m=(Map)payIssuefilter.get(i);
					
					double tamount = Double.valueOf((String)m.get("TOTAL_AMOUNT"));
					if(tamount != 0) {
						zerofilter.add(m);
					}
					
	       		 }
				String totalWithoutCurrency = StrUtils.addZeroes(totalamt, numberOfDecimal);
				String total = currency+totalWithoutCurrency;
				jsonObjectResult.put("PAYMENT", zerofilter);   
				jsonObjectResult.put("total", total);
				jsonObjectResult.put("currency", currency);
				jsonObjectResult.put("totalWithoutCurrency", totalWithoutCurrency);
				
			} catch (Exception e) {
				jsonObjectResult.put("SEARCH_DATA", "");
	            JSONObject resultJsonInt = new JSONObject();
	            resultJsonInt.put("ERROR_MESSAGE",  e.getMessage());
	            resultJsonInt.put("ERROR_CODE", "98");
	            jsonArrayErr.add(resultJsonInt);
	            jsonObjectResult.put("ERROR", jsonArrayErr);
			}
		}else if(action.equalsIgnoreCase("GET_PAYMENT_RECEIPT_SUMMARY")) {
	        JSONArray jsonArrayErr = new JSONArray();
	        ExpensesUtil expensesUtil = new ExpensesUtil();
	        PlantMstDAO plantMstDAO = new PlantMstDAO();
			try {
				String numberOfDecimal = plantMstDAO.getNumberOfDecimal(plant);
				String fromDate = StrUtils.fString(request.getParameter("FROM_DATE")).trim();
				String toDate = StrUtils.fString(request.getParameter("TO_DATE")).trim();
				String Account = StrUtils.fString(request.getParameter("ACCOUNTNAME")).trim();
				String Customer = StrUtils.fString(request.getParameter("CUSTOMER")).trim();
				String currency = StrUtils.fString((String) request.getSession().getAttribute("BASE_CURRENCY")).trim();
				
				ArrayList PayReceipt = expensesUtil.getPaymentReceiptSummaryFromJournlForDashboard(plant, fromDate, toDate, Account, numberOfDecimal);
				double totalamt =0;
				
				ArrayList PayReceiptfilter = new ArrayList<>();
				if(Customer.equalsIgnoreCase("") || Customer == null) {
					for(int i=0 ; i<PayReceipt.size();i++)
		      		 {
						Map m=(Map)PayReceipt.get(i);
						totalamt = totalamt + Double.valueOf((String)m.get("TOTAL_AMOUNT"));
		       		 }
					PayReceiptfilter.addAll(PayReceipt);
				}else {
					for(int i=0 ; i<PayReceipt.size();i++)
		      		 {
						Map m=(Map)PayReceipt.get(i);
						String CUST = (String)m.get("NAME");
						if(Customer.equalsIgnoreCase(CUST)) {
							PayReceiptfilter.add(m);
							totalamt = totalamt + Double.valueOf((String)m.get("TOTAL_AMOUNT"));
						}
		       		 }
				}
				
				ArrayList zerofilter = new ArrayList<>();
				
				for(int i=0 ; i<PayReceiptfilter.size();i++)
	      		 {
					Map m=(Map)PayReceiptfilter.get(i);
					
					double tamount = Double.valueOf((String)m.get("TOTAL_AMOUNT"));
					if(tamount != 0) {
						zerofilter.add(m);
					}
					
	       		 }
				String totalWithoutCurrency = StrUtils.addZeroes(totalamt, numberOfDecimal);
				String total = currency+totalWithoutCurrency;
				jsonObjectResult.put("PAYMENT", zerofilter);   
				jsonObjectResult.put("total", total);
				jsonObjectResult.put("currency", currency);
				jsonObjectResult.put("totalWithoutCurrency", totalWithoutCurrency);
				
			} catch (Exception e) {
				jsonObjectResult.put("SEARCH_DATA", "");
	            JSONObject resultJsonInt = new JSONObject();
	            resultJsonInt.put("ERROR_MESSAGE",  e.getMessage());
	            resultJsonInt.put("ERROR_CODE", "98");
	            jsonArrayErr.add(resultJsonInt);
	            jsonObjectResult.put("ERROR", jsonArrayErr);
			}
		} else if (action.equalsIgnoreCase("GET_CUSTOMER_AGEING_SUMMARY")) {			
			jsonObjectResult = this.getCustomerAgeingSummary(request);			
		} else if (action.equalsIgnoreCase("GET_SUPPLIER_AGEING_SUMMARY")) {			
			jsonObjectResult = this.getSupplierAgeingSummary(request);			
		} else if (action.equalsIgnoreCase("GET_ACCOUNT_PAYABLE_TO_SUPPLIER")) {			
			jsonObjectResult = this.getAccountPayable(request);			
		} else if (action.equalsIgnoreCase("GET_ACCOUNT_RECEIVABLE_FROM_CUSTOMER")) {			
			jsonObjectResult = this.getAccountReceivable(request);			
		}
		
		else if(action.equalsIgnoreCase("GET_POS_SALES_DASHBOARD")) {
	        JSONArray jsonArrayErr = new JSONArray();
	        ExpensesUtil expensesUtil = new ExpensesUtil();
	        PlantMstDAO plantMstDAO = new PlantMstDAO();
	        OutletBeanDAO outletBeanDAO = new OutletBeanDAO();
	        DoHdrDAO doHdrDAO = new DoHdrDAO();
			try {
				String numberOfDecimal = plantMstDAO.getNumberOfDecimal(plant);
				String toDate = new DateUtils().getDate();
				//String toDate = "13/05/2023";
				//ArrayList outputlist = new ArrayList();
				String plantname = "";
				ArrayList resultlist = new ArrayList();
				String PARENT_PLANT = new ParentChildCmpDetDAO().getPARENT_CHILD(plant);//Check Parent Plant or child plant
				Hashtable htData = new Hashtable();	
				 if(PARENT_PLANT != null){
					 
					 
					 
					 
					 ArrayList ouletparent =  outletBeanDAO.getalloutlet(plant);
					 double totalsalesprice = 0.0;
       			  	 double totalsalescost = 0.0;
       			  	 plantname = plantMstDAO.getcmpyname(plant);
       			     ArrayList outputlist = new ArrayList();
					 for (int j=0; j < ouletparent.size(); j++ ) {
	        			  Map po = (Map) ouletparent.get(j);
	        			  String poutname = (String) po.get("OUTLET");
	        			  ArrayList terminalparent = outletBeanDAO.getterminalbyoutlet(plant, poutname);
	        			  
	        			  
	        			  for (int k=0; k < terminalparent.size(); k++ ) {
		        			  Map pt = (Map) terminalparent.get(k);
		        			  String ptname = (String) pt.get("TERMINAL");
		        			  String tinitime = (String) pt.get("TERMINAL_STARTTIME");
		        			  String touttime = (String) pt.get("TERMINAL_ENDTIME");
		        			  ArrayList psaledata = doHdrDAO.selectPosSales(plant, toDate, poutname, ptname);
		        			  String intime = doHdrDAO.getstarttime(plant, toDate, poutname, ptname);
 		        			  String outtime = doHdrDAO.getclosingtime(plant, toDate, poutname, ptname);
 		        			  if(psaledata.size() > 0) {
 		        			 for (int t=0; t < psaledata.size(); t++ ) {
 		        				
	 		        			  Map map = (Map) psaledata.get(t);
	 		        			 totalsalesprice = totalsalesprice + Double.valueOf((String) map.get("TOTALPRICE"));
	 		        			totalsalescost = totalsalescost + Double.valueOf((String) map.get("TOTALCOST"));
	 		        			  map.put("intime", intime);
	 		        			  map.put("outtime", outtime);
	 		        			  
	 		        			  if(!intime.equalsIgnoreCase("00:00")) {
	 		        			  long starttime = findDiff(tinitime, intime);
	 		        			  String infiff = splitToComponentTimes(starttime);
	 		        			  if(starttime < 0) {
	 		        				 map.put("intimestatus", "red");
	 		        				 map.put("intimediff", infiff);
	 		        			  }else {
	 		        				 map.put("intimestatus", "green");
	 		        				 map.put("intimediff", infiff);
	 		        			  }
	 		        			  }else {
	 		        				 map.put("intimestatus", "-"); 
	 		        				 map.put("intimediff", "-");
	 		        			  }
	 		        			  
	 		        			 if(!outtime.equalsIgnoreCase("00:00")) {
	 		        			 long closetime = findDiff(outtime, touttime);
	 		        			 String outdiff = splitToComponentTimes(closetime);
	 		        			 if(closetime < 0) {
	 		        				 map.put("outtimestatus", "red");
	 		        				 map.put("outtimediff", outdiff);
	 		        			  }else {
		 		        		     map.put("outtimestatus", "green");
		 		        		     map.put("outtimediff", outdiff);
		 		        		  }
	 		        			 }else {
	 		        				map.put("outtimestatus", "-");
	 		        				map.put("outtimediff", "-");
	 		        			 }
	 		        			 
	 		        			map.put("ISOUTLET", "0");
	 		        			 
	 		        		  }
 		        			  }else {
 		        				 Map termap = new HashMap<>();
  		        				termap.put("TERMINAL", (String) pt.get("TERMINAL"));
  		        				termap.put("TERMINALNAME", (String) pt.get("TERMINAL_NAME"));
  		        				termap.put("intime",intime);
  		        				termap.put("TOTALCOST",0.0);
  		        				termap.put("TOTALPRICE",0.0);
  		        				termap.put("outtime",outtime);
  		        				termap.put("OUTLET",poutname);
  		        				termap.put("ORDDATE","");
  		        				termap.put("OUTLETNAME",(String) po.get("OUTLET_NAME"));
  		        				termap.put("ISOUTLET", "0");
 								 
 								 if(!intime.equalsIgnoreCase("00:00")) {
 	 		        			  long starttime = findDiff(tinitime, intime);
 	 		        			  String infiff = splitToComponentTimes(starttime);
 	 		        			  if(starttime < 0) {
 	 		        				 termap.put("intimestatus", "red");
 	 		        				 termap.put("intimediff", infiff);
 	 		        			  }else {
 	 		        				 termap.put("intimestatus", "green");
 	 		        				 termap.put("intimediff", infiff);
 	 		        			  }
 	 		        			  }else {
 	 		        				 termap.put("intimestatus", "-"); 
 	 		        				 termap.put("intimediff", "-");
 	 		        			  }
 	 		        			  
 	 		        			 if(!outtime.equalsIgnoreCase("00:00")) {
 	 		        			 long closetime = findDiff(outtime, touttime);
 	 		        			 String outdiff = splitToComponentTimes(closetime);
 	 		        			 if(closetime < 0) {
 	 		        				 termap.put("outtimestatus", "red");
 	 		        				 termap.put("outtimediff", outdiff);
 	 		        			  }else {
 		 		        		     termap.put("outtimestatus", "green");
 		 		        		     termap.put("outtimediff", outdiff);
 		 		        		  }
 	 		        			 }else {
 	 		        				termap.put("outtimestatus", "-");
 	 		        				termap.put("outtimediff", "-");
 	 		        			 }
 	 		        			if(!intime.equalsIgnoreCase("00:00"))
 	 		        				psaledata.add(termap);
 		        			  }
 		        			 
 		        			  
		        			  outputlist.addAll(psaledata);
	        			  }
	        			  
	        			  	
					 }
					 
					Map outmap1 = new HashMap<>();
     			  	outmap1.put("TERMINAL", plantname);
     			  	outmap1.put("TERMINALNAME", "");
     			  	outmap1.put("intime","");
     			  	outmap1.put("TOTALCOST",totalsalescost);
     			  	outmap1.put("TOTALPRICE",totalsalesprice);
     			  	outmap1.put("outtime","");
     			  	outmap1.put("intimediff","");
     			  	outmap1.put("outtimediff", "");
     			  	outmap1.put("OUTLET","");
     			  	outmap1.put("outtimestatus","");
     			  	outmap1.put("ORDDATE","");
     			  	outmap1.put("OUTLETNAME","");
     			  	outmap1.put("intimestatus","");
     			  	outmap1.put("ISOUTLET", "1");
     			  	if(totalsalesprice > 0 || outputlist.size() > 0)
     				resultlist.add(outmap1);
     				resultlist.addAll(outputlist);
					 
					 
					 
					 String query="SELECT CHILD_PLANT FROM PARENT_CHILD_COMPANY_DET WHERE PARENT_PLANT='"+plant+"'";
		        	  ArrayList arrList = new ItemMstUtil().selectForReport(query,htData,"");
		        	  if (arrList.size() > 0) {
		        		  for (int i=0; i < arrList.size(); i++ ) {
		        			  Map m = (Map) arrList.get(i);
		        			  String childplant = (String) m.get("CHILD_PLANT");
		        			  
		        			  
		        			  ArrayList ouletchild =  outletBeanDAO.getalloutlet(childplant);
		        			  	 double totalsalesprice2 = 0.0;
		        			  	 double totalsalescost2 = 0.0;
		        			  	 plantname = plantMstDAO.getcmpyname(childplant);
		        			     ArrayList outputlist2 = new ArrayList();
		 					 for (int j=0; j < ouletchild.size(); j++ ) {
		 	        			  Map po = (Map) ouletchild.get(j);
		 	        			  String poutname = (String) po.get("OUTLET");
		 	        			  ArrayList terminalparent = outletBeanDAO.getterminalbyoutlet(childplant, poutname);
		 	        			
		 	        			  for (int k=0; k < terminalparent.size(); k++ ) {
		 		        			  Map pt = (Map) terminalparent.get(k);
		 		        			  String ptname = (String) pt.get("TERMINAL");
		 		        			 String tinitime = (String) pt.get("TERMINAL_STARTTIME");
				        			  String touttime = (String) pt.get("TERMINAL_ENDTIME");
		 		        			  ArrayList psaledata = doHdrDAO.selectPosSales(childplant, toDate, poutname, ptname);
		 		        			  String intime = doHdrDAO.getstarttime(childplant, toDate, poutname, ptname);
		 		        			  String outtime = doHdrDAO.getclosingtime(childplant, toDate, poutname, ptname);
		 		        			 if(psaledata.size() > 0) {
		 		        			 for (int t=0; t < psaledata.size(); t++ ) {
			 		        			  Map map = (Map) psaledata.get(t);
			 		        			 totalsalesprice2 = totalsalesprice2 + Double.valueOf((String) map.get("TOTALPRICE"));
				 		        			totalsalescost2 = totalsalescost2 + Double.valueOf((String) map.get("TOTALCOST"));
			 		        			  map.put("intime", intime);
			 		        			  map.put("outtime", outtime);
			 		        			  
			 		        			  if(!intime.equalsIgnoreCase("00:00")) {
			 		        			  long starttime = findDiff(tinitime, intime);
			 		        			  String infiff = splitToComponentTimes(starttime);
			 		        			  if(starttime < 0) {
			 		        				 map.put("intimestatus", "red");
			 		        				 map.put("intimediff", infiff);
			 		        			  }else {
			 		        				 map.put("intimestatus", "green");
			 		        				 map.put("intimediff", infiff);
			 		        			  }
			 		        			  }else {
			 		        				 map.put("intimestatus", "-"); 
			 		        				 map.put("intimediff", "-");
			 		        			  }
			 		        			  
			 		        			 if(!outtime.equalsIgnoreCase("00:00")) {
			 		        			 long closetime = findDiff(outtime, touttime);
			 		        			 String outdiff = splitToComponentTimes(closetime);
			 		        			 if(closetime < 0) {
			 		        				 map.put("outtimestatus", "red");
			 		        				 map.put("outtimediff", outdiff);
			 		        			  }else {
				 		        		     map.put("outtimestatus", "green");
				 		        		     map.put("outtimediff", outdiff);
				 		        		  }
			 		        			 }else {
			 		        				map.put("outtimestatus", "-");
			 		        				map.put("outtimediff", "-");
			 		        			 }
			 		        			map.put("ISOUTLET", "0");
			 		        		  } 
		 		        			 }else {
		 		        				Map termap = new HashMap<>();
		 		        				termap.put("TERMINAL", (String) pt.get("TERMINAL"));
		 		        				termap.put("TERMINALNAME", (String) pt.get("TERMINAL_NAME"));
		 		        				termap.put("intime",intime);
		 		        				termap.put("TOTALCOST",0.0);
		 		        				termap.put("TOTALPRICE",0.0);
		 		        				termap.put("outtime",outtime);
		 		        				termap.put("OUTLET",poutname);
		 		        				termap.put("ORDDATE","");
		 		        				termap.put("OUTLETNAME",(String) po.get("OUTLET_NAME"));
		 		        				termap.put("ISOUTLET", "0");
										 
										 if(!intime.equalsIgnoreCase("00:00")) {
			 		        			  long starttime = findDiff(tinitime, intime);
			 		        			  String infiff = splitToComponentTimes(starttime);
			 		        			  if(starttime < 0) {
			 		        				 termap.put("intimestatus", "red");
			 		        				 termap.put("intimediff", infiff);
			 		        			  }else {
			 		        				 termap.put("intimestatus", "green");
			 		        				 termap.put("intimediff", infiff);
			 		        			  }
			 		        			  }else {
			 		        				 termap.put("intimestatus", "-"); 
			 		        				 termap.put("intimediff", "-");
			 		        			  }
			 		        			  
			 		        			 if(!outtime.equalsIgnoreCase("00:00")) {
			 		        			 long closetime = findDiff(outtime, touttime);
			 		        			 String outdiff = splitToComponentTimes(closetime);
			 		        			 if(closetime < 0) {
			 		        				 termap.put("outtimestatus", "red");
			 		        				 termap.put("outtimediff", outdiff);
			 		        			  }else {
				 		        		     termap.put("outtimestatus", "green");
				 		        		     termap.put("outtimediff", outdiff);
				 		        		  }
			 		        			 }else {
			 		        				termap.put("outtimestatus", "-");
			 		        				termap.put("outtimediff", "-");
			 		        			 }
			 		        			if(!intime.equalsIgnoreCase("00:00"))
										 psaledata.add(termap);
			  		        			  }
		 		        			  
		 		        			  outputlist2.addAll(psaledata);
		 	        			  }
		 	        			  
		 	        				
		 					 }
		        			  
		 					Map outmap2 = new HashMap<>();
		 					outmap2.put("TERMINAL", plantname);
		 					outmap2.put("TERMINALNAME", "");
		 					outmap2.put("intime","");
		 					outmap2.put("TOTALCOST",totalsalescost2);
	        				outmap2.put("TOTALPRICE",totalsalesprice2);
	        				outmap2.put("outtime","");
	        				outmap2.put("intimediff","");
	        				outmap2.put("outtimediff", "");
	        				outmap2.put("OUTLET","");
	        				outmap2.put("outtimestatus","");
	        				outmap2.put("ORDDATE","");
	        				outmap2.put("OUTLETNAME","");
	        				outmap2.put("intimestatus","");
	        				outmap2.put("ISOUTLET", "1");
	        				if(totalsalesprice2 > 0 || outputlist2.size() > 0)
	        				resultlist.add(outmap2);
	        				resultlist.addAll(outputlist2);
		        		  }
		        	  }
		        	  
		        	  
				 }else {
		        	  String queryparent="SELECT PARENT_PLANT FROM PARENT_CHILD_COMPANY_DET WHERE CHILD_PLANT='"+plant+"'";
		        	  ArrayList arrLi = new ItemMstUtil().selectForReport(queryparent,htData,"");
		        	  if (arrLi.size() > 0) {
		        	  Map mst = (Map) arrLi.get(0);
		        	  String parent = (String) mst.get("PARENT_PLANT");
		        	  
		        	  
		        	  
		        	  ArrayList ouletparent =  outletBeanDAO.getalloutlet(parent);
		        	  ArrayList outputlist3 = new ArrayList();
        			  double totalsalesprice3 = 0.0;
        			  double totalsalescost3 = 0.0;
        			  plantname = plantMstDAO.getcmpyname(parent);
						 for (int j=0; j < ouletparent.size(); j++ ) {
		        			  Map po = (Map) ouletparent.get(j);
		        			  String poutname = (String) po.get("OUTLET");
		        			  ArrayList terminalparent = outletBeanDAO.getterminalbyoutlet(parent, poutname);
		        			  
		        			  for (int k=0; k < terminalparent.size(); k++ ) {
			        			  Map pt = (Map) terminalparent.get(k);
			        			  String ptname = (String) pt.get("TERMINAL");
			        			  String tinitime = (String) pt.get("TERMINAL_STARTTIME");
			        			  String touttime = (String) pt.get("TERMINAL_ENDTIME");
			        			  ArrayList psaledata = doHdrDAO.selectPosSales(parent, toDate, poutname, ptname);
			        			  String intime = doHdrDAO.getstarttime(parent, toDate, poutname, ptname);
	 		        			  String outtime = doHdrDAO.getclosingtime(parent, toDate, poutname, ptname);
	 		        			 if(psaledata.size() > 0) {
	 		        			 for (int t=0; t < psaledata.size(); t++ ) {
		 		        			  Map map = (Map) psaledata.get(t);
		 		        			    totalsalesprice3 = totalsalesprice3 + Double.valueOf((String) map.get("TOTALPRICE"));
			 		        			totalsalescost3 = totalsalescost3 + Double.valueOf((String) map.get("TOTALCOST"));
		 		        			  map.put("intime", intime);
		 		        			  map.put("outtime", outtime);
		 		        			  
		 		        			  if(!intime.equalsIgnoreCase("00:00")) {
		 		        			  long starttime = findDiff(tinitime, intime);
		 		        			  String infiff = splitToComponentTimes(starttime);
		 		        			  if(starttime < 0) {
		 		        				 map.put("intimestatus", "red");
		 		        				 map.put("intimediff", infiff);
		 		        			  }else {
		 		        				 map.put("intimestatus", "green");
		 		        				 map.put("intimediff", infiff);
		 		        			  }
		 		        			  }else {
		 		        				 map.put("intimestatus", "-"); 
		 		        				 map.put("intimediff", "-");
		 		        			  }
		 		        			  
		 		        			 if(!outtime.equalsIgnoreCase("00:00")) {
		 		        			 long closetime = findDiff(outtime, touttime);
		 		        			 String outdiff = splitToComponentTimes(closetime);
		 		        			 if(closetime < 0) {
		 		        				 map.put("outtimestatus", "red");
		 		        				 map.put("outtimediff", outdiff);
		 		        			  }else {
			 		        		     map.put("outtimestatus", "green");
			 		        		     map.put("outtimediff", outdiff);
			 		        		  }
		 		        			 }else {
		 		        				map.put("outtimestatus", "-");
		 		        				map.put("outtimediff", "-");
		 		        			 }
		 		        			map.put("ISOUTLET", "0");
		 		        		  }
	 		        			 }else {
	 		        				Map termap = new HashMap<>();
	 		        				termap.put("TERMINAL", (String) pt.get("TERMINAL"));
	 		        				termap.put("TERMINALNAME", (String) pt.get("TERMINAL_NAME"));
	 		        				termap.put("intime",intime);
	 		        				termap.put("TOTALCOST",0.0);
	 		        				termap.put("TOTALPRICE",0.0);
	 		        				termap.put("outtime",outtime);
	 		        				termap.put("OUTLET",poutname);
	 		        				termap.put("ORDDATE","");
	 		        				termap.put("OUTLETNAME",(String) po.get("OUTLET_NAME"));
	 		        				termap.put("ISOUTLET", "0");
									 
									 if(!intime.equalsIgnoreCase("00:00")) {
		 		        			  long starttime = findDiff(tinitime, intime);
		 		        			  String infiff = splitToComponentTimes(starttime);
		 		        			  if(starttime < 0) {
		 		        				 termap.put("intimestatus", "red");
		 		        				 termap.put("intimediff", infiff);
		 		        			  }else {
		 		        				 termap.put("intimestatus", "green");
		 		        				 termap.put("intimediff", infiff);
		 		        			  }
		 		        			  }else {
		 		        				 termap.put("intimestatus", "-"); 
		 		        				 termap.put("intimediff", "-");
		 		        			  }
		 		        			  
		 		        			 if(!outtime.equalsIgnoreCase("00:00")) {
		 		        			 long closetime = findDiff(outtime, touttime);
		 		        			 String outdiff = splitToComponentTimes(closetime);
		 		        			 if(closetime < 0) {
		 		        				 termap.put("outtimestatus", "red");
		 		        				 termap.put("outtimediff", outdiff);
		 		        			  }else {
			 		        		     termap.put("outtimestatus", "green");
			 		        		     termap.put("outtimediff", outdiff);
			 		        		  }
		 		        			 }else {
		 		        				termap.put("outtimestatus", "-");
		 		        				termap.put("outtimediff", "-");
		 		        			 }
		 		        			if(!intime.equalsIgnoreCase("00:00"))
									 psaledata.add(termap);
	 		        			  }
			        			  outputlist3.addAll(psaledata);
		        			  }
		        			  
		        				
						 }
						 
						 	Map outmap3 = new HashMap<>();
	        			  	outmap3.put("TERMINAL", plantname);
	        				outmap3.put("TERMINALNAME", "");
	        				outmap3.put("intime","");
	        				outmap3.put("TOTALCOST",totalsalescost3);
	        				outmap3.put("TOTALPRICE",totalsalesprice3);
	        				outmap3.put("outtime","");
	        				outmap3.put("intimediff","");
	        				outmap3.put("outtimediff", "");
	        				outmap3.put("OUTLET","");
	        				outmap3.put("outtimestatus","");
	        				outmap3.put("ORDDATE","");
	        				outmap3.put("OUTLETNAME","");
	        				outmap3.put("intimestatus","");
	        				outmap3.put("ISOUTLET", "1");
	        				if(totalsalesprice3 > 0 || outputlist3.size() > 0)
	        				resultlist.add(outmap3);
	        				resultlist.addAll(outputlist3);
						 
						 
						 ArrayList ouletchild =  outletBeanDAO.getalloutlet(plant);
						 ArrayList outputlist4 = new ArrayList();
	        			  double totalsalesprice4 = 0.0;
	        			  double totalsalescost4 = 0.0;
	        			  plantname = plantMstDAO.getcmpyname(plant);
						 for (int j=0; j < ouletchild.size(); j++ ) {
		        			  Map po = (Map) ouletchild.get(j);
		        			  String poutname = (String) po.get("OUTLET");
		        			  ArrayList terminalparent = outletBeanDAO.getterminalbyoutlet(plant, poutname);
		        			  
		        			  for (int k=0; k < terminalparent.size(); k++ ) {
			        			  Map pt = (Map) terminalparent.get(k);
			        			  String ptname = (String) pt.get("TERMINAL");
			        			  String tinitime = (String) pt.get("TERMINAL_STARTTIME");
			        			  String touttime = (String) pt.get("TERMINAL_ENDTIME");
			        			  ArrayList psaledata = doHdrDAO.selectPosSales(plant, toDate, poutname, ptname);
			        			  String intime = doHdrDAO.getstarttime(plant, toDate, poutname, ptname);
	 		        			  String outtime = doHdrDAO.getclosingtime(plant, toDate, poutname, ptname);
	 		        			 if(psaledata.size() > 0) {
	 		        			 for (int t=0; t < psaledata.size(); t++ ) {
		 		        			  Map map = (Map) psaledata.get(t);
		 		        			 totalsalesprice4 = totalsalesprice4 + Double.valueOf((String) map.get("TOTALPRICE"));
			 		        			totalsalescost4 = totalsalescost4 + Double.valueOf((String) map.get("TOTALCOST"));
		 		        			  map.put("intime", intime);
		 		        			  map.put("outtime", outtime);
		 		        			  
		 		        			  if(!intime.equalsIgnoreCase("00:00")) {
		 		        			  long starttime = findDiff(tinitime, intime);
		 		        			  String infiff = splitToComponentTimes(starttime);
		 		        			  if(starttime < 0) {
		 		        				 map.put("intimestatus", "red");
		 		        				 map.put("intimediff", infiff);
		 		        			  }else {
		 		        				 map.put("intimestatus", "green");
		 		        				 map.put("intimediff", infiff);
		 		        			  }
		 		        			  }else {
		 		        				 map.put("intimestatus", "-"); 
		 		        				 map.put("intimediff", "-");
		 		        			  }
		 		        			  
		 		        			 if(!outtime.equalsIgnoreCase("00:00")) {
		 		        			 long closetime = findDiff(outtime, touttime);
		 		        			 String outdiff = splitToComponentTimes(closetime);
		 		        			 if(closetime < 0) {
		 		        				 map.put("outtimestatus", "red");
		 		        				 map.put("outtimediff", outdiff);
		 		        			  }else {
			 		        		     map.put("outtimestatus", "green");
			 		        		     map.put("outtimediff", outdiff);
			 		        		  }
		 		        			 }else {
		 		        				map.put("outtimestatus", "-");
		 		        				map.put("outtimediff", "-");
		 		        			 }
		 		        			map.put("ISOUTLET", "0");
		 		        		  }
	 		        			 
	 		        			 }else {
	 		        				Map termap = new HashMap<>();
	 		        				termap.put("TERMINAL", (String) pt.get("TERMINAL"));
	 		        				termap.put("TERMINALNAME", (String) pt.get("TERMINAL_NAME"));
	 		        				termap.put("intime",intime);
	 		        				termap.put("TOTALCOST",0.0);
	 		        				termap.put("TOTALPRICE",0.0);
	 		        				termap.put("outtime",outtime);
	 		        				termap.put("OUTLET",poutname);
	 		        				termap.put("ORDDATE","");
	 		        				termap.put("OUTLETNAME",(String) po.get("OUTLET_NAME"));
	 		        				termap.put("ISOUTLET", "0");
									 
									 if(!intime.equalsIgnoreCase("00:00")) {
		 		        			  long starttime = findDiff(tinitime, intime);
		 		        			  String infiff = splitToComponentTimes(starttime);
		 		        			  if(starttime < 0) {
		 		        				 termap.put("intimestatus", "red");
		 		        				 termap.put("intimediff", infiff);
		 		        			  }else {
		 		        				 termap.put("intimestatus", "green");
		 		        				 termap.put("intimediff", infiff);
		 		        			  }
		 		        			  }else {
		 		        				 termap.put("intimestatus", "-"); 
		 		        				 termap.put("intimediff", "-");
		 		        			  }
		 		        			  
		 		        			 if(!outtime.equalsIgnoreCase("00:00")) {
		 		        			 long closetime = findDiff(outtime, touttime);
		 		        			 String outdiff = splitToComponentTimes(closetime);
		 		        			 if(closetime < 0) {
		 		        				 termap.put("outtimestatus", "red");
		 		        				 termap.put("outtimediff", outdiff);
		 		        			  }else {
			 		        		     termap.put("outtimestatus", "green");
			 		        		     termap.put("outtimediff", outdiff);
			 		        		  }
		 		        			 }else {
		 		        				termap.put("outtimestatus", "-");
		 		        				termap.put("outtimediff", "-");
		 		        			 }
		 		        			if(!intime.equalsIgnoreCase("00:00"))
									 psaledata.add(termap);
	 		        			  }
	 		        			  
			        			  outputlist4.addAll(psaledata);
		        			  }
		        			  
		        				
						 }
						 Map outmap4 = new HashMap<>();
	        			  	outmap4.put("TERMINAL", plantname);
	        				outmap4.put("TERMINALNAME","");
	        				outmap4.put("intime","");
	        				outmap4.put("TOTALCOST",totalsalescost4);
	        				outmap4.put("TOTALPRICE",totalsalesprice4);
	        				outmap4.put("outtime","");
	        				outmap4.put("intimediff","");
	        				outmap4.put("outtimediff", "");
	        				outmap4.put("OUTLET","");
	        				outmap4.put("outtimestatus","");
	        				outmap4.put("ORDDATE","");
	        				outmap4.put("OUTLETNAME","");
	        				outmap4.put("intimestatus","");
	        				outmap4.put("ISOUTLET", "1");
	        				if(totalsalesprice4 > 0 || outputlist4.size() > 0)
	        				resultlist.add(outmap4);
	        				resultlist.addAll(outputlist4);
		        	  
		        	  
		        	  String query="SELECT CHILD_PLANT FROM PARENT_CHILD_COMPANY_DET WHERE PARENT_PLANT='"+parent+"' AND CHILD_PLANT !='"+plant+"' ";
		        	  ArrayList arrList = new ItemMstUtil().selectForReport(query,htData,"");
		        	  Map m = new HashMap();
		        	  if (arrList.size() > 0) {
		        		  for (int i=0; i < arrList.size(); i++ ) {
		        			  m = (Map) arrList.get(i);
		        			  String childplant = (String) m.get("CHILD_PLANT");
		        			  
		        			  
		        			  
		        			  ArrayList ouletc=  outletBeanDAO.getalloutlet(childplant);
		        			  ArrayList outputlist5 = new ArrayList();
		 	        			double totalsalesprice5 = 0.0;
			        			  double totalsalescost5 = 0.0;
			        			  plantname = plantMstDAO.getcmpyname(childplant);
			 					 for (int j=0; j < ouletc.size(); j++ ) {
			 	        			  Map po = (Map) ouletc.get(j);
			 	        			  String poutname = (String) po.get("OUTLET");
			 	        			  ArrayList terminalparent = outletBeanDAO.getterminalbyoutlet(childplant, poutname);
			 	        			 
			 	        			  for (int k=0; k < terminalparent.size(); k++ ) {
			 		        			  Map pt = (Map) terminalparent.get(k);
			 		        			  String ptname = (String) pt.get("TERMINAL");
			 		        			  String tinitime = (String) pt.get("TERMINAL_STARTTIME");
					        			  String touttime = (String) pt.get("TERMINAL_ENDTIME");
			 		        			  ArrayList psaledata = doHdrDAO.selectPosSales(childplant, toDate, poutname, ptname);
			 		        			  String intime = doHdrDAO.getstarttime(childplant, toDate, poutname, ptname);
			 		        			  String outtime = doHdrDAO.getclosingtime(childplant, toDate, poutname, ptname);
			 		        			 if(psaledata.size() > 0) {
			 		        			 for (int t=0; t < psaledata.size(); t++ ) {
				 		        			  Map map = (Map) psaledata.get(t);
				 		        			 totalsalesprice5 = totalsalesprice5 + Double.valueOf((String) map.get("TOTALPRICE"));
					 		        			totalsalescost5 = totalsalescost5 + Double.valueOf((String) map.get("TOTALCOST"));
				 		        			  map.put("intime", intime);
				 		        			  map.put("outtime", outtime);
				 		        			  
				 		        			  if(!intime.equalsIgnoreCase("00:00")) {
				 		        			  long starttime = findDiff(tinitime, intime);
				 		        			  String infiff = splitToComponentTimes(starttime);
				 		        			  if(starttime < 0) {
				 		        				 map.put("intimestatus", "red");
				 		        				 map.put("intimediff", infiff);
				 		        			  }else {
				 		        				 map.put("intimestatus", "green");
				 		        				 map.put("intimediff", infiff);
				 		        			  }
				 		        			  }else {
				 		        				 map.put("intimestatus", "-"); 
				 		        				 map.put("intimediff", "-");
				 		        			  }
				 		        			  
				 		        			 if(!outtime.equalsIgnoreCase("00:00")) {
				 		        			 long closetime = findDiff(outtime, touttime);
				 		        			 String outdiff = splitToComponentTimes(closetime);
				 		        			 if(closetime < 0) {
				 		        				 map.put("outtimestatus", "red");
				 		        				 map.put("outtimediff", outdiff);
				 		        			  }else {
					 		        		     map.put("outtimestatus", "green");
					 		        		     map.put("outtimediff", outdiff);
					 		        		  }
				 		        			 }else {
				 		        				map.put("outtimestatus", "-");
				 		        				map.put("outtimediff", "-");
				 		        			 }
				 		        			map.put("ISOUTLET", "0");
				 		        		  }
			 		        			 
			 		        			 }else {
			 		        				Map termap = new HashMap<>();
			 		        				termap.put("TERMINAL", (String) pt.get("TERMINAL"));
			 		        				termap.put("TERMINALNAME", (String) pt.get("TERMINAL_NAME"));
			 		        				termap.put("intime",intime);
			 		        				termap.put("TOTALCOST",0.0);
			 		        				termap.put("TOTALPRICE",0.0);
			 		        				termap.put("outtime",outtime);
			 		        				termap.put("OUTLET",poutname);
			 		        				termap.put("ORDDATE","");
			 		        				termap.put("OUTLETNAME",(String) po.get("OUTLET_NAME"));
			 		        				termap.put("ISOUTLET", "0");
											 
											 if(!intime.equalsIgnoreCase("00:00")) {
				 		        			  long starttime = findDiff(tinitime, intime);
				 		        			  String infiff = splitToComponentTimes(starttime);
				 		        			  if(starttime < 0) {
				 		        				 termap.put("intimestatus", "red");
				 		        				 termap.put("intimediff", infiff);
				 		        			  }else {
				 		        				 termap.put("intimestatus", "green");
				 		        				 termap.put("intimediff", infiff);
				 		        			  }
				 		        			  }else {
				 		        				 termap.put("intimestatus", "-"); 
				 		        				 termap.put("intimediff", "-");
				 		        			  }
				 		        			  
				 		        			 if(!outtime.equalsIgnoreCase("00:00")) {
				 		        			 long closetime = findDiff(outtime, touttime);
				 		        			 String outdiff = splitToComponentTimes(closetime);
				 		        			 if(closetime < 0) {
				 		        				 termap.put("outtimestatus", "red");
				 		        				 termap.put("outtimediff", outdiff);
				 		        			  }else {
					 		        		     termap.put("outtimestatus", "green");
					 		        		     termap.put("outtimediff", outdiff);
					 		        		  }
				 		        			 }else {
				 		        				termap.put("outtimestatus", "-");
				 		        				termap.put("outtimediff", "-");
				 		        			 }
				 		        			if(!intime.equalsIgnoreCase("00:00"))
											 psaledata.add(termap);
			 		        			  }
			 		        			  outputlist5.addAll(psaledata);
			 	        			  }
			 	        			  
			 	        				
			 					 }
			 					Map outmap5 = new HashMap<>();
		        			  	outmap5.put("TERMINAL", plantname);
		        				outmap5.put("TERMINALNAME", "");
		        				outmap5.put("intime","");
		        				outmap5.put("TOTALCOST",totalsalescost5);
		        				outmap5.put("TOTALPRICE",totalsalesprice5);
		        				outmap5.put("outtime","");
		        				outmap5.put("intimediff","");
		        				outmap5.put("outtimediff", "");
		        				outmap5.put("OUTLET","");
		        				outmap5.put("outtimestatus","");
		        				outmap5.put("ORDDATE","");
		        				outmap5.put("OUTLETNAME","");
		        				outmap5.put("intimestatus","");
		        				outmap5.put("ISOUTLET", "1");
		        				if(totalsalesprice5 > 0 || outputlist5.size() > 0)
		        				resultlist.add(outmap5);
		        				resultlist.addAll(outputlist5);
		        			  
		        			  
		        			  
		        		  }
		        	  }
		        	  
		        	  
		        	  }
				 }
				
				jsonObjectResult.put("SEARCH_DATA", resultlist); 
					 
				
			} catch (Exception e) {
				jsonObjectResult.put("SEARCH_DATA", "");
	            JSONObject resultJsonInt = new JSONObject();
	            resultJsonInt.put("ERROR_MESSAGE",  e.getMessage());
	            resultJsonInt.put("ERROR_CODE", "98");
	            jsonArrayErr.add(resultJsonInt);
	            jsonObjectResult.put("ERROR", jsonArrayErr);
			}
		}
		
		
		
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		this.mLogger.auditInfo(this.printInfo, jsonObjectResult.toString());
		response.getWriter().write(jsonObjectResult.toString());
		response.getWriter().flush();
		response.getWriter().close();

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

	private JSONObject getCurrentDate(HttpServletRequest request) {
		JSONObject resultJson = new JSONObject();
		JSONArray jsonArray = new JSONArray();
		JSONArray jsonArrayErr = new JSONArray();
		JSONObject resultJsonInt = new JSONObject();
		resultJson.put("CURRENT_DATE", new java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").format(new java.util.Date()));
		resultJsonInt.put("ERROR_MESSAGE", "NO ERRORS!");
		resultJsonInt.put("ERROR_CODE", "100");
		jsonArrayErr.add(resultJsonInt);
		jsonArray.add(resultJson);
		resultJson.put("time", jsonArray);
		resultJson.put("errors", jsonArrayErr);
		return resultJson;
	}

	private JSONObject getTotalReceipt(HttpServletRequest request) {
		JSONObject resultJson = new JSONObject();
		JSONArray jsonArray = new JSONArray();
		JSONArray jsonArrayErr = new JSONArray();
		try {

			String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
			String fromDate = StrUtils.fString(request.getParameter("FROM_DATE")).trim();
			String toDate = StrUtils.fString(request.getParameter("TO_DATE")).trim();
			List listQry = "track".equalsIgnoreCase(plant) ? new ArrayList() : new POUtil().getTotalReceipt(plant, fromDate, toDate);
			if (listQry.size() > 0) {
				for (int iCnt = 0; iCnt < listQry.size(); iCnt++) {
//					int iIndex = iCnt + 1;
					HashMap hmItem = (HashMap) listQry.get(iCnt);
					JSONObject resultJsonInt = new JSONObject();
					resultJsonInt.put("TOTAL_RECEIPT", (String) hmItem.get("TOTAL_RECEIPT"));
					resultJsonInt.put("TOTAL_RECQTY", (String) hmItem.get("TOTAL_RECQTY"));
					jsonArray.add(resultJsonInt);
				}
				JSONObject resultJsonInt = new JSONObject();
				resultJsonInt.put("ERROR_MESSAGE", "NO ERRORS!");
				resultJsonInt.put("ERROR_CODE", "100");
				jsonArrayErr.add(resultJsonInt);
				resultJson.put("receipts", jsonArray);
				resultJson.put("errors", jsonArrayErr);
			} else {
				JSONObject resultJsonInt = new JSONObject();
				resultJsonInt.put("ERROR_MESSAGE", "NO PRODUCT RECORD FOUND!");
				resultJsonInt.put("ERROR_CODE", "99");
				jsonArrayErr.add(resultJsonInt);
				jsonArray.add("");
				resultJson.put("receipts", jsonArray);
				resultJson.put("errors", jsonArrayErr);
			}
			resultJson.put("recordsFiltered", listQry.size());
		} catch (Exception e) {
			resultJson.put("SEARCH_DATA", "");
			JSONObject resultJsonInt = new JSONObject();
			resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
			resultJsonInt.put("ERROR_CODE", "98");
			jsonArrayErr.add(resultJsonInt);
			jsonArray.add("");
			resultJson.put("receipts", jsonArray);
			resultJson.put("errors", jsonArrayErr);
			resultJson.put("recordsFiltered", 0);
		}
		return resultJson;
	}

	private JSONObject getTotalIssue(HttpServletRequest request) {
		JSONObject resultJson = new JSONObject();
		JSONArray jsonArray = new JSONArray();
		JSONArray jsonArrayErr = new JSONArray();
		try {

			String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
			String fromDate = StrUtils.fString(request.getParameter("FROM_DATE")).trim();
			String toDate = StrUtils.fString(request.getParameter("TO_DATE")).trim();
			List listQry = "track".equalsIgnoreCase(plant) ? new ArrayList() : new DOUtil().getTotalIssue(plant, fromDate, toDate);
			if (listQry.size() > 0) {
				for (int iCnt = 0; iCnt < listQry.size(); iCnt++) {
//					int iIndex = iCnt + 1;
					HashMap hmItem = (HashMap) listQry.get(iCnt);
					JSONObject resultJsonInt = new JSONObject();
					resultJsonInt.put("TOTAL_ISSUE", (String) hmItem.get("TOTAL_ISSUE"));
					resultJsonInt.put("TOTAL_PICKQTY", (String) hmItem.get("TOTAL_PICKQTY"));
					jsonArray.add(resultJsonInt);
				}
				JSONObject resultJsonInt = new JSONObject();
				resultJsonInt.put("ERROR_MESSAGE", "NO ERRORS!");
				resultJsonInt.put("ERROR_CODE", "100");
				jsonArrayErr.add(resultJsonInt);
				resultJson.put("issues", jsonArray);
				resultJson.put("errors", jsonArrayErr);
			} else {
				JSONObject resultJsonInt = new JSONObject();
				resultJsonInt.put("ERROR_MESSAGE", "NO PRODUCT RECORD FOUND!");
				resultJsonInt.put("ERROR_CODE", "99");
				jsonArrayErr.add(resultJsonInt);
				jsonArray.add("");
				resultJson.put("issues", jsonArray);
				resultJson.put("errors", jsonArrayErr);
			}
			resultJson.put("recordsFiltered", listQry.size());
		} catch (Exception e) {
			resultJson.put("SEARCH_DATA", "");
			JSONObject resultJsonInt = new JSONObject();
			resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
			resultJsonInt.put("ERROR_CODE", "98");
			jsonArrayErr.add(resultJsonInt);
			jsonArray.add("");
			resultJson.put("issues", jsonArray);
			resultJson.put("errors", jsonArrayErr);
			resultJson.put("recordsFiltered", 0);
		}
		return resultJson;
	}

	private JSONObject getTopReceivedProducts(HttpServletRequest request) {
		JSONObject resultJson = new JSONObject();
		JSONArray jsonArray = new JSONArray();
		JSONArray jsonArrayErr = new JSONArray();
		try {

			String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
			String fromDate = StrUtils.fString(request.getParameter("FROM_DATE")).trim();
			String toDate = StrUtils.fString(request.getParameter("TO_DATE")).trim();
			List listQry = "track".equalsIgnoreCase(plant) ? new ArrayList() : new POUtil().getTopReceivedProducts(plant, fromDate, toDate);
			if (listQry.size() > 0) {
				for (int iCnt = 0; iCnt < listQry.size(); iCnt++) {
//					int iIndex = iCnt + 1;
					HashMap hmItem = (HashMap) listQry.get(iCnt);
					JSONObject resultJsonInt = new JSONObject();
					resultJsonInt.put("PRODUCT", (String) hmItem.get("PRODUCT"));
					resultJsonInt.put("ITEMDESC", (String) hmItem.get("ITEMDESC"));
					resultJsonInt.put("RECEIVEDQTY", (String) hmItem.get("RECEIVEDQTY"));
					jsonArray.add(resultJsonInt);
				}
				JSONObject resultJsonInt = new JSONObject();
				resultJsonInt.put("ERROR_MESSAGE", "NO ERRORS!");
				resultJsonInt.put("ERROR_CODE", "100");
				jsonArrayErr.add(resultJsonInt);
				resultJson.put("products", jsonArray);
				resultJson.put("errors", jsonArrayErr);
			} else {
				JSONObject resultJsonInt = new JSONObject();
				resultJsonInt.put("ERROR_MESSAGE", "NO PRODUCT RECORD FOUND!");
				resultJsonInt.put("ERROR_CODE", "99");
				jsonArrayErr.add(resultJsonInt);
				jsonArray.add("");
				resultJson.put("products", jsonArray);
				resultJson.put("errors", jsonArrayErr);
			}
			resultJson.put("recordsFiltered", listQry.size());
		} catch (Exception e) {
			resultJson.put("SEARCH_DATA", "");
			JSONObject resultJsonInt = new JSONObject();
			resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
			resultJsonInt.put("ERROR_CODE", "98");
			jsonArrayErr.add(resultJsonInt);
			jsonArray.add("");
			resultJson.put("products", jsonArray);
			resultJson.put("errors", jsonArrayErr);
			resultJson.put("recordsFiltered", 0);
		}
		return resultJson;
	}

	private JSONObject getTopIssuedProducts(HttpServletRequest request) {
		JSONObject resultJson = new JSONObject();
		JSONArray jsonArray = new JSONArray();
		JSONArray jsonArrayErr = new JSONArray();
		try {

			String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
			String fromDate = StrUtils.fString(request.getParameter("FROM_DATE")).trim();
			String toDate = StrUtils.fString(request.getParameter("TO_DATE")).trim();
			List listQry = "track".equalsIgnoreCase(plant) ? new ArrayList() : new DOUtil().getTopIssuedProducts(plant, fromDate, toDate);
			if (listQry.size() > 0) {
				for (int iCnt = 0; iCnt < listQry.size(); iCnt++) {
//					int iIndex = iCnt + 1;
					HashMap hmItem = (HashMap) listQry.get(iCnt);
					JSONObject resultJsonInt = new JSONObject();
					resultJsonInt.put("PRODUCT", (String) hmItem.get("PRODUCT"));
					resultJsonInt.put("ITEMDESC", (String) hmItem.get("ITEMDESC"));
					resultJsonInt.put("ISSUEDQTY", (String) hmItem.get("ISSUEDQTY"));
					jsonArray.add(resultJsonInt);
				}
				JSONObject resultJsonInt = new JSONObject();
				resultJsonInt.put("ERROR_MESSAGE", "NO ERRORS!");
				resultJsonInt.put("ERROR_CODE", "100");
				jsonArrayErr.add(resultJsonInt);
				resultJson.put("products", jsonArray);
				resultJson.put("errors", jsonArrayErr);
			} else {
				JSONObject resultJsonInt = new JSONObject();
				resultJsonInt.put("ERROR_MESSAGE", "NO PRODUCT RECORD FOUND!");
				resultJsonInt.put("ERROR_CODE", "99");
				jsonArrayErr.add(resultJsonInt);
				jsonArray.add("");
				resultJson.put("products", jsonArray);
				resultJson.put("errors", jsonArrayErr);
			}
			resultJson.put("recordsFiltered", listQry.size());
		} catch (Exception e) {
			resultJson.put("SEARCH_DATA", "");
			JSONObject resultJsonInt = new JSONObject();
			resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
			resultJsonInt.put("ERROR_CODE", "98");
			jsonArrayErr.add(resultJsonInt);
			jsonArray.add("");
			resultJson.put("products", jsonArray);
			resultJson.put("errors", jsonArrayErr);
			resultJson.put("recordsFiltered", 0);
		}
		return resultJson;
	}

	private JSONObject getExpiringProducts(HttpServletRequest request) {
		JSONObject resultJson = new JSONObject();
		JSONArray jsonArray = new JSONArray();
		JSONArray jsonArrayErr = new JSONArray();
		try {

			String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
			String fromDate = StrUtils.fString(request.getParameter("FROM_DATE")).trim();
			String toDate = StrUtils.fString(request.getParameter("TO_DATE")).trim();
			List listQry = "track".equalsIgnoreCase(plant) ? new ArrayList() : new ItemMstUtil().getExpiringProducts(plant, fromDate, toDate);
			if (listQry.size() > 0) {
				for (int iCnt = 0; iCnt < listQry.size(); iCnt++) {
//					int iIndex = iCnt + 1;
					HashMap hmItem = (HashMap) listQry.get(iCnt);
					JSONObject resultJsonInt = new JSONObject();
					resultJsonInt.put("PRODUCT", (String) hmItem.get("PRODUCT"));
					resultJsonInt.put("LOCATION", (String) hmItem.get("LOCATION"));
					resultJsonInt.put("BATCH", (String) hmItem.get("BATCH"));
					resultJsonInt.put("EXPIRY_DATE", (String) hmItem.get("EXPIRY_DATE"));
					resultJsonInt.put("QUANTITY", (String) hmItem.get("QUANTITY"));
					jsonArray.add(resultJsonInt);
				}
				JSONObject resultJsonInt = new JSONObject();
				resultJsonInt.put("ERROR_MESSAGE", "NO ERRORS!");
				resultJsonInt.put("ERROR_CODE", "100");
				jsonArrayErr.add(resultJsonInt);
				resultJson.put("products", jsonArray);
				resultJson.put("errors", jsonArrayErr);
			} else {
				JSONObject resultJsonInt = new JSONObject();
				resultJsonInt.put("ERROR_MESSAGE", "NO PRODUCT RECORD FOUND!");
				resultJsonInt.put("ERROR_CODE", "99");
				jsonArrayErr.add(resultJsonInt);
				jsonArray.add("");
				resultJson.put("products", jsonArray);
				resultJson.put("errors", jsonArrayErr);
			}
			resultJson.put("recordsFiltered", listQry.size());
		} catch (Exception e) {
			resultJson.put("SEARCH_DATA", "");
			JSONObject resultJsonInt = new JSONObject();
			resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
			resultJsonInt.put("ERROR_CODE", "98");
			jsonArrayErr.add(resultJsonInt);
			jsonArray.add("");
			resultJson.put("products", jsonArray);
			resultJson.put("errors", jsonArrayErr);
			resultJson.put("recordsFiltered", 0);
		}
		return resultJson;
	}

	private JSONObject getLowStockProducts(HttpServletRequest request) {
		JSONObject resultJson = new JSONObject();
		JSONArray jsonArray = new JSONArray();
		JSONArray jsonArrayErr = new JSONArray();
		try {

			String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
			List listQry = "track".equalsIgnoreCase(plant) ? new ArrayList() : new InvMstUtil().getLowQuantityProducts(plant);
			if (listQry.size() > 0) {
				for (int iCnt = 0; iCnt < listQry.size(); iCnt++) {
//					int iIndex = iCnt + 1;
					HashMap hmItem = (HashMap) listQry.get(iCnt);
					JSONObject resultJsonInt = new JSONObject();
					resultJsonInt.put("PRODUCT", (String) hmItem.get("PRODUCT"));
					resultJsonInt.put("ITEMDESC", (String) hmItem.get("ITEMDESC"));
					resultJsonInt.put("INVENTORYUOM", (String) hmItem.get("INVENTORYUOM"));
					resultJsonInt.put("MIN_QUANTITY", (String) hmItem.get("MIN_QUANTITY"));
					resultJsonInt.put("CURR_QUANTITY", (String) hmItem.get("CURR_QUANTITY"));
					resultJsonInt.put("TOTAL_AVG", (String) hmItem.get("TOTAL_AVG"));
					jsonArray.add(resultJsonInt);
				}
				JSONObject resultJsonInt = new JSONObject();
				resultJsonInt.put("ERROR_MESSAGE", "NO ERRORS!");
				resultJsonInt.put("ERROR_CODE", "100");
				jsonArrayErr.add(resultJsonInt);
				resultJson.put("products", jsonArray);
				resultJson.put("errors", jsonArrayErr);
			} else {
				JSONObject resultJsonInt = new JSONObject();
				resultJsonInt.put("ERROR_MESSAGE", "NO PRODUCT RECORD FOUND!");
				resultJsonInt.put("ERROR_CODE", "99");
				jsonArrayErr.add(resultJsonInt);
				jsonArray.add("");
				resultJson.put("products", jsonArray);
				resultJson.put("errors", jsonArrayErr);
			}
			resultJson.put("recordsFiltered", listQry.size());
		} catch (Exception e) {
			resultJson.put("SEARCH_DATA", "");
			JSONObject resultJsonInt = new JSONObject();
			resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
			resultJsonInt.put("ERROR_CODE", "98");
			jsonArrayErr.add(resultJsonInt);
			jsonArray.add("");
			resultJson.put("products", jsonArray);
			resultJson.put("errors", jsonArrayErr);
			resultJson.put("recordsFiltered", 0);
		}
		return resultJson;
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

	public HashMap<String, String> populateMapData(String companyCode, String userCode) {
		HashMap<String, String> loggerDetailsHasMap = new HashMap<String, String>();
		loggerDetailsHasMap.put(MLogger.COMPANY_CODE, companyCode);
		loggerDetailsHasMap.put(MLogger.USER_CODE, userCode);
		return loggerDetailsHasMap;

	}
	
	/*Start code by Abhilash on 02-11-2019*/
	private JSONObject getTotalReceiptByProduct(HttpServletRequest request) {
		JSONObject resultJson = new JSONObject();
		JSONArray jsonArray = new JSONArray();
		JSONArray jsonArrayErr = new JSONArray();
		try {
			String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
			String fromDate = StrUtils.fString(request.getParameter("FROM_DATE")).trim();
			String toDate = StrUtils.fString(request.getParameter("TO_DATE")).trim();
			String item = StrUtils.fString(request.getParameter("ITEM")).trim();
			List listQry = "track".equalsIgnoreCase(plant) ? new ArrayList() : new POUtil().getTotalInvQtyByProduct(item, plant, fromDate, toDate);
			//List listQry = "track".equalsIgnoreCase(plant) ? new ArrayList() : new POUtil().getTotalReceiptByProduct(item, plant, fromDate, toDate);
			if (listQry.size() > 0) {
				for (int iCnt = 0; iCnt < listQry.size(); iCnt++) {
//					int iIndex = iCnt + 1;
					HashMap hmItem = (HashMap) listQry.get(iCnt);
					JSONObject resultJsonInt = new JSONObject();
					resultJsonInt.put("TOTAL_RECEIPT", (String) hmItem.get("TOTAL_RECEIPT"));
					jsonArray.add(resultJsonInt);
				}
				JSONObject resultJsonInt = new JSONObject();
				resultJsonInt.put("ERROR_MESSAGE", "NO ERRORS!");
				resultJsonInt.put("ERROR_CODE", "100");
				jsonArrayErr.add(resultJsonInt);
				resultJson.put("receipts", jsonArray);
				resultJson.put("errors", jsonArrayErr);
			} else {
				JSONObject resultJsonInt = new JSONObject();
				resultJsonInt.put("ERROR_MESSAGE", "NO PRODUCT RECORD FOUND!");
				resultJsonInt.put("ERROR_CODE", "99");
				jsonArrayErr.add(resultJsonInt);
				jsonArray.add("");
				resultJson.put("receipts", jsonArray);
				resultJson.put("errors", jsonArrayErr);
			}
			resultJson.put("recordsFiltered", listQry.size());
		} catch (Exception e) {
			resultJson.put("SEARCH_DATA", "");
			JSONObject resultJsonInt = new JSONObject();
			resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
			resultJsonInt.put("ERROR_CODE", "98");
			jsonArrayErr.add(resultJsonInt);
			jsonArray.add("");
			resultJson.put("receipts", jsonArray);
			resultJson.put("errors", jsonArrayErr);
			resultJson.put("recordsFiltered", 0);
		}
		return resultJson;
	}
	
	private JSONObject getTotalIssueByProduct(HttpServletRequest request) {
		JSONObject resultJson = new JSONObject();
		JSONArray jsonArray = new JSONArray();
		JSONArray jsonArrayErr = new JSONArray();
		try {
			String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
			String fromDate = StrUtils.fString(request.getParameter("FROM_DATE")).trim();
			String toDate = StrUtils.fString(request.getParameter("TO_DATE")).trim();
			String item = StrUtils.fString(request.getParameter("ITEM")).trim();
			List listQry = "track".equalsIgnoreCase(plant) ? new ArrayList() : new DOUtil().getTotalIssueQtyByProduct(item, plant, fromDate, toDate);
			//List listQry = "track".equalsIgnoreCase(plant) ? new ArrayList() : new DOUtil().getTotalIssueByProduct(item, plant, fromDate, toDate);
			if (listQry.size() > 0) {
				for (int iCnt = 0; iCnt < listQry.size(); iCnt++) {
//					int iIndex = iCnt + 1;
					HashMap hmItem = (HashMap) listQry.get(iCnt);
					JSONObject resultJsonInt = new JSONObject();
					resultJsonInt.put("TOTAL_ISSUE", (String) hmItem.get("TOTAL_ISSUE"));
					jsonArray.add(resultJsonInt);
				}
				JSONObject resultJsonInt = new JSONObject();
				resultJsonInt.put("ERROR_MESSAGE", "NO ERRORS!");
				resultJsonInt.put("ERROR_CODE", "100");
				jsonArrayErr.add(resultJsonInt);
				resultJson.put("issues", jsonArray);
				resultJson.put("errors", jsonArrayErr);
			} else {
				JSONObject resultJsonInt = new JSONObject();
				resultJsonInt.put("ERROR_MESSAGE", "NO PRODUCT RECORD FOUND!");
				resultJsonInt.put("ERROR_CODE", "99");
				jsonArrayErr.add(resultJsonInt);
				jsonArray.add("");
				resultJson.put("issues", jsonArray);
				resultJson.put("errors", jsonArrayErr);
			}
			resultJson.put("recordsFiltered", listQry.size());
		} catch (Exception e) {
			resultJson.put("SEARCH_DATA", "");
			JSONObject resultJsonInt = new JSONObject();
			resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
			resultJsonInt.put("ERROR_CODE", "98");
			jsonArrayErr.add(resultJsonInt);
			jsonArray.add("");
			resultJson.put("issues", jsonArray);
			resultJson.put("errors", jsonArrayErr);
			resultJson.put("recordsFiltered", 0);
		}
		return resultJson;
	}
	/*End code by Abhilash on 02-11-2019*/
	
	private JSONObject getNewSuppliers(HttpServletRequest request) {
		JSONObject resultJson = new JSONObject();
		JSONArray jsonArray = new JSONArray();
		JSONArray jsonArrayErr = new JSONArray();
		try {
			String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
			List listQry = "track".equalsIgnoreCase(plant) ? new ArrayList() : new VendUtil().getNewSuppliers(plant);
			if (listQry.size() > 0) {
				for (int iCnt = 0; iCnt < listQry.size(); iCnt++) {
//					int iIndex = iCnt + 1;
					HashMap hmItem = (HashMap) listQry.get(iCnt);
					JSONObject resultJsonInt = new JSONObject();
					resultJsonInt.put("VENDNO", (String) hmItem.get("VENDNO"));
					resultJsonInt.put("VNAME", (String) hmItem.get("VNAME"));
					resultJsonInt.put("NAME", (String) hmItem.get("NAME"));
					resultJsonInt.put("HPNO", (String) hmItem.get("HPNO"));
					resultJsonInt.put("EMAIL", (String) hmItem.get("EMAIL"));
					jsonArray.add(resultJsonInt);
				}
				JSONObject resultJsonInt = new JSONObject();
				resultJsonInt.put("ERROR_MESSAGE", "NO ERRORS!");
				resultJsonInt.put("ERROR_CODE", "100");
				jsonArrayErr.add(resultJsonInt);
				resultJson.put("suppliers", jsonArray);
				resultJson.put("errors", jsonArrayErr);
			} else {
				JSONObject resultJsonInt = new JSONObject();
				resultJsonInt.put("ERROR_MESSAGE", "NO PRODUCT RECORD FOUND!");
				resultJsonInt.put("ERROR_CODE", "99");
				jsonArrayErr.add(resultJsonInt);
				jsonArray.add("");
				resultJson.put("suppliers", jsonArray);
				resultJson.put("errors", jsonArrayErr);
			}
			resultJson.put("recordsFiltered", listQry.size());
		} catch (Exception e) {
			resultJson.put("SEARCH_DATA", "");
			JSONObject resultJsonInt = new JSONObject();
			resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
			resultJsonInt.put("ERROR_CODE", "98");
			jsonArrayErr.add(resultJsonInt);
			jsonArray.add("");
			resultJson.put("suppliers", jsonArray);
			resultJson.put("errors", jsonArrayErr);
			resultJson.put("recordsFiltered", 0);
		}
		return resultJson;
	}
	
	private JSONObject getTotalSuppliers(HttpServletRequest request) {
		JSONObject resultJson = new JSONObject();
		JSONArray jsonArray = new JSONArray();
		JSONArray jsonArrayErr = new JSONArray();
		try {

			String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
			List listQry = "track".equalsIgnoreCase(plant) ? new ArrayList() : new VendMstDAO().getTotalSupplier(plant);
			if (listQry.size() > 0) {
				for (int iCnt = 0; iCnt < listQry.size(); iCnt++) {
//					int iIndex = iCnt + 1;
					HashMap hmItem = (HashMap) listQry.get(iCnt);
					JSONObject resultJsonInt = new JSONObject();
					resultJsonInt.put("TOTAL_SUPPLIERS", (String) hmItem.get("TOTAL_SUPPLIERS"));
					jsonArray.add(resultJsonInt);
				}
				JSONObject resultJsonInt = new JSONObject();
				resultJsonInt.put("ERROR_MESSAGE", "NO ERRORS!");
				resultJsonInt.put("ERROR_CODE", "100");
				jsonArrayErr.add(resultJsonInt);
				resultJson.put("suppliers", jsonArray);
				resultJson.put("errors", jsonArrayErr);
			} else {
				JSONObject resultJsonInt = new JSONObject();
				resultJsonInt.put("ERROR_MESSAGE", "NO PRODUCT RECORD FOUND!");
				resultJsonInt.put("ERROR_CODE", "99");
				jsonArrayErr.add(resultJsonInt);
				jsonArray.add("");
				resultJson.put("suppliers", jsonArray);
				resultJson.put("errors", jsonArrayErr);
			}
			resultJson.put("recordsFiltered", listQry.size());
		} catch (Exception e) {
			resultJson.put("SEARCH_DATA", "");
			JSONObject resultJsonInt = new JSONObject();
			resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
			resultJsonInt.put("ERROR_CODE", "98");
			jsonArrayErr.add(resultJsonInt);
			jsonArray.add("");
			resultJson.put("suppliers", jsonArray);
			resultJson.put("errors", jsonArrayErr);
			resultJson.put("recordsFiltered", 0);
		}
		return resultJson;
	}
	
	private JSONObject getTopSuppliers(HttpServletRequest request) {
		JSONObject resultJson = new JSONObject();
		JSONArray jsonArray = new JSONArray();
		JSONArray jsonArrayErr = new JSONArray();
		try {
			String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
			String fromDate = StrUtils.fString(request.getParameter("FROM_DATE")).trim();
			String toDate = StrUtils.fString(request.getParameter("TO_DATE")).trim();
			String period = StrUtils.fString(request.getParameter("PERIOD")).trim();
			PlantMstDAO plantMstDAO = new PlantMstDAO();
		    String numberOfDecimal = plantMstDAO.getNumberOfDecimal(plant);
			List listQry = "track".equalsIgnoreCase(plant) ? new ArrayList() : new VendUtil().getTopSupplier(plant, period, fromDate, toDate,numberOfDecimal);
			if (listQry.size() > 0) {
				for (int iCnt = 0; iCnt < listQry.size(); iCnt++) {
//					int iIndex = iCnt + 1;
					HashMap hmItem = (HashMap) listQry.get(iCnt);
					JSONObject resultJsonInt = new JSONObject();
					resultJsonInt.put("CUSTNAME", (String) hmItem.get("CUSTNAME"));
					resultJsonInt.put("TOTAL_COST", (String) hmItem.get("TOTAL_COST"));
					jsonArray.add(resultJsonInt);
				}
				JSONObject resultJsonInt = new JSONObject();
				resultJsonInt.put("ERROR_MESSAGE", "NO ERRORS!");
				resultJsonInt.put("ERROR_CODE", "100");
				jsonArrayErr.add(resultJsonInt);
				resultJson.put("suppliers", jsonArray);
				resultJson.put("errors", jsonArrayErr);
			} else {
				JSONObject resultJsonInt = new JSONObject();
				resultJsonInt.put("ERROR_MESSAGE", "NO PRODUCT RECORD FOUND!");
				resultJsonInt.put("ERROR_CODE", "99");
				jsonArrayErr.add(resultJsonInt);
				jsonArray.add("");
				resultJson.put("suppliers", jsonArray);
				resultJson.put("errors", jsonArrayErr);
			}
			resultJson.put("recordsFiltered", listQry.size());
		} catch (Exception e) {
			resultJson.put("SEARCH_DATA", "");
			JSONObject resultJsonInt = new JSONObject();
			resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
			resultJsonInt.put("ERROR_CODE", "98");
			jsonArrayErr.add(resultJsonInt);
			jsonArray.add("");
			resultJson.put("suppliers", jsonArray);
			resultJson.put("errors", jsonArrayErr);
			resultJson.put("recordsFiltered", 0);
		}
		return resultJson;
	}
	
	private JSONObject getPurchaseSummary(HttpServletRequest request) {
		JSONObject resultJson = new JSONObject();
		JSONArray jsonArray = new JSONArray();
		JSONArray jsonArrayErr = new JSONArray();
		try {

			String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
			String fromDate = StrUtils.fString(request.getParameter("FROM_DATE")).trim();
			String toDate = StrUtils.fString(request.getParameter("TO_DATE")).trim();
			String period = StrUtils.fString(request.getParameter("PERIOD")).trim();
			PlantMstDAO plantMstDAO = new PlantMstDAO();
		    String numberOfDecimal = plantMstDAO.getNumberOfDecimal(plant);
			List listQry = "track".equalsIgnoreCase(plant) ? new ArrayList() : new VendUtil().getPurchaseSummary(plant, period, fromDate, toDate, numberOfDecimal);
			if (listQry.size() > 0) {
				for (int iCnt = 0; iCnt < listQry.size(); iCnt++) {
//					int iIndex = iCnt + 1;
					HashMap hmItem = (HashMap) listQry.get(iCnt);
					JSONObject resultJsonInt = new JSONObject();
					resultJsonInt.put("PURCHASE_DATE", (String) hmItem.get("PURCHASE_DATE"));
					resultJsonInt.put("TOTAL_COST", (String) hmItem.get("TOTAL_COST"));
					jsonArray.add(resultJsonInt);
				}
				JSONObject resultJsonInt = new JSONObject();
				resultJsonInt.put("ERROR_MESSAGE", "NO ERRORS!");
				resultJsonInt.put("ERROR_CODE", "100");
				jsonArrayErr.add(resultJsonInt);
				resultJson.put("suppliers", jsonArray);
				resultJson.put("errors", jsonArrayErr);
			} else {
				JSONObject resultJsonInt = new JSONObject();
				resultJsonInt.put("ERROR_MESSAGE", "NO PRODUCT RECORD FOUND!");
				resultJsonInt.put("ERROR_CODE", "99");
				jsonArrayErr.add(resultJsonInt);
				jsonArray.add("");
				resultJson.put("suppliers", jsonArray);
				resultJson.put("errors", jsonArrayErr);
			}
			resultJson.put("recordsFiltered", listQry.size());
		} catch (Exception e) {
			resultJson.put("SEARCH_DATA", "");
			JSONObject resultJsonInt = new JSONObject();
			resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
			resultJsonInt.put("ERROR_CODE", "98");
			jsonArrayErr.add(resultJsonInt);
			jsonArray.add("");
			resultJson.put("suppliers", jsonArray);
			resultJson.put("errors", jsonArrayErr);
			resultJson.put("recordsFiltered", 0);
		}
		return resultJson;
	}
	
	private JSONArray getPurchaseDeliveryDate(HttpServletRequest request) {
		JSONObject resultJson = new JSONObject();
		JSONArray jsonArray = new JSONArray();
		JSONArray jsonArrayErr = new JSONArray();
		try {

			String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
			List listQry = "track".equalsIgnoreCase(plant) ? new ArrayList() : new VendUtil().getPurchaseDeliveryDate(plant);
			if (listQry.size() > 0) {
				for (int iCnt = 0; iCnt < listQry.size(); iCnt++) {
//					int iIndex = iCnt + 1;
					HashMap hmItem = (HashMap) listQry.get(iCnt);
					JSONObject resultJsonInt = new JSONObject();
					resultJsonInt.put("title", (String) hmItem.get("PONO"));
					resultJsonInt.put("start", (String) hmItem.get("DELIVERY_DATE"));
					resultJsonInt.put("PONO", (String) hmItem.get("PONO"));
					resultJsonInt.put("CUSTNAME", (String) hmItem.get("CUSTNAME"));
					resultJsonInt.put("COLLECTIONDATE", (String) hmItem.get("CollectionDate"));
					resultJsonInt.put("DELIVERY_DATE", (String) hmItem.get("DELDATE"));
					resultJsonInt.put("TOTAL_COST", (String) hmItem.get("TOTAL_COST"));
					resultJsonInt.put("TOTAL_QTY", (String) hmItem.get("TOTAL_QTY"));
					jsonArray.add(resultJsonInt);
				}
				JSONObject resultJsonInt = new JSONObject();
				resultJsonInt.put("ERROR_MESSAGE", "NO ERRORS!");
				resultJsonInt.put("ERROR_CODE", "100");
				jsonArrayErr.add(resultJsonInt);
				resultJson.put("", jsonArray);
				resultJson.put("errors", jsonArrayErr);
			} else {
				JSONObject resultJsonInt = new JSONObject();
				resultJsonInt.put("ERROR_MESSAGE", "NO PRODUCT RECORD FOUND!");
				resultJsonInt.put("ERROR_CODE", "99");
				jsonArrayErr.add(resultJsonInt);
				jsonArray.add("");
				resultJson.put("suppliers", jsonArray);
				resultJson.put("errors", jsonArrayErr);
			}
			resultJson.put("recordsFiltered", listQry.size());
		} catch (Exception e) {
			resultJson.put("SEARCH_DATA", "");
			JSONObject resultJsonInt = new JSONObject();
			resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
			resultJsonInt.put("ERROR_CODE", "98");
			jsonArrayErr.add(resultJsonInt);
			jsonArray.add("");
			resultJson.put("suppliers", jsonArray);
			resultJson.put("errors", jsonArrayErr);
			resultJson.put("recordsFiltered", 0);
		}
		return jsonArray;
	}
	
	private JSONArray getExpiredPurchaseOrder(HttpServletRequest request) {
		JSONObject resultJson = new JSONObject();
		JSONArray jsonArray = new JSONArray();
		JSONArray jsonArrayErr = new JSONArray();
		try {

			String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
			List listQry = "track".equalsIgnoreCase(plant) ? new ArrayList() : new VendUtil().getExpiredPurchaseOrder(plant);
			if (listQry.size() > 0) {
				for (int iCnt = 0; iCnt < listQry.size(); iCnt++) {
//					int iIndex = iCnt + 1;
					HashMap hmItem = (HashMap) listQry.get(iCnt);
					JSONObject resultJsonInt = new JSONObject();
					resultJsonInt.put("title", (String) hmItem.get("PONO"));
					resultJsonInt.put("start", (String) hmItem.get("DELIVERY_DATE"));
					resultJsonInt.put("PONO", (String) hmItem.get("PONO"));
					resultJsonInt.put("CUSTNAME", (String) hmItem.get("CUSTNAME"));
					resultJsonInt.put("COLLECTIONDATE", (String) hmItem.get("CollectionDate"));
					resultJsonInt.put("DELIVERY_DATE", (String) hmItem.get("DELDATE"));
					resultJsonInt.put("TOTAL_COST", (String) hmItem.get("TOTAL_COST"));
					jsonArray.add(resultJsonInt);
				}
				JSONObject resultJsonInt = new JSONObject();
				resultJsonInt.put("ERROR_MESSAGE", "NO ERRORS!");
				resultJsonInt.put("ERROR_CODE", "100");
				jsonArrayErr.add(resultJsonInt);
				resultJson.put("", jsonArray);
				resultJson.put("errors", jsonArrayErr);
			} else {
				JSONObject resultJsonInt = new JSONObject();
				resultJsonInt.put("ERROR_MESSAGE", "NO PRODUCT RECORD FOUND!");
				resultJsonInt.put("ERROR_CODE", "99");
				jsonArrayErr.add(resultJsonInt);
				jsonArray.add("");
				resultJson.put("suppliers", jsonArray);
				resultJson.put("errors", jsonArrayErr);
			}
			resultJson.put("recordsFiltered", listQry.size());
		} catch (Exception e) {
			resultJson.put("SEARCH_DATA", "");
			JSONObject resultJsonInt = new JSONObject();
			resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
			resultJsonInt.put("ERROR_CODE", "98");
			jsonArrayErr.add(resultJsonInt);
			jsonArray.add("");
			resultJson.put("suppliers", jsonArray);
			resultJson.put("errors", jsonArrayErr);
			resultJson.put("recordsFiltered", 0);
		}
		return jsonArray;
	}
	
	private JSONObject getSalesSummary(HttpServletRequest request) {
		JSONObject resultJson = new JSONObject();
		JSONArray jsonArray = new JSONArray();
		JSONArray jsonArrayErr = new JSONArray();
		try {

			String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
			String fromDate = StrUtils.fString(request.getParameter("FROM_DATE")).trim();
			String toDate = StrUtils.fString(request.getParameter("TO_DATE")).trim();
			String period = StrUtils.fString(request.getParameter("PERIOD")).trim();
			PlantMstDAO plantMstDAO = new PlantMstDAO();
		    String numberOfDecimal = plantMstDAO.getNumberOfDecimal(plant);
			List listQry = "track".equalsIgnoreCase(plant) ? new ArrayList() : new DOUtil().getSalesSummary(plant, period, fromDate, toDate, numberOfDecimal);
			if (listQry.size() > 0) {
				for (int iCnt = 0; iCnt < listQry.size(); iCnt++) {
//					int iIndex = iCnt + 1;
					HashMap hmItem = (HashMap) listQry.get(iCnt);
					JSONObject resultJsonInt = new JSONObject();
					resultJsonInt.put("SOLD_DATE", (String) hmItem.get("SOLD_DATE"));
					resultJsonInt.put("TOTAL_PRICE", (String) hmItem.get("TOTAL_PRICE"));
					jsonArray.add(resultJsonInt);
				}
				JSONObject resultJsonInt = new JSONObject();
				resultJsonInt.put("ERROR_MESSAGE", "NO ERRORS!");
				resultJsonInt.put("ERROR_CODE", "100");
				jsonArrayErr.add(resultJsonInt);
				resultJson.put("sales", jsonArray);
				resultJson.put("errors", jsonArrayErr);
			} else {
				JSONObject resultJsonInt = new JSONObject();
				resultJsonInt.put("ERROR_MESSAGE", "NO PRODUCT RECORD FOUND!");
				resultJsonInt.put("ERROR_CODE", "99");
				jsonArrayErr.add(resultJsonInt);
				jsonArray.add("");
				resultJson.put("sales", jsonArray);
				resultJson.put("errors", jsonArrayErr);
			}
			resultJson.put("recordsFiltered", listQry.size());
		} catch (Exception e) {
			resultJson.put("SEARCH_DATA", "");
			JSONObject resultJsonInt = new JSONObject();
			resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
			resultJsonInt.put("ERROR_CODE", "98");
			jsonArrayErr.add(resultJsonInt);
			jsonArray.add("");
			resultJson.put("sales", jsonArray);
			resultJson.put("errors", jsonArrayErr);
			resultJson.put("recordsFiltered", 0);
		}
		return resultJson;
	}
	
	private JSONObject getTopCustomers(HttpServletRequest request) {
		JSONObject resultJson = new JSONObject();
		JSONArray jsonArray = new JSONArray();
		JSONArray jsonArrayErr = new JSONArray();
		try {
			String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
			String fromDate = StrUtils.fString(request.getParameter("FROM_DATE")).trim();
			String toDate = StrUtils.fString(request.getParameter("TO_DATE")).trim();
			String period = StrUtils.fString(request.getParameter("PERIOD")).trim();
			PlantMstDAO plantMstDAO = new PlantMstDAO();
		    String numberOfDecimal = plantMstDAO.getNumberOfDecimal(plant);
			List listQry = "track".equalsIgnoreCase(plant) ? new ArrayList() : new CustUtil().getTopCustomer(plant, period, fromDate, toDate,numberOfDecimal);
			if (listQry.size() > 0) {
				for (int iCnt = 0; iCnt < listQry.size(); iCnt++) {
//					int iIndex = iCnt + 1;
					HashMap hmItem = (HashMap) listQry.get(iCnt);
					JSONObject resultJsonInt = new JSONObject();
					resultJsonInt.put("CUSTNAME", (String) hmItem.get("CUSTNAME"));
					resultJsonInt.put("TOTAL_PRICE", (String) hmItem.get("TOTAL_PRICE"));
					jsonArray.add(resultJsonInt);
				}
				JSONObject resultJsonInt = new JSONObject();
				resultJsonInt.put("ERROR_MESSAGE", "NO ERRORS!");
				resultJsonInt.put("ERROR_CODE", "100");
				jsonArrayErr.add(resultJsonInt);
				resultJson.put("customers", jsonArray);
				resultJson.put("errors", jsonArrayErr);
			} else {
				JSONObject resultJsonInt = new JSONObject();
				resultJsonInt.put("ERROR_MESSAGE", "NO PRODUCT RECORD FOUND!");
				resultJsonInt.put("ERROR_CODE", "99");
				jsonArrayErr.add(resultJsonInt);
				jsonArray.add("");
				resultJson.put("customers", jsonArray);
				resultJson.put("errors", jsonArrayErr);
			}
			resultJson.put("recordsFiltered", listQry.size());
		} catch (Exception e) {
			resultJson.put("SEARCH_DATA", "");
			JSONObject resultJsonInt = new JSONObject();
			resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
			resultJsonInt.put("ERROR_CODE", "98");
			jsonArrayErr.add(resultJsonInt);
			jsonArray.add("");
			resultJson.put("customers", jsonArray);
			resultJson.put("errors", jsonArrayErr);
			resultJson.put("recordsFiltered", 0);
		}
		return resultJson;
	}
	
	private JSONObject getNewCustomers(HttpServletRequest request) {
		JSONObject resultJson = new JSONObject();
		JSONArray jsonArray = new JSONArray();
		JSONArray jsonArrayErr = new JSONArray();
		try {
			String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
			List listQry = "track".equalsIgnoreCase(plant) ? new ArrayList() : new CustUtil().getNewCustomers(plant);
			if (listQry.size() > 0) {
				for (int iCnt = 0; iCnt < listQry.size(); iCnt++) {
//					int iIndex = iCnt + 1;
					HashMap hmItem = (HashMap) listQry.get(iCnt);
					JSONObject resultJsonInt = new JSONObject();
					resultJsonInt.put("CUSTNO", (String) hmItem.get("CUSTNO"));
					resultJsonInt.put("CNAME", (String) hmItem.get("CNAME"));
					resultJsonInt.put("NAME", (String) hmItem.get("NAME"));
					resultJsonInt.put("HPNO", (String) hmItem.get("HPNO"));
					resultJsonInt.put("EMAIL", (String) hmItem.get("EMAIL"));
					jsonArray.add(resultJsonInt);
				}
				JSONObject resultJsonInt = new JSONObject();
				resultJsonInt.put("ERROR_MESSAGE", "NO ERRORS!");
				resultJsonInt.put("ERROR_CODE", "100");
				jsonArrayErr.add(resultJsonInt);
				resultJson.put("customers", jsonArray);
				resultJson.put("errors", jsonArrayErr);
			} else {
				JSONObject resultJsonInt = new JSONObject();
				resultJsonInt.put("ERROR_MESSAGE", "NO PRODUCT RECORD FOUND!");
				resultJsonInt.put("ERROR_CODE", "99");
				jsonArrayErr.add(resultJsonInt);
				jsonArray.add("");
				resultJson.put("customers", jsonArray);
				resultJson.put("errors", jsonArrayErr);
			}
			resultJson.put("recordsFiltered", listQry.size());
		} catch (Exception e) {
			resultJson.put("SEARCH_DATA", "");
			JSONObject resultJsonInt = new JSONObject();
			resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
			resultJsonInt.put("ERROR_CODE", "98");
			jsonArrayErr.add(resultJsonInt);
			jsonArray.add("");
			resultJson.put("customers", jsonArray);
			resultJson.put("errors", jsonArrayErr);
			resultJson.put("recordsFiltered", 0);
		}
		return resultJson;
	}
	
	private JSONObject getTotalCustomers(HttpServletRequest request) {
		JSONObject resultJson = new JSONObject();
		JSONArray jsonArray = new JSONArray();
		JSONArray jsonArrayErr = new JSONArray();
		try {

			String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
			List listQry = "track".equalsIgnoreCase(plant) ? new ArrayList() : new CustMstDAO().getTotalCustomers(plant);
			if (listQry.size() > 0) {
				for (int iCnt = 0; iCnt < listQry.size(); iCnt++) {
//					int iIndex = iCnt + 1;
					HashMap hmItem = (HashMap) listQry.get(iCnt);
					JSONObject resultJsonInt = new JSONObject();
					resultJsonInt.put("TOTAL_CUSTOMERS", (String) hmItem.get("TOTAL_CUSTOMERS"));
					jsonArray.add(resultJsonInt);
				}
				JSONObject resultJsonInt = new JSONObject();
				resultJsonInt.put("ERROR_MESSAGE", "NO ERRORS!");
				resultJsonInt.put("ERROR_CODE", "100");
				jsonArrayErr.add(resultJsonInt);
				resultJson.put("customers", jsonArray);
				resultJson.put("errors", jsonArrayErr);
			} else {
				JSONObject resultJsonInt = new JSONObject();
				resultJsonInt.put("ERROR_MESSAGE", "NO PRODUCT RECORD FOUND!");
				resultJsonInt.put("ERROR_CODE", "99");
				jsonArrayErr.add(resultJsonInt);
				jsonArray.add("");
				resultJson.put("customers", jsonArray);
				resultJson.put("errors", jsonArrayErr);
			}
			resultJson.put("recordsFiltered", listQry.size());
		} catch (Exception e) {
			resultJson.put("SEARCH_DATA", "");
			JSONObject resultJsonInt = new JSONObject();
			resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
			resultJsonInt.put("ERROR_CODE", "98");
			jsonArrayErr.add(resultJsonInt);
			jsonArray.add("");
			resultJson.put("customers", jsonArray);
			resultJson.put("errors", jsonArrayErr);
			resultJson.put("recordsFiltered", 0);
		}
		return resultJson;
	}
	
	private JSONArray getPendingSalesEstimate(HttpServletRequest request) {
		JSONObject resultJson = new JSONObject();
		JSONArray jsonArray = new JSONArray();
		JSONArray jsonArrayErr = new JSONArray();
		try {

			String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
			PlantMstDAO plantMstDAO = new PlantMstDAO();
		    String numberOfDecimal = plantMstDAO.getNumberOfDecimal(plant);
			List listQry = "track".equalsIgnoreCase(plant) ? new ArrayList() : new ESTUtil().getPendingSalesEstimate(plant,numberOfDecimal);
			if (listQry.size() > 0) {
				for (int iCnt = 0; iCnt < listQry.size(); iCnt++) {
//					int iIndex = iCnt + 1;
					HashMap hmItem = (HashMap) listQry.get(iCnt);
					JSONObject resultJsonInt = new JSONObject();
					resultJsonInt.put("title", (String) hmItem.get("ESTNO"));
					resultJsonInt.put("start", (String) hmItem.get("DEL_DATE"));
					resultJsonInt.put("ESTNO", (String) hmItem.get("ESTNO"));
					resultJsonInt.put("CUSTNAME", (String) hmItem.get("CustName"));
					resultJsonInt.put("COLLECTIONDATE", (String) hmItem.get("CollectionDate"));
					resultJsonInt.put("DELIVERY_DATE", (String) hmItem.get("DELIVERYDATE"));
					resultJsonInt.put("TOTAL_PRICE", (String) hmItem.get("TOTAL_PRICE"));
					jsonArray.add(resultJsonInt);
				}
				JSONObject resultJsonInt = new JSONObject();
				resultJsonInt.put("ERROR_MESSAGE", "NO ERRORS!");
				resultJsonInt.put("ERROR_CODE", "100");
				jsonArrayErr.add(resultJsonInt);
				resultJson.put("", jsonArray);
				resultJson.put("errors", jsonArrayErr);
			} else {
				JSONObject resultJsonInt = new JSONObject();
				resultJsonInt.put("ERROR_MESSAGE", "NO PRODUCT RECORD FOUND!");
				resultJsonInt.put("ERROR_CODE", "99");
				jsonArrayErr.add(resultJsonInt);
				jsonArray.add("");
				resultJson.put("estimates", jsonArray);
				resultJson.put("errors", jsonArrayErr);
			}
			resultJson.put("recordsFiltered", listQry.size());
		} catch (Exception e) {
			resultJson.put("SEARCH_DATA", "");
			JSONObject resultJsonInt = new JSONObject();
			resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
			resultJsonInt.put("ERROR_CODE", "98");
			jsonArrayErr.add(resultJsonInt);
			jsonArray.add("");
			resultJson.put("estimates", jsonArray);
			resultJson.put("errors", jsonArrayErr);
			resultJson.put("recordsFiltered", 0);
		}
		return jsonArray;
	}
	
	private JSONArray getSalesOrderDeliveryDate(HttpServletRequest request) {
		JSONObject resultJson = new JSONObject();
		JSONArray jsonArray = new JSONArray();
		JSONArray jsonArrayErr = new JSONArray();
		try {

			String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
			PlantMstDAO plantMstDAO = new PlantMstDAO();
		    String numberOfDecimal = plantMstDAO.getNumberOfDecimal(plant);
			List listQry = "track".equalsIgnoreCase(plant) ? new ArrayList() : new DOUtil().getSalesOrderDeliveryDate(plant,numberOfDecimal);
			if (listQry.size() > 0) {
				for (int iCnt = 0; iCnt < listQry.size(); iCnt++) {
//					int iIndex = iCnt + 1;
					HashMap hmItem = (HashMap) listQry.get(iCnt);
					JSONObject resultJsonInt = new JSONObject();
					resultJsonInt.put("title", (String) hmItem.get("DONO"));
					resultJsonInt.put("start", (String) hmItem.get("DELIVERY_DATE"));
					resultJsonInt.put("DONO", (String) hmItem.get("DONO"));
					resultJsonInt.put("CUSTNAME", (String) hmItem.get("CUSTNAME"));
					resultJsonInt.put("COLLECTIONDATE", (String) hmItem.get("CollectionDate"));
					resultJsonInt.put("DELIVERY_DATE", (String) hmItem.get("DELIVERYDATE"));
					resultJsonInt.put("TOTAL_PRICE", (String) hmItem.get("TOTAL_PRICE"));
					jsonArray.add(resultJsonInt);
				}
				JSONObject resultJsonInt = new JSONObject();
				resultJsonInt.put("ERROR_MESSAGE", "NO ERRORS!");
				resultJsonInt.put("ERROR_CODE", "100");
				jsonArrayErr.add(resultJsonInt);
				resultJson.put("", jsonArray);
				resultJson.put("errors", jsonArrayErr);
			} else {
				JSONObject resultJsonInt = new JSONObject();
				resultJsonInt.put("ERROR_MESSAGE", "NO PRODUCT RECORD FOUND!");
				resultJsonInt.put("ERROR_CODE", "99");
				jsonArrayErr.add(resultJsonInt);
				jsonArray.add("");
				resultJson.put("sales", jsonArray);
				resultJson.put("errors", jsonArrayErr);
			}
			resultJson.put("recordsFiltered", listQry.size());
		} catch (Exception e) {
			resultJson.put("SEARCH_DATA", "");
			JSONObject resultJsonInt = new JSONObject();
			resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
			resultJsonInt.put("ERROR_CODE", "98");
			jsonArrayErr.add(resultJsonInt);
			jsonArray.add("");
			resultJson.put("sales", jsonArray);
			resultJson.put("errors", jsonArrayErr);
			resultJson.put("recordsFiltered", 0);
		}
		return jsonArray;
	}
	
	private JSONObject getTopSalesProduct(HttpServletRequest request) {
		JSONObject resultJson = new JSONObject();
		JSONArray jsonArray1 = new JSONArray();
		JSONArray jsonArray2 = new JSONArray();
		JSONArray jsonArray3 = new JSONArray();
		JSONArray jsonArray4 = new JSONArray();
		JSONArray jsonArray5 = new JSONArray();
		JSONArray jsonArrayErr = new JSONArray();
		try {
			String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
			String fromDate = StrUtils.fString(request.getParameter("FROM_DATE")).trim();
			String toDate = StrUtils.fString(request.getParameter("TO_DATE")).trim();
			String period = StrUtils.fString(request.getParameter("PERIOD")).trim();
			PlantMstDAO plantMstDAO = new PlantMstDAO();
		    String numberOfDecimal = plantMstDAO.getNumberOfDecimal(plant);
			List listQry = "track".equalsIgnoreCase(plant) ? new ArrayList() : new DOUtil().getTopSalesItem(plant, fromDate, toDate,numberOfDecimal);
			if (listQry.size() > 0) {
				for (int iCnt = 0; iCnt < listQry.size(); iCnt++) {
//					int iIndex = iCnt + 1;
					HashMap hmItem = (HashMap) listQry.get(iCnt);
					
					List listQry2 = "track".equalsIgnoreCase(plant) ? new ArrayList() : new DOUtil().getTopSalesItemDetail(plant, period, fromDate, toDate, (String) hmItem.get("ITEM"), numberOfDecimal);
					if (listQry2.size() > 0) {
						for (int iCnt1 = 0; iCnt1 < listQry2.size(); iCnt1++) {
						JSONObject resultJsonInt = new JSONObject();
						HashMap hmItem1 = (HashMap) listQry2.get(iCnt1);
						resultJsonInt.put("ITEM", (String) hmItem.get("ITEM"));
						resultJsonInt.put("SOLD_DATE", (String) hmItem1.get("SOLD_DATE"));
						resultJsonInt.put("TOTAL_PRICE", (String) hmItem1.get("TOTAL_PRICE"));
						if(iCnt == 0)
							jsonArray1.add(resultJsonInt);
						else if(iCnt == 1)
							jsonArray2.add(resultJsonInt);
						else if(iCnt == 2)
							jsonArray3.add(resultJsonInt);
						else if(iCnt == 3)
							jsonArray4.add(resultJsonInt);
						else if(iCnt == 4)
							jsonArray5.add(resultJsonInt);
						}
					}
				}
				JSONObject resultJsonInt = new JSONObject();
				resultJsonInt.put("ERROR_MESSAGE", "NO ERRORS!");
				resultJsonInt.put("ERROR_CODE", "100");
				jsonArrayErr.add(resultJsonInt);
				resultJson.put("label1", jsonArray1);
				resultJson.put("label2", jsonArray2);
				resultJson.put("label3", jsonArray3);
				resultJson.put("label4", jsonArray4);
				resultJson.put("label5", jsonArray5);
				resultJson.put("errors", jsonArrayErr);
			} else {
				JSONObject resultJsonInt = new JSONObject();
				resultJsonInt.put("ERROR_MESSAGE", "NO PRODUCT RECORD FOUND!");
				resultJsonInt.put("ERROR_CODE", "99");
				jsonArrayErr.add(resultJsonInt);
				jsonArray1.add("");
				jsonArray2.add("");
				jsonArray3.add("");
				jsonArray4.add("");
				jsonArray5.add("");
				resultJson.put("label1", jsonArray1);
				resultJson.put("label2", jsonArray2);
				resultJson.put("label3", jsonArray3);
				resultJson.put("label4", jsonArray4);
				resultJson.put("label5", jsonArray5);
				resultJson.put("errors", jsonArrayErr);
			}
			resultJson.put("recordsFiltered", listQry.size());
		} catch (Exception e) {
			resultJson.put("SEARCH_DATA", "");
			JSONObject resultJsonInt = new JSONObject();
			resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
			resultJsonInt.put("ERROR_CODE", "98");
			jsonArrayErr.add(resultJsonInt);
			jsonArray1.add("");
			jsonArray2.add("");
			jsonArray3.add("");
			jsonArray4.add("");
			jsonArray5.add("");
			resultJson.put("label1", jsonArray1);
			resultJson.put("label2", jsonArray2);
			resultJson.put("label3", jsonArray3);
			resultJson.put("label4", jsonArray4);
			resultJson.put("label5", jsonArray5);
			resultJson.put("errors", jsonArrayErr);
			resultJson.put("recordsFiltered", 0);
		}
		return resultJson;
	}
	
	private JSONObject getPoWoPriceSummary(HttpServletRequest request) {
		JSONObject resultJson = new JSONObject();
		JSONArray jsonArray = new JSONArray();
		JSONArray jsonArrayErr = new JSONArray();
		try {

			String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
			String fromDate = StrUtils.fString(request.getParameter("FROM_DATE")).trim();
			String toDate = StrUtils.fString(request.getParameter("TO_DATE")).trim();
			String period = StrUtils.fString(request.getParameter("PERIOD")).trim();
			PlantMstDAO plantMstDAO = new PlantMstDAO();
		    String numberOfDecimal = plantMstDAO.getNumberOfDecimal(plant);
			List listQry = "track".equalsIgnoreCase(plant) ? new ArrayList() : new VendUtil().getPoWoPriceSummary(plant, period, fromDate, toDate, numberOfDecimal);
			if (listQry.size() > 0) {
				for (int iCnt = 0; iCnt < listQry.size(); iCnt++) {
//					int iIndex = iCnt + 1;
					HashMap hmItem = (HashMap) listQry.get(iCnt);
					JSONObject resultJsonInt = new JSONObject();
					resultJsonInt.put("PURCHASE_DATE", (String) hmItem.get("PURCHASE_DATE"));
					resultJsonInt.put("TOTAL_RECV_QTY", (String) hmItem.get("TOTAL_RECV_QTY"));
					jsonArray.add(resultJsonInt);
				}
				JSONObject resultJsonInt = new JSONObject();
				resultJsonInt.put("ERROR_MESSAGE", "NO ERRORS!");
				resultJsonInt.put("ERROR_CODE", "100");
				jsonArrayErr.add(resultJsonInt);
				resultJson.put("suppliers", jsonArray);
				resultJson.put("errors", jsonArrayErr);
			} else {
				JSONObject resultJsonInt = new JSONObject();
				resultJsonInt.put("ERROR_MESSAGE", "NO PRODUCT RECORD FOUND!");
				resultJsonInt.put("ERROR_CODE", "99");
				jsonArrayErr.add(resultJsonInt);
				jsonArray.add("");
				resultJson.put("suppliers", jsonArray);
				resultJson.put("errors", jsonArrayErr);
			}
			resultJson.put("recordsFiltered", listQry.size());
		} catch (Exception e) {
			resultJson.put("SEARCH_DATA", "");
			JSONObject resultJsonInt = new JSONObject();
			resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
			resultJsonInt.put("ERROR_CODE", "98");
			jsonArrayErr.add(resultJsonInt);
			jsonArray.add("");
			resultJson.put("suppliers", jsonArray);
			resultJson.put("errors", jsonArrayErr);
			resultJson.put("recordsFiltered", 0);
		}
		return resultJson;
	}
	
	private JSONObject getGrnSummary(HttpServletRequest request) {
		JSONObject resultJson = new JSONObject();
		JSONArray jsonArray = new JSONArray();
		JSONArray jsonArrayErr = new JSONArray();
		try {

			String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
			String fromDate = StrUtils.fString(request.getParameter("FROM_DATE")).trim();
			String toDate = StrUtils.fString(request.getParameter("TO_DATE")).trim();
			String period = StrUtils.fString(request.getParameter("PERIOD")).trim();
			PlantMstDAO plantMstDAO = new PlantMstDAO();
		    String numberOfDecimal = plantMstDAO.getNumberOfDecimal(plant);
			List listQry = "track".equalsIgnoreCase(plant) ? new ArrayList() : new VendUtil().getGrnSummaryDashboard(plant, period, fromDate, toDate, numberOfDecimal);
			if (listQry.size() > 0) {
				for (int iCnt = 0; iCnt < listQry.size(); iCnt++) {
//					int iIndex = iCnt + 1;
					HashMap hmItem = (HashMap) listQry.get(iCnt);
					JSONObject resultJsonInt = new JSONObject();
					resultJsonInt.put("RECEIVED_DATE", (String) hmItem.get("RECVDATE"));
					resultJsonInt.put("TOTAL_RECV_QTY", (String) hmItem.get("TOTAL_RECV_QTY"));
					jsonArray.add(resultJsonInt);
				}
				JSONObject resultJsonInt = new JSONObject();
				resultJsonInt.put("ERROR_MESSAGE", "NO ERRORS!");
				resultJsonInt.put("ERROR_CODE", "100");
				jsonArrayErr.add(resultJsonInt);
				resultJson.put("suppliers", jsonArray);
				resultJson.put("errors", jsonArrayErr);
			} else {
				JSONObject resultJsonInt = new JSONObject();
				resultJsonInt.put("ERROR_MESSAGE", "NO PRODUCT RECORD FOUND!");
				resultJsonInt.put("ERROR_CODE", "99");
				jsonArrayErr.add(resultJsonInt);
				jsonArray.add("");
				resultJson.put("suppliers", jsonArray);
				resultJson.put("errors", jsonArrayErr);
			}
			resultJson.put("recordsFiltered", listQry.size());
		} catch (Exception e) {
			resultJson.put("SEARCH_DATA", "");
			JSONObject resultJsonInt = new JSONObject();
			resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
			resultJsonInt.put("ERROR_CODE", "98");
			jsonArrayErr.add(resultJsonInt);
			jsonArray.add("");
			resultJson.put("suppliers", jsonArray);
			resultJson.put("errors", jsonArrayErr);
			resultJson.put("recordsFiltered", 0);
		}
		return resultJson;
	}
	
	private JSONObject getGriSummary(HttpServletRequest request) {
		JSONObject resultJson = new JSONObject();
		JSONArray jsonArray = new JSONArray();
		JSONArray jsonArrayErr = new JSONArray();
		try {

			String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
			String fromDate = StrUtils.fString(request.getParameter("FROM_DATE")).trim();
			String toDate = StrUtils.fString(request.getParameter("TO_DATE")).trim();
			String period = StrUtils.fString(request.getParameter("PERIOD")).trim();
			PlantMstDAO plantMstDAO = new PlantMstDAO();
		    String numberOfDecimal = plantMstDAO.getNumberOfDecimal(plant);
			List listQry = "track".equalsIgnoreCase(plant) ? new ArrayList() : new DOUtil().getGriSummaryDashboard(plant, period, fromDate, toDate, numberOfDecimal);
			if (listQry.size() > 0) {
				for (int iCnt = 0; iCnt < listQry.size(); iCnt++) {
//					int iIndex = iCnt + 1;
					HashMap hmItem = (HashMap) listQry.get(iCnt);
					JSONObject resultJsonInt = new JSONObject();
					resultJsonInt.put("ISSUEDATE", (String) hmItem.get("ISSUEDATE"));
					resultJsonInt.put("TOTAL_ISSUE_QTY", (String) hmItem.get("TOTAL_ISSUE_QTY"));
					jsonArray.add(resultJsonInt);
				}
				JSONObject resultJsonInt = new JSONObject();
				resultJsonInt.put("ERROR_MESSAGE", "NO ERRORS!");
				resultJsonInt.put("ERROR_CODE", "100");
				jsonArrayErr.add(resultJsonInt);
				resultJson.put("orders", jsonArray);
				resultJson.put("errors", jsonArrayErr);
			} else {
				JSONObject resultJsonInt = new JSONObject();
				resultJsonInt.put("ERROR_MESSAGE", "NO PRODUCT RECORD FOUND!");
				resultJsonInt.put("ERROR_CODE", "99");
				jsonArrayErr.add(resultJsonInt);
				jsonArray.add("");
				resultJson.put("orders", jsonArray);
				resultJson.put("errors", jsonArrayErr);
			}
			resultJson.put("recordsFiltered", listQry.size());
		} catch (Exception e) {
			resultJson.put("SEARCH_DATA", "");
			JSONObject resultJsonInt = new JSONObject();
			resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
			resultJsonInt.put("ERROR_CODE", "98");
			jsonArrayErr.add(resultJsonInt);
			jsonArray.add("");
			resultJson.put("orders", jsonArray);
			resultJson.put("errors", jsonArrayErr);
			resultJson.put("recordsFiltered", 0);
		}
		return resultJson;
	}
	
	private JSONArray getSalesOrderExpiredDeliveryDate(HttpServletRequest request) {
		JSONObject resultJson = new JSONObject();
		JSONArray jsonArray = new JSONArray();
		JSONArray jsonArrayErr = new JSONArray();
		try {

			String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
			PlantMstDAO plantMstDAO = new PlantMstDAO();
		    String numberOfDecimal = plantMstDAO.getNumberOfDecimal(plant);
			List listQry = "track".equalsIgnoreCase(plant) ? new ArrayList() : new DOUtil().getSalesOrderExpiredDeliveryDate(plant,numberOfDecimal);
			if (listQry.size() > 0) {
				for (int iCnt = 0; iCnt < listQry.size(); iCnt++) {
//					int iIndex = iCnt + 1;
					HashMap hmItem = (HashMap) listQry.get(iCnt);
					JSONObject resultJsonInt = new JSONObject();
					resultJsonInt.put("title", (String) hmItem.get("DONO"));
					resultJsonInt.put("start", (String) hmItem.get("DELIVERY_DATE"));
					resultJsonInt.put("DONO", (String) hmItem.get("DONO"));
					resultJsonInt.put("CUSTNAME", (String) hmItem.get("CUSTNAME"));
					resultJsonInt.put("COLLECTIONDATE", (String) hmItem.get("CollectionDate"));
					resultJsonInt.put("DELIVERY_DATE", (String) hmItem.get("DELIVERYDATE"));
					resultJsonInt.put("TOTAL_QTY", (String) hmItem.get("TOTAL_QTY"));
					jsonArray.add(resultJsonInt);
				}
				JSONObject resultJsonInt = new JSONObject();
				resultJsonInt.put("ERROR_MESSAGE", "NO ERRORS!");
				resultJsonInt.put("ERROR_CODE", "100");
				jsonArrayErr.add(resultJsonInt);
				resultJson.put("", jsonArray);
				resultJson.put("errors", jsonArrayErr);
			} else {
				JSONObject resultJsonInt = new JSONObject();
				resultJsonInt.put("ERROR_MESSAGE", "NO PRODUCT RECORD FOUND!");
				resultJsonInt.put("ERROR_CODE", "99");
				jsonArrayErr.add(resultJsonInt);
				jsonArray.add("");
				resultJson.put("sales", jsonArray);
				resultJson.put("errors", jsonArrayErr);
			}
			resultJson.put("recordsFiltered", listQry.size());
		} catch (Exception e) {
			resultJson.put("SEARCH_DATA", "");
			JSONObject resultJsonInt = new JSONObject();
			resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
			resultJsonInt.put("ERROR_CODE", "98");
			jsonArrayErr.add(resultJsonInt);
			jsonArray.add("");
			resultJson.put("sales", jsonArray);
			resultJson.put("errors", jsonArrayErr);
			resultJson.put("recordsFiltered", 0);
		}
		return jsonArray;
	}
	
	private JSONObject getReadyToPackOrders(HttpServletRequest request) {
		JSONObject resultJson = new JSONObject();
		JSONArray jsonArray = new JSONArray();
		JSONArray jsonArrayErr = new JSONArray();
		try {

			String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
			String fromDate = StrUtils.fString(request.getParameter("FROM_DATE")).trim();
			String toDate = StrUtils.fString(request.getParameter("TO_DATE")).trim();
			List listQry = "track".equalsIgnoreCase(plant) ? new ArrayList() : new DOUtil().getReadyToPackOrders(plant, fromDate, toDate);
			if (listQry.size() > 0) {
				for (int iCnt = 0; iCnt < listQry.size(); iCnt++) {
//					int iIndex = iCnt + 1;
					HashMap hmItem = (HashMap) listQry.get(iCnt);
					JSONObject resultJsonInt = new JSONObject();
					resultJsonInt.put("DONO", (String) hmItem.get("DONO"));
					resultJsonInt.put("CUSTNAME", (String) hmItem.get("CustName"));
					resultJsonInt.put("DOLNNO", (String) hmItem.get("DOLNNO"));
					resultJsonInt.put("UNITMO", (String) hmItem.get("UNITMO"));
					resultJsonInt.put("COLLECTIONDATE", (String) hmItem.get("CollectionDate"));
					resultJsonInt.put("QTYOR", (String) hmItem.get("QTYOR"));
					resultJsonInt.put("AVL_QTY", (String) hmItem.get("AVL_QTY"));
					jsonArray.add(resultJsonInt);
				}
				JSONObject resultJsonInt = new JSONObject();
				resultJsonInt.put("ERROR_MESSAGE", "NO ERRORS!");
				resultJsonInt.put("ERROR_CODE", "100");
				jsonArrayErr.add(resultJsonInt);
				resultJson.put("orders", jsonArray);
				resultJson.put("errors", jsonArrayErr);
			} else {
				JSONObject resultJsonInt = new JSONObject();
				resultJsonInt.put("ERROR_MESSAGE", "NO PRODUCT RECORD FOUND!");
				resultJsonInt.put("ERROR_CODE", "99");
				jsonArrayErr.add(resultJsonInt);
				jsonArray.add("");
				resultJson.put("orders", jsonArray);
				resultJson.put("errors", jsonArrayErr);
			}
			resultJson.put("recordsFiltered", listQry.size());
		} catch (Exception e) {
			resultJson.put("SEARCH_DATA", "");
			JSONObject resultJsonInt = new JSONObject();
			resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
			resultJsonInt.put("ERROR_CODE", "98");
			jsonArrayErr.add(resultJsonInt);
			jsonArray.add("");
			resultJson.put("orders", jsonArray);
			resultJson.put("errors", jsonArrayErr);
			resultJson.put("recordsFiltered", 0);
		}
		return resultJson;
	}
	
	private JSONObject getTotalPurchaseByBill(HttpServletRequest request) {
		JSONObject resultJson = new JSONObject();
		JSONArray jsonArray = new JSONArray();
		JSONArray jsonArrayErr = new JSONArray();
		try {
			String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
			String fromDate = StrUtils.fString(request.getParameter("FROM_DATE")).trim();
			String toDate = StrUtils.fString(request.getParameter("TO_DATE")).trim();
			List listQry = "track".equalsIgnoreCase(plant) ? new ArrayList() : new BillUtil().getTotalPurchaseForDashBoard(plant, fromDate, toDate);
			if (listQry.size() > 0) {
				for (int iCnt = 0; iCnt < listQry.size(); iCnt++) {
//					int iIndex = iCnt + 1;
					HashMap hmItem = (HashMap) listQry.get(iCnt);
					JSONObject resultJsonInt = new JSONObject();
					resultJsonInt.put("TOTAL_PURCHASE", (String) hmItem.get("TOTAL_PURCHASE"));
					jsonArray.add(resultJsonInt);
				}
				JSONObject resultJsonInt = new JSONObject();
				resultJsonInt.put("ERROR_MESSAGE", "NO ERRORS!");
				resultJsonInt.put("ERROR_CODE", "100");
				jsonArrayErr.add(resultJsonInt);
				resultJson.put("bills", jsonArray);
				resultJson.put("errors", jsonArrayErr);
			} else {
				JSONObject resultJsonInt = new JSONObject();
				resultJsonInt.put("ERROR_MESSAGE", "NO PRODUCT RECORD FOUND!");
				resultJsonInt.put("ERROR_CODE", "99");
				jsonArrayErr.add(resultJsonInt);
				jsonArray.add("");
				resultJson.put("bills", jsonArray);
				resultJson.put("errors", jsonArrayErr);
			}
			resultJson.put("recordsFiltered", listQry.size());
		} catch (Exception e) {
			resultJson.put("SEARCH_DATA", "");
			JSONObject resultJsonInt = new JSONObject();
			resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
			resultJsonInt.put("ERROR_CODE", "98");
			jsonArrayErr.add(resultJsonInt);
			jsonArray.add("");
			resultJson.put("bills", jsonArray);
			resultJson.put("errors", jsonArrayErr);
			resultJson.put("recordsFiltered", 0);
		}
		return resultJson;
	}
	
	private JSONObject getTotalPurchaseByBillFromJournal(HttpServletRequest request) {
		JSONObject resultJson = new JSONObject();
		JSONArray jsonArray = new JSONArray();
		JSONArray jsonArrayErr = new JSONArray();
		try {
			String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
			String fromDate = StrUtils.fString(request.getParameter("FROM_DATE")).trim();
			String toDate = StrUtils.fString(request.getParameter("TO_DATE")).trim();
			List listQry = "track".equalsIgnoreCase(plant) ? new ArrayList() : new BillUtil().getTotalPurchaseForDashBoardFromJournal(plant, fromDate, toDate);
			if (listQry.size() > 0) {
				for (int iCnt = 0; iCnt < listQry.size(); iCnt++) {
//					int iIndex = iCnt + 1;
					HashMap hmItem = (HashMap) listQry.get(iCnt);
					JSONObject resultJsonInt = new JSONObject();
					resultJsonInt.put("TOTAL_PURCHASE", (String) hmItem.get("TOTAL_PURCHASE"));
					jsonArray.add(resultJsonInt);
				}
				JSONObject resultJsonInt = new JSONObject();
				resultJsonInt.put("ERROR_MESSAGE", "NO ERRORS!");
				resultJsonInt.put("ERROR_CODE", "100");
				jsonArrayErr.add(resultJsonInt);
				resultJson.put("bills", jsonArray);
				resultJson.put("errors", jsonArrayErr);
			} else {
				JSONObject resultJsonInt = new JSONObject();
				resultJsonInt.put("ERROR_MESSAGE", "NO PRODUCT RECORD FOUND!");
				resultJsonInt.put("ERROR_CODE", "99");
				jsonArrayErr.add(resultJsonInt);
				jsonArray.add("");
				resultJson.put("bills", jsonArray);
				resultJson.put("errors", jsonArrayErr);
			}
			resultJson.put("recordsFiltered", listQry.size());
		} catch (Exception e) {
			resultJson.put("SEARCH_DATA", "");
			JSONObject resultJsonInt = new JSONObject();
			resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
			resultJsonInt.put("ERROR_CODE", "98");
			jsonArrayErr.add(resultJsonInt);
			jsonArray.add("");
			resultJson.put("bills", jsonArray);
			resultJson.put("errors", jsonArrayErr);
			resultJson.put("recordsFiltered", 0);
		}
		return resultJson;
	}
	
	private JSONObject getTotalSalesByInvoice(HttpServletRequest request) {
		JSONObject resultJson = new JSONObject();
		JSONArray jsonArray = new JSONArray();
		JSONArray jsonArrayErr = new JSONArray();
		try {
			String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
			String fromDate = StrUtils.fString(request.getParameter("FROM_DATE")).trim();
			String toDate = StrUtils.fString(request.getParameter("TO_DATE")).trim();
			List listQry = "track".equalsIgnoreCase(plant) ? new ArrayList() : new InvoiceUtil().getTotalSalesForDashBoard(plant, fromDate, toDate);
			if (listQry.size() > 0) {
				for (int iCnt = 0; iCnt < listQry.size(); iCnt++) {
//					int iIndex = iCnt + 1;
					HashMap hmItem = (HashMap) listQry.get(iCnt);
					JSONObject resultJsonInt = new JSONObject();
					resultJsonInt.put("TOTAL_SALES", (String) hmItem.get("TOTAL_SALES"));
					jsonArray.add(resultJsonInt);
				}
				JSONObject resultJsonInt = new JSONObject();
				resultJsonInt.put("ERROR_MESSAGE", "NO ERRORS!");
				resultJsonInt.put("ERROR_CODE", "100");
				jsonArrayErr.add(resultJsonInt);
				resultJson.put("invoices", jsonArray);
				resultJson.put("errors", jsonArrayErr);
			} else {
				JSONObject resultJsonInt = new JSONObject();
				resultJsonInt.put("ERROR_MESSAGE", "NO PRODUCT RECORD FOUND!");
				resultJsonInt.put("ERROR_CODE", "99");
				jsonArrayErr.add(resultJsonInt);
				jsonArray.add("");
				resultJson.put("invoices", jsonArray);
				resultJson.put("errors", jsonArrayErr);
			}
			resultJson.put("recordsFiltered", listQry.size());
		} catch (Exception e) {
			resultJson.put("SEARCH_DATA", "");
			JSONObject resultJsonInt = new JSONObject();
			resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
			resultJsonInt.put("ERROR_CODE", "98");
			jsonArrayErr.add(resultJsonInt);
			jsonArray.add("");
			resultJson.put("invoices", jsonArray);
			resultJson.put("errors", jsonArrayErr);
			resultJson.put("recordsFiltered", 0);
		}
		return resultJson;
	}
	
	private JSONObject getTotalSalesByInvoiceFromJournal(HttpServletRequest request) {
		JSONObject resultJson = new JSONObject();
		JSONArray jsonArray = new JSONArray();
		JSONArray jsonArrayErr = new JSONArray();
		try {
			String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
			String fromDate = StrUtils.fString(request.getParameter("FROM_DATE")).trim();
			String toDate = StrUtils.fString(request.getParameter("TO_DATE")).trim();
			List listQry = "track".equalsIgnoreCase(plant) ? new ArrayList() : new InvoiceUtil().getTotalSalesForDashBoardFromJournal(plant, fromDate, toDate);
			if (listQry.size() > 0) {
				for (int iCnt = 0; iCnt < listQry.size(); iCnt++) {
//					int iIndex = iCnt + 1;
					HashMap hmItem = (HashMap) listQry.get(iCnt);
					JSONObject resultJsonInt = new JSONObject();
					resultJsonInt.put("TOTAL_SALES", (String) hmItem.get("TOTAL_SALES"));
					jsonArray.add(resultJsonInt);
				}
				JSONObject resultJsonInt = new JSONObject();
				resultJsonInt.put("ERROR_MESSAGE", "NO ERRORS!");
				resultJsonInt.put("ERROR_CODE", "100");
				jsonArrayErr.add(resultJsonInt);
				resultJson.put("invoices", jsonArray);
				resultJson.put("errors", jsonArrayErr);
			} else {
				JSONObject resultJsonInt = new JSONObject();
				resultJsonInt.put("ERROR_MESSAGE", "NO PRODUCT RECORD FOUND!");
				resultJsonInt.put("ERROR_CODE", "99");
				jsonArrayErr.add(resultJsonInt);
				jsonArray.add("");
				resultJson.put("invoices", jsonArray);
				resultJson.put("errors", jsonArrayErr);
			}
			resultJson.put("recordsFiltered", listQry.size());
		} catch (Exception e) {
			resultJson.put("SEARCH_DATA", "");
			JSONObject resultJsonInt = new JSONObject();
			resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
			resultJsonInt.put("ERROR_CODE", "98");
			jsonArrayErr.add(resultJsonInt);
			jsonArray.add("");
			resultJson.put("invoices", jsonArray);
			resultJson.put("errors", jsonArrayErr);
			resultJson.put("recordsFiltered", 0);
		}
		return resultJson;
	}
	
	private JSONObject getTotalPurchaseSummaryByBill(HttpServletRequest request) {
		JSONObject resultJson = new JSONObject();
		JSONArray jsonArray = new JSONArray();
		JSONArray jsonArrayErr = new JSONArray();
		try {

			String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
			String fromDate = StrUtils.fString(request.getParameter("FROM_DATE")).trim();
			String toDate = StrUtils.fString(request.getParameter("TO_DATE")).trim();
			String period = StrUtils.fString(request.getParameter("PERIOD")).trim();
			String currency = StrUtils.fString((String) request.getSession().getAttribute("BASE_CURRENCY")).trim();
			double totalAmount= 0.0;
			PlantMstDAO plantMstDAO = new PlantMstDAO();
		    String numberOfDecimal = plantMstDAO.getNumberOfDecimal(plant);
		    List listQry = "track".equalsIgnoreCase(plant) ? new ArrayList() : new BillUtil().getTotalPurchaseSummaryByBill(plant, period, fromDate, toDate, numberOfDecimal);
			if (listQry.size() > 0) {
				for (int iCnt = 0; iCnt < listQry.size(); iCnt++) {
//					int iIndex = iCnt + 1;
					HashMap hmItem = (HashMap) listQry.get(iCnt);
					JSONObject resultJsonInt = new JSONObject();
					resultJsonInt.put("BILL_DATE", (String) hmItem.get("BILL_DATE"));
					resultJsonInt.put("TOTAL_COST", (String) hmItem.get("TOTAL_COST"));
					totalAmount += Double.parseDouble((String) hmItem.get("TOTAL_COST"));
					jsonArray.add(resultJsonInt);
				}
				JSONObject resultJsonInt = new JSONObject();
				resultJsonInt.put("ERROR_MESSAGE", "NO ERRORS!");
				resultJsonInt.put("ERROR_CODE", "100");
				jsonArrayErr.add(resultJsonInt);
				resultJson.put("bills", jsonArray);
				resultJson.put("errors", jsonArrayErr);
				resultJson.put("TOTAL_AMOUNT", currency+StrUtils.addZeroes(totalAmount, numberOfDecimal));
			} else {
				JSONObject resultJsonInt = new JSONObject();
				resultJsonInt.put("ERROR_MESSAGE", "NO PRODUCT RECORD FOUND!");
				resultJsonInt.put("ERROR_CODE", "99");
				jsonArrayErr.add(resultJsonInt);
				jsonArray.add("");
				resultJson.put("bills", jsonArray);
				resultJson.put("errors", jsonArrayErr);
			}
			resultJson.put("recordsFiltered", listQry.size());
		} catch (Exception e) {
			resultJson.put("SEARCH_DATA", "");
			JSONObject resultJsonInt = new JSONObject();
			resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
			resultJsonInt.put("ERROR_CODE", "98");
			jsonArrayErr.add(resultJsonInt);
			jsonArray.add("");
			resultJson.put("bills", jsonArray);
			resultJson.put("errors", jsonArrayErr);
			resultJson.put("recordsFiltered", 0);
		}
		return resultJson;
	}
	
	private JSONObject getTotalPurchaseSummaryByBillFromJournal(HttpServletRequest request) {
		JSONObject resultJson = new JSONObject();
		JSONArray jsonArray = new JSONArray();
		JSONArray jsonArrayErr = new JSONArray();
		BillUtil billUtil = new BillUtil();
//        DateUtils _dateUtils = new DateUtils();
        ArrayList movQryList  = new ArrayList();
        ArrayList movQryListfilter1  = new ArrayList();
		try {

			String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
			String fromDate = StrUtils.fString(request.getParameter("FROM_DATE")).trim();
			String toDate = StrUtils.fString(request.getParameter("TO_DATE")).trim();
			String period = StrUtils.fString(request.getParameter("PERIOD")).trim();
			String currency = StrUtils.fString((String) request.getSession().getAttribute("BASE_CURRENCY")).trim();
			double totalAmount= 0.0;
			PlantMstDAO plantMstDAO = new PlantMstDAO();
		    String numberOfDecimal = plantMstDAO.getNumberOfDecimal(plant);
		    
			
		    movQryList = billUtil.getpurchaseDashboard(plant,fromDate,toDate,"","",numberOfDecimal);	
			if (movQryList.size() > 0) {
				for (int iCnt =0; iCnt<movQryList.size(); iCnt++){
					Map lineArr = (Map) movQryList.get(iCnt);
					String condition = (String)lineArr.get("CONDITIONS");
					if(!condition.equalsIgnoreCase("")) {
						movQryListfilter1.add(lineArr);
					}
				}
			}
		    
			if(period.equalsIgnoreCase("Today") || period.equalsIgnoreCase("yesterday") || period.equalsIgnoreCase("Last 30 days") || period.equalsIgnoreCase("This Month") || period.equalsIgnoreCase("Last month") || period.equalsIgnoreCase("Last 7 days") || period.equalsIgnoreCase("This week") || period.equalsIgnoreCase("Last 15 days")) {
				SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
				SimpleDateFormat formatter1 = new SimpleDateFormat("dd MMM,yyyy");
				List<Date> getbdates = getbetweendates(fromDate, toDate);
				for(int i=0;i<getbdates.size();i++){
				    Date lDate =(Date)getbdates.get(i);
				    String ds = formatter.format(lDate);    
				    System.out.println(" Date is ..." + ds);
				    double amtadd =0;
				    for (int iCnt =0; iCnt<movQryListfilter1.size(); iCnt++){
						Map lineArr = (Map) movQryListfilter1.get(iCnt);
						String condition = (String)lineArr.get("JOURNAL_DATE");
						if(condition.equalsIgnoreCase(ds)) {
							amtadd = amtadd + Double.valueOf((String)lineArr.get("TOTAL_AMOUNT"));
						}
					}
				    
				    JSONObject resultJsonInt = new JSONObject();
					resultJsonInt.put("BILL_DATE", formatter1.format(lDate));
					resultJsonInt.put("TOTAL_COST", StrUtils.addZeroes(amtadd, numberOfDecimal));
					totalAmount += amtadd;
					jsonArray.add(resultJsonInt);
				}
				
				JSONObject resultJsonInt = new JSONObject();
				resultJsonInt.put("ERROR_MESSAGE", "NO ERRORS!");
				resultJsonInt.put("ERROR_CODE", "100");
				jsonArrayErr.add(resultJsonInt);
				resultJson.put("bills", jsonArray);
				resultJson.put("errors", jsonArrayErr);
				resultJson.put("currency", currency);
				String totalAmountWithoutCurrency = StrUtils.addZeroes(totalAmount, numberOfDecimal);
				resultJson.put("totalAmountWithoutCurrency", totalAmountWithoutCurrency);
				resultJson.put("TOTAL_AMOUNT", currency+totalAmountWithoutCurrency);
			}else if(period.equalsIgnoreCase("This quarter") || period.equalsIgnoreCase("This year") || period.equalsIgnoreCase("Last quarter") || period.equalsIgnoreCase("Last year")){
				SimpleDateFormat formatter2 = new SimpleDateFormat("dd-MMM-yyyy");
				SimpleDateFormat formatter3 = new SimpleDateFormat("MMMM-yyyy");
				SimpleDateFormat formatter4 = new SimpleDateFormat("dd/MM/yyyy");
				Calendar beginCalendar = Calendar.getInstance();
		        Calendar finishCalendar = Calendar.getInstance();
		        beginCalendar.setTime(formatter2.parse(fromDate));
	            finishCalendar.setTime(formatter2.parse(toDate));
	            finishCalendar.add(Calendar.MONTH, 1);
	            while (beginCalendar.before(finishCalendar)) {
	                // add one month to date per loop
	                String Bdate =     formatter3.format(beginCalendar.getTime()).toUpperCase();
	                beginCalendar.set(Calendar.DATE, beginCalendar.getActualMinimum(Calendar.DAY_OF_MONTH));
	                String fdate = formatter2.format(beginCalendar.getTime()).toUpperCase();
	                beginCalendar.set(Calendar.DATE, beginCalendar.getActualMaximum(Calendar.DAY_OF_MONTH));
	                String tdate = formatter2.format(beginCalendar.getTime()).toUpperCase();
	                
	                double amtadd =0;
	                List<Date> getbdates = getbetweendates(fdate, tdate);
					for(int i=0;i<getbdates.size();i++){
					    Date lDate =(Date)getbdates.get(i);
					    String ds = formatter4.format(lDate);    
					    for (int iCnt =0; iCnt<movQryListfilter1.size(); iCnt++){
							Map lineArr = (Map) movQryListfilter1.get(iCnt);
							String condition = (String)lineArr.get("JOURNAL_DATE");
							if(condition.equalsIgnoreCase(ds)) {
								System.out.println("Date----"+ds+"       "+Double.valueOf((String)lineArr.get("TOTAL_AMOUNT")));
								amtadd = amtadd + Double.valueOf((String)lineArr.get("TOTAL_AMOUNT"));
								System.out.println(amtadd);
							}
						}
					}
					
					JSONObject resultJsonInt = new JSONObject();
					resultJsonInt.put("BILL_DATE",  Bdate);
					resultJsonInt.put("TOTAL_COST",  StrUtils.addZeroes(amtadd, numberOfDecimal));
					totalAmount += amtadd;
					jsonArray.add(resultJsonInt);
	                
	                beginCalendar.add(Calendar.MONTH, 1);
	            }
	            
	            JSONObject resultJsonInt = new JSONObject();
				resultJsonInt.put("ERROR_MESSAGE", "NO ERRORS!");
				resultJsonInt.put("ERROR_CODE", "100");
				jsonArrayErr.add(resultJsonInt);
				resultJson.put("bills", jsonArray);
				resultJson.put("errors", jsonArrayErr);
				resultJson.put("currency", currency);
				String totalAmountWithoutCurrency = StrUtils.addZeroes(totalAmount, numberOfDecimal);
				resultJson.put("totalAmountWithoutCurrency", totalAmountWithoutCurrency);
				resultJson.put("TOTAL_AMOUNT", currency+totalAmountWithoutCurrency);
			}else {
				JSONObject resultJsonInt = new JSONObject();
				resultJsonInt.put("ERROR_MESSAGE", "NO PRODUCT RECORD FOUND!");
				resultJsonInt.put("ERROR_CODE", "99");
				jsonArrayErr.add(resultJsonInt);
				jsonArray.add("");
				resultJson.put("bills", jsonArray);
				resultJson.put("errors", jsonArrayErr);
			}
			
			resultJson.put("recordsFiltered", movQryListfilter1.size());
			
			
			/*List listQry = "track".equalsIgnoreCase(plant) ? new ArrayList() : new BillUtil().getTotalPurchaseSummaryByBillFromJournal(plant, period, fromDate, toDate, numberOfDecimal);
		    if (listQry.size() > 0) {
				for (int iCnt = 0; iCnt < listQry.size(); iCnt++) {
					int iIndex = iCnt + 1;
					HashMap hmItem = (HashMap) listQry.get(iCnt);
					JSONObject resultJsonInt = new JSONObject();
					resultJsonInt.put("BILL_DATE", (String) hmItem.get("BILL_DATE"));
					resultJsonInt.put("TOTAL_COST", (String) hmItem.get("TOTAL_COST"));
					totalAmount += Double.parseDouble((String) hmItem.get("TOTAL_COST"));
					jsonArray.add(resultJsonInt);
				}
				JSONObject resultJsonInt = new JSONObject();
				resultJsonInt.put("ERROR_MESSAGE", "NO ERRORS!");
				resultJsonInt.put("ERROR_CODE", "100");
				jsonArrayErr.add(resultJsonInt);
				resultJson.put("bills", jsonArray);
				resultJson.put("errors", jsonArrayErr);
				resultJson.put("TOTAL_AMOUNT", currency+StrUtils.addZeroes(totalAmount, numberOfDecimal));
			} else {
				JSONObject resultJsonInt = new JSONObject();
				resultJsonInt.put("ERROR_MESSAGE", "NO PRODUCT RECORD FOUND!");
				resultJsonInt.put("ERROR_CODE", "99");
				jsonArrayErr.add(resultJsonInt);
				jsonArray.add("");
				resultJson.put("bills", jsonArray);
				resultJson.put("errors", jsonArrayErr);
			}
			resultJson.put("recordsFiltered", listQry.size());*/
		} catch (Exception e) {
			resultJson.put("SEARCH_DATA", "");
			JSONObject resultJsonInt = new JSONObject();
			resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
			resultJsonInt.put("ERROR_CODE", "98");
			jsonArrayErr.add(resultJsonInt);
			jsonArray.add("");
			resultJson.put("bills", jsonArray);
			resultJson.put("errors", jsonArrayErr);
			resultJson.put("recordsFiltered", 0);
		}
		return resultJson;
	}
	
	private JSONObject getTotalSalesSummaryByInvoice(HttpServletRequest request) {
		JSONObject resultJson = new JSONObject();
		JSONArray jsonArray = new JSONArray();
		JSONArray jsonArrayErr = new JSONArray();
		try {

			String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
			String currency = StrUtils.fString((String) request.getSession().getAttribute("BASE_CURRENCY")).trim();
			String fromDate = StrUtils.fString(request.getParameter("FROM_DATE")).trim();
			String toDate = StrUtils.fString(request.getParameter("TO_DATE")).trim();
			String period = StrUtils.fString(request.getParameter("PERIOD")).trim();
			double totalAmount= 0.0;
			PlantMstDAO plantMstDAO = new PlantMstDAO();
		    String numberOfDecimal = plantMstDAO.getNumberOfDecimal(plant);
		    List listQry = "track".equalsIgnoreCase(plant) ? new ArrayList() : new InvoiceUtil().getTotalSalesSummaryByInvoice(plant, period, fromDate, toDate, numberOfDecimal);
			if (listQry.size() > 0) {
				for (int iCnt = 0; iCnt < listQry.size(); iCnt++) {
//					int iIndex = iCnt + 1;
					HashMap hmItem = (HashMap) listQry.get(iCnt);
					JSONObject resultJsonInt = new JSONObject();
					resultJsonInt.put("INVOICE_DATE", (String) hmItem.get("INVOICE_DATE"));
					resultJsonInt.put("TOTAL_PRICE", (String) hmItem.get("TOTAL_PRICE"));
					totalAmount += Double.parseDouble((String) hmItem.get("TOTAL_PRICE"));
					jsonArray.add(resultJsonInt);
				}
				JSONObject resultJsonInt = new JSONObject();
				resultJsonInt.put("ERROR_MESSAGE", "NO ERRORS!");
				resultJsonInt.put("ERROR_CODE", "100");
				jsonArrayErr.add(resultJsonInt);
				resultJson.put("invoices", jsonArray);
				resultJson.put("errors", jsonArrayErr);
				resultJson.put("TOTAL_AMOUNT", currency+StrUtils.addZeroes(totalAmount, numberOfDecimal));
			} else {
				JSONObject resultJsonInt = new JSONObject();
				resultJsonInt.put("ERROR_MESSAGE", "NO PRODUCT RECORD FOUND!");
				resultJsonInt.put("ERROR_CODE", "99");
				jsonArrayErr.add(resultJsonInt);
				jsonArray.add("");
				resultJson.put("invoices", jsonArray);
				resultJson.put("errors", jsonArrayErr);
			}
			resultJson.put("recordsFiltered", listQry.size());
		} catch (Exception e) {
			resultJson.put("SEARCH_DATA", "");
			JSONObject resultJsonInt = new JSONObject();
			resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
			resultJsonInt.put("ERROR_CODE", "98");
			jsonArrayErr.add(resultJsonInt);
			jsonArray.add("");
			resultJson.put("invoices", jsonArray);
			resultJson.put("errors", jsonArrayErr);
			resultJson.put("recordsFiltered", 0);
		}
		return resultJson;
	}
	
/*	private JSONObject getTotalSalesSummaryByInvoiceFromJournal(HttpServletRequest request) {
		JSONObject resultJson = new JSONObject();
		JSONArray jsonArray = new JSONArray();
		JSONArray jsonArrayErr = new JSONArray();
		try {

			String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
			String currency = StrUtils.fString((String) request.getSession().getAttribute("BASE_CURRENCY")).trim();
			String fromDate = StrUtils.fString(request.getParameter("FROM_DATE")).trim();
			String toDate = StrUtils.fString(request.getParameter("TO_DATE")).trim();
			String period = StrUtils.fString(request.getParameter("PERIOD")).trim();
			double totalAmount= 0.0;
			PlantMstDAO plantMstDAO = new PlantMstDAO();
		    String numberOfDecimal = plantMstDAO.getNumberOfDecimal(plant);
		    List listQry = "track".equalsIgnoreCase(plant) ? new ArrayList() : new InvoiceUtil().getTotalSalesSummaryByInvoiceFromJournal(plant, period, fromDate, toDate, numberOfDecimal);
			if (listQry.size() > 0) {
				for (int iCnt = 0; iCnt < listQry.size(); iCnt++) {
					int iIndex = iCnt + 1;
					HashMap hmItem = (HashMap) listQry.get(iCnt);
					JSONObject resultJsonInt = new JSONObject();
					resultJsonInt.put("INVOICE_DATE", (String) hmItem.get("INVOICE_DATE"));
					resultJsonInt.put("TOTAL_PRICE", (String) hmItem.get("TOTAL_PRICE"));
					totalAmount += Double.parseDouble((String) hmItem.get("TOTAL_PRICE"));
					jsonArray.add(resultJsonInt);
				}
				JSONObject resultJsonInt = new JSONObject();
				resultJsonInt.put("ERROR_MESSAGE", "NO ERRORS!");
				resultJsonInt.put("ERROR_CODE", "100");
				jsonArrayErr.add(resultJsonInt);
				resultJson.put("invoices", jsonArray);
				resultJson.put("errors", jsonArrayErr);
				resultJson.put("TOTAL_AMOUNT", currency+StrUtils.addZeroes(totalAmount, numberOfDecimal));
			} else {
				JSONObject resultJsonInt = new JSONObject();
				resultJsonInt.put("ERROR_MESSAGE", "NO PRODUCT RECORD FOUND!");
				resultJsonInt.put("ERROR_CODE", "99");
				jsonArrayErr.add(resultJsonInt);
				jsonArray.add("");
				resultJson.put("invoices", jsonArray);
				resultJson.put("errors", jsonArrayErr);
			}
			resultJson.put("recordsFiltered", listQry.size());
		} catch (Exception e) {
			resultJson.put("SEARCH_DATA", "");
			JSONObject resultJsonInt = new JSONObject();
			resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
			resultJsonInt.put("ERROR_CODE", "98");
			jsonArrayErr.add(resultJsonInt);
			jsonArray.add("");
			resultJson.put("invoices", jsonArray);
			resultJson.put("errors", jsonArrayErr);
			resultJson.put("recordsFiltered", 0);
		}
		return resultJson;
	}*/
	

	private JSONObject getTotalSalesSummaryByInvoiceFromJournal(HttpServletRequest request) {
		JSONObject resultJson = new JSONObject();
		JSONArray jsonArray = new JSONArray();
		JSONArray jsonArrayErr = new JSONArray();
//		BillUtil billUtil = new BillUtil();
		InvoiceUtil movHisUtil       = new InvoiceUtil();
//        DateUtils _dateUtils = new DateUtils();
        ArrayList movQryList  = new ArrayList();
        ArrayList movQryListfilter1  = new ArrayList();
		try {

			String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
			String fromDate = StrUtils.fString(request.getParameter("FROM_DATE")).trim();
			String toDate = StrUtils.fString(request.getParameter("TO_DATE")).trim();
			String period = StrUtils.fString(request.getParameter("PERIOD")).trim();
			String currency = StrUtils.fString((String) request.getSession().getAttribute("BASE_CURRENCY")).trim();
			double totalAmount= 0.0;
			PlantMstDAO plantMstDAO = new PlantMstDAO();
		    String numberOfDecimal = plantMstDAO.getNumberOfDecimal(plant);	
		    movQryList = movHisUtil.getInvoiceSummaryDashboardView(fromDate,toDate,plant,"","",numberOfDecimal);	
			if (movQryList.size() > 0) {
				for (int iCnt =0; iCnt<movQryList.size(); iCnt++){
					Map lineArr = (Map) movQryList.get(iCnt);
					String condition = (String)lineArr.get("CONDITIONS");
					if(!condition.equalsIgnoreCase("")) {
						movQryListfilter1.add(lineArr);
					}
				}
			}
		    
			if(period.equalsIgnoreCase("Today") || period.equalsIgnoreCase("yesterday") || period.equalsIgnoreCase("Last 30 days") || period.equalsIgnoreCase("This Month") || period.equalsIgnoreCase("Last month") || period.equalsIgnoreCase("Last 7 days") || period.equalsIgnoreCase("This week") || period.equalsIgnoreCase("Last 15 days")) {
				SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
				SimpleDateFormat formatter1 = new SimpleDateFormat("dd MMM,yyyy");
				List<Date> getbdates = getbetweendates(fromDate, toDate);
				for(int i=0;i<getbdates.size();i++){
				    Date lDate =(Date)getbdates.get(i);
				    String ds = formatter.format(lDate);    
				    System.out.println(" Date is ..." + ds);
				    double amtadd =0;
				    for (int iCnt =0; iCnt<movQryListfilter1.size(); iCnt++){
						Map lineArr = (Map) movQryListfilter1.get(iCnt);
						String condition = (String)lineArr.get("JOURNAL_DATE");
						if(condition.equalsIgnoreCase(ds)) {
							amtadd = amtadd + Double.valueOf((String)lineArr.get("TOTAL_AMOUNT"));
						}
					}
				    
				    JSONObject resultJsonInt = new JSONObject();
					resultJsonInt.put("INVOICE_DATE", formatter1.format(lDate));
					resultJsonInt.put("TOTAL_PRICE", StrUtils.addZeroes(amtadd, numberOfDecimal));
					totalAmount += amtadd;
					jsonArray.add(resultJsonInt);
				}
				
				JSONObject resultJsonInt = new JSONObject();
				resultJsonInt.put("ERROR_MESSAGE", "NO ERRORS!");
				resultJsonInt.put("ERROR_CODE", "100");
				jsonArrayErr.add(resultJsonInt);
				resultJson.put("invoices", jsonArray);
				resultJson.put("errors", jsonArrayErr);
				resultJson.put("TOTAL_AMOUNT", currency+StrUtils.addZeroes(totalAmount, numberOfDecimal));
			}else if(period.equalsIgnoreCase("This quarter") || period.equalsIgnoreCase("This year") || period.equalsIgnoreCase("Last quarter") || period.equalsIgnoreCase("Last year")){
				SimpleDateFormat formatter2 = new SimpleDateFormat("dd-MMM-yyyy");
				SimpleDateFormat formatter3 = new SimpleDateFormat("MMMM-yyyy");
				SimpleDateFormat formatter4 = new SimpleDateFormat("dd/MM/yyyy");
				Calendar beginCalendar = Calendar.getInstance();
		        Calendar finishCalendar = Calendar.getInstance();
		        beginCalendar.setTime(formatter2.parse(fromDate));
	            finishCalendar.setTime(formatter2.parse(toDate));
	            finishCalendar.add(Calendar.MONTH, 1);
	            while (beginCalendar.before(finishCalendar)) {
	                // add one month to date per loop
	                String Bdate =     formatter3.format(beginCalendar.getTime()).toUpperCase();
	                beginCalendar.set(Calendar.DATE, beginCalendar.getActualMinimum(Calendar.DAY_OF_MONTH));
	                String fdate = formatter2.format(beginCalendar.getTime()).toUpperCase();
	                beginCalendar.set(Calendar.DATE, beginCalendar.getActualMaximum(Calendar.DAY_OF_MONTH));
	                String tdate = formatter2.format(beginCalendar.getTime()).toUpperCase();
	                
	                double amtadd =0;
	                List<Date> getbdates = getbetweendates(fdate, tdate);
					for(int i=0;i<getbdates.size();i++){
					    Date lDate =(Date)getbdates.get(i);
					    String ds = formatter4.format(lDate);    
					    for (int iCnt =0; iCnt<movQryListfilter1.size(); iCnt++){
							Map lineArr = (Map) movQryListfilter1.get(iCnt);
							String condition = (String)lineArr.get("JOURNAL_DATE");
							if(condition.equalsIgnoreCase(ds)) {
								amtadd = amtadd + Double.valueOf((String)lineArr.get("TOTAL_AMOUNT"));
							}
						}
					}
					
					JSONObject resultJsonInt = new JSONObject();
					resultJsonInt.put("INVOICE_DATE",  Bdate);
					resultJsonInt.put("TOTAL_PRICE", StrUtils.addZeroes(amtadd, numberOfDecimal));
					totalAmount += amtadd;
					jsonArray.add(resultJsonInt);
	                
	                beginCalendar.add(Calendar.MONTH, 1);
	            }
	            
	            JSONObject resultJsonInt = new JSONObject();
				resultJsonInt.put("ERROR_MESSAGE", "NO ERRORS!");
				resultJsonInt.put("ERROR_CODE", "100");
				jsonArrayErr.add(resultJsonInt);
				resultJson.put("invoices", jsonArray);
				resultJson.put("errors", jsonArrayErr);
				resultJson.put("TOTAL_AMOUNT", currency+StrUtils.addZeroes(totalAmount, numberOfDecimal));
			}else {
				JSONObject resultJsonInt = new JSONObject();
				resultJsonInt.put("ERROR_MESSAGE", "NO PRODUCT RECORD FOUND!");
				resultJsonInt.put("ERROR_CODE", "99");
				jsonArrayErr.add(resultJsonInt);
				//jsonArray.add("");
				resultJson.put("invoices", jsonArray);
				resultJson.put("errors", jsonArrayErr);
			}
			
			resultJson.put("recordsFiltered", movQryListfilter1.size());
		} catch (Exception e) {
			resultJson.put("SEARCH_DATA", "");
			JSONObject resultJsonInt = new JSONObject();
			resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
			resultJsonInt.put("ERROR_CODE", "98");
			jsonArrayErr.add(resultJsonInt);
			//jsonArray.add("");
			resultJson.put("invoices", jsonArray);
			resultJson.put("errors", jsonArrayErr);
			resultJson.put("recordsFiltered", 0);
		}
		return resultJson;
	}
	
	private JSONObject getTotalIncomeForDashboard(HttpServletRequest request) {
		JSONObject resultJson = new JSONObject();
		JSONArray jsonArray = new JSONArray();
		JSONArray jsonArrayErr = new JSONArray();
		try {
			String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
			String currency = StrUtils.fString((String) request.getSession().getAttribute("BASE_CURRENCY")).trim();
			String fromDate = StrUtils.fString(request.getParameter("FROM_DATE")).trim();
			String toDate = StrUtils.fString(request.getParameter("TO_DATE")).trim();
			PlantMstDAO plantMstDAO = new PlantMstDAO();
		    String numberOfDecimal = plantMstDAO.getNumberOfDecimal(plant);
		    double totalAmount= 0.0;
		    List listQry = "track".equalsIgnoreCase(plant) ? new ArrayList() : new InvoiceUtil().getTotalIncomeForDashboard(plant, fromDate, toDate, numberOfDecimal);
			if (listQry.size() > 0) {
				for (int iCnt = 0; iCnt < listQry.size(); iCnt++) {
//					int iIndex = iCnt + 1;
					HashMap hmItem = (HashMap) listQry.get(iCnt);
					JSONObject resultJsonInt = new JSONObject();
					resultJsonInt.put("ACCOUNTDETAILTYPE", (String) hmItem.get("ACCOUNTDETAILTYPE"));
					resultJsonInt.put("TOTAL_AMOUNT", (String) hmItem.get("TOTAL_AMOUNT"));
					totalAmount += Double.parseDouble((String) hmItem.get("TOTAL_AMOUNT"));
					jsonArray.add(resultJsonInt);
				}
				JSONObject resultJsonInt = new JSONObject();
				resultJsonInt.put("ERROR_MESSAGE", "NO ERRORS!");
				resultJsonInt.put("ERROR_CODE", "100");
				jsonArrayErr.add(resultJsonInt);
				resultJson.put("invoices", jsonArray);
				resultJson.put("errors", jsonArrayErr);
				resultJson.put("TOTAL_AMOUNT", currency+StrUtils.addZeroes(totalAmount, numberOfDecimal));
			} else {
				JSONObject resultJsonInt = new JSONObject();
				resultJsonInt.put("ERROR_MESSAGE", "NO PRODUCT RECORD FOUND!");
				resultJsonInt.put("ERROR_CODE", "99");
				jsonArrayErr.add(resultJsonInt);
				jsonArray.add("");
				resultJson.put("invoices", jsonArray);
				resultJson.put("errors", jsonArrayErr);
			}
			resultJson.put("recordsFiltered", listQry.size());
		} catch (Exception e) {
			resultJson.put("SEARCH_DATA", "");
			JSONObject resultJsonInt = new JSONObject();
			resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
			resultJsonInt.put("ERROR_CODE", "98");
			jsonArrayErr.add(resultJsonInt);
			jsonArray.add("");
			resultJson.put("invoices", jsonArray);
			resultJson.put("errors", jsonArrayErr);
			resultJson.put("recordsFiltered", 0);
		}
		return resultJson;
	}
	
	private JSONObject getTotalIncomeFromJournalForDashboard(HttpServletRequest request) {
		JSONObject resultJson = new JSONObject();
		JSONArray jsonArray = new JSONArray();
		JSONArray jsonArrayErr = new JSONArray();
		try {
			String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
			String currency = StrUtils.fString((String) request.getSession().getAttribute("BASE_CURRENCY")).trim();
			String fromDate = StrUtils.fString(request.getParameter("FROM_DATE")).trim();
			String toDate = StrUtils.fString(request.getParameter("TO_DATE")).trim();
			PlantMstDAO plantMstDAO = new PlantMstDAO();
		    String numberOfDecimal = plantMstDAO.getNumberOfDecimal(plant);
		    double totalAmount= 0.0;
		    List listQry = "track".equalsIgnoreCase(plant) ? new ArrayList() : new InvoiceUtil().getTotalIncomeFromJournlForDashboard(plant, fromDate, toDate, numberOfDecimal);
			if (listQry.size() > 0) {
				for (int iCnt = 0; iCnt < listQry.size(); iCnt++) {
//					int iIndex = iCnt + 1;
					HashMap hmItem = (HashMap) listQry.get(iCnt);
					double tamount = Double.valueOf((String) hmItem.get("TOTAL_AMOUNT"));
					if(tamount != 0) {
						JSONObject resultJsonInt = new JSONObject();
						resultJsonInt.put("ACCOUNTDETAILTYPE", (String) hmItem.get("ACCOUNT_NAME"));
						resultJsonInt.put("TOTAL_AMOUNT", (String) hmItem.get("TOTAL_AMOUNT"));
						totalAmount += Double.parseDouble((String) hmItem.get("TOTAL_AMOUNT"));
						jsonArray.add(resultJsonInt);
					}
				}
				JSONObject resultJsonInt = new JSONObject();
				resultJsonInt.put("ERROR_MESSAGE", "NO ERRORS!");
				resultJsonInt.put("ERROR_CODE", "100");
				jsonArrayErr.add(resultJsonInt);
				resultJson.put("invoices", jsonArray);
				resultJson.put("errors", jsonArrayErr);
				resultJson.put("TOTAL_AMOUNT", currency+StrUtils.addZeroes(totalAmount, numberOfDecimal));
			} else {
				JSONObject resultJsonInt = new JSONObject();
				resultJsonInt.put("ERROR_MESSAGE", "NO PRODUCT RECORD FOUND!");
				resultJsonInt.put("ERROR_CODE", "99");
				jsonArrayErr.add(resultJsonInt);
				jsonArray.add("");
				resultJson.put("invoices", jsonArray);
				resultJson.put("errors", jsonArrayErr);
			}
			resultJson.put("recordsFiltered", listQry.size());
		} catch (Exception e) {
			resultJson.put("SEARCH_DATA", "");
			JSONObject resultJsonInt = new JSONObject();
			resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
			resultJsonInt.put("ERROR_CODE", "98");
			jsonArrayErr.add(resultJsonInt);
			jsonArray.add("");
			resultJson.put("invoices", jsonArray);
			resultJson.put("errors", jsonArrayErr);
			resultJson.put("recordsFiltered", 0);
		}
		return resultJson;
	}
	
	private JSONObject getTotalExpenseForDashboard(HttpServletRequest request) {
		JSONObject resultJson = new JSONObject();
		JSONArray jsonArray = new JSONArray();
		JSONArray jsonArrayErr = new JSONArray();
		try {
			String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
			String currency = StrUtils.fString((String) request.getSession().getAttribute("BASE_CURRENCY")).trim();
			String fromDate = StrUtils.fString(request.getParameter("FROM_DATE")).trim();
			String toDate = StrUtils.fString(request.getParameter("TO_DATE")).trim();
			double totalAmount= 0.0;
			PlantMstDAO plantMstDAO = new PlantMstDAO();
		    String numberOfDecimal = plantMstDAO.getNumberOfDecimal(plant);
		    List listQry = "track".equalsIgnoreCase(plant) ? new ArrayList() : new BillUtil().getTotalExpenseForDashboard(plant, fromDate, toDate, numberOfDecimal);
			if (listQry.size() > 0) {
				for (int iCnt = 0; iCnt < listQry.size(); iCnt++) {
//					int iIndex = iCnt + 1;
					HashMap hmItem = (HashMap) listQry.get(iCnt);
					JSONObject resultJsonInt = new JSONObject();
					resultJsonInt.put("ACCOUNTDETAILTYPE", (String) hmItem.get("ACCOUNTDETAILTYPE"));
					resultJsonInt.put("TOTAL_AMOUNT", (String) hmItem.get("TOTAL_AMOUNT"));
					totalAmount += Double.parseDouble((String) hmItem.get("TOTAL_AMOUNT"));
					jsonArray.add(resultJsonInt);
				}
				JSONObject resultJsonInt = new JSONObject();
				resultJsonInt.put("ERROR_MESSAGE", "NO ERRORS!");
				resultJsonInt.put("ERROR_CODE", "100");
				jsonArrayErr.add(resultJsonInt);
				resultJson.put("bills", jsonArray);
				resultJson.put("errors", jsonArrayErr);
				resultJson.put("TOTAL_AMOUNT", currency+StrUtils.addZeroes(totalAmount, numberOfDecimal));
			} else {
				JSONObject resultJsonInt = new JSONObject();
				resultJsonInt.put("ERROR_MESSAGE", "NO PRODUCT RECORD FOUND!");
				resultJsonInt.put("ERROR_CODE", "99");
				jsonArrayErr.add(resultJsonInt);
				jsonArray.add("");
				resultJson.put("bills", jsonArray);
				resultJson.put("errors", jsonArrayErr);
			}
			resultJson.put("recordsFiltered", listQry.size());
		} catch (Exception e) {
			resultJson.put("SEARCH_DATA", "");
			JSONObject resultJsonInt = new JSONObject();
			resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
			resultJsonInt.put("ERROR_CODE", "98");
			jsonArrayErr.add(resultJsonInt);
			jsonArray.add("");
			resultJson.put("bills", jsonArray);
			resultJson.put("errors", jsonArrayErr);
			resultJson.put("recordsFiltered", 0);
		}
		return resultJson;
	}
	
	private JSONObject getTotalExpenseForDashboardFromJournal(HttpServletRequest request) {
		JSONObject resultJson = new JSONObject();
		JSONArray jsonArray = new JSONArray();
		JSONArray jsonArrayErr = new JSONArray();
		try {
			String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
			String currency = StrUtils.fString((String) request.getSession().getAttribute("BASE_CURRENCY")).trim();
			String fromDate = StrUtils.fString(request.getParameter("FROM_DATE")).trim();
			String toDate = StrUtils.fString(request.getParameter("TO_DATE")).trim();
			double totalAmount= 0.0;
			PlantMstDAO plantMstDAO = new PlantMstDAO();
		    String numberOfDecimal = plantMstDAO.getNumberOfDecimal(plant);
		    List listQry = "track".equalsIgnoreCase(plant) ? new ArrayList() : new BillUtil().getTotalExpenseForDashboardFromJournal(plant, fromDate, toDate, numberOfDecimal);
			if (listQry.size() > 0) {
				for (int iCnt = 0; iCnt < listQry.size(); iCnt++) {
//					int iIndex = iCnt + 1;
					HashMap hmItem = (HashMap) listQry.get(iCnt);
					double tamount = Double.valueOf((String) hmItem.get("TOTAL_AMOUNT"));
					if(tamount != 0) {
						JSONObject resultJsonInt = new JSONObject();
						resultJsonInt.put("ACCOUNTDETAILTYPE", (String) hmItem.get("ACCOUNT_NAME"));
						resultJsonInt.put("TOTAL_AMOUNT", (String) hmItem.get("TOTAL_AMOUNT"));
						totalAmount += Double.parseDouble((String) hmItem.get("TOTAL_AMOUNT"));
						jsonArray.add(resultJsonInt);
					}
					
				}
				JSONObject resultJsonInt = new JSONObject();
				resultJsonInt.put("ERROR_MESSAGE", "NO ERRORS!");
				resultJsonInt.put("ERROR_CODE", "100");
				jsonArrayErr.add(resultJsonInt);
				resultJson.put("bills", jsonArray);
				resultJson.put("errors", jsonArrayErr);
				resultJson.put("TOTAL_AMOUNT", currency+StrUtils.addZeroes(totalAmount, numberOfDecimal));
			} else {
				JSONObject resultJsonInt = new JSONObject();
				resultJsonInt.put("ERROR_MESSAGE", "NO PRODUCT RECORD FOUND!");
				resultJsonInt.put("ERROR_CODE", "99");
				jsonArrayErr.add(resultJsonInt);
				jsonArray.add("");
				resultJson.put("bills", jsonArray);
				resultJson.put("errors", jsonArrayErr);
			}
			resultJson.put("recordsFiltered", listQry.size());
		} catch (Exception e) {
			resultJson.put("SEARCH_DATA", "");
			JSONObject resultJsonInt = new JSONObject();
			resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
			resultJsonInt.put("ERROR_CODE", "98");
			jsonArrayErr.add(resultJsonInt);
			jsonArray.add("");
			resultJson.put("bills", jsonArray);
			resultJson.put("errors", jsonArrayErr);
			resultJson.put("recordsFiltered", 0);
		}
		return resultJson;
	}
	
	private JSONObject getTotalPaymentMadeForDashboardFromJournal(HttpServletRequest request) {
		JSONObject resultJson = new JSONObject();
		JSONArray jsonArray = new JSONArray();
		JSONArray jsonArrayErr = new JSONArray();
		try {
			String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
			String currency = StrUtils.fString((String) request.getSession().getAttribute("BASE_CURRENCY")).trim();
			String fromDate = StrUtils.fString(request.getParameter("FROM_DATE")).trim();
			String toDate = StrUtils.fString(request.getParameter("TO_DATE")).trim();
			double totalAmount= 0.0;
			PlantMstDAO plantMstDAO = new PlantMstDAO();
		    String numberOfDecimal = plantMstDAO.getNumberOfDecimal(plant);
		    List listQry = "track".equalsIgnoreCase(plant) ? new ArrayList() : new BillUtil().getTotalPaymentMadeForDashboardFromJournal(plant, fromDate, toDate, numberOfDecimal);
			if (listQry.size() > 0) {
				for (int iCnt = 0; iCnt < listQry.size(); iCnt++) {
//					int iIndex = iCnt + 1;
					HashMap hmItem = (HashMap) listQry.get(iCnt);
					double tamount = Double.parseDouble((String) hmItem.get("TOTAL_AMOUNT"));
					if(tamount != 0) {
						JSONObject resultJsonInt = new JSONObject();
						resultJsonInt.put("ACCOUNTDETAILTYPE", (String) hmItem.get("ACCOUNT_NAME"));
						resultJsonInt.put("TOTAL_AMOUNT", (String) hmItem.get("TOTAL_AMOUNT"));
						totalAmount += Double.parseDouble((String) hmItem.get("TOTAL_AMOUNT"));
						jsonArray.add(resultJsonInt);
					}
					
				}
				JSONObject resultJsonInt = new JSONObject();
				resultJsonInt.put("ERROR_MESSAGE", "NO ERRORS!");
				resultJsonInt.put("ERROR_CODE", "100");
				jsonArrayErr.add(resultJsonInt);
				resultJson.put("payment", jsonArray);
				resultJson.put("errors", jsonArrayErr);
				resultJson.put("TOTAL_AMOUNT", currency+StrUtils.addZeroes(totalAmount, numberOfDecimal));
			} else {
				JSONObject resultJsonInt = new JSONObject();
				resultJsonInt.put("ERROR_MESSAGE", "NO PRODUCT RECORD FOUND!");
				resultJsonInt.put("ERROR_CODE", "99");
				jsonArrayErr.add(resultJsonInt);
				jsonArray.add("");
				resultJson.put("payment", jsonArray);
				resultJson.put("errors", jsonArrayErr);
			}
			resultJson.put("recordsFiltered", listQry.size());
		} catch (Exception e) {
			resultJson.put("SEARCH_DATA", "");
			JSONObject resultJsonInt = new JSONObject();
			resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
			resultJsonInt.put("ERROR_CODE", "98");
			jsonArrayErr.add(resultJsonInt);
			jsonArray.add("");
			resultJson.put("payment", jsonArray);
			resultJson.put("errors", jsonArrayErr);
			resultJson.put("recordsFiltered", 0);
		}
		return resultJson;
	}
	
	private JSONObject getTotalPaymentReceiptForDashboardFromJournal(HttpServletRequest request) {
		JSONObject resultJson = new JSONObject();
		JSONArray jsonArray = new JSONArray();
		JSONArray jsonArrayErr = new JSONArray();
		try {
			String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
			String currency = StrUtils.fString((String) request.getSession().getAttribute("BASE_CURRENCY")).trim();
			String fromDate = StrUtils.fString(request.getParameter("FROM_DATE")).trim();
			String toDate = StrUtils.fString(request.getParameter("TO_DATE")).trim();
			double totalAmount= 0.0;
			PlantMstDAO plantMstDAO = new PlantMstDAO();
		    String numberOfDecimal = plantMstDAO.getNumberOfDecimal(plant);
		    List listQry = "track".equalsIgnoreCase(plant) ? new ArrayList() : new BillUtil().getTotalPaymentReceiptForDashboardFromJournal(plant, fromDate, toDate, numberOfDecimal);
			if (listQry.size() > 0) {
				for (int iCnt = 0; iCnt < listQry.size(); iCnt++) {
//					int iIndex = iCnt + 1;
					HashMap hmItem = (HashMap) listQry.get(iCnt);
					double tamount = Double.parseDouble((String) hmItem.get("TOTAL_AMOUNT"));
					if(tamount != 0) {
						JSONObject resultJsonInt = new JSONObject();
						resultJsonInt.put("ACCOUNTDETAILTYPE", (String) hmItem.get("ACCOUNT_NAME"));
						resultJsonInt.put("TOTAL_AMOUNT", (String) hmItem.get("TOTAL_AMOUNT"));
						totalAmount += Double.parseDouble((String) hmItem.get("TOTAL_AMOUNT"));
						jsonArray.add(resultJsonInt);
					}
					
				}
				JSONObject resultJsonInt = new JSONObject();
				resultJsonInt.put("ERROR_MESSAGE", "NO ERRORS!");
				resultJsonInt.put("ERROR_CODE", "100");
				jsonArrayErr.add(resultJsonInt);
				resultJson.put("payment", jsonArray);
				resultJson.put("errors", jsonArrayErr);
				resultJson.put("TOTAL_AMOUNT", currency+StrUtils.addZeroes(totalAmount, numberOfDecimal));
			} else {
				JSONObject resultJsonInt = new JSONObject();
				resultJsonInt.put("ERROR_MESSAGE", "NO PRODUCT RECORD FOUND!");
				resultJsonInt.put("ERROR_CODE", "99");
				jsonArrayErr.add(resultJsonInt);
				//jsonArray.add("");
				resultJson.put("payment", jsonArray);
				resultJson.put("errors", jsonArrayErr);
			}
			resultJson.put("recordsFiltered", listQry.size());
		} catch (Exception e) {
			resultJson.put("SEARCH_DATA", "");
			JSONObject resultJsonInt = new JSONObject();
			resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
			resultJsonInt.put("ERROR_CODE", "98");
			jsonArrayErr.add(resultJsonInt);
			//jsonArray.add("");
			resultJson.put("payment", jsonArray);
			resultJson.put("errors", jsonArrayErr);
			resultJson.put("recordsFiltered", 0);
		}
		return resultJson;
	}
	
	private JSONObject getTotalIncomeSummaryByInvoice(HttpServletRequest request) {
		JSONObject resultJson = new JSONObject();
		JSONArray jsonArray = new JSONArray();
		JSONArray jsonArrayErr = new JSONArray();
		try {

			String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
			String fromDate = StrUtils.fString(request.getParameter("FROM_DATE")).trim();
			String toDate = StrUtils.fString(request.getParameter("TO_DATE")).trim();
			String period = StrUtils.fString(request.getParameter("PERIOD")).trim();
			PlantMstDAO plantMstDAO = new PlantMstDAO();
		    String numberOfDecimal = plantMstDAO.getNumberOfDecimal(plant);
		    List listQry = "track".equalsIgnoreCase(plant) ? new ArrayList() : new InvoiceUtil().getTotalIncomeSummaryByInvoice(plant, period, fromDate, toDate, numberOfDecimal);
			if (listQry.size() > 0) {
				for (int iCnt = 0; iCnt < listQry.size(); iCnt++) {
//					int iIndex = iCnt + 1;
					HashMap hmItem = (HashMap) listQry.get(iCnt);
					JSONObject resultJsonInt = new JSONObject();
					resultJsonInt.put("INVOICE_DATE", (String) hmItem.get("INVOICE_DATE"));
					resultJsonInt.put("TOTAL_PRICE", (String) hmItem.get("TOTAL_PRICE"));
					jsonArray.add(resultJsonInt);
				}
				JSONObject resultJsonInt = new JSONObject();
				resultJsonInt.put("ERROR_MESSAGE", "NO ERRORS!");
				resultJsonInt.put("ERROR_CODE", "100");
				jsonArrayErr.add(resultJsonInt);
				resultJson.put("invoices", jsonArray);
				resultJson.put("errors", jsonArrayErr);
			} else {
				JSONObject resultJsonInt = new JSONObject();
				resultJsonInt.put("ERROR_MESSAGE", "NO PRODUCT RECORD FOUND!");
				resultJsonInt.put("ERROR_CODE", "99");
				jsonArrayErr.add(resultJsonInt);
				jsonArray.add("");
				resultJson.put("invoices", jsonArray);
				resultJson.put("errors", jsonArrayErr);
			}
			resultJson.put("recordsFiltered", listQry.size());
		} catch (Exception e) {
			resultJson.put("SEARCH_DATA", "");
			JSONObject resultJsonInt = new JSONObject();
			resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
			resultJsonInt.put("ERROR_CODE", "98");
			jsonArrayErr.add(resultJsonInt);
			jsonArray.add("");
			resultJson.put("invoices", jsonArray);
			resultJson.put("errors", jsonArrayErr);
			resultJson.put("recordsFiltered", 0);
		}
		return resultJson;
	}
	
	private JSONObject getTotalIncomeSummaryByInvoiceFromJournal(HttpServletRequest request) {
		JSONObject resultJson = new JSONObject();
		JSONArray jsonArray = new JSONArray();
		JSONArray jsonArrayErr = new JSONArray();
		try {

			String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
			String fromDate = StrUtils.fString(request.getParameter("FROM_DATE")).trim();
			String toDate = StrUtils.fString(request.getParameter("TO_DATE")).trim();
			String period = StrUtils.fString(request.getParameter("PERIOD")).trim();
			PlantMstDAO plantMstDAO = new PlantMstDAO();
		    String numberOfDecimal = plantMstDAO.getNumberOfDecimal(plant);
		    List listQry = "track".equalsIgnoreCase(plant) ? new ArrayList() : new InvoiceUtil().getTotalIncomeSummaryByInvoiceFromJournal(plant, period, fromDate, toDate, numberOfDecimal);
			if (listQry.size() > 0) {
				for (int iCnt = 0; iCnt < listQry.size(); iCnt++) {
//					int iIndex = iCnt + 1;
					HashMap hmItem = (HashMap) listQry.get(iCnt);
					JSONObject resultJsonInt = new JSONObject();
					resultJsonInt.put("INVOICE_DATE", (String) hmItem.get("INVOICE_DATE"));
					resultJsonInt.put("TOTAL_PRICE", (String) hmItem.get("TOTAL_PRICE"));
					jsonArray.add(resultJsonInt);
				}
				JSONObject resultJsonInt = new JSONObject();
				resultJsonInt.put("ERROR_MESSAGE", "NO ERRORS!");
				resultJsonInt.put("ERROR_CODE", "100");
				jsonArrayErr.add(resultJsonInt);
				resultJson.put("invoices", jsonArray);
				resultJson.put("errors", jsonArrayErr);
			} else {
				JSONObject resultJsonInt = new JSONObject();
				resultJsonInt.put("ERROR_MESSAGE", "NO PRODUCT RECORD FOUND!");
				resultJsonInt.put("ERROR_CODE", "99");
				jsonArrayErr.add(resultJsonInt);
				jsonArray.add("");
				resultJson.put("invoices", jsonArray);
				resultJson.put("errors", jsonArrayErr);
			}
			resultJson.put("recordsFiltered", listQry.size());
		} catch (Exception e) {
			resultJson.put("SEARCH_DATA", "");
			JSONObject resultJsonInt = new JSONObject();
			resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
			resultJsonInt.put("ERROR_CODE", "98");
			jsonArrayErr.add(resultJsonInt);
			jsonArray.add("");
			resultJson.put("invoices", jsonArray);
			resultJson.put("errors", jsonArrayErr);
			resultJson.put("recordsFiltered", 0);
		}
		return resultJson;
	}
	
	private JSONObject getTotalExpenseSummaryByBill(HttpServletRequest request) {
		JSONObject resultJson = new JSONObject();
		JSONArray jsonArray = new JSONArray();
		JSONArray jsonArrayErr = new JSONArray();
		try {

			String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
			String fromDate = StrUtils.fString(request.getParameter("FROM_DATE")).trim();
			String toDate = StrUtils.fString(request.getParameter("TO_DATE")).trim();
			String period = StrUtils.fString(request.getParameter("PERIOD")).trim();
			PlantMstDAO plantMstDAO = new PlantMstDAO();
		    String numberOfDecimal = plantMstDAO.getNumberOfDecimal(plant);
		    List listQry = "track".equalsIgnoreCase(plant) ? new ArrayList() : new BillUtil().getTotalExpenseSummaryByBill(plant, period, fromDate, toDate, numberOfDecimal);
			if (listQry.size() > 0) {
				for (int iCnt = 0; iCnt < listQry.size(); iCnt++) {
//					int iIndex = iCnt + 1;
					HashMap hmItem = (HashMap) listQry.get(iCnt);
					JSONObject resultJsonInt = new JSONObject();
					resultJsonInt.put("BILL_DATE", (String) hmItem.get("BILL_DATE"));
					resultJsonInt.put("TOTAL_COST", (String) hmItem.get("TOTAL_COST"));
					jsonArray.add(resultJsonInt);
				}
				JSONObject resultJsonInt = new JSONObject();
				resultJsonInt.put("ERROR_MESSAGE", "NO ERRORS!");
				resultJsonInt.put("ERROR_CODE", "100");
				jsonArrayErr.add(resultJsonInt);
				resultJson.put("bills", jsonArray);
				resultJson.put("errors", jsonArrayErr);
			} else {
				JSONObject resultJsonInt = new JSONObject();
				resultJsonInt.put("ERROR_MESSAGE", "NO PRODUCT RECORD FOUND!");
				resultJsonInt.put("ERROR_CODE", "99");
				jsonArrayErr.add(resultJsonInt);
				jsonArray.add("");
				resultJson.put("bills", jsonArray);
				resultJson.put("errors", jsonArrayErr);
			}
			resultJson.put("recordsFiltered", listQry.size());
		} catch (Exception e) {
			resultJson.put("SEARCH_DATA", "");
			JSONObject resultJsonInt = new JSONObject();
			resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
			resultJsonInt.put("ERROR_CODE", "98");
			jsonArrayErr.add(resultJsonInt);
			jsonArray.add("");
			resultJson.put("bills", jsonArray);
			resultJson.put("errors", jsonArrayErr);
			resultJson.put("recordsFiltered", 0);
		}
		return resultJson;
	}
	
	private JSONObject getTotalExpenseSummaryByBillFromJourny(HttpServletRequest request) {
		JSONObject resultJson = new JSONObject();
		JSONArray jsonArray = new JSONArray();
		JSONArray jsonArrayErr = new JSONArray();
		try {

			String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
			String fromDate = StrUtils.fString(request.getParameter("FROM_DATE")).trim();
			String toDate = StrUtils.fString(request.getParameter("TO_DATE")).trim();
			String period = StrUtils.fString(request.getParameter("PERIOD")).trim();
			PlantMstDAO plantMstDAO = new PlantMstDAO();
		    String numberOfDecimal = plantMstDAO.getNumberOfDecimal(plant);
		    List listQry = "track".equalsIgnoreCase(plant) ? new ArrayList() : new BillUtil().getTotalExpenseSummaryByBillFromJourny(plant, period, fromDate, toDate, numberOfDecimal);
			if (listQry.size() > 0) {
				for (int iCnt = 0; iCnt < listQry.size(); iCnt++) {
//					int iIndex = iCnt + 1;
					HashMap hmItem = (HashMap) listQry.get(iCnt);
					JSONObject resultJsonInt = new JSONObject();
					resultJsonInt.put("BILL_DATE", (String) hmItem.get("BILL_DATE"));
					resultJsonInt.put("TOTAL_COST", (String) hmItem.get("TOTAL_COST"));
					jsonArray.add(resultJsonInt);
				}
				JSONObject resultJsonInt = new JSONObject();
				resultJsonInt.put("ERROR_MESSAGE", "NO ERRORS!");
				resultJsonInt.put("ERROR_CODE", "100");
				jsonArrayErr.add(resultJsonInt);
				resultJson.put("bills", jsonArray);
				resultJson.put("errors", jsonArrayErr);
			} else {
				JSONObject resultJsonInt = new JSONObject();
				resultJsonInt.put("ERROR_MESSAGE", "NO PRODUCT RECORD FOUND!");
				resultJsonInt.put("ERROR_CODE", "99");
				jsonArrayErr.add(resultJsonInt);
				jsonArray.add("");
				resultJson.put("bills", jsonArray);
				resultJson.put("errors", jsonArrayErr);
			}
			resultJson.put("recordsFiltered", listQry.size());
		} catch (Exception e) {
			resultJson.put("SEARCH_DATA", "");
			JSONObject resultJsonInt = new JSONObject();
			resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
			resultJsonInt.put("ERROR_CODE", "98");
			jsonArrayErr.add(resultJsonInt);
			jsonArray.add("");
			resultJson.put("bills", jsonArray);
			resultJson.put("errors", jsonArrayErr);
			resultJson.put("recordsFiltered", 0);
		}
		return resultJson;
	}
	
	private JSONObject getTotalPaymentMadeSummaryFromJourny(HttpServletRequest request) {
		JSONObject resultJson = new JSONObject();
		JSONArray jsonArray = new JSONArray();
		JSONArray jsonArrayErr = new JSONArray();
		try {

			String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
			String fromDate = StrUtils.fString(request.getParameter("FROM_DATE")).trim();
			String toDate = StrUtils.fString(request.getParameter("TO_DATE")).trim();
			String period = StrUtils.fString(request.getParameter("PERIOD")).trim();
			PlantMstDAO plantMstDAO = new PlantMstDAO();
		    String numberOfDecimal = plantMstDAO.getNumberOfDecimal(plant);
		    List listQry = "track".equalsIgnoreCase(plant) ? new ArrayList() : new BillUtil().getTotalPaymentMadeSummaryFromJourny(plant, period, fromDate, toDate, numberOfDecimal);
			if (listQry.size() > 0) {
				for (int iCnt = 0; iCnt < listQry.size(); iCnt++) {
//					int iIndex = iCnt + 1;
					HashMap hmItem = (HashMap) listQry.get(iCnt);
					JSONObject resultJsonInt = new JSONObject();
					resultJsonInt.put("BILL_DATE", (String) hmItem.get("BILL_DATE"));
					resultJsonInt.put("TOTAL_COST", (String) hmItem.get("TOTAL_COST"));
					jsonArray.add(resultJsonInt);
				}
				JSONObject resultJsonInt = new JSONObject();
				resultJsonInt.put("ERROR_MESSAGE", "NO ERRORS!");
				resultJsonInt.put("ERROR_CODE", "100");
				jsonArrayErr.add(resultJsonInt);
				resultJson.put("payment", jsonArray);
				resultJson.put("errors", jsonArrayErr);
			} else {
				JSONObject resultJsonInt = new JSONObject();
				resultJsonInt.put("ERROR_MESSAGE", "NO PRODUCT RECORD FOUND!");
				resultJsonInt.put("ERROR_CODE", "99");
				jsonArrayErr.add(resultJsonInt);
				jsonArray.add("");
				resultJson.put("payment", jsonArray);
				resultJson.put("errors", jsonArrayErr);
			}
			resultJson.put("recordsFiltered", listQry.size());
		} catch (Exception e) {
			resultJson.put("SEARCH_DATA", "");
			JSONObject resultJsonInt = new JSONObject();
			resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
			resultJsonInt.put("ERROR_CODE", "98");
			jsonArrayErr.add(resultJsonInt);
			jsonArray.add("");
			resultJson.put("payment", jsonArray);
			resultJson.put("errors", jsonArrayErr);
			resultJson.put("recordsFiltered", 0);
		}
		return resultJson;
	}
	
	private JSONObject getTotalPaymentReceiptSummaryFromJourny(HttpServletRequest request) {
		JSONObject resultJson = new JSONObject();
		JSONArray jsonArray = new JSONArray();
		JSONArray jsonArrayErr = new JSONArray();
		try {

			String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
			String fromDate = StrUtils.fString(request.getParameter("FROM_DATE")).trim();
			String toDate = StrUtils.fString(request.getParameter("TO_DATE")).trim();
			String period = StrUtils.fString(request.getParameter("PERIOD")).trim();
			PlantMstDAO plantMstDAO = new PlantMstDAO();
		    String numberOfDecimal = plantMstDAO.getNumberOfDecimal(plant);
		    List listQry = "track".equalsIgnoreCase(plant) ? new ArrayList() : new BillUtil().getTotalPaymentReceiptSummaryFromJourny(plant, period, fromDate, toDate, numberOfDecimal);
			if (listQry.size() > 0) {
				for (int iCnt = 0; iCnt < listQry.size(); iCnt++) {
//					int iIndex = iCnt + 1;
					HashMap hmItem = (HashMap) listQry.get(iCnt);
					JSONObject resultJsonInt = new JSONObject();
					resultJsonInt.put("BILL_DATE", (String) hmItem.get("BILL_DATE"));
					resultJsonInt.put("TOTAL_COST", (String) hmItem.get("TOTAL_COST"));
					jsonArray.add(resultJsonInt);
				}
				JSONObject resultJsonInt = new JSONObject();
				resultJsonInt.put("ERROR_MESSAGE", "NO ERRORS!");
				resultJsonInt.put("ERROR_CODE", "100");
				jsonArrayErr.add(resultJsonInt);
				resultJson.put("payment", jsonArray);
				resultJson.put("errors", jsonArrayErr);
			} else {
				JSONObject resultJsonInt = new JSONObject();
				resultJsonInt.put("ERROR_MESSAGE", "NO PRODUCT RECORD FOUND!");
				resultJsonInt.put("ERROR_CODE", "99");
				jsonArrayErr.add(resultJsonInt);
				jsonArray.add("");
				resultJson.put("payment", jsonArray);
				resultJson.put("errors", jsonArrayErr);
			}
			resultJson.put("recordsFiltered", listQry.size());
		} catch (Exception e) {
			resultJson.put("SEARCH_DATA", "");
			JSONObject resultJsonInt = new JSONObject();
			resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
			resultJsonInt.put("ERROR_CODE", "98");
			jsonArrayErr.add(resultJsonInt);
			jsonArray.add("");
			resultJson.put("payment", jsonArray);
			resultJson.put("errors", jsonArrayErr);
			resultJson.put("recordsFiltered", 0);
		}
		return resultJson;
	}
	
	private JSONObject getPaymentPdcForDashboard(HttpServletRequest request) {
		JSONObject resultJson = new JSONObject();
		JSONArray jsonArray = new JSONArray();
		JSONArray jsonArrayErr = new JSONArray();
		try {
			String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
			String fromDate = StrUtils.fString(request.getParameter("FROM_DATE")).trim();
			String toDate = StrUtils.fString(request.getParameter("TO_DATE")).trim();
			String vname = StrUtils.fString(request.getParameter("VNAME")).trim();
			PlantMstDAO plantMstDAO = new PlantMstDAO();
		    String numberOfDecimal = plantMstDAO.getNumberOfDecimal(plant);
			List listQry = "track".equalsIgnoreCase(plant) ? new ArrayList() : new BillPaymentDAO().getPaymentPdcForDashboard(plant, fromDate, toDate, numberOfDecimal);
			if (listQry.size() > 0) {
				if(vname.length()>0) {
					for (int iCnt = 0; iCnt < listQry.size(); iCnt++) {
//						int iIndex = iCnt + 1;
						HashMap hmItem = (HashMap) listQry.get(iCnt);
						if(vname.equalsIgnoreCase((String) hmItem.get("SUPPLIER"))) {
							JSONObject resultJsonInt = new JSONObject();
							resultJsonInt.put("SNO", (String) hmItem.get("PAYMENTID"));
							resultJsonInt.put("PAYMENT_DATE", (String) hmItem.get("PAYMENT_DATE"));
							resultJsonInt.put("SUPPLIER", (String) hmItem.get("SUPPLIER"));
							resultJsonInt.put("BANK_BRANCH", (String) hmItem.get("BANK_BRANCH"));
							resultJsonInt.put("CHECQUE_NO", (String) hmItem.get("CHECQUE_NO"));
							resultJsonInt.put("CHEQUE_DATE", (String) hmItem.get("CHEQUE_DATE"));
							resultJsonInt.put("CHEQUE_REVERSAL_DATE", (String) hmItem.get("CHEQUE_REVERSAL_DATE"));
							resultJsonInt.put("CHEQUE_AMOUNT", (String) hmItem.get("CHEQUE_AMOUNT"));
							jsonArray.add(resultJsonInt);
						}
					}
				}else {
					for (int iCnt = 0; iCnt < listQry.size(); iCnt++) {
//						int iIndex = iCnt + 1;
						HashMap hmItem = (HashMap) listQry.get(iCnt);
						JSONObject resultJsonInt = new JSONObject();
						resultJsonInt.put("SNO", (String) hmItem.get("PAYMENTID"));
						resultJsonInt.put("PAYMENT_DATE", (String) hmItem.get("PAYMENT_DATE"));
						resultJsonInt.put("SUPPLIER", (String) hmItem.get("SUPPLIER"));
						resultJsonInt.put("BANK_BRANCH", (String) hmItem.get("BANK_BRANCH"));
						resultJsonInt.put("CHECQUE_NO", (String) hmItem.get("CHECQUE_NO"));
						resultJsonInt.put("CHEQUE_DATE", (String) hmItem.get("CHEQUE_DATE"));
						resultJsonInt.put("CHEQUE_REVERSAL_DATE", (String) hmItem.get("CHEQUE_REVERSAL_DATE"));
						resultJsonInt.put("CHEQUE_AMOUNT", (String) hmItem.get("CHEQUE_AMOUNT"));
						jsonArray.add(resultJsonInt);
					}
				}
				
				JSONObject resultJsonInt = new JSONObject();
				resultJsonInt.put("ERROR_MESSAGE", "NO ERRORS!");
				resultJsonInt.put("ERROR_CODE", "100");
				jsonArrayErr.add(resultJsonInt);
				if(jsonArray.size()==0)
					jsonArray.add("");
				resultJson.put("payments", jsonArray);
				resultJson.put("errors", jsonArrayErr);
			} else {
				JSONObject resultJsonInt = new JSONObject();
				resultJsonInt.put("ERROR_MESSAGE", "NO PRODUCT RECORD FOUND!");
				resultJsonInt.put("ERROR_CODE", "99");
				jsonArrayErr.add(resultJsonInt);
				jsonArray.add("");
				resultJson.put("payments", jsonArray);
				resultJson.put("errors", jsonArrayErr);
			}
			resultJson.put("recordsFiltered", listQry.size());
		} catch (Exception e) {
			resultJson.put("SEARCH_DATA", "");
			JSONObject resultJsonInt = new JSONObject();
			resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
			resultJsonInt.put("ERROR_CODE", "98");
			jsonArrayErr.add(resultJsonInt);
			jsonArray.add("");
			resultJson.put("payments", jsonArray);
			resultJson.put("errors", jsonArrayErr);
			resultJson.put("recordsFiltered", 0);
		}
		return resultJson;
	}
	
	private JSONObject getPaymentRecvPdcForDashboard(HttpServletRequest request) {
		JSONObject resultJson = new JSONObject();
		JSONArray jsonArray = new JSONArray();
		JSONArray jsonArrayErr = new JSONArray();
		try {
			String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
			String fromDate = StrUtils.fString(request.getParameter("FROM_DATE")).trim();
			String toDate = StrUtils.fString(request.getParameter("TO_DATE")).trim();
			String cname = StrUtils.fString(request.getParameter("CNAME")).trim();
			PlantMstDAO plantMstDAO = new PlantMstDAO();
		    String numberOfDecimal = plantMstDAO.getNumberOfDecimal(plant);
			List listQry = "track".equalsIgnoreCase(plant) ? new ArrayList() : new InvoicePaymentDAO().getPaymentRecvPdcForDashboard(plant, fromDate, toDate, numberOfDecimal);
			if (listQry.size() > 0) {
				if(cname.length()>0) {
					for (int iCnt = 0; iCnt < listQry.size(); iCnt++) {
//						int iIndex = iCnt + 1;
						HashMap hmItem = (HashMap) listQry.get(iCnt);
						if(cname.equalsIgnoreCase((String) hmItem.get("CUSTOMER"))) {
							JSONObject resultJsonInt = new JSONObject();
							resultJsonInt.put("SNO", (String) hmItem.get("RECEIVEID"));
							resultJsonInt.put("PAYMENT_DATE", (String) hmItem.get("RECEIVE_DATE"));
							resultJsonInt.put("CUSTOMER", (String) hmItem.get("CUSTOMER"));
							resultJsonInt.put("BANK_BRANCH", (String) hmItem.get("BANK_BRANCH"));
							resultJsonInt.put("CHECQUE_NO", (String) hmItem.get("CHECQUE_NO"));
							resultJsonInt.put("CHEQUE_DATE", (String) hmItem.get("CHEQUE_DATE"));
							resultJsonInt.put("CHEQUE_REVERSAL_DATE", (String) hmItem.get("CHEQUE_REVERSAL_DATE"));
							resultJsonInt.put("CHEQUE_AMOUNT", (String) hmItem.get("CHEQUE_AMOUNT"));
							jsonArray.add(resultJsonInt);
						}
					}
				}else {
					for (int iCnt = 0; iCnt < listQry.size(); iCnt++) {
//						int iIndex = iCnt + 1;
						HashMap hmItem = (HashMap) listQry.get(iCnt);
						JSONObject resultJsonInt = new JSONObject();
						resultJsonInt.put("SNO", (String) hmItem.get("RECEIVEID"));
						resultJsonInt.put("PAYMENT_DATE", (String) hmItem.get("RECEIVE_DATE"));
						resultJsonInt.put("CUSTOMER", (String) hmItem.get("CUSTOMER"));
						resultJsonInt.put("BANK_BRANCH", (String) hmItem.get("BANK_BRANCH"));
						resultJsonInt.put("CHECQUE_NO", (String) hmItem.get("CHECQUE_NO"));
						resultJsonInt.put("CHEQUE_DATE", (String) hmItem.get("CHEQUE_DATE"));
						resultJsonInt.put("CHEQUE_REVERSAL_DATE", (String) hmItem.get("CHEQUE_REVERSAL_DATE"));
						resultJsonInt.put("CHEQUE_AMOUNT", (String) hmItem.get("CHEQUE_AMOUNT"));
						jsonArray.add(resultJsonInt);
					}
				}
				
				JSONObject resultJsonInt = new JSONObject();
				resultJsonInt.put("ERROR_MESSAGE", "NO ERRORS!");
				resultJsonInt.put("ERROR_CODE", "100");
				jsonArrayErr.add(resultJsonInt);
				if(jsonArray.size()==0)
					jsonArray.add("");
				resultJson.put("payments", jsonArray);
				resultJson.put("errors", jsonArrayErr);
			} else {
				JSONObject resultJsonInt = new JSONObject();
				resultJsonInt.put("ERROR_MESSAGE", "NO PRODUCT RECORD FOUND!");
				resultJsonInt.put("ERROR_CODE", "99");
				jsonArrayErr.add(resultJsonInt);
				jsonArray.add("");
				resultJson.put("payments", jsonArray);
				resultJson.put("errors", jsonArrayErr);
			}
			resultJson.put("recordsFiltered", listQry.size());
		} catch (Exception e) {
			resultJson.put("SEARCH_DATA", "");
			JSONObject resultJsonInt = new JSONObject();
			resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
			resultJsonInt.put("ERROR_CODE", "98");
			jsonArrayErr.add(resultJsonInt);
			jsonArray.add("");
			resultJson.put("payments", jsonArray);
			resultJson.put("errors", jsonArrayErr);
			resultJson.put("recordsFiltered", 0);
		}
		return resultJson;
	}

	private JSONObject getAccountPayableForDashboard(HttpServletRequest request) {
		JSONObject resultJson = new JSONObject();
		JSONArray jsonArray = new JSONArray();
		JSONArray jsonArrayErr = new JSONArray();
		try {
			String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
			String currency = StrUtils.fString((String) request.getSession().getAttribute("BASE_CURRENCY")).trim();
			String fromDate = StrUtils.fString(request.getParameter("FROM_DATE")).trim();
			String toDate = StrUtils.fString(request.getParameter("TO_DATE")).trim();
			PlantMstDAO plantMstDAO = new PlantMstDAO();
		    String numberOfDecimal = plantMstDAO.getNumberOfDecimal(plant);
		    double amountPayable = Double.parseDouble(new InvoiceDAO().getTotalPayableForDashboard(plant, fromDate, toDate, numberOfDecimal));
		    double pdcAmount = Double.parseDouble(new BillPaymentDAO().getPdcAmountPayableForDashboard(plant, fromDate, toDate, numberOfDecimal));
		    double totalAmount= amountPayable + pdcAmount;
		    
			if (!(amountPayable == 0 && pdcAmount == 0)) {
				JSONObject resultJsonInt = new JSONObject();
				resultJsonInt.put("TYPE", "NON PDC PAYMENT");
				resultJsonInt.put("AMOUNT", amountPayable);
				jsonArray.add(resultJsonInt);
				
				resultJsonInt = new JSONObject();
				resultJsonInt.put("TYPE", "PDC PAYMENT");
				resultJsonInt.put("AMOUNT", pdcAmount);
				jsonArray.add(resultJsonInt);
				
				resultJsonInt = new JSONObject();
				resultJsonInt.put("ERROR_MESSAGE", "NO ERRORS!");
				resultJsonInt.put("ERROR_CODE", "100");
				jsonArrayErr.add(resultJsonInt);
				resultJson.put("payments", jsonArray);
				resultJson.put("errors", jsonArrayErr);
				resultJson.put("TOTAL_AMOUNT", currency+StrUtils.addZeroes(totalAmount, numberOfDecimal));
			} else {
				JSONObject resultJsonInt = new JSONObject();
				resultJsonInt.put("ERROR_MESSAGE", "NO PRODUCT RECORD FOUND!");
				resultJsonInt.put("ERROR_CODE", "99");
				jsonArrayErr.add(resultJsonInt);
				jsonArray.add("");
				resultJson.put("payments", jsonArray);
				resultJson.put("errors", jsonArrayErr);
			}
		} catch (Exception e) {
			resultJson.put("SEARCH_DATA", "");
			JSONObject resultJsonInt = new JSONObject();
			resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
			resultJsonInt.put("ERROR_CODE", "98");
			jsonArrayErr.add(resultJsonInt);
			jsonArray.add("");
			resultJson.put("payments", jsonArray);
			resultJson.put("errors", jsonArrayErr);
			resultJson.put("recordsFiltered", 0);
		}
		return resultJson;
	}
	
	private JSONObject getAccountReceivableForDashboard(HttpServletRequest request) {
		JSONObject resultJson = new JSONObject();
		JSONArray jsonArray = new JSONArray();
		JSONArray jsonArrayErr = new JSONArray();
		try {
			String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
			String currency = StrUtils.fString((String) request.getSession().getAttribute("BASE_CURRENCY")).trim();
			String fromDate = StrUtils.fString(request.getParameter("FROM_DATE")).trim();
			String toDate = StrUtils.fString(request.getParameter("TO_DATE")).trim();
			PlantMstDAO plantMstDAO = new PlantMstDAO();
		    String numberOfDecimal = plantMstDAO.getNumberOfDecimal(plant);
		    double amountReceivable = Double.parseDouble(new BillDAO().getTotalReceivableForDashboard(plant, fromDate, toDate, numberOfDecimal));
		    double pdcAmount = Double.parseDouble(new InvoicePaymentDAO().getPdcAmountReceivableForDashboard(plant, fromDate, toDate, numberOfDecimal));
		    double totalAmount= amountReceivable + pdcAmount;
			if (!(amountReceivable == 0 && pdcAmount == 0)) {
				JSONObject resultJsonInt = new JSONObject();
				resultJsonInt.put("TYPE", "NON PDC PAYMENT");
				resultJsonInt.put("AMOUNT", amountReceivable);
				jsonArray.add(resultJsonInt);
				
				resultJsonInt = new JSONObject();
				resultJsonInt.put("TYPE", "PDC PAYMENT");
				resultJsonInt.put("AMOUNT", pdcAmount);
				jsonArray.add(resultJsonInt);
				
				resultJsonInt = new JSONObject();
				resultJsonInt.put("ERROR_MESSAGE", "NO ERRORS!");
				resultJsonInt.put("ERROR_CODE", "100");
				jsonArrayErr.add(resultJsonInt);
				resultJson.put("payments", jsonArray);
				resultJson.put("errors", jsonArrayErr);
				resultJson.put("TOTAL_AMOUNT", currency+StrUtils.addZeroes(totalAmount, numberOfDecimal));
			} else {
				JSONObject resultJsonInt = new JSONObject();
				resultJsonInt.put("ERROR_MESSAGE", "NO PRODUCT RECORD FOUND!");
				resultJsonInt.put("ERROR_CODE", "99");
				jsonArrayErr.add(resultJsonInt);
				jsonArray.add("");
				resultJson.put("payments", jsonArray);
				resultJson.put("errors", jsonArrayErr);
			}
		} catch (Exception e) {
			resultJson.put("SEARCH_DATA", "");
			JSONObject resultJsonInt = new JSONObject();
			resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
			resultJsonInt.put("ERROR_CODE", "98");
			jsonArrayErr.add(resultJsonInt);
			jsonArray.add("");
			resultJson.put("payments", jsonArray);
			resultJson.put("errors", jsonArrayErr);
			resultJson.put("recordsFiltered", 0);
		}
		return resultJson;
	}
	
	private JSONObject getPayableAmountBySupplier(HttpServletRequest request) {
		JSONObject resultJson = new JSONObject();
		JSONArray jsonArray = new JSONArray();
		JSONArray jsonArrayErr = new JSONArray();
		try {

			String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
			String currency = StrUtils.fString((String) request.getSession().getAttribute("BASE_CURRENCY")).trim();
			String fromDate = StrUtils.fString(request.getParameter("FROM_DATE")).trim();
			String toDate = StrUtils.fString(request.getParameter("TO_DATE")).trim();
//			String period = StrUtils.fString(request.getParameter("PERIOD")).trim();
			String vname = StrUtils.fString(request.getParameter("VNAME")).trim();
			double totalAmount= 0.0;
			PlantMstDAO plantMstDAO = new PlantMstDAO();
		    String numberOfDecimal = plantMstDAO.getNumberOfDecimal(plant);
		    List listQry = "track".equalsIgnoreCase(plant) ? new ArrayList() : new BillDAO().getPayableAmountBySupplierForDashboard(plant, fromDate, toDate, numberOfDecimal);
			if (listQry.size() > 0) {
				if(vname.length()>0) {
					for (int iCnt = 0; iCnt < listQry.size(); iCnt++) {
//						int iIndex = iCnt + 1;
						HashMap hmItem = (HashMap) listQry.get(iCnt);
						if(vname.equalsIgnoreCase((String) hmItem.get("SUPPLIER"))) {
							JSONObject resultJsonInt = new JSONObject();
							resultJsonInt.put("SUPPLIER", (String) hmItem.get("SUPPLIER"));
							resultJsonInt.put("TOTAL_PAYABLE", (String) hmItem.get("TOTAL_PAYABLE"));
							resultJsonInt.put("NON_PDC_PAYMENT", (String) hmItem.get("AMOUNT_PAYABLE"));
							resultJsonInt.put("PDC_PAYMENT", (String) hmItem.get("PDC_AMOUNT"));					
							jsonArray.add(resultJsonInt);						
							totalAmount += Double.parseDouble((String) hmItem.get("TOTAL_PAYABLE"));
						}
					}
				}else {
					for (int iCnt = 0; iCnt < listQry.size(); iCnt++) {
//						int iIndex = iCnt + 1;
						HashMap hmItem = (HashMap) listQry.get(iCnt);
						JSONObject resultJsonInt = new JSONObject();
						resultJsonInt.put("SUPPLIER", (String) hmItem.get("SUPPLIER"));
						resultJsonInt.put("TOTAL_PAYABLE", (String) hmItem.get("TOTAL_PAYABLE"));
						resultJsonInt.put("NON_PDC_PAYMENT", (String) hmItem.get("AMOUNT_PAYABLE"));
						resultJsonInt.put("PDC_PAYMENT", (String) hmItem.get("PDC_AMOUNT"));					
						jsonArray.add(resultJsonInt);						
						totalAmount += Double.parseDouble((String) hmItem.get("TOTAL_PAYABLE"));
					}					
				}
				
				JSONObject resultJsonInt = new JSONObject();
				resultJsonInt.put("ERROR_MESSAGE", "NO ERRORS!");
				resultJsonInt.put("ERROR_CODE", "100");
				jsonArrayErr.add(resultJsonInt);
				if(jsonArray.size()==0)
					jsonArray.add("");
				resultJson.put("bills", jsonArray);
				resultJson.put("errors", jsonArrayErr);
				resultJson.put("TOTAL_AMOUNT", currency+StrUtils.addZeroes(totalAmount, numberOfDecimal));
			} else {
				JSONObject resultJsonInt = new JSONObject();
				resultJsonInt.put("ERROR_MESSAGE", "NO PRODUCT RECORD FOUND!");
				resultJsonInt.put("ERROR_CODE", "99");
				jsonArrayErr.add(resultJsonInt);
				jsonArray.add("");
				resultJson.put("bills", jsonArray);
				resultJson.put("errors", jsonArrayErr);
			}
			resultJson.put("recordsFiltered", listQry.size());
		} catch (Exception e) {
			resultJson.put("SEARCH_DATA", "");
			JSONObject resultJsonInt = new JSONObject();
			resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
			resultJsonInt.put("ERROR_CODE", "98");
			jsonArrayErr.add(resultJsonInt);
			jsonArray.add("");
			resultJson.put("bills", jsonArray);
			resultJson.put("errors", jsonArrayErr);
			resultJson.put("recordsFiltered", 0);
		}
		return resultJson;
	}
	
	private JSONObject getReceivableAmountByCustomer(HttpServletRequest request) {
		JSONObject resultJson = new JSONObject();
		JSONArray jsonArray = new JSONArray();
		JSONArray jsonArrayErr = new JSONArray();
		try {

			String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
			String currency = StrUtils.fString((String) request.getSession().getAttribute("BASE_CURRENCY")).trim();
			String fromDate = StrUtils.fString(request.getParameter("FROM_DATE")).trim();
			String toDate = StrUtils.fString(request.getParameter("TO_DATE")).trim();
//			String period = StrUtils.fString(request.getParameter("PERIOD")).trim();
			String cname = StrUtils.fString(request.getParameter("CNAME")).trim();
			double totalAmount= 0.0;
			PlantMstDAO plantMstDAO = new PlantMstDAO();
		    String numberOfDecimal = plantMstDAO.getNumberOfDecimal(plant);
		    List listQry = "track".equalsIgnoreCase(plant) ? new ArrayList() : new InvoiceDAO().getReceivableAmountByCustomerForDashboard(plant, fromDate, toDate, numberOfDecimal);
			if (listQry.size() > 0) {
				if(cname.length()>0){
					for (int iCnt = 0; iCnt < listQry.size(); iCnt++) {
//						int iIndex = iCnt + 1;
						HashMap hmItem = (HashMap) listQry.get(iCnt);
						if(cname.equalsIgnoreCase((String) hmItem.get("CUSTOMER"))) {
							JSONObject resultJsonInt = new JSONObject();
							resultJsonInt.put("CUSTOMER", (String) hmItem.get("CUSTOMER"));
							resultJsonInt.put("TOTAL_RECEIVABLE", (String) hmItem.get("TOTAL_RECEIVABLE"));
							resultJsonInt.put("NON_PDC_PAYMENT", (String) hmItem.get("AMOUNT_RECEIVABLE"));
							resultJsonInt.put("PDC_PAYMENT", (String) hmItem.get("PDC_AMOUNT"));
							totalAmount += Double.parseDouble((String) hmItem.get("TOTAL_RECEIVABLE"));
							jsonArray.add(resultJsonInt);
						}
					}
				}else {
					for (int iCnt = 0; iCnt < listQry.size(); iCnt++) {
//						int iIndex = iCnt + 1;
						HashMap hmItem = (HashMap) listQry.get(iCnt);
						JSONObject resultJsonInt = new JSONObject();
						resultJsonInt.put("CUSTOMER", (String) hmItem.get("CUSTOMER"));
						resultJsonInt.put("TOTAL_RECEIVABLE", (String) hmItem.get("TOTAL_RECEIVABLE"));
						resultJsonInt.put("NON_PDC_PAYMENT", (String) hmItem.get("AMOUNT_RECEIVABLE"));
						resultJsonInt.put("PDC_PAYMENT", (String) hmItem.get("PDC_AMOUNT"));
						totalAmount += Double.parseDouble((String) hmItem.get("TOTAL_RECEIVABLE"));
						jsonArray.add(resultJsonInt);
					}
				}
				
				JSONObject resultJsonInt = new JSONObject();
				resultJsonInt.put("ERROR_MESSAGE", "NO ERRORS!");
				resultJsonInt.put("ERROR_CODE", "100");
				jsonArrayErr.add(resultJsonInt);
				if(jsonArray.size() == 0)
					jsonArray.add("");
				resultJson.put("invoices", jsonArray);
				resultJson.put("errors", jsonArrayErr);
				resultJson.put("TOTAL_AMOUNT", currency+StrUtils.addZeroes(totalAmount, numberOfDecimal));
			} else {
				JSONObject resultJsonInt = new JSONObject();
				resultJsonInt.put("ERROR_MESSAGE", "NO PRODUCT RECORD FOUND!");
				resultJsonInt.put("ERROR_CODE", "99");
				jsonArrayErr.add(resultJsonInt);
				jsonArray.add("");
				resultJson.put("invoices", jsonArray);
				resultJson.put("errors", jsonArrayErr);
			}
			resultJson.put("recordsFiltered", listQry.size());
		} catch (Exception e) {
			resultJson.put("SEARCH_DATA", "");
			JSONObject resultJsonInt = new JSONObject();
			resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
			resultJsonInt.put("ERROR_CODE", "98");
			jsonArrayErr.add(resultJsonInt);
			jsonArray.add("");
			resultJson.put("invoices", jsonArray);
			resultJson.put("errors", jsonArrayErr);
			resultJson.put("recordsFiltered", 0);
		}
		return resultJson;
	}
	
	private JSONObject PrintAgeingReport(HttpServletRequest request){
		JSONObject resultJson = new JSONObject();
		JSONArray jsonArray = new JSONArray();
        JSONArray jsonArrayErr = new JSONArray();
		try {
//			CustUtil cUtil = new CustUtil();
//			PlantMstUtil pmUtil = new PlantMstUtil();
//		    CustomerBeanDAO customerBeanDAO = new CustomerBeanDAO();
//			Map m = null;
			HttpSession session = request.getSession();
			String invoiceNo="",loadCust="";/*CNAME = "", CADD1 = "", CADD2 = "", CADD3 = "", CADD4 = "", CZIP = "", CCOUNTRY = "", CTEL = "", CFAX = "", CONTACTNAME = "", 
				   CSTATE = "", CHPNO = "", CEMAIL = "",CRCBNO="",LOGIN_USER="",*/
			
			String Orderno ="",custName="";//,custcode="",ordRefNo="",Duetopay="",OrdDate="",OrderAmt="",chkddata="",PAYMENT_AMT="";
			String ORDER ="";//,ordNo="",PAYMENT_DATE="",PAYMENT_MODE="",PAYMENT_REF="",id="";
			String STATEMENT_DATE="",FROM_DATE="",TO_DATE="",currencyid="",fdate="",tdate="",pmtdays="";//,sCustCode ="",sCustName="",sAddr1="",sAddr2="",sAddr3="",sAddr4="",sCountry="",sZip="",sContactName="",sTelNo="",sHpNo="",sFax="",sEmail="",dueDate="";
//			ArrayList arrCust = new ArrayList();
			ArrayList arrCustomers = new ArrayList();
			
			String PLANT = (String) session.getAttribute("PLANT");
//			LOGIN_USER = StrUtils.fString((String) request.getSession().getAttribute("LOGIN_USER")).trim();
			ORDER=StrUtils.fString(request.getParameter("Order"));
//	        ordRefNo = StrUtils.fString(request.getParameter("ordRefNo"));
//	        custName= StrUtils.fString(request.getParameter("custName"));
	        STATEMENT_DATE     = StrUtils.fString(request.getParameter("STATEMENT_DATE"));
	        FROM_DATE     = StrUtils.fString(request.getParameter("FROM_DATE"));
	        TO_DATE   = StrUtils.fString(request.getParameter("TO_DATE"));
	        Orderno = StrUtils.fString(request.getParameter("ORDERNO"));
	        invoiceNo = StrUtils.fString(request.getParameter("NAME"));
	        loadCust = StrUtils.fString(request.getParameter("LOADCUST"));
	        
	        AgeingUtil ageingUtil = new AgeingUtil();
	        
	        if(FROM_DATE==null) FROM_DATE=""; else FROM_DATE = FROM_DATE.trim();
	        String curDate =DateUtils.getDate();
            if(FROM_DATE.length()<0||FROM_DATE==null||FROM_DATE.equalsIgnoreCase(""))FROM_DATE=curDate;
            
            if(STATEMENT_DATE.length()<0||STATEMENT_DATE==null||STATEMENT_DATE.equalsIgnoreCase(""))STATEMENT_DATE=curDate;
	           
	        if (FROM_DATE.length()>5)

	        	fdate    = FROM_DATE.substring(6)+"-"+ FROM_DATE.substring(3,5)+"-"+FROM_DATE.substring(0,2);

	       	if (TO_DATE.length()>5)
	        	tdate    = TO_DATE.substring(6)+"-"+ TO_DATE.substring(3,5)+"-"+TO_DATE.substring(0,2);
 
	        String searchCond="",dtCondStr="",subSearchCond="";//extracondpmt="",extraCon="",dtCondpmt="",paymenttype="",currencycond="",
		   
            /*if(ORDER.equalsIgnoreCase("BILL"))
            {
            	extraCon = " ORDERTYPE='BILL' ";
            }
            else if(ORDER.equalsIgnoreCase("INVOICE"))
            {
            	extraCon = " ORDERTYPE='INVOICE' ";
            }
            
            paymenttype = " and ISNULL(PAYMENT_MODE,'')<>'' ";*/
          
            dtCondStr = "  AND CAST((SUBSTRING(PAYMENT_DT, 7, 4) + '-' + SUBSTRING(PAYMENT_DT, 4, 2) + '-' + SUBSTRING(PAYMENT_DT, 1, 2)) AS date)";
        	 if (fdate.length() > 0) {
				 searchCond = searchCond + dtCondStr + "  >= '" 
						+ fdate
						+ "'  ";
			
				if (tdate.length() > 0) {
					searchCond = searchCond +dtCondStr+ " <= '" 
					+ tdate
					+ "'  ";
					
				}
			} else {
				if (tdate.length() > 0) {
					searchCond = searchCond +dtCondStr+ " <= '" 
					+ tdate
					+ "'  ";
					
				}
			} 
			if (tdate.length() > 0) {
				subSearchCond = subSearchCond +dtCondStr+ " <= '" 
				+ tdate
				+ "'  ";
				
			}
			
        	Calendar cal = new GregorianCalendar();
   			SimpleDateFormat sdfrmt = new SimpleDateFormat("dd/MM/yyyy");
//   			SimpleDateFormat sdfrmt2 = new SimpleDateFormat("yyyy-MM-dd");
       		Date date;
       		date = sdfrmt.parse(FROM_DATE);
  			cal.setTime(date);
  			cal.add(Calendar.DATE, -1);
  			sdfrmt.setCalendar(cal);
//  			String prevFromDate = sdfrmt.format(cal.getTime());

//			String SysDate = DateUtils.getDate();
			
//		boolean flag =  
				ageingUtil.InsertTempOrderPaymentForAgeingReport(PLANT, fdate, tdate, ORDER, custName, Orderno, invoiceNo);	
		arrCustomers =  ageingUtil.getcustomerorsuppliername(PLANT, fdate, tdate, ORDER, custName);
		
		if(arrCustomers.size() > 0) {
		String imagePath = DbBean.COMPANY_LOGO_PATH + "/" + PLANT.toLowerCase() + DbBean.LOGO_FILE;
		File checkImageFile = new File(imagePath);
		if (!checkImageFile.exists()) {
			imagePath = DbBean.COMPANY_LOGO_PATH + "/" + DbBean.NO_LOGO_FILE;
		}
		
		for (int i = 0; i < arrCustomers.size(); i++)
		{
			Map lineArrcust = (Map) arrCustomers.get(i);                      
//			custcode = (String)lineArrcust.get("custcode");
			custName = (String)lineArrcust.get("custname");
			pmtdays = (String)lineArrcust.get("pmtdays");
//			JSONArray ageingDetArray = new JSONArray();
            /*extracondpmt="";
			if(custcode.length()>0){
				extracondpmt = " AND "+extraCon + searchCond + " AND (   CustCode = '"+custName+"') "; 	
			}*/
//            currencycond = subSearchCond + " AND (   CustCode = '"+custName+"') ";
			 
				 
			 ArrayList movQryList  = new ArrayList();
//			 ArrayList reportDetailList  = new ArrayList();
			 double currentdue=0;double days30=0;double days60=0;double days90=0;double days120=0;double amtdue=0;
			 curDate = STATEMENT_DATE;
			 curDate = curDate.substring(0,2) + curDate.substring(3,5) + curDate.substring(6);
			 int pdays = 0;
			 if(pmtdays.length()>0)
			 {
  				 pdays = Integer.parseInt(pmtdays);
  			 }
			 
			 movQryList = ageingUtil.getAgeingDetailsByFrwdBal(PLANT, fdate, tdate, ORDER, custName,"",currencyid);
			 
			 for (int iCnt =0; iCnt<movQryList.size(); iCnt++){					 
				 Map lineArr = (Map) movQryList.get(iCnt);
//	               int iIndex = iCnt + 1;		              
	               String ordDate = (String)lineArr.get("ordDate");
	               double balPay = Double.parseDouble((String)lineArr.get("DueToPay"));		               
	               ordDate = ordDate.substring(0,2) + ordDate.substring(3,5) + ordDate.substring(6);		                
	               Calendar cal1 = new GregorianCalendar();
	               Calendar cal2 = new GregorianCalendar();
	               SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy");
	               Date d1 = sdf.parse(ordDate);
	               cal1.setTime(d1);
	               Date d2 = sdf.parse(curDate);
	               cal2.setTime(d2);		               
	               Long diff = d2.getTime()-d1.getTime();		               
	               int days =  (int) (diff/(1000 * 60 * 60 * 24));	
	               if(days <= pdays)
	               {
	            	   currentdue = currentdue + balPay;
	               }
	               else{		            	   
	            	   days =  days-pdays;		               
	            	   if(days>0 && days<=30)
	            	   {
	            		   days30 = days30 + balPay;
	            	   }
	            	   if(days>30 && days<=60)
	            	   {
	            		   days60 = days60 + balPay;
	            	   }
	            	   if(days>60 && days<=90)
	            	   {
	            		   days90 = days90 + balPay;
	            	   }
	            	   if(days>90)
	            	   {
	            		   days120 = days120 + balPay;
	            	   }
	               }  		               
			 }				 
			 amtdue =  currentdue+days30+days60+days90+days120;					 
			 String currentdueamt = String.valueOf(currentdue);
			 currentdueamt = StrUtils.formattwodecNum(currentdueamt); 
			 
			 String days30due = String.valueOf(days30);
			 days30due = StrUtils.formattwodecNum(days30due); 
			
			 String days60due = String.valueOf(days60);
			 days60due = StrUtils.formattwodecNum(days60due); 
			
			 String days90due = String.valueOf(days90);
			 days90due = StrUtils.formattwodecNum(days90due); 
			
			 String days120due = String.valueOf(days120);
			 days120due = StrUtils.formattwodecNum(days120due); 
			
			 String totaldue = String.valueOf(amtdue);
			 totaldue = StrUtils.formattwodecNum(totaldue); 
		                
//            Map parameters = new HashMap();
            JSONObject reportContent = new JSONObject();
            
            /* Customer Details */
            reportContent.put("To_CompanyName", custName);
            
            /* Aging by Days*/
            reportContent.put("currentdue", currentdueamt);
            reportContent.put("v30daysdue", days30due);
            reportContent.put("v60daysdue", days60due);
            reportContent.put("v90daysdue", days90due);
            reportContent.put("v90plusdaysdue", days120due);
			reportContent.put("amountdue", totaldue);
			
			jsonArray.add(reportContent);
			 resultJson.put("reportContent", jsonArray);	 
		}
		}else {
			jsonArray.add("");
			resultJson.put("reportContent", jsonArray);	 
		}
		JSONObject resultJsonInt = new JSONObject();
        resultJsonInt.put("ERROR_MESSAGE", "NO ERRORS!");
        resultJsonInt.put("ERROR_CODE", "100");
        jsonArrayErr.add(resultJsonInt);
        resultJson.put("errors", jsonArrayErr);
        resultJson.put("LOADCUST", loadCust);
        
		} catch (Exception e) {
			jsonArray.add("");
    		resultJson.put("reportContent", jsonArray);
            resultJson.put("SEARCH_DATA", "");
            JSONObject resultJsonInt = new JSONObject();
            resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
            resultJsonInt.put("ERROR_CODE", "98");
            jsonArrayErr.add(resultJsonInt);
            resultJson.put("ERROR", jsonArrayErr);
		} 
       return resultJson;
	}
	
	private JSONObject getTotalAsset(HttpServletRequest request) {
		JSONObject resultJson = new JSONObject();
//		JSONArray jsonArray = new JSONArray();
		JSONArray jsonArrayErr = new JSONArray();
		try {
			String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
			String fromDate = StrUtils.fString(request.getParameter("FROM_DATE")).trim();
			String toDate = StrUtils.fString(request.getParameter("TO_DATE")).trim();
			ReportService reportService = new ReportServiceImpl();
			double totalAsset = reportService.totalAsset(plant, fromDate, toDate);
			
			JSONObject resultJsonInt = new JSONObject();
			resultJsonInt.put("ERROR_MESSAGE", "NO ERRORS!");
			resultJsonInt.put("ERROR_CODE", "100");
			jsonArrayErr.add(resultJsonInt);
			resultJson.put("TOTAL_ASSET", totalAsset);
			resultJson.put("errors", jsonArrayErr);
			
		} catch (Exception e) {
			resultJson.put("SEARCH_DATA", "");
			JSONObject resultJsonInt = new JSONObject();
			resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
			resultJsonInt.put("ERROR_CODE", "98");
			jsonArrayErr.add(resultJsonInt);
			resultJson.put("TOTAL_ASSET", "");
			resultJson.put("errors", jsonArrayErr);
			resultJson.put("recordsFiltered", 0);
		}
		return resultJson;
	}
	
	private JSONObject getTotalCashInHand(HttpServletRequest request) {
		JSONObject resultJson = new JSONObject();
//		JSONArray jsonArray = new JSONArray();
		JSONArray jsonArrayErr = new JSONArray();
		try {
			String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
			String fromDate = StrUtils.fString(request.getParameter("FROM_DATE")).trim();
			String toDate = StrUtils.fString(request.getParameter("TO_DATE")).trim();
			ReportService reportService = new ReportServiceImpl();
			double totalCashInHand = reportService.totalCashInHand(plant, fromDate, toDate);
			
			JSONObject resultJsonInt = new JSONObject();
			resultJsonInt.put("ERROR_MESSAGE", "NO ERRORS!");
			resultJsonInt.put("ERROR_CODE", "100");
			jsonArrayErr.add(resultJsonInt);
			resultJson.put("TOTAL_CASHINHAND", totalCashInHand);
			resultJson.put("errors", jsonArrayErr);
			
		} catch (Exception e) {
			resultJson.put("SEARCH_DATA", "");
			JSONObject resultJsonInt = new JSONObject();
			resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
			resultJsonInt.put("ERROR_CODE", "98");
			jsonArrayErr.add(resultJsonInt);
			resultJson.put("TOTAL_ASSET", "");
			resultJson.put("errors", jsonArrayErr);
			resultJson.put("recordsFiltered", 0);
		}
		return resultJson;
	}
	
	private JSONObject getTotalCashInHandAll(HttpServletRequest request) {
		JSONObject resultJson = new JSONObject();
//		JSONArray jsonArray = new JSONArray();
		JSONArray jsonArrayErr = new JSONArray();
		JournalDAO journaldao = new JournalDAO();
		try {
			String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
			ReportService reportService = new ReportServiceImpl();
			//double totalCashInHand = reportService.totalCashInHandAll(plant);
			double totalCashInHand = journaldao.getJournalDetailsForCash(plant, "(156,157)");
			JSONObject resultJsonInt = new JSONObject();
			resultJsonInt.put("ERROR_MESSAGE", "NO ERRORS!");
			resultJsonInt.put("ERROR_CODE", "100");
			jsonArrayErr.add(resultJsonInt);
			resultJson.put("TOTAL_CASHINHAND", totalCashInHand);
			resultJson.put("errors", jsonArrayErr);
			
		} catch (Exception e) {
			resultJson.put("SEARCH_DATA", "");
			JSONObject resultJsonInt = new JSONObject();
			resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
			resultJsonInt.put("ERROR_CODE", "98");
			jsonArrayErr.add(resultJsonInt);
			resultJson.put("TOTAL_ASSET", "");
			resultJson.put("errors", jsonArrayErr);
			resultJson.put("recordsFiltered", 0);
		}
		return resultJson;
	}
	
	private JSONObject getTotalLiability(HttpServletRequest request) {
		JSONObject resultJson = new JSONObject();
//		JSONArray jsonArray = new JSONArray();
		JSONArray jsonArrayErr = new JSONArray();
		try {
			String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
			String fromDate = StrUtils.fString(request.getParameter("FROM_DATE")).trim();
			String toDate = StrUtils.fString(request.getParameter("TO_DATE")).trim();
			ReportService reportService = new ReportServiceImpl();
			double totalLiability = reportService.totalLiability(plant, fromDate, toDate);
			
			JSONObject resultJsonInt = new JSONObject();
			resultJsonInt.put("ERROR_MESSAGE", "NO ERRORS!");
			resultJsonInt.put("ERROR_CODE", "100");
			jsonArrayErr.add(resultJsonInt);
			resultJson.put("TOTAL_LIABILITY", totalLiability);
			resultJson.put("errors", jsonArrayErr);
			
		} catch (Exception e) {
			resultJson.put("SEARCH_DATA", "");
			JSONObject resultJsonInt = new JSONObject();
			resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
			resultJsonInt.put("ERROR_CODE", "98");
			jsonArrayErr.add(resultJsonInt);
			resultJson.put("TOTAL_LIABILITY", "");
			resultJson.put("errors", jsonArrayErr);
			resultJson.put("recordsFiltered", 0);
		}
		return resultJson;
	}
	
	private JSONObject getTotalCashAtBank(HttpServletRequest request) {
		JSONObject resultJson = new JSONObject();
//		JSONArray jsonArray = new JSONArray();
		JSONArray jsonArrayErr = new JSONArray();
		
		try {
			String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
			String fromDate = StrUtils.fString(request.getParameter("FROM_DATE")).trim();
			String toDate = StrUtils.fString(request.getParameter("TO_DATE")).trim();
			ReportService reportService = new ReportServiceImpl();
			double totalCashAtBank = reportService.totalCashAtBank(plant, fromDate, toDate);
			
			JSONObject resultJsonInt = new JSONObject();
			resultJsonInt.put("ERROR_MESSAGE", "NO ERRORS!");
			resultJsonInt.put("ERROR_CODE", "100");
			jsonArrayErr.add(resultJsonInt);
			resultJson.put("TOTAL_CASHATBANK", totalCashAtBank);
			resultJson.put("errors", jsonArrayErr);
			
		} catch (Exception e) {
			resultJson.put("SEARCH_DATA", "");
			JSONObject resultJsonInt = new JSONObject();
			resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
			resultJsonInt.put("ERROR_CODE", "98");
			jsonArrayErr.add(resultJsonInt);
			resultJson.put("TOTAL_LIABILITY", "");
			resultJson.put("errors", jsonArrayErr);
			resultJson.put("recordsFiltered", 0);
		}
		return resultJson;
	}
	
	private JSONObject getTotalCashAtBankAll(HttpServletRequest request) {
		JSONObject resultJson = new JSONObject();
//		JSONArray jsonArray = new JSONArray();
		JSONArray jsonArrayErr = new JSONArray();
		JournalDAO journaldao = new JournalDAO();
		try {
			String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
			ReportService reportService = new ReportServiceImpl();
			//double totalCashAtBank = reportService.totalCashAtBankAll(plant);
			double totalCashAtBank = journaldao.getJournalDetailsBybank(plant,"(8)","(156,157)");
			JSONObject resultJsonInt = new JSONObject();
			resultJsonInt.put("ERROR_MESSAGE", "NO ERRORS!");
			resultJsonInt.put("ERROR_CODE", "100");
			jsonArrayErr.add(resultJsonInt);
			resultJson.put("TOTAL_CASHATBANK", totalCashAtBank);
			resultJson.put("errors", jsonArrayErr);
			
		} catch (Exception e) {
			resultJson.put("SEARCH_DATA", "");
			JSONObject resultJsonInt = new JSONObject();
			resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
			resultJsonInt.put("ERROR_CODE", "98");
			jsonArrayErr.add(resultJsonInt);
			resultJson.put("TOTAL_LIABILITY", "");
			resultJson.put("errors", jsonArrayErr);
			resultJson.put("recordsFiltered", 0);
		}
		return resultJson;
	}
	
	private JSONObject getNetProfit(HttpServletRequest request) {
		JSONObject resultJson = new JSONObject();
//		JSONArray jsonArray = new JSONArray();
		JSONArray jsonArrayErr = new JSONArray();
		try {
			String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
			String fromDate = StrUtils.fString(request.getParameter("FROM_DATE")).trim();
			String toDate = StrUtils.fString(request.getParameter("TO_DATE")).trim();
			ReportService reportService = new ReportServiceImpl();
			double netProfit = reportService.NetProfit(plant, fromDate, toDate);
			
			JSONObject resultJsonInt = new JSONObject();
			resultJsonInt.put("ERROR_MESSAGE", "NO ERRORS!");
			resultJsonInt.put("ERROR_CODE", "100");
			jsonArrayErr.add(resultJsonInt);
			resultJson.put("NET_PROFIT", netProfit);
			resultJson.put("errors", jsonArrayErr);
			
		} catch (Exception e) {
			resultJson.put("SEARCH_DATA", "");
			JSONObject resultJsonInt = new JSONObject();
			resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
			resultJsonInt.put("ERROR_CODE", "98");
			jsonArrayErr.add(resultJsonInt);
			resultJson.put("NET_PROFIT", "");
			resultJson.put("errors", jsonArrayErr);
			resultJson.put("recordsFiltered", 0);
		}
		return resultJson;
	}
	
	private JSONObject getGrossProfit(HttpServletRequest request) {
		JSONObject resultJson = new JSONObject();
//		JSONArray jsonArray = new JSONArray();
		JSONArray jsonArrayErr = new JSONArray();
		try {
			String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
			String fromDate = StrUtils.fString(request.getParameter("FROM_DATE")).trim();
			String toDate = StrUtils.fString(request.getParameter("TO_DATE")).trim();
			ReportService reportService = new ReportServiceImpl();
			double grossProfit = reportService.GrossProfit(plant, fromDate, toDate);
			
			JSONObject resultJsonInt = new JSONObject();
			resultJsonInt.put("ERROR_MESSAGE", "NO ERRORS!");
			resultJsonInt.put("ERROR_CODE", "100");
			jsonArrayErr.add(resultJsonInt);
			resultJson.put("GROSS_PROFIT", grossProfit);
			resultJson.put("errors", jsonArrayErr);
			
		} catch (Exception e) {
			resultJson.put("SEARCH_DATA", "");
			JSONObject resultJsonInt = new JSONObject();
			resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
			resultJsonInt.put("ERROR_CODE", "98");
			jsonArrayErr.add(resultJsonInt);
			resultJson.put("GROSS_PROFIT", "");
			resultJson.put("errors", jsonArrayErr);
			resultJson.put("recordsFiltered", 0);
		}
		return resultJson;
	}
	
	private JSONObject getProductListForSuggestion(HttpServletRequest request) {
        JSONObject resultJson = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        JSONArray jsonArrayErr = new JSONArray();
        try {
//        	StrUtils strUtils = new StrUtils();
//        	ItemMstUtil itemUtil = new ItemMstUtil();
        	String plant = StrUtils.fString(request.getParameter("PLANT")).trim();
        	String text = StrUtils.fString(request.getParameter("TEXT")).trim();
        	String user_id = StrUtils.fString((String) request.getSession().getAttribute("LOGIN_USER")).trim();
        	String level_name = new userBean().getUserLevel(user_id,plant);
        	ArrayList listQry = (ArrayList) new userBean().getMenuListForSuggestion(plant, level_name, text);
        	if (listQry.size() > 0) {        		
        		for(int i =0; i<listQry.size(); i++) {
        			Map m=(Map)listQry.get(i);
        			JSONObject resultJsonInt = new JSONObject();
        			resultJsonInt.put("TEXT", StrUtils.fString((String)m.get("TEXT")));
        			resultJsonInt.put("URL", StrUtils.fString((String)m.get("URL")));
                    jsonArray.add(resultJsonInt);
        		}
        		resultJson.put("menus", jsonArray);
                JSONObject resultJsonInt = new JSONObject();
                resultJsonInt.put("ERROR_MESSAGE", "NO ERRORS!");
                resultJsonInt.put("ERROR_CODE", "100");
                jsonArrayErr.add(resultJsonInt);
                resultJson.put("errors", jsonArrayErr);
        	} else {
                JSONObject resultJsonInt = new JSONObject();
                resultJsonInt.put("ERROR_MESSAGE", "NO RECORD FOUND!");
                resultJsonInt.put("ERROR_CODE", "99");
                jsonArrayErr.add(resultJsonInt);
                jsonArray.add("");
               /* resultJson.put("menus", jsonArray);*/
                resultJson.put("errors", jsonArrayErr);
        	}
        } catch (Exception e) {
	            resultJson.put("SEARCH_DATA", "");
	            JSONObject resultJsonInt = new JSONObject();
	            resultJsonInt.put("ERROR_MESSAGE",  e.getMessage());
	            resultJsonInt.put("ERROR_CODE", "98");
	            jsonArrayErr.add(resultJsonInt);
	            resultJson.put("ERROR", jsonArrayErr);
        }
	    return resultJson;
	}
	
	public List<Date> getbetweendates(String str_date , String end_date){
		List<Date> dates = new ArrayList<Date>();
		try {
			SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
			Date  startDate = (Date)formatter.parse(str_date); 
			Date  endDate = (Date)formatter.parse(end_date);
			long interval = 24*1000 * 60 * 60; // 1 hour in millis
			long endTime =endDate.getTime() ; // create your endtime here, possibly using Calendar or Date
			long curTime = startDate.getTime();
			while (curTime <= endTime) {
			    dates.add(new Date(curTime));
			    curTime += interval;
			}
			/*for(int i=0;i<dates.size();i++){
			    Date lDate =(Date)dates.get(i);
			    String ds = formatter.format(lDate);    
			    System.out.println(" Date is ..." + ds);
			}*/
		}catch (Exception e) {
			System.out.println(e);
		}
		
		return dates;
	}
	
	private JSONObject getCustomerAgeingSummary(HttpServletRequest request){
		HttpSession session = request.getSession();
		String fdate="",tdate="";/*,LOGIN_USER="",ORDER="",ordRefNo="",custName="",STATEMENT_DATE="",
				FROM_DATE="",TO_DATE="",Orderno="",invoiceNo="";*/
		
		JSONObject resultJson = new JSONObject();
		JSONArray jsonArray = new JSONArray();
//		JSONArray ageingDetailsArray = new JSONArray();
        JSONArray jsonArrayErr = new JSONArray();
        JSONArray ageingDetArray = new JSONArray();
        try {
        	String PLANT = (String) session.getAttribute("PLANT");
//    		LOGIN_USER = StrUtils.fString((String) request.getSession().getAttribute("LOGIN_USER")).trim();
//            custName = StrUtils.fString(request.getParameter("custName"));
            String numberOfDecimal = new PlantMstDAO().getNumberOfDecimal(PLANT);
	       	
            Hashtable htCondition = new Hashtable();
    		htCondition.put("PLANT", PLANT);
    		htCondition.put("FROMDATE", fdate);
    		htCondition.put("TODATE", tdate);
    		ArrayList custAgingDetails = new AgeingUtil().getAgingSummaryForDashboard(htCondition);
    		for(int i=0;i<custAgingDetails.size();i++) {
    			Map custAgingDetail = (Map) custAgingDetails.get(i);
    			JSONObject reportDetails = new JSONObject();
    			reportDetails.put("name", (String)custAgingDetail.get("NAME"));
    			reportDetails.put("notDue", StrUtils.addZeroes(Double.parseDouble((String)custAgingDetail.get("DAY0")),numberOfDecimal));
    			reportDetails.put("v30daysdue", StrUtils.addZeroes(Double.parseDouble((String)custAgingDetail.get("DAY1")),numberOfDecimal));
    			reportDetails.put("v60daysdue", StrUtils.addZeroes(Double.parseDouble((String)custAgingDetail.get("DAY2")),numberOfDecimal));
    			reportDetails.put("v90daysdue", StrUtils.addZeroes(Double.parseDouble((String)custAgingDetail.get("DAY3")),numberOfDecimal));
    			reportDetails.put("v90plusdaysdue", StrUtils.addZeroes(Double.parseDouble((String)custAgingDetail.get("DAY4")),numberOfDecimal));
    			reportDetails.put("amountdue", StrUtils.addZeroes(Double.parseDouble((String)custAgingDetail.get("TOTAL_DUE")),numberOfDecimal));
    			ageingDetArray.add(reportDetails);
    		}
    		//ageingDetailsArray.add(ageingDetArray);
    		resultJson.put("reportContent", ageingDetArray);
        } catch (Exception e) {
        	jsonArray.add("");
    		resultJson.put("reportContent", jsonArray);
            resultJson.put("SEARCH_DATA", "");
            JSONObject resultJsonInt = new JSONObject();
            resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
            resultJsonInt.put("ERROR_CODE", "98");
            jsonArrayErr.add(resultJsonInt);
            resultJson.put("ERROR", jsonArrayErr);
        }
        return resultJson;        
	}
	
	private JSONObject getSupplierAgeingSummary(HttpServletRequest request){
		HttpSession session = request.getSession();
		String fdate="",tdate="",loadCust="";/*,LOGIN_USER="",ORDER="",ordRefNo="",custName="",STATEMENT_DATE="",
				FROM_DATE="",TO_DATE="",Orderno="",invoiceNo="";*/
		
		JSONObject resultJson = new JSONObject();
		JSONArray jsonArray = new JSONArray();
//		JSONArray ageingDetailsArray = new JSONArray();
        JSONArray jsonArrayErr = new JSONArray();
        JSONArray ageingDetArray = new JSONArray();
        try {
        	String PLANT = (String) session.getAttribute("PLANT");
//    		LOGIN_USER = StrUtils.fString((String) request.getSession().getAttribute("LOGIN_USER")).trim();
//            custName = StrUtils.fString(request.getParameter("custName"));
            loadCust = StrUtils.fString(request.getParameter("LOADCUST"));
            String numberOfDecimal = new PlantMstDAO().getNumberOfDecimal(PLANT);
	       	
            Hashtable htCondition = new Hashtable();
    		htCondition.put("PLANT", PLANT);
    		htCondition.put("FROMDATE", fdate);
    		htCondition.put("TODATE", tdate);
    		ArrayList custAgingDetails = new AgeingUtil().getSupplierAgingSummaryForDashboard(htCondition);
    		for(int i=0;i<custAgingDetails.size();i++) {
    			Map custAgingDetail = (Map) custAgingDetails.get(i);
    			JSONObject reportDetails = new JSONObject();
    			reportDetails.put("name", (String)custAgingDetail.get("NAME"));
    			reportDetails.put("notDue", StrUtils.addZeroes(Double.parseDouble((String)custAgingDetail.get("DAY0")),numberOfDecimal));
    			reportDetails.put("v30daysdue", StrUtils.addZeroes(Double.parseDouble((String)custAgingDetail.get("DAY1")),numberOfDecimal));
    			reportDetails.put("v60daysdue", StrUtils.addZeroes(Double.parseDouble((String)custAgingDetail.get("DAY2")),numberOfDecimal));
    			reportDetails.put("v90daysdue", StrUtils.addZeroes(Double.parseDouble((String)custAgingDetail.get("DAY3")),numberOfDecimal));
    			reportDetails.put("v90plusdaysdue", StrUtils.addZeroes(Double.parseDouble((String)custAgingDetail.get("DAY4")),numberOfDecimal));
    			reportDetails.put("amountdue", StrUtils.addZeroes(Double.parseDouble((String)custAgingDetail.get("TOTAL_DUE")),numberOfDecimal));
    			ageingDetArray.add(reportDetails);
    		}
    		//ageingDetailsArray.add(ageingDetArray);
    		resultJson.put("reportContent", ageingDetArray);
    		resultJson.put("LOADCUST", loadCust);
        } catch (Exception e) {
        	jsonArray.add("");
    		resultJson.put("reportContent", jsonArray);
            resultJson.put("SEARCH_DATA", "");
            JSONObject resultJsonInt = new JSONObject();
            resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
            resultJsonInt.put("ERROR_CODE", "98");
            jsonArrayErr.add(resultJsonInt);
            resultJson.put("ERROR", jsonArrayErr);
        }
        return resultJson;        
	}
	
	private JSONObject getAccountPayable(HttpServletRequest request){
		HttpSession session = request.getSession();
		String fdate="",tdate="",loadCust="",TO_DATE="",vendName="";/*,LOGIN_USER="",ORDER="",ordRefNo="",STATEMENT_DATE="",
				FROM_DATE="",Orderno="",invoiceNo="";*/
		
		JSONObject resultJson = new JSONObject();
		JSONArray jsonArray = new JSONArray();
//		JSONArray ageingDetailsArray = new JSONArray();
        JSONArray jsonArrayErr = new JSONArray();
        JSONArray ageingDetArray = new JSONArray();
        try {
        	String PLANT = (String) session.getAttribute("PLANT");
//    		LOGIN_USER = StrUtils.fString((String) request.getSession().getAttribute("LOGIN_USER")).trim();
    		vendName = StrUtils.fString(request.getParameter("vendName"));
            loadCust = StrUtils.fString(request.getParameter("LOADCUST"));
            //FROM_DATE     = StrUtils.fString(request.getParameter("FROM_DATE"));
	        TO_DATE   = StrUtils.fString(request.getParameter("TO_DATE"));
            String numberOfDecimal = new PlantMstDAO().getNumberOfDecimal(PLANT);
            
            /*if (FROM_DATE.length()>5)
	        	fdate    = FROM_DATE.substring(6)+"-"+ FROM_DATE.substring(3,5)+"-"+FROM_DATE.substring(0,2);*/

	       	if (TO_DATE.length()>5)
	        	tdate    = TO_DATE.substring(6)+"-"+ TO_DATE.substring(3,5)+"-"+TO_DATE.substring(0,2);
	       	
            Hashtable htCondition = new Hashtable();
    		htCondition.put("PLANT", PLANT);
    		htCondition.put("FROMDATE", fdate);
    		htCondition.put("TODATE", tdate);
    		htCondition.put("VENDNAME", vendName);
    		ArrayList custAgingDetails = new AgeingUtil().getAccountPayableForDashboard(htCondition);
    		double totalOutstanding = 0.0;
    		for(int i=0;i<custAgingDetails.size();i++) {
    			Map custAgingDetail = (Map) custAgingDetails.get(i);
    			JSONObject reportDetails = new JSONObject();
    			reportDetails.put("name", (String)custAgingDetail.get("NAME"));
    			reportDetails.put("under_due", StrUtils.addZeroes(Double.parseDouble((String)custAgingDetail.get("UNDER_DUE")),numberOfDecimal));
    			reportDetails.put("over_due", StrUtils.addZeroes(Double.parseDouble((String)custAgingDetail.get("OVER_DUE")),numberOfDecimal));
    			reportDetails.put("total_due", StrUtils.addZeroes(Double.parseDouble((String)custAgingDetail.get("TOTAL_DUE")),numberOfDecimal));
    			ageingDetArray.add(reportDetails);
    			totalOutstanding += Double.parseDouble((String)custAgingDetail.get("TOTAL_DUE"));
    		}
    		resultJson.put("reportContent", ageingDetArray);
    		resultJson.put("totalOutstanding", StrUtils.addZeroes(totalOutstanding,numberOfDecimal));
    		resultJson.put("LOADCUST", loadCust);
        } catch (Exception e) {
        	jsonArray.add("");
    		resultJson.put("reportContent", jsonArray);
            resultJson.put("SEARCH_DATA", "");
            JSONObject resultJsonInt = new JSONObject();
            resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
            resultJsonInt.put("ERROR_CODE", "98");
            jsonArrayErr.add(resultJsonInt);
            resultJson.put("ERROR", jsonArrayErr);
        }
        return resultJson;      
	}
	
	private JSONObject getAccountReceivable(HttpServletRequest request){
		HttpSession session = request.getSession();
		String custName="",TO_DATE="",fdate="",tdate="";//LOGIN_USER="",ORDER="",ordRefNo="",STATEMENT_DATE="",FROM_DATE="",Orderno="",invoiceNo="",
		
		JSONObject resultJson = new JSONObject();
		JSONArray jsonArray = new JSONArray();
//		JSONArray ageingDetailsArray = new JSONArray();
        JSONArray jsonArrayErr = new JSONArray();
        JSONArray ageingDetArray = new JSONArray();
        try {
        	String PLANT = (String) session.getAttribute("PLANT");
//    		LOGIN_USER = StrUtils.fString((String) request.getSession().getAttribute("LOGIN_USER")).trim();
    		custName = StrUtils.fString(request.getParameter("custName"));
            //FROM_DATE     = StrUtils.fString(request.getParameter("FROM_DATE"));
	        TO_DATE   = StrUtils.fString(request.getParameter("TO_DATE"));
            String numberOfDecimal = new PlantMstDAO().getNumberOfDecimal(PLANT);
            
            /*if (FROM_DATE.length()>5)
	        	fdate    = FROM_DATE.substring(6)+"-"+ FROM_DATE.substring(3,5)+"-"+FROM_DATE.substring(0,2);*/

	       	if (TO_DATE.length()>5)
	        	tdate    = TO_DATE.substring(6)+"-"+ TO_DATE.substring(3,5)+"-"+TO_DATE.substring(0,2);
	       	
            Hashtable htCondition = new Hashtable();
    		htCondition.put("PLANT", PLANT);
    		htCondition.put("FROMDATE", fdate);
    		htCondition.put("TODATE", tdate);
    		htCondition.put("CNAME", custName);
    		ArrayList custAgingDetails = new AgeingUtil().getAccountReceivableForDashboard(htCondition);
    		double totalOutstanding = 0.0;
    		for(int i=0;i<custAgingDetails.size();i++) {
    			Map custAgingDetail = (Map) custAgingDetails.get(i);
    			JSONObject reportDetails = new JSONObject();
    			reportDetails.put("name", (String)custAgingDetail.get("NAME"));
    			reportDetails.put("under_due", StrUtils.addZeroes(Double.parseDouble((String)custAgingDetail.get("UNDER_DUE")),numberOfDecimal));
    			reportDetails.put("over_due", StrUtils.addZeroes(Double.parseDouble((String)custAgingDetail.get("OVER_DUE")),numberOfDecimal));
    			reportDetails.put("total_due", StrUtils.addZeroes(Double.parseDouble((String)custAgingDetail.get("TOTAL_DUE")),numberOfDecimal));
    			ageingDetArray.add(reportDetails);
    			totalOutstanding += Double.parseDouble((String)custAgingDetail.get("TOTAL_DUE"));
    		}
    		resultJson.put("reportContent", ageingDetArray);
    		resultJson.put("totalOutstanding", StrUtils.addZeroes(totalOutstanding,numberOfDecimal));
        } catch (Exception e) {
        	jsonArray.add("");
    		resultJson.put("reportContent", jsonArray);
            resultJson.put("SEARCH_DATA", "");
            JSONObject resultJsonInt = new JSONObject();
            resultJsonInt.put("ERROR_MESSAGE", e.getMessage());
            resultJsonInt.put("ERROR_CODE", "98");
            jsonArrayErr.add(resultJsonInt);
            resultJson.put("ERROR", jsonArrayErr);
        }
        return resultJson;      
	}
	
	
    public long findDiff(String startdate,String enddate) {

        SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");  
        long lseconds =0;
        try {  
   
        	startdate = new DateUtils().getDate()+" "+startdate+":00";
        	enddate = new DateUtils().getDate()+" "+enddate+":00";
            
            Date d0 = sdf1.parse(startdate);  
            Date d1 = sdf1.parse(enddate);   
            long Time_difference  
                = d0.getTime() - d1.getTime();  
   
            long Minutes_difference  
                = (Time_difference  
                   / (1000 * 60))  
                  % 60;  
   
            long Hours_difference  
                = (Time_difference  
                   / (1000 * 60 * 60))  
                  % 24;  
   
            
            Hours_difference = Hours_difference * 60 * 60;
            Minutes_difference = Minutes_difference * 60;
            lseconds = Hours_difference + Minutes_difference;
   
         
        }  
   
        // Catching the Exception  
        catch (ParseException e) {  
            e.printStackTrace();  
        }  
        
        return lseconds;
    } 
    
    
    public static String splitToComponentTimes(long longVal)
    {
    	//BigDecimal biggy = new BigDecimal(sbiggy);
        //long longVal = biggy.longValue();
    	longVal = Math.abs(longVal);
        int hours = (int) longVal / 3600;
        int remainder = (int) longVal - hours * 3600;
        int mins = remainder / 60;
        remainder = remainder - mins * 60;
        int secs = remainder;
        String result = "";
        if(hours == 0) {
        	result = mins+"m";
        }else {
        	result = hours+"h"+mins+"m";
        }
       
        return result;
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