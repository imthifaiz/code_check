package com.track.servlet;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

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
import com.track.dao.PlantMstDAO;
import com.track.db.object.BalanceSheet;
import com.track.db.object.Journal;
import com.track.db.object.JournalDetail;
import com.track.db.object.TrialBalanceDetails;
import com.track.service.JournalService;
import com.track.service.ReportService;
import com.track.serviceImplementation.JournalEntry;
import com.track.serviceImplementation.ReportServiceImpl;
import com.track.util.IMLogger;
import com.track.util.MLogger;
import com.track.util.StrUtils;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@WebServlet("/BalanceSheetServlet")
public class BalanceSheetServlet extends HttpServlet implements IMLogger {

	private static final long serialVersionUID = 1L;
	private boolean printLog = MLoggerConstant.ProfitLossServlet_PRINTPLANTMASTERLOG;
	private boolean printInfo = MLoggerConstant.ProfitLossServlet_PRINTPLANTMASTERINFO;
	String action = "";
	private JournalService journalService = new JournalEntry();
	private ReportService reportService = new ReportServiceImpl();

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		action = StrUtils.fString(request.getParameter("action")).trim();

		if (action.equalsIgnoreCase("getBalanceSheetExcel")) {

			String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
			String user = StrUtils.fString((String) request.getSession().getAttribute("LOGIN_USER")).trim();
			String fromDate = StrUtils.fString(request.getParameter("fromDate")).trim();
			String toDate = StrUtils.fString(request.getParameter("toDate")).trim();
			List<String> list = Arrays.asList("('1','2','3')", "('4','5','6')", "('7','12','13')");
			List<JSONArray> excelDataArray = new ArrayList<>();
			int i = 1;
			for (String account : list) {

				JSONArray jarray = new JSONArray();
				ArrayList<Integer> subids = new ArrayList<Integer>();
				Map<Integer, BalanceSheet> BalanceSheetArr = new Hashtable<Integer, BalanceSheet>();
				Map<Integer, BalanceSheet> bs1 = new Hashtable<Integer, BalanceSheet>();
				Map<Integer, BalanceSheet> bs2 = new Hashtable<Integer, BalanceSheet>();
				try {

					Set<Integer> AccountId = new HashSet<Integer>();

					List<JournalDetail> journalDetails = journalService.getJournalDetailsByAccountTypeAndDATE(plant,
							fromDate, toDate, account);

					for (JournalDetail journalDet : journalDetails) {
						if (!AccountId.contains(journalDet.getACCOUNT_ID())) {
							AccountId.add(journalDet.getACCOUNT_ID());
							CoaDAO coaDAO = new CoaDAO();

							JSONObject coaFullDetail = coaDAO.CoaFullRecord(plant,
									String.valueOf(journalDet.getACCOUNT_ID()));
							JSONObject coaJson = (JSONObject) coaFullDetail.get("results");
							// jarray.add(coaFullDetail.get("results"));
							System.out.println("CoaJson" + coaJson);
							if (coaJson != null) {
								BalanceSheet balanceSheet = new BalanceSheet();
								balanceSheet.setAccount_id(journalDet.getACCOUNT_ID());
								String accountcode = coaDAO.GetAccountCodeByName(coaJson.getString("account_name"), plant);
								balanceSheet.setAccount_name(accountcode+" "+coaJson.getString("account_name"));
								
								int sid = 0;
								String subaccountname = "";
								if(coaJson.getString("issub_account").equalsIgnoreCase("1")) {
									subaccountname = coaDAO.GetAccountNameById(coaJson.getString("sub_account"), plant);
									sid = Integer.valueOf(coaJson.getString("sub_account"));
								}else {
									subaccountname = coaJson.getString("account_name");
								}
								String subaccountcode = coaDAO.GetAccountCodeByName(subaccountname, plant);
								balanceSheet.setSub_account(subaccountcode+" "+subaccountname);
								balanceSheet.setSub_account_id(sid);
								if(sid != 0) {
									if(!subids.contains(sid)) {
										subids.add(sid);
									}
								}
								
								balanceSheet.setAccount_type(coaJson.getString("account_type_name"));
								balanceSheet.setExtended_type(coaJson.getString("account_det_type"));
								balanceSheet.setMain_account(coaJson.getString("mainaccname"));
								balanceSheet.setNet_credit(journalDet.getCREDITS());
								balanceSheet.setNet_debit(journalDet.getDEBITS());
								BalanceSheetArr.put(journalDet.getACCOUNT_ID(), balanceSheet);
								if(coaJson.getString("issub_account").equalsIgnoreCase("1")) {
									bs2.put(journalDet.getACCOUNT_ID(), balanceSheet);
								}else {
									bs1.put(journalDet.getACCOUNT_ID(), balanceSheet);
								}
							}

							// trialBalArr.put(coaJson.getString("ACCOUNT_NAME"), journalDet.get)
						} else {
							BalanceSheet balanceSheet = BalanceSheetArr.get(journalDet.getACCOUNT_ID());
							if (balanceSheet != null) {
								CoaDAO coaDAO = new CoaDAO();
								
								JSONObject coaFullDetail = coaDAO.CoaFullRecord(plant,
										String.valueOf(journalDet.getACCOUNT_ID()));
								JSONObject coaJson = (JSONObject) coaFullDetail.get("results");
								Double debit = balanceSheet.getNet_debit() + journalDet.getDEBITS();
								Double credit = balanceSheet.getNet_credit() + journalDet.getCREDITS();
								balanceSheet.setNet_debit(debit);
								balanceSheet.setNet_credit(credit);
								BalanceSheetArr.replace(journalDet.getACCOUNT_ID(), balanceSheet);
								if(coaJson.getString("issub_account").equalsIgnoreCase("1")) {
									bs2.replace(journalDet.getACCOUNT_ID(), balanceSheet);
								}else {
									bs1.replace(journalDet.getACCOUNT_ID(), balanceSheet);
								}
							}

						}
					}
					if (i == 1) {
						BalanceSheetArr.forEach((k, v) -> {
							// jarray.add(v);
							Double total = v.getNet_debit() - v.getNet_credit();
							v.setTotal(total);

							// Double total=v.getNet_credit()-v.getNet_debit();

							BalanceSheetArr.replace(k, v);
						});
					} else {
						BalanceSheetArr.forEach((k, v) -> {

							Double total = v.getNet_credit() - v.getNet_debit();
							v.setTotal(total);

							// Double total=v.getNet_credit()-v.getNet_debit();

							BalanceSheetArr.replace(k, v);
						});
					}

					/*BalanceSheetArr.forEach((k, v) -> {
						jarray.add(v);
					});*/
					// jarray.add(trialBalArr.values().toArray());
					bs1.forEach((k, v) -> {
						jarray.add(v);
					});
					
					for (int j = 0; j < jarray.size(); j++) {
						JSONObject object = jarray.getJSONObject(j);
						JSONArray childArray = new JSONArray();
						object.put("_children", childArray);
					}
					
					for (int s = 0; s < subids.size(); s++) {
						JSONArray childArray = new JSONArray();
						/*String accname = "";
						String mainaccname = "";*/
						Double subtotal = 0.0;
						int lsubid = subids.get(s);
						JSONObject newNode = new JSONObject();
						/*bs2.forEach((k, v) -> {
							if(Integer.valueOf(v.getSub_account_id()) == lsubid) {
								childArray.add(v);
								newNode.put("account_name",v.getSub_account());
								newNode.put("main_account",v.getMain_account());
								newNode.put("account_type",v.getAccount_type());
								newNode.put("total",v.getTotal());
							}
						});*/
						
						for(Map.Entry<Integer, BalanceSheet> entry : bs2.entrySet()) {
							Integer key = entry.getKey();
							BalanceSheet value = entry.getValue();
							if(value.getSub_account_id() == lsubid) {
								childArray.add(value);
								newNode.put("account_name",value.getSub_account());
								newNode.put("account_type",value.getAccount_type());
								newNode.put("account_id","0");
								subtotal = subtotal + value.getTotal();
								newNode.put("total",subtotal);
							}
						}

						newNode.put("_children", childArray);
						
						jarray.add(newNode);
					}
					
					
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				excelDataArray.add(jarray);
				
				i++;
			}

			JSONObject profitloss = new JSONObject();
			Double current_earnings = 0.00;
			try {
				current_earnings = reportService.NetProfit(plant, fromDate, toDate);
				profitloss.put("currentearnings", current_earnings);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println(excelDataArray);
			System.out.println(profitloss);

			if (!fromDate.isEmpty()) {
				PlantMstDAO _PlantMstDAO = new PlantMstDAO();

				HSSFWorkbook workbook = null;
				try {
					String numberOfDecimal = _PlantMstDAO.getNumberOfDecimal(plant);
					workbook = populateExcel("Balance Sheet", toDate, excelDataArray, numberOfDecimal, profitloss);
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
				response.setHeader("Content-Disposition", "attachment; filename=BalanceSheet.xls");
				OutputStream outStream = response.getOutputStream();
				outStream.write(outArray);
				outStream.flush();
				outStream.close();
			}

		}else if (action.equalsIgnoreCase("getBalanceSheetReport")) {
			String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
			String fromDate = "2021-01-01";
			String toDate = "2021-12-31";
			String accounttypes = "('1','2','3')";
			List<Journal> journalList = null;
			JSONArray jarray = new JSONArray();
			ArrayList<Integer> subids = new ArrayList<Integer>();
			Map<Integer, BalanceSheet> BalanceSheetArr = new Hashtable<Integer, BalanceSheet>();
			Map<Integer, BalanceSheet> bs1 = new Hashtable<Integer, BalanceSheet>();
			Map<Integer, BalanceSheet> bs2 = new Hashtable<Integer, BalanceSheet>();
			
			
			try {
				// journalList=journalService.getAllJournalHeader(plant, user, type);
				// journalList=journalService.getJournalsByDateWithoutAttach(plant, fromDate,
				// toDate);
				Set<Integer> AccountId = new HashSet<Integer>();

				// String accounttypes= "('8','9')";
				List<JournalDetail> journalDetails = journalService.getJournalDetailsByAccountTypeAndDATE(plant,
						fromDate, toDate, accounttypes);

				for (JournalDetail journalDet : journalDetails) {
					if (!AccountId.contains(journalDet.getACCOUNT_ID())) {
						AccountId.add(journalDet.getACCOUNT_ID());
						CoaDAO coaDAO = new CoaDAO();
						
						JSONObject coaFullDetail = coaDAO.CoaFullRecord(plant,
								String.valueOf(journalDet.getACCOUNT_ID()));
						JSONObject coaJson = (JSONObject) coaFullDetail.get("results");
						// jarray.add(coaFullDetail.get("results"));
						System.out.println("CoaJson" + coaJson);
						if (coaJson != null) {
							BalanceSheet balanceSheet = new BalanceSheet();
							balanceSheet.setAccount_id(journalDet.getACCOUNT_ID());
							String accountcode = coaDAO.GetAccountCodeByName(coaJson.getString("account_name"), plant);
							
							String subaccountname = "";
							int sid = 0;
							if(coaJson.getString("issub_account").equalsIgnoreCase("1")) {
								subaccountname = coaDAO.GetAccountNameById(coaJson.getString("sub_account"), plant);
								sid = Integer.valueOf(coaJson.getString("sub_account"));
							}else {
								subaccountname = coaJson.getString("account_name");
							}
							String subaccountcode = coaDAO.GetAccountCodeByName(subaccountname, plant);
							balanceSheet.setSub_account(subaccountcode+" "+subaccountname);
							balanceSheet.setSub_account_id(sid);
							if(sid != 0) {
								if(!subids.contains(sid)) {
									subids.add(sid);
								}
							}
							balanceSheet.setAccount_name(accountcode+" "+coaJson.getString("account_name"));
							balanceSheet.setAccount_type(coaJson.getString("account_type_name"));
							balanceSheet.setExtended_type(coaJson.getString("account_det_type"));
							balanceSheet.setMain_account(coaJson.getString("mainaccname"));
							balanceSheet.setNet_credit(journalDet.getCREDITS());
							balanceSheet.setNet_debit(journalDet.getDEBITS());
							BalanceSheetArr.put(journalDet.getACCOUNT_ID(), balanceSheet);
							if(coaJson.getString("issub_account").equalsIgnoreCase("1")) {
								bs2.put(journalDet.getACCOUNT_ID(), balanceSheet);
							}else {
								bs1.put(journalDet.getACCOUNT_ID(), balanceSheet);
							}
						}

						// trialBalArr.put(coaJson.getString("ACCOUNT_NAME"), journalDet.get)
					} else {
						BalanceSheet balanceSheet = BalanceSheetArr.get(journalDet.getACCOUNT_ID());
						if (balanceSheet != null) {
							CoaDAO coaDAO = new CoaDAO();
							
							JSONObject coaFullDetail = coaDAO.CoaFullRecord(plant,
									String.valueOf(journalDet.getACCOUNT_ID()));
							JSONObject coaJson = (JSONObject) coaFullDetail.get("results");
							Double debit = balanceSheet.getNet_debit() + journalDet.getDEBITS();
							Double credit = balanceSheet.getNet_credit() + journalDet.getCREDITS();
							balanceSheet.setNet_debit(debit);
							balanceSheet.setNet_credit(credit);
							BalanceSheetArr.replace(journalDet.getACCOUNT_ID(), balanceSheet);
							if(coaJson.getString("issub_account").equalsIgnoreCase("1")) {
								bs2.replace(journalDet.getACCOUNT_ID(), balanceSheet);
							}else {
								bs1.replace(journalDet.getACCOUNT_ID(), balanceSheet);
							}
						}

					}
				}

				BalanceSheetArr.forEach((k, v) -> {
					// jarray.add(v);
					Double total = v.getNet_debit() - v.getNet_credit();
					// Double total=v.getNet_credit()-v.getNet_debit();
					v.setTotal(total);
					BalanceSheetArr.replace(k, v);
				});

				bs1.forEach((k, v) -> {
					jarray.add(v);
				});
				
				for (int i = 0; i < subids.size(); i++) {
					JSONArray childArray = new JSONArray();
					/*String accname = "";
					String mainaccname = "";
					Double subtotal = 0.0;*/
					int lsubid = subids.get(i);
					JSONObject newNode = new JSONObject();
					bs2.forEach((k, v) -> {
						if(Integer.valueOf(v.getSub_account_id()) == lsubid) {
							childArray.add(v);
							newNode.put("account_name",v.getAccount_name());
							newNode.put("main_account",v.getMain_account());
							newNode.put("total",v.getTotal());
						}
					});

					newNode.put("_children", childArray);
					
					jarray.add(newNode);
				}
				
				
				// jarray.add(trialBalArr.values().toArray());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			response.getWriter().write(jarray.toString());
			response.getWriter().flush();
			response.getWriter().close();
		} 

	}

	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		action = StrUtils.fString(req.getParameter("action")).trim();
		String plant = StrUtils.fString((String) req.getSession().getAttribute("PLANT")).trim();
		String user = StrUtils.fString((String) req.getSession().getAttribute("LOGIN_USER")).trim();
		String type = "ALL";

		if (action.equalsIgnoreCase("getBalanceSheetAsset")) {
			String fromDate = StrUtils.fString(req.getParameter("fromDate")).trim();
			String toDate = StrUtils.fString(req.getParameter("toDate")).trim();
			String accounttypes = StrUtils.fString(req.getParameter("accounttypes")).trim();
			List<Journal> journalList = null;
			JSONArray jarray = new JSONArray();
			Map<Integer, BalanceSheet> BalanceSheetArr = new Hashtable<Integer, BalanceSheet>();
			try {
				// journalList=journalService.getAllJournalHeader(plant, user, type);
				// journalList=journalService.getJournalsByDateWithoutAttach(plant, fromDate,
				// toDate);
				Set<Integer> AccountId = new HashSet<Integer>();

				// String accounttypes= "('8','9')";
				List<JournalDetail> journalDetails = journalService.getJournalDetailsByAccountTypeAndDATE(plant,
						fromDate, toDate, accounttypes);

				for (JournalDetail journalDet : journalDetails) {
					if (!AccountId.contains(journalDet.getACCOUNT_ID())) {
						AccountId.add(journalDet.getACCOUNT_ID());
						CoaDAO coaDAO = new CoaDAO();
						
						JSONObject coaFullDetail = coaDAO.CoaFullRecord(plant,
								String.valueOf(journalDet.getACCOUNT_ID()));
						JSONObject coaJson = (JSONObject) coaFullDetail.get("results");
						// jarray.add(coaFullDetail.get("results"));
						System.out.println("CoaJson" + coaJson);
						if (coaJson != null) {
							BalanceSheet balanceSheet = new BalanceSheet();
							balanceSheet.setAccount_id(journalDet.getACCOUNT_ID());
							String accountcode = coaDAO.GetAccountCodeByName(coaJson.getString("account_name"), plant);
							
							String subaccountname = "";
							if(coaJson.getString("issub_account").equalsIgnoreCase("1")) {
								subaccountname = coaDAO.GetAccountNameById(coaJson.getString("sub_account"), plant);
							}else {
								subaccountname = coaJson.getString("account_name");
							}
							String subaccountcode = coaDAO.GetAccountCodeByName(subaccountname, plant);
							balanceSheet.setSub_account(subaccountcode+" "+subaccountname);
							
							balanceSheet.setAccount_name(accountcode+" "+coaJson.getString("account_name"));
							balanceSheet.setAccount_type(coaJson.getString("account_type_name"));
							balanceSheet.setExtended_type(coaJson.getString("account_det_type"));
							balanceSheet.setMain_account(coaJson.getString("mainaccname"));
							balanceSheet.setNet_credit(journalDet.getCREDITS());
							balanceSheet.setNet_debit(journalDet.getDEBITS());
							BalanceSheetArr.put(journalDet.getACCOUNT_ID(), balanceSheet);
						}

						// trialBalArr.put(coaJson.getString("ACCOUNT_NAME"), journalDet.get)
					} else {
						BalanceSheet balanceSheet = BalanceSheetArr.get(journalDet.getACCOUNT_ID());
						if (balanceSheet != null) {
							Double debit = balanceSheet.getNet_debit() + journalDet.getDEBITS();
							Double credit = balanceSheet.getNet_credit() + journalDet.getCREDITS();
							balanceSheet.setNet_debit(debit);
							balanceSheet.setNet_credit(credit);
							BalanceSheetArr.replace(journalDet.getACCOUNT_ID(), balanceSheet);
						}

					}
				}

				BalanceSheetArr.forEach((k, v) -> {
					// jarray.add(v);
					Double total = v.getNet_debit() - v.getNet_credit();
					// Double total=v.getNet_credit()-v.getNet_debit();
					v.setTotal(total);
					BalanceSheetArr.replace(k, v);
				});

				BalanceSheetArr.forEach((k, v) -> {
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
		}else if (action.equalsIgnoreCase("getBalanceSheetReport")) {
			String fromDate = StrUtils.fString(req.getParameter("fromDate")).trim();
			String toDate = StrUtils.fString(req.getParameter("toDate")).trim();
			String accounttypes = StrUtils.fString(req.getParameter("accounttypes")).trim();
			List<Journal> journalList = null;
			JSONArray jarray = new JSONArray();
			ArrayList<Integer> subids = new ArrayList<Integer>();
			Map<Integer, BalanceSheet> BalanceSheetArr = new Hashtable<Integer, BalanceSheet>();
			Map<Integer, BalanceSheet> bs1 = new Hashtable<Integer, BalanceSheet>();
			Map<Integer, BalanceSheet> bs2 = new Hashtable<Integer, BalanceSheet>();
			
			
			try {
				// journalList=journalService.getAllJournalHeader(plant, user, type);
				// journalList=journalService.getJournalsByDateWithoutAttach(plant, fromDate,
				// toDate);
				Set<Integer> AccountId = new HashSet<Integer>();

				// String accounttypes= "('8','9')";
				List<JournalDetail> journalDetails = journalService.getJournalDetailsByAccountTypeAndDATE(plant,
						fromDate, toDate, accounttypes);

				for (JournalDetail journalDet : journalDetails) {
					if (!AccountId.contains(journalDet.getACCOUNT_ID())) {
						AccountId.add(journalDet.getACCOUNT_ID());
						CoaDAO coaDAO = new CoaDAO();
						
						JSONObject coaFullDetail = coaDAO.CoaFullRecord(plant,
								String.valueOf(journalDet.getACCOUNT_ID()));
						JSONObject coaJson = (JSONObject) coaFullDetail.get("results");
						// jarray.add(coaFullDetail.get("results"));
						System.out.println("CoaJson" + coaJson);
						if (coaJson != null) {
							BalanceSheet balanceSheet = new BalanceSheet();
							balanceSheet.setAccount_id(journalDet.getACCOUNT_ID());
							String accountcode = coaDAO.GetAccountCodeByName(coaJson.getString("account_name"), plant);
							
							String subaccountname = "";
							int sid = 0;
							if(coaJson.getString("issub_account").equalsIgnoreCase("1")) {
								subaccountname = coaDAO.GetAccountNameById(coaJson.getString("sub_account"), plant);
								sid = Integer.valueOf(coaJson.getString("sub_account"));
							}else {
								subaccountname = coaJson.getString("account_name");
							}
							String subaccountcode = coaDAO.GetAccountCodeByName(subaccountname, plant);
							balanceSheet.setSub_account(subaccountcode+" "+subaccountname);
							balanceSheet.setSub_account_id(sid);
							if(sid != 0) {
								if(!subids.contains(sid)) {
									subids.add(sid);
								}
							}
							balanceSheet.setAccount_name(accountcode+" "+coaJson.getString("account_name"));
							balanceSheet.setAccount_type(coaJson.getString("account_type_name"));
							balanceSheet.setExtended_type(coaJson.getString("account_det_type"));
							balanceSheet.setMain_account(coaJson.getString("mainaccname"));
							balanceSheet.setNet_credit(journalDet.getCREDITS());
							balanceSheet.setNet_debit(journalDet.getDEBITS());
							BalanceSheetArr.put(journalDet.getACCOUNT_ID(), balanceSheet);
							if(coaJson.getString("issub_account").equalsIgnoreCase("1")) {
								bs2.put(journalDet.getACCOUNT_ID(), balanceSheet);
							}else {
								bs1.put(journalDet.getACCOUNT_ID(), balanceSheet);
							}
						}

						// trialBalArr.put(coaJson.getString("ACCOUNT_NAME"), journalDet.get)
					} else {
						BalanceSheet balanceSheet = BalanceSheetArr.get(journalDet.getACCOUNT_ID());
						if (balanceSheet != null) {
							CoaDAO coaDAO = new CoaDAO();
							
							JSONObject coaFullDetail = coaDAO.CoaFullRecord(plant,
									String.valueOf(journalDet.getACCOUNT_ID()));
							JSONObject coaJson = (JSONObject) coaFullDetail.get("results");
							Double debit = balanceSheet.getNet_debit() + journalDet.getDEBITS();
							Double credit = balanceSheet.getNet_credit() + journalDet.getCREDITS();
							balanceSheet.setNet_debit(debit);
							balanceSheet.setNet_credit(credit);
							BalanceSheetArr.replace(journalDet.getACCOUNT_ID(), balanceSheet);
							if(coaJson.getString("issub_account").equalsIgnoreCase("1")) {
								bs2.replace(journalDet.getACCOUNT_ID(), balanceSheet);
							}else {
								bs1.replace(journalDet.getACCOUNT_ID(), balanceSheet);
							}
						}

					}
				}

				BalanceSheetArr.forEach((k, v) -> {
					// jarray.add(v);
					Double total = v.getNet_debit() - v.getNet_credit();
					// Double total=v.getNet_credit()-v.getNet_debit();
					v.setTotal(total);
					BalanceSheetArr.replace(k, v);
				});

				bs1.forEach((k, v) -> {
					jarray.add(v);
				});
				
				for (int i = 0; i < subids.size(); i++) {
					JSONArray childArray = new JSONArray();
					JSONArray addtotal = new JSONArray();
					/*String accname = "";
					String mainaccname = "";*/
					Double subtotal = 0.0;
					int lsubid = subids.get(i);
					JSONObject newNode = new JSONObject();
					/*bs2.forEach((k, v) -> {
						if(Integer.valueOf(v.getSub_account_id()) == lsubid) {
							childArray.add(v);
							newNode.put("account_name",v.getSub_account());
							newNode.put("account_type",v.getAccount_type());
							newNode.put("account_id","0");
							newNode.put("total",v.getTotal());
							addtotal.add(v);
						}
					});*/
					
					for(Map.Entry<Integer, BalanceSheet> entry : bs2.entrySet()) {
						Integer key = entry.getKey();
						BalanceSheet value = entry.getValue();
						if(value.getSub_account_id() == lsubid) {
							childArray.add(value);
							newNode.put("account_name",value.getSub_account());
							newNode.put("account_type",value.getAccount_type());
							newNode.put("account_id","0");
							subtotal = subtotal + value.getTotal();
							newNode.put("total",subtotal);
						}
					}
					
					newNode.put("_children", childArray);
					
					jarray.add(newNode);
				}
				
				
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
		}else if (action.equalsIgnoreCase("getBalanceSheetReport2")) {
			String fromDate = StrUtils.fString(req.getParameter("fromDate")).trim();
			String toDate = StrUtils.fString(req.getParameter("toDate")).trim();
			String accounttypes = StrUtils.fString(req.getParameter("accounttypes")).trim();
			List<Journal> journalList = null;
			JSONArray jarray = new JSONArray();
			ArrayList<Integer> subids = new ArrayList<Integer>();
			Map<Integer, BalanceSheet> BalanceSheetArr = new Hashtable<Integer, BalanceSheet>();
			Map<Integer, BalanceSheet> bs1 = new Hashtable<Integer, BalanceSheet>();
			Map<Integer, BalanceSheet> bs2 = new Hashtable<Integer, BalanceSheet>();
			
			
			try {
				// journalList=journalService.getAllJournalHeader(plant, user, type);
				// journalList=journalService.getJournalsByDateWithoutAttach(plant, fromDate,
				// toDate);
				Set<Integer> AccountId = new HashSet<Integer>();

				// String accounttypes= "('8','9')";
				List<JournalDetail> journalDetails = journalService.getJournalDetailsByAccountTypeAndDATE(plant,
						fromDate, toDate, accounttypes);

				for (JournalDetail journalDet : journalDetails) {
					if (!AccountId.contains(journalDet.getACCOUNT_ID())) {
						AccountId.add(journalDet.getACCOUNT_ID());
						CoaDAO coaDAO = new CoaDAO();
						
						JSONObject coaFullDetail = coaDAO.CoaFullRecord(plant,
								String.valueOf(journalDet.getACCOUNT_ID()));
						JSONObject coaJson = (JSONObject) coaFullDetail.get("results");
						// jarray.add(coaFullDetail.get("results"));
						System.out.println("CoaJson" + coaJson);
						if (coaJson != null) {
							BalanceSheet balanceSheet = new BalanceSheet();
							balanceSheet.setAccount_id(journalDet.getACCOUNT_ID());
							String accountcode = coaDAO.GetAccountCodeByName(coaJson.getString("account_name"), plant);
							
							String subaccountname = "";
							int sid = 0;
							if(coaJson.getString("issub_account").equalsIgnoreCase("1")) {
								subaccountname = coaDAO.GetAccountNameById(coaJson.getString("sub_account"), plant);
								sid = Integer.valueOf(coaJson.getString("sub_account"));
							}else {
								subaccountname = coaJson.getString("account_name");
							}
							String subaccountcode = coaDAO.GetAccountCodeByName(subaccountname, plant);
							balanceSheet.setSub_account(subaccountcode+" "+subaccountname);
							balanceSheet.setSub_account_id(sid);
							if(sid != 0) {
								if(!subids.contains(sid)) {
									subids.add(sid);
								}
							}
							balanceSheet.setAccount_name(accountcode+" "+coaJson.getString("account_name"));
							balanceSheet.setAccount_type(coaJson.getString("account_type_name"));
							balanceSheet.setExtended_type(coaJson.getString("account_det_type"));
							balanceSheet.setMain_account(coaJson.getString("mainaccname"));
							balanceSheet.setNet_credit(journalDet.getCREDITS());
							balanceSheet.setNet_debit(journalDet.getDEBITS());
							BalanceSheetArr.put(journalDet.getACCOUNT_ID(), balanceSheet);
							if(coaJson.getString("issub_account").equalsIgnoreCase("1")) {
								bs2.put(journalDet.getACCOUNT_ID(), balanceSheet);
							}else {
								bs1.put(journalDet.getACCOUNT_ID(), balanceSheet);
							}
						}

						// trialBalArr.put(coaJson.getString("ACCOUNT_NAME"), journalDet.get)
					} else {
						BalanceSheet balanceSheet = BalanceSheetArr.get(journalDet.getACCOUNT_ID());
						if (balanceSheet != null) {
							CoaDAO coaDAO = new CoaDAO();
							
							JSONObject coaFullDetail = coaDAO.CoaFullRecord(plant,
									String.valueOf(journalDet.getACCOUNT_ID()));
							JSONObject coaJson = (JSONObject) coaFullDetail.get("results");
							Double debit = balanceSheet.getNet_debit() + journalDet.getDEBITS();
							Double credit = balanceSheet.getNet_credit() + journalDet.getCREDITS();
							balanceSheet.setNet_debit(debit);
							balanceSheet.setNet_credit(credit);
							BalanceSheetArr.replace(journalDet.getACCOUNT_ID(), balanceSheet);
							if(coaJson.getString("issub_account").equalsIgnoreCase("1")) {
								bs2.replace(journalDet.getACCOUNT_ID(), balanceSheet);
							}else {
								bs1.replace(journalDet.getACCOUNT_ID(), balanceSheet);
							}
						}

					}
				}

				BalanceSheetArr.forEach((k, v) -> {
					// jarray.add(v);
					//Double total = v.getNet_debit() - v.getNet_credit();
					Double total=v.getNet_credit()-v.getNet_debit();
					v.setTotal(total);
					BalanceSheetArr.replace(k, v);
				});

				bs1.forEach((k, v) -> {
					jarray.add(v);
				});
				
				for (int i = 0; i < subids.size(); i++) {
					JSONArray childArray = new JSONArray();
					JSONArray addtotal = new JSONArray();
					/*String accname = "";
					String mainaccname = "";*/
					Double subtotal = 0.0;
					int lsubid = subids.get(i);
					JSONObject newNode = new JSONObject();
					/*bs2.forEach((k, v) -> {
						if(Integer.valueOf(v.getSub_account_id()) == lsubid) {
							childArray.add(v);
							newNode.put("account_name",v.getSub_account());
							newNode.put("account_type",v.getAccount_type());
							newNode.put("account_id","0");
							newNode.put("total",v.getTotal());
							addtotal.add(v);
						}
					});*/
					
					for(Map.Entry<Integer, BalanceSheet> entry : bs2.entrySet()) {
						Integer key = entry.getKey();
						BalanceSheet value = entry.getValue();
						if(value.getSub_account_id() == lsubid) {
							childArray.add(value);
							newNode.put("account_name",value.getSub_account());
							newNode.put("account_type",value.getAccount_type());
							newNode.put("account_id","0");
							subtotal = subtotal + value.getTotal();
							newNode.put("total",subtotal);
						}
					}
					
					newNode.put("_children", childArray);
					
					jarray.add(newNode);
				}
				
				
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
		}  else if (action.equalsIgnoreCase("getBalanceSheetLiability")) {
			String fromDate = StrUtils.fString(req.getParameter("fromDate")).trim();
			String toDate = StrUtils.fString(req.getParameter("toDate")).trim();
			String accounttypes = StrUtils.fString(req.getParameter("accounttypes")).trim();
			List<Journal> journalList = null;
			JSONArray jarray = new JSONArray();
			Map<Integer, BalanceSheet> BalanceSheetArr = new Hashtable<Integer, BalanceSheet>();
			try {
				// journalList=journalService.getAllJournalHeader(plant, user, type);
				// journalList=journalService.getJournalsByDateWithoutAttach(plant, fromDate,
				// toDate);
				Set<Integer> AccountId = new HashSet<Integer>();

				// String accounttypes= "('8','9')";
				List<JournalDetail> journalDetails = journalService.getJournalDetailsByAccountTypeAndDATE(plant,
						fromDate, toDate, accounttypes);

				for (JournalDetail journalDet : journalDetails) {
					if (!AccountId.contains(journalDet.getACCOUNT_ID())) {
						AccountId.add(journalDet.getACCOUNT_ID());
						CoaDAO coaDAO = new CoaDAO();

						JSONObject coaFullDetail = coaDAO.CoaFullRecord(plant,
								String.valueOf(journalDet.getACCOUNT_ID()));
						JSONObject coaJson = (JSONObject) coaFullDetail.get("results");
						// jarray.add(coaFullDetail.get("results"));
						System.out.println("CoaJson" + coaJson);
						if (coaJson != null) {
							BalanceSheet balanceSheet = new BalanceSheet();
							balanceSheet.setAccount_id(journalDet.getACCOUNT_ID());
							String accountcode = coaDAO.GetAccountCodeByName(coaJson.getString("account_name"), plant);
							String subaccountname = "";
							if(coaJson.getString("issub_account").equalsIgnoreCase("1")) {
								subaccountname = coaDAO.GetAccountNameById(coaJson.getString("sub_account"), plant);
							}else {
								subaccountname = coaJson.getString("account_name");
							}
							String subaccountcode = coaDAO.GetAccountCodeByName(subaccountname, plant);
							balanceSheet.setSub_account(subaccountcode+" "+subaccountname);
							balanceSheet.setAccount_name(accountcode+" "+coaJson.getString("account_name"));
							balanceSheet.setAccount_type(coaJson.getString("account_type_name"));
							balanceSheet.setExtended_type(coaJson.getString("account_det_type"));
							balanceSheet.setMain_account(coaJson.getString("mainaccname"));
							balanceSheet.setNet_credit(journalDet.getCREDITS());
							balanceSheet.setNet_debit(journalDet.getDEBITS());
							BalanceSheetArr.put(journalDet.getACCOUNT_ID(), balanceSheet);
						}

						// trialBalArr.put(coaJson.getString("ACCOUNT_NAME"), journalDet.get)
					} else {
						BalanceSheet balanceSheet = BalanceSheetArr.get(journalDet.getACCOUNT_ID());
						if (balanceSheet != null) {
							Double debit = balanceSheet.getNet_debit() + journalDet.getDEBITS();
							Double credit = balanceSheet.getNet_credit() + journalDet.getCREDITS();
							balanceSheet.setNet_debit(debit);
							balanceSheet.setNet_credit(credit);
							BalanceSheetArr.replace(journalDet.getACCOUNT_ID(), balanceSheet);
						}

					}
				}

				BalanceSheetArr.forEach((k, v) -> {
					// jarray.add(v);
					// Double total=v.getNet_debit()-v.getNet_credit();
					Double total = v.getNet_credit() - v.getNet_debit();
					v.setTotal(total);
					BalanceSheetArr.replace(k, v);
				});

				BalanceSheetArr.forEach((k, v) -> {
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
		} else if (action.equalsIgnoreCase("getBalanceSheetEquity")) {
			String fromDate = StrUtils.fString(req.getParameter("fromDate")).trim();
			String toDate = StrUtils.fString(req.getParameter("toDate")).trim();
			String accounttypes = StrUtils.fString(req.getParameter("accounttypes")).trim();
			List<Journal> journalList = null;
			JSONArray jarray = new JSONArray();
			Map<Integer, BalanceSheet> BalanceSheetArr = new Hashtable<Integer, BalanceSheet>();
			try {
				// journalList=journalService.getAllJournalHeader(plant, user, type);
				// journalList=journalService.getJournalsByDateWithoutAttach(plant, fromDate,
				// toDate);
				Set<Integer> AccountId = new HashSet<Integer>();

				// String accounttypes= "('8','9')";
				List<JournalDetail> journalDetails = journalService.getJournalDetailsByAccountTypeAndDATE(plant,
						fromDate, toDate, accounttypes);

				for (JournalDetail journalDet : journalDetails) {
					if (!AccountId.contains(journalDet.getACCOUNT_ID())) {
						AccountId.add(journalDet.getACCOUNT_ID());
						CoaDAO coaDAO = new CoaDAO();

						JSONObject coaFullDetail = coaDAO.CoaFullRecord(plant,
								String.valueOf(journalDet.getACCOUNT_ID()));
						JSONObject coaJson = (JSONObject) coaFullDetail.get("results");
						// jarray.add(coaFullDetail.get("results"));
						System.out.println("CoaJson" + coaJson);
						if (coaJson != null) {
							BalanceSheet balanceSheet = new BalanceSheet();
							balanceSheet.setAccount_id(journalDet.getACCOUNT_ID());
							String accountcode = coaDAO.GetAccountCodeByName(coaJson.getString("account_name"), plant);
							
							String subaccountname = "";
							if(coaJson.getString("issub_account").equalsIgnoreCase("1")) {
								subaccountname = coaDAO.GetAccountNameById(coaJson.getString("sub_account"), plant);
							}else {
								subaccountname = coaJson.getString("account_name");
							}
							String subaccountcode = coaDAO.GetAccountCodeByName(subaccountname, plant);
							balanceSheet.setSub_account(subaccountcode+" "+subaccountname);
							
							balanceSheet.setAccount_name(accountcode+" "+coaJson.getString("account_name"));
							balanceSheet.setAccount_type(coaJson.getString("account_type_name"));
							balanceSheet.setExtended_type(coaJson.getString("account_det_type"));
							balanceSheet.setMain_account(coaJson.getString("mainaccname"));
							balanceSheet.setNet_credit(journalDet.getCREDITS());
							balanceSheet.setNet_debit(journalDet.getDEBITS());
							BalanceSheetArr.put(journalDet.getACCOUNT_ID(), balanceSheet);
						}

						// trialBalArr.put(coaJson.getString("ACCOUNT_NAME"), journalDet.get)
					} else {
						BalanceSheet balanceSheet = BalanceSheetArr.get(journalDet.getACCOUNT_ID());
						if (balanceSheet != null) {
							Double debit = balanceSheet.getNet_debit() + journalDet.getDEBITS();
							Double credit = balanceSheet.getNet_credit() + journalDet.getCREDITS();
							balanceSheet.setNet_debit(debit);
							balanceSheet.setNet_credit(credit);
							BalanceSheetArr.replace(journalDet.getACCOUNT_ID(), balanceSheet);
						}

					}
				}

				BalanceSheetArr.forEach((k, v) -> {
					// jarray.add(v);
					// Double total=v.getNet_debit()-v.getNet_credit();
					Double total = v.getNet_credit() - v.getNet_debit();
					v.setTotal(total);
					BalanceSheetArr.replace(k, v);
				});

				BalanceSheetArr.forEach((k, v) -> {
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
		} else if (action.equalsIgnoreCase("getBalanceSheetProfitLoss")) {
			JSONObject profitloss = new JSONObject();
			String fromDate = StrUtils.fString(req.getParameter("fromDate")).trim();
			String toDate = StrUtils.fString(req.getParameter("toDate")).trim();
			Double current_earnings = 0.00;
			try {
				current_earnings = reportService.NetProfit(plant, fromDate, toDate);
				profitloss.put("currentearnings", current_earnings);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			resp.setContentType("application/json");
			resp.setCharacterEncoding("UTF-8");
			resp.getWriter().write(profitloss.toString());
			resp.getWriter().flush();
			resp.getWriter().close();
		} else if (action.equalsIgnoreCase("getBalanceSheetDetails")) {
			JSONArray jarray = new JSONArray();
			String account = StrUtils.fString(req.getParameter("account")).trim();
			String fromDate = StrUtils.fString(req.getParameter("fromDate")).trim();
			String toDate = StrUtils.fString(req.getParameter("toDate")).trim();
			List<TrialBalanceDetails> trialBalanceDetails = new ArrayList<TrialBalanceDetails>();
			try {
				trialBalanceDetails = journalService.getTrialBalanceDetailsByAccount(plant, account, fromDate, toDate);

				jarray.addAll(trialBalanceDetails);

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
		}
	}

	public HSSFWorkbook populateExcel(String headerTitle, String excelHeaderDate, List<JSONArray> excelDatas,
			String noOfDecimal, JSONObject profitLoss) throws JSONException

	{

		String assetsString = excelDatas.get(0).toString();
		String liabilitiesString = excelDatas.get(1).toString();
		String reservesSurPlus = excelDatas.get(2).toString();
		
		DecimalFormat  n = (DecimalFormat)NumberFormat.getCurrencyInstance(Locale.US); 
		DecimalFormatSymbols symbols = n.getDecimalFormatSymbols();
		symbols.setCurrencySymbol(""); // Don't use null.
		n.setDecimalFormatSymbols(symbols);
		n.setNegativePrefix("-");
		n.setNegativeSuffix("");
		
		// Create blank workbook
		HSSFWorkbook workbook = new HSSFWorkbook();
		// Create a blank sheet
		HSSFSheet spreadsheet = workbook.createSheet("BalanceSheet");
		// Create row object

		HSSFCellStyle my_style = workbook.createCellStyle();
		/* Create HSSFFont object from the workbook */
		HSSFFont my_font = workbook.createFont();
		/* set the weight of the font */
		my_font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		/* attach the font to the style created earlier */
		my_style.setFont(my_font);
		
		HSSFCellStyle rightAligned = workbook.createCellStyle();
		rightAligned.setAlignment(HSSFCellStyle.ALIGN_RIGHT);

		HSSFRow row = spreadsheet.createRow(0);
		HSSFCell cell = row.createCell(0);
		cell.setCellValue(headerTitle);
		cell.setCellStyle(my_style);

		HSSFRow row1 = spreadsheet.createRow(1);
		HSSFCell cell1 = row1.createCell(0);
		cell1.setCellValue("As of " + excelHeaderDate);
		cell1.setCellStyle(my_style);

		HSSFRow row2 = spreadsheet.createRow(3);
		HSSFCell cellAccountHeader = row2.createCell(0);
		cellAccountHeader.setCellValue("Account");
		cellAccountHeader.setCellStyle(my_style);
		HSSFCell netTotalheader = row2.createCell(1);
		netTotalheader.setCellValue("Total");
		netTotalheader.setCellStyle(my_style);
		netTotalheader.setCellStyle(rightAligned);

		HSSFRow row3 = spreadsheet.createRow(4);
		HSSFCell cellAccountHeader1 = row3.createCell(0);
		cellAccountHeader1.setCellValue("Fixed assets");
		cellAccountHeader1.setCellStyle(my_style);

		int dataRow = 5;
		org.json.JSONArray array = new org.json.JSONArray(assetsString);
		
		/*List<String> subacclist = new ArrayList<>();
		JSONArray assetsList = new JSONArray();
	        if (array.length() > 0) {
	            for (int r = 0; r < array.length(); r++) {
	                org.json.JSONObject jsonObject = array.getJSONObject(r);
	                String subacc = jsonObject.getString("sub_account");
	                if (!subacclist.contains(subacc)) {
	                	subacclist.add(subacc.toString());
	                }
	            }
	    		for (int s = 0; s < subacclist.size(); s++) {
	    			 org.json.JSONObject newNode = new  org.json.JSONObject();
	    			newNode.put("subaccount",subacclist.get(s));
	    			org.json.JSONArray arrayElementOneArray = new org.json.JSONArray();
	    			
	    			for (int r = 0; r < array.length(); r++) {
		                org.json.JSONObject jsonObject = array.getJSONObject(r);
		                String subacc = jsonObject.getString("sub_account");
		                if (subacclist.get(s).equalsIgnoreCase(subacc)) {
		                	arrayElementOneArray.put(jsonObject);
		                }
		            }
	    			newNode.put("subdata", arrayElementOneArray);
	    			assetsList.add(newNode);
	    		}

	        }
		
	        System.out.println(assetsList.toString());*/
		
		org.json.JSONArray array1 = new org.json.JSONArray(liabilitiesString);
		
		org.json.JSONArray array2 = new org.json.JSONArray(reservesSurPlus);

		for (int i = 0; i < array.length(); i++) {
			org.json.JSONObject object = array.getJSONObject(i);
			if (object.getString("account_type").contains("Fixed assets")) {
				HSSFRow row4 = spreadsheet.createRow(dataRow);
				HSSFCell row4Data1 = row4.createCell(0);
				row4Data1.setCellValue(new HSSFRichTextString(StrUtils.fString(object.getString("account_name"))));

				String total = StrUtils.fString(object.getString("total"));
				total = StrUtils.addZeroes(Double.parseDouble(total), noOfDecimal);
				total = n.format(Double.parseDouble(total));

				org.json.JSONArray chldarray = new org.json.JSONArray(object.getString("_children"));
				if(chldarray.length() > 0) {
					HSSFCell row4Data2 = row4.createCell(1);
					row4Data2.setCellValue("");
				}else {
					HSSFCell row4Data2 = row4.createCell(1);
					row4Data2.setCellValue(new HSSFRichTextString(total));
					row4Data2.setCellStyle(rightAligned);
				}
				
				dataRow++;
				
				if(chldarray.length() > 0) {
					
					for (int c = 0; c < chldarray.length(); c++) {
						org.json.JSONObject cobject = chldarray.getJSONObject(c);
						if (cobject.getString("account_type").contains("Fixed assets")) {
							HSSFRow crow4 = spreadsheet.createRow(dataRow);
							HSSFCell crow4Data1 = crow4.createCell(0);
							crow4Data1.setCellValue(new HSSFRichTextString("   "+StrUtils.fString(cobject.getString("account_name"))));

							String ctotal = StrUtils.fString(cobject.getString("total"));
							ctotal = StrUtils.addZeroes(Double.parseDouble(ctotal), noOfDecimal);
							ctotal = n.format(Double.parseDouble(ctotal));

							HSSFCell crow4Data2 = crow4.createCell(1);
							crow4Data2.setCellValue(new HSSFRichTextString(ctotal));
							crow4Data2.setCellStyle(rightAligned);

							dataRow++;
							
						}
					}
				}
				
				
				
			}

		}

		HSSFRow row4 = spreadsheet.createRow(dataRow++);
		HSSFCell cellCurrentAss = row4.createCell(0);
		cellCurrentAss.setCellValue("Current assets");
		cellCurrentAss.setCellStyle(my_style);

		dataRow = dataRow++;
			
		double totalAssets = 0.00;
	
		for (int i = 0; i < array.length(); i++) {
			org.json.JSONObject object = array.getJSONObject(i);
			
			double assetTotal = object.getDouble("total");
			
			totalAssets += assetTotal;

			if (object.getString("account_type").contains("Current assets")) {
				row4 = spreadsheet.createRow(dataRow);
				HSSFCell row4Data1 = row4.createCell(0);
				row4Data1.setCellValue(new HSSFRichTextString(StrUtils.fString(object.getString("account_name"))));

				String total = StrUtils.fString(object.getString("total"));
				total = StrUtils.addZeroes(Double.parseDouble(total), noOfDecimal);
				total = n.format(Double.parseDouble(total));

				org.json.JSONArray chldarray = new org.json.JSONArray(object.getString("_children"));
				if(chldarray.length() > 0) {
					HSSFCell row4Data2 = row4.createCell(1);
					row4Data2.setCellValue("");
				}else {
					HSSFCell row4Data2 = row4.createCell(1);
					row4Data2.setCellValue(new HSSFRichTextString(total));
					row4Data2.setCellStyle(rightAligned);
				}
				
				dataRow++;
				
				if(chldarray.length() > 0) {
					
					for (int c = 0; c < chldarray.length(); c++) {
						org.json.JSONObject cobject = chldarray.getJSONObject(c);
						if (cobject.getString("account_type").contains("Current assets")) {
							HSSFRow crow4 = spreadsheet.createRow(dataRow);
							HSSFCell crow4Data1 = crow4.createCell(0);
							crow4Data1.setCellValue(new HSSFRichTextString("   "+StrUtils.fString(cobject.getString("account_name"))));

							String ctotal = StrUtils.fString(cobject.getString("total"));
							ctotal = StrUtils.addZeroes(Double.parseDouble(ctotal), noOfDecimal);
							ctotal = n.format(Double.parseDouble(ctotal));

							HSSFCell crow4Data2 = crow4.createCell(1);
							crow4Data2.setCellValue(new HSSFRichTextString(ctotal));
							crow4Data2.setCellStyle(rightAligned);

							dataRow++;
							
						}
					}
				}
				
				
				
			}

		}
		
		
		HSSFRow rowTotal = spreadsheet.createRow(dataRow++);
		HSSFCell cellTotal = rowTotal.createCell(0);                             
		cellTotal.setCellValue("Total Assets:");
		cellTotal.setCellStyle(my_style);

		String totalAssetVal = StrUtils.fString(String.valueOf(totalAssets));
		totalAssetVal = StrUtils.addZeroes(Double.parseDouble(totalAssetVal), noOfDecimal);
		totalAssetVal = n.format(Double.parseDouble(totalAssetVal));

		HSSFCell cellTotal1 = rowTotal.createCell(1);
		cellTotal1.setCellValue(new HSSFRichTextString(totalAssetVal));
		cellTotal1.setCellStyle(my_style);
		cellTotal1.setCellStyle(rightAligned);

		HSSFRow rowLiabilities = spreadsheet.createRow(dataRow++);
		HSSFCell rowLiab = rowLiabilities.createCell(0);
		rowLiab.setCellValue("Current liabilities");
		rowLiab.setCellStyle(my_style);

		dataRow = dataRow++;
		
		totalAssets = 0.00;
		
		for (int i = 0; i < array1.length(); i++) 
		{
		    org.json.JSONObject object = array1.getJSONObject(i);
			
		    double assetTotal = object.getDouble("total");
			
			totalAssets += assetTotal;
			
			if (object.getString("account_type").contains("Current liabilities")) {
				row4 = spreadsheet.createRow(dataRow);
				HSSFCell row4Data1 = row4.createCell(0);
				row4Data1.setCellValue(new HSSFRichTextString(StrUtils.fString(object.getString("account_name"))));

				String total = StrUtils.fString(object.getString("total"));
				total = StrUtils.addZeroes(Double.parseDouble(total), noOfDecimal);
				total = n.format(Double.parseDouble(total));

				org.json.JSONArray chldarray = new org.json.JSONArray(object.getString("_children"));
				if(chldarray.length() > 0) {
					HSSFCell row4Data2 = row4.createCell(1);
					row4Data2.setCellValue("");
				}else {
					HSSFCell row4Data2 = row4.createCell(1);
					row4Data2.setCellValue(new HSSFRichTextString(total));
					row4Data2.setCellStyle(rightAligned);
				}
				
				dataRow++;
				
				if(chldarray.length() > 0) {
					
					for (int c = 0; c < chldarray.length(); c++) {
						org.json.JSONObject cobject = chldarray.getJSONObject(c);
						if (cobject.getString("account_type").contains("Current liabilities")) {
							HSSFRow crow4 = spreadsheet.createRow(dataRow);
							HSSFCell crow4Data1 = crow4.createCell(0);
							crow4Data1.setCellValue(new HSSFRichTextString("   "+StrUtils.fString(cobject.getString("account_name"))));

							String ctotal = StrUtils.fString(cobject.getString("total"));
							ctotal = StrUtils.addZeroes(Double.parseDouble(ctotal), noOfDecimal);
							ctotal = n.format(Double.parseDouble(ctotal));

							HSSFCell crow4Data2 = crow4.createCell(1);
							crow4Data2.setCellValue(new HSSFRichTextString(ctotal));
							crow4Data2.setCellStyle(rightAligned);

							dataRow++;
							
						}
					}
				}
				
				
				
			}

		}
		

		HSSFRow rowLiabilities1 = spreadsheet.createRow(dataRow++);
		HSSFCell rowLiab1 = rowLiabilities1.createCell(0);
		rowLiab1.setCellValue("Non-Current liabilities");
		rowLiab1.setCellStyle(my_style);

		dataRow = dataRow++;

		for (int i = 0; i < array1.length(); i++) 
		{
			org.json.JSONObject object = array1.getJSONObject(i);
			if (object.getString("account_type").contains("Non-current liabilities")) {
				row4 = spreadsheet.createRow(dataRow);
				HSSFCell row4Data1 = row4.createCell(0);
				row4Data1.setCellValue(new HSSFRichTextString(StrUtils.fString(object.getString("account_name"))));

				String total = StrUtils.fString(object.getString("total"));
				total = StrUtils.addZeroes(Double.parseDouble(total), noOfDecimal);
				total = n.format(Double.parseDouble(total));

				org.json.JSONArray chldarray = new org.json.JSONArray(object.getString("_children"));
				if(chldarray.length() > 0) {
					HSSFCell row4Data2 = row4.createCell(1);
					row4Data2.setCellValue("");
				}else {
					HSSFCell row4Data2 = row4.createCell(1);
					row4Data2.setCellValue(new HSSFRichTextString(total));
					row4Data2.setCellStyle(rightAligned);
				}
				
				dataRow++;
				
				if(chldarray.length() > 0) {
					
					for (int c = 0; c < chldarray.length(); c++) {
						org.json.JSONObject cobject = chldarray.getJSONObject(c);
						if (cobject.getString("account_type").contains("Non-current liabilities")) {
							HSSFRow crow4 = spreadsheet.createRow(dataRow);
							HSSFCell crow4Data1 = crow4.createCell(0);
							crow4Data1.setCellValue(new HSSFRichTextString("   "+StrUtils.fString(cobject.getString("account_name"))));

							String ctotal = StrUtils.fString(cobject.getString("total"));
							ctotal = StrUtils.addZeroes(Double.parseDouble(ctotal), noOfDecimal);
							ctotal = n.format(Double.parseDouble(ctotal));

							HSSFCell crow4Data2 = crow4.createCell(1);
							crow4Data2.setCellValue(new HSSFRichTextString(ctotal));
							crow4Data2.setCellStyle(rightAligned);

							dataRow++;
							
						}
					}
				}
				
				
				
			}

		}
		
		
		rowTotal = spreadsheet.createRow(dataRow++);
		cellTotal = rowTotal.createCell(0);                             
		cellTotal.setCellValue("Total Liabilities:");
		cellTotal.setCellStyle(my_style);

		totalAssetVal = StrUtils.fString(String.valueOf(totalAssets));
		String tAssetVal = totalAssetVal;
		totalAssetVal = StrUtils.addZeroes(Double.parseDouble(totalAssetVal), noOfDecimal);
		totalAssetVal = n.format(Double.parseDouble(totalAssetVal));

		cellTotal1 = rowTotal.createCell(1);
		cellTotal1.setCellValue(new HSSFRichTextString(totalAssetVal));
		cellTotal1.setCellStyle(my_style);
		cellTotal1.setCellStyle(rightAligned);
		
		HSSFRow rowLiabilities123 = spreadsheet.createRow(dataRow++);
		HSSFCell rowStatuory = rowLiabilities123.createCell(0);
		rowStatuory.setCellValue("Equity");
		rowStatuory.setCellStyle(my_style);

		dataRow = dataRow++;
		
		
		String reserPlus  = "0";
		
		for (int i = 0; i < array2.length(); i++) 
		{
		    org.json.JSONObject object = array2.getJSONObject(i);		
			if (object.getString("account_type").contains("Equity")) 
			{
				row4 = spreadsheet.createRow(dataRow);
				HSSFCell row4Data1 = row4.createCell(0);
				row4Data1.setCellValue(new HSSFRichTextString(StrUtils.fString(object.getString("account_name"))));

				String total = StrUtils.fString(object.getString("total"));
				total = StrUtils.addZeroes(Double.parseDouble(total), noOfDecimal);
				total = n.format(Double.parseDouble(total));

				org.json.JSONArray chldarray = new org.json.JSONArray(object.getString("_children"));
				if(chldarray.length() > 0) {
					HSSFCell row4Data2 = row4.createCell(1);
					row4Data2.setCellValue("");
				}else {
					HSSFCell row4Data2 = row4.createCell(1);
					row4Data2.setCellValue(new HSSFRichTextString(total));
					row4Data2.setCellStyle(rightAligned);
				}
				
				dataRow++;
				
				if(chldarray.length() > 0) {
					
					for (int c = 0; c < chldarray.length(); c++) {
						org.json.JSONObject cobject = chldarray.getJSONObject(c);
						if (cobject.getString("account_type").contains("Equity")) {
							HSSFRow crow4 = spreadsheet.createRow(dataRow);
							HSSFCell crow4Data1 = crow4.createCell(0);
							crow4Data1.setCellValue(new HSSFRichTextString("   "+StrUtils.fString(cobject.getString("account_name"))));

							String ctotal = StrUtils.fString(cobject.getString("total"));
							ctotal = StrUtils.addZeroes(Double.parseDouble(ctotal), noOfDecimal);
							ctotal = n.format(Double.parseDouble(ctotal));

							HSSFCell crow4Data2 = crow4.createCell(1);
							crow4Data2.setCellValue(new HSSFRichTextString(ctotal));
							crow4Data2.setCellStyle(rightAligned);

							dataRow++;
							
						}
					}
				}
				
				
				
			}

		}
		

		String cequity  = "0";
		
		for (int i = 0; i < array2.length(); i++) {
			org.json.JSONObject object = array2.getJSONObject(i);
			String total = StrUtils.fString(object.getString("total"));
			total = StrUtils.addZeroes(Double.parseDouble(total), noOfDecimal);
			cequity = total;

		}
		
		HSSFRow rowEarnings = spreadsheet.createRow(dataRow++);
		HSSFCell cellEarnings = rowEarnings.createCell(0);
		cellEarnings.setCellValue("Current earnings");
		cellEarnings.setCellStyle(my_style);

		String currEarnings = StrUtils.fString(profitLoss.getString("currentearnings"));
		currEarnings = StrUtils.addZeroes(Double.parseDouble(currEarnings), noOfDecimal);
		String cEarnings = currEarnings;
		currEarnings = n.format(Double.parseDouble(currEarnings));

		HSSFCell cellEarnings1 = rowEarnings.createCell(1);
		cellEarnings1.setCellValue(new HSSFRichTextString(currEarnings));
		cellEarnings1.setCellStyle(my_style);
		cellEarnings1.setCellStyle(rightAligned);
		
		HSSFRow rowTotalEquity = spreadsheet.createRow(dataRow++);
		HSSFCell cellTotalEquity = rowTotalEquity.createCell(0);
		cellTotalEquity.setCellValue("Total Equity");
		cellTotalEquity.setCellStyle(my_style);
		
		Double totalEquity = Double.parseDouble(cEarnings) + Double.parseDouble(cequity);

		String totalEuityVal = StrUtils.fString(String.valueOf(totalEquity));
		totalEuityVal = StrUtils.addZeroes(Double.parseDouble(totalEuityVal), noOfDecimal);
		String tEuityVal = totalEuityVal;
		totalEuityVal = n.format(Double.parseDouble(totalEuityVal));

		HSSFCell cellTotalEquity1 = rowTotalEquity.createCell(1);
		cellTotalEquity1.setCellValue(new HSSFRichTextString(totalEuityVal));
		cellTotalEquity1.setCellStyle(my_style);
		cellTotalEquity1.setCellStyle(rightAligned);
		
		
		HSSFRow rowTotalValue = spreadsheet.createRow(dataRow++);
		HSSFCell cellTotalValue = rowTotalValue.createCell(0);
		cellTotalValue.setCellValue("Total");
		cellTotalValue.setCellStyle(my_style);
		
		double totalvalue = Double.parseDouble(tAssetVal) + Double.parseDouble(tEuityVal);

		String totalVal = StrUtils.fString(String.valueOf(totalvalue));
		totalVal = StrUtils.addZeroes(Double.parseDouble(totalVal), noOfDecimal);
		totalVal = n.format(Double.parseDouble(totalVal));

		HSSFCell cellTotalvalue1 = rowTotalValue.createCell(1);
		cellTotalvalue1.setCellValue(new HSSFRichTextString(totalVal));
		cellTotalvalue1.setCellStyle(my_style);
		cellTotalvalue1.setCellStyle(rightAligned);
		
		
		spreadsheet.autoSizeColumn(0);
		spreadsheet.autoSizeColumn(1);
		spreadsheet.autoSizeColumn(2);

		return workbook;
	}

	public HashMap<String, String> populateMapData(String companyCode, String userCode) {
		// TODO Auto-generated method stub
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

}
