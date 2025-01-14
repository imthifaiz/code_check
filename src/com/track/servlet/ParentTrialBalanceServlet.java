package com.track.servlet;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
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
import com.track.db.object.ParentChildCmpDet;
import com.track.db.object.TrialBalance;
import com.track.db.object.TrialBalanceDetails;
import com.track.service.JournalService;
import com.track.serviceImplementation.JournalEntry;
import com.track.util.IMLogger;
import com.track.util.MLogger;
import com.track.util.StrUtils;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@WebServlet("/ParentTrialBalanceServlet")
public class ParentTrialBalanceServlet extends HttpServlet implements IMLogger {

	private static final long serialVersionUID = 1L;
	private boolean printLog = MLoggerConstant.TrialBalanceServlet_PRINTPLANTMASTERLOG;
	private boolean printInfo = MLoggerConstant.TrialBalanceServlet_PRINTPLANTMASTERINFO;
	String action = "";
	private JournalService journalService = new JournalEntry();

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		action = StrUtils.fString(request.getParameter("action")).trim();

	}

	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		action = StrUtils.fString(req.getParameter("action")).trim();
		//String plant = StrUtils.fString((String) req.getSession().getAttribute("PLANT")).trim();
		String user = StrUtils.fString((String) req.getSession().getAttribute("LOGIN_USER")).trim();
		String curency = StrUtils.fString((String) req.getSession().getAttribute("BASE_CURRENCY"));
		String accName = StrUtils.fString(req.getParameter("accname")).trim();

		String type = "ALL";

		if (action.equalsIgnoreCase("getTrialBalance")) {
			String fromDate = StrUtils.fString(req.getParameter("fromDate")).trim();
			String toDate = StrUtils.fString(req.getParameter("toDate")).trim();
			String plant = StrUtils.fString(req.getParameter("plant")).trim();
			String plist = StrUtils.fString(req.getParameter("plist")).trim();
			List<Journal> journalList = null;
			JSONArray jarray = new JSONArray();
			Map<Integer, TrialBalance> trialBalArr = new Hashtable<Integer, TrialBalance>();
			try {
				// journalList=journalService.getAllJournalHeader(plant, user, type);
				journalList = journalService.getJournalsByDateWithoutAttach(plant, fromDate, toDate);
				Set<Integer> AccountId = new HashSet<Integer>();

				for (Journal journal : journalList) {
					List<JournalDetail> journalDetails = journal.getJournalDetails();
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
								TrialBalance trialBal = new TrialBalance();
								trialBal.setAccount_id(journalDet.getACCOUNT_ID());
								trialBal.setAccount_name(coaJson.getString("account_name"));
								trialBal.setAccount_type(coaJson.getString("account_type"));
								trialBal.setExtended_type(coaJson.getString("account_det_type"));
								trialBal.setMain_account(coaJson.getString("mainaccname"));
								trialBal.setNet_credit(journalDet.getCREDITS());
								trialBal.setNet_debit(journalDet.getDEBITS());
								trialBalArr.put(journalDet.getACCOUNT_ID(), trialBal);
							}

							// trialBalArr.put(coaJson.getString("ACCOUNT_NAME"), journalDet.get)
						} else {
							TrialBalance trialBal = trialBalArr.get(journalDet.getACCOUNT_ID());
							if (trialBal != null) {
								Double debit = trialBal.getNet_debit() + journalDet.getDEBITS();
								Double credit = trialBal.getNet_credit() + journalDet.getCREDITS();
								trialBal.setNet_debit(debit);
								trialBal.setNet_credit(credit);
								trialBalArr.replace(journalDet.getACCOUNT_ID(), trialBal);
							}

						}
					}
				}

				trialBalArr.forEach((k, v) -> {
					// jarray.add(v);
					Double total = v.getNet_debit() - v.getNet_credit();
					if (total > 0) {
						v.setNet_debit(total);
						v.setNet_credit(0.00);
						System.out.println("Positive" + total);
					} else {
						total = Math.abs(total);
						v.setNet_debit(0.00);
						v.setNet_credit(total);
						System.out.println("Negative" + total);
					}
					trialBalArr.replace(k, v);
				});

				trialBalArr.forEach((k, v) -> {
					jarray.add(v);
				});
				// jarray.add(trialBalArr.values().toArray());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			if (action.equalsIgnoreCase("getTrialBalanceAsExcel") && !fromDate.isEmpty()) {
				PlantMstDAO _PlantMstDAO = new PlantMstDAO();

				HSSFWorkbook workbook = null;
				try {
					String numberOfDecimal = _PlantMstDAO.getNumberOfDecimal(plant);
					workbook = populateExcel("Trial Balance", fromDate , toDate, plist, plant);
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
				resp.setHeader("Content-Disposition", "attachment; filename=TrialBalance.xls");
				OutputStream outStream = resp.getOutputStream();
				outStream.write(outArray);
				outStream.flush();
				outStream.close();
			} else {
				resp.setContentType("application/json");
				resp.setCharacterEncoding("UTF-8");
				System.out.println(jarray.toString());
				resp.getWriter().write(jarray.toString());
				resp.getWriter().flush();
				resp.getWriter().close();
			}

		}else if (action.equalsIgnoreCase("getTrialBalanceAsExcel")) {
			String fromDate = StrUtils.fString(req.getParameter("fromDate")).trim();
			String toDate = StrUtils.fString(req.getParameter("toDate")).trim();
			String plist = StrUtils.fString(req.getParameter("plist")).trim();
			String plant = StrUtils.fString((String) req.getSession().getAttribute("PLANT")).trim();

			if (!fromDate.isEmpty()) {
				PlantMstDAO _PlantMstDAO = new PlantMstDAO();

				HSSFWorkbook workbook = null;
				try {
					workbook = populateExcel("Trial Balance", fromDate , toDate, plist, plant);
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
				resp.setHeader("Content-Disposition", "attachment; filename=TrialBalance.xls");
				OutputStream outStream = resp.getOutputStream();
				outStream.write(outArray);
				outStream.flush();
				outStream.close();
			} 

		} 
		
		
		else if (action.equalsIgnoreCase("getTrialBalanceDetails")) {
			JSONArray jarray = new JSONArray();
			String account = StrUtils.fString(req.getParameter("account")).trim();
			String fromDate = StrUtils.fString(req.getParameter("fromDate")).trim();
			String toDate = StrUtils.fString(req.getParameter("toDate")).trim();
			String plant = StrUtils.fString(req.getParameter("plant")).trim();
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

		else if (action.equalsIgnoreCase("getProjectTrialBalance")) {

			String fromDate = StrUtils.fString(req.getParameter("fromDate")).trim();
			String toDate = StrUtils.fString(req.getParameter("toDate")).trim();
			String projectId = StrUtils.fString(req.getParameter("projectId")).trim();
			String plant = StrUtils.fString(req.getParameter("plant")).trim();
			List<Journal> journalList = null;
			JSONArray jarray = new JSONArray();
			Map<Integer, TrialBalance> trialBalArr = new Hashtable<Integer, TrialBalance>();
			try {
				// journalList=journalService.getAllJournalHeader(plant, user, type);
				journalList = journalService.getProjectJournalsByDateWithoutAttach(plant, fromDate, toDate, projectId);
				Set<Integer> AccountId = new HashSet<Integer>();

				for (Journal journal : journalList) {
					List<JournalDetail> journalDetails = journal.getJournalDetails();
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
								TrialBalance trialBal = new TrialBalance();
								trialBal.setAccount_id(journalDet.getACCOUNT_ID());
								trialBal.setAccount_name(coaJson.getString("account_name"));
								trialBal.setAccount_type(coaJson.getString("account_type"));
								trialBal.setExtended_type(coaJson.getString("account_det_type"));
								trialBal.setMain_account(coaJson.getString("mainaccname"));
								trialBal.setNet_credit(journalDet.getCREDITS());
								trialBal.setNet_debit(journalDet.getDEBITS());
								trialBalArr.put(journalDet.getACCOUNT_ID(), trialBal);
							}

							// trialBalArr.put(coaJson.getString("ACCOUNT_NAME"), journalDet.get)
						} else {
							TrialBalance trialBal = trialBalArr.get(journalDet.getACCOUNT_ID());
							if (trialBal != null) {
								Double debit = trialBal.getNet_debit() + journalDet.getDEBITS();
								Double credit = trialBal.getNet_credit() + journalDet.getCREDITS();
								trialBal.setNet_debit(debit);
								trialBal.setNet_credit(credit);
								trialBalArr.replace(journalDet.getACCOUNT_ID(), trialBal);
							}

						}
					}
				}

				trialBalArr.forEach((k, v) -> {
					// jarray.add(v);
					Double total = v.getNet_debit() - v.getNet_credit();
					if (total > 0) {
						v.setNet_debit(total);
						v.setNet_credit(0.00);
						System.out.println("Positive" + total);
					} else {
						total = Math.abs(total);
						v.setNet_debit(0.00);
						v.setNet_credit(total);
						System.out.println("Negative" + total);
					}
					trialBalArr.replace(k, v);
				});

				trialBalArr.forEach((k, v) -> {
					jarray.add(v);
				});
				// jarray.add(trialBalArr.values().toArray());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			
				resp.setContentType("application/json");
				resp.setCharacterEncoding("UTF-8");
				System.out.println(jarray.toString());
				resp.getWriter().write(jarray.toString());
				resp.getWriter().flush();
				resp.getWriter().close();


		}else if (action.equalsIgnoreCase("getProjectTrialBalanceExcel")) {
			String fromDate = StrUtils.fString(req.getParameter("fromDate")).trim();
			String toDate = StrUtils.fString(req.getParameter("toDate")).trim();
			String plist = StrUtils.fString(req.getParameter("plist")).trim();
			String plant = StrUtils.fString((String) req.getSession().getAttribute("PLANT")).trim();
			String projectId = StrUtils.fString(req.getParameter("projectId")).trim();

			if (!fromDate.isEmpty()) {
				PlantMstDAO _PlantMstDAO = new PlantMstDAO();

				HSSFWorkbook workbook = null;
				try {
					workbook = populateExcelProjectTrialBalance("Project Trial Balance",fromDate , toDate, plist, plant,projectId);
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
				resp.setHeader("Content-Disposition", "attachment; filename=ProjectTrialBalance.xls");
				OutputStream outStream = resp.getOutputStream();
				outStream.write(outArray);
				outStream.flush();
				outStream.close();
			} 

		} 

		else if (action.equalsIgnoreCase("getProjectTrialBalanceExcel")) {
			PlantMstDAO _PlantMstDAO = new PlantMstDAO();
			JSONArray jarray = new JSONArray();
			String fromDate = StrUtils.fString(req.getParameter("fromDate")).trim();
			String toDate = StrUtils.fString(req.getParameter("toDate")).trim();
			String account = StrUtils.fString(req.getParameter("account")).trim();
			String plant = StrUtils.fString(req.getParameter("plant")).trim();

			List<TrialBalanceDetails> trialBalanceDetails = new ArrayList<TrialBalanceDetails>();
			try {
				trialBalanceDetails = journalService.getTrialBalanceDetailsByAccountByType(plant, account, fromDate,
						toDate);
				jarray.addAll(trialBalanceDetails);
				String numberOfDecimal = _PlantMstDAO.getNumberOfDecimal(plant);

				HSSFWorkbook workbook = null;
				String accountName = accName;
				try {

					workbook = populateExcelForAccounts(accountName, fromDate + "to" + toDate, jarray.toString(),
							numberOfDecimal, curency);
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
				resp.setHeader("Content-Disposition", "attachment; filename=" + accountName + ".xls");
				OutputStream outStream = resp.getOutputStream();
				outStream.write(outArray);
				outStream.flush();
				outStream.close();

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}

	public HSSFWorkbook populateExcelForAccounts(String headerTitle, String excelHeaderDate, String excelData,
			String noOfDecimal, String currency) throws JSONException {

		// Create blank workbook
		HSSFWorkbook workbook = new HSSFWorkbook();
		// Create a blank sheet
		HSSFSheet spreadsheet = workbook.createSheet(headerTitle);
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
		cell1.setCellValue("from " + excelHeaderDate);
		cell1.setCellStyle(my_style);

		HSSFRow row2 = spreadsheet.createRow(3);
		HSSFCell cellAccountHeader = row2.createCell(0);
		cellAccountHeader.setCellValue("DATE");
		cellAccountHeader.setCellStyle(my_style);
		HSSFCell cell11 = row2.createCell(1);
		cell11.setCellValue("ACCOUNT");
		cell11.setCellStyle(my_style);
		HSSFCell cell22 = row2.createCell(2);
		cell22.setCellValue("TRANSACTION_DETAILS");
		cell22.setCellStyle(my_style);
		HSSFCell cell33 = row2.createCell(3);
		cell33.setCellValue("TRANSACTION_TYPE");
		cell33.setCellStyle(my_style);
		HSSFCell cell44 = row2.createCell(4);
		cell44.setCellValue("TRANSACTION_ID");
		cell44.setCellStyle(my_style);
		HSSFCell cell55 = row2.createCell(5);
		cell55.setCellValue("REFERENCE");
		cell55.setCellStyle(my_style);
		HSSFCell cell66 = row2.createCell(6);
		cell66.setCellValue("DEBIT");
		cell66.setCellStyle(my_style);
		HSSFCell cell77 = row2.createCell(7);
		cell77.setCellValue("CREDIT");
		cell77.setCellStyle(my_style);
		HSSFCell cell88 = row2.createCell(8);
		cell88.setCellValue("AMOUNT");
		cell88.setCellStyle(my_style);

		Double totalNetDebit = 0.00;
		Double totalNetCredit = 0.00;
		Double totalClosingBalance = 0.00;

		HSSFRow row4 = spreadsheet.createRow(4);
		HSSFCell row4Data1 = row4.createCell(0);
		row4Data1.setCellValue(new HSSFRichTextString("As on " + excelHeaderDate.split("to")[0]));

		HSSFCell row4Data2 = row4.createCell(1);
		row4Data2.setCellValue(new HSSFRichTextString("Opening Balance"));

		HSSFCell row4Data22 = row4.createCell(7);
		row4Data22.setCellValue(new HSSFRichTextString(
				currency + " " + StrUtils.addZeroes(Double.parseDouble(String.valueOf(totalNetDebit)), noOfDecimal)));

		int dataRow = 5;
		org.json.JSONArray array = new org.json.JSONArray(excelData);

		for (int i = 0; i < array.length(); i++) {
			org.json.JSONObject object = array.getJSONObject(i);

			double debit = object.getDouble("DEBIT");
			double credit = object.getDouble("CREDIT");

			totalNetDebit = debit + totalNetDebit;
			totalNetCredit = credit + totalNetCredit;

			HSSFRow row5 = spreadsheet.createRow(dataRow);
			HSSFCell row5Data1 = row5.createCell(0);
			row5Data1.setCellValue(new HSSFRichTextString(StrUtils.fString(object.getString("DATE"))));

			HSSFCell row5Data2 = row5.createCell(1);
			row5Data2.setCellValue(new HSSFRichTextString(StrUtils.fString(object.getString("ACCOUNT"))));

			HSSFCell row5Data3 = row5.createCell(2);
			row5Data3.setCellValue(new HSSFRichTextString(StrUtils.fString(object.getString("TRANSACTION_DETAILS"))));
			HSSFCell row5Data4 = row5.createCell(3);
			row5Data4.setCellValue(new HSSFRichTextString(StrUtils.fString(object.getString("TRANSACTION_TYPE"))));
			HSSFCell row5Data5 = row5.createCell(4);
			row5Data5.setCellValue(new HSSFRichTextString(StrUtils.fString(object.getString("TRANSACTION_ID"))));
			HSSFCell row5Data6 = row5.createCell(5);
			row5Data6.setCellValue(new HSSFRichTextString(StrUtils.fString(object.getString("REFERENCE"))));
			HSSFCell row5Data7 = row5.createCell(6);

			String netDebitVal = StrUtils.fString(object.getString("DEBIT"));
			netDebitVal = StrUtils.addZeroes(Double.parseDouble(netDebitVal), noOfDecimal);
			row5Data7.setCellValue(new HSSFRichTextString(netDebitVal));
			HSSFCell row5Data8 = row5.createCell(7);

			String netCreditVal = StrUtils.fString(object.getString("CREDIT"));
			netCreditVal = StrUtils.addZeroes(Double.parseDouble(netCreditVal), noOfDecimal);

			row5Data8.setCellValue(new HSSFRichTextString(netCreditVal));
			if (netDebitVal.equals("0.00000")) {

				HSSFCell row5Data9 = row5.createCell(8);
				row5Data9.setCellValue(new HSSFRichTextString(StrUtils.fString(netCreditVal + " Cr")));
			}
			if (netCreditVal.equals("0.00000")) {
				HSSFCell row5Data9 = row5.createCell(8);
				row5Data9.setCellValue(new HSSFRichTextString(StrUtils.fString(netDebitVal + " Dr")));
			}

			dataRow++;

		}

		totalClosingBalance = totalNetDebit - totalNetCredit;
		HSSFRow rowTotal1 = spreadsheet.createRow(dataRow++);
		HSSFCell row4Data11 = rowTotal1.createCell(1);
		row4Data11.setCellValue(new HSSFRichTextString("Total Debits and Credits"));

		HSSFCell row4Data221 = rowTotal1.createCell(2);
		String date = "(" + excelHeaderDate.split("to")[0] + "-" + excelHeaderDate.split("to")[1] + ")";
		row4Data221.setCellValue(new HSSFRichTextString(date));

		String totalDebitVal = StrUtils.fString(String.valueOf(totalNetDebit));
		totalDebitVal = StrUtils.addZeroes(Double.parseDouble(totalDebitVal), noOfDecimal);

		String totalCreditVal = StrUtils.fString(String.valueOf(totalNetCredit));
		totalCreditVal = StrUtils.addZeroes(Double.parseDouble(totalCreditVal), noOfDecimal);

		HSSFCell row4Data222 = rowTotal1.createCell(6);
		row4Data222.setCellValue(new HSSFRichTextString(totalDebitVal));

		HSSFCell row4Data2222 = rowTotal1.createCell(7);
		row4Data2222.setCellValue(new HSSFRichTextString(totalCreditVal));

		HSSFRow rowLast = spreadsheet.createRow(dataRow++);
		HSSFCell ce11 = rowLast.createCell(0);
		ce11.setCellValue(new HSSFRichTextString("As on " + excelHeaderDate.split("to")[1]));

		HSSFCell rowLast12 = rowLast.createCell(1);
		rowLast12.setCellValue(new HSSFRichTextString("Closing Balance"));

		String totalClosingVal = StrUtils.fString(String.valueOf(totalClosingBalance));
		totalClosingVal = StrUtils.addZeroes(Double.parseDouble(totalClosingVal), noOfDecimal);

		// totalClosingBalance = totalNetDebit - totalNetCredit;
		if (totalNetDebit > totalNetCredit) {
			HSSFCell rowLast13 = rowLast.createCell(6);
			rowLast13.setCellValue(new HSSFRichTextString(currency + " " + totalClosingVal.replace("-", "")));

		} else if (totalNetDebit < totalNetCredit) {
			HSSFCell rowLast13 = rowLast.createCell(7);
			rowLast13.setCellValue(new HSSFRichTextString(currency + " " + totalClosingVal.replace("-", "")));

		}

		spreadsheet.autoSizeColumn(0);
		spreadsheet.autoSizeColumn(1);
		spreadsheet.autoSizeColumn(2);
		spreadsheet.autoSizeColumn(3);
		spreadsheet.autoSizeColumn(4);
		spreadsheet.autoSizeColumn(5);
		spreadsheet.autoSizeColumn(6);
		spreadsheet.autoSizeColumn(7);
		spreadsheet.autoSizeColumn(8);

		return workbook;

	}
	
	public HSSFWorkbook populateExcel(String headerTitle, String fromDate, String toDate, String plantclist, String plant1)
			throws Exception {
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
			
			List<Journal> journalList = null;
			JSONArray jarray = new JSONArray();
			Map<Integer, TrialBalance> trialBalArr = new Hashtable<Integer, TrialBalance>();
			try {
				// journalList=journalService.getAllJournalHeader(plant, user, type);
				journalList = journalService.getJournalsByDateWithoutAttach(plant, fromDate, toDate);
				Set<Integer> AccountId = new HashSet<Integer>();

				for (Journal journal : journalList) {
					List<JournalDetail> journalDetails = journal.getJournalDetails();
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
								TrialBalance trialBal = new TrialBalance();
								trialBal.setAccount_id(journalDet.getACCOUNT_ID());
								trialBal.setAccount_name(coaJson.getString("account_name"));
								trialBal.setAccount_type(coaJson.getString("account_type"));
								trialBal.setExtended_type(coaJson.getString("account_det_type"));
								trialBal.setMain_account(coaJson.getString("mainaccname"));
								trialBal.setNet_credit(journalDet.getCREDITS());
								trialBal.setNet_debit(journalDet.getDEBITS());
								trialBalArr.put(journalDet.getACCOUNT_ID(), trialBal);
							}

							// trialBalArr.put(coaJson.getString("ACCOUNT_NAME"), journalDet.get)
						} else {
							TrialBalance trialBal = trialBalArr.get(journalDet.getACCOUNT_ID());
							if (trialBal != null) {
								Double debit = trialBal.getNet_debit() + journalDet.getDEBITS();
								Double credit = trialBal.getNet_credit() + journalDet.getCREDITS();
								trialBal.setNet_debit(debit);
								trialBal.setNet_credit(credit);
								trialBalArr.replace(journalDet.getACCOUNT_ID(), trialBal);
							}

						}
					}
				}

				trialBalArr.forEach((k, v) -> {
					// jarray.add(v);
					Double total = v.getNet_debit() - v.getNet_credit();
					if (total > 0) {
						v.setNet_debit(total);
						v.setNet_credit(0.00);
						System.out.println("Positive" + total);
					} else {
						total = Math.abs(total);
						v.setNet_debit(0.00);
						v.setNet_credit(total);
						System.out.println("Negative" + total);
					}
					trialBalArr.replace(k, v);
				});

				trialBalArr.forEach((k, v) -> {
					jarray.add(v);
				});
				// jarray.add(trialBalArr.values().toArray());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
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
		HSSFCell netDebitHeader = row2.createCell(1);
		netDebitHeader.setCellValue("Net Debit");
		netDebitHeader.setCellStyle(my_style);
		HSSFCell netCreditHeader = row2.createCell(2);
		netCreditHeader.setCellValue("Net Credit");
		netCreditHeader.setCellStyle(my_style);
		HSSFRow row3 = spreadsheet.createRow(4);
		HSSFCell row3Data = row3.createCell(0);
		row3Data.setCellValue("Assets");
		row3Data.setCellStyle(my_style);

		System.out.println(jarray.toString());
		int dataRow = 5;
		org.json.JSONArray array = new org.json.JSONArray(jarray.toString());

		for (int i = 0; i < array.length(); i++) {
			org.json.JSONObject object = array.getJSONObject(i);
			if (object.getString("main_account").equalsIgnoreCase("assets")) {
				HSSFRow row4 = spreadsheet.createRow(dataRow);
				HSSFCell row4Data1 = row4.createCell(0);
				row4Data1.setCellValue(new HSSFRichTextString(StrUtils.fString(object.getString("account_name"))));

				HSSFCell row4Data2 = row4.createCell(1);

				String netDebitVal = StrUtils.fString(object.getString("net_debit"));
				netDebitVal = StrUtils.addZeroes(Double.parseDouble(netDebitVal), noOfDecimal);

				row4Data2.setCellValue(new HSSFRichTextString(netDebitVal));

				String netCreditVal = StrUtils.fString(object.getString("net_credit"));
				netCreditVal = StrUtils.addZeroes(Double.parseDouble(netCreditVal), noOfDecimal);

				HSSFCell row4Data3 = row4.createCell(2);
				row4Data3.setCellValue(new HSSFRichTextString(netCreditVal));
				dataRow++;
			}

		}

		HSSFRow rowLiabilities = spreadsheet.createRow(dataRow++);
		HSSFCell rowLiab = rowLiabilities.createCell(0);
		rowLiab.setCellValue("Liabilities");
		rowLiab.setCellStyle(my_style);

		int liaDataRow = dataRow;

		for (int i = 0; i < array.length(); i++) {
			org.json.JSONObject object = array.getJSONObject(i);
			if (object.getString("main_account").equalsIgnoreCase("liabilities")) {
				HSSFRow rowLia1 = spreadsheet.createRow(liaDataRow);
				HSSFCell rowLiaData1 = rowLia1.createCell(0);
				rowLiaData1.setCellValue(new HSSFRichTextString(StrUtils.fString(object.getString("account_name"))));

				String netDebitVal = StrUtils.fString(object.getString("net_debit"));
				netDebitVal = StrUtils.addZeroes(Double.parseDouble(netDebitVal), noOfDecimal);

				String netCreditVal = StrUtils.fString(object.getString("net_credit"));
				netCreditVal = StrUtils.addZeroes(Double.parseDouble(netCreditVal), noOfDecimal);

				HSSFCell rowLiaData2 = rowLia1.createCell(1);
				rowLiaData2.setCellValue(new HSSFRichTextString(netDebitVal));

				HSSFCell rowLiaData3 = rowLia1.createCell(2);
				rowLiaData3.setCellValue(new HSSFRichTextString(netCreditVal));
				liaDataRow++;
			}

		}

		HSSFRow rowEquity = spreadsheet.createRow(liaDataRow++);
		HSSFCell cellEquity = rowEquity.createCell(0);
		cellEquity.setCellValue("Equity");
		cellEquity.setCellStyle(my_style);

		int equityDataRow = liaDataRow;

		for (int i = 0; i < array.length(); i++) {
			org.json.JSONObject object = array.getJSONObject(i);
			if (object.getString("main_account").equalsIgnoreCase("Equity")) {
				HSSFRow rowLia1 = spreadsheet.createRow(equityDataRow);
				HSSFCell rowLiaData1 = rowLia1.createCell(0);
				rowLiaData1.setCellValue(new HSSFRichTextString(StrUtils.fString(object.getString("account_name"))));

				String netDebitVal = StrUtils.fString(object.getString("net_debit"));
				netDebitVal = StrUtils.addZeroes(Double.parseDouble(netDebitVal), noOfDecimal);

				String netCreditVal = StrUtils.fString(object.getString("net_credit"));
				netCreditVal = StrUtils.addZeroes(Double.parseDouble(netCreditVal), noOfDecimal);

				HSSFCell rowLiaData2 = rowLia1.createCell(1);
				rowLiaData2.setCellValue(new HSSFRichTextString(netDebitVal));

				HSSFCell rowLiaData3 = rowLia1.createCell(2);
				rowLiaData3.setCellValue(new HSSFRichTextString(netCreditVal));
				equityDataRow++;
			}

		}

		HSSFRow rowIncome = spreadsheet.createRow(equityDataRow++);
		HSSFCell cellIncome = rowIncome.createCell(0);
		cellIncome.setCellValue("Income");
		cellIncome.setCellStyle(my_style);

		int incomeDataRow = equityDataRow;

		for (int i = 0; i < array.length(); i++) {
			org.json.JSONObject object = array.getJSONObject(i);
			if (object.getString("main_account").equalsIgnoreCase("Income")) {
				HSSFRow rowLia1 = spreadsheet.createRow(incomeDataRow);
				HSSFCell rowLiaData1 = rowLia1.createCell(0);
				rowLiaData1.setCellValue(new HSSFRichTextString(StrUtils.fString(object.getString("account_name"))));

				String netDebitVal = StrUtils.fString(object.getString("net_debit"));
				netDebitVal = StrUtils.addZeroes(Double.parseDouble(netDebitVal), noOfDecimal);

				String netCreditVal = StrUtils.fString(object.getString("net_credit"));
				netCreditVal = StrUtils.addZeroes(Double.parseDouble(netCreditVal), noOfDecimal);

				HSSFCell rowLiaData2 = rowLia1.createCell(1);
				rowLiaData2.setCellValue(new HSSFRichTextString(netDebitVal));

				HSSFCell rowLiaData3 = rowLia1.createCell(2);
				rowLiaData3.setCellValue(new HSSFRichTextString(netCreditVal));
				incomeDataRow++;
			}

		}

		HSSFRow rowExpenses = spreadsheet.createRow(incomeDataRow++);
		HSSFCell cellExpenses = rowExpenses.createCell(0);
		cellExpenses.setCellValue("Expenses");
		cellExpenses.setCellStyle(my_style);

		int expenseDataRow = incomeDataRow;

		double totalNetdebit = 0.00;
		double totalNetCredit = 0.00;

		for (int i = 0; i < array.length(); i++) {
			org.json.JSONObject object = array.getJSONObject(i);

			double debit = object.getDouble("net_debit");
			double credit = object.getDouble("net_credit");

			totalNetdebit = debit + totalNetdebit;
			totalNetCredit = credit + totalNetCredit;

			if (object.getString("main_account").equalsIgnoreCase("Expenses")) {
				HSSFRow rowLia1 = spreadsheet.createRow(expenseDataRow);
				HSSFCell rowLiaData1 = rowLia1.createCell(0);
				rowLiaData1.setCellValue(new HSSFRichTextString(StrUtils.fString(object.getString("account_name"))));

				String netDebitVal = StrUtils.fString(object.getString("net_debit"));
				netDebitVal = StrUtils.addZeroes(Double.parseDouble(netDebitVal), noOfDecimal);

				String netCreditVal = StrUtils.fString(object.getString("net_credit"));
				netCreditVal = StrUtils.addZeroes(Double.parseDouble(netCreditVal), noOfDecimal);

				HSSFCell rowLiaData2 = rowLia1.createCell(1);
				rowLiaData2.setCellValue(new HSSFRichTextString(netDebitVal));

				HSSFCell rowLiaData3 = rowLia1.createCell(2);
				rowLiaData3.setCellValue(new HSSFRichTextString(netCreditVal));
				expenseDataRow++;
			}

		}

		HSSFRow rowTotal = spreadsheet.createRow(expenseDataRow++);
		HSSFCell cellTotal = rowTotal.createCell(0);
		cellTotal.setCellValue("Total");
		cellTotal.setCellStyle(my_style);

		String totalDebitVal = StrUtils.fString(String.valueOf(totalNetdebit));
		totalDebitVal = StrUtils.addZeroes(Double.parseDouble(totalDebitVal), noOfDecimal);

		String totalCreditVal = StrUtils.fString(String.valueOf(totalNetCredit));
		totalCreditVal = StrUtils.addZeroes(Double.parseDouble(totalCreditVal), noOfDecimal);

		HSSFCell cellTotal1 = rowTotal.createCell(1);
		cellTotal1.setCellValue(totalDebitVal);
		cellTotal1.setCellStyle(my_style);

		HSSFCell cellTotal2 = rowTotal.createCell(2);
		cellTotal2.setCellValue(totalCreditVal);
		cellTotal2.setCellStyle(my_style);

		spreadsheet.autoSizeColumn(0);
		spreadsheet.autoSizeColumn(1);
		spreadsheet.autoSizeColumn(2);
		
		}

		return workbook;
	}
	
	
	
	public HSSFWorkbook populateExcelProjectTrialBalance(String headerTitle, String fromDate, String toDate, String plantclist, String plant1,String projectId)
			throws Exception {
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
			
			List<Journal> journalList = null;
			JSONArray jarray = new JSONArray();
			Map<Integer, TrialBalance> trialBalArr = new Hashtable<Integer, TrialBalance>();
			try {
				// journalList=journalService.getAllJournalHeader(plant, user, type);
				journalList = journalService.getProjectJournalsByDateWithoutAttach(plant, fromDate, toDate, projectId);
				Set<Integer> AccountId = new HashSet<Integer>();

				for (Journal journal : journalList) {
					List<JournalDetail> journalDetails = journal.getJournalDetails();
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
								TrialBalance trialBal = new TrialBalance();
								trialBal.setAccount_id(journalDet.getACCOUNT_ID());
								trialBal.setAccount_name(coaJson.getString("account_name"));
								trialBal.setAccount_type(coaJson.getString("account_type"));
								trialBal.setExtended_type(coaJson.getString("account_det_type"));
								trialBal.setMain_account(coaJson.getString("mainaccname"));
								trialBal.setNet_credit(journalDet.getCREDITS());
								trialBal.setNet_debit(journalDet.getDEBITS());
								trialBalArr.put(journalDet.getACCOUNT_ID(), trialBal);
							}

							// trialBalArr.put(coaJson.getString("ACCOUNT_NAME"), journalDet.get)
						} else {
							TrialBalance trialBal = trialBalArr.get(journalDet.getACCOUNT_ID());
							if (trialBal != null) {
								Double debit = trialBal.getNet_debit() + journalDet.getDEBITS();
								Double credit = trialBal.getNet_credit() + journalDet.getCREDITS();
								trialBal.setNet_debit(debit);
								trialBal.setNet_credit(credit);
								trialBalArr.replace(journalDet.getACCOUNT_ID(), trialBal);
							}

						}
					}
				}

				trialBalArr.forEach((k, v) -> {
					// jarray.add(v);
					Double total = v.getNet_debit() - v.getNet_credit();
					if (total > 0) {
						v.setNet_debit(total);
						v.setNet_credit(0.00);
						System.out.println("Positive" + total);
					} else {
						total = Math.abs(total);
						v.setNet_debit(0.00);
						v.setNet_credit(total);
						System.out.println("Negative" + total);
					}
					trialBalArr.replace(k, v);
				});

				trialBalArr.forEach((k, v) -> {
					jarray.add(v);
				});
				// jarray.add(trialBalArr.values().toArray());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
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
		HSSFCell netDebitHeader = row2.createCell(1);
		netDebitHeader.setCellValue("Net Debit");
		netDebitHeader.setCellStyle(my_style);
		HSSFCell netCreditHeader = row2.createCell(2);
		netCreditHeader.setCellValue("Net Credit");
		netCreditHeader.setCellStyle(my_style);
		HSSFRow row3 = spreadsheet.createRow(4);
		HSSFCell row3Data = row3.createCell(0);
		row3Data.setCellValue("Assets");
		row3Data.setCellStyle(my_style);

		System.out.println(jarray.toString());
		int dataRow = 5;
		org.json.JSONArray array = new org.json.JSONArray(jarray.toString());

		for (int i = 0; i < array.length(); i++) {
			org.json.JSONObject object = array.getJSONObject(i);
			if (object.getString("main_account").equalsIgnoreCase("assets")) {
				HSSFRow row4 = spreadsheet.createRow(dataRow);
				HSSFCell row4Data1 = row4.createCell(0);
				row4Data1.setCellValue(new HSSFRichTextString(StrUtils.fString(object.getString("account_name"))));

				HSSFCell row4Data2 = row4.createCell(1);

				String netDebitVal = StrUtils.fString(object.getString("net_debit"));
				netDebitVal = StrUtils.addZeroes(Double.parseDouble(netDebitVal), noOfDecimal);

				row4Data2.setCellValue(new HSSFRichTextString(netDebitVal));

				String netCreditVal = StrUtils.fString(object.getString("net_credit"));
				netCreditVal = StrUtils.addZeroes(Double.parseDouble(netCreditVal), noOfDecimal);

				HSSFCell row4Data3 = row4.createCell(2);
				row4Data3.setCellValue(new HSSFRichTextString(netCreditVal));
				dataRow++;
			}

		}

		HSSFRow rowLiabilities = spreadsheet.createRow(dataRow++);
		HSSFCell rowLiab = rowLiabilities.createCell(0);
		rowLiab.setCellValue("Liabilities");
		rowLiab.setCellStyle(my_style);

		int liaDataRow = dataRow;

		for (int i = 0; i < array.length(); i++) {
			org.json.JSONObject object = array.getJSONObject(i);
			if (object.getString("main_account").equalsIgnoreCase("liabilities")) {
				HSSFRow rowLia1 = spreadsheet.createRow(liaDataRow);
				HSSFCell rowLiaData1 = rowLia1.createCell(0);
				rowLiaData1.setCellValue(new HSSFRichTextString(StrUtils.fString(object.getString("account_name"))));

				String netDebitVal = StrUtils.fString(object.getString("net_debit"));
				netDebitVal = StrUtils.addZeroes(Double.parseDouble(netDebitVal), noOfDecimal);

				String netCreditVal = StrUtils.fString(object.getString("net_credit"));
				netCreditVal = StrUtils.addZeroes(Double.parseDouble(netCreditVal), noOfDecimal);

				HSSFCell rowLiaData2 = rowLia1.createCell(1);
				rowLiaData2.setCellValue(new HSSFRichTextString(netDebitVal));

				HSSFCell rowLiaData3 = rowLia1.createCell(2);
				rowLiaData3.setCellValue(new HSSFRichTextString(netCreditVal));
				liaDataRow++;
			}

		}

		HSSFRow rowEquity = spreadsheet.createRow(liaDataRow++);
		HSSFCell cellEquity = rowEquity.createCell(0);
		cellEquity.setCellValue("Equity");
		cellEquity.setCellStyle(my_style);

		int equityDataRow = liaDataRow;

		for (int i = 0; i < array.length(); i++) {
			org.json.JSONObject object = array.getJSONObject(i);
			if (object.getString("main_account").equalsIgnoreCase("Equity")) {
				HSSFRow rowLia1 = spreadsheet.createRow(equityDataRow);
				HSSFCell rowLiaData1 = rowLia1.createCell(0);
				rowLiaData1.setCellValue(new HSSFRichTextString(StrUtils.fString(object.getString("account_name"))));

				String netDebitVal = StrUtils.fString(object.getString("net_debit"));
				netDebitVal = StrUtils.addZeroes(Double.parseDouble(netDebitVal), noOfDecimal);

				String netCreditVal = StrUtils.fString(object.getString("net_credit"));
				netCreditVal = StrUtils.addZeroes(Double.parseDouble(netCreditVal), noOfDecimal);

				HSSFCell rowLiaData2 = rowLia1.createCell(1);
				rowLiaData2.setCellValue(new HSSFRichTextString(netDebitVal));

				HSSFCell rowLiaData3 = rowLia1.createCell(2);
				rowLiaData3.setCellValue(new HSSFRichTextString(netCreditVal));
				equityDataRow++;
			}

		}

		HSSFRow rowIncome = spreadsheet.createRow(equityDataRow++);
		HSSFCell cellIncome = rowIncome.createCell(0);
		cellIncome.setCellValue("Income");
		cellIncome.setCellStyle(my_style);

		int incomeDataRow = equityDataRow;

		for (int i = 0; i < array.length(); i++) {
			org.json.JSONObject object = array.getJSONObject(i);
			if (object.getString("main_account").equalsIgnoreCase("Income")) {
				HSSFRow rowLia1 = spreadsheet.createRow(incomeDataRow);
				HSSFCell rowLiaData1 = rowLia1.createCell(0);
				rowLiaData1.setCellValue(new HSSFRichTextString(StrUtils.fString(object.getString("account_name"))));

				String netDebitVal = StrUtils.fString(object.getString("net_debit"));
				netDebitVal = StrUtils.addZeroes(Double.parseDouble(netDebitVal), noOfDecimal);

				String netCreditVal = StrUtils.fString(object.getString("net_credit"));
				netCreditVal = StrUtils.addZeroes(Double.parseDouble(netCreditVal), noOfDecimal);

				HSSFCell rowLiaData2 = rowLia1.createCell(1);
				rowLiaData2.setCellValue(new HSSFRichTextString(netDebitVal));

				HSSFCell rowLiaData3 = rowLia1.createCell(2);
				rowLiaData3.setCellValue(new HSSFRichTextString(netCreditVal));
				incomeDataRow++;
			}

		}

		HSSFRow rowExpenses = spreadsheet.createRow(incomeDataRow++);
		HSSFCell cellExpenses = rowExpenses.createCell(0);
		cellExpenses.setCellValue("Expenses");
		cellExpenses.setCellStyle(my_style);

		int expenseDataRow = incomeDataRow;

		double totalNetdebit = 0.00;
		double totalNetCredit = 0.00;

		for (int i = 0; i < array.length(); i++) {
			org.json.JSONObject object = array.getJSONObject(i);

			double debit = object.getDouble("net_debit");
			double credit = object.getDouble("net_credit");

			totalNetdebit = debit + totalNetdebit;
			totalNetCredit = credit + totalNetCredit;

			if (object.getString("main_account").equalsIgnoreCase("Expenses")) {
				HSSFRow rowLia1 = spreadsheet.createRow(expenseDataRow);
				HSSFCell rowLiaData1 = rowLia1.createCell(0);
				rowLiaData1.setCellValue(new HSSFRichTextString(StrUtils.fString(object.getString("account_name"))));

				String netDebitVal = StrUtils.fString(object.getString("net_debit"));
				netDebitVal = StrUtils.addZeroes(Double.parseDouble(netDebitVal), noOfDecimal);

				String netCreditVal = StrUtils.fString(object.getString("net_credit"));
				netCreditVal = StrUtils.addZeroes(Double.parseDouble(netCreditVal), noOfDecimal);

				HSSFCell rowLiaData2 = rowLia1.createCell(1);
				rowLiaData2.setCellValue(new HSSFRichTextString(netDebitVal));

				HSSFCell rowLiaData3 = rowLia1.createCell(2);
				rowLiaData3.setCellValue(new HSSFRichTextString(netCreditVal));
				expenseDataRow++;
			}

		}

		HSSFRow rowTotal = spreadsheet.createRow(expenseDataRow++);
		HSSFCell cellTotal = rowTotal.createCell(0);
		cellTotal.setCellValue("Total");
		cellTotal.setCellStyle(my_style);

		String totalDebitVal = StrUtils.fString(String.valueOf(totalNetdebit));
		totalDebitVal = StrUtils.addZeroes(Double.parseDouble(totalDebitVal), noOfDecimal);

		String totalCreditVal = StrUtils.fString(String.valueOf(totalNetCredit));
		totalCreditVal = StrUtils.addZeroes(Double.parseDouble(totalCreditVal), noOfDecimal);

		HSSFCell cellTotal1 = rowTotal.createCell(1);
		cellTotal1.setCellValue(totalDebitVal);
		cellTotal1.setCellStyle(my_style);

		HSSFCell cellTotal2 = rowTotal.createCell(2);
		cellTotal2.setCellValue(totalCreditVal);
		cellTotal2.setCellStyle(my_style);

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
