package com.track.servlet;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.json.JSONException;

import com.track.constants.MLoggerConstant;
import com.track.dao.BillPaymentDAO;
import com.track.dao.CoaDAO;
import com.track.dao.CustMstDAO;
import com.track.dao.InvoicePaymentDAO;
import com.track.dao.JournalDAO;
import com.track.dao.ParentChildCmpDetDAO;
import com.track.dao.PlantMstDAO;
import com.track.dao.VendMstDAO;
import com.track.db.object.InvPaymentHeader;
import com.track.db.object.Journal;
import com.track.db.object.JournalAttachment;
import com.track.db.object.JournalDetail;
import com.track.db.object.JournalHeader;
import com.track.db.object.LedgerDetails;
import com.track.db.object.LedgerDetailsRec;
import com.track.db.object.ParentChildCmpDet;
import com.track.db.object.ReconciliationDatePojo;
import com.track.db.object.ReconciliationPojo;
import com.track.gates.DbBean;
import com.track.service.JournalService;
import com.track.service.LedgerService;
import com.track.serviceImplementation.JournalEntry;
import com.track.serviceImplementation.LedgerServiceImpl;
import com.track.util.DateUtils;
import com.track.util.FileHandling;
import com.track.util.IMLogger;
import com.track.util.MLogger;
import com.track.util.StrUtils;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@WebServlet("/ParentJournalServlet")
public class ParentJournalServlet extends HttpServlet implements IMLogger {
	private static final long serialVersionUID = 1L;
	private boolean printLog = MLoggerConstant.JournalServlet_PRINTPLANTMASTERLOG;
	private boolean printInfo = MLoggerConstant.JournalServlet_PRINTPLANTMASTERINFO;

	private JournalService journalService = new JournalEntry();
	private LedgerService ledgerService = new LedgerServiceImpl();
	String action = "";
	StrUtils strUtils = new StrUtils();

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String username = StrUtils.fString((String) request.getSession().getAttribute("LOGIN_USER")).trim();
		String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
		JournalService journalService = new JournalEntry();
		CoaDAO coadao = new CoaDAO();
		action = strUtils.fString(request.getParameter("action"));
		
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String username = StrUtils.fString((String) request.getSession().getAttribute("LOGIN_USER")).trim();
		//String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
		action = strUtils.fString(request.getParameter("action"));
		if (action.equalsIgnoreCase("getJournalReport")) {
			JSONArray jarray = new JSONArray();
			String fromDate = StrUtils.fString(request.getParameter("fromDate")).trim();
			String toDate = StrUtils.fString(request.getParameter("toDate")).trim();
			String plant = StrUtils.fString(request.getParameter("plant")).trim();
			// String limit = StrUtils.fString(request.getParameter("limit")).trim();
			List<LedgerDetails> ledgerDetails = new ArrayList<LedgerDetails>();
			try {
				ledgerDetails = ledgerService.getLedgerDetailsByDate(plant, fromDate, toDate);
				jarray.addAll(ledgerDetails);
				response.setContentType("application/json");
				response.setCharacterEncoding("UTF-8");
				response.getWriter().write(jarray.toString());
				response.getWriter().flush();
				response.getWriter().close();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if (action.equalsIgnoreCase("getJournalReportAsExcel")) {
			
			String fromDate = StrUtils.fString(request.getParameter("fromDate")).trim();
			String toDate = StrUtils.fString(request.getParameter("toDate")).trim();
			String plant = StrUtils.fString((String) request.getSession().getAttribute("PLANT")).trim();
			// String limit = StrUtils.fString(request.getParameter("limit")).trim();
			String plist = StrUtils.fString(request.getParameter("plist")).trim();
			
			try {
			
				HSSFWorkbook workbook = null;
				try {
					//workbook = populateExcel("Journal Report", toDate, jarray.toString(), numberOfDecimal);
					workbook = populateExcel("Journal Report", fromDate , toDate, plist, plant);
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
				response.setHeader("Content-Disposition", "attachment; filename=JournalReport.xls");
				OutputStream outStream = response.getOutputStream();
				outStream.write(outArray);
				outStream.flush();
				outStream.close();

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} 

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
			
			JSONArray jarray = new JSONArray();
			List<LedgerDetails> ledgerDetails = new ArrayList<LedgerDetails>();
			ledgerDetails = ledgerService.getLedgerDetailsByDate(plant, fromDate, toDate);
			jarray.addAll(ledgerDetails);
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
		netDebitHeader.setCellValue("Debit");
		netDebitHeader.setCellStyle(my_style);
		HSSFCell netCreditHeader = row2.createCell(2);
		netCreditHeader.setCellValue("Credit");
		netCreditHeader.setCellStyle(my_style);
		
		int dataRow = 4;
		

		
		org.json.JSONArray array = new org.json.JSONArray(jarray.toString());
		
		String existingrowHeader = "";

		for (int i = 0; i < array.length(); i++) 
		{
				org.json.JSONObject object = array.getJSONObject(i);
			
				String rowHeader = object.getString("DATE") + "-"+object.getString("TRANSACTION_TYPE") +"  "+object.getString("TRANSACTION_ID");
				
				if(!existingrowHeader.equalsIgnoreCase(rowHeader))
				{
					HSSFRow row4 = spreadsheet.createRow(dataRow++);
					HSSFCell row4Data1 = row4.createCell(0);
					row4Data1.setCellValue(new HSSFRichTextString(StrUtils.fString(rowHeader)));
					row4Data1.setCellStyle(my_style);
					
				}
				
				

				HSSFRow row5 = spreadsheet.createRow(dataRow++);
				HSSFCell row4Data11 = row5.createCell(0);
				row4Data11.setCellValue(new HSSFRichTextString(StrUtils.fString(object.getString("ACCOUNT"))));
				
				
				
				String netDebitVal = StrUtils.fString(object.getString("DEBIT"));
				netDebitVal = StrUtils.addZeroes(Double.parseDouble(netDebitVal), noOfDecimal);				
				HSSFCell row4Data2 = row5.createCell(1);
				row4Data2.setCellValue(new HSSFRichTextString(netDebitVal));

				String netCreditVal = StrUtils.fString(object.getString("CREDIT"));
				netCreditVal = StrUtils.addZeroes(Double.parseDouble(netCreditVal), noOfDecimal);

				HSSFCell row4Data3 = row5.createCell(2);
				row4Data3.setCellValue(new HSSFRichTextString(netCreditVal));
				//dataRow++;
				existingrowHeader = rowHeader;
			

		}



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
