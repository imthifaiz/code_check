package com.track.servlet;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.transaction.UserTransaction;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;

import com.oreilly.servlet.MultipartRequest;
import com.track.constants.IConstants;
import com.track.constants.IDBConstants;
import com.track.constants.MLoggerConstant;
import com.track.constants.TransactionConstants;
import com.track.dao.MovHisDAO;
import com.track.dao.OutletBeanDAO;
import com.track.dao.PlantMstDAO;
import com.track.dao.VendMstDAO;
import com.track.db.util.CountSheetDownloaderUtil;
import com.track.db.util.ItemUtil;
import com.track.db.util.PlantMstUtil;
import com.track.gates.DbBean;
import com.track.tran.WmsTran;
import com.track.tran.WmsTranItemSup;
import com.track.tran.WmsTranDesc;
import com.track.tran.WmsTranImg;
import com.track.tran.WmsTranItem;
import com.track.tran.WmsUploadAlternateProduct;
import com.track.tran.WmsUploadItemSheet;
import com.track.util.DateUtils;
import com.track.util.MLogger;
import com.track.util.StrUtils;

/**
 * Servlet implementation class ImportItemServlet
 */
public class ImportItemServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	private boolean printLog = MLoggerConstant.ProductServlet_PRINTPLANTMASTERLOG;
	private boolean printInfo = MLoggerConstant.ProductServlet_PRINTPLANTMASTERINFO;
	private static final String CONTENT_TYPE = "text/html; charset=windows-1252";
	private MLogger mlogger = new MLogger();
	String action = "";
	String PLANT = "";
	String login_user = "";
	String sys_date = "";
	String StrFileName = "";
	String orgFilePath = "";
	String StrSheetName = "";
	String result = "";
	String divertTo = "";
        String truncateval="";

	public ImportItemServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		action = StrUtils.fString(request.getParameter("action")).trim();

		StrFileName = StrUtils.fString(request.getParameter("ImportFile"));
		StrSheetName = StrUtils.fString(request.getParameter("SheetName"));
		
	        truncateval = StrUtils.fString(request.getParameter("Truncate"));
	        System.out.println("truncateval  *********:" + truncateval);
		orgFilePath = StrFileName;
		System.out.println("Import File  *********:" + orgFilePath);
		System.out.println("Import File  *********:" + StrFileName);
		System.out.println("Import sheet *********:" + StrSheetName);

		PLANT = (String) request.getSession().getAttribute("PLANT");
		login_user = (String) request.getSession().getAttribute("LOGIN_USER");
		sys_date = DateUtils.getDate();
		try {
			
			if (action.equalsIgnoreCase("importCountSheet")) {
				onImportCountSheet(request, response);
			} else if (action.equalsIgnoreCase("confirmCountSheet")) {
				onConfirmCountSheet(request, response);
			} else if (action.equalsIgnoreCase("downloadItemTemplate")) {
				onDownloadItemTemplate(request, response);
			//imti added additional	
			} else if (action.equalsIgnoreCase("downloadItemDescTemplate")) {
				onDownloadItemDescTemplate(request, response);
			} else if (action.equalsIgnoreCase("downloadItemImgTemplate")) {
				onDownloadItemImgTemplate(request, response);
			} else if (action.equalsIgnoreCase("downloadAddItemTemplate")) {
				onDownloadAddItemTemplate(request, response);
			}else if (action.equalsIgnoreCase("importDescCountSheet")) {
				onImportDescCountSheet(request, response);
			}else if (action.equalsIgnoreCase("importImgCountSheet")) {
				onImportImgCountSheet(request, response);
			}else if (action.equalsIgnoreCase("importItemCountSheet")) {
				onImportItemCountSheet(request, response);
			} else if (action.equalsIgnoreCase("confirmDescCountSheet")) {
				onConfirmDescCountSheet(request, response);
			} else if (action.equalsIgnoreCase("confirmImgCountSheet")) {
				onConfirmImgCountSheet(request, response);
			} else if (action.equalsIgnoreCase("confirmItemCountSheet")) {
				onConfirmItemCountSheet(request, response);
			} else if (action.equalsIgnoreCase("downloadItemsupplierTemplate")) {
				onDownloadAddItemSupplierTemplate(request, response);
			}else if (action.equalsIgnoreCase("importItemSupplierCountSheet")) {
				onImportItemSupplierCountSheet(request, response);
			} else if (action.equalsIgnoreCase("confirmItemSupplierCountSheet")) {
				onConfirmItemSupplierCountSheet(request, response);
			//imti end	
			}else if (action.equalsIgnoreCase("importAltPrdCountSheet")) {
				onImportAltrPrdCountSheet(request, response);
			} else if (action.equalsIgnoreCase("confirmAltPrdCountSheet")) {
				onConfirmAltrPrdCountSheet(request, response);
			} else if (action.equalsIgnoreCase("downloadAltPrdTemplate")) {
				onDownloadAltrPrdTemplate(request, response);
				
			} else if (action.equalsIgnoreCase("downloadMinMaxTemplate")) {
				onDownloadMinMaxTemplate(request, response);
			}else if (action.equalsIgnoreCase("importMinMaxCountSheet")) {
				onImportMinMaxCountSheet(request, response);
			} else if (action.equalsIgnoreCase("confirmMinMaxCountSheet")) {
				onConfirmMinMaxCountSheet(request, response);
				
			} else if (action.equalsIgnoreCase("downloadOutletMinMaxTemplate")) {
				onDownloadOutletMinMaxTemplate(request, response);
			}else if (action.equalsIgnoreCase("importOutletMinMaxCountSheet")) {
				onImportOutletMinMaxCountSheet(request, response);
			} else if (action.equalsIgnoreCase("confirmOutletMinMaxCountSheet")) {
				onConfirmOutletMinMaxCountSheet(request, response);
				
			} else if (action.equalsIgnoreCase("downloadContactTemplate")) {
				onDownloadContactTemplate(request, response);
			}else if (action.equalsIgnoreCase("importContactCountSheet")) {
				onImportContactCountSheet(request, response);
			} else if (action.equalsIgnoreCase("confirmContactCountSheet")) {
				onConfirmContactCountSheet(request, response);
				
			} else if(action.equals("Export_Excel")){
				 HSSFWorkbook wb = new HSSFWorkbook();
				 wb = this.writeToExcel(request,response,wb);
				 ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
				 wb.write(outByteStream);
				 byte [] outArray = outByteStream.toByteArray();
				 response.setContentType("application/ms-excel");
				 response.setContentLength(outArray.length);
				 response.setHeader("Expires:", "0"); // eliminates browser caching
				 response.setHeader("Content-Disposition", "attachment; filename=ProductList.xls");
				 OutputStream outStream = response.getOutputStream();
				 outStream.write(outArray);
				 outStream.flush();

				} else if(action.equals("AlternetItemExport_Excel")){
				 HSSFWorkbook wb = new HSSFWorkbook();
				 wb = this.writeToExcelAlternateItem(request,response,wb);
				 ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
				 wb.write(outByteStream);
				 byte [] outArray = outByteStream.toByteArray();
				 response.setContentType("application/ms-excel");
				 response.setContentLength(outArray.length);
				 response.setHeader("Expires:", "0"); // eliminates browser caching
				 response.setHeader("Content-Disposition", "attachment; filename=AlternateProductList.xls");
				 OutputStream outStream = response.getOutputStream();
				 outStream.write(outArray);
				 outStream.flush();				 
			 }

		} catch (Exception e) {

		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		response.setContentType(CONTENT_TYPE);
		doGet(request, response);
	}

	private void onImportCountSheet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException,
			Exception {
		String PLANT = (String) request.getSession().getAttribute("PLANT");
		String NOOFINVENTORY = (String) request.getSession().getAttribute("NOOFINVENTORY");
		System.out.println("********************* onImportCountSheet **************************");
		String result = "";
		ArrayList alRes = new ArrayList();

		try {

			try {

				MultipartRequest mreq = new MultipartRequest(request,
						DbBean.CountSheetUploadPath, 2048000);
			} catch (Exception eee) {

				throw eee;

			}

			request.getSession().setAttribute("IMP_ITEMRESULT", null);
			// get the sheet name
			String filename = "";
			File f = new File(StrFileName);
			filename = f.getName();
			f = null;

			// folder
			StrFileName = DbBean.CountSheetUploadPath + filename;

			// StrFileName = DbBean.CountSheetUploadPath + filename;
			System.out.println("After conversion Import File  *********:"
					+ StrFileName);

			CountSheetDownloaderUtil util = new CountSheetDownloaderUtil();

			alRes = util.downloadItemSheetData(PLANT, StrFileName, StrSheetName,truncateval,NOOFINVENTORY	);

			if (alRes.size() > 0) {
				String ValidNumber ="";
				for (int j = 0; j < alRes.size(); j++) {
				Map mValidNumber=(Map)alRes.get(j);
				ValidNumber = (String)mValidNumber.get("ValidNumber");
				}
				
				int index = alRes.size() - 1; 
				alRes.remove(index);
				
				if(ValidNumber.equalsIgnoreCase(""))
				{
				mlogger.info("Data Imported Successfully");
				result = "<font color=\"green\">Data Imported Successfully,Click on Confirm Buttton to upload data. </font>";
				}
				else
				{
					mlogger.info("Data Imported Successfully,You have reached the limit");
					if(ValidNumber.equalsIgnoreCase("INVENTORY"))
					result = "<font color=\"red\">You have reached the limit of "+NOOFINVENTORY+" products you can create</font>";
				}
			} else {
				throw new Exception(
						"no data found  for given data in the excel");
			}

		} catch (Exception e) {

			// throw e;
			result = "<font color=\"red\">" + e.getMessage() + "</font>";
		}

		System.out.println("Setting into session ");

		request.getSession().setAttribute("ITEMRESULT", result);
		request.getSession().setAttribute("IMP_ITEMRESULT", alRes);
		//       
		response.sendRedirect("../product/u-cloproduct?ImportFile="
				+ orgFilePath + "&SheetName=" + StrSheetName + "");

	}
	
	private void onDownloadItemTemplate(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException,
			Exception {
		
		String path = DbBean.DownloadItemTemplate;

		response.setContentType("application/vnd.ms-excel");
		response.setHeader("Content-Disposition", "attachment; filename=\"ProductMstData.xls\"");
		response.setHeader("cache-control", "no-cache");
		response.setHeader("Pragma", "no-cache");
		//java.net.URL url = new java.net.URL("file://"+path);
		//File file = new File(url.getPath());
		path = path.replace("\\", File.separator);
		File file = new File(path);
		System.out.println("Path:" + file.getPath());
		System.out.print("file exist:" + file.exists());
		java.io.FileInputStream fileInputStream = new java.io.FileInputStream(file);
		BufferedInputStream bin = new BufferedInputStream(fileInputStream);

		int i;
		//byte[] b = new byte[10];
		ServletOutputStream sosStream = response.getOutputStream();
		while ((i = bin.read()) != -1) {
			sosStream.write(i);
		}

		sosStream.flush();
		sosStream.close();
		bin.close();
		fileInputStream.close();

	}
	
	
	private void onDownloadAddItemSupplierTemplate(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException,Exception {
		String path = DbBean.DownloadItemSupplierTemplate;
		response.setContentType("application/vnd.ms-excel");
		response.setHeader("Content-Disposition", "attachment; filename=\"ItemSupplierData.xls\"");
		response.setHeader("cache-control", "no-cache");
		response.setHeader("Pragma", "no-cache");
		path = path.replace("\\", File.separator);
		File file = new File(path);
		System.out.println("Path:" + file.getPath());
		System.out.print("file exist:" + file.exists());
		java.io.FileInputStream fileInputStream = new java.io.FileInputStream(file);
		BufferedInputStream bin = new BufferedInputStream(fileInputStream);
		int i;
		ServletOutputStream sosStream = response.getOutputStream();
		while ((i = bin.read()) != -1) {
			sosStream.write(i);
		}
		sosStream.flush();
		sosStream.close();
		bin.close();
		fileInputStream.close();
	}
	
	private void onImportItemSupplierCountSheet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException,Exception {
		String PLANT = (String) request.getSession().getAttribute("PLANT");
		System.out.println("********************* onImportCountSheet **************************");
		String result = "";
		ArrayList alRes = new ArrayList();
		try {
			try {
				MultipartRequest mreq = new MultipartRequest(request,DbBean.CountSheetUploadPath, 2048000);
			} catch (Exception eee) {
				throw eee;
			}
			request.getSession().setAttribute("IMP_ITEMRESULT", null);
			String filename = "";
			File f = new File(StrFileName);
			filename = f.getName();
			f = null;
			// folder
			StrFileName = DbBean.CountSheetUploadPath + filename;
			// StrFileName = DbBean.CountSheetUploadPath + filename;
			System.out.println("After conversion Import File  *********:"+ StrFileName);
			CountSheetDownloaderUtil util = new CountSheetDownloaderUtil();
			alRes = util.downloadItemSupplierSheetData(PLANT, StrFileName, StrSheetName	);
			if (alRes.size() > 0) {
				String ValidNumber ="";
				for (int j = 0; j < alRes.size(); j++) {
					Map mValidNumber=(Map)alRes.get(j);
					ValidNumber = (String)mValidNumber.get("ValidNumber");
				}
				int index = alRes.size() - 1; 
				alRes.remove(index);
				if(ValidNumber.equalsIgnoreCase(""))
				{
					mlogger.info("Data Imported Successfully");
					result = "<font color=\"green\">Data Imported Successfully,Click on Confirm Buttton to upload data. </font>";
				}
				else
				{
					mlogger.info("Data Imported Successfully,You have reached the limit");
				}
			} else {
				throw new Exception(
						"no data found  for given data in the excel");
			}
			
		} catch (Exception e) {
			result = "<font color=\"red\">" + e.getMessage() + "</font>";
		}
		System.out.println("Setting into session ");
		request.getSession().setAttribute("ITEMRESULT", result);
		request.getSession().setAttribute("IMP_ITEMRESULT", alRes);
		response.sendRedirect("../product/importitemsupplier?ImportFile="+ orgFilePath + "&SheetName=" + StrSheetName + "");
	}
	
	@SuppressWarnings("unchecked")
	private void onConfirmItemSupplierCountSheet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException,Exception {
		String result = "";
		ArrayList alRes = new ArrayList();
		UserTransaction ut = null;
		boolean flag = true;
		boolean errflg = false;
		try {
			ut = com.track.gates.DbBean.getUserTranaction();
			ut.begin();
			String PLANT = (String) request.getSession().getAttribute("PLANT");
			String _login_user = (String) request.getSession().getAttribute("LOGIN_USER");
			ArrayList al = (ArrayList) request.getSession().getAttribute("IMP_ITEMRESULT");
			Map linemap = new HashMap();
			for (int iCnt = 0; iCnt < al.size(); iCnt++) {
				Map lineArr = (Map) al.get(iCnt);
				if(flag){
					linemap.put("LOGIN_USER", _login_user);
					linemap.put("PLANT", PLANT);
					linemap.put(IConstants.ITEM, (String) lineArr.get(IConstants.ITEM));
					linemap.put("SUP1", (String) lineArr.get("SUP1"));
					linemap.put("SUP2", (String) lineArr.get("SUP2"));
					linemap.put("SUP3", (String) lineArr.get("SUP3"));
					linemap.put("SUP4", (String) lineArr.get("SUP4"));
					linemap.put("SUP5", (String) lineArr.get("SUP5"));
					linemap.put("SUP6", (String) lineArr.get("SUP6"));
					linemap.put("SUP7", (String) lineArr.get("SUP7"));
					linemap.put("SUP8", (String) lineArr.get("SUP8"));
					linemap.put("SUP9", (String) lineArr.get("SUP9"));
					linemap.put("SUP10", (String) lineArr.get("SUP10"));
					flag = process_Wms_ItemSup_CountSheet(linemap);
				}
			}
			
			if (flag == true) {
				DbBean.CommitTran(ut);
				request.getSession().removeAttribute("IMP_ITEMRESULT");
				flag = true;
				result = "<font color=\"green\">Product Assigned Supplier Confirmed Successfully</font>";
			} else {
				DbBean.RollbackTran(ut);
				flag = false;
				result = "<font color=\"red\">Error in Confirming Product Assigned Supplier </font>";
			}
			
		} catch (Exception e) {
			flag = false;
			DbBean.RollbackTran(ut);
			request.getSession().setAttribute("ITEMRESULT", result);
			result = "<font color=\"red\">"+ e.getMessage() +"</font>";
		}
		request.getSession().setAttribute("ITEMRESULT", result);
		response.sendRedirect("../product/importitemsupplier");
	}
	
	private void onDownloadItemDescTemplate(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException,Exception {
		String path = DbBean.DownloadItemDescTemplate;
		response.setContentType("application/vnd.ms-excel");
		response.setHeader("Content-Disposition", "attachment; filename=\"AdditonalProductDescData.xls\"");
		response.setHeader("cache-control", "no-cache");
		response.setHeader("Pragma", "no-cache");
		path = path.replace("\\", File.separator);
		File file = new File(path);
		System.out.println("Path:" + file.getPath());
		System.out.print("file exist:" + file.exists());
		java.io.FileInputStream fileInputStream = new java.io.FileInputStream(file);
		BufferedInputStream bin = new BufferedInputStream(fileInputStream);
		int i;
		ServletOutputStream sosStream = response.getOutputStream();
		while ((i = bin.read()) != -1) {
			sosStream.write(i);
		}
		sosStream.flush();
		sosStream.close();
		bin.close();
		fileInputStream.close();
	}
	
	private void onDownloadItemImgTemplate(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException,Exception {
		String path = DbBean.DownloadItemImgTemplate;
		response.setContentType("application/vnd.ms-excel");
		response.setHeader("Content-Disposition", "attachment; filename=\"AdditionalProductImages.xls\"");
		response.setHeader("cache-control", "no-cache");
		response.setHeader("Pragma", "no-cache");
		path = path.replace("\\", File.separator);
		File file = new File(path);
		System.out.println("Path:" + file.getPath());
		System.out.print("file exist:" + file.exists());
		java.io.FileInputStream fileInputStream = new java.io.FileInputStream(file);
		BufferedInputStream bin = new BufferedInputStream(fileInputStream);
		int i;
		ServletOutputStream sosStream = response.getOutputStream();
		while ((i = bin.read()) != -1) {
			sosStream.write(i);
		}
		sosStream.flush();
		sosStream.close();
		bin.close();
		fileInputStream.close();
	}
	
	private void onDownloadAddItemTemplate(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException,Exception {
		String path = DbBean.DownloadAddItemTemplate;
		response.setContentType("application/vnd.ms-excel");
		response.setHeader("Content-Disposition", "attachment; filename=\"AdditionalProductData.xls\"");
		response.setHeader("cache-control", "no-cache");
		response.setHeader("Pragma", "no-cache");
		path = path.replace("\\", File.separator);
		File file = new File(path);
		System.out.println("Path:" + file.getPath());
		System.out.print("file exist:" + file.exists());
		java.io.FileInputStream fileInputStream = new java.io.FileInputStream(file);
		BufferedInputStream bin = new BufferedInputStream(fileInputStream);
		int i;
		ServletOutputStream sosStream = response.getOutputStream();
		while ((i = bin.read()) != -1) {
			sosStream.write(i);
		}
		sosStream.flush();
		sosStream.close();
		bin.close();
		fileInputStream.close();
	}
	
	private void onImportDescCountSheet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException,Exception {
		String PLANT = (String) request.getSession().getAttribute("PLANT");
		System.out.println("********************* onImportCountSheet **************************");
		String result = "";
		ArrayList alRes = new ArrayList();
		try {
			try {
				MultipartRequest mreq = new MultipartRequest(request,DbBean.CountSheetUploadPath, 2048000);
			} catch (Exception eee) {
				throw eee;
			}
			request.getSession().setAttribute("IMP_ITEMRESULT", null);
			String filename = "";
			File f = new File(StrFileName);
			filename = f.getName();
			f = null;
			// folder
			StrFileName = DbBean.CountSheetUploadPath + filename;
			// StrFileName = DbBean.CountSheetUploadPath + filename;
			System.out.println("After conversion Import File  *********:"+ StrFileName);
			CountSheetDownloaderUtil util = new CountSheetDownloaderUtil();
			alRes = util.downloadItemDescSheetData(PLANT, StrFileName, StrSheetName);
			if (alRes.size() > 0) {
				String ValidNumber ="";
				for (int j = 0; j < alRes.size(); j++) {
					Map mValidNumber=(Map)alRes.get(j);
					ValidNumber = (String)mValidNumber.get("ValidNumber");
				}
				int index = alRes.size() - 1; 
				alRes.remove(index);
				if(ValidNumber.equalsIgnoreCase(""))
				{
					mlogger.info("Data Imported Successfully");
					result = "<font color=\"green\">Data Imported Successfully,Click on Confirm Buttton to upload data. </font>";
				}
				else
				{
					mlogger.info("Data Imported Successfully,You have reached the limit");
				}
			} else {
				throw new Exception(
						"no data found  for given data in the excel");
			}
			
		} catch (Exception e) {
			result = "<font color=\"red\">" + e.getMessage() + "</font>";
		}
		System.out.println("Setting into session ");
		request.getSession().setAttribute("ITEMRESULT", result);
		request.getSession().setAttribute("IMP_ITEMRESULT", alRes);
		response.sendRedirect("../product/additional?cmd=desc&ImportFile="+ orgFilePath + "&SheetName=" + StrSheetName + "");
	}
	
	private void onImportImgCountSheet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException,Exception {
		String PLANT = (String) request.getSession().getAttribute("PLANT");
		System.out.println("********************* onImportCountSheet **************************");
		String result = "";
		ArrayList alRes = new ArrayList();
		try {
			try {
				MultipartRequest mreq = new MultipartRequest(request,DbBean.CountSheetUploadPath, 2048000);
			} catch (Exception eee) {
				throw eee;
			}
			request.getSession().setAttribute("IMP_ITEMRESULT", null);
			String filename = "";
			File f = new File(StrFileName);
			filename = f.getName();
			f = null;
			// folder
			StrFileName = DbBean.CountSheetUploadPath + filename;
			// StrFileName = DbBean.CountSheetUploadPath + filename;
			System.out.println("After conversion Import File  *********:"+ StrFileName);
			CountSheetDownloaderUtil util = new CountSheetDownloaderUtil();
			alRes = util.downloadItemimgSheetData(PLANT, StrFileName, StrSheetName);
			if (alRes.size() > 0) {
				String ValidNumber ="";
				for (int j = 0; j < alRes.size(); j++) {
					Map mValidNumber=(Map)alRes.get(j);
					ValidNumber = (String)mValidNumber.get("ValidNumber");
				}
				int index = alRes.size() - 1; 
				alRes.remove(index);
				if(ValidNumber.equalsIgnoreCase(""))
				{
					mlogger.info("Data Imported Successfully");
					result = "<font color=\"green\">Data Imported Successfully,Click on Confirm Buttton to upload data. </font>";
				}
				else
				{
					mlogger.info("Data Imported Successfully,You have reached the limit");
				}
			} else {
				throw new Exception(
						"no data found  for given data in the excel");
			}
			
		} catch (Exception e) {
			result = "<font color=\"red\">" + e.getMessage() + "</font>";
		}
		System.out.println("Setting into session ");
		request.getSession().setAttribute("ITEMRESULT", result);
		request.getSession().setAttribute("IMP_ITEMRESULT", alRes);
		response.sendRedirect("../product/additional?cmd=img&ImportFile="+ orgFilePath + "&SheetName=" + StrSheetName + "");
	}
	
	private void onImportItemCountSheet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException,Exception {
		String PLANT = (String) request.getSession().getAttribute("PLANT");
		System.out.println("********************* onImportCountSheet **************************");
		String result = "";
		ArrayList alRes = new ArrayList();
		try {
			try {
				MultipartRequest mreq = new MultipartRequest(request,DbBean.CountSheetUploadPath, 2048000);
			} catch (Exception eee) {
				throw eee;
			}
			request.getSession().setAttribute("IMP_ITEMRESULT", null);
			String filename = "";
			File f = new File(StrFileName);
			filename = f.getName();
			f = null;
			// folder
			StrFileName = DbBean.CountSheetUploadPath + filename;
			// StrFileName = DbBean.CountSheetUploadPath + filename;
			System.out.println("After conversion Import File  *********:"+ StrFileName);
			CountSheetDownloaderUtil util = new CountSheetDownloaderUtil();
			alRes = util.downloadAddItemSheetData(PLANT, StrFileName, StrSheetName	);
			if (alRes.size() > 0) {
				String ValidNumber ="";
				for (int j = 0; j < alRes.size(); j++) {
					Map mValidNumber=(Map)alRes.get(j);
					ValidNumber = (String)mValidNumber.get("ValidNumber");
				}
				int index = alRes.size() - 1; 
				alRes.remove(index);
				if(ValidNumber.equalsIgnoreCase(""))
				{
					mlogger.info("Data Imported Successfully");
					result = "<font color=\"green\">Data Imported Successfully,Click on Confirm Buttton to upload data. </font>";
				}
				else
				{
					mlogger.info("Data Imported Successfully,You have reached the limit");
				}
			} else {
				throw new Exception(
						"no data found  for given data in the excel");
			}
			
		} catch (Exception e) {
			result = "<font color=\"red\">" + e.getMessage() + "</font>";
		}
		System.out.println("Setting into session ");
		request.getSession().setAttribute("ITEMRESULT", result);
		request.getSession().setAttribute("IMP_ITEMRESULT", alRes);
		response.sendRedirect("../product/additional?cmd=item&ImportFile="+ orgFilePath + "&SheetName=" + StrSheetName + "");
	}

	@SuppressWarnings("unchecked")
	private void onConfirmDescCountSheet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException,Exception {
		String result = "";
		ArrayList alRes = new ArrayList();
		UserTransaction ut = null;
		boolean flag = true;
		boolean errflg = false;
		try {
			ut = com.track.gates.DbBean.getUserTranaction();
			ut.begin();
			String PLANT = (String) request.getSession().getAttribute("PLANT");
			String _login_user = (String) request.getSession().getAttribute("LOGIN_USER");
			ArrayList al = (ArrayList) request.getSession().getAttribute("IMP_ITEMRESULT");
			Map linemap = new HashMap();
			for (int iCnt = 0; iCnt < al.size(); iCnt++) {
				Map lineArr = (Map) al.get(iCnt);
				if(flag){
					linemap.put("LOGIN_USER", _login_user);
					linemap.put("PLANT", PLANT);
					linemap.put(IConstants.ITEM, (String) lineArr.get(IConstants.ITEM));
					linemap.put("ITEM_DESC1", (String) lineArr.get("ITEM_DESC1"));
					linemap.put("ITEM_DESC2", (String) lineArr.get("ITEM_DESC2"));
					linemap.put("ITEM_DESC3", (String) lineArr.get("ITEM_DESC3"));
					linemap.put("ITEM_DESC4", (String) lineArr.get("ITEM_DESC4"));
					linemap.put("ITEM_DESC5", (String) lineArr.get("ITEM_DESC5"));
					linemap.put("ITEM_DESC6", (String) lineArr.get("ITEM_DESC6"));
					linemap.put("ITEM_DESC7", (String) lineArr.get("ITEM_DESC7"));
					linemap.put("ITEM_DESC8", (String) lineArr.get("ITEM_DESC8"));
					linemap.put("ITEM_DESC9", (String) lineArr.get("ITEM_DESC9"));
					linemap.put("ITEM_DESC10", (String) lineArr.get("ITEM_DESC10"));
					flag = process_Wms_Desc_CountSheet(linemap);
				}
			}
			
			if (flag == true) {
				DbBean.CommitTran(ut);
				request.getSession().removeAttribute("IMP_ITEMRESULT");
				flag = true;
				result = "<font color=\"green\">Additional Detail Description Confirmed Successfully</font>";
			} else {
				DbBean.RollbackTran(ut);
				flag = false;
				result = "<font color=\"red\">Error in Confirming Additional Detail Description</font>";
			}
			
		} catch (Exception e) {
			flag = false;
			DbBean.RollbackTran(ut);
			request.getSession().setAttribute("ITEMRESULT", result);
			result = "<font color=\"red\">"+ e.getMessage() +"</font>";
		}
		request.getSession().setAttribute("ITEMRESULT", result);
		response.sendRedirect("../product/additional?cmd=desc");
	}
	
	@SuppressWarnings("unchecked")
	private void onConfirmImgCountSheet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException,Exception {
		String result = "";
		ArrayList alRes = new ArrayList();
		UserTransaction ut = null;
		boolean flag = true;
		boolean errflg = false;
		try {
			ut = com.track.gates.DbBean.getUserTranaction();
			ut.begin();
			String PLANT = (String) request.getSession().getAttribute("PLANT");
			String _login_user = (String) request.getSession().getAttribute("LOGIN_USER");
			ArrayList al = (ArrayList) request.getSession().getAttribute("IMP_ITEMRESULT");
			Map linemap = new HashMap();
			for (int iCnt = 0; iCnt < al.size(); iCnt++) {
				Map lineArr = (Map) al.get(iCnt);
				if(flag){
					linemap.put("LOGIN_USER", _login_user);
					linemap.put("PLANT", PLANT);
					linemap.put(IConstants.ITEM, (String) lineArr.get(IConstants.ITEM));
					linemap.put("IMG1", lineArr.get("IMG1"));
					linemap.put("IMG2", lineArr.get("IMG2"));
					linemap.put("IMG3", lineArr.get("IMG3"));
					linemap.put("IMG4", lineArr.get("IMG4"));
					linemap.put("IMG5", lineArr.get("IMG5"));
					flag = process_Wms_Img_CountSheet(linemap);
				}
			}
			
			if (flag == true) {
				DbBean.CommitTran(ut);
				request.getSession().removeAttribute("IMP_ITEMRESULT");
				flag = true;
				result = "<font color=\"green\">Additional Products Images Confirmed Successfully</font>";
			} else {
				DbBean.RollbackTran(ut);
				flag = false;
				result = "<font color=\"red\">Error in Confirming Additional Products Images</font>";
			}
			
		} catch (Exception e) {
			flag = false;
			DbBean.RollbackTran(ut);
			request.getSession().setAttribute("ITEMRESULT", result);
			result = "<font color=\"red\">"+ e.getMessage() +"</font>";
		}
		request.getSession().setAttribute("ITEMRESULT", result);
		response.sendRedirect("../product/additional?cmd=img");
	}
	
	@SuppressWarnings("unchecked")
	private void onConfirmItemCountSheet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException,Exception {
		String result = "";
		ArrayList alRes = new ArrayList();
		UserTransaction ut = null;
		boolean flag = true;
		boolean errflg = false;
		try {
			ut = com.track.gates.DbBean.getUserTranaction();
			ut.begin();
			String PLANT = (String) request.getSession().getAttribute("PLANT");
			String _login_user = (String) request.getSession().getAttribute("LOGIN_USER");
			ArrayList al = (ArrayList) request.getSession().getAttribute("IMP_ITEMRESULT");
			Map linemap = new HashMap();
			for (int iCnt = 0; iCnt < al.size(); iCnt++) {
				Map lineArr = (Map) al.get(iCnt);
				if(flag){
					linemap.put("LOGIN_USER", _login_user);
					linemap.put("PLANT", PLANT);
					linemap.put(IConstants.ITEM, (String) lineArr.get(IConstants.ITEM));
					linemap.put("ITEM1", (String) lineArr.get("ITEM1"));
					linemap.put("ITEM2", (String) lineArr.get("ITEM2"));
					linemap.put("ITEM3", (String) lineArr.get("ITEM3"));
					linemap.put("ITEM4", (String) lineArr.get("ITEM4"));
					linemap.put("ITEM5", (String) lineArr.get("ITEM5"));
					linemap.put("ITEM6", (String) lineArr.get("ITEM6"));
					linemap.put("ITEM7", (String) lineArr.get("ITEM7"));
					linemap.put("ITEM8", (String) lineArr.get("ITEM8"));
					linemap.put("ITEM9", (String) lineArr.get("ITEM9"));
					linemap.put("ITEM10", (String) lineArr.get("ITEM10"));
					flag = process_Wms_Item_CountSheet(linemap);
				}
			}
			
			if (flag == true) {
				DbBean.CommitTran(ut);
				request.getSession().removeAttribute("IMP_ITEMRESULT");
				flag = true;
				result = "<font color=\"green\">Additional Products Confirmed Successfully</font>";
			} else {
				DbBean.RollbackTran(ut);
				flag = false;
				result = "<font color=\"red\">Error in Confirming Additional Products </font>";
			}
			
		} catch (Exception e) {
			flag = false;
			DbBean.RollbackTran(ut);
			request.getSession().setAttribute("ITEMRESULT", result);
			result = "<font color=\"red\">"+ e.getMessage() +"</font>";
		}
		request.getSession().setAttribute("ITEMRESULT", result);
		response.sendRedirect("../product/additional?cmd=item");
	}

	@SuppressWarnings("unchecked")
	private void onConfirmCountSheet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException,
			Exception {
		String result = "";
		ArrayList alRes = new ArrayList();

		UserTransaction ut = null;
		boolean flag = true;
		boolean errflg = false;
		try {
			ut = com.track.gates.DbBean.getUserTranaction();
			ut.begin();
			String PLANT = (String) request.getSession().getAttribute("PLANT");
			String _login_user = (String) request.getSession().getAttribute(
					"LOGIN_USER");

			ArrayList al = (ArrayList) request.getSession().getAttribute(
					"IMP_ITEMRESULT");

			Map linemap = new HashMap();
			for (int iCnt = 0; iCnt < al.size(); iCnt++) {
				Map lineArr = (Map) al.get(iCnt);
				
				if(flag){
					
					linemap.put("LOGIN_USER", _login_user);
					linemap.put("PLANT", PLANT);
					linemap.put(IConstants.ITEM, (String) lineArr.get(IConstants.ITEM));
					linemap.put(IConstants.ITEM_DESC, (String) lineArr.get(IConstants.ITEM_DESC));
					linemap.put(IConstants.PRICE, (String) lineArr.get(IConstants.PRICE));
					//linemap.put(IConstants.SERVICEPRICE, (String) lineArr.get(IConstants.SERVICEPRICE));
					//linemap.put(IConstants.RENTALPRICE, (String) lineArr.get(IConstants.RENTALPRICE));
					linemap.put(IConstants.MIN_S_PRICE, (String) lineArr.get(IConstants.MIN_S_PRICE));
					linemap.put(IConstants.COST, (String) lineArr.get(IConstants.COST));
					linemap.put(IConstants.PRODGST, (String) lineArr.get(IConstants.PRODGST));
					//linemap.put(IConstants.DISCOUNT, (String) lineArr.get(IConstants.DISCOUNT));
					linemap.put(IConstants.STKQTY, (String) lineArr.get(IConstants.STKQTY));
					linemap.put(IConstants.MAXSTKQTY, (String) lineArr.get(IConstants.MAXSTKQTY));
					linemap.put(IDBConstants.PRDDEPTID, (String) lineArr.get(IDBConstants.PRDDEPTID));//Resvi-08/10/21
					linemap.put(IDBConstants.PRDCLSID, (String) lineArr.get(IDBConstants.PRDCLSID));
					linemap.put(IDBConstants.ITEMMST_ITEM_TYPE, (String) lineArr.get(IDBConstants.ITEMMST_ITEM_TYPE));
					// Start code added by Bruhan for product brand on 11/9/12
					linemap.put(IDBConstants.PRDBRANDID, (String) lineArr.get(IDBConstants.PRDBRANDID));
					// End code added by Bruhan for product brand on 11/9/12
					linemap.put(IConstants.STKUOM, (String) lineArr.get(IConstants.STKUOM));
					linemap.put(IConstants.PURCHASEUOM, (String) lineArr.get(IConstants.PURCHASEUOM));
					linemap.put(IConstants.SALESUOM, (String) lineArr.get(IConstants.SALESUOM));
					//linemap.put(IConstants.RENTALUOM, (String) lineArr.get(IConstants.RENTALUOM));
					//linemap.put(IConstants.SERVICEUOM, (String)lineArr.get(IConstants.SERVICEUOM));
					linemap.put(IConstants.INVENTORYUOM, (String) lineArr.get(IConstants.INVENTORYUOM));
					if (((String) lineArr.get(IConstants.ISBASICUOM)).equals("Y")){
					linemap.put(IConstants.ISBASICUOM, ("1") );
					linemap.put(IConstants.PURCHASEUOM, (String) lineArr.get(IConstants.STKUOM));
					linemap.put(IConstants.SALESUOM, (String) lineArr.get(IConstants.STKUOM));
					//linemap.put(IConstants.RENTALUOM, (String) lineArr.get(IConstants.STKUOM));
					//linemap.put(IConstants.SERVICEUOM, (String) lineArr.get(IConstants.STKUOM));
					linemap.put(IConstants.INVENTORYUOM, (String) lineArr.get(IConstants.STKUOM));
					}
					else{
					linemap.put(IConstants.ISBASICUOM, ("0"));
					linemap.put(IConstants.PURCHASEUOM, (String) lineArr.get(IConstants.PURCHASEUOM));
					linemap.put(IConstants.SALESUOM, (String) lineArr.get(IConstants.SALESUOM));
					//linemap.put(IConstants.RENTALUOM, (String) lineArr.get(IConstants.RENTALUOM));
					//linemap.put(IConstants.SERVICEUOM, (String)lineArr.get(IConstants.SERVICEUOM));
					linemap.put(IConstants.INVENTORYUOM, (String) lineArr.get(IConstants.INVENTORYUOM));
					}
					linemap.put(IConstants.VINNO, (String) lineArr.get(IDBConstants.VINNO));
					linemap.put(IConstants.MODEL, (String) lineArr.get(IDBConstants.MODEL));
					linemap.put(IDBConstants.ITEMMST_REMARK3, (String) lineArr.get(IDBConstants.ITEMMST_REMARK3));
					linemap.put(IDBConstants.ITEMMST_REMARK4, (String) lineArr.get(IDBConstants.ITEMMST_REMARK4));
					linemap.put(IDBConstants.ITEMMST_REMARK1, (String) lineArr.get(IDBConstants.ITEMMST_REMARK1));
					linemap.put(IConstants.NETWEIGHT, (String) lineArr.get(IConstants.NETWEIGHT));
					linemap.put(IConstants.GROSSWEIGHT, (String) lineArr.get(IConstants.GROSSWEIGHT));
					linemap.put(IConstants.DIMENSION, (String) lineArr.get(IConstants.DIMENSION));
					linemap.put(IConstants.HSCODE, (String) lineArr.get(IConstants.HSCODE));
					linemap.put(IConstants.COO, (String) lineArr.get(IConstants.COO));
					linemap.put(IConstants.ISKITTING, lineArr.get(IConstants.ISKITTING));
					linemap.put(IConstants.ISACTIVE, lineArr.get(IConstants.ISACTIVE));
					linemap.put(IConstants.NONSTKFLAG, lineArr.get(IConstants.NONSTKFLAG));
					linemap.put(IConstants.CATLOGPATH, lineArr.get(IConstants.CATLOGPATH));
					//linemap.put("ITEM_LOC", lineArr.get("ITEM_LOC"));
					linemap.put("ISCOMPRO",lineArr.get("ISCOMPRO"));
					linemap.put("CPPI",lineArr.get("CPPI"));
					linemap.put("INCPRICE",lineArr.get("INCPRICE"));
					linemap.put("INCPRICEUNIT",lineArr.get("INCPRICEUNIT"));
					linemap.put(IConstants.VENDNO,lineArr.get(IConstants.VENDNO));
					linemap.put(IConstants.PRDDEPTID,lineArr.get(IConstants.PRDDEPTID));
					
					
					flag = process_Wms_CountSheet(linemap);
				}
				
			}
		

			if (flag == true) {
				DbBean.CommitTran(ut);
				request.getSession().removeAttribute("IMP_ITEMRESULT");
				flag = true;
				result = "<font color=\"green\">Products Confirmed Successfully</font>";
			} else {
				DbBean.RollbackTran(ut);
				flag = false;
				result = "<font color=\"red\">Error in Confirming Products </font>";
			}

		} catch (Exception e) {
			flag = false;
			DbBean.RollbackTran(ut);
			request.getSession().setAttribute("ITEMRESULT", result);
			//throw e;
			result = "<font color=\"red\">"+ e.getMessage() +"</font>";
		}

		request.getSession().setAttribute("ITEMRESULT", result);
		response.sendRedirect("../product/u-cloproduct?");

	}

	private boolean process_Wms_CountSheet(Map map) throws Exception {
		MLogger.log(1, this.getClass() + " process_Wms_CountSheet()");
		boolean flag = false;

		WmsTran tran = new WmsUploadItemSheet();
		flag = tran.processWmsTran(map);
		MLogger.log(-1, this.getClass() + " process_Wms_CountSheet()");
		return flag;
	}
	
		
	private boolean process_Wms_ItemSup_CountSheet(Map map) throws Exception {
		MLogger.log(1, this.getClass() + " process_Wms_CountSheet()");
		boolean flag = false;
		
		WmsTranItemSup tran = new WmsUploadItemSheet();
		flag = tran.processWmsTranItemSup(map);
		MLogger.log(-1, this.getClass() + " process_Wms_CountSheet()");
		return flag;
	}
	
	private void onDownloadContactTemplate(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException,Exception {
    	String path = DbBean.DownloadContactTemplate;
    	response.setContentType("application/vnd.ms-excel");
    	response.setHeader("Content-Disposition", "attachment; filename=\"ContactTemplate.xls\"");
    	response.setHeader("cache-control", "no-cache");
    	response.setHeader("Pragma", "no-cache");
    	java.net.URL url = new java.net.URL("file://"+path);
    	File file = new File(path);
    	System.out.println("Path:" + file.getPath());
    	System.out.print("file exist:" + file.exists());
    	java.io.FileInputStream fileInputStream = new java.io.FileInputStream(file);
    	BufferedInputStream bin = new BufferedInputStream(fileInputStream);
    	int i;
    	byte[] b = new byte[10];
    	ServletOutputStream sosStream = response.getOutputStream();
    	while ((i = bin.read()) != -1) {
    		sosStream.write(i);
    	}
    	sosStream.flush();
    	sosStream.close();
    	bin.close();
    	fileInputStream.close();
    }
    
   private void onImportContactCountSheet(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException,Exception {
           String PLANT = (String)request.getSession().getAttribute("PLANT");
           System.out.println("********************* onImportContactCountSheet **************************");
           String result = "";
           ArrayList alRes = new ArrayList();
           try {
                   try {
                	   MultipartRequest mreq = new MultipartRequest(request,DbBean.CountSheetUploadPath, 2048000);
                   } catch (Exception eee) {
                	   throw eee;
                   }
                   request.getSession().setAttribute("IMP_ITEMRESULT", null);
                   // get the sheet name
                   String filename = "";
                   File f = new File(StrFileName);
                   filename = f.getName();
                   f = null;
                   // folder
                   StrFileName = DbBean.CountSheetUploadPath + filename;
                   System.out.println("After conversion Import File  *********:"+ StrFileName);
                   CountSheetDownloaderUtil util = new CountSheetDownloaderUtil();
                   alRes = util.downloadContactSheetData(PLANT, StrFileName, StrSheetName,login_user );
                   if (alRes.size() > 0) {
                           mlogger.info("Data Imported Successfully");
                           result = "<font color=\"green\">Data Imported Successfully,Click on Confirm Buttton to upload data.</font>";
                   } else {
                           throw new Exception("no data found  for given data in the excel");
                   }
           } catch (Exception e) {
                   result = "<font color=\"red\">" + e.getMessage() + "</font>";
           }

           System.out.println("Setting into session ");
           request.getSession().setAttribute("ITEMRESULT", result);
           request.getSession().setAttribute("IMP_ITEMRESULT", alRes);
           response.sendRedirect("../contact/import?ImportFile="+ orgFilePath + "&SheetName=" + StrSheetName + "");
   }
    
    @SuppressWarnings("unchecked")
    private void onConfirmContactCountSheet(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException,Exception {
    	String result = "";
    	UserTransaction ut = null;
    	boolean flag = true;
    	boolean itemUpdated = false;
    	try {
    		ut = com.track.gates.DbBean.getUserTranaction();
    		ut.begin();
    		String PLANT = (String) request.getSession().getAttribute("PLANT");
    		String login_user = (String) request.getSession().getAttribute("LOGIN_USER");
    		ArrayList al = (ArrayList) request.getSession().getAttribute("IMP_ITEMRESULT");
    		Map linemap = new HashMap();
    		PlantMstUtil plantmstutil = new PlantMstUtil();
    		DateUtils dateutils = new DateUtils();
    		
    		String company="",country="",industry="",contact_person="",designation="",tel_no="",contact_no="",how_we_know="",
    				email="",sales_cons="",lifecycle="",lead_status="",sales_prob="",website="",facebook="",linkedln="",notes="";
    		
    		for (int iCnt = 0; iCnt < al.size(); iCnt++) {
    			Map lineArr = (Map) al.get(iCnt);
    			if(flag){
    				company = (String) lineArr.get("COMPANY");
    				country = (String) lineArr.get("COUNTRY");
    				industry = (String) lineArr.get("INDUSTRY");
    				contact_person = (String) lineArr.get("CONTACT_PERSON");
    				designation = (String) lineArr.get("DESIGNATION");
    				tel_no = (String) lineArr.get("TEL_NO");
    				contact_no = (String) lineArr.get("CONTACT_NO");
    				how_we_know = (String) lineArr.get("HOW_WE_KNOW");
    				email = (String) lineArr.get("EMAIL");
    				sales_cons = (String) lineArr.get("SALES_CONS");
    				lifecycle = (String) lineArr.get("LIFECYCLE");
    				lead_status = (String) lineArr.get("LEAD_STATUS");
    				sales_prob = (String) lineArr.get("SALES_PROB");
    				website = (String) lineArr.get("WEBSITE");
    				facebook = (String) lineArr.get("FACEBOOK");
    				linkedln = (String) lineArr.get("LINKEDLN");
    				notes = (String) lineArr.get("NOTES");

    				Hashtable hdr = new Hashtable();
    			 	Hashtable det = new Hashtable();
    			 	
    			    hdr.put(IDBConstants.PLANT,PLANT);
    			    hdr.put("NAME",company);
    			    
    			    if(!(plantmstutil.isExistsContactName(hdr))){
    			         hdr.put(IDBConstants.PLANT,PLANT);
    			         hdr.put("NAME",company);
    			         hdr.put("EMAIL",email); 
    			         hdr.put("COUNTRY",country); 
    			         hdr.put("COMPANYCONTACT",contact_person); 
    			         hdr.put("JOB",designation); 
    			         hdr.put("PHONENUMBER",tel_no); 
    			         hdr.put("LEADSTATUS",lead_status); 
    			         hdr.put("MOBILENO",contact_no); 
    			         hdr.put("HOWWEKNOW",how_we_know); 
    			         hdr.put("USER_NAME",sales_cons); 
    			         hdr.put("INDUSTRY",industry); 
    			         hdr.put("WEBSITE",website); 
    			         hdr.put("FACEBOOK",facebook); 
    			         hdr.put("LINKED",linkedln); 
    			         hdr.put("SALESPROBABILITY",sales_prob); 
    			         hdr.put("CRBY",login_user);hdr.put("CRAT",dateutils.getDateTime());
    			         hdr.put("UPBY",login_user);hdr.put("UPAT",dateutils.getDateTime());  
    			         
    			         int contacthdrID = plantmstutil.insertContactHdr(hdr,PLANT);
    			         
//						if (contacthdrID > 0) {
//							int attchSize = ContactAttachmentList.size();
//							for (int i = 0; i < attchSize; i++) {
//								Hashtable contactAttachmentat = new Hashtable<String, String>();
//								contactAttachmentat = ContactAttachmentList.get(i);
//								contactAttachmentat.put("CONTACTHDRID", String.valueOf(contacthdrID));
//								contactAttachmentInfoList.add(contactAttachmentat);
//							}
//							if (contactAttachmentInfoList.size() > 0)
//								contactAttachDAO.addcontactAttachments(contactAttachmentInfoList, plant);
//						}
    			  			
							MovHisDAO mdao = new MovHisDAO(PLANT);
							mdao.setmLogger(mLogger);
							Hashtable htm = new Hashtable();
							htm.put(IDBConstants.PLANT, PLANT);
							htm.put(IDBConstants.DIRTYPE, TransactionConstants.ADD_CONTACT);
							htm.put("RECID", "");
							htm.put("ITEM", String.valueOf(contacthdrID));
							htm.put(IDBConstants.CREATED_BY, login_user);
							htm.put(IDBConstants.CREATED_AT, dateutils.getDateTime());
							htm.put(IDBConstants.TRAN_DATE, dateutils.getDateinyyyy_mm_dd(dateutils.getDate()));
    			            if(!notes.equals(""))
    			            	htm.put(IDBConstants.REMARKS, company+","+notes);
    			            else
    			            	htm.put(IDBConstants.REMARKS, company);
    			            
    			            boolean  inserted = mdao.insertIntoMovHis(htm);
    			            
    			            det.put(IDBConstants.PLANT,PLANT); 
    			            det.put("LEADDATE",DateUtils.getDate()); 
    			            det.put("LIFECYCLESTAGE",lifecycle); 
    			            det.put("LEADSTATUS",lead_status); 
    			            det.put("NOTE",notes); 
    			            det.put("CONTACTHDRID",Integer.toString(contacthdrID)); 
    			            det.put("UPAT",dateutils.getDateTime()); det.put("CRAT",dateutils.getDateTime());
    			            det.put("UPBY",login_user);   det.put("CRBY",login_user);
    			            boolean ContactDetInserted = plantmstutil.insertContactDet(det);
    			            
    			            if (contacthdrID > 0) 
    			            	itemUpdated = true;
    			  			
    			  			
    			     }
    				
    				if(itemUpdated)
    				flag = true;
    			}
    		}
    		
    		if (flag == true) {
    			DbBean.CommitTran(ut);
    			request.getSession().removeAttribute("IMP_ITEMRESULT");
    			flag = true;
    			result = "<font color=\"green\">Contacts Confirmed Successfully</font>";
    		} else {
    			DbBean.RollbackTran(ut);
    			flag = false;
    			result = "<font color=\"red\">Error in Confirming Contacts </font>";
    		}
    		
    	} catch (Exception e) {
    		flag = false;
    		DbBean.RollbackTran(ut);
    		request.getSession().setAttribute("ITEMRESULT", result);
    		result = "<font color=\"red\">"+ e.getMessage() +"</font>";
    	}
    	request.getSession().setAttribute("ITEMRESULT", result);
    	response.sendRedirect("../contact/import?");
    }
	
	
	private void onDownloadMinMaxTemplate(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException,Exception {
    	String path = DbBean.DownloadMinMaxTemplate;
    	response.setContentType("application/vnd.ms-excel");
    	response.setHeader("Content-Disposition", "attachment; filename=\"ProductMinMaxTemplate.xls\"");
    	response.setHeader("cache-control", "no-cache");
    	response.setHeader("Pragma", "no-cache");
    	java.net.URL url = new java.net.URL("file://"+path);
    	File file = new File(path);
    	System.out.println("Path:" + file.getPath());
    	System.out.print("file exist:" + file.exists());
    	java.io.FileInputStream fileInputStream = new java.io.FileInputStream(file);
    	BufferedInputStream bin = new BufferedInputStream(fileInputStream);
    	int i;
    	byte[] b = new byte[10];
    	ServletOutputStream sosStream = response.getOutputStream();
    	while ((i = bin.read()) != -1) {
    		sosStream.write(i);
    	}
    	sosStream.flush();
    	sosStream.close();
    	bin.close();
    	fileInputStream.close();
    }
	
	
	private void onDownloadOutletMinMaxTemplate(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException,Exception {
		String path = DbBean.DownloadOutletMinMaxTemplate;
		response.setContentType("application/vnd.ms-excel");
		response.setHeader("Content-Disposition", "attachment; filename=\"ProductOutletMinMaxTemplate.xls\"");
		response.setHeader("cache-control", "no-cache");
		response.setHeader("Pragma", "no-cache");
		java.net.URL url = new java.net.URL("file://"+path);
		File file = new File(path);
		System.out.println("Path:" + file.getPath());
		System.out.print("file exist:" + file.exists());
		java.io.FileInputStream fileInputStream = new java.io.FileInputStream(file);
		BufferedInputStream bin = new BufferedInputStream(fileInputStream);
		int i;
		byte[] b = new byte[10];
		ServletOutputStream sosStream = response.getOutputStream();
		while ((i = bin.read()) != -1) {
			sosStream.write(i);
		}
		sosStream.flush();
		sosStream.close();
		bin.close();
		fileInputStream.close();
	}
    
   private void onImportMinMaxCountSheet(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException,Exception {
           String PLANT = (String)request.getSession().getAttribute("PLANT");
           System.out.println("********************* onImportMinMaxCountSheet **************************");
           String result = "";
           ArrayList alRes = new ArrayList();
           try {
                   try {
                	   MultipartRequest mreq = new MultipartRequest(request,DbBean.CountSheetUploadPath, 2048000);
                   } catch (Exception eee) {
                	   throw eee;
                   }
                   request.getSession().setAttribute("IMP_ITEMRESULT", null);
                   // get the sheet name
                   String filename = "";
                   File f = new File(StrFileName);
                   filename = f.getName();
                   f = null;
                   // folder
                   StrFileName = DbBean.CountSheetUploadPath + filename;
                   System.out.println("After conversion Import File  *********:"+ StrFileName);
                   CountSheetDownloaderUtil util = new CountSheetDownloaderUtil();
                   alRes = util.downloadMinMaxSheetData(PLANT, StrFileName, StrSheetName );
                   if (alRes.size() > 0) {
                           mlogger.info("Data Imported Successfully");
                           result = "<font color=\"green\">Data Imported Successfully,Click on Confirm Buttton to upload data.</font>";
                   } else {
                           throw new Exception("no data found  for given data in the excel");
                   }
           } catch (Exception e) {
                   result = "<font color=\"red\">" + e.getMessage() + "</font>";
           }

           System.out.println("Setting into session ");
           request.getSession().setAttribute("ITEMRESULT", result);
           request.getSession().setAttribute("IMP_ITEMRESULT", alRes);
           response.sendRedirect("../product/importminmax?ImportFile="+ orgFilePath + "&SheetName=" + StrSheetName + "");
   }
   
   private void onImportOutletMinMaxCountSheet(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException,Exception {
	   String PLANT = (String)request.getSession().getAttribute("PLANT");
	   System.out.println("********************* onImportOutletMinMaxCountSheet **************************");
	   String result = "";
	   ArrayList alRes = new ArrayList();
	   try {
		   try {
			   MultipartRequest mreq = new MultipartRequest(request,DbBean.CountSheetUploadPath, 2048000);
		   } catch (Exception eee) {
			   throw eee;
		   }
		   request.getSession().setAttribute("IMP_ITEMRESULT", null);
		   // get the sheet name
		   String filename = "";
		   File f = new File(StrFileName);
		   filename = f.getName();
		   f = null;
		   // folder
		   StrFileName = DbBean.CountSheetUploadPath + filename;
		   System.out.println("After conversion Import File  *********:"+ StrFileName);
		   CountSheetDownloaderUtil util = new CountSheetDownloaderUtil();
		   alRes = util.downloadOutletMinMaxSheetData(PLANT, StrFileName, StrSheetName );
		   if (alRes.size() > 0) {
			   mlogger.info("Data Imported Successfully");
			   result = "<font color=\"green\">Data Imported Successfully,Click on Confirm Buttton to upload data.</font>";
		   } else {
			   throw new Exception("no data found  for given data in the excel");
		   }
	   } catch (Exception e) {
		   result = "<font color=\"red\">" + e.getMessage() + "</font>";
	   }
	   
	   System.out.println("Setting into session ");
	   request.getSession().setAttribute("ITEMRESULT", result);
	   request.getSession().setAttribute("IMP_ITEMRESULT", alRes);
	   response.sendRedirect("../product/importoutletminmax?ImportFile="+ orgFilePath + "&SheetName=" + StrSheetName + "");
   }
    
    @SuppressWarnings("unchecked")
    private void onConfirmMinMaxCountSheet(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException,Exception {
    	String result = "";
    	UserTransaction ut = null;
    	boolean flag = true;
    	boolean itemUpdated = false;
    	try {
    		ut = com.track.gates.DbBean.getUserTranaction();
    		ut.begin();
    		String PLANT = (String) request.getSession().getAttribute("PLANT");
    		String _login_user = (String) request.getSession().getAttribute("LOGIN_USER");
    		ArrayList al = (ArrayList) request.getSession().getAttribute("IMP_ITEMRESULT");
    		Map linemap = new HashMap();
    		ItemUtil itemUtil = new ItemUtil();
    		String Item="",Minqty="",Maxqty="";
    		for (int iCnt = 0; iCnt < al.size(); iCnt++) {
    			Map lineArr = (Map) al.get(iCnt);
    			if(flag){
    				linemap.put("LOGIN_USER", _login_user);
    				linemap.put("PLANT", PLANT);
    				linemap.put(IConstants.ITEM, (String) lineArr.get(IConstants.ITEM));
    				linemap.put(IConstants.STKQTY, (String) lineArr.get(IConstants.STKQTY));
    				linemap.put(IConstants.MAXSTKQTY, (String) lineArr.get(IConstants.MAXSTKQTY));
    				Item = (String) lineArr.get(IConstants.ITEM);
    				Minqty = (String) lineArr.get(IConstants.STKQTY);
    				Maxqty = (String) lineArr.get(IConstants.MAXSTKQTY);
    				if((itemUtil.isExistsItemMst(Item,PLANT)))
    			    {
    					Hashtable htCondition = new Hashtable();
    			        htCondition.put(IConstants.ITEM,Item);
    			        htCondition.put(IConstants.PLANT,PLANT);
    			        
    			        Hashtable htUpdate = new Hashtable();
    			        htUpdate.put(IConstants.STKQTY,Minqty); //stkqty
    			        htUpdate.put(IConstants.MAXSTKQTY,Maxqty); //maxstkqty
    			        itemUpdated = itemUtil.updateItem(htUpdate,htCondition);
    			    }
    				if(itemUpdated)
    				flag = true;
    			}
    		}
    		
    		if (flag == true) {
    			DbBean.CommitTran(ut);
    			request.getSession().removeAttribute("IMP_ITEMRESULT");
    			flag = true;
//    			result = "<font color=\"green\">Min/max  Qty Updated Successfully</font>";
    			result = "<font color=\"green\">Products Min/max  Qty Confirmed Successfully</font>";
    		} else {
    			DbBean.RollbackTran(ut);
    			flag = false;
    			result = "<font color=\"red\">Error in Confirming Products </font>";
    		}
    		
    	} catch (Exception e) {
    		flag = false;
    		DbBean.RollbackTran(ut);
    		request.getSession().setAttribute("ITEMRESULT", result);
    		result = "<font color=\"red\">"+ e.getMessage() +"</font>";
    	}
    	request.getSession().setAttribute("ITEMRESULT", result);
    	response.sendRedirect("../product/importminmax?");
    }
    
    @SuppressWarnings("unchecked")
    private void onConfirmOutletMinMaxCountSheet(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException,Exception {
    	String result = "";
    	UserTransaction ut = null;
    	boolean flag = true;
    	boolean itemUpdated = false;
    	try {
    		ut = com.track.gates.DbBean.getUserTranaction();
    		ut.begin();
    		String PLANT = (String) request.getSession().getAttribute("PLANT");
    		String login_user = (String) request.getSession().getAttribute("LOGIN_USER");
    		ArrayList al = (ArrayList) request.getSession().getAttribute("IMP_ITEMRESULT");
    		DateUtils dateutils = new DateUtils();
    		Map linemap = new HashMap();
    		ItemUtil itemUtil = new ItemUtil();
    		String Item="",Outlet="",Minqty="",Maxqty="";
    		for (int iCnt = 0; iCnt < al.size(); iCnt++) {
    			Map lineArr = (Map) al.get(iCnt);
    			if(flag){
    				linemap.put("LOGIN_USER", login_user);
    				linemap.put("PLANT", PLANT);
    				linemap.put(IConstants.ITEM, (String) lineArr.get(IConstants.ITEM));
    				linemap.put(IConstants.OUTLET, (String) lineArr.get(IConstants.OUTLET));
    				linemap.put(IConstants.STKQTY, (String) lineArr.get(IConstants.STKQTY));
    				linemap.put(IConstants.MAXSTKQTY, (String) lineArr.get(IConstants.MAXSTKQTY));
    				Item = (String) lineArr.get(IConstants.ITEM);
    				Outlet = (String) lineArr.get(IConstants.OUTLET);
    				Minqty = (String) lineArr.get(IConstants.STKQTY);
    				Maxqty = (String) lineArr.get(IConstants.MAXSTKQTY);
    				if((new OutletBeanDAO().isExistsOutletMinmax(Outlet,Item,PLANT)))
    				{
    					Hashtable htCondition = new Hashtable();
    					htCondition.put(IConstants.PLANT,PLANT);
    					htCondition.put(IConstants.ITEM,Item);
    					htCondition.put(IConstants.OUTLET,Outlet);
    					
    					Hashtable htUpdate = new Hashtable();
    					htUpdate.put("MINQTY",Minqty); //stkqty
    					htUpdate.put("MAXQTY",Maxqty); //maxstkqty
    					htUpdate.put("UPBY",login_user);
    					htUpdate.put("UPAT",dateutils.getDateTime());
    					itemUpdated = new OutletBeanDAO().updateOutletMinmax(htUpdate,htCondition);
    				}else {
    					Hashtable ht = new Hashtable();
    					ht.put(IConstants.PLANT,PLANT);
    					ht.put(IConstants.ITEM,Item);
    					ht.put(IConstants.OUTLET,Outlet);
    					ht.put("MINQTY",Minqty);
    					ht.put("MAXQTY",Maxqty);
    					ht.put("CRBY",login_user);
    					ht.put("CRAT",dateutils.getDateTime());
    					itemUpdated = new OutletBeanDAO().insertIntoOutletMinmax(ht);
    				}
    				if(itemUpdated)
    					flag = true;
    			}
    		}
    		
    		if (flag == true) {
    			DbBean.CommitTran(ut);
    			request.getSession().removeAttribute("IMP_ITEMRESULT");
    			flag = true;
//    			result = "<font color=\"green\">Min/max  Qty Updated Successfully</font>";
    			result = "<font color=\"green\">Products Outlet Min/max  Qty Confirmed Successfully</font>";
    		} else {
    			DbBean.RollbackTran(ut);
    			flag = false;
    			result = "<font color=\"red\">Error in Confirming Products </font>";
    		}
    		
    	} catch (Exception e) {
    		flag = false;
    		DbBean.RollbackTran(ut);
    		request.getSession().setAttribute("ITEMRESULT", result);
    		result = "<font color=\"red\">"+ e.getMessage() +"</font>";
    	}
    	request.getSession().setAttribute("ITEMRESULT", result);
    	response.sendRedirect("../product/importoutletminmax?");
    }
	
	private boolean process_Wms_Desc_CountSheet(Map map) throws Exception {
		MLogger.log(1, this.getClass() + " process_Wms_CountSheet()");
		boolean flag = false;
		
		WmsTranDesc tran = new WmsUploadItemSheet();
		flag = tran.processWmsTranDesc(map);
		MLogger.log(-1, this.getClass() + " process_Wms_CountSheet()");
		return flag;
	}
	
	private boolean process_Wms_Img_CountSheet(Map map) throws Exception {
		MLogger.log(1, this.getClass() + " process_Wms_CountSheet()");
		boolean flag = false;
		
		WmsTranImg tran = new WmsUploadItemSheet();
		flag = tran.processWmsTranImg(map);
		MLogger.log(-1, this.getClass() + " process_Wms_CountSheet()");
		return flag;
	}
	
	private boolean process_Wms_Item_CountSheet(Map map) throws Exception {
		MLogger.log(1, this.getClass() + " process_Wms_CountSheet()");
		boolean flag = false;
		
		WmsTranItem tran = new WmsUploadItemSheet();
		flag = tran.processWmsTranItem(map);
		MLogger.log(-1, this.getClass() + " process_Wms_CountSheet()");
		return flag;
	}
        
        
    private void onImportAltrPrdCountSheet(HttpServletRequest request,
                    HttpServletResponse response) throws ServletException, IOException,
                    Exception {
    	
            String PLANT = (String)request.getSession().getAttribute("PLANT");
            System.out.println("********************* onImportAltrPrdCountSheet **************************");
            String result = "";
            ArrayList alRes = new ArrayList();


            try {

                    try {

                            MultipartRequest mreq = new MultipartRequest(request,
                                            DbBean.CountSheetUploadPath, 2048000);
                    } catch (Exception eee) {

                            throw eee;

                    }

                    request.getSession().setAttribute("IMP_ITEMRESULT", null);
                    // get the sheet name
                    String filename = "";
                    File f = new File(StrFileName);
                    filename = f.getName();
                    f = null;

                    // folder
                    StrFileName = DbBean.CountSheetUploadPath + filename;

                    // StrFileName = DbBean.CountSheetUploadPath + filename;
                    System.out.println("After conversion Import File  *********:"
                                    + StrFileName);

                    CountSheetDownloaderUtil util = new CountSheetDownloaderUtil();

                    alRes = util.downloadAltrPrdSheetData(PLANT, StrFileName, StrSheetName );

                    if (alRes.size() > 0) {
                            mlogger.info("Data Imported Successfully");
                            result = "<font color=\"green\">Data Imported Successfully,Click on Confirm Buttton to upload data.</font>";
                    } else {
                            throw new Exception(
                                            "no data found  for given data in the excel");
                    }

            } catch (Exception e) {

                    // throw e;
                    result = "<font color=\"red\">" + e.getMessage() + "</font>";
            }

            System.out.println("Setting into session ");

            request.getSession().setAttribute("ITEMRESULT", result);
            request.getSession().setAttribute("IMP_ITEMRESULT", alRes);
            //       
            response.sendRedirect("../product/importalterprd?ImportFile="
                            + orgFilePath + "&SheetName=" + StrSheetName + "");

    }
    
    private void onDownloadAltrPrdTemplate(HttpServletRequest request,
                    HttpServletResponse response) throws ServletException, IOException,
                    Exception {
            
            String path = DbBean.DownloadAltrPrdTemplate;

            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-Disposition", "attachment; filename=\"AlternateProductTemplate.xls\"");
            response.setHeader("cache-control", "no-cache");
            response.setHeader("Pragma", "no-cache");
            java.net.URL url = new java.net.URL("file://"+path);
            //File file = new File(url.getPath());
            File file = new File(path);
            System.out.println("Path:" + file.getPath());
            System.out.print("file exist:" + file.exists());
            java.io.FileInputStream fileInputStream = new java.io.FileInputStream(file);
            BufferedInputStream bin = new BufferedInputStream(fileInputStream);

            int i;
            byte[] b = new byte[10];
            ServletOutputStream sosStream = response.getOutputStream();
            while ((i = bin.read()) != -1) {
                    sosStream.write(i);
            }

            sosStream.flush();
            sosStream.close();
            bin.close();
            fileInputStream.close();

    }
    
    @SuppressWarnings("unchecked")
    private void onConfirmAltrPrdCountSheet(HttpServletRequest request,
                    HttpServletResponse response) throws ServletException, IOException,
                    Exception {
            String result = "";
            UserTransaction ut = null;
            boolean flag = true;
           
            try {
                    ut = com.track.gates.DbBean.getUserTranaction();
                    ut.begin();
                    String PLANT = (String) request.getSession().getAttribute("PLANT");
                    String _login_user = (String) request.getSession().getAttribute("LOGIN_USER");

                    ArrayList al = (ArrayList) request.getSession().getAttribute("IMP_ITEMRESULT");

                    Map linemap = new HashMap();
                    for (int iCnt = 0; iCnt < al.size(); iCnt++) {
                            Map lineArr = (Map) al.get(iCnt);
                            
                            if(flag){
                                    
                                    linemap.put("LOGIN_USER", _login_user);
                                    linemap.put("PLANT", PLANT);
                                    linemap.put(IConstants.ITEM, (String) lineArr.get(IConstants.ITEM));
                                    linemap.put(IConstants.ALTERNATE_ITEM, (String) lineArr.get(IConstants.ALTERNATE_ITEM));
                                    flag = process_WmsAltrPrd_CountSheet(linemap);
                            }
                            
                    }
            

                    if (flag == true) {
                            DbBean.CommitTran(ut);
                            request.getSession().removeAttribute("IMP_ITEMRESULT");
                            flag = true;
                            result = "<font color=\"green\">Alternate Products Confirmed Successfully</font>";
                    } else {
                            DbBean.RollbackTran(ut);
                            flag = false;
                            result = "<font color=\"red\">Error in Confirming Alternate Products </font>";
                    }

            } catch (Exception e) {
                    flag = false;
                    DbBean.RollbackTran(ut);
                    request.getSession().setAttribute("ITEMRESULT", result);
                    //throw e;
                    result = "<font color=\"red\">"+ e.getMessage() +"</font>";
            }

            request.getSession().setAttribute("ITEMRESULT", result);
            response.sendRedirect("../product/importalterprd?");

    }

    private boolean process_WmsAltrPrd_CountSheet(Map map) throws Exception {
            MLogger.log(1, this.getClass() + " process_WmsAltrPrd_CountSheet()");
            boolean flag = false;

            WmsTran tran = new WmsUploadAlternateProduct();
            flag = tran.processWmsTran(map);
            MLogger.log(-1, this.getClass() + " process_WmsAltrPrd_CountSheet()");
            return flag;
    }
    
	 private HSSFWorkbook writeToExcel(HttpServletRequest request , HttpServletResponse response, HSSFWorkbook wb){
			String plant = "";
			int maxRowsPerSheet = 65535;
		
			try{
				StrUtils strUtils = new StrUtils();
				HttpSession session = request.getSession();
				plant = session.getAttribute("PLANT").toString();
				PlantMstDAO _PlantMstDAO = new PlantMstDAO();
			    String numberOfDecimal = _PlantMstDAO.getNumberOfDecimal(PLANT);
			    String STOCKTYPE = "";
				int SheetNo =1;				 
				 String ITEM     = StrUtils.fString(request.getParameter("ITEM")).trim();
				 String ITEM_DESC = StrUtils.fString(request.getParameter("ITEM_DESC")).trim();
				 String  PRD_CLS_ID =  StrUtils.fString(request.getParameter("PRD_CLS_ID")).trim();
				 String  PRD_DEPT_ID =  StrUtils.fString(request.getParameter("PRD_DEPT_ID")).trim();//Resvi
				 String PRD_TYPE_ID =  StrUtils.fString(request.getParameter("PRD_TYPE_ID")).trim();
				 String PRD_BRAND_ID =  StrUtils.fString(request.getParameter("PRD_BRAND_ID")).trim();
//				 String NONSTKFLAG =  StrUtils.fString(request.getParameter("STOCKTYPE")).trim();
				 STOCKTYPE =  StrUtils.fString(request.getParameter("STOCKTYPE")).trim();
				 //imti start
				 if(STOCKTYPE.equalsIgnoreCase("Stock"))
					 STOCKTYPE="N";
				 else if(STOCKTYPE.equalsIgnoreCase("Non-Stock"))
					 STOCKTYPE="Y";
				 //imti end
//				 //imti start
//				 if(NONSTKFLAG.equalsIgnoreCase("Stock"))
//					 NONSTKFLAG="N";
//				 else if(NONSTKFLAG.equalsIgnoreCase("Non-Stock"))
//					 NONSTKFLAG="Y";
//				 //imti end
				 List listQry = new ItemUtil().queryItemMstForSearchCriteria(ITEM,ITEM_DESC,PRD_CLS_ID,PRD_TYPE_ID,PRD_BRAND_ID,PRD_DEPT_ID,STOCKTYPE,plant,"");
					
					
							HSSFSheet sheet = null ;
							HSSFCellStyle styleHeader = null;
							HSSFCellStyle dataStyle = null;
							dataStyle = createDataStyle(wb);
							 sheet = wb.createSheet("PRODUCT_MST_"+SheetNo);
							 styleHeader = createStyleHeader(wb);
							 sheet = this.createWidth(sheet);
							 sheet = this.createHeader(sheet,styleHeader);
							 int index = 1;
							 if (listQry.size() > 0) {
							 for (int iCnt =0; iCnt<listQry.size(); iCnt++){
								
									   Map lineArr = (Map) listQry.get(iCnt);	
									    int k = 0;
									    
									    String netweight = (String)lineArr.get("NETWEIGHT");
									    String grossweight = (String)lineArr.get("GROSSWEIGHT");
									    String stkQty = (String)lineArr.get("STKQTY");
									    String maxStkQty = (String)lineArr.get("MAXSTKQTY");
									    String unitPriceValue = (String)lineArr.get("UNITPRICE");
									    String costValue = (String)lineArr.get("COST");
									    String minPriceValue = (String)lineArr.get("MINSPRICE");
									    String prodGstValue = (String)lineArr.get("PRODGST");
									   /* String rentalpriceValue = (String)lineArr.get("RENTALPRICE");*/
									    String isbasicuom = (String)lineArr.get("ISBASICUOM");
									    String vendno = (String)lineArr.get("VENDNO");
									    
									    String SupplierDesc="";
									    if(vendno.length()>0) {
					        			Hashtable HM = new Hashtable();
					        		   	HM.put(IConstants.PLANT, plant);
					    				HM.put(IConstants.VENDNO, (String)lineArr.get("VENDNO"));
					    				VendMstDAO vendMstDAO = new VendMstDAO();
					    				SupplierDesc=vendMstDAO.getVendorNameByNo(HM);
									    }
									    
									    if(isbasicuom.equals("0")){
									    	
									    	isbasicuom="N";
									    	
									    }else if(isbasicuom.equals("1")){
									    	isbasicuom="Y";
									    }
									    
									    
									    
									    float netweightVal ="".equals(netweight) ? 0.0f :  Float.parseFloat(netweight);
									    float grossweightVal ="".equals(grossweight) ? 0.0f :  Float.parseFloat(grossweight);
									    float stkQtyVal ="".equals(stkQty) ? 0.0f :  Float.parseFloat(stkQty);
									    float maxStkQtyVal ="".equals(maxStkQty) ? 0.0f :  Float.parseFloat(maxStkQty);
									    float unitPriceVal ="".equals(unitPriceValue) ? 0.0f :  Float.parseFloat(unitPriceValue);
									    float costVal ="".equals(costValue) ? 0.0f :  Float.parseFloat(costValue);
									    float minPriceVal ="".equals(minPriceValue) ? 0.0f :  Float.parseFloat(minPriceValue);
									    float prodGstVal ="".equals(prodGstValue) ? 0.0f :  Float.parseFloat(prodGstValue);
									   /* float rentalpriceVal ="".equals(rentalpriceValue) ? 0.0f :  Float.parseFloat(rentalpriceValue);*/
									    
							    		
							    		if(netweightVal==0f){
							    			netweight="0.000";
							    		}else{
							    			netweight=netweight.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
							    		}if(grossweightVal==0f){
							    			grossweight="0.000";
							    		}else{
							    			grossweight=grossweight.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
							    		}if(maxStkQtyVal==0f){
							    			maxStkQty="0.000";
							    		}else{
							    			maxStkQty=maxStkQty.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
							    		}if(unitPriceVal==0f){
							    			unitPriceValue="0.00000";
							    		}else{
							    			unitPriceValue=unitPriceValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
							    		}if(costVal==0f){
							    			costValue="0.00000";
							    		}else{
							    			costValue=costValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
							    		}if(minPriceVal==0f){
							    			minPriceValue="0.00000";
							    		}else{
							    			minPriceValue=minPriceValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
							    		}if(prodGstVal==0f){
							    			prodGstValue="0.000";
							    		}else{
							    			prodGstValue=prodGstValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
							    		}if(stkQtyVal==0f){
							    			stkQty="0.000";
							    		}else{
							    			stkQty=stkQty.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
							    		}/*if(rentalpriceVal==0f){
							    			rentalpriceValue="0.000";
							    		}else{
							    			rentalpriceValue=rentalpriceValue.replaceAll("\\.0*$|(?<=\\.[0-9]{0,2147483646})0*$", "");
							    		}*/
							    		
									    
							    		/**/
							    		double dUnitPriceVal = Double.parseDouble(unitPriceValue);
							    		unitPriceValue = StrUtils.addZeroes(dUnitPriceVal, numberOfDecimal);
							    		
							    		double dCostVal = Double.parseDouble(costValue);
							    		costValue = StrUtils.addZeroes(dCostVal, numberOfDecimal);
							    		
							    		double dMinPriceVal = Double.parseDouble(minPriceValue);
							    		minPriceValue = StrUtils.addZeroes(dMinPriceVal, numberOfDecimal);
							            /**/
									    
									    HSSFRow row = sheet.createRow(index);
									  
									    HSSFCell cell = row.createCell((short) k++);
									    cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("ITEM"))));
										cell.setCellStyle(dataStyle);
										
										
										cell = row.createCell((short) k++);
										cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("ITEMDESC"))));
										cell.setCellStyle(dataStyle);
										
										cell = row.createCell((short) k++);
										cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("REMARK1"))));
										cell.setCellStyle(dataStyle);
										
										cell = row.createCell((short) k++);
										cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("STKUOM"))));
										cell.setCellStyle(dataStyle);
										
										cell = row.createCell((short) k++);
										cell.setCellValue(new HSSFRichTextString(isbasicuom));
										cell.setCellStyle(dataStyle);

										cell = row.createCell((short) k++);
										cell.setCellValue(new HSSFRichTextString(SupplierDesc));
										cell.setCellStyle(dataStyle);
										
										cell = row.createCell((short) k++);
										cell.setCellValue(new HSSFRichTextString(netweight));
										cell.setCellStyle(dataStyle);
										
										cell = row.createCell((short) k++);
										cell.setCellValue(new HSSFRichTextString(grossweight));
										cell.setCellStyle(dataStyle);
										
										cell = row.createCell((short) k++);
										cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("DIMENSION"))));
										cell.setCellStyle(dataStyle);
										
										cell = row.createCell((short) k++);
										cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("PRD_DEPT_ID"))));
										cell.setCellStyle(dataStyle);
										
										cell = row.createCell((short) k++);
										cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("PRD_CLS_ID"))));
										cell.setCellStyle(dataStyle);
										
										cell = row.createCell((short) k++);
										cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("ITEMTYPE"))));
										cell.setCellStyle(dataStyle);
										
										cell = row.createCell((short) k++);
										cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("PRD_BRAND_ID"))));
										cell.setCellStyle(dataStyle);
										
										cell = row.createCell((short) k++);
										cell.setCellValue(prodGstValue);
										cell.setCellStyle(dataStyle);
																																																	
										cell = row.createCell((short) k++);
										cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("HSCODE"))));
										cell.setCellStyle(dataStyle);
										
										cell = row.createCell((short) k++);
										cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("COO"))));
										cell.setCellStyle(dataStyle);
										
										cell = row.createCell((short) k++);
										cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("VINNO"))));
										cell.setCellStyle(dataStyle);
										
										cell = row.createCell((short) k++);
										cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("MODEL"))));
										cell.setCellStyle(dataStyle);
										
										cell = row.createCell((short) k++);
										cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("REMARK3"))));
										cell.setCellStyle(dataStyle);
										
										cell = row.createCell((short) k++);
										cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("REMARK4"))));
										cell.setCellStyle(dataStyle);
										
										cell = row.createCell((short) k++);
										cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("PURCHASEUOM"))));
										cell.setCellStyle(dataStyle);
										
										cell = row.createCell((short) k++);
										cell.setCellValue(costValue);
										cell.setCellStyle(dataStyle);
										
										cell = row.createCell((short) k++);
										cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("SALESUOM"))));
										cell.setCellStyle(dataStyle);
										
									    cell = row.createCell((short) k++);
									    cell.setCellValue(unitPriceValue);
										cell.setCellStyle(dataStyle);
										
										cell = row.createCell((short) k++);
										cell.setCellValue(minPriceValue);
										cell.setCellStyle(dataStyle);
										
									  /*cell = row.createCell((short) k++);
										cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("RENTALUOM"))));
										cell.setCellStyle(dataStyle);
										
										cell = row.createCell((short) k++);
										cell.setCellValue(rentalpriceValue);
										cell.setCellStyle(dataStyle);*/
										
										cell = row.createCell((short) k++);
										cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("INVENTORYUOM"))));
										cell.setCellStyle(dataStyle);
										
										cell = row.createCell((short) k++);
										cell.setCellValue(stkQty);
										cell.setCellStyle(dataStyle);

										cell = row.createCell((short) k++);
										cell.setCellValue(maxStkQty);
										cell.setCellStyle(dataStyle);
										
										/*cell = row.createCell((short) k++);
										cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("REMARK2"))));
										cell.setCellStyle(dataStyle);*/
																		
										/*cell = row.createCell((short) k++);
										cell.setCellValue(Double.parseDouble(StrUtils.fString((String)lineArr.get("DISCOUNT"))));
										cell.setCellStyle(dataStyle);*/
																												
								/*		cell = row.createCell((short) k++);
										cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("USERFLD1"))));
										cell.setCellStyle(dataStyle);*/
										
										cell = row.createCell((short) k++);
										cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("ISACTIVE"))));
										cell.setCellStyle(dataStyle);
										
										cell = row.createCell((short) k++);
										cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("NONSTKFLAG"))));
										cell.setCellStyle(dataStyle);									
										
										
										 index++;
										 if((index-1)%maxRowsPerSheet==0){
											 index = 1;
											 SheetNo++;
											 sheet = wb.createSheet("PRODUCT_MST_"+SheetNo);
											 styleHeader = createStyleHeader(wb);
											 sheet = this.createWidth(sheet);
											 sheet = this.createHeader(sheet,styleHeader);
											 
										 }
										

								  }
							 listQry = new ItemUtil().queryAlternateItemMstList(ITEM,ITEM_DESC,PRD_CLS_ID,PRD_TYPE_ID,PRD_TYPE_ID,PRD_DEPT_ID,plant,"");
							 sheet = wb.createSheet("ALTERNATE_PRODUCT");
							 styleHeader = createStyleHeader(wb);
							 sheet.setColumnWidth((short)0 ,(short)5500);
							 sheet.setColumnWidth((short)1 ,(short)8000);
							 HSSFRow rowhead = sheet.createRow((short) 0);
							HSSFCell cell = rowhead.createCell((short) 0);
							cell.setCellValue(new HSSFRichTextString("Product ID"));
							cell.setCellStyle(styleHeader);
							
							cell = rowhead.createCell((short) 1);
							cell.setCellValue(new HSSFRichTextString("Alternate Product"));
							cell.setCellStyle(styleHeader);
								
								
								
							 index = 1;
							 for (int iCnt =0; iCnt<listQry.size(); iCnt++){
								
									   Map lineArr = (Map) listQry.get(iCnt);	
									    int k = 0;
									    
									    HSSFRow row = sheet.createRow(index);
									  
									    HSSFCell cell1 = row.createCell((short) k++);
									    cell1.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("ITEM"))));
									    cell1.setCellStyle(dataStyle);
										
										
									    cell1 = row.createCell((short) k++);
									    cell1.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("ALTERNATE_ITEM_NAME"))));
									    cell1.setCellStyle(dataStyle);
							 }
					 }
					 else if (listQry.size() < 1) {		
						

							System.out.println("No Records Found To List");
						}
			}catch(Exception e){
				this.mLogger.exception(this.printLog, "", e);
			}
			
			return wb;
		}
private HSSFWorkbook writeToExcelAlternateItem(HttpServletRequest request , HttpServletResponse response, HSSFWorkbook wb){
			String plant = "";
			int maxRowsPerSheet = 65535;
		
			try{
				StrUtils strUtils = new StrUtils();
				HttpSession session = request.getSession();
				plant = session.getAttribute("PLANT").toString();
				int SheetNo =1;				 
				 String ITEM     = StrUtils.fString(request.getParameter("ITEM")).trim();
				 String ITEM_DESC = StrUtils.fString(request.getParameter("ITEM_DESC")).trim();
				 String  PRD_CLS_ID =  StrUtils.fString(request.getParameter("PRD_CLS_ID")).trim();
				 String PRD_TYPE_ID =  StrUtils.fString(request.getParameter("PRD_TYPE_ID")).trim();
				 String PRD_BRAND_ID =  StrUtils.fString(request.getParameter("PRD_BRAND_ID")).trim();
				 String PRD_DEPT_ID =  StrUtils.fString(request.getParameter("PRD_DEPT_ID")).trim();
				 List listQry = new ItemUtil().queryAlternateItemMstForSearchCriteria(ITEM,ITEM_DESC,PRD_CLS_ID,PRD_TYPE_ID,PRD_BRAND_ID,PRD_DEPT_ID,plant," ORDER BY ITEM");
					
					
							HSSFSheet sheet = null ;
							HSSFCellStyle styleHeader = null;
							HSSFCellStyle dataStyle = null;
							dataStyle = createDataStyle(wb);
							 sheet = wb.createSheet("PRODUCT_MST_"+SheetNo);
							 styleHeader = createStyleHeader(wb);
							 sheet = this.createWidth(sheet);
							 sheet = this.createAlternateStyleHeader(sheet,styleHeader);
							 int index = 1;
							 if (listQry.size() > 0) {
							 for (int iCnt =0; iCnt<listQry.size(); iCnt++){
								
									   Map lineArr = (Map) listQry.get(iCnt);	
									    int k = 0;
									    
									    HSSFRow row = sheet.createRow(index);
									  
									    HSSFCell cell = row.createCell((short) k++);
									    cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("ITEM"))));
										cell.setCellStyle(dataStyle);
										
										
										cell = row.createCell((short) k++);
										cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("ITEMDESC"))));
										cell.setCellStyle(dataStyle);
										
										
									    cell = row.createCell((short) k++);									    
									    cell.setCellValue(new HSSFRichTextString(StrUtils.fString((String)lineArr.get("ALTERNATEITEM"))));
										cell.setCellStyle(dataStyle);
										
										
										
										
										
										 index++;
										
										

								  }
							 
					 }
					 else if (listQry.size() < 1) {		
						

							System.out.println("No Records Found To List");
						}
			}catch(Exception e){
				this.mLogger.exception(this.printLog, "", e);
			}
			
			return wb;
		}
	 
	 private HSSFSheet createAlternateStyleHeader(HSSFSheet sheet, HSSFCellStyle styleHeader){
			int k = 0;
			try{
			
			
			HSSFRow rowhead = sheet.createRow((short) 0);			
			
			HSSFCell cell = rowhead.createCell((short) k++);			
			cell.setCellValue(new HSSFRichTextString("Product ID"));
			cell.setCellStyle(styleHeader);
			
			cell = rowhead.createCell((short) k++);
			cell.setCellValue(new HSSFRichTextString("Product Description"));
			cell.setCellStyle(styleHeader);
			
			cell = rowhead.createCell((short) k++);
			cell.setCellValue(new HSSFRichTextString("Alternate Product Barcode"));
			cell.setCellStyle(styleHeader);
			
			
			
			
			
			
			}
			catch(Exception e){
				this.mLogger.exception(this.printLog, "", e);
			}
			return sheet;
		}
	 
	 private HSSFSheet createHeader(HSSFSheet sheet, HSSFCellStyle styleHeader){
			int k = 0;
			try{
			
			
			HSSFRow rowhead = sheet.createRow((short) 0);
			HSSFCell cell = rowhead.createCell((short) k++);
			cell.setCellValue(new HSSFRichTextString("Product ID"));
			cell.setCellStyle(styleHeader);
			
			cell = rowhead.createCell((short) k++);
			cell.setCellValue(new HSSFRichTextString("Product Description"));
			cell.setCellStyle(styleHeader);
			
			cell = rowhead.createCell((short) k++);
			cell.setCellValue(new HSSFRichTextString("Detail Description"));
			cell.setCellStyle(styleHeader);
			
			cell = rowhead.createCell((short) k++);
			cell.setCellValue(new HSSFRichTextString("Base UOM"));
			cell.setCellStyle(styleHeader);
			
			cell = rowhead.createCell((short) k++);
			cell.setCellValue(new HSSFRichTextString("Apply to all UOM (Y or N)"));
			cell.setCellStyle(styleHeader);			

			cell = rowhead.createCell((short) k++);
			cell.setCellValue(new HSSFRichTextString("Supplier"));
			cell.setCellStyle(styleHeader);
			
			cell = rowhead.createCell((short) k++);
			cell.setCellValue(new HSSFRichTextString("Net Weight"));
			cell.setCellStyle(styleHeader);
			
			cell = rowhead.createCell((short) k++);
			cell.setCellValue(new HSSFRichTextString("Gross Weight"));
			cell.setCellStyle(styleHeader);
			
			cell = rowhead.createCell((short) k++);
			cell.setCellValue(new HSSFRichTextString("Dimension"));
			cell.setCellStyle(styleHeader);

			cell = rowhead.createCell((short) k++);
			cell.setCellValue(new HSSFRichTextString("Product Department"));
			cell.setCellStyle(styleHeader);
			
			cell = rowhead.createCell((short) k++);
			cell.setCellValue(new HSSFRichTextString("Product Category"));
			cell.setCellStyle(styleHeader);
			
			cell = rowhead.createCell((short) k++);
			cell.setCellValue(new HSSFRichTextString("Product Sub Category"));
			cell.setCellStyle(styleHeader);
			
			cell = rowhead.createCell((short) k++);
			cell.setCellValue(new HSSFRichTextString("Product Brand"));
			cell.setCellStyle(styleHeader);
			
			cell = rowhead.createCell((short) k++);
			cell.setCellValue(new HSSFRichTextString("VAT/GST"));
			cell.setCellStyle(styleHeader);
			
			cell = rowhead.createCell((short) k++);
			cell.setCellValue(new HSSFRichTextString("HS Code"));
			cell.setCellStyle(styleHeader);
			
			cell = rowhead.createCell((short) k++);
			cell.setCellValue(new HSSFRichTextString("COO"));
			cell.setCellStyle(styleHeader);
			
			cell = rowhead.createCell((short) k++);
			cell.setCellValue(new HSSFRichTextString("VIN No"));
			cell.setCellStyle(styleHeader);
			
			cell = rowhead.createCell((short) k++);
			cell.setCellValue(new HSSFRichTextString("Model"));
			cell.setCellStyle(styleHeader);
			
			cell = rowhead.createCell((short) k++);
			cell.setCellValue(new HSSFRichTextString("Remarks1"));
			cell.setCellStyle(styleHeader);
			
			cell = rowhead.createCell((short) k++);
			cell.setCellValue(new HSSFRichTextString("Remarks2"));
			cell.setCellStyle(styleHeader);

			cell = rowhead.createCell((short) k++);
			cell.setCellValue(new HSSFRichTextString("Purchase UOM"));
			cell.setCellStyle(styleHeader);
			
			cell = rowhead.createCell((short) k++);
			cell.setCellValue(new HSSFRichTextString("Cost"));
			cell.setCellStyle(styleHeader);
			
			cell = rowhead.createCell((short) k++);
			cell.setCellValue(new HSSFRichTextString("Sales UOM"));
			cell.setCellStyle(styleHeader);
			
			cell = rowhead.createCell((short) k++);
			cell.setCellValue(new HSSFRichTextString("List Price"));
			cell.setCellStyle(styleHeader);
			
			cell = rowhead.createCell((short) k++);
			cell.setCellValue(new HSSFRichTextString("Min Selling Price"));
			cell.setCellStyle(styleHeader);
			
/*			cell = rowhead.createCell((short) k++);
			cell.setCellValue(new HSSFRichTextString("Rental UOM"));
			cell.setCellStyle(styleHeader);
			
			cell = rowhead.createCell((short) k++);
			cell.setCellValue(new HSSFRichTextString("Rental Price"));
			cell.setCellStyle(styleHeader);*/
			
			cell = rowhead.createCell((short) k++);
			cell.setCellValue(new HSSFRichTextString("Inventory UOM"));
			cell.setCellStyle(styleHeader);
			
			cell = rowhead.createCell((short) k++);
			cell.setCellValue(new HSSFRichTextString("Min Stock Qty"));
			cell.setCellStyle(styleHeader);

			cell = rowhead.createCell((short) k++);
			cell.setCellValue(new HSSFRichTextString("Max Stock Qty"));
			cell.setCellStyle(styleHeader);
			
			/*cell = rowhead.createCell((short) k++);
			cell.setCellValue(new HSSFRichTextString("Manufacturer"));
			cell.setCellStyle(styleHeader);*/
						
			/*cell = rowhead.createCell((short) k++);
			cell.setCellValue(new HSSFRichTextString("Discount POS(%)"));
			cell.setCellStyle(styleHeader);*/
		
/*			cell = rowhead.createCell((short) k++);
			cell.setCellValue(new HSSFRichTextString("Is Kitting(K/N)?"));
			cell.setCellStyle(styleHeader);*/
			
			cell = rowhead.createCell((short) k++);
			cell.setCellValue(new HSSFRichTextString("Is Active(Y/N)?"));
			cell.setCellStyle(styleHeader);
			
			cell = rowhead.createCell((short) k++);
			cell.setCellValue(new HSSFRichTextString("Is Non-Stock(Y/N)"));
			cell.setCellStyle(styleHeader);
			
			
			
			}
			catch(Exception e){
				this.mLogger.exception(this.printLog, "", e);
			}
			return sheet;
		}

		private HSSFSheet createWidth(HSSFSheet sheet){
			
			try{
				sheet.setColumnWidth((short)0 ,(short)5500);
				sheet.setColumnWidth((short)1 ,(short)8000);
				sheet.setColumnWidth((short)2 ,(short)5500);
				sheet.setColumnWidth((short)3 ,(short)5500);
				sheet.setColumnWidth((short)4 ,(short)5500);
				sheet.setColumnWidth((short)5 ,(short)5500);
				sheet.setColumnWidth((short)6 ,(short)3500);
				sheet.setColumnWidth((short)7 ,(short)3500);
				sheet.setColumnWidth((short)8 ,(short)3500);
				sheet.setColumnWidth((short)9 ,(short)3500);
				sheet.setColumnWidth((short)10 ,(short)3500);
				sheet.setColumnWidth((short)11 ,(short)3500);
				sheet.setColumnWidth((short)12 ,(short)3500);
				sheet.setColumnWidth((short)13 ,(short)3500);
				sheet.setColumnWidth((short)14 ,(short)3500);
				sheet.setColumnWidth((short)15 ,(short)3500);
				sheet.setColumnWidth((short)16 ,(short)3500);
				sheet.setColumnWidth((short)17 ,(short)3500);
				sheet.setColumnWidth((short)18 ,(short)3500);
				sheet.setColumnWidth((short)19 ,(short)3500);
				sheet.setColumnWidth((short)20 ,(short)3500);
				sheet.setColumnWidth((short)21 ,(short)3500);
				sheet.setColumnWidth((short)22 ,(short)3500);
				sheet.setColumnWidth((short)23 ,(short)3500);
				sheet.setColumnWidth((short)24,(short)3500);
				sheet.setColumnWidth((short)25 ,(short)3500);
				sheet.setColumnWidth((short)26 ,(short)4000);
				sheet.setColumnWidth((short)27 ,(short)4000);
				sheet.setColumnWidth((short)28 ,(short)4000);
				sheet.setColumnWidth((short)28 ,(short)4000);
				sheet.setColumnWidth((short)29 ,(short)4000);
				sheet.setColumnWidth((short)30 ,(short)4000);
				/*sheet.setColumnWidth((short)23,(short)2000);*/
				
				
			}
			catch(Exception e){
				this.mLogger.exception(this.printLog, "", e);
			}
			return sheet;
		}
		
		private HSSFCellStyle createStyleHeader(HSSFWorkbook wb){
			
			//Create style
			 HSSFCellStyle styleHeader = wb.createCellStyle();
			  HSSFFont fontHeader  = wb.createFont();
			  fontHeader.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			  fontHeader.setFontName("Arial");	
			  styleHeader.setFont(fontHeader);
			  styleHeader.setFillForegroundColor(HSSFColor.GREY_40_PERCENT.index);
			  styleHeader.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
			  styleHeader.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			  styleHeader.setWrapText(true);
			  return styleHeader;
		}
		
	    private HSSFCellStyle createDataStyle(HSSFWorkbook wb){
			
			//Create style
			  HSSFCellStyle dataStyle = wb.createCellStyle();
			  dataStyle.setWrapText(true);
			  return dataStyle;
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
