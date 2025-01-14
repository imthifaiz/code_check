package com.track.servlet;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.UserTransaction;

import com.track.constants.IConstants;
import com.track.constants.IDBConstants;
import com.track.dao.MovHisDAO;
import com.track.db.util.CountSheetDownloaderUtil;
import com.track.gates.DbBean;
import com.track.tran.WmsTran;
import com.track.tran.WmsUploadAppDisplay;
import com.track.tran.WmsUploadCountSheet;
import com.track.tran.WmsUploadInvSheet;
import com.track.util.DateUtils;
import com.track.util.MLogger;
import com.track.util.StrUtils;
import com.oreilly.servlet.MultipartRequest;

/**
 * Servlet implementation class ImportInvServlet
 */
public class ImportInvServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String CONTENT_TYPE = "text/html; charset=windows-1252";
	/**
	 * @see HttpServlet#HttpServlet()
	 */
	String action = "";
	String PLANT = "";
	String login_user = "";
	String sys_date = "";
	String StrFileName = "";
	String orgFilePath = "";
	String StrSheetName = "";
	String result = "";
	String divertTo = "";

	public ImportInvServlet() {
		super();
	}

	private MLogger mlogger = new MLogger();

	public HashMap<String, String> populateMapData(String companyCode,
			String userCode) {
		HashMap<String, String> loggerDetailsHasMap = new HashMap<String, String>();
		loggerDetailsHasMap.put(MLogger.COMPANY_CODE, companyCode);
		loggerDetailsHasMap.put(MLogger.USER_CODE, userCode);
		return loggerDetailsHasMap;

	}

	public void setMapDataToLogger(HashMap<String, String> dataForLogging) {
		this.mlogger.setLoggerConstans(dataForLogging);
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
			}else if (action.equalsIgnoreCase("importDisplayCountSheet")) {
				onImportDisplayCountSheet(request, response);
			} else if (action.equalsIgnoreCase("confirmCountSheet")) {
				onConfirmCountSheet(request, response);
			} else if (action.equalsIgnoreCase("confirmDisplayCountSheet")) {
				onConfirmDisplayCountSheet(request, response);
			} else if (action.equalsIgnoreCase("downloadInvTemplate")) {
				onDownloadInvTemplate(request, response);
			}else if (action.equalsIgnoreCase("downloadInvDisplay")) {
				onDownloadInvDisplay(request, response);
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

	private void onDownloadInvTemplate(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException,
			Exception {

		String path = DbBean.DownloadInvTemplate;

		response.setContentType("application/vnd.ms-excel");
		response.setHeader("Content-Disposition",
				"attachment; filename=\"InvMstData.xls\"");
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
	
	private void onDownloadInvDisplay(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException,
			Exception {

		String path = DbBean.DownloadInvDisplay;

		response.setContentType("application/vnd.ms-excel");
		response.setHeader("Content-Disposition",
				"attachment; filename=\"Order_DisplayQuantity.xls\"");
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
	

	private void onImportCountSheet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException,
			Exception {
		String PLANT = (String) request.getSession().getAttribute("PLANT");
		System.out
				.println("********************* onImportCountSheet **************************");
		String result = "";
		ArrayList alRes = new ArrayList();

		try {
			try {

				MultipartRequest mreq = new MultipartRequest(request,
						DbBean.CountSheetUploadPath, 2048000);

			} catch (Exception eee) {
				
				throw eee;

			}

			request.getSession().setAttribute("IMPINV_RESULT", null);
			// get the sheet name
			String filename = "";
			File f = new File(StrFileName);
			filename = f.getName();
			f = null;

			// folder
			StrFileName = DbBean.CountSheetUploadPath + filename;

			
			System.out.println("After conversion Import File  *********:"
					+ StrFileName);

			CountSheetDownloaderUtil util = new CountSheetDownloaderUtil();

			alRes = util.downloadInventorySheetData(PLANT, StrFileName,
					StrSheetName);
			String ValidNumber ="",NOOFORDER="";
			for (int j = 0; j < alRes.size(); j++) {
			Map mValidNumber=(Map)alRes.get(j);
			ValidNumber = (String)mValidNumber.get("ValidNumber");
			}
			
			int index = alRes.size() - 1; 
			alRes.remove(index);
			
			if(ValidNumber.equalsIgnoreCase(""))
			{
			if (alRes.size() > 0) {
				mlogger.info("Data Imported Successfully");
				result = "<font color=\"green\">Data Imported Successfully,Click on Confirm Buttton to upload data.</font>";
			}
			else
			{
				mlogger.info("Data Imported Successfully,You have reached the limit");
				result = "<font color=\"red\">You have reached the limit of "+NOOFORDER+" order's you can create</font>";
			}
			} else {
				throw new Exception(
						"no data found  for given data in the excel");
			}

		} catch (Exception e) {
			
			result = "<font color=\"red\">" + e.getMessage() + "</font>";
		}

		System.out.println("Setting into session ");

		request.getSession().setAttribute("INVRESULT", result);
		request.getSession().setAttribute("IMPINV_RESULT", alRes);
		//       
		response.sendRedirect("../import/inventoryexcel?ImportFile="
				+ orgFilePath + "&SheetName=" + StrSheetName + "");

	}
	
	private void onImportDisplayCountSheet(HttpServletRequest request,
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
			request.getSession().setAttribute("IMPINV_RESULT", null);
			String filename = "";
			File f = new File(StrFileName);
			filename = f.getName();
			f = null;
			StrFileName = DbBean.CountSheetUploadPath + filename;
			System.out.println("After conversion Import File  *********:"+ StrFileName);
			
			CountSheetDownloaderUtil util = new CountSheetDownloaderUtil();
			
			alRes = util.downloadInventoryDisplaySheetData(PLANT, StrFileName,StrSheetName);
			String ValidNumber ="",NOOFORDER="";
			for (int j = 0; j < alRes.size(); j++) {
				Map mValidNumber=(Map)alRes.get(j);
				ValidNumber = (String)mValidNumber.get("ValidNumber");
			}
			int index = alRes.size() - 1; 
			alRes.remove(index);
			if(ValidNumber.equalsIgnoreCase(""))
			{
				if (alRes.size() > 0) {
					mlogger.info("Data Imported Successfully");
					result = "<font color=\"green\">Data Imported Successfully,Click on Confirm Buttton to upload data.</font>";
				}
				else
				{
					mlogger.info("Data Imported Successfully,You have reached the limit");
					result = "<font color=\"red\">You have reached the limit of "+NOOFORDER+" order's you can create</font>";
				}
			} else {
				throw new Exception(
						"no data found  for given data in the excel");
			}
		} catch (Exception e) {
			result = "<font color=\"red\">" + e.getMessage() + "</font>";
		}
		System.out.println("Setting into session ");
		request.getSession().setAttribute("INVRESULT", result);
		request.getSession().setAttribute("IMPINV_RESULT", alRes);
		response.sendRedirect("../import/orderdisplaymaxqty?ImportFile="
				+ orgFilePath + "&SheetName=" + StrSheetName + "");
		
	}
	
	@SuppressWarnings("unchecked")
	private void onConfirmDisplayCountSheet(HttpServletRequest request,
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
			ArrayList al = (ArrayList) request.getSession().getAttribute("IMPINV_RESULT");
			
			Map linemap = new HashMap();
			for (int iCnt = 0; iCnt < al.size(); iCnt++) {
				Map lineArr = (Map) al.get(iCnt);
				if(flag){
					lineArr.put("LOGIN_USER", _login_user);
					linemap.put(IDBConstants.PLANT, PLANT);
					linemap.put("LOGIN_USER", _login_user);
					linemap.put("CUSTNO", (String) lineArr.get("CUSTNO"));
					linemap.put(IDBConstants.ITEM, (String) lineArr.get(IDBConstants.ITEM));
					linemap.put(IDBConstants.ITEM_DESC, (String) lineArr.get(IDBConstants.ITEM_DESC));
					linemap.put("ORDER_QTY", (String) lineArr.get("ORDER_QTY"));
					linemap.put("MAX_ORDER_QTY", (String) lineArr.get("MAX_ORDER_QTY"));
//					linemap.put("MAX_ORDER_QTY", String.valueOf("MAX_ORDER_QTY"));					
					flag = process_Wms_DisplayCountSheet(linemap);
				}
			}
			
			if (flag == true) {
				DbBean.CommitTran(ut);
				request.getSession().removeAttribute("IMPINV_RESULT");
				flag = true;
				result = "<font color=\"green\">Order Display/Max Quantity Confirmed Successfully</font>";
			} else {
				DbBean.RollbackTran(ut);
				flag = false;
				result = "<font color=\"red\">Error in Confirming Order Display/Max Quantity</font>";
			}
			
		} catch (Exception e) {
			flag = false;
			DbBean.RollbackTran(ut);
			result = "<font color=\"red\">"+ e.getMessage() +"</font>";
			request.getSession().setAttribute("INVRESULT", result);
		}
		
		request.getSession().setAttribute("INVRESULT", result);
		response.sendRedirect("../import/orderdisplaymaxqty?");
	}

	@SuppressWarnings("unchecked")
	private void onConfirmCountSheet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException,
			Exception {
		String result = "";
		ArrayList alRes = new ArrayList();
		;
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
					"IMPINV_RESULT");

			Map linemap = new HashMap();
			for (int iCnt = 0; iCnt < al.size(); iCnt++) {
				Map lineArr = (Map) al.get(iCnt);
				String uom=(String) lineArr.get(IConstants.IN_UNITMO);
				String UOMQTY="1";
				Hashtable<String, String> htTrand1 = new Hashtable<String, String>();
				MovHisDAO movHisDao1 = new MovHisDAO();
				ArrayList getuomqty = movHisDao1.selectForReport("select ISNULL(QPUOM,1) as UOMQTY from "+PLANT+"_UOM where UOM='"+uom+"'",htTrand1);
				if(getuomqty.size()>0)
				{
				Map mapval = (Map) getuomqty.get(0);
				UOMQTY=(String)mapval.get("UOMQTY");
				}
				String stkqty1 = (String) lineArr.get(IConstants.QTY);
				double invumqty = Double.valueOf(stkqty1) * Double.valueOf(UOMQTY);
				if(flag){
					
					lineArr.put("LOGIN_USER", _login_user);
					linemap.put(IDBConstants.PLANT, PLANT);
					linemap.put("LOGIN_USER", _login_user);
					linemap.put(IConstants.UOM, uom);
					linemap.put(IDBConstants.LOC, (String) lineArr.get("LOC"));
					linemap.put(IDBConstants.BATCH, (String) lineArr.get(IDBConstants.BATCH));
					linemap.put(IDBConstants.ITEM, (String) lineArr.get(IDBConstants.ITEM));
					linemap.put(IConstants.QTY, String.valueOf(invumqty));
					linemap.put("IMPORTQTY", stkqty1);
					linemap.put(IConstants.EXPIREDATE, (String) lineArr.get(IConstants.EXPIREDATE));
					linemap.put(IConstants.EXPIREDATE, (String) lineArr.get(IConstants.EXPIREDATE));
					linemap.put("AVERAGEUNITCOST", (String) lineArr.get("AVERAGEUNITCOST"));
					
					flag = process_Wms_CountSheet(linemap);
				}
				

			}
			//if (!flag) {

			//	throw new Exception("Error in Confirming Count Sheet");
			//}

			if (flag == true) {
				DbBean.CommitTran(ut);
				request.getSession().removeAttribute("IMPINV_RESULT");
				flag = true;
				result = "<font color=\"green\">Inventory Confirmed Successfully</font>";
			} else {
				DbBean.RollbackTran(ut);
				flag = false;
				result = "<font color=\"red\">Error in Confirming Inventory </font>";
			}

		} catch (Exception e) {
			flag = false;
			DbBean.RollbackTran(ut);
			result = "<font color=\"red\">"+ e.getMessage() +"</font>";
			request.getSession().setAttribute("INVRESULT", result);
			
		}

		request.getSession().setAttribute("INVRESULT", result);
		response.sendRedirect("../import/inventoryexcel?");

	}

	private boolean process_Wms_CountSheet(Map map) throws Exception {
		MLogger.log(1, this.getClass() + " process_Wms_InvSheet()");
		boolean flag = false;

		WmsTran tran = new WmsUploadInvSheet();
		flag = tran.processWmsTran(map);
		MLogger.log(-1, this.getClass() + " process_Wms_InvSheet()");
		return flag;
	}
	
	private boolean process_Wms_DisplayCountSheet(Map map) throws Exception {
		MLogger.log(1, this.getClass() + " process_Wms_InvSheet()");
		boolean flag = false;
		
		WmsTran tran = new WmsUploadAppDisplay();
		flag = tran.processWmsTran(map);
		MLogger.log(-1, this.getClass() + " process_Wms_InvSheet()");
		return flag;
	}
}
