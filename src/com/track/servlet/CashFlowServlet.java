package com.track.servlet;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.json.JSONException;

import com.track.constants.MLoggerConstant;
import com.track.dao.CoaDAO;
import com.track.dao.JournalDAO;
import com.track.dao.PlantMstDAO;
import com.track.db.object.BalanceSheet;
import com.track.db.object.CashFlow;
import com.track.db.object.Journal;
import com.track.db.object.JournalDetail;
import com.track.db.object.Ledger;
import com.track.db.object.LedgerDetails;
import com.track.db.object.ProfitLoss;
import com.track.service.JournalService;
import com.track.service.LedgerService;
import com.track.service.ReportService;
import com.track.serviceImplementation.JournalEntry;
import com.track.serviceImplementation.LedgerServiceImpl;
import com.track.serviceImplementation.ReportServiceImpl;
import com.track.util.IMLogger;
import com.track.util.StrUtils;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@WebServlet("/CashFlowServlet")
public class CashFlowServlet extends HttpServlet implements IMLogger {
	
	private static final long serialVersionUID = 1L;
	private boolean printLog = MLoggerConstant.CashFlowServlet_PRINTPLANTMASTERLOG;
	private boolean printInfo = MLoggerConstant.CashFlowServlet_PRINTPLANTMASTERINFO;
	String action = "";
	private JournalService journalService = new JournalEntry();
	private LedgerService ledgerSerice = new LedgerServiceImpl();

	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		action = StrUtils.fString(req.getParameter("action")).trim();
		String plant = StrUtils.fString((String) req.getSession().getAttribute("PLANT")).trim();
		String user = StrUtils.fString((String) req.getSession().getAttribute("LOGIN_USER")).trim();
		String type = "ALL";

		if (action.equalsIgnoreCase("getcashflow1")) {
			String fromDate = StrUtils.fString(req.getParameter("fromDate")).trim();
			String toDate = StrUtils.fString(req.getParameter("toDate")).trim();
			//String accounttypes = StrUtils.fString(req.getParameter("accounttypes")).trim();
			List<Journal> journalList = null;
			JSONArray jarray = new JSONArray();
			Map<Integer, CashFlow> cashFlowArr0 = new Hashtable<Integer, CashFlow>();
			Map<Integer, CashFlow> cashFlowArr1 = new Hashtable<Integer, CashFlow>();
			Map<Integer, CashFlow> cashFlowArr2 = new Hashtable<Integer, CashFlow>();
			Map<Integer, CashFlow> cashFlowArr3 = new Hashtable<Integer, CashFlow>();
			Map<Integer, CashFlow> cashFlowArr4 = new Hashtable<Integer, CashFlow>();
			try {
				// journalList=journalService.getAllJournalHeader(plant, user, type);
				// journalList=journalService.getJournalsByDateWithoutAttach(plant, fromDate,
				// toDate);
				
				
				CashFlow cashFlow1 = new CashFlow();
				cashFlow1.setAccount_id(0);
				cashFlow1.setAccount_name("Profit");
				cashFlow1.setAccount_type("Profit");
				// profitLoss.setAccount_type(coaJson.getString("account_type_name"));
				cashFlow1.setExtended_type("");
				cashFlow1.setMain_account("");
				cashFlow1.setNet_credit(0.0);
				cashFlow1.setNet_debit(0.0);
				cashFlow1.setTotal(getNetProfit(plant,fromDate,toDate));
				cashFlowArr0.put(0, cashFlow1);
				

				cashFlowArr0.forEach((k, v) -> {
					jarray.add(v);
				});

				Set<Integer> AccountId1 = new HashSet<Integer>();
				String accounttypes= "('32')";
				List<JournalDetail> journalDetails1 = journalService.getJournalDetailsByAccountDetailTypeAndDATE(plant,
						fromDate, toDate, accounttypes);
				if (journalDetails1.size() > 0) {
					for (JournalDetail journalDet : journalDetails1) {
						if (!AccountId1.contains(journalDet.getACCOUNT_ID())) {
							AccountId1.add(journalDet.getACCOUNT_ID());
							CoaDAO coaDAO = new CoaDAO();

							JSONObject coaFullDetail = coaDAO.CoaFullRecord(plant,
									String.valueOf(journalDet.getACCOUNT_ID()));
							JSONObject coaJson = (JSONObject) coaFullDetail.get("results");
							// jarray.add(coaFullDetail.get("results"));
							System.out.println("CoaJson" + coaJson);
							if (coaJson != null) {
								CashFlow cashFlow = new CashFlow();
								cashFlow.setAccount_id(journalDet.getACCOUNT_ID());
								String accountcode = coaDAO.GetAccountCodeByName(coaJson.getString("account_name"), plant);
								cashFlow.setAccount_name(accountcode+" "+coaJson.getString("account_name"));
								int detid = Integer.valueOf(coaJson.getString("account_det_id"));
								cashFlow.setAccount_type("Depreciation");
								// profitLoss.setAccount_type(coaJson.getString("account_type_name"));
								cashFlow.setExtended_type(coaJson.getString("account_det_type"));
								cashFlow.setMain_account(coaJson.getString("mainaccname"));
								cashFlow.setNet_credit(journalDet.getCREDITS());
								cashFlow.setNet_debit(journalDet.getDEBITS());
								cashFlowArr1.put(journalDet.getACCOUNT_ID(), cashFlow);
							}

							// trialBalArr.put(coaJson.getString("ACCOUNT_NAME"), journalDet.get)
						} else {
							CashFlow cashflow = cashFlowArr1.get(journalDet.getACCOUNT_ID());
							if (cashflow != null) {
								Double debit = cashflow.getNet_debit() + journalDet.getDEBITS();
								Double credit = cashflow.getNet_credit() + journalDet.getCREDITS();
								cashflow.setNet_debit(debit);
								cashflow.setNet_credit(credit);
								cashFlowArr1.replace(journalDet.getACCOUNT_ID(), cashflow);
							}

						}
					}
				}else {
					CashFlow cashFlow = new CashFlow();
					cashFlow.setAccount_id(0);
					cashFlow.setAccount_name("-");
					cashFlow.setAccount_type("Depreciation");
					// profitLoss.setAccount_type(coaJson.getString("account_type_name"));
					cashFlow.setExtended_type("");
					cashFlow.setMain_account("");
					cashFlow.setNet_credit(0.0);
					cashFlow.setNet_debit(0.0);
					cashFlowArr1.put(0, cashFlow);
				}

				cashFlowArr1.forEach((k, v) -> {
					String accounttype = v.getAccount_type().trim();
					Double total = 0.00;
					//total = v.getNet_credit() - v.getNet_debit();
					total = v.getNet_debit() - v.getNet_credit();
					v.setTotal(total);
					cashFlowArr1.replace(k, v);
				});

				cashFlowArr1.forEach((k, v) -> {
					jarray.add(v);
				});
				
				
				
				Set<Integer> AccountId2 = new HashSet<Integer>();
				accounttypes= "('31')";
				List<JournalDetail> journalDetails2 = journalService.getJournalDetailsByAccountDetailTypeAndDATE(plant,
						fromDate, toDate, accounttypes);
				if (journalDetails2.size() > 0) {
					for (JournalDetail journalDet : journalDetails2) {
						if (!AccountId2.contains(journalDet.getACCOUNT_ID())) {
							AccountId2.add(journalDet.getACCOUNT_ID());
							CoaDAO coaDAO = new CoaDAO();

							JSONObject coaFullDetail = coaDAO.CoaFullRecord(plant,
									String.valueOf(journalDet.getACCOUNT_ID()));
							JSONObject coaJson = (JSONObject) coaFullDetail.get("results");
							// jarray.add(coaFullDetail.get("results"));
							System.out.println("CoaJson" + coaJson);
							if (coaJson != null) {
								CashFlow cashFlow = new CashFlow();
								cashFlow.setAccount_id(journalDet.getACCOUNT_ID());
								String accountcode = coaDAO.GetAccountCodeByName(coaJson.getString("account_name"), plant);
								cashFlow.setAccount_name(accountcode+" "+coaJson.getString("account_name"));
								//cashFlow.setAccount_name(coaJson.getString("account_name"));
								int detid = Integer.valueOf(coaJson.getString("account_det_id"));
								cashFlow.setAccount_type("Interest on Bank borrowings / Borrowed Funds");
								// profitLoss.setAccount_type(coaJson.getString("account_type_name"));
								cashFlow.setExtended_type(coaJson.getString("account_det_type"));
								cashFlow.setMain_account(coaJson.getString("mainaccname"));
								cashFlow.setNet_credit(journalDet.getCREDITS());
								cashFlow.setNet_debit(journalDet.getDEBITS());
								cashFlowArr2.put(journalDet.getACCOUNT_ID(), cashFlow);
							}

							// trialBalArr.put(coaJson.getString("ACCOUNT_NAME"), journalDet.get)
						} else {
							CashFlow cashflow = cashFlowArr2.get(journalDet.getACCOUNT_ID());
							if (cashflow != null) {
								Double debit = cashflow.getNet_debit() + journalDet.getDEBITS();
								Double credit = cashflow.getNet_credit() + journalDet.getCREDITS();
								cashflow.setNet_debit(debit);
								cashflow.setNet_credit(credit);
								cashFlowArr2.replace(journalDet.getACCOUNT_ID(), cashflow);
							}

						}
					}
				}else {
					CashFlow cashFlow = new CashFlow();
					cashFlow.setAccount_id(0);
					cashFlow.setAccount_name("-");
					cashFlow.setAccount_type("Interest on Bank borrowings / Borrowed Funds");
					// profitLoss.setAccount_type(coaJson.getString("account_type_name"));
					cashFlow.setExtended_type("");
					cashFlow.setMain_account("");
					cashFlow.setNet_credit(0.0);
					cashFlow.setNet_debit(0.0);
					cashFlowArr2.put(0, cashFlow);
				}

				cashFlowArr2.forEach((k, v) -> {
					String accounttype = v.getAccount_type().trim();
					Double total = 0.00;
					//total = v.getNet_credit() - v.getNet_debit();
					total = v.getNet_debit() - v.getNet_credit();
					v.setTotal(total);
					cashFlowArr2.replace(k, v);
				});

				cashFlowArr2.forEach((k, v) -> {
					jarray.add(v);
				});
				
				
				
				Set<Integer> AccountId3 = new HashSet<Integer>();
				accounttypes= "('27')";
				List<JournalDetail> journalDetails3 = journalService.getJournalDetailsByAccountDetailTypeAndDATE(plant,
						fromDate, toDate, accounttypes);
				if (journalDetails3.size() > 0) {
					for (JournalDetail journalDet : journalDetails3) {
						if (!AccountId3.contains(journalDet.getACCOUNT_ID())) {
							AccountId3.add(journalDet.getACCOUNT_ID());
							CoaDAO coaDAO = new CoaDAO();

							JSONObject coaFullDetail = coaDAO.CoaFullRecord(plant,
									String.valueOf(journalDet.getACCOUNT_ID()));
							JSONObject coaJson = (JSONObject) coaFullDetail.get("results");
							// jarray.add(coaFullDetail.get("results"));
							System.out.println("CoaJson" + coaJson);
							if (coaJson != null) {
								CashFlow cashFlow = new CashFlow();
								cashFlow.setAccount_id(journalDet.getACCOUNT_ID());
								String accountcode = coaDAO.GetAccountCodeByName(coaJson.getString("account_name"), plant);
								cashFlow.setAccount_name(accountcode+" "+coaJson.getString("account_name"));
								//cashFlow.setAccount_name(coaJson.getString("account_name"));
								int detid = Integer.valueOf(coaJson.getString("account_det_id"));
								cashFlow.setAccount_type("Bank Interest");
								// profitLoss.setAccount_type(coaJson.getString("account_type_name"));
								cashFlow.setExtended_type(coaJson.getString("account_det_type"));
								cashFlow.setMain_account(coaJson.getString("mainaccname"));
								cashFlow.setNet_credit(journalDet.getCREDITS());
								cashFlow.setNet_debit(journalDet.getDEBITS());
								cashFlowArr3.put(journalDet.getACCOUNT_ID(), cashFlow);
							}

							// trialBalArr.put(coaJson.getString("ACCOUNT_NAME"), journalDet.get)
						} else {
							CashFlow cashflow = cashFlowArr3.get(journalDet.getACCOUNT_ID());
							if (cashflow != null) {
								Double debit = cashflow.getNet_debit() + journalDet.getDEBITS();
								Double credit = cashflow.getNet_credit() + journalDet.getCREDITS();
								cashflow.setNet_debit(debit);
								cashflow.setNet_credit(credit);
								cashFlowArr3.replace(journalDet.getACCOUNT_ID(), cashflow);
							}

						}
					}
				}else {
					CashFlow cashFlow = new CashFlow();
					cashFlow.setAccount_id(0);
					cashFlow.setAccount_name("-");
					cashFlow.setAccount_type("Bank Interest");
					// profitLoss.setAccount_type(coaJson.getString("account_type_name"));
					cashFlow.setExtended_type("");
					cashFlow.setMain_account("");
					cashFlow.setNet_credit(0.0);
					cashFlow.setNet_debit(0.0);
					cashFlowArr3.put(0, cashFlow);
				}

				cashFlowArr3.forEach((k, v) -> {
					String accounttype = v.getAccount_type().trim();
					Double total = 0.00;
					//total = v.getNet_credit() - v.getNet_debit();
					total = v.getNet_debit() - v.getNet_credit();
					v.setTotal(total);
					cashFlowArr3.replace(k, v);
				});

				cashFlowArr3.forEach((k, v) -> {
					jarray.add(v);
				});
				
				
				Set<Integer> AccountId4 = new HashSet<Integer>();
				accounttypes= "('34')";
				List<JournalDetail> journalDetails4 = journalService.getJournalDetailsByAccountDetailTypeAndDATE(plant,
						fromDate, toDate, accounttypes);
				if (journalDetails4.size() > 0) {
					for (JournalDetail journalDet : journalDetails4) {
						if (!AccountId4.contains(journalDet.getACCOUNT_ID())) {
							AccountId4.add(journalDet.getACCOUNT_ID());
							CoaDAO coaDAO = new CoaDAO();

							JSONObject coaFullDetail = coaDAO.CoaFullRecord(plant,
									String.valueOf(journalDet.getACCOUNT_ID()));
							JSONObject coaJson = (JSONObject) coaFullDetail.get("results");
							// jarray.add(coaFullDetail.get("results"));
							System.out.println("CoaJson" + coaJson);
							if (coaJson != null) {
								CashFlow cashFlow = new CashFlow();
								cashFlow.setAccount_id(journalDet.getACCOUNT_ID());
								String accountcode = coaDAO.GetAccountCodeByName(coaJson.getString("account_name"), plant);
								cashFlow.setAccount_name(accountcode+" "+coaJson.getString("account_name"));
								//cashFlow.setAccount_name(coaJson.getString("account_name"));
								int detid = Integer.valueOf(coaJson.getString("account_det_id"));
								cashFlow.setAccount_type("Dividend");
								// profitLoss.setAccount_type(coaJson.getString("account_type_name"));
								cashFlow.setExtended_type(coaJson.getString("account_det_type"));
								cashFlow.setMain_account(coaJson.getString("mainaccname"));
								cashFlow.setNet_credit(journalDet.getCREDITS());
								cashFlow.setNet_debit(journalDet.getDEBITS());
								cashFlowArr4.put(journalDet.getACCOUNT_ID(), cashFlow);
							}

							// trialBalArr.put(coaJson.getString("ACCOUNT_NAME"), journalDet.get)
						} else {
							CashFlow cashflow = cashFlowArr4.get(journalDet.getACCOUNT_ID());
							if (cashflow != null) {
								Double debit = cashflow.getNet_debit() + journalDet.getDEBITS();
								Double credit = cashflow.getNet_credit() + journalDet.getCREDITS();
								cashflow.setNet_debit(debit);
								cashflow.setNet_credit(credit);
								cashFlowArr4.replace(journalDet.getACCOUNT_ID(), cashflow);
							}

						}
					}
				}else {
					CashFlow cashFlow = new CashFlow();
					cashFlow.setAccount_id(0);
					cashFlow.setAccount_name("-");
					cashFlow.setAccount_type("Dividend");
					// profitLoss.setAccount_type(coaJson.getString("account_type_name"));
					cashFlow.setExtended_type("");
					cashFlow.setMain_account("");
					cashFlow.setNet_credit(0.0);
					cashFlow.setNet_debit(0.0);
					cashFlowArr4.put(0, cashFlow);
				}

				cashFlowArr4.forEach((k, v) -> {
					String accounttype = v.getAccount_type().trim();
					Double total = 0.00;
					//total = v.getNet_credit() - v.getNet_debit();
					total = v.getNet_debit() - v.getNet_credit();
					v.setTotal(total);
					cashFlowArr4.replace(k, v);
				});

				cashFlowArr4.forEach((k, v) -> {
					jarray.add(v);
				});
				
				/*List<Ledger> ledger1 = getopenbalandclosebal(plant, fromDate, toDate, "('3')", "('9')");
				Map<Integer, CashFlow> cashFlowArr5 = new Hashtable<Integer, CashFlow>();
				int l1 = 0;
				for (Ledger ledger : ledger1) {
					CashFlow cashFlow = new CashFlow();
					cashFlow.setAccount_id(0);
					cashFlow.setAccount_name(ledger.getACCOUNT());
					cashFlow.setAccount_type("(Increase)/Decrease in Current Assets");
					cashFlow.setExtended_type("");
					cashFlow.setMain_account("");
					cashFlow.setNet_credit(0.0);
					cashFlow.setNet_debit(0.0);
					double bal = ledger.getOPENING_BALANCE() - ledger.getCLOSING_BALANCE();
					cashFlow.setTotal(bal);
					cashFlowArr5.put(l1, cashFlow);
					l1++;
				}
				
				cashFlowArr5.forEach((k, v) -> {
					jarray.add(v);
				});*/
				
				// jarray.add(trialBalArr.values().toArray());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			resp.setContentType("application/json");
			resp.setCharacterEncoding("UTF-8");
			resp.getWriter().write(jarray.toString());
			resp.getWriter().flush();
			resp.getWriter().close();
		} else if (action.equalsIgnoreCase("getcashflow2")) {
			String fromDate = StrUtils.fString(req.getParameter("fromDate")).trim();
			String toDate = StrUtils.fString(req.getParameter("toDate")).trim();
			CoaDAO coaDAO = new CoaDAO();
			//String accounttypes = StrUtils.fString(req.getParameter("accounttypes")).trim();
			List<Journal> journalList = null;
			JSONArray jarray = new JSONArray();
			Map<Integer, CashFlow> cashFlowArr0 = new Hashtable<Integer, CashFlow>();
			Map<Integer, CashFlow> cashFlowArr1 = new Hashtable<Integer, CashFlow>();
			Map<Integer, CashFlow> cashFlowArr2 = new Hashtable<Integer, CashFlow>();
			Map<Integer, CashFlow> cashFlowArr3 = new Hashtable<Integer, CashFlow>();
			Map<Integer, CashFlow> cashFlowArr4 = new Hashtable<Integer, CashFlow>();
			Map<Integer, CashFlow> cashFlowArr5 = new Hashtable<Integer, CashFlow>();
			try {
				
				
				List<Ledger> ledger1 = getopenbalandclosebal(plant, fromDate, toDate, "('3')", "('9')");
				int l1 = 0;
				for (Ledger ledger : ledger1) {
					CashFlow cashFlow = new CashFlow();
					cashFlow.setAccount_id(0);
					String accountcode = coaDAO.GetAccountCodeByName(ledger.getACCOUNT(), plant);
					cashFlow.setAccount_name(accountcode+" "+ledger.getACCOUNT());
					cashFlow.setAccount_type("(Increase)/Decrease in Current Assets");
					cashFlow.setExtended_type("");
					cashFlow.setMain_account("");
					cashFlow.setNet_credit(0.0);
					cashFlow.setNet_debit(0.0);
					double bal = ledger.getOPENING_BALANCE() - ledger.getCLOSING_BALANCE();
					cashFlow.setTotal(bal);
					cashFlowArr1.put(l1, cashFlow);
					l1++;
				}
				if(ledger1.size() == 0) {
					CashFlow cashFlow = new CashFlow();
					cashFlow.setAccount_id(0);
					cashFlow.setAccount_name("-");
					cashFlow.setAccount_type("(Increase)/Decrease in Current Assets");
					cashFlow.setExtended_type("");
					cashFlow.setMain_account("");
					cashFlow.setNet_credit(0.0);
					cashFlow.setNet_debit(0.0);
					cashFlowArr1.put(0, cashFlow);
				}
				cashFlowArr1.forEach((k, v) -> {
					jarray.add(v);
				});
				
				List<Ledger> ledger2 = getopenbalandclosebal(plant, fromDate, toDate, "('2')", "('4')");
				int l2 = 0;
				for (Ledger ledger : ledger2) {
					CashFlow cashFlow = new CashFlow();
					cashFlow.setAccount_id(0);
					String accountcode = coaDAO.GetAccountCodeByName(ledger.getACCOUNT(), plant);
					cashFlow.setAccount_name(accountcode+" "+ledger.getACCOUNT());
					//cashFlow.setAccount_name(ledger.getACCOUNT());
					cashFlow.setAccount_type("(Increase)/Decrease in Non-Current Assets");
					cashFlow.setExtended_type("");
					cashFlow.setMain_account("");
					cashFlow.setNet_credit(0.0);
					cashFlow.setNet_debit(0.0);
					double bal = ledger.getOPENING_BALANCE() - ledger.getCLOSING_BALANCE();
					cashFlow.setTotal(bal);
					cashFlowArr2.put(l2, cashFlow);
					l2++;
				}
				if(ledger2.size() == 0) {
					CashFlow cashFlow = new CashFlow();
					cashFlow.setAccount_id(0);
					cashFlow.setAccount_name("-");
					cashFlow.setAccount_type("(Increase)/Decrease in Non-Current Assets");
					cashFlow.setExtended_type("");
					cashFlow.setMain_account("");
					cashFlow.setNet_credit(0.0);
					cashFlow.setNet_debit(0.0);
					cashFlowArr2.put(0, cashFlow);
				}
				cashFlowArr2.forEach((k, v) -> {
					jarray.add(v);
				});
				
				List<Ledger> ledger3 = getopenbalandclosebal(plant, fromDate, toDate, "('6')", "('0')");
				int l3 = 0;
				for (Ledger ledger : ledger3) {
					CashFlow cashFlow = new CashFlow();
					cashFlow.setAccount_id(0);
					String accountcode = coaDAO.GetAccountCodeByName(ledger.getACCOUNT(), plant);
					cashFlow.setAccount_name(accountcode+" "+ledger.getACCOUNT());
					//cashFlow.setAccount_name(ledger.getACCOUNT());
					cashFlow.setAccount_type("(Decrease)/Increase in Current Liabilities");
					cashFlow.setExtended_type("");
					cashFlow.setMain_account("");
					cashFlow.setNet_credit(0.0);
					cashFlow.setNet_debit(0.0);
					double bal = ledger.getOPENING_BALANCE() - ledger.getCLOSING_BALANCE();
					cashFlow.setTotal(bal);
					cashFlowArr3.put(l3, cashFlow);
					l3++;
				}
				if(ledger3.size() == 0) {
					CashFlow cashFlow = new CashFlow();
					cashFlow.setAccount_id(0);
					cashFlow.setAccount_name("-");
					cashFlow.setAccount_type("(Decrease)/Increase in Current Liabilities");
					cashFlow.setExtended_type("");
					cashFlow.setMain_account("");
					cashFlow.setNet_credit(0.0);
					cashFlow.setNet_debit(0.0);
					cashFlowArr3.put(0, cashFlow);
				}
				cashFlowArr3.forEach((k, v) -> {
					jarray.add(v);
				});
				
				List<Ledger> ledger4 = getopenbalandclosebal(plant, fromDate, toDate, "('5')", "('0')");
				int l4 = 0;
				for (Ledger ledger : ledger4) {
					CashFlow cashFlow = new CashFlow();
					cashFlow.setAccount_id(0);
					String accountcode = coaDAO.GetAccountCodeByName(ledger.getACCOUNT(), plant);
					cashFlow.setAccount_name(accountcode+" "+ledger.getACCOUNT());
					//cashFlow.setAccount_name(ledger.getACCOUNT());
					cashFlow.setAccount_type("(Decrease)/Increase in Provisions");
					cashFlow.setExtended_type("");
					cashFlow.setMain_account("");
					cashFlow.setNet_credit(0.0);
					cashFlow.setNet_debit(0.0);
					double bal = ledger.getOPENING_BALANCE() - ledger.getCLOSING_BALANCE();
					cashFlow.setTotal(bal);
					cashFlowArr4.put(l4, cashFlow);
					l4++;
				}
				if(ledger4.size() == 0) {
					CashFlow cashFlow = new CashFlow();
					cashFlow.setAccount_id(0);
					cashFlow.setAccount_name("-");
					cashFlow.setAccount_type("(Decrease)/Increase in Provisions");
					cashFlow.setExtended_type("");
					cashFlow.setMain_account("");
					cashFlow.setNet_credit(0.0);
					cashFlow.setNet_debit(0.0);
					cashFlowArr4.put(0, cashFlow);
				}
				cashFlowArr4.forEach((k, v) -> {
					jarray.add(v);
				});
				
				List<Ledger> ledger5 = getopenbalandclosebal(plant, fromDate, toDate, "('4')", "('14')");
				int l5 = 0;
				for (Ledger ledger : ledger5) {
					CashFlow cashFlow = new CashFlow();
					cashFlow.setAccount_id(0);
					String accountcode = coaDAO.GetAccountCodeByName(ledger.getACCOUNT(), plant);
					cashFlow.setAccount_name(accountcode+" "+ledger.getACCOUNT());
					//cashFlow.setAccount_name(ledger.getACCOUNT());
					cashFlow.setAccount_type("(Decrease)/Increase in Non-current liabilities");
					cashFlow.setExtended_type("");
					cashFlow.setMain_account("");
					cashFlow.setNet_credit(0.0);
					cashFlow.setNet_debit(0.0);
					double bal = ledger.getOPENING_BALANCE() - ledger.getCLOSING_BALANCE();
					cashFlow.setTotal(bal);
					cashFlowArr5.put(l5, cashFlow);
					l5++;
				}
				if(ledger5.size() == 0) {
					CashFlow cashFlow = new CashFlow();
					cashFlow.setAccount_id(0);
					cashFlow.setAccount_name("-");
					cashFlow.setAccount_type("(Decrease)/Increase in Non-current liabilities");
					cashFlow.setExtended_type("");
					cashFlow.setMain_account("");
					cashFlow.setNet_credit(0.0);
					cashFlow.setNet_debit(0.0);
					cashFlowArr5.put(0, cashFlow);
				}
				cashFlowArr5.forEach((k, v) -> {
					jarray.add(v);
				});
				
				// jarray.add(trialBalArr.values().toArray());
			} catch (Exception e) {
				e.printStackTrace();
			}

			resp.setContentType("application/json");
			resp.setCharacterEncoding("UTF-8");
			resp.getWriter().write(jarray.toString());
			resp.getWriter().flush();
			resp.getWriter().close();
		}  else if (action.equalsIgnoreCase("getcashflow3")) {
			String fromDate = StrUtils.fString(req.getParameter("fromDate")).trim();
			String toDate = StrUtils.fString(req.getParameter("toDate")).trim();
			CoaDAO coaDAO = new CoaDAO();
			//String accounttypes = StrUtils.fString(req.getParameter("accounttypes")).trim();
			List<Journal> journalList = null;
			JSONArray jarray = new JSONArray();
			Map<Integer, CashFlow> cashFlowArr0 = new Hashtable<Integer, CashFlow>();
			Map<Integer, CashFlow> cashFlowArr1 = new Hashtable<Integer, CashFlow>();
			Map<Integer, CashFlow> cashFlowArr2 = new Hashtable<Integer, CashFlow>();
			Map<Integer, CashFlow> cashFlowArr3 = new Hashtable<Integer, CashFlow>();
			Map<Integer, CashFlow> cashFlowArr4 = new Hashtable<Integer, CashFlow>();
			Map<Integer, CashFlow> cashFlowArr5 = new Hashtable<Integer, CashFlow>();
			try {
				
				
				List<Ledger> ledger1 = getopenbalandclosebalexttype(plant, fromDate, toDate, "('1')");
				int l1 = 0;
				for (Ledger ledger : ledger1) {
					CashFlow cashFlow = new CashFlow();
					cashFlow.setAccount_id(0);
					String accountcode = coaDAO.GetAccountCodeByName(ledger.getACCOUNT(), plant);
					cashFlow.setAccount_name(accountcode+" "+ledger.getACCOUNT());
					//cashFlow.setAccount_name(ledger.getACCOUNT());
					cashFlow.setAccount_type("Purchase of Fixed Assets");
					cashFlow.setExtended_type("");
					cashFlow.setMain_account("");
					cashFlow.setNet_credit(0.0);
					cashFlow.setNet_debit(0.0);
					double bal = ledger.getOPENING_BALANCE() - ledger.getCLOSING_BALANCE();
					cashFlow.setTotal(bal);
					cashFlowArr1.put(l1, cashFlow);
					l1++;
				}
				if(ledger1.size() == 0) {
					CashFlow cashFlow = new CashFlow();
					cashFlow.setAccount_id(0);
					cashFlow.setAccount_name("-");
					cashFlow.setAccount_type("Purchase of Fixed Assets");
					cashFlow.setExtended_type("");
					cashFlow.setMain_account("");
					cashFlow.setNet_credit(0.0);
					cashFlow.setNet_debit(0.0);
					cashFlowArr1.put(0, cashFlow);
				}
				cashFlowArr1.forEach((k, v) -> {
					jarray.add(v);
				});
				
				List<Ledger> ledger2 = getopenbalandclosebalexttype(plant, fromDate, toDate, "('27')");
				int l2 = 0;
				for (Ledger ledger : ledger2) {
					CashFlow cashFlow = new CashFlow();
					cashFlow.setAccount_id(0);
					String accountcode = coaDAO.GetAccountCodeByName(ledger.getACCOUNT(), plant);
					cashFlow.setAccount_name(accountcode+" "+ledger.getACCOUNT());
					//cashFlow.setAccount_name(ledger.getACCOUNT());
					cashFlow.setAccount_type("Bank Interest");
					cashFlow.setExtended_type("");
					cashFlow.setMain_account("");
					cashFlow.setNet_credit(0.0);
					cashFlow.setNet_debit(0.0);
					double bal = ledger.getOPENING_BALANCE() - ledger.getCLOSING_BALANCE();
					cashFlow.setTotal(bal);
					cashFlowArr2.put(l2, cashFlow);
					l2++;
				}
				if(ledger2.size() == 0) {
					CashFlow cashFlow = new CashFlow();
					cashFlow.setAccount_id(0);
					cashFlow.setAccount_name("-");
					cashFlow.setAccount_type("Bank Interest");
					cashFlow.setExtended_type("");
					cashFlow.setMain_account("");
					cashFlow.setNet_credit(0.0);
					cashFlow.setNet_debit(0.0);
					cashFlowArr2.put(0, cashFlow);
				}
				cashFlowArr2.forEach((k, v) -> {
					jarray.add(v);
				});
				
				List<Ledger> ledger3 = getopenbalandclosebalexttype(plant, fromDate, toDate, "('4')");
				int l3 = 0;
				for (Ledger ledger : ledger3) {
					CashFlow cashFlow = new CashFlow();
					cashFlow.setAccount_id(0);
					//cashFlow.setAccount_name(ledger.getACCOUNT());
					String accountcode = coaDAO.GetAccountCodeByName(ledger.getACCOUNT(), plant);
					cashFlow.setAccount_name(accountcode+" "+ledger.getACCOUNT());
					cashFlow.setAccount_type("Long term investment");
					cashFlow.setExtended_type("");
					cashFlow.setMain_account("");
					cashFlow.setNet_credit(0.0);
					cashFlow.setNet_debit(0.0);
					double bal = ledger.getOPENING_BALANCE() - ledger.getCLOSING_BALANCE();
					cashFlow.setTotal(bal);
					cashFlowArr3.put(l3, cashFlow);
					l3++;
				}
				if(ledger3.size() == 0) {
					CashFlow cashFlow = new CashFlow();
					cashFlow.setAccount_id(0);
					cashFlow.setAccount_name("-");
					cashFlow.setAccount_type("Long term investment");
					cashFlow.setExtended_type("");
					cashFlow.setMain_account("");
					cashFlow.setNet_credit(0.0);
					cashFlow.setNet_debit(0.0);
					cashFlowArr3.put(0, cashFlow);
				}
				cashFlowArr3.forEach((k, v) -> {
					jarray.add(v);
				});
				
				// jarray.add(trialBalArr.values().toArray());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			resp.setContentType("application/json");
			resp.setCharacterEncoding("UTF-8");
			resp.getWriter().write(jarray.toString());
			resp.getWriter().flush();
			resp.getWriter().close();
		} else if (action.equalsIgnoreCase("getcashflow4")) {
			String fromDate = StrUtils.fString(req.getParameter("fromDate")).trim();
			String toDate = StrUtils.fString(req.getParameter("toDate")).trim();
			CoaDAO coaDAO = new CoaDAO();
			//String accounttypes = StrUtils.fString(req.getParameter("accounttypes")).trim();
			List<Journal> journalList = null;
			JSONArray jarray = new JSONArray();
			Map<Integer, CashFlow> cashFlowArr0 = new Hashtable<Integer, CashFlow>();
			Map<Integer, CashFlow> cashFlowArr1 = new Hashtable<Integer, CashFlow>();
			Map<Integer, CashFlow> cashFlowArr2 = new Hashtable<Integer, CashFlow>();
			Map<Integer, CashFlow> cashFlowArr3 = new Hashtable<Integer, CashFlow>();
			Map<Integer, CashFlow> cashFlowArr4 = new Hashtable<Integer, CashFlow>();
			Map<Integer, CashFlow> cashFlowArr5 = new Hashtable<Integer, CashFlow>();
			try {
				
				
				List<Ledger> ledger1 = getopenbalandclosebalexttype(plant, fromDate, toDate, "('23')");
				int l1 = 0;
				for (Ledger ledger : ledger1) {
					CashFlow cashFlow = new CashFlow();
					cashFlow.setAccount_id(0);
					//cashFlow.setAccount_name(ledger.getACCOUNT());
					String accountcode = coaDAO.GetAccountCodeByName(ledger.getACCOUNT(), plant);
					cashFlow.setAccount_name(accountcode+" "+ledger.getACCOUNT());
					cashFlow.setAccount_type("Introduction of share capital");
					cashFlow.setExtended_type("");
					cashFlow.setMain_account("");
					cashFlow.setNet_credit(0.0);
					cashFlow.setNet_debit(0.0);
					double bal = ledger.getOPENING_BALANCE() - ledger.getCLOSING_BALANCE();
					cashFlow.setTotal(bal);
					cashFlowArr1.put(l1, cashFlow);
					l1++;
				}
				if(ledger1.size() == 0) {
					CashFlow cashFlow = new CashFlow();
					cashFlow.setAccount_id(0);
					cashFlow.setAccount_name("-");
					cashFlow.setAccount_type("Introduction of share capital");
					cashFlow.setExtended_type("");
					cashFlow.setMain_account("");
					cashFlow.setNet_credit(0.0);
					cashFlow.setNet_debit(0.0);
					cashFlowArr1.put(0, cashFlow);
				}
				cashFlowArr1.forEach((k, v) -> {
					jarray.add(v);
				});
				
				List<Ledger> ledger2 = getopenbalandclosebalexttype(plant, fromDate, toDate, "('14')");
				int l2 = 0;
				for (Ledger ledger : ledger2) {
					CashFlow cashFlow = new CashFlow();
					cashFlow.setAccount_id(0);
					//cashFlow.setAccount_name(ledger.getACCOUNT());
					String accountcode = coaDAO.GetAccountCodeByName(ledger.getACCOUNT(), plant);
					cashFlow.setAccount_name(accountcode+" "+ledger.getACCOUNT());
					cashFlow.setAccount_type("Bank Borrowings / Borrowed Funds");
					cashFlow.setExtended_type("");
					cashFlow.setMain_account("");
					cashFlow.setNet_credit(0.0);
					cashFlow.setNet_debit(0.0);
					double bal = ledger.getOPENING_BALANCE() - ledger.getCLOSING_BALANCE();
					cashFlow.setTotal(bal);
					cashFlowArr2.put(l2, cashFlow);
					l2++;
				}
				if(ledger2.size() == 0) {
					CashFlow cashFlow = new CashFlow();
					cashFlow.setAccount_id(0);
					cashFlow.setAccount_name("-");
					cashFlow.setAccount_type("Bank Borrowings / Borrowed Funds");
					cashFlow.setExtended_type("");
					cashFlow.setMain_account("");
					cashFlow.setNet_credit(0.0);
					cashFlow.setNet_debit(0.0);
					cashFlowArr2.put(0, cashFlow);
				}
				cashFlowArr2.forEach((k, v) -> {
					jarray.add(v);
				});
				
				List<Ledger> ledger3 = getopenbalandclosebalexttype(plant, fromDate, toDate, "('31')");
				int l3 = 0;
				for (Ledger ledger : ledger3) {
					CashFlow cashFlow = new CashFlow();
					cashFlow.setAccount_id(0);
					//cashFlow.setAccount_name(ledger.getACCOUNT());
					String accountcode = coaDAO.GetAccountCodeByName(ledger.getACCOUNT(), plant);
					cashFlow.setAccount_name(accountcode+" "+ledger.getACCOUNT());
					cashFlow.setAccount_type("Interest on Bank Borrowings / Borrowed Funds");
					cashFlow.setExtended_type("");
					cashFlow.setMain_account("");
					cashFlow.setNet_credit(0.0);
					cashFlow.setNet_debit(0.0);
					double bal = ledger.getOPENING_BALANCE() - ledger.getCLOSING_BALANCE();
					cashFlow.setTotal(bal);
					cashFlowArr3.put(l3, cashFlow);
					l3++;
				}
				if(ledger3.size() == 0) {
					CashFlow cashFlow = new CashFlow();
					cashFlow.setAccount_id(0);
					cashFlow.setAccount_name("-");
					cashFlow.setAccount_type("Interest on Bank Borrowings / Borrowed Funds");
					cashFlow.setExtended_type("");
					cashFlow.setMain_account("");
					cashFlow.setNet_credit(0.0);
					cashFlow.setNet_debit(0.0);
					cashFlowArr3.put(0, cashFlow);
				}
				cashFlowArr3.forEach((k, v) -> {
					jarray.add(v);
				});
				
				List<Ledger> ledger4 = getopenbalandclosebalexttype(plant, fromDate, toDate, "('34')");
				int l4 = 0;
				for (Ledger ledger : ledger4) {
					CashFlow cashFlow = new CashFlow();
					cashFlow.setAccount_id(0);
					//cashFlow.setAccount_name(ledger.getACCOUNT());
					String accountcode = coaDAO.GetAccountCodeByName(ledger.getACCOUNT(), plant);
					cashFlow.setAccount_name(accountcode+" "+ledger.getACCOUNT());
					cashFlow.setAccount_type("Dividend disbursed");
					cashFlow.setExtended_type("");
					cashFlow.setMain_account("");
					cashFlow.setNet_credit(0.0);
					cashFlow.setNet_debit(0.0);
					double bal = ledger.getOPENING_BALANCE() - ledger.getCLOSING_BALANCE();
					cashFlow.setTotal(bal);
					cashFlowArr4.put(l4, cashFlow);
					l4++;
				}
				if(ledger4.size() == 0) {
					CashFlow cashFlow = new CashFlow();
					cashFlow.setAccount_id(0);
					cashFlow.setAccount_name("-");
					cashFlow.setAccount_type("Dividend disbursed");
					cashFlow.setExtended_type("");
					cashFlow.setMain_account("");
					cashFlow.setNet_credit(0.0);
					cashFlow.setNet_debit(0.0);
					cashFlowArr4.put(0, cashFlow);
				}
				cashFlowArr4.forEach((k, v) -> {
					jarray.add(v);
				});
				
				
				
				// jarray.add(trialBalArr.values().toArray());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			resp.setContentType("application/json");
			resp.setCharacterEncoding("UTF-8");
			resp.getWriter().write(jarray.toString());
			resp.getWriter().flush();
			resp.getWriter().close();
		} else if (action.equalsIgnoreCase("getcashflow5")) {
			String fromDate = StrUtils.fString(req.getParameter("fromDate")).trim();
			String toDate = StrUtils.fString(req.getParameter("toDate")).trim();
			//String accounttypes = StrUtils.fString(req.getParameter("accounttypes")).trim();
			List<Journal> journalList = null;
			JSONArray jarray = new JSONArray();
			JSONObject resultJson = new JSONObject();
			
			try {
				double openingbalance =0.00;
				double closingbalance =0.00;
				List<Ledger> ledger1 = getopenbalandclosebalexttype(plant, fromDate, toDate, "('8','9')");
				for (Ledger ledger : ledger1) {
					openingbalance += ledger.getOPENING_BALANCE();
					closingbalance += ledger.getCLOSING_BALANCE();
				}
				resultJson.put("OPENBALANCE", openingbalance);
				resultJson.put("CLOSEBALANCE", closingbalance);
				jarray.add(resultJson);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			resp.setContentType("application/json");
			resp.setCharacterEncoding("UTF-8");
			resp.getWriter().write(resultJson.toString());
			resp.getWriter().flush();
			resp.getWriter().close();
		} 

	

	}
	

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		action = StrUtils.fString(request.getParameter("action")).trim();
		String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
		String user = StrUtils.fString((String) request.getSession().getAttribute("LOGIN_USER")).trim();
		String type = "ALL";
		
		if (action.equalsIgnoreCase("getCashFlowExcel")) {
			String fromDate = StrUtils.fString(request.getParameter("fromDate")).trim();
			String toDate = StrUtils.fString(request.getParameter("toDate")).trim();
			List<JSONArray> excelDataArray = new ArrayList<>();
			JSONObject cashflow = new JSONObject();
			try{
				JSONArray cf1 = cf1(plant, fromDate, toDate);
				excelDataArray.add(cf1);
				JSONArray cf2 = cf2(plant, fromDate, toDate);
				excelDataArray.add(cf2);
				JSONArray cf3 = cf3(plant, fromDate, toDate);
				excelDataArray.add(cf3);
				JSONArray cf4 = cf4(plant, fromDate, toDate);
				excelDataArray.add(cf4);
				cashflow = cf5(plant, fromDate, toDate);
			}catch (Exception e) {
				e.printStackTrace();
			}

			System.out.println(excelDataArray);
			System.out.println(cashflow);

			if (!fromDate.isEmpty()) {
				PlantMstDAO _PlantMstDAO = new PlantMstDAO();

				HSSFWorkbook workbook = null;
				try {
					String numberOfDecimal = _PlantMstDAO.getNumberOfDecimal(plant);
					workbook = populateExcel("Cash Flow", toDate, excelDataArray, numberOfDecimal, cashflow);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
				workbook.write(outByteStream);
				byte[] outArray = outByteStream.toByteArray();
				response.setContentType("application/ms-excel");
				response.setContentLength(outArray.length);
				response.setHeader("Expires:", "0"); // eliminates browser caching
				response.setHeader("Content-Disposition", "attachment; filename=CashFlow.xls");
				OutputStream outStream = response.getOutputStream();
				outStream.write(outArray);
				outStream.flush();
				outStream.close();
			}

		}



	}
	
	public List<Ledger> getopenbalandclosebal(String plant,String fromDate,String toDate,String accounttypes,String accountdetailtypes) {
		JSONArray jarray=new JSONArray();
		LedgerService ledgerService=new LedgerServiceImpl();
		JournalDAO journalDAO = new JournalDAO();
		CoaDAO coaDAO=new CoaDAO();
		List<LedgerDetails> ledgerDetails=new ArrayList<LedgerDetails>();
		List<Ledger> ledgerList=new ArrayList<Ledger>();
		try {

			ledgerDetails=journalDAO.getDetailsByActTypeNoActDetAndDATELedger(plant, fromDate, toDate, accounttypes,accountdetailtypes);
			Map<String,List<LedgerDetails>> ledgerDetailsGrouped=ledgerDetails.stream().collect(Collectors.groupingBy(LedgerDetails::getACCOUNT));
			for (Map.Entry<String,List<LedgerDetails>> entry : ledgerDetailsGrouped.entrySet()) {
				
				Double openingBalance=0.00;
				Ledger ledger=new Ledger();
				ledger.setACCOUNT(entry.getKey());
				ledger.setLEDGER_DETAILS(entry.getValue());
				LocalDate yearStartDay=LocalDate.parse(fromDate).withDayOfYear(1);
				JSONObject caoRecord=coaDAO.getCOAByName(plant, entry.getKey());
				if(caoRecord.get("id")!=null)
				{
					openingBalance=ledgerService.openingBalance(plant,caoRecord.getString("id"),yearStartDay.toString(),fromDate);
				}
				ledger.setOPENING_BALANCE(openingBalance);
				Double totalDebit=0.00;
				Double totalCredit=0.00;
				for(LedgerDetails ledgerDet:entry.getValue())
				{
					totalDebit+=ledgerDet.getDEBIT();
					totalCredit+=ledgerDet.getCREDIT();
				}
				Double closingBalance=totalDebit-totalCredit+openingBalance;
				ledger.setCLOSING_BALANCE(closingBalance);
				ledgerList.add(ledger);
			}
		
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		
		return ledgerList;
	}
	
	public List<Ledger> getopenbalandclosebalexttype(String plant,String fromDate,String toDate,String accountdetailtypes) {
		JSONArray jarray=new JSONArray();
		LedgerService ledgerService=new LedgerServiceImpl();
		JournalDAO journalDAO = new JournalDAO();
		CoaDAO coaDAO=new CoaDAO();
		List<LedgerDetails> ledgerDetails=new ArrayList<LedgerDetails>();
		List<Ledger> ledgerList=new ArrayList<Ledger>();
		try {

			ledgerDetails=journalDAO.getDetailsByActDetAndDATELedger(plant, fromDate, toDate,accountdetailtypes);
			Map<String,List<LedgerDetails>> ledgerDetailsGrouped=ledgerDetails.stream().collect(Collectors.groupingBy(LedgerDetails::getACCOUNT));
			for (Map.Entry<String,List<LedgerDetails>> entry : ledgerDetailsGrouped.entrySet()) {
				
				Double openingBalance=0.00;
				Ledger ledger=new Ledger();
				ledger.setACCOUNT(entry.getKey());
				ledger.setLEDGER_DETAILS(entry.getValue());
				LocalDate yearStartDay=LocalDate.parse(fromDate).withDayOfYear(1);
				JSONObject caoRecord=coaDAO.getCOAByName(plant, entry.getKey());
				if(caoRecord.get("id")!=null)
				{
					openingBalance=ledgerService.openingBalance(plant,caoRecord.getString("id"),yearStartDay.toString(),fromDate);
				}
				ledger.setOPENING_BALANCE(openingBalance);
				Double totalDebit=0.00;
				Double totalCredit=0.00;
				for(LedgerDetails ledgerDet:entry.getValue())
				{
					totalDebit+=ledgerDet.getDEBIT();
					totalCredit+=ledgerDet.getCREDIT();
				}
				Double closingBalance=totalDebit-totalCredit+openingBalance;
				ledger.setCLOSING_BALANCE(closingBalance);
				ledgerList.add(ledger);
			}
		
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		
		return ledgerList;
	}
	
	private double getNetProfit(String plant,String fromDate,String toDate) {
		double netProfit = 0.0;
		try {
			ReportService reportService = new ReportServiceImpl();
			netProfit = reportService.NetProfit(plant, fromDate, toDate);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return netProfit;
	}

	public JSONArray cf1(String plant,String fromDate,String toDate) {
		List<Journal> journalList = null;
		JSONArray jarray = new JSONArray();
		Map<Integer, CashFlow> cashFlowArr0 = new Hashtable<Integer, CashFlow>();
		Map<Integer, CashFlow> cashFlowArr1 = new Hashtable<Integer, CashFlow>();
		Map<Integer, CashFlow> cashFlowArr2 = new Hashtable<Integer, CashFlow>();
		Map<Integer, CashFlow> cashFlowArr3 = new Hashtable<Integer, CashFlow>();
		Map<Integer, CashFlow> cashFlowArr4 = new Hashtable<Integer, CashFlow>();
		try {
			// journalList=journalService.getAllJournalHeader(plant, user, type);
			// journalList=journalService.getJournalsByDateWithoutAttach(plant, fromDate,
			// toDate);
			
			
			CashFlow cashFlow1 = new CashFlow();
			cashFlow1.setAccount_id(0);
			cashFlow1.setAccount_name("Profit");
			cashFlow1.setAccount_type("Profit");
			// profitLoss.setAccount_type(coaJson.getString("account_type_name"));
			cashFlow1.setExtended_type("");
			cashFlow1.setMain_account("");
			cashFlow1.setNet_credit(0.0);
			cashFlow1.setNet_debit(0.0);
			cashFlow1.setTotal(getNetProfit(plant,fromDate,toDate));
			cashFlowArr0.put(0, cashFlow1);
			

			cashFlowArr0.forEach((k, v) -> {
				jarray.add(v);
			});

			Set<Integer> AccountId1 = new HashSet<Integer>();
			String accounttypes= "('32')";
			List<JournalDetail> journalDetails1 = journalService.getJournalDetailsByAccountDetailTypeAndDATE(plant,
					fromDate, toDate, accounttypes);
			if (journalDetails1.size() > 0) {
				for (JournalDetail journalDet : journalDetails1) {
					if (!AccountId1.contains(journalDet.getACCOUNT_ID())) {
						AccountId1.add(journalDet.getACCOUNT_ID());
						CoaDAO coaDAO = new CoaDAO();

						JSONObject coaFullDetail = coaDAO.CoaFullRecord(plant,
								String.valueOf(journalDet.getACCOUNT_ID()));
						JSONObject coaJson = (JSONObject) coaFullDetail.get("results");
						// jarray.add(coaFullDetail.get("results"));
						System.out.println("CoaJson" + coaJson);
						if (coaJson != null) {
							CashFlow cashFlow = new CashFlow();
							cashFlow.setAccount_id(journalDet.getACCOUNT_ID());
							String accountcode = coaDAO.GetAccountCodeByName(coaJson.getString("account_name"), plant);
							cashFlow.setAccount_name(accountcode+" "+coaJson.getString("account_name"));
							int detid = Integer.valueOf(coaJson.getString("account_det_id"));
							cashFlow.setAccount_type("Depreciation");
							// profitLoss.setAccount_type(coaJson.getString("account_type_name"));
							cashFlow.setExtended_type(coaJson.getString("account_det_type"));
							cashFlow.setMain_account(coaJson.getString("mainaccname"));
							cashFlow.setNet_credit(journalDet.getCREDITS());
							cashFlow.setNet_debit(journalDet.getDEBITS());
							cashFlowArr1.put(journalDet.getACCOUNT_ID(), cashFlow);
						}

						// trialBalArr.put(coaJson.getString("ACCOUNT_NAME"), journalDet.get)
					} else {
						CashFlow cashflow = cashFlowArr1.get(journalDet.getACCOUNT_ID());
						if (cashflow != null) {
							Double debit = cashflow.getNet_debit() + journalDet.getDEBITS();
							Double credit = cashflow.getNet_credit() + journalDet.getCREDITS();
							cashflow.setNet_debit(debit);
							cashflow.setNet_credit(credit);
							cashFlowArr1.replace(journalDet.getACCOUNT_ID(), cashflow);
						}

					}
				}
			}else {
				CashFlow cashFlow = new CashFlow();
				cashFlow.setAccount_id(0);
				cashFlow.setAccount_name("-");
				cashFlow.setAccount_type("Depreciation");
				// profitLoss.setAccount_type(coaJson.getString("account_type_name"));
				cashFlow.setExtended_type("");
				cashFlow.setMain_account("");
				cashFlow.setNet_credit(0.0);
				cashFlow.setNet_debit(0.0);
				cashFlowArr1.put(0, cashFlow);
			}

			cashFlowArr1.forEach((k, v) -> {
				String accounttype = v.getAccount_type().trim();
				Double total = 0.00;
				//total = v.getNet_credit() - v.getNet_debit();
				total = v.getNet_debit() - v.getNet_credit();
				v.setTotal(total);
				cashFlowArr1.replace(k, v);
			});

			cashFlowArr1.forEach((k, v) -> {
				jarray.add(v);
			});
			
			
			
			Set<Integer> AccountId2 = new HashSet<Integer>();
			accounttypes= "('31')";
			List<JournalDetail> journalDetails2 = journalService.getJournalDetailsByAccountDetailTypeAndDATE(plant,
					fromDate, toDate, accounttypes);
			if (journalDetails2.size() > 0) {
				for (JournalDetail journalDet : journalDetails2) {
					if (!AccountId2.contains(journalDet.getACCOUNT_ID())) {
						AccountId2.add(journalDet.getACCOUNT_ID());
						CoaDAO coaDAO = new CoaDAO();

						JSONObject coaFullDetail = coaDAO.CoaFullRecord(plant,
								String.valueOf(journalDet.getACCOUNT_ID()));
						JSONObject coaJson = (JSONObject) coaFullDetail.get("results");
						// jarray.add(coaFullDetail.get("results"));
						System.out.println("CoaJson" + coaJson);
						if (coaJson != null) {
							CashFlow cashFlow = new CashFlow();
							cashFlow.setAccount_id(journalDet.getACCOUNT_ID());
							String accountcode = coaDAO.GetAccountCodeByName(coaJson.getString("account_name"), plant);
							cashFlow.setAccount_name(accountcode+" "+coaJson.getString("account_name"));
							//cashFlow.setAccount_name(coaJson.getString("account_name"));
							int detid = Integer.valueOf(coaJson.getString("account_det_id"));
							cashFlow.setAccount_type("Interest on Bank borrowings / Borrowed Funds");
							// profitLoss.setAccount_type(coaJson.getString("account_type_name"));
							cashFlow.setExtended_type(coaJson.getString("account_det_type"));
							cashFlow.setMain_account(coaJson.getString("mainaccname"));
							cashFlow.setNet_credit(journalDet.getCREDITS());
							cashFlow.setNet_debit(journalDet.getDEBITS());
							cashFlowArr2.put(journalDet.getACCOUNT_ID(), cashFlow);
						}

						// trialBalArr.put(coaJson.getString("ACCOUNT_NAME"), journalDet.get)
					} else {
						CashFlow cashflow = cashFlowArr2.get(journalDet.getACCOUNT_ID());
						if (cashflow != null) {
							Double debit = cashflow.getNet_debit() + journalDet.getDEBITS();
							Double credit = cashflow.getNet_credit() + journalDet.getCREDITS();
							cashflow.setNet_debit(debit);
							cashflow.setNet_credit(credit);
							cashFlowArr2.replace(journalDet.getACCOUNT_ID(), cashflow);
						}

					}
				}
			}else {
				CashFlow cashFlow = new CashFlow();
				cashFlow.setAccount_id(0);
				cashFlow.setAccount_name("-");
				cashFlow.setAccount_type("Interest on Bank borrowings / Borrowed Funds");
				// profitLoss.setAccount_type(coaJson.getString("account_type_name"));
				cashFlow.setExtended_type("");
				cashFlow.setMain_account("");
				cashFlow.setNet_credit(0.0);
				cashFlow.setNet_debit(0.0);
				cashFlowArr2.put(0, cashFlow);
			}

			cashFlowArr2.forEach((k, v) -> {
				String accounttype = v.getAccount_type().trim();
				Double total = 0.00;
				//total = v.getNet_credit() - v.getNet_debit();
				total = v.getNet_debit() - v.getNet_credit();
				v.setTotal(total);
				cashFlowArr2.replace(k, v);
			});

			cashFlowArr2.forEach((k, v) -> {
				jarray.add(v);
			});
			
			
			
			Set<Integer> AccountId3 = new HashSet<Integer>();
			accounttypes= "('27')";
			List<JournalDetail> journalDetails3 = journalService.getJournalDetailsByAccountDetailTypeAndDATE(plant,
					fromDate, toDate, accounttypes);
			if (journalDetails3.size() > 0) {
				for (JournalDetail journalDet : journalDetails3) {
					if (!AccountId3.contains(journalDet.getACCOUNT_ID())) {
						AccountId3.add(journalDet.getACCOUNT_ID());
						CoaDAO coaDAO = new CoaDAO();

						JSONObject coaFullDetail = coaDAO.CoaFullRecord(plant,
								String.valueOf(journalDet.getACCOUNT_ID()));
						JSONObject coaJson = (JSONObject) coaFullDetail.get("results");
						// jarray.add(coaFullDetail.get("results"));
						System.out.println("CoaJson" + coaJson);
						if (coaJson != null) {
							CashFlow cashFlow = new CashFlow();
							cashFlow.setAccount_id(journalDet.getACCOUNT_ID());
							String accountcode = coaDAO.GetAccountCodeByName(coaJson.getString("account_name"), plant);
							cashFlow.setAccount_name(accountcode+" "+coaJson.getString("account_name"));
							//cashFlow.setAccount_name(coaJson.getString("account_name"));
							int detid = Integer.valueOf(coaJson.getString("account_det_id"));
							cashFlow.setAccount_type("Bank Interest");
							// profitLoss.setAccount_type(coaJson.getString("account_type_name"));
							cashFlow.setExtended_type(coaJson.getString("account_det_type"));
							cashFlow.setMain_account(coaJson.getString("mainaccname"));
							cashFlow.setNet_credit(journalDet.getCREDITS());
							cashFlow.setNet_debit(journalDet.getDEBITS());
							cashFlowArr3.put(journalDet.getACCOUNT_ID(), cashFlow);
						}

						// trialBalArr.put(coaJson.getString("ACCOUNT_NAME"), journalDet.get)
					} else {
						CashFlow cashflow = cashFlowArr3.get(journalDet.getACCOUNT_ID());
						if (cashflow != null) {
							Double debit = cashflow.getNet_debit() + journalDet.getDEBITS();
							Double credit = cashflow.getNet_credit() + journalDet.getCREDITS();
							cashflow.setNet_debit(debit);
							cashflow.setNet_credit(credit);
							cashFlowArr3.replace(journalDet.getACCOUNT_ID(), cashflow);
						}

					}
				}
			}else {
				CashFlow cashFlow = new CashFlow();
				cashFlow.setAccount_id(0);
				cashFlow.setAccount_name("-");
				cashFlow.setAccount_type("Bank Interest");
				// profitLoss.setAccount_type(coaJson.getString("account_type_name"));
				cashFlow.setExtended_type("");
				cashFlow.setMain_account("");
				cashFlow.setNet_credit(0.0);
				cashFlow.setNet_debit(0.0);
				cashFlowArr3.put(0, cashFlow);
			}

			cashFlowArr3.forEach((k, v) -> {
				String accounttype = v.getAccount_type().trim();
				Double total = 0.00;
				//total = v.getNet_credit() - v.getNet_debit();
				total = v.getNet_debit() - v.getNet_credit();
				v.setTotal(total);
				cashFlowArr3.replace(k, v);
			});

			cashFlowArr3.forEach((k, v) -> {
				jarray.add(v);
			});
			
			
			Set<Integer> AccountId4 = new HashSet<Integer>();
			accounttypes= "('34')";
			List<JournalDetail> journalDetails4 = journalService.getJournalDetailsByAccountDetailTypeAndDATE(plant,
					fromDate, toDate, accounttypes);
			if (journalDetails4.size() > 0) {
				for (JournalDetail journalDet : journalDetails4) {
					if (!AccountId4.contains(journalDet.getACCOUNT_ID())) {
						AccountId4.add(journalDet.getACCOUNT_ID());
						CoaDAO coaDAO = new CoaDAO();

						JSONObject coaFullDetail = coaDAO.CoaFullRecord(plant,
								String.valueOf(journalDet.getACCOUNT_ID()));
						JSONObject coaJson = (JSONObject) coaFullDetail.get("results");
						// jarray.add(coaFullDetail.get("results"));
						System.out.println("CoaJson" + coaJson);
						if (coaJson != null) {
							CashFlow cashFlow = new CashFlow();
							cashFlow.setAccount_id(journalDet.getACCOUNT_ID());
							String accountcode = coaDAO.GetAccountCodeByName(coaJson.getString("account_name"), plant);
							cashFlow.setAccount_name(accountcode+" "+coaJson.getString("account_name"));
							//cashFlow.setAccount_name(coaJson.getString("account_name"));
							int detid = Integer.valueOf(coaJson.getString("account_det_id"));
							cashFlow.setAccount_type("Dividend");
							// profitLoss.setAccount_type(coaJson.getString("account_type_name"));
							cashFlow.setExtended_type(coaJson.getString("account_det_type"));
							cashFlow.setMain_account(coaJson.getString("mainaccname"));
							cashFlow.setNet_credit(journalDet.getCREDITS());
							cashFlow.setNet_debit(journalDet.getDEBITS());
							cashFlowArr4.put(journalDet.getACCOUNT_ID(), cashFlow);
						}

						// trialBalArr.put(coaJson.getString("ACCOUNT_NAME"), journalDet.get)
					} else {
						CashFlow cashflow = cashFlowArr4.get(journalDet.getACCOUNT_ID());
						if (cashflow != null) {
							Double debit = cashflow.getNet_debit() + journalDet.getDEBITS();
							Double credit = cashflow.getNet_credit() + journalDet.getCREDITS();
							cashflow.setNet_debit(debit);
							cashflow.setNet_credit(credit);
							cashFlowArr4.replace(journalDet.getACCOUNT_ID(), cashflow);
						}

					}
				}
			}else {
				CashFlow cashFlow = new CashFlow();
				cashFlow.setAccount_id(0);
				cashFlow.setAccount_name("-");
				cashFlow.setAccount_type("Dividend");
				// profitLoss.setAccount_type(coaJson.getString("account_type_name"));
				cashFlow.setExtended_type("");
				cashFlow.setMain_account("");
				cashFlow.setNet_credit(0.0);
				cashFlow.setNet_debit(0.0);
				cashFlowArr4.put(0, cashFlow);
			}

			cashFlowArr4.forEach((k, v) -> {
				String accounttype = v.getAccount_type().trim();
				Double total = 0.00;
				//total = v.getNet_credit() - v.getNet_debit();
				total = v.getNet_debit() - v.getNet_credit();
				v.setTotal(total);
				cashFlowArr4.replace(k, v);
			});

			cashFlowArr4.forEach((k, v) -> {
				jarray.add(v);
			});
			
			/*List<Ledger> ledger1 = getopenbalandclosebal(plant, fromDate, toDate, "('3')", "('9')");
			Map<Integer, CashFlow> cashFlowArr5 = new Hashtable<Integer, CashFlow>();
			int l1 = 0;
			for (Ledger ledger : ledger1) {
				CashFlow cashFlow = new CashFlow();
				cashFlow.setAccount_id(0);
				cashFlow.setAccount_name(ledger.getACCOUNT());
				cashFlow.setAccount_type("(Increase)/Decrease in Current Assets");
				cashFlow.setExtended_type("");
				cashFlow.setMain_account("");
				cashFlow.setNet_credit(0.0);
				cashFlow.setNet_debit(0.0);
				double bal = ledger.getOPENING_BALANCE() - ledger.getCLOSING_BALANCE();
				cashFlow.setTotal(bal);
				cashFlowArr5.put(l1, cashFlow);
				l1++;
			}
			
			cashFlowArr5.forEach((k, v) -> {
				jarray.add(v);
			});*/
			
			// jarray.add(trialBalArr.values().toArray());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return jarray;
	}
	
	public JSONArray cf2(String plant,String fromDate,String toDate) {
		CoaDAO coaDAO = new CoaDAO();
		List<Journal> journalList = null;
		JSONArray jarray = new JSONArray();
		Map<Integer, CashFlow> cashFlowArr0 = new Hashtable<Integer, CashFlow>();
		Map<Integer, CashFlow> cashFlowArr1 = new Hashtable<Integer, CashFlow>();
		Map<Integer, CashFlow> cashFlowArr2 = new Hashtable<Integer, CashFlow>();
		Map<Integer, CashFlow> cashFlowArr3 = new Hashtable<Integer, CashFlow>();
		Map<Integer, CashFlow> cashFlowArr4 = new Hashtable<Integer, CashFlow>();
		Map<Integer, CashFlow> cashFlowArr5 = new Hashtable<Integer, CashFlow>();
		try {
			
			
			List<Ledger> ledger1 = getopenbalandclosebal(plant, fromDate, toDate, "('3')", "('9')");
			int l1 = 0;
			for (Ledger ledger : ledger1) {
				CashFlow cashFlow = new CashFlow();
				cashFlow.setAccount_id(0);
				String accountcode = coaDAO.GetAccountCodeByName(ledger.getACCOUNT(), plant);
				cashFlow.setAccount_name(accountcode+" "+ledger.getACCOUNT());
				cashFlow.setAccount_type("(Increase)/Decrease in Current Assets");
				cashFlow.setExtended_type("");
				cashFlow.setMain_account("");
				cashFlow.setNet_credit(0.0);
				cashFlow.setNet_debit(0.0);
				double bal = ledger.getOPENING_BALANCE() - ledger.getCLOSING_BALANCE();
				cashFlow.setTotal(bal);
				cashFlowArr1.put(l1, cashFlow);
				l1++;
			}
			if(ledger1.size() == 0) {
				CashFlow cashFlow = new CashFlow();
				cashFlow.setAccount_id(0);
				cashFlow.setAccount_name("-");
				cashFlow.setAccount_type("(Increase)/Decrease in Current Assets");
				cashFlow.setExtended_type("");
				cashFlow.setMain_account("");
				cashFlow.setNet_credit(0.0);
				cashFlow.setNet_debit(0.0);
				cashFlowArr1.put(0, cashFlow);
			}
			cashFlowArr1.forEach((k, v) -> {
				jarray.add(v);
			});
			
			List<Ledger> ledger2 = getopenbalandclosebal(plant, fromDate, toDate, "('2')", "('4')");
			int l2 = 0;
			for (Ledger ledger : ledger2) {
				CashFlow cashFlow = new CashFlow();
				cashFlow.setAccount_id(0);
				//cashFlow.setAccount_name(ledger.getACCOUNT());
				String accountcode = coaDAO.GetAccountCodeByName(ledger.getACCOUNT(), plant);
				cashFlow.setAccount_name(accountcode+" "+ledger.getACCOUNT());
				cashFlow.setAccount_type("(Increase)/Decrease in Non-Current Assets");
				cashFlow.setExtended_type("");
				cashFlow.setMain_account("");
				cashFlow.setNet_credit(0.0);
				cashFlow.setNet_debit(0.0);
				double bal = ledger.getOPENING_BALANCE() - ledger.getCLOSING_BALANCE();
				cashFlow.setTotal(bal);
				cashFlowArr2.put(l2, cashFlow);
				l2++;
			}
			if(ledger2.size() == 0) {
				CashFlow cashFlow = new CashFlow();
				cashFlow.setAccount_id(0);
				cashFlow.setAccount_name("-");
				cashFlow.setAccount_type("(Increase)/Decrease in Non-Current Assets");
				cashFlow.setExtended_type("");
				cashFlow.setMain_account("");
				cashFlow.setNet_credit(0.0);
				cashFlow.setNet_debit(0.0);
				cashFlowArr2.put(0, cashFlow);
			}
			cashFlowArr2.forEach((k, v) -> {
				jarray.add(v);
			});
			
			List<Ledger> ledger3 = getopenbalandclosebal(plant, fromDate, toDate, "('6')", "('0')");
			int l3 = 0;
			for (Ledger ledger : ledger3) {
				CashFlow cashFlow = new CashFlow();
				cashFlow.setAccount_id(0);
				//cashFlow.setAccount_name(ledger.getACCOUNT());
				String accountcode = coaDAO.GetAccountCodeByName(ledger.getACCOUNT(), plant);
				cashFlow.setAccount_name(accountcode+" "+ledger.getACCOUNT());
				cashFlow.setAccount_type("(Decrease)/Increase in Current Liabilities");
				cashFlow.setExtended_type("");
				cashFlow.setMain_account("");
				cashFlow.setNet_credit(0.0);
				cashFlow.setNet_debit(0.0);
				double bal = ledger.getOPENING_BALANCE() - ledger.getCLOSING_BALANCE();
				cashFlow.setTotal(bal);
				cashFlowArr3.put(l3, cashFlow);
				l3++;
			}
			if(ledger3.size() == 0) {
				CashFlow cashFlow = new CashFlow();
				cashFlow.setAccount_id(0);
				cashFlow.setAccount_name("-");
				cashFlow.setAccount_type("(Decrease)/Increase in Current Liabilities");
				cashFlow.setExtended_type("");
				cashFlow.setMain_account("");
				cashFlow.setNet_credit(0.0);
				cashFlow.setNet_debit(0.0);
				cashFlowArr3.put(0, cashFlow);
			}
			cashFlowArr3.forEach((k, v) -> {
				jarray.add(v);
			});
			
			List<Ledger> ledger4 = getopenbalandclosebal(plant, fromDate, toDate, "('5')", "('0')");
			int l4 = 0;
			for (Ledger ledger : ledger4) {
				CashFlow cashFlow = new CashFlow();
				cashFlow.setAccount_id(0);
				//cashFlow.setAccount_name(ledger.getACCOUNT());
				String accountcode = coaDAO.GetAccountCodeByName(ledger.getACCOUNT(), plant);
				cashFlow.setAccount_name(accountcode+" "+ledger.getACCOUNT());
				cashFlow.setAccount_type("(Decrease)/Increase in Provisions");
				cashFlow.setExtended_type("");
				cashFlow.setMain_account("");
				cashFlow.setNet_credit(0.0);
				cashFlow.setNet_debit(0.0);
				double bal = ledger.getOPENING_BALANCE() - ledger.getCLOSING_BALANCE();
				cashFlow.setTotal(bal);
				cashFlowArr4.put(l4, cashFlow);
				l4++;
			}
			if(ledger4.size() == 0) {
				CashFlow cashFlow = new CashFlow();
				cashFlow.setAccount_id(0);
				cashFlow.setAccount_name("-");
				cashFlow.setAccount_type("(Decrease)/Increase in Provisions");
				cashFlow.setExtended_type("");
				cashFlow.setMain_account("");
				cashFlow.setNet_credit(0.0);
				cashFlow.setNet_debit(0.0);
				cashFlowArr4.put(0, cashFlow);
			}
			cashFlowArr4.forEach((k, v) -> {
				jarray.add(v);
			});
			
			List<Ledger> ledger5 = getopenbalandclosebal(plant, fromDate, toDate, "('4')", "('14')");
			int l5 = 0;
			for (Ledger ledger : ledger5) {
				CashFlow cashFlow = new CashFlow();
				cashFlow.setAccount_id(0);
				//cashFlow.setAccount_name(ledger.getACCOUNT());
				String accountcode = coaDAO.GetAccountCodeByName(ledger.getACCOUNT(), plant);
				cashFlow.setAccount_name(accountcode+" "+ledger.getACCOUNT());
				cashFlow.setAccount_type("(Decrease)/Increase in Non-current liabilities");
				cashFlow.setExtended_type("");
				cashFlow.setMain_account("");
				cashFlow.setNet_credit(0.0);
				cashFlow.setNet_debit(0.0);
				double bal = ledger.getOPENING_BALANCE() - ledger.getCLOSING_BALANCE();
				cashFlow.setTotal(bal);
				cashFlowArr5.put(l5, cashFlow);
				l5++;
			}
			if(ledger5.size() == 0) {
				CashFlow cashFlow = new CashFlow();
				cashFlow.setAccount_id(0);
				cashFlow.setAccount_name("-");
				cashFlow.setAccount_type("(Decrease)/Increase in Non-current liabilities");
				cashFlow.setExtended_type("");
				cashFlow.setMain_account("");
				cashFlow.setNet_credit(0.0);
				cashFlow.setNet_debit(0.0);
				cashFlowArr5.put(0, cashFlow);
			}
			cashFlowArr5.forEach((k, v) -> {
				jarray.add(v);
			});
			
			// jarray.add(trialBalArr.values().toArray());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jarray;
	}
	
	public JSONArray cf3(String plant,String fromDate,String toDate) {
		CoaDAO coaDAO = new CoaDAO();
		List<Journal> journalList = null;
		JSONArray jarray = new JSONArray();
		Map<Integer, CashFlow> cashFlowArr0 = new Hashtable<Integer, CashFlow>();
		Map<Integer, CashFlow> cashFlowArr1 = new Hashtable<Integer, CashFlow>();
		Map<Integer, CashFlow> cashFlowArr2 = new Hashtable<Integer, CashFlow>();
		Map<Integer, CashFlow> cashFlowArr3 = new Hashtable<Integer, CashFlow>();
		Map<Integer, CashFlow> cashFlowArr4 = new Hashtable<Integer, CashFlow>();
		Map<Integer, CashFlow> cashFlowArr5 = new Hashtable<Integer, CashFlow>();
		try {
			
			
			List<Ledger> ledger1 = getopenbalandclosebalexttype(plant, fromDate, toDate, "('1')");
			int l1 = 0;
			for (Ledger ledger : ledger1) {
				CashFlow cashFlow = new CashFlow();
				cashFlow.setAccount_id(0);
				//cashFlow.setAccount_name(ledger.getACCOUNT());
				String accountcode = coaDAO.GetAccountCodeByName(ledger.getACCOUNT(), plant);
				cashFlow.setAccount_name(accountcode+" "+ledger.getACCOUNT());
				cashFlow.setAccount_type("Purchase of Fixed Assets");
				cashFlow.setExtended_type("");
				cashFlow.setMain_account("");
				cashFlow.setNet_credit(0.0);
				cashFlow.setNet_debit(0.0);
				double bal = ledger.getOPENING_BALANCE() - ledger.getCLOSING_BALANCE();
				cashFlow.setTotal(bal);
				cashFlowArr1.put(l1, cashFlow);
				l1++;
			}
			if(ledger1.size() == 0) {
				CashFlow cashFlow = new CashFlow();
				cashFlow.setAccount_id(0);
				cashFlow.setAccount_name("-");
				cashFlow.setAccount_type("Purchase of Fixed Assets");
				cashFlow.setExtended_type("");
				cashFlow.setMain_account("");
				cashFlow.setNet_credit(0.0);
				cashFlow.setNet_debit(0.0);
				cashFlowArr1.put(0, cashFlow);
			}
			cashFlowArr1.forEach((k, v) -> {
				jarray.add(v);
			});
			
			List<Ledger> ledger2 = getopenbalandclosebalexttype(plant, fromDate, toDate, "('27')");
			int l2 = 0;
			for (Ledger ledger : ledger2) {
				CashFlow cashFlow = new CashFlow();
				cashFlow.setAccount_id(0);
				String accountcode = coaDAO.GetAccountCodeByName(ledger.getACCOUNT(), plant);
				cashFlow.setAccount_name(accountcode+" "+ledger.getACCOUNT());
				cashFlow.setAccount_type("Bank Interest");
				cashFlow.setExtended_type("");
				cashFlow.setMain_account("");
				cashFlow.setNet_credit(0.0);
				cashFlow.setNet_debit(0.0);
				double bal = ledger.getOPENING_BALANCE() - ledger.getCLOSING_BALANCE();
				cashFlow.setTotal(bal);
				cashFlowArr2.put(l2, cashFlow);
				l2++;
			}
			if(ledger2.size() == 0) {
				CashFlow cashFlow = new CashFlow();
				cashFlow.setAccount_id(0);
				cashFlow.setAccount_name("-");
				cashFlow.setAccount_type("Bank Interest");
				cashFlow.setExtended_type("");
				cashFlow.setMain_account("");
				cashFlow.setNet_credit(0.0);
				cashFlow.setNet_debit(0.0);
				cashFlowArr2.put(0, cashFlow);
			}
			cashFlowArr2.forEach((k, v) -> {
				jarray.add(v);
			});
			
			List<Ledger> ledger3 = getopenbalandclosebalexttype(plant, fromDate, toDate, "('4')");
			int l3 = 0;
			for (Ledger ledger : ledger3) {
				CashFlow cashFlow = new CashFlow();
				cashFlow.setAccount_id(0);
				String accountcode = coaDAO.GetAccountCodeByName(ledger.getACCOUNT(), plant);
				cashFlow.setAccount_name(accountcode+" "+ledger.getACCOUNT());
				cashFlow.setAccount_type("Long term investment");
				cashFlow.setExtended_type("");
				cashFlow.setMain_account("");
				cashFlow.setNet_credit(0.0);
				cashFlow.setNet_debit(0.0);
				double bal = ledger.getOPENING_BALANCE() - ledger.getCLOSING_BALANCE();
				cashFlow.setTotal(bal);
				cashFlowArr3.put(l3, cashFlow);
				l3++;
			}
			if(ledger3.size() == 0) {
				CashFlow cashFlow = new CashFlow();
				cashFlow.setAccount_id(0);
				cashFlow.setAccount_name("-");
				cashFlow.setAccount_type("Long term investment");
				cashFlow.setExtended_type("");
				cashFlow.setMain_account("");
				cashFlow.setNet_credit(0.0);
				cashFlow.setNet_debit(0.0);
				cashFlowArr3.put(0, cashFlow);
			}
			cashFlowArr3.forEach((k, v) -> {
				jarray.add(v);
			});
			
			// jarray.add(trialBalArr.values().toArray());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return jarray;
	}
	
	public JSONArray cf4(String plant,String fromDate,String toDate) {
		CoaDAO coaDAO = new CoaDAO();
		List<Journal> journalList = null;
		JSONArray jarray = new JSONArray();
		Map<Integer, CashFlow> cashFlowArr0 = new Hashtable<Integer, CashFlow>();
		Map<Integer, CashFlow> cashFlowArr1 = new Hashtable<Integer, CashFlow>();
		Map<Integer, CashFlow> cashFlowArr2 = new Hashtable<Integer, CashFlow>();
		Map<Integer, CashFlow> cashFlowArr3 = new Hashtable<Integer, CashFlow>();
		Map<Integer, CashFlow> cashFlowArr4 = new Hashtable<Integer, CashFlow>();
		Map<Integer, CashFlow> cashFlowArr5 = new Hashtable<Integer, CashFlow>();
		try {
			
			
			List<Ledger> ledger1 = getopenbalandclosebalexttype(plant, fromDate, toDate, "('23')");
			int l1 = 0;
			for (Ledger ledger : ledger1) {
				CashFlow cashFlow = new CashFlow();
				cashFlow.setAccount_id(0);
				String accountcode = coaDAO.GetAccountCodeByName(ledger.getACCOUNT(), plant);
				cashFlow.setAccount_name(accountcode+" "+ledger.getACCOUNT());
				cashFlow.setAccount_type("Introduction of share capital");
				cashFlow.setExtended_type("");
				cashFlow.setMain_account("");
				cashFlow.setNet_credit(0.0);
				cashFlow.setNet_debit(0.0);
				double bal = ledger.getOPENING_BALANCE() - ledger.getCLOSING_BALANCE();
				cashFlow.setTotal(bal);
				cashFlowArr1.put(l1, cashFlow);
				l1++;
			}
			if(ledger1.size() == 0) {
				CashFlow cashFlow = new CashFlow();
				cashFlow.setAccount_id(0);
				cashFlow.setAccount_name("-");
				cashFlow.setAccount_type("Introduction of share capital");
				cashFlow.setExtended_type("");
				cashFlow.setMain_account("");
				cashFlow.setNet_credit(0.0);
				cashFlow.setNet_debit(0.0);
				cashFlowArr1.put(0, cashFlow);
			}
			cashFlowArr1.forEach((k, v) -> {
				jarray.add(v);
			});
			
			List<Ledger> ledger2 = getopenbalandclosebalexttype(plant, fromDate, toDate, "('14')");
			int l2 = 0;
			for (Ledger ledger : ledger2) {
				CashFlow cashFlow = new CashFlow();
				cashFlow.setAccount_id(0);
				String accountcode = coaDAO.GetAccountCodeByName(ledger.getACCOUNT(), plant);
				cashFlow.setAccount_name(accountcode+" "+ledger.getACCOUNT());
				cashFlow.setAccount_type("Bank Borrowings / Borrowed Funds");
				cashFlow.setExtended_type("");
				cashFlow.setMain_account("");
				cashFlow.setNet_credit(0.0);
				cashFlow.setNet_debit(0.0);
				double bal = ledger.getOPENING_BALANCE() - ledger.getCLOSING_BALANCE();
				cashFlow.setTotal(bal);
				cashFlowArr2.put(l2, cashFlow);
				l2++;
			}
			if(ledger2.size() == 0) {
				CashFlow cashFlow = new CashFlow();
				cashFlow.setAccount_id(0);
				cashFlow.setAccount_name("-");
				cashFlow.setAccount_type("Bank Borrowings / Borrowed Funds");
				cashFlow.setExtended_type("");
				cashFlow.setMain_account("");
				cashFlow.setNet_credit(0.0);
				cashFlow.setNet_debit(0.0);
				cashFlowArr2.put(0, cashFlow);
			}
			cashFlowArr2.forEach((k, v) -> {
				jarray.add(v);
			});
			
			List<Ledger> ledger3 = getopenbalandclosebalexttype(plant, fromDate, toDate, "('31')");
			int l3 = 0;
			for (Ledger ledger : ledger3) {
				CashFlow cashFlow = new CashFlow();
				cashFlow.setAccount_id(0);
				String accountcode = coaDAO.GetAccountCodeByName(ledger.getACCOUNT(), plant);
				cashFlow.setAccount_name(accountcode+" "+ledger.getACCOUNT());
				cashFlow.setAccount_type("Interest on Bank Borrowings / Borrowed Funds");
				cashFlow.setExtended_type("");
				cashFlow.setMain_account("");
				cashFlow.setNet_credit(0.0);
				cashFlow.setNet_debit(0.0);
				double bal = ledger.getOPENING_BALANCE() - ledger.getCLOSING_BALANCE();
				cashFlow.setTotal(bal);
				cashFlowArr3.put(l3, cashFlow);
				l3++;
			}
			if(ledger3.size() == 0) {
				CashFlow cashFlow = new CashFlow();
				cashFlow.setAccount_id(0);
				cashFlow.setAccount_name("-");
				cashFlow.setAccount_type("Interest on Bank Borrowings / Borrowed Funds");
				cashFlow.setExtended_type("");
				cashFlow.setMain_account("");
				cashFlow.setNet_credit(0.0);
				cashFlow.setNet_debit(0.0);
				cashFlowArr3.put(0, cashFlow);
			}
			cashFlowArr3.forEach((k, v) -> {
				jarray.add(v);
			});
			
			List<Ledger> ledger4 = getopenbalandclosebalexttype(plant, fromDate, toDate, "('34')");
			int l4 = 0;
			for (Ledger ledger : ledger4) {
				CashFlow cashFlow = new CashFlow();
				cashFlow.setAccount_id(0);
				String accountcode = coaDAO.GetAccountCodeByName(ledger.getACCOUNT(), plant);
				cashFlow.setAccount_name(accountcode+" "+ledger.getACCOUNT());
				cashFlow.setAccount_type("Dividend disbursed");
				cashFlow.setExtended_type("");
				cashFlow.setMain_account("");
				cashFlow.setNet_credit(0.0);
				cashFlow.setNet_debit(0.0);
				double bal = ledger.getOPENING_BALANCE() - ledger.getCLOSING_BALANCE();
				cashFlow.setTotal(bal);
				cashFlowArr4.put(l4, cashFlow);
				l4++;
			}
			if(ledger4.size() == 0) {
				CashFlow cashFlow = new CashFlow();
				cashFlow.setAccount_id(0);
				cashFlow.setAccount_name("-");
				cashFlow.setAccount_type("Dividend disbursed");
				cashFlow.setExtended_type("");
				cashFlow.setMain_account("");
				cashFlow.setNet_credit(0.0);
				cashFlow.setNet_debit(0.0);
				cashFlowArr4.put(0, cashFlow);
			}
			cashFlowArr4.forEach((k, v) -> {
				jarray.add(v);
			});
			
			
			
			// jarray.add(trialBalArr.values().toArray());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return jarray;
	}
	
	public JSONObject cf5(String plant,String fromDate,String toDate) {
		JSONObject resultJson = new JSONObject();
		PlantMstDAO _PlantMstDAO = new PlantMstDAO();
			
		try {
			String numberOfDecimal = _PlantMstDAO.getNumberOfDecimal(plant);
			double openingbalance =0.00;
			double closingbalance =0.00;
			List<Ledger> ledger1 = getopenbalandclosebalexttype(plant, fromDate, toDate, "('8','9')");
			for (Ledger ledger : ledger1) {
				openingbalance += ledger.getOPENING_BALANCE();
				closingbalance += ledger.getCLOSING_BALANCE();
			}
			String opbal = StrUtils.addZeroes(openingbalance, numberOfDecimal);
			String clbal = StrUtils.addZeroes(closingbalance, numberOfDecimal);
			resultJson.put("OPENBALANCE", opbal);
			resultJson.put("CLOSEBALANCE", clbal);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return resultJson;
	}
	
	public HSSFWorkbook populateExcel(String headerTitle, String excelHeaderDate, List<JSONArray> excelDatas,
			String noOfDecimal, JSONObject cashflow) throws JSONException

	{

		String cf1 = excelDatas.get(0).toString();
		String cf2 = excelDatas.get(1).toString();
		String cf3 = excelDatas.get(2).toString();
		String cf4 = excelDatas.get(3).toString();
		
		// Create blank workbook
		HSSFWorkbook workbook = new HSSFWorkbook();
		// Create a blank sheet
		HSSFSheet spreadsheet = workbook.createSheet("CashFlow");
		// Create row object

		HSSFCellStyle my_style = workbook.createCellStyle();
		/* Create HSSFFont object from the workbook */
		HSSFFont my_font = workbook.createFont();
		/* set the weight of the font */
		my_font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		/* attach the font to the style created earlier */
		my_style.setFont(my_font);

		HSSFRow row = spreadsheet.createRow(0);
		HSSFCell cell = row.createCell(0);
		cell.setCellValue(headerTitle);
		cell.setCellStyle(my_style);

		HSSFRow row1 = spreadsheet.createRow(1);
		HSSFCell cell1 = row1.createCell(0);
		cell1.setCellValue("As of " + excelHeaderDate);
		cell1.setCellStyle(my_style);
		/*
		HSSFRow row2 = spreadsheet.createRow(3);
		HSSFCell cellAccountHeader = row2.createCell(0);
		cellAccountHeader.setCellValue("");
		cellAccountHeader.setCellStyle(my_style);
		HSSFCell netTotalheader = row2.createCell(1);
		netTotalheader.setCellValue("");
		netTotalheader.setCellStyle(my_style);*/

		HSSFRow row3 = spreadsheet.createRow(3);
		HSSFCell cellprofit = row3.createCell(0);
		cellprofit.setCellValue("Profit");
		cellprofit.setCellStyle(my_style);
		double prof = 0.00;
		int dataRow = 4;
		org.json.JSONArray array = new org.json.JSONArray(cf1);
		org.json.JSONArray array1 = new org.json.JSONArray(cf2);
		org.json.JSONArray array2 = new org.json.JSONArray(cf3);
		org.json.JSONArray array3 = new org.json.JSONArray(cf4);
		//org.json.JSONArray array1 = new org.json.JSONArray(liabilitiesString);
		
		//org.json.JSONArray array2 = new org.json.JSONArray(reservesSurPlus);

		for (int i = 0; i < array.length(); i++) {
			org.json.JSONObject object = array.getJSONObject(i);
			if (object.getString("account_type").contains("Profit")) {
				double total1 = object.getDouble("total");
				prof += total1;
				HSSFRow row4 = spreadsheet.createRow(dataRow);
				HSSFCell row4Data1 = row4.createCell(0);
				row4Data1.setCellValue(new HSSFRichTextString(StrUtils.fString(object.getString("account_name"))));

				String total = StrUtils.fString(object.getString("total"));
				total = StrUtils.addZeroes(Double.parseDouble(total), noOfDecimal);

				HSSFCell row4Data2 = row4.createCell(1);
				row4Data2.setCellValue(new HSSFRichTextString(total));

				dataRow++;
			}

		}

		HSSFRow row4 = spreadsheet.createRow(dataRow++);
		HSSFCell cellDepreciation = row4.createCell(0);
		cellDepreciation.setCellValue("Depreciation");
		cellDepreciation.setCellStyle(my_style);

		dataRow = dataRow++;
			
		double ncpoa = 0.00;
	
		for (int i = 0; i < array.length(); i++) {
			org.json.JSONObject object = array.getJSONObject(i);
			if (object.getString("account_type").contains("Depreciation")) {
				double total1 = object.getDouble("total");
				ncpoa += total1;
				
				row4 = spreadsheet.createRow(dataRow);
				HSSFCell row4Data1 = row4.createCell(0);
				row4Data1.setCellValue(new HSSFRichTextString(StrUtils.fString(object.getString("account_name"))));

				String total = StrUtils.fString(object.getString("total"));
				total = StrUtils.addZeroes(Double.parseDouble(total), noOfDecimal);

				HSSFCell row4Data2 = row4.createCell(1);
				row4Data2.setCellValue(new HSSFRichTextString(total));

				dataRow++;
			}

		}
		
		HSSFRow row5 = spreadsheet.createRow(dataRow++);
		HSSFCell cellIntBank = row5.createCell(0);
		cellIntBank.setCellValue("Interest on Bank borrowings / Borrowed Funds");
		cellIntBank.setCellStyle(my_style);
		dataRow = dataRow++;
		for (int i = 0; i < array.length(); i++) {
			org.json.JSONObject object = array.getJSONObject(i);
			
			if (object.getString("account_type").contains("Interest on Bank borrowings / Borrowed Funds")) {
				double total1 = object.getDouble("total");		
				ncpoa += total1;
				
				row4 = spreadsheet.createRow(dataRow);
				HSSFCell row4Data1 = row4.createCell(0);
				row4Data1.setCellValue(new HSSFRichTextString(StrUtils.fString(object.getString("account_name"))));

				String total = StrUtils.fString(object.getString("total"));
				total = StrUtils.addZeroes(Double.parseDouble(total), noOfDecimal);

				HSSFCell row4Data2 = row4.createCell(1);
				row4Data2.setCellValue(new HSSFRichTextString(total));

				dataRow++;
			}
		}
		
		HSSFRow row6 = spreadsheet.createRow(dataRow++);
		HSSFCell cellBankInt = row6.createCell(0);
		cellBankInt.setCellValue("Bank Interest");
		cellBankInt.setCellStyle(my_style);
		dataRow = dataRow++;
		for (int i = 0; i < array.length(); i++) {
			org.json.JSONObject object = array.getJSONObject(i);
			
			if (object.getString("account_type").contains("Bank Interest")) {
				double total1 = object.getDouble("total");		
				ncpoa += total1;
				
				row4 = spreadsheet.createRow(dataRow);
				HSSFCell row4Data1 = row4.createCell(0);
				row4Data1.setCellValue(new HSSFRichTextString(StrUtils.fString(object.getString("account_name"))));

				String total = StrUtils.fString(object.getString("total"));
				total = StrUtils.addZeroes(Double.parseDouble(total), noOfDecimal);

				HSSFCell row4Data2 = row4.createCell(1);
				row4Data2.setCellValue(new HSSFRichTextString(total));

				dataRow++;
			}
		}
		
		HSSFRow row7 = spreadsheet.createRow(dataRow++);
		HSSFCell cellDividend = row7.createCell(0);
		cellDividend.setCellValue("Dividend");
		cellDividend.setCellStyle(my_style);
		dataRow = dataRow++;
		for (int i = 0; i < array.length(); i++) {
			org.json.JSONObject object = array.getJSONObject(i);
			
			if (object.getString("account_type").contains("Dividend")) {
				double total1 = object.getDouble("total");		
				ncpoa += total1;
				
				row4 = spreadsheet.createRow(dataRow);
				HSSFCell row4Data1 = row4.createCell(0);
				row4Data1.setCellValue(new HSSFRichTextString(StrUtils.fString(object.getString("account_name"))));

				String total = StrUtils.fString(object.getString("total"));
				total = StrUtils.addZeroes(Double.parseDouble(total), noOfDecimal);

				HSSFCell row4Data2 = row4.createCell(1);
				row4Data2.setCellValue(new HSSFRichTextString(total));

				dataRow++;
			}
		}
		
		dataRow++;
		
		HSSFRow rowmwc = spreadsheet.createRow(dataRow++);
		HSSFCell cellMwc = rowmwc.createCell(0);                             
		cellMwc.setCellValue("Movements in working capital");
		cellMwc.setCellStyle(my_style);
		
		HSSFRow row8 = spreadsheet.createRow(dataRow++);
		HSSFCell cellDca = row8.createCell(0);
		cellDca.setCellValue("(Increase)/Decrease in Current Assets");
		cellDca.setCellStyle(my_style);
		dataRow = dataRow++;
		for (int i = 0; i < array1.length(); i++) {
			org.json.JSONObject object = array1.getJSONObject(i);
			
			if (object.getString("account_type").contains("(Increase)/Decrease in Current Assets")) {
				double total1 = object.getDouble("total");		
				ncpoa += total1;
				
				row4 = spreadsheet.createRow(dataRow);
				HSSFCell row4Data1 = row4.createCell(0);
				row4Data1.setCellValue(new HSSFRichTextString(StrUtils.fString(object.getString("account_name"))));

				String total = StrUtils.fString(object.getString("total"));
				total = StrUtils.addZeroes(Double.parseDouble(total), noOfDecimal);

				HSSFCell row4Data2 = row4.createCell(1);
				row4Data2.setCellValue(new HSSFRichTextString(total));

				dataRow++;
			}
		}
		
		HSSFRow row9 = spreadsheet.createRow(dataRow++);
		HSSFCell cellDnca = row9.createCell(0);
		cellDnca.setCellValue("(Increase)/Decrease in Non-Current Assets");
		cellDnca.setCellStyle(my_style);
		dataRow = dataRow++;
		for (int i = 0; i < array1.length(); i++) {
			org.json.JSONObject object = array1.getJSONObject(i);
			
			if (object.getString("account_type").contains("(Increase)/Decrease in Non-Current Assets")) {
				double total1 = object.getDouble("total");		
				ncpoa += total1;
				
				row4 = spreadsheet.createRow(dataRow);
				HSSFCell row4Data1 = row4.createCell(0);
				row4Data1.setCellValue(new HSSFRichTextString(StrUtils.fString(object.getString("account_name"))));

				String total = StrUtils.fString(object.getString("total"));
				total = StrUtils.addZeroes(Double.parseDouble(total), noOfDecimal);

				HSSFCell row4Data2 = row4.createCell(1);
				row4Data2.setCellValue(new HSSFRichTextString(total));

				dataRow++;
			}
		}
		
		HSSFRow row10 = spreadsheet.createRow(dataRow++);
		HSSFCell cellIcl = row10.createCell(0);
		cellIcl.setCellValue("(Decrease)/Increase in Current Liabilities");
		cellIcl.setCellStyle(my_style);
		dataRow = dataRow++;
		for (int i = 0; i < array1.length(); i++) {
			org.json.JSONObject object = array1.getJSONObject(i);
			
			if (object.getString("account_type").contains("(Decrease)/Increase in Current Liabilities")) {
				double total1 = object.getDouble("total");		
				ncpoa += total1;
				
				row4 = spreadsheet.createRow(dataRow);
				HSSFCell row4Data1 = row4.createCell(0);
				row4Data1.setCellValue(new HSSFRichTextString(StrUtils.fString(object.getString("account_name"))));

				String total = StrUtils.fString(object.getString("total"));
				total = StrUtils.addZeroes(Double.parseDouble(total), noOfDecimal);

				HSSFCell row4Data2 = row4.createCell(1);
				row4Data2.setCellValue(new HSSFRichTextString(total));

				dataRow++;
			}
		}
		
		HSSFRow row11 = spreadsheet.createRow(dataRow++);
		HSSFCell cellIp = row11.createCell(0);
		cellIp.setCellValue("(Decrease)/Increase in Provisions");
		cellIp.setCellStyle(my_style);
		dataRow = dataRow++;
		for (int i = 0; i < array1.length(); i++) {
			org.json.JSONObject object = array1.getJSONObject(i);
			
			if (object.getString("account_type").contains("(Decrease)/Increase in Provisions")) {
				double total1 = object.getDouble("total");		
				ncpoa += total1;
				
				row4 = spreadsheet.createRow(dataRow);
				HSSFCell row4Data1 = row4.createCell(0);
				row4Data1.setCellValue(new HSSFRichTextString(StrUtils.fString(object.getString("account_name"))));

				String total = StrUtils.fString(object.getString("total"));
				total = StrUtils.addZeroes(Double.parseDouble(total), noOfDecimal);

				HSSFCell row4Data2 = row4.createCell(1);
				row4Data2.setCellValue(new HSSFRichTextString(total));

				dataRow++;
			}
		}
		
		HSSFRow row12 = spreadsheet.createRow(dataRow++);
		HSSFCell cellInl = row12.createCell(0);
		cellInl.setCellValue("(Decrease)/Increase in Non-current liabilities");
		cellInl.setCellStyle(my_style);
		dataRow = dataRow++;
		for (int i = 0; i < array1.length(); i++) {
			org.json.JSONObject object = array1.getJSONObject(i);
			
			if (object.getString("account_type").contains("(Decrease)/Increase in Non-current liabilities")) {
				double total1 = object.getDouble("total");		
				ncpoa += total1;
				
				row4 = spreadsheet.createRow(dataRow);
				HSSFCell row4Data1 = row4.createCell(0);
				row4Data1.setCellValue(new HSSFRichTextString(StrUtils.fString(object.getString("account_name"))));

				String total = StrUtils.fString(object.getString("total"));
				total = StrUtils.addZeroes(Double.parseDouble(total), noOfDecimal);

				HSSFCell row4Data2 = row4.createCell(1);
				row4Data2.setCellValue(new HSSFRichTextString(total));

				dataRow++;
			}
		}
		dataRow++;
		HSSFRow rowTotal = spreadsheet.createRow(dataRow++);
		HSSFCell cellTotal = rowTotal.createCell(0);                             
		cellTotal.setCellValue("Net Cash provided by operating activity:");
		cellTotal.setCellStyle(my_style);

		String ncpoaVal = StrUtils.fString(String.valueOf(ncpoa));
		ncpoaVal = StrUtils.addZeroes(Double.parseDouble(ncpoaVal), noOfDecimal);

		HSSFCell cellTotal1 = rowTotal.createCell(1);
		cellTotal1.setCellValue(new HSSFRichTextString(ncpoaVal));
		cellTotal1.setCellStyle(my_style);
		
		dataRow++;
		
		HSSFRow rowcffia = spreadsheet.createRow(dataRow++);
		HSSFCell cellCffia = rowcffia.createCell(0);                             
		cellCffia.setCellValue("Cash flows from investing activities");
		cellCffia.setCellStyle(my_style);
		
		double cffia = 0.0;
		
		HSSFRow row13 = spreadsheet.createRow(dataRow++);
		HSSFCell cellPfa = row13.createCell(0);
		cellPfa.setCellValue("Purchase of Fixed Assets");
		cellPfa.setCellStyle(my_style);
		dataRow = dataRow++;
		for (int i = 0; i < array2.length(); i++) {
			org.json.JSONObject object = array2.getJSONObject(i);
			
			if (object.getString("account_type").contains("Purchase of Fixed Assets")) {
				double total1 = object.getDouble("total");		
				cffia += total1;
				
				row4 = spreadsheet.createRow(dataRow);
				HSSFCell row4Data1 = row4.createCell(0);
				row4Data1.setCellValue(new HSSFRichTextString(StrUtils.fString(object.getString("account_name"))));

				String total = StrUtils.fString(object.getString("total"));
				total = StrUtils.addZeroes(Double.parseDouble(total), noOfDecimal);

				HSSFCell row4Data2 = row4.createCell(1);
				row4Data2.setCellValue(new HSSFRichTextString(total));

				dataRow++;
			}
		}
		
		HSSFRow row14 = spreadsheet.createRow(dataRow++);
		HSSFCell cellCbi = row14.createCell(0);
		cellCbi.setCellValue("Bank Interest");
		cellCbi.setCellStyle(my_style);
		dataRow = dataRow++;
		for (int i = 0; i < array2.length(); i++) {
			org.json.JSONObject object = array2.getJSONObject(i);
			
			if (object.getString("account_type").contains("Bank Interest")) {
				double total1 = object.getDouble("total");		
				cffia += total1;
				
				row4 = spreadsheet.createRow(dataRow);
				HSSFCell row4Data1 = row4.createCell(0);
				row4Data1.setCellValue(new HSSFRichTextString(StrUtils.fString(object.getString("account_name"))));

				String total = StrUtils.fString(object.getString("total"));
				total = StrUtils.addZeroes(Double.parseDouble(total), noOfDecimal);

				HSSFCell row4Data2 = row4.createCell(1);
				row4Data2.setCellValue(new HSSFRichTextString(total));

				dataRow++;
			}
		}
		
		HSSFRow row15 = spreadsheet.createRow(dataRow++);
		HSSFCell cellLti = row15.createCell(0);
		cellLti.setCellValue("Long term investment");
		cellLti.setCellStyle(my_style);
		dataRow = dataRow++;
		for (int i = 0; i < array2.length(); i++) {
			org.json.JSONObject object = array2.getJSONObject(i);
			
			if (object.getString("account_type").contains("Long term investment")) {
				double total1 = object.getDouble("total");		
				cffia += total1;
				
				row4 = spreadsheet.createRow(dataRow);
				HSSFCell row4Data1 = row4.createCell(0);
				row4Data1.setCellValue(new HSSFRichTextString(StrUtils.fString(object.getString("account_name"))));

				String total = StrUtils.fString(object.getString("total"));
				total = StrUtils.addZeroes(Double.parseDouble(total), noOfDecimal);

				HSSFCell row4Data2 = row4.createCell(1);
				row4Data2.setCellValue(new HSSFRichTextString(total));

				dataRow++;
			}
		}
		
		dataRow++;
		HSSFRow rowTotala1 = spreadsheet.createRow(dataRow++);
		HSSFCell cellTotala1 = rowTotala1.createCell(0);                             
		cellTotala1.setCellValue("Net cash (used in) investing activities:");
		cellTotala1.setCellStyle(my_style);

		String cffiaVal = StrUtils.fString(String.valueOf(cffia));
		cffiaVal = StrUtils.addZeroes(Double.parseDouble(cffiaVal), noOfDecimal);

		HSSFCell cellTotalb1 = rowTotala1.createCell(1);
		cellTotalb1.setCellValue(new HSSFRichTextString(cffiaVal));
		cellTotalb1.setCellStyle(my_style);
		
		dataRow++;
		
		HSSFRow rowcfffa = spreadsheet.createRow(dataRow++);
		HSSFCell cellCfffa = rowcfffa.createCell(0);                             
		cellCfffa.setCellValue("Cash flows from financing activities");
		cellCfffa.setCellStyle(my_style);
		
		double cfffa = 0.0;
		
		HSSFRow row16 = spreadsheet.createRow(dataRow++);
		HSSFCell cellIsc = row16.createCell(0);
		cellIsc.setCellValue("Introduction of share capital");
		cellIsc.setCellStyle(my_style);
		dataRow = dataRow++;
		for (int i = 0; i < array3.length(); i++) {
			org.json.JSONObject object = array3.getJSONObject(i);
			
			if (object.getString("account_type").contains("Introduction of share capital")) {
				double total1 = object.getDouble("total");		
				cfffa += total1;
				
				row4 = spreadsheet.createRow(dataRow);
				HSSFCell row4Data1 = row4.createCell(0);
				row4Data1.setCellValue(new HSSFRichTextString(StrUtils.fString(object.getString("account_name"))));

				String total = StrUtils.fString(object.getString("total"));
				total = StrUtils.addZeroes(Double.parseDouble(total), noOfDecimal);

				HSSFCell row4Data2 = row4.createCell(1);
				row4Data2.setCellValue(new HSSFRichTextString(total));

				dataRow++;
			}
		}
		
		HSSFRow row17 = spreadsheet.createRow(dataRow++);
		HSSFCell cellBbbf = row17.createCell(0);
		cellBbbf.setCellValue("Bank Borrowings / Borrowed Funds");
		cellBbbf.setCellStyle(my_style);
		dataRow = dataRow++;
		for (int i = 0; i < array3.length(); i++) {
			org.json.JSONObject object = array3.getJSONObject(i);
			
			if (object.getString("account_type").contains("Bank Borrowings / Borrowed Funds")) {
				double total1 = object.getDouble("total");		
				cfffa += total1;
				
				row4 = spreadsheet.createRow(dataRow);
				HSSFCell row4Data1 = row4.createCell(0);
				row4Data1.setCellValue(new HSSFRichTextString(StrUtils.fString(object.getString("account_name"))));

				String total = StrUtils.fString(object.getString("total"));
				total = StrUtils.addZeroes(Double.parseDouble(total), noOfDecimal);

				HSSFCell row4Data2 = row4.createCell(1);
				row4Data2.setCellValue(new HSSFRichTextString(total));

				dataRow++;
			}
		}
		
		HSSFRow row18 = spreadsheet.createRow(dataRow++);
		HSSFCell cellIbbbf = row18.createCell(0);
		cellIbbbf.setCellValue("Interest on Bank Borrowings / Borrowed Funds");
		cellIbbbf.setCellStyle(my_style);
		dataRow = dataRow++;
		for (int i = 0; i < array3.length(); i++) {
			org.json.JSONObject object = array3.getJSONObject(i);
			
			if (object.getString("account_type").contains("Interest on Bank Borrowings / Borrowed Funds")) {
				double total1 = object.getDouble("total");		
				cfffa += total1;
				
				row4 = spreadsheet.createRow(dataRow);
				HSSFCell row4Data1 = row4.createCell(0);
				row4Data1.setCellValue(new HSSFRichTextString(StrUtils.fString(object.getString("account_name"))));

				String total = StrUtils.fString(object.getString("total"));
				total = StrUtils.addZeroes(Double.parseDouble(total), noOfDecimal);

				HSSFCell row4Data2 = row4.createCell(1);
				row4Data2.setCellValue(new HSSFRichTextString(total));

				dataRow++;
			}
		}
		
		HSSFRow row19 = spreadsheet.createRow(dataRow++);
		HSSFCell celldd = row19.createCell(0);
		celldd.setCellValue("Dividend disbursed");
		celldd.setCellStyle(my_style);
		dataRow = dataRow++;
		for (int i = 0; i < array3.length(); i++) {
			org.json.JSONObject object = array3.getJSONObject(i);
			
			if (object.getString("account_type").contains("Dividend disbursed")) {
				double total1 = object.getDouble("total");		
				cfffa += total1;
				
				row4 = spreadsheet.createRow(dataRow);
				HSSFCell row4Data1 = row4.createCell(0);
				row4Data1.setCellValue(new HSSFRichTextString(StrUtils.fString(object.getString("account_name"))));

				String total = StrUtils.fString(object.getString("total"));
				total = StrUtils.addZeroes(Double.parseDouble(total), noOfDecimal);

				HSSFCell row4Data2 = row4.createCell(1);
				row4Data2.setCellValue(new HSSFRichTextString(total));

				dataRow++;
			}
		}
		
		dataRow++;
		HSSFRow rowTotala2 = spreadsheet.createRow(dataRow++);
		HSSFCell cellTotala2 = rowTotala2.createCell(0);                             
		cellTotala2.setCellValue("Net cash generated from financing activities :");
		cellTotala2.setCellStyle(my_style);

		String cfffaVal = StrUtils.fString(String.valueOf(cfffa));
		cfffaVal = StrUtils.addZeroes(Double.parseDouble(cfffaVal), noOfDecimal);

		HSSFCell cellTotalb2 = rowTotala2.createCell(1);
		cellTotalb2.setCellValue(new HSSFRichTextString(cfffaVal));
		cellTotalb2.setCellStyle(my_style);
		
		double alltotal = prof+ncpoa+cffia+cfffa;
		
		dataRow++;
		HSSFRow rowTotala3 = spreadsheet.createRow(dataRow++);
		HSSFCell cellTotala3 = rowTotala3.createCell(0);                             
		cellTotala3.setCellValue("Net increase in cash at bank :");
		cellTotala3.setCellStyle(my_style);

		String Val1 = StrUtils.fString(String.valueOf(alltotal));
		Val1 = StrUtils.addZeroes(Double.parseDouble(Val1), noOfDecimal);

		HSSFCell cellTotalb3 = rowTotala3.createCell(1);
		cellTotalb3.setCellValue(new HSSFRichTextString(Val1));
		cellTotalb3.setCellStyle(my_style);
		
		HSSFRow rowTotala4 = spreadsheet.createRow(dataRow++);
		HSSFCell cellTotala4 = rowTotala4.createCell(0);                             
		cellTotala4.setCellValue("Cash at bank at beginning of the year :");
		cellTotala4.setCellStyle(my_style);
		
		String Val2 = StrUtils.fString(String.valueOf(cashflow.get("OPENBALANCE")));
		Val2 = StrUtils.addZeroes(Double.parseDouble(Val2), noOfDecimal);

		HSSFCell cellTotalb4 = rowTotala4.createCell(1);
		cellTotalb4.setCellValue(new HSSFRichTextString(Val2));
		cellTotalb4.setCellStyle(my_style);

		dataRow++;
		HSSFRow rowTotala5 = spreadsheet.createRow(dataRow++);
		HSSFCell cellTotala5 = rowTotala5.createCell(0);                             
		cellTotala5.setCellValue("Cash at bank at end of the year :");
		cellTotala5.setCellStyle(my_style);

		String Val3 = StrUtils.fString(String.valueOf(cashflow.get("CLOSEBALANCE")));
		Val3 = StrUtils.addZeroes(Double.parseDouble(Val3), noOfDecimal);

		HSSFCell cellTotalb5 = rowTotala5.createCell(1);
		cellTotalb5.setCellValue(new HSSFRichTextString(Val3));
		cellTotalb5.setCellStyle(my_style);
		
		
		spreadsheet.autoSizeColumn(0);
		spreadsheet.autoSizeColumn(1);
		spreadsheet.autoSizeColumn(2);

		return workbook;
	}
	
	@Override
	public HashMap<String, String> populateMapData(String companyCode, String userCode) {
		return null;
	}

	@Override
	public void setMapDataToLogger(HashMap<String, String> dataForLogging) {
	}

}
