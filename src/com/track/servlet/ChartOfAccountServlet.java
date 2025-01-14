package com.track.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.stream.Collectors;

import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.NotSupportedException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.track.constants.IDBConstants;
import com.track.constants.MLoggerConstant;
import com.track.constants.TransactionConstants;
import com.track.dao.CoaDAO;
import com.track.dao.MovHisDAO;
import com.track.dao.VendMstDAO;
import com.track.db.object.Journal;
import com.track.db.object.JournalDetail;
import com.track.db.object.JournalHeader;
import com.track.db.util.CoaUtil;
import com.track.db.util.InvoiceUtil;
import com.track.gates.DbBean;
import com.track.service.JournalService;
import com.track.serviceImplementation.JournalEntry;
import com.track.util.DateUtils;
import com.track.util.IMLogger;
import com.track.util.MLogger;
import com.track.util.StrUtils;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class ChartOfAccountServlet extends HttpServlet implements IMLogger {
	private static final long serialVersionUID = 1L;
	private boolean printLog = MLoggerConstant.ChartOfAccountServlet_PRINTPLANTMASTERLOG;
	private boolean printInfo = MLoggerConstant.ChartOfAccountServlet_PRINTPLANTMASTERINFO;
	String action = "";

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		JSONObject jsonObjectResult = new JSONObject();
		action = StrUtils.fString(request.getParameter("action"));
		if (action.equalsIgnoreCase("create")) {
			jsonObjectResult = createAccount(request, response);
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			response.getWriter().write(jsonObjectResult.toString());
			response.getWriter().flush();
			response.getWriter().close();
		} else if (action.equalsIgnoreCase("add_extended")) {
			jsonObjectResult = createExtendedType(request, response);
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			response.getWriter().write(jsonObjectResult.toString());
			response.getWriter().flush();
			response.getWriter().close();
		}
		else if (action.equalsIgnoreCase("read")) {
			jsonObjectResult = readTable(request, response);
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			response.getWriter().write(jsonObjectResult.toString());
			response.getWriter().flush();
			response.getWriter().close();
		} else if (action.equalsIgnoreCase("read_record")) {
			jsonObjectResult = readTableRecord(request, response);
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			response.getWriter().write(jsonObjectResult.toString());
			response.getWriter().flush();
			response.getWriter().close();
		} else if (action.equalsIgnoreCase("update")) {
			jsonObjectResult = updateTable(request, response);
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			response.getWriter().write(jsonObjectResult.toString());
			response.getWriter().flush();
			response.getWriter().close();
		} else if (action.equalsIgnoreCase("getGroupData")) {
			jsonObjectResult = this.getGroupTypes(request, response);
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			response.getWriter().write(jsonObjectResult.toString());
			response.getWriter().flush();
			response.getWriter().close();
		} else if (action.equalsIgnoreCase("getDataForSuggestion")) {
			jsonObjectResult = getDataForSuggestion(request, response);
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			response.getWriter().write(jsonObjectResult.toString());
			response.getWriter().flush();
			response.getWriter().close();
		} else if (action.equalsIgnoreCase("createExpense")) {
			jsonObjectResult = createExpenseAccount(request, response);
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			response.getWriter().write(jsonObjectResult.toString());
			response.getWriter().flush();
			response.getWriter().close();
		} else if (action.equalsIgnoreCase("GetGroupDetail")) {
			jsonObjectResult = this.getGroup(request, response);
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			response.getWriter().write(jsonObjectResult.toString());
			response.getWriter().flush();
			response.getWriter().close();
		} else if (action.equalsIgnoreCase("getAccountType")) {
			jsonObjectResult = this.getAccountType(request, response);
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			response.getWriter().write(jsonObjectResult.toString());
			response.getWriter().flush();
			response.getWriter().close();
		} else if (action.equalsIgnoreCase("checkAccountCode")) {
			jsonObjectResult = this.getcheckAccountCode(request, response);
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			response.getWriter().write(jsonObjectResult.toString());
			response.getWriter().flush();
			response.getWriter().close();
		}   else if (action.equalsIgnoreCase("getSubAccountCode")) {
			jsonObjectResult = this.getSubAccountCode(request, response);
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			response.getWriter().write(jsonObjectResult.toString());
			response.getWriter().flush();
			response.getWriter().close();
		}else if (action.equalsIgnoreCase("getAccountCode")) {
			jsonObjectResult = this.getAccountCode(request, response);
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			response.getWriter().write(jsonObjectResult.toString());
			response.getWriter().flush();
			response.getWriter().close();
		}else if (action.equalsIgnoreCase("getAccountDETCode")) {
			jsonObjectResult = this.getAccountDETCode(request, response);
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			response.getWriter().write(jsonObjectResult.toString());
			response.getWriter().flush();
			response.getWriter().close();
		}else if (action.equalsIgnoreCase("getAccountDetailType")) {
			jsonObjectResult = this.getAccountDetailType(request, response);
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			response.getWriter().write(jsonObjectResult.toString());
			response.getWriter().flush();
			response.getWriter().close();
		} else if (action.equalsIgnoreCase("getSubAccountType")) {
			jsonObjectResult = this.getSubAccountType(request, response);
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			response.getWriter().write(jsonObjectResult.toString());
			response.getWriter().flush();
			response.getWriter().close();
		} else if (action.equalsIgnoreCase("getSubAccountTypeGroup")) {
			jsonObjectResult = this.getSubAccountTypeGrouped(request, response);
			System.out.println("Results::::" + jsonObjectResult);
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			response.getWriter().write(jsonObjectResult.toString());
			response.getWriter().flush();
			response.getWriter().close();
		} else if (action.equalsIgnoreCase("getSubAccountTypeGroupedFilter")) {
			jsonObjectResult = this.getSubAccountTypeGroupedFilter(request, response);
			System.out.println("Results::::" + jsonObjectResult);
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			response.getWriter().write(jsonObjectResult.toString());
			response.getWriter().flush();
			response.getWriter().close();
		} else if (action.equalsIgnoreCase("getSubAccountTypeSummary")) {
			jsonObjectResult = this.getSubAccountTypeSummary(request, response);
			System.out.println("Results::::" + jsonObjectResult);
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			response.getWriter().write(jsonObjectResult.toString());
			response.getWriter().flush();
			response.getWriter().close();
		} else if (action.equalsIgnoreCase("checkAccountCount")) {
			String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
			String username = StrUtils.fString((String) request.getSession().getAttribute("LOGIN_USER")).trim();
			String accName=request.getParameter("accname");
			CoaDAO coaDAO = new CoaDAO();
			String acccount = coaDAO.checkAccCount(accName, plant);
			System.out.println("Results::::" + jsonObjectResult);
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			response.getWriter().write(acccount);
			response.getWriter().flush();
			response.getWriter().close();
		}
		else if (action.equalsIgnoreCase("checkDetailAccountCount")) {
			String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
			String username = StrUtils.fString((String) request.getSession().getAttribute("LOGIN_USER")).trim();
			String detName=request.getParameter("detname");
			CoaDAO coaDAO = new CoaDAO();
			String acccount = coaDAO.checkDetAccCount(detName, plant);
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			response.getWriter().write(acccount);
			response.getWriter().flush();
			response.getWriter().close();
		}
		else if (action.equalsIgnoreCase("deleteAccount")) {
			String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
			String username = StrUtils.fString((String) request.getSession().getAttribute("LOGIN_USER")).trim();
			String accid=request.getParameter("accid");
			JSONObject result=new JSONObject();
			CoaDAO coaDAO = new CoaDAO();
			boolean acccount = coaDAO.deleteAccount(accid, plant);
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			if(acccount)
			{
				result.put("status", "OK");
			}
			else
			{
				result.put("status", "ERROR");
			}
			response.getWriter().write(result.toString());
			response.getWriter().flush();
			response.getWriter().close();
		}
		else if (action.equalsIgnoreCase("isbankcharge")) {
			String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
			String username = StrUtils.fString((String) request.getSession().getAttribute("LOGIN_USER")).trim();
			String accname=request.getParameter("accountname");
			JSONObject result=new JSONObject();
			CoaDAO coaDAO = new CoaDAO();
			boolean acccount = coaDAO.isbankbalance(plant, accname);
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			if(acccount)
			{
				result.put("status", "OK");
			}
			else
			{
				result.put("status", "NOT OK");
			}
			response.getWriter().write(result.toString());
			response.getWriter().flush();
			response.getWriter().close();
		}
		else if (action.equalsIgnoreCase("UpdateAccount")) {
			String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
			String username = StrUtils.fString((String) request.getSession().getAttribute("LOGIN_USER")).trim();
			UserTransaction ut = null;
			CoaDAO coaDAO = new CoaDAO();
			DateUtils dateutils = new DateUtils();
			boolean isAdded = false;
			String result = "";
			List id = new ArrayList(), accountName = new ArrayList();
			List<Hashtable<String, String>> htDetInfoList = null;
			Hashtable<String, String> htDetInfo = null;
			int idCount = 0, accountNameCount = 0;
			try {
				////////////////
				boolean isMultipart = ServletFileUpload.isMultipartContent(request);
				if (isMultipart) {
					FileItemFactory factory = new DiskFileItemFactory();
					ServletFileUpload upload = new ServletFileUpload(factory);
					List items = upload.parseRequest(request);
					Iterator iterator = items.iterator();
					StrUtils strUtils = new StrUtils();
					while (iterator.hasNext()) {
						FileItem fileItem = (FileItem) iterator.next();

						if (fileItem.isFormField()) {
							if (fileItem.getFieldName().equalsIgnoreCase("id")) {
								id.add(idCount, StrUtils.fString(fileItem.getString()).trim());
								idCount++;
							}
						}

						if (fileItem.isFormField()) {
							if (fileItem.getFieldName().equalsIgnoreCase("account_name")) {
								accountName.add(accountNameCount, StrUtils.fString(fileItem.getString()).trim());
								accountNameCount++;
							}
						}
					}
				}
				for (int i = 0; i < id.size(); i++) {
					String idd = (String) id.get(i);
					String accountNamee = (String) accountName.get(i);
					isAdded = coaDAO.updateaccount(plant, idd, " SET account_name = '" + accountNamee + "',UPAT = '"
							+ dateutils.getDateTime() + "',UPBY = '" + username + "'");
				}

				if (isAdded) {
					DbBean.CommitTran(ut);
					result = "Account updated successfully";
				} else {
					DbBean.RollbackTran(ut);
					result = "Account not updated";
				}

				response.sendRedirect("jsp/chartOfAccounts.jsp");
			} catch (Exception e) {
				DbBean.RollbackTran(ut);
			}

		}
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
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

	public JSONObject createAccount(HttpServletRequest request, HttpServletResponse response) {
		String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
		String username = StrUtils.fString((String) request.getSession().getAttribute("LOGIN_USER")).trim();
		String accountType = StrUtils.fString(request.getParameter("acc_type"));
		String accountDetType = StrUtils.fString(request.getParameter("acc_det_type_id"));
		String accountName = StrUtils.fString(request.getParameter("acc_name"));
		String description = StrUtils.fString(request.getParameter("acc_desc"));
		String isSubAccount = StrUtils.fString(request.getParameter("acc_is_sub"));
		String ac_code_pre = StrUtils.fString(request.getParameter("acc_code_pre"));
		String ac_code_post = StrUtils.fString(request.getParameter("acc_code"));
		String ac_code_show = StrUtils.fString(request.getParameter("acc_code_show"));
		String ac_code_post_sub = StrUtils.fString(request.getParameter("sub_maincode"));
		String sub_sub_code = StrUtils.fString(request.getParameter("sub_sub_code"));
		String isExpGstAccount = StrUtils.fString(request.getParameter("acc_is_exp_gst"));
		String ac_code = ac_code_pre+ac_code_post;
		
		if (isSubAccount.equalsIgnoreCase("on"))
			isSubAccount = "1";
		else
			isSubAccount = "0";
		
		if (isExpGstAccount.equalsIgnoreCase("on"))
			isExpGstAccount = "1";
		else
			isExpGstAccount = "0";
		String subAccount = StrUtils.fString(request.getParameter("acc_subAcct"));
		if (isSubAccount.equalsIgnoreCase("0"))
			subAccount = "";
		String accountBalance = StrUtils.fString(request.getParameter("acc_balance"));
		String accountBalanceDate = StrUtils.fString(request.getParameter("acc_balanceDate"));
		String landedcost=StrUtils.fString(request.getParameter("landedcost"));
		UserTransaction ut = null;
		CoaUtil coaUtil = new CoaUtil();
		CoaDAO coaDAO = new CoaDAO();
		DateUtils dateutils = new DateUtils();
		boolean accountCreated = false;
		String result = "";
		JSONObject resultJson = new JSONObject();
		Hashtable<String, String> accountHt = new Hashtable<>();
		accountHt.put("PLANT", plant);
		accountHt.put("ACCOUNTTYPE", accountType);
		accountHt.put("ACCOUNTDETAILTYPE", accountDetType);
		accountHt.put("ACCOUNT_NAME", accountName);
		accountHt.put("DESCRIPTION", description);
		accountHt.put("ISSUBACCOUNT", isSubAccount);
		accountHt.put("ISEXPENSEGST", isExpGstAccount);
		accountHt.put("SUBACCOUNTNAME", subAccount);
		accountHt.put("OPENINGBALANCE", accountBalance);
		accountHt.put("OPENINGBALANCEDATE", accountBalanceDate);
		accountHt.put("LANDEDCOSTCAL", landedcost);
		if(isSubAccount.equals("0")) {
			accountHt.put("CODE", ac_code_post);
		}else {
			accountHt.put("CODE", ac_code_post_sub);
			accountHt.put("SUB_CODE", sub_sub_code);
		}
		
		accountHt.put("ACCOUNT_CODE", ac_code_show);
		accountHt.put("CRAT", dateutils.getDateTime());
		accountHt.put("CRBY", username);
		accountHt.put("UPAT", dateutils.getDateTime());
		try {
			ut = DbBean.getUserTranaction();
			ut.begin();
			Hashtable<String, String> ht = new Hashtable<String, String>();
			ht.put("PLANT", plant);
			ht.put("ACCOUNTTYPE", accountType);
			ht.put("ACCOUNTDETAILTYPE", accountDetType);
			ht.put("ACCOUNT_NAME", accountName);
			ht.put("ISSUBACCOUNT", isSubAccount);
			ht.put("SUBACCOUNTNAME", subAccount);
			boolean chkaccount = coaDAO.isExists(ht, "FINCHARTOFACCOUNTS", plant);
			if (!chkaccount)
				accountCreated = coaUtil.addAccount(accountHt, plant);
			if (accountCreated) {
				if (Double.parseDouble(accountBalance) != 0) {
					String mainid = coaDAO.GetMainAccountId(accountType, plant);
					int mid = Integer.valueOf(mainid);
					if (mid == 1 || mid == 2 || mid == 3) {
						// String curDate = dateutils.getDate();
						String curency = StrUtils.fString((String) request.getSession().getAttribute("BASE_CURRENCY"))
								.trim();
						// Journal Entry
						JournalHeader journalHead = new JournalHeader();
						journalHead.setPLANT(plant);
						journalHead.setJOURNAL_DATE(accountBalanceDate);
						journalHead.setJOURNAL_STATUS("PUBLISHED");
						journalHead.setJOURNAL_TYPE("Cash");
						journalHead.setCURRENCYID(curency);
						journalHead.setTRANSACTION_TYPE("OPENINGBALANCE");
						journalHead.setTRANSACTION_ID(Integer.toString(0));
						journalHead.setSUB_TOTAL(Double.parseDouble(accountBalance));
						journalHead.setTOTAL_AMOUNT(Double.parseDouble(accountBalance));
						journalHead.setCRAT(dateutils.getDateTime());
						journalHead.setCRBY(username);

						List<JournalDetail> journalDetails = new ArrayList<>();

						if (mid == 1) {
							JournalDetail journalDetail = new JournalDetail();
							journalDetail.setPLANT(plant);
							JSONObject coaJson = coaDAO.getCOAByName(plant, accountName);
							System.out.println("Json" + coaJson.toString());
							journalDetail.setACCOUNT_ID(Integer.parseInt(coaJson.getString("id")));
							journalDetail.setACCOUNT_NAME(accountName);
							journalDetail.setDEBITS(Double.parseDouble(accountBalance));
							journalDetails.add(journalDetail);

							JournalDetail journalDetail1 = new JournalDetail();
							journalDetail1.setPLANT(plant);
							JSONObject coaJson1 = coaDAO.getCOAByName(plant, "Opening Balance Equity");
							System.out.println("Json" + coaJson1.toString());
							journalDetail1.setACCOUNT_ID(Integer.parseInt(coaJson1.getString("id")));
							journalDetail1.setACCOUNT_NAME("Opening Balance Equity");
							journalDetail1.setCREDITS(Double.parseDouble(accountBalance));
							journalDetails.add(journalDetail1);
						} else {
							JournalDetail journalDetail = new JournalDetail();
							journalDetail.setPLANT(plant);
							JSONObject coaJson = coaDAO.getCOAByName(plant, accountName);
							System.out.println("Json" + coaJson.toString());
							journalDetail.setACCOUNT_ID(Integer.parseInt(coaJson.getString("id")));
							journalDetail.setACCOUNT_NAME(accountName);
							journalDetail.setCREDITS(Double.parseDouble(accountBalance));
							journalDetails.add(journalDetail);

							JournalDetail journalDetail1 = new JournalDetail();
							journalDetail1.setPLANT(plant);
							JSONObject coaJson1 = coaDAO.getCOAByName(plant, "Opening Balance Equity");
							System.out.println("Json" + coaJson1.toString());
							journalDetail1.setACCOUNT_ID(Integer.parseInt(coaJson1.getString("id")));
							journalDetail1.setACCOUNT_NAME("Opening Balance Equity");
							journalDetail1.setDEBITS(Double.parseDouble(accountBalance));
							journalDetails.add(journalDetail1);
						}

						Journal journal = new Journal();
						journal.setJournalHeader(journalHead);
						journal.setJournalDetails(journalDetails);
						JournalService journalService = new JournalEntry();
						journalService.addJournal(journal, username);
						MovHisDAO movHisDao = new MovHisDAO();
						Hashtable jhtMovHis = new Hashtable();
						jhtMovHis.put(IDBConstants.PLANT, plant);
						jhtMovHis.put("DIRTYPE", TransactionConstants.CREATE_JOURNAL);	
						jhtMovHis.put(IDBConstants.TRAN_DATE, DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));														
						jhtMovHis.put(IDBConstants.ITEM, "");
						jhtMovHis.put(IDBConstants.QTY, "0.0");
						jhtMovHis.put("RECID", "");
						jhtMovHis.put(IDBConstants.MOVHIS_ORDNUM,journal.getJournalHeader().getTRANSACTION_TYPE()+" "+journal.getJournalHeader().getTRANSACTION_ID());
						jhtMovHis.put(IDBConstants.CREATED_BY, username);		
						jhtMovHis.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
						jhtMovHis.put("REMARKS","");
						movHisDao.insertIntoMovHis(jhtMovHis);
					}

				}
			}
			if (accountCreated) {
				DbBean.CommitTran(ut);
				resultJson.put("MESSAGE", "Account created successfully");
				resultJson.put("ACCOUNT_NAME", accountName);
				resultJson.put("STATUS", "SUCCESS");
			} else {
				DbBean.RollbackTran(ut);
				if (chkaccount)
					resultJson.put("MESSAGE", "Account already exists");
				else
					resultJson.put("MESSAGE", "Account creation failed");
				resultJson.put("STATUS", "FAIL");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultJson;
	}
	public JSONObject createExtendedType(HttpServletRequest request, HttpServletResponse response) {
		String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
		String username = StrUtils.fString((String) request.getSession().getAttribute("LOGIN_USER")).trim();
		String accountType = StrUtils.fString(request.getParameter("acc_type_extended"));
		String accountDetType = StrUtils.fString(request.getParameter("acc_det_type_extended"));
		String det_code = StrUtils.fString(request.getParameter("acc_code_extended"));
		String accDet_code = StrUtils.fString(request.getParameter("acc_code_show_extended"));
		UserTransaction ut = null;
		CoaUtil coaUtil = new CoaUtil();
		CoaDAO coaDAO = new CoaDAO();
		DateUtils dateutils = new DateUtils();
		int CreatedId = 0;
		String result = "";
		JSONObject resultJson = new JSONObject();
		Hashtable<String, String> accountHt = new Hashtable<>();
		accountHt.put("PLANT", plant);
		accountHt.put("ACCOUNTTYPEID", accountType);
		accountHt.put("ACCOUNTDETAILTYPE", accountDetType);
		accountHt.put("CODE", det_code);
		accountHt.put("ACCOUNT_CODE", accDet_code);
		accountHt.put("CRAT", dateutils.getDateTime());
		accountHt.put("CRBY", username);
		accountHt.put("UPAT", dateutils.getDateTime());
		try {
			ut = DbBean.getUserTranaction();
			ut.begin();
		
				CreatedId = coaUtil.addExtendedType(accountHt, plant);
			if (CreatedId>0) {
				DbBean.CommitTran(ut);
				resultJson.put("MESSAGE", "Extended type created successfully");
				resultJson.put("EXTENDEDTYPE", accDet_code+"  "+accountDetType);
				resultJson.put("CODE", det_code);
				resultJson.put("EXTENDEDID", CreatedId);
				resultJson.put("STATUS", "SUCCESS");
			} else {
				DbBean.RollbackTran(ut);
				resultJson.put("MESSAGE", "Extended type creation failed");
				resultJson.put("STATUS", "FAIL");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultJson;
	}
	public JSONObject readTable(HttpServletRequest request, HttpServletResponse response) {

		String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();

		CoaUtil coaUtil = new CoaUtil();
		return coaUtil.readTable(plant);
	}

	private JSONObject updateTable(HttpServletRequest request, HttpServletResponse response) {
		String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
		String accountId = StrUtils.fString((String) request.getParameter("acc_id"));
		String description = StrUtils.fString((String) request.getParameter("eacc_desc"));
		String account_type = StrUtils.fString((String) request.getParameter("eacc_type"));
		String account_det_type = StrUtils.fString((String) request.getParameter("eacc_det_type_id"));
		String accountName = StrUtils.fString((String) request.getParameter("eacc_name"));
		String account_is_sub = StrUtils.fString((String) request.getParameter("eacc_is_sub"));
		String account_is_exp_gst = StrUtils.fString((String) request.getParameter("eacc_is_exp_gst"));
		String account_subAcct = StrUtils.fString((String) request.getParameter("eacc_subAcct"));
		String account_balance = StrUtils.fString((String) request.getParameter("eacc_balance"));
		if(account_balance == null || account_balance.length() == 0) {
			account_balance="0";
		}
		String account_balanceDate = StrUtils.fString((String) request.getParameter("eacc_balanceDate"));
		String landedcost=StrUtils.fString(request.getParameter("editlandedcost"));
		String username = StrUtils.fString((String) request.getSession().getAttribute("LOGIN_USER")).trim();
		if (!account_is_sub.equalsIgnoreCase("1")) {
			if (account_is_sub.equalsIgnoreCase("on"))
				account_is_sub = "1";
			else
				account_is_sub = "0";
		}
		
		if (!account_is_exp_gst.equalsIgnoreCase("1")) {
			if (account_is_exp_gst.equalsIgnoreCase("on"))
				account_is_exp_gst = "1";
			else
				account_is_exp_gst = "0";
		}
		CoaUtil coaUtil = new CoaUtil();
		CoaDAO coaDAO = new CoaDAO();
		boolean accountupdated = false;
		JSONObject resultJson = new JSONObject();
		UserTransaction ut = null;
		try {
			ut = DbBean.getUserTranaction();
			ut.begin();
			Hashtable<String, String> ht = new Hashtable<String, String>();
			ht.put("PLANT", plant);
			ht.put("ACCOUNTTYPE", account_type);
			ht.put("ACCOUNTDETAILTYPE", account_det_type);
			ht.put("ACCOUNT_NAME", accountName);
			ht.put("ISSUBACCOUNT", account_is_sub);
			ht.put("SUBACCOUNTNAME", account_subAcct);
			boolean chkaccount = coaDAO.isExists(ht, "FINCHARTOFACCOUNTS", plant);
			if (chkaccount) {
				ArrayList listQry = new ArrayList();
				listQry = coaDAO.selectReport("select ID from " + plant + "_FINCHARTOFACCOUNTS ", ht, "");
				if (listQry.size() > 0) {
					Map lineArr = (Map) listQry.get(0);
					String lineid = (String) lineArr.get("ID");
					if (accountId.equalsIgnoreCase(lineid))
						chkaccount = false;
				}
			}
			if (!chkaccount)
				accountupdated = coaUtil.updateTable(plant, accountId, accountName, account_type, account_det_type,
						description, account_is_sub, account_subAcct, account_balance, account_balanceDate,landedcost,account_is_exp_gst);
			
			if (accountupdated) {
				
				if (Double.parseDouble(account_balance) != 0) {
					String mainid = coaDAO.GetMainAccountId(account_type, plant);
					int mid = Integer.valueOf(mainid);
					if (mid == 1 || mid == 2 || mid == 3) {
						DateUtils dateutils = new DateUtils();
						// String curDate = dateutils.getDate();
						String curency = StrUtils.fString((String) request.getSession().getAttribute("BASE_CURRENCY"))
								.trim();
						// Journal Entry
						JournalHeader journalHead = new JournalHeader();
						journalHead.setPLANT(plant);
						journalHead.setJOURNAL_DATE(account_balanceDate);
						journalHead.setJOURNAL_STATUS("PUBLISHED");
						journalHead.setJOURNAL_TYPE("Cash");
						journalHead.setCURRENCYID(curency);
						journalHead.setTRANSACTION_TYPE("OPENINGBALANCE");
						journalHead.setTRANSACTION_ID(Integer.toString(0));
						journalHead.setSUB_TOTAL(Double.parseDouble(account_balance));
						journalHead.setTOTAL_AMOUNT(Double.parseDouble(account_balance));
						journalHead.setCRAT(dateutils.getDateTime());
						journalHead.setCRBY(username);

						List<JournalDetail> journalDetails = new ArrayList<>();

						if (mid == 1) {
							JournalDetail journalDetail = new JournalDetail();
							journalDetail.setPLANT(plant);
							JSONObject coaJson = coaDAO.getCOAByName(plant, accountName);
							System.out.println("Json" + coaJson.toString());
							journalDetail.setACCOUNT_ID(Integer.parseInt(coaJson.getString("id")));
							journalDetail.setACCOUNT_NAME(accountName);
							journalDetail.setDEBITS(Double.parseDouble(account_balance));
							journalDetails.add(journalDetail);

							JournalDetail journalDetail1 = new JournalDetail();
							journalDetail1.setPLANT(plant);
							JSONObject coaJson1 = coaDAO.getCOAByName(plant, "Opening Balance Equity");
							System.out.println("Json" + coaJson1.toString());
							journalDetail1.setACCOUNT_ID(Integer.parseInt(coaJson1.getString("id")));
							journalDetail1.setACCOUNT_NAME("Opening Balance Equity");
							journalDetail1.setCREDITS(Double.parseDouble(account_balance));
							journalDetails.add(journalDetail1);
						} else {
							JournalDetail journalDetail = new JournalDetail();
							journalDetail.setPLANT(plant);
							JSONObject coaJson = coaDAO.getCOAByName(plant, accountName);
							System.out.println("Json" + coaJson.toString());
							journalDetail.setACCOUNT_ID(Integer.parseInt(coaJson.getString("id")));
							journalDetail.setACCOUNT_NAME(accountName);
							journalDetail.setCREDITS(Double.parseDouble(account_balance));
							journalDetails.add(journalDetail);

							JournalDetail journalDetail1 = new JournalDetail();
							journalDetail1.setPLANT(plant);
							JSONObject coaJson1 = coaDAO.getCOAByName(plant, "Opening Balance Equity");
							System.out.println("Json" + coaJson1.toString());
							journalDetail1.setACCOUNT_ID(Integer.parseInt(coaJson1.getString("id")));
							journalDetail1.setACCOUNT_NAME("Opening Balance Equity");
							journalDetail1.setDEBITS(Double.parseDouble(account_balance));
							journalDetails.add(journalDetail1);
						}

						Journal journal = new Journal();
						journal.setJournalHeader(journalHead);
						journal.setJournalDetails(journalDetails);
						JournalService journalService = new JournalEntry();
						journalService.addJournal(journal, username);
						MovHisDAO movHisDao = new MovHisDAO();
						Hashtable jhtMovHis = new Hashtable();
						jhtMovHis.put(IDBConstants.PLANT, plant);
						jhtMovHis.put("DIRTYPE", TransactionConstants.CREATE_JOURNAL);	
						jhtMovHis.put(IDBConstants.TRAN_DATE, DateUtils.getDateinyyyy_mm_dd(DateUtils.getDate()));														
						jhtMovHis.put(IDBConstants.ITEM, "");
						jhtMovHis.put(IDBConstants.QTY, "0.0");
						jhtMovHis.put("RECID", "");
						jhtMovHis.put(IDBConstants.MOVHIS_ORDNUM,journal.getJournalHeader().getTRANSACTION_TYPE()+" "+journal.getJournalHeader().getTRANSACTION_ID());
						jhtMovHis.put(IDBConstants.CREATED_BY, username);		
						jhtMovHis.put(IDBConstants.CREATED_AT, DateUtils.getDateTime());
						jhtMovHis.put("REMARKS","");
						movHisDao.insertIntoMovHis(jhtMovHis);
					}

				}
			}
			
			if (accountupdated) {
				DbBean.CommitTran(ut);
				resultJson.put("MESSAGE", "Account updated successfully");
				resultJson.put("ACCOUNT_NAME", accountName);
				resultJson.put("STATUS", "SUCCESS");
			} else {
				DbBean.RollbackTran(ut);
				if (chkaccount)
					resultJson.put("MESSAGE", "Account already exists");
				else
					resultJson.put("MESSAGE", "Account update failed");
				resultJson.put("STATUS", "FAIL");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultJson;
	}

	public JSONObject getGroupTypes(HttpServletRequest request, HttpServletResponse response) {

		String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
		String group = StrUtils.fString((String) request.getParameter("GROUP")).trim();
		JSONObject resultJson = new JSONObject();
		JSONArray jsonArray = new JSONArray();
		JSONArray jsonArrayErr = new JSONArray();
		CoaUtil coaUtil = new CoaUtil();
		List<Map<String, String>> listQry = coaUtil.getAccountGroup(plant, group);

		if (listQry.size() > 0) {
			for (int i = 0; i < listQry.size(); i++) {
				Map<String, String> m = listQry.get(i);
				JSONObject resultJsonInt = new JSONObject();
				resultJsonInt.put("GROUPID", m.get("GROUPID"));
				resultJsonInt.put("GROUPS", m.get("GROUPS"));
				resultJsonInt.put("TYPE", m.get("TYPE"));
				// resultJsonInt.put("CRBY", m.get("CRBY"));
				// resultJsonInt.put("UPAT", m.get("UPAT"));
				// resultJsonInt.put("UPBY", m.get("UPBY"));
				jsonArray.add(resultJsonInt);
			}
			resultJson.put("groups", jsonArray);
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
			// resultJson.put("groups", jsonArray);
			resultJson.put("errors", jsonArrayErr);
		}

		return resultJson;
	}

	public JSONObject readTableRecord(HttpServletRequest request, HttpServletResponse response) {

		String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
		String id = StrUtils.fString((String) request.getParameter("id"));
		CoaUtil coaUtil = new CoaUtil();
		return coaUtil.readTableRecord(plant, id);
	}

	public JSONObject getDataForSuggestion(HttpServletRequest request, HttpServletResponse response) {
		String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
		String name = StrUtils.fString((String) request.getParameter("NAME")).trim();
		JSONObject resultJson = new JSONObject();
		JSONArray jsonArray = new JSONArray();
		JSONArray jsonArrayErr = new JSONArray();
		CoaUtil coaUtil = new CoaUtil();
		resultJson = coaUtil.readTableRecordByName(plant, name);
		return resultJson;
	}

	public JSONObject createExpenseAccount(HttpServletRequest request, HttpServletResponse response) {
		String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
		String username = StrUtils.fString((String) request.getSession().getAttribute("LOGIN_USER")).trim();
		String accountType = StrUtils.fString(request.getParameter("acctypes"));
		String accountGroup = StrUtils.fString(request.getParameter("acc_group"));
		String acc_groupid = StrUtils.fString(request.getParameter("acc_groupid"));

		UserTransaction ut = null;
		CoaUtil coaUtil = new CoaUtil();
		DateUtils dateutils = new DateUtils();
		boolean accountCreated = false;
		String result = "";
		JSONObject resultJson = new JSONObject();

		acc_groupid = coaUtil.GetaccountGroupId(accountGroup, plant);
		if (acc_groupid.isEmpty()) {
			Hashtable<String, String> accountHt = new Hashtable<>();
			accountHt.put("PLANT", plant);
			accountHt.put("GROUPS", accountGroup);
			accountHt.put("CRAT", dateutils.getDateTime());
			accountHt.put("CRBY", username);
			accountHt.put("UPAT", dateutils.getDateTime());
			accountCreated = coaUtil.addAccountGroup(accountHt, plant);
			if (accountCreated)
				acc_groupid = coaUtil.GetaccountGroupId(accountGroup, plant);
		}

		// FINACCTYPE
		Hashtable<String, String> accountHt = new Hashtable<>();
		accountHt.put("PLANT", plant);
		accountHt.put("TYPE", accountType);
		accountHt.put("ACCGROUPID", acc_groupid);
		accountHt.put("CRAT", dateutils.getDateTime());
		accountHt.put("CRBY", username);
		accountHt.put("UPAT", dateutils.getDateTime());
		try {
			ut = DbBean.getUserTranaction();
			ut.begin();
			accountCreated = coaUtil.isExistsType(accountType, accountGroup, plant);
			if (!accountCreated)
				accountCreated = coaUtil.addAccountType(accountHt, plant);
			if (accountCreated) {
				DbBean.CommitTran(ut);
				resultJson.put("MESSAGE", "Account created successfully");
				resultJson.put("ACCOUNT_TYPE", accountType);
				resultJson.put("STATUS", "SUCCESS");
			} else {
				DbBean.RollbackTran(ut);
				resultJson.put("MESSAGE", "Fail to create Account ");
				resultJson.put("STATUS", "FAIL");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultJson;
	}

	public JSONObject getGroup(HttpServletRequest request, HttpServletResponse response) {

		String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
		String group = StrUtils.fString((String) request.getParameter("GROUP")).trim();
		JSONObject resultJson = new JSONObject();
		JSONArray jsonArray = new JSONArray();
		JSONArray jsonArrayErr = new JSONArray();
		CoaUtil coaUtil = new CoaUtil();
		List<Map<String, String>> listQry = coaUtil.getGroup(plant, group);

		if (listQry.size() > 0) {
			for (int i = 0; i < listQry.size(); i++) {
				Map<String, String> m = listQry.get(i);
				JSONObject resultJsonInt = new JSONObject();
				resultJsonInt.put("GROUPID", m.get("GROUPID"));
				resultJsonInt.put("GROUPS", m.get("GROUPS"));
				jsonArray.add(resultJsonInt);
			}
			resultJson.put("groups", jsonArray);
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
			resultJson.put("errors", jsonArrayErr);
		}

		return resultJson;
	}

	public JSONObject getAccountType(HttpServletRequest request, HttpServletResponse response) {

		String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
		JSONObject resultJson = new JSONObject();
		JSONArray groupArray = new JSONArray();
		CoaUtil coaUtil = new CoaUtil();
		List<Map<String, String>> listQry = coaUtil.getAccountType(plant);
		for (Map<String, String> map : listQry) {
			String text = map.get("text");
			String accode = map.get("ACCOUNT_CODE");
			map.put("text", accode+' '+text);
		}
		Map<String, List<Map<String, String>>> listGrouped = listQry.stream()
				.collect(Collectors.groupingBy(w -> w.get("MAINACCID")));
		Map<String, List<Map<String, String>>> sortedMap = new TreeMap<String, List<Map<String, String>>>(listGrouped);
		for (Map.Entry<String, List<Map<String, String>>> entry : sortedMap.entrySet()) {
			System.out.println("Item : " + entry.getKey() + " value : " + entry.getValue().get(0).get("MAINACCOUNT") + "\n");
			JSONObject groupName = new JSONObject();
			
			groupName.put("text", entry.getValue().get(0).get("MAINACCOUNT"));
			groupName.put("children", entry.getValue());
			groupArray.add(groupName);
		}
		resultJson.put("results", groupArray);

		return resultJson;
	}

	
	public JSONObject getAccountDetailType(HttpServletRequest request, HttpServletResponse response) {

		String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
		String group = StrUtils.fString((String) request.getParameter("GROUP")).trim();
		String type = StrUtils.fString((String) request.getParameter("TYPE")).trim();
		JSONObject resultJson = new JSONObject();
		JSONArray jsonArray = new JSONArray();
		JSONArray jsonArrayErr = new JSONArray();
		CoaUtil coaUtil = new CoaUtil();
		List<Map<String, String>> listQry = coaUtil.getAccountDetailType(plant, group, type);

		if (listQry.size() > 0) {
			for (int i = 0; i < listQry.size(); i++) {
				Map<String, String> m = listQry.get(i);
				JSONObject resultJsonInt = new JSONObject();
				resultJsonInt.put("ACCOUNTDETAILTYPE_ID", m.get("ACCOUNTDETAILTYPE_ID"));
				resultJsonInt.put("ACCOUNTDETAILTYPE", StrUtils.fString((String) m.get("ACCOUNTDETAILTYPE")));
				resultJsonInt.put("ACCOUNT_CODE", StrUtils.fString((String) m.get("ACCOUNT_CODE")));
				resultJsonInt.put("CODE", StrUtils.fString((String) m.get("CODE")));
				resultJsonInt.put("NAME_CODE", StrUtils.fString((String) m.get("ACCOUNT_CODE"))+' '+StrUtils.fString((String) m.get("ACCOUNTDETAILTYPE")));
				jsonArray.add(resultJsonInt);
			}
			resultJson.put("groups", jsonArray);
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
			resultJson.put("errors", jsonArrayErr);
		}

		return resultJson;
	}

	public JSONObject getSubAccountType(HttpServletRequest request, HttpServletResponse response) {

		String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
		String group = StrUtils.fString((String) request.getParameter("GROUP")).trim();
		String type = StrUtils.fString((String) request.getParameter("TYPE")).trim();
		System.out.println("Group Value" + group);
		JSONObject resultJson = new JSONObject();
		JSONArray jsonArray = new JSONArray();
		JSONArray jsonArrayErr = new JSONArray();
		CoaUtil coaUtil = new CoaUtil();
		List<Map<String, String>> listQry = coaUtil.getSubAccountType(plant, group,type);

		if (listQry.size() > 0) {
			for (int i = 0; i < listQry.size(); i++) {
				Map<String, String> m = listQry.get(i);
				JSONObject resultJsonInt = new JSONObject();
				/* String Name = m.get("ACCOUNT") +" : "+ m.get("ACCOUNTTYPE"); */
				String Name = m.get("ACCOUNT");
				resultJsonInt.put("ID", m.get("ID"));
				resultJsonInt.put("ACCOUNTNAME", m.get("ACCOUNT"));
				resultJsonInt.put("ACCOUNTTYPE", m.get("ACCOUNTTYPE"));
				jsonArray.add(resultJsonInt);
			}
			resultJson.put("groups", jsonArray);
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
			resultJson.put("errors", jsonArrayErr);
		}

		return resultJson;
	}

	public JSONObject getSubAccountTypeGrouped(HttpServletRequest request, HttpServletResponse response) {

		String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
		String module= StrUtils.fString(request.getParameter("module"));
		String QUERY= StrUtils.fString(request.getParameter("ITEM"));
		JSONObject resultJson = new JSONObject();
		JSONArray groupArray = new JSONArray();
		JSONArray childArray = new JSONArray();
		CoaDAO coadao = new CoaDAO();
		List<Map<String, String>> listQry = null;
		try {
			listQry = coadao.selectSubAccountTypeList(plant,QUERY);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Map<String, List<Map<String, String>>> listGrouped=null;
		/**Separate functionality by module name**/
		if(module.equalsIgnoreCase("billPayment"))
		{
			String[] filterArray = {"1", "7","9","3"};
			listGrouped=listQry.stream()
					.filter(x->Arrays.stream(filterArray)
					.anyMatch(e -> e.equals(x.get("ACCOUNTTYPEID"))))
					.collect(Collectors.groupingBy(w -> w.get("ACCOUNTTYPE")));
			
		}
		else if(module.equalsIgnoreCase("expensepaiddrop"))
		{
			String[] filterArray = {"3"};
			String[] detailArray= {"8","9"};
			listQry=listQry.stream().filter(y->Arrays.stream(detailArray).anyMatch(e -> e.equals(y.get("DETAILID"))))
					.collect(Collectors.toList());
			listGrouped=listQry.stream()
					.filter(x->Arrays.stream(filterArray)
					.anyMatch(e -> e.equals(x.get("ACCOUNTTYPEID"))))
					.collect(Collectors.groupingBy(w -> w.get("DETAILID")));
			
		}
		else if(module.equalsIgnoreCase("billpaymentpaidthrough"))
		{
			String[] filterArray = {"3"};
			String[] detailArray= {"8","9"};
			listQry=listQry.stream().filter(y->Arrays.stream(detailArray).anyMatch(e -> e.equals(y.get("DETAILID"))))
					.collect(Collectors.toList());
			listGrouped=listQry.stream()
					.filter(x->Arrays.stream(filterArray)
					.anyMatch(e -> e.equals(x.get("ACCOUNTTYPEID"))))
					.collect(Collectors.groupingBy(w -> w.get("DETAILID")));
			
		}
		else if(module.equalsIgnoreCase("billpaymentvoucheraccount"))
		{
			String[] filterArray = {"1","2","3","4","5","6","7","8","9","10","11"};
			listQry.removeIf(value -> value.get("DETAILID").equalsIgnoreCase("18"));
			listQry.removeIf(value -> value.get("DETAILID").equalsIgnoreCase("8"));
			//listQry.removeIf(value -> value.get("DETAILID").equalsIgnoreCase("9"));
			
			listGrouped=listQry.stream()
					.filter(x->Arrays.stream(filterArray)
					.anyMatch(e -> e.equals(x.get("ACCOUNTTYPEID"))))
					.collect(Collectors.groupingBy(w -> w.get("ACCOUNTTYPE")));
			
			
			
			
		}
		else if(module.equalsIgnoreCase("invoicepaymentvoucheraccount"))
		{
			String[] filterArray = {"1","2","3","4","5","6","7","8","9","10","11"};
			listQry.removeIf(value -> value.get("DETAILID").equalsIgnoreCase("7"));
			listQry.removeIf(value -> value.get("DETAILID").equalsIgnoreCase("8"));
			listQry.removeIf(value -> value.get("DETAILID").equalsIgnoreCase("9"));
			
			listGrouped=listQry.stream()
					.filter(x->Arrays.stream(filterArray)
					.anyMatch(e -> e.equals(x.get("ACCOUNTTYPEID"))))
					.collect(Collectors.groupingBy(w -> w.get("ACCOUNTTYPE")));
			
			
			
			
		}
		else if(module.equalsIgnoreCase("invoicepaymentdepositto"))
		{
			String[] filterArray = {"3"};
			String[] detailArray= {"8","9"};
			listQry=listQry.stream().filter(y->Arrays.stream(detailArray).anyMatch(e -> e.equals(y.get("DETAILID"))))
					.collect(Collectors.toList());
			listGrouped=listQry.stream()
					.filter(x->Arrays.stream(filterArray)
					.anyMatch(e -> e.equals(x.get("ACCOUNTTYPEID"))))
					.collect(Collectors.groupingBy(w -> w.get("DETAILID")));
			
			
			
			
		}
		else if(module.equalsIgnoreCase("reconciliationaccounts"))
		{
			String[] filterArray = {"3"};
			String[] detailArray= {"8"};
			listQry=listQry.stream().filter(y->Arrays.stream(detailArray).anyMatch(e -> e.equals(y.get("DETAILID"))))
					.collect(Collectors.toList());
			listGrouped=listQry.stream()
					.filter(x->Arrays.stream(filterArray)
					.anyMatch(e -> e.equals(x.get("ACCOUNTTYPEID"))))
					.collect(Collectors.groupingBy(w -> w.get("DETAILID")));
			
			
			
			
		}
		else if(module.equalsIgnoreCase("expenseaccount"))
		{
			String[] filterArray = {"6","10","11"};
			listGrouped=listQry.stream()
					.filter(x->Arrays.stream(filterArray)
					.anyMatch(e -> e.equals(x.get("ACCOUNTTYPEID"))))
					.collect(Collectors.groupingBy(w -> w.get("ACCOUNTTYPE")));
		}
		else if(module.equalsIgnoreCase("billaccount"))
		{

			String[] filterArray = {"3"};
			String[] detailArray= {"6"};
			listQry=listQry.stream().filter(y->Arrays.stream(detailArray).anyMatch(e -> e.equals(y.get("DETAILID"))))
					.collect(Collectors.toList());
			listGrouped=listQry.stream()
					.filter(x->Arrays.stream(filterArray)
					.anyMatch(e -> e.equals(x.get("ACCOUNTTYPEID"))))
					.collect(Collectors.groupingBy(w -> w.get("MAINACCID")));
		}
		else if(module.equalsIgnoreCase("claimaccount"))
		{

			String[] filterArray = {"6"};
			String[] detailArray= {"20"};
			listQry=listQry.stream().filter(y->Arrays.stream(detailArray).anyMatch(e -> e.equals(y.get("DETAILID"))))
					.collect(Collectors.toList());
			listGrouped=listQry.stream()
					.filter(x->Arrays.stream(filterArray)
					.anyMatch(e -> e.equals(x.get("ACCOUNTTYPEID"))))
					.collect(Collectors.groupingBy(w -> w.get("MAINACCID")));
		}
		else if(module.equalsIgnoreCase("claim_account"))
		{
			String[] filterArray = {"38"};
			listGrouped=listQry.stream()
					.filter(x->Arrays.stream(filterArray)
					.anyMatch(e -> e.equals(x.get("SUBACCOUNTNAME"))))
					.collect(Collectors.groupingBy(w -> w.get("ACCOUNTTYPE")));
			
		}
		else if(module.equalsIgnoreCase("purchasecreditaccount"))
		{

			String[] filterArray = {"3","11"};
			String[] detailArray= {"6","30","31","32","33","34"};
			listQry=listQry.stream().filter(y->Arrays.stream(detailArray).anyMatch(e -> e.equals(y.get("DETAILID"))))
					.collect(Collectors.toList());
			listGrouped=listQry.stream()
					.filter(x->Arrays.stream(filterArray)
					.anyMatch(e -> e.equals(x.get("ACCOUNTTYPEID"))))
					.collect(Collectors.groupingBy(w -> w.get("MAINACCID")));
		}
		else if(module.equalsIgnoreCase("salescreditaccount"))
		{

			String[] filterArray = {"8"};
			String[] detailArray= {"26","25"};
			listQry=listQry.stream().filter(y->Arrays.stream(detailArray).anyMatch(e -> e.equals(y.get("DETAILID"))))
					.collect(Collectors.toList());
			listGrouped=listQry.stream()
					.filter(x->Arrays.stream(filterArray)
					.anyMatch(e -> e.equals(x.get("ACCOUNTTYPEID"))))
					.collect(Collectors.groupingBy(w -> w.get("MAINACCID")));
		}
		else if(module.equalsIgnoreCase("expense"))
		{

			String[] filterArray = {"3","4","7","10","12","14"};
			listGrouped=listQry.stream()
					.filter(x->Arrays.stream(filterArray)
					.anyMatch(e -> e.equals(x.get("ACCOUNTTYPEID"))))
					.collect(Collectors.groupingBy(w -> w.get("ACCOUNTTYPE")));
		}
		else if(module.equalsIgnoreCase("contraaccount"))
		{

			String[] filterArray = {"3"};
			String[] detailArray= {"8","9"};
			listQry=listQry.stream().filter(y->Arrays.stream(detailArray).anyMatch(e -> e.equals(y.get("DETAILID"))))
					.collect(Collectors.toList());
			listGrouped=listQry.stream()
					.filter(x->Arrays.stream(filterArray)
					.anyMatch(e -> e.equals(x.get("ACCOUNTTYPEID"))))
					.collect(Collectors.groupingBy(w -> w.get("ACCOUNTTYPE")));
			
			/*String[] filterArrayDet = {"Bank","Cash in hand","Petty Cash"};
			listGrouped=listQry.stream()
					.filter(x->Arrays.stream(filterArrayDet)
					.anyMatch(e -> e.equals(x.get("text"))))
					.collect(Collectors.groupingBy(w -> w.get("ACCOUNTTYPE")));*/
		}
		else if(module.equalsIgnoreCase("journalaccount"))
		{	
			String[] filterArray = {"1","2","3","4","5","6","7","8","9","10","11","12","13"};
		//	listQry.removeIf(value -> value.get("DETAILID").equalsIgnoreCase("8"));
			//listQry.removeIf(value -> value.get("DETAILID").equalsIgnoreCase("9"));
			
			listGrouped=listQry.stream()
					.filter(x->Arrays.stream(filterArray)
					.anyMatch(e -> e.equals(x.get("ACCOUNTTYPEID"))))
					.collect(Collectors.groupingBy(w -> w.get("ACCOUNTTYPE")));
			
			/*listQry.removeIf(value -> value.containsValue("Bank"));
			listQry.removeIf(value -> value.containsValue("Cash in hand"));
			listQry.removeIf(value -> value.containsValue("Petty Cash"));
			listGrouped=listQry.stream().collect(Collectors.groupingBy(w -> w.get("MAINACCID")));*/
		}else if(module.equalsIgnoreCase("incomesummaryfilter"))
		{

			String[] filterArray = {"9"};
			listGrouped=listQry.stream()
					.filter(x->Arrays.stream(filterArray)
					.anyMatch(e -> e.equals(x.get("ACCOUNTTYPEID"))))
					.collect(Collectors.groupingBy(w -> w.get("ACCOUNTTYPE")));
			
			/*String[] filterArrayDet = {"Bank","Cash in hand","Petty Cash"};
			listGrouped=listQry.stream()
					.filter(x->Arrays.stream(filterArrayDet)
					.anyMatch(e -> e.equals(x.get("text"))))
					.collect(Collectors.groupingBy(w -> w.get("ACCOUNTTYPE")));*/
		}
		
		else if(module.equalsIgnoreCase("Expensesummaryfilter"))
		{

			String[] filterArray = {"11"};
			listGrouped=listQry.stream()
					.filter(x->Arrays.stream(filterArray)
					.anyMatch(e -> e.equals(x.get("ACCOUNTTYPEID"))))
					.collect(Collectors.groupingBy(w -> w.get("ACCOUNTTYPE")));
			
			/*String[] filterArrayDet = {"Bank","Cash in hand","Petty Cash"};
			listGrouped=listQry.stream()
					.filter(x->Arrays.stream(filterArrayDet)
					.anyMatch(e -> e.equals(x.get("text"))))
					.collect(Collectors.groupingBy(w -> w.get("ACCOUNTTYPE")));*/
		}
		else
		{
			listGrouped=listQry.stream().collect(Collectors.groupingBy(w -> w.get("MAINACCID")));
		}
		
		for (Map.Entry<String, List<Map<String, String>>> entry : listGrouped.entrySet()) {
			//System.out.println("Item : " + entry.getKey() + " value : " + entry.getValue() + "\n");
			JSONObject childName = new JSONObject();
			for(Map<String, String> item:entry.getValue())
			{
				boolean isSub="1".equals(item.get("ISSUBACCOUNT"));
				//System.out.println(item.get("text")+"My Sub...."+isSub);
				childName.put("text",item.get("ACCOUNT_CODE")+" "+item.get("text"));
				childName.put("id", item.get("id"));
				childName.put("issub", isSub);
				childName.put("accountname",item.get("text"));
				if(isSub)
				{
					String accName=coadao.getSubAccName(item.get("SUBACCOUNTNAME"), plant);
					childName.put("sub", accName);
				}
				
				childName.put("type", item.get("ACCOUNTDET_CODE")+" "+item.get("ACCOUNTDETAILTYPE"));
				childName.put("acctype", item.get("ACCOUNTTYPE"));
				childName.put("ACCOUNT_CODE", item.get("ACCOUNT_CODE"));
				childName.put("CODE", item.get("CODE"));
				childName.put("ISEXPENSEGST", item.get("ISEXPENSEGST"));
				childArray.add(childName);
			}
			/*
			 * JSONObject groupName = new JSONObject();
			 * 
			 * groupName.put("text", entry.getKey()); groupName.put("children", childArray);
			 * groupArray.add(groupName);
			 */
		}
		resultJson.put("results", childArray);
		return resultJson;
	}
	
	public JSONObject getSubAccountTypeSummary(HttpServletRequest request, HttpServletResponse response) {

		String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
		JSONObject resultJson = new JSONObject();
		JSONArray groupArray = new JSONArray();
		JSONArray CoaArray = new JSONArray();
		CoaDAO coadao = new CoaDAO();
		List<Map<String, String>> listQry = null;
		try {
			listQry = coadao.selectSubAccountTypeList(plant,"");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Map<String, List<Map<String, String>>> listGrouped = listQry.stream()
				.collect(Collectors.groupingBy(w -> w.get("ACCOUNTTYPE")));
		
		Map<Integer, Object> FinalList = new TreeMap<Integer, Object>();
		for (Map.Entry<String, List<Map<String, String>>> entry : listGrouped.entrySet()) {
			//System.out.println("Item : " + entry.getKey() + " value : " + entry.getValue() + "\n");
			JSONObject childName = new JSONObject();
			Map<String, Object> parentList = new HashMap<String, Object>();
			Map<String, Object> sortedParentList = new TreeMap<String, Object>();
			for(Map<String, String> item:entry.getValue())
			{
				
				JSONObject newNode = new JSONObject();
				boolean isSub="1".equals(item.get("ISSUBACCOUNT"));
				//System.out.println(item.get("text")+"My Sub...."+isSub);
				
				
				if(isSub) {
					newNode.put("id",item.get("id"));
					newNode.put("accountcode",item.get("ACCOUNT_CODE"));
					newNode.put("name",item.get("text"));
					newNode.put("type", entry.getKey());
					newNode.put("detailtype", item.get("ACCOUNTDETAILTYPE"));
					//String accName=coadao.getSubAccName(item.get("SUBACCOUNTNAME"), plant);
					if(parentList.containsKey(item.get("SUBACCOUNTNAME")))
					{
						JSONObject parent=(JSONObject) parentList.get(item.get("SUBACCOUNTNAME"));
						//System.out.println("Get Children:"+parent.toString());
						//System.out.println("Has Children:"+parent.isNullObject());
						if(parent.get("_children")!=null)
						{
							//parent.get("_children").toString().isEmpty();
							if(parent.has("_children"))
							{
								JSONArray childArray=parent.getJSONArray("_children");
								childArray.add(newNode);
								parent.put("_children", childArray);
							}
								
							
						}
						else
						{
							JSONArray childArray = new JSONArray();
							childArray.add(newNode);
							parent.put("_children", childArray);
						}
						
						parentList.replace(item.get("SUBACCOUNTNAME"), parent);
					}
					else
					{
						//JSONObject parent=(JSONObject) parentList.get(item.get("id"));
						
						for (Entry<String, Object> tt : parentList.entrySet()) {
							//System.out.println("Item : " + tt.getKey() + " Count : " + tt.getValue());
							JSONObject childNode=(JSONObject) tt.getValue();
							if(checkSubAccountForParent(tt.getKey(),childNode,item,newNode,parentList))
							{
								parentList.replace(item.get("SUBACCOUNTNAME"), childNode);
								break;
							}
						}
						
					}
					
					
					//parentNode.replace(arg0, arg1)
				}
				else
				{
					newNode.put("id",item.get("id"));
					newNode.put("accountcode",item.get("ACCOUNT_CODE"));
					newNode.put("name",item.get("text"));
					newNode.put("type", entry.getKey());
					newNode.put("detailtype", item.get("ACCOUNTDETAILTYPE"));
					parentList.put(item.get("id"), newNode);
				}
				  
				
			}
			for(Map.Entry<String, Object> coaitem:parentList.entrySet())
			{
				FinalList.put(Integer.parseInt(coaitem.getKey()), coaitem.getValue());
				//CoaArray.add(coaitem.getValue());
			}
			
		}
		for(Map.Entry<Integer, Object> coaitemnew:FinalList.entrySet())
		{
			CoaArray.add(coaitemnew.getValue());
		}
		resultJson.put("results", CoaArray);
		return resultJson;
	}
	private Boolean checkSubAccountForParent(String key,JSONObject accObj,Map<String, String> item,JSONObject newNode,Map<String, Object> parentList)
	{
		if(accObj.get("_children")!=null)
		{
			//parent.get("_children").toString().isEmpty();
			if(accObj.has("_children"))
			{
				JSONArray childArray=accObj.getJSONArray("_children");
				for(int i=0;i<childArray.size();i++)
				{
					JSONObject childNode=childArray.getJSONObject(i);
					if(childNode.get("id").equals(item.get("SUBACCOUNTNAME")))
					{
						if(childNode.get("_children")!=null)
						{
							//parent.get("_children").toString().isEmpty();
							if(childNode.has("_children"))
							{
								JSONArray newArray=childNode.getJSONArray("_children");
								newArray.add(newNode);
								childNode.put("_children", newArray);
							}
								
							
						}
						else
						{
							JSONArray newArray = new JSONArray();
							newArray.add(newNode);
							childNode.put("_children", newArray);
						}
						//parentList.replace(item.get("SUBACCOUNTNAME"), parent);
						//System.out.println("Item Found..............!"+childNode.get("id").toString());
						return true;
					}
					else
					{
						//System.out.println("Item Not Found..............!"+childNode.get("id").toString());
						checkSubAccountForParent(key,childNode,item,newNode,parentList);
					}
				}
				//childArray.add(newNode);
				//parent.put("_children", childArray);
			}
			else
			{
				return false;
			}
				
			
		}
		return false;
	}
	
	
	public JSONObject getSubAccountTypeGroupedFilter(HttpServletRequest request, HttpServletResponse response) {

		String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
		JSONObject resultJson = new JSONObject();
		JSONArray groupArray = new JSONArray();
		JSONArray childArray = new JSONArray();
		CoaDAO coadao = new CoaDAO();
		List<Map<String, String>> listQry = null;
		try {
			String whereQuery="a.ACCOUNTTYPE=2 OR a.ACCOUNTTYPE=3";
			listQry = coadao.selectSubAccountTypeByFilter(whereQuery, plant, "order by a.ACCOUNTTYPE desc");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Map<String, List<Map<String, String>>> listGrouped = listQry.stream()
				.collect(Collectors.groupingBy(w -> w.get("ACCOUNTTYPE")));
		for (Map.Entry<String, List<Map<String, String>>> entry : listGrouped.entrySet()) {
			//System.out.println("Item : " + entry.getKey() + " value : " + entry.getValue() + "\n");
			JSONObject childName = new JSONObject();
			for(Map<String, String> item:entry.getValue())
			{
				boolean isSub="1".equals(item.get("ISSUBACCOUNT"));
				//System.out.println(item.get("text")+"My Sub...."+isSub);
				childName.put("text",item.get("text"));
				childName.put("id", item.get("id"));
				childName.put("issub", isSub);
				if(isSub)
				{
					String accName=coadao.getSubAccName(item.get("SUBACCOUNTNAME"), plant);
					childName.put("sub", accName);
				}
				
				childName.put("type", entry.getKey());
				childArray.add(childName);
			}
			/*
			 * JSONObject groupName = new JSONObject();
			 * 
			 * groupName.put("text", entry.getKey()); groupName.put("children", childArray);
			 * groupArray.add(groupName);
			 */
		}
		resultJson.put("results", childArray);
		return resultJson;
	}

	
	public JSONObject getcheckAccountCode(HttpServletRequest request, HttpServletResponse response) {
		String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
		String accountCode = StrUtils.fString((String) request.getParameter("AccountCode")).trim();
		JSONObject resultJson = new JSONObject();
		CoaDAO coaDAO = new CoaDAO();
		try {
			boolean status1 = coaDAO.isExistsAccountCodeAG(plant, accountCode);
			boolean status2 = coaDAO.isExistsAccountCodeAT(plant, accountCode);
			boolean status3 = coaDAO.isExistsAccountCodeCOA(plant, accountCode);
			if(status1 || status2 || status3 ) {
				resultJson.put("results", true);
			}else {
				resultJson.put("results", false);
			}
		}catch (Exception e) {
			resultJson.put("results", false);
		}
		return resultJson;
	}
	
	
	
	
	
	public JSONObject getAccountCode(HttpServletRequest request, HttpServletResponse response) {
		String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
		String accountCode = StrUtils.fString((String) request.getParameter("AccountCode")).trim();
		String agid = StrUtils.fString((String) request.getParameter("Atid")).trim();
		String atid = StrUtils.fString((String) request.getParameter("Adtid")).trim();
		String atcode = "";
		JSONObject resultJson = new JSONObject();
		CoaDAO coaDAO = new CoaDAO();
		try {
			List<Map<String, String>> listQry = coaDAO.getMaxAccoutCode(plant, agid, atid);
			String maxid = "";
			if(listQry.size() > 0) {
				for (int i = 0; i < listQry.size(); i++) {
					Map<String, String> m = listQry.get(i);
					maxid = m.get("CODE");
				}
				if(maxid == null) {
					atcode = "01";
				}else {
					String countst = maxid.replaceAll(accountCode, "");
					int count = Integer.valueOf(countst);
					atcode = String.valueOf(count+1);
					if(atcode.length() == 1) {
						atcode = "0"+atcode;
					}
				}
				
				
			}else {
				atcode = "01";
			}
			 
			resultJson.put("results", atcode);

		}catch (Exception e) {
			resultJson.put("results", atcode);
		}
		return resultJson;
	}
	
	public JSONObject getAccountDETCode(HttpServletRequest request, HttpServletResponse response) {
		String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
		String accountCode = StrUtils.fString((String) request.getParameter("AccountCode")).trim();
		String agid = StrUtils.fString((String) request.getParameter("Atid")).trim();
		String atcode = "";
		JSONObject resultJson = new JSONObject();
		CoaDAO coaDAO = new CoaDAO();
		try {
			List<Map<String, String>> listQry = coaDAO.getMaxAccoutDETCode(plant, agid);
			String maxid = "";
			if(listQry.size() > 0) {
				for (int i = 0; i < listQry.size(); i++) {
					Map<String, String> m = listQry.get(i);
					maxid = m.get("CODE");
				}
				
				String countst = maxid.replaceAll(accountCode, "");
				int count = Integer.valueOf(countst);
				atcode = String.valueOf(count+1);
				/*if(atcode.length() == 1) {
					atcode = atcode+"00";
				}else if(atcode.length() == 2) {
					atcode = atcode+"0";
				}*/
			}else {
				atcode = "1";
			}
			 
			resultJson.put("results", atcode);

		}catch (Exception e) {
			resultJson.put("results", atcode);
		}
		return resultJson;
	}
	
	public JSONObject getSubAccountCode(HttpServletRequest request, HttpServletResponse response) {
		String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
		String SID = StrUtils.fString((String) request.getParameter("SID")).trim();
		String Acode = StrUtils.fString((String) request.getParameter("ACODE")).trim();
		JSONObject resultJson = new JSONObject();
		CoaDAO coaDAO = new CoaDAO();
		try {
			List<Map<String, String>> subaccount = coaDAO.getMaxSubCode(plant, SID);
			String scode ="";
			String atcode ="";
			if(subaccount.size() > 0) {
				for (int i = 0; i < subaccount.size(); i++) {
					Map<String, String> m = subaccount.get(i);
					scode = m.get("CODE");
				}
				
				if(scode == null) {
					atcode = "01";
				}else {
					int count = Integer.valueOf(scode);
					atcode = String.valueOf(count+1);
					if(atcode.length() == 1) {
						atcode = "0"+atcode;
					}
				}
				
				
			}else {
				atcode = "01";
			}
			
			/*int count = subaccount.size();
			String scount = String.valueOf(count+1);
			if(scount.length() == 1) {
				scount = "0"+scount;
			}*/
			String accountcode = Acode +"-"+atcode;
			resultJson.put("results", accountcode);
			resultJson.put("scode", atcode);
		}catch (Exception e) {
			resultJson.put("results", "0");
			resultJson.put("scode", "0");
		}
		return resultJson;
	}
}
