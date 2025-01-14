package com.track.servlet;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.UserTransaction;

import com.track.constants.IConstants;
import com.track.constants.IDBConstants;
import com.track.db.util.CountSheetDownloaderUtil;
import com.track.gates.DbBean;
import com.track.tran.WmsTran;
import com.track.tran.WmsUploadItemSheet;
import com.track.util.DateUtils;
import com.track.util.MLogger;
import com.track.util.StrUtils;
import com.oreilly.servlet.MultipartRequest;

/**
 * Servlet implementation class OutboundOrderServlet
 */
public class OutboundOrderServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
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

	public OutboundOrderServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		action = StrUtils.fString(request.getParameter("action")).trim();

		StrFileName = StrUtils.fString(request.getParameter("ImportFile"));
		StrSheetName = StrUtils.fString(request.getParameter("SheetName"));
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
			} else if (action.equalsIgnoreCase("downloadOutboundTemplate")) {
				onDownloadOutboundTemplate(request, response);
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
		// TODO Auto-generated method stub
		response.setContentType(CONTENT_TYPE);
		doGet(request, response);
	}

	private void onImportCountSheet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException,
			Exception {
		String PLANT = (String) request.getSession().getAttribute("PLANT");
		String NOOFINVENTORY = (String) request.getSession().getAttribute("NOOFINVENTORY");
		System.out
				.println("********************* onImportCountSheet **************************");
		String result = "";
		ArrayList alRes = new ArrayList();

		// MLogger.info("CS Upload path : " + DbBean.CountSheetUploadPath);

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

			alRes = util.downloadItemSheetData(PLANT, StrFileName, StrSheetName,"",NOOFINVENTORY);

			if (alRes.size() > 0) {
				mlogger.info("Data Imported Successfully");
				result = "<font color=\"green\">Data Imported Successfully,Click on <b>Confirm Buttton</b> to upload data.</font>";
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
		response.sendRedirect("jsp/importItemExcelSheet.jsp?ImportFile="
				+ orgFilePath + "&SheetName=" + StrSheetName + "");

	}

	private void onDownloadOutboundTemplate(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException,
			Exception {

		String path = DbBean.DownloadOutBoundTemplate;

		response.setContentType("application/vnd.ms-excel");
		response.setHeader("Content-Disposition",
				"attachment; filename=\"OutboundOrderData.xls\"");
		response.setHeader("cache-control", "no-cache");
		response.setHeader("Pragma", "no-cache");
		java.net.URL url = new java.net.URL("file://" + path);
		// File file = new File(url.getPath());
		File file = new File(path);
		System.out.println("Path:" + file.getPath());
		System.out.print("file exist:" + file.exists());
		java.io.FileInputStream fileInputStream = new java.io.FileInputStream(
				file);
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

	private void onConfirmCountSheet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException,
			Exception {
		String result = "";
		ArrayList alRes = new ArrayList();

		UserTransaction ut = null;
		boolean flag = false;
		boolean errflg = false;
		try {
			ut = com.track.gates.DbBean.getUserTranaction();
			ut.begin();
			String PLANT = (String) request.getSession().getAttribute("PLANT");
			String _login_user = (String) request.getSession().getAttribute(
					"LOGIN_USER");

			ArrayList al = (ArrayList) request.getSession().getAttribute(
					"IMP_ITEMRESULT");
			if (al.size() > 0) {
				// System.out.println("Data Imported Successfully in confirm Item");
				for (int i = 0; i < al.size(); i++) {
					Map m = (Map) al.get(i);
					String item = (String) m.get("ITEM");
					String itemdesc = (String) m.get("ITEMDESC");
					String uom = (String) m.get("STKUOM");
					if (item == null || item.equalsIgnoreCase("null")) {
						errflg = true;
						break;
					} else if (itemdesc == null
							|| itemdesc.equalsIgnoreCase("null")) {
						errflg = true;
						break;
					} else if (uom == null || uom.equalsIgnoreCase("null")) {
						errflg = true;
						break;
					}

				}
				if (errflg == true) {
					al.clear();
					result = "<font color=\"red\">Error in uploading data from Excel</font>";
					request.getSession().setAttribute("ITEMRESULT", result);

					response.sendRedirect("jsp/importItemExcelSheet.jsp?");
				}
			}

			Map linemap = new HashMap();
			for (int iCnt = 0; iCnt < al.size(); iCnt++) {
				Map lineArr = (Map) al.get(iCnt);
				linemap.put("LOGIN_USER", _login_user);
				linemap.put("PLANT", PLANT);

				linemap.put(IConstants.ITEM, (String) lineArr
						.get(IConstants.ITEM));
				linemap.put(IConstants.ITEM_DESC, (String) lineArr
						.get(IConstants.ITEM_DESC));
				linemap.put(IConstants.PRICE, (String) lineArr
						.get(IConstants.PRICE));
				linemap.put(IConstants.COST, (String) lineArr
						.get(IConstants.COST));
				linemap.put(IConstants.DISCOUNT, (String) lineArr
						.get(IConstants.DISCOUNT));
				linemap.put(IConstants.STKQTY, (String) lineArr
						.get(IConstants.STKQTY));
				linemap.put(IDBConstants.PRDCLSID, (String) lineArr
						.get(IDBConstants.PRDCLSID));
				linemap.put(IDBConstants.ITEMMST_ITEM_TYPE, (String) lineArr
						.get(IDBConstants.ITEMMST_ITEM_TYPE));
				linemap.put(IConstants.STKUOM, (String) lineArr
						.get(IConstants.STKUOM));
				linemap.put(IDBConstants.ITEMMST_REMARK2, (String) lineArr
						.get(IDBConstants.ITEMMST_REMARK2));
				linemap.put(IDBConstants.ITEMMST_REMARK3, (String) lineArr
						.get(IDBConstants.ITEMMST_REMARK3));
				linemap.put(IDBConstants.ITEMMST_REMARK4, (String) lineArr
						.get(IDBConstants.ITEMMST_REMARK4));
				linemap.put(IDBConstants.ITEMMST_REMARK1, (String) lineArr
						.get(IDBConstants.ITEMMST_REMARK1));
				linemap.put(IConstants.ISKITTING, lineArr
						.get(IConstants.ISKITTING));
				linemap.put(IConstants.ISACTIVE, lineArr
						.get(IConstants.ISACTIVE));
			
				flag = process_Wms_CountSheet(linemap);
			
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
			
			result = "<font color=\"red\">" + e.getMessage() + "</font>";
		}

		request.getSession().setAttribute("ITEMRESULT", result);
		response.sendRedirect("jsp/importItemExcelSheet.jsp?");

	}

	private boolean process_Wms_CountSheet(Map map) throws Exception {
		MLogger.log(1, this.getClass() + " process_Wms_CountSheet()");
		boolean flag = false;

		WmsTran tran = new WmsUploadItemSheet();
		flag = tran.processWmsTran(map);
		MLogger.log(-1, this.getClass() + " process_Wms_CountSheet()");
		return flag;
	}
}
