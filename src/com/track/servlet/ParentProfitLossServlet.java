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
import com.track.dao.ParentChildCmpDetDAO;
import com.track.dao.PlantMstDAO;
import com.track.db.object.Journal;
import com.track.db.object.JournalDetail;
import com.track.db.object.LedgerDetails;
import com.track.db.object.ParentChildCmpDet;
import com.track.db.object.ProfitLoss;
import com.track.service.JournalService;
import com.track.service.LedgerService;
import com.track.serviceImplementation.JournalEntry;
import com.track.serviceImplementation.LedgerServiceImpl;
import com.track.util.IMLogger;
import com.track.util.MLogger;
import com.track.util.StrUtils;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@WebServlet("/ParentProfitLossServlet")
public class ParentProfitLossServlet extends HttpServlet implements IMLogger {

	private static final long serialVersionUID = 1L;
	private boolean printLog = MLoggerConstant.ProfitLossServlet_PRINTPLANTMASTERLOG;
	private boolean printInfo = MLoggerConstant.ProfitLossServlet_PRINTPLANTMASTERINFO;
	String action = "";
	private JournalService journalService = new JournalEntry();
	private LedgerService ledgerSerice = new LedgerServiceImpl();

	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		action = StrUtils.fString(req.getParameter("action")).trim();
		//String plant = StrUtils.fString((String) req.getSession().getAttribute("PLANT")).trim();
		String user = StrUtils.fString((String) req.getSession().getAttribute("LOGIN_USER")).trim();
		String type = "ALL";

		if (action.equalsIgnoreCase("getProfitLoss")) {
			String fromDate = StrUtils.fString(req.getParameter("fromDate")).trim();
			String toDate = StrUtils.fString(req.getParameter("toDate")).trim();
			String accounttypes = StrUtils.fString(req.getParameter("accounttypes")).trim();
			String plant = StrUtils.fString(req.getParameter("plant")).trim();
			List<Journal> journalList = null;
			JSONArray jarray = new JSONArray();
			Map<Integer, ProfitLoss> profitLossArr = new Hashtable<Integer, ProfitLoss>();
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
							ProfitLoss profitLoss = new ProfitLoss();
							profitLoss.setAccount_id(journalDet.getACCOUNT_ID());
							profitLoss.setAccount_name(coaJson.getString("account_name"));
							profitLoss.setAccount_type(coaJson.getString("account_type_name"));
							profitLoss.setExtended_type(coaJson.getString("account_det_type"));
							profitLoss.setMain_account(coaJson.getString("mainaccname"));
							profitLoss.setNet_credit(journalDet.getCREDITS());
							profitLoss.setNet_debit(journalDet.getDEBITS());
							profitLossArr.put(journalDet.getACCOUNT_ID(), profitLoss);
						}

						// trialBalArr.put(coaJson.getString("ACCOUNT_NAME"), journalDet.get)
					} else {
						ProfitLoss profitLoss = profitLossArr.get(journalDet.getACCOUNT_ID());
						if (profitLoss != null) {
							Double debit = profitLoss.getNet_debit() + journalDet.getDEBITS();
							Double credit = profitLoss.getNet_credit() + journalDet.getCREDITS();
							profitLoss.setNet_debit(debit);
							profitLoss.setNet_credit(credit);
							profitLossArr.replace(journalDet.getACCOUNT_ID(), profitLoss);
						}

					}
				}

				profitLossArr.forEach((k, v) -> {
					String accounttype = v.getAccount_type().trim();
					Double total = 0.00;
					if (accounttype.equalsIgnoreCase("Sales") || accounttype.equalsIgnoreCase("Income")) {
						total = v.getNet_credit() - v.getNet_debit();
					} else {
						total = v.getNet_debit() - v.getNet_credit();
					}
					v.setTotal(total);
					profitLossArr.replace(k, v);
				});

				profitLossArr.forEach((k, v) -> {
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
		} else if (action.equalsIgnoreCase("getProfitLossDetails")) {
			JSONArray jarray = new JSONArray();
			JSONObject jObject = new JSONObject();
			String account = StrUtils.fString(req.getParameter("account")).trim();
			String fromDate = StrUtils.fString(req.getParameter("fromDate")).trim();
			String toDate = StrUtils.fString(req.getParameter("toDate")).trim();
			String plant = StrUtils.fString(req.getParameter("plant")).trim();
			List<LedgerDetails> ledgerDetails = new ArrayList<LedgerDetails>();
			try {
				ledgerDetails = ledgerSerice.getLedgerDetailsByAccount(plant, account, fromDate, toDate);
				LocalDate yearStartDay = LocalDate.parse(fromDate).withDayOfYear(1);
				System.out.println("Year Start Day:" + yearStartDay.toString());
				Double openingBalance = ledgerSerice.openingBalance(plant, account, yearStartDay.toString(), fromDate);
				jarray.addAll(ledgerDetails);
				jObject.put("openingbalance", openingBalance);
				jObject.put("transactions", jarray);
				// jarray.add(trialBalArr.values().toArray());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			resp.setContentType("application/json");
			resp.setCharacterEncoding("UTF-8");
			resp.getWriter().write(jObject.toString());
			resp.getWriter().flush();
			resp.getWriter().close();
		}

	}

	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		action = StrUtils.fString(req.getParameter("action")).trim();
		//String plant = StrUtils.fString((String) req.getSession().getAttribute("PLANT")).trim();
		String user = StrUtils.fString((String) req.getSession().getAttribute("LOGIN_USER")).trim();
		String type = "ALL";

		if (action.equalsIgnoreCase("getProfitLoss")) {
			String fromDate = StrUtils.fString(req.getParameter("fromDate")).trim();
			String toDate = StrUtils.fString(req.getParameter("toDate")).trim();
			String accounttypes = StrUtils.fString(req.getParameter("accounttypes")).trim();
			String plant = StrUtils.fString(req.getParameter("plant")).trim();
			List<Journal> journalList = null;
			JSONArray jarray = new JSONArray();
			Map<Integer, ProfitLoss> profitLossArr = new Hashtable<Integer, ProfitLoss>();
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
							ProfitLoss profitLoss = new ProfitLoss();
							profitLoss.setAccount_id(journalDet.getACCOUNT_ID());
							profitLoss.setAccount_name(coaJson.getString("account_name"));
							profitLoss.setAccount_type(coaJson.getString("account_type_name"));
							profitLoss.setExtended_type(coaJson.getString("account_det_type"));
							profitLoss.setMain_account(coaJson.getString("mainaccname"));
							profitLoss.setNet_credit(journalDet.getCREDITS());
							profitLoss.setNet_debit(journalDet.getDEBITS());
							profitLossArr.put(journalDet.getACCOUNT_ID(), profitLoss);
						}

						// trialBalArr.put(coaJson.getString("ACCOUNT_NAME"), journalDet.get)
					} else {
						ProfitLoss profitLoss = profitLossArr.get(journalDet.getACCOUNT_ID());
						if (profitLoss != null) {
							Double debit = profitLoss.getNet_debit() + journalDet.getDEBITS();
							Double credit = profitLoss.getNet_credit() + journalDet.getCREDITS();
							profitLoss.setNet_debit(debit);
							profitLoss.setNet_credit(credit);
							profitLossArr.replace(journalDet.getACCOUNT_ID(), profitLoss);
						}

					}
				}

				profitLossArr.forEach((k, v) -> {
					String accounttype = v.getAccount_type().trim();
					Double total = 0.00;
					if (accounttype.equalsIgnoreCase("Sales") || accounttype.equalsIgnoreCase("Income")) {
						total = v.getNet_credit() - v.getNet_debit();
					} else {
						total = v.getNet_debit() - v.getNet_credit();
					}
					v.setTotal(total);
					profitLossArr.replace(k, v);
				});

				profitLossArr.forEach((k, v) -> {
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
		} else if (action.equalsIgnoreCase("getProfitLossDetails")) {
			JSONArray jarray = new JSONArray();
			JSONObject jObject = new JSONObject();
			String account = StrUtils.fString(req.getParameter("account")).trim();
			String fromDate = StrUtils.fString(req.getParameter("fromDate")).trim();
			String toDate = StrUtils.fString(req.getParameter("toDate")).trim();
			String plant = StrUtils.fString(req.getParameter("plant")).trim();
			List<LedgerDetails> ledgerDetails = new ArrayList<LedgerDetails>();
			try {
				ledgerDetails = ledgerSerice.getLedgerDetailsByAccount(plant, account, fromDate, toDate);
				LocalDate yearStartDay = LocalDate.parse(fromDate).withDayOfYear(1);
				System.out.println("Year Start Day:" + yearStartDay.toString());
				Double openingBalance = ledgerSerice.openingBalance(plant, account, yearStartDay.toString(), fromDate);
				jarray.addAll(ledgerDetails);
				jObject.put("openingbalance", openingBalance);
				jObject.put("transactions", jarray);
				// jarray.add(trialBalArr.values().toArray());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			resp.setContentType("application/json");
			resp.setCharacterEncoding("UTF-8");
			resp.getWriter().write(jObject.toString());
			resp.getWriter().flush();
			resp.getWriter().close();
		}

		if (action.equalsIgnoreCase("getProfitLossExcel")) {

			String fromDate = StrUtils.fString(req.getParameter("fromDate")).trim();
			String toDate = StrUtils.fString(req.getParameter("toDate")).trim();
			String plant = StrUtils.fString((String) req.getSession().getAttribute("PLANT")).trim();
			String plist = StrUtils.fString(req.getParameter("plist")).trim();
			
			if (!fromDate.isEmpty()) {
				HSSFWorkbook workbook = null;
				try {
					workbook = populateExcel("Profit&Loss",fromDate,toDate,plist,plant);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
				workbook.write(outByteStream);
				byte[] outArray = outByteStream.toByteArray();
				resp.setContentType("application/ms-excel");
				resp.setContentLength(outArray.length);
				resp.setHeader("Expires:", "0"); // eliminates browser caching
				resp.setHeader("Content-Disposition", "attachment; filename=Profit&Loss.xls");
				OutputStream outStream = resp.getOutputStream();
				outStream.write(outArray);
				outStream.flush();
				outStream.close();
			}
		}

	}

	public HSSFWorkbook populateExcel(String headerTitle, String fromDate, String toDate, String plantclist, String plant1) throws Exception {

		

		// Create blank workbook
		HSSFWorkbook workbook = new HSSFWorkbook();
		
		String[] arrOfStr = plantclist.split(",");
		ParentChildCmpDetDAO pcdao = new ParentChildCmpDetDAO();
		PlantMstDAO plantMstDAO = new PlantMstDAO();
		List<ParentChildCmpDet> plantlist = pcdao.getAllParentChildCmpDetdropdown(plant1, "");
		for (ParentChildCmpDet plist : plantlist) {
			String plant = plist.getCHILD_PLANT();
			boolean archeck = true;
			for (String ars : arrOfStr) {
				if(ars.equalsIgnoreCase(plant)) {
					archeck = false;
				}
			}
			if(archeck) {
				continue;
			}
			String name = plantMstDAO.getcmpyname(plist.getCHILD_PLANT());
			String noOfDecimal = plantMstDAO.getNumberOfDecimal(plant);
			

			List<String> list = Arrays.asList("('10','8')", "('9','11')");
			List<JSONArray> excelDataArray = new ArrayList<>();

			for (String account : list) {

				JSONArray jarray = new JSONArray();
				Map<Integer, ProfitLoss> profitLossArr = new Hashtable<Integer, ProfitLoss>();
				try {
					// journalList=journalService.getAllJournalHeader(plant, user, type);
					// journalList=journalService.getJournalsByDateWithoutAttach(plant, fromDate,
					// toDate);
					Set<Integer> AccountId = new HashSet<Integer>();

					// String accounttypes= "('8','9')";
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
								ProfitLoss profitLoss = new ProfitLoss();
								profitLoss.setAccount_id(journalDet.getACCOUNT_ID());
								profitLoss.setAccount_name(coaJson.getString("account_name"));
								profitLoss.setAccount_type(coaJson.getString("account_type_name"));
								profitLoss.setExtended_type(coaJson.getString("account_det_type"));
								profitLoss.setMain_account(coaJson.getString("mainaccname"));
								profitLoss.setNet_credit(journalDet.getCREDITS());
								profitLoss.setNet_debit(journalDet.getDEBITS());
								profitLossArr.put(journalDet.getACCOUNT_ID(), profitLoss);
							}

							// trialBalArr.put(coaJson.getString("ACCOUNT_NAME"), journalDet.get)
						} else {
							ProfitLoss profitLoss = profitLossArr.get(journalDet.getACCOUNT_ID());
							if (profitLoss != null) {
								Double debit = profitLoss.getNet_debit() + journalDet.getDEBITS();
								Double credit = profitLoss.getNet_credit() + journalDet.getCREDITS();
								profitLoss.setNet_debit(debit);
								profitLoss.setNet_credit(credit);
								profitLossArr.replace(journalDet.getACCOUNT_ID(), profitLoss);
							}

						}
					}

					profitLossArr.forEach((k, v) -> {
						String accounttype = v.getAccount_type().trim();
						Double total = 0.00;
						if (accounttype.equalsIgnoreCase("Sales") || accounttype.equalsIgnoreCase("Income")) {
							total = v.getNet_credit() - v.getNet_debit();
						} else {
							total = v.getNet_debit() - v.getNet_credit();
						}
						v.setTotal(total);
						profitLossArr.replace(k, v);
					});

					profitLossArr.forEach((k, v) -> {
						jarray.add(v);
					});
					// jarray.add(trialBalArr.values().toArray());
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				excelDataArray.add(jarray);
			}
			
			
			String salesString = excelDataArray.get(0).toString();
			String incomeString = excelDataArray.get(1).toString();
		// Create a blank sheet
		HSSFSheet spreadsheet = workbook.createSheet(name);
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
		cell1.setCellValue("As of " + toDate);
		cell1.setCellStyle(my_style);

		HSSFRow row2 = spreadsheet.createRow(3);
		HSSFCell cellAccountHeader = row2.createCell(0);
		cellAccountHeader.setCellValue("Account");
		cellAccountHeader.setCellStyle(my_style);
		HSSFCell netTotalheader = row2.createCell(1);
		netTotalheader.setCellValue("Total");
		netTotalheader.setCellStyle(my_style);

		HSSFRow row3 = spreadsheet.createRow(4);
		HSSFCell cellAccountHeader1 = row3.createCell(0);
		cellAccountHeader1.setCellValue("Sales");
		cellAccountHeader1.setCellStyle(my_style);

		int dataRow = 5;
		org.json.JSONArray array = new org.json.JSONArray(salesString);

		org.json.JSONArray array1 = new org.json.JSONArray(incomeString);

		double totalSales = 0.00;

		double totalCostOfSales = 0.00;
		for (int i = 0; i < array.length(); i++) {
			org.json.JSONObject object = array.getJSONObject(i);

			
			if (object.getString("account_type").contains("Sales")) {
				
				double assetTotal = object.getDouble("total");

				totalSales += assetTotal;

				
				
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
		HSSFCell cellCurrentAss = row4.createCell(0);
		cellCurrentAss.setCellValue("Cost of Sales");
		cellCurrentAss.setCellStyle(my_style);

		dataRow = dataRow++;

		for (int i = 0; i < array.length(); i++) {
			org.json.JSONObject object = array.getJSONObject(i);

			

			if (object.getString("account_type").contains("Cost of sales")) {
				
				
				double assetTotal = object.getDouble("total");

				totalCostOfSales += assetTotal;
				
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

		HSSFRow rowTotal = spreadsheet.createRow(dataRow++);
		HSSFCell cellTotal = rowTotal.createCell(0);
		cellTotal.setCellValue("Gross Profit:");
		cellTotal.setCellStyle(my_style);

		double totalGrossProfit = totalSales - totalCostOfSales;

		String totalAssetVal = StrUtils.fString(String.valueOf(totalGrossProfit));
		totalAssetVal = StrUtils.addZeroes(Double.parseDouble(totalAssetVal), noOfDecimal);

		HSSFCell cellTotal1 = rowTotal.createCell(1);
		cellTotal1.setCellValue(new HSSFRichTextString(totalAssetVal));
		cellTotal1.setCellStyle(my_style);

		HSSFRow rowLiabilities = spreadsheet.createRow(dataRow++);
		HSSFCell rowLiab = rowLiabilities.createCell(0);
		rowLiab.setCellValue("Income");
		rowLiab.setCellStyle(my_style);

		dataRow = dataRow++;
		
		double totalIncome = 0.00;
		
		for (int i = 0; i < array1.length(); i++) {
			org.json.JSONObject object = array1.getJSONObject(i);

			

			// totalAssets += assetTotal;

			if (object.getString("account_type").contains("Income")) {
				
				double assetTotal = object.getDouble("total");
				
				totalIncome += assetTotal;
				
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

		HSSFRow rowLiabilities1 = spreadsheet.createRow(dataRow++);
		HSSFCell rowLiab1 = rowLiabilities1.createCell(0);
		rowLiab1.setCellValue("Expenses");
		rowLiab1.setCellStyle(my_style);

		dataRow = dataRow++;

		
		double totalExpense = 0.00;
		
		for (int i = 0; i < array1.length(); i++) {
			org.json.JSONObject object = array1.getJSONObject(i);
			
			
			if (object.getString("account_type").contains("Expenses")) {
				
			   double assetTotal = object.getDouble("total");
				
				totalExpense += assetTotal;
				
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

		rowTotal = spreadsheet.createRow(dataRow++);
		cellTotal = rowTotal.createCell(0);
		cellTotal.setCellValue("Net Profit:");
		cellTotal.setCellStyle(my_style);

		double totalnetProfit = totalIncome - totalExpense;
		double finalNetProfit = totalGrossProfit + totalnetProfit;
				
		totalAssetVal = StrUtils.fString(String.valueOf(finalNetProfit));
		totalAssetVal = StrUtils.addZeroes(Double.parseDouble(totalAssetVal), noOfDecimal);

		cellTotal1 = rowTotal.createCell(1);
		cellTotal1.setCellValue(new HSSFRichTextString(totalAssetVal));
		cellTotal1.setCellStyle(my_style);

//		HSSFRow rowLiabilities123 = spreadsheet.createRow(dataRow++);
//		HSSFCell rowStatuory = rowLiabilities123.createCell(0);
//		rowStatuory.setCellValue("Reserves & Surplus");
//		rowStatuory.setCellStyle(my_style);
//
//		dataRow = dataRow++;
//
//		String reserPlus = "";
//
//		HSSFRow rowEarnings = spreadsheet.createRow(dataRow++);
//		HSSFCell cellEarnings = rowEarnings.createCell(0);
//		cellEarnings.setCellValue("Current earnings");
//		cellEarnings.setCellStyle(my_style);
//
//		String currEarnings = StrUtils.fString(profitLoss.getString("currentearnings"));
//		currEarnings = StrUtils.addZeroes(Double.parseDouble(currEarnings), noOfDecimal);
//
//		HSSFCell cellEarnings1 = rowEarnings.createCell(1);
//		cellEarnings1.setCellValue(new HSSFRichTextString(currEarnings));
//		cellEarnings1.setCellStyle(my_style);
//
//		HSSFRow rowTotalEquity = spreadsheet.createRow(dataRow++);
//		HSSFCell cellTotalEquity = rowTotalEquity.createCell(0);
//		cellTotalEquity.setCellValue("Total Equity");
//		cellTotalEquity.setCellStyle(my_style);
//
//		double totalEquity = Double.parseDouble(currEarnings) - Double.parseDouble(reserPlus);
//
//		String totalEuityVal = StrUtils.fString(String.valueOf(totalEquity));
//		totalEuityVal = StrUtils.addZeroes(Double.parseDouble(totalEuityVal), noOfDecimal);
//
//		HSSFCell cellTotalEquity1 = rowTotalEquity.createCell(1);
//		cellTotalEquity1.setCellValue(new HSSFRichTextString(totalEuityVal));
//		cellTotalEquity1.setCellStyle(my_style);

		spreadsheet.autoSizeColumn(0);
		spreadsheet.autoSizeColumn(1);
		spreadsheet.autoSizeColumn(2);
		}
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
